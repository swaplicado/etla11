/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.bol;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Isabel Servín
 */
public class SBolLocations {
    
    protected String sLocationType;
    protected String sLocationId;
    protected String sShiptFolios;
    protected String sFiscalId;
    protected String sNameFiscalId;
    protected String sStreet;
    protected String sExtNumber;
    protected String sIntNumber;
    protected String sNeighborhood;
    protected String sLocalityCode;
    protected String sLocality;
    protected String sReference;
    protected String sCountyCode;
    protected String sCounty;
    protected String sStateCode;
    protected String sCountryCode;
    protected String sZipCode;
    
    protected HashMap<String, String> moNeighborhoodsMap;
    
    protected ArrayList<SBolMerchandises> maMerchandises;
    
    public SBolLocations(){
        sLocationType = "";
        sLocationId = "";
        sShiptFolios = "";
        sFiscalId = "";
        sNameFiscalId = "";
        sStreet = "";
        sExtNumber = "";
        sIntNumber = "";
        sNeighborhood = "";
        sLocalityCode = "";
        sLocality = "";
        sReference = "";
        sCountyCode = "";
        sCounty = "";
        sStateCode = "";
        sCountryCode = "";
        sZipCode = "";
        
        maMerchandises = new ArrayList<>();
    }    
    
    public void setLocationType(String s) { sLocationType = s; }
    public void setLocationId(String s) { sLocationId = s; }
    public void setShiptFolios(String s) { sShiptFolios = s; }
    public void setFiscalId(String s) { sFiscalId = s; }
    public void setNameFiscalId(String s) { sNameFiscalId = s; }
    public void setStreet(String s) { sStreet = s; }
    public void setExtNumber(String s) { sExtNumber = s; }
    public void setIntNumber(String s) { sIntNumber = s; }
    public void setNeighborhood(String s) { sNeighborhood = s; }
    public void setLocalityCode(String s) { sLocalityCode = s; }
    public void setLocality(String s) { sLocality = s; }
    public void setReference(String s) { sReference = s; }
    public void setCountyCode(String s) { sCountyCode = s; }
    public void setCounty(String s) { sCounty = s; }
    public void setStateCode(String s) { sStateCode = s; }
    public void setCountryCode(String s) { sCountryCode = s; }
    public void setZipCode(String s) { sZipCode = s; }
    
    public void setNeighborhoodsMap(HashMap<String, String> o) { moNeighborhoodsMap = o; }
    
    public void setMerchandises(ArrayList<SBolMerchandises> o) { maMerchandises = o; }
    
    public String getLocationType() { return sLocationType; }
    public String getLocationId() { return sLocationId; }
    public String getShiptFolios() { return sShiptFolios; }
    public String getFiscalId() { return sFiscalId; }
    public String getNameFiscalId() { return sNameFiscalId; }
    public String getStreet() { return sStreet; }
    public String getExtNumber() { return sExtNumber; }
    public String getIntNumber() { return sIntNumber; }
    public String getNeighborhood() { return sNeighborhood; }
    public String getLocalityCode() { return sLocalityCode; }
    public String getLocality() { return sLocality; }
    public String getReference() { return sReference; }
    public String getCountyCode() { return sCountyCode; }
    public String getCounty() { return sCounty; }
    public String getStateCode() { return sStateCode; }
    public String getCountryCode() { return sCountryCode; }
    public String getZipCode() { return sZipCode; }
    
    public HashMap<String, String> getNeighborhoodsMap() { return moNeighborhoodsMap; }
    
    public ArrayList<SBolMerchandises> getMerchandises() { return maMerchandises; }
    
    public JSONObject getJson() {
        JSONObject loc = new JSONObject();
        loc.put("tipoUbicacion", sLocationType);
        loc.put("rfcRemitenteDestinatario", sFiscalId);
        loc.put("nombreRFC", sNameFiscalId);
        loc.put("talones", sShiptFolios);
        
        JSONObject add = new JSONObject();
        add.put("calle", sStreet);
        add.put("numeroExterior", sExtNumber);
        add.put("numeroInterior", sIntNumber);
        add.put("colonia", sNeighborhood);
        add.put("referencia", sReference);
        add.put("localidad", sLocalityCode);
        add.put("municipio", sCountyCode);
        add.put("estado", sStateCode);
        add.put("pais", sCountryCode);
        add.put("codigoPostal", sZipCode);
        
        loc.put("domicilio", add);
        
        JSONArray merch = new JSONArray();
        
        for (SBolMerchandises merchandise : maMerchandises) {
            merch.add(merchandise.getJson());
        }
        
        loc.put("mercancias", merch);
        
        return loc;
    }
}
