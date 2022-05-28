package com.thxbrop.springboot;

public class ServerException extends RuntimeException {
    public static final ServerException REGISTER_EMAIL_EXIST = new ServerException("Email has been already registered");
    public static final ServerException LOGIN_EMAIL_EXIST = new ServerException("Email has not been registered");
    public static final ServerException INVITE_EXIST = new ServerException("You have invited this user");
    public static final ServerException INVITE_NOT_EXIST = new ServerException("Invite is not existed");
    public static final ServerException CONVERSATION_NOT_EXIST = new ServerException("Conversation is not existed");
    public static final ServerException INVITE_APPROVED = new ServerException("Invite has been approved");
    public static final ServerException INVITE_REFUSED = new ServerException("Invite has been refused");
    public static final ServerException PERMISSION_DENIED = new ServerException("Permission denied");
    public static final ServerException MEMBER_EXIST = new ServerException("The user is already a member of this conversation");
    public static final ServerException USER_NOT_EXIST = new ServerException("The user is not existed");
    public static final ServerException FROM_USER_NOT_EXIST = new ServerException("The current user is not existed");
    public static final ServerException TO_USER_NOT_EXIST = new ServerException("The target user is not existed");
    public static final ServerException WRONG_PASSWORD = new ServerException("The password is incorrect");
    public static final ServerException INVALIDATE_TOKEN = new ServerException("The token is invalidate");

    public ServerException(String message) {
        super(message);
    }

    public <T> Result<T> asResult() {
        return new Result<>(getMessage());
    }
}
