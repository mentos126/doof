package fr.doofapp.doof.ClassMetier;

import java.util.ArrayList;

public class Profile {

    private String photo;
    private double noteTotale;
    private double notaAccueil;
    private double noteProprete;
    private double noteCuisine;
    private String nom;
    private String prenom;
    private String birth;
    private int age;
    private ArrayList<Comment> comments = new ArrayList<>();

    public Profile(String nom, String prenom, String birth, int age, String photo,
                   double noteTotale, double notaAccueil, double noteProprete, double noteCuisine) {
        this.nom = nom;
        this.prenom = prenom;
        this.birth = birth;
        this.age = age;
        this.notaAccueil = notaAccueil;
        this.photo = photo;
        this.noteTotale = noteTotale;
        this.noteProprete = noteProprete;
        this.noteCuisine = noteCuisine;
    }

    public String getNom() {return nom;}
    public void setNom(String nom) {this.nom = nom;}

    public String getPrenom() {return prenom;}
    public void setPrenom(String prenom) {this.prenom = prenom;}

    public String getBirth() {return birth;}
    public void setBirth(String birth) {this.birth = birth;}

    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}

    public ArrayList<Comment> getComments() {return comments;}
    public void addComments(Comment c) {this.comments.add(c);}
    public void removeComments(Comment c) {this.comments.remove(c);}

    public String getPhoto() {return photo;}
    public void setPhoto(String photo) {this.photo = photo;}

    public double getNoteTotale() {return noteTotale;}
    public void setNoteTotale(double noteTotale) {this.noteTotale = noteTotale;}

    public double getNotaAccueil() {return notaAccueil;}
    public void setNotaAccueil(double notaAccueil) {this.notaAccueil = notaAccueil;}

    public double getNoteProprete() {return noteProprete;}
    public void setNoteProprete(double noteProprete) {this.noteProprete = noteProprete;}

    public double getNoteCuisine() {return noteCuisine;}
    public void setNoteCuisine(double noteCuisine) {this.noteCuisine = noteCuisine;}
}
