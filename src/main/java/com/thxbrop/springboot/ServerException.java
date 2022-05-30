package com.thxbrop.springboot;

public class ServerException extends RuntimeException {
    public static final ServerException REGISTER_EMAIL_EXIST = new ServerException("Email has been already registered", 501);
    public static final ServerException LOGIN_EMAIL_EXIST = new ServerException("Email has not been registered", 502);
    public static final ServerException INVITE_EXIST = new ServerException("You have invited this user", 503);
    public static final ServerException INVITE_NOT_EXIST = new ServerException("Invite is not existed", 504);
    public static final ServerException CONVERSATION_NOT_EXIST = new ServerException("Conversation is not existed", 505);
    public static final ServerException INVITE_APPROVED = new ServerException("Invite has been approved", 506);
    public static final ServerException INVITE_REFUSED = new ServerException("Invite has been refused", 507);
    public static final ServerException PERMISSION_DENIED = new ServerException("Permission denied", 508);
    public static final ServerException MEMBER_EXIST = new ServerException("The user is already a member of this conversation", 509);
    public static final ServerException USER_NOT_EXIST = new ServerException("The user is not existed", 510);
    public static final ServerException FROM_USER_NOT_EXIST = new ServerException("The current user is not existed", 511);
    public static final ServerException TO_USER_NOT_EXIST = new ServerException("The target user is not existed", 512);
    public static final ServerException WRONG_PASSWORD = new ServerException("The password is incorrect", 513);
    public static final ServerException INVALIDATE_TOKEN = new ServerException("The token is invalidate", 514);

    private final int code;

    public ServerException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    public <T> Result<T> asResult() {
        return new Result<>(getMessage(), code);
    }
}
