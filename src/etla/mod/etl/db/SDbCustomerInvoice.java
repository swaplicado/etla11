/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

import etla.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbCustomerInvoice extends SDbRegistryUser {

    protected int mnCustomerInvoiceKey;
    protected String msCustomerId;
    protected String msInvoiceNumber;
    protected int mnBatchNumber;
    protected Date mtCreated;
    protected String msDescription;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    
    public SDbCustomerInvoice() {
        super(SModConsts.A_CUSTOMERINVOICES);
    }
    
    public void setCustomerInvoiceKey(int n) { mnCustomerInvoiceKey = n; }
    public void setCustomerId(String s) { msCustomerId = s; }
    public void setInvoiceNumber(String s) { msInvoiceNumber = s; }
    public void setBatchNumber(int n) { mnBatchNumber = n; }
    public void setCreated(Date t) { mtCreated = t; }
    public void setDescription(String s) { msDescription = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getCustomerInvoiceKey() { return mnCustomerInvoiceKey; }
    public String getCustomerId() { return msCustomerId; }
    public String getInvoiceNumber() { return msInvoiceNumber; }
    public int getBatchNumber() { return mnBatchNumber; }
    public Date getCreated() { return mtCreated; }
    public String getDescription() { return msDescription; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnCustomerInvoiceKey = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnCustomerInvoiceKey };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnCustomerInvoiceKey = 0;
        msCustomerId = "";
        msInvoiceNumber = "";
        mnBatchNumber = 0;
        mtCreated = null;
        msDescription = "";
        mbDeleted = false;
        mbSystem = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE CustomerInvoiceKey = " + mnCustomerInvoiceKey + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE CustomerInvoiceKey = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession sgs) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnCustomerInvoiceKey = resultSet.getInt("CustomerInvoiceKey");
            msCustomerId = resultSet.getString("CustomerId");
            msInvoiceNumber = resultSet.getString("InvoiceNumber");
            mnBatchNumber = resultSet.getInt("BatchNumber");
            mtCreated = resultSet.getTimestamp("Created");
            msDescription = resultSet.getString("Description");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if(mbRegistryNew) {
            verifyRegistryNew(session);
        }
        
        if (mbRegistryNew) {
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnCustomerInvoiceKey + ", " + 
                    "'" + msCustomerId + "', " + 
                    "'" + msInvoiceNumber + "', " + 
                    mnBatchNumber + ", " + 
                    "NOW()" + ", " + 
                    "'" + msDescription + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"CustomerInvoiceKey = " + mnCustomerInvoiceKey + ", " +
                    "CustomerId = '" + msCustomerId + "', " +
                    "InvoiceNumber = '" + msInvoiceNumber + "', " +
                    "BatchNumber = " + mnBatchNumber + ", " +
                    "Created = " + "NOW()" + ", " +
                    "Description = '" + msDescription + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + ", " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbCustomerInvoice registry = new SDbCustomerInvoice();
        
        registry.setCustomerInvoiceKey(this.getCustomerInvoiceKey());
        registry.setCustomerId(this.getCustomerId());
        registry.setInvoiceNumber(this.getInvoiceNumber());
        registry.setBatchNumber(this.getBatchNumber());
        registry.setCreated(this.getCreated());
        registry.setDescription(this.getDescription());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
