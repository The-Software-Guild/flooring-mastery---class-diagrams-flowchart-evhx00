
package com.flooring_mastery.dao;

/**
 *
 * @author everlynleon
 */
public interface FlooringMasteryAuditDao {
    void writeAuditEntry(String entry) throws FlooringMasteryPersistenceException;
    
}
