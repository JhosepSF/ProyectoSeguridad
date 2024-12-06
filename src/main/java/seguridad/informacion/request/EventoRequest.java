package seguridad.informacion.request;

import java.util.Set;

public class EventoRequest 
{
	String name;
	Integer impacto;
	Integer probabilidad;
	Integer empresa;
	Set<Integer> impacto_v;
	Set<Integer> probabilidad_v;
	Set<Integer> riesgo_aceptable;
	Set<Integer> riesgo_tolerable;
	Set<Integer> riesgo_alto;
	Set<Integer> riesgo_extremo;
	Integer estadoFirmado;
	String nombreInforme;
	
	public Integer getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Integer empresa) {
		this.empresa = empresa;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getImpacto() {
		return impacto;
	}
	public void setImpacto(Integer impacto) {
		this.impacto = impacto;
	}
	public Integer getProbabilidad() {
		return probabilidad;
	}
	public void setProbabilidad(Integer probabilidad) {
		this.probabilidad = probabilidad;
	}
	public Set<Integer> getImpacto_v() {
		return impacto_v;
	}
	public void setImpacto_v(Set<Integer> impacto_v) {
		this.impacto_v = impacto_v;
	}
	public Set<Integer> getProbabilidad_v() {
		return probabilidad_v;
	}
	public void setProbabilidad_v(Set<Integer> probabilidad_v) {
		this.probabilidad_v = probabilidad_v;
	}
	public Set<Integer> getRiesgo_aceptable() {
		return riesgo_aceptable;
	}
	public void setRiesgo_aceptable(Set<Integer> riesgo_aceptable) {
		this.riesgo_aceptable = riesgo_aceptable;
	}
	public Set<Integer> getRiesgo_tolerable() {
		return riesgo_tolerable;
	}
	public void setRiesgo_tolerable(Set<Integer> riesgo_tolerable) {
		this.riesgo_tolerable = riesgo_tolerable;
	}
	public Set<Integer> getRiesgo_alto() {
		return riesgo_alto;
	}
	public void setRiesgo_alto(Set<Integer> riesgo_alto) {
		this.riesgo_alto = riesgo_alto;
	}
	public Set<Integer> getRiesgo_extremo() {
		return riesgo_extremo;
	}
	public void setRiesgo_extremo(Set<Integer> riesgo_extremo) {
		this.riesgo_extremo = riesgo_extremo;
	}
	public Integer getEstadoFirmado() {
		return estadoFirmado;
	}
	public void setEstadoFirmado(Integer estadoFirmado) {
		this.estadoFirmado = estadoFirmado;
	}
	public String getNombreInforme() {
		return nombreInforme;
	}
	public void setNombreInforme(String nombreInforme) {
		this.nombreInforme = nombreInforme;
	}
}
