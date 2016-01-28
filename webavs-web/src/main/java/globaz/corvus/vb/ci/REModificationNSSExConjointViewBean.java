/*
 * Créé le 12 janv. 07
 */
package globaz.corvus.vb.ci;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * @author mmo
 * 
 */

public class REModificationNSSExConjointViewBean extends PRAbstractViewBeanSupport {

    private String anneeDebut = "";
    private String anneeFin = "";
    private String eMailAddress = "";
    private String forIdRCI = "";
    private String idTiers = "";
    private String nouveauNSS = "";

    public String getAnneeDebut() {
        return anneeDebut;
    }

    public String getAnneeFin() {
        return anneeFin;
    }

    public String geteMailAddress() {
        return eMailAddress;
    }

    public String getForIdRCI() {
        return forIdRCI;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNouveauNSS() {
        return nouveauNSS;
    }

    public void setAnneeDebut(String anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public void setAnneeFin(String anneeFin) {
        this.anneeFin = anneeFin;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public void setForIdRCI(String forIdRCI) {
        this.forIdRCI = forIdRCI;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNouveauNSS(String nouveauNSS) {
        this.nouveauNSS = nouveauNSS;
    }

    @Override
    public boolean validate() {
        return true;
    }

}
