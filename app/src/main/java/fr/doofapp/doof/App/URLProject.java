package fr.doofapp.doof.App;


public class URLProject  {

    private static URLProject uniqueInstance;

    private String DNS = "http://mon-site-en-ligne.fr";

    private String DNSPORT = DNS + ":4007";
    private String SERVICE_DIR = DNS + "/doofprod";

    private String APP_DIR = DNSPORT + "/doof";

    private String LOGIN = "http://mon-site-en-ligne.fr:4007/doof/login/jc/papillon";
    /*APP_DIR + "/login/jc/papillon";*/ //SERVICE_DIR + "/user.json";


    private String PROFILE = SERVICE_DIR + "/profile.json";
    private String MYPROFILE = PROFILE;
    private String UPDATE_PROFILE = SERVICE_DIR + "/update_profile.json";
    private String PROFILE_COMMENTS = SERVICE_DIR + "/comments.json";
    private String PROFILE_MEALS = SERVICE_DIR + "/profile_meals.json";
    private String MEALS = SERVICE_DIR + "/meals.json";
    private String ONEMEAL = SERVICE_DIR + "/meal.json";

    private String PROFILE_UPDATE_ROFILE = "";

    public static synchronized URLProject getInstance()
    {
        if(uniqueInstance==null)
        {
            uniqueInstance = new URLProject();
        }
        return uniqueInstance;
    }

    public String getLOGIN() {return LOGIN;}
    public String getPROFILE() {return PROFILE;}
    public String getPROFILE_COMMENTS() {return PROFILE_COMMENTS;}
    public String getPROFILE_MEALS() {return PROFILE_MEALS;}
    public String getMEALS() {return MEALS;}
    public String getMYPROFILE() {return MYPROFILE;}
    public String getONEMEAL() {return ONEMEAL;}
    public String updatePROFILE() {return UPDATE_PROFILE;}
}
