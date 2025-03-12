package DomainModel;

public class Order {

    private int customerID;
    private int orderID;
    private String orderStatus;
    private int totalCP;
    private String orderDate;

    // constructor for managers to see all orders and their respective customers.
    public Order(int orderID, String orderStatus, int totalCP, String orderDate, int customerID) {
        this.orderID = orderID;
        this.orderStatus = orderStatus;
        this.totalCP = totalCP;
        this.orderDate = orderDate;
        this.customerID = customerID;
    }

    // constructor for customers to show their orders - it doesn't need the customerID
    public Order(int orderID, String orderStatus, int totalCP, String orderDate) {
        this.orderID = orderID;
        this.orderStatus = orderStatus;
        this.totalCP = totalCP;
        this.orderDate = orderDate;
    }

    public String getOrderData(){
        return "OrderID: " + orderID + "\n OrderDate: " + orderDate + "\n OrderStatus: " + orderStatus;
    }

    //GETTER AND SETTER

    public String getOrderDate() {
        return orderDate;
    }

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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
