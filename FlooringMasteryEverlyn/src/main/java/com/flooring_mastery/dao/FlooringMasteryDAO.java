
package com.flooring_mastery.dao;

import com.flooring_mastery.model.Order;
import com.flooring_mastery.model.Product;
import com.flooring_mastery.model.Tax;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author everlynleon
 */
public interface FlooringMasteryDAO {

    ArrayList<File> getAllFiles();
    
    void addOrder(String orderNumber, Order order, String fileName) throws FlooringMasteryPersistenceException;

    List<Order> getAllOrders(String fileName) throws FlooringMasteryPersistenceException;

    List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException;
    
    List<Product> getAllProducts() throws FlooringMasteryPersistenceException;

    Order getOrder(String orderNumber, String fileName) throws FlooringMasteryPersistenceException;
    
    Tax getTax(String stateAB) throws FlooringMasteryPersistenceException;
    
    List<String> getAllStateAB() throws FlooringMasteryPersistenceException;
    
    List<String> getAllStates() throws FlooringMasteryPersistenceException;
    
    Product getProduct(String productType) throws FlooringMasteryPersistenceException;
    
    List<String> getAllProductTypes() throws FlooringMasteryPersistenceException;
    
    Order removeOrder(String orderNumber, String fileName) throws FlooringMasteryPersistenceException;
    
    Order calculateOrderInfo(Order prevOrder, Tax specificTax, Product specificProduct);

    void editOrder(Order editOrder, ArrayList<String> newValues, String fileName) throws FlooringMasteryPersistenceException;
    
    String generateNextOrderNumber(String fileName) throws FlooringMasteryPersistenceException;
}
