package ch.globaz.pegasus.businessimpl.utils.annonce.annoncelaprams.model;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.al.business.constantes.ALCSAllocataire;
import ch.globaz.pegasus.business.constantes.Compteurs;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDestinationSortieHome;
import ch.globaz.pegasus.business.constantes.IPCHabitat;
import ch.globaz.pegasus.business.constantes.IPCLienRepondant;
import ch.globaz.pegasus.business.constantes.IPCPermis;
import ch.globaz.pegasus.business.constantes.IPCVariableMetier;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCAutresDettesProuvees;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCDessaisissementFortune;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCDonneeFinanciere;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCIJAI;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCIJAPG;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCPensionAlimentaire;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCTaxeJournaliere;
import ch.globaz.pegasus.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.pegasus.business.models.annonce.AnnonceLaprams;
import ch.globaz.pegasus.business.models.annonce.AnnonceLapramsDonneeFinanciere;
import ch.globaz.pegasus.business.models.annonce.AnnonceLapramsMediatorDonneeFinanciere;
import ch.globaz.pegasus.business.models.annonce.AnnonceLapramsMediatorDonneeFinanciereSearch;
import ch.globaz.pegasus.business.models.annonce.DonneePersSiutationFamilliale;
import ch.globaz.pegasus.business.models.annonce.DonneePersSiutationFamillialeSearch;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesHome;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesHomeSearch;
import ch.globaz.pegasus.business.models.habitat.SimpleTaxeJournaliereHome;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeDroit;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeDroitSearch;
import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeePlanCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeePlanCalculSearch;
import ch.globaz.pegasus.business.models.variablemetier.SimpleVariableMetier;
import ch.globaz.pegasus.business.models.variablemetier.SimpleVariableMetierSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.annonce.annoncelaprams.ResolveSequence;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.ProxyCalculDates;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.StrategieLoyer;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieIJAI;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieIJAPG;
import ch.globaz.pegasus.businessimpl.utils.dessaisissement.DessaisissementUtils;
import ch.globaz.pegasus.businessimpl.utils.dessaisissement.DessaisissementUtils.Fortune;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class LapramsDataMediator {

    /**
     * Utilitaire pour sommer simplement les données financières pour le modèle AnnonceLapramsMediatorDonneeFinanciere
     * 
     * @author eco
     */
    private abstract class DFHDetailProcessor {

        public String build(boolean isConjoint, boolean all) throws AnnonceException {
            float montantTotal = 0;
            for (JadeAbstractModel absDonnee : dfMediatorSearch.getSearchResults()) {
                AnnonceLapramsMediatorDonneeFinanciere donnee = (AnnonceLapramsMediatorDonneeFinanciere) absDonnee;

                // Chapi Chapeau exclusif :) Le circonflex ( ^ ) signifique que l'on fait un ou excluisif.
                if (((isConjoint && isDonneeDetailConjoint(donnee)) ^ (!isConjoint && isDonneeDetailRequerant(donnee)))
                        || all) {
                    String[] fields = getFields(donnee);
                    if ((fields != null) && (fields.length > 0)) {
                        for (String field : fields) {
                            montantTotal += montantStrToFloat(field);
                        }
                    }

                    montantTotal += customAction(donnee);
                }
            }

            return String.valueOf(montantTotal);
        }

        /**
         * Méthode à surcharger s'il y a des opérations spécifiques à executer
         * 
         * @param donnee
         *            le modèle de données
         * @return le montant résultant
         * @throws AnnonceException
         */
        public float customAction(AnnonceLapramsMediatorDonneeFinanciere donnee) throws AnnonceException {
            return 0;
        }

        /**
         * Methode abstraite définissant la liste des champs à sommer directement. (pas d'annualisation ni d'operation
         * de transformation)
         * 
         * @param donnee
         * @return
         */
        public abstract String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee);

    }

    public static final String CS_CANTON_VAUD = "505022";

    private static final String CS_GENRE_ANNONCE_FIN_DROIT = "D";
    private static final String CS_GENRE_ANNONCE_NOUVEAU_CAS_ET_MAJ = "A";
    private static final String CS_TYPLE_COUPLE_CONJOINT_HOME = "H";

    private static final int NB_MOIS = 12;

    private static String generateKey() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        int year = cal.get(Calendar.YEAR);
        return String.valueOf(year) + "_" + week;
    }

    private AdresseTiersDetail adresseTiersConjoint = null;
    private AdresseTiersDetail adresseTiersRequerant = null;
    private TreeMap<Long, String> amortissements = null;
    private List<AnnonceLaprams> annonces = new ArrayList<AnnonceLaprams>();
    private CalculDonneesHome chambreRequerant = null;
    private String dateDecompte = null;

    private String dateRapport = null;

    private AnnonceLaprams defaultAnnonce = null;
    private AnnonceLapramsMediatorDonneeFinanciereSearch dfMediatorSearch = null;

    private List<AnnonceLapramsDonneeFinanciere> doFinH = new ArrayList<AnnonceLapramsDonneeFinanciere>();
    private DonneePersSiutationFamilliale donneePerssonnelleConjoint = null;
    private DonneePersSiutationFamilliale donneePerssonnelleRequerant = null;
    private CalculDonneesHomeSearch homes = null;

    // private List<PCAccordee> listePCAccordees = null;
    private final Map<String, String> mappingCsEtatCivil = new HashMap<String, String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            put(ALCSAllocataire.ETAT_CIVIL_CELIBATAIRE, "01");
            put(ALCSAllocataire.ETAT_CIVIL_MARIE, "02");
            put(ALCSAllocataire.ETAT_CIVIL_VEUF, "03");
            put(ALCSAllocataire.ETAT_CIVIL_DIVORCE, "04");
            put(ALCSAllocataire.ETAT_CIVIL_SEPARE, "05");
            put(ALCSAllocataire.ETAT_CIVIL_SEPARE_DE_FAIT, "06");
            put("515007", "07"); // CS_LPART_ENREGISTRE
            put("515010", "08"); // CS_LPART_SEPARE_DE_FAIT;
            put("515008", "09"); // CS_LPART_DISSOUS
            put("515009", "10"); // CS_LPART_DISSOUS_DECES
        }
    };

    private final Map<String, String> mappingCsLienRepondant = new HashMap<String, String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            put(IPCLienRepondant.CS_PERE_MERE, "01");
            put(IPCLienRepondant.CS_FILS_FILLE, "02");
            put(IPCLienRepondant.CS_CONJOINT, "09");
            put(IPCLienRepondant.CS_CURATEUR, "10");
            put(IPCLienRepondant.CS_AUTRE_CURATEUR, "11");
            put(IPCLienRepondant.CS_AUTRE_PARENT, "22");
            put(IPCLienRepondant.CS_AUTRE_TIERS, "23");
        }
    };

    private final Map<String, String> mappingCsSexe = new HashMap<String, String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            put(ALCSAllocataire.SEXE_F, "F");
            put(ALCSAllocataire.SEXE_M, "H");
        }
    };

    private final Map<String, String> mappingCsSortieHome = new HashMap<String, String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            put(IPCDestinationSortieHome.CS_AUTRE_HOME, "EM");
            put(IPCDestinationSortieHome.CS_AUTRES_DESTINATIONS, "  ");
            put(IPCDestinationSortieHome.CS_DECES, "DC");
            put(IPCDestinationSortieHome.CS_DOMICILE, "DO");
            put(IPCDestinationSortieHome.CS_HOPITAL, "HO");
        }
    };

    private final Map<String, String> mappingTypeDecision = new HashMap<String, String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            put(IPCDecision.CS_TYPE_REFUS_AC, LapramsDataMediator.CS_GENRE_ANNONCE_FIN_DROIT);
            put(IPCDecision.CS_TYPE_REFUS_SC, "");
            put(IPCDecision.CS_TYPE_SUPPRESSION_SC, LapramsDataMediator.CS_GENRE_ANNONCE_FIN_DROIT);
            put(IPCDecision.CS_TYPE_ADAPTATION_AC, LapramsDataMediator.CS_GENRE_ANNONCE_NOUVEAU_CAS_ET_MAJ);
            put(IPCDecision.CS_TYPE_OCTROI_AC, LapramsDataMediator.CS_GENRE_ANNONCE_NOUVEAU_CAS_ET_MAJ);
            put(IPCDecision.CS_TYPE_PARTIEL_AC, LapramsDataMediator.CS_GENRE_ANNONCE_NOUVEAU_CAS_ET_MAJ);
        }
    };

    private final Map<String, String> mappingTypePermis = new HashMap<String, String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            put(IPCPermis.CS_PERMIS_B, "01");
            put(IPCPermis.CS_PERMIS_C, "02");
            put(IPCPermis.CS_PERMIS_F, "03");
            put(IPCPermis.CS_PERMIS_N, "04");
        }
    };

    private String noSerie = null;

    private AdresseTiersDetail repondant = new AdresseTiersDetail();

    private int sequence = 0;
    private AnnonceLapramsMediatorDonneeFinanciere taxeJournaliereHomeRequerant = null;

    private PersonneEtendueComplexModel tiersConjoint = null;
    private PersonneEtendueComplexModel tiersRequerant = null;

    private SimpleVariableMetierSearch variablesMetier = null;

    public LapramsDataMediator(List<AnnonceLaprams> annonces, String dateRapport) {
        super();
        this.annonces = annonces;
        this.dateRapport = dateRapport;
        ResolveSequence sequence = new ResolveSequence();
        try {
            this.sequence = Integer.valueOf(JadePersistenceManager.incIndentifiant("LAPRAMS"));
            // this.sequence = sequence.resolveSequence("ANNONCE_LAPRAMS", LapramsDataMediator.generateKey());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String annualiseMontantMensuel(String pensionAlimentaireMontant) {
        if (JadeNumericUtil.isNumeric(pensionAlimentaireMontant)) {
            return String.valueOf(Float.parseFloat(pensionAlimentaireMontant) * LapramsDataMediator.NB_MOIS);
        } else {
            return "0";
        }
    }

    public AdresseTiersDetail getAdresse(boolean isConjoint) {
        AdresseTiersDetail personne = (isConjoint ? adresseTiersConjoint : adresseTiersRequerant);
        return personne;
    }

    public String getAllocationPourImpotent() throws AnnonceException {

        return multiplyBy(processBoth(new DFHDetailProcessor() {

            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {
                return new String[] { donnee.getAllocationImpotentRevenu() };
            }
        }), LapramsDataMediator.NB_MOIS);
    }

    public List<AnnonceLaprams> getAnnonces() {
        return annonces;
    }

    public String getAutresDeductions(boolean isConjoint) throws AnnonceException {
        return process(isConjoint, new DFHDetailProcessor() {

            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {
                String montant = "0";
                if (IPCPensionAlimentaire.CS_TYPE_PENSION_ALIMENTAIRE_VERSEE.equals(donnee
                        .getPensionAlimentaireCsTypePension())) {
                    montant = donnee.getPensionAlimentaireMontant();
                }
                return new String[] { LapramsDataMediator.this.annualiseMontantMensuel(montant) };
            }

        });
    }

    public String getAutresPrenoms(boolean isConjoint) {
        return getPersonneEtendue(isConjoint).getTiers().getDesignation3() + " "
                + getPersonneEtendue(isConjoint).getTiers().getDesignation4();
    }

    public String getAutresRentes() throws AnnonceException {

        String montant = processDivideBy2IfHasConjoint(new DFHDetailProcessor() {
            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {

                String montantIjai = "0";
                if (IPCIJAI.CS_TYPE_DONNEE_FINANCIERE.equals(donnee.getCsTypeDonneeFinanciere())) {
                    montantIjai = String.valueOf(StrategieIJAI.calculeRevenu(donnee.getIndemniteJournaliereAiRevenu(),
                            donnee.getIndemniteJournaliereAiNbjours(), defaultAnnonce.getPcAccordee()
                                    .getSimplePCAccordee().getDateDebut()));

                }

                // String montantViager = "0";
                //
                // if (IPCAssuranceRenteViagere.CS_TYPE_DONNEE_FINANCIERE.equals(donnee.getCsTypeDonneeFinanciere())) {
                // StrategieAssuranceRenteViagere strategieAssuranceRenteViagere = new StrategieAssuranceRenteViagere();
                // montantViager = String.valueOf(strategieAssuranceRenteViagere.calculRevenu(
                // donnee.getAssuranceRenteViagereMontant(), donnee.getAssuranceRenteViagereExcedent()));
                // }

                return new String[] { donnee.getAutreRenteRevenu(), donnee.getAutreApiRevenu(), montantIjai,
                        donnee.getAssuranceRenteViagereMontant(), donnee.getAssuranceRenteViagereExcedent() };
            }
        });

        return montant;
    }

    public String getAutresRevenus() throws AnnonceException {
        return processDivideBy2IfHasConjoint(new DFHDetailProcessor() {
            @Override
            public float customAction(AnnonceLapramsMediatorDonneeFinanciere donnee) throws AnnonceException {
                float montantTotal = 0;

                if (IPCPensionAlimentaire.CS_TYPE_PENSION_ALIMENTAIRE_DUE.equals(donnee
                        .getPensionAlimentaireCsTypePension())) {

                    montantTotal += (LapramsDataMediator.this.montantStrToFloat(donnee.getPensionAlimentaireMontant()))
                            * LapramsDataMediator.NB_MOIS;
                }

                return montantTotal;
            }

            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {
                String allocationsFamilialesMontantMensuel = donnee.getAllocationsFamilialesMontantMensuel();
                float allocFamilialleAnnuelle = 0;
                if (JadeNumericUtil.isNumeric(allocationsFamilialesMontantMensuel)) {
                    allocFamilialleAnnuelle = Float.parseFloat(allocationsFamilialesMontantMensuel) * 12;
                }

                float lca = 0;

                if (donnee.getTaxeJournaliereHomeIsPartLCA()) {
                    lca = ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieTaxeJournaliere
                            .calculeRevenu(donnee.getTaxeJournaliereHomePartLCA(),
                                    donnee.getTaxeJournaliereHomePimeAPayerLCA(),
                                    LapramsDataMediator.this.getDureePeriode());
                }
                Float montantIjApg = 0.00f;

                if (IPCIJAPG.CS_TYPE_DONNEE_FINANCIERE.equals(donnee.getCsTypeDonneeFinanciere())) {
                    StrategieIJAPG strategieIJAPG = new StrategieIJAPG();
                    String mensualisationIjChomage = LapramsDataMediator.this.getVariableMetier(
                            IPCVariableMetier.MENSUALISATION_IJ_CHOMAGE).getTaux();

                    montantIjApg = strategieIJAPG.calculeRevenu(donnee.getIjApgMontant(),
                            donnee.getIjApgMontantBrutAC(), donnee.getIjApgNbJours(), donnee.getIjApgTauxAVS(),
                            donnee.getIjApgTauxAA(), donnee.getIjApgCotisationLPPMensC(),
                            donnee.getIjApgGainIntAnnuel(), mensualisationIjChomage,
                            LapramsDataMediator.this.getDureePeriode());
                }
                return new String[] { donnee.getNumeraireMontantInteret(), donnee.getCompteBancaireCCPMontantInteret(),
                        donnee.getTitreRendement(), donnee.getPretEnversTiersMontantInteret(),
                        donnee.getCapitalLPPMontantInteret(), String.valueOf(montantIjApg),
                        donnee.getContratEntretienViagerMontant(), String.valueOf(allocFamilialleAnnuelle),
                        donnee.getAutresRevenusMontant(), donnee.getRevenuHypothetiqueMontantBrut(),
                        LapramsDataMediator.this.getRevenusDessaisisRequerant(donnee), String.valueOf(lca),
                        donnee.getRevenuHypothetiqueMontantNet() };

            }
        });
    }

    public String getBiensDessaisis() {

        // préparation de la liste des déssaisissements manuelles de fortune
        List<Fortune> dessaisissements = new ArrayList<Fortune>();
        float dessaisissementAmorti = 0;
        Date dateDebutCalcul = null;
        for (JadeAbstractModel absDonnee : LapramsDataMediator.this.dfMediatorSearch.getSearchResults()) {
            AnnonceLapramsMediatorDonneeFinanciere donnee = (AnnonceLapramsMediatorDonneeFinanciere) absDonnee;
            if (IPCDessaisissementFortune.CS_TYPE_DONNEE_FINANCIERE.equals(donnee.getCsTypeDonneeFinanciere())) {
                String montantBrut = donnee.getDessaisissementFortuneMontantBrut();
                String deductions = donnee.getDessaisissementFortuneDeductions();
                if (!JadeNumericUtil.isNumeric(deductions)) {
                    deductions = "0";
                }
                float montantDessaisi = Float.parseFloat(montantBrut) - Float.parseFloat(deductions);
                String strDateDebut = "01." + donnee.getDateDebutDonneeFinanciere();
                Date dateDebut = JadeDateUtil.getGlobazDate(strDateDebut);

                Fortune fortune = new Fortune(dateDebut, montantDessaisi);
                dessaisissements.add(fortune);

                // recherche de la plus ancienne date de début de dessaisissement
                if ((dateDebutCalcul == null) || dateDebut.before(dateDebutCalcul)) {
                    dateDebutCalcul = dateDebut;
                }
            }
        }

        if (dateDebutCalcul != null) {
            Date dateDebutPeriode = JadeDateUtil.getGlobazDate("01."
                    + defaultAnnonce.getSimpleDecisionHeader().getDateDebutDecision());

            dessaisissementAmorti = DessaisissementUtils.calculeAmortissement(dateDebutPeriode, dateDebutCalcul,
                    dessaisissements, amortissements);

        }
        return String.valueOf(dessaisissementAmorti);
    }

    private CalculDonneesHome getChambreHome(String idTaxeJournaliereHome) throws AnnonceException {

        if (homes == null) {
            throw new AnnonceException("Homes search model is not initialized!");
        }
        if (homes.getSize() > 0) {
            for (JadeAbstractModel absDonnee : homes.getSearchResults()) {
                CalculDonneesHome donnee = (CalculDonneesHome) absDonnee;

                if (donnee.getIdTaxesJournaliere().equals(idTaxeJournaliereHome)) {
                    // Ce filtrage est fait en amont pour le searchModel
                    // Verifier que le prix soit valable pour la date de début de validite de la PCA
                    // && JadeDateUtil.isDateAfter("01."
                    // + this.defaultAnnonce.getPcAccordee().getSimplePCAccordee().getDateDebut(),
                    // "01." + donnee.getDateDebutPrixChambre())
                    // && (JadeDateUtil.isDateBefore("01."
                    // + this.defaultAnnonce.getPcAccordee().getSimplePCAccordee().getDateDebut(),
                    // "01." + donnee.getDateFinPrixChambre()) || JadeStringUtil.isBlankOrZero(donnee
                    // .getDateFinPrixChambre()))) {

                    return donnee;
                }
            }
        }

        return null;
    }

    private CalculDonneesHome getChambreRequerant() throws AnnonceException {
        if (chambreRequerant == null) {
            AnnonceLapramsMediatorDonneeFinanciere donnee = getTaxeJournaliereRequerant();
            if (donnee != null) {
                chambreRequerant = LapramsDataMediator.this.getChambreHome(donnee.getIdTaxeJournaliereHome());
            }
        }
        return chambreRequerant;
    }

    public String getChargesAnnuellesReelles(boolean isConjoint) throws AnnonceException {
        return process(isConjoint, new DFHDetailProcessor() {

            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {

                float chargesAnnuellesReelles = 0;

                String csTypeLoyer = donnee.getLoyerCsTypeLoyer();
                if (IPCHabitat.CS_LOYER_NET_AVEC_CHARGE_FORFAITAIRES.equals(csTypeLoyer)) {
                    chargesAnnuellesReelles = Float.parseFloat(LapramsDataMediator.this.getVariableMetier(
                            IPCVariableMetier.CS_FORFAIT_FRAIS_CHAUFFAGE).getMontant());

                } else if (IPCHabitat.CS_VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE.equals(csTypeLoyer)) {
                    chargesAnnuellesReelles = Float.parseFloat(LapramsDataMediator.this.getVariableMetier(
                            IPCVariableMetier.CS_FORFAIT_CHARGES).getMontant());
                } else if (IPCHabitat.CS_LOYER_NET_AVEC_CHARGE.equals(csTypeLoyer)) {
                    float montantChargesLocatives = 0;
                    if (JadeNumericUtil.isNumeric(donnee.getLoyerCharges())) {
                        montantChargesLocatives = Float.parseFloat(donnee.getLoyerCharges());
                    }
                    chargesAnnuellesReelles = montantChargesLocatives * LapramsDataMediator.NB_MOIS;

                }

                return new String[] { String.valueOf(chargesAnnuellesReelles) };
            }
        });
    }

    public String getCodeCommuneOrigine(boolean isConjoint) {
        if (isConjoint) {
            return donneePerssonnelleConjoint.getCodeCommuneOFS();
        } else {
            return donneePerssonnelleRequerant.getCodeCommuneOFS();
        }
    }

    public String getCodeDestinationSortieRequerant() {
        if (getTaxeJournaliereRequerant() != null) {
            String code = getTaxeJournaliereRequerant().getTaxeJournaliereHomeCsDestinationSortie();
            if (!JadeStringUtil.isBlankOrZero(code)) {
                return mappingCsSortieHome.get(code);
            } else if (isDecisionModifDece()) {
                return "DC";
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public String getCodeEnfantsRequerant() {
        // blanc si pas d'enfants
        if (JadeStringUtil.isBlankOrZero(defaultAnnonce.getSimpleAnnonceLaprams().getCodeEnfant())) {
            return " ";
        } else {
            return defaultAnnonce.getSimpleAnnonceLaprams().getCodeEnfant();
        }
    }

    public String getCodeEtatCivil(boolean isConjoint) {
        String etatCivil = getPersonneEtendue(isConjoint).getPersonne().getEtatCivil();
        if (mappingCsEtatCivil.containsKey(etatCivil)) {
            return mappingCsEtatCivil.get(etatCivil);
        } else {
            return null;
        }
    }

    public String getCodeGenreAnnonce() {
        String typeDecision = defaultAnnonce.getSimpleDecisionHeader().getCsTypeDecision();
        if (mappingTypeDecision.containsKey(typeDecision)) {
            return mappingTypeDecision.get(typeDecision);
        } else {
            return null;
        }
    }

    public String getCodeLienRepondant() {
        String csLien = donneePerssonnelleRequerant.getCsLienRepondant();
        if (mappingCsLienRepondant.containsKey(csLien)) {
            return mappingCsLienRepondant.get(csLien);
        } else {
            return null;
        }
    }

    public String getCodePaysOrigine(boolean isConjoint) throws AnnonceException {
        String idPays = null;
        String codePays = "";
        if (isConjoint) {
            idPays = tiersConjoint.getTiers().getIdPays();
        } else {
            idPays = tiersRequerant.getTiers().getIdPays();
        }

        PaysSearchSimpleModel pssmReq = new PaysSearchSimpleModel();
        pssmReq.setForIdPays(idPays);
        try {
            PaysSearchSimpleModel search = TIBusinessServiceLocator.getAdresseService().findPays(pssmReq);

            if (search.getSize() > 0) {
                PaysSimpleModel paysSimpleModel = (PaysSimpleModel) search.getSearchResults()[0];
                codePays = paysSimpleModel.getCodeCentrale();
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AnnonceException("JadeApplicationServiceNotAvailableException", e);
        } catch (JadePersistenceException e) {
            throw new AnnonceException("JadePersistenceException", e);
        } catch (JadeApplicationException e) {
            throw new AnnonceException("JadeApplicationException", e);
        }
        return codePays;
        // return this.geti this.getAdresse(isConjoint).getFields().get(AdresseTiersDetail.ADRESSE_VAR_PAYS_ISO);// Le
        // code
        // centrale
        // OFS
    }

    public String getCodePermisSejour(boolean isConjoint) {
        String permis = donneePerssonnelleRequerant.getCsPermis();

        if (mappingTypePermis.containsKey(permis)) {
            return mappingTypePermis.get(permis);
        } else {
            return null;
        }
    }

    public String getCodeTypeLitRequerant() throws AnnonceException {

        try {
            // détermine si c'est un home vaudois
            CalculDonneesHome chambreRequerant = getChambreRequerant();
            if (chambreRequerant != null) {
                String idHome = chambreRequerant.getIdHome();

                Home home;
                home = PegasusServiceLocator.getHomeService().read(idHome);
                if (LapramsDataMediator.CS_CANTON_VAUD.equals(home.getAdresse().getLocalite().getIdCanton())) {

                    if (IPCTaxeJournaliere.CS_CATEGORIE_CHAMBRE_MEDICALISE.equals(chambreRequerant.getCsTypeChambre())) {
                        return "C";
                    } else {
                        return "D";
                    }

                } else {
                    return "P";
                }
            } else {
                return " ";
            }
        } catch (HomeException e) {
            throw new AnnonceException("An exception rose while trying to read home!", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AnnonceException("A jade service exception happened!", e);
        } catch (JadePersistenceException e) {
            throw new AnnonceException("A jade persistence exception happened!", e);
        }
    }

    public String getComplementCantonalPourDepensesPersonnelles(boolean b) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCotisationAVSAI(boolean isConjoint) throws AnnonceException {
        return process(isConjoint, new DFHDetailProcessor() {

            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {
                return new String[] { donnee.getCotisationsPsalMontantCotisationsAnnuelles() };
            }
        });
    }

    public String getDateDebutSejourLitRequerant() {
        return "";
    }

    public String getDateDebutValidite() {
        return defaultAnnonce.getSimpleDecisionHeader().getDateDebutDecision();
    }

    public String getDateDeces(boolean isConjoint) {

        return getPersonneEtendue(isConjoint).getPersonne().getDateDeces();
    }

    public String getDateDecisionPC() {

        return defaultAnnonce.getSimpleDecisionHeader().getDateDecision();
    }

    public String getDateDecompte() throws AnnonceException {
        // if (this.dateDecompte == null) {
        // retourner la date d'entrée effective en home
        dateDecompte = "";
        // Le traitement s'attend à ne trouver une date de décompte que dans les annonces concernant une nouvelle entrée
        // en EMS avec jours d'appoint
        if (hasJoursAppoint()) {
            AnnonceLapramsMediatorDonneeFinanciere taxeJournaliere = getTaxeJournaliereRequerant();
            if (taxeJournaliere != null) {
                if (JadeStringUtil.isBlankOrZero(taxeJournaliere.getTaxeJournaliereHomeDateEntreeHome())) {
                    dateDecompte = getDateEntreeHomeRequerant();
                } else {
                    dateDecompte = taxeJournaliere.getTaxeJournaliereHomeDateEntreeHome();
                }
            }
        }
        // }
        return dateDecompte;
    }

    public String getDateEntreeHomeRequerant() throws AnnonceException {
        CalculDonneesHome chambre = getChambreRequerant();
        if (chambre != null) {
            return chambre.getDateEntreeHome();
        }

        return null;
    }

    public String getDateEnvoi() {
        return JadeDateUtil.getGlobazFormattedDate(new Date());
        // return this.defaultAnnonce.getSimpleAnnonceLaprams().getDateEnvoi();
    }

    public String getDateFinValidite() {
        return defaultAnnonce.getSimpleDecisionHeader().getDateFinDecision();
    }

    public String getDateNaissance(boolean isConjoint) {
        return getPersonneEtendue(isConjoint).getPersonne().getDateNaissance();
    }

    public String getDateRapport() {
        return dateRapport;
    }

    public String getDateSortieHomeRequerant() throws AnnonceException {
        CalculDonneesHome chambre = getChambreRequerant();
        if (chambre != null) {
            return chambre.getDateFinDFH();
        }

        return null;
    }

    public String getDeductionAutresDettes() throws AnnonceException {
        return processBoth(new DFHDetailProcessor() {

            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {
                if (IPCAutresDettesProuvees.CS_TYPE_DONNEE_FINANCIERE.equals(donnee.getCsTypeDonneeFinanciere())) {
                    return new String[] { donnee.getAutresDettesProuveesMontantDette() };
                } else {
                    return new String[] { "0" };
                }
            }
        });
    }

    public String getDeductionDettesHypo() throws AnnonceException {
        return processBoth(new DFHDetailProcessor() {

            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {
                return new String[] { donnee.getBienImmobilierServantHabitationPrincipaleMontantDetteHypothecaire(),
                        donnee.getBienImmobilierHabitationNonPrincipaleMontantDetteHypothecaire(),
                        donnee.getBienImmobilierNonHabitableMontantDetteHypothecaire() };
            }
        });
    }

    public String getDeductionLegaleImmeuble() throws AnnonceException {

        final boolean[] hasDeduction = new boolean[1];
        hasDeduction[0] = false;
        return processBoth(new DFHDetailProcessor() {

            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {
                String deductionForfaitaire = "0";
                if (!hasDeduction[0]) {
                    if (JadeNumericUtil.isNumericPositif(donnee
                            .getBienImmobilierServantHabitationPrincipaleMontantValeurFiscale())) {
                        deductionForfaitaire = LapramsDataMediator.this.getVariableMetier(
                                IPCVariableMetier.CS_DEDUCTION_FORAITAIRE_IMMOBILIER_ASSURE).getMontant();
                        hasDeduction[0] = true;
                    }
                }

                return new String[] { deductionForfaitaire

                };
            }
        });
    }

    public AnnonceLaprams getDefaultAnnonce() {
        return defaultAnnonce;
    }

    public String getDepensesPersonnellesAnnuelles(final boolean isConjoint) throws AnnonceException {

        String montant = annualiseMontantMensuel(process(isConjoint, new DFHDetailProcessor() {
            @Override
            public float customAction(AnnonceLapramsMediatorDonneeFinanciere donnee) throws AnnonceException {

                if ((donnee != null) && !JadeStringUtil.isBlankOrZero(donnee.getIdTaxeJournaliereHome())) {
                    if ((isConjoint && LapramsDataMediator.this.isDonneeDetailConjoint(donnee))
                            ^ (!isConjoint && LapramsDataMediator.this.isDonneeDetailRequerant(donnee))) {
                        CalculDonneesHome chambre = LapramsDataMediator.this.getChambreHome(donnee
                                .getIdTaxeJournaliereHome());
                        if (chambre != null) {
                            return getMontantArgentPoche(chambre);
                        } else {
                            return 0;
                        }
                    }
                }
                return 0;
            }

            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {
                return new String[0];
            }

        }));

        if (JadeNumericUtil.isEmptyOrZero(montant)) {
            montant = LapramsDataMediator.this.getVariableMetier(IPCVariableMetier.CS_BESOINS_VITAUX_CELIBATAIRES)
                    .getMontant();
        }
        return montant;
    }

    private float getMontantArgentPoche(CalculDonneesHome chambre) throws AnnonceException {

        try {
            if (EPCProperties.LVPC.getBooleanValue() && idDateDebutPCAForStartegyLVPC()) {
                if (IPCTaxeJournaliere.CS_EMS_NON_MEDICALISE_AGE.equals(chambre.getCsCategorieArgentPoche())) {
                    return Float.parseFloat(LapramsDataMediator.this.getVariableMetier(
                            IPCVariableMetier.CS_CATEGORIE_ARGENT_POCHE_LVPC_EMS_NOMMED_AGE_AVANCE).getMontant());
                } else if (IPCTaxeJournaliere.CS_EMS_NON_MEDICALISE_PSY.equals(chambre.getCsCategorieArgentPoche())) {
                    return Float.parseFloat(LapramsDataMediator.this.getVariableMetier(
                            IPCVariableMetier.CS_CATEGORIE_ARGENT_POCHE_LVPC_EMS_NONMED_PSY).getMontant());
                } else if (IPCTaxeJournaliere.CS_ESE_HANDICAP_PHYSIQUE.equals(chambre.getCsCategorieArgentPoche())) {
                    return Float.parseFloat(LapramsDataMediator.this.getVariableMetier(
                            IPCVariableMetier.CS_CATEGORIE_ARGENT_POCHE_LVPC_ESE_HANDICAP_PHYSIQUE).getMontant());
                }
            } else {
                if (IPCTaxeJournaliere.CS_CATEGORIE_CHAMBRE_MEDICALISE.equals(chambre.getCsTypeChambre())) {
                    return Float.parseFloat(LapramsDataMediator.this.getVariableMetier(
                            IPCVariableMetier.CS_ARGENT_POCHE_MEDICALISE).getMontant());
                } else {
                    return Float.parseFloat(LapramsDataMediator.this.getVariableMetier(
                            IPCVariableMetier.CS_ARGENT_POCHE_NON_MEDICALISE).getMontant());
                }
            }
        } catch (Exception e) {
            throw new AnnonceException(e.getMessage());
        }

        return 0;
    }

    private boolean idDateDebutPCAForStartegyLVPC() {
        String dateAConsiderer = "01." + defaultAnnonce.getPcAccordee().getSimplePCAccordee().getDateDebut();
        String dateDebutStrategy = JadeDateUtil.getGlobazFormattedDate(new Date(
                ProxyCalculDates.DEPENSE_TOTAL_RECONNUES_SWITCH_STRATEGY_DATE.timestamp));

        return JadeDateUtil.isDateBefore(dateDebutStrategy, dateAConsiderer)
                || dateAConsiderer.equals(dateDebutStrategy);

    }

    public List<AnnonceLapramsDonneeFinanciere> getDoFinH() {
        return doFinH;
    }

    private Integer getDureePeriode() {
        java.util.Calendar cal = JadeDateUtil.getGlobazCalendar("01."
                + defaultAnnonce.getSimpleDecisionHeader().getDateDebutDecision());
        Integer nbDays = cal.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
        return nbDays;
    }

    public String getFortuneMobiliere() throws AnnonceException {
        return processBoth(new DFHDetailProcessor() {
            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {

                return new String[] {
                        donnee.getAssuranceRenteViagereMontantValeurRachat(),
                        donnee.getAssuranceVieMontant(),

                        LapramsDataMediator.this.montantFractionne(donnee.getNumeraireMontant(),
                                donnee.getNumeraireNumerateur(), donnee.getNumeraireDenominateur(),
                                donnee.getNumeraireCsTypePropriete()),

                        LapramsDataMediator.this.montantFractionne(donnee.getAutreFortuneMobiliereMontant(),
                                donnee.getAutreFortuneMobiliereNumerateur(),
                                donnee.getAutreFortuneMobiliereDenominateur(),
                                donnee.getAutreFortuneMobiliereCsTypePropriete()),

                        LapramsDataMediator.this.montantFractionne(donnee.getBetailMontant(),
                                donnee.getBetailNumerateur(), donnee.getBetailDenominateur(),
                                donnee.getBetailCsTypePropriete()),

                        LapramsDataMediator.this.montantFractionne(donnee.getCapitalLPPMontant(),
                                donnee.getCapitalLPPNumerateur(), donnee.getCapitalLPPDenominateur(),
                                donnee.getCapitalLPPCsTypePropriete()),

                        LapramsDataMediator.this.montantFractionne(donnee.getCompteBancaireCCPMontant(),
                                donnee.getCompteBancaireCCPNumerateur(), donnee.getCompteBancaireCCPDenominateur(),
                                donnee.getCompteBancaireCCPCsTypePropriete()),

                        LapramsDataMediator.this.montantFractionne(donnee.getMarchandisesStockMontant(),
                                donnee.getMarchandisesStockNumerateur(), donnee.getMarchandisesStockDenominateur(),
                                donnee.getMarchandisesStockCsTypePropriete()),

                        LapramsDataMediator.this.montantFractionne(donnee.getPretEnversTiersMontant(),
                                donnee.getPretEnversTiersNumerateur(), donnee.getPretEnversTiersDenominateur(),
                                donnee.getPretEnversTiersCsTypePropriete()),

                        LapramsDataMediator.this.montantFractionne(donnee.getTitreMontant(),
                                donnee.getTitreNumerateur(), donnee.getTitreDenominateur(),
                                donnee.getTitreCsTypePropriete()),

                        LapramsDataMediator.this.montantFractionne(donnee.getVehiculeMontant(),
                                donnee.getVehiculeNumerateur(), donnee.getVehiculeDenominateur(),
                                donnee.getVehiculeCsTypePropriete())

                };
            }
        });
    }

    public String getFraisSejourAnnuelsRequerant() throws AnnonceException {

        CalculDonneesHome chambre = getChambreRequerant();
        if (chambre != null) {
            float montantAnnuel = Float.parseFloat(chambre.getPrixJournalier()) * getDureePeriode();
            return String.valueOf(montantAnnuel);
        }

        return "";
    }

    public String getIdLot() throws JadePersistenceException {
        return JadePersistenceManager.incIndentifiant(Compteurs.LAPRAMS_ANNONCE);
    }

    public String getIdTiersRepondant() {
        return donneePerssonnelleRequerant.getIdTiersRepondant();
    }

    public String getInteretHypoImmeuble(boolean isConjoint) throws AnnonceException {
        return process(isConjoint, new DFHDetailProcessor() {
            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {
                return new String[] { donnee.getBienImmobilierServantHabitationPrincipaleMontantInteretHypothecaire(),
                        donnee.getBienImmobilierHabitationNonPrincipaleMontantInteretHypothecaire(),
                        donnee.getBienImmobilierNonHabitableMontantInteretHypothecaire() };
            }
        });
    }

    public String getLieu(boolean isConjoint) {
        return getAdresse(isConjoint).getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE);
    }

    public String getLieuDit(boolean isConjoint) {
        return "";
    }

    public String getLoyerAnnuelReel(boolean isConjoint) throws AnnonceException {
        return process(isConjoint, new DFHDetailProcessor() {

            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {
                StrategieLoyer strategieLoyer = new StrategieLoyer();

                return new String[] { LapramsDataMediator.this.annualiseMontantMensuel(donnee.getLoyerMontantNet()) };
            }
        });
    }

    public String getMontantPCDecision(boolean isConjoint) throws AnnonceException {
        try {
            String montant = "0";
            if (isConjoint) {
                PCAccordeePlanCalculSearch search = new PCAccordeePlanCalculSearch();
                search.setForIdVersionDroit(defaultAnnonce.getPcAccordee().getSimplePCAccordee().getIdVersionDroit());
                search.setForIdPCAccordee(defaultAnnonce.getPcAccordee().getSimplePCAccordee().getIdPCAccordee());
                search.setForDateDebut(defaultAnnonce.getPcAccordee().getSimplePCAccordee().getDateDebut());
                search.setForDateFin(defaultAnnonce.getPcAccordee().getSimplePCAccordee().getDateFin());
                search.setForIsPlanRetenu(true);
                search.setWhereKey(PCAccordeePlanCalculSearch.FOR_PCA_CONJOINT);
                search = PegasusImplServiceLocator.getPCAccordeeService().search(search);
                if (search.getSize() > 0) {
                    montant = ((PCAccordeePlanCalcul) search.getSearchResults()[0]).getSimplePlanDeCalcul()
                            .getMontantPCMensuelle();
                }
            } else {
                if (defaultAnnonce.getPcAccordee().getPlanCalculs() == null) {
                    defaultAnnonce.getPcAccordee().loadPlanCalculs();
                    montant = defaultAnnonce.getPcAccordee().getPlanRetenu().getMontantPCMensuelle();
                }
            }
            return annualiseMontantMensuel(montant);
        } catch (NumberFormatException e) {
            throw new AnnonceException("A number format exception happened!", e);
        } catch (PCAccordeeException e) {
            throw new AnnonceException("An error happened while loading a plan calcul!", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AnnonceException("Could not load plan calcul!", e);
        } catch (JadePersistenceException e) {
            throw new AnnonceException("Could not load plan calcul!", e);
        }
    }

    public String getNoAvs(boolean isConjoint) {
        return getPersonneEtendue(isConjoint).getPersonneEtendue().getNumAvsActuel();
    }

    public String getNoAVSPC() {
        return getNoAvs(false); // retourne le no AVS du requerant
    }

    public String getNoEMSRequerant() throws AnnonceException {
        CalculDonneesHome chambre = getChambreRequerant();
        if ((chambre != null) && (homes != null) && (homes.getSize() > 0)) {
            CalculDonneesHome home = null;
            for (JadeAbstractModel model : homes.getSearchResults()) {
                home = (CalculDonneesHome) model;
                if (home.getIdTaxesJournaliere().equals(chambre.getIdTaxesJournaliere())) {
                    break;
                }
            }
            return home.getNumeroIdentification();
        } else {
            return "";
        }
    }

    public String getNom(boolean isConjoint) {
        return getPersonneEtendue(isConjoint).getTiers().getDesignation1();
    }

    public String getNomCommuneOrigine(boolean isConjoint) {
        if (isConjoint) {
            return donneePerssonnelleConjoint.getCommuneOrigine();
        } else {
            return donneePerssonnelleRequerant.getCommuneOrigine();
        }
    }

    public String getNoPostal(boolean isConjoint) {
        HashMap<String, String> fields = getAdresse(isConjoint).getFields();
        String npa = fields.get(AdresseTiersDetail.ADRESSE_VAR_NPA);
        String npaSup = fields.get(AdresseTiersDetail.ADRESSE_VAR_NPA_SUP);

        try {
            if (Integer.valueOf(npaSup) < 10) {
                npaSup = "0" + npaSup;
            }
        } catch (Throwable e) {
            JadeCodingUtil.catchException(this, "getNoPostal", e);
        }

        return npa + npaSup;
    }

    public String getNoSerie() {
        return noSerie;
    }

    public String getNouveauAVS(boolean isConjoint) {
        return getNoAvs(isConjoint).replaceAll("\\.", "");
    }

    public String getNouveauAVSPC() {
        return getNoAVSPC().replaceAll("\\.", "");
    }

    public String getNumeroRueDomicile(boolean isConjoint) {
        return getAdresse(isConjoint).getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO);

    }

    public PersonneEtendueComplexModel getPersonneEtendue(boolean isConjoint) {
        PersonneEtendueComplexModel personne = (isConjoint ? tiersConjoint : tiersRequerant);
        return personne;
    }

    public String getPrenom(boolean isConjoint) {
        return getPersonneEtendue(isConjoint).getTiers().getDesignation2();
    }

    public String getRendementFortuneMobiliere() throws AnnonceException {
        return processDivideBy2IfHasConjoint(new DFHDetailProcessor() {
            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {
                return new String[] { donnee.getBienImmobilierServantHabitationPrincipaleMontantLoyersEncaisses(),
                        donnee.getBienImmobilierServantHabitationPrincipaleMontantSousLocation(),
                        donnee.getBienImmobilierHabitationNonPrincipaleMontantLoyersEncaisses(),
                        donnee.getBienImmobilierHabitationNonPrincipaleMontantSousLocation(),
                        donnee.getBienImmobilierNonHabitableMontantRendement() };
            }
        });
    }

    public String getRentesAVSAI() throws AnnonceException {
        return multiplyBy(processDivideBy2IfHasConjoint(new DFHDetailProcessor() {
            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {
                return new String[] { donnee.getRenteAvsAiMontant() };
            }
        }), LapramsDataMediator.NB_MOIS);
    }

    private String getRepondandField(String field) {
        if ((repondant != null) && (repondant.getFields() != null)) {
            return repondant.getFields().get(field);
        }
        return "";
    }

    public String getRepondantLieuDit() {
        return "";
    }

    public String getRepondantLocalite() {
        return getRepondandField(AdresseTiersDetail.ADRESSE_VAR_LOCALITE);
    }

    public String getRepondantNom() {
        return getRepondandField(AdresseTiersDetail.ADRESSE_VAR_D2);

    }

    public String getRepondantNpa() {
        String npa = getRepondandField(AdresseTiersDetail.ADRESSE_VAR_NPA);
        String npaSup = getRepondandField(AdresseTiersDetail.ADRESSE_VAR_NPA_SUP);

        try {
            if (Integer.valueOf(npaSup) < 10) {
                npaSup = "0" + npaSup;
            }
        } catch (Throwable e) {
            JadeCodingUtil.catchException(this, "getNoPostal", e);
        }

        return npa + npaSup;
    }

    public String getRepondantPrenom() {
        return getRepondandField(AdresseTiersDetail.ADRESSE_VAR_D1);
    }

    public String getRepondantRue() {
        return getRepondandField(AdresseTiersDetail.ADRESSE_VAR_RUE);
    }

    public String getRepondantRueNumero() {
        return getRepondandField(AdresseTiersDetail.ADRESSE_VAR_NUMERO);
    }

    public String getRevenuActivitesLucratives() throws AnnonceException {
        return processDivideBy2IfHasConjoint(new DFHDetailProcessor() {
            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {
                return new String[] { donnee.getActiviteLucrativeDependanteRevenuMontant(),
                        donnee.getActiviteLucrativeIndependanteRevenuMontant() };
            }
        });
    }

    private String getRevenusDessaisisRequerant(AnnonceLapramsMediatorDonneeFinanciere donnee) {

        String montantBrut = donnee.getDessaisissementRevenuMontantBrut();
        String deductions = donnee.getDessaisissementRevenuDeductions();
        if (!JadeNumericUtil.isNumeric(montantBrut)) {
            montantBrut = "0";
        }
        if (!JadeNumericUtil.isNumeric(deductions)) {
            deductions = "0";
        }
        return String.valueOf(Float.parseFloat(montantBrut) - Float.parseFloat(deductions));
    }

    public String getRueDomicile(boolean isConjoint) {
        return getAdresse(isConjoint).getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE);
    }

    public int getSequence() {
        return sequence;
    }

    public String getSexe(boolean isConjoint) {
        String sexe = getPersonneEtendue(isConjoint).getPersonne().getSexe();
        if (mappingCsSexe.containsKey(sexe)) {
            return mappingCsSexe.get(sexe);
        } else {
            return null;
        }
    }

    private AnnonceLapramsMediatorDonneeFinanciere getTaxeJournaliereRequerant() {

        if (taxeJournaliereHomeRequerant == null) {
            for (JadeAbstractModel absDonnee : LapramsDataMediator.this.dfMediatorSearch.getSearchResults()) {
                AnnonceLapramsMediatorDonneeFinanciere donnee = (AnnonceLapramsMediatorDonneeFinanciere) absDonnee;
                if (IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE.equals(donnee.getCsTypeDonneeFinanciere())
                        && LapramsDataMediator.this.isDonneeDetailBeneficiaire(false, donnee)) {
                    return donnee;
                }
            }
        }

        return taxeJournaliereHomeRequerant;
    }

    public String getTypeCoupleRequerant() {
        if (hasConjoint()) {
            // recherche les éventuelles taxes journalières pour déterminer si le conjoint est dans un home
            boolean isConjointHome = false;
            for (JadeAbstractModel absDonnee : dfMediatorSearch.getSearchResults()) {
                AnnonceLapramsMediatorDonneeFinanciere donnee = (AnnonceLapramsMediatorDonneeFinanciere) absDonnee;
                if (IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE.equals(donnee.getCsTypeDonneeFinanciere())
                        && isDonneeDetailConjoint(donnee)) {
                    isConjointHome = true;
                    break;
                }
            }
            if (isConjointHome) {
                // si conjoint au home (hébergé)
                return LapramsDataMediator.CS_TYPLE_COUPLE_CONJOINT_HOME;
            } else {
                // si conjoint à domicile
                return LapramsDataMediator.CS_GENRE_ANNONCE_FIN_DROIT;
            }
        } else {
            // si personne seule
            return " ";
        }
    }

    public String getValeurFiscaleImmeubles() throws AnnonceException {
        return processBoth(new DFHDetailProcessor() {
            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {
                return new String[] { donnee.getBienImmobilierServantHabitationPrincipaleMontantValeurFiscale()
                // donnee.getBienImmobilierHabitationNonPrincipaleMontantValeurFiscale(),
                // donnee.getBienImmobilierNonHabitableValeurFiscale()
                };

            }
        });
    }

    public String getValeurLocativeLogement(boolean isConjoint) throws AnnonceException {
        return process(isConjoint, new DFHDetailProcessor() {
            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {
                return new String[] { donnee.getBienImmobilierServantHabitationPrincipaleMontantValeurLocative() };
            }
        });
    }

    public String getValeurVenaleImmeubles() throws AnnonceException {
        return processBoth(new DFHDetailProcessor() {
            @Override
            public String[] getFields(AnnonceLapramsMediatorDonneeFinanciere donnee) {
                return new String[] { donnee.getBienImmobilierNonHabitableValeurVenale(),
                        donnee.getBienImmobilierHabitationNonPrincipaleValeurVenale() };

            }
        });
    }

    private SimpleVariableMetier getVariableMetier(String codeSystem) {
        for (JadeAbstractModel absDonnee : variablesMetier.getSearchResults()) {
            SimpleVariableMetier donnee = (SimpleVariableMetier) absDonnee;
            if (donnee.getCsTypeVariableMetier().equals(codeSystem)) {
                return donnee;
            }
        }

        return null;
    }

    public boolean hasConjoint() {
        return tiersConjoint != null;
    }

    public boolean hasJoursAppoint() {
        return defaultAnnonce.getPcAccordee().getSimplePCAccordee().getHasJoursAppoint();
    }

    public boolean isBothInHome() {
        return getTypeCoupleRequerant().equals(LapramsDataMediator.CS_TYPLE_COUPLE_CONJOINT_HOME);
    }

    public boolean isConjointDead() {
        return !JadeStringUtil.isBlankOrZero(tiersConjoint.getPersonne().getDateDeces());
    }

    public boolean isConjointDomicile() {

        // cherche s'il y a une taxe journalière pour le conjoint
        for (JadeAbstractModel absDonnee : LapramsDataMediator.this.dfMediatorSearch.getSearchResults()) {
            AnnonceLapramsMediatorDonneeFinanciere donnee = (AnnonceLapramsMediatorDonneeFinanciere) absDonnee;

            if (isDonneeDetailConjoint(donnee) && !JadeStringUtil.isBlankOrZero(donnee.getIdTaxeJournaliereHome())) {
                return false;
            }
        }
        return true; // pas de taxe journalière trouvée, donc le conjoint est à domicile
    }

    public boolean isDecisionDeSuppression() {

        // verifie les cas de figure de sortie du droit PC home

        if (IPCDecision.CS_TYPE_SUPPRESSION_SC.equals(defaultAnnonce.getSimpleDecisionHeader().getCsTypeDecision())) {
            return true;
        }

        // ce code n'est pas util car les anonces sont filtré lors da la validation des décisions.

        // if ((this.listePCAccordees.size() == 2)
        // && JadeStringUtil.isBlankOrZero(this.defaultAnnonce.getSimpleAnnonceLaprams().getCsMotifSuppression())) {
        //
        // PCAccordee pcaCourante = this.listePCAccordees.get(0);
        // PCAccordee pcaPrecedente = this.listePCAccordees.get(1);
        //
        // // verifie cas de figure retour à domicile
        // if (IPCPCAccordee.CS_GENRE_PC_HOME.equals(pcaPrecedente.getSimplePCAccordee().getCsGenrePC())
        // && IPCPCAccordee.CS_GENRE_PC_DOMICILE.equals(pcaCourante.getSimplePCAccordee().getCsGenrePC())) {
        // return false;
        // }
        //
        // }

        return false;
    }

    public boolean isDecisionModifDece() {
        return IPCDecision.CS_MOTIF_SUPPRESSION_DECES
                .equals(defaultAnnonce.getSimpleDecisionSuppression().getCsMotif());
    }

    private boolean isDonneeDetailBeneficiaire(boolean isConjoint, AnnonceLapramsMediatorDonneeFinanciere donnee) {
        if (isConjoint) {
            return isDonneeDetailConjoint(donnee);
        } else {
            return isDonneeDetailRequerant(donnee);
        }
    }

    private boolean isDonneeDetailConjoint(AnnonceLapramsMediatorDonneeFinanciere donnee) {
        return donnee.getMembreFamilleEtendu().getDroitMembreFamille().getSimpleDroitMembreFamille()
                .getIdDroitMembreFamille().equals(defaultAnnonce.getSimpleAnnonceLaprams().getIdDroitMbrFamConjoint());

        // return IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(donnee.getMembreFamilleEtendu().getDroitMembreFamille()
        // .getSimpleDroitMembreFamille().getCsRoleFamillePC());
    }

    private boolean isDonneeDetailRequerant(AnnonceLapramsMediatorDonneeFinanciere donnee) {

        return donnee.getMembreFamilleEtendu().getDroitMembreFamille().getSimpleDroitMembreFamille()
                .getIdDroitMembreFamille().equals(defaultAnnonce.getSimpleAnnonceLaprams().getIdDroitMbrFamRequerant());

        // return IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(donnee.getMembreFamilleEtendu().getDroitMembreFamille()
        // .getSimpleDroitMembreFamille().getCsRoleFamillePC());
    }

    public boolean isRequerantDead() {
        return !JadeStringUtil.isBlankOrZero(tiersRequerant.getPersonne().getDateDeces());
    }

    private String montantFractionne(String montant, String numerateur, String denominateur, String csTypePropriete) {
        if (!JadeNumericUtil.isNumeric(montant)
                || IPCDonneeFinanciere.CS_TYPE_PROPRIETE_USUFRUITIER.equals(csTypePropriete)
                || IPCDonneeFinanciere.CS_TYPE_PROPRIETE_NU_PROPRIETAIRE.equals(csTypePropriete)) {
            return "0";
        }

        return String.valueOf((Float.parseFloat(montant) * Float.parseFloat(numerateur))
                / Float.parseFloat(denominateur));
    }

    private float montantStrToFloat(String montant) {
        if (!JadeNumericUtil.isNumeric(montant)) {
            return 0;
        }
        return Float.parseFloat(montant);
    }

    private String multiplyBy(String montant, int multiplicand) {
        return new BigDecimal(montant).multiply(new BigDecimal(multiplicand)).toString();
    }

    public String process(boolean isConjoint, DFHDetailProcessor pr) throws AnnonceException {
        return pr.build(isConjoint, false);
    }

    public String processBoth(DFHDetailProcessor pr) throws AnnonceException {
        return pr.build(false, true);
    }

    private String processDivideBy2IfHasConjoint(DFHDetailProcessor pr) throws AnnonceException {
        if (hasConjoint()) {
            return new BigDecimal(processBoth(pr)).divide(new BigDecimal(2)).toString();
        } else {
            return processBoth(pr);
        }
    }

    private void retrieveDetailDonneesFinancieres() throws JadeApplicationServiceNotAvailableException,
            AnnonceException {
        try {

            retriveHome();
            // chargement des variables métier
            variablesMetier = new SimpleVariableMetierSearch();
            variablesMetier.setWhereKey("withDateValable");
            variablesMetier.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            variablesMetier.setForDateValable(defaultAnnonce.getSimpleDecisionHeader().getDateDebutDecision());
            variablesMetier = PegasusImplServiceLocator.getSimpleVariableMetierService().search(variablesMetier);

            // chargement de l'historique de la variable metier d'amortissement
            amortissements = new TreeMap<Long, String>();
            SimpleVariableMetierSearch varMetAmortissementSearch = new SimpleVariableMetierSearch();
            varMetAmortissementSearch
                    .setForforCsTypeVariableMetier(IPCVariableMetier.CS_AMORTISSEMENT_ANNUEL_DESSAISISSEMENT_FORTUNE);
            varMetAmortissementSearch = PegasusImplServiceLocator.getSimpleVariableMetierService().search(
                    varMetAmortissementSearch);
            for (JadeAbstractModel absVar : varMetAmortissementSearch.getSearchResults()) {
                SimpleVariableMetier varMet = (SimpleVariableMetier) absVar;
                String dateDebut = varMet.getDateDebut();
                Calendar cal = JadeDateUtil.getGlobazCalendar(PegasusDateUtil.convertToJadeDate(dateDebut));

                amortissements.put(cal.getTimeInMillis(), varMet.getMontant());
            }

        } catch (CalculException e) {
            throw new AnnonceException("An error happened while searching homes!", e);
        } catch (JadePersistenceException e) {
            throw new AnnonceException("A persistence error happened while searching data for laprams mediator!", e);
        } catch (VariableMetierException e) {
            throw new AnnonceException("An error happened while searching variables metier!", e);
        }
    }

    private void retrieveTiers() throws AnnonceException {
        try {
            retriveDonneePersonnell();
            adresseTiersRequerant = retriveTiersAdresse(donneePerssonnelleRequerant.getIdTiers());
            tiersRequerant = TIBusinessServiceLocator.getPersonneEtendueService().read(
                    donneePerssonnelleRequerant.getIdTiers());
            if ((donneePerssonnelleConjoint != null)
                    && !JadeStringUtil.isBlankOrZero(donneePerssonnelleConjoint.getIdTiers())) {
                adresseTiersConjoint = retriveTiersAdresse(donneePerssonnelleConjoint.getIdTiers());
                tiersConjoint = TIBusinessServiceLocator.getPersonneEtendueService().read(
                        donneePerssonnelleConjoint.getIdTiers());
            }
            if (!JadeStringUtil.isBlankOrZero(donneePerssonnelleRequerant.getIdTiersRepondant())) {
                repondant = retriveTiersAdresse(donneePerssonnelleRequerant.getIdTiersRepondant());
            } else {
                repondant = new AdresseTiersDetail();
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AnnonceException("Service not found while searching data model!", e);
        } catch (JadePersistenceException e) {
            throw new AnnonceException("A persistence error happened while searching data model!", e);
        } catch (JadeApplicationException e) {
            throw new AnnonceException("An error happened while searching data model!", e);
        }
    }

    private void retriveDonneePersonnell() throws JadePersistenceException, AnnonceException {
        List<String> inIdDroitMembreFamille = new ArrayList<String>();
        inIdDroitMembreFamille.add(defaultAnnonce.getSimpleAnnonceLaprams().getIdDroitMbrFamRequerant());
        if (!JadeStringUtil.isBlankOrZero(defaultAnnonce.getSimpleAnnonceLaprams().getIdDroitMbrFamConjoint())) {
            inIdDroitMembreFamille.add(defaultAnnonce.getSimpleAnnonceLaprams().getIdDroitMbrFamConjoint());
        }
        DonneePersSiutationFamillialeSearch donneePersSiutationFamillialeSearch = new DonneePersSiutationFamillialeSearch();
        donneePersSiutationFamillialeSearch.setInIdDroitMbrFam(inIdDroitMembreFamille);
        donneePersSiutationFamillialeSearch = (DonneePersSiutationFamillialeSearch) JadePersistenceManager
                .search(donneePersSiutationFamillialeSearch);

        for (JadeAbstractModel model : donneePersSiutationFamillialeSearch.getSearchResults()) {
            DonneePersSiutationFamilliale donneePersonnel = (DonneePersSiutationFamilliale) model;
            if (JadeStringUtil.isBlankOrZero(donneePersonnel.getCodeCommuneOFS())) {
                donneePersonnel.setCodeCommuneOFS("");
            }
            if (donneePersonnel.getIdDroitMembreFamille().equals(
                    defaultAnnonce.getSimpleAnnonceLaprams().getIdDroitMbrFamRequerant())) {
                donneePerssonnelleRequerant = donneePersonnel;
            } else if (!JadeStringUtil.isBlankOrZero(defaultAnnonce.getSimpleAnnonceLaprams()
                    .getIdDroitMbrFamConjoint())
                    && donneePersonnel.getIdDroitMembreFamille().equals(
                            defaultAnnonce.getSimpleAnnonceLaprams().getIdDroitMbrFamConjoint())) {
                donneePerssonnelleConjoint = donneePersonnel;

            } else {
                throw new AnnonceException("Aucune donnée personnel trouver pour cette idDroitMembreFamille: "
                        + donneePersonnel.getIdDroitMembreFamille());
            }
        }

        // Si on a une décision de type suppression et que c'est que le conjoint qui est en home il faut inverser les
        // personnes car on a une seule annonce.
        if (isDecisionDeSuppression()) {
            AnnonceLapramsMediatorDonneeFinanciere taxe = getTaxeJournaliereRequerant();
            if (taxe == null) {
                DonneePersSiutationFamilliale donneePersonnelTemp = donneePerssonnelleRequerant;
                String idDroitMbrFamTemp = defaultAnnonce.getSimpleAnnonceLaprams().getIdDroitMbrFamRequerant();
                donneePerssonnelleRequerant = donneePerssonnelleConjoint;
                defaultAnnonce.getSimpleAnnonceLaprams().setIdDroitMbrFamRequerant(
                        defaultAnnonce.getSimpleAnnonceLaprams().getIdDroitMbrFamConjoint());
                defaultAnnonce.getSimpleAnnonceLaprams().setIdDroitMbrFamConjoint(idDroitMbrFamTemp);
                donneePerssonnelleConjoint = donneePersonnelTemp;
            }
        }
    }

    private void retriveDonneesFinancieres() throws AnnonceException {
        dfMediatorSearch = new AnnonceLapramsMediatorDonneeFinanciereSearch();

        dfMediatorSearch.setForInIdDroitMbrFam(new ArrayList<String>() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            {
                this.add(defaultAnnonce.getSimpleAnnonceLaprams().getIdDroitMbrFamRequerant());
                this.add(defaultAnnonce.getSimpleAnnonceLaprams().getIdDroitMbrFamConjoint());
            }
        });

        dfMediatorSearch.setForIdAnnonce(defaultAnnonce.getSimpleAnnonceLaprams().getIdAnnonceLAPRAMS());
        try {
            dfMediatorSearch = PegasusImplServiceLocator.getAnnonceLapramsService().searchDonneesFinancieresDetail(
                    dfMediatorSearch);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AnnonceException("An error happened while searching donnees financiere!", e);
        } catch (JadePersistenceException e) {
            throw new AnnonceException("An error happened while searching donnees financiere!", e);
        }
    }

    private void retriveHome() throws CalculException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        List<String> listIdTypeChambre = new ArrayList<String>();

        for (JadeAbstractModel absDonnee : dfMediatorSearch.getSearchResults()) {
            AnnonceLapramsMediatorDonneeFinanciere donnee = (AnnonceLapramsMediatorDonneeFinanciere) absDonnee;

            if (IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE.equals(donnee.getCsTypeDonneeFinanciere())) {
                listIdTypeChambre.add(donnee.getTaxeJournaliereHomeIdTypeChambre());
            }
        }

        if (listIdTypeChambre.isEmpty()) {
            retriveTaxeJournaliere();
            listIdTypeChambre.add(taxeJournaliereHomeRequerant.getTaxeJournaliereHomeIdTypeChambre());
        }

        // chargement des homes
        homes = new CalculDonneesHomeSearch();
        if (listIdTypeChambre.size() > 0) {
            homes.setInIdTypeChambre(listIdTypeChambre);
            homes.setForDateDebut(defaultAnnonce.getPcAccordee().getSimplePCAccordee().getDateDebut());
            homes.setForDateFin(defaultAnnonce.getPcAccordee().getSimplePCAccordee().getDateDebut());
            homes.setForIdVersionDroit(defaultAnnonce.getPcAccordee().getSimpleVersionDroit().getIdVersionDroit());
            homes.setWhereKey(CalculDonneesHomeSearch.SEARCH_HOME_FOR_LAPRAMS);
            homes.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            homes = PegasusImplServiceLocator.getCalculDonneesHomeService().search(homes);
        }
    }

    private void retriveTaxeJournaliere() {
        TaxeJournaliereHomeDroitSearch search = new TaxeJournaliereHomeDroitSearch();
        if (isDecisionDeSuppression()) {
            search.setForIdDroit(defaultAnnonce.getSimpleVersionDroitForSuppression().getIdDroit());
            search.setForNoVersionDroit(defaultAnnonce.getSimpleVersionDroitForSuppression().getNoVersion());
        } else {
            search.setForIdDroit(defaultAnnonce.getPcAccordee().getSimpleDroit().getIdDroit());
            search.setForNoVersionDroit(defaultAnnonce.getPcAccordee().getSimpleVersionDroit().getNoVersion());
        }
        // String date = JadeDateUtil.addMonths("01."
        // + this.defaultAnnonce.getSimpleDecisionHeader().getDateDebutDecision(), -1);
        // search.setForDate(date.substring(3));
        search.setForIdDroitMemembreFamill(defaultAnnonce.getSimpleAnnonceLaprams().getIdDroitMbrFamRequerant());

        try {
            search.setWhereKey(TaxeJournaliereHomeDroitSearch.WITH_LAPRMAS);

            search = PegasusServiceLocator.getDroitService().search(search);
            if (search.getSize() > 0) {
                TaxeJournaliereHomeDroit taxeJournaliereHomeDroit = (TaxeJournaliereHomeDroit) search
                        .getSearchResults()[0];
                SimpleTaxeJournaliereHome taxe = taxeJournaliereHomeDroit.getSimpleTaxeJournaliereHome();
                AnnonceLapramsMediatorDonneeFinanciere donnee = new AnnonceLapramsMediatorDonneeFinanciere();

                donnee.setTaxeJournaliereHomeCsDestinationSortie(taxe.getCsDestinationSortie());
                donnee.setTaxeJournaliereHomeDateEntreeHome(taxe.getDateEntreeHome());
                donnee.setTaxeJournaliereHomeIdTypeChambre(taxe.getIdTypeChambre());
                donnee.setTaxeJournaliereHomeIsPartLCA(taxe.getIsParticipationLCA());
                donnee.setTaxeJournaliereHomePartLCA(taxe.getMontantJournalierLCA());
                donnee.setTaxeJournaliereHomePimeAPayerLCA(taxe.getPrimeAPayer());
                donnee.setIdTaxeJournaliereHome(taxe.getIdTaxeJournaliereHome());

                taxeJournaliereHomeRequerant = donnee;
            }

        } catch (HomeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TaxeJournaliereHomeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JadeApplicationServiceNotAvailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JadePersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private AdresseTiersDetail retriveTiersAdresse(String idTiers) throws JadePersistenceException,
            JadeApplicationException, JadeApplicationServiceNotAvailableException, AnnonceException {
        if (!JadeStringUtil.isBlankOrZero(idTiers)) {

            AdresseTiersDetail detailTiers = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTiers,
                    Boolean.TRUE, JACalendar.todayJJsMMsAAAA(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, AdresseService.CS_TYPE_COURRIER, null);

            if (detailTiers != null) {
                return detailTiers;
            } else {
                throw new AnnonceException("idTiers( répondant) non trouvé : " + idTiers);
            }
        } else {
            throw new AnnonceException("Tier non trouvé avec cette idTiers: " + idTiers);
        }
    }

    public void setDefaultAnnonce(AnnonceLaprams defaultAnnonce) throws JadeApplicationServiceNotAvailableException,
            AnnonceException {

        this.defaultAnnonce = defaultAnnonce;
        chambreRequerant = null;
        donneePerssonnelleRequerant = null;
        donneePerssonnelleConjoint = null;
        amortissements = null;
        dfMediatorSearch = null;
        doFinH = null;
        repondant = new AdresseTiersDetail();
        tiersRequerant = null;
        tiersConjoint = null;
        adresseTiersConjoint = null;
        adresseTiersRequerant = null;
        taxeJournaliereHomeRequerant = null;
        retriveDonneesFinancieres();
        retrieveTiers();
        retrieveDetailDonneesFinancieres();
    }

    public void setDefaultAnnonceSuppressionForConjoint(AnnonceLaprams defaultAnnonce) throws AnnonceException,
            JadeApplicationServiceNotAvailableException {
        if (isBothInHome()) {

            DonneePersSiutationFamilliale donneePersonnelTemp = donneePerssonnelleRequerant;
            String idDroitMbrFamTemp = this.defaultAnnonce.getSimpleAnnonceLaprams().getIdDroitMbrFamRequerant();
            donneePerssonnelleRequerant = donneePerssonnelleConjoint;
            this.defaultAnnonce.getSimpleAnnonceLaprams().setIdDroitMbrFamRequerant(
                    this.defaultAnnonce.getSimpleAnnonceLaprams().getIdDroitMbrFamConjoint());
            this.defaultAnnonce.getSimpleAnnonceLaprams().setIdDroitMbrFamConjoint(idDroitMbrFamTemp);
            donneePerssonnelleConjoint = donneePersonnelTemp;

            retrieveTiers();
        }
    }

    public void setNoSerie(String noSerie) {
        this.noSerie = noSerie;
    }

}
