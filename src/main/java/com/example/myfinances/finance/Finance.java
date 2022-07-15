package com.example.myfinances.finance;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class Finance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private Float amount;
    private String description;

    private LocalDate dot; //date of transaction

    public Finance() {
    }

    public Finance(String type, float amount, String description, LocalDate dot) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.dot = dot;
    }

    public Finance(Long id, String type, float amount, String description, LocalDate dot) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.dot = dot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDot() {return dot;}

    public void setDot(LocalDate dot) {this.dot = dot;}
}
