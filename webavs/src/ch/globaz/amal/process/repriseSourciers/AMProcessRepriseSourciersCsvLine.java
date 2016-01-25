/**
 * 
 */
package ch.globaz.amal.process.repriseSourciers;

import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.amal.business.exceptions.models.uploadfichierreprise.AMUploadFichierRepriseException;

/**
 * @author dhi
 * 
 */
public class AMProcessRepriseSourciersCsvLine implements Comparable<AMProcessRepriseSourciersCsvLine> {

    public final static String SEPARATOR = ";";

    private String anneeSourcier = null;

    private String dateDebutSourcier = null;

    private String dateFinSourcier = null;

    private String debiteurSourcier = null;

    private String lineContent = null;

    private String naissanceConjoint = null;

    private String naissanceSourcier = null;

    private String natureSourcier = null;

    private String nbEnfantSourcier = null;

    private String noAVSConjoint = null;

    private String noAVSSourcier = null;

    private String noConjoint = null;

    private String nomConjoint = null;

    private String nomSourcier = null;

    private String noNSSConjoint = null;

    private String noNSSSourcier = null;

    private String noSourcier = null;

    private String prenomConjoint = null;

    private String prenomSourcier = null;

    private String revenuSourcier = null;

    private String sexeSourcier = null;

    /**
     * Default constructor
     * 
     * @param content
     */
    public AMProcessRepriseSourciersCsvLine(String content) {
        setLineContent(content);
        initFields();
    }

    @Override
    public int compareTo(AMProcessRepriseSourciersCsvLine o) {
        return getSexeSourcier().compareTo(o.getSexeSourcier());
    }

    /**
     * @return the anneeSourcier
     */
    public String getAnneeSourcier() {
        return anneeSourcier;
    }

    /**
     * @return the dateDebutSourcier
     */
    public String getDateDebutSourcier() {
        return dateDebutSourcier;
    }

    /**
     * @return the dateFinSourcier
     */
    public String getDateFinSourcier() {
        return dateFinSourcier;
    }

    /**
     * @return the debiteurSourcier
     */
    public String getDebiteurSourcier() {
        return debiteurSourcier;
    }

    /**
     * @return the lineContent
     */
    public String getLineContent() {
        return lineContent;
    }

    /**
     * @return the naissanceConjoint
     */
    public String getNaissanceConjoint() {
        return naissanceConjoint;
    }

    /**
     * @return the naissanceSourcier
     */
    public String getNaissanceSourcier() {
        return naissanceSourcier;
    }

    /**
     * @return the natureSourcier
     */
    public String getNatureSourcier() {
        return natureSourcier;
    }

    /**
     * @return the nbEnfantSourcier
     */
    public String getNbEnfantSourcier() {
        return nbEnfantSourcier;
    }

    /**
     * @return the noAVSConjoint
     */
    public String getNoAVSConjoint() {
        return noAVSConjoint;
    }

    /**
     * @return the noAVSSourcier
     */
    public String getNoAVSSourcier() {
        return noAVSSourcier;
    }

    /**
     * @return the noConjoint
     */
    public String getNoConjoint() {
        return noConjoint;
    }

    /**
     * @return the nomConjoint
     */
    public String getNomConjoint() {
        return nomConjoint;
    }

    /**
     * @return the nomSourcier
     */
    public String getNomSourcier() {
        return nomSourcier;
    }

    /**
     * @return the noNSSConjoint
     */
    public String getNoNSSConjoint() {
        return noNSSConjoint;
    }

    /**
     * @return the noNSSSourcier
     */
    public String getNoNSSSourcier() {
        return noNSSSourcier;
    }

    /**
     * @return the noSourcier
     */
    public String getNoSourcier() {
        return noSourcier;
    }

    /**
     * @return the prenomConjoint
     */
    public String getPrenomConjoint() {
        return prenomConjoint;
    }

    /**
     * @return the prenomSourcier
     */
    public String getPrenomSourcier() {
        return prenomSourcier;
    }

