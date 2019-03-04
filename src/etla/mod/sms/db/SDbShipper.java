/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.db;

import etla.mod.SModConsts;
import etla.mod.SModSysConsts;
import etla.mod.cfg.db.SDbUser;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Daniel López
 */
public class SDbShipper extends SDbRegistryUser {
    
    protected int mnPkShipperId;
    protected String msCode;
    protected String msName;
    protected String msMail;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkUserId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected SDbUser moDbUser;
    
    public SDbShipper () {
        super(SModConsts.SU_SHIPPER);
    }
    
    /*
     * Public methods
     */
    
    public void setPkShipperId(int n) { mnPkShipperId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setMail(String s) { msMail = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkShipperId() { return mnPkShipperId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getMail() { return msMail; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserId() { return mnFkUserId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public void setDbUser(SDbUser user) { moDbUser = user; }
    
    public SDbUser getDbUser() { return moDbUser; }
    
    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkShipperId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkShipperId };
    }

    @Override
    public void initRegistry() {
        
        initBaseRegistry();
        
        mnPkShipperId = 0;
        msCode = "";
        msName = "";
        msMail = "";
        mbDeleted = false;
        mbSystem = false;
        mnFkUserId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        moDbUser = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_shipper = " + mnPkShipperId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_shipper = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkShipperId = 0;

        msSql = "SELECT COALESCE(MAX(id_shipper), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkShipperId = resultSet.getInt(1);
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
            mnPkShipperId = resultSet.getInt("id_shipper");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msMail = resultSet.getString("mail");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserId = resultSet.getInt("fk_usr");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            mbRegistryNew = false;
        }
        
        moDbUser = (SDbUser) session.readRegistry(SModConsts.CU_USR, new int[] { mnFkUserId });
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {       
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        if (mbRegistryNew) {
            moDbUser = new SDbUser();

            //moDbUser.setPkUserId(...);
            moDbUser.setDesUserId(SLibConsts.UNDEFINED);
            moDbUser.setName(msMail);
            //moDbUser.setPassword(...);
            moDbUser.setWeb(true);
            //moDbUser.setDeleted(...);
            //moDbUser.setSystem(...);
            moDbUser.setFkUserTypeId(SModSysConsts.CS_USR_TP_USR);
            moDbUser.setFkWebRoleId(SModSysConsts.SS_WEB_ROLE_SHIPPER);
            //moDbUser.setFkUserInsertId(...);
            //moDbUser.setFkUserUpdateId(...);
            //moDbUser.setTsUserInsert(...);
            //moDbUser.setTsUserUpdate(...);
            
            moDbUser.setAuxClearPasswordOnSave(true);
        }
        else {
            moDbUser.setName(msMail);
        }
        
        moDbUser.save(session);
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserId = moDbUser.getPkUserId();
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                mnPkShipperId + ", " + 
                "'" + msCode + "', " + 
                "'" + msName + "', " + 
                "'" + msMail + "', " + 
                (mbDeleted ? 1 : 0) + ", " + 
                (mbSystem ? 1 : 0) + ", " +
                mnFkUserId + ", " + 
                mnFkUserInsertId + ", " + 
                mnFkUserUpdateId + ", " + 
                "NOW()" + ", " + 
                "NOW()" + " " +  
                 ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                //"id_shipper = " + mnPkShipperId + ", " +
                "code = '" + msCode + "', " +
                "name = '" + msName + "', " +
                "mail = '" + msMail + "', " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                "fk_usr = " + mnFkUserId + ", " +
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
    public SDbShipper clone() throws CloneNotSupportedException {
        SDbShipper  registry = new SDbShipper();
        
        registry.setPkShipperId(this.getPkShipperId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setMail(this.getMail());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserId(this.getFkUserId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setDbUser(this.getDbUser() == null ? null : this.getDbUser().clone());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
