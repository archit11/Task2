package com.theacecoder.task2;

/**
 * Created by Archit on 27-07-2016.
 */
public class Chat {

    private String message;
    private String author;

    Chat(String message, String author) {
        this.message = message;
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}
