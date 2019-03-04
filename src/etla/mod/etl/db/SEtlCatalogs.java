/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

import etla.mod.SModConsts;
import etla.mod.cfg.db.SDbConfig;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SEtlCatalogs {
    
    private SGuiSession moSession;
    private SDbConfigAvista moConfigAvista;
    private boolean mbReadSalesAgents;
    private boolean mbReadItems;
    private ArrayList<SDbSysCurrency> maCurrencies;
    private ArrayList<SDbSysUnitOfMeasure> maUnitOfMeasures;
    private ArrayList<SDbSalesAgent> maSalesAgents;
    private ArrayList<SDbItem> maItems;
    
    public SEtlCatalogs(SGuiSession session, boolean readSalesAgents, boolean readItems) throws Exception {
        moSession = session;
        moConfigAvista = ((SDbConfig) session.getConfigSystem()).getDbConfigAvista();
        mbReadSalesAgents = readSalesAgents;
        mbReadItems = readItems;
        
        maCurrencies = new ArrayList<>();
        maUnitOfMeasures = new ArrayList<>();
        
        if (mbReadSalesAgents) {
            maSalesAgents = new ArrayList<>();
        }
        
        if (mbReadItems) {
            maItems = new ArrayList<>();
        }
        
        readCatalogs();
    }
    
    /*
     * Private methods:
     */

    private void readCatalogs() throws Exception {
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;
        
        statement = moSession.getStatement().getConnection().createStatement();
        
        sql = "SELECT id_cur FROM " + SModConsts.TablesMap.get(SModConsts.AS_CUR) + " ORDER BY id_cur ";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            SDbSysCurrency db = new SDbSysCurrency();
            db.read(moSession, new int[] { resultSet.getInt(1) });
            maCurrencies.add(db);
        }
        
        sql = "SELECT id_uom FROM " + SModConsts.TablesMap.get(SModConsts.AS_UOM) + " ORDER BY id_uom ";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            SDbSysUnitOfMeasure db = new SDbSysUnitOfMeasure();
            db.read(moSession, new int[] { resultSet.getInt(1) });
            maUnitOfMeasures.add(db);
        }
        
        if (mbReadSalesAgents) {
            sql = "SELECT id_sal_agt FROM " + SModConsts.TablesMap.get(SModConsts.AU_SAL_AGT) + " WHERE b_del=0 ORDER BY id_sal_agt ";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                SDbSalesAgent db = new SDbSalesAgent();
                db.read(moSession, new int[] { resultSet.getInt(1) });
                maSalesAgents.add(db);
            }
        }
        
        if (mbReadItems) {
            sql = "SELECT id_itm FROM " + SModConsts.TablesMap.get(SModConsts.AU_ITM) + " WHERE b_del=0 ORDER BY id_itm ";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                SDbItem db = new SDbItem();
                db.read(moSession, new int[] { resultSet.getInt(1) });
                maItems.add(db);
            }
        }
    }
    
    /*
     * Public methods:
     */
    
    public ArrayList<SDbSysCurrency> getCurrencies() { return maCurrencies; }
    public ArrayList<SDbSysUnitOfMeasure> getUnitOfMeasures() { return maUnitOfMeasures; }
    public ArrayList<SDbItem> getItems() { return maItems; }
    
    /**
     * @param etlId ETL's currency PK.
     */
    public SDbSysCurrency getEtlCurrency(final int etlId) {
        SDbSysCurrency db = null;
        
        for (SDbSysCurrency c : maCurrencies) {
            if (c.getPkCurrencyId() == etlId) {
                db = c;
                break;
            }
        }
        
        return db;
    }
    
    /**
     * @param etlId ETL's unit of measure PK.
     */
    public SDbSysUnitOfMeasure getEtlUnitOfMeasure(final int etlId) {
        SDbSysUnitOfMeasure db = null;
        
        for (SDbSysUnitOfMeasure u : maUnitOfMeasures) {
            if (u.getPkUnitOfMeasureId() == etlId) {
                db = u;
                break;
            }
        }
        
        return db;
    }
    
    /**
     * @param etlId ETL's sales agent PK.
     */
    public SDbSalesAgent getEtlSalesAgent(final int etlId) {
        SDbSalesAgent db = null;
        
        for (SDbSalesAgent s : maSalesAgents) {
            if (s.getPkSalesAgentId()== etlId) {
                db = s;
                break;
            }
        }
        
        return db;
    }
    
    /**
     * @param etlId ETL's item PK.
     */
    public SDbItem getEtlItem(final int etlId) {
        SDbItem db = null;
        
        for (SDbItem i : maItems) {
            if (i.getPkItemId()== etlId) {
                db = i;
                break;
            }
        }
        
        return db;
    }
    
    /**
     * @param sourcePk Avista's currency PK.
     */
    public int getEtlIdForCurrency(final int sourcePk) {
        int id = 0;
        
        for (SDbSysCurrency c : maCurrencies) {
            if (c.getSrcCurrencyId() == sourcePk) {
                id = c.getPkCurrencyId();
                break;
            }
        }
        
        return id;
    }
    
    /**
     * @param sourcePk Avista's unit of measure PK.
     */
    public int getEtlIdForUnitOfMeasure(final String sourcePk) {
        int id = 0;
        
        for (SDbSysUnitOfMeasure i : maUnitOfMeasures) {
            if (i.getSrcUnitOfMeasureId().compareTo(sourcePk) == 0) {
                id = i.getPkUnitOfMeasureId();
                break;
            }
        }
        
        return id;
    }
    
    /**
     * @param sourcePk Avista's sales agent PK.
     */
    public int getEtlIdForSalesAgent(final int sourcePk) {
        int id = 0;
        
        for (SDbSalesAgent s : maSalesAgents) {
            if (s.getSrcSalesAgentId()== sourcePk) {
                id = s.getPkSalesAgentId();
                break;
            }
        }
        
        return id;
    }
    
    /**
     * @param boardTypePk Avista's board type PK.
     * @param flutePk Avista's flute PK.
     * @param customerPk Avista's customer PK.
     */
    public int getEtlIdForItem(final int boardTypePk, final String flutePk, final String customerPk) {
        int id = 0;
        
        for (SDbItem i : maItems) {
            if (i.getSrcBoardTypeFk() == boardTypePk && i.getSrcFluteFk().compareTo(flutePk) == 0 && i.getSrcCustomerFk_n().compareTo(customerPk) == 0) {
                id = i.getPkItemId();
                break;
            }
        }
        
        if (id == 0) {
            for (SDbItem i : maItems) {
                if (i.getSrcBoardTypeFk() == boardTypePk && i.getSrcFluteFk().compareTo(flutePk) == 0) {
                    id = i.getPkItemId();
                    break;
                }
            }
        }
        
        return id;
    }
}
