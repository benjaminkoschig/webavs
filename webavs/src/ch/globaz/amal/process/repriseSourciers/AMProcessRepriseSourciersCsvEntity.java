/**
 * 
 */
package ch.globaz.amal.process.repriseSourciers;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import java.util.List;

/**
 * @author dhi
 * 
 */
public class AMProcessRepriseSourciersCsvEntity {

    private String annee = null;
    private String dateNaissanceConjoint = null;
    private String dateNaissanceSourcier = null;
    private String noAVSConjoint = null;
    private String noAVSSourcier = null;
    private String noConjoint = null;
    private String nombreEnfants = null;
    private String nomConjoint = null;
    private String nomSourcier = null;
    private String noNSSConjoint = null;
    private String noNSSSourcier = null;
    private String noSourcier = null;
    private String prenomConjoint = null;
    private String prenomSourcier = null;
    private List<AMProcessRepriseSourciersCsvLine> relatedLines = null;
    private String revenuConjoint = null;
    private String revenuSourcier = null;
    private String sexeConjoint = null;
    private String sexeSourcier = null;

    /**
     * Default constructor for next uses
     * 
     * @param relatedLines
     */
    public AMProcessRepriseSourciersCsvEntity() {
    }

    /**
     * Default constructor when parsing the file
     * 
     * @param relatedLines
     */
    public AMProcessRepriseSourciersCsvEntity(List<AMProcessRepriseSourciersCsvLine> relatedLines) {
        setRelatedLines(relatedLines);
        createEntity();
    }

    /**
     * Default constructor for entity process use
     * 
     * @param fileUploadSavedString
     */
    public AMProcessRepriseSourciersCsvEntity(String fileUploadSavedString) {
        parseSavedString(fileUploadSavedString);
    }

