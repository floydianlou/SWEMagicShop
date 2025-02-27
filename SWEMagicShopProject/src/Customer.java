// Classe Customer che estende Person
public class Customer extends Person {

    //attributi di Customer
    public boolean ArcaneMembership;
    public int age;
    public int numberRequests;

    // Costruttore della classe Customer(con i super di Person)
    public Customer(int personID, int name, int surname, int email, int password) {
        super(personID, name, surname, email, password);
        this.ArcaneMembership = ArcaneMembership;
        this.age = age;
        this.numberRequests = 0;
    }




}





