package BusinessLogic;

import DomainModel.ArcaneRequest;
import DomainModel.Customer;
import java.util.ArrayList;

public class ArcaneRequestManager {

    public boolean makeArcaneRequest(Customer customer) {
        if (customer.isArcaneMember())
            return false; // if they're already a member, no need to make a request.
        // if they have already made 5 requests or have a pending one, no need to make a request.
        ORM.ArcaneDAO arcaneDAO = new ORM.ArcaneDAO();
        if (arcaneDAO.canMakeArcaneRequest(customer.getPersonID())) {
            arcaneDAO.createArcaneRequest(customer.getPersonID());
        } else return false;
        return true;
    }

    public int viewNumberOfArcaneRequests(int customerID) { // for customer GUI
        ORM.ArcaneDAO arcaneDAO = new ORM.ArcaneDAO();
        return arcaneDAO.countCustomerRequests(customerID);
    }

    // both for manager and customer, to see specific customer requests and their statuses.
    public ArrayList<ArcaneRequest> viewRequestsByCustomer (int customerID) {
        ORM.ArcaneDAO arcaneDAO = new ORM.ArcaneDAO();
        return arcaneDAO.viewRequestsByCustomer(customerID);
    }

    public ArrayList<ArcaneRequest> viewPendingRequests() {
        ORM.ArcaneDAO arcaneDAO = new ORM.ArcaneDAO();
        return arcaneDAO.viewPendingRequests();
    }


    // to allow manager to GUI.view all requests, including refused and accepted ones.
    public ArrayList<ArcaneRequest> viewAllRequests() {
        ORM.ArcaneDAO arcaneDAO = new ORM.ArcaneDAO();
        return arcaneDAO.viewAllArcaneRequests();
    }

    public boolean approveRequest(ArcaneRequest arcaneRequest) {
        ORM.ArcaneDAO arcaneDAO = new ORM.ArcaneDAO();
        if (arcaneRequest == null) {
            System.out.println("There was a problem retrieving the request.");
            return false;
        }

       return arcaneDAO.updateArcaneRequest(arcaneRequest, 2);
    }

    public boolean rejectRequest(ArcaneRequest arcaneRequest) {
        ORM.ArcaneDAO arcaneDAO = new ORM.ArcaneDAO();
        if (arcaneRequest == null) {
            System.out.println("There was a problem retrieving the request.");
            return false;
        }

        return arcaneDAO.updateArcaneRequest(arcaneRequest, 3);
    }
}