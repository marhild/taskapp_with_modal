package com.example.taskapp.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author platoiscoding.com
 */
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @NotEmpty
    private String title;

    @Lob
    @NotEmpty
    @Type(type = "org.hibernate.type.TextType") //heroku config
    private String content;

    //status may not be null
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Status status;

    public Task() {
        this.status = Status.OPEN;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void closeTask(){
        this.status = Status.CLOSED;
    }

    public void reopenTask(){
        this.status = Status.REOPENED;
    }
}
