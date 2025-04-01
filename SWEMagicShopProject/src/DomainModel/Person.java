package DomainModel;

public class Person {

    private int personID;
    private String name, surname;
    private String email, password;

    public Person(int personID, String name, String surname, String email, String password) {
        this.personID = personID;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public Person(int personID, String name, String surname, String email) {
        this.personID = personID;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public Person (int personID) {
        this.personID = personID;
    }


    public String getLoginData() { return "Email: " + email + "\nPassword: " + password; }
    public void updateEmail(String email) { this.email = email; }
    public void updatePassword(String password) { this.password = password; }

    // GETTER AND SETTER

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}