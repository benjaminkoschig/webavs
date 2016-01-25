package ch.globaz.osiris.businessimpl.service;

import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.process.journal.CAProcessComptabiliserJournal;
import java.util.Iterator;
import ch.globaz.osiris.business.data.JournalConteneur;
import ch.globaz.osiris.business.model.EcritureSimpleModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.model.OrdreVersementComplexModel;
import ch.globaz.osiris.business.service.JournalService;
import ch.globaz.osiris.exception.OsirisException;

/**
 * Implémentation des services d'un journal
 * 
 * @author SCO 19 mai 2010
 */
public class JournalServiceImpl implements JournalService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.osiris.business.service.JournalService#addOperation(ch.globaz
     * .osiris.business.model.OperationSimpleModel)
     */
    @Override
    public EcritureSimpleModel addEcriture(String idJournal, String codeDebitCredit, String idCompteAnnexe,
            String idSection, String date, String numeroRubrique, String montant) throws JadePersistenceException,
            JadeApplicationException {

        // Verification des parametres
        if (JadeStringUtil.isBlank(idJournal)) {
            throw new OsirisException("Unable to add ecriture, the id journal passed is null or empty");
        }

        // Verification de l'etat du journal
        CAJournal journal = new CAJournal();
        journal.setIdJournal(idJournal);

        try {

            journal.retrieve();

        } catch (Exception e) {
            throw new OsirisException("Technical exception, error to retrieve the journal's state", e);
        }

        if (journal.isNew()
                || !(JournalService.OUVERT.equals(journal.getEtat()) || JournalService.TRAITEMENT.equals(journal
                        .getEtat()))) {
            throw new OsirisException("Unable to add ecriture, the journal is not in a good state");
        }

        // Récupération de la rubrique car ce n'est pas l'id rubrique qui est
        // renseigné mais son id externe
        CARubrique rubrique = new CARubrique();
        rubrique.setIdExterne(numeroRubrique);
        rubrique.setAlternateKey(APIRubrique.AK_IDEXTERNE);
        try {

            rubrique.retrieve();

        } catch (Exception e) {
            throw new OsirisException("Technical exception, error to retrieve the id's rubrique", e);
        }

        // Si la rubrique n'a pas été récupérée
        if (rubrique.isNew()) {
            throw new OsirisException("Unable to retrieve the rubrique id");
        }

        CAEcriture ecriture = new CAEcriture();
        ecriture.setIdCompteAnnexe(idCompteAnnexe);
        ecriture.setIdJournal(idJournal);
        ecriture.setIdSection(idSection);
        ecriture.setDate(date);
        ecriture.setIdCompte(rubrique.getIdRubrique());
        ecriture.setMontant(montant);
        ecriture.setCodeDebitCredit(codeDebitCredit);

        try {
            ecriture.add();

            if (ecriture.hasErrors()) {
                throw new OsirisException("Unable to create the ecriture : " + ecriture.getErrors().toString());
            }

        } catch (Exception e) {
            throw new OsirisException("Technical exception, error to create the ecriture", e);
        }

        return this.parse(ecriture);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.osiris.business.service.JournalService#addEcritureByRefRubrique(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public EcritureSimpleModel addEcritureByRefRubrique(String idJournal, String libelle, String codeDebitCredit,
            String idCompteAnnexe, String idSection, String date, String referenceRubrique, String montant)
            throws JadePersistenceException, JadeApplicationException {
        // Verification des parametres
        if (JadeStringUtil.isBlank(idJournal)) {
            throw new OsirisException("Unable to add ecriture, the id journal passed is null or empty");
        }

        // Verification de l'etat du journal
        CAJournal journal = new CAJournal();
        journal.setIdJournal(idJournal);

        try {

            journal.retrieve();

        } catch (Exception e) {
            throw new OsirisException("Technical exception, error to retrieve the journal's state", e);
        }

        if (journal.isNew()
                || !(JournalService.OUVERT.equals(journal.getEtat()) || JournalService.TRAITEMENT.equals(journal
                        .getEtat()))) {
            throw new OsirisException("Unable to add ecriture, the journal is not in a good state");
        }

        // Récupération de la rubrique par sa référence
        CAReferenceRubrique refRubrique = new CAReferenceRubrique();
        refRubrique.setIdCodeReference(referenceRubrique);
        refRubrique.setAlternateKey(APIRubrique.AK_IDEXTERNE);

        try {

            refRubrique.retrieve();

        } catch (Exception e) {
            throw new OsirisException("Technical exception, error to retrieve the reference rubrique", e);
        }

        if (refRubrique.isNew()) {
            throw new OsirisException("Unable to retrieve the reference rubrique");
        }

        CAEcriture ecriture = new CAEcriture();
        ecriture.setLibelle(libelle);
        ecriture.setIdCompteAnnexe(idCompteAnnexe);
        ecriture.setIdJournal(idJournal);
        ecriture.setIdSection(idSection);
        ecriture.setDate(date);
        ecriture.setIdCompte(refRubrique.getIdRubrique());
        ecriture.setMontant(montant);
        ecriture.setCodeDebitCredit(codeDebitCredit);

        try {
            ecriture.add();

            if (ecriture.hasErrors()) {
                throw new OsirisException("Unable to create the ecriture : " + ecriture.getErrors().toString());
            }

        } catch (Exception e) {
            throw new OsirisException("Technical exception, error to create the ecriture", e);
        }

        return this.parse(ecriture);
    }

    @Override
    public OrdreVersementComplexModel addOrdreVersement(String idJournal, String idCompteAnnexe, String idSection,
            String idAdressePaiement, String date, String montant, String codeIsoMonnaieBoni,
            String codeIsoMonnaieDepot, String typeVirement, String natureOrdre, String motif)
            throws JadePersistenceException, JadeApplicationException {
        return addOrdreVersementCommun(idJournal, idCompteAnnexe, idSection, idAdressePaiement, date, montant,
                codeIsoMonnaieBoni, codeIsoMonnaieDepot, typeVirement, natureOrdre, motif, "");
    }

    @Override
    public OrdreVersementComplexModel addOrdreVersement(String idJournal, String idCompteAnnexe, String idSection,
            String idAdressePaiement, String date, String montant, String codeIsoMonnaieBoni,
            String codeIsoMonnaieDepot, String typeVirement, String natureOrdre, String motif, String referenceBVR)
            throws JadePersistenceException, JadeApplicationException {
        return addOrdreVersementCommun(idJournal, idCompteAnnexe, idSection, idAdressePaiement, date, montant,
                codeIsoMonnaieBoni, codeIsoMonnaieDepot, typeVirement, natureOrdre, motif, referenceBVR);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.osiris.business.service.JournalService#addOrdreVersement(ch
     * .globaz.osiris.business.model.OrdreVersementComplexModel)
     */
    private OrdreVersementComplexModel addOrdreVersementCommun(String idJournal, String idCompteAnnexe,
            String idSection, String idAdressePaiement, String date, String montant, String codeIsoMonnaieBoni,
            String codeIsoMonnaieDepot, String typeVirement, String natureOrdre, String motif, String referenceBVR)
            throws JadePersistenceException, JadeApplicationException {

        CAOperationOrdreVersement ordreVersement = new CAOperationOrdreVersement();
        ordreVersement.setIdJournal(idJournal);
        ordreVersement.setIdCompteAnnexe(idCompteAnnexe);
        ordreVersement.setIdSection(idSection);
        ordreVersement.setIdAdressePaiement(idAdressePaiement);
        ordreVersement.setDate(date);
        ordreVersement.setMontant(montant);
        ordreVersement.setCodeISOMonnaieBonification(codeIsoMonnaieBoni);
        ordreVersement.setCodeISOMonnaieDepot(codeIsoMonnaieDepot);
        ordreVersement.setTypeVirement(typeVirement);
        ordreVersement.setNatureOrdre(natureOrdre);
        ordreVersement.setMotif(motif);
        ordreVersement.setCodeDebitCredit(APIEcriture.DEBIT);
        ordreVersement.setReferenceBVR(referenceBVR);

        try {

            ordreVersement.add();

            if (ordreVersement.hasErrors()) {
                throw new OsirisException("Unable to create the ordreVersement : "
                        + ordreVersement.getErrors().toString());
            }

        } catch (Exception e) {
            throw new OsirisException("Technical exception, error to create ordre de versement", e);
        }

        return this.parse(ordreVersement);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.osiris.business.service.JournalService#comptabilise(ch.globaz
     * .osiris.business.model.JournalSimpleModel)
     */
    @Override
    public JournalSimpleModel comptabilise(JournalSimpleModel journalModel) throws JadePersistenceException,
            JadeApplicationException {

        // Verification de la présence d'un journal
        if (journalModel == null) {
            throw new OsirisException("Unable to comptabilise the journal, the journal passed is null");
        }
        if (JadeStringUtil.isDecimalEmpty(journalModel.getIdJournal())) {
            throw new OsirisException("Unable to comptabilise the journal, the journal passed has no id");
        }

        // Comptabilisation du journal
        CAProcessComptabiliserJournal comptabilisationProcess = new CAProcessComptabiliserJournal();
        comptabilisationProcess.setIdJournal(journalModel.getIdJournal());
        comptabilisationProcess.setImprimerJournal(false);

        // Récupère la session de l'application cliente :
        BSession clienteSession = (BSession) JadeThread.currentContext().getTemporaryObject("bsession");
        // Crée une nouvelle session pour attaquer OSIRIS parce que des
        // blaireaux se sont basés sur plusieurs transactions et que donc il est
        // nécessaire afin d'éviter une collusion de transaction de se baser sur
        // une session OSIRIS sinon, c'est la grosse shieessstraaak !
        // CECI EST UN GROS HACK !!! NECESSAIRE !!! Ref : VYJ
        // ET C'EST MOCHE !!!
        try {
            BSession session = new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS);
            clienteSession.connectSession(session);
            JadeThread.currentContext().storeTemporaryObject("bsession", session);

            BProcessLauncher.start(comptabilisationProcess);

        } catch (Exception e) {
            throw new OsirisException("Technical exception, Error in the process of comptabilisation", e);
        } finally {
            JadeThread.currentContext().storeTemporaryObject("bsession", clienteSession);
        }

        return journalModel;
    }

    @Override
    public JournalSimpleModel comptabiliseSynchrone(JournalSimpleModel journalModel) throws JadePersistenceException,
            JadeApplicationException {

        // Verification de la présence d'un journal
        if (journalModel == null) {
            throw new OsirisException("Unable to comptabilise the journal, the journal passed is null");
        }
        if (JadeStringUtil.isDecimalEmpty(journalModel.getIdJournal())) {
            throw new OsirisException("Unable to comptabilise the journal, the journal passed has no id");
        }

        // Comptabilisation du journal
        CAProcessComptabiliserJournal comptabilisationProcess = new CAProcessComptabiliserJournal();
        comptabilisationProcess.setIdJournal(journalModel.getIdJournal());
        comptabilisationProcess.setImprimerJournal(false);

        // Récupère la session de l'application cliente :
        BSession clienteSession = (BSession) JadeThread.currentContext().getTemporaryObject("bsession");
        // Crée une nouvelle session pour attaquer OSIRIS parce que des
        // blaireaux se sont basés sur plusieurs transactions et que donc il est
        // nécessaire afin d'éviter une collusion de transaction de se baser sur
        // une session OSIRIS sinon, c'est la grosse shieessstraaak !
        // CECI EST UN GROS HACK !!! NECESSAIRE !!! Ref : VYJ
        // ET C'EST MOCHE !!!
        try {
            BSession session = new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS);
            clienteSession.connectSession(session);
            JadeThread.currentContext().storeTemporaryObject("bsession", session);

            comptabilisationProcess.executeProcess();

        } catch (Exception e) {
            throw new OsirisException("Technical exception, Error in the process of comptabilisation", e);
        } finally {
            JadeThread.currentContext().storeTemporaryObject("bsession", clienteSession);
        }

        return journalModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.osiris.business.service.JournalService#create(ch.globaz.osiris .business.model.JournalSimpleModel)
     */
    @Override
    public JournalSimpleModel createJournal(String libelle, String dateValeur) throws JadePersistenceException,
            JadeApplicationException {

        // Verification des parametres
        if (JadeStringUtil.isBlank(libelle)) {
            throw new OsirisException("Unable to create Journal, the libelle passed is null or empty !");
        }
        if (JadeStringUtil.isBlank(dateValeur)) {
            throw new OsirisException("Unable to create Journal, the dateValeur passed is null or empty!");
        }

        // Création du journal
        CAJournal journal = new CAJournal();
        journal.setLibelle(libelle);
        journal.setDateValeurCG(dateValeur);
        journal.setTypeJournal(CAJournal.TYPE_AUTOMATIQUE);

        try {

            journal.add();

            if (journal.hasErrors()) {
                throw new OsirisException("Unable to create the journal : " + journal.getErrors().toString());
            }

        } catch (Exception e) {
            throw new OsirisException("Technical exception, Error in creating of the journal", e);
        }

        return this.parse(journal);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.osiris.business.service.JournalService#createJournalAndEcritures
     * (ch.globaz.osiris.business.data.JournalConteneur)
     */
    @Override
    public JournalSimpleModel createJournalAndOperations(JournalConteneur conteneur) throws JadePersistenceException,
            JadeApplicationException {

        // Verification des parametres
        if (conteneur == null) {
            throw new OsirisException("Unable to create journal and ecritures, the JournalConteneur passed is null");
        }
        if (conteneur.getJournalModel() == null) {
            throw new OsirisException(
                    "Unable to create journal ans ecritures, there are no journal in the JournalConteneur");
        }
        if (conteneur.getCollectionEcriture() == null) {
            throw new OsirisException(
                    "Unable to create journal ans ecritures, there are no ecritures in the JournalConteneur");
        }

        // Création du journal
        JournalSimpleModel journalModel = conteneur.getJournalModel();
        if (journalModel.isNew()) {
            journalModel = createJournal(journalModel.getLibelle(), journalModel.getDateValeurCG());
        }

        // Création des écritures
        Iterator<EcritureSimpleModel> itOpe = conteneur.getCollectionEcriture().iterator();
        while (itOpe.hasNext()) {
            EcritureSimpleModel opeModel = itOpe.next();
            opeModel.setIdJournal(journalModel.getIdJournal());

            // Récupération de la rubrique car ce n'est pas l'id rubrique qui
            // est renseigné mais son id externe
            // FIXME: incohérence entre utilisation du champ et type en DB
            // si le modèle EcritureSimpleModel était ajouté en DB tel quel, ca pèterait
            CARubrique rubrique = new CARubrique();

            // HACK
            if (opeModel.getIdRubrique() != null && opeModel.getIdRubrique().length() != 0) {
                rubrique.setId(opeModel.getIdRubrique());
            } else {
                rubrique.setIdExterne(opeModel.getIdCompte());
                rubrique.setAlternateKey(APIRubrique.AK_IDEXTERNE);
            }

            try {

                rubrique.retrieve();

            } catch (Exception e) {
                throw new OsirisException("Technical exception, error to retrieve the id's rubrique", e);
            }

            // Si la rubrique n'a pas été récupérée
            if (rubrique.isNew()) {
                throw new OsirisException("Unable to retrieve the rubrique id, rubrique is new");
            }

            opeModel.setIdCompte(rubrique.getIdRubrique());
            CAEcriture ecriture = this.parse(opeModel, rubrique.isUseCaissesProf());

            try {
                ecriture.add();

                if (ecriture.hasErrors()) {
                    throw new OsirisException("Unable to create the ecriture : " + ecriture.getErrors().toString());
                }

            } catch (Exception e) {
                throw new OsirisException("Technical exception, error to create the ecriture", e);
            }
        }

        // création des ordres de versement
        Iterator<OrdreVersementComplexModel> itOve = conteneur.getCollectionOrdreVersement().iterator();
        while (itOve.hasNext()) {
            OrdreVersementComplexModel oveModel = itOve.next();

            oveModel.getOperation().setIdJournal(journalModel.getIdJournal());

            this.addOrdreVersement(oveModel.getOperation().getIdJournal(), oveModel.getOperation().getIdCompteAnnexe(),
                    oveModel.getOperation().getIdSection(), oveModel.getOrdre().getIdAdressePaiement(), oveModel
                            .getOperation().getDate(), oveModel.getOperation().getMontant(), oveModel.getOrdre()
                            .getCodeISOMonnaieBonification(), oveModel.getOrdre().getCodeISOMonnaieDepot(), oveModel
                            .getOrdre().getTypeVirement(), oveModel.getOrdre().getNatureOrdre(), oveModel.getOrdre()
                            .getMotif());
        }

        return journalModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.osiris.business.service.JournalService#getSommeEcritures(ch
     * .globaz.osiris.business.model.JournalSimpleModel)
     */
    @Override
    public String getSommeEcritures(JournalSimpleModel journalModel) throws JadePersistenceException,
            JadeApplicationException {

        // Verification des parametres
        if (journalModel == null) {
            throw new OsirisException("Unable to get sum of ecriture, the model passed is null");
        }
        if (JadeStringUtil.isDecimalEmpty(journalModel.getIdJournal())) {
            throw new OsirisException("Unable to get sum of ecriture, the journal passed has no id");
        }

        return this.parse(journalModel)._getTotalEcritures();
    }

    /**
     * Perser d'une entité opération en un model d'écriture
     * 
     * @param ecriture
     * @return
     * @throws OsirisException
     */
    private EcritureSimpleModel parse(CAEcriture ecriture) throws OsirisException {

        if (ecriture == null) {
            throw new OsirisException("Unable to parse ecriture, the entity ecriture is null");
        }

        if (ecriture.isNew()) {
            return new EcritureSimpleModel();
        }

        EcritureSimpleModel ecritureModel = new EcritureSimpleModel();
        ecritureModel.setId(ecriture.getIdOperation());
        ecritureModel.setIdJournal(ecriture.getIdJournal());
        ecritureModel.setLibelle(ecriture.getLibelle());
        ecritureModel.setIdCompteAnnexe(ecriture.getIdCompteAnnexe());
        ecritureModel.setIdSection(ecriture.getIdSection());
        ecritureModel.setDate(ecriture.getDate());
        ecritureModel.setIdCompte(ecriture.getIdCompte());
        ecritureModel.setMontant(ecriture.getMontant());
        ecritureModel.setCodeDebitCredit(ecriture.getCodeDebitCredit());

        ecritureModel.setSpy(ecriture.getSpy().getFullData());

        return ecritureModel;

    }

    /**
     * Parser d'une entité journal en un model de journal
     * 
     * @param journal
     * @return
     * @throws OsirisException
     */
    private JournalSimpleModel parse(CAJournal journal) throws OsirisException {

        if (journal == null) {
            throw new OsirisException("Unable to parse journal, the entity journal is null");
        }

        if (journal.isNew()) {
            return new JournalSimpleModel();
        }

        JournalSimpleModel journalModel = new JournalSimpleModel();

        journalModel.setId(journal.getIdJournal());
        journalModel.setLibelle(journal.getLibelle());
        journalModel.setProprietaire(journal.getProprietaire());
        journalModel.setEtat(journal.getEtat());
        journalModel.setDate(journal.getDate());
        journalModel.setDateValeurCG(journal.getDateValeurCG());
        journalModel.setTypeJournal(journal.getTypeJournal());
        journalModel.setSpy(journal.getSpy().getFullData());

        return journalModel;
    }

    private OrdreVersementComplexModel parse(CAOperationOrdreVersement ordreVersement) throws OsirisException {

        if (ordreVersement == null) {
            throw new OsirisException("Unable to parse ordreVersement, the entity ordreVersement is null");
        }

        if (ordreVersement.isNew()) {
            return new OrdreVersementComplexModel();
        }

        OrdreVersementComplexModel ordreVersementModel = new OrdreVersementComplexModel();
        ordreVersementModel.setId(ordreVersement.getIdOperation());
        ordreVersementModel.getOperation().setIdCompteAnnexe(ordreVersement.getIdCompteAnnexe());
        ordreVersementModel.getOperation().setIdJournal(ordreVersement.getIdJournal());
        ordreVersementModel.getOperation().setIdSection(ordreVersement.getIdSection());
        ordreVersementModel.getOperation().setDate(ordreVersement.getDate());
        ordreVersementModel.getOperation().setMontant(ordreVersement.getMontant());
        ordreVersementModel.getOperation().setCodeDebitCredit(ordreVersement.getCodeDebitCredit());
        ordreVersementModel.getOrdre().setIdAdressePaiement(ordreVersement.getIdAdressePaiement());
        ordreVersementModel.getOrdre().setCodeISOMonnaieBonification(ordreVersement.getCodeISOMonnaieBonification());
        ordreVersementModel.getOrdre().setCodeISOMonnaieDepot(ordreVersement.getCodeISOMonnaieDepot());
        ordreVersementModel.getOrdre().setTypeVirement(ordreVersement.getTypeVirement());
        ordreVersementModel.getOrdre().setNatureOrdre(ordreVersement.getNatureOrdre());
        ordreVersementModel.getOrdre().setMotif(ordreVersement.getMotif());

        ordreVersementModel.setSpy(ordreVersement.getSpy().getFullData());

        return ordreVersementModel;

    }

    /**
     * Parser d'un model d'écriture en une entité opération
     * 
     * @param journalModel
     * @return
     */
    private CAEcriture parse(EcritureSimpleModel ecritureModel, boolean useCaisseProf) {

        CAEcriture ecriture = new CAEcriture();
        ecriture.setIdOperation(ecritureModel.getIdOperation());
        ecriture.setIdJournal(ecritureModel.getIdJournal());
        ecriture.setLibelle(ecritureModel.getLibelle());
        ecriture.setIdTypeOperation(ecritureModel.getIdTypeOperation());
        ecriture.setIdCompteAnnexe(ecritureModel.getIdCompteAnnexe());
        ecriture.setIdSection(ecritureModel.getIdSection());
        ecriture.setDate(ecritureModel.getDate());
        ecriture.setIdCompte(ecritureModel.getIdCompte());
        ecriture.setMontant(ecritureModel.getMontant());
        ecriture.setCodeDebitCredit(ecritureModel.getCodeDebitCredit());
        ecriture.setAnneeCotisation(ecritureModel.getAnnee());
        ecriture.setMasse(ecritureModel.getMasse());
        ecriture.setTaux(ecritureModel.getTaux());
        if (useCaisseProf) {
            ecriture.setIdCaisseProfessionnelle(ecritureModel.getIdCaisseProf());
        }

        ecriture.populateSpy(ecritureModel.getSpy());

        return ecriture;
    }

    /**
     * Parser d'un model de journal en une entité journal
     * 
     * @param journalModel
     * @return
     */
    private CAJournal parse(JournalSimpleModel journalModel) {

        CAJournal journal = new CAJournal();

        journal.setIdJournal(journalModel.getIdJournal());
        journal.setLibelle(journalModel.getLibelle());
        journal.setDate(journalModel.getDate());
        journal.setDateValeurCG(journalModel.getDateValeurCG());
        journal.setTypeJournal(journalModel.getTypeJournal());
        journal.setProprietaire(journal.getProprietaire());
        journal.setEtat(journalModel.getEtat());
        journal.populateSpy(journalModel.getSpy());

        return journal;
    }
}
