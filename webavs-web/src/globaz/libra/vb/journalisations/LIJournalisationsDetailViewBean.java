package globaz.libra.vb.journalisations;

import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.journalisation.constantes.JOConstantes;
import globaz.journalisation.db.journalisation.access.JOComplementJournal;
import globaz.journalisation.db.journalisation.access.JOComplementJournalManager;
import globaz.journalisation.db.journalisation.access.JOGroupeJournal;
import globaz.journalisation.db.journalisation.access.JOJournalisation;
import globaz.journalisation.db.journalisation.access.JOReferenceProvenance;
import globaz.journalisation.db.journalisation.access.JOReferenceProvenanceManager;
import globaz.journalisation.implementation.service.JOServiceLocator;
import globaz.libra.db.dossiers.LIDossiersJointTiers;
import globaz.libra.db.journalisations.LIEcheancesMultipleManager;
import globaz.libra.db.utils.LIUtilsEntity;
import globaz.libra.utils.LIEcransUtil;
import globaz.libra.vb.LIAbstractPersistentObjectViewBean;
import globaz.pyxis.db.tiers.TITiersViewBean;
import ch.globaz.libra.constantes.ILIConstantesExternes;

public class LIJournalisationsDetailViewBean extends LIAbstractPersistentObjectViewBean {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private JOComplementJournal complementJournal = null;
    private String dateRappel = new String();
    private LIDossiersJointTiers dossierJointTier = null;
    private LIEcheancesMultipleManager echeanceMultipleManager = null;
    private JOGroupeJournal groupeJournal = null;
    private String idDossier = new String();
    private String idTiers = new String();

    private JOJournalisation journalisation = null;
    private String libelle = new String();
    private String remarque = new String();
    private TITiersViewBean tiers = null;
    // Pour exécution
    private String typeAction = new String();

    private LIUtilsEntity utils = null;

    // ~ Constructors
    // ----------------------------------------------------------------------------------------------------

