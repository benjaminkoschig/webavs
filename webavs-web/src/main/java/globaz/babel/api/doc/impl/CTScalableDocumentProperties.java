/*
 * Créé le 15 nov. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.api.doc.impl;

import globaz.babel.api.doc.ICTScalableDocumentAnnexe;
import globaz.babel.api.doc.ICTScalableDocumentCopie;
import globaz.babel.api.doc.ICTScalableDocumentNiveau;
import globaz.babel.api.doc.ICTScalableDocumentProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CTScalableDocumentProperties implements ICTScalableDocumentProperties {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ArrayList annexes = null;
    private ArrayList copies = null;
    private String idDocument = "";
    private String idTiersPrincipal = "";
    private Boolean isMetaDocument = Boolean.FALSE;
    private ArrayList niveaux = null;
    private HashMap parameters = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
	 * 
	 */
    public CTScalableDocumentProperties() {
        super();
        parameters = new HashMap();
        annexes = new ArrayList();
        copies = new ArrayList();
        niveaux = new ArrayList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#addAnnexe(globaz.babel
     * .api.doc.ICTScalableDocumentAnnexe)
     */
    @Override
    public void addAnnexe(ICTScalableDocumentAnnexe annexe) {
        annexes.add(annexe);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#addCopie(globaz.babel .api.doc.ICTScalableDocumentCopie)
     */
    @Override
    public void addCopie(ICTScalableDocumentCopie copie) {
        copies.add(copie);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#addNiveau(globaz.babel
     * .api.doc.ICTScalableDocumentNiveau)
     */
    @Override
    public void addNiveau(ICTScalableDocumentNiveau niveau) {
        niveaux.add(niveau);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#containsParameter( java.lang.String)
     */
    @Override
    public Boolean containsParameter(String key) {
        return new Boolean(parameters.containsKey(key));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#getAnnexe(int)
     */
    @Override
    public ICTScalableDocumentAnnexe getAnnexe(int pos) {
        return (ICTScalableDocumentAnnexe) annexes.get(pos);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#getAnnexesIterator()
     */
    @Override
    public Iterator getAnnexesIterator() {
        return annexes.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#getAnnexesSize()
     */
    @Override
    public int getAnnexesSize() {
        return annexes.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#getCopie(int)
     */
    @Override
    public ICTScalableDocumentCopie getCopie(int pos) {
        return (ICTScalableDocumentCopie) copies.get(pos);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#getCopiesIterator()
     */
    @Override
    public Iterator getCopiesIterator() {
        return copies.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#getCopiesSize()
     */
    @Override
    public int getCopiesSize() {
        return copies.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#getIdDocument()
     */
    @Override
    public String getIdDocument() {
        return idDocument;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#getIdTiersPrincipal()
     */
    @Override
    public String getIdTiersPrincipal() {
        return idTiersPrincipal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#getNiveau(int)
     */
    @Override
    public ICTScalableDocumentNiveau getNiveau(int pos) {
        return (ICTScalableDocumentNiveau) niveaux.get(pos);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#getNiveauxIterator()
     */
    @Override
    public Iterator getNiveauxIterator() {
        return niveaux.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#getNiveauxSize()
     */
    @Override
    public int getNiveauxSize() {
        return niveaux.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#getParameter(java. lang.String)
     */
    @Override
    public String getParameter(String key) {
        return (String) parameters.get(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#getParametersEntrySet ()
     */
    @Override
    public Set getParametersEntrySet() {
        return parameters.entrySet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#isMetaDocument()
     */
    @Override
    public Boolean isMetaDocument() {
        return isMetaDocument;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#removeAllAnnexes()
     */
    @Override
    public void removeAllAnnexes() {
        annexes.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#removeAllCopies()
     */
    @Override
    public void removeAllCopies() {
        copies.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#removeAllParameters()
     */
    @Override
    public void removeAllParameters() {
        parameters.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#removeAnnexe(globaz
     * .babel.api.doc.ICTScalableDocumentAnnexe)
     */
    @Override
    public void removeAnnexe(ICTScalableDocumentAnnexe annexe) {
        annexes.remove(annexes.indexOf(annexe));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#removeCopie(globaz
     * .babel.api.doc.ICTScalableDocumentCopie)
     */
    @Override
    public void removeCopie(ICTScalableDocumentCopie copie) {
        copies.remove(copies.indexOf(copie));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#setIdDocument(java .lang.String)
     */
    @Override
    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#setIdTiersPrincipal (java.lang.String)
     */
    @Override
    public void setIdTiersPrincipal(String idTiersPrincipal) {
        this.idTiersPrincipal = idTiersPrincipal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#setIsMetaDocument( boolean)
     */
    @Override
    public void setIsMetaDocument(Boolean isMetaDocument) {
        this.isMetaDocument = isMetaDocument;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentProperties#setParameter(java. lang.String, java.lang.String)
     */
    @Override
    public void setParameter(String key, String value) {
        parameters.put(key, value);
    }
}
