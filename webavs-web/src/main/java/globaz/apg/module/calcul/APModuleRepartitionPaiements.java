package globaz.apg.module.calcul;

import globaz.apg.acor.parser.APACORPrestationsParser;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.*;
import globaz.apg.db.prestation.APCotisation;
import globaz.apg.db.prestation.APCotisationManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.db.prestation.APRepartitionPaiementsManager;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.helpers.droits.APSituationProfessionnelleHelper;
import globaz.apg.module.calcul.wrapper.APMontantJour;
import globaz.apg.services.APRechercherAssuranceFromDroitCotisationService;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAVector;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.properties.JadePropertiesService;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.api.IAFAssurance;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tauxImposition.api.IPRTauxImposition;
import globaz.prestation.tools.PRCalcul;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Descpription.
 * 
 * @author scr Date de création 31 mai 05
 */
public class APModuleRepartitionPaiements {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    class CasCotisations {

        public static final String COLLAB_AGR = "103";

        public static final String EMPLOYEUR_SANS_SALAIRE = "109";
        public static final String INDEPENDANT = "102";
        public static final String NON_ACTIF = "105";
        public static final String SALARIE = "100";
        public static final String SANS_EMPLOYEUR_AP20 = "108";
        public static final String SANS_EMPLOYEUR_AV18 = "106";
        public static final String SANS_EMPLOYEUR_AV21 = "107";
        public static final String TRAVAILLEUR_AGR = "104";
        public Map mapCasCotisations = new HashMap();
    }

    class Cotisations {
        public boolean isVersementEmployeur = false;
        public FWCurrency montant = null;
    }

    private static final boolean ADDITION = true;

    private static final JACalendar CALENDAR = new JACalendarGregorian();

    private static final boolean SOUSTRACTION = false;

    private void ajouterLigneImpotSource(BSession session, APPrestationCalculee prestation,
            APRepartitionPaiements repa, String idTauxImposition, String taux) throws Exception {
        // creer la cotisation
        APCotisation impot = new APCotisation();

        impot.setIdExterne(idTauxImposition);
        impot.setTaux(taux);
        impot.setSession(session);
        impot.setDateDebut(prestation.getDateDebut().toStr("."));
        impot.setDateFin(prestation.getDateFin().toStr("."));
        impot.setIdRepartitionBeneficiairePaiement(repa.getIdRepartitionBeneficiairePaiement());
        impot.setType(APCotisation.TYPE_IMPOT);

        // calculer le montant
        impot.setMontantBrut(repa.getMontantBrut());
        impot.setMontant("-"
                + JANumberFormatter.round(String.valueOf(PRCalcul.pourcentage100(repa.getMontantBrut(), taux)), 0.05,
                        2, JANumberFormatter.NEAR));

        impot.setSession(session);
        impot.add();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * renvoie une Map contenant une APCotisation en key et le nombre de jours en String en value.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param prestation
     *            DOCUMENT ME!
     * @param repa
     *            DOCUMENT ME!
     * @param idAssurance
     *            DOCUMENT ME!
     * @param ajouter
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * @throws APCalculException
     *             DOCUMENT ME!
     */
    private APCotisation[] ajouterLignesCotisations(BSession session, APPrestationCalculee prestation,
            APRepartitionPaiements repa, String idAssurance, boolean ajouter) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(idAssurance)) {
            // l'assurance n'est pas definie, on n'ajoute pas de lignes
            return new APCotisation[0];
        }

        // la liste qui va servir à retourner ce qui a été créé
        List cotisations = new ArrayList();

        List taux = PRAffiliationHelper.getTauxAssurance(session, idAssurance, "",
                prestation.getDateDebut().toStr("."), prestation.getDateDebut().toStr("."));

        if (taux.isEmpty()) {
            throw new APCalculException("les taux pour cette assurance " + idAssurance + " ne sont pas valables");
        }

        int nbJoursSoldes = Integer.parseInt(prestation.getNombreJoursSoldes());
        int nbJoursSoldesRestants = nbJoursSoldes;
        double montantBrut = Double.parseDouble(repa.getMontantBrut());

        /*
         * ajouter tous les taux dans l'ordre. s'il y a plus d'un taux, on va essayer de mettre un maximum de jours
         * soldes su les premiers taux. Par exemple, s'il y a 20 jours soldes et deux taux, le second debutant 15 jours
         * apres le debut de la prestation, on va mettre 15 jours soldes sur le premier taux et les 5 restants sur le
         * second taux.
         */
        for (int idTaux = 0; idTaux < taux.size(); ++idTaux) {
            AFTauxAssurance tauxCourant = (AFTauxAssurance) taux.get(idTaux);

            // creer la cotisation
            APCotisation assurance = new APCotisation();

            assurance.setType(APCotisation.TYPE_ASSURANCE);
            assurance.setIdExterne(tauxCourant.getAssuranceId());
            assurance.setSession(session);
            assurance.setIdRepartitionBeneficiairePaiement(repa.getIdRepartitionBeneficiairePaiement());

            if (idTaux > 0) {
                assurance.setDateDebut(tauxCourant.getDateDebut());
            } else {
                assurance.setDateDebut(prestation.getDateDebut().toStr("."));
            }

            // determiner le nombre de jours soldes pour ce taux
            JADate dateFinAssurance = new JADate(tauxCourant.getDateFin());
            int jours;

            if (JACalendar.isNull(dateFinAssurance)) {
                // le taux n'a pas de date de fin, on met tous les jours soldes
                // sur ce taux
                jours = nbJoursSoldesRestants;
                assurance.setDateFin(prestation.getDateFin().toStr("."));
            } else {
                // le taux a une date de fin, ce qui signifie qu'il doit y avoir
                // un autre taux qui lui
                // succede, dans ce cas on met un maximum de jours soldes
                jours = (int) APModuleRepartitionPaiements.CALENDAR.daysBetween(assurance.getDateDebut(),
                        tauxCourant.getDateFin() + 1); // on
                // inclut
                // la
                // date
                // de
                // fin
                jours = Math.min(jours, nbJoursSoldesRestants); // on ne prend
                // pas plus que
                // le nombre de
                // jours
                // restants

                // la date de fin n'est plus tard que la date de fin de la
                // prestation
                if (APModuleRepartitionPaiements.CALENDAR.compare(dateFinAssurance, prestation.getDateFin()) == JACalendar.COMPARE_FIRSTLOWER) {
                    assurance.setDateFin(tauxCourant.getDateFin());
                } else {
                    assurance.setDateFin(prestation.getDateFin().toStr("."));
                }
            }

            if ((jours != 0) && (nbJoursSoldes != 0)) {
                assurance.setMontantBrut(r((jours / nbJoursSoldes) * montantBrut));
            } else {
                assurance.setMontantBrut(r(0));
            }

            assurance
                    .setMontant(r(PRCalcul.pourcentage100(assurance.getMontantBrut(), tauxCourant.getValeurEmployeur())));

            if (!ajouter) {
                assurance.setMontant("-" + assurance.getMontant());
            }

            assurance.add(session.getCurrentThreadTransaction());
            cotisations.add(assurance);

            // si tous les jours soldés sont taxés, quitter la boucle.
            nbJoursSoldesRestants -= jours;

            if (nbJoursSoldesRestants <= 0) {
                // il n'y a plus de jours soldes pour d'autres taux, on
                // abandonne
                break;
            }
        }

