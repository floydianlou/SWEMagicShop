package DomainModel;

public class Manager extends Person {
    public Manager (int personID, String name, String surname, String email, String password) {
        super(personID, name, surname, email, password);
    }

    // Non servono i getter e setter del manager tanto li eredita da Person
}