/*
 * Créé le 28 août 07
 */
package globaz.corvus.process;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.lots.RELotManager;
import globaz.corvus.db.rentesaccordees.REPaiementRentes;
import globaz.corvus.db.rentesaccordees.REPaiementRentesManager;
import globaz.corvus.module.compta.AREModuleComptable;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.pmt.mensuel.RECumulPrstParRubrique;
import globaz.corvus.utils.pmt.mensuel.REGroupOperationCAUtil;
import globaz.corvus.utils.pmt.mensuel.REGroupOperationMotifUtil;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JATime;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIGestionRentesExterne;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Process effectuant le paiement mensuel des rentes accordées en erreur
 * </p>
 * 
 * @author SCR
 */
public class REExecuterRentesEnErreurProcess extends AREPmtMensuel {

    // Id du lot à corriger.
    // Initialisé lors de la validation du traitement.
    // String idLot = null;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDernierPmt;
    private String datePmtEnCours;
    private boolean isErreursDetectee;
    private long nombreDeRentesVersees;

    public REExecuterRentesEnErreurProcess() {
        super();
    }

    public REExecuterRentesEnErreurProcess(BProcess parent) {
        super(parent);
    }

    public REExecuterRentesEnErreurProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected void _validate() throws Exception {

        setSendCompletionMail(true);
        setSendMailOnError(true);
        setControleTransaction(true);

        if (JadeStringUtil.isBlankOrZero(getDescription())) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_DECSC_OBL"));
        } else if (getDescription().length() > 40) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_DESC_40_CARS"));
        }

        // On contrôle que le mois précédent ait bien été validé...
        JADate moisPrecedent = new JADate(getMoisPaiement());
        JACalendar cal = new JACalendarGregorian();
        moisPrecedent = cal.addMonths(moisPrecedent, -1);

        RELotManager mgr = new RELotManager();
        mgr.setSession(getSession());
        mgr.setForCsType(IRELot.CS_TYP_LOT_MENSUEL);
        mgr.setForCsEtat(IRELot.CS_ETAT_LOT_VALIDE);
        mgr.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
        mgr.setForDateEnvoiInMMxAAAA(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(moisPrecedent.toStrAMJ()));
        mgr.find(getTransaction());

        if (mgr.size() != 1) {
            throw new Exception(getSession().getLabel("ERREUR_PREAPARATION_PMTMENSUEL_PAS_ENCORE_EFFECTUEE"));
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    @Override
    protected boolean runProcess() {

        boolean succes = false;
        isErreursDetectee = false;

        BTransaction transaction = getTransaction();
        APIGestionRentesExterne compta = null;

        BTransaction innerTransaction = null;

        // Ne comprend pas les rentes bloquées, ni les retenues
        long nombreDeRentes = 0;
        nombreDeRentesVersees = 0;

        // Ne comprend pas les rentes bloquées, ni les retenues
        FWCurrency montantTotalAVerser = new FWCurrency("0");
        try {

            _validate();
            datePmtEnCours = REPmtMensuel.getDateDernierPmt(getSession());
            dateDernierPmt = REPmtMensuel.getDateDernierPmt(getSession());
            JADate jd = new JADate("01." + dateDernierPmt);
            JACalendar cal = new JACalendarGregorian();
            jd = cal.addMonths(jd, -1);
            dateDernierPmt = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(jd.toStrAMJ());

            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            // La transaction pour chaque groupe d'opération
            innerTransaction = (BTransaction) getSession().newTransaction();
            innerTransaction.openTransaction();

            getMemoryLog().logMessage(
                    "Starting process PaiementMensuelRentes : " + (new JATime(JACalendar.now())).toStr(":"),
                    FWMessage.INFORMATION, "");
            _validate();

            getMemoryLog().setTransaction(innerTransaction);

            RELotManager mgrl = new RELotManager();
            mgrl.setSession(getSession());
            mgrl.setForCsEtat(IRELot.CS_ETAT_LOT_PARTIEL);
            mgrl.setForCsType(IRELot.CS_TYP_LOT_MENSUEL);
            mgrl.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
            mgrl.setForDateEnvoiInMMxAAAA(getMoisPaiement());
            mgrl.find(innerTransaction);

            RELot lot = null;

            if (mgrl.size() > 1) {
                throw new Exception(getSession().getLabel("ERREUR_PLS_LOTS_ERREUR_TROUVES"));
            } else if (mgrl.size() == 0) {
                throw new Exception(getSession().getLabel("ERREUR_AUCUN_LOTS_ERREUR_TROUVES"));
            } else {
                // Récupération du lot
                lot = (RELot) mgrl.getFirstEntity();
            }

            if (getSession().hasErrors()) {
                succes = false;
                throw new Exception("Validation failed !!!");
            }

            innerTransaction = commitResetTransaction(innerTransaction);
            getMemoryLog().setTransaction(innerTransaction);

            /*
             * Instanciation du processus standard de compta
             */

            BISession sessionOsiris = null;

            sessionOsiris = PRSession.connectSession(getSession(), CAApplication.DEFAULT_APPLICATION_OSIRIS);
            compta = (APIGestionRentesExterne) sessionOsiris.getAPIFor(APIGestionRentesExterne.class);

            // Creation des journaux
            compta.createJournal(getSession(), innerTransaction, getDescription() + " ", "01." + getMoisPaiement());
            compta.createOrdreGroupe(getSession(), innerTransaction, getSession().getLabel("PMT_MENSUEL_OG") + " "
                    + getDescription() + " ", getDateEcheancePaiement(), getNumeroOG(), getSession().getApplication()
                    .getProperty(REApplication.PROPERTY_ID_ORGANE_EXECUTION));

            // reservation de la plage d'increment pour les ecritures en CA
            // inclure retenue et exclure blocage
            getMemoryLog().logMessage("2a) before getSqlCount : " + (new JATime(JACalendar.now())).toStr(":"),
                    FWMessage.INFORMATION, "");
            innerTransaction = commitResetTransaction(innerTransaction);

            nombreDeRentes = getNombreRentes(getSession(), innerTransaction, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);

            nombreDeRentesVersees = nombreDeRentes;
            getMemoryLog().logMessage("2b) after getSqlCount : " + (new JATime(JACalendar.now())).toStr(":"),
                    FWMessage.INFORMATION, "");
            PlageIncrement plageIncrementsOperations = reserverPlageIncrementsOperationsCA(getSession(), nombreDeRentes);

            PlageIncrement plageIncrementsSections = reserverPlageIncrementsSectionsCA(getSession(), nombreDeRentes);

            if ((plageIncrementsOperations == null) || getMemoryLog().hasErrors()) {
                throw new Exception("Erreur lors la reservation de la plage d'increments en CA");
            } else {
                getMemoryLog().logMessage(
                        "Plage incrément : min/max = " + plageIncrementsOperations.min + "/"
                                + plageIncrementsOperations.max, FWMessage.INFORMATION, "");
            }

            // Récupération des rentes à verser...
            REPaiementRentesManager mgr = new REPaiementRentesManager();
            mgr.setSession(getSession());
            mgr.setForDatePaiement(getMoisPaiement());
            mgr.setForIsEnErreur(Boolean.TRUE);
            BStatement statement = null;
            REPaiementRentes rente = null;
            statement = mgr.cursorOpen(transaction);

            Key previousKey = null;
            Key currentKey = null;
            // parcours des rentes accordées
            long increment = plageIncrementsOperations.min;

            long incrementSection = plageIncrementsSections.min;

            // Récupération des infos CA
            // idCompteCourant
            // idRubrique

            String noSecteurRente = getNoSecteurRente(getSession());

            String idCompteCourant = getIdCompteCourant(getSession(), innerTransaction,
                    APISection.ID_TYPE_SECTION_RENTE_AVS_AI, noSecteurRente);

            String idRubriqueOV = getIdRubriquePourVersement(getSession(), innerTransaction);

            REGroupOperationCAUtil grpOP = null;
            REGroupOperationMotifUtil motifVersement = null;
            String nomCache = null;

            if (innerTransaction.hasErrors() || getSession().hasErrors()) {
                throw new Exception(innerTransaction.getErrors().toString());
            }

            getMemoryLog().logMessage("3a) before main sql request : " + (new JATime(JACalendar.now())).toStr(":"),
                    FWMessage.INFORMATION, "");
            boolean isOperationAdded = false;

            // Contrôle que la période comptable soit bien ouverte...
            // Il faut passer la innerTransaction dans l'objet comptaAux, pour
            // éviter des lock entre transaction, car
            // c'est la inner transaction qui est utilisé pour les écritures
            // comptable, également pour les blocage/retenues...

            initComptaExterne(innerTransaction, false);
            if (!comptaExt.isPeriodeComptableOuverte("01." + getMoisPaiement())) {
                throw new Exception("Aucune periodre comptable ouverte pour la période " + getMoisPaiement());
            }
            // On force la création du journal
            initComptaExterne(innerTransaction, true);
            innerTransaction = commitResetTransaction(innerTransaction);

            Map<Integer, RECumulPrstParRubrique> mapCumulPrstParGenreRentes = new HashMap<Integer, RECumulPrstParRubrique>();
            int counterTransaction = 0;

            String currentIdRA = "aaa";
            String previousIdRa = "bbb";

            getMemoryLog().logMessage("Point de non retour atteint !!!", FWMessage.INFORMATION, "");
            int noSectionIncrement = 0;

            while ((rente = (REPaiementRentes) mgr.cursorReadNext(statement)) != null) {
                try {
                    // Contrôle que la ra n'est pas traitée 2 fois, ne devrait
                    // jamais arriver.
                    // cf. bz-3675
                    currentIdRA = rente.getIdRenteAccordee();
                    if (currentIdRA.equals(previousIdRa)) {
                        if (grpOP != null) {
                            grpOP.setGroupOperationEnErreur(true);
                        } else {
                            doMiseEnErreurRA(getSession(), currentIdRA,
                                    getSession().getLabel("RENTE_ACCORDEE_TRAITMT_DOUBLE"));
                        }
                        getMemoryLog().logMessage(getSession().getLabel("RENTE_ACCORDEE_TRAITMT_DOUBLE"),
                                FWMessage.ERREUR,
                                getSession().getLabel("CONTROLER_ADR_PMT_RA") + " idRA = " + currentIdRA);
                        isErreursDetectee = true;
                        continue;
                    }
                    previousIdRa = currentIdRA;

                    try {
                        currentKey = getKey(rente);
                    } catch (Exception e) {
                        // En cas d'erreur de récupération de la clé (par exemple
                        // pas de CA)
                        // on met la RA en erreur !!!
                        doMiseEnErreurRA(getSession(), rente.getIdRenteAccordee(), e.toString());
                        getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
                        --nombreDeRentesVersees;
                        isErreursDetectee = true;
                        continue;
                    }

                    String idTiersPrincipal = rente.getIdTiersAdressePmt();

                    if (JadeStringUtil.isBlankOrZero(idTiersPrincipal) || Long.parseLong(idTiersPrincipal) < 0) {
                        idTiersPrincipal = rente.getIdTiersBeneficiaire();
                    }

                    String isoLangFromIdTiers = PRTiersHelper.getIsoLangFromIdTiers(getSession(), idTiersPrincipal);

                    // 1ère écriture...
                    if (previousKey == null) {
                        noSectionIncrement = 0;
                        // Le motif de versement est généré par rapport à la 1ère
                        // écriture trouvée pour
                        // chaque groupe d'opération, car trié par groupe
                        motifVersement = new REGroupOperationMotifUtil(rente.getNssTBE(), getMoisPaiement(),
                                rente.getNomTBE(), rente.getPrenomTBE(), rente.getReferencePmt(),
                                rente.getCodePrestation(), isoLangFromIdTiers);

                        nomCache = getNomCache(rente);

                        getMemoryLog().logMessage(
                                "3b) after main sql request : " + (new JATime(JACalendar.now())).toStr(":"),
                                FWMessage.INFORMATION, "");
                        grpOP = new REGroupOperationCAUtil();
                        grpOP.initOperation(getSession(), rente.getIdCompteAnnexe(), idCompteCourant,
                                rente.getIdTiersBeneficiaire(), idRubriqueOV, rente.getIdTiersAdressePmt(),
                                getIdAdrPmt(rente));

                        traiterRente(rente, grpOP, increment);
                    }
                    // Les clé sont identiques -> toujours dans la même adresse
                    else if (currentKey.equals(previousKey)) {
                        motifVersement.addCodePrest(rente.getCodePrestation());
                        traiterRente(rente, grpOP, increment);
                    }
                    // Nouvelle adresse de paiement
                    else {
                        long incrEcritureTypeVersement = increment;
                        long incOV = incrEcritureTypeVersement + 1;

                        // Génère les écritures comptables pour ce groupe...
                        try {

                            // Si l'on est toujours dans le même compte annexe, il faut utiliser un nouveau no de
                            // section.
                            if ((currentKey.idCompteAnnexe != null)
                                    && currentKey.idCompteAnnexe.equals(previousKey.idCompteAnnexe)) {
                                noSectionIncrement++;
                            }
                            // On change de compte annexe,
                            else {
                                noSectionIncrement = 0;
                            }

                            counterTransaction++;
                            RECumulPrstParRubrique[] array = grpOP.doTraitementComptable(this, getSession(),
                                    innerTransaction, compta, motifVersement.getMotif(getSession()),
                                    incrEcritureTypeVersement, incOV, incrementSection, datePmtEnCours, nomCache,
                                    noSectionIncrement, getDateEcheancePaiement());

                            for (RECumulPrstParRubrique element : array) {
                                mapCumulPrstParGenreRentes = grpOP
                                        .cumulParRubrique(mapCumulPrstParGenreRentes, element);
                            }

                            isOperationAdded = true;
                            if (counterTransaction >= 50) {
                                counterTransaction = 0;
                            }
                        } catch (Exception e) {
                            --nombreDeRentesVersees;
                            isErreursDetectee = true;
                            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
                        }

                        ++incrementSection;
                        ++increment;
                        ++increment;
                        // Réinitialisation du nouveau groupe
                        grpOP = new REGroupOperationCAUtil();
                        grpOP.initOperation(getSession(), rente.getIdCompteAnnexe(), idCompteCourant,
                                rente.getIdTiersBeneficiaire(), idRubriqueOV, rente.getIdTiersAdressePmt(),
                                getIdAdrPmt(rente));

                        // Le motif de versement est généré par rapport à la 1ère
                        // écriture trouvée pour
                        // chaque groupe d'opération, car trié par groupe
                        motifVersement = new REGroupOperationMotifUtil(rente.getNssTBE(), getMoisPaiement(),
                                rente.getNomTBE(), rente.getPrenomTBE(), rente.getReferencePmt(),
                                rente.getCodePrestation(), isoLangFromIdTiers);

                        nomCache = getNomCache(rente);

                        traiterRente(rente, grpOP, increment);

                    }
                    previousKey = new Key(currentKey);
                    ++increment;

                    // Utilisé pour le traitement du dernier groupe d'opération.
                    // On ne stocke que les info utiles
                    motifVersement = new REGroupOperationMotifUtil(rente.getNssTBE(), getMoisPaiement(),
                            rente.getNomTBE(), rente.getPrenomTBE(), rente.getReferencePmt(),
                            rente.getCodePrestation(), isoLangFromIdTiers);

                    nomCache = getNomCache(rente);
                } catch (Exception e) {
                    doMiseEnErreurRA(getSession(), rente.getIdRenteAccordee(), e.toString());
                    getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
                    --nombreDeRentesVersees;
                    isErreursDetectee = true;
                    continue;
                }
            }

            // currentKey sera a null, si aucune rente trouvée...
            if (currentKey != null) {
                // Génère les écritures comptables pour le dernier groupe...
                try {
                    RECumulPrstParRubrique[] array = grpOP.doTraitementComptable(this, getSession(), innerTransaction,
                            compta, motifVersement.getMotif(getSession()), increment, ++increment, incrementSection,
                            datePmtEnCours, nomCache, 0, getDateEcheancePaiement());

                    for (RECumulPrstParRubrique element : array) {
                        mapCumulPrstParGenreRentes = grpOP.cumulParRubrique(mapCumulPrstParGenreRentes, element);
                    }

                    isOperationAdded = true;
                } catch (Exception e) {
                    --nombreDeRentesVersees;
                    isErreursDetectee = true;
                    getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
                }

                // Des écritures ont été créé en compta --> on peut
                // comptabiliser...
                if (isOperationAdded) {
                    // On comptabilise le processus de compta externe, utilisé
                    // pour le traitement des RA bloquées et retenues
                    if (comptaExt != null) {
                        comptaExt.comptabiliser();
                    }
                }

                if (!isErreursDetectee) {
                    lot.retrieve(innerTransaction);
                    lot.setCsEtatLot(IRELot.CS_ETAT_LOT_VALIDE);
                    lot.update(innerTransaction);
                }
            }
            // Aucune rente à verser, le lot passe en validé
            else {
                lot.retrieve(innerTransaction);
                lot.setCsEtatLot(IRELot.CS_ETAT_LOT_VALIDE);
                lot.update(innerTransaction);
            }

            innerTransaction = commitResetTransaction(innerTransaction);
            transaction = commitResetTransaction(transaction);

            compta.finalize(this, getSession(), innerTransaction);

            getMemoryLog().logMessage("3b) Process ending at : " + (new JATime(JACalendar.now())).toStr(":"),
                    FWMessage.INFORMATION, "");

            getMemoryLog().logMessage("Paiement mensuel terminé.", FWMessage.INFORMATION, "");
            getMemoryLog().logMessage("=============================================================",
                    FWMessage.INFORMATION, "");
            getMemoryLog().logMessage("==                      RESUME                             ==",
                    FWMessage.INFORMATION, "");
            getMemoryLog().logMessage("=============================================================",
                    FWMessage.INFORMATION, "");
            getMemoryLog().logMessage("", FWMessage.INFORMATION, "");
            getMemoryLog().logMessage("Nombre de rentes à verser (exclus prst. bloquées)= " + nombreDeRentes,
                    FWMessage.INFORMATION, "");
            getMemoryLog().logMessage(
                    "Nombre de rentes versées (exclus prst. bloquées, retenues)= " + nombreDeRentesVersees,
                    FWMessage.INFORMATION, "");

            getMemoryLog()
                    .logMessage(
                            "Montant total versé (exclus prst. bloquées et retenues) = "
                                    + montantTotalAVerser.toStringFormat(), FWMessage.INFORMATION, "");

            // final check !!!!!!
            validationPmt(getSession(), compta, comptaExt, mapCumulPrstParGenreRentes);
            innerTransaction = commitResetTransaction(innerTransaction);

            String libelleOG = getDescription() + " (suite)";
            if (comptaExt != null) {
                getMemoryLog().logMessage(
                        "Préparation de l'OG des retenues/blocages : " + (new JATime(JACalendar.now())).toStr(":"),
                        FWMessage.INFORMATION, "");
                int n = Integer.parseInt(getNumeroOG());
                n++;
                if (n < 10) {
                    libelleOG = "OPAE 0" + n + " - " + libelleOG;
                } else {
                    libelleOG = "OPAE" + n + " - " + libelleOG;
                }
                comptaExt.preparerOrdreGroupe(getIdOrganeExecution(), String.valueOf(n), getDateEcheancePaiement(),
                        CAOrdreGroupe.VERSEMENT, CAOrdreGroupe.NATURE_RENTES_AVS_AI, libelleOG, getIsoCsTypeAvis(),
                        getIsoGestionnaire(), getIsoHighPriority());

            }

            Set<Integer> keys = mapCumulPrstParGenreRentes.keySet();
            Iterator<Integer> iter = keys.iterator();
            while (iter.hasNext()) {
                Integer key = iter.next();
                RECumulPrstParRubrique cppr = mapCumulPrstParGenreRentes.get(key);
                getMemoryLog().logMessage(
                        "Type - Rubrique : " + cppr.getType() + " - " + cppr.getIdRubrique() + " \tmontant ="
                                + cppr.getMontant(), FWMessage.INFORMATION, "");
            }

            succes = true;
            innerTransaction = commitResetTransaction(innerTransaction);

        } catch (Exception e) {
            succes = false;
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            if (innerTransaction.hasErrors()) {
                getMemoryLog().logMessage(innerTransaction.getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }
            if (getSession().hasErrors()) {
                getMemoryLog().logMessage(getSession().getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }

            try {
                innerTransaction.rollback();
            } catch (Exception e1) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            }
            return false;
        } finally {

            if (succes && !isErreursDetectee) {
                emailObject = getSession().getLabel("EMAIL_OBJECT_PMT_MENS_RENTES_SUCCES");
            } else {
                emailObject = getSession().getLabel("EMAIL_OBJECT_PMT_MENS_RENTES_ERREUR");
            }
            try {
                // Workaround
                // Vidage du cache, sans quoi, l'execution de l'OG plante. Raison : inconnue !!!!
                GlobazServer.getCurrentSystem().resetCache();

                innerTransaction.closeTransaction();
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                try {
                    innerTransaction.closeTransaction();
                    compta.closeAllStatements();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private void traiterRente(REPaiementRentes rente, REGroupOperationCAUtil grpOP, long increment) throws Exception {
        try {
            APIRubrique rubriqueComptable = AREModuleComptable.getRubriqueWithInit(initSessionOsiris(),
                    rente.getCodePrestation(), rente.getSousTypeCodePrestation(),
                    AREModuleComptable.TYPE_RUBRIQUE_NORMAL);

            checkAPIRubrique(rubriqueComptable, rente);

            grpOP.traiterEcriture(getSession(), rente,
                    rente.getNomTBE() + " " + rente.getPrenomTBE() + " " + rente.getCodePrestation(), increment,
                    rubriqueComptable == null ? null : rubriqueComptable.getIdRubrique(), dateDernierPmt,
                    datePmtEnCours, false);

        } catch (Exception e) {
            doMiseEnErreurRA(getSession(), rente.getIdRenteAccordee(), e.toString());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            --nombreDeRentesVersees;
            isErreursDetectee = true;
        }

    }
}
