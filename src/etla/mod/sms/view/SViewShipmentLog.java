/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.view;

import etla.mod.SModConsts;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Isabel Servín
 */
public class SViewShipmentLog extends SGridPaneView {
    
    private final SGridFilterDatePeriod moFilterDatePeriod;
    
    public SViewShipmentLog (SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_SHIPT_LOG, SLibConsts.UNDEFINED, title);
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("s.shipt_date", (SGuiDate) filter);
        
        msSql = "SELECT sl.id_shipt_log AS " + SDbConsts.FIELD_ID + "1, "
                + "s.number AS " + SDbConsts.FIELD_NAME + ", "
                + "s.shipt_date, "
                + "IF(sl.file_tp = 1, 'MAIL', 'CPT') AS enviado_via, "
                + "sl.mail, "
                + "sl.parameters, "
                + "sl.code AS " + SDbConsts.FIELD_CODE + ", "
                + "sl.message, "
                + "sl.response, "
                + "u.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", " 
                + "sl.ts_usr AS " + SDbConsts.FIELD_USER_INS_TS + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_SHIPT_LOG) + " AS sl "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.S_SHIPT) + " AS s ON sl.fk_shipt = s.id_shipt "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS u ON sl.fk_usr = u.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY s.number, s.shipt_date;";
    }
    
    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<SGridColumnView>();

//        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, SDbConsts.FIELD_NAME, "Folio"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "s.shipt_date", "Fecha"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "enviado_via", "Enviado vía"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "sl.mail", "Mail"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "sl.parameters", "Parametros"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE + " CPT"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "sl.message", "Descripción código CPT"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "sl.response", "Respuesta CPT"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, "Usuario"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, "Fecha-hora envío"));
        
        return columns;
    }
    
    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.S_SHIPT_LOG);
        moSuscriptionsSet.add(SModConsts.S_SHIPT);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}
