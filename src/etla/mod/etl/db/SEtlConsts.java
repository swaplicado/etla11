/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod.etl.db;

import java.util.HashMap;

/**
 *
 * @author Sergio Flores
 */
public abstract class SEtlConsts {
    
    public static final boolean SHOW_DEBUG_MSGS = true;
    
    public static final int EXP_MODE_CAT = 1; // catalogs
    public static final int EXP_MODE_ALL = 2; // all (catalogs & invoices)
    
    public static final int UPD_MODE_SEL = 1; // selective
    public static final int UPD_MODE_ALL = 2; // all
    
    public static final int DB_MYSQL = 1;
    public static final int DB_SQL_SERVER = 2;
    public static final int DB_SYBASE = 3;
    
    public static final int AVISTA_INV_STA_APP = 2; // invoice status: approved
    public static final int AVISTA_INV_STA_ARC = 3; // invoice status: archived
    public static final int AVISTA_INV_TP_INV = 1;  // invoice type: invoice
    
    public static final int AVISTA_CUR_USD = 1;
    public static final int AVISTA_CUR_MXN = 2;
    
    public static final int BILLING_DELAY_DAYS = 3; // days allowed to bill invoices after issue
    
    public static final String AVISTA_UOM_MSM = "MSM";  // millar square meter
    public static final String AVISTA_UOM_MSF = "MSF";  // millar square feet
    public static final String AVISTA_UOM_SQM = "SQM";  // square meter - not supported yet!
    public static final String AVISTA_UOM_SQF = "SQF";  // square feet - not supported yet!
    public static final String AVISTA_UOM_PC = "PC";    // piece
    public static final String AVISTA_UOM_M = "M";      // meter - not supported yet!
    public static final String AVISTA_UOM_KG = "KG";    // kg - not supported yet!
    public static final String AVISTA_UOM_TON = "TON";  // ton
    public static final String AVISTA_UOM_FF = "FF";    // some kind of 'fee' - due to issue of extra charge in CustomerInvoices with CustomerInvoiceKey=85155, InvoiceNumber=82164, BOL=85613, Created=2018-02-15
    
    public static final String AVISTA_PAY_TERM_CNT = "CNT"; // contado
    public static final String AVISTA_BOOL_N = "N"; // no
    public static final String AVISTA_BOOL_Y = "Y"; // yes
    public static final String AVISTA_LOC_CTY = "MX";   // México
    public static final String AVISTA_LOC_STA = "MEX";  // Estado de México
    
    public static final int SIIE_DEFAULT = 1;
    public static final String SIIE_PAY_ACC_UNDEF = "NO IDENTIFICADO";
    public static final int SIIE_UNIT_MSM = 108;
    public static final int SIIE_UNIT_MSF = 109;
    public static final int[] SIIE_TAX_0 = new int[] { 1, 2 }; // IVA 0%
    public static final double SIIE_TAX_RATE_0 = 0.0;
    public static final int[] SIIE_TAX_16 = new int[] { 1, 6 }; // IVA 16%
    public static final double SIIE_TAX_RATE_16 = 0.16;
    public static final int SIIE_PRICE_UNIT_DECS = 3;
    
    public static final HashMap<String, String> AvistaCountriesMap = new HashMap<>();
    public static final HashMap<String, String> AvistaStatesMap = new HashMap<>();
    
    static {
        AvistaCountriesMap.put(AVISTA_LOC_CTY, "México");
        AvistaStatesMap.put(AVISTA_LOC_STA, "Estado de México");
    }
    
    public static final int RFC_LEN_PER = 13;
    public static final int RFC_LEN_ORG = 12;
    
    public static final int STEP_NA = 0; // n/a
    public static final int STEP_AUX_NA = 0; // n/a

    public static final int STEP_ETL_STA = 100; // starting ELT process
    public static final int STEP_ETL_STA_DB_SIIE = 110; // DB connection SIIE stablished
    public static final int STEP_ETL_STA_DB_AVISTA = 120; // DB connection Avista stablished
    public static final int STEP_ETL_END = 900; // ELT process finished

    public static final int STEP_CUS_STA = 200; // starting ELT customers
    public static final int STEP_AUX_CUS_AUX_1 = 1; // customers auxiliar step #1
    public static final int STEP_AUX_CUS_AUX_2 = 2; // customers auxiliar step #2
    public static final int STEP_AUX_CUS_AUX_3 = 3; // customers auxiliar step #3
    public static final int STEP_CUS_END = 299; // finished ELT customers

    public static final int STEP_ITM_STA = 300; // starting ELT items
    public static final int STEP_AUX_ITM_AUX_1 = 1; // items auxiliar step #1
    public static final int STEP_AUX_ITM_AUX_2 = 2; // items auxiliar step #2
    public static final int STEP_AUX_ITM_AUX_3 = 3; // items auxiliar step #3
    public static final int STEP_ITM_END = 399; // finished ETL items

    public static final int STEP_INV_STA = 400; // starting ETL invoices
    public static final int STEP_AUX_INV_AUX_10 = 10; // invoices auxiliar step #10
    public static final int STEP_AUX_INV_AUX_11 = 11; // invoices auxiliar step #11
    public static final int STEP_AUX_INV_AUX_12 = 12; // invoices auxiliar step #12
    public static final int STEP_AUX_INV_AUX_13 = 13; // invoices auxiliar step #13
    public static final int STEP_AUX_INV_AUX_14 = 14; // invoices auxiliar step #14
    public static final int STEP_AUX_INV_AUX_20 = 20; // invoices auxiliar step #20
    public static final int STEP_INV_END = 499; // finished ETL invoices

