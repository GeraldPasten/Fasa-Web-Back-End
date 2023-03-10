package cl.api.base.karaf.convenios.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RolUsuario {

	private String id_aplicacion;
	private String id_usuario;
	private String id_rol;
	private String vigencia;
	private String detalle;
	private String codigo;
	private String id_recurso;

	public String getId_recurso() {
		return id_recurso;
	}

	public void setId_recurso(String id_recurso) {
		this.id_recurso = id_recurso;
	}

	public String getId_aplicacion() {
		return id_aplicacion;
	}

	public void setId_aplicacion(String id_aplicacion) {
		this.id_aplicacion = id_aplicacion;
	}

	public String getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getId_rol() {
		return id_rol;
	}

	public void setId_rol(String id_rol) {
		this.id_rol = id_rol;
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
