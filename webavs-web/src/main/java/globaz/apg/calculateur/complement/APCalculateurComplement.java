package globaz.apg.calculateur.complement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.application.APApplication;
import globaz.apg.calculateur.IAPPrestationCalculateur;
import globaz.apg.calculateur.pojo.APPrestationCalculeeAPersister;
import globaz.apg.calculateur.pojo.APRepartitionCalculeeAPersister;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.db.prestation.APCotisation;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.enums.APAssuranceTypeAssociation;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APCotisationData;
import globaz.apg.module.calcul.APRepartitionPaiementData;
import globaz.apg.module.calcul.APSituationProfessionnelleCanton;
import globaz.apg.module.calcul.complement.APComplementCalculateur;
import globaz.apg.properties.APProperties;
import globaz.apg.services.APRechercherAssuranceFromDroitCotisationService;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.properties.JadePropertiesService;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.api.IAFAssurance;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.planAffiliation.AFPlanAffiliationManager;

public class APCalculateurComplement implements IAPPrestationCalculateur<APCalculateurComplementDonneesPersistence, APCalculateurComplementDonneeDomaine, APCalculateurComplementDonneesPersistence> {
    
    @Override
    public List<APCalculateurComplementDonneesPersistence> calculerPrestation(List<APCalculateurComplementDonneeDomaine> donneesDomainCalcul) throws Exception {
        //

        if (donneesDomainCalcul == null || donneesDomainCalcul.size() == 0) {
            throw new Exception("Aucune données pour le calcul des compléments");
        }

        List<APCalculateurComplementDonneesPersistence> resultatCalcul = new LinkedList<>();

        for (APCalculateurComplementDonneeDomaine prestationStandard : donneesDomainCalcul) {
            APPrestation prestation = prestationStandard.getPrestation();

            BigDecimal salaireMensuel = new BigDecimal(prestation.getRevenuMoyenDeterminant())
                    .multiply(new BigDecimal(30));

            APCalculateurComplementDonneesPersistence prestationCalculeeAPersister = new APCalculateurComplementDonneesPersistence(
                    prestation.getDateDebut(), prestation.getDateFin(), Integer.valueOf(prestation.getNombreJoursSoldes()),
                    prestation.getIdDroit());

            // Calculateur
            //String idSituationProf = prestationStandard.getRepartitions().get(0).getIdSituationProfessionnelle();
            String idSituationProf = prestationStandard.getSituationProfessionnelle().keySet().iterator().next();
            APDroitAPG droit = (APDroitAPG)prestationStandard.getDroit();
            APComplementCalculateur calculateur = APComplementCalculateur.getCalculateur(
                    prestationStandard.getMontantsMax(),
                    prestationStandard.getSituationProfessionnelle().get(idSituationProf).getCanton(),
                    prestation.getDateDebut(),
                    droit.getGenreService(),
                    Integer.valueOf(droit.loadSituationFamilliale().getNbrEnfantsDebutDroit()));

            BigDecimal montant = calculateur.calculerMontant(salaireMensuel,
                    Integer.valueOf(prestation.getNombreJoursSoldes()));
            BigDecimal montantBrut = arrondir(montant);
            BigDecimal montantBrutFederal = getMontantFederal(prestation);
            BigDecimal montantBrutJournalier = BigDecimal.ZERO;
            if (montantBrut.compareTo(montantBrutFederal) > 0) {
                montantBrut = montantBrut.subtract(montantBrutFederal);
                prestationCalculeeAPersister.setMontantBrut(montantBrut);
                prestationCalculeeAPersister.setMontantJournalier(montantBrut.divide(BigDecimal.valueOf(calculateur.getNbJourPrisEnCompte()),2, RoundingMode.HALF_UP));
                prestationCalculeeAPersister.setNombreDeJoursSoldes(calculateur.getNbJourPrisEnCompte());
                montantBrutJournalier = montantBrut.divide(BigDecimal.valueOf(calculateur.getNbJourPrisEnCompte()), 10, RoundingMode.HALF_UP);
            } else {
                return resultatCalcul;
            }

            BigDecimal montantPrestationComplementJournalier = new BigDecimal("0");
            BigDecimal montantPrestationComplementBrut = new BigDecimal("0");
            BigDecimal montantPrestationComplementNet = new BigDecimal("0");

            for(APRepartitionJointPrestation repartition : prestationStandard.getRepartitions()) {
                if (prestationStandard.getSituationProfessionnelle().get(repartition.getIdSituationProfessionnelle()) != null) {

                    APSituationProfessionnelleCanton sitProf = prestationStandard.getSituationProfessionnelle().get(repartition.getIdSituationProfessionnelle());

                    BigDecimal tauxCalcul = new BigDecimal(repartition.getMontantBrut()).divide(new BigDecimal(prestation.getMontantBrut()), 10, RoundingMode.HALF_UP);

                    BigDecimal montantBrutReparti =  montantBrutJournalier.multiply(tauxCalcul);

                    BigDecimal montantBrutRepartition = getMontantBrutRepartition(
                            montantBrutReparti, prestationCalculeeAPersister.getNombreDeJoursSoldes());
                    final BigDecimal[] tauxAvsAc = prestationStandard.getTaux().get(sitProf.getId());

                    APCotisation cotisationAvsStandardParitaire = getCotisationFromMap(prestationStandard.getMapCotisation().get(repartition), APProperties.ASSURANCE_AVS_PAR_ID.getValue());
                    APCotisation cotisationAvsStandardPersonnelle = getCotisationFromMap(prestationStandard.getMapCotisation().get(repartition), APProperties.ASSURANCE_AVS_PER_ID.getValue());
                    APCotisation cotisationAcStandardParitaire = getCotisationFromMap(prestationStandard.getMapCotisation().get(repartition), APProperties.ASSURANCE_AC_PAR_ID.getValue());
                    APCotisation cotisationAcStandardPersonnelle = getCotisationFromMap(prestationStandard.getMapCotisation().get(repartition), APProperties.ASSURANCE_AC_PER_ID.getValue());

                    APCotisation cotisationAvsStandard = null;
                    APCotisation cotisationAcStandard = null;

                    // Création des cotisations
                    APCotisationData cotisationAvs = null;
                    APCotisationData cotisationAc = null;

                    if(cotisationAvsStandardParitaire != null) {
                        cotisationAvsStandard = cotisationAvsStandardParitaire;
                        cotisationAvs = creerCotisation(montantBrutRepartition, tauxAvsAc[0], APProperties.ASSURANCE_AVS_PAR_ID.getValue());
                    } else if (cotisationAvsStandardPersonnelle != null){
                        cotisationAvsStandard = cotisationAvsStandardPersonnelle;
                        cotisationAvs = creerCotisation(montantBrutRepartition, tauxAvsAc[2], APProperties.ASSURANCE_AVS_PER_ID.getValue());
                    }

                    if(cotisationAcStandardParitaire != null) {
                        cotisationAcStandard = cotisationAcStandardParitaire;
                        cotisationAc = creerCotisation(montantBrutRepartition, tauxAvsAc[1], APProperties.ASSURANCE_AC_PAR_ID.getValue());
                    } else if (cotisationAcStandardPersonnelle != null){
                        cotisationAcStandard = cotisationAcStandardPersonnelle;
                        cotisationAc = creerCotisation(montantBrutRepartition, tauxAvsAc[3], APProperties.ASSURANCE_AC_PER_ID.getValue());
                    }

                    // création de la répartition
                    montantBrutRepartition = arrondir(montantBrutRepartition);
                    final APRepartitionPaiementData repartitionPaiementData = creerRepartition(
                            montantBrutRepartition, cotisationAvs, cotisationAc,
                            IAPRepartitionPaiements.CS_NORMAL, IAPRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR,
                            sitProf.getIdTiersEmployeur(), sitProf.getIdTiersPaiementEmployeur(),
                            sitProf.getIdDomainePaiementEmployeur(),
                            sitProf.getAssociation().getCodesystemToString(),
                            sitProf.getId(), sitProf.getNom(),
                            sitProf.getIdAffilie(),
                            sitProf.getTypeAffiliation(),
                            sitProf.getIndependant(),
                            cotisationAvsStandard,
                            cotisationAcStandard
                            );
                    prestationCalculeeAPersister.getRepartitionsPaiementMap().add(repartitionPaiementData);

                    // màj montant prestation
                    montantPrestationComplementJournalier = montantPrestationComplementJournalier.add(sitProf
                            .getMontantJournalier());
                    montantPrestationComplementBrut = montantPrestationComplementBrut.add(repartitionPaiementData
                            .getMontantBrut());
                    montantPrestationComplementNet = montantPrestationComplementNet.add(repartitionPaiementData
                            .getMontantNet());
                }
            }

            prestationCalculeeAPersister.setMontantNet(montantPrestationComplementNet);
            prestationCalculeeAPersister.setMontantBrut(montantPrestationComplementBrut);

            resultatCalcul.add(prestationCalculeeAPersister);


        }

        return resultatCalcul;
    }

