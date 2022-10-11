package globaz.aquila.db.access.batch;

import globaz.aquila.common.COBEntity;
import globaz.aquila.db.access.batch.transition.COTransitionAction;
import globaz.aquila.db.access.batch.transition.COTransitionException;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.process.COInsertQueryBuilder;
import globaz.aquila.process.COProcessContentieux;
import globaz.aquila.process.ICOExportableSQL;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.servlet.COActionBatch;
import globaz.aquila.util.COActionUtils;
import globaz.aquila.util.CODateUtils;
import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.external.IntRole;

/**
 * <h1>Description</h1>
 * <p>
 * Repr�sente une entit� de type Transition
 * </p>
 * <p>
 * Une transition relie deux {@link COEtape �tapes} entre elles. Les transitions ont deux modes d'ex�cution, l'un
 * {@link #getAuto() automatique} et l'autre {@link #getManuel() manuel}, ces modes indiquent si la transition peut
 * respectivement �tre lanc�e par le {@link COProcessContentieux batch} ou � la main depuis les {@link COActionBatch
 * �crans}.
 * </p>
 * <p>
 * Trois param�tres d�finissent le moment � partir duquel la transition peut �tre lanc�e automatiquement, d'une part le
 * {@link #getAuto() mode}, d'autre part le {@link #getDuree() d�lai} et enfin le {@link #getGenreDelai() mode de
 * calcul} de l'�ch�ance du d�lai.
 * </p>
 * <p>
 * Pour chaque transition, une action est ex�cut�e, cette action est instanci�e automatiquement en fonction
 * {@link #getTransitionAction() de sa classe}. Les param�tres de cette action peuvent �tre saisis par l'utilisateur
 * dans un �cran sp�cial, ce dernier est configurable, on l'appelle le {@link #getFormSnippet() snippet}.
 * </p>
 * 
 * @author Arnaud Dostes, 08-oct-2004
 */
public class COTransition extends COBEntity implements ICOExportableSQL {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String CS_RELATIF_DEBUT_CONTENTIEUX = "5400001";

    public static final String CS_RELATIF_DERNIERE_ECHEANCE = "5400003";
    public static final String CS_RELATIF_DERNIERE_EXECUTION = "5400002";
    public static final String CS_RELATIF_ECHEANCE_SECTION = "5400005";
    public static final String CS_RELATIF_SECTION = "5400004";
    public static final String FNAME_AUTO = "OGBAUT";
    public static final String FNAME_DUREE = "OGNDUR";
    public static final String FNAME_DUREE_PARITAIRE = "OGNDUP";
    public static final String FNAME_FORM_SNIPPET = "OGLSNI";
    public static final String FNAME_GENRE_DELAI = "OGTDEL";
    public static final String FNAME_GENRE_DELAI_PARITAIRE = "OGTDEP";
    public static final String FNAME_ID_ETAPE = "ODIETA";
    public static final String FNAME_ID_ETAPE_SUIVANTE = "OGIESU";
    public static final String FNAME_ID_TRANSITION = "OGITRA";
    public static final String FNAME_MANUEL = "OGBMAN";
    public static final String FNAME_PRIORITE = "OGNPRI";
    public static final String FNAME_TRANSITION_ACTION = "OGLACT";

