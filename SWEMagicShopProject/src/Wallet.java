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
}