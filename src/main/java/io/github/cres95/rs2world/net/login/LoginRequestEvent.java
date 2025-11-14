package io.github.cres95.rs2world.net.login;

import org.springframework.context.ApplicationEvent;

public class LoginRequestEvent extends ApplicationEvent {

    private final LoginRequest request;

    public LoginRequestEvent(Object source, LoginRequest request) {
        super(source);
        this.request = request;
    }

    public LoginRequest getRequest() {
        return request;
    }
}
