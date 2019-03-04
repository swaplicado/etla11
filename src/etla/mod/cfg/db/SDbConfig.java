/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.cfg.db;

import etla.mod.SModConsts;
import etla.mod.etl.db.SDbConfigAvista;
import etla.mod.sms.db.SDbConfigSms;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiConfigSystem;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbConfig extends SDbRegistryUser implements SGuiConfigSystem {
    
    protected int mnPkConfigId;
    protected int mnVersion;
    protected Date mtVersionTs;
    protected String msSiieHost;
    protected int mnSiiePort;
    protected String msSiieName;
    protected String msSiieUser;
    protected String msSiiePassword;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected SDbConfigAvista moDbConfigAvista;
    protected SDbConfigSms moDbConfigSms;
    
    public SDbConfig() {
        super(SModConsts.C_CFG);
    }

    /*
     * Public methods
     */
    
    public void setPkConfigId(int n) { mnPkConfigId = n; }
    public void setVersion(int n) { mnVersion = n; }
    public void setVersionTs(Date t) { mtVersionTs = t; }
    public void setSiieHost(String s) { msSiieHost = s; }
    public void setSiiePort(int n) { mnSiiePort = n; }
    public void setSiieName(String s) { msSiieName = s; }
    public void setSiieUser(String s) { msSiieUser = s; }
    public void setSiiePassword(String s) { msSiiePassword = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkConfigId() { return mnPkConfigId; }
    public int getVersion() { return mnVersion; }
    public Date getVersionTs() { return mtVersionTs; }
    public String getSiieHost() { return msSiieHost; }
    public int getSiiePort() { return mnSiiePort; }
    public String getSiieName() { return msSiieName; }
    public String getSiieUser() { return msSiieUser; }
    public String getSiiePassword() { return msSiiePassword; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setDbConfigAvista(SDbConfigAvista o) { moDbConfigAvista = o; }
    public void setDbConfigSms(SDbConfigSms o) { moDbConfigSms = o; }
    
    public SDbConfigAvista getDbConfigAvista() { return moDbConfigAvista; }
    public SDbConfigSms getDbConfigSms() { return moDbConfigSms; }
    
    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkConfigId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkConfigId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkConfigId = 0;
        mnVersion = 0;
        mtVersionTs = null;
        msSiieHost = "";
        mnSiiePort = 0;
        msSiieName = "";
        msSiieUser = "";
        msSiiePassword = "";
        mbDeleted = false;
        mbSystem = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        moDbConfigAvista = null;
        moDbConfigSms = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_cfg = " + mnPkConfigId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cfg = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkConfigId = 0;

        msSql = "SELECT COALESCE(MAX(id_cfg), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkConfigId = resultSet.getInt(1);
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
            mnPkConfigId = resultSet.getInt("id_cfg");
            mnVersion = resultSet.getInt("ver");
            mtVersionTs = resultSet.getTimestamp("ver_ts");
            msSiieHost = resultSet.getString("sie_host");
            mnSiiePort = resultSet.getInt("sie_port");
            msSiieName = resultSet.getString("sie_name");
            msSiieUser = resultSet.getString("sie_user");
            msSiiePassword = resultSet.getString("sie_pswd");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // Read aswell supplementary info:
            
            moDbConfigAvista = new SDbConfigAvista();
            moDbConfigAvista.read(session, new int[] { mnPkConfigId });
            
            moDbConfigSms = new SDbConfigSms();
            moDbConfigSms.read(session, new int[] { mnPkConfigId });
            
            // Finish registry reading:

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbUpdatable = true;
            mbDisableable = true;
            mbDeletable = true;
            mbDisabled = false;
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkConfigId + ", " + 
                    mnVersion + ", " + 
                    "NOW()" + ", " + 
                    "'" + msSiieHost + "', " + 
                    mnSiiePort + ", " + 
                    "'" + msSiieName + "', " + 
                    "'" + msSiieUser + "', " + 
                    "'" + msSiiePassword + "', " + 
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
                    //"id_cfg = " + mnPkConfigId + ", " +
                    "ver = " + mnVersion + ", " +
                    "ver_ts = " + "NOW()" + ", " +
                    "sie_host = '" + msSiieHost + "', " +
                    "sie_port = " + mnSiiePort + ", " +
                    "sie_name = '" + msSiieName + "', " +
                    "sie_user = '" + msSiieUser + "', " +
                    "sie_pswd = '" + msSiiePassword + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + ", " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // Save aswell supplementary info:
        
        moDbConfigAvista.save(session);
        
        moDbConfigSms.save(session);
            
        // Finish registry updating:
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbConfig registry = new SDbConfig();

        registry.setPkConfigId(this.getPkConfigId());
        registry.setVersion(this.getVersion());
        registry.setVersionTs(this.getVersionTs());
        registry.setSiieHost(this.getSiieHost());
        registry.setSiiePort(this.getSiiePort());
        registry.setSiieName(this.getSiieName());
        registry.setSiieUser(this.getSiieUser());
        registry.setSiiePassword(this.getSiiePassword());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setDbConfigAvista(this.getDbConfigAvista() == null ? null : this.getDbConfigAvista().clone());
        registry.setDbConfigSms(this.getDbConfigSms() == null ? null : this.getDbConfigSms().clone());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public int getSystemId() {
        return getPkConfigId();
    }
}
