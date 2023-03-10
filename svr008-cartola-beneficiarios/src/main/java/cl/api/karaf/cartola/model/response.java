package cl.api.karaf.cartola.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class response {

	private String Estado;
	private String Fecha;
	private int Farmacia;
	private int Id_receta;
	private String Direccion;
	private String Comuna;
	private int Boleta;
	private int Guia;
	private int SAP;
	private String Decripcion_producto;
	private int Cantidad;
	private int Precio;
	private int Descto;
	private int Bonificado;
	private int Copago;
	private int Total;
	private String Tipo;
	private String codigo;
	private String detalle;
	private String b64;
	
	


	public String getB64() {
		return b64;
	}

	public void setB64(String b64) {
		this.b64 = b64;
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

	public String getEstado() {
		return Estado;
	}

	public void setEstado(String estado) {
		Estado = estado;
	}

	public String getFecha() {
		return Fecha;
	}

	public void setFecha(String fecha) {
		Fecha = fecha;
	}

	public int getFarmacia() {
		return Farmacia;
	}

	public void setFarmacia(int farmacia) {
		Farmacia = farmacia;
	}

	public int getId_receta() {
		return Id_receta;
	}

	public void setId_receta(int id_receta) {
		Id_receta = id_receta;
	}

	public String getDireccion() {
		return Direccion;
	}

	public void setDireccion(String direccion) {
		Direccion = direccion;
	}

	public String getComuna() {
		return Comuna;
	}

	public void setComuna(String comuna) {
		Comuna = comuna;
	}

	public int getBoleta() {
		return Boleta;
	}

	public void setBoleta(int boleta) {
		Boleta = boleta;
	}

	public int getGuia() {
		return Guia;
	}

	public void setGuia(int guia) {
		Guia = guia;
	}

	public int getSAP() {
		return SAP;
	}

	public void setSAP(int sAP) {
		SAP = sAP;
	}

	public String getDecripcion_producto() {
		return Decripcion_producto;
	}

	public void setDecripcion_producto(String decripcion_producto) {
		Decripcion_producto = decripcion_producto;
	}

	public int getCantidad() {
		return Cantidad;
	}

	public void setCantidad(int cantidad) {
		Cantidad = cantidad;
	}

	public int getPrecio() {
		return Precio;
	}

	public void setPrecio(int precio) {
		Precio = precio;
	}

	public int getDescto() {
		return Descto;
	}

	public void setDescto(int descto) {
		Descto = descto;
	}

	public int getBonificado() {
		return Bonificado;
	}

	public void setBonificado(int bonificado) {
		Bonificado = bonificado;
	}

	public int getCopago() {
		return Copago;
	}

	public void setCopago(int copago) {
		Copago = copago;
	}

	public int getTotal() {
		return Total;
	}

	public void setTotal(int total) {
		Total = total;
	}

	public String getTipo() {
		return Tipo;
	}

	public void setTipo(String tipo) {
		Tipo = tipo;
	}

}
