/*
 * Créé le 3 nov. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.vb.copies;

import globaz.babel.db.copies.CTDocumentJointDefaultCopies;
import globaz.framework.bean.FWViewBeanInterface;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CTDocumentJointDefaultCopiesViewBean extends CTDocumentJointDefaultCopies implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csDestinataireDocument = "";
    private String csDomaineDocument = "";
    private String csGroupeTypeIntervenant = "";
    private String csTypeDocument = "";
    private String idDocument = "";
    private String nomDocument = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return
     */
    public String getCsDestinataireDocument() {
        return csDestinataireDocument;
    }

    /**
     * @return
     */
    public String getCsDomaineDocument() {
        return csDomaineDocument;
    }

    /**
     * @return
     */
    public String getCsGroupeTypeIntervenant() {
        return csGroupeTypeIntervenant;
    }

    /**
     * @return
     */
    public String getCsTypeDocument() {
        return csTypeDocument;
    }

    /**
     * @return
     */
    public String getCsTypeIntervenantLibelle() {
        return getSession().getCodeLibelle(getCsTypeIntervenant());
    }

    /**
     * @return
     */
    public String getIdDocument() {
        return idDocument;
    }

    /**
     * @return
     */
    public String getLibelleCsDestinataireDocument() {
        return getSession().getCodeLibelle(csDestinataireDocument);
    }

    /**
     * @return
     */
    public String getLibelleCsDomaineDocument() {
        return getSession().getCodeLibelle(csDomaineDocument);
    }

    /**
     * @return
     */
    public String getLibelleCsTypeDocument() {
        return getSession().getCodeLibelle(csTypeDocument);
    }

    /**
     * @return
     */
    public String getNomDocument() {
        return nomDocument;
    }

    /**
     * @param string
     */
    public void setCsDestinataireDocument(String string) {
        csDestinataireDocument = string;
    }

    /**
     * @param string
     */
    public void setCsDomaineDocument(String string) {
        csDomaineDocument = string;
    }

    /**
     * @param string
     */
    public void setCsGroupeTypeIntervenant(String string) {
        csGroupeTypeIntervenant = string;
    }

    /**
     * @param string
     */
    public void setCsTypeDocument(String string) {
        csTypeDocument = string;
    }

    /**
     * @param string
     */
    public void setIdDocument(String string) {
        idDocument = string;
    }

    /**
     * @param string
     */
    public void setNomDocument(String string) {
        nomDocument = string;
    }

}
