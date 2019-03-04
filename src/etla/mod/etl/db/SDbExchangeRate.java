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
public class SDbExchangeRate extends SDbRegistryUser {

    protected int mnPkCurrencyId;
    protected int mnPkExchangeRateId;
    protected Date mtDate;
    protected double mdExchangeRate;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbExchangeRate() {
        super(SModConsts.A_EXR);
    }

    /*
     * Public methods
     */

    public void setPkCurrencyId(int n) { mnPkCurrencyId = n; }
    public void setPkExchangeRateId(int n) { mnPkExchangeRateId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkCurrencyId() { return mnPkCurrencyId; }
    public int getPkExchangeRateId() { return mnPkExchangeRateId; }
    public Date getDate() { return mtDate; }
    public double getExchangeRate() { return mdExchangeRate; }
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
        mnPkCurrencyId = pk[0];
        mnPkExchangeRateId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCurrencyId, mnPkExchangeRateId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCurrencyId = 0;
        mnPkExchangeRateId = 0;
        mtDate = null;
        mdExchangeRate = 0;
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
        return "WHERE id_cur = " + mnPkCurrencyId + " AND "
                + "id_exr = " + mnPkExchangeRateId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cur = " + pk[0] + " AND "
                + "id_exr = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkExchangeRateId = 0;

        msSql = "SELECT COALESCE(MAX(id_exr), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_cur = " + mnPkCurrencyId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkExchangeRateId = resultSet.getInt(1);
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
            mnPkCurrencyId = resultSet.getInt("id_cur");
            mnPkExchangeRateId = resultSet.getInt("id_exr");
            mtDate = resultSet.getDate("dat");
            mdExchangeRate = resultSet.getDouble("exr");
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
                    mnPkCurrencyId + ", " + 
                    mnPkExchangeRateId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                    mdExchangeRate + ", " + 
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
                    //"id_cur = " + mnPkCurrencyId + ", " +
                    "id_exr = " + mnPkExchangeRateId + ", " +
                    "dat = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "exr = " + mdExchangeRate + ", " +
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
    public SDbExchangeRate clone() throws CloneNotSupportedException {
        SDbExchangeRate registry = new SDbExchangeRate();

        registry.setPkCurrencyId(this.getPkCurrencyId());
        registry.setPkExchangeRateId(this.getPkExchangeRateId());
        registry.setDate(this.getDate());
        registry.setExchangeRate(this.getExchangeRate());
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
