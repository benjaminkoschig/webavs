package globaz.corvus.utils.pmt.mensuel;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.corvus.api.retenues.IRERetenues;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPaiementRentes;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeBloquee;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.retenues.RERetenuesPaiement;
import globaz.corvus.db.retenues.RERetenuesPaiementManager;
import globaz.corvus.module.compta.AREModuleComptable;
import globaz.corvus.module.compta.REModuleComptableFactory;
import globaz.corvus.process.AREPmtMensuel;
import globaz.corvus.process.REExecuterRentesEnErreurProcess;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.retenues.RERetenuesUtil;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIGestionRentesExterne;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.external.IntRole;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tauxImposition.api.IPRTauxImposition;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SCR
 */
public class REGroupOperationCAUtil {

    /**
     * @author SCR
     */
    public class OrdreVersement {
        public String idAdrPmt = "";
        public String idRubrique = "";
        public FWCurrency montant = new FWCurrency(0);
    }

    /**
     * List des écritures à passer en compta via les interfaces optimisées
     */
    private List<REEcritureUtil> ecritures = new ArrayList<REEcritureUtil>();
    private String errorMsgPhasePreparation = null;
    private String idCompteAnnexe = null;
    private String idCompteCourant = null;
    /**
     * <p>
     * Utilisé pour le contrôle uniquement.<br/>
     * Tous les idTiersAdrPmt doivent être les mêmes pour un groupe d'écriture.
     * </p>
     */
    private String idTiersAdrPmt = null;
    private String idTiersBeneficiaire = null;
    private boolean isGroupOperationEnErreur = false;
    /**
     * <p>
     * Pour le traitement des retenues et blocage de rentes, via les interfaces<br/>
     * CA standards.
     * </p>
     */
    private boolean isOperationSurJournalExterne = false;
    private OrdreVersement ov = null;
    /**
     * Section de blocage d'un compte annexe. Après réflexion sur le BZ 5755 avec RJE, SCR et PBA, il a été décidé qu'un
     * compte annexe n'aurait qu'une section de blocage en cours à la fois
     */
    private APISection sectionBlocage = null;

    public Map<Integer, RECumulPrstParRubrique> cumulParRubrique(Map<Integer, RECumulPrstParRubrique> result,
            RECumulPrstParRubrique cppr) {

        if (result.containsKey(cppr.getKey())) {
            RECumulPrstParRubrique elm = result.get(cppr.getKey());
            elm.addMontant(cppr.getMontant());
            result.put(cppr.getKey(), elm);
        } else {
            result.put(cppr.getKey(), cppr);
        }
        return result;
    }

    private Map<Integer, RECumulPrstParRubrique> cumulParRubrique(Map<Integer, RECumulPrstParRubrique> result,
            String type, String idRubrique, FWCurrency montant) {
        RECumulPrstParRubrique cppr = new RECumulPrstParRubrique();
        cppr.setIdRubrique(idRubrique);
        cppr.setMontant(new FWCurrency(montant.toString()));
        cppr.setType(type);

        return this.cumulParRubrique(result, cppr);
    }

    // TODO : Supprimer le message log sauf en cas d'erreur, car risque de outOfMemoryException
    protected BIMessage doEcriture(BSession session, APIGestionComptabiliteExterne compta, String libelle,
            String montantSigne, APIRubrique rubrique, String idCompteAnnexe, String idSection, String dateComptable) {

        FWMemoryLog log = new FWMemoryLog();

        if (!JadeStringUtil.isDecimalEmpty(montantSigne)) {
            FWCurrency montant = new FWCurrency(montantSigne);
            boolean positif = true;

            if (montant.isNegative()) {
                montant.negate();
                positif = false;
            }

            APIEcriture ecriture = compta.createEcriture();
            ecriture.setIdCompteAnnexe(idCompteAnnexe);
            ecriture.setIdSection(idSection);
            ecriture.setDate(dateComptable);
            ecriture.setIdCompte(rubrique.getIdRubrique());
            ecriture.setMontant(montant.toString());
            ecriture.setLibelle(libelle);

            if (positif) {
                ecriture.setCodeDebitCredit(APIEcriture.CREDIT);
            } else {
                ecriture.setCodeDebitCredit(APIEcriture.DEBIT);
            }
            compta.addOperation(ecriture);
            return null;
        } else {
            log.logMessage("Aucune écriture générée  - montant : " + montantSigne.toString(), FWMessage.INFORMATION,
                    this.getClass().getName());

            return log.getMessage(0);
        }

    }

