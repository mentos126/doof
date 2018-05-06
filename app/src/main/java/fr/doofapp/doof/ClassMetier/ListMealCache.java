package fr.doofapp.doof.ClassMetier;

import android.graphics.Bitmap;
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

    private static String date;
    private static String time;
    private static String adress;
    /*private static String mainDescription;
    private static String mainTitle;
    private static Integer mainNbPortion;
    private static Integer mainPrice;
    private static Bitmap mainPhoto;*/
    private static List<Bitmap> photos;
    private static List<String> descriptions;
    private static List<String> titles;
    private static List<Integer> nbPortions;
    private static List<Integer> prices;
    private static Boolean isContain;

    public static String getDate() {return date;}
    public static void setDate(String date) {ListMealCache.date = date;}

    public static String getTime() {return time;}
    public static void setTime(String time) {ListMealCache.time = time;}

    public static String getAdress() {return adress;}
    public static void setAdress(String adress) {ListMealCache.adress = adress;}

    /*public static String getMainDescription() {return mainDescription;}
    public static void setMainDescription(String mainDescription) {ListMealCache.mainDescription = mainDescription;}

    public static String getMainTitle() {return mainTitle;}
    public static void setMainTitle(String mainTitle) {ListMealCache.mainTitle = mainTitle;}

    public static Integer getMainNbPortion() {return mainNbPortion;}
    public static void setMainNbPortion(Integer mainNbPortion) {ListMealCache.mainNbPortion = mainNbPortion;}

    public static Integer getMainPrice() {return mainPrice;}
    public static void setMainPrice(Integer mainPrice) {ListMealCache.mainPrice = mainPrice;}

    public Bitmap getMainPhoto(){return mainPhoto;}
    public static void setMainPhoto(Bitmap mainPhoto){ListMealCache.mainPhoto =mainPhoto;}*/

    public static List<Bitmap> getPhotos(){return photos;}
    public static void setPhotos(List<Bitmap> photos){ListMealCache.photos =photos;}

    public static List<String> getDescriptions(){return descriptions;}
    public static void setDescriptions(List<String> descriptions){ListMealCache.descriptions =descriptions;}

    public static List<String> getTitles(){return titles;}
    public static void setTitles(List<String> titles){ListMealCache.titles =titles;}

    public static List<Integer> getNbPortions(){return nbPortions;}
    public static void setNbPortions(List<Integer> nbPortions){ListMealCache.nbPortions =nbPortions;}

    public static List<Integer> getPrices(){return prices;}
    public static void setPrices(List<Integer> prices){ListMealCache.prices =prices;}

    public static Boolean getIsContain() {return isContain;}
    public static void setIsContain(Boolean isContain) {ListMealCache.isContain = isContain;}
}
