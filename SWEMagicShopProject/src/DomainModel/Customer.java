package DomainModel;

public class Customer extends Person {

    private Species ownSpecies;
    private int age;
    private int phoneNumber;
    private boolean isArcaneMember = false;
    private Wallet ownWallet;   // QUESTION: maybe when you log in, the DAO creates a new wallet object
                                // and associates it to the customer loading its balance etc.?

    //TODO: other constructors (in case of optional data when registering)
    public Customer (int personID, String name, String surname, String email, String password, int age,
                     int phoneNumber, boolean isArcaneMember, Wallet ownWallet, Species ownSpecies) {
        super(personID, name, surname, email, password);
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.isArcaneMember = isArcaneMember;
        this.ownWallet = ownWallet;
        this.ownSpecies = ownSpecies;
    }

    //TODO: getter and setters

}