package BusinessLogic;

import DomainModel.Customer;
import DomainModel.Item;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class CartManager {
    ArrayList<Item> cartItems;
    String cartSavePath;

    public CartManager(Customer customer) {
        this.cartSavePath = "data/cart_" + customer.getPersonID() + ".json";
        this.cartItems = loadCartItems(cartSavePath);
    }

    public ArrayList<Item> loadCartItems (String jsonPath) {
        ArrayList<Item> loadedCart = new ArrayList<>();
        File cartFile = new File(jsonPath);

        if (!cartFile.exists())
            return loadedCart;
        else {
            try (FileReader reader = new FileReader(jsonPath)){
                Gson gson = new Gson();
                loadedCart = gson.fromJson(reader, new TypeToken<ArrayList<Item>>() {}.getType());
                return (loadedCart != null) ? loadedCart : new ArrayList<>();
            } catch (IOException cartError) {
                System.err.println("Something went wrong while loading your cart...");
                return new ArrayList<>();
            }
        }

    }

    public void saveCartToJson() {
        if (cartItems.isEmpty()) {
            File checking = new File(cartSavePath);
            if (checking.exists())
                checking.delete(); // Checks if file already existed and deletes it since cart is empty
        } else {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(cartSavePath)) {
                gson.toJson(cartItems, writer);
            } catch (IOException cartError) {
                System.err.println("Something went wrong while saving your cart...");
                File cartFile = new File(cartSavePath);
                if (cartFile.exists()) {
                    cartFile.delete(); // Checks if file already existed and if so, deletes it to avoid corrupt files
                }
            }
        }
    }

    public ArrayList<Item> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public void addItemToCart (Item item) {
        for (Item i : cartItems) {
            if (i.getItemID() == item.getItemID()) {
                i.setItemQuantity(i.getItemQuantity() + item.getItemQuantity());
                return; }
        }
        cartItems.add(item);
    }

    public void increaseItemQuantity (Item item) {
        for (Item i : cartItems) {
            if (i.getItemID() == item.getItemID()) {
                i.setItemQuantity(i.getItemQuantity() + 1);
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
                break;
            }
        }
    }

    public void removeItemFromCart (Item item) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getItemID() == item.getItemID()) {
                cartItems.remove(i);
                break;
            }
        }
    }
}