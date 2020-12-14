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
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
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
import som.mod.cfg.db.SDbCompany;
import som.mod.som.db.SSomUtils;

/**
 *
 * @author Isabel Servín
 */
public class SReportMailerMonthly {
    
    public static final String SYS_TYPE_ETL = "ETL";
    public static final String SYS_TYPE_SOM = "SOM";
    
    public static final String REP_TYPE_IN = "E";
    public static final String REP_TYPE_OUT = "S";
    
    private static final int ARG_SYS_TYPE = 0;
    private static final int ARG_REP_TYPE = 1;
    private static final int ARG_MAIL_TO = 2;
    private static final int ARG_MAIL_BCC = 3;
    private static final String DEF_MAIL_TO = "sflores@swaplicado.com.mx;isabel_servin1992@hotmail.com";
    private static final String DEF_MAIL_BCC = "floresgtz@hotmail.com";
    private static final HashMap<String, String> ReportTypesMap = new HashMap<>();
    
    private final static Logger LOGGER = Logger.getLogger("mailer.SReportMonthlyMailer");
    
    static {
        ReportTypesMap.put(REP_TYPE_IN, "Entradas");
        ReportTypesMap.put(REP_TYPE_OUT, "Salidas");
    }

    /**
     * @param args Los argumentos de la interfaz de línea de comandos (CLI).
     * Argumentos esperados:
     * 1: Tipo de sistema quien convoca al reporte, puede ser ETL o SOM
     * 2: Tipo de reporte indicado mediante una letra mayúscula: E para entradas y S para salidas.
     * 3: Buzones correo-e destinatarios (TO) (separados con punto y coma, sin espacios en blanco entre ellos, obviamente.)
     * 4: Buzones correo-e de copia oculta (BCC) (separados con punto y coma, sin espacios en blanco entre ellos, obviamente.)
     */
    public static void main(String[] args) {
        try {
            // Definir los argumentos del programa:
            
            String systemType = SYS_TYPE_ETL;
            String reportType = REP_TYPE_OUT;
            String mailTo = DEF_MAIL_TO;
            String mailBcc = DEF_MAIL_BCC;
            
            if (args.length >= 1) {
                systemType = args[ARG_SYS_TYPE];
            }
            if (args.length >= 2) {
                reportType = args[ARG_REP_TYPE];
            }
            if (args.length >= 3) {
                mailTo = args[ARG_MAIL_TO];
            }
            if (args.length >= 4) {
                mailBcc = args[ARG_MAIL_BCC];
            }
            
            // Creacion de parametros para la bitacora de erroes
            
            /*  Con el manejador de archivo, indicamos el archivo donde se mandaran los logs
                El segundo argumento controla si se sobre escribe el archivo o se agregan los logs al final
                Pase un true para agregar al final, false para sobre escribir todo el archivo
            */
            Handler fileHandler = new FileHandler(ReportTypesMap.get(reportType) + "_mailerMonthly.log", true);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            LOGGER.addHandler(fileHandler);
            
            // Se envía mensaje a la bitacora
            
            LOGGER.log(Level.INFO, "{0}\n", "Mailer inicializado");
            
            // Leer el archivo de configuración del software:
            
            String xml = SXmlUtils.readXml(SUtilConsts.FILE_NAME_CFG);
            SUtilConfigXml configXml = new SUtilConfigXml();
            configXml.processXml(xml);
            
            // Establecer la zona horaria por defecto del JVM:

            TimeZone zone = SLibUtils.createTimeZone(TimeZone.getDefault(), TimeZone.getTimeZone((String) configXml.getAttribute(SUtilConfigXml.ATT_TIME_ZONE).getValue()));
            SLibUtils.restoreDateFormats(zone);
            TimeZone.setDefault(zone);
            
            // Establecer conexión a la base de datos:

            SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);
            String bd = "";
            switch (systemType) {
                case SYS_TYPE_ETL:
                    bd = "etla_com"; // TODO: oportunidad de mejora al parametrizar esta constante
                    break;
                case SYS_TYPE_SOM:
                    bd = "som_com";  // TODO: oportunidad de mejora al parametrizar esta constante
                    break;
                default:
            }
            
            int result = database.connect(
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_DB_HOST).getValue(),
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_DB_PORT).getValue(),
                    bd, // TODO: oportunidad de mejora al parametrizar esta constante
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_USR_NAME).getValue(),
                    (String) configXml.getAttribute(SUtilConfigXml.ATT_USR_PSWD).getValue());

            if (result != SDbConsts.CONNECTION_OK) {
                throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION);
            }
            
            // Crear sesión de usuario con la conexión a la base de datos:
            
            SGuiSession session = new SGuiSession(null);
            session.setDatabase(database);
            
            Connection connection = null;
            switch (systemType) {
                case SYS_TYPE_ETL:
                // Leer configuración del módulo de embarques ETLA:

                    SDbConfigSms configSms = new SDbConfigSms();
                    configSms.read(session, new int[] { 1 });

                    // Establecer conexión a la base de datos de Revuelta:

                    connection = SEtlProcess.createConnection(SEtlConsts.DB_SYBASE,
                    configSms.getRevueltaHost(),
                    configSms.getRevueltaPort(),
                    configSms.getRevueltaName(),
                    configSms.getRevueltaUser(),
                    configSms.getRevueltaPassword());
                    break;
                    
                case SYS_TYPE_SOM:
                    // Establecer conexión a la base de datos de Revuelta:
                    connection = SSomUtils.openConnectionRevueltaJdbc(session);
                    break;
                    
                default:
            }
            
            // Generar el asunto del correo-e:
            
            String mailSubject = systemType.equals(SYS_TYPE_ETL) ? "[SIIE] " : "[" + SYS_TYPE_SOM + "] ";
            String mailTitle = ReportTypesMap.get(reportType) + " mensuales báscula al " + SLibUtils.DateFormatDate.format(new Date());
            mailSubject += mailTitle;

            // Generar el cuerpo del correo-e en formato HTML:

            String mailBody = SReportMailerMonthlyHtml.generateReportHtml(connection, reportType, mailTitle);
            
            
            // Preparar los destinatarios del correo-e:
            
            ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mailTo, ";")));
            ArrayList<String> recipientsBcc = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mailBcc, ";")));
            
            // Parametros necesarios para enviar el correo
            
            String mailHost = "";
            String mailPort = "";
            String mailProtocol = "";
            boolean mailStartTls = false;
            boolean mailAuth = false;
            String mailUser = "";
            String mailPassword = "";
            
            switch (systemType) {
                case SYS_TYPE_ETL:
                // Leer configuración de ETLA:
                    SDbConfig config = new SDbConfig();
                    config.read(session, new int[] { 1 });
                    mailHost = config.getMailHost();
                    mailPort = config.getMailPort();
                    mailProtocol = config.getMailProtocol();
                    mailStartTls = config.isMailStartTls();
                    mailAuth = config.isMailAuth();
                    mailUser = config.getMailUser();
                    mailPassword = config.getMailPassword();
                    break;
                
                case SYS_TYPE_SOM:
                    // Leer la configuración de SOM:
                    SDbCompany company = new SDbCompany();
                    company.read(session, new int[] { 1 });
                    mailHost = company.getMailNotificationConfigHost();
                    mailPort = company.getMailNotificationConfigPort();
                    mailProtocol = company.getMailNotificationConfigProtocol();
                    mailStartTls = company.isMailNotificationConfigStartTls();
                    mailAuth = company.isMailNotificationConfigAuth();
                    mailUser = company.getMailNotificationConfigUser();
                    mailPassword = company.getMailNotificationConfigPassword();
                    break;
                    
                default:
            }
            
            
            // Crear y enviar correo-e:
            
            //SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "som@aeth.mx", "AETHSOM", "som@aeth.mx");
            SMailSender sender = new SMailSender(mailHost, mailPort, mailProtocol, mailStartTls, mailAuth, mailUser, mailPassword, mailUser);
            
            SMail mail = new SMail(sender, SMailUtils.encodeSubjectUtf8(SLibUtils.textToAscii(mailSubject)), mailBody, recipientsTo);
            mail.getBccRecipients().addAll(recipientsBcc);
            mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
            mail.send();
            
            System.out.println("Mail send!");
        }
        catch (Exception e) {
            SLibUtils.printException("main()", e);
            LOGGER.log(Level.SEVERE, "{0}\n", e.getMessage());
        }
    }
}
