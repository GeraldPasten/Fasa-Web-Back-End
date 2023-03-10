package cl.api.base.karaf.convenios.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class roles {

	private String id_aplicacion;
	private String id_rol;
	private String nombre;
	private String menu_xml;
	private String vigencia;
	private String detalle;
	private String codigo;
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
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getMenu_xml() {
		return menu_xml;
	}
	public void setMenu_xml(String menu_xml) {
		this.menu_xml = menu_xml;
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
