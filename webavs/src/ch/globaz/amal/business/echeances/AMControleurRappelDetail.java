/**
 * 
 */
package ch.globaz.amal.business.echeances;

import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.famille.SimpleFamille;

/**
 * @author DHI
 * 
 *         Classe permettant la mise à disposition d'un détail d'échéance LIBRA, type rappel, à la sauce AMAL Doit être
 *         renseigné par le service ControleurRappelService
 * 
 */
public class AMControleurRappelDetail {

    private String csFormuleRappel = null;

    private String dateRappel = null;

    private SimpleDetailFamille detailFamille = null;

    private String idJournalisationLibra = null;

    private String libelleFormuleRappel = null;

    private SimpleFamille simpleFamille = null;

    /**
     * Default constructor
     * 
     */
    public AMControleurRappelDetail() {
        super();
    }

    /**
     * @return the csFormuleRappel
     */
    public String getCsFormuleRappel() {
        return csFormuleRappel;
    }

    /**
     * @return the dateRappel
     */
    public String getDateRappel() {
        return dateRappel;
    }

    /**
     * @return the detailFamille
     */
    public SimpleDetailFamille getDetailFamille() {
        return detailFamille;
    }

    /**
     * @return the idLibra
     */
    public String getIdJournalisationLibra() {
        return idJournalisationLibra;
    }

    /**
     * @return the libelleFormuleRappel
     */
    public String getLibelleFormuleRappel() {
        return libelleFormuleRappel;
    }

    /**
     * @return the simpleFamille
     */
    public SimpleFamille getSimpleFamille() {
        return simpleFamille;
    }

    /**
     * @param csFormuleRappel
     *            the csFormuleRappel to set
     */
    public void setCsFormuleRappel(String csFormuleRappel) {
        this.csFormuleRappel = csFormuleRappel;
    }

    /**
     * @param dateRappel
     *            the dateRappel to set
     */
    public void setDateRappel(String dateRappel) {
        this.dateRappel = dateRappel;
    }

    /**
     * @param detailFamille
     *            the detailFamille to set
     */
    public void setDetailFamille(SimpleDetailFamille detailFamille) {
        this.detailFamille = detailFamille;
    }

    /**
     * @param idJournalisationLibra
     *            the idJournalisationLibra to set
     */
    public void setIdJournalisationLibra(String idJournalisationLibra) {
        this.idJournalisationLibra = idJournalisationLibra;
    }

    /**
     * @param libelleFormuleRappel
     *            the libelleFormuleRappel to set
     */
    public void setLibelleFormuleRappel(String libelleFormuleRappel) {
        this.libelleFormuleRappel = libelleFormuleRappel;
    }

    /**
     * @param simpleFamille
     *            the simpleFamille to set
     */
    public void setSimpleFamille(SimpleFamille simpleFamille) {
        this.simpleFamille = simpleFamille;
    }
}
