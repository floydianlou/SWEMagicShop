package GUI.controller;

import BusinessLogic.ReportManager;
import DomainModel.Manager;
import DomainModel.Customer;
import BusinessLogic.Utilities;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import org.jfree.chart.JFreeChart;
import java.awt.image.BufferedImage;

public class ManagerStatisticsViewController {

    private Manager loggedManager;
    private ReportManager reportManager;

    @FXML private Label totalRevenueGP;
    @FXML private Label totalRevenueSP;
    @FXML private Label totalRevenueCP;

    // Client statistics
    @FXML private Label biggestSpenderName;
    @FXML private Label biggestSpenderSurname;
    @FXML private Label smallestSpenderName;
    @FXML private Label smallestSpenderSurname;
    @FXML private Label biggestSpenderMoneyGP;
    @FXML private Label biggestSpenderMoneySP;
    @FXML private Label biggestSpenderMoneyCP;
    @FXML private Label smallestSpenderMoneyGP;
    @FXML private Label smallestSpenderMoneySP;
    @FXML private Label smallestSpenderMoneyCP;
    @FXML private Label totalNumberOfClients;
    @FXML private Label numberOfArcaneClients;
    @FXML private Label numberOfNonArcaneClients;
    @FXML private Label percentageOfArcaneClients;
    @FXML private Label percentageOfNonArcaneClients;

    // Category statistics
    @FXML private Label hotCategory;
    @FXML private Label uglyCategory;
    @FXML private Label hotCategoryNumberSold;
    @FXML private Label uglyCategoryNumberSold;

    // Product statistics
    @FXML private Label hotProduct;
    @FXML private Label uglyProduct;
    @FXML private Label hotProductNumber;
    @FXML private Label uglyProductNumber;

    // Charts
    @FXML private Canvas categoryChartCanvas;
    @FXML private Canvas productChartCanvas;
    @FXML private Canvas categoryChartCanvas2;
    @FXML private Canvas productChartCanvas2;

    // Containers
    @FXML private VBox clientsContainer;
    @FXML private VBox revenueContainer;
    @FXML private VBox productsContainer;
    @FXML private VBox categoryContainer;

    @FXML
    public void initialize(){
        loggedManager = (Manager) LoggedUserManager.getInstance().getLoggedUser();
        reportManager = new ReportManager();
        loadStatistics();
        loadCategorySalesChart();
        loadProductSalesChart();
    }

    @FXML
    private void loadStatistics(){
        // Revenue statistics
        int[] totalRevenue = Utilities.normalizeCurrencyArray(reportManager.revenue());
        totalRevenueGP.setText(String.valueOf(totalRevenue[0]));
        totalRevenueSP.setText(String.valueOf(totalRevenue[1]));
        totalRevenueCP.setText(String.valueOf(totalRevenue[2]));

        // Client statistics
        Customer biggestSpender = reportManager.viewBiggestSpender();
        Customer smallestSpender = reportManager.viewSmallestSpender();
        biggestSpenderName.setText(biggestSpender.getName());
        biggestSpenderSurname.setText(biggestSpender.getSurname());
        smallestSpenderName.setText(smallestSpender.getName());
        smallestSpenderSurname.setText(smallestSpender.getSurname());
        int[] biggestSpenderMoney = Utilities.normalizeCurrencyArray(reportManager.viewTotalSpentByCustomerId(biggestSpender.getPersonID()));
        int[] smallestSpenderMoney = Utilities.normalizeCurrencyArray(reportManager.viewTotalSpentByCustomerId(smallestSpender.getPersonID()));
        biggestSpenderMoneyGP.setText(String.valueOf(biggestSpenderMoney[0]));
        biggestSpenderMoneySP.setText(String.valueOf(biggestSpenderMoney[1]));
        biggestSpenderMoneyCP.setText(String.valueOf(biggestSpenderMoney[2]));
        smallestSpenderMoneyGP.setText(String.valueOf(smallestSpenderMoney[0]));
        smallestSpenderMoneySP.setText(String.valueOf(smallestSpenderMoney[1]));
        smallestSpenderMoneyCP.setText(String.valueOf(smallestSpenderMoney[2]));
        totalNumberOfClients.setText(String.valueOf(reportManager.numberOfCustomers()));
        numberOfArcaneClients.setText(String.valueOf(reportManager.numberOfArcaneMembers()));
        numberOfNonArcaneClients.setText(String.valueOf(reportManager.numberOfNonArcaneMembers()));
        int [] percentages = reportManager.percentageOfArcane();
        percentageOfArcaneClients.setText(String.valueOf(percentages[0]));
        percentageOfNonArcaneClients.setText(String.valueOf(percentages[1]));

        // Category statistics
        String hotCategoryName = reportManager.hotCategory();
        String uglyCategoryName = reportManager.uglyCategory();
        hotCategory.setText(hotCategoryName);
        uglyCategory.setText(uglyCategoryName);
        hotCategoryNumberSold.setText(String.valueOf(reportManager.categoryNum(hotCategoryName)));
        uglyCategoryNumberSold.setText(String.valueOf(reportManager.categoryNum(uglyCategoryName)));

        // Product statistics
        String hotProductName = reportManager.hotProduct().getItemName();
        String uglyProductName = reportManager.uglyProduct().getItemName();
        hotProduct.setText(hotProductName);
        uglyProduct.setText(uglyProductName);
        hotProductNumber.setText(String.valueOf(reportManager.productNumById(reportManager.hotProduct().getItemID())));
        uglyProductNumber.setText(String.valueOf(reportManager.productNumById(reportManager.uglyProduct().getItemID())));
    }

    @FXML
    private void loadCategorySalesChart() {
        JFreeChart categoryChart = ReportManager.createCategorySalesChart();
        drawChartOnCanvas(categoryChart, categoryChartCanvas);
        drawChartOnCanvas(categoryChart, categoryChartCanvas2);
    }

    @FXML
    private void loadProductSalesChart() {
        JFreeChart productChart = ReportManager.createProductSalesChart();
        drawChartOnCanvas(productChart, productChartCanvas);
        drawChartOnCanvas(productChart, productChartCanvas2);
    }

    private void drawChartOnCanvas(JFreeChart chart, Canvas canvas) {
        // Converte il grafico in un'immagine
        BufferedImage bufferedImage = chart.createBufferedImage(
                (int) canvas.getWidth(),
                (int) canvas.getHeight()
        );
        Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);

        // Disegna l'immagine sul Canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(fxImage, 0, 0);
    }

    // Containers view methods
    @FXML
    private void showClients() {
        setVisibility(clientsContainer);
    }

    @FXML
    private void showRevenue() {
        setVisibility(revenueContainer);
    }

    @FXML
    private void showProducts() {
        setVisibility(productsContainer);
    }

    @FXML
    private void showCategory() {
        setVisibility(categoryContainer);
    }

    private void setVisibility(VBox visibleContainer) {
        clientsContainer.setVisible(false);
        revenueContainer.setVisible(false);
        productsContainer.setVisible(false);
        categoryContainer.setVisible(false);

        visibleContainer.setVisible(true);
    }
}
