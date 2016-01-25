package globaz.apg.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PrestationVerseePojo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut;
    private String dateFin;
    private String idTiers;
    private List<PrestationVerseeLignePrestationPojo> listeLignePrestationVersee;
    private Map<String, PrestationVerseeLigneRecapitulationPojo> mapLigneRecapitulationPrestationVersee;
    private String montantBrutTotal;
    private String nombreCasTotal;
    private String numeroAffilie;
    private String nomAffilie;
    private String selecteurPrestationLibelle;

    public PrestationVerseePojo() {
        super();
        listeLignePrestationVersee = new ArrayList<PrestationVerseeLignePrestationPojo>();
        mapLigneRecapitulationPrestationVersee = new TreeMap<String, PrestationVerseeLigneRecapitulationPojo>();
        montantBrutTotal = "0";
        nombreCasTotal = "0";
        numeroAffilie = "";
        nomAffilie = "";
        selecteurPrestationLibelle = "";
        dateDebut = "";
        dateFin = "";
        idTiers = "";
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getNomAffilie() {
        return nomAffilie;
    }

    public void setNomAffilie(String nomAffilie) {
        this.nomAffilie = nomAffilie;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public List<PrestationVerseeLignePrestationPojo> getListeLignePrestationVersee() {
        return listeLignePrestationVersee;
    }

    public Map<String, PrestationVerseeLigneRecapitulationPojo> getMapLigneRecapitulationPrestationVersee() {
        return mapLigneRecapitulationPrestationVersee;
    }

    public String getMontantBrutTotal() {
        return montantBrutTotal;
    }

    public String getNombreCasTotal() {
        return nombreCasTotal;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getSelecteurPrestationLibelle() {
        return selecteurPrestationLibelle;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setListeLignePrestationVersee(List<PrestationVerseeLignePrestationPojo> listeLignePrestationVersee) {
        this.listeLignePrestationVersee = listeLignePrestationVersee;
    }

    public void setMapLigneRecapitulationPrestationVersee(
            Map<String, PrestationVerseeLigneRecapitulationPojo> mapLigneRecapitulationPrestationVersee) {
        this.mapLigneRecapitulationPrestationVersee = mapLigneRecapitulationPrestationVersee;
    }

    public void setMontantBrutTotal(String montantBrutTotal) {
        this.montantBrutTotal = montantBrutTotal;
    }

    public void setNombreCasTotal(String nombreCasTotal) {
        this.nombreCasTotal = nombreCasTotal;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setSelecteurPrestationLibelle(String selecteurPrestationLibelle) {
        this.selecteurPrestationLibelle = selecteurPrestationLibelle;
    }

}
