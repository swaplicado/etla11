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
public class SDbSysUnitOfMeasure extends SDbRegistrySys {

    protected int mnPkUnitOfMeasureId;
    protected String msSrcUnitOfMeasureId;
    protected int mnDesUnitOfMeasureId;
    protected String msCode;
    protected String msName;
    protected String msType;
    protected String msUnit;
    protected String msBaseUnit;
    protected double mdConversionFactor;
    /*
    protected int mnSortingPos;
    protected boolean mbDeleted;
    protected int mnFkUserId;
    protected Date mtTsUser;
    */
    
    public SDbSysUnitOfMeasure() {
        super(SModConsts.AS_UOM);
    }

    /*
     * Public methods
     */

    public void setPkUnitOfMeasureId(int n) { mnPkUnitOfMeasureId = n; }
    public void setSrcUnitOfMeasureId(String s) { msSrcUnitOfMeasureId = s; }
    public void setDesUnitOfMeasureId(int n) { mnDesUnitOfMeasureId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setType(String s) { msType = s; }
    public void setUnit(String s) { msUnit = s; }
    public void setBaseUnit(String s) { msBaseUnit = s; }
    public void setConversionFactor(double d) { mdConversionFactor = d; }
    public void setSortingPos(int n) { mnSortingPos = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }

    public int getPkUnitOfMeasureId() { return mnPkUnitOfMeasureId; }
    public String getSrcUnitOfMeasureId() { return msSrcUnitOfMeasureId; }
    public int getDesUnitOfMeasureId() { return mnDesUnitOfMeasureId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getType() { return msType; }
    public String getUnit() { return msUnit; }
    public String getBaseUnit() { return msBaseUnit; }
    public double getConversionFactor() { return mdConversionFactor; }
    public int getSortingPos() { return mnSortingPos; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkUnitOfMeasureId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkUnitOfMeasureId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkUnitOfMeasureId = 0;
        msSrcUnitOfMeasureId = "";
        mnDesUnitOfMeasureId = 0;
        msCode = "";
        msName = "";
        msType = "";
        msUnit = "";
        msBaseUnit = "";
        mdConversionFactor = 0;
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
        return "WHERE id_uom = " + mnPkUnitOfMeasureId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_uom = " + pk[0] + " ";
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
            mnPkUnitOfMeasureId = resultSet.getInt("id_uom");
            msSrcUnitOfMeasureId = resultSet.getString("src_uom_id");
            mnDesUnitOfMeasureId = resultSet.getInt("des_uom_id");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msType = resultSet.getString("type");
            msUnit = resultSet.getString("unit");
            msBaseUnit = resultSet.getString("base_unit");
            mdConversionFactor = resultSet.getDouble("conv_fact");
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
    public SDbSysUnitOfMeasure clone() throws CloneNotSupportedException {
        SDbSysUnitOfMeasure registry = new SDbSysUnitOfMeasure();

        registry.setPkUnitOfMeasureId(this.getPkUnitOfMeasureId());
        registry.setSrcUnitOfMeasureId(this.getSrcUnitOfMeasureId());
        registry.setDesUnitOfMeasureId(this.getDesUnitOfMeasureId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setType(this.getType());
        registry.setUnit(this.getUnit());
        registry.setBaseUnit(this.getBaseUnit());
        registry.setConversionFactor(this.getConversionFactor());
        registry.setSortingPos(this.getSortingPos());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserId(this.getFkUserId());
        registry.setTsUser(this.getTsUser());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
