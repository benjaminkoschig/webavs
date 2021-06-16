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
import globaz.prestation.beans.PRPeriode;
import globaz.prestation.utils.PRDateUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class APcalculateurACM2 implements IAPPrestationCalculateur<APPrestationCalculeeAPersister, ACM2BusinessDataParEmployeur, ACM2PersistenceInputData> {

    @Override
    public List<APPrestationCalculeeAPersister> calculerPrestation(List<ACM2BusinessDataParEmployeur> donneesDomainCalcul) throws Exception {
        //

        if (donneesDomainCalcul == null || donneesDomainCalcul.size() == 0) {
            throw new Exception("Aucune données pour le calcul des ACM 2");
        }

        // On prend le premier employeur pour récupérer certaines données invariable d'un employeur à l'autre
        ACM2BusinessDataParEmployeur donneesEmployeurTmp = donneesDomainCalcul.get(0);
        String idDroit = donneesEmployeurTmp.getIdDroit();
        int nombreJoursACM2 = donneesEmployeurTmp.getNombreJoursPrestationACM2();
        PRPeriode periodeACM1 = null;
        PRPeriode periodeLAMat = null;

        /*
         * Calcul du montant journalier de la prestation ACM 2
         * ET recherche de la période ACM 1 -> au moins un des employeurs doit payer des ACM 1 !
         */
        FWCurrency revenuMoyenDeterminantACM2 = new FWCurrency("0");
        for (ACM2BusinessDataParEmployeur object : donneesDomainCalcul) {
            revenuMoyenDeterminantACM2.add(object.getRevenuMoyenDeterminant());
            if (donneesEmployeurTmp.hasPrestationACM1() && periodeACM1 == null) {
                periodeACM1 = donneesEmployeurTmp.getPeriodeACM1();
            }
            if (donneesEmployeurTmp.hasPrestationLAMat() && periodeLAMat == null) {
                periodeLAMat = donneesEmployeurTmp.getPeriodeACM1();
            }
        }

        if (periodeACM1 == null) {
            throw new Exception("Aucune période ACM 1 trouvée");
        }

        boolean hasLAMat = periodeLAMat != null;

        // Calcul de la période initiale des ACM 2
        String ddfACM1 = periodeACM1.getDateDeFin();
        String dateDebutACM2 = JadeDateUtil.addDays(ddfACM1, 1); // TODO ESVE MAT EXT ACM2 Pas limité par date de reprise
        String dateFinACM2 = JadeDateUtil.addDays(ddfACM1, nombreJoursACM2);
        PRPeriode periodeACM2 = new PRPeriode(dateDebutACM2, dateFinACM2);

        SimpleDateFormat reader = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat anneeWriter = new SimpleDateFormat("yyyy");

        List<PRPeriode> periodes = new LinkedList<PRPeriode>();

        /*
         * Analyse de la période des ACM2 pourvoir si on tombe a cheval sur 2 années
         */

        // On récupère l'année de la date de début de la période
        String annee = anneeWriter.format(reader.parse(periodeACM2.getDateDeDebut()));
        // On créé la date du 31.12 de l'année correspondante
        String finDeAnnee = "31.12." + annee;
        boolean result = PRDateUtils.isDateDansLaPeriode(periodeACM2, finDeAnnee);
        if (result) {
            PRPeriode p1 = new PRPeriode(periodeACM2.getDateDeDebut(), finDeAnnee);
            String debutAnnee = JadeDateUtil.addDays(finDeAnnee, 1);
            PRPeriode p2 = new PRPeriode(debutAnnee, periodeACM2.getDateDeFin());
            periodes.add(p1);
            periodes.add(p2);
        } else {
            periodes.add(periodeACM2);
        }

        List<APPrestationCalculeeAPersister> resultatCalcul = new LinkedList<>();

        // Pour chaque période on créer une prestation
        for (PRPeriode periode : periodes) {
            APPrestationCalculeeAPersister prestationCalculeeAPersister = new APPrestationCalculeeAPersister();

            String dateDeDebut = periode.getDateDeDebut();
            String dateDeFin = periode.getDateDeFin();
            int nombreDeJours = PRDateUtils.getNbDayBetween(dateDeDebut, dateDeFin);
            nombreDeJours++;

            BigDecimal montantBrut = revenuMoyenDeterminantACM2.getBigDecimalValue().multiply(
                    new BigDecimal(nombreDeJours));
            montantBrut = arrondir(montantBrut);

            APPrestation prestationACreer = new APPrestation();
            prestationACreer.setDateCalcul(JACalendar.todayJJsMMsAAAA());
            prestationACreer.setDateDebut(dateDeDebut);
            prestationACreer.setDateFin(dateDeFin);
            prestationACreer.setEtat(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
            prestationACreer.setGenre(APTypeDePrestation.ACM2_ALFA.getCodesystemString());
            prestationACreer.setIdDroit(idDroit);
            prestationACreer.setMontantBrut(montantBrut.toString());
            prestationACreer.setMontantJournalier(revenuMoyenDeterminantACM2.toString());
            prestationACreer.setNombreJoursSoldes(String.valueOf(nombreDeJours));
            prestationACreer.setNoRevision(IAPDroitMaternite.CS_REVISION_MATERNITE_2005);
            prestationACreer.setRevenuMoyenDeterminant(revenuMoyenDeterminantACM2.toString());
            prestationACreer.setType(IAPPrestation.CS_TYPE_NORMAL);

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

                // calcul du montant brut
                BigDecimal montantBrutRepartition = donneesParEmployeur.getRevenuMoyenDeterminant()
                        .getBigDecimalValue().multiply(new BigDecimal(nombreDeJours));
                montantBrutRepartition = arrondir(montantBrutRepartition);
                repartitionPaiements.setMontantBrut(montantBrutRepartition.toString());
                // repartitionPaiements.setIdRepartitionBeneficiairePaiement();
                repartitionPaiements.setTauxRJM(tauxRJM);
                // repartitionPaiements.setTypeAssociationAssurance(); spécifique ACM NE ?
                // TODO repartitionPaiements.chercherAdressePaiement(session);
                // repartitionPaiements.setAdressePaiement();

                // ---------------------------------------- //

                repartitionCalculeeAPersister.setRepartitionPaiements(repartitionPaiements);
                repartitionCalculeeAPersister.setCotisations(new ArrayList<APCotisation>());
            }

            prestationCalculeeAPersister.setRepartitions(repartitions);
            resultatCalcul.add(prestationCalculeeAPersister);
        }

        return resultatCalcul;
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
    public List<ACM2BusinessDataParEmployeur> persistenceToDomain(ACM2PersistenceInputData donneesDepuisPersistance) throws Exception {
        ACM2PersistenceInputData persistenceInputData = donneesDepuisPersistance;

        List<ACM2BusinessDataParEmployeur> donneesBusiness = new LinkedList<>();
        // On boucle sur les sit prof
        for (APSitProJointEmployeur sitPro : persistenceInputData.getSituationProfessionnelleEmployeur()) {
            // Doit-on calculer de l'ACM2 pour cette sit prof
            if (hasACM2(sitPro)) {

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
    public List<APPrestationCalculeeAPersister> domainToPersistence(List<APPrestationCalculeeAPersister> resultat) throws Exception {
        return resultat;
    }

}
