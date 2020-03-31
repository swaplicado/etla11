/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mbps.data.SDataBizPartnerBranchContact;
import erp.mbps.data.SDataBizPartnerCategory;
import erp.mmkt.data.SDataCustomerBranchConfig;
import erp.mmkt.data.SDataCustomerConfig;
import erp.mod.SModSysConsts;
import etla.mod.SModConsts;
import etla.mod.cfg.db.SDbConfig;
import etla.mod.cfg.db.SDbUser;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public abstract class SEtlProcessCatCustomers {
    
    private static SDataBizPartnerCategory createBizPartnerCategory(final SGuiSession session, final SEtlPackage etlPackage, final ResultSet resultSet, final int idCurrency) throws Exception {
        SDataBizPartnerCategory dataBizPartnerCategory = new SDataBizPartnerCategory();
        
        //dataBizPartnerCategory.setPkBizPartnerId(...); // set by business partner
        dataBizPartnerCategory.setPkBizPartnerCategoryId(SModSysConsts.BPSS_CT_BP_CUS);
        dataBizPartnerCategory.setKey(SLibUtils.textTrim(resultSet.getString("CustomerNumber")));
        dataBizPartnerCategory.setCompanyKey("");
        dataBizPartnerCategory.setCreditLimit(resultSet.getDouble("CreditLimit"));
        dataBizPartnerCategory.setDaysOfCredit(SLibUtils.parseInt(resultSet.getString("PayTermCode")));
        dataBizPartnerCategory.setDaysOfGrace(0);
        dataBizPartnerCategory.setGuarantee(0);
        dataBizPartnerCategory.setInsurance(0);
        dataBizPartnerCategory.setDateStart(SLibTimeUtils.getBeginOfYear(etlPackage.PeriodStart));
        dataBizPartnerCategory.setDateEnd_n(null);
        dataBizPartnerCategory.setPaymentAccount("");
        dataBizPartnerCategory.setNumberExporter("");
        dataBizPartnerCategory.setDiotOperation("");
        dataBizPartnerCategory.setCfdiPaymentWay("");
        dataBizPartnerCategory.setCfdiCfdiUsage("");
        dataBizPartnerCategory.setIsCreditByUser(false);
        dataBizPartnerCategory.setIsGuaranteeInProcess(false);
        dataBizPartnerCategory.setIsInsuranceInProcess(false);
        dataBizPartnerCategory.setIsDeleted(false);
        dataBizPartnerCategory.setFkBizPartnerCategoryId(SModSysConsts.BPSS_CT_BP_CUS);
        dataBizPartnerCategory.setFkBizPartnerTypeId(SModSysConsts.BPSU_TP_BP_DEF);
        dataBizPartnerCategory.setFkCreditTypeId_n(SLibConsts.UNDEFINED);
        dataBizPartnerCategory.setFkRiskId_n(SLibConsts.UNDEFINED);
        dataBizPartnerCategory.setFkCfdAddendaTypeId(SModSysConsts.BPSS_TP_CFD_ADD_CFD_ADD_NA);
        dataBizPartnerCategory.setFkLanguageId_n(SLibConsts.UNDEFINED);
        dataBizPartnerCategory.setFkCurrencyId_n(idCurrency);
        dataBizPartnerCategory.setFkPaymentSystemTypeId_n(SLibConsts.UNDEFINED);
        dataBizPartnerCategory.setFkUserAnalystId_n(SLibConsts.UNDEFINED);
        dataBizPartnerCategory.setFkUserNewId(((SDbUser) session.getUser()).getDesUserId());
        dataBizPartnerCategory.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
        dataBizPartnerCategory.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
        //dataBizPartnerCategory.setUserNewTs(...);
        //dataBizPartnerCategory.setUserEditTs(...);
        //dataBizPartnerCategory.setUserDeleteTs(...);

        return dataBizPartnerCategory;
    }
    
    private static SDataCustomerConfig createCustomerConfig(final SGuiSession session, final SDbSalesAgent salesAgent) throws Exception {
        SDataCustomerConfig dataBizPartnerCustomerConfig = new SDataCustomerConfig();
        
        //dataBizPartnerCustomerConfig.setPkCustomerId(...); // set by business partner
        dataBizPartnerCustomerConfig.setIsFreeDiscountDoc(false);
        dataBizPartnerCustomerConfig.setIsFreeCommissions(false);
        dataBizPartnerCustomerConfig.setIsDeleted(false);
        dataBizPartnerCustomerConfig.setFkCustomerTypeId(SEtlConsts.SIIE_DEFAULT);
        dataBizPartnerCustomerConfig.setFkMarketSegmentId(SEtlConsts.SIIE_DEFAULT);
        dataBizPartnerCustomerConfig.setFkMarketSubsegmentId(SEtlConsts.SIIE_DEFAULT);
        dataBizPartnerCustomerConfig.setFkDistributionChannelId(SEtlConsts.SIIE_DEFAULT);
        dataBizPartnerCustomerConfig.setFkSalesAgentId_n(salesAgent == null ? SLibConsts.UNDEFINED : salesAgent.getDesSalesAgentId());
        dataBizPartnerCustomerConfig.setFkSalesSupervisorId_n(SLibConsts.UNDEFINED);
        dataBizPartnerCustomerConfig.setFkUserNewId(((SDbUser) session.getUser()).getDesUserId());
        dataBizPartnerCustomerConfig.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
        dataBizPartnerCustomerConfig.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
        //dataBizPartnerCustomerConfig.setUserNewTs(...);
        //dataBizPartnerCustomerConfig.setUserEditTs(...);
        //dataBizPartnerCustomerConfig.setUserDeleteTs(...);
        
        return dataBizPartnerCustomerConfig;
    }
    
    private static SDataCustomerBranchConfig createCustomerBranchConfig(final SGuiSession session) throws Exception {
        SDataCustomerBranchConfig dataBizPartnerCustomerBranchConfig = new SDataCustomerBranchConfig();
        
        //dataBizPartnerCustomerBranchConfig.setPkCustomerBranchId(...); // set by business-partner branch
        dataBizPartnerCustomerBranchConfig.setIsDeleted(false);
        dataBizPartnerCustomerBranchConfig.setFkSalesRouteId(SEtlConsts.SIIE_DEFAULT);
        dataBizPartnerCustomerBranchConfig.setFkSalesAgentId_n(SLibConsts.UNDEFINED);
        dataBizPartnerCustomerBranchConfig.setFkSalesSupervisorId_n(SLibConsts.UNDEFINED);
        dataBizPartnerCustomerBranchConfig.setFkUserNewId(((SDbUser) session.getUser()).getDesUserId());
        dataBizPartnerCustomerBranchConfig.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
        dataBizPartnerCustomerBranchConfig.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
        //dataBizPartnerCustomerBranchConfig.setUserNewTs(...);
        //dataBizPartnerCustomerBranchConfig.setUserEditTs(...);
        //dataBizPartnerCustomerBranchConfig.setUserDeleteTs(...);
        
        return dataBizPartnerCustomerBranchConfig;
    }
    
    public static String computeEtlCustomers(final SGuiSession session, final SEtlPackage etlPackage) throws Exception {
        int nCount = 0;
        int nClients = 0;
        int nBizPartnerId = 0;
        int nBizPartnerAliveId = 0;
        int nBizPartnerDeletedId = 0;
        int nBizPartnerBranchId = 0;
        int nSalesAgentId = 0;
        int nCustomerId = 0;
        int nAvistaCurrencyCustomerFk = 0;
        String sAvistaCustomerId = "";
        String sAvistaCustomerTaxId = "";
        String sAvistaCustomerName = "";
        String sAvistaCustomerTradename = "";
        String sAvistaCountry = "";
        String sAvistaCountryFk = "";
        String sAvistaState = "";
        String sAvistaStateFk = "";
        String sAvistaUnitOfMeasureCustomerFk = "";
        int nSiieCurrencyFk = 0;
        boolean bIsBizPartnerPerson = false;
        boolean bIsBizPartnerCustomer = false;
        String sql = "";
        String[] sqlWheres = null;
        Statement statementEtl = session.getStatement().getConnection().createStatement();
        Statement statementSiie = etlPackage.ConnectionSiie.createStatement();
        Statement statementAvista = etlPackage.ConnectionAvista.createStatement();
        ResultSet resultSetEtl = null;
        ResultSet resultSetSiie = null;
        ResultSet resultSetAvista = null;
        SDataBizPartner dataBizPartner = null;
        SDataBizPartnerCategory dataBizPartnerCategory = null;
        SDataBizPartnerBranch dataBizPartnerBranch = null;
        SDataBizPartnerBranchAddress dataBizPartnerBranchAddress = null;
        SDataBizPartnerBranchContact dataBizPartnerBranchContact = null;
        SDataCustomerConfig dataBizPartnerCustomerConfig = null;
        SDataCustomerBranchConfig dataBizPartnerCustomerBranchConfig = null;
        SDbSysCurrency dbSysCurrencyCustomer = null;
        SDbSysCurrency dbSysCurrencyRequired = null;
        SDbSysUnitOfMeasure dbSysUnitOfMeasureCustomer = null;
        SDbSysUnitOfMeasure dbSysUnitOfMeasureRequired = null;
        SDbConfigAvista dbConfigAvista = ((SDbConfig) session.getConfigSystem()).getDbConfigAvista();
        SDbSalesAgent dbSalesAgent = null;
        SDbCustomer dbCustomer = null;
        SEtlCatalogs etlCatalogs = null;
        
        etlPackage.EtlLog.setStep(SEtlConsts.STEP_CUS_STA);
        
        etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_NA);
        etlPackage.EtlLog.save(session);
        
        // I. Obtain sales agents list from Avista invoices:
        
        sql = "SELECT DISTINCT c.SalesUserKey, u.UserId, u.FullName "
                + "FROM dbo.CustomerInvoices AS ci "
                + "INNER JOIN dbo.Customers AS c ON c.CustomerId=ci.CustomerId "
                + "INNER JOIN dbo.Users AS u ON u.UserKey=c.SalesUserKey "
                + "WHERE CAST(ci.Created AS DATE) BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodEnd) + "' AND "
                + "ci.CurrentStatusKey IN (" + SEtlConsts.AVISTA_INV_STA_APP + ", " + SEtlConsts.AVISTA_INV_STA_ARC + ") AND "
                + "ci.CustomerInvoiceTypeKey=" + SEtlConsts.AVISTA_INV_TP_INV + " "
                + (etlPackage.InvoiceBatch == SLibConsts.UNDEFINED ? "" : "AND ci.BatchNumber=" + etlPackage.InvoiceBatch + " ")
                + "ORDER BY c.SalesUserKey, u.UserId ";
        resultSetAvista = statementAvista.executeQuery(sql);
        while (resultSetAvista.next()) {
            /****************************************************************/
            if (SEtlConsts.SHOW_DEBUG_MSGS) {
                System.out.println(SEtlConsts.TXT_SAL_AGT + " (" + ++nCount + "): " + SLibUtils.textTrim(resultSetAvista.getString("FullName")));
            }
            /****************************************************************/
            
            // From Avista's ID try to obtain sales agent on ETL:
            
            nSalesAgentId = 0;
            
            sql = "SELECT id_sal_agt "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.AU_SAL_AGT) + " "
                    + "WHERE src_sal_agt_id='" + resultSetAvista.getString("SalesUserKey") + "' "
                    + "ORDER BY id_sal_agt ";
            resultSetEtl = statementEtl.executeQuery(sql);
            if (resultSetEtl.next()) {
                nSalesAgentId = resultSetEtl.getInt(1);
            }
            
            try {
                if (nSalesAgentId == 0) {
                    // Sales agent is new on ETL:

                    statementEtl.execute("START TRANSACTION");
                    
                    dbSalesAgent = new SDbSalesAgent(); // set on save
                    //dbSalesAgent.setPkSalesAgentId(...); // set on save
                    dbSalesAgent.setSrcSalesAgentId(resultSetAvista.getInt("SalesUserKey"));
                    //dbSalesAgent.setDesSalesAgentId(..); // user defined
                    dbSalesAgent.setCode(SLibUtils.textToSql(resultSetAvista.getString("UserId")));
                    dbSalesAgent.setName(SLibUtils.textToSql(resultSetAvista.getString("FullName")));
                    //dbSalesAgent.setFirstEtlInsert(...); // set on save
                    //dbSalesAgent.setLastEtlUpdate(...); // set on save
                    dbSalesAgent.setDeleted(false);
                    dbSalesAgent.setSystem(false);
                    dbSalesAgent.setFkLastEtlLogId(etlPackage.EtlLog.getPkEtlLogId());
                    //dbSalesAgent.setFkUserInsertId(...); // set on save
                    //dbSalesAgent.setFkUserUpdateId(...); // set on save
                    //dbSalesAgent.setTsUserInsert(...); // set on save
                    //dbSalesAgent.setTsUserUpdate(...); // set on save
                    
                    dbSalesAgent.save(session);
                    
                    statementEtl.execute("COMMIT");
                }
                else {
                    // Sales agent already exists on ETL:

                    //dbSalesAgent = new SDbSalesAgent();
                }
            }
            catch (Exception e) {
                statementEtl.execute("ROLLBACK");
                throw e;
            }
        }
        
        etlCatalogs = new SEtlCatalogs(session, true, false);
        
        // Obtener la cantidad de clientes
        nCount = 0;
        
        sql = "SELECT DISTINCT COUNT(*) "
                + "FROM dbo.CustomerInvoices AS ci "
                + "INNER JOIN dbo.Customers AS c ON c.CustomerId=ci.CustomerId "
                + "LEFT OUTER JOIN dbo.StateCodes AS sc ON sc.StateCode=c.State "
                + "LEFT OUTER JOIN dbo.CountryCodes AS cc ON cc.CountryCode=c.Country "
                + "WHERE CAST(ci.Created AS DATE) BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodEnd) + "' AND "
                + "ci.CurrentStatusKey IN (" + SEtlConsts.AVISTA_INV_STA_APP + ", " + SEtlConsts.AVISTA_INV_STA_ARC + ") AND "
                + "ci.CustomerInvoiceTypeKey=" + SEtlConsts.AVISTA_INV_TP_INV + " "
                + (etlPackage.InvoiceBatch == SLibConsts.UNDEFINED ? "" : "AND ci.BatchNumber=" + etlPackage.InvoiceBatch + " ");
        resultSetAvista = statementAvista.executeQuery(sql);
        while (resultSetAvista.next()) {
            nClients = resultSetAvista.getInt(1);
        }
        
        // II. Obtain customers list from Avista invoices:
        sql = "SELECT DISTINCT c.CustomerId, c.TaxId, c.CustomerNumber, c.CustomerName, c.ShortName, c.Active, c.DeletedFlag, "
                + "c.Address1, c.Address2, c.Address3, c.AddressInternalNumber, c.County AS Neighborhood, c.City, c.District AS County, "
                + "c.State, sc.StateDescription, c.Country, cc.CountryDescription, c.Zip, c.Phone, c.Fax, "
                + "c.AddressReference, c.PayTermCode, c.CreditLimit, c.CreditStatusCode, c.DefaultPricePerCode, c.CurrencyKey, c.SalesUserKey "
                + "FROM dbo.CustomerInvoices AS ci "
                + "INNER JOIN dbo.Customers AS c ON c.CustomerId=ci.CustomerId "
                + "LEFT OUTER JOIN dbo.StateCodes AS sc ON sc.StateCode=c.State "
                + "LEFT OUTER JOIN dbo.CountryCodes AS cc ON cc.CountryCode=c.Country "
                + "WHERE CAST(ci.Created AS DATE) BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodEnd) + "' AND "
                + "ci.CurrentStatusKey IN (" + SEtlConsts.AVISTA_INV_STA_APP + ", " + SEtlConsts.AVISTA_INV_STA_ARC + ") AND "
                + "ci.CustomerInvoiceTypeKey=" + SEtlConsts.AVISTA_INV_TP_INV + " "
                + (etlPackage.InvoiceBatch == SLibConsts.UNDEFINED ? "" : "AND ci.BatchNumber=" + etlPackage.InvoiceBatch + " ")
                + "ORDER BY c.CustomerId, c.TaxId ";
        resultSetAvista = statementAvista.executeQuery(sql);
        while (resultSetAvista.next()) {
            sAvistaCustomerId = resultSetAvista.getString("CustomerId");
            sAvistaCustomerTaxId = SLibUtils.textToSql(resultSetAvista.getString("TaxId"));
            sAvistaCustomerName = SLibUtils.textToSql(resultSetAvista.getString("CustomerName")).replaceAll("'", "''");
            sAvistaCustomerTradename = SLibUtils.textToSql(resultSetAvista.getString("ShortName")).replaceAll("'", "''");
            
            String msgCustomer = "Cliente: '" + sAvistaCustomerName + "'; RFC: '" + sAvistaCustomerTaxId + "'; alias: '" + sAvistaCustomerTradename + "'; ID: '" + sAvistaCustomerId + "'.";
            
            /****************************************************************/
            if (SEtlConsts.SHOW_DEBUG_MSGS) {
                System.out.println(SEtlConsts.TXT_CUS + " (" + ++nCount + "): " + sAvistaCustomerName);
            }
            /****************************************************************/
            
            // Validate customer's tax ID:
            
            if (sAvistaCustomerTaxId.isEmpty() || (sAvistaCustomerTaxId.length() != SEtlConsts.RFC_LEN_PER && sAvistaCustomerTaxId.length() != SEtlConsts.RFC_LEN_ORG)) {
                throw new Exception(SEtlConsts.MSG_ERR_CUS_TAX_ID + msgCustomer);
            }
            
            // Validate customer's name:
            
            if (sAvistaCustomerName.isEmpty()) {
                throw new Exception(SEtlConsts.MSG_ERR_CUS_NAME + msgCustomer);
            }
            
            // Select customer's country:

            sAvistaCountryFk = resultSetAvista.getString("Country");
            sAvistaCountry = resultSetAvista.getString("CountryDescription");

            if (sAvistaCountryFk == null || sAvistaCountryFk.isEmpty()) {
                sAvistaCountryFk = dbConfigAvista.getSrcLocalCountryFk();
                sAvistaCountry = SEtlConsts.AvistaCountriesMap.get(sAvistaCountryFk);
                if (sAvistaCountry == null) {
                    throw new Exception(SEtlConsts.MSG_ERR_UNK_CTY + "'" + sAvistaCountryFk + "'.\n" + SEtlConsts.TXT_CUS + ": " + msgCustomer);
                }
            }
            sAvistaCountry = SLibUtils.textTrim(sAvistaCountry).toUpperCase();

            if (sAvistaCountryFk.compareTo(dbConfigAvista.getSrcLocalCountryFk()) != 0) {
                throw new Exception(SEtlConsts.MSG_ERR_UNK_CTY + "'" + sAvistaCountryFk + "'.\n" + SEtlConsts.TXT_CUS + ": " + msgCustomer); // by now, only local country allowed (i.e., MX)
            }

            // Select customer's state:
            
            sAvistaStateFk = resultSetAvista.getString("State");
            sAvistaState = resultSetAvista.getString("StateDescription");
            
            if (sAvistaStateFk == null || sAvistaStateFk.isEmpty()) {
                sAvistaStateFk = dbConfigAvista.getSrcLocalStateFk();
            }
            
            if (sAvistaState == null || sAvistaState.isEmpty()) {
                sAvistaState = SEtlConsts.AvistaStatesMap.get(sAvistaStateFk);
            }
            
            if (sAvistaState == null) {
                throw new Exception(SEtlConsts.MSG_ERR_UNK_STA + "'" + sAvistaStateFk + "'.\n" + SEtlConsts.TXT_CUS + ": " + msgCustomer);
            }
            
            sAvistaState = SLibUtils.textTrim(sAvistaState).toUpperCase();

            // Select customer's currency:

            nAvistaCurrencyCustomerFk = resultSetAvista.getInt("CurrencyKey");
            
            if (nAvistaCurrencyCustomerFk == 0) {
                dbSysCurrencyCustomer = null;
                dbSysCurrencyRequired = null;
            }
            else {
                dbSysCurrencyCustomer = etlCatalogs.getEtlCurrency(etlCatalogs.getEtlIdForCurrency(nAvistaCurrencyCustomerFk));
                dbSysCurrencyRequired = dbSysCurrencyCustomer.getPkCurrencyId() == dbConfigAvista.getFkSrcDefaultCurrencyId() ? null : dbSysCurrencyCustomer;
            }
            
            nSiieCurrencyFk = dbSysCurrencyCustomer == null || dbSysCurrencyCustomer.getDesCurrencyId() == dbConfigAvista.getDesLocalCurrencyFk() ? SLibConsts.UNDEFINED : dbSysCurrencyCustomer.getDesCurrencyId();

            // Select customer's unit of measure:

            sAvistaUnitOfMeasureCustomerFk = resultSetAvista.getString("DefaultPricePerCode") == null ? "" : resultSetAvista.getString("DefaultPricePerCode");
            
            if (sAvistaUnitOfMeasureCustomerFk.isEmpty()) {
                dbSysUnitOfMeasureCustomer = null;
                dbSysUnitOfMeasureRequired = null;
            }
            else {
                dbSysUnitOfMeasureCustomer = etlCatalogs.getEtlUnitOfMeasure(etlCatalogs.getEtlIdForUnitOfMeasure(sAvistaUnitOfMeasureCustomerFk));
                dbSysUnitOfMeasureRequired = dbSysUnitOfMeasureCustomer.getPkUnitOfMeasureId() == dbConfigAvista.getFkSrcDefaultUnitOfMeasureId() ? null : dbSysUnitOfMeasureCustomer;
            }
            
            // Select customer's sales agent:
            
            dbSalesAgent = etlCatalogs.getEtlSalesAgent(etlCatalogs.getEtlIdForSalesAgent(resultSetAvista.getInt("SalesUserKey")));
            if (dbSalesAgent != null && dbSalesAgent.getDesSalesAgentId() == 0) {
                throw new Exception(SEtlConsts.MSG_ERR_UNK_SAL_AGT + "'" + dbSalesAgent.getName() + "'.\n" + SEtlConsts.TXT_CUS + ": " + msgCustomer);
            }

            // II.1. Export business partner to SIIE:
            
            etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_CUS_AUX_1);
            etlPackage.EtlLog.save(session);

            // From SIIE, obtain business partner, preferably active and the oldest:
            
            nBizPartnerAliveId = 0;
            nBizPartnerDeletedId = 0;
            bIsBizPartnerPerson = sAvistaCustomerTaxId.length() == SEtlConsts.RFC_LEN_PER;
            bIsBizPartnerCustomer = false;
            
            // search SIIE's Business Partners:
            sqlWheres = new String[] {
                // a) by Customer ID:
                "ext_id='" + sAvistaCustomerId + "' ",
                // b) by Tax ID:
                "fiscal_id='" + sAvistaCustomerTaxId + "' ",
                // c) by Customer Name:
                "bp='" + sAvistaCustomerName + "' ",
                // d) by Customer Tradename:
                "bp_comm='" + sAvistaCustomerTradename + "' "
            };
            
            // search first actives then those deleted SIIE's Business Partners:
            queries:
            for (String where : sqlWheres) {
                sql = "SELECT id_bp, b_cus "
                        + "FROM erp.bpsu_bp "
                        + "WHERE " + where + "AND b_del = ? "
                        + "ORDER BY id_bp;";
                PreparedStatement preparedStatement = etlPackage.ConnectionSiie.prepareStatement(sql);
                
                for (int del = 0; del <= 1; del++) {
                    boolean deleted = del == 1; // 0 = false; 1 = true
                    preparedStatement.setBoolean(1, deleted);
                    resultSetSiie = preparedStatement.executeQuery();
                    while (resultSetSiie.next()) {
                        if (!deleted) {
                            nBizPartnerAliveId = resultSetSiie.getInt("id_bp");
                            bIsBizPartnerCustomer = resultSetSiie.getBoolean("b_cus");
                            break queries; // stop search
                        }
                        else {
                            if (nBizPartnerDeletedId == 0) {
                                nBizPartnerDeletedId = resultSetSiie.getInt("id_bp");
                                bIsBizPartnerCustomer = resultSetSiie.getBoolean("b_cus");
                                // continue searching for an active business partner...
                            }
                        }
                    }
                }
            }
            
            try {
                if (nBizPartnerAliveId == 0 && nBizPartnerDeletedId == 0) {
                    // No SIIE's business partner found!
                    
                    statementSiie.execute("START TRANSACTION");

                    // Create business-partner registry:

                    dataBizPartner = new SDataBizPartner();
                    
                    //dataBizPartner.setPkBizPartnerId(...); // set on save
                    dataBizPartner.setBizPartner(sAvistaCustomerName);
                    dataBizPartner.setBizPartnerCommercial(sAvistaCustomerTradename);
                    dataBizPartner.setLastname(bIsBizPartnerPerson ? sAvistaCustomerName : "");
                    dataBizPartner.setFirstname("");
                    dataBizPartner.setFiscalId(sAvistaCustomerTaxId); // keystone for ETL processing!
                    dataBizPartner.setFiscalFrgId("");
                    dataBizPartner.setAlternativeId("");
                    dataBizPartner.setExternalId(sAvistaCustomerId); // keystone for ETL processing!
                    dataBizPartner.setCodeBankSantander("");
                    dataBizPartner.setCodeBankBanBajio("");
                    dataBizPartner.setWeb("");
                    dataBizPartner.setIsCompany(false);
                    dataBizPartner.setIsSupplier(false);
                    dataBizPartner.setIsCustomer(true);
                    dataBizPartner.setIsCreditor(false);
                    dataBizPartner.setIsDebtor(false);
                    dataBizPartner.setIsAttributeBank(false);
                    dataBizPartner.setIsAttributeCarrier(false);
                    dataBizPartner.setIsAttributeEmployee(false);
                    dataBizPartner.setIsAttributeSalesAgent(false);
                    dataBizPartner.setIsAttributePartnerShareholder(false);
                    dataBizPartner.setIsAttributeRelatedParty(false);
                    dataBizPartner.setIsDeleted(false);
                    dataBizPartner.setFkBizPartnerIdentityTypeId(bIsBizPartnerPerson ? SModSysConsts.BPSS_TP_BP_IDY_PER : SModSysConsts.BPSS_TP_BP_IDY_ORG);
                    dataBizPartner.setFkTaxIdentityId(bIsBizPartnerPerson ? SModSysConsts.BPSS_TP_BP_IDY_PER : SModSysConsts.BPSS_TP_BP_IDY_ORG);
                    dataBizPartner.setFkFiscalBankId(SModSysConsts.FINS_FISCAL_BANK_NA);
                    dataBizPartner.setFkBizAreaId(SModSysConsts.BPSU_BA_DEF);
                    dataBizPartner.setFkUserNewId(((SDbUser) session.getUser()).getDesUserId());
                    dataBizPartner.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartner.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                    //dataBizPartner.setUserNewTs(...);
                    //dataBizPartner.setUserEditTs(...);
                    //dataBizPartner.setUserDeleteTs(...);

                    // Create business-partner-category registry:

                    dataBizPartnerCategory = createBizPartnerCategory(session, etlPackage, resultSetAvista, nSiieCurrencyFk);

                    //dataBizPartner.setDbmsCategorySettingsCo(...);
                    //dataBizPartner.setDbmsCategorySettingsSup(...);
                    dataBizPartner.setDbmsCategorySettingsCus(dataBizPartnerCategory);
                    //dataBizPartner.setDbmsCategorySettingsCdr(...);
                    //dataBizPartner.setDbmsCategorySettingsDbr(...);

                    // Create business-partner-customer configuration:

                    dataBizPartnerCustomerConfig = createCustomerConfig(session, dbSalesAgent);

                    dataBizPartner.setDbmsDataCustomerConfig(dataBizPartnerCustomerConfig);

                    // Create business-partner branch:

                    dataBizPartnerBranch = new SDataBizPartnerBranch();
                    
                    //dataBizPartnerBranch.setPkBizPartnerBranchId(...); // set on save
                    dataBizPartnerBranch.setBizPartnerBranch(SModSysConsts.TXT_HQ);
                    dataBizPartnerBranch.setCode("");
                    dataBizPartnerBranch.setIsAddressPrintable(true);
                    dataBizPartnerBranch.setIsDeleted(false);
                    //dataBizPartnerBranch.setFkBizPartnerId(...); // set by business partner
                    dataBizPartnerBranch.setFkBizPartnerBranchTypeId(SModSysConsts.BPSS_TP_BPB_HQ);
                    dataBizPartnerBranch.setFkTaxRegionId_n(SLibConsts.UNDEFINED);
                    dataBizPartnerBranch.setFkAddressFormatTypeId_n(SLibConsts.UNDEFINED);
                    dataBizPartnerBranch.setFkUserNewId(((SDbUser) session.getUser()).getDesUserId());
                    dataBizPartnerBranch.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartnerBranch.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                    //dataBizPartnerBranch.setUserNewTs();
                    //dataBizPartnerBranch.setUserEditTs();
                    //dataBizPartnerBranch.setUserDeleteTs();

                    // Create business-partner-branch address:

                    dataBizPartnerBranchAddress = new SDataBizPartnerBranchAddress();
                    
                    //dataBizPartnerBranchAddress.setPkBizPartnerBranchId(...); // set by business-partner branch
                    //dataBizPartnerBranchAddress.setPkAddressId(...); // set on save
                    dataBizPartnerBranchAddress.setAddress(SModSysConsts.TXT_OFFICIAL);
                    dataBizPartnerBranchAddress.setStreet(SLibUtils.textToSql(resultSetAvista.getString("Address1")).replaceAll("'", "''"));
                    dataBizPartnerBranchAddress.setStreetNumberExt(SLibUtils.textToSql(resultSetAvista.getString("Address2")).replaceAll("'", "''"));
                    dataBizPartnerBranchAddress.setStreetNumberInt(SLibUtils.textToSql(resultSetAvista.getString("AddressInternalNumber")).replaceAll("'", "''"));
                    dataBizPartnerBranchAddress.setNeighborhood(SLibUtils.textToSql(resultSetAvista.getString("Neighborhood")).replaceAll("'", "''"));
                    dataBizPartnerBranchAddress.setReference(SLibUtils.textToSql(resultSetAvista.getString("AddressReference")).replaceAll("'", "''"));
                    dataBizPartnerBranchAddress.setLocality(SLibUtils.textToSql(resultSetAvista.getString("City")));
                    dataBizPartnerBranchAddress.setCounty(SLibUtils.textToSql(resultSetAvista.getString("County")));
                    dataBizPartnerBranchAddress.setState(sAvistaState);
                    dataBizPartnerBranchAddress.setZipCode(SLibUtils.textToSql(resultSetAvista.getString("Zip")));
                    dataBizPartnerBranchAddress.setPoBox("");
                    dataBizPartnerBranchAddress.setIsDefault(true);
                    dataBizPartnerBranchAddress.setIsDeleted(false);
                    dataBizPartnerBranchAddress.setFkAddressTypeId(SModSysConsts.BPSS_TP_ADD_OFF);
                    dataBizPartnerBranchAddress.setFkCountryId_n(SLibConsts.UNDEFINED); // by now, only local country allowed (i.e., MX)
                    dataBizPartnerBranchAddress.setFkStateId_n(SLibConsts.UNDEFINED);
                    dataBizPartnerBranchAddress.setFkUserNewId(((SDbUser) session.getUser()).getDesUserId());
                    dataBizPartnerBranchAddress.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartnerBranchAddress.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                    //dataBizPartnerBranchAddress.setUserNewTs(...);
                    //dataBizPartnerBranchAddress.setUserEditTs(...);
                    //dataBizPartnerBranchAddress.setUserDeleteTs(...);

                    dataBizPartnerBranch.getDbmsBizPartnerBranchAddresses().add(dataBizPartnerBranchAddress);

                    // Create business-partner-branch-address contact:

                    dataBizPartnerBranchContact = new SDataBizPartnerBranchContact();
                    
                    //dataBizPartnerBranchContact.setPkBizPartnerBranchId(...); // set by business-partner branch
                    //dataBizPartnerBranchContact.setPkContactId(); // set on save
                    dataBizPartnerBranchContact.setContact("");
                    dataBizPartnerBranchContact.setContactPrefix("");
                    dataBizPartnerBranchContact.setContactSuffix("");
                    dataBizPartnerBranchContact.setLastname("");
                    dataBizPartnerBranchContact.setFirstname("");
                    dataBizPartnerBranchContact.setCharge("");
                    dataBizPartnerBranchContact.setTelAreaCode01("");
                    dataBizPartnerBranchContact.setTelNumber01(SLibUtils.textToSql(resultSetAvista.getString("Phone")));
                    dataBizPartnerBranchContact.setTelExt01("");
                    dataBizPartnerBranchContact.setTelAreaCode02("");
                    dataBizPartnerBranchContact.setTelNumber02(SLibUtils.textToSql(resultSetAvista.getString("Fax")));
                    dataBizPartnerBranchContact.setTelExt02("");
                    dataBizPartnerBranchContact.setTelAreaCode03("");
                    dataBizPartnerBranchContact.setTelNumber03("");
                    dataBizPartnerBranchContact.setTelExt03("");
                    dataBizPartnerBranchContact.setNextelId01("");
                    dataBizPartnerBranchContact.setNextelId02("");
                    dataBizPartnerBranchContact.setEmail01("");
                    dataBizPartnerBranchContact.setEmail02("");
                    dataBizPartnerBranchContact.setSkype01("");
                    dataBizPartnerBranchContact.setSkype02("");
                    dataBizPartnerBranchContact.setIsDeleted(false);
                    dataBizPartnerBranchContact.setPkContactTypeId(SModSysConsts.BPSS_TP_CON_ADM);
                    dataBizPartnerBranchContact.setPkTelephoneType01Id(SLibUtils.textToSql(resultSetAvista.getString("Phone")).isEmpty() ? SModSysConsts.BPSS_TP_TEL_NA : SModSysConsts.BPSS_TP_TEL_TEL);
                    dataBizPartnerBranchContact.setPkTelephoneType02Id(SLibUtils.textToSql(resultSetAvista.getString("Fax")).isEmpty() ? SModSysConsts.BPSS_TP_TEL_NA : SModSysConsts.BPSS_TP_TEL_FAX);
                    dataBizPartnerBranchContact.setPkTelephoneType03Id(SModSysConsts.BPSS_TP_TEL_NA);
                    dataBizPartnerBranchContact.setFkUserNewId(((SDbUser) session.getUser()).getDesUserId());
                    dataBizPartnerBranchContact.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                    dataBizPartnerBranchContact.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                    //dataBizPartnerBranchContact.setUserNewTs(...);
                    //dataBizPartnerBranchContact.setUserEditTs(...);
                    //dataBizPartnerBranchContact.setUserDeleteTs(...);

                    dataBizPartnerBranch.getDbmsBizPartnerBranchContacts().add(dataBizPartnerBranchContact);

                    // Create-business-partner-branch-customer configuration:

                    dataBizPartnerCustomerBranchConfig = createCustomerBranchConfig(session);

                    dataBizPartnerBranch.getDbmsDataCustomerBranchConfig().add(dataBizPartnerCustomerBranchConfig);

                    // Add business-partner branch to business partner:

                    dataBizPartner.getDbmsBizPartnerBranches().add(dataBizPartnerBranch);

                    // Save new SIIE's business partner:

                    if (dataBizPartner.save(etlPackage.ConnectionSiie) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SEtlConsts.MSG_ERR_SIIE_CUS_INS + msgCustomer);
                    }
                    
                    statementSiie.execute("COMMIT");
                    
                    nBizPartnerId = dataBizPartner.getPkBizPartnerId();
                }
                else {
                    // Business partner already exists on SIIE:
                    
                    nBizPartnerId = nBizPartnerAliveId != 0 ? nBizPartnerAliveId : nBizPartnerDeletedId;

                    dataBizPartner = new SDataBizPartner();
                    if (dataBizPartner.read(new int[] { nBizPartnerId }, statementSiie) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SEtlConsts.MSG_ERR_SIIE_CUS_QRY + msgCustomer);
                    }
                    
                    if (!dataBizPartner.getExternalId().equals(sAvistaCustomerId) ||
                            dataBizPartner.getIsDeleted() ||
                            dataBizPartner.getDbmsCategorySettingsCus() == null ||
                            dataBizPartner.getDbmsCategorySettingsCus().getIsDeleted()) {
                        
                        statementSiie.execute("START TRANSACTION");

                        dataBizPartner.setExternalId(sAvistaCustomerId); // keystone for ETL processing!
                        if (!bIsBizPartnerCustomer) {
                            dataBizPartner.setIsCustomer(true);
                        }
                        dataBizPartner.setIsDeleted(false);
                        //dataBizPartner.setFkUserNewId(...);
                        dataBizPartner.setFkUserEditId(/*((SDbUser) session.getUser()).getDesUserId()*/SDataConstantsSys.USRX_USER_NA); // to prevent confusion in Department of Credit
                        //dataBizPartner.setFkUserDeleteId(...);
                        
                        if (dataBizPartner.getDbmsCategorySettingsCus() == null) {
                            // Create business-partner-category registry:

                            dataBizPartnerCategory = createBizPartnerCategory(session, etlPackage, resultSetAvista, nSiieCurrencyFk);

                            //dataBizPartner.setDbmsCategorySettingsCo(...);
                            //dataBizPartner.setDbmsCategorySettingsSup(...);
                            dataBizPartner.setDbmsCategorySettingsCus(dataBizPartnerCategory);
                            //dataBizPartner.setDbmsCategorySettingsCdr(...);
                            //dataBizPartner.setDbmsCategorySettingsDbr(...);
                            
                            // Create business-partner-customer configuration:

                            dataBizPartnerCustomerConfig = createCustomerConfig(session, dbSalesAgent);

                            dataBizPartner.setDbmsDataCustomerConfig(dataBizPartnerCustomerConfig);
                            
                            // Create-business-partner-branch-customer configuration:
                            
                            dataBizPartnerCustomerBranchConfig = createCustomerBranchConfig(session);

                            dataBizPartner.getDbmsHqBranch().getDbmsDataCustomerBranchConfig().add(dataBizPartnerCustomerBranchConfig);
                        }
                        
                        if (dataBizPartner.getDbmsCategorySettingsCus().getIsDeleted()) {
                            dataBizPartnerCategory = dataBizPartner.getDbmsCategorySettingsCus();
                            dataBizPartnerCategory.setIsDeleted(false);
                            //dataBizPartnerCategory.setFkUserNewId(...);
                            dataBizPartnerCategory.setFkUserEditId(/*((SDbUser) session.getUser()).getDesUserId()*/SDataConstantsSys.USRX_USER_NA); // to prevent confusion in Department of Credit
                            //dataBizPartnerCategory.setFkUserDeleteId(...);
                        }
                        
                        if (dataBizPartner.save(etlPackage.ConnectionSiie) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SEtlConsts.MSG_ERR_SIIE_CUS_UPD + msgCustomer);
                        }
                        
                        statementSiie.execute("COMMIT");
                    }
                }
                
                nBizPartnerBranchId = dataBizPartner.getDbmsHqBranch().getPkBizPartnerBranchId();
            }
            catch (Exception e) {
                statementSiie.execute("ROLLBACK");
                throw e;
            }
            
            // II.2. Export business partner to ETL:
            
            etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_CUS_AUX_2);
            etlPackage.EtlLog.save(session);
            
            // From Avista obtain customer:
            
            nCustomerId = 0;
            
            sql = "SELECT id_cus "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.AU_CUS) + " "
                    + "WHERE src_cus_id='" + sAvistaCustomerId + "' "
                    + "ORDER BY id_cus ";
            resultSetEtl = statementEtl.executeQuery(sql);
            if (resultSetEtl.next()) {
                nCustomerId = resultSetEtl.getInt(1);
            }
            
            try {
                if (nCustomerId == 0) {
                    // Customer is new on ETL:

                    statementEtl.execute("START TRANSACTION");
                    
                    dbCustomer = new SDbCustomer();
                    //dbCustomersetPkCustomerId(...); // set on save
                    dbCustomer.setSrcCustomerId(sAvistaCustomerId);
                    dbCustomer.setDesCustomerId(nBizPartnerId); // user defined, but default value set
                    dbCustomer.setDesCustomerBranchId(nBizPartnerBranchId); // user defined, but default value set
                    dbCustomer.setCode(SLibUtils.textTrim(resultSetAvista.getString("CustomerNumber")));
                    dbCustomer.setName(sAvistaCustomerName);
                    dbCustomer.setNameShort(sAvistaCustomerTradename);
                    dbCustomer.setTaxId(sAvistaCustomerTaxId);
                    dbCustomer.setStreet(SLibUtils.textToSql(resultSetAvista.getString("Address1")).replaceAll("'", "''"));
                    dbCustomer.setNumberExt(SLibUtils.textToSql(resultSetAvista.getString("Address2")).replaceAll("'", "''"));
                    dbCustomer.setNumberInt(SLibUtils.textToSql(resultSetAvista.getString("AddressInternalNumber")).replaceAll("'", "''"));
                    dbCustomer.setNeighborhood(SLibUtils.textToSql(resultSetAvista.getString("Neighborhood")).replaceAll("'", "''"));
                    dbCustomer.setReference(SLibUtils.textToSql(resultSetAvista.getString("AddressReference")).replaceAll("'", "''"));
                    dbCustomer.setLocality(SLibUtils.textToSql(resultSetAvista.getString("City")));
                    dbCustomer.setCounty(SLibUtils.textToSql(resultSetAvista.getString("County")));
                    dbCustomer.setSrcStateFk(resultSetAvista.getString("State") == null ? "" : SLibUtils.textToSql(resultSetAvista.getString("State"))); // preserve original source value, even if null or not set
                    dbCustomer.setState(sAvistaState); // original or customized value
                    dbCustomer.setSrcCountryFk(resultSetAvista.getString("Country") == null ? "" : SLibUtils.textToSql(resultSetAvista.getString("Country"))); // preserve original source value, even if null or not set
                    dbCustomer.setCountry(sAvistaCountry); // original or customized value
                    dbCustomer.setZip(SLibUtils.textToSql(resultSetAvista.getString("Zip")));
                    dbCustomer.setPhone(SLibUtils.textToSql(resultSetAvista.getString("Phone")));
                    dbCustomer.setFax(SLibUtils.textToSql(resultSetAvista.getString("Fax")));
                    dbCustomer.setPayAccount(""); // user defined, but default value set
                    dbCustomer.setCreditDays(SLibUtils.parseInt(resultSetAvista.getString("PayTermCode")));
                    dbCustomer.setCreditLimit(resultSetAvista.getDouble("CreditLimit"));
                    dbCustomer.setCreditStatusCode(SLibUtils.textToSql(resultSetAvista.getString("CreditStatusCode")));
                    dbCustomer.setPayTermCode(SLibUtils.textToSql(resultSetAvista.getString("PayTermCode")));
                    dbCustomer.setSrcCustomerCurrencyFk_n(nAvistaCurrencyCustomerFk);
                    dbCustomer.setSrcCustomerUnitOfMeasureFk_n(sAvistaUnitOfMeasureCustomerFk);
                    dbCustomer.setSrcCustomerSalesAgentFk_n(resultSetAvista.getInt("SalesUserKey"));
                    dbCustomer.setSrcRequiredCurrencyFk_n(dbSysCurrencyRequired == null ? SLibConsts.UNDEFINED : dbSysCurrencyRequired.getSrcCurrencyId()); // user defined, but default value set
                    dbCustomer.setSrcRequiredUnitOfMeasureFk_n(dbSysUnitOfMeasureRequired == null ? "" : dbSysUnitOfMeasureRequired.getSrcUnitOfMeasureId()); // user defined, but default value set
                    //dbCustomersetFirstEtlInsert(...); // set on save
                    //dbCustomersetLastEtlUpdate(...); // set on save
                    dbCustomer.setEtlIgnore(false);
                    dbCustomer.setActive(resultSetAvista.getString("Active").compareTo(SEtlConsts.AVISTA_BOOL_Y) == 0);
                    dbCustomer.setDeleted(resultSetAvista.getString("DeletedFlag") == null ? false : resultSetAvista.getString("DeletedFlag").compareTo(SEtlConsts.AVISTA_BOOL_Y) == 0);
                    dbCustomer.setSystem(false);
                    dbCustomer.setFkSrcCustomerCurrencyId_n(dbSysCurrencyCustomer == null ? SLibConsts.UNDEFINED : dbSysCurrencyCustomer.getPkCurrencyId());
                    dbCustomer.setFkSrcCustomerUnitOfMeasureId_n(dbSysUnitOfMeasureCustomer == null ? SLibConsts.UNDEFINED : dbSysUnitOfMeasureCustomer.getPkUnitOfMeasureId());
                    dbCustomer.setFkSrcCustomerSalesAgentId_n(dbSalesAgent == null ? SLibConsts.UNDEFINED : dbSalesAgent.getPkSalesAgentId());
                    dbCustomer.setFkSrcRequiredCurrencyId_n(dbSysCurrencyRequired == null ? SLibConsts.UNDEFINED : dbSysCurrencyRequired.getPkCurrencyId()); // user defined, but default value set
                    dbCustomer.setFkSrcRequiredUnitOfMeasureId_n(dbSysUnitOfMeasureRequired == null ? SLibConsts.UNDEFINED : dbSysUnitOfMeasureRequired.getPkUnitOfMeasureId()); // user defined, but default value set
                    dbCustomer.setFkDesRequiredPayMethodId_n(SLibConsts.UNDEFINED);
                    dbCustomer.setFkLastEtlLogId(etlPackage.EtlLog.getPkEtlLogId());
                    //dbCustomersetFkUserInsertId(...); // set on save
                    //dbCustomersetFkUserUpdateId(...); // set on save
                    //dbCustomersetTsUserInsert(...); // set on save
                    //dbCustomersetTsUserUpdate(...); // set on save
                    
                    dbCustomer.save(session);
                    
                    statementEtl.execute("COMMIT");
                }
                else {
                    // Customer already exists on ETL:

                    //customer = new SDbCustomer();
                }
            }
            catch (Exception e) {
                statementEtl.execute("ROLLBACK");
                throw e;
            }

            etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_CUS_AUX_3);
            etlPackage.EtlLog.save(session);
        }
        
        String message = nCount + " clientes exportados de " + nClients + " encontrados.\n";
        
        etlPackage.EtlLog.setStep(SEtlConsts.STEP_CUS_END);
        
        etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_NA);
        etlPackage.EtlLog.save(session);
        
        session.notifySuscriptors(SModConsts.AU_SAL_AGT);
        session.notifySuscriptors(SModConsts.AU_CUS);
        
        return message;
    }
}
