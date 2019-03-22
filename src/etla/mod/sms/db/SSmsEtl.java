/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.db;

import erp.data.SDataConstantsSys;
import etla.mod.SModConsts;
import etla.mod.SModSysConsts;
import etla.mod.cfg.db.SDbConfig;
import etla.mod.etl.db.SEtlConsts;
import etla.mod.etl.db.SEtlProcess;
import sa.lib.gui.SGuiSession;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Alfredo Pérez, Cesar Orozco, Sergio Flores
 */
public class SSmsEtl {

    private final SGuiSession moSession;
    private Connection moConnectionSiie;
    private Connection moConnectionRevuelta;
    private SDbErpDocEtlLog moLastErpDocEtlLog;
    private Date mtEtlStart;
    private Date mtMaxDocTsEdit;

    /**
     * Creates an object to compute SMS ETL.
     * @param session
     * @throws Exception 
     */
    public SSmsEtl(SGuiSession session) throws Exception {
        moSession = session;
        moConnectionSiie = connectToSiie();
        moConnectionRevuelta = connectToRevuelta();
        moLastErpDocEtlLog = null;
        mtEtlStart = null;
        mtMaxDocTsEdit = null;
    }

    /**
     * Creates Revuelta DB connection.
     * @throws Exception
     */
    private Connection connectToRevuelta() throws Exception {
        SDbConfigSms configSms = (SDbConfigSms) moSession.readRegistry(SModConsts.S_CFG, new int[] { SUtilConsts.BPR_CO_ID });

        return SEtlProcess.createConnection(SEtlConsts.DB_SYBASE,
                configSms.getRevueltaHost(),
                configSms.getRevueltaPort(),
                configSms.getRevueltaName(),
                configSms.getRevueltaUser(),
                configSms.getRevueltaPassword());
    }

    /**
     * Creates SIIE DB connection.
     * @throws Exception
     */
    private Connection connectToSiie() throws Exception {
        SDbConfig config = (SDbConfig) moSession.readRegistry(SModConsts.C_CFG, new int[] { SUtilConsts.BPR_CO_ID });

        return SEtlProcess.createConnection(SEtlConsts.DB_MYSQL,
                config.getSiieHost(),
                config.getSiiePort(),
                config.getSiieName(),
                config.getSiieUser(),
                config.getSiiePassword());
    }

    /**
     * Imports weighing machine users (companies) from Revuelta DB.
     * @throws Exception 
     */
    private void importRevueltaWmUsers() throws Exception {
        String sql = "SELECT Usb_ID, Usb_Nombre "
                + "FROM dba.UsuariosBas "
                + "ORDER BY Usb_ID";
        ResultSet resultSetRev = moConnectionRevuelta.createStatement().executeQuery(sql);
        while (resultSetRev.next()) {
            SDbWmUser wmUser = new SDbWmUser();

            sql = "SELECT id_wm_user "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_WM_USER) + " "
                    + "WHERE user_id = '" + resultSetRev.getString("Usb_ID") + "' "
                    + "ORDER BY id_wm_user";
            ResultSet resultSet = moSession.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                wmUser.read(moSession, new int[] { resultSet.getInt(1) });
            }

            //wmUser.setPkWmUserId();
            wmUser.setUserId(resultSetRev.getString("Usb_ID"));
            wmUser.setName(resultSetRev.getString("Usb_Nombre"));
            //wmUser.setDeleted();
            wmUser.setSystem(true);
            /*
            wmUser.setFkUserInsertId();
            wmUser.setFkUserUpdateId();
            wmUser.setTsUserInsert();
            wmUser.setTsUserUpdate();
            */

