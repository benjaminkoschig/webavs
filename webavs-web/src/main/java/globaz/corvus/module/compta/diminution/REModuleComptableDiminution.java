/**
 * Traitement des diminutions de rentes accordées.
 * 
 * @author : scr
 */
package globaz.corvus.module.compta.diminution;

import globaz.corvus.api.basescalcul.IREFactureARestituer;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.diminution.IREDiminution;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.api.recap.IRERecap;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.REFactureARestituer;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.helpers.process.REDebloquerMontantRAHandler;
import globaz.corvus.module.compta.AREModuleComptable;
import globaz.corvus.module.compta.REModuleComptableFactory;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.beneficiaire.principal.REBeneficiairePrincipal;
import globaz.corvus.utils.beneficiaire.principal.REBeneficiairePrincipalVO;
import globaz.corvus.utils.compta.REComptaUtil;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIJournal;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.external.IntRole;
import globaz.osiris.utils.CAUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRSession;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description Génère les écritures comptable suite à la diminution d'une rente accordée
 * 
 * @author scr
 */
public class REModuleComptableDiminution extends AREModuleComptable {

    // Doit être déclare en variable de classe.
    // Bien que référence dans l'objet APIGestionCompta, ceci car un
    // compta.getJournal met
    // le log de la compta en erreur si le journal est null.

    class InnerBPInfoContainer {

        String idDemande = "";
        String idTiersBeneficiaire = "";

    }

    // Class statique, référence par cette instance
    private static REModuleComptableDiminution instance = null;

    /**
     * @return L'instance de cette classe
     * @throws Exception
     */
    public static synchronized REModuleComptableDiminution getInstance(final BSession session) throws Exception {
        if (REModuleComptableDiminution.instance == null) {
            REModuleComptableDiminution.instance = new REModuleComptableDiminution(session, true);
        }
        return REModuleComptableDiminution.instance;
    }

    // Workaround pour eviter comportement incohérent dans la compta.
    private APIJournal journal = null;

    private BSession session = null;

    private REModuleComptableDiminution(final BSession session, final boolean isGenererEcritureComptable)
            throws Exception {
        super(isGenererEcritureComptable);
        this.session = session;
    }

    /**
     * Contrôle si des erreurs sont présentes dans la session courante , dans la session Osiris et dans la transaction
     * du process
     * 
     * @param process
     * @param sessionOsiris
     * @throws RemoteException
     */
    private void checkErrorsAndThrowExceptionIfNeeded(final BProcess process, final BISession sessionOsiris,
            final String message) throws RemoteException {
        if (sessionOsiris.hasErrors()) {
            String messageFinal = message + ". Error message : " + sessionOsiris.getErrors().toString();
            throw new RETechnicalException(messageFinal);
        }
        if (session.hasErrors()) {
            String messageFinal = message + ". Error message : " + session.getErrors().toString();
            throw new RETechnicalException(messageFinal);
        }
        if (process.getTransaction().hasErrors()) {
            String messageFinal = message + ". Error message : " + process.getTransaction().getErrors().toString();
            throw new RETechnicalException(messageFinal);
        }
    }

