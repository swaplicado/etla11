/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.view;

import etla.mod.SModConsts;
import etla.mod.etl.db.SEtlConsts;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Sergio Flores
 */
public class SViewCustomer extends SGridPaneView {
    
    public SViewCustomer(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.AU_CUS, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(false, false, true, false, false);
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

        msSql = "SELECT "
                + "v.id_cus AS " + SDbConsts.FIELD_ID + "1, "
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "v.src_cus_id, "
                + "v.des_cus_id, "
                + "v.des_cus_bra_id, "
                + "v.tax_id, "
                + "TRIM(CONCAT(v.str, ' ', v.num_ext, ' ', v.num_int)) AS f_add1, "
                + "TRIM(CONCAT(v.nei, ' ', v.ref)) AS f_add2, "
                + "TRIM(CONCAT(v.zip, ' ', v.loc, ' ', v.cou, ' ', v.sta, ' ', v.cty)) AS f_add3, "
                + "v.pay_acc, "
                + "v.cdt_day, "
                + "v.cdt_lim, "
                + "v.src_cus_cur_fk_n, "
                + "v.src_cus_uom_fk_n, "
                + "v.src_cus_sal_agt_fk_n, "
                + "v.src_req_cur_fk_n, "
                + "v.src_req_uom_fk_n, "
                + "v.fst_etl_ins, "
                + "v.lst_etl_upd, "
                + "v.b_etl_ign, "
                + "v.fk_src_cus_cur_n, "
                + "v.fk_src_cus_uom_n, "
                + "v.fk_src_cus_sal_agt_n, "
                + "v.fk_src_req_cur_n, "
                + "v.fk_src_req_uom_n, "
                + "v.fk_des_req_pay_met_n, "
                + "v.fk_lst_etl_log, "
                + "sal_agt.code, "
                + "sal_agt.name, "
                + "cur.code, "
                + "cur.name, "
                + "uom.code, "
                + "uom.name, "
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
                + "FROM " + SModConsts.TablesMap.get(SModConsts.AU_CUS) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.AU_SAL_AGT) + " AS sal_agt ON "
                + "v.fk_src_cus_sal_agt_n = sal_agt.id_sal_agt "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.AS_CUR) + " AS cur ON "
                + "v.fk_src_req_cur_n = cur.id_cur "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.AS_UOM) + " AS uom ON "
                + "v.fk_src_req_uom_n = uom.id_uom "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.AS_PAY_MET) + " AS pay_met ON "
                + "v.fk_des_req_pay_met_n = pay_met.id_pay_met "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.name, v.id_cus ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "v.src_cus_id", "ID " + SEtlConsts.TXT_SYS_AVISTA));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "v.des_cus_id", "ID asoc negocios " + SEtlConsts.TXT_SYS_SIIE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "v.des_cus_bra_id", "ID suc matriz " + SEtlConsts.TXT_SYS_SIIE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.tax_id", "RFC"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_add1", "Calle y número"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_add2", "Colonia"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_add3", "Localidad"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "pay_met.name", "Método pago " + SEtlConsts.TXT_SYS_SIIE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.pay_acc", "Cuenta pago"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "v.cdt_day", "Días crédito"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "v.cdt_lim", "Límite crédito $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "v.src_cus_sal_agt_fk_n", "ID agente ventas " + SEtlConsts.TXT_SYS_AVISTA));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "sal_agt.name", "Agente ventas " + SEtlConsts.TXT_SYS_AVISTA));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "v.src_req_cur_fk_n", "ID moneda req " + SEtlConsts.TXT_SYS_AVISTA));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "cur.name", "Moneda req " + SEtlConsts.TXT_SYS_AVISTA));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "v.src_req_uom_fk_n", "ID unidad req " + SEtlConsts.TXT_SYS_AVISTA));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "uom.name", "Unidad req " + SEtlConsts.TXT_SYS_AVISTA));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_etl_ign", "Ignorar exportación SIIE"));
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
        moSuscriptionsSet.add(SModConsts.AU_SAL_AGT);
        moSuscriptionsSet.add(SModConsts.CU_USR);
    }
}
