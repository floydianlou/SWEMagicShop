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

    //TODO: getter and setters


    public int getPersonID() {
        return personID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}