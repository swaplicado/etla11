/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.form;

import etla.mod.SModConsts;
import etla.mod.sms.db.SDbConfigSms;
import java.sql.ResultSet;
import sa.gui.util.SUtilConsts;
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
public class SDialogWeightComparisonReport extends SBeanDialogReport {
    private SDbConfigSms moConfigSms;

    /**
     * Creates new form SDialogWeightComparisonReport
     * @param client
     * @param title
     */
    public SDialogWeightComparisonReport(SGuiClient client, String title) {
        setFormSettings(client, SModConsts.SR_WEIGHT_COMPAR, 0, title);
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

        bgReportType = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moDateDateStart = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel12 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        moDateDateEnd = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel13 = new javax.swing.JPanel();
        jrbAll = new javax.swing.JRadioButton();
        jPanel14 = new javax.swing.JPanel();
        jrbSurplus = new javax.swing.JRadioButton();
        jPanel15 = new javax.swing.JPanel();
        jrbMissing = new javax.swing.JRadioButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(11, 1, 0, 5));

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

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgReportType.add(jrbAll);
        jrbAll.setText("Todos los embarques con diferencia en peso");
        jPanel13.add(jrbAll);

        jPanel2.add(jPanel13);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgReportType.add(jrbSurplus);
        jrbSurplus.setText("Sólo embarques con peso excedente");
        jPanel14.add(jrbSurplus);

        jPanel2.add(jPanel14);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgReportType.add(jrbMissing);
        jrbMissing.setText("Sólo embarques con peso faltante");
        jPanel15.add(jrbMissing);

        jPanel2.add(jPanel15);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgReportType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JRadioButton jrbAll;
    private javax.swing.JRadioButton jrbMissing;
    private javax.swing.JRadioButton jrbSurplus;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateStart;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 640, 400);

        moDateDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateStart), true);
        moDateDateEnd.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateEnd), true);

        moFields.addField(moDateDateStart);
        moFields.addField(moDateDateEnd);

        moFields.setFormButton(jbPrint);

        moDateDateStart.setValue(SLibTimeUtils.getBeginOfYear(miClient.getSession().getCurrentDate()));
        moDateDateEnd.setValue(SLibTimeUtils.getEndOfYear(miClient.getSession().getCurrentDate()));
        
        jrbAll.setSelected(true);

        moConfigSms = (SDbConfigSms) miClient.getSession().readRegistry(SModConsts.S_CFG, new int[] { SUtilConsts.BPR_CO_ID });
    }
    
    private int getTotalTicketsCant() {
        int cant = 0;
        try {
            String sql = "SELECT COUNT(*) " +
                "FROM s_shipt AS s " +
                "INNER JOIN s_wm_ticket AS t ON s.ticket_id = t.ticket_id " +
                "INNER JOIN su_vehic_tp AS v ON s.fk_vehic_tp = v.id_vehic_tp " +
                "INNER JOIN su_shipper AS sh ON s.fk_shipper = sh.id_shipper " +
                "WHERE s.b_del = 0 AND t.b_del = 0 " +
                "AND s.shipt_date BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(moDateDateStart.getValue()) + "' " +
                "AND '" + SLibUtils.DbmsDateFormatDate.format(moDateDateEnd.getValue()) + "' ";
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                cant = resultSet.getInt(1);
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
        return cant;
    }
    
    private int getSurplusTicketsCant() {
        int cant = 0;
        try {
            String sql = "SELECT COUNT(*) " +
                "FROM s_shipt AS s " +
                "INNER JOIN s_wm_ticket AS t ON s.ticket_id = t.ticket_id " +
                "INNER JOIN su_vehic_tp AS v ON s.fk_vehic_tp = v.id_vehic_tp " +
                "INNER JOIN su_shipper AS sh ON s.fk_shipper = sh.id_shipper " +
                "WHERE s.b_del = 0 AND t.b_del = 0 " +
                "AND s.shipt_date BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(moDateDateStart.getValue()) + "' " +
                "AND '" + SLibUtils.DbmsDateFormatDate.format(moDateDateEnd.getValue()) + "' " +
                "AND (t.weight - s.kg > " + moConfigSms.getWmOutMaxVariationWeight() + " " + 
                "OR (t.weight - s.kg)/t.weight > " + moConfigSms.getWmOutMaxVariationPercent() + ") ";
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                cant = resultSet.getInt(1);
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
        return cant;
    }
    
    private int getMissingTicketsCant() {
        int cant = 0;
        try {
            String sql = "SELECT COUNT(*) " +
                "FROM s_shipt AS s " +
                "INNER JOIN s_wm_ticket AS t ON s.ticket_id = t.ticket_id " +
                "INNER JOIN su_vehic_tp AS v ON s.fk_vehic_tp = v.id_vehic_tp " +
                "INNER JOIN su_shipper AS sh ON s.fk_shipper = sh.id_shipper " +
                "WHERE s.b_del = 0 AND t.b_del = 0 " +
                "AND s.shipt_date BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(moDateDateStart.getValue()) + "' " +
                "AND '" + SLibUtils.DbmsDateFormatDate.format(moDateDateEnd.getValue()) + "' " +
                "AND (t.weight - s.kg < " + moConfigSms.getWmOutMaxVariationWeight() + " " + 
                "OR (t.weight - s.kg)/t.weight < " + moConfigSms.getWmOutMaxVariationPercent() + ") ";
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                cant = resultSet.getInt(1);
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
        return cant;
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
        
        String sqlWhere;
        String reportType;
        if (jrbSurplus.isSelected()) {
            sqlWhere = "AND (t.weight - s.kg > " + moConfigSms.getWmOutMaxVariationWeight() + " " + 
                "OR (t.weight - s.kg)/t.weight > " + moConfigSms.getWmOutMaxVariationPercent() + ") ";
            reportType = jrbSurplus.getText();
        }
        else if (jrbMissing.isSelected()) {
            sqlWhere = "AND (t.weight - s.kg < " + moConfigSms.getWmOutMaxVariationWeight() + " " + 
                "OR (t.weight - s.kg)/t.weight < " + moConfigSms.getWmOutMaxVariationPercent() + ") ";
            reportType = jrbMissing.getText();
        }
        else {
            sqlWhere = "AND t.weight - s.kg <> 0 ";
            reportType = jrbAll.getText();
        }
        
        moParamsMap.put("tDateStart", moDateDateStart.getValue());
        moParamsMap.put("tDateEnd", moDateDateEnd.getValue());
        moParamsMap.put("sSqlWhere", sqlWhere);
        moParamsMap.put("nTotalTicketsCant", getTotalTicketsCant());
        moParamsMap.put("nSurplusTicketsCant", getSurplusTicketsCant());
        moParamsMap.put("nMissingTicketsCant", getMissingTicketsCant());
        moParamsMap.put("sReportType", reportType);
        
    }
}
