package globaz.osiris.db.comptes;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.util.JANumberFormatter;
import globaz.osiris.api.APIOperation;
import java.io.Serializable;
import java.math.BigDecimal;

public class CACompteur extends BEntity implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Clé alternée sur l'idCompteAnnexe, idRubrique, annee */
    public final static int AK_CPTA_RUB_ANNEE = 1;

    private static final String ANNEE_1948 = "1948";
    public static final String FIELD_ANNEE = "ANNEE";
    public static final String FIELD_CUMULCOTISATION = "CUMULCOTISATION";
    public static final String FIELD_CUMULMASSE = "CUMULMASSE";
    public static final String FIELD_IDCOMPTEANNEXE = "IDCOMPTEANNEXE";
    public static final String FIELD_IDCOMPTEUR = "IDCOMPTEUR";
    public static final String FIELD_IDRUBRIQUE = "IDRUBRIQUE";

    public static final String FIELD_VALEURINITIALECOT = "VALEURINITIALECOT";

    public static final String TABLE_CACPTRP = "CACPTRP";
    private String annee = new String();
    private String cumulCotisation = "0.00";
    private String cumulMasse = "0.00";
    private String idCompteAnnexe = new String();
    private String idCompteur = new String();
    private String idRubrique = new String();
    private CARubrique rubrique;

    private String valeurInitialeCot = "0.00";

    public CACompteur() {
        super();
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente le prochain numéro
        setIdCompteur(this._incCounter(transaction, idCompteur));
    }

    @Override
    protected String _getTableName() {
        return CACompteur.TABLE_CACPTRP;
    }

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idCompteur = statement.dbReadNumeric(CACompteur.FIELD_IDCOMPTEUR);
        idCompteAnnexe = statement.dbReadNumeric(CACompteur.FIELD_IDCOMPTEANNEXE);
        idRubrique = statement.dbReadNumeric(CACompteur.FIELD_IDRUBRIQUE);
        annee = statement.dbReadNumeric(CACompteur.FIELD_ANNEE);
        cumulMasse = statement.dbReadNumeric(CACompteur.FIELD_CUMULMASSE, 2);
        cumulCotisation = statement.dbReadNumeric(CACompteur.FIELD_CUMULCOTISATION, 2);
        valeurInitialeCot = statement.dbReadNumeric(CACompteur.FIELD_VALEURINITIALECOT, 2);
    }

    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdCompteur(), getSession().getLabel("7310"));
        _propertyMandatory(statement.getTransaction(), getIdCompteAnnexe(), getSession().getLabel("7060"));
        _propertyMandatory(statement.getTransaction(), getIdRubrique(), getSession().getLabel("7311"));
        _propertyMandatory(statement.getTransaction(), getAnnee(), getSession().getLabel("7312"));

        if (getAnnee().compareTo(CACompteur.ANNEE_1948) < 0) {
            _addError(statement.getTransaction(), getSession().getLabel("5130") + " " + getAnnee());
        }
    }

    @Override
    protected void _writeAlternateKey(globaz.globall.db.BStatement statement, int alternateKey)
            throws java.lang.Exception {
        // Clé alternée numéro 1 : idCompteAnnexe, idRubrique et année
        switch (alternateKey) {
            case AK_CPTA_RUB_ANNEE:
                statement.writeKey(CACompteur.FIELD_IDCOMPTEANNEXE,
                        this._dbWriteNumeric(statement.getTransaction(), getIdCompteAnnexe(), ""));
                statement.writeKey(CACompteur.FIELD_IDRUBRIQUE,
                        this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), ""));
                statement.writeKey(CACompteur.FIELD_ANNEE,
                        this._dbWriteNumeric(statement.getTransaction(), getAnnee(), ""));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(CACompteur.FIELD_IDCOMPTEUR,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteur(), ""));
    }

    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(CACompteur.FIELD_IDCOMPTEUR,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteur(), "idCompteur"));
        statement.writeField(CACompteur.FIELD_IDCOMPTEANNEXE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteAnnexe(), "idCompteAnnexe"));
        statement.writeField(CACompteur.FIELD_IDRUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
        statement.writeField(CACompteur.FIELD_ANNEE,
                this._dbWriteNumeric(statement.getTransaction(), getAnnee(), "annee"));
        statement.writeField(CACompteur.FIELD_CUMULMASSE,
                this._dbWriteNumeric(statement.getTransaction(), getCumulMasse(), "cumulMasse"));
        statement.writeField(CACompteur.FIELD_CUMULCOTISATION,
                this._dbWriteNumeric(statement.getTransaction(), getCumulCotisation(), "cumulCotisation"));
        statement.writeField(CACompteur.FIELD_VALEURINITIALECOT,
                this._dbWriteNumeric(statement.getTransaction(), getValeurInitialeCot(), "valeurInitialeCot"));
    }

    /**
     * Ajouter la masse et le montant d'une opération au compteur.
     * 
     * @param oper
     * @return
     */
    public FWMessage addMasseAndMontant(CAOperation oper) {
        FWMessage msg = null;

        if (oper.isInstanceOrSubClassOf(APIOperation.CAECRITURE)) {
            try {
                CAEcriture ecr = (CAEcriture) oper;
                BigDecimal montant = new BigDecimal(ecr.getMontant());
                BigDecimal masse = new BigDecimal(ecr.getMasse());
                BigDecimal tmpCumulMasse = new BigDecimal(getCumulMasse());
                BigDecimal tmpCumulCotisation = new BigDecimal(getCumulCotisation());

                tmpCumulMasse = tmpCumulMasse.add(masse);
                setCumulMasse(JANumberFormatter.formatNoQuote(tmpCumulMasse));

                tmpCumulCotisation = tmpCumulCotisation.add(montant);
                setCumulCotisation(JANumberFormatter.formatNoQuote(tmpCumulCotisation));
            } catch (Exception e) {
                msg = new FWMessage();
                msg.setMessageId("5131");
                msg.setComplement(e.getMessage());
                msg.setIdSource("CACompteur");
                msg.setTypeMessage(FWMessage.ERREUR);
            }
        }

        return msg;
    }

    public String getAnnee() {
        return annee;
    }

    public String getCumulCotisation() {
        return cumulCotisation;
    }

    public String getCumulMasse() {
        return cumulMasse;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdCompteur() {
        return idCompteur;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * Cette méthode permet de récupérer une rubrique. Utilisé dans l'écran.
     * 
     * @return globaz.osiris.db.comptes.CARubrique
     */
    public CARubrique getRubrique() {
        if (rubrique == null) {
            rubrique = new CARubrique();
            rubrique.setISession(getSession());
            rubrique.setIdRubrique(getIdRubrique());
            try {
                rubrique.retrieve();
                if (rubrique.isNew()) {
                    rubrique = null;
                }

            } catch (Exception e) {
                rubrique = null;
            }
        }

        return rubrique;
    }

    public String getValeurInitialeCot() {
        return valeurInitialeCot;
    }

    /**
     * Supprime la masse et le montant d'une opération du compteur.
     * 
     * @param oper
     * @return
     */
    public FWMessage removeMasseAndMontant(CAOperation oper) {
        FWMessage msg = null;

        if (oper.isInstanceOrSubClassOf(APIOperation.CAECRITURE)) {
            try {
                CAEcriture ecr = (CAEcriture) oper;
                BigDecimal montant = new BigDecimal(ecr.getMontant());
                BigDecimal masse = new BigDecimal(ecr.getMasse());
                BigDecimal tmpCumulMasse = new BigDecimal(getCumulMasse());
                BigDecimal tmpCumulCotisation = new BigDecimal(getCumulCotisation());

                tmpCumulMasse = tmpCumulMasse.subtract(masse);
                setCumulMasse(JANumberFormatter.formatNoQuote(tmpCumulMasse));

                tmpCumulCotisation = tmpCumulCotisation.subtract(montant);
                setCumulCotisation(JANumberFormatter.formatNoQuote(tmpCumulCotisation));
            } catch (Exception e) {
                msg = new FWMessage();
                msg.setMessageId("5168");
                msg.setComplement(e.getMessage());
                msg.setIdSource("CACompteur");
                msg.setTypeMessage(FWMessage.ERREUR);
            }
        }

        return msg;
    }

    public void setAnnee(String newAnnee) {
        annee = newAnnee;
    }

    public void setCumulCotisation(String newCumulCotisation) {
        cumulCotisation = newCumulCotisation;
    }

    public void setCumulMasse(String newCumulMasse) {
        cumulMasse = newCumulMasse;
    }

    public void setIdCompteAnnexe(String newIdCompteAnnexe) {
        idCompteAnnexe = newIdCompteAnnexe;
    }

    /**
     * Setter
     */
    public void setIdCompteur(String newIdCompteur) {
        idCompteur = newIdCompteur;
    }

    public void setIdRubrique(String newIdRubrique) {
        idRubrique = newIdRubrique;
    }

    public void setValeurInitialeCot(String newValeurInitialeCot) {
        valeurInitialeCot = newValeurInitialeCot;
    }

}
