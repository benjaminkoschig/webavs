package globaz.lynx.db.journal;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;
import globaz.lynx.translation.LXCodeSystem;
import globaz.lynx.utils.LXUtils;
import globaz.osiris.process.journal.CAUtilsJournal;

public class LXJournal extends BEntity implements Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Code systeme ETAT
    public static final String CS_ETAT_ANNULE = "7400003";

    public static final String CS_ETAT_COMPTABILISE = "7400004";
    public static final String CS_ETAT_OUVERT = "7400001";
    public static final String CS_ETAT_TRAITEMENT = "7400002";
    public static final String CS_TYPE_JOURNALIER = "8000002";
    // Code systeme TYPE JOURNAUX
    public static final String CS_TYPE_STANDARD = "8000001";
    public static final String FIELD_CSETAT = "CSETAT";
    public static final String FIELD_CSTYPEJOURNAL = "CSTYPEJOURNAL";
    public static final String FIELD_DATECREATION = "DATECREATION";
    public static final String FIELD_DATEVALEURCG = "DATEVALEURCG";
    public static final String FIELD_IDJOURNAL = "IDJOURNAL";

    public static final String FIELD_IDJOURNALCG = "IDJOURNALCG";

    public static final String FIELD_IDORDREGROUPESRC = "IDORDREGROUPESRC";
    public static final String FIELD_IDSOCIETE = "IDSOCIETE";
    public static final String FIELD_LIBELLE = "LIBELLE";
    public static final String FIELD_PROPRIETAIRE = "PROPRIETAIRE";

    public static final String TABLE_LXJOURP = "LXJOURP";
    private String csEtat = "";

    private String csTypeJournal = "";
    private String dateCreation = "";
    private String dateValeurCG = "";
    private String idJournal = "";
    private String idJournalCG = "";
    private String idOrdreGroupeSrc = "";
    private String idSociete = "";
    private String libelle = "";
    private String nomSociete = "";
    private String proprietaire = "";
    private LXSocieteDebitrice societe;

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdJournal(_incCounter(transaction, getIdJournal()));

        if (JadeStringUtil.isBlank(getProprietaire())) {
            setProprietaire(getSession().getUserName());
        }

        if (JadeStringUtil.isBlank(getDateCreation())) {
            setDateCreation(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
        }

        if (JadeStringUtil.isBlank(getCsEtat())) {
            setCsEtat(CS_ETAT_OUVERT);
        }

        if (JadeStringUtil.isIntegerEmpty(getCsTypeJournal())) {
            setCsTypeJournal(CS_TYPE_STANDARD);
        }

        if (!JadeStringUtil.isIntegerEmpty(getIdSociete())
                && !new CAUtilsJournal().isPeriodeComptableOuverte(getSession(), transaction, getDateValeurCG(),
                        getSociete().getIdMandat())) {
            return;
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws java.lang.Exception {

        // Suppression impossible d'un journal
        _addError(transaction, getSession().getLabel("JOURNAL_SUPPR_IMPOSSIBLE"));
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getIdSociete())
                && !new CAUtilsJournal().isPeriodeComptableOuverte(getSession(), transaction, getDateValeurCG(),
                        getSociete().getIdMandat())) {
            return;
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_LXJOURP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdJournal(statement.dbReadNumeric(FIELD_IDJOURNAL));
        setIdSociete(statement.dbReadNumeric(FIELD_IDSOCIETE));
        setDateValeurCG(statement.dbReadDateAMJ(FIELD_DATEVALEURCG));
        setDateCreation(statement.dbReadDateAMJ(FIELD_DATECREATION));
        setLibelle(statement.dbReadString(FIELD_LIBELLE));
        setProprietaire(statement.dbReadString(FIELD_PROPRIETAIRE));
        setCsEtat(statement.dbReadNumeric(FIELD_CSETAT));
        setIdJournalCG(statement.dbReadNumeric(FIELD_IDJOURNALCG));
        setIdOrdreGroupeSrc(statement.dbReadNumeric(FIELD_IDORDREGROUPESRC));
        setCsTypeJournal(statement.dbReadNumeric(FIELD_CSTYPEJOURNAL));

        try {
            // Recuperation du nom et complement sur la table des tiers
            setNomSociete(LXUtils.getNomComplet(statement.dbReadString("HTLDE1"), statement.dbReadString("HTLDE2")));
        } catch (Exception e) {
            // nothing
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Controle de l'id societe
        if (JadeStringUtil.isIntegerEmpty(getIdSociete())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
        }

        // Vérifier le code système état
        if (LXCodeSystem.getCsEtatJournal(getSession()).getCodeSysteme(getCsEtat()) == null) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_ETAT"));
        }

        // Vérifie présence du libellé
        if (JadeStringUtil.isBlank(getLibelle())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_LIBELLE_OBLIGATOIRE"));
        }

        // Vérification présence de la date
        if (!JadeDateUtil.isGlobazDate(getDateValeurCG())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_DATE_COMPTABLE_OBLIGATOIRE"));
        }

    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDJOURNAL, _dbWriteNumeric(statement.getTransaction(), getIdJournal(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELD_IDJOURNAL, _dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField(FIELD_IDSOCIETE, _dbWriteNumeric(statement.getTransaction(), getIdSociete(), "idSociete"));
        statement.writeField(FIELD_DATEVALEURCG,
                _dbWriteDateAMJ(statement.getTransaction(), getDateValeurCG(), "dateValeurCG"));
        statement.writeField(FIELD_DATECREATION,
                _dbWriteDateAMJ(statement.getTransaction(), getDateCreation(), "dateCreation"));
        statement.writeField(FIELD_LIBELLE, _dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField(FIELD_PROPRIETAIRE,
                _dbWriteString(statement.getTransaction(), getProprietaire(), "proprietaire"));
        statement.writeField(FIELD_CSETAT, _dbWriteNumeric(statement.getTransaction(), getCsEtat(), "csEtat"));
        statement.writeField(FIELD_IDJOURNALCG,
                _dbWriteNumeric(statement.getTransaction(), getIdJournalCG(), "idJournalCG"));
        statement.writeField(FIELD_IDORDREGROUPESRC,
                _dbWriteNumeric(statement.getTransaction(), getIdOrdreGroupeSrc(), "idOrdreGroupeSrc"));
        statement.writeField(FIELD_CSTYPEJOURNAL,
                _dbWriteNumeric(statement.getTransaction(), getCsTypeJournal(), "csTypeJournal"));
    }

    // *******************************************************
    // Clonage de l'object
    // *******************************************************
    @Override
    public Object clone() {
        LXJournal journalClone = null;
        try {
            journalClone = (LXJournal) super.clone();
        } catch (CloneNotSupportedException cnse) {

            cnse.printStackTrace(System.err);
        }
        return journalClone;
    }

    public String getCsEtat() {
        return csEtat;
    }

    public String getCsTypeJournal() {
        return csTypeJournal;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getDateValeurCG() {
        return dateValeurCG;
    }

    public String getIdJournal() {
        return idJournal;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getIdJournalCG() {
        return idJournalCG;
    }

    public String getIdOrdreGroupeSrc() {
        return idOrdreGroupeSrc;
    }

    public String getIdSociete() {
        return idSociete;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getNomSociete() {
        return nomSociete;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    /**
     * Return la société débitrice.
     * 
     * @return
     */
    public LXSocieteDebitrice getSociete() {
        retrieveSociete();

        return societe;
    }

    /**
     * Le journal est-il en état ANNULE ?
     * 
     * @return
     */
    public boolean isAnnule() {
        return ((getCsEtat() != null) && (getCsEtat().equals(CS_ETAT_ANNULE)));
    }

    /**
     * Le journal est-il en état COMPTABILISE ?
     * 
     * @return
     */
    public boolean isComptabilise() {
        return ((getCsEtat() != null) && (getCsEtat().equals(CS_ETAT_COMPTABILISE)));
    }

    /**
     * Le journal est-il en état OUVERT ?
     * 
     * @return
     */
    public boolean isOuvert() {
        return ((getCsEtat() != null) && (getCsEtat().equals(CS_ETAT_OUVERT)));
    }

    /**
     * Le journal est-il en état TRAITEMENT ?
     * 
     * @return
     */
    public boolean isTraitement() {
        return ((getCsEtat() != null) && (getCsEtat().equals(CS_ETAT_TRAITEMENT)));
    }

    // *******************************************************
    // Setter
    // *******************************************************

    /**
     * Retrouve la societe si pas encore chargée.
     */
    private void retrieveSociete() {
        if (!JadeStringUtil.isIntegerEmpty(getIdSociete()) && societe == null) {
            try {
                societe = new LXSocieteDebitrice();
                societe.setSession(getSession());
                societe.setIdSociete(getIdSociete());
                societe.retrieve();

                if (societe.hasErrors() || societe.isNew()) {
                    societe = null;
                    return;
                }
            } catch (Exception e) {
                // nothing
            }
        }
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsTypeJournal(String csTypeJournal) {
        this.csTypeJournal = csTypeJournal;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateValeurCG(String dateValeurCG) {
        this.dateValeurCG = dateValeurCG;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdJournalCG(String idJournalCG) {
        this.idJournalCG = idJournalCG;
    }

    public void setIdOrdreGroupeSrc(String idOrdreGroupeSrc) {
        this.idOrdreGroupeSrc = idOrdreGroupeSrc;
    }

    public void setIdSociete(String idSociete) {
        this.idSociete = idSociete;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

}
