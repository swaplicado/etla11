/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.sms.bol;

import org.json.simple.JSONObject;

/**
 *
 * @author Isabel Servín
 */
public class SBolMerchandises {
    
    protected String sBienesTransp;
    protected String sSatDescription;
    protected String sInvDescription;
    protected double dQuantity;
    protected String sUnitCode;
    protected double dWeight;
    protected double dValue;
    protected String sCurrency;
    
    public SBolMerchandises() {
        sBienesTransp = "";
        sSatDescription = "";
        sInvDescription = "";
        dQuantity = 0;
        sUnitCode = "";
        dWeight = 0;
        dValue = 0;
        sCurrency = "";
    }
    
    public void setBienesTransp(String s) { sBienesTransp = s; }
    public void setSatDescription(String s) { sSatDescription = s; }
    public void setInvDescription(String s) { sInvDescription = s; }
    public void setQuantity(double d) { dQuantity = d; }
    public void setUnitCode(String s) { sUnitCode = s; }
    public void setWeight(double d) { dWeight = d; }
    public void setValue(double d) { dValue = d; }
    public void setCurrency(String s) { sCurrency = s; }
    
    public String getBienesTransp() { return sBienesTransp; }
    public String getSatDescription() { return sSatDescription; }
    public String getInvDescription() { return sInvDescription; }
    public double getQuantity() { return dQuantity; }
    public String getUnitCode() { return sUnitCode; }
    public double getWeight() { return dWeight; }
    public double getValue() { return dValue; }
    public String getCurrency() { return sCurrency; }
    
    public JSONObject getJson() {
        JSONObject merch = new JSONObject();
        merch.put("bienesTransp", sBienesTransp);
        merch.put("cantidad", dQuantity);
        merch.put("claveUnidad", sUnitCode);
        merch.put("pesoEnKg", dWeight);
        merch.put("valorMercancia", dValue);
        merch.put("moneda", sCurrency);
        
        return merch;
    }
}
