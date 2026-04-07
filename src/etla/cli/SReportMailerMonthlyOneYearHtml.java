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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sa.lib.SLibUtils;

/**
 *
 * @author Isabel Servín, Rodrigo Ayala
 */
public abstract class SReportMailerMonthlyOneYearHtml {
    
    public static String generateReportHtml(final Connection connection, final String reportType, final String companies, final String mailSubject) throws Exception {
        Date today = new Date();
        boolean hasData = false;
        
        // Celdas donde se encuentan el nombre del usuario y del producto y el ID del usuario y del producto:
        
        int nomPro = 2; 
        int idPro = 1;
        
        String monthsTable []= {"Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"};
        
        // HTML:
        
        String headHtmlTable = "";
        String htmlTable = "";
        String html = "<html>\n";
        
        // Cabeza del HTML:
        
        html += "<head>\n";
        html += "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n";
        html += "<title>"+ mailSubject +"</title>\n";
        html += "<style type=\"text/css\">\n" +
            "body {\n" +
            "    padding-left: 2em;\n" +
            "    padding-right: 2em;\n" +
            "    font-family:  \"Times New Roman\",\n" +
            "        Times, serif;\n" +
            "    color: black;\n" +
            "    background-color: whitesmoke}\n" +
            "h1, h2 {\n" +
            "    font-family: Helvetica, Geneva, Arial,\n" +
            "        SunSans-Regular, sans-serif }\n" +
            "address {\n" +
            "    margin-top: 1em;\n" +
            "    padding-top: 1em;\n" +
            "    border-top: thin dotted;\n"+
            "    font-size: 62.5% }\n" +
            "table {\n" +
            "    border: 1px solid #000;\n" +
            "    border-collapse: collapse;\n" +
            "    font-size: 14px;\n" +
            "    width:auto;" +
            "}\n" +
            "th, td {\n" +
            "    vertical-align: top;\n" +
            "    border: 1px solid #000;\n" +
            "    border-collapse: collapse;\n" +
            "    padding: 0.3em;\n" +
            "    caption-side: bottom;\n"+
            "    font-family:  \"Times New Roman\";" +
            "}\n" +
            "caption {\n" +
            "    padding: 0.3em;\n" +
            "    color: #fff;\n" +
            "    background: #000;\n" +
            "}\n" +
            "th {\n" +
            "   background: #eee;\n" +
            "}\n" +
            ".item-header {\n" +
            "    background-color: #ddd;\n" +
            "    min-width: 250px;\n" +
            "    font-weight: bold;\n" +
            "    text-align: center;\n" +
            "    white-space: nowrap;\n}\n" +
            "@media only screen and (max-width: 600px) {\n" +   // dispositivos móviles
            "    table {\n" +
            "        font-size: 20px !important;\n" +
            "    }\n" +
            "    th, td {\n" +
            "        padding: 0.5em !important;\n" +
            "        white-space: nowrap;\n" +
            "    }\n" +
            "    .item-header {\n" +
            "        min-width: 300px !important;\n" +
            "        font-size: 20px !important;\n" +
            "    }\n" +
            "}\n" +
            "</style>";
        
        html += "</head>\n";
        
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
        html += "<br>\n";
        
        // Consulta de productos:
        
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
        
        // Parametros donde se guardarán todos los items y sus datos:
        
        List<String> itemIds = new ArrayList<>();
        List<String> itemNames = new ArrayList<>();
        List<int[]> itemValsEmp1 = new ArrayList<>();
        List<int[]> itemValsEmp2 = new ArrayList<>();
        List<Double> itemTotalAME = new ArrayList<>();
        List<Double> itemTotalAETH = new ArrayList<>();
        
        // Tabla dinámica principal:
                
        html += "<table style='width: max-content;'>\n";

        while (resultSetPro.next()) {
            
            double totalAnioAeth = 0;
            double totalAnioAme = 0;
            
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
                    totalAnioAeth = resultSetTotalAnio.getDouble("AETH");
                    totalAnioAme = resultSetTotalAnio.getDouble(emp1[0]);
                }
            }
            
