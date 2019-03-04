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
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbSalesAgent extends SDbRegistryUser {

    protected int mnPkSalesAgentId;
    protected int mnSrcSalesAgentId;
    protected int mnDesSalesAgentId;
    protected String msCode;
    protected String msName;
    protected Date mtFirstEtlInsert;
    protected Date mtLastEtlUpdate;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkLastEtlLogId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbSalesAgent() {
        super(SModConsts.AU_SAL_AGT);
    }

    /*
     * Public methods
     */

    public void setPkSalesAgentId(int n) { mnPkSalesAgentId = n; }
    public void setSrcSalesAgentId(int n) { mnSrcSalesAgentId = n; }
    public void setDesSalesAgentId(int n) { mnDesSalesAgentId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setFirstEtlInsert(Date t) { mtFirstEtlInsert = t; }
    public void setLastEtlUpdate(Date t) { mtLastEtlUpdate = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkLastEtlLogId(int n) { mnFkLastEtlLogId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkSalesAgentId() { return mnPkSalesAgentId; }
    public int getSrcSalesAgentId() { return mnSrcSalesAgentId; }
    public int getDesSalesAgentId() { return mnDesSalesAgentId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public Date getFirstEtlInsert() { return mtFirstEtlInsert; }
    public Date getLastEtlUpdate() { return mtLastEtlUpdate; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkLastEtlLogId() { return mnFkLastEtlLogId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSalesAgentId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSalesAgentId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkSalesAgentId = 0;
        mnSrcSalesAgentId = 0;
        mnDesSalesAgentId = 0;
        msCode = "";
        msName = "";
        mtFirstEtlInsert = null;
        mtLastEtlUpdate = null;
        mbDeleted = false;
        mbSystem = false;
        mnFkLastEtlLogId = 0;
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
        return "WHERE id_sal_agt = " + mnPkSalesAgentId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_sal_agt = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkSalesAgentId = 0;

        msSql = "SELECT COALESCE(MAX(id_sal_agt), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkSalesAgentId = resultSet.getInt(1);
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
            mnPkSalesAgentId = resultSet.getInt("id_sal_agt");
            mnSrcSalesAgentId = resultSet.getInt("src_sal_agt_id");
            mnDesSalesAgentId = resultSet.getInt("des_sal_agt_id");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mtFirstEtlInsert = resultSet.getTimestamp("fst_etl_ins");
            mtLastEtlUpdate = resultSet.getTimestamp("lst_etl_upd");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkLastEtlLogId = resultSet.getInt("fk_lst_etl_log");
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
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkSalesAgentId + ", " + 
                    mnSrcSalesAgentId + ", " + 
                    mnDesSalesAgentId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkLastEtlLogId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_sal_agt = " + mnPkSalesAgentId + ", " +
                    "src_sal_agt_id = " + mnSrcSalesAgentId + ", " +
                    "des_sal_agt_id = " + mnDesSalesAgentId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    //"fst_etl_ins = " + "NOW()" + ", " +
                    "lst_etl_upd = " + "NOW()" + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_lst_etl_log = " + mnFkLastEtlLogId + ", " +
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
    public SDbSalesAgent clone() throws CloneNotSupportedException {
        SDbSalesAgent registry = new SDbSalesAgent();

        registry.setPkSalesAgentId(this.getPkSalesAgentId());
        registry.setSrcSalesAgentId(this.getSrcSalesAgentId());
        registry.setDesSalesAgentId(this.getDesSalesAgentId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setFirstEtlInsert(this.getFirstEtlInsert());
        registry.setLastEtlUpdate(this.getLastEtlUpdate());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkLastEtlLogId(this.getFkLastEtlLogId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
