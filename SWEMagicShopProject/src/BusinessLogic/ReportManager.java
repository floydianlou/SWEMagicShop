package BusinessLogic;

import DomainModel.Item;
import ORM.ItemDAO;
import ORM.InventoryDAO;
import BusinessLogic.InventoryManager;
import ORM.AccountDAO;
import DomainModel.Customer;
import ORM.OrderDAO;
import DomainModel.Order;

import java.sql.SQLException;
import java.util.ArrayList;

public class ReportManager {
    InventoryDAO inventoryDAO;

    public ReportManager(InventoryDAO inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }

    public ReportManager() {
        //FOR TESTING
    }

    public String hotCategory(){
        InventoryDAO inventoryDAO = new InventoryDAO();
        return inventoryDAO.categoryMostSold();
    }

    public String uglyCategory(){
        InventoryDAO inventoryDAO = new InventoryDAO();
        return inventoryDAO.categoryLeastSold();
    }

    public int categoryNum(String category){
        InventoryDAO inventoryDAO = new InventoryDAO();
        if(!Utilities.checkCategory(category)){
            throw new IllegalArgumentException("Invalid category!");
        }
        return inventoryDAO.numberCategorySold(category);
    }

    public Item hotProduct(){
        InventoryDAO inventoryDAO = new InventoryDAO();
        Item item = inventoryDAO.productMostSold();
        if(item == null){
            throw new IllegalArgumentException("Item not found!");
        }
        return item;
    }

    public Item uglyProduct(){
        InventoryDAO inventoryDAO = new InventoryDAO();
        Item item = inventoryDAO.productLeastSold();
        if(item == null){
            throw new IllegalArgumentException("Item not found!");
        }
        return item;
    }

    public int productNumById(int itemid){
        InventoryDAO inventoryDAO = new InventoryDAO();
        if( itemid <= 0 ){
            throw new IllegalArgumentException("ItemID not valid!");
        }
        int number = inventoryDAO.numberProductSold(itemid);
        if(number == -1){
            throw new IllegalArgumentException("ItemID not found!");
        }
        return number;
    }

    public Customer viewBiggestSpender(){
        InventoryDAO inventoryDAO = new InventoryDAO();
        Customer customer = inventoryDAO.biggestSpender();
        if(customer == null){
            throw new IllegalArgumentException("Customer not found.");
        }
        return customer;
    }

    public int viewTotalSpentByCustomerId(int customerid){
        InventoryDAO inventoryDAO = new InventoryDAO();
        if(customerid <= 0){
            throw new IllegalArgumentException("CustomerID not valid!");
        }
        int number = inventoryDAO.totalSpentByCustomer(customerid);
        if(number == -1){
            throw new IllegalArgumentException("CustomerID not found!");
        }
        return number;
    }

    public Customer viewSmallestSpender(){
        InventoryDAO inventoryDAO = new InventoryDAO();
        Customer customer = inventoryDAO.smallestSpender();
        if(customer == null){
            throw new IllegalArgumentException("Customer not found.");
        } else return customer;
    }

    public int revenue(){
        InventoryDAO inventoryDAO = new InventoryDAO();
        int revenue = inventoryDAO.totalRevenue();
        if(revenue == -1){
            throw new IllegalArgumentException("Revenue not found!");
        }
        return revenue;
    }

    public int numberOfArcaneMembers(){
        AccountDAO accountDAO = new AccountDAO();
        ArrayList<Customer> customers = accountDAO.viewAllCustomers();
        int arcane = 0;
        for (Customer c : customers) {
            if(c.isArcaneMember()){
                arcane++;
            }
        }
        return arcane;
    }

    public int numberOfNonArcaneMembers(){
        AccountDAO accountDAO = new AccountDAO();
        ArrayList<Customer> customers = accountDAO.viewAllCustomers();
        int nonArcane = 0;
        for (Customer c : customers) {
            if(!c.isArcaneMember()){
                nonArcane++;
            }
        }
        return nonArcane;
    }

    public int numberOfCustomers() {
        AccountDAO accountDAO = new AccountDAO();
        ArrayList<Customer> customers = accountDAO.viewAllCustomers();
        return customers.size();
    }

    public void persentageOfArcane(){
        int arcane = numberOfArcaneMembers();
        int nonArcane = numberOfNonArcaneMembers();
        int total = numberOfCustomers();
        int arcanePercent = arcane * 100 / total;
        int nonArcanePercent = nonArcane * 100 / total;
        System.out.println("Percentage of Arcane Members: " + arcanePercent);
        System.out.println("Percentage of Non Arcane Members: " + nonArcanePercent);
    }

}
