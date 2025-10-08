package BusinessLogic;

import DomainModel.ArcaneRequest;
import DomainModel.Customer;
import java.util.ArrayList;

public class ArcaneRequestManager {

    public boolean makeArcaneRequest(Customer customer) {
        if (customer.isArcaneMember())
            return false; // if they're already a member, no need to make a request.
        // if they have already made 5 requests or have a pending one, no need to make a request.
        DAO.ArcaneDAO arcaneDAO = new DAO.ArcaneDAO();
        if (arcaneDAO.canMakeArcaneRequest(customer.getPersonID())) {
            arcaneDAO.createArcaneRequest(customer.getPersonID());
        } else return false;
        return true;
    }

    public int viewNumberOfArcaneRequests(int customerID) { // for customer GUI
        DAO.ArcaneDAO arcaneDAO = new DAO.ArcaneDAO();
        return arcaneDAO.countCustomerRequests(customerID);
    }

    public ArrayList<ArcaneRequest> viewRequestsByCustomer (int customerID) {
        DAO.ArcaneDAO arcaneDAO = new DAO.ArcaneDAO();
        return arcaneDAO.viewRequestsByCustomer(customerID);
    }

    public ArrayList<ArcaneRequest> viewPendingRequests() {
        DAO.ArcaneDAO arcaneDAO = new DAO.ArcaneDAO();
        return arcaneDAO.viewPendingRequests();
    }

    public ArrayList<ArcaneRequest> viewAllRequests() {
        DAO.ArcaneDAO arcaneDAO = new DAO.ArcaneDAO();
        return arcaneDAO.viewAllArcaneRequests();
    }

    public boolean approveRequest(ArcaneRequest arcaneRequest) {
        DAO.ArcaneDAO arcaneDAO = new DAO.ArcaneDAO();
        if (arcaneRequest == null) {
            System.out.println("There was a problem retrieving the request.");
            return false;
        }

       return arcaneDAO.updateArcaneRequest(arcaneRequest, 2);
    }

    public boolean rejectRequest(ArcaneRequest arcaneRequest) {
        DAO.ArcaneDAO arcaneDAO = new DAO.ArcaneDAO();
        if (arcaneRequest == null) {
            System.out.println("There was a problem retrieving the request.");
            return false;
        }

        return arcaneDAO.updateArcaneRequest(arcaneRequest, 3);
    }
}