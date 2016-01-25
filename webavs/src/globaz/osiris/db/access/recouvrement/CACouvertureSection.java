package globaz.osiris.db.access.recouvrement;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import globaz.osiris.db.contentieux.CAMotifContentieuxManager;
import globaz.osiris.translation.CACodeSystem;
import java.math.BigInteger;

/**
 * Représente une entité de type CouvertureSection.
 * 
 * @author Arnaud Dostes, 23-mar-2005
 */
public class CACouvertureSection extends CABEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String DATE_FIN = "31.12.2999";
    public static final String FIELD_IDCOUVERTURESECTION = "IDCOUVERTURESECT";
    public static final String FIELD_IDPLANRECOUVREMENT = "IDPLANRECOUVREMENT";
    public static final String FIELD_IDSECTION = "IDSECTION";
    public static final String FIELD_NUMEROORDRE = "NUMEROORDRE";
    public static final String TABLE_NAME = "CAPLCSP";
    private String idCouvertureSection = "";
    private String idPlanRecouvrement = "";
    private String idSection = "";
    private String numeroOrdre = "";
    private CAPlanRecouvrement planRecouvrement = null;
    private CASection section = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        // on bloque la section
        CASection sectionCouverte = getSection();
        sectionCouverte.setContentieuxEstSuspendu(Boolean.TRUE);
        // Si ce n'est pas pour de la part pénal.
        if (!planRecouvrement.getPartPenale().booleanValue()) {
            if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
                sectionCouverte.setIdMotifContentieuxSuspendu(CACodeSystem.CS_PLAN_RECOUVREMENT);
                sectionCouverte.setDateSuspendu(CACouvertureSection.DATE_FIN);

                sectionCouverte.update(transaction);
            } else {
                // Si contentieux Aquila
                // On met à jour la section
                sectionCouverte.update(transaction);

                // On ajoute le motif dans la table des motifs
                CAMotifContentieux motif = new CAMotifContentieux();
                motif.setSession(getSession());
                motif.setIdSection(sectionCouverte.getIdSection());
                motif.setIdMotifBlocage(CACodeSystem.CS_PLAN_RECOUVREMENT);
                motif.setDateDebut(getPlanRecouvrement().getDate());
                motif.setDateFin(CACouvertureSection.DATE_FIN);
                motif.add(transaction);
            }
        } else {
            // Si part pénale : On met un autre motif pour Aquila et on ne fait
            // rien pour l'ancien ctx.
            // Motif de blocage 204052 - Sursis au paiement sur part pénale
            // Paramétré dans FWPARP pour ne rien bloquer (PPRAVN = 0 -> ne
            // bloque rien).
            if (CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
                // Si contentieux Aquila
                // On met à jour la section
                sectionCouverte.update(transaction);

                // On ajoute le motif dans la table des motifs
                CAMotifContentieux motif = new CAMotifContentieux();
                motif.setSession(getSession());
                motif.setIdSection(sectionCouverte.getIdSection());
                motif.setIdMotifBlocage(CACodeSystem.CS_SURSIS_PMT_PART_PENALE);
                motif.setDateDebut(getPlanRecouvrement().getDate());
                motif.setDateFin(CACouvertureSection.DATE_FIN);
                motif.add(transaction);
            }
        }

        /* on fixe les numéros d'ordre */
        // if
        // (CAPlanRecouvrement.CS_VEN_VANC.equals(getPlanRecouvrement().getIdModeVentilation())
        // ||
        // CAPlanRecouvrement.CS_VEN_VREC.equals(getPlanRecouvrement().getIdModeVentilation()))
        // {
        // // on récupère les sections dans l'ordre de ventilation
        // CASection[] sections =
        // CAPlanRecouvrement.serviceSectionsCouvrir(getSession(),
        // getIdPlanRecouvrement());
        // // on numérote l'ordre
        // HashMap ordre = new HashMap(sections.length);
        // for (int i = 0; i < sections.length; i++) {
        // CASection section = sections[i];
        // ordre.put(section.getIdSection(), String.valueOf(i + 1));
        // }
        // // on balaye nos sections couvertes
        // CACouvertureSectionManager couvertures = new
        // CACouvertureSectionManager();
        // couvertures.setSession(getSession());
        // couvertures.setForIdPlanRecouvrement(getIdPlanRecouvrement());
        // couvertures.find(transaction);
        // for (int i = 0; i < couvertures.size(); i++) {
        // CACouvertureSection couverture = (CACouvertureSection)
        // couvertures.getEntity(i);
        // couverture.wantCallMethodAfter(false);
        // couverture.setNumeroOrdre((String)
        // ordre.get(couverture.getIdSection()));
        // couverture.update(transaction);
        // }
        // }
        /* fin fix num ordre */
    }

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        // La section est toujours chargée
        section = (CASection) retrieveEntityByIdIfNeeded(transaction, getIdSection(), CASection.class, section);
    }

    /**
     * @see globaz.globall.db.BEntity#_autoInherits()
     */
    @Override
    protected boolean _autoInherits() {
        // Cette entité n'hérite pas d'une autre entité
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdCouvertureSection(this._incCounter(transaction, "0", _getTableName()));
        if (CAPlanRecouvrement.CS_VEN_VPRO.equals(getPlanRecouvrement().getIdModeVentilation())) {
            setNumeroOrdre("1");
        }
        /*
         * if (JadeStringUtil.isEmpty(getNumeroOrdre())) { // trouver le prochain numéro d'ordre
         * CACouvertureSectionManager sections = new CACouvertureSectionManager(); sections.setSession(getSession());
         * sections.setForIdPlanRecouvrement(getIdPlanRecouvrement()); sections.setOrder("NUMEROORDRE DESC");
         * sections.find(transaction); CACouvertureSection section = (CACouvertureSection) sections.getFirstEntity(); if
         * (section != null) { setNumeroOrdre("" + (Integer.parseInt(section.getNumeroOrdre()) + 1)); } else { if
         * (isNumeroOrdreEditable()) { setNumeroOrdre("1"); } } }
         */
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        if ((getPlanRecouvrement() != null) && !getPlanRecouvrement().getIdEtat().equals(CAPlanRecouvrement.CS_INACTIF)) {
            // Empêche la suppression si une ou plusieurs échéances existent
            CAEcheancePlanManager manager = new CAEcheancePlanManager();
            manager.setSession(getSession());
            manager.setForIdPlanRecouvrement(getIdPlanRecouvrement());
            try {
                manager.find(transaction);
            } catch (Exception e) {
                _addError(transaction, e.toString());
            }
            if (manager.size() > 0) {
                _addError(transaction, getSession().getLabel("SUPPR_SECTION_COUVERTE_IMPOSSIBLE"));
                return;
            }
        }

        // on débloque la section et on supprime l'id plan de la section
        CASection sectionCouverte = getSection();
        if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            if (CACodeSystem.CS_PLAN_RECOUVREMENT.equals(sectionCouverte.getIdMotifContentieuxSuspendu())) {
                // il est suspendu pour le bon motif
                sectionCouverte.setContentieuxEstSuspendu(Boolean.FALSE);
                sectionCouverte.setIdMotifContentieuxSuspendu("");
                sectionCouverte.setDateSuspendu("0");
            }
        } else {
            CAMotifContentieuxManager motifMgr = new CAMotifContentieuxManager();
            motifMgr.setSession(getSession());
            motifMgr.setForIdSection(sectionCouverte.getIdSection());
            motifMgr.setForIdMotifBlocage(CACodeSystem.CS_PLAN_RECOUVREMENT);
            motifMgr.find(transaction);
            CAMotifContentieux motif = (CAMotifContentieux) motifMgr.getFirstEntity();
            if (motif != null) {
                motif.setSession(getSession());
                motif.delete(transaction);
            }
        }
        sectionCouverte.setIdPlanRecouvrement("");
        sectionCouverte.update(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CACouvertureSection.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdCouvertureSection(statement.dbReadNumeric(CACouvertureSection.FIELD_IDCOUVERTURESECTION));
        setIdPlanRecouvrement(statement.dbReadNumeric(CACouvertureSection.FIELD_IDPLANRECOUVREMENT));
        setIdSection(statement.dbReadNumeric(CACouvertureSection.FIELD_IDSECTION));
        setNumeroOrdre(statement.dbReadNumeric(CACouvertureSection.FIELD_NUMEROORDRE));
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        BTransaction transaction = statement.getTransaction();
        // idPlanRecouvrement
        _propertyMandatory(transaction, getIdPlanRecouvrement(), getSession().getLabel("PLAN_OBLIGATOIRE"));
        getPlanRecouvrement(); // Instancie le plan
        // idSection
        _propertyMandatory(transaction, getIdSection(), getSession().getLabel("SECTION_OBLIGATOIRE"));
        getSection(); // Instancie la section
        CACouvertureSectionManager manager = null;

        // Est-ce que cette section n'est pas déjà couverte
        manager = new CACouvertureSectionManager();
        manager.setSession(getSession());
        manager.setForIdSection(getIdSection());
        try {
            manager.find(transaction);
        } catch (Exception e) {
            _addError(transaction, e.toString());
        }
        boolean sectionDejaCouverte = false;
        for (int i = 0; i < manager.size(); i++) {
            CACouvertureSection cs = (CACouvertureSection) manager.getEntity(i);

            if (!cs.getIdCouvertureSection().equals(getIdCouvertureSection())
                    && !cs.getPlanRecouvrement().getIdEtat().equals(CAPlanRecouvrement.CS_ANNULE)
                    && !cs.getPlanRecouvrement().getIdEtat().equals(CAPlanRecouvrement.CS_SOLDE)) {
                sectionDejaCouverte = true;
                _addError(transaction,
                        getSession().getLabel("SECTION_DEJA_COUVERTE") + " " + cs.getIdPlanRecouvrement());
            }
        }
        if (!sectionDejaCouverte) {
            getSection().setIdPlanRecouvrement(getIdPlanRecouvrement());
            getSection().save(transaction);
        }
        // numeroOrdre
        if (isNumeroOrdreEditable()) {
            try {
                if (new BigInteger(getNumeroOrdre()).compareTo(BigInteger.valueOf(0)) <= 0) {
                    _addError(transaction, getSession().getLabel("ORDRE_POSITIF"));
                }
            } catch (Exception e) {
                _addError(transaction, getSession().getLabel("ORDRE_POSITIF"));
            }
        }
        // else {
        //
        // setNumeroOrdre("0");
        // }

        if (!getPlanRecouvrement().getIdEtat().equals(CAPlanRecouvrement.CS_INACTIF)) {
            // on regarde si ce plan n'a pas déjà d'échéances
            CAEcheancePlanManager echeances = new CAEcheancePlanManager();
            echeances.setSession(getSession());
            echeances.setForIdPlanRecouvrement(getIdPlanRecouvrement());
            echeances.find(transaction);
            if (echeances.size() > 0) {
                _addError(transaction, getSession().getLabel("PLAN_CALCULE"));
            }
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CACouvertureSection.FIELD_IDCOUVERTURESECTION, getIdCouvertureSection());
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CACouvertureSection.FIELD_IDCOUVERTURESECTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdCouvertureSection()));
        statement.writeField(CACouvertureSection.FIELD_IDPLANRECOUVREMENT,
                this._dbWriteNumeric(statement.getTransaction(), getIdPlanRecouvrement()));
        statement.writeField(CACouvertureSection.FIELD_IDSECTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdSection()));
        statement.writeField(CACouvertureSection.FIELD_NUMEROORDRE,
                this._dbWriteNumeric(statement.getTransaction(), getNumeroOrdre()));
    }

    public CACompteAnnexe getCompteAnnexe() {
        return getPlanRecouvrement().getCompteAnnexe();
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdCouvertureSection() {
        return idCouvertureSection;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getNumeroOrdre() {
        return numeroOrdre;
    }

    /**
     * @return Le plan de recouvrement lié
     */
    public CAPlanRecouvrement getPlanRecouvrement() {
        planRecouvrement = (CAPlanRecouvrement) retrieveEntityByIdIfNeeded(null, getIdPlanRecouvrement(),
                CAPlanRecouvrement.class, planRecouvrement);
        return planRecouvrement;
    }

    /**
     * @return La section liée
     */
    public CASection getSection() {
        section = (CASection) retrieveEntityByIdIfNeeded(null, getIdSection(), CASection.class, section);
        return section;
    }

    /**
     * @return true si le numéro d'ordre peut être modifié
     */
    public boolean isNumeroOrdreEditable() {
        return (getPlanRecouvrement() != null)
                && getPlanRecouvrement().getIdModeVentilation().equals(CAPlanRecouvrement.CS_VEN_VPRIO);
    }

    /**
     * @return true si la section peut être modifiée
     */
    public boolean isSectionEditable() {
        return isNew();
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdCouvertureSection(String string) {
        idCouvertureSection = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdPlanRecouvrement(String string) {
        idPlanRecouvrement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdSection(String string) {
        idSection = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setNumeroOrdre(String string) {
        numeroOrdre = string;
    }
}
