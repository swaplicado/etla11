/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.db;

import etla.mod.SModConsts;
import etla.mod.etl.db.SDbCustomer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Daniel López, Sergio Flores, Alfredo Pérez, Isabel Servín
 */
public class SDbShipmentRow extends SDbRegistryUser {

    protected int mnPkShipmentId;
    protected int mnPkRowId;
    protected int mnShipmentOrderFolio;
    protected String msDeliveryId;
    protected int mnDeliveryNumber;
    protected Date mtDeliveryDate;
    protected int mnBolId;
    protected int mnInvoiceIdYear;
    protected int mnInvoiceIdDoc;
    protected String msInvoiceSeries;
    protected String msInvoiceNumber;
    protected int mnOrders;
    protected int mnBales;
    protected double mdMeters2;
    protected double mdKilograms;
    protected int mnFkCustomerId;
    protected int mnFkDestinationId;
    
    protected String msDbmsCustomer;
    protected String msDbmsCustomerTaxId;
    protected String msDbmsCustomerZip;
    
    protected SDbDestination moDbmsDestination;
    
    protected int mnAuxSiteLocationId;
    protected boolean mbAuxDestinationCreated;
    protected boolean mbAuxIsRowSelected;
    protected int mnAuxRow;

    public SDbShipmentRow () {
        super(SModConsts.S_SHIPT_ROW);
    }
    
    /*
     * Public methods
     */

    public void setPkShipmentId(int n) { mnPkShipmentId = n; }
    public void setPkRowId(int n) { mnPkRowId = n; }
    public void setShipmentOrderFolio(int n) { mnShipmentOrderFolio = n; }
    public void setDeliveryId(String s) { msDeliveryId = s; }
    public void setDeliveryNumber(int n) { mnDeliveryNumber = n; }
    public void setDeliveryDate(Date t) { mtDeliveryDate = t; }
    public void setBolId(int n) { mnBolId = n; }
    public void setInvoiceIdYear(int n) { mnInvoiceIdYear = n; }
    public void setInvoiceIdDoc(int n) { mnInvoiceIdDoc = n; }
    public void setInvoiceSeries(String s) { msInvoiceSeries = s; }
    public void setInvoiceNumber(String s) { msInvoiceNumber = s; }
    public void setOrders(int n) { mnOrders = n; }
    public void setBales(int n) { mnBales = n; }
    public void setMeters2(double d) { mdMeters2 = d; }
    public void setKilograms(double d) { mdKilograms = d; }
    public void setFkCustomerId(int n) { mnFkCustomerId = n; }
    public void setFkDestinationId(int n) { mnFkDestinationId = n; }

    public int getPkShipmentId() { return mnPkShipmentId; }
    public int getPkRowId() { return mnPkRowId; }
    public int getShipmentOrderFolio() { return mnShipmentOrderFolio; }
    public String getDeliveryId() { return msDeliveryId; }
    public int getDeliveryNumber() { return mnDeliveryNumber; }
    public Date getDeliveryDate() { return mtDeliveryDate; }
    public int getBolId() { return mnBolId; }
    public int getInvoiceIdYear() { return mnInvoiceIdYear; }
    public int getInvoiceIdDoc() { return mnInvoiceIdDoc; }
    public String getInvoiceSeries() { return msInvoiceSeries; }
    public String getInvoiceNumber() { return msInvoiceNumber; }
    public int getOrders() { return mnOrders; }
    public int getBales() { return mnBales; }
    public double getMeters2() { return mdMeters2; }
    public double getKilograms() { return mdKilograms; }
    public int getFkCustomerId() { return mnFkCustomerId; }
    public int getFkDestinationId() { return mnFkDestinationId; }
    
    public void setDbmsCustomer(String s) { msDbmsCustomer = s; }
    public void setDbmsCustomerTaxId(String s) { msDbmsCustomerTaxId = s; }
    public void setDbmsCustomerZip(String s) { msDbmsCustomerZip = s; }
    
    public String getDbmsCustomer() { return msDbmsCustomer; }
    public String getDbmsCustomerTaxId() { return msDbmsCustomerTaxId; }
    public String getDbmsCustomerZip() { return msDbmsCustomerZip; }
    
