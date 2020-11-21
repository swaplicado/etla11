/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mitm.data.SDataItem;
import erp.mod.SModSysConsts;
import etla.mod.SModConsts;
import etla.mod.cfg.db.SDbConfig;
import etla.mod.cfg.db.SDbUser;
import java.sql.ResultSet;
import java.sql.Statement;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public class SEtlProcessCatItems {
    
    public static String computeEtlItems(final SGuiSession session, final SEtlPackage etlPackage) throws Exception {
        int nCount = 0;
        int nItems = 0;
        int nSiieItemId = 0;
        int nSiieItemAliveId = 0;
        int nSiieItemDeletedId = 0;
        int nAvistaItemId = 0;
        String sItemCode = "";
        String sItemCodeRaw = "";
        String sItemName = "";
        String sql = "";
        Statement statementEtl = session.getStatement().getConnection().createStatement();
        Statement statementSiie = etlPackage.ConnectionSiie.createStatement();
        Statement statementAvista = etlPackage.ConnectionAvista.createStatement();
        ResultSet resultSetEtl = null;
        ResultSet resultSetSiie = null;
        ResultSet resultSetAvista = null;
        SDataItem dataItem = null;
        SDbConfigAvista dbConfigAvista = ((SDbConfig) session.getConfigSystem()).getDbConfigAvista();
        SDbItem dbItem = null;
        SEtlCatalogs etlCatalogs = null;
        
        etlPackage.EtlLog.setStep(SEtlConsts.STEP_ITM_STA);
        
        etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_NA);
        etlPackage.EtlLog.save(session);
        
        etlCatalogs = new SEtlCatalogs(session, false, false);
        
        // Obtener la cantidad de Items
        
        sql = "SELECT DISTINCT COUNT(*) "
                + "FROM dbo.CustomerInvoices AS ci "
                + "INNER JOIN dbo.CustomerInvoiceItems AS cii ON cii.CustomerInvoiceKey=ci.CustomerInvoiceKey "
                + "INNER JOIN dbo.CustomerOrder AS co ON co.OrderKey=cii.OrderKey "
                + "INNER JOIN dbo.PlantEst AS pe ON pe.EstKey=co.EstKey "
                + "INNER JOIN dbo.PlantBoardType AS pbt ON pbt.PlantBoardTypeKey=pe.PlantBoardTypeKey "
                + "WHERE CAST(ci.Created AS DATE) BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodEnd) + "' AND "
                + "ci.CurrentStatusKey IN (" + SEtlConsts.AVISTA_INV_STA_APP + ", " + SEtlConsts.AVISTA_INV_STA_ARC + ") AND "
                + "ci.CustomerInvoiceTypeKey=" + SEtlConsts.AVISTA_INV_TP_INV + " "
                + (etlPackage.InvoiceBatch == SLibConsts.UNDEFINED ? "" : "AND ci.BatchNumber=" + etlPackage.InvoiceBatch + " ");
        resultSetAvista = statementAvista.executeQuery(sql);
        while (resultSetAvista.next()) {
            nItems = resultSetAvista.getInt(1);
        }
        
        // Obtain items list from Avista:
        sql = "SELECT DISTINCT pe.PlantBoardTypeKey, pbt.ShortDesc, pe.Flute "
                + "FROM dbo.CustomerInvoices AS ci "
                + "INNER JOIN dbo.CustomerInvoiceItems AS cii ON cii.CustomerInvoiceKey=ci.CustomerInvoiceKey "
                + "INNER JOIN dbo.CustomerOrder AS co ON co.OrderKey=cii.OrderKey "
                + "INNER JOIN dbo.PlantEst AS pe ON pe.EstKey=co.EstKey "
                + "INNER JOIN dbo.PlantBoardType AS pbt ON pbt.PlantBoardTypeKey=pe.PlantBoardTypeKey "
                + "WHERE CAST(ci.Created AS DATE) BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(etlPackage.PeriodEnd) + "' AND "
                + "ci.CurrentStatusKey IN (" + SEtlConsts.AVISTA_INV_STA_APP + ", " + SEtlConsts.AVISTA_INV_STA_ARC + ") AND "
                + "ci.CustomerInvoiceTypeKey=" + SEtlConsts.AVISTA_INV_TP_INV + " "
                + (etlPackage.InvoiceBatch == SLibConsts.UNDEFINED ? "" : "AND ci.BatchNumber=" + etlPackage.InvoiceBatch + " ")
                + "ORDER BY pe.PlantBoardTypeKey, pe.Flute ";
        resultSetAvista = statementAvista.executeQuery(sql);
        while (resultSetAvista.next()) {
            /****************************************************************/
            if (SEtlConsts.SHOW_DEBUG_MSGS) {
                System.out.println(SEtlConsts.TXT_ITM + " (" + ++nCount + "): " + SLibUtils.textTrim(resultSetAvista.getString("ShortDesc") + " " + resultSetAvista.getString("Flute")));
            }
            /****************************************************************/
            
            // From SIIE, obtain oldest item, alive and deleted ones, both of them when possible:
            
            etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_ITM_AUX_1);
            etlPackage.EtlLog.save(session);

            nSiieItemAliveId = 0;
            nSiieItemDeletedId = 0;
            sItemCode = SEtlUtils.composeItemCode(dbConfigAvista.getDesItemCodePrefix(), resultSetAvista.getInt("PlantBoardTypeKey"), resultSetAvista.getString("Flute"));
            sItemCodeRaw = SEtlUtils.composeItemCodeRaw(resultSetAvista.getInt("PlantBoardTypeKey"), resultSetAvista.getString("Flute"));
            sItemName = SLibUtils.textToSql(resultSetAvista.getString("ShortDesc")) + " " + resultSetAvista.getString("Flute");
            
            sql = "SELECT id_item, b_del "
                    + "FROM erp.itmu_item "
                    + "WHERE item_key='" + sItemCode + "' "
                    + "ORDER BY id_item ";
            
            resultSetSiie = statementSiie.executeQuery(sql);
            while (resultSetSiie.next()) {
                if (!resultSetSiie.getBoolean("b_del")) {
                    nSiieItemAliveId = resultSetSiie.getInt("id_item");
                    break;
                }
                else {
                    if (nSiieItemDeletedId == 0) {
                        nSiieItemDeletedId = resultSetSiie.getInt("id_item");
                    }
                }
            }
            
            try {
                nSiieItemId = nSiieItemAliveId != 0 ? nSiieItemAliveId : nSiieItemDeletedId;

                if (nSiieItemId == 0) {
                    // Item is new on SIIE:

                    statementSiie.execute("START TRANSACTION");

                    // Create item registry:

                    dataItem = new SDataItem();
                    //dataItem.setPkItemId(...); // set on save
                    dataItem.setKey(sItemCode);
                    dataItem.setItem(sItemName);
                    dataItem.setItemShort(sItemName);
                    dataItem.setName(sItemName);
                    dataItem.setNameShort(sItemName);
                    dataItem.setPresentation("");
                    dataItem.setPresentationShort("");
                    dataItem.setCode(sItemCodeRaw);
                    dataItem.setPartNumber("");
                    dataItem.setIsInventoriable(true); // required to enable alternative unit type (when quantity is required as unit instead of surface)
                    dataItem.setIsLotApplying(false);
                    dataItem.setIsBulk(true);
                    dataItem.setUnitsContained(0);
                    dataItem.setUnitsVirtual(0);
                    dataItem.setUnitsPackage(0);
                    dataItem.setNetContent(0);
                    dataItem.setNetContentUnitary(0);
                    dataItem.setIsNetContentVariable(false);
                    dataItem.setLength(0);
                    dataItem.setLengthUnitary(0);
                    dataItem.setIsLengthVariable(false);
                    dataItem.setSurface(1);
                    dataItem.setSurfaceUnitary(0);
                    dataItem.setIsSurfaceVariable(true);
                    dataItem.setVolume(0);
                    dataItem.setVolumeUnitary(0);
                    dataItem.setIsVolumeVariable(false);
                    dataItem.setMass(1);
                    dataItem.setMassUnitary(0);
                    dataItem.setIsMassVariable(true);
                    dataItem.setProductionTime(0);
                    dataItem.setProductionCost(0);
                    dataItem.setWeightGross(0);
                    dataItem.setWeightDelivery(0);
                    dataItem.setUnitAlternativeBaseEquivalence(0);
                    dataItem.setSurplusPercentage(0);
                    dataItem.setTariff("");
                    dataItem.setCustomsUnit("");
                    dataItem.setCustomsEquivalence(0);
                    dataItem.setIsReference(false);
                    dataItem.setIsPrepayment(false);
                    dataItem.setIsFreePrice(false);
                    dataItem.setIsFreeDiscount(false);
                    dataItem.setIsFreeDiscountUnitary(false);
                    dataItem.setIsFreeDiscountEntry(false);
                    dataItem.setIsFreeDiscountDoc(false);
                    dataItem.setIsFreeCommissions(false);
                    dataItem.setIsSalesFreightRequired(false);
                    dataItem.setIsDeleted(false);
                    dataItem.setFkItemGenericId(dbConfigAvista.getDesDefaultItemGenericFk());
                    dataItem.setFkItemLineId_n(SLibConsts.UNDEFINED);
                    dataItem.setFkItemStatusId(SModSysConsts.ITMS_ST_ITEM_ACT);
                    dataItem.setFkUnitId(etlCatalogs.getEtlUnitOfMeasure(etlCatalogs.getEtlIdForUnitOfMeasure(dbConfigAvista.getSrcDefaultUnitOfMeasureFk())).getDesUnitOfMeasureId());
                    dataItem.setFkUnitUnitsContainedId(SModSysConsts.ITMU_UNIT_NA);
                    dataItem.setFkUnitUnitsVirtualId(SModSysConsts.ITMU_UNIT_NA);
                    dataItem.setFkUnitNetContentId(SModSysConsts.ITMU_UNIT_NA);
                    dataItem.setFkUnitNetContentUnitaryId(SModSysConsts.ITMU_UNIT_NA);
                    dataItem.setFkUnitAlternativeTypeId(SModSysConsts.ITMU_TP_UNIT_QTY); // required to enable alternative unit type (when quantity is required as unit instead of surface)
                    dataItem.setFkLevelTypeId(SDataConstantsSys.ITMU_TP_LEV_NA);
                    dataItem.setFkBrandId(SDataConstantsSys.ITMU_BRD_NA);
                    dataItem.setFkManufacturerId(SDataConstantsSys.ITMU_MFR_NA);
                    dataItem.setFkElementId(SDataConstantsSys.ITMU_EMT_NA);
                    dataItem.setFkVariety01Id(SDataConstantsSys.ITMU_VAR_NA);
                    dataItem.setFkVariety02Id(SDataConstantsSys.ITMU_VAR_NA);
                    dataItem.setFkVariety03Id(SDataConstantsSys.ITMU_VAR_NA);
                    dataItem.setFkAdministrativeConceptTypeId(SDataConstantsSys.NA);
                    dataItem.setFkTaxableConceptTypeId(SDataConstantsSys.NA);
                    dataItem.setFkAccountEbitdaTypeId(SDataConstantsSys.NA);
                    dataItem.setFkFiscalAccountIncId(SModSysConsts.FIN_ACC_NA);
                    dataItem.setFkFiscalAccountExpId(SModSysConsts.FIN_ACC_NA);
                    dataItem.setFkItemPackageId_n(SLibConsts.UNDEFINED);
                    dataItem.setFkDefaultItemRefId_n(SLibConsts.UNDEFINED);
                    dataItem.setFkCfdProdServId_n(SLibConsts.UNDEFINED);
                    dataItem.setFkUserNewId(((SDbUser) session.getUser()).getDesUserId());
                    dataItem.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
                    dataItem.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
                    //dataItem.setUserNewTs(...);
                    //dataItem.setUserEditTs(...);
                    //dataItem.setUserDeleteTs(...);
                    
                    // Save new item:

                    if (dataItem.save(etlPackage.ConnectionSiie) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SEtlConsts.MSG_ERR_SIIE_ITM_INS + "'" + SLibUtils.textTrim(resultSetAvista.getString("ShortDesc")) + "'.");
                    }
                    
                    statementSiie.execute("COMMIT");
                    
                    nSiieItemId = dataItem.getPkItemId();
                }
                else {
                    // Item already exists on SIIE:

                    /* Future features not implemented yet (sflores, 2015-11-27)
                    dataItem = new SDataItem();
                    if (dataItem.read(new int[] { nSiieItemId }, statementSiie) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SEtlConsts.MSG_ERR_SIIE_ITM_QRY + "'" + SLibUtils.textTrim(resultSetAvista.getString("ShortDesc")) + "'.");
                    }
                    
                    if (dataBizPartner.getExternalId().compareTo(resultSetAvista.getString("CustomerId")) != 0) {
                        
                        statementSiie.execute("START TRANSACTION");

                        dataBizPartner.setExternalId(resultSetAvista.getString("CustomerId"));
                        //dataBizPartner.setFkUserNewId(...); // by now, no trace of updating user required
                        //dataBizPartner.setFkUserEditId(...); // by now, no trace of updating user required
                        //dataBizPartner.setFkUserDeleteId(...); // by now, no trace of updating user required
                        
                        if (dataBizPartner.save(etlPackage.ConnectionSiie) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SEtlConsts.MSG_ERR_SIIE_ITM_UPD + "'" + SLibUtils.textTrim(resultSetAvista.getString("CustomerName")) + "'.");
                        }
                        
                        statementSiie.execute("COMMIT");
                    }
                    */
                }
                
            }
            catch (Exception e) {
                statementSiie.execute("ROLLBACK");
                throw e;
            }
            
            // From Avista obtain item:
            
            etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_ITM_AUX_2);
            etlPackage.EtlLog.save(session);
            
            nAvistaItemId = 0;
            
            sql = "SELECT id_itm "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.AU_ITM) + " "
                    + "WHERE code='" + sItemCode + "' "
                    + "ORDER BY id_itm ";
            resultSetEtl = statementEtl.executeQuery(sql);
            if (resultSetEtl.next()) {
                nAvistaItemId = resultSetEtl.getInt(1);
            }
            
            try {
                if (nAvistaItemId == 0) {
                    // Item is new on ETL:

                    statementEtl.execute("START TRANSACTION");
                    
                    dbItem = new SDbItem();
                    //dbItem.setPkItemId(...); // set on save
                    dbItem.setDesItemId(nSiieItemId);
                    dbItem.setCode(sItemCode);
                    dbItem.setName(sItemName);
                    dbItem.setNameBoardType(SLibUtils.textToSql(resultSetAvista.getString("ShortDesc")));
                    dbItem.setNameFlute(resultSetAvista.getString("Flute"));
                    dbItem.setSrcBoardTypeFk(resultSetAvista.getInt("PlantBoardTypeKey"));
                    dbItem.setSrcFluteFk(resultSetAvista.getString("Flute"));
                    dbItem.setSrcCustomerFk_n("");
                    dbItem.setSrcRequiredCurrencyFk_n(SLibConsts.UNDEFINED);
                    dbItem.setSrcRequiredUnitOfMeasureFk_n("");
                    //dbItem.setFirstEtlInsert(...); // set on save
                    //dbItem.setLastEtlUpdate(...); // set on save
                    dbItem.setDeleted(false);
                    dbItem.setSystem(false);
                    dbItem.setFkSrcCustomerId_n(SLibConsts.UNDEFINED);
                    dbItem.setFkSrcRequiredCurrencyId_n(SLibConsts.UNDEFINED);
                    dbItem.setFkSrcRequiredUnitOfMeasureId_n(SLibConsts.UNDEFINED);
                    dbItem.setFkLastEtlLogId(etlPackage.EtlLog.getPkEtlLogId());
                    //dbItem.setFkUserInsertId(...); // set on save
                    //dbItem.setFkUserUpdateId(...); // set on save
                    //dbItem.setTsUserInsert(...); // set on save
                    //dbItem.setTsUserUpdate(...); // set on save
                    
                    dbItem.save(session);
                    
                    statementEtl.execute("COMMIT");
                }
                else {
                    // Item already exists on ETL:

                    //item = new SDbItem();
                }
            }
            catch (Exception e) {
                statementEtl.execute("ROLLBACK");
                throw e;
            }

            etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_ITM_AUX_3);
            etlPackage.EtlLog.save(session);
        }
        
        String message = nCount + " items exportados de " + nItems + " encontrados.\n";
        
        etlPackage.EtlLog.setStep(SEtlConsts.STEP_ITM_END);
        
        etlPackage.EtlLog.setStepAux(SEtlConsts.STEP_AUX_NA);
        etlPackage.EtlLog.save(session);
        
        session.notifySuscriptors(SModConsts.AU_ITM);
        
        return message;
    }
}
