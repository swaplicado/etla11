/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.view;

import etla.mod.SModConsts;
import etla.mod.SModSysConsts;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibTimeUtils;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateRange;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Isabel Servín
 */
public class SViewShipmentByPeriod extends SGridPaneView {
    
    private SGridFilterDateRange moFilterDateRange;
    
    public SViewShipmentByPeriod (SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.S_SHIPT, SModSysConsts.SS_SHIPT_BY_PERIOD, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        
        moFilterDateRange = new SGridFilterDateRange(miClient, this);
        moFilterDateRange.initFilter(new Date[] { SLibTimeUtils.getBeginOfMonth(miClient.getSession().getCurrentDate()), SLibTimeUtils.getEndOfMonth(miClient.getSession().getCurrentDate()) }); 
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateRange);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(0);
        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDeletableApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        
        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "s.b_del = 0 ";
        }
        
        filter = (Date[]) moFiltersMap.get(SGridConsts.FILTER_DATE_RANGE).getValue();
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDateRange("s.shipt_date", (Date[]) filter);
        
        msSql = "SELECT " 
                + "s.number, "
                + "sr.shipt_folio AS f_code, " 
                + "sr.delivery_date, " 
                + "sr.bol_id, " 
                + "c.name AS f_name, " 
                + "sh.name AS transportista, " 
                + "s.driver_name, " 
                + "s.vehic_plate, " 
                + "s.driver_phone, " 
                + "sr.m2, " 
                + "f.name AS montacarguista, " 
                + "s.shipt_date, " 
                + "vt.name AS tipo_vehiculo, " 
                + "state.sta AS estado, " 
                + "county.description AS municipio, " 
                + "crew.name AS cuadrilla " 
                + "FROM s_shipt_row AS sr " 
                + "INNER JOIN s_shipt AS s ON sr.id_shipt = s.id_shipt " 
                + "INNER JOIN au_cus AS c ON sr.fk_customer = c.id_cus " 
                + "INNER JOIN su_destin AS d ON sr.fk_destin = d.id_destin " 
                + "INNER JOIN su_shipper AS sh ON s.fk_shipper = sh.id_shipper " 
                + "INNER JOIN su_vehic_tp AS vt ON s.fk_vehic_tp = vt.id_vehic_tp " 
                + "LEFT JOIN su_forklift_drv AS f ON s.fk_forklift_drv = f.id_forklift_drv " 
                + "LEFT JOIN su_crew AS crew ON s.fk_crew = crew.id_crew " 
                + "LEFT JOIN erp.locs_bol_zip_code AS z ON d.zip_code = z.id_zip_code "  
                + "LEFT JOIN erp.locu_sta AS state ON z.id_sta_code = state.sta_code " 
                + "LEFT JOIN erp.locs_bol_county AS county ON z.county_code = county.id_county_code AND z.id_sta_code = county.id_sta_code "
                + (sql.isEmpty() ? "" : "WHERE " + sql);
    }
    
    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "s.number", "Embarque"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "s.shipt_date", "Fecha embarque"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_code", "Orden embarque"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sr.bol_id", "Remisión"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "sr.delivery_date", "Fecha remisión"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "f_name", "Cliente"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "transportista", "Línea transportista"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tipo_vehiculo", "Tipo de unidad"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "s.vehic_plate", "Placas"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "s.driver_name", "Nombre chofer"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "s.driver_phone", "Teléfono chofer"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "sr.m2", "m²"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "montacarguista", "Montacarguista"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "cuadrilla", "Cuadrilla"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "estado", "Estado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "municipio", "Municipio"));

        return columns;
    }
    
    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.S_SHIPT);
        moSuscriptionsSet.add(SModConsts.S_SHIPT_ROW);
        moSuscriptionsSet.add(SModConsts.SU_DESTIN);
        moSuscriptionsSet.add(SModConsts.SU_SHIPPER);
        moSuscriptionsSet.add(SModConsts.SU_VEHIC_TP);
        moSuscriptionsSet.add(SModConsts.SU_FORKLIFT_DRV);
        moSuscriptionsSet.add(SModConsts.SU_CREW);
    }
}
