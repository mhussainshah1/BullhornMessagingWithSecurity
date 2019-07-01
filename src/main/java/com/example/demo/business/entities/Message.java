package com.example.demo.business.entities;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Message object should have at least:
 * -------------------------------------
 * id
 * title
 * content
 * postedDate
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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate postedDate;

    @NonNull
    //@Size(min = 4)
    private String picturePath;

    @ManyToOne
    private User user;

    public Message() {
        picturePath = "";
        postedDate = LocalDate.now();
        user = new User();
    }

    public Message(@Size(min = 4) String title, @Size(min = 10) String content, LocalDate postedDate, String picturePath, User user) {
        this.title = title;
        this.content = content;
        this.postedDate = postedDate;
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

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
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
                ", Posted Date = " + postedDate +
                ", Posted By =" + user.getFirstName() +
                ", Picture Path = " + picturePath +"]";
        return  string;
    }*/
}