    public void setDbmsDestination(SDbDestination o) { moDbmsDestination = o; }
    
    public SDbDestination getDbmsDestination() { return moDbmsDestination; }
    
    public void setAuxSiteLocationId(int n) { mnAuxSiteLocationId = n; }
    public void setAuxDestinationCreated(boolean b) { mbAuxDestinationCreated = b; }
    public void setAuxIsRowSelected(boolean b) { mbAuxIsRowSelected = b; }
    public void setAuxRow(int n) { mnAuxRow = n; }

    public int getAuxSiteLocationId() { return mnAuxSiteLocationId; }
    public boolean isAuxDestinationCreated() { return mbAuxDestinationCreated; }
    public boolean isAuxIsRowSelected() { return mbAuxIsRowSelected; }
    public int getAuxRow() { return mnAuxRow; }
    
    public void readDbmsCustomer (SGuiSession session) {
        if (mnFkCustomerId != 0) {
            msDbmsCustomer = (String) session.readField(SModConsts.AU_CUS, new int[] { mnFkCustomerId }, SDbRegistry.FIELD_NAME);
            SDbCustomer cus = (SDbCustomer) session.readRegistry(SModConsts.AU_CUS, new int[] { mnFkCustomerId });
            msDbmsCustomerTaxId = cus.getTaxId();
            msDbmsCustomerZip = cus.getZip();
        }
    }

    
    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkShipmentId = pk[0];
        mnPkRowId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkShipmentId, mnPkRowId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkShipmentId = 0;
        mnPkRowId = 0;
        mnShipmentOrderFolio = 0;
        msDeliveryId = "";
        mnDeliveryNumber = 0;
        mtDeliveryDate = null;
        mnBolId = 0;
        mnInvoiceIdYear = 0;
        mnInvoiceIdDoc = 0;
        msInvoiceSeries = "";
        msInvoiceNumber = "";
        mnOrders = 0;
        mnBales = 0;
        mdMeters2 = 0;
        mdKilograms = 0;
        mnFkCustomerId = 0;
        mnFkDestinationId = 0;

        msDbmsCustomer = "";
        msDbmsCustomerTaxId = "";
        msDbmsCustomerZip = "";
        
        moDbmsDestination = null;

        mnAuxSiteLocationId = 0;
        mbAuxDestinationCreated = false;
        mbAuxIsRowSelected = false;
        mnAuxRow = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_shipt = " + mnPkShipmentId + " AND "
                + "id_row = " + mnPkRowId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_shipt = " + pk[0] + " AND "
                + "id_row = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;

        mnPkRowId = 0;

