/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.form;

import etla.mod.SModConsts;
import etla.mod.cfg.db.SDbConfig;
import etla.mod.etl.db.SDbConfigAvista;
import etla.mod.etl.db.SEtlConsts;
import etla.mod.etl.db.SEtlProcess;
import static etla.mod.etl.db.SEtlProcess.createConnection;
import etla.mod.etl.db.SEtlUtils;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiItem;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFieldBoolean;
import sa.lib.gui.bean.SBeanFieldRadio;
import sa.lib.gui.bean.SBeanFormDialog;

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public class SDialogEtl extends SBeanFormDialog implements ActionListener, ItemListener {
    
    private Connection moConnectionAvista;
    private Statement moStatementAvista;

    /**
     * Creates new form SDialogEtl
     * @param client
     * @param title
     */
    public SDialogEtl(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.AX_ETL, SLibConsts.UNDEFINED, title);
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgExportMode = new javax.swing.ButtonGroup();
        bgUpdateMode = new javax.swing.ButtonGroup();
        bgInvoiceBatch = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        moRadExportModeCat = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel7 = new javax.swing.JPanel();
        moRadExportModeAll = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel3 = new javax.swing.JPanel();
        jlDatePeriodStart = new javax.swing.JLabel();
        moDatePeriodStart = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel4 = new javax.swing.JPanel();
        jlDatePeriodEnd = new javax.swing.JLabel();
        moDatePeriodEnd = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel12 = new javax.swing.JPanel();
        jlDateIssue = new javax.swing.JLabel();
        moDateIssue = new sa.lib.gui.bean.SBeanFieldDate();
        jbDateIssueBackward = new javax.swing.JButton();
        jbDateIssueForward = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jbInvoiceBatchShow = new javax.swing.JButton();
        jbInvoiceBatchClear = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        moRadInvoiceBatchAll = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel17 = new javax.swing.JPanel();
        moRadInvoiceBatchSel = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel18 = new javax.swing.JPanel();
        jlInvoiceBatch = new javax.swing.JLabel();
        moKeyInvoiceBatch = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel19 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        moBoolUpdateData = new sa.lib.gui.bean.SBeanFieldBoolean();
        jPanel10 = new javax.swing.JPanel();
        moRadUpdateModeSel = new sa.lib.gui.bean.SBeanFieldRadio();
        jlUpdateModeSel = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        moRadUpdateModeAll = new sa.lib.gui.bean.SBeanFieldRadio();
        jlUpdateModeAll = new javax.swing.JLabel();

        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel13.setLayout(new java.awt.GridLayout(1, 2, 5, 0));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros de exportación a SIIE:"));
        jPanel2.setLayout(new java.awt.GridLayout(5, 0, 0, 5));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgExportMode.add(moRadExportModeCat);
        moRadExportModeCat.setText("Exportar catálogos");
        moRadExportModeCat.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel6.add(moRadExportModeCat);

        jPanel2.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgExportMode.add(moRadExportModeAll);
        moRadExportModeAll.setText("Exportar catálogos y facturas");
        moRadExportModeAll.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel7.add(moRadExportModeAll);

        jPanel2.add(jPanel7);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDatePeriodStart.setText("Fecha inicial:*");
        jlDatePeriodStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlDatePeriodStart);
        jPanel3.add(moDatePeriodStart);

        jPanel2.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDatePeriodEnd.setText("Fecha final:*");
        jlDatePeriodEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlDatePeriodEnd);
        jPanel4.add(moDatePeriodEnd);

        jPanel2.add(jPanel4);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateIssue.setText("Fecha emisión:*");
        jlDateIssue.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlDateIssue);
        jPanel12.add(moDateIssue);

        jbDateIssueBackward.setText("-");
        jbDateIssueBackward.setToolTipText("Disminuir día");
        jbDateIssueBackward.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbDateIssueBackward.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel12.add(jbDateIssueBackward);

        jbDateIssueForward.setText("+");
        jbDateIssueForward.setToolTipText("Aumentar día");
        jbDateIssueForward.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbDateIssueForward.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel12.add(jbDateIssueForward);

        jPanel2.add(jPanel12);

        jPanel13.add(jPanel2);

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Lotes de facturas para exportación:"));
        jPanel14.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbInvoiceBatchShow.setText("Mostrar");
        jbInvoiceBatchShow.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel15.add(jbInvoiceBatchShow);

        jbInvoiceBatchClear.setText("Limpiar");
        jbInvoiceBatchClear.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel15.add(jbInvoiceBatchClear);

        jPanel14.add(jPanel15);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgInvoiceBatch.add(moRadInvoiceBatchAll);
        moRadInvoiceBatchAll.setText("Exportar todos los lotes de facturas");
        moRadInvoiceBatchAll.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel16.add(moRadInvoiceBatchAll);

        jPanel14.add(jPanel16);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgInvoiceBatch.add(moRadInvoiceBatchSel);
        moRadInvoiceBatchSel.setText("Exportar lote de facturas seleccionado");
        moRadInvoiceBatchSel.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel17.add(moRadInvoiceBatchSel);

        jPanel14.add(jPanel17);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInvoiceBatch.setText("Lote de facturas:*");
        jlInvoiceBatch.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel18.add(jlInvoiceBatch);

        moKeyInvoiceBatch.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel18.add(moKeyInvoiceBatch);

        jPanel14.add(jPanel18);

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel14.add(jPanel19);

        jPanel13.add(jPanel14);

        jPanel1.add(jPanel13, java.awt.BorderLayout.NORTH);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Criterio de actualización de catálogos SIIE:"));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel8.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolUpdateData.setText("Actualizar catálogos SIIE exportados anteriormente");
        moBoolUpdateData.setPreferredSize(new java.awt.Dimension(500, 23));
        jPanel9.add(moBoolUpdateData);

        jPanel8.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgUpdateMode.add(moRadUpdateModeSel);
        moRadUpdateModeSel.setText("Actualizar selectivamente");
        moRadUpdateModeSel.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel10.add(moRadUpdateModeSel);

        jlUpdateModeSel.setForeground(java.awt.Color.gray);
        jlUpdateModeSel.setText("(Últimos cambios realizados por los usuarios se conservan)");
        jlUpdateModeSel.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel10.add(jlUpdateModeSel);

        jPanel8.add(jPanel10);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgUpdateMode.add(moRadUpdateModeAll);
        moRadUpdateModeAll.setText("Actualizar todo");
        moRadUpdateModeAll.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel11.add(moRadUpdateModeAll);

        jlUpdateModeAll.setForeground(java.awt.Color.gray);
        jlUpdateModeAll.setText("(Últimos cambios realizados por los usuarios se pierden)");
        jlUpdateModeAll.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel11.add(jlUpdateModeAll);

        jPanel8.add(jPanel11);

        jPanel5.add(jPanel8, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgExportMode;
    private javax.swing.ButtonGroup bgInvoiceBatch;
    private javax.swing.ButtonGroup bgUpdateMode;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbDateIssueBackward;
    private javax.swing.JButton jbDateIssueForward;
    private javax.swing.JButton jbInvoiceBatchClear;
    private javax.swing.JButton jbInvoiceBatchShow;
    private javax.swing.JLabel jlDateIssue;
    private javax.swing.JLabel jlDatePeriodEnd;
    private javax.swing.JLabel jlDatePeriodStart;
    private javax.swing.JLabel jlInvoiceBatch;
    private javax.swing.JLabel jlUpdateModeAll;
    private javax.swing.JLabel jlUpdateModeSel;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolUpdateData;
    private sa.lib.gui.bean.SBeanFieldDate moDateIssue;
    private sa.lib.gui.bean.SBeanFieldDate moDatePeriodEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDatePeriodStart;
    private sa.lib.gui.bean.SBeanFieldKey moKeyInvoiceBatch;
    private sa.lib.gui.bean.SBeanFieldRadio moRadExportModeAll;
    private sa.lib.gui.bean.SBeanFieldRadio moRadExportModeCat;
    private sa.lib.gui.bean.SBeanFieldRadio moRadInvoiceBatchAll;
    private sa.lib.gui.bean.SBeanFieldRadio moRadInvoiceBatchSel;
    private sa.lib.gui.bean.SBeanFieldRadio moRadUpdateModeAll;
    private sa.lib.gui.bean.SBeanFieldRadio moRadUpdateModeSel;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods
     */
    
    private void initComponentsCustom () {
        SDbConfigAvista configAvista = null;
        
        SGuiUtils.setWindowBounds(this, 640, 400);
        
        moRadExportModeCat.setBooleanSettings(SGuiUtils.getLabelName(moRadExportModeCat.getText()), false);
        moRadExportModeAll.setBooleanSettings(SGuiUtils.getLabelName(moRadExportModeAll.getText()), true);
        moDatePeriodStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDatePeriodStart), true);
        moDatePeriodEnd.setDateSettings(miClient, SGuiUtils.getLabelName(jlDatePeriodEnd), true);
        moDateIssue.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateIssue), true);
        moRadInvoiceBatchAll.setBooleanSettings(SGuiUtils.getLabelName(moRadInvoiceBatchAll.getText()), true);
        moRadInvoiceBatchSel.setBooleanSettings(SGuiUtils.getLabelName(moRadInvoiceBatchSel.getText()), false);
        moKeyInvoiceBatch.setKeySettings(miClient, SGuiUtils.getLabelName(jlInvoiceBatch), true);
        moBoolUpdateData.setBooleanSettings(SGuiUtils.getLabelName(moBoolUpdateData.getText()), false);
        moRadUpdateModeSel.setBooleanSettings(SGuiUtils.getLabelName(moRadUpdateModeSel.getText()), false);
        moRadUpdateModeAll.setBooleanSettings(SGuiUtils.getLabelName(moRadUpdateModeAll.getText()), false);
        
        moFields.addField(moRadExportModeCat);
        moFields.addField(moRadExportModeAll);
        moFields.addField(moDatePeriodStart);
        moFields.addField(moDatePeriodEnd);
        moFields.addField(moDateIssue);
        moFields.addField(moRadInvoiceBatchAll);
        moFields.addField(moRadInvoiceBatchSel);
        moFields.addField(moKeyInvoiceBatch);
        moFields.addField(moBoolUpdateData);
        moFields.addField(moRadUpdateModeSel);
        moFields.addField(moRadUpdateModeAll);
        moFields.setFormButton(jbSave);
        
        jbSave.setText(SGuiConsts.TXT_BTN_OK);
        
        try {
            configAvista = ((SDbConfig) miClient.getSession().getConfigSystem()).getDbConfigAvista();
            
            moConnectionAvista = createConnection(
                    SEtlConsts.DB_SQL_SERVER, 
                    configAvista.getAvistaHost(), 
                    configAvista.getAvistaPort(), 
                    configAvista.getAvistaName(), 
                    configAvista.getAvistaUser(), 
                    configAvista.getAvistaPassword());
            
            moStatementAvista = moConnectionAvista.createStatement();
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void populateInvoiceBatchs() {
        ArrayList<SGuiItem> items = null;
        
        try {
            moKeyInvoiceBatch.removeAllItems();

            items = SEtlUtils.createAvistaInvoiceBatchGuiItems(new Date[]{ moDatePeriodStart.getValue(), moDatePeriodEnd.getValue() }, moStatementAvista);
            
            for (SGuiItem item : items) {
                moKeyInvoiceBatch.addItem(item);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private void updateStatusExportMode() {
        if (moRadExportModeCat.isSelected()) {
            moDateIssue.setEditable(false);
            jbDateIssueBackward.setEnabled(false);
            jbDateIssueForward.setEnabled(false);
            
            moDateIssue.setValue(null);
            
            actionPerformedInvoiceBatchClear(false);
            jbInvoiceBatchShow.setEnabled(false);
        }
        else {
            moDateIssue.setEditable(true);
            jbDateIssueBackward.setEnabled(true);
            jbDateIssueForward.setEnabled(true);
            
            moDateIssue.setValue(moDatePeriodEnd.getValue());
            
            jbInvoiceBatchShow.setEnabled(true);
        }
    }
    
    private void updateStatusUpdateMode() {
        if (!moBoolUpdateData.isSelected()) {
            moRadUpdateModeSel.setEnabled(false);
            moRadUpdateModeAll.setEnabled(false);
            
            moRadUpdateModeSel.setSelected(true);
        }
        else {
            moRadUpdateModeSel.setEnabled(true);
            moRadUpdateModeAll.setEnabled(true);
            
            if (((SDbConfig) miClient.getSession().getConfigSystem()).getDbConfigAvista().getGuiEtlUpdateMode() == SEtlConsts.UPD_MODE_SEL) {
                moRadUpdateModeSel.setSelected(true);
            }
            else {
                moRadUpdateModeAll.setSelected(true);
            }
        }
    }
    
    private void updateStatusInvoiceBatch(boolean show) {
        if (show) {
            populateInvoiceBatchs();
        }
        else {
            moKeyInvoiceBatch.removeAllItems();
        }

        jbInvoiceBatchShow.setEnabled(!show);
        jbInvoiceBatchClear.setEnabled(show);

        moDatePeriodEnd.setEditable(!show);
        moDatePeriodStart.setEditable(!show);
        moDateIssue.setEditable(!show && moRadExportModeAll.isSelected());
        jbDateIssueBackward.setEnabled(!show && moRadExportModeAll.isSelected());
        jbDateIssueForward.setEnabled(!show && moRadExportModeAll.isSelected());

        moRadInvoiceBatchAll.setEnabled(show);
        moRadInvoiceBatchSel.setEnabled(show);

        if (show) {
            moRadInvoiceBatchSel.setSelected(true);
            itemStateChangedRadInvoiceBatchSel();
        }
        else {
            moRadInvoiceBatchAll.setSelected(true);
            itemStateChangedRadInvoiceBatchAll();
        }
    }
    
    private void actionPerformedIssueBackward() {
        moDateIssue.setValue(SLibTimeUtils.addDate(moDateIssue.getValue() != null ? moDateIssue.getValue() : moDatePeriodEnd.getValue(), 0, 0, -1));
        moDateIssue.requestFocus();
    }
    
    private void actionPerformedIssueForward() {
        moDateIssue.setValue(SLibTimeUtils.addDate(moDateIssue.getValue() != null ? moDateIssue.getValue() : moDatePeriodEnd.getValue(), 0, 0, 1));
        moDateIssue.requestFocus();
    }
    
    private void actionPerformedInvoiceBatchShow() {
        SGuiValidation validation = new SGuiValidation();
        
        if (moDatePeriodStart.getValue() == null) {
            validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlDatePeriodStart) + "'.");
            validation.setComponent(moDatePeriodStart);
        }
        else if (moDatePeriodEnd.getValue() == null) {
            validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlDatePeriodEnd) + "'.");
            validation.setComponent(moDatePeriodEnd);
        }
        else if (moDateIssue.getValue() == null) {
            validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlDateIssue) + "'.");
            validation.setComponent(moDateIssue);
        }
        
        if (!validation.isValid()) {
            SGuiUtils.computeValidation(miClient, validation);
        }
        else {
            updateStatusInvoiceBatch(true);
            moRadInvoiceBatchAll.requestFocus();
        }
    }
    
    private void actionPerformedInvoiceBatchClear(boolean requestFocus) {
        updateStatusInvoiceBatch(false);
        if (requestFocus) {
            moDatePeriodStart.requestFocus();
        }
    }
    
    private void itemStateChangedRadExportModeCat() {
        updateStatusExportMode();
    }
    
    private void itemStateChangedRadExportModeAll() {
        updateStatusExportMode();
    }
    
    private void itemStateChangedRadInvoiceBatchAll() {
        moKeyInvoiceBatch.setEnabled(false);
        
        if (moKeyInvoiceBatch.getItemCount() > 0) {
            moKeyInvoiceBatch.setSelectedIndex(0);
        }
    }
    
    private void itemStateChangedRadInvoiceBatchSel() {
        moKeyInvoiceBatch.setEnabled(true);
    }
    
    private void itemStateChangedUpdateData() {
        updateStatusUpdateMode();
    }
    
    /*
     * Public methods
     */
    
    /*
     * Overriden methods
     */
    
    @Override
    public void addAllListeners() {
        jbDateIssueBackward.addActionListener(this);
        jbDateIssueForward.addActionListener(this);
        jbInvoiceBatchShow.addActionListener(this);
        jbInvoiceBatchClear.addActionListener(this);
        moRadExportModeCat.addItemListener(this);
        moRadExportModeAll.addItemListener(this);
        moRadInvoiceBatchAll.addItemListener(this);
        moRadInvoiceBatchSel.addItemListener(this);
        moBoolUpdateData.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbDateIssueBackward.removeActionListener(this);
        jbDateIssueForward.removeActionListener(this);
        jbInvoiceBatchShow.removeActionListener(this);
        jbInvoiceBatchClear.removeActionListener(this);
        moRadExportModeCat.removeItemListener(this);
        moRadExportModeAll.removeItemListener(this);
        moRadInvoiceBatchAll.removeItemListener(this);
        moRadInvoiceBatchSel.removeItemListener(this);
        moBoolUpdateData.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        if (validation.isValid()) {
            if (moRadExportModeAll.isSelected()) {
                if (moDateIssue.getValue().after(moDatePeriodEnd.getValue())) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DATE_ + "'" + SGuiUtils.getLabelName(jlDateIssue) + "'" + SGuiConsts.ERR_MSG_FIELD_DATE_LESS_EQUAL + "'" + SLibUtils.DateFormatDate.format(moDatePeriodEnd.getValue()) + "'.");
                    validation.setComponent(moDateIssue.getComponent());
                }
                else if (moDateIssue.getValue().before(SLibTimeUtils.addDate(moDatePeriodEnd.getValue(), 0, 0, -SEtlConsts.BILLING_DELAY_DAYS))) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DATE_ + "'" + SGuiUtils.getLabelName(jlDateIssue) + "'" + SGuiConsts.ERR_MSG_FIELD_DATE_GREAT_EQUAL + "'" + SLibUtils.DateFormatDate.format(SLibTimeUtils.addDate(moDatePeriodEnd.getValue(), 0, 0, -SEtlConsts.BILLING_DELAY_DAYS)) + "'.");
                    validation.setComponent(moDateIssue.getComponent());
                }
                else if (moKeyInvoiceBatch.getItemCount() == 0) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlInvoiceBatch) + "'.");
                    validation.setComponent(jbInvoiceBatchShow);
                }
            }
        }
        
        return validation;
    }

    @Override
    public void initForm() {
        try {
            removeAllListeners();

            reloadCatalogues();

            moRadExportModeAll.setSelected(true);
            moDatePeriodStart.setValue(miClient.getSession().getCurrentDate());
            moDatePeriodEnd.setValue(miClient.getSession().getCurrentDate());

            updateStatusExportMode();
            updateStatusUpdateMode();
            actionPerformedInvoiceBatchClear(false);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            addAllListeners();
        }
    }
    
    @Override
    public void actionSave() {
        String msg = "";
        
        if (jbSave.isEnabled()) {
            if (SGuiUtils.computeValidation(miClient, validateForm())) {
                msg = "PARÁMETROS DE EXPORTACIÓN A " + SEtlConsts.TXT_SYS_SIIE + ":\n\n"
                        + "- Fecha de sistema = " + SLibUtils.DateFormatDate.format(miClient.getSession().getSystemDate()) + "\n"
                        + "- Período de exportación = " + SLibUtils.DateFormatDate.format(moDatePeriodStart.getValue()) + " - " + SLibUtils.DateFormatDate.format(moDatePeriodEnd.getValue()) + "\n"
                        + "- Fecha de emisión de facturas = " + (moDateIssue.getValue() == null ? "n/a" : SLibUtils.DateFormatDate.format(moDateIssue.getValue())) + "\n"
                        + "- Lote de facturas = " + (moKeyInvoiceBatch.getSelectedIndex() <= 0 ? "n/a" : "#" + moKeyInvoiceBatch.getValue()[0]) + "\n"
                        + "- ¿Actualizar catálogos " + SEtlConsts.TXT_SYS_SIIE + " exportados anteriormente? = " + (moBoolUpdateData.isSelected() ? "Sí" : "No") + "\n\n"
                        + SGuiConsts.MSG_CNF_CONT;
                
                if (miClient.showMsgBoxConfirm(msg) == JOptionPane.YES_OPTION) {
                    try {
                        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

                        String message = SEtlProcess.computeEtl(miClient.getSession(), 
                                moRadExportModeCat.isSelected() ? SEtlConsts.EXP_MODE_CAT : SEtlConsts.EXP_MODE_ALL, 
                                moDatePeriodStart.getValue(), 
                                moDatePeriodEnd.getValue(), 
                                moDateIssue.getValue(), 
                                moKeyInvoiceBatch.getSelectedIndex() <= 0 ? SLibConsts.UNDEFINED : moKeyInvoiceBatch.getValue()[0], 
                                moBoolUpdateData.getValue(), 
                                moRadUpdateModeSel.isSelected() ? SEtlConsts.UPD_MODE_SEL : SEtlConsts.UPD_MODE_ALL);

                        miClient.showMsgBoxInformation(SLibConsts.MSG_PROCESS_FINISHED + "\n" + message);

                        mnFormResult = SGuiConsts.FORM_RESULT_OK;
                        dispose();
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                    finally {
                        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbDateIssueBackward) {
                actionPerformedIssueBackward();
            }
            else if (button == jbDateIssueForward) {
                actionPerformedIssueForward();
            }
            else if (button == jbInvoiceBatchShow) {
                actionPerformedInvoiceBatchShow();
            }
            else if (button == jbInvoiceBatchClear) {
                actionPerformedInvoiceBatchClear(true);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof SBeanFieldRadio) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                SBeanFieldRadio field = (SBeanFieldRadio) e.getSource();
                
                if (field == moRadExportModeCat) {
                    itemStateChangedRadExportModeCat();
                }
                else if (field == moRadExportModeAll) {
                    itemStateChangedRadExportModeAll();
                }
                else if (field == moRadInvoiceBatchAll) {
                    itemStateChangedRadInvoiceBatchAll();
                }
                else if (field == moRadInvoiceBatchSel) {
                    itemStateChangedRadInvoiceBatchSel();
                }
            }
        }
        else if (e.getSource() instanceof SBeanFieldBoolean) {
            SBeanFieldBoolean field = (SBeanFieldBoolean) e.getSource();

            if (field == moBoolUpdateData) {
                itemStateChangedUpdateData();
            }
        }
    }
}
