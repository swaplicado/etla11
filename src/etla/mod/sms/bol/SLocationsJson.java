/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.bol;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Isabel Servín
 */
public class SLocationsJson {
    
    protected String sLocationType;
    protected String sFiscalId;
    protected String sNameFiscalId;
    protected String sStreet;
    protected String sExtNumber;
    protected String sIntNumber;
    protected String sNeighborhood;
    protected String sLocality;
    protected String sReference;
    protected String sCounty;
    protected String sState;
    protected String sCountry;
    protected String sZipCode;
    
    protected ArrayList<SMerchandisesJson> maMerchandises;
    
    public SLocationsJson(){
        sLocationType = "";
        sFiscalId = "";
        sNameFiscalId = "";
        sStreet = "";
        sExtNumber = "";
        sIntNumber = "";
        sNeighborhood = "";
        sLocality = "";
        sReference = "";
        sCounty = "";
        sState = "";
        sCountry = "";
        sZipCode = "";
        
        maMerchandises = new ArrayList<>();
    }    
    public void setLocationType(String s) { sLocationType = s; }
    public void setFiscalId(String s) { sFiscalId = s; }
    public void setNameFiscalId(String s) { sNameFiscalId = s; }
    public void setStreet(String s) { sStreet = s; }
    public void setExtNumber(String s) { sExtNumber = s; }
    public void setIntNumber(String s) { sIntNumber = s; }
    public void setNeighborhood(String s) { sNeighborhood = s; }
    public void setLocality(String s) { sLocality = s; }
    public void setReference(String s) { sReference = s; }
    public void setCounty(String s) { sCounty = s; }
    public void setState(String s) { sState = s; }
    public void setCountry(String s) { sCountry = s; }
    public void setZipCode(String s) { sZipCode = s; }
    
    public void setMerchandises(ArrayList<SMerchandisesJson> o) { maMerchandises = o; }
    
    public JSONObject getJson() {
        JSONObject loc = new JSONObject();
        loc.put("tipoUbicacion", sLocationType);
        loc.put("rfcRemitenteDestinatario", sFiscalId);
        loc.put("nombreRFC", sNameFiscalId);
        
        JSONObject add = new JSONObject();
        add.put("calle", sStreet);
        add.put("numeroExterior", sExtNumber);
        add.put("numeroInterior", sIntNumber);
        add.put("colonia", sNeighborhood);
        add.put("referencia", sReference);
        add.put("localidad", sLocality);
        add.put("municipio", sCounty);
        add.put("estado", sState);
        add.put("pais", sCountry);
        add.put("codigoPostal", sZipCode);
        
        loc.put("direccion", add);
        
        JSONArray merch = new JSONArray();
        
        for (SMerchandisesJson merchandise : maMerchandises) {
            merch.add(merchandise.getJson());
        }
        
        loc.put("mercancias", merch);
        
        return loc;
    }
}
