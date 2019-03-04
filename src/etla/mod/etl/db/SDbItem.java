/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

import etla.mod.SModConsts;
import etla.mod.cfg.db.SDbConfig;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbItem extends SDbRegistryUser {

    protected int mnPkItemId;
    protected int mnDesItemId;
    protected String msCode;
    protected String msName;
    protected String msNameBoardType;
    protected String msNameFlute;
    protected int mnSrcBoardTypeFk;
    protected String msSrcFluteFk;
    protected String msSrcCustomerFk_n;
    protected int mnSrcRequiredCurrencyFk_n;
    protected String msSrcRequiredUnitOfMeasureFk_n;
    protected Date mtFirstEtlInsert;
    protected Date mtLastEtlUpdate;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkSrcCustomerId_n;
    protected int mnFkSrcRequiredCurrencyId_n;
    protected int mnFkSrcRequiredUnitOfMeasureId_n;
    protected int mnFkLastEtlLogId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbItem() {
        super(SModConsts.AU_ITM);
    }

    /*
     * Public methods
     */

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setDesItemId(int n) { mnDesItemId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setNameBoardType(String s) { msNameBoardType = s; }
    public void setNameFlute(String s) { msNameFlute = s; }
    public void setSrcBoardTypeFk(int n) { mnSrcBoardTypeFk = n; }
    public void setSrcFluteFk(String s) { msSrcFluteFk = s; }
    public void setSrcCustomerFk_n(String s) { msSrcCustomerFk_n = s; }
    public void setSrcRequiredCurrencyFk_n(int n) { mnSrcRequiredCurrencyFk_n = n; }
    public void setSrcRequiredUnitOfMeasureFk_n(String s) { msSrcRequiredUnitOfMeasureFk_n = s; }
    public void setFirstEtlInsert(Date t) { mtFirstEtlInsert = t; }
    public void setLastEtlUpdate(Date t) { mtLastEtlUpdate = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkSrcCustomerId_n(int n) { mnFkSrcCustomerId_n = n; }
    public void setFkSrcRequiredCurrencyId_n(int n) { mnFkSrcRequiredCurrencyId_n = n; }
    public void setFkSrcRequiredUnitOfMeasureId_n(int n) { mnFkSrcRequiredUnitOfMeasureId_n = n; }
    public void setFkLastEtlLogId(int n) { mnFkLastEtlLogId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkItemId() { return mnPkItemId; }
    public int getDesItemId() { return mnDesItemId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getNameBoardType() { return msNameBoardType; }
    public String getNameFlute() { return msNameFlute; }
    public int getSrcBoardTypeFk() { return mnSrcBoardTypeFk; }
    public String getSrcFluteFk() { return msSrcFluteFk; }
    public String getSrcCustomerFk_n() { return msSrcCustomerFk_n; }
    public int getSrcRequiredCurrencyFk_n() { return mnSrcRequiredCurrencyFk_n; }
    public String getSrcRequiredUnitOfMeasureFk_n() { return msSrcRequiredUnitOfMeasureFk_n; }
    public Date getFirstEtlInsert() { return mtFirstEtlInsert; }
    public Date getLastEtlUpdate() { return mtLastEtlUpdate; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkSrcCustomerId_n() { return mnFkSrcCustomerId_n; }
    public int getFkSrcRequiredCurrencyId_n() { return mnFkSrcRequiredCurrencyId_n; }
    public int getFkSrcRequiredUnitOfMeasureId_n() { return mnFkSrcRequiredUnitOfMeasureId_n; }
    public int getFkLastEtlLogId() { return mnFkLastEtlLogId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkItemId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkItemId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkItemId = 0;
        mnDesItemId = 0;
        msCode = "";
        msName = "";
        msNameBoardType = "";
        msNameFlute = "";
        mnSrcBoardTypeFk = 0;
        msSrcFluteFk = "";
        msSrcCustomerFk_n = "";
        mnSrcRequiredCurrencyFk_n = 0;
        msSrcRequiredUnitOfMeasureFk_n = "";
        mtFirstEtlInsert = null;
        mtLastEtlUpdate = null;
        mbDeleted = false;
        mbSystem = false;
        mnFkSrcCustomerId_n = 0;
        mnFkSrcRequiredCurrencyId_n = 0;
        mnFkSrcRequiredUnitOfMeasureId_n = 0;
        mnFkLastEtlLogId = 0;
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
        return "WHERE id_itm = " + mnPkItemId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_itm = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkItemId = 0;

        msSql = "SELECT COALESCE(MAX(id_itm), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkItemId = resultSet.getInt(1);
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
            mnPkItemId = resultSet.getInt("id_itm");
            mnDesItemId = resultSet.getInt("des_itm_id");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msNameBoardType = resultSet.getString("name_brd_type");
            msNameFlute = resultSet.getString("name_flu");
            mnSrcBoardTypeFk = resultSet.getInt("src_brd_type_fk");
            msSrcFluteFk = resultSet.getString("src_flu_fk");
            msSrcCustomerFk_n = resultSet.getString("src_cus_fk_n");
            if (resultSet.wasNull()) msSrcCustomerFk_n = "";
            mnSrcRequiredCurrencyFk_n = resultSet.getInt("src_req_cur_fk_n");
            msSrcRequiredUnitOfMeasureFk_n = resultSet.getString("src_req_uom_fk_n");
            if (resultSet.wasNull()) msSrcRequiredUnitOfMeasureFk_n = "";
            mtFirstEtlInsert = resultSet.getTimestamp("fst_etl_ins");
            mtLastEtlUpdate = resultSet.getTimestamp("lst_etl_upd");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkSrcCustomerId_n = resultSet.getInt("fk_cus_n");
            mnFkSrcRequiredCurrencyId_n = resultSet.getInt("fk_src_req_cur_n");
            mnFkSrcRequiredUnitOfMeasureId_n = resultSet.getInt("fk_src_req_uom_n");
            mnFkLastEtlLogId = resultSet.getInt("fk_lst_etl_log");
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

        // Compare and reset Avista Configuration default values:

        configAvista = ((SDbConfig) session.getConfigSystem()).getDbConfigAvista();
        
        if (mnFkSrcRequiredCurrencyId_n == configAvista.getFkSrcDefaultCurrencyId()) {
            mnFkSrcRequiredCurrencyId_n = SLibConsts.UNDEFINED;
            mnSrcRequiredCurrencyFk_n = SLibConsts.UNDEFINED;
        }
        
        if (mnFkSrcRequiredUnitOfMeasureId_n == configAvista.getFkSrcDefaultUnitOfMeasureId()) {
            mnFkSrcRequiredUnitOfMeasureId_n = SLibConsts.UNDEFINED;
            msSrcRequiredUnitOfMeasureFk_n = "";
        }
        
        // Save registry:
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkItemId + ", " + 
                    mnDesItemId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + msNameBoardType + "', " + 
                    "'" + msNameFlute + "', " + 
                    mnSrcBoardTypeFk + ", " + 
                    "'" + msSrcFluteFk + "', " + 
                    (msSrcCustomerFk_n.isEmpty() ? "NULL" : "'" + msSrcCustomerFk_n + "'") + ", " + 
                    (mnSrcRequiredCurrencyFk_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnSrcRequiredCurrencyFk_n) + ", " + 
                    (msSrcRequiredUnitOfMeasureFk_n.isEmpty() ? "NULL" : "'" + msSrcRequiredUnitOfMeasureFk_n + "'") + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    (mnFkSrcCustomerId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcCustomerId_n) + ", " + 
                    (mnFkSrcRequiredCurrencyId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcRequiredCurrencyId_n) + ", " + 
                    (mnFkSrcRequiredUnitOfMeasureId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcRequiredUnitOfMeasureId_n) + ", " + 
                    mnFkLastEtlLogId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_itm = " + mnPkItemId + ", " +
                    "des_itm_id = " + mnDesItemId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "name_brd_type = '" + msNameBoardType + "', " +
                    "name_flu = '" + msNameFlute + "', " +
                    "src_brd_type_fk = " + mnSrcBoardTypeFk + ", " +
                    "src_flu_fk = '" + msSrcFluteFk + "', " +
                    "src_cus_fk_n = " + (msSrcCustomerFk_n.isEmpty() ? "NULL" : "'" + msSrcCustomerFk_n + "'") + ", " +
                    "src_req_cur_fk_n = " + (mnSrcRequiredCurrencyFk_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnSrcRequiredCurrencyFk_n) + ", " +
                    "src_req_uom_fk_n = " + (msSrcRequiredUnitOfMeasureFk_n.isEmpty() ? "NULL" : "'" + msSrcRequiredUnitOfMeasureFk_n + "'") + ", " +
                    //"fst_etl_ins = " + "NOW()" + ", " +
                    "lst_etl_upd = " + "NOW()" + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_cus_n = " + (mnFkSrcCustomerId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcCustomerId_n) + ", " +
                    "fk_src_req_cur_n = " + (mnFkSrcRequiredCurrencyId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcRequiredCurrencyId_n) + ", " +
                    "fk_src_req_uom_n = " + (mnFkSrcRequiredUnitOfMeasureId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcRequiredUnitOfMeasureId_n) + ", " +
                    "fk_lst_etl_log = " + mnFkLastEtlLogId + ", " +
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
    public SDbItem clone() throws CloneNotSupportedException {
        SDbItem registry = new SDbItem();

        registry.setPkItemId(this.getPkItemId());
        registry.setDesItemId(this.getDesItemId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setNameBoardType(this.getNameBoardType());
        registry.setNameFlute(this.getNameFlute());
        registry.setSrcBoardTypeFk(this.getSrcBoardTypeFk());
        registry.setSrcFluteFk(this.getSrcFluteFk());
        registry.setSrcCustomerFk_n(this.getSrcCustomerFk_n());
        registry.setSrcRequiredCurrencyFk_n(this.getSrcRequiredCurrencyFk_n());
        registry.setSrcRequiredUnitOfMeasureFk_n(this.getSrcRequiredUnitOfMeasureFk_n());
        registry.setFirstEtlInsert(this.getFirstEtlInsert());
        registry.setLastEtlUpdate(this.getLastEtlUpdate());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkSrcCustomerId_n(this.getFkSrcCustomerId_n());
        registry.setFkSrcRequiredCurrencyId_n(this.getFkSrcRequiredCurrencyId_n());
        registry.setFkSrcRequiredUnitOfMeasureId_n(this.getFkSrcRequiredUnitOfMeasureId_n());
        registry.setFkLastEtlLogId(this.getFkLastEtlLogId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
