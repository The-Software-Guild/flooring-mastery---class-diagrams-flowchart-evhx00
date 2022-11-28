
package com.flooring_mastery.view;

import com.flooring_mastery.model.Order;
import com.flooring_mastery.model.Product;
import com.flooring_mastery.model.Tax;
import com.flooring_mastery.userio.UserIO;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author everlynleon
 */
public class FlooringMasteryView {
    
    private final UserIO io;
    
    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }
    
    public int printMenuAndGetSelection() {
        io.print("   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
        io.print("   * <<Flooring Program>>");
        io.print("   * 1. Display Orders");
        io.print("   * 2. Add an Order");
        io.print("   * 3. Edit an Order");
        io.print("   * 4. Remove an Order");
        io.print("   * 5. Export All Data");
        io.print("   * 6. Quit");
        io.print("   *");
        io.print("   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

        return io.readInt("Please select from the choices.", 1, 6);
    }
    
    public HashMap<String,Order> getNewOrderInfo(List<String> states, List<String> stateAB, List<Product> products, List<String> productTypes) {
        HashMap<String,Order> answer= new HashMap<>();
        String orderDate = getDateCreate();
        String customerName;
        boolean flag;
        do{
        customerName = io.readString("Please enter the Customer Name: ");
        if(customerName == null || customerName.trim().isEmpty()){
            io.print("Name Required");
            flag = true;
        }else{
            flag = false;
        }
        
        }while(flag);
        
        String state;
        do{
            state = io.readString("Please enter State Name:");
            if(state == null || state.trim().isEmpty()){
                io.print("State Required");
                flag = true;
            }else if(!states.contains(state)){
                io.print("STATE: "+ state);
                io.print("State Does not Exist.");
                flag = true;
            }else{
                flag=false;
            }
            
        }while(flag);
     
        
        displayProductList(products);
        String productType;
        do{
            productType = io.readString("Please enter Product Name:");
            if(productType == null || productType.trim().isEmpty()){
                io.print("Product Required");
                flag = true;
            }else if(!productTypes.contains(productType)){
                io.print("Product Required");
                flag = true;
            }else{
                flag=false;
            }
        }while(flag);
                
        
        String area;
        do{
            area = io.readString("Please enter the Area (Minimum is 100 sqft)");
            if(Double.parseDouble(area) < 0 || Double.parseDouble(area) > 100){
                io.print("Area is not Within Range.");
                flag = true;
            }else{
                flag=false;
            }
        }while(flag);
        
        BigDecimal newarea = new BigDecimal(area);
        
        Order unfinishedOrder = new Order(customerName, state, productType, newarea);
        
        answer.put(orderDate, unfinishedOrder);
        return answer;
    }
    
    public void displayCreateOrderBanner() {
        io.print("=== Create Order ===");
    }
    
    public boolean createConfirmation(){
        String confirm = io.readString("Do you want to place the order? (Y/N)");
        return confirm.equals("y".toLowerCase());
    }
    
    public String getDateCreate(){
        LocalDate date = null;
        boolean flag = true;
        
        while(flag){
            String newdate = io.readString("Please enter the date (MM/DD/YYYY)");
            
            date = LocalDate.parse(newdate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            LocalDate currentDate = LocalDate.now();
                if(date.compareTo(currentDate) > 0){
                    flag=false;
                }else{
                    io.print("Date Invalid");
                }
            }
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM/d/uuuu");
        String text = date.format(formatters);
       
        io.print("DATE: "+ text);
        
        return text;
    }
    
   
    public String getDate(){
        String date = io.readString("Please enter the date (MM/DD/YYYY)");
        String newDate = date.replace("/", "");
        String order = "Orders_";
        return order + newDate + ".txt";
        
    }
    
    public void displayCreateSuccessBanner() {
        io.readString("Order created. Hit enter to continue.");
    }
    
    public void displayOrderList(List<Order> orderList) {
        if(orderList.size() == 0){
            displayErrorMessage("Error");
        }
        for (Order order : orderList) {
            String orderInfo = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                order.getOrderNumber(),
                order.getCustomerName(),
                order.getState(),
                order.getTaxRate().toString(),
                order.getProductType(),
                order.getArea().toString(),
                order.getCostPerSquareFoot().toString(),
                order.getLaborCostPerSquareFoot().toString(),
                order.getMaterialCost().toString(),
                order.getLaborCost().toString(),
                order.getTax().toString(),
                order.getTotal().toString());
                  
            io.print(orderInfo);
        }
        io.readString("Please hit enter to continue.");
    }
    
    public void displayTaxList(List<Tax> orderList) {
        for (Tax tax : orderList) {
            String taxInfo = String.format("%s,%s,%s",
                tax.getStateAbbreviation(),
                tax.getStateName(),
                tax.getTaxRate().toString());
            io.print(taxInfo);
        }
        io.readString("Please hit enter to continue.");
    }
    
    
    
    public void displayProductList(List<Product> orderList) {
        for (Product product : orderList) {
            String productInfo = String.format("%s,%s,%s",
                product.getProductType(),
                product.getCostPerSquareFoot().toString(),
                product.getLaborCostPerSquareFoot().toString());
            io.print(productInfo);
        }
        io.readString("Please hit enter to continue.");
    }
    
    public void displayFileList(ArrayList<File> fileList) {
        for (File currentFile : fileList) {
            io.print(currentFile.getName());
        }
        io.readString("Please hit enter to continue.");
    }
    
    public void displayDisplayAllBanner() {
        io.print("=== Display All Orders ===");
    }
    
    public void displayDisplayOrderBanner() {
        io.print("=== Display Order ===");
    }

    public String getOrderIdChoice() {
        return io.readString("Please enter the Order ID:");
    }
    
    public String getOrderDate(){
        return io.readString("Please enter the Order Date:");
    }

    public void displayOrder(Order order) {
        if (order != null) {
            io.print("Order Number: " + order.getOrderNumber());
            io.print("Customer Name: " + order.getCustomerName());
            io.print("State: " + order.getState());
            io.print("Tax Rate: " + order.getTaxRate().toString());
            io.print("Product Type: " + order.getProductType());
            io.print("Area: " + order.getArea().toString());
            io.print("Cost Per Square Foot: " + order.getCostPerSquareFoot().toString());
            io.print("Labor Cost Per Square Foot: " + order.getLaborCostPerSquareFoot().toString());
            io.print("Material Cost: " + order.getMaterialCost().toString());
            io.print("Labor Cost: " + order.getLaborCost().toString());
            io.print("Tax: " + order.getTax().toString());
            io.print("Total: " + order.getTotal().toString());
            io.print("");
        } else {
            io.print("No such order.");
        }
        io.readString("Please hit enter to continue.");
    }
    
    public void displayRemoveOrderBanner() {
        io.print("=== Remove Order ===");
    }

    public void displayRemoveResult(Order order) {
        if(order != null){
          io.print("Order removed.");
        }else{
          io.print("No such order.");
        }
        io.readString("Please hit enter to continue.");
    }
    
    public boolean displayConfirmation(String action) {
        String confirm = io.readString("Are you sure you want to " + action + " the order? (y/n)");
        return confirm.equals("y");
    }
    
    
    public void displayEditDVDBanner(){
        io.print("=== Edit Order ===");
    }
    
    public boolean displayEditResult(Order order) {
        if(order != null){
          io.print("Order #" + order.getOrderNumber() + " Selected");
          return true;
        }
          io.print("No such Order.");
          io.readString("Please hit enter to continue.");
          return false;
    }
    
    
    public ArrayList<String> editMenu(String prevCustomerName, String prevState, String prevProductType, BigDecimal prevArea){
        String newCustomer = io.readString("Enter customer name (" + prevCustomerName + "):");
        newCustomer = newCustomer.equals("") ? prevCustomerName : newCustomer;
        
        String newstate = io.readString("Enter state name (" + prevState + "):");
        newstate = newstate.equals("") ? prevState : newstate;

        String newProductType = io.readString("Enter product type (" + prevProductType + "):");
        newProductType = newProductType.equals("") ? prevProductType : newProductType;

        
        String newArea = io.readString("Enter area (" + prevArea + "):");
        newArea = newArea.equals("") ? prevArea.toString() : newArea;

        ArrayList<String> newValues = new ArrayList<>();
        newValues.add(newCustomer);
        newValues.add(newstate);
        newValues.add(newProductType);
        newValues.add(newArea);
        
        return newValues;
    }
    
    public boolean isEmptyString(String string) {
        return string == null || string.isEmpty();
    }

    public void displaySaveWorkBanner() {
        io.print("=== SAVE WORK === \n");
    }

    public void saveWork() {
        io.print("Work saved. \n");
    }

    public void displayAllOrdersExportedBanner() {
        io.print("Orders Exported");
    }
    public void displaySuccessEdit(){
        io.print("Edit Completed.");
    }
    public void displayExitBanner() {
        io.print("Good Bye.");
    }

    public void displayUnknownCommandBanner() {
        io.print("Unknown Command.");
    }

    public void displayErrorMessage(String message) {
        io.print("=== ERROR === \n");
        io.print(message + "\n");
    }

}
