/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.view;

import etla.mod.SModConsts;
import etla.mod.SModSysConsts;
import etla.mod.cfg.db.SDbConfig;
import etla.mod.sms.db.SDbConfigSms;
import etla.mod.sms.db.SDbShipment;
import etla.mod.sms.db.SDbShipmentPrinting;
import etla.mod.sms.db.SDbShipper;
import etla.mod.sms.db.SShippingUtils;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.img.SImgConsts;
import sa.lib.img.SImgUtils;

/**
 *
 * @author Daniel López, Claudio Peña, Isabel Servín
 */
public class SViewShipment extends SGridPaneView implements ActionListener{
    
    private final JButton mjPrint;
    private JButton mjSendBack;
    private JButton mjSendNext;
    private JButton mjSendMail;
    private JButton mjSendCpt;
    private final SGridFilterDatePeriod moFilterDatePeriod;
    private final int subType;
    
    public SViewShipment (SGuiClient client, int subtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_SHIPT, subtype, title);
        subType = subtype;        
        mjPrint = SGridUtils.createButton(miClient.getImageIcon(SImgConsts.CMD_STD_PRINT), SUtilConsts.TXT_PRINT, this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjPrint);
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        mjSendBack = SGridUtils.createButton(new ImageIcon(getClass().getResource("/etla/gui/img/icon_std_move_left.gif")), "Regresar", this);
        mjSendNext = SGridUtils.createButton(new ImageIcon(getClass().getResource("/etla/gui/img/icon_std_move_right.gif")), "Adelantar", this);
        mjSendMail = SGridUtils.createButton(new ImageIcon(getClass().getResource("/etla/gui/img/icon_std_mail.gif")), "Enviar la información del embarque al transportista", this);
        mjSendMail.setEnabled(true);
        mjSendCpt = SGridUtils.createButton(new ImageIcon(getClass().getResource("/etla/gui/img/icon_std_mail.gif")), "Enviar la información del embarque al CPT", this);
        mjSendCpt.setEnabled(true);
        
