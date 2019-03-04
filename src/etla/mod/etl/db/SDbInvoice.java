/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

import etla.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbInvoice extends SDbRegistryUser {

    protected int mnPkInvoiceId;
    protected int mnSrcInvoiceId;
    protected int mnDesInvoiceYearId;
    protected int mnDesInvoiceDocumentId;
    protected String msOriginalNumber;
    protected String msFinalSeries;
    protected String msFinalNumber;
    protected Date mtOriginalDate;
    protected Date mtFinalDate;
    protected String msPayAccount;
    protected int mnCreditDays;
    protected double mdOriginalAmount;
    protected double mdFinalAmount;
    protected double mdExchangeRate;
    protected int mnBatch;
    protected String msPaymentConditions;
    protected String msCustomerOrder;
    protected String msBillOfLading;
    protected String msSrcCustomerFk;
    protected int mnDesCustomerFk;
    protected int mnSrcSalesAgentFk;
    protected int mnDesSalesAgentFk;
    protected int mnSrcOriginalCurrencyFk;
    protected int mnSrcFinalCurrencyFk;
    protected int mnDesOriginalCurrencyFk;
    protected int mnDesFinalCurrencyFk;
    protected int mnDesPayMethodFk;
    protected String msDesCfdiZipIssue;
    protected String msDesCfdiTaxRegime;
    protected String msDesCfdiPaymentWay;
    protected String msDesCfdiCfdiUsage;
    protected Date mtFirstEtlInsert;
    protected Date mtLastEtlUpdate;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkSrcCustomerId;
    protected int mnFkSrcSalesAgentId_n;
    protected int mnFkSrcOriginalCurrencyId;
    protected int mnFkSrcFinalCurrencyId;
    protected int mnFkDesPayMethodId;
    protected int mnFkLastEtlLogId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbInvoiceRow> maChildRows;
    
    protected int mnAuxDesSalesSupervisorFk;
    
    public SDbInvoice() {
        super(SModConsts.A_INV);
    }

    /*
     * Public methods
     */

    public void setPkInvoiceId(int n) { mnPkInvoiceId = n; }
    public void setSrcInvoiceId(int n) { mnSrcInvoiceId = n; }
    public void setDesInvoiceYearId(int n) { mnDesInvoiceYearId = n; }
    public void setDesInvoiceDocumentId(int n) { mnDesInvoiceDocumentId = n; }
    public void setOriginalNumber(String s) { msOriginalNumber = s; }
    public void setFinalSeries(String s) { msFinalSeries = s; }
    public void setFinalNumber(String s) { msFinalNumber = s; }
    public void setOriginalDate(Date t) { mtOriginalDate = t; }
    public void setFinalDate(Date t) { mtFinalDate = t; }
    public void setPayAccount(String s) { msPayAccount = s; }
    public void setCreditDays(int n) { mnCreditDays = n; }
    public void setOriginalAmount(double d) { mdOriginalAmount = d; }
    public void setFinalAmount(double d) { mdFinalAmount = d; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setBatch(int n) { mnBatch = n; }
    public void setPaymentConditions(String s) { msPaymentConditions = s; }
    public void setCustomerOrder(String s) { msCustomerOrder = s; }
    public void setBillOfLading(String s) { msBillOfLading = s; }
    public void setSrcCustomerFk(String s) { msSrcCustomerFk = s; }
    public void setDesCustomerFk(int n) { mnDesCustomerFk = n; }
    public void setSrcSalesAgentFk(int n) { mnSrcSalesAgentFk = n; }
    public void setDesSalesAgentFk(int n) { mnDesSalesAgentFk = n; }
    public void setSrcOriginalCurrencyFk(int n) { mnSrcOriginalCurrencyFk = n; }
    public void setSrcFinalCurrencyFk(int n) { mnSrcFinalCurrencyFk = n; }
    public void setDesOriginalCurrencyFk(int n) { mnDesOriginalCurrencyFk = n; }
    public void setDesFinalCurrencyFk(int n) { mnDesFinalCurrencyFk = n; }
    public void setDesPayMethodFk(int n) { mnDesPayMethodFk = n; }
    public void setDesCfdiZipIssue(String s) { msDesCfdiZipIssue = s; }
    public void setDesCfdiTaxRegime(String s) { msDesCfdiTaxRegime = s; }
    public void setDesCfdiPaymentWay(String s) { msDesCfdiPaymentWay = s; }
    public void setDesCfdiCfdiUsage(String s) { msDesCfdiCfdiUsage = s; }
    public void setFirstEtlInsert(Date t) { mtFirstEtlInsert = t; }
    public void setLastEtlUpdate(Date t) { mtLastEtlUpdate = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkSrcCustomerId(int n) { mnFkSrcCustomerId = n; }
    public void setFkSrcSalesAgentId_n(int n) { mnFkSrcSalesAgentId_n = n; }
    public void setFkSrcOriginalCurrencyId(int n) { mnFkSrcOriginalCurrencyId = n; }
    public void setFkSrcFinalCurrencyId(int n) { mnFkSrcFinalCurrencyId = n; }
    public void setFkDesPayMethodId(int n) { mnFkDesPayMethodId = n; }
    public void setFkLastEtlLogId(int n) { mnFkLastEtlLogId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkInvoiceId() { return mnPkInvoiceId; }
    public int getSrcInvoiceId() { return mnSrcInvoiceId; }
    public int getDesInvoiceYearId() { return mnDesInvoiceYearId; }
    public int getDesInvoiceDocumentId() { return mnDesInvoiceDocumentId; }
    public String getOriginalNumber() { return msOriginalNumber; }
    public String getFinalSeries() { return msFinalSeries; }
    public String getFinalNumber() { return msFinalNumber; }
    public Date getOriginalDate() { return mtOriginalDate; }
    public Date getFinalDate() { return mtFinalDate; }
    public String getPayAccount() { return msPayAccount; }
    public int getCreditDays() { return mnCreditDays; }
    public double getOriginalAmount() { return mdOriginalAmount; }
    public double getFinalAmount() { return mdFinalAmount; }
    public double getExchangeRate() { return mdExchangeRate; }
    public int getBatch() { return mnBatch; }
    public String getPaymentConditions() { return msPaymentConditions; }
    public String getCustomerOrder() { return msCustomerOrder; }
    public String getBillOfLading() { return msBillOfLading; }
    public String getSrcCustomerFk() { return msSrcCustomerFk; }
    public int getDesCustomerFk() { return mnDesCustomerFk; }
    public int getSrcSalesAgentFk() { return mnSrcSalesAgentFk; }
    public int getDesSalesAgentFk() { return mnDesSalesAgentFk; }
    public int getSrcOriginalCurrencyFk() { return mnSrcOriginalCurrencyFk; }
    public int getSrcFinalCurrencyFk() { return mnSrcFinalCurrencyFk; }
    public int getDesOriginalCurrencyFk() { return mnDesOriginalCurrencyFk; }
    public int getDesFinalCurrencyFk() { return mnDesFinalCurrencyFk; }
    public int getDesPayMethodFk() { return mnDesPayMethodFk; }
    public String getDesCfdiZipIssue() { return msDesCfdiZipIssue; }
    public String getDesCfdiTaxRegime() { return msDesCfdiTaxRegime; }
    public String getDesCfdiPaymentWay() { return msDesCfdiPaymentWay; }
    public String getDesCfdiCfdiUsage() { return msDesCfdiCfdiUsage; }
    public Date getFirstEtlInsert() { return mtFirstEtlInsert; }
    public Date getLastEtlUpdate() { return mtLastEtlUpdate; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkSrcCustomerId() { return mnFkSrcCustomerId; }
    public int getFkSrcSalesAgentId_n() { return mnFkSrcSalesAgentId_n; }
    public int getFkSrcOriginalCurrencyId() { return mnFkSrcOriginalCurrencyId; }
    public int getFkSrcFinalCurrencyId() { return mnFkSrcFinalCurrencyId; }
    public int getFkDesPayMethodId() { return mnFkDesPayMethodId; }
    public int getFkLastEtlLogId() { return mnFkLastEtlLogId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public ArrayList<SDbInvoiceRow> getChildRows() { return maChildRows; }
    
    public void setAuxDesSalesSupervisorFk(int n) { mnAuxDesSalesSupervisorFk = n; }
    
    public int getAuxDesSalesSupervisorFk() { return mnAuxDesSalesSupervisorFk; }
    
    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkInvoiceId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkInvoiceId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkInvoiceId = 0;
        mnSrcInvoiceId = 0;
        mnDesInvoiceYearId = 0;
        mnDesInvoiceDocumentId = 0;
        msOriginalNumber = "";
        msFinalSeries = "";
        msFinalNumber = "";
        mtOriginalDate = null;
        mtFinalDate = null;
        msPayAccount = "";
        mnCreditDays = 0;
        mdOriginalAmount = 0;
        mdFinalAmount = 0;
        mdExchangeRate = 0;
        mnBatch = 0;
        msPaymentConditions = "";
        msCustomerOrder = "";
        msBillOfLading = "";
        msSrcCustomerFk = "";
        mnDesCustomerFk = 0;
        mnSrcSalesAgentFk = 0;
        mnDesSalesAgentFk = 0;
        mnSrcOriginalCurrencyFk = 0;
        mnSrcFinalCurrencyFk = 0;
        mnDesOriginalCurrencyFk = 0;
        mnDesFinalCurrencyFk = 0;
        mnDesPayMethodFk = 0;
        msDesCfdiZipIssue = "";
        msDesCfdiTaxRegime = "";
        msDesCfdiPaymentWay = "";
        msDesCfdiCfdiUsage = "";
        mtFirstEtlInsert = null;
        mtLastEtlUpdate = null;
        mbDeleted = false;
        mbSystem = false;
        mnFkSrcCustomerId = 0;
        mnFkSrcSalesAgentId_n = 0;
        mnFkSrcOriginalCurrencyId = 0;
        mnFkSrcFinalCurrencyId = 0;
        mnFkDesPayMethodId = 0;
        mnFkLastEtlLogId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildRows = new ArrayList<>();
        
        mnAuxDesSalesSupervisorFk = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_inv = " + mnPkInvoiceId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_inv = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkInvoiceId = 0;

        msSql = "SELECT COALESCE(MAX(id_inv), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkInvoiceId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
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
            mnSrcInvoiceId = resultSet.getInt("src_inv_id");
            mnDesInvoiceYearId = resultSet.getInt("des_inv_yea_id");
            mnDesInvoiceDocumentId = resultSet.getInt("des_inv_doc_id");
            msOriginalNumber = resultSet.getString("ori_num");
            msFinalSeries = resultSet.getString("fin_ser");
            msFinalNumber = resultSet.getString("fin_num");
            mtOriginalDate = resultSet.getDate("ori_dat");
            mtFinalDate = resultSet.getDate("fin_dat");
            msPayAccount = resultSet.getString("pay_acc");
            mnCreditDays = resultSet.getInt("cdt_day");
            mdOriginalAmount = resultSet.getDouble("ori_amt");
            mdFinalAmount = resultSet.getDouble("fin_amt");
            mdExchangeRate = resultSet.getDouble("exr");
            mnBatch = resultSet.getInt("batch");
            msPaymentConditions = resultSet.getString("pay_cnd");
            msCustomerOrder = resultSet.getString("cus_ord");
            msBillOfLading = resultSet.getString("bol");
            msSrcCustomerFk = resultSet.getString("src_cus_fk");
            mnDesCustomerFk = resultSet.getInt("des_cus_fk");
            mnSrcSalesAgentFk = resultSet.getInt("src_sal_agt_fk");
            mnDesSalesAgentFk = resultSet.getInt("des_sal_agt_fk");
            mnSrcOriginalCurrencyFk = resultSet.getInt("src_ori_cur_fk");
            mnSrcFinalCurrencyFk = resultSet.getInt("src_fin_cur_fk");
            mnDesOriginalCurrencyFk = resultSet.getInt("des_ori_cur_fk");
            mnDesFinalCurrencyFk = resultSet.getInt("des_fin_cur_fk");
            mnDesPayMethodFk = resultSet.getInt("des_pay_met_fk");
            msDesCfdiZipIssue = resultSet.getString("des_cfdi_zip_iss");
            msDesCfdiTaxRegime = resultSet.getString("des_cfdi_tax_reg");
            msDesCfdiPaymentWay = resultSet.getString("des_cfdi_pay_way");
            msDesCfdiCfdiUsage = resultSet.getString("des_cfdi_cfd_use");
            mtFirstEtlInsert = resultSet.getTimestamp("fst_etl_ins");
            mtLastEtlUpdate = resultSet.getTimestamp("lst_etl_upd");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkSrcCustomerId = resultSet.getInt("fk_src_cus");
            mnFkSrcSalesAgentId_n = resultSet.getInt("fk_src_sal_agt_n");
            mnFkSrcOriginalCurrencyId = resultSet.getInt("fk_src_ori_cur");
            mnFkSrcFinalCurrencyId = resultSet.getInt("fk_src_fin_cur");
            mnFkDesPayMethodId = resultSet.getInt("fk_des_pay_met");
            mnFkLastEtlLogId = resultSet.getInt("fk_lst_etl_log");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // Read aswell child registries:
            
            statement = session.getStatement().getConnection().createStatement();
            
            msSql = "SELECT id_row FROM " + SModConsts.TablesMap.get(SModConsts.A_INV_ROW) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbInvoiceRow child = new SDbInvoiceRow();
                child.read(session, new int[] { mnPkInvoiceId, resultSet.getInt(1) });
                maChildRows.add(child);
                
            }
            
            // Continue registry reading:

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
                    mnSrcInvoiceId + ", " + 
                    mnDesInvoiceYearId + ", " + 
                    mnDesInvoiceDocumentId + ", " + 
                    "'" + msOriginalNumber + "', " + 
                    "'" + msFinalSeries + "', " + 
                    "'" + msFinalNumber + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtOriginalDate) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtFinalDate) + "', " + 
                    "'" + msPayAccount + "', " + 
                    mnCreditDays + ", " + 
                    mdOriginalAmount + ", " + 
                    mdFinalAmount + ", " + 
                    mdExchangeRate + ", " + 
                    mnBatch + ", " + 
                    "'" + msPaymentConditions + "', " + 
                    "'" + msCustomerOrder + "', " + 
                    "'" + msBillOfLading + "', " + 
                    "'" + msSrcCustomerFk + "', " + 
                    mnDesCustomerFk + ", " + 
                    mnSrcSalesAgentFk + ", " + 
                    mnDesSalesAgentFk + ", " + 
                    mnSrcOriginalCurrencyFk + ", " + 
                    mnSrcFinalCurrencyFk + ", " + 
                    mnDesOriginalCurrencyFk + ", " + 
                    mnDesFinalCurrencyFk + ", " + 
                    mnDesPayMethodFk + ", " + 
                    "'" + msDesCfdiZipIssue + "', " + 
                    "'" + msDesCfdiTaxRegime + "', " + 
                    "'" + msDesCfdiPaymentWay + "', " + 
                    "'" + msDesCfdiCfdiUsage + "', " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkSrcCustomerId + ", " + 
                    (mnFkSrcSalesAgentId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcSalesAgentId_n) + ", " + 
                    mnFkSrcOriginalCurrencyId + ", " + 
                    mnFkSrcFinalCurrencyId + ", " + 
                    mnFkDesPayMethodId + ", " + 
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
                    //"id_inv = " + mnPkInvoiceId + ", " +
                    "src_inv_id = " + mnSrcInvoiceId + ", " +
                    "des_inv_yea_id = " + mnDesInvoiceYearId + ", " +
                    "des_inv_doc_id = " + mnDesInvoiceDocumentId + ", " +
                    "ori_num = '" + msOriginalNumber + "', " +
                    "fin_ser = '" + msFinalSeries + "', " +
                    "fin_num = '" + msFinalNumber + "', " +
                    "ori_dat = '" + SLibUtils.DbmsDateFormatDate.format(mtOriginalDate) + "', " +
                    "fin_dat = '" + SLibUtils.DbmsDateFormatDate.format(mtFinalDate) + "', " +
                    "pay_acc = '" + msPayAccount + "', " +
                    "cdt_day = " + mnCreditDays + ", " +
                    "ori_amt = " + mdOriginalAmount + ", " +
                    "fin_amt = " + mdFinalAmount + ", " +
                    "exr = " + mdExchangeRate + ", " +
                    "batch = " + mnBatch + ", " +
                    "pay_cnd = '" + msPaymentConditions + "', " +
                    "cus_ord = '" + msCustomerOrder + "', " +
                    "bol = '" + msBillOfLading + "', " +
                    "src_cus_fk = '" + msSrcCustomerFk + "', " +
                    "des_cus_fk = " + mnDesCustomerFk + ", " +
                    "src_sal_agt_fk = " + mnSrcSalesAgentFk + ", " +
                    "des_sal_agt_fk = " + mnDesSalesAgentFk + ", " +
                    "src_ori_cur_fk = " + mnSrcOriginalCurrencyFk + ", " +
                    "src_fin_cur_fk = " + mnSrcFinalCurrencyFk + ", " +
                    "des_ori_cur_fk = " + mnDesOriginalCurrencyFk + ", " +
                    "des_fin_cur_fk = " + mnDesFinalCurrencyFk + ", " +
                    "des_pay_met_fk = " + mnDesPayMethodFk + ", " +
                    "des_cfdi_zip_iss = '" + msDesCfdiZipIssue + "', " +
                    "des_cfdi_tax_reg = '" + msDesCfdiTaxRegime + "', " +
                    "des_cfdi_pay_way = '" + msDesCfdiPaymentWay + "', " +
                    "des_cfdi_cfd_use = '" + msDesCfdiCfdiUsage + "', " +
                    //"fst_etl_ins = " + "NOW()" + ", " +
                    "lst_etl_upd = " + "NOW()" + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_src_cus = " + mnFkSrcCustomerId + ", " +
                    "fk_src_sal_agt_n = " + (mnFkSrcSalesAgentId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkSrcSalesAgentId_n) + ", " +
                    "fk_src_ori_cur = " + mnFkSrcOriginalCurrencyId + ", " +
                    "fk_src_fin_cur = " + mnFkSrcFinalCurrencyId + ", " +
                    "fk_des_pay_met = " + mnFkDesPayMethodId + ", " +
                    "fk_lst_etl_log = " + mnFkLastEtlLogId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // Save aswell child registries:
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.A_INV_ROW) + " " + getSqlWhere();
        session.getStatement().execute(msSql);
        
        for (SDbInvoiceRow child : maChildRows) {
            child.setPkInvoiceId(mnPkInvoiceId);
            child.setPkRowId(SLibConsts.UNDEFINED);
            child.setRegistryNew(true);
            child.save(session);
        }
        
        // Continue registry saving:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbInvoice clone() throws CloneNotSupportedException {
        SDbInvoice registry = new SDbInvoice();

        registry.setPkInvoiceId(this.getPkInvoiceId());
        registry.setSrcInvoiceId(this.getSrcInvoiceId());
        registry.setDesInvoiceYearId(this.getDesInvoiceYearId());
        registry.setDesInvoiceDocumentId(this.getDesInvoiceDocumentId());
        registry.setOriginalNumber(this.getOriginalNumber());
        registry.setFinalSeries(this.getFinalSeries());
        registry.setFinalNumber(this.getFinalNumber());
        registry.setOriginalDate(this.getOriginalDate());
        registry.setFinalDate(this.getFinalDate());
        registry.setPayAccount(this.getPayAccount());
        registry.setCreditDays(this.getCreditDays());
        registry.setOriginalAmount(this.getOriginalAmount());
        registry.setFinalAmount(this.getFinalAmount());
        registry.setExchangeRate(this.getExchangeRate());
        registry.setBatch(this.getBatch());
        registry.setPaymentConditions(this.getPaymentConditions());
        registry.setCustomerOrder(this.getCustomerOrder());
        registry.setBillOfLading(this.getBillOfLading());
        registry.setSrcCustomerFk(this.getSrcCustomerFk());
        registry.setDesCustomerFk(this.getDesCustomerFk());
        registry.setSrcSalesAgentFk(this.getSrcSalesAgentFk());
        registry.setDesSalesAgentFk(this.getDesSalesAgentFk());
        registry.setSrcOriginalCurrencyFk(this.getSrcOriginalCurrencyFk());
        registry.setSrcFinalCurrencyFk(this.getSrcFinalCurrencyFk());
        registry.setDesOriginalCurrencyFk(this.getDesOriginalCurrencyFk());
        registry.setDesFinalCurrencyFk(this.getDesFinalCurrencyFk());
        registry.setDesPayMethodFk(this.getDesPayMethodFk());
        registry.setDesCfdiZipIssue(this.getDesCfdiZipIssue());
        registry.setDesCfdiTaxRegime(this.getDesCfdiTaxRegime());
        registry.setDesCfdiPaymentWay(this.getDesCfdiPaymentWay());
        registry.setDesCfdiCfdiUsage(this.getDesCfdiCfdiUsage());
        registry.setFirstEtlInsert(this.getFirstEtlInsert());
        registry.setLastEtlUpdate(this.getLastEtlUpdate());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkSrcCustomerId(this.getFkSrcCustomerId());
        registry.setFkSrcSalesAgentId_n(this.getFkSrcSalesAgentId_n());
        registry.setFkSrcOriginalCurrencyId(this.getFkSrcOriginalCurrencyId());
        registry.setFkSrcFinalCurrencyId(this.getFkSrcFinalCurrencyId());
        registry.setFkDesPayMethodId(this.getFkDesPayMethodId());
        registry.setFkLastEtlLogId(this.getFkLastEtlLogId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        for (SDbInvoiceRow child : maChildRows) {
            registry.getChildRows().add(child.clone());
        }
        
        registry.setAuxDesSalesSupervisorFk(this.getAuxDesSalesSupervisorFk());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
