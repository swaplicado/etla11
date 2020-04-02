/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package etla.mod.cfg.db;

import etla.gui.SGuiMainSessionCustom;
import etla.mod.SModConsts;
import etla.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;
import sa.lib.gui.SGuiSessionCustom;
import sa.lib.gui.SGuiUser;

/**
 *
 * @author Sergio Flores
 */
public class SDbUser extends SDbRegistryUser implements SGuiUser {

    public static final int FIELD_PASSWORD = 1;

    protected int mnPkUserId;
    protected int mnDesUserId;
    protected String msName;
    protected String msPassword;
    protected boolean mbRightEtl;
    protected boolean mbWeb;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkUserTypeId;
    protected int mnFkWebRoleId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected boolean mbAuxClearPasswordOnSave;

    public SDbUser() {
        super(SModConsts.CU_USR);
    }

    /*
     * Public methods
     */
    
    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setDesUserId(int n) { mnDesUserId = n; }
    public void setName(String s) { msName = s; }
    public void setPassword(String s) { msPassword = s; }
    public void setRightEtl (boolean b) { mbRightEtl  = b; }
    public void setWeb(boolean b) { mbWeb = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserTypeId(int n) { mnFkUserTypeId = n; }
    public void setFkWebRoleId(int n) { mnFkWebRoleId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkUserId() { return mnPkUserId; }
    public int getDesUserId() { return mnDesUserId; }
    public String getName() { return msName; }
    public String getPassword() { return msPassword; }
    public boolean isRightEtl () { return mbRightEtl ; }
    public boolean isWeb() { return mbWeb; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserTypeId() { return mnFkUserTypeId; }
    public int getFkWebRoleId() { return mnFkWebRoleId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setAuxClearPasswordOnSave(boolean b) { mbAuxClearPasswordOnSave = b; }

    public boolean isAuxClearPasswordOnSave() { return mbAuxClearPasswordOnSave; }
    
    /*
     * Overriden methods
     */
    
    @Override
    public boolean isAdministrator() {
        return isSupervisor() || mnFkUserTypeId == SModSysConsts.CS_USR_TP_ADM;
    }

    @Override
    public boolean isSupervisor() {
        return mnFkUserTypeId == SModSysConsts.CS_USR_TP_SUP;
    }

    @Override
    public boolean hasModuleAccess(final int module) {
        boolean access = false;

        switch (module) {
            case SModConsts.MOD_CFG:
                access = isAdministrator();
                break;
            case SModConsts.MOD_ETL:
                access = true;
                break;
            default:
        }

        return access;
    }

    @Override
    public boolean hasPrivilege(final int privilege) {
        boolean hasPrivilege = false;

        if (isSupervisor()) {
            hasPrivilege = true;
        }
        else if (isAdministrator()) {
            hasPrivilege = true;
        }

        return hasPrivilege;
    }

    @Override
    public boolean hasPrivilege(final int[] privileges) {
        boolean hasPrivilege = false;

        for (int privilege : privileges) {
            if (hasPrivilege(privilege)) {
                hasPrivilege = true;
                break;
            }
        }

        return hasPrivilege;
    }

    @Override
    public int getPrivilegeLevel(final int privilege) {
        return SUtilConsts.LEV_MANAGER;
    }

    @Override
    public HashMap<Integer, Integer> getPrivilegesMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HashSet<Integer> getModulesSet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void computeAccess(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiSessionCustom createSessionCustom(SGuiClient client) {
        return new SGuiMainSessionCustom(client);
    }

    @Override
    public SGuiSessionCustom createSessionCustom(SGuiClient client, int terminal) {
        return new SGuiMainSessionCustom(client, 0);
    }

    @Override
    public boolean showUserSessionConfigOnLogin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkUserId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkUserId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkUserId = 0;
        mnDesUserId = 0;
        msName = "";
        msPassword = "";
        mbRightEtl  = false;
        mbWeb = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkUserTypeId = 0;
        mnFkWebRoleId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        mbAuxClearPasswordOnSave = false;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_usr = " + mnPkUserId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_usr = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkUserId = 0;

        msSql = "SELECT COALESCE(MAX(id_usr), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkUserId = resultSet.getInt(1);
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
            mnPkUserId = resultSet.getInt("id_usr");
            mnDesUserId = resultSet.getInt("des_usr_id");
            msName = resultSet.getString("name");
            //msPassword = resultSet.getString("pswd");     // stored value is a string digestion, so it is useless
            mbRightEtl  = resultSet.getBoolean("b_right_etl");
            mbWeb = resultSet.getBoolean("b_web");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserTypeId = resultSet.getInt("fk_usr_tp");
            mnFkWebRoleId = resultSet.getInt("fk_web_role");
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
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (mbAuxClearPasswordOnSave) {
            msPassword = "";
        }

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
                    mnPkUserId + ", " +
                    mnDesUserId + ", " + 
                    "'" + msName + "', " +
                    (mbAuxClearPasswordOnSave ? "''" : "PASSWORD('" + msPassword + "')") + ", " +
                    (mbRightEtl  ? 1 : 0) + ", " + 
                    (mbWeb ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkUserTypeId + ", " +
                    mnFkWebRoleId + ", " + 
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_usr = " + mnPkUserId + ", " +
                    "des_usr_id = " + mnDesUserId + ", " +
                    "name = '" + msName + "', " +
                    (mbAuxClearPasswordOnSave ? "pswd = '', " : (msPassword.isEmpty() ? "" : "pswd = PASSWORD('" + msPassword + "'), ")) +
                    "b_right_etl = " + (mbRightEtl  ? 1 : 0) + ", " +
                    "b_web = " + (mbWeb ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_usr_tp = " + mnFkUserTypeId + ", " +
                    "fk_web_role = " + mnFkWebRoleId + ", " +
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
    public SDbUser clone() throws CloneNotSupportedException {
        SDbUser registry = new SDbUser();

        registry.setPkUserId(this.getPkUserId());
        registry.setDesUserId(this.getDesUserId());
        registry.setName(this.getName());
        registry.setPassword(this.getPassword());
        registry.setRightEtl (this.isRightEtl ());
        registry.setWeb(this.isWeb());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserTypeId(this.getFkUserTypeId());
        registry.setFkWebRoleId(this.getFkWebRoleId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setAuxClearPasswordOnSave(this.isAuxClearPasswordOnSave());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_PASSWORD:
                msSql += "pswd = PASSWORD('" + value + "') ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);
        mnQueryResultId = SDbConsts.SAVE_OK;
    }
}
