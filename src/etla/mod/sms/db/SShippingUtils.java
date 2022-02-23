/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.db;

import etla.mod.SModConsts;
import etla.mod.SModSysConsts;
import etla.mod.etl.db.SDbInvoice;
import etla.mod.etl.db.SEtlConsts;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Daniel López, Sergio Flores, Alfredo Pérez, Isabel Servín
 */
public abstract class SShippingUtils {
    
    public static final String BOL = "BOL: ";

    /**
     * Obtains available shipment deliveries from external system as shipment rows.
     * @param session Current user session.
     * @param connection Database connection to external system.
     * @param date Date to filter available shipment deliveries.
     * @return Available rows.
     * @throws SQLException
     * @throws Exception 
     */
    public static ArrayList<SRowShipmentRow> obtainAvailableRows(final SGuiSession session, final Connection connection, final Date date) throws SQLException, Exception {
        ArrayList<SRowShipmentRow> availableRows = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = null;

        String sql = "SELECT ci.CustomerInvoiceKey, ci.InvoiceNumber, ci.BatchNumber, ci.Created, ci.Description, RIGHT(ci.Description, LEN(ci.Description) - " + BOL.length() + ") AS _bol, " +
                "c.CustomerId, c.CustomerName, st.SiteLocation, st.Address1, st.Address2, st.City + ', ' + st.State + ', ' + st.ZipCode AS _site_loc, st.ZipCode, st.Country, " + 
                "SUM(cii.Area)/1000000.0 AS _m2, SUM(cii.Weight)/1000000.0 AS _kg, COUNT(*) AS _orders, " +
                "(SELECT SUM(NoLoads) FROM dbo.BOLUnitsView where BOLKey=RIGHT(ci.Description, LEN(ci.Description) - " + BOL.length() + ")) AS _bales " +
                "FROM dbo.CustomerInvoices AS ci " +
                "INNER JOIN dbo.CustomerInvoiceItems AS cii ON cii.CustomerInvoiceKey=ci.CustomerInvoiceKey " +
                "INNER JOIN dbo.Customers AS c ON c.CustomerId=ci.CustomerId " +
                "INNER JOIN dbo.ShipTo AS st ON st.SiteLocation=ci.SiteLocationKey " +
                "WHERE " +
                "CAST(ci.Created AS DATE) = '" + SLibUtils.DbmsDateFormatDate.format(date) + "' AND " +
                "ci.CurrentStatusKey IN (" + SEtlConsts.AVISTA_INV_STA_APP + ", " + SEtlConsts.AVISTA_INV_STA_ARC + ") AND " +
                "ci.CustomerInvoiceTypeKey=" + SEtlConsts.AVISTA_INV_TP_INV + " " +
                "GROUP BY " +
                "ci.CustomerInvoiceKey, ci.InvoiceNumber, ci.BatchNumber, ci.Created, ci.Description, ci.SiteLocationKey, " +
                "c.CustomerId, c.CustomerName, " +
                "st.SiteLocation,st.Address1, st.Address2, st.City, st.State, st.ZipCode " +
                "ORDER BY " +
                "ci.InvoiceNumber, ci.CustomerInvoiceKey ";

        resultSet = statement.executeQuery(sql);
        while(resultSet.next()) {
            SDbShipmentRow row = new SDbShipmentRow();

            //row.setPkShipmentId(...);
            //row.setPkRowId(...);
            row.setDeliveryId(resultSet.getString("CustomerInvoiceKey"));
            row.setDeliveryNumber(SLibUtils.parseInt(resultSet.getString("InvoiceNumber")));
            row.setDeliveryDate(resultSet.getDate("Created"));
            row.setBolId(SLibUtils.parseInt(resultSet.getString("_bol")));

            SDbInvoice invoice = getInvoice(session, resultSet.getInt("CustomerInvoiceKey"));
            if (invoice != null) {
                row.setInvoiceIdYear(invoice.getDesInvoiceYearId());
                row.setInvoiceIdDoc(invoice.getDesInvoiceDocumentId());
                row.setInvoiceSeries(invoice.getFinalSeries());
                row.setInvoiceNumber(invoice.getFinalNumber());
            }

            row.setOrders(resultSet.getInt("_orders"));
            row.setBales(resultSet.getInt("_bales"));
            row.setMeters2(resultSet.getDouble("_m2"));
            row.setKilograms(resultSet.getDouble("_kg"));
            row.setFkCustomerId(getCustomerId(session, resultSet.getString("CustomerId")));
            row.setFkDestinationId(getDestinationId(session, resultSet.getInt("SiteLocation")));

            row.setDbmsCustomer(resultSet.getString("CustomerName"));
            row.setDbmsDestination(resultSet.getString("_site_loc"));
            row.setDbmsAddress1(resultSet.getString("Address1"));
            row.setDbmsAddress2(resultSet.getString("Address2"));
            row.setDbmsDestinationZip(resultSet.getString("ZipCode"));
            row.setDbmsCountry(resultSet.getString("Country") == null || resultSet.getString("Country").toLowerCase().equals("null") ? "" : resultSet.getString("Country"));
            row.setAuxSiteLocationId(resultSet.getInt("SiteLocation"));

            availableRows.add(new SRowShipmentRow(row));
        }

        return availableRows;
    }

