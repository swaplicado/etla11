/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.db;

import sa.lib.grid.SGridRow;

/**
 *
 * @author Daniel López, Sergio Flores, Isabel Servín
 */
public class SRowShipmentRow implements SGridRow {
    
    protected SDbShipmentRow moShipmentRow;
    
    public SRowShipmentRow(SDbShipmentRow shipmentRow) {
        moShipmentRow = shipmentRow;
    }

    public SDbShipmentRow getShipmentRow() { return moShipmentRow; }
    
    @Override
    public int[] getRowPrimaryKey() {
        return moShipmentRow.getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return "" + moShipmentRow.getDeliveryNumber();
    }

    @Override
    public String getRowName() {
        return getRowCode();
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return false;
    }

    @Override
    public void setRowEdited(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;
        
        switch(row){
            case 0: 
                value = moShipmentRow.getShipmentOrderFolio();
                break;
            case 1:
                value = moShipmentRow.getBolId();
                break;
            case 2:
                value = moShipmentRow.getDeliveryNumber();
                break;
            case 3:
                value = moShipmentRow.getDeliveryDate();
                break;
            case 4:
                value = moShipmentRow.getDbmsCustomer();
                break;
            case 5:
                value = moShipmentRow.getDbmsDestination();
                break;
            case 6:
                value = moShipmentRow.getMeters2();
                break;
            case 7:
                value = moShipmentRow.getKilograms();
                break;
            default:
        }      

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch(row) {
            case 0:
                moShipmentRow.setShipmentOrderFolio((int) value);
                break;
        }
    }
}
