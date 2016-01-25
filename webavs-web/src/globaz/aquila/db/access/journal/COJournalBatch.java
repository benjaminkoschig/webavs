package globaz.aquila.db.access.journal;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Journal du contentieux Aquila.
 * 
 * @author vre
 */
public class COJournalBatch extends BEntity {

    public final static String ANNULE = "5190005";

    public final static String ERREUR = "5190004";

    public static final String FNAME_CS_ETAT = "OOTETA";

    public static final String FNAME_DATE_CREATION = "OODCRE";
    public static final String FNAME_ID_JOURNAL = "OOIJRN";
    public static final String FNAME_LIBELLE = "OOLIBL";
    public static final String FNAME_UTILISATEUR = "OOLUSR";
    private static final String LABEL_JOURNAL_LIBELLE = "JOURNAL_LIBELLE";

    public final static String OUVERT = "5190001";
    public final static String PARTIEL = "5190003";
    private static final long serialVersionUID = 4499716031081295253L;
    private static final String TABLE_NAME = "COJOURP";
    public final static String TRAITE = "5190002";
    public final static String TRAITEMENT = "5190006";

    private String dateCreation;
    private String etat;
    private String idJournal = new String();
    private String libelle;
    private String user;

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idJournal = this._incCounter(transaction, idJournal);

        if (JadeStringUtil.isBlank(getUser())) {
            setUser(getSession().getUserName());
        }

        if (JAUtil.isDateEmpty(getDateCreation())) {
            setDateCreation(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
        }

        if (JadeStringUtil.isIntegerEmpty(getEtat())) {
            setEtat(COJournalBatch.OUVERT);
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return COJournalBatch.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idJournal = statement.dbReadNumeric(COJournalBatch.FNAME_ID_JOURNAL);
        etat = statement.dbReadNumeric(COJournalBatch.FNAME_CS_ETAT);
        libelle = statement.dbReadString(COJournalBatch.FNAME_LIBELLE);
        dateCreation = statement.dbReadDateAMJ(COJournalBatch.FNAME_DATE_CREATION);
        user = statement.dbReadString(COJournalBatch.FNAME_UTILISATEUR);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isBlank(getLibelle())) {
            _addError(statement.getTransaction(), getSession().getLabel(COJournalBatch.LABEL_JOURNAL_LIBELLE));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement
                .writeKey(COJournalBatch.FNAME_ID_JOURNAL, this._dbWriteNumeric(statement.getTransaction(), idJournal));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(COJournalBatch.FNAME_ID_JOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), idJournal, "idJournal"));
        statement.writeField(COJournalBatch.FNAME_CS_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), etat, "csEtat"));
        statement.writeField(COJournalBatch.FNAME_LIBELLE,
                this._dbWriteString(statement.getTransaction(), libelle, "libelle"));
        statement.writeField(COJournalBatch.FNAME_DATE_CREATION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateCreation, "dateCreation"));
        statement.writeField(COJournalBatch.FNAME_UTILISATEUR,
                this._dbWriteString(statement.getTransaction(), user, "utilisateur"));
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getEtat() {
        return etat;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getUser() {
        return user;
    }

    /**
     * @see ANNULE
     * @return True état ANNULE
     */
    public boolean isAnnule() {
        return COJournalBatch.ANNULE.equals(getEtat());
    }

    /**
     * @see ERREUR
     * @return True état ERREUR
     */
    public boolean isErreur() {
        return COJournalBatch.ERREUR.equals(getEtat());
    }

    /**
     * @see OUVERT
     * @return True état OUVERT
     */
    public boolean isOuvert() {
        return COJournalBatch.OUVERT.equals(getEtat());
    }

    /**
     * @see PARTIEL
     * @return True état PARTIEL
     */
    public boolean isPartiel() {
        return COJournalBatch.PARTIEL.equals(getEtat());
    }

    /**
     * @see TRAITE
     * @return True état TRAITE
     */
    public boolean isTraite() {
        return COJournalBatch.TRAITE.equals(getEtat());
    }

    /**
     * @see TRAITEMENT
     * @return True état TRAITEMENT
     */
    public boolean isTraitement() {
        return COJournalBatch.TRAITEMENT.equals(getEtat());
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
