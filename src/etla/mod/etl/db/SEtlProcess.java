/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

import etla.mod.cfg.db.SDbConfig;
import etla.mod.cfg.db.SDbUser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.Properties;
import sa.lib.SLibConsts;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SEtlProcess {
    
    public static void computeEtl(final SGuiSession session, final int mode, final Date periodStart, final Date periodEnd, final Date dateIssue, final int invoiceLot, final boolean updateData, final int updateMode) throws Exception {
        SDbEtlLog etlLog = new SDbEtlLog();
        SDbConfig config = (SDbConfig) session.getConfigSystem();
        SDbConfigAvista configAvista = config.getDbConfigAvista();
        Connection connectionSiie = null;
        Connection connectionAvista = null;
        SEtlPackage etlPackage = null;
        
        //etlLog.setPkEtlLogId(this.getPkEtlLogId());
        etlLog.setEtlMode(mode);
        etlLog.setDateStart(periodStart);
        etlLog.setDateEnd(periodEnd);
        etlLog.setDateIssue_n(dateIssue);
        etlLog.setInvoiceBatch(invoiceLot);
        etlLog.setUpdateData(updateData);
        etlLog.setUpdateMode(updateMode);
        //etlLog.setTsStart(...);
        //etlLog.setTsEnd_n(...);
        etlLog.setStep(SEtlConsts.STEP_NA);
        etlLog.setStepAux(SEtlConsts.STEP_AUX_NA);
        //etlLog.setDeleted(...);
        etlLog.setSystem(true);
        etlLog.save(session);
        
        // Validate current user:
        
        if (((SDbUser) session.getUser()).getDesUserId() == 0) {
            throw new Exception(SEtlConsts.MSG_ERR_USR_DES_ID); // no ID SIIE available
        }
        
        // Starting ETL process:
        
        etlLog.setStep(SEtlConsts.STEP_ETL_STA);
        etlLog.setStepAux(SEtlConsts.STEP_AUX_NA);
        etlLog.save(session);
        
        // Stablishing SIIE DB connection:
        
        connectionSiie = createConnection(
                SEtlConsts.DB_MYSQL, 
                config.getSiieHost(), 
                config.getSiiePort(), 
                config.getSiieName(), 
                config.getSiieUser(), 
                config.getSiiePassword());
        
        etlLog.setStep(SEtlConsts.STEP_ETL_STA_DB_SIIE);
        etlLog.setStepAux(SEtlConsts.STEP_AUX_NA);
        etlLog.save(session);
        
        // Stablishing Avista DB connection:
        
        connectionAvista = createConnection(
                SEtlConsts.DB_SQL_SERVER, 
                configAvista.getAvistaHost(), 
                configAvista.getAvistaPort(), 
                configAvista.getAvistaName(), 
                configAvista.getAvistaUser(), 
                configAvista.getAvistaPassword());
        
        etlLog.setStep(SEtlConsts.STEP_ETL_STA_DB_AVISTA);
        etlLog.setStepAux(SEtlConsts.STEP_AUX_NA);
        etlLog.save(session);
        
        // Prepare ETL package:
        
        etlPackage = new SEtlPackage();
        etlPackage.EtlLog = etlLog;
        etlPackage.ConnectionSiie = connectionSiie;
        etlPackage.ConnectionAvista = connectionAvista;
        etlPackage.PeriodStart = periodStart;
        etlPackage.PeriodEnd = periodEnd;
        etlPackage.DateIssue = dateIssue;
        etlPackage.InvoiceBatch = invoiceLot;
        etlPackage.UpdateData = updateData;
        etlPackage.UpdateMode = updateMode;
        
        // ETL customers:
        
        SEtlProcessCatCustomers.computeEtlCustomers(session, etlPackage);
        
        // ETL items:
        
        SEtlProcessCatItems.computeEtlItems(session, etlPackage);
        
        // ETL invoices:
        
        if (mode == SEtlConsts.EXP_MODE_ALL) {
            SEtlProcessDocInvoices.computeEtlInvoices(session, etlPackage);
        }
        
        // Finishing ETL process:
        
        etlLog.setStep(SEtlConsts.STEP_ETL_END);
        etlLog.setStepAux(SEtlConsts.STEP_AUX_NA);
        etlLog.setAuxClosed(true);
        etlLog.save(session);
    }
    
    /**
     * Creates connection to external system's database.
     * @param type Database type, constants defined in SEtlConsts.DB_...
     * @param host Database host.
     * @param port Database port.
     * @param name Database name.
     * @param user User name.
     * @param pswd User's password.
     * @return Database connection.
     * @throws Exception 
     */
    public static Connection createConnection(final int type, final String host, final int port, final String name, final String user, final String pswd) throws Exception {
        Connection connection = null;
        
        switch (type) {
            case SEtlConsts.DB_MYSQL:
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection("jdbc:mysql://" + host + (port == 0 ? "" : ":" + port) + "/" + name + "?user=" + user + "&password=" + pswd);
                connection.createStatement().execute("SET AUTOCOMMIT=1");
                break;
            case SEtlConsts.DB_SQL_SERVER:
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection("jdbc:sqlserver://" + host + (port == 0 ? "" : ":" + port) + ";databaseName=" + name + ";user=" + user + ";password=" + pswd);
                break;
            case SEtlConsts.DB_SYBASE:
                Class.forName("com.sybase.jdbc3.jdbc.SybDriver");
                Properties prop = new Properties();
                prop.put("user", user);
                prop.put("password", pswd);
                connection = DriverManager.getConnection("jdbc:sybase:Tds:" + host + ":" + port + "/" + name, prop);
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return connection;
    }
}
