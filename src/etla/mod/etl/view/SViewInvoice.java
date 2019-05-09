/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.view;

import etla.mod.SModConsts;
import etla.mod.etl.db.SEtlConsts;
import java.util.ArrayList;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Sergio Flores, Alfredo Pérez
 */
public class SViewInvoice extends SGridPaneView {

    public static final int SUBTYPE_ALL = 1;
    public static final int SUBTYPE_PEND = 2;

    private SGridFilterDatePeriod moFilterDatePeriod;

    public SViewInvoice(SGuiClient client, int subtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.A_INV, subtype, title);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false, false, false, false, false);

        if (mnGridSubtype == SUBTYPE_ALL) {
            moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        try {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        }
        catch(Exception e) {
            filter = null;
        }

        if (filter != null) {
            sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.fin_dat", (SGuiDate) filter);
        }

        if(mnGridSubtype == SUBTYPE_PEND){
            sql += (sql.isEmpty() ? "" : "AND ") + "v.des_inv_yea_id = 0 AND v.src_inv_id NOT IN (SELECT i.src_inv_id FROM "
                    + SModConsts.TablesMap.get(SModConsts.A_INV) + " AS i WHERE i.des_inv_yea_id <> 0) ";
        }

        msSql = "SELECT "
                + "v.id_inv AS " + SDbConsts.FIELD_ID + "1, "
                + "v.src_inv_id AS " + SDbConsts.FIELD_CODE + ", "
                + "CONCAT(v.fin_ser, '-', v.fin_num) AS " + SDbConsts.FIELD_NAME + ", "
                + "v.src_inv_id, "
                + "v.des_inv_yea_id, "
                + "v.des_inv_doc_id, "
                + "v.ori_num, "
                + "v.fin_ser, "
                + "v.fin_num, "
                + "v.ori_dat, "
                + "v.fin_dat, "
                + "v.pay_acc, "
                + "v.cdt_day, "
                + "v.ori_amt, "
                + "v.fin_amt, "
                + "v.exr, "
                + "v.batch, "
                + "v.pay_cnd, "
                + "v.cus_ord, "
                + "v.bol, "
                + "v.fst_etl_ins, "
                + "v.lst_etl_upd, "
                + "v.fk_lst_etl_log, "
                + "cus.code, "
                + "cus.name, "
                + "cus.tax_id, "
                + "cur_ori.code, "
                + "cur_ori.name, "
                + "cur_fin.code, "
                + "cur_fin.name, "
                + "pay_met.code, "
                + "pay_met.name, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.name AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.name AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.A_INV) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.AU_CUS) + " AS cus ON "
                + "v.fk_src_cus = cus.id_cus "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.AS_CUR) + " AS cur_ori ON "
                + "v.fk_src_ori_cur = cur_ori.id_cur "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.AS_CUR) + " AS cur_fin ON "
                + "v.fk_src_fin_cur = cur_fin.id_cur "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.AS_PAY_MET) + " AS pay_met ON "
                + "v.fk_des_pay_met = pay_met.id_pay_met "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.fin_ser, v.fin_num, v.id_inv ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "v.fin_ser", "Serie " + SEtlConsts.TXT_SYS_SIIE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "v.fin_num", "Folio " + SEtlConsts.TXT_SYS_SIIE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "v.ori_num", "Folio " + SEtlConsts.TXT_SYS_AVISTA));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "v.batch", "Batch"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.ori_dat", "Fecha original"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.fin_dat", "Fecha final"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "v.src_inv_id", "ID " + SEtlConsts.TXT_SYS_AVISTA));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "v.des_inv_yea_id", "ID año " + SEtlConsts.TXT_SYS_SIIE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "v.des_inv_doc_id", "ID doc " + SEtlConsts.TXT_SYS_SIIE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "cus.name", SGridConsts.COL_TITLE_NAME + " " + SEtlConsts.TXT_CUS.toLowerCase()));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "cus.code", SGridConsts.COL_TITLE_CODE + " " + SEtlConsts.TXT_CUS.toLowerCase()));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "cus.tax_id", "RFC"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "v.ori_amt", "Monto original $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "cur_ori.code", "Moneda original"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "v.fin_amt", "Monto final $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "cur_fin.code", "Moneda final"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_EXC_RATE, "v.exr", "Tipo cambio"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "v.cdt_day", "Días crédito"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.pay_acc", "Cuenta pago"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.pay_cnd", "Condiciones pago"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.cus_ord", "Orden compra"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.bol", "Remisión"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.fst_etl_ins", "Primera exportación"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.lst_etl_upd", "Última exportación"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "v.fk_lst_etl_log", "# exportación"));
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
        moSuscriptionsSet.add(SModConsts.AX_ETL);
        moSuscriptionsSet.add(SModConsts.AU_CUS);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}
