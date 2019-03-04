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
public class SDbCustomer extends SDbRegistryUser {

    protected int mnPkCustomerId;
    protected String msSrcCustomerId;
    protected int mnDesCustomerId;
    protected int mnDesCustomerBranchId;
    protected String msCode;
    protected String msName;
    protected String msNameShort;
    protected String msTaxId;
    protected String msStreet;
    protected String msNumberExt;
    protected String msNumberInt;
    protected String msNeighborhood;
    protected String msReference;
    protected String msLocality;
    protected String msCounty;
    protected String msSrcStateFk;
    protected String msState;
    protected String msSrcCountryFk;
    protected String msCountry;
    protected String msZip;
    protected String msPhone;
    protected String msFax;
    protected String msPayAccount;
    protected int mnCreditDays;
    protected double mdCreditLimit;
    protected String msCreditStatusCode;
    protected String msPayTermCode;
    protected int mnSrcCustomerCurrencyFk_n;
    protected String msSrcCustomerUnitOfMeasureFk_n;
    protected int mnSrcCustomerSalesAgentFk_n;
    protected int mnSrcRequiredCurrencyFk_n;
    protected String msSrcRequiredUnitOfMeasureFk_n;
    protected String msDesCfdiPaymentWay;
    protected String msDesCfdiCfdiUsage;
    protected Date mtFirstEtlInsert;
    protected Date mtLastEtlUpdate;
    protected boolean mbEtlIgnore;
    protected boolean mbActive;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkSrcCustomerCurrencyId_n;
    protected int mnFkSrcCustomerUnitOfMeasureId_n;
    protected int mnFkSrcCustomerSalesAgentId_n;
    protected int mnFkSrcRequiredCurrencyId_n;
    protected int mnFkSrcRequiredUnitOfMeasureId_n;
    protected int mnFkDesRequiredPayMethodId_n;
    protected int mnFkLastEtlLogId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbCustomer() {
        super(SModConsts.AU_CUS);
    }

    /*
     * Public methods
     */

