/*
 * Créé le 1 mars 06
 */
package globaz.aquila.db.suiviprocedure;

import globaz.aquila.vb.COAbstractViewBeanSupport;

/**
 * @author dvh
 */
public class CORechercheARD extends COAbstractViewBeanSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idCompteAuxiliaire = "";
    private String nomAdministrateur = "";
    private String numeroAdministrateur = "";

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getIdCompteAuxiliaire() {
        return idCompteAuxiliaire;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getNomAdministrateur() {
        return nomAdministrateur;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getNumeroAdministrateur() {
        return numeroAdministrateur;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setIdCompteAuxiliaire(String string) {
        idCompteAuxiliaire = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setNomAdministrateur(String string) {
        nomAdministrateur = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setNumeroAdministrateur(String string) {
        numeroAdministrateur = string;
    }

}
