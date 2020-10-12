package globaz.naos.db.assurance;

import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.process.DSProcessValidation;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.db.tauxAssurance.AFTauxAssuranceManager;
import globaz.naos.db.tauxAssurance.AFTauxMoyen;
import globaz.naos.db.tauxAssurance.AFTauxMoyenManager;
import globaz.naos.db.tauxAssurance.AFTauxVariableUtil;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;

/**
 * Cette classe va effectuer le calcul des cotisations d'une assurance Remarque: Valable uniquement pour les assurances
 * paritaires Calcule le resultat d'une assurance avec les taux actifs à une date passée en paramètre
 * 
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFCalculAssurance {

    /**
     * Retourne le montant d'une cotisation
     * 
     * Remarque: il est nécessaire que la liste de taux soit triée pour qu'on aie le dernier taux actif en premier
     * 
     * @param session
     * @param dateFin
     * @param tauxList
     * @return montantMensuel de type double
     * @throws Exception
     */
    public static double calculMontant(String dateFin, AFTauxAssurance tauxAssurance, BSession session)
            throws Exception {

        double montantMensuel = 0.0;

        // Montant
        if (BSessionUtil.compareDateFirstLowerOrEqual(session, tauxAssurance.getDateDebut(), dateFin)) {

            // Un seul Montant pour la période
            if (tauxAssurance.getPeriodiciteMontant().equals(CodeSystem.PERIODICITE_ANNUELLE)) {
                montantMensuel = Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())) / 12;
            } else if (tauxAssurance.getPeriodiciteMontant().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                montantMensuel = Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())) / 3;
            } else if (tauxAssurance.getPeriodiciteMontant().equals(CodeSystem.PERIODICITE_MENSUELLE)) {
                montantMensuel = Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal()));
            }
        }

        return montantMensuel;
    }

    /**
     * Calcule le montant final de la cotisation
     * 
     * @param tauxAss
     * @param tauxman
     * @param masse
     * @param dateDebut
     * @param dateFin
     * @param session
     * @return montantCal de type FWCurrency
     * @throws Exception
     */
    private static FWCurrency calculMontantFinal(String dateDebut, String dateFin, AFTauxAssurance tauxAss,
            double masse, BSession session) throws Exception {

        FWCurrency montantCal = new FWCurrency(0);

        // if (tauxList.size() > 0) {
        if (tauxAss != null) {
            // On prend n'importe quel taux pour savoir de quel type il est
            // AFTauxAssurance tauxAss = (AFTauxAssurance) tauxList.get(0);
            // On regarde si c'est un taux fixe
            if (tauxAss.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_TAUX)) {
                // Si c'est le cas, recherche le dernier taux fixe
                // tauxAss = calculTauxFixe(tauxAss);
                // Et on calcule le montant avec la formule du taux fixe
                montantCal = new FWCurrency((masse * tauxAss.getTauxDouble()));

                // On regarde si c'est un taux variable
            } else if (tauxAss.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE)) {
                // Si c'est le cas, on prend le taux variable
                // double[] tauxFraction = calculTauxVariable(dateDebut,
                // dateFin,
                // tauxAss, masse, session);
                // Et on calcule le montant avec la formule du taux variable
                // montantCal = new FWCurrency((masse * tauxFraction[0])
                // / tauxFraction[1]);
                AFTauxVariableUtil tauxVarUtil = AFTauxVariableUtil.getInstance(tauxAss.getAssuranceId());
                AFApplication app = (AFApplication) GlobazServer.getCurrentSystem().getApplication(
                        AFApplication.DEFAULT_APPLICATION_NAOS);
                montantCal = new FWCurrency(tauxVarUtil.getMontantCotisation(String.valueOf(masse), dateFin, tauxAss,
                        app.isCotisationMinimale()));
                // On regarde si c'est un montant
            } else if (tauxAss.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_MONTANT)) {
                // Si c'est le cas, on regarde le nombre de mois contenu dans la
                // période à calculer
                int nbMois = AFUtil.nbMoisPeriode(session, dateDebut, dateFin);
                // On va recherche le montant avec le lequel on va calculer
                double montantMensuel = AFCalculAssurance.calculMontant(dateFin, tauxAss, session);
                // Et on calcule le montant final avec la formule du montant
                montantCal = new FWCurrency(montantMensuel * nbMois);
            }
        }
        return montantCal;
    }

    /**
     * Calcule le resultat final de l'assurance
     * 
     * @param idAssurance
     * @param session
     * @param dateDebut
     * @param dateFin
     * @param masse
     * @return montantCal de type double
     * @throws Exception
     * 
     */
    public static double calculResultatAssurance(String dateDebut, String dateFin, AFTauxAssurance taux, double masse,
            BSession session) throws Exception {

        FWCurrency montant = new FWCurrency(0);

        // Dans le cas où on n'a pas d'assurance de référence on appelle
        // simplement la méthode
        // qui permet de calculer le montant de la cotisation
        // On calcule le montant de l'assurance
        montant = AFCalculAssurance.calculMontantFinal(dateDebut, dateFin, taux, masse, session);

        // Dans le cas où on possède une assurance de référence, on va calculer
        // le montant de
        // l'assurance de référence afin d'obtenir la masse sur laquelle on doit
        // calculer notre assurance

        return montant.doubleValue();
    }

    /**
     * Met à jour taux moyen seulement pour les lignes
     * qui respecte les critères passés en paramètres
     * spécifique à la FERCIAM
     *
     * @param session
     * @param affiliationId
     * @param assuranceId
     * @param typeAssurance
     * @param assuranceGenre
     * @param tauxGenre
     * @param masse
     * @param annee
     * @return le taux moyen ou null
     * @throws Exception
     *
     */
    public static String updateTauxMoyen(BSession session, String affiliationId, String assuranceId, String typeAssurance, String assuranceGenre, String tauxGenre, String masse, String annee, String typeDocument) throws Exception {
        if (CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE.equals(typeDocument)
                || (CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equals(typeDocument)
                || DSDeclarationViewBean.CS_PRINCIPALE.equals(typeDocument)
                && "true".equals(session.getApplication().getProperty(AFApplication.PROPERTY_IS_TAUX_PAR_PALIER, "false"))
                && CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equals(tauxGenre)
                && CodeSystem.TYPE_ASS_FRAIS_ADMIN.equals(typeAssurance) && CodeSystem.GENRE_ASS_PARITAIRE.equals(assuranceGenre))) {
            return AFCalculAssurance.calculTauxMoyen((BSession) DSProcessValidation.getSessionNaos(session)
                    , affiliationId
                    , assuranceId
                    , masse
                    , annee);
        }
        return null;
    }

    /**
     * Calcul du taux moyen appelé lors de la validation des décomptes 13 et 14
     * 
     * @param session
     *            session NOAS connectée
     * @param affiliationId
     *            id de l'employeur concerné
     * @param assuranceId
     *            id de l'assurance facturée
     * @param masse
     *            masse salariale saisie dans la déclaration de l'assurance donnée au format xxxxxxxx.xx
     * @param annee
     *            année de la déclaration au format aaaa
     * @return la valeur du taux moyen calculé ou null pas applicable (pas de taux variable pour l'assurance donnée)
     * @throws Exception
     *             si une erreur survient lors du calcul
     */
    public static String calculTauxMoyen(BSession session, String affiliationId, String assuranceId, String masse,
            String annee) throws Exception {
        if ((session == null) || JadeStringUtil.isIntegerEmpty(affiliationId) || JadeStringUtil.isEmpty(masse)
                || JadeStringUtil.isIntegerEmpty(annee)) {
            // pré-condition non remplie
            throw new Exception("calculTauxMoyen: pré-conditions non respectées");
        }
        // si assurance ne contient pas de taux variables, ne rien faire
        AFTauxAssuranceManager manager = new AFTauxAssuranceManager();
        manager.setSession(session);
        manager.setForGenreValeur(CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE);
        manager.setForIdAssurance(assuranceId);
        manager.find();
        if (manager.size() == 0) {
            // taux variable n'existent pas, ne rien faire
            return null;
        }
        // calcul du nombre de mois facturé (radiation ou création en cours
        // d'année
        String dateDebut = "01.01." + annee;
        String dateFin = "31.12." + annee;
        // les date d'affiliation sont utilisées
        AFAffiliation aff = new AFAffiliation();
        aff.setSession(session);
        aff.setAffiliationId(affiliationId);
        aff.retrieve();
        if (!aff.isNew()) {
            // calcul de la date de début
            if (BSessionUtil.compareDateFirstGreater(session, aff.getDateDebut(), dateDebut)) {
                // affiliation en cours d'année
                dateDebut = aff.getDateDebut();
            }
            // calcul de la date de fin
            if (!JadeStringUtil.isIntegerEmpty(aff.getDateFin())
                    && BSessionUtil.compareDateFirstLower(session, aff.getDateFin(), dateFin)) {
                // affiliation en cours d'année
                dateFin = aff.getDateFin();
            }
        } else {
            throw new Exception("calculTauxMoyen: Affiliation non trouvée");
        }
        int nbMoisFacturer = AFUtil.nbMoisPeriode(session, dateDebut, dateFin);
        // recherche si ajout ou mise à jour
        AFTauxMoyenManager mgr = new AFTauxMoyenManager();
        mgr.setSession(session);
        mgr.setForAnnee(annee);
        mgr.setForIdAffiliation(affiliationId);
        mgr.setForIdAssurance(assuranceId);
        mgr.find();
        AFTauxMoyen tauxMoyen = null;
        if (mgr.size() != 0) {
            // un taux existe pour cette année. Mise à jour du taux existant
            tauxMoyen = (AFTauxMoyen) mgr.getFirstEntity();
            // effacer la valeur du taux pour recalcul si non bloqué
            if (!tauxMoyen.getBlocage().booleanValue()) {
                tauxMoyen.setTauxMoyen("");
            }
        } else {
            // aucun taux, création
            tauxMoyen = new AFTauxMoyen();
            tauxMoyen.setSession(session);
            tauxMoyen.setAffiliationId(affiliationId);
            tauxMoyen.setAnnee(annee);
            tauxMoyen.setAssuranceId(assuranceId);
        }
        tauxMoyen.setMasseAnnuelle(masse);
        tauxMoyen.setNbrMois(String.valueOf(nbMoisFacturer));
        tauxMoyen.save();
        return tauxMoyen.getTauxTotal();
    }
}
