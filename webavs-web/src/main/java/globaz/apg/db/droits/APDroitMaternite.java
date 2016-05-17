package globaz.apg.db.droits;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.application.APApplication;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.clone.factory.IPRCloneable;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.constantes.IConstantes;
import ch.globaz.common.sql.QueryWriterExecutor;

/**
 * BEntity représentant un droit maternité Créé le 9 mai 05
 * 
 * @author vre
 */
public class APDroitMaternite extends APDroitLAPG implements IPRCloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME_MAT = "APDRMAP";
    public static final String FIELDNAME_DATEREPRISEACTIV = "VPDRAC";
    public static final String FIELDNAME_IDDROIT_MAT = "VPIDRO";

    private String dateRepriseActiv = "";

    /**
     * retourne la durée en jours d'un droit maternité.
     * 
     * @param session
     * @return la valeur courante de l'attribut duree droit mat
     * @throws Exception
     *             Si la durée du droit maternité n'est pas configurée ou est invalide
     */
    public static final int getDureeDroitMat(BSession session) throws Exception {
        APApplication app = (APApplication) GlobazSystem.getApplication(APApplication.DEFAULT_APPLICATION_APG);

        try {
            return Integer.parseInt(app.getProperty(APApplication.PROPERTY_DROIT_MAT_DUREE_JOURS));
        } catch (NumberFormatException e) {
            throw new Exception(session.getLabel("DUREE_DROITMAT_INVALIDE"));
        }
    }

    public APDroitMaternite() {
    }

    /**
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        // a faire en premier !!!
        int idDroitPrecedant = findIdDroitPrecedant(transaction);

        // effacement des situations familiales
        APSituationFamilialeMatManager mgr = new APSituationFamilialeMatManager();

        mgr.setSession(getSession());
        mgr.setForIdDroitMaternite(getIdDroit());
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        for (int idSitFam = 0; idSitFam < mgr.size(); ++idSitFam) {
            APSituationFamilialeMat sitFam = (APSituationFamilialeMat) mgr.get(idSitFam);
            sitFam.setSession(getSession());
            sitFam.delete(transaction);
        }

        // effacement des enfants
        APDroitMaterniteManager dMgr = new APDroitMaterniteManager();
        dMgr.setSession(getSession());
        dMgr.setForIdDroitParent(getIdDroit());
        dMgr.find(transaction, BManager.SIZE_NOLIMIT);

        for (int idEnfant = 0; idEnfant < dMgr.size(); ++idEnfant) {
            APDroitMaternite droitMat = (APDroitMaternite) dMgr.get(idEnfant);
            droitMat.setSession(getSession());
            droitMat.delete(transaction);
        }

        // repris depuis APDroitLAPG
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

        // les prestations annulées du droit parent ou du frère doivent être mise dans
        // l'etat valide
        if (!JadeStringUtil.isBlankOrZero(getIdDroitParent())) {
            pMgr = new APPrestationManager();

            pMgr.setSession(getSession());
            pMgr.setForIdDroit(getIdDroitParent());
            if (idDroitPrecedant != 0) {
                pMgr.setForIdDroit(String.valueOf(idDroitPrecedant));
            }
            pMgr.setForEtat(IAPPrestation.CS_ETAT_PRESTATION_ANNULE);
            pMgr.find(transaction, BManager.SIZE_NOLIMIT);

            boolean foundPrestation = false;
            for (int idPrestation = 0; idPrestation < pMgr.size(); ++idPrestation) {
                APPrestation prestation = (APPrestation) pMgr.get(idPrestation);
                prestation.setSession(getSession());
                prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
                prestation.update(transaction);
                foundPrestation = true;
            }
            // Dans ce cas, le droit parent doit être remis dans l'état
            // 'PARTIEL'.
            if (foundPrestation) {
                APDroitLAPG parent = new APDroitLAPG();
                parent.setSession(getSession());
                parent.setIdDroit(getIdDroitParent());
                if (idDroitPrecedant != 0) {
                    parent.setIdDroit(String.valueOf(idDroitPrecedant));
                }
                parent.retrieve(transaction);
                PRAssert.notIsNew(parent, null);
                parent.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_PARTIEL);
                parent.update(transaction);
            }
        }

        super._afterDelete(transaction);
    }

    private Integer findIdDroitPrecedant(BTransaction transaction) {
        if (!JadeStringUtil.isBlankOrZero(getIdDroitParent())) {
            return QueryWriterExecutor
                    .query("select max(anciennePrestation.VHIDRO) from schema.APPRESP as nouvellePrestation inner join schema.APPRESP as anciennePrestation on anciennePrestation.VHIRST = nouvellePrestation.VHIPRS where nouvellePrestation.VHIDRO = ?",
                            getIdDroit()).useTransaction(transaction).executeAggregateToInt();
        }
        return 0;
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
        calculerDateFinDroit();
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        super._beforeUpdate(transaction);
        calculerDateFinDroit();
    }

    /**
     * crée la jointure entre les tables APDroitLAPG et APDroitMaternite pour la lecture
     * 
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer fromClause = new StringBuffer();
        String schema = _getCollection();

        fromClause.append(schema);
        fromClause.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClause.append(APDroitLAPG.SQL_INNER_JOIN);
        fromClause.append(schema);
        fromClause.append(APDroitMaternite.TABLE_NAME_MAT);
        fromClause.append(APDroitLAPG.SQL_ON);
        fromClause.append(schema);
        fromClause.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClause.append(APDroitLAPG.SQL_DOT);
        fromClause.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        fromClause.append(APDroitLAPG.SQL_EQUALS);
        fromClause.append(schema);
        fromClause.append(APDroitMaternite.TABLE_NAME_MAT);
        fromClause.append(APDroitLAPG.SQL_DOT);
        fromClause.append(APDroitMaternite.FIELDNAME_IDDROIT_MAT);

        return fromClause.toString();
    }

    /**
     * @return la chaine TABLE_NAME_MAT
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return APDroitMaternite.TABLE_NAME_MAT;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        setIdDroit(statement.dbReadNumeric(APDroitMaternite.FIELDNAME_IDDROIT_MAT));
        dateRepriseActiv = statement.dbReadDateAMJ(APDroitMaternite.FIELDNAME_DATEREPRISEACTIV);
        // dateDeces = statement.dbReadDateAMJ(FIELDNAME_DATEDECES);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // validation de DroitLAPG
        _validateBase(statement);

        // check des dates
        BTransaction transaction = statement.getTransaction();

        // _checkDate(transaction, dateDeces,
        // getSession().getLabel("DATE_DECES_INCORRECTE"));
        _checkDate(transaction, dateRepriseActiv, getSession().getLabel("DATE_REPRISE_ACTIVITE_INCORRECTE"));
        _propertyMandatory(transaction, getDateDebutDroit(), getSession().getLabel("DATE_DEBUT_INCORRECTE"));

        // check de la date de debut:
        // normalement: pas avant le 26.03.2005 (98 jours avant le 01.07.2005)
        // si caisse GE: pas avant le (112 jours avant le 01.07.2001)
        if ("true".equals(PRAbstractApplication.getApplication(APApplication.DEFAULT_APPLICATION_APG).getProperty(
                "isDroitMaterniteCantonale"))) {
            if (!BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateDebutDroit(), "12.03.2001")) {
                _addError(transaction, getSession().getLabel("DATE_DEBUT_DROIT_GE_MAT_TROP_VIEILLE"));
            }
        } else {
            if (!BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateDebutDroit(), "26.03.2005")) {
                _addError(transaction, getSession().getLabel("DATE_DEBUT_DROIT__MAT_TROP_VIEILLE"));
            }
        }

        // check de la date de debut:
        // la date de debut du droit ne peut pas etre supperieure a la date du
        // jour
        if (BSessionUtil.compareDateFirstGreater(getSession(), getDateDebutDroit(),
                new JADate(JACalendar.todayJJsMMsAAAA()).toStr("."))) {
            _addError(transaction, getSession().getLabel("DATE_DEBUT_DROIT_POSTERIEURE_DATE_DU_JOUR"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(APDroitMaternite.FIELDNAME_IDDROIT_MAT,
                this._dbWriteNumeric(statement.getTransaction(), getIdDroit(), "idDroit"));
    }

    /**
     * @see globaz.globall.db.BEntity#__writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        if (_getAction() == BEntity.ACTION_COPY) {
            // si l'action est une copie, copier sans validation
            super._writeProperties(statement);
        } else {
            statement.writeField(APDroitMaternite.FIELDNAME_IDDROIT_MAT,
                    this._dbWriteNumeric(statement.getTransaction(), getIdDroit(), "idDroit"));
        }
        statement.writeField(APDroitMaternite.FIELDNAME_DATEREPRISEACTIV,
                this._dbWriteDateAMJ(statement.getTransaction(), dateRepriseActiv, "dateRepriseActiv"));
    }

    /**
     * Calculer la date de fin du droit pour ce droit maternité.
     */
    public void calculerDateFinDroit() throws Exception {
        JADate debut = new JADate(getDateDebutDroit());
        JACalendar cal = new JACalendarGregorian();
        /*
         * calculer la date de fin de droit en fonction de la durée officielle de l'allocation maternité. Au moment de
         * la création de cette classe, cette durée est de 98 jours. Comme cette valeur peut changer, elle est stockée
         * comme propriété de l'application.
         */
        JADate fin = cal.addDays(debut, APDroitMaternite.getDureeDroitMat(getSession()) - 1);
        setDateFinDroit(fin.toStr("."));
    }

    @Override
    public IPRCloneable duplicate(int actionType) {
        APDroitMaternite clone = new APDroitMaternite();

        clone.setNpa(getNpa());
        clone.setDateDebutDroit(getDateDebutDroit());
        clone.setDateDepot(getDateDepot());
        clone.setDateReception(getDateReception());
        clone.setDateRepriseActiv(getDateRepriseActiv());
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
        clone.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        return clone;
    }

    /**
     * getter pour l'attribut Date Reprise Activ
     * 
     * @return la valeur courante de l'attribut Date Reprise Activ
     */
    public String getDateRepriseActiv() {
        return dateRepriseActiv;
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
     * Réactive le pspy pour la table des droits MAT.
     * 
     * @return true
     * @see globaz.globall.db.BEntity#hasSpy()
     */
    @Override
    public boolean hasSpy() {
        return true;
    }

    /**
     * setter pour l'attribut Date Reprise Activ
     * 
     * @param dateRepriseActiv
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateRepriseActiv(String dateRepriseActiv) {
        if (!dateRepriseActiv.equals(this.dateRepriseActiv)) {
            setDeletePrestationRequis(true);
        }
        this.dateRepriseActiv = dateRepriseActiv;
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
     * @see globaz.apg.db.droits.APDroitLAPG#validateBeforeCalcul(globaz.globall.db.BTransaction)
     */
    @Override
    public boolean validateBeforeCalcul(BTransaction transaction) throws Exception {
        boolean retValue = true;

        // il doit y avoir au moins un enfant
        APEnfantMatManager mgr = new APEnfantMatManager();

        mgr.setSession(getSession());
        mgr.setForIdDroitMaternite(getIdDroit());
        mgr.find(transaction);

        if (mgr.size() < 1) {
            _addError(transaction, getSession().getLabel("AU_MOINS_UN_ENFANT"));
            retValue = false;
        }

        // la date de naissance du premier enfant doit être antérieure ou égale
        // é la date de debut du droit
        boolean enfantAvant = false;
        for (int idEnfant = 0; idEnfant < mgr.size(); ++idEnfant) {
            APEnfantMat enfant = (APEnfantMat) mgr.get(idEnfant);
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
        int limiteRetLigne = 3;
        if (tab.length > limiteRetLigne) {
            _addError(statement.getTransaction(), getSession().getLabel("REMARQUE_INVALIDE") + " " + limiteRetLigne);
        }
    }
}
