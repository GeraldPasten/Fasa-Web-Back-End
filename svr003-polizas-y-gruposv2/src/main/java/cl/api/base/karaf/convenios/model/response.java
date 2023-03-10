package cl.api.base.karaf.convenios.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class response {
    
    private String grupoAhumada;
    private String nombrePoliza;
    private String codigoPoliza;
    private int polizaAceptaBioequivalente;
    private String rutEmpresa;
    private String terminoBeneficio;
    private String cuentaLiquidador;
    private String estadoPolizaAhumada;
    
    public String getGrupoAhumada() {
        return grupoAhumada;
    }
    public void setGrupoAhumada(String grupoAhumada) {
        this.grupoAhumada = grupoAhumada;
    }
    public String getNombrePoliza() {
        return nombrePoliza;
    }
    public void setNombrePoliza(String nombrePoliza) {
        this.nombrePoliza = nombrePoliza;
    }
    public String getCodigoPoliza() {
        return codigoPoliza;
    }
    public void setCodigoPoliza(String codigoPoliza) {
        this.codigoPoliza = codigoPoliza;
    }
    public int getPolizaAceptaBioequivalente() {
        return polizaAceptaBioequivalente;
    }
    public void setPolizaAceptaBioequivalente(int polizaAceptaBioequivalente) {
        this.polizaAceptaBioequivalente = polizaAceptaBioequivalente;
    }
    public String getRutEmpresa() {
        return rutEmpresa;
    }
    public void setRutEmpresa(String rutEmpresa) {
        this.rutEmpresa = rutEmpresa;
    }
    public String getTerminoBeneficio() {
        return terminoBeneficio;
    }
    public void setTerminoBeneficio(String terminoBeneficio) {
        this.terminoBeneficio = terminoBeneficio;
    }
    public String getCuentaLiquidador() {
        return cuentaLiquidador;
    }
    public void setCuentaLiquidador(String cuentaLiquidador) {
        this.cuentaLiquidador = cuentaLiquidador;
    }
    public String getEstadoPolizaAhumada() {
        return estadoPolizaAhumada;
    }
    public void setEstadoPolizaAhumada(String estadoPolizaAhumada) {
        this.estadoPolizaAhumada = estadoPolizaAhumada;
    }
}
