/*
 * Créé le 13 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.vb.cat;

import globaz.babel.db.cat.CTDocumentManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 * @see globaz.babel.db.cat.CTDocumentManager
 */
public class CTDocumentListViewBean extends CTDocumentManager implements FWListViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csGroupeDomaines = "";
    private String csGroupeTypesDocuments = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CTDocumentViewBean();
    }

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
     * setter pour l'attribut for actif.
     * 
     * @param forActif
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setForActif(Boolean forActif) {
        // HACK: empeche JSPUtils.setBeanProperties de setter une valeur
        // incorrecte
    }

    /**
     * setter pour l'attribut for defaut.
     * 
     * @param forDefaut
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setForDefaut(Boolean forDefaut) {
        // HACK: empeche JSPUtils.setBeanProperties de setter une valeur
        // incorrecte
    }
}
