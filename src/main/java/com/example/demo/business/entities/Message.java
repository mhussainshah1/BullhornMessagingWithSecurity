package com.example.demo.business.entities;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Message object should have at least:
 * -------------------------------------
 * id
 * title
 * content
 * postedDateTime
 * postedBy
 */

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    @Size(min = 4)
    private String title;

    @NonNull
    @Size(min = 10)
    private String content;

    //@DateTimeFormat(pattern = "dd MMM yyyy, hh:mm")
    private LocalDateTime postedDateTime;

    @NonNull
    private String picturePath;

    @ManyToOne
    private User user;

    public Message() {
        picturePath = "";
        postedDateTime = LocalDateTime.now();
        user = new User();
    }

    public Message(@Size(min = 4) String title, @Size(min = 10) String content, LocalDateTime postedDateTime, String picturePath, User user) {
        this.title = title;
        this.content = content;
        this.postedDateTime = postedDateTime;
        this.picturePath = picturePath;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPostedDateTime() {
        return postedDateTime;
    }

    public void setPostedDateTime(LocalDateTime postedDateTime) {
        this.postedDateTime = postedDateTime;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

   /* @Override
    public String toString() {
        String string = "[Title = " + title +
                ", Content = "+ content +
                ", Posted Date = " + postedDateTime +
                ", Posted By =" + user.getFirstName() +
                ", Picture Path = " + picturePath +"]";
        return  string;
    }*/
}
