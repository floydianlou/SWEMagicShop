package DAOInterface;

import DomainModel.Customer;
import DomainModel.Wallet;

public interface WalletDAO {

    //there should be a function to create a wallet but I still need to figure out
    // when it shall be called. Maybe in the creation of the customer account?
    public void createWalletForCustomer(Customer customer);
    public Wallet getWalletByID(int ID);
    public void updateWallet(Wallet wallet);

}