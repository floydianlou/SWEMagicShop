package DomainModel;

public class Manager extends Person {

    // Costruttore della classe Manager
    public Manager(int personID, String name, String surname, String email, String password) {
        super(personID, name, surname, email, password);
    }

    public Manager(int personID) {
        super(personID);
    }
}