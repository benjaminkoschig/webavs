package globaz.apg.utils;

import ch.globaz.common.util.CaisseInfoPropertiesWrapper;
import globaz.apg.ApgServiceLocator;
import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.business.service.APAnnoncesRapgService;
import globaz.apg.db.annonces.APAnnonceAPG;
import globaz.apg.db.annonces.APBreakRule;
import globaz.apg.db.annonces.APBreakRuleManager;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APDroitPandemie;
import globaz.apg.db.droits.APDroitPaternite;
import globaz.apg.db.droits.APDroitPaterniteJointTiers;
import globaz.apg.db.droits.APDroitPaterniteJointTiersManager;
import globaz.apg.db.droits.APDroitProcheAidant;
import globaz.apg.db.droits.APEnfantAPG;
import globaz.apg.db.droits.APEnfantAPGManager;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.apg.db.droits.APSituationFamilialeAPG;
import globaz.apg.db.droits.APSituationFamilialePat;
import globaz.apg.db.droits.APSituationFamilialePatManager;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationJointLotTiersDroit;
import globaz.apg.db.prestation.APPrestationJointLotTiersDroitManager;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.db.prestation.APRepartitionPaiementsManager;
import globaz.apg.enums.APAllPlausibiliteRules;
import globaz.apg.enums.APBreakableRules;
import globaz.apg.enums.APTypeActiviteProfessionnel;
import globaz.apg.enums.APTypeVersement;
import globaz.apg.exceptions.APEntityNotFoundException;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.properties.APParameter;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class APGenerateurAnnonceRAPG {
    private final int NB_JOURS_PATERNITE_14 = 14;
    private final int NB_JOURS_PATERNITE_7 = 7;

    public APAnnonceAPG createAnnonceSedex(BSession session, APPrestation prestation, APDroitLAPG droit,
            String moisAnneeComptable, Boolean hasComplementCIAB) throws Exception {
        boolean isPrestationMaternite = APGUtils.isTypeMaternite(droit.getGenreService());
        boolean isPrestationPaternite = APGUtils.isTypePaternite(droit.getGenreService());
        // On va rechercher le bon type de droit
        String idDroit = droit.getIdDroit();
        if (droit instanceof APDroitPandemie) {
            droit = new APDroitPandemie();
            droit.setSession(session);
            droit.setIdDroit(idDroit);
            droit.retrieve();
        } else if (droit instanceof APDroitPaternite) {
            droit = new APDroitPaternite();
            droit.setSession(session);
            droit.setIdDroit(idDroit);
            droit.retrieve();
        } else if (droit instanceof APDroitProcheAidant) {
            droit = new APDroitProcheAidant();
            droit.setSession(session);
            droit.setIdDroit(idDroit);
            droit.retrieve();
       } else if (isPrestationMaternite) {
            droit = new APDroitMaternite();
            droit.setSession(session);
            droit.setIdDroit(idDroit);
            droit.retrieve();
        } else {
            droit = new APDroitAPG();
            droit.setSession(session);
            droit.setIdDroit(idDroit);
            droit.retrieve();
        }
        if (droit.isNew()) {
            throw new APEntityNotFoundException(APDroitLAPG.class, idDroit);
        }
        // creation de l'annonce
        final APAnnonceAPG annonceACreer = new APAnnonceAPG();
        annonceACreer.setSession(session);
        annonceACreer.setTimeStamp(JadeDateUtil.getCurrentTime().toString());
        annonceACreer.setTypeAnnonce(IAPAnnonce.CS_APGSEDEX);
        annonceACreer.setNumeroCaisse(CaisseHelperFactory.getInstance().getNoCaisse(session.getApplication()));
        annonceACreer.setNumeroAgence(CaisseHelperFactory.getInstance().getNoAgence(session.getApplication()));
        annonceACreer.setMoisAnneeComptable(moisAnneeComptable);
        annonceACreer.setHasComplementCIAB( hasComplementCIAB? "1" : "0");
        annonceACreer.setContenuAnnonce(session.getCode(prestation.getContenuAnnonce()));

        // LGA 06.09.2013 CE TRAITEMENT N'A PLUS LIEU D'?TRE SUITE AU MANDAT IR D0005 POINT 5.3.1.8 SAUF EN CAS DE
        // PRESTATION MATERNITE
        // // Si c'est une prestation de type 1, voir si c'est la premi?re prestation vers?e ou non
        // // Si c'est pas la premi?re, il faut la mettre en type 3
        if (isPrestationMaternite && "1".equals(annonceACreer.getContenuAnnonce())) {
            if (!JadeDateUtil.areDatesEquals(droit.getDateDebutDroit(), prestation.getDateDebut())) {
                // Mettre le type d'annonce ? 3, pour les suivantes (plus ? 1)
                annonceACreer.setContenuAnnonce("3");
            }
        }
        if(isPrestationPaternite && "1".equals(annonceACreer.getContenuAnnonce())){
            APDroitPaterniteJointTiersManager manager2 = new APDroitPaterniteJointTiersManager();
            manager2.setSession(session);
            manager2.setForIdDroit(idDroit);
            manager2.find();
            String dateDebut1erPeriode = "";
            for (int i = 0; i < manager2.size(); i++) {
                APDroitPaterniteJointTiers droitTiers = ( APDroitPaterniteJointTiers) manager2.get(i);
                for(APPeriodeAPG periodePat : droitTiers.getPeriodes()) {
                    if(JadeStringUtil.isBlankOrZero(dateDebut1erPeriode) ||JadeDateUtil.isDateBefore(periodePat.getDateDebutPeriode(),dateDebut1erPeriode) ){
                        dateDebut1erPeriode = periodePat.getDateDebutPeriode();
                    }
                }
            }
            if (!JadeDateUtil.areDatesEquals(dateDebut1erPeriode, prestation.getDateDebut())) {
                // Mettre le type d'annonce ? 3, pour les suivantes (plus ? 1)
                annonceACreer.setContenuAnnonce("3");
            }
        }

        // Champs non modifiables --> Calcul
        FWCurrency montantJournalierPrestation = new FWCurrency(prestation.getMontantJournalier());
        annonceACreer.setNombreJoursService(prestation.getNombreJoursSoldes());

        // Gestion du taux journalier si droit acquis
        if (!JadeStringUtil.isBlankOrZero(prestation.getDroitAcquis())) {
            // Si c'est une prestation maternit? on ne s'occupe pas du nombre d'enfants -> g?r? dans la m?thode
            int nombreEnfant = Integer.valueOf(getNombreEnfant(session, droit, prestation.getDateDebut()));

            BigDecimal montantPrestation = new BigDecimal(prestation.getBasicDailyAmount());

            BigDecimal montantMaxDroitAcquis = getMontantMaxDroitAcquis(session, nombreEnfant,
                    prestation.getDateDebut());
            if (montantPrestation.compareTo(montantMaxDroitAcquis) == 1) {
                annonceACreer.setTauxJournalierAllocationBase(montantMaxDroitAcquis.toString());
            } else {
                annonceACreer.setTauxJournalierAllocationBase(montantPrestation.toString());
            }

        } else {
            annonceACreer.setTauxJournalierAllocationBase(prestation.getBasicDailyAmount());
        }

        BigDecimal montantTotalAPG = new BigDecimal(montantJournalierPrestation.toString())
                .multiply(new BigDecimal(annonceACreer.getNombreJoursService()));
        if (JadeStringUtil.isIntegerEmpty(prestation.getMontantAllocationExploitation())) {
            annonceACreer.setIsAllocationExploitation("0");
        } else {
            annonceACreer.setIsAllocationExploitation("1");
        }
        if (JadeStringUtil.isDecimalEmpty(droit.getDroitAcquis())) {
            annonceACreer.setGarantieIJ("0");
        } else {
            annonceACreer.setGarantieIJ("1");
        }
        FWCurrency montantFraisGarde = new FWCurrency(prestation.getFraisGarde());

        annonceACreer.setIsAllocationFraisGarde(montantFraisGarde.isZero() ? "0" : "1");
        annonceACreer.setMontantAllocationFraisGarde(montantFraisGarde.toString());

        // si les frais de garde ne sont renseignes, on les ajoutes au montant
        // total des APG
        if (!montantFraisGarde.isZero()) {
            montantTotalAPG = montantTotalAPG.add(new BigDecimal(montantFraisGarde.toString()));
        }
        if(IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT.equals(droit.getGenreService())
            && montantTotalAPG.compareTo(new BigDecimal(prestation.getMontantBrut())) != 0) {
            montantTotalAPG = new BigDecimal(prestation.getMontantBrut());
        }
        annonceACreer.setTotalAPG(montantTotalAPG.toString());
        annonceACreer.setEventDate(prestation.getDateDebut());
        annonceACreer.setGenre(session.getCode(droit.getGenreService()));

        if ("1".equals(annonceACreer.getContenuAnnonce()) || "2".equals(annonceACreer.getContenuAnnonce())) {
            annonceACreer.setInsurantDomicileCountry(PRACORConst.csEtatToAcor(droit.getPays()));
            annonceACreer.setNumeroAssure(
                    droit.loadDemande().loadTiers().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
            annonceACreer.setEtatCivil(getCodeEtatCivil(session, droit));
            annonceACreer.setGenreActivite(getGenreActivite(session, prestation));
            annonceACreer.setRevenuMoyenDeterminant(prestation.getRevenuMoyenDeterminant());
            annonceACreer.setPeriodeDe(prestation.getDateDebut());
            annonceACreer.setPeriodeA(prestation.getDateFin());
            annonceACreer.setModePaiement(getModePaiement(session, prestation));

            if (droit instanceof APDroitAPG) {
                annonceACreer.setNumeroControle(((APDroitAPG) droit).getNoControlePers());
                // Rule 301 si service type = 90, numberOfChidren doit ?tre vide ou non renseign?
                annonceACreer.setNumeroCompte(((APDroitAPG) droit).getNoCompte());
                annonceACreer.setNombreEnfants(getNombreEnfant(session, droit, prestation.getDateDebut()));
            }

            if (!JadeStringUtil.isIntegerEmpty(droit.getNpa())) {
                annonceACreer
                        .setCantonEtat(PRACORConst.csCantonToAcor(PRTiersHelper.getCanton(session, droit.getNpa())));
            }
        } else if ("4".equals(annonceACreer.getContenuAnnonce())) {
            annonceACreer.setNombreJoursService(null);
            annonceACreer.setTauxJournalierAllocationBase(null);

        } else if ("3".equals(annonceACreer.getContenuAnnonce())) {
            annonceACreer.setPeriodeDe(prestation.getDateDebut());
            annonceACreer.setPeriodeA(prestation.getDateFin());
            annonceACreer.setTauxJournalierAllocationBase(null);
            annonceACreer.setNumeroAssure(null);
        }

        // Champs RAPG
        Set<String> breakRules = new HashSet<String>();
        // BreakRules
        APBreakRuleManager brManager = new APBreakRuleManager();
        brManager.setSession(session);
        brManager.setForIdDroit(droit.getIdDroit());
        brManager.find();
        @SuppressWarnings("unchecked")
        Iterator<APBreakRule> it = brManager.iterator();
        while (it.hasNext()) {
            String code = it.next().getBreakRuleCode();
            // Si c'est une annonce de type 1, pas de breakRule 509 sinon l'annonce reviendra en retour (Plausi 318)
            boolean nePasInserer = "1".equals(annonceACreer.getContenuAnnonce())
                    && APAllPlausibiliteRules.R_509.getCodeAsString().equals(code);
            if (!nePasInserer) {
                breakRules.add(code);
            }
        }
        // Ajouter la breakRules 505 si c'est un duplicata
        if ("2".equals(annonceACreer.getContenuAnnonce())) {
            breakRules.add("505");
        }

        // type 1 : prestation initial
        // type 2 : duplicata
        // type 3 : annonce corrective compl?ment
        // type 4 : annonce corrective restitution

        // BusinessProcessId
        if ("4".equals(annonceACreer.getContenuAnnonce()) || "3".equals(annonceACreer.getContenuAnnonce())) {
            // Retrouver l'ancienne prestation
            APPrestationManager prestManager = new APPrestationManager();
            if ("3".equals(annonceACreer.getContenuAnnonce())) {
                // Pour les prestations mat
                prestManager.setForIdDroit(droit.getIdDroit());
                // Il faut qu'on retrouve la premi?re
                prestManager.setForContenuAnnonce(IAPAnnonce.CS_DEMANDE_ALLOCATION);
            } else {
                // On est dans une type 4, il nous faut retrouver la prestation corrig?e
                prestManager.setForIdRestitution(prestation.getIdPrestation());
            }
            prestManager.setSession(session);
            prestManager.setOrderBy(
                    APPrestation.FIELDNAME_DATEDEBUT + " asc, " + APPrestation.FIELDNAME_IDANNONCE + " asc ");
            prestManager.find(BManager.SIZE_NOLIMIT);
            // Si il n'y a rien on essaie peut-?tre que c'?tait un duplicata (seulement dans le cas des mat)
            if (prestManager.getSize() == 0) {
                prestManager.setForContenuAnnonce(IAPAnnonce.CS_DUPLICATA);
                prestManager.find(BManager.SIZE_NOLIMIT);
            }

            boolean annonceEnHistorique = false;
            if (prestManager.getSize() > 0) {
                APPrestation prestBase = (APPrestation) prestManager.getFirstEntity();
                APAnnonceAPG oldAnnonce = new APAnnonceAPG();
                oldAnnonce.setIdAnnonce(prestBase.getIdAnnonce());
                oldAnnonce.setSession(session);
                oldAnnonce.retrieve();

                // Si c'est une annonce sedex, on a trouv? le bpid si non c'est une annonce en historique
                if (IAPAnnonce.CS_APGSEDEX.equals(oldAnnonce.getTypeAnnonce())) {
                    annonceACreer.setBusinessProcessId(oldAnnonce.getBusinessProcessId());

                    // Pour les cas mat (type 3) il faut reprendre la date de d?but du premier droit
                    if ("3".equals(annonceACreer.getContenuAnnonce())) {
                        annonceACreer.setPeriodeDe(prestBase.getDateDebut());
                    }
                    if(JadeStringUtil.isBlankOrZero(oldAnnonce.getBusinessProcessId())){
                        annonceACreer.setBusinessProcessId(prestBase.getIdPrestation());
                    }
                    annonceACreer.setTauxJournalierAllocationBase(calculTauxSiTauxDifferent(prestation, prestManager));
                } else {
                    annonceEnHistorique = true;
                }
            } else if (isPrestationMaternite) {
                // C'est une annonce mat?rnit? dont l'initiale ?tait en historique
                annonceEnHistorique = true;
            } else {
                throw new Exception(
                        "Impossible de retrouver le businessProcessId pour l'annonce de r?stitution ! (idPrestation="
                                + prestation.getIdPrestation() + " )");
            }
            if (annonceEnHistorique) {
                breakRules.add("600");
                annonceACreer.setNumeroAssure(
                        droit.loadDemande().loadTiers().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                annonceACreer.setMoisAnneeComptable(moisAnneeComptable);
                annonceACreer.setTauxJournalierAllocationBase(montantJournalierPrestation.toString());
                annonceACreer.setPeriodeDe(prestation.getDateDebut());
                annonceACreer.setPeriodeA(prestation.getDateFin());
                if ("4".equals(annonceACreer.getContenuAnnonce())) {
                    Integer joursSoldesNegatifs = new Integer(prestation.getNombreJoursSoldes());
                    if (joursSoldesNegatifs > 0) {
                        joursSoldesNegatifs = joursSoldesNegatifs * -1;
                    }
                    annonceACreer.setNombreJoursService(joursSoldesNegatifs.toString());
                } else {
                    annonceACreer.setNombreJoursService(prestation.getNombreJoursSoldes());
                }
            }

        } else {
            annonceACreer.setBusinessProcessId(prestation.getIdPrestation());
        }
        // Pr?paration de la string des breakRules
        String breakRulesString = "";
        ArrayList<String> breakableRules = new ArrayList<String>();
        for (APBreakableRules br : APBreakableRules.values()) {
            breakableRules.add(br.toString().replace("R_", ""));
        }
        for (String s : breakRules) {
            if (breakableRules.contains(s)) {
                breakRulesString += s + ",";
            }
        }
        if (breakRulesString.length() > 1) {
            breakRulesString = breakRulesString.substring(0, breakRulesString.length() - 1);
        }
        annonceACreer.setBreakRules(breakRulesString);

        annonceACreer.setMessageType(APAnnoncesRapgService.messageType);
        if ("1".equals(annonceACreer.getContenuAnnonce()) || "2".equals(annonceACreer.getContenuAnnonce())) {
            annonceACreer.setSubMessageType(APAnnoncesRapgService.subMessageType1);
            // Vu que les duplicatas s'annoncent avec une action 1 on met a 1 dans tous les cas
            annonceACreer.setContenuAnnonce("1");
        } else if ("3".equals(annonceACreer.getContenuAnnonce())) {
            annonceACreer.setSubMessageType(APAnnoncesRapgService.subMessageType3);
            // Vu qu'il n'y a plus de type 3 on passe en 4
            annonceACreer.setContenuAnnonce("4");
        } else if ("4".equals(annonceACreer.getContenuAnnonce())) {
            annonceACreer.setSubMessageType(APAnnoncesRapgService.subMessageType4);
        } else {
            throw new Exception("Le type d'annonce n'a pas pu ?tre d?fini");
        }

        /**
         * Paternit?
         */
        if(droit instanceof APDroitPaternite){
            annonceACreer.setTypePaternite(getTypePaternite(annonceACreer.getNumeroAssure(), idDroit, session));
            setDataEnfant(annonceACreer, idDroit, session);
        }

        if(droit instanceof APDroitProcheAidant){
            APDroitProcheAidant droitProcheAidant =(APDroitProcheAidant)droit;
            APSituationFamilialePatManager manager = new APSituationFamilialePatManager();
            manager.setSession(session);
            manager.setForIdDroitPaternite(idDroit);
            manager.find(BManager.SIZE_NOLIMIT);
            manager.<APSituationFamilialePat>getContainerAsList().stream().findFirst()
                   .ifPresent(enfant->{
                       annonceACreer.setNssEnfantOldestDroit(enfant.getNoAVS());
                       annonceACreer.setPaysNaissanceEnfant(enfant.getNationalite());
                       annonceACreer.setCantonNaissanceEnfant(PRACORConst.csCantonToAcor(enfant.getCanton()));
                       annonceACreer.setDateNaissanceEnfant(enfant.getDateNaissance());
                   });
            annonceACreer.setCareLeaveEventID(CaisseInfoPropertiesWrapper.noCaisseNoAgence()+droitProcheAidant.getCareLeaveEventID());
        }

        return annonceACreer;
    }

    private String calculTauxSiTauxDifferent(APPrestation prestation, APPrestationManager prestManager) throws Exception {
        if(prestManager.size() == 0) {
            return null;
        }
        APPrestation prestBase = (APPrestation) prestManager.getFirstEntity();
        if(!prestBase.getMontantJournalier().equals(prestation.getMontantJournalier())) {
            List<APPrestation> listPrestation = prestManager.getContainerAsList();
            List<APPrestation> listPrestationVersee = new ArrayList<>();
            BigDecimal totalBasicDailyAmount = BigDecimal.ZERO;
            for(APPrestation prest : listPrestation) {
                APAnnonceAPG oldAnnonce = new APAnnonceAPG();
                oldAnnonce.setIdAnnonce(prest.getIdAnnonce());
                oldAnnonce.setSession(prestManager.getSession());
                oldAnnonce.retrieve();
                if(!oldAnnonce.isNew()) {
                    listPrestationVersee.add(prest);
                }
                if(!JadeStringUtil.isBlankOrZero(oldAnnonce.getTauxJournalierAllocationBase())) {
                    totalBasicDailyAmount = totalBasicDailyAmount.add(new BigDecimal(oldAnnonce.getTauxJournalierAllocationBase()));
                }
            }
            listPrestationVersee.add(prestation);
            BigDecimal totalPrestationVerse = listPrestationVersee.stream().map(p -> new BigDecimal(p.getMontantBrut())).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalNombreJours = listPrestationVersee.stream().map(p -> new BigDecimal(p.getNombreJoursSoldes())).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalJournalier = totalPrestationVerse.divide(totalNombreJours, 2, RoundingMode.HALF_UP);
            return totalJournalier.subtract(totalBasicDailyAmount).toString();
        }
        return null;
    }

    private String getTypePaternite(String nss, String idDroit, BSession session) throws Exception {
        String typePaternite = "";
        List<APPeriodeAPG> periodes = ApgServiceLocator.getEntityService().getPeriodesDuDroitAPG(session,
                session.getCurrentThreadTransaction(), idDroit);
        int nombreJours = 0;
        int nombrePeriode = periodes.size();
        boolean HasPeriode7Jours = false;
        for (APPeriodeAPG periodeAPG : periodes) {
            nombreJours += Integer.parseInt(periodeAPG.getNbrJours());
            if (Integer.parseInt(periodeAPG.getNbrJours())== NB_JOURS_PATERNITE_7) {
                HasPeriode7Jours = true;
            }
        }

        if (nombrePeriode == 1 && nombreJours == NB_JOURS_PATERNITE_14) {
            return "1";
        }
        if (nombrePeriode == 2 && nombreJours == NB_JOURS_PATERNITE_7) {
            return "2";
        }
        if (nombrePeriode >= 2 && HasPeriode7Jours) {
            return "4";
        }
        return "3";

    }

    private APAnnonceAPG setDataEnfant(APAnnonceAPG annonceACreer, String idDroit, BSession session) throws Exception {
        APSituationFamilialePatManager manager = new APSituationFamilialePatManager();
        manager.setSession(session);
        manager.setForIdDroitPaternite(idDroit);
        manager.find(BManager.SIZE_NOLIMIT);
        APSituationFamilialePat enfant = null;
        String dateNaissance = "";
        for (APSituationFamilialePat situationFamilialePat : (List<APSituationFamilialePat>) manager.getContainer()) {
            if (JadeStringUtil.isBlankOrZero(dateNaissance)
                    || JadeDateUtil.isDateBefore(situationFamilialePat.getDateNaissance(), dateNaissance)) {
                enfant = situationFamilialePat;
            }
        }
        annonceACreer.setPaysNaissanceEnfant(enfant.getNationalite());
        annonceACreer.setCantonNaissanceEnfant(PRACORConst.csCantonToAcor(enfant.getCanton()));
        annonceACreer.setNssEnfantOldestDroit(enfant.getNoAVS());
        annonceACreer.setDateNaissanceEnfant(enfant.getDateNaissance());


        APPrestationJointLotTiersDroitManager manager3 = new APPrestationJointLotTiersDroitManager();
        manager3.setSession(session);
        manager3.setForIdDroit(idDroit);
        List<APPrestationJointLotTiersDroit> tousLesDroits;
        try {
            manager3.find(BManager.SIZE_NOLIMIT);
            tousLesDroits = manager3.getContainer();
        } catch (Exception e) {
            throw (e);
        }
        String dateDebutPeriodeEntier = "";
        String dateFinPeriodeEntier = "";

        if(annonceACreer.getNombreJoursService() != null && !JadeStringUtil.isBlankOrZero(annonceACreer.getNombreJoursService())){
            int nombreJours = Integer.parseInt(annonceACreer.getNombreJoursService());
            int nombreJoursOuverts = 0;
            //TODO: A confirmer au niveau du calcul des dates
            switch (nombreJours) {
                case 1:
                case 2 :
                case 3:
                case 4 :
                    nombreJoursOuverts = nombreJours;
                    break;
                case 5:
                case 6:
                case 7:
                    nombreJoursOuverts = 5;
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                    nombreJoursOuverts = nombreJours - 2;
                    break;
                case 12:
                case 13:
                case 14:
                    nombreJoursOuverts = 10;
                    break;
            }
            annonceACreer.setNombreJoursOuvrable(String.valueOf(nombreJoursOuverts));
        }else{
            annonceACreer.setNombreJoursOuvrable("0");
        }

        return annonceACreer;

    }

    /**
     * Tri des droits pour garder uniquement la derni?re correction d'un droit
     *
     * @param tousLesDroits
     * @return
     * @throws APRuleExecutionException
     */
    protected List<APDroitAvecParent> skipDroitParent(List<APDroitAvecParent> tousLesDroits)
            throws APRuleExecutionException {
        List<APDroitAvecParent> droitFinaux = new ArrayList<APDroitAvecParent>();
        // On r?cup?re tous les droits et leurs idParents s'il y en a
        Map<String, List<APDroitAvecParent>> droitAvecParent = new HashMap<String, List<APDroitAvecParent>>();
        for (APDroitAvecParent droit : tousLesDroits) {

            // S'il n'y a pas de droit parent, on le prend directement
            if (JadeStringUtil.isBlankOrZero(droit.getIdDroitParent())) {
                droitFinaux.add(droit);
            } else {
                String idParent = droit.getIdDroitParent();
                if (!droitAvecParent.containsKey(idParent)) {
                    droitAvecParent.put(idParent, new ArrayList<APDroitAvecParent>());
                }
                droitAvecParent.get(idParent).add(droit);
            }
        }

        // Maintenant on va prend tous les droits avec parent de plus grand id
        for (String idDroitParent : droitAvecParent.keySet()) {
            APDroitAvecParent droitAvecPlusGrandId = null;
            List<APDroitAvecParent> allDroits = new ArrayList<APDroitAvecParent>();
            for (APDroitAvecParent droit : droitAvecParent.get(idDroitParent)) {
                if (droitAvecPlusGrandId == null) {
                    droitAvecPlusGrandId = droit;
                } else {
                    try {
                        int idDroitAvecPlusGrandId = Integer.parseInt(droitAvecPlusGrandId.getIdDroit());
                        int idDroit = Integer.parseInt(droit.getIdDroit());
                        if (idDroit > idDroitAvecPlusGrandId) {
                            droitAvecPlusGrandId = droit;
                        }
                    } catch (Exception e) {
                        throw new APRuleExecutionException(
                                "Exception thrown when getting the child with big id for APDroitLAPGJointTiers. Id 1 = ["
                                        + droitAvecPlusGrandId.getIdDroit() + "], id 2 = [" + droit.getIdDroit() + "]",
                                e);
                    }
                }
            }
            // Test en cas d'erreur lors de l'?valuation des id...
            if (droitAvecPlusGrandId != null) {
                droitFinaux.add(droitAvecPlusGrandId);
            }
        }
        return droitFinaux;
    }

    /**
     * Recherche le montant max du taux journalier pour un droit acquis en fonction du nombre d'enfant
     *
     * @param session La session ? utiliser
     * @param nombreEnfant le nombre d'enfant
     * @return le montant max du taux journalier pour un droit acquis en fonction du nombre d'enfant
     * @throws Exception si aucune valeur n'a pu ?tr trouv?e
     */
    private BigDecimal getMontantMaxDroitAcquis(BSession session, int nombreEnfant, String date) throws Exception {
        String parameterName = null;
        if (nombreEnfant == 0) {
            parameterName = APParameter.TAUX_JOURNALIER_MAX_DROIT_ACQUIS_0_ENFANT.getParameterName();
        } else if (nombreEnfant == 1) {
            parameterName = APParameter.TAUX_JOURNALIER_MAX_DROIT_ACQUIS_1_ENFANT.getParameterName();
        } else if (nombreEnfant == 2) {
            parameterName = APParameter.TAUX_JOURNALIER_MAX_DROIT_ACQUIS_2_ENFANT.getParameterName();
        } else {
            parameterName = APParameter.TAUX_JOURNALIER_MAX_DROIT_ACQUIS_PLUS_DE_2_ENFANT.getParameterName();
        }
        return new BigDecimal(
                FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "0", parameterName, date, "", 2));
    }

    private String getGenreActivite(BSession session, APPrestation prestation) throws Exception {
        APSituationProfessionnelleManager situationProfessionnelleManager = new APSituationProfessionnelleManager();
        situationProfessionnelleManager.setSession(session);
        situationProfessionnelleManager.setForIdDroit(prestation.getIdDroit());
        situationProfessionnelleManager.find();

        APSituationProfessionnelle situationProfessionnelle = null;
        boolean aUnNiIndependantNiNonActif = false; // pour savoir si il
        // y a au moins une
        // situation pro
        // salari?e
        boolean aUnIndependant = false; // ind?pendante
        boolean aUnNonActif = false; // et non actif

        for (int j = 0; j < situationProfessionnelleManager.size(); j++) {
            situationProfessionnelle = (APSituationProfessionnelle) (situationProfessionnelleManager.getEntity(j));
            aUnNiIndependantNiNonActif = aUnNiIndependantNiNonActif
                    || (!situationProfessionnelle.getIsIndependant().booleanValue()
                            && !situationProfessionnelle.getIsNonActif().booleanValue());
            aUnIndependant = aUnIndependant || situationProfessionnelle.getIsIndependant().booleanValue();
            aUnNonActif = aUnNonActif || situationProfessionnelle.getIsNonActif().booleanValue();
        }

        // choix du genre d'activit?
        String genreActivite = "";

        if ((aUnIndependant && aUnNiIndependantNiNonActif) || (aUnNonActif && aUnNiIndependantNiNonActif)) {
            genreActivite = APTypeActiviteProfessionnel.ACTIVITE_SALARIE_ET_INDEPENDANT.getCodeActiviteAsString();
        } else if (aUnNiIndependantNiNonActif) {
            genreActivite = APTypeActiviteProfessionnel.ACTIVITE_SALARIE.getCodeActiviteAsString();
        } else if (aUnIndependant && !aUnNonActif) {
            genreActivite = APTypeActiviteProfessionnel.ACTIVITE_INDEPENDANT.getCodeActiviteAsString();
        } else if (aUnNonActif && !aUnIndependant) {
            genreActivite = APTypeActiviteProfessionnel.ACTIVITE_NON_ACTIF.getCodeActiviteAsString();
        } else {
            genreActivite = APTypeActiviteProfessionnel.ACTIVITE_NON_ACTIF.getCodeActiviteAsString();
        }

        return genreActivite;
    }

    private String getCodeEtatCivil(BSession session, APDroitLAPG droit) throws Exception {
        String s = session.getCode(PRTiersHelper.getPersonneAVS(session, droit.loadDemande().getIdTiers())
                .getProperty(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL));

        try {
            // Si code ?tat civil == vide -> return 1 (c?libataire)
            // Si code ?tat civil == 5 ou 6 (s?par? ou s?par? de fait)-> return
            // 2 (mari?)
            if (JadeStringUtil.isIntegerEmpty(s)) {
                return PRACORConst.CA_CELIBATAIRE;
            } else {
                if ((5 == Integer.parseInt(s)) || (6 == Integer.parseInt(s))) {
                    return PRACORConst.CA_MARIE;
                } else if (7 == Integer.parseInt(s)) {
                    return PRACORConst.CA_LPART_ENREGISTRE;
                } else if (8 == Integer.parseInt(s)) {
                    return PRACORConst.CA_LPART_DISSOUS;
                } else if (9 == Integer.parseInt(s)) {
                    return PRACORConst.CA_LPART_DECES;
                } else if (10 == Integer.parseInt(s)) {
                    return PRACORConst.CA_LPART_ENREGISTRE;
                }
            }

            // si le code n'est pas 1, 2, 3 ou 4, on retourne 1
            if (!((Integer.parseInt(s) == 1) || (Integer.parseInt(s) == 2) || (Integer.parseInt(s) == 3)
                    || (Integer.parseInt(s) == 4))) {
                return PRACORConst.CA_CELIBATAIRE;
            }
        } catch (Exception e) {
            return PRACORConst.CA_CELIBATAIRE;
        }

        return s;
    }

    private String getNombreEnfant(BSession session, APDroitLAPG droit, String dateDebutPrestation) throws Exception {
        if (droit instanceof APDroitMaternite || droit instanceof APDroitPaternite || droit instanceof APDroitProcheAidant || droit instanceof APDroitPandemie) {
            return "0";
        } else{
            APSituationFamilialeAPG situationFamilialeAPG = ((APDroitAPG) droit).loadSituationFamilliale();

            APEnfantAPGManager enfantAPGManager = new APEnfantAPGManager();
            enfantAPGManager.setSession(session);
            enfantAPGManager.setForIdSituationFamiliale(situationFamilialeAPG.getIdSitFamAPG());
            enfantAPGManager.find();

            APEnfantAPG enfantAPG = null;
            int nbEnfantsPendantPrestation = 0;

            for (int j = 0; j < enfantAPGManager.size(); j++) {
                enfantAPG = (APEnfantAPG) (enfantAPGManager.getEntity(j));

                // Si un enfant est n? pendant un droit, sa
                // date de d?but droit coincide avec la date
                // de d?but d'une prestation
                if (enfantAPG.getDateDebutDroit().equals(dateDebutPrestation)) {
                    nbEnfantsPendantPrestation++;
                }
            }

            int nombreEnfantDebutDroit = 0;

            if (!JadeStringUtil.isIntegerEmpty(situationFamilialeAPG.getNbrEnfantsDebutDroit())) {
                nombreEnfantDebutDroit += Integer.parseInt(situationFamilialeAPG.getNbrEnfantsDebutDroit());
            }

            return Integer.toString(nombreEnfantDebutDroit + nbEnfantsPendantPrestation);
        }
    }

    private String getModePaiement(BSession session, APPrestation prestation) throws Exception {
        String modePaiement = null;

        APRepartitionPaiementsManager repartitionPaiementsManager = new APRepartitionPaiementsManager();
        repartitionPaiementsManager.setSession(session);
        repartitionPaiementsManager.setForIdPrestation(prestation.getIdPrestationApg());
        repartitionPaiementsManager.find(BManager.SIZE_NOLIMIT);

        if (repartitionPaiementsManager.getSize() == 0) {
            throw new Exception("Aucune r?partition pour cette prestation : idPRst = " + prestation.getIdPrestationApg()
                    + " idDroit= " + prestation.getIdDroit());
        }
        boolean hasVersementEmployeur = false;
        boolean hasVersementPlusieursEmployeurs = false;
        boolean hasVersementAssure = false;

        for(APRepartitionPaiements repartitionPaiements: repartitionPaiementsManager.<APRepartitionPaiements>getContainerAsList()) {
            hasVersementPlusieursEmployeurs |= hasVersementEmployeur
                    && IAPRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR.equals(repartitionPaiements.getTypePaiement());
            hasVersementEmployeur |= IAPRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR.equals(repartitionPaiements.getTypePaiement());
            hasVersementAssure |= IAPRepartitionPaiements.CS_PAIEMENT_DIRECT.equals(repartitionPaiements.getTypePaiement());
        }

        if(hasVersementPlusieursEmployeurs) {
            modePaiement = APTypeVersement.VERSEMENT_PLUSIEURS_EMPLOYEURS.getCodeTypeVersementAsString();
        } else if (hasVersementAssure) {
            if (hasVersementEmployeur) {
                modePaiement = APTypeVersement.VERSEMENT_EMPLOYEUR_ET_ASSURE.getCodeTypeVersementAsString();
            } else {
                modePaiement = APTypeVersement.VERSEMENT_ASSURE.getCodeTypeVersementAsString();
            }
        } else {
            if (hasVersementEmployeur) {
                modePaiement = APTypeVersement.VERSEMENT_EMPLOYEUR.getCodeTypeVersementAsString();
            } else {
                throw new Exception("Impossible de d?finir le mode de paiement");
            }
        }

        return modePaiement;
    }
}