    public static final String TXT_INV = "Factura";
    public static final String TXT_BRD = "Lámina";
    public static final String TXT_FLT = "Flauta";
    public static final String TXT_CUR = "Moneda";
    public static final String TXT_UOM = "Unidad de medida";
    public static final String TXT_SAL_AGT = "Agente de ventas";
    public static final String TXT_CUS = "Cliente";
    public static final String TXT_ITM = "Ítem";
    public static final String TXT_EXR = "Tipo de cambio";
    public static final String TXT_MISC_ID = "ID";
    public static final String TXT_MISC_DAT = "Fecha";
    public static final String TXT_MISC_SRC = "Origen";
    public static final String TXT_MISC_DES = "Destino";
    public static final String TXT_MISC_REQ = "Requerid@";
    public static final String TXT_MISC_QTY = "Cantidad";
    public static final String TXT_MISC_SIZ = "Tamaño";
    public static final String TXT_MISC_WEI = "Peso";
    public static final String TXT_MISC_O = "Orden";
    public static final String TXT_MISC_O_ACR = "O";
    public static final String TXT_MISC_PO = "Orden de compra";
    public static final String TXT_MISC_PO_ACR = "OC";
    public static final String TXT_SYS_SIIE = "SIIE";
    public static final String TXT_SYS_AVISTA = "Avista Axiom";
    
    public static final String MSG_ERR = "Ha ocurrido una excepción";
    public static final String MSG_ERR_UNS_UOM = MSG_ERR + ": unidad de medida no soportada.";
    public static final String MSG_ERR_WRG_QTY = MSG_ERR + ": cantidad igual a cero.";
    public static final String MSG_ERR_USR_DES_ID = MSG_ERR + ": el usuario no tiene su ID " + TXT_SYS_SIIE + ".";
    public static final String MSG_ERR_CUS_TAX_ID = MSG_ERR + " al validar el RFC el cliente: ";
    public static final String MSG_ERR_CUS_NAME = MSG_ERR + " al validar el nombre el cliente: ";
    public static final String MSG_ERR_UNK_CTY = MSG_ERR + " al determinar el país: ";
    public static final String MSG_ERR_UNK_STA = MSG_ERR + " al determinar el estado: ";
    public static final String MSG_ERR_UNK_CUR = MSG_ERR + " al determinar la moneda: ";
    public static final String MSG_ERR_UNK_CUR_MLT_ETL = MSG_ERR_UNK_CUR + "múltiples monedas definidas para exportación.";
    public static final String MSG_ERR_UNK_CUR_MLT_SRC = MSG_ERR_UNK_CUR + "múltiples monedas de origen.";
    public static final String MSG_ERR_UNK_UOM = MSG_ERR + " al determinar la unidad de medida: ";
    public static final String MSG_ERR_UNK_SAL_AGT = MSG_ERR + " al determinar el agente de ventas: ";
    public static final String MSG_ERR_UNK_CUS = MSG_ERR + " al determinar el cliente: ";
    public static final String MSG_ERR_UNK_ITM = MSG_ERR + " al determinar el ítem: ";
    public static final String MSG_ERR_UNK_EXR = MSG_ERR + " al determinar el tipo de cambio: ";
    public static final String MSG_ERR_SIIE_COM_QRY = MSG_ERR + " al consultar el registro SIIE empresa: ";
    public static final String MSG_ERR_SIIE_COM_INS = MSG_ERR + " al insertar el registro SIIE empresa: ";
    public static final String MSG_ERR_SIIE_COM_UPD = MSG_ERR + " al actualizar el registro SIIE empresa: ";
    public static final String MSG_ERR_SIIE_CUS_QRY = MSG_ERR + " al consultar el registro SIIE cliente: ";
    public static final String MSG_ERR_SIIE_CUS_INS = MSG_ERR + " al insertar el registro SIIE cliente: ";
    public static final String MSG_ERR_SIIE_CUS_UPD = MSG_ERR + " al actualizar el registro SIIE cliente: ";
    public static final String MSG_ERR_SIIE_CUS_STA = MSG_ERR + " en el estatus del registro SIIE cliente: ";
    public static final String MSG_ERR_SIIE_ITM_QRY = MSG_ERR + " al consultar el registro SIIE ítem: ";
    public static final String MSG_ERR_SIIE_ITM_INS = MSG_ERR + " al insertar el registro SIIE ítem: ";
    public static final String MSG_ERR_SIIE_ITM_UPD = MSG_ERR + " al actualizar el registro SIIE ítem: ";
    public static final String MSG_ERR_SIIE_ITM_STA = MSG_ERR + " en el estatus del registro SIIE ítem: ";
    public static final String MSG_ERR_SIIE_DOC_QRY = MSG_ERR + " al consultar el registro SIIE documento: ";
    public static final String MSG_ERR_SIIE_DOC_INS = MSG_ERR + " al insertar el registro SIIE documento: ";
    public static final String MSG_ERR_SIIE_DOC_UPD = MSG_ERR + " al actualizar el registro SIIE documento: ";
    public static final String MSG_ERR_SIIE_DOC_STA = MSG_ERR + " en el estatus del registro SIIE documento: ";
    public static final String MSG_ERR_REC_STA_DEL = "El registro está eliminado.";
}