    public FWMemoryLog diminuerRenteAccordee(final BProcess process, final String dateFinDroit,
            final RERenteAccordee ra, final String csGenreTraitementDiminution, final String dateRecap)
            throws Exception {

        // on vérifie si la rente accordée n'a pas déjà été diminuée. Cela permet d'éviter de créer des diminutions à
        // double.
        if (!JadeStringUtil.isBlankOrZero(ra.getDateFinDroit())) {
            String message = java.text.MessageFormat.format(
                    session.getLabel("ERREUR_RENTE_ACCORDEE_DEJA_DIMINUEE"),
                    new Object[] { ra.getCodePrestation(), ra.getMontantPrestation(), ra.getIdTiersBeneficiaire(),
                            ra.getDateFinDroit() });
            throw new RETechnicalException(message);
        }

        FWMemoryLog memoryLog = new FWMemoryLog();
        memoryLog.logMessage(session.getLabel("ERREUR_TRAITEMENT_DIM_RA"), FWMessage.INFORMATION, this.getClass()
                .getName());

        JACalendar cal = new JACalendarGregorian();
        String dateDernierPaiement = REPmtMensuel.getDateDernierPmt(session);

        if (REPmtMensuel.DATE_NON_TROUVEE_POUR_DERNIER_PAIEMENT.equals(dateDernierPaiement)) {
            String message = session.getLabel("ERREUR_IMPOSSIBLE_RETROUVER_DATE_DERNIER_PAIEMENT");
            throw new RETechnicalException(message);
        }

        JADate dateDernierPmt = new JADate(dateDernierPaiement);
        JADate dateFinRA = new JADate(dateFinDroit);
        InnerBPInfoContainer ic = getBPInfoContanier(session, process.getTransaction(), ra);
        String libelle = getLibelleJournal();
        String dateValeurComptable = getDateValeurComptable(session, cal);
        BISession sessionOsiris = PRSession.connectSession(session, CAApplication.DEFAULT_APPLICATION_OSIRIS);

        // Inititialisation du processus de compta
        APIGestionComptabiliteExterne compta = initComptaExterne((BSession) sessionOsiris, process, libelle,
                dateValeurComptable);

        RELot lot = new RELot();
        lot.setSession(session);
        lot.setCsEtatLot(IRELot.CS_ETAT_LOT_VALIDE);
        lot.setCsTypeLot(IRELot.CS_TYP_LOT_DIMINUTION);
        lot.setCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
        lot.setDescription(libelle);
        lot.add(process.getTransaction());

        checkErrorsAndThrowExceptionIfNeeded(process, sessionOsiris, "Errors ccours after RELot creation");

        boolean creerRestitution = doitOnCreerUneRestitutionEnCompta(ra, cal, dateDernierPmt, dateFinRA);

        if (creerRestitution) {

            // Creation de l'idExterneRole (qui est tout simplement le numéro numéro AVS de l'assuré
            String idExterneRole = PRTiersHelper.getTiersParId(session, ic.idTiersBeneficiaire).getProperty(
                    PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

            memoryLog.logMessage(session.getLabel("BENEFICIAIRE_PRINCIPAL") + " " + idExterneRole,
                    FWMessage.INFORMATION, this.getClass().getName());

            // Initialisation des rubriques
            if (REModuleComptableFactory.getInstance().RO_AVS == null) {
                REModuleComptableFactory.getInstance().initIdsRubriques(sessionOsiris);
            }

            // On prend le mois suivant, car le mois de fin est du, il ne faut donc pas le prendre en compte pour le
            // calcul du montant a restituer
            dateFinRA = cal.addMonths(dateFinRA, 1);

            // Montant a restituer. Le nombre de mois à restituer est égal au montant RETRO
            FWCurrency montantARestituer = getCumulDesMontantsDeA(session, process.getTransaction(), ra,
                    PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateFinRA.toStrAMJ()),
                    PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateDernierPmt.toStrAMJ()));

            // Y'a t'il un montant à restituer. On va créer une restitution uniquement si le montant est positif
            boolean montantARestituerEstPositif = montantARestituer.isPositive();

            // Si le montant est positif on va réaliser la restitution en fonction du code du genre de traitement de
            // diminution
            if (montantARestituerEstPositif) {

                if (JadeStringUtil.isBlankOrZero(csGenreTraitementDiminution)) {
                    creerRestitution(process, ra, memoryLog, ic);
                }

                else if (IREDiminution.CS_GENRE_TRATEMENT_DIM_RESTITUTION_PAR_FACTURATION
                        .equals(csGenreTraitementDiminution)) {
                    creerRestitutionParFacturation(process, ra, ic, idExterneRole, montantARestituer);
                }

                else if (IREDiminution.CS_GENRE_TRATEMENT_DIM_RESTITUTION.equals(csGenreTraitementDiminution)) {
                    creerRestitutionStandard(process, ra, memoryLog, dateDernierPmt, dateFinRA, ic, sessionOsiris,
                            dateValeurComptable, compta, lot, idExterneRole, montantARestituer);
                }
            }
        }

        /**
         * Traitement de la recap. avant cela on va s'assurer qu'il n'y a pas d'erreur
         */
        checkErrorsAndThrowExceptionIfNeeded(process, sessionOsiris, "Errors founded after restitution creation");

        creerEcritureDeRecap(process, ra, dateRecap, memoryLog, ic, lot);

