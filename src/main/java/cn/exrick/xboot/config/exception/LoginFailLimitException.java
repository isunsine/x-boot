package cn.exrick.xboot.config.exception;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

/**
 * @author Exrickx
 */
public class LoginFailLimitException extends InternalAuthenticationServiceException {

    private String msg;

    public LoginFailLimitException(String msg){
        super(msg);
        this.msg = msg;
    }

    public LoginFailLimitException(String msg, Throwable t) {
        super(msg, t);
        this.msg = msg;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }

}
