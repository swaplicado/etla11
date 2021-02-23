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
public class SDbExtraCharge extends SDbRegistryUser {

    protected int mnPkExtraChargeId;
    protected int mnDesItemId;
    protected int mnDesUnitOfMeasureId;
    protected String msCode;
    protected String msName;
    protected double mdChargePercentage;
    protected boolean mbActive;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbExtraCharge() {
        super(SModConsts.A_CHARGE);
    }

    /*
     * Public methods
     */

    public void setPkExtraChargeId(int n) { mnPkExtraChargeId = n; }
    public void setDesItemId(int n) { mnDesItemId = n; }
    public void setDesUnitOfMeasureId(int n) { mnDesUnitOfMeasureId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setChargePercentage(double d) { mdChargePercentage = d; }
    public void setActive(boolean b) { mbActive = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkExtraChargeId() { return mnPkExtraChargeId; }
    public int getDesItemId() { return mnDesItemId; }
    public int getDesUnitOfMeasureId() { return mnDesUnitOfMeasureId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public double getChargePercentage() { return mdChargePercentage; }
    public boolean isActive() { return mbActive; }
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
        mnPkExtraChargeId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkExtraChargeId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkExtraChargeId = 0;
        mnDesItemId = 0;
        mnDesUnitOfMeasureId = 0;
        msCode = "";
        msName = "";
        mdChargePercentage = 0;
        mbActive = false;
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
        return "WHERE id_charge = " + mnPkExtraChargeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_charge = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkExtraChargeId = 0;

        msSql = "SELECT COALESCE(MAX(id_charge), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkExtraChargeId = resultSet.getInt(1);
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
            mnPkExtraChargeId = resultSet.getInt("id_charge");
            mnDesItemId = resultSet.getInt("des_itm_id");
            mnDesUnitOfMeasureId = resultSet.getInt("des_uom_id");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mdChargePercentage = resultSet.getDouble("charge_pct");
            mbActive = resultSet.getBoolean("b_act");
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
        SDbConfigAvista configAvista = null;
        
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
                    mnPkExtraChargeId + ", " + 
                    mnDesItemId + ", " + 
                    mnDesUnitOfMeasureId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    mdChargePercentage + ", " + 
                    (mbActive ? 1 : 0) + ", " + 
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
                    //"id_charge = " + mnPkExtraChargeId + ", " +
                    "des_itm_id = " + mnDesItemId + ", " +
                    "des_uom_id = " + mnDesUnitOfMeasureId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "charge_pct = " + mdChargePercentage + ", " +
                    "b_act = " + (mbActive ? 1 : 0) + ", " +
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
    public SDbExtraCharge clone() throws CloneNotSupportedException {
        SDbExtraCharge registry = new SDbExtraCharge();

        registry.setPkExtraChargeId(this.getPkExtraChargeId());
        registry.setDesItemId(this.getDesItemId());
        registry.setDesUnitOfMeasureId(this.getDesUnitOfMeasureId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setChargePercentage(this.getChargePercentage());
        registry.setActive(this.isActive());
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