    /**
     * Création de l'entité et regroupement des informations noSourcier, conjoint et revenu
     * 
     */
    private void createEntity() {

        // 0 - sauvegarde des informations globales sourciers et son conjoint
        AMProcessRepriseSourciersCsvLine firstLine = getRelatedLines().get(0);
        setAnnee(firstLine.getAnneeSourcier());
        setNombreEnfants(firstLine.getNbEnfantSourcier());
        setDateNaissanceSourcier(firstLine.getNaissanceSourcier());
        setNoAVSSourcier(firstLine.getNoAVSSourcier());
        setNomSourcier(firstLine.getNomSourcier());
        setNoNSSSourcier(firstLine.getNoNSSSourcier());
        setNoSourcier(firstLine.getNoSourcier());
        setPrenomSourcier(firstLine.getPrenomSourcier());
        setSexeSourcier(firstLine.getSexeSourcier());

        // Recherche d'une ligne qui contient le conjoint
        if (!JadeStringUtil.isEmpty(firstLine.getNoConjoint())) {
            setNoConjoint(firstLine.getNoConjoint());
            setNomConjoint(firstLine.getNomConjoint());
            setPrenomConjoint(firstLine.getPrenomConjoint());
            setDateNaissanceConjoint(firstLine.getNaissanceConjoint());
            setNoAVSConjoint(firstLine.getNoAVSConjoint());
            setNoNSSConjoint(firstLine.getNoNSSConjoint());
            setSexeConjoint("");
        } else {
            boolean bFoundConjoint = false;
            for (int iLine = 0; iLine < getRelatedLines().size(); iLine++) {
                AMProcessRepriseSourciersCsvLine currentLine = getRelatedLines().get(iLine);
                if (!currentLine.getNoSourcier().equals(firstLine.getNoSourcier())) {
                    // Il s'agit du conjoint
                    setNoConjoint(currentLine.getNoSourcier());
                    setNomConjoint(currentLine.getNomSourcier());
                    setPrenomConjoint(currentLine.getPrenomSourcier());
                    setDateNaissanceConjoint(currentLine.getNaissanceSourcier());
                    setNoAVSConjoint(currentLine.getNoAVSSourcier());
                    setNoNSSConjoint(currentLine.getNoNSSSourcier());
                    setSexeConjoint(currentLine.getSexeSourcier());
                    bFoundConjoint = true;
                    break;
                }
            }
            if (!bFoundConjoint) {
                // initialisation not null
                setNoConjoint(firstLine.getNoConjoint());
                setNomConjoint(firstLine.getNomConjoint());
                setPrenomConjoint(firstLine.getPrenomConjoint());
                setDateNaissanceConjoint(firstLine.getNaissanceConjoint());
                setNoAVSConjoint(firstLine.getNoAVSConjoint());
                setNoNSSConjoint(firstLine.getNoNSSConjoint());
                setSexeConjoint("");
            }
        }

        // Parcours des lignes
        double revenuSourcier = 0.0;
        double revenuConjoint = 0.0;
        for (int iLine = 0; iLine < getRelatedLines().size(); iLine++) {
            AMProcessRepriseSourciersCsvLine currentLine = getRelatedLines().get(iLine);
            // détection si sourcier ou conjoint >>
            if (getNoConjoint().equals(currentLine.getNoSourcier())) {
                // 1 - sauvegarde des informations globales conjoints
                setDateNaissanceConjoint(currentLine.getNaissanceSourcier());
                setNoAVSConjoint(currentLine.getNoAVSSourcier());
                setNoConjoint(currentLine.getNoSourcier());
                setNomConjoint(currentLine.getNomSourcier());
                setNoNSSConjoint(currentLine.getNoNSSSourcier());
                setPrenomConjoint(currentLine.getPrenomSourcier());
                setSexeConjoint(currentLine.getSexeSourcier());
                // 2 - calcul du revenu conjoint
                try {
                    revenuConjoint += Double.parseDouble(currentLine.getRevenuSourcier());
                } catch (Exception ex) {
                    JadeLogger.error(null, "Error parsing revenu conjoint : " + currentLine.getRevenuSourcier());
                }
            } else {
                // 3 - calcul du revenu sourcier
                try {
                    revenuSourcier += Double.parseDouble(currentLine.getRevenuSourcier());
                } catch (Exception ex) {
                    JadeLogger.error(null, "Error parsing revenu sourcier : " + currentLine.getRevenuSourcier());
                }
            }
            // si nombre d'enfant différents, prendre le plus grand
            if (!JadeStringUtil.isEmpty(currentLine.getNbEnfantSourcier())) {
                if (!JadeStringUtil.isEmpty(getNombreEnfants())) {
                    try {
                        int iCurrentNbEnfant = Integer.parseInt(getNombreEnfants());
                        int iNewNbEnfant = Integer.parseInt(currentLine.getNbEnfantSourcier());
                        if (iNewNbEnfant > iCurrentNbEnfant) {
                            setNombreEnfants(currentLine.getNbEnfantSourcier());
                        }
                    } catch (Exception ex) {
                        JadeLogger.error(null, "Error parsing nb enfants : " + getNombreEnfants() + " vs "
                                + currentLine.getNbEnfantSourcier());
                    }
                } else {
                    setNombreEnfants(currentLine.getNbEnfantSourcier());
                }
            }
        }
        // enregistrement des revenus
        setRevenuSourcier(String.format("%.2f", revenuSourcier));
        setRevenuConjoint(String.format("%.2f", +revenuConjoint));

    }

    /**
     * @return the annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @return the dateNaissanceConjoint
     */
    public String getDateNaissanceConjoint() {
        return dateNaissanceConjoint;
    }

