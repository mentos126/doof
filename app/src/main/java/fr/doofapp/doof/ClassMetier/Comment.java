package fr.doofapp.doof.ClassMetier;

public class Comment {

    private String descriptif;
    private String photo;
    private String linkUser;
    private String nameUser;
    private String photoUser;
    private double noteTotale;
    private double noteAccueil;
    private double noteProprete;
    private double noteCuisine;

    public Comment(String descriptif, String photo, String link, String nameUser, String photoUser, double noteAccueil, double noteProprete, double noteCuisine, double noteTotale) {
        this.descriptif = descriptif;
        this.photo = photo;
        this.linkUser = link;
        this.nameUser = nameUser;
        this.photoUser = photoUser;
        this.noteAccueil = noteAccueil;
        this.noteProprete = noteProprete;
        this.noteCuisine = noteCuisine;
        this.noteTotale = noteTotale;
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

    public double getNoteAccueil() {return noteAccueil;}
    public void setNoteAccueil(double noteAccueil) {this.noteAccueil = noteAccueil;}

    public double getNoteProprete() {return noteProprete;}
    public void setNoteProprete(double noteProprete) {this.noteProprete = noteProprete;}

    public double getNoteCuisine() {return noteCuisine;}
    public void setNoteCuisine(double noteCuisine) {this.noteCuisine = noteCuisine;}

    public double getNoteTotale() {return noteTotale;}
    public void setNoteTotale(double noteTotale) {this.noteTotale = noteTotale;}
}
