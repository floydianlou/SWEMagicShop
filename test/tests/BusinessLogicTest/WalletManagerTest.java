package tests.BusinessLogicTest;

import BusinessLogic.WalletManager;
import DomainModel.Customer;
import DomainModel.Wallet;
import ORM.WalletDAO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class WalletManagerTest {

    private Customer customer;
    private WalletManager walletManager;
    private Wallet wallet;
    private WalletDAO walletDAO;

    @BeforeEach
    public void setUp() {
        customer = mock(Customer.class);
        wallet = mock(Wallet.class);
        walletDAO = mock(WalletDAO.class);
        walletManager = new WalletManager(walletDAO);

        when(customer.getPersonID()).thenReturn(1);
        when(walletDAO.getWalletByID(1)).thenReturn(wallet);
        when(wallet.getCPbalance()).thenReturn(150); // Example balance
    }

    @AfterEach
    public void tearDown() {
        customer = null;
        wallet = null;
        walletManager = null;
        walletDAO = null;
    }

    @Test
    public void viewBalance() {


        int balance = walletManager.viewBalance(1);

        assertEquals(150, balance);
        verify(walletDAO, times(1)).getWalletByID(1);
    }

    @Test
    public void viewBalance_WalletNotFound() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            walletManager.viewBalance(2);
        });

        assertEquals("Wallet not found for this customer", exception.getMessage());
        verify(walletDAO, times(1)).getWalletByID(1);
    }

    @Test
    public void addFunds() {

        int gp = 1, sp = 2, cp = 3; // Totale: 123 CP

        walletManager.addFunds(gp, sp, cp, customer);

        assertEquals(173, walletManager.viewBalance(1));
        verify(wallet, times(1)).setCPbalance(173);
        verify(walletDAO, times(1)).updateWallet(wallet);
    }

    @Test
    public void withdrawFunds() {

        int withdrawAmount = 50; // Prelievo di 50 CP

        boolean result = walletManager.withdrawFunds(withdrawAmount, customer);

        assertTrue(result);
        verify(wallet, times(1)).setCPbalance(100);
        verify(walletDAO, times(1)).updateWallet(wallet);
    }

    @Test
    public void withdrawFunds_InsufficientBalance() {

        int withdrawAmount = 200; // Importo superiore al saldo

        boolean result = walletManager.withdrawFunds(withdrawAmount, customer);

        assertFalse(result);
        verify(wallet, never()).setCPbalance(anyInt());
        verify(walletDAO, never()).updateWallet(wallet);
    }
}