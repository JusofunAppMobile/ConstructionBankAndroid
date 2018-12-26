package com.jusfoun.baselibrary.base;

import java.io.Serializable;

/**
 * Created by wang on 2016/11/8.
 */

public abstract class BaseModel implements Serializable {
    protected int rescode;

    protected int result;

    protected String resmsg;

    protected String message;

    protected String error;

    public int total;

    public String msg;

    public int getCode() {
        return rescode;
    }

    public void setCode(int code) {
        this.rescode = code;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return resmsg;
    }

    public void setMsg(String msg) {
        this.resmsg = msg;
        this.msg = msg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean success() {
        return result == 0;
    }

}