            wmUser.save(moSession);
        }
    }

    /**
     * Imports weighing machine items (products) from Revuelta DB.
     * @throws Exception 
     */
    private void importRevueltaWmItems() throws Exception {
        String sql = "SELECT Pro_ID, Pro_Nombre "
                + "FROM dba.Productos "
                + "ORDER BY Pro_ID";
        ResultSet resultSetRev = moConnectionRevuelta.createStatement().executeQuery(sql);
        while (resultSetRev.next()) {
            SDbWmItem wmItem = new SDbWmItem();

            sql = "SELECT id_wm_item "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_WM_ITEM) + " "
                    + "WHERE prod_id = '" + resultSetRev.getString("Pro_ID") + "' "
                    + "ORDER BY id_wm_item";
            ResultSet resultSet = moSession.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                wmItem.read(moSession, new int[] { resultSet.getInt(1) });
            }

            //wmItem.setPkWmItemId();
            wmItem.setProductId(resultSetRev.getString("Pro_Id"));
            wmItem.setCode(resultSetRev.getString("Pro_Id"));
            wmItem.setName(resultSetRev.getString("Pro_Nombre"));
            //wmItem.setDeleted();
            wmItem.setSystem(true);
            /*
            wmItem.setFkUserInsertId();
            wmItem.setFkUserUpdateId();
            wmItem.setTsUserInsert();
            wmItem.setTsUserUpdate();
            */

            wmItem.save(moSession);
        }
    }

    /**
     * Finds own ID for user (company) from ID in Revuelta.
     * @param userId
     * @return Own ID.
     * @throws Exception
     */
    private int searchWmUserId(String userId) throws Exception {
        int id = 0;
        String sql = "SELECT id_wm_user "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_WM_USER) + " "
                + "WHERE user_id = '" + userId + "' "
                + "ORDER BY id_wm_user";
        ResultSet resultSet = moSession.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
        return id;
    }

    /**
     * Finds own ID for item (product) from ID in Revuelta.
     * @param itemId
     * @return Own ID.
     * @throws Exception
     */
    private int searchWmItemId(String itemId) throws Exception {
        int id = 0;
        String sql = "SELECT id_wm_item "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_WM_ITEM) + " "
                + "WHERE prod_id = '" + itemId + "' "
                + "ORDER BY id_wm_item";
        ResultSet resultSet = moSession.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
        return id;
    }

    /**
     * Imports weighing machine tickets from Revuelta DB.
     * @throws Exception 
     */
    private void importRevueltaWmTickets() throws Exception {
        String sql = "SELECT Pes_ID, pro.Pro_ID, Pes_FecHorPri, Pes_FecHorSeg, "
                + "Emp_Nombre, Pes_Chofer, Pes_Placas, "
                + "Pes_PesoPri, Pes_PesoSeg, "
                + "Pes_FecHor, pe.Pro_ID, Usb_ID "
                + "FROM dba.Pesadas as pe "
                + "INNER JOIN dba.Productos AS pro ON pe.Pro_ID = pro.Pro_ID "
                + "WHERE Pes_Completo = 1 AND "
                + "Pes_FecHor > '" + SLibUtils.DbmsDateFormatDate.format(moLastErpDocEtlLog.getTsBaseNextEtl()) + "' "
                + "ORDER BY Pes_ID";
        ResultSet resultSetRev = moConnectionRevuelta.createStatement().executeQuery(sql);
        while (resultSetRev.next()) {
            SDbWmTicket wmTicket = new SDbWmTicket();

            sql = "SELECT id_wm_ticket "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.S_WM_TICKET) + " "
                    + "WHERE ticket_id = " + resultSetRev.getInt("Pes_ID") + " "
                    + "ORDER BY id_wm_ticket";
            ResultSet resultSet = moSession.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                wmTicket.read(moSession, new int[] {resultSet.getInt(1) });
            }

            //wmTicket.setPkWmTicketId();
            wmTicket.setTicketId(resultSetRev.getInt("Pes_id"));
            wmTicket.setTicketDatetimeArrival(resultSetRev.getDate("Pes_FecHorPri"));
            wmTicket.setTicketDatetimeDeparture(resultSetRev.getDate("Pes_FecHorSeg"));
            wmTicket.setCompany(resultSetRev.getString("Emp_nombre"));
            wmTicket.setDriverName(resultSetRev.getString("Pes_Chofer"));
            wmTicket.setVehiclePlate(resultSetRev.getString("Pes_Placas"));
            wmTicket.setWeightArrival(resultSetRev.getDouble("Pes_PesoPri"));
            wmTicket.setWeightDeparture(resultSetRev.getDouble("Pes_PesoSeg"));
            //wmTicket.setWeight();
            wmTicket.setWmInfoArrival(true);
            wmTicket.setWmInfoDeparture(true);
            wmTicket.setTared(true);
            wmTicket.setClosed(false);
            //wmTicket.setDeleted();
            wmTicket.setSystem(true);
            boolean isIn = resultSetRev.getDouble("Pes_PesoPri") >= resultSetRev.getDouble("Pes_PesoSeg");
            wmTicket.setFkWmTicketTypeId(isIn ? SModSysConsts.SS_WM_TICKET_TP_IN : SModSysConsts.SS_WM_TICKET_TP_OUT);
            wmTicket.setFkWmItemId(searchWmItemId(resultSetRev.getString("Pro_ID")));
            wmTicket.setFkWmUserId(searchWmUserId(resultSetRev.getString("Usb_ID")));
            /*
            wmTicket.setFkUserTareId();
            wmTicket.setFkUserClosedId();
            wmTicket.setFkUserInsertId();
            wmTicket.setFkUserUpdateId();
            wmTicket.setTsUserTare();
            wmTicket.setTsUserClosed();
            wmTicket.setTsUserInsert();
            wmTicket.setTsUserUpdate();
            */

            wmTicket.setAuxJustTared(true);

            wmTicket.save(moSession);
        }
    }

    /**
     * Imports ERP documents for income and expenses from SIIE DB.
     * @throws Exception 
     */
    private void importSiieDocs() throws Exception {
        mtMaxDocTsEdit = new Date(0);

        String sql = "SELECT d.id_year, d.id_doc, d.fid_ct_dps, d.fid_cl_dps, "
                + "d.num_ser, d.num, d.dt, d.ts_edit, b.id_bp, b.bp, "
                + "SUM(de.weight_gross) AS _weight_gross "
                + "FROM trn_dps AS d "
                + "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc "
                + "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp "
                + "WHERE NOT d.b_del AND NOT de.b_del AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND "
                + "d.ts_edit >= '" + SLibUtils.DbmsDateFormatDate.format(moLastErpDocEtlLog.getTsBaseNextEtl()) + "' AND "
                + "(fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] + " OR fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1] + ") "
                + "GROUP BY d.id_year, d.id_doc, d.fid_ct_dps, d.fid_cl_dps, "
                + "d.num_ser, d.num, d.dt, d.ts_edit, b.id_bp, b.bp "
                + "ORDER BY d.id_year, d.id_doc ";
        ResultSet resultSetSiie = moConnectionSiie.createStatement().executeQuery(sql);
        while (resultSetSiie.next()) {
            SDbErpDoc erpDoc = new SDbErpDoc();

            sql = "SELECT id_erp_doc "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.S_ERP_DOC) + " "
                    + "WHERE erp_year_id = " + resultSetSiie.getInt("d.id_year") + " AND erp_doc_id = " + resultSetSiie.getInt("d.id_doc") + " "
                    + "ORDER BY id_erp_doc";
            ResultSet resultSet = moSession.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                erpDoc.read(moSession, new int[] { resultSet.getInt(1) });
            }
            //erpDoc.setPkErpDocId();
            erpDoc.setErpYearId(resultSetSiie.getInt("d.id_year"));
            erpDoc.setErpDocId(resultSetSiie.getInt("d.id_doc"));
            erpDoc.setDocClass(resultSetSiie.getInt("d.fid_ct_dps") == SDataConstantsSys.TRNS_CT_DPS_SAL ? SSmsConsts.DOC_CLASS_INC : SSmsConsts.DOC_CLASS_EXP);
            erpDoc.setDocType(resultSetSiie.getInt("fid_cl_dps") == SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] ? SSmsConsts.DOC_TYPE_INV : SSmsConsts.DOC_TYPE_CN);
            erpDoc.setDocSeries(resultSetSiie.getString("d.num_ser"));
            erpDoc.setDocNumber(resultSetSiie.getString("d.num"));
            erpDoc.setDocDate(resultSetSiie.getDate("d.dt"));
            erpDoc.setBizPartnerId(resultSetSiie.getInt("b.id_bp"));
            erpDoc.setBizPartner(resultSetSiie.getString("b.bp"));
            erpDoc.setWeight(resultSetSiie.getDouble("_weight_gross"));
            erpDoc.setDocUpdate(resultSetSiie.getTimestamp("ts_edit"));
            //erpDoc.setClosed();
            //erpDoc.setDeleted();
            erpDoc.setSystem(true);
            /*
            erpDoc.setFkUserClosedId();
            erpDoc.setFkUserInsertId();
            erpDoc.setFkUserUpdateId();
            registry.setTsUserClosed();
            registry.setTsUserInsert();
            registry.setTsUserUpdate();
            */

            erpDoc.save(moSession);

            if (mtMaxDocTsEdit.before(resultSetSiie.getTimestamp("ts_edit"))){
                mtMaxDocTsEdit = resultSetSiie.getTimestamp("ts_edit");
            }
        }
    }

    /**
     * Obtains last entry log of ETL of ERP documents.
     * NOTE: must be invoked at the begining of ETL process!
     * @throws Exception
     */
    private void obtainLastErpDocEtlLog() throws Exception {
        String sql = "SELECT MAX(id_erp_doc_etl_log) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_ERP_DOC_ETL_LOG);
        ResultSet resultSet = moSession.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            moLastErpDocEtlLog = (SDbErpDocEtlLog) moSession.readRegistry(SModConsts.S_ERP_DOC_ETL_LOG, new int[] { resultSet.getInt(1) });
        }
    }

    /**
     * Creates entry log of ETL of ERP documents.
     * NOTE: must be invoked at the end of ETL process!
     * @throws Exception
     */
    private void createErpDocEtlLog() throws Exception {
        SDbErpDocEtlLog erpDocEtlLog = new SDbErpDocEtlLog();

        //erpDocEtlLog.setPkErpDocEtlLogId();
        erpDocEtlLog.setTsBaseNextEtl(mtMaxDocTsEdit);
        erpDocEtlLog.setTsEtlStart(mtEtlStart);
        erpDocEtlLog.setTsEtlEnd(new Date());
        //erpDocEtlLog.setDeleted();
        erpDocEtlLog.setSystem(true);
        /*
        erpDocEtlLog.setFkUserInsertId();
        erpDocEtlLog.setFkUserUpdateId();
        erpDocEtlLog.setTsUserInsert();
        erpDocEtlLog.setTsUserUpdate();
        */
        
        erpDocEtlLog.save(moSession);
    }

    /**
     * Computes ETL process for importing Users, Items and Tickets from Revuelta and Documents from SIIE.
     * @throws Exception
     */
    public void computeEtl() throws Exception {
        mtEtlStart = new Date();

        obtainLastErpDocEtlLog(); // must be invoked at the begining!
        importRevueltaWmUsers();
        importRevueltaWmItems();
        importRevueltaWmTickets();
        importSiieDocs();
        createErpDocEtlLog(); // must be invoked at the end!
    }
}
