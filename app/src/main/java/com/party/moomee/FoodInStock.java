package com.party.moomee;

public class FoodInStock {

    private String foodName,nameInDatabase;
    private double quantityLeft = 0;
    private QualitativeNoun qualitativeNoun;

    public FoodInStock(String foodName, String nameInDatabase, double quantityLeft, QualitativeNoun qualitativeNoun) {
        this.foodName = foodName;
        this.nameInDatabase = nameInDatabase;
        this.quantityLeft = quantityLeft;
        this.qualitativeNoun = qualitativeNoun;
    }

    public FoodInStock(String foodName, String nameInDatabase, QualitativeNoun qualitativeNoun) {
        this.foodName = foodName;
        this.nameInDatabase = nameInDatabase;
        this.qualitativeNoun = qualitativeNoun;
    }

    public FoodInStock(String foodName, double quantityLeft, QualitativeNoun qualitativeNoun) {
        this.foodName = foodName;
        this.nameInDatabase = foodName;
        this.quantityLeft = quantityLeft;
        this.qualitativeNoun = qualitativeNoun;
    }

    public FoodInStock(String foodName, QualitativeNoun qualitativeNoun) {
        this.foodName = foodName;
        this.nameInDatabase = foodName;
        this.qualitativeNoun = qualitativeNoun;
    }

    public FoodInStock(String foodName, double quantityLeft) {
        this.foodName = foodName;
        this.quantityLeft = quantityLeft;
    }

    public enum QualitativeNoun {
        ถุง , ไม้
    }

    public String getFoodName() {
        return foodName;
    }

    public double getQuantityLeft() {
        return this.quantityLeft;
    }

    public void setQuantityLeft(double quantityLeft) {
        this.quantityLeft = quantityLeft;
    }

    public QualitativeNoun getQualitativeNoun() {
        return qualitativeNoun;
    }

    public void setQualitativeNoun(QualitativeNoun qualitativeNoun) {
        this.qualitativeNoun = qualitativeNoun;
    }

    public String getQualitativeNounAsString() {
        return qualitativeNoun.toString();
    }

    public String getNameInDatabase() {
        return nameInDatabase;
    }

    public void addAmount(double amount) {
        quantityLeft += amount;
    }

    public boolean removeAmount(double amount) {
        if(amount<=this.quantityLeft) {
            quantityLeft -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "\n------ " + nameInDatabase + "------\n" +
                foodName + " " + quantityLeft + " " + getQualitativeNounAsString() + "\n";
    }
}
