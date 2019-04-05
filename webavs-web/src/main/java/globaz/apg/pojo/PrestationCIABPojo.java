package globaz.apg.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PrestationCIABPojo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<PrestationCIABLigneRecapitulationPojo> listPrestationCIAB;
    private Map<String, PrestationCIABAssuranceComplPojo> mapPrestationComplementaireAssuranceCIAB;
    private String dateTraitement;
    private String selecteurPrestationLibelle;
    private String dateDebut;
    private String dateFin;
    private String numeroAffilie;
    private String nomAffilie;
    private double totalMontantBrutComplementaire;
    private double totalMontantBrutJourIsole;
    private int totalNbCasComplementaire;
    private int totalJourIsole;

    public PrestationCIABPojo() {
        super();
        listPrestationCIAB = new ArrayList<PrestationCIABLigneRecapitulationPojo>();
        mapPrestationComplementaireAssuranceCIAB = new TreeMap<String, PrestationCIABAssuranceComplPojo>();
        selecteurPrestationLibelle = "";
        totalMontantBrutComplementaire = 0;
        totalMontantBrutJourIsole = 0;
        totalNbCasComplementaire = 0;
        dateDebut = "";
        dateFin = "";
    }

    public ArrayList<PrestationCIABLigneRecapitulationPojo> getListPrestationCIAB() {
        return listPrestationCIAB;
    }

    public void setListPrestationCIAB(ArrayList<PrestationCIABLigneRecapitulationPojo> listPrestationCIAB) {
        this.listPrestationCIAB = listPrestationCIAB;
    }

    public String getDateTraitement() {
        return dateTraitement;
    }

    public void setDateTraitement(String dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public String getSelecteurPrestationLibelle() {
        return selecteurPrestationLibelle;
    }

    public void setSelecteurPrestationLibelle(String selecteurPrestationLibelle) {
        this.selecteurPrestationLibelle = selecteurPrestationLibelle;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numAffilie) {
        this.numeroAffilie = numAffilie;
    }

    public String getNomAffilie() {
        return nomAffilie;
    }

    public void setNomAffilie(String nomAffilie) {
        this.nomAffilie = nomAffilie;
    }

    public int getTotalJourIsole() {
        return totalJourIsole;
    }

    public void setTotalJourIsole(int totalJourIsole) {
        this.totalJourIsole = totalJourIsole;
    }

    public double getTotalMontantBrutComplementaire() {
        return totalMontantBrutComplementaire;
    }

    public void setTotalMontantBrutComplementaire(double totalMontantBrutComplementaire) {
        this.totalMontantBrutComplementaire = totalMontantBrutComplementaire;
    }

    public int getTotalNbCasComplementaire() {
        return totalNbCasComplementaire;
    }

    public void setTotalNbCasComplementaire(int totalNbCasComplementaire) {
        this.totalNbCasComplementaire = totalNbCasComplementaire;
    }

    public double getTotalMontantBrutJourIsole() {
        return totalMontantBrutJourIsole;
    }

    public void setTotalMontantBrutJourIsole(double totalMontantBrutJourIsole) {
        this.totalMontantBrutJourIsole = totalMontantBrutJourIsole;
    }

    public Map<String, PrestationCIABAssuranceComplPojo> getMapPrestationComplementaireAssuranceCIAB() {
        return mapPrestationComplementaireAssuranceCIAB;
    }

    public void setMapPrestationComplementaireAssuranceCIAB(Map<String, PrestationCIABAssuranceComplPojo> mapPrestationComplementaireCIAB) {
        this.mapPrestationComplementaireAssuranceCIAB = mapPrestationComplementaireCIAB;
    }
}
