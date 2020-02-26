/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.cli;

import etla.gui.SGuiMain;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Date;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;

/**
 *
 * @author Isabel Servín
 */
public abstract class SReportHtml {
    
    /**
     * Generates report in HTML 5 format.
     * @param connection
     * @param reportType
     * @param mailSubject
     * @return
     * @throws Exception 
     */
    public static String generateReportHtml(final Connection connection, final String reportType, final String mailSubject) throws Exception {
        
        // Definir el intervalo de fechas del reporte:
        Date today = new Date();
        Date yesterday = SLibTimeUtils.addDate(today, 0, 0, -1);
        String cutOffHour = "18:00";
        
        // Definir las constantes que se van a utilizar:
        int nomUsrBas = 2;
        int idUsrBas = 1; 
        int nomPro = 2; 
        int idPro = 1;
        boolean contentInTable = false;
        
        // HTML:
        
        String html = "<html>\n";
        
        // Cabeza del HTML:
        
        html += "<head>\n";
        html += "<title>"+ mailSubject +"</title>\n";
        html += "</head>\n";
        html += "<style type=\"text/css\">\n" +
            " body {\n" +
            "  padding-left: 2em;\n" +
            "  padding-right: 2em;\n" +
            "  font-family:  \"Times New Roman\",\n" +
            "        Times, serif;\n" +
            "  color: black;\n" +
            "  background-color: whitesmoke}\n" +
            "h1, h2 {\n" +
            "  font-family: Helvetica, Geneva, Arial,\n" +
            "        SunSans-Regular, sans-serif }\n" +
            "address {\n" +
            "  margin-top: 1em;\n" +
            "  padding-top: 1em;\n" +
            "  border-top: thin dotted;"+
            "font-size: 62.5% }\n" +
            "table {\n" +
            "   border: 1px solid #000;\n" +
            "width: 650px;" +
            "table-layout: fixed;" +
            "}\n" +
            "th, td {\n" +
            "   vertical-align: top;\n" +
            "   border: 1px solid #000;\n" +
            "   border-collapse: collapse;\n" +
            "   padding: 0.3em;\n" +
            "   caption-side: bottom;\n"+
            "   font-family:  \"Times New Roman\";" +
            "}\n" +
            "caption {\n" +
            "   padding: 0.3em;\n" +
            "   color: #fff;\n" +
            "    background: #000;\n" +
            "}\n" +
            "th {\n" +
            "   background: #eee;\n" +
            "}\n" +
            "}\n" +
            "  </style>";
        
        // Cuerpo del HTML:
        
        html += "<body>\n";
        html += "<h1>" + SLibUtils.textToHtml(mailSubject) + "</h1>\n";
        html += "" + SLibUtils.textToHtml("Del") + " " + SLibUtils.textToHtml(SLibUtils.DateFormatDate.format(yesterday)) + " " + cutOffHour + 
                " " + SLibUtils.textToHtml("al") + " " + SLibUtils.textToHtml(SLibUtils.DateFormatDate.format(today)) + " " + cutOffHour + "\n";
        
        // Obtener los datos del reporte:
        
        String sql = "SELECT Pro_Nombre, SUM(Pes_Neto) as Total "
            + "FROM dba.Pesadas "
            + "WHERE Pes_FecHorSeg > '" + SLibUtils.DbmsDateFormatDate.format(yesterday) + " " + cutOffHour + "' AND Pes_FecHorSeg <= '" + SLibUtils.DbmsDateFormatDate.format(today) + " " + cutOffHour + "' "
            + "AND Pes_Bruto != Pes_Tara "
            + "AND Pes_PesoPri - Pes_PesoSeg " + (reportType.equals(SReportMailer.REP_TYPE_IN_P) || reportType.equals(SReportMailer.REP_TYPE_IN_PA) ? ">" : "<") + " 0 "
            + "GROUP BY Pro_Nombre "
            + "ORDER BY Pro_Nombre ";
        
        Statement statement = connection.createStatement();
        
        // Tablas HTML:
        // Cabeza de la tabla GLOBAL:
        
        html += "<h2>RESUMEN GLOBAL</h2>\n" +
                "<table>\n" +
                "    <tr>\n" ;
        html += "        <th>Producto</th>\n" +
                "        <th>Total (kg)</th>\n" +
                "    </tr>\n" ;
        
        // Cuerpo de la tabla GLOBAL:
                
        ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int total = 0;
        while (resultSet.next()) {
            contentInTable = true;
            html += "<tr>\n";
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                if(resultSetMetaData.getColumnName(i).equals("Total")){
                    html += "<td align=\"right\">" + SLibUtils.DecimalFormatInteger.format(resultSet.getDouble(i)) + "</td>\n";
                    total += resultSet.getInt(i);
                }
                else
                    html += "<td>" + SLibUtils.textToHtml(resultSet.getString(i)) + "</td>\n";
            }
            html += "</tr>\n";
        }
        
