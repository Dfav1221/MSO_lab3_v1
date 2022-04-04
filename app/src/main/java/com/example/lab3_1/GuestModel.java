package com.example.lab3_1;

public class GuestModel {

    private int id;
    private String message;
    private String SEND_AT;

    public GuestModel(int id,String message,String SEND_AT){
        this.id = id;
        this.message = message;
        this.SEND_AT = SEND_AT;
    }
    public GuestModel(){}

    @Override
    public String toString(){
        return "GuestModel{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", date='" + SEND_AT + '\'' +
                '}';
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSEND_AT() {
        return SEND_AT;
    }

    public void setSEND_AT(String SEND_AT) {
        this.SEND_AT = SEND_AT;
    }
}
