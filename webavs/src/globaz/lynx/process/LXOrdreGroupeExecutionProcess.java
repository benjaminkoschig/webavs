package globaz.lynx.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.lynx.application.LXApplication;
import globaz.lynx.db.fournisseur.LXFournisseur;
import globaz.lynx.db.journal.LXJournal;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationManager;
import globaz.lynx.db.ordregroupe.LXOrdreGroupe;
import globaz.lynx.db.ordreversement.LXOrdreVersement;
import globaz.lynx.db.ordreversement.LXOrdreVersementManager;
import globaz.lynx.db.organeexecution.LXOrganeExecution;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.helpers.utils.LXHelperUtils;
import globaz.lynx.helpers.utils.LXPaiementUtils;
import globaz.lynx.service.tiers.LXTiersService;
import globaz.osiris.db.ordres.format.CAOrdreFormateur;
import globaz.osiris.db.ordres.format.CAProcessFormatOrdreDTAFournisseur;
import globaz.osiris.db.ordres.format.CAProcessFormatOrdreOPAE;
import globaz.osiris.db.ordres.utils.CAOrdreGroupeFtpUtils;
import globaz.osiris.process.journal.CAUtilsJournal;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class LXOrdreGroupeExecutionProcess extends LXOrdreGroupeProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String DESTINATION_FILENAME = "pttcria";
    private static final String WORK_DIRECTORY = "/work/";
    private LXJournal journal = null;

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            if (isAborted()) {
                return false;
            }

            if (JadeStringUtil.isIntegerEmpty(getOrdreGroupe().getIdJournalLie())) {
                createJournal();
                updateIdJournalLie();
            }

            if (isAborted()) {
                return false;
            }

            if (executeOrdreGroupe()) {
                attachEscomptesToJournal();

                if (isAborted()) {
                    return false;
                }

                updateEtatOrdreGroupeGenere();

                if (isAborted()) {
                    return false;
                }

                comptabiliserJournal();

                if (isAborted()) {
                    return false;
                }

                imprimer();

                addMailInformations();
            }

        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("EXECUTION_ERREUR"));

            if (!JadeStringUtil.isBlank(e.getMessage())) {
                this._addError(getTransaction(), e.getMessage());
            } else {
                this._addError(getTransaction(), e.toString());
            }

            return false;
        }

        return true;
    }

    /**
     * @see BProcess#_validate() throws Exception
     */
    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isIntegerEmpty(getIdSociete())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
            return;
        }

        if (JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_ORDREGROUPE"));
            return;
        }

        if (!LXOrdreGroupe.CS_ETAT_PREPARE.equals(getOrdreGroupe().getCsEtat())
                && !LXOrdreGroupe.CS_ETAT_GENERE.equals(getOrdreGroupe().getCsEtat())) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_ORDREGROUPE_ETAT_PREPARE_GENERE"));
            return;
        }

        if (!new CAUtilsJournal().isPeriodeComptableOuverte(getSession(), getTransaction(), getOrdreGroupe()
                .getDateEcheance(), getSociete().getIdMandat())) {
            return;
        }
    }

    /**
     * Ajoute des informations dans l'email.
     */
    private void addMailInformations() throws Exception {
        getMemoryLog().logMessage(getSession().getLabel("TOTAL_PAIEMENTS") + "" + getOrdreGroupe().getTotalPaiement(),
                FWMessage.INFORMATION, this.getClass().getName());
        getMemoryLog().logMessage(getSession().getLabel("TOTAL_ESCOMPTES") + "" + getOrdreGroupe().getTotalEscompte(),
                FWMessage.INFORMATION, this.getClass().getName());
    }

    /**
     * Si le fournisseur est bloqué, le versement doit être annulé.
     * 
     * @param versement
     * @throws Exception
     */
    private void annuleVersement(LXOrdreVersement versement) throws Exception {
        versement.setCsEtatOperation(LXOperation.CS_ETAT_ANNULE);
        updateVersement(versement);
    }

    /**
     * Attache les escomptes au journal.
     * 
     * @throws Exception
     */
    private void attachEscomptesToJournal() throws Exception {
        BTransaction readTransaction = null;

        try {
            readTransaction = (BTransaction) getSession().newTransaction();
            readTransaction.openTransaction();

            LXOperationManager manager = getListeEscomptes(readTransaction);

            setProgressScaleValue(manager.size() + (int) (manager.size() * 0.20));

            for (int i = 0; (i < manager.size()) && !isAborted(); i++) {
                LXOperation escompte = (LXOperation) manager.get(i);

                LXSection tmpSection = LXHelperUtils
                        .getSection(getSession(), getTransaction(), escompte.getIdSection());
                LXFournisseur tmpFournisseur = LXHelperUtils.getFournisseur(getSession(), getTransaction(),
                        tmpSection.getIdFournisseur());

                if (!tmpFournisseur.isEstBloque().booleanValue() && !escompte.getEstBloque().booleanValue()) {
                    updateEscompte(escompte);
                } else {
                    escompte.setCsEtatOperation(LXOperation.CS_ETAT_ANNULE);
                    updateEscompte(escompte);
                }

                incProgressCounter();
            }

        } catch (Exception e) {
            getTransaction().rollback();
            throw e;
        } finally {
            if (readTransaction != null) {
                try {
                    readTransaction.rollback();
                } finally {
                    readTransaction.closeTransaction();
                }
            }
        }
    }

    /**
     * Si le formateur est banque ou poste, le terminé en y ajoutant la fin du fichier. <br/>
     * Si banque l'attaché au mail <br/>
     * Si poste, ftp si actif sinon attaché au mail
     * 
     * @param fileName
     * @param of
     * @param nombreTransaction
     * @throws Exception
     * @throws IOException
     */
    private void closeAndRegisterFormateur(String fileName, CAOrdreFormateur of, int nombreTransaction)
            throws Exception {
        if (of != null) {
            // Si l'ordre groupé contient un seul paiement bloqué, aucun ordre
            // ne sera contenu dans le fichier
            if (nombreTransaction > 0) {
                of.formatEOF(getOrdreGroupe());
            }

            of.getPrintWriter().close();

            // Si l'ordre groupé contient un seul paiement bloqué, aucun ordre
            // ne sera contenu dans le fichier
            if (nombreTransaction > 0) {
                CAOrdreGroupeFtpUtils.sendOrRegisterFile(this, getOrdreGroupe(), getOrganeExecution(), fileName);

                updateDateTransmissionOrdre();
            }
        }
    }

    /**
     * Comptabilise le journal qui contient les paiements et les escomptes afin de les ajouter en comptabilité générale.
     * 
     * @throws Exception
     */
    private void comptabiliserJournal() throws Exception {
        if (!LXJournal.CS_ETAT_COMPTABILISE.equals(getJournal().getCsEtat())) {
            LXJournalComptabiliserProcess comptabiliser = new LXJournalComptabiliserProcess();
            comptabiliser.setParent(this);
            comptabiliser.setSession(getSession());

            comptabiliser.setSendCompletionMail(false);

            comptabiliser.setIdJournal(getJournal().getIdJournal());
            comptabiliser.setIdSociete(getJournal().getIdSociete());

            comptabiliser.executeProcess();
        }
    }

    /**
     * Créer le journal de destination afin de comptabiliser les paiements grâce au batch de comptabilisation.
     * 
     * @throws Exception
     */
    private void createJournal() throws Exception {
        if (journal == null) {
            journal = new LXJournal();
            journal.setSession(getSession());

            journal.setIdSociete(getOrdreGroupe().getIdSociete());
            journal.setLibelle(getOrdreGroupe().getLibelle());
            journal.setDateCreation(JACalendar.todayJJsMMsAAAA());

            if (JadeDateUtil.isGlobazDate(getOrdreGroupe().getDatePaiement())) {
                journal.setDateValeurCG(getOrdreGroupe().getDatePaiement());
            } else {
                journal.setDateValeurCG(getOrdreGroupe().getDateEcheance());
            }
            journal.setIdOrdreGroupeSrc(getOrdreGroupe().getIdOrdreGroupe());

            journal.add(getTransaction());

            if (getTransaction().hasErrors() || getSession().hasErrors()) {
                getTransaction().rollback();
                throw new Exception(getSession().getLabel("IMPOSSIBLE_CREER_JOURNAL"));
            } else {
                getTransaction().commit();
            }
        }

    }

    /**
     * Exécute l'ordre groupé. <br/>
     * - Retrouve les paiements et affecte l'id adresse de paiement si nécessaire <br/>
     * - Créer le fichier pour la poste ou l'ubs <br/>
     * - Comptabilisation des mouvements en comptabilité générale <br/>
     * - Mise à jour état <br/>
     * - Impresssion
     * 
     * @throws Exception
     * @return True si au moins un paiement candidat a été effectué
     */
    private boolean executeOrdreGroupe() throws Exception {
        BTransaction readTransaction = null;
        int countVersementEffectue = 0;

        try {
            readTransaction = (BTransaction) getSession().newTransaction();
            readTransaction.openTransaction();

            String fileName = Jade.getInstance().getHomeDir() + LXApplication.DEFAULT_LYNX_ROOT
                    + LXOrdreGroupeExecutionProcess.WORK_DIRECTORY + LXOrdreGroupeExecutionProcess.DESTINATION_FILENAME;

            CAOrdreFormateur of = initFormateur(fileName);

            LXOrdreVersementManager manager = getListePaiements(readTransaction);

            setProgressScaleValue(manager.size() + (int) (manager.size() * 0.20));
            countVersementEffectue = manager.size();

            int numeroTransaction = 0;

            for (int i = 0; (i < manager.size()) && !isAborted(); i++) {
                LXOrdreVersement versement = (LXOrdreVersement) manager.get(i);

                if (!versement.getFournisseur().isEstBloque().booleanValue()
                        && !versement.getEstBloque().booleanValue()) {
                    // Le montant doit être annoncé en positif
                    FWCurrency tmpMontant = new FWCurrency(versement.getMontant());
                    FWCurrency tmpMontantMonnaie = new FWCurrency(versement.getMontantMonnaie());

                    if (tmpMontant.isNegative()) {
                        // Si of est null, donc nous sommes en mode CAISSE ou
                        // LSV
                        if (of != null) {
                            setVersementIdAdressePaiement(versement);

                            negateMontantVersement(versement, tmpMontant, tmpMontantMonnaie);

                            numeroTransaction = incrementNumeroTransaction(numeroTransaction, versement);

                            of.format(versement);

                            if (getMemoryLog().hasErrors() || getSession().hasErrors()) {
                                // Throw not needed, errors already presents in
                                // history
                                return false;
                            }

                            negateMontantVersement(versement, tmpMontant, tmpMontantMonnaie);
                        }

                        updateVersement(versement);

                        updateEtatFactureBaseSolde(versement);

                    }
                } else {
                    annuleVersement(versement);
                }

                incProgressCounter();

                countVersementEffectue++;

            }

            closeAndRegisterFormateur(fileName, of, numeroTransaction);

        } catch (Exception e) {
            getTransaction().rollback();
            throw e;
        } finally {
            if (readTransaction != null) {
                try {
                    readTransaction.rollback();
                } finally {
                    readTransaction.closeTransaction();
                }
            }
        }

        return countVersementEffectue > 0;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("EXECUTION_ERREUR");
        } else {
            return getSession().getLabel("EXECUTION_OK");
        }
    }

    /**
     * Retrouve et return le journal
     * 
     * @return
     * @throws Exception
     */
    private LXJournal getJournal() throws Exception {
        if (journal == null) {
            if (!JadeStringUtil.isIntegerEmpty(getOrdreGroupe().getIdJournalLie())) {
                journal = new LXJournal();
                journal.setSession(getSession());

                journal.setIdJournal(getOrdreGroupe().getIdJournalLie());

                journal.retrieve(getTransaction());

                if (journal.hasErrors()) {
                    throw new Exception(journal.getErrors().toString());
                }

                if (journal.isNew()) {
                    throw new Exception(getSession().getLabel("VALIDATE_JOURNAL_INCONNU"));
                }
            } else {
                throw new Exception(getSession().getLabel("VAL_IDENTIFIANT_JOURNAL"));
            }
        }

        return journal;
    }

    /**
     * Return la liste des escomptes liées à l'ordre groupé.
     * 
     * @param transaction
     * @return
     * @throws Exception
     */
    private LXOperationManager getListeEscomptes(BTransaction transaction) throws Exception {
        LXOperationManager opeManager = new LXOperationManager();
        opeManager.setSession(getSession());

        opeManager.setForIdOrdreGroupe(getIdOrdreGroupe());

        ArrayList<String> forCsTypeOperationIn = new ArrayList<String>();
        forCsTypeOperationIn.add(LXOperation.CS_TYPE_ESCOMPTE);
        opeManager.setForIdTypeOperationIn(forCsTypeOperationIn);

        opeManager.setForJournalBlank(new Boolean(true));

        ArrayList<String> listeEtat = new ArrayList<String>();
        listeEtat.add(LXOperation.CS_ETAT_OUVERT);
        listeEtat.add(LXOperation.CS_ETAT_TRAITEMENT);
        opeManager.setForCsEtatIn(listeEtat);

        opeManager.find(transaction, BManager.SIZE_NOLIMIT);

        return opeManager;
    }

    /**
     * Recherche la liste des paiements à effectuer
     * 
     * @param transaction
     */
    private LXOrdreVersementManager getListePaiements(BTransaction transaction) throws Exception {
        LXOrdreVersementManager opeManager = new LXOrdreVersementManager();
        opeManager.setSession(getSession());

        opeManager.setForIdOrdreGroupe(getIdOrdreGroupe());

        ArrayList<String> forCsTypeOperationIn = new ArrayList<String>();

        if (LXOrganeExecution.CS_GENRE_CAISSE.equals(getOrganeExecution().getCsGenre())) {
            forCsTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_CAISSE);
        } else if (LXOrganeExecution.CS_GENRE_LSV.equals(getOrganeExecution().getCsGenre())) {
            forCsTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_LSV);
        } else {
            forCsTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_BVR_ORANGE);
            forCsTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_BVR_ROUGE);
            forCsTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_VIREMENT);
        }

        opeManager.setForCsTypeOperationIn(forCsTypeOperationIn);

        // Sauf état ANNULE, tout les paiements doivent être introduit dans le
        // fichier postal ou banquaire
        ArrayList<String> listeEtat = new ArrayList<String>();
        listeEtat.add(LXOperation.CS_ETAT_OUVERT);
        listeEtat.add(LXOperation.CS_ETAT_TRAITEMENT);
        listeEtat.add(LXOperation.CS_ETAT_COMPTABILISE);
        opeManager.setForCsEtatIn(listeEtat);

        opeManager.find(transaction, BManager.SIZE_NOLIMIT);

        return opeManager;
    }

    /**
     * Imprime le journal de l'ordre groupé.
     * 
     * @throws Exception
     */
    private void imprimer() throws Exception {
        LXOrdreGroupeImprimerProcess imprimer = new LXOrdreGroupeImprimerProcess();
        imprimer.setParent(this);
        imprimer.setSession(getSession());

        imprimer.setSendCompletionMail(false);

        imprimer.setIdOrdreGroupe(getOrdreGroupe().getIdOrdreGroupe());
        imprimer.setIdSociete(getJournal().getIdSociete());
        imprimer.setIdOrganeExecution(getOrganeExecution().getIdOrganeExecution());

        imprimer.executeProcess();
    }

    /**
     * Incrémente le numéro de transaction. Au cas ou déjà renseigné retourne le numéro trouvé.
     * 
     * @param numeroTransaction
     * @param versement
     * @return
     * @throws Exception
     */
    private int incrementNumeroTransaction(int numeroTransaction, LXOrdreVersement versement) throws Exception {
        if (JadeStringUtil.isDecimalEmpty(versement.getNumeroTransaction())) {
            numeroTransaction++;
            versement.setNumeroTransaction("" + numeroTransaction);

            return numeroTransaction;
        } else {
            return Integer.parseInt(versement.getNumeroTransaction());
        }
    }

    /**
     * Return le formateur initialisé du fichier a généré.
     * 
     * @param fileName
     * @return Si l'organe d'exécution n'est pas poste ou banque alors renvois null.
     * @throws Exception
     */
    private CAOrdreFormateur initFormateur(String fileName) throws Exception {
        CAOrdreFormateur of = null;

        if (LXOrganeExecution.CS_GENRE_BANQUE.equals(getOrganeExecution().getCsGenre())) {
            of = new CAProcessFormatOrdreDTAFournisseur();
        } else if (LXOrganeExecution.CS_GENRE_POSTE.equals(getOrganeExecution().getCsGenre())) {
            of = new CAProcessFormatOrdreOPAE();
        } else {
            return null;
        }

        of.setSession(getSession());

        of.setMemoryLog(getMemoryLog());

        of.setPrintWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "ISO8859_1")));

        of.formatHeader(getOrdreGroupe());

        return of;
    }

    /**
     * L'ordre de versement (le fichier) doit être créé avec un montant positif à exécuter.
     * 
     * @param versement
     * @param tmpMontant
     * @param tmpMontantMonnaie
     * @return
     */
    private void negateMontantVersement(LXOrdreVersement versement, FWCurrency tmpMontant, FWCurrency tmpMontantMonnaie) {
        tmpMontant.negate();
        versement.setMontant(tmpMontant.toString());

        tmpMontantMonnaie.negate();
        versement.setMontantMonnaie(tmpMontantMonnaie.toString());
    }

    /**
     * Si l'adresse de paiement n'est pas encore sélectionniée, la sélectionnée et sauvegarder en DB.
     * 
     * @param versement
     * @throws Exception
     */
    private void setVersementIdAdressePaiement(LXOrdreVersement versement) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(versement.getIdAdressePaiement())) {
            TIAdressePaiementData adressePaiement = LXTiersService.getAdresseFournisseurPaiementAsData(getSession(),
                    getTransaction(), versement.getFournisseur().getIdTiers(), getOrdreGroupe().getDateEcheance());

            if (adressePaiement == null) {
                throw new Exception(getSession().getLabel("IMPOSSIBLE_ADRESSE_PAIEMENT") + " - "
                        + versement.getFournisseur().getIdExterne() + " " + versement.getFournisseur().getNomComplet());
            }

            versement.setIdAdressePaiement(adressePaiement.getIdAdressePaiement());

            versement.update(getTransaction());

            if (getTransaction().hasErrors() || getSession().hasErrors()) {
                getTransaction().rollback();
                throw new Exception(getSession().getLabel("IMPOSSIBLE_ADRESSE_PAIEMENT") + " - "
                        + versement.getFournisseur().getIdExterne() + " " + versement.getFournisseur().getNomComplet());
            } else {
                getTransaction().commit();
            }
        }
    }

    /**
     * Mise à jour de la date de tranmission de l'ordre groupe
     * 
     * @throws Exception
     */
    private void updateDateTransmissionOrdre() throws Exception {
        if (!LXOrdreGroupe.CS_ETAT_GENERE.equals(getOrdreGroupe().getCsEtat())) {
            LXOrdreGroupe ordreGroupe = getOrdreGroupe();

            ordreGroupe.setDateTransmission(JACalendar.todayJJsMMsAAAA());

            ordreGroupe.update(getTransaction());

            if (ordreGroupe.hasErrors() || getTransaction().hasErrors()) {
                getTransaction().rollback();
                throw new Exception(getSession().getLabel("MAJ_ORDREGROUPE_IMPOSSIBLE"));
            } else {
                getTransaction().commit();
            }
        }
    }

    /**
     * Rattache l'escompte au journal et la met à jour.
     * 
     * @param escompte
     * @throws Exception
     */
    private void updateEscompte(LXOperation escompte) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(escompte.getIdJournal())) {
            escompte.setIdJournal(getJournal().getIdJournal());

            escompte.update(getTransaction());

            if (getTransaction().hasErrors() || getSession().hasErrors()) {
                getTransaction().rollback();
                throw new Exception(getSession().getLabel("UPDATE_IMPOSSIBLE_ESCOMPTE"));
            } else {
                getTransaction().commit();
            }
        }
    }

    /**
     * Mise à jour de la facture de base à l'état SOLDE si le total du paiement, des paiements et des escomptes
     * précédents est égal au montant de la facture de base.
     * 
     * @param versement
     * @throws Exception
     */
    private void updateEtatFactureBaseSolde(LXOrdreVersement versement) throws Exception {
        if (LXOperation.CS_ETAT_OUVERT.equals(versement.getCsEtatOperation())) {
            LXOperation factureBase = LXPaiementUtils.getFactureBase(getSession(), getTransaction(),
                    versement.getIdOperationSrc());
            FWCurrency base = new FWCurrency(factureBase.getMontant());

            LXOperationManager otherManager = LXPaiementUtils.getOtherPaiementEscompte(getSession(),
                    versement.getIdOperationSrc(), versement.getIdOperation());

            FWCurrency pmtAndEscompte = new FWCurrency(otherManager.getSum(LXOperation.FIELD_MONTANT, getTransaction())
                    .toString());
            pmtAndEscompte.add(versement.getMontant());

            if (pmtAndEscompte.isZero() || otherManager.hasErrors()) {
                throw new Exception(getSession().getLabel("SOMME_PAIEMENTS_ESCOMPTE_ZERO"));
            }

            pmtAndEscompte.negate();

            if (pmtAndEscompte.compareTo(base) > 0) {
                throw new Exception(getSession().getLabel("SOMME_PAIEMENTS_ESCOMPTE_FACTURE"));
            } else if (pmtAndEscompte.compareTo(base) == 0) {
                factureBase.setCsEtatOperation(LXOperation.CS_ETAT_SOLDE);
                factureBase.update(getTransaction());

                if (factureBase.hasErrors()) {
                    throw new Exception(factureBase.getErrors().toString());
                }
            }
        }
    }

    /**
     * Permet le changement d'etat de l'ordre groupe
     * 
     * @throws Exception
     */
    private void updateEtatOrdreGroupeGenere() throws Exception {
        if (!LXOrdreGroupe.CS_ETAT_GENERE.equals(getOrdreGroupe().getCsEtat())) {
            LXOrdreGroupe ordreGroupe = getOrdreGroupe();

            ordreGroupe.setCsEtat(LXOrdreGroupe.CS_ETAT_GENERE);

            ordreGroupe.update(getTransaction());

            if (ordreGroupe.hasErrors() || getTransaction().hasErrors()) {
                getTransaction().rollback();
                throw new Exception(getSession().getLabel("MAJ_ORDREGROUPE_IMPOSSIBLE"));
            } else {
                getTransaction().commit();
            }
        }
    }

    /**
     * Mise à jour de l'idJournaLie de l'ordre groupé.
     * 
     * @throws Exception
     */
    private void updateIdJournalLie() throws Exception {
        LXOrdreGroupe ordreGroupe = getOrdreGroupe();

        ordreGroupe.setIdJournalLie(getJournal().getIdJournal());
        ordreGroupe.update(getTransaction());

        if (ordreGroupe.hasErrors() || getTransaction().hasErrors()) {
            getTransaction().rollback();
            throw new Exception(getSession().getLabel("MAJ_ORDREGROUPE_IMPOSSIBLE"));
        } else {
            getTransaction().commit();
        }
    }

    /**
     * Mise à jour du versement en base de données. <br/>
     * Rattache au journal généré. <br/>
     * Grâce au commit, également sauvegarde du numéro de transaction.
     * 
     * @param versement
     * @throws Exception
     */
    private void updateVersement(LXOrdreVersement versement) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(versement.getIdJournal())) {
            versement.setIdJournal(getJournal().getIdJournal());

            versement.update(getTransaction());

            if (getTransaction().hasErrors() || getSession().hasErrors()) {
                getTransaction().rollback();
                throw new Exception(getSession().getLabel("UPDATE_IMPOSSIBLE"));
            } else {
                getTransaction().commit();
            }
        }
    }

}
