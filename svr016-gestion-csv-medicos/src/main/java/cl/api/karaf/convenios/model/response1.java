package cl.api.karaf.convenios.model;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class response1 {

 private int codigoRespuesta;
 private String detalleRespuest;
 private String descripcion;
 
public int getCodigoRespuesta() {
	return codigoRespuesta;
}
public void setCodigoRespuesta(int codigoRespuesta) {
	this.codigoRespuesta = codigoRespuesta;
}
public String getDetalleRespuest() {
	return detalleRespuest;
}
public void setDetalleRespuest(String detalleRespuest) {
	this.detalleRespuest = detalleRespuest;
}
public String getDescripcion() {
	return descripcion;
}
public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}
 

 
 

 


 

 
 
 
    
}
