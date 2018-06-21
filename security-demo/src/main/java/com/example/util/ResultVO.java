package com.example.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author SongQingWei
 * @description 封装返回对象
 * @date 2018年04月19 14:18
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVO<T> implements Serializable {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 返回对象
     */
    private T data;

    public ResultVO() {

    }

    private ResultVO(Integer code) {
        this.code = code;
    }

    private ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ResultVO(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    private ResultVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code.equals(0);
    }

    public static <T> ResultVO<T> createBySuccess() {
        return new ResultVO<>(0);
    }

    public static <T> ResultVO<T> createBySuccessMessage(String msg) {
        return new ResultVO<>(0, msg);
    }

    public static <T> ResultVO<T> createBySuccess(T data) {
        return new ResultVO<>(0, data);
    }

    public static <T> ResultVO<T> createBySuccess(String msg, T data) {
        return new ResultVO<>(0, msg, data);
    }

    public static <T> ResultVO<T> createByError() {
        return new ResultVO<>(1);
    }

    public static <T> ResultVO<T> createByErrorMessage(String errorMessage) {
        return new ResultVO<>(1, errorMessage);
    }

    public static <T> ResultVO<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ResultVO<>(errorCode, errorMessage);
    }
}
