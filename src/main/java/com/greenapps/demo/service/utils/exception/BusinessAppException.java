/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapps.demo.service.utils.exception;

/**
 *
 * @author danny
 */
public class BusinessAppException extends Exception {

    private String code;
    private String msj;

    public BusinessAppException() {

    }

    public BusinessAppException(String code, String msj) {
        this.code = code;
        this.msj = msj;
    }

    public BusinessAppException(String code, String msj, String message) {
        super(message);
        this.code = code;
        this.msj = msj;
    }

    public BusinessAppException(String code, String msj, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.msj = msj;
    }

    public BusinessAppException(String code, String msj, Throwable cause) {
        super(cause);
        this.code = code;
        this.msj = msj;
    }

    public BusinessAppException(String code, String msj, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.msj = msj;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsj() {
        return msj;
    }

    public void setMsj(String msj) {
        this.msj = msj;
    }

}
