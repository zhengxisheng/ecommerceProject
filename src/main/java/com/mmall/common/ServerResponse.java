package com.mmall.common;

import com.sun.org.apache.regexp.internal.RE;

import java.io.Serializable;

/**
 * Created by fanlinglong on 2018/1/6.
 */
public class ServerResponse <T> implements Serializable{

    private int status;
    private String msg;
    private T data;

    private ServerResponse (int status){
        this.status=status;
    }
    private ServerResponse (int status,String msg){
        this.status=status;
        this.msg=msg;
    }
    private ServerResponse (int status,String msg,T data){
        this.status=status;
        this.msg=msg;
        this.data=data;
    }
    private ServerResponse (int status,T data){
        this.status=status;
        this.data=data;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMsg(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode());
    }

    public static <T> ServerResponse<T> createByErrorMsg(String msg){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),msg);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String msg){
        return new ServerResponse<T>(errorCode,msg);
    }

}
