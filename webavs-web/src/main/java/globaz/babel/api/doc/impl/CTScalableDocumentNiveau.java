/*
 * Créé le 8 déc. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.api.doc.impl;

import globaz.babel.api.doc.ICTScalableDocumentNiveau;
import globaz.babel.api.doc.ICTScalableDocumentPosition;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CTScalableDocumentNiveau implements ICTScalableDocumentNiveau {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String description = "";
    private Integer key = null;
    private String niveau = "";
    private ArrayList positions = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
	 * 
	 */
    public CTScalableDocumentNiveau(Integer key) {
        super();
        positions = new ArrayList();
        this.key = key;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentNiveau#addPosition(globaz.babel
     * .api.doc.ICTScalableDocumentPosition)
     */
    @Override
    public void addPosition(ICTScalableDocumentPosition position) {
        positions.add(position);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentNiveau#getDescription()
     */
    @Override
    public String getDescription() {
        return description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentNiveau#getKey()
     */
    @Override
    public String getKey() {
        return key.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentNiveau#getNiveau()
     */
    @Override
    public String getNiveau() {
        return niveau;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentNiveau#getPosition(int)
     */
    @Override
    public ICTScalableDocumentPosition getPosition(int pos) {
        return (ICTScalableDocumentPosition) positions.get(pos);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentNiveau#getPositionIterator()
     */
    @Override
    public Iterator getPositionIterator() {
        return positions.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentNiveau#getPositionSize()
     */
    @Override
    public int getPositionSize() {
        return positions.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentNiveau#setDescription(java.lang .String)
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentNiveau#setNiveau(java.lang.String )
     */
    @Override
    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }
}
