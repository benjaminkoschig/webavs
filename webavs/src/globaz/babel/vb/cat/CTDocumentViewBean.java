/*
 * Créé le 13 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.vb.cat;

import globaz.babel.api.ICTDocument;
import globaz.babel.db.cat.CTDocument;
import globaz.framework.bean.FWViewBeanInterface;
import java.util.Set;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 * @see globaz.babel.db.cat.CTDocument
 */
public class CTDocumentViewBean extends CTDocument implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csGroupeDomaines;
    private String csGroupeTypesDocuments;
    private Set csTypesDocumentsExclus;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut cs groupe domaines.
     * 
     * @return la valeur courante de l'attribut cs groupe domaines
     */
    public String getCsGroupeDomaines() {
        return csGroupeDomaines;
    }

    /**
     * getter pour l'attribut cs groupe types documents.
     * 
     * @return la valeur courante de l'attribut cs groupe types documents
     */
    public String getCsGroupeTypesDocuments() {
        return csGroupeTypesDocuments;
    }

    /**
     * getter pour l'attribut cs types documents exclus.
     * 
     * @return la valeur courante de l'attribut cs types documents exclus
     */
    public Set getCsTypesDocumentsExclus() {
        return csTypesDocumentsExclus;
    }

    /**
     * getter pour l'attribut image actif.
     * 
     * @return la valeur courante de l'attribut image actif
     */
    public String getImageActif() {
        if (getActif().booleanValue()) {
            return "/images/ok.gif";
        } else {
            return "/images/erreur.gif";
        }
    }

    /**
     * getter pour l'attribut image actif.
     * 
     * @return la valeur courante de l'attribut image actif
     */
    public String getImageDefaut() {
        if (getDefaut().booleanValue()) {
            return "/images/ok.gif";
        } else {
            return "/images/erreur.gif";
        }
    }

    /**
     * getter pour l'attribut libelle destinataire.
     * 
     * @return la valeur courante de l'attribut libelle destinataire
     */
    public String getLibelleDestinataire() {
        return getSession().getCodeLibelle(getCsDestinataire());
    }

    /**
     * getter pour l'attribut libelle domaine.
     * 
     * @return la valeur courante de l'attribut libelle domaine
     */
    public String getLibelleDomaine() {
        return getSession().getCodeLibelle(getCsDomaine());
    }

    /**
     * getter pour l'attribut libelle editable.
     * 
     * @return la valeur courante de l'attribut libelle editable
     */
    public String getLibelleEditable() {
        return getSession().getCodeLibelle(getCsEditable());
    }

    /**
     * getter pour l'attribut libelle type document.
     * 
     * @return la valeur courante de l'attribut libelle type document
     */
    public String getLibelleTypeDocument() {
        return getSession().getCodeLibelle(getCsTypeDocument());
    }

    /**
     * getter pour l'attribut document editable.
     * 
     * @return la valeur courante de l'attribut document editable
     */
    public boolean isDocumentEditable() {
        return ICTDocument.CS_EDITABLE.equals(getCsEditable()) || isAdministrateur();
    }

    /**
     * getter pour l'attribut textes editables.
     * 
     * @return la valeur courante de l'attribut textes editables
     */
    public boolean isTextesEditables() {
        return !ICTDocument.CS_NON_EDITABLE.equals(getCsEditable()) || isAdministrateur();
    }

    /**
     * setter pour l'attribut cs groupe domaines.
     * 
     * @param csGroupeDomaines
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsGroupeDomaines(String csGroupeDomaines) {
        this.csGroupeDomaines = csGroupeDomaines;
    }

    /**
     * setter pour l'attribut cs groupe types documents.
     * 
     * @param csGroupeTypesDocuments
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsGroupeTypesDocuments(String csGroupeTypesDocuments) {
        this.csGroupeTypesDocuments = csGroupeTypesDocuments;
    }

    /**
     * setter pour l'attribut cs types documents exclus.
     * 
     * @param csTypesDocumentsExclus
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypesDocumentsExclus(Set csTypesDocumentsExclus) {
        this.csTypesDocumentsExclus = csTypesDocumentsExclus;
    }
}