    /**
     * Création des opérations comptable pour les blocage
     * 
     * @param session
     * @param transaction
     * @param process
     * @param ecriture
     * @return idSection
     * @throws Exception
     */
    private RECumulPrstParRubrique doOperationsBlocage(BSession session, BTransaction transaction,
            AREPmtMensuel process, APIGestionRentesExterne compta, String idExterneRole, APICompteAnnexe compteAnnexe,
            REEcriturePrstBloqueeUtil ecriture, String datePmtEnCours) throws Exception {

        REPrestationsAccordees ra = new REPrestationsAccordees();
        ra.setIdPrestationAccordee(ecriture.idRA);
        ra.setSession(session);
        ra.retrieve(transaction);

        PRAssert.notIsNew(ra, null);

        // On reset les flags isAttenteMaj....
        ra.setIsAttenteMajBlocage(Boolean.FALSE);
        ra.setIsAttenteMajRetenue(Boolean.FALSE);
        ra.update(transaction);

        REInformationsComptabilite ic = ra.loadInformationsComptabilite();
        PRAssert.notIsNew(ic, null);

        if (compteAnnexe == null) {
            throw new Exception(
                    "Traitement retenue : Impossible de créer le compte annexe, contrôlez les logs pour plus de détails.");
        }

        // si c'est la 1ère fois qu'on passe ici pour ce compte annexe, on récupère la section de blocage
        // correspondante (si inexistante, ou toutes les sections blocages soldées, la compta en recréer une)
        // ceci permet de n'avoir qu'une section de blocage active en même temps par compte annexe, ce qui facilite
        // le choix de la section à utiliser lors du déblocage d'une des rentes liées à ce compte annexe
        if (sectionBlocage == null) {
            sectionBlocage = compta.getOrCreateLastSectionForPrestationsBloquees(session, transaction,
                    process.initComptaExterne(transaction, true), ic.getIdCompteAnnexe(), idExterneRole,
                    IntRole.ROLE_RENTIER, REPmtMensuel.getDateProchainPmt(session).substring(3, 7));
        }

        if (sectionBlocage == null) {
            throw new Exception("Unable to retrieve sectionBlocage. idCA/idTypeSct/idExtRole = " + idCompteAnnexe + "/"
                    + APISection.ID_TYPE_SECTION_BLOCAGE + "/" + idExterneRole);
        }

        FWCurrency montantBloque = new FWCurrency(ecriture.montant);
        APIRubrique rubrique = AREModuleComptable.getRubrique(ra.getCodePrestation(), ra.getSousTypeGenrePrestation(),
                AREModuleComptable.TYPE_RUBRIQUE_NORMAL);

        process.getMemoryLog().logMessage(
                doEcriture(session, process.initComptaExterne(transaction, true), ecriture.libelle,
                        montantBloque.toString(), rubrique, compteAnnexe.getIdCompteAnnexe(),
                        sectionBlocage.getIdSection(), "01." + datePmtEnCours));

        RECumulPrstParRubrique result = new RECumulPrstParRubrique();
        result.setIdRubrique(rubrique.getIdRubrique());
        result.setMontant(montantBloque);
        result.setType(RECumulPrstParRubrique.TYPE_BLOCAGE_RETENUE);

        REPrestationsAccordees pracc = new REPrestationsAccordees();
        pracc.setSession(session);
        pracc.setIdPrestationAccordee(ecriture.idRA);
        pracc.retrieve(transaction);
        PRAssert.notIsNew(pracc, null);

        REEnteteBlocage enteteBlk = new REEnteteBlocage();
        if (JadeStringUtil.isBlankOrZero(pracc.getIdEnteteBlocage())) {
            enteteBlk.setSession(session);
            enteteBlk.setMontantBloque(montantBloque.toString());
            enteteBlk.add(transaction);
            pracc.setIdEnteteBlocage(enteteBlk.getIdEnteteBlocage());
            pracc.update(transaction);
        } else {
            enteteBlk.setSession(session);
            enteteBlk.setIdEnteteBlocage(pracc.getIdEnteteBlocage());
            enteteBlk.retrieve(transaction);
            PRAssert.notIsNew(enteteBlk, null);

            FWCurrency mnt = new FWCurrency(enteteBlk.getMontantBloque());
            mnt.add(montantBloque);
            enteteBlk.setMontantBloque(mnt.toString());
            enteteBlk.update(transaction);
        }

        // Stocker le blocage dans la table des blocage, pour permettre le
        // déblocage ultérieur...
        REPrestationAccordeeBloquee rab = new REPrestationAccordeeBloquee();
        rab.setSession(session);
        rab.setIdEnteteBlocage(enteteBlk.getIdEnteteBlocage());
        rab.setDateBlocage(datePmtEnCours);
        rab.setMontant(montantBloque.toString());
        rab.add(transaction);
        return result;
    }

