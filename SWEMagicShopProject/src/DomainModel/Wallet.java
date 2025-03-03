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

    //function to add and widthdraw money from the wallet
    public void addFunds(int amountGP, int amountSP, int amountCP) {
        setGPbalance(GPbalance + amountGP);
        setSPbalance(SPbalance + amountSP);
        setCPbalance(CPbalance + amountCP);
        normalizeCurrency();
    }

    // Money conversion 10CP = 1SP and 10SP = 1GP
    private void normalizeCurrency() {
        SPbalance += CPbalance / 10;
        CPbalance = CPbalance % 10;

        GPbalance += SPbalance / 10;
        SPbalance = SPbalance % 10;
    }

    public boolean withdrawFunds(int amountGP, int amountSP, int amountCP) {
        int totalCPbalance = (this.GPbalance * 100) + (this.SPbalance * 10) + this.CPbalance;
        int totalCPwithdraw = (amountGP * 100) + (amountSP * 10) + amountCP;

        if (totalCPwithdraw > totalCPbalance) {
            System.out.println("You don't have enough funds to withdraw this amount.");
            return false;
        }

        totalCPbalance -= totalCPwithdraw;

        setGPbalance(0);
        setSPbalance(0);
        setCPbalance(totalCPbalance);

        normalizeCurrency();

        return true;
    }

    // Don't know if it will ever be useful, maybe for testing later
    public void printBalance() {
        System.out.println("Wallet ID: " + walletID);
        System.out.println("Gold Pieces: " + GPbalance);
        System.out.println("Silver Pieces: " + SPbalance);
        System.out.println("Copper Pieces: " + CPbalance);
    }
}