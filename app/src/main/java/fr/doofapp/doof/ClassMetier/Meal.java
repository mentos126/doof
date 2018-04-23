package fr.doofapp.doof.ClassMetier;

public class Meal {

    private String photo;
    private int price;
    private String name;

    public Meal(String photo, int price, String name){
        this.photo = photo;
        this.price = price;
        this.name = name;
    }

    public String getPhoto() {return photo;}
    public void setPhoto(String photo) {this.photo = photo;}

    public int getPrice() {return price;}
    public void setPrice(int price) {this.price = price;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
