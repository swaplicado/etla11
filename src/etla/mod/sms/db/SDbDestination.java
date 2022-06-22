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
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Daniel López, Alfredo Pérez, Sergio Flores, Isabel Servín
 */
public class SDbDestination extends SDbRegistryUser{
    
    protected int mnPkDestinationId;
    protected int mnSiteLocationId;
    protected String msCode;
    protected String msName;
    protected String msAddress1;
    protected String msAddress2;
    protected String msAddress3;
    /** En realidad contiene municipio */
    protected String msDistrict;
    protected String msCity;
    /** En realidad contiene la colonia */
    protected String msCounty;
    protected String msState;
    protected String msZipCode;
    protected String msCountry;

    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbDestination () {
        super(SModConsts.SU_DESTIN);
    }
    
    private void checkSiteLocationId(SGuiSession session) throws SQLException {
        ResultSet resultSet;

        mnPkDestinationId = 0;

        msSql = "SELECT id_destin FROM " + getSqlTable() + " WHERE site_loc_id = " + mnSiteLocationId;
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkDestinationId = resultSet.getInt(1);
            mbRegistryNew = false;
        }
    }

    /*
     * Public methods
     */

    public void setPkDestinationId(int n) { mnPkDestinationId = n; }
    public void setSiteLocationId(int n) { mnSiteLocationId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setAddress1(String s) { msAddress1 = s; }
    public void setAddress2(String s) { msAddress2 = s; }
    public void setAddress3(String s) { msAddress3 = s; }
    public void setDistrict(String s) { msDistrict = s; }
    public void setCity(String s) { msCity = s; }
    public void setCounty(String s) { msCounty = s; }
    public void setState(String s) { msState = s; }
    public void setZipCode(String s) { msZipCode = s; }
    public void setCountry(String s) { msCountry = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkDestinationId() { return mnPkDestinationId; }
    public int getSiteLocationId() { return mnSiteLocationId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getAddress1() { return msAddress1; }
    public String getAddress2() { return msAddress2; }
    public String getAddress3() { return msAddress3; }
    public String getDistrict() { return msDistrict; }
    public String getCity() { return msCity; }
    public String getCounty() { return msCounty; }
    public String getState() { return msState; }
    public String getZipCode() { return msZipCode; }
    public String getCountry() { return msCountry; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDestinationId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDestinationId };
    }

    @Override
    public void initRegistry() {

        initBaseRegistry();

        mnPkDestinationId = 0;
        mnSiteLocationId = 0;
        msCode = "";
        msName = "";
        msAddress1 = "";
        msAddress2 = "";
        msAddress3 = "";
        msDistrict = "";
        msCity = "";
        msCounty = "";
        msState = "";
        msZipCode = "";
        msCountry = "";
        mbDeleted = false;
        mbSystem = false;
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
        return "WHERE id_destin = " + mnPkDestinationId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_destin = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;

        mnPkDestinationId = 0;

        msSql = "SELECT COALESCE(MAX(id_destin), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkDestinationId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkDestinationId = resultSet.getInt("id_destin");
            mnSiteLocationId = resultSet.getInt("site_loc_id");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msAddress1 = resultSet.getString("address1");
            msAddress2 = resultSet.getString("address2");
            msAddress3 = resultSet.getString("address3");
            msDistrict = resultSet.getString("district");
            msCity = resultSet.getString("city");
            msCounty = resultSet.getString("county");
            msState = resultSet.getString("state");
            msZipCode = resultSet.getString("zip_code");
            msCountry = resultSet.getString("country");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
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
            checkSiteLocationId(session);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                mnPkDestinationId + ", " +
                mnSiteLocationId + ", " + 
                "'" + msCode + "', " + 
                "'" + msName + "', " + 
                "'" + msAddress1 + "', " + 
                "'" + msAddress2 + "', " + 
                "'" + msAddress3 + "', " + 
                "'" + msDistrict + "', " + 
                "'" + msCity + "', " + 
                "'" + msCounty + "', " + 
                "'" + msState + "', " + 
                "'" + msZipCode + "', " + 
                "'" + msCountry + "', " + 
                (mbDeleted ? 1 : 0) + ", " + 
                (mbSystem ? 1 : 0) + ", " + 
                mnFkUserInsertId + ", " + 
                mnFkUserUpdateId + ", " + 
                "NOW()" + ", " + 
                "NOW()" + 
                 ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_destin = " + mnPkDestinationId + ", " +
                    "site_loc_id = " + mnSiteLocationId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "address1 = '" + msAddress1 + "', " +
                    "address2 = '" + msAddress2 + "', " +
                    "address3 = '" + msAddress3 + "', " +
                    "district = '" + msDistrict + "', " +
                    "city = '" + msCity + "', " +
                    "county = '" + msCounty + "', " +
                    "state = '" + msState + "', " +
                    "zip_code = '" + msZipCode + "', " +
                    "country = '" + msCountry + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW() " +
                 getSqlWhere(); 
        }

        session.getStatement().execute(msSql);

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbDestination clone() throws CloneNotSupportedException {
        SDbDestination  registry = new SDbDestination();

        registry.setPkDestinationId(this.getPkDestinationId());
        registry.setSiteLocationId(this.getSiteLocationId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setAddress1(this.getAddress1());
        registry.setAddress2(this.getAddress2());
        registry.setAddress3(this.getAddress3());
        registry.setDistrict(this.getDistrict());
        registry.setCity(this.getCity());
        registry.setCounty(this.getCounty());
        registry.setState(this.getState());
        registry.setZipCode(this.getZipCode());
        registry.setCountry(this.getCountry());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