        return memoryLog;
    }

    /**
     * Détermine si une diminution/restitution doit être créé en compta sur la base des infos suivantes
     * - date du dernier paiement
     * - date de fin de la RA
     * - si la prestation est bloquée
     * 
     * @param ra
     * @param cal
     * @param dateDernierPmt
     * @param dateFinRA
     * @return
     */
    private boolean doitOnCreerUneRestitutionEnCompta(final RERenteAccordee ra, JACalendar cal, JADate dateDernierPmt,
            JADate dateFinRA) {
        boolean creerRestitution = true;
        REEnteteBlocage entete = new REEnteteBlocage();
        try {
            entete.setSession(ra.getSession());
            entete.setIdEnteteBlocage(ra.getIdEnteteBlocage());
            entete.retrieve();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /*
         * CONDITION 1
         * Si la date du dernier pmt est égal à la date de la diminution et
         * n'est pas bloquée....
         */
        if ((cal.compare(dateDernierPmt, dateFinRA) == JACalendar.COMPARE_EQUALS)
                && (ra.getIsPrestationBloquee() != null) && (!ra.getIsPrestationBloquee().booleanValue())
                && (entete.getMontantBloque().equals(entete.getMontantDebloque()))) {
            creerRestitution = false;
        }

        /*
         * CONDITION 2
         * BZ 7052 - BZ 6742 - BZ 6396
         * Si la date de diminution est égal à la date du dernier paiement ET
         * Si la rente accordée est bloquée ET
         * Si la rente accordée ne contient pas d'id d'entête de blocage
         * ==> on ne fait pas d'inscription dans la compta
         */
        if (creerRestitution) {
            if (cal.compare(dateDernierPmt, dateFinRA) == JACalendar.COMPARE_EQUALS) {
                if ((ra.getIsPrestationBloquee() != null) && (ra.getIsPrestationBloquee())
                        && "0".equals(ra.getIdEnteteBlocage())) {
                    creerRestitution = false;
                }
            }
        }

        /*
         * CONDITION 3
         * BZ 8454
         * La restitution de montant et la création de journaux en compta n'est réalisé que si la date de radiation est
         * plus grand que le mois en cours
         */
        if (creerRestitution) {
            if (cal.compare(dateDernierPmt, dateFinRA) == JACalendar.COMPARE_FIRSTLOWER) {
                creerRestitution = false;
            }
        }
        return creerRestitution;
    }

    /**
     * Créé l'écriture de récapitulation
     * 
     * @param process
     * @param ra
     * @param dateRecap
     * @param memoryLog
     * @param ic
     * @param lot
     * @throws Exception
     */
    private void creerEcritureDeRecap(final BProcess process, final RERenteAccordee ra, final String dateRecap,
            FWMemoryLog memoryLog, InnerBPInfoContainer ic, RELot lot) throws Exception {

        int codeRecap = AREModuleComptable.getCodeRecap(ra.getCodePrestation(), IRERecap.GENRE_RECAP_DIMINUTION);

        memoryLog.logMessage(this.doEcritureRecap(session, process.getTransaction(), codeRecap,
                new FWCurrency(ra.getMontantPrestation()), ic.idTiersBeneficiaire, dateRecap, lot.getIdLot()));
    }

    private void creerRestitutionStandard(final BProcess process, final RERenteAccordee ra, FWMemoryLog memoryLog,
            JADate dateDernierPmt, JADate dateFinRA, InnerBPInfoContainer ic, BISession sessionOsiris,
            String dateValeurComptable, APIGestionComptabiliteExterne compta, RELot lot, String idExterneRole,
            FWCurrency montantARestituer) throws Exception, RemoteException {
        APISection sectionRestitution;
        // Ecriture normal (rente en cours)
        montantARestituer.negate();

        // récupération du compte annexe RENTIER
        APICompteAnnexe compteAnnexe = null;

        compteAnnexe = getComptaAnnexeForRoleRentier(compteAnnexe, compta, ic.idTiersBeneficiaire, idExterneRole);
        if (compteAnnexe == null) {
            throw new Exception(session.getLabel("ERREUR_CREATION_COMPTE_ANNEXE") + " /idExt=" + idExterneRole
                    + "/idTiersBP=" + ic.idTiersBeneficiaire + "/idDemande=" + ic.idDemande);

        }

        /*
         * 
         * Initialisation de la section
         */
        sectionRestitution = getSection(sessionOsiris, process.getTransaction(), compta, idExterneRole,
                dateValeurComptable, compteAnnexe.getIdCompteAnnexe(), APISection.ID_CATEGORIE_SECTION_RESTITUTIONS);

        // Ecritures sur la rubrique concernée
        APIRubrique rubriqueRestit = AREModuleComptable.getRubrique(ra.getCodePrestation(),
                ra.getSousTypeGenrePrestation(), AREModuleComptable.TYPE_RUBRIQUE_RESTITUTION);

        memoryLog.logMessage(doEcriture(session, compta, montantARestituer.toString(), rubriqueRestit,
                compteAnnexe.getIdCompteAnnexe(), sectionRestitution.getIdSection(), dateValeurComptable, null));

        montantARestituer.negate();

        // Ecritures sur la rubrique concernée
        APIRubrique rubrique = AREModuleComptable.getRubrique(ra.getCodePrestation(), ra.getSousTypeGenrePrestation(),
                AREModuleComptable.TYPE_RUBRIQUE_NORMAL);

        doTraitementRenteAccordeeBloquee(session, (BSession) sessionOsiris, process.getTransaction(), compta,
                sectionRestitution, ic.idTiersBeneficiaire, idExterneRole, dateValeurComptable, compteAnnexe, rubrique,
                ra, montantARestituer, dateFinRA, dateDernierPmt, memoryLog);

        if (!process.getTransaction().hasErrors() && !session.hasErrors() && !sessionOsiris.hasErrors()
                && !compta.hasFatalErrors() && !compta.getMessageLog().hasErrors()) {
            if ((journal != null) && !journal.isNew()) {
                lot.retrieve(process.getTransaction());
                lot.setIdJournalCA(journal.getIdJournal());
                lot.update(process.getTransaction());
                compta.comptabiliser();
            }
        }
    }

    private void creerRestitutionParFacturation(final BProcess process, final RERenteAccordee ra,
            InnerBPInfoContainer ic, String idExterneRole, FWCurrency montantARestituer) throws Exception {
        REFactureARestituer far = new REFactureARestituer();
        far.setSession(session);
        far.setIdExterne(idExterneRole);
        far.setCsCatSection(APISection.ID_CATEGORIE_SECTION_RESTITUTIONS);
        far.setCsEtat(IREFactureARestituer.CS_A_FACTURER);
        far.setCsRole(IntRole.ROLE_RENTIER);
        far.setIdRenteAccordee(ra.getIdPrestationAccordee());
        far.setIdTiersBenefPrincipal(ic.idTiersBeneficiaire);
        far.setMontantFactARestituer(montantARestituer.toString());
        far.add(process.getTransaction());
    }

    /**
     * Créé une restitution de type standard. En résumé ce type de restitution est pratiqué si le code récap est vide
     * 
     * @param process
     * @param ra
     * @param memoryLog
     * @param ic
     * @return
     * @throws Exception
     */
    private void creerRestitution(final BProcess process, final RERenteAccordee ra, final FWMemoryLog memoryLog,
            InnerBPInfoContainer ic) throws Exception {

        REDebloquerMontantRAHandler handler = new REDebloquerMontantRAHandler(process, session,
                process.getTransaction());
        handler.setIdRenteAccordee(ra.getIdPrestationAccordee());

        REInformationsComptabilite infoCompta = new REInformationsComptabilite();
        infoCompta.setIdInfoCompta(ra.getIdInfoCompta());
        infoCompta.retrieve(process.getTransaction());

        // Si pas trouvé d'adresse dans le domaine des rentes, retournera celle du domaine standard.
        TIAdressePaiementData adrpmtData = PRTiersHelper.getAdressePaiementData(session, process.getTransaction(),
                infoCompta.getIdTiersAdressePmt(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, null,
                JACalendar.todayJJsMMsAAAA());

        if (adrpmtData.isNew()) {
            throw new Exception("Aucune adresse de paiement trouvée pour la ra #" + ra.getIdPrestationAccordee());
        }

        Collection<APISection> listSectionBloquee = REComptaUtil.getSectionsBloqueesACompenser(session,
                ic.idTiersBeneficiaire, "999999999", APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN);

        if ((listSectionBloquee != null) && (listSectionBloquee.size() > 0)) {
            FWCurrency montantADebloquer = new FWCurrency("0");
            // Récupération du montant à verser !!!
            handler.setIdDomaine(adrpmtData.getIdApplication());
            handler.setIdTiersAdrPmt(infoCompta.getIdTiersAdressePmt());

            for (APISection section : listSectionBloquee) {
                FWCurrency mnt = new FWCurrency(section.getSolde());
                mnt.negate();
                handler.setIdSection(section.getIdSection());
                handler.setMontantADebloque(mnt.toString());
                montantADebloquer.add(mnt.toString());
                FWMemoryLog logCompta = handler.doTraitement(process, false);
                if (logCompta.hasErrors()) {
                    throw new Exception(session.getLabel("ERREUR_GENERATION_ECRITURES_COMPTABLES"));
                }
            }
            handler.comptabiliser();

            // memoryLog.logMessage("Un ordre de versement à été créé d'un montant de "
            // + montantADebloquer.toStringFormat() + " pour : " +
            // adrpmtData.getDesignation1_tiers() + " " +
            // adrpmtData.getDesignation2_tiers(),
            // FWMessage.WARNING,
            // this.getClass().getName());
        }
    }

    /**
     * Principe de traitement Cas 1) Diminution pour 09.2009 (septembre est donc du) Exemple : [RA 1000.- ][ RA 1100.-
     * ----------------------[ 05.2009][06.2009][07.2009][08.2009][09.2009]------
     * ----------------------------------------------------------------->t + --- 200974000 --- --- 200974010 blocage
     * Montant bloque : 1000 + 1100 + 1100 + 1100 + 1100 = 5400.- Sct. blocage en CA : 200974000 = 3200.-
     * (dateDebutPeride de la section = 01.05.2009) Sct. blocage en CA : 200974010 = 2200.- (dateDebutPeride de la
     * section = 01.08.2009) Cas 2) Diminution pour 09.2009 Exemple : [RA 1000.- ][ RA 1100.- ----------------------[
     * 05.2009][06.2009][07.2009][08.2009][09.2009]------
     * ----------------------------------------------------------------->t + --- 200974000 --- --- 200974010 blocage
     * Montant bloque : 1000 + 1100 + 1100 + 1100 + 1100 = 5400.- Sct. blocage en CA : 200974000 = 6000.-
     * (dateDebutPeride de la section = 01.05.2009) Sct. blocage en CA : 200974010 = 3500.- (dateDebutPeride de la
     * section = 01.08.2009) Algo : 0) Générer l'écriture de restitution avec le montant total à restituer. 1) Parcourir
     * toutes les sections de la plus récente à la plus ancienne 2) Pour chaque mois de la période de la dite section,
     * trouver le montant de la rente correspondant. 2.a) Tant qu'on est dans la même section, cumuler le montant à
     * restituer 2.b) Générer l'écriture de compensation. Exemple : 1) Sct = 200974010 2.a) Montant RA pour 09.2009 :
     * 1100.- Montant RA pour 08.2009 : 1100.- Cumul : 2200.- 2.b) Ecfiture de compensation de 2200.- sur section
     * 200974010 2.a) Montant RA pour 07.2009 : 1100.- Montant RA pour 06.2009 : 1100.- Montant RA pour 05.2009 : 1000.-
     * Cumul : 3200.- 2.b) Ecriture de compensation de 3200.- sur section 200974000
     * 
     * @param session
     * @param transaction
     * @param compta
     * @param sectionRestitution
     *            peut être null, dans ce cas, sera créée
     * @param idExterneRole
     * @param dateValeurComptable
     * @param compteAnnexe
     * @param rubriqueNormale
     * @param ra
     * @param log
     * @throws Exception
     */
    private void doTraitementRenteAccordeeBloquee(final BSession session, final BSession sessionOsiris,
            final BTransaction transaction, final APIGestionComptabiliteExterne compta, APISection sectionRestitution,
            final String idTiersBP, final String idExterneRole, final String dateValeurComptable,
            final APICompteAnnexe compteAnnexe, final APIRubrique rubriqueNormale, final RERenteAccordee ra,
            final FWCurrency montantTotalARestituer, final JADate dateFinRA, final JADate dateDernierPmt,
            final FWMemoryLog log) throws Exception {

        log.logMessage(session.getLabel("ERREUR_TAITEMENT_RA_BLOQUEE"), FWMessage.INFORMATION, this.getClass()
                .getName());

        // La RA n'est pas bloquee --> on ne fait rien
        if ((ra.getIsPrestationBloquee() == null) || !ra.getIsPrestationBloquee().booleanValue()) {

            log.logMessage(session.getLabel("ERREUR_PRESTATION_NON_BLOQUEE"), FWMessage.INFORMATION, this.getClass()
                    .getName());

            return;
        }

        REInformationsComptabilite infoCompta = new REInformationsComptabilite();
        infoCompta.setIdInfoCompta(ra.getIdInfoCompta());
        infoCompta.retrieve(transaction);

        APIRubrique rubriqueCompensation = AREModuleComptable.getRubriqueWithInit(sessionOsiris, null, null,
                AREModuleComptable.TYPE_RUBRIQUE_COMPENSATION);

        if (sectionRestitution == null) {
            sectionRestitution = getSection(sessionOsiris, transaction, compta, idExterneRole, dateValeurComptable,
                    compteAnnexe.getIdCompteAnnexe(), APISection.ID_CATEGORIE_SECTION_RESTITUTIONS);
        }

        Collection<APISection> listSectionsACompenser = REComptaUtil.getSectionsBloqueesACompenser(session, idTiersBP,
                montantTotalARestituer.toString(), APICompteAnnexe.PC_ORDRE_PLUS_RECENT);
        if ((listSectionsACompenser == null) || (listSectionsACompenser.size() == 0)) {
            log.logMessage(session.getLabel("ERREUR_PRESTATION_BLOQUEE_VERSEMENT_BLOQUE"), FWMessage.INFORMATION, this
                    .getClass().getName());
            return;
        }
        // Les section sont dans l'ordre de la plus récente à la plus
        // ancienne....
        Map<String, String> montantsParSectionBloquee = new HashMap<String, String>();

        FWCurrency soldeMontantAVerser = new FWCurrency(0);
        for (APISection section : listSectionsACompenser) {
            // Calcul des montants par section de blocage
            FWCurrency ss = new FWCurrency(section.getSolde());
            ss.negate();

            // On ne prend en compte que les sections non soldées
            if (ss.isNegative()) {
                continue;
            }
            montantsParSectionBloquee.put(section.getIdSection(), ss.toString());
            soldeMontantAVerser.add(ss.toString());
        }
        soldeMontantAVerser.sub(montantTotalARestituer.toString());
        Set<String> idsSection = montantsParSectionBloquee.keySet();

        FWCurrency soldeMontantTotalARestituer = new FWCurrency(montantTotalARestituer.toString());

        for (String idSectionBlocage : idsSection) {
            String montantSectionBlocage = montantsParSectionBloquee.get(idSectionBlocage);
            FWCurrency montantSct = new FWCurrency(montantSectionBlocage);

            FWCurrency montantDeLaCompensation = null;
            FWCurrency montantOV = null;
            if (soldeMontantTotalARestituer.compareTo(montantSct) == -1) {
                montantDeLaCompensation = new FWCurrency(soldeMontantTotalARestituer.toString());
                soldeMontantTotalARestituer = new FWCurrency(0);
            } else {
                montantDeLaCompensation = new FWCurrency(montantSct.toString());
                soldeMontantTotalARestituer.sub(montantSct);
            }

            if (soldeMontantAVerser.compareTo(montantSct) == -1) {
                montantOV = new FWCurrency(soldeMontantAVerser.toString());
            } else {
                montantOV = new FWCurrency(montantSct.toString());
            }

            if (montantDeLaCompensation.isPositive()) {
                // Compensation sur section de restitution.
                log.logMessage(doEcriture(session, compta, montantDeLaCompensation.toString(), rubriqueCompensation,
                        compteAnnexe.getIdCompteAnnexe(), sectionRestitution.getIdSection(), dateValeurComptable, null));

                montantDeLaCompensation.negate();

                // Do ecriture de compensation sur section blocage
                log.logMessage(doEcriture(session, compta, montantDeLaCompensation.toString(), rubriqueCompensation,
                        compteAnnexe.getIdCompteAnnexe(), idSectionBlocage, dateValeurComptable, null));
            }

            log.logMessage(FWMessageFormat.format((session).getLabel("DIM_RA_BLOQUE_MNT_DISPO"), montantOV),
                    FWViewBeanInterface.WARNING, this.getClass().getName());
        }

        // A chaque pmt d'une rente accordée bloquée, on remet la RA dans l'état
        // non bloqué,
        // même si la totalité du solde n'est pas débloqué (dixit rje).
        ra.setIsPrestationBloquee(Boolean.FALSE);
        ra.setTypeDeMiseAJours("0");
        JADate dateEcheance = new JADate(ra.getDateEcheance());
        JACalendar cal = new JACalendarGregorian();
        if (cal.compare(dateEcheance, dateDernierPmt) == JACalendar.COMPARE_SECONDUPPER) {
            ra.setDateEcheance("");
        }
        ra.update(transaction);

        // MAJ de l'entête
        REEnteteBlocage entete = new REEnteteBlocage();
        entete.setSession(session);
        entete.setIdEnteteBlocage(ra.getIdEnteteBlocage());
        entete.retrieve(transaction);
        PRAssert.notIsNew(entete, null);

        FWCurrency montantDebloque = new FWCurrency(entete.getMontantDebloque());
        montantDebloque.add(montantTotalARestituer);
        entete.setMontantDebloque(montantDebloque.toString());
        entete.update(transaction);
    }

    /**
     * Recherche de l'id tiers bénéficiaire principal et de l'id de la demande de rente
     * 
     * @param session
     * @param transaction
     * @param ra
     * @return
     */
    private InnerBPInfoContainer getBPInfoContanier(final BSession session, final BTransaction transaction,
            final RERenteAccordee ra) throws Exception {

        InnerBPInfoContainer result = new InnerBPInfoContainer();

        REInformationsComptabilite ic = new REInformationsComptabilite();
        ic.setSession(session);
        ic.setIdInfoCompta(ra.getIdInfoCompta());
        ic.retrieve(transaction);

        if (ic.isNew() || JadeStringUtil.isBlankOrZero(ic.getIdCompteAnnexe())) {
            result.idTiersBeneficiaire = getIdTiersBP(session, transaction, ra);
        } else {
            APICompteAnnexe ca = null;
            BISession sessionOsiris = PRSession.connectSession(session, CAApplication.DEFAULT_APPLICATION_OSIRIS);
            ca = (APICompteAnnexe) sessionOsiris.getAPIFor(APICompteAnnexe.class);
            ca.setIdCompteAnnexe(ic.getIdCompteAnnexe());
            ca.retrieve();

            if (ca.isNew()) {
                result.idTiersBeneficiaire = getIdTiersBP(session, transaction, ra);
            } else {
                result.idTiersBeneficiaire = ca.getIdTiers();
            }
        }

        RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager mgr = new RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager();
        mgr.setSession(session);
        mgr.setForIdRenteAccordee(ra.getIdPrestationAccordee());
        mgr.find(transaction);

        // Ce cas arrive si la RA à été saisie manuellement...
        if (mgr.getSize() == 0) {

            REBasesCalcul bc = new REBasesCalcul();
            bc.setSession(session);
            bc.setIdBasesCalcul(ra.getIdBaseCalcul());
            bc.retrieve(transaction);

            RERenteCalculee rc = new RERenteCalculee();
            rc.setSession(session);
            rc.setIdRenteCalculee(bc.getIdRenteCalculee());
            rc.retrieve(transaction);
            PRAssert.notIsNew(rc, null);

            REDemandeRente demande = new REDemandeRente();
            demande.setSession(session);
            demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
            demande.setIdRenteCalculee(rc.getIdRenteCalculee());
            demande.retrieve(transaction);
            PRAssert.notIsNew(demande, null);

            result.idDemande = demande.getIdDemandeRente();
        }

        else {
            RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions elm = (RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions) mgr
                    .get(0);
            REDecisionEntity decision = new REDecisionEntity();
            decision.setSession(session);
            decision.setIdDecision(elm.getIdDecision());
            decision.retrieve(transaction);
            PRAssert.notIsNew(decision, null);

            result.idDemande = decision.getIdDemandeRente();
        }

        return result;
    }

    private APICompteAnnexe getComptaAnnexeForRoleRentier(APICompteAnnexe compteAnnexe,
            final APIGestionComptabiliteExterne compta, final String idTiersBP, final String idExterneRole) {

        if (compteAnnexe != null) {
            return compteAnnexe;
        }

        // if (journal==null) {
        compta.createJournal();
        journal = compta.getJournal();
        // }

        // récupération du compte annexe RENTIER
        compteAnnexe = compta.getCompteAnnexeByRole(idTiersBP, IntRole.ROLE_RENTIER, idExterneRole);
        return compteAnnexe;
    }

    /**
     * Cumul des montants mensuels pour la période donnée. Les dates fournies sont inclusent
     * 
     * @param session
     * @param transaction
     * @param ra
     * @param dateDebut
     *            format : mm.aaaa
     * @param dateFin
     *            format : mm.aaaa
     * @return
     */
    public FWCurrency getCumulDesMontantsDeA(final BSession session, final BTransaction transaction,
            final RERenteAccordee ra, final String dateDebut, final String dateFin) throws Exception {

        REPrestationsDuesManager mgr = new REPrestationsDuesManager();
        mgr.setSession(session);
        mgr.setForIdRenteAccordes(ra.getIdPrestationAccordee());
        mgr.setToDateDebut(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateFin));
        mgr.setFromDateFin(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateDebut));
        mgr.setForCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
        mgr.setOrderBy(REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT + " ASC ");
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        JACalendar cal = new JACalendarGregorian();

        JADate df = new JADate(dateFin);

        FWCurrency result = new FWCurrency(0);
        for (int i = 0; i < mgr.size(); i++) {
            REPrestationDue pd = (REPrestationDue) mgr.getEntity(i);

            // Check si la date calIdx est incluse dans la prestation due.
            // Si ce n'est pas le cas, on passe à la suivante
            JADate ddPD = new JADate(pd.getDateDebutPaiement());

            JADate calIdx = new JADate(dateDebut);
            if (cal.compare(calIdx, ddPD) == JACalendar.COMPARE_FIRSTLOWER) {
                calIdx = new JADate(pd.getDateDebutPaiement());
            }

            JADate dfPD = null;
            if (JadeStringUtil.isBlankOrZero(pd.getDateFinPaiement())) {
                dfPD = new JADate("31.12.2999");
            } else {
                dfPD = new JADate(pd.getDateFinPaiement());
            }

            while (((cal.compare(calIdx, ddPD) == JACalendar.COMPARE_FIRSTUPPER) || (cal.compare(calIdx, ddPD) == JACalendar.COMPARE_EQUALS))
                    && ((cal.compare(calIdx, dfPD) == JACalendar.COMPARE_FIRSTLOWER) || (cal.compare(calIdx, dfPD) == JACalendar.COMPARE_EQUALS))) {

                result.add(pd.getMontant());

                // On passe au mois suivant
                calIdx = cal.addMonths(calIdx, 1);

                if (cal.compare(calIdx, df) == JACalendar.COMPARE_FIRSTUPPER) {
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * Ex. today = 10.08.2007 date dernier pmt = 08.2007 return 10.08.2007 today = 31.08.2007 date dernier pmt = 09.2007
     * return 01.09.2007
     * 
     * @param session
     * @param cal
     * @return Date valeur comptable au format jj.mm.aaaa
     * @throws JAException
     */
    private String getDateValeurComptable(final BSession session, final JACalendar cal) throws JAException {

        JADate todayMMxAAAA = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(JACalendar.todayJJsMMsAAAA()));
        JADate dateDernierPmt = new JADate(REPmtMensuel.getDateDernierPmt(session));

        if (cal.compare(todayMMxAAAA, dateDernierPmt) == JACalendar.COMPARE_FIRSTLOWER) {
            return "01." + PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateDernierPmt.toStrAMJ());
        } else {
            return JACalendar.todayJJsMMsAAAA();
        }

    }

    private String getIdTiersBP(final BSession session, final BTransaction transaction, final RERenteAccordee ra)
            throws Exception {
        REBasesCalcul bc = new REBasesCalcul();
        bc.setSession(session);
        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
        bc.retrieve(transaction);

        RERenteCalculee rc = new RERenteCalculee();
        rc.setSession(session);
        rc.setIdRenteCalculee(bc.getIdRenteCalculee());
        rc.retrieve(transaction);
        PRAssert.notIsNew(rc, null);

        REDemandeRente demande = new REDemandeRente();
        demande.setSession(session);
        demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
        demande.setIdRenteCalculee(rc.getIdRenteCalculee());
        demande.retrieve(transaction);
        PRAssert.notIsNew(demande, null);

        RERenteAccJoinTblTiersJoinDemRenteManager mgr2 = new RERenteAccJoinTblTiersJoinDemRenteManager();
        mgr2.setSession(session);
        mgr2.setForNoDemandeRente(demande.getIdDemandeRente());
        mgr2.find(transaction, BManager.SIZE_NOLIMIT);

        List<Long> idsRA = new ArrayList<Long>();
        for (RERenteAccJoinTblTiersJoinDemandeRente r : mgr2.getContainerAsList()) {
            idsRA.add(Long.parseLong(r.getIdPrestationAccordee()));
        }
        REBeneficiairePrincipalVO bpVO = REBeneficiairePrincipal.retrieveBeneficiairePrincipal(session, transaction,
                idsRA);
        return bpVO.getIdTiersBeneficiairePrincipal();

    }

    private String getLibelleJournal() {
        String r = session.getLabel("JOURNAL_DIMINUTION");
        if (r.length() > 40) {
            r = r.substring(0, 40);
        }
        return r;
    }

    private APISection getSection(final BISession sessionOsiris, final BTransaction transaction,
            final APIGestionComptabiliteExterne compta, final String idExterneRole, final String dateComptable,
            final String idCompteAnnexe, final String idCategorieSection) throws Exception {

        String typeSection = null;
        if (APISection.ID_CATEGORIE_SECTION_RETOUR.equals(idCategorieSection)) {
            typeSection = APISection.ID_TYPE_SECTION_RETOUR;
        } else if (APISection.ID_CATEGORIE_SECTION_RESTITUTIONS.equals(idCategorieSection)) {
            typeSection = APISection.ID_TYPE_SECTION_RESTITUTION;

        } else if (APISection.ID_CATEGORIE_SECTION_PRESTATIONS_BLOQUEES.equals(idCategorieSection)) {
            typeSection = APISection.ID_TYPE_SECTION_BLOCAGE;
        } else {
            throw new Exception("Unsupported idCategorieSection : " + idCategorieSection);
        }

        // on créé un numero de facture unique qui servira a creer la section
        String noFacture = CAUtil.creerNumeroSectionUnique(sessionOsiris, transaction, IntRole.ROLE_RENTIER,
                idExterneRole, typeSection, String.valueOf(new JADate(dateComptable).getYear()), idCategorieSection);

        if (journal == null) {
            compta.createJournal();
            journal = compta.getJournal();
        }

        APISection section = compta.getSectionByIdExterne(idCompteAnnexe, typeSection, noFacture,
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, null);
        return section;

    }

    /**
     * Cumul le total des montants bloqués, et désactive les montants bloqués.
     * 
     * @param session
     * @param transaction
     * @param ra
     * @return le total des montants bloqué
     * @throws Exception
     */
    /*
     * private FWCurrency doTraitementMontantBloque(BISession session, BTransaction transaction, RERenteAccordee ra)
     * throws Exception {
     * 
     * REEnteteBlocage enteteBlk = new REEnteteBlocage(); enteteBlk.setSession((BSession)session);
     * enteteBlk.setIdEnteteBlocage(ra.getIdEnteteBlocage()); enteteBlk.retrieve(transaction);
     * 
     * if (enteteBlk.isNew()) { return new FWCurrency(0); } else { FWCurrency mnt = new
     * FWCurrency(enteteBlk.getMontantBloque()); mnt.sub(enteteBlk.getMontantDebloque()); return mnt; } }
     */

    /**
     * Instantiation et initialisation du module de compta externe
     * 
     * @param sessionOsiris
     * @param process
     * @param libelle
     * @param dateValeurComptable
     * @return APIGestionComptabiliteExterne - Le module de compta externe
     * @throws Exception
     */
    private APIGestionComptabiliteExterne initComptaExterne(final BSession sessionOsiris, final BProcess process,
            final String libelle, final String dateValeurComptable) throws Exception {
        APIGestionComptabiliteExterne compta = null;
        compta = (APIGestionComptabiliteExterne) sessionOsiris.getAPIFor(APIGestionComptabiliteExterne.class);
        compta.setDateValeur(dateValeurComptable);
        compta.setEMailAddress(process.getEMailAddress());

        FWMemoryLog comptaMemoryLog = new FWMemoryLog();
        comptaMemoryLog.setSession(sessionOsiris);
        compta.setMessageLog(comptaMemoryLog);

        compta.setSendCompletionMail(false);
        compta.setTransaction(process.getTransaction());
        compta.setLibelle(libelle);
        compta.setProcess(process);

        return compta;
    }
}
