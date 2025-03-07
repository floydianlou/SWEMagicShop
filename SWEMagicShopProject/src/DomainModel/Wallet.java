package DomainModel;

public class Wallet {
    private int walletID;
    private int CPbalance;

    // constructor
    public Wallet (int walletID, int CPbalance) {
        this.walletID = walletID;
        this.CPbalance = CPbalance;
    }

    //GETTER AND SETTER
    public int getWalletID() {
        return walletID;
    }

    public void setWalletID(int walletID) {
        this.walletID = walletID;
    }

    public int getCPbalance() {
        return CPbalance;
    }

    public void setCPbalance(int CPbalance) {
        this.CPbalance = CPbalance;
    }


    // Don't know if it will ever be useful, maybe for testing later
    public void printBalance() {
        System.out.println("Wallet ID: " + walletID);
        System.out.println("Copper Pieces: " + CPbalance);
    }
}