    private BigDecimal getMontantFederal(APPrestation prestation) {
        BigDecimal montantBrutFederal = new BigDecimal(prestation.getMontantBrut());
        BigDecimal montantAllocationExploitation = new BigDecimal(prestation.getMontantAllocationExploitation());
        BigDecimal nbJours = new BigDecimal(prestation.getNombreJoursSoldes());
        montantAllocationExploitation = montantAllocationExploitation.multiply(nbJours);
        BigDecimal fraisGarde = new BigDecimal(prestation.getFraisGarde());
        return montantBrutFederal.subtract(montantAllocationExploitation).subtract(fraisGarde);
    }
    
    private APCotisationData creerCotisation(final BigDecimal montantBrutRepartition, final BigDecimal tauxAc,
            final String idAssurance) {

        final BigDecimal montantBrutCotisation = getMontantBrutCotisation(montantBrutRepartition, tauxAc);
        final APCotisationData cotisationAc = new APCotisationData(montantBrutCotisation, montantBrutRepartition,
                tauxAc, APCotisation.TYPE_ASSURANCE, idAssurance);

        return cotisationAc;
    }
    
    private APRepartitionPaiementData creerRepartition(final BigDecimal montantBrutRepartition,
            final APCotisationData cotisationAvs, final APCotisationData cotisationAc,
            final String typePrestation, final String typePaiement,
            final String idTiersEmployeur, final String idTiersPaiementEmployeur,
            final String idDomainePaiementEmployeur, final String typeAssociationAssurance,
            final String idSituationProfessionnelle, final String nom, final String idAffilie,
            final String typeAffiliation, final Boolean isIndependant,
            final APCotisation cotisationAvsStandard, final APCotisation cotisationAcStandard) {

        BigDecimal montantNetRepartition;

        BigDecimal cotFinalAvs = null;
        BigDecimal cotFinalAc = null;
        cotFinalAvs = getCotisationFinal(cotisationAvs, cotisationAvsStandard);
        cotFinalAc = getCotisationFinal(cotisationAc, cotisationAcStandard);

        montantNetRepartition = montantBrutRepartition;
        if(cotFinalAvs != null) {
            montantNetRepartition = montantNetRepartition.add(cotFinalAvs);
            cotisationAvs.setMontantCotisation(cotFinalAvs);
        }
        if(cotFinalAc != null) {
            montantNetRepartition = montantNetRepartition.add(cotFinalAc);
            cotisationAc.setMontantCotisation(cotFinalAc);
        }

        // création de la répartition
        final APRepartitionPaiementData repartition = new APRepartitionPaiementData(montantBrutRepartition,
                montantNetRepartition, typePrestation, typePaiement, idTiersEmployeur, idTiersPaiementEmployeur,
                idDomainePaiementEmployeur, typeAssociationAssurance, idSituationProfessionnelle, nom, idAffilie);

        if(cotFinalAvs != null) {
            repartition.getCotisations().add(cotisationAvs);
        }

        if(cotFinalAc != null) {
            repartition.getCotisations().add(cotisationAc);
        }

        return repartition;

    }

