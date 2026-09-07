package com.example.crm.base;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class Response.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */

//@Deprecated
@Getter @Setter
public class Response<T> {
    /** The data. */
    private T data;

    /** The message. */
    private String message;

    /** The status.*/
    private int status;

    public Response(T data){
        this.data=data;
    }

    public Response(String message){
        this.message = message;
    }

    public Response(String message , int status){
        this.message  = message;
        this.status = status;
    }

    public Response(T data , int status){
        this.data  = data;
        this.status = status;
    }

    public Response(int status){
        this.status = status;
    }
    public Response(T data, String message) {
        this.data = data;
        this.message = message;
    }




    public Response(T data, String message, int status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }
}

