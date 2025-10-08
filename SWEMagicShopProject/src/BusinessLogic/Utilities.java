package BusinessLogic;

import DomainModel.Species;
import DAO.CategoryDAO;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Utilities {
    public static boolean checkEmail (String email) {
        String emailChecker = "^[a-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$";
        return email.matches(emailChecker);
    }

    public static boolean checkPassword (String password) {
        String notAllowedChars = "[!#$%^&*()+=\\[\\]{};:'\",<>?/\\\\|]";
        return !password.matches("." + notAllowedChars + ".");
    }

    public static boolean checkAgeLimit (Species species, int age) {
        return age >= species.getLegalAge() && age <= species.getLimitAge();
    }

    public static boolean checkPhone (String number) {
        return number.matches("\\d{10}");
    }

    public static boolean validManagerPassword(String inputPassword) {
        String requiredPassword = "ManagerAccountCreation";
        return inputPassword.equals(requiredPassword);
    }

    public static void initialiseSavesFolder() {
        File saveFiles = new File("data/cart");
        if (!saveFiles.exists() && !saveFiles.mkdirs()) {
            System.out.println("Unable to create folder");
        }
    }

    // Money conversion 10CP = 1SP and 10SP = 1GP
    public static String normalizeCurrency(int copperAmount) {
        int SPbalance = copperAmount / 10;
        copperAmount = copperAmount % 10;

        int GPbalance = SPbalance / 10;
        SPbalance = SPbalance % 10;

        return ("GP:" + GPbalance + "\n SP:" + SPbalance + "\n CP:" + copperAmount);
    }

    public static int[] normalizeCurrencyArray(int copperAmount) {
        int SPbalance = copperAmount / 10;
        copperAmount = copperAmount % 10;

        int GPbalance = SPbalance / 10;
        SPbalance = SPbalance % 10;

        return new int[]{GPbalance, SPbalance, copperAmount};

    }

    // to check if the category selected is inside the database
    public static boolean checkCategory(String category) {
        CategoryDAO categoryDAO = new CategoryDAO();
        ArrayList<String> categories = categoryDAO.viewAllCategories();
        for (String c : categories) {
            if (c.equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }

    public static int checkBooleanArcane(boolean arcane) {
        if(arcane == true || arcane == false){
            return 1;
        }
        else return 0;
    }

    public static String formatOrderDate(String rawDate) {
        try {
            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSSSSS][.SSSSS][.SSSS][.SSS][.SS][.S]");
            DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime parsedDate = LocalDateTime.parse(rawDate, inputFormat);
            return outputFormat.format(parsedDate);
        } catch (Exception e) {
            System.err.println("Data format error: " + rawDate);
            return rawDate;
        }
    }

    public static void stylePlot(org.jfree.chart.JFreeChart chart) {
        var plot = chart.getCategoryPlot();

        chart.setBackgroundPaint(new java.awt.Color(0,0,0,0));
        plot.setBackgroundPaint(new java.awt.Color(0,0,0,0));
        plot.setBackgroundAlpha(0f);
        plot.setOutlineVisible(false);

        java.awt.Color BRAND_VIOLET = new java.awt.Color(0x46,0x0E,0x56);
        java.awt.Color LILAC_SOFT   = new java.awt.Color(0xD2,0xB4,0xDE, 120);

        plot.setRangeGridlinePaint(LILAC_SOFT);
        plot.getDomainAxis().setAxisLinePaint(BRAND_VIOLET);
        plot.getDomainAxis().setTickMarkPaint(BRAND_VIOLET);
        plot.getRangeAxis().setAxisLinePaint(BRAND_VIOLET);
        plot.getRangeAxis().setTickMarkPaint(BRAND_VIOLET);

        java.awt.Color WHITE = java.awt.Color.WHITE;
        plot.getDomainAxis().setLabelPaint(WHITE);
        plot.getDomainAxis().setTickLabelPaint(WHITE);
        plot.getRangeAxis().setLabelPaint(WHITE);
        plot.getRangeAxis().setTickLabelPaint(WHITE);

        if (chart.getTitle() != null) {
            chart.getTitle().setPaint(WHITE);
            chart.getTitle().setFont(new java.awt.Font("Belwe Bd BT", java.awt.Font.BOLD, 20));
        }

        if (plot.getRenderer() instanceof org.jfree.chart.renderer.category.BarRenderer) {
            final java.awt.Color[] PURPLES = new java.awt.Color[] {
                    new java.awt.Color(0x46,0x0E,0x56),
                    new java.awt.Color(0x6C,0x2E,0x8E),
                    new java.awt.Color(0x8E,0x44,0xAD),
                    new java.awt.Color(0xA2,0x6D,0xB2),
                    new java.awt.Color(0xBE,0x90,0xD4),
                    new java.awt.Color(0xD2,0xB4,0xDE)
            };

            org.jfree.chart.renderer.category.BarRenderer r = new org.jfree.chart.renderer.category.BarRenderer() {
                @Override
                public java.awt.Paint getItemPaint(int row, int column) {
                    // colore per CATEGORIA (column), indipendente dalla serie
                    return PURPLES[column % PURPLES.length];
                }
            };
            r.setShadowVisible(false);
            r.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
            r.setDrawBarOutline(false);
            r.setMaximumBarWidth(0.10);
            r.setItemMargin(0.10);

            r.setDefaultItemLabelsVisible(true);
            r.setDefaultItemLabelPaint(WHITE);
            r.setDefaultItemLabelFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 12));
            r.setDefaultItemLabelGenerator(
                    new org.jfree.chart.labels.StandardCategoryItemLabelGenerator(
                            "{2}", java.text.NumberFormat.getIntegerInstance()
                    )
            );

            r.setItemLabelAnchorOffset(2.0);

            r.setDefaultPositiveItemLabelPosition(
                    new org.jfree.chart.labels.ItemLabelPosition(
                            org.jfree.chart.labels.ItemLabelAnchor.INSIDE12,
                            org.jfree.chart.ui.TextAnchor.TOP_CENTER
                    )
            );
            r.setDefaultNegativeItemLabelPosition(
                    new org.jfree.chart.labels.ItemLabelPosition(
                            org.jfree.chart.labels.ItemLabelAnchor.INSIDE6,
                            org.jfree.chart.ui.TextAnchor.BOTTOM_CENTER
                    )
            );

                r.setPositiveItemLabelPositionFallback(
                        new org.jfree.chart.labels.ItemLabelPosition(
                                org.jfree.chart.labels.ItemLabelAnchor.OUTSIDE12,
                                org.jfree.chart.ui.TextAnchor.BOTTOM_CENTER
                        )
                );
                r.setNegativeItemLabelPositionFallback(
                        new org.jfree.chart.labels.ItemLabelPosition(
                                org.jfree.chart.labels.ItemLabelAnchor.OUTSIDE6,
                                org.jfree.chart.ui.TextAnchor.TOP_CENTER
                        )
                );

            plot.setRenderer(r);
        }
    }
}
