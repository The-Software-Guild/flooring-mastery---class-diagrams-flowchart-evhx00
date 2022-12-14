
package com.flooring_mastery.service;

import com.flooring_mastery.dao.FlooringMasteryAuditDao;
import com.flooring_mastery.dao.FlooringMasteryDAO;
import com.flooring_mastery.dao.FlooringMasteryPersistenceException;
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
@SuppressWarnings("ALL")
public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer{

    private final FlooringMasteryDAO dao;
    private final FlooringMasteryAuditDao auditDao;
   
    public FlooringMasteryServiceLayerImpl(FlooringMasteryDAO dao, FlooringMasteryAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;
    }

    @Override
    public void createOrder(Order order, String fileName) throws FlooringMasteryPersistenceException {
        dao.addOrder(order.getOrderNumber(), order, fileName);
        auditDao.writeAuditEntry("Student " + order.getOrderNumber() + " CREATED.");
    }

    @Override
    public List<Order> getAllOrders(String fileName) throws FlooringMasteryPersistenceException {
        return dao.getAllOrders(fileName);
    }

    @Override
    public Order getOrder(String orderNumber, String fileName) throws FlooringMasteryPersistenceException {
        return dao.getOrder(orderNumber, fileName);
    }
    
    public  Tax getTax(String stateAB) throws FlooringMasteryPersistenceException{
        return dao.getTax(stateAB);
    }
    
    public  Product getProduct(String productType) throws FlooringMasteryPersistenceException{
        return dao.getProduct(productType);
    }

    @Override
    public Order removeOrder(String orderNumber, String fileName) throws FlooringMasteryPersistenceException {
        Order order = dao.removeOrder(orderNumber, fileName);
        auditDao.writeAuditEntry("Student " + orderNumber + " REMOVED.");
        return order;
    }
    
    public void editOrder(Order editOrder, ArrayList<String> newValues, String fileName) throws FlooringMasteryPersistenceException{
        dao.editOrder(editOrder, newValues, fileName);
    }
    
    @Override
    public ArrayList<File> getAllFiles() {
        return dao.getAllFiles();
    }
    
    @Override
    public List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException{
        return dao.getAllTaxes();
    }
    
    public List<String> getAllStates() throws FlooringMasteryPersistenceException{
        return dao.getAllStates();
    }
    
    public List<String> getAllProductTypes() throws FlooringMasteryPersistenceException{
        return dao.getAllProductTypes();
    }
    
    @Override
    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException{
        return dao.getAllProducts();
    }

    
    @Override
    public Order calculateOrderInfo(Order prevOrder, Tax specificTax, Product specificProduct) {
        return dao.calculateOrderInfo(prevOrder, specificTax, specificProduct);
    }
    
    private void validateCalculateData(Order prevOrder,Tax specificTax,Product specificProduct) throws FlooringMasteryPersistenceException {
        List<Product> products = getAllProducts();
        List<Tax> taxInfo = getAllTaxes();
        String message = "";
    }
    
    @Override
    public String generateNextOrderNumber(String fileName) throws FlooringMasteryPersistenceException{
        return dao.generateNextOrderNumber(fileName);
    }
    
}
