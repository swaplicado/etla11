/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.mod;

/**
 *
 * @author Sergio Flores, Alfredo Pérez, Isabel Servín
 */
public abstract class SModSysConsts {

    public static final int CS_USR_TP_USR = 1;
    public static final int CS_USR_TP_ADM = 2;
    public static final int CS_USR_TP_SUP = 3;

    public static final int CU_USR_NA = 1;
    public static final int CU_USR_ADM = 2;
    public static final int CU_USR_SUP = 3;

    public static final int C_CFG_CFG = 1;

    public static final int AS_CUR_MXN = 1;
    public static final int AS_CUR_USD = 2;

    public static final int AS_UOM_MSM = 1;
    public static final int AS_UOM_MSF = 2;
    public static final int AS_UOM_SQM = 3;
    public static final int AS_UOM_SQF = 4;
    public static final int AS_UOM_PC = 5;
    public static final int AS_UOM_K = 6;
    public static final int AS_UOM_KG = 7;
    public static final int AS_UOM_TON = 8;

    public static final int AS_PAY_MET_NA = 1;
    public static final int AS_PAY_MET_CSH = 11;
    public static final int AS_PAY_MET_CHK = 12;
    public static final int AS_PAY_MET_TRN = 13;
    public static final int AS_PAY_MET_DBT = 21;
    public static final int AS_PAY_MET_CDT = 22;
    public static final int AS_PAY_MET_E_PUR = 31;
    public static final int AS_PAY_MET_E_MON = 32;
    public static final int AS_PAY_MET_FOO = 41;
    public static final int AS_PAY_MET_UND = 98;
    public static final int AS_PAY_MET_OTH = 99;

    public static final int SS_SHIPT_ST_REL_TO = 1;
    public static final int SS_SHIPT_ST_REL = 2;
    public static final int SS_SHIPT_ST_ACC_TO = 11;
    public static final int SS_SHIPT_ST_ACC = 12;

    public static final int SS_WEB_ROLE_NA = 1;
    public static final int SS_WEB_ROLE_ADMIN = 11;
    public static final int SS_WEB_ROLE_CREDIT = 21;
    public static final int SS_WEB_ROLE_SHIPPER = 31;

    public static final int SS_WM_TICKET_TP_IN = 1;     // in
    public static final int SS_WM_TICKET_TP_OUT = 2;    // out

    public static final int SS_WM_LINK_ST_PEND = 1; // pending
    public static final int SS_WM_LINK_ST_APPD = 2; // approved
    public static final int SS_WM_LINK_ST_REJD = 3; // rejected

    public static final int SU_VEHIC_TP_CUS = 9; //customer picks up

    public static final int SU_SHIPPER_NA = 1;

    public static final int SU_DESTIN_NA = 1;

    public static final int SU_WM_ITEM_NA = 1;

    public static final int SU_WM_USER_NA = 1;

    public static final int S_CFG_CFG = 1;
    public static final int S_CFG_SHIPPER_CODE_NAME = 1; 
    public static final int S_CFG_SHIPPER_NAME_CODE = 2; 
}
