/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.form;

import erp.lib.SLibUtilities;
import etla.mod.SModConsts;
import etla.mod.SModSysConsts;
import etla.mod.cfg.db.SDbConfig;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import javax.mail.MessagingException;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiSession;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFormDialog;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import java.util.Iterator;

/**
 *
 * @author Alfredo Pérez, Cesar Orozco, Sergio Flores
 */
public class SDialogSendWmReport extends SBeanFormDialog implements ActionListener, ItemListener {

    /**
     * 
     * @param client
     * @param title 
     */
    public SDialogSendWmReport(SGuiClient client, String title) {
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
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlES = new javax.swing.JLabel();
        moKeyES = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel7 = new javax.swing.JPanel();
        jlCompany = new javax.swing.JLabel();
        moKeyCompany = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel5 = new javax.swing.JPanel();
        jlStart = new javax.swing.JLabel();
        moStartDate = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel8 = new javax.swing.JPanel();
        jlEnd = new javax.swing.JLabel();
        moEndDate = new sa.lib.gui.bean.SBeanFieldDate();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Envio mail resumen báscula:"));
        jPanel1.setPreferredSize(new java.awt.Dimension(640, 400));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel2.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlES.setText("Entrada/Salida:*");
        jlES.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlES);

        moKeyES.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel4.add(moKeyES);
        jPanel4.add(jPanel7);

