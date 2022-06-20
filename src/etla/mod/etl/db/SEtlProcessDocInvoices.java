/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

import cfd.DCfdConsts;
import cfd.ver33.DCfdi33Catalogs;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mitm.data.SDataItem;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsCfd;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.SDataDpsEntryNotes;
import erp.mtrn.data.SDataDpsEntryTax;
import erp.mtrn.data.SDataDpsNotes;
import etla.mod.SModConsts;
import etla.mod.SModSysConsts;
import etla.mod.cfg.db.SDbConfig;
import etla.mod.cfg.db.SDbUser;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public class SEtlProcessDocInvoices {
    
    public static String computeEtlInvoices(final SGuiSession session, final SEtlPackage etlPackage) throws Exception {
        ArrayList<SDbExtraChargePeriod> array = SEtlUtils.getExtraChargePeriods(session, etlPackage.DateIssue);
        
        // Avista invoices variables:
        
        int nInvoicesCount = 0;
        int nInvoces = 0;
        int idInvoice = 0;
        int idInvoiceSalesAgentDes = 0;
        int idInvoiceSalesSupervisorDes = 0;
        int idInvoicePayMethodDes = 0;
        String sInvoicePayAccountDes = "";
        int idInvoiceCurrencySrc = 0;
        int idInvoiceCurrencyReq = 0;
        boolean isInvoiceExported = false;
        HashSet<Integer> setLineCurrencySrcIds = new HashSet<>();
        String sInvoiceErrMsg = "";
        String sInvoiceItemErrMsg = "";
        
        double dInvoiceAmountSrc = 0;
        double dInvoiceAmountReq = 0;
        double dInvoiceExchangeRate = 0;
        double dFactorCurrency = 0;
        SDbCustomer dbInvoiceCustomer = null;
        SDbSalesAgent dbInvoiceSalesAgent = null;
        SDbSysCurrency dbInvoiceCurrencySrc = null;
        SDbSysCurrency dbInvoiceCurrencyReq = null;
        
        // Miscellaneous variables:
        
        int nMiscDecsAmount = 0;
        int nMiscDecsAmountUnit = 0;
        int nMiscDefaultSiieUnitId = 0;
        double dMisc1kFeetTo1kMeters = 0;
        
        // Avista invoice row (line) variables:
        
        double dLineUnits = 0;      // units
        double dLinePieces = 0;     // pce
        double dLineArea = 0;       // 1k·m²
        double dLineWeight = 0;     // kg
        double dLineUnitsSrc = 0;
        double dLineUnitsReq = 0;
        double dLinePriceReq = 0;
        double dLineAmountReq = 0;
        boolean isLineUnitEqual = false;
        String sLinePricePerCode = "";  // for dbo.CustomerInvoiceItems.PricePer, i.e., price per unit of measure (provided by code)
        
        // SIIE & ETL invoice row variables:
        
        double dEntryArea = 0;      // m²
        double dEntryWeight = 0;    // kg
        double dEntryQuantity = 0;
        double dEntryPriceUnitOrigCy = 0;
        double dEntryPriceUnitCy = 0;
        double dEntryPriceUnit = 0;
        double dEntryTotal = 0;
        double dEntryTaxCharged = 0;
        double dEntryTaxRetained = 0;
        double dEntrySubtotal = 0;
        
        // SIIE & ETL data registries:
        
        SDbItem dbLineItem = null;
        SDbSysUnitOfMeasure dbLineUnitOfMeasureSrc = null;
        SDbSysUnitOfMeasure dbLineUnitOfMeasureReq = null;
        SDataBizPartner dataBizPartnerCompany = null;
        SDataBizPartner dataBizPartnerCustomer = null;
        SDataItem dataItem = null;
        SDataDps dataDps = null;
        SDataDpsCfd dataDpsCfd = null;
        SDataDpsNotes dataDpsNotes = null;
        SDataDpsEntry dataDpsEntry = null;
        SDataDpsEntryTax dataDpsEntryTax = null;
        SDataDpsEntryNotes dataDpsEntryNotes = null;
        
        // ETL-process variables:
        
        String sql = "";
        Statement stEtl = session.getStatement().getConnection().createStatement();
        Statement stSiie = etlPackage.ConnectionSiie.createStatement();
        Statement stAvistaInvoiceList = etlPackage.ConnectionAvista.createStatement();
        Statement stAvistaInvoiceListRows = etlPackage.ConnectionAvista.createStatement();
        Statement stAvistaInvoiceData = etlPackage.ConnectionAvista.createStatement();
        ResultSet rsEtl = null;
        ResultSet rsSiie = null;
        ResultSet rsAvistaInvoiceList = null;
        ResultSet rsAvistaInvoiceListRows = null;
        ResultSet rsAvistaInvoiceData = null;
        SDbInvoice dbInvoice = null;
        SDbInvoiceRow dbInvoiceRow = null;
        SDbConfigAvista dbConfigAvista = ((SDbConfig) session.getConfigSystem()).getDbConfigAvista();
        SEtlCatalogs etlCatalogs = null;
        
        // ETL-process start-up:
        
        etlPackage.EtlLog.setStep(SEtlConsts.STEP_INV_STA);
        
        etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_NA);
        etlPackage.EtlLog.save(session);
        
        // Validate issue period is open:
        
        if (!SEtlUtils.isSiiePeriodOpen(SLibTimeUtils.digestMonth(etlPackage.DateIssue), etlPackage.ConnectionSiie.createStatement())) {
            throw new Exception(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
        }
        
        // Prepare miscellaneous values:
        
        etlCatalogs = new SEtlCatalogs(session, true, true);
        dataBizPartnerCompany = new SDataBizPartner();
        if (dataBizPartnerCompany.read(new int[] { dbConfigAvista.getDesCompanyFk() }, stSiie) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SEtlConsts.MSG_ERR_SIIE_COM_QRY + "'" + dbConfigAvista.getDesCompanyFk() + "'.");
        }
        
        nMiscDecsAmount = SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits();
        nMiscDecsAmountUnit = SLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits();
        nMiscDefaultSiieUnitId = ((SDbSysUnitOfMeasure) etlCatalogs.getEtlUnitOfMeasure(dbConfigAvista.getFkSrcDefaultUnitOfMeasureId())).getDesUnitOfMeasureId();
        dMisc1kFeetTo1kMeters = ((SDbSysUnitOfMeasure) etlCatalogs.getEtlUnitOfMeasure(SModSysConsts.AS_UOM_MSF)).getConversionFactor();
        
        // Get invoices count:
        
        sql = "SELECT COUNT(*) "
                + "FROM dbo.CustomerInvoices AS ci "
                + "WHERE CAST(ci.Created AS DATE) BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodEnd) + "' AND "
                + "ci.CurrentStatusKey IN (" + SEtlConsts.AVISTA_INV_STA_APP + ", " + SEtlConsts.AVISTA_INV_STA_ARC + ") AND "
                + "ci.CustomerInvoiceTypeKey=" + SEtlConsts.AVISTA_INV_TP_INV + " "
                + (etlPackage.InvoiceBatch == SLibConsts.UNDEFINED ? "" : "AND ci.BatchNumber=" + etlPackage.InvoiceBatch + " ");
        
        rsAvistaInvoiceList = stAvistaInvoiceList.executeQuery(sql);
        while (rsAvistaInvoiceList.next()) {
            nInvoces = rsAvistaInvoiceList.getInt(1);
        }
        
        // Obtain invoices list from Avista:
        
        sql = "SELECT ci.CustomerInvoiceKey, ci.InvoiceNumber, ci.Created AS InvoiceCreated, ci.CustomerId, ci.Description "
                + "FROM dbo.CustomerInvoices AS ci "
                + "WHERE CAST(ci.Created AS DATE) BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodEnd) + "' AND "
                + "ci.CurrentStatusKey IN (" + SEtlConsts.AVISTA_INV_STA_APP + ", " + SEtlConsts.AVISTA_INV_STA_ARC + ") AND "
                + "ci.CustomerInvoiceTypeKey=" + SEtlConsts.AVISTA_INV_TP_INV + " "
                + (etlPackage.InvoiceBatch == SLibConsts.UNDEFINED ? "" : "AND ci.BatchNumber=" + etlPackage.InvoiceBatch + " ")
                + "ORDER BY CAST(ci.Created AS DATE), ci.InvoiceNumber, ci.CustomerInvoiceKey ";
        
        rsAvistaInvoiceList = stAvistaInvoiceList.executeQuery(sql);
        while (rsAvistaInvoiceList.next()) {
            /****************************************************************/
            if (SEtlConsts.SHOW_DEBUG_MSGS) {
                System.out.println(SEtlConsts.TXT_INV + " (" + ++nInvoicesCount + "): " + rsAvistaInvoiceList.getInt("CustomerInvoiceKey"));
            }
            /****************************************************************/
            
            // On ETL, check if Avista invoice does not exist or has not been exported yet:
            
            etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_INV_AUX_10);
            etlPackage.EtlLog.save(session);

            idInvoice = 0;
            isInvoiceExported = false;
            
            //int idYear = 0;   // used for debugging purposes
            //int idDoc = 0;    // used for debugging purposes
            
            sql = "SELECT id_inv, des_inv_yea_id, des_inv_doc_id "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.A_INV) + " "
                    + "WHERE src_inv_id=" + rsAvistaInvoiceList.getInt("CustomerInvoiceKey") + " AND b_del=0 "
                    + "ORDER BY id_inv DESC "; // descendent order, the newest record appears first!
            
            rsEtl = stEtl.executeQuery(sql);
            if (rsEtl.next()) {
                idInvoice = rsEtl.getInt("id_inv");
                isInvoiceExported = rsEtl.getInt("des_inv_yea_id") != 0 && rsEtl.getInt("des_inv_doc_id") != 0;
                //idYear = rsEtl.getInt("des_inv_yea_id");  // used for debugging purposes
                //idDoc = rsEtl.getInt("des_inv_doc_id");   // used for debugging purposes
            }
            
            /****************************************************************/
            // used for debugging purposes:
            //if (SEtlConsts.SHOW_DEBUG_MSGS) {
            //    System.out.println(SEtlConsts.TXT_INV + " (# " + ++nInvoicesCount + "): Avista Key = <" + rsAvistaInvoiceList.getInt("CustomerInvoiceKey") + ">; ETL ID = <" + idInvoice + ">; already exported to SIIE? = <" + isInvoiceExported + "> (" + idYear + "-" + idDoc + ")");
            //}
            /****************************************************************/
            
            if (idInvoice == 0 || !isInvoiceExported) {
                // Avista invoice does not exist or has not been exported yet into ETL:
            
                idInvoiceSalesAgentDes = 0;
                idInvoiceSalesSupervisorDes = 0;
                idInvoicePayMethodDes = 0;
                sInvoicePayAccountDes = "";
                idInvoiceCurrencySrc = 0;
                idInvoiceCurrencyReq = 0;
                setLineCurrencySrcIds.clear();
                sInvoiceErrMsg = "\n(" + SEtlConsts.TXT_INV + " " + SEtlConsts.TXT_SYS_AVISTA + ": ID=" + rsAvistaInvoiceList.getInt("CustomerInvoiceKey") + "; #" + rsAvistaInvoiceList.getString("InvoiceNumber") + "; '" + rsAvistaInvoiceList.getString("Description") + "')";

                dInvoiceAmountSrc = 0;
                dInvoiceAmountReq = 0;
                dInvoiceExchangeRate = 0;

                // Set invoice's customer registry:
                
                dbInvoiceCustomer = SEtlUtils.getEtlCustomer(session, rsAvistaInvoiceList.getString("CustomerId"));
                if (dbInvoiceCustomer == null) {
                    throw new Exception(SEtlConsts.MSG_ERR_UNK_CUS + "\n"
                            + SEtlConsts.TXT_MISC_ID + "='" + rsAvistaInvoiceList.getString("CustomerId") + "'."
                            + sInvoiceErrMsg);
                }
                else if (dbInvoiceCustomer.isEtlIgnore()) {
                    continue;   // customer is set to be ignored on ETL process
                }
                
                // Get business partner of invoice's customer registry (method and account of payment set on SIIE, when set, have priority):
                
                dataBizPartnerCustomer = new SDataBizPartner();
                if (dataBizPartnerCustomer.read(new int[] { dbInvoiceCustomer.getDesCustomerId() }, stSiie) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SEtlConsts.MSG_ERR_SIIE_CUS_QRY + "'" + dbInvoiceCustomer.getName() + "'."
                            + sInvoiceErrMsg);
                }
                else if (dataBizPartnerCustomer.getIsDeleted() || dataBizPartnerCustomer.getDbmsCategorySettingsCus().getIsDeleted()) {
                    throw new Exception(SEtlConsts.MSG_ERR_SIIE_CUS_STA + "'" + dbInvoiceCustomer.getName() + "'.\n"
                            + SEtlConsts.MSG_ERR_REC_STA_DEL
                            + sInvoiceErrMsg); // business partner record (at least as customer) is deleted
                }

                // Set invoice's pay method & account from SIIE customer's registries:
                
                idInvoiceSalesAgentDes = dataBizPartnerCustomer.getDbmsDataCustomerConfig().getFkSalesAgentId_n();
                idInvoiceSalesSupervisorDes = dataBizPartnerCustomer.getDbmsDataCustomerConfig().getFkSalesSupervisorId_n();
                
                // Set invoice's pay method & account from SIIE customer's registries:
                
                idInvoicePayMethodDes = dataBizPartnerCustomer.getDbmsCategorySettingsCus().getFkPaymentSystemTypeId_n();  // current SIIE's settings have preference
                if (idInvoicePayMethodDes == SLibConsts.UNDEFINED) {
                    idInvoicePayMethodDes = dbInvoiceCustomer.getFkDesRequiredPayMethodId_n();
                    if (idInvoicePayMethodDes == SLibConsts.UNDEFINED) {
                        idInvoicePayMethodDes = dbConfigAvista.getFkDesDefaultPayMethodId();
                    }
                }
                
                sInvoicePayAccountDes = dataBizPartnerCustomer.getDbmsCategorySettingsCus().getPaymentAccount();   // current SIIE's settings have preference
                if (sInvoicePayAccountDes.isEmpty()) {
                    sInvoicePayAccountDes = dbInvoiceCustomer.getPayAccount();
                }
                
                // Explore invoice items and currencies row by row (i.e.,  line by line):
                
                sql = "SELECT cii.ItemNumber, cii.LineAmount, cii.CurrencyKey, cii.ExchangeToLocal, "
                        + "pe.Flute, pe.PlantBoardTypeKey "
                        + "FROM dbo.CustomerInvoices AS ci "
                        + "INNER JOIN dbo.CustomerInvoiceItems AS cii ON cii.CustomerInvoiceKey=ci.CustomerInvoiceKey "
                        + "INNER JOIN dbo.CustomerOrder AS co ON co.OrderKey=cii.OrderKey "
                        + "INNER JOIN dbo.PlantEst AS pe ON pe.EstKey=co.EstKey "
                        + "WHERE ci.CustomerInvoiceKey=" + rsAvistaInvoiceList.getInt("CustomerInvoiceKey") + " "
                        + "ORDER BY cii.ItemNumber ";
                rsAvistaInvoiceListRows = stAvistaInvoiceListRows.executeQuery(sql);
                while (rsAvistaInvoiceListRows.next()) {
                    dbLineItem = etlCatalogs.getEtlItem(etlCatalogs.getEtlIdForItem(rsAvistaInvoiceListRows.getInt("PlantBoardTypeKey"), rsAvistaInvoiceListRows.getString("Flute"), rsAvistaInvoiceList.getString("CustomerId")));
                    if (dbLineItem == null) {
                        throw new Exception(SEtlConsts.MSG_ERR_UNK_ITM + "\n"
                                + SEtlConsts.TXT_BRD + "='" + rsAvistaInvoiceListRows.getInt("PlantBoardTypeKey") + "', "
                                + SEtlConsts.TXT_FLT + "='" + rsAvistaInvoiceListRows.getString("Flute") + "'."
                                + sInvoiceErrMsg);
                    }
                    
                    if (dbLineItem.getFkSrcRequiredCurrencyId_n() != 0) { // does current item have required currency?
                        if (idInvoiceCurrencyReq == 0) {
                            idInvoiceCurrencyReq = dbLineItem.getFkSrcRequiredCurrencyId_n();
                        }
                        else if (idInvoiceCurrencyReq != dbLineItem.getFkSrcRequiredCurrencyId_n()) {
                            throw new Exception(SEtlConsts.MSG_ERR_UNK_CUR_MLT_ETL
                                    + sInvoiceErrMsg);
                        }
                    }
                    
                    if (rsAvistaInvoiceListRows.getInt("CurrencyKey") != 0) {
                        setLineCurrencySrcIds.add(etlCatalogs.getEtlIdForCurrency(rsAvistaInvoiceListRows.getInt("CurrencyKey")));
                    }
                    
                    dInvoiceAmountSrc += rsAvistaInvoiceListRows.getDouble("LineAmount");
                }
                
                if (setLineCurrencySrcIds.size() > 1) {
                    throw new Exception(SEtlConsts.MSG_ERR_UNK_CUR_MLT_SRC
                            + sInvoiceErrMsg);
                }
                
                // Set invoice's original currency:
                
                idInvoiceCurrencySrc = 
                        !setLineCurrencySrcIds.isEmpty() ? (Integer) setLineCurrencySrcIds.toArray()[0] : 
                        dbConfigAvista.getFkSrcDefaultCurrencyId(); // set invoice lines' currency if any, otherwise system's default currency
                
                dbInvoiceCurrencySrc = etlCatalogs.getEtlCurrency(idInvoiceCurrencySrc);
                if (dbInvoiceCurrencySrc == null) {
                    throw new Exception(SEtlConsts.MSG_ERR_UNK_CUR + "\n"
                            + SEtlConsts.TXT_MISC_ID + "='" + idInvoiceCurrencySrc + "' (" + SEtlConsts.TXT_CUR + " " + SEtlConsts.TXT_MISC_SRC + ")."
                            + sInvoiceErrMsg);
                }
                
                // Set invoice's required currency:
                
                if (idInvoiceCurrencyReq == 0) { // is invoice required currency not already set?
                    idInvoiceCurrencyReq = 
                            dbInvoiceCustomer.getFkSrcRequiredCurrencyId_n() != 0 ? dbInvoiceCustomer.getFkSrcRequiredCurrencyId_n() : 
                            idInvoiceCurrencySrc; // set customer's required currency if any, otherwise invoice's original currency
                }
                
                dbInvoiceCurrencyReq = etlCatalogs.getEtlCurrency(idInvoiceCurrencyReq);
                if (dbInvoiceCurrencyReq == null) {
                    throw new Exception(SEtlConsts.MSG_ERR_UNK_CUR + "\n"
                            + SEtlConsts.TXT_MISC_ID + "='" + idInvoiceCurrencyReq + "' (" + SEtlConsts.TXT_CUR + " " + SEtlConsts.TXT_MISC_REQ + ")."
                            + sInvoiceErrMsg);
                }
                
                // Validate exchange rate if necessary:
                
                if (idInvoiceCurrencyReq == dbConfigAvista.getFkSrcLocalCurrencyId()) {
                    dInvoiceExchangeRate = 1;
                }
                else {
                    dInvoiceExchangeRate = SEtlUtils.getEtlExchangeRate(session, idInvoiceCurrencyReq, etlPackage.DateIssue);
                    if (dInvoiceExchangeRate == 0) {
                        throw new Exception(SEtlConsts.MSG_ERR_UNK_EXR + "\n"
                                + SEtlConsts.TXT_CUR + "='" + dbInvoiceCurrencyReq.getName() + "', "
                                + SEtlConsts.TXT_MISC_DAT + "='" + SLibUtils.DateFormatDate.format(etlPackage.DateIssue) + "'."
                                + sInvoiceErrMsg);
                    }
                }
                
                // Compute factor for currency:
                
                if (idInvoiceCurrencySrc == idInvoiceCurrencyReq) {
                    dFactorCurrency = 1;
                }
                else if (idInvoiceCurrencySrc == dbConfigAvista.getFkSrcLocalCurrencyId()) {
                    dFactorCurrency = 1 / dInvoiceExchangeRate;
                }
                else {
                    dFactorCurrency = 1 * dInvoiceExchangeRate;
                }
                
                // Process ETL for current invoice:
                
                try {
                    sql = "SELECT ci.CustomerInvoiceKey, ci.InvoiceNumber, ci.BatchNumber, ci.Created AS InvoiceCreated, ci.CustomerId, ci.Description AS InvoiceDescription, ci.CurrentStatusKey, "
                            + "cii.ItemNumber, cii.LineAmount, cii.UnitPrice AS LinePrice, cii.PricePer AS LinePricePerCode, cii.Units, cii.Pieces, cii.Area, cii.Weight, cii.ProductDescription, cii.CurrencyKey, cii.ExchangeToLocal, "
                            + "co.OrderNumber, COALESCE(co.CustomerPO, '') as CustomerPO, co.QuantityOrdered, co.ItemDescription AS OrderItemDescription, co.Price AS OrderPrice, co.PricePerCode AS OrderPricePerCode, "
                            + "pe.EstNo, pe.ItemDescription AS EstItemDescription, pe.Width, pe.Length, pe.Flute, pe.Price AS EstPrice, pe.PricePerCode AS EstPricePerCode, pe.PlantBoardTypeKey, pbt.ShortDesc, "
                            + "COALESCE(ci.PayTermCode, '') AS PayTermCode, COALESCE(pt.Description, 'Contado') AS PayTermDescription "
                            + "FROM dbo.CustomerInvoices AS ci "
                            + "INNER JOIN dbo.CustomerInvoiceItems AS cii ON cii.CustomerInvoiceKey=ci.CustomerInvoiceKey "
                            + "INNER JOIN dbo.CustomerOrder AS co ON co.OrderKey=cii.OrderKey "
                            + "INNER JOIN dbo.PlantEst AS pe ON pe.EstKey=co.EstKey "
                            + "INNER JOIN dbo.PlantBoardType AS pbt ON pbt.PlantBoardTypeKey=pe.PlantBoardTypeKey "
                            + "LEFT OUTER JOIN dbo.PayTerm AS pt ON pt.PayTermCode=ci.PayTermCode "
                            + "WHERE ci.CustomerInvoiceKey=" + rsAvistaInvoiceList.getInt("CustomerInvoiceKey") + " "
                            + "ORDER BY cii.ItemNumber ";
                    rsAvistaInvoiceData = stAvistaInvoiceData.executeQuery(sql);
                    if (rsAvistaInvoiceData.next()) {
                        stEtl.execute("START TRANSACTION");
                        
                        dbInvoice = new SDbInvoice();
                        
                        //invoice.setPkInvoiceId(...);  // set when saved
                        dbInvoice.setSrcInvoiceId(rsAvistaInvoiceData.getInt("CustomerInvoiceKey"));
                        dbInvoice.setDesInvoiceYearId(0);       // set when SIIE registry saved
                        dbInvoice.setDesInvoiceDocumentId(0);   // set when SIIE registry saved
                        dbInvoice.setOriginalNumber(rsAvistaInvoiceData.getString("InvoiceNumber"));
                        dbInvoice.setFinalSeries("");   // set when SIIE registry saved
                        dbInvoice.setFinalNumber("");   // set when SIIE registry saved
                        dbInvoice.setOriginalDate(rsAvistaInvoiceData.getDate("InvoiceCreated"));
                        dbInvoice.setFinalDate(etlPackage.DateIssue);
                        dbInvoice.setPayAccount(sInvoicePayAccountDes);
                        //dbInvoice.setCreditDays(SLibUtils.parseInt(rsAvistaInvoiceData.getString("PayTermCode")));        // formerlly, until may 2017, credit days taken from Avista's customer invoice
                        dbInvoice.setCreditDays(dataBizPartnerCustomer.getDbmsCategorySettingsCus().getDaysOfCredit());     // since may 2017, credit days taken from SIIE's business partner customer settings
                        dbInvoice.setOriginalAmount(dInvoiceAmountSrc);
                        //dbInvoice.setFinalAmount(...);    // set later on this method
                        dbInvoice.setExchangeRate(dInvoiceExchangeRate);
                        dbInvoice.setBatch(rsAvistaInvoiceData.getInt("BatchNumber"));
                        dbInvoice.setPaymentConditions(SLibUtils.textToSql(rsAvistaInvoiceData.getString("PayTermDescription").toUpperCase()));
                        dbInvoice.setCustomerOrder(SLibUtils.textToSql(rsAvistaInvoiceData.getString("CustomerPO").toUpperCase()));
                        dbInvoice.setBillOfLading(SLibUtils.textToSql(rsAvistaInvoiceData.getString("InvoiceDescription")));
                        dbInvoice.setSrcCustomerFk(dbInvoiceCustomer.getSrcCustomerId());
                        dbInvoice.setDesCustomerFk(dbInvoiceCustomer.getDesCustomerId());
                        dbInvoice.setSrcSalesAgentFk(dbInvoiceSalesAgent == null ? SLibConsts.UNDEFINED : dbInvoiceSalesAgent.getSrcSalesAgentId());
                        dbInvoice.setDesSalesAgentFk(idInvoiceSalesAgentDes);
                        dbInvoice.setAuxDesSalesSupervisorFk(idInvoiceSalesSupervisorDes);  // out of real place as class method, but in logical place
                        dbInvoice.setSrcOriginalCurrencyFk(dbInvoiceCurrencySrc.getSrcCurrencyId());
                        dbInvoice.setSrcFinalCurrencyFk(dbInvoiceCurrencyReq.getSrcCurrencyId());
                        dbInvoice.setDesOriginalCurrencyFk(dbInvoiceCurrencySrc.getDesCurrencyId());
                        dbInvoice.setDesFinalCurrencyFk(dbInvoiceCurrencyReq.getDesCurrencyId());
                        dbInvoice.setDesPayMethodFk(idInvoicePayMethodDes);    // SIIE & SIIE-ETL primary keys of both catalogs are the same
                        dbInvoice.setDesCfdiZipIssue(dbConfigAvista.getDesCfdiZipIssue());
                        dbInvoice.setDesCfdiTaxRegime(dbConfigAvista.getDesCfdiTaxRegime());
                        dbInvoice.setDesCfdiPaymentWay(dbInvoice.getCreditDays() == 0 ? SDataConstantsSys.TRNS_CFD_CAT_PAY_WAY_99 : dataBizPartnerCustomer.getDbmsCategorySettingsCus().getCfdiPaymentWay());
                        dbInvoice.setDesCfdiCfdiUsage(!dataBizPartnerCustomer.getDbmsCategorySettingsCus().getCfdiCfdiUsage().isEmpty() ? dataBizPartnerCustomer.getDbmsCategorySettingsCus().getCfdiCfdiUsage() : dbConfigAvista.getDesCfdiCfdiUsage());
                        //dbInvoice.setFirstEtlInsert(...); // set when saved
                        //dbInvoice.setLastEtlUpdate(...);  // set when saved
                        dbInvoice.setDeleted(false);
                        dbInvoice.setSystem(false);
                        dbInvoice.setFkSrcCustomerId(dbInvoiceCustomer.getPkCustomerId());
                        dbInvoice.setFkSrcSalesAgentId_n(dbInvoiceSalesAgent == null ? SLibConsts.UNDEFINED : dbInvoiceSalesAgent.getPkSalesAgentId());
                        dbInvoice.setFkSrcOriginalCurrencyId(dbInvoiceCurrencySrc.getPkCurrencyId());
                        dbInvoice.setFkSrcFinalCurrencyId(dbInvoiceCurrencyReq.getPkCurrencyId());
                        dbInvoice.setFkDesPayMethodId(idInvoicePayMethodDes);  // SIIE & SIIE-ETL primary keys of both catalogs are the same
                        dbInvoice.setFkLastEtlLogId(etlPackage.EtlLog.getPkEtlLogId());
                        //invoice.setFkUserInsertId(...);
                        //invoice.setFkUserUpdateId(...);
                        //invoice.setTsUserInsert(...);
                        //invoice.setTsUserUpdate(...);
                        
                        do {
                            sInvoiceItemErrMsg = sInvoiceErrMsg + "; ItemNumber=" + rsAvistaInvoiceData.getInt("ItemNumber");
                            
                            // Define item:
                            
                            dbLineItem = etlCatalogs.getEtlItem(etlCatalogs.getEtlIdForItem(rsAvistaInvoiceData.getInt("PlantBoardTypeKey"), rsAvistaInvoiceData.getString("Flute"), rsAvistaInvoiceData.getString("CustomerId")));
                            if (dbLineItem == null) {
                                throw new Exception(SEtlConsts.MSG_ERR_UNK_ITM + "\n"
                                        + SEtlConsts.TXT_BRD + "='" + rsAvistaInvoiceData.getInt("PlantBoardTypeKey") + "', "
                                        + SEtlConsts.TXT_FLT + "='" + rsAvistaInvoiceData.getString("Flute") + "'."
                                        + sInvoiceItemErrMsg);
                            }
                            
                            dLineUnits = rsAvistaInvoiceData.getDouble("Units"); // units
                            dLinePieces = rsAvistaInvoiceData.getDouble("Pieces"); // pce
                            dLineArea = rsAvistaInvoiceData.getDouble("Length") * rsAvistaInvoiceData.getDouble("Width") * rsAvistaInvoiceData.getDouble("Pieces") / Math.pow(10, 9); // mm² to 1k·m²
                            dLineWeight = rsAvistaInvoiceData.getDouble("Weight") / Math.pow(10, 6); // mg to kg
                            
                            // Define original (source) unit of measure:
                            
                            sLinePricePerCode = rsAvistaInvoiceData.getString("LinePricePerCode");
                            dbLineUnitOfMeasureSrc = etlCatalogs.getEtlUnitOfMeasure(etlCatalogs.getEtlIdForUnitOfMeasure(sLinePricePerCode));
                            
                            // check source unit of measure for current row:
                            if (dbLineUnitOfMeasureSrc == null) {
                                throw new Exception(SEtlConsts.MSG_ERR_UNK_UOM + "\n"
                                        + SEtlConsts.TXT_MISC_ID + "='" + rsAvistaInvoiceData.getString("LinePricePerCode") + "' (" + SEtlConsts.TXT_UOM + " " + SEtlConsts.TXT_MISC_SRC + ")."
                                        + sInvoiceItemErrMsg);
                            }
                            
                            // Define required unit of measure:
                            
                            if (sLinePricePerCode.compareTo(SEtlConsts.AVISTA_UOM_FF) == 0) {
                                dbLineUnitOfMeasureReq = dbLineUnitOfMeasureSrc;    // same unit when 'FF'
                            }
                            else if (dbLineItem.getFkSrcRequiredUnitOfMeasureId_n() != SLibConsts.UNDEFINED) {
                                dbLineUnitOfMeasureReq = etlCatalogs.getEtlUnitOfMeasure(dbLineItem.getFkSrcRequiredUnitOfMeasureId_n());   // required unit at item's level
                            }
                            else if (dbInvoiceCustomer.getFkSrcRequiredUnitOfMeasureId_n() != SLibConsts.UNDEFINED) {
                                dbLineUnitOfMeasureReq = etlCatalogs.getEtlUnitOfMeasure(dbInvoiceCustomer.getFkSrcRequiredUnitOfMeasureId_n());    // required unit at customer's level
                            }
                            else {
                                dbLineUnitOfMeasureReq = dbLineUnitOfMeasureSrc;    // same unit because there is not any spacial need to fit
                            }
                            
                            // check required unit of measure for current row (does not really needed, just for consistence):
                            if (dbLineUnitOfMeasureReq == null) {
                                throw new Exception(SEtlConsts.MSG_ERR_UNK_UOM + "\n"
                                        + SEtlConsts.TXT_MISC_ID + "='" + dbLineItem.getSrcRequiredUnitOfMeasureFk_n() + "' (" + SEtlConsts.TXT_UOM + " " + SEtlConsts.TXT_MISC_REQ + ")."
                                        + sInvoiceItemErrMsg);
                            }
                            
                            // Compute final price and amount:
                            
                            dLineUnitsSrc = 0;
                            dLineUnitsReq = 0;
                            
                            switch (dbLineUnitOfMeasureSrc.getSrcUnitOfMeasureId()) {
                                case SEtlConsts.AVISTA_UOM_MSM: // millar square meter
                                case SEtlConsts.AVISTA_UOM_MSF: // millar square feet
                                    dLineUnitsSrc = dLineArea;
                                    break;
                                case SEtlConsts.AVISTA_UOM_PC: // piece
                                    dLineUnitsSrc = dLinePieces;
                                    break;
                                case SEtlConsts.AVISTA_UOM_TON: // ton
                                    dLineUnitsSrc = dLineWeight;
                                    break;
                                case SEtlConsts.AVISTA_UOM_FF: // some kind of 'fee'
                                    dLineUnitsSrc = dLineUnits;
                                    System.out.println("Unidad de medida atípica: '" + dbLineUnitOfMeasureSrc.getSrcUnitOfMeasureId() + "'." + sInvoiceItemErrMsg);
                                    break;
                                default:
                                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n"
                                            + SEtlConsts.TXT_MISC_QTY + " " + SEtlConsts.TXT_MISC_SRC.toLowerCase() + ": '" + dbLineUnitOfMeasureSrc.getSrcUnitOfMeasureId() + "'."
                                            + sInvoiceItemErrMsg);
                            }
                            
                            if (dbLineUnitOfMeasureSrc.getPkUnitOfMeasureId() == dbLineUnitOfMeasureReq.getPkUnitOfMeasureId()) {
                                isLineUnitEqual = true;
                                dLineUnitsReq = dLineUnitsSrc;
                            }
                            else {
                                isLineUnitEqual = false;
                                
                                switch (dbLineUnitOfMeasureReq.getSrcUnitOfMeasureId()) {
                                    case SEtlConsts.AVISTA_UOM_MSM: // millar square meter
                                    case SEtlConsts.AVISTA_UOM_MSF: // millar square feet
                                    case SEtlConsts.AVISTA_UOM_PC: // piece
                                    case SEtlConsts.AVISTA_UOM_TON: // ton
                                    case SEtlConsts.AVISTA_UOM_FF: // some kind of 'fee'
                                        break;
                                    default:
                                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n"
                                                + SEtlConsts.TXT_MISC_QTY + " " + SEtlConsts.TXT_MISC_REQ.toLowerCase() + ": '" + dbLineUnitOfMeasureReq.getSrcUnitOfMeasureId() + "'."
                                                + sInvoiceItemErrMsg);
                                }
                                
                                switch (dbLineUnitOfMeasureSrc.getSrcUnitOfMeasureId()) {
                                    case SEtlConsts.AVISTA_UOM_MSM:
                                        switch (dbLineUnitOfMeasureReq.getSrcUnitOfMeasureId()) {
                                            case SEtlConsts.AVISTA_UOM_MSF:
                                                dLineUnitsReq = dLineArea * 1000.0 / dMisc1kFeetTo1kMeters;
                                                break;
                                            case SEtlConsts.AVISTA_UOM_PC:
                                                dLineUnitsReq = dLinePieces;
                                                break;
                                            case SEtlConsts.AVISTA_UOM_TON:
                                                dLineUnitsReq = dLineWeight;
                                                break;
                                            default:
                                        }
                                        break;
                                        
                                    case SEtlConsts.AVISTA_UOM_MSF:
                                        switch (dbLineUnitOfMeasureReq.getSrcUnitOfMeasureId()) {
                                            case SEtlConsts.AVISTA_UOM_MSF:
                                                dLineUnitsReq = dLineArea * dMisc1kFeetTo1kMeters / 1000.0;
                                                break;
                                            case SEtlConsts.AVISTA_UOM_PC:
                                                dLineUnitsReq = dLinePieces;
                                                break;
                                            case SEtlConsts.AVISTA_UOM_TON:
                                                dLineUnitsReq = dLineWeight;
                                                break;
                                            default:
                                        }
                                        break;
                                        
                                    case SEtlConsts.AVISTA_UOM_PC:
                                        switch (dbLineUnitOfMeasureReq.getSrcUnitOfMeasureId()) {
                                            case SEtlConsts.AVISTA_UOM_MSM:
                                                dLineUnitsReq = dLineArea;
                                                break;
                                            case SEtlConsts.AVISTA_UOM_MSF:
                                                dLineUnitsReq = dLineArea * 1000.0 / dMisc1kFeetTo1kMeters;
                                                break;
                                            case SEtlConsts.AVISTA_UOM_TON:
                                                dLineUnitsReq = dLineWeight;
                                                break;
                                            default:
                                        }
                                        break;
                                    case SEtlConsts.AVISTA_UOM_TON:
                                        switch (dbLineUnitOfMeasureReq.getSrcUnitOfMeasureId()) {
                                            case SEtlConsts.AVISTA_UOM_MSM:
                                                dLineUnitsReq = dLineArea;
                                                break;
                                            case SEtlConsts.AVISTA_UOM_MSF:
                                                dLineUnitsReq = dLineArea * 1000.0 / dMisc1kFeetTo1kMeters;
                                                break;
                                            case SEtlConsts.AVISTA_UOM_PC:
                                                dLineUnitsReq = dLinePieces;
                                                break;
                                            default:
                                        }
                                        break;
                                        
                                    case SEtlConsts.AVISTA_UOM_FF:
                                        switch (dbLineUnitOfMeasureReq.getSrcUnitOfMeasureId()) {
                                            case SEtlConsts.AVISTA_UOM_MSM:
                                            case SEtlConsts.AVISTA_UOM_MSF:
                                            case SEtlConsts.AVISTA_UOM_PC:
                                            case SEtlConsts.AVISTA_UOM_TON:
                                                dLineUnitsReq = dLineUnitsSrc;
                                                break;
                                            default:
                                        }
                                        break;
                                        
                                    default:
                                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n"
                                                + SEtlConsts.TXT_MISC_QTY + " " + SEtlConsts.TXT_MISC_REQ.toLowerCase() + "."
                                                + sInvoiceItemErrMsg);
                                }
                            }
                            
                            if (dLineUnitsReq == 0) {
                                if (dLineUnits == 0 && dLinePieces == 0 && dLineArea == 0 && dLineWeight == 0) {
                                    // skip current row!, this is an atypical row without units!
                                    System.out.println(sInvoiceItemErrMsg);
                                    continue;
                                }
                                else {
                                    throw new Exception(sInvoiceItemErrMsg);
                                }
                            }
                            
                            if (isLineUnitEqual) {
                                // Both line (invoice row) units, source and required, are the same:
                                
                                dLinePriceReq = SLibUtils.round(rsAvistaInvoiceData.getDouble("LinePrice") * dFactorCurrency, nMiscDecsAmountUnit);
                                dLineAmountReq = SLibUtils.round(dLineUnitsReq * dLinePriceReq, nMiscDecsAmount);
                            }
                            else {
                                // Source and required line (invoice row) units are different:
                                
                                dLineAmountReq = SLibUtils.round(SLibUtils.round(dLineUnitsSrc * rsAvistaInvoiceData.getDouble("LinePrice"), nMiscDecsAmountUnit) * dFactorCurrency, nMiscDecsAmount);
                                dLinePriceReq = SLibUtils.round(dLineAmountReq / dLineUnitsReq, nMiscDecsAmountUnit);
                            }
                            
                            dbInvoiceRow = new SDbInvoiceRow();
                            
                            //dbInvoiceRow.setPkInvoiceId(...); // set when saved
                            //dbInvoiceRow.setPkRowId(...);     // set when saved
                            dbInvoiceRow.setSrcInvoiceId(rsAvistaInvoiceData.getInt("CustomerInvoiceKey"));
                            dbInvoiceRow.setSrcInvoiceRowId(rsAvistaInvoiceData.getInt("ItemNumber"));
                            dbInvoiceRow.setCode(dbLineItem.getCode());
                            dbInvoiceRow.setName(dbLineItem.getName());
                            dbInvoiceRow.setProductDescription(SLibUtils.textToSql(rsAvistaInvoiceData.getString("ProductDescription")));
                            dbInvoiceRow.setCustomerOrder(SLibUtils.textToSql(rsAvistaInvoiceData.getString("CustomerPO").toUpperCase()));
                            dbInvoiceRow.setOrderNumber(rsAvistaInvoiceData.getString("OrderNumber"));
                            dbInvoiceRow.setOrderDescription(SLibUtils.textToSql(rsAvistaInvoiceData.getString("OrderItemDescription")));
                            dbInvoiceRow.setEstimateNumber(rsAvistaInvoiceData.getString("EstNo"));
                            dbInvoiceRow.setEstimateDescription(SLibUtils.textToSql(rsAvistaInvoiceData.getString("EstItemDescription")));
                            dbInvoiceRow.setQuantityOrdered(rsAvistaInvoiceData.getInt("QuantityOrdered"));
                            dbInvoiceRow.setQuantity(rsAvistaInvoiceData.getInt("Pieces"));
                            dbInvoiceRow.setLength(rsAvistaInvoiceData.getDouble("Length"));    // mm
                            dbInvoiceRow.setWidth(rsAvistaInvoiceData.getDouble("Width"));      // mm
                            dbInvoiceRow.setArea(rsAvistaInvoiceData.getDouble("Area"));        // mm²
                            dbInvoiceRow.setWeight(rsAvistaInvoiceData.getDouble("Weight"));    // mg
                            
                            dbInvoiceRow.setOriginalPrice(rsAvistaInvoiceData.getDouble("LinePrice"));
                            dbInvoiceRow.setOriginalPricePerUnit(rsAvistaInvoiceData.getString("LinePricePerCode"));
                            dbInvoiceRow.setOriginalUnits(rsAvistaInvoiceData.getDouble("Units"));
                            dbInvoiceRow.setOriginalAmount(rsAvistaInvoiceData.getDouble("LineAmount"));
                            
                            dbInvoiceRow.setFinalPrice(dLinePriceReq);
                            dbInvoiceRow.setFinalPricePerUnit(dbLineUnitOfMeasureReq.getSrcUnitOfMeasureId());
                            dbInvoiceRow.setFinalUnits(dLineUnitsReq);
                            dbInvoiceRow.setFinalAmount(dLineAmountReq);
                            
                            dbInvoiceRow.setSrcBoardType(SLibUtils.textToSql(rsAvistaInvoiceData.getString("ShortDesc")));
                            dbInvoiceRow.setSrcBoardTypeFk(rsAvistaInvoiceData.getInt("PlantBoardTypeKey"));
                            dbInvoiceRow.setSrcFluteFk(rsAvistaInvoiceData.getString("Flute"));
                            dbInvoiceRow.setDesItemFk(dbLineItem.getDesItemId());
                            
                            dbInvoiceRow.setSrcOriginalUnitOfMeasureFk(dbLineUnitOfMeasureSrc.getSrcUnitOfMeasureId());
                            dbInvoiceRow.setSrcFinalUnitOfMeasureFk(dbLineUnitOfMeasureReq.getSrcUnitOfMeasureId());
                            
                            dbInvoiceRow.setDesOriginalUnitOfMeasureFk(dbLineUnitOfMeasureSrc.getDesUnitOfMeasureId());
                            dbInvoiceRow.setDesFinalUnitOfMeasureFk(dbLineUnitOfMeasureReq.getDesUnitOfMeasureId());
                            
                            dbInvoiceRow.setDeleted(false);
                            dbInvoiceRow.setSystem(false);
                            dbInvoiceRow.setFkItemId(dbLineItem.getPkItemId());
                            dbInvoiceRow.setFkSrcOriginalUnitOfMeasureId(dbLineUnitOfMeasureSrc.getPkUnitOfMeasureId());
                            dbInvoiceRow.setFkSrcFinalUnitOfMeasureId(dbLineUnitOfMeasureReq.getPkUnitOfMeasureId());
                            //dbInvoiceRow.setFkUserInsertId(...);
                            //dbInvoiceRow.setFkUserUpdateId(...);
                            //dbInvoiceRow.setTsUserInsert(...);
                            //dbInvoiceRow.setTsUserUpdate(...);
                            
                            dbInvoice.getChildRows().add(dbInvoiceRow);
                            dInvoiceAmountReq += dLineAmountReq;
                        } while (rsAvistaInvoiceData.next());
                        
                        dbInvoice.save(session);

                        stEtl.execute("COMMIT");
                    }
                }
                catch (Exception e) {
                    stEtl.execute("ROLLBACK");
                    throw e;
                }
                
                try {
                    // DPS is new on SIIE:

                    stSiie.execute("START TRANSACTION");

                    // Create Document for Purchases & Sales registry:

                    dataDps = new SDataDps();
                    
                    dataDps.setPkYearId(SLibTimeUtils.digestYear(dbInvoice.getFinalDate())[0]);
                    //dataDps.setPkDocId(...); // set when saved
                    dataDps.setDate(dbInvoice.getFinalDate());
                    dataDps.setDateDoc(dataDps.getDate());
                    dataDps.setDateStartCredit(dataDps.getDate());
                    dataDps.setDateShipment_n(null);
                    dataDps.setDateDelivery_n(null);
                    dataDps.setDateDocLapsing_n(null);
                    dataDps.setDateDocDelivery_n(null);
                    dataDps.setNumberSeries(dbConfigAvista.getInvoiceSeries());
                    dataDps.setNumber("" + SEtlUtils.getSiieNextDpsNumber(session, stSiie));
                    dataDps.setNumberReference(dbInvoice.getCustomerOrder().length() <= 25 ? dbInvoice.getCustomerOrder() : dbInvoice.getCustomerOrder().substring(0, 25));
                    dataDps.setCommissionsReference("");
                    dataDps.setApprovalYear(dataDps.getPkYearId());
                    dataDps.setApprovalNumber(1);
                    dataDps.setDaysOfCredit(dbInvoice.getCreditDays());
                    dataDps.setIsDiscountDocApplying(false);
                    dataDps.setIsDiscountDocPercentage(false);
                    dataDps.setDiscountDocPercentage(0);
                    //dataDps.setSubtotalProvisional_r(0);  // set later when DPS calculated
                    //dataDps.setDiscountDoc_r(0);  // set later when DPS calculated
                    //dataDps.setSubtotal_r(0);     // set later when DPS calculated
                    //dataDps.setTaxCharged_r(0);   // set later when DPS calculated
                    //dataDps.setTaxRetained_r(0);  // set later when DPS calculated
                    //dataDps.setTotal_r(0);        // set later when DPS calculated
                    //dataDps.setCommissions_r(0);  // set later when DPS calculated
                    dataDps.setExchangeRate(dbInvoice.getExchangeRate());
                    dataDps.setExchangeRateSystem(dbInvoice.getExchangeRate());
                    //dataDps.setSubtotalProvisionalCy_r(0); // set later when DPS calculated
                    //dataDps.setDiscountDocCy_r(0);    // set later when DPS calculated
                    //dataDps.setSubtotalCy_r(0);       // set later when DPS calculated
                    //dataDps.setTaxChargedCy_r(0);     // set later when DPS calculated
                    //dataDps.setTaxRetainedCy_r(0);    // set later when DPS calculated
                    //dataDps.setTotalCy_r(0);          // set later when DPS calculated
                    //dataDps.setCommissionsCy_r(0);    // set later when DPS calculated
                    dataDps.setDriver("");
                    dataDps.setPlate("");
                    dataDps.setTicket("");
                    dataDps.setShipments(0);
                    dataDps.setPayments(0);
                    //dataDps.setPaymentMethod((String) session.readField(SModConsts.AS_PAY_MET, new int[] { dbInvoice.getFkDesPayMethodId() }, SDbRegistry.FIELD_NAME));   // deprecated since CFDI 3.3
                    dataDps.setPaymentMethod("");
                    //dataDps.setPaymentAccount(dbInvoice.getPayAccount()); // deprecated since CFDI 3.3
                    dataDps.setPaymentAccount("");
                    dataDps.setAutomaticAuthorizationRejection(0);  // N/A
                    dataDps.setIsPublic(false);
                    dataDps.setIsLinked(false);
                    dataDps.setIsClosed(false);
                    dataDps.setIsClosedCommissions(false);
                    dataDps.setIsShipped(false);
                    dataDps.setIsDpsDeliveryAck(false);
                    dataDps.setIsRebill(false);
                    dataDps.setIsAudited(false);
                    dataDps.setIsAuthorized(false);
                    dataDps.setIsRecordAutomatic(true);
                    dataDps.setIsCopy(false);
                    dataDps.setIsCopied(false);
                    dataDps.setIsSystem(false);
                    dataDps.setIsDeleted(false);
                    dataDps.setFkDpsCategoryId(erp.mod.SModSysConsts.TRNU_TP_DPS_SAL_INV[0]);
                    dataDps.setFkDpsClassId(erp.mod.SModSysConsts.TRNU_TP_DPS_SAL_INV[1]);
                    dataDps.setFkDpsTypeId(erp.mod.SModSysConsts.TRNU_TP_DPS_SAL_INV[2]);
                    dataDps.setFkPaymentTypeId(dataDps.getDaysOfCredit() == 0 ? SDataConstantsSys.TRNS_TP_PAY_CASH : SDataConstantsSys.TRNS_TP_PAY_CREDIT);
                    //dataDps.setFkPaymentSystemTypeId(dbInvoice.getFkDesPayMethodId());    // deprecated since CFDI 3.3
                    dataDps.setFkPaymentSystemTypeId(SDataConstantsSys.TRNU_TP_PAY_SYS_NA);
                    dataDps.setFkDpsStatusId(SDataConstantsSys.TRNS_ST_DPS_EMITED);
                    dataDps.setFkDpsValidityStatusId(SDataConstantsSys.TRNS_ST_DPS_VAL_EFF);
                    dataDps.setFkDpsAuthorizationStatusId(SDataConstantsSys.TRNS_ST_DPS_AUTHORN_NA);
                    dataDps.setFkDpsAnnulationTypeId(erp.mod.SModSysConsts.TRNU_TP_DPS_ANN_NA);
                    dataDps.setFkDpsNatureId(SDataConstantsSys.TRNU_DPS_NAT_DEF);
                    dataDps.setFkCompanyBranchId(dbConfigAvista.getDesCompanyBranchFk());
                    dataDps.setFkFunctionalAreaId(erp.mod.SModSysConsts.CFGU_FUNC_NON);
                    dataDps.setFkBizPartnerId_r(dbInvoiceCustomer.getDesCustomerId());
                    dataDps.setFkBizPartnerBranchId(dbInvoiceCustomer.getDesCustomerBranchId());
                    dataDps.setFkBizPartnerBranchAddressId(SEtlConsts.SIIE_DEFAULT);
                    dataDps.setFkBizPartnerAltId_r(dbInvoiceCustomer.getDesCustomerId());
                    dataDps.setFkBizPartnerBranchAltId(dbInvoiceCustomer.getDesCustomerBranchId());
                    dataDps.setFkBizPartnerBranchAddressAltId(SEtlConsts.SIIE_DEFAULT);
                    dataDps.setFkBizPartnerAddresseeId_n(SLibConsts.UNDEFINED);
                    dataDps.setFkAddresseeBizPartnerId_nr(SLibConsts.UNDEFINED);
                    dataDps.setFkAddresseeBizPartnerBranchId_n(SLibConsts.UNDEFINED);
                    dataDps.setFkAddresseeBizPartnerBranchAddressId_n(SLibConsts.UNDEFINED);
                    dataDps.setFkContactBizPartnerBranchId_n(SLibConsts.UNDEFINED);
                    dataDps.setFkContactContactId_n(SLibConsts.UNDEFINED);
                    dataDps.setFkTaxIdentityEmisorTypeId(dataBizPartnerCompany.getFkTaxIdentityId());
                    dataDps.setFkTaxIdentityReceptorTypeId(dataBizPartnerCustomer.getFkTaxIdentityId());
                    dataDps.setFkLanguajeId(SEtlConsts.SIIE_DEFAULT);
                    dataDps.setFkCurrencyId(dbInvoice.getDesFinalCurrencyFk());
                    dataDps.setFkSalesAgentId_n(dbInvoice.getDesSalesAgentFk());
                    dataDps.setFkSalesAgentBizPartnerId_n(dbInvoice.getDesSalesAgentFk());
                    dataDps.setFkSalesSupervisorId_n(dbInvoice.getAuxDesSalesSupervisorFk());
                    dataDps.setFkSalesSupervisorBizPartnerId_n(dbInvoice.getAuxDesSalesSupervisorFk());
                    dataDps.setFkIncotermId(erp.mod.SModSysConsts.LOGS_INC_NA);
                    dataDps.setFkSpotSourceId_n(SLibConsts.UNDEFINED);
                    dataDps.setFkSpotDestinyId_n(SLibConsts.UNDEFINED);
                    dataDps.setFkModeOfTransportationTypeId(erp.mod.SModSysConsts.LOGS_TP_MOT_NA);
                    dataDps.setFkCarrierTypeId(erp.mod.SModSysConsts.LOGS_TP_CAR_NA);
                    dataDps.setFkCarrierId_n(SLibConsts.UNDEFINED);
                    dataDps.setFkVehicleTypeId_n(SLibConsts.UNDEFINED);
                    dataDps.setFkVehicleId_n(SLibConsts.UNDEFINED);
                    dataDps.setFkBillOfLading_n(SLibConsts.UNDEFINED);
                    dataDps.setFkSourceYearId_n(SLibConsts.UNDEFINED);
                    dataDps.setFkSourceDocId_n(SLibConsts.UNDEFINED);
                    dataDps.setFkMfgYearId_n(SLibConsts.UNDEFINED);
                    dataDps.setFkMfgOrderId_n(SLibConsts.UNDEFINED);
                    dataDps.setFkUserLinkedId(SDataConstantsSys.USRX_USER_NA);
                    dataDps.setFkUserClosedId(SDataConstantsSys.USRX_USER_NA);
                    dataDps.setFkUserClosedCommissionsId(SDataConstantsSys.USRX_USER_NA);
                    dataDps.setFkUserShippedId(SDataConstantsSys.USRX_USER_NA);
                    dataDps.setFkUserDpsDeliveryAckId(SDataConstantsSys.USRX_USER_NA);
                    dataDps.setFkUserAuditedId(SDataConstantsSys.USRX_USER_NA);
                    dataDps.setFkUserAuthorizedId(SDataConstantsSys.USRX_USER_NA);
                    dataDps.setFkUserNewId(((SDbUser) session.getUser()).getDesUserId());
                    dataDps.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                    dataDps.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                    //dataDps.setUserLinkedTs(...);
                    //dataDps.setUserClosedTs(...);
                    //dataDps.setUserClosedCommissionsTs(...);
                    //dataDps.setUserShippedTs(...);
                    //dataDps.setUserDpsDeliveryAckTs(...);
                    //dataDps.setUserAuditedTs(...);
                    //dataDps.setUserAuthorizedTs(...);
                    //dataDps.setUserNewTs(...);
                    //dataDps.setUserEditTs(...);
                    //dataDps.setUserDeleteTs(...);
                    
                    dataDpsCfd = new SDataDpsCfd();
                    
                    //dataDpsCfd.setPkYearId(...);
                    //dataDpsCfd.setPkDocId(...);
                    dataDpsCfd.setVersion("" + DCfdConsts.CFDI_VER_33);
                    dataDpsCfd.setCfdiType(DCfdi33Catalogs.CFD_TP_I);
                    dataDpsCfd.setPaymentWay(dbInvoice.getDesCfdiPaymentWay());
                    dataDpsCfd.setPaymentMethod(dataDps.getFkPaymentTypeId() == SDataConstantsSys.TRNS_TP_PAY_CASH ? SDataConstantsSys.TRNS_CFD_CAT_PAY_MET_PUE : SDataConstantsSys.TRNS_CFD_CAT_PAY_MET_PPD);
                    dataDpsCfd.setPaymentConditions(dataDps.getFkPaymentTypeId() == SDataConstantsSys.TRNS_TP_PAY_CASH ? "CONTADO" : "CRÉDITO " + dataDps.getDaysOfCredit() + " DÍAS"); // XXX: implement method!
                    dataDpsCfd.setZipIssue(dbInvoice.getDesCfdiZipIssue());
                    //dataDpsCfd.setConfirmation(...);
                    dataDpsCfd.setTaxRegime(dbInvoice.getDesCfdiTaxRegime());
                    dataDpsCfd.setCfdiUsage(dbInvoice.getDesCfdiCfdiUsage());
                    
                    dataDps.setDbmsDataDpsCfd(dataDpsCfd);
                    
                    dataDpsNotes = new SDataDpsNotes();
                    
                    //dataDpsNotes.setPkYearId(...);    // set when saved
                    //dataDpsNotes.setPkDocId(...);     // set when saved
                    //dataDpsNotes.setPkNotesId(...);   // set when saved
                    dataDpsNotes.setNotes(dbInvoice.getBillOfLading());
                    //dataDpsNotes.setCfdComplementDisposition(...);
                    //dataDpsNotes.setCfdComplementRule(...);
                    dataDpsNotes.setIsAllDocs(true);
                    dataDpsNotes.setIsPrintable(true);
                    //dataDpsNotes.setIsCfdComplement(...);
                    dataDpsNotes.setIsDeleted(false);
                    dataDpsNotes.setFkUserNewId(((SDbUser) session.getUser()).getDesUserId());
                    dataDpsNotes.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                    dataDpsNotes.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                    //dataDpsNotes.setUserNewTs(...);
                    //dataDpsNotes.setUserEditTs(...);
                    //dataDpsNotes.setUserDeleteTs(...);
                    
                    dataDps.getDbmsDpsNotes().add(dataDpsNotes);
                    
                    if (dataDps.getFkCurrencyId() != erp.mod.SModSysConsts.CFGU_CUR_MXN) {
                        String cur = "";
                        SDataDpsNotes dataDpsNotesCur = new SDataDpsNotes();
                        
                        switch (dataDps.getFkCurrencyId()) {
                            case erp.mod.SModSysConsts.CFGU_CUR_USD:
                                cur = "DÓLARES ESTADOUNIDENSES";
                                break;
                            case erp.mod.SModSysConsts.CFGU_CUR_EUR:
                                cur = "EUROS";
                                break;
                            default:
                                cur = "MONEDA EXTRANJERA";
                        }
                        
                        String notes = "ESTA FACTURA SE PODRÁ LIQUIDAR EN " + cur + " O "
                                + "EN PESOS MEXICANOS AL TIPO DE CAMBIO QUE ESTÉ VIGENTE EL DÍA EN QUE SE LIQUIDE.";

                        //dataDpsNotes.setPkYearId(...);    // set when saved
                        //dataDpsNotes.setPkDocId(...);     // set when saved
                        //dataDpsNotes.setPkNotesId(...);   // set when saved
                        dataDpsNotesCur.setNotes(notes);
                        //dataDpsNotes.setCfdComplementDisposition(...);
                        //dataDpsNotes.setCfdComplementRule(...);
                        dataDpsNotesCur.setIsAllDocs(true);
                        dataDpsNotesCur.setIsPrintable(true);
                        //dataDpsNotes.setIsCfdComplement(...);
                        dataDpsNotesCur.setIsDeleted(false);
                        dataDpsNotesCur.setFkUserNewId(((SDbUser) session.getUser()).getDesUserId());
                        dataDpsNotesCur.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                        dataDpsNotesCur.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                        //dataDpsNotes.setUserNewTs(...);
                        //dataDpsNotes.setUserEditTs(...);
                        //dataDpsNotes.setUserDeleteTs(...);

                        dataDps.getDbmsDpsNotes().add(dataDpsNotesCur);
                    }
                    
                    for (SDbInvoiceRow row : dbInvoice.getChildRows()) {
                        // Obtain entry item and check default SIIE unit (MSM):
                        
                        dataItem = new SDataItem();
                        if (dataItem.read(new int[] { row.getDesItemFk() }, stSiie) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SEtlConsts.MSG_ERR_SIIE_ITM_QRY + "'" + row.getName() + "'."
                                                + sInvoiceErrMsg);
                        }
                        else if (dataItem.getFkUnitId() != nMiscDefaultSiieUnitId) {
                            throw new Exception(SEtlConsts.MSG_ERR_UNS_UOM + "\n(" + SEtlConsts.TXT_ITM + "='" + row.getName() + "')."
                                                + sInvoiceErrMsg);
                        }
                        
                        // Compute entry quantity:
                        
                        dEntryArea = SLibUtils.round(row.getLength() * row.getWidth() * row.getQuantity() / Math.pow(10, 6), nMiscDecsAmountUnit); // from mm² to m²
                        dEntryWeight = SLibUtils.round(row.getWeight() / Math.pow(10, 6), nMiscDecsAmountUnit);   // from mg to kg
                        
                        switch (dataItem.getFkUnitId()) {
                            case SEtlConsts.SIIE_UNIT_MSM:
                            case erp.mod.SModSysConsts.ITMU_UNIT_PCE:
                            case erp.mod.SModSysConsts.ITMU_UNIT_MT_TON:
                                dEntryQuantity = SLibUtils.round(dEntryArea / 1000.0, nMiscDecsAmountUnit);       // from m² to 1k·m²
                                break;
                            case SEtlConsts.SIIE_UNIT_MSF:
                                dEntryQuantity = SLibUtils.round((dEntryArea / 1000.0) * (1000.0 / dMisc1kFeetTo1kMeters), nMiscDecsAmountUnit);  // from m² to 1k·m², then to 1k·ft²
                                break;
                            case erp.mod.SModSysConsts.ITMU_UNIT_UNIT:
                                dEntryQuantity = row.getOriginalUnits();
                                break;
                            default:
                                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n" + SEtlConsts.MSG_ERR_UNS_UOM + "\n(" + SEtlConsts.TXT_ITM + "='" + row.getName() + "')."
                                                + sInvoiceErrMsg);
                        }
                        
                        dataDpsEntry = new SDataDpsEntry();
                        
                        //dataDpsEntry.setPkYearId(...);    // set when saved
                        //dataDpsEntry.setPkDocId(...);     // set when saved
                        //dataDpsEntry.setPkEntryId(...);   // set when saved
                        dataDpsEntry.setConceptKey(row.getCode());
                        dataDpsEntry.setConcept(SEtlConsts.TXT_MISC_O + ": " + row.getOrderNumber() + "; " + SEtlConsts.TXT_BRD + ": " + row.getName() + "; " + row.getLength() + " X " + row.getWidth() + "; " + SEtlConsts.TXT_MISC_PO_ACR + ": " + row.getCustomerOrder());
                        dataDpsEntry.setReference("");
                        
                        dataDpsEntry.setQuantity(dEntryQuantity);
                        
                        dataDpsEntry.setIsDiscountDocApplying(true);
                        dataDpsEntry.setIsDiscountUnitaryPercentage(false);
                        dataDpsEntry.setIsDiscountUnitaryPercentageSystem(false);
                        dataDpsEntry.setIsDiscountEntryPercentage(false);
                        dataDpsEntry.setDiscountUnitaryPercentage(0);
                        dataDpsEntry.setDiscountUnitaryPercentageSystem(0);
                        dataDpsEntry.setDiscountEntryPercentage(0);
                        
                        dEntryPriceUnitOrigCy = row.getFinalPrice();

                        dataDpsEntry.setOriginalQuantity(row.getFinalUnits());
                        dataDpsEntry.setOriginalPriceUnitaryCy(SLibUtils.round(dEntryPriceUnitOrigCy, SEtlConsts.SIIE_PRICE_UNIT_DECS));    // round price to fixed number of decimals
                        dataDpsEntry.setOriginalPriceUnitarySystemCy(SLibUtils.round(dEntryPriceUnitOrigCy, nMiscDecsAmountUnit));          // preserve price as is
                        dataDpsEntry.setOriginalDiscountUnitaryCy(0);
                        dataDpsEntry.setOriginalDiscountUnitarySystemCy(0);
                        dataDpsEntry.setSalesPriceUnitaryCy(0);
                        dataDpsEntry.setSalesFreightUnitaryCy(0);
                        
                        if (dataItem.getFkUnitId() == row.getDesFinalUnitOfMeasureFk()) {
                            dEntryPriceUnitCy = dEntryPriceUnitOrigCy;
                        }
                        else {
                            dEntryPriceUnitCy = SLibUtils.round((dataDpsEntry.getOriginalPriceUnitaryCy() - dataDpsEntry.getOriginalDiscountUnitaryCy()) * dataDpsEntry.getOriginalQuantity(), nMiscDecsAmount) / dataDpsEntry.getQuantity();
                        }
                        
                        dataDpsEntry.setPriceUnitaryCy(SLibUtils.round(dEntryPriceUnitCy, SEtlConsts.SIIE_PRICE_UNIT_DECS));    // round price to fixed number of decimals
                        dataDpsEntry.setPriceUnitarySystemCy(SLibUtils.round(dEntryPriceUnitCy, nMiscDecsAmountUnit));          // preserve price as is
                        dataDpsEntry.setDiscountUnitaryCy(0);
                        dataDpsEntry.setDiscountUnitarySystemCy(0);
                        dataDpsEntry.setDiscountEntryCy(0);
                        dataDpsEntry.setSubtotalProvisionalCy_r(SLibUtils.round(((dataDpsEntry.getPriceUnitaryCy() - dataDpsEntry.getDiscountUnitaryCy()) * dataDpsEntry.getQuantity()) - dataDpsEntry.getDiscountEntryCy(), nMiscDecsAmount));
                        dataDpsEntry.setDiscountDocCy(0);
                        dataDpsEntry.setSubtotalCy_r(SLibUtils.round(dataDpsEntry.getSubtotalProvisionalCy_r() - dataDpsEntry.getDiscountDocCy(), nMiscDecsAmount));
                        dataDpsEntry.setTaxChargedCy_r(SLibUtils.round(dataDpsEntry.getSubtotalCy_r() * SEtlConsts.SIIE_TAX_RATE, nMiscDecsAmount));
                        dataDpsEntry.setTaxRetainedCy_r(0);
                        dataDpsEntry.setTotalCy_r(SLibUtils.round(dataDpsEntry.getSubtotalCy_r() + dataDpsEntry.getTaxChargedCy_r() - dataDpsEntry.getTaxRetainedCy_r(), nMiscDecsAmount));
                        
                        dataDpsEntry.setPriceUnitaryRealCy_r(SLibUtils.round(dataDpsEntry.getSubtotalCy_r() / dataDpsEntry.getQuantity(), nMiscDecsAmountUnit));
                        dataDpsEntry.setCommissionsCy_r(0);
                        
                        dEntryTotal = SLibUtils.round(dataDpsEntry.getTotalCy_r() * dataDps.getExchangeRate(), nMiscDecsAmount);
                        dEntrySubtotal = SLibUtils.round(dEntryTotal / (1.0 + SEtlConsts.SIIE_TAX_RATE), nMiscDecsAmount);
                        dEntryTaxRetained = 0;
                        dEntryTaxCharged = SLibUtils.round(dEntryTotal - dEntrySubtotal, nMiscDecsAmount);
                        
                        if (dataItem.getFkUnitId() == row.getDesFinalUnitOfMeasureFk() && dbInvoice.getFkSrcOriginalCurrencyId() == dbInvoice.getFkSrcFinalCurrencyId()) {
                            dEntryPriceUnit = dEntryPriceUnitCy;
                        }
                        else {
                            dEntryPriceUnit = dEntrySubtotal / dataDpsEntry.getQuantity();
                        }
                        
                        dataDpsEntry.setPriceUnitary(SLibUtils.round(dEntryPriceUnit, SEtlConsts.SIIE_PRICE_UNIT_DECS));
                        dataDpsEntry.setPriceUnitarySystem(SLibUtils.round(dEntryPriceUnit, nMiscDecsAmountUnit));
                        dataDpsEntry.setDiscountUnitary(0);
                        dataDpsEntry.setDiscountUnitarySystem(0);
                        dataDpsEntry.setDiscountEntry(0);
                        dataDpsEntry.setSubtotalProvisional_r(dEntrySubtotal);
                        dataDpsEntry.setDiscountDoc(0);
                        dataDpsEntry.setSubtotal_r(dEntrySubtotal);
                        dataDpsEntry.setTaxCharged_r(dEntryTaxCharged);
                        dataDpsEntry.setTaxRetained_r(dEntryTaxRetained);
                        dataDpsEntry.setTotal_r(dEntryTotal);
                        
                        dataDpsEntry.setPriceUnitaryReal_r(SLibUtils.round(dataDpsEntry.getSubtotal_r() / dataDpsEntry.getQuantity(), nMiscDecsAmountUnit));
                        dataDpsEntry.setCommissions_r(0);
                        
                        dataDpsEntry.setLength(0);
                        dataDpsEntry.setSurface(dEntryArea);    // m²
                        dataDpsEntry.setVolume(0);
                        dataDpsEntry.setMass(dEntryWeight);     // kg
                        dataDpsEntry.setWeightPackagingExtra(0);
                        dataDpsEntry.setWeightGross(0);
                        dataDpsEntry.setWeightDelivery(0);
                        dataDpsEntry.setSurplusPercentage(0);
                        dataDpsEntry.setContractBase(0);
                        dataDpsEntry.setContractFuture(0);
                        dataDpsEntry.setContractFactor(0);
                        dataDpsEntry.setContractPriceYear(0);
                        dataDpsEntry.setContractPriceMonth(0);
                        dataDpsEntry.setSealQuality("");
                        dataDpsEntry.setSealSecurity("");
                        dataDpsEntry.setDriver("");
                        dataDpsEntry.setPlate("");
                        dataDpsEntry.setTicket("");
                        dataDpsEntry.setContainerTank("");
                        dataDpsEntry.setVgm("");
                        dataDpsEntry.setOperationsType(SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS);
                        dataDpsEntry.setUserId(SLibConsts.UNDEFINED);
                        //dataDpsEntry.setSortingPosition(...); // set when saved
                        dataDpsEntry.setIsPrepayment(false);
                        dataDpsEntry.setIsDiscountRetailChain(false);
                        dataDpsEntry.setIsTaxesAutomaticApplying(true);
                        dataDpsEntry.setIsPriceVariable(false);
                        dataDpsEntry.setIsPriceConfirm(false);
                        dataDpsEntry.setIsSalesFreightRequired(false);
                        dataDpsEntry.setIsSalesFreightConfirm(false);
                        dataDpsEntry.setIsSalesFreightAdd(false);
                        dataDpsEntry.setIsInventoriable(false); // to prevent unnecessary stock supply status
                        dataDpsEntry.setIsDeleted(false);
                        dataDpsEntry.setFkItemId(dataItem.getPkItemId());
                        dataDpsEntry.setFkUnitId(dataItem.getFkUnitId());
                        dataDpsEntry.setFkOriginalUnitId(row.getDesFinalUnitOfMeasureFk());
                        dataDpsEntry.setFkTaxRegionId(dataBizPartnerCustomer.getDbmsBizPartnerBranchHq().getFkTaxRegionId_n() == SLibConsts.UNDEFINED ? SEtlConsts.SIIE_DEFAULT : dataBizPartnerCustomer.getDbmsBizPartnerBranchHq().getFkTaxRegionId_n());
                        dataDpsEntry.setFkThirdTaxCausingId_n(0);
                        dataDpsEntry.setFkDpsAdjustmentTypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[0]);
                        dataDpsEntry.setFkDpsAdjustmentSubtypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[1]);
                        dataDpsEntry.setFkDpsEntryTypeId(SDataConstantsSys.TRNS_TP_DPS_ETY_ORDY);
                        dataDpsEntry.setFkVehicleTypeId_n(SLibConsts.UNDEFINED);
                        dataDpsEntry.setFkCashCompanyBranchId_n(SLibConsts.UNDEFINED);
                        dataDpsEntry.setFkCashAccountId_n(SLibConsts.UNDEFINED);
                        dataDpsEntry.setFkCostCenterId_n(dbConfigAvista.getDesDefaultCostCenterFk());
                        dataDpsEntry.setFkItemRefId_n(SLibConsts.UNDEFINED);
                        dataDpsEntry.setFkUserNewId(((SDbUser) session.getUser()).getDesUserId());
                        dataDpsEntry.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                        dataDpsEntry.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                        //dataDpsEntry.setUserNewTs(...);
                        //dataDpsEntry.setUserEditTs(...);
                        //dataDpsEntry.setUserDeleteTs(...);
                        
                        dataDpsEntryNotes = new SDataDpsEntryNotes();
                        
                        //dataDpsEntryNotes.setPkYearId(...);
                        //dataDpsEntryNotes.setPkDocId(...);
                        //dataDpsEntryNotes.setPkEntryId(...);
                        //dataDpsEntryNotes.setPkNotesId(...);
                        dataDpsEntryNotes.setNotes(
                                "CANT. SOL/EMB: " + row.getQuantityOrdered() + "/" + row.getQuantity() + "; "
                                        + SEtlConsts.TXT_MISC_WEI.toUpperCase() + ": " + SLibUtils.getDecimalFormatQuantity().format(dEntryWeight) + " " + SEtlConsts.AVISTA_UOM_KG + "; "
                                        + row.getEstimateNumber());
                        dataDpsEntryNotes.setIsAllDocs(true);
                        dataDpsEntryNotes.setIsPrintable(true);
                        dataDpsEntryNotes.setIsCfd(false);
                        dataDpsEntryNotes.setIsDeleted(false);
                        dataDpsEntryNotes.setFkUserNewId(((SDbUser) session.getUser()).getDesUserId());
                        dataDpsEntryNotes.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                        dataDpsEntryNotes.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                        //dataDpsEntryNotes.setUserNewTs(...);
                        //dataDpsEntryNotes.setUserEditTs(...);
                        //dataDpsEntryNotes.setUserDeleteTs(...);
                        
                        dataDpsEntry.getDbmsEntryNotes().add(dataDpsEntryNotes);
                        
                        dataDpsEntryTax = new SDataDpsEntryTax();
                        
                        //dataDpsEntryTax.setPkYearId(...);     // set when saved
                        //dataDpsEntryTax.setPkDocId(...);      // set when saved
                        //dataDpsEntryTax.setPkEntryId(...);    // set when saved
                        dataDpsEntryTax.setPkTaxBasicId(SEtlConsts.SIIE_TAX[0]);
                        dataDpsEntryTax.setPkTaxId(SEtlConsts.SIIE_TAX[1]);
                        dataDpsEntryTax.setPercentage(SEtlConsts.SIIE_TAX_RATE);
                        dataDpsEntryTax.setValueUnitary(0);
                        dataDpsEntryTax.setValue(0);
                        dataDpsEntryTax.setTax(dataDpsEntry.getTaxCharged_r());
                        dataDpsEntryTax.setTaxCy(dataDpsEntry.getTaxChargedCy_r());
                        dataDpsEntryTax.setFkTaxTypeId(erp.mod.SModSysConsts.FINS_TP_TAX_CHARGED);
                        dataDpsEntryTax.setFkTaxCalculationTypeId(erp.mod.SModSysConsts.FINS_TP_TAX_CAL_RATE);
                        dataDpsEntryTax.setFkTaxApplicationTypeId(erp.mod.SModSysConsts.FINS_TP_TAX_APP_CASH);
                        
                        dataDpsEntry.getDbmsEntryTaxes().add(dataDpsEntryTax);
                        
                        dataDps.getDbmsDpsEntries().add(dataDpsEntry);
                    }

                    // Process extra charges, if any:
                    
                    dataDps.calculateTotal(null);
                    
                    double subtotalCy = dataDps.getSubtotalCy_r(); // base amount for charges
                    
                    ArrayList<SDbExtraChargePeriod> periods = SEtlUtils.getExtraChargePeriods(session, dataDps.getDate());
                    
                    for (SDbExtraChargePeriod period : periods) {
                        double chargeCy = SLibUtils.round(subtotalCy * period.getChargePercentage(), nMiscDecsAmount);
                        
                        dataDpsEntry = new SDataDpsEntry();
                        
                        //dataDpsEntry.setPkYearId(...);    // set when saved
                        //dataDpsEntry.setPkDocId(...);     // set when saved
                        //dataDpsEntry.setPkEntryId(...);   // set when saved
                        dataDpsEntry.setConceptKey(period.getCode());
                        dataDpsEntry.setConcept(period.getName());
                        dataDpsEntry.setReference("");
                        
                        dataDpsEntry.setQuantity(1);
                        
                        dataDpsEntry.setIsDiscountDocApplying(true);
                        dataDpsEntry.setIsDiscountUnitaryPercentage(false);
                        dataDpsEntry.setIsDiscountUnitaryPercentageSystem(false);
                        dataDpsEntry.setIsDiscountEntryPercentage(false);
                        dataDpsEntry.setDiscountUnitaryPercentage(0);
                        dataDpsEntry.setDiscountUnitaryPercentageSystem(0);
                        dataDpsEntry.setDiscountEntryPercentage(0);
                        
                        dataDpsEntry.setOriginalQuantity(1);
                        dataDpsEntry.setOriginalPriceUnitaryCy(chargeCy);
                        dataDpsEntry.setOriginalPriceUnitarySystemCy(chargeCy);
                        dataDpsEntry.setOriginalDiscountUnitaryCy(0);
                        dataDpsEntry.setOriginalDiscountUnitarySystemCy(0);
                        dataDpsEntry.setSalesPriceUnitaryCy(0);
                        dataDpsEntry.setSalesFreightUnitaryCy(0);
                        
                        dataDpsEntry.setPriceUnitaryCy(chargeCy);
                        dataDpsEntry.setPriceUnitarySystemCy(chargeCy);
                        dataDpsEntry.setDiscountUnitaryCy(0);
                        dataDpsEntry.setDiscountUnitarySystemCy(0);
                        dataDpsEntry.setDiscountEntryCy(0);
                        dataDpsEntry.setSubtotalProvisionalCy_r(SLibUtils.round(((dataDpsEntry.getPriceUnitaryCy() - dataDpsEntry.getDiscountUnitaryCy()) * dataDpsEntry.getQuantity()) - dataDpsEntry.getDiscountEntryCy(), nMiscDecsAmount));
                        dataDpsEntry.setDiscountDocCy(0);
                        dataDpsEntry.setSubtotalCy_r(SLibUtils.round(dataDpsEntry.getSubtotalProvisionalCy_r() - dataDpsEntry.getDiscountDocCy(), nMiscDecsAmount));
                        dataDpsEntry.setTaxChargedCy_r(SLibUtils.round(dataDpsEntry.getSubtotalCy_r() * SEtlConsts.SIIE_TAX_RATE, nMiscDecsAmount));
                        dataDpsEntry.setTaxRetainedCy_r(0);
                        dataDpsEntry.setTotalCy_r(SLibUtils.round(dataDpsEntry.getSubtotalCy_r() + dataDpsEntry.getTaxChargedCy_r() - dataDpsEntry.getTaxRetainedCy_r(), nMiscDecsAmount));
                        
                        dataDpsEntry.setPriceUnitaryRealCy_r(SLibUtils.round(dataDpsEntry.getSubtotalCy_r() / dataDpsEntry.getQuantity(), nMiscDecsAmountUnit));
                        dataDpsEntry.setCommissionsCy_r(0);
                        
                        dEntryTotal = SLibUtils.round(dataDpsEntry.getTotalCy_r() * dataDps.getExchangeRate(), nMiscDecsAmount);
                        dEntrySubtotal = SLibUtils.round(dEntryTotal / (1.0 + SEtlConsts.SIIE_TAX_RATE), nMiscDecsAmount);
                        dEntryTaxRetained = 0;
                        dEntryTaxCharged = SLibUtils.round(dEntryTotal - dEntrySubtotal, nMiscDecsAmount);
                        
                        if (dbInvoice.getFkSrcOriginalCurrencyId() == dbInvoice.getFkSrcFinalCurrencyId()) {
                            dEntryPriceUnit = chargeCy;
                        }
                        else {
                            dEntryPriceUnit = dEntrySubtotal / dataDpsEntry.getQuantity();
                        }
                        
                        dataDpsEntry.setPriceUnitary(SLibUtils.round(dEntryPriceUnit, nMiscDecsAmount));
                        dataDpsEntry.setPriceUnitarySystem(SLibUtils.round(dEntryPriceUnit, nMiscDecsAmount));
                        dataDpsEntry.setDiscountUnitary(0);
                        dataDpsEntry.setDiscountUnitarySystem(0);
                        dataDpsEntry.setDiscountEntry(0);
                        dataDpsEntry.setSubtotalProvisional_r(dEntrySubtotal);
                        dataDpsEntry.setDiscountDoc(0);
                        dataDpsEntry.setSubtotal_r(dEntrySubtotal);
                        dataDpsEntry.setTaxCharged_r(dEntryTaxCharged);
                        dataDpsEntry.setTaxRetained_r(dEntryTaxRetained);
                        dataDpsEntry.setTotal_r(dEntryTotal);
                        
                        dataDpsEntry.setPriceUnitaryReal_r(SLibUtils.round(dataDpsEntry.getSubtotal_r() / dataDpsEntry.getQuantity(), nMiscDecsAmountUnit));
                        dataDpsEntry.setCommissions_r(0);
                        
                        dataDpsEntry.setLength(0);
                        dataDpsEntry.setSurface(0);
                        dataDpsEntry.setVolume(0);
                        dataDpsEntry.setMass(0);
                        dataDpsEntry.setWeightPackagingExtra(0);
                        dataDpsEntry.setWeightGross(0);
                        dataDpsEntry.setWeightDelivery(0);
                        dataDpsEntry.setSurplusPercentage(0);
                        dataDpsEntry.setContractBase(0);
                        dataDpsEntry.setContractFuture(0);
                        dataDpsEntry.setContractFactor(0);
                        dataDpsEntry.setContractPriceYear(0);
                        dataDpsEntry.setContractPriceMonth(0);
                        dataDpsEntry.setSealQuality("");
                        dataDpsEntry.setSealSecurity("");
                        dataDpsEntry.setDriver("");
                        dataDpsEntry.setPlate("");
                        dataDpsEntry.setTicket("");
                        dataDpsEntry.setContainerTank("");
                        dataDpsEntry.setVgm("");
                        dataDpsEntry.setOperationsType(SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS);
                        dataDpsEntry.setUserId(SLibConsts.UNDEFINED);
                        //dataDpsEntry.setSortingPosition(...); // set when saved
                        dataDpsEntry.setIsPrepayment(false);
                        dataDpsEntry.setIsDiscountRetailChain(false);
                        dataDpsEntry.setIsTaxesAutomaticApplying(true);
                        dataDpsEntry.setIsPriceVariable(false);
                        dataDpsEntry.setIsPriceConfirm(false);
                        dataDpsEntry.setIsSalesFreightRequired(false);
                        dataDpsEntry.setIsSalesFreightConfirm(false);
                        dataDpsEntry.setIsSalesFreightAdd(false);
                        dataDpsEntry.setIsInventoriable(false); // to prevent unnecessary stock supply status
                        dataDpsEntry.setIsDeleted(false);
                        dataDpsEntry.setFkItemId(period.getDbmsExtraCharge().getDesItemId());
                        dataDpsEntry.setFkUnitId(period.getDbmsExtraCharge().getDesUnitOfMeasureId());
                        dataDpsEntry.setFkOriginalUnitId(period.getDbmsExtraCharge().getDesUnitOfMeasureId());
                        dataDpsEntry.setFkTaxRegionId(dataBizPartnerCustomer.getDbmsBizPartnerBranchHq().getFkTaxRegionId_n() == SLibConsts.UNDEFINED ? SEtlConsts.SIIE_DEFAULT : dataBizPartnerCustomer.getDbmsBizPartnerBranchHq().getFkTaxRegionId_n());
                        dataDpsEntry.setFkThirdTaxCausingId_n(0);
                        dataDpsEntry.setFkDpsAdjustmentTypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[0]);
                        dataDpsEntry.setFkDpsAdjustmentSubtypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[1]);
                        dataDpsEntry.setFkDpsEntryTypeId(SDataConstantsSys.TRNS_TP_DPS_ETY_ORDY);
                        dataDpsEntry.setFkVehicleTypeId_n(SLibConsts.UNDEFINED);
                        dataDpsEntry.setFkCashCompanyBranchId_n(SLibConsts.UNDEFINED);
                        dataDpsEntry.setFkCashAccountId_n(SLibConsts.UNDEFINED);
                        dataDpsEntry.setFkCostCenterId_n(dbConfigAvista.getDesDefaultCostCenterFk());
                        dataDpsEntry.setFkItemRefId_n(SLibConsts.UNDEFINED);
                        dataDpsEntry.setFkUserNewId(((SDbUser) session.getUser()).getDesUserId());
                        dataDpsEntry.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                        dataDpsEntry.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                        //dataDpsEntry.setUserNewTs(...);
                        //dataDpsEntry.setUserEditTs(...);
                        //dataDpsEntry.setUserDeleteTs(...);
                        
                        dataDpsEntryNotes = new SDataDpsEntryNotes();
                        
                        //dataDpsEntryNotes.setPkYearId(...);
                        //dataDpsEntryNotes.setPkDocId(...);
                        //dataDpsEntryNotes.setPkEntryId(...);
                        //dataDpsEntryNotes.setPkNotesId(...);
                        dataDpsEntryNotes.setNotes("BASE: $" + SLibUtils.getDecimalFormatAmount().format(subtotalCy) + " " + dbInvoiceCurrencyReq.getCode() + ".");
                        dataDpsEntryNotes.setIsAllDocs(true);
                        dataDpsEntryNotes.setIsPrintable(true);
                        dataDpsEntryNotes.setIsCfd(false);
                        dataDpsEntryNotes.setIsDeleted(false);
                        dataDpsEntryNotes.setFkUserNewId(((SDbUser) session.getUser()).getDesUserId());
                        dataDpsEntryNotes.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                        dataDpsEntryNotes.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                        //dataDpsEntryNotes.setUserNewTs(...);
                        //dataDpsEntryNotes.setUserEditTs(...);
                        //dataDpsEntryNotes.setUserDeleteTs(...);
                        
                        dataDpsEntry.getDbmsEntryNotes().add(dataDpsEntryNotes);
                        
                        dataDpsEntryTax = new SDataDpsEntryTax();
                        
                        //dataDpsEntryTax.setPkYearId(...);     // set when saved
                        //dataDpsEntryTax.setPkDocId(...);      // set when saved
                        //dataDpsEntryTax.setPkEntryId(...);    // set when saved
                        dataDpsEntryTax.setPkTaxBasicId(SEtlConsts.SIIE_TAX[0]);
                        dataDpsEntryTax.setPkTaxId(SEtlConsts.SIIE_TAX[1]);
                        dataDpsEntryTax.setPercentage(SEtlConsts.SIIE_TAX_RATE);
                        dataDpsEntryTax.setValueUnitary(0);
                        dataDpsEntryTax.setValue(0);
                        dataDpsEntryTax.setTax(dataDpsEntry.getTaxCharged_r());
                        dataDpsEntryTax.setTaxCy(dataDpsEntry.getTaxChargedCy_r());
                        dataDpsEntryTax.setFkTaxTypeId(erp.mod.SModSysConsts.FINS_TP_TAX_CHARGED);
                        dataDpsEntryTax.setFkTaxCalculationTypeId(erp.mod.SModSysConsts.FINS_TP_TAX_CAL_RATE);
                        dataDpsEntryTax.setFkTaxApplicationTypeId(erp.mod.SModSysConsts.FINS_TP_TAX_APP_CASH);
                        
                        dataDpsEntry.getDbmsEntryTaxes().add(dataDpsEntryTax);
                        
                        dataDps.getDbmsDpsEntries().add(dataDpsEntry);
                    }
                    
                    // Proceed to save invoice:
                    
                    if (!periods.isEmpty()) {
                        dataDps.calculateTotal(null); // recalculate total
                    }

                    if (dataDps.save(etlPackage.ConnectionSiie) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SEtlConsts.MSG_ERR_SIIE_ITM_INS + "'" + SLibUtils.textTrim(rsAvistaInvoiceData.getString("InvoiceNumber")) + "'."
                                                + sInvoiceErrMsg);
                    }

                    stSiie.execute("COMMIT");
                }
                catch (Exception e) {
                    stSiie.execute("ROLLBACK");
                    throw e;
                }
                
                // Update ETL invoice:
                
                dbInvoice.setDesInvoiceYearId(dataDps.getPkYearId());
                dbInvoice.setDesInvoiceDocumentId(dataDps.getPkDocId());
                dbInvoice.setFinalSeries(dataDps.getNumberSeries());
                dbInvoice.setFinalNumber(dataDps.getNumber());
                //dbInvoice.setFinalAmount(SLibUtils.round(dInvoiceAmountReq, nMiscDecsAmount));    // variable "dInvoiceAmountReq" should be used, but due to the arbitrary fixed number of decimals on price,
                dbInvoice.setFinalAmount(dataDps.getSubtotalCy_r());                                // data member "dataDps.getSubtotalCy_r()" suits better
                dbInvoice.save(session);
            }
        }

        String message = nInvoicesCount + " facturas exportadas de " + nInvoces + " encontrados.";
        
        etlPackage.EtlLog.setStep(SEtlConsts.STEP_INV_END);
        
        etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_NA);
        etlPackage.EtlLog.save(session);
        
        session.notifySuscriptors(SModConsts.A_INV);
        
        return message;
    }
}
