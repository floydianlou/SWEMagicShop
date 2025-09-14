package tests.BusinessLogicTest;


import BusinessLogic.ReportManager;
import Exceptions.InventoryExceptions;
import ORM.InventoryDAO;
import ORM.AccountDAO;
import DomainModel.Customer;
import DomainModel.Item;

import org.jfree.chart.JFreeChart;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReportManagerTest {

    private InventoryDAO inventoryDAO;
    private AccountDAO accountDAO;
    private ReportManager reportManager;
    private Customer customer;
    private Item item;

    @BeforeEach
    public void setUp() {
        inventoryDAO = mock(InventoryDAO.class);
        accountDAO = mock(AccountDAO.class);
        reportManager = new ReportManager(inventoryDAO, accountDAO);
    }

    @AfterEach
    public void tearDown() {
        inventoryDAO = null;
        accountDAO = null;
        reportManager = null;
        customer = null;
        item = null;
    }

    @Test
    public void testHotCategory() {
        when(inventoryDAO.categoryMostSold()).thenReturn("Weapons");
        assertEquals("Weapons", reportManager.hotCategory());
        verify(inventoryDAO, times(1)).categoryMostSold();
    }

    @Test
    public void testUglyCategory() {
        when(inventoryDAO.categoryLeastSold()).thenReturn("Armor");
        assertEquals("Armor", reportManager.uglyCategory());
        verify(inventoryDAO, times(1)).categoryLeastSold();
    }

    @Test
    public void testCategoryNumValid() {
        when(inventoryDAO.numberCategorySold("Weapons")).thenReturn(10);
        assertEquals(10, reportManager.categoryNum("Weapons"));
        verify(inventoryDAO, times(1)).numberCategorySold("Weapons");
    }

    @Test
    public void testCategoryNum_InvalidCategory() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportManager.categoryNum("InvalidCategory");
        });
        assertEquals("Invalid category!", exception.getMessage());

        verify(inventoryDAO, never()).numberCategorySold("InvalidCategory");
    }

    @Test
    public void testHotProduct() {
        Item item = new Item(1, "Sword", "A sharp blade", "Weapons", false, 100, "/images/sword.png");
        when(inventoryDAO.productMostSold()).thenReturn(item);
        assertEquals(item, reportManager.hotProduct());
        verify(inventoryDAO, times(1)).productMostSold();
    }

    @Test
    public void testHotProduct_Null() {
        when(inventoryDAO.productMostSold()).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportManager.hotProduct();
        });
        assertEquals("Item not found!", exception.getMessage());
        verify(inventoryDAO, times(1)).productMostSold();
    }

    @Test
    public void testUglyProduct() {
        Item item = new Item(1, "Shield", "A sturdy shield", "Armor", false, 150, "/images/shield.png");
        when(inventoryDAO.productLeastSold()).thenReturn(item);
        assertEquals(item, reportManager.uglyProduct());
        verify(inventoryDAO, times(1)).productLeastSold();
    }

    @Test
    public void testUglyProduct_Null() {
        when(inventoryDAO.productLeastSold()).thenReturn(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportManager.uglyProduct();
        });
        assertEquals("Item not found!", exception.getMessage());
        verify(inventoryDAO, times(1)).productLeastSold();
    }

    @Test
    public void testProductNumById() {
        when(inventoryDAO.numberProductSold(5)).thenReturn(12);
        assertEquals(12, reportManager.productNumById(5));
        verify(inventoryDAO, times(1)).numberProductSold(5);
    }

    @Test
    public void testProductNumById_InvalidId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportManager.productNumById(0);
        });
        assertEquals("ItemID not valid!", exception.getMessage());
        verify(inventoryDAO, never()).numberProductSold(0);
    }

    @Test
    public void testProductNumById_NotFound() {
        when(inventoryDAO.numberProductSold(7)).thenReturn(-1);
        assertThrows(IllegalArgumentException.class, () -> reportManager.productNumById(7));
    }

    @Test
    public void testViewBiggestSpender() {
        Customer c = new Customer(1);
        when(inventoryDAO.biggestSpender()).thenReturn(c);
        assertEquals(c, reportManager.viewBiggestSpender());
        verify(inventoryDAO, times(1)).biggestSpender();
    }

    @Test
    public void testViewBiggestSpender_Null() {
        when(inventoryDAO.biggestSpender()).thenReturn(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportManager.viewBiggestSpender();
        });
        assertEquals("Customer not found.", exception.getMessage());
        verify(inventoryDAO, times(1)).biggestSpender();
    }

    @Test
    public void testViewTotalSpent() {
        when(inventoryDAO.totalSpentByCustomer(1)).thenReturn(100);
        assertEquals(100, reportManager.viewTotalSpentByCustomerId(1));
        verify(inventoryDAO, times(1)).totalSpentByCustomer(1);
    }

    @Test
    public void testViewTotalSpent_InvalidId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportManager.viewTotalSpentByCustomerId(0);
        });
        assertEquals("CustomerID not valid!", exception.getMessage());
        verify(inventoryDAO, never()).totalSpentByCustomer(0);
    }

    @Test
    public void testViewTotalSpent_NotFound() {
        when(inventoryDAO.totalSpentByCustomer(5)).thenReturn(-1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportManager.viewTotalSpentByCustomerId(5);
        });
        assertEquals("CustomerID not found!", exception.getMessage());
        verify(inventoryDAO, times(1)).totalSpentByCustomer(5);
    }

    @Test
    public void testViewSmallestSpenderValid() {
        Customer c = new Customer(1);
        when(inventoryDAO.smallestSpender()).thenReturn(c);
        assertEquals(c, reportManager.viewSmallestSpender());
        verify(inventoryDAO, times(1)).smallestSpender();
    }

    @Test
    public void testViewSmallestSpenderThrowsIfNull() {
        when(inventoryDAO.smallestSpender()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> reportManager.viewSmallestSpender());
    }

    @Test
    public void testRevenue() {
        when(inventoryDAO.totalRevenue()).thenReturn(500);
        assertEquals(500, reportManager.revenue());
        verify(inventoryDAO, times(1)).totalRevenue();
    }

    @Test
    public void testRevenueThrows_NotFound() {
        when(inventoryDAO.totalRevenue()).thenReturn(-1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportManager.revenue();
        });
        assertEquals("Revenue not found!", exception.getMessage());
        verify(inventoryDAO, times(1)).totalRevenue();

    }

    @Test
    public void testArcaneAndNonArcaneMembers() {
        Customer c1 = mock(Customer.class);
        Customer c2 = mock(Customer.class);
        when(c1.isArcaneMember()).thenReturn(true);
        when(c2.isArcaneMember()).thenReturn(false);

        ArrayList<Customer> customers = new ArrayList<>();
        customers.add(c1);
        customers.add(c2);

        when(accountDAO.viewAllCustomers()).thenReturn(customers);

        assertEquals(1, reportManager.numberOfArcaneMembers());
        assertEquals(1, reportManager.numberOfNonArcaneMembers());
        assertEquals(2, reportManager.numberOfCustomers());
        verify(accountDAO, times(3)).viewAllCustomers();

        int[] percentages = reportManager.percentageOfArcane();
        assertEquals(50, percentages[0]);
        assertEquals(50, percentages[1]);
    }

    @Test
    public void testCreateCategorySalesChartNotNull() {
        JFreeChart chart = ReportManager.createCategorySalesChart();
        assertNotNull(chart);
    }

    @Test
    public void testCreateProductSalesChartNotNull() {
        JFreeChart chart = ReportManager.createProductSalesChart();
        assertNotNull(chart);
    }

}
