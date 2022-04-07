/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.db;

import etla.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbForkliftDriver extends SDbRegistryUser {
    
    protected int mnPkForkliftDriver;
    protected String msCode;
    protected String msName;
    protected String msLastname;
    protected String msForename;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbForkliftDriver() {
        super(SModConsts.SU_FORKLIFT_DRV);
    }

    /*
     * Public methods
     */
    
    public void setPkForkliftDriver(int n) { mnPkForkliftDriver = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setLastname(String s) { msLastname = s; }
    public void setForename(String s) { msForename = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkForkliftDriver() { return mnPkForkliftDriver; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getLastname() { return msLastname; }
    public String getForename() { return msForename; }
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
        mnPkForkliftDriver = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkForkliftDriver };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkForkliftDriver = 0;
        msCode = "";
        msName = "";
        msLastname = "";
        msForename = "";
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
        return "WHERE id_forklift_drv = " + mnPkForkliftDriver + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_forklift_drv = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkForkliftDriver = 0;
        
        msSql = "SELECT COALESCE(MAX(id_forklift_drv), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkForkliftDriver = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkForkliftDriver = resultSet.getInt("id_forklift_drv");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msLastname = resultSet.getString("lastname");
            msForename = resultSet.getString("forename");
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
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" + 
                mnPkForkliftDriver + ", " + 
                "'" + msCode + "', " + 
                "'" + msName + "', " + 
                "'" + msLastname + "', " + 
                "'" + msForename + "', " + 
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
                //"id_forklift_drv = " + mnPkForkliftDriver + ", " +
                "code = '" + msCode + "', " +
                "name = '" + msName + "', " +
                "lastname = '" + msLastname + "', " +
                "forename = '" + msForename + "', " +
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
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbForkliftDriver registry = new SDbForkliftDriver();
        
        registry.setPkForkliftDriver(this.getPkForkliftDriver());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setLastname(this.getLastname());
        registry.setForename(this.getForename());
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
