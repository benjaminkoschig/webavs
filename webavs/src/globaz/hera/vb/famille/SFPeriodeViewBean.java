/*
 * Créé le 16 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.vb.famille;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFPeriode;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

/**
 * @author jpa
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class SFPeriodeViewBean extends SFPeriode implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csDomaine;
    private String dateNaissance;
    private String libellePays;
    private String libelleSexe;
    private String nomPrenom;
    private String numAvs;

    public String getCsDomaine() {
        return csDomaine;
    }

    /**
     * @return
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getLibellePays() {
        return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", libellePays));
    }

    /**
     * @return
     */
    public String getLibelleSexe() {
        return getSession().getCodeLibelle(libelleSexe);
    }

    /**
     * @return
     */
    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * @return
     */
    public String getNumAvs() {
        return numAvs;
    }

    // pour retrouver un membre d'après l'idMembreFamille
    public void retrieveMembre(BISession session, String idMembre) {
        SFMembreFamille membre = new SFMembreFamille();
        membre.setSession((BSession) session);
        membre.setIdMembreFamille(idMembre);
        try {
            membre.retrieve();
            if (!membre.isNew()) {
                setNumAvs(membre.getNss());
                setNomPrenom(membre.getNom() + " " + membre.getPrenom());
                setDateNaissance(membre.getDateNaissance());
                setLibellePays(membre.getPays());
                if (!JadeStringUtil.isEmpty(getLibellePays())) {
                    setLibellePays(membre.getCsNationalite());
                }
                setLibelleSexe(membre.getCsSexe());
                setCsDomaine(membre.getCsDomaineApplication());
            }
        } catch (Exception e) {
            JadeLogger.warn("Erreur retrieve periode_rc : retrieveMembre()", e);
        }
    }

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    /**
     * @param string
     */
    public void setDateNaissance(String string) {
        dateNaissance = string;
    }

    /**
     * @param string
     */
    public void setLibellePays(String string) {
        libellePays = string;
    }

    /**
     * @param string
     */
    public void setLibelleSexe(String string) {
        libelleSexe = string;
    }

    /**
     * @param string
     */
    public void setNomPrenom(String string) {
        nomPrenom = string;
    }

    /**
     * @param string
     */
    public void setNumAvs(String string) {
        numAvs = string;
    }

}