    /**
     * @return the revenuSourcier
     */
    public String getRevenuSourcier() {
        return revenuSourcier;
    }

    /**
     * @return the sexeSourcier
     */
    public String getSexeSourcier() {
        return sexeSourcier;
    }

    /*
     * Parsing de la ligne dans les champs
     */
    private void initFields() {
        String[] data = getLineContent().split(AMProcessRepriseSourciersCsvLine.SEPARATOR, 20);

        setNoSourcier(data[0]);
        setNomSourcier(data[1]);
        setPrenomSourcier(data[2]);
        setNaissanceSourcier(data[3]);
        setSexeSourcier(data[4]);
        setNoAVSSourcier(data[5]);
        setNoNSSSourcier(data[6]);
        setDebiteurSourcier(data[7]);
        setNatureSourcier(data[8]);
        setAnneeSourcier(data[9]);
        setDateDebutSourcier(data[10]);
        setDateFinSourcier(data[11]);
        setRevenuSourcier(data[12]);
        setNbEnfantSourcier(data[13]);
        setNoConjoint(data[14]);
        setNomConjoint(data[15]);
        setPrenomConjoint(data[16]);
        setNaissanceConjoint(data[17]);
        setNoAVSConjoint(data[18]);
        setNoNSSConjoint(data[19]);
    }

    /**
     * @param anneeSourcier
     *            the anneeSourcier to set
     */
    public void setAnneeSourcier(String anneeSourcier) {
        this.anneeSourcier = anneeSourcier;
    }

    /**
     * @param dateDebutSourcier
     *            the dateDebutSourcier to set
     */
    public void setDateDebutSourcier(String dateDebutSourcier) {
        this.dateDebutSourcier = dateDebutSourcier;
    }

    /**
     * @param dateFinSourcier
     *            the dateFinSourcier to set
     */
    public void setDateFinSourcier(String dateFinSourcier) {
        this.dateFinSourcier = dateFinSourcier;
    }

    /**
     * @param debiteurSourcier
     *            the debiteurSourcier to set
     */
    public void setDebiteurSourcier(String debiteurSourcier) {
        this.debiteurSourcier = debiteurSourcier;
    }

    /**
     * @param lineContent
     *            the lineContent to set
     */
    public void setLineContent(String lineContent) {
        this.lineContent = lineContent;
    }

    /**
     * @param naissanceConjoint
     *            the naissanceConjoint to set
     */
    public void setNaissanceConjoint(String naissanceConjoint) {
        this.naissanceConjoint = naissanceConjoint;
    }

    /**
     * @param naissanceSourcier
     *            the naissanceSourcier to set
     */
    public void setNaissanceSourcier(String naissanceSourcier) {
        this.naissanceSourcier = naissanceSourcier;
    }

    /**
     * @param natureSourcier
     *            the natureSourcier to set
     */
    public void setNatureSourcier(String natureSourcier) {
        this.natureSourcier = natureSourcier;
    }

    /**
     * @param nbEnfantSourcier
     *            the nbEnfantSourcier to set
     */
    public void setNbEnfantSourcier(String nbEnfantSourcier) {
        this.nbEnfantSourcier = nbEnfantSourcier;
    }

    /**
     * @param noAVSConjoint
     *            the noAVSConjoint to set
     */
    public void setNoAVSConjoint(String noAVSConjoint) {
        this.noAVSConjoint = noAVSConjoint;
    }

    /**
     * @param noAVSSourcier
     *            the noAVSSourcier to set
     */
    public void setNoAVSSourcier(String noAVSSourcier) {
        this.noAVSSourcier = noAVSSourcier;
    }

    /**
     * @param noConjoint
     *            the noConjoint to set
     */
    public void setNoConjoint(String noConjoint) {
        this.noConjoint = noConjoint;
    }

