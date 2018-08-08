package com.example.enums;

/**
 * @author SongQingWei
 * @date 2018年07月30 16:36
 */
public enum DcpTypeEnum {
    dcp("dcp/"),
    zip("zip/");

    private String msg;

    DcpTypeEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
