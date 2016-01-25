package globaz.osiris.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.retours.CALignesRetours;
import globaz.osiris.db.retours.CALignesRetoursListViewBean;
import globaz.osiris.db.retours.CALignesRetoursViewBean;
import globaz.osiris.db.retours.CALotsRetours;
import globaz.osiris.db.retours.CALotsRetoursManager;
import globaz.osiris.db.retours.CARetours;
import globaz.osiris.db.retours.CARetoursJointLotsRetoursManager;
import globaz.osiris.db.retours.CARetoursManager;
import globaz.osiris.db.retours.CARetoursViewBean;
import globaz.osiris.externe.CAGestionComptabiliteExterne;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Iterator;

/**
 * Insérez la description du type ici. Date de création : (25.02.2002 13:41:13)
 * 
 * @author: bsc
 */
public class CAProcessComptabiliserRetours extends BProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CAJournal journal = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Commentaire relatif au constructeur CAProcessComptabiliserRetours.
     */
    public CAProcessComptabiliserRetours() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CAProcessComptabiliserRetours(BProcess parent) {
        super(parent);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {

        FWMemoryLog comptaMemoryLog = null;
        FWMemoryLog comptaBisMemoryLog = null;
        boolean noErrorBeforeClose = false;

        // Sous controle d'exceptions
        try {

            // /////////////////////////////////////////////////////////////////////////////
            // Phase 1
            // comptabilisation des retours des lots
            // /////////////////////////////////////////////////////////////////////////////
            CALotsRetoursManager lrManager = new CALotsRetoursManager();
            lrManager.setSession(getSession());
            lrManager.setForCsEtatLot(CALotsRetours.CS_ETAT_LOT_OUVERT);
            lrManager.changeManagerSize(BManager.SIZE_NOLIMIT);
            lrManager.find(getTransaction());

            getMemoryLog().logMessage(
                    getSession().getLabel("INFO_PROCESS_COMPTABILISER_COMPTABILISATION_LOTS_RETOURS"),
                    FWMessage.INFORMATION, this.getClass().getName());

            // pour tous les lots ouvert
            Iterator lotIter = lrManager.iterator();
            while (lotIter.hasNext()) {
                CALotsRetours lot = (CALotsRetours) lotIter.next();

                // controle du total du lot et du total des retours du lot
                CARetoursManager rManager = new CARetoursManager();
                rManager.setSession(getSession());
                rManager.setForIdLot(lot.getIdLot());
                rManager.changeManagerSize(BManager.SIZE_NOLIMIT);
                BigDecimal totalRetoursLot = rManager.getSum(CARetours.FIELDNAME_MONTANT_RETOUR, getTransaction());

                // si le total des retours du lot et le montant total du lot ne
                // sont pas identiques
                // on refus le lot
                if (totalRetoursLot.compareTo(new BigDecimal(lot.getMontantTotal())) != 0) {

                    getMemoryLog().logMessage(
                            FWMessageFormat.format(getSession().getLabel("ERROR_PROCESS_COMPTABILISER_MONTANTS"),
                                    lot.getIdLot() + " - " + lot.getLibelleLot(), new FWCurrency(lot.getMontantTotal())
                                            .toStringFormat(), new FWCurrency(totalRetoursLot.doubleValue())
                                            .toStringFormat()), FWMessage.INFORMATION, this.getClass().getName());
                    continue;
                }

                // init. de la compta
                CAGestionComptabiliteExterne compta = new CAGestionComptabiliteExterne(this);
                compta.setSession(getSession());
                compta.setLibelle(lot.getLibelleLot());
                compta.setDateValeur(lot.getDateLot());

                comptaMemoryLog = new FWMemoryLog();
                compta.setMessageLog(comptaMemoryLog);

                journal = (CAJournal) compta.createJournal();

                // ///////////////////////////////////////////////////////////////////////
                // traitement des retours du lot
                // ///////////////////////////////////////////////////////////////////////
                rManager.find(getTransaction());

                Iterator retoursIter = rManager.iterator();
                while (retoursIter.hasNext()) {
                    CARetours retour = (CARetours) retoursIter.next();

                    // la ref. rubrique est dans le lot
                    CARubrique rubrique = new CARubrique();
                    rubrique.setSession(getSession());
                    rubrique.setIdRubrique(lot.getIdCompteFinancier());
                    rubrique.retrieve(getTransaction());

                    APIEcriture ecriture = compta.createEcriture();

                    FWCurrency montant = new FWCurrency(retour.getMontantRetour());
                    boolean positif = true;
                    if (montant.isNegative()) {
                        montant.negate();
                        positif = false;
                    }

                    ecriture.setIdCompteAnnexe(retour.getIdCompteAnnexe());
                    // Bug 6227
                    APISection section = compta.getSectionByIdExterne(retour.getIdCompteAnnexe(),
                            APISection.ID_TYPE_SECTION_RETOUR, "" + JACalendar.getYear(retour.getDateRetour())
                                    + APISection.CATEGORIE_SECTION_RETOUR + "000");
                    ecriture.setIdSection(section.getIdSection());

                    ecriture.setDate(lot.getDateLot());
                    // idCompte = idRubrique... si
                    ecriture.setIdCompte(rubrique.getIdRubrique());
                    ecriture.setMontant(montant.toString());

                    if (positif) {
                        ecriture.setCodeDebitCredit(APIEcriture.CREDIT);
                    } else {
                        ecriture.setCodeDebitCredit(APIEcriture.DEBIT);
                    }

                    // la description de l'ecriture doit etre celle du lot
                    ecriture.setLibelle(lot.getLibelleLot());

                    compta.addOperation(ecriture);

                    getMemoryLog().logMessage(
                            java.text.MessageFormat.format(getSession()
                                    .getLabel("INFO_PROCESS_COMPTABILISATION_RETOUR"),
                                    new Object[] { retour.getMontantRetour(), rubrique.getIdExterne() }),
                            FWMessage.INFORMATION, this.getClass().getName());

                    // mise a jour du retour
                    retour.setCsEtatRetour(CARetours.CS_ETAT_RETOUR_SUSPENS);
                    retour.update(getTransaction());
                }

                FWMemoryLog beforeCloseComptaMemoryLog = new FWMemoryLog();
                // si pas d'erreurs avant le close, on sauvegarde les messages
                // du comptaMemoryLog
                // pour les restaurer si une erreure survient durant le close
                if (!comptaMemoryLog.hasErrors()) {
                    noErrorBeforeClose = true;
                    beforeCloseComptaMemoryLog.logMessage(comptaMemoryLog);

                    // on memorise l'id du journal en CA pour pouvoir rediriger
                    // sur celui-ci depuis
                    // lêcran de detail du lot
                    lot.setIdJournalCA(journal.getIdJournal());
                    // mise a jour du lot de retour
                    lot.setCsEtatLot(CALotsRetours.CS_ETAT_LOT_COMPTABILISE);
                    lot.update(getTransaction());
                }

                if (!compta.isJournalEmpty()) {
                    compta.comptabiliser();
                }

                // si pas d'erreurs avant le close et en erreur après le close,
                // on restaure l'ancien
                // memory log pour masquer l'exception.
                // Elle sera directement traitee dans la compta.
                if (noErrorBeforeClose && comptaMemoryLog.hasErrors()) {
                    comptaMemoryLog = beforeCloseComptaMemoryLog;
                }

                // le traitement de ce lot est termine, on commit
                if (noErrorBeforeClose) {
                    getTransaction().commit();
                }

                getMemoryLog().logMessage(
                        FWMessageFormat.format(getSession().getLabel("INFO_PROCESS_COMPTABILISER_LOT_OK"),
                                lot.getIdLot() + " - " + lot.getLibelleLot()), FWMessage.INFORMATION,
                        this.getClass().getName());
            }

            // /////////////////////////////////////////////////////////////////////////////
            // Phase 2
            // traitement des lignes de retour
            // /////////////////////////////////////////////////////////////////////////////

            CARetoursJointLotsRetoursManager rjlrManager = new CARetoursJointLotsRetoursManager();
            rjlrManager.setSession(getSession());
            rjlrManager.setMontantRetourEqualSommeMontantsLignes(Boolean.TRUE);
            rjlrManager.setForCsEtatLot(CALotsRetours.CS_ETAT_LOT_COMPTABILISE);
            rjlrManager.setForCsEtatRetour(CARetours.CS_ETAT_RETOUR_TRAITE);
            rjlrManager.changeManagerSize(BManager.SIZE_NOLIMIT);
            rjlrManager.find(getTransaction());

            getMemoryLog().logMessage(getSession().getLabel("INFO_PROCESS_COMPTABILISER_LIQUIDATION_RETOURS"),
                    FWMessage.INFORMATION, this.getClass().getName());

            if (rjlrManager.size() > 0) {
                // init. de la compta
                CAGestionComptabiliteExterne comptaBis = new CAGestionComptabiliteExterne(this);
                comptaBis.setSession(getSession());
                comptaBis.setLibelle(FWMessageFormat.format(getSession()
                        .getLabel("LIBELLE_PROCESS_LIQUIDATION_RETOURS"), JACalendar.todayJJsMMsAAAA()));
                comptaBis.setDateValeur(JACalendar.todayJJsMMsAAAA());
                comptaBisMemoryLog = new FWMemoryLog();
                comptaBis.setMessageLog(comptaBisMemoryLog);

                // un seul journal pour la liquidation de tous les retours meme
                // si pas dans le meme lot de retour
                journal = (CAJournal) comptaBis.createJournal();

                // pour tout les retour qui peuvent etre liquides
                Iterator retoursIter = rjlrManager.iterator();
                while (retoursIter.hasNext()) {
                    CARetoursViewBean retour = (CARetoursViewBean) retoursIter.next();

                    // on cherche le lot du retour
                    CALotsRetours lot = new CALotsRetours();
                    lot.setSession(getSession());
                    lot.setIdLot(retour.getIdLot());
                    lot.reserve(getTransaction());

                    // la ref. rubrique est dans le lot
                    CARubrique rubrique = new CARubrique();
                    rubrique.setSession(getSession());
                    rubrique.setIdRubrique(lot.getIdCompteFinancier());
                    rubrique.retrieve(getTransaction());

                    // on cherche les lignes de retour du retour
                    CALignesRetoursListViewBean lirManager = new CALignesRetoursListViewBean();
                    lirManager.setSession(getSession());
                    lirManager.setForIdRetour(retour.getIdRetour());
                    lirManager.changeManagerSize(BManager.SIZE_NOLIMIT);
                    lirManager.find(getTransaction());

                    // pour toutes les lignes de retours
                    Iterator lirIter = lirManager.iterator();
                    while (lirIter.hasNext()) {
                        CALignesRetoursViewBean lir = (CALignesRetoursViewBean) lirIter.next();

                        if (CALignesRetours.CS_ETAT_LIGNE_REPAIEMENT.equals(lir.getCsType())) {

                            // /////////////////////////////////////////////////////////////////////////////
                            // repaiement -> OV
                            // /////////////////////////////////////////////////////////////////////////////
                            APIOperationOrdreVersement ordreVersement = comptaBis.createOperationOrdreVersement();

                            ordreVersement.setIdAdressePaiement(lir
                                    .getIdAvoirPaiementUniqueLigneRetourSurAdressePaiementAdresse());
                            ordreVersement.setDate(JACalendar.todayJJsMMsAAAA());
                            ordreVersement.setIdCompteAnnexe(retour.getIdCompteAnnexe());

                            // Bugzilla 4992: la section de retour ne doit pas etre retrouvee avec l'annee de la date du
                            // jour, mais avec l'annee de la date de retour
                            APISection section = comptaBis.getSectionByIdExterne(retour.getIdCompteAnnexe(),
                                    APISection.ID_TYPE_SECTION_RETOUR, "" + JACalendar.getYear(retour.getDateRetour())
                                    /* JACalendar.today().getYear() */+ APISection.CATEGORIE_SECTION_RETOUR + "000");
                            ordreVersement.setIdSection(section.getIdSection());

                            ordreVersement.setMontant(lir.getMontant());
                            ordreVersement.setCodeISOMonnaieBonification(getSession().getCode("215001"));
                            ordreVersement.setCodeISOMonnaieDepot(getSession().getCode("215001"));
                            ordreVersement.setTypeVirement(APIOperationOrdreVersement.VIREMENT);

                            // si la nature de l'ordre n'est pas donnee dans le
                            // retour,
                            // on prends la nature par defaut du lot
                            if (JadeStringUtil.isIntegerEmpty(retour.getCsNatureOrdre())) {
                                ordreVersement.setNatureOrdre(lot.getCsNatureOrdre());
                            } else {
                                ordreVersement.setNatureOrdre(retour.getCsNatureOrdre());
                            }

                            String motif = "";
                            // pour les rentes on ajoute le NSS
                            if (CAOrdreGroupe.NATURE_RENTES_AVS_AI.equals(ordreVersement.getNatureOrdre())) {
                                CACompteAnnexe ca = retour.getCompteAnnexe();
                                motif = ca.getIdExterneRole() + " ";
                            }
                            motif = motif + getSession().getCodeLibelle(retour.getCsMotifRetour());
                            if (!JadeStringUtil.isEmpty(retour.getLibelleRetour())) {
                                motif += " - " + retour.getLibelleRetour();
                            }

                            // tester motif vide. Bug 5780
                            if (!JadeStringUtil.isBlank(motif)) {
                                ordreVersement.setMotif(JadeStringUtil.substring(motif, 0, 80));
                            }

                            getMemoryLog()
                                    .logMessage(
                                            MessageFormat
                                                    .format(getSession().getLabel("INFO_PROCESS_COMPTABILISATION_OV"),
                                                            new Object[] {
                                                                    lir.getMontant().toString(),
                                                                    lir.getIdAvoirPaiementUniqueLigneRetourSurAdressePaiementAdresse() }),
                                            FWMessage.INFORMATION, this.getClass().getName());

                            comptaBis.addOperation(ordreVersement);

                        } else {

                            // /////////////////////////////////////////////////////////////////////////////
                            // compensation -> 2 ecriture
                            // 1) section de retour
                            // 2) section compensee
                            // /////////////////////////////////////////////////////////////////////////////

                            // section de retour
                            APIEcriture ecritureSectionRetour = comptaBis.createEcriture();
                            ecritureSectionRetour.setIdCompteAnnexe(retour.getIdCompteAnnexe());

                            // Bugzilla 4992: la section de retour ne doit pas etre retrouvee avec l'annee de la date du
                            // jour, mais avec l'annee de la date de retour
                            APISection sectionRetour = comptaBis.getSectionByIdExterne(retour.getIdCompteAnnexe(),
                                    APISection.ID_TYPE_SECTION_RETOUR, "" + JACalendar.getYear(retour.getDateRetour())
                                    /* JACalendar.today().getYear() */+ APISection.CATEGORIE_SECTION_RETOUR + "000");

                            // Ici, on utilisle la rubrique de compensation
                            APIRubrique rubriqueCompensation = null;
                            CAReferenceRubrique ref = new CAReferenceRubrique();
                            ref.setSession(journal.getSession());
                            rubriqueCompensation = ref
                                    .getRubriqueByCodeReference(APIReferenceRubrique.RUBRIQUE_DE_LISSAGE);

                            ecritureSectionRetour.setIdSection(sectionRetour.getIdSection());
                            ecritureSectionRetour.setDate(JACalendar.todayJJsMMsAAAA());
                            if (rubriqueCompensation != null) {
                                ecritureSectionRetour.setIdCompte(rubriqueCompensation.getIdRubrique());
                            } else {
                                getTransaction().addErrors(
                                        journal.getSession().getLabel("CODE_REFERENCE_NON_ATTRIBUE")
                                                + APIReferenceRubrique.RUBRIQUE_DE_LISSAGE);
                            }
                            ecritureSectionRetour.setMontant(lir.getMontant());
                            ecritureSectionRetour.setCodeDebitCredit(APIEcriture.DEBIT);

                            // la description de l'ecriture doit etre celle du
                            // lot
                            ecritureSectionRetour.setLibelle(lot.getLibelleLot());

                            // section compensee
                            APIEcriture ecritureSectionCompensee = comptaBis.createEcriture();
                            ecritureSectionCompensee.setIdCompteAnnexe(retour.getIdCompteAnnexe());
                            ecritureSectionCompensee.setIdSection(lir.getIdSection());
                            ecritureSectionCompensee.setDate(JACalendar.todayJJsMMsAAAA());
                            if (rubriqueCompensation != null) {
                                ecritureSectionCompensee.setIdCompte(rubriqueCompensation.getIdRubrique());
                            } else {
                                getTransaction().addErrors(
                                        journal.getSession().getLabel("CODE_REFERENCE_NON_ATTRIBUE")
                                                + APIReferenceRubrique.RUBRIQUE_DE_LISSAGE);
                            }
                            ecritureSectionCompensee.setMontant(lir.getMontant());
                            ecritureSectionCompensee.setCodeDebitCredit(APIEcriture.CREDIT);

                            // la description de l'ecriture doit etre celle du
                            // lot
                            ecritureSectionCompensee.setLibelle(lot.getLibelleLot());

                            // ajout des ecritures
                            comptaBis.addOperation(ecritureSectionRetour);
                            comptaBis.addOperation(ecritureSectionCompensee);

                            getMemoryLog().logMessage(
                                    MessageFormat.format(
                                            getSession().getLabel("INFO_PROCESS_COMPTABILISATION_COMPENSATION"),
                                            new Object[] { lir.getMontant().toString(), lir.getIdSection() }),
                                    FWMessage.INFORMATION, this.getClass().getName());
                        }

                        // on memorise l'id du journal en CA pour pouvoir
                        // rediriger sur celui-ci depuis
                        // l'ecran de detail des retours
                        retour.setIdJournal(journal.getIdJournal());
                        retour.setCsEtatRetour(CARetours.CS_ETAT_RETOUR_LIQUIDE);
                        retour.update(getTransaction());

                        // si il n'y a plus de retour EN_SUSPENS ou TRAITE dans le lot, le
                        // lot passe LIQUIDE
                        CARetoursJointLotsRetoursManager retoursOuvertsManager = new CARetoursJointLotsRetoursManager();
                        retoursOuvertsManager.setSession(getSession());
                        retoursOuvertsManager.setForIdLot(lot.getIdLot());
                        retoursOuvertsManager.setForCsEtatRetourIn(CARetours.CS_ETAT_RETOUR_SUSPENS + ","
                                + CARetours.CS_ETAT_RETOUR_TRAITE);
                        int nbRetourOuvert = retoursOuvertsManager.getCount(getTransaction());
                        if (nbRetourOuvert == 0) {
                            lot.setCsEtatLot(CALotsRetours.CS_ETAT_LOT_LIQUIDE);
                            lot.update(getTransaction());
                        }
                    }
                }

                FWMemoryLog beforeCloseComptaMemoryLog = new FWMemoryLog();
                // si pas d'erreurs avant le close, on sauvegarde les messages
                // du comptaMemoryLog
                // pour les restaurer si une erreure survient durant le close
                if (!comptaBisMemoryLog.hasErrors()) {
                    noErrorBeforeClose = true;
                    beforeCloseComptaMemoryLog.logMessage(comptaBisMemoryLog);
                }

                if (!comptaBis.isJournalEmpty()) {
                    comptaBis.comptabiliser();
                }

                // si pas d'erreurs avant le close et en erreur après le close,
                // on restaure l'ancien
                // memory log pour masquer l'exception.
                // Elle sera directement traitee dans la compta.
                if (noErrorBeforeClose && comptaBisMemoryLog.hasErrors()) {
                    comptaBisMemoryLog = beforeCloseComptaMemoryLog;
                }

                // le traitement de ce lot est termine, on commit
                if (noErrorBeforeClose) {
                    getTransaction().commit();
                }

            }

        } catch (Exception e) {
            // si l'exception survient durant le close -> noErrorBeforeClose ==
            // true, l'exception n'est pas remontee
            // Elle sera directement traitee dans la compta.
            if (!noErrorBeforeClose) {
                getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR,
                        getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));
                e.printStackTrace();

                return false;
            }
        } finally {
            if ((comptaMemoryLog != null) && (comptaMemoryLog.size() > 0)) {

                getMemoryLog()
                        .logMessage("", FWMessage.INFORMATION,
                                ":::::::::::: START LOG OSIRIS (COMPTABILISATION DES LOTS DE RETOURS) :::::::::::::::::::::::::::::");

                for (int i = 0; i < comptaMemoryLog.size(); i++) {
                    getMemoryLog()
                            .logMessage(null, comptaMemoryLog.getMessage(i).getComplement(),
                                    comptaMemoryLog.getMessage(i).getTypeMessage(),
                                    comptaMemoryLog.getMessage(i).getIdSource());

                }
                getMemoryLog()
                        .logMessage("", FWMessage.INFORMATION,
                                ":::::::::::: END LOG OSIRIS (COMPTABILISATION DES LOTS DE RETOURS):::::::::::::::::::::::::::::");
            }

            if ((comptaBisMemoryLog != null) && (comptaBisMemoryLog.size() > 0)) {

                getMemoryLog().logMessage("", FWMessage.INFORMATION,
                        ":::::::::::: START LOG OSIRIS (LIQUIDATION DES RETOURS) :::::::::::::::::::::::::::::");

                for (int i = 0; i < comptaBisMemoryLog.size(); i++) {
                    getMemoryLog().logMessage(null, comptaBisMemoryLog.getMessage(i).getComplement(),
                            comptaBisMemoryLog.getMessage(i).getTypeMessage(),
                            comptaBisMemoryLog.getMessage(i).getIdSource());

                }
                getMemoryLog().logMessage("", FWMessage.INFORMATION,
                        ":::::::::::: END LOG OSIRIS (LIQUIDATION DES RETOURS):::::::::::::::::::::::::::::");
            }
        }

        // Fin de la procédure
        return true;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return String
     */
    @Override
    protected String getEMailObject() {

        // Déterminer l'objet du message en fonction du code erreur
        String obj;

        if (getMemoryLog().hasErrors()) {
            obj = getSession().getLabel("MAIL_PROCESS_COMPTABILISER_KO");
        } else {
            obj = getSession().getLabel("MAIL_PROCESS_COMPTABILISER_OK");
        }

        // Restituer l'objet
        return obj;

    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }
}
