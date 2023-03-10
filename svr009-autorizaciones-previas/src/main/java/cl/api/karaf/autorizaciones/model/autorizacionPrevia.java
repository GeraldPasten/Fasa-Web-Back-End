package cl.api.karaf.autorizaciones.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class autorizacionPrevia {

	private String credencial;
	private String codigoPersona;
	private String campo;
	private String LAPB_VALOR_CAMPO;
	private String LAPB_INCLUIR_EXCLUIR;
	private String LAPB_FECHA_INICIO;
	private String LAPB_FECHA_TERMINO;
	private String LAPB_Q_ENVASES_MENSUAL;
	private String LAPB_ACUMULADO_ENVASES_MENSUAL;
	private String LAPB_416_ID_AP;
	private String PLA_524_PLAN_ID;
	private String LMA_ID_MEDICO;
	private String LAPB_MEDICO_INCEXC;
	private String detalle;
	private String codigo;
	
	

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

	public String getCredencial() {
		return credencial;
	}

	public void setCredencial(String credencial) {
		this.credencial = credencial;
	}

	public String getCodigoPersona() {
		return codigoPersona;
	}

	public void setCodigoPersona(String codigoPersona) {
		this.codigoPersona = codigoPersona;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getLAPB_VALOR_CAMPO() {
		return LAPB_VALOR_CAMPO;
	}

	public void setLAPB_VALOR_CAMPO(String lAPB_VALOR_CAMPO) {
		LAPB_VALOR_CAMPO = lAPB_VALOR_CAMPO;
	}

	public String getLAPB_INCLUIR_EXCLUIR() {
		return LAPB_INCLUIR_EXCLUIR;
	}

	public void setLAPB_INCLUIR_EXCLUIR(String lAPB_INCLUIR_EXCLUIR) {
		LAPB_INCLUIR_EXCLUIR = lAPB_INCLUIR_EXCLUIR;
	}

	public String getLAPB_FECHA_INICIO() {
		return LAPB_FECHA_INICIO;
	}

	public void setLAPB_FECHA_INICIO(String lAPB_FECHA_INICIO) {
		LAPB_FECHA_INICIO = lAPB_FECHA_INICIO;
	}

	public String getLAPB_FECHA_TERMINO() {
		return LAPB_FECHA_TERMINO;
	}

	public void setLAPB_FECHA_TERMINO(String lAPB_FECHA_TERMINO) {
		LAPB_FECHA_TERMINO = lAPB_FECHA_TERMINO;
	}

	public String getLAPB_Q_ENVASES_MENSUAL() {
		return LAPB_Q_ENVASES_MENSUAL;
	}

	public void setLAPB_Q_ENVASES_MENSUAL(String lAPB_Q_ENVASES_MENSUAL) {
		LAPB_Q_ENVASES_MENSUAL = lAPB_Q_ENVASES_MENSUAL;
	}

	public String getLAPB_ACUMULADO_ENVASES_MENSUAL() {
		return LAPB_ACUMULADO_ENVASES_MENSUAL;
	}

	public void setLAPB_ACUMULADO_ENVASES_MENSUAL(String lAPB_ACUMULADO_ENVASES_MENSUAL) {
		LAPB_ACUMULADO_ENVASES_MENSUAL = lAPB_ACUMULADO_ENVASES_MENSUAL;
	}

	public String getLAPB_416_ID_AP() {
		return LAPB_416_ID_AP;
	}

	public void setLAPB_416_ID_AP(String lAPB_416_ID_AP) {
		LAPB_416_ID_AP = lAPB_416_ID_AP;
	}

	public String getPLA_524_PLAN_ID() {
		return PLA_524_PLAN_ID;
	}

	public void setPLA_524_PLAN_ID(String pLA_524_PLAN_ID) {
		PLA_524_PLAN_ID = pLA_524_PLAN_ID;
	}

	public String getLMA_ID_MEDICO() {
		return LMA_ID_MEDICO;
	}

	public void setLMA_ID_MEDICO(String lMA_ID_MEDICO) {
		LMA_ID_MEDICO = lMA_ID_MEDICO;
	}

	public String getLAPB_MEDICO_INCEXC() {
		return LAPB_MEDICO_INCEXC;
	}

	public void setLAPB_MEDICO_INCEXC(String lAPB_MEDICO_INCEXC) {
		LAPB_MEDICO_INCEXC = lAPB_MEDICO_INCEXC;
	}

}
