package com.mralexmay.projects.download_manager.server.commons.exception;

public class WrongUserCredentialException extends ApiException {
    private Long userId;


    public WrongUserCredentialException() {
    }

    public WrongUserCredentialException(Long userId) {
        this.userId = userId;
    }


    public Long getUserId() {
        return userId;
    }
}
