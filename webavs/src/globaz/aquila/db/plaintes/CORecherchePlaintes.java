/*
 * Créé le 1 mars 06
 */
package globaz.aquila.db.plaintes;

import globaz.aquila.util.COAdministrateurUtil;
import globaz.aquila.vb.COAbstractViewBeanSupport;
import globaz.globall.api.BISession;
import java.util.Map;

/**
 * @author dvh
 */
public class CORecherchePlaintes extends COAbstractViewBeanSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idCompteAuxiliaire = "";
    private String nomAdministrateur = "";
    private String numeroAdministrateur = "";
    private Map<String, String> societes = null;

    /**
     * @return DOCUMENT ME!
     */
    public String getIdCompteAuxiliaire() {
        return idCompteAuxiliaire;
    }

    /**
     * @return DOCUMENT ME!
     */
    public String getNomAdministrateur() {
        return nomAdministrateur;
    }

    /**
     * @return DOCUMENT ME!
     */
    public String getNumeroAdministrateur() {
        return numeroAdministrateur;
    }

    public Map<String, String> getSocietes() {
        return societes;
    }

    /**
     * @param string
     *            DOCUMENT ME!
     */
    public void setIdCompteAuxiliaire(String string) {
        idCompteAuxiliaire = string;
    }

    /**
     * @param string
     *            DOCUMENT ME!
     */
    public void setNomAdministrateur(String string) {
        nomAdministrateur = string;
    }

    /**
     * @param string
     *            DOCUMENT ME!
     */
    public void setNumeroAdministrateur(String string) {
        numeroAdministrateur = string;
    }

    public void setSocietes(String idCompteAuxiliaire, BISession session) throws Exception {
        societes = COAdministrateurUtil.getSocietes(idCompteAuxiliaire, session);
    }

}