    /**
     * @return the dateNaissanceSourcier
     */
    public String getDateNaissanceSourcier() {
        return dateNaissanceSourcier;
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
     * @return the nombreEnfants
     */
    public String getNombreEnfants() {
        return nombreEnfants;
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
     * @return the relatedLines
     */
    public List<AMProcessRepriseSourciersCsvLine> getRelatedLines() {
        return relatedLines;
    }

    /**
     * @return the revenuConjoint
     */
    public String getRevenuConjoint() {
        return revenuConjoint;
    }

    /**
     * @return the revenuSourcier
     */
    public String getRevenuSourcier() {
        return revenuSourcier;
    }

    /**
     * @return the sexeConjoint
     */
    public String getSexeConjoint() {
        return sexeConjoint;
    }

    /**
     * @return the sexeSourcier
     */
    public String getSexeSourcier() {
        return sexeSourcier;
    }

    /**
     * restore object from a string saved in MAUPLFR
     * 
     * @param fileUploadSavedString
     */
    private void parseSavedString(String fileUploadSavedString) {
        String[] data = fileUploadSavedString.split(AMProcessRepriseSourciersCsvLine.SEPARATOR, 18);

        setNoSourcier(data[0]);
        setNomSourcier(data[1]);
        setPrenomSourcier(data[2]);
        setDateNaissanceSourcier(data[3]);
        setSexeSourcier(data[4]);
        setNoAVSSourcier(data[5]);
        setNoNSSSourcier(data[6]);
        setNoConjoint(data[7]);
        setNomConjoint(data[8]);
        setPrenomConjoint(data[9]);
        setDateNaissanceConjoint(data[10]);
        setSexeConjoint(data[11]);
        setNoAVSConjoint(data[12]);
        setNoNSSConjoint(data[13]);
        setAnnee(data[14]);
        setNombreEnfants(data[15]);
        setRevenuSourcier(data[16]);
        setRevenuConjoint(data[17]);

    }

    /**
     * @param annee
     *            the annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * @param dateNaissanceConjoint
     *            the dateNaissanceConjoint to set
     */
    public void setDateNaissanceConjoint(String dateNaissanceConjoint) {
        this.dateNaissanceConjoint = dateNaissanceConjoint;
    }

    /**
     * @param dateNaissanceSourcier
     *            the dateNaissanceSourcier to set
     */
    public void setDateNaissanceSourcier(String dateNaissanceSourcier) {
        this.dateNaissanceSourcier = dateNaissanceSourcier;
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
     * @param nombreEnfants
     *            the nombreEnfants to set
     */
    public void setNombreEnfants(String nombreEnfants) {
        this.nombreEnfants = nombreEnfants;
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
     * @param relatedLines
     *            the relatedLines to set
     */
    public void setRelatedLines(List<AMProcessRepriseSourciersCsvLine> relatedLines) {
        this.relatedLines = relatedLines;
    }

    /**
     * @param revenuConjoint
     *            the revenuConjoint to set
     */
    public void setRevenuConjoint(String revenuConjoint) {
        this.revenuConjoint = revenuConjoint;
    }

    /**
     * @param revenuSourcier
     *            the revenuSourcier to set
     */
    public void setRevenuSourcier(String revenuSourcier) {
        this.revenuSourcier = revenuSourcier;
    }

    /**
     * @param sexeConjoint
     *            the sexeConjoint to set
     */
    public void setSexeConjoint(String sexeConjoint) {
        this.sexeConjoint = sexeConjoint;
    }

    /**
     * @param sexeSourcier
     *            the sexeSourcier to set
     */
    public void setSexeSourcier(String sexeSourcier) {
        this.sexeSourcier = sexeSourcier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String returnString = "";
        returnString += getNoSourcier() + ";";
        returnString += getNomSourcier() + ";";
        returnString += getPrenomSourcier() + ";";
        returnString += getDateNaissanceSourcier() + ";";
        returnString += getSexeSourcier() + ";";
        returnString += getNoAVSSourcier() + ";";
        returnString += getNoNSSSourcier() + ";";
        returnString += getNoConjoint() + ";";
        returnString += getNomConjoint() + ";";
        returnString += getPrenomConjoint() + ";";
        returnString += getDateNaissanceConjoint() + ";";
        returnString += getSexeConjoint() + ";";
        returnString += getNoAVSConjoint() + ";";
        returnString += getNoNSSConjoint() + ";";
        returnString += getAnnee() + ";";
        returnString += getNombreEnfants() + ";";
        returnString += getRevenuSourcier() + ";";
        returnString += getRevenuConjoint();
        return returnString;
    }

}
