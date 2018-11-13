package com.xiaomi.mall.common.result;

/**
 * @Author lcyang
 * @Date 2018/11/11 9:51
 * @Description
 */
public enum ExceptionMsg {
    SUCCESS("000", "操作成功"),
    FAILED("111","操作失败"),
    ParamError("001", "参数错误！"),

    LoginNameOrPassWordError("002", "用户名或者密码错误！"),
    UserNameUsed("003","该用户名已存在"),
    PassWordError("004","密码输入错误"),
    UserNameLengthLimit("005","用户名长度超限"),
    LoginNameNotExists("006","该用户未注册"),
    ;
    private ExceptionMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }

}
