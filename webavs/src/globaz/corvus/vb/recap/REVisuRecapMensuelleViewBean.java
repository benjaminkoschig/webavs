/*
 * Créé le 19 nov. 07
 */
package globaz.corvus.vb.recap;

import globaz.globall.db.BSpy;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * @author BSC
 */
public class REVisuRecapMensuelleViewBean extends PRAbstractViewBeanSupport {
    private String dateRapport = "";
    private String idRecapMensuelle = "";

    /**
     * Constructeur
     */
    public REVisuRecapMensuelleViewBean() {
        super();
    }

    /**
     * Récuèpère la date de rapport
     * 
     * @return the dateRapport
     */
    public String getDateRapport() {
        return dateRapport;
    }

    /**
     * Récupération de l'id de récap mensuelle
     * 
     * @return the idRecapMensuelle
     */
    public String getIdRecapMensuelle() {
        return idRecapMensuelle;
    }

    /**
     * @return
     */
    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * Modifie la date de rapport
     * 
     * @param dateRapport
     *            the newDateRapport to set
     */
    public void setDateRapport(String newDateRapport) {
        dateRapport = newDateRapport;
    }

    /**
     * Modification de l'id de la récap mensuelle
     * 
     * @param idRecapMensuelle
     *            the idRecapMensuelle to set
     */
    public void setIdRecapMensuelle(String idRecapMensuelle) {
        this.idRecapMensuelle = idRecapMensuelle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {
        return true;
    }

}
