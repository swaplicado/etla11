/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

import erp.mod.SModSysConsts;
import etla.mod.SModConsts;
import etla.mod.cfg.db.SDbConfig;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiItem;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SEtlUtils {
    
    public static DecimalFormat DecimalFormatPlantBoardType = new DecimalFormat("000000");
    
    public static String composeItemCode(final String prefix, final int plantBoardType, final String flute) {
        return prefix + "-" + DecimalFormatPlantBoardType.format(plantBoardType) + "-" + flute;
    }
    
    public static String composeItemCodeRaw(final int plantBoardType, final String flute) {
        return DecimalFormatPlantBoardType.format(plantBoardType) + "-" + flute;
    }
    
    public static SDbCustomer getEtlCustomer(final SGuiSession session, final String customerId) throws Exception {
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;
        SDbCustomer customer = null;
        
        statement = session.getStatement().getConnection().createStatement();
        
        sql = "SELECT id_cus, b_del "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.AU_CUS) + " "
                + "WHERE src_cus_id='" + customerId + "' "
                + "ORDER BY b_del DESC, id_cus "; // priority on alive records, newest record first!
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            customer = (SDbCustomer) session.readRegistry(SModConsts.AU_CUS, new int[] { resultSet.getInt("id_cus") });
        }
        
        return customer;
    }
    
    public static double getEtlExchangeRate(final SGuiSession session, final int currencyPk, final Date date) throws Exception {
        double exchangeRate = 0;
        String sql = "";
        ResultSet resultSet = null;
        
        sql = "SELECT exr, id_exr "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.A_EXR) + " "
                + "WHERE id_cur=" + currencyPk + " AND dat='" + SLibUtils.DbmsDateFormatDate.format(date) + "' "
                + "ORDER BY id_exr DESC "; // newest record first!
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            exchangeRate = resultSet.getDouble(1);
        }
        
        return exchangeRate;
    }
    
    public static int getSiieNextDpsNumber(final SGuiSession session, final Statement statement) throws Exception {
        int number = 0;
        String sql = "";
        ResultSet resultSet = null;
        SDbConfigAvista configAvista = ((SDbConfig) session.getConfigSystem()).getDbConfigAvista();
        
        sql = "SELECT COALESCE(MAX(CAST(num AS SIGNED)) + 1, 0) "
                + "FROM trn_dps "
                + "WHERE num_ser='" + configAvista.getInvoiceSeries() + "' AND "
                + "fid_ct_dps=" + SModSysConsts.TRNU_TP_DPS_SAL_INV[0] + " AND "
                + "fid_cl_dps=" + SModSysConsts.TRNU_TP_DPS_SAL_INV[1] + " AND "
                + "fid_tp_dps=" + SModSysConsts.TRNU_TP_DPS_SAL_INV[2] + " AND "
                + "b_del=0 ";
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            number = resultSet.getInt(1);
        }
        
        if (number == 0) {
            number = configAvista.getInvoiceNumberStarting();
        }
        
        return number;
    }
    
    public static boolean isSiiePeriodOpen(final int[] period, final Statement statement) throws Exception {
        boolean open = false;
        String sql = "";
        ResultSet resultSet = null;
        
        sql = "SELECT b_closed "
                + "FROM fin_year_per "
                + "WHERE id_year=" + period[0] + " AND "
                + "id_per=" + period[1] + " ";
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            open = !resultSet.getBoolean(1);
        }
        
        return open;
    }
    
    public static ArrayList<SGuiItem> createAvistaInvoiceBatchGuiItems(final Date[] period, final Statement statement) throws Exception {
        ArrayList<SGuiItem> items = new ArrayList<>();
        String sql = "";
        ResultSet resultSet = null;
        
        items.add(new SGuiItem("- " + SGuiConsts.TXT_BTN_SELECT + " lote facturas -"));
        
        sql = "SELECT DISTINCT CAST(ci.Created AS DATE) AS BatchDate, ci.BatchNumber "
                + "FROM CustomerInvoices AS ci "
                + "WHERE CAST(ci.Created AS DATE) BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(period[0]) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(period[1]) + "' AND "
                + "ci.CurrentStatusKey IN (" + SEtlConsts.AVISTA_INV_STA_APP + ", " + SEtlConsts.AVISTA_INV_STA_ARC + ") AND "
                + "ci.CustomerInvoiceTypeKey=" + SEtlConsts.AVISTA_INV_TP_INV + " "
                + "ORDER BY CAST(ci.Created AS DATE), ci.BatchNumber ";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            items.add(new SGuiItem(new int[] { resultSet.getInt("BatchNumber") }, SLibUtils.DateFormatDate.format(resultSet.getDate("BatchDate")) + " - #" + resultSet.getInt("BatchNumber")));
        }
        
        return items;
    }
}