        html += "<tr>\n"+
                "   <td style=\"font-weight: bold\">TOTAL</td>\n";
        html += "   <td style=\"font-weight: bold\" align=\"right\">" + SLibUtils.DecimalFormatInteger.format(total) + "</td>\n"+
                "</tr>";
        
        // fin de la tabla GLOBAL:
        
        html += "</table>";
            
        if(!contentInTable){
            html += "<h3>" + SLibUtils.textToHtml("No se encontró información para el período reportado.") + "</h3>";
        }
        
        // Inicio de las tablas por producto:
        
        // Consulta por asociado de negocios:
        
        String queryUsrBas = "SELECT Usb_ID, Usb_Nombre FROM dba.UsuariosBas";
        
        ResultSet resultSetUsb = statement.executeQuery(queryUsrBas);
        if (reportType.equals(SReportMailer.REP_TYPE_IN_P) || reportType.equals(SReportMailer.REP_TYPE_OUT_P)){
            Statement statementUsb = connection.createStatement();
            while (resultSetUsb.next()){
                String sqlxUsrBas = "SELECT Pro_Nombre, SUM(Pes_Neto) as Total "
                    + "FROM dba.Pesadas "
                    + "WHERE Pes_FecHorSeg > '" + SLibUtils.DbmsDateFormatDate.format(yesterday) + " " + cutOffHour + "' AND Pes_FecHorSeg <= '" + SLibUtils.DbmsDateFormatDate.format(today) + " " + cutOffHour + "' "
                    + "AND Usb_ID = '" + resultSetUsb.getString(idUsrBas) + "' "
                    + "AND Pes_Bruto != Pes_Tara "
                    + "AND Pes_PesoPri - Pes_PesoSeg " + (reportType.equals(SReportMailer.REP_TYPE_IN_P) || reportType.equals(SReportMailer.REP_TYPE_IN_PA) ? ">" : "<") + " 0 "
                    + "GROUP BY Pro_Nombre "
                    + "ORDER BY Pro_Nombre ";
                ResultSet resultSetxUsrBas = statementUsb.executeQuery(sqlxUsrBas);
                ResultSetMetaData resultSetxUsrBasMetaData = resultSetxUsrBas.getMetaData();
                int encabezado = 0;
                int totalUsb = 0;
                while(resultSetxUsrBas.next()){
                    if (encabezado == 0){
                       encabezado++; 
                       html += "<br>\n" +
                            "<h2>" + SLibUtils.textToHtml(resultSetUsb.getString(nomUsrBas)) + "</h2>" +
                            "<table>\n" +
                            "    <tr>\n" ;
                       html += "        <th>Producto</th>\n" +
                            "        <th>Total (kg)</th>\n" +
                            "    </tr>\n";
                    }
                    html += "<tr>\n";
                    for (int i = 1; i <= resultSetxUsrBasMetaData.getColumnCount(); i++) {
                        if(resultSetxUsrBasMetaData.getColumnName(i).equals("Total")){
                            html += "<td align=\"right\">" + SLibUtils.DecimalFormatInteger.format(resultSetxUsrBas.getDouble(i)) + "</td>\n";
                            totalUsb += resultSetxUsrBas.getInt(i);
                        }
                        else
                            html += "<td>" + SLibUtils.textToHtml(resultSetxUsrBas.getString(i)) + "</td>\n";
                    }
                    html += "</tr>\n";
                }
                if (totalUsb != 0){
                    html += "<tr>\n"+
                        "   <td style=\"font-weight: bold\">TOTAL</td>\n";
                    html +="<td style=\"font-weight: bold\" align=\"right\">" + SLibUtils.DecimalFormatInteger.format(totalUsb) + "</td>\n"+
                        "</tr>";
                }
                html += "</table>";
            }
        }
        // Inicio de las tablas por asociado de negocio:
        else{
            Statement statementPro = connection.createStatement();
            Statement statementxPro = connection.createStatement();
            while (resultSetUsb.next()){
                String sqlPro = "SELECT Pro_ID, Pro_Nombre FROM dba.Productos ORDER BY Pro_Nombre";
                ResultSet resultSetPro = statementPro.executeQuery(sqlPro);
                int usuarioBas = 0; // Hace que el nombre del Usuario Bascula solo se imprima una vez
                while (resultSetPro.next()){
                    sqlPro = "SELECT Emp_Nombre, SUM(Pes_Neto) as Total "
                        + "FROM dba.Pesadas "
                        + "WHERE Pes_FecHorSeg > '" + SLibUtils.DbmsDateFormatDate.format(yesterday) + " " + cutOffHour + "' AND Pes_FecHorSeg <= '" + SLibUtils.DbmsDateFormatDate.format(today) + " " + cutOffHour + "' "
                        + "AND Usb_ID = '" + resultSetUsb.getString(idUsrBas) + "' "
                        + "AND Pro_ID = '" + resultSetPro.getString(idPro) + "' "
                        + "AND Pes_Bruto != Pes_Tara "
                        + "AND Pes_PesoPri - Pes_PesoSeg " + (reportType.equals(SReportMailer.REP_TYPE_IN_P) || reportType.equals(SReportMailer.REP_TYPE_IN_PA) ? ">" : "<") + " 0 "
                        + "GROUP BY Emp_Nombre "
                        + "ORDER BY Emp_Nombre ";
                    ResultSet resultSetxPro = statementxPro.executeQuery(sqlPro);
                    ResultSetMetaData resultSetxProMetaData = resultSetxPro.getMetaData();
                    int encabezado = 0;
                    int totalPro = 0;
                    while (resultSetxPro.next()){
                        if (encabezado == 0){
                            encabezado++;
                            if (usuarioBas == 0){
                                html += "<br>\n" +
                                    "<h2>" + SLibUtils.textToHtml(resultSetUsb.getString(nomUsrBas)) + "</h2>\n" ;
                                usuarioBas++;
                            }
                            html += "<h3>" + SLibUtils.textToHtml(resultSetPro.getString(nomPro)) + "</h3>\n" +
                                "<table>\n" +
                                "    <tr>\n" ;
                            html += "        <th>Asociado de negocios</th>\n" +
                                "        <th>Total (kg)</th>\n" +
                                "    </tr>\n";
                        }
                        html += "<tr>\n";
                        for (int i = 1; i <= resultSetxProMetaData.getColumnCount(); i++) {
                            if(resultSetxProMetaData.getColumnName(i).equals("Total")){
                                html += "<td align=\"right\">" + SLibUtils.DecimalFormatInteger.format(resultSetxPro.getDouble(i)) + "</td>\n";
                                totalPro += resultSetxPro.getInt(i);
                            }
                            else {
                                html += "<td>" + SLibUtils.textToHtml(resultSetxPro.getString(i)) + "</td>\n";
                            }
                        }
                        html += "</tr>\n";
                    }
                    if (totalPro != 0) {
                        html += "<tr>\n"+
                            "   <td style=\"font-weight: bold\">TOTAL</td>\n";
                        html +="<td style=\"font-weight: bold\" align=\"right\">" + SLibUtils.DecimalFormatInteger.format(totalPro) + "</td>\n"+
                            "</tr>";
                    }
                    
                    html += "</table>";
                }
            }
        }
        
        // fin del mensaje en formato HTML:
           
        html += "<address>" +
                SLibUtils.textToHtml("Favor de no responder este mail, fue generado de forma automática.") +
                "<br>" +
               SLibUtils.textToHtml(SGuiMain.APP_NAME) + " " + SLibUtils.textToHtml(SGuiMain.APP_COPYRIGHT) + " " +
                "<br>" +
                SLibUtils.textToHtml(SGuiMain.APP_PROVIDER) +
                "</font>" +
                "<br>" +
                "<font size='1'>" +
                SLibUtils.textToHtml(SGuiMain.APP_RELEASE) +
                "</font>" +
                "</p> </address>";
        
        html += "</body>\n";
        
        html += "</html>";
        
        return html;
    }
}
