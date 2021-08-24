package globaz.naos.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
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
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.external.ServicesFacturation;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Process pour la facturation des Cotisations Personnelles, Paritaires
 * 
 * @author: mmu, sau
 */

public final class AFProcessFacturation extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Classe contenant les données issues d'un calcul de cotisation. Permet d'enregistrer les valeurs de facturation
     * avant insertion dans la BD
     * 
     * @author mmu
     */
    public static class CalculCotisation {
        private String affilieNumero = null;
        private List<String> assuranceException = null;
        private String idExterneFacture = null;
        private String idModuleFacturation = null;
        private String idSousTypeFacture = null;

        private double montant;
        private List<LineFacturation> montantList = null;

        private int nbException = 0;

        // private String assuranceId = null;

        private String periodiciteAff = null;

        public CalculCotisation() {
            super();
        }

        public String getAffilieNumero() {
            return affilieNumero;
        }

        public List<String> getAssuranceException() {
            return assuranceException;
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

        public List<LineFacturation> getMontantList() {
            return montantList;
        }

        public int getNbException() {
            return nbException;
        }

        public String getPeriodiciteAff() {
            return periodiciteAff;
        }

        public int incNbException() {
            return nbException++;
        }

        public void setAffilieNumero(String string) {
            affilieNumero = string;
        }

        public void setAssuranceException(List<String> list) {
            assuranceException = list;
        }

        public void setIdExterneFacture(String string) {
            idExterneFacture = string;
        }

        public void setIdModuleFacturation(String string) {
            idModuleFacturation = string;
        }

        public void setIdSousTypeFacture(String string) {
            idSousTypeFacture = string;
        }

        public void setMontant(double d) {
            montant = d;
        }

        public void setMontantList(List<LineFacturation> list) {
            montantList = list;
        }

        public void setNbException(int i) {
            nbException = i;
        }

        public void setPeriodiciteAff(String string) {
            periodiciteAff = string;
        }

    }

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
            this.debutPeriode = debutPeriode;
            this.finPeriode = finPeriode;
            this.masse = masse;
            this.montant = montant;
            this.taux = taux;

            if (donneesFacturation.getNatureRubrique().equals(CodeSystem.NAT_RUBRIQUE_COTI_AVEC_MASSE)
                    || donneesFacturation.getNatureRubrique().equals(CodeSystem.NAT_RUBRIQUE_COTI_SANS_MASSE)) {

                anneeCotisation = anneeFacturation;
            } else {
                anneeCotisation = "";
            }

            String langue = donneesFacturation.getLangue();
            if (langue.equalsIgnoreCase(CodeSystem.LANGUE_FRANCAIS)) {
                libelle = donneesFacturation.getAssuranceLibelleFr();
            } else if (langue.equalsIgnoreCase(CodeSystem.LANGUE_ALLEMAND)) {
                libelle = donneesFacturation.getAssuranceLibelleAl();
            } else if (langue.equalsIgnoreCase(CodeSystem.LANGUE_ITALIEN)) {
                libelle = donneesFacturation.getAssuranceLibelleIt();
            }
            assuranceId = donneesFacturation.getAssuranceId();
            idRubrique = donneesFacturation.getAssuranceRubriqueId();

            // caisse
            // si une caisse principale existe, utiliser la gestion
            // multi-caisse: prendre la caisse liée à la cotisation ou
            // celle de la caisse principale si vide
            // si la caisse principale n'existe pas, on ne renseigne rien (sans
            // gestion caisse)
            if (!JadeStringUtil.isIntegerEmpty(donneesFacturation.getIdCaissePrincipale())) {
                if (JadeStringUtil.isIntegerEmpty(donneesFacturation.getIdCaisseAdhesion())) {
                    // aucune adhésion, utiliser la caisse principale
                    caisse = donneesFacturation.getIdCaissePrincipale();
                } else {
                    caisse = donneesFacturation.getIdCaisseAdhesion();
                }
            }

        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            LineFacturation other = (LineFacturation) obj;
            if (anneeCotisation == null) {
                if (other.anneeCotisation != null) {
                    return false;
                }
            } else if (!anneeCotisation.equals(other.anneeCotisation)) {
                return false;
            }
            if (assuranceId == null) {
                if (other.assuranceId != null) {
                    return false;
                }
            } else if (!assuranceId.equals(other.assuranceId)) {
                return false;
            }
            if (caisse == null) {
                if (other.caisse != null) {
                    return false;
                }
            } else if (!caisse.equals(other.caisse)) {
                return false;
            }

            if (idRubrique == null) {
                if (other.idRubrique != null) {
                    return false;
                }
            } else if (!idRubrique.equals(other.idRubrique)) {
                return false;
            }
            if (isTauxCache != other.isTauxCache) {
                return false;
            }
            if (libelle == null) {
                if (other.libelle != null) {
                    return false;
                }
            } else if (!libelle.equals(other.libelle)) {
                return false;
            }
            if (Double.doubleToLongBits(masse) != Double.doubleToLongBits(other.masse)) {
                return false;
            }
            if (Double.doubleToLongBits(montant) != Double.doubleToLongBits(other.montant)) {
                return false;
            }
            if (Double.doubleToLongBits(taux) != Double.doubleToLongBits(other.taux)) {
                return false;
            }
            return true;
        }

        public String getAnneeCotisation() {
            return anneeCotisation;
        }

        public String getAssuranceId() {
            return assuranceId;
        }

        /**
         * @return
         */
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

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + ((anneeCotisation == null) ? 0 : anneeCotisation.hashCode());
            result = (prime * result) + ((assuranceId == null) ? 0 : assuranceId.hashCode());
            result = (prime * result) + ((caisse == null) ? 0 : caisse.hashCode());
            result = (prime * result) + ((debutPeriode == null) ? 0 : debutPeriode.hashCode());
            result = (prime * result) + ((finPeriode == null) ? 0 : finPeriode.hashCode());
            result = (prime * result) + ((idRubrique == null) ? 0 : idRubrique.hashCode());
            result = (prime * result) + (isTauxCache ? 1231 : 1237);
            result = (prime * result) + ((libelle == null) ? 0 : libelle.hashCode());
            long temp;
            temp = Double.doubleToLongBits(masse);
            result = (prime * result) + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(montant);
            result = (prime * result) + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(taux);
            result = (prime * result) + (int) (temp ^ (temp >>> 32));
            return result;
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

        /**
         * @param string
         */
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
    private static String ID_MOD_FACURATION_LAE = null;
    private static String ID_MOD_FACURATION_PARITAIRE = null;

    private static String ID_MOD_FACURATION_PERSONNEL = null;

    private static String ID_MOD_FACURATION_RELEVE = null;
    private static String ID_MOD_FACURATION_RI_PC = null;
    private static HashMap<String, AFAgeRente> listRente = new HashMap<String, AFAgeRente>();

    public final static int PERIODIQUE_COT_PERS = 0;

    public final static int PERIODIQUE_COT_PERS_IND = 1;

    public final static int PERIODIQUE_COT_PERS_NAC = 2;

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
        planAffiliation.setPlanAffiliationId(donneesFacturation.getIdPlanAffiliation());
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

                    double montant = (masseMensuel * nbMoisFacturer * Double.parseDouble(JANumberFormatter
                            .deQuote(tauxAssurance.getValeurTotal())))
                            / Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getFraction()));

                    sommeMasse.add(montant);

                }

                // *****************************************************
                // Montant
                // *****************************************************
            } else if (tauxAssurance.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_MONTANT)) {

                if (nbMoisFacturer > 0) {

                    double montantMensuel = 0.0;
                    // Montant

                    // if
                    // (BSessionUtil.compareDateFirstLowerOrEqual(process.getSession(),
                    // tauxAssurance.getDateDebut(),
                    // dateEffectiveDebutFacturation)) {

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
            } else if (donneesFacturation.getTypeCalcul().equals(CodeSystem.TYPE_CALCUL_COTISATION)) {
                double montant = (Double.parseDouble(masseAnnuel) / 12) * nbMoisFacturer;

                sommeMasse.add(montant);
            }
        } else {
            // Il n' y a pas de taux
            // Si pour l'assurance, le type de calcul est "Cotisation"
            // Le montant de la cotisation est égal à la masse de la cotisation
            // au
            // "Prorata Temporis".
            if (donneesFacturation.getTypeCalcul().equals(CodeSystem.TYPE_CALCUL_COTISATION)
                    || CodeSystem.TYPE_CALCUL_MONTANT_FIXE.equals(donneesFacturation.getTypeCalcul())) {
                double montant = (Double.parseDouble(masseAnnuel) / 12) * nbMoisFacturer;

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
    public static final CalculCotisation calculerCotisation(BProcess process,
            AFProcessFacturationViewBean donneesFacturation, String anneeFacturation, String moisFacturation,
            boolean isParitaire, boolean isPersonnel, boolean isRI_PC, boolean isLAE,
            List<LineFacturation> cotisationAffilie, int typeFacturationPersonnelle, String idPassage) throws Exception {

        BSession sessionNaos = null;

        String applicationId = process.getSession().getApplicationId();

        if ("MUSCA".equalsIgnoreCase(applicationId)) {
            FAApplication applicationMusca = (FAApplication) process.getSession().getApplication();
            sessionNaos = (BSession) applicationMusca.getSessionNaos(process.getSession());
        } else {
            sessionNaos = process.getSession();
        }

        String affilieNumero = null;
        String periodiciteCoti = null;
        String noTrimestre = null;
        String idExterneFacture = null;
        String idSousTypeFacture = null;

        // ************************************************************
        // Initialisation et calcule de l'Age de Rente
        // ************************************************************
        String periodeCalcul = "31." + moisFacturation + "." + anneeFacturation;
        AFAgeRente ageRente;

        if (AFProcessFacturation.listRente.containsKey(periodeCalcul)) {
            ageRente = AFProcessFacturation.listRente.get(periodeCalcul);
        } else {
            ageRente = new AFAgeRente();
            ageRente.initDateRente(sessionNaos, periodeCalcul);
            AFProcessFacturation.listRente.put(periodeCalcul, ageRente);
        }

        String debutPeriodeFacturation = null;
        String finPeriodeFacturation = null;

        String dateEffectiveDebutFacturation = null;
        String dateEffectiveFinFacturation = null;
        String dateDebutCotisation = null;
        String dateFinCotisation = null;
        String dateRetraite = null;
        String dateDeces = null;

        String idModuleFacturation = "";
        List<LineFacturation> montantList = new ArrayList<LineFacturation>();

        List<String> assuranceException = new ArrayList<String>();
        double montant = 0.0;

        int nbException = 0;

        affilieNumero = donneesFacturation.getAffilieNumero();

        // ************************************************************
        // Calcul de la date de Debut et de Fin de Facturation et
        // des code pour la facturation en fonction de la période
        // ************************************************************

        // DGI 24.08.2007 Afin de gérer des périodicité différentes entre cot
        // par et pers,
        // le no de décompte est calculé en fonction de la coti lorsque
        // l'affiliation est mensuelle (mensuelle et trimestrielle au niveau des
        // cotis)
        periodiciteCoti = donneesFacturation.getPeriodiciteCoti();

        if (periodiciteCoti.equals(CodeSystem.PERIODICITE_MENSUELLE)
                && !donneesFacturation.getPeriodiciteCoti().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {

            // Période de Facturation
            debutPeriodeFacturation = "01." + moisFacturation + "." + anneeFacturation;
            finPeriodeFacturation = AFUtil.getDateEndOfMonth(debutPeriodeFacturation);

            // Code de facturation
            idExterneFacture = anneeFacturation + moisFacturation + "000";
            idSousTypeFacture = "2270" + moisFacturation;

        } else if (periodiciteCoti.equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)
                || donneesFacturation.getPeriodiciteCoti().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {

            // Période de Facturation
            if ((Integer.parseInt(moisFacturation) - 2) < 10) {
                debutPeriodeFacturation = "01.0" + Integer.toString(Integer.parseInt(moisFacturation) - 2) + "."
                        + anneeFacturation;
            } else {
                debutPeriodeFacturation = "01." + Integer.toString(Integer.parseInt(moisFacturation) - 2) + "."
                        + anneeFacturation;
            }
            finPeriodeFacturation = AFUtil.getDateEndOfMonth("01." + moisFacturation + "." + anneeFacturation);

            // Code de facturation
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

            idExterneFacture = anneeFacturation + "4" + noTrimestre + "000";
            idSousTypeFacture = "22704" + noTrimestre;

        } else if (periodiciteCoti.equals(CodeSystem.PERIODICITE_ANNUELLE)) {
            // Période de Facturation
            debutPeriodeFacturation = "01.01." + anneeFacturation;
            finPeriodeFacturation = "31.12." + anneeFacturation;

            // Code de facturation
            if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.MARS_TRIMESTRE_1)) {
                noTrimestre = "5";
            } else if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.JUIN_TRIMESTRE_2)) {
                noTrimestre = "6";
            } else if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager.SEPTEMBRE_TRIMESTRE_3)) {
                noTrimestre = "7";
                /*
                 * } else if (moisFacturation.equalsIgnoreCase(AFProcessFacturationManager .ANNUEL)) { noTrimestre =
                 * "0"; }
                 */
            } else {
                noTrimestre = "0";
            }
            idExterneFacture = anneeFacturation + "4" + noTrimestre + "000";
            idSousTypeFacture = "22704" + noTrimestre;
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
        // PERSONNEL
        // *******************************
        if (AFProcessFacturation.isCotisationPersonnel(donneesFacturation) && isPersonnel) {
            idModuleFacturation = AFProcessFacturation.getIdModuleFacurationPersonnel(sessionNaos,
                    process.getTransaction(), typeFacturationPersonnelle);

            montant = AFProcessFacturation.calculerCotisationPersonnelle(donneesFacturation, anneeFacturation,
                    sessionNaos, dateEffectiveDebutFacturation, dateEffectiveFinFacturation, montantList,
                    nbMoisFacturer);
        }

        // *******************************
        // RI_PC
        // *******************************
        else if (AFProcessFacturation.isCotisationPersonnel(donneesFacturation) && isRI_PC) {
            idModuleFacturation = AFProcessFacturation
                    .getIdModuleFacurationRI_PC(sessionNaos, process.getTransaction());

            montant = AFProcessFacturation.calculerCotisationPersonnelle(donneesFacturation, anneeFacturation,
                    sessionNaos, dateEffectiveDebutFacturation, dateEffectiveFinFacturation, montantList,
                    nbMoisFacturer);
        }
        // *******************************
        // LAE
        // *******************************
        else if (AFProcessFacturation.isCotisationParitaire(donneesFacturation) && isLAE) {
            idModuleFacturation = AFProcessFacturation.getIdModuleFacurationLAE(sessionNaos, process.getTransaction());
            // Spécial CICICAM: La facturation se fait en juin et se base sur les 6 premiers de masse facturé
            if (CodeSystem.TYPE_ASS_LAE.equals(donneesFacturation.getTypeAssurance())) {
                montantList.clear();

                // Récupération du cumul des 5 premiers mois
                CACompteur cnt = AFProcessFacturation.getCompteur("151", donneesFacturation.getAffilieNumero(),
                        anneeFacturation, sessionNaos);
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
                afac.find();
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
                    LineFacturation lineFactu = new LineFacturation(donneesFacturation, anneeFacturation,
                            dateEffectiveDebutFacturation, dateEffectiveFinFacturation, montantCompta.doubleValue(),
                            montant, Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())));
                    montantList.add(lineFactu);

                }
            }
        }
        // *******************************
        // PARITAIRE
        // *******************************
        else if (AFProcessFacturation.isCotisationParitaire(donneesFacturation) && isParitaire) {
            idModuleFacturation = AFProcessFacturation.getIdModuleFacurationParitaire(sessionNaos,
                    process.getTransaction());

            montantList.clear();
            // Exception
            if (donneesFacturation.getMotifFinCoti().equals(CodeSystem.MOTIF_FIN_EXCEPTION)) {
                nbException++;
                // Si la cotisation est une Exception, on supprime la cotisation
                // correspondant
                // (même assuranceId) si elle a déjà été calculée.
                for (int i = cotisationAffilie.size() - 1; i >= 0; i--) {
                    LineFacturation aLine = cotisationAffilie.get(i);
                    if (aLine.assuranceId.equals(donneesFacturation.getAssuranceId())) {
                        cotisationAffilie.remove(i);
                    }
                }
                assuranceException.add(donneesFacturation.getAssuranceId());

            } else {

                // Si la cotisation a déjà une exception calculée, on ne facture
                // rien.
                for (int i = 0; i < assuranceException.size(); i++) {
                    String assuranceId = assuranceException.get(i);
                    if (assuranceId.equals(donneesFacturation.getAssuranceId())) {
                        nbMoisFacturer = 0;
                        break;
                    }
                }
            }
            double masseMensuel = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation
                    .getMasseAnnuelleCoti())) / 12;

            if (!JadeStringUtil.isIntegerEmpty(donneesFacturation.getIdReferenceAssurance())) {

                masseMensuel = AFProcessFacturation.calculeMasseAssuranceReference(process, donneesFacturation,
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
                    AFProcessFacturation.calculerCotisationFFPP(process, donneesFacturation, anneeFacturation,
                            cotisationAffilie, sessionNaos, montantList, tauxAssurance);

                    // *****************************************************
                    // Taux Fixe
                    // *****************************************************
                } else if (tauxAssurance.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_TAUX)) {
                    AFProcessFacturation.calculerCotisationTauxFixe(donneesFacturation, anneeFacturation,
                            dateEffectiveDebutFacturation, dateEffectiveFinFacturation, montantList, nbMoisFacturer,
                            masseMensuel, tauxAssurance);

                    // *****************************************************
                    // Taux Variable
                    // *****************************************************
                } else if (tauxAssurance.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE)) {

                    AFProcessFacturation.calculerCotisationTauxVariable(process, donneesFacturation, anneeFacturation,
                            dateEffectiveDebutFacturation, dateEffectiveFinFacturation, montantList, nbMoisFacturer,
                            masseMensuel, tauxAssurance);

                    // *****************************************************
                    // Montant
                    // *****************************************************
                } else if (tauxAssurance.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_MONTANT)) {

                    AFProcessFacturation.calculerCotisationMontantFixe(donneesFacturation, anneeFacturation,
                            dateEffectiveDebutFacturation, dateEffectiveFinFacturation, montantList, nbMoisFacturer,
                            tauxAssurance);
                }
            } else {
                // Il n' y a pas de taux
                // Si pour l'assurance, le type de calcul est "Cotisation"
                // Le montant de la cotisation est égal à la masse de la
                // cotisation au
                // "Prorata Temporis".

                if (nbMoisFacturer > 0) {
                    if (donneesFacturation.getTypeCalcul().equals(CodeSystem.TYPE_CALCUL_COTISATION)
                            || CodeSystem.TYPE_CALCUL_MONTANT_FIXE.equals(donneesFacturation.getTypeCalcul())) {
                        montant = (Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation
                                .getMasseAnnuelleCoti())) / 12) * nbMoisFacturer;

                        montantList.add(new LineFacturation(donneesFacturation, anneeFacturation,
                                dateEffectiveDebutFacturation, dateEffectiveFinFacturation, 0.0, montant, 0.0));
                    } else {
                        process.getMemoryLog().logMessage(
                                FWMessageFormat.format(sessionNaos.getLabel("1800"),
                                        donneesFacturation.getCotisationId(), donneesFacturation.getAssuranceId()),
                                FWMessage.AVERTISSEMENT, AFProcessFacturation.class.getName());
                    }
                }
            }

            montant = 0.0;

            for (int i = 0; i < montantList.size(); i++) {
                montant += (montantList.get(i)).montant;

            }

        } else {
            montant = 0.0;
            if (AFProcessFacturation.isCotisationParitaire(donneesFacturation)) {
                process.getMemoryLog().logMessage(
                        FWMessageFormat.format(sessionNaos.getLabel("1810"), donneesFacturation.getCotisationId(),
                                donneesFacturation.getTypeAffiliation()), FWMessage.AVERTISSEMENT,
                        AFProcessFacturation.class.getName());
            } else if (AFProcessFacturation.isCotisationPersonnel(donneesFacturation)) {
                process.getMemoryLog().logMessage(
                        FWMessageFormat.format(sessionNaos.getLabel("1820"), donneesFacturation.getCotisationId(),
                                donneesFacturation.getTypeAffiliation()), FWMessage.AVERTISSEMENT,
                        AFProcessFacturation.class.getName());
            }
        }

        if (CodeSystem.TYPE_ASS_MANUELLE.equalsIgnoreCase(donneesFacturation.getTypeAssurance())
                && (!CodeSystem.CODE_FACTU_MONTANT_LIBRE.equalsIgnoreCase(donneesFacturation.getCodeFacturation()) || CodeSystem.GENRE_ASS_PERSONNEL
                        .equalsIgnoreCase(donneesFacturation.getGenreAssurance()))) {

            montantList.clear();

            if (CodeSystem.PERIODICITE_ANNUELLE.equalsIgnoreCase(donneesFacturation.getPeriodiciteCoti())) {

                dateEffectiveDebutFacturation = "01.01." + anneeFacturation;
                dateEffectiveFinFacturation = "31.12." + anneeFacturation;

                if (CodeSystem.GENRE_ASS_PARITAIRE.equalsIgnoreCase(donneesFacturation.getGenreAssurance())) {
                    montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMasseAnnuelleCoti()));
                } else {
                    montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMontantAnnuelCoti()));
                }

            } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equalsIgnoreCase(donneesFacturation.getPeriodiciteCoti())) {

                String theMoisDebutFacturation = JadeStringUtil.fillWithZeroes(
                        String.valueOf(Integer.parseInt(moisFacturation) - 2), 2);
                dateEffectiveDebutFacturation = "01." + theMoisDebutFacturation + "." + anneeFacturation;
                dateEffectiveFinFacturation = AFUtil
                        .getDateEndOfMonth("01." + moisFacturation + "." + anneeFacturation);

                if (CodeSystem.GENRE_ASS_PARITAIRE.equalsIgnoreCase(donneesFacturation.getGenreAssurance())) {
                    montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMasseAnnuelleCoti())) / 4;
                } else {
                    montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation
                            .getMontantTrimestrielCoti()));
                }

            } else {
                dateEffectiveDebutFacturation = "01." + moisFacturation + "." + anneeFacturation;
                dateEffectiveFinFacturation = AFUtil.getDateEndOfMonth(dateEffectiveDebutFacturation);

                if (CodeSystem.GENRE_ASS_PARITAIRE.equalsIgnoreCase(donneesFacturation.getGenreAssurance())) {
                    montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMasseAnnuelleCoti())) / 12;
                } else {
                    montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMontantMensuelCoti()));
                }
            }

            if (BSessionUtil.compareDateFirstGreater(sessionNaos, dateDebutCotisation, dateEffectiveDebutFacturation)) {
                dateEffectiveDebutFacturation = dateDebutCotisation;
            }

            if ((!JadeStringUtil.isIntegerEmpty(dateFinCotisation))
                    && BSessionUtil.compareDateFirstLower(sessionNaos, dateFinCotisation, dateEffectiveFinFacturation)) {

                dateEffectiveFinFacturation = dateFinCotisation;
            }

            if ((!JadeStringUtil.isIntegerEmpty(dateRetraite))
                    && BSessionUtil.compareDateFirstLower(sessionNaos, dateRetraite, dateEffectiveFinFacturation)) {

                if (CodeSystem.TYPE_AFFILI_NON_ACTIF.equals(donneesFacturation.getTypeAffiliation())) {
                    dateEffectiveFinFacturation = dateRetraite;
                    donneesFacturation.setIsRentier(new Boolean("true"));
                } else {
                    // si AC ou AC2 IND, on ne facture que avant age rente
                    if ((CodeSystem.TYPE_ASS_COTISATION_AC.equals(donneesFacturation.getAssurance().getTypeAssurance()) || CodeSystem.TYPE_ASS_COTISATION_AC2
                            .equals(donneesFacturation.getAssurance().getTypeAssurance()))
                            && CodeSystem.GENRE_ASS_PERSONNEL.equals(donneesFacturation.getAssurance()
                                    .getAssuranceGenre())) {
                        dateEffectiveFinFacturation = dateRetraite;
                    }
                }
            }

            if ((!JadeStringUtil.isIntegerEmpty(dateDeces))
                    && BSessionUtil.compareDateFirstLower(sessionNaos, dateDeces, dateEffectiveFinFacturation)) {

                dateEffectiveFinFacturation = dateDeces;
            }

            if (montant != 0.0) {
                montantList.add(new LineFacturation(donneesFacturation, anneeFacturation,
                        dateEffectiveDebutFacturation, dateEffectiveFinFacturation, 0.0, montant, 0.0));
            }

        }

        // Si le module de facturation n'est pas renseigné pour éviter que ça fasse des afacts manuels
        if (JadeStringUtil.isBlankOrZero(idModuleFacturation)) {
            if (isPersonnel) {
                idModuleFacturation = AFProcessFacturation.getIdModuleFacurationPersonnel(sessionNaos,
                        process.getTransaction(), typeFacturationPersonnelle);
            } else if (isRI_PC) {
                idModuleFacturation = AFProcessFacturation.getIdModuleFacurationRI_PC(sessionNaos,
                        process.getTransaction());
            } else {
                idModuleFacturation = AFProcessFacturation.getIdModuleFacurationParitaire(sessionNaos,
                        process.getTransaction());
            }
        }

        CalculCotisation calcul = new CalculCotisation();

        calcul.setAffilieNumero(affilieNumero);
        calcul.setPeriodiciteAff(periodiciteCoti);
        calcul.setIdExterneFacture(idExterneFacture);
        calcul.setIdSousTypeFacture(idSousTypeFacture);
        calcul.setIdModuleFacturation(idModuleFacturation);
        calcul.setNbException(nbException);
        calcul.setMontant(montant);
        calcul.setMontantList(montantList);
        calcul.setAssuranceException(assuranceException);

        return calcul;
    }

    protected static void calculerCotisationFFPP(BProcess process, AFProcessFacturationViewBean donneesFacturation,
            String anneeFacturation, List<LineFacturation> cotisationAffilie, BSession sessionNaos,
            List<LineFacturation> montantList, AFTauxAssurance tauxAssurance) throws Exception {
        double montant;
        if (AFParticulariteAffiliation.existeParticularite(process.getSession(), donneesFacturation.getAffiliationId(),
                CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL, JACalendar.todayJJsMMsAAAA())) {
            // l'affilié est sans personnel -> ne pas facturer
            process.getMemoryLog().logMessage(
                    FWMessageFormat.format((sessionNaos).getLabel("2010"), donneesFacturation.getCotisationId(),
                            donneesFacturation.getAssuranceId()), FWMessage.AVERTISSEMENT,
                    AFProcessFacturation.class.getName());
        } else {
            // recherche du nombre d'assuré
            String nbrAssures = donneesFacturation.getNbrAssures(anneeFacturation);
            if (nbrAssures != null) {
                AFTauxAssurance taux = tauxAssurance;
                if ((taux == null) || taux.isNew()) {
                    process.getMemoryLog().logMessage(sessionNaos.getLabel("2020"), FWMessage.AVERTISSEMENT,
                            AFProcessFacturation.class.getName());
                } else {
                    montant = Integer.parseInt(JANumberFormatter.deQuote(taux.getValeurEmployeur()))
                            * Double.parseDouble(JANumberFormatter.deQuote(nbrAssures));
                    // création de la ligne
                    LineFacturation lineFFPP = new LineFacturation(donneesFacturation, anneeFacturation, "", "", 0.0,
                            montant, 0.0);
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
                    if (cotisationAffilie.contains(lineFFPP) == false) {
                        montantList.add(lineFFPP);
                    }
                }
            } else {
                // l'affiliation est bien une FFPP mais le nombre
                // d'assuré n'est pas renseigné
                process.getMemoryLog().logMessage(
                        FWMessageFormat.format((sessionNaos).getLabel("2010"), donneesFacturation.getCotisationId(),
                                donneesFacturation.getAssuranceId()), FWMessage.AVERTISSEMENT,
                        AFProcessFacturation.class.getName());
            }
        }
    }

    protected static void calculerCotisationMontantFixe(AFProcessFacturationViewBean donneesFacturation,
            String anneeFacturation, String dateEffectiveDebutFacturation, String dateEffectiveFinFacturation,
            List<LineFacturation> montantList, int nbMoisFacturer, AFTauxAssurance tauxAssurance) {
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

            montantList.add(new LineFacturation(donneesFacturation, anneeFacturation, dateEffectiveDebutFacturation,
                    dateEffectiveFinFacturation, 0.0, montant, 0.0));

        }
    }

    protected static double calculerCotisationPersonnelle(AFProcessFacturationViewBean donneesFacturation,
            String anneeFacturation, BSession sessionNaos, String dateEffectiveDebutFacturation,
            String dateEffectiveFinFacturation, List<LineFacturation> montantList, int nbMoisFacturer) throws Exception {
        double montant;
        montantList.clear();

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
                CACompteur cnt = AFProcessFacturation.getCompteur(donneesFacturation.getAssuranceRubriqueId(),
                        donneesFacturation.getAffilieNumero(), anneeFacturation, sessionNaos);
                FWCurrency montantCompta = new FWCurrency(montant);
                if (cnt != null) {

                    montantCompta.sub(cnt.getCumulCotisation());

                }
                if (montantCompta.isNegative()) {

                } else {
                    montant = montantCompta.doubleValue();
                }
            }

            if (montant > 0.0) {
                montantList.add(new LineFacturation(donneesFacturation, anneeFacturation,
                        dateEffectiveDebutFacturation, dateEffectiveFinFacturation, 0.0, montant, 0.0));
            }
        }
        return montant;
    }

    protected static void calculerCotisationTauxFixe(AFProcessFacturationViewBean donneesFacturation,
            String anneeFacturation, String dateEffectiveDebutFacturation, String dateEffectiveFinFacturation,
            List<LineFacturation> montantList, int nbMoisFacturer, double masseMensuel, AFTauxAssurance tauxAssurance) {
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
                montantList.add(lineFactu);
            } else {
                // taux à 100%
                montantList.add(new LineFacturation(donneesFacturation, anneeFacturation,
                        dateEffectiveDebutFacturation, dateEffectiveFinFacturation, 0.0, montant, 0.0));
            }
        }
    }

    protected static void calculerCotisationTauxVariable(BProcess process,
            AFProcessFacturationViewBean donneesFacturation, String anneeFacturation,
            String dateEffectiveDebutFacturation, String dateEffectiveFinFacturation,
            List<LineFacturation> montantList, int nbMoisFacturer, double masseMensuel, AFTauxAssurance tauxAssurance)
            throws Exception {
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

                montant = (Double.parseDouble(JANumberFormatter.deQuote(montantAnnuel)) / 12) * nbMoisFacturer;
                LineFacturation lineFactu = new LineFacturation(donneesFacturation, anneeFacturation,
                        dateEffectiveDebutFacturation, dateEffectiveFinFacturation, masseMensuel * nbMoisFacturer,
                        montant, Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())));
                if (!tauxAssurance.isAffichageTaux()) {
                    lineFactu.setTauxCache(true);

                }
                montantList.add(lineFactu);

            } else {
                String montantAnnuel = tauxVarUtil.getMontantCotisation(donneesFacturation.getMasseAnnuelleCoti(),
                        dateEffectiveFinFacturation, tauxAssurance, app.isCotisationMinimale());

                montant = (Double.parseDouble(JANumberFormatter.deQuote(montantAnnuel)) / 12) * nbMoisFacturer;
                LineFacturation lineFactu = new LineFacturation(donneesFacturation, anneeFacturation,
                        dateEffectiveDebutFacturation, dateEffectiveFinFacturation, masseMensuel * nbMoisFacturer,
                        montant, Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())));
                if (!tauxAssurance.isAffichageTaux()) {
                    lineFactu.setTauxCache(true);

                }
                montantList.add(lineFactu);
            }

        }
    }

    /**
     * Permet de récuperer un compte annexe Date de création : (22.05.2003 10:12:15)
     * 
     * @return globaz.osiris.db.comptes.CACompteAnnexe
     */
    public static CACompteAnnexe getCompteAnnexe(String numAff, BSession sessionNaos) throws Exception {
        // Si _compteAnnexe n'est pas déjà instancié
        // if (_compteAnnexe == null) {
        // Chargement du manager
        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.setSession(sessionNaos);
        // manager.setForIdTiers(getAffiliation().getIdTiers());
        manager.setForIdRole(CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(sessionNaos.getApplication()));
        manager.setForIdExterneRole(numAff);
        manager.find();
        if (!manager.isEmpty()) {
            AFProcessFacturation._compteAnnexe = (CACompteAnnexe) manager.getEntity(0);
            /*
             * }else{ throw new Exception(getSession().getLabel("DECL_ERREUR_LECTURE_CA")); }
             */
        }
        // }
        return AFProcessFacturation._compteAnnexe;
    }

    public static CACompteur getCompteur(String rubriqueId, String numAff, String dateFin, BSession sessionNaos)
            throws Exception {
        // Chargement du compteur
        CACompteur compteur = new CACompteur();
        compteur.setSession(sessionNaos);
        compteur.setAlternateKey(CACompteur.AK_CPTA_RUB_ANNEE);
        compteur.setAnnee(dateFin);
        if (AFProcessFacturation.getCompteAnnexe(numAff, sessionNaos) == null) {
            return null;
        }
        compteur.setIdCompteAnnexe(AFProcessFacturation.getCompteAnnexe(numAff, sessionNaos).getIdCompteAnnexe());
        compteur.setIdRubrique(rubriqueId);
        compteur.retrieve();
        if (!compteur.isNew()) {
            return compteur;
        } else {
            return null;
        }
    }

    public static final String getIdModuleFacurationLAE(BSession session, BTransaction transaction) {
        if (AFProcessFacturation.ID_MOD_FACURATION_LAE == null) {
            AFProcessFacturation.ID_MOD_FACURATION_LAE = ServicesFacturation.getIdModFacturationByType(session,
                    transaction, FAModuleFacturation.CS_MODULE_PERIODIQUE_LAE);
        }
        return AFProcessFacturation.ID_MOD_FACURATION_LAE;
    }

    /**
     * Renvoie idModuleFacturation pour la paritaire
     */
    public static final String getIdModuleFacurationParitaire(BSession session, BTransaction transaction) {
        if (AFProcessFacturation.ID_MOD_FACURATION_PARITAIRE == null) {
            AFProcessFacturation.ID_MOD_FACURATION_PARITAIRE = ServicesFacturation.getIdModFacturationByType(session,
                    transaction, FAModuleFacturation.CS_MODULE_PERIODIQUE_PARITAIRE);
        }
        return AFProcessFacturation.ID_MOD_FACURATION_PARITAIRE;
    }

    /**
     * Renvoi idModuleFacturation pour la personnelle
     */
    public static final String getIdModuleFacurationPersonnel(BSession session, BTransaction transaction,
            int typeFacturationPersonnelle) {

        if (AFProcessFacturation.ID_MOD_FACURATION_PERSONNEL == null) {
            if (typeFacturationPersonnelle == AFProcessFacturation.PERIODIQUE_COT_PERS_IND) {
                AFProcessFacturation.ID_MOD_FACURATION_PERSONNEL = ServicesFacturation.getIdModFacturationByType(
                        session, transaction, FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS_IND);
            } else if (typeFacturationPersonnelle == AFProcessFacturation.PERIODIQUE_COT_PERS_NAC) {
                AFProcessFacturation.ID_MOD_FACURATION_PERSONNEL = ServicesFacturation.getIdModFacturationByType(
                        session, transaction, FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS_NAC);
            } else {
                AFProcessFacturation.ID_MOD_FACURATION_PERSONNEL = ServicesFacturation.getIdModFacturationByType(
                        session, transaction, FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS);
            }
        }
        return AFProcessFacturation.ID_MOD_FACURATION_PERSONNEL;
    }

    /**
     * Renvoi idModuleFacturation de la périodique RI_PC
     * 
     * @param session
     * @param transaction
     * @return
     */
    public static final String getIdModuleFacurationRI_PC(BSession session, BTransaction transaction) {
        if (AFProcessFacturation.ID_MOD_FACURATION_RI_PC == null) {
            AFProcessFacturation.ID_MOD_FACURATION_RI_PC = ServicesFacturation.getIdModFacturationByType(session,
                    transaction, FAModuleFacturation.CS_MODULE_PERIODIQUE_RI_PC);
        }
        return AFProcessFacturation.ID_MOD_FACURATION_RI_PC;
    }

    /**
     * Determine si les données sont celles d'une cotisation Paritaire.
     * 
     * @param data
     * @return
     */
    private static boolean isCotisationParitaire(AFProcessFacturationViewBean data) {
        boolean result = false;
        if (data.getCodeFacturation().equalsIgnoreCase(CodeSystem.CODE_FACTU_MONTANT_LIBRE)) {
            return result;
        }
        // soit la masseAnnuel est non nulle soit c'est une assurance FFPP
        if (Double.parseDouble(data.getMasseAnnuelleCoti()) > 0.0) {
            result = true;
        } else if (CodeSystem.TYPE_ASS_FFPP.equals(data.getTypeAssurance())) {
            result = true;
        } else if (CodeSystem.MOTIF_FIN_EXCEPTION.equals(data.getMotifFinCoti())) {
            result = true;
        } else if (CodeSystem.TYPE_ASS_LAE.equals(data.getTypeAssurance())) {
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

    private boolean facturerLAE = false;

    private boolean facturerParitaire = false;

    private boolean facturerPersonnel = false;
    private boolean facturerRI_PC = false;
    private String numAffilieTest = null;
    private globaz.musca.api.IFAPassage passage = null;
    private int typeFacturationPersonnelle = 0;

    /**
     * Constructeur de AFProcessFacturation.
     */
    public AFProcessFacturation() {
        super();
    }

    /**
     * Constructeur de AFProcessFacturation.
     * 
     * @param parent
     *            globaz.framework.process.FWProcess
     */
    public AFProcessFacturation(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution.
     */
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

    protected void creerLigneDeFacture(String idPassage, CalculCotisation calculCotisation,
            FAEnteteFacture enteteFacture, LineFacturation aLine) throws Exception {
        FAAfact lineFacture = new FAAfact();
        lineFacture.setISession(getSession());
        lineFacture.setIdEnteteFacture(enteteFacture.getIdEntete());
        lineFacture.setIdPassage(idPassage);
        lineFacture.setIdModuleFacturation(calculCotisation.getIdModuleFacturation());
        lineFacture.setIdTypeAfact(CodeSystem.TYPE_FACT_FACT_STANDARD);
        lineFacture.setNonImprimable(Boolean.FALSE);
        lineFacture.setNonComptabilisable(Boolean.FALSE);
        lineFacture.setAQuittancer(Boolean.FALSE);
        lineFacture.setAnneeCotisation(aLine.getAnneeCotisation());
        lineFacture.setLibelle(aLine.getLibelle());
        lineFacture.setIdRubrique(aLine.getIdRubrique());
        lineFacture.setDebutPeriode(aLine.getDebutPeriode());
        lineFacture.setFinPeriode(aLine.getFinPeriode());
        lineFacture.setMasseFacture(Double.toString(JANumberFormatter.round(aLine.getMasse(), 0.05, 2,
                JANumberFormatter.NEAR)));
        lineFacture.setMontantFacture(Double.toString(JANumberFormatter.round(aLine.getMontant(), 0.05, 2,
                JANumberFormatter.NEAR)));
        lineFacture.setTauxFacture(Double.toString(aLine.getTaux()));
        // ajout de la caisse métier
        String caisseMetier = aLine.getCaisse();
        if (caisseMetier == null) {
            caisseMetier = "";
        }
        lineFacture.setNumCaisse(caisseMetier);
        // indication si taux à cacher
        lineFacture.setAffichtaux(new Boolean(!aLine.isTauxCache()));
        lineFacture.add(getTransaction());
    }

    protected void creerNouvelleEntete(String idPassage, AFProcessFacturationViewBean donneesFacturation,
            CalculCotisation calculCotisation, FAEnteteFacture enteteFacture, String roleCoti, int idFacturationExt)
            throws Exception {
        enteteFacture.setIdTiers(donneesFacturation.getIdTiers());
        enteteFacture.setISession(getSession());
        enteteFacture.setIdPassage(idPassage);
        enteteFacture.setIdTypeFacture("1");
        enteteFacture.setIdRole(roleCoti);
        enteteFacture.setIdExterneRole(calculCotisation.getAffilieNumero());
        if (idFacturationExt == 0) {
            // aucune entête trouvée précédemment
            enteteFacture.setIdExterneFacture(calculCotisation.getIdExterneFacture());
        } else {
            // incrémenter l'id externe facturation de la
            // dernière en-tête trouvée
            enteteFacture.setIdExterneFacture(String.valueOf(idFacturationExt + 1));
        }
        enteteFacture.setIdSousType(calculCotisation.getIdSousTypeFacture());
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
        enteteFacture.add(getTransaction());
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
            BSession muscaSession = getSession(); // (BSession)passage.getISession();
            FAApplication muscaApp = (FAApplication) muscaSession.getApplication();
            BSession naosSession = (BSession) muscaApp.getSessionNaos(muscaSession);

            manager = new AFProcessFacturationManager();
            manager.setISession(naosSession);

            String idPassage = passage.getIdPassage();
            String datePeriode = passage.getDatePeriode();
            String moisFacturation = datePeriode.substring(0, 2);
            String anneeFacturation = datePeriode.substring(3);

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

            // ************************************************************
            // Parcourir toutes les Cotisations a facturer
            // ************************************************************
            AFProcessFacturationViewBean donneesFacturation = null;

            String previousKey = null;

            // stoque toutes les lignes de factures pour un même affilié
            List<LineFacturation> cotisationAffilie = new ArrayList<LineFacturation>();
            CalculCotisation calculCotisation = null;
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

            // ****************************************************************
            // POUR CHAQUE COTISATION
            // ****************************************************************
            while ((donneesFacturation = (AFProcessFacturationViewBean) manager.cursorReadNext(statement)) != null) {

                // PO 9032
                if (donneesFacturation.getMotifFinCoti().equals(CodeSystem.MOTIF_FIN_EXCEPTION)
                        && AFProcessFacturation.isCotisationParitaire(donneesFacturation) && isFacturerParitaire()) {
                    if (!JadeStringUtil.isEmpty(donneesFacturation.getDateFinAffiliation())
                            && BSessionUtil.compareDateFirstGreater(getSession(), donneesFacturation.getDateFinCoti(),
                                    donneesFacturation.getDateFinAffiliation())) {
                        continue;
                    }
                }

                // les données sont triées par tiers
                // Si le tiers a changé, on inscrit tous les afacts de l'entete
                // précédant stockés dans cotisationAffilié
                if ((previousKey != null)
                        && !previousKey.equals(donneesFacturation.getIdTiers() + ","
                                + donneesFacturation.getIdPlanAffiliation())) {

                    if (enteteFacture != null) {
                        for (int i = 0; i < cotisationAffilie.size(); i++) {
                            LineFacturation aLine = cotisationAffilie.get(i);

                            creerLigneDeFacture(idPassage, calculCotisation, enteteFacture, aLine);
                            nbLineFactureToAdd++;
                        }
                    }

                    // Validation finale
                    if (!getTransaction().hasErrors()) {
                        getTransaction().commit();

                        nbEnteteOK += nbEnteteToAdd;
                        nbLineFactureOK += nbLineFactureToAdd;
                    } else {
                        rollbackTransaction(getSession().getLabel("691") + affilieNumero);
                        getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());

                        nbEnteteError += nbEnteteToAdd;
                        nbLineFactureError += nbLineFactureToAdd;
                    }

                    enteteFacture = null;
                    cotisationAffilie.clear();
                    nbEnteteToAdd = 0;
                    nbLineFactureToAdd = 0;

                    // Sortie si erreur fatale
                    if (getMemoryLog().isOnFatalLevel()) {
                        return false;
                    }
                } // end if

                calculCotisation = AFProcessFacturation.calculerCotisation(this, donneesFacturation, anneeFacturation,
                        moisFacturation, isFacturerParitaire(), isFacturerPersonnel(), isFacturerRI_PC(),
                        isFacturerLAE(), cotisationAffilie, getTypeFacturationPersonnelle(), idPassage);
                nbException += calculCotisation.getNbException();

                // recherche du rôle pour la coti en cours
                String roleCoti = findRoleAffilie(donneesFacturation);

                affilieNumero = calculCotisation.getAffilieNumero();

                if (calculCotisation.getMontant() != 0.0) {

                    // ******************************************************************
                    // Insertion de l'en-tete facture, Si un nouveau tier
                    // ******************************************************************
                    if ((previousKey == null)
                            || ((previousKey != null) && !previousKey.equals(donneesFacturation.getIdTiers() + ","
                                    + donneesFacturation.getIdPlanAffiliation()))) {

                        // on vérifie que l'entete n'existe pas déjà ou cet
                        // affilié
                        // clé unique: idPassage, idRole, idExterneRole,
                        // idTypeFacture, idExterneFacture
                        enteteFacture = new FAEnteteFacture();

                        FAEnteteFactureManager entete = new FAEnteteFactureManager();
                        entete.setSession(getSession());
                        entete.setForIdPassage(idPassage);
                        // recherche des en-tête de la même période (DGI
                        // 21.06.07)
                        entete.setLikeIdExterneFacture(calculCotisation.getIdExterneFacture().substring(0, 6));
                        entete.setForIdExterneRole(calculCotisation.getAffilieNumero());
                        entete.setForIdTiers(donneesFacturation.getIdTiers());
                        entete.find();
                        boolean enteteFound = false;
                        int idFacturationExt = 0;
                        // recherche sur les en-tête déjà existantes
                        for (int iEntete = 0; (iEntete < entete.size()) && !enteteFound; iEntete++) {
                            FAEnteteFacture ef = (FAEnteteFacture) entete.getEntity(iEntete);
                            // recherche et sauvegarde du dernier id externe
                            // facturation
                            int idFact = Integer.parseInt(ef.getIdExterneFacture());
                            if (idFacturationExt < idFact) {
                                idFacturationExt = idFact;
                            }
                            if (roleCoti.equals(ef.getIdRole())
                                    && donneesFacturation.getLibelleFacture().equals(ef.getLibelle())) {
                                // il existe une en-tête avec le bon rôle et les
                                // mêmes domaines -> l'utiliser
                                enteteFound = true;
                                enteteFacture.setSession(getSession());
                                enteteFacture.setIdEntete(ef.getIdEntete());
                                enteteFacture.retrieve();
                                if (donneesFacturation.getIsRentier().booleanValue()) {
                                    // si retraite, le renseigner dans le
                                    // décompte
                                    enteteFacture.setEstRentierNa(donneesFacturation.getIsRentier());
                                    enteteFacture.update(getTransaction());
                                }
                            }
                        } // for
                        if (!enteteFound) {
                            // n'existe pas --> on ajoute l'entete de facture
                            creerNouvelleEntete(idPassage, donneesFacturation, calculCotisation, enteteFacture,
                                    roleCoti, idFacturationExt);
                            nbEnteteToAdd++;
                        }
                    }

                    // ******************************************************************
                    // Creation de(s) ligne(s) de facturation
                    // ******************************************************************
                    for (int i = calculCotisation.getMontantList().size() - 1; i >= 0; i--) {
                        LineFacturation aLine = calculCotisation.getMontantList().get(i);
                        cotisationAffilie.add(aLine);
                    }

                    previousKey = donneesFacturation.getIdTiers() + "," + donneesFacturation.getIdPlanAffiliation();
                } else {
                    nbMontantZero++;
                }
                nbCotisation++;
            } // end while

            if (enteteFacture != null) {
                for (int i = 0; i < cotisationAffilie.size(); i++) {
                    LineFacturation aLine = cotisationAffilie.get(i);

                    creerLigneDeFacture(idPassage, calculCotisation, enteteFacture, aLine);
                    nbLineFactureToAdd++;
                }
            }

            // Validation finale
            if (!getTransaction().hasErrors()) {
                getTransaction().commit();

                nbEnteteOK += nbEnteteToAdd;
                nbLineFactureOK += nbLineFactureToAdd;
            } else {
                rollbackTransaction(getSession().getLabel("691") + calculCotisation.getAffilieNumero());
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());

                nbEnteteError += nbEnteteToAdd;
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

    protected String findRoleAffilie(AFProcessFacturationViewBean donneesFacturation) throws Exception {
        String roleCoti = CodeSystem.ROLE_AFFILIE;
        if (CodeSystem.GENRE_ASS_PERSONNEL.equals(donneesFacturation.getGenreAssurance())) {
            // assurance personnelle
            roleCoti = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication());
        }
        if (CodeSystem.GENRE_ASS_PARITAIRE.equals(donneesFacturation.getGenreAssurance())) {
            // assurance personnelle
            roleCoti = CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(getSession().getApplication());
        }
        return roleCoti;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        return "";
    }

    public String getNumAffilieTest() {
        return numAffilieTest;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.05.2003 11:48:36)
     * 
     * @return globaz.musca.api.IFAPassage
     */
    public globaz.musca.api.IFAPassage getPassage() {
        return passage;
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
            e.printStackTrace();
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

    public boolean isFacturerLAE() {
        return facturerLAE;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 14:42:58)
     * 
     * @return boolean
     */
    public boolean isFacturerParitaire() {
        return facturerParitaire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 14:44:20)
     * 
     * @return boolean
     */
    public boolean isFacturerPersonnel() {
        return facturerPersonnel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 14:44:20)
     * 
     * @return boolean
     */
    public boolean isFacturerRI_PC() {
        return facturerRI_PC;
    }

    /**
     * Renvoie la Job Queue à utiliser pour soumettre le process.
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.06.2003 13:59:19)
     * 
     * @param message
     *            java.lang.String
     */
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

    public void setFacturerLAE(boolean newFacturerLAE) {
        facturerLAE = newFacturerLAE;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 14:42:58)
     * 
     * @param newFacturerParitaire
     *            boolean
     */
    public void setFacturerParitaire(boolean newFacturerParitaire) {
        facturerParitaire = newFacturerParitaire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 14:44:20)
     * 
     * @param newFacturerPersonnel
     *            boolean
     */
    public void setFacturerPersonnel(boolean newFacturerPersonnel) {
        facturerPersonnel = newFacturerPersonnel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 14:44:20)
     * 
     * @param newFacturerPersonnel
     *            boolean
     */
    public void setFacturerRI_PC(boolean newFacturerRI_PC) {
        facturerRI_PC = newFacturerRI_PC;
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

    public void setNumAffilieTest(String numAffilieTest) {
        this.numAffilieTest = numAffilieTest;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.05.2003 11:48:36)
     * 
     * @param newPassage
     *            globaz.musca.api.IFAPassage
     */
    public void setPassage(globaz.musca.api.IFAPassage newPassage) {
        passage = newPassage;
    }

    public void setTypeFacturationPersonnelle(int typeFactuationPersonnelle) {
        typeFacturationPersonnelle = typeFactuationPersonnelle;
    }

}
