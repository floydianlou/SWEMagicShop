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

}