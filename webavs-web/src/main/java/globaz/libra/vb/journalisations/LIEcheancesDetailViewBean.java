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
import globaz.journalisation.db.journalisation.access.JOReferenceDestination;
import globaz.journalisation.db.journalisation.access.JOReferenceDestinationManager;
import globaz.journalisation.db.journalisation.access.JOReferenceProvenance;
import globaz.journalisation.db.journalisation.access.JOReferenceProvenanceManager;
import globaz.journalisation.implementation.service.JOServiceLocator;
import globaz.libra.db.dossiers.LIDossiers;
import globaz.libra.db.dossiers.LIDossiersJointTiers;
import globaz.libra.db.journalisations.LIEcheancesMultiple;
import globaz.libra.db.journalisations.LIEcheancesMultipleManager;
import globaz.libra.db.utilisateurs.LIUtilisateurs;
import globaz.libra.db.utilisateurs.LIUtilisateursManager;
import globaz.libra.db.utils.LIUtilsEntity;
import globaz.libra.utils.LIEcransUtil;
import globaz.libra.vb.LIAbstractPersistentObjectViewBean;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ch.globaz.libra.constantes.ILIConstantesExternes;

public class LIEcheancesDetailViewBean extends LIAbstractPersistentObjectViewBean {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * Inner Class
     * 
     * @author HPE
     */
    public class LIEcheanceMultipleObject extends Object {

        public String dateRece = "";
        public String idEchMul = "";
        public boolean isRecu = false;

    }

    private JOComplementJournal complementJournal = null;
    private String csType = new String();
    private String dateRappel = new String();
    private LIDossiersJointTiers dossierJointTier = null;
    private LIEcheancesMultipleManager echeanceMultipleManager = null;
    private JOGroupeJournal groupeJournal = null;
    private String idDossier = new String();

    private String idGestionnaire = new String();
    private String idTiers = new String();
    private JOJournalisation journalisation = null;
    private String libelle = new String();
    private List listEcheanceMult = new ArrayList();
    private JOReferenceDestination refDestination = null;
    private String remarque = new String();

    private TITiersViewBean tiers = null;

    private String userDefault = new String();

    // ~ Constructors
    // ----------------------------------------------------------------------------------------------------

    private LIUtilsEntity utils = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public LIEcheancesDetailViewBean() {
        super();
        journalisation = new JOJournalisation();
        groupeJournal = new JOGroupeJournal();
        complementJournal = new JOComplementJournal();
        dossierJointTier = new LIDossiersJointTiers();
        tiers = new TITiersViewBean();
        utils = new LIUtilsEntity();
        echeanceMultipleManager = new LIEcheancesMultipleManager();
        refDestination = new JOReferenceDestination();
    }

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

    public String getCsType() {
        return csType;
    }

    public String getDateRappel() {
        return dateRappel;
    }

