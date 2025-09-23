package BusinessLogic;

import ORM.WalletDAO;
import DomainModel.Customer;
import DomainModel.Wallet;

public class WalletManager {

    WalletDAO walletDAO;

    public WalletManager(WalletDAO walletDAO) {
        this.walletDAO = walletDAO;
    }

    public WalletManager() {
        this.walletDAO = new WalletDAO();
    }

    // function to see balance
    public int viewBalance(int id) {
        Wallet wallet = walletDAO.getWalletByID(id);

        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found for this customer");
        }

        return wallet.getCPbalance();
    }

    // function to get wallet by customer
    public Wallet getWalletByCustomer(Customer customer) throws IllegalArgumentException {
        Wallet wallet = walletDAO.getWalletByID(customer.getPersonID());

        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found for this customer");
        }

        return wallet;
    }

    // function to add and widthdraw money from the wallet
    public void addFunds(int amountGP, int amountSP, int amountCP, Customer customer) throws IllegalArgumentException {
        Wallet wallet = walletDAO.getWalletByID(customer.getPersonID());

        if(amountGP < 0 || amountSP < 0 || amountCP < 0){
            throw new IllegalArgumentException("Amount must be positive!");
        }

        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found for this customer");
        }

        int totalAmount = (amountGP*100) + (amountSP*10) + amountCP;

        wallet.setCPbalance(wallet.getCPbalance() + totalAmount);
        walletDAO.updateWallet(wallet);
        customer.setOwnWallet(wallet);
    }


    public boolean withdrawFunds(int amountCP, Customer customer) throws IllegalArgumentException {
        Wallet wallet = walletDAO.getWalletByID(customer.getPersonID());

        try{
            if( amountCP < 0){
                throw new IllegalArgumentException("Amount must be positive!");
            }

            if (wallet == null) {
                throw new IllegalArgumentException("Wallet not found for this customer.");
            }

            if (amountCP > wallet.getCPbalance()) {
                throw new IllegalArgumentException("You don't have enough funds to withdraw this amount.");
            }

            wallet.setCPbalance(wallet.getCPbalance() - amountCP);
            walletDAO.updateWallet(wallet);
            customer.setOwnWallet(wallet);
            return true;
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return false;
        }

    }
}