    /**
     * @param nomConjoint
     *            the nomConjoint to set
     */
    public void setNomConjoint(String nomConjoint) {
        this.nomConjoint = nomConjoint;
    }

    /**
     * @param nomSourcier
     *            the nomSourcier to set
     */
    public void setNomSourcier(String nomSourcier) {
        this.nomSourcier = nomSourcier;
    }

    /**
     * @param noNSSConjoint
     *            the noNSSConjoint to set
     */
    public void setNoNSSConjoint(String noNSSConjoint) {
        this.noNSSConjoint = noNSSConjoint;
    }

    /**
     * @param noNSSSourcier
     *            the noNSSSourcier to set
     */
    public void setNoNSSSourcier(String noNSSSourcier) {
        this.noNSSSourcier = noNSSSourcier;
    }

    /**
     * @param noSourcier
     *            the noSourcier to set
     */
    public void setNoSourcier(String noSourcier) {
        this.noSourcier = noSourcier;
    }

    /**
     * @param prenomConjoint
     *            the prenomConjoint to set
     */
    public void setPrenomConjoint(String prenomConjoint) {
        this.prenomConjoint = prenomConjoint;
    }

    /**
     * @param prenomSourcier
     *            the prenomSourcier to set
     */
    public void setPrenomSourcier(String prenomSourcier) {
        this.prenomSourcier = prenomSourcier;
    }

    /**
     * @param revenuSourcier
     *            the revenuSourcier to set
     */
    public void setRevenuSourcier(String revenuSourcier) {
        this.revenuSourcier = revenuSourcier;
    }

    /**
     * @param sexeSourcier
     *            the sexeSourcier to set
     */
    public void setSexeSourcier(String sexeSourcier) {
        this.sexeSourcier = sexeSourcier;
    }

    /**
     * Vérification des champs enregistrés
     * 
     * @throws AMUploadFichierRepriseException
     */
    public void validateFields() throws AMUploadFichierRepriseException {
        String errorMessage = "";
        if (JadeStringUtil.isEmpty(getNoSourcier())) {
            errorMessage += "\nNo de sourcier est vide : " + getLineContent();
        }
        if (JadeStringUtil.isEmpty(getNomSourcier())) {
            errorMessage += "\nNom du sourcier est vide : " + getLineContent();
        }
        if (!JadeStringUtil.isEmpty(getNomSourcier()) && (getNomSourcier().length() < 2)) {
            errorMessage += "\nNom du sourcier est court : " + getLineContent();
        }
        if (JadeStringUtil.isEmpty(getPrenomSourcier())) {
            errorMessage += "\nPrénom du sourcier est vide : " + getLineContent();
        }
        if (!JadeStringUtil.isEmpty(getPrenomSourcier()) && (getPrenomSourcier().length() < 2)) {
            errorMessage += "\nPrénom du sourcier est court : " + getLineContent();
        }
        if (JadeStringUtil.isEmpty(getNaissanceSourcier())) {
            errorMessage += "\nDate de naissance du sourcier est vide : " + getLineContent();
        }
        if (JadeStringUtil.isEmpty(getSexeSourcier())) {
            errorMessage += "\nGenre (sexe) du sourcier est vide : " + getLineContent();
        }
        if (JadeStringUtil.isEmpty(getAnneeSourcier())) {
            errorMessage += "\nAnnée taxation du sourcier est vide : " + getLineContent();
        }
        if (JadeStringUtil.isEmpty(getDateDebutSourcier())) {
            errorMessage += "\nDate de début du sourcier est vide : " + getLineContent();
        }
        if (JadeStringUtil.isEmpty(getDateFinSourcier())) {
            errorMessage += "\nDate de fin du sourcier est vide : " + getLineContent();
        }
        if (JadeStringUtil.isEmpty(getRevenuSourcier())) {
            errorMessage += "\nRevenu sourcier est vide : " + getLineContent();
        }
        if (errorMessage.length() > 0) {
            throw new AMUploadFichierRepriseException(errorMessage);
        }
    }

}
