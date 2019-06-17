/*
 * Créé le 15 janv. 07
 *
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.corvus.vb.acor;

import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.api.BITransaction;

/**
 * @author scr
 *
 */
public class RECalculACORDemandeRenteViewBean extends REAbstractCalculACORViewBean {

    private String csTypeDemandeRente;
    private String idDemandeRente;
    private String idTiers;
    private boolean isFileContent = false;

    public String getCsTypeDemandeRente() {
        return csTypeDemandeRente;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * @return
     */
    public String getIdTiers() {
        return idTiers;
    }

    public boolean isFileContent() {
        return isFileContent;
    }

    /**
     * charge correctement une instance d'une demande de rente et la renvoie
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public REDemandeRente loadDemandeRente(BITransaction transaction) throws Exception {

        return REDemandeRente.loadDemandeRente(getSession(), transaction, idDemandeRente, csTypeDemandeRente);

    }

    public void setCsTypeDemandeRente(String csTypeDemande) {
        csTypeDemandeRente = csTypeDemande;
    }

    public void setIdDemandeRente(String idDemande) {
        idDemandeRente = idDemande;
    }

    /**
     * @param string
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    public void setIsFileContent(boolean elm) {
        isFileContent = elm;
    }

}
