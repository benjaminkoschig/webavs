/*
 * Créé le 22 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.db.affiliation;

import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFAffiliationJTiersCI extends AFAffiliation {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENT ME!
     * 
     * @param _collection
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String _collection) {
        StringBuffer fromClause = new StringBuffer();

        fromClause.append(_collection);
        fromClause.append("AFAFFIP INNER JOIN ");
        fromClause.append(_collection);
        fromClause.append("TITIERP ON ");
        fromClause.append(_collection);
        fromClause.append("AFAFFIP.HTITIE=");
        fromClause.append(_collection);
        fromClause.append("TITIERP.HTITIE INNER JOIN ");
        fromClause.append(_collection);
        fromClause.append("TIPAVSP ON ");
        fromClause.append(_collection);
        fromClause.append("AFAFFIP.HTITIE=");
        fromClause.append(_collection);
        fromClause.append("TIPAVSP.HTITIE");

        return fromClause.toString();
    }

    private String numAVS;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String prenomNom;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAffiliationJTiersCI.
     */
    public AFAffiliationJTiersCI() {
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return AFAffiliationJTiersCI.createFromClause(_getCollection());
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        numAVS = statement.dbReadString("HXNAVS");
        prenomNom = statement.dbReadString("HTLDE2") + " " + statement.dbReadString("HTLDE1");
    }

    /**
     * getter pour l'attribut num AVS
     * 
     * @return la valeur courante de l'attribut num AVS
     */
    public String getNumAVS() {
        return numAVS;
    }

    /**
     * getter pour l'attribut prenom nom
     * 
     * @return la valeur courante de l'attribut prenom nom
     */
    public String getPrenomNom() {
        return prenomNom;
    }

    /**
     * setter pour l'attribut num AVS
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNumAVS(String string) {
        numAVS = string;
    }

    /**
     * setter pour l'attribut prenom nom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPrenomNom(String string) {
        prenomNom = string;
    }
}
