/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.db;

import erp.lib.SLibUtilities;
import erp.mod.SModDataUtils;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsEntry;
import etla.gui.SGuiMain;
import etla.mod.SModConsts;
import etla.mod.SModSysConsts;
import etla.mod.cfg.db.SDbConfig;
import etla.mod.etl.db.SEtlConsts;
import etla.mod.etl.db.SEtlProcess;
import etla.mod.sms.bol.CPTLoginResponse;
import etla.mod.sms.bol.SBolLocations;
import etla.mod.sms.bol.SBolMerchandises;
import etla.mod.sms.bol.SBolShipment;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import sa.lib.mail.SMailUtils;

/**
 *
 * @author Daniel López, Sergio Flores, Isabel Servín
 */
public class SDbShipment extends SDbRegistryUser{
    
    public static final int WEB_KEY_LENGTH = 10;
    public static final String MSG_EVIDENCES = "El registro tiene evidencias.";
    public static final String MSG_STATUS = "El estatus actual del registro no es el adecuado.";
    
    protected int mnPkShipmentId;
    protected int mnNumber;
    protected Date mtShiptmentDate;
    protected String msDriverName;
    protected String msDriverPhone;
    protected String msVehiclePlate;
    protected String msTrailerPlate;
    protected String msWebKey;
    protected double mdMeters2;
    protected double mdKilograms;
    protected String msComments;
    protected int mnTicketId;
    protected boolean mbAnnulled;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkShipmentStatusId;
    protected int mnFkShipmentTypeId;
    protected int mnFkCargoTypeId;
    protected int mnFkHandlingTypeId;
    protected int mnFkVehicleTypeId;
    protected int mnFkShipperId;
    protected int mnFkForkliftDriverId;
    protected int mnFkCrewId;
    protected int mnFkUserTareId;
    protected int mnFkUserReleaseId;
    protected int mnFkUserAcceptId;
    protected int mnFkUserAnnulId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserTare;
    protected Date mtTsUserRelease;
    protected Date mtTsUserAccept;
    protected Date mtTsUserAnnul;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbShipmentRow> maChildRows;
    
    protected boolean mbOriginalTared;
    protected boolean mbOriginalAnnulled;
    protected int mnOriginalFkShipmentStatusId;
    
    protected boolean mbAuxSendMail;
    
    protected String _TOKEN;
    
    SBolShipment moBolShip;
    
    public SDbShipment () {
        super(SModConsts.S_SHIPT);
    }
    
    /*
     * Private methods
     */
    
    private void computeWebKey() {
        msWebKey = SLibUtils.generateRandomKey(WEB_KEY_LENGTH);
    }
    
