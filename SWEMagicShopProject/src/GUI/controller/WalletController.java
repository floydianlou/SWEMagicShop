package GUI.controller;

import BusinessLogic.Utilities;
import BusinessLogic.WalletManager;
import DomainModel.Customer;
import DomainModel.Wallet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WalletController {

    private Customer loggedCustomer;

    @FXML private Label GPwallet;
    @FXML private Label SPwallet;
    @FXML private Label CPwallet;
    @FXML private TextField GPwalletInput;
    @FXML private TextField SPwalletInput;
    @FXML private TextField CPwalletInput;

    @FXML private ImageView coinIcon1;
    @FXML private ImageView coinIcon2;

    @FXML private Label errorLabel;

    @FXML private Button rechargeButton;
    @FXML private Button topUpButton;

    private WalletManager walletManager;


    @FXML
    public void initialize() {
        loggedCustomer = (Customer) LoggedUserManager.getInstance().getLoggedUser();
        walletManager = new WalletManager();
        loadWallet();
        coinIcon1.setImage(new Image(getClass().getResource("/images/goldCoin.png").toExternalForm()));
        coinIcon2.setImage(new Image(getClass().getResource("/images/goldCoin.png").toExternalForm()));
    }

    @FXML
    private void loadWallet(){
        Wallet wallet = walletManager.getWalletByCustomer(loggedCustomer);
        int[] walletValue = Utilities.normalizeCurrencyArray(wallet.getCPbalance());
        GPwallet.setText(String.valueOf(walletValue[0]));
        SPwallet.setText(String.valueOf(walletValue[1]));
        CPwallet.setText(String.valueOf(walletValue[2]));
    }

    @FXML
    private void topUpWallet() {
        GPwalletInput.setEditable(true);
        SPwalletInput.setEditable(true);
        CPwalletInput.setEditable(true);
        rechargeButton.setVisible(true);
        topUpButton.setVisible(false);
    }

    @FXML
    private void rechargeWallet(){
        int GP = 0;
        int SP = 0;
        int CP = 0;
        try{
            GP = GPwalletInput.getText().isEmpty()? 0:  Integer.parseInt(GPwalletInput.getText());
            SP = SPwalletInput.getText().isEmpty()? 0:  Integer.parseInt(SPwalletInput.getText());
            CP = CPwalletInput.getText().isEmpty()? 0:  Integer.parseInt(CPwalletInput.getText());
        }catch(NumberFormatException e){
            errorLabel.setText("Select a numeric value for money input.");            errorLabel.setVisible(true);
            errorLabel.setVisible(true);
            resetButton();
            return;
        }
        try{
            walletManager.addFunds(GP, SP, CP, loggedCustomer);
            popupWalletEdited();
        }catch (Exception e){
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
            resetButton();
            return;
        }
        errorLabel.setVisible(false);
        resetButton();
        loadWallet();
    }

    private void resetButton(){
        GPwalletInput.setEditable(false);
        SPwalletInput.setEditable(false);
        CPwalletInput.setEditable(false);
        GPwalletInput.clear();
        SPwalletInput.clear();
        CPwalletInput.clear();
        rechargeButton.setVisible(false);
        topUpButton.setVisible(true);
    }

    private void popupWalletEdited() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/popup.fxml"));
            Parent root = loader.load();

            PopupController popupController = loader.getController();
            popupController.setPopupContent("Wallet Recharged", "Your wallet has been successfully recharged.", "OK");

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.setTitle("Account Updated");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            popupStage.setScene(scene);
            popupStage.setResizable(false);
            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}