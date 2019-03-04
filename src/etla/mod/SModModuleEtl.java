/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod;

import etla.mod.etl.db.SDbConfigAvista;
import etla.mod.etl.db.SDbCustomer;
import etla.mod.etl.db.SDbExchangeRate;
import etla.mod.etl.db.SDbInvoice;
import etla.mod.etl.db.SDbInvoiceRow;
import etla.mod.etl.db.SDbItem;
import etla.mod.etl.db.SDbSalesAgent;
import etla.mod.etl.db.SDbSysCurrency;
import etla.mod.etl.db.SDbSysUnitOfMeasure;
import etla.mod.etl.form.SFormCustomer;
import etla.mod.etl.form.SFormExchangeRate;
import etla.mod.etl.form.SFormItem;
import etla.mod.etl.form.SFormSalesAgent;
import etla.mod.etl.view.SViewCustomer;
import etla.mod.etl.view.SViewExchangeRate;
import etla.mod.etl.view.SViewInvoice;
import etla.mod.etl.view.SViewItem;
import etla.mod.etl.view.SViewSalesAgent;
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
 * @author Sergio Flores
 */
public class SModModuleEtl extends SGuiModule {
    
    private SFormSalesAgent moFormSalesAgent;
    private SFormCustomer moFormCustomer;
    private SFormItem moFormItem;
    private SFormExchangeRate moFormExchangeRate;
    
    public SModModuleEtl(SGuiClient client) {
        super(client, SModConsts.MOD_ETL, SLibConsts.UNDEFINED);
    }

