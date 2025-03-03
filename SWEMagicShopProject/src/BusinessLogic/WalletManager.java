package BusinessLogic;

import DAOInterface.WalletDAO;
import DomainModel.Customer;
import DomainModel.Wallet;

public class WalletManager {
    WalletDAO walletDAO; //TODO: TO BE INCLUDED IN METHODS WHEN DAO IS IMPLEMENTED, CAN'T BE IMPLEMENTED NOW SINCE WE'RE USING AN INTERFACE

    public WalletManager(WalletDAO walletDAO) {
        this.walletDAO = walletDAO;
    }


    //function to add and widthdraw money from the wallet
    public void addFunds(int amountGP, int amountSP, int amountCP, Customer customer) {
        Wallet wallet = walletDAO.getWalletByID(customer.getPersonID());

        if (wallet == null) {
            System.out.println("Wallet not found for this customer.");
            return;
        }

        int totalAmount = (amountGP*100) + (amountSP*10) + amountCP;

        wallet.setCPbalance(wallet.getCPbalance() + totalAmount);
        customer.setOwnWallet(wallet);
        walletDAO.updateWallet(wallet);
    }


    public boolean withdrawFunds(int amountCP, Customer customer) {
        Wallet wallet = walletDAO.getWalletByID(customer.getPersonID());

        if (wallet == null) {
            System.out.println("Wallet not found for this customer.");
            return false;
        }

        if (amountCP > wallet.getCPbalance()) {
            System.out.println("You don't have enough funds to withdraw this amount.");
            return false;
        }

        wallet.setCPbalance(wallet.getCPbalance() - amountCP);
        customer.setOwnWallet(wallet);
        walletDAO.updateWallet(wallet);
        return true;
    }
}