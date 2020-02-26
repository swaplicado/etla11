/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.db;

import etla.mod.SModConsts;
import etla.mod.SModSysConsts;
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

    public int getPkShipmentId() { return mnPkShipmentId; }
    public int getNumber() { return mnNumber; }
    public Date getShiptmentDate() { return mtShiptmentDate; }
    public String getDriverName() { return msDriverName; }
    public String getDriverPhone() { return msDriverPhone; }
    public String getVehiclePlate() { return msVehiclePlate; }
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
}
