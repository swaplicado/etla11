/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

import etla.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistrySys;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbSysCurrency extends SDbRegistrySys {

    protected int mnPkCurrencyId;
    protected int mnSrcCurrencyId;
    protected int mnDesCurrencyId;
    protected String msCode;
    protected String msName;
    /*
    protected int mnSortingPos;
    protected boolean mbDeleted;
    protected int mnFkUserId;
    protected Date mtTsUser;
    */
    
    public SDbSysCurrency() {
        super(SModConsts.AS_CUR);
    }

    /*
     * Public methods
     */

    public void setPkCurrencyId(int n) { mnPkCurrencyId = n; }
    public void setSrcCurrencyId(int n) { mnSrcCurrencyId = n; }
    public void setDesCurrencyId(int n) { mnDesCurrencyId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setSortingPos(int n) { mnSortingPos = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }

    public int getPkCurrencyId() { return mnPkCurrencyId; }
    public int getSrcCurrencyId() { return mnSrcCurrencyId; }
    public int getDesCurrencyId() { return mnDesCurrencyId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public int getSortingPos() { return mnSortingPos; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCurrencyId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCurrencyId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCurrencyId = 0;
        mnSrcCurrencyId = 0;
        mnDesCurrencyId = 0;
        msCode = "";
        msName = "";
        mnSortingPos = 0;
        mbDeleted = false;
        mnFkUserId = 0;
        mtTsUser = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_cur = " + mnPkCurrencyId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cur = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
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
            mnSrcCurrencyId = resultSet.getInt("src_cur_id");
            mnDesCurrencyId = resultSet.getInt("des_cur_id");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mnSortingPos = resultSet.getInt("sort");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbSysCurrency clone() throws CloneNotSupportedException {
        SDbSysCurrency registry = new SDbSysCurrency();

        registry.setPkCurrencyId(this.getPkCurrencyId());
        registry.setSrcCurrencyId(this.getSrcCurrencyId());
        registry.setDesCurrencyId(this.getDesCurrencyId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setSortingPos(this.getSortingPos());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserId(this.getFkUserId());
        registry.setTsUser(this.getTsUser());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
