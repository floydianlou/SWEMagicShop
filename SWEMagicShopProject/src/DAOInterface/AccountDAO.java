package DAOInterface;

// JUST AN INTERFACE TO MAKE BUSINESS LOGIC FIRST !!
public interface AccountDAO {
    public void createCustomerAccount (String name, String surname, String email, String password, int age,
                                       int phoneNumber);
    // this function should check for existent customer emails (maybe return boolean then??) then create account
    public void createManagerAccount (String name, String surname, String email, String password);
    // same as above

}