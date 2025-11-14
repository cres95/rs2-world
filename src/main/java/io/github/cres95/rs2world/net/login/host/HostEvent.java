package io.github.cres95.rs2world.net.login.host;

import io.github.cres95.rs2world.net.login.LoginResponseCode;

public enum HostEvent {

    CONNECTION_ATTEMPT(LoginResponseCode.WAIT_TRY_AGAIN),
    LOGIN_ATTEMPT(LoginResponseCode.LOGIN_ATTEMPTS_EXCEEDED),
    ACCOUNT_CREATION(LoginResponseCode.LOGIN_ATTEMPTS_EXCEEDED);

    private final int errorCode;

    HostEvent(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
