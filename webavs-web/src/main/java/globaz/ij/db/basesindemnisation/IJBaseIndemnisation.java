/*
 * Créé le 6 sept. 05
 */
package globaz.ij.db.basesindemnisation;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJPrestationManager;
import globaz.ij.db.prononces.IJMesureJointAgentExecution;
import globaz.ij.db.prononces.IJMesureJointAgentExecutionManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.regles.IJPrestationRegles;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.factory.IPRCloneable;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRHierarchique;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * une prestation est creee a chaque fois qu'un instance de cette classe est ajoutee dans la base.
 * </p>
 * 
 * @author dvh
 */
public class IJBaseIndemnisation extends BEntity implements PRHierarchique, IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** DOCUMENT ME! */
    public static final String FIELDNAME_ATTESTATIONJOURS = "XKLATT";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_CANTON_IMPOT_SOURCE = "XKTCIS";

    public static final String FIELDNAME_CS_TYPE_BASE = "XKTTBA";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_DATEDEBUTPERIODE = "XKDDEB";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_DATEFINPERIODE = "XKDFIN";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_ETAT = "XKTETA";

    public static final String FIELDNAME_ID_CORRECTION = "XKICOR";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_IDBASEINDEMNISATION = "XKIBIN";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_IDPARENT = "XKIPAR";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_IDPRONONCE = "XKIPAI";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_MOTIFINTERRUPTION = "XKTMOI";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_NOMBREJOURSCOUVERTS = "XKNJCO";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_NOMBREJOURSEXTERNE = "XKNJEX";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_NOMBREJOURSINTERNE = "XKNJIN";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_NOMBREJOURSINTERRUPTION = "XKNJOI";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_REMARQUE = "XKLREM";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_TAUX_IMPOT_SOURCE = "XKMTIS";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_TYPE = "XKTTYP";
    public static final String FIELDNAME_TYPE_IJ = "XKTTIJ";
    /** DOCUMENT ME! */
    public static final String TABLE_NAME = "IJBASIND";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String attestationJours = "";
    private boolean creerFormulaires = false;
    private String csCantonImpotSource = "";
    private String csEtat = IIJBaseIndemnisation.CS_OUVERT;
    private String csMotifInterruption = "";
    private String csTypeBase = IIJBaseIndemnisation.CS_NORMAL;
    private String csTypeIJ = "";
    private String dateDebutPeriode = "";
    private String dateFinPeriode = "";
    private transient IJBaseIndemnisation enfantActif;
    private transient IJBaseIndemnisation enfantCorrection;
    private String idBaseIndemisation = "";
    private String idCorrection = "";
    private String idParent = "";
    private String idPrononce = "";
    private String nombreJoursCouverts = "";
    private String nombreJoursExterne = "";
    private String nombreJoursInterne = "";

    private String nombreJoursInterruption = "";
    private transient IJPrononce prononce;
    private String remarque = "";

    private String tauxImpotSource = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        // pre-creer des formulaires pour tous les agents d'execution actuels du
        // prononce
        if (isCreerFormulaires()) {
            IJMesureJointAgentExecutionManager agents = new IJMesureJointAgentExecutionManager();

            agents.setForIdPrononce(idPrononce);
            agents.setSession(getSession());
            agents.find();

            for (int idAgent = 0; idAgent < agents.size(); ++idAgent) {
                IJFormulaireIndemnisation formulaire = new IJFormulaireIndemnisation();

                formulaire.setIdIndemnisation(idBaseIndemisation);
                formulaire.setIdInstitutionResponsable(((IJMesureJointAgentExecution) agents.get(idAgent))
                        .getIdAgentExecution());

                formulaire.setSession(getSession());
                formulaire.wantCallValidate(false);
                formulaire.add(transaction);
            }
        }
    }

    /**
     * (non-Javadoc).
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        super._afterDelete(transaction);

        // effacement des prestations
        IJPrestationManager prestations = new IJPrestationManager();

        prestations.setForIdBaseIndemnisation(idBaseIndemisation);
        prestations.setSession(getSession());
        prestations.find();

        for (int idPrestation = 0; idPrestation < prestations.size(); ++idPrestation) {
            IJPrestation prestation = (IJPrestation) prestations.get(idPrestation);

            IJPrestationRegles.annulerMiseEnLot(getSession(), transaction, prestation);
            prestation.delete(transaction);
        }

        // effacement des formulaires
        IJFormulaireIndemnisationManager formulaires = new IJFormulaireIndemnisationManager();

        formulaires.setForIdBaseIndemnisation(idBaseIndemisation);
        formulaires.setSession(getSession());
        formulaires.find();

        for (int idFormulaire = 0; idFormulaire < formulaires.size(); ++idFormulaire) {
            ((IJFormulaireIndemnisation) formulaires.get(idFormulaire)).delete(transaction);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    /**
     * DOCUMENT ME!
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idBaseIndemisation = this._incCounter(transaction, "0", IJBaseIndemnisation.TABLE_NAME);

        // charger le type de l'ij
        if (JadeStringUtil.isIntegerEmpty(csTypeIJ) && !JadeStringUtil.isIntegerEmpty(idPrononce)) {
            IJPrononce prononce = new IJPrononce();

            prononce.setIdPrononce(idPrononce);
            prononce.setSession(getSession());
            prononce.retrieve(transaction);

            csTypeIJ = prononce.getCsTypeIJ();
        }
    }

    /**
     * (non-Javadoc).
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return IJBaseIndemnisation.TABLE_NAME;
    }

    /**
     * (non-Javadoc).
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        dateDebutPeriode = statement.dbReadDateAMJ(IJBaseIndemnisation.FIELDNAME_DATEDEBUTPERIODE);
        dateFinPeriode = statement.dbReadDateAMJ(IJBaseIndemnisation.FIELDNAME_DATEFINPERIODE);
        idBaseIndemisation = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION);
        nombreJoursCouverts = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_NOMBREJOURSCOUVERTS);
        nombreJoursInterne = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_NOMBREJOURSINTERNE);
        nombreJoursExterne = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_NOMBREJOURSEXTERNE);
        attestationJours = statement.dbReadString(IJBaseIndemnisation.FIELDNAME_ATTESTATIONJOURS);
        nombreJoursInterruption = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_NOMBREJOURSINTERRUPTION);
        csMotifInterruption = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_MOTIFINTERRUPTION);
        remarque = statement.dbReadString(IJBaseIndemnisation.FIELDNAME_REMARQUE);
        csEtat = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_ETAT);
        idParent = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_IDPARENT);
        idPrononce = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_IDPRONONCE);
        csCantonImpotSource = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_CANTON_IMPOT_SOURCE);
        tauxImpotSource = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_TAUX_IMPOT_SOURCE, 2);
        csTypeIJ = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_TYPE_IJ);
        idCorrection = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_ID_CORRECTION);
        csTypeBase = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_CS_TYPE_BASE);
    }

    /**
     * (non-Javadoc).
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // la date de début doit être renseignée
        _propertyMandatory(statement.getTransaction(), dateDebutPeriode,
                getSession().getLabel("JSP_BASE_IND_DATE_DEBUT_REQUISE"));

        // la date de fin doit être renseignée
        _propertyMandatory(statement.getTransaction(), dateFinPeriode,
                getSession().getLabel("JSP_BASE_IND_DATE_FIN_REQUISE"));

        // le prononcé est requis
        _propertyMandatory(statement.getTransaction(), idPrononce, getSession()
                .getLabel("JSP_BASE_IND_PRONONCE_REQUIS"));
        _propertyMandatory(statement.getTransaction(), csTypeIJ, getSession().getLabel("JSP_BASE_IND_PRONONCE_REQUIS"));

        // la date de début et la date de fin doivent être dans la même année
        // pour les CI.
        // Interdit d'envoyer un CI pour une période à cheval sur 2 années
        JADate dd = new JADate(dateDebutPeriode);
        JADate df = new JADate(dateFinPeriode);

        int y1 = dd.getYear();
        int y2 = df.getYear();

        if (y1 != y2) {
            _addError(statement.getTransaction(), getSession().getLabel("JSP_BI_PERIODE_INVALID"));
        }

        int nbrJoursCouvert = 0;

        if (!JadeStringUtil.isIntegerEmpty(getNombreJoursExterne())
                || !JadeStringUtil.isIntegerEmpty(getNombreJoursInterne())) {
            nbrJoursCouvert = 0;

            // ajouter les jours interne s'ils sont renseignes
            if (!JadeStringUtil.isEmpty(getNombreJoursInterne())) {
                nbrJoursCouvert = Integer.parseInt(getNombreJoursInterne());
            }

            // ajouter les jours externe s'ils sont renseignes
            if (!JadeStringUtil.isEmpty(getNombreJoursExterne())) {
                nbrJoursCouvert += Integer.parseInt(getNombreJoursExterne());
            }

            setNombreJoursCouverts(String.valueOf(nbrJoursCouvert));

            // Annulation de la string des attestations si le nombres de jours
            // interne ou externe est saisi.
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < getAttestationJours().length(); i++) {
                sb.append(IIJBaseIndemnisation.IJ_CALENDAR_NON_ATTESTE);
            }

            setAttestationJours(sb.toString());

        } else {
            for (int i = 0; i < getAttestationJours().length(); i++) {
                char c = getAttestationJours().charAt(i);

                if (IIJBaseIndemnisation.IJ_CALENDAR_EXTERNE.equals(String.valueOf(c))
                        || IIJBaseIndemnisation.IJ_CALENDAR_INTERNE.equals(String.valueOf(c))
                        || IIJBaseIndemnisation.IJ_CALENDAR_INCAPACITE_PARTIELLE.equals(String.valueOf(c))) {
                    nbrJoursCouvert++;
                }
            }

            setNombreJoursCouverts(String.valueOf(nbrJoursCouvert));
        }

        /*
         * D0118 : possibilité des saisir des BI avec 0 jours interne et externes
         */
        // if (JadeStringUtil.isIntegerEmpty(getNombreJoursExterne())
        // && JadeStringUtil.isIntegerEmpty(getNombreJoursInterne()) && (nbrJoursCouvert == 0)) {
        // _addError(statement.getTransaction(), getSession().getLabel("JSP_BASE_IND_AUCUN_JOURS_ATTESTE"));
        // }

        // la base ne doit pas recouvrir une autre base
        IJBaseIndemnisationManager bases = new IJBaseIndemnisationManager();

        bases.setForPeriode(getDateDebutPeriode(), getDateFinPeriode());
        bases.setForIdPrononce(getIdPrononce());

        String[] tabEtat = new String[] { IIJBaseIndemnisation.CS_OUVERT, IIJBaseIndemnisation.CS_VALIDE,
                IIJBaseIndemnisation.CS_COMMUNIQUE };

        bases.setForCsEtats(tabEtat);

        // ignorer soi-meme et ses propres parents pour ce test
        if (isEnfant()) {
            bases.setNotForIdBaseIndemnisation(idParent);
            bases.setNotForIdParent(idParent);
        } else {
            bases.setNotForIdBaseIndemnisation(idBaseIndemisation);
            bases.setNotForIdParent(idBaseIndemisation);
        }

        bases.setSession(getSession());

        if (bases.getCount() > 0) {
            _addError(statement.getTransaction(), getSession().getLabel("BASES_DOUBLES"));
        }
    }

    /**
     * (non-Javadoc).
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION,
                this._dbWriteNumeric(statement.getTransaction(), idBaseIndemisation, "idBaseIndemnisation"));
    }

    /**
     * (non-Javadoc).
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        BTransaction transaction = statement.getTransaction();

        statement.writeField(IJBaseIndemnisation.FIELDNAME_DATEDEBUTPERIODE,
                this._dbWriteDateAMJ(transaction, dateDebutPeriode, "dateDebutPeriode"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_DATEFINPERIODE,
                this._dbWriteDateAMJ(transaction, dateFinPeriode, "dateFinPeriode"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION,
                this._dbWriteNumeric(transaction, idBaseIndemisation, "idBaseIndeminsation"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_NOMBREJOURSCOUVERTS,
                this._dbWriteNumeric(transaction, nombreJoursCouverts, "nombreJoursCouverts"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_NOMBREJOURSINTERNE,
                this._dbWriteNumeric(transaction, nombreJoursInterne, "nombreJoursInterne"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_NOMBREJOURSEXTERNE,
                this._dbWriteNumeric(transaction, nombreJoursExterne, "nombreJoursExterne"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_ATTESTATIONJOURS,
                this._dbWriteString(transaction, attestationJours, "attestationJours"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_NOMBREJOURSINTERRUPTION,
                this._dbWriteNumeric(transaction, nombreJoursInterruption, "nombreJoursInterruption"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_MOTIFINTERRUPTION,
                this._dbWriteNumeric(transaction, csMotifInterruption, "csMotifInterruption"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_REMARQUE,
                this._dbWriteString(transaction, remarque, "remarque"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_ETAT, this._dbWriteNumeric(transaction, csEtat, "csEtat"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_IDPARENT,
                this._dbWriteNumeric(transaction, idParent, "idParent"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_IDPRONONCE,
                this._dbWriteNumeric(transaction, idPrononce, "idPrononce"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_CANTON_IMPOT_SOURCE,
                this._dbWriteNumeric(transaction, csCantonImpotSource, "csCantonImpotSource"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_TAUX_IMPOT_SOURCE,
                this._dbWriteNumeric(transaction, tauxImpotSource, "tauxImpotSource"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_TYPE_IJ,
                this._dbWriteNumeric(transaction, csTypeIJ, "csTypeIJ"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_ID_CORRECTION,
                this._dbWriteNumeric(transaction, idCorrection, "idCorrection"));
        statement.writeField(IJBaseIndemnisation.FIELDNAME_CS_TYPE_BASE,
                this._dbWriteNumeric(transaction, csTypeBase, "csTypeBase"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param action
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        IJBaseIndemnisation clone = new IJBaseIndemnisation();

        clone.setAttestationJours(getAttestationJours());
        clone.setCsTypeBase(getCsTypeBase());
        clone.setCsCantonImpotSource(getCsCantonImpotSource());
        clone.setCsEtat(getCsEtat());
        clone.setCsMotifInterruption(getCsMotifInterruption());
        clone.setCsTypeIJ(getCsTypeIJ());
        clone.setIdPrononce(getIdPrononce());
        clone.setDateDebutPeriode(getDateDebutPeriode());
        clone.setDateFinPeriode(getDateFinPeriode());
        clone.setIdCorrection(getIdBaseIndemisation());
        clone.setNombreJoursCouverts(getNombreJoursCouverts());
        clone.setNombreJoursExterne(getNombreJoursExterne());
        clone.setNombreJoursInterne(getNombreJoursInterne());
        clone.setNombreJoursInterruption(getNombreJoursInterruption());
        clone.setRemarque(getRemarque());
        clone.setTauxImpotSource(getTauxImpotSource());

        if (action == IIJBaseIndemnisation.CLONE_FILS) {
            if (JadeStringUtil.isIntegerEmpty(getIdParent())) {
                clone.setIdParent(getIdBaseIndemisation());
            } else {
                clone.setIdParent(getIdParent());
            }
        } else {
            clone.setIdParent("0");
        }

        // On ne veut pas de la validation pendant une duplication
        clone.wantCallValidate(false);

        return clone;
    }

    /**
     * getter pour l'attribut attestation jours.
     * 
     * @return la valeur courante de l'attribut attestation jours
     */
    public String getAttestationJours() {
        return attestationJours;
    }

    /**
     * getter pour l'attribut canton libelle.
     * 
     * @return la valeur courante de l'attribut canton libelle
     */
    public String getCantonLibelle() {
        return getSession().getCodeLibelle(getCsCantonImpotSource());
    }

    /**
     * getter pour le canton de l'impôt à la source
     * 
     * @return la valeur (Code Système) du canton : - Si Impôt à la source dans prononcé --> 1 Soit le code existant, 2
     *         si vide, a le code du canton de domicile, b le code du canton de la caisse, c sinon vide.
     */
    public String getCsCantonImpotSource() {

        try {
            if (loadPrononce(null).getSoumisImpotSource().booleanValue()) {
                if (JadeStringUtil.isBlankOrZero(csCantonImpotSource)) {
                    return loadPrononce(null).getCsCantonImpositionSource();
                    /*
                     * PRTiersWrapper tiers = loadPrononce(null).loadDemande(null).loadTiers(); if(tiers!=null && !
                     * JadeStringUtil.isEmpty(tiers.getProperty (PRTiersWrapper.PROPERTY_ID_CANTON))){ return
                     * tiers.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON); }
                     * 
                     * return CaisseHelperFactory.getInstance().getCsDefaultCantonCaisse
                     * (getSession().getApplication());
                     */
                } else {
                    return csCantonImpotSource;
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * getter pour l'attribut csEtat.
     * 
     * @return la valeur courante de l'attribut csEtat
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * getter pour l'attribut motif interruption.
     * 
     * @return la valeur courante de l'attribut motif interruption
     */
    public String getCsMotifInterruption() {
        return csMotifInterruption;
    }

    /**
     * getter pour l'attribut cs type base.
     * 
     * @return la valeur courante de l'attribut cs type base
     */
    public String getCsTypeBase() {
        return csTypeBase;
    }

    /**
     * getter pour l'attribut cs type IJ.
     * 
     * @return la valeur courante de l'attribut cs type IJ
     */
    public String getCsTypeIJ() {
        return csTypeIJ;
    }

    /**
     * getter pour l'attribut date debut periode.
     * 
     * @return la valeur courante de l'attribut date debut periode
     */
    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    /**
     * getter pour l'attribut date fin periode.
     * 
     * @return la valeur courante de l'attribut date fin periode
     */
    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    /**
     * getter pour l'attribut description prononce.
     * 
     * @return la valeur courante de l'attribut description prononce
     */
    public String getDescriptionPrononce() {
        try {
            if (loadPrononce(null) != null) {
                return loadPrononce(null).getIdPrononce() + " / "
                        + new JADate(loadPrononce(null).getDateDebutPrononce()).toStr(".");
            } else {
                return getIdPrononce();
            }
        } catch (Exception e) {
            return getIdPrononce();
        }
    }

    /**
     * getter pour l'attribut etat libelle.
     * 
     * @return la valeur courante de l'attribut etat libelle
     */
    public String getEtatLibelle() {
        return getSession().getCodeLibelle(getCsEtat());
    }

    /**
     * getter pour l'attribut full description prononce.
     * 
     * @return la valeur courante de l'attribut full description prononce
     */
    public String[] getFullDescriptionPrononce() {
        String[] result = new String[3];

        result[0] = new String();
        result[1] = new String();
        result[2] = new String();

        result[0] = getDescriptionPrononce();

        try {
            result[1] = loadPrononce(null).loadDemande(null).loadTiers()
                    .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)
                    + " "
                    + loadPrononce(null).loadDemande(null).loadTiers().getProperty(PRTiersWrapper.PROPERTY_NOM)
                    + " "
                    + loadPrononce(null).loadDemande(null).loadTiers().getProperty(PRTiersWrapper.PROPERTY_PRENOM);
            result[2] = loadPrononce(null).loadDemande(null).loadTiers()
                    .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        } catch (Exception e) {
        }

        return result;
    }

    /**
     * getter pour l'attribut id base indemisation.
     * 
     * @return la valeur courante de l'attribut id base indemisation
     */
    public String getIdBaseIndemisation() {
        return idBaseIndemisation;
    }

    /**
     * getter pour l'attribut id correction.
     * 
     * @return la valeur courante de l'attribut id correction
     */
    public String getIdCorrection() {
        return idCorrection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.tools.PRHierarchique#getIdMajeur()
     */
    /**
     * getter pour l'attribut id majeur.
     * 
     * @return la valeur courante de l'attribut id majeur
     */
    @Override
    public String getIdMajeur() {
        return getIdBaseIndemisation();
    }

    /**
     * getter pour l'attribut id parent.
     * 
     * @return la valeur courante de l'attribut id parent
     */
    @Override
    public String getIdParent() {
        return idParent;
    }

    /**
     * getter pour l'attribut id prononce.
     * 
     * @return la valeur courante de l'attribut id prononce
     */
    public String getIdPrononce() {
        return idPrononce;
    }

    /**
     * getter pour l'attribut motif interruption libelle.
     * 
     * @return la valeur courante de l'attribut motif interruption libelle
     */
    public String getMotifInterruptionLibelle() {
        return getSession().getCodeLibelle(getCsMotifInterruption());
    }

    /**
     * getter pour l'attribut nombre jours couverts.
     * 
     * @return la valeur courante de l'attribut nombre jours couverts
     */
    public String getNombreJoursCouverts() {
        if (JadeStringUtil.isIntegerEmpty(nombreJoursCouverts)) {
            return "";
        } else {
            return nombreJoursCouverts;
        }
    }

    /**
     * getter pour l'attribut nombre jours externe.
     * 
     * @return la valeur courante de l'attribut nombre jours externe
     */
    public String getNombreJoursExterne() {
        if (JadeStringUtil.isIntegerEmpty(nombreJoursExterne)) {
            return "";
        } else {
            return nombreJoursExterne;
        }
    }

    /**
     * getter pour l'attribut nombre jours interne.
     * 
     * @return la valeur courante de l'attribut nombre jours interne
     */
    public String getNombreJoursInterne() {
        if (JadeStringUtil.isIntegerEmpty(nombreJoursInterne)) {
            return "";
        } else {
            return nombreJoursInterne;
        }
    }

    /**
     * getter pour l'attribut nombre jours interruption.
     * 
     * @return la valeur courante de l'attribut nombre jours interruption
     */
    public String getNombreJoursInterruption() {
        if (JadeStringUtil.isIntegerEmpty(nombreJoursInterruption)) {
            return "";
        } else {
            return nombreJoursInterruption;
        }
    }

    /**
     * getter pour l'attribut remarque.
     * 
     * @return la valeur courante de l'attribut remarque
     */
    public String getRemarque() {
        return remarque;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getTauxImpotSource() {
        try {
            if (loadPrononce(null).getSoumisImpotSource().booleanValue()) {
                if (JadeStringUtil.isBlankOrZero(tauxImpotSource)) {
                    return loadPrononce(null).getTauxImpositionSource();
                } else {
                    return tauxImpotSource;
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * getter pour l'attribut unique primary key.
     * 
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdBaseIndemisation();
    }

    /**
     * getter pour l'attribut creer formulaires.
     * 
     * @return la valeur courante de l'attribut creer formulaires
     */
    public boolean isCreerFormulaires() {
        return creerFormulaires;
    }

    private boolean isEnfant() {
        return !JadeStringUtil.isIntegerEmpty(idParent);
    }

    /**
     * si cette base est dans l'état annulé, retourne son seul enfant possible ou le seul enfant possible de son parent
     * qui est dans l'état valide ou communiqué.
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJBaseIndemnisation loadEnfantActif(BITransaction transaction) throws Exception {
        if (IIJBaseIndemnisation.CS_ANNULE.equals(csEtat) && (enfantActif == null)) {
            IJBaseIndemnisationManager bases = new IJBaseIndemnisationManager();

            bases.setForIdParent(!JadeStringUtil.isIntegerEmpty(idParent) ? idParent : idBaseIndemisation);
            bases.setForCsEtats(new String[] { IIJBaseIndemnisation.CS_VALIDE, IIJBaseIndemnisation.CS_COMMUNIQUE });
            bases.setSession(getSession());
            if (transaction == null) {
                bases.find();
            } else {
                bases.find(transaction);
            }

            if (!bases.isEmpty()) {
                enfantActif = (IJBaseIndemnisation) bases.get(0);
            }
        }

        return enfantActif;
    }

    /**
     * retourne son (seul possible) enfant de correction.
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJBaseIndemnisation loadEnfantCorrection() throws Exception {
        if (enfantCorrection == null) {
            IJBaseIndemnisationManager bases = new IJBaseIndemnisationManager();

            bases.setForIdCorrection(idBaseIndemisation);
            bases.setSession(getSession());
            bases.find();

            if (!bases.isEmpty()) {
                enfantCorrection = (IJBaseIndemnisation) bases.get(0);
            }
        }

        return enfantCorrection;
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJPrononce loadPrononce(BITransaction transaction) throws Exception {
        if ((prononce == null) && !JadeStringUtil.isIntegerEmpty(getIdPrononce())) {
            prononce = IJPrononce.loadPrononce(getSession(), transaction, getIdPrononce(), getCsTypeIJ());
        }

        return prononce;
    }

    /**
     * si le nombre de jours externes est renseigne, retourne cette valeur, sinon compte le nombre de jours externe dans
     * le calendrier et retourne cette valeur.
     * 
     * @return la valeur courante de l'attribut nombre jours externe
     */
    public String nombreJoursExterne() {
        if (JadeStringUtil.isIntegerEmpty(nombreJoursExterne)) {
            return String.valueOf(JadeStringUtil.occurrencesOf(attestationJours,
                    IIJBaseIndemnisation.IJ_CALENDAR_EXTERNE));
        } else {
            return nombreJoursExterne;
        }
    }

    /**
     * si le nombre de jours internes est renseigne, retourne cette valeur, sinon compte le nombre de jours interne dans
     * le calendrier et retourne cette valeur.
     * 
     * @return la valeur courante de l'attribut nombre jours interne
     */
    public String nombreJoursInterne() {
        if (JadeStringUtil.isIntegerEmpty(nombreJoursInterne)) {
            return String.valueOf(JadeStringUtil.occurrencesOf(attestationJours,
                    IIJBaseIndemnisation.IJ_CALENDAR_INTERNE));
        } else {
            return nombreJoursInterne;
        }
    }

    /**
     * setter pour l'attribut attestation jours.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setAttestationJours(String string) {
        attestationJours = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setCsCantonImpotSource(String string) {
        csCantonImpotSource = string;
    }

    /**
     * setter pour l'attribut csEtat.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsEtat(String string) {
        csEtat = string;
    }

    /**
     * setter pour l'attribut motif interruption.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsMotifInterruption(String string) {
        csMotifInterruption = string;
    }

    /**
     * setter pour l'attribut cs type base.
     * 
     * @param csTypeBase
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeBase(String csTypeBase) {
        this.csTypeBase = csTypeBase;
    }

    /**
     * setter pour l'attribut cs type IJ.
     * 
     * @param csTypeIJ
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeIJ(String csTypeIJ) {
        this.csTypeIJ = csTypeIJ;
    }

    /**
     * setter pour l'attribut date debut periode.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutPeriode(String string) {
        dateDebutPeriode = string;
    }

    /**
     * setter pour l'attribut date fin periode.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinPeriode(String string) {
        dateFinPeriode = string;
    }

    /**
     * setter pour l'attribut id base indemisation.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdBaseIndemisation(String string) {
        idBaseIndemisation = string;
    }

    /**
     * setter pour l'attribut id correction.
     * 
     * @param idCorrection
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdCorrection(String idCorrection) {
        this.idCorrection = idCorrection;
    }

    /**
     * setter pour l'attribut id parent.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdParent(String string) {
        idParent = string;
    }

    /**
     * setter pour l'attribut id prononce.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrononce(String string) {
        idPrononce = string;
    }

    /**
     * setter pour l'attribut nombre jours couverts.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreJoursCouverts(String string) {
        nombreJoursCouverts = string;
    }

    /**
     * setter pour l'attribut nombre jours externe.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreJoursExterne(String string) {
        nombreJoursExterne = string;
    }

    /**
     * setter pour l'attribut nombre jours interne.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreJoursInterne(String string) {
        nombreJoursInterne = string;
    }

    /**
     * setter pour l'attribut nombre jours interruption.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreJoursInterruption(String string) {
        nombreJoursInterruption = string;
    }

    /**
     * setter pour l'attribut remarque.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setRemarque(String string) {
        remarque = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setTauxImpotSource(String string) {
        tauxImpotSource = string;
    }

    /**
     * setter pour l'attribut unique primary key.
     * 
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdBaseIndemisation(pk);
    }

    /**
     * @param creerFormulaires
     *            DOCUMENT ME!
     */
    public void wantCreerFormulaires(boolean creerFormulaires) {
        this.creerFormulaires = creerFormulaires;
    }
}
