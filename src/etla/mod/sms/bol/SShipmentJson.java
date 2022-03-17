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
public class SShipmentJson {
    
    protected int nShipmentId;
    protected int nTicket;
    protected String sOrigLocId;
    protected String sShipperFiscalId;
    protected String sCurrency;
    protected String sPlate;
    protected String sDriverFiscalId;
    protected double dTotWei;
    
    protected ArrayList<SLocationsJson> maLocations;
    
    public SShipmentJson() {
        nShipmentId = 0;
        nTicket = 0;
        sOrigLocId = "";
        sShipperFiscalId = "";
        sCurrency = "";
        sPlate = "";
        sDriverFiscalId = "";
        dTotWei = 0;
        
        maLocations = new ArrayList<>();
    }
    
    public void setShipmentId(int n) { nShipmentId = n; }
    public void setTicket(int n) { nTicket = n; }
    public void setOrigLocId(String s) { sOrigLocId = s; }
    public void setShipperFiscalId(String s) { sShipperFiscalId = s; }
    public void setCurrency(String s) { sCurrency = s; }
    public void setPlate(String s) { sPlate = s; }
    public void setDriverFiscalId(String s) { sDriverFiscalId = s; }
    public void setTotWei(double d) { dTotWei = d; }
    
    public void setLocations(ArrayList<SLocationsJson> o) { maLocations = o; }
    
    public String encodeJson() {
        JSONObject cp = new JSONObject();
        cp.put("embarque", nShipmentId);
        cp.put("boleto", nTicket);
        cp.put("idOrigen", sOrigLocId);
        cp.put("rfcTransportista", sShipperFiscalId);
        cp.put("moneda", sCurrency);
        cp.put("placaTransporte", sPlate);
        cp.put("rfcFigura", sDriverFiscalId);
        cp.put("pesoBrutoTotal", dTotWei);
        
        JSONArray loc = new JSONArray();
        for (SLocationsJson location : maLocations) {
            loc.add(location.getJson());
        }
        
        cp.put("ubicaciones", loc);
        
        return cp.toJSONString();
    }
}