    private BigDecimal getCotisationFinal(APCotisationData cotisation, APCotisation cotisationStandard) {
        if (cotisationStandard != null && cotisation != null) {
            BigDecimal cotFinal;
            if (new BigDecimal(cotisationStandard.getMontant()).compareTo(BigDecimal.ZERO) > 0) {
                cotFinal = cotisation.getMontantCotisation();
            } else {
                cotFinal = cotisation.getMontantCotisation().negate();

            }
            cotFinal = arrondir(cotFinal);
            return cotFinal;
        }
        return null;

    }

    private BigDecimal getMontantBrutCotisation(final BigDecimal montantBrutRepartition, final BigDecimal taux) {
        final BigDecimal montantBrutCotisation = montantBrutRepartition.multiply(taux);
        return montantBrutCotisation;
    }
    
    private BigDecimal getMontantBrutRepartition(final BigDecimal montantJournalierAcmNe, final int nombreDeJoursSoldes) {
        return montantJournalierAcmNe.multiply(new BigDecimal(nombreDeJoursSoldes));
    }
    
    private APCotisation getCotisationFromMap(Map<String, APCotisation> mapCotisation, String idAssurance) {
        if (mapCotisation != null && mapCotisation.get(idAssurance) != null) {
            return mapCotisation.get(idAssurance);
        }
        return null;
    }
    
