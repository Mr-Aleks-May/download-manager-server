package com.mralexmay.projects.download_manager.server.commons.exception;

public class UserAlreadyExistsException extends ApiException {
    private final Long userId;


    public UserAlreadyExistsException(Long userId) {
        this.userId = userId;
    }


    public Long getUserId() {
        return userId;
    }
}
