/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etla.cli;

import etla.gui.SGuiMain;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import sa.lib.SLibUtils;

/**
 *
 * @author Isabel Servín
 */
public abstract class SReportMailerMonthlyOneYearHtml {
    
    public static String generateReportHtml(final Connection connection, final String reportType, final String companies, final String mailSubject) throws Exception {
        Date today = new Date();
        
        // Celdas donde se encuentan el nombre del usuario y del producto y el ID del usuario y del producto
        int nomPro = 2; 
        int idPro = 1;
        
        String monthsTable []= {"Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"};
        
        // HTML:
        
        String htmlTables = "";
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
//            "h2 { background-color: turquoise}" +
            "address {\n" +
            "  margin-top: 1em;\n" +
            "  padding-top: 1em;\n" +
            "  border-top: thin dotted;"+
            "font-size: 62.5% }\n" +
            "table {\n" +
            "   border: 1px solid #000;\n" +
            "width:auto;" +
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
        
        Statement statementHour = connection.createStatement();
        String sqlHour = "SELECT YEAR(NOW()), HOUR(NOW()), MINUTE(NOW());";
        ResultSet resultSetHour = statementHour.executeQuery(sqlHour);
        String hour = "";
        String minute = "";
        String year = "";
        if (resultSetHour.next()) {
            year = resultSetHour.getString(1);
            hour = resultSetHour.getString(2);
            minute = resultSetHour.getString(3).length() == 1 ? "0" + resultSetHour.getString(3) : resultSetHour.getString(3);
        }
        html += "<body>\n";
        html += "<h1>" + SLibUtils.textToHtml(mailSubject) + "</h1>\n";
        html += SLibUtils.textToHtml("Año: " + year) + "<br>";
        html += "Hora de corte: " + hour + ":" + minute + " hrs." + "\n";
        
        // Consulta de productos
        String empresas[] = companies.split(";");
        String emp1[] = empresas[0].split("=");
        String emp2[] = empresas[1].split("=");
        
        Statement statementPro = connection.createStatement();
        String queryPro = "SELECT DISTINCT pro.Pro_ID, pro.Pro_Nombre FROM dba.Productos AS pro "
            + "INNER JOIN dba.Pesadas AS pes ON pes.Pro_ID = pro.Pro_ID "
            + "WHERE YEAR(pes.Pes_FecHorSeg) >= YEAR('" + SLibUtils.DbmsDateFormatDate.format(today) + "') "
            + "AND (pes.Usb_ID = '" + emp1[0] + "' OR pes.Usb_ID = '" + emp2[0] + "') "
            + "ORDER BY pro.Pro_Nombre";
        ResultSet resultSetPro = statementPro.executeQuery(queryPro);
        Statement statementxPro = connection.createStatement();

        while (resultSetPro.next()) {
            double totalAnioAETH = 0;
            double totalAnioAME = 0;
            
            String sqlTotalAnio = "SELECT Pro_ID, SUM( CASE WHEN Usb_ID = '" + emp2[0] + "' THEN Pes_Neto ELSE 0.0 END ) as AETH, "
                + "SUM(CASE WHEN Usb_ID = '" + emp1[0] + "' THEN Pes_Neto ELSE 0.0 END) as " + emp1[0] + " " 
                + "FROM dba.Pesadas " 
                + "WHERE YEAR(Pes_FecHorSeg) = YEAR('" + SLibUtils.DbmsDateFormatDate.format(today) + "') " 
                + "AND (Usb_ID = '" + emp1[0] + "' OR Usb_ID = '" + emp2[0] + "') " 
                + "AND Pes_PesoPri - Pes_PesoSeg " + (reportType.equals(SReportMailerMonthly.REP_TYPE_IN) ? ">" : "<") + " 0 " 
                + "AND Pro_ID = '" + resultSetPro.getString("Pro_ID") + "' " 
                + "GROUP BY Pro_ID";
            try (Statement statementTotalAnio = connection.createStatement()) {
                ResultSet resultSetTotalAnio = statementTotalAnio.executeQuery(sqlTotalAnio);
                if (resultSetTotalAnio.next()) {
                    totalAnioAETH = resultSetTotalAnio.getDouble("AETH");
                    totalAnioAME = resultSetTotalAnio.getDouble(emp1[0]);
                }
            }
            
            if (totalAnioAETH != 0 || totalAnioAME != 0){
                // Consulta que saca las cantidades mes por mes
                String sqlxPro = "SELECT Pro_ID, YEAR(Pes_FecHorSeg) as Anio, MONTH(Pes_FecHorSeg) as Mes, SUM( CASE WHEN Usb_ID = '" + emp2[0] + "' THEN Pes_Neto ELSE 0.0 END ) as " + emp2[0] + ", "
                    + "SUM(CASE WHEN Usb_ID = '" + emp1[0] + "' THEN Pes_Neto ELSE 0.0 END) as " + emp1[0] + " "
                    + "FROM dba.Pesadas "
                    + "WHERE YEAR(Pes_FecHorSeg) = YEAR('" + SLibUtils.DbmsDateFormatDate.format(today) + "') "
                    + "AND (Usb_ID = '" + emp1[0] + "' OR Usb_ID = '" + emp2[0] + "') "
                    + "AND Pes_PesoPri - Pes_PesoSeg " + (reportType.equals(SReportMailerMonthly.REP_TYPE_IN) ? ">" : "<") + " 0 "
                    + "AND Pro_ID = '" + resultSetPro.getString(idPro) + "' "
                    + "GROUP BY Pro_ID, Anio, Mes "
                    + "ORDER BY Mes , Anio DESC";

                ResultSet resultSetxPro = statementxPro.executeQuery(sqlxPro);

                //Cabecera
                htmlTables += "<h2>" + SLibUtils.textToHtml(resultSetPro.getString(nomPro)) + "</h2>" +
                        "<table>\n" +
                        "    <tr>\n" +
                        "        <th style=\"width: 25px\" scope=\"row\" rowspan=\"2\">Mes</th>\n" +
                        "        <th style=\"width: 200px\" colspan=\"2\">" + emp1[1] + "</th>\n" +
                        "        <th style=\"width: 200px\" colspan=\"2\">" + emp2[1] + "</th>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <th style=\"width: 150px\">kg</th>\n" +
                        "        <th style=\"width: 50px\">%</th>\n" +
                        "        <th style=\"width: 150px\">kg</th>\n" +
                        "        <th style=\"width: 50px\">%</th>\n" +
                        "    </tr>";

                // Cuerpo de la tabla
                int i = 1;
                while (resultSetxPro.next()) {
                    while (resultSetxPro.getInt("Mes") != i) {
                        htmlTables += "<tr>\n" +
                                "<td>" + SLibUtils.textToHtml(monthsTable[i - 1]) + "</td>\n" +
                                "<td style=\"width: 150px\" align=\"right\">" + SLibUtils.DecimalFormatInteger.format(0) + "</td>\n" +
                                "<td style=\"width: 50px;font-size:80%\" align=\"right\">" + SLibUtils.DecimalFormatPercentage2D.format(0) + "</td>\n" +
                                "<td style=\"width: 150px\" align=\"right\">" + SLibUtils.DecimalFormatInteger.format(0) + "</td>\n" +
                                "<td style=\"width: 50px;font-size:80%\" align=\"right\">" + SLibUtils.DecimalFormatPercentage2D.format(0) + "</td>\n" ;
                        i++;
                    }
                    htmlTables += "<tr>\n" +
                            "<td>" + SLibUtils.textToHtml(monthsTable[i - 1]) + "</td>\n" +
                            "<td style=\"width: 150px\" align=\"right\">" + SLibUtils.DecimalFormatInteger.format(resultSetxPro.getInt(emp1[0])) + "</td>\n" +
                            "<td style=\"width: 50px;font-size:80%\" align=\"right\">" + SLibUtils.DecimalFormatPercentage2D.format(totalAnioAME != 0 ? resultSetxPro.getInt(emp1[0]) / totalAnioAME : 0) + "</td>\n" +
                            "<td style=\"width: 150px\" align=\"right\">" + SLibUtils.DecimalFormatInteger.format(resultSetxPro.getInt(emp2[0])) + "</td>\n" +
                            "<td style=\"width: 50px;font-size:80%\" align=\"right\">" + SLibUtils.DecimalFormatPercentage2D.format(totalAnioAETH != 0 ? resultSetxPro.getInt(emp2[0]) / totalAnioAETH : 0) + "</td>\n" ;
                    i++;
                }
                while (i <= monthsTable.length) {
                    htmlTables += "<tr>\n" +
                            "<td>" + SLibUtils.textToHtml(monthsTable[i - 1]) + "</td>\n" +
                            "<td style=\"width: 150px\" align=\"right\">" + SLibUtils.DecimalFormatInteger.format(0) + "</td>\n" +
                            "<td style=\"width: 50px;font-size:80%\" align=\"right\">" + SLibUtils.DecimalFormatPercentage2D.format(0) + "</td>\n" +
                            "<td style=\"width: 150px\" align=\"right\">" + SLibUtils.DecimalFormatInteger.format(0) + "</td>\n" +
                            "<td style=\"width: 50px;font-size:80%\" align=\"right\">" + SLibUtils.DecimalFormatPercentage2D.format(0) + "</td>\n" ;
                    i++;
                }
                htmlTables += "<tr>\n" +
                        "<td style=\"font-weight: bold\">Total</td>\n" +
                        "<td style=\"font-weight: bold; width: 150px\" align=\"right\">" + SLibUtils.DecimalFormatInteger.format(totalAnioAME) + "</td>\n" +
                        "<td style=\" font-weight: bold; width: 50px;font-size:80%\" align=\"right\">" + SLibUtils.DecimalFormatPercentage2D.format(totalAnioAME != 0 ? 1 : 0) + "</td>\n" +
                        "<td style=\"font-weight: bold; width: 150px\" align=\"right\">" + SLibUtils.DecimalFormatInteger.format(totalAnioAETH) + "</td>\n" +
                        "<td style=\" font-weight: bold; width: 50px;font-size:80%\" align=\"right\">" + SLibUtils.DecimalFormatPercentage2D.format(totalAnioAETH != 0 ? 1 : 0) + "</td>\n" ;

                htmlTables += "</tr>\n";
                htmlTables += "</table>\n"
                    + "<br>\n";
            }
        }
        if (htmlTables.isEmpty()) {
            htmlTables += "<h3>No se encontro informacion para el año reportado.</h3>";
        }
        
        html += htmlTables;
        
        // Final del HTML
        
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
