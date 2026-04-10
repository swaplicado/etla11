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
        int maxItemsByTable = 20; // ajustar el tamaño maximo de items por tabla
        
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
        html += "<title>"+ mailSubject +"</title>\n";
        html += "<style>\n" +
            "body { font-size: 100%; font-family: sans-serif; }" +
            "table { border-collapse: collapse; font-size: 0.80em; font-family: sans-serif; table-layout: fixed; width: 100%; }" +
            "table, th, td { border: 1px solid black; }" +
            "h1 {\n" + 
                "font-size: 2.00em;\n" +
                "font-family: sans-serif;\n" +
                "}\n" +
            "h2 {" +
                "font-size: 1.75em;" +
                "font-family: sans-serif;" + 
                "} " +
            
            "th {\n" +
            "    border: 1px solid #000;\n" +
            "    padding: 2px;\n" +
            "    text-align: center;" +
            "    overflow-wrap: break-word;\n" +
            "    white-space: nowrap;\n" +
            "    word-break: keep-all;\n" +
            "    font-size: 0.85em;\n" +
            "}\n" +
            "td {\n"
            + " padding: 2px;"
            + " white-space: nowrap;"
            + " word-break: keep-all;"
            + " font-size: 0.80em;"
            + "}"
                
                /* Columna MES */
            + "td.col-month, th.col-month {"
            + " width: 45px;"
            + " text-align: center;"
            + " font-size: 0.80em;"
            + "}"

                /* Encabezado del item (Nombre) */
            + ".item-header {"
            + " width: 150px;"
            + " word-break: break-word;"
            + " font-weight: bold;"
            + " font-size: 0.85em;"
            + " text-align: center;"
            + " white-space: normal;"
            + " overflow-wrap: anywhere;"
            + "}"
                
                /* kg */
            + "td.col-kg, th.col-kg {"
            + " width: 65px;"
            + " text-align: right;"
            + " font-size: 0.80em;"
            + "}"

            /* % */
            + "td.col-pct, th.col-pct {"
            + " width: 28px;"
            + " text-align: center;"
            + " font-size: 0.75em;"
            + "}"

            /* Empresa */
            + "td.col-emp, th.col-emp {"
            + " width: 65px;"
            + " text-align: center;"
            + " font-size: 0.85em;"
            + "}" +
            
            "address {\n" +
            "    margin-top: 1em;\n" +
            "    padding-top: 1em;\n" +
            "    border-top: thin dotted;"+
            "    font-size: 82.5% }\n" +
                
            "caption {\n" +
            "    padding: 0.3em;\n" +
            "    color: #fff;\n" +
            "    background: #000;\n" +
            "}\n" +
            "th {\n" +
            "   background: #eee;\n" +
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
        html += "<br><br>\n";
        
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
        
        // Obtención de datos para la tablas:
        
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
                        
                        if (valsEmp1[mes - 1] != 0 || valsEmp2[mes - 1] != 0) {
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
        
        // Ver si llegaron mas de 20 items, partir la tabla en 2:
        
        int totalItems = itemNames.size();
        int half = totalItems > maxItemsByTable ? (int) Math.ceil(totalItems / 2.0) : totalItems;

        // Primera mitad (siempre la mayor)
        int startA = 0;
        int endA = half;

        // Segunda mitad
        int startB = half;
        int endB = totalItems;
        
        // Tabla dinámica principal:
                
        if (itemNames.size() > maxItemsByTable) {
            
            htmlTable += "<table>\n";
            
            htmlTable += buildTableSection(
                    startA, endA,
                    itemNames, itemValsEmp1, itemValsEmp2,
                    itemTotalAME, itemTotalAETH,
                    emp1, emp2,
                    monthsTable);

            htmlTable += "</table><br>\n";
            htmlTable += "<br><table>\n";

            htmlTable += buildTableSection(
                    startB, endB,
                    itemNames, itemValsEmp1, itemValsEmp2,
                    itemTotalAME, itemTotalAETH,
                    emp1, emp2,
                    monthsTable);

            htmlTable += "</table>\n";


        } else {
            
            html += "<table>\n";

            // Tabla normal
            htmlTable += buildTableSection(
                    0, itemNames.size(),
                    itemNames, itemValsEmp1, itemValsEmp2,
                    itemTotalAME, itemTotalAETH,
                    emp1, emp2,
                    monthsTable);
            
            htmlTable += "</table>\n";

        }
        
        if (!hasData) {
            htmlTable += "<h3>No se encontró información para el año reportado.</h3>";
        }

        html += htmlTable + "<br>\n";
        
        // Final del HTML
        
        html += "" +
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

    private static String buildTableSection(
            int start, 
            int end,
            List<String> itemNames,
            List<int[]> itemValsEmp1,
            List<int[]> itemValsEmp2,
            List<Double> itemTotalAME,
            List<Double> itemTotalAETH,
            String[] emp1,
            String[] emp2,
            String[] monthsTable){
        
        String html = "";
        int colMesRep = 5; // cada cuando vuelve a salir la columna mes
        
        // Encabezados superiores de la tabla:

        html = "<tr>\n";
        
        for (int item = start; item < end; item++) {
            if ((item - start) % colMesRep  == 0) {   // cada 3 items vuelve a salir la columna "mes"
                html += "<th class='col-month' scope='row' rowspan='3'>Mes</th>\n";
            }

            html += "<th colspan='2' class='item-header'>" + itemNames.get(item) + "</th>\n";
        }

        html += "</tr>\n";
        html += "<tr>\n";

        for (int item = start; item < end; item++) {
            html += "<th colspan='1' class='col-emp'> " + emp1[1] + "\n" + "</th><th colspan='1' class='col-emp'>" + emp2[1] + "\n" + "</th>";
        }
        
        html += "</tr>\n";
        html += "<tr>\n";

        for (int item = start; item < end; item++) {
            html += "<th class='col-kg'>kg</th>"
                    //+ "<th class='col-pct'>%</th>"
                    + "<th class='col-kg'>kg</th>";
                    //+ "<th class='col-pct'>%</th>\n";
        }

        html += "</tr>";

        // Generación de las filas mes x mes:
                
        for (int m = 0; m < monthsTable.length; m++) {
            html += "<tr>\n";

            for (int item = start; item < end; item++) {

                if ((item - start) % colMesRep == 0) {
                    html += "<td class='col-month'>" + monthsTable[m] + "</td>";
                }

                int v1 = itemValsEmp1.get(item)[m];
                int v2 = itemValsEmp2.get(item)[m];

                html += "<td class='col-kg' align='right'>" + SLibUtils.DecimalFormatInteger.format(v1) + "</td>";
                //htmlTable += "<td class='col-pct' align='right'>" + SLibUtils.DecimalFormatPercentage2D.format(itemTotalAME.get(item) != 0 ? (double)v1 / itemTotalAME.get(item) : 0) + "</td>";

                html += "<td class='col-kg' align='right'>" + SLibUtils.DecimalFormatInteger.format(v2) + "</td>";
//                htmlTable += "<td class='col-pct' align='right'>" + SLibUtils.DecimalFormatPercentage2D.format(itemTotalAETH.get(item) != 0 ? (double)v2 / itemTotalAETH.get(item) : 0) + "</td>";
            }

            html += "</tr>\n";
        }
        
        // Fila final de totales:

        html += "<tr>";

        for (int item = start; item < end; item++) {

            if ((item - start) % colMesRep == 0) {
                html += "<td class='col-month' style='font-weight:bold;'>Total</td>";
            }

            double totAME = itemTotalAME.get(item);
            double totAETH = itemTotalAETH.get(item);

            html += "<td class='col-kg' align='right' style='font-weight:bold;'>" 
                    + SLibUtils.DecimalFormatInteger.format(totAME) + "</td>";

 //           htmlTable += "<td class='col-pct' align='right' style='font-weight:bold;'>" 
   //                 + SLibUtils.DecimalFormatPercentage2D.format(totAME != 0 ? 1 : 0) + "</td>";

            html += "<td class='col-kg' align='right' style='font-weight:bold;'>" 
                    + SLibUtils.DecimalFormatInteger.format(totAETH) + "</td>";

 //           htmlTable += "<td class='col-pct' align='right' style='font-weight:bold;'>" 
   //                 + SLibUtils.DecimalFormatPercentage2D.format(totAETH != 0 ? 1 : 0) + "</td>";
        }

        html += "</tr>";
        
        return html;
    }

}
