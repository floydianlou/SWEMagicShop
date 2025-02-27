package DAOInterface;

import DomainModel.Person;
import DomainModel.Customer;
import DomainModel.Species;

import java.util.ArrayList;

// JUST AN INTERFACE TO MAKE BUSINESS LOGIC FIRST !!
public interface AccountDAO {
    public void createCustomerAccount (String name, String surname, String email, String password, int age,
                                       int phoneNumber, Species species); // this function should check for existent customer emails (maybe return boolean then??) then create account
    public void createManagerAccount (String name, String surname, String email, String password);
    public Person loginPerson (String email, String password);
    public ArrayList<Customer> viewAllCustomers();
    public Customer getCustomerByID (int customerID); // should check if ID exists then return Customer object from data in database.

    public void updateCustomerAccount(); //TODO add parameters
    public void updateManagerAccount(); //TODO complete this
}