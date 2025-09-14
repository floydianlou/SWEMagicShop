package tests.DomainModelTest;

import DomainModel.Wallet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    void testConstructorAndGetters() {
        Wallet wallet = new Wallet(1, 100);
        assertEquals(1, wallet.getWalletID());
        assertEquals(100, wallet.getCPbalance());
    }

    @Test
    void testSetWalletID() {
        Wallet wallet = new Wallet(1, 50);
        wallet.setWalletID(10);
        assertEquals(10, wallet.getWalletID());
    }

    @Test
    void testSetCPbalance() {
        Wallet wallet = new Wallet(1, 50);
        wallet.setCPbalance(200);
        assertEquals(200, wallet.getCPbalance());
    }
}

