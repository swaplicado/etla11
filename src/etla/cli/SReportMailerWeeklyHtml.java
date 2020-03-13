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
import java.util.Calendar;
import java.util.Date;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;

/**
 *
 * @author Isabel Servín
 */
public abstract class SReportMailerWeeklyHtml {

    public static String generateReportHtml(final Connection connection, final String reportType, final String mailSubject) throws Exception {
        // Define inicio y fin del reporte (hoy a menos 3 años)
        Date today = new Date();
        Date lastYear = SLibTimeUtils.addDate(today, -1, 0, 0);
        Date ancestorYear = SLibTimeUtils.addDate(today, -2, 0, 0);
        
        Calendar ancestorYearCalendar = Calendar.getInstance(); 
        ancestorYearCalendar.setTime(ancestorYear);
        Calendar todayCalendar = Calendar.getInstance(); 
        todayCalendar.setTime(today);
        Calendar lastYearCalendar = Calendar.getInstance();
        lastYearCalendar.setTime(lastYear);
        // Celdas donde se encuentan el nombre del producto y el ID producto
        int nomPro = 2; 
        int idPro = 1;
        // Celdas donde se encuentan el año, mes y el total de la consulta por producto
        int yearxPro = 2;
        int monthxPro = 3;
        int totxPro = 4;
        String monthsTable []= {"Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic","Total"};
        
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
        
        html += "<body>\n";
        html += "<h1>" + SLibUtils.textToHtml(mailSubject) + "</h1>\n";
        html += "Hora de corte: 12:00 hr" + "\n";
        
        
        // Consulta de productos
        Statement statement = connection.createStatement();
        String queryPro = "SELECT Pro_ID, Pro_Nombre FROM dba.Productos ORDER BY Pro_Nombre";
        ResultSet resultSetPro = statement.executeQuery(queryPro);
        Statement statementUsb = connection.createStatement();
        
        while (resultSetPro.next()){
            // Consulta que saca las cantidades mes por mes
            String sqlxPro = "SELECT Pro_ID, YEAR(Pes_FecHorSeg) as Anio, MONTH(Pes_FecHorSeg) as Mes, SUM(Pes_Neto) as Total "
                + "FROM dba.Pesadas "
                + "WHERE YEAR(Pes_FecHorSeg) >= YEAR(" + SLibUtils.DbmsDateFormatDate.format(ancestorYear) + ") "
                + "AND Pes_PesoPri - Pes_PesoSeg " + (reportType.equals(SReportMailerWeekly.REP_TYPE_IN) ? ">" : "<") + " 0 "
                + "AND Pro_ID = '" + resultSetPro.getString(idPro) + "' "
                + "GROUP BY Pro_ID, Anio, Mes "
                + "ORDER BY Mes , Anio DESC";
            ResultSet resultSetxPro = statementUsb.executeQuery(sqlxPro);
            
            // Creacion del array que contendrá los datos
            Double dataArray [][]= new Double[13][6];
            
            // Llenado de 0's:
            for (Double[] dataArray1 : dataArray) {
                for (int j = 0; j < dataArray1.length; j++) {
                    dataArray1[j] = 0.0;
                }
            }
            
            // Llenado el array con los datos de la consulta
            
            while (resultSetxPro.next()){
                if(resultSetxPro.getInt(yearxPro)==todayCalendar.get(Calendar.YEAR)){
                    dataArray[resultSetxPro.getInt(monthxPro)-1][0] = resultSetxPro.getDouble(totxPro);
                }
                if(resultSetxPro.getInt(yearxPro)==lastYearCalendar.get(Calendar.YEAR)){
                    dataArray[resultSetxPro.getInt(monthxPro)-1][2] = resultSetxPro.getDouble(totxPro);
                }
                if(resultSetxPro.getInt(yearxPro)==ancestorYearCalendar.get(Calendar.YEAR)){
                    dataArray[resultSetxPro.getInt(monthxPro)-1][4] = resultSetxPro.getDouble(totxPro);
                }
            }
                
            // Cálculo de totales por año
            
            double totYear = 0; 
            double totLastYear = 0; 
            double totAncestorYear = 0;
            
            for(int fila = 0; fila < dataArray.length; fila++){
                totYear += dataArray[fila][0];
                totLastYear += dataArray[fila][2];
                totAncestorYear += dataArray[fila][4];
                if(fila == dataArray.length-1){
                    dataArray[fila][0] = totYear;
                    dataArray[fila][2] = totLastYear;
                    dataArray[fila][4] = totAncestorYear;
                }
            }
            
            // Cálculo de porcentajes por mes
            
            for(int fila = 0; fila < dataArray.length; fila++){ 
                dataArray[fila][1] = (dataArray[fila][0])/totYear;
                dataArray[fila][3] = (dataArray[fila][2])/totLastYear;
                dataArray[fila][5] = (dataArray[fila][4])/totAncestorYear;
            }
            
            //Creación y llenado de la tabla HTML
            //Excluye las tablas totalmente vacias
            if (totYear != 0 || totLastYear != 0 || totAncestorYear != 0) {
                //Cabecera
                html += "<h3>" + SLibUtils.textToHtml(resultSetPro.getString(nomPro)) + "</h3>" +
                        "<table>\n" +
                        "    <tr>\n" +
                        "        <th style=\"width: 25px\" scope=\"row\" rowspan=\"2\">Mes</th>\n" +
                        "        <th style=\"width: 200px\" colspan=\"2\">" + SLibUtils.textToHtml(todayCalendar.get(Calendar.YEAR)+"") + "</th>\n" +
                        "        <th style=\"width: 200px\" colspan=\"2\">" + SLibUtils.textToHtml(lastYearCalendar.get(Calendar.YEAR)+"") + "</th>\n" +
                        "        <th style=\"width: 200px\" colspan=\"2\">" + SLibUtils.textToHtml(ancestorYearCalendar.get(Calendar.YEAR)+"") + "</th>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <th style=\"width: 150px\">kg</th>\n" +
                        "        <th style=\"width: 50px\">%</th>\n" +
                        "        <th style=\"width: 150px\">kg</th>\n" +
                        "        <th style=\"width: 50px\">%</th>\n" +
                        "        <th style=\"width: 150px\">kg</th>\n" +
                        "        <th style=\"width: 50px\">%</th>    \n" +
                        "    </tr>";
                // Cuerpo de la tabla
                for ( int fila = 0; fila < dataArray.length; fila++) {
                    if ( fila != dataArray.length-1 ){
                        html += "<tr>\n"
                                + "<td>" + SLibUtils.textToHtml(monthsTable[fila]) + "</td>\n";
                    }
                    else {
                        html += "<tr>\n"
                                + "<td style=\"font-weight: bold\">" + SLibUtils.textToHtml(monthsTable[fila]) + "</td>\n";
                    }
                    for (int columna = 0; columna < dataArray[fila].length; columna++) {
                        if (columna == 0 || columna == 2 || columna == 4) {
                            if ( fila != dataArray.length-1 ){
                                html += "<td style=\"width: 150px\" align=\"right\">" + SLibUtils.DecimalFormatInteger.format(dataArray[fila][columna]) + "</td>\n";
                            }
                            else {
                                html += "<td style=\"font-weight: bold; width: 150px\" align=\"right\">" + SLibUtils.DecimalFormatInteger.format(dataArray[fila][columna]) + "</td>\n";
                            }
                            if (dataArray[fila][columna] != 0) {
                                if ( fila != dataArray.length-1 ){
                                    html += "<td style=\"width: 50px;font-size:80%\" align=\"right\">" + SLibUtils.DecimalFormatPercentage2D.format(dataArray[fila][columna+1]) + "</td>\n";
                                }
                                else {
                                    html += "<td style=\" font-weight: bold; width: 50px;font-size:80%\" align=\"right\">" + SLibUtils.DecimalFormatPercentage2D.format(dataArray[fila][columna+1]) + "</td>\n";
                                }
                            }
                            else {
                                if ( fila != dataArray.length-1 ){
                                    html += "<td style=\"width: 50px; font-size:80%\" align=\"right\">" + SLibUtils.DecimalFormatPercentage2D.format(0) + "</td>\n";
                                }
                                else {
                                    html += "<td style=\"font-weight: bold; width: 50px; font-size:80%\" align=\"right\">" + SLibUtils.DecimalFormatPercentage2D.format(0) + "</td>\n";
                                }
                            }
                        }
                    }
                    html += "</tr>\n";
                }
                html += "</table>\n"
                        + "<br>\n";
            }
        }
        

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
