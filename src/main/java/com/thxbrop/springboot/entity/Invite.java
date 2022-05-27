package com.thxbrop.springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Invite {
    public static final int STATE_PENDING = 0;
    public static final int STATE_APPROVED = 1;
    public static final int STATE_REFUSED = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "con_id")
    private int conId;
    @Column(name = "from_id")
    private int fromId;
    @Column(name = "to_id")
    private int toId;
    private int state = STATE_PENDING;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConId() {
        return conId;
    }

    public void setConId(int conId) {
        this.conId = conId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @JsonIgnore
    public boolean isApproved() {
        return state == STATE_APPROVED;
    }
    @JsonIgnore
    public boolean isPending() {
        return state == STATE_PENDING;
    }
    @JsonIgnore
    public boolean isRefused() {
        return state == STATE_REFUSED;
    }
}
