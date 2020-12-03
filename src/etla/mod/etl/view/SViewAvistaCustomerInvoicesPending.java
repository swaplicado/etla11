/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.view;

import etla.mod.SModConsts;
import etla.mod.etl.db.SDbCustomerInvoice;
import etla.mod.etl.db.SDbCustomerInvoiceExcuse;
import etla.mod.etl.db.SEtlConsts;
import etla.mod.etl.db.SEtlUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Alfredo Pérez, Sergio Flores, Isabel Servín
 */
public class SViewAvistaCustomerInvoicesPending extends SGridPaneView implements ActionListener {
    
    private JButton mjEditExcuse;

    public SViewAvistaCustomerInvoicesPending(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.AX_CUST_INV_PEND, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false, false, false, false, false);

        mjEditExcuse = SGridUtils.createButton(new ImageIcon(getClass().getResource("/sa/lib/img/cmd_std_note.gif")), "Agregar excusa", this);
        mjEditExcuse.setEnabled(true);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjEditExcuse);
        
        try { 
            SEtlUtils.extractCustomerInvoices(miClient.getSession());
        } 
        catch (Exception e){
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedEditExcuse() {
        if (mjEditExcuse.isEnabled()) {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else if (gridRow.isRowSystem()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
            }
            else if (!gridRow.isUpdatable()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
            }
            else {
                SGuiParams params;
                SDbCustomerInvoice customerInvoice = (SDbCustomerInvoice) miClient.getSession().readRegistry(SModConsts.A_CUSTOMERINVOICES, gridRow.getRowPrimaryKey(), SDbConsts.MODE_STEALTH);
                SDbCustomerInvoiceExcuse excuse = (SDbCustomerInvoiceExcuse) miClient.getSession().readRegistry(SModConsts.A_CUSTOMERINVOICES_EXCUSES, gridRow.getRowPrimaryKey(), SDbConsts.MODE_STEALTH);

                params = new SGuiParams();
                params.getParamsMap().put(SModConsts.A_CUSTOMERINVOICES, customerInvoice);

                if (!excuse.isRegistryNew()) {
                    params.setType(SModConsts.A_CUSTOMERINVOICES_EXCUSES);
                    params.setKey(gridRow.getRowPrimaryKey());
                }

                miClient.getSession().showForm(SModConsts.A_CUSTOMERINVOICES_EXCUSES, 0, params);
            }
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
                + "ci.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "cie.excuse, "
                + "cu_exc.name AS " + SDbConsts.FIELD_USER_INS_NAME + "_excuse, "
                + "cie.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + "_excuse, "
                + "cu_exc2.name AS " + SDbConsts.FIELD_USER_UPD_NAME + "_excuse, "
                + "cie.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + "_excuse "
                + "FROM "
                + SModConsts.TablesMap.get(SModConsts.A_CUSTOMERINVOICES) + " AS ci "
                + "INNER JOIN "
                + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS cu ON ci.fk_usr_ins = cu.id_usr "
                + "LEFT OUTER JOIN "
                + SModConsts.TablesMap.get(SModConsts.AU_CUS) + " AS cus ON ci.CustomerId = cus.src_cus_id "
                + "LEFT OUTER JOIN "
                + SModConsts.TablesMap.get(SModConsts.A_CUSTOMERINVOICES_EXCUSES) + " AS cie ON ci.CustomerInvoiceKey = cie.CustomerInvoiceKey "
                + "LEFT OUTER JOIN "
                + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS cu_exc ON cie.fk_usr_ins = cu_exc.id_usr " 
                + "LEFT OUTER JOIN "
                + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS cu_exc2 ON cie.fk_usr_upd = cu_exc2.id_usr "
                + "WHERE ci.CustomerInvoiceKey NOT IN (SELECT src_inv_id FROM " + SModConsts.TablesMap.get(SModConsts.A_INV) + ") AND NOT cus.b_etl_ign "
                + "ORDER BY ci.InvoiceNumber, ci.CustomerInvoiceKey ";
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
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "cie.excuse", "Excusa", 300));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_USER_INS_NAME + "_excuse", SGridConsts.COL_TITLE_USER_INS_NAME + " excusa"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_USER_INS_TS + "_excuse", SGridConsts.COL_TITLE_USER_INS_TS + " excusa"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_USER_UPD_NAME + "_excuse", SGridConsts.COL_TITLE_USER_UPD_NAME + " excusa"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_USER_UPD_TS + "_excuse", SGridConsts.COL_TITLE_USER_UPD_TS + " excusa"));

        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.A_CUSTOMERINVOICES);
        moSuscriptionsSet.add(SModConsts.A_CUSTOMERINVOICES_EXCUSES);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() instanceof JButton) {
            JButton button = (JButton) evt.getSource();
            
            if (button == mjEditExcuse) {
                actionPerformedEditExcuse();
            }
        }
    }
}