    public String getDefaultUser() throws Exception {
        return getISession().getUserId();
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

    public String getIdGestionnaire() {
        return idGestionnaire;
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

    public List getListEcheanceMult() {
        return listEcheanceMult;
    }

    public JOReferenceDestination getRefDestination() {
        return refDestination;
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

    public String getUserDefault() throws Exception {

        LIUtilisateurs utilisateurs = new LIUtilisateurs();
        utilisateurs.setSession((BSession) getISession());
        utilisateurs.setIdUtilisateur(userDefault);
        utilisateurs.retrieve();

        return utilisateurs.getIdUtilisateurExterne();

    }

    public String[] getUsersList(String idDossier) throws Exception {

        if (null == idDossier) {
            idDossier = dossierJointTier.getIdDossier();
        }

        LIDossiers dossier = new LIDossiers();
        dossier.setSession((BSession) getISession());
        dossier.setIdDossier(idDossier);
        dossier.retrieve();

        userDefault = dossier.getIdGestionnaire();

        LIUtilisateursManager usrMgr = new LIUtilisateursManager();
        usrMgr.setSession((BSession) getISession());
        usrMgr.setForIdGroupe(dossier.getIdGroupe());
        usrMgr.find();

        String[] usersList = new String[usrMgr.size() * 2];
        int i = 0;

        for (Iterator iterator = usrMgr.iterator(); iterator.hasNext();) {
            LIUtilisateurs user = (LIUtilisateurs) iterator.next();

            usersList[i] = user.getIdUtilisateur();
            usersList[i + 1] = user.getIdUtilisateurExterne();

            i = i + 2;
        }

        return usersList;

    }

    public LIUtilsEntity getUtils() {
        return utils;
    }

    private void processAdd(BITransaction transaction) throws Exception {
        // ajout du groupeJournal
        groupeJournal.setDateRappel(getDateRappel());
        groupeJournal.add(transaction);

        // ajout de journalisation
        journalisation.setDate(JACalendar.todayJJsMMsAAAA());
        journalisation.setCsTypeJournal(JOConstantes.CS_JO_RAPPEL);
        journalisation.setIdUtilisateur(getIdGestionnaire());
        journalisation.setIdGroupeJournal(groupeJournal.getIdGroupeJournal());
        journalisation.setLibelle(getLibelle());
        journalisation.add(transaction);

        // ajout du complementJournal
        complementJournal.setIdJournalisation(journalisation.getIdJournalisation());
        complementJournal.setCsTypeCodeSysteme(JOConstantes.GR_CS_JO_FMT_ID);
        complementJournal.setValeurCodeSysteme(getCsType());
        complementJournal.add(transaction);

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

        // ajout de la remarque
        utils.setIdJournalisation(journalisation.getIdJournalisation());
        utils.setRemarque(getRemarque());
        utils.add(transaction);
    }

    private void processDelete(BITransaction transaction) throws Exception {

        // Delete de tout le système de l'échéance
        JOServiceLocator serviceLocator = JOServiceLocator.getInstance();
        serviceLocator.getJournalisationService().delete((BTransaction) transaction, journalisation);

        // Delete remarque
        utils.delete(transaction);

        for (Iterator iterator = getEcheanceMultipleManager().iterator(); iterator.hasNext();) {
            LIEcheancesMultiple echMul = (LIEcheancesMultiple) iterator.next();
            echMul.delete(transaction);
        }

    }

    private void processUpd(BITransaction transaction) throws Exception {

        // groupejoural
        groupeJournal.setDateRappel(getDateRappel());
        groupeJournal.update(transaction);

        // journalisation
        journalisation.setLibelle(getLibelle());
        journalisation.setIdUtilisateur(getIdGestionnaire());
        journalisation.update(transaction);

        // remarque
        utils.setRemarque(getRemarque());
        utils.update(transaction);

        // Echéances multiples
        List listEchMul = getListEcheanceMult();
        for (Iterator iter = listEchMul.iterator(); iter.hasNext();) {
            LIEcheanceMultipleObject echMulObj = (LIEcheanceMultipleObject) iter.next();

            LIEcheancesMultiple echMul = new LIEcheancesMultiple();
            echMul.setIdEcheanceMultiple(echMulObj.idEchMul);
            echMul.setSession((BSession) getISession());
            echMul.retrieve();

            echMul.setIsRecu(new Boolean(echMulObj.isRecu));
            echMul.setDateReception(echMulObj.dateRece);
            echMul.update(transaction);

        }

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

        JOReferenceDestinationManager referenceDestinationManager = new JOReferenceDestinationManager();
        referenceDestinationManager.setSession((BSession) getISession());
        referenceDestinationManager.setForIdJournalisation(journalisation.getIdJournalisation());
        referenceDestinationManager.setForTypeReferenceDestination(ILIConstantesExternes.CS_TYPE_FORM_PRESTATIONS);
        referenceDestinationManager.find();

        if (!referenceDestinationManager.isEmpty()) {
            refDestination = (JOReferenceDestination) referenceDestinationManager.getFirstEntity();
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

    public void setCsType(String csType) {
        this.csType = csType;
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

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
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
            utils.setSession((BSession) newSession);
            echeanceMultipleManager.setSession((BSession) newSession);
        }
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setListEcheanceMult(List listEcheanceMult) {
        this.listEcheanceMult = listEcheanceMult;
    }

    public void setRefDestination(JOReferenceDestination refDestination) {
        this.refDestination = refDestination;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
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
