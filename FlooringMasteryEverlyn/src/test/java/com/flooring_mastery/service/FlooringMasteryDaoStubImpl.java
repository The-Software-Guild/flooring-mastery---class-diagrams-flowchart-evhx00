
package com.flooring_mastery.service;

import com.flooring_mastery.dao.FlooringMasteryDAO;
import com.flooring_mastery.model.Order;
import com.flooring_mastery.model.Product;
import com.flooring_mastery.model.Tax;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author everlynleon
 */

public class FlooringMasteryDaoStubImpl implements FlooringMasteryDAO{

    public final Order onlyOrder;

    public FlooringMasteryDaoStubImpl() {
        onlyOrder = new Order("3", "John", "CA", new BigDecimal("3.50"),
            "Wood", new BigDecimal("12"), new BigDecimal("5.00"), 
            new BigDecimal("4.99"), new BigDecimal("12.50"), 
            new BigDecimal("13.90"), new BigDecimal("8.50"),
            new BigDecimal("13.00")); 
        
    }

    public FlooringMasteryDaoStubImpl(Order onlyOrder) {
        this.onlyOrder = onlyOrder;
    }
    
    @Override
    public void addOrder(String orderNumber, Order order, String fileName) {
    }
    
    @Override
    public List<Order> getAllOrders(String fileName) {
        List<Order> orderList = new ArrayList<>();
        orderList.add(onlyOrder);
        return orderList;
    }
    
    @Override
    public Order getOrder(String orderNumber, String fileName) {
        if (orderNumber.equals(onlyOrder.getOrderNumber())) {
            return onlyOrder;
        } else {
            return null;
        }       
    }
    
    @Override
    public Order removeOrder(String orderNumber, String fileName) {
        if (orderNumber.equals(onlyOrder.getOrderNumber())) {
            return onlyOrder;
        } else {
            return null;
        }
    }   
    
    
    @Override
    public ArrayList<File> getAllFiles() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Tax> getAllTaxes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Product> getAllProducts() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Tax getTax(String stateAB) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getAllStateAB() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
     @Override
    public List<String> getAllStates() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List<String> getAllProductTypes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Product getProduct(String productType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Order calculateOrderInfo(Order prevOrder, Tax specificTax, Product specificProduct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void editOrder(Order editOrder, ArrayList<String> newValues, String fileName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String generateNextOrderNumber(String fileName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
