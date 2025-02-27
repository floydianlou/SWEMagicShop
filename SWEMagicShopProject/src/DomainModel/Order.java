package DomainModel;

public class Order {

    private int orderID;
    private int orderStatus;
    private int totalCP;
    private int totalSP;
    private int totalGP;
    private String orderDate;

    public Order(int orderID, int orderStatus, int totalCP, int totalSP, int totalGP, String orderDate) {
        this.orderID = orderID;
        this.orderStatus = orderStatus;
        this.totalCP = totalCP;
        this.totalSP = totalSP;
        this.totalGP = totalGP;
        this.orderDate = orderDate;
    }

    public String getOrderData(){
        return "OrderID: " + orderID + "\n OrderDate: " + orderDate + "\n OrderStatus: " + orderStatus;
    }

    //GETTER AND SETTER

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getTotalCP() {
        return totalCP;
    }

    public void setTotalCP(int totalCP) {
        this.totalCP = totalCP;
    }

    public int getTotalSP() {
        return totalSP;
    }

    public void setTotalSP(int totalSP) {
        this.totalSP = totalSP;
    }

    public int getTotalGP() {
        return totalGP;
    }

    public void setTotalGP(int totalGP) {
        this.totalGP = totalGP;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
