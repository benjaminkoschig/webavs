/*
 * Créé le 15 nov. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.api.doc.impl;

import globaz.babel.api.doc.ICTScalableDocumentAnnexe;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CTScalableDocumentAnnexe implements ICTScalableDocumentAnnexe {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Integer key = null;
    private String libelle = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
	 * 
	 */
    public CTScalableDocumentAnnexe(Integer key) {
        super();
        this.key = key;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object arg0) {
        if (arg0 instanceof CTScalableDocumentAnnexe) {
            return ((CTScalableDocumentAnnexe) arg0).key == key;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentCopie#getKey()
     */
    @Override
    public String getKey() {
        return key.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentAnnexe#getLibelle()
     */
    @Override
    public String getLibelle() {
        return libelle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentAnnexe#setLibelle(java.lang.String )
     */
    @Override
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
