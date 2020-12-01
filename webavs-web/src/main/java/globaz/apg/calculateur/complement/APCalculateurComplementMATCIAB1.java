package globaz.apg.calculateur.complement;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.calculateur.IAPPrestationCalculateur;
import globaz.apg.calculateur.pojo.APPrestationCalculeeAPersister;
import globaz.apg.calculateur.pojo.APRepartitionCalculeeAPersister;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.prestation.APCotisation;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.enums.APAssuranceTypeAssociation;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.helpers.droits.APSituationProfessionnelleHelper;
import globaz.apg.module.calcul.APCotisationData;
import globaz.apg.module.calcul.APRepartitionPaiementData;
import globaz.apg.module.calcul.APSituationProfessionnelleCanton;
import globaz.apg.module.calcul.complement.APComplementCalculateur;
import globaz.apg.properties.APProperties;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.prestation.api.IPRDemande;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class APCalculateurComplementMATCIAB1 implements IAPPrestationCalculateur<APCalculateurComplementDonneesPersistence, APCalculateurComplementDonneeDomaine, APCalculateurComplementDonneesPersistence> {

    @Override
    public List<APCalculateurComplementDonneesPersistence> calculerPrestation(List<APCalculateurComplementDonneeDomaine> donneesDomainCalcul) throws Exception {

        if (donneesDomainCalcul == null || donneesDomainCalcul.size() == 0) {
            throw new Exception("Aucune donn�es pour le calcul des compl�ments");
        }

        List<APCalculateurComplementDonneesPersistence> resultatCalcul = new LinkedList<>();

        for (APCalculateurComplementDonneeDomaine prestationStandard : donneesDomainCalcul) {
            APPrestation prestation = prestationStandard.getPrestation();

            BigDecimal salaireJournalier = new BigDecimal(prestation.getRevenuMoyenDeterminant());

            APCalculateurComplementDonneesPersistence prestationCalculeeAPersister = new APCalculateurComplementDonneesPersistence(
                    prestation.getDateDebut(), prestation.getDateFin(), Integer.valueOf(prestation.getNombreJoursSoldes()),
                    prestation.getIdDroit());

            // Calculateur
            //String idSituationProf = prestationStandard.getRepartitions().get(0).getIdSituationProfessionnelle();
            String idSituationProf = prestationStandard.getSituationProfessionnelle().keySet().iterator().next();
            APDroitMaternite droit = (APDroitMaternite)prestationStandard.getDroit();
            APComplementCalculateur calculateur = APComplementCalculateur.getCalculateur(
                    prestationStandard.getMontantsMax(),
                    prestationStandard.getSituationProfessionnelle().get(idSituationProf).getCanton(),
                    prestation.getDateDebut(),
                    droit.getGenreService(),
                    0); //fix� � 0 car non utilis�

            // La date de d�but prestation.getDateDebut() et peut avoir �t� adapt� par rapport
            // � la propri�t� PROPERTY_APG_FERCIAB_MATERNITE et donc le nombre de jours soldes
            // pour la p�riode entre prestation.getDateDebut() et prestation.getDateFin() est � recalculer
            int nombreJoursSoldes = Integer.parseInt(prestation.getNombreJoursSoldes()); // nombreJoursSoldes initial avant l'adaptation de la p�riode par la propri�t� PROPERTY_APG_FERCIAB_MATERNITE
            int nombreJoursSoldesPeriodePriseEnCompte = JadeDateUtil.getNbDaysBetween(prestation.getDateDebut(), prestation.getDateFin()) + 1; // nombreJoursSoldesPeriodePriseEnCompte apr�s l'adapation par la propri�t� PROPERTY_APG_FERCIAB_MATERNITE
            BigDecimal montantMATCIAB1 = calculateur.calculerMontantMATCIAB1(salaireJournalier, nombreJoursSoldesPeriodePriseEnCompte);
            BigDecimal montantBrutFederal;
            BigDecimal salaireJournalier80 = salaireJournalier.multiply(BigDecimal.valueOf(80)).divide(BigDecimal.valueOf(100));
            if (salaireJournalier80.compareTo(new BigDecimal(prestation.getMontantJournalier())) < 0) {
                // ce cas ne se produit que si il y a plusieurs employeur et que certain de ces remployeur ne cotise pas au assurance compl�mentaire
                // le calcul MATCIAB1 se base alors sur le 80% des revenus moyen d�terminent qui cotise au assurance compl�mentaire
                montantBrutFederal = getMontantFederal(salaireJournalier80, prestation, nombreJoursSoldesPeriodePriseEnCompte);
            } else {
                // ce cas se produit pour les cas ou le nombre de revenu qui cotise au assurance compl�mentaire est le m�me que le nombre de revenu total
                // le calcul MATCIAB1 se base alors sur le m�me revenu moyen d�terminent que le calcul Standard
                montantBrutFederal = getMontantFederal(new BigDecimal(prestation.getMontantJournalier()), prestation, nombreJoursSoldesPeriodePriseEnCompte);
            }
            montantMATCIAB1 = montantMATCIAB1.subtract(montantBrutFederal);
            prestationCalculeeAPersister.setMontantBrut(montantMATCIAB1);
            prestationCalculeeAPersister.setMontantJournalier(montantMATCIAB1.divide(BigDecimal.valueOf(nombreJoursSoldesPeriodePriseEnCompte),2, RoundingMode.HALF_UP));
            // met � jour le nombre de jours soldes avec le nombre de jours soldes prise en compte
            prestationCalculeeAPersister.setNombreDeJoursSoldes(nombreJoursSoldesPeriodePriseEnCompte);
            BigDecimal montantBrutJournalier = montantMATCIAB1.divide(BigDecimal.valueOf(nombreJoursSoldesPeriodePriseEnCompte), 10, RoundingMode.HALF_UP);

            BigDecimal montantPrestationComplementJournalier = new BigDecimal("0");
            BigDecimal montantPrestationComplementBrut = new BigDecimal("0");
            BigDecimal montantPrestationComplementNet = new BigDecimal("0");

            for(APRepartitionJointPrestation repartition : prestationStandard.getRepartitions()) {
                if (prestationStandard.getSituationProfessionnelle().get(repartition.getIdSituationProfessionnelle()) != null) {

                    APSituationProfessionnelleCanton sitProf = prestationStandard.getSituationProfessionnelle().get(repartition.getIdSituationProfessionnelle());

                    BigDecimal tauxCalcul = new BigDecimal(repartition.getMontantBrut()).divide(montantBrutFederal, 10, RoundingMode.HALF_UP);

                    BigDecimal montantBrutReparti = montantBrutJournalier.multiply(tauxCalcul);

                    BigDecimal montantBrutRepartition = getMontantBrutRepartition(
                            montantBrutReparti, prestationCalculeeAPersister.getNombreDeJoursSoldes());
                    final BigDecimal[] tauxAvsAc = prestationStandard.getTaux().get(sitProf.getId());

                    APCotisation cotisationAvsParitaire = getCotisationFromMap(prestationStandard.getMapCotisation().get(repartition), APProperties.ASSURANCE_AVS_PAR_ID.getValue());
                    APCotisation cotisationAvsPersonnelle = getCotisationFromMap(prestationStandard.getMapCotisation().get(repartition), APProperties.ASSURANCE_AVS_PER_ID.getValue());
                    APCotisation cotisationAcParitaire = getCotisationFromMap(prestationStandard.getMapCotisation().get(repartition), APProperties.ASSURANCE_AC_PAR_ID.getValue());
                    APCotisation cotisationAcPersonnelle = getCotisationFromMap(prestationStandard.getMapCotisation().get(repartition), APProperties.ASSURANCE_AC_PER_ID.getValue());

                    // Cr�ation des cotisations
                    APCotisationData cotisationAvsParitaireData = null;
                    APCotisationData cotisationAcParitaireData = null;
                    APCotisationData cotisationAvsPersonelleData = null;
                    APCotisationData cotisationAcPersonelleData = null;

                    if (cotisationAvsParitaire != null) {
                        cotisationAvsParitaireData = creerCotisation(montantBrutRepartition, tauxAvsAc[0], APProperties.ASSURANCE_AVS_PAR_ID.getValue());
                    } else if (cotisationAvsPersonnelle != null) {
                        cotisationAvsPersonelleData = creerCotisation(montantBrutRepartition, tauxAvsAc[2], APProperties.ASSURANCE_AVS_PER_ID.getValue());
                    }

                    if (cotisationAcParitaire != null) {
                        cotisationAcParitaireData = creerCotisation(montantBrutRepartition, tauxAvsAc[1], APProperties.ASSURANCE_AC_PAR_ID.getValue());
                    } else if (cotisationAcPersonnelle != null) {
                        cotisationAcPersonelleData = creerCotisation(montantBrutRepartition, tauxAvsAc[3], APProperties.ASSURANCE_AC_PER_ID.getValue());
                    }

                    if (cotisationAvsParitaireData != null || cotisationAcParitaireData != null) {
                        // cr�ation de la r�partition paritaire
                        montantBrutRepartition = arrondir(montantBrutRepartition);
                        final APRepartitionPaiementData repartitionPaiementData = creerRepartition(
                                montantBrutRepartition, cotisationAvsParitaireData, cotisationAcParitaireData,
                                IAPRepartitionPaiements.CS_NORMAL, "PARITAIRE", IAPRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR,
                                sitProf.getIdTiersEmployeur(), sitProf.getIdTiersPaiementEmployeur(),
                                sitProf.getIdDomainePaiementEmployeur(),
                                sitProf.getAssociation().getCodesystemToString(),
                                sitProf.getId(), sitProf.getNom(),
                                sitProf.getIdAffilie(),
                                //ESVE MATERNITE ici le type de la repartition est set � paritaire ou personelle mais pas utilis�
                                sitProf.getTypeAffiliation(),
                                sitProf.getIndependant(),
                                cotisationAvsParitaire,
                                cotisationAcParitaire
                        );
                        prestationCalculeeAPersister.getRepartitionsPaiementMap().add(repartitionPaiementData);

                        montantPrestationComplementBrut = montantPrestationComplementBrut.add(repartitionPaiementData
                                .getMontantBrut());
                        montantPrestationComplementNet = montantPrestationComplementNet.add(repartitionPaiementData
                                .getMontantNet());
                    }

                    if (cotisationAvsPersonelleData != null || cotisationAcPersonelleData != null) {
                        // cr�ation de la r�partition personelle
                        montantBrutRepartition = arrondir(montantBrutRepartition);
                        final APRepartitionPaiementData repartitionPaiementData2 = creerRepartition(
                                montantBrutRepartition, cotisationAvsPersonelleData, cotisationAcPersonelleData,
                                IAPRepartitionPaiements.CS_NORMAL, "PERSONELLE", IAPRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR,
                                sitProf.getIdTiersEmployeur(), sitProf.getIdTiersPaiementEmployeur(),
                                sitProf.getIdDomainePaiementEmployeur(),
                                sitProf.getAssociation().getCodesystemToString(),
                                sitProf.getId(), sitProf.getNom(),
                                sitProf.getIdAffilie(),
                                //ESVE MATERNITE ici le type de la repartition est set � paritaire ou personelle mais pas utilis�
                                sitProf.getTypeAffiliation(),
                                sitProf.getIndependant(),
                                cotisationAvsPersonnelle,
                                cotisationAcPersonnelle
                        );
                        prestationCalculeeAPersister.getRepartitionsPaiementMap().add(repartitionPaiementData2);

                        montantPrestationComplementBrut = montantPrestationComplementBrut.add(repartitionPaiementData2
                                .getMontantBrut());
                        montantPrestationComplementNet = montantPrestationComplementNet.add(repartitionPaiementData2
                                .getMontantNet());
                    }

                    // m�j montant prestation
                    montantPrestationComplementJournalier = montantPrestationComplementJournalier.add(sitProf
                            .getMontantJournalier());
                }
            }

            prestationCalculeeAPersister.setMontantNet(montantPrestationComplementNet);
            prestationCalculeeAPersister.setMontantBrut(montantPrestationComplementBrut);

            resultatCalcul.add(prestationCalculeeAPersister);


        }

        return resultatCalcul;
    }

    private BigDecimal getMontantFederal(BigDecimal montantJournalier, APPrestation prestation, int nombreJoursSoldesPeriodePriseEnCompte) {
        BigDecimal montantAllocationExploitation = new BigDecimal(prestation.getMontantAllocationExploitation());
        // dans le cas ou la prestation.getDateDebut() � �t� modifi� par la propri�t� PROPERTY_APG_FERCIAB_MATERNITE
        // il faut recalculer le salaire en fonction du nombreJoursSoldesPeriodePriseEnCompte
        montantJournalier = montantJournalier.multiply(BigDecimal.valueOf(nombreJoursSoldesPeriodePriseEnCompte));
        montantAllocationExploitation = montantAllocationExploitation.multiply(BigDecimal.valueOf(nombreJoursSoldesPeriodePriseEnCompte));
        BigDecimal fraisGarde = new BigDecimal(prestation.getFraisGarde());
        return montantJournalier.subtract(montantAllocationExploitation).subtract(fraisGarde);
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
            final String typePrestation, final String typeRepartition, final String typePaiement,
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

        // cr�ation de la r�partition
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
     * R�gle retrouv�e dans le code...</br>
     * arrondi � 2 chiffres apr�s la virgule, � 5cts pr�s.
     *
     * @param montant
     * @return
     */
    private BigDecimal arrondir(BigDecimal montant) {
        // arrondi � 2 chiffres apr�s la virgule, � 5cts pr�s.
        return new BigDecimal(JANumberFormatter
                .deQuote(JANumberFormatter.format(montant.toString(), 0.05, 2, JANumberFormatter.NEAR)));
    }


    /**
     * Cr�� l'objet en fonction des entit�s situation professionnelle et droit
     *
     * @param donneesPersistancePourCalcul
     * @return APPrestationStandardData
     * @throws JadePersistenceException
     * @throws Exception
     */
    @Override
    public List<APCalculateurComplementDonneeDomaine> persistenceToDomain(final APCalculateurComplementDonneesPersistence donneesPersistancePourCalcul) throws JadePersistenceException, Exception {

        // Donn�es en entr�e
        final List<APRepartitionJointPrestation> listePrestationsPersistance = donneesPersistancePourCalcul
                .getPrestationJointRepartitions();

        // Donn�es en sortie
        final List<APCalculateurComplementDonneeDomaine> listePrestationsComplementDomaineConverties = new ArrayList<>();


        Map<String, List<APRepartitionJointPrestation>> mapPrestations = new HashMap<>();
        // Pour chacune des prestations standard joint r�partition
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

        // Pour chacune des prestations standard Joint r�partition
        for (final APCalculateurComplementDonneeDomaine prestationStandard : listePrestationsComplementDomaineConverties) {

            prestationStandard.setTaux(donneesPersistancePourCalcul.getTaux());
            prestationStandard.setMontantsMax(donneesPersistancePourCalcul.getMontantsMax());

            // Recherche de la situation professionnelle
            final List<APSitProJointEmployeur> apSitProJoiEmpEntityList = donneesPersistancePourCalcul
                    .getSituationProfessionnelleEmployeur();

            String idSituationProfessionnelleCourante = "";
            AFAffiliationManager afManager = new AFAffiliationManager();

            FWCurrency sommeRevenuMoyenDeterminant = new FWCurrency(0);
            for (final APSitProJointEmployeur apSitProJoiEmpEnt : apSitProJoiEmpEntityList) {

                if (apSitProJoiEmpEnt != null) {
                    // nouvelle situation professionnelle
                    if (!idSituationProfessionnelleCourante.equals(apSitProJoiEmpEnt.getIdSitPro())) {
                        idSituationProfessionnelleCourante = apSitProJoiEmpEnt.getIdSitPro();

                        sommeRevenuMoyenDeterminant.add(donneesPersistancePourCalcul.getRevenuMoyenDeterminant(idSituationProfessionnelleCourante)); // sommme le revenu moyen d�terminant des diff�rentes situations proffessionelles

                        // D�finition de l'association
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

            prestationStandard.getPrestation().setRevenuMoyenDeterminant(sommeRevenuMoyenDeterminant.toString());
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
            // Prestation calcul�e convertie vers la persistence
            final APPrestationCalculeeAPersister prestationPersistente = new APPrestationCalculeeAPersister();

            final APPrestation prestation = new APPrestation();
            // Les prestations MATCIAB1 ne doivent pas �tre annonc�es
            prestation.setContenuAnnonce(null);
            prestation.setDateCalcul(JACalendar.todayJJsMMsAAAA());
            prestation.setDateDebut(prestationDomainCalculee.getDateDebut());
            prestation.setDateFin(prestationDomainCalculee.getDateFin());
            prestation.setIdDroit(prestationDomainCalculee.getIdDroit());
            prestation.setNombreJoursSoldes(String.valueOf(prestationDomainCalculee.getNombreDeJoursSoldes()));
            // assigne la nouvelle prestation en IPRDemande.CS_TYPE_MATERNITE
            prestation.setType(IPRDemande.CS_TYPE_MATERNITE);
            prestation.setMontantJournalier(arrondir(prestationDomainCalculee.getMontantJournalier()).toString());
            prestation.setMontantBrut(arrondir(prestationDomainCalculee.getMontantBrut()).toString());
            // assigne la nouvelle prestation avec le genre MATCIAB1
            prestation.setGenre(String.valueOf(APTypeDePrestation.MATCIAB1.getCodesystem()));
            // assigne le noRevision en CS_REVISION_MATERNITE_2005
            prestation.setNoRevision(IAPDroitMaternite.CS_REVISION_MATERNITE_2005);

            prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
            prestationPersistente.setPrestation(prestation);

            // Cr�ation des r�partitions des paiements et cotisations
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

                // Cr�ation des cotisations
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