    private static final long serialVersionUID = 4735437486250842643L;
    public static final String TABLE_NAME = "COTRANP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean auto = new Boolean(false);
    private String duree = "";
    private String dureeParitaire = "";
    private COEtape etape = new COEtape();
    private COEtape etapeSuivante = new COEtape();
    private String formSnippet = "";
    private String genreDelai = "";
    private String genreDelaiParitaire = "";
    private String idEtape = "";
    private String idEtapeSuivante = "";
    private String idTransition = "";
    private Boolean manuel = new Boolean(false);
    private String priorite = "";
    private String transitionAction = "";
    private String eBillTransactionID = "";
    private Boolean eBillPrintable = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        refreshLinks(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        refreshLinks(transaction);
        setIdTransition(this._incCounter(transaction, getIdTransition(), _getTableName()));
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        refreshLinks(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return COTransition.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idTransition = statement.dbReadNumeric(COTransition.FNAME_ID_TRANSITION);
        idEtape = statement.dbReadNumeric(COTransition.FNAME_ID_ETAPE);
        idEtapeSuivante = statement.dbReadNumeric(COTransition.FNAME_ID_ETAPE_SUIVANTE);
        formSnippet = statement.dbReadString(COTransition.FNAME_FORM_SNIPPET);
        duree = statement.dbReadNumeric(COTransition.FNAME_DUREE);
        transitionAction = statement.dbReadString(COTransition.FNAME_TRANSITION_ACTION);
        manuel = statement.dbReadBoolean(COTransition.FNAME_MANUEL);
        auto = statement.dbReadBoolean(COTransition.FNAME_AUTO);
        priorite = statement.dbReadNumeric(COTransition.FNAME_PRIORITE);
        genreDelai = statement.dbReadNumeric(COTransition.FNAME_GENRE_DELAI);
        dureeParitaire = statement.dbReadNumeric(COTransition.FNAME_DUREE_PARITAIRE);
        genreDelaiParitaire = statement.dbReadNumeric(COTransition.FNAME_GENRE_DELAI_PARITAIRE);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) {
        // la classe d'action doit exister
        Class clazz = null;

        try {
            clazz = Class.forName(COServiceLocator.getActionService().getActionClassName(this));
        } catch (Exception e) {
            _addError(statement.getTransaction(), getSession().getLabel("AQUILA_ACTION_TRANSITION_INCORRECTE"));
        }

        if ((clazz != null) && !(COTransitionAction.class.isAssignableFrom(clazz))) {
            _addError(statement.getTransaction(), getSession().getLabel("AQUILA_ACTION_TRANSITION_INCORRECTE"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(COTransition.FNAME_ID_TRANSITION,
                this._dbWriteNumeric(statement.getTransaction(), getIdTransition(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(COTransition.FNAME_ID_TRANSITION,
                this._dbWriteNumeric(statement.getTransaction(), getIdTransition(), "idTransition"));
        statement.writeField(COTransition.FNAME_ID_ETAPE,
                this._dbWriteNumeric(statement.getTransaction(), getIdEtape(), "idEtape"));
        statement.writeField(COTransition.FNAME_ID_ETAPE_SUIVANTE,
                this._dbWriteNumeric(statement.getTransaction(), getIdEtapeSuivante(), "idEtapeSuivante"));
        statement.writeField(COTransition.FNAME_FORM_SNIPPET,
                this._dbWriteString(statement.getTransaction(), getFormSnippet(), "formSnippet"));
        statement.writeField(COTransition.FNAME_DUREE,
                this._dbWriteNumeric(statement.getTransaction(), getDuree(), COTransition.FNAME_DUREE));
        statement.writeField(COTransition.FNAME_TRANSITION_ACTION,
                this._dbWriteString(statement.getTransaction(), getTransitionAction(), "transitionAction"));
        statement.writeField(COTransition.FNAME_MANUEL, this._dbWriteBoolean(statement.getTransaction(), getManuel(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, COTransition.FNAME_MANUEL));
        statement.writeField(COTransition.FNAME_AUTO, this._dbWriteBoolean(statement.getTransaction(), getAuto(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, COTransition.FNAME_AUTO));
        statement.writeField(COTransition.FNAME_PRIORITE,
                this._dbWriteNumeric(statement.getTransaction(), getPriorite(), COTransition.FNAME_PRIORITE));
        statement.writeField(COTransition.FNAME_GENRE_DELAI,
                this._dbWriteNumeric(statement.getTransaction(), genreDelai));
        statement.writeField(COTransition.FNAME_DUREE_PARITAIRE, this._dbWriteNumeric(statement.getTransaction(),
                getDureeParitaire(), COTransition.FNAME_DUREE_PARITAIRE));
        statement.writeField(COTransition.FNAME_GENRE_DELAI_PARITAIRE,
                this._dbWriteNumeric(statement.getTransaction(), genreDelaiParitaire));
    }

    /**
     * calcule la date du prochain declenchement en fonction de la duree et du genre de delai de cette transition.
     * <p>
     * Le contentieux doit contenir une date d'ex�cution de la derni�re transition et un genre de delai valables.
     * </p>
     * 
     * @param contentieux
     *            le contentieux dont on veut calculer la date du prochain declenchement
     * @return la date du prochain declenchement
     * @throws COTransitionException
     *             si genreDelai ou contentieux.dateExecution sont invalides pour le calcul
     */
    public String calculerDateProchainDeclenchement(COContentieux contentieux) throws COTransitionException {
        String dateRef;
        String genreDelaiActif;
        String dureeActive;

        if (contentieux.getCompteAnnexe().getIdRole().equals(IntRole.ROLE_AFFILIE_PARITAIRE)) {
            if (!JadeStringUtil.isIntegerEmpty(getGenreDelaiParitaire())) {
                genreDelaiActif = getGenreDelaiParitaire();
            } else {
                genreDelaiActif = getGenreDelai();
            }
            if (!JadeStringUtil.isIntegerEmpty(getDureeParitaire())) {
                dureeActive = getDureeParitaire();
            } else {
                dureeActive = getDuree();
            }
        } else {
            genreDelaiActif = getGenreDelai();
            dureeActive = getDuree();
        }

        if (COTransition.CS_RELATIF_DEBUT_CONTENTIEUX.equals(genreDelaiActif)) {
            dateRef = contentieux.getDateOuverture();
        } else if (COTransition.CS_RELATIF_DERNIERE_EXECUTION.equals(genreDelaiActif)) {
            dateRef = contentieux.getDateExecution();
        } else if (COTransition.CS_RELATIF_DERNIERE_ECHEANCE.equals(genreDelaiActif)) {
            dateRef = contentieux.getDateDeclenchement();
        } else if (COTransition.CS_RELATIF_SECTION.equals(genreDelaiActif)) {
            dateRef = contentieux.getSection().getDateSection();
        } else if (COTransition.CS_RELATIF_ECHEANCE_SECTION.equals(genreDelaiActif)) {
            dateRef = contentieux.getSection().getDateEcheance();
        } else {
            throw new IllegalStateException("Code syst�me du genre de d�lai invalide:" + genreDelaiActif);
        }

        // pour le cas ou l'on tente de calculer la date pour un contentieux qui
        // n'est pas encore cr��
        if (JadeStringUtil.isEmpty(dateRef)) {
            if (JadeStringUtil.isEmpty(contentieux.getSection().getDateEcheance())) {
                /*
                 * si la date d'�ch�ance n'est pas renseign�e, on utilise la dur�e de la transition pour proposer une
                 * date d'�ch�ance � partir de la date de facture. Cette date est quasiment obligatoirement fausse mais
                 * sinon que faire ?
                 */
                contentieux.getSection().getDateSection();
            } else {
                // sinon on retourne la date d'�ch�anceF
                return contentieux.getSection().getDateEcheance();
            }
        }

        try {
            return CODateUtils.getDatePlusDaysDDsMMsYYYY(new JADate(dateRef), Integer.parseInt(dureeActive));
        } catch (Exception e) {
            throw new COTransitionException("AQUILA_DATE_PROCHAINE_TRANSITION_ERROR", COActionUtils.getMessage(
                    getSession(), "AQUILA_DATE_PROCHAINE_TRANSITION_ERROR"));
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param query
     * @param transaction
     */
    @Override
    public void export(COInsertQueryBuilder query, BTransaction transaction) {
        query.addColumn(COTransition.FNAME_ID_TRANSITION,
                this._dbWriteNumeric(transaction, getIdTransition(), "idTransition"));
        query.addColumn(COTransition.FNAME_ID_ETAPE, this._dbWriteNumeric(transaction, getIdEtape(), "idEtape"));
        query.addColumn(COTransition.FNAME_ID_ETAPE_SUIVANTE,
                this._dbWriteNumeric(transaction, getIdEtapeSuivante(), "idEtapeSuivante"));
        query.addColumn(COTransition.FNAME_FORM_SNIPPET,
                this._dbWriteString(transaction, getFormSnippet(), "formSnippet"));
        query.addColumn(COTransition.FNAME_DUREE,
                this._dbWriteNumeric(transaction, getDuree(), COTransition.FNAME_DUREE));
        query.addColumn(COTransition.FNAME_TRANSITION_ACTION,
                this._dbWriteString(transaction, getTransitionAction(), "transitionAction"));
        query.addColumn(COTransition.FNAME_MANUEL, this._dbWriteBoolean(transaction, getManuel(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, COTransition.FNAME_MANUEL));
        query.addColumn(COTransition.FNAME_AUTO,
                this._dbWriteBoolean(transaction, getAuto(), BConstants.DB_TYPE_BOOLEAN_CHAR, COTransition.FNAME_AUTO));
        query.addColumn(COTransition.FNAME_PRIORITE,
                this._dbWriteNumeric(transaction, getPriorite(), COTransition.FNAME_PRIORITE));
        query.addColumn(COTransition.FNAME_GENRE_DELAI, this._dbWriteNumeric(transaction, genreDelai));
        query.addColumn(COTransition.FNAME_DUREE_PARITAIRE,
                this._dbWriteNumeric(transaction, getDureeParitaire(), COTransition.FNAME_DUREE_PARITAIRE));
        query.addColumn(COTransition.FNAME_GENRE_DELAI_PARITAIRE,
                this._dbWriteNumeric(transaction, genreDelaiParitaire));
    }

    /**
     * @return vrai si la transition peut �tre effectu�e automatiquement
     */
    public Boolean getAuto() {
        return auto;
    }

    /**
     * @return la dur�e du d�lai avant que l'action puisse �tre ex�cut�e automatiquement
     */
    public String getDuree() {
        return duree;
    }

    /**
     * @return la dur�e du d�lai paritaire avant que l'action puisse �tre ex�cut�e automatiquement
     */
    public String getDureeParitaire() {
        return dureeParitaire;
    }

    /**
     * @return l'�tape de d�part
     */
    public COEtape getEtape() {
        return etape;
    }

    /**
     * @return l'�tape d'arriv�e
     */
    public COEtape getEtapeSuivante() {
        return etapeSuivante;
    }

    /**
     * @return le fragment de jsp � afficher pour que l'utilisateur saisisse les param�tres de l'
     *         {@link #getTransitionAction() action}.
     */
    public String getFormSnippet() {
        return formSnippet;
    }

    /**
     * @return un cs qui indique � partir de quel moment calculer le d�lai d'ex�cution automatique.
     * @see #calculerDateProchainDeclenchement(COContentieux)
     */
    public String getGenreDelai() {
        return genreDelai;
    }

    /**
     * @return le label li� au cs {@link #getGenreDelai()}
     */
    public String getGenreDelaiLibelle() {
        return getSession().getCodeLibelle(genreDelai);
    }

    /**
     * @return un cs qui indique � partir de quel moment calculer le d�lai d'ex�cution automatique pour un paritaire.
     * @see #calculerDateProchainDeclenchement(COContentieux)
     */
    public String getGenreDelaiParitaire() {
        return genreDelaiParitaire;
    }

    /**
     * @return
     */
    public String getGenreDelaiWithStepTransitionDepuis() {
        String text = "";
        if (COTransition.CS_RELATIF_DERNIERE_EXECUTION.equals(getGenreDelai())) {
            text = getSession().getLabel("MODIF_ETAPE_GENRE_LAST_DATE_EXEC") + " "
                    + getEtapeSuivante().getLibActionLibelle();
        } else {
            text = getGenreDelaiLibelle();
        }
        return text;
    }

    /**
     * @param etape
     * @return
     */
    public String getGenreDelaiWithStepTransitionVers(String etape) {
        String text = "";
        if (COTransition.CS_RELATIF_DERNIERE_EXECUTION.equals(getGenreDelai())) {
            text = getSession().getLabel("MODIF_ETAPE_GENRE_LAST_DATE_EXEC") + " " + etape;
        } else {
            text = getGenreDelaiLibelle();
        }
        return text;
    }

    /**
     * @return l'identifiant de l'�tape de d�part
     */
    public String getIdEtape() {
        return idEtape;
    }

    /**
     * @return l'identifiant de l'�tape d'arriv�e
     */
    public String getIdEtapeSuivante() {
        return idEtapeSuivante;
    }

    /**
     * @return l'identifiant de la trasnstion
     */
    public String getIdTransition() {
        return idTransition;
    }

    /**
     * @return vrai si la transtion peut �tre ex�cut�e manuellement depuis les �crans
     * @see COActionBatch
     */
    public Boolean getManuel() {
        return manuel;
    }

    /**
     * un ordre de priorit� d'ex�cution des transitions automatiques depuis cette {@link #getEtape() �tape de d�part}.
     * <p>
     * la transition avec la valeur la plus �lev�e est ex�cut�e en premier.
     * </p>
     * 
     * @return un ordre de priorit� d'ex�cution des transitions automatiques depuis cette {@link #getEtape() �tape de
     *         d�part}.
     */
    public String getPriorite() {
        return priorite;
    }

    /**
     * @return lenom de la table
     */
    @Override
    public String getTableName() {
        return _getTableName();
    }

    /**
     * @return le nom de la classe (sans package) de l'action � ex�cuter pour cette transition, une classe enfant de
     *         {@link COTransitionAction}
     * @see COTransitionAction
     */
    public String getTransitionAction() {
        return transitionAction;
    }

    /**
     * @return the eBillTransactionID
     */
    public String geteBillTransactionID() {
        return eBillTransactionID;
    }

    /**
     * @return the eBillPrintable
     */
    public Boolean geteBillPrintable() {
        return eBillPrintable;
    }

    /**
     * @see #getAuto()
     */
    public boolean isAuto() {
        return auto.booleanValue();
    }

    /**
     * @see #getManuel()
     */
    public boolean isManuel() {
        return manuel.booleanValue();
    }

    /**
     * Actualise les objets li�s.
     * 
     * @param transaction
     *            La transaction � utiliser
     * @exception Exception
     *                Si un probl�me est rencontr�
     */
    private void refreshLinks(BTransaction transaction) throws Exception {
        // Actualisation de l'�tape
        etape = new COEtape();
        etape.setSession(getSession());
        etape.setIdEtape(getIdEtape());
        etape.retrieve(transaction);

        // Actualisation de l'�tape suivante
        etapeSuivante = new COEtape();
        etapeSuivante.setSession(getSession());
        etapeSuivante.setIdEtape(getIdEtapeSuivante());
        etapeSuivante.retrieve(transaction);
    }

    /**
     * @see #getAuto()
     */
    public void setAuto(Boolean boolean1) {
        auto = boolean1;
    }

    /**
     * @see #getDuree()
     */
    public void setDuree(String string) {
        duree = string;
    }

    /**
     * @see #getDureeParitaire()
     */
    public void setDureeParitaire(String string) {
        dureeParitaire = string;
    }

    /**
     * @see #getEtape()
     */
    public void setEtape(COEtape value) {
        etape = value;
    }

    /**
     * @see #getEtapeSuivante()
     */
    public void setEtapeSuivante(COEtape value) {
        etapeSuivante = value;
    }

    /**
     * @see #getFormSnippet()
     */
    public void setFormSnippet(String string) {
        formSnippet = string;
    }

    /**
     * @see #getGenreDelai()
     */
    public void setGenreDelai(String string) {
        genreDelai = string;
    }

    /**
     * @see #getGenreDelaiParitaire()
     */
    public void setGenreDelaiParitaire(String string) {
        genreDelaiParitaire = string;
    }

    /**
     * @see #getIdEtape()
     */
    public void setIdEtape(String string) {
        idEtape = string;
    }

    /**
     * @see #getIdEtapeSuivante()
     */
    public void setIdEtapeSuivante(String string) {
        idEtapeSuivante = string;
    }

    /**
     * @see #getIdTransition()
     */
    public void setIdTransition(String string) {
        idTransition = string;
    }

    /**
     * @see #getManuel()
     */
    public void setManuel(Boolean boolean1) {
        manuel = boolean1;
    }

    /**
     * @see #getPriorite()
     */
    public void setPriorite(String string) {
        priorite = string;
    }

    /**
     * @see #getTransitionAction()
     */
    public void setTransitionAction(String string) {
        transitionAction = string;
    }

    /**
     * @param eBillTransactionID the eBillTransactionID to set
     */
    public void setEBillTransactionID(String eBillTransactionID) {
        this.eBillTransactionID = eBillTransactionID;
    }

    /**
     * @param eBillPrintable the eBillPrintable to set
     */
    public void setEBillPrintable(Boolean eBillPrintable) {
        this.eBillPrintable = eBillPrintable;
    }
}
