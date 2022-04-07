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
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbShipmentLog extends SDbRegistryUser {
    
    protected int mnPkShipmentLogId;
    protected int mnFileType;
    protected String msMail;
    protected String msResponse;
    protected int mnFkShipmentId;
    protected int mnFkUserId;
    protected Date mtTsUser;

    public SDbShipmentLog() {
        super(SModConsts.S_SHIPT_LOG);
    }
    
    /*
     * Public methods
     */
    
    public void setPkShipmentLogId(int n) { mnPkShipmentLogId = n; }
    public void setFileType(int n) { mnFileType = n; }
    public void setMail(String s) { msMail = s; }
    public void setResponse(String s) { msResponse = s; }
    public void setFkShipmentId(int n) { mnFkShipmentId = n; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }


    public int getPkShipmentLogId() { return mnPkShipmentLogId; }
    public int getFileType() { return mnFileType; }
    public String getMail() { return msMail; }
    public String getResponse() { return msResponse; }
    public int getFkShipmentId() { return mnFkShipmentId; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }
    
    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkShipmentLogId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkShipmentLogId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkShipmentLogId = 0;
        mnFileType = 0;
        msMail = "";
        msResponse = "";
        mnFkShipmentId = 0;
        mnFkUserId = 0;
        mtTsUser = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_shipt_log = " + mnPkShipmentLogId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_shipt_log = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkShipmentLogId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_shipt_log), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkShipmentLogId = resultSet.getInt(1);
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
            mnPkShipmentLogId = resultSet.getInt("id_shipt_log");
            mnFileType = resultSet.getInt("file_tp");
            msMail = resultSet.getString("mail");
            msResponse = resultSet.getString("response");
            mnFkShipmentId = resultSet.getInt("fk_shipt");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");

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
            mnFkUserId = session.getUser().getPkUserId();
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                mnPkShipmentLogId + ", " + 
                mnFileType + ", " + 
                "'" + msMail + "', " + 
                "'" + msResponse + "', " + 
                mnFkShipmentId + ", " + 
                mnFkUserId + ", " + 
                "NOW()" + " " + 
                ")" ;
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
//                "id_shipt_log = " + mnPkShipmentLogId + ", " +
                "file_tp = " + mnFileType + ", " +
                "mail = '" + msMail + "', " +
                "response = '" + msResponse + "', " +
                "fk_shipt = " + mnFkShipmentId + ", " +
                "fk_usr = " + mnFkUserId + ", " +
                "ts_usr = " + "NOW()" + " " +
                getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbShipmentLog registry = new SDbShipmentLog();
        
        registry.setPkShipmentLogId(this.getPkShipmentLogId());
        registry.setFileType(this.getFileType());
        registry.setMail(this.getMail());
        registry.setResponse(this.getResponse());
        registry.setFkShipmentId(this.getFkShipmentId());
        registry.setFkUserId(this.getFkUserId());
        registry.setTsUser(this.getTsUser());

        return registry;
    }
    
}
