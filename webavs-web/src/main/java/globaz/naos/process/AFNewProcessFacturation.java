package globaz.naos.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.util.FAUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.processFacturation.AFProcessFacturationManager;
import globaz.naos.db.processFacturation.AFProcessFacturationViewBean;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.db.tauxAssurance.AFTauxVariableUtil;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFAgeRente;
import globaz.naos.util.AFUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CACompteur;
import globaz.pyxis.util.TISQL;
import globaz.pyxis.util.TIToolBox;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;

/**
 * Process pour la facturation des Cotisations Personnelles, Paritaires
 * 
 * @author: mmu, sau
 */

public final class AFNewProcessFacturation extends BProcess {

    private static final long serialVersionUID = -3142573899646701295L;

    /**
     * Class utilisée pour enregistrer les valeurs de facturation avant insertion dans la DB.
     * 
     * @author mmu, sau
     */
    public static class LineFacturation {

        private String anneeCotisation = null;
        private String assuranceId = null;
        private String caisse = null;
        private String debutPeriode = null;
        private String finPeriode = null;
        private String idRubrique = null;
        // taux caché
        private boolean isTauxCache = false;
        private String libelle = null;
        private double masse = 0.0;
        private double montant = 0.0;
        private double taux = 0.0;

        /**
         * Retourne un nouvelle enregistrement de facturation initialisé avec les paramètres d'entrées.
         * 
         * @param donneesFacturation
         * @param anneeFacturation
         * @param debutPeriode
         *            DD.MM.YYYY
         * @param finPeriode
         *            DD.MM.YYYY
         * @param masse
         * @param montant
         * @param taux
         * @return Un nouvelle enregistrement
         */
        public LineFacturation(AFProcessFacturationViewBean donneesFacturation, String anneeFacturation,
                String debutPeriode, String finPeriode, double masse, double montant, double taux) {
            setDebutPeriode(debutPeriode);
            setFinPeriode(finPeriode);
            setMasse(masse);
            setMontant(montant);
            setTaux(taux);

            if (donneesFacturation.getNatureRubrique().equals(CodeSystem.NAT_RUBRIQUE_COTI_AVEC_MASSE)
                    || donneesFacturation.getNatureRubrique().equals(CodeSystem.NAT_RUBRIQUE_COTI_SANS_MASSE)) {

                setAnneeCotisation(anneeFacturation);
            } else {
                setAnneeCotisation("");
            }

            String langue = donneesFacturation.getLangue();
            if (langue.equalsIgnoreCase(CodeSystem.LANGUE_FRANCAIS)) {
                setLibelle(donneesFacturation.getAssuranceLibelleFr());
            } else if (langue.equalsIgnoreCase(CodeSystem.LANGUE_ALLEMAND)) {
                setLibelle(donneesFacturation.getAssuranceLibelleAl());
            } else if (langue.equalsIgnoreCase(CodeSystem.LANGUE_ITALIEN)) {
                setLibelle(donneesFacturation.getAssuranceLibelleIt());
            }
            setAssuranceId(donneesFacturation.getAssuranceId());
            setIdRubrique(donneesFacturation.getAssuranceRubriqueId());

            // caisse
            // si une caisse principale existe, utiliser la gestion
            // multi-caisse: prendre la caisse liée à la cotisation ou
            // celle de la caisse principale si vide
            // si la caisse principale n'existe pas, on ne renseigne rien (sans
            // gestion caisse)
            if (!JadeStringUtil.isIntegerEmpty(donneesFacturation.getIdCaissePrincipale())) {
                if (JadeStringUtil.isIntegerEmpty(donneesFacturation.getIdCaisseAdhesion())) {
                    // aucune adhésion, utiliser la caisse principale
                    setCaisse(donneesFacturation.getIdCaissePrincipale());
                } else {
                    setCaisse(donneesFacturation.getIdCaisseAdhesion());
                }
            }
        }

        public String getAnneeCotisation() {
            return anneeCotisation;
        }

        public String getAssuranceId() {
            return assuranceId;
        }

        public String getCaisse() {
            return caisse;
        }

        public String getDebutPeriode() {
            return debutPeriode;
        }

        public String getFinPeriode() {
            return finPeriode;
        }

        public String getIdRubrique() {
            return idRubrique;
        }

        public String getLibelle() {
            return libelle;
        }

        public double getMasse() {
            return masse;
        }

        public double getMontant() {
            return montant;
        }

        public double getTaux() {
            return taux;
        }

        public boolean isTauxCache() {
            return isTauxCache;
        }

        public void setAnneeCotisation(String string) {
            anneeCotisation = string;
        }

        public void setAssuranceId(String string) {
            assuranceId = string;
        }

        public void setCaisse(String string) {
            caisse = string;
        }

        public void setDebutPeriode(String string) {
            debutPeriode = string;
        }

        public void setFinPeriode(String string) {
            finPeriode = string;
        }

        public void setIdRubrique(String string) {
            idRubrique = string;
        }

        public void setLibelle(String string) {
            libelle = string;
        }

        public void setMasse(double d) {
            masse = d;
        }

        public void setMontant(double d) {
            montant = d;
        }

        public void setTaux(double d) {
            taux = d;
        }

        public void setTauxCache(boolean isTauxCache) {
            this.isTauxCache = isTauxCache;
        }

    }

    public static CACompteAnnexe _compteAnnexe = null;
    private static HashMap<String, AFAgeRente> listRente = new HashMap<String, AFAgeRente>();
    public final static int PERIODIQUE_COT_PERS = 0;
    public final static int PERIODIQUE_COT_PERS_IND = 1;
    public final static int PERIODIQUE_COT_PERS_NAC = 2;
    private static Boolean ffppDejaFacturee = false;
    private static Boolean ffppNegativeDejaFacturee = false;