    public LIJournalisationsDetailViewBean() {
        super();
        journalisation = new JOJournalisation();
        groupeJournal = new JOGroupeJournal();
        complementJournal = new JOComplementJournal();
        dossierJointTier = new LIDossiersJointTiers();
        tiers = new TITiersViewBean();
        utils = new LIUtilsEntity();
        echeanceMultipleManager = new LIEcheancesMultipleManager();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    public void add() throws Exception {
        BITransaction transaction = null;
        try {
            transaction = ((BSession) getISession()).newTransaction();
            transaction.openTransaction();
            processAdd(transaction);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    @Override
    public void delete() throws Exception {
        BITransaction transaction = null;
        try {
            transaction = ((BSession) getISession()).newTransaction();
            transaction.openTransaction();
            processDelete(transaction);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    public JOComplementJournal getComplementJournal() {
        return complementJournal;
    }

    public String getDateRappel() {
        return dateRappel;
    }

    public String getDetailGestionnaire() throws Exception {
        return LIEcransUtil.getDetailGestionnaire((BSession) getISession(), dossierJointTier.getIdGestionnaire());
    }

    public String getDetailTiersLigne() throws Exception {
        return LIEcransUtil.getDetailTiersLigne((BSession) getISession(), dossierJointTier.getIdTiers());
    }

    public String getDetailTiersLigneNew(String idTiers) throws Exception {
        return LIEcransUtil.getDetailTiersLigne((BSession) getISession(), idTiers);
    }

    public LIDossiersJointTiers getDossierJointTier() {
        return dossierJointTier;
    }

    public LIEcheancesMultipleManager getEcheanceMultipleManager() {
        return echeanceMultipleManager;
    }

    public JOGroupeJournal getGroupeJournal() {
        return groupeJournal;
    }

    @Override
    public String getId() {
        return journalisation.getIdJournalisation();
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public JOJournalisation getJournalisation() {
        return journalisation;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getRemarque() {
        return remarque;
    }

    @Override
    public BSpy getSpy() {
        return journalisation.getSpy();
    }

    public TITiersViewBean getTiers() {
        return tiers;
    }

    public String getTypeAction() {
        return typeAction;
    }

    public LIUtilsEntity getUtils() {
        return utils;
    }

    private void processAdd(BITransaction transaction) throws Exception {
        // ajout du groupeJournal
        groupeJournal.add(transaction);

        // ajout de journalisation
        journalisation.setDate(JACalendar.todayJJsMMsAAAA());
        journalisation.setCsTypeJournal(JOConstantes.CS_JO_JOURNALISATION);
        journalisation.setIdGroupeJournal(groupeJournal.getIdGroupeJournal());
        journalisation.setLibelle(getLibelle());
        journalisation.setIdUtilisateur(getISession().getUserId());
        journalisation.add(transaction);

        // ajout du complementJournal
        complementJournal.setIdJournalisation(journalisation.getIdJournalisation());
        complementJournal.setCsTypeCodeSysteme(JOConstantes.GR_CS_JO_FMT_ID);
        complementJournal.setValeurCodeSysteme(JOConstantes.CS_JO_FMT_MANUELLE);
        complementJournal.add(transaction);

        // ajout de la remarque
        utils.setIdJournalisation(journalisation.getIdJournalisation());
        utils.setRemarque(getRemarque());
        utils.add(transaction);

        // ajout de provenance dossier
        JOReferenceProvenance provenanceDoss = new JOReferenceProvenance();
        provenanceDoss.setSession((BSession) getISession());
        provenanceDoss.setIdJournalisation(journalisation.getIdJournalisation());
        provenanceDoss.setTypeReferenceProvenance(ILIConstantesExternes.REF_PRO_DOSSIER);
        provenanceDoss.setIdCleReferenceProvenance(getIdDossier());
        provenanceDoss.add(transaction);

        // ajout de provenance tiers
        JOReferenceProvenance provenanceTiers = new JOReferenceProvenance();
        provenanceTiers.setSession((BSession) getISession());
        provenanceTiers.setIdJournalisation(journalisation.getIdJournalisation());
        provenanceTiers.setTypeReferenceProvenance(ILIConstantesExternes.REF_PRO_TIERS);
        provenanceTiers.setIdCleReferenceProvenance(getIdTiers());
        provenanceTiers.add(transaction);
    }

    private void processDelete(BITransaction transaction) throws Exception {
        JOServiceLocator serviceLocator = JOServiceLocator.getInstance();
        serviceLocator.getJournalisationService().delete((BTransaction) transaction, journalisation);
    }

    private void processUpd(BITransaction transaction) throws Exception {
        // groupejoural
        groupeJournal.setDateRappel(getDateRappel());
        groupeJournal.update(transaction);

        // journalisation
        journalisation.setDateRappel(getDateRappel());
        journalisation.setLibelle(getLibelle());
        journalisation.update(transaction);

        // complementJournal
        complementJournal.update(transaction);

    }

    @Override
    public void retrieve() throws Exception {
        journalisation.retrieve();

        groupeJournal.setIdGroupeJournal(journalisation.getIdGroupeJournal());
        groupeJournal.retrieve();

        JOComplementJournalManager complementJournalManager = new JOComplementJournalManager();
        complementJournalManager.setSession((BSession) getISession());
        complementJournalManager.setForIdJournalisation(journalisation.getIdJournalisation());
        complementJournalManager.find();
        if (complementJournalManager.size() > 0) {
            complementJournal = (JOComplementJournal) complementJournalManager.getEntity(0);
        }

        JOReferenceProvenanceManager referenceProvenanceManager = new JOReferenceProvenanceManager();
        referenceProvenanceManager.setSession((BSession) getISession());
        referenceProvenanceManager.setForIdJournalisation(journalisation.getIdJournalisation());
        referenceProvenanceManager.find();

        for (int i = 0; i < referenceProvenanceManager.size(); i++) {
            JOReferenceProvenance refProvenance = (JOReferenceProvenance) referenceProvenanceManager.getEntity(i);
            if (ILIConstantesExternes.REF_PRO_DOSSIER.equals(refProvenance.getTypeReferenceProvenance())) {
                dossierJointTier.setIdDossier(refProvenance.getIdCleReferenceProvenance());
                dossierJointTier.retrieve();
            }
            if (ILIConstantesExternes.REF_PRO_TIERS.equals(refProvenance.getTypeReferenceProvenance())) {
                tiers.setIdTiers(refProvenance.getIdCleReferenceProvenance());
                tiers.retrieve();
            }
        }

        utils = new LIUtilsEntity();
        utils.setSession((BSession) getISession());
        utils.setAlternateKey(LIUtilsEntity.ALTERNATE_KEY_ID_JOURNALISATION);
        utils.setIdJournalisation(journalisation.getIdJournalisation());
        utils.retrieve();

        if (complementJournal.getValeurCodeSysteme().equals(JOConstantes.CS_JO_AVS_FMT_ENVOI_MULTIPLE)) {
            echeanceMultipleManager = new LIEcheancesMultipleManager();
            echeanceMultipleManager.setSession((BSession) getISession());
            echeanceMultipleManager.setForIdJournalisation(journalisation.getIdJournalisation());
            echeanceMultipleManager.find();
        }

    }

    public void setDateRappel(String dateRappel) {
        this.dateRappel = dateRappel;
    }

    @Override
    public void setId(String newId) {
        journalisation.setId(newId);
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    @Override
    public void setISession(BISession newSession) {
        super.setISession(newSession);
        if (newSession instanceof BSession) {
            journalisation.setSession((BSession) newSession);
            groupeJournal.setSession((BSession) newSession);
            complementJournal.setSession((BSession) newSession);
            dossierJointTier.setSession((BSession) newSession);
            tiers.setSession((BSession) newSession);
            echeanceMultipleManager.setSession((BSession) newSession);
        }
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public void setTypeAction(String typeAction) {
        this.typeAction = typeAction;
    }

    public void setUtils(LIUtilsEntity utils) {
        this.utils = utils;
    }

    @Override
    public void update() throws Exception {
        BITransaction transaction = null;
        try {
            transaction = ((BSession) getISession()).newTransaction();
            transaction.openTransaction();
            processUpd(transaction);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

}