    /**
     * Règle retrouvée dans le code...</br>
     * arrondi à 2 chiffres après la virgule, à 5cts près.
     * 
     * @param montant
     * @return
     */
    private BigDecimal arrondir(BigDecimal montant) {
        // arrondi à 2 chiffres après la virgule, à 5cts près.
        return new BigDecimal(JANumberFormatter
                .deQuote(JANumberFormatter.format(montant.toString(), 0.05, 2, JANumberFormatter.NEAR)));
    }

    
    /**
     * Créé l'objet en fonction des entités situation professionnelle et droit
     * 
     * @param donneesPersistancePourCalcul
     * @return APPrestationStandardData
     * @throws JadePersistenceException
     * @throws Exception
     */
    @Override
    public List<APCalculateurComplementDonneeDomaine> persistenceToDomain(final APCalculateurComplementDonneesPersistence donneesPersistancePourCalcul) throws JadePersistenceException, Exception {

        // Données en entrée
        final List<APRepartitionJointPrestation> listePrestationsPersistance = donneesPersistancePourCalcul
                .getPrestationJointRepartitions();

        // Données en sortie
        final List<APCalculateurComplementDonneeDomaine> listePrestationsComplementDomaineConverties = new ArrayList<>();


        Map<String, List<APRepartitionJointPrestation>> mapPrestations = new HashMap<>();
        // Pour chacune des prestations standard joint répartition
        for (APRepartitionJointPrestation prestationJoinRepartition : listePrestationsPersistance) {
            if(mapPrestations.get(prestationJoinRepartition.getIdPrestationApg()) == null) {
                mapPrestations.put(prestationJoinRepartition.getIdPrestationApg(), new ArrayList<APRepartitionJointPrestation>());
            }
            mapPrestations.get(prestationJoinRepartition.getIdPrestationApg()).add(prestationJoinRepartition);
        }

        for(APPrestation prestation : donneesPersistancePourCalcul.getListPrestationStandard()) {
            List<APRepartitionJointPrestation> repartitions = mapPrestations.get(prestation.getIdPrestationApg());
            if(repartitions != null) {
                APCalculateurComplementDonneeDomaine donneeDomaine = new APCalculateurComplementDonneeDomaine(prestation);
                donneeDomaine.setRepartitions(repartitions);
                listePrestationsComplementDomaineConverties.add(donneeDomaine);
            }
        }

        // Pour chacune des prestations standard Joint répartition
        for (final APCalculateurComplementDonneeDomaine prestationStandard : listePrestationsComplementDomaineConverties) {
            
            prestationStandard.setTaux(donneesPersistancePourCalcul.getTaux());
            prestationStandard.setMontantsMax(donneesPersistancePourCalcul.getMontantsMax());

            // Recherche de la situation professionnelle
            final List<APSitProJointEmployeur> apSitProJoiEmpEntityList = donneesPersistancePourCalcul
                    .getSituationProfessionnelleEmployeur();

            String idSituationProfessionnelleCourante = "";
            AFAffiliationManager afManager = new AFAffiliationManager();

            for (final APSitProJointEmployeur apSitProJoiEmpEnt : apSitProJoiEmpEntityList) {

                if (apSitProJoiEmpEnt != null) {
                    // nouvelle situation professionnelle
                    if (!idSituationProfessionnelleCourante.equals(apSitProJoiEmpEnt.getIdSitPro())) {
                        idSituationProfessionnelleCourante = apSitProJoiEmpEnt.getIdSitPro();

                        // Définition de l'association
                        final APAssuranceTypeAssociation association = getAssociationFromSituationProf(apSitProJoiEmpEnt);

                        APSituationProfessionnelleCanton sitProf = new APSituationProfessionnelleCanton(association, apSitProJoiEmpEnt.getDateDebut(),
                                apSitProJoiEmpEnt.getDateFin(), 
                                new BigDecimal(apSitProJoiEmpEnt
                                        .getMontantJournalierAcmNe()), 
                                apSitProJoiEmpEnt.getIdSitPro(),
                                apSitProJoiEmpEnt.getIdTiers(), apSitProJoiEmpEnt.getIdAffilie(),
                                apSitProJoiEmpEnt.getNom(), apSitProJoiEmpEnt.getIdTiersPaiementEmployeur(),
                                apSitProJoiEmpEnt.getIdDomainePaiementEmployeur(),
                                donneesPersistancePourCalcul.getMapTypeAffiliation().get(apSitProJoiEmpEnt.getIdSitPro()),
                                apSitProJoiEmpEnt.getIndependant());
                        sitProf.setCanton(donneesPersistancePourCalcul.getMapCanton().get(apSitProJoiEmpEnt.getIdSitPro()));
                        prestationStandard.getSituationProfessionnelle().put(
                                apSitProJoiEmpEnt.getIdSitPro(),sitProf
                                );
                        
                    }
                } else {
                    throw new Exception("APCalculerComplementService.convert(): impossible de retrouver la prestation");
                }
            }
            prestationStandard.setDroit(donneesPersistancePourCalcul.getDroit());

            for (APCotisation cotisation : donneesPersistancePourCalcul.getListCotisation()) {
                for(APRepartitionJointPrestation prestation : prestationStandard.getRepartitions()) {
                    if(prestation.getId().equals(cotisation.getIdRepartitionBeneficiairePaiement())) {
                        if(prestationStandard.getMapCotisation().get(prestation) == null) {
                            prestationStandard.getMapCotisation().put(prestation, new HashMap<>());
                        }
                        prestationStandard.getMapCotisation().get(prestation).put(cotisation.getIdExterne(), cotisation);
                    }
                }
            }

        }

        return listePrestationsComplementDomaineConverties;
    }