        msSql = "SELECT COALESCE(MAX(id_row), 0) + 1 FROM " + getSqlTable() + " WHERE id_shipt = " + mnPkShipmentId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkRowId = resultSet.getInt(1);
        }
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
            mnPkShipmentId = resultSet.getInt("id_shipt");
            mnPkRowId = resultSet.getInt("id_row");
            mnShipmentOrderFolio = resultSet.getInt("shipt_folio");
            msDeliveryId = resultSet.getString("delivery_id");
            mnDeliveryNumber = resultSet.getInt("delivery_number");
            mtDeliveryDate = resultSet.getDate("delivery_date");
            mnBolId = resultSet.getInt("bol_id");
            mnInvoiceIdYear = resultSet.getInt("invoice_id_year");
            mnInvoiceIdDoc = resultSet.getInt("invoice_id_doc");
            msInvoiceSeries = resultSet.getString("invoice_series");
            msInvoiceNumber = resultSet.getString("invoice_number");
            mnOrders = resultSet.getInt("orders");
            mnBales = resultSet.getInt("bales");
            mdMeters2 = resultSet.getDouble("m2");
            mdKilograms = resultSet.getDouble("kg");
            mnFkCustomerId = resultSet.getInt("fk_customer");
            mnFkDestinationId = resultSet.getInt("fk_destin");

            readDbmsCustomer(session);
            
            moDbmsDestination = (SDbDestination) session.readRegistry(SModConsts.SU_DESTIN, new int[] { mnFkDestinationId });
            
            mbAuxIsRowSelected = true;
            mnAuxRow = mnPkRowId;

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {       
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        moDbmsDestination.save(session);
        mnFkDestinationId = moDbmsDestination.getPkDestinationId();
        
        // save shipment row registry:

        if (mbRegistryNew) {
            computePrimaryKey(session);

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                mnPkShipmentId + ", " + 
                mnPkRowId + ", " + 
                mnShipmentOrderFolio + ", " + 
                "'" + msDeliveryId + "', " + 
                mnDeliveryNumber + ", " +
                "'" + SLibUtils.DbmsDateFormatDate.format(mtDeliveryDate) + "', " + 
                mnBolId + ", " + 
                mnInvoiceIdYear + ", " + 
                mnInvoiceIdDoc + ", " + 
                "'" + msInvoiceSeries + "', " + 
                "'" + msInvoiceNumber + "', " + 
                mnOrders + ", " + 
                mnBales + ", " + 
                SLibUtils.round(mdMeters2, SLibUtils.getDecimalFormatQuantity().getMaximumFractionDigits()) + ", " + 
                SLibUtils.round(mdKilograms, SLibUtils.getDecimalFormatQuantity().getMaximumFractionDigits()) + ", " + 
                mnFkCustomerId + ", " + 
                mnFkDestinationId + " " +
                ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                //"id_shipt = " + mnPkShipmentId + ", " +
                //"id_row = " + mnPkRowId + ", " +
                "shipt_folio = " + mnShipmentOrderFolio + ", " +
                "delivery_id = '" + msDeliveryId + "', " +
                "delivery_number = " + mnDeliveryNumber + ", " +
                "delivery_date = '" + SLibUtils.DbmsDateFormatDate.format(mtDeliveryDate) + "', " +
                "bol_id = " + mnBolId + ", " +
                "invoice_id_year = " + mnInvoiceIdYear + ", " +
                "invoice_id_doc = " + mnInvoiceIdDoc + ", " +
                "invoice_series = '" + msInvoiceSeries + "', " +
                "invoice_number = '" + msInvoiceNumber + "', " +
                "orders = " + mnOrders + ", " +
                "bales = " + mnBales + ", " +
                "m2 = " + SLibUtils.round(mdMeters2, SLibUtils.getDecimalFormatQuantity().getMaximumFractionDigits()) + ", " +
                "kg = " + SLibUtils.round(mdKilograms, SLibUtils.getDecimalFormatQuantity().getMaximumFractionDigits()) + ", " +
                "fk_customer = " + mnFkCustomerId + ", " +
                "fk_destin = " + mnFkDestinationId + " " +
                getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbShipmentRow clone() throws CloneNotSupportedException {
        SDbShipmentRow registry = new SDbShipmentRow();

        registry.setPkShipmentId(this.getPkShipmentId());
        registry.setPkRowId(this.getPkRowId());
        registry.setShipmentOrderFolio(this.getShipmentOrderFolio());
        registry.setDeliveryId(this.getDeliveryId());
        registry.setDeliveryNumber(this.getDeliveryNumber());
        registry.setDeliveryDate(this.getDeliveryDate());
        registry.setBolId(this.getBolId());
        registry.setInvoiceIdYear(this.getInvoiceIdYear());
        registry.setInvoiceIdDoc(this.getInvoiceIdDoc());
        registry.setInvoiceSeries(this.getInvoiceSeries());
        registry.setInvoiceNumber(this.getInvoiceNumber());
        registry.setOrders(this.getOrders());
        registry.setBales(this.getBales());
        registry.setMeters2(this.getMeters2());
        registry.setKilograms(this.getKilograms());
        registry.setFkCustomerId(this.getFkCustomerId());
        registry.setFkDestinationId(this.getFkDestinationId());

        registry.setDbmsCustomer(this.getDbmsCustomer());
        registry.setDbmsCustomerTaxId(this.getDbmsCustomerTaxId());
        registry.setDbmsCustomerZip(this.getDbmsCustomerZip());
        
        registry.setDbmsDestination(this.getDbmsDestination());
        
        registry.setAuxSiteLocationId(this.getAuxSiteLocationId());
        registry.setAuxDestinationCreated(this.isAuxDestinationCreated());
        registry.setAuxIsRowSelected(this.isAuxIsRowSelected());
        registry.setAuxRow(this.getAuxRow());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
