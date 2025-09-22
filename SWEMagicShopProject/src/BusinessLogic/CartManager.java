package BusinessLogic;

import DomainModel.Customer;
import DomainModel.Item;
import Exceptions.OrderExceptions.ItemNotInCartException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class CartManager {

    // to automatically save cart
    private ScheduledExecutorService scheduler;
    private boolean cartModified = false;

    private static CartManager instance;
    ArrayList<Item> cartItems;
    String cartSavePath;

    public CartManager(Customer customer) {
        Utilities.initialiseSavesFolder();
        this.cartSavePath = "data/cart_" + customer.getPersonID() + ".json";
        this.cartItems = loadCartItems(cartSavePath);
        startAutoSave();
    }

    public static void init(Customer customer) {
        resetInstance();
        instance = new CartManager(customer);
    }

    public static CartManager getInstance() {
        return instance;
    }

    public ArrayList<Item> loadCartItems (String jsonPath) {
        ArrayList<Item> loadedCart = new ArrayList<>();
        File cartFile = new File(jsonPath);

        if (!cartFile.exists()) return loadedCart;
        else {
            try (FileReader reader = new FileReader(jsonPath)) {
                Gson gson = new Gson();
                loadedCart = gson.fromJson(reader, new TypeToken<ArrayList<Item>>() {}.getType());
                return (loadedCart != null) ? loadedCart : new ArrayList<>();
            } catch (JsonSyntaxException | IOException cartError) {
                System.err.println("Something went wrong while loading your cart...");
                cartFile.delete();
                return new ArrayList<>();
            }
        }

    }

    public void closeCartSession() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
        saveCart();
        cartItems.clear();
    }

    public static void resetInstance() {
        if (instance != null) {
            instance.closeCartSession();
            instance = null;
        }
    }

    public ArrayList<Item> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
        cartModified = true;
    }

    public void addItemToCart (Item item) {
        for (Item i : cartItems) {
            if (i.getItemID() == item.getItemID()) {
                i.setItemQuantity(i.getItemQuantity() + item.getItemQuantity());
                cartModified = true;
                return; }
        }
        cartItems.add(item);
        cartModified = true;
    }

    public void increaseItemQuantity (Item item) {
        for (Item i : cartItems) {
            if (i.getItemID() == item.getItemID()) {
                i.setItemQuantity(i.getItemQuantity() + 1);
                cartModified = true;
                return; }
        }
    }

    public void reduceItemQuantity (Item item) {
        for (int i = 0; i < cartItems.size(); i++) {
            Item currentItem = cartItems.get(i);
            if (currentItem.getItemID() == item.getItemID()) {
                currentItem.setItemQuantity(currentItem.getItemQuantity() - 1);
                if (currentItem.getItemQuantity() <= 0)
                    cartItems.remove(i);
                cartModified = true;
                break;
            }
        }
    }

    public void removeItemFromCart (Item item) throws ItemNotInCartException {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getItemID() == item.getItemID()) {
                cartItems.remove(i);
                cartModified = true;
                return;
            }
        } throw new ItemNotInCartException("There was no such item in your cart!");
    }

    public void setItemQuantity(Item item, int quantity) {
        for (Item i : cartItems) {
            if (i.getItemID() == item.getItemID()) {
                i.setItemQuantity(quantity);
                cartModified = true;
                return; }
        }
    }

    public void saveCart() {
        if (cartItems.isEmpty()) {
            File checking = new File(cartSavePath);
            if (checking.exists())
                checking.delete(); // checks if file already existed and deletes it since cart is empty
        } else {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(cartSavePath)) {
                gson.toJson(cartItems, writer);
            } catch (IOException cartError) {
                System.err.println("Something went wrong while saving your cart...");
                File cartFile = new File(cartSavePath);
                if (cartFile.exists()) {
                    cartFile.delete(); // checks if file already existed and if so, deletes it to avoid corrupt files
                }
            }
        }
    }

    private void startAutoSave() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            if (cartModified) {
                saveCart();
                cartModified = false;
            }
        }, 10, 15, TimeUnit.SECONDS);
    }

}