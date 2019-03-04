/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package etla.mod;

import etla.mod.cfg.db.SDbConfig;
import etla.mod.cfg.db.SDbUser;
import etla.mod.cfg.db.SDbUserGui;
import etla.mod.cfg.form.SFormUser;
import etla.mod.cfg.view.SViewUser;
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
public class SModModuleCfg extends SGuiModule {
    
    private SFormUser moFormUser;
    
    public SModModuleCfg(SGuiClient client) {
        super(client, SModConsts.MOD_CFG, SLibConsts.UNDEFINED);
    }

    @Override
    public JMenu[] getMenus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SDbRegistry getRegistry(final int type, final SGuiParams params) {
        SDbRegistry registry = null;

        switch (type) {
            case SModConsts.CS_USR_TP:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_usr_tp = " + pk[0] + " "; }
                };
                break;
            case SModConsts.CU_USR:
                registry = new SDbUser();
                break;
            case SModConsts.C_CFG:
                registry = new SDbConfig();
                break;
            case SModConsts.C_USR_GUI:
                registry = new SDbUserGui();
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
            case SModConsts.CS_USR_TP:
                settings = new SGuiCatalogueSettings("Tipo usuario", 1);
                sql = "SELECT id_usr_tp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case SModConsts.CU_USR:
                settings = new SGuiCatalogueSettings("Usuario", 1);
                sql = "SELECT id_usr AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE NOT b_del AND NOT b_web ORDER BY name, id_usr ";
                break;
            case SModConsts.C_CFG:
            case SModConsts.C_USR_GUI:
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
            case SModConsts.CS_USR_TP:
                break;
            case SModConsts.CU_USR:
                view = new SViewUser(miClient, "Usuarios");
                break;
            case SModConsts.C_CFG:
            case SModConsts.C_USR_GUI:
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return view;
    }

    @Override
    public SGuiOptionPicker getOptionPicker(int type, int subtype, SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiForm getForm(int type, int subtype, SGuiParams params) {
        SGuiForm form = null;

        switch (type) {
            case SModConsts.CS_USR_TP:
                break;
            case SModConsts.CU_USR:
                if (moFormUser == null) moFormUser = new SFormUser(miClient, "Usuario");
                form = moFormUser;
                break;
            case SModConsts.C_CFG:
            case SModConsts.C_USR_GUI:
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return form;
    }

    @Override
    public SGuiReport getReport(int type, int subtype, SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
