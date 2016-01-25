/*
 * Créé le 15 fevr. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class REPrestationDue extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CS_ETAT = "YMTETA";
    public static final String FIELDNAME_CS_TYPE = "YMTTYP";
    public static final String FIELDNAME_CS_TYPE_PAIEMENT = "YMTTPA";
    public static final String FIELDNAME_DATE_DEBUT_PAIEMENT = "YMDDPA";
    public static final String FIELDNAME_DATE_FIN_PAIEMENT = "YMDFPA";
    public static final String FIELDNAME_ID_INTERET_MORATOIRE = "YMIIMO";
    public static final String FIELDNAME_ID_PRESTATION_DUE = "YMIPRD";
    public static final String FIELDNAME_ID_RENTE_ACCORDEE = "YMIRAC";
    public static final String FIELDNAME_MONTANT = "YMMMON";
    public static final String FIELDNAME_MONTANT_REDUCTION_ANTICIPATION = "YMMREA";
    public static final String FIELDNAME_MONTANT_SUPPLEMENT_AJOURNEMENT = "YMMSUA";

    public static final String FIELDNAME_RAM = "YMMRAM";
    public static final String FIELDNAME_TAUX_REDUCTION_ANTICIPATION = "YMMTRA";
    public static final String TABLE_NAME_PRESTATIONS_DUES = "REPRSDU";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csEtat = "";

    private String csType = "";
    private String csTypePaiement = "";

    // Format YYYYMM
    private String dateDebutPaiement = "";
    private String dateFinPaiement = "";
    private String idInteretMoratoire = "";
    private String idPrestationDue = "";
    private String idRenteAccordee = "";
    private String montant = "";
    private String montantReductionAnticipation = "";

    private String montantSupplementAjournement = "";
    private String ram = "";
    private String tauxReductionAnticipation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * initialise la valeur de Id période api
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdPrestationDue(_incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_PRESTATIONS_DUES;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idPrestationDue = statement.dbReadNumeric(FIELDNAME_ID_PRESTATION_DUE);
        dateDebutPaiement = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(FIELDNAME_DATE_DEBUT_PAIEMENT));
        dateFinPaiement = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(FIELDNAME_DATE_FIN_PAIEMENT));
        montant = statement.dbReadNumeric(FIELDNAME_MONTANT);

        montantSupplementAjournement = statement.dbReadNumeric(FIELDNAME_MONTANT_SUPPLEMENT_AJOURNEMENT);
        montantReductionAnticipation = statement.dbReadNumeric(FIELDNAME_MONTANT_REDUCTION_ANTICIPATION);
        tauxReductionAnticipation = statement.dbReadNumeric(FIELDNAME_TAUX_REDUCTION_ANTICIPATION);

        ram = statement.dbReadNumeric(FIELDNAME_RAM);
        csTypePaiement = statement.dbReadNumeric(FIELDNAME_CS_TYPE_PAIEMENT);
        csType = statement.dbReadNumeric(FIELDNAME_CS_TYPE);
        csEtat = statement.dbReadNumeric(FIELDNAME_CS_ETAT);
        idRenteAccordee = statement.dbReadNumeric(FIELDNAME_ID_RENTE_ACCORDEE);
        idInteretMoratoire = statement.dbReadNumeric(FIELDNAME_ID_INTERET_MORATOIRE);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        // Test pour savoir si la date de début et fin de paiement a le bon
        // format
        JADate dateDebut = new JADate(getDateDebutPaiement());

        if (getDateDebutPaiement().length() != 7) {
            _addError(statement.getTransaction(), getSession().getLabel("JSP_MVE_D_ERREUR_FORMAT_DATE_DEBUT_PAIEMENT"));
        } else if (dateDebut.getMonth() > 12 || dateDebut.getMonth() < 1) {
            _addError(statement.getTransaction(), getSession().getLabel("JSP_MVE_D_ERREUR_FORMAT_DATE_DEBUT_PAIEMENT"));
        }

        // La date de fin de paiement peut ne pas être renseignée
        if (!JadeStringUtil.isEmpty(getDateFinPaiement())) {
            JADate DateFin = new JADate(getDateFinPaiement());
            if (getDateFinPaiement().length() != 7) {
                _addError(statement.getTransaction(), getSession()
                        .getLabel("JSP_MVE_D_ERREUR_FORMAT_DATE_FIN_PAIEMENT"));
            } else if (DateFin.getMonth() > 12 || DateFin.getMonth() < 1) {
                _addError(statement.getTransaction(), getSession()
                        .getLabel("JSP_MVE_D_ERREUR_FORMAT_DATE_FIN_PAIEMENT"));
            }
        }
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_PRESTATION_DUE,
                _dbWriteNumeric(statement.getTransaction(), idPrestationDue, "idPrestationDue"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_ID_PRESTATION_DUE,
                _dbWriteNumeric(statement.getTransaction(), idPrestationDue, "idPrestationDue"));
        statement.writeField(
                FIELDNAME_DATE_DEBUT_PAIEMENT,
                _dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateDebutPaiement), "dateDebutPaiement"));
        statement.writeField(
                FIELDNAME_DATE_FIN_PAIEMENT,
                _dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateFinPaiement), "dateFinPaiement"));
        statement.writeField(FIELDNAME_MONTANT, _dbWriteNumeric(statement.getTransaction(), montant, "montant"));

        statement.writeField(
                FIELDNAME_MONTANT_REDUCTION_ANTICIPATION,
                _dbWriteNumeric(statement.getTransaction(), montantReductionAnticipation,
                        "montantReductionAnticipation"));
        statement.writeField(
                FIELDNAME_MONTANT_SUPPLEMENT_AJOURNEMENT,
                _dbWriteNumeric(statement.getTransaction(), montantSupplementAjournement,
                        "montantSupplementAjournement"));
        statement.writeField(FIELDNAME_TAUX_REDUCTION_ANTICIPATION,
                _dbWriteNumeric(statement.getTransaction(), tauxReductionAnticipation, "tauxReductionAnticipation"));

        statement.writeField(FIELDNAME_RAM, _dbWriteNumeric(statement.getTransaction(), ram, "ram"));
        statement.writeField(FIELDNAME_CS_TYPE_PAIEMENT,
                _dbWriteNumeric(statement.getTransaction(), csTypePaiement, "csTypePaiement"));
        statement.writeField(FIELDNAME_CS_TYPE, _dbWriteNumeric(statement.getTransaction(), csType, "csType"));
        statement.writeField(FIELDNAME_CS_ETAT, _dbWriteNumeric(statement.getTransaction(), csEtat, "csEtat"));
        statement.writeField(FIELDNAME_ID_RENTE_ACCORDEE,
                _dbWriteNumeric(statement.getTransaction(), idRenteAccordee, "idRenteAccordee"));
        statement.writeField(FIELDNAME_ID_INTERET_MORATOIRE,
                _dbWriteNumeric(statement.getTransaction(), idInteretMoratoire, "idInteretMoratoire"));
    }

    /**
     * @return
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * @return
     */
    public String getCsType() {
        return csType;
    }

    /**
     * @return
     */
    public String getCsTypePaiement() {
        return csTypePaiement;
    }

    /**
     * @return
     */
    public String getDateDebutPaiement() {
        return dateDebutPaiement;
    }

    /**
     * @return
     */
    public String getDateFinPaiement() {
        return dateFinPaiement;
    }

    /**
     * @return
     */
    public String getIdInteretMoratoire() {
        return idInteretMoratoire;
    }

    /**
     * @return the idPrestationDue
     */
    public String getIdPrestationDue() {
        return idPrestationDue;
    }

    /**
     * @return
     */
    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    /**
     * @return
     */
    public String getMontant() {
        return montant;
    }

    public String getMontantReductionAnticipation() {
        return montantReductionAnticipation;
    }

    public String getMontantSupplementAjournement() {
        return montantSupplementAjournement;
    }

    /**
     * @return
     */
    public String getRam() {
        return ram;
    }

    public String getTauxReductionAnticipation() {
        return tauxReductionAnticipation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    /**
     * @param string
     */
    public void setCsEtat(String string) {
        csEtat = string;
    }

    /**
     * @param string
     */
    public void setCsType(String string) {
        csType = string;
    }

    /**
     * @param string
     */
    public void setCsTypePaiement(String string) {
        csTypePaiement = string;
    }

    /**
     * @param string
     */
    public void setDateDebutPaiement(String string) {
        dateDebutPaiement = string;
    }

    /**
     * @param string
     */
    public void setDateFinPaiement(String string) {
        dateFinPaiement = string;
    }

    /**
     * @param string
     */
    public void setIdInteretMoratoire(String string) {
        idInteretMoratoire = string;
    }

    /**
     * @param idPrestationDue
     *            the idPrestationDue to set
     */
    public void setIdPrestationDue(String idPrestationDue) {
        this.idPrestationDue = idPrestationDue;
    }

    /**
     * @param string
     */
    public void setIdRenteAccordee(String string) {
        idRenteAccordee = string;
    }

    /**
     * @param string
     */
    public void setMontant(String string) {
        montant = string;
    }

    public void setMontantReductionAnticipation(String montantReductionAnticipation) {
        this.montantReductionAnticipation = montantReductionAnticipation;
    }

    public void setMontantSupplementAjournement(String montantSupplementAjournement) {
        this.montantSupplementAjournement = montantSupplementAjournement;
    }

    /**
     * @param string
     */
    public void setRam(String string) {
        ram = string;
    }

    public void setTauxReductionAnticipation(String tauxReductionAnticipation) {
        this.tauxReductionAnticipation = tauxReductionAnticipation;
    }

}
