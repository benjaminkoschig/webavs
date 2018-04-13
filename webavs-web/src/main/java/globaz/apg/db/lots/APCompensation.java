/*
 * Créé le 28 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.lots;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APCompensation extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_DETTES = "VNMDET";

    /**
	 */
    public static final String FIELDNAME_GENRE_PRESTATION = "VNTGEP";

    /**
     */
    public static final String FIELDNAME_IDAFFILIE = "VNIAFF";

    /**
     */
    public static final String FIELDNAME_IDCOMPENSATION = "VNICOM";

    /**
     */
    public static final String FIELDNAME_IDLOT = "VNILOT";

    /**
     */
    public static final String FIELDNAME_IDTIERS = "VNITIE";

    /**
     */
    public static final String FIELDNAME_MONTANTTOTAL = "VNMMOT";

    /**
     */
    public static final String FIELDNAME_ISINDEPENDANT = "VNIIND";

    /**
     */
    public static final String FIELDNAME_ISEMPLOYEUR = "VNIEMP";

    /**
     */
    public static final String FIELDNAME_ISPORTECOMPTE = "VNPORT";

    /**
     */
    public static final String TABLE_NAME = "APCOMPP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dettes = "";
    private String genrePrestation = "";
    private String idAffilie = "";
    private String idCompensation = "";
    private String idLot = "";
    private String idTiers = "";
    private String montantTotal = "";
    private Boolean isIndependant = false;
    private Boolean isEmployeur = false;
    private Boolean isPorteEnCompte = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        // suppression des factures à compenser
        APFactureACompenserManager factureACompenserManager = new APFactureACompenserManager();
        factureACompenserManager.setSession(getSession());
        factureACompenserManager.setForIdCompensationParente(idCompensation);
        factureACompenserManager.find(transaction);

        APFactureACompenser factureACompenser = null;

        for (int i = 0; i < factureACompenserManager.size(); i++) {
            factureACompenser = (APFactureACompenser) factureACompenserManager.getEntity(i);
            factureACompenser.delete(transaction);
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdCompensation(this._incCounter(transaction, "0"));
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return APCompensation.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAffilie = statement.dbReadNumeric(APCompensation.FIELDNAME_IDAFFILIE);
        idCompensation = statement.dbReadNumeric(APCompensation.FIELDNAME_IDCOMPENSATION);
        idLot = statement.dbReadNumeric(APCompensation.FIELDNAME_IDLOT);
        idTiers = statement.dbReadNumeric(APCompensation.FIELDNAME_IDTIERS);
        montantTotal = statement.dbReadNumeric(APCompensation.FIELDNAME_MONTANTTOTAL, 2);
        dettes = statement.dbReadNumeric(APCompensation.FIELDNAME_DETTES, 2);
        genrePrestation = statement.dbReadNumeric(APCompensation.FIELDNAME_GENRE_PRESTATION);
        isIndependant = statement.dbReadBoolean(APCompensation.FIELDNAME_ISINDEPENDANT);
        isEmployeur = statement.dbReadBoolean(APCompensation.FIELDNAME_ISEMPLOYEUR);
        isPorteEnCompte = statement.dbReadBoolean(APCompensation.FIELDNAME_ISPORTECOMPTE);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(APCompensation.FIELDNAME_IDCOMPENSATION,
                this._dbWriteNumeric(statement.getTransaction(), idCompensation, "idCompensation"));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        BTransaction transaction = statement.getTransaction();

        statement.writeField(APCompensation.FIELDNAME_IDAFFILIE,
                this._dbWriteNumeric(transaction, idAffilie, "idAffilie"));
        statement.writeField(APCompensation.FIELDNAME_IDCOMPENSATION,
                this._dbWriteNumeric(transaction, idCompensation, "idCompensation"));
        statement.writeField(APCompensation.FIELDNAME_IDLOT, this._dbWriteNumeric(transaction, idLot, "idLot"));
        statement.writeField(APCompensation.FIELDNAME_IDTIERS, this._dbWriteNumeric(transaction, idTiers, "idTiers"));
        statement.writeField(APCompensation.FIELDNAME_MONTANTTOTAL,
                this._dbWriteNumeric(transaction, montantTotal, "montantTotal"));
        statement.writeField(APCompensation.FIELDNAME_DETTES, this._dbWriteNumeric(transaction, dettes, "dettes"));
        statement.writeField(APCompensation.FIELDNAME_GENRE_PRESTATION,
                this._dbWriteNumeric(transaction, genrePrestation, "genrePrestation"));

        statement.writeField(APCompensation.FIELDNAME_ISINDEPENDANT,
                this._dbWriteBoolean(transaction, isIndependant, BConstants.DB_TYPE_BOOLEAN_CHAR, "isIndependant"));
        statement.writeField(APCompensation.FIELDNAME_ISEMPLOYEUR,
                this._dbWriteBoolean(transaction, isEmployeur, BConstants.DB_TYPE_BOOLEAN_CHAR, "isEmployeur"));
        statement.writeField(APCompensation.FIELDNAME_ISPORTECOMPTE,
                this._dbWriteBoolean(transaction, isPorteEnCompte, BConstants.DB_TYPE_BOOLEAN_CHAR, "isPorteEnCompte"));
    }

    /**
     * getter pour l'attribut dettes
     * 
     * @return la valeur courante de l'attribut dettes
     */
    public String getDettes() {
        return dettes;
    }

    /**
     * @return
     */
    public String getGenrePrestation() {
        return genrePrestation;
    }

    /**
     * getter pour l'attribut id affilie compensation
     * 
     * @return la valeur courante de l'attribut id affilie compensation
     */
    public String getIdAffilie() {
        return idAffilie;
    }

    /**
     * getter pour l'attribut id compensation
     * 
     * @return la valeur courante de l'attribut id compensation
     */
    public String getIdCompensation() {
        return idCompensation;
    }

    /**
     * getter pour l'attribut id lot
     * 
     * @return la valeur courante de l'attribut id lot
     */
    public String getIdLot() {
        return idLot;
    }

    /**
     * getter pour l'attribut id tiers compensation
     * 
     * @return la valeur courante de l'attribut id tiers compensation
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * getter pour l'attribut montant total
     * 
     * @return la valeur courante de l'attribut montant total
     */
    public String getMontantTotal() {
        return montantTotal;
    }

    /**
     * setter pour l'attribut dettes
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDettes(String string) {
        dettes = string;
    }

    /**
     * @param string
     */
    public void setGenrePrestation(String string) {
        genrePrestation = string;
    }

    /**
     * setter pour l'attribut id affilie compensation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAffilie(String string) {
        idAffilie = string;
    }

    /**
     * setter pour l'attribut id compensation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdCompensation(String string) {
        idCompensation = string;
    }

    /**
     * setter pour l'attribut id lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdLot(String string) {
        idLot = string;
    }

    /**
     * setter pour l'attribut id tiers compensation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * setter pour l'attribut montant total
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantTotal(String string) {
        montantTotal = string;
    }

    /**
     * setter pour l'attribut is employeur
     * 
     * @param isEmployeur
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsEmployeur(Boolean isEmployeur) {
        this.isEmployeur = isEmployeur;
    }

    /**
     * setter pour l'attribut is indépendant
     * 
     * @param isIndependant
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsIndependant(Boolean isIndependant) {
        this.isIndependant = isIndependant;
    }

    /**
     * getter pour l'attribut is indépendant
     * 
     * @return la valeur courante de l'attribut is indépendant
     */
    public Boolean getIsIndependant() {
        return isIndependant;
    }

    /**
     * getter pour l'attribut is employeur
     * 
     * @return la valeur courante de l'attribut is employeur
     */
    public Boolean getIsEmployeur() {
        return isEmployeur;
    }

    /**
     * getter pour l'attribut isPorte
     * 
     * @return la valeur courante de l'attribut is indépendant
     */

    public Boolean getIsPorteEnCompte() {
        return isPorteEnCompte;
    }

    /**
     * setter pour l'attribut isPorte
     * 
     * @return la valeur courante de l'attribut is indépendant
     */
    public void setIsPorteEnCompte(Boolean isPorteEnCompte) {
        this.isPorteEnCompte = isPorteEnCompte;
    }

}
