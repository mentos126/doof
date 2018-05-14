package fr.doofapp.doof.App;


public class URLProject  {

    private static URLProject uniqueInstance;

    private String API = "http://jcdev.io:4007/doof/";
    private String LOGIN = API + "login";
    private String REGISTER = API + "register";
    private String PROFILE_UPDATE = API + "profile/update";
    private String MY_ADRESS = API + "user/adress/mine";
    private String CREATE_MEAL = API + "repas/publish";
    private String UPLOAD = API + "upload";
    private String SEARCH_MEAL = API + "repas/find";
    private String ONEMEAL = API + "repas/show";

    private String PROFILE = API + "/profile.json";
    private String MYPROFILE = PROFILE;
    private String UPDATE_PROFILE = API + "/update_profile.json";
    private String PROFILE_COMMENTS = API + "/comments.json";
    private String PROFILE_MEALS = API + "/profile_meals.json";
    private String MEALS = API + "/meals.json";
    private String CALENDARMEALS = API + "/calendar.json";

    private String PROFILE_UPDATE_ROFILE = "";

    public static synchronized URLProject getInstance()
    {
        if(uniqueInstance==null)
        {
            uniqueInstance = new URLProject();
        }
        return uniqueInstance;
    }

    public String getPROFILE() {return PROFILE;}
    public String getPROFILE_COMMENTS() {return PROFILE_COMMENTS;}
    public String getPROFILE_MEALS() {return PROFILE_MEALS;}
    public String getMEALS() {return MEALS;}
    public String getMYPROFILE() {return MYPROFILE;}
    public String updatePROFILE() {return UPDATE_PROFILE;}
    public String getCALENDARMEALS() {return CALENDARMEALS;}
    /// NEW ***

    public String getREGISTER() {return REGISTER;}
    public String getLOGIN() {return LOGIN;}
    public String getPROFILE_UPDATE() {return PROFILE_UPDATE;}
    public String getMY_ADRESS() {return MY_ADRESS;}
    public String getCREATE_MEAL() {return CREATE_MEAL;}
    public String getUPLOAD() {return UPLOAD;}
    public String getSEARCH_MEAL() {return SEARCH_MEAL;}
    public String getONEMEAL() {return ONEMEAL;}
}
