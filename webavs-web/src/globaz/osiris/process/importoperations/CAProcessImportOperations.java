/*
 * Créé le Sep 30, 2005, dda
 */
package globaz.osiris.process.importoperations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.xml.JadeXmlReader;
import globaz.jade.client.xml.JadeXmlReaderException;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.process.APIProcessUpload;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAAuxiliaire;
import globaz.osiris.db.comptes.CAAuxiliairePaiement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAEcritureCompensation;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrement;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import globaz.osiris.db.comptes.CAPaiement;
import globaz.osiris.db.comptes.CAPaiementBVR;
import globaz.osiris.process.journal.CAProcessComptabiliserJournal;
import globaz.osiris.translation.CACodeSystem;
import java.io.FileInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author dda
 */
public class CAProcessImportOperations extends BProcess implements APIProcessUpload {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_IMPORT_ET_COMPTABILISATION_OPERATIONS_ERROR = "IMPORT_ET_COMPTABILISATION_OPERATIONS_ERROR";
    private static final String LABEL_IMPORT_ET_COMPTABILISATION_OPERATIONS_OK = "IMPORT_ET_COMPTABILISATION_OPERATIONS_OK";

    private static final String LABEL_IMPORT_OPERATIONS_ERROR = "IMPORT_OPERATIONS_ERROR";
    private static final String LABEL_IMPORT_OPERATIONS_OK = "IMPORT_OPERATIONS_OK";

    private static final String LABEL_LIBELLE_JOURNAL_NON_RENSEIGNE = "7014";

    private String dateTraitement;

    /**
     * contain a fully qualified filename for example : C:\temp\test.txt
     */
    private String fileName;

    /* pour lancement par ligne de commande via GlobazCommandLineJob */
    private Boolean isBatch = new Boolean(false);

    private CAJournal journal = null;

    private String libelleJournal;
    private String remoteDirectoryATraiter = "";

    private String remoteDirectoryTraiteKo = "";

    private String remoteDirectoryTraiteOk = "";

