package globaz.helios.db.comptes;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author user
 * @version 1.1
 * 
 */
public class CGExtendedBalMvtJournal extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String date = new String();
    private String dateValeur = new String();
    private String idEtat = new String();
    private String libelle = new String();
    private String montant = new String();
    private String numero = new String();

    /**
     * Commentaire relatif au constructeur CGCompteOfas
     */
    public CGExtendedBalMvtJournal() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
    }

    @Override
    protected String _getFields(BStatement statement) {

        return _getCollection() + "CGJOURP.numero, " + _getCollection() + "CGJOURP.libelle, " + _getCollection()
                + "CGJOURP.date, " + _getCollection() + "CGJOURP.dateValeur, " + _getCollection() + "CGJOURP.idEtat, "
                + "sum (" + _getCollection() + "CGECRIP.montant) as montant ";
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return _getCollection() + "CGJOURP";
    }

    /**
     * cfcp.CGCOMTP* read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {

        numero = statement.dbReadNumeric("NUMERO");
        libelle = statement.dbReadString("LIBELLE");
        date = statement.dbReadNumeric("DATE");
        dateValeur = statement.dbReadNumeric("DATEVALEUR");
        numero = statement.dbReadNumeric("NUMERO");
        idEtat = statement.dbReadNumeric("IDETAT");
        montant = statement.dbReadNumeric("MONTANT");

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
    }

    public BManager[] getChilds() {
        return null;
    }

    /**
     * Returns the date.
     * 
     * @return String
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns the dateValeur.
     * 
     * @return String
     */
    public String getDateValeur() {
        return dateValeur;
    }

    /**
     * Returns the idEtat.
     * 
     * @return String
     */
    public String getIdEtat() {
        return idEtat;
    }

    /**
     * Returns the libelle.
     * 
     * @return String
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Returns the montant.
     * 
     * @return String
     */
    public String getMontant() {
        return montant;
    }

    /**
     * Returns the numero.
     * 
     * @return String
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Sets the date.
     * 
     * @param date
     *            The date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Sets the dateValeur.
     * 
     * @param dateValeur
     *            The dateValeur to set
     */
    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    /**
     * Sets the idEtat.
     * 
     * @param idEtat
     *            The idEtat to set
     */
    public void setIdEtat(String idEtat) {
        this.idEtat = idEtat;
    }

    /**
     * Sets the libelle.
     * 
     * @param libelle
     *            The libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Sets the montant.
     * 
     * @param montant
     *            The montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * Sets the numero.
     * 
     * @param numero
     *            The numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

}
