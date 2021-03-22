package com.cst2335.pate0844;

public class Message {
    //class variable
//define variables -> 3
    //string variable called msg
    String msg;
    long id;
    boolean send;
    //long variable called id
    //boolean variable:

    //constructor
    public Message(String msg, long id, boolean send) {
        this.msg = msg;
        this.id = id;
        this.send = send;
    }
    public String getMsg(){
        return msg;
    }
    public long getId(){
        return id;
    }
    public boolean getSend(){
        return send;
    }
}