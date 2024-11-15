package com.makechi.pesa.tally.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "transactions",
        foreignKeys = @ForeignKey(
                entity = Goal.class,
                parentColumns = "id",
                childColumns = "goal",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("goal")}
)
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Integer goal;
    private String date;
    private double amount;
    private String type;

    public Transaction(Integer goal, String date, double amount, String type) {
        this.goal = goal;
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
