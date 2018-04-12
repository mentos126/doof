package fr.doofapp.doof.App;


public class URLProject  {

    private static URLProject uniqueInstance;

    protected String DNS = "http://mon-site-en-ligne.fr";
    protected String SERVICE_DIR = DNS + "/doofprod";

    private String LOGIN =  SERVICE_DIR + "/user.json";
    private String PROFILE = SERVICE_DIR + "/profile.json";
    private String PROFILE_COMMENTS = SERVICE_DIR + "/comments.json";
    private String PROFILE_MEALS = SERVICE_DIR + "";

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
    public String getPROFILE_MEALs() {return PROFILE_MEALS;}


}
