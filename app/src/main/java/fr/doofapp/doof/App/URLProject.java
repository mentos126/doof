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
    private String GET_NB_TIKET = API + "account/get/credit";
    private String COMMAND_ORDER = API + "repas/order";
    private String CALENDAR = API + "planning/get";
    private String SEND_COMMENT = API + "repas/comment";
    private String CREDIT = API + "account/credit";

    public static synchronized URLProject getInstance()
    {
        if(uniqueInstance==null)
        {
            uniqueInstance = new URLProject();
        }
        return uniqueInstance;
    }

    public String getPROFILE() {return "";}
    public String getPROFILE_COMMENTS() {return "";}
    public String getPROFILE_MEALS() {return "";}
    public String getMYPROFILE() {return "";}


    /// NEW ***
    public String getREGISTER() {return REGISTER;}
    public String getLOGIN() {return LOGIN;}
    public String getPROFILE_UPDATE() {return PROFILE_UPDATE;}
    public String getMY_ADRESS() {return MY_ADRESS;}
    public String getCREATE_MEAL() {return CREATE_MEAL;}
    public String getUPLOAD() {return UPLOAD;}
    public String getSEARCH_MEAL() {return SEARCH_MEAL;}
    public String getONEMEAL() {return ONEMEAL;}
    public String getGET_NB_TIKET() {return GET_NB_TIKET;}
    public String getCOMMAND_ORDER() {return COMMAND_ORDER;}
    public String getCALENDAR() {return CALENDAR;}
    public String getSEND_COMMENT() {return SEND_COMMENT;}
    public String getCREDIT() {return CREDIT;}
}
