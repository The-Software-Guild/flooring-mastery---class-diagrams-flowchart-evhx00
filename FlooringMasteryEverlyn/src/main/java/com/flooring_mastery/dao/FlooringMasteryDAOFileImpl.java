
package com.flooring_mastery.dao;

import com.flooring_mastery.model.Order;
import com.flooring_mastery.model.Product;
import com.flooring_mastery.model.Tax;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 *
 * @author everlynleon
 */
//@Component
public class FlooringMasteryDAOFileImpl implements FlooringMasteryDAO {

    private final Map<String, Order> orders = new HashMap<>();
    private final Map<String, Product> product = new HashMap<>();
    private final Map<String, Tax> tax = new HashMap<>();

    private final File folder = new File("SampleFileData/Orders");

    public final String ROSTER_FILE;

    public static final String TAX_FILE = "SampleFileData/Data/Taxes.txt";

    public static final String PRODUCT_FILE = "SampleFileData/Data/Products.txt";

    public static final String DELIMITER = ",";

    public FlooringMasteryDAOFileImpl(){
        ROSTER_FILE = "SampleFileData/Orders/";
    }

    public FlooringMasteryDAOFileImpl(String rosterTextFile){
        ROSTER_FILE = rosterTextFile;
    }

    @Override
    public ArrayList<File> getAllFiles() {
        return new ArrayList<>(Arrays.asList(Objects.requireNonNull(folder.listFiles())));
    }

    @Override
    public void addOrder(String orderNumber, Order order, String fileName) throws FlooringMasteryPersistenceException {
           ArrayList<File> files = getAllFiles();
           Order prev;
            for(File file: files){
                if(file.getName().contains(fileName)){
                     loadRoster(fileName);
                     prev = orders.put(orderNumber,order);
                     write(fileName);
                     return;
                }
            }

            File newFile = new File(fileName);
            files.add(newFile);
            prev = orders.put(orderNumber,order);
            write(fileName);
    }

    @Override
    public List<Order> getAllOrders(String fileName) throws FlooringMasteryPersistenceException{
        loadRoster(fileName);
        return new ArrayList<>(orders.values());
    }

    @Override
    public Order getOrder(String orderNumber,String fileName) throws FlooringMasteryPersistenceException {
        loadRoster(fileName);
        return orders.get(orderNumber);
    }

    @Override
    public List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException{
        loadTaxes();
        return new ArrayList<>(tax.values());
    }

    @Override
    public List<String> getAllStateTax() throws FlooringMasteryPersistenceException{
        loadTaxes();
        ArrayList<String> states = new ArrayList<>();
        List<Tax> list = getAllTaxes();
        for(Tax tax: list){
            states.add(tax.getStateAbbreviation());
        }
        return states;
    }

    @Override
    public Tax getTax(String state) throws FlooringMasteryPersistenceException {
        loadTaxes();
        return tax.get(state);
    }

    @Override
    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException{
        loadProducts();
        return new ArrayList<>(product.values());
    }

    @Override
    public List<String> getAllStates() throws FlooringMasteryPersistenceException{
        loadTaxes();
        ArrayList<String> states = new ArrayList<>();
        List<Tax> list = getAllTaxes();
        for(Tax tax: list){
            states.add(tax.getStateName());
        }
        return states;
    }

    @Override
    public List<String> getAllProductTypes() throws FlooringMasteryPersistenceException{
        loadProducts();
        ArrayList<String> pType = new ArrayList<>();
        List<Product> list = getAllProducts();
        for(Product p: list){
            pType.add(p.getProductType());
        }
        return pType;
    }

    @Override
    public Product getProduct(String productType) throws FlooringMasteryPersistenceException {
        loadProducts();
        return product.get(productType);
    }

    public Order calculateOrderInfo(Order prevOrder, Tax specificTax, Product specificProduct){
        BigDecimal orderArea = prevOrder.getArea();

        prevOrder.setTaxRate(specificTax.getTaxRate());

        prevOrder.setCostPerSquareFoot(specificProduct.getCostPerSquareFoot());

        prevOrder.setLaborCostPerSquareFoot(specificProduct.getLaborCostPerSquareFoot());

        BigDecimal materialCost = orderArea.multiply(specificProduct.getCostPerSquareFoot());
        prevOrder.setMaterialCost(materialCost);

        BigDecimal laborCost = orderArea.multiply(specificProduct.getLaborCostPerSquareFoot());
        prevOrder.setLaborCost(laborCost);

        BigDecimal costs = materialCost.add(laborCost);
        BigDecimal hundred = new BigDecimal("100");
        BigDecimal newTaxRate = specificTax.getTaxRate().divide(hundred);
        BigDecimal theTax = costs.multiply(newTaxRate);
        prevOrder.setTax(theTax.setScale(2, RoundingMode.HALF_UP));

        BigDecimal total = materialCost.add(laborCost);
        BigDecimal grandTotal = total.add(theTax);
        prevOrder.setTotal(grandTotal.setScale(2, RoundingMode.HALF_UP));
        return prevOrder;

    }


    @Override
    public Order removeOrder(String orderNumber, String fileName) throws FlooringMasteryPersistenceException{
        loadRoster(fileName);
        Order removedOrder = orders.remove(orderNumber);
        write(fileName);
        return removedOrder;
    }

