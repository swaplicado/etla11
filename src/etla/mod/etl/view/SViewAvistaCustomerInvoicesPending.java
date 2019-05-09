/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.view;

import etla.mod.SModConsts;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import etla.mod.etl.db.SEtlConsts;
import etla.mod.etl.db.SEtlUtils;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alfredo Pérez, Sergio Flores
 */
public class SViewAvistaCustomerInvoicesPending extends SGridPaneView {

    public SViewAvistaCustomerInvoicesPending(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.AX_CUST_INV_PEND, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        try {
            setRowButtonsEnabled(false, false, false, false, false);
            SEtlUtils.extractCustomerInvoices(miClient.getSession());
        }
        catch (Exception ex) {
            Logger.getLogger(SViewAvistaCustomerInvoicesPending.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void prepareSqlQuery() {
        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);

        msSql = "SELECT "
                + "ci.CustomerInvoiceKey AS " + SDbConsts.FIELD_ID + "1, "
                + "ci.InvoiceNumber AS "+ SDbConsts.FIELD_CODE + ", "
                + "ci.InvoiceNumber AS "+ SDbConsts.FIELD_NAME + ", "
                + "ci.BatchNumber, "
                + "ci.Description, " 
                + "ci.Created, "
                + "cus.name, "
                + "cus.code, "
                + "cus.tax_id, "
                + "cu.id_usr AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "cu.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "ci.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + " "
                + "FROM "
                + SModConsts.TablesMap.get(SModConsts.A_CUSTOMERINVOICES) + " AS ci "
                + "INNER JOIN "
                + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS cu ON ci.fk_usr_ins = cu.id_usr "
                + "LEFT JOIN "
                + SModConsts.TablesMap.get(SModConsts.AU_CUS) + " AS cus ON ci.CustomerId = cus.src_cus_id "
                + "WHERE CustomerInvoiceKey NOT IN (SELECT src_inv_id FROM " + SModConsts.TablesMap.get(SModConsts.A_INV) + ") AND NOT cus.b_etl_ign ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_CODE, "Folio " + SEtlConsts.TXT_SYS_AVISTA));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "ci.BatchNumber", "Batch"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "ci.Created", "Fecha original"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_ID + "1", "Id " + SEtlConsts.TXT_SYS_AVISTA));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "ci.Description", "Descripción"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "cus.name", SGridConsts.COL_TITLE_NAME + " " + SEtlConsts.TXT_CUS.toLowerCase()));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "cus.code", SGridConsts.COL_TITLE_CODE + " " + SEtlConsts.TXT_CUS.toLowerCase()));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "cus.tax_id", "RFC"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));

        return columns;
    }

    @Override
    public void defineSuscriptions() {
    }
}
