package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min = 1, max = 100)
    private String message;

    private LocalDateTime sentDateTime;

    @ManyToOne
    @JoinColumn(name = "from_user_id", nullable = false)
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    private User toUser;

    public Message() {}

    public Message(long id, String message, LocalDateTime sentDateTime, User fromUser, User toUser) {
        this.id = id;
        this.message = message;
        this.sentDateTime = sentDateTime;
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}

    public LocalDateTime getSentDateTime() {return sentDateTime;}

    public void setSentDateTime(LocalDateTime sentDateTime) {this.sentDateTime = sentDateTime;}

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }
}
