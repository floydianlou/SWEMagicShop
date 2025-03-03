package DAOInterface;

import DomainModel.Wallet;

public interface WalletDAO {

    public Wallet getWalletByID(int ID);
    public void updateWallet(Wallet wallet);

}