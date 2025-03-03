package DomainModel;

public class Order {

    private int orderID;
    private int orderStatus;
    private int totalCP;
    private String orderDate;

    public Order(int orderID, int orderStatus, int totalCP, String orderDate) {
        this.orderID = orderID;
        this.orderStatus = orderStatus;
        this.totalCP = totalCP;
        this.orderDate = orderDate;
    }

    public String getOrderData(){
        return "OrderID: " + orderID + "\n OrderDate: " + orderDate + "\n OrderStatus: " + orderStatus;
    }

    //GETTER AND SETTER

    public int getTotalCP() {
        return totalCP;
    }
    public void setTotalCP(int newCP) {totalCP = newCP;}

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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
