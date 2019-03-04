/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package etla.mod.cfg.db;

import etla.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import sa.lib.gui.SGuiUserGui;

/**
 *
 * @author Sergio Flores
 */
public class SDbUserGui extends SDbRegistryUser implements SGuiUserGui {

    protected int mnPkUserId;
    protected int mnPkGuiId;
    protected int mnPkGuiTypeId;
    protected int mnPkGuiSubtypeId;
    protected int mnPkGuiModeId;
    protected int mnPkGuiSubmodeId;
    protected String msGui;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbUserGui() {
        super(SModConsts.C_USR_GUI);
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkGuiId(int n) { mnPkGuiId = n; }
    public void setPkGuiTypeId(int n) { mnPkGuiTypeId = n; }
    public void setPkGuiSubtypeId(int n) { mnPkGuiSubtypeId = n; }
    public void setPkGuiModeId(int n) { mnPkGuiModeId = n; }
    public void setPkGuiSubmodeId(int n) { mnPkGuiSubmodeId = n; }
    public void setGui(String s) { msGui = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkGuiId() { return mnPkGuiId; }
    public int getPkGuiTypeId() { return mnPkGuiTypeId; }
    public int getPkGuiSubtypeId() { return mnPkGuiSubtypeId; }
    public int getPkGuiModeId() { return mnPkGuiModeId; }
    public int getPkGuiSubmodeId() { return mnPkGuiSubmodeId; }
    public String getGui() { return msGui; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkUserId = pk[0];
        mnPkGuiId = pk[1];
        mnPkGuiTypeId = pk[2];
        mnPkGuiSubtypeId = pk[3];
        mnPkGuiModeId = pk[4];
        mnPkGuiSubmodeId = pk[5];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkUserId, mnPkGuiId, mnPkGuiTypeId, mnPkGuiSubtypeId, mnPkGuiModeId, mnPkGuiSubmodeId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkUserId = 0;
        mnPkGuiId = 0;
        mnPkGuiTypeId = 0;
        mnPkGuiSubtypeId = 0;
        mnPkGuiModeId = 0;
        mnPkGuiSubmodeId = 0;
        msGui = null;
        mbDeleted = false;
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
        return "WHERE id_usr = " + mnPkUserId + " AND " +
                "id_gui = " + mnPkGuiId + " AND " +
                "id_gui_tp = " + mnPkGuiTypeId + " AND " +
                "id_gui_stp = " + mnPkGuiSubtypeId + " AND " +
                "id_gui_md = " + mnPkGuiModeId + " AND " +
                "id_gui_smd = " + mnPkGuiSubmodeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_usr = " + pk[0] + " AND " +
                "id_gui = " + pk[1] + " AND " +
                "id_gui_tp = " + pk[2] + " AND " +
                "id_gui_stp = " + pk[3] + " AND " +
                "id_gui_md = " + pk[4] + " AND " +
                "id_gui_smd = " + pk[5] + " ";
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
            mnPkUserId = resultSet.getInt("id_usr");
            mnPkGuiId = resultSet.getInt("id_gui");
            mnPkGuiTypeId = resultSet.getInt("id_gui_tp");
            mnPkGuiSubtypeId = resultSet.getInt("id_gui_stp");
            mnPkGuiModeId = resultSet.getInt("id_gui_md");
            mnPkGuiSubmodeId = resultSet.getInt("id_gui_smd");
            msGui = resultSet.getString("gui");
            mbDeleted = resultSet.getBoolean("b_del");
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

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkUserId + ", " +
                    mnPkGuiId + ", " +
                    mnPkGuiTypeId + ", " +
                    mnPkGuiSubtypeId + ", " +
                    mnPkGuiModeId + ", " +
                    mnPkGuiSubmodeId + ", " +
                    "'" + msGui + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_usr = " + mnPkUserId + ", " +
                    "id_gui = " + mnPkGuiId + ", " +
                    "id_gui_tp = " + mnPkGuiTypeId + ", " +
                    "id_gui_stp = " + mnPkGuiSubtypeId + ", " +
                    "id_gui_md = " + mnPkGuiModeId + ", " +
                    "id_gui_smd = " + mnPkGuiSubmodeId + ", " +
                    */
                    "gui = '" + msGui + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
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
    public SDbUserGui clone() throws CloneNotSupportedException {
        SDbUserGui registry = new SDbUserGui();

        registry.setPkUserId(this.getPkUserId());
        registry.setPkGuiId(this.getPkGuiId());
        registry.setPkGuiTypeId(this.getPkGuiTypeId());
        registry.setPkGuiSubtypeId(this.getPkGuiSubtypeId());
        registry.setPkGuiModeId(this.getPkGuiModeId());
        registry.setPkGuiSubmodeId(this.getPkGuiSubmodeId());
        registry.setGui(this.getGui());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
