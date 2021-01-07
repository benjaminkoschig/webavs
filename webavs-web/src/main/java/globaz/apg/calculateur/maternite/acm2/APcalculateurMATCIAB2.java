package globaz.apg.calculateur.maternite.acm2;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.calculateur.IAPPrestationCalculateur;
import globaz.apg.calculateur.pojo.APPrestationCalculeeAPersister;
import globaz.apg.calculateur.pojo.APRepartitionCalculeeAPersister;
import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.db.prestation.APCotisation;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.enums.APTypeDePrestation;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.beans.PRPeriode;
import globaz.prestation.utils.PRDateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class APcalculateurMATCIAB2 implements IAPPrestationCalculateur<APPrestationCalculeeAPersister, ACM2BusinessDataParEmployeur, ACM2PersistenceInputData> {

    @Override
    public List<APPrestationCalculeeAPersister> calculerPrestation(List<ACM2BusinessDataParEmployeur> donneesDomainCalcul) throws Exception {

        if (donneesDomainCalcul == null || donneesDomainCalcul.size() == 0) {
            throw new Exception("Aucune données pour le calcul des MATCIAB2");
        }

        // On prend le premier employeur pour récupérer certaines données invariable d'un employeur à l'autre
        ACM2BusinessDataParEmployeur donneesEmployeurTmp = donneesDomainCalcul.get(0);
        String idDroit = donneesEmployeurTmp.getIdDroit();
        int nombreJoursMATCIAB2 = donneesEmployeurTmp.getNombreJoursPrestationACM2();
        PRPeriode periodeMATCIAB1ouStandard = null;

        /*
         * Calcul du montant journalier de la prestation MATCIAB2
         * ET recherche de la période MATCIAB1 ou Standard -> au moins un des employeurs doit payer des MATCIAB1 ou Standard !
         */
        FWCurrency sommeRevenuMoyenDeterminantMATCIAB2 = new FWCurrency("0");
        FWCurrency sommeRevenuMoyenDeterminantMATCIAB2Arrondi = new FWCurrency("0");
        for (ACM2BusinessDataParEmployeur object : donneesDomainCalcul) {
            sommeRevenuMoyenDeterminantMATCIAB2.add(object.getRevenuMoyenDeterminant());
            if (donneesEmployeurTmp.hasPrestationMATCIAB1() && periodeMATCIAB1ouStandard == null) {
                periodeMATCIAB1ouStandard = donneesEmployeurTmp.getPeriodeMATCIAB1();
            } else if (donneesEmployeurTmp.hasPrestationStandard() && periodeMATCIAB1ouStandard == null) {
                periodeMATCIAB1ouStandard = donneesEmployeurTmp.getPeriodeStandard();
            }
        }

        sommeRevenuMoyenDeterminantMATCIAB2 = new FWCurrency(String.valueOf(sommeRevenuMoyenDeterminantMATCIAB2.getBigDecimalValue()));
        sommeRevenuMoyenDeterminantMATCIAB2Arrondi = new FWCurrency(String.valueOf(arrondir(sommeRevenuMoyenDeterminantMATCIAB2.getBigDecimalValue())));

        if (periodeMATCIAB1ouStandard == null) {
            throw new Exception("Aucune période MATCIAB1 ni Standard trouvée");
        }

        // Calcul de la période initiale des MATCIAB2
        String ddfMATCIAB1ouStandard = periodeMATCIAB1ouStandard.getDateDeFin();
        String dateDebutMATCIAB2 = JadeDateUtil.addDays(ddfMATCIAB1ouStandard, 1);
        String dateFinMATCIAB2 = JadeDateUtil.addDays(ddfMATCIAB1ouStandard, nombreJoursMATCIAB2);
        dateFinMATCIAB2 = limiteDateFinMATCIAB2AvecMinDateFinContrat(donneesDomainCalcul, dateFinMATCIAB2);

        PRPeriode periodeMATCIAB2 = new PRPeriode(dateDebutMATCIAB2, dateFinMATCIAB2);

        SimpleDateFormat reader = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat anneeWriter = new SimpleDateFormat("yyyy");

        List<PRPeriode> periodes = new LinkedList<PRPeriode>();

        /*
         * Analyse de la période des MATCIAB2 pourvoir si on tombe a cheval sur 2 années
         */

        // On récupère l'année de la date de début de la période
        String annee = anneeWriter.format(reader.parse(periodeMATCIAB2.getDateDeDebut()));
        // On créé la date du 31.12 de l'année correspondante
        String finDeAnnee = "31.12." + annee;
        boolean result = PRDateUtils.isDateDansLaPeriode(periodeMATCIAB2, finDeAnnee);
        if (result) {
            PRPeriode p1 = new PRPeriode(periodeMATCIAB2.getDateDeDebut(), finDeAnnee);
            String debutAnnee = JadeDateUtil.addDays(finDeAnnee, 1);
            PRPeriode p2 = new PRPeriode(debutAnnee, periodeMATCIAB2.getDateDeFin());
            periodes.add(p1);
            periodes.add(p2);
        } else {
            periodes.add(periodeMATCIAB2);
        }

        List<APPrestationCalculeeAPersister> resultatCalcul = new LinkedList<>();

        // Pour chaque période on créer une prestation
        for (PRPeriode periode : periodes) {
            APPrestationCalculeeAPersister prestationCalculeeAPersister = new APPrestationCalculeeAPersister();

            String dateDeDebut = periode.getDateDeDebut();
            String dateDeFin = periode.getDateDeFin();
            int nombreDeJours = PRDateUtils.getNbDayBetween(dateDeDebut, dateDeFin);
            nombreDeJours++;

            BigDecimal montantBrut = sommeRevenuMoyenDeterminantMATCIAB2Arrondi.getBigDecimalValue().multiply(
                    new BigDecimal(nombreDeJours));
            montantBrut = arrondir(montantBrut);

            APPrestation prestationACreer = new APPrestation();
            prestationACreer.setDateCalcul(JACalendar.todayJJsMMsAAAA());
            prestationACreer.setDateDebut(dateDeDebut);
            prestationACreer.setDateFin(dateDeFin);
            prestationACreer.setEtat(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
            // assigne la nouvelle prestation avec le genre MATCIAB2
            prestationACreer.setGenre(APTypeDePrestation.MATCIAB2.getCodesystemString());
            prestationACreer.setIdDroit(idDroit);
            prestationACreer.setMontantBrut(montantBrut.toString());
            prestationACreer.setMontantJournalier(sommeRevenuMoyenDeterminantMATCIAB2Arrondi.toString());
            prestationACreer.setNombreJoursSoldes(String.valueOf(nombreDeJours));
            // assigne le noRevision en CS_REVISION_MATERNITE_2005
            prestationACreer.setNoRevision(IAPDroitMaternite.CS_REVISION_MATERNITE_2005);
            prestationACreer.setRevenuMoyenDeterminant(sommeRevenuMoyenDeterminantMATCIAB2Arrondi.toString());
            // assigne la nouvelle prestation en IPRDemande.CS_TYPE_MATERNITE
            prestationACreer.setType(IPRDemande.CS_TYPE_MATERNITE);

            prestationCalculeeAPersister.setPrestation(prestationACreer);
            List<APRepartitionCalculeeAPersister> repartitions = new LinkedList<APRepartitionCalculeeAPersister>();
            prestationCalculeeAPersister.setRepartitions(repartitions);

            // Pour chaque employeur on créer une répartition
            for (ACM2BusinessDataParEmployeur donneesParEmployeur : donneesDomainCalcul) {
                APRepartitionCalculeeAPersister repartitionCalculeeAPersister = new APRepartitionCalculeeAPersister();
                repartitions.add(repartitionCalculeeAPersister);

                APSitProJointEmployeur sitPro = donneesParEmployeur.getSituationProfJointEmployeur();

                // Création de la répartition de paiement, on se base sur la 1ère prestation standard
                // TODO à valider
                APRepartitionJointPrestation prestStandard = donneesParEmployeur.getPrestationStandard().get(0);
                APRepartitionPaiements repartitionPaiements = new APRepartitionPaiements();
                repartitionPaiements.setDateValeur(JACalendar.todayJJsMMsAAAA());
                repartitionPaiements.setIdAffilie(sitPro.getIdAffilie());
                repartitionPaiements.setIdSituationProfessionnelle(sitPro.getIdSitPro());
                repartitionPaiements.setIdAffilieAdrPmt(prestStandard.getIdAffilieAdrPmt());
                repartitionPaiements.setIdDomaineAdressePaiement(prestStandard.getIdDomaineAdressePaiement());
                repartitionPaiements.setIdTiers(prestStandard.getIdTiers());
                repartitionPaiements.setIdTiersAdressePaiement(prestStandard.getIdTiersAdressePaiement());
                repartitionPaiements.setNom(prestStandard.getNom());
                repartitionPaiements.setReferenceInterne(prestStandard.getReferenceInterne());
                repartitionPaiements.setTypePaiement(prestStandard.getTypePaiement());
                repartitionPaiements.setTypePrestation(IAPRepartitionPaiements.CS_NORMAL);

                // Recherche du taux prorata sur le salaire journalier de la situation selon 1 des repartitions
                // existants
                String tauxRJM = "";
                for (APRepartitionJointPrestation repartition : donneesParEmployeur.getPrestationStandard()) {
                    if (repartition.getIdSituationProfessionnelle().equals(sitPro.getIdSitPro())) {
                        tauxRJM = repartition.getTauxRJM();
                        break;
                    }
                }

                if (JadeStringUtil.isBlankOrZero(tauxRJM)) {
                    for (APRepartitionJointPrestation repartition : donneesParEmployeur.getPrestationACM1()) {
                        if (repartition.getIdSituationProfessionnelle().equals(sitPro.getIdSitPro())) {
                            tauxRJM = repartition.getTauxRJM();
                            break;
                        }
                    }
                }

                // calcul du montant brut par repartitions
                BigDecimal tauxCalcul; // proportion de la répartion par rapport a la somme des repartitions
                // si le nombre de prestation prise en compte pour MATCIAB2 est différent du nombre de situation proffessionelle prise en compte pour la maternité fédérale
                if (donneesParEmployeur.getNombreInitialDeSituationsProfessionelles() != donneesDomainCalcul.size()) {
                    tauxCalcul = donneesParEmployeur.getRevenuMoyenDeterminant().getBigDecimalValue().divide(sommeRevenuMoyenDeterminantMATCIAB2.getBigDecimalValue(), 4, RoundingMode.HALF_UP);
                } else {
                    tauxCalcul = new BigDecimal(tauxRJM).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
                }

                BigDecimal montantBrutRepartition = sommeRevenuMoyenDeterminantMATCIAB2Arrondi.getBigDecimalValue().multiply(tauxCalcul);
                montantBrutRepartition = arrondir(montantBrutRepartition);
                montantBrutRepartition = montantBrutRepartition.multiply(new BigDecimal(nombreDeJours));

                repartitionPaiements.setMontantBrut(montantBrutRepartition.toString());
                // repartitionPaiements.setIdRepartitionBeneficiairePaiement();
                repartitionPaiements.setTauxRJM(tauxRJM);
                // repartitionPaiements.setTypeAssociationAssurance(); spécifique ACM NE ?
                // TODO repartitionPaiements.chercherAdressePaiement(session);
                // repartitionPaiements.setAdressePaiement();

                // ---------------------------------------- //

                repartitionCalculeeAPersister.setRepartitionPaiements(repartitionPaiements);
                repartitionCalculeeAPersister.setCotisations(new ArrayList<APCotisation>());
                repartitionCalculeeAPersister.setNombreInitialDeSituationsProfessionelles(donneesParEmployeur.getNombreInitialDeSituationsProfessionelles());
            }

            prestationCalculeeAPersister.setRepartitions(repartitions);
            resultatCalcul.add(prestationCalculeeAPersister);
        }

        return resultatCalcul;
    }

    private String limiteDateFinMATCIAB2AvecMinDateFinContrat(List<ACM2BusinessDataParEmployeur> donneesDomainCalcul, String dateFinMATCIAB2) {
        // La situation professionele qui possède une date de fin de contrat la proche
        ACM2BusinessDataParEmployeur donneesEmployeurTmpMinDateFin = donneesDomainCalcul.get(0);
        for (ACM2BusinessDataParEmployeur object : donneesDomainCalcul) {
            PRDateUtils.PRDateEquality prestationEndDateCheck = PRDateUtils.compare(object.getSituationProfJointEmployeur().getDateFin(), donneesEmployeurTmpMinDateFin.getSituationProfJointEmployeur().getDateFin());
            if (donneesEmployeurTmpMinDateFin.getSituationProfJointEmployeur().getDateFin().isEmpty() || prestationEndDateCheck.equals(PRDateUtils.PRDateEquality.BEFORE)) {
                donneesEmployeurTmpMinDateFin = object;
            }
        }
        PRDateUtils.PRDateEquality prestationEndDateCheck = PRDateUtils.compare(dateFinMATCIAB2, donneesEmployeurTmpMinDateFin.getSituationProfJointEmployeur().getDateFin());
        if (prestationEndDateCheck.equals(PRDateUtils.PRDateEquality.BEFORE)) {
            dateFinMATCIAB2 = donneesEmployeurTmpMinDateFin.getSituationProfJointEmployeur().getDateFin();
        }
        return dateFinMATCIAB2;
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
        return new BigDecimal(JANumberFormatter.deQuote(JANumberFormatter.format(montant.toString(), 0.05, 2,
                JANumberFormatter.NEAR)));
    }

    /**
     * Règle retrouvée dans le code...</br>
     * arrondi à 2 chiffres après la virgule, à 1Chf près.
     *
     * @param montant
     * @return
     */
    private BigDecimal arrondirFranc(BigDecimal montant) {
        // arrondi à 2 chiffres après la virgule, à 1chf près.
        return new BigDecimal(JANumberFormatter.deQuote(JANumberFormatter.format(montant.toString(), 1, 2,
                JANumberFormatter.SUP)));
    }

    @Override
    public List<ACM2BusinessDataParEmployeur> persistenceToDomain(ACM2PersistenceInputData donneesDepuisPersistance) throws Exception {
        ACM2PersistenceInputData persistenceInputData = donneesDepuisPersistance;

        List<ACM2BusinessDataParEmployeur> donneesBusiness = new LinkedList<>();
        // On boucle sur les sit prof
        for (APSitProJointEmployeur sitPro : persistenceInputData.getSituationProfessionnelleEmployeur()) {

                ACM2BusinessDataParEmployeur donneesEmployeur = new ACM2BusinessDataParEmployeur(
                        persistenceInputData.getIdDroit(), persistenceInputData.getNombreJoursPrestationACM2(), sitPro);
                donneesEmployeur.setRevenuMoyenDeterminant(persistenceInputData.getRevenuMoyenDeterminant(sitPro
                        .getIdSitPro()));

                for (APRepartitionJointPrestation prestation : persistenceInputData.getPrestationJointRepartitions()) {

                    if (sitPro.getIdSitPro().equals(prestation.getIdSituationProfessionnelle())) {

                        // Est-ce que c'est la prestation standard
                        if (APTypeDePrestation.STANDARD.getCodesystemString().equals(
                                prestation.getGenrePrestationPrestation())) {
                            donneesEmployeur.addPrestationStandard(prestation);
                            continue;
                        }

                        // Est-ce que c'est la prestation MATCIAB1
                        if (APTypeDePrestation.MATCIAB1.getCodesystemString().equals(
                                prestation.getGenrePrestationPrestation())) {
                            donneesEmployeur.addPrestationMATCIAB1(prestation);
                            continue;
                        }

                    }
                }
                donneesEmployeur.setNombreInitialDeSituationsProfessionelles(donneesDepuisPersistance.getNombreInitialDeSituationsProfessionelles());
                donneesBusiness.add(donneesEmployeur);

        }
        return donneesBusiness;
    }

    private boolean hasACM2(APSitProJointEmployeur sitPro) {
        return sitPro.getHasAcm2AlphaPrestations() && sitPro.getIsVersementEmployeur() != null
                && sitPro.getIsVersementEmployeur().booleanValue();
    }

    @Override
    public List<APPrestationCalculeeAPersister> domainToPersistence(List<APPrestationCalculeeAPersister> resultat) throws Exception {
        return resultat;
    }

}
