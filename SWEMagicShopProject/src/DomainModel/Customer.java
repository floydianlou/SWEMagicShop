package DomainModel;

public class Customer extends Person {

    private Species ownSpecies;  // created by DAO when data is retrieved from db
    private int age;
    private String phoneNumber;
    private boolean isArcaneMember;
    private Wallet ownWallet;   // QUESTION: maybe when you log in, the DAO creates a new wallet object
                                // and associates it to the customer loading its balance etc.?

    //TODO: other constructors (in case of optional data when registering)
    public Customer (int personID, String name, String surname, String email, String password, int age,
                     String phoneNumber, boolean isArcaneMember, Wallet ownWallet, Species ownSpecies) {
        super(personID, name, surname, email, password);
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.isArcaneMember = isArcaneMember;
        this.ownWallet = ownWallet;
        this.ownSpecies = ownSpecies;
    }

    public String getPersonalData() { return "Name: " + getName() + "\nSurname: " + getSurname() + "\nAge: " + age; }

    //GETTER AND SETTER

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Species getOwnSpecies() {
        return ownSpecies;
    }

    public void setOwnSpecies(Species ownSpecies) {
        this.ownSpecies = ownSpecies;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isArcaneMember() {
        return isArcaneMember;
    }

    public String getSpeciesName() {
        return this.ownSpecies.name;
    }

    public int getWalletBalance() {
        return this.ownWallet.getCPbalance();
    }

    public void setArcaneMember(boolean arcaneMember) {
        isArcaneMember = arcaneMember;
    }

    public Wallet getOwnWallet() {
        return ownWallet;
    }

    public void setOwnWallet(Wallet ownWallet) {
        this.ownWallet = ownWallet;
    }
}