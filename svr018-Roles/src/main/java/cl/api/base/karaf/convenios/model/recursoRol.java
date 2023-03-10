package cl.api.base.karaf.convenios.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class recursoRol {

	private String id_aplicacion;
	private String id_rol;
	private String id_recurso;
	private String id_recursoPadre;
	private String codigo;
	private String detalle;

	public String getId_aplicacion() {
		return id_aplicacion;
	}

	public void setId_aplicacion(String id_aplicacion) {
		this.id_aplicacion = id_aplicacion;
	}

	public String getId_rol() {
		return id_rol;
	}

	public void setId_rol(String id_rol) {
		this.id_rol = id_rol;
	}

	public String getId_recurso() {
		return id_recurso;
	}

	public void setId_recurso(String id_recurso) {
		this.id_recurso = id_recurso;
	}

	public String getId_recursoPadre() {
		return id_recursoPadre;
	}

	public void setId_recursoPadre(String id_recursoPadre) {
		this.id_recursoPadre = id_recursoPadre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

}