            if (totalAnioAeth != 0 || totalAnioAme != 0){
                itemIds.add(resultSetPro.getString("Pro_ID"));
                itemNames.add(resultSetPro.getString("Pro_Nombre"));
            
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
               
                // Crear un arreglo temporal para los valores por mes:
                
                int[] valsEmp1 = new int[monthsTable.length];
                int[] valsEmp2 = new int[monthsTable.length];

                while (resultSetxPro.next()) {
                    int mes = resultSetxPro.getInt("Mes");
                    if (mes >= 1 && mes <= monthsTable.length) {
                        valsEmp1[mes - 1] = resultSetxPro.getInt(emp1[0]);
                        valsEmp2[mes - 1] = resultSetxPro.getInt(emp2[0]);
                        
                        if (valsEmp1 != null || valsEmp2 != null) {
                            hasData = true;
                        }
                    }
                }
                
                itemValsEmp1.add(valsEmp1);
                itemValsEmp2.add(valsEmp2);
                itemTotalAME.add(totalAnioAme);
                itemTotalAETH.add(totalAnioAeth);
            }
        }
        
        // Encabezados superiores de la tabla:

        headHtmlTable = "<tr>\n";

        for (int item = 0; item < itemNames.size(); item++) {
            if (item % 3 == 0) {   // cada 3 items vuelve a salir la columna "mes"
                headHtmlTable += "<th style=\"width: 25px\" scope=\"row\" rowspan=\"3\">Mes</th>\n";
            }

            headHtmlTable += "<th colspan='4' class='item-header'>" + itemNames.get(item) + "</th>\n";
        }

        headHtmlTable += "</tr>\n";
        headHtmlTable += "<tr>\n";

        for (int i = 0; i < itemNames.size(); i++) {
            headHtmlTable += "<th colspan='2'>" + emp1[1] + "\n" + "</th><th colspan='2'>" + emp2[1] + "\n" + "</th>";
        }
        
        headHtmlTable += "</tr>\n";
        headHtmlTable += "<tr>\n";

        for (int i = 0; i < itemNames.size(); i++) {
            headHtmlTable += "<th>kg</th><th>%</th><th>kg</th><th>%</th>\n";
        }

        headHtmlTable += "</tr>";

        // Generación de las filas mes x mes:
                
        for (int m = 0; m < monthsTable.length; m++) {
            htmlTable += "<tr>\n";

            for (int item = 0; item < itemNames.size(); item++) {

                if (item % 3 == 0) {
                    htmlTable += "<td>" + monthsTable[m] + "</td>";
                }

                int v1 = itemValsEmp1.get(item)[m];
                int v2 = itemValsEmp2.get(item)[m];

                htmlTable += "<td align='right'>" + SLibUtils.DecimalFormatInteger.format(v1) + "</td>";
                htmlTable += "<td align='right'>" + SLibUtils.DecimalFormatPercentage2D.format(itemTotalAME.get(item) != 0 ? (double)v1 / itemTotalAME.get(item) : 0) + "</td>";

                htmlTable += "<td align='right'>" + SLibUtils.DecimalFormatInteger.format(v2) + "</td>";
                htmlTable += "<td align='right'>" + SLibUtils.DecimalFormatPercentage2D.format(itemTotalAETH.get(item) != 0 ? (double)v2 / itemTotalAETH.get(item) : 0) + "</td>";
            }

            htmlTable += "</tr>\n";
        }
        
        // Fila final de totales:

        htmlTable += "<tr>";

        for (int item = 0; item < itemNames.size(); item++) {

            if (item % 3 == 0) {
                htmlTable += "<td style='font-weight:bold;'>Total</td>";
            }

            double totAME = itemTotalAME.get(item);
            double totAETH = itemTotalAETH.get(item);

            htmlTable += "<td align='right' style='font-weight:bold;'>" 
                    + SLibUtils.DecimalFormatInteger.format(totAME) + "</td>";

            htmlTable += "<td align='right' style='font-weight:bold;'>" 
                    + SLibUtils.DecimalFormatPercentage2D.format(totAME != 0 ? 1 : 0) + "</td>";

            htmlTable += "<td align='right' style='font-weight:bold;'>" 
                    + SLibUtils.DecimalFormatInteger.format(totAETH) + "</td>";

            htmlTable += "<td align='right' style='font-weight:bold;'>" 
                    + SLibUtils.DecimalFormatPercentage2D.format(totAETH != 0 ? 1 : 0) + "</td>";
        }

        htmlTable += "</tr>";
        
        if (!hasData) {
            htmlTable += "<h3>No se encontró información para el año reportado.</h3>";
        }

        html += headHtmlTable + htmlTable + "</table>\n";
        
        // Final del HTML
        
        html += "<hr>" +
                "<address>" +
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
                "</p>" +
                "</address>";
        
        html += "</body>\n";
        html += "</html>";
        
        return html;
    }
}
