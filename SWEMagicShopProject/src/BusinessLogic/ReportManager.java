package BusinessLogic;

import DomainModel.Item;
import ORM.InventoryDAO;
import ORM.AccountDAO;
import DomainModel.Customer;

//FOR GRAPH
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;


import java.util.ArrayList;
import java.util.Map;

public class ReportManager {

    public ReportManager() {}

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

    //FOR GRAPH
    // Metodo che crea il grafico delle vendite per categoria
    public static JFreeChart createCategorySalesChart() {
        // Dataset per il grafico a barre
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        InventoryDAO inventoryDAO = new InventoryDAO();
        Map<String, Integer> categorySales = inventoryDAO.getCategorySales();

        // Aggiungi i dati al dataset
        for (Map.Entry<String, Integer> entry : categorySales.entrySet()) {
            dataset.addValue(entry.getValue(), "Vendite", entry.getKey());
        }

        // Crea il grafico a barre
        JFreeChart chart = ChartFactory.createBarChart(
                "Vendite per Categoria",  // Titolo del grafico
                "Categoria",              // Asse X
                "Numero di Vendite",      // Asse Y
                dataset                   // Dataset
        );

        return chart;
    }

    // Metodo che crea il grafico delle vendite per prodotto
    public static JFreeChart createProductSalesChart() {
        // Dataset per il grafico a barre
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        InventoryDAO inventoryDAO = new InventoryDAO();
        Map<String, Integer> itemSales = inventoryDAO.getItemSales();

        // Aggiungi i dati al dataset
        for (Map.Entry<String, Integer> entry : itemSales.entrySet()) {
            dataset.addValue(entry.getValue(), "Vendite", entry.getKey());
        }

        // Crea il grafico a barre
        JFreeChart chart = ChartFactory.createBarChart(
                "Vendite per Prodotto",  // Titolo del grafico
                "Prodotto",              // Asse X
                "Numero di Vendite",      // Asse Y
                dataset                   // Dataset
        );

        return chart;
    }

    // Metodo che crea il pannello per il grafico
    public static ChartPanel createChartPanel(JFreeChart chart) {
        return new ChartPanel(chart);
    }

}
