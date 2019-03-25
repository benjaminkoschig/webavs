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
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.exception.JadePersistenceException;

public class APCalculateurComplement implements IAPPrestationCalculateur<APCalculateurComplementDonneesPersistence, APCalculateurComplementDonneeDomaine, APCalculateurComplementDonneesPersistence> {
    
    @Override
    public List<APCalculateurComplementDonneesPersistence> calculerPrestation(List<APCalculateurComplementDonneeDomaine> donneesDomainCalcul) throws Exception {
        //

        if (donneesDomainCalcul == null || donneesDomainCalcul.size() == 0) {
            throw new Exception("Aucune donn�es pour le calcul des compl�ments");
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
            String idSituationProf = prestationStandard.getRepartitions().get(0).getIdSituationProfessionnelle();
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
            if (montantBrut.compareTo(montantBrutFederal) > 0) {
                montantBrut = montantBrut.subtract(montantBrutFederal);
                prestationCalculeeAPersister.setMontantBrut(montantBrut);
                prestationCalculeeAPersister.setMontantJournalier(montantBrut.divide(BigDecimal.valueOf(calculateur.getNbJourPrisEnCompte()),2, RoundingMode.HALF_UP));
                prestationCalculeeAPersister.setNombreDeJoursSoldes(calculateur.getNbJourPrisEnCompte());
            } else {
                return resultatCalcul;
            }

            BigDecimal montantPrestationComplementJournalier = new BigDecimal("0");
            BigDecimal montantPrestationComplementBrut = new BigDecimal("0");
            BigDecimal montantPrestationComplementNet = new BigDecimal("0");
            
            for(APRepartitionJointPrestation repartition : prestationStandard.getRepartitions()) {
                APSituationProfessionnelleCanton sitProf = prestationStandard.getSituationProfessionnelle().get(repartition.getIdSituationProfessionnelle());
                // tester si sitProf cotise
                BigDecimal montantBrutReparti =  prestationCalculeeAPersister.getMontantJournalier().multiply((new BigDecimal(repartition.getTauxRJM()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)));
                

                //if (isCalculPrestationAcmNeRequis(sitProf)) {
                        
                        final BigDecimal montantBrutRepartition = getMontantBrutRepartition(
                                montantBrutReparti, prestationCalculeeAPersister.getNombreDeJoursSoldes());
                        final BigDecimal[] tauxAvsAcFne = prestationStandard.getTaux().get(sitProf.getId());

                        // Cr�ation des cotisations
                        final APCotisationData cotisationAvs = creerCotisation(montantBrutRepartition, tauxAvsAcFne[0],
                                APProperties.ASSURANCE_AVS_PAR_ID.getValue());
                        final APCotisationData cotisationAc = creerCotisation(montantBrutRepartition, tauxAvsAcFne[1],
                                APProperties.ASSURANCE_AC_PAR_ID.getValue());
                        APCotisationData cotisationFne = null;
                        // si FNE, ajout d'une cotisation suppl�mentaire
//                        if (isAssociationFne(sitProf)) {
//                            // Pour du FNE, Le montant est le montant brut de la repartition de la prestation
//                            // standard
//                            // correspondante plus le montant brut de la repartition ACM
//                            BigDecimal montantBrutCotisationFne = montantBrutRepartition.add(prestationStandard
//                                    .getRepartitions().get(sitProf.getId()));
//                            cotisationFne = creerCotisation(montantBrutCotisationFne, tauxAvsAcFne[2],
//                                    APProperties.ASSURANCE_FNE_ID.getValue());
//                        }

                        // cr�ation de la r�partition
                        final APRepartitionPaiementData repartitionPaiementData = creerRepartition(
                                montantBrutRepartition, cotisationAvs, cotisationAc, cotisationFne,
                                IAPRepartitionPaiements.CS_NORMAL, IAPRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR,
                                sitProf.getIdTiersEmployeur(), sitProf.getIdTiersPaiementEmployeur(),
                                sitProf.getIdDomainePaiementEmployeur(), sitProf.getAssociation()
                                        .getCodesystemToString(), sitProf.getId(), sitProf.getNom(),
                                        sitProf.getIdAffilie());
                        prestationCalculeeAPersister.getRepartitionsPaiementMap().add(repartitionPaiementData);

                        // m�j montant prestation
                        montantPrestationComplementJournalier = montantPrestationComplementJournalier.add(sitProf
                                .getMontantJournalier());
                        montantPrestationComplementBrut = montantPrestationComplementBrut.add(repartitionPaiementData
                                .getMontantBrut());
                        montantPrestationComplementNet = montantPrestationComplementNet.add(repartitionPaiementData
                                .getMontantNet());
               // }
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
            final APCotisationData cotisationFne, final String typePrestation, final String typePaiement,
            final String idTiersEmployeur, final String idTiersPaiementEmployeur,
            final String idDomainePaiementEmployeur, final String typeAssociationAssurance,
            final String idSituationProfessionnelle, final String nom, final String idAffilie) {

        final BigDecimal montantNetRepartition = getMontantNetRepartition(montantBrutRepartition,
                arrondir(cotisationAvs.getMontantCotisation()), arrondir(cotisationAc.getMontantCotisation()),
                cotisationFne == null ? new BigDecimal("0") : arrondir(cotisationFne.getMontantCotisation()));

        // cr�ation de la r�partition
        final APRepartitionPaiementData repartition = new APRepartitionPaiementData(montantBrutRepartition,
                montantNetRepartition, typePrestation, typePaiement, idTiersEmployeur, idTiersPaiementEmployeur,
                idDomainePaiementEmployeur, typeAssociationAssurance, idSituationProfessionnelle, nom, idAffilie);
        repartition.getCotisations().add(cotisationAvs);
        repartition.getCotisations().add(cotisationAc);
        if (cotisationFne != null) {
            repartition.getCotisations().add(cotisationFne);
        }

        return repartition;

    }
    
    private BigDecimal getMontantBrutCotisation(final BigDecimal montantBrutRepartition, final BigDecimal tauxAc) {
        final BigDecimal montantBrutCotisation = montantBrutRepartition.multiply(tauxAc);
        return montantBrutCotisation;
    }
    
    private BigDecimal getMontantBrutRepartition(final BigDecimal montantJournalierAcmNe, final int nombreDeJoursSoldes) {
        return montantJournalierAcmNe.multiply(new BigDecimal(nombreDeJoursSoldes));
    }
    
    private BigDecimal getMontantNetRepartition(final BigDecimal montantBrutRepartition,
            final BigDecimal montantCotisationAvs, final BigDecimal montantCotisationAc,
            final BigDecimal montantCotisationFne) {

        final BigDecimal montantNetRepartition = montantBrutRepartition.add(montantCotisationAvs)
                .add(montantCotisationAc).add(montantCotisationFne);

        return montantNetRepartition;
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
            APCalculateurComplementDonneeDomaine donneeDomaine = new  APCalculateurComplementDonneeDomaine(prestation);
            donneeDomaine.setRepartitions(mapPrestations.get(prestation.getIdPrestationApg()));
            listePrestationsComplementDomaineConverties.add(donneeDomaine);
        }

        Map<String, APPrestation> listePrestationsStandard = new HashMap<>();
        
        for(APPrestation prestation : donneesPersistancePourCalcul.getListPrestationStandard()) {
            listePrestationsStandard.put(prestation.getIdPrestationApg(), prestation);
        }


        // Pour chacune des prestations standard Joint r�partition
        for (final APCalculateurComplementDonneeDomaine prestationStandard : listePrestationsComplementDomaineConverties) {
            
            prestationStandard.setTaux(donneesPersistancePourCalcul.getTaux());
            prestationStandard.setMontantsMax(donneesPersistancePourCalcul.getMontantsMax());

            // Recherche de la situation professionnelle
            final List<APSitProJointEmployeur> apSitProJoiEmpEntityList = donneesPersistancePourCalcul
                    .getSituationProfessionnelleEmployeur();

            String idSituationProfessionnelleCourante = "";

            for (final APSitProJointEmployeur apSitProJoiEmpEnt : apSitProJoiEmpEntityList) {

                if (apSitProJoiEmpEnt != null) {
                    // nouvelle situation professionnelle
                    if (!idSituationProfessionnelleCourante.equals(apSitProJoiEmpEnt.getIdSitPro())) {
                        idSituationProfessionnelleCourante = apSitProJoiEmpEnt.getIdSitPro();
                        // D�finition de l'association
                        final APAssuranceTypeAssociation association = getAssociationFromSituationProf(apSitProJoiEmpEnt);

                        APSituationProfessionnelleCanton sitProf = new APSituationProfessionnelleCanton(association, apSitProJoiEmpEnt.getDateDebut(),
                                apSitProJoiEmpEnt.getDateFin(), 
                                new BigDecimal(apSitProJoiEmpEnt
                                        .getMontantJournalierAcmNe()), 
                                apSitProJoiEmpEnt.getIdSitPro(),
                                apSitProJoiEmpEnt.getIdTiers(), apSitProJoiEmpEnt.getIdAffilie(),
                                apSitProJoiEmpEnt.getNom(), apSitProJoiEmpEnt.getIdTiersPaiementEmployeur(),
                                apSitProJoiEmpEnt.getIdDomainePaiementEmployeur());
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
                // Les prestations ACM ne doivent pas �tre annonc�es
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
