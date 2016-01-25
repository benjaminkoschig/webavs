/*
 * Créé le 1 mars 06
 */
package globaz.aquila.db.ard;

import globaz.aquila.util.COAdministrateurUtil;
import globaz.aquila.vb.COAbstractViewBeanSupport;
import globaz.globall.api.BISession;
import java.util.Map;

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
    private Map societes = null;

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

    public Map getSocietes() {
        return societes;
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

    public void setSocietes(String IdCompteAuxiliaire, BISession session) throws Exception {
        societes = COAdministrateurUtil.getSocietes(IdCompteAuxiliaire, session);
    }

}