    /**
     * Calcul de la masse MENSUELLE de la cotisation de l'assurance de Référence.
     * 
     * @param donneesFacturation
     * @param dateEffectiveDebutFacturation
     * @param dateEffectiveFinFacturation
     * @return
     * @throws Exception
     */
    private static double calculeMasseAssuranceReference(BProcess process,
            AFProcessFacturationViewBean donneesFacturation, String dateEffectiveDebutFacturation,
            String dateEffectiveFinFacturation, String idReference) throws Exception {

        FWCurrency sommeMasse = new FWCurrency(0.0);
        String masseAnnuel = donneesFacturation.getMasseAnnuelleCoti();
        double masseMensuel = Double.parseDouble(masseAnnuel) / 12;

        int nbMoisFacturer = AFUtil.nbMoisPeriode(process.getSession(), dateEffectiveDebutFacturation,
                dateEffectiveFinFacturation);

        AFPlanAffiliation planAffiliation = new AFPlanAffiliation();
        planAffiliation.setAffiliationId(donneesFacturation.getAffiliationId());
        planAffiliation.retrieve(process.getTransaction());

        // Recherche des taux par l'intermédiaire de la cotisation
        AFCotisation cotisation = new AFCotisation();
        cotisation.setPlanAffiliationId(planAffiliation.getPlanAffiliationId());
        cotisation.setAssuranceId(idReference);
        cotisation.retrieve(process.getTransaction());
        AFTauxAssurance tauxAssurance = cotisation.findTaux(dateEffectiveFinFacturation, cotisation.getMasseAnnuelle(),
                true);

        if (tauxAssurance != null) {

            // *****************************************************
            // Taux Fixe
            // *****************************************************
            if (tauxAssurance.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_TAUX)) {

                if (nbMoisFacturer > 0) {

                    double montant = masseMensuel * nbMoisFacturer
                            * Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal()))
                            / Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getFraction()));

                    sommeMasse.add(montant);

                }

                // *****************************************************
                // Montant
                // *****************************************************
            } else if (tauxAssurance.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_MONTANT)) {

                if (nbMoisFacturer > 0) {

                    double montantMensuel = 0.0;

                    if (tauxAssurance.getPeriodiciteMontant().equals(CodeSystem.PERIODICITE_ANNUELLE)) {
                        montantMensuel = Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())) / 12;
                    } else if (tauxAssurance.getPeriodiciteMontant().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                        montantMensuel = Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())) / 3;
                    } else if (tauxAssurance.getPeriodiciteMontant().equals(CodeSystem.PERIODICITE_MENSUELLE)) {
                        montantMensuel = Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal()));
                    }

                    // Un seul Montant pour la période de Facturation.
                    double montant = nbMoisFacturer * montantMensuel;

                    sommeMasse.add(montant);
                }
            }
        } else {
            // Il n' y a pas de taux
            // Si pour l'assurance, le type de calcul est "Cotisation"
            // Le montant de la cotisation est égal à la masse de la cotisation
            // au
            // "Prorata Temporis".
            if (donneesFacturation.getTypeCalcul().equals(CodeSystem.TYPE_CALCUL_COTISATION)) {
                double montant = Double.parseDouble(masseAnnuel) / 12 * nbMoisFacturer;

                sommeMasse.add(montant);
            } else {
                process.getTransaction().addErrors(
                        FWMessageFormat.format(process.getSession().getLabel("1830"),
                                donneesFacturation.getCotisationId(), donneesFacturation.getAssuranceId()));
            }
        }

        if (nbMoisFacturer > 0) {
            return sommeMasse.doubleValue() / nbMoisFacturer;
        } else {
            return 0.0;
        }
    }

    /**
     * Calcul une cotisation à partir d'une ligne du AFProcessFactureManager <br>
     * 
     * @param process
     *            process appellant la méthode: process naos ou musca
     * @param donneeFacturation
     *            entity retourné par AFProcessFacturationViewBean
     * @param anneeFacturation
     * @param moisFacturation
     * @param isParitaire
     *            true is on veut calculer une paritaire
     * @param isPersonel
     *            true is on veut calculer une Personelle
     * @param cotisationAffilie
     *            liste de cotisation déjà calculées pour un affilié
     */
    public static final LineFacturation calculerCotisation(BProcess process,
            AFProcessFacturationViewBean donneesFacturation, String anneeFacturation, String moisFacturation,
            boolean isParitaire, boolean isPersonnel, boolean isRI_PC, boolean isLAE, String debutPeriodeFacturation,
            String finPeriodeFacturation, AFAgeRente ageRente, String roleAffilie, String idPassage) throws Exception {

        String dateEffectiveDebutFacturation = null;
        String dateEffectiveFinFacturation = null;
        String dateDebutCotisation = null;
        String dateFinCotisation = null;
        String dateRetraite = null;
        String dateDeces = null;
        LineFacturation lineFacturation = null;

        BSession sessionNaos = null;

        String applicationId = process.getSession().getApplicationId();

        if ("MUSCA".equalsIgnoreCase(applicationId)) {
            FAApplication applicationMusca = (FAApplication) process.getSession().getApplication();
            sessionNaos = (BSession) applicationMusca.getSessionNaos(process.getSession());
        } else {
            sessionNaos = process.getSession();
        }

        // ************************************************************
        // Calcul de la periode exacte de facturation
        // ************************************************************
        // Date début : le plus grand entre début Facturation, début Cotisation
        // Date fin : le plus petit entre fin Facturation, fin Cotisation,
        // Retraite, Décés

        // Date de début
        dateEffectiveDebutFacturation = debutPeriodeFacturation;

        dateDebutCotisation = AFUtil.getDateBeginingOfMonth(donneesFacturation.getDateDebutCoti());
        if (BSessionUtil.compareDateFirstGreater(sessionNaos, dateDebutCotisation, dateEffectiveDebutFacturation)) {
            dateEffectiveDebutFacturation = dateDebutCotisation;
        }

        // Date de Fin
        dateEffectiveFinFacturation = finPeriodeFacturation;

        dateFinCotisation = AFUtil.getDateEndOfMonth(donneesFacturation.getDateFinCoti());
        dateRetraite = ageRente.getDateRente(donneesFacturation.getDateNaissance(), donneesFacturation.getSexe());
        dateDeces = AFUtil.getDateEndOfMonth(donneesFacturation.getDateDeces());

        if ((!JadeStringUtil.isIntegerEmpty(dateFinCotisation))
                && BSessionUtil.compareDateFirstLower(sessionNaos, dateFinCotisation, dateEffectiveFinFacturation)) {

            dateEffectiveFinFacturation = dateFinCotisation;
        }
        if ((!JadeStringUtil.isIntegerEmpty(dateRetraite))
                && BSessionUtil.compareDateFirstLowerOrEqual(sessionNaos, dateRetraite, dateEffectiveFinFacturation)) {

            if (CodeSystem.TYPE_AFFILI_NON_ACTIF.equals(donneesFacturation.getTypeAffiliation())) {
                // if (donneesFacturation.getTypeAffiliation().equals("804004"))
                // {
                dateEffectiveFinFacturation = dateRetraite;
                // l'indiquer dans les données de facturation afin de renseigne
                // le décompte
                donneesFacturation.setIsRentier(new Boolean("true"));
            } else {
                // si AC ou AC2 IND, on ne facture que avant age rente
                if ((CodeSystem.TYPE_ASS_COTISATION_AC.equals(donneesFacturation.getAssurance().getTypeAssurance()) || CodeSystem.TYPE_ASS_COTISATION_AC2
                        .equals(donneesFacturation.getAssurance().getTypeAssurance()))
                        && CodeSystem.GENRE_ASS_PERSONNEL.equals(donneesFacturation.getAssurance().getAssuranceGenre())) {
                    dateEffectiveFinFacturation = dateRetraite;
                }
            }

        }
        if ((!JadeStringUtil.isIntegerEmpty(dateDeces))
                && BSessionUtil.compareDateFirstLower(sessionNaos, dateDeces, dateEffectiveFinFacturation)) {

            dateEffectiveFinFacturation = dateDeces;
        }
        // ************************************************************
        // Calcul le nombre de mois a facturer
        // ************************************************************
        int nbMoisFacturer = AFUtil.nbMoisPeriode(sessionNaos, dateEffectiveDebutFacturation,
                dateEffectiveFinFacturation);

        // ************************************************************
        // Calculs des montants a facturer
        // ************************************************************

        /**
         * Il est indispensable de laisser les tests avec "isPersonnel" / "isRI_PC" / "isParitaire" (ces flags sont
         * settés par le module de facturation)
         * 
         * En effet, pour le type RI_PC, le manager filtre les AFFILIATIONS mais pas les COTISATIONS (cotisations
         * personnelles)
         * 
         * Par conséquent, ces flags sont indispensables pour distinguer le cas personnel et le cas RI_PC ci-dessous
         */

        // *******************************
        // PERSONNEL ou RI_PC
        // *******************************
        double montant = 0.0;
        if (AFNewProcessFacturation.isCotisationPersonnel(donneesFacturation) && (isPersonnel || isRI_PC)) {
            lineFacturation = AFNewProcessFacturation.calculerCotisationPersonnelle(donneesFacturation,
                    anneeFacturation, sessionNaos, dateEffectiveDebutFacturation, dateEffectiveFinFacturation,
                    nbMoisFacturer, roleAffilie);
        }
        // *******************************
        // LAE
        // *******************************
        else if (AFNewProcessFacturation.isCotisationParitaire(donneesFacturation) && isLAE) {

            if (CodeSystem.TYPE_ASS_LAE.equals(donneesFacturation.getTypeAssurance())) {
                lineFacturation = AFNewProcessFacturation.facturationLAE(process, donneesFacturation, anneeFacturation,
                        moisFacturation, sessionNaos, dateEffectiveDebutFacturation, dateEffectiveFinFacturation,
                        lineFacturation, roleAffilie, idPassage);
            }
        }
        // *******************************
        // PARITAIRE
        // *******************************
        else if (AFNewProcessFacturation.isCotisationParitaire(donneesFacturation) && isParitaire) {
            lineFacturation = AFNewProcessFacturation.facturationParitaire(process, donneesFacturation,
                    anneeFacturation, dateEffectiveDebutFacturation, dateEffectiveFinFacturation, lineFacturation,
                    sessionNaos, nbMoisFacturer);

        } else {
            montant = 0.0;
            if (AFNewProcessFacturation.isCotisationParitaire(donneesFacturation)) {
                process.getMemoryLog().logMessage(
                        FWMessageFormat.format(sessionNaos.getLabel("1810"), donneesFacturation.getCotisationId(),
                                donneesFacturation.getTypeAffiliation()), FWMessage.AVERTISSEMENT,
                        AFNewProcessFacturation.class.getName());
            } else if (AFNewProcessFacturation.isCotisationPersonnel(donneesFacturation)) {
                process.getMemoryLog().logMessage(
                        FWMessageFormat.format(sessionNaos.getLabel("1820"), donneesFacturation.getCotisationId(),
                                donneesFacturation.getTypeAffiliation()), FWMessage.AVERTISSEMENT,
                        AFNewProcessFacturation.class.getName());
            }
        }

        if (CodeSystem.TYPE_ASS_MANUELLE.equalsIgnoreCase(donneesFacturation.getTypeAssurance())
                && (!CodeSystem.CODE_FACTU_MONTANT_LIBRE.equalsIgnoreCase(donneesFacturation.getCodeFacturation()) || CodeSystem.GENRE_ASS_PERSONNEL
                        .equalsIgnoreCase(donneesFacturation.getGenreAssurance()))) {
            if (CodeSystem.PERIODICITE_ANNUELLE.equalsIgnoreCase(donneesFacturation.getPeriodiciteCoti())) {
                if (CodeSystem.GENRE_ASS_PARITAIRE.equalsIgnoreCase(donneesFacturation.getGenreAssurance())) {
                    montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMasseAnnuelleCoti()));
                } else {
                    montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMontantAnnuelCoti()));
                }

            } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equalsIgnoreCase(donneesFacturation.getPeriodiciteCoti())) {
                if (CodeSystem.GENRE_ASS_PARITAIRE.equalsIgnoreCase(donneesFacturation.getGenreAssurance())) {
                    montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMasseAnnuelleCoti())) / 4;
                } else {
                    montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation
                            .getMontantTrimestrielCoti()));
                }

            } else {
                if (CodeSystem.GENRE_ASS_PARITAIRE.equalsIgnoreCase(donneesFacturation.getGenreAssurance())) {
                    montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMasseAnnuelleCoti())) / 12;
                } else {
                    montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMontantMensuelCoti()));
                }
            }
            if (montant != 0.0) {
                lineFacturation = new LineFacturation(donneesFacturation, anneeFacturation,
                        dateEffectiveDebutFacturation, dateEffectiveFinFacturation, 0.0, montant, 0.0);
            }

        }

        return lineFacturation;
    }

    protected static LineFacturation calculerCotisationMontantFixe(AFProcessFacturationViewBean donneesFacturation,
            String anneeFacturation, String dateEffectiveDebutFacturation, String dateEffectiveFinFacturation,
            int nbMoisFacturer, AFTauxAssurance tauxAssurance) throws NumberFormatException {
        double montant;
        if (nbMoisFacturer > 0) {

            double montantMensuel = 0.0;

            if (tauxAssurance.getPeriodiciteMontant().equals(CodeSystem.PERIODICITE_ANNUELLE)) {
                montantMensuel = Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())) / 12;
            } else if (tauxAssurance.getPeriodiciteMontant().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                montantMensuel = Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())) / 3;
            } else if (tauxAssurance.getPeriodiciteMontant().equals(CodeSystem.PERIODICITE_MENSUELLE)) {
                montantMensuel = Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal()));
            }

            // Un seul Montant pour la période de Facturation.
            montant = nbMoisFacturer * montantMensuel;

            return new LineFacturation(donneesFacturation, anneeFacturation, dateEffectiveDebutFacturation,
                    dateEffectiveFinFacturation, 0.0, montant, 0.0);

        }
        return null;
    }

    protected static LineFacturation calculerCotisationPersonnelle(AFProcessFacturationViewBean donneesFacturation,
            String anneeFacturation, BSession sessionNaos, String dateEffectiveDebutFacturation,
            String dateEffectiveFinFacturation, int nbMoisFacturer, String roleAffilie) throws NumberFormatException,
            Exception {
        double montant;

        if (nbMoisFacturer == 0) {
            montant = 0.0;
        } else {
            if (nbMoisFacturer == 12) {

                montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMontantAnnuelCoti()));

            } else if (nbMoisFacturer == 3) {
                montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMontantTrimestrielCoti()));
            } else {
                montant = nbMoisFacturer
                        * Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMontantMensuelCoti()));
            }
            // Si périodicité annuelle faire une soustraction du montant
            // déjà payer

            if (donneesFacturation.getPeriodiciteAff().equalsIgnoreCase(CodeSystem.PERIODICITE_ANNUELLE)) {
                CACompteur cnt = AFNewProcessFacturation.getCompteur(donneesFacturation.getAssuranceRubriqueId(),
                        donneesFacturation.getAffilieNumero(), anneeFacturation, sessionNaos, roleAffilie);
                FWCurrency montantCompta = new FWCurrency(montant);
                if (cnt != null) {

                    montantCompta.sub(cnt.getCumulCotisation());

                }
                if (montantCompta.isNegative() == false) {
                    montant = montantCompta.doubleValue();
                }
            }
            if (montant > 0.0) {
                return new LineFacturation(donneesFacturation, anneeFacturation, dateEffectiveDebutFacturation,
                        dateEffectiveFinFacturation, 0.0, montant, 0.0);
            }
        }
        return null;
    }

    protected static LineFacturation calculerCotisationTauxFixe(AFProcessFacturationViewBean donneesFacturation,
            String anneeFacturation, String dateEffectiveDebutFacturation, String dateEffectiveFinFacturation,
            int nbMoisFacturer, double masseMensuel, AFTauxAssurance tauxAssurance) throws NumberFormatException {
        double montant;
        if (nbMoisFacturer > 0) {
            // Un seul Taux pour la periode de Facturation
            double tauxCalcul = Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal()))
                    / Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getFraction()));
            montant = masseMensuel * nbMoisFacturer * tauxCalcul;
            if (tauxCalcul != 1) {
                // taux différent de 100%

                LineFacturation lineFactu = new LineFacturation(donneesFacturation, anneeFacturation,
                        dateEffectiveDebutFacturation, dateEffectiveFinFacturation, masseMensuel * nbMoisFacturer,
                        montant, Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())));
                // taux à cacher?
                if (!tauxAssurance.isAffichageTaux()) {
                    lineFactu.setTauxCache(true);
                }
                return lineFactu;
            } else {
                // taux à 100%
                return new LineFacturation(donneesFacturation, anneeFacturation, dateEffectiveDebutFacturation,
                        dateEffectiveFinFacturation, 0.0, montant, 0.0);
            }
        }
        return null;
    }

    protected static LineFacturation calculerCotisationTauxVariable(BProcess process,
            AFProcessFacturationViewBean donneesFacturation, String anneeFacturation,
            String dateEffectiveDebutFacturation, String dateEffectiveFinFacturation, int nbMoisFacturer,
            double masseMensuel, AFTauxAssurance tauxAssurance) throws Exception, NumberFormatException {
        double montant;
        if (nbMoisFacturer > 0) {

            // chargement de l'utilitaire de calcul
            AFTauxVariableUtil tauxVarUtil = AFTauxVariableUtil.getInstance(donneesFacturation.getAssuranceId());
            AFApplication app = (AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS);

            if ("true".equals(process.getSession().getApplication().getProperty("factuCoti"))) {

                double masseMensuelnew = masseMensuel * 12;

                String masseString = Double.toString(masseMensuelnew);

                String montantAnnuel = tauxVarUtil.getMontantCotisation(masseString, dateEffectiveFinFacturation,
                        tauxAssurance, app.isCotisationMinimale());

                montant = Double.parseDouble(JANumberFormatter.deQuote(montantAnnuel)) / 12 * nbMoisFacturer;
                LineFacturation lineFactu = new LineFacturation(donneesFacturation, anneeFacturation,
                        dateEffectiveDebutFacturation, dateEffectiveFinFacturation, masseMensuel * nbMoisFacturer,
                        montant, Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())));
                if (!tauxAssurance.isAffichageTaux()) {
                    lineFactu.setTauxCache(true);
                }
                return lineFactu;

            } else {
                String montantAnnuel = tauxVarUtil.getMontantCotisation(donneesFacturation.getMasseAnnuelleCoti(),
                        dateEffectiveFinFacturation, tauxAssurance, app.isCotisationMinimale());

                montant = Double.parseDouble(JANumberFormatter.deQuote(montantAnnuel)) / 12 * nbMoisFacturer;
                LineFacturation lineFactu = new LineFacturation(donneesFacturation, anneeFacturation,
                        dateEffectiveDebutFacturation, dateEffectiveFinFacturation, masseMensuel * nbMoisFacturer,
                        montant, Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())));
                if (!tauxAssurance.isAffichageTaux()) {
                    lineFactu.setTauxCache(true);
                }
                return lineFactu;
            }

        }
        return null;
    }

    protected static LineFacturation calculerCotsationFFPP(BProcess process,
            AFProcessFacturationViewBean donneesFacturation, String anneeFacturation, BSession sessionNaos,
            AFTauxAssurance tauxAssurance) throws Exception, NumberFormatException {
        double montant;

        if (AFParticulariteAffiliation.existeParticularite(process.getSession(), donneesFacturation.getAffiliationId(),
                CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL, JACalendar.todayJJsMMsAAAA())) {
            // l'affilié est sans personnel -> ne pas facturer
            process.getMemoryLog().logMessage(
                    FWMessageFormat.format((sessionNaos).getLabel("2010"), donneesFacturation.getCotisationId(),
                            donneesFacturation.getAssuranceId()), FWMessage.AVERTISSEMENT,
                    AFNewProcessFacturation.class.getName());
        } else {
            // recherche du nombre d'assuré
            String nbrAssures = donneesFacturation.getNbrAssures(anneeFacturation);
            if (nbrAssures != null) {
                AFTauxAssurance taux = tauxAssurance;
                if ((taux == null) || taux.isNew()) {
                    process.getMemoryLog().logMessage(sessionNaos.getLabel("2020"), FWMessage.AVERTISSEMENT,
                            AFNewProcessFacturation.class.getName());
                } else {

                    montant = Integer.parseInt(JANumberFormatter.deQuote(taux.getValeurEmployeur()))
                            * Double.parseDouble(JANumberFormatter.deQuote(nbrAssures));

                    if ((ffppDejaFacturee == false && montant >= 0)
                            || (ffppNegativeDejaFacturee == false && montant < 0)) {
                        // création de la ligne
                        LineFacturation lineFFPP = new LineFacturation(donneesFacturation, anneeFacturation, "", "",
                                0.0, montant, 0.0);
                        // modifier le libellé par défaut
                        String langue = donneesFacturation.getLangue();
                        if (langue.equalsIgnoreCase(CodeSystem.LANGUE_FRANCAIS)) {
                            lineFFPP.setLibelle(donneesFacturation.getAssuranceLibelleFr() + " " + nbrAssures + " x "
                                    + taux.getValeurEmployeur() + ".-");
                        } else if (langue.equalsIgnoreCase(CodeSystem.LANGUE_ALLEMAND)) {
                            lineFFPP.setLibelle(donneesFacturation.getAssuranceLibelleAl() + " " + nbrAssures + " x "
                                    + taux.getValeurEmployeur() + ".-");
                        } else if (langue.equalsIgnoreCase(CodeSystem.LANGUE_ITALIEN)) {
                            lineFFPP.setLibelle(donneesFacturation.getAssuranceLibelleIt() + " " + nbrAssures + " x "
                                    + taux.getValeurEmployeur() + ".-");
                        }

                        if (ffppDejaFacturee == false && montant >= 0) {
                            ffppDejaFacturee = true;
                        }
                        if (ffppNegativeDejaFacturee == false && montant < 0) {
                            ffppNegativeDejaFacturee = true;
                        }

                        return lineFFPP;
                    }
                }
            } else {
                // l'affiliation est bien une FFPP mais le nombre
                // d'assuré n'est pas renseigné
                process.getMemoryLog().logMessage(
                        FWMessageFormat.format((sessionNaos).getLabel("2010"), donneesFacturation.getCotisationId(),
                                donneesFacturation.getAssuranceId()), FWMessage.AVERTISSEMENT,
                        AFNewProcessFacturation.class.getName());
            }
        }

        return null;
    }

    protected static String determinerNumeroTrimestre(AFProcessFacturationViewBean donneesFacturation,
            String moisFacturation, String periodiciteAffilie) {
        String noTrimestre = null;
        // Déterminnation du n° de trimestre pour l'idexternefacture
        if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equalsIgnoreCase(periodiciteAffilie)) {
            if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.MARS_TRIMESTRE_1)) {
                noTrimestre = "1";
            } else if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.JUIN_TRIMESTRE_2)) {
                noTrimestre = "2";
            } else if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.SEPTEMBRE_TRIMESTRE_3)) {
                noTrimestre = "3";
            } else if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.ANNUEL)) {
                noTrimestre = "4";
            } else {
                noTrimestre = "0";
            }
        } else if (CodeSystem.PERIODICITE_ANNUELLE.equalsIgnoreCase(periodiciteAffilie)) {
            // Code de facturation
            if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.MARS_TRIMESTRE_1)) {
                noTrimestre = "5";
            } else if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.JUIN_TRIMESTRE_2)) {
                noTrimestre = "6";
            } else if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.SEPTEMBRE_TRIMESTRE_3)) {
                noTrimestre = "7";
            } else {
                noTrimestre = "0";
            }
        }
        return noTrimestre;
    }

    protected static LineFacturation facturationLAE(BProcess process, AFProcessFacturationViewBean donneesFacturation,
            String anneeFacturation, String moisFacturation, BSession sessionNaos,
            String dateEffectiveDebutFacturation, String dateEffectiveFinFacturation, LineFacturation lineFacturation,
            String roleAffilie, String idPassage) throws Exception, NumberFormatException {
        double montant;
        // Récupération du cumul des 5 premiers mois
        CACompteur cnt = AFNewProcessFacturation.getCompteur("151", donneesFacturation.getAffilieNumero(),
                anneeFacturation, sessionNaos, roleAffilie);
        FWCurrency montantCompta = new FWCurrency();
        if (cnt != null) {
            montantCompta.add(cnt.getCumulMasse());
        }
        // Recherche afact avec masse en cours pour l'additionner à la masse déjà facturée
        FAAfactManager afac = new FAAfactManager();
        afac.setForIdTiers(donneesFacturation.getIdTiers());
        afac.setForAnneeCotisation(anneeFacturation);
        afac.setForIdRubrique("151");
        afac.setForIdPassage(idPassage);
        afac.setSession(sessionNaos);
        afac.find(BManager.SIZE_USEDEFAULT);
        if (afac.getSize() > 0) {

            FAAfact fa = (FAAfact) afac.getFirstEntity();
            montantCompta.add(fa.getMasseFacture());
        }
        // Recherche des taux par l'intermédiaire de la cotisation
        AFCotisation cotisation = new AFCotisation();
        cotisation.setCotisationId(donneesFacturation.getCotisationId());
        cotisation.retrieve(process.getTransaction());
        AFTauxAssurance tauxAssurance = cotisation.findTaux(dateEffectiveFinFacturation,
                donneesFacturation.getMasseAnnuelleCoti(), true);
        double tauxCalcul = Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal()))
                / Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getFraction()));
        montant = montantCompta.doubleValue() * tauxCalcul;
        if (tauxCalcul != 1) {
            // taux différent de 100%
            lineFacturation = new LineFacturation(donneesFacturation, anneeFacturation, dateEffectiveDebutFacturation,
                    dateEffectiveFinFacturation, montantCompta.doubleValue(), montant,
                    Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())));
        }

        return lineFacturation;
    }

    protected static LineFacturation facturationParitaire(BProcess process,
            AFProcessFacturationViewBean donneesFacturation, String anneeFacturation,
            String dateEffectiveDebutFacturation, String dateEffectiveFinFacturation, LineFacturation lineFacturation,
            BSession sessionNaos, int nbMoisFacturer) throws NumberFormatException, Exception {
        double montant;
        double masseMensuel = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMasseAnnuelleCoti())) / 12;

        if (!JadeStringUtil.isIntegerEmpty(donneesFacturation.getIdReferenceAssurance())) {

            masseMensuel = AFNewProcessFacturation.calculeMasseAssuranceReference(process, donneesFacturation,
                    dateEffectiveDebutFacturation, dateEffectiveFinFacturation,
                    donneesFacturation.getIdReferenceAssurance());

        }
        // Recherche des taux par l'intermédiaire de la cotisation
        AFCotisation cotisation = new AFCotisation();
        cotisation.setCotisationId(donneesFacturation.getCotisationId());
        cotisation.retrieve(process.getTransaction());
        AFTauxAssurance tauxAssurance = cotisation.findTaux(dateEffectiveFinFacturation,
                donneesFacturation.getMasseAnnuelleCoti(), true);

        if (tauxAssurance != null) {
            // *****************************************************
            // Cas particuliers pour les assurances qui se calculent
            // autrement que taux*masse
            //
            // *****************************************************
            // Assurance FFPP paritaire
            // le montant pour un affilié est calculé ainsi: (nb d'employé
            // qui cotisent)*(montant de la cotisation)
            if (CodeSystem.TYPE_ASS_FFPP.equals(donneesFacturation.getTypeAssurance())) {
                // test si avec personnel
                lineFacturation = AFNewProcessFacturation.calculerCotsationFFPP(process, donneesFacturation,
                        anneeFacturation, sessionNaos, tauxAssurance);

                // *****************************************************
                // Taux Fixe
                // *****************************************************
            } else if (tauxAssurance.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_TAUX)) {
                lineFacturation = AFNewProcessFacturation.calculerCotisationTauxFixe(donneesFacturation,
                        anneeFacturation, dateEffectiveDebutFacturation, dateEffectiveFinFacturation, nbMoisFacturer,
                        masseMensuel, tauxAssurance);

                // *****************************************************
                // Taux Variable
                // *****************************************************
            } else if (tauxAssurance.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE)) {

                lineFacturation = AFNewProcessFacturation.calculerCotisationTauxVariable(process, donneesFacturation,
                        anneeFacturation, dateEffectiveDebutFacturation, dateEffectiveFinFacturation, nbMoisFacturer,
                        masseMensuel, tauxAssurance);

                // *****************************************************
                // Montant
                // *****************************************************
            } else if (tauxAssurance.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_MONTANT)) {

                lineFacturation = AFNewProcessFacturation.calculerCotisationMontantFixe(donneesFacturation,
                        anneeFacturation, dateEffectiveDebutFacturation, dateEffectiveFinFacturation, nbMoisFacturer,
                        tauxAssurance);
            }
        } else {
            // Il n' y a pas de taux
            // Si pour l'assurance, le type de calcul est "Cotisation"
            // Le montant de la cotisation est égal à la masse de la
            // cotisation au
            // "Prorata Temporis".

            if (nbMoisFacturer > 0) {
                if (donneesFacturation.getTypeCalcul().equals(CodeSystem.TYPE_CALCUL_COTISATION)) {
                    montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMasseAnnuelleCoti()))
                            / 12 * nbMoisFacturer;

                    lineFacturation = new LineFacturation(donneesFacturation, anneeFacturation,
                            dateEffectiveDebutFacturation, dateEffectiveFinFacturation, 0.0, montant, 0.0);
                } else {
                    process.getMemoryLog().logMessage(
                            FWMessageFormat.format(sessionNaos.getLabel("1800"), donneesFacturation.getCotisationId(),
                                    donneesFacturation.getAssuranceId()), FWMessage.AVERTISSEMENT,
                            AFNewProcessFacturation.class.getName());
                }
            }
        }
        return lineFacturation;
    }

    /**
     * Permet de récuperer un compte annexe Date de création : (22.05.2003 10:12:15)
     * 
     * @return globaz.osiris.db.comptes.CACompteAnnexe
     */
    public static CACompteAnnexe getCompteAnnexe(String numAff, BSession sessionNaos, String roleAffilie)
            throws Exception {
        // Chargement du manager
        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.setSession(sessionNaos);
        manager.setForIdRole(roleAffilie);
        manager.setForIdExterneRole(numAff);
        manager.find(BManager.SIZE_USEDEFAULT);
        if (!manager.isEmpty()) {
            AFNewProcessFacturation._compteAnnexe = (CACompteAnnexe) manager.getEntity(0);
        }
        return AFNewProcessFacturation._compteAnnexe;
    }

    public static CACompteur getCompteur(String rubriqueId, String numAff, String dateFin, BSession sessionNaos,
            String roleAffilie) throws Exception {
        // Chargement du compteur
        CACompteur compteur = new CACompteur();
        compteur.setSession(sessionNaos);
        compteur.setAlternateKey(CACompteur.AK_CPTA_RUB_ANNEE);
        compteur.setAnnee(dateFin);
        if (AFNewProcessFacturation.getCompteAnnexe(numAff, sessionNaos, roleAffilie) == null) {
            return null;
        }
        compteur.setIdCompteAnnexe(AFNewProcessFacturation.getCompteAnnexe(numAff, sessionNaos, roleAffilie)
                .getIdCompteAnnexe());
        compteur.setIdRubrique(rubriqueId);
        compteur.retrieve();
        if (!compteur.isNew()) {
            return compteur;
        } else {
            return null;
        }
    }

    /**
     * Determine si les données sont celles d'une cotisation Paritaire.
     * 
     * @param data
     * @return
     */
    private static boolean isCotisationParitaire(AFProcessFacturationViewBean data) {
        boolean result = false;

        if (CodeSystem.TYPE_ASS_MANUELLE.equalsIgnoreCase(data.getTypeAssurance())
                && (!CodeSystem.CODE_FACTU_MONTANT_LIBRE.equalsIgnoreCase(data.getCodeFacturation()) || CodeSystem.GENRE_ASS_PERSONNEL
                        .equalsIgnoreCase(data.getGenreAssurance()))) {
            return false;
        }

        if (data.getCodeFacturation().equalsIgnoreCase(CodeSystem.CODE_FACTU_MONTANT_LIBRE)) {
            return false;
        }
        // soit la masseAnnuel est non nulle soit c'est une assurance FFPP
        if ((Double.parseDouble(data.getMasseAnnuelleCoti()) > 0.0)
                || CodeSystem.TYPE_ASS_FFPP.equals(data.getTypeAssurance())
                || CodeSystem.MOTIF_FIN_EXCEPTION.equals(data.getMotifFinCoti())
                || CodeSystem.TYPE_ASS_LAE.equals(data.getTypeAssurance())) {
            result = true;
        }
        return result;
    }

    /**
     * Determine si les données sont celles d'une cotisation Personnelle.
     * 
     * @param data
     * @return
     */
    private static boolean isCotisationPersonnel(AFProcessFacturationViewBean data) {
        boolean result = false;

        if (Double.parseDouble(data.getMontantTrimestrielCoti()) > 0.0) {
            result = true;
        }
        return result;
    }

    String debutPeriodeFacturation = null;
    private boolean facturerLAE = false;
    private boolean facturerParitaire = false;
    private boolean facturerPersonnel = false;

    private boolean facturerRI_PC = false;
    String finPeriodeFacturation = null;

    String idExterneFacture = null;
    private int idFacturationExt = 0;
    private String idModuleFacturation = "";
    String idSousTypeFacture = null;
    private double montant = 0.0;
    private String numAffilieTest = null;

    private globaz.musca.api.IFAPassage passage = null;

    private String periodiciteAffilieLaPlusPetite = "";
    private int typeFacturationPersonnelle = 0;

    public AFNewProcessFacturation() {
        super();
    }

    public AFNewProcessFacturation(BProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Process de Facturation.
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {

        try {
            facturer(getPassage());
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            getTransaction().addErrors(e.getMessage());
            JadeLogger.error(this, e);
        }
        return !isOnError();
    }

    protected AFAgeRente calculAgeRente(BSession naosSession, String moisFacturation, String anneeFacturation)
            throws Exception {
        String periodeCalcul = "31." + moisFacturation + "." + anneeFacturation;
        AFAgeRente ageRente;

        if (AFNewProcessFacturation.listRente.containsKey(periodeCalcul)) {
            ageRente = AFNewProcessFacturation.listRente.get(periodeCalcul);
        } else {
            ageRente = new AFAgeRente();
            ageRente.initDateRente(naosSession, periodeCalcul);
            AFNewProcessFacturation.listRente.put(periodeCalcul, ageRente);
        }
        return ageRente;
    }

    protected void creerLigneDeFacture(FAEnteteFacture enteteFacture, AFProcessFacturationViewBean donneesFacturation,
            String anneeFacturation, LineFacturation lineFacturation) throws Exception {
        FAAfact lineFacture = new FAAfact();
        lineFacture.setISession(getSession());
        lineFacture.setIdEnteteFacture(enteteFacture.getIdEntete());
        lineFacture.setIdPassage(enteteFacture.getIdPassage());
        lineFacture.setIdModuleFacturation(getIdModuleFacturation());
        lineFacture.setIdTypeAfact(CodeSystem.TYPE_FACT_FACT_STANDARD);
        lineFacture.setNonImprimable(Boolean.FALSE);
        lineFacture.setNonComptabilisable(Boolean.FALSE);
        lineFacture.setAQuittancer(Boolean.FALSE);
        if (donneesFacturation.getNatureRubrique().equals(CodeSystem.NAT_RUBRIQUE_COTI_AVEC_MASSE)
                || donneesFacturation.getNatureRubrique().equals(CodeSystem.NAT_RUBRIQUE_COTI_SANS_MASSE)) {

            lineFacture.setAnneeCotisation(anneeFacturation);
        } else {
            lineFacture.setAnneeCotisation("");
        }
        if (StringUtils.isEmpty(lineFacturation.getLibelle().trim())) {
            String langue = donneesFacturation.getLangue();
            if (langue.equalsIgnoreCase(CodeSystem.LANGUE_FRANCAIS)) {
                lineFacture.setLibelle(donneesFacturation.getAssuranceLibelleFr());
            } else if (langue.equalsIgnoreCase(CodeSystem.LANGUE_ALLEMAND)) {
                lineFacture.setLibelle(donneesFacturation.getAssuranceLibelleAl());
            } else if (langue.equalsIgnoreCase(CodeSystem.LANGUE_ITALIEN)) {
                lineFacture.setLibelle(donneesFacturation.getAssuranceLibelleIt());
            }
        } else {
            lineFacture.setLibelle(lineFacturation.getLibelle());
        }
        lineFacture.setIdRubrique(donneesFacturation.getAssuranceRubriqueId());
        lineFacture.setDebutPeriode(lineFacturation.getDebutPeriode());
        lineFacture.setFinPeriode(lineFacturation.getFinPeriode());
        lineFacture.setMasseFacture(Double.toString(JANumberFormatter.round(lineFacturation.getMasse(), 0.05, 2,
                JANumberFormatter.NEAR)));
        lineFacture.setMontantFacture(Double.toString(JANumberFormatter.round(lineFacturation.getMontant(), 0.05, 2,
                JANumberFormatter.NEAR)));
        lineFacture.setTauxFacture(Double.toString(lineFacturation.getTaux()));
        // ajout de la caisse métier
        String caisseMetier = "";
        if (!JadeStringUtil.isIntegerEmpty(donneesFacturation.getIdCaissePrincipale())) {
            if (JadeStringUtil.isIntegerEmpty(donneesFacturation.getIdCaisseAdhesion())) {
                // aucune adhésion, utiliser la caisse principale
                caisseMetier = donneesFacturation.getIdCaissePrincipale();
            } else {
                caisseMetier = donneesFacturation.getIdCaisseAdhesion();
            }
        }
        if (caisseMetier == null) {
            caisseMetier = "";
        }
        lineFacture.setNumCaisse(caisseMetier);
        // indication si taux à cacher
        lineFacture.setAffichtaux(new Boolean(!lineFacturation.isTauxCache()));
        lineFacture.setNewFacturation(true);
        lineFacture.add(getTransaction());
    }

    protected FAEnteteFacture creerNouvelleEntete(String idPassage, AFProcessFacturationViewBean donneesFacturation,
            String roleCoti, String anneeFacturation, String idSousTypeFacture) throws Exception {

        FAEnteteFacture enteteFacture = new FAEnteteFacture();

        enteteFacture.setIdTiers(donneesFacturation.getIdTiers());
        enteteFacture.setISession(getSession());
        enteteFacture.setIdPassage(idPassage);
        enteteFacture.setIdTypeFacture("1");
        enteteFacture.setIdRole(roleCoti);
        enteteFacture.setIdExterneRole(donneesFacturation.getAffilieNumero());
        enteteFacture.setIdExterneFacture(idExterneFacture);
        if (idFacturationExt != 0) {
            enteteFacture.setIdExterneFacture(String.valueOf(idFacturationExt + 1));
        }
        enteteFacture.setIdSousType(getIdSousTypeFacture());
        enteteFacture.setNonImprimable(Boolean.FALSE);
        enteteFacture.setIdSoumisInteretsMoratoires(CodeSystem.INTERET_MORATOIRE_AUTOMATIQUE);
        enteteFacture.setIdModeRecouvrement(CodeSystem.MODE_RECOUV_AUTOMATIQUE);
        enteteFacture.setIdDomaineCourrier(donneesFacturation.getDomaineCourrier());
        enteteFacture.setIdDomaineLSV(donneesFacturation.getDomaineRecouvrement());
        enteteFacture.setIdDomaineRemboursement(donneesFacturation.getDomaineRemboursement());
        enteteFacture.setNonImprimable(donneesFacturation.getBlocageEnvoi());
        enteteFacture.setLibelle(donneesFacturation.getLibelleFacture());
        // si retraite, le renseigner dans le décompte
        enteteFacture.setEstRentierNa(donneesFacturation.getIsRentier());
        enteteFacture.setProcessusMasse(true);
        enteteFacture.add(getTransaction());
        return enteteFacture;
    }

    protected void determineIdExterneEtSousType(String moisFacturation, String anneeFacturation, String noTrimestre) {
        if (CodeSystem.PERIODICITE_MENSUELLE.equalsIgnoreCase(getPeriodiciteAffilieLaPlusPetite())) {
            setIdExterneFacture(anneeFacturation + moisFacturation + "000");
            setIdSousTypeFacture("2270" + moisFacturation);
        } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equalsIgnoreCase(getPeriodiciteAffilieLaPlusPetite())
                || CodeSystem.PERIODICITE_ANNUELLE.equalsIgnoreCase(getPeriodiciteAffilieLaPlusPetite())) {
            setIdExterneFacture(anneeFacturation + "4" + noTrimestre + "000");
            setIdSousTypeFacture("22704" + noTrimestre);
        }
    }

    /**
     * Traitement de l'affiliation Création de la facturation périodique, personnel, paritaire ou annuel au 30 juin.
     * -------------------------------- ---------------------------------------------------- Ne prendre que les
     * affiliations concernées par le passage et qui n'ont pas été traitées
     * 
     * @param passage
     * @return
     */
    private boolean facturer(globaz.musca.api.IFAPassage passage) {

        getMemoryLog().logMessage("Process Started", FWMessage.INFORMATION, this.getClass().getName());
        AFProcessFacturationManager manager = null;
        BTransaction cursorTransaction = null;
        BStatement statement = null;

        try {
            AFProcessFacturationViewBean donneesFacturation = null;

            String previousKey = null;

            FAEnteteFacture enteteFacture = null;

            String affilieNumero = null;

            int nbCotisation = 0;
            int nbEnteteToAdd = 0;
            int nbLineFactureToAdd = 0;
            int nbEnteteOK = 0;
            int nbLineFactureOK = 0;
            int nbEnteteError = 0;
            int nbLineFactureError = 0;
            int nbMontantZero = 0;
            int nbException = 0;
            boolean exceptionPourAffilie = false;
            boolean ligneFactureCree = false;

            BSession muscaSession = getSession(); // (BSession)passage.getISession();
            FAApplication muscaApp = (FAApplication) muscaSession.getApplication();
            BSession naosSession = (BSession) muscaApp.getSessionNaos(muscaSession);

            manager = new AFProcessFacturationManager();
            manager.setISession(naosSession);

            String idPassage = passage.getIdPassage();
            String datePeriode = passage.getDatePeriode();
            String moisFacturation = datePeriode.substring(0, 2);
            String anneeFacturation = datePeriode.substring(3);

            // ************************************************************
            // Initialisation et calcule de l'Age de Rente
            // ************************************************************
            AFAgeRente ageRente = calculAgeRente(naosSession, moisFacturation, anneeFacturation);

            logInfoDebutProcess(idPassage, datePeriode);

            // ************************************************************
            // Initialisation des paramètres pour la recherche des Cotisations
            // ************************************************************

            initManager(manager, idPassage, datePeriode);

            // Ne pas prendre les bvr neutres
            // manager.setNotInCodeFacturation(CodeSystem.CODE_FACTU_MONTANT_LIBRE);
            // ************************************************************
            // Création du cursorTransaction
            // ************************************************************
            cursorTransaction = (BTransaction) getSession().newTransaction();
            cursorTransaction.openTransaction();
            statement = manager.cursorOpen(cursorTransaction);

            // ****************************************************************
            // POUR CHAQUE COTISATION
            // ****************************************************************
            while ((donneesFacturation = (AFProcessFacturationViewBean) manager.cursorReadNext(statement)) != null
                    && !isAborted()) {
                // Sortie si erreur fatale
                if (getMemoryLog().isOnFatalLevel()) {
                    return false;
                }
                // Détermination de la périodicité
                // Cas mensuel : codePeriodiciteAffilie = 1
                // Cas trimestriel : codePeriodiciteAffilie = 2
                // Cas annuel : codePeriodiciteAffilie = 3
                // Autre = 0 => erreur
                String periodiciteAffilie = FAUtil.determineCodePeriodicite(donneesFacturation);
                // Détermination des dates de début et de fin de la période de facturation de la cotisation
                initPeriodeFacturation(moisFacturation, anneeFacturation, periodiciteAffilie);

                // Détermination du rôle de l'affilié
                String roleAffilie = FAUtil.findRoleAffilie(getSession(), donneesFacturation);

                if (AFNewProcessFacturation.isCotisationParitaire(donneesFacturation) && isFacturerParitaire()) {
                    if (donneesFacturation.getMotifFinCoti().equals(CodeSystem.MOTIF_FIN_EXCEPTION)) {
                        // PO 9032
                        if (JadeStringUtil.isEmpty(donneesFacturation.getDateFinAffiliation())
                                || BSessionUtil
                                        .compareDateFirstLowerOrEqual(getSession(),
                                                donneesFacturation.getDateFinCoti(),
                                                donneesFacturation.getDateFinAffiliation())) {
                            exceptionPourAffilie = true;

                        } else {
                            continue;
                        }
                    } else {
                        // Tester si la cotisation n'a pas une exception pour la période de facturation concernée. Si
                        // c'est le cas il faut l'ignorer car cette assurance sera traitée lors de l'exception
                        AFProcessFacturationManager cotiExcept = new AFProcessFacturationManager();
                        cotiExcept = manager;
                        cotiExcept.setForAffilieNumero(donneesFacturation.getAffilieNumero());
                        cotiExcept.setForIdAffiliation(donneesFacturation.getAffiliationId());
                        cotiExcept.setForAssuranceId(donneesFacturation.getAssuranceId());
                        cotiExcept.setForMotifFin(CodeSystem.MOTIF_FIN_EXCEPTION);
                        if (cotiExcept.getCount() > 0) {
                            continue;
                        }
                    }
                }

                // les données sont triées par tiers
                // Si le tiers a changé, on inscrit tous les afacts de l'entete
                // précédant stockés dans cotisationAffilié
                if ((previousKey == null)
                        || !previousKey.equals(donneesFacturation.getIdTiers() + ","
                                + donneesFacturation.getIdPlanAffiliation())) {
                    ffppDejaFacturee = false;
                    ffppNegativeDejaFacturee = false;
                    // Indiquer le nombre d'affilié qui ont eu une exception dans une de leur cotisation à facturer
                    if (exceptionPourAffilie) {
                        nbException++;
                        exceptionPourAffilie = false;
                    }
                    // Ne pas commiter la première fois
                    if (previousKey != null) {
                        if (!getTransaction().hasErrors()) {
                            // Ne pas commité l'entête si il n'y a pas eu de ligne de facturation de créé.
                            if (ligneFactureCree) {
                                getTransaction().commit();
                                nbEnteteOK += nbEnteteToAdd;
                                nbLineFactureOK += nbLineFactureToAdd;
                            } else {
                                getTransaction().rollback();
                            }
                        } else {
                            rollbackTransaction(getSession().getLabel("691") + affilieNumero);
                            nbEnteteError++;
                            nbLineFactureError += nbLineFactureToAdd;
                        }
                        enteteFacture = null;
                        nbEnteteToAdd = 0;
                        nbLineFactureToAdd = 0;
                    }
                    affilieNumero = donneesFacturation.getAffilieNumero();

                    // DGI 24.08.2007 Afin de gérer des périodicité différentes entre cot
                    // par et pers,
                    // le no de décompte est calculé en fonction de la coti lorsque
                    // l'affiliation est mensuelle (mensuelle et trimestrielle au niveau des
                    // cotis)

                    // PO 7942: Détermination de la périodicité la plus petite

                    AFProcessFacturationManager managerAffilie = new AFProcessFacturationManager();
                    managerAffilie = manager;
                    managerAffilie.setSession(getSession());
                    managerAffilie.setForAffilieNumero(donneesFacturation.getAffilieNumero());
                    managerAffilie.setWantOrderByCodePeriodicite(true);
                    managerAffilie.setForIdAffiliation(donneesFacturation.getAffiliationId());
                    managerAffilie.setForMotifFin("");
                    managerAffilie.setForAssuranceId("");
                    managerAffilie.find(BManager.SIZE_USEDEFAULT);
                    if (managerAffilie.getSize() > 0) {
                        periodiciteAffilieLaPlusPetite = ((AFProcessFacturationViewBean) managerAffilie
                                .getFirstEntity()).getPeriodiciteCoti();
                    }
                    // Détermination du n° de trimestre pour l'idexternefacture
                    String noTrimestre = AFNewProcessFacturation.determinerNumeroTrimestre(donneesFacturation,
                            moisFacturation, getPeriodiciteAffilieLaPlusPetite());

                    determineIdExterneEtSousType(moisFacturation, anneeFacturation, noTrimestre);

                    enteteFacture = rechercherEnteteExistante(idPassage, donneesFacturation, roleAffilie,
                            idExterneFacture);
                    if (enteteFacture.isNew()) {
                        // n'existe pas --> on ajoute l'entete de facture
                        enteteFacture = creerNouvelleEntete(idPassage, donneesFacturation, roleAffilie,
                                anneeFacturation, idExterneFacture);
                        nbEnteteToAdd++;
                        ligneFactureCree = false;
                    }
                }
                if (enteteFacture != null) {
                    LineFacturation lineFacturation = AFNewProcessFacturation.calculerCotisation(this,
                            donneesFacturation, anneeFacturation, moisFacturation, isFacturerParitaire(),
                            isFacturerPersonnel(), isFacturerRI_PC(), isFacturerLAE(), getDebutPeriodeFacturation(),
                            getFinPeriodeFacturation(), ageRente, roleAffilie, idPassage);
                    // Mise à jour du code rentier... déterminer dans le calcul de la cotisation
                    if (enteteFacture.getEstRentierNa().equals(Boolean.FALSE)
                            && donneesFacturation.getIsRentier().equals(Boolean.TRUE)) {
                        enteteFacture.setEstRentierNa(donneesFacturation.getIsRentier());
                        enteteFacture.update(getTransaction());
                    }
                    if ((lineFacturation != null) && (lineFacturation.getMontant() != 0.0)) {
                        ligneFactureCree = true;
                        creerLigneDeFacture(enteteFacture, donneesFacturation, anneeFacturation, lineFacturation);
                        nbLineFactureToAdd++;
                    } else {
                        nbMontantZero++;
                    }
                }
                previousKey = donneesFacturation.getIdTiers() + "," + donneesFacturation.getIdPlanAffiliation();
                nbCotisation++;
            } // end while

            // Validation finale
            if (!getTransaction().hasErrors()) {
                // Ne pas commité l'entête si il n'y a pas eu de ligne de facturation de créé.
                if (ligneFactureCree) {
                    getTransaction().commit();
                    nbEnteteOK += nbEnteteToAdd;
                    nbLineFactureOK += nbLineFactureToAdd;
                } else {
                    getTransaction().rollback();
                }
            } else {
                rollbackTransaction(getSession().getLabel("691") + affilieNumero);
                nbEnteteError++;
                nbLineFactureError += nbLineFactureToAdd;
            }

            // Sortie si erreur fatale
            if (getMemoryLog().isOnFatalLevel()) {
                return false;
            }
            // Information à stocker dans le memory log (pour email)
            setInfoForMemoryLog(nbCotisation, nbEnteteOK, nbLineFactureOK, nbEnteteError, nbLineFactureError,
                    nbMontantZero, nbException);
        } catch (Exception e) {
            // Fait remonter l'erreur au processus parent
            getTransaction().addErrors(e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            JadeLogger.error(this, e);

            try {
                getTransaction().rollback();
            } catch (Exception f) {
                getMemoryLog().logMessage(f.getMessage(), FWMessage.FATAL, this.getClass().getName());
                JadeLogger.error(this, f);
            }
            return false;
        } finally {
            try {
                if (statement != null) {
                    try {
                        manager.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } catch (Exception g) {
                getMemoryLog().logMessage(g.getMessage(), FWMessage.FATAL, this.getClass().getName());
                JadeLogger.error(this, g);
            } finally {
                if ((cursorTransaction != null) && (cursorTransaction.isOpened())) {
                    try {
                        cursorTransaction.closeTransaction();
                    } catch (Exception e) {
                        JadeLogger.error(this, e);
                    }
                }
            }
        }
        getMemoryLog().logMessage("Process Terminated", FWMessage.INFORMATION, this.getClass().getName());
        return !isOnError();
    }

    public String getDebutPeriodeFacturation() {
        return debutPeriodeFacturation;
    }

    @Override
    protected String getEMailObject() {
        return "";
    }

    public String getFinPeriodeFacturation() {
        return finPeriodeFacturation;
    }

    public String getIdExterneFacture() {
        return idExterneFacture;
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    public String getIdSousTypeFacture() {
        return idSousTypeFacture;
    }

    public double getMontant() {
        return montant;
    }

    public String getNumAffilieTest() {
        return numAffilieTest;
    }

    public globaz.musca.api.IFAPassage getPassage() {
        return passage;
    }

    public String getPeriodiciteAffilieLaPlusPetite() {
        return periodiciteAffilieLaPlusPetite;
    }

    public int getTypeFacturationPersonnelle() {
        return typeFacturationPersonnelle;
    }

    private boolean hasSousEnsembleAffilies(String idPassage) {
        try {
            String sql = "FROM " + TIToolBox.getCollection() + "FAFIAFP";
            sql += " WHERE " + TIToolBox.getCollection() + "FAFIAFP.IDPASSAGE = " + idPassage;
            int count = (int) TISQL.count(getSession(), sql);
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return false;
    }

    protected void initManager(AFProcessFacturationManager manager, String idPassage, String datePeriode) {
        manager.setWantSousEnsembleAffilie(hasSousEnsembleAffilies(idPassage));
        manager.setForIdPassage(idPassage);

        manager.setForDateFacturation(datePeriode);
        // ajout d'un filtre pour un assuré pour tests
        if (!JadeStringUtil.isEmpty(numAffilieTest)) {
            manager.setForAffilieNumero(numAffilieTest);
        }

        if (isFacturerPersonnel() && !isFacturerParitaire()) {
            manager.setForTypeAffiliation(AFProcessFacturationManager.TYPE_AFFILIATION_PERSONNEL);
            // manager.setForRole(CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication()));
            // manager.setForRole(CodeSystem.ROLE_AFFILIE);
            manager.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
        } else if (isFacturerParitaire() && !isFacturerPersonnel()) {
            // tri sur les affiliation aux acomptes
            manager.setForTypeFacturation(AFProcessFacturationManager.TYPE_FACTURATION_ACOMPTE);
            manager.setForTypeAffiliation(AFProcessFacturationManager.TYPE_AFFILIATION_PARITAIRE);
            // manager.setForRole(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(getSession().getApplication()));
            // manager.setForRole(CodeSystem.ROLE_AFFILIE);
            manager.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
        } else if (isFacturerParitaire() && isFacturerPersonnel()) {
            // ne devrait plus arriver car les modules sont séparés
            manager.setForTypeAffiliation(AFProcessFacturationManager.TYPE_AFFILIATION_PARITAIRE_PERSONNEL);
            // manager.setForRole(CodeSystem.ROLE_AFFILIE);
        } else if (isFacturerRI_PC()) {
            // ne devrait plus arriver car les modules sont séparés
            manager.setForTypeAffiliation(AFProcessFacturationManager.TYPE_AFFILIATION_RI_PC);
            manager.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
        } else if (isFacturerLAE()) {
            // ne devrait plus arriver car les modules sont séparés
            manager.setForTypeAffiliation(AFProcessFacturationManager.TYPE_AFFILIATION_PARITAIRE);
            manager.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
        } else {
            manager.setForTypeAffiliation("0");
        }
        /*
         * Inforom 314s: Si forTypeFacturationPersonnelle = 0 => facturation périodique de toutes les cot.pers. Si
         * forTypeFacturationPersonnelle = 1 => facturation périodique des indépendants et TSE. Si
         * forTypeFacturationPersonnelle = 2 => facturation périodique des non actifs.
         */
        manager.setForTypeFacturationPersonnelle(getTypeFacturationPersonnelle());
    }

    protected void initPeriodeFacturation(String moisFacturation, String anneeFacturation, String periodiciteAffilie)
            throws NumberFormatException {
        if (CodeSystem.PERIODICITE_MENSUELLE.equalsIgnoreCase(periodiciteAffilie)) {
            // Période de Facturation mensuelle
            setDebutPeriodeFacturation("01." + moisFacturation + "." + anneeFacturation);
            setFinPeriodeFacturation(AFUtil.getDateEndOfMonth(debutPeriodeFacturation));
        } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equalsIgnoreCase(periodiciteAffilie)) {
            // Période de Facturation trimestrielle
            // Période de Facturation trimestrielle
            String theMoisDebutFacturation = JadeStringUtil.fillWithZeroes(
                    String.valueOf(Integer.parseInt(moisFacturation) - 2), 2);
            setDebutPeriodeFacturation("01." + theMoisDebutFacturation + "." + anneeFacturation);
            setFinPeriodeFacturation(AFUtil.getDateEndOfMonth("01." + moisFacturation + "." + anneeFacturation));
        } else if (CodeSystem.PERIODICITE_ANNUELLE.equalsIgnoreCase(periodiciteAffilie)) {
            // Période de Facturation annuelle
            // Période de Facturation annuelle
            setDebutPeriodeFacturation("01.01." + anneeFacturation);
            setFinPeriodeFacturation("31.12." + anneeFacturation);
        }
    }

    public boolean isFacturerLAE() {
        return facturerLAE;
    }

    public boolean isFacturerParitaire() {
        return facturerParitaire;
    }

    public boolean isFacturerPersonnel() {
        return facturerPersonnel;
    }

    public boolean isFacturerRI_PC() {
        return facturerRI_PC;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    protected void logInfoDebutProcess(String idPassage, String datePeriode) {
        getMemoryLog().logMessage("idPassage           = " + idPassage, FWMessage.INFORMATION,
                this.getClass().getName());
        getMemoryLog().logMessage("datePeriode         = " + datePeriode, FWMessage.INFORMATION,
                this.getClass().getName());
        getMemoryLog().logMessage("isFacturerPersonnel = " + isFacturerPersonnel(), FWMessage.INFORMATION,
                this.getClass().getName());
        getMemoryLog().logMessage("isFacturerRI_PC = " + isFacturerRI_PC(), FWMessage.INFORMATION,
                this.getClass().getName());
        getMemoryLog().logMessage("isFacturerLAE = " + isFacturerLAE(), FWMessage.INFORMATION,
                this.getClass().getName());
        getMemoryLog().logMessage("isFacturerParitaire = " + isFacturerParitaire(), FWMessage.INFORMATION,
                this.getClass().getName());
        getMemoryLog().logMessage("------------------------", FWMessage.INFORMATION, this.getClass().getName());
    }

    protected FAEnteteFacture rechercherEnteteExistante(String idPassage,
            AFProcessFacturationViewBean donneesFacturation, String roleCoti, String idExterneFacture)
            throws Exception, NumberFormatException {
        FAEnteteFactureManager entete = new FAEnteteFactureManager();
        entete.setSession(getSession());
        entete.setForIdPassage(idPassage);
        // recherche des en-tête de la même période (DGI
        // 21.06.07)
        entete.setLikeIdExterneFacture(idExterneFacture.substring(0, 6));
        entete.setForIdExterneRole(donneesFacturation.getAffilieNumero());
        entete.setForIdTiers(donneesFacturation.getIdTiers());
        entete.find(BManager.SIZE_NOLIMIT);
        // recherche sur les en-tête déjà existantes
        idFacturationExt = 0;
        for (int iEntete = 0; iEntete < entete.size(); iEntete++) {
            FAEnteteFacture ef = (FAEnteteFacture) entete.getEntity(iEntete);
            // recherche et sauvegarde du dernier id externe
            // facturation
            int idFact = Integer.parseInt(ef.getIdExterneFacture());
            if (idFacturationExt < idFact) {
                idFacturationExt = idFact;
            }
            if (roleCoti.equals(ef.getIdRole()) && donneesFacturation.getLibelleFacture().equals(ef.getLibelle())) {
                // il existe une en-tête avec le bon rôle et les
                // mêmes domaines -> l'utiliser
                FAEnteteFacture enteteFacture = new FAEnteteFacture();
                enteteFacture.setSession(getSession());
                enteteFacture.setIdEntete(ef.getIdEntete());
                enteteFacture.retrieve();
                if (donneesFacturation.getIsRentier().booleanValue()) {
                    // si retraite, le renseigner dans le
                    // décompte
                    enteteFacture.setEstRentierNa(donneesFacturation.getIsRentier());
                    enteteFacture.update(getTransaction());
                }
                return enteteFacture;
            }
        } // for
        return new FAEnteteFacture();
    }

    private void rollbackTransaction(String message) {
        // Logger l'erreur
        getMemoryLog().logMessage(message, FWMessage.ERREUR, this.getClass().getName());
        // Logger les messages d'erreur de la transaction
        getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
        try {
            getTransaction().rollback();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            JadeLogger.error(this, e);
        }
    }

    public void setDebutPeriodeFacturation(String debutPeriodeFacturation) {
        this.debutPeriodeFacturation = debutPeriodeFacturation;
    }

    public void setFacturerLAE(boolean newFacturerLAE) {
        facturerLAE = newFacturerLAE;
    }

    public void setFacturerParitaire(boolean newFacturerParitaire) {
        facturerParitaire = newFacturerParitaire;
    }

    public void setFacturerPersonnel(boolean newFacturerPersonnel) {
        facturerPersonnel = newFacturerPersonnel;
    }

    public void setFacturerRI_PC(boolean newFacturerRI_PC) {
        facturerRI_PC = newFacturerRI_PC;
    }

    public void setFinPeriodeFacturation(String finPeriodeFacturation) {
        this.finPeriodeFacturation = finPeriodeFacturation;
    }

    public void setIdExterneFacture(String idExterneFacture) {
        this.idExterneFacture = idExterneFacture;
    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }

    public void setIdSousTypeFacture(String idSousTypeFacture) {
        this.idSousTypeFacture = idSousTypeFacture;
    }

    protected void setInfoForMemoryLog(int nbCotisation, int nbEnteteOK, int nbLineFactureOK, int nbEnteteError,
            int nbLineFactureError, int nbMontantZero, int nbException) {
        getMemoryLog().logMessage("------------------------", FWMessage.INFORMATION, this.getClass().getName());
        getMemoryLog().logMessage("Cotisation handeled    : " + nbCotisation, FWMessage.INFORMATION,
                this.getClass().getName());
        getMemoryLog().logMessage("------------------------", FWMessage.INFORMATION, this.getClass().getName());
        getMemoryLog().logMessage("Nb HeaderFacture OK    : " + nbEnteteOK, FWMessage.INFORMATION,
                this.getClass().getName());
        getMemoryLog().logMessage("Nb LineFacture   OK    : " + nbLineFactureOK, FWMessage.INFORMATION,
                this.getClass().getName());
        getMemoryLog().logMessage("Nb Exception           : " + nbException, FWMessage.INFORMATION,
                this.getClass().getName());
        getMemoryLog().logMessage("Nb Sum = 0             : " + nbMontantZero, FWMessage.INFORMATION,
                this.getClass().getName());
        getMemoryLog().logMessage("Nb HeaderFacture Error : " + nbEnteteError, FWMessage.INFORMATION,
                this.getClass().getName());
        getMemoryLog().logMessage("Nb LineFacture   Error : " + nbLineFactureError, FWMessage.INFORMATION,
                this.getClass().getName());
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void setNumAffilieTest(String numAffilieTest) {
        this.numAffilieTest = numAffilieTest;
    }

    public void setPassage(globaz.musca.api.IFAPassage newPassage) {
        passage = newPassage;
    }

    public void setPeriodiciteAffilieLaPlusPetite(String periodiciteAffilieLaPlusPetite) {
        this.periodiciteAffilieLaPlusPetite = periodiciteAffilieLaPlusPetite;
    }

    public void setTypeFacturationPersonnelle(int typeFactuationPersonnelle) {
        typeFacturationPersonnelle = typeFactuationPersonnelle;
    }

}
