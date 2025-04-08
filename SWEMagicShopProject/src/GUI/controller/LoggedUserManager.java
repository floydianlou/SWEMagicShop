package GUI.controller;

import DomainModel.Person;

public class LoggedUserManager {
    private static LoggedUserManager instance;

    private Person loggedUser;


    private LoggedUserManager() {
    }

    public static LoggedUserManager getInstance() {
        if (instance == null) {
            instance = new LoggedUserManager();
        }
        return instance;
    }

    public void setLoggedUser(Person loggedUser) {
        this.loggedUser = loggedUser;
    }

    public Person getLoggedUser() {
        return loggedUser;
    }

    // called when logging out and going back to welcome view
    public void clearSession() {
        loggedUser=null;
}
}