    @Override
    public void editOrder(Order editOrder, ArrayList<String> newValues, String fileName) throws FlooringMasteryPersistenceException{
        loadRoster(fileName);
        String newCustomerName = newValues.get(0);
        String newState = newValues.get(1);
        String newProductType = newValues.get(2);
        String newArea = newValues.get(3);

        editOrder.setCustomerName(newCustomerName);
        editOrder.setState(newState);
        editOrder.setProductType(newProductType);
        editOrder.setArea(new BigDecimal(newArea));

        orders.replace(editOrder.getOrderNumber(), editOrder);
        write(fileName);
    }

    private Order unmarshallOrder(String orderAsText){
        String[] orderTokens = orderAsText.split(DELIMITER);

        String orderId = orderTokens[0];
        Order orderFromFile = new Order(orderId);
        String customerName = orderTokens[1];
        String state = orderTokens[2];
        BigDecimal taxRate = new BigDecimal(orderTokens[3]);
        String productType = orderTokens[4];
        BigDecimal area = new BigDecimal(orderTokens[5]);
        BigDecimal costPerSquareFoot = new BigDecimal(orderTokens[6]);
        BigDecimal laborCostPerSquareFoot = new BigDecimal(orderTokens[7]);
          BigDecimal materialCost = new BigDecimal(orderTokens[8]);
          BigDecimal laborCost = new BigDecimal(orderTokens[9]);
          BigDecimal tax = new BigDecimal(orderTokens[10]);
          BigDecimal total = new BigDecimal(orderTokens[11]);

        return new Order(orderId, customerName, state, taxRate,
        productType, area, costPerSquareFoot, laborCostPerSquareFoot,
        materialCost, laborCost, tax, total);
    }

    private void loadRoster(String fileName) throws FlooringMasteryPersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(ROSTER_FILE + fileName)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException("Could not load data.", e);
        }

        String currentLine;

        Order currentOrder;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentOrder = unmarshallOrder(currentLine);

            orders.put(currentOrder.getOrderNumber(), currentOrder);
        }
        scanner.close();
    }

    private String marshallOrder(Order aOrder){

        String orderAsText = aOrder.getOrderNumber() + DELIMITER;

        orderAsText += aOrder.getCustomerName() + DELIMITER;
        orderAsText += aOrder.getState() + DELIMITER;
        orderAsText += aOrder.getTaxRate() + DELIMITER;
        orderAsText += aOrder.getProductType()+ DELIMITER;
        orderAsText += aOrder.getArea() + DELIMITER;
        orderAsText += aOrder.getCostPerSquareFoot() + DELIMITER;
        orderAsText += aOrder.getLaborCostPerSquareFoot() + DELIMITER;
        orderAsText += aOrder.getMaterialCost()+ DELIMITER;
        orderAsText += aOrder.getLaborCost() + DELIMITER;
        orderAsText += aOrder.getTax() + DELIMITER;
        orderAsText += aOrder.getTotal();
        return orderAsText;
    }

    private void loadTaxes() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(TAX_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException("Could not load data.", e);
        }

        String currentLine;
        Tax currentTax;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentTax = unmarshallTax(currentLine);

            tax.put(currentTax.getStateName(), currentTax);
        }
        scanner.close();
    }

    private void write(String fileName) throws FlooringMasteryPersistenceException {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(ROSTER_FILE  + fileName));
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException("Could not save data.", e);
        }

        String orderAsText;
        List<Order> orderList = this.getAllOrders(fileName);
        for (Order currentOrder : orderList) {
            orderAsText = marshallOrder(currentOrder);
            out.println(orderAsText);
            out.flush();
        }
        out.close();
    }

    private Product unmarshallProduct(String productAsText){
        String[] productTokens = productAsText.split(DELIMITER);
        String productType = productTokens[0];
        Product productFromFile = new Product(productType);
        BigDecimal costPerSquareFoot = new BigDecimal(productTokens[1]);
        BigDecimal laborCostPerSquareFoot = new BigDecimal(productTokens[2]);
        return new Product(productType, costPerSquareFoot, laborCostPerSquareFoot);
    }


    @Override
    public String generateNextOrderNumber(String fileName) throws FlooringMasteryPersistenceException{
        ArrayList<File> files = getAllFiles();
        int orderNum;
        for(File file: files){
            if(file.getName().contains(fileName)){
                loadRoster(fileName);
                ArrayList<String> orderNumbers = new ArrayList<>(orders.keySet());
                for(String key : orderNumbers){
                    System.out.println("Order Number: " + key);
                }
                String last = orderNumbers.get(orderNumbers.size() - 1);
                System.out.println("Last Number: " + last);

                orderNum = Integer.parseInt(last);

                orderNum++;
                return String.valueOf(orderNum);
            }
        }
        return "1";

    }
    private Tax unmarshallTax(String taxAsText){
        String[] taxTokens = taxAsText.split(DELIMITER);
        String stateAB = taxTokens[0];

        Tax taxFromFile = new Tax(stateAB);
        String stateName = taxTokens[1];
        BigDecimal taxRate = new BigDecimal(taxTokens[2]);

        return new Tax(stateAB, stateName, taxRate);
    }
    private void loadProducts() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                    "Could not load data.", e);
        }
        String currentLine;
        Product currentProduct;
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);

            product.put(currentProduct.getProductType(), currentProduct);
        }
        scanner.close();
    }


}
