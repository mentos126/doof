package fr.doofapp.doof.ClassMetier;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

public class ListMealCache /*implements Serializable*/ {

    private ListMealCache (){}
    private static ListMealCache INSTANCE = null;
    public static synchronized ListMealCache getInstance()
    {
        if (INSTANCE == null)
        {   INSTANCE = new ListMealCache();
        }
        return INSTANCE;
    }


    static Bitmap mainPhoto;
    static List<byte[] > photos;
    static List<String> descriptions;
    static List<String> titles;
    static List<Integer> nbPortions;
    static List<Integer> prices;

    public Bitmap getMainPhoto(){return mainPhoto;}
    public void setMainPhoto(Bitmap mainPhoto){this.mainPhoto=mainPhoto;}

    public List<byte[] > getPhotos(){return photos;}
    public void setPhotos(List<byte[] > photos){this.photos=photos;}

    public List<String> getDescriptions(){return descriptions;}
    public void setDescriptions(List<String> descriptions){this.descriptions=descriptions;}

    public List<String> getTitles(){return titles;}
    public void setTitles(List<String> titles){this.titles=titles;}

    public List<Integer> getNbPortions(){return nbPortions;}
    public void setNbPortions(List<Integer> nbPortions){this.nbPortions=nbPortions;}

    public List<Integer> getPrices(){return prices;}
    public void setPrices(List<Integer> prices){this.prices=prices;}

}
