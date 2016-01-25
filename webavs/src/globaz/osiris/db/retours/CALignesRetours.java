package globaz.osiris.db.retours;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * 
 * @author mmo Cette classe représente l'entité CALIREP qui est une table contenant les lignes de détail du retour
 * 
 *         Ces lignes stockent la manière dont est ventilé le montant du retour (compensation de sections existantes ou
 *         ordres de versements)
 * 
 */
public class CALignesRetours extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_ETAT_LIGNE_COMPENSATION = "252002";
    public final static String CS_ETAT_LIGNE_REPAIEMENT = "252001";
    public final static String FIELDNAME_ID_DOMAINE = "IDDOMAINE";
    public final static String FIELDNAME_ID_EXTERNE = "IDEXTERNE";
    public final static String FIELDNAME_ID_JOURNAL = "IDJOURNAL";
    public final static String FIELDNAME_ID_LIGNE_RETOUR = "IDLIGNERETOUR";
    public final static String FIELDNAME_ID_RETOUR = "IDRETOUR";
    public final static String FIELDNAME_ID_SECTION = "IDSECTION";
    public final static String FIELDNAME_ID_TIERS = "IDTIERS";
    public static final String FIELDNAME_MONTANT = "MONTANT";

    public static final String FIELDNAME_TYPE = "TYPE";
    public final static String TABLE_NAME_LIGNES_RETOURS = "CALIREP";

    private String csType = "";
    private String idDomaine = "";
    private String idExterne = "";
    private String idJournal = "";
    private String idLigneRetour = "";
    private String idRetour = "";
    private String idSection = "";
    private String idTiers = "";
    private String montant = "";

    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        updateEtatRetour(transaction);
    }

    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        updateEtatRetour(transaction);
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdLigneRetour(this._incCounter(transaction, "0"));
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
    }

    @Override
    protected String _getTableName() {
        return CALignesRetours.TABLE_NAME_LIGNES_RETOURS;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idLigneRetour = statement.dbReadNumeric(CALignesRetours.FIELDNAME_ID_LIGNE_RETOUR);
        idRetour = statement.dbReadNumeric(CALignesRetours.FIELDNAME_ID_RETOUR);
        idTiers = statement.dbReadNumeric(CALignesRetours.FIELDNAME_ID_TIERS);
        idDomaine = statement.dbReadNumeric(CALignesRetours.FIELDNAME_ID_DOMAINE);
        idSection = statement.dbReadNumeric(CALignesRetours.FIELDNAME_ID_SECTION);
        csType = statement.dbReadNumeric(CALignesRetours.FIELDNAME_TYPE);
        montant = statement.dbReadNumeric(CALignesRetours.FIELDNAME_MONTANT);
        idExterne = statement.dbReadNumeric(CALignesRetours.FIELDNAME_ID_EXTERNE);
        idJournal = statement.dbReadNumeric(CALignesRetours.FIELDNAME_ID_JOURNAL);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // L'di retour est obligatoire
        if (JadeStringUtil.isBlankOrZero(getIdRetour())) {
            _addError(statement.getTransaction(), getSession().getLabel("ID_RETOUR_OBLIGATOIRE"));
        }
        // Si mode repaiement, id tiers adresse et domaine obligatoire
        if (CALignesRetours.CS_ETAT_LIGNE_REPAIEMENT.equals(getCsType())) {
            if (JadeStringUtil.isBlankOrZero(getIdTiers()) || JadeStringUtil.isBlankOrZero(getIdDomaine())) {
                _addError(statement.getTransaction(), getSession().getLabel("ADRESSE_DOMAINE_OBLIGATOIRE"));
            }
        }
        // Si mode compensation, id section obligatoire
        if (CALignesRetours.CS_ETAT_LIGNE_COMPENSATION.equals(getCsType())) {
            if (JadeStringUtil.isBlankOrZero(getIdSection())) {
                _addError(statement.getTransaction(), getSession().getLabel("SECTION_OBLIGATOIRE"));
            }
        }
        // Type obligatoire
        if (JadeStringUtil.isBlankOrZero(getCsType())) {
            _addError(statement.getTransaction(), getSession().getLabel("TYPE_OBLIGATOIRE"));
        }
        // Montant obligatoire
        if (JadeStringUtil.isBlankOrZero(getMontant())) {
            _addError(statement.getTransaction(), getSession().getLabel("MONTANT_OBLIGATOIRE"));
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CALignesRetours.FIELDNAME_ID_LIGNE_RETOUR,
                this._dbWriteNumeric(statement.getTransaction(), idLigneRetour, "idLigneRetour"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CALignesRetours.FIELDNAME_ID_LIGNE_RETOUR,
                this._dbWriteNumeric(statement.getTransaction(), idLigneRetour, "idLigneRetour"));
        statement.writeField(CALignesRetours.FIELDNAME_ID_RETOUR,
                this._dbWriteNumeric(statement.getTransaction(), idRetour, "idRetour"));
        statement.writeField(CALignesRetours.FIELDNAME_ID_TIERS,
                this._dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(CALignesRetours.FIELDNAME_ID_DOMAINE,
                this._dbWriteNumeric(statement.getTransaction(), idDomaine, "idDomaine"));
        statement.writeField(CALignesRetours.FIELDNAME_ID_SECTION,
                this._dbWriteNumeric(statement.getTransaction(), idSection, "idSection"));
        statement.writeField(CALignesRetours.FIELDNAME_TYPE,
                this._dbWriteNumeric(statement.getTransaction(), csType, "csType"));
        statement.writeField(CALignesRetours.FIELDNAME_MONTANT,
                this._dbWriteNumeric(statement.getTransaction(), montant, "montant"));
        statement.writeField(CALignesRetours.FIELDNAME_ID_EXTERNE,
                this._dbWriteNumeric(statement.getTransaction(), idExterne, "idExterne"));
        statement.writeField(CALignesRetours.FIELDNAME_ID_JOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), idJournal, "idJournal"));
    }

    public String getCsType() {
        return csType;
    }

    public String getIdDomaine() {
        return idDomaine;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdLigneRetour() {
        return idLigneRetour;
    }

    public String getIdRetour() {
        return idRetour;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontant() {
        return montant;
    }

    private CARetours loadRetour(BTransaction transaction) throws Exception {

        CARetours theRetours = new CARetours();
        theRetours.setSession(getSession());
        theRetours.setIdRetour(getIdRetour());
        theRetours.retrieve(transaction);

        return theRetours;

    }

    public void setCsType(String csType) {
        this.csType = csType;
    }

    public void setIdDomaine(String idDomaine) {
        this.idDomaine = idDomaine;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdLigneRetour(String idLigneRetour) {
        this.idLigneRetour = idLigneRetour;
    }

    public void setIdRetour(String idRetour) {
        this.idRetour = idRetour;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    private void updateEtatRetour(BTransaction transaction) throws Exception {

        CARetours theRetours = loadRetour(transaction);

        if (CARetours.CS_ETAT_RETOUR_SUSPENS.equalsIgnoreCase(theRetours.getCsEtatRetour())) {
            if (theRetours.getSoldeRetour(transaction).doubleValue() == 0) {
                theRetours.setCsEtatRetour(CARetours.CS_ETAT_RETOUR_TRAITE);
                theRetours.update(transaction);
            }

        } else if (CARetours.CS_ETAT_RETOUR_TRAITE.equalsIgnoreCase(theRetours.getCsEtatRetour())) {
            if (theRetours.getSoldeRetour(transaction).doubleValue() != 0) {
                theRetours.setCsEtatRetour(CARetours.CS_ETAT_RETOUR_SUSPENS);
                theRetours.update(transaction);
            }
        }
    }
}
