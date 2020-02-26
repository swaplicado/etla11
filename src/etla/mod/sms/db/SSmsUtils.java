/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.db;

import etla.mod.SModConsts;
import etla.mod.cfg.db.SDbConfig;
import etla.mod.etl.db.SEtlConsts;
import etla.mod.etl.db.SEtlProcess;
import java.sql.Connection;
import java.sql.ResultSet;
import sa.gui.util.SUtilConsts;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public abstract class SSmsUtils {
    
    /**
     * Creates Revuelta DB connection.
     * @param session Current user session.
     * @return 
     * @throws Exception
     */
    public static Connection connectToRevuelta(final SGuiSession session) throws Exception {
        SDbConfigSms configSms = (SDbConfigSms) session.readRegistry(SModConsts.S_CFG, new int[] { SUtilConsts.BPR_CO_ID });

        return SEtlProcess.createConnection(SEtlConsts.DB_SYBASE,
                configSms.getRevueltaHost(),
                configSms.getRevueltaPort(),
                configSms.getRevueltaName(),
                configSms.getRevueltaUser(),
                configSms.getRevueltaPassword());
    }

    /**
     * Creates SIIE DB connection.
     * @param session Current user session.
     * @return 
     * @throws Exception
     */
    public static Connection connectToSiie(final SGuiSession session) throws Exception {
        SDbConfig config = (SDbConfig) session.readRegistry(SModConsts.C_CFG, new int[] { SUtilConsts.BPR_CO_ID });

        return SEtlProcess.createConnection(SEtlConsts.DB_MYSQL,
                config.getSiieHost(),
                config.getSiiePort(),
                config.getSiieName(),
                config.getSiieUser(),
                config.getSiiePassword());
    }

    /**
     * Get shipper ID from Revuelta carrier ID.
     * @param session Current user session.
     * @param carrierId Revuelta carrier ID
     * @return Shipper ID if could be found, otherwise zero.
     * @throws Exception 
     */
    public static int getShipperId(final SGuiSession session, final String carrierId) throws Exception {
        int shipperId = 0;
        
        String sql = "SELECT id_shipper "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.SU_SHIPPER) + " "
                + "WHERE carr_id = " + carrierId + ";";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                shipperId = resultSet.getInt(1);
            }
        }
        
        return shipperId;
    }

    /**
     * Get shipper ID from Revuelta carrier ID.
     * @param session Current user session.
     * @param ticketId Revuelta ticket ID
     * @return Weighing machine ticket ID if could be found, otherwise zero.
     * @throws Exception 
     */
    public static int getWmTicketId(final SGuiSession session, final int ticketId) throws Exception {
        int wmTicketId = 0;

        String sql = "SELECT id_wm_ticket "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.S_WM_TICKET) + " "
                + "WHERE ticket_id = " + ticketId + " "
                + "ORDER BY id_wm_ticket";
        ResultSet resultSet = session.getStatement().executeQuery(sql);

        if (resultSet.next()) {
            wmTicketId = resultSet.getInt(1);
        }        
        
        return wmTicketId;
    }
}
