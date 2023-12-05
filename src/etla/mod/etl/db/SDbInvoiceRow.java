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
public class SDbInvoiceRow extends SDbRegistryUser {

    protected int mnPkInvoiceId;
    protected int mnPkRowId;
    protected int mnSrcInvoiceId;
    protected int mnSrcInvoiceRowId;
    protected String msCode;
    protected String msName;
    protected String msProductDescription;
    protected String msCustomerOrder;
    protected String msOrderNumber;
    protected String msOrderDescription;
    protected String msEstimateNumber;
    protected String msEstimateDescription;
    protected int mnQuantityOrdered;
    protected int mnQuantity;
    protected double mdLength;
    protected double mdWidth;
    protected double mdArea;
    protected double mdWeight;
    protected double mdOriginalPrice;
    protected String msOriginalPricePerUnit;
    protected double mdOriginalUnits;
    protected double mdOriginalAmount;
    protected double mdFinalPrice;
    protected String msFinalPricePerUnit;
    protected double mdFinalUnits;
    protected double mdFinalAmount;
    protected String msSrcBoardType;
    protected int mnSrcBoardTypeFk;
    protected String msSrcFluteFk;
    protected int mnDesItemFk;
    protected String msSrcOriginalUnitOfMeasureFk;
    protected String msSrcFinalUnitOfMeasureFk;
    protected int mnDesOriginalUnitOfMeasureFk;
    protected int mnDesFinalUnitOfMeasureFk;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkItemId;
    protected int mnFkSrcOriginalUnitOfMeasureId;
    protected int mnFkSrcFinalUnitOfMeasureId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbInvoiceRow() {
        super(SModConsts.A_INV_ROW);
    }

    /*
     * Public methods
     */

