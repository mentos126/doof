package fr.doofapp.doof.ClassMetier;

public class User {

    private String userId;
    private String password;
    private int role;
    private int isConnected;

    public User(String userId, String password, int role, int isConnected){
        super();
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.isConnected = isConnected;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String email) {
        this.userId = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }

    public int getConnected() {
        return isConnected;
    }
    public void setConnected(int connected) {
        this.isConnected = connected;
    }

}