package org.yyuze.ret;

/**
 * @Author: yanghanbo
 * @Date: 7/15/2020 12:23 PM
 */
public enum ExecuteResultEnum implements BaseResultCodeEnum {

    FAIL("Command {} executed failed", -1);

    private String msg;

    private int code;

    ExecuteResultEnum(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public static ExecuteResultEnum buildCmdFail(String cmd){
        ExecuteResultEnum fail = FAIL;
        fail.setMsg(fail.getMsg().replace("{}", cmd));
        return fail;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