        jlCompany.setText("Empresa:");
        jlCompany.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlCompany);

        moKeyCompany.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel4.add(moKeyCompany);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlStart.setText("Fecha inicial:*");
        jlStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlStart);
        jPanel5.add(moStartDate);
        jPanel5.add(jPanel8);

        jlEnd.setText("Fecha final:*");
        jlEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlEnd);
        jPanel5.add(moEndDate);

        jPanel2.add(jPanel5);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgExportMode;
    private javax.swing.ButtonGroup bgInvoiceBatch;
    private javax.swing.ButtonGroup bgUpdateMode;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel jlCompany;
    private javax.swing.JLabel jlES;
    private javax.swing.JLabel jlEnd;
    private javax.swing.JLabel jlStart;
    private sa.lib.gui.bean.SBeanFieldDate moEndDate;
    private sa.lib.gui.bean.SBeanFieldKey moKeyCompany;
    private sa.lib.gui.bean.SBeanFieldKey moKeyES;
    private sa.lib.gui.bean.SBeanFieldDate moStartDate;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 640, 400);

        moStartDate.setDateSettings(miClient, SGuiUtils.getLabelName(jlStart), true);
        moEndDate.setDateSettings(miClient, SGuiUtils.getLabelName(jlEnd), true);
        moKeyES.setKeySettings(miClient, SGuiUtils.getLabelName(jlES), true);
        moKeyCompany.setKeySettings(miClient, SGuiUtils.getLabelName(jlCompany), true);

        moFields.addField(moKeyES);
        moFields.addField(moKeyCompany);
        moFields.addField(moStartDate);
        moFields.addField(moEndDate);
        moFields.setFormButton(jbSave);
        moStartDate.setValue(miClient.getSession().getCurrentDate());
        moEndDate.setValue(miClient.getSession().getCurrentDate());

        jbSave.setText(SGuiConsts.TXT_BTN_OK);
    }

    /*
     * Overriden methods
     */
    @Override
    public void addAllListeners() {
        moKeyES.addItemListener(this);
        moKeyCompany.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        moKeyES.removeActionListener(this);
        moKeyCompany.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyES, SModConsts.SS_WM_TICKET_TP, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyCompany, SModConsts.SU_WM_USER, SLibConsts.UNDEFINED, null);
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

        if (moKeyES.getItemCount() == 0) {
            validation.setMessage(SGuiConsts.MSG_GUI_SELECT_OPTION + "'" + SGuiUtils.getLabelName(jlES) + "'.");
        }
        if (moStartDate.getValue() == null) {
            validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlStart) + "'.");
        }
        if (moEndDate.getValue() != null) {
            if (moEndDate.getValue().before(moStartDate.getValue())) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DATE_ + "'" + SGuiUtils.getLabelName(jlEnd) + "'" + SGuiConsts.ERR_MSG_FIELD_DATE_GREAT_EQUAL + "'" + SGuiUtils.getLabelName(jlStart) + "'.");
                validation.setComponent(moEndDate.getComponent());
            }
        }

        return validation;
    }

    @Override
    public void initForm() {
        try {
            removeAllListeners();
            reloadCatalogues();
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
        if (jbSave.isEnabled()) {
            if (SGuiUtils.computeValidation(miClient, validateForm())) {
                try {
                    sendMail(miClient.getSession());
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }

    /**
     * 
     * @param session
     * @return all available weignt machin users
     * @throws SQLException 
     */
    private ArrayList availableWmUsers(final SGuiSession session) throws SQLException {
        ArrayList<Integer> maWmUserIds = new ArrayList<>();
        Statement statement = session.getDatabase().getConnection().createStatement();
        String msSql = "SELECT id_wm_user "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_WM_USER) + " "
                + "WHERE NOT b_del AND "
                + "id_wm_user <> " + SModSysConsts.SU_WM_USER_NA;
        ResultSet resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            maWmUserIds.add(resultSet.getInt("id_wm_user"));
        }
        return maWmUserIds;
    }

    /**
     * 
     * @param session
     * @param msCompany
     * @return selected weight machin user id
     * @throws Exception 
     */
    private ArrayList selectedWmUserIds(final SGuiSession session, String msCompany) throws Exception {
        ArrayList<Integer> maWmUserids = new ArrayList<>();
        Statement statement = session.getDatabase().getConnection().createStatement();
        String msSql = "SELECT id_wm_user "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_WM_USER) + " "
                + "WHERE name = '" + msCompany + "' AND "
                + "id_wm_user <> " + SModSysConsts.SU_WM_USER_NA;
        ResultSet resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            maWmUserids.add(resultSet.getInt("id_wm_user"));
        }
        return maWmUserids;
    }

    /**
     * 
     * @param session
     * @throws Exception 
     */
    private void sendMail(final SGuiSession session) throws Exception {
        String subject;
        String body = "";
        int nComparator = 0;
        double totalWeight = 0;
        double percent;
        double auxWeight;
        ArrayList<String> suppliersNames = new ArrayList<>();
        ArrayList<Double> weights = new ArrayList<>();
        Iterator<String> itrName;
        Iterator<Double> itrWeight;
        int totalCompany = 0;
        boolean canSend = false;

        int startYear = SLibTimeUtils.digestDate(moStartDate.getValue())[0];
        int startMonth = SLibTimeUtils.digestDate(moStartDate.getValue())[1];
        int startDay = SLibTimeUtils.digestDate(moStartDate.getValue())[2];
        String startDate = startYear + "-" + startMonth + "-" + startDay;
        int endYear = SLibTimeUtils.digestDate(moEndDate.getValue())[0];
        int endMonth = SLibTimeUtils.digestDate(moEndDate.getValue())[1];
        int endDay = SLibTimeUtils.digestDate(moEndDate.getValue())[2];
        String endDate = endYear + "-" + endMonth + "-" + endDay;
        int loop = 1;
        boolean currentCompany = true;
        ArrayList ids = null;
        
        
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.0 '%'");

        String dateStart = (startDay <= 9 ? "0" + startDay : startDay) + "-"
                + (startMonth <= 9 ? "0" + startMonth : startMonth) + "-"
                + startYear;
        String dateEnd = (endDay <= 9 ? "0" + endDay : endDay) + "-"
                + (endMonth <= 9 ? "0" + endMonth : endMonth) + "-"
                + endYear;
        String dateRange = dateStart + " al " + dateEnd;
        
        subject = "[ETL] " + (moKeyES.getSelectedIndex() == 1 ? "Entradas" : "Salidas") + " báscula " + (dateStart.equals(dateEnd) ? dateStart : dateRange);

        if (moKeyCompany.getSelectedIndex() == 1) {
            ids = availableWmUsers(session);
            moKeyCompany.getSelectedItem();
            loop = moKeyCompany.getItemCount();
            currentCompany = false;
        }
        else {
            ids = selectedWmUserIds(session, moKeyCompany.getSelectedItem() + "");
        }

        for (int i = 1; ids.size() >= i; i++) {
            String msSql = "SELECT "
                    + "company, "
                    + "fk_wm_item, "
                    + "SUM(weight) AS Peso, "
                    + "(SELECT SUM(weight) "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.S_WM_TICKET) + " "
                    + "WHERE weight_arr " + (moKeyES.getSelectedIndex() == 1 ? ">" : "<") + "  weight_dep AND "
                    + "fk_wm_user =  '" + ids.get(i - 1) + "' AND "
                    + "(ticket_dt_arr BETWEEN '" + (startDate) + "' AND '" + (endDate) + "')) AS SUMA_TOTAL_TOTAL, "
                    + "((sum(weight))/("
                    + "SELECT SUM(weight) "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.S_WM_TICKET) + " "
                    + "WHERE weight_arr " + (moKeyES.getSelectedIndex() == 1 ? ">" : "<") + "  weight_dep AND "
                    + "fk_wm_user = '" + ids.get(i - 1) + "' AND "
                    + "(ticket_dt_arr BETWEEN '" + (startDate) + "' AND '" + (endDate) + "'))) AS Porcentaje, "
                    + "i.name, "
                    + "wmu.name "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.S_WM_TICKET) + " "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_WM_ITEM) + " AS i ON fk_wm_item = i.id_wm_item "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_WM_USER) + " AS wmu ON fk_wm_user = wmu.id_wm_user "
                    + "WHERE weight_arr " + (moKeyES.getSelectedIndex() == 1 ? ">" : "<") + "  weight_dep AND "
                    + "(ticket_dt_arr BETWEEN  '" + (startDate) + "' AND '" + (endDate) + "') AND "
                    + "fk_wm_user = '" + ids.get(i - 1) + "' "
                    + "GROUP BY " + (moKeyES.getSelectedIndex() == 1 ? "company " : "fk_wm_item ")
                    + "ORDER BY i.name, fk_wm_item, company;";

            Statement statement;
            statement = session.getDatabase().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(msSql);

            if (resultSet.next()) {
                body += "<b><h2> " + 
                        (currentCompany == true ? moKeyCompany.getSelectedItem() : moKeyES.getSelectedIndex() == 1 ? resultSet.getString("wmu.name") : resultSet.getString("company")) 
                        + "</h2></b> ";
                resultSet.previous();
                if (moKeyES.getSelectedIndex() == 1) {
                    body += "<b>Boletos de entrada: "
                            + (dateStart.equals(dateEnd) ? dateStart : dateRange) + "</b><br><br> ";
                    while (resultSet.next()) {
                        if (nComparator == resultSet.getInt("fk_wm_item")) {
                            suppliersNames.add(resultSet.getString("company"));
                            weights.add(resultSet.getDouble("Peso"));
                            totalWeight += resultSet.getDouble("Peso");
                        }
                        else {
                            resultSet.previous();
                            itrName = suppliersNames.iterator();
                            itrWeight = weights.iterator();
                            while (itrName.hasNext()) {
                                auxWeight = itrWeight.next();
                                percent = (auxWeight) / totalWeight;
                                body += "<tr>"
                                        + "<td align='left'>" + itrName.next() + "</td> "
                                        + "<td align='right'>" + sa.lib.SLibUtils.DecimalFormatValue0D.format(auxWeight) + "</td> "
                                        + "<td align='right'>" + decimalFormat.format(percent * 100) + "</td> "
                                        + "</tr> ";
                            }
                            if (nComparator != 0) {
                                body += "<tr> "
                                        + "<td align='center'>Total</td> "
                                        + "<td align='right'>" + sa.lib.SLibUtils.DecimalFormatValue0D.format(totalWeight) + "</td> "
                                        + "<td align='right'>100%</td> "
                                        + "</tr> "
                                        + "</table><br> ";
                            }
                            weights.clear();
                            suppliersNames.clear();
                            totalCompany += totalWeight;
                            totalWeight = 0;
                            resultSet.next();
                            nComparator = resultSet.getInt("fk_wm_item");
                            body += "<b>Producto: " + resultSet.getString("i.name") + "</b> ";
                            body += "<table border='1' bordercolor='#000000' style='background-color:' width='400' cellpadding='0' cellspacing='0'> "
                                    + "<tr> "
                                    + "<th>Provedor</th> "
                                    + "<th>kg</th> "
                                    + "<th>%</th> "
                                    + "</tr> ";
                            resultSet.previous();
                        }
                    }
                    resultSet.previous();
                    itrName = suppliersNames.iterator();
                    itrWeight = weights.iterator();
                    while (itrName.hasNext()) {
                        auxWeight = itrWeight.next();
                        percent = (auxWeight) / totalWeight;
                        body += "<tr> "
                                + "<td align='left'>" + itrName.next() + "</td> "
                                + "<td align='right'>" + sa.lib.SLibUtils.DecimalFormatValue0D.format(auxWeight) + "</td> "
                                + "<td align='right'>" + decimalFormat.format(percent * 100) + "</td> "
                                + "</tr> ";
                    }
                    if (nComparator != 0) {
                        body += "<tr> "
                                + "<td align='center'>Total</td> "
                                + "<td align='right'>" + sa.lib.SLibUtils.DecimalFormatValue0D.format(totalWeight) + "</td> "
                                + "<td align='right'>100%</td> "
                                + "</tr> "
                                + "</table><br> ";
                        weights.clear();
                        suppliersNames.clear();
                        totalCompany += totalWeight;
                        totalWeight = 0;
                        nComparator = 0;
                    }
                }
                else {
                    body += "<b>Boletos de salida: " + (dateStart.equals(dateEnd) ? dateStart : dateRange) + "</b><br><br> ";
                    body += "<table border='1' bordercolor='#000000' style='background-color:' width='400' cellpadding='0' cellspacing='0'> "
                            + "<tr> "
                            + "<th>Producto</th> "
                            + "<th>kg</th> "
                            + "<th>%</th> "
                            + "</tr> ";

                    while (resultSet.next()) {
                        body += "<tr> "
                                + "<td align='left'>" + resultSet.getString("i.name") + "</td> "
                                + "<td align='right'>" + sa.lib.SLibUtils.DecimalFormatValue0D.format(resultSet.getDouble("Peso")) + "</td> "
                                + "<td align='right'>" + decimalFormat.format(resultSet.getDouble("Porcentaje") * 100) + "</td> "
                                + "</tr> ";
                    }
                    resultSet.previous();
                    body += "<tr><td align='right'>Total</td> "
                            + "<td align='right'>" + sa.lib.SLibUtils.DecimalFormatValue0D.format(resultSet.getDouble("SUMA_TOTAL_TOTAL")) + "</td> "
                            + "<td align='right'>100%</td> "
                            + "</tr> "
                            + "</table><br> ";
                    totalCompany += resultSet.getDouble("SUMA_TOTAL_TOTAL");
                }
                canSend = true;
            }
        }
        if (loop > 1) {
            body += "<br><b>Total de todas las empresas: " + (sa.lib.SLibUtils.DecimalFormatValue0D.format(totalCompany)) + " kg</b>";
        }
        SDbConfig config = (SDbConfig) miClient.getSession().getConfigSystem();
        
        SMailSender sender = new SMailSender(
                config.getMailHost(),
                config.getMailPort(),
                config.getMailProtocol(),
                config.isMailStartTls(),
                config.isMailAuth(),
                config.getMailUser(),
                config.getMailPassword(),
                config.getMailUser());

        String AuxSql = "SELECT " + (moKeyES.getSelectedIndex() == 1 ? "mail_to_wm_in " : "mail_to_wm_out ") + "FROM " + SModConsts.TablesMap.get(SModConsts.S_CFG) + ";";

        ResultSet resultSet = miClient.getSession().getStatement().executeQuery(AuxSql);
        try {
            resultSet.next();
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        ArrayList<String> recipients = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(resultSet.getString(1), ";")));
        SMail mail = new SMail(sender, subject, body, recipients);

        mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);

        try {
            if (canSend) {
                mail.send();
                miClient.showMsgBoxInformation(SLibConsts.MSG_PROCESS_FINISHED);
            }
            else {
                miClient.showMsgBoxError("No se encontraron registros para los parametros seleccionados");
            }
        }
        catch (MessagingException ex) {
            SLibUtils.showException(this, ex);
        }
    }
}
