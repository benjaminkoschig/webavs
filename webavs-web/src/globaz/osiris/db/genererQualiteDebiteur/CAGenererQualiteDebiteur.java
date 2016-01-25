package globaz.osiris.db.genererQualiteDebiteur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;

/**
 * @author dda CAOPERP table entity. Use for the "Extrait de Compte" function.
 */
public class CAGenererQualiteDebiteur extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_IDCOMPTEANNEXE = "OAICOA";
    public static final String FIELD_IDCONTENTIEUX = "OAICON";
    public static final String FIELD_IDSECTION = "OAISEC";
    public static final String FIELD_IDSEQUENCE = "OFISEQ";
    public static final String FIELD_NOMBRE_DELAI_MUTE = "OANDMU";
    public static final String TABLE_COCAVSP = "COCAVSP";

    // Variable

    private String categorieSection;
    private String idCompteAnnexe;
    private String idContentieux;
    private String idSection;
    private String idSequence;
    private String nombreDelaiMute;

    /**
     * Return le nom de la table.
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
        setIdContentieux(statement.dbReadNumeric(CAGenererQualiteDebiteur.FIELD_IDCONTENTIEUX));
        setIdCompteAnnexe(statement.dbReadNumeric(CAGenererQualiteDebiteur.FIELD_IDCOMPTEANNEXE));
        setIdSection(statement.dbReadNumeric(CAGenererQualiteDebiteur.FIELD_IDSECTION));
        setIdSequence(statement.dbReadNumeric(CAGenererQualiteDebiteur.FIELD_IDSEQUENCE));
        setNombreDelaiMute(statement.dbReadNumeric(CAGenererQualiteDebiteur.FIELD_NOMBRE_DELAI_MUTE));
        setCategorieSection(statement.dbReadNumeric(CASection.FIELD_CATEGORIESECTION));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

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
     * @return the categorieSection
     */
    public String getCategorieSection() {
        return categorieSection;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return the idContentieux
     */
    public String getIdContentieux() {
        return idContentieux;
    }

    /**
     * @return
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * @return the idSequence
     */
    public String getIdSequence() {
        return idSequence;
    }

    /**
     * @return the nombreDelaiMute
     */
    public String getNombreDelaiMute() {
        return nombreDelaiMute;
    }

    /**
     * @param categorieSection
     *            the categorieSection to set
     */
    public void setCategorieSection(String categorieSection) {
        this.categorieSection = categorieSection;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * @param idContentieux
     *            the idContentieux to set
     */
    public void setIdContentieux(String idContentieux) {
        this.idContentieux = idContentieux;
    }

    /**
     * @param string
     */
    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    /**
     * @param idSequence
     *            the idSequence to set
     */
    public void setIdSequence(String idSequence) {
        this.idSequence = idSequence;
    }

    /**
     * @param nombreDelaiMute
     *            the nombreDelaiMute to set
     */
    public void setNombreDelaiMute(String nombreDelaiMute) {
        this.nombreDelaiMute = nombreDelaiMute;
    }

}
