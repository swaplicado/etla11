/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.cli;

import erp.lib.SLibUtilities;
import etla.mod.cfg.db.SDbConfig;
import etla.mod.etl.db.SEtlConsts;
import etla.mod.etl.db.SEtlProcess;
import etla.mod.sms.db.SDbConfigSms;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import sa.gui.util.SUtilConfigXml;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiSession;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import sa.lib.mail.SMailUtils;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author Isabel Servín
 */
public class SReportMailer {
    
    public static final String REP_TYPE_IN_P = "EP";
    public static final String REP_TYPE_IN_PA = "EPA";
    public static final String REP_TYPE_OUT_P = "SP";
    public static final String REP_TYPE_OUT_PA = "SPA";
    
    private static final int ARG_REP_TYPE = 0;
    private static final int ARG_MAIL_TO = 1;
    private static final int ARG_MAIL_BCC = 2;
    private static final String DEF_MAIL_TO = "sflores@swaplicado.com.mx;isabel_servin1992@hotmail.com";
    private static final String DEF_MAIL_BCC = "floresgtz@hotmail.com";
    private static final HashMap<String, String> ReportTypesMap = new HashMap<>();
    
    static {
        ReportTypesMap.put(REP_TYPE_IN_P, "Entradas");
        ReportTypesMap.put(REP_TYPE_IN_PA, "Entradas");
        ReportTypesMap.put(REP_TYPE_OUT_P, "Salidas");
        ReportTypesMap.put(REP_TYPE_OUT_PA, "Salidas");
    }

    /**
     * @param args Los argumentos de la interfaz de línea de comandos (CLI).
     * Argumentos esperados:
     * 1: Tipo de reporte indicado mediante una letra mayúscula: EP para entradas por productos, EPA para entradas por producto y asociado de negocio, 
            SP para salidas por producto y SPA para salidas por producto y asociado de negocio.
     * 2: Buzones correo-e destinatarios (TO) (separados con punto y coma, sin espacios en blanco entre ellos, obviamente.)
     * 3: Buzones correo-e de copia oculta (BCC) (separados con punto y coma, sin espacios en blanco entre ellos, obviamente.)
     */
    public static void main(String[] args) {
        try {
            // Definir los argumentos del programa:
            
            String reportType = REP_TYPE_OUT_P;
            String mailTo = DEF_MAIL_TO;
            String mailBcc = DEF_MAIL_BCC;
            
            if (args.length >= 1) {
                reportType = args[ARG_REP_TYPE];
            }
            if (args.length >= 2) {
                mailTo = args[ARG_MAIL_TO];
            }
            if (args.length >= 3) {
                mailBcc = args[ARG_MAIL_BCC];
            }
            
            // Leer el archivo de configuración del software ETLA:
            
            String xml = SXmlUtils.readXml(SUtilConsts.FILE_NAME_CFG);
            SUtilConfigXml configXml = new SUtilConfigXml();
            configXml.processXml(xml);
            
            // Establecer la zona horaria por defecto del JVM:

            TimeZone zone = SLibUtils.createTimeZone(TimeZone.getDefault(), TimeZone.getTimeZone((String) configXml.getAttribute(SUtilConfigXml.ATT_TIME_ZONE).getValue()));
            SLibUtils.restoreDateFormats(zone);
            TimeZone.setDefault(zone);
            
            // Establecer conexión a la base de datos de ETLA:

            SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);
            int result = database.connect(
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_DB_HOST).getValue(),
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_DB_PORT).getValue(),
                    "etla_com", // TODO: oportunidad de mejora al parametrizar esta constante
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_USR_NAME).getValue(),
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_USR_PSWD).getValue());

            if (result != SDbConsts.CONNECTION_OK) {
                throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION);
            }
            
            // Crear sesión de usuario con la conexión a la base de datos de ETLA:
            
            SGuiSession session = new SGuiSession(null);
            session.setDatabase(database);
            
            // Leer configuración del módulo de embarques:
            
            SDbConfigSms configSms = new SDbConfigSms();
            configSms.read(session, new int[] { 1 });
            
            // Establecer conexión a la base de datos de Revuelta:
            
            Connection connection = SEtlProcess.createConnection(SEtlConsts.DB_SYBASE,
                configSms.getRevueltaHost(),
                configSms.getRevueltaPort(),
                configSms.getRevueltaName(),
                configSms.getRevueltaUser(),
                configSms.getRevueltaPassword());
            
            // Generar el asunto del correo-e:
            
            String mailSubject = "Informe " + ReportTypesMap.get(reportType) + " Báscula " + SLibUtils.DateFormatDate.format(new Date());

            // Generar el cuerpo del correo-e en formato HTML:

            String mailBody = SReportHtml.generateReportHtml(connection, reportType, mailSubject);
            
            // Preparar los destinatarios del correo-e:
            
            ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mailTo, ";")));
            ArrayList<String> recipientsBcc = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mailBcc, ";")));
            
            // Leer configuración de ETLA:
            
            SDbConfig config = new SDbConfig();
            config.read(session, new int[] { 1 });
            
            // Crear y enviar correo-e:
            
            //SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "som@aeth.mx", "AETHSOM", "som@aeth.mx");
            SMailSender sender = new SMailSender(config.getMailHost(), config.getMailPort(), config.getMailProtocol(), config.isMailStartTls(), config.isMailAuth(), config.getMailUser(), config.getMailPassword(), config.getMailUser());
            
            SMail mail = new SMail(sender, SMailUtils.encodeSubjectUtf8(SLibUtils.textToAscii(mailSubject)), mailBody, recipientsTo);
            mail.getBccRecipients().addAll(recipientsBcc);
            mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
            mail.send();
            
            System.out.println("Mail send!");
        }
        catch (Exception e) {
            SLibUtils.printException("main()", e);
        }
    }
}
