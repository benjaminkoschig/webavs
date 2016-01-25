/*
 * Cr�� le 8 d�c. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.babel.api.doc.impl;

import globaz.babel.api.doc.ICTScalableDocumentPosition;

/**
 * @author bsc
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CTScalableDocumentPosition implements ICTScalableDocumentPosition {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String description = "";
    private boolean isSelected = false;
    private Integer key = null;
    private String position = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
	 * 
	 */
    public CTScalableDocumentPosition(Integer key) {
        super();
        this.key = key;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentPosition#getDescription()
     */
    @Override
    public String getDescription() {
        return description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentPosition#getKey()
     */
    @Override
    public String getKey() {
        return key.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentPosition#getPosition()
     */
    @Override
    public String getPosition() {
        return position;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentPosition#isSelected()
     */
    @Override
    public boolean isSelected() {
        return isSelected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentPosition#setDescription(java. lang.String)
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentPosition#setIsSelected(boolean)
     */
    @Override
    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentPosition#setPosition(java.lang .String)
     */
    @Override
    public void setPosition(String position) {
        this.position = position;
    }

}