    /**
     * Création des opérations comptable pour les retenues
     * 
     * @param session
     * @param transaction
     * @param process
     * @param ecriture
     * @return idSection
     * @throws Exception
     */
    private RECumulPrstParRubrique[] doOperationsRetenues(BSession session, BTransaction transaction,
            AREPmtMensuel process, APICompteAnnexe compteAnnexe, REEcritureRetenueUtil ecriture, String datePmtEnCours,
            PRTiersWrapper tw, APISection sectionStandard, String dateEcheance) throws Exception {

        List<RECumulPrstParRubrique> result = new ArrayList<RECumulPrstParRubrique>();

        List<RERetenueInfoUtil> retenues = ecriture.getRetenues();
        if (retenues != null) {

            // Ecriture principale de la rente accordée
            REPrestationsAccordees ra = new REPrestationsAccordees();
            ra.setIdPrestationAccordee(ecriture.idRA);
            ra.setSession(session);
            ra.retrieve(transaction);
            PRAssert.notIsNew(ra, null);

            // On reset les flags isAttenteMaj....
            ra.setIsAttenteMajBlocage(Boolean.FALSE);
            ra.setIsAttenteMajRetenue(Boolean.FALSE);
            ra.update(transaction);

            APIRubrique rubrique = AREModuleComptable.getRubrique(ra.getCodePrestation(),
                    ra.getSousTypeGenrePrestation(), AREModuleComptable.TYPE_RUBRIQUE_NORMAL);

            REInformationsComptabilite ic = ra.loadInformationsComptabilite();
            PRAssert.notIsNew(ic, null);

            if (compteAnnexe == null) {
                throw new Exception(
                        "Traitement retenue : Impossible de créer le compte annexe, contrôlez les logs pour plus de détails.");
            }

            FWCurrency mnt = new FWCurrency(ecriture.montant);
            // On créé l'écriture...

            // Test préliminaire....
            // On s'assure que le montant total des retenues n'excede pas le
            // montant de la rente accordee dans quel cas,
            // on mettra cette ra en erreur.

            FWCurrency mntRA = new FWCurrency(ra.getMontantPrestation());
            FWCurrency mntCumuleRetenues = new FWCurrency(0);
            for (RERetenueInfoUtil riu : retenues) {
                mntCumuleRetenues.add(riu.montantRetenu);
            }

            if (mntCumuleRetenues.compareTo(mntRA) == 1) {
                throw new Exception(session.getLabel("PMT_MENSUEL_ERROR_MNT_RETENUE") + " idRA = "
                        + ra.getIdPrestationAccordee());
            }

            process.getMemoryLog().logMessage(
                    doEcriture(session, process.initComptaExterne(transaction, true), ecriture.libelle, mnt.toString(),
                            rubrique, compteAnnexe.getIdCompteAnnexe(), sectionStandard.getIdSection(), "01."
                                    + datePmtEnCours));

            RECumulPrstParRubrique cppr = new RECumulPrstParRubrique();
            cppr.setType(RECumulPrstParRubrique.TYPE_BLOCAGE_RETENUE);
            cppr.setIdRubrique(rubrique.getIdRubrique());
            cppr.setMontant(new FWCurrency(mnt.toString()));
            result.add(cppr);

            for (RERetenueInfoUtil riu : retenues) {

                // MAJ de la retenue...

                RERetenuesPaiement retenue = new RERetenuesPaiement();
                retenue.setSession(session);
                retenue.setIdRetenue(riu.idRetenue);
                retenue.retrieve(transaction);
                PRAssert.notIsNew(retenue, null);

                FWCurrency montantDejaRetenu = new FWCurrency(retenue.getMontantDejaRetenu());
                montantDejaRetenu.add(riu.montantRetenu);
                retenue.setMontantDejaRetenu(JANumberFormatter.format(montantDejaRetenu.toString(), 0.01, 2,
                        JANumberFormatter.NEAR));

                if (!IRERetenues.CS_TYPE_IMPOT_SOURCE.equals(retenue.getCsTypeRetenue())) {
                    // Tout à été retenu, maj de la date de fin du paiement...
                    if (montantDejaRetenu.compareTo(new FWCurrency(retenue.getMontantTotalARetenir())) >= 0) {
                        retenue.setDateFinRetenue(datePmtEnCours);
                    }
                }

                retenue.update(transaction);

                // Traitement des écritures comptables
                if (IRERetenues.CS_TYPE_IMPOT_SOURCE.equals(riu.getCsTypeRetenue())) {
                    rubrique = REModuleComptableFactory.getInstance().IMPOT_SOURCE;

                    riu.getMontantRetenu().negate();
                    // On créé l'écriture...

                    process.getMemoryLog().logMessage(
                            doEcriture(session, process.initComptaExterne(transaction, true), ecriture.libelle, riu
                                    .getMontantRetenu().toString(), rubrique, compteAnnexe.getIdCompteAnnexe(),
                                    sectionStandard.getIdSection(), "01." + datePmtEnCours));

                    cppr = new RECumulPrstParRubrique();
                    cppr.setType(RECumulPrstParRubrique.TYPE_BLOCAGE_RETENUE);
                    cppr.setIdRubrique(rubrique.getIdRubrique());
                    cppr.setMontant(riu.getMontantRetenu());
                    result.add(cppr);

                } else if (IRERetenues.CS_TYPE_ADRESSE_PMT.equals(riu.getCsTypeRetenue())) {

                    // Le versement...
                    String idTiersPrincipal = idTiersAdrPmt;

                    if (JadeStringUtil.isBlankOrZero(idTiersPrincipal) || Long.parseLong(idTiersPrincipal) < 0) {
                        idTiersPrincipal = idTiersBeneficiaire;
                    }

                    String isoLangFromIdTiers = PRTiersHelper.getIsoLangFromIdTiers(session, idTiersPrincipal);

                    doOrdreVersement(
                            session,
                            process.initComptaExterne(transaction, true),
                            compteAnnexe.getIdCompteAnnexe(),
                            sectionStandard.getIdSection(),
                            riu.montantRetenu.toString(),
                            loadAdressePaiement(session, transaction, process.getDateEcheancePaiement(),
                                    retenue.getIdTiersAdressePmt(), retenue.getIdDomaineApplicatif())
                                    .getIdAvoirPaiementUnique(), process.getMotifVersement(
                                    tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), datePmtEnCours,
                                    tw.getProperty(PRTiersWrapper.PROPERTY_NOM),
                                    tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM), retenue.getReferenceInterne(),
                                    ra.getCodePrestation(), isoLangFromIdTiers), dateEcheance);

                    cppr = new RECumulPrstParRubrique();
                    cppr.setType(RECumulPrstParRubrique.TYPE_BLOCAGE_RETENUE);
                    cppr.setIdRubrique(RECumulPrstParRubrique.RUBRIQUE_FICTIVE_OV_PMT_BLOCAGE_RETENUE);
                    cppr.setMontant(riu.getMontantRetenu());
                    result.add(cppr);

                }

                else if (IRERetenues.CS_TYPE_COMPTE_SPECIAL.equals(riu.getCsTypeRetenue())) {

                    rubrique = (APIRubrique) process.initSessionOsiris().getAPIFor(APIRubrique.class);
                    rubrique.setIdRubrique(retenue.getIdRubrique());
                    rubrique.retrieve(transaction);

                    // On créé l'écriture...
                    riu.montantRetenu.negate();

                    process.getMemoryLog().logMessage(
                            doEcriture(session, process.initComptaExterne(transaction, true), ecriture.libelle, riu
                                    .getMontantRetenu().toString(), rubrique, compteAnnexe.getIdCompteAnnexe(),
                                    sectionStandard.getIdSection(), "01." + datePmtEnCours));

                    cppr = new RECumulPrstParRubrique();
                    cppr.setType(RECumulPrstParRubrique.TYPE_BLOCAGE_RETENUE);
                    cppr.setIdRubrique(rubrique.getIdRubrique());
                    cppr.setMontant(riu.getMontantRetenu());
                    result.add(cppr);

                } else if (IRERetenues.CS_TYPE_FACTURE_EXISTANTE.equals(riu.getCsTypeRetenue())) {

                    APIRubrique rubriqueCompensation = REModuleComptableFactory.getInstance().COMPENSATION;

                    // Compensation
                    riu.montantRetenu.negate();

                    process.getMemoryLog().logMessage(
                            doEcriture(session, process.initComptaExterne(transaction, true), ecriture.libelle,
                                    riu.montantRetenu.toString(), rubriqueCompensation,
                                    compteAnnexe.getIdCompteAnnexe(), sectionStandard.getIdSection(), "01."
                                            + datePmtEnCours));

                    cppr = new RECumulPrstParRubrique();
                    cppr.setType(RECumulPrstParRubrique.TYPE_BLOCAGE_RETENUE);
                    cppr.setIdRubrique(rubriqueCompensation.getIdRubrique());
                    cppr.setMontant(riu.getMontantRetenu());
                    result.add(cppr);

                    // c'est une facture existante
                    // récupération de la section et du cpt annexe de la facture
                    // existante...

                    APICompteAnnexe compteAnnexe2 = null;
                    APISection section2 = (APISection) process.initSessionOsiris().getAPIFor(APISection.class);

                    // En priorité, on recherche la section par idSection.

                    if (!JadeStringUtil.isBlankOrZero(retenue.getReferenceInterne())) {
                        section2.setIdSection(retenue.getReferenceInterne());
                        section2.retrieve(transaction);
                        compteAnnexe2 = process.initComptaExterne(transaction, true).getCompteAnnexeById(
                                section2.getIdCompteAnnexe());
                    } else {

                        String idRole = null;
                        if (retenue.getRole().equalsIgnoreCase(CaisseHelperFactory.CS_AFFILIE_PERSONNEL)) {
                            idRole = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                                    session.getApplication());
                        } else if (retenue.getRole().equalsIgnoreCase(CaisseHelperFactory.CS_AFFILIE_PARITAIRE)) {
                            idRole = CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                                    session.getApplication());
                        } else {
                            idRole = retenue.getRole();
                        }

                        CACompteAnnexeManager mgr = new CACompteAnnexeManager();
                        mgr.setSession(session);
                        mgr.setForIdExterneRole(retenue.getIdExterne());
                        mgr.setForIdRole(idRole);
                        mgr.find(transaction);

                        if (!mgr.isEmpty()) {
                            compteAnnexe2 = (CACompteAnnexe) mgr.getFirstEntity();
                        } else {
                            throw new Exception("Aucun compte annexe trouvé pour la retenue idRetenue/idExt "
                                    + retenue.getIdRetenue() + "/" + retenue.getIdExterne());
                        }

                        section2 = process.initComptaExterne(transaction, true).getSectionByIdExterne(
                                compteAnnexe2.getIdCompteAnnexe(), retenue.getIdTypeSection(), retenue.getNoFacture(),
                                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, null);

                    }

