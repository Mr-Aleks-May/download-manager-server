package com.mralexmay.projects.download_manager.server.commons.exception;

public class UserNotFoundException extends ApiException {
    private Long userId;


    public UserNotFoundException() {
    }

    public UserNotFoundException(Long userId) {
        this.userId = userId;
    }


    public Long getUserId() {
        return userId;
    }
}
