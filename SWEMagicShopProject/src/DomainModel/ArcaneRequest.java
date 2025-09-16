package DomainModel;

public class ArcaneRequest {
    private int requestID, customerID;
    private String requestStatus, customerName, requestDate;

    // VIEW CONSRUCTOR
    public ArcaneRequest (int requestID, String requestStatus, int customerID,  String customerName, String requestDate) {
        this.customerID = customerID;
        this.requestID = requestID;
        this.requestStatus = requestStatus;
        this.customerName = customerName;
        this.requestDate = requestDate;
    }

    public ArcaneRequest (int requestID, String requestStatus, int customerID) {
        this.customerID = customerID;
        this.requestID = requestID;
        this.requestStatus = requestStatus;
    }

    // CUSTOMER CONSTRUCTOR
    public ArcaneRequest (int customerID) {
        this.customerID = customerID;
    }

    //GETTER AND SETTER

    public String getCustomerName() {
        return customerName;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestDate() {
        return requestDate;
    }
}