                    if ((section2 == null) || section2.isNew()) {
                        throw new Exception("Section not found for idSection : " + retenue.getReferenceInterne());
                    }

                    //

                    riu.montantRetenu.negate();
                    process.getMemoryLog()
                            .logMessage(
                                    doEcriture(session, process.initComptaExterne(transaction, true), ecriture.libelle,
                                            riu.montantRetenu.toString(), rubriqueCompensation,
                                            compteAnnexe2.getIdCompteAnnexe(), section2.getIdSection(), "01."
                                                    + datePmtEnCours));

                    cppr = new RECumulPrstParRubrique();
                    cppr.setType(RECumulPrstParRubrique.TYPE_BLOCAGE_RETENUE);
                    cppr.setIdRubrique(rubriqueCompensation.getIdRubrique());
                    cppr.setMontant(riu.getMontantRetenu());
                    result.add(cppr);

                }

                else if (IRERetenues.CS_TYPE_FACTURE_FUTURE.equals(riu.getCsTypeRetenue())) {

                    APIRubrique rubriqueCompensation = REModuleComptableFactory.getInstance().COMPENSATION;

                    // Compensation
                    riu.montantRetenu.negate();

                    process.getMemoryLog().logMessage(
                            doEcriture(session, process.initComptaExterne(transaction, true), ecriture.libelle,
                                    riu.montantRetenu.toString(), rubriqueCompensation,
                                    compteAnnexe.getIdCompteAnnexe(), sectionStandard.getIdSection(), "01."
                                            + datePmtEnCours));

                    cppr = new RECumulPrstParRubrique();
                    cppr.setType(RECumulPrstParRubrique.TYPE_BLOCAGE_RETENUE);
                    cppr.setIdRubrique(rubriqueCompensation.getIdRubrique());
                    cppr.setMontant(riu.getMontantRetenu());
                    result.add(cppr);

                    // Récupération du role. idRole correspond à un code
                    // système...
                    String idRole = null;
                    if (retenue.getRole().equalsIgnoreCase(CaisseHelperFactory.CS_AFFILIE_PERSONNEL)) {
                        idRole = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(session.getApplication());
                    } else if (retenue.getRole().equalsIgnoreCase(CaisseHelperFactory.CS_AFFILIE_PARITAIRE)) {
                        idRole = CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(session.getApplication());
                    } else {
                        idRole = retenue.getRole();
                    }

                    APICompteAnnexe compteAnnexeAffilie = process.initComptaExterne(transaction, true)
                            .getCompteAnnexeByRole(retenue.getReferenceInterne(), idRole, retenue.getIdExterne());

                    if (compteAnnexeAffilie == null) {
                        throw new Exception("Unable to retrieve compteAnnexe affilie. refInterne/idRole/idExterne = "
                                + retenue.getReferenceInterne() + "/" + idRole + "/" + retenue.getIdExterne());
                    }
                    riu.montantRetenu.negate();

                    APISection sectionCompensationFutures = process.initComptaExterne(transaction, true)
                            .getSectionByIdExterne(compteAnnexeAffilie.getIdCompteAnnexe(),
                                    APISection.ID_TYPE_SECTION_RENTE_AVS_AI,
                                    String.valueOf(new JADate(datePmtEnCours).getYear()) + "28000",
                                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, null);

                    if (sectionCompensationFutures == null) {
                        throw new Exception(
                                "Unable to retrieve sectionCompensationFutures. idCA/idTypeSct/noFacture = "
                                        + idCompteAnnexe + "/" + APISection.ID_TYPE_SECTION_RENTE_AVS_AI + "/"
                                        + String.valueOf(new JADate(datePmtEnCours).getYear()) + "28000");
                    }

                    process.getMemoryLog().logMessage(
                            doEcriture(session, process.initComptaExterne(transaction, true), ecriture.libelle,
                                    riu.montantRetenu.toString(), rubriqueCompensation,
                                    compteAnnexeAffilie.getIdCompteAnnexe(), sectionCompensationFutures.getIdSection(),
                                    "01." + datePmtEnCours));

                    cppr = new RECumulPrstParRubrique();
                    cppr.setType(RECumulPrstParRubrique.TYPE_BLOCAGE_RETENUE);
                    cppr.setIdRubrique(rubriqueCompensation.getIdRubrique());
                    cppr.setMontant(riu.getMontantRetenu());
                    result.add(cppr);

                }
            }
        }

        return result.toArray(new RECumulPrstParRubrique[result.size()]);
    }

    private RECumulPrstParRubrique doOperationsStandards(BSession session, BTransaction transaction,
            AREPmtMensuel process, APICompteAnnexe compteAnnexe, REEcritureUtil ecriture, String datePmtEnCours,
            APISection sectionStandard) throws Exception {

        REPrestationsAccordees ra = new REPrestationsAccordees();
        ra.setIdPrestationAccordee(ecriture.idRA);
        ra.setSession(session);
        ra.retrieve(transaction);
        PRAssert.notIsNew(ra, null);

        // On reset les flags isAttenteMaj....
        ra.setIsAttenteMajBlocage(Boolean.FALSE);
        ra.setIsAttenteMajRetenue(Boolean.FALSE);
        ra.update(transaction);

        APIRubrique rubrique = AREModuleComptable.getRubrique(ra.getCodePrestation(), ra.getSousTypeGenrePrestation(),
                AREModuleComptable.TYPE_RUBRIQUE_NORMAL);

        REInformationsComptabilite ic = ra.loadInformationsComptabilite();
        PRAssert.notIsNew(ic, null);

        if (compteAnnexe == null) {
            throw new Exception(
                    "Traitement retenue : Impossible de créer le compte annexe, contrôlez les logs pour plus de détails.");
        }

        // On créé l'écriture...
        process.getMemoryLog().logMessage(
                doEcriture(session, process.initComptaExterne(transaction, true), ecriture.libelle, ecriture.montant,
                        rubrique, compteAnnexe.getIdCompteAnnexe(), sectionStandard.getIdSection(), "01."
                                + datePmtEnCours));

        RECumulPrstParRubrique result = new RECumulPrstParRubrique();
        result.setIdRubrique(rubrique.getIdRubrique());
        result.setMontant(new FWCurrency(ecriture.montant));
        result.setType(RECumulPrstParRubrique.TYPE_BLOCAGE_RETENUE);

        return result;
    }

    /**
     * Effectue un ordre de versement, lance une Exception si le montant est négatif
     * 
     * @param compta
     * @param idCompteAnnexe
     * @param idSection
     * @param montant
     * @param idAdressePaiement
     * @param nssRequerant
     *            String, NSS du requérant principal. Cette valeur peut être null.
     * @throws Exception
     *             Si le montant est négatif
     * @throws IllegalArgumentException
     */
    protected BIMessage doOrdreVersement(BSession session, APIGestionComptabiliteExterne compta, String idCompteAnnexe,
            String idSection, String montant, String idAdressePaiement, String motifVersement, String dateComptable)
            throws Exception {

        if (new FWCurrency(montant).isNegative()) {
            throw new IllegalArgumentException("Montant négatif non accepté pour un ordre de versement");
        }

        if (new FWCurrency(montant).isZero()) {
            return null;
        }

        APIOperationOrdreVersement ordreVersement = compta.createOperationOrdreVersement();
        ordreVersement.setIdAdressePaiement(idAdressePaiement);
        ordreVersement.setDate(dateComptable);
        ordreVersement.setIdCompteAnnexe(idCompteAnnexe);
        ordreVersement.setIdSection(idSection);
        ordreVersement.setMontant(montant);
        ordreVersement.setCodeISOMonnaieBonification(session
                .getCode(IPRConstantesExternes.OSIRIS_CS_CODE_ISO_MONNAIE_CHF));
        ordreVersement.setCodeISOMonnaieDepot(session.getCode(IPRConstantesExternes.OSIRIS_CS_CODE_ISO_MONNAIE_CHF));
        ordreVersement.setTypeVirement(APIOperationOrdreVersement.VIREMENT);

        ordreVersement.setNatureOrdre(CAOrdreGroupe.NATURE_RENTES_AVS_AI);
        ordreVersement.setMotif(motifVersement);
        compta.addOperation(ordreVersement);

        return null;
    }

    /**
     * Génère les opérations comptable du groupe d'opération considéré, a savoir : ecritures comptables et ordre de
     * versement Les rentes accordées avec retenue ou blocage ne sont pas traitées ici !!! Cette méthode est 'atomique',
     * en cas d'erreur,
     * 
     * @param session
     * @param transaction
     * @param idAdrPmt
     * @param motifVersement
     * @param idOrganeExecution
     * @param idOperationOV
     * @throws Exception
     */
    public RECumulPrstParRubrique[] doTraitementComptable(AREPmtMensuel process, BSession session,
            BTransaction transaction, APIGestionRentesExterne compta, String motif, long idOperationEcritureVersement,
            long idOperationOV, long idSection, String datePmtEnCours, String nomCache, int sectionIncrement,
            String dateEcheance) throws Exception {

        boolean hasError = false;
        String errorMsg = null;

        Map<Integer, RECumulPrstParRubrique> result = new HashMap<Integer, RECumulPrstParRubrique>();

        try {

            if (isGroupOperationEnErreur()) {
                throw new Exception("Groupe d'opération en erreur.");
            }

            // Traitement des erreurs
            if (errorMsgPhasePreparation != null) {
                throw new Exception(errorMsgPhasePreparation);
            }

            if (JadeStringUtil.isIntegerEmpty(idCompteAnnexe)) {
                throw new Exception("Compte annexe non renseigné !");
            }

            if (JadeStringUtil.isBlankOrZero(ov.idAdrPmt) && ((ov.montant != null) && ov.montant.isPositive())) {
                throw new Exception("Aucune adresse de pmt trouvée pour idTiers : " + idTiersBeneficiaire);
            }

            if (motif == null) {
                throw new Exception("Genre de rente inconnu.");
            }

            /*
             * 
             * Traitement des RA standards
             */
            if (!isOperationSurJournalExterne) {

                // mm.aaaa
                JADate date = new JADate(process.getMoisPaiement());
                JACalendar cal = new JACalendarGregorian();
                String lastDayInMonth = String.valueOf(cal.daysInMonth(date.getMonth(), date.getYear()));

                compta.createSection(
                        session,
                        transaction,
                        idCompteAnnexe,
                        String.valueOf(idSection),
                        process.getNoSection(session, new JADate(datePmtEnCours), sectionIncrement),
                        PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(JACalendar.todayJJsMMsAAAA()),
                        PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(process.getDateEcheancePaiement()),
                        PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ("01." + process.getMoisPaiement()),
                        PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(lastDayInMonth + "."
                                + process.getMoisPaiement()), "-" + ov.montant.toString(), ov.montant.toString(),
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                        IPRConstantesExternes.TIERS_CS_TYPE_ADRESSE_COURRIER);

                for (REEcritureUtil ecriture : ecritures) {
                    if (ecriture.idRubrique == null) {
                        throw new Exception("Aucune rubrique trouvée pour idRA = " + ecriture.idRA);
                    }
                    // Si l'on corrige les erreurs...., il faut remettre a jours
                    // les RA
                    if (process instanceof REExecuterRentesEnErreurProcess) {
                        REPrestationsAccordees ra = new REPrestationsAccordees();
                        ra.setSession(session);
                        ra.setIdPrestationAccordee(ecriture.idRA);
                        ra.retrieve(transaction);
                        PRAssert.notIsNew(ra, null);

                        ra.setIsErreur(Boolean.FALSE);
                        ra.update(transaction);
                    }
                    // Cette liste contient les rentes std a verser uniquement,
                    // pas de retenues, ni de prestations bloquées
                    compta.addEcriture(session, transaction, String.valueOf(ecriture.idOperation), idCompteAnnexe,
                            String.valueOf(idSection), ecriture.idRubrique, idCompteCourant, "-" + ecriture.montant,
                            PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ("01." + process.getMoisPaiement()),
                            ecriture.libelle);

                    result = this.cumulParRubrique(result, RECumulPrstParRubrique.TYPE_STANDARD, ecriture.idRubrique,
                            new FWCurrency("-" + ecriture.montant));
                }
                if ((ov.montant != null) && ov.montant.isPositive()) {

                    // Maximum 40 char pour le nom cache !!!
                    if ((nomCache != null) && (nomCache.length() >= 40)) {
                        nomCache = nomCache.substring(0, 39);
                    }

                    compta.addVersement(session, transaction, String.valueOf(idOperationEcritureVersement),
                            idCompteAnnexe, String.valueOf(idSection), ov.idRubrique, idCompteCourant,
                            ov.montant.toString(),
                            PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(process.getDateEcheancePaiement()), "",
                            String.valueOf(idOperationOV), ov.idAdrPmt, process.getIdOrganeExecution(), motif, nomCache);

                    result = this.cumulParRubrique(result, RECumulPrstParRubrique.TYPE_STANDARD, ov.idRubrique,
                            ov.montant);
                }
            }
        } catch (Exception e) {
            hasError = true;
            errorMsg = e.toString();
        } finally {
            if ((transaction != null) && (hasError || transaction.hasErrors())) {
                hasError = true;
                if (transaction.hasErrors()) {
                    errorMsg = transaction.getErrors().toString();
                }
                transaction.rollback();
            }

            if (hasError) {
                result.clear();
                doTraitementDesErreurs(session, process, errorMsg);
                throw new Exception(errorMsg);
            }
        }

        try {
            /*
             * 
             * Traitement des écritures comptables dans un journal CA externe CAD traitement des RA bloquées et des
             * retenues Ce traitement s'opère par les interfaces standards de la compta auxiliaire
             */
            if (isOperationSurJournalExterne) {

                APICompteAnnexe compteAnnexe = process.initComptaExterne(transaction, true).getCompteAnnexeById(
                        idCompteAnnexe);

                APISection sectionStandard = process.initComptaExterne(transaction, true).getSectionByIdExterne(
                        idCompteAnnexe, APISection.ID_TYPE_SECTION_RENTE_AVS_AI,
                        process.getNoSection(session, new JADate(datePmtEnCours), sectionIncrement),
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, null);

                int count = 1;
                REPrestationsAccordees ra = null;
                PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTiersBeneficiaire);

                for (REEcritureUtil ecriture : ecritures) {

                    if (count == 1) {
                        // On récupère la 1ère ra trouvée pour ce groupe
                        // d'opération...
                        ra = new REPrestationsAccordees();
                        ra.setSession(session);
                        ra.setIdPrestationAccordee(ecriture.idRA);
                        ra.retrieve(transaction);
                    }
                    count++;

                    // Si l'on corrige les erreurs...., il faut remettre a jours
                    // les RA
                    if (process instanceof REExecuterRentesEnErreurProcess) {
                        REPrestationsAccordees elm = new REPrestationsAccordees();
                        elm.setSession(session);
                        elm.setIdPrestationAccordee(ecriture.idRA);
                        elm.retrieve(transaction);
                        PRAssert.notIsNew(elm, null);
                        elm.setIsErreur(Boolean.FALSE);
                        elm.update(transaction);
                    }

                    if (ecriture instanceof REEcriturePrstBloqueeUtil) {

                        result = this.cumulParRubrique(
                                result,
                                doOperationsBlocage(session, transaction, process, compta,
                                        tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), compteAnnexe,
                                        (REEcriturePrstBloqueeUtil) ecriture, datePmtEnCours));
                    } else if (ecriture instanceof REEcritureRetenueUtil) {

                        RECumulPrstParRubrique[] array = doOperationsRetenues(session, transaction, process,
                                compteAnnexe, (REEcritureRetenueUtil) ecriture, datePmtEnCours, tw, sectionStandard,
                                dateEcheance);
                        for (RECumulPrstParRubrique element : array) {
                            result = this.cumulParRubrique(result, element);
                        }

                    } else {
                        result = this.cumulParRubrique(
                                result,
                                doOperationsStandards(session, transaction, process, compteAnnexe, ecriture,
                                        datePmtEnCours, sectionStandard));
                    }
                }

                /*
                 * 
                 * Ordre de versement
                 */
                if ((ov.montant != null) && ov.montant.isPositive()) {
                    // Le versement...
                    String idTiersPrincipal = idTiersAdrPmt;

                    if (JadeStringUtil.isBlankOrZero(idTiersPrincipal) || Long.parseLong(idTiersPrincipal) < 0) {
                        idTiersPrincipal = idTiersBeneficiaire;
                    }

                    String isoLangFromIdTiers = PRTiersHelper.getIsoLangFromIdTiers(session, idTiersPrincipal);

                    doOrdreVersement(session, process.initComptaExterne(transaction, true),
                            compteAnnexe.getIdCompteAnnexe(), sectionStandard.getIdSection(), ov.montant.toString(),
                            ov.idAdrPmt, process.getMotifVersement(
                                    tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), datePmtEnCours,
                                    tw.getProperty(PRTiersWrapper.PROPERTY_NOM),
                                    tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM), ra.getReferencePmt(),
                                    ra.getCodePrestation(), isoLangFromIdTiers), process.getDateEcheancePaiement());

                    result = this.cumulParRubrique(result, RECumulPrstParRubrique.TYPE_STANDARD,
                            RECumulPrstParRubrique.RUBRIQUE_FICTIVE_OV_PMT_BLOCAGE_RETENUE, ov.montant);
                }
            }
            transaction.commit();

        } catch (Exception e) {
            hasError = true;
            errorMsg = e.toString();
        } finally {
            if (hasError || transaction.hasErrors()) {
                hasError = true;
                if (transaction.hasErrors()) {
                    errorMsg = transaction.getErrors().toString();
                }
                transaction.rollback();
            }

            if (hasError) {
                result.clear();
                doTraitementDesErreurs(session, process, errorMsg);
                if (transaction != null) {
                    transaction.clearErrorBuffer();
                }
                throw new Exception(errorMsg);
            }
        }
        return result.values().toArray(new RECumulPrstParRubrique[result.size()]);
    }

    /**
     * Cette méthode met en erreur toute les RA de ce groupe d'opération en erreur.
     * 
     * @throws Exception
     */
    private void doTraitementDesErreurs(BSession session, AREPmtMensuel process, String errorMsg) throws Exception {

        // On flag toutes les RA en erreurs...
        BITransaction transaction = session.newTransaction();
        try {

            transaction.openTransaction();
            for (REEcritureUtil ecriture : ecritures) {
                process.getMemoryLog().logMessage("Mise en erreur de la RA no " + ecriture.idRA,
                        FWMessage.AVERTISSEMENT, this.getClass().toString());
                process.getMemoryLog().logMessage("\t" + errorMsg, FWMessage.AVERTISSEMENT, this.getClass().toString());

                REPrestationsAccordees ra = new REPrestationsAccordees();
                ra.setSession(session);
                ra.setIdPrestationAccordee(ecriture.idRA);
                ra.retrieve(transaction);
                PRAssert.notIsNew(ra, "Trait. des erreurs, RA " + ecriture.idRA + " not found !!! ( "
                        + ecriture.libelle + " )");
                ra.setIsErreur(Boolean.TRUE);
                ra.update(transaction);

                if (transaction.hasErrors()) {
                    process.getMemoryLog().logMessage(FWViewBeanInterface.ERROR, transaction.getErrors().toString(),
                            this.getClass().toString());
                    throw new Exception();
                }
            }

            transaction.commit();

        } catch (Exception e) {
            process.getMemoryLog().logMessage(FWViewBeanInterface.ERROR, e.toString(), this.getClass().toString());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    public String getErrorMsgPhasePreparation() {
        return errorMsgPhasePreparation;
    }

    public void initOperation(BSession session, String idCompteAnnexe, String idCompteCourant,
            String idTiersBeneficiaire, String idRubriqueOV, String idTiersAdrPmt, String idAdrPmt) throws Exception {

        // si compte annexe différent du précédant, on ne garde pas la section de blocage
        if ((this.idCompteAnnexe != null) && !this.idCompteAnnexe.equals(idCompteAnnexe)) {
            sectionBlocage = null;
        }

        this.idCompteAnnexe = idCompteAnnexe;
        this.idCompteCourant = idCompteCourant;
        this.idTiersBeneficiaire = idTiersBeneficiaire;
        this.idTiersAdrPmt = idTiersAdrPmt;

        if (ov == null) {
            ov = new OrdreVersement();
        }

        ov.idRubrique = idRubriqueOV;
        ov.idAdrPmt = idAdrPmt;
        isOperationSurJournalExterne = false;
    }

    public boolean isGroupOperationEnErreur() {
        return isGroupOperationEnErreur;
    }

    /**
     * charge l'adresse de paiement.
     * 
     * @return une adresse de paiement ou null.
     * @throws Exception
     *             DOCUMENT ME!
     */
    public TIAdressePaiementData loadAdressePaiement(BSession session, BTransaction transaction,
            String dateValeurCompta, String idTiersAdressePaiement, String idDomaine) throws Exception {

        TIAdressePaiementData retValue = PRTiersHelper.getAdressePaiementData(session, transaction,
                idTiersAdressePaiement, idDomaine, "", dateValeurCompta);

        return retValue;
    }

    public void setErrorMsgPhasePreparation(String errorMsgPhasePreparation) {
        this.errorMsgPhasePreparation = errorMsgPhasePreparation;
    }

    public void setGroupOperationEnErreur(boolean isGroupOperationEnErreur) {
        this.isGroupOperationEnErreur = isGroupOperationEnErreur;
    }

    /**
     * Retourne la liste des retenues actives (id + montant).
     * 
     * @param session
     * @param transaction
     * @param rente
     * @return
     * @throws Exception
     */
    private List<RERetenueInfoUtil> traitementRetenues(BSession session, REPaiementRentes rente, String dateDernierPmt,
            String dateMoisEnCours, boolean isTraitementPAEnERreur) throws Exception {

        // On récupère toutes les retenues de cette RA... même les inactives...
        RERetenuesPaiementManager mgr = new RERetenuesPaiementManager();
        mgr.setSession(session);
        mgr.setForIdRenteAccordee(rente.getIdRenteAccordee());

        REPrestationsAccordees renteAccordee = new REPrestationsAccordees();
        renteAccordee.setSession(session);
        renteAccordee.setIdPrestationAccordee(rente.getIdRenteAccordee());
        renteAccordee.retrieve();
        PRAssert.notIsNew(renteAccordee, null);

        BStatement statement = null;
        BTransaction transaction = (BTransaction) session.newTransaction();
        List<RERetenueInfoUtil> result = new ArrayList<RERetenueInfoUtil>();
        try {
            transaction.openTransaction();
            statement = mgr.cursorOpen(transaction);
            RERetenuesPaiement retenue = null;

            while ((retenue = (RERetenuesPaiement) mgr.cursorReadNext(statement)) != null) {

                // maj du montant restant et de la date de fin, le cas échéant
                // On contrôle que la retenue soit active...
                if (RERetenuesUtil.isRetenueActive(session, retenue, isTraitementPAEnERreur)) {

                    FWCurrency montantARetenir = new FWCurrency(retenue.getMontantRetenuMensuel());
                    FWCurrency montantRestant = new FWCurrency(RERetenuesUtil.getMontantRestant(retenue).toString());
                    FWCurrency montantRetenu = new FWCurrency("0");

                    // Cas spécial des IS
                    if (IRERetenues.CS_TYPE_IMPOT_SOURCE.equals(retenue.getCsTypeRetenue())) {

                        // Trois cas possible :
                        // cas a) MontantARetenir > 0 && (dateFin vide ou > dateDernierPmt)
                        // cas b) Canton > 0 && taux vide && (dateFin vide ou > dateDernierPmt)
                        // cas c) Taux > 0 && canton && (dateFin vide ou > dateDernierPmt)

                        JADate dfr = null;
                        if ((retenue.getDateFinRetenue() != null)
                                && !JadeStringUtil.isBlankOrZero(retenue.getDateFinRetenue())) {
                            dfr = new JADate(retenue.getDateFinRetenue());
                        } else {
                            dfr = new JADate("31.12.2999");
                        }

                        JACalendar cal = new JACalendarGregorian();

                        JADate ddp = new JADate(dateDernierPmt);

                        // cas a)
                        if ((montantARetenir != null) && montantARetenir.isPositive()
                                && (cal.compare(dfr, ddp) == JACalendar.COMPARE_FIRSTUPPER)) {
                            montantRetenu = new FWCurrency(montantARetenir.toString());
                        }
                        // cas b)
                        else if (!JadeNumericUtil.isEmptyOrZero(retenue.getCantonImposition())
                                && JadeNumericUtil.isEmptyOrZero(retenue.getTauxImposition())
                                && (cal.compare(dfr, ddp) == JACalendar.COMPARE_FIRSTUPPER)) {

                            // Calcul du montant a retenir...
                            // Recherche du barême...
                            PRTauxImpositionManager tisMgr = new PRTauxImpositionManager();
                            tisMgr.setSession(session);
                            tisMgr.setForCsCanton(retenue.getCantonImposition());
                            tisMgr.setForPeriode("01." + dateMoisEnCours, "01." + dateMoisEnCours);
                            tisMgr.setForTypeImpot(IPRTauxImposition.CS_TARIF_D);
                            tisMgr.find(transaction);
                            if (!tisMgr.isEmpty()) {

                                PRTauxImposition tis = (PRTauxImposition) tisMgr.getFirstEntity();

                                montantRetenu = new FWCurrency(
                                        (new FWCurrency(renteAccordee.getMontantPrestation()).floatValue() / 100)
                                                * (new FWCurrency(tis.getTaux())).floatValue());
                                montantRetenu.round(FWCurrency.ROUND_ENTIER);
                            } else {
                                throw new Exception("Aucun taux d'imposition trouvé pour csCanton/idRA = "
                                        + retenue.getIdRenteAccordee() + "/" + retenue.getCantonImposition());
                            }

                        }
                        // cas c)
                        else if (!JadeNumericUtil.isEmptyOrZero(retenue.getCantonImposition())
                                && !JadeNumericUtil.isEmptyOrZero(retenue.getTauxImposition())
                                && (cal.compare(dfr, ddp) == JACalendar.COMPARE_FIRSTUPPER)) {

                            montantRetenu = new FWCurrency(
                                    (new FWCurrency(renteAccordee.getMontantPrestation()).floatValue() / 100)
                                            * (new FWCurrency(retenue.getTauxImposition())).floatValue());
                            montantRetenu.round(FWCurrency.ROUND_ENTIER);
                        } else {
                            throw new Exception("Les données saisies pour la retenue sur IS de idRA = "
                                    + renteAccordee.getIdPrestationAccordee() + "ne sont pas cohérente");

                        }

                    } else {

                        // On prend le plus petit des deux...
                        if (montantRestant.compareTo(montantARetenir) == -1) {
                            montantRetenu.add(montantRestant.toString());
                        } else {
                            montantRetenu.add(montantARetenir.toString());
                        }
                    }

                    RERetenueInfoUtil riu = new RERetenueInfoUtil(retenue.getCsTypeRetenue(), retenue.getIdRetenue(),
                            montantRetenu);
                    result.add(riu);
                }
            }
        } finally {
            transaction.closeTransaction();
        }
        return result;

    }

    /**
     * @param session
     * @param transaction
     * @param rente
     * @param libelle
     * @param idOperation
     * @param idRubrique
     * @param dateDernierPmt
     * @throws Exception
     */
    public void traiterEcriture(BSession session, REPaiementRentes rente, String libelle, long idOperation,
            String idRubrique, String dateDernierPmt, String dateMoisEnCours, boolean isTraitementPAEnErreur) {

        try {
            if ((libelle != null) && (libelle.length() > 40)) {
                libelle = libelle.substring(0, 39);
            }

            // Les rentes bloquees et/ou avec retenues sont regroupée de la même
            // manières que les autres,
            // cad sur le même CA si même adr. pmt et groupe
            REEcritureUtil ecr = null;

            /*
             * 
             * Initialisation de l'ordre de versement
             */
            // Pas encore initialisé...
            // 1ère écriture, cad celle du groupe principal
            if (ov == null) {
                ov = new OrdreVersement();
            } else {
                // qqes checks... car pour un même groupe, tous les éléments
                // suivants doivent conincider
                if (!idTiersAdrPmt.equals(rente.getIdTiersAdressePmt())) {
                    throw new Exception(
                            "Erreur dans la génération du group d'opération. idTiersAdrPmt1/idTiersAdrPmt2/idRenteAccordee/nssTiersBeneficiaire = "
                                    + idTiersAdrPmt + "/" + rente.getIdTiersAdressePmt() + "/"
                                    + rente.getIdRenteAccordee() + rente.getNssTBE());
                }
            }

            /*
             * 
             * Identification des prestations bloquées
             * 
             * Le blocage à la priorité sur les retenues, cad si retenues sur une RA bloquée, elle sera traiter comme du
             * blocage uniquement.
             */

            if ((rente.getIsPrestationBloquee() != null) && rente.getIsPrestationBloquee().booleanValue()
                    && (rente.getIsAttenteMajBlocage() != null) && rente.getIsAttenteMajBlocage().booleanValue()) {

                isOperationSurJournalExterne = true;
                ecr = new REEcriturePrstBloqueeUtil();
                ecr.libelle = libelle;
                ecr.montant = rente.getMontant();
                ecr.idOperation = idOperation;
                ecr.idRA = rente.getIdRenteAccordee();
                ecr.idRubrique = idRubrique;
                ecritures.add(ecr);

                // Pas de maj du montant de l'OV
            }

            /*
             * 
             * 
             * Identification des retenues
             */
            else if ((rente.getIsRetenue() != null) && rente.getIsRetenue().booleanValue()
                    && (rente.getIsAttenteMajRetenue() != null) && rente.getIsAttenteMajRetenue().booleanValue()) {

                isOperationSurJournalExterne = true;

                List<RERetenueInfoUtil> retenues = traitementRetenues(session, rente, dateDernierPmt, dateMoisEnCours,
                        isTraitementPAEnErreur);
                ecr = new REEcritureRetenueUtil();
                ecr.libelle = libelle;
                ecr.montant = rente.getMontant();
                ecr.idOperation = idOperation;
                ecr.idRA = rente.getIdRenteAccordee();
                ecr.idRubrique = idRubrique;
                ((REEcritureRetenueUtil) ecr).addRetenues(retenues);
                ecritures.add(ecr);

                // Il faut soustraire au montant de l'OV, le cumul des retenues
                FWCurrency montantVersement = new FWCurrency(rente.getMontant());
                for (RERetenueInfoUtil riu : retenues) {
                    montantVersement.sub(riu.montantRetenu);
                }
                // MAJ du montant de l'ordre de versement
                ov.montant.add(montantVersement);

            }

            // Prestation standard, cad sans retenues ni blocage
            else {
                REEcritureUtil ecrit = new REEcritureUtil();
                ecrit.libelle = libelle;
                ecrit.montant = rente.getMontant();
                ecrit.idOperation = idOperation;
                ecrit.idRA = rente.getIdRenteAccordee();
                ecrit.idRubrique = idRubrique;
                ecritures.add(ecrit);

                // MAJ du montant de l'ordre de versement
                ov.montant.add(rente.getMontant());

            }
        } catch (Exception e) {
            errorMsgPhasePreparation = "Error : " + e.toString();
        }
    }
}
