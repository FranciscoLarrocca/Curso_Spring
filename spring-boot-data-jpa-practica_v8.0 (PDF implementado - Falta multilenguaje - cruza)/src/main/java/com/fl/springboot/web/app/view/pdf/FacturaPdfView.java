package com.fl.springboot.web.app.view.pdf;

import java.awt.Color;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.fl.springboot.web.app.models.entity.Factura;
import com.fl.springboot.web.app.models.entity.ItemFactura;
import com.lowagie.text.Anchor;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

//Seccion PDF view:
@Component("factura/ver") //Se especifica la ruta para representar la factura en PDF.
public class FacturaPdfView extends AbstractPdfView{

	
	//Recibir los parametros enviados por la vista:
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Factura factura = (Factura) model.get("factura");
		
		//Agregar imagen al pdf:
		Image image1 = Image.getInstance("C:\\Users\\Fran\\Documents\\Curso_Spring\\spring-boot-data-jpa-practica_01\\src\\main\\resources\\static\\images\\spring-logo.png");
		image1.setAlignment(Element.ALIGN_CENTER);
		image1.scaleAbsolute(180, 50);
		//Add to document:
		document.add(image1);
		
		PdfPTable tabla_cliente = new PdfPTable(1);
		tabla_cliente.setSpacingBefore(10);
		tabla_cliente.setSpacingAfter(20);
		
		PdfPCell cell = null;
		
		cell = new PdfPCell(new Phrase("Datos del cliente: "));
		cell.setBackgroundColor(new Color(184, 218, 255));
		cell.setPadding(8f);
		tabla_cliente.addCell(cell);
		tabla_cliente.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
		tabla_cliente.addCell(factura.getCliente().getEmail());
	
		PdfPTable tabla_factura = new PdfPTable(1);
		tabla_factura.setSpacingAfter(20);
		
		cell = new PdfPCell(new Phrase("Datos de la factura: "));
		cell.setBackgroundColor(new Color(195, 230, 203));
		cell.setPadding(8f);
		tabla_factura.addCell(cell);
		
		tabla_factura.addCell("Folio: "+ factura.getId());
		tabla_factura.addCell("Descripción: "+ factura.getDescripcion());
		
		//Si la observación no es nula:
		if(factura.getObservacion() != null) {
		tabla_factura.addCell("Observación: "+ factura.getObservacion());
		}else {
		tabla_factura.addCell("Observación: - No hay observación -");
		}
		
		tabla_factura.addCell("Fecha: "+ factura.getCreateAt());
	
		document.add(tabla_cliente);
		document.add(tabla_factura);
	
		PdfPTable tabla_itemsFactura = new PdfPTable(4); //4 columnas
		tabla_itemsFactura.setWidths(new float [] {2.5f, 1, 1, 1});
		tabla_itemsFactura.addCell("Producto: ");
		tabla_itemsFactura.addCell("Precio: ");
		tabla_itemsFactura.addCell("Cantidad: ");
		tabla_itemsFactura.addCell("Total: ");
		
		for(ItemFactura item: factura.getItems()) {
			tabla_itemsFactura.addCell(item.getProducto().getNombre());
			tabla_itemsFactura.addCell("$" + item.getProducto().getPrecio().toString());
			
			//Celda "Cantidad" alineada al centro:
			cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			tabla_itemsFactura.addCell(cell);
			
			tabla_itemsFactura.addCell("$" + item.calcularImporte().toString());
		}
		
		cell = new PdfPCell(new Phrase("Total:   "));
		cell.setColspan(3);
		cell.setBackgroundColor(new Color(62, 213, 21));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		tabla_itemsFactura.addCell(cell);
		tabla_itemsFactura.addCell("$" + factura.getTotal().toString());
		
		document.add(tabla_itemsFactura);
		
		//Agregar Links a pdf:		
		Paragraph creditos = new Paragraph();
		creditos.setSpacingBefore(40);
		creditos.setAlignment(Element.ALIGN_CENTER);
		creditos.add(new Phrase("- Este proyecto, fue desarrollado con Spring Boot y Thymeleaf -"));
        
        
        Paragraph creditos_profesor = new Paragraph();
        creditos_profesor.setSpacingBefore(20);
        creditos_profesor.add(new Phrase("Profesor: "));
        Anchor link_1 = new Anchor("Andrés Guzman");
        link_1.setReference("https://cl.linkedin.com/in/andresguzf");
        creditos_profesor.add(link_1);       
        
        Paragraph creditos_alumno = new Paragraph();
        creditos_alumno.add(new Phrase("Alumno: "));
        Anchor link_2 = new Anchor("Francisco Larrocca");
        link_2.setReference("https://www.linkedin.com/in/franciscolarrocca/");
        creditos_alumno.add(link_2);       
        
        document.add(creditos);
        document.add(creditos_profesor);
        document.add(creditos_alumno);
		
	}	

}
