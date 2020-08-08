package org.yyuze.bean;

import org.yyuze.ret.BaseResultCodeEnum;

public class ExecuteResult {

    private static final int SUCCESS_CODE = 0;

    private int code;

    private String msg;

    private boolean success;

    public ExecuteResult(){}

    public ExecuteResult(BaseResultCodeEnum res){
        this.code = res.getCode();
        this.msg = res.getMsg();
        this.success = this.code == SUCCESS_CODE;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "{code : " + this.code + ", msg : " + this.msg+ "}";
    }
}
