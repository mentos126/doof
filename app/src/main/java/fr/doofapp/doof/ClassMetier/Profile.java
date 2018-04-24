package fr.doofapp.doof.ClassMetier;

import java.io.Serializable;
import java.util.ArrayList;

public class Profile implements Serializable {

    private String photo;
    private double noteTotal;
    private double notaHome;
    private double noteCleanless;
    private double noteCooked;
    private String familyName;
    private String name;
    private String birth;
    private int age;
    private String adress;
    private String phone;
    private ArrayList<Comment> comments = new ArrayList<>();

    public Profile(String nom, String prenom, String birth, int age, String photo,
                   double noteTotal, double notaAccueil, double noteProprete, double noteCuisine,
                   String adress, String phone) {
        this.familyName = nom;
        this.name = prenom;
        this.birth = birth;
        this.age = age;
        this.notaHome = notaAccueil;
        this.photo = photo;
        this.noteTotal = noteTotal;
        this.noteCleanless = noteProprete;
        this.noteCooked = noteCuisine;
        this.phone = phone;
        this.adress = adress;
    }

    public String getFamilyName() {return familyName;}
    public void setFamilyName(String familyName) {this.familyName = familyName;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getBirth() {return birth;}
    public void setBirth(String birth) {this.birth = birth;}

    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}

    public ArrayList<Comment> getComments() {return comments;}
    public void addComments(Comment c) {this.comments.add(c);}
    public void removeComments(Comment c) {this.comments.remove(c);}

    public String getPhoto() {return photo;}
    public void setPhoto(String photo) {this.photo = photo;}

    public double getNoteTotal() {return noteTotal;}
    public void setNoteTotal(double noteTotal) {this.noteTotal = noteTotal;}

    public double getNotaHome() {return notaHome;}
    public void setNotaHome(double notaHome) {this.notaHome = notaHome;}

    public double getNoteCleanless() {return noteCleanless;}
    public void setNoteCleanless(double noteCleanless) {this.noteCleanless = noteCleanless;}

    public double getNoteCooked() {return noteCooked;}
    public void setNoteCooked(double noteCooked) {this.noteCooked = noteCooked;}

    public String getAdress() {return adress;}
    public void setAdress(String adress) {this.adress = adress;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

}