    public void setPkCustomerId(int n) { mnPkCustomerId = n; }
    public void setSrcCustomerId(String s) { msSrcCustomerId = s; }
    public void setDesCustomerId(int n) { mnDesCustomerId = n; }
    public void setDesCustomerBranchId(int n) { mnDesCustomerBranchId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setNameShort(String s) { msNameShort = s; }
    public void setTaxId(String s) { msTaxId = s; }
    public void setStreet(String s) { msStreet = s; }
    public void setNumberExt(String s) { msNumberExt = s; }
    public void setNumberInt(String s) { msNumberInt = s; }
    public void setNeighborhood(String s) { msNeighborhood = s; }
    public void setReference(String s) { msReference = s; }
    public void setLocality(String s) { msLocality = s; }
    public void setCounty(String s) { msCounty = s; }
    public void setSrcStateFk(String s) { msSrcStateFk = s; }
    public void setState(String s) { msState = s; }
    public void setSrcCountryFk(String s) { msSrcCountryFk = s; }
    public void setCountry(String s) { msCountry = s; }
    public void setZip(String s) { msZip = s; }
    public void setPhone(String s) { msPhone = s; }
    public void setFax(String s) { msFax = s; }
    public void setPayAccount(String s) { msPayAccount = s; }
    public void setCreditDays(int n) { mnCreditDays = n; }
    public void setCreditLimit(double d) { mdCreditLimit = d; }
    public void setCreditStatusCode(String s) { msCreditStatusCode = s; }
    public void setPayTermCode(String s) { msPayTermCode = s; }
    public void setSrcCustomerCurrencyFk_n(int n) { mnSrcCustomerCurrencyFk_n = n; }
    public void setSrcCustomerUnitOfMeasureFk_n(String s) { msSrcCustomerUnitOfMeasureFk_n = s; }
    public void setSrcCustomerSalesAgentFk_n(int n) { mnSrcCustomerSalesAgentFk_n = n; }
    public void setSrcRequiredCurrencyFk_n(int n) { mnSrcRequiredCurrencyFk_n = n; }
    public void setSrcRequiredUnitOfMeasureFk_n(String s) { msSrcRequiredUnitOfMeasureFk_n = s; }
    public void setDesCfdiPaymentWay(String s) { msDesCfdiPaymentWay = s; }
    public void setDesCfdiCfdiUsage(String s) { msDesCfdiCfdiUsage = s; }
    public void setFirstEtlInsert(Date t) { mtFirstEtlInsert = t; }
    public void setLastEtlUpdate(Date t) { mtLastEtlUpdate = t; }
    public void setEtlIgnore(boolean b) { mbEtlIgnore = b; }
    public void setActive(boolean b) { mbActive = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkSrcCustomerCurrencyId_n(int n) { mnFkSrcCustomerCurrencyId_n = n; }
    public void setFkSrcCustomerUnitOfMeasureId_n(int n) { mnFkSrcCustomerUnitOfMeasureId_n = n; }
    public void setFkSrcCustomerSalesAgentId_n(int n) { mnFkSrcCustomerSalesAgentId_n = n; }
    public void setFkSrcRequiredCurrencyId_n(int n) { mnFkSrcRequiredCurrencyId_n = n; }
    public void setFkSrcRequiredUnitOfMeasureId_n(int n) { mnFkSrcRequiredUnitOfMeasureId_n = n; }
    public void setFkDesRequiredPayMethodId_n(int n) { mnFkDesRequiredPayMethodId_n = n; }
    public void setFkLastEtlLogId(int n) { mnFkLastEtlLogId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkCustomerId() { return mnPkCustomerId; }
    public String getSrcCustomerId() { return msSrcCustomerId; }
    public int getDesCustomerId() { return mnDesCustomerId; }
    public int getDesCustomerBranchId() { return mnDesCustomerBranchId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getNameShort() { return msNameShort; }
    public String getTaxId() { return msTaxId; }
    public String getStreet() { return msStreet; }
    public String getNumberExt() { return msNumberExt; }
    public String getNumberInt() { return msNumberInt; }
    public String getNeighborhood() { return msNeighborhood; }
    public String getReference() { return msReference; }
    public String getLocality() { return msLocality; }
    public String getCounty() { return msCounty; }
    public String getSrcStateFk() { return msSrcStateFk; }
    public String getState() { return msState; }
    public String getSrcCountryFk() { return msSrcCountryFk; }
    public String getCountry() { return msCountry; }
    public String getZip() { return msZip; }
    public String getPhone() { return msPhone; }
    public String getFax() { return msFax; }
    public String getPayAccount() { return msPayAccount; }
    public int getCreditDays() { return mnCreditDays; }
    public double getCreditLimit() { return mdCreditLimit; }
    public String getCreditStatusCode() { return msCreditStatusCode; }
    public String getPayTermCode() { return msPayTermCode; }
    public int getSrcCustomerCurrencyFk_n() { return mnSrcCustomerCurrencyFk_n; }
    public String getSrcCustomerUnitOfMeasureFk_n() { return msSrcCustomerUnitOfMeasureFk_n; }
    public int getSrcCustomerSalesAgentFk_n() { return mnSrcCustomerSalesAgentFk_n; }
    public int getSrcRequiredCurrencyFk_n() { return mnSrcRequiredCurrencyFk_n; }
    public String getSrcRequiredUnitOfMeasureFk_n() { return msSrcRequiredUnitOfMeasureFk_n; }
    public String getDesCfdiPaymentWay() { return msDesCfdiPaymentWay; }
    public String getDesCfdiCfdiUsage() { return msDesCfdiCfdiUsage; }
    public Date getFirstEtlInsert() { return mtFirstEtlInsert; }
    public Date getLastEtlUpdate() { return mtLastEtlUpdate; }
    public boolean isEtlIgnore() { return mbEtlIgnore; }
    public boolean isActive() { return mbActive; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkSrcCustomerCurrencyId_n() { return mnFkSrcCustomerCurrencyId_n; }
    public int getFkSrcCustomerUnitOfMeasureId_n() { return mnFkSrcCustomerUnitOfMeasureId_n; }
    public int getFkSrcCustomerSalesAgentId_n() { return mnFkSrcCustomerSalesAgentId_n; }
    public int getFkSrcRequiredCurrencyId_n() { return mnFkSrcRequiredCurrencyId_n; }
    public int getFkSrcRequiredUnitOfMeasureId_n() { return mnFkSrcRequiredUnitOfMeasureId_n; }
    public int getFkDesRequiredPayMethodId_n() { return mnFkDesRequiredPayMethodId_n; }
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
        mnPkCustomerId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCustomerId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCustomerId = 0;
        msSrcCustomerId = "";
        mnDesCustomerId = 0;
        mnDesCustomerBranchId = 0;
        msCode = "";
        msName = "";
        msNameShort = "";
        msTaxId = "";
        msStreet = "";
        msNumberExt = "";
        msNumberInt = "";
        msNeighborhood = "";
        msReference = "";
        msLocality = "";
        msCounty = "";
        msSrcStateFk = "";
        msState = "";
        msSrcCountryFk = "";
        msCountry = "";
        msZip = "";
        msPhone = "";
        msFax = "";
        msPayAccount = "";
        mnCreditDays = 0;
        mdCreditLimit = 0;
        msCreditStatusCode = "";
        msPayTermCode = "";
        mnSrcCustomerCurrencyFk_n = 0;
        msSrcCustomerUnitOfMeasureFk_n = "";
        mnSrcCustomerSalesAgentFk_n = 0;
        mnSrcRequiredCurrencyFk_n = 0;
        msSrcRequiredUnitOfMeasureFk_n = "";
        msDesCfdiPaymentWay = "";
        msDesCfdiCfdiUsage = "";
        mtFirstEtlInsert = null;
        mtLastEtlUpdate = null;
        mbEtlIgnore = false;
        mbActive = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkSrcCustomerCurrencyId_n = 0;
        mnFkSrcCustomerUnitOfMeasureId_n = 0;
        mnFkSrcCustomerSalesAgentId_n = 0;
        mnFkSrcRequiredCurrencyId_n = 0;
        mnFkSrcRequiredUnitOfMeasureId_n = 0;
        mnFkDesRequiredPayMethodId_n = 0;
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
        return "WHERE id_cus = " + mnPkCustomerId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cus = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkCustomerId = 0;

        msSql = "SELECT COALESCE(MAX(id_cus), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkCustomerId = resultSet.getInt(1);
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
            mnPkCustomerId = resultSet.getInt("id_cus");
            msSrcCustomerId = resultSet.getString("src_cus_id");
            mnDesCustomerId = resultSet.getInt("des_cus_id");
            mnDesCustomerBranchId = resultSet.getInt("des_cus_bra_id");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msNameShort = resultSet.getString("name_s");
            msTaxId = resultSet.getString("tax_id");
            msStreet = resultSet.getString("str");
            msNumberExt = resultSet.getString("num_ext");
            msNumberInt = resultSet.getString("num_int");
            msNeighborhood = resultSet.getString("nei");
            msReference = resultSet.getString("ref");
            msLocality = resultSet.getString("loc");
            msCounty = resultSet.getString("cou");
            msSrcStateFk = resultSet.getString("src_sta_fk");
            msState = resultSet.getString("sta");
            msSrcCountryFk = resultSet.getString("src_cty_fk");
            msCountry = resultSet.getString("cty");
            msZip = resultSet.getString("zip");
            msPhone = resultSet.getString("pho");
            msFax = resultSet.getString("fax");
            msPayAccount = resultSet.getString("pay_acc");
            mnCreditDays = resultSet.getInt("cdt_day");
            mdCreditLimit = resultSet.getDouble("cdt_lim");
            msCreditStatusCode = resultSet.getString("cdt_sta_code");
            msPayTermCode = resultSet.getString("pay_ter_code");
            mnSrcCustomerCurrencyFk_n = resultSet.getInt("src_cus_cur_fk_n");
            msSrcCustomerUnitOfMeasureFk_n = resultSet.getString("src_cus_uom_fk_n");
            if (resultSet.wasNull()) msSrcCustomerUnitOfMeasureFk_n = "";
            mnSrcCustomerSalesAgentFk_n = resultSet.getInt("src_cus_sal_agt_fk_n");
            mnSrcRequiredCurrencyFk_n = resultSet.getInt("src_req_cur_fk_n");
            msSrcRequiredUnitOfMeasureFk_n = resultSet.getString("src_req_uom_fk_n");
            msDesCfdiPaymentWay = resultSet.getString("des_cfdi_pay_way");
            msDesCfdiCfdiUsage = resultSet.getString("des_cfdi_cfd_use");
            mtFirstEtlInsert = resultSet.getTimestamp("fst_etl_ins");
            mtLastEtlUpdate = resultSet.getTimestamp("lst_etl_upd");
            mbEtlIgnore = resultSet.getBoolean("b_etl_ign");
            mbActive = resultSet.getBoolean("b_act");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkSrcCustomerCurrencyId_n = resultSet.getInt("fk_src_cus_cur_n");
            mnFkSrcCustomerUnitOfMeasureId_n = resultSet.getInt("fk_src_cus_uom_n");
            mnFkSrcCustomerSalesAgentId_n = resultSet.getInt("fk_src_cus_sal_agt_n");
            mnFkSrcRequiredCurrencyId_n = resultSet.getInt("fk_src_req_cur_n");
            mnFkSrcRequiredUnitOfMeasureId_n = resultSet.getInt("fk_src_req_uom_n");
            mnFkDesRequiredPayMethodId_n = resultSet.getInt("fk_des_req_pay_met_n");
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
        
        if (mnFkDesRequiredPayMethodId_n == configAvista.getFkDesDefaultPayMethodId()) {
            mnFkDesRequiredPayMethodId_n = SLibConsts.UNDEFINED;
        }
        
        // Save registry:
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkCustomerId + ", " + 
                    "'" + msSrcCustomerId + "', " + 
                    mnDesCustomerId + ", " + 
                    mnDesCustomerBranchId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + msNameShort + "', " + 
                    "'" + msTaxId + "', " + 
                    "'" + msStreet + "', " + 
                    "'" + msNumberExt + "', " + 
                    "'" + msNumberInt + "', " + 
                    "'" + msNeighborhood + "', " + 
                    "'" + msReference + "', " + 
                    "'" + msLocality + "', " + 
                    "'" + msCounty + "', " + 
                    "'" + msSrcStateFk + "', " + 
                    "'" + msState + "', " + 
                    "'" + msSrcCountryFk + "', " + 
                    "'" + msCountry + "', " + 
                    "'" + msZip + "', " + 
                    "'" + msPhone + "', " + 
                    "'" + msFax + "', " + 
                    "'" + msPayAccount + "', " + 
                    mnCreditDays + ", " + 
                    mdCreditLimit + ", " + 
                    "'" + msCreditStatusCode + "', " + 
                    "'" + msPayTermCode + "', " + 
                    (mnSrcCustomerCurrencyFk_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnSrcCustomerCurrencyFk_n) + ", " + 
                    (msSrcCustomerUnitOfMeasureFk_n.isEmpty() ? "NULL" : "'" + msSrcCustomerUnitOfMeasureFk_n + "'") + ", " + 
                    (mnSrcCustomerSalesAgentFk_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnSrcCustomerSalesAgentFk_n) + ", " + 
                    (mnSrcRequiredCurrencyFk_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnSrcRequiredCurrencyFk_n) + ", " + 
                    (msSrcRequiredUnitOfMeasureFk_n.isEmpty() ? "NULL" : "'" + msSrcRequiredUnitOfMeasureFk_n + "'") + ", " + 
                    "'" + msDesCfdiPaymentWay + "', " + 
                    "'" + msDesCfdiCfdiUsage + "', " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    (mbEtlIgnore ? 1 : 0) + ", " + 
                    (mbActive ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    (mnFkSrcCustomerCurrencyId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcCustomerCurrencyId_n) + ", " + 
                    (mnFkSrcCustomerUnitOfMeasureId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcCustomerUnitOfMeasureId_n) + ", " + 
                    (mnFkSrcCustomerSalesAgentId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcCustomerSalesAgentId_n) + ", " + 
                    (mnFkSrcRequiredCurrencyId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcRequiredCurrencyId_n) + ", " + 
                    (mnFkSrcRequiredUnitOfMeasureId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcRequiredUnitOfMeasureId_n) + ", " + 
                    (mnFkDesRequiredPayMethodId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkDesRequiredPayMethodId_n) + ", " + 
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
                    //"id_cus = " + mnPkCustomerId + ", " +
                    "src_cus_id = '" + msSrcCustomerId + "', " +
                    "des_cus_id = " + mnDesCustomerId + ", " +
                    "des_cus_bra_id = " + mnDesCustomerBranchId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "name_s = '" + msNameShort + "', " +
                    "tax_id = '" + msTaxId + "', " +
                    "str = '" + msStreet + "', " +
                    "num_ext = '" + msNumberExt + "', " +
                    "num_int = '" + msNumberInt + "', " +
                    "nei = '" + msNeighborhood + "', " +
                    "ref = '" + msReference + "', " +
                    "loc = '" + msLocality + "', " +
                    "cou = '" + msCounty + "', " +
                    "src_sta_fk = '" + msSrcStateFk + "', " +
                    "sta = '" + msState + "', " +
                    "src_cty_fk = '" + msSrcCountryFk + "', " +
                    "cty = '" + msCountry + "', " +
                    "zip = '" + msZip + "', " +
                    "pho = '" + msPhone + "', " +
                    "fax = '" + msFax + "', " +
                    "pay_acc = '" + msPayAccount + "', " +
                    "cdt_day = " + mnCreditDays + ", " +
                    "cdt_lim = " + mdCreditLimit + ", " +
                    "cdt_sta_code = '" + msCreditStatusCode + "', " +
                    "pay_ter_code = '" + msPayTermCode + "', " +
                    "src_cus_cur_fk_n = " + (mnSrcCustomerCurrencyFk_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnSrcCustomerCurrencyFk_n) + ", " +
                    "src_cus_uom_fk_n = " + (msSrcCustomerUnitOfMeasureFk_n.isEmpty() ? "NULL" : "'" + msSrcCustomerUnitOfMeasureFk_n + "'") + ", " +
                    "src_cus_sal_agt_fk_n = " + (mnSrcCustomerSalesAgentFk_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnSrcCustomerSalesAgentFk_n) + ", " +
                    "src_req_cur_fk_n = " + (mnSrcRequiredCurrencyFk_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnSrcRequiredCurrencyFk_n) + ", " +
                    "src_req_uom_fk_n = " + (msSrcRequiredUnitOfMeasureFk_n.isEmpty() ? "NULL" : "'" + msSrcRequiredUnitOfMeasureFk_n + "'") + ", " +
                    "des_cfdi_pay_way = '" + msDesCfdiPaymentWay + "', " +
                    "des_cfdi_cfd_use = '" + msDesCfdiCfdiUsage + "', " +
                    //"fst_etl_ins = " + "NOW()" + ", " +
                    "lst_etl_upd = " + "NOW()" + ", " +
                    "b_etl_ign = " + (mbEtlIgnore ? 1 : 0) + ", " +
                    "b_act = " + (mbActive ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_src_cus_cur_n = " + (mnFkSrcCustomerCurrencyId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcCustomerCurrencyId_n) + ", " +
                    "fk_src_cus_uom_n = " + (mnFkSrcCustomerUnitOfMeasureId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcCustomerUnitOfMeasureId_n) + ", " +
                    "fk_src_cus_sal_agt_n = " + (mnFkSrcCustomerSalesAgentId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcCustomerSalesAgentId_n) + ", " +
                    "fk_src_req_cur_n = " + (mnFkSrcRequiredCurrencyId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcRequiredCurrencyId_n) + ", " +
                    "fk_src_req_uom_n = " + (mnFkSrcRequiredUnitOfMeasureId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcRequiredUnitOfMeasureId_n) + ", " +
                    "fk_des_req_pay_met_n = " + (mnFkDesRequiredPayMethodId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkDesRequiredPayMethodId_n) + ", " +
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
    public SDbCustomer clone() throws CloneNotSupportedException {
        SDbCustomer registry = new SDbCustomer();

        registry.setPkCustomerId(this.getPkCustomerId());
        registry.setSrcCustomerId(this.getSrcCustomerId());
        registry.setDesCustomerId(this.getDesCustomerId());
        registry.setDesCustomerBranchId(this.getDesCustomerBranchId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setNameShort(this.getNameShort());
        registry.setTaxId(this.getTaxId());
        registry.setStreet(this.getStreet());
        registry.setNumberExt(this.getNumberExt());
        registry.setNumberInt(this.getNumberInt());
        registry.setNeighborhood(this.getNeighborhood());
        registry.setReference(this.getReference());
        registry.setLocality(this.getLocality());
        registry.setCounty(this.getCounty());
        registry.setSrcStateFk(this.getSrcStateFk());
        registry.setState(this.getState());
        registry.setSrcCountryFk(this.getSrcCountryFk());
        registry.setCountry(this.getCountry());
        registry.setZip(this.getZip());
        registry.setPhone(this.getPhone());
        registry.setFax(this.getFax());
        registry.setPayAccount(this.getPayAccount());
        registry.setCreditDays(this.getCreditDays());
        registry.setCreditLimit(this.getCreditLimit());
        registry.setCreditStatusCode(this.getCreditStatusCode());
        registry.setPayTermCode(this.getPayTermCode());
        registry.setSrcCustomerCurrencyFk_n(this.getSrcCustomerCurrencyFk_n());
        registry.setSrcCustomerUnitOfMeasureFk_n(this.getSrcCustomerUnitOfMeasureFk_n());
        registry.setSrcCustomerSalesAgentFk_n(this.getSrcCustomerSalesAgentFk_n());
        registry.setSrcRequiredCurrencyFk_n(this.getSrcRequiredCurrencyFk_n());
        registry.setSrcRequiredUnitOfMeasureFk_n(this.getSrcRequiredUnitOfMeasureFk_n());
        registry.setDesCfdiPaymentWay(this.getDesCfdiPaymentWay());
        registry.setDesCfdiCfdiUsage(this.getDesCfdiCfdiUsage());
        registry.setFirstEtlInsert(this.getFirstEtlInsert());
        registry.setLastEtlUpdate(this.getLastEtlUpdate());
        registry.setEtlIgnore(this.isEtlIgnore());
        registry.setActive(this.isActive());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkSrcCustomerCurrencyId_n(this.getFkSrcCustomerCurrencyId_n());
        registry.setFkSrcCustomerUnitOfMeasureId_n(this.getFkSrcCustomerUnitOfMeasureId_n());
        registry.setFkSrcCustomerSalesAgentId_n(this.getFkSrcCustomerSalesAgentId_n());
        registry.setFkSrcRequiredCurrencyId_n(this.getFkSrcRequiredCurrencyId_n());
        registry.setFkSrcRequiredUnitOfMeasureId_n(this.getFkSrcRequiredUnitOfMeasureId_n());
        registry.setFkDesRequiredPayMethodId_n(this.getFkDesRequiredPayMethodId_n());
        registry.setFkLastEtlLogId(this.getFkLastEtlLogId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
