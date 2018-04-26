package fr.doofapp.doof.App;


public class URLProject  {

    private static URLProject uniqueInstance;

    private String DNS = "http://mon-site-en-ligne.fr";
    private String SERVICE_DIR = DNS + "/doofprod";

    private String DNSPORT = DNS + ":4007";
    private String APP_DIR = DNSPORT + "/doof";

    private String LOGIN = APP_DIR + "/login/jc/papillon"; //SERVICE_DIR + "/user.json";
    private String PROFILE = SERVICE_DIR + "/profile.json";
    private String UPDATE_PROFILE = SERVICE_DIR + "/update_profile.json";
    private String PROFILE_COMMENTS = SERVICE_DIR + "/comments.json";
    private String PROFILE_MEALS = SERVICE_DIR + "/profile_meals.json";

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
    public String updatePROFILE() {return UPDATE_PROFILE;}
    public String getPROFILE_COMMENTS() {return PROFILE_COMMENTS;}
    public String getPROFILE_MEALS() {return PROFILE_MEALS;}


}
