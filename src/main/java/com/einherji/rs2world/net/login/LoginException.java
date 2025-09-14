package com.einherji.rs2world.net.login;

public class LoginException extends RuntimeException {

    private final int responseCode;

    public LoginException(int responseCode) {
        super("Encountered login exception with code: " + responseCode);
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
