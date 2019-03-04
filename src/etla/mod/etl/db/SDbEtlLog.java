/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

import etla.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbEtlLog extends SDbRegistryUser {

    protected int mnPkEtlLogId;
    protected int mnEtlMode;
    protected Date mtDateStart;
    protected Date mtDateEnd;
    protected Date mtDateIssue_n;
    protected int mnInvoiceBatch;
    protected boolean mbUpdateData;
    protected int mnUpdateMode;
    protected Date mtTsStart;
    protected Date mtTsEnd_n;
    protected int mnStep;
    protected int mnStepAux;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected boolean mbAuxClosed;
    
    public SDbEtlLog() {
        super(SModConsts.A_ETL_LOG);
    }

    /*
     * Public methods
     */

    public void setPkEtlLogId(int n) { mnPkEtlLogId = n; }
    public void setEtlMode(int n) { mnEtlMode = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }
    public void setDateIssue_n(Date t) { mtDateIssue_n = t; }
    public void setInvoiceBatch(int n) { mnInvoiceBatch = n; }
    public void setUpdateData(boolean b) { mbUpdateData = b; }
    public void setUpdateMode(int n) { mnUpdateMode = n; }
    public void setTsStart(Date t) { mtTsStart = t; }
    public void setTsEnd_n(Date t) { mtTsEnd_n = t; }
    public void setStep(int n) { mnStep = n; }
    public void setStepAux(int n) { mnStepAux = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkEtlLogId() { return mnPkEtlLogId; }
    public int getEtlMode() { return mnEtlMode; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public Date getDateIssue_n() { return mtDateIssue_n; }
    public int getInvoiceBatch() { return mnInvoiceBatch; }
    public boolean isUpdateData() { return mbUpdateData; }
    public int getUpdateMode() { return mnUpdateMode; }
    public Date getTsStart() { return mtTsStart; }
    public Date getTsEnd_n() { return mtTsEnd_n; }
    public int getStep() { return mnStep; }
    public int getStepAux() { return mnStepAux; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setAuxClosed(boolean b) { mbAuxClosed = b; }
    
    public boolean isAuxClosed() { return mbAuxClosed; }
    
    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEtlLogId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEtlLogId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkEtlLogId = 0;
        mnEtlMode = 0;
        mtDateStart = null;
        mtDateEnd = null;
        mtDateIssue_n = null;
        mnInvoiceBatch = 0;
        mbUpdateData = false;
        mnUpdateMode = 0;
        mtTsStart = null;
        mtTsEnd_n = null;
        mnStep = 0;
        mnStepAux = 0;
        mbDeleted = false;
        mbSystem = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        mbAuxClosed = false;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_etl_log = " + mnPkEtlLogId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_etl_log = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkEtlLogId = 0;

        msSql = "SELECT COALESCE(MAX(id_etl_log), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkEtlLogId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkEtlLogId = resultSet.getInt("id_etl_log");
            mnEtlMode = resultSet.getInt("etl_mode");
            mtDateStart = resultSet.getDate("dat_sta");
            mtDateEnd = resultSet.getDate("dat_end");
            mtDateIssue_n = resultSet.getDate("dat_iss_n");
            mnInvoiceBatch = resultSet.getInt("inv_bat");
            mbUpdateData = resultSet.getBoolean("b_upd_data");
            mnUpdateMode = resultSet.getInt("upd_mode");
            mtTsStart = resultSet.getTimestamp("ts_sta");
            mtTsEnd_n = resultSet.getTimestamp("ts_end_n");
            mnStep = resultSet.getInt("step");
            mnStepAux = resultSet.getInt("step_aux");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        if (!mbUpdateData) {
            mnUpdateMode = SLibConsts.UNDEFINED;
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            //mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkEtlLogId + ", " + 
                    mnEtlMode + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " + 
                    (mtDateIssue_n == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateIssue_n) + "'") + ", " + 
                    mnInvoiceBatch + ", " + 
                    (mbUpdateData ? 1 : 0) + ", " + 
                    mnUpdateMode + ", " + 
                    "NOW(), " + 
                    (!mbAuxClosed ? "NULL" : "NOW()") + ", " + 
                    mnStep + ", " + 
                    mnStepAux + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_etl_log = " + mnPkEtlLogId + ", " +
                    "etl_mode = " + mnEtlMode + ", " +
                    "dat_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dat_end = '" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    "dat_iss_n = " + (mtDateIssue_n == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateIssue_n) + "'") + ", " +
                    "inv_bat = " + mnInvoiceBatch + ", " +
                    "b_upd_data = " + (mbUpdateData ? 1 : 0) + ", " +
                    "upd_mode = " + mnUpdateMode + ", " +
                    //"ts_sta = NOW(), " +
                    (!mbAuxClosed ? "" : "ts_end_n = NOW(), ") +
                    "step = " + mnStep + ", " +
                    "step_aux = " + mnStepAux + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbEtlLog clone() throws CloneNotSupportedException {
        SDbEtlLog registry = new SDbEtlLog();

        registry.setPkEtlLogId(this.getPkEtlLogId());
        registry.setEtlMode(this.getEtlMode());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd(this.getDateEnd());
        registry.setDateIssue_n(this.getDateIssue_n());
        registry.setInvoiceBatch(this.getInvoiceBatch());
        registry.setUpdateData(this.isUpdateData());
        registry.setUpdateMode(this.getUpdateMode());
        registry.setTsStart(this.getTsStart());
        registry.setTsEnd_n(this.getTsEnd_n());
        registry.setStep(this.getStep());
        registry.setStepAux(this.getStepAux());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