        switch (subType) { 
            case SLibConsts.UNDEFINED:  // for new shipments
                setRowButtonsEnabled(true, false, true, true, true);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
                mjSendBack.setEnabled(false);
                mjSendNext.setEnabled(false);
                break;
            case SModSysConsts.SS_SHIPT_ST_REL_TO:  // for release of shipments
                setRowButtonsEnabled(false);
                mjSendBack.setEnabled(false);
                mjSendNext.setEnabled(true);
                break;
            case SModSysConsts.SS_SHIPT_ST_REL:     // for released shipments
                setRowButtonsEnabled(false);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
                mjSendBack.setEnabled(true);
                mjSendNext.setEnabled(false);
                break;
            default:
                break;
        }
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjSendBack);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjSendNext);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjSendMail);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjSendCpt);
    }
    
    private int getNewStatus() {
        int newStatus = SLibConsts.UNDEFINED;
        
        switch (subType) {
            case SModSysConsts.SS_SHIPT_ST_REL_TO:
                newStatus = SModSysConsts.SS_SHIPT_ST_REL;
                break;
            case SModSysConsts.SS_SHIPT_ST_REL:
                newStatus = SModSysConsts.SS_SHIPT_ST_REL_TO;
                break;
            default:
        }
        
        return newStatus;
    }
    
    private void changeStatus() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else {
                try {
                    SShippingUtils.changeStatus(miClient.getSession(), gridRow.getRowPrimaryKey(), getNewStatus());
                    miClient.getSession().notifySuscriptors(SModConsts.S_SHIPT);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionPerformedPrint() throws Exception {
        if (mjPrint.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {

                    try {
                        miClient.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
                        
                        SDbShipment shipment = (SDbShipment) miClient.getSession().readRegistry(SModConsts.S_SHIPT, getSelectedGridRow().getRowPrimaryKey());
                        
                        // preserve number of this shipment printings:
                        
                        SDbShipmentPrinting printing = new SDbShipmentPrinting();
                        printing.setPkShipmentId(shipment.getPkShipmentId());
                        printing.save(miClient.getSession());

                        populateGrid(SGridConsts.REFRESH_MODE_RELOAD); // to update number of printings
                        
                        // prepare printing:
                        
                        HashMap<String, Object> map = miClient.createReportParams();
                        
                        map.put("nShiptId", shipment.getPkShipmentId());
                        map.put("bReleased", shipment.getFkShipmentStatusId() >= SModSysConsts.SS_SHIPT_ST_REL);
                        
                        // create QR for shipment order and server address:
                        
                        SDbConfigSms configSms = ((SDbConfig) miClient.getSession().getConfigSystem()).getDbConfigSms();
                        BufferedImage imageQr = SImgUtils.createQrCodeBufferedImageCfdi33(configSms.getUrlSms() + "/url.php?key=" + shipment.getWebKey(), 400, 400);

                        if (imageQr != null) {
                            map.put("oImageQr", imageQr.getScaledInstance(imageQr.getWidth(), imageQr.getHeight(), Image.SCALE_DEFAULT));
                        }

                        // print:
                        
                        miClient.getSession().printReport(SModConsts.SR_SHIPT, SLibConsts.UNDEFINED, null, map);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                    finally {
                        miClient.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            }
        }
    }
    
    private void actionPerformedSendNext() {
        if (mjSendNext.isEnabled()) {
            changeStatus();
        }
    }
    
    private void actionPerformedSendBack () {
        if (mjSendBack.isEnabled()) {
            changeStatus();
        }
    }
    
    private void actionPerformedSendMail() {
        if (mjSendMail.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    try {
                        SDbShipment shipt = (SDbShipment) miClient.getSession().readRegistry(SModConsts.S_SHIPT, gridRow.getRowPrimaryKey());
                        SDbShipper shipper = (SDbShipper) miClient.getSession().readRegistry(SModConsts.SU_SHIPPER, new int[] { shipt.getFkShipperId() });
                        String mail = shipper.getMail().toLowerCase();
                        if (miClient.showMsgBoxConfirm("Se enviará un correo con la información del embarque al transportista:\n"
                                + "'" + shipper.getName() + "', al buzón '" + mail + "'.\n"
                                + "Favor de confirmar el envío.") == JOptionPane.OK_OPTION) {
                            shipt.setAuxSendMail(true);
                            shipt.collectShipmentInfo(miClient.getSession());
                            shipt.sendMail(miClient.getSession());
                        }
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        } 
    }
    
    private void actionPerformedSendCpt() {
        if (mjSendMail.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    try {
                        SDbShipment shipt = (SDbShipment) miClient.getSession().readRegistry(SModConsts.S_SHIPT, gridRow.getRowPrimaryKey());
                        if (miClient.showMsgBoxConfirm("Se enviará la información del embarque a la aplicación CPT ¿Desea continuar?") == JOptionPane.OK_OPTION) {
                            shipt.collectShipmentInfo(miClient.getSession());
                            shipt.sendJson(miClient.getSession());
                        }
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        
        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "sh.b_del = 0 ";
        }
        
        if (subType == SLibConsts.UNDEFINED) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("sh.shipt_date", (SGuiDate) filter);
        }
        else if (subType == SModSysConsts.SS_SHIPT_ST_REL_TO) {
            sql += (sql.isEmpty() ? "" : "AND ") + "sh.fk_shipt_st = " + SModSysConsts.SS_SHIPT_ST_REL_TO + " ";
        }
        else if (subType == SModSysConsts.SS_SHIPT_ST_REL) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("sh.shipt_date", (SGuiDate) filter);
            sql += (sql.isEmpty() ? "" : "AND ") + "sh.fk_shipt_st = " + SModSysConsts.SS_SHIPT_ST_REL + " ";
        }
        
        msSql = "SELECT "
                + "sp.name, "
                + "vh.name, "
                + "sh.vehic_plate, "
                + "sh.driver_name, "
                + "sh.driver_phone, "
                + "sh.id_shipt AS " + SDbConsts.FIELD_ID + "1, "
                + "sh.number AS " + SDbConsts.FIELD_CODE + ", "
                + "sh.number AS " + SDbConsts.FIELD_NAME + ", "
                + "sh.shipt_date AS " + SDbConsts.FIELD_DATE + ", "
                + "sh.b_ann, "
                + "sh.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "sh.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "sh.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "sh.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "sh.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "sh.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "sh.fk_usr_release, "
                + "sh.ts_usr_release, "
                + "sh.kg, "
                + "sh.ticket_id, "
                + "sht.name, "
                + "shs.name, "
                + "shc.name, "
                + "shh.name, "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
                + "ur.name, "
                + "tk.weight, "
                + "sh.kg - tk.weight AS _weight_diff, "
                + "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.S_SHIPT_PRT) +  " AS prt WHERE prt.id_shipt = sh.id_shipt) AS _printings "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_SHIPT) + " AS sh "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SHIPT_TP) + " AS sht ON "
                + "sh.fk_shipt_tp = sht.id_shipt_tp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SS_SHIPT_ST) + " AS shs ON "
                + "sh.fk_shipt_st = shs.id_shipt_st "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_CARGO_TP) + " AS shc ON "
                + "sh.fk_cargo_tp = shc.id_cargo_tp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_HANDG_TP) + " AS shh ON "
                + "sh.fk_handg_tp = shh.id_handg_tp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_SHIPPER) + " AS sp ON "
                + "sp.id_shipper = sh.fk_shipper "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.SU_VEHIC_TP) + " AS vh ON "
                + "vh.id_vehic_tp = sh.fk_vehic_tp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "sh.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "sh.fk_usr_upd = uu.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ur ON "
                + "sh.fk_usr_release = ur.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_SHIPT_ROW) + " AS sr ON "
                + "sh.id_shipt = sr.id_shipt "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.S_WM_TICKET) + " AS tk ON "
                + "sh.ticket_id = tk.ticket_id "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "GROUP BY sh.number, sh.id_shipt "
                + "ORDER BY sh.number, sh.id_shipt ";
    }
    
    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();
      
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, SDbConsts.FIELD_CODE, "Folio"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "sht.name", "Tipo embarque"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "shc.name", "Tipo carga"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "shh.name", "Tipo maniobra"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "sp.name", "Línea transportista"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vh.name", "Tipo vehículo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "sh.vehic_plate", "Placas vehículo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "sh.driver_name", "Nombre chofer"));        
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "sh.driver_phone", "Teléfono chofer"));        
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "shs.name", "Estatus embarque"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "sh.ticket_id", "ID boleto Revuelta"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_8B, "tk.weight", "Peso Revuelta (kg)"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_8B, "sh.kg", "Peso Axiom (kg)"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_8B, "_weight_diff", "Diferencia peso (kg)"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "_printings", "Impresiones"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "sh.b_ann", "Anulado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "ur.name", "Usr Libera"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "sh.ts_usr_release", "Usr TS Libera"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return columns;
    }
    
    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CU_USR);
        moSuscriptionsSet.add(SModConsts.S_SHIPT);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() instanceof JButton) {
            JButton button = (JButton) evt.getSource();
            
            if (button == mjPrint) {
                try {
                    actionPerformedPrint();
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
            else if (button == mjSendNext) {
                actionPerformedSendNext();
            }
            else if (button == mjSendBack) {
                actionPerformedSendBack();
            }
            else if (button == mjSendMail) {
                actionPerformedSendMail();
            }
            else if (button == mjSendCpt) {
                actionPerformedSendCpt();
            }
        }
    }
}
