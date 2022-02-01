/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod;

import etla.mod.etl.view.SViewAvistaCustomerInvoicesPending;
import etla.mod.sms.db.SDbCargoType;
import etla.mod.sms.db.SDbComment;
import etla.mod.sms.db.SDbConfigSms;
import etla.mod.sms.db.SDbDestination;
import etla.mod.sms.db.SDbErpDoc;
import etla.mod.sms.db.SDbErpDocEtlLog;
import etla.mod.sms.db.SDbHandlingType;
import etla.mod.sms.db.SDbShipment;
import etla.mod.sms.db.SDbShipmentRow;
import etla.mod.sms.db.SDbShipmentType;
import etla.mod.sms.db.SDbShipper;
import etla.mod.sms.db.SDbVehicleType;
import etla.mod.sms.db.SDbWmItem;
import etla.mod.sms.db.SDbWmTicket;
import etla.mod.sms.db.SDbWmUser;
import etla.mod.sms.form.SFormShipment;
import etla.mod.sms.form.SFormShipper;
import etla.mod.sms.view.SViewShipment;
import etla.mod.sms.view.SViewShipper;
import javax.swing.JMenu;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistrySysFly;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiCatalogueSettings;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiForm;
import sa.lib.gui.SGuiModule;
import sa.lib.gui.SGuiOptionPicker;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiReport;

/**
 *
 * @author Daniel López, Alfredo Pérez, Sergio Flores, Isabel Servín
 */
public class SModModuleSms extends SGuiModule {

    private SFormShipper moFormShipper;
    private SFormShipment moFormShipment;

    public SModModuleSms(SGuiClient client) {
        super(client, SModConsts.MOD_SMS, SLibConsts.UNDEFINED);
    }

    @Override
    public JMenu[] getMenus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SDbRegistry getRegistry(int type, SGuiParams params) {
        SDbRegistry registry = null;

        switch (type) {
            case SModConsts.SS_SHIPT_ST:
                registry = new SDbRegistrySysFly(type) {
                    @Override
                    public void initRegistry() { }
                    @Override
                    public String getSqlTable() { return SModConsts.TablesMap.get(type); }
                    @Override
                    public String getSqlWhere(int[] key) { return "WHERE id_shipt_st = " + key[0] + " "; }
                };
                break;
            case SModConsts.SS_WEB_ROLE:
                registry = new SDbRegistrySysFly(type) {
                    @Override
                    public void initRegistry() { }
                    @Override
                    public String getSqlTable() { return SModConsts.TablesMap.get(type); }
                    @Override
                    public String getSqlWhere(int[] key) { return "WHERE id_web_role = " + key[0] + " "; }
                };
                break;
            case SModConsts.SU_SHIPT_TP:
                registry = new SDbShipmentType();
                break;
            case SModConsts.SU_CARGO_TP:
                registry = new SDbCargoType();
                break;
            case SModConsts.SU_HANDG_TP:
                registry = new SDbHandlingType();
                break;
            case SModConsts.SU_VEHIC_TP:
                registry = new SDbVehicleType();
                break;
            case SModConsts.SU_COMMENT:
                registry = new SDbComment();
                break;
            case SModConsts.SU_SHIPPER:
                registry = new SDbShipper();
                break;
            case SModConsts.SU_DESTIN:
                registry = new SDbDestination();
                break;
            case SModConsts.SU_WM_ITEM:
                registry = new SDbWmItem();
                break;
            case SModConsts.SU_WM_USER:
                registry = new SDbWmUser();
                break;
            case SModConsts.S_CFG:
                registry = new SDbConfigSms();
                break;
            case SModConsts.S_SHIPT:
                registry = new SDbShipment();
                break;
            case SModConsts.S_SHIPT_ROW:
                registry = new SDbShipmentRow();
                break;
            case SModConsts.S_EVIDENCE:
                // Not supported yet!
                break;
            case SModConsts.S_ERP_DOC:
                registry = new SDbErpDoc();
                break;
            case SModConsts.S_ERP_DOC_ETL_LOG:
                registry = new SDbErpDocEtlLog();
                break;
            case SModConsts.S_WM_TICKET:
                registry = new SDbWmTicket();
                break;
            case SModConsts.S_WM_TICKET_LINK:
                // Not supported yet!
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

         return registry;
    }

    @Override
    public SGuiCatalogueSettings getCatalogueSettings(int type, int subtype, SGuiParams params) {
        String sql = "";
        SGuiCatalogueSettings settings = null;

        switch(type) {
            case SModConsts.SS_SHIPT_ST:
            case SModConsts.SS_WEB_ROLE:
                break;
            case SModConsts.SU_SHIPT_TP:
                settings = new SGuiCatalogueSettings("Tipo embarque", 1);
                sql = "SELECT id_shipt_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "                        
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY id_shipt_tp ";
                break;
            case SModConsts.SU_CARGO_TP:
                settings = new SGuiCatalogueSettings("Tipo carga", 1);
                sql = "SELECT id_cargo_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "                        
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY id_cargo_tp ";
                break;
            case SModConsts.SU_HANDG_TP:
                settings = new SGuiCatalogueSettings("Tipo maniobra", 1);
                sql = "SELECT id_handg_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "                        
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY id_handg_tp ";
                break;
            case SModConsts.SU_VEHIC_TP:
                settings = new SGuiCatalogueSettings("Tipo vehículo", 1);
                sql = "SELECT id_vehic_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "                        
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY id_vehic_tp ";
                break;
            case SModConsts.SU_COMMENT:
                settings = new SGuiCatalogueSettings("Observaciones", 1);
                sql = "SELECT id_comment AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "                        
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY id_comment ";
                break;
            case SModConsts.SU_SHIPPER:
                settings = new SGuiCatalogueSettings("Transportista", 1);
                switch(subtype) {
                    case SModSysConsts.S_CFG_SHIPPER_CODE_NAME:
                        sql = "SELECT id_shipper AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + " "                        
                                + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_shipper ";
                        break;
                    case SModSysConsts.S_CFG_SHIPPER_NAME_CODE:
                        sql = "SELECT id_shipper AS " + SDbConsts.FIELD_ID + "1, CONCAT(name, ' - ', code) AS " + SDbConsts.FIELD_ITEM + " "                        
                                + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_shipper ";
                        break;
                    default:
                        sql = "SELECT id_shipper AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "                        
                                + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_shipper ";
                        break;
                }
                break;
            case SModConsts.SU_DESTIN:
                settings = new SGuiCatalogueSettings("Destino", 1);
                sql = "SELECT id_destin AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "                        
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_destin ";
                break;
            case SModConsts.SU_WM_USER:
                settings = new SGuiCatalogueSettings("Usuarios Bascula", 1);
                sql = "SELECT id_wm_user AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_wm_user ";
                break;
            case SModConsts.SS_WM_TICKET_TP:
                settings = new SGuiCatalogueSettings("E/S", 1);
                sql = "SELECT id_wm_ticket_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_wm_ticket_tp ";
                break;
            case SModConsts.S_SHIPT:
            case SModConsts.S_SHIPT_ROW:
            case SModConsts.S_EVIDENCE:
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        if (settings != null) {
            settings.setSql(sql);
        }

        return settings;
    }

    @Override
    public SGridPaneView getView(int type, int subtype, SGuiParams params) {
        SGridPaneView view = null;

        switch (type) {
            case SModConsts.AX_CUST_INV_PEND:
                view = new SViewAvistaCustomerInvoicesPending(miClient, "Remisiones pendientes");
                break;
            case SModConsts.S_SHIPT:
                switch (subtype) {
                    case SLibConsts.UNDEFINED:
                        view = new SViewShipment(miClient, subtype, "Embarques");
                        break;
                    case SModSysConsts.SS_SHIPT_ST_REL_TO:
                        view = new SViewShipment(miClient, subtype, "Embarques por liberar");
                        break;
                    case SModSysConsts.SS_SHIPT_ST_REL:
                        view = new SViewShipment(miClient, subtype, "Embarques liberados");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.SU_SHIPPER:
                view = new SViewShipper(miClient, "Transportistas");
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return view;
    }

    @Override
    public SGuiOptionPicker getOptionPicker(int type, int subtype, SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiForm getForm(int type, int subtype, SGuiParams params) {
        SGuiForm form = null;

        switch (type) {
            case SModConsts.S_SHIPT:
                if (moFormShipment == null) moFormShipment = new SFormShipment(miClient, "Embarques");
                form = moFormShipment;
                break;
            case SModConsts.SU_SHIPPER:
                if (moFormShipper == null) moFormShipper = new SFormShipper(miClient, "Transportista");
                form = moFormShipper;
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return form;
    }

    @Override
    public SGuiReport getReport(int type, int subtype, SGuiParams params) {
        SGuiReport report = null;

        switch (type){
            case SModConsts.SR_SHIPT:
                report = new SGuiReport("reps/shipt.jasper", "Orden de embarque");
                break;
            case SModConsts.SR_WEIGHT_COMPAR:
                report = new SGuiReport("reps/weight_compar.jasper", "Comparativo peso embarques vs. báscula");
                break;
            case SModConsts.SR_SHIPT_CAPACITY:
                report = new SGuiReport("reps/shipt_capacity.jasper", "Capacidad utilizada en m² por embarque");
                break;
            case SModConsts.SR_SHIPT_TRIPS:
                report = new SGuiReport("reps/shipt_trips.jasper", "Número de viajes por cliente");
                break;
            default:
        }

        return report;
    }
}
