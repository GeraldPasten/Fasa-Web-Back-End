package cl.api.karaf.autorizaciones.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class response {

	private String descripcion;
	private int codigo;
	private int id;
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	




}
