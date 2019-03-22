/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.db;

import etla.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Alfredo Pérez, Sergio Flores
 */
public class SDbErpDocEtlLog extends SDbRegistryUser {

    protected int mnPkErpDocEtlLogId;
    protected Date mtTsBaseNextEtl;
    protected Date mtTsEtlStart;
    protected Date mtTsEtlEnd;

    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbErpDocEtlLog() {
        super(SModConsts.S_ERP_DOC_ETL_LOG);
    }

    /*
     * Public methods
     */

    public void setPkErpDocEtlLogId(int n) { mnPkErpDocEtlLogId = n; }
    public void setTsBaseNextEtl(Date t) { mtTsBaseNextEtl = t; }
    public void setTsEtlStart(Date t) { mtTsEtlStart = t; }
    public void setTsEtlEnd(Date t) { mtTsEtlEnd = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkErpDocEtlLogId() { return mnPkErpDocEtlLogId; }
    public Date getTsBaseNextEtl() { return mtTsBaseNextEtl; }
    public Date getTsEtlStart() { return mtTsEtlStart; }
    public Date getTsEtlEnd() { return mtTsEtlEnd; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkErpDocEtlLogId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkErpDocEtlLogId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkErpDocEtlLogId = 0;
        mtTsBaseNextEtl = null;
        mtTsEtlStart = null;
        mtTsEtlEnd = null;
        mbDeleted = false;
        mbSystem = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_erp_doc_etl_log = " + mnPkErpDocEtlLogId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_erp_doc_etl_log = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkErpDocEtlLogId = 0;

        msSql = "SELECT COALESCE(MAX(id_erp_doc_etl_log), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkErpDocEtlLogId = resultSet.getInt(1);
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
            mnPkErpDocEtlLogId = resultSet.getInt("id_erp_doc_etl_log");
            mtTsBaseNextEtl = resultSet.getTimestamp("ts_base_next_etl");
            mtTsEtlStart = resultSet.getTimestamp("ts_etl_sta");
            mtTsEtlEnd = resultSet.getTimestamp("ts_etl_end");
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

        // Save registry:
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkErpDocEtlLogId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(mtTsBaseNextEtl) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(mtTsEtlStart) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(mtTsEtlEnd) + "', " + 
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
                    "id_erp_doc_etl_log = " + mnPkErpDocEtlLogId + ", " +
                    "ts_etl_low_lim = '" + SLibUtils.DbmsDateFormatDatetime.format(mtTsBaseNextEtl) + "', " +
                    "ts_etl_low_lim = '" + SLibUtils.DbmsDateFormatDatetime.format(mtTsEtlStart) + "', " +
                    "ts_etl_end = '" + SLibUtils.DbmsDateFormatDatetime.format(mtTsEtlEnd) + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbErpDocEtlLog clone() throws CloneNotSupportedException {
        SDbErpDocEtlLog registry = new SDbErpDocEtlLog();

        registry.setPkErpDocEtlLogId(this.getPkErpDocEtlLogId());
        registry.setTsBaseNextEtl(this.getTsBaseNextEtl());
        registry.setTsEtlStart(this.getTsEtlStart());
        registry.setTsEtlEnd(this.getTsEtlEnd());
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
