package cl.api.base.karaf.convenios.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class recursos {

	private String id_recursos;
	private String id_aplicacion;
	private String nombre_logico;
	private String nombre_fisico;
	private String clase;
	private String vigencia;
	private String detalle;
	private String codigo;
	public String getId_recursos() {
		return id_recursos;
	}
	public void setId_recursos(String id_recursos) {
		this.id_recursos = id_recursos;
	}
	public String getId_aplicacion() {
		return id_aplicacion;
	}
	public void setId_aplicacion(String id_aplicacion) {
		this.id_aplicacion = id_aplicacion;
	}
	public String getNombre_logico() {
		return nombre_logico;
	}
	public void setNombre_logico(String nombre_logico) {
		this.nombre_logico = nombre_logico;
	}
	public String getNombre_fisico() {
		return nombre_fisico;
	}
	public void setNombre_fisico(String nombre_fisico) {
		this.nombre_fisico = nombre_fisico;
	}
	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}
	public String getVigencia() {
		return vigencia;
	}
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	

}
