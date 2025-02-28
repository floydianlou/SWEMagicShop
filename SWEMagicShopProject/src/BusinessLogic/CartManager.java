package BusinessLogic;

import DomainModel.Customer;
import DomainModel.Item;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.Gson;
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

    public void clearCart() {
        cartItems.clear();
    }

    

}