    public void setPkInvoiceId(int n) { mnPkInvoiceId = n; }
    public void setPkRowId(int n) { mnPkRowId = n; }
    public void setSrcInvoiceId(int n) { mnSrcInvoiceId = n; }
    public void setSrcInvoiceRowId(int n) { mnSrcInvoiceRowId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setProductDescription(String s) { msProductDescription = s; }
    public void setCustomerOrder(String s) { msCustomerOrder = s; }
    public void setOrderNumber(String s) { msOrderNumber = s; }
    public void setOrderDescription(String s) { msOrderDescription = s; }
    public void setEstimateNumber(String s) { msEstimateNumber = s; }
    public void setEstimateDescription(String s) { msEstimateDescription = s; }
    public void setQuantityOrdered(int n) { mnQuantityOrdered = n; }
    public void setQuantity(int n) { mnQuantity = n; }
    public void setLength(double d) { mdLength = d; }
    public void setWidth(double d) { mdWidth = d; }
    public void setArea(double d) { mdArea = d; }
    public void setWeight(double d) { mdWeight = d; }
    public void setOriginalPrice(double d) { mdOriginalPrice = d; }
    public void setOriginalPricePerUnit(String s) { msOriginalPricePerUnit = s; }
    public void setOriginalUnits(double d) { mdOriginalUnits = d; }
    public void setOriginalAmount(double d) { mdOriginalAmount = d; }
    public void setFinalPrice(double d) { mdFinalPrice = d; }
    public void setFinalPricePerUnit(String s) { msFinalPricePerUnit = s; }
    public void setFinalUnits(double d) { mdFinalUnits = d; }
    public void setFinalAmount(double d) { mdFinalAmount = d; }
    public void setSrcBoardType(String s) { msSrcBoardType = s; }
    public void setSrcBoardTypeFk(int n) { mnSrcBoardTypeFk = n; }
    public void setSrcFluteFk(String s) { msSrcFluteFk = s; }
    public void setDesItemFk(int n) { mnDesItemFk = n; }
    public void setSrcOriginalUnitOfMeasureFk(String s) { msSrcOriginalUnitOfMeasureFk = s; }
    public void setSrcFinalUnitOfMeasureFk(String s) { msSrcFinalUnitOfMeasureFk = s; }
    public void setDesOriginalUnitOfMeasureFk(int n) { mnDesOriginalUnitOfMeasureFk = n; }
    public void setDesFinalUnitOfMeasureFk(int n) { mnDesFinalUnitOfMeasureFk = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkSrcOriginalUnitOfMeasureId(int n) { mnFkSrcOriginalUnitOfMeasureId = n; }
    public void setFkSrcFinalUnitOfMeasureId(int n) { mnFkSrcFinalUnitOfMeasureId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkInvoiceId() { return mnPkInvoiceId; }
    public int getPkRowId() { return mnPkRowId; }
    public int getSrcInvoiceId() { return mnSrcInvoiceId; }
    public int getSrcInvoiceRowId() { return mnSrcInvoiceRowId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getProductDescription() { return msProductDescription; }
    public String getCustomerOrder() { return msCustomerOrder; }
    public String getOrderNumber() { return msOrderNumber; }
    public String getOrderDescription() { return msOrderDescription; }
    public String getEstimateNumber() { return msEstimateNumber; }
    public String getEstimateDescription() { return msEstimateDescription; }
    public int getQuantityOrdered() { return mnQuantityOrdered; }
    public int getQuantity() { return mnQuantity; }
    public double getLength() { return mdLength; }
    public double getWidth() { return mdWidth; }
    public double getArea() { return mdArea; }
    public double getWeight() { return mdWeight; }
    public double getOriginalPrice() { return mdOriginalPrice; }
    public String getOriginalPricePerUnit() { return msOriginalPricePerUnit; }
    public double getOriginalUnits() { return mdOriginalUnits; }
    public double getOriginalAmount() { return mdOriginalAmount; }
    public double getFinalPrice() { return mdFinalPrice; }
    public String getFinalPricePerUnit() { return msFinalPricePerUnit; }
    public double getFinalUnits() { return mdFinalUnits; }
    public double getFinalAmount() { return mdFinalAmount; }
    public String getSrcBoardType() { return msSrcBoardType; }
    public int getSrcBoardTypeFk() { return mnSrcBoardTypeFk; }
    public String getSrcFluteFk() { return msSrcFluteFk; }
    public int getDesItemFk() { return mnDesItemFk; }
    public String getSrcOriginalUnitOfMeasureFk() { return msSrcOriginalUnitOfMeasureFk; }
    public String getSrcFinalUnitOfMeasureFk() { return msSrcFinalUnitOfMeasureFk; }
    public int getDesOriginalUnitOfMeasureFk() { return mnDesOriginalUnitOfMeasureFk; }
    public int getDesFinalUnitOfMeasureFk() { return mnDesFinalUnitOfMeasureFk; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkSrcOriginalUnitOfMeasureId() { return mnFkSrcOriginalUnitOfMeasureId; }
    public int getFkSrcFinalUnitOfMeasureId() { return mnFkSrcFinalUnitOfMeasureId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkInvoiceId = pk[0];
        mnPkRowId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkInvoiceId, mnPkRowId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkInvoiceId = 0;
        mnPkRowId = 0;
        mnSrcInvoiceId = 0;
        mnSrcInvoiceRowId = 0;
        msCode = "";
        msName = "";
        msProductDescription = "";
        msCustomerOrder = "";
        msOrderNumber = "";
        msOrderDescription = "";
        msEstimateNumber = "";
        msEstimateDescription = "";
        mnQuantityOrdered = 0;
        mnQuantity = 0;
        mdLength = 0;
        mdWidth = 0;
        mdArea = 0;
        mdWeight = 0;
        mdOriginalPrice = 0;
        msOriginalPricePerUnit = "";
        mdOriginalUnits = 0;
        mdOriginalAmount = 0;
        mdFinalPrice = 0;
        msFinalPricePerUnit = "";
        mdFinalUnits = 0;
        mdFinalAmount = 0;
        msSrcBoardType = "";
        mnSrcBoardTypeFk = 0;
        msSrcFluteFk = "";
        mnDesItemFk = 0;
        msSrcOriginalUnitOfMeasureFk = "";
        msSrcFinalUnitOfMeasureFk = "";
        mnDesOriginalUnitOfMeasureFk = 0;
        mnDesFinalUnitOfMeasureFk = 0;
        mbDeleted = false;
        mbSystem = false;
        mnFkItemId = 0;
        mnFkSrcOriginalUnitOfMeasureId = 0;
        mnFkSrcFinalUnitOfMeasureId = 0;
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
        return "WHERE id_inv = " + mnPkInvoiceId + " AND "
                + "id_row = " + mnPkRowId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_inv = " + pk[0] + " AND "
                + "id_row = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkRowId = 0;

        msSql = "SELECT COALESCE(MAX(id_row), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_inv = " + mnPkInvoiceId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkRowId = resultSet.getInt(1);
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
            mnPkInvoiceId = resultSet.getInt("id_inv");
            mnPkRowId = resultSet.getInt("id_row");
            mnSrcInvoiceId = resultSet.getInt("src_inv_id");
            mnSrcInvoiceRowId = resultSet.getInt("src_inv_row_id");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msProductDescription = resultSet.getString("pro_dsc");
            msCustomerOrder = resultSet.getString("cus_ord");
            msOrderNumber = resultSet.getString("ord_num");
            msOrderDescription = resultSet.getString("ord_dsc");
            msEstimateNumber = resultSet.getString("est_num");
            msEstimateDescription = resultSet.getString("est_dsc");
            mnQuantityOrdered = resultSet.getInt("qty_ord");
            mnQuantity = resultSet.getInt("qty");
            mdLength = resultSet.getDouble("len");
            mdWidth = resultSet.getDouble("wid");
            mdArea = resultSet.getDouble("are");
            mdWeight = resultSet.getDouble("wei");
            mdOriginalPrice = resultSet.getDouble("ori_prc");
            msOriginalPricePerUnit = resultSet.getString("ori_prc_unt");
            mdOriginalUnits = resultSet.getDouble("ori_unt");
            mdOriginalAmount = resultSet.getDouble("ori_amt");
            mdFinalPrice = resultSet.getDouble("fin_prc");
            msFinalPricePerUnit = resultSet.getString("fin_prc_unt");
            mdFinalUnits = resultSet.getDouble("fin_unt");
            mdFinalAmount = resultSet.getDouble("fin_amt");
            msSrcBoardType = resultSet.getString("src_brd_type");
            mnSrcBoardTypeFk = resultSet.getInt("src_brd_type_fk");
            msSrcFluteFk = resultSet.getString("src_flu_fk");
            mnDesItemFk = resultSet.getInt("des_itm_fk");
            msSrcOriginalUnitOfMeasureFk = resultSet.getString("src_ori_uom_fk");
            msSrcFinalUnitOfMeasureFk = resultSet.getString("src_fin_uom_fk");
            mnDesOriginalUnitOfMeasureFk = resultSet.getInt("des_ori_uom_fk");
            mnDesFinalUnitOfMeasureFk = resultSet.getInt("des_fin_uom_fk");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkItemId = resultSet.getInt("fk_itm");
            mnFkSrcOriginalUnitOfMeasureId = resultSet.getInt("fk_src_ori_uom");
            mnFkSrcFinalUnitOfMeasureId = resultSet.getInt("fk_src_fin_uom");
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
                    mnPkInvoiceId + ", " + 
                    mnPkRowId + ", " + 
                    mnSrcInvoiceId + ", " + 
                    mnSrcInvoiceRowId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + msProductDescription + "', " + 
                    "'" + msCustomerOrder + "', " + 
                    "'" + msOrderNumber + "', " + 
                    "'" + msOrderDescription + "', " + 
                    "'" + msEstimateNumber + "', " + 
                    "'" + msEstimateDescription + "', " + 
                    mnQuantityOrdered + ", " + 
                    mnQuantity + ", " + 
                    mdLength + ", " + 
                    mdWidth + ", " + 
                    mdArea + ", " + 
                    mdWeight + ", " + 
                    mdOriginalPrice + ", " + 
                    "'" + msOriginalPricePerUnit + "', " + 
                    mdOriginalUnits + ", " + 
                    mdOriginalAmount + ", " + 
                    mdFinalPrice + ", " + 
                    "'" + msFinalPricePerUnit + "', " + 
                    mdFinalUnits + ", " + 
                    mdFinalAmount + ", " + 
                    "'" + msSrcBoardType + "', " + 
                    mnSrcBoardTypeFk + ", " + 
                    "'" + msSrcFluteFk + "', " + 
                    mnDesItemFk + ", " + 
                    "'" + msSrcOriginalUnitOfMeasureFk + "', " + 
                    "'" + msSrcFinalUnitOfMeasureFk + "', " + 
                    mnDesOriginalUnitOfMeasureFk + ", " + 
                    mnDesFinalUnitOfMeasureFk + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkItemId + ", " + 
                    mnFkSrcOriginalUnitOfMeasureId + ", " + 
                    mnFkSrcFinalUnitOfMeasureId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_inv = " + mnPkInvoiceId + ", " +
                    //"id_row = " + mnPkRowId + ", " +
                    "src_inv_id = " + mnSrcInvoiceId + ", " +
                    "src_inv_row_id = " + mnSrcInvoiceRowId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "pro_dsc = '" + msProductDescription + "', " +
                    "cus_ord = '" + msCustomerOrder + "', " +
                    "ord_num = '" + msOrderNumber + "', " +
                    "ord_dsc = '" + msOrderDescription + "', " + 
                    "est_num = '" + msEstimateNumber + "', " +
                    "est_dsc = '" + msEstimateDescription + "', " +
                    "qty_ord = " + mnQuantityOrdered + ", " +
                    "qty = " + mnQuantity + ", " +
                    "len = " + mdLength + ", " +
                    "wid = " + mdWidth + ", " +
                    "are = " + mdArea + ", " +
                    "wei = " + mdWeight + ", " +
                    "ori_prc = " + mdOriginalPrice + ", " +
                    "ori_prc_unt = '" + msOriginalPricePerUnit + "', " +
                    "ori_unt = " + mdOriginalUnits + ", " +
                    "ori_amt = " + mdOriginalAmount + ", " +
                    "fin_prc = " + mdFinalPrice + ", " +
                    "fin_prc_unt = '" + msFinalPricePerUnit + "', " +
                    "fin_unt = " + mdFinalUnits + ", " +
                    "fin_amt = " + mdFinalAmount + ", " +
                    "src_brd_type = '" + msSrcBoardType + "', " +
                    "src_brd_type_fk = " + mnSrcBoardTypeFk + ", " +
                    "src_flu_fk = '" + msSrcFluteFk + "', " +
                    "des_itm_fk = " + mnDesItemFk + ", " +
                    "src_ori_uom_fk = '" + msSrcOriginalUnitOfMeasureFk + "', " +
                    "src_fin_uom_fk = '" + msSrcFinalUnitOfMeasureFk + "', " +
                    "des_ori_uom_fk = " + mnDesOriginalUnitOfMeasureFk + ", " +
                    "des_fin_uom_fk = " + mnDesFinalUnitOfMeasureFk + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_itm = " + mnFkItemId + ", " +
                    "fk_src_ori_uom = " + mnFkSrcOriginalUnitOfMeasureId + ", " +
                    "fk_src_fin_uom = " + mnFkSrcFinalUnitOfMeasureId + ", " +
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
    public SDbInvoiceRow clone() throws CloneNotSupportedException {
        SDbInvoiceRow registry = new SDbInvoiceRow();

        registry.setPkInvoiceId(this.getPkInvoiceId());
        registry.setPkRowId(this.getPkRowId());
        registry.setSrcInvoiceId(this.getSrcInvoiceId());
        registry.setSrcInvoiceRowId(this.getSrcInvoiceRowId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setProductDescription(this.getProductDescription());
        registry.setCustomerOrder(this.getCustomerOrder());
        registry.setOrderNumber(this.getOrderNumber());
        registry.setOrderDescription(this.getOrderDescription());
        registry.setEstimateNumber(this.getEstimateNumber());
        registry.setEstimateDescription(this.getEstimateDescription());
        registry.setQuantityOrdered(this.getQuantityOrdered());
        registry.setQuantity(this.getQuantity());
        registry.setLength(this.getLength());
        registry.setWidth(this.getWidth());
        registry.setArea(this.getArea());
        registry.setWeight(this.getWeight());
        registry.setOriginalPrice(this.getOriginalPrice());
        registry.setOriginalPricePerUnit(this.getOriginalPricePerUnit());
        registry.setOriginalUnits(this.getOriginalUnits());
        registry.setOriginalAmount(this.getOriginalAmount());
        registry.setFinalPrice(this.getFinalPrice());
        registry.setFinalPricePerUnit(this.getFinalPricePerUnit());
        registry.setFinalUnits(this.getFinalUnits());
        registry.setFinalAmount(this.getFinalAmount());
        registry.setSrcBoardType(this.getSrcBoardType());
        registry.setSrcBoardTypeFk(this.getSrcBoardTypeFk());
        registry.setSrcFluteFk(this.getSrcFluteFk());
        registry.setDesItemFk(this.getDesItemFk());
        registry.setSrcOriginalUnitOfMeasureFk(this.getSrcOriginalUnitOfMeasureFk());
        registry.setSrcFinalUnitOfMeasureFk(this.getSrcFinalUnitOfMeasureFk());
        registry.setDesOriginalUnitOfMeasureFk(this.getDesOriginalUnitOfMeasureFk());
        registry.setDesFinalUnitOfMeasureFk(this.getDesFinalUnitOfMeasureFk());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkSrcOriginalUnitOfMeasureId(this.getFkSrcOriginalUnitOfMeasureId());
        registry.setFkSrcFinalUnitOfMeasureId(this.getFkSrcFinalUnitOfMeasureId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
