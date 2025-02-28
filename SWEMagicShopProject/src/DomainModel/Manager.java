package DomainModel;

public class Manager extends Person {

    // Costruttore della classe Manager
    public Manager(int personID, int name, int surname, int email, int password) {
        super(personID, name, surname, email, password);
    }

    // Non servono i getter e setter del manager tanto li eredita da Person
}