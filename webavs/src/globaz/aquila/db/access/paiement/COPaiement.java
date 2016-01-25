package globaz.aquila.db.access.paiement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;

/**
 * @author dda CAOPERP table entity. Use for the "Extrait de Compte" function.
 */
public class COPaiement extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELD_DATE = CAOperation.FIELD_DATE;

    public static final String FIELD_IDCOMPTE = CAOperation.FIELD_IDCOMPTE;
    public static final String FIELD_IDCOMPTEANNEXE = CASection.FIELD_IDCOMPTEANNEXE;
    public static final String FIELD_IDEXTERNEROLE = CACompteAnnexe.FIELD_IDEXTERNEROLE;
    public static final String FIELD_IDSECTION = CASection.FIELD_IDSECTION;
    public static final String FIELD_MONTANT = CAOperation.FIELD_MONTANT;
    public static final String FIELD_PROVENANCEPMT = CAOperation.FIELD_PROVENANCEPMT;

    public static final String SECTIONDATE_FIELD = "SECTIONDATE";
    private String dateOperation;
    private String idCompte;
    private String idCompteAnnexe;
    private String idSection;
    private String montant;
    private String provenancePmt;

    /**
     * Return le nom de la table (CAOPERP).
     */
    @Override
    protected String _getTableName() {
        return "";

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdSection(statement.dbReadNumeric(COPaiement.FIELD_IDSECTION));
        setIdCompteAnnexe(statement.dbReadNumeric(COPaiement.FIELD_IDCOMPTEANNEXE));
        setDateOperation(statement.dbReadDateAMJ(COPaiement.FIELD_DATE));
        setMontant(statement.dbReadNumeric(COPaiement.FIELD_MONTANT, 2));
        setIdCompte(statement.dbReadNumeric(COPaiement.FIELD_IDCOMPTE));
        setProvenancePmt(statement.dbReadNumeric(COPaiement.FIELD_PROVENANCEPMT));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        try {
            BSessionUtil.checkDateGregorian(getSession(), getDateOperation());
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
        // Not needed here
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
     * @return la rubrique
     */
    public CARubrique getCompte() {
        CARubrique rubrique;
        rubrique = new CARubrique();
        rubrique.setISession(getSession());
        rubrique.setIdRubrique(getIdCompte());
        try {
            rubrique.retrieve();
            if (rubrique.isNew()) {
                rubrique = null;
            }
        } catch (Exception e) {
            rubrique = null;
        }
        return rubrique;
    }

    /**
     * Cette méhode retourne un compte annexe
     * 
     * @param idCompteAnnexe
     * @return CACompteAnnexe
     * @throws Exception
     */
    public CACompteAnnexe getCompteAnnexe() throws Exception {
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());
        compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());
        compteAnnexe.retrieve();
        return compteAnnexe;
    }

    public String getDateOperation() {
        return dateOperation;
    }

    public String getIdCompte() {
        return idCompte;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return l'idSection
     */
    public String getIdSection() {
        return idSection;
    }

    public String getLibelleEtape() throws Exception {
        String etape = "";
        if (getSection() != null) {
            etape = getSession().getCodeLibelle(getSection().getIdLastEtatAquila());
        }
        return etape;
    }

    public String getLibelleProvenance() {
        return getSession().getCodeLibelle(provenancePmt);
    }

    public String getMontant() {
        return montant;
    }

    public String getProvenancePmt() {
        return provenancePmt;
    }

    /**
     * Cette méthode retourne une section
     * 
     * @param idSection
     * @return CASection
     * @throws Exception
     */
    public CASection getSection() throws Exception {
        CASection section = new CASection();
        section.setSession(getSession());
        section.setIdSection(getIdSection());
        section.retrieve();
        return section;
    }

    public void setDateOperation(String dateOperation) {
        this.dateOperation = dateOperation;
    }

    public void setIdCompte(String idCompte) {
        this.idCompte = idCompte;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * @param string
     */
    public void setIdSection(String id) {
        idSection = id;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setProvenancePmt(String provenancePmt) {
        this.provenancePmt = provenancePmt;
    }

}
