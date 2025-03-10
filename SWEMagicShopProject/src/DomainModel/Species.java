package DomainModel;

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

    public Species (int speciesID) {
        this.speciesID = speciesID;
    }

    public int getSpeciesID() {
        return speciesID;
    }
    public int getLegalAge() {
        return this.legalAge;
    }

    public int getLimitAge() {
        return limitAge;
    }
}