package globaz.apg.db.droits;

import ch.globaz.common.exceptions.Exceptions;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.common.sql.converters.LocalDateConverter;
import ch.globaz.common.util.Dates;
import ch.globaz.queryexec.bridge.jade.SCM;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.properties.APParameter;
import globaz.globall.api.BISession;
import globaz.globall.db.BConstants;
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
import globaz.prestation.clone.factory.IPRCloneable;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.constantes.IConstantes;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * BEntity représentant un droit paternité Créé le 01 Décembre 2020
 *
 * @author eniv
 */
public class APDroitProcheAidant extends APDroitLAPG implements IPRCloneable {


    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME_PAT = "APDROITPROCHEAIDANT";
    public static final String FIELDNAME_DUPLICATA = "VBBDUP";
    public static final String FIELDNAME_IDDROIT_PAI = "ID_DROIT";
    public static final String FIELDNAME_IDSITUATIONFAM = "VBISIF";
    public static final String FIELDNAME_REMARQUE = "REMARQUE";
    public static final String FIELDNAME_CARE_LEAVE_EVENT_ID = "CAREEVENTID";
    public static final String FIELDNAME_ID_DROIT_COPIE = "ID_DROIT_COPIE";

    private String nbrJourSoldes = "";
    private String idSituationFam = "";
    private Boolean duplicata = Boolean.FALSE;
    private String noCompte = "";
    private String noControlePers = "";
    private String noRevision = "";
    private String remarque = "";
    private String careLeaveEventID = "";
    private String idDroitCopie = "";


    public APDroitProcheAidant() { }

    public int calculerNbjourTotalIndemnise() {
        return  calculerNbjourTotalIndemnise(false);
    }

    private int calculerNbjourTotalIndemnise(boolean filtrerPeriodePlusAnciennne) {
        return  loadNbJourDateMin(filtrerPeriodePlusAnciennne).map(NbJourDateMin::getNbJours).orElse(0);
    }

    public int calculerNbJourDisponibleSansPeriodePlusRecente() {
        return this.getNbJourMax() - calculerNbjourTotalIndemnise(true);
    }

    public int calculerNbJourDisponible() {
        return this.getNbJourMax() - calculerNbjourTotalIndemnise(false);
    }

    private Integer getNbJourMax() {
        return APParameter.PROCHE_AIDANT_JOUR_MAX.findValueOrWithDateNow(this.getDateDebutDroit(), this.getSession());
    }

    public Optional<LocalDate> calculerDelai() {
        return loadNbJourDateMin(false).map(value -> {
            if (value.getDateDebutMin() == null) {
                return Dates.toDate(this.getDateDebutDroit());
            }
            return value.getDateDebutMin();
        }).map(this::calculerDelai);
    }

    public LocalDate calculerDelai(LocalDate date) {
        return this.calculerDelai(date, trouverNombreMoisMax());
    }

    public Integer trouverNombreMoisMax() {
        return APParameter.PROCHE_AIDANT_MOIS_MAX.findValueOrWithDateNow(this.getDateDebutDroit(), this.getSession());
    }

    public static LocalDate calculerDelai(LocalDate date, Integer nbMois) {
        return date.plusMonths(nbMois).minusDays(1);
    }

    public Optional<LocalDate> resolveDateDebutDelaiCadre() {
        Optional<LocalDate> localDate = loadNbJourDateMin(false)
                .map(NbJourDateMin::getDateDebutMin);

        if(localDate.isPresent()){
            return localDate;
        }

        return Optional.of(Dates.toDate(this.getDateDebutDroit()));
    }

    @Override
    protected void _beforeRetrieve(final BTransaction transaction) throws Exception {
        super._beforeRetrieve(transaction);
    }

