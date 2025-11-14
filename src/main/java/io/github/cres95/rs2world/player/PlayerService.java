package io.github.cres95.rs2world.player;

import io.github.cres95.rs2world.net.login.LoginException;
import io.github.cres95.rs2world.net.login.LoginResponseCode;
import io.github.cres95.rs2world.util.ArrayWrapper;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class PlayerService {

    private final ArrayWrapper<Player> players;

    public PlayerService() {
        this.players = ArrayWrapper.wrapConcurrent(new Player[2000]);
    }

    public Player register(Consumer<PlayerInitializer> customizer) {
        int slot = players.findEmpty();
        if (slot == -1) {
            throw LoginException.forResponse(LoginResponseCode.WORLD_FULL);
        }
        PlayerInitializer initializer = PlayerInitializer.withId(slot);
        customizer.accept(initializer);
        Player player = initializer.build();
        players.set(slot, player);
        return player;
    }

    public void cycle() {
        players.forEach(player -> {

        });
    }
}
