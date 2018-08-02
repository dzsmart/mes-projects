/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;
import java.awt.Color;
import java.math.BigDecimal;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.examples.Templates;
import static net.sf.dynamicreports.examples.Templates.boldCenteredStyle;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author Amine
 */
public class ABSReports {
    private JasperReportBuilder myReport = null;
    
    public ABSReports() {
        D3DQtyReport("22-05-2017");
    }
    
    private JRDataSource createDataSourceD3D() {
        DRDataSource dataSource = new DRDataSource("item", "quantity", "unitprice");
        dataSource.add("Tablet", 350, new BigDecimal(300));
        dataSource.add("Laptop", 300, new BigDecimal(500));
        dataSource.add("Smartphone", 450, new BigDecimal(250));
        return dataSource;
    }
    
    private void D3DQtyReport(String reportDate) {
        StyleBuilder titleStyle = stl.style(boldCenteredStyle)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setFontSize(15);
        FontBuilder boldFont = stl.fontArialBold().setFontSize(12);
        StyleBuilder boldStyle         = stl.style().bold();
        StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
        StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle)
                .setBorder(stl.pen1Point())
                .setBackgroundColor(Color.LIGHT_GRAY);
        TextColumnBuilder<String> itemColumn = col.column("Item", "item", type.stringType());
        TextColumnBuilder<Integer> quantityColumn = col.column("Quantity", "quantity", type.integerType());
        TextColumnBuilder<BigDecimal> unitPriceColumn = col.column("Unit price", "unitprice", type.bigDecimalType());
	
        
        try {
            myReport = report()//create new report design
                    .setColumnTitleStyle(columnTitleStyle)
                    .highlightDetailEvenRows()
                    
	            .columns(itemColumn, quantityColumn, unitPriceColumn)
	            .title(//shows report title
                            cmp.horizontalList()
                                    .add(
                                            cmp.image(getClass().getResourceAsStream("/img/AbouSofiane.png")).setFixedDimension(96, 65)
                                            //cmp.text("©").setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.LEFT).setFixedDimension(15, 96),
                                            ))
                                    //.add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))
                    .title(//shows report title
                            cmp.horizontalList()
                                    .add(cmp.text("Bar3DChart").setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.CENTER)))
                    .title(
                            cmp.horizontalList()
                                    .add(cmp.text("Le : "+reportDate).setStyle(stl.style(boldStyle)).setHorizontalAlignment(HorizontalAlignment.RIGHT))
                                    .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))
	            .summary(
	                  cht.bar3DChart()
	                  .setTitle("Bar 3D chart")
	                  .setTitleFont(boldFont)
	                  .setCategory(itemColumn)
	                  .series(
	                     cht.serie(quantityColumn), cht.serie(unitPriceColumn))
	                  .setCategoryAxisFormat(
	                     cht.axisFormat().setLabel("Item")))
	            .pageFooter(Templates.footerComponent)
	            .setDataSource(createDataSourceD3D());
            JasperPrint reportPrint = myReport.toJasperPrint();
            JasperViewer reportViewer = new JasperViewer(DefaultJasperReportsContext.getInstance(), reportPrint, false, null, null);
            reportViewer.setTitle("Repports Etps. Abousofiane");
            reportViewer.setVisible(true);
        } catch (DRException e) {
            e.printStackTrace();
        }
    }
    
    private void QtyReport(String reportDate) {
        StyleBuilder titleStyle = stl.style(boldCenteredStyle)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setFontSize(15);
        FontBuilder boldFont = stl.fontArialBold().setFontSize(12);
        StyleBuilder boldStyle         = stl.style().bold();
        StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
        StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle)
                .setBorder(stl.pen1Point())
                .setBackgroundColor(Color.LIGHT_GRAY);
        TextColumnBuilder<String> yearColumn = col.column("Year", "year", type.stringType());
	TextColumnBuilder<String> quarterColumn = col.column("Quarter", "quarter", type.stringType());
	TextColumnBuilder<Integer> stock1Column = col.column("Stock 1", "stock1", type.integerType());
	TextColumnBuilder<Integer> stock2Column = col.column("Stock 2", "stock2", type.integerType());
	TextColumnBuilder<Integer> stock3Column = col.column("Stock 3", "stock3", type.integerType());
	
        
        try {
            myReport = report()//create new report design
                    .setColumnTitleStyle(columnTitleStyle)
                    .highlightDetailEvenRows()
                    
	            .columns(yearColumn, quarterColumn, stock1Column, stock2Column, stock3Column)
	            .title(//shows report title
                            cmp.horizontalList()
                                    .add(
                                            cmp.image(getClass().getResourceAsStream("/img/AbouSofiane.png")).setFixedDimension(96, 65)
                                            //cmp.text("©").setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.LEFT).setFixedDimension(15, 96),
                                            ))
                                    //.add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))
                    .title(//shows report title
                            cmp.horizontalList()
                                    .add(cmp.text("Analyse Physique-Chimique").setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.CENTER)))
                    .title(
                            cmp.horizontalList()
                                    .add(cmp.text("Le : "+reportDate).setStyle(stl.style(boldStyle)).setHorizontalAlignment(HorizontalAlignment.RIGHT))
                                    .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))
	            .summary(
	               cht.groupedStackedBarChart()
	                  .setTitle("Grouped stacked bar chart")
	                  .setTitleFont(boldFont)
	                  .setCategory(yearColumn)
	                  .series(
	                     cht.groupedSerie(stock1Column).setGroup(quarterColumn),
	                     cht.groupedSerie(stock2Column).setGroup(quarterColumn),
	                     cht.groupedSerie(stock3Column).setGroup(quarterColumn)))
	            .pageFooter(Templates.footerComponent)
	            .setDataSource(createDataSource());
            JasperPrint reportPrint = myReport.toJasperPrint();
            JasperViewer reportViewer = new JasperViewer(DefaultJasperReportsContext.getInstance(), reportPrint, false, null, null);
            reportViewer.setTitle("Repports Etps. Abousofiane");
            reportViewer.setVisible(true);
        } catch (DRException e) {
            e.printStackTrace();
        }
    }
	 
	   private JRDataSource createDataSource() {
	      DRDataSource dataSource = new DRDataSource("year", "quarter", "stock1", "stock2", "stock3");
	      dataSource.add("2010", "Q1", 80, 25, 18);
	      dataSource.add("2010", "Q2", 98, 78, 22);
	      dataSource.add("2010", "Q3", 50, 10, 89);
	      dataSource.add("2010", "Q4", 121, 40, 43);
	      dataSource.add("2011", "Q1", 103, 120, 34);
	      dataSource.add("2011", "Q2", 190, 95, 22);
	      dataSource.add("2011", "Q3", 43, 109, 51);
	      dataSource.add("2011", "Q4", 80, 88, 40);
	      return dataSource;
	   }
	 
	   public static void main(String[] args) {
	      new ABSReports();
	   }
}
