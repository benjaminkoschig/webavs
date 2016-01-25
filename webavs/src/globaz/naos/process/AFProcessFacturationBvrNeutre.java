package globaz.naos.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.naos.application.AFApplication;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.processFacturation.AFProcessFacturationManager;
import globaz.naos.db.processFacturation.AFProcessFacturationViewBean;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFAgeRente;
import globaz.naos.util.AFUtil;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperationBulletinNeutre;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.utils.CAUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Process pour la facturation des affiliés avec bulletin neutre
 * 
 * @author: mmu, sau
 */

public final class AFProcessFacturationBvrNeutre extends BProcess {
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
        private String idRole = null;

        private String idSousTypeFacture = null;
        private double montant;

        private List<LineFacturation> montantList = null;

        private int nbException = 0;

        private String periodiciteAff = null;

        private BSession sessionNaos = null;

        private BSession sessionOsiris = null;

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

        public String getIdRole() {
            return idRole;
        }

        // public String getAssuranceId() {
        // return assuranceId;
        // }
        //
        // public void setAssuranceId(String string) {
        // assuranceId = string;
        // }

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

        public BSession getSessionNaos() {
            return sessionNaos;
        }

        public BSession getSessionOsiris() {
            return sessionOsiris;
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

        public void setIdRole(String idRole) {
            this.idRole = idRole;
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

        public void setSessionNaos(BSession sessionNaos) {
            this.sessionNaos = sessionNaos;
        }

        public void setSessionOsiris(BSession sessionOsiris) {
            this.sessionOsiris = sessionOsiris;
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

            // DGI janvier 2008: est-ce nécessaire de supprimer la période???
            // mis en commentaire... car nécessaire pour le calcul de
            // remboursement des assistés
            /*
             * // si cotisation annuelle, ne pas spécifier de date if(CodeSystem.
             * PERIODICITE_ANNUELLE.equals(donneesFacturation.getPeriodiciteCoti ())) { this.debutPeriode = "";
             * this.finPeriode = ""; }
             */

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

    private static HashMap<String, AFAgeRente> listRente = new HashMap<String, AFAgeRente>();

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
    public static final CalculCotisation calculerCotisation(CalculCotisation calculCotisation, BProcess process,
            AFProcessFacturationViewBean donneesFacturation, String anneeFacturation, String moisFacturation,
            List<LineFacturation> cotisationAffilie, String IdModuleFacturationBvrNeutre) throws Exception {

        String affilieNumero = null;
        String periodiciteAff = null;
        String noTrimestre = null;
        String idExterneFacture = null;
        String idSousTypeFacture = null;

        // ************************************************************
        // Initialisation et calcule de l'Age de Rente
        // ************************************************************
        String periodeCalcul = "31." + moisFacturation + "." + anneeFacturation;
        AFAgeRente ageRente;

        if (AFProcessFacturationBvrNeutre.listRente.containsKey(periodeCalcul)) {
            ageRente = AFProcessFacturationBvrNeutre.listRente.get(periodeCalcul);
        } else {
            ageRente = new AFAgeRente();
            ageRente.initDateRente(calculCotisation.getSessionNaos(), periodeCalcul);
            AFProcessFacturationBvrNeutre.listRente.put(periodeCalcul, ageRente);
        }

        String debutPeriodeFacturation = null;
        String finPeriodeFacturation = null;

        String dateEffectiveDebutFacturation = null;
        String dateEffectiveFinFacturation = null;
        String dateDebutCotisation = null;
        String dateFinCotisation = null;
        String dateRetraite = null;
        String dateDeces = null;

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
        periodiciteAff = donneesFacturation.getPeriodiciteAff();

        if (periodiciteAff.equals(CodeSystem.PERIODICITE_MENSUELLE)
                && !donneesFacturation.getPeriodiciteCoti().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {

            // Période de Facturation
            debutPeriodeFacturation = "01." + moisFacturation + "." + anneeFacturation;
            finPeriodeFacturation = AFUtil.getDateEndOfMonth(debutPeriodeFacturation);

            // Code de facturation
            idSousTypeFacture = "2270" + moisFacturation;
        } else if (periodiciteAff.equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)
                || donneesFacturation.getPeriodiciteCoti().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
            // Période de Facturation
            if (Integer.parseInt(moisFacturation) - 2 < 10) {
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
            }
            idSousTypeFacture = "22704" + noTrimestre;

        } else if (periodiciteAff.equals(CodeSystem.PERIODICITE_ANNUELLE)) {
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
            idSousTypeFacture = "22704" + noTrimestre;
        }
        idExterneFacture = CAUtil.creerNumeroSectionUnique(calculCotisation.getSessionOsiris(),
                process.getTransaction(), calculCotisation.getIdRole(), affilieNumero,
                APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE, anneeFacturation, idSousTypeFacture);

        // ************************************************************
        // Calcul de la periode exacte de facturation
        // ************************************************************
        // Date début : le plus grand entre début Facturation, début Cotisation
        // Date fin : le plus petit entre fin Facturation, fin Cotisation,
        // Retraite, Décés

        // Date de début
        dateEffectiveDebutFacturation = debutPeriodeFacturation;

        dateDebutCotisation = AFUtil.getDateBeginingOfMonth(donneesFacturation.getDateDebutCoti());
        if (BSessionUtil.compareDateFirstGreater(calculCotisation.getSessionNaos(), dateDebutCotisation,
                dateEffectiveDebutFacturation)) {
            dateEffectiveDebutFacturation = dateDebutCotisation;
        }

        // Date de Fin
        dateEffectiveFinFacturation = finPeriodeFacturation;

        dateFinCotisation = AFUtil.getDateEndOfMonth(donneesFacturation.getDateFinCoti());
        dateRetraite = ageRente.getDateRente(donneesFacturation.getDateNaissance(), donneesFacturation.getSexe());
        dateDeces = AFUtil.getDateEndOfMonth(donneesFacturation.getDateDeces());

        if ((!JadeStringUtil.isIntegerEmpty(dateFinCotisation))
                && BSessionUtil.compareDateFirstLower(calculCotisation.getSessionNaos(), dateFinCotisation,
                        dateEffectiveFinFacturation)) {

            dateEffectiveFinFacturation = dateFinCotisation;
        }
        if ((!JadeStringUtil.isIntegerEmpty(dateRetraite))
                && BSessionUtil.compareDateFirstLower(calculCotisation.getSessionNaos(), dateRetraite,
                        dateEffectiveFinFacturation)) {

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
                && BSessionUtil.compareDateFirstLower(calculCotisation.getSessionNaos(), dateDeces,
                        dateEffectiveFinFacturation)) {

            dateEffectiveFinFacturation = dateDeces;
        }

        // System.out.println("date début effective: "+dateEffectiveDebutFacturation);
        // System.out.println("date fin effective: "+dateEffectiveFinFacturation);
        // ************************************************************
        // Calcul le nombre de mois a facturer
        // ************************************************************
        int nbMoisFacturer = AFUtil.nbMoisPeriode(calculCotisation.getSessionNaos(), dateEffectiveDebutFacturation,
                dateEffectiveFinFacturation);

        // ************************************************************
        // Calculs des montants a facturer
        // ************************************************************

        // *******************************
        // PARITAIRE
        // *******************************

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

        double masseMensuel = 0;
        if (!JadeStringUtil.isIntegerEmpty(donneesFacturation.getIdReferenceAssurance())) {
            masseMensuel = 0;
        }

        // Recherche des taux par l'intermédiaire de la cotisation
        AFCotisation cotisation = new AFCotisation();
        cotisation.setCotisationId(donneesFacturation.getCotisationId());
        cotisation.retrieve(process.getTransaction());
        AFTauxAssurance tauxAssurance = cotisation.findTaux(dateEffectiveFinFacturation,
                donneesFacturation.getMasseAnnuelleCoti(), true);
        if (tauxAssurance != null) {

            // *****************************************************
            // Cas particuliers pour les assurances qui se calculent autrement
            // que taux*masse
            //
            // *****************************************************
            if (tauxAssurance.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_TAUX)) {
                // System.out.println("nbMoisFacturer: "+nbMoisFacturer);
                // System.out.println("masseMensuelle: "+masseMensuel);
                if (nbMoisFacturer > 0) {
                    // Un seul Taux pour la periode de Facturation
                    double tauxCalcul = Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal()))
                            / Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getFraction()));
                    montant = masseMensuel * nbMoisFacturer * tauxCalcul;
                    if (tauxCalcul != 1) {
                        // taux différent de 100%

                        LineFacturation lineFactu = new LineFacturation(donneesFacturation, anneeFacturation,
                                dateEffectiveDebutFacturation, dateEffectiveFinFacturation, masseMensuel
                                        * nbMoisFacturer, montant, Double.parseDouble(JANumberFormatter
                                        .deQuote(tauxAssurance.getValeurTotal())));
                        // taux à cacher?
                        if (!tauxAssurance.isAffichageTaux()) {
                            // if
                            // (CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equals(tauxAssurance.getGenreValeur())
                            // &&
                            // "true".equals(sessionNaos.getApplication().getProperty(AFApplication.PROPERTY_IS_TAUX_PAR_PALIER)))
                            // {
                            // if(CodeSystem.TYPE_TAUX_MOYEN.equals(tauxAssurance.getTypeId()))
                            // {
                            lineFactu.setTauxCache(true);
                        }
                        montantList.add(lineFactu);
                    } else {
                        // taux à 100%
                        montantList.add(new LineFacturation(donneesFacturation, anneeFacturation,
                                dateEffectiveDebutFacturation, dateEffectiveFinFacturation, 0.0, montant, 0.0));
                    }
                }
                // *****************************************************
                // Taux Variable
                // *****************************************************
            } else if (tauxAssurance.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE)) {

                if (nbMoisFacturer > 0) {
                    // chargement de l'utilitaire de calcul
                    // HNA 31.08.09 AFTauxVariableUtil tauxVarUtil =
                    // AFTauxVariableUtil.getInstance(donneesFacturation.getAssuranceId());
                    // HNA 31.08.09 AFApplication app =
                    // (AFApplication)GlobazServer.getCurrentSystem().getApplication(AFApplication.DEFAULT_APPLICATION_NAOS);
                    // HNA 31.08.09 String montantAnnuel =
                    // tauxVarUtil.getMontantCotisation(donneesFacturation.getMasseAnnuelleCoti(),
                    // dateEffectiveFinFacturation, tauxAssurance,
                    // app.isCotisationMinimale());
                    String montantAnnuel = "0";
                    montant = Double.parseDouble(JANumberFormatter.deQuote(montantAnnuel)) / 12 * nbMoisFacturer;
                    LineFacturation lineFactu = new LineFacturation(donneesFacturation, anneeFacturation,
                            dateEffectiveDebutFacturation, dateEffectiveFinFacturation, masseMensuel * nbMoisFacturer,
                            montant, Double.parseDouble(JANumberFormatter.deQuote(tauxAssurance.getValeurTotal())));
                    if (!tauxAssurance.isAffichageTaux()) {
                        // if
                        // (CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equals(tauxAssurance.getGenreValeur())
                        // &&
                        // "true".equals(sessionNaos.getApplication().getProperty(AFApplication.PROPERTY_IS_TAUX_PAR_PALIER)))
                        // {
                        // if(CodeSystem.TYPE_TAUX_MOYEN.equals(tauxAssurance.getTypeId()))
                        // {
                        lineFactu.setTauxCache(true);
                    }
                    montantList.add(lineFactu);
                }
                // *****************************************************
                // Montant
                // *****************************************************
            } else if (tauxAssurance.getGenreValeur().equals(CodeSystem.GEN_VALEUR_ASS_MONTANT)) {

                if (nbMoisFacturer > 0) {

                    double montantMensuel = 0.0;
                    // Un seul Montant pour la période de Facturation.
                    montant = nbMoisFacturer * montantMensuel;

                    montantList.add(new LineFacturation(donneesFacturation, anneeFacturation,
                            dateEffectiveDebutFacturation, dateEffectiveFinFacturation, 0.0, montant, 0.0));

                }
            }
        } else {
            // Il n' y a pas de taux
            // Si pour l'assurance, le type de calcul est "Cotisation"
            // Le montant de la cotisation est égal à la masse de la cotisation
            // au
            // "Prorata Temporis".

            if (nbMoisFacturer > 0) {
                if (donneesFacturation.getTypeCalcul().equals(CodeSystem.TYPE_CALCUL_COTISATION)) {
                    montant = Double.parseDouble(JANumberFormatter.deQuote(donneesFacturation.getMasseAnnuelleCoti()))
                            / 12 * nbMoisFacturer;

                    montantList.add(new LineFacturation(donneesFacturation, anneeFacturation,
                            dateEffectiveDebutFacturation, dateEffectiveFinFacturation, 0.0, montant, 0.0));
                } else {
                    process.getMemoryLog().logMessage(
                            FWMessageFormat.format(calculCotisation.getSessionNaos().getLabel("1800"),
                                    donneesFacturation.getCotisationId(), donneesFacturation.getAssuranceId()),
                            FWMessage.AVERTISSEMENT, AFProcessFacturationBvrNeutre.class.getName());
                }
            }
        }

        montant = 0.0;
        for (int i = 0; i < montantList.size(); i++) {
            montant += (montantList.get(i)).montant;
        }
        // System.out.println("montant process fact:"+ montant);
        // }

        CalculCotisation calcul = new CalculCotisation();

        calcul.setAffilieNumero(affilieNumero);
        calcul.setPeriodiciteAff(periodiciteAff);
        calcul.setIdExterneFacture(idExterneFacture);
        calcul.setIdSousTypeFacture(idSousTypeFacture);
        calcul.setIdModuleFacturation(IdModuleFacturationBvrNeutre);
        calcul.setNbException(nbException);
        calcul.setMontant(montant);
        calcul.setMontantList(montantList);
        calcul.setAssuranceException(assuranceException);

        return calcul;
    }

    private String idModuleFacturationBvrNeutre = "";

    private String numAffilieTest = null;
    private globaz.musca.api.IFAPassage passage = null;

    /**
     * Constructeur de AFProcessFacturation.
     */
    public AFProcessFacturationBvrNeutre() {
        super();
    }

    /**
     * Constructeur de AFProcessFacturation.
     * 
     * @param parent
     *            globaz.framework.process.FWProcess
     */
    public AFProcessFacturationBvrNeutre(BProcess parent) {
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
        BSession sessionNaos = null;
        BSession sessionOsiris = null;
        String applicationId = getSession().getApplicationId();

        try {
            BSession muscaSession = getSession(); // (BSession)passage.getISession();
            FAApplication muscaApp = (FAApplication) muscaSession.getApplication();
            BSession naosSession = (BSession) muscaApp.getSessionNaos(muscaSession);
            if ("MUSCA".equalsIgnoreCase(applicationId)) {
                FAApplication applicationMusca = (FAApplication) getSession().getApplication();
                sessionNaos = (BSession) applicationMusca.getSessionNaos(getSession());
            } else {
                sessionNaos = getSession();
            }
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

            // ************************************************************
            // Initialisation des paramètres pour la recherche des Cotisations
            // ************************************************************

            manager.setForDateFacturation(datePeriode);

            // ajout d'un filtre pour un assuré pour tests
            if (!JadeStringUtil.isEmpty(numAffilieTest)) {
                manager.setForAffilieNumero(numAffilieTest);
            }

            manager.setForTypeAffiliation(AFProcessFacturationManager.TYPE_AFFILIATION_PARITAIRE);
            manager.setInCodeFacturation(CodeSystem.CODE_FACTU_MONTANT_LIBRE);

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
            CalculCotisation calculCotisation = new CalculCotisation();
            FAEnteteFacture enteteFacture = null;

            String affilieNumero = null;

            sessionOsiris = new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS);
            getSession().connectSession(sessionOsiris);

            int nbCotisation = 0;
            int nbEnteteToAdd = 0;
            int nbLineFactureToAdd = 0;
            int nbEnteteOK = 0;
            int nbLineFactureOK = 0;
            int nbEnteteError = 0;
            int nbLineFactureError = 0;
            int nbException = 0;

            String roleCoti = CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                    getSession().getApplication());
            // ****************************************************************
            // POUR CHAQUE COTISATION
            // ****************************************************************
            while ((donneesFacturation = (AFProcessFacturationViewBean) manager.cursorReadNext(statement)) != null) {
                // Prendre seulement les assurances paritaires
                if (CodeSystem.GENRE_ASS_PARITAIRE.equals(donneesFacturation.getGenreAssurance())) {
                    // les données sont triées par tiers
                    // Si le tiers a changé, on inscrit tous les afacts de
                    // l'entete précédant stockés dans cotisationAffilié
                    if ((previousKey != null)
                            && !previousKey.equals(donneesFacturation.getIdTiers() + ","
                                    + donneesFacturation.getIdPlanAffiliation())) {
                        if (enteteFacture != null) {
                            for (int i = 0; i < cotisationAffilie.size(); i++) {
                                LineFacturation aLine = cotisationAffilie.get(i);

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
                                lineFacture.setMasseFacture(Double.toString(JANumberFormatter.round(aLine.getMasse(),
                                        0.05, 2, JANumberFormatter.NEAR)));
                                lineFacture.setMontantFacture(Double.toString(JANumberFormatter.round(
                                        aLine.getMontant(), 0.05, 2, JANumberFormatter.NEAR)));
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
                    calculCotisation.setSessionNaos(sessionNaos);
                    calculCotisation.setSessionOsiris(sessionOsiris);
                    calculCotisation.setIdRole(roleCoti);
                    calculCotisation = AFProcessFacturationBvrNeutre.calculerCotisation(calculCotisation, this,
                            donneesFacturation, anneeFacturation, moisFacturation, cotisationAffilie,
                            getIdModuleFacturationBvrNeutre());
                    // recherche du rôle pour la coti en cours
                    affilieNumero = calculCotisation.getAffilieNumero();
                    // ******************************************************************
                    // Insertion de l'en-tete facture, Si un nouveau tiers
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
                            enteteFacture.setIdTiers(donneesFacturation.getIdTiers());
                            enteteFacture.setISession(getSession());
                            enteteFacture.setIdPassage(idPassage);
                            enteteFacture.setIdTypeFacture(APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE);
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
                    // nbMontantZero++;
                    // }
                    nbCotisation++;
                }
            } // end while
            if (enteteFacture != null) {
                for (int i = 0; i < cotisationAffilie.size(); i++) {
                    LineFacturation aLine = cotisationAffilie.get(i);
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
            getMemoryLog().logMessage("Nb HeaderFacture Error : " + nbEnteteError, FWMessage.INFORMATION,
                    this.getClass().getName());
            getMemoryLog().logMessage("Nb LineFacture   Error : " + nbLineFactureError, FWMessage.INFORMATION,
                    this.getClass().getName());
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

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        // Déterminer l'objet du message en fonction du code erreur
        String obj = "";

        /*
         * if (getMemoryLog().hasErrors()) obj = FWMessage.getMessageFromId("5031")+ " " + getIdPassage(); else obj =
         * FWMessage.getMessageFromId("5030")+ " " + getIdDecision();
         */
        // Restituer l'objet
        return obj;
    }

    public String getIdModuleFacturationBvrNeutre() {
        return idModuleFacturationBvrNeutre;
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
     * Retourne une liste de cotisations soumises sous la forme d'opération comptables de type bulletin neutre
     * 
     * @param transaction
     *            transaction courant
     * @param numeroAffilie
     *            numero d'affilié
     * @param periodeDebut
     *            période de début de facturation
     * @param periodeFin
     *            période de fin de facturation
     * @return une liste d'opérations comptables de type bulletin neutre
     */
    public ArrayList<CAOperationBulletinNeutre> listCotisationsForBulletinNeutre(BTransaction transaction,
            CASection section, CAJournal journal) throws Exception {
        ArrayList<CAOperationBulletinNeutre> listOperationBN = new ArrayList<CAOperationBulletinNeutre>();
        ArrayList<LineFacturation> listCotisations = new ArrayList<LineFacturation>();
        // Contrôle de validation des zones en entrées
        if (transaction == null) {
            throw new Exception("listCotisationForBulletinNeutre: transaction required");
        }
        if (section == null) {
            throw new Exception("listCotisationForBulletinNeutre: section required");
        }
        if (journal == null) {
            throw new Exception("listCotisationForBulletinNeutre: journal required");
        }
        if (section.getCompteAnnexe() == null) {
            throw new Exception("listCotisationForBulletinNeutre: compteAnnexe required");
        }
        if (!JadeDateUtil.isGlobazDate(section.getDateFinPeriode())) {
            throw new Exception("listCotisationForBulletinNeutre: section.DateFinPeriode invalid : "
                    + section.getDateDebutPeriode());
        }
        if (!JadeDateUtil.isGlobazDate(section.getDateDebutPeriode())) {
            throw new Exception("listCotisationForBulletinNeutre: section.DateFinPeriode invalid : "
                    + section.getDateDebutPeriode());
        }
        // Créer une session Naos
        BSession sessionNaos = new BSession(AFApplication.DEFAULT_APPLICATION_NAOS);
        sessionNaos.connectSession(transaction.getSession());
        // Initialisation
        JADate dateFinPeriode = new JADate(section.getDateFinPeriode());
        String anneeFacturation = dateFinPeriode.toStrAMJ().substring(0, 4);
        String moisFacturation = dateFinPeriode.toStrAMJ().substring(4, 6);
        // Manager de facturation
        AFProcessFacturationManager manager = new AFProcessFacturationManager();
        manager.setSession(sessionNaos);
        manager.setForAffilieNumero(section.getCompteAnnexe().getIdExterneRole());
        manager.setForDateFacturation(section.getDateFinPeriode());
        manager.setForTypeAffiliation(AFProcessFacturationManager.TYPE_AFFILIATION_PARITAIRE);
        manager.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
        manager.find(transaction, BManager.SIZE_NOLIMIT);
        // Boucle principale de lecture des cotisations
        for (int i = 0; i < manager.size(); i++) {
            AFProcessFacturationViewBean donneeFacturation = (AFProcessFacturationViewBean) manager.getEntity(i);
            CalculCotisation calculCotisation = new CalculCotisation();
            calculCotisation.setSessionNaos(sessionNaos);
            calculCotisation.setSessionOsiris(section.getSession());
            calculCotisation.setIdRole(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                    sessionNaos.getApplication()));
            calculCotisation = AFProcessFacturationBvrNeutre.calculerCotisation(calculCotisation, this,
                    donneeFacturation, anneeFacturation, moisFacturation, listCotisations,
                    getIdModuleFacturationBvrNeutre());
            for (int j = calculCotisation.getMontantList().size() - 1; j >= 0; j--) {
                LineFacturation aLine = calculCotisation.getMontantList().get(j);
                listCotisations.add(aLine);
                // Création d'une opération de type bulletin neutre
                CAOperationBulletinNeutre opBulletinNeutre = new CAOperationBulletinNeutre();
                opBulletinNeutre.setSession(section.getSession());
                opBulletinNeutre.setIdSection(section.getIdSection());
                opBulletinNeutre.setIdCompteAnnexe(section.getIdCompteAnnexe());
                opBulletinNeutre.setDate(journal.getDateValeurCG());
                opBulletinNeutre.setAnneeCotisation(anneeFacturation);
                opBulletinNeutre.setIdCompte(aLine.getIdRubrique());
                opBulletinNeutre.setIdCaisseProfessionnelle(aLine.getCaisse());
                opBulletinNeutre.setCodeDebitCredit(APIEcriture.DEBIT);
                opBulletinNeutre.setLibelle(aLine.getLibelle());
                opBulletinNeutre.setTaux(Double.toString(aLine.getTaux()));
                opBulletinNeutre.setIdJournal(journal.getIdJournal());
                listOperationBN.add(opBulletinNeutre);
            }
        }
        //
        return listOperationBN;
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

    public void setIdModuleFacturationBvrNeutre(String idModuleFacturationBvrNeutre) {
        this.idModuleFacturationBvrNeutre = idModuleFacturationBvrNeutre;
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

}