    private Optional<NbJourDateMin> loadNbJourDateMin(boolean filtrerPeriodePlusAnciennne) {
        if (this.getIdDroit() == null || JadeStringUtil.isBlankOrZero(this.getIdDroit())) {
            return Optional.empty();
        }
        List<NbJourDateMin> list = loadNbJourDateMinGroupedByEventId(filtrerPeriodePlusAnciennne, this.careLeaveEventID);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    public List<NbJourDateMin> loadNbJourDateMinByEventId() {
        return loadNbJourDateMinGroupedByEventId(false, null);
    }

    private List<NbJourDateMin> loadNbJourDateMinGroupedByEventId(final boolean filtrerPeriodePlusAnciennne,
                                                                  final String careLeaveEventID) {
        String etatsDroit = String.join(
                ",",
                IAPDroitLAPG.CS_ETAT_DROIT_VALIDE,
                IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF);

        SQLWriter sqlWriter = SQLWriter.writeWithSchema()
                                       .append("select sum(schema.APPRESP.VHNNJS) as nb_jours, min(schema.APPRESP.VHDDEB) as date_debut_min")
                                       .append(", schema.APDROITPROCHEAIDANT.CAREEVENTID as care_leave_event_id")
                                       .append("from schema." + APDroitLAPG.TABLE_NAME_LAPG)
                                       .join("schema.APDROITPROCHEAIDANT ON schema.APDROITPROCHEAIDANT.ID_DROIT = schema.APDROIP.VAIDRO")
                                       .join("schema.APSIFMP ON schema.APSIFMP.VQIDRM = schema.APDROIP.VAIDRO")
                                       .join("schema.APPRESP ON schema.APPRESP.VHIDRO = schema.APDROIP.VAIDRO") // prendre en compte les jours des prestations réellement versé et pas ceux des périodes
                                       .append("where schema.APSIFMP.VQLAVS = (")
                                       .append("select distinct schema.APSIFMP.VQLAVS as nss_enfant")
                                       .from(APDroitLAPG.TABLE_NAME_LAPG)
                                       .join("schema.APDROITPROCHEAIDANT ON schema.APDROITPROCHEAIDANT.ID_DROIT = schema.APDROIP.VAIDRO")
                                       .join("schema.APSIFMP ON schema.APSIFMP.VQIDRM = schema.APDROIP.VAIDRO")
                                       .append(" where schema.APDROIP.VAIDRO = ?)", this.getIdDroit())
                                       .append(" and schema.APDROIP.VATETA in(?)", etatsDroit)
                                       .append(" and schema.APPRESP.VHDMOB > 0")                                // ne pas prendre en compte les jours des restitutions
                                       .append(" and schema.APDROITPROCHEAIDANT.CAREEVENTID = ?", careLeaveEventID)
                                       .append(" and ((schema.APDROIP.VAIPAR is null or schema.APDROIP.VAIPAR = 0") // qui n'a pas de droit enfant ...
                                       .append(" and 0 = (select count(child.VAIDRO) as nb")
                                       .append(" from schema.APDROIP as child")
                                       .append(" where child.VAIPAR = schema.APDROIP.VAIDRO")
                                       .append(" and child.VATETA in (?)))", etatsDroit)
                                       .append(" or ((schema.APDROIP.VAIPAR is not null and schema.APDROIP.VAIPAR != 0)") // ... où qui est le dernier droit enfant
                                       .append(" and schema.APDROIP.VAIDRO = (select max(child.VAIDRO)")
                                       .append(" from schema.APDROIP as child")
                                       .append(" where child.VAIPAR = schema.APDROIP.VAIPAR")
                                       .append(" and child.VATETA in (?))))", etatsDroit);
        if (filtrerPeriodePlusAnciennne) {
            sqlWriter.append("and SUBSTR(schema.APPRESP.VHDFIN,1,6) <= SUBSTR((select max(periodeMax.VHDFIN)")
                     .append("                                 from schema.APDROIP as currentDroit")
                     .append("                                inner join schema.APPRESP as periodeMax  ")
                     .append("                                   ON periodeMax.VHIDRO = currentDroit.VAIDRO")
                     .append("                      where VAIDRO = ?), 1,6)", this.getIdDroit());
        }
        sqlWriter.append("group by schema.APDROITPROCHEAIDANT.CAREEVENTID");
        sqlWriter.append("order by schema.APDROITPROCHEAIDANT.CAREEVENTID desc");

        List<NbJourDateMin> list = SCM.newInstance(NbJourDateMin.class)
                                      .session(this.getSession()).query(sqlWriter.toSql())
                                      .converters(new LocalDateConverter())
                                      .execute();
        return list;
    }

    public int calculerNbJourIndemnise() {
        SQLWriter sqlWriter = SQLWriter.writeWithSchema()
                                       .append("select sum(VFNJIN) from schema.APSIPRP where schema.APSIPRP.VFIDRO = ?", this.getIdDroit());

        return SCM.newInstance(BigDecimal.class)
                  .session(this.getSession())
                  .query(sqlWriter.toSql())
                  .executeAggregate().intValue();
    }

    public Optional<NbJourDateMin> rechercherNbJourDateMinPourLeDroit() {
        SQLWriter sqlWriter = SQLWriter.writeWithSchema()
                                       .append("select sum(VCNNBJ + VCNNBJOURSUP) as nb_Jours, min(schema.APPERIP.VCDDEB) as date_debut_min")
                                       .append("from schema." + APDroitLAPG.TABLE_NAME_LAPG)
                                       .join("schema.APDROITPROCHEAIDANT ON schema.APDROITPROCHEAIDANT.ID_DROIT = schema.APDROIP.VAIDRO")
                                       .join("schema.APPERIP ON schema.APPERIP.VCIDRO = schema.APDROIP.VAIDRO")
                                       .append("where schema.APDROIP.VAIDRO = ?", this.getIdDroit());

        return SCM.newInstance(NbJourDateMin.class).session(this.getSession())
                  .converters(new LocalDateConverter())
                  .query(sqlWriter.toSql()).execute().stream().findFirst();
    }

    public int calculerNbjourTotalDuDroit() {
        return rechercherNbJourDateMinPourLeDroit().map(NbJourDateMin::getNbJours).orElse(0);
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
            APDroitProcheAidant droitPat = (APDroitProcheAidant) dMgr.get(idEnfant);
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
            APDroitProcheAidant droitPat = (APDroitProcheAidant) o;

            mettrePrestationEnValide(transaction, droitPat.getIdDroit());
        } else {
            APDroitProcheAidant dm = new APDroitProcheAidant();
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
     * @param statement DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
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
        fromClause.append(APDroitProcheAidant.TABLE_NAME_PAT);
        fromClause.append(APDroitLAPG.SQL_ON);
        fromClause.append(schema);
        fromClause.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClause.append(APDroitLAPG.SQL_DOT);
        fromClause.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        fromClause.append(APDroitLAPG.SQL_EQUALS);
        fromClause.append(schema);
        fromClause.append(APDroitProcheAidant.TABLE_NAME_PAT);
        fromClause.append(APDroitLAPG.SQL_DOT);
        fromClause.append(APDroitProcheAidant.FIELDNAME_IDDROIT_PAI);

        return fromClause.toString();
    }

    /**
     * @return la chaine TABLE_NAME_PAT
     *
     * @see BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return APDroitProcheAidant.TABLE_NAME_PAT;
    }

    /**
     * @see BEntity#_readProperties(BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        setIdDroit(statement.dbReadNumeric(APDroitProcheAidant.FIELDNAME_IDDROIT_PAI));
        idSituationFam = statement.dbReadNumeric(APDroitAPG.FIELDNAME_IDSITUATIONFAM);
        noCompte = statement.dbReadNumeric(APDroitAPG.FIELDNAME_NOCOMPTE);
        noControlePers = statement.dbReadNumeric(APDroitAPG.FIELDNAME_NOCONTROLEPERS);
        duplicata = statement.dbReadBoolean(APDroitProcheAidant.FIELDNAME_DUPLICATA);
        noRevision = statement.dbReadNumeric(APDroitAPG.FIELDNAME_REVISION);
        remarque = statement.dbReadString(APDroitProcheAidant.FIELDNAME_REMARQUE);
        careLeaveEventID = statement.dbReadString(APDroitProcheAidant.FIELDNAME_CARE_LEAVE_EVENT_ID);
        idDroitCopie= statement.dbReadString(APDroitProcheAidant.FIELDNAME_ID_DROIT_COPIE);
    }

    /**
     * @see BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        _validateBase(statement);
        BTransaction transaction = statement.getTransaction();
        _propertyMandatory(transaction, getDateDebutDroit(), getSession().getLabel("DATE_DEBUT_INCORRECTE"));

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
        statement.writeKey(
                APDroitProcheAidant.FIELDNAME_IDDROIT_PAI,
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
            statement.writeField(
                    APDroitProcheAidant.FIELDNAME_IDDROIT_PAI,
                    this._dbWriteNumeric(statement.getTransaction(), getIdDroit(), "idDroit"));
        }
        statement.writeField(
                APDroitAPG.FIELDNAME_IDSITUATIONFAM,
                this._dbWriteNumeric(statement.getTransaction(), idSituationFam, "idSituationFam"));
        statement.writeField(
                APDroitAPG.FIELDNAME_NOCOMPTE,
                this._dbWriteNumeric(statement.getTransaction(), noCompte, "noCompte"));
        statement.writeField(
                APDroitAPG.FIELDNAME_NOCONTROLEPERS,
                this._dbWriteNumeric(statement.getTransaction(), noControlePers, "noControlePers"));
        statement.writeField(APDroitProcheAidant.FIELDNAME_DUPLICATA, this._dbWriteBoolean(statement.getTransaction(),
                                                                                           duplicata, BConstants.DB_TYPE_BOOLEAN_CHAR, "duplicata"));
        statement.writeField(
                APDroitAPG.FIELDNAME_REVISION,
                this._dbWriteNumeric(statement.getTransaction(), noRevision, "noRevision"));
        statement.writeField(
                APDroitProcheAidant.FIELDNAME_REMARQUE,
                this._dbWriteString(statement.getTransaction(), remarque, "remarque"));
        statement.writeField(
                APDroitProcheAidant.FIELDNAME_CARE_LEAVE_EVENT_ID,
                this._dbWriteNumeric(statement.getTransaction(), careLeaveEventID, "careLeaveEventId"));

        statement.writeField(
                APDroitProcheAidant.FIELDNAME_ID_DROIT_COPIE,
                this._dbWriteNumeric(statement.getTransaction(), idDroitCopie, "idDroitCopie"));
    }

    @Override
    public IPRCloneable duplicate(int actionType) {
        APDroitProcheAidant clone = new APDroitProcheAidant();

        clone.setNpa(getNpa());
        clone.setDateDepot(getDateDepot());
        clone.setDateReception(getDateReception());
        clone.setGenreService(getGenreService());
        clone.setIdCaisse(getIdCaisse());
        clone.setIdDemande(getIdDemande());
        clone.setIdDroitCopie(this.getIdDroit());

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
        clone.setCareLeaveEventID(this.careLeaveEventID);

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
        if (actionType == ACTION_CREER_NOUVEAU_DROIT_PROCHE_AIDANT) {
            clone.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_VALIDE);
        } else {
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
     *
     * @see BEntity#hasSpy()
     */
    @Override
    public boolean hasSpy() {
        return true;
    }

    /**
     * setter pour l'attribut unique primary key
     *
     * @param pk une nouvelle valeur pour cet attribut
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

    public String getCareLeaveEventID() {
        return careLeaveEventID;
    }

    public void setCareLeaveEventID(final String careLeaveEventID) {
        this.careLeaveEventID = careLeaveEventID;
    }

    @Data
    public static class NbJourDateMin {
        private Integer nbJours;
        private LocalDate dateDebutMin;
        private Integer careLeaveEventId;
    }

    public boolean isACopy(){
        return !JadeStringUtil.isBlankOrZero(this.idDroitCopie);
    }

    public String getIdDroitCopie() {
        return idDroitCopie;
    }

    public void setIdDroitCopie(final String idDroitCopie) {
        this.idDroitCopie = idDroitCopie;
    }

    public static APDroitProcheAidant retrieve(String id, BISession session) {
        return Exceptions.checkedToUnChecked(() -> {
            APDroitProcheAidant apDroitProcheAidant = new APDroitProcheAidant();
            apDroitProcheAidant.setISession(session);
            apDroitProcheAidant.setId(id);
            apDroitProcheAidant.retrieve();
            return apDroitProcheAidant;
        }, "Error with this id:" + id);
    }


    @Getter
    @ToString
    @EqualsAndHashCode
    public static final class CareLeaveEventId {
        private final LocalDate delaiValidite;
        private final Integer id;
        private final boolean created;

        private CareLeaveEventId(LocalDate delaiValidite, Integer careLeaveEventId, boolean created) {
            this.delaiValidite = delaiValidite;
            this.id = careLeaveEventId;
            this.created = created;
        }

        public static CareLeaveEventId of(LocalDate delaiValidite, Integer careLeaveEventId) {
            return new CareLeaveEventId(delaiValidite, careLeaveEventId, false);
        }

        public static CareLeaveEventId ofCreated(LocalDate delaiValidite, Integer careLeaveEventId) {
            return new CareLeaveEventId(delaiValidite, careLeaveEventId, true);
        }
    }
}
