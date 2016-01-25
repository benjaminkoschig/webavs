package globaz.osiris.db.comptes.extrait;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.osiris.api.APITypeSection;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CATypeSection;

/**
 * @author dda CAOPERP table entity. Use for the "Extrait de Compte" function.
 */
public class CAExtraitCompte extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String SECTIONDATE_FIELD = "SECTIONDATE";

    // CAOPERP variable
    private String date;
    // CAJOURP date valeur
    private String dateJournal;
    // CARERUP variable
    private String idCodeReference;
    // CASECTP variable
    private String idExterne;
    private String idJournal;
    private String idRubrique;
    private String idSection;
    private String idSectionCompensation;

    private String idTypeOperation;

    private String idTypeSection;
    private String libelle;
    private String libelleExtrait;
    // CARUBRP variable
    private String libelleExtraitCompte;

    private String montant;
    private String provenancePmt;

    private CARubrique rubrique = null;

    private String sectionCompensationDeSur;

    private String sectionDate;
    private double sum;
    private CATypeSection typeSection = null;

    /**
     * Return le nom de la table (CAOPERP).
     */
    @Override
    protected String _getTableName() {
        return CAOperation.TABLE_CAOPERP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        date = statement.dbReadDateAMJ(CAOperation.FIELD_DATE);
        idSection = statement.dbReadNumeric(CAOperation.FIELD_IDSECTION);
        idTypeOperation = statement.dbReadString(CAOperation.FIELD_IDTYPEOPERATION);
        montant = statement.dbReadNumeric(CAOperation.FIELD_MONTANT);

        idExterne = statement.dbReadString(CASection.FIELD_IDEXTERNE);
        idTypeSection = statement.dbReadNumeric(CASection.FIELD_IDTYPESECTION);
        sectionDate = statement.dbReadDateAMJ(CASection.FIELD_DATESECTION);
        libelleExtrait = statement.dbReadString(CARubrique.FIELD_LIBELLEEXTRAIT);
        idRubrique = statement.dbReadNumeric(CARubrique.FIELD_IDRUBRIQUE);
        provenancePmt = statement.dbReadNumeric(CAOperation.FIELD_PROVENANCEPMT);

        idJournal = statement.dbReadNumeric(CAOperation.FIELD_IDJOURNAL);

        dateJournal = statement.dbReadDateAMJ(CAJournal.FIELD_DATEVALEURCG);

        idSectionCompensation = statement.dbReadNumeric(CAOperation.FIELD_IDSECTION_COMPENSATION);
        sectionCompensationDeSur = statement.dbReadString(CAOperation.FIELD_SECTION_COMPENSATION_DE_SUR);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        try {
            BSessionUtil.checkDateGregorian(getSession(), getDate());
        } catch (Exception ex) {
            _addError(statement.getTransaction(), "Invalid date");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Not needed here
    }

    /**
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * @return the dateJournal
     */
    public String getDateJournal() {
        return dateJournal;
    }

    /**
     * @return the idCodeReference
     */
    public String getIdCodeReference() {
        return idCodeReference;
    }

    /**
     * @return
     */
    public String getIdExterne() {
        return idExterne;
    }

    /**
     * @return the idJournal
     */
    public String getIdJournal() {
        return idJournal;
    }

    /**
     * @return the idRubrique
     */
    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * @return
     */
    public String getIdSection() {
        return idSection;
    }

    public String getIdSectionCompensation() {
        return idSectionCompensation;
    }

    /**
     * @return
     */
    public String getIdTypeOperation() {
        return idTypeOperation;
    }

    /**
     * @return
     */
    public String getIdTypeSection() {
        return idTypeSection;
    }

    /**
     * Libelle de l'ecriture
     * 
     * @return the libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * @return the libelleExtrait
     */
    public String getLibelleExtrait() {
        return libelleExtrait;
    }

    /**
     * @return
     */
    public String getLibelleExtraitCompte() {
        return libelleExtraitCompte;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the provenancePmt
     */
    public String getProvenancePmt() {
        return provenancePmt;
    }

    /**
     * @return the rubrique
     */
    public CARubrique getRubrique() {
        if (rubrique == null) {
            rubrique = new CARubrique();
            rubrique.setIdRubrique(getIdRubrique());
            rubrique.setSession(getSession());
            try {
                rubrique.retrieve();
            } catch (Exception e) {
                rubrique = null;
            }
        }
        return rubrique;
    }

    public String getSectionCompensationDeSur() {
        return sectionCompensationDeSur;
    }

    /**
     * @return
     */
    public String getSectionDate() {
        return sectionDate;
    }

    /**
     * @return
     */
    public double getSum() {
        return sum;
    }

    public APITypeSection getTypeSection() {
        if (typeSection == null) {
            typeSection = new CATypeSection();
            typeSection.setISession(getSession());
            typeSection.setIdTypeSection(getIdTypeSection());
            try {
                typeSection.retrieve();
                if (typeSection.isNew()) {
                    typeSection = null;
                }
            } catch (Exception e) {
                typeSection = null;
            }
        }
        return typeSection;
    }

    /**
     * @param string
     */
    public void setDate(String string) {
        date = string;
    }

    /**
     * @param dateJournal
     *            the dateJournal to set
     */
    public void setDateJournal(String dateJournal) {
        this.dateJournal = dateJournal;
    }

    /**
     * @param idCodeReference
     *            the idCodeReference to set
     */
    public void setIdCodeReference(String idCodeReference) {
        this.idCodeReference = idCodeReference;
    }

    /**
     * @param string
     */
    public void setIdExterne(String string) {
        idExterne = string;
    }

    /**
     * @param idJournal
     *            the idJournal to set
     */
    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    /**
     * @param idRubrique
     *            the idRubrique to set
     */
    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    /**
     * @param string
     */
    public void setIdSection(String id) {
        idSection = id;
    }

    public void setIdSectionCompensation(String idSectionCompensation) {
        this.idSectionCompensation = idSectionCompensation;
    }

    /**
     * @param string
     */
    public void setIdTypeOperation(String string) {
        idTypeOperation = string;
    }

    /**
     * @param string
     */
    public void setIdTypeSection(String string) {
        idTypeSection = string;
    }

    /**
     * @param libelle
     *            the libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * @param libelleExtrait
     *            the libelleExtrait to set
     */
    public void setLibelleExtrait(String libelleExtrait) {
        this.libelleExtrait = libelleExtrait;
    }

    /**
     * @param string
     */
    public void setLibelleExtraitCompte(String string) {
        libelleExtraitCompte = string;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * @param provenancePmt
     *            the provenancePmt to set
     */
    public void setProvenancePmt(String provenancePmt) {
        this.provenancePmt = provenancePmt;
    }

    /**
     * @param rubrique
     *            the rubrique to set
     */
    public void setRubrique(CARubrique rubrique) {
        this.rubrique = rubrique;
    }

    public void setSectionCompensationDeSur(String sectionCompensationDeSur) {
        this.sectionCompensationDeSur = sectionCompensationDeSur;
    }

    /**
     * @param string
     */
    public void setSectionDate(String string) {
        sectionDate = string;
    }

    /**
     * @param string
     */
    public void setSum(double sum) {
        this.sum = sum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#toString()
     */
    public String toMyString() {
        return this.getClass().getName() + " : " + "\nDate : " + date + "\nidSection : " + idSection
                + "\nIdTypeOperation : " + getIdTypeOperation() + "\nsum : " + sum + "\nidExterne : " + getIdExterne()
                + "\ngetIdTypeSection : " + getIdTypeSection() + "\nSection date : " + getSectionDate();
    }

}