    private void computeNextNumber(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;
        mnNumber = 0;

        msSql = "SELECT COALESCE(MAX(number), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnNumber = resultSet.getInt(1);
        }
    }
    
    private void computeTotals() {
        mdMeters2 = 0;
        mdKilograms = 0;
        
        for (SDbShipmentRow child : maChildRows) {
            mdMeters2 += child.getMeters2();
            mdKilograms += child.getKilograms();
        }
    }
    
      
    private boolean checkChangedAnnulled() {
        return mbAnnulled != mbOriginalAnnulled;
    }
        
    private boolean checkChangedShipmentStatus() {
        return mnFkShipmentStatusId != mnOriginalFkShipmentStatusId;
    }
    
    private int countEvidences(final SGuiSession session) throws Exception {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.S_EVIDENCE) + " WHERE fk_ship_ship = " + mnPkShipmentId + " ";
        Statement statement = session.getStatement().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        return count;
    }
        
    /*
     * Public methods
     */
    
    public void setPkShipmentId(int n) { mnPkShipmentId = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setShiptmentDate(Date t) { mtShiptmentDate = t; }
    public void setDriverName(String s) { msDriverName = s; }
    public void setDriverPhone(String s) { msDriverPhone = s; }
    public void setVehiclePlate(String s) { msVehiclePlate = s; }
    public void setTrailerPlate(String s) { msTrailerPlate = s; }
    public void setWebKey(String s) { msWebKey = s; }
    public void setMeters2(double d) { mdMeters2 = d; }
    public void setKilograms(double d) { mdKilograms = d; }
    public void setComments(String s) { msComments = s; }
    public void setTicketId(int n) { mnTicketId = n; }
    public void setAnnulled(boolean b) { mbAnnulled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkShipmentStatusId(int n) { mnFkShipmentStatusId = n; }
    public void setFkShipmentTypeId(int n) { mnFkShipmentTypeId = n; }
    public void setFkCargoTypeId(int n) { mnFkCargoTypeId = n; }
    public void setFkHandlingTypeId(int n) { mnFkHandlingTypeId = n; }
    public void setFkVehicleTypeId(int n) { mnFkVehicleTypeId = n; }
    public void setFkShipperId(int n) { mnFkShipperId = n; }
    public void setFkForkliftDriverId(int n) { mnFkForkliftDriverId = n; }
    public void setFkCrewId(int n) { mnFkCrewId = n; }
    public void setFkUserTareId(int n) { mnFkUserTareId = n; }
    public void setFkUserReleaseId(int n) { mnFkUserReleaseId = n; }
    public void setFkUserAcceptId(int n) { mnFkUserAcceptId = n; }
    public void setFkUserAnnulId(int n) { mnFkUserAnnulId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserTare(Date t) { mtTsUserTare = t; }
    public void setTsUserRelease(Date t) { mtTsUserRelease = t; }
    public void setTsUserAccept(Date t) { mtTsUserAccept = t; }
    public void setTsUserAnnul(Date t) { mtTsUserAnnul = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    public void setAuxSendMail(boolean b) { mbAuxSendMail = b; }

    public int getPkShipmentId() { return mnPkShipmentId; }
    public int getNumber() { return mnNumber; }
    public Date getShiptmentDate() { return mtShiptmentDate; }
    public String getDriverName() { return msDriverName; }
    public String getDriverPhone() { return msDriverPhone; }
    public String getVehiclePlate() { return msVehiclePlate; }
    public String getTrailerPlate() { return msTrailerPlate; }
    public String getWebKey() { return msWebKey; }
    public double getMeters2() { return mdMeters2; }
    public double getKilograms() { return mdKilograms; }
    public String getComments() { return msComments; }
    public int getTicketId() { return mnTicketId; }
    public boolean isAnnulled() { return mbAnnulled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkShipmentStatusId() { return mnFkShipmentStatusId; }
    public int getFkShipmentTypeId() { return mnFkShipmentTypeId; }
    public int getFkCargoTypeId() { return mnFkCargoTypeId; }
    public int getFkHandlingTypeId() { return mnFkHandlingTypeId; }
    public int getFkVehicleTypeId() { return mnFkVehicleTypeId; }
    public int getFkShipperId() { return mnFkShipperId; }
    public int getFkForkliftDriverId() { return mnFkForkliftDriverId; }
    public int getFkCrewId() { return mnFkCrewId; }
    public int getFkUserTareId() { return mnFkUserTareId; }
    public int getFkUserReleaseId() { return mnFkUserReleaseId; }
    public int getFkUserAcceptId() { return mnFkUserAcceptId; }
    public int getFkUserAnnulId() { return mnFkUserAnnulId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserTare() { return mtTsUserTare; }
    public Date getTsUserRelease() { return mtTsUserRelease; }
    public Date getTsUserAccept() { return mtTsUserAccept; }
    public Date getTsUserAnnul() { return mtTsUserAnnul; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    public boolean getAuxSendMail() { return mbAuxSendMail; }
    
    public ArrayList<SDbShipmentRow> getChildRows() { return maChildRows; }
    
    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkShipmentId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkShipmentId };
    }

    @Override
    public void initRegistry() {
        
        initBaseRegistry();

        mnPkShipmentId = 0;
        mnNumber = 0;
        mtShiptmentDate = null;
        msDriverName = "";
        msDriverPhone = "";
        msVehiclePlate = "";
        msTrailerPlate = "";
        msWebKey = "";
        mdMeters2 = 0;
        mdKilograms = 0;
        msComments = "";
        mnTicketId = 0;
        mbAnnulled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkShipmentStatusId = 0;
        mnFkShipmentTypeId = 0;
        mnFkCargoTypeId = 0;
        mnFkHandlingTypeId = 0;
        mnFkVehicleTypeId = 0;
        mnFkShipperId = 0;
        mnFkForkliftDriverId = 0;
        mnFkCrewId = 0;
        mnFkUserTareId = 0;
        mnFkUserReleaseId = 0;
        mnFkUserAcceptId = 0;
        mnFkUserAnnulId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserTare = null;
        mtTsUserRelease = null;
        mtTsUserAccept = null;
        mtTsUserAnnul = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildRows = new ArrayList<>();
        
        mbOriginalTared = false;
        mbOriginalAnnulled = false;
        mnOriginalFkShipmentStatusId = 0;
        mbAuxSendMail = false;
        
        moBolShip = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_shipt = " + mnPkShipmentId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_shipt = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkShipmentId = 0;

        msSql = "SELECT COALESCE(MAX(id_shipt), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkShipmentId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement;
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
            mnPkShipmentId = resultSet.getInt("id_shipt");
            mnNumber = resultSet.getInt("number");
            mtShiptmentDate = resultSet.getDate("shipt_date");
            msDriverName = resultSet.getString("driver_name");
            msDriverPhone = resultSet.getString("driver_phone");
            msVehiclePlate = resultSet.getString("vehic_plate");
            msTrailerPlate = resultSet.getString("trailer_plate");
            msWebKey = resultSet.getString("web_key");
            mdMeters2 = resultSet.getDouble("m2");
            mdKilograms = resultSet.getDouble("kg");
            msComments = resultSet.getString("comments");
            mnTicketId = resultSet.getInt("ticket_id");
            mbAnnulled = mbOriginalAnnulled = resultSet.getBoolean("b_ann"); // preserve original value
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkShipmentStatusId = mnOriginalFkShipmentStatusId = resultSet.getInt("fk_shipt_st"); // preserve original value
            mnFkShipmentTypeId = resultSet.getInt("fk_shipt_tp");
            mnFkCargoTypeId = resultSet.getInt("fk_cargo_tp");
            mnFkHandlingTypeId = resultSet.getInt("fk_handg_tp");
            mnFkVehicleTypeId = resultSet.getInt("fk_vehic_tp");
            mnFkShipperId = resultSet.getInt("fk_shipper");
            mnFkForkliftDriverId = resultSet.getInt("fk_forklift_drv");
            mnFkCrewId = resultSet.getInt("fk_crew");
            mnFkUserTareId = resultSet.getInt("fk_usr_tare");
            mnFkUserReleaseId = resultSet.getInt("fk_usr_release");
            mnFkUserAcceptId = resultSet.getInt("fk_usr_accept");
            mnFkUserAnnulId = resultSet.getInt("fk_usr_ann");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserTare = resultSet.getTimestamp("ts_usr_tare");
            mtTsUserRelease = resultSet.getTimestamp("ts_usr_release");
            mtTsUserAccept = resultSet.getTimestamp("ts_usr_accept");
            mtTsUserAnnul = resultSet.getTimestamp("ts_usr_ann");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            statement = session.getStatement().getConnection().createStatement();
            
            msSql = "SELECT id_row FROM " + SModConsts.TablesMap.get(SModConsts.S_SHIPT_ROW) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                maChildRows.add((SDbShipmentRow) session.readRegistry(SModConsts.S_SHIPT_ROW, new int[] { mnPkShipmentId, resultSet.getInt(1) }));
            }

            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {       
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        computeTotals();
        
        // preserve user that changed current shipment status:
        if (checkChangedShipmentStatus()) {
            switch (mnFkShipmentStatusId) {
                case SModSysConsts.SS_SHIPT_ST_REL_TO:
                    break;
                case SModSysConsts.SS_SHIPT_ST_REL:
                    mnFkUserReleaseId = session.getUser().getPkUserId();
                    break;
                case SModSysConsts.SS_SHIPT_ST_ACC_TO:
                    break;
                case SModSysConsts.SS_SHIPT_ST_ACC:
                    mnFkUserAcceptId = session.getUser().getPkUserId();
                    break;
                default:
            }
        }
        
        // preserve user that changed current annulled status:
        if (mbAnnulled && checkChangedAnnulled()) {
            mnFkUserAnnulId = session.getUser().getPkUserId();
        }
        
        // set non-applicable user where required:
        if (mnFkUserTareId == SLibConsts.UNDEFINED) {
            mnFkUserTareId = SUtilConsts.USR_NA_ID;
        }
        if (mnFkUserReleaseId == SLibConsts.UNDEFINED) {
            mnFkUserReleaseId = SUtilConsts.USR_NA_ID;
        }
        if (mnFkUserAcceptId == SLibConsts.UNDEFINED) {
            mnFkUserAcceptId = SUtilConsts.USR_NA_ID;
        }
        if (mnFkUserAnnulId == SLibConsts.UNDEFINED) {
            mnFkUserAnnulId = SUtilConsts.USR_NA_ID;
        }
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            computeNextNumber(session);
            computeWebKey();
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                mnPkShipmentId + ", " + 
                mnNumber + ", " + 
                "'" + SLibUtils.DbmsDateFormatDate.format(mtShiptmentDate) + "', " + 
                "'" + msDriverName + "', " + 
                "'" + msDriverPhone + "', " + 
                "'" + msVehiclePlate + "', " + 
                "'" + msTrailerPlate + "', " + 
                "'" + msWebKey + "', " + 
                SLibUtils.round(mdMeters2, SLibUtils.getDecimalFormatQuantity().getMaximumFractionDigits()) + ", " + 
                SLibUtils.round(mdKilograms, SLibUtils.getDecimalFormatQuantity().getMaximumFractionDigits()) + ", " + 
                "'" + msComments + "', " + 
                mnTicketId + ", " + 
                (mbAnnulled ? 1 : 0) + ", " + 
                (mbDeleted ? 1 : 0) + ", " + 
                (mbSystem ? 1 : 0) + ", " + 
                mnFkShipmentStatusId + ", " + 
                mnFkShipmentTypeId + ", " + 
                mnFkCargoTypeId + ", " + 
                mnFkHandlingTypeId + ", " + 
                mnFkVehicleTypeId + ", " + 
                mnFkShipperId + ", " +
                (mnFkForkliftDriverId == 0 ? "NULL, " : mnFkForkliftDriverId + ", ") + 
                (mnFkCrewId == 0 ? "NULL, " : mnFkCrewId + ", ") + 
                mnFkUserTareId + ", " + 
                mnFkUserReleaseId + ", " + 
                mnFkUserAcceptId + ", " + 
                mnFkUserAnnulId + ", " + 
                mnFkUserInsertId + ", " + 
                mnFkUserUpdateId + ", " +
                "NOW()" + ", " +
                "NOW()" + ", " + 
                "NOW()" + ", " + 
                "NOW()" + ", " + 
                "NOW()" + ", " + 
                "NOW()" + " " + 
                 ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            mnFkUserReleaseId = (mnFkShipmentStatusId == SModSysConsts.SS_SHIPT_ST_REL ? session.getUser().getPkUserId() : SUtilConsts.USR_NA_ID);
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                //"id_shipt = " + mnPkShipmentId + ", " +
                "number = " + mnNumber + ", " +
                "shipt_date = '" + SLibUtils.DbmsDateFormatDate.format(mtShiptmentDate) + "', " +
                "driver_name = '" + msDriverName + "', " +
                "driver_phone = '" + msDriverPhone + "', " +
                "vehic_plate = '" + msVehiclePlate + "', " +
                "trailer_plate = '" + msTrailerPlate + "', " +
                "web_key = '" + msWebKey + "', " +
                "m2 = " + SLibUtils.round(mdMeters2, SLibUtils.getDecimalFormatQuantity().getMaximumFractionDigits()) + ", " +
                "kg = " + SLibUtils.round(mdKilograms, SLibUtils.getDecimalFormatQuantity().getMaximumFractionDigits()) + ", " +   
                "comments = '" + msComments + "', " +
                "ticket_id = " + mnTicketId + ", " +
                "b_ann = " + (mbAnnulled ? 1 : 0) + ", " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                "fk_shipt_st = " + mnFkShipmentStatusId + ", " +
                "fk_shipt_tp = " + mnFkShipmentTypeId + ", " +
                "fk_cargo_tp = " + mnFkCargoTypeId + ", " +
                "fk_handg_tp = " + mnFkHandlingTypeId + ", " +
                "fk_vehic_tp = " + mnFkVehicleTypeId + ", " +
                "fk_shipper = " + mnFkShipperId + ", " +
                "fk_forklift_drv = " + mnFkForkliftDriverId + ", " +
                "fk_crew = " + mnFkCrewId + ", " +
                "fk_usr_tare = " + mnFkUserTareId + ", " +
                "fk_usr_release = " + mnFkUserReleaseId + ", " +
                "fk_usr_accept = " + mnFkUserAcceptId + ", " +
                "fk_usr_ann = " + mnFkUserAnnulId + ", " +
                //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                (mnFkShipmentStatusId == SModSysConsts.SS_SHIPT_ST_REL && checkChangedShipmentStatus() ? "ts_usr_release = NOW(), " : "") +
                (mnFkShipmentStatusId == SModSysConsts.SS_SHIPT_ST_ACC && checkChangedShipmentStatus() ? "ts_usr_accept = NOW(), " : "") +
                (mbAnnulled && checkChangedAnnulled() ? "ts_usr_ann = NOW(), " : "") +
                //"ts_usr_ins = " + "NOW()" + ", " +
                "ts_usr_upd = " + "NOW()" + " " +
                 getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.S_SHIPT_ROW) + " " + getSqlWhere();
        session.getStatement().execute(msSql);
        
        for (SDbShipmentRow child : maChildRows) {
            child.setPkShipmentId(mnPkShipmentId);
            child.setRegistryNew(true); // force treatment of row as new, consider that it is actually new
            child.save(session);
            
            // if new destination has just been created, propagate its ID to other rows to prevent multiple creation of the same destination:
            if (child.isAuxDestinationCreated()) {
                for (SDbShipmentRow childAux : maChildRows) {
                    if (childAux.isRegistryNew() && childAux.getFkDestinationId() == SLibConsts.UNDEFINED && childAux.getAuxSiteLocationId() == child.getAuxSiteLocationId()) {
                        childAux.setFkDestinationId(child.getFkDestinationId());
                    }
                }
            }
        }

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
        
        collectShipmentInfo(session);
        sendMail(session);
        sendJson(session);
    }

    @Override
    public SDbShipment clone() throws CloneNotSupportedException {
        SDbShipment  registry = new SDbShipment();
        
        registry.setPkShipmentId(this.getPkShipmentId());
        registry.setNumber(this.getNumber());
        registry.setShiptmentDate(this.getShiptmentDate());
        registry.setDriverName(this.getDriverName());
        registry.setDriverPhone(this.getDriverPhone());
        registry.setVehiclePlate(this.getVehiclePlate());
        registry.setTrailerPlate(this.getTrailerPlate());
        registry.setWebKey(this.getWebKey());
        registry.setMeters2(this.getMeters2());
        registry.setKilograms(this.getKilograms());
        registry.setComments(this.getComments());
        registry.setTicketId(this.getTicketId());
        registry.setAnnulled(this.isAnnulled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkShipmentStatusId(this.getFkShipmentStatusId());
        registry.setFkShipmentTypeId(this.getFkShipmentTypeId());
        registry.setFkCargoTypeId(this.getFkCargoTypeId());
        registry.setFkHandlingTypeId(this.getFkHandlingTypeId());
        registry.setFkVehicleTypeId(this.getFkVehicleTypeId());
        registry.setFkShipperId(this.getFkShipperId());
        registry.setFkForkliftDriverId(this.getFkForkliftDriverId());
        registry.setFkCrewId(this.getFkCrewId());
        registry.setFkUserTareId(this.getFkUserTareId());
        registry.setFkUserReleaseId(this.getFkUserReleaseId());
        registry.setFkUserAcceptId(this.getFkUserAcceptId());
        registry.setFkUserAnnulId(this.getFkUserAnnulId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserTare(this.getTsUserTare());
        registry.setTsUserRelease(this.getTsUserRelease());
        registry.setTsUserAccept(this.getTsUserAccept());
        registry.setTsUserAnnul(this.getTsUserAnnul());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        registry.setAuxSendMail(this.getAuxSendMail());
        
        for (SDbShipmentRow child : maChildRows) {
            registry.getChildRows().add(child.clone());
        }
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    @Override
    public boolean canSave(final SGuiSession session) throws SQLException, Exception {
        boolean canSave = super.canSave(session);
        
        if (canSave) {
            if (countEvidences(session) > 0) {
                throw new Exception(MSG_EVIDENCES);
            }
        }
        
        return canSave;
    }

    @Override
    public boolean canDisable(final SGuiSession session) throws SQLException, Exception {
        boolean canDisable = super.canDisable(session);
        
        if (canDisable) {
            if (!mbAnnulled) {
                if (countEvidences(session) > 0) {
                    throw new Exception(MSG_EVIDENCES);
                }
                else if (mnFkShipmentStatusId != SModSysConsts.SS_SHIPT_ST_REL_TO) {
                    throw new Exception(MSG_STATUS);
                }
            }
        }
        
        return canDisable;
    }

    @Override
    public boolean canDelete(final SGuiSession session) throws SQLException, Exception {
        boolean canDelete = super.canDelete(session);
        
        if (canDelete) {
            if (!mbDeleted) {
                if (countEvidences(session) > 0) {
                    throw new Exception(MSG_EVIDENCES);
                }
                else if (mnFkShipmentStatusId != SModSysConsts.SS_SHIPT_ST_REL_TO) {
                    throw new Exception(MSG_STATUS);
                }
            }
        }
        
        return canDelete;
    }

    @Override
    public void disable(final SGuiSession session) throws SQLException, Exception {
        mbAnnulled = !mbAnnulled;
        save(session);
    }
    
    private Statement getStatementSiie(SGuiSession session) throws Exception {
        SDbConfig config = (SDbConfig) session.getConfigSystem();
        Connection connectionSiie = SEtlProcess.createConnection(
                SEtlConsts.DB_MYSQL, 
                config.getSiieHost(), 
                config.getSiiePort(), 
                config.getSiieName(), 
                config.getSiieUser(), 
                config.getSiiePassword());
        
        /* Isabel Servín 30/03/2022: Conexion para pruebas desde localhost a siie de cartró */
//        Connection connectionSiie = SEtlProcess.createConnection(
//                SEtlConsts.DB_MYSQL, 
//                "10.83.32.129", 
//                3306, 
//                "erp_cartro", 
//                "root", 
//                "msroot");
        
        return connectionSiie.createStatement();
    }
    
    private int loginCPT(SDbConfigSms confSms) {
        try {
            URLConnection connection = new URL(confSms.getWebUrl() + "login").openConnection();
            
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = "{ \"email\": \"" + confSms.getWebUser() + "\",\"password\": \"" + confSms.getWebPassword() + "\" }";

            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);			
            }
            
            InputStream response = connection.getInputStream();
            
            try (Scanner scanner = new Scanner(response)) {
                String responseBody = scanner.useDelimiter("\\A").next();
                
                ObjectMapper mapper = new ObjectMapper();
                
                CPTLoginResponse capResponse = mapper.readValue(responseBody, CPTLoginResponse.class);
                
                this._TOKEN = capResponse.getAccess_token();
                
                return 200;
            }
        }
        catch (Exception e) {
            System.err.println(e.toString());
            return - 1;
        }
        
    }
    
    public void sendJson(SGuiSession session) {
        try {
            SDbConfigSms confSms = new SDbConfigSms();
            confSms.read(session, new int[] { 1 });
            SDbShipper shipper = (SDbShipper) session.readRegistry(SModConsts.SU_SHIPPER, new int[] { mnFkShipperId });
            if (shipper.isWeb()) {
                if (loginCPT(confSms) == 200) {
                    String url = confSms.getWebUrl() + "requestdocument";
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                    con.setRequestProperty("Accept", "application/json");
                    con.setRequestProperty("Authorization", "Bearer " + this._TOKEN);
                    con.setRequestProperty("User-Agent", "Java client");
                    String parameters = moBolShip.encodeJson();

                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(parameters);
                    wr.flush();
                    wr.close();

                    int responseCode = con.getResponseCode();
                    System.out.println("\nSending 'POST' request to URL : " + url);
                    System.out.println("Post parameters : " + parameters);
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Imprime el resultado
                    System.out.println(response.toString());
                    JSONParser parser = new JSONParser();
                    long code = 0;
                    String message = "";
                    
                    try {
                        Object responseObj = parser.parse(response.toString());

                        JSONObject jsonObject = (JSONObject) responseObj;
//                        System.out.println(jsonObject);

                        code = (long) jsonObject.get("code");
                        message = (String) jsonObject.get("message");
                    } 
                    catch (Exception e) {
                        System.err.println(e.toString());
                    }

                    SDbShipmentLog log = new SDbShipmentLog();
                    log.setFileType(2); // JSON
                    log.setCode(code == 0 ? responseCode + "" : code + "");
                    log.setMessage(message);
                    log.setResponse(response.toString());
                    log.setFkShipmentId(mnPkShipmentId);
                    log.save(session);
                }
            } 
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    
    private String composeMailBody(SGuiSession session, SDbShipper shipper) {
        String mailBody = "";
        
        try {
            SDbVehicleType vehicleType = (SDbVehicleType) session.readRegistry(SModConsts.SU_VEHIC_TP, new int[] { mnFkVehicleTypeId });
            
            mailBody = "Embarque: #" + mnNumber + "; " + SLibUtils.DateFormatDate.format(mtShiptmentDate) + "\n" +
                    "Transportista: " + shipper.getName() + "; " + shipper.getCode() + "\n" + 
                    "No. proveedor: " + moBolShip.getProviderCode() + "\n" + 
                    "Vehiculo: " + vehicleType.getName() + "; " + msVehiclePlate + "\n" + 
                    (msTrailerPlate.isEmpty() ? "" :"Remolque: " + msTrailerPlate + "\n") +
                    "Chofer: " + msDriverName + "; " + (msDriverPhone.isEmpty() || msDriverPhone.equals("0") ? "(SIN TELEFONO)" : msDriverPhone) +"\n" +
                    "Boleto bascula: #" + mnTicketId + "\n";
             
            int i = 0;
            for (SBolLocations location : moBolShip.getLocations()) {
                i++;
                mailBody += "\n\nUBICACION " + i + ":\n";
                
                mailBody += "ID Destino (sugerido): " + location.getLocationId() + "\n- Destinatario. Nombre: " + location.getNameFiscalId() + "; " +
                        "RFC: " + (location.getFiscalId().isEmpty() ? "(SIN RFC)" : location.getFiscalId()) + "\n" + 
                        "- Localidad. Clave SAT: " + (location.getLocalityCode().isEmpty() ? "(NO APLICA)" : location.getLocalityCode() + "; Nombre: " + location.getLocality()) + "\n" +
                        "- Municipio. Clave SAT: " + (location.getCountyCode().isEmpty() ? "(NO APLICA)" : location.getCountyCode() + "; Nombre: " + location.getCounty()) + "\n" +
                        "- Estado. Clave SAT: " + (location.getStateCode().isEmpty() ? "(NO APLICA)" : location.getStateCode()) + "\n" +
                        "- Pais. Clave SAT: " + (location.getCountryCode().isEmpty() ? "(SIN INFORMACION)" : location.getCountryCode()) + "\n" + 
                        "- Código postal: " + location.getZipCode() + "\n";
                
                if (location.getNeighborhoodsMap().size() == 1) {
                    for(Map.Entry<String, String> neighborhood : location.getNeighborhoodsMap().entrySet()) {
                        mailBody += "- Colonia. Clave SAT: " + neighborhood.getKey() + "; Nombre: " + neighborhood.getValue() + "\n" ;
                    }
                }
                else if (location.getNeighborhoodsMap().size() > 1) {
                    mailBody += "- Colonia. Se encontraron " + location.getNeighborhoodsMap().size() + " colonias para el codigo postal " + location.getZipCode() + " (Clave SAT - Descripcion): \n";
                    int j = 0;
                    for(Map.Entry<String, String> neighborhood : location.getNeighborhoodsMap().entrySet()) {
                        j++;
                        mailBody += j + ") " + neighborhood.getKey() + " - " + neighborhood.getValue() + (j != location.getNeighborhoodsMap().size() ? "; " : "");
                    }
                    mailBody += "\n";
                }
                mailBody += "- Orden(es) embarque: " + location.getShiptFolios() + "\n"; 
                mailBody += "\n";
                int j = 0;
                for (SBolMerchandises merchandise : location.getMerchandises()) {
                    j++;
                    mailBody += "- Producto " + j + ". Clave SAT " + merchandise.getBienesTransp() + 
                            "; Descripcion SAT: " + merchandise.getSatDescription() + "; Descripcion factura: " + merchandise.getInvDescription() + "\nPeso: " + SLibUtils.getDecimalFormatAmount().format(SLibUtils.round(merchandise.getWeight(), 3)) + " kg \n";
                    mailBody += "- Total valor mercancia: $" + SLibUtils.getDecimalFormatAmount().format(SLibUtils.round(merchandise.getValue(), 2)) + " " + merchandise.getCurrency() + "\n"; 
                }
            }

            mailBody += "Embalaje (sugerido aunque no requerido). Clave SAT: Z01; Descripcion SAT: No requerido\n\n";
            
            mailBody += "-------------------------------------------------------------------------------------\n" +
                "Favor de no responder este mail, fue generado de forma automática.\n" +
               SGuiMain.APP_NAME + " " + SGuiMain.APP_COPYRIGHT + "\n" +
                SGuiMain.APP_PROVIDER + "\n" +
                SGuiMain.APP_RELEASE;
            mailBody = mailBody.replace("©", "(c)");
            
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
        
        return mailBody;
    }
    
    public void sendMail(SGuiSession session) {
        if (mbAuxSendMail) {
            try {
                SDbShipper shipper = (SDbShipper) session.readRegistry(SModConsts.SU_SHIPPER, new int[] { mnFkShipperId });
                String mail = shipper.getMail().toLowerCase();

                // Generar asunto del correo-e:

                String mailSubject = "[Cartro] Información embarque #" + mnNumber + " " + SLibUtils.DateFormatDate.format(mtShiptmentDate);
                String mailTitle = "";
                mailSubject += mailTitle;

                // Generar cuerpo del correo-e en formato HTML:

                String mailBody = composeMailBody(session, shipper);

                // Preparar destinatarios del correo-e:

                ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mail, ";")));
//                ArrayList<String> recipientsBcc = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode("sflores@swaplicado.com.mx", ";")));

                 // Leer configuración de ETLA:
                SDbConfig configEtla = new SDbConfig();
                configEtla.read(session, new int[] { 1 });
                String mailHost = configEtla.getMailHost();
                String mailPort = configEtla.getMailPort();
                String mailProtocol = configEtla.getMailProtocol();
                boolean mailStartTls = configEtla.isMailStartTls();
                boolean mailAuth = configEtla.isMailAuth();
                String mailUser = configEtla.getMailUser();
                String mailPassword = configEtla.getMailPassword();

                // Crear y enviar correo-e:

                //SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "som@aeth.mx", "AETHSOM", "som@aeth.mx");
                SMailSender sender = new SMailSender(mailHost, mailPort, mailProtocol, mailStartTls, mailAuth, mailUser, mailPassword, mailUser);

                SMail mailSender = new SMail(sender, SMailUtils.encodeSubjectUtf8(SLibUtils.textToAscii(mailSubject)), SLibUtils.textToAscii(mailBody), recipientsTo);
//                mailSender.getBccRecipients().addAll(recipientsBcc);
                mailSender.setContentType(SMailConsts.CONT_TP_TEXT_PLAIN);
                mailSender.send();

                System.out.println("Mail send!");
                
                SDbShipmentLog log = new SDbShipmentLog();
                log.setFileType(1); // mail
                log.setMail(mail);
                log.setFkShipmentId(mnPkShipmentId);
                log.save(session);
                
            }
            catch (Exception e) {
                SLibUtils.printException(this, e);
            }
        }
    }
    
    private ArrayList<SBolMerchandises> getChildMerchandises (SGuiSession session, SDbShipmentRow child) throws Exception {
        // Datos de los ítems:
        Statement statement = session.getDatabase().getConnection().createStatement();
        ArrayList<SBolMerchandises> bolMerchs;
        Statement statementSiie = getStatementSiie(session);
        SDataDps dps = new SDataDps();
        dps.read(new int[] { child.getInvoiceIdYear(), child.getInvoiceIdDoc()}, statementSiie);
        Vector<SDataDpsEntry> entries = dps.getDbmsDpsEntries();
        bolMerchs = new ArrayList<>();
        for (SDataDpsEntry entry : entries) {
            String sql = "SELECT i.name, icfd.code AS item_code, igencfd.code AS igen_code, igencfd.name AS sat_name FROM erp.itmu_item AS i " +
                    "INNER JOIN erp.itmu_igen AS igen ON i.fid_igen = igen.id_igen " +
                    "INNER JOIN erp.itms_cfd_prod_serv AS igencfd ON igencfd.id_cfd_prod_serv = igen.fid_cfd_prod_serv " +
                    "LEFT OUTER JOIN erp.itms_cfd_prod_serv AS icfd ON icfd.id_cfd_prod_serv = i.fid_cfd_prod_serv_n " +
                    "WHERE id_item = " + entry.getFkItemId();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                SBolMerchandises merch = new SBolMerchandises();
                merch.setBienesTransp((resultSet.getString("item_code") == null ? resultSet.getString("igen_code") : resultSet.getString("item_code")));
                merch.setSatDescription(resultSet.getString("sat_name"));
                merch.setInvDescription(resultSet.getString("name"));
                merch.setQuantity(entry.getMass());
                merch.setUnitCode("KGM");
                merch.setWeight(entry.getMass());
                merch.setValue(entry.getSubtotalCy_r());
                merch.setCurrency("MXN");
                bolMerchs.add(merch);
            }
        }
        return bolMerchs;
    } 
    
    private void processChildRows(SGuiSession session) throws Exception {
        ArrayList<SBolLocations> locs = new ArrayList<>();
        
        if (!maChildRows.isEmpty()) {
            for (SDbShipmentRow child : maChildRows) {
                // Datos de ubicaciones y destinatarios:

                boolean newLocation = true;
                String stateCode = "";
                String countyCode = "";
                String localityCode = "";
                String countryCode;
                String countyName = "";
                String localityName= "";

                String locationId = "DE" + String.format("%06d", child.getFkDestinationId());
                for (SBolLocations bolLoc : locs) {
                    if (bolLoc.getLocationId().equals(locationId)) {
                        ArrayList<SBolMerchandises> bolMerchs = getChildMerchandises(session, child);
                        for (SBolMerchandises bolMerch : bolMerchs) {
                            bolLoc.getMerchandises().add(bolMerch);
                    }
                        
                        boolean newFolio = true;
                        String folios[] = bolLoc.getShiptFolios().split(", ");
                        for (String folio : folios) {
                            if (folio.equals(child.getShipmentOrderFolio() + "")) {
                                newFolio = false;
                                break;
                }
                        }

                        if (newFolio) {
                            bolLoc.setShiptFolios(bolLoc.getShiptFolios() + ", " + child.getShipmentOrderFolio());
                        }
                        
                        newLocation = false;
                        break;
                    }
                }

                if (newLocation) {
                    String zipCode = child.getDbmsDestinationZip();
                    if (zipCode.isEmpty()) {
                        zipCode = child.getDbmsCustomerZip();
                    }

                    String sql = "SELECT * FROM erp.locs_bol_zip_code WHERE id_zip_code = '" + zipCode + "' AND NOT b_del;";
                    Statement statement = session.getDatabase().getConnection().createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    if (resultSet.next()) {
                        stateCode = resultSet.getString("id_sta_code");
                        countyCode = resultSet.getString("county_code");
                        localityCode = resultSet.getString("locality_code");
                    }

                    if (!countyCode.isEmpty()) {
                        countyName = SModDataUtils.getLocCatalogNameByCode(session, SModConsts.LOCS_BOL_COUNTY, countyCode, stateCode);
                    }
                    if (!localityCode.isEmpty()) {
                        localityName = SModDataUtils.getLocCatalogNameByCode(session, SModConsts.LOCS_BOL_LOCALITY, localityCode, stateCode);
                    }
                    if (zipCode.isEmpty()) {
                        zipCode = "00000";
                    }

                    switch (child.getDbmsCountry()) {
                        case "MX" : countryCode = "MEX"; break;
                        case "US" : countryCode = "USA"; break;
                        case "CA" : countryCode = "CAN"; break;
                        default : countryCode = ""; break;
                    }

                    sql = "SELECT nei_code, description FROM erp.locs_bol_nei_zip_code WHERE zip_code = '" + zipCode + "';";
                    resultSet = statement.executeQuery(sql);
                    HashMap<String, String> neighborhoods = new HashMap<>();
                    while (resultSet.next()) {
                        neighborhoods.put(resultSet.getString(1), resultSet.getString(2)); 
                    }

                    SBolLocations loc = new SBolLocations();
                    loc.setFreight(child.getPkRowId() == 1);
                    loc.setLocationType("Destino");
                    loc.setLocationId(locationId);
                    loc.setShiptFolios(child.getShipmentOrderFolio() + "");
                    loc.setFiscalId((child.getDbmsCustomerTaxId()));
                    loc.setNameFiscalId(child.getDbmsCustomer());
                    loc.setLocalityCode(localityCode);
                    loc.setLocality(localityName);
                    loc.setCountyCode(countyCode);
                    loc.setCounty(countyName);
                    loc.setStateCode(stateCode);
                    loc.setCountryCode(countryCode);
                    loc.setZipCode(zipCode);
                    loc.setNeighborhoodsMap(neighborhoods);

                    String neiborhoodsKeys = "";

                    int j = 0;
                    for(Map.Entry<String, String> neighborhood : neighborhoods.entrySet()) {
                        j++;
                        neiborhoodsKeys += neighborhood.getKey() + (j != neighborhoods.size() ? ", " : " ");
                    }

                    loc.setNeighborhood(neiborhoodsKeys);

                    loc.setMerchandises(getChildMerchandises(session, child));
                    locs.add(loc);
                }
            }
            if (moBolShip != null) {
                moBolShip.setLocations(locs);
            }
        }
    }
    
    public void collectShipmentInfo(SGuiSession session) {
        moBolShip = new SBolShipment();
        try {
            Statement statementSiie = getStatementSiie(session);
            
            SDbConfigSms confSms = new SDbConfigSms();
            confSms.read(session, new int[] { 1 });
            
            SDbShipper shipper = (SDbShipper) session.readRegistry(SModConsts.SU_SHIPPER, new int[] { mnFkShipperId });
            
            moBolShip.setShipmentId(mnNumber);
            moBolShip.setTicket(mnTicketId);
            moBolShip.setOrigLocId(confSms.getWebLocationId());
            moBolShip.setShipperFiscalId(shipper.getFiscalId());
            /* Isabel Servín 12/04/2022: RFC para pruebas */
            //moBolShip.setShipperFiscalId("MISC491214B86");
            moBolShip.setPlate(msVehiclePlate);
            moBolShip.setTrailerPlate(msTrailerPlate);
            moBolShip.setTotWei(mdKilograms);
            moBolShip.setCurrency("MXN");
            
            String sql = "SELECT bp_key FROM erp.bpsu_bp_ct WHERE id_bp = " + shipper.getDesSupplierId() + " AND id_ct_bp = 2";
            ResultSet resultSet = statementSiie.executeQuery(sql);
            if (resultSet.next()) {
                moBolShip.setProviderCode(resultSet.getString(1));
            }
            
            processChildRows(session);
            /* Isabel Servín 12/04/2022: Para pruebas, obtener el JSON antes de ser enviado */
//            moBolShip.encodeJson();
        }
        catch(Exception e) {
            SLibUtils.printException(this, e);
        }
    }
}
