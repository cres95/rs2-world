package io.github.cres95.rs2world.net.login;

import java.util.HashMap;
import java.util.Map;

public class LoginException extends RuntimeException {

    private static final Map<Integer, LoginException> exceptionMapping = new HashMap<>();

    private final int responseCode;

    private LoginException(int responseCode) {
        this.responseCode = responseCode;
    }

    public static LoginException forResponse(int responseCode) {
        LoginException ex = exceptionMapping.get(responseCode);
        if (ex == null) {
            ex = new LoginException(responseCode);
            exceptionMapping.put(responseCode, ex);
        }
        return ex;
    }

    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        //LoginExceptions are re-usable for control-flow efficiency
        //The stack trace will not be needed and can be skipped
        return this;
    }
}
