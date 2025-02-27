package DomainModel;

// QUESTION: is this class really necessary for Customer? Shouldn't this be modeled by AccountManager
// maybe? I have various doubts. The only way i can see this being useful is if we use the object
// associated with every customer to calculate if the Customer is of age, but that shouldn't happen in
// Customer everytime, probably only once when you register and then save it on the database...
public class Species {
    int speciesID;
    String name;
    int legalAge, limitAge;

    public Species (int speciesID, String name, int legalAge, int limitAge) {
        this.legalAge = legalAge;
        this.name = name;
        this.limitAge = limitAge;
        this.speciesID = speciesID;
    }

    public int getLegalAge() {
        return this.legalAge;
    }

    public int getLimitAge() {
        return limitAge;
    }
}