    /**
     * Constructor for CAProcessImportOperations.
     */
    public CAProcessImportOperations() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor for CAProcessImportOperations.
     * 
     * @param parent
     */
    public CAProcessImportOperations(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CAProcessImportOperations.
     * 
     * @param session
     */
    public CAProcessImportOperations(BSession session) throws Exception {
        super(session);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {

        if (JadeStringUtil.isBlank(getLibelleJournal())) {
            this._addError(getTransaction(),
                    getSession().getLabel(CAProcessImportOperations.LABEL_LIBELLE_JOURNAL_NON_RENSEIGNE));
            return false;
        }

        if (isFileNameBlank()) {
            this._addError(getTransaction(), getSession().getLabel("FILE_NOT_FOUND"));
            return false;
        }

        boolean successfulExecution = true;

        try {
            successfulExecution = executeImportations(getSession(), getTransaction(), getFileName());
        } catch (Exception e) {
            this._addError(getTransaction(), e.toString());
            successfulExecution = false;
        }

        if (isBatch) {
            if (isAborted() || isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
                successfulExecution = false;
            }

            String remoteDirectoryForFichierTraite = getRemoteDirectoryTraiteKo();
            if (successfulExecution) {
                remoteDirectoryForFichierTraite = getRemoteDirectoryTraiteOk();
            }

            try {
                // Copie du fichier traité vers le ftp (répertoire différent en fonction de successfulExecution)
                JadeFsFacade.copyFile(fileName,
                        remoteDirectoryForFichierTraite + "/" + JadeFilenameUtil.extractFilename(fileName));

                // Suppression du fichier traité sur le ftp (répertoire contenant les fichiers à traiter) et en local
                JadeFsFacade.delete(getRemoteDirectoryATraiter() + "/" + JadeFilenameUtil.extractFilename(fileName));
                JadeFsFacade.delete(fileName);
            } catch (Exception e) {
                this._addError(
                        getTransaction(),
                        getSession().getLabel("IMPORT_ET_COMPTABILISATION_OPERATIONS_TECHNICAL_ERROR") + " : "
                                + e.toString());
                successfulExecution = false;
            }

            if (successfulExecution && (journal != null) && !journal.isNew()) {
                CAProcessComptabiliserJournal processComptabiliserJournal = new CAProcessComptabiliserJournal();
                processComptabiliserJournal.setParentWithCopy(this);
                processComptabiliserJournal.setSession(getSession());
                processComptabiliserJournal.setEMailAddress(getEMailAddress());
                processComptabiliserJournal.setIdJournal(journal.getIdJournal());
                processComptabiliserJournal.setImprimerJournal(new Boolean(false));
                processComptabiliserJournal.executeProcess();

                // il est important de recharger le journal car il a été modifié par la comptabilisation
                // la lecture doit se faire même si la transaction est en erreurs
                journal.retrieve(false);
            }

            if (isAborted() || isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
                successfulExecution = false;
            }

        }

        return successfulExecution;

    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isBlank(getLibelleJournal())) {
            this._addError(getTransaction(),
                    getSession().getLabel(CAProcessImportOperations.LABEL_LIBELLE_JOURNAL_NON_RENSEIGNE));
        }

        if (isFileNameBlank()) {
            this._addError(getTransaction(), getSession().getLabel("FILE_NOT_FOUND"));
        }
    }

    /**
     * Ajout des nouvelles opérations auxiliaires et paiement auxiliaires.
     * 
     * @param session
     * @param transaction
     * @param doc
     * @param journal
     * @throws Exception
     */
    private void addAuxiliaires(BSession session, BTransaction transaction, Document doc) throws Exception {
        NodeList elements = CAImportOperationsUtils.getAuxiliaires(doc);

        for (int i = 0; (i < elements.getLength()) && !isAborted(); i++) {
            try {
                CAAuxiliaire newAuxiliaire = createAuxiliaire(session, transaction, (Element) elements.item(i));
                validerOperation(transaction, newAuxiliaire);
                newAuxiliaire.add(transaction);

                confirmTransaction(transaction);
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());

                transaction.rollback();
                transaction.clearErrorBuffer();
            }
        }

        elements = CAImportOperationsUtils.getPaiementAuxiliaires(doc);

        for (int i = 0; (i < elements.getLength()) && !isAborted(); i++) {
            try {
                CAAuxiliairePaiement newAuxiliairePaiement = new CAAuxiliairePaiement(createAuxiliaire(session,
                        transaction, (Element) elements.item(i)));
                validerOperation(transaction, newAuxiliairePaiement);
                newAuxiliairePaiement.add(transaction);

                confirmTransaction(transaction);
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());

                transaction.rollback();
                transaction.clearErrorBuffer();
            }
        }
    }

    /**
     * Ajout des nouvelles écritures.
     * 
     * @param session
     * @param transaction
     * @param doc
     * @param journal
     * @throws Exception
     */
    private void addEcritures(BSession session, BTransaction transaction, Document doc) throws Exception {
        NodeList elements = CAImportOperationsUtils.getEcritures(doc);

        for (int i = 0; (i < elements.getLength()) && !isAborted(); i++) {
            try {
                CAEcriture newEcriture = createEcriture(session, transaction, (Element) elements.item(i));
                validerOperation(transaction, newEcriture);
                newEcriture.add(transaction);

                confirmTransaction(transaction);
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());

                transaction.rollback();
                transaction.clearErrorBuffer();
            }
        }
    }

    /**
     * Ajout des nouvelles écritures compensation.
     * 
     * @param session
     * @param transaction
     * @param doc
     * @param journal
     * @throws Exception
     */
    private void addEcrituresCompensation(BSession session, BTransaction transaction, Document doc) throws Exception {
        NodeList elements = CAImportOperationsUtils.getCompensations(doc);

        for (int i = 0; (i < elements.getLength()) && !isAborted(); i++) {
            try {
                CAEcritureCompensation newEcriture = createCompensation(session, transaction,
                        (Element) elements.item(i));
                validerOperation(transaction, newEcriture);

                if (JadeStringUtil.isIntegerEmpty(newEcriture.getIdSection())) {
                    newEcriture.setEtat(APIOperation.ETAT_ERREUR);
                }

                newEcriture.add(transaction);

                confirmTransaction(transaction);
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());

                transaction.rollback();
                transaction.clearErrorBuffer();
            }
        }
    }

    /**
     * Ajout des nouvaux ordres de versement.
     * 
     * @param session
     * @param transaction
     * @param doc
     * @param journal
     * @throws Exception
     */
    private void addOrdresVersement(BSession session, BTransaction transaction, Document doc) throws Exception {
        NodeList elements = CAImportOperationsUtils.getOrdresVersement(doc);

        for (int i = 0; (i < elements.getLength()) && !isAborted(); i++) {
            try {
                CAOperationOrdreVersement newOpOrdreVersement = createOperationOrdreVersement(session, transaction,
                        (Element) elements.item(i));
                validerOperation(transaction, newOpOrdreVersement);
                newOpOrdreVersement.add(transaction);

                confirmTransaction(transaction);
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());

                transaction.rollback();
                transaction.clearErrorBuffer();
            }
        }
    }

    /**
     * Ajout des nouveaux paiements.
     * 
     * @param session
     * @param transaction
     * @param doc
     * @param journal
     * @throws Exception
     */
    private void addPaiements(BSession session, BTransaction transaction, Document doc) throws Exception {
        NodeList elements = CAImportOperationsUtils.getPaiements(doc);

        for (int i = 0; (i < elements.getLength()) && !isAborted(); i++) {
            try {
                CAPaiement newPaiement = createPaiement(session, transaction, (Element) elements.item(i));
                validerOperation(transaction, newPaiement);
                newPaiement.add(transaction);

                confirmTransaction(transaction);
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());

                transaction.rollback();
                transaction.clearErrorBuffer();
            }
        }
    }

    /**
     * Ajouts des nouveaux paiements BVR.
     * 
     * @param session
     * @param transaction
     * @param doc
     * @param journal
     * @throws Exception
     */
    private void addPaiementsBVR(BSession session, BTransaction transaction, Document doc) throws Exception {
        NodeList elements = CAImportOperationsUtils.getPaiementsBVR(doc);

        for (int i = 0; (i < elements.getLength()) && !isAborted(); i++) {
            try {
                CAPaiementBVR newPaiement = createPaiementBVR(session, transaction, (Element) elements.item(i));
                validerOperation(transaction, newPaiement);
                newPaiement.add(transaction);

                confirmTransaction(transaction);
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());

                transaction.rollback();
                transaction.clearErrorBuffer();
            }
        }
    }

    /**
     * Ajout des nouvaux recouvrements.
     * 
     * @param session
     * @param transaction
     * @param doc
     * @param journal
     * @throws Exception
     */
    private void addRecouvrements(BSession session, BTransaction transaction, Document doc) throws Exception {
        NodeList elements = CAImportOperationsUtils.getRecouvrements(doc);

        for (int i = 0; (i < elements.getLength()) && !isAborted(); i++) {
            try {
                CAOperationOrdreRecouvrement newRecouvrement = createRecouvrement(session, transaction,
                        (Element) elements.item(i));
                validerOperation(transaction, newRecouvrement);
                newRecouvrement.add(transaction);

                confirmTransaction(transaction);
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());

                transaction.rollback();
                transaction.clearErrorBuffer();
            }
        }
    }

    /**
     * Si la transaction contient aucunes erreurs => commit.<br/>
     * Sinon rollback + sauvegarde des erreurs dans le memory log.
     * 
     * @param transaction
     * @throws Exception
     */
    private void confirmTransaction(BTransaction transaction) throws Exception {
        if (transaction.hasErrors()) {
            getMemoryLog().logStringBuffer(transaction.getErrors(), this.getClass().getName());

            transaction.rollback();
            transaction.clearErrorBuffer();
        } else {
            transaction.commit();
        }
    }

    /**
     * Créé une nouvelle opération auxiliaire.
     * 
     * @param session
     * @param transaction
     * @param e
     * @param journal
     * @return
     * @throws Exception
     */
    private CAAuxiliaire createAuxiliaire(BSession session, BTransaction transaction, Element e) throws Exception {
        CAAuxiliaire newAuxiliaire = new CAAuxiliaire();
        newAuxiliaire.setSession(session);
        newAuxiliaire.setIdJournal(journal.getIdJournal());

        newAuxiliaire.setIdCompteAnnexe(CAImportOperationsUtils.getIdCompteAnnexe(session, transaction, e, journal,
                CACodeSystem.COMPTE_AUXILIAIRE));
        newAuxiliaire.setIdSection(CAImportOperationsUtils.getIdSection(session, transaction, e,
                newAuxiliaire.getIdCompteAnnexe(), journal));
        newAuxiliaire.setDate(CAImportOperationsUtils.getDate(e, journal));

        newAuxiliaire.setMontant(CAImportOperationsUtils.getMontant(e));

        newAuxiliaire.setLibelle(CAImportOperationsUtils.getLibelle(e));
        newAuxiliaire.setPiece(CAImportOperationsUtils.getPieceComptable(e));

        newAuxiliaire.setCodeDebitCredit(CAImportOperationsUtils.getCodeDebitCredit(session, e));

        return newAuxiliaire;
    }

    /**
     * Créer une écriture de compensation.
     * 
     * @param session
     * @param e
     * @param journal
     * @return
     * @throws Exception
     */
    private CAEcritureCompensation createCompensation(BSession session, BTransaction transaction, Element e)
            throws Exception {
        CAEcritureCompensation newEcriture = new CAEcritureCompensation();
        newEcriture.setSession(session);
        newEcriture.setIdJournal(journal.getIdJournal());

        newEcriture.setIdCompteAnnexe(CAImportOperationsUtils.getIdCompteAnnexe(session, transaction, e, journal,
                CACompteAnnexe.GENRE_COMPTE_STANDARD));
        newEcriture.setIdSection(CAImportOperationsUtils.getIdSectionCompensation(session, transaction, e,
                newEcriture.getIdCompteAnnexe(), journal));
        newEcriture.setDate(CAImportOperationsUtils.getDate(e, journal));

        newEcriture
                .setIdCompte(CAImportOperationsUtils.getIdRubriqueCompensation(session, e, newEcriture.getSection()));

        newEcriture.setMontant(CAImportOperationsUtils.getMontant(e));
        newEcriture.setCodeDebitCredit(CAImportOperationsUtils.getCodeDebitCredit(session, e));

        return newEcriture;
    }

    /**
     * Créer l'écriture en fonction de l'élément xml.
     * 
     * @param session
     * @param transaction
     * @param e
     * @param journal
     * @return
     * @throws Exception
     */
    private CAEcriture createEcriture(BSession session, BTransaction transaction, Element e) throws Exception {
        CAEcriture newEcriture = new CAEcriture();
        newEcriture.setSession(session);
        newEcriture.setIdJournal(journal.getIdJournal());

        newEcriture.setIdCompteAnnexe(CAImportOperationsUtils.getIdCompteAnnexe(session, transaction, e, journal,
                CACompteAnnexe.GENRE_COMPTE_STANDARD));
        newEcriture.setIdSection(CAImportOperationsUtils.getIdSection(session, transaction, e,
                newEcriture.getIdCompteAnnexe(), journal));
        newEcriture.setDate(CAImportOperationsUtils.getDate(e, journal));
        newEcriture.setIdCompte(CAImportOperationsUtils.getIdRubrique(session, e));
        newEcriture.setAnneeCotisation(CAImportOperationsUtils.getAnneeCotisation(e));
        newEcriture.setIdCaisseProfessionnelle(CAImportOperationsUtils.getIdCaisseProfessionnelle(session, e));
        newEcriture.setMasse(CAImportOperationsUtils.getMasse(e));
        newEcriture.setTaux(CAImportOperationsUtils.getTaux(e));
        newEcriture.setMontant(CAImportOperationsUtils.getMontant(e));

        newEcriture.setLibelle(CAImportOperationsUtils.getLibelle(e));
        newEcriture.setPiece(CAImportOperationsUtils.getPieceComptable(e));

        newEcriture.setCodeDebitCredit(CAImportOperationsUtils.getCodeDebitCredit(session, e));
        newEcriture.setIdCompteCourant(CAImportOperationsUtils.getIdCompteCourant(session, e));

        return newEcriture;
    }

    /**
     * Créer le journal qui contiendra les nouvelles opérations.
     * 
     * @param session
     * @param transaction
     * @param doc
     * @return
     * @throws Exception
     */
    private void createJournal(BSession session, BTransaction transaction, Document doc) throws Exception {

        journal = new CAJournal();
        journal.setSession(session);

        journal.setLibelle(getLibelleJournal());

        if (JadeStringUtil.isBlank(getDateTraitement())) {
            journal.setDateValeurCG(JACalendar.todayJJsMMsAAAA());
            journal.setDate(JACalendar.todayJJsMMsAAAA());
        } else {
            journal.setDateValeurCG(getDateTraitement());
            journal.setDate(JACalendar.todayJJsMMsAAAA());
        }

        journal.add(transaction);

        if (transaction.hasErrors()) {
            transaction.rollback();
        } else {
            transaction.commit();
        }

    }

    /**
     * Créer l'ordre de versement en fonction de l'élément xml.
     * 
     * @param session
     * @param transaction
     * @param e
     * @param journal
     * @return
     * @throws Exception
     */
    private CAOperationOrdreVersement createOperationOrdreVersement(BSession session, BTransaction transaction,
            Element e) throws Exception {
        CAOperationOrdreVersement newOpOrdreVersement = new CAOperationOrdreVersement();
        newOpOrdreVersement.setSession(session);
        newOpOrdreVersement.setIdJournal(journal.getIdJournal());

        newOpOrdreVersement.setIdCompteAnnexe(CAImportOperationsUtils.getIdCompteAnnexe(session, transaction, e,
                journal, CACompteAnnexe.GENRE_COMPTE_STANDARD));
        newOpOrdreVersement.setIdSection(CAImportOperationsUtils.getIdSection(session, transaction, e,
                newOpOrdreVersement.getIdCompteAnnexe(), journal));
        newOpOrdreVersement.setDate(CAImportOperationsUtils.getDate(e, journal));
        newOpOrdreVersement.setMontant(CAImportOperationsUtils.getMontant(e));

        newOpOrdreVersement.setTypeVirement(CAImportOperationsUtils.getTypeVirement(e));
        newOpOrdreVersement.setCodeISOMonnaieDepot(CAImportOperationsUtils.getCodeISOMonnaieDepot(e));
        newOpOrdreVersement.setCodeISOMonnaieBonification(CAImportOperationsUtils.getCodeISOMonnaieBonification(e));
        newOpOrdreVersement.setNatureOrdre(CAImportOperationsUtils.getNatureOrdre(e));
        newOpOrdreVersement.setReferenceBVR(CAImportOperationsUtils.getReferenceBVR(e));
        newOpOrdreVersement.setIdOrganeExecution(CAImportOperationsUtils.getIdOrganeExecution(e));
        newOpOrdreVersement.setIdAdressePaiement(CAImportOperationsUtils.getIdAdressePaiement(e));

        newOpOrdreVersement.setMotif(CAImportOperationsUtils.getMotif(e));
        newOpOrdreVersement.setEstBloque(CAImportOperationsUtils.isBloque(e));

        return newOpOrdreVersement;
    }

    /**
     * Créer le nouvau paiement en fonction de l'élément xml.
     * 
     * @param session
     * @param transaction
     * @param e
     * @param journal
     * @return
     * @throws Exception
     */
    private CAPaiement createPaiement(BSession session, BTransaction transaction, Element e) throws Exception {
        CAPaiement newPaiement = new CAPaiement();
        newPaiement.setSession(session);
        newPaiement.setIdJournal(journal.getIdJournal());

        newPaiement.setIdCompteAnnexe(CAImportOperationsUtils.getIdCompteAnnexe(session, transaction, e, journal,
                CACompteAnnexe.GENRE_COMPTE_STANDARD));
        newPaiement.setIdSection(CAImportOperationsUtils.getIdSection(session, transaction, e,
                newPaiement.getIdCompteAnnexe(), journal));
        newPaiement.setDate(CAImportOperationsUtils.getDate(e, journal));
        newPaiement.setIdCompte(CAImportOperationsUtils.getIdRubrique(session, e));
        newPaiement.setMontant(CAImportOperationsUtils.getMontant(e));
        newPaiement.setCodeDebitCredit(CAImportOperationsUtils.getCodeDebitCredit(session, e));

        newPaiement.setLibelle(CAImportOperationsUtils.getLibelle(e));
        newPaiement.setPiece(CAImportOperationsUtils.getPieceComptable(e));
        newPaiement.setIdCompteCourant(CAImportOperationsUtils.getIdCompteCourant(session, e));

        return newPaiement;
    }

    /**
     * Créer un nouveau paiementBVR.
     * 
     * @param session
     * @param transaction
     * @param e
     * @param journal
     * @return
     * @throws Exception
     */
    private CAPaiementBVR createPaiementBVR(BSession session, BTransaction transaction, Element e) throws Exception {
        CAPaiement paiement = createPaiement(session, transaction, CAImportOperationsUtils.getFirstPaiement(e));

        CAPaiementBVR newPaiementBVR = new CAPaiementBVR(paiement);
        newPaiementBVR.setSession(session);
        newPaiementBVR.setIdJournal(journal.getIdJournal());

        newPaiementBVR.setGenreTransaction(CAImportOperationsUtils.getGenreTransaction(e));
        newPaiementBVR.setReferenceBVR(CAImportOperationsUtils.getReferenceBVR(e));
        newPaiementBVR.setReferenceInterne(CAImportOperationsUtils.getReferenceInterne(e));
        newPaiementBVR.setDateTraitement(CAImportOperationsUtils.getDateTraitement(e));
        newPaiementBVR.setDateDepot(CAImportOperationsUtils.getDateDepot(e));
        newPaiementBVR.setDateInscription(CAImportOperationsUtils.getDateInscription(e));

        newPaiementBVR.setIdOrganeExecution(CAImportOperationsUtils.getIdOrganeExecution(e));

        return newPaiementBVR;
    }

    /**
     * Créer un recouvrement en fonction de l'élément xml.
     * 
     * @param session
     * @param transaction
     * @param e
     * @param journal
     * @return
     * @throws Exception
     */
    private CAOperationOrdreRecouvrement createRecouvrement(BSession session, BTransaction transaction, Element e)
            throws Exception {
        CAOperationOrdreRecouvrement newRecouvrement = new CAOperationOrdreRecouvrement();
        newRecouvrement.setSession(session);
        newRecouvrement.setIdJournal(journal.getIdJournal());

        newRecouvrement.setIdCompteAnnexe(CAImportOperationsUtils.getIdCompteAnnexe(session, transaction, e, journal,
                CACompteAnnexe.GENRE_COMPTE_STANDARD));
        newRecouvrement.setIdSection(CAImportOperationsUtils.getIdSection(session, transaction, e,
                newRecouvrement.getIdCompteAnnexe(), journal));
        newRecouvrement.setDate(CAImportOperationsUtils.getDate(e, journal));
        newRecouvrement.setMontant(CAImportOperationsUtils.getMontant(e));
        newRecouvrement.setCodeISOMonnaieBonification(CAImportOperationsUtils.getCodeISOMonnaieBonification(e));

        newRecouvrement.setNatureOrdre(CAImportOperationsUtils.getNatureOrdre(e));
        newRecouvrement.setReferenceBVR(CAImportOperationsUtils.getReferenceBVR(e));
        newRecouvrement.setIdOrganeExecution(CAImportOperationsUtils.getIdOrganeExecution(e));

        newRecouvrement.setMotif(CAImportOperationsUtils.getMotif(e));
        newRecouvrement.setEstBloque(CAImportOperationsUtils.isBloque(e));

        return newRecouvrement;
    }

    /**
     * @param session
     * @param transaction
     * @param sourceFile
     * @return
     * @throws Exception
     */
    private boolean executeImportations(BSession session, BTransaction transaction, String sourceFile) throws Exception {
        setProgressScaleValue(9);

        Document doc = getDocument(sourceFile);

        incProgressCounter();
        if (isAborted()) {
            return false;
        }

        createJournal(session, transaction, doc);

        incProgressCounter();

        if (!journal.hasErrors() && !journal.isNew() && !isAborted()) {
            addEcritures(session, transaction, doc);

            incProgressCounter();
            if (isAborted()) {
                return false;
            }
            addEcrituresCompensation(session, transaction, doc);

            incProgressCounter();
            if (isAborted()) {
                return false;
            }
            addOrdresVersement(session, transaction, doc);

            incProgressCounter();
            if (isAborted()) {
                return false;
            }
            addPaiements(session, transaction, doc);

            incProgressCounter();
            if (isAborted()) {
                return false;
            }
            addPaiementsBVR(session, transaction, doc);

            incProgressCounter();
            if (isAborted()) {
                return false;
            }
            addAuxiliaires(session, transaction, doc);

            incProgressCounter();
            if (isAborted()) {
                return false;
            }
            addRecouvrements(session, transaction, doc);

            incProgressCounter();
        }

        if (isAborted()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return
     */
    public String getDateTraitement() {
        return dateTraitement;
    }

    /**
     * @param sourceFileName
     * @return
     * @throws JadeXmlReaderException
     */
    private Document getDocument(String sourceFileName) throws Exception {
        String filename = "";
        if (!isBatch.booleanValue()) {
            JadeFsFacade.copyFile("jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + sourceFileName, Jade
                    .getInstance().getHomeDir() + "work/" + sourceFileName);
            filename = Jade.getInstance().getHomeDir() + "work/" + sourceFileName;
        } else {
            filename = sourceFileName;
        }
        return JadeXmlReader.parseFile(new FileInputStream(filename));
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {

        String theLabel = "";
        boolean isProcessOnError = isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors();

        if (isBatch) {

            theLabel = getSession().getLabel(CAProcessImportOperations.LABEL_IMPORT_ET_COMPTABILISATION_OPERATIONS_OK);

            if (isProcessOnError || (journal == null) || !CAJournal.COMPTABILISE.equalsIgnoreCase(journal.getEtat())) {
                theLabel = getSession().getLabel(
                        CAProcessImportOperations.LABEL_IMPORT_ET_COMPTABILISATION_OPERATIONS_ERROR);
            }

        } else {

            theLabel = getSession().getLabel(CAProcessImportOperations.LABEL_IMPORT_OPERATIONS_OK);
            if (isProcessOnError) {
                theLabel = getSession().getLabel(CAProcessImportOperations.LABEL_IMPORT_OPERATIONS_ERROR);
            }

        }

        return FWMessageFormat.format(theLabel, JadeFilenameUtil.extractFilename(fileName));

    }

    /**
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    public Boolean getIsBatch() {
        return isBatch;
    }

    /**
     * @return
     */
    public String getLibelleJournal() {
        return libelleJournal;
    }

    public String getRemoteDirectoryATraiter() {
        return remoteDirectoryATraiter;
    }

    public String getRemoteDirectoryTraiteKo() {
        return remoteDirectoryTraiteKo;
    }

    public String getRemoteDirectoryTraiteOk() {
        return remoteDirectoryTraiteOk;
    }

    /**
     * Le nom de fichier est-il vide ?
     * 
     * @return
     */
    private boolean isFileNameBlank() {
        return JadeStringUtil.isBlank(getFileName());
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * @param string
     */
    public void setDateTraitement(String s) {
        dateTraitement = s;
    }

    /**
     * @param string
     */
    @Override
    public void setFileName(String s) {
        fileName = s;
    }

    public void setIsBatch(Boolean isBatch) {
        this.isBatch = isBatch;
    }

    /**
     * @param string
     */
    public void setLibelleJournal(String s) {
        libelleJournal = s;
    }

    public void setRemoteDirectoryATraiter(String remoteDirectoryATraiter) {
        this.remoteDirectoryATraiter = remoteDirectoryATraiter;
    }

    public void setRemoteDirectoryTraiteKo(String remoteDirectoryTraiteKo) {
        this.remoteDirectoryTraiteKo = remoteDirectoryTraiteKo;
    }

    public void setRemoteDirectoryTraiteOk(String remoteDirectoryTraiteOk) {
        this.remoteDirectoryTraiteOk = remoteDirectoryTraiteOk;
    }

    /**
     * Valide l'opération. Si la validation contient des erreurs l'état de l'opération est setter en erreur.
     * 
     * @param transaction
     * @param operation
     */
    private void validerOperation(BTransaction transaction, CAOperation operation) {
        operation.valider(transaction);
        if (operation.getMemoryLog().hasErrors()) {
            operation.setEtat(APIOperation.ETAT_ERREUR);
            getMemoryLog().logMessage(operation.getMemoryLog());
        }
    }
}
