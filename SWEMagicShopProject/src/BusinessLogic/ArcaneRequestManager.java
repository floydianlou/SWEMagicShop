package BusinessLogic;

import DAOInterface.ArcaneDAO;
import DomainModel.ArcaneRequest;
import ORM.AccountDAO;

import java.util.ArrayList;

public class ArcaneRequestManager {

    ArcaneDAO arcaneDAO;

    public boolean makeArcaneRequest(int customerID) {
        if (arcaneDAO.countCustomerRequests(customerID) >= 5 || arcaneDAO.pendingArcaneRequest(customerID)) {
            System.out.println("Can't make an Arcane Request at the moment.");
            return false; // either customer already made 5 refused requests or has a pending request
        }
        ArcaneRequest newRequest = new ArcaneRequest(customerID); // passes only customerID since DAO assigns ID and pending status
        boolean requestSaved = arcaneDAO.createArcaneRequest(newRequest);
        if (!requestSaved) {
            System.out.println("Failed to create an Arcane Request.");
            return false;
        }
        return true;
    }


    public int viewNumberOfArcaneRequests(int customerID) { // for customer GUI
        return arcaneDAO.getNumberCustomerRequests(customerID);
    }

    // both for manager and customer, to see specific customer requests and their statuses.
    public ArrayList<ArcaneRequest> viewRequestsByCustomer (int customerID) {
        return arcaneDAO.viewRequestsByCustomer(customerID);
    }

    public ArrayList<ArcaneRequest> viewPendingRequests() {
        return arcaneDAO.viewPendingRequests();
    }


    // to allow manager to view all requests, including refused and accepted ones.
    public ArrayList<ArcaneRequest> viewAllRequests() {
        return arcaneDAO.viewAllArcaneRequests();
    }

    public boolean approveRequest(ArcaneRequest arcaneRequest) {

        AccountDAO accountDAO = new AccountDAO();
        if (arcaneRequest == null) {
            System.out.println("There was a problem retrieving the request.");
            return false;
        }

        // we need to update the request's status to APPROVED in the database
       if(!arcaneDAO.updateRequestStatus(arcaneRequest.getRequestID(), 1)) {
           System.out.println("There was a problem updating the request.");
           return false;
       }

       // we now need to update the customer's membership...
        if (!accountDAO.updateCustomerArcaneStatus(arcaneRequest.getCustomerID(), true)) {
            System.out.println("There was a problem updating customer's status. Reversing the request.");
            arcaneDAO.updateRequestStatus(arcaneRequest.getRequestID(), 0);
            return false;
        }
       return true;
    }

    public boolean rejectRequest(ArcaneRequest arcaneRequest) {
        if (arcaneRequest == null) {
            System.out.println("There was a problem retrieving the request.");
            return false;
        }

        if(!arcaneDAO.updateRequestStatus(arcaneRequest.getRequestID(), 2)) { // 2 == REJECTED in database
            System.out.println("There was a problem updating the request.");
            return false;
        }

        return true;
    }
}
