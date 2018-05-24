package fr.doofapp.doof.ClassMetier;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Meal implements Serializable {

    private String photo;
    private int price;
    private String name;
    private String linkMeal;
    private String date;
    private String description;
    private String adress;
    private LatLng latlng;
    private Boolean contain;
    private int nbPart;

    private Boolean isComment;
    private int sold;
    private  Boolean isSell;
    private double note;

    public Meal(String photo, int price, String name, String linkMeal, String date, String description, String adress, LatLng latlng){
        this.photo = photo;
        this.price = price;
        this.name = name;
        this.date = date;
        this.linkMeal = linkMeal;
        this.description = description;
        this.adress = adress;
        this.latlng = latlng;
    }

    public String getPhoto() {return photo;}
    public void setPhoto(String photo) {this.photo = photo;}

    public int getPrice() {return price;}
    public void setPrice(int price) {this.price = price;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getLinkMeal() {return linkMeal;}
    public void setLinkMeal(String linkMeal) {this.linkMeal = linkMeal;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getAdress() {return adress;}
    public void setAdress(String adress) {this.adress = adress;}

    public LatLng getLatlng() {return latlng;}
    public void setLatlng(LatLng latlng) {this.latlng = latlng;}

    public Boolean getContain() {return contain;}
    public void setContain(Boolean contain) {this.contain = contain;}

    public int getNbPart() {return nbPart;}
    public void setNbPart(int nbPart) {this.nbPart = nbPart;}

    public Boolean getComment() {return isComment;}
    public void setComment(Boolean comment) {isComment = comment;}

    public int getSold() {return sold;}
    public void setSold(int sold) {this.sold = sold;}

    public Boolean getSell() {return isSell;}
    public void setSell(Boolean sell) {isSell = sell;}

    public double getNote() {return note;}
    public void setNote(double note) {this.note = note;}
}
