package DAOInterface;

import DomainModel.ArcaneRequest;

import java.util.ArrayList;

public interface ArcaneDAO {
    public ArrayList<ArcaneRequest> viewRequestsByCustomer(int customerID); // output is a list of requests made by customer with such ID
    public int countCustomerRequests(int customerID); // counts requests in ArcaneRequest table
    public boolean pendingArcaneRequest(int customerID); // checks inf customerID has any requests with status 01 (PENDING)
    public boolean createArcaneRequest(ArcaneRequest arcaneRequest); // adds a PENDING request to database
    public ArrayList<ArcaneRequest> viewAllArcaneRequests();
    public int getNumberCustomerRequests(int customerID);
    public ArrayList<ArcaneRequest> viewPendingRequests();
    public boolean updateRequestStatus(int requestID, int status);

}
