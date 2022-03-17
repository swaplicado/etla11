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
 * @author Sergio Flores, Alfredo Pérez, Isabel Servín
 */
public class SDbConfigSms extends SDbRegistryUser{

    protected int mnPkConfigSmsId;
    protected String msUrlSms;
    protected double mdWmInMaxVariationPercent;
    protected double mdWmInMaxVariationWeight;
    protected double mdWmOutMaxVariationPercent;
    protected double mdWmOutMaxVariationWeight;
    protected boolean mbWmTicketValidation;
    protected String msMailWmInTo;
    protected String msMailWmInBcc;
    protected String msMailWmOutTo;
    protected String msMailWmOutBcc;
    protected String msRevueltaHost;
    protected int mnRevueltaPort;
    protected String msRevueltaName;
    protected String msRevueltaUser;
    protected String msRevueltaPassword;
    protected String msWebUrl;
    protected String msWebUser;
    protected String msWebPassword;
    protected String msWebLocationId;
    protected int mnShipperConfig;
    protected boolean mbShiptmentMail;

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
    public void setWmInMaxVariationPercent(double d) { mdWmInMaxVariationPercent = d; }
    public void setWmInMaxVariationWeight(double d) { mdWmInMaxVariationWeight = d; }
    public void setWmOutMaxVariationPercent(double d) { mdWmOutMaxVariationPercent = d; }
    public void setWmOutMaxVariationWeight(double d) { mdWmOutMaxVariationWeight = d; }
    public void setWmTicketValidation(boolean b) { mbWmTicketValidation = b; }
    public void setMailWmInTo(String s) { msMailWmInTo = s; }
    public void setMailWmInBcc(String s) { msMailWmInBcc = s; }
    public void setMailWmOutTo(String s) { msMailWmOutTo = s; }
    public void setMailWmOutBcc(String s) { msMailWmOutBcc = s; }
    public void setRevueltaHost(String s) { msRevueltaHost = s; }
    public void setRevueltaPort(int n) { mnRevueltaPort = n; }
    public void setRevueltaName(String s) { msRevueltaName = s; }
    public void setRevueltaUser(String s) { msRevueltaUser = s; }
    public void setRevueltaPassword(String s) { msRevueltaPassword = s; }
    public void setWebUrl(String s) { msWebUrl = s; }
    public void setWebUser(String s) { msWebUser = s; }
    public void setWebPassword(String s) { msWebPassword = s; }
    public void setWebLocationId(String s) { msWebLocationId = s; }
    public void setShipperConfig(int n) { mnShipperConfig = n; }
    public void setShiptmentMail(boolean b) { mbShiptmentMail = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkConfigSmsId() { return mnPkConfigSmsId; }
    public String getUrlSms() { return msUrlSms; }
    public double getWmInMaxVariationPercent() { return mdWmInMaxVariationPercent; }
    public double getWmInMaxVariationWeight() { return mdWmInMaxVariationWeight; }
    public double getWmOutMaxVariationPercent() { return mdWmOutMaxVariationPercent; }
    public double getWmOutMaxVariationWeight() { return mdWmOutMaxVariationWeight; }
    public boolean isWmTicketValidation() { return mbWmTicketValidation; }
    public String getMailWmInTo() { return msMailWmInTo; }
    public String getMailWmInBcc() { return msMailWmInBcc; }
    public String getMailWmOutTo() { return msMailWmOutTo; }
    public String getMailWmOutBcc() { return msMailWmOutBcc; }
    public String getRevueltaHost() { return msRevueltaHost; }
    public int getRevueltaPort() { return mnRevueltaPort; }
    public String getRevueltaName() { return msRevueltaName; }
    public String getRevueltaUser() { return msRevueltaUser; }
    public String getRevueltaPassword() { return msRevueltaPassword; }
    public String getWebUrl() { return msWebUrl; }
    public String getWebUser() { return msWebUser; }
    public String getWebPassword() { return msWebPassword; }
    public String getWebLocationId() { return msWebLocationId; }
    public int getShipperConfig() { return mnShipperConfig; }
    public boolean isShiptmentMail() { return mbShiptmentMail; }
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
        mdWmInMaxVariationPercent = 0;
        mdWmInMaxVariationWeight = 0;
        mdWmOutMaxVariationPercent = 0;
        mdWmOutMaxVariationWeight = 0;
        mbWmTicketValidation = false;
        msMailWmInTo = "";
        msMailWmInBcc = "";
        msMailWmOutTo = "";
        msMailWmOutBcc = "";
        msRevueltaHost = "";
        mnRevueltaPort = 0;
        msRevueltaName = "";
        msRevueltaUser = "";
        msRevueltaPassword = "";
        msWebUrl = "";
        msWebUser = "";
        msWebPassword = "";
        msWebLocationId = "";
        mnShipperConfig = 0;
        mbShiptmentMail = false;
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
            mdWmInMaxVariationPercent = resultSet.getDouble("wm_in_max_var_pct");
            mdWmInMaxVariationWeight = resultSet.getDouble("wm_in_max_var_weight");
            mdWmOutMaxVariationPercent = resultSet.getDouble("wm_out_max_var_pct");
            mdWmOutMaxVariationWeight = resultSet.getDouble("wm_out_max_var_weight");
            mbWmTicketValidation = resultSet.getBoolean("wm_ticket_val");
            msMailWmInTo = resultSet.getString("mail_wm_in_to");
            msMailWmInBcc = resultSet.getString("mail_wm_in_bcc");
            msMailWmOutTo = resultSet.getString("mail_wm_out_to");
            msMailWmOutBcc = resultSet.getString("mail_wm_out_bcc");
            msRevueltaHost = resultSet.getString("rev_host");
            mnRevueltaPort = resultSet.getInt("rev_port");
            msRevueltaName = resultSet.getString("rev_name");
            msRevueltaUser = resultSet.getString("rev_user");
            msRevueltaPassword = resultSet.getString("rev_pswd");
            msWebUrl = resultSet.getString("web_url");
            msWebUser = resultSet.getString("web_user");
            msWebPassword = resultSet.getString("web_pswd");
            msWebLocationId = resultSet.getString("web_loc_id");
            mnShipperConfig = resultSet.getInt("shipper_cfg");
            mbShiptmentMail = resultSet.getBoolean("b_shipt_mail");
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
                mdWmInMaxVariationPercent + ", " + 
                mdWmInMaxVariationWeight + ", " + 
                mdWmOutMaxVariationPercent + ", " + 
                mdWmOutMaxVariationWeight + ", " + 
                (mbWmTicketValidation ? 1 : 0) + ", " + 
                "'" + msMailWmInTo + "', " + 
                "'" + msMailWmInBcc + "', " + 
                "'" + msMailWmOutTo + "', " + 
                "'" + msMailWmOutBcc + "', " + 
                "'" + msRevueltaHost + "', " + 
                mnRevueltaPort + ", " + 
                "'" + msRevueltaName + "', " + 
                "'" + msRevueltaUser + "', " + 
                "'" + msRevueltaPassword + "', " + 
                "'" + msWebUrl + "', " + 
                "'" + msWebUser + "', " + 
                "'" + msWebPassword + "', " + 
                "'" + msWebLocationId + "', " + 
                mnShipperConfig + ", " + 
                (mbShiptmentMail ? 1 : 0) + ", " + 
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
                "wm_in_max_var_pct = " + mdWmInMaxVariationPercent + ", " +
                "wm_in_max_var_weight = " + mdWmInMaxVariationWeight + ", " +
                "wm_out_max_var_pct = " + mdWmOutMaxVariationPercent + ", " +
                "wm_out_max_var_weight = " + mdWmOutMaxVariationWeight + ", " +
                "wm_ticket_val = " + (mbWmTicketValidation ? 1 : 0) + ", " +
                "mail_wm_in_to = '" + msMailWmInTo + "', " +
                "mail_wm_in_bcc = '" + msMailWmInBcc + "', " +
                "mail_wm_out_to = '" + msMailWmOutTo + "', " +
                "mail_wm_out_bcc = '" + msMailWmOutBcc + "', " +
                "rev_host = '" + msRevueltaHost + "', " +
                "rev_port = " + mnRevueltaPort + ", " +
                "rev_name = '" + msRevueltaName + "', " +
                "rev_user = '" + msRevueltaUser + "', " +
                "rev_pswd = '" + msRevueltaPassword + "', " +
                "web_url = '" + msWebUrl + "', " +
                "web_user = '" + msWebUser + "', " +
                "web_pswd = '" + msWebPassword + "', " +
                "web_loc_id = '" + msWebLocationId + "', " +
                "shipper_cfg = " + mnShipperConfig + ", " +
                "b_shipt_mail = " + (mbShiptmentMail ? 1 : 0) + ", " +
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
        registry.setWmInMaxVariationPercent(this.getWmInMaxVariationPercent());
        registry.setWmInMaxVariationWeight(this.getWmInMaxVariationWeight());
        registry.setWmOutMaxVariationPercent(this.getWmOutMaxVariationPercent());
        registry.setWmOutMaxVariationWeight(this.getWmOutMaxVariationWeight());
        registry.setWmTicketValidation(this.isWmTicketValidation());
        registry.setMailWmInTo(this.getMailWmInTo());
        registry.setMailWmInBcc(this.getMailWmInBcc());
        registry.setMailWmOutTo(this.getMailWmOutTo());
        registry.setMailWmOutBcc(this.getMailWmOutBcc());
        registry.setRevueltaHost(this.getRevueltaHost());
        registry.setRevueltaPort(this.getRevueltaPort());
        registry.setRevueltaName(this.getRevueltaName());
        registry.setRevueltaUser(this.getRevueltaUser());
        registry.setRevueltaPassword(this.getRevueltaPassword());
        registry.setShipperConfig(this.getShipperConfig());
        registry.setShiptmentMail(this.isShiptmentMail());
        registry.setWebUrl(this.getWebUrl());
        registry.setWebUser(this.getWebUser());
        registry.setWebPassword(this.getWebPassword());
        registry.setWebLocationId(this.getWebLocationId());
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
