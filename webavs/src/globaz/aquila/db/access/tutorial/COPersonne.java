package globaz.aquila.db.access.tutorial;

import globaz.aquila.common.COBEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAStringFormatter;
import globaz.globall.util.JAUtil;

/**
 * Représente une entité de type Personne
 * 
 * @author Pascal Lovy, 05-oct-2004
 */
public class COPersonne extends COBEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (datenaissance) */
    private String dateNaissance = "";
    /** (employeur) */
    private COEmployeur employeur = new COEmployeur();
    /** (genre) */
    private String genre = "";
    /** (idemployeur) */
    private String idEmployeur = "";
    /** (idpersonne) */
    private String idPersonne = "";
    /** (nom) */
    private String nom = "";
    /** (numavs) */
    private String numAVS = "";
    /** (prenom) */
    private String prenom = "";

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        refreshLinks(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        refreshLinks(transaction);
        idPersonne = this._incCounter(transaction, "0");
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        refreshLinks(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "PERSONNE";
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idPersonne = statement.dbReadNumeric("idpersonne");
        idEmployeur = statement.dbReadNumeric("idemployeur");
        nom = statement.dbReadString("nom");
        prenom = statement.dbReadString("prenom");
        genre = statement.dbReadNumeric("genre");
        dateNaissance = statement.dbReadDateAMJ("datenaissance");
        numAVS = JAUtil.formatAvs(statement.dbReadAVS("numavs"));
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) {
        _checkDate(statement.getTransaction(), dateNaissance, "La date de naissance est invalide");
        _checkAVS(statement.getTransaction(), numAVS, "Le numéro AVS est invalide");
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("idpersonne", this._dbWriteString(statement.getTransaction(), idPersonne, ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("idpersonne", this._dbWriteNumeric(statement.getTransaction(), idPersonne, "idPersonne"));
        statement.writeField("idemployeur",
                this._dbWriteNumeric(statement.getTransaction(), idEmployeur, "idEmployeur"));
        statement.writeField("nom", this._dbWriteString(statement.getTransaction(), nom, "nom"));
        statement.writeField("prenom", this._dbWriteString(statement.getTransaction(), prenom, "prenom"));
        statement.writeField("genre", this._dbWriteNumeric(statement.getTransaction(), genre, "genre"));
        statement.writeField("datenaissance",
                this._dbWriteDateAMJ(statement.getTransaction(), dateNaissance, "dateNaissance"));
        statement.writeField("numavs",
                this._dbWriteAVS(statement.getTransaction(), JAStringFormatter.deformatAvs(numAVS), "numAVS"));
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public COEmployeur getEmployeur() {
        return employeur;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @return Le libellé du genre
     */
    public String getGenreLibelle() {
        return getSession().getCodeLibelle(genre);
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdEmployeur() {
        return idEmployeur;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdPersonne() {
        return idPersonne;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getNumAVS() {
        return numAVS;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Actualise les objets liés
     * 
     * @param transaction
     *            La transaction courante
     */
    private void refreshLinks(BTransaction transaction) {
        employeur.setSession(getSession());
        employeur.setIdEmployeur(idEmployeur);
        try {
            employeur.retrieve(transaction);
        } catch (Exception e) {
            _addError(transaction, e.toString());
        }
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDateNaissance(String string) {
        dateNaissance = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setGenre(String string) {
        genre = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdEmployeur(String string) {
        idEmployeur = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdPersonne(String string) {
        idPersonne = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setNom(String string) {
        nom = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setNumAVS(String string) {
        numAVS = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setPrenom(String string) {
        prenom = string;
    }

}