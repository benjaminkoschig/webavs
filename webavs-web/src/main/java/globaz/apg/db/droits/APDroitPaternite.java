package globaz.apg.db.droits;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.application.APApplication;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.properties.APParameter;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.*;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.clone.factory.IPRCloneable;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.constantes.IConstantes;

import java.math.BigDecimal;

/**
 * BEntity représentant un droit paternité Créé le 01 Décembre 2020
 * 
 * @author eniv
 */
public class APDroitPaternite extends APDroitLAPG implements IPRCloneable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME_PAT = "APDROITPATERNITE";
    public static final String FIELDNAME_DUPLICATA = "VBBDUP";
    public static final String FIELDNAME_IDDROIT_PAT = "ID_DROIT";
    public static final String FIELDNAME_IDSITUATIONFAM = "VBISIF";
    public static final String FIELDNAME_REMARQUE = "REMARQUE";
    private String nbrJourSoldes = "";
    private String idSituationFam = "";
    private Boolean duplicata = Boolean.FALSE;
    private String noCompte = "";
    private String noControlePers = "";
    private String noRevision = "";
    private String remarque = "";

    /**
     * retourne la durée en jours d'un droit paternité.
     *
     * @param session
     * @return la valeur courante de l'attribut duree droit pat
     * @throws Exception
     *             Si la durée du droit paternité n'est pas configurée ou est invalide
     */
    public static final int getDureeDroitPat(BSession session) throws Exception {
        APApplication app = (APApplication) GlobazSystem.getApplication(APApplication.DEFAULT_APPLICATION_APG);

        try {
            return Integer.parseInt(app.getProperty(APApplication.PROPERTY_DROIT_PAT_DUREE_JOURS));
        } catch (NumberFormatException e) {
            throw new Exception(session.getLabel("DUREE_DROIPPAT_INVALIDE"));
        }
    }

    public APDroitPaternite() {
    }

    /**
     * @see BEntity#_afterDelete(BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {

        // effacement des situations familiales
        APSituationFamilialePatManager mgr = new APSituationFamilialePatManager();
        mgr.setSession(getSession());
        mgr.setForIdDroitPaternite(getIdDroit());
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        for (int idSitFam = 0; idSitFam < mgr.size(); ++idSitFam) {
            APSituationFamilialePat sitFam = (APSituationFamilialePat) mgr.get(idSitFam);
            sitFam.setSession(getSession());
            sitFam.delete(transaction);
        }

        // effacement des enfants du droit -> code inutile
        APDroitPaterniteManager dMgr = new APDroitPaterniteManager();
        dMgr.setSession(getSession());
        dMgr.setForIdDroitParent(getIdDroit());
        dMgr.find(transaction, BManager.SIZE_NOLIMIT);

        for (int idEnfant = 0; idEnfant < dMgr.size(); ++idEnfant) {
            APDroitPaternite droitPat = (APDroitPaternite) dMgr.get(idEnfant);
            droitPat.setSession(getSession());
            droitPat.delete(transaction);
        }
        // fin du code inutile

        // effacement des situations professionnelles
        APSituationProfessionnelleManager mgrsp = new APSituationProfessionnelleManager();

        mgrsp.setSession(getSession());
        mgrsp.setForIdDroit(getIdDroit());
        mgrsp.find(transaction, BManager.SIZE_NOLIMIT);

        for (int idSitPro = 0; idSitPro < mgrsp.size(); ++idSitPro) {
            APSituationProfessionnelle sitPro = (APSituationProfessionnelle) mgrsp.get(idSitPro);
            sitPro.setSession(getSession());
            sitPro.delete(transaction);
        }

        // effacement des prestations
        APPrestationManager pMgr = new APPrestationManager();
        pMgr.setSession(getSession());
        pMgr.setForIdDroit(getIdDroit());
        pMgr.find(transaction, BManager.SIZE_NOLIMIT);

        for (int idPrestation = 0; idPrestation < pMgr.size(); ++idPrestation) {
            APPrestation prestation = (APPrestation) pMgr.get(idPrestation);
            prestation.setSession(getSession());
            prestation.delete(transaction);
        }
        if (!JadeStringUtil.isBlankOrZero(getIdDroitParent())) {
            reactiverDroitParent(getSession(), transaction, getIdDroitParent());
        }

        super._afterDelete(transaction);
    }

    private void reactiverDroitParent(final BSession session, final BTransaction transaction, final String idDroitParent)
            throws Exception {
        /*
         * Recherche du droit parent, plusieurs droit peuvent avoir le même idDroitParent, il faut les ordonner par
         * idDroit afin de retrouver le parent direct
         * C'est le rôle du manager de bien les ordonner
         */
        APDroitPaterniteManager manager = new APDroitPaterniteManager();
        manager.setSession(session);
        manager.setForIdDroitParent(idDroitParent);
        manager.setIdDroitAExclure(getIdDroit());
        manager.find(transaction, BManager.SIZE_NOLIMIT);

        if (manager.getContainer().size() > 0) {
            Object o = manager.getContainer().get(0);
            APDroitPaternite droitPat = (APDroitPaternite) o;

            mettrePrestationEnValide(transaction, droitPat.getIdDroit());
        } else {
            APDroitPaternite dm = new APDroitPaternite();
            dm.setSession(getSession());
            dm.setIdDroit(idDroitParent);
            dm.retrieve(transaction);
            if (!dm.isNew()) {
                mettrePrestationEnValide(transaction, dm.getIdDroit());
            }
        }
    }

    /**
     * Récupère toutes les prestations du droit paternité en état ANNULE et les remets en état VALIDE
     *
     * @param transaction
     * @param idDroitPat
     * @throws Exception
     */
    private void mettrePrestationEnValide(final BTransaction transaction, String idDroitPat) throws Exception {
        APPrestationManager pMgr = new APPrestationManager();
        pMgr.setSession(getSession());
        pMgr.setForIdDroit(idDroitPat);
        pMgr.setForEtat(IAPPrestation.CS_ETAT_PRESTATION_ANNULE);
        pMgr.find(transaction, BManager.SIZE_NOLIMIT);

        boolean foundPrestation = !pMgr.getContainer().isEmpty();

        for (Object prest : pMgr.getContainer()) {
            APPrestation prestation = (APPrestation) prest;
            prestation.setSession(getSession());
            prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
            prestation.update(transaction);
        }

        // Dans ce cas, le droit parent doit être remis dans l'état
        // 'PARTIEL'.
        if (foundPrestation) {
            APDroitLAPG parent = new APDroitLAPG();
            parent.setSession(getSession());
            parent.setIdDroit(idDroitPat);
            parent.retrieve(transaction);
            PRAssert.notIsNew(parent, null);
            parent.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_PARTIEL);
            parent.update(transaction);
        }

    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        super._beforeUpdate(transaction);
    }

    /**
     * crée la jointure entre les tables APDroitLAPG et APDroitPaternite pour la lecture
     *
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @see BEntity#_getFrom(BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer fromClause = new StringBuffer();
        String schema = _getCollection();

        fromClause.append(schema);
        fromClause.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClause.append(APDroitLAPG.SQL_INNER_JOIN);
        fromClause.append(schema);
        fromClause.append(APDroitPaternite.TABLE_NAME_PAT);
        fromClause.append(APDroitLAPG.SQL_ON);
        fromClause.append(schema);
        fromClause.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClause.append(APDroitLAPG.SQL_DOT);
        fromClause.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        fromClause.append(APDroitLAPG.SQL_EQUALS);
        fromClause.append(schema);
        fromClause.append(APDroitPaternite.TABLE_NAME_PAT);
        fromClause.append(APDroitLAPG.SQL_DOT);
        fromClause.append(APDroitPaternite.FIELDNAME_IDDROIT_PAT);

        return fromClause.toString();
    }

    /**
     * @return la chaine TABLE_NAME_PAT
     * @see BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return APDroitPaternite.TABLE_NAME_PAT;
    }

    /**
     * @see BEntity#_readProperties(BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        setIdDroit(statement.dbReadNumeric(APDroitPaternite.FIELDNAME_IDDROIT_PAT));
        idSituationFam = statement.dbReadNumeric(APDroitAPG.FIELDNAME_IDSITUATIONFAM);
        noCompte = statement.dbReadNumeric(APDroitAPG.FIELDNAME_NOCOMPTE);
        noControlePers = statement.dbReadNumeric(APDroitAPG.FIELDNAME_NOCONTROLEPERS);
        duplicata = statement.dbReadBoolean(APDroitPaternite.FIELDNAME_DUPLICATA);
        noRevision = statement.dbReadNumeric(APDroitAPG.FIELDNAME_REVISION);
        remarque = statement.dbReadString(APDroitPaternite.FIELDNAME_REMARQUE);
    }

    /**
     * @see BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // validation de DroitLAPG
        _validateBase(statement);

        // check des dates
        BTransaction transaction = statement.getTransaction();

        // _checkDate(transaction, dateDeces,
        // getSession().getLabel("DATE_DECES_INCORRECTE"));
//        _checkDate(transaction, dateRepriseActiv, getSession().getLabel("DATE_REPRISE_ACTIVITE_INCORRECTE"));
        _propertyMandatory(transaction, getDateDebutDroit(), getSession().getLabel("DATE_DEBUT_INCORRECTE"));

        // check de la date de debut:
        // normalement: pas avant le 26.03.2005 (98 jours avant le 01.07.2005)
        // si caisse GE: pas avant le (112 jours avant le 01.07.2001)
//        if ("true".equals(PRAbstractApplication.getApplication(APApplication.DEFAULT_APPLICATION_APG).getProperty(
//                "isDroitPaterniteCantonale"))) {
//            if (!BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateDebutDroit(), "12.03.2001")) {
//                _addError(transaction, getSession().getLabel("DATE_DEBUT_DROIT_GE_PAT_TROP_VIEILLE"));
//            }
//        } else {
//            if (!BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateDebutDroit(), "26.03.2005")) {
//                _addError(transaction, getSession().getLabel("DATE_DEBUT_DROIT_PAT_TROP_VIEILLE"));
//            }
//        }

        // check de la date de debut:
        // la date de debut du droit ne peut pas etre supperieure a la date du
        // jour
        if (BSessionUtil.compareDateFirstGreater(getSession(), getDateDebutDroit(),
                new JADate(JACalendar.todayJJsMMsAAAA()).toStr("."))) {
            _addError(transaction, getSession().getLabel("DATE_DEBUT_DROIT_POSTERIEURE_DATE_DU_JOUR"));
        }
    }

    /**
     * @see BEntity#_writePrimaryKey(BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(APDroitPaternite.FIELDNAME_IDDROIT_PAT,
                this._dbWriteNumeric(statement.getTransaction(), getIdDroit(), "idDroit"));
    }

    /**
     * @see BEntity#_writeProperties(BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        if (_getAction() == BEntity.ACTION_COPY) {
            // si l'action est une copie, copier sans validation
            super._writeProperties(statement);
        } else {
            statement.writeField(APDroitPaternite.FIELDNAME_IDDROIT_PAT,
                    this._dbWriteNumeric(statement.getTransaction(), getIdDroit(), "idDroit"));
        }
        statement.writeField(APDroitAPG.FIELDNAME_IDSITUATIONFAM,
                this._dbWriteNumeric(statement.getTransaction(), idSituationFam, "idSituationFam"));
        statement.writeField(APDroitAPG.FIELDNAME_NOCOMPTE,
                this._dbWriteNumeric(statement.getTransaction(), noCompte, "noCompte"));
        statement.writeField(APDroitAPG.FIELDNAME_NOCONTROLEPERS,
                this._dbWriteNumeric(statement.getTransaction(), noControlePers, "noControlePers"));
        statement.writeField(APDroitPaternite.FIELDNAME_DUPLICATA, this._dbWriteBoolean(statement.getTransaction(),
                duplicata, BConstants.DB_TYPE_BOOLEAN_CHAR, "duplicata"));
        statement.writeField(APDroitAPG.FIELDNAME_REVISION,
                this._dbWriteNumeric(statement.getTransaction(), noRevision, "noRevision"));
        statement.writeField(APDroitPaternite.FIELDNAME_REMARQUE,
                this._dbWriteString(statement.getTransaction(), remarque, "remarque"));

    }

    @Override
    public IPRCloneable duplicate(int actionType) {
        APDroitPaternite clone = new APDroitPaternite();

        clone.setNpa(getNpa());
//        clone.setDateDebutDroit(getDateDebutDroit());
        clone.setDateDepot(getDateDepot());
        clone.setDateReception(getDateReception());
        clone.setGenreService(getGenreService());
        clone.setIdCaisse(getIdCaisse());
        clone.setIdDemande(getIdDemande());

        if (null != getSession().getUserId()) {
            clone.setIdGestionnaire(getSession().getUserId());
        } else {
            clone.setIdGestionnaire("");
        }

        clone.setIdGestionnaire(getIdGestionnaire());

        if (IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_APG_PARENT == actionType) {
            clone.setIdDroitParent(null);
        } else {
            // un seul niveau hiérarchique... Si l'on clone un fils, le parent du droit
            // cloné sera le même que le parent du fils.
            if (JadeStringUtil.isIntegerEmpty(getIdDroitParent())) {
                clone.setIdDroitParent(getIdDroit());
            } else {
                clone.setIdDroitParent(getIdDroitParent());
            }
        }

        clone.setNoDroit(getNoDroit());
        clone.setPays(getPays());
        clone.setReference(getReference());
        clone.setDroitAcquis(getDroitAcquis());
        clone.setIsSoumisImpotSource(getIsSoumisImpotSource());
        clone.setTauxImpotSource(getTauxImpotSource());

        // S'il s'agit d'une simple copie, la date de début du droit et de fin
        // ne doit pas être copiée
        // HACK ce test ne doit pas se faire dans la méthode duplicate
        if (IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_APG_PARENT != actionType) {
            clone.setDateDebutDroit(getDateDebutDroit());
            clone.setDateFinDroit(getDateFinDroit());
        }

        // On ne veut pas de la validation pendant une duplication
        clone.wantCallValidate(false);
        // Etat par défaut : attente
        if(actionType == ACTION_CREER_NOUVEAU_DROIT_PATERNITE_RESTI_FILS){
            clone.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_VALIDE);
        }else{
            clone.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        }

        return clone;
    }


    /**
     * getter pour l'attribut unique primary key
     *
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdDroit();
    }

    /**
     * Réactive le pspy pour la table des droits PAT.
     *
     * @return true
     * @see BEntity#hasSpy()
     */
    @Override
    public boolean hasSpy() {
        return true;
    }

    /**
     * setter pour l'attribut unique primary key
     *
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdDroit(pk);
    }

    /**
     * @see APDroitLAPG#validateBeforeCalcul(BTransaction)
     */
    @Override
    public boolean validateBeforeCalcul(BTransaction transaction) throws Exception {
        boolean retValue = true;

        // il doit y avoir au moins un enfant
        APEnfantPatManager mgr = new APEnfantPatManager();

        mgr.setSession(getSession());
        mgr.setForIdDroitPaternite(getIdDroit());
        mgr.find(transaction);

        if (mgr.size() < 1) {
            _addError(transaction, getSession().getLabel("AU_MOINS_UN_ENFANT"));
            retValue = false;
        }

        // la date de naissance du premier enfant doit être antérieure ou égale
        // é la date de debut du droit
        boolean enfantAvant = false;
        for (int idEnfant = 0; idEnfant < mgr.size(); ++idEnfant) {
            APEnfantPat enfant = (APEnfantPat) mgr.get(idEnfant);
            if (JAUtil.isDateEmpty(enfant.getDateNaissance())
                    || BSessionUtil.compareDateFirstLowerOrEqual(getSession(), enfant.getDateNaissance(),
                            getDateDebutDroit())) {
                enfantAvant = true;
                break;
            }
        }

        if (!enfantAvant) {
            retValue = false;
            _addError(transaction, getSession().getLabel("DATE_NAISSANCE_SUP_DATE_DEBUT_DROIT"));
        }
        return super.validateBeforeCalcul(transaction) && retValue;
    }

    protected void _validateBase(BStatement statement) throws Exception {
        BTransaction transaction = statement.getTransaction();

        _propertyMandatory(transaction, getGenreService(), getSession().getLabel("GENRE_SERVICE_OBLIGATOIRE"));
        _propertyMandatory(transaction, loadDemande().getIdTiers(), getSession().getLabel("TIERS_INTROUVABLE"));
        _checkDate(transaction, getDateDepot(), getSession().getLabel("DATE_DEPOT_INCORRECTE"));
        _checkDate(transaction, getDateReception(), getSession().getLabel("DATE_RECEPTION_INCORRECTE"));
        _checkDate(transaction, getDateDebutDroit(), getSession().getLabel("DATE_DEBUT_INCORRECTE"));

        // la date de debut du droit doit etre ulterieure au 01.07.1999
        if (new JACalendarGregorian().compare("01.07.1999", getDateDebutDroit()) == JACalendar.COMPARE_FIRSTUPPER) {
            _addError(transaction, getSession().getLabel("DROIT_TROP_ANCIEN"));
        }

        validerRemarque(statement);

        if (IConstantes.ID_PAYS_SUISSE.equals(getPays())) {
            _propertyMandatory(transaction, getNpa(), getSession().getLabel("NPA_REQUIS"));
        }

        if (!JadeStringUtil.isIntegerEmpty(getNpa())) {
            // le pays doit être la suisse
            if (!IConstantes.ID_PAYS_SUISSE.equals(getPays())) {
                _addError(transaction, getSession().getLabel("PAYS_DOIT_ETRE_SUISSE"));
            } else {
                // recherche du canton
                try {
                    String canton = PRTiersHelper.getCanton(getSession(), getNpa());

                    if (canton == null) {
                        _addError(transaction, getSession().getLabel("CANTON_INTROUVABLE"));
                    }
                } catch (Exception e) {
                    _addError(transaction, getSession().getLabel("CANTON_INTROUVABLE"));
                }
            }
        }
    }

    private void validerRemarque(BStatement statement) {
        String[] tab = PRStringUtils.split(getRemarque(), '\n');
        int limiteRetLigne = 5;
        if (tab.length > limiteRetLigne) {
            _addError(statement.getTransaction(), getSession().getLabel("REMARQUE_INVALIDE") + " " + limiteRetLigne);
        }
    }

    public String getNbrJourSoldes() {
        return nbrJourSoldes;
    }

    public void setNbrJourSoldes(String nbrJourSoldes) {
        this.nbrJourSoldes = nbrJourSoldes;
    }

    public Boolean getDuplicata() {
        return duplicata;
    }

    public void setDuplicata(Boolean duplicata) {
        this.duplicata = duplicata;
    }


    /**
     * getter pour l'attribut No Compte
     *
     * @return la valeur courante de l'attribut No Compte
     */
    public String getNoCompte() {
        // pour éviter d'avoir "0" si l'enregistrement ds la base est vide
        if (JadeStringUtil.isIntegerEmpty(noCompte)) {
            return "";
        }

        return noCompte;
    }

    /**
     * getter pour l'attribut No Controle Pers
     *
     * @return la valeur courante de l'attribut No Controle Pers
     */
    public String getNoControlePers() {
        // pour éviter d'avoir "0" si l'enregistrement ds la base est vide
        if (JadeStringUtil.isIntegerEmpty(noControlePers)) {
            return "";
        }

        return noControlePers;
    }

    public void setNoCompte(String noCompte) {
        this.noCompte = noCompte;
    }

    public void setNoControlePers(String noControlePers) {
        this.noControlePers = noControlePers;
    }

    public String getNoRevision() {
        return noRevision;
    }

    public void setNoRevision(String noRevision) {
        this.noRevision = noRevision;
    }

    public String getIdSituationFam() {
        return idSituationFam;
    }

    public void setIdSituationFam(String idSituationFam) {
        this.idSituationFam = idSituationFam;
    }

    @Override
    public String getRemarque() {
        return remarque;
    }

    @Override
    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }
}
