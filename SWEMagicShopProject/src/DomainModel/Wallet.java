package DomainModel;

public class Wallet {
    private int walletID;
    private int GPbalance, SPbalance, CPbalance;

    // constructor
    public Wallet (int walletID, int GPbalance, int SPbalance, int CPbalance) {
        this.walletID = walletID;
        this.GPbalance = GPbalance;
        this.SPbalance = SPbalance;
        this.CPbalance = CPbalance;
    }


    //GETTER AND SETTER
    public int getWalletID() {
        return walletID;
    }

    public void setWalletID(int walletID) {
        this.walletID = walletID;
    }

    public int getGPbalance() {
        return GPbalance;
    }

    public void setGPbalance(int GPbalance) {
        this.GPbalance = GPbalance;
    }

    public int getSPbalance() {
        return SPbalance;
    }

    public void setSPbalance(int SPbalance) {
        this.SPbalance = SPbalance;
    }

    public int getCPbalance() {
        return CPbalance;
    }

    public void setCPbalance(int CPbalance) {
        this.CPbalance = CPbalance;
    }

}