    @Override
    public JMenu[] getMenus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SDbRegistry getRegistry(int type, SGuiParams params) {
        SDbRegistry registry = null;

        switch (type) {
            case SModConsts.AS_CUR:
                registry = new SDbSysCurrency();
                break;
            case SModConsts.AS_UOM:
                registry = new SDbSysUnitOfMeasure();
                break;
            case SModConsts.AS_PAY_MET:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_pay_met = " + pk[0] + " "; }
                };
                break;
            case SModConsts.AU_SAL_AGT:
                registry = new SDbSalesAgent();
                break;
            case SModConsts.AU_CUS:
                registry = new SDbCustomer();
                break;
            case SModConsts.AU_ITM:
                registry = new SDbItem();
                break;
            case SModConsts.A_CFG:
                registry = new SDbConfigAvista();
                break;
            case SModConsts.A_INV:
                registry = new SDbInvoice();
                break;
            case SModConsts.A_INV_ROW:
                registry = new SDbInvoiceRow();
                break;
            case SModConsts.A_EXR:
                registry = new SDbExchangeRate();
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

        switch (type) {
            case SModConsts.AS_CUR:
                settings = new SGuiCatalogueSettings("Moneda", 1, 0, SLibConsts.DATA_TYPE_INT);
                sql = "SELECT id_cur AS " + SDbConsts.FIELD_ID + "1, CONCAT(name, ' (', code, ')') AS " + SDbConsts.FIELD_ITEM + ", "
                        + "src_cur_id AS " + SDbConsts.FIELD_COMP + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case SModConsts.AS_UOM:
                settings = new SGuiCatalogueSettings("Unidad", 1, 0, SLibConsts.DATA_TYPE_TEXT);
                sql = "SELECT id_uom AS " + SDbConsts.FIELD_ID + "1, CONCAT(name, ' (', code, ')') AS " + SDbConsts.FIELD_ITEM + ", "
                        + "src_uom_id AS " + SDbConsts.FIELD_COMP + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case SModConsts.AS_PAY_MET:
                settings = new SGuiCatalogueSettings("Método pago", 1, 0, SLibConsts.DATA_TYPE_INT);
                sql = "SELECT id_pay_met AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + ", "
                        + "id_pay_met AS " + SDbConsts.FIELD_COMP + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case SModConsts.AU_SAL_AGT:
                settings = new SGuiCatalogueSettings("Agente ventas", 1, 0, SLibConsts.DATA_TYPE_INT);
                sql = "SELECT id_sal_agt AS " + SDbConsts.FIELD_ID + "1, CONCAT(name, ' (', code, ')') AS " + SDbConsts.FIELD_ITEM + ", "
                        + "src_sal_agt_id AS " + SDbConsts.FIELD_COMP + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, code, id_sal_agt ";
                break;
            case SModConsts.AU_CUS:
                settings = new SGuiCatalogueSettings("Cliente", 1, 0, SLibConsts.DATA_TYPE_INT);
                sql = "SELECT id_cus AS " + SDbConsts.FIELD_ID + "1, CONCAT(name, ' (', code, ')') AS " + SDbConsts.FIELD_ITEM + ", "
                        + "src_cus_id AS " + SDbConsts.FIELD_COMP + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, code, id_cus ";
                break;
            case SModConsts.AU_ITM:
                break;
            case SModConsts.A_CFG:
                break;
            case SModConsts.A_INV:
                break;
            case SModConsts.A_INV_ROW:
                break;
            case SModConsts.A_EXR:
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
            case SModConsts.AS_CUR:
            case SModConsts.AS_UOM:
            case SModConsts.AS_PAY_MET:
                break;
            case SModConsts.AU_SAL_AGT:
                view = new SViewSalesAgent(miClient, "Agentes ventas");
                break;
            case SModConsts.AU_CUS:
                view = new SViewCustomer(miClient, "Clientes");
                break;
            case SModConsts.AU_ITM:
                view = new SViewItem(miClient, "Ítems");
                break;
            case SModConsts.A_CFG:
                break;
            case SModConsts.A_INV:
                view = new SViewInvoice(miClient, "Facturas");
                break;
            case SModConsts.A_INV_ROW:
                break;
            case SModConsts.A_EXR:
                view = new SViewExchangeRate(miClient, "Tipos cambio");
                break;
            /*
            case SModConsts.RS_DB_TP:
                break;
            case SModConsts.RS_VER:
                view = new SViewVersion(miClient, "Versiones SIIE");
                break;
            case SModConsts.RU_INS:
                view = new SViewInstance(miClient, "Instancias SIIE");
                break;
            case SModConsts.RU_COM:
                view = new SViewCompany(miClient, "Empresas instancias SIIE");
                break;
            case SModConsts.RU_GRP:
                view = new SViewGroup(miClient, "Grupos consolidación");
                break;
            case SModConsts.RU_GRP_COM:
                view = new SViewGroupCompany(miClient, "Empresas grupos consolidación");
                break;
            */
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
            case SModConsts.AS_CUR:
            case SModConsts.AS_UOM:
            case SModConsts.AS_PAY_MET:
                break;
            case SModConsts.AU_SAL_AGT:
                if (moFormSalesAgent == null) moFormSalesAgent = new SFormSalesAgent(miClient, "Agente ventas");
                form = moFormSalesAgent;
                break;
            case SModConsts.AU_CUS:
                if (moFormCustomer == null) moFormCustomer = new SFormCustomer(miClient, "Cliente");
                form = moFormCustomer;
                break;
            case SModConsts.AU_ITM:
                if (moFormItem == null) moFormItem = new SFormItem(miClient, "Ítem");
                form = moFormItem;
                break;
            case SModConsts.A_CFG:
                break;
            case SModConsts.A_INV:
                break;
            case SModConsts.A_INV_ROW:
                break;
            case SModConsts.A_EXR:
                if (moFormExchangeRate == null) moFormExchangeRate = new SFormExchangeRate(miClient, "Tipo cambio");
                form = moFormExchangeRate;
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return form;
    }

    @Override
    public SGuiReport getReport(int type, int subtype, SGuiParams params) {
        SGuiReport report = null;
        
        switch (type) {
            /*
            case SModConsts.RR_FIN_STA_BS:
                report = new SGuiReport("reps/fin_sta_bs.jasper", SLibUtils.textProperCase(SRepConsts.TXT_FIN_STA_BS));
                break;
            case SModConsts.RR_FIN_STA_PLS:
                report = new SGuiReport("reps/fin_sta_pls.jasper", SLibUtils.textProperCase(SRepConsts.TXT_FIN_STA_PLS));
                break;
            case SModConsts.RR_FIN_RAT:
                report = new SGuiReport("reps/fin_rat.jasper", SLibUtils.textProperCase(SRepConsts.TXT_FIN_STA_PLS));
                break;
            */
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return report;
    }
}