        // on convertit la liste de cotisations en array pour le retourner
        return (APCotisation[]) cotisations.toArray(new APCotisation[cotisations.size()]);
    }

    /**
     * renvoie une Map contenant une APCotisation en key et le nombre de jours en String en value.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param cotisationAVS
     *            DOCUMENT ME!
     * @param repa
     *            DOCUMENT ME!
     * @param idAssurance
     *            DOCUMENT ME!
     * @param ajouter
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * @throws APCalculException
     *             DOCUMENT ME!
     */
    private void ajouterLignesCotisationsFraisAdministration(BSession session, APCotisation cotisationAVS,
            APRepartitionPaiements repa, String idAssurance, boolean ajouter) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(idAssurance)) {
            // l'assurance n'est pas definie, on n'ajoute pas de lignes
            return;
        }

        List taux = PRAffiliationHelper.getTauxAssurance(session, idAssurance, "", cotisationAVS.getDateDebut(),
                cotisationAVS.getDateFin());

        if (taux.isEmpty()) {
            throw new APCalculException("les taux pour cette assurance " + idAssurance + " ne sont pas valables");
        }

        int nbJoursSoldes = (int) APModuleRepartitionPaiements.CALENDAR.daysBetween(cotisationAVS.getDateDebut(),
                cotisationAVS.getDateFin());
        nbJoursSoldes++;

        int nbJoursSoldesRestants = nbJoursSoldes;
        double montantBrut = Math.abs(Double.parseDouble(cotisationAVS.getMontant()));

        /*
         * ajouter tous les taux dans l'ordre. s'il y a plus d'un taux, on va essayer de mettre un maximum de jours
         * soldes su les premiers taux. Par exemple, s'il y a 20 jours soldes et deux taux, le second debutant 15 jours
         * apres le debut de la prestation, on va mettre 15 jours soldes sur le premier taux et les 5 restants sur le
         * second taux.
         */
        for (int idTaux = 0; idTaux < taux.size(); ++idTaux) {
            AFTauxAssurance tauxCourant = (AFTauxAssurance) taux.get(idTaux);

            // creer la cotisation
            APCotisation assurance = new APCotisation();

            assurance.setType(APCotisation.TYPE_ASSURANCE);
            assurance.setIdExterne(tauxCourant.getAssuranceId());
            assurance.setSession(session);
            assurance.setIdRepartitionBeneficiairePaiement(repa.getIdRepartitionBeneficiairePaiement());

            if (idTaux > 0) {
                assurance.setDateDebut(tauxCourant.getDateDebut());
            } else {
                assurance.setDateDebut(cotisationAVS.getDateDebut());
            }

            // determiner le nombre de jours soldes pour ce taux
            JADate dateFinCotisation = new JADate(cotisationAVS.getDateFin());
            JADate dateFinAssurance = new JADate(tauxCourant.getDateFin());
            int jours;

            if (JACalendar.isNull(dateFinAssurance)) {
                // le taux n'a pas de date de fin, on met tous les jours soldes
                // sur ce taux
                jours = nbJoursSoldesRestants;
                assurance.setDateFin(cotisationAVS.getDateFin());
            } else {
                // le taux a une date de fin, ce qui signifie qu'il doit y avoir
                // un autre taux qui lui
                // succede, dans ce cas on met un maximum de jours soldes
                jours = (int) APModuleRepartitionPaiements.CALENDAR.daysBetween(assurance.getDateDebut(),
                        tauxCourant.getDateFin() + 1); // on
                // inclut
                // la
                // date
                // de
                // fin
                jours = Math.min(jours, nbJoursSoldesRestants); // on ne prend
                // pas plus que
                // le nombre de
                // jours
                // restants

                // la date de fin n'est plus tard que la date de fin de la
                // prestation
                if (APModuleRepartitionPaiements.CALENDAR.compare(dateFinAssurance, dateFinCotisation) == JACalendar.COMPARE_FIRSTLOWER) {
                    assurance.setDateFin(tauxCourant.getDateFin());
                } else {
                    assurance.setDateFin(cotisationAVS.getDateFin());
                }
            }

            assurance.setMontantBrut(r((jours / nbJoursSoldes) * montantBrut));
            assurance
                    .setMontant(r(PRCalcul.pourcentage100(assurance.getMontantBrut(), tauxCourant.getValeurEmployeur())));

            if (!ajouter) {
                assurance.setMontant("-" + assurance.getMontant());
            }

            assurance.add(session.getCurrentThreadTransaction());

            // si tous les jours soldés sont taxés, quitter la boucle.
            nbJoursSoldesRestants -= jours;

            if (nbJoursSoldesRestants <= 0) {
                // il n'y a plus de jours soldes pour d'autres taux, on
                // abandonne
                break;
            }
        }
    }

    /**
     * Generation cotisation des travailleurs agricole Versement employeur
     * 
     * @param session
     * @param prestation
     * @param repa
     * @param resCalc
     * @param casCotisation
     * @throws Exception
     */
    private void cotisationsCollaborateurAgricole(BSession session, APPrestationCalculee prestation,
            APRepartitionPaiements repa, APResultatCalculSituationProfessionnel resCalc, CasCotisations casCotisation)
            throws Exception {
        /**
         * UNIQUEMENT COLLABORATEUR AGRICOLE
         */
        if (casCotisation.mapCasCotisations.containsKey(CasCotisations.COLLAB_AGR)) {

            // ajouter les lignes pour l'AVS
            ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                    APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                    APModuleRepartitionPaiements.ADDITION);
        }
    }

    /**
     * Generation des cotisations employeur Versement employeur
     * 
     * @param session
     * @param prestation
     * @param repa
     * @param resCalc
     * @param casCotisation
     * @throws Exception
     */
    private void cotisationsEmployeur(BSession session, APPrestationCalculee prestation, APRepartitionPaiements repa,
            APResultatCalculSituationProfessionnel resCalc, CasCotisations casCotisation) throws Exception {

        // //////////////////////////////////////////////////////////////////////////////////////////////////////////
        // SALARIE
        // //////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (casCotisation.mapCasCotisations.containsKey(CasCotisations.SALARIE)) {

            // ajouter les lignes pour l'AVS
            ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                    APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                    APModuleRepartitionPaiements.ADDITION);

            // pas d'AC pour les retraites
            // il faut aller chercher le tiers du droit car la repartition est
            // pour l'employeur
            APDroitLAPG droit = new APDroitLAPG();
            droit.setSession(session);
            droit.setIdDroit(prestation.getIdDroit());
            droit.retrieve();

            PRDemande demande = droit.loadDemande();

            if (!PRTiersHelper.isRentier(session, demande.getIdTiers(), prestation.getDateDebut().toStr("."))) {
                // ajouter les les lignes pour l'AC
                ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AC.getIdAssurance(
                        APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                        APModuleRepartitionPaiements.ADDITION);
            }
        }
    }

    // Versement à l'assuré !!!!
    private void cotisationsEmployeurSansSalaire(BSession session, APPrestationCalculee prestation,
            APRepartitionPaiements repa, CasCotisations casCotisation) throws Exception {

        APCotisation[] cotisationsAVS = null;

        // ajouter les lignes pour l'AVS et récupération des cotisation ajoutées
        // pour générer les frais d'administration
        cotisationsAVS = ajouterLignesCotisations(session, prestation, repa,
                PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                        PRAffiliationHelper.TYPE_PARITAIRE), APModuleRepartitionPaiements.SOUSTRACTION);

        // ajouter les les lignes pour l'AC
        ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AC.getIdAssurance(
                APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                APModuleRepartitionPaiements.SOUSTRACTION);

        // ajouter les les lignes pour les frais d'administration
        for (int i = 0; i < cotisationsAVS.length; i++) {
            ajouterLignesCotisationsFraisAdministration(session, cotisationsAVS[i], repa,
                    PRAffiliationHelper.GENRE_FRAIS_ADM.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                            PRAffiliationHelper.TYPE_PARITAIRE), APModuleRepartitionPaiements.SOUSTRACTION);
        }

        // ajouter l'imposition a la source
        if (prestation.getResultatCalcul().isSoumisImpotSource()) {
            ajouterLigneImpotSource(session, prestation, repa, prestation.getResultatCalcul().getIdTauxImposition(),
                    prestation.getResultatCalcul().getTauxImposition());
        }
    }

    // /**
    // * point d'entrée du processus de génération des cotisations d'assurances.
    // *
    // * @param session DOCUMENT ME!
    // * @param prestation DOCUMENT ME!
    // * @param repa DOCUMENT ME!
    // * @param resCalc DOCUMENT ME!
    // *
    // * @throws Exception DOCUMENT ME!
    // */
    // protected void genererCotisations(BSession session,
    // APPrestationCalculee prestation,
    // APRepartitionPaiements repa,
    // APResultatCalculSituationProfessionnel resCalc,
    // boolean isEtudiant,
    // boolean isIndependantEtVersementAssure) throws Exception {
    // if (resCalc == null) {
    // // une resCalc nulle signifie qu'il n'y a pas d'employeurs et donc tout
    // est verse a l'assure
    // cotisationsAssures(session, prestation, repa, null, isEtudiant,
    // isIndependantEtVersementAssure);
    // } else {
    // if (resCalc.isSoumisCotisation()) {
    // // si le bénéficiaire n'est pas soumis à cotisations, on ne fait rien
    // if (repa.isBeneficiaireEmployeur()) {
    // // le beneficiaire est un employeur
    // if (resCalc.isIndependant()) {
    // // il s'agit d'un independant
    // cotisationsIndependants(session, prestation, repa, resCalc);
    // } else {
    // // il s'agit d'une entreprise
    // cotisationsEmployeur(session, prestation, repa, resCalc);
    // }
    // } else {
    // // le bénéficiaire est un assuré
    // cotisationsAssures(session, prestation, repa, resCalc, isEtudiant,
    // isIndependantEtVersementAssure);
    // }
    // }
    // }
    // }

    /**
     * Generation cotisation des ndépendants, non actifs Versement employeur
     * 
     * @param session
     * @param prestation
     * @param repa
     * @param resCalc
     * @param casCotisation
     * @throws Exception
     */
    private void cotisationsIndependants(BSession session, APPrestationCalculee prestation,
            APRepartitionPaiements repa, APResultatCalculSituationProfessionnel resCalc, CasCotisations casCotisation)
            throws Exception {

        // //////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Indépendant ou non actif uniquement
        // //////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (casCotisation.mapCasCotisations.containsKey(CasCotisations.INDEPENDANT)
                || casCotisation.mapCasCotisations.containsKey(CasCotisations.NON_ACTIF)) {

            // ajouter les lignes pour l'AVS
            APCotisation[] cotisationsAVS = ajouterLignesCotisations(session, prestation, repa,
                    PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                            PRAffiliationHelper.TYPE_PERSONNEL), APModuleRepartitionPaiements.SOUSTRACTION);

            // ajouter les les lignes pour les frais d'administration
            for (int i = 0; i < cotisationsAVS.length; i++) {
                ajouterLignesCotisationsFraisAdministration(session, cotisationsAVS[i], repa,
                        PRAffiliationHelper.GENRE_FRAIS_ADM.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                                PRAffiliationHelper.TYPE_PERSONNEL), APModuleRepartitionPaiements.SOUSTRACTION);
            }

            // //////////////////////////////////////////////////////////////////////////////////////////////////////////
            // INDEPENDANT
            // //////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (casCotisation.mapCasCotisations.containsKey(CasCotisations.INDEPENDANT)) {
                boolean isAjouterCotisationAC = false;

                /*
                 * INDEPENDANT / SALARIE
                 */
                if (casCotisation.mapCasCotisations.containsKey(CasCotisations.SALARIE)) {

                    FWCurrency montantInd = null;
                    FWCurrency montantSal = null;

                    // recuperation du montant independant
                    if (casCotisation.mapCasCotisations.containsKey(CasCotisations.INDEPENDANT)) {
                        montantInd = ((Cotisations) casCotisation.mapCasCotisations.get(CasCotisations.INDEPENDANT)).montant;
                    }
                    if (montantInd == null) {
                        montantInd = new FWCurrency(0);
                    }

                    // recuperation de montant salarie
                    montantSal = ((Cotisations) casCotisation.mapCasCotisations.get(CasCotisations.SALARIE)).montant;
                    boolean isVersementEmployeurForMontantSal = ((Cotisations) casCotisation.mapCasCotisations
                            .get(CasCotisations.SALARIE)).isVersementEmployeur;

                    if (montantSal == null) {
                        montantSal = new FWCurrency(0);
                        isVersementEmployeurForMontantSal = false;
                    }

                    // On rajoute cot. AC si par salarie > par indépendant
                    if (montantSal.compareTo(montantInd) < 0) {
                        isAjouterCotisationAC = false;
                    } else {
                        if (!isVersementEmployeurForMontantSal) {
                            isAjouterCotisationAC = true;
                        }
                    }
                }
                /*
                 * INDEPENDANT / TRAVAILLEUR AGRICOLE
                 */
                boolean isAjouterLFA = false;

                FWCurrency montantTrAgr = null;
                FWCurrency montantInd = null;

                // recuperation du montant travailleur agricole
                if (casCotisation.mapCasCotisations.containsKey(CasCotisations.TRAVAILLEUR_AGR)) {
                    montantTrAgr = ((Cotisations) casCotisation.mapCasCotisations.get(CasCotisations.TRAVAILLEUR_AGR)).montant;
                }
                if (montantTrAgr == null) {
                    montantTrAgr = new FWCurrency(0);
                }

                // recuperation du montant independant
                if (casCotisation.mapCasCotisations.containsKey(CasCotisations.INDEPENDANT)) {
                    montantInd = ((Cotisations) casCotisation.mapCasCotisations.get(CasCotisations.INDEPENDANT)).montant;
                }
                if (montantInd == null) {
                    montantInd = new FWCurrency(0);
                }

                // On rajoute cot. AC si par travailleur agricole > par
                // indépendant
                if (casCotisation.mapCasCotisations.containsKey(CasCotisations.TRAVAILLEUR_AGR)) {
                    if (montantTrAgr.compareTo(montantInd) < 0) {
                        isAjouterCotisationAC = false;
                    } else {
                        isAjouterCotisationAC = true;
                    }
                }

                if (isAjouterCotisationAC) {
                    // ajouter les les lignes pour l'AC
                    ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AC.getIdAssurance(
                            APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PERSONNEL),
                            APModuleRepartitionPaiements.SOUSTRACTION);
                }

                // ajouter l'imposition a la source
                if (prestation.getResultatCalcul().isSoumisImpotSource()) {
                    ajouterLigneImpotSource(session, prestation, repa, prestation.getResultatCalcul().getIdTauxImposition(),
                            prestation.getResultatCalcul().getTauxImposition());
                }

                // InfoRom557 pas de cotisations LFA pour les travailleurs agricoles si versement à l'assuré
                // if (isAjouterLFA) {
                // // il s'agit d'une exploitation agricole, on ajoute la LFA
                // this.ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_LFA
                // .getIdAssurance(APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PERSONNEL),
                // APModuleRepartitionPaiements.SOUSTRACTION);
                // }
            }
        }
    }

    /**
     * Generation des cotisations pour les cas sans employeurs
     * 
     * @param session
     * @param prestation
     * @param repa
     * @param casCotisation
     * @throws Exception
     */
    private void cotisationsSansEmployeur(BSession session, APPrestationCalculee prestation,
            APRepartitionPaiements repa, CasCotisations casCotisation) throws Exception {

        APCotisation[] cotisationsAVS = null;

        // Pas de cotisations
        if (casCotisation.mapCasCotisations.containsKey(CasCotisations.SANS_EMPLOYEUR_AV18)) {
            ;
        } else if (casCotisation.mapCasCotisations.containsKey(CasCotisations.SANS_EMPLOYEUR_AV21)) {
            // ajouter les lignes pour l'AVS et récupération des cotisation
            // ajoutées pour générer les frais d'administration
            cotisationsAVS = ajouterLignesCotisations(session, prestation, repa,
                    PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                            PRAffiliationHelper.TYPE_PARITAIRE), APModuleRepartitionPaiements.SOUSTRACTION);

        } else if (casCotisation.mapCasCotisations.containsKey(CasCotisations.SANS_EMPLOYEUR_AP20)) {

            // pas de cotisation AVS pour les rentier sans employeur
            if (!PRTiersHelper.isRentier(session, repa.getIdTiers(), prestation.getDateDebut().toStr("."))) {
                // ajouter les lignes pour l'AVS et récupération des cotisation
                // ajoutées pour générer les frais d'administration
                cotisationsAVS = ajouterLignesCotisations(session, prestation, repa,
                        PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                                PRAffiliationHelper.TYPE_PARITAIRE), APModuleRepartitionPaiements.SOUSTRACTION);
            }

            // Pas de cotisations AC si en age AVS
            // Ou si pas de situation prof.
            // ==========================================================================
            // Selon CCJU, pas de cotisation AC pour les cas sans sit. prof.
            // après 18 ans.
            // ==========================================================================
            APSituationProfessionnelleManager sitProManager = new APSituationProfessionnelleManager();
            sitProManager.setSession(session);
            sitProManager.setForIdDroit(prestation.getIdDroit());
            int nbSitPro = sitProManager.getCount();

            if (!PRTiersHelper.isRentier(session, repa.getIdTiers(), prestation.getDateDebut().toStr("."))
                    && (nbSitPro > 0)) {

                // ajouter les les lignes pour l'AC
                ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AC.getIdAssurance(
                        APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                        APModuleRepartitionPaiements.SOUSTRACTION);
            }
        }

        if (cotisationsAVS != null) {
            // ajouter les les lignes pour les frais d'administration
            for (int i = 0; i < cotisationsAVS.length; i++) {
                ajouterLignesCotisationsFraisAdministration(session, cotisationsAVS[i], repa,
                        PRAffiliationHelper.GENRE_FRAIS_ADM.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                                PRAffiliationHelper.TYPE_PARITAIRE), APModuleRepartitionPaiements.SOUSTRACTION);
            }
        }

        // ajouter l'imposition a la source
        if (prestation.getResultatCalcul().isSoumisImpotSource()) {
            ajouterLigneImpotSource(session, prestation, repa, prestation.getResultatCalcul().getIdTauxImposition(),
                    prestation.getResultatCalcul().getTauxImposition());
        }
    }

    /**
     * Generation cotisations des travailleurs agricole Versement employeur
     * 
     * @param session
     * @param prestation
     * @param repa
     * @param resCalc
     * @param casCotisation
     * @throws Exception
     */
    private void cotisationsTravailleurAgricole(BSession session, APPrestationCalculee prestation,
            APRepartitionPaiements repa, APResultatCalculSituationProfessionnel resCalc, CasCotisations casCotisation)
            throws Exception {

        /*
         * UNIQUEMENT TRAVAILLEUR AGRICOLE
         */
        if (casCotisation.mapCasCotisations.containsKey(CasCotisations.TRAVAILLEUR_AGR)) {

            Cotisations cotSalarie = (Cotisations) casCotisation.mapCasCotisations.get(CasCotisations.SALARIE);

            // ajouter les lignes pour l'AVS
            ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                    APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                    APModuleRepartitionPaiements.ADDITION);

            // ajouter les les lignes pour l'AC
            ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AC.getIdAssurance(
                    APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                    APModuleRepartitionPaiements.ADDITION);

            // il s'agit d'une exploitation agricole, on ajoute la LFA
            // si c'est la repartition correspondant a la partie salariale, pas
            // de cotisations LFA
            if (cotSalarie == null) {

                ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_LFA.getIdAssurance(
                        APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                        APModuleRepartitionPaiements.ADDITION);
            } else {

                BigDecimal montantCotSalaire = cotSalarie.montant.getBigDecimalValue();
                montantCotSalaire = montantCotSalaire.multiply(new BigDecimal(prestation.getNombreJoursSoldes()));

                if (montantCotSalaire.compareTo(new BigDecimal(repa.getMontantBrut())) != 0) {
                    ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_LFA.getIdAssurance(
                            APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                            APModuleRepartitionPaiements.ADDITION);
                }
            }
        }
    }

    // // génére les cotisations d'assurances pour un affilie, les montants
    // seront rajoutes
    // private void cotisationsEmployeur(BSession session,
    // APPrestationCalculee prestation,
    // APRepartitionPaiements repa,
    // APResultatCalculSituationProfessionnel resCalc) throws Exception {
    //
    // // ajouter les lignes pour l'AVS
    // ajouterLignesCotisations(session, prestation, repa,
    // PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,PRAffiliationHelper.TYPE_PARITAIRE),
    // ADDITION);
    //
    // // ajouter les les lignes pour l'AC
    // ajouterLignesCotisations(session, prestation, repa,
    // PRAffiliationHelper.GENRE_AC.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,PRAffiliationHelper.TYPE_PARITAIRE),
    // ADDITION);
    //
    // if (isBrancheEconomiqueAgricole(session, repa.getIdTiers(),
    // repa.getIdAffilie())) {
    // // la branche économique de cet affilié est l'agriculture.
    // if (!resCalc.isCollaborateurAgricole()) {
    // // il s'agit d'une exploitation agricole, on ajoute la LFA
    // ajouterLignesCotisations(session, prestation, repa,
    // PRAffiliationHelper.GENRE_LFA.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,PRAffiliationHelper.TYPE_PARITAIRE),
    // ADDITION);
    // }
    // }
    //
    // }

    // private void cotisationsAssures(BSession session,
    // APPrestationCalculee prestation,
    // APRepartitionPaiements repa,
    // APResultatCalculSituationProfessionnel resCalc,
    // boolean isEtudiant,
    // boolean isIndependant) throws Exception {
    //
    //
    //
    // // ajouter les lignes pour l'AVS et récupération des cotisation ajoutées
    // pour générer les frais d'administration
    // APCotisation[] cotisationsAVS = ajouterLignesCotisations(session,
    // prestation, repa,
    // PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,PRAffiliationHelper.TYPE_PARITAIRE),
    // SOUSTRACTION);
    //
    //
    // //Si étudiant ou collaborateur agricole ou rentier, on ne calcul pas les
    // cotisations AC
    // if (!isEtudiant && !isIndependant && !PRTiersHelper.isRentier(session,
    // repa.getIdTiers(), JACalendar.todayJJsMMsAAAA()) && (resCalc==null ||
    // !resCalc.isCollaborateurAgricole())) {
    // // ajouter les les lignes pour l'AC
    // ajouterLignesCotisations(session, prestation, repa,
    // PRAffiliationHelper.GENRE_AC.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,PRAffiliationHelper.TYPE_PARITAIRE),
    // SOUSTRACTION);
    //
    // }
    //
    // // ajouter les les lignes pour les frais d'administration
    // for (int i = 0; i < cotisationsAVS.length; i++) {
    // ajouterLignesCotisationsFraisAdministration(session, cotisationsAVS[i],
    // repa,
    // PRAffiliationHelper.GENRE_FRAIS_ADM.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,PRAffiliationHelper.TYPE_PARITAIRE),
    // SOUSTRACTION);
    // }
    //
    //
    // APDroitAPG droit = new APDroitAPG();
    // droit.setIdDroit(prestation.getIdDroit());
    // droit.setSession(session);
    // droit.retrieve();
    //
    //
    // // ajouter l'imposition a la source
    // if (prestation.getResultatCalcul().isSoumisImpotSource()) {
    // ajouterLigneImpotSource(session, prestation, repa,
    // prestation.getResultatCalcul().getIdTauxImposition(),
    // prestation.getResultatCalcul().getTauxImposition());
    // }
    // }

    // private void cotisationsIndependants(BSession session,
    // APPrestationCalculee prestation,
    // APRepartitionPaiements repa,
    // APResultatCalculSituationProfessionnel resCalc) throws Exception {
    // // ajouter les lignes pour l'AVS
    // ajouterLignesCotisations(session, prestation, repa,
    // PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,PRAffiliationHelper.TYPE_PERSONNEL),
    // SOUSTRACTION);
    //
    // // ajouter les lignes pour l'assurance chomage si l'assuré est
    // indépendant et salarié et que son activité
    // // salariée lui rapporte plus que son activité indépendante.
    // APResultatCalcul rc = prestation.getResultatCalcul();
    //
    // if (rc.isIndependantEtSalarie() && rc.isVersementEmployeUniquement() &&
    // (rc.getSalaireJournalierTotal().compareTo(rc.getRevenuJournalierIndependantTotal())
    // > 0)) {
    // ajouterLignesCotisations(session, prestation, repa,
    // PRAffiliationHelper.GENRE_AC.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,PRAffiliationHelper.TYPE_PERSONNEL),
    // SOUSTRACTION);
    // }
    // }

    /**
     * Donne les taux valable pour un canton durant une periode
     * 
     * @param session
     * @param dateDebut
     * @param dateFin
     * @param idCanton
     * @return
     * @throws Exception
     */
    private List findTauxImposition(BSession session, String dateDebut, String dateFin, String idCanton)
            throws Exception {

        PRTauxImpositionManager mgrTauxImpot = new PRTauxImpositionManager();
        mgrTauxImpot.setSession(session);
        mgrTauxImpot.setForTypeImpot(IPRTauxImposition.CS_TARIF_D);
        mgrTauxImpot.setOrderBy(PRTauxImposition.FIELDNAME_DATEDEBUT);
        mgrTauxImpot.setForPeriode(dateDebut, dateFin);
        mgrTauxImpot.setForCsCanton(idCanton);
        mgrTauxImpot.find();

        return mgrTauxImpot.getContainer();
    }

    protected void genererCotisations(BSession session, APPrestationCalculee prestation, APRepartitionPaiements repa,
            APResultatCalculSituationProfessionnel resCalc, CasCotisations casCotisation) throws Exception {

        if (resCalc.isSoumisCotisation()) {
            // si le bénéficiaire n'est pas soumis à cotisations, on ne fait
            // rien
            if (resCalc.isIndependant()) {
                // il s'agit d'un independant
                cotisationsIndependants(session, prestation, repa, resCalc, casCotisation);
            } else if (resCalc.isCollaborateurAgricole()) {
                cotisationsCollaborateurAgricole(session, prestation, repa, resCalc, casCotisation);
            }
            // Travailleur agricole
            else if (casCotisation.mapCasCotisations.containsKey(CasCotisations.TRAVAILLEUR_AGR)) {
                cotisationsTravailleurAgricole(session, prestation, repa, resCalc, casCotisation);
            } else if (JadeStringUtil.isIntegerEmpty(repa.getMontantBrut())
                    && casCotisation.mapCasCotisations.containsKey(CasCotisations.EMPLOYEUR_SANS_SALAIRE)) {
                cotisationsEmployeurSansSalaire(session, prestation, repa, casCotisation);
            }
            // Versement employeur
            else {
                cotisationsEmployeur(session, prestation, repa, resCalc, casCotisation);
            }
        }
    }

    /**
     * Les cotisations AVS & AC sont toujours calculée pour les montants ACM.
     * 
     * @param session
     * @param prestation
     * @param repa
     * 
     * @throws Exception
     */
    public void genererCotisationsACM(BSession session, APPrestationCalculee prestation, APRepartitionPaiements repa)
            throws Exception {
        if (repa.isBeneficiaireEmployeur()) {
            // le beneficiaire est un employeur
            ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                    APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                    APModuleRepartitionPaiements.ADDITION);

            // le beneficiaire est un employeur
            ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AC.getIdAssurance(
                    APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                    APModuleRepartitionPaiements.ADDITION);
        } else {
            // le bénéficiaire est un assuré
            ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                    APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                    APModuleRepartitionPaiements.SOUSTRACTION);

            // le beneficiaire est un employeur
            ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AC.getIdAssurance(
                    APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                    APModuleRepartitionPaiements.SOUSTRACTION);
        }
    }

    /**
     * Les cotisations AVS & AC sont toujours calculée pour les montants ACM.
     *
     * @param session
     * @param prestation
     * @param repa
     *
     * @throws Exception
     */
    public void genererCotisationsMATCIAB2(BSession session, APPrestationCalculee prestation, APRepartitionPaiements repa, boolean isIndependant)
            throws Exception {
        if (!isIndependant) {
            // le beneficiaire est un employeur
            ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                    APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                    APModuleRepartitionPaiements.ADDITION);

            // le beneficiaire est un employeur
            ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AC.getIdAssurance(
                    APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                    APModuleRepartitionPaiements.ADDITION);
        } else {
            // le bénéficiaire est un assuré
            ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                    APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PERSONNEL),
                    APModuleRepartitionPaiements.SOUSTRACTION);

            // le beneficiaire est un assuré
            ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AC.getIdAssurance(
                    APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PERSONNEL),
                    APModuleRepartitionPaiements.SOUSTRACTION);
        }
    }

    /**
     * Generation des cotisations lors d'un paiement a l'assure
     * 
     * @param session
     * @param prestation
     * @param repa
     * @param casCotisation
     * @throws Exception
     */
    private void genererCotisationsAssure(BSession session, APPrestationCalculee prestation,
            APRepartitionPaiements repa, CasCotisations casCotisation) throws Exception {

        // le cas des sans employeurs
        if (casCotisation.mapCasCotisations.containsKey(CasCotisations.SANS_EMPLOYEUR_AV18)
                || casCotisation.mapCasCotisations.containsKey(CasCotisations.SANS_EMPLOYEUR_AV21)
                || casCotisation.mapCasCotisations.containsKey(CasCotisations.SANS_EMPLOYEUR_AP20)) {

            cotisationsSansEmployeur(session, prestation, repa, casCotisation);
            return;
        }

        APCotisation[] cotisationsAVS = null;
        boolean isAjouterCotisationAC = false;

        // le cas des retraites
        if (PRTiersHelper.isRentier(session, repa.getIdTiers(), prestation.getDateDebut().toStr("."))) {
            // FWCurrency montantBrut = new FWCurrency(repa.getMontantBrut());

            // pas de cotisation pour les montants infereurs a 1400.-
            // if(MONTANT_MIN_POUR_COTISER_APRES_RETRAITE.compareTo(montantBrut)<
            // 0){

            // la cotisation se paie sur la diff entre le montant minimal pour
            // paier des cotisations et le montant brut
            // montantBrut.sub(MONTANT_MIN_POUR_COTISER_APRES_RETRAITE);
            // repa.setMontantBrut(montantBrut.toString());

            ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                    APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                    APModuleRepartitionPaiements.SOUSTRACTION);
            // }

        } else {

            // ajouter les lignes pour l'AVS
            cotisationsAVS = ajouterLignesCotisations(session, prestation, repa,
                    PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                            PRAffiliationHelper.TYPE_PARITAIRE), APModuleRepartitionPaiements.SOUSTRACTION);

            // Identification des cas ou il faut rajouter les cotisations AC à
            // l'assuré :
            isAjouterCotisationAC = true;

            // //////////////////////////////////////////////////////////////////////////////////////////////////////////
            // COLLABORATEUR AGRICOLE
            // //////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (casCotisation.mapCasCotisations.containsKey(CasCotisations.COLLAB_AGR)) {
                Cotisations cotCollAgr = (Cotisations) casCotisation.mapCasCotisations.get(CasCotisations.COLLAB_AGR);
                Cotisations cotSalarie = (Cotisations) casCotisation.mapCasCotisations.get(CasCotisations.SALARIE);

                /*
                 * UNIQUEMENT COLLABORATEUR AGRICOLE
                 */
                if (cotSalarie == null) {
                    isAjouterCotisationAC = false;
                }
                /*
                 * COLLABORATEUR AGRICOLE / SALARIE
                 */
                else {

                    FWCurrency montantCollAgr = cotCollAgr.montant;
                    FWCurrency montantSalarie = cotSalarie.montant;

                    if (montantCollAgr == null) {
                        montantCollAgr = new FWCurrency("0.0");
                    }
                    if (montantSalarie == null) {
                        montantSalarie = new FWCurrency("0.0");
                    }

                    // si le montant salarie est plus grand que le montant
                    // collaborateur agricole
                    if (montantSalarie.compareTo(montantCollAgr) > 0) {

                        // La part du collaborateur agricole est versée à
                        // l'assuré
                        // et que la part du salarie est versee a l'employeur
                        // -> pas de cotisations AC
                        if (!cotCollAgr.isVersementEmployeur) {
                            if (cotSalarie.isVersementEmployeur) {
                                isAjouterCotisationAC = false;
                            }
                        }
                    } else {

                        // les deux parts sont versee a l'assure
                        // -> pas de cotisations AC
                        if (!cotCollAgr.isVersementEmployeur) {
                            if (!cotSalarie.isVersementEmployeur) {
                                isAjouterCotisationAC = false;
                            }
                        }

                    }

                }
            }
            // //////////////////////////////////////////////////////////////////////////////////////////////////////////
            // TRAVAILLEUR AGRICOLE
            // //////////////////////////////////////////////////////////////////////////////////////////////////////////
            else if (casCotisation.mapCasCotisations.containsKey(CasCotisations.TRAVAILLEUR_AGR)) {

                Cotisations cotTrAgr = (Cotisations) casCotisation.mapCasCotisations
                        .get(CasCotisations.TRAVAILLEUR_AGR);
                Cotisations cotSalarie = (Cotisations) casCotisation.mapCasCotisations.get(CasCotisations.SALARIE);
                Cotisations cotInd = (Cotisations) casCotisation.mapCasCotisations.get(CasCotisations.INDEPENDANT);

                boolean isAjouterCotisationLFA = false;

                /*
                 * UNIQUEMENT TRAVAILLEUR AGRICOLE
                 */
                if ((cotSalarie == null) && (cotInd == null)) {
                    isAjouterCotisationAC = true;
                    // isAjouterCotisationLFA = true;
                }
                /*
                 * TRAVAILLEUR AGRICOLE / SALARIE
                 */
                else if (cotSalarie != null) {

                    // On ajoute toujours les cotisations
                    isAjouterCotisationAC = true;
                }
                /*
                 * TRAVAILLEUR AGRICOLE / INDEPENDANT
                 */
                else if (cotInd != null) {

                    // La part du travailleur agricole est versée à l'assuré
                    if (!cotTrAgr.isVersementEmployeur) {

                        // Si la part du travailleur agricole supérieur au
                        // montant de l'indépendant,
                        // on ajoute les cotisations LFA
                        FWCurrency montantTr = cotTrAgr.montant;
                        FWCurrency montantInd = cotInd.montant;
                        if (montantInd == null) {
                            montantInd = new FWCurrency(0);
                        }

                        if (montantInd.compareTo(montantTr) < 0) {
                            isAjouterCotisationAC = true;
                        } else {
                            isAjouterCotisationAC = false;
                        }
                    }
                }

                if (isAjouterCotisationLFA) {
                    ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_LFA.getIdAssurance(
                            APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                            APModuleRepartitionPaiements.SOUSTRACTION);
                }
            }
            // //////////////////////////////////////////////////////////////////////////////////////////////////////////
            // INDEPENDANT
            // //////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (casCotisation.mapCasCotisations.containsKey(CasCotisations.INDEPENDANT)) {

                Cotisations cotSalarie = (Cotisations) casCotisation.mapCasCotisations.get(CasCotisations.SALARIE);

                /*
                 * INDEPENDANT / SALARIE
                 */
                if (cotSalarie != null) {

                    // On rajoute cot. AC uniquement si par salarial > par
                    // indépendant
                    FWCurrency montantInd = ((Cotisations) casCotisation.mapCasCotisations
                            .get(CasCotisations.INDEPENDANT)).montant;
                    FWCurrency montantSalarie = ((Cotisations) casCotisation.mapCasCotisations
                            .get(CasCotisations.SALARIE)).montant;
                    if (montantSalarie == null) {
                        montantSalarie = new FWCurrency(0);
                    }

                    if (montantSalarie.compareTo(montantInd) < 0) {
                        isAjouterCotisationAC = false;
                    } else {
                        isAjouterCotisationAC = true;
                    }
                }

                /*
                 * UNIQUEMENT INDEPENDANT
                 */
                // Ne peut pas arriver, car on ne verse jamais à l'assuré si
                // indépendant only.
            }
        }

        if (isAjouterCotisationAC) {

            // ajouter les les lignes pour l'AC
            ajouterLignesCotisations(session, prestation, repa, PRAffiliationHelper.GENRE_AC.getIdAssurance(
                    APApplication.DEFAULT_APPLICATION_APG, PRAffiliationHelper.TYPE_PARITAIRE),
                    APModuleRepartitionPaiements.SOUSTRACTION);
        }

        // ajouter les lignes pour les frais d'administration
        if (cotisationsAVS != null) {
            for (int i = 0; i < cotisationsAVS.length; i++) {
                ajouterLignesCotisationsFraisAdministration(session, cotisationsAVS[i], repa,
                        PRAffiliationHelper.GENRE_FRAIS_ADM.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                                PRAffiliationHelper.TYPE_PARITAIRE), APModuleRepartitionPaiements.SOUSTRACTION);
            }
        }

        // ajouter l'imposition a la source
        if (prestation.getResultatCalcul().isSoumisImpotSource()) {
            ajouterLigneImpotSource(session, prestation, repa, prestation.getResultatCalcul().getIdTauxImposition(),
                    prestation.getResultatCalcul().getTauxImposition());
        }

    }

    /**
     * Pas vraiment de cotisations pour la LaMat, mais des impots a la source
     * 
     * @param session
     * @param prestation
     * @param repa
     * 
     * @throws Exception
     */
    private void genererCotisationsLaMat(BSession session, APPrestation prestation, APRepartitionPaiements repa,
            APDroitLAPG droit) throws Exception {

        // si impots a la source
        if (droit.getIsSoumisImpotSource().booleanValue()) {

            // on va rechercher tous les taux pour ce canton et pour la période
            // de la prestation
            String cantonImposition = PRTiersHelper.getCanton(session, droit.getNpa());
            List tauxImpots = findTauxImposition(session, prestation.getDateDebut(), prestation.getDateFin(),
                    cantonImposition);

            // remarque: s'il n'y a pas de taux d'impositions definis pour ce
            // canton et qu'on n'en a pas saisi à la main,
            // aucune cotisation n'est creee...
            if (((tauxImpots == null) || tauxImpots.isEmpty())
                    && JadeStringUtil.isDecimalEmpty(droit.getTauxImpotSource())) {
                return;
            }

            PRTauxImposition taux = null;
            if (JadeStringUtil.isDecimalEmpty(droit.getTauxImpotSource())) {

                // Si l'utilisateur n'a pas redefini de taux d'imposition on
                // prend le taux au debut de la periode
                taux = (PRTauxImposition) tauxImpots.get(0);

            } else {
                taux = new PRTauxImposition();
                taux.setTaux(droit.getTauxImpotSource());
            }

            // creer la cotisation
            APCotisation impot = new APCotisation();

            impot.setIdExterne(taux.getIdTauxImposition());
            impot.setTaux(taux.getTaux());
            impot.setSession(session);
            impot.setDateDebut(prestation.getDateDebut());
            impot.setDateFin(prestation.getDateFin());
            impot.setIdRepartitionBeneficiairePaiement(repa.getIdRepartitionBeneficiairePaiement());
            impot.setType(APCotisation.TYPE_IMPOT);

            // calculer le montant
            impot.setMontantBrut(repa.getMontantBrut());
            impot.setMontant("-"
                    + JANumberFormatter.round(
                            String.valueOf(PRCalcul.pourcentage100(repa.getMontantBrut(), taux.getTaux())), 0.05, 2,
                            JANumberFormatter.NEAR));

            impot.setSession(session);
            impot.add();
        }
    }

    private FWCurrency getMontantTotalCotisation(BSession session, BTransaction transaction, APRepartitionPaiements repa)
            throws Exception {
        FWCurrency result = new FWCurrency(0);
        APCotisationManager mgr = new APCotisationManager();

        mgr.setSession(session);
        mgr.setForIdRepartitionBeneficiairePaiement(repa.getIdRepartitionBeneficiairePaiement());
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        for (Iterator iter = mgr.iterator(); iter.hasNext();) {
            APCotisation element = (APCotisation) iter.next();

            result.add(element.getMontant());
        }

        return new FWCurrency(result.toString());
    }

    protected CasCotisations identifierCasCotisations(BSession session, BTransaction transaction,
            PRTiersWrapper assure, List listBeneficiairePmtPotentiel, APPrestationCalculee prestationCalculee)
            throws Exception {

        CasCotisations casCotisation = new CasCotisations();
        // Aucun employeur, tout est versé à l'assuré
        if ((listBeneficiairePmtPotentiel == null)
                || (listBeneficiairePmtPotentiel.size() == 0)
                || ((listBeneficiairePmtPotentiel.size() == 1) && ((APResultatCalculSituationProfessionnel) listBeneficiairePmtPotentiel
                        .get(0)).isTravailleurSansEmployeur())) {

            // 3 cas possibles :
            // sans employeur, assuré a moins de 18ans -> avant années des 18
            // ans
            // sans employeur, assuré a moins de 21ans -> avant années des 21
            // ans
            // sans employeur, assuré après 20 ans -> l'années des 21 ans ou
            // après

            JADate dateDebut = prestationCalculee.getDateDebut();
            JADate dateRef = new JADate(1, 1, dateDebut.getYear() + 1);

            String dateNaissance = assure.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

            JACalendarGregorian jaCal = new JACalendarGregorian();
            JADate dateMoins18 = jaCal.addYears(dateRef, -18);
            JADate dateMoins21 = jaCal.addYears(dateRef, -21);

            if (BSessionUtil.compareDateFirstGreater(session, dateNaissance, dateMoins18.toStr("."))) {

                Cotisations cot = new Cotisations();
                cot.montant = new FWCurrency(0);
                cot.isVersementEmployeur = false;
                casCotisation.mapCasCotisations.put(CasCotisations.SANS_EMPLOYEUR_AV18, cot);

            } else if (BSessionUtil.compareDateFirstGreater(session, dateNaissance, dateMoins21.toStr("."))) {

                Cotisations cot = new Cotisations();
                cot.montant = new FWCurrency(0);
                cot.isVersementEmployeur = false;

                casCotisation.mapCasCotisations.put(CasCotisations.SANS_EMPLOYEUR_AV21, cot);
            } else {

                Cotisations cot = new Cotisations();
                cot.montant = new FWCurrency(0);
                cot.isVersementEmployeur = false;

                casCotisation.mapCasCotisations.put(CasCotisations.SANS_EMPLOYEUR_AP20, cot);
            }
            return casCotisation;
        }

        FWCurrency montantTotalSalaire = new FWCurrency();
        boolean hasOnlyVersementAssure = true;

        for (Iterator iter = listBeneficiairePmtPotentiel.iterator(); iter.hasNext();) {

            APResultatCalculSituationProfessionnel benefPotentiel = (APResultatCalculSituationProfessionnel) iter
                    .next();
            montantTotalSalaire.add(benefPotentiel.getSalaireJournalierNonArrondi());

            if (benefPotentiel.isVersementEmployeur()) {
                hasOnlyVersementAssure = false;
            }

            // Indépendant
            if (benefPotentiel.isIndependant()) {
                IPRAffilie aff = PRAffiliationHelper.getEmployeurParIdAffilie(session, transaction,
                        benefPotentiel.getIdAffilie(), benefPotentiel.getIdTiers());
                if ((aff != null) && IAFAffiliation.TYPE_AFFILI_NON_ACTIF.equals(aff.getTypeAffiliation())) {
                    if (casCotisation.mapCasCotisations.containsKey(CasCotisations.NON_ACTIF)) {
                        Cotisations cot = (Cotisations) casCotisation.mapCasCotisations.get(CasCotisations.NON_ACTIF);
                        cot.montant.add(benefPotentiel.getSalaireJournalierNonArrondi());

                    } else {
                        Cotisations cot = new Cotisations();
                        cot.montant = new FWCurrency(benefPotentiel.getSalaireJournalierNonArrondi().toString());
                        cot.isVersementEmployeur = benefPotentiel.isVersementEmployeur();
                        casCotisation.mapCasCotisations.put(CasCotisations.NON_ACTIF, cot);
                    }

                } else {
                    if (casCotisation.mapCasCotisations.containsKey(CasCotisations.INDEPENDANT)) {
                        Cotisations cot = (Cotisations) casCotisation.mapCasCotisations.get(CasCotisations.INDEPENDANT);
                        cot.montant.add(benefPotentiel.getSalaireJournalierNonArrondi());
                    } else {
                        Cotisations cot = new Cotisations();
                        cot.montant = new FWCurrency(benefPotentiel.getSalaireJournalierNonArrondi().toString());
                        cot.isVersementEmployeur = benefPotentiel.isVersementEmployeur();

                        casCotisation.mapCasCotisations.put(CasCotisations.INDEPENDANT, cot);
                    }

                }
            }

            // Travailleur agricole
            else if ((isBrancheEconomiqueAgricole(session, benefPotentiel.getIdTiers(), benefPotentiel.getIdAffilie()) || benefPotentiel
                    .isTravailleurAgricole()) && !benefPotentiel.isCollaborateurAgricole()) {

                if (casCotisation.mapCasCotisations.containsKey(CasCotisations.TRAVAILLEUR_AGR)) {

                    Cotisations cot = (Cotisations) casCotisation.mapCasCotisations.get(CasCotisations.TRAVAILLEUR_AGR);
                    cot.montant.add(benefPotentiel.getSalaireJournalierNonArrondi());

                } else {
                    Cotisations cot = new Cotisations();
                    cot.montant = new FWCurrency(benefPotentiel.getSalaireJournalierNonArrondi().toString());
                    cot.isVersementEmployeur = benefPotentiel.isVersementEmployeur();

                    casCotisation.mapCasCotisations.put(CasCotisations.TRAVAILLEUR_AGR, cot);
                }
            }

            // Collaborateur agricole
            else if (benefPotentiel.isCollaborateurAgricole()) {
                if (casCotisation.mapCasCotisations.containsKey(CasCotisations.COLLAB_AGR)) {

                    Cotisations cot = (Cotisations) casCotisation.mapCasCotisations.get(CasCotisations.COLLAB_AGR);
                    cot.montant.add(benefPotentiel.getSalaireJournalierNonArrondi());
                } else {
                    Cotisations cot = new Cotisations();
                    cot.montant = new FWCurrency(benefPotentiel.getSalaireJournalierNonArrondi().toString());
                    cot.isVersementEmployeur = benefPotentiel.isVersementEmployeur();

                    casCotisation.mapCasCotisations.put(CasCotisations.COLLAB_AGR, cot);
                }
            }
            // Salarie
            else {
                if (casCotisation.mapCasCotisations.containsKey(CasCotisations.SALARIE)) {
                    Cotisations cot = (Cotisations) casCotisation.mapCasCotisations.get(CasCotisations.SALARIE);
                    cot.montant.add(benefPotentiel.getSalaireJournalierNonArrondi());
                } else {
                    Cotisations cot = new Cotisations();
                    cot.montant = new FWCurrency(benefPotentiel.getSalaireJournalierNonArrondi().toString());
                    cot.isVersementEmployeur = benefPotentiel.isVersementEmployeur();

                    casCotisation.mapCasCotisations.put(CasCotisations.SALARIE, cot);
                }
            }
        }

        if (montantTotalSalaire.isZero() && !hasOnlyVersementAssure) {

            Cotisations cot = new Cotisations();
            cot.montant = new FWCurrency(0);
            cot.isVersementEmployeur = false;

            casCotisation.mapCasCotisations.put(CasCotisations.EMPLOYEUR_SANS_SALAIRE, cot);

            // On supprime le type salarié si exstant, car est remplacé par
            // employeur sans salaire.
            if (casCotisation.mapCasCotisations.containsKey(CasCotisations.SALARIE)) {
                casCotisation.mapCasCotisations.remove(CasCotisations.SALARIE);
            }
        }

        return casCotisation;
    }

    /**
     * retourne vrai si le tiers indique est affilie est qu'il travaille dans la branche economique agricole.
     * 
     * <p>
     * Note: si le tiers n'est pas affilie dans cette caisse, on retourne de toutes facons faux, les cotisations LFA
     * devront etre ajoutees a la main...
     * </p>
     * 
     * @param session
     * @param idTiers
     * @param idAffilie
     * 
     * @return
     * 
     * @throws Exception
     */
    private boolean isBrancheEconomiqueAgricole(BSession session, String idTiers, String idAffilie) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(idAffilie)) {
            return false;
        } else {
            IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(session,
                    session.getCurrentThreadTransaction(), idAffilie, idTiers);

            return IPRConstantesExternes.BE_AGRICOLE.equals(affilie.getBrancheEconomique());
        }
    }

    private void montantCotisationDesRestitutions(BSession session, BTransaction transaction,
            String idRepartitionBenefPmtDeLaRestitution, APRepartitionPaiements repartitionBeneficiairesARestituer)
            throws Exception {
        // Récupération de toute les cotisations pour le bénéficiaire donnée.
        APCotisationManager mgr = new APCotisationManager();

        mgr.setSession(session);
        mgr.setForIdRepartitionBeneficiairePaiement(repartitionBeneficiairesARestituer
                .getIdRepartitionBeneficiairePaiement());
        mgr.find(transaction);

        if (mgr.iterator() != null) {
            for (Iterator iter = mgr.iterator(); iter.hasNext();) {
                APCotisation assuranceARestituer = (APCotisation) iter.next();
                APCotisation assuranceDeRestitution = new APCotisation();

                assuranceDeRestitution.setSession(session);
                assuranceDeRestitution.setType(assuranceARestituer.getType());
                assuranceDeRestitution.setDateDebut(assuranceARestituer.getDateDebut());
                assuranceDeRestitution.setDateFin(assuranceARestituer.getDateFin());
                assuranceDeRestitution.setIdExterne(assuranceARestituer.getIdExterne());
                assuranceDeRestitution.setTaux(assuranceARestituer.getTaux());
                assuranceDeRestitution.setIdRepartitionBeneficiairePaiement(idRepartitionBenefPmtDeLaRestitution);
                assuranceDeRestitution.setMontant(new BigDecimal(assuranceARestituer.getMontant()).negate().toString());
                assuranceDeRestitution.setMontantBrut(new BigDecimal(assuranceARestituer.getMontantBrut()).negate()
                        .toString());
                assuranceDeRestitution.add(transaction);
            }
        }
    }

    private String r(double val) {
        return JANumberFormatter.round(String.valueOf(val), 0.05, 2, JANumberFormatter.NEAR);
    }

    /**
     * Calcul de la répartition des paiements au pro rata des salaires versé par les employeurs. Prends en compte les
     * anciennes prestataions. Versement de la différence à l'assuré si montant de la prestation > salaire versé par
     * l'employeur.
     * 
     * @param session
     * @param transaction
     * @param idAssure
     * @param prestationCalculee
     * @param idPrestation
     * 
     * @throws Exception
     * @throws APCalculException
     *             DOCUMENT ME!
     */
    public void repartirPaiements(BSession session, BTransaction transaction, String idAssure,
            APPrestationCalculee prestationCalculee, String idPrestation) throws Exception {
        // TODO assurances sociales, + maj du montant net

        List listBeneficiairePmtPotentielTmp = prestationCalculee.getResultatCalcul()
                .getResultatsCalculsSitProfessionnelle();

        List<APResultatCalculSituationProfessionnel> listBeneficiairePmtPotentiel = getListForJoursIsolees(listBeneficiairePmtPotentielTmp, prestationCalculee, session);

        BigDecimal nbJours = new BigDecimal(prestationCalculee.getNombreJoursSoldes());

        PRTiersWrapper tiersWrapper = PRTiersHelper.getTiersAdresseParId(session, idAssure);

        // Ajouter les frais de garde, s'il y en a
        if ((prestationCalculee.getFraisGarde() != null) && !prestationCalculee.getFraisGarde().isZero()) {
            APRepartitionPaiements repartitionBenefPaiement = new APRepartitionPaiements();

            repartitionBenefPaiement.wantCallValidate(false);
            repartitionBenefPaiement.setSession(session);

            repartitionBenefPaiement.setDateValeur(JACalendar.todayJJsMMsAAAA());
            repartitionBenefPaiement.setIdPrestationApg(idPrestation);

            if (tiersWrapper != null) {
                repartitionBenefPaiement.setNom(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
            }

            repartitionBenefPaiement.setIdTiers(idAssure);
            repartitionBenefPaiement.setMontantBrut(prestationCalculee.getFraisGarde().toString());
            repartitionBenefPaiement.setMontantNet(prestationCalculee.getFraisGarde().toString());
            repartitionBenefPaiement.setTypePaiement(IAPRepartitionPaiements.CS_PAIEMENT_DIRECT);
            repartitionBenefPaiement.setTypePrestation(IAPRepartitionPaiements.CS_FRAIS_GARDE);
            repartitionBenefPaiement.add(transaction);

            // les frais de garde ne sont pas soumis à cotisations
        }

        boolean isVersementAssure = false;

        // Identification du cas pour le calcul des cotisations
        CasCotisations casCotisations = identifierCasCotisations(session, transaction, tiersWrapper,
                listBeneficiairePmtPotentiel, prestationCalculee);

        // Aucun employeur -> tout est versé à l'assuré.
        if ((listBeneficiairePmtPotentiel == null) || (listBeneficiairePmtPotentiel.size() == 0)) {

            // Si une allocation d'exploiation doit être versée, il doit y avoir
            // 1 employeur.
            if ((prestationCalculee.getResultatCalcul().getAllocationJournaliereExploitation() != null)
                    && !JadeStringUtil.isIntegerEmpty(prestationCalculee.getResultatCalcul()
                            .getAllocationJournaliereExploitation().toString())) {
                throw new APCalculException(session.getLabel("MODULE_REPART_PMT_ALLOC_EXPLOIT_SANS_EMPLOYEUR_ERROR"));
            }

            APRepartitionPaiements repartitionBenefPaiement = new APRepartitionPaiements();

            repartitionBenefPaiement.wantCallValidate(false);
            repartitionBenefPaiement.setSession(session);

            repartitionBenefPaiement.setDateValeur(JACalendar.todayJJsMMsAAAA());
            repartitionBenefPaiement.setIdPrestationApg(idPrestation);
            repartitionBenefPaiement.setTauxRJM("100.00");

            repartitionBenefPaiement.setNom(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM));

            repartitionBenefPaiement.setIdTiers(idAssure);

            // Montant brut de la prestation calculée à versé à l'assure
            BigDecimal montantBrutPrestation = prestationCalculee.getMontantJournalier().getBigDecimalValue();

            montantBrutPrestation = montantBrutPrestation.multiply(nbJours);

            String montantBrutPrestationArrondit = JANumberFormatter.formatNoQuote(montantBrutPrestation);

            repartitionBenefPaiement.setMontantBrut(montantBrutPrestationArrondit);
            repartitionBenefPaiement.setTypePaiement(IAPRepartitionPaiements.CS_PAIEMENT_DIRECT);
            repartitionBenefPaiement.setTypePrestation(IAPRepartitionPaiements.CS_NORMAL);
            repartitionBenefPaiement.add(transaction);

            // calculer les cotisations pour l'assure
            genererCotisationsAssure(session, prestationCalculee, repartitionBenefPaiement, casCotisations);

            FWCurrency montantNet = new FWCurrency(montantBrutPrestationArrondit);
            montantNet.add(getMontantTotalCotisation(session, transaction, repartitionBenefPaiement));
            repartitionBenefPaiement.setMontantNet(montantNet.toString());
            repartitionBenefPaiement.update(transaction);

            return;
        }

        boolean isLastRecord = false;
        BigDecimal montantTotalVerse = new BigDecimal(0);

        // Montant brut de la prestation sans facteur de prorata correctif
        BigDecimal montantBrutTotalPrestation = null;
        BigDecimal montantJournalierPrestation = null;

        Integer nbJoursAssure = 0;

        if (prestationCalculee.getMontantJournalier().getBigDecimalValue()
                .compareTo(prestationCalculee.getDroitAcquis().getBigDecimalValue()) < 0) {
            montantBrutTotalPrestation = prestationCalculee.getDroitAcquis().getBigDecimalValue();
        } else {
            montantBrutTotalPrestation = prestationCalculee.getMontantJournalier().getBigDecimalValue();
        }
        montantJournalierPrestation = new BigDecimal(montantBrutTotalPrestation.toString());

        montantBrutTotalPrestation = montantBrutTotalPrestation.multiply(nbJours);

        boolean isAllocVerseeAEmployeur = false;

        for (Iterator iter = listBeneficiairePmtPotentiel.iterator(); iter.hasNext();) {
            APResultatCalculSituationProfessionnel benefPotentiel = (APResultatCalculSituationProfessionnel) iter
                    .next();

            if (!iter.hasNext()) {
                isLastRecord = true;
            }

            // Montant brut de la prestation calculée à versé à l'employeur
            BigDecimal montantBrutPrestation = benefPotentiel.getMontant(montantBrutTotalPrestation);

            // plusieurs employeurs et différentes périodes
            Optional<APMontantJour> opMontantJour = Optional.empty();
            if(prestationCalculee.hasMontantJournalierList()) {
                opMontantJour = prestationCalculee.getMontantJournalierList().stream()
                        .filter(mj -> mj.getSituationProfessionelle().equals(benefPotentiel.getIdSituationProfessionnelle()))
                        .findFirst();
            }
            if(opMontantJour.isPresent()) {
                montantBrutPrestation = opMontantJour.get().calculMontantPrestation();
            }

            if (benefPotentiel.isVersementEmployeur()) {
                APRepartitionPaiements repartitionBenefPaiement = new APRepartitionPaiements();

                repartitionBenefPaiement.wantCallValidate(false);
                repartitionBenefPaiement.setSession(session);
                repartitionBenefPaiement.setIdSituationProfessionnelle(benefPotentiel.getIdSituationProfessionnelle());

                repartitionBenefPaiement.setDateValeur(JACalendar.todayJJsMMsAAAA());
                repartitionBenefPaiement.setIdPrestationApg(idPrestation);
                // InfoRom531
                try {

                    if (((benefPotentiel.getNom() != null) && (benefPotentiel.getNom()
                            .startsWith(APACORPrestationsParser.CONST_TYPE_AFFILI_EMPLOY)))
                            || benefPotentiel.getNom().startsWith(APACORPrestationsParser.CONST_TYPE_AFFILI_INDEP)
                            || benefPotentiel.getNom().startsWith(
                                    APACORPrestationsParser.CONST_TYPE_AFFILI_INDEP_EMPLOY)
                            || benefPotentiel.getNom().startsWith(APACORPrestationsParser.CONST_TYPE_AFFILI_EMPLOY_D_F)
                            || benefPotentiel.getNom().startsWith(APACORPrestationsParser.CONST_TYPE_AFFILI_LTN)
                            || benefPotentiel.getNom().startsWith(APACORPrestationsParser.CONST_TYPE_AFFILI_TSE)
                            || benefPotentiel.getNom().startsWith(
                                    APACORPrestationsParser.CONST_TYPE_AFFILI_TSE_VOLONTAIRE)) {

                        repartitionBenefPaiement.setNom(benefPotentiel.getNom().substring(3,
                                benefPotentiel.getNom().length()));
                    } else {
                        repartitionBenefPaiement.setNom(benefPotentiel.getNom());
                    }
                } catch (Exception e) {
                    repartitionBenefPaiement.setNom(benefPotentiel.getNom());
                }

                if(opMontantJour.isPresent()) {
                    BigDecimal tauxRJM = opMontantJour.get().calculMontantPrestation().divide(prestationCalculee.getMontantJournalier().getBigDecimalValue().multiply(new BigDecimal(prestationCalculee.getNombreJoursSoldes())), 4, RoundingMode.HALF_UP);
                    repartitionBenefPaiement.setTauxRJM(JANumberFormatter.format(tauxRJM
                            .doubleValue() * 100d, 0.01, 2, JANumberFormatter.NEAR));
                    repartitionBenefPaiement.setNombreJoursSoldes(opMontantJour.get().getJours().toString());
                    repartitionBenefPaiement.setMontantJournalierRepartition(opMontantJour.get().getMontant().toString());
                } else {
                    repartitionBenefPaiement.setTauxRJM(JANumberFormatter.format(benefPotentiel.getTauxProRata()
                    .doubleValue() * 100d, 0.01, 2, JANumberFormatter.NEAR));
                }

                // Correspond à l'allocation journalière de la prestation sans
                // prendre les
                // éventuelles prestations précédentes en compte.
                // Est utilisé pour connaître dans le cas des versement à
                // l'employeur,
                // si le montant de la prestation est supérieur au montant versé
                // par l'employeur.
                BigDecimal allocBrutPrestationCumulee = new BigDecimal(montantJournalierPrestation.toString());

                if(opMontantJour.isPresent()) {
                    allocBrutPrestationCumulee = opMontantJour.get().calculMontantPrestation();
                } else {
                    allocBrutPrestationCumulee = allocBrutPrestationCumulee.multiply(nbJours);
                    allocBrutPrestationCumulee = benefPotentiel.getMontant(allocBrutPrestationCumulee);
                }


                // Montant brut correspondant au salaire versé par l'employeur
                if (prestationCalculee.getResultatCalcul().getVersementAssure() != null) {
                    // on revient de ACOR, on sait qu'un certain montant doit
                    // deja etre verse a l'assure
                    isVersementAssure = true;

                    // Hack :
                    // Il arrive que ACOR retourne un montant dans le champs
                    // versement à l'assuré (fichier annonce.pay, $p : pos. 75 à
                    // 83)
                    // alors que tout est versé à l'employeur.
                    // On test donc que si le montant devant être versé à
                    // l'assuré == au montant total de la prestation
                    // et que l'on ait coché la case versement à l'employeur,
                    // dans ce cas
                    // on ne verse rien à l'assuré.

                    // TODO
                    // Ces lignes de codes devront être supprimé lorsque ACOR
                    // aura corrigé le problème.
                    if (allocBrutPrestationCumulee.equals(prestationCalculee.getResultatCalcul().getVersementAssure()
                            .getBigDecimalValue())) {
                        isVersementAssure = false;
                    }

                } else {
                    BigDecimal montantBrutEmployeur = null;
                    BigDecimal montantJournalierMinVerseAssure = new BigDecimal(session.getApplication().getProperty(
                            APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_ASSURE));

                    if ((benefPotentiel.getSalaireJournalierVerse() != null)
                            && !JadeStringUtil.isIntegerEmpty(benefPotentiel.getSalaireJournalierVerse().toString())) {
                        montantBrutEmployeur = benefPotentiel.getSalaireJournalierVerse().getBigDecimalValue();
                    } else {
                        montantBrutEmployeur = benefPotentiel.getSalaireJournalierNonArrondi().getBigDecimalValue();
                    }

                    if(opMontantJour.isPresent()) {
                        montantBrutEmployeur = montantBrutEmployeur.multiply(BigDecimal.valueOf(opMontantJour.get().getJours()));
                    } else {
                        montantBrutEmployeur = montantBrutEmployeur.multiply(nbJours);
                    }

                    // Si le montant à verser est > que le montant versé par
                    // l'employeur et que la différence est plus
                    // grande que le seuil toléré, on verse la différence à
                    // l'assuré
                    if ((allocBrutPrestationCumulee.compareTo(montantBrutEmployeur) > 0)
                            && (allocBrutPrestationCumulee.subtract(montantBrutEmployeur).compareTo(
                                    montantJournalierMinVerseAssure.multiply(nbJours)) > 0)) {

                        // Dans le cas ou l'indépendant est l'assuré, on verse
                        // tout à l'indépendant.
                        if (benefPotentiel.isIndependant() && benefPotentiel.getIdTiers().equals(idAssure)) {
                            isVersementAssure = false;
                        } else {

                            // Example : montant versé par l'employeur = 36.-
                            // [ P1 35.- ]
                            // [ P2 +5.- ]
                            //
                            // allocBrutPrestationCumulee = 40.-
                            // montantBrutPrestation = 5.-
                            // montantBrutEmployeur = 36.-

                            // diff : 40 - 36 = 4.-
                            // Montant a verser à l'assuré : 4.-
                            // Montant à verser à l'employeur : 36.- au total,
                            // soit 1 francs de plus.
                            // (5 - 4) = 1.-

                            BigDecimal diff = allocBrutPrestationCumulee.subtract(montantBrutEmployeur);
                            isVersementAssure = true;
                            montantBrutPrestation = montantBrutPrestation.subtract(diff);
                        }

                    }
                }

                repartitionBenefPaiement.setIdAffilie(benefPotentiel.getIdAffilie());
                repartitionBenefPaiement.setIdSituationProfessionnelle(benefPotentiel.getIdSituationProfessionnelle());
                repartitionBenefPaiement.setIdTiers(benefPotentiel.getIdTiers());

                // Dernier benef paiement, et pas de pmt à l'assuré
                // et pas dans le cas de jour isolé (pas de cotisation employeur)
                // Pour éviter des erreurs d'arrondi, le dernier montant
                // A verser n'est pas calculé au prorata mais par différence du
                // reste.

                if (isLastRecord && !isVersementAssure && listBeneficiairePmtPotentielTmp.size() == listBeneficiairePmtPotentiel.size()) {
                    montantBrutPrestation = montantBrutTotalPrestation.subtract(montantTotalVerse);
                }

                // Si une allocation d'exploiation doit être versée, on l'ajoute
                // au montant brut de l'employeur
                // On verse l'alloc. d'exploitation a l'employeur dont
                // l'allocation d'esploitation est cochee
                // point ouvert 1211
                // point ouvert 1319
                APSituationProfessionnelle sp = new APSituationProfessionnelle();
                sp.setSession(session);
                sp.setIdSituationProf(benefPotentiel.getIdSituationProfessionnelle());
                sp.retrieve(transaction);

                if (!isAllocVerseeAEmployeur && sp.getIsAllocationExploitation().booleanValue()) {
                    if ((prestationCalculee.getResultatCalcul().getAllocationJournaliereExploitation() != null)
                            && !JadeStringUtil.isIntegerEmpty(prestationCalculee.getResultatCalcul()
                                    .getAllocationJournaliereExploitation().toString())) {

                        BigDecimal montantTotAllocExploit = prestationCalculee.getResultatCalcul()
                                .getAllocationJournaliereExploitation().getBigDecimalValue();
                        montantTotAllocExploit = montantTotAllocExploit.multiply(nbJours);
                        montantBrutPrestation = montantBrutPrestation.add(montantTotAllocExploit);
                        // il faut aussi metre a jour le montant brut total
                        // (point ouvert 232)
                        montantBrutTotalPrestation = montantBrutTotalPrestation.add(montantTotAllocExploit);

                        isAllocVerseeAEmployeur = true;
                    }
                }

                repartitionBenefPaiement.setMontantBrut(JANumberFormatter.formatNoQuote(montantBrutPrestation));
                repartitionBenefPaiement.setTypePaiement(IAPRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR);

                repartitionBenefPaiement.setTypePrestation(IAPRepartitionPaiements.CS_NORMAL);
                repartitionBenefPaiement.add(transaction);
                montantTotalVerse = montantTotalVerse.add(new BigDecimal(JANumberFormatter
                        .formatNoQuote(montantBrutPrestation)).setScale(2, BigDecimal.ROUND_DOWN));

                genererCotisations(session, prestationCalculee, repartitionBenefPaiement, benefPotentiel,
                        casCotisations);

                FWCurrency montantNet = new FWCurrency(JANumberFormatter.format(montantBrutPrestation));

                montantNet.add(getMontantTotalCotisation(session, transaction, repartitionBenefPaiement));
                repartitionBenefPaiement.setMontantNet(montantNet.toString());
                repartitionBenefPaiement.update(transaction);
            }

            else if (opMontantJour.isPresent()) {

                if (isLastRecord && listBeneficiairePmtPotentielTmp.size() == listBeneficiairePmtPotentiel.size()) {
                    montantBrutPrestation = montantBrutTotalPrestation.subtract(montantTotalVerse);
                }
                createRepartitionAssure(session, transaction, idAssure, prestationCalculee, idPrestation, tiersWrapper,
                        casCotisations, new BigDecimal(0), montantBrutPrestation, opMontantJour.get().getJours(), opMontantJour.get().getMontant().toString());
                montantTotalVerse = montantTotalVerse.add(new BigDecimal(JANumberFormatter
                        .formatNoQuote(montantBrutPrestation)).setScale(2, BigDecimal.ROUND_DOWN));
            }
            // On verse le tout à l'assuré
            else {
                isVersementAssure = true;
                if(opMontantJour.isPresent()) {
                    nbJoursAssure+= opMontantJour.get().getJours();
                }
            }
        }

        // Dernier contrôle, s'il y a une allocation d'exploitation et qu'elle
        // n'a été versée à aucun employeur (
        // isVersementEmployeur==false)
        // on génère une exception.
        if ((prestationCalculee.getResultatCalcul().getAllocationJournaliereExploitation() != null)
                && !JadeStringUtil.isIntegerEmpty(prestationCalculee.getResultatCalcul()
                        .getAllocationJournaliereExploitation().toString()) && (isAllocVerseeAEmployeur == false)) {
            throw new APCalculException(session.getLabel("MODULE_REPART_PMT_ALLOC_EXPLOIT_SANS_EMPLOYEUR_ERROR"));
        }

        // Il y a un montant à verser à l'assuré
        if (isVersementAssure) {
            createRepartitionAssure(session, transaction, idAssure, prestationCalculee, idPrestation, tiersWrapper, casCotisations, montantTotalVerse, montantBrutTotalPrestation, nbJoursAssure, null);
        }
    }

    private void createRepartitionAssure(BSession session, BTransaction transaction, String idAssure, APPrestationCalculee prestationCalculee,
                                         String idPrestation, PRTiersWrapper tiersWrapper, CasCotisations casCotisations, BigDecimal montantTotalVerse,
                                         BigDecimal montantBrutTotalPrestation, Integer nbJoursAssure, String montantJournalier) throws Exception {
        APRepartitionPaiements repartitionBenefPaiement = new APRepartitionPaiements();

        repartitionBenefPaiement.wantCallValidate(false);
        repartitionBenefPaiement.setSession(session);

        repartitionBenefPaiement.setDateValeur(JACalendar.todayJJsMMsAAAA());
        repartitionBenefPaiement.setIdPrestationApg(idPrestation);

        if (tiersWrapper != null) {
            repartitionBenefPaiement.setNom(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
        }

        repartitionBenefPaiement.setIdTiers(idAssure);

        if (prestationCalculee.getResultatCalcul().getVersementAssure() != null) {
            repartitionBenefPaiement.setMontantBrut(JANumberFormatter.formatNoQuote(prestationCalculee
                    .getResultatCalcul().getVersementAssure().getBigDecimalValue()));
        } else {
            repartitionBenefPaiement.setMontantBrut(JANumberFormatter.formatNoQuote(montantBrutTotalPrestation
                    .subtract(montantTotalVerse)));
        }

        repartitionBenefPaiement.setTypePaiement(IAPRepartitionPaiements.CS_PAIEMENT_DIRECT);
        repartitionBenefPaiement.setTypePrestation(IAPRepartitionPaiements.CS_NORMAL);
        repartitionBenefPaiement.setNombreJoursSoldes(nbJoursAssure.toString());
        repartitionBenefPaiement.add(transaction);

        genererCotisationsAssure(session, prestationCalculee, repartitionBenefPaiement, casCotisations);

        FWCurrency montantNet = new FWCurrency(repartitionBenefPaiement.getMontantBrut());

        montantNet.add(getMontantTotalCotisation(session, transaction, repartitionBenefPaiement));
        repartitionBenefPaiement.setMontantNet(montantNet.toString());
        if(montantJournalier != null) {
            repartitionBenefPaiement.setMontantJournalierRepartition(montantJournalier);
        }
        repartitionBenefPaiement.update(transaction);
    }

    List<APResultatCalculSituationProfessionnel> getListForJoursIsolees (List<APResultatCalculSituationProfessionnel> list, APPrestationCalculee prestation, BSession session) throws Exception {

        String isFerciab = JadePropertiesService.getInstance().getProperty(APApplication.PROPERTY_IS_FERCIAB);
        if(!"true".equals(isFerciab) || !APTypeDePrestation.JOUR_ISOLE.getCodesystemString().equals(prestation.getCsGenrePrestation()) ) {
            return list;
        }

        List<APResultatCalculSituationProfessionnel> newList = new ArrayList<>();

        String idAssuranceParitaireJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID);
        String idAssuranceParitaireBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID);
        String idAssurancePersonnelJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID);
        String idAssurancePersonnelBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID);
        for (APResultatCalculSituationProfessionnel employeur : list) {
            List<IAFAssurance> listAssurance = APRechercherAssuranceFromDroitCotisationService.rechercher(prestation.getIdDroit(),
                    employeur.getIdAffilie(), session);
            for (IAFAssurance assurance : listAssurance) {
                if(employeur.isIndependant()){
                    // Concerne uniquement les employeurs indépendant cotisants à une assurance personnelle
                    if(assurance.getAssuranceId().equals(idAssurancePersonnelJU)
                        || assurance.getAssuranceId().equals(idAssurancePersonnelBE)) {
                        newList.add(employeur);
                    }
                    // S'il n'est pas indépendant, on va contrôler s'il cotise à une assurance paritaire
                }else if(assurance.getAssuranceId().equals(idAssuranceParitaireJU)
                        || assurance.getAssuranceId().equals(idAssuranceParitaireBE)) {
                    newList.add(employeur);
                }
            }
        }
        return newList;
    }

    /**
     * Réparti les paiements de la prestation ACM, avec comme base de calcul, la répartition de la prestation
     * précédente.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param lastPrestation
     *            DOCUMENT ME!
     * @param prestationACM
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void repartirPaiementsACM(BSession session, BTransaction transaction, APPrestation lastPrestation,
            APPrestation prestationACM, HashMap montants) throws Exception {

        // Récupération de la répartition de paiements de la prestation
        // précédente
        APRepartitionPaiementsManager mgr = new APRepartitionPaiementsManager();

        mgr.setSession(session);
        mgr.setForIdPrestation(lastPrestation.getIdPrestationApg());
        mgr.find(transaction);
        JAVector mgrResult = mgr.getContainer();

        // On ne prend pas en compte les montant ventilé et les paiements à
        // l'assuré
        // On les supprimes de la liste avant de la reparcourir pour creer les
        // repartitions ACM
        // Ceci pour etre sur de creer une repartition lors du dernier passage
        // de la boucle de creation
        for (Iterator iter = mgrResult.iterator(); iter.hasNext();) {

            APRepartitionPaiements element = (APRepartitionPaiements) iter.next();
            // On ne prend pas en compte les montant ventilé
            if (!JadeStringUtil.isIntegerEmpty(element.getIdParent())) {
                iter.remove();
            }

            if (IAPRepartitionPaiements.CS_PAIEMENT_DIRECT.equals(element.getTypePaiement())) {
                // Pas de prestation ACM payé à l'assuré.
                iter.remove();
            }
        }

        // pour toutes les repartitions on regarde si on a un montant a verser
        // pour cet employeur
        // on verifie aussi que tous les montant calcule ont ete reparti, si ce
        // n'est pas le cas, on
        // cherche une repartition pour cet employeur dans les prestations de ce
        // droit
        for (Iterator iter = mgrResult.iterator(); iter.hasNext() || (!iter.hasNext() && !montants.isEmpty());) {

            APRepartitionPaiements element = null;
            if (iter.hasNext()) {
                element = (APRepartitionPaiements) iter.next();
            } else {
                String keyIdEmployeur = (String) montants.keySet().iterator().next();
                // on cherche une repartition pour cet employeur dans les autre
                // prestations
                element = retrieveRepartitionForEmployeurInDroit(session, transaction, lastPrestation.getIdDroit(),
                        keyIdEmployeur);
            }

            APRepartitionPaiements repartitionACM = new APRepartitionPaiements();

            repartitionACM.wantCallValidate(false);
            repartitionACM.setSession(session);
            repartitionACM.setDateValeur(JACalendar.todayJJsMMsAAAA());
            repartitionACM.setIdAffilie(element.getIdAffilie());
            repartitionACM.setIdDomaineAdressePaiement(element.getIdDomaineAdressePaiement());
            repartitionACM.setIdPrestationApg(prestationACM.getIdPrestationApg());
            repartitionACM.setIdTiers(element.getIdTiers());
            repartitionACM.setIdTiersAdressePaiement(element.getIdTiersAdressePaiement());
            repartitionACM.setIdSituationProfessionnelle(element.getIdSituationProfessionnelle());
            repartitionACM.setNom(element.getNom());
            repartitionACM.setTauxRJM(element.getTauxRJM());
            repartitionACM.setTypePaiement(element.getTypePaiement());
            repartitionACM.setTypePrestation(element.getTypePrestation());

            // Le montant des repartitions pour chacun des employeurs est dans
            // la map montants
            String idEmployeur = element.loadSituationProfessionnelle().getIdEmployeur();
            // on a un montant pour cet employeur
            if (montants.containsKey(idEmployeur)) {
                BigDecimal montantAcmJournalier = (BigDecimal) montants.get(idEmployeur);
                BigDecimal montantAcm = montantAcmJournalier.multiply(new BigDecimal(prestationACM
                        .getNombreJoursSoldes()));
                repartitionACM.setMontantBrut(montantAcm.toString());
                // on retire le montant
                montants.remove(idEmployeur);
            } else {
                repartitionACM.setMontantBrut("0");
            }

            // on ne cree que les repartitions avec un montant superieur a zero
            if (Double.parseDouble(repartitionACM.getMontantBrut()) > 0) {
                repartitionACM.add(transaction);

                // Génération des cotisations ACM
                APPrestationCalculee prestationCalculee = new APPrestationCalculee();

                prestationCalculee.setDateDebut(new JADate(prestationACM.getDateDebut()));
                prestationCalculee.setDateFin(new JADate(prestationACM.getDateFin()));
                prestationCalculee.setNombreJoursSoldes(prestationACM.getNombreJoursSoldes());
                genererCotisationsACM(session, prestationCalculee, repartitionACM);

                FWCurrency montantNet = new FWCurrency(JANumberFormatter.format(repartitionACM.getMontantBrut()));

                montantNet.add(getMontantTotalCotisation(session, transaction, repartitionACM));
                repartitionACM.setMontantNet(montantNet.toString());
                repartitionACM.update(transaction);
            }
        }
    }

    /**
     * Réparti les paiements de la prestation LAMat, avec comme base de calcul, la repartition de la prestation
     * precedente.
     * 
     * On se base sur les repartitions de la prestation standard pour le calcul de la repartitions LAMat.
     * 
     * 1)Dans un permier temps on ne considere que les employeurs.
     * 
     * a) Dans un premier temps on determine le taux de repartition libere par les employeurs qui recevait un paiement
     * Standard, mais qui n'ont pas droit a la LaMat.
     * 
     * b) Puis, on supprime de la liste les employeurs pour lesquels on paye a l'assure.
     * 
     * c) On augmente le taux des employeurs restants (qui on droit a la LaMat et qui on un paiement employeur) en
     * fonction du taux libere du poit a.
     * 
     * d) On determine le montant maximal pour chaque employeur avec le montant de la LaMat et le taux de l'employeur.
     * 
     * e) On donne a l'employeur (standard + LaMat) au maximum le montant du salaire journalie
     * 
     * 2)Finalement, si il reste de l'argent, on verse le solde a l'assure.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param lastPrestation
     *            DOCUMENT ME!
     * @param prestationLAMat
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void repartirPaiementsLAMat(BSession session, BTransaction transaction, APPrestation lastPrestation,
            APPrestation prestationLAMat) throws Exception {

        // Récupération de la répartition de paiements de la prestation
        // précédente
        APRepartitionPaiementsManager mgr = new APRepartitionPaiementsManager();

        mgr.setSession(session);
        mgr.setForIdPrestation(lastPrestation.getIdPrestationApg());
        mgr.find(transaction);
        JAVector mgrResult = mgr.getContainer();

        FWCurrency montantTotalReparti = new FWCurrency(0);

        // //////////////////////////////////////////////////////////////////////////////////////
        // EMPLOYEURS
        // //////////////////////////////////////////////////////////////////////////////////////

        // la somme des taux libere par les repartitions standards payees a des
        // employeurs
        // qui n'ont pas droit a la LaMat
        BigDecimal sommeTauxLibere = new BigDecimal(0);
        int nbSitProPaiementAssure = 0;

        // pour toutes les repartitions de la prestation precedante
        for (Iterator iter = mgrResult.iterator(); iter.hasNext();) {

            APRepartitionPaiements element = (APRepartitionPaiements) iter.next();
            // On ne prend pas en compte les montants ventilés
            if (!JadeStringUtil.isIntegerEmpty(element.getIdParent())) {
                iter.remove();
                continue;
            }

            // On regarde la situation prof. pour savoir si la case LAMat est
            // cochee.
            // C'est cette coche qui fait foi et non pas les cotisations payees
            // dans les affiliation.
            APSituationProfessionnelle sitPro = element.loadSituationProfessionnelle();

            if (sitPro != null) {

                // si paiement a l'employeur et que la coche LaMat n'est pas
                // cochee on ajoute le taux
                // ou qu'une date de fin de contract est passee
                // a la somme des taux liberes
                boolean finContractAvantDebutPeriode = false;
                if (!JadeStringUtil.isEmpty(sitPro.getDateFinContrat())) {
                    if (BSessionUtil.compareDateFirstLowerOrEqual(session, sitPro.getDateFinContrat(),
                            prestationLAMat.getDateDebut())) {
                        finContractAvantDebutPeriode = true;
                    }
                }

                if (IAPRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR.equals(element.getTypePaiement())
                        && (!sitPro.getHasLaMatPrestations().booleanValue() || finContractAvantDebutPeriode)) {

                    sommeTauxLibere = sommeTauxLibere.add(new BigDecimal(element.getTauxRJM()));
                    iter.remove();
                }

                // si paiement a l'assure, on retire la repartition de la liste
                if (IAPRepartitionPaiements.CS_PAIEMENT_DIRECT.equals(element.getTypePaiement())) {

                    nbSitProPaiementAssure++;
                    iter.remove();
                }
            } else {

                // lors du paiement a l'assure, la situation prof. est null.
                // Donc, il faut aussi retirer la repartition de la liste dans
                // ce cas.
                // Car le cas ci-dessus ne suffit pas!
                nbSitProPaiementAssure++;
                iter.remove();
            }
        }

        // mise à jour du tauxRmj
        BigDecimal tauxAjustement = new BigDecimal("100.00").subtract(sommeTauxLibere);

        for (Iterator iter = mgrResult.iterator(); iter.hasNext();) {

            APRepartitionPaiements element = (APRepartitionPaiements) iter.next();

            BigDecimal tauxRmj = new BigDecimal(element.getTauxRJM());
            tauxRmj = tauxRmj.setScale(5);
            tauxRmj = tauxRmj.multiply(tauxAjustement);
            tauxRmj = tauxRmj.divide(new BigDecimal("100"), 5, BigDecimal.ROUND_DOWN);

            element.setTauxRJM(tauxRmj.toString());
        }

        // creation des repartitions LAMat pour les employeurs
        for (Iterator iter = mgrResult.iterator(); iter.hasNext();) {
            APRepartitionPaiements element = (APRepartitionPaiements) iter.next();
            APRepartitionPaiements repartitionLAMat = new APRepartitionPaiements();

            repartitionLAMat.wantCallValidate(false);

            repartitionLAMat.setSession(session);
            repartitionLAMat.setDateValeur(JACalendar.todayJJsMMsAAAA());
            repartitionLAMat.setIdAffilie(element.getIdAffilie());
            repartitionLAMat.setIdDomaineAdressePaiement(element.getIdDomaineAdressePaiement());
            repartitionLAMat.setIdPrestationApg(prestationLAMat.getIdPrestationApg());
            repartitionLAMat.setIdTiers(element.getIdTiers());
            repartitionLAMat.setIdTiersAdressePaiement(element.getIdTiersAdressePaiement());
            repartitionLAMat.setIdSituationProfessionnelle(element.getIdSituationProfessionnelle());
            repartitionLAMat.setNom(element.getNom());
            repartitionLAMat.setTauxRJM(element.getTauxRJM());
            repartitionLAMat.setTypePaiement(element.getTypePaiement());
            repartitionLAMat.setTypePrestation(element.getTypePrestation());

            // Preparation du montant (maximal) de la repartition
            // Dernier enregistrement, pour éviter les erreurs d'arrondi, on
            // sette la différence (si pas de paiement assure)
            FWCurrency montant = null;
            if (!iter.hasNext() && (nbSitProPaiementAssure == 0)) {
                montant = new FWCurrency(prestationLAMat.getMontantBrut());

                montant.sub(montantTotalReparti);
            } else {
                montant = new FWCurrency(JANumberFormatter.format(
                        PRCalcul.pourcentage100(prestationLAMat.getMontantBrut(), repartitionLAMat.getTauxRJM()), 0.05,
                        2, JANumberFormatter.NEAR));
            }

            // verifie que le montant total (standard+LAMat) ne depasse pas le
            // montant verse par l'employeur

            // on cherche le montant journalier verse pour cette situation prof.
            FWCurrency salaireJournalierVerse = APSituationProfessionnelleHelper.getSalaireJournalierVerse(element
                    .loadSituationProfessionnelle());
            salaireJournalierVerse = new FWCurrency(JANumberFormatter.format(salaireJournalierVerse.toString(), 1, 2,
                    JANumberFormatter.SUP));

            // le montant journalier de la prestation standard
            FWCurrency montantJournalierStandard = retrieveMontantJournalierPrestationStd(prestationLAMat.getIdDroit(),
                    element.loadSituationProfessionnelle().getIdEmployeur(), prestationLAMat.getDateDebut(),
                    prestationLAMat.getDateFin(), session);

            // si c'est une adoption, il ne faut pas tenir compte du montant
            // journalier de la prestation std qui
            // n'est utilisee que pour le calcul et effacee ensuite
            if (IAPDroitMaternite.CS_REVISION_MATERNITE_2005.equals(prestationLAMat.getNoRevision())) {
                APEnfantMatManager emManager = new APEnfantMatManager();
                emManager.setSession(session);
                emManager.setForIdDroitMaternite(prestationLAMat.getIdDroit());
                emManager.find(transaction);

                Iterator iter2 = emManager.iterator();
                while (iter2.hasNext()) {
                    APEnfantMat enfantMat = (APEnfantMat) iter2.next();

                    if (enfantMat.getIsAdoption().booleanValue()) {
                        montantJournalierStandard = new FWCurrency("0.00");
                    }
                }
            }

            // le montantJournalier LAMat
            BigDecimal montantJournalierLAMat = new BigDecimal(montant.toString());
            montantJournalierLAMat = montantJournalierLAMat.divide(
                    new BigDecimal(prestationLAMat.getNombreJoursSoldes()), BigDecimal.ROUND_HALF_UP);

            // si le montant journalier de la prestation std est plus grand ou
            // egal au salaire journalier
            // pas de prestaions LAMat pour cet employeur
            if (montantJournalierStandard.compareTo(salaireJournalierVerse) >= 0) {
                montant = new FWCurrency("0");
            }
            // si la somme des montant journalier Std et LAMat est plus grande
            // ou egale au salaire journalier
            // on donne la difference entre la prestation std et le salaire
            // journalier le tout multiplie par
            // le nombre de jours
            else if (montantJournalierLAMat.add(new BigDecimal(montantJournalierStandard.toString())).compareTo(
                    new BigDecimal(salaireJournalierVerse.toString())) >= 0) {

                montantJournalierLAMat = new BigDecimal(salaireJournalierVerse.toString()).subtract(new BigDecimal(
                        montantJournalierStandard.toString()));
                montant = new FWCurrency(JANumberFormatter.format(
                        montantJournalierLAMat.multiply(new BigDecimal(prestationLAMat.getNombreJoursSoldes()))
                                .toString(), 1, 2, JANumberFormatter.SUP));
            } else {
                // le montant a verser est celui de la repartition du taux RJM
                // Mais on veut arrondire le montant journalier a 0.05 cts
                montantJournalierLAMat = new BigDecimal(JANumberFormatter.format(montantJournalierLAMat.toString(),
                        0.05, 2, JANumberFormatter.NEAR));
                montant = new FWCurrency(montantJournalierLAMat.multiply(
                        new BigDecimal(prestationLAMat.getNombreJoursSoldes())).toString());
            }

            // on set le montant
            repartitionLAMat.setMontantBrut(montant.toString());
            montantTotalReparti.add(montant.toString());

            // on ne cree que les repartitions avec un montant superieur a zero
            if (Double.parseDouble(repartitionLAMat.getMontantBrut()) > 0) {
                repartitionLAMat.add(transaction);

                // Pas de cotisations pour la LAMat
                FWCurrency montantNet = new FWCurrency(JANumberFormatter.format(repartitionLAMat.getMontantBrut()));

                montantNet.add(0);
                repartitionLAMat.setMontantNet(montantNet.toString());
                repartitionLAMat.update(transaction);
            }
        }

        // //////////////////////////////////////////////////////////////////////////////////////
        // ASSURE
        // //////////////////////////////////////////////////////////////////////////////////////

        // si il reste de l'argent a verse. on le verse a l'assure
        if (montantTotalReparti.compareTo(new FWCurrency(prestationLAMat.getMontantBrut())) < 0) {

            APRepartitionPaiements repartitionAssure = new APRepartitionPaiements();

            repartitionAssure.wantCallValidate(false);
            repartitionAssure.setSession(session);
            repartitionAssure.setDateValeur(JACalendar.todayJJsMMsAAAA());
            repartitionAssure.setIdPrestationApg(prestationLAMat.getIdPrestationApg());

            APDroitLAPG droit = new APDroitLAPG();
            droit.setSession(session);
            droit.setIdDroit(prestationLAMat.getIdDroit());
            droit.retrieve(transaction);
            PRTiersWrapper tiersWrapper = droit.loadDemande().loadTiers();
            if (tiersWrapper != null) {
                repartitionAssure.setNom(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
            }
            repartitionAssure.setIdTiers(droit.loadDemande().getIdTiers());

            FWCurrency montant = new FWCurrency(prestationLAMat.getMontantBrut());
            montant.sub(montantTotalReparti);
            repartitionAssure.setMontantBrut(montant.toString());

            repartitionAssure.setTypePaiement(IAPRepartitionPaiements.CS_PAIEMENT_DIRECT);
            repartitionAssure.setTypePrestation(IAPRepartitionPaiements.CS_NORMAL);
            repartitionAssure.add(transaction);

            // Pas de cotisations pour la LAMat, mais des impots à la source
            genererCotisationsLaMat(session, prestationLAMat, repartitionAssure, droit);

            FWCurrency montantNet = new FWCurrency(repartitionAssure.getMontantBrut());

            montantNet.add(getMontantTotalCotisation(session, transaction, repartitionAssure));
            repartitionAssure.setMontantNet(montantNet.toString());
            repartitionAssure.update(transaction);
        }
    }

    /**
     * Ajout des bénéficiaires de paiements pour leur restituer les montants des prestations à restituer.
     * 
     * @param session
     * @param transaction
     * @param restitution
     * @param prestationsARestituer
     * 
     * @throws Exception
     */
    public void restituerPaiements(BSession session, BTransaction transaction, APPrestation restitution,
            APPrestation[] prestationsARestituer) throws Exception {
        for (int i = 0; i < prestationsARestituer.length; i++) {
            APPrestation prestationARestituer = prestationsARestituer[i];
            APRepartitionPaiementsManager mgr = new APRepartitionPaiementsManager();

            mgr.setSession(session);
            mgr.setForIdPrestation(prestationARestituer.getIdPrestationApg());
            mgr.setParentOnly(true);
            mgr.find(transaction);

            // Restitution du parent, dans le cas de montant ventilé.
            // String idParentRepartRestitution = "0";
            for (int j = 0; j < mgr.size(); j++) {
                APRepartitionPaiements repartARestituer = (APRepartitionPaiements) mgr.getEntity(j);

                // Recherche du parent lui même
                // if (JAUtil.isIntegerEmpty(repartARestituer.getIdParent())) {
                APRepartitionPaiements repartBenefPmt = new APRepartitionPaiements();

                repartBenefPmt.setSession(session);

                // La date du jour
                repartBenefPmt.setIdDomaineAdressePaiement(repartARestituer.getIdDomaineAdressePaiement());
                repartBenefPmt.setIdTiersAdressePaiement(repartARestituer.getIdTiersAdressePaiement());
                repartBenefPmt.setDateValeur(JACalendar.todayJJsMMsAAAA());

                repartBenefPmt.setIdTiers(repartARestituer.getIdTiers());

                // ////////////////////////////////////////////////////////////////////////////////////////////
                //
                // Workaround pour correction de cas issus de la reprise !!!
                //
                // ////////////////////////////////////////////////////////////////////////////////////////////
                // Correctif pour reprise CCVD
                try {
                    if (IAPRepartitionPaiements.CS_PAIEMENT_DIRECT.equals(repartARestituer.getTypePaiement())) {
                        if (!JadeStringUtil.isBlankOrZero(repartARestituer.getIdAffilie())) {
                            System.out
                                    .println("Reset de la réf. sur l'affilié car pmt direct. (Peut arriver en cas d'erreur dans les reprise).");
                            System.out.println("idPrestation/idRepartionARestituer/idAffilie reseté "
                                    + repartARestituer.getIdPrestationApg() + "/"
                                    + repartARestituer.getIdRepartitionBeneficiairePaiement() + "/"
                                    + repartARestituer.getIdAffilie());

                            repartBenefPmt.setIdAffilie("");
                        } else {
                            repartBenefPmt.setIdAffilie(repartARestituer.getIdAffilie());
                        }

                    } else {
                        repartBenefPmt.setIdAffilie(repartARestituer.getIdAffilie());
                    }
                } catch (Exception e) {
                    // Comportement standard
                    System.out.println("Erreur lors de la maj de la réf. sur idAffilie !!! " + e.getMessage());
                    repartBenefPmt.setIdAffilie(repartARestituer.getIdAffilie());
                }

                // Cas CCVD pour reprise de données par exemple.
                // Dans ce cas, les décomptes ne sortent pas correctement 2
                // décomptes pour PAR + nouvelle prest.
                try {

                    if (JadeStringUtil.isBlankOrZero(repartARestituer.getIdSituationProfessionnelle())) {
                        APPrestation prest = new APPrestation();
                        prest.setSession(session);
                        prest.setIdPrestationApg(repartARestituer.getIdPrestationApg());
                        prest.retrieve(transaction);

                        // APDroitLAPG droit = new APDroitLAPG();
                        // droit.setSession(session);
                        // droit.setIdDroit(prest.getIdDroit());
                        // droit.retrieve(transaction);
                        //
                        // String idDroit = droit.getIdDroitParent();
                        // if (JadeStringUtil.isBlankOrZero(idDroit)) {
                        // idDroit = droit.getIdDroit();
                        // }

                        APSituationProfessionnelleManager spMgr = new APSituationProfessionnelleManager();
                        spMgr.setSession(session);
                        spMgr.setForIdDroit(prest.getIdDroit());
                        spMgr.find(transaction);

                        String idSP0 = "";
                        String idSP1 = "";
                        String idSP2 = "";
                        for (int k = 0; k < spMgr.size(); k++) {
                            APSituationProfessionnelle sp = (APSituationProfessionnelle) spMgr.get(k);
                            if (k == 0) {
                                idSP0 = sp.getIdSituationProf();
                            }

                            APEmployeur emp = sp.loadEmployeur();
                            if ((emp != null) && !JadeStringUtil.isBlankOrZero(emp.getIdTiers())
                                    && emp.getIdTiers().equals(repartBenefPmt.getIdTiers())) {
                                idSP1 = sp.getIdSituationProf();
                                if (!JadeStringUtil.isBlankOrZero(emp.getIdAffilie())
                                        && emp.getIdAffilie().equals(repartBenefPmt.getIdAffilie())) {
                                    idSP2 = sp.getIdSituationProf();
                                    break;
                                }
                            }
                        }

                        if (!JadeStringUtil.isBlankOrZero(idSP2)) {
                            System.out.println("Récupération de idSitProf (2). idRepartARestituer/idSitProf = "
                                    + repartARestituer.getIdRepartitionBeneficiairePaiement() + "/" + idSP2);
                            repartBenefPmt.setIdSituationProfessionnelle(idSP2);
                        } else if (!JadeStringUtil.isBlankOrZero(idSP1)) {
                            repartBenefPmt.setIdSituationProfessionnelle(idSP1);
                            System.out.println("Récupération de idSitProf (1). idRepartARestituer/idSitProf = "
                                    + repartARestituer.getIdRepartitionBeneficiairePaiement() + "/" + idSP1);
                        } else {
                            repartBenefPmt.setIdSituationProfessionnelle(idSP0);
                            System.out.println("Récupération de idSitProf (0). idRepartARestituer/idSitProf = "
                                    + repartARestituer.getIdRepartitionBeneficiairePaiement() + "/" + idSP0);
                        }
                    } else {
                        repartBenefPmt.setIdSituationProfessionnelle(repartARestituer.getIdSituationProfessionnelle());
                    }

                } catch (Exception e) {
                    // Comportement standard
                    System.out.println("Erreur lors de la récupération de l'idSitProf !!! " + e.getMessage());
                    repartBenefPmt.setIdSituationProfessionnelle(repartARestituer.getIdSituationProfessionnelle());
                }

                repartBenefPmt.setTypeAssociationAssurance(repartARestituer.getTypeAssociationAssurance());
                repartBenefPmt.setIdPrestationApg(restitution.getIdPrestationApg());
                repartBenefPmt.setMontantBrut(new BigDecimal(repartARestituer.getMontantBrut()).negate().toString());
                repartBenefPmt.setMontantNet(new BigDecimal(repartARestituer.getMontantNet()).negate().toString());
                repartBenefPmt.setMontantVentile(new BigDecimal(repartARestituer.getMontantVentile()).negate()
                        .toString());
                repartBenefPmt.setNom(repartARestituer.getNom());
                repartBenefPmt.setTypePaiement(repartARestituer.getTypePaiement());
                repartBenefPmt.setTypePrestation(repartARestituer.getTypePrestation());
                repartBenefPmt.setNombreJoursSoldes(repartARestituer.getNombreJoursSoldes());
                repartBenefPmt.add(transaction);
                montantCotisationDesRestitutions(session, transaction,
                        repartBenefPmt.getIdRepartitionBeneficiairePaiement(), repartARestituer);
                // idParentRepartRestitution =
                // repartBenefPmt.getIdRepartitionBeneficiairePaiement();

                // // Restitution des fils du parent....
                // APRepartitionPaiementsManager mgrFils = new
                // APRepartitionPaiementsManager();
                // mgr.setSession(session);
                // mgr.setForIdPrestation(prestationARestituer.getIdPrestationApg());
                // mgr.setForIdParent(repartARestituer.getIdParent());
                // mgr.find(transaction);
                //
                // for (int k = 0; k < mgrFils.size(); k++) {
                // APRepartitionPaiements repartFilsARestituer =
                // (APRepartitionPaiements) mgrFils.getEntity(k);
                //
                // APRepartitionPaiements repartFilsBenefPmt = new
                // APRepartitionPaiements();
                // repartFilsBenefPmt.setSession(session);
                //
                // repartFilsBenefPmt.setDateValeur(JACalendar.todayJJsMMsAAAA());
                // repartFilsBenefPmt.setIdAffilie(repartFilsARestituer.getIdAffilie());
                //
                // repartFilsBenefPmt.setIdParent(idParentRepartRestitution);
                // repartFilsBenefPmt.setIdPrestationApg(restitution.getIdPrestationApg());
                //
                // repartFilsBenefPmt.setIdTiers(repartFilsARestituer.getIdTiers());
                // repartFilsBenefPmt.setIdDomaineAdressePaiement(repartFilsARestituer.getIdDomaineAdressePaiement());
                // repartFilsBenefPmt.setIdTiersAdressePaiement(repartFilsARestituer.getIdTiersAdressePaiement());
                // repartFilsBenefPmt.setMontantBrut(new
                // BigDecimal(repartFilsARestituer.getMontantBrut()).negate()
                // .toString());
                // repartFilsBenefPmt.setMontantNet(new
                // BigDecimal(repartFilsARestituer.getMontantNet()).negate()
                // .toString());
                // repartFilsBenefPmt.setMontantVentile(new
                // BigDecimal(repartFilsARestituer.getMontantVentile())
                // .negate().toString());
                // repartFilsBenefPmt.setNom(repartFilsARestituer.getNom());
                // repartFilsBenefPmt.setTypePaiement(repartFilsARestituer.getTypePaiement());
                // repartFilsBenefPmt.setTypePrestation(repartFilsARestituer.getTypePrestation());
                // repartFilsBenefPmt.add(transaction);
                // montantCotisationDesRestitutions(session, transaction,
                // repartFilsBenefPmt.getIdRepartitionBeneficiairePaiement(),
                // repartFilsARestituer);
                // }
                // }
            }
        }
    }

    /**
     * Donne le montant journalier de la prestation standard donne a un employeur pour un droit
     * 
     * @param idDroit
     * @param idEmployeur
     * @return
     */
    private FWCurrency retrieveMontantJournalierPrestationStd(String idDroit, String idEmployeur, String dateDebut,
            String dateFin, BSession session) {

        // on cherche une prestation de type standard
        APPrestationManager prestManager = new APPrestationManager();
        prestManager.setSession(session);
        prestManager.setForIdDroit(idDroit);
        prestManager.setForGenre(APTypeDePrestation.STANDARD.getCodesystemString());
        prestManager.setFromDateDebut(dateDebut);
        prestManager.setToDateFin(dateFin);
        try {
            prestManager.find();

            if (prestManager.getSize() > 0) {

                APPrestation prestation = (APPrestation) prestManager.get(0);

                // on cherche les repartition de la prestation
                APRepartitionPaiementsManager repManager = new APRepartitionPaiementsManager();
                repManager.setSession(session);
                repManager.setForIdPrestation(prestation.getIdPrestationApg());
                repManager.find();

                // on cherche une repartition pour l'employeur donne
                Iterator iter = repManager.iterator();
                while (iter.hasNext()) {
                    APRepartitionPaiements repartition = (APRepartitionPaiements) iter.next();
                    APSituationProfessionnelle sitPro = repartition.loadSituationProfessionnelle();

                    if ((sitPro != null) && sitPro.getIdEmployeur().equals(idEmployeur)) {

                        BigDecimal montantBrut = new BigDecimal(repartition.getMontantBrut());
                        BigDecimal nbJours = new BigDecimal(prestation.getNombreJoursSoldes());

                        return new FWCurrency(montantBrut.divide(nbJours, BigDecimal.ROUND_HALF_UP).toString());
                    }
                }

            }

        } catch (Exception e) {
            return new FWCurrency("0");
        }

        return new FWCurrency("0");
    }

    /**
     * @param session
     * @param transaction
     * @param idDroit
     * @param keyIdEmployeur
     * @return
     */
    private APRepartitionPaiements retrieveRepartitionForEmployeurInDroit(BSession session, BTransaction transaction,
            String idDroit, String keyIdEmployeur) throws Exception {
        APPrestationManager prestManager = new APPrestationManager();
        prestManager.setSession(session);
        prestManager.setForIdDroit(idDroit);

        prestManager.find(transaction);
        // toutes les prestation du droit
        Iterator iterPrest = prestManager.iterator();
        while (iterPrest.hasNext()) {
            APPrestation prestation = (APPrestation) iterPrest.next();

            APRepartitionPaiementsManager repManager = new APRepartitionPaiementsManager();
            repManager.setSession(session);
            repManager.setForIdPrestation(prestation.getIdPrestationApg());
            repManager.find(transaction);

            Iterator iterRep = repManager.iterator();
            while (iterRep.hasNext()) {
                APRepartitionPaiements rep = (APRepartitionPaiements) iterRep.next();

                if (keyIdEmployeur.equals(rep.loadSituationProfessionnelle().getIdEmployeur())) {
                    return rep;
                }
            }
        }

        throw new Exception("Aucune repartition pour l'employeur: " + keyIdEmployeur);
    }
}
