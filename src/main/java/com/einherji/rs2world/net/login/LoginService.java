package com.einherji.rs2world.net.login;

import com.einherji.rs2world.net.clients.Client;
import com.einherji.rs2world.util.ArrayWrapper;
import com.einherji.rs2world.util.ThreadSafeArrayWrapper;
import org.springframework.stereotype.Component;

import static com.einherji.rs2world.net.login.LoginResponseCodes.WORLD_FULL;

@Component
public class LoginService {

    private static final int LOGIN_QUEUE_SIZE = 10;

    private final ArrayWrapper<Client> loginQueue;

    public LoginService() {
        loginQueue = ThreadSafeArrayWrapper.wrap(new Client[LOGIN_QUEUE_SIZE]);
    }

    public void add(Client client) {
        int position = loginQueue.place(client);
        if (position == -1) {
            throw new LoginException(WORLD_FULL);
        }
    }

    public void cycle() {

    }
}
