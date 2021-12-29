/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.form;

import etla.mod.SModConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanDialogReport;

/**
 *
 * @author Isabel Servín 
 * 
 */
public class SDialogCapacityUsedPerShipment extends SBeanDialogReport {
    
    /**
     * Creates new form SDialogCapacityUsedPerShipment
     * @param client
     * @param title
     */
    public SDialogCapacityUsedPerShipment(SGuiClient client, String title) {
        setFormSettings(client, SModConsts.SR_SHIPT_CAPACITY, 0, title);
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moDateDateStart = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel12 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        moDateDateEnd = new sa.lib.gui.bean.SBeanFieldDate();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial:*");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlDateStart);
        jPanel11.add(moDateDateStart);

        jPanel2.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha final:*");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlDateEnd);
        jPanel12.add(moDateDateEnd);

        jPanel2.add(jPanel12);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateStart;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 320, 200);

        moDateDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateStart), true);
        moDateDateEnd.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateEnd), true);

        moFields.addField(moDateDateStart);
        moFields.addField(moDateDateEnd);

        moFields.setFormButton(jbPrint);

        moDateDateStart.setValue(SLibTimeUtils.getBeginOfYear(miClient.getSession().getCurrentDate()));
        moDateDateEnd.setValue(SLibTimeUtils.getEndOfYear(miClient.getSession().getCurrentDate()));
        
    }
    
    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            validation = SGuiUtils.validateDateRange(moDateDateStart, moDateDateEnd);
        }
        return validation;
    }

    @Override
    public void createParamsMap() {
        moParamsMap = miClient.createReportParams();
        
        String sqlWhere = "WHERE shipt_date BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(moDateDateStart.getValue()) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(moDateDateEnd.getValue()) + "'";
        
        moParamsMap.put("tDateStart", moDateDateStart.getValue());
        moParamsMap.put("tDateEnd", moDateDateEnd.getValue());
        moParamsMap.put("sSqlWhere", sqlWhere);
    }
}