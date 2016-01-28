package globaz.aquila.db.access.batch;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.api.ICOEtapeConstante;
import globaz.aquila.common.COBEntity;
import globaz.aquila.process.COInsertQueryBuilder;
import globaz.aquila.process.ICOExportableSQL;
import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.HashMap;
import java.util.List;

/**
 * <h1>Description</h1>
 * <p>
 * Une étape représente un état dans la séquence d'événements d'une mise au contentieux.
 * </p>
 * <p>
 * A chaque moment, un contentieux est dans un certain état (une étape) et peut passer dans un autre état (une autre
 * étape) en fonction de {@link COTransition}. Ces étapes et transitions forment un graphe, une sorte de machine à états
 * finis, qui représente le chemin que peut suivre un contentieux. Il y a un graphe pour chaque type de contentieux,
 * c'est-a-dire un graphe pour chaque {@link COSequence}.
 * </p>
 * 
 * @author Arnaud Dostes, 08-oct-2004
 * @see COTransition
 */
public class COEtape extends COBEntity implements ICOExportableSQL, ICOEtapeConstante {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final int ALT_KEY_LIB_ETAPE = 1;

    private static final long serialVersionUID = 7723980374885145504L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private transient List<COEtapeInfoConfig> etapeInfosConfigs;
    private String idEtape = "";
    private String idSequence = "";
    private Boolean ignorerDateExecution = new Boolean(false);
    private String libAction = "";
    private String libEtape = "";
    private String montantMinimal = "";

    private String typeEtape = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        // effacer toutes les transitions qui mènent à cette étape
        COTransitionManager transitionManager = new COTransitionManager();

        transitionManager.setSession(getSession());
        transitionManager.setForIdEtapeSuivante(getIdEtape());
        transitionManager.find();

        for (int id = 0; id < transitionManager.size(); ++id) {
            ((COTransition) transitionManager.get(id)).delete(transaction);
        }

        // effacer toutes les transitions qui partent de cette étape
        transitionManager.setForIdEtapeSuivante("");
        transitionManager.setForIdEtape(getIdEtape());
        transitionManager.find();

        for (int id = 0; id < transitionManager.size(); ++id) {
            ((COTransition) transitionManager.get(id)).delete(transaction);
        }

        // effacer toutes les configurations d'informations par étapes
        COEtapeInfoConfigManager infoConfigManager = new COEtapeInfoConfigManager();

        infoConfigManager.setSession(getSession());
        infoConfigManager.setForIdEtape(getIdEtape());
        infoConfigManager.find();

