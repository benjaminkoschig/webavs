/*
 * Créé le 15 nov. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.api.doc.impl;

import globaz.babel.api.doc.ICTScalableDocumentCopie;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CTScalableDocumentCopie implements ICTScalableDocumentCopie {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csIntervenant = "";
    private String idAffilie = "";
    private String idTiers = "";
    private boolean isCopieOAI = false;
    private Integer key = null;
    private String prenomNom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
	 *
	 */
    public CTScalableDocumentCopie(Integer key) {
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
        if (arg0 instanceof CTScalableDocumentCopie) {
            return ((CTScalableDocumentCopie) arg0).key == key;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentCopie#getCsIntervenant()
     */
    @Override
    public String getCsIntervenant() {
        return csIntervenant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentCopie#getIdAffilie()
     */
    @Override
    public String getIdAffilie() {
        return idAffilie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentCopie#getIdTiers()
     */
    @Override
    public String getIdTiers() {
        return idTiers;
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
     * @see globaz.babel.api.doc.ICTScalableDocumentCopie#getPrenomNomTiers()
     */
    @Override
    public String getPrenomNomTiers() {
        return prenomNom;
    }

    public boolean isCopieOAI() {
        return isCopieOAI;
    }

    public void setCopieOAI(boolean isCopieOAI) {
        this.isCopieOAI = isCopieOAI;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentCopie#setCsIntervenant(java.lang .String)
     */
    @Override
    public void setCsIntervenant(String csIntervenant) {
        this.csIntervenant = csIntervenant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentCopie#setIdAffilie(java.lang. String)
     */
    @Override
    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentCopie#setIdTiers(java.lang.String )
     */
    @Override
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentCopie#setPrenomNomTiers(java. lang.String)
     */
    @Override
    public void setPrenomNomTiers(String prenomNomTiers) {
        prenomNom = prenomNomTiers;
    }

}