    /**
     * Gets invoice from invoice key in external system.
     * @param session Current user session.
     * @param invoiceKey Invoice key in external system
     * @return Customer ID.
     * @throws SQLException
     * @throws Exception 
     */
    public static SDbInvoice getInvoice(final SGuiSession session, final int invoiceKey) throws SQLException, Exception {
        SDbInvoice invoice = null;

        String sql = "SELECT id_inv FROM " + SModConsts.TablesMap.get(SModConsts.A_INV) + " "
                + "WHERE src_inv_id = " + invoiceKey + " AND NOT b_del "
                + "ORDER BY id_inv DESC ";
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            invoice = (SDbInvoice) session.readRegistry(SModConsts.A_INV, new int[] { resultSet.getInt(1) });
        }

        return invoice;
    }

    /**
     * Gets customer ID from custemer key in external system.
     * @param session Current user session.
     * @param customerKey Custemer key in external system
     * @return Customer ID.
     * @throws SQLException
     * @throws Exception 
     */
    public static int getCustomerId(final SGuiSession session, final String customerKey) throws SQLException, Exception {
        int id = SLibConsts.UNDEFINED;

        String sql = "SELECT id_cus "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.AU_CUS) + " "
                + "WHERE src_cus_id = '" + customerKey + "' ";
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }

        return id;
    }

    /**
     * Gets destination ID from site location key in external system.
     * @param session Current user session.
     * @param siteLocationKey Site location key in external system
     * @return Destination ID.
     * @throws SQLException
     * @throws Exception 
     */
    public static int getDestinationId(final SGuiSession session, final int siteLocationKey) throws SQLException, Exception {
        int id = SLibConsts.UNDEFINED;

        String sql = "SELECT id_destin "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_DESTIN) + " "
                + "WHERE site_loc_id = " + siteLocationKey + " ";
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
        
        return id;
    }

    /**
     * Change status of a shipment.
     * @param session Current user session.
     * @param shipmentOrderId ID of the sipment.
     * @param status The new status of the shipment.
     * @throws SQLException
     * @throws Exception 
     */
    public static void changeStatus(final SGuiSession session, final int[] shipmentOrderId, final int status) throws SQLException, Exception {
        SDbShipment shipment = (SDbShipment) session.readRegistry(SModConsts.S_SHIPT, shipmentOrderId);
        
        if (shipment != null) {
            if (!shipment.isDeleted()) {
                if (!shipment.isAnnulled()) {
                    switch (status) {
                        case SModSysConsts.SS_SHIPT_ST_REL:
                            if (shipment.getFkShipmentStatusId() != SModSysConsts.SS_SHIPT_ST_REL_TO) {
                                throw new Exception("El estatus del embarque no es el apropiado.");
                            }
                            break;
                        case SModSysConsts.SS_SHIPT_ST_REL_TO:
                            if (shipment.getFkShipmentStatusId() != SModSysConsts.SS_SHIPT_ST_REL) {
                                throw new Exception("El estatus del embarque no es el apropiado.");
                            }
                            break;
                        default:
                            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }
                    
                    shipment.setFkShipmentStatusId(status);
                    shipment.save(session);
                }
                else {
                    throw new Exception("El embarque está anulado.");
                }
            }
            else {
                throw new Exception("El embarque está eliminado.");
            }
        }
        else {
            throw new Exception("El embarque no fue encontrado.");
        }
    }
}
