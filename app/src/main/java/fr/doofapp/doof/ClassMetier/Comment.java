package fr.doofapp.doof.ClassMetier;

public class Comment {

    private String descriptif;
    private String title;
    private String photo;
    private String linkMeal;
    private String linkUser;
    private String nameUser;
    private String photoUser;
    private double noteTotal;
    private double noteHome;
    private double noteCleanless;
    private double noteCooked;

    public Comment(String descriptif, String title, String photo, String linkMeal, String link, String nameUser, String photoUser, double noteAccueil, double noteProprete, double noteCuisine, double noteTotale) {
        this.descriptif = descriptif;
        this.title = title;
        this.photo = photo;
        this.linkMeal = linkMeal;
        this.linkUser = link;
        this.nameUser = nameUser;
        this.photoUser = photoUser;
        this.noteHome = noteAccueil;
        this.noteCleanless = noteProprete;
        this.noteCooked = noteCuisine;
        this.noteTotal = noteTotale;
    }

    public String getDescriptif() {return descriptif;}
    public void setDescriptif(String descriptif) {this.descriptif = descriptif;}

    public String getPhoto() {return photo;}
    public void setPhoto(String photo) {this.photo = photo;}

    public String getLinkUser() {return linkUser;}
    public void setLinkUser(String link) {this.linkUser = link;}

    public String getNameUser() {return nameUser;}
    public void setNameUser(String nameUserSend) {this.nameUser = nameUserSend;}

    public String getPhotoUser() {return photoUser;}
    public void setPhotoUser(String photoUserSend) {this.photoUser = photoUserSend;}

    public double getNoteHome() {return noteHome;}
    public void setNoteHome(double noteHome) {this.noteHome = noteHome;}

    public double getNoteCleanless() {return noteCleanless;}
    public void setNoteCleanless(double noteCleanless) {this.noteCleanless = noteCleanless;}

    public double getNoteCooked() {return noteCooked;}
    public void setNoteCooked(double noteCooked) {this.noteCooked = noteCooked;}

    public double getNoteTotal() {return noteTotal;}
    public void setNoteTotal(double noteTotal) {this.noteTotal = noteTotal;}

    public String getLinkMeal() {return linkMeal;}
    public void setLinkMeal(String linkMeal) {this.linkMeal = linkMeal;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
}
