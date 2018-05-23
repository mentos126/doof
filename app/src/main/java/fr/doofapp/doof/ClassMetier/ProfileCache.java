package fr.doofapp.doof.ClassMetier;

import fr.doofapp.doof.ProfileActivity.ProfileActivity;

public class ProfileCache {

    private Profile profile;

    private ProfileCache (){}
    private static ProfileCache INSTANCE = null;
    public static synchronized ProfileCache getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new ProfileCache();
        }
        return INSTANCE;
    }

    public Profile getProfile() {return profile;}
    public void setProfile(Profile profile) {this.profile = profile;}

}
