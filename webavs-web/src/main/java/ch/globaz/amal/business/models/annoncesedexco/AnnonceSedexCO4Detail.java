/**
 * 
 */
package ch.globaz.amal.business.models.annoncesedexco;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LBE
 * 
 */
public class AnnonceSedexCO4Detail {
    private AnnonceSedexCOAssureContainer debiteur;
    private List<AnnonceSedexCOAssureContainer> assureList;
    private String idAnnonceCO;
    private String idSedex;
    private String anneeAnnonce;
    private String dateAnnonce;
    private String periodAnnonce;
    private String nomCaisse;
    private String libelleAnnonce;
    private String statusAnnonce;
    private String interets;
    private String frais;
    private String total;

    private List<AnnonceSedexCOPaimentsContainer> paList;
    private String paDebiteur;
    private String paRetroatives;
    private String paAnnulation;
    private String paTotal;

    /**
     * Default constructor
     */
    public AnnonceSedexCO4Detail() {
        debiteur = new AnnonceSedexCOAssureContainer();
        assureList = new ArrayList<AnnonceSedexCOAssureContainer>();
        idAnnonceCO = "";
        idSedex = "";
        anneeAnnonce = "";
        dateAnnonce = "";
        periodAnnonce = "";
        nomCaisse = "";
        libelleAnnonce = "";
        statusAnnonce = "";
        interets = "";
        frais = "";
        total = "";

        paList = new ArrayList<AnnonceSedexCOPaimentsContainer>();
        paDebiteur = "";
        paRetroatives = "";
        paAnnulation = "";
        paTotal = "";
    }

    /**
     * @return the debiteur
     */
    public AnnonceSedexCOAssureContainer getDebiteur() {
        return debiteur;
    }

    /**
     * @param debiteur the debiteur to set
     */
    public void setDebiteur(AnnonceSedexCOAssureContainer debiteur) {
        this.debiteur = debiteur;
    }

    /**
     * @return the assureList
     */
    public List<AnnonceSedexCOAssureContainer> getAssureList() {
        return assureList;
    }

    /**
     * @param assureList the assureList to set
     */
    public void setAssureList(List<AnnonceSedexCOAssureContainer> assureList) {
        this.assureList = assureList;
    }

    /**
     * @return the idAnnonceCO
     */
    public String getIdAnnonceCO() {
        return idAnnonceCO;
    }

    /**
     * @param idAnnonceCO the idAnnonceCO to set
     */
    public void setIdAnnonceCO(String idAnnonceCO) {
        this.idAnnonceCO = idAnnonceCO;
    }

    /**
     * @return the idSedex
     */
    public String getIdSedex() {
        return idSedex;
    }

    /**
     * @param idSedex the idSedex to set
     */
    public void setIdSedex(String idSedex) {
        this.idSedex = idSedex;
    }

    /**
     * @return the anneeAnnonce
     */
    public String getAnneeAnnonce() {
        return anneeAnnonce;
    }

    /**
     * @param anneeAnnonce the anneeAnnonce to set
     */
    public void setAnneeAnnonce(String anneeAnnonce) {
        this.anneeAnnonce = anneeAnnonce;
    }

    /**
     * @return the daAnnonce
     */
    public String getDateAnnonce() {
        return dateAnnonce;
    }

    /**
     * @param daAnnonce the daAnnonce to set
     */
    public void setDateAnnonce(String dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }

    /**
     * @return the periodAnnonce
     */
    public String getPeriodAnnonce() {
        return periodAnnonce;
    }

    /**
     * @param periodAnnonce the periodAnnonce to set
     */
    public void setPeriodAnnonce(String periodAnnonce) {
        this.periodAnnonce = periodAnnonce;
    }

    /**
     * @return the nomCaisse
     */
    public String getNomCaisse() {
        return nomCaisse;
    }

    /**
     * @param nomCaisse the nomCaisse to set
     */
    public void setNomCaisse(String nomCaisse) {
        this.nomCaisse = nomCaisse;
    }

    /**
     * @return the libelleAnnonce
     */
    public String getLibelleAnnonce() {
        return libelleAnnonce;
    }

    /**
     * @param libelleAnnonce the libelleAnnonce to set
     */
    public void setLibelleAnnonce(String libelleAnnonce) {
        this.libelleAnnonce = libelleAnnonce;
    }

    /**
     * @return the statusAnnonce
     */
    public String getStatusAnnonce() {
        return statusAnnonce;
    }

    /**
     * @param statusAnnonce the statusAnnonce to set
     */
    public void setStatusAnnonce(String statusAnnonce) {
        this.statusAnnonce = statusAnnonce;
    }

    /**
     * @return the interets
     */
    public String getInterets() {
        return interets;
    }

    /**
     * @param interets the interets to set
     */
    public void setInterets(String interets) {
        this.interets = interets;
    }

    /**
     * @return the frais
     */
    public String getFrais() {
        return frais;
    }

    /**
     * @param frais the frais to set
     */
    public void setFrais(String frais) {
        this.frais = frais;
    }

    /**
     * @return the total
     */
    public String getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(String total) {
        this.total = total;
    }

    /**
     * @return the paList
     */
    public List<AnnonceSedexCOPaimentsContainer> getPaList() {
        return paList;
    }

    /**
     * @param paList the paList to set
     */
    public void setPaList(List<AnnonceSedexCOPaimentsContainer> paList) {
        this.paList = paList;
    }

    /**
     * @return the paDebiteur
     */
    public String getPaDebiteur() {
        return paDebiteur;
    }

    /**
     * @param paDebiteur the paDebiteur to set
     */
    public void setPaDebiteur(String paDebiteur) {
        this.paDebiteur = paDebiteur;
    }

    /**
     * @return the paRetroatives
     */
    public String getPaRetroatives() {
        return paRetroatives;
    }

    /**
     * @param paRetroatives the paRetroatives to set
     */
    public void setPaRetroatives(String paRetroatives) {
        this.paRetroatives = paRetroatives;
    }

    /**
     * @return the paAnnulation
     */
    public String getPaAnnulation() {
        return paAnnulation;
    }

    /**
     * @param paAnnulation the paAnnulation to set
     */
    public void setPaAnnulation(String paAnnulation) {
        this.paAnnulation = paAnnulation;
    }

    /**
     * @return the paTotal
     */
    public String getPaTotal() {
        return paTotal;
    }

    /**
     * @param paTotal the paTotal to set
     */
    public void setPaTotal(String paTotal) {
        this.paTotal = paTotal;
    }

}
