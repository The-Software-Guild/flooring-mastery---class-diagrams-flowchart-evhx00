
package com.flooring_mastery.controller;

import com.flooring_mastery.dao.FlooringMasteryPersistenceException;
import com.flooring_mastery.model.Order;
import com.flooring_mastery.model.Product;
import com.flooring_mastery.model.Tax;
import com.flooring_mastery.service.FlooringMasteryServiceLayer;
import com.flooring_mastery.view.FlooringMasteryView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author everlynleon
 */
//@Component
@SuppressWarnings("ALL")
public class FlooringMasteryController {
    private final FlooringMasteryView view;
    private final FlooringMasteryServiceLayer service;

    public FlooringMasteryController(FlooringMasteryServiceLayer service, FlooringMasteryView view) {
        this.service = service;
        this.view = view;
    }
    
    public void run() {
        boolean choice = true;
        int menuSelection;
        try{
            while (choice) {
                menuSelection = getMenuSelection();
                switch (menuSelection) {
                    case 1:
                        listOrders();
                        break;
                    case 2:
                        createOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportAllOrders();
                        break;
                    case 6:
                        choice = false;
                        break;
                    default:
                        unknownCommand();
                }
            }
            exitMessage();
        } catch (FlooringMasteryPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void exportAllOrders() {
        System.out.println("Getting errors on this(finish later)");
    }

    private void listOrders() throws FlooringMasteryPersistenceException{
        view.displayDisplayAllBanner();
        String date = view.getDate();
        ArrayList<File> list = service.getAllFiles();
        boolean flag = true;
        do{
            if(!list.contains(date)){
                view.displayErrorMessage("Wrong date!");
                flag = false;
            }
        }while(flag);
        List<Order> orderList = service.getAllOrders(date);
        view.displayOrderList(orderList);
    }

    private void createOrder() throws FlooringMasteryPersistenceException{
        view.displayCreateOrderBanner();
        HashMap<String,Order> dateAndOrder;
        dateAndOrder = view.getNewOrderInfo(service.getAllStates(),service.getAllStateAB(),service.getAllProducts(), service.getAllProductTypes());
        Map.Entry<String,Order> entry = dateAndOrder.entrySet().iterator().next();
        String date = entry.getKey();
        Order newOrder = entry.getValue();
        String fileName = date.replace("/", "");
        fileName = "Orders_" + fileName + ".txt";
        String orderNumber = service.generateNextOrderNumber(fileName);
        newOrder.setOrderNumber(orderNumber);
        Tax specificTax = service.getTax(newOrder.getState());
        Product specificProduct = service.getProduct(newOrder.getProductType());
        Order finishedOrder = service.calculateOrderInfo(newOrder,specificTax,specificProduct);
        view.displayOrder(finishedOrder);
        if(view.displayConfirmation("create")){
            service.createOrder(finishedOrder, fileName);
            view.displayCreateSuccessBanner();
        }

    }
    private void editOrder() throws FlooringMasteryPersistenceException{
        view.displayEditDVDBanner();
        String date = view.getDate();
        String orderID = view.getOrderIdChoice();
        Order order = service.getOrder(orderID,date);

        boolean bool = view.displayEditResult(order);
        if(bool){
            ArrayList<String> newValues = view.editMenu(order.getCustomerName(), order.getState(),order.getProductType(),order.getArea());
            service.editOrder(order, newValues, date);
            view.displayOrder(order);
            if(view.displayConfirmation("edit")){
                view.displaySuccessEdit();
            }
        }
    }

    private void removeOrder() throws FlooringMasteryPersistenceException{
        view.displayRemoveOrderBanner();
        String date = view.getDate();
        String orderID = view.getOrderIdChoice();
        Order order = service.getOrder(orderID, date);
        view.displayOrder(order);
        if(view.displayConfirmation("remove")){
            Order removedOrder = service.removeOrder(orderID, date);
            view.displayRemoveResult(removedOrder);
        }
    }

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }

}