        for (int id = 0; id < infoConfigManager.size(); ++id) {
            ((COEtapeInfoConfig) infoConfigManager.get(id)).delete(transaction);
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdEtape(this._incCounter(transaction, "0"));
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return ICOEtapeConstante.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idEtape = statement.dbReadNumeric(ICOEtapeConstante.FNAME_ID_ETAPE);
        idSequence = statement.dbReadNumeric(ICOEtapeConstante.FNAME_ID_SEQUENCE);
        libAction = statement.dbReadNumeric(ICOEtapeConstante.FNAME_LIBACTION);
        libEtape = statement.dbReadNumeric(ICOEtapeConstante.FNAME_LIBETAPE);
        montantMinimal = statement.dbReadNumeric(ICOEtapeConstante.FNAME_MONTANT_MINIMAL);
        typeEtape = statement.dbReadNumeric(ICOEtapeConstante.FNAME_TYPE_ETAPE);
        ignorerDateExecution = statement.dbReadBoolean(ICOEtapeConstante.FNAME_IGNORER_DATE_EXECUTION);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == COEtape.ALT_KEY_LIB_ETAPE) {
            statement.writeKey(ICOEtapeConstante.FNAME_LIBETAPE,
                    this._dbWriteNumeric(statement.getTransaction(), getLibEtape(), "Lib Etape"));
            statement.writeKey(ICOEtapeConstante.FNAME_ID_SEQUENCE,
                    this._dbWriteNumeric(statement.getTransaction(), getIdSequence(), "Id Sequence"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(ICOEtapeConstante.FNAME_ID_ETAPE,
                this._dbWriteNumeric(statement.getTransaction(), getIdEtape(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(ICOEtapeConstante.FNAME_ID_ETAPE,
                this._dbWriteNumeric(statement.getTransaction(), idEtape, "idEtape"));
        statement.writeField(ICOEtapeConstante.FNAME_ID_SEQUENCE,
                this._dbWriteNumeric(statement.getTransaction(), idSequence, "idSequence"));
        statement.writeField(ICOEtapeConstante.FNAME_LIBACTION,
                this._dbWriteNumeric(statement.getTransaction(), libAction, "libAction"));
        statement.writeField(ICOEtapeConstante.FNAME_LIBETAPE,
                this._dbWriteNumeric(statement.getTransaction(), libEtape, "libEtape"));
        statement.writeField(ICOEtapeConstante.FNAME_MONTANT_MINIMAL,
                this._dbWriteNumeric(statement.getTransaction(), montantMinimal, "montantMinimal"));
        statement.writeField(ICOEtapeConstante.FNAME_TYPE_ETAPE,
                this._dbWriteNumeric(statement.getTransaction(), typeEtape, "typeEtape"));
        statement.writeField(ICOEtapeConstante.FNAME_IGNORER_DATE_EXECUTION, this._dbWriteBoolean(
                statement.getTransaction(), getIgnorerDateExecution(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                "ignorerDateExecution"));
    }

    /**
     * @see ICOExportableSQL#export(COInsertQueryBuilder, BTransaction)
     */
    @Override
    public void export(COInsertQueryBuilder query, BTransaction transaction) {
        query.addColumn(ICOEtapeConstante.FNAME_ID_ETAPE, this._dbWriteNumeric(transaction, idEtape, "idEtape"));
        query.addColumn(ICOEtapeConstante.FNAME_ID_SEQUENCE,
                this._dbWriteNumeric(transaction, idSequence, "idSequence"));
        query.addColumn(ICOEtapeConstante.FNAME_LIBACTION, this._dbWriteNumeric(transaction, libAction, "libAction"));
        query.addColumn(ICOEtapeConstante.FNAME_LIBETAPE, this._dbWriteNumeric(transaction, libEtape, "libEtape"));
        query.addColumn(ICOEtapeConstante.FNAME_MONTANT_MINIMAL,
                this._dbWriteNumeric(transaction, montantMinimal, "montantMinimal"));
        query.addColumn(ICOEtapeConstante.FNAME_TYPE_ETAPE, this._dbWriteNumeric(transaction, typeEtape, "typeEtape"));
        query.addColumn(ICOEtapeConstante.FNAME_IGNORER_DATE_EXECUTION,
                this._dbWriteBoolean(transaction, ignorerDateExecution, "ignorerDateExecution"));
    }

    /**
     * @return l'identifiant de cette étape
     */
    public String getIdEtape() {
        return idEtape;
    }

    /**
     * @return l'identifiant de la séquence pour laquelle cette étape existe
     */
    public String getIdSequence() {
        return idSequence;
    }

    /**
     * @return the ignorerDateExecution
     */
    public Boolean getIgnorerDateExecution() {
        return ignorerDateExecution;
    }

    /**
     * @return un cs qui indique le libellé à afficher lorsque l'utilisateur veut atteindre cette étape pour un
     *         contentieux.
     */
    public String getLibAction() {
        return libAction;
    }

    /**
     * @return le libellé associé au cs de l'{@link #getLibAction() action}.
     */
    public String getLibActionLibelle() {
        return getSession().getCodeLibelle(libAction);
    }

    /**
     * @return un cs qui indique le libellé à afficher lorsque l'utilisateur a atteint cette étape pour un contentieux.
     */
    public String getLibEtape() {
        return libEtape;
    }

    /**
     * @return le libellé associé au cs de l'{@link #getLibEtape() étape}
     */
    public String getLibEtapeLibelle() {
        return getSession().getCodeLibelle(libEtape);
    }

    /**
     * @return le montant minimal du solde pour avoir le droit d'atteindre cette étape.
     */
    public String getMontantMinimal() {
        return montantMinimal;
    }

    /**
     * @return le nom de la table
     */
    @Override
    public String getTableName() {
        return _getTableName();
    }

    /**
     * @return the typeEtape
     */
    public String getTypeEtape() {
        return typeEtape;
    }

    /**
     * @return
     */
    public boolean isIgnorerDateExecution() {
        return getIgnorerDateExecution().booleanValue();
    }

    /**
     * @see ICOEtape#load(HashMap)
     */
    public BManager load(HashMap<String, String> criteres) throws Exception {
        COEtapeManager etapes = new COEtapeManager();

        if (criteres.containsKey(ICOEtape.FOR_ID_ETAPE)) {
            etapes.setForIdEtape(criteres.get(ICOEtape.FOR_ID_ETAPE));
        }

        if (criteres.containsKey(ICOEtape.FOR_ID_SEQUENCE)) {
            etapes.setForIdSequence(criteres.get(ICOEtape.FOR_ID_SEQUENCE));
        }

        etapes.setSession(getSession());
        etapes.find();

        return etapes;
    }

    /**
     * charge la liste des configurations d'infos pour cette étape.
     * 
     * @return la liste des configurations d'infos (COEtapeInfoConfig) ou null si aucune
     * @throws Exception
     */
    public List<COEtapeInfoConfig> loadEtapeInfoConfigs() throws Exception {
        if ((etapeInfosConfigs == null) && !JadeStringUtil.isIntegerEmpty(idEtape)) {
            COEtapeInfoConfigManager configManager = new COEtapeInfoConfigManager();

            configManager.setForIdEtape(idEtape);
            configManager.setSession(getSession());
            configManager.find();
            etapeInfosConfigs = configManager.getContainer();
        }

        return etapeInfosConfigs;
    }

    /**
     * @see #getIdEtape()
     */
    public void setIdEtape(String string) {
        idEtape = string;
    }

    /**
     * @see #getIdSequence()
     */
    public void setIdSequence(String string) {
        idSequence = string;
    }

    /**
     * @param ignorerDateExecution
     *            the ignorerDateExecution to set
     */
    public void setIgnorerDateExecution(Boolean ignorerDateExecution) {
        this.ignorerDateExecution = ignorerDateExecution;
    }

    /**
     * @see #getLibAction()
     */
    public void setLibAction(String string) {
        libAction = string;
    }

    /**
     * @see #getLibEtape()
     */
    public void setLibEtape(String string) {
        libEtape = string;
    }

    /**
     * @see #getMontantMinimal()
     */
    public void setMontantMinimal(String montantMinimal) {
        this.montantMinimal = montantMinimal;
    }

    /**
     * @param typeEtape
     *            the typeEtape to set
     */
    public void setTypeEtape(String typeEtape) {
        this.typeEtape = typeEtape;
    }
}