    private APAssuranceTypeAssociation getAssociationFromSituationProf(final APSitProJointEmployeur apSitProJoiEmpEnt) {
        APAssuranceTypeAssociation association;
        if (APAssuranceTypeAssociation.FNE.isCodeSystemEqual(apSitProJoiEmpEnt.getCsAssuranceAssociation())) {
            association = APAssuranceTypeAssociation.FNE;
        } else if (APAssuranceTypeAssociation.MECP.isCodeSystemEqual(apSitProJoiEmpEnt.getCsAssuranceAssociation())) {
            association = APAssuranceTypeAssociation.MECP;
        } else if (APAssuranceTypeAssociation.PP.isCodeSystemEqual(apSitProJoiEmpEnt.getCsAssuranceAssociation())) {
            association = APAssuranceTypeAssociation.PP;
        } else {
            association = APAssuranceTypeAssociation.NONE;
        }
        return association;
    }


    @Override
    public List<APPrestationCalculeeAPersister> domainToPersistence(List<APCalculateurComplementDonneesPersistence> resultat) throws Exception {
        List<APPrestationCalculeeAPersister> list = new ArrayList<APPrestationCalculeeAPersister>();
        for (APCalculateurComplementDonneesPersistence prestationDomainCalculee : resultat) {
                // Prestation calculée convertie vers la persistence
                final APPrestationCalculeeAPersister prestationPersistente = new APPrestationCalculeeAPersister();

                final APPrestation prestation = new APPrestation();
                // Les prestations ACM ne doivent pas être annoncées
                prestation.setContenuAnnonce(null);
                prestation.setDateCalcul(JACalendar.todayJJsMMsAAAA());
                prestation.setDateDebut(prestationDomainCalculee.getDateDebut());
                prestation.setDateFin(prestationDomainCalculee.getDateFin());
                prestation.setIdDroit(prestationDomainCalculee.getIdDroit());
                prestation.setNombreJoursSoldes(String.valueOf(prestationDomainCalculee.getNombreDeJoursSoldes()));
                prestation.setType(IAPPrestation.CS_TYPE_NORMAL);
                prestation.setMontantJournalier(arrondir(prestationDomainCalculee.getMontantJournalier()).toString());
                prestation.setMontantBrut(arrondir(prestationDomainCalculee.getMontantBrut()).toString());
                prestation.setGenre(String.valueOf(APTypeDePrestation.COMPCIAB.getCodesystem()));
                prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
                prestationPersistente.setPrestation(prestation);

                // Création des répartitions des paiements et cotisations
                for (final APRepartitionPaiementData apRepPaiDat : prestationDomainCalculee.getRepartitionsPaiementMap()) {

                    final APRepartitionPaiements apRepartitionPaiementsEntity = new APRepartitionPaiements();
                    apRepartitionPaiementsEntity.wantCallValidate(false);
                    apRepartitionPaiementsEntity.setDateValeur(JACalendar.todayJJsMMsAAAA());
                    apRepartitionPaiementsEntity.setMontantBrut(arrondir(apRepPaiDat.getMontantBrut()).toString());
                    apRepartitionPaiementsEntity.setMontantNet(arrondir(apRepPaiDat.getMontantNet()).toString());
                    apRepartitionPaiementsEntity.setTypeAssociationAssurance(apRepPaiDat.getTypeAssociationAssurance());
                    apRepartitionPaiementsEntity.setTypePaiement(apRepPaiDat.getTypePaiement());
                    apRepartitionPaiementsEntity.setTypePrestation(apRepPaiDat.getTypePrestation());
                    apRepartitionPaiementsEntity.setIdSituationProfessionnelle(apRepPaiDat.getIdSituationProfessionnelle());
                    apRepartitionPaiementsEntity.setNom(apRepPaiDat.getNom());
                    apRepartitionPaiementsEntity.setIdTiers(apRepPaiDat.getIdTiersEmployeur());
                    apRepartitionPaiementsEntity.setIdTiersAdressePaiement(apRepPaiDat.getIdTiersPaiementEmployeur());
                    apRepartitionPaiementsEntity.setIdAffilie(apRepPaiDat.getIdAffilie());
                    apRepartitionPaiementsEntity.setIdDomaineAdressePaiement(apRepPaiDat.getIdDomainePaiementEmployeur());

                    final APRepartitionCalculeeAPersister apResRepAcmNeEntite = new APRepartitionCalculeeAPersister();
                    apResRepAcmNeEntite.setRepartitionPaiements(apRepartitionPaiementsEntity);

                    // Création des cotisations
                    for (final APCotisationData cotisationData : apRepPaiDat.getCotisations()) {
                        final APCotisation apCot = new APCotisation();
                        apCot.setType(String.valueOf(cotisationData.getType()));
                        apCot.setMontantBrut(arrondir(cotisationData.getMontantBrut()).toString());
                        apCot.setType(APCotisation.TYPE_ASSURANCE);
                        apCot.setDateDebut(prestationDomainCalculee.getDateDebut());
                        apCot.setDateFin(prestationDomainCalculee.getDateFin());
                        apCot.setMontant(arrondir(cotisationData.getMontantCotisation()).toString());
                        apCot.setIdExterne(cotisationData.getIdAssurance());
                        apCot.setTaux(cotisationData.getTaux().multiply(new BigDecimal(100))
                                .setScale(2, RoundingMode.HALF_UP).toString());

                        apResRepAcmNeEntite.getCotisations().add(apCot);
                    }
                    prestationPersistente.getRepartitions().add(apResRepAcmNeEntite);
                }
                list.add(prestationPersistente);
            }
        return list;
    }

}
