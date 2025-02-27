package DomainModel;

public class ArcaneRequest {
    private int requestID, customerID;
    private String requestStatus;

    public ArcaneRequest (int requestID, String requestStatus, int customerID) {
        this.customerID = customerID;
        this.requestID = requestID;
        this.requestStatus = requestStatus;
    }
}