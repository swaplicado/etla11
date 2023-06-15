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
public class SBolShipment {
    
    protected int nShipmentId;
    protected int nTicket;
    protected String sOrigLocId;
    protected String sShipperFiscalId;
    protected String sProviderCode;
    protected String sCurrency;
    protected String sPlate;
    protected String sTrailerPlate;
    protected String sDriverFiscalId;
    protected double dTotWei;
    
    protected ArrayList<SBolLocations> maLocations;
    
    public SBolShipment() {
        nShipmentId = 0;
        nTicket = 0;
        sOrigLocId = "";
        sShipperFiscalId = "";
        sProviderCode = "";
        sCurrency = "";
        sPlate = "";
        sTrailerPlate = "";
        sDriverFiscalId = "";
        dTotWei = 0;
        
        maLocations = new ArrayList<>();
    }
    
    public void setShipmentId(int n) { nShipmentId = n; }
    public void setTicket(int n) { nTicket = n; }
    public void setOrigLocId(String s) { sOrigLocId = s; }
    public void setShipperFiscalId(String s) { sShipperFiscalId = s; }
    public void setProviderCode(String s) { sProviderCode = s; }
    public void setCurrency(String s) { sCurrency = s; }
    public void setPlate(String s) { sPlate = s; }
    public void setTrailerPlate(String s) { sTrailerPlate = s; }
    public void setDriverFiscalId(String s) { sDriverFiscalId = s; }
    public void setTotWei(double d) { dTotWei = d; }
    
    public void setLocations(ArrayList<SBolLocations> o) { maLocations = o; }
    
    public String getProviderCode() { return sProviderCode; }
    
    public ArrayList<SBolLocations> getLocations() { return maLocations; }
    
    @SuppressWarnings("unchecked")
    public String encodeJson() {
        JSONObject cp = new JSONObject();
        cp.put("embarque", nShipmentId);
        cp.put("boleto", nTicket);
        cp.put("idOrigen", sOrigLocId);
        cp.put("rfcTransportista", sShipperFiscalId);
        cp.put("proveedor", sProviderCode);
        cp.put("moneda", sCurrency);
        cp.put("placaTransporte", sPlate);
        cp.put("placaRemolque", sTrailerPlate);
        cp.put("rfcFigura", sDriverFiscalId);
        cp.put("pesoBrutoTotal", dTotWei);
        
        JSONArray loc = new JSONArray();
        for (SBolLocations location : maLocations) {
            loc.add(location.getJson());
        }
        
        cp.put("ubicaciones", loc);
        
        return cp.toJSONString();
    }
}
