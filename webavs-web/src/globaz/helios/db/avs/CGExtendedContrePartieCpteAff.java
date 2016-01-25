package globaz.helios.db.avs;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author user
 * @version 1.1
 * 
 */
public class CGExtendedContrePartieCpteAff extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String libelleDe = new String();
    private String libelleFr = new String();
    private String libelleIt = new String();
    private String libelleSectDe = new String();
    private String libelleSectFr = new String();
    private String libelleSectIt = new String();
    private java.lang.String montant = new String();
    private java.lang.String numeroCompteOfas = new String();

    /**
     * Commentaire relatif au constructeur CGCompteOfas
     */
    public CGExtendedContrePartieCpteAff() {
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

        return _getCollection() + "CGOFCPP.idexterne, " + "sum (CONTRE_ECRITURE.montant) as montant, "
                + _getCollection() + "CGOFCPP.libellefr, " + _getCollection() + "CGOFCPP.libellede, "
                + _getCollection() + "CGOFCPP.libelleit ";

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return _getCollection() + "CGPERIP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        numeroCompteOfas = statement.dbReadString("IDEXTERNE");
        montant = statement.dbReadNumeric("MONTANT");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");
        libelleSectFr = statement.dbReadString("LIBELLESECTFR");
        libelleSectDe = statement.dbReadString("LIBELLESECTDE");
        libelleSectIt = statement.dbReadString("LIBELLESECTIT");
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
     * Returns the libelleDe.
     * 
     * @return String
     */
    public String getLibelleDe() {
        return libelleDe;
    }

    /**
     * Returns the libelleFr.
     * 
     * @return String
     */
    public String getLibelleFr() {
        return libelleFr;
    }

    /**
     * Returns the libelleIt.
     * 
     * @return String
     */
    public String getLibelleIt() {
        return libelleIt;
    }

    /**
     * Returns the libelleSectDe.
     * 
     * @return String
     */
    public String getLibelleSectDe() {
        return libelleSectDe;
    }

    /**
     * Returns the libelleSectFr.
     * 
     * @return String
     */
    public String getLibelleSectFr() {
        return libelleSectFr;
    }

    /**
     * Returns the libelleSectIt.
     * 
     * @return String
     */
    public String getLibelleSectIt() {
        return libelleSectIt;
    }

    /**
     * Returns the montant.
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontant() {
        return montant;
    }

    /**
     * Returns the numeroCompteOfas.
     * 
     * @return java.lang.String
     */
    public java.lang.String getNumeroCompteOfas() {
        return numeroCompteOfas;
    }

    /**
     * Sets the libelleDe.
     * 
     * @param libelleDe
     *            The libelleDe to set
     */
    public void setLibelleDe(String libelleDe) {
        this.libelleDe = libelleDe;
    }

    /**
     * Sets the libelleFr.
     * 
     * @param libelleFr
     *            The libelleFr to set
     */
    public void setLibelleFr(String libelleFr) {
        this.libelleFr = libelleFr;
    }

    /**
     * Sets the libelleIt.
     * 
     * @param libelleIt
     *            The libelleIt to set
     */
    public void setLibelleIt(String libelleIt) {
        this.libelleIt = libelleIt;
    }

    /**
     * Sets the libelleSectDe.
     * 
     * @param libelleSectDe
     *            The libelleSectDe to set
     */
    public void setLibelleSectDe(String libelleSectDe) {
        this.libelleSectDe = libelleSectDe;
    }

    /**
     * Sets the libelleSectFr.
     * 
     * @param libelleSectFr
     *            The libelleSectFr to set
     */
    public void setLibelleSectFr(String libelleSectFr) {
        this.libelleSectFr = libelleSectFr;
    }

    /**
     * Sets the libelleSectIt.
     * 
     * @param libelleSectIt
     *            The libelleSectIt to set
     */
    public void setLibelleSectIt(String libelleSectIt) {
        this.libelleSectIt = libelleSectIt;
    }

    /**
     * Sets the montant.
     * 
     * @param montant
     *            The montant to set
     */
    public void setMontant(java.lang.String montant) {
        this.montant = montant;
    }

    /**
     * Sets the numeroCompteOfas.
     * 
     * @param numeroCompteOfas
     *            The numeroCompteOfas to set
     */
    public void setNumeroCompteOfas(java.lang.String numeroCompteOfas) {
        this.numeroCompteOfas = numeroCompteOfas;
    }
}
