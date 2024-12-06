package seguridad.informacion.entitys;

import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "evento")
public class Evento 
{
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	Integer id;

	@Column
	String name;
	Integer impacto;
	Integer probabilidad;
	Integer valorRiesgo;
	String color;
	String nivel;
	Integer estadoFirmado;
	String nombreInforme;
	
	@Column(columnDefinition = "longtext")
	String recomendacion;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name= "idempresa")
	Empresa idempresa;

    @Lob
    @Column(columnDefinition = "longblob")
    byte[] informe;
    
	@ElementCollection
    @CollectionTable(name = "impacto_v", joinColumns = @JoinColumn(name = "evento_id"))
    @Column(name = "impacto_value")
    private Set<Integer> impacto_v;

    @ElementCollection
    @CollectionTable(name = "probabilidad_v", joinColumns = @JoinColumn(name = "evento_id"))
    @Column(name = "probabilidad_value")
    private Set<Integer> probabilidad_v;

    @ElementCollection
    @CollectionTable(name = "riesgo_aceptable", joinColumns = @JoinColumn(name = "evento_id"))
    @Column(name = "riesgo_aceptable_value")
    private Set<Integer> riesgo_aceptable;

    @ElementCollection
    @CollectionTable(name = "riesgo_tolerable", joinColumns = @JoinColumn(name = "evento_id"))
    @Column(name = "riesgo_tolerable_value")
    private Set<Integer> riesgo_tolerable;

    @ElementCollection
    @CollectionTable(name = "riesgo_alto", joinColumns = @JoinColumn(name = "evento_id"))
    @Column(name = "riesgo_alto_value")
    private Set<Integer> riesgo_alto;

    @ElementCollection
    @CollectionTable(name = "riesgo_extremo", joinColumns = @JoinColumn(name = "evento_id"))
    @Column(name = "riesgo_extremo_value")
    private Set<Integer> riesgo_extremo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getValorRiesgo() {
		return valorRiesgo;
	}

	public void setValorRiesgo(Integer valorRiesgo) {
		this.valorRiesgo = valorRiesgo;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getRecomendacion() {
		return recomendacion;
	}

	public void setRecomendacion(String recomendacion) {
		this.recomendacion = recomendacion;
	}

	public Empresa getIdempresa() {
		return idempresa;
	}

	public void setIdempresa(Empresa idempresa) {
		this.idempresa = idempresa;
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

	public byte[] getInforme() {
		return informe;
	}

	public void setInforme(byte[] informe) {
		this.informe = informe;
	}
}
