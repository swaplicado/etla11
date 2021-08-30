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
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbShipmentPrinting extends SDbRegistryUser {

    protected int mnPkShipmentId;
    protected int mnPkPrintingId;
    protected int mnFkUserPrintingId;
    protected Date mtTsUserPrinting;

    public SDbShipmentPrinting () {
        super(SModConsts.S_SHIPT_PRT);
    }

    /*
     * Public methods
     */

    public void setPkShipmentId(int n) { mnPkShipmentId = n; }
    public void setPkPrintingId(int n) { mnPkPrintingId = n; }
    public void setFkUserPrintingId(int n) { mnFkUserPrintingId = n; }
    public void setTsUserPrinting(Date t) { mtTsUserPrinting = t; }

    public int getPkShipmentId() { return mnPkShipmentId; }
    public int getPkPrintingId() { return mnPkPrintingId; }
    public int getFkUserPrintingId() { return mnFkUserPrintingId; }
    public Date getTsUserPrinting() { return mtTsUserPrinting; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkShipmentId = pk[0];
        mnPkPrintingId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkShipmentId, mnPkPrintingId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkShipmentId = 0;
        mnPkPrintingId = 0;
        mnFkUserPrintingId = 0;
        mtTsUserPrinting = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_shipt = " + mnPkShipmentId + " AND "
                + "id_prt = " + mnPkPrintingId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_shipt = " + pk[0] + " AND "
                + "id_prt = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkPrintingId = 0;

        msSql = "SELECT COALESCE(MAX(id_prt), 0) + 1 FROM " + getSqlTable() + " WHERE id_shipt = " + mnPkShipmentId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkPrintingId = resultSet.getInt(1);
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
            mnPkShipmentId = resultSet.getInt("id_shipt");
            mnPkPrintingId = resultSet.getInt("id_prt");
            mnFkUserPrintingId = resultSet.getInt("fk_usr_prt");
            mtTsUserPrinting = resultSet.getTimestamp("ts_usr_prt");

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
            mnFkUserPrintingId = session.getUser().getPkUserId();

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                mnPkShipmentId + ", " + 
                mnPkPrintingId + ", " + 
                mnFkUserPrintingId + ", " + 
                "NOW()" + " " + 
                ")";
        }
        else {
            throw new Exception(SDbConsts.ERR_MSG_REG_NON_UPDATABLE);
        }

        session.getStatement().execute(msSql);

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbShipmentPrinting clone() throws CloneNotSupportedException {
        SDbShipmentPrinting registry = new SDbShipmentPrinting();

        registry.setPkShipmentId(this.getPkShipmentId());
        registry.setPkPrintingId(this.getPkPrintingId());
        registry.setFkUserPrintingId(this.getFkUserPrintingId());
        registry.setTsUserPrinting(this.getTsUserPrinting());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
