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
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbExtraChargePeriod extends SDbRegistryUser {

    protected int mnPkPeriodId;
    protected Date mtDateStart;
    protected Date mtDateEnd_n;
    protected double mdChargePercentage;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkExtraChargeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected SDbExtraCharge moDbmsExtraCharge;
    
    public SDbExtraChargePeriod() {
        super(SModConsts.A_CHARGE_PERIOD);
    }

    /*
     * Public methods
     */

    public void setPkPeriodId(int n) { mnPkPeriodId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd_n(Date t) { mtDateEnd_n = t; }
    public void setChargePercentage(double d) { mdChargePercentage = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkExtraChargeId(int n) { mnFkExtraChargeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkPeriodId() { return mnPkPeriodId; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd_n() { return mtDateEnd_n; }
    public double getChargePercentage() { return mdChargePercentage; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkExtraChargeId() { return mnFkExtraChargeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setDbmsExtraCharge(SDbExtraCharge o) { moDbmsExtraCharge = o; }
    
    public SDbExtraCharge getDbmsExtraCharge() { return moDbmsExtraCharge; }
    
    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPeriodId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPeriodId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkPeriodId = 0;
        mtDateStart = null;
        mtDateEnd_n = null;
        mdChargePercentage = 0;
        mbDeleted = false;
        mbSystem = false;
        mnFkExtraChargeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        moDbmsExtraCharge = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_period = " + mnPkPeriodId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_period = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkPeriodId = 0;

        msSql = "SELECT COALESCE(MAX(id_period), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkPeriodId = resultSet.getInt(1);
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
            mnPkPeriodId = resultSet.getInt("id_period");
            mtDateStart = resultSet.getTimestamp("dt_sta");
            mtDateEnd_n = resultSet.getTimestamp("dt_end_n");
            mdChargePercentage = resultSet.getDouble("charge_pct");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkExtraChargeId = resultSet.getInt("fk_charge");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            moDbmsExtraCharge = (SDbExtraCharge) session.readRegistry(SModConsts.A_CHARGE, new int[] { mnFkExtraChargeId });

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
                    mnPkPeriodId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(mtDateStart) + "', " + 
                    (mtDateEnd_n == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDatetime.format(mtDateEnd_n) + "'") + ", " + 
                    mdChargePercentage + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkExtraChargeId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    "id_period = " + mnPkPeriodId + ", " +
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDatetime.format(mtDateStart) + "', " +
                    "dt_end_n = " + (mtDateEnd_n == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDatetime.format(mtDateEnd_n) + "'") + ", " +
                    "charge_pct = " + mdChargePercentage + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_charge = " + mnFkExtraChargeId + ", " +
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
    public SDbExtraChargePeriod clone() throws CloneNotSupportedException {
        SDbExtraChargePeriod registry = new SDbExtraChargePeriod();

        registry.setPkPeriodId(this.getPkPeriodId());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd_n(this.getDateEnd_n());
        registry.setChargePercentage(this.getChargePercentage());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkExtraChargeId(this.getFkExtraChargeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        if (this.getDbmsExtraCharge() != null) {
            registry.setDbmsExtraCharge(this.getDbmsExtraCharge().clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
