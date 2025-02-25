package BusinessLogic;

import DAOInterface.AccountDAO;
import DomainModel.Person;
import DomainModel.Species;

public class AccountManager {
    Person person;
    AccountDAO accountDAO; // TO BE INCLUDED IN METHODS WHEN DAO IS IMPLEMENTED

    public AccountManager (Person person) {
        this.person = person;
    }

    // methods
    public Person login (String email, String password) { // TODO: after DAO, exceptions need to be managed
        // here we create a connection with DAO...

        // this function should return a method from AccountManagerDAO which checks in the database for
        // a match with the given email and password, returning a Person - Customer or Manager - to activate
        // their session.
        return person;  // here we use a method from DAO, leaving this return person as a placeholder.
    }

    public void createCustomerAccount (String name, String surname, String email, String password, int age,
                                  int phoneNumber, Species species) {
        // WARNING: not exactly sure how to manage this Species object thing yet...so it's probably due to be changed soon.
    }



}