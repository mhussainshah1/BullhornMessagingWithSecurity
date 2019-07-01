package com.example.demo.business.entities;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "User_Data")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty
    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @NotEmpty
    //@ValidPassword
    @Column(name = "password")
    private String password;

    @NotEmpty
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty
    @Column(name = "last_name")
    private String lastName;

    @AssertTrue
    @Column(name = "enabled")
    private boolean enabled;

    @NotEmpty
    @Column(name = "username")
    private String username;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    //for Messages
    @OneToMany(mappedBy = "user")
    private Set<Message> messages;

    //For followers and following
    @ManyToMany
    private Set<User> followings;

    @ManyToMany(mappedBy = "followings")
    private Set<User> followers;

    public User() {
        followers = new HashSet<>();
        followings = new HashSet<>();
    }

    public User(@NotEmpty @Email String email,
                @NotEmpty String password,
                @NotEmpty String firstName,
                @NotEmpty String lastName,
                @AssertTrue boolean enabled,
                @NotEmpty String username) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public Set<User> getFollowings() {
        return followings;
    }

    public void setFollowings(Set<User> followings) {
        this.followings = followings;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

   /* public void addFollower(User follower) {
        followers.add(follower);
        follower.followings.add(this);
    }

    public void removeFollower(User follower) {
        followers.remove(follower);
        follower.followings.remove(this);
    }

    public void addFollowing(User followed) {
        followed.addFollower(this);
    }

    public void removeFollowing(User followed) {
        followed.removeFollower(this);
    }*/
}
