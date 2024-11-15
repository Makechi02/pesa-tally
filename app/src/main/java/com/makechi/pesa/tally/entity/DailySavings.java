package com.makechi.pesa.tally.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "daily_savings")
public class DailySavings {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String date;
    private double amount;
    private double targetAmount;
    private boolean isCompleted;

    public DailySavings(String date, double amount, double targetAmount, boolean isCompleted) {
        this.date = date;
        this.amount = amount;
        this.targetAmount = targetAmount;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
