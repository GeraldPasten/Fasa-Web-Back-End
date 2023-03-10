package cl.api.karaf.convenios.model;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class response1 {

 private int codigoRespuesta;
 private String detalleRespuest;
 
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
 

 
 
 
    
}
