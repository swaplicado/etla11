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
public class SMerchandisesJson {
    
    protected String sBienesTransp;
    protected double dQuantity;
    protected String sUnitCode;
    protected double dWeight;
    protected double dValue;
    protected String sCurrency;
    
    public SMerchandisesJson() {
        sBienesTransp = "";
        dQuantity = 0;
        sUnitCode = "";
        dWeight = 0;
        dValue = 0;
        sCurrency = "";
    }
    
    public void setBienesTransp(String s) { sBienesTransp = s; }
    public void setQuantity(double d) { dQuantity = d; }
    public void setUnitCode(String s) { sUnitCode = s; }
    public void setWeight(double d) { dWeight = d; }
    public void setValue(double d) { dValue = d; }
    public void setCurrency(String s) { sCurrency = s; }
    
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
