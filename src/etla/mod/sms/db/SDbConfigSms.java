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
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbConfigSms extends SDbRegistryUser{
    
    protected int mnPkConfigSmsId;
    protected String msUrlSms;
    protected double mdVmInMaxVariationPercent;
    protected double mdVmInMaxVariationWeight;
    protected double mdVmOutMaxVariationPercent;
    protected double mdVmOutMaxVariationWeight;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbConfigSms () {
        super(SModConsts.S_CFG);
    }
    
    /*
     * Public methods
     */
    
    public void setPkConfigSmsId(int n) { mnPkConfigSmsId = n; }
    public void setUrlSms(String s) { msUrlSms = s; }
    public void setVmInMaxVariationPercent(double d) { mdVmInMaxVariationPercent = d; }
    public void setVmInMaxVariationWeight(double d) { mdVmInMaxVariationWeight = d; }
    public void setVmOutMaxVariationPercent(double d) { mdVmOutMaxVariationPercent = d; }
    public void setVmOutMaxVariationWeight(double d) { mdVmOutMaxVariationWeight = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkConfigSmsId() { return mnPkConfigSmsId; }
    public String getUrlSms() { return msUrlSms; }
    public double getVmInMaxVariationPercent() { return mdVmInMaxVariationPercent; }
    public double getVmInMaxVariationWeight() { return mdVmInMaxVariationWeight; }
    public double getVmOutMaxVariationPercent() { return mdVmOutMaxVariationPercent; }
    public double getVmOutMaxVariationWeight() { return mdVmOutMaxVariationWeight; }
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
        mnPkConfigSmsId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkConfigSmsId };
    }

    @Override
    public void initRegistry() {
        
        initBaseRegistry();
        
        mnPkConfigSmsId = 0;
        msUrlSms = "";
        mdVmInMaxVariationPercent = 0;
        mdVmInMaxVariationWeight = 0;
        mdVmOutMaxVariationPercent = 0;
        mdVmOutMaxVariationWeight = 0;
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
        return "WHERE id_cfg = " + mnPkConfigSmsId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cfg = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkConfigSmsId = 0;

        msSql = "SELECT COALESCE(MAX(id_cfg), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkConfigSmsId = resultSet.getInt(1);
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
            mnPkConfigSmsId = resultSet.getInt("id_cfg");
            msUrlSms = resultSet.getString("url_sms");
            mdVmInMaxVariationPercent = resultSet.getDouble("wm_in_max_var_pct");
            mdVmInMaxVariationWeight = resultSet.getDouble("wm_in_max_var_weight");
            mdVmOutMaxVariationPercent = resultSet.getDouble("wm_out_max_var_pct");
            mdVmOutMaxVariationWeight = resultSet.getDouble("wm_out_max_var_weight");
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
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                mnPkConfigSmsId + ", " + 
                "'" + msUrlSms + "', " + 
                mdVmInMaxVariationPercent + ", " + 
                mdVmInMaxVariationWeight + ", " + 
                mdVmOutMaxVariationPercent + ", " + 
                mdVmOutMaxVariationWeight + ", " + 
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
                //"id_cfg = " + mnPkConfigSmsId + ", " +
                "url_sms = '" + msUrlSms + "', " +
                "wm_in_max_var_pct = " + mdVmInMaxVariationPercent + ", " +
                "wm_in_max_var_weight = " + mdVmInMaxVariationWeight + ", " +
                "wm_out_max_var_pct = " + mdVmOutMaxVariationPercent + ", " +
                "wm_out_max_var_weight = " + mdVmOutMaxVariationWeight + ", " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                //"ts_usr_ins = " + "NOW()" + ", " +
                "ts_usr_upd = " + "NOW()" + ", " +
                 getSqlWhere();
        }
        
        session.getStatement().execute(msSql);

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbConfigSms clone() throws CloneNotSupportedException {
        SDbConfigSms  registry = new SDbConfigSms();
        
        registry.setPkConfigSmsId(this.getPkConfigSmsId());
        registry.setUrlSms(this.getUrlSms());
        registry.setVmInMaxVariationPercent(this.getVmInMaxVariationPercent());
        registry.setVmInMaxVariationWeight(this.getVmInMaxVariationWeight());
        registry.setVmOutMaxVariationPercent(this.getVmOutMaxVariationPercent());
        registry.setVmOutMaxVariationWeight(this.getVmOutMaxVariationWeight());
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
