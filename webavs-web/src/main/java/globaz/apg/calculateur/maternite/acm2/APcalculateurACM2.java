package globaz.apg.calculateur.maternite.acm2;

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
import globaz.prestation.beans.PRPeriode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class APcalculateurACM2 implements IAPPrestationCalculateur {

    @Override
    public List<Object> calculerPrestation(List<Object> donneesDomainCalcul) throws Exception {
        List<APRepartitionCalculeeAPersister> list = new LinkedList<APRepartitionCalculeeAPersister>();

        if (donneesDomainCalcul == null || donneesDomainCalcul.size() == 0) {
            throw new Exception("Aucune données pour le calcul des ACM 2");
        }

        // On prend le premier employeur pour récupérer certaines données invariable d'un employeur à l'autre
        ACM2BusinessDataParEmployeur donneesEmployeur = (ACM2BusinessDataParEmployeur) donneesDomainCalcul.get(0);

        BigDecimal tauxAVS = donneesEmployeur.getTauxAVS();
        BigDecimal tauxAC = donneesEmployeur.getTauxAC();
        String idDroit = donneesEmployeur.getIdDroit();
        int nombreJoursACM2 = donneesEmployeur.getNombreJoursPrestationACM2();
        PRPeriode periodeACM1 = null;

        /*
         * Calcul du montant journalier de la prestation ACM 2
         * ET recherche de la période ACM 1 -> au moins un des employeurs doit payer des ACM 1 !
         */
        FWCurrency montantJournalierACM2 = new FWCurrency("0");
        for (Object object : donneesDomainCalcul) {
            montantJournalierACM2.add(((ACM2BusinessDataParEmployeur) object).getRevenuMoyenDeterminant());
            if (donneesEmployeur.hasPrestationACM1() && periodeACM1 == null) {
                periodeACM1 = donneesEmployeur.getPeriodeACM1();
            }
        }

        if (periodeACM1 == null) {
            // throw new Exception("Aucune période ACM 1 trouvée");
        }
        // Calcul de la période ACM 2
        String dateDebutACM2 = null;
        String dateFinACM2 = null;

        // Calcul de la période des ACM 1
        String ddfACM1 = periodeACM1.getDateDeFin();
        dateDebutACM2 = JadeDateUtil.addDays(ddfACM1, 1);
        dateFinACM2 = JadeDateUtil.addDays(ddfACM1, nombreJoursACM2);

        BigDecimal montantBrut = montantJournalierACM2.getBigDecimalValue().multiply(new BigDecimal(nombreJoursACM2));
        montantBrut = arrondir(montantBrut);

        APPrestation prestationACreer = new APPrestation();
        prestationACreer.setDateCalcul(JACalendar.todayJJsMMsAAAA());
        prestationACreer.setDateDebut(dateDebutACM2);
        prestationACreer.setDateFin(dateFinACM2);
        prestationACreer.setEtat(IAPPrestation.CS_ETAT_PRESTATION_OUVERT);
        prestationACreer.setGenre(APTypeDePrestation.ACM2_ALFA.getCodesystemString());
        prestationACreer.setIdDroit(idDroit);
        prestationACreer.setMontantBrut(montantBrut.toString());
        // TODO gérer arrondis
        prestationACreer.setMontantJournalier(montantJournalierACM2.toString());
        prestationACreer.setNombreJoursSoldes(String.valueOf(nombreJoursACM2));
        prestationACreer.setType(IAPPrestation.CS_TYPE_NORMAL);

        APPrestationCalculeeAPersister prestationCalculeeAPersister = new APPrestationCalculeeAPersister();
        prestationCalculeeAPersister.setPrestation(prestationACreer);

        // Pour chaque employeur
        for (Object object : donneesDomainCalcul) {
            ACM2BusinessDataParEmployeur donneesParEmployeur = (ACM2BusinessDataParEmployeur) object;
            APSitProJointEmployeur sitPro = donneesParEmployeur.getSituationProfJointEmployeur();
            // BigDecimal montantJournalier = null;
            // BigDecimal montantBrut = null;

            // /*
            // * Calcul du montant journalier, 2 cas possible; avec LAMat et sans....
            // */
            // String montantJournalierString = null;
            // if (hasLAMat) {
            // // TODO
            // } else {
            // // FIXME prendre ce revenu
            // // APPrestation prestation = new APPrestation();
            // // prestation.getRevenuMoyenDeterminant()
            // // Pas de LAMat, on prend le montant de la dernière prestation ACM1
            // montantJournalierString = prestationStandard.get(prestationStandard.size() - 1).getMontantJournalier();
            // }

            // montantJournalier = new BigDecimal(montantJournalierString);

            // création de la prestation

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
            repartitionPaiements.setTypePrestation(IAPRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR);

            // calcul du montant brut
            BigDecimal montantBrutRepartition = donneesParEmployeur.getRevenuMoyenDeterminant().getBigDecimalValue()
                    .multiply(new BigDecimal(nombreJoursACM2));
            montantBrutRepartition = arrondir(montantBrutRepartition);
            repartitionPaiements.setMontantBrut(montantBrutRepartition.toString());
            // repartitionPaiements.setIdRepartitionBeneficiairePaiement();
            // repartitionPaiements.setTauxRJM(); taux prorata ????
            // repartitionPaiements.setTypeAssociationAssurance(); spécifique ACM NE ?
            // TODO repartitionPaiements.chercherAdressePaiement(session);
            // repartitionPaiements.setAdressePaiement();
            // TODO voir avec SCR pour le calcul des coti : repartitionPaiements.setMontantVentile();

            // ---------------------------------------- //

            // Création des cotisations
            List<APCotisation> cotisations = new ArrayList<APCotisation>();
            // final APCotisation cotisationAvs = creerCotisation(prestationACreer, tauxAVS,
            // APProperties.ASSURANCE_AVS_PAR_ID.getValue());
            // cotisations.add(cotisationAvs);
            //
            // final APCotisation cotisationAc = creerCotisation(prestationACreer, tauxAC,
            // APProperties.ASSURANCE_AC_PAR_ID.getValue());
            // cotisations.add(cotisationAc);

            APRepartitionCalculeeAPersister repartitionCalculeeAPersister = new APRepartitionCalculeeAPersister();
            repartitionCalculeeAPersister.setRepartitionPaiements(repartitionPaiements);
            repartitionCalculeeAPersister.setCotisations(cotisations);

            list.add(repartitionCalculeeAPersister);

        }

        prestationCalculeeAPersister.setRepartitions(list);
        List<Object> resultat = new ArrayList<Object>();
        resultat.add(prestationCalculeeAPersister);
        return resultat;
    }

    private APCotisation creerCotisation(final APPrestation prestation, final BigDecimal taux, String idExterne) {
        BigDecimal montantBrutCotisation = new BigDecimal(prestation.getMontantBrut()).multiply(taux);
        montantBrutCotisation = arrondir(montantBrutCotisation);
        APCotisation cotisation = new APCotisation();
        cotisation.setDateDebut(prestation.getDateDebut());
        cotisation.setDateFin(prestation.getDateFin());
        cotisation.setMontant(montantBrutCotisation.toString());
        cotisation.setMontantBrut(prestation.getMontantBrut());
        cotisation.setTaux(taux.toString());
        cotisation.setType(APCotisation.TYPE_ASSURANCE);
        cotisation.setIdExterne(idExterne);
        return cotisation;
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

    @Override
    public List<Object> persistenceToDomain(Object donneesDepuisPersistance) throws Exception {
        ACM2PersistenceInputData persistenceInputData = (ACM2PersistenceInputData) donneesDepuisPersistance;

        List<Object> donneesBusiness = new LinkedList<Object>();
        // On boucle sur les sit prof
        for (APSitProJointEmployeur sitPro : persistenceInputData.getSituationProfessionnelleEmployeur()) {
            // Doit-on calculer de l'ACM2 pour cette sit prof
            if (hasACM2(sitPro)) {

                ACM2BusinessDataParEmployeur donneesEmployeur = new ACM2BusinessDataParEmployeur(
                        persistenceInputData.getIdDroit(), persistenceInputData.getNombreJoursPrestationACM2(), sitPro);
                donneesEmployeur.setTauxAVS(persistenceInputData.getTauxAVS());
                donneesEmployeur.setTauxAC(persistenceInputData.getTauxAC());
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

                        // Est-ce que c'est la prestation ACM1
                        if (APTypeDePrestation.ACM_ALFA.getCodesystemString().equals(
                                prestation.getGenrePrestationPrestation())) {
                            donneesEmployeur.addPrestationACM1(prestation);
                            continue;
                        }
                        // Est-ce que c'est la prestation LAMat
                        if (APTypeDePrestation.LAMAT.getCodesystemString().equals(
                                prestation.getGenrePrestationPrestation())) {
                            donneesEmployeur.addPrestationLAMat(prestation);
                            continue;
                        }
                    }
                }
                donneesBusiness.add(donneesEmployeur);
            }
        }
        return donneesBusiness;
    }

    private boolean hasACM2(APSitProJointEmployeur sitPro) {
        return sitPro.getHasAcm2AlphaPrestations() && sitPro.getIsVersementEmployeur() != null
                && sitPro.getIsVersementEmployeur().booleanValue();
    }

    @Override
    public List<APPrestationCalculeeAPersister> domainToPersistence(List<Object> resultat) throws Exception {
        List<APPrestationCalculeeAPersister> list = new ArrayList<APPrestationCalculeeAPersister>();
        for (Object o : resultat) {
            list.add((APPrestationCalculeeAPersister) o);
        }
        return list;
    }

}
