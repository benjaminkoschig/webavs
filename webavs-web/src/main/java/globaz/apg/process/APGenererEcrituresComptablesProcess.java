/*
 * Créé le 29 août 05
 */
package globaz.apg.process;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.*;
import globaz.apg.db.lots.APFactureACompenser;
import globaz.apg.db.lots.APFactureACompenserManager;
import globaz.apg.db.lots.APLot;
import globaz.apg.db.prestation.*;
import globaz.apg.enums.APAssuranceTypeAssociation;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.module.calcul.constantes.ECanton;
import globaz.apg.pojo.AcmNeBean;
import globaz.apg.pojo.AcmNeFneBean;
import globaz.apg.pojo.ComplementBean;
import globaz.apg.properties.APProperties;
import globaz.apg.services.APRechercherAssuranceFromDroitCotisationService;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BISession;
import globaz.globall.db.*;
import globaz.globall.util.JADate;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.naos.api.IAFAssurance;
import globaz.naos.process.statOfas.AFStatistiquesOfasProcess;
import globaz.osiris.api.*;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.external.IntRole;
import globaz.osiris.utils.CAUtil;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;
import globaz.pyxis.util.TIToolBox;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Process effectuant les écritures comptable. Il procède par étapes successives :
 * <dl>
 * <dt>Etape 1</dt>
 * <dd>il regarde si le lot qu'on est en train de traiter est un lot APG ou maternité (les rubriques en compta sont
 * différentes)</dd>
 * <dt>Etape 2</dt>
 * <dd>instanciation du processus de compta</dd>
 * <dt>Etape 3</dt>
 * <dd>Initialisation des rubriques une fois pour tout le process.</dd>
 * <dt>Etape 4</dt>
 * <dd>Regroupement des répartitions (méthode {@link #getMapRepartitions() getMapRepartitions()}). On obtient une map
 * contenant en clef une instance de {@link globaz.apg.process.Key Key} et contenant des listes de répartitions.
 * Concrètement, les répartitions de paiement du lot que l'on est en train de traiter sont regroupées par idTiers(du
 * bénéficiaire), idAffilie(du bénéficiaire). Dans le cas d'un bénéficiaire de type employeur non affilié, l'id tiers de
 * la clé est remplacé par l'id tiers de l'assuré de base; car c'est sur le compte annexe de l'assuré que doivent
 * s'effectuer les écritures comptables, avec un ordre de versement sur l'adresse de l'employeur non affilié. Si un
 * employeur non affilié à des dettes à compenser, elles ne seront pas effectuées car l'idTiers de cet employeur est
 * remplacé par celui de l'assuré. Cela dit, un non affilié ne devrait jamais avoir de dettes à compenser.</dd>
 * <dt>Etape 5</dt>
 * <dd>Cette map de listes de répartitions est ensuite parcourue pour ajouter a chaque répartition la liste de
 * ventilations lui correspondant {@link #createVentilations(Map) createVentilations(Map)}</dd>
 * <dt>Etape 6</dt>
 * <dd>Création d'une autre map ayant les mêmes clefs que la map des répartitions et en valeur des listes de
 * compensations ( {@link #createCompensations(Map) createCompensations(Map)}). On a donc à ce moment une map contenant
 * des listes de répartitions avec leurs ventilations regroupées par idTiers, idAffilie, etc. et une map contenant les
 * compensations correspondantes.</dd>
 * <dt>Etape 7</dt>
 * <dd>génération des écritures pour chaque regroupement de répartitions. cette étape est elle même composée de
 * plusieurs étapes
 * <ul>
 * <li>Regroupement pour chaque groupement de répartition des cotisations par année de cotisation, et des montants
 * bruts. ces regroupements sont faits en différenciant les prestations normales des prestations de restitutions qui
 * doivent être écrits différemment en compta</li>
 * <li>Versement des ventilations de la répartition qu'on est en train de traiter</li>
 * <li>Ecriture des montants bruts et des restitutions sur les rubriques concernées</li>
 * <li>Ecriture des cotisations pour la répartition en cours</li>
 * <li>Ecriture des compensations pour la répartition en cours</li>
 * <li>Versement effectif de ce qui reste a verser après cotisations, ventilations, compensations</li>
 * </ul>
 * </dd>
 * </dl>
 *
 * @author dvh
 */
public class APGenererEcrituresComptablesProcess extends BProcess {

    /**
     * Une classe représentant une compensation
     */
    private class Compensation {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        public String genrePrestation = "";
        public String idFacture;
        public boolean isCompensationPourRoleAffiliePersonnel = false;
        public String montant;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        public Compensation(final String montant, final String idFacture, final String genrePrestation,
                            final boolean isCompensationPourRoleAffiliePersonnel) {
            this.montant = montant;
            this.idFacture = idFacture;
            this.isCompensationPourRoleAffiliePersonnel = isCompensationPourRoleAffiliePersonnel;
            this.genrePrestation = genrePrestation;
        }
    }

    // Inner class
    class CompensationInterne {
        public String idCompteAnnexe = null;
        public String idSectionNormale = null;
        ;
        public String idSectionRestitution = null;
        public Montants montantTotPrestations = new Montants();
        public Montants montantTotRestitutions = new Montants();
        public String typeAssociation = "";
    }

    class CotisationFNE {

        public FWCurrency montantBrutCotisation = new FWCurrency("0");
        public FWCurrency montantCotisation = new FWCurrency("0");
        public BigDecimal tauxCotisation = new BigDecimal("0");

    }

    // classe interne à la méthode, elle ne sert qu'ici, pour regrouper les
    // montants par rubrique et section (apg
    // ou affiliation);
    class KeyRegroupementRubriqueConcernee {
        APIRubrique rubrique;
        String section;

        KeyRegroupementRubriqueConcernee(final APIRubrique rubrique, final String section) {
            this.rubrique = rubrique;
            this.section = section;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(final Object obj) {
            final KeyRegroupementRubriqueConcernee key = (KeyRegroupementRubriqueConcernee) obj;

            return (rubrique.getId().equals(key.rubrique.getId())) && section.equals(key.section);
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return rubrique.getId().hashCode();
        }
    }

    // Inner class
    class Montants {
        public static final String TYPE_ACM = "ACM";
        public static final String TYPE_ACM_NE = "ACM_NE";
        public static final String TYPE_AMAT = "AMATF";
        public static final String TYPE_APG = "APG";
        public static final String TYPE_LAMAT = "LAMAT";
        public static final String TYPE_COMPCIAB = "COMPCIAB";
        public static final String TYPE_PANDEMIE = "PANDEMIE";
        public static final String TYPE_MATCIAB = "MATCIAB";

        private final FWCurrency montantACM = new FWCurrency(0);
        private final FWCurrency montantACMNE = new FWCurrency(0);
        private final FWCurrency montantAMAT = new FWCurrency(0);
        private final FWCurrency montantAPG = new FWCurrency(0);
        private final FWCurrency montantLAMAT = new FWCurrency(0);
        private final FWCurrency montantCOMPCIAB = new FWCurrency(0);
        private final FWCurrency montantPANDEMIE = new FWCurrency(0);
        private final FWCurrency montantMATCIAB = new FWCurrency(0);

        public void add(final Montants montant) {
            montantACMNE.add(montant.montantACMNE);
            montantACM.add(montant.montantACM);
            montantLAMAT.add(montant.montantLAMAT);
            montantAMAT.add(montant.montantAMAT);
            montantAPG.add(montant.montantAPG);
            montantCOMPCIAB.add(montant.montantCOMPCIAB);
            montantPANDEMIE.add(montant.montantPANDEMIE);
            montantMATCIAB.add(montant.montantMATCIAB);
        }

        /**
         * @param type    Valeur possible : TYPE_ACM, TYPE_LAMAT, TYPE_APG, TYPE_AMAT, TYPE_ACM_NE, TYPE_COMPCIAB, TYPE_PANDEMIE
         * @param montant
         */
        public void add(final String type, final FWCurrency montant) {
            if (Montants.TYPE_ACM.equals(type)) {
                montantACM.add(montant);
            } else if (Montants.TYPE_LAMAT.equals(type)) {
                montantLAMAT.add(montant);
            } else if (Montants.TYPE_AMAT.equals(type)) {
                montantAMAT.add(montant);
            } else if (Montants.TYPE_APG.equals(type)) {
                montantAPG.add(montant);
            } else if (Montants.TYPE_ACM_NE.equals(type)) {
                montantACMNE.add(montant);
            } else if (Montants.TYPE_COMPCIAB.equals(type)) {
                montantCOMPCIAB.add(montant);
            } else if (Montants.TYPE_PANDEMIE.equals(type)) {
                montantPANDEMIE.add(montant);
            } else if (Montants.TYPE_MATCIAB.equals(type)) {
                montantMATCIAB.add(montant);
            }
        }

        /**
         * @param type    Valeur possible : TYPE_ACM, TYPE_LAMAT, TYPE_APG, TYPE_AMAT, TYPE_ACM_NE, TYPE_COMPCIAB
         * @param montant
         */
        public void add(final String type, final String montant) {
            this.add(type, new FWCurrency(montant));
        }

        public FWCurrency getMontant(final String type) {

            if (Montants.TYPE_ACM.equals(type)) {
                return new FWCurrency(montantACM.toString());
            } else if (Montants.TYPE_LAMAT.equals(type)) {
                return new FWCurrency(montantLAMAT.toString());
            } else if (Montants.TYPE_AMAT.equals(type)) {
                return new FWCurrency(montantAMAT.toString());
            } else if (Montants.TYPE_APG.equals(type)) {
                return new FWCurrency(montantAPG.toString());
            } else if (Montants.TYPE_ACM_NE.equals(type)) {
                return new FWCurrency(montantACMNE.toString());
            } else if (Montants.TYPE_COMPCIAB.equals(type)) {
                return new FWCurrency(montantCOMPCIAB.toString());
            } else if (Montants.TYPE_PANDEMIE.equals(type)) {
                return new FWCurrency(montantPANDEMIE.toString());
            } else if (Montants.TYPE_MATCIAB.equals(type)) {
                return new FWCurrency(montantMATCIAB.toString());
            } else {
                return null;
            }
        }

        /**
         * @param expression format exemple : ACM_APG_AMATF_LAMAT
         * @return
         */

        public FWCurrency getMontantCumule(final String expression) {

            final FWCurrency result = new FWCurrency(0);

            if (expression.indexOf(Montants.TYPE_ACM) != -1) {
                result.add(montantACM);
            }
            if (expression.indexOf(Montants.TYPE_AMAT) != -1) {
                result.add(montantAMAT);
            }
            if (expression.indexOf(Montants.TYPE_APG) != -1) {
                result.add(montantAPG);
            }
            if (expression.indexOf(Montants.TYPE_LAMAT) != -1) {
                result.add(montantLAMAT);
            }
            if (expression.indexOf(Montants.TYPE_ACM_NE) != -1) {
                result.add(montantACMNE);
            }
            if (expression.indexOf(Montants.TYPE_COMPCIAB) != -1) {
                result.add(montantCOMPCIAB);
            }
            if (expression.indexOf(Montants.TYPE_PANDEMIE) != -1) {
                result.add(montantPANDEMIE);
            }
            if (expression.indexOf(Montants.TYPE_MATCIAB) != -1) {
                result.add(montantMATCIAB);
            }
            return result;
        }

        public boolean isZero() {
            return montantACM.isZero() && montantACMNE.isZero() && montantAMAT.isZero() && montantAPG.isZero()
                    && montantLAMAT.isZero() && montantCOMPCIAB.isZero() && montantPANDEMIE.isZero() && montantMATCIAB.isZero();
        }

        public void sub(final Montants montant) {
            montantACMNE.sub(montant.montantACMNE);
            montantACM.sub(montant.montantACM);
            montantLAMAT.sub(montant.montantLAMAT);
            montantAMAT.sub(montant.montantAMAT);
            montantAPG.sub(montant.montantAPG);
            montantCOMPCIAB.sub(montant.montantCOMPCIAB);
            montantPANDEMIE.sub(montant.montantPANDEMIE);
            montantMATCIAB.sub(montant.montantMATCIAB);
        }

    }

    /**
     * classe représentant une répartition de paiement.
     */
    private class Repartition {

        public String anneeCotisation = "";
        public String cotisationAC = "";
        public String cotisationAVS = "";
        public String cotisationFNE = "";
        public String cotisationLFA = "";
        public String fraisAdministration = "";
        public String genrePrestation = "";
        public String idAdressePaiement = "";
        public String idCompensation = "";
        public String idDepartement = "";
        public String idRepartitionPaiement = "";
        public String impotSource = "";
        public boolean isEmployeur = false;
        public boolean isIndependant = false;
        public boolean isMaternite = false;
        public boolean isRestitution = false;
        public String montant = "";
        public String montantBrutCotisationFNE = "";
        public APIRubrique rubriqueConcernee = null;
        public String section = "";
        public String tauxCotisationFNE = "";
        public String typeAssociation = "";
        public List ventilations = new ArrayList();
        public String idDroit = "";
    }

    /**
     * classe représentant une ventilation
     */
    private class Ventilation {
        public String genrePrestation = "";
        public String idAdressePaiement = "";
        public String montant = "";
        public String referenceInterne = "";

        public Ventilation(final String montant, final String genrePrestation, final String idAdressePaiement,
                           final String referenceInterne) {
            this.montant = montant;
            this.idAdressePaiement = idAdressePaiement;
            this.genrePrestation = genrePrestation;
            this.referenceInterne = referenceInterne;
        }
    }

    private static final String COMPENSATION_ACMNE = "COMPENSATION_ACMNE";
    private static final String PROP_NATURE_VERSEMENT_ACM_APG = "nature.versement.acm.apg";
    ;
    private static final String PROP_NATURE_VERSEMENT_ACM_MAT = "nature.versement.acm.maternite";
    private static final String PROP_NATURE_VERSEMENT_AMAT = "nature.versement.maternite";
    private static final String PROP_NATURE_VERSEMENT_APG = "nature.versement.apg";
    private static final String PROP_NATURE_VERSEMENT_LAMAT = "nature.versement.lamat";
    private static final String PROP_NATURE_VERSEMENT_PANDEMIE = "nature.versement.pandemie";
    private static final String PROP_RUBRIQUE_COMPENSATION = "rubrique.compensation.standard";
    private static final String PROP_RUBRIQUE_COMPENSATION_ACM = "rubrique.compensation.acm";
    public static final String REQUETE_SQL_CAISSE_PROF_COL_NAME_ID_TIERS_ADMINISTRATION = "ID_TIERS_ADMINISTRATION";

    private static final long serialVersionUID = -7018310890658933220L;

    public static void main(final String[] args) throws InstantiationException, IllegalAccessException {
        final Map<String, Montants> mapMontants = new HashMap<String, APGenererEcrituresComptablesProcess.Montants>();

        final APGenererEcrituresComptablesProcess ec = new APGenererEcrituresComptablesProcess();

        final Montants m1 = ec.new Montants();
        m1.add(Montants.TYPE_ACM, new FWCurrency("100"));

        final Montants m2 = ec.new Montants();
        m2.add(Montants.TYPE_ACM, new FWCurrency("200"));
        m1.add(m2);

        m2.add(Montants.TYPE_ACM, new FWCurrency("200"));

        mapMontants.put("m1", m1);
        mapMontants.put("m1", m2);
        System.out.println(mapMontants.get("m1").getMontant(Montants.TYPE_ACM));
    }

    public enum TypeComplement {
        JU_PARITAIRE(ECanton.JU, "PARITAIRE"),
        JU_PERSONNEL(ECanton.JU, "PERSONNEL"),
        BE_PARITAIRE(ECanton.BE, "PARITAIRE"),
        BE_PERSONNEL(ECanton.BE, "PERSONNEL");

        private TypeComplement(ECanton canton, String genre) {
            this.canton = canton;
            this.genre = genre;
        }

        private final ECanton canton;
        private final String genre;

        public ECanton getCanton() {
            return canton;
        }

        public String getGenre() {
            return genre;
        }
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    // ACM
    private APIRubrique ACM_MONTANT_BRUT = null;
    private APIRubrique ACM_RESTITUTION = null;
    private APIRubrique AVEC_AC_ASSURE;
    private APIRubrique AVEC_AC_INDEPENDANT = null;
    private APIRubrique AVEC_AC_EMPLOYEUR = null;
    private APIRubrique SANS_AC_ASSURE = null;
    private APIRubrique SANS_AC_INDEPENDANT = null;
    private APIRubrique SANS_AC_EMPLOYEUR = null;
    private APIRubrique COMPENSATION = null;
    private APIRubrique COMPENSATION_ACM = null;
    private APIRubrique COMPENSATION_LAMAT = null;
    private APIRubrique COT_AC_ASSURE = null;
    private APIRubrique COT_AC_INDEPENDANT = null;
    private APIRubrique COT_AVS_ASSURE = null;
    private APIRubrique COT_AVS_INDEPENDANT = null;
    private APIRubrique COT_LFA = null;
    private APIRubrique IMPOT_SOURCE_ASSURE = null;
    private APIRubrique IMPOT_SOURCE_INDEPENDANT = null;
    private APIRubrique IMPOT_SOURCE_ACM = null;
    private APIRubrique IMPOT_SOURCE_LAMAT_CANTONALE_ASSURE = null;
    private APIRubrique IMPOT_SOURCE_LAMAT_CANTONALE_INDEPENDANT = null;
    private APIRubrique FONDS_DE_COMPENSATION_ASSURE = null;
    private APIRubrique FONDS_DE_COMPENSATION_EMPLOYEUR = null;
    private APIRubrique FONDS_DE_COMPENSATION_INDEPENDANT = null;
    private APIRubrique FONDS_DE_COMPENSATION_ACM = null;
    private APIRubrique FRAIS_ADMINISTRATION = null;
    private APIRubrique PRESTATION_A_RESTITUER_ASSURE = null;
    private APIRubrique PRESTATION_A_RESTITUER_EMPLOYEUR = null;
    private APIRubrique PRESTATION_A_RESTITUER_INDEPENDANT = null;
    private APIRubrique PRESTATION_A_RESTITUER_LAMAT_ASSURE = null;
    private APIRubrique PRESTATION_A_RESTITUER_LAMAT_EMPLOYEUR = null;
    private APIRubrique PRESTATION_A_RESTITUER_LAMAT_INDEPENDANT = null;
    private APIRubrique SANS_COTISATION = null;
    private APIRubrique SANS_COTISATION_LAMAT_ADOPTION_ASSURE = null;
    private APIRubrique SANS_COTISATION_LAMAT_ADOPTION_EMPLOYEUR = null;
    private APIRubrique SANS_COTISATION_LAMAT_ADOPTION_INDEPENDANT = null;
    private APIRubrique SANS_COTISATION_LAMAT_NAISSANCE_ASSURE = null;
    private APIRubrique SANS_COTISATION_LAMAT_NAISSANCE_EMPLOYEUR = null;
    private APIRubrique SANS_COTISATION_LAMAT_NAISSANCE_INDEPENDANT = null;
    private APIRubrique INDEMN_GARDE_ENFANT_SALARIE;
    private APIRubrique INDEMN_GARDE_ENFANT_INDEPENDANT;
    private APIRubrique INDEMN_QUARANTAINE_SALARIE;
    private APIRubrique INDEMN_QUARANTAINE_INDEPENDANT;
    private APIRubrique INDEMN_FERMETURE_FORCEE;
    private APIRubrique INDEMN_INTERDICTION_MANIFESTATION;
    private APIRubrique INDEMN_CAS_RIGUEUR_10k_90k;
    private APIRubrique INDEMN_GARDE_ENFANT_HANDICAP_SALARIE;
    private APIRubrique INDEMN_GARDE_ENFANT_HANDICAP_INDEPENDANT;
    private APIRubrique INDEMN_SALARIE_EVENEMENTIEL;
    private APIRubrique INDEMN_INDEPANDANT_FERMETURE;
    private APIRubrique INDEMN_DIRIGEANT_SALARIE_FERMETURE;
    private APIRubrique INDEMN_INDEPENDANT_MANIF_ANNULEE;
    private APIRubrique INDEMN_DIRIGEANT_SALARIE_MANIF_ANNULEE;
    private APIRubrique INDEMN_INDEPENDANT_LIMITATION_ACTIVITE;
    private APIRubrique INDEMN_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE;
    private APIRubrique INDEMN_QUARANTAINE_17_09_20_INDEPENDANT;
    private APIRubrique INDEMN_QUARANTAINE_17_09_20_SALARIE;
    private APIRubrique INDEMN_GARDE_PARENTALE_17_09_20_INDEPENDANT;
    private APIRubrique INDEMN_GARDE_PARENTALE_17_09_20_SALARIE;
    private APIRubrique INDEMN_GARDE_PARENTALE_HANDICAP_17_09_20_INDEPENDANT;
    private APIRubrique INDEMN_GARDE_PARENTALE_HANDICAP_17_09_20_SALARIE;
    private APIRubrique INDEMN_PERSONNE_VULNERABLE_SALARIE;
    private APIRubrique INDEMN_PERSONNE_VULNERABLE_INDEPENDANT;
    private APIRubrique RESTIT_GARDE_ENFANT_SALARIE;
    private APIRubrique RESTIT_GARDE_ENFANT_INDEPENDANT;
    private APIRubrique RESTIT_QUARANTAINE_SALARIE;
    private APIRubrique RESTIT_QUARANTAINE_INDEPENDANT;
    private APIRubrique RESTIT_FERMETURE_FORCEE;
    private APIRubrique RESTIT_INTERDICTION_MANIFESTATION;
    private APIRubrique RESTIT_CAS_RIGUEUR_10k_90k;
    private APIRubrique RESTIT_GARDE_ENFANT_HANDICAP_SALARIE;
    private APIRubrique RESTIT_GARDE_ENFANT_HANDICAP_INDEPENDANT;
    private APIRubrique RESTIT_SALARIE_EVENEMENTIEL;
    private APIRubrique RESTIT_INDEPANDANT_FERMETURE;
    private APIRubrique RESTIT_DIRIGEANT_SALARIE_FERMETURE;
    private APIRubrique RESTIT_INDEPENDANT_MANIF_ANNULEE;
    private APIRubrique RESTIT_DIRIGEANT_SALARIE_MANIF_ANNULEE;
    private APIRubrique RESTIT_INDEPENDANT_LIMITATION_ACTIVITE;
    private APIRubrique RESTIT_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE;
    private APIRubrique RESTIT_QUARANTAINE_17_09_20_INDEPENDANT;
    private APIRubrique RESTIT_QUARANTAINE_17_09_20_SALARIE;
    private APIRubrique RESTIT_GARDE_PARENTALE_17_09_20_INDEPENDANT;
    private APIRubrique RESTIT_GARDE_PARENTALE_17_09_20_SALARIE;
    private APIRubrique RESTIT_GARDE_PARENTALE_HANDICAP_17_09_20_INDEPENDANT;
    private APIRubrique RESTIT_GARDE_PARENTALE_HANDICAP_17_09_20_SALARIE;
    private APIRubrique RESTIT_PERSONNE_VULNERABLE_SALARIE;
    private APIRubrique RESTIT_PERSONNE_VULNERABLE_INDEPENDANT;

    private String dateComptable = "";
    private String dateSurDocument = "";
    private String idLot = "";
    private String typeLot = "";
    private final Map<String, AcmNeBean> mapAcmNeBean = new HashMap<String, AcmNeBean>();
    private final Map<String, ComplementBean> mapComplementBean = new HashMap<String, ComplementBean>();
    private String schemaDBWithTablePrefix = null;

    /**
     * Crée une nouvelle instance de la classe APGenererEcrituresComptablesProcess.
     */
    public APGenererEcrituresComptablesProcess() {
        super();
        schemaDBWithTablePrefix = TIToolBox.getCollection();
    }

    /**
     * Crée une nouvelle instance de la classe APGenererEcrituresComptablesProcess.
     *
     * @param parent DOCUMENT ME!
     */
    public APGenererEcrituresComptablesProcess(final BProcess parent) {
        super(parent);
        schemaDBWithTablePrefix = TIToolBox.getCollection();
    }

    /**
     * Crée une nouvelle instance de la classe APGenererEcrituresComptablesProcess.
     *
     * @param session DOCUMENT ME!
     */
    public APGenererEcrituresComptablesProcess(final BSession session) {
        super(session);
        schemaDBWithTablePrefix = TIToolBox.getCollection();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {

        FWMemoryLog comptaMemoryLog = new FWMemoryLog();
        boolean noErrorBeforeClose = false;

        try {

            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = initThreadContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // récupération du lot pour savoir pour la suite si c'est un lot
            // maternité ou apg
            final APLot lot = new APLot();
            lot.setSession(getSession());
            lot.setIdLot(idLot);
            lot.retrieve(getTransaction());

            typeLot = lot.getTypeLot();

            // instanciation du processus de compta
            final BISession sessionOsiris = PRSession.connectSession(getSession(), "OSIRIS");
            final APIGestionComptabiliteExterne compta = (APIGestionComptabiliteExterne) sessionOsiris
                    .getAPIFor(APIGestionComptabiliteExterne.class);

            comptaMemoryLog.setSession((BSession) sessionOsiris);
            compta.setMessageLog(comptaMemoryLog);

            compta.setDateValeur(dateComptable);
            compta.setEMailAddress(getEMailAddress());
            // compta.setMessageLog(getMemoryLog());
            compta.setSendCompletionMail(false);
            compta.setTransaction(getTransaction());
            compta.setProcess(this);
            if (lot != null) {
                compta.setLibelle(lot.getDescription());
            } else {
                compta.setLibelle("APG");
            }

            final APIJournal journal = compta.createJournal();

            // initialisation des ids des rubrique une fois pour tout le
            // process.
            initRubriques(sessionOsiris);

            // Récupération du regroupement des répartitions de paiement
            final Map repartitionsClassees = getMapRepartitions();

            // Mise ajout des ventilation pour ces répartitions de paiement
            createVentilations(repartitionsClassees);

            // Creation d'une map de listes de compensations dont les clefs sont
            // les mêmes que celles des répartition.
            // On aura ainsi pour chaque regroupement de repartitions les
            // compensations correspondantes.
            final Map compensations = createCompensations(repartitionsClassees);

            // pour chaque element de la map repartitionsclassee, on va faire
            // les ecriture comptables
            final Set keysRepartitionsClassees = repartitionsClassees.keySet();
            final Iterator repartitionsClassesIterator = keysRepartitionsClassees.iterator();

            final Map mapCompensationsInterne = new HashMap();

            while (repartitionsClassesIterator.hasNext()) {
                final Key key = (Key) repartitionsClassesIterator.next();
                final List repartitions = (List) repartitionsClassees.get(key);
                final List compensationsPourCetId = (List) compensations.get(key);

                Montants montantsBrutTotal = new Montants();
                Montants ventilationTotale = new Montants();
                Montants compensationTotale = new Montants();

                final Iterator repartitionsIterator = repartitions.iterator();

                // recherche du montant total des ventilations et du montant
                // brut total
                while (repartitionsIterator.hasNext()) {
                    final Repartition repartition = (Repartition) repartitionsIterator.next();

                    montantsBrutTotal = getCumulMontantsParGenre(montantsBrutTotal, repartition.genrePrestation,
                            repartition.montant);

                    final Iterator ventilationIterator = repartition.ventilations.iterator();

                    while (ventilationIterator.hasNext()) {
                        final Ventilation ventilation = (Ventilation) ventilationIterator.next();
                        // Montant total des ventilations, par genre de
                        // prestation
                        ventilationTotale = getCumulMontantsParGenre(ventilationTotale, ventilation.genrePrestation,
                                ventilation.montant);
                    }
                }

                final Iterator compensationsPourCetIdIterator = compensationsPourCetId.iterator();

                // recherche du montant total des compensations
                while (compensationsPourCetIdIterator.hasNext()) {
                    final Compensation compensation = (Compensation) compensationsPourCetIdIterator.next();

                    // Montant total des compensations, par genre de prestation
                    compensationTotale = getCumulMontantsParGenre(compensationTotale, compensation.genrePrestation,
                            compensation.montant);
                }

                final CompensationInterne ci = faisEcritures(repartitions, compensationsPourCetId, montantsBrutTotal,
                        ventilationTotale, compensationTotale, key, compta, sessionOsiris);

                // Cumul des compensations interne par idTiers+idAffilié.
                // La clé se fait par idTiers et idAffilié. On casse la rupture
                // sur les idAdressePmt et idDepartement.
                // Car si pour un même bénéficiaire, on verse la prestation de
                // restitution à une adresse de pmt et la prestation
                // standard sur une autre adresse, la compensation interne ne se
                // fera pas.

                if (mapCompensationsInterne.containsKey(key)) {
                    final CompensationInterne totCI = (CompensationInterne) mapCompensationsInterne.get(key);

                    if (!JadeStringUtil.isBlankOrZero(ci.typeAssociation)) {
                        totCI.typeAssociation = ci.typeAssociation;
                    }

                    totCI.montantTotPrestations.add(ci.montantTotPrestations);
                    totCI.montantTotRestitutions.add(ci.montantTotRestitutions);

                    // Si idExtra1 ou 2 n'est pas vide, cela signifie qu'il
                    // s'agit d'un employeur non affilié.
                    // Dans ce cas, il ne faut pas prendre la section du non
                    // affilié pour pour compenser la restitution,
                    // mais celle de l'assuré / affilié.
                    if (JadeStringUtil.isBlankOrZero(totCI.idSectionNormale)
                            && JadeStringUtil.isBlankOrZero(key.idExtra1) && JadeStringUtil.isBlankOrZero(key.idExtra2)) {
                        totCI.idSectionNormale = ci.idSectionNormale;
                    }

                    if (JadeStringUtil.isBlankOrZero(totCI.idSectionRestitution)
                            && JadeStringUtil.isBlankOrZero(key.idExtra1) && JadeStringUtil.isBlankOrZero(key.idExtra2)) {
                        totCI.idSectionRestitution = ci.idSectionRestitution;
                    }

                    mapCompensationsInterne.put(key, totCI);
                } else {
                    mapCompensationsInterne.put(key, ci);
                }
            }

            doCompensationsInterne(compta, mapCompensationsInterne);

            final FWMemoryLog beforeCloseComptaMemoryLog = new FWMemoryLog();
            // si pas d'erreurs avant le close, on sauvegarde les messages du
            // comptaMemoryLog
            // pour les restaurer si une erreure survient durant le close
            if (!comptaMemoryLog.hasErrors()) {
                noErrorBeforeClose = true;
                beforeCloseComptaMemoryLog.logMessage(comptaMemoryLog);

                // on memorise l'id du journal en CA pour pouvoir rediriger sur
                // celui-ci depuis les APG/Mat
                lot.setIdJournalCA(journal.getIdJournal());
                lot.update(getTransaction());
            }

            compta.comptabiliser();

            // si pas d'erreurs avant le close et en erreur après le close, on
            // restaure l'ancien
            // memory log pour masquer l'exception.
            // Elle sera directement traitee dans la compta.
            if (noErrorBeforeClose && comptaMemoryLog.hasErrors()) {
                comptaMemoryLog = beforeCloseComptaMemoryLog;
            }

        } catch (final Exception e) {
            // si l'exception survient durant le close -> noErrorBeforeClose ==
            // true, l'exception n'est pas remontee
            // Elle sera directement traitee dans la compta.
            if (!noErrorBeforeClose) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                        getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));
                e.printStackTrace();

                return false;
            }
        } finally {
            if ((comptaMemoryLog != null) && (comptaMemoryLog.size() > 0)) {

                getMemoryLog().logMessage("", FWMessage.INFORMATION,
                        ":::::::::::: START LOG OSIRIS :::::::::::::::::::::::::::::");

                for (int i = 0; i < comptaMemoryLog.size(); i++) {
                    getMemoryLog()
                            .logMessage(null, comptaMemoryLog.getMessage(i).getComplement(),
                                    comptaMemoryLog.getMessage(i).getTypeMessage(),
                                    comptaMemoryLog.getMessage(i).getIdSource());

                }
                getMemoryLog().logMessage("", FWMessage.INFORMATION,
                        ":::::::::::: END LOG OSIRIS :::::::::::::::::::::::::::::");
            }

            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        return true;
    }

    /**
     * (non-Javadoc)
     *
     * @throws Exception DOCUMENT ME!
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isIntegerEmpty(idLot)) {
            this._addError(getSession().getLabel("NO_LOT_REQUIS"));
        }

        super._validate();
    }

    private String convertGenrePrestToRubrique(final String genrePrestation) {
        // Si genre de prestation ACM ou ACM 2, les deux sont du même type "ACM" afin de les mettre dans les mêmes
        // écritures comptable (rubrique)
        if (APTypeDePrestation.ACM_ALFA.isCodeSystemEqual(genrePrestation)
                || APTypeDePrestation.ACM2_ALFA.isCodeSystemEqual(genrePrestation)) {
            return Montants.TYPE_ACM;
        } else if (APTypeDePrestation.ACM_NE.isCodeSystemEqual(genrePrestation)) {
            return Montants.TYPE_ACM_NE;
        } else if (APTypeDePrestation.COMPCIAB.isCodeSystemEqual(genrePrestation)) {
            return Montants.TYPE_COMPCIAB;
        } else if (APTypeDePrestation.JOUR_ISOLE.isCodeSystemEqual(genrePrestation)) {
            return Montants.TYPE_COMPCIAB;
        } else if (APTypeDePrestation.PANDEMIE.isCodeSystemEqual(genrePrestation)) {
            return Montants.TYPE_PANDEMIE;
        } else if (APTypeDePrestation.MATCIAB1.isCodeSystemEqual(genrePrestation)
                || APTypeDePrestation.MATCIAB2.isCodeSystemEqual(genrePrestation)) {
            return Montants.TYPE_MATCIAB;
        } else {
            return Montants.TYPE_APG;
        }
    }

    private Map createCompensations(final Map repartitionsClassees) throws Exception {
        final Map compensations = new HashMap();
        final Set keysRepartitionClassees = repartitionsClassees.keySet();
        final Iterator repartitionsClassesIterator = keysRepartitionClassees.iterator();

        while (repartitionsClassesIterator.hasNext()) {
            final List compensationsPourCetteRepartition = new ArrayList();

            final Key key = (Key) repartitionsClassesIterator.next();

            final List listDesCompensationsTraitees = new ArrayList();

            boolean isCompensationPourRoleAffiliePersonnel = false;
            // On parcours la liste de toute les répartitions pour contrôler si
            // au moins une de ces répartition
            // est versée à l'indépendant.
            // Si c'est le cas, toutes les écritures comptable devront se faire
            // sur le role : AFFILIE PERSONNEL
            final Iterator iterRepartition = ((List) repartitionsClassees.get(key)).iterator();
            while (iterRepartition.hasNext()) {
                final Repartition element = (Repartition) iterRepartition.next();
                if (element.isIndependant && element.isEmployeur) {
                    isCompensationPourRoleAffiliePersonnel = true;
                }

                final String idCompensation = element.idCompensation;

                // Les compensations sont regroupées et cumulées par
                // idTiers/idAffilie/idParticularité/idGenrePrestation
                // Les écritures sont regroupées par idTiers/idAffilie
                // Pour une clé donnée (idTiers/idAffilié), il est donc possible
                // de d'obtenir
                // plusieurs Repartition, ayant le même idCompensation.
                // Il ne faut donc pas traiter plusieurs fois cette même
                // compensation !!!.
                if (listDesCompensationsTraitees.contains(idCompensation)) {
                    // Passe à l'itération suivante
                    continue;
                } else {
                    listDesCompensationsTraitees.add(idCompensation);
                }

                // l'idCompensation peut être vide s'il s'agit d'une répartition
                // de paiement de frais de garde. Dans ce
                // cas, on ne fait rien
                if (!JadeStringUtil.isIntegerEmpty(idCompensation)) {
                    final APFactureACompenserManager factureACompenserManager = new APFactureACompenserManager();
                    factureACompenserManager.setSession(getSession());
                    factureACompenserManager.setForIdCompensationParente(idCompensation);
                    factureACompenserManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

                    for (int i = 0; i < factureACompenserManager.size(); i++) {
                        final APFactureACompenser factureACompenser = (APFactureACompenser) factureACompenserManager
                                .getEntity(i);

                        if (factureACompenser.getIsCompenser().booleanValue()) {
                            compensationsPourCetteRepartition.add(new Compensation(factureACompenser.getMontant(),
                                    factureACompenser.getIdFacture(), element.genrePrestation,
                                    isCompensationPourRoleAffiliePersonnel));
                        }
                    }
                }
            }

            if (compensations.containsKey(key) && !compensationsPourCetteRepartition.isEmpty()) {
                final List listFactureACompenser = (List) compensations.get(key);
                listFactureACompenser.addAll(compensationsPourCetteRepartition);
                compensations.put(key, listFactureACompenser);
            } else {
                compensations.put(key, compensationsPourCetteRepartition);
            }
        }

        return compensations;
    }

    /**
     * Ajoute les ventilations pour chaque repartition de paiement de chaque liste de la map donnée en parametre
     *
     * @param repartitionsClassees DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    private void createVentilations(final Map repartitionsClassees) throws Exception {
        final Set repartitionsClassesKeys = repartitionsClassees.keySet();
        final Iterator repartitionsClasseesIterator = repartitionsClassesKeys.iterator();

        while (repartitionsClasseesIterator.hasNext()) {
            final List repartitions = (List) repartitionsClassees.get(repartitionsClasseesIterator.next());
            final Iterator repartitionsIterator = repartitions.iterator();

            while (repartitionsIterator.hasNext()) {
                final Repartition repartition = (Repartition) repartitionsIterator.next();
                final String idRepartition = repartition.idRepartitionPaiement;
                final APRepartitionPaiementsManager repartitionPaiementsManager = new APRepartitionPaiementsManager();
                repartitionPaiementsManager.setSession(getSession());
                repartitionPaiementsManager.setForIdParent(idRepartition);
                repartitionPaiementsManager.find(getTransaction());

                for (int i = 0; i < repartitionPaiementsManager.size(); i++) {
                    final APRepartitionPaiements repartitionPaiementFille = (APRepartitionPaiements) repartitionPaiementsManager
                            .getEntity(i);
                    repartition.ventilations.add(new Ventilation(repartitionPaiementFille.getMontantVentile(),
                            repartition.genrePrestation, repartitionPaiementFille.loadAdressePaiement(
                            getDateComptable()).getIdAvoirPaiementUnique(), repartitionPaiementFille
                            .getReferenceInterne()));
                }
            }
        }
    }

    //
    // Compensation Interne dans CA
    // //////////////////////////////////////////////////////////////////////////////////////////
    private void doCompensationsInterne(final APIGestionComptabiliteExterne compta, final Map map) throws Exception {

        final Set keys = map.keySet();
        for (final Iterator iter = keys.iterator(); iter.hasNext(); ) {
            final Key key = (Key) iter.next();

            final CompensationInterne ci = (CompensationInterne) map.get(key);

            // S'il y a des restitutions ainsi que des prestations standard, on
            // va compenser en interne
            // ces écritures. Cela évite de faire le lettrage à la main.

            //
            // Compensation des prestations de genre ACM :
            //
            // /////////////////////////////////////////////////////////////////////////

            String propRubrique = getSession().getApplication().getProperty(
                    APGenererEcrituresComptablesProcess.PROP_RUBRIQUE_COMPENSATION_ACM);

            final boolean isPropRubriqueCompensationACMNe = APGenererEcrituresComptablesProcess.COMPENSATION_ACMNE
                    .equals(propRubrique);
            final boolean isPropRubriqueCompensationACMAlpha = "COMPENSATION_ALFA".equals(propRubrique);

            FWCurrency montantTotPrestationACMAlphaOrNe = ci.montantTotPrestations.montantACM;
            FWCurrency montantTotRestitutionACMAlphaOrNe = ci.montantTotRestitutions.montantACM;

            if (isPropRubriqueCompensationACMNe) {
                montantTotPrestationACMAlphaOrNe = ci.montantTotPrestations.montantACMNE;
                montantTotRestitutionACMAlphaOrNe = ci.montantTotRestitutions.montantACMNE;

            }

            if (!montantTotPrestationACMAlphaOrNe.isZero() && !montantTotRestitutionACMAlphaOrNe.isZero()) {

                APIRubrique rubriqueCompensationACMAlphaOrNe = COMPENSATION_ACM;
                if (isPropRubriqueCompensationACMNe) {
                    rubriqueCompensationACMAlphaOrNe = mapAcmNeBean.get(ci.typeAssociation).getRubriqueCompensation();
                }

                montantTotRestitutionACMAlphaOrNe.abs();
                montantTotPrestationACMAlphaOrNe.abs();

                FWCurrency plusPetitDesMontantAbs = null;
                // prendre le plus petit des montant en val. abs
                if (montantTotPrestationACMAlphaOrNe.compareTo(montantTotRestitutionACMAlphaOrNe) < 0) {
                    plusPetitDesMontantAbs = montantTotPrestationACMAlphaOrNe;
                } else {
                    plusPetitDesMontantAbs = montantTotRestitutionACMAlphaOrNe;
                }

                final FWCurrency montantInverse = new FWCurrency(plusPetitDesMontantAbs.toString());
                montantInverse.negate();

                getMemoryLog().logMessage("Compensations internes à la CA >>>>>>>>", FWMessage.INFORMATION,
                        getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));

                if ("RUBRIQUE_DE_LISSAGE".equals(propRubrique)) {
                    doEcriture(compta, plusPetitDesMontantAbs.toString(), COMPENSATION, ci.idCompteAnnexe,
                            ci.idSectionRestitution, null);

                    doEcriture(compta, montantInverse.toString(), COMPENSATION, ci.idCompteAnnexe, ci.idSectionNormale,
                            null);
                } else if (isPropRubriqueCompensationACMAlpha || isPropRubriqueCompensationACMNe) {
                    doEcriture(compta, plusPetitDesMontantAbs.toString(), rubriqueCompensationACMAlphaOrNe,
                            ci.idCompteAnnexe, ci.idSectionRestitution, null);

                    doEcriture(compta, montantInverse.toString(), rubriqueCompensationACMAlphaOrNe, ci.idCompteAnnexe,
                            ci.idSectionNormale, null);
                }

            }

            //
            // Compensation des prestations de genre Complément (COMPCIAB) :
            //
            // /////////////////////////////////////////////////////////////////////////

            propRubrique = getSession().getApplication().getProperty(
                    APGenererEcrituresComptablesProcess.PROP_RUBRIQUE_COMPENSATION);

            if (!ci.montantTotPrestations.montantCOMPCIAB.isZero() && !ci.montantTotRestitutions.montantCOMPCIAB.isZero()) {

                ci.montantTotRestitutions.montantCOMPCIAB.abs();
                ci.montantTotPrestations.montantCOMPCIAB.abs();

                FWCurrency plusPetitDesMontantAbs = null;
                // prendre le plus petit des montant en val. abs
                if (ci.montantTotPrestations.montantCOMPCIAB.compareTo(ci.montantTotRestitutions.montantCOMPCIAB) < 0) {
                    plusPetitDesMontantAbs = ci.montantTotPrestations.montantCOMPCIAB;
                } else {
                    plusPetitDesMontantAbs = ci.montantTotRestitutions.montantCOMPCIAB;
                }

                final FWCurrency montantInverse = new FWCurrency(plusPetitDesMontantAbs.toString());
                montantInverse.negate();

                getMemoryLog().logMessage("Compensations internes à la CA >>>>>>>>", FWMessage.INFORMATION,
                        getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));

                if ("RUBRIQUE_DE_LISSAGE".equals(propRubrique)) {
                    doEcriture(compta, plusPetitDesMontantAbs.toString(), COMPENSATION, ci.idCompteAnnexe,
                            ci.idSectionRestitution, null);

                    doEcriture(compta, montantInverse.toString(), COMPENSATION, ci.idCompteAnnexe, ci.idSectionNormale,
                            null);
                }
            }

            //
            // Compensation des prestations de genre Complément (MATCIAB) :
            //
            // /////////////////////////////////////////////////////////////////////////

            propRubrique = getSession().getApplication().getProperty(
                    APGenererEcrituresComptablesProcess.PROP_RUBRIQUE_COMPENSATION);

            if (!ci.montantTotPrestations.montantMATCIAB.isZero() && !ci.montantTotRestitutions.montantMATCIAB.isZero()) {

                ci.montantTotRestitutions.montantMATCIAB.abs();
                ci.montantTotPrestations.montantMATCIAB.abs();

                FWCurrency plusPetitDesMontantAbs = null;
                // prendre le plus petit des montant en val. abs
                if (ci.montantTotPrestations.montantMATCIAB.compareTo(ci.montantTotRestitutions.montantMATCIAB) < 0) {
                    plusPetitDesMontantAbs = ci.montantTotPrestations.montantMATCIAB;
                } else {
                    plusPetitDesMontantAbs = ci.montantTotRestitutions.montantMATCIAB;
                }

                final FWCurrency montantInverse = new FWCurrency(plusPetitDesMontantAbs.toString());
                montantInverse.negate();

                getMemoryLog().logMessage("Compensations internes à la CA >>>>>>>>", FWMessage.INFORMATION,
                        getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));

                if ("RUBRIQUE_DE_LISSAGE".equals(propRubrique)) {
                    doEcriture(compta, plusPetitDesMontantAbs.toString(), COMPENSATION, ci.idCompteAnnexe,
                            ci.idSectionRestitution, null);

                    doEcriture(compta, montantInverse.toString(), COMPENSATION, ci.idCompteAnnexe, ci.idSectionNormale,
                            null);
                }
            }

            //
            // Compensation des prestations de genre Standard (APG) :
            //
            // /////////////////////////////////////////////////////////////////////////

            propRubrique = getSession().getApplication().getProperty(
                    APGenererEcrituresComptablesProcess.PROP_RUBRIQUE_COMPENSATION);

            if (!ci.montantTotPrestations.montantAPG.isZero() && !ci.montantTotRestitutions.montantAPG.isZero()) {

                ci.montantTotRestitutions.montantAPG.abs();
                ci.montantTotPrestations.montantAPG.abs();

                FWCurrency plusPetitDesMontantAbs = null;
                // prendre le plus petit des montant en val. abs
                if (ci.montantTotPrestations.montantAPG.compareTo(ci.montantTotRestitutions.montantAPG) < 0) {
                    plusPetitDesMontantAbs = ci.montantTotPrestations.montantAPG;
                } else {
                    plusPetitDesMontantAbs = ci.montantTotRestitutions.montantAPG;
                }

                final FWCurrency montantInverse = new FWCurrency(plusPetitDesMontantAbs.toString());
                montantInverse.negate();

                getMemoryLog().logMessage("Compensations internes à la CA >>>>>>>>", FWMessage.INFORMATION,
                        getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));

                if ("RUBRIQUE_DE_LISSAGE".equals(propRubrique)) {
                    doEcriture(compta, plusPetitDesMontantAbs.toString(), COMPENSATION, ci.idCompteAnnexe,
                            ci.idSectionRestitution, null);

                    doEcriture(compta, montantInverse.toString(), COMPENSATION, ci.idCompteAnnexe, ci.idSectionNormale,
                            null);
                } else if ("COMPENSATION_ALFA".equals(propRubrique)) {
                    doEcriture(compta, plusPetitDesMontantAbs.toString(), COMPENSATION_ACM, ci.idCompteAnnexe,
                            ci.idSectionRestitution, null);

                    doEcriture(compta, montantInverse.toString(), COMPENSATION_ACM, ci.idCompteAnnexe,
                            ci.idSectionNormale, null);
                }
            }
            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }

            //
            // Compensation des prestations de genre Standard (APG) :
            //
            // /////////////////////////////////////////////////////////////////////////

            propRubrique = getSession().getApplication().getProperty(
                    APGenererEcrituresComptablesProcess.PROP_RUBRIQUE_COMPENSATION);

            if (!ci.montantTotPrestations.montantPANDEMIE.isZero() && !ci.montantTotRestitutions.montantPANDEMIE.isZero()) {

                ci.montantTotRestitutions.montantPANDEMIE.abs();
                ci.montantTotPrestations.montantPANDEMIE.abs();

                FWCurrency plusPetitDesMontantAbs = null;
                // prendre le plus petit des montant en val. abs
                if (ci.montantTotPrestations.montantPANDEMIE.compareTo(ci.montantTotRestitutions.montantPANDEMIE) < 0) {
                    plusPetitDesMontantAbs = ci.montantTotPrestations.montantPANDEMIE;
                } else {
                    plusPetitDesMontantAbs = ci.montantTotRestitutions.montantPANDEMIE;
                }

                final FWCurrency montantInverse = new FWCurrency(plusPetitDesMontantAbs.toString());
                montantInverse.negate();

                getMemoryLog().logMessage("Compensations internes à la CA >>>>>>>>", FWMessage.INFORMATION,
                        getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));

                if ("RUBRIQUE_DE_LISSAGE".equals(propRubrique)) {
                    doEcriture(compta, plusPetitDesMontantAbs.toString(), COMPENSATION, ci.idCompteAnnexe,
                            ci.idSectionRestitution, null);

                    doEcriture(compta, montantInverse.toString(), COMPENSATION, ci.idCompteAnnexe, ci.idSectionNormale,
                            null);
                } else if ("COMPENSATION_ALFA".equals(propRubrique)) {
                    doEcriture(compta, plusPetitDesMontantAbs.toString(), COMPENSATION_ACM, ci.idCompteAnnexe,
                            ci.idSectionRestitution, null);

                    doEcriture(compta, montantInverse.toString(), COMPENSATION_ACM, ci.idCompteAnnexe,
                            ci.idSectionNormale, null);
                }
            }
            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }
        }
    }

    /**
     * écrit une écriture en compta. Ne fait rien si le montant est nul.
     *
     * @param compta          une instance de APIProcessComptabilisation
     * @param montantSigne    Le montant a écrire, signé
     * @param rubrique        l'id de la rubrique
     * @param idCompteAnnexe  l'id du compta annexe
     * @param idSection       l'id de la section
     * @param anneeCotisation l'année de la cotisation, null s'il ne s'agit pas d'une cotisation
     */
    private void doEcriture(final APIGestionComptabiliteExterne compta, final String montantSigne,
                            final APIRubrique rubrique, final String idCompteAnnexe, final String idSection,
                            final String anneeCotisation) {
        if (!JadeStringUtil.isDecimalEmpty(montantSigne)) {
            final FWCurrency montant = new FWCurrency(montantSigne);
            boolean positif = true;

            if (montant.isNegative()) {
                montant.negate();
                positif = false;
            }

            final APIEcriture ecriture = compta.createEcriture();
            ecriture.setIdCompteAnnexe(idCompteAnnexe);
            ecriture.setIdSection(idSection);
            ecriture.setDate(dateComptable);
            ecriture.setIdCompte(rubrique.getIdRubrique());
            ecriture.setMontant(montant.toString());

            getMemoryLog().logMessage(
                    java.text.MessageFormat.format(getSession().getLabel("ECR_COM_ECRITURE"), new Object[]{
                            montantSigne, rubrique.getIdExterne()}), FWMessage.INFORMATION,
                    getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));

            if (positif) {
                ecriture.setCodeDebitCredit(APIEcriture.CREDIT);
            } else {
                ecriture.setCodeDebitCredit(APIEcriture.DEBIT);
            }

            if (anneeCotisation != null) {
                ecriture.setAnneeCotisation(anneeCotisation);
            }

            compta.addOperation(ecriture);
        }
    }

    private void doEcritureFNE(final APIGestionComptabiliteExterne compta, final String montantSigne,
                               final String masse, final String taux, final APIRubrique rubrique, final String idCompteAnnexe,
                               final String idSection, final String anneeCotisation, String idCaisseProf) {

        if (!JadeStringUtil.isDecimalEmpty(montantSigne)) {

            final FWCurrency montant = new FWCurrency(montantSigne);
            final FWCurrency masseCurrency = new FWCurrency(masse);
            boolean positif = true;

            if (montant.isNegative()) {
                montant.negate();
                masseCurrency.negate();
                positif = false;
            }

            final APIEcriture ecriture = compta.createEcriture();
            ecriture.setIdCompteAnnexe(idCompteAnnexe);
            ecriture.setIdSection(idSection);
            ecriture.setDate(dateComptable);
            ecriture.setIdCompte(rubrique.getIdRubrique());
            ecriture.setMasse(masse);
            ecriture.setTaux(taux);
            ecriture.setIdCaisseProfessionnelle(idCaisseProf);
            ecriture.setMontant(montant.toString());

            getMemoryLog().logMessage(
                    java.text.MessageFormat.format(getSession().getLabel("ECR_COM_ECRITURE"), new Object[]{
                            montantSigne, rubrique.getIdExterne()}), FWMessage.INFORMATION,
                    getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));

            if (positif) {
                ecriture.setCodeDebitCredit(APIEcriture.CREDIT);
            } else {
                ecriture.setCodeDebitCredit(APIEcriture.DEBIT);
            }

            if (anneeCotisation != null) {
                ecriture.setAnneeCotisation(anneeCotisation);
            }

            compta.addOperation(ecriture);
        }
    }

    /**
     * Effectue un ordre de versement, lance une Exception si le montant est négatif
     *
     * @param compta            DOCUMENT ME!
     * @param idCompteAnnexe    DOCUMENT ME!
     * @param idSection         DOCUMENT ME!
     * @param montantParNature  DOCUMENT ME!
     * @param idAdressePaiement DOCUMENT ME!
     * @param nature            String, valeurs possibles : - NATURE_VERSEMENT_APG - NATURE_ASSURANCE_MATERNITE -
     *                          NATURE_ASSURANCE_MATERNITE_CANTONALE (non utilisé) - NATURE_VERSEMENT_ACM - NATURE_VERSEMENT_LAMAT
     * @param nomRequerant      String, NSS du requérant principal. Cette valeur peut être null.
     * @throws Exception                Si le montant est négatif
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    private void doOrdreVersement(final APIGestionComptabiliteExterne compta, final String idCompteAnnexe,
                                  final String idSection, final String montantParNature, final String idAdressePaiement, final String nature,
                                  final String nomRequerant, final String referenceInterne) throws Exception {
        if (new FWCurrency(montantParNature).isNegative()) {
            throw new IllegalArgumentException(getSession().getLabel(
                    "ERROR_GENERATION_ECRITURES_COMPTABLES_MONTANT_NEG_OV"));
        }

        final APIOperationOrdreVersement ordreVersement = compta.createOperationOrdreVersement();
        ordreVersement.setIdAdressePaiement(idAdressePaiement);
        ordreVersement.setDate(dateComptable);
        ordreVersement.setIdCompteAnnexe(idCompteAnnexe);
        ordreVersement.setIdSection(idSection);
        ordreVersement.setMontant(montantParNature);
        ordreVersement.setCodeISOMonnaieBonification(getSession().getCode(
                IPRConstantesExternes.OSIRIS_CS_CODE_ISO_MONNAIE_CHF));
        ordreVersement.setCodeISOMonnaieDepot(getSession()
                .getCode(IPRConstantesExternes.OSIRIS_CS_CODE_ISO_MONNAIE_CHF));
        ordreVersement.setTypeVirement(APIOperationOrdreVersement.VIREMENT);

        getMemoryLog().logMessage(
                java.text.MessageFormat.format(getSession().getLabel("ECR_COM_ORDRE_VERSEMENT"), new Object[]{
                        montantParNature, idAdressePaiement}), FWMessage.INFORMATION,
                getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));

        String motif = "";

        if ("NATURE_ASSURANCE_MATERNITE".equals(nature)) {
            ordreVersement.setNatureOrdre(CAOrdreGroupe.NATURE_ASSURANCE_MATERNITE);
            motif = FWMessageFormat.format(getSession().getLabel("MOTIF_MAT"), getDateSurDocument());
        } else if ("NATURE_ASSURANCE_MATERNITE_CANTONALE".equals(nature)) {
            ordreVersement.setNatureOrdre(CAOrdreGroupe.NATURE_ASSURANCE_MATERNITE);
            motif = FWMessageFormat.format(getSession().getLabel("MOTIF_MAT"), getDateSurDocument());
        } else if ("NATURE_VERSEMENT_APG".equals(nature)) {
            ordreVersement.setNatureOrdre(CAOrdreGroupe.NATURE_VERSEMENT_APG);
            motif = FWMessageFormat.format(getSession().getLabel("MOTIF_APG"), getDateSurDocument());
        } else if ("NATURE_ALFA_ACM".equals(nature)) {
            ordreVersement.setNatureOrdre(CAOrdreGroupe.NATURE_ALFA_ACM);
            motif = FWMessageFormat.format(getSession().getLabel("MOTIF_APG"), getDateSurDocument());
        } else if ("NATURE_VERSEMENT_PANDEMIE".equals(nature)) {
            ordreVersement.setNatureOrdre(CAOrdreGroupe.NATURE_VERSEMENT_PANDEMIE);
            motif = FWMessageFormat.format(getSession().getLabel("MOTIF_PANDEMIE"), getDateSurDocument());
        } else {
            throw new Exception("Nature du versement non reconnue, contrôler les properties !!! (" + nature + ")");
        }

        if (nomRequerant != null) {
            motif += " (" + nomRequerant + ")";
        }

        if ((referenceInterne != null) && !JadeStringUtil.isEmpty(referenceInterne)) {
            motif += " " + getSession().getLabel("REFERENCE_INTERNE_VENTILLATION") + " : " + referenceInterne;
        }

        ordreVersement.setMotif(motif);
        compta.addOperation(ordreVersement);
    }

    /**
     * Ecris en compta les cotisations d'un certain type (AVS, AC, etc.)
     *
     * @param compta         une instance de APIProcessComptabilisation
     * @param rubrique       l'id de la rubrique sur laquelle écrire
     * @param idCompteAnnexe l'id du compte annexe
     * @param idSection      l'id de la section
     * @param cotisations    une map contenant les montants des cotisations regroupés par année de cotisation
     * @return le montant total des écritures passées.
     */
    private Montants ecrisCotisations(final APIGestionComptabiliteExterne compta, final APIRubrique rubrique,
                                      final String idCompteAnnexe, final String idSection, final Map cotisations) {
        final Montants result = new Montants();

        final Set keys = cotisations.keySet();
        final Iterator iterator = keys.iterator();

        while (iterator.hasNext()) {
            final String annee = (String) iterator.next();

            final Montants montants = (Montants) cotisations.get(annee);
            result.add(montants);
            final FWCurrency montant = montants.getMontantCumule(Montants.TYPE_ACM + "_" + Montants.TYPE_APG + "_"
                    + Montants.TYPE_ACM_NE + "_" + Montants.TYPE_COMPCIAB + "_" + Montants.TYPE_PANDEMIE + "_" + Montants.TYPE_MATCIAB);
            doEcriture(compta, montant.toString(), rubrique, idCompteAnnexe, idSection, annee);
        }
        return result;
    }

    private void ecrisCotisationsFNE(final APIGestionComptabiliteExterne compta, final APIRubrique rubrique,
                                     final String idCompteAnnexe, final String idSection, final Map<String, CotisationFNE> cotisationsFNE,
                                     String idAffilie) throws Exception {

        final Set keys = cotisationsFNE.keySet();
        final Iterator iterator = keys.iterator();
        String assuranceId = APProperties.ASSURANCE_FNE_ID.getValue();

        String idCaisseProf = rechercheIdCaisseProf(idAffilie, assuranceId);

        while (iterator.hasNext()) {

            final String annee = (String) iterator.next();
            final CotisationFNE cotisationFNEPourAnnee = cotisationsFNE.get(annee);

            String montantCotisationFNE = cotisationFNEPourAnnee.montantCotisation.toString();
            String montantBrutCotisationFNE = cotisationFNEPourAnnee.montantBrutCotisation.toString();
            String tauxCotisationFNE = cotisationFNEPourAnnee.tauxCotisation.toString();

            doEcritureFNE(compta, montantCotisationFNE, montantBrutCotisationFNE, tauxCotisationFNE, rubrique,
                    idCompteAnnexe, idSection, annee, idCaisseProf);

        }

    }

    private List<Map<String, String>> executeQuery(String sql) throws JadePersistenceException {

        Statement stmt = null;
        ResultSet resultSet = null;
        List<Map<String, String>> results = new ArrayList<Map<String, String>>();

        try {
            stmt = JadeThread.currentJdbcConnection().createStatement();
            resultSet = stmt.executeQuery(sql);

            ResultSetMetaData md = resultSet.getMetaData();
            int columns = md.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> row = new HashMap<String, String>();

                // Attention ! La première colonne du Resultset est 1 et non 0
                for (int i = 1; i <= columns; i++) {
                    row.put(md.getColumnName(i), resultSet.getString(i));
                }

                results.add(row);
            }

        } catch (SQLException e) {
            throw new JadePersistenceException(getName() + " - " + "Unable to execute query (" + sql
                    + "), a SQLException happend!", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    JadeLogger.warn(AFStatistiquesOfasProcess.class,
                            "Problem to close statement in AFStatistiquesOfasProcess, reason : " + e.toString());
                }
            }

        }

        return results;
    }

    /**
     * Cette méthode va parcourir les répartitions pour regrouper les montants à inscrire par rubrique, en faisant la
     * différence entre des répartitions normales et des répartitions de restitution.
     * <p>
     * Elle fait ensuite les écritures dans la compta
     * </p>
     *
     * @param repartitions           Une List de répartitions concernant le même idTiers, idAffilie et idAdressePaiement
     * @param compensationsPourCetId Les compensations ratachées aux répartitions
     * @param montantsBrutTotal      Le montant brut total des répartitions
     * @param ventilationTotale      Le montant total des ventilations pour les répartitions
     * @param compensationsTotale    le montant total des compensations pour les répartitions
     * @param key                    une clef contenant les infos sur le regroupement des répartitions
     * @param compta                 une instance de APIProcessComptabilisation (OSIRIS)
     * @param sessionOsiris          Une session d'OSIRIS
     * @throws Exception
     */
    private CompensationInterne faisEcritures(final List repartitions, final List compensationsPourCetId,
                                              final Montants montantsBrutTotal, final Montants ventilationTotale, final Montants compensationsTotale,
                                              final Key key, final APIGestionComptabiliteExterne compta, final BISession sessionOsiris) throws Exception {

        // creation de l'idExterneRole (qui est tout simplement le numéro
        // d'affilié s'il s'agit d'un affilié, le numéro
        // AVS sinon)
        String idExterneRole = null;

        if (JadeStringUtil.isIntegerEmpty(key.idAffilie)) {
            idExterneRole = PRTiersHelper.getTiersParId(getSession(), key.idTiers).getProperty(
                    PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        } else {
            idExterneRole = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getTransaction(), key.idAffilie,
                    key.idTiers).getNumAffilie();
        }

        getMemoryLog().logMessage("------------------------", FWMessage.INFORMATION,
                getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));
        getMemoryLog().logMessage(getSession().getLabel("ECR_COM_TRAITEMENT_DE") + " " + idExterneRole,
                FWMessage.INFORMATION, getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));

        // récupération du compte annexe APG
        final APICompteAnnexe compteAnnexeAPG = compta.getCompteAnnexeByRole(key.idTiers, IntRole.ROLE_APG,
                idExterneRole);
        if (compteAnnexeAPG == null) {
            throw new Exception("Impossible de créer le compte annexe, contrôlez les logs pour plus de détails.");
        }

        // on créé un numero de facture unique qui servira a creer la section
        final String noFactureNormale;
        if (StringUtils.equals(IPRDemande.CS_TYPE_PANDEMIE, typeLot)) {
            noFactureNormale = CAUtil.creerNumeroSectionUnique(sessionOsiris, getTransaction(),
                    IntRole.ROLE_APG, idExterneRole, APISection.ID_TYPE_SECTION_APG,
                    String.valueOf(new JADate(dateComptable).getYear()), APISection.ID_CATEGORIE_SECTION_PANDEMIE);
        } else {
            noFactureNormale = CAUtil.creerNumeroSectionUnique(sessionOsiris, getTransaction(),
                IntRole.ROLE_APG, idExterneRole, APISection.ID_TYPE_SECTION_APG,
                String.valueOf(new JADate(dateComptable).getYear()), APISection.ID_CATEGORIE_SECTION_APG);
        }

        // création de la section "normale" elle servira pour toutes les
        // écritures hormis les restitutions et les
        // compensations sur facture future
        final APISection sectionNormale = compta.getSectionByIdExterne(compteAnnexeAPG.getIdCompteAnnexe(),
                APISection.ID_TYPE_SECTION_APG, noFactureNormale);


        // Plein plein plein de variables pour garder ce qu'on a besoin
        // les cotisations sont sommées dans une map avec comme clef l'année de
        // cotisation (nécessaire pour les
        // écritures en compta)
        final Map mapTotalAVSAssure = new HashMap();
        final Map mapTotalAVSIndependant = new HashMap();
        final Map mapTotalACAssure = new HashMap();
        final Map mapTotalACIndependant = new HashMap();
        final Map mapTotalLFA = new HashMap();
        final Map<String, CotisationFNE> mapTotalCotFNE = new HashMap<String, APGenererEcrituresComptablesProcess.CotisationFNE>();

        final Montants totalFA = new Montants();
        Montants totalISAssure = new Montants();
        Montants totalISIndependant = new Montants();
        final Montants totalFondDeCompensationAssure = new Montants();
        final Montants totalFondDeCompensationEmployeur = new Montants();
        final Montants totalFondDeCompensationIndependant = new Montants();
        final Montants totalFondDeCompensationACM = new Montants();
        final Montants totalFondDeCompensationACMNE = new Montants();

        final Map mapTotalAVSRestitutionAssure = new HashMap();
        final Map mapTotalAVSRestitutionIndependant = new HashMap();
        final Map mapTotalACRestitutionAssure = new HashMap();
        final Map mapTotalACRestitutionIndependant = new HashMap();
        final Map mapTotalLFARestitution = new HashMap();

        FWCurrency montantTotalCotFNE = new FWCurrency();

        final Montants totalFARestitution = new Montants();
        Montants totalISRestitutionAssure = new Montants();
        Montants totalISRestitutionIndependant = new Montants();
        final Montants totalFondDeCompensationRestitutionAssure = new Montants();
        final Montants totalFondDeCompensationRestitutionEmployeur = new Montants();
        final Montants totalFondDeCompensationRestitutionIndependant = new Montants();
        final Montants totalFondDeCompensationRestitutionACM = new Montants();
        final Montants totalFondDeCompensationRestitutionACMNE = new Montants();

        // variables pour les compléments CIAB
        final Montants totalFondDeCompensationComplementEmployeur = new Montants();
        final Montants totalFondDeCompensationComplementIndependant = new Montants();
        final Montants totalFondDeCompensationComplementRestitutionEmployeur = new Montants();
        final Montants totalFondDeCompensationComplementRestitutionIndependant = new Montants();
        final Map mapTotalACComplementIndependant = new HashMap();
        final Map mapTotalACComplementRestitutionIndependant = new HashMap();
        final Map mapTotalAVSComplementIndependant = new HashMap();
        final Map mapTotalAVSComplementRestitutionIndependant = new HashMap();

        final Map mapEcrituresRubriquesConcernees = new HashMap();

        final Map mapEcrituresRubriquesConcerneesRestitution = new HashMap();

        Montants totalCotisations = new Montants();

        String idAdressePaiementBeneficiaireDeBase = "";

        final Iterator repartitionsIterator = repartitions.iterator();

        Repartition repartition = null;

        // pour savoir s'il sera nécessaire de créer la section de restitution
        boolean hasRestitution = false;

        String typeAssociationPourAcmNe = "";

        for (final Repartition aRepartition : (List<Repartition>) repartitions) {
            if (APTypeDePrestation.ACM_NE.isCodeSystemEqual(aRepartition.genrePrestation)) {
                final String newTypeAssociationPourAcmNe = aRepartition.typeAssociation;

                if (JadeStringUtil.isBlankOrZero(newTypeAssociationPourAcmNe)) {
                    throw new Exception("typeAssociation can't be blank or zero for acm ne");
                }

                if (JadeStringUtil.isBlankOrZero(typeAssociationPourAcmNe)) {
                    typeAssociationPourAcmNe = newTypeAssociationPourAcmNe;
                }

                if (!newTypeAssociationPourAcmNe.equalsIgnoreCase(typeAssociationPourAcmNe)) {
                    throw new Exception("typeAssociation can't be different for a list repartition with acm ne");
                }
            }

        }

        TypeComplement typeComplement = null;

        // parcours de toutes les répartitions
        while (repartitionsIterator.hasNext()) {

            for (Object repar : repartitions) {
                Repartition repartition1 = (Repartition) repar;
                if (APTypeDePrestation.COMPCIAB.isCodeSystemEqual(repartition1.genrePrestation) ||
                        APTypeDePrestation.JOUR_ISOLE.isCodeSystemEqual(repartition1.genrePrestation)) {
                    typeComplement = getTypeComplementFromAssurance(getSession(), key.idAffilie, repartition1.idDroit, repartition1.isIndependant);
                    break;
                }
                if (APTypeDePrestation.MATCIAB1.isCodeSystemEqual(repartition1.genrePrestation) ||
                        APTypeDePrestation.MATCIAB2.isCodeSystemEqual(repartition1.genrePrestation)) {
                    typeComplement = getTypeComplementFromAssuranceDateDebut(getSession(), key.idAffilie, repartition1.idDroit, repartition1.isIndependant);
                    break;
                }
            }

            repartition = (Repartition) repartitionsIterator.next();

            final boolean isGenrePrestationACMAlpha = APTypeDePrestation.ACM_ALFA
                    .isCodeSystemEqual(repartition.genrePrestation)
                    || APTypeDePrestation.ACM2_ALFA.isCodeSystemEqual(repartition.genrePrestation);
            final boolean isGenrePrestationACMNe = APTypeDePrestation.ACM_NE
                    .isCodeSystemEqual(repartition.genrePrestation);
            final boolean isGenrePrestationCompCIAB = APTypeDePrestation.COMPCIAB
                    .isCodeSystemEqual(repartition.genrePrestation);
            final boolean isGenrePrestationJoursIsoles = APTypeDePrestation.JOUR_ISOLE
                    .isCodeSystemEqual(repartition.genrePrestation);
            final boolean isGenrePrestationMatCIAB = APTypeDePrestation.MATCIAB1
                    .isCodeSystemEqual(repartition.genrePrestation)
                    || APTypeDePrestation.MATCIAB2.isCodeSystemEqual(repartition.genrePrestation);

            final GenreDestinataire destinataire = GenreDestinataire.getDestinataire(repartition.isEmployeur,
                    repartition.isIndependant);

            // recup de l'idAdressePaiement
            idAdressePaiementBeneficiaireDeBase = repartition.idAdressePaiement;

            final boolean isRestitution = repartition.isRestitution;

            KeyRegroupementRubriqueConcernee keyRegroupementRubriqueConcernee = new KeyRegroupementRubriqueConcernee(
                    repartition.rubriqueConcernee, repartition.section);

            // Les montants stocké dans la Map sont de type Montants, seul les
            // type ACM et APG seront utilisé
            // ACM -> pour la rubrique ALFA_ACM
            // APG -> pour la rubrique LISSAGE

            // regroupement des montants par rubrique, en diférenciant les
            // restitutions des normales
            if (!isRestitution) {
                if (mapEcrituresRubriquesConcernees.containsKey(keyRegroupementRubriqueConcernee)) {
                    ((Montants) mapEcrituresRubriquesConcernees.get(keyRegroupementRubriqueConcernee)).add(
                            convertGenrePrestToRubrique(repartition.genrePrestation), repartition.montant);
                } else {
                    final Montants mnt = new Montants();
                    mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.montant);
                    mapEcrituresRubriquesConcernees.put(keyRegroupementRubriqueConcernee, mnt);
                }
            } else {
                hasRestitution = true;

                if (mapEcrituresRubriquesConcerneesRestitution.containsKey(keyRegroupementRubriqueConcernee)) {
                    ((Montants) mapEcrituresRubriquesConcerneesRestitution.get(keyRegroupementRubriqueConcernee)).add(
                            convertGenrePrestToRubrique(repartition.genrePrestation), repartition.montant);
                } else {
                    final Montants mnt = new Montants();
                    mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.montant);
                    mapEcrituresRubriquesConcerneesRestitution.put(keyRegroupementRubriqueConcernee, mnt);
                }
            }

            keyRegroupementRubriqueConcernee = null;

            if (!JadeStringUtil.isDecimalEmpty(repartition.cotisationFNE)) {

                montantTotalCotFNE.add(repartition.cotisationFNE);

                totalCotisations = getCumulMontantsParGenre(totalCotisations, repartition.genrePrestation,
                        repartition.cotisationFNE);

                CotisationFNE cotisationFNEPourAnnee = mapTotalCotFNE.get(repartition.anneeCotisation);

                if (cotisationFNEPourAnnee == null) {
                    cotisationFNEPourAnnee = new CotisationFNE();
                }

                cotisationFNEPourAnnee.montantCotisation.add(repartition.cotisationFNE);
                cotisationFNEPourAnnee.montantBrutCotisation.add(repartition.montantBrutCotisationFNE);
                cotisationFNEPourAnnee.tauxCotisation = new BigDecimal(repartition.tauxCotisationFNE);

                mapTotalCotFNE.put(repartition.anneeCotisation, cotisationFNEPourAnnee);

            }

            // les cotisations AVS et AC ne doivent pas être écrites si c'est un
            // employeur non indépendant, on n'écrit
            // que dans
            // le fond de compensation dans ce cas

            // regroupement des cotisations pour les faire en une seule écriture
            // (normales, et restitutions, regroupées
            // suivant l'annee de cotisation)

            // AC
            if (!JadeStringUtil.isDecimalEmpty(repartition.cotisationAC)) {

                totalCotisations = getCumulMontantsParGenre(totalCotisations, repartition.genrePrestation,
                        repartition.cotisationAC);

                if (isGenrePrestationACMAlpha || isGenrePrestationACMNe) {

                    if (repartition.isEmployeur) {

                        final Montants mnt = new Montants();
                        mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationAC);

                        if (isGenrePrestationACMAlpha && isRestitution) {
                            totalFondDeCompensationRestitutionACM.add(mnt);
                        } else if (isGenrePrestationACMAlpha && !isRestitution) {
                            totalFondDeCompensationACM.add(mnt);
                        } else if (isGenrePrestationACMNe && isRestitution) {
                            totalFondDeCompensationRestitutionACMNE.add(mnt);
                        } else if (isGenrePrestationACMNe && !isRestitution) {
                            totalFondDeCompensationACMNE.add(mnt);
                        } else {
                            throw new Exception("Not implemented");
                        }
                    }
                } else if (isGenrePrestationCompCIAB || isGenrePrestationJoursIsoles || isGenrePrestationMatCIAB) {
                    final Montants mnt = new Montants();
                    mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationAC);
                    if (GenreDestinataire.INDEPENDANT.equals(destinataire) && !isRestitution) {
                        putMontantIntoMap(mapTotalACComplementIndependant, repartition, repartition.cotisationAC);
                        totalFondDeCompensationComplementIndependant.sub(mnt);
                    } else if (GenreDestinataire.INDEPENDANT.equals(destinataire) && isRestitution) {
                        putMontantIntoMap(mapTotalACComplementRestitutionIndependant, repartition, repartition.cotisationAC);
                        totalFondDeCompensationComplementRestitutionIndependant.sub(mnt);
                    } else if (!isRestitution) {
                        totalFondDeCompensationComplementEmployeur.add(mnt);
                    } else {
                        totalFondDeCompensationComplementRestitutionEmployeur.add(mnt);
                    }
                } else {
                    if (!isRestitution) {
                        if (GenreDestinataire.INDEPENDANT.equals(destinataire)) {
                            putMontantIntoMap(mapTotalACIndependant, repartition, repartition.cotisationAC);

                            final Montants mnt = new Montants();
                            mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationAC);
                            totalFondDeCompensationIndependant.sub(mnt);
                        } else if (GenreDestinataire.EMPLOYEUR.equals(destinataire)) {
                            final Montants mnt = new Montants();
                            mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationAC);
                            totalFondDeCompensationEmployeur.add(mnt);
                        } else {
                            putMontantIntoMap(mapTotalACAssure, repartition, repartition.cotisationAC);

                            final Montants mnt = new Montants();
                            mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationAC);
                            totalFondDeCompensationAssure.sub(mnt);
                        }
                    } else {
                        if (GenreDestinataire.INDEPENDANT.equals(destinataire)) {
                            putMontantIntoMap(mapTotalACRestitutionIndependant, repartition, repartition.cotisationAC);

                            final Montants mnt = new Montants();
                            mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationAC);
                            totalFondDeCompensationRestitutionIndependant.sub(mnt);
                        } else if (GenreDestinataire.EMPLOYEUR.equals(destinataire)) {
                            final Montants mnt = new Montants();
                            mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationAC);
                            totalFondDeCompensationRestitutionEmployeur.add(mnt);
                        } else {
                            putMontantIntoMap(mapTotalACRestitutionAssure, repartition, repartition.cotisationAC);

                            final Montants mnt = new Montants();
                            mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationAC);
                            totalFondDeCompensationRestitutionAssure.sub(mnt);
                        }
                    }
                }
            }

            // AVS
            if (!JadeStringUtil.isDecimalEmpty(repartition.cotisationAVS)) {

                totalCotisations = getCumulMontantsParGenre(totalCotisations, repartition.genrePrestation,
                        repartition.cotisationAVS);

                if (isGenrePrestationACMAlpha || isGenrePrestationACMNe) {

                    if (repartition.isEmployeur) {

                        final Montants mnt = new Montants();
                        mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationAVS);

                        if (isGenrePrestationACMAlpha && isRestitution) {
                            totalFondDeCompensationRestitutionACM.add(mnt);
                        } else if (isGenrePrestationACMAlpha && !isRestitution) {
                            totalFondDeCompensationACM.add(mnt);
                        } else if (isGenrePrestationACMNe && isRestitution) {
                            totalFondDeCompensationRestitutionACMNE.add(mnt);
                        } else if (isGenrePrestationACMNe && !isRestitution) {
                            totalFondDeCompensationACMNE.add(mnt);
                        } else {
                            throw new Exception("Not implemented");
                        }
                    }
                } else if (isGenrePrestationCompCIAB || isGenrePrestationJoursIsoles || isGenrePrestationMatCIAB) {
                    final Montants mnt = new Montants();
                    mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationAVS);
                    if (GenreDestinataire.INDEPENDANT.equals(destinataire) && !isRestitution) {
                        putMontantIntoMap(mapTotalAVSComplementIndependant, repartition, repartition.cotisationAVS);
                        totalFondDeCompensationComplementIndependant.sub(mnt);
                    } else if (GenreDestinataire.INDEPENDANT.equals(destinataire) && isRestitution) {
                        putMontantIntoMap(mapTotalAVSComplementRestitutionIndependant, repartition, repartition.cotisationAVS);
                        totalFondDeCompensationComplementRestitutionIndependant.sub(mnt);
                    } else if (!isRestitution) {
                        totalFondDeCompensationComplementEmployeur.add(mnt);
                    } else {
                        totalFondDeCompensationComplementRestitutionEmployeur.add(mnt);
                    }
                } else {
                    if (!isRestitution) {
                        if (GenreDestinataire.INDEPENDANT.equals(destinataire)) {
                            putMontantIntoMap(mapTotalAVSIndependant, repartition, repartition.cotisationAVS);

                            final Montants mnt = new Montants();
                            mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationAVS);
                            totalFondDeCompensationIndependant.sub(mnt);
                        } else if (GenreDestinataire.EMPLOYEUR.equals(destinataire)) {
                            final Montants mnt = new Montants();
                            mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationAVS);
                            totalFondDeCompensationEmployeur.add(mnt);
                        } else {
                            putMontantIntoMap(mapTotalAVSAssure, repartition, repartition.cotisationAVS);

                            final Montants mnt = new Montants();
                            mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationAVS);
                            totalFondDeCompensationAssure.sub(mnt);
                        }
                    } else {
                        if (GenreDestinataire.INDEPENDANT.equals(destinataire)) {
                            putMontantIntoMap(mapTotalAVSRestitutionIndependant, repartition, repartition.cotisationAVS);

                            final Montants mnt = new Montants();
                            mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationAVS);
                            totalFondDeCompensationRestitutionIndependant.sub(mnt);
                        } else if (GenreDestinataire.EMPLOYEUR.equals(destinataire)) {
                            final Montants mnt = new Montants();
                            mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationAVS);
                            totalFondDeCompensationRestitutionEmployeur.add(mnt);
                        } else {
                            putMontantIntoMap(mapTotalAVSRestitutionAssure, repartition, repartition.cotisationAVS);

                            final Montants mnt = new Montants();
                            mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationAVS);
                            totalFondDeCompensationRestitutionAssure.sub(mnt);
                        }
                    }
                }
            }

            // LFA
            if (!JadeStringUtil.isDecimalEmpty(repartition.cotisationLFA)) {

                totalCotisations = getCumulMontantsParGenre(totalCotisations, repartition.genrePrestation,
                        repartition.cotisationLFA);

                if (!isRestitution) {
                    if (mapTotalLFA.containsKey(repartition.anneeCotisation)) {

                        final Montants mnt = new Montants();
                        mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationLFA);
                        ((Montants) mapTotalLFA.get(repartition.anneeCotisation)).add(mnt);
                    } else {
                        final Montants mnt = new Montants();
                        mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationLFA);
                        mapTotalLFA.put(repartition.anneeCotisation, mnt);
                    }
                } else {
                    if (mapTotalLFARestitution.containsKey(repartition.anneeCotisation)) {
                        final Montants mnt = new Montants();
                        mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationLFA);
                        ((Montants) mapTotalLFARestitution.get(repartition.anneeCotisation)).add(mnt);
                    } else {
                        final Montants mnt = new Montants();
                        mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.cotisationLFA);
                        mapTotalLFARestitution.put(repartition.anneeCotisation, mnt);

                    }
                }
            }

            // FA
            if (!JadeStringUtil.isDecimalEmpty(repartition.fraisAdministration)) {
                totalCotisations = getCumulMontantsParGenre(totalCotisations, repartition.genrePrestation,
                        repartition.fraisAdministration);

                if (!isRestitution) {
                    final Montants mnt = new Montants();
                    mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.fraisAdministration);
                    totalFA.add(mnt);
                } else {
                    final Montants mnt = new Montants();
                    mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), repartition.fraisAdministration);
                    totalFARestitution.add(mnt);
                }
            }

            // Impot à la source
            if (!JadeStringUtil.isDecimalEmpty(repartition.impotSource)) {
                totalCotisations = getCumulMontantsParGenre(totalCotisations, repartition.genrePrestation,
                        repartition.impotSource);

                if (!isRestitution) {
                    if (GenreDestinataire.INDEPENDANT.equals(destinataire)) {
                        totalISIndependant = getCumulMontantsParGenre(totalISIndependant, repartition.genrePrestation,
                                repartition.impotSource);
                    } else if (GenreDestinataire.ASSURE.equals(destinataire)) {
                        totalISAssure = getCumulMontantsParGenre(totalISAssure, repartition.genrePrestation,
                                repartition.impotSource);
                    }
                } else {
                    if (GenreDestinataire.INDEPENDANT.equals(destinataire)) {
                        totalISRestitutionIndependant = getCumulMontantsParGenre(totalISRestitutionIndependant,
                                repartition.genrePrestation, repartition.impotSource);
                    } else if (GenreDestinataire.ASSURE.equals(destinataire)) {
                        totalISRestitutionAssure = getCumulMontantsParGenre(totalISRestitutionAssure,
                                repartition.genrePrestation, repartition.impotSource);
                    }

                }
            }

            // Versement des ventilations de cette répartition
            final Iterator ventilationsIterator = repartition.ventilations.iterator();

            String nomPrenom = null;
            if (ventilationsIterator.hasNext()) {

                // Pour chaque ventilation, il faut ajouter dans le motif de
                // l'ordre de versement
                // le nss du requérant principal.
                nomPrenom = getNomPrenomRequerantPrincipal(repartition.idRepartitionPaiement);
            }

            while (ventilationsIterator.hasNext()) {
                final Ventilation ventilation = (Ventilation) ventilationsIterator.next();

                if (typeLot.equals(IPRDemande.CS_TYPE_MATERNITE)) {

                    //ESVE MATERNITE COMPTABILITE
                    if (APTypeDePrestation.ACM_ALFA.isCodeSystemEqual(ventilation.genrePrestation)
                            || APTypeDePrestation.ACM2_ALFA.isCodeSystemEqual(ventilation.genrePrestation)
                    ) {

                        doOrdreVersement(
                                compta,
                                compteAnnexeAPG.getIdCompteAnnexe(),
                                sectionNormale.getIdSection(),
                                ventilation.montant,
                                ventilation.idAdressePaiement,
                                getSession().getApplication().getProperty(
                                        APGenererEcrituresComptablesProcess.PROP_NATURE_VERSEMENT_ACM_MAT), nomPrenom,
                                ventilation.referenceInterne);
                    } else if (APTypeDePrestation.LAMAT.isCodeSystemEqual(ventilation.genrePrestation)) {

                        doOrdreVersement(compta, compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                                ventilation.montant, ventilation.idAdressePaiement, getSession().getApplication()
                                        .getProperty(APGenererEcrituresComptablesProcess.PROP_NATURE_VERSEMENT_LAMAT),
                                nomPrenom, ventilation.referenceInterne);
                    } else if (APTypeDePrestation.STANDARD.isCodeSystemEqual(ventilation.genrePrestation)
                            || APTypeDePrestation.MATCIAB1.isCodeSystemEqual(ventilation.genrePrestation)
                            || APTypeDePrestation.MATCIAB2.isCodeSystemEqual(ventilation.genrePrestation)) {
                        doOrdreVersement(compta, compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                                ventilation.montant, ventilation.idAdressePaiement, getSession().getApplication()
                                        .getProperty(APGenererEcrituresComptablesProcess.PROP_NATURE_VERSEMENT_AMAT),
                                nomPrenom, ventilation.referenceInterne);
                    }
                } else {
                    if (APTypeDePrestation.ACM_ALFA.isCodeSystemEqual(ventilation.genrePrestation)
                            || APTypeDePrestation.ACM_NE.isCodeSystemEqual(ventilation.genrePrestation)) {
                        doOrdreVersement(
                                compta,
                                compteAnnexeAPG.getIdCompteAnnexe(),
                                sectionNormale.getIdSection(),
                                ventilation.montant,
                                ventilation.idAdressePaiement,
                                getSession().getApplication().getProperty(
                                        APGenererEcrituresComptablesProcess.PROP_NATURE_VERSEMENT_ACM_APG), nomPrenom,
                                ventilation.referenceInterne);

                    } else if (APTypeDePrestation.STANDARD.isCodeSystemEqual(ventilation.genrePrestation)) {
                        doOrdreVersement(compta, compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                                ventilation.montant, ventilation.idAdressePaiement, getSession().getApplication()
                                        .getProperty(APGenererEcrituresComptablesProcess.PROP_NATURE_VERSEMENT_APG),
                                nomPrenom, ventilation.referenceInterne);
                    } else if (APTypeDePrestation.PANDEMIE.isCodeSystemEqual(ventilation.genrePrestation)) {
                        doOrdreVersement(compta, compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                                ventilation.montant, ventilation.idAdressePaiement, getSession().getApplication()
                                        .getProperty(APGenererEcrituresComptablesProcess.PROP_NATURE_VERSEMENT_PANDEMIE),
                                nomPrenom, ventilation.referenceInterne);
                    }
                }
            }
        }

        // Ces 2 variables sont utilisées pour connaitre les montants totaux
        // Standard et Restitution
        // pour la compensation interne.
        // Ces variables sont de type Montants, seul les type ACM et APG seront
        // utilisé
        // ACM -> pour la rubrique ALFA_ACM
        // APG -> pour la rubrique LISSAGE

        final Montants montantTotalStandard = new Montants();
        final Montants montantTotalRestitution = new Montants();

        // Ecritures sur les rubriques concernées (pour normal et restitution)
        final Set keys = mapEcrituresRubriquesConcernees.keySet();
        final Iterator ecrituresRubriquesConcerneesIterator = keys.iterator();

        while (ecrituresRubriquesConcerneesIterator.hasNext()) {
            final KeyRegroupementRubriqueConcernee keyRegroupementRubriqueConcernee = (KeyRegroupementRubriqueConcernee) ecrituresRubriquesConcerneesIterator
                    .next();
            final Montants montants = (Montants) (mapEcrituresRubriquesConcernees.get(keyRegroupementRubriqueConcernee));
            montantTotalStandard.add(montants);
            final FWCurrency montant = montants.getMontantCumule(Montants.TYPE_ACM + "_" + Montants.TYPE_APG + "_"
                    + Montants.TYPE_ACM_NE + "_" + Montants.TYPE_COMPCIAB + "_" + Montants.TYPE_PANDEMIE + "_" + Montants.TYPE_MATCIAB);
            doEcriture(compta, montant.toString(), keyRegroupementRubriqueConcernee.rubrique,
                    compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(), null);
        }

        // Restitutions
        // creation de la section de restitution seulement si cela est
        // nécessaire
        APISection sectionRestitution = null;

        if (hasRestitution) {
            final String noFactureRestitution = CAUtil.creerNumeroSectionUnique(sessionOsiris, getTransaction(),
                    IntRole.ROLE_APG, idExterneRole, APISection.ID_TYPE_SECTION_RESTITUTION,
                    String.valueOf(new JADate(dateComptable).getYear()), APISection.ID_CATEGORIE_SECTION_RESTITUTIONS);
            sectionRestitution = compta.getSectionByIdExterne(compteAnnexeAPG.getIdCompteAnnexe(),
                    APISection.ID_TYPE_SECTION_RESTITUTION, noFactureRestitution);
        }

        // Ecriture des restitutions
        final Set keysRestitution = mapEcrituresRubriquesConcerneesRestitution.keySet();
        final Iterator ecrituresRubriquesConcerneesRestitutionIterator = keysRestitution.iterator();

        while (ecrituresRubriquesConcerneesRestitutionIterator.hasNext()) {
            final KeyRegroupementRubriqueConcernee keyRegroupementRubriqueConcernee = (KeyRegroupementRubriqueConcernee) ecrituresRubriquesConcerneesRestitutionIterator
                    .next();
            final Montants montants = (Montants) (mapEcrituresRubriquesConcerneesRestitution
                    .get(keyRegroupementRubriqueConcernee));

            montantTotalRestitution.add(montants);
            final FWCurrency montant = montants.getMontantCumule(Montants.TYPE_ACM + "_" + Montants.TYPE_APG + "_"
                    + Montants.TYPE_ACM_NE + "_" + Montants.TYPE_COMPCIAB + "_" + Montants.TYPE_PANDEMIE + "_" + Montants.TYPE_MATCIAB);

            doEcriture(compta, montant.toString(), keyRegroupementRubriqueConcernee.rubrique,
                    compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);
        }

        Montants montantTemp = new Montants();

        // ecriture des cotisations (normales et restitutions)
        montantTemp = ecrisCotisations(compta, COT_AVS_ASSURE, compteAnnexeAPG.getIdCompteAnnexe(),
                sectionNormale.getIdSection(), mapTotalAVSAssure);
        montantTotalStandard.add(montantTemp);
        montantTemp = ecrisCotisations(compta, COT_AVS_INDEPENDANT, compteAnnexeAPG.getIdCompteAnnexe(),
                sectionNormale.getIdSection(), mapTotalAVSIndependant);
        montantTotalStandard.add(montantTemp);

        montantTemp = ecrisCotisations(compta, COT_AC_ASSURE, compteAnnexeAPG.getIdCompteAnnexe(),
                sectionNormale.getIdSection(), mapTotalACAssure);
        montantTotalStandard.add(montantTemp);
        montantTemp = ecrisCotisations(compta, COT_AC_INDEPENDANT, compteAnnexeAPG.getIdCompteAnnexe(),
                sectionNormale.getIdSection(), mapTotalACIndependant);
        montantTotalStandard.add(montantTemp);

        montantTotalStandard.add(totalFA);
        doEcriture(compta,
                totalFA.getMontantCumule(Montants.TYPE_ACM + "_" + Montants.TYPE_APG + "_" + Montants.TYPE_ACM_NE + "_" + Montants.TYPE_PANDEMIE)
                        .toString(), FRAIS_ADMINISTRATION, compteAnnexeAPG.getIdCompteAnnexe(),
                sectionNormale.getIdSection(), null);

        montantTemp = ecrisCotisations(compta, COT_LFA, compteAnnexeAPG.getIdCompteAnnexe(),
                sectionNormale.getIdSection(), mapTotalLFA);
        montantTotalStandard.add(montantTemp);

        final Montants mntIS = new Montants();
        if (typeLot.equals(IPRDemande.CS_TYPE_MATERNITE)) {
            FWCurrency montantAssure = totalISAssure.getMontant(Montants.TYPE_AMAT);
            FWCurrency montantIndependant = totalISIndependant.getMontant(Montants.TYPE_AMAT);

            if ((montantAssure != null) && !montantAssure.isZero()) {
                doEcriture(compta, montantAssure.toString(), IMPOT_SOURCE_ASSURE, compteAnnexeAPG.getIdCompteAnnexe(),
                        sectionNormale.getIdSection(), null);

                mntIS.add(Montants.TYPE_APG, montantAssure);
            }
            if ((montantIndependant != null) && !montantIndependant.isZero()) {
                doEcriture(compta, montantIndependant.toString(), IMPOT_SOURCE_INDEPENDANT,
                        compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(), null);

                mntIS.add(Montants.TYPE_APG, montantIndependant);
            }

            montantAssure = totalISAssure.getMontant(Montants.TYPE_LAMAT);
            montantIndependant = totalISIndependant.getMontant(Montants.TYPE_LAMAT);
            if ((montantAssure != null) && !montantAssure.isZero()) {
                doEcriture(compta, montantAssure.toString(), IMPOT_SOURCE_LAMAT_CANTONALE_ASSURE,
                        compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(), null);

                mntIS.add(Montants.TYPE_APG, montantAssure);
            }
            if ((montantIndependant != null) && !montantIndependant.isZero()) {
                doEcriture(compta, montantIndependant.toString(), IMPOT_SOURCE_LAMAT_CANTONALE_INDEPENDANT,
                        compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(), null);

                mntIS.add(Montants.TYPE_APG, montantIndependant);
            }

            montantAssure = totalISAssure.getMontant(Montants.TYPE_ACM);
            montantIndependant = totalISIndependant.getMontant(Montants.TYPE_ACM);
            if ((montantAssure != null) && !montantAssure.isZero()) {
                doEcriture(compta, montantAssure.toString(), IMPOT_SOURCE_ACM, compteAnnexeAPG.getIdCompteAnnexe(),
                        sectionNormale.getIdSection(), null);

                mntIS.add(Montants.TYPE_ACM, montantAssure);
            }
            if ((montantIndependant != null) && !montantIndependant.isZero()) {
                doEcriture(compta, montantIndependant.toString(), IMPOT_SOURCE_ACM,
                        compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(), null);

                mntIS.add(Montants.TYPE_ACM, montantIndependant);
            }

        } else {
            FWCurrency montantAssure = totalISAssure.getMontant(Montants.TYPE_APG);
            FWCurrency montantIndependant = totalISIndependant.getMontant(Montants.TYPE_APG);

            if ((montantAssure != null) && !montantAssure.isZero()) {
                doEcriture(compta, montantAssure.toString(), IMPOT_SOURCE_ASSURE, compteAnnexeAPG.getIdCompteAnnexe(),
                        sectionNormale.getIdSection(), null);

                mntIS.add(Montants.TYPE_APG, montantAssure);
            }
            if ((montantIndependant != null) && !montantIndependant.isZero()) {
                doEcriture(compta, montantIndependant.toString(), IMPOT_SOURCE_INDEPENDANT,
                        compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(), null);

                mntIS.add(Montants.TYPE_APG, montantIndependant);
            }

            montantAssure = totalISAssure.getMontant(Montants.TYPE_ACM);
            montantIndependant = totalISIndependant.getMontant(Montants.TYPE_ACM);
            if ((montantAssure != null) && !montantAssure.isZero()) {
                doEcriture(compta, montantAssure.toString(), IMPOT_SOURCE_ACM, compteAnnexeAPG.getIdCompteAnnexe(),
                        sectionNormale.getIdSection(), null);

                mntIS.add(Montants.TYPE_ACM, montantAssure);
            }
            if ((montantIndependant != null) && !montantIndependant.isZero()) {
                doEcriture(compta, montantIndependant.toString(), IMPOT_SOURCE_ACM,
                        compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(), null);

                mntIS.add(Montants.TYPE_ACM, montantIndependant);
            }

            montantAssure = totalISAssure.getMontant(Montants.TYPE_ACM_NE);
            montantIndependant = totalISIndependant.getMontant(Montants.TYPE_ACM_NE);
            if ((montantAssure != null) && !montantAssure.isZero()) {
                doEcriture(compta, montantAssure.toString(), mapAcmNeBean.get(typeAssociationPourAcmNe)
                                .getRubriqueImpotSource(), compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                        null);

                mntIS.add(Montants.TYPE_ACM_NE, montantAssure);
            }
            if ((montantIndependant != null) && !montantIndependant.isZero()) {
                doEcriture(compta, montantIndependant.toString(), mapAcmNeBean.get(typeAssociationPourAcmNe)
                                .getRubriqueImpotSource(), compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                        null);

                mntIS.add(Montants.TYPE_ACM_NE, montantIndependant);
            }

            montantAssure = totalISAssure.getMontant(Montants.TYPE_COMPCIAB);
            montantIndependant = totalISIndependant.getMontant(Montants.TYPE_COMPCIAB);
            if ((montantAssure != null) && !montantAssure.isZero()) {

                doEcriture(compta, montantAssure.toString(), mapComplementBean.get(typeComplement.canton)
                                .getRubriquePersonnelImpotSource(), compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                        null);

                mntIS.add(Montants.TYPE_COMPCIAB, montantAssure);
            }
            if ((montantIndependant != null) && !montantIndependant.isZero()) {
                doEcriture(compta, montantIndependant.toString(), mapComplementBean.get(typeComplement.canton)
                                .getRubriqueParitaireImpotSource(), compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                        null);

                mntIS.add(Montants.TYPE_COMPCIAB, montantIndependant);
            }

            montantAssure = totalISAssure.getMontant(Montants.TYPE_PANDEMIE);
            montantIndependant = totalISIndependant.getMontant(Montants.TYPE_PANDEMIE);
            if ((montantAssure != null) && !montantAssure.isZero()) {

                doEcriture(compta, montantAssure.toString(), IMPOT_SOURCE_ASSURE, compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                        null);

                mntIS.add(Montants.TYPE_PANDEMIE, montantAssure);
            }
            if ((montantIndependant != null) && !montantIndependant.isZero()) {
                doEcriture(compta, montantIndependant.toString(), IMPOT_SOURCE_INDEPENDANT, compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                        null);

                mntIS.add(Montants.TYPE_PANDEMIE, montantIndependant);
            }
        }

        montantTotalStandard.add(mntIS);

        montantTotalStandard.add(totalFondDeCompensationAssure);
        doEcriture(compta, totalFondDeCompensationAssure.getMontantCumule(Montants.TYPE_ACM + "_" + Montants.TYPE_APG + "_" + Montants.TYPE_PANDEMIE + "_" + Montants.TYPE_MATCIAB)
                        .toString(), FONDS_DE_COMPENSATION_ASSURE, compteAnnexeAPG.getIdCompteAnnexe(),
                sectionNormale.getIdSection(), null);
        montantTotalStandard.add(totalFondDeCompensationEmployeur);
        doEcriture(compta,
                totalFondDeCompensationEmployeur.getMontantCumule(Montants.TYPE_ACM + "_" + Montants.TYPE_APG + "_" + Montants.TYPE_PANDEMIE + "_" + Montants.TYPE_MATCIAB)
                        .toString(), FONDS_DE_COMPENSATION_EMPLOYEUR, compteAnnexeAPG.getIdCompteAnnexe(),
                sectionNormale.getIdSection(), null);
        montantTotalStandard.add(totalFondDeCompensationIndependant);
        doEcriture(compta,
                totalFondDeCompensationIndependant.getMontantCumule(Montants.TYPE_ACM + "_" + Montants.TYPE_APG + "_" + Montants.TYPE_PANDEMIE + "_" + Montants.TYPE_MATCIAB)
                        .toString(), FONDS_DE_COMPENSATION_INDEPENDANT, compteAnnexeAPG.getIdCompteAnnexe(),
                sectionNormale.getIdSection(), null);

        montantTotalStandard.add(totalFondDeCompensationACM);
        doEcriture(compta, totalFondDeCompensationACM.getMontantCumule(Montants.TYPE_ACM + "_" + Montants.TYPE_APG)
                        .toString(), FONDS_DE_COMPENSATION_ACM, compteAnnexeAPG.getIdCompteAnnexe(),
                sectionNormale.getIdSection(), null);

        if ((totalFondDeCompensationACMNE != null) && !totalFondDeCompensationACMNE.isZero()) {

            montantTotalStandard.add(totalFondDeCompensationACMNE);
            doEcriture(compta,
                    totalFondDeCompensationACMNE.getMontantCumule(Montants.TYPE_ACM_NE + "_" + Montants.TYPE_APG)
                            .toString(), mapAcmNeBean.get(typeAssociationPourAcmNe).getRubriqueFondCompensation(),
                    compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(), null);

        }

        if ((totalFondDeCompensationComplementEmployeur != null) && !totalFondDeCompensationComplementEmployeur.isZero()) {
            APIRubrique rubrique = null;
            rubrique = mapComplementBean.get(typeComplement.getCanton().getValue()).getRubriqueParitaireParticipationCotisation();

            montantTotalStandard.add(totalFondDeCompensationComplementEmployeur);
            doEcriture(compta,
                    totalFondDeCompensationComplementEmployeur.getMontantCumule(Montants.TYPE_COMPCIAB + "_" + Montants.TYPE_APG + "_" + Montants.TYPE_MATCIAB)
                            .toString(), rubrique, compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(), null);

        }

        if ((totalFondDeCompensationComplementIndependant != null) && !totalFondDeCompensationComplementIndependant.isZero()) {
            APIRubrique rubrique = null;
            rubrique = mapComplementBean.get(typeComplement.getCanton().getValue()).getRubriquePersonnelParticipationCotisation();

            montantTotalStandard.add(totalFondDeCompensationComplementIndependant);
            doEcriture(compta,
                    totalFondDeCompensationComplementIndependant.getMontantCumule(Montants.TYPE_COMPCIAB + "_" + Montants.TYPE_APG + "_" + Montants.TYPE_MATCIAB)
                            .toString(), rubrique, compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(), null);

        }

        if (!mapTotalACComplementIndependant.isEmpty()) {
            APIRubrique rubrique = mapComplementBean.get(typeComplement.getCanton().getValue()).getRubriquePersonnelCotisationAC();
            montantTemp = ecrisCotisations(compta, rubrique, compteAnnexeAPG.getIdCompteAnnexe(),
                    sectionNormale.getIdSection(), mapTotalACComplementIndependant);
            montantTotalStandard.add(montantTemp);
        }

        if (!mapTotalAVSComplementIndependant.isEmpty()) {
            APIRubrique rubrique = mapComplementBean.get(typeComplement.getCanton().getValue()).getRubriquePersonnelCotisationAVS();
            montantTemp = ecrisCotisations(compta, rubrique, compteAnnexeAPG.getIdCompteAnnexe(),
                    sectionNormale.getIdSection(), mapTotalAVSComplementIndependant);
            montantTotalStandard.add(montantTemp);
        }

        if (hasRestitution) {

            montantTemp = ecrisCotisations(compta, COT_AVS_ASSURE, compteAnnexeAPG.getIdCompteAnnexe(),
                    sectionRestitution.getIdSection(), mapTotalAVSRestitutionAssure);
            montantTotalRestitution.add(montantTemp);
            montantTemp = ecrisCotisations(compta, COT_AVS_INDEPENDANT, compteAnnexeAPG.getIdCompteAnnexe(),
                    sectionRestitution.getIdSection(), mapTotalAVSRestitutionIndependant);
            montantTotalRestitution.add(montantTemp);

            montantTemp = ecrisCotisations(compta, COT_AC_ASSURE, compteAnnexeAPG.getIdCompteAnnexe(),
                    sectionRestitution.getIdSection(), mapTotalACRestitutionAssure);
            montantTotalRestitution.add(montantTemp);
            montantTemp = ecrisCotisations(compta, COT_AC_INDEPENDANT, compteAnnexeAPG.getIdCompteAnnexe(),
                    sectionRestitution.getIdSection(), mapTotalACRestitutionIndependant);
            montantTotalRestitution.add(montantTemp);

            montantTotalRestitution.add(totalFARestitution);
            doEcriture(
                    compta,
                    totalFARestitution.getMontantCumule(
                            Montants.TYPE_ACM + "_" + Montants.TYPE_APG + "_" + Montants.TYPE_ACM_NE + "_" + Montants.TYPE_PANDEMIE).toString(),
                    FRAIS_ADMINISTRATION, compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);

            montantTemp = ecrisCotisations(compta, COT_LFA, compteAnnexeAPG.getIdCompteAnnexe(),
                    sectionRestitution.getIdSection(), mapTotalLFARestitution);
            montantTotalRestitution.add(montantTemp);

            final Montants mntISRestit = new Montants();
            if (typeLot.equals(IPRDemande.CS_TYPE_MATERNITE)) {
                FWCurrency montantAssure = totalISRestitutionAssure.getMontant(Montants.TYPE_AMAT);
                FWCurrency montantIndependant = totalISRestitutionIndependant.getMontant(Montants.TYPE_AMAT);

                if ((montantAssure != null) && !montantAssure.isZero()) {
                    doEcriture(compta, montantAssure.toString(), IMPOT_SOURCE_ASSURE,
                            compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);

                    mntISRestit.add(Montants.TYPE_APG, montantAssure);
                }
                if ((montantIndependant != null) && !montantIndependant.isZero()) {
                    doEcriture(compta, montantIndependant.toString(), IMPOT_SOURCE_INDEPENDANT,
                            compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);

                    mntISRestit.add(Montants.TYPE_APG, montantIndependant);
                }

                montantAssure = totalISRestitutionAssure.getMontant(Montants.TYPE_LAMAT);
                montantIndependant = totalISRestitutionIndependant.getMontant(Montants.TYPE_LAMAT);
                if ((montantAssure != null) && !montantAssure.isZero()) {
                    doEcriture(compta, montantAssure.toString(), IMPOT_SOURCE_LAMAT_CANTONALE_ASSURE,
                            compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);

                    mntISRestit.add(Montants.TYPE_APG, montantAssure);
                }
                if ((montantIndependant != null) && !montantIndependant.isZero()) {
                    doEcriture(compta, montantIndependant.toString(), IMPOT_SOURCE_LAMAT_CANTONALE_INDEPENDANT,
                            compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);

                    mntISRestit.add(Montants.TYPE_APG, montantIndependant);
                }

                montantAssure = totalISRestitutionAssure.getMontant(Montants.TYPE_ACM);
                montantIndependant = totalISRestitutionIndependant.getMontant(Montants.TYPE_ACM);
                if ((montantAssure != null) && !montantAssure.isZero()) {
                    doEcriture(compta, montantAssure.toString(), IMPOT_SOURCE_ACM, compteAnnexeAPG.getIdCompteAnnexe(),
                            sectionRestitution.getIdSection(), null);

                    mntISRestit.add(Montants.TYPE_ACM, montantAssure);
                }
                if ((montantIndependant != null) && !montantIndependant.isZero()) {
                    doEcriture(compta, montantIndependant.toString(), IMPOT_SOURCE_ACM,
                            compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);

                    mntISRestit.add(Montants.TYPE_ACM, montantIndependant);
                }
            } else {
                FWCurrency montantAssure = totalISRestitutionAssure.getMontant(Montants.TYPE_APG);
                FWCurrency montantIndependant = totalISRestitutionIndependant.getMontant(Montants.TYPE_APG);

                if ((montantAssure != null) && !montantAssure.isZero()) {
                    doEcriture(compta, montantAssure.toString(), IMPOT_SOURCE_ASSURE,
                            compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);

                    mntISRestit.add(Montants.TYPE_APG, montantAssure);
                }
                if ((montantIndependant != null) && !montantIndependant.isZero()) {
                    doEcriture(compta, montantIndependant.toString(), IMPOT_SOURCE_INDEPENDANT,
                            compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);

                    mntISRestit.add(Montants.TYPE_APG, montantIndependant);
                }

                montantAssure = totalISRestitutionAssure.getMontant(Montants.TYPE_ACM);
                montantIndependant = totalISRestitutionIndependant.getMontant(Montants.TYPE_ACM);
                if ((montantAssure != null) && !montantAssure.isZero()) {
                    doEcriture(compta, montantAssure.toString(), IMPOT_SOURCE_ACM, compteAnnexeAPG.getIdCompteAnnexe(),
                            sectionRestitution.getIdSection(), null);

                    mntISRestit.add(Montants.TYPE_ACM, montantAssure);
                }
                if ((montantIndependant != null) && !montantIndependant.isZero()) {
                    doEcriture(compta, montantIndependant.toString(), IMPOT_SOURCE_ACM,
                            compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);

                    mntISRestit.add(Montants.TYPE_ACM, montantIndependant);
                }

                montantAssure = totalISRestitutionAssure.getMontant(Montants.TYPE_ACM_NE);
                montantIndependant = totalISRestitutionIndependant.getMontant(Montants.TYPE_ACM_NE);
                if ((montantAssure != null) && !montantAssure.isZero()) {
                    doEcriture(compta, montantAssure.toString(), mapAcmNeBean.get(typeAssociationPourAcmNe)
                                    .getRubriqueImpotSource(), compteAnnexeAPG.getIdCompteAnnexe(),
                            sectionRestitution.getIdSection(), null);

                    mntISRestit.add(Montants.TYPE_ACM_NE, montantAssure);
                }
                if ((montantIndependant != null) && !montantIndependant.isZero()) {
                    doEcriture(compta, montantIndependant.toString(), mapAcmNeBean.get(typeAssociationPourAcmNe)
                                    .getRubriqueImpotSource(), compteAnnexeAPG.getIdCompteAnnexe(),
                            sectionRestitution.getIdSection(), null);

                    mntISRestit.add(Montants.TYPE_ACM_NE, montantIndependant);
                }

                montantAssure = totalISRestitutionAssure.getMontant(Montants.TYPE_COMPCIAB);
                montantIndependant = totalISRestitutionIndependant.getMontant(Montants.TYPE_COMPCIAB);
                if ((montantAssure != null) && !montantAssure.isZero()) {
                    doEcriture(compta, montantAssure.toString(), mapComplementBean.get(typeComplement.canton)
                                    .getRubriquePersonnelImpotSource(), compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(),
                            null);

                    mntISRestit.add(Montants.TYPE_COMPCIAB, montantAssure);
                }
                if ((montantIndependant != null) && !montantIndependant.isZero()) {
                    doEcriture(compta, montantIndependant.toString(), mapComplementBean.get(typeComplement.canton)
                                    .getRubriquePersonnelImpotSource(), compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(),
                            null);

                    mntISRestit.add(Montants.TYPE_COMPCIAB, montantIndependant);
                }

                montantAssure = totalISRestitutionAssure.getMontant(Montants.TYPE_PANDEMIE);
                montantIndependant = totalISRestitutionIndependant.getMontant(Montants.TYPE_PANDEMIE);
                if ((montantAssure != null) && !montantAssure.isZero()) {
                    doEcriture(compta, montantAssure.toString(), IMPOT_SOURCE_ASSURE, compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(),
                            null);

                    mntISRestit.add(Montants.TYPE_PANDEMIE, montantAssure);
                }
                if ((montantIndependant != null) && !montantIndependant.isZero()) {
                    doEcriture(compta, montantIndependant.toString(), IMPOT_SOURCE_INDEPENDANT, compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(),
                            null);

                    mntISRestit.add(Montants.TYPE_PANDEMIE, montantIndependant);
                }
            }

            montantTotalRestitution.add(mntISRestit);

            montantTotalRestitution.add(totalFondDeCompensationRestitutionAssure);
            doEcriture(
                    compta,
                    totalFondDeCompensationRestitutionAssure.getMontantCumule(
                            Montants.TYPE_ACM + "_" + Montants.TYPE_APG + "_" + Montants.TYPE_PANDEMIE + "_" + Montants.TYPE_MATCIAB).toString(), FONDS_DE_COMPENSATION_ASSURE,
                    compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);
            montantTotalRestitution.add(totalFondDeCompensationRestitutionEmployeur);
            doEcriture(
                    compta,
                    totalFondDeCompensationRestitutionEmployeur.getMontantCumule(
                            Montants.TYPE_ACM + "_" + Montants.TYPE_APG + "_" + Montants.TYPE_PANDEMIE + "_" + Montants.TYPE_MATCIAB).toString(), FONDS_DE_COMPENSATION_EMPLOYEUR,
                    compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);
            montantTotalRestitution.add(totalFondDeCompensationRestitutionIndependant);
            doEcriture(
                    compta,
                    totalFondDeCompensationRestitutionIndependant.getMontantCumule(
                            Montants.TYPE_ACM + "_" + Montants.TYPE_APG + "_" + Montants.TYPE_PANDEMIE + "_" + Montants.TYPE_MATCIAB).toString(), FONDS_DE_COMPENSATION_INDEPENDANT,
                    compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);

            montantTotalRestitution.add(totalFondDeCompensationRestitutionACM);
            doEcriture(compta,
                    totalFondDeCompensationRestitutionACM.getMontantCumule(Montants.TYPE_ACM + "_" + Montants.TYPE_APG + "_" + Montants.TYPE_PANDEMIE)
                            .toString(), FONDS_DE_COMPENSATION_ACM, compteAnnexeAPG.getIdCompteAnnexe(),
                    sectionRestitution.getIdSection(), null);

            if ((totalFondDeCompensationRestitutionACMNE != null) && !totalFondDeCompensationRestitutionACMNE.isZero()) {

                montantTotalRestitution.add(totalFondDeCompensationRestitutionACMNE);
                doEcriture(
                        compta,
                        totalFondDeCompensationRestitutionACMNE.getMontantCumule(
                                Montants.TYPE_ACM_NE + "_" + Montants.TYPE_APG + "_" + Montants.TYPE_PANDEMIE).toString(),
                        mapAcmNeBean.get(typeAssociationPourAcmNe).getRubriqueFondCompensation(),
                        compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);

            }

            if ((totalFondDeCompensationComplementRestitutionEmployeur != null) && !totalFondDeCompensationComplementRestitutionEmployeur.isZero()) {
                APIRubrique rubrique = null;
                rubrique = mapComplementBean.get(typeComplement.getCanton().getValue()).getRubriqueParitaireParticipationCotisation();

                montantTotalRestitution.add(totalFondDeCompensationComplementRestitutionEmployeur);
                doEcriture(compta,
                        totalFondDeCompensationComplementRestitutionEmployeur.getMontantCumule(Montants.TYPE_COMPCIAB + "_" + Montants.TYPE_APG + "_" + Montants.TYPE_PANDEMIE + "_" + Montants.TYPE_MATCIAB)
                                .toString(), rubrique, compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);
            }

            if ((totalFondDeCompensationComplementRestitutionIndependant != null) && !totalFondDeCompensationComplementRestitutionIndependant.isZero()) {
                APIRubrique rubrique = null;
                rubrique = mapComplementBean.get(typeComplement.getCanton().getValue()).getRubriquePersonnelParticipationCotisation();

                montantTotalRestitution.add(totalFondDeCompensationComplementRestitutionIndependant);
                doEcriture(compta,
                        totalFondDeCompensationComplementRestitutionIndependant.getMontantCumule(Montants.TYPE_COMPCIAB + "_" + Montants.TYPE_APG + "_" + Montants.TYPE_PANDEMIE + "_" + Montants.TYPE_MATCIAB)
                                .toString(), rubrique, compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);

            }

            if (!mapTotalACComplementRestitutionIndependant.isEmpty()) {
                APIRubrique rubrique = mapComplementBean.get(typeComplement.getCanton().getValue()).getRubriquePersonnelCotisationAC();
                montantTemp = ecrisCotisations(compta, rubrique, compteAnnexeAPG.getIdCompteAnnexe(),
                        sectionRestitution.getIdSection(), mapTotalACComplementRestitutionIndependant);
                montantTotalRestitution.add(montantTemp);
            }

            if (!mapTotalAVSComplementRestitutionIndependant.isEmpty()) {
                APIRubrique rubrique = mapComplementBean.get(typeComplement.getCanton().getValue()).getRubriquePersonnelCotisationAVS();
                montantTemp = ecrisCotisations(compta, rubrique, compteAnnexeAPG.getIdCompteAnnexe(),
                        sectionRestitution.getIdSection(), mapTotalAVSComplementRestitutionIndependant);
                montantTotalRestitution.add(montantTemp);
            }

        }

        // compensations
        final Iterator compensationsPourCetIdIterator = compensationsPourCetId.iterator();

        // creation de la section pour les compensations seulement si nécessaire
        APICompteAnnexe compteAnnexeAffilie = null;
        APISection sectionCompensationFutures = null;

        while (compensationsPourCetIdIterator.hasNext()) {
            final Compensation compensation = (Compensation) compensationsPourCetIdIterator.next();

            final boolean isGenrePrestationACMAlpha = APTypeDePrestation.ACM_ALFA
                    .isCodeSystemEqual(compensation.genrePrestation)
                    || APTypeDePrestation.ACM2_ALFA.isCodeSystemEqual(compensation.genrePrestation);
            final boolean isGenrePrestationACMNe = APTypeDePrestation.ACM_NE
                    .isCodeSystemEqual(compensation.genrePrestation);

            if (isGenrePrestationACMNe && !montantTotalCotFNE.isZero()) {
                // Attention modifie également la valeur de la compensation dans la map de base
                // OK pas d'impact détecté
                // idFacture à 0 permet d'aller compenser sur le compte de l'affilié
                BigDecimal montantCompensation = new BigDecimal(compensation.montant);
                montantCompensation = montantCompensation.subtract(new BigDecimal(montantTotalCotFNE.toString()));
                compensation.montant = montantCompensation.toString();
                compensation.idFacture = "0";
            }

            BigDecimal montantACompenserInverse = new BigDecimal(compensation.montant);
            montantACompenserInverse = montantACompenserInverse.negate();
            Montants mnt = new Montants();
            mnt.add(convertGenrePrestToRubrique(compensation.genrePrestation), montantACompenserInverse.toString());
            montantTotalStandard.add(mnt);

            String propRubrique = null;

            if (isGenrePrestationACMAlpha || isGenrePrestationACMNe) {
                propRubrique = getSession().getApplication().getProperty(
                        APGenererEcrituresComptablesProcess.PROP_RUBRIQUE_COMPENSATION_ACM);
            } else {
                propRubrique = getSession().getApplication().getProperty(
                        APGenererEcrituresComptablesProcess.PROP_RUBRIQUE_COMPENSATION);
            }

            final boolean isPropRubriqueCompensationACMAlpha = "COMPENSATION_ALFA".equals(propRubrique);
            final boolean isPropRubriqueCompensationACMNe = APGenererEcrituresComptablesProcess.COMPENSATION_ACMNE
                    .equals(propRubrique);

            APIRubrique rubriqueCompensationACMAlphaOrNe = null;

            if (isPropRubriqueCompensationACMAlpha) {
                rubriqueCompensationACMAlphaOrNe = COMPENSATION_ACM;
            } else if (isPropRubriqueCompensationACMNe) {
                rubriqueCompensationACMAlphaOrNe = mapAcmNeBean.get(typeAssociationPourAcmNe).getRubriqueCompensation();
            }

            if ((montantACompenserInverse.signum() == 1) && (sectionRestitution != null)) {

                // Compensation pour une prestation LAMAT
                if (APTypeDePrestation.LAMAT.isCodeSystemEqual(compensation.genrePrestation)) {
                    doEcriture(compta, montantACompenserInverse.toString(), COMPENSATION_LAMAT,
                            compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);
                } else {
                    if ("RUBRIQUE_DE_LISSAGE".equals(propRubrique)) {
                        doEcriture(compta, montantACompenserInverse.toString(), COMPENSATION,
                                compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);
                    } else if (isPropRubriqueCompensationACMAlpha || isPropRubriqueCompensationACMNe) {
                        doEcriture(compta, montantACompenserInverse.toString(), rubriqueCompensationACMAlphaOrNe,
                                compteAnnexeAPG.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);
                    }
                }
            } else {
                if (APTypeDePrestation.LAMAT.isCodeSystemEqual(compensation.genrePrestation)) {
                    doEcriture(compta, montantACompenserInverse.toString(), COMPENSATION_LAMAT,
                            compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(), null);
                } else {
                    if ("RUBRIQUE_DE_LISSAGE".equals(propRubrique)) {
                        doEcriture(compta, montantACompenserInverse.toString(), COMPENSATION,
                                compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(), null);
                    } else if (isPropRubriqueCompensationACMAlpha || isPropRubriqueCompensationACMNe) {
                        doEcriture(compta, montantACompenserInverse.toString(), rubriqueCompensationACMAlphaOrNe,
                                compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(), null);
                    }
                }
            }

            if (!JadeStringUtil.isIntegerEmpty(compensation.idFacture)) {
                // c'est une facture existante
                final APISection section = new CASection();
                section.setISession(getSession());
                section.setIdSection(compensation.idFacture);
                section.retrieve(getTransaction());

                mnt = new Montants();
                mnt.add(convertGenrePrestToRubrique(compensation.genrePrestation), compensation.montant);
                montantTotalStandard.add(mnt);

                if (APTypeDePrestation.LAMAT.isCodeSystemEqual(compensation.genrePrestation)) {
                    doEcriture(compta, compensation.montant, COMPENSATION_LAMAT, section.getIdCompteAnnexe(),
                            section.getIdSection(), null);
                } else {
                    if ("RUBRIQUE_DE_LISSAGE".equals(propRubrique)) {
                        doEcriture(compta, compensation.montant, COMPENSATION, section.getIdCompteAnnexe(),
                                section.getIdSection(), null);
                    } else if (isPropRubriqueCompensationACMAlpha || isPropRubriqueCompensationACMNe) {
                        doEcriture(compta, compensation.montant, rubriqueCompensationACMAlphaOrNe,
                                section.getIdCompteAnnexe(), section.getIdSection(), null);
                    }
                }

            } else {
                // facture future
                if (compteAnnexeAffilie == null) {

                    // Uniquement pour les affilié. Si compensation manuelle sur
                    // facture future pour un assuré
                    // non répertorié dans la liste des situations prof. avec no
                    // affilié, généré message d'erreur.
                    if (JadeStringUtil.isIntegerEmpty(key.idAffilie)) {
                        getMemoryLog().logMessage("Erreur : Compensation impossible pour non affilié!!!",
                                FWMessage.ERREUR, this.getClass().getName());
                        getMemoryLog().logMessage("Erreur : idExterne = " + idExterneRole, FWMessage.ERREUR,
                                this.getClass().getName());
                        throw new Exception("Compensation impossible pour non affilié!!!");
                    }

                    String idRole = null;
                    if (compensation.isCompensationPourRoleAffiliePersonnel) {
                        idRole = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                                getSession().getApplication());
                    } else {
                        idRole = CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                                getSession().getApplication());
                    }

                    compteAnnexeAffilie = compta.getCompteAnnexeByRole(key.idTiers, idRole, idExterneRole);

                    sectionCompensationFutures = compta.getSectionByIdExterne(compteAnnexeAffilie.getIdCompteAnnexe(),
                            APISection.ID_TYPE_SECTION_APG, noFactureNormale);

                }

                // montantTotalCotFNE et mapTotalCotFNE contiennent le même montant
                if (isGenrePrestationACMNe && !montantTotalCotFNE.isZero()) {

                    if (JadeStringUtil.isBlankOrZero(key.idAffilie)) {
                        throw new Exception("Can't write coti fne for a non affilie");
                    }

                    ecrisCotisationsFNE(compta, ((AcmNeFneBean) mapAcmNeBean.get(APAssuranceTypeAssociation.FNE
                                    .getCodesystemToString())).getRubriqueCotisation(),
                            compteAnnexeAffilie.getIdCompteAnnexe(), sectionCompensationFutures.getIdSection(),
                            mapTotalCotFNE, key.idAffilie);
                    montantTotalCotFNE = new FWCurrency("0");

                }

                mnt = new Montants();
                mnt.add(convertGenrePrestToRubrique(compensation.genrePrestation), compensation.montant);
                montantTotalStandard.add(mnt);

                if (APTypeDePrestation.LAMAT.isCodeSystemEqual(compensation.genrePrestation)) {
                    doEcriture(compta, compensation.montant, COMPENSATION_LAMAT,
                            compteAnnexeAffilie.getIdCompteAnnexe(), sectionCompensationFutures.getIdSection(), null);
                } else {
                    if ("RUBRIQUE_DE_LISSAGE".equals(propRubrique)) {
                        doEcriture(compta, compensation.montant, COMPENSATION, compteAnnexeAffilie.getIdCompteAnnexe(),
                                sectionCompensationFutures.getIdSection(), null);
                    } else if (isPropRubriqueCompensationACMAlpha || isPropRubriqueCompensationACMNe) {
                        doEcriture(compta, compensation.montant, rubriqueCompensationACMAlphaOrNe,
                                compteAnnexeAffilie.getIdCompteAnnexe(), sectionCompensationFutures.getIdSection(),
                                null);
                    }
                }
            }
        }

        // Un ordre de versement pour chaque nature de versement à effectuer...
        int iNature = 0;
        do {
            String nature = null;
            String typeMontant = null;

            if (typeLot.equals(IPRDemande.CS_TYPE_MATERNITE)) {
                switch (iNature) {
                    // AMAT et LAMAT ne doivent produire qu'un seul versement avec les montants cumulés
                    // On va donc mettre seulement un des deux types dans le typeMontant
                    case 0:
                        nature = getSession().getApplication().getProperty(
                                APGenererEcrituresComptablesProcess.PROP_NATURE_VERSEMENT_AMAT);
                        typeMontant = Montants.TYPE_AMAT;

                        // Si il y a des montants MATCIAB on additionne les montants de type AMAT et MATCIAB
                        if (!cumulMontantsParType(new FWCurrency(0), montantsBrutTotal, totalCotisations, compensationsTotale, ventilationTotale, Montants.TYPE_MATCIAB).isZero()) {
                            typeMontant = Montants.TYPE_AMAT + "_" + Montants.TYPE_MATCIAB;
                        }
                        break;
                    // case 1:
                    // nature = getSession().getApplication().getProperty(
                    // APGenererEcrituresComptablesProcess.PROP_NATURE_VERSEMENT_LAMAT);
                    // typeMontant = Montants.TYPE_LAMAT;
                    // break;
                    case 1:
                        nature = getSession().getApplication().getProperty(
                                APGenererEcrituresComptablesProcess.PROP_NATURE_VERSEMENT_ACM_MAT);
                        typeMontant = Montants.TYPE_ACM;
                        break;
                    default:
                        iNature = 100;
                        break;
                }

            } else if (StringUtils.equals(IPRDemande.CS_TYPE_PANDEMIE, typeLot)) {
                switch (iNature) {
                    case 0:
                        nature = getSession().getApplication().getProperty(
                                APGenererEcrituresComptablesProcess.PROP_NATURE_VERSEMENT_PANDEMIE);
                        typeMontant = Montants.TYPE_PANDEMIE;
                        break;
                    default:
                        iNature = 100;
                        break;
                }
            } else {
                switch (iNature) {
                    case 0:
                        nature = getSession().getApplication().getProperty(
                                APGenererEcrituresComptablesProcess.PROP_NATURE_VERSEMENT_APG);
                        typeMontant = Montants.TYPE_APG + "_" + Montants.TYPE_COMPCIAB;
                        break;
                    case 1:
                        nature = getSession().getApplication().getProperty(
                                APGenererEcrituresComptablesProcess.PROP_NATURE_VERSEMENT_ACM_APG);
                        typeMontant = Montants.TYPE_ACM + "_" + Montants.TYPE_ACM_NE;
                        break;
                    default:
                        iNature = 100;
                        break;
                }
            }
            ++iNature;
            if (iNature < 100) {

                // versement effectif
                FWCurrency versement = new FWCurrency(0);
                // On regroupe les type AMAT (Fédéral) et LAMAT (Cantonal)
                if (Montants.TYPE_AMAT.equals(typeMontant)) {
                    versement = cumulMontantsParType(versement, montantsBrutTotal, totalCotisations,
                            compensationsTotale, ventilationTotale, Montants.TYPE_AMAT);
                    versement = cumulMontantsParType(versement, montantsBrutTotal, totalCotisations,
                            compensationsTotale, ventilationTotale, Montants.TYPE_LAMAT);
                } else {
                    versement = montantsBrutTotal.getMontantCumule(typeMontant);
                    versement.add(new FWCurrency(totalCotisations.getMontantCumule(typeMontant).toString()));
                    versement.sub(new FWCurrency(compensationsTotale.getMontantCumule(typeMontant).toString()));
                    versement.sub(new FWCurrency(ventilationTotale.getMontantCumule(typeMontant).toString()));
                }

                if (versement.isPositive()) {
                    doOrdreVersement(compta, compteAnnexeAPG.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                            versement.toString(), idAdressePaiementBeneficiaireDeBase, nature, null, null);
                }

                if (getTransaction().hasErrors()) {
                    throw new Exception(getTransaction().getErrors().toString());
                }

            }
        } while (iNature < 100);

        // Retourne les éventuelles compensations interne pour ce
        // benéficiaire...
        final CompensationInterne ci = new CompensationInterne();
        ci.typeAssociation = typeAssociationPourAcmNe;

        ci.montantTotPrestations.add(montantTotalStandard);
        ci.montantTotRestitutions.add(montantTotalRestitution);
        ci.idCompteAnnexe = compteAnnexeAPG.getIdCompteAnnexe();

        if (sectionNormale != null) {
            ci.idSectionNormale = sectionNormale.getIdSection();
        }

        if (sectionRestitution != null) {
            ci.idSectionRestitution = sectionRestitution.getIdSection();
        }
        return ci;

    }

    private FWCurrency cumulMontantsParType(FWCurrency versement, Montants montantsBrutTotal,
                                            Montants totalCotisations, Montants compensationsTotale, Montants ventilationTotale, String typeAmat) {
        versement.add(new FWCurrency(montantsBrutTotal.getMontantCumule(typeAmat).toString()));
        versement.add(new FWCurrency(totalCotisations.getMontantCumule(typeAmat).toString()));
        versement.sub(new FWCurrency(compensationsTotale.getMontantCumule(typeAmat).toString()));
        versement.sub(new FWCurrency(ventilationTotale.getMontantCumule(typeAmat).toString()));
        return versement;
    }

    private void putMontantIntoMap(Map<String, Montants> montantsInMap, Repartition repartition, String cotisation) {
        if (montantsInMap.containsKey(repartition.anneeCotisation)) {
            final Montants mnt = new Montants();
            mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), cotisation);

            montantsInMap.get(repartition.anneeCotisation).add(mnt);
            montantsInMap.get(repartition.anneeCotisation).add(mnt);
        } else {
            final Montants mnt = new Montants();
            mnt.add(convertGenrePrestToRubrique(repartition.genrePrestation), cotisation);

            montantsInMap.put(repartition.anneeCotisation, mnt);
            montantsInMap.get(repartition.anneeCotisation).add(mnt);
        }
    }

    private Montants getCumulMontantsParGenre(final Montants montants, final String genrePrestation,
                                              final String montant) {

        // Cumul des cotisations par genre de prestations
        // Les montants ACM et ACM 2 sont cumulés
        if (APTypeDePrestation.ACM_ALFA.isCodeSystemEqual(genrePrestation)
                || APTypeDePrestation.ACM2_ALFA.isCodeSystemEqual(genrePrestation)) {
            montants.add(Montants.TYPE_ACM, montant);
        }

        if (APTypeDePrestation.ACM_NE.isCodeSystemEqual(genrePrestation)) {
            montants.add(Montants.TYPE_ACM_NE, montant);
        }

        if (APTypeDePrestation.COMPCIAB.isCodeSystemEqual(genrePrestation)) {
            montants.add(Montants.TYPE_COMPCIAB, montant);
        }

        if (APTypeDePrestation.MATCIAB1.isCodeSystemEqual(genrePrestation)
        || APTypeDePrestation.MATCIAB2.isCodeSystemEqual(genrePrestation)) {
            montants.add(Montants.TYPE_MATCIAB, montant);
        }

        if (APTypeDePrestation.JOUR_ISOLE.isCodeSystemEqual(genrePrestation)) {
            montants.add(Montants.TYPE_COMPCIAB, montant);
        }

        if (APTypeDePrestation.PANDEMIE.isCodeSystemEqual(genrePrestation)) {
            montants.add(Montants.TYPE_PANDEMIE, montant);
        }

        if (typeLot.equals(IPRDemande.CS_TYPE_MATERNITE)) {
            if (APTypeDePrestation.LAMAT.isCodeSystemEqual(genrePrestation)) {
                montants.add(Montants.TYPE_LAMAT, montant);
            } else if (APTypeDePrestation.STANDARD.isCodeSystemEqual(genrePrestation)) {
                montants.add(Montants.TYPE_AMAT, montant);
            }
        } else {
            if (APTypeDePrestation.STANDARD.isCodeSystemEqual(genrePrestation)) {
                montants.add(Montants.TYPE_APG, montant);
            }
        }
        return montants;

    }

    /**
     * getter pour l'attribut date comptable
     *
     * @return la valeur courante de l'attribut date comptable
     */
    public String getDateComptable() {
        return dateComptable;
    }

    /**
     * @return
     */
    public String getDateSurDocument() {
        return dateSurDocument;
    }

    /**
     * (non-Javadoc)
     *
     * @return la valeur courante de l'attribut EMail object
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS");
    }

    /**
     * getter pour l'attribut no lot
     *
     * @return la valeur courante de l'attribut no lot
     */
    public String getIdLot() {
        return idLot;
    }

    /**
     * renvoie une map contenant des listes de APGenererEcrituresComptablesProcess.Repartition regroupées par idTiers,
     * idAffilie et idAdressePaiement
     *
     * @return DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     *                   <p>
     *                   ATTENTION : Les répartitions doivent être lcassées
     *                   </p>
     */
    private Map getMapRepartitions() throws Exception {
        final APRepartJointCotJointPrestJointEmployeurManager repartJointCotJointPrestManager = new APRepartJointCotJointPrestJointEmployeurManager();
        repartJointCotJointPrestManager.setSession(getSession());
        repartJointCotJointPrestManager.setForIdLot(idLot);
        repartJointCotJointPrestManager.setForParentOnly(true);
        repartJointCotJointPrestManager.setOrderBy(APCotisation.FIELDNAME_IDREPARTITIONBENEFICIAIREPAIEMENT);

        final BStatement statement = repartJointCotJointPrestManager.cursorOpen(getTransaction());

        APRepartJointCotJointPrestJointEmployeur repartJointCotJointPrest = (APRepartJointCotJointPrestJointEmployeur) repartJointCotJointPrestManager
                .cursorReadNext(statement);

        final Map repartitions = new HashMap();

        while (repartJointCotJointPrest != null) {
            // On a à ce moment la premiere ligne d'une série de lignes
            // correspondant à la même répartition de
            // paiement (la repartition est la même, la différence entre les
            // lignes est la cotisation

            final String idRepartitionPaiement = repartJointCotJointPrest.getIdRepartitionBeneficiairePaiement();
            final boolean isEmployeur = repartJointCotJointPrest.getTypePaiement().equals(
                    IAPRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR);
            final boolean isIndependant = repartJointCotJointPrest.getIsIndependant().booleanValue();
            boolean hasAC = false;
            boolean hasCotisation = false;
            final String idTiers = repartJointCotJointPrest.getIdTiers();
            final String idAffilie = repartJointCotJointPrest.getIdAffilie();
            final String noRevision = repartJointCotJointPrest.getNoRevision();
            final boolean isRestitution = repartJointCotJointPrest.getContenuAnnonce()
                    .equals(IAPAnnonce.CS_RESTITUTION);

            final Repartition repartition = new Repartition();
            repartition.montant = repartJointCotJointPrest.getMontantBrut();
            repartition.typeAssociation = repartJointCotJointPrest.getTypeAssociationAssurance();

            if (JadeStringUtil.isBlankOrZero(repartJointCotJointPrest.getIdTiersAdressePaiement())) {
                // Si exception, c'est qu'il n'y a pas d'adresse de paiement...
                final APRepartitionPaiementsManager rep = new APRepartitionPaiementsManager();
                rep.setForIdParent(repartJointCotJointPrest.getIdRepartitionBeneficiairePaiement());
                rep.setNotForIdRepartition(repartJointCotJointPrest.getIdRepartitionBeneficiairePaiement());
                rep.setSession(getSession());

                // on check l'adresse de paiement, seulement si le montant net -
                // les ventilations donnent un reste
                final FWCurrency totalVentilations = new FWCurrency(rep.getSum("VIMMOV", statement.getTransaction())
                        .toString());
                final FWCurrency montantCtrl = new FWCurrency(repartJointCotJointPrest.getMontantNet());
                montantCtrl.sub(totalVentilations);

                if (montantCtrl.isPositive()) {
                    throw new Exception("Pas d'adresse de paiement pour idTiers : "
                            + repartJointCotJointPrest.getIdTiers() + " idAff : "
                            + repartJointCotJointPrest.getIdAffilie() + " en date du " + getDateComptable());
                }
            } else {
                try {
                    repartition.idAdressePaiement = repartJointCotJointPrest.loadAdressePaiement(getDateComptable())
                            .getIdAvoirPaiementUnique();

                } catch (final NullPointerException e) {
                    throw new Exception("Pas d'adresse de paiement pour idTiers : "
                            + repartJointCotJointPrest.getIdTiers() + " idAff : "
                            + repartJointCotJointPrest.getIdAffilie() + " en date du " + getDateComptable());
                }
            }

            repartition.idRepartitionPaiement = repartJointCotJointPrest.getIdRepartitionBeneficiairePaiement();
            repartition.isRestitution = isRestitution;
            repartition.idCompensation = repartJointCotJointPrest.getIdCompensation();
            repartition.isMaternite = noRevision.equals(IAPDroitMaternite.CS_REVISION_MATERNITE_2005) ? true : false;
            repartition.isEmployeur = isEmployeur;
            repartition.isIndependant = repartJointCotJointPrest.getIsIndependant().booleanValue();
            repartition.anneeCotisation = String.valueOf(new JADate(repartJointCotJointPrest.getDateDebutPrestation())
                    .getYear());
            repartition.genrePrestation = repartJointCotJointPrest.getGenrePrestation();
            repartition.idDepartement = repartJointCotJointPrest.getIdDepartement();
            Boolean isPorteEnCompte = isSituationProfPorteEnCompte(repartJointCotJointPrest
                    .getIdSituationProfessionnelle());

            final String idPrestation = repartJointCotJointPrest.getIdPrestationApg();
            // On regarde ce qu'on a a regarder pour chaque cotisation de cette
            // repartition de paiement
            do {
                final String genreCotisation = repartJointCotJointPrest.getGenreCotisation();

                // On regarde si il y a un genre de cotisation ET si l'idExterne
                // (id de l'assurance) n'est pas vide
                // (cela peut arriver si l'id n'est pas mis dans APG.properties)
                // sauf si c'est un impot à la source, il est possible de ne pas
                // avoir d'id externe
                if (genreCotisation.equals(APRepartJointCotJointPrestJointEmployeur.IMPOT_SOURCE)
                        || (!JadeStringUtil.isEmpty(genreCotisation) && !JadeStringUtil
                        .isIntegerEmpty(repartJointCotJointPrest.getIdExterne()))) {
                    final String montantCotisation = repartJointCotJointPrest.getMontantCotisation();
                    hasCotisation = true;

                    if (genreCotisation.equals(APRepartJointCotJointPrestJointEmployeur.AC)) {
                        hasAC = true;
                        repartition.cotisationAC = montantCotisation;
                    } else if (genreCotisation.equals(APRepartJointCotJointPrestJointEmployeur.AVS)) {
                        repartition.cotisationAVS = montantCotisation;
                    } else if (genreCotisation.equals(APRepartJointCotJointPrestJointEmployeur.FRAIS_ADMINISTRATION)) {
                        repartition.fraisAdministration = montantCotisation;
                    } else if (genreCotisation.equals(APRepartJointCotJointPrestJointEmployeur.IMPOT_SOURCE)) {
                        repartition.impotSource = montantCotisation;
                    } else if (genreCotisation.equals(APRepartJointCotJointPrestJointEmployeur.LFA)) {
                        repartition.cotisationLFA = montantCotisation;
                    } else if (APRepartJointCotJointPrestJointEmployeur.COT_FNE.equals(genreCotisation)) {
                        repartition.cotisationFNE = montantCotisation;
                        repartition.montantBrutCotisationFNE = repartJointCotJointPrest.getMontantBrutCotisation();
                        repartition.tauxCotisationFNE = repartJointCotJointPrest.getTauxCotisation();
                    } else {
                        // ben là on est pas dans la mouise...
                        throw new Exception("Imposible de trouver le genre de la cotisation");
                    }
                }
            } while (((repartJointCotJointPrest = (APRepartJointCotJointPrestJointEmployeur) repartJointCotJointPrestManager
                    .cursorReadNext(statement)) != null)
                    && idRepartitionPaiement.equals(repartJointCotJointPrest.getIdRepartitionBeneficiairePaiement()));

            Key key = null;

            // On récupère l'id de l'assuré de base
            final APPrestation prestation = new APPrestation();
            prestation.setSession(getSession());
            prestation.setIdPrestationApg(idPrestation);
            prestation.retrieve(getTransaction());

            repartition.idDroit = prestation.getIdDroit();

            final APDroitLAPG droit = new APDroitLAPG();
            droit.setSession(getSession());
            droit.setIdDroit(prestation.getIdDroit());
            droit.retrieve(getTransaction());

            APDroitPanSituationManager panSituationManager = new APDroitPanSituationManager();
            panSituationManager.setSession(getSession());
            panSituationManager.setForIdDroit(prestation.getIdDroit());
            panSituationManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
            APDroitPanSituation droitPanSituation = (APDroitPanSituation) panSituationManager.getFirstEntity();

            // Maternité
            boolean isAdoption = false;

            if (noRevision.equals(IAPDroitMaternite.CS_REVISION_MATERNITE_2005)) {
                final APSituationFamilialeMat sfMat = new APSituationFamilialeMat();
                sfMat.setSession(getSession());
                sfMat.setAlternateKey(APSituationFamilialeMat.ALTERNATE_KEY_ID_DROIT_TYPE);
                sfMat.setIdDroitMaternite(prestation.getIdDroit());
                sfMat.setType(IAPDroitMaternite.CS_TYPE_ENFANT);
                sfMat.retrieve(getTransaction());

                isAdoption = sfMat.getIsAdoption().booleanValue();
            }

            TypeComplement typeComplement = null;
            if (APTypeDePrestation.COMPCIAB.isCodeSystemEqual(repartition.genrePrestation) ||
                    APTypeDePrestation.JOUR_ISOLE.isCodeSystemEqual(repartition.genrePrestation)) {
                typeComplement = getTypeComplementFromAssurance(getSession(), idAffilie, prestation.getIdDroit(), isIndependant);
            }
            if (APTypeDePrestation.MATCIAB1.isCodeSystemEqual(repartition.genrePrestation) ||
                    APTypeDePrestation.MATCIAB2.isCodeSystemEqual(repartition.genrePrestation)) {
                typeComplement = getTypeComplementFromAssuranceDateDebut(getSession(), idAffilie, prestation.getIdDroit(), isIndependant);
            }

            boolean isManifestationAnnulee = false;
            String genreService;
            if (isRestitution && StringUtils.isNotEmpty(droit.getIdDroitParent())) {
                final APDroitLAPG droitParent = new APDroitLAPG();
                droitParent.setSession(getSession());
                droitParent.setIdDroit(droit.getIdDroitParent());
                droitParent.retrieve(getTransaction());

                panSituationManager.setForIdDroit(droit.getIdDroitParent());
                panSituationManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
                APDroitPanSituation droitPanSituationParent = (APDroitPanSituation) panSituationManager.getFirstEntity();

                genreService = droitParent.getGenreService();

                if (Objects.nonNull(droitPanSituationParent)) {
                    isManifestationAnnulee = StringUtils.isNotEmpty(droitPanSituationParent.getDateDebutManifestationAnnulee());
                }
            } else {
                // Dans le cas d'une correction de droit, on regarde le droit parent pour la restituion.
                genreService = droit.getGenreService();

                if (Objects.nonNull(droitPanSituation)) {
                    isManifestationAnnulee = StringUtils.isNotEmpty(droitPanSituation.getDateDebutManifestationAnnulee());
                }
            }

            // choix de la rubrique
            repartition.rubriqueConcernee = getRubriqueConcernee(isEmployeur, isIndependant, hasAC, hasCotisation,
                    isRestitution, repartition.genrePrestation, isAdoption, repartition.typeAssociation, typeComplement, genreService, isManifestationAnnulee);

            final String idAssureDeBase = droit.loadDemande().getIdTiers();

            // Cas ou le bénéficiaire est l'assuré de base
            if (idAssureDeBase.equals(idTiers)) {
                // choix de la section
                repartition.section = getSection(idTiers, idAffilie, isRestitution);
                key = new Key(idTiers, idAffilie, "0", "0", "0", false, false, repartition.idAdressePaiement, false);

            }
            // Cas ou le bénéficiaire est un affilié
            else if (!JadeStringUtil.isIntegerEmpty(idAffilie)) {
                // choix de la section
                repartition.section = getSection(idTiers, idAffilie, isRestitution);
                key = new Key(idTiers, idAffilie, "0", "0", "0", false, false, repartition.idAdressePaiement,
                        isPorteEnCompte);

            }
            // Cas ou le bénéficiaire est un employeur non affilié,
            // la clé est composé de l'idtiers du l'assuré principal.
            // Ceci implique que : les écritures seront effectuées sur le CA de
            // l'assuré.
            // On garde cependant l'adresse de paiement du non affilié pour le
            // versement.
            else {
                // choix de la section
                repartition.section = getSection(idAssureDeBase, idAffilie, isRestitution);
                key = new Key(idAssureDeBase, "0", idTiers, "0", "0", false, false, repartition.idAdressePaiement,
                        false);
            }

            if (repartitions.containsKey(key)) {
                ((List) repartitions.get(key)).add(repartition);
            } else {
                final List l = new ArrayList();
                l.add(repartition);
                repartitions.put(key, l);
            }
        }

        return repartitions;
    }

    private TypeComplement getTypeComplementFromAssuranceDateDebut(BSession session, String idAffilie, String idDroit, boolean isIndependant) throws Exception {
        // list les cantons
        String idAssuranceParitaireJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID);
        String idAssurancePersonnelJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID);
        String idAssuranceParitaireBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID);
        String idAssurancePersonnelBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID);

        Map<IAFAssurance, String> listAssurance = APRechercherAssuranceFromDroitCotisationService.rechercherAvecDateDebut(idDroit,
                idAffilie, session);
        for (Map.Entry<IAFAssurance, String> assurance : listAssurance.entrySet()) {
            if (isIndependant) {
                if (assurance.getKey().getAssuranceId().equals(idAssurancePersonnelBE)) {
                    return TypeComplement.BE_PERSONNEL;
                } else if (assurance.getKey().getAssuranceId().equals(idAssurancePersonnelJU)) {
                    return TypeComplement.JU_PERSONNEL;
                }
            } else {
                if (assurance.getKey().getAssuranceId().equals(idAssuranceParitaireBE)) {
                    return TypeComplement.BE_PARITAIRE;
                } else if (assurance.getKey().getAssuranceId().equals(idAssuranceParitaireJU)) {
                    return TypeComplement.JU_PARITAIRE;
                }
            }
        }
        return null;
    }

    private TypeComplement getTypeComplementFromAssurance(BSession session, String idAffilie, String idDroit, boolean isIndependant) throws Exception {
        // list les cantons
        String idAssuranceParitaireJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID);
        String idAssurancePersonnelJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID);
        String idAssuranceParitaireBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID);
        String idAssurancePersonnelBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID);
        List<IAFAssurance> listAssurance = APRechercherAssuranceFromDroitCotisationService.rechercher(idDroit,
                idAffilie, session);
        for (IAFAssurance assurance : listAssurance) {
            if (isIndependant) {
                if (assurance.getAssuranceId().equals(idAssurancePersonnelBE)) {
                    return TypeComplement.BE_PERSONNEL;
                } else if (assurance.getAssuranceId().equals(idAssurancePersonnelJU)) {
                    return TypeComplement.JU_PERSONNEL;
                }
            } else {
                if (assurance.getAssuranceId().equals(idAssuranceParitaireBE)) {
                    return TypeComplement.BE_PARITAIRE;
                } else if (assurance.getAssuranceId().equals(idAssuranceParitaireJU)) {
                    return TypeComplement.JU_PARITAIRE;
                }
            }
        }
        return null;
    }

    /**
     * Recherche la situation professionnelle avec son ID et récupère son champ isPorteEnCompte
     *
     * @param idSituationProfessionnelle Retourne true si la situation professionnelle passée en paramètre est portée en compte
     * @throws Exception
     */
    private boolean isSituationProfPorteEnCompte(String idSituationProfessionnelle) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(idSituationProfessionnelle)) {
            APSituationProfessionnelle situationPro = new APSituationProfessionnelle();
            situationPro.setId(idSituationProfessionnelle);
            situationPro.setSession(getSession());
            situationPro.retrieve(getTransaction());
            if (!situationPro.isNew()) {
                return situationPro.getIsPorteEnCompte();
            }
        }
        return false;
    }

    /**
     * @param idRepartitionPmt
     * @return NSS du requérant principal, à partir de l'id de la répartition des paiements. null si non trouvé.
     */
    private String getNomPrenomRequerantPrincipal(final String idRepartitionPmt) {

        String nom = null;

        try {
            final APRepartitionPaiements rep = new APRepartitionPaiements();
            rep.setSession(getSession());
            rep.setIdRepartitionBeneficiairePaiement(idRepartitionPmt);
            rep.retrieve();

            if (!rep.isNew()) {
                final String idPrestation = rep.getIdPrestationApg();
                final APPrestation prestation = new APPrestation();
                prestation.setSession(getSession());
                prestation.setIdPrestationApg(idPrestation);
                prestation.retrieve();
                if (!prestation.isNew()) {

                    // prestation.getid

                    final APDroitLAPG droit = new APDroitLAPG();
                    droit.setSession(getSession());
                    droit.setIdDroit(prestation.getIdDroit());
                    droit.retrieve();

                    if (!droit.isNew()) {
                        final PRTiersWrapper tiersWP = droit.loadDemande().loadTiers();
                        nom = tiersWP.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                + tiersWP.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                    }
                }
            }
        } catch (final Exception e) {
            JadeLogger.info(e, e.getLocalizedMessage());
            return null;
        }
        return nom;
    }

    /**
     * @param isEmployeur     si c'est un employeur
     * @param isIndependant   Si c'est un indépendant
     * @param hasAC           si il a des cotisations AC
     * @param hasCotisation   si il a des cotisations
     * @param isRestitution   s'il s'agit d'une restitution
     * @param genrePrestation normal, ACM, LAMAT
     * @param genreSerivce
     * @param isManifestationAnnulee
     * @return la rubrique concernée
     */
    protected APIRubrique getRubriqueConcernee(final boolean isEmployeur, final boolean isIndependant,
                                               final boolean hasAC, final boolean hasCotisation, final boolean isRestitution,
                                               final String genrePrestation, final boolean isAdoption, final String typeAssociation, TypeComplement typeComplement, String genreSerivce, boolean isManifestationAnnulee) {
        APIRubrique rubrique = null;

        final GenreDestinataire destinataire = GenreDestinataire.getDestinataire(isEmployeur, isIndependant);

        boolean isLamat = false;
        if (APTypeDePrestation.LAMAT.isCodeSystemEqual(genrePrestation)) {
            isLamat = true;
        }

        // cas des ACM
        if (APTypeDePrestation.ACM_ALFA.isCodeSystemEqual(genrePrestation)
                || APTypeDePrestation.ACM2_ALFA.isCodeSystemEqual(genrePrestation)) {
            if (isRestitution) {
                rubrique = ACM_RESTITUTION;
            } else {
                rubrique = ACM_MONTANT_BRUT;
            }
        } else if (APTypeDePrestation.ACM_NE.isCodeSystemEqual(genrePrestation)) {
            if (isRestitution) {
                rubrique = mapAcmNeBean.get(typeAssociation).getRubriqueRestitution();
            } else {
                rubrique = mapAcmNeBean.get(typeAssociation).getRubriqueDeBase();
            }
        } else if (APTypeDePrestation.COMPCIAB.isCodeSystemEqual(genrePrestation)
                || APTypeDePrestation.JOUR_ISOLE.isCodeSystemEqual(genrePrestation)) { // Complément
            if (isRestitution) {
                if ("PARITAIRE".equals(typeComplement.genre)) {
                    rubrique = mapComplementBean.get(typeComplement.getCanton().getValue()).getRubriqueParitaireRestitution();
                } else if ("PERSONNEL".equals(typeComplement.genre)) {
                    rubrique = mapComplementBean.get(typeComplement.getCanton().getValue()).getRubriquePersonnelRestitution();
                }
            } else {
                if ("PARITAIRE".equals(typeComplement.genre)) {
                    rubrique = mapComplementBean.get(typeComplement.getCanton().getValue()).getRubriqueParitaireMontantBrut();
                } else if ("PERSONNEL".equals(typeComplement.genre)) {
                    rubrique = mapComplementBean.get(typeComplement.getCanton().getValue()).getRubriquePersonnelMontantBrut();
                }
            }
        }  else if (APTypeDePrestation.MATCIAB1.isCodeSystemEqual(genrePrestation) ||APTypeDePrestation.MATCIAB2.isCodeSystemEqual(genrePrestation)) { // Complément
                if (isRestitution) {
                    if ("PARITAIRE".equals(typeComplement.genre)) {
                        rubrique = mapComplementBean.get(typeComplement.getCanton().getValue()).getRubriqueParitaireRestitution();
                    } else if ("PERSONNEL".equals(typeComplement.genre)) {
                        rubrique = mapComplementBean.get(typeComplement.getCanton().getValue()).getRubriquePersonnelRestitution();
                    }
                } else {
                    if ("PARITAIRE".equals(typeComplement.genre)) {
                        rubrique = mapComplementBean.get(typeComplement.getCanton().getValue()).getRubriqueParitaireMontantBrut();
                    } else if ("PERSONNEL".equals(typeComplement.genre)) {
                        rubrique = mapComplementBean.get(typeComplement.getCanton().getValue()).getRubriquePersonnelMontantBrut();
                    }
                }
        } else if (APTypeDePrestation.PANDEMIE.isCodeSystemEqual(genrePrestation)) {
            rubrique = getRubriquePandemie(isIndependant, isRestitution, genreSerivce, isManifestationAnnulee, rubrique);
        } else if (isRestitution) {
            if (isLamat) {
                if (GenreDestinataire.INDEPENDANT.equals(destinataire)) {
                    rubrique = PRESTATION_A_RESTITUER_LAMAT_INDEPENDANT;
                } else if (GenreDestinataire.EMPLOYEUR.equals(destinataire)) {
                    rubrique = PRESTATION_A_RESTITUER_LAMAT_EMPLOYEUR;
                } else {
                    rubrique = PRESTATION_A_RESTITUER_LAMAT_ASSURE;
                }
            } else {
                if (GenreDestinataire.INDEPENDANT.equals(destinataire)) {
                    rubrique = PRESTATION_A_RESTITUER_INDEPENDANT;
                } else if (GenreDestinataire.EMPLOYEUR.equals(destinataire)) {
                    rubrique = PRESTATION_A_RESTITUER_EMPLOYEUR;
                } else {
                    rubrique = PRESTATION_A_RESTITUER_ASSURE;
                }
            }
        } else {
            if (!hasCotisation) {
                if (isLamat) {
                    if (isAdoption) {
                        if (GenreDestinataire.INDEPENDANT.equals(destinataire)) {
                            rubrique = SANS_COTISATION_LAMAT_ADOPTION_INDEPENDANT;
                        } else if (GenreDestinataire.EMPLOYEUR.equals(destinataire)) {
                            rubrique = SANS_COTISATION_LAMAT_ADOPTION_EMPLOYEUR;
                        } else {
                            rubrique = SANS_COTISATION_LAMAT_ADOPTION_ASSURE;
                        }
                    } else {
                        if (GenreDestinataire.INDEPENDANT.equals(destinataire)) {
                            rubrique = SANS_COTISATION_LAMAT_NAISSANCE_INDEPENDANT;
                        } else if (GenreDestinataire.EMPLOYEUR.equals(destinataire)) {
                            rubrique = SANS_COTISATION_LAMAT_NAISSANCE_EMPLOYEUR;
                        } else {
                            rubrique = SANS_COTISATION_LAMAT_NAISSANCE_ASSURE;
                        }
                    }
                } else {
                    rubrique = SANS_COTISATION;
                }
            } else {
                // Pour les cas Lamat avec impôt à la source
                if (isLamat) {
                    if (isAdoption) {
                        if (GenreDestinataire.INDEPENDANT.equals(destinataire)) {
                            rubrique = SANS_COTISATION_LAMAT_ADOPTION_INDEPENDANT;
                        } else if (GenreDestinataire.EMPLOYEUR.equals(destinataire)) {
                            rubrique = SANS_COTISATION_LAMAT_ADOPTION_EMPLOYEUR;
                        } else {
                            rubrique = SANS_COTISATION_LAMAT_ADOPTION_ASSURE;
                        }
                    } else {
                        if (GenreDestinataire.INDEPENDANT.equals(destinataire)) {
                            rubrique = SANS_COTISATION_LAMAT_NAISSANCE_INDEPENDANT;
                        } else if (GenreDestinataire.EMPLOYEUR.equals(destinataire)) {
                            rubrique = SANS_COTISATION_LAMAT_NAISSANCE_EMPLOYEUR;
                        } else {
                            rubrique = SANS_COTISATION_LAMAT_NAISSANCE_ASSURE;
                        }
                    }
                } else {
                    if (hasAC) {
                        if (GenreDestinataire.INDEPENDANT.equals(destinataire)) {
                            rubrique = AVEC_AC_INDEPENDANT;
                        } else if (GenreDestinataire.EMPLOYEUR.equals(destinataire)) {
                            rubrique = AVEC_AC_EMPLOYEUR;
                        } else {
                            rubrique = AVEC_AC_ASSURE;
                        }
                    } else {
                        if (GenreDestinataire.INDEPENDANT.equals(destinataire)) {
                            rubrique = SANS_AC_INDEPENDANT;
                        } else if (GenreDestinataire.EMPLOYEUR.equals(destinataire)) {
                            rubrique = SANS_AC_EMPLOYEUR;
                        } else {
                            rubrique = SANS_AC_ASSURE;
                        }
                    }
                }
            }
        }

        return rubrique;
    }

    /**
     * permet de récupérer la rubrique pour les APG pandémie en fonction du genre de service.
     *
     * @param isIndependant
     * @param isRestitution
     * @param genreSerivce
     * @param isManifestationAnnulee
     * @param rubrique
     * @return
     */
    private APIRubrique getRubriquePandemie(boolean isIndependant, boolean isRestitution, String genreSerivce, boolean isManifestationAnnulee, APIRubrique rubrique) {
        if (isRestitution) {
            if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_GARDE_PARENTALE)) {
                if (isIndependant) {
                    rubrique = RESTIT_GARDE_ENFANT_INDEPENDANT;
                } else {
                    rubrique = RESTIT_GARDE_ENFANT_SALARIE;
                }
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_QUARANTAINE)) {
                if (isIndependant) {
                    rubrique = RESTIT_QUARANTAINE_INDEPENDANT;
                } else {
                    rubrique = RESTIT_QUARANTAINE_SALARIE;
                }
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_INDEPENDANT_PANDEMIE)) {
                if (isManifestationAnnulee) {
                    rubrique = RESTIT_INTERDICTION_MANIFESTATION;
                } else {
                    rubrique = RESTIT_FERMETURE_FORCEE;
                }
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_INDEPENDANT_PERTE_GAINS)) {
                rubrique = RESTIT_CAS_RIGUEUR_10k_90k;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP)) {
                if (isIndependant) {
                    rubrique = RESTIT_GARDE_ENFANT_HANDICAP_INDEPENDANT;
                } else {
                    rubrique = RESTIT_GARDE_ENFANT_HANDICAP_SALARIE;
                }
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_INDEPENDANT_MANIF_ANNULEE)) {
                rubrique = RESTIT_INTERDICTION_MANIFESTATION;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_SALARIE_EVENEMENTIEL)) {
                rubrique = RESTIT_SALARIE_EVENEMENTIEL;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_INDEPENDANT_FERMETURE)) {
                rubrique = RESTIT_INDEPANDANT_FERMETURE;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_DIRIGEANT_SALARIE_FERMETURE)) {
                rubrique = RESTIT_DIRIGEANT_SALARIE_FERMETURE;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_INDEPENDANT_MANIFESTATION_ANNULEE)) {
                rubrique = RESTIT_INDEPENDANT_MANIF_ANNULEE;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_DIRIGEANT_SALARIE_MANIFESTATION_ANNULEE)) {
                rubrique = RESTIT_DIRIGEANT_SALARIE_MANIF_ANNULEE;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_INDEPENDANT_LIMITATION_ACTIVITE)) {
                rubrique = RESTIT_INDEPENDANT_LIMITATION_ACTIVITE;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE)) {
                rubrique = RESTIT_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_QUARANTAINE_17_09_20)) {
                if (isIndependant) {
                    rubrique = RESTIT_QUARANTAINE_17_09_20_INDEPENDANT;
                } else {
                    rubrique = RESTIT_QUARANTAINE_17_09_20_SALARIE;
                }
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_GARDE_PARENTALE_17_09_20)) {
                if (isIndependant) {
                    rubrique = RESTIT_GARDE_PARENTALE_17_09_20_INDEPENDANT;
                } else {
                    rubrique = RESTIT_GARDE_PARENTALE_17_09_20_SALARIE;
                }
            }  else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP_17_09_20)) {
                if (isIndependant) {
                    rubrique = RESTIT_GARDE_PARENTALE_HANDICAP_17_09_20_INDEPENDANT;
                } else {
                    rubrique = RESTIT_GARDE_PARENTALE_HANDICAP_17_09_20_SALARIE;
                }
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_SALARIE_PERSONNE_VULNERABLE)) {
                rubrique = RESTIT_PERSONNE_VULNERABLE_SALARIE;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_INDEPENDANT_PERSONNE_VULNERABLE)) {
                rubrique = RESTIT_PERSONNE_VULNERABLE_INDEPENDANT;
            }
        } else {
            if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_GARDE_PARENTALE)) {
                if (isIndependant) {
                    rubrique = INDEMN_GARDE_ENFANT_INDEPENDANT;
                } else {
                    rubrique = INDEMN_GARDE_ENFANT_SALARIE;
                }
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_QUARANTAINE)) {
                if (isIndependant) {
                    rubrique = INDEMN_QUARANTAINE_INDEPENDANT;
                } else {
                    rubrique = INDEMN_QUARANTAINE_SALARIE;
                }
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_INDEPENDANT_PANDEMIE)) {
                if (isManifestationAnnulee) {
                    rubrique = INDEMN_INTERDICTION_MANIFESTATION;
                } else {
                    rubrique = INDEMN_FERMETURE_FORCEE;
                }
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_INDEPENDANT_PERTE_GAINS)) {
                rubrique = INDEMN_CAS_RIGUEUR_10k_90k;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP)) {
                if (isIndependant) {
                    rubrique = INDEMN_GARDE_ENFANT_HANDICAP_INDEPENDANT;
                } else {
                    rubrique = INDEMN_GARDE_ENFANT_HANDICAP_SALARIE;
                }
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_INDEPENDANT_MANIF_ANNULEE)) {
                rubrique = INDEMN_INTERDICTION_MANIFESTATION;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_SALARIE_EVENEMENTIEL)) {
                rubrique = INDEMN_SALARIE_EVENEMENTIEL;
            }else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_INDEPENDANT_FERMETURE)) {
                rubrique = INDEMN_INDEPANDANT_FERMETURE;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_DIRIGEANT_SALARIE_FERMETURE)) {
                rubrique = INDEMN_DIRIGEANT_SALARIE_FERMETURE;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_INDEPENDANT_MANIFESTATION_ANNULEE)) {
                rubrique = INDEMN_INDEPENDANT_MANIF_ANNULEE;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_DIRIGEANT_SALARIE_MANIFESTATION_ANNULEE)) {
                rubrique = INDEMN_DIRIGEANT_SALARIE_MANIF_ANNULEE;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_INDEPENDANT_LIMITATION_ACTIVITE)) {
                rubrique = INDEMN_INDEPENDANT_LIMITATION_ACTIVITE;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE)) {
                rubrique = INDEMN_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_QUARANTAINE_17_09_20)) {
                if (isIndependant) {
                    rubrique = INDEMN_QUARANTAINE_17_09_20_INDEPENDANT;
                } else {
                    rubrique = INDEMN_QUARANTAINE_17_09_20_SALARIE;
                }
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_GARDE_PARENTALE_17_09_20)) {
                if (isIndependant) {
                    rubrique = INDEMN_GARDE_PARENTALE_17_09_20_INDEPENDANT;
                } else {
                    rubrique = INDEMN_GARDE_PARENTALE_17_09_20_SALARIE;
                }
            }  else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP_17_09_20)) {
                if (isIndependant) {
                    rubrique = INDEMN_GARDE_PARENTALE_HANDICAP_17_09_20_INDEPENDANT;
                } else {
                    rubrique = INDEMN_GARDE_PARENTALE_HANDICAP_17_09_20_SALARIE;
                }
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_SALARIE_PERSONNE_VULNERABLE)) {
                rubrique = INDEMN_PERSONNE_VULNERABLE_SALARIE;
            } else if (Objects.equals(genreSerivce, IAPDroitLAPG.CS_INDEPENDANT_PERSONNE_VULNERABLE)) {
                rubrique = INDEMN_PERSONNE_VULNERABLE_INDEPENDANT;
            }
        }
        return rubrique;
    }

    private String getSection(final String idTiers, final String idAffilie, final boolean isRestitution)
            throws Exception {
        String idExterne = null;

        if (!JadeStringUtil.isIntegerEmpty(idAffilie)) {
            idExterne = PRAffiliationHelper
                    .getEmployeurParIdAffilie(getSession(), getTransaction(), idAffilie, idTiers).getNumAffilie();
        } else {
            idExterne = PRTiersHelper.getTiersParId(getSession(), idTiers).getProperty(
                    PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        }

        String section = null;

        if (isRestitution) {
            section = "restitution " + idExterne;
        } else {
            section = "APG " + idExterne;
        }

        return section;
    }

    private String getSqlCaisseProf(String idAffilie, String assuranceId) {

        String sql = " SELECT  plancaisse.HTITIE AS "
                + APGenererEcrituresComptablesProcess.REQUETE_SQL_CAISSE_PROF_COL_NAME_ID_TIERS_ADMINISTRATION + " "
                + " FROM schema.AFCOTIP AS cotisation "
                + " INNER JOIN schema.AFADHEP AS adhesion ON cotisation.MRIADH = adhesion.MRIADH "
                + " INNER JOIN schema.AFAFFIP AS affiliation ON adhesion.MAIAFF = affiliation.MAIAFF "
                + " INNER JOIN schema.AFPLCAP AS plancaisse ON plancaisse.MSIPLC = cotisation.MSIPLC "
                + " WHERE cotisation.mbiass = " + assuranceId + " and " + " affiliation.MAIAFF = " + idAffilie + " "
                + " order by cotisation.meddeb desc ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;

    }

    /**
     * Initialise les Id des rubriques
     *
     * @param sessionOsiris une instance de APIProcessComptabilisation
     * @throws Exception DOCUMENT ME!
     */
    private void initRubriques(final BISession sessionOsiris) throws Exception {
        final APIReferenceRubrique referenceRubrique = (APIReferenceRubrique) sessionOsiris
                .getAPIFor(APIReferenceRubrique.class);

        if (typeLot.equals(IPRDemande.CS_TYPE_MATERNITE)) {
            AVEC_AC_EMPLOYEUR = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_EMPLOYEUR_AVEC_AC);
            SANS_AC_EMPLOYEUR = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_EMPLOYEUR_SANS_AC);

            AVEC_AC_ASSURE = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_AVEC_AC_ASSURE);
            AVEC_AC_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_AVEC_AC_INDEPENDANT);

            SANS_AC_ASSURE = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_SANS_AC_ASSURE);
            SANS_AC_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_SANS_AC_INDEPENDANT);

            SANS_COTISATION = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_SANS_COTISATIONS);

            SANS_COTISATION_LAMAT_ADOPTION_ASSURE = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.PRESTATION_LAMATGE_ADOPTION_ASSURE);
            SANS_COTISATION_LAMAT_ADOPTION_EMPLOYEUR = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.PRESTATION_LAMATGE_ADOPTION_EMPLOYEUR);
            SANS_COTISATION_LAMAT_ADOPTION_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.PRESTATION_LAMATGE_ADOPTION_INDEPENDANT);

            SANS_COTISATION_LAMAT_NAISSANCE_ASSURE = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.PRESTATION_LAMATGE_NAISSANCE_ASSURE);
            SANS_COTISATION_LAMAT_NAISSANCE_EMPLOYEUR = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.PRESTATION_LAMATGE_NAISSANCE_EMPLOYEUR);
            SANS_COTISATION_LAMAT_NAISSANCE_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.PRESTATION_LAMATGE_NAISSANCE_INDEPENDANT);

            PRESTATION_A_RESTITUER_LAMAT_ASSURE = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.PRESTATION_LAMATGE_RESTITUTION_ASSURE);
            PRESTATION_A_RESTITUER_LAMAT_EMPLOYEUR = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.PRESTATION_LAMATGE_RESTITUTION_EMPLOYEUR);
            PRESTATION_A_RESTITUER_LAMAT_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.PRESTATION_LAMATGE_RESTITUTION_INDEPENDANT);

            COMPENSATION_LAMAT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.COMPENSATION_APG_MAT);

            PRESTATION_A_RESTITUER_ASSURE = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_PRESTATION_A_RESTITUER_ASSURE);
            PRESTATION_A_RESTITUER_EMPLOYEUR = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_PRESTATION_A_RESTITUER_EMPLOYEUR);
            PRESTATION_A_RESTITUER_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_PRESTATION_A_RESTITUER_INDEPENDANT);

            COT_AVS_ASSURE = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_COTISATION_AVS_ASSURE);
            COT_AVS_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_COTISATION_AVS_INDEPENDANT);

            COT_AC_ASSURE = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_COTISATION_AC_ASSURE);
            COT_AC_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_COTISATION_AC_INDEPENDANT);

            FONDS_DE_COMPENSATION_ASSURE = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_FONDS_DE_COMPENSATION_ASSURE);
            FONDS_DE_COMPENSATION_EMPLOYEUR = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_FONDS_DE_COMPENSATION_EMPLOYEUR);
            FONDS_DE_COMPENSATION_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_FONDS_DE_COMPENSATION_INDEPENDANT);

            FONDS_DE_COMPENSATION_ACM = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_ACM_FONDS_DE_COMPENSATION);

            IMPOT_SOURCE_ASSURE = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_IMPOT_A_LA_SOURCE_ASSURE);
            IMPOT_SOURCE_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_IMPOT_A_LA_SOURCE_INDEPENDANT);

            IMPOT_SOURCE_LAMAT_CANTONALE_ASSURE = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_IMPOT_A_LA_SOURCE_LAMAT_CANTONALE_ASSURE);
            IMPOT_SOURCE_LAMAT_CANTONALE_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_IMPOT_A_LA_SOURCE_LAMAT_CANTONALE_INDEPENDANT);

            IMPOT_SOURCE_ACM = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_IMPOT_A_LA_SOURCE_ACM);

            COT_LFA = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_COTISATION_LFA);
            FRAIS_ADMINISTRATION = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_FRAIS_ADMINISTRATION);
            COMPENSATION = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.COMPENSATION_APG_MAT);
            COMPENSATION_ACM = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.COMPENSATION_ALFA);
            ACM_MONTANT_BRUT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_ACM_MONTANT_BRUT);
            ACM_RESTITUTION = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATERNITE_ACM_RESTITUTION);

            final ComplementBean matciabJUBean = new ComplementBean();
            matciabJUBean.setRubriqueParitaireParticipationCotisation(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATCIAB_JU_PARITAIRE_PARTICIPATION));
            matciabJUBean.setRubriqueParitaireMontantBrut(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATCIAB_JU_PARITAIRE_MONTANT_BRUT));
            matciabJUBean.setRubriqueParitaireRestitution(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATCIAB_JU_PARITAIRE_MONTANT_RESTITUTION));
            matciabJUBean.setRubriquePersonnelParticipationCotisation(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATCIAB_JU_PERSONNEL_PARTICIPATION));
            matciabJUBean.setRubriquePersonnelMontantBrut(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATCIAB_JU_PERSONNEL_MONTANT_BRUT));
            matciabJUBean.setRubriquePersonnelRestitution(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATCIAB_JU_PERSONNEL_MONTANT_RESTITUTION));
            matciabJUBean.setRubriquePersonnelCotisationAC(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATCIAB_JU_PERSONNEL_COTISATIONS_AC));
            matciabJUBean.setRubriquePersonnelCotisationAVS(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATCIAB_JU_PERSONNEL_COTISATIONS_AVS));

            mapComplementBean.put(ECanton.JU.getValue(), matciabJUBean);

            final ComplementBean matciabBEBean = new ComplementBean();
            matciabBEBean.setRubriqueParitaireParticipationCotisation(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATCIAB_BE_PARITAIRE_PARTICIPATION));
            matciabBEBean.setRubriqueParitaireMontantBrut(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATCIAB_BE_PARITAIRE_MONTANT_BRUT));
            matciabBEBean.setRubriqueParitaireRestitution(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATCIAB_BE_PARITAIRE_MONTANT_RESTITUTION));
            matciabBEBean.setRubriquePersonnelParticipationCotisation(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATCIAB_BE_PERSONNEL_PARTICIPATION));
            matciabBEBean.setRubriquePersonnelMontantBrut(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATCIAB_BE_PERSONNEL_MONTANT_BRUT));
            matciabBEBean.setRubriquePersonnelRestitution(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATCIAB_BE_PERSONNEL_MONTANT_RESTITUTION));
            matciabBEBean.setRubriquePersonnelCotisationAC(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATCIAB_BE_PERSONNEL_COTISATIONS_AC));
            matciabBEBean.setRubriquePersonnelCotisationAVS(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.MATCIAB_BE_PERSONNEL_COTISATIONS_AVS));

            mapComplementBean.put(ECanton.BE.getValue(), matciabBEBean);


        } else if (typeLot.equals(IPRDemande.CS_TYPE_PANDEMIE)) {
            INDEMN_GARDE_ENFANT_SALARIE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_GARDE_ENFANTS_POUR_SALARIE);
            INDEMN_GARDE_ENFANT_INDEPENDANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_GARDE_ENFANTS_POUR_INDEPENDANT);
            INDEMN_QUARANTAINE_SALARIE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_QUARANTAINE_POUR_SALARIE);
            INDEMN_QUARANTAINE_INDEPENDANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_QUARANTAINE_POUR_INDEPENDANT);
            INDEMN_FERMETURE_FORCEE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_FERMETURE_FORCEE);
            INDEMN_INTERDICTION_MANIFESTATION = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_INTERDICTION_MANIFESTATION);
            INDEMN_CAS_RIGUEUR_10k_90k = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_CAS_RIGUEUR_10k_90k);
            INDEMN_GARDE_ENFANT_HANDICAP_SALARIE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_GARDE_ENFANTS_HANDICAP_POUR_SALARIE);
            INDEMN_GARDE_ENFANT_HANDICAP_INDEPENDANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_GARDE_ENFANTS_HANDICAP_POUR_INDEPENDANT);
            INDEMN_SALARIE_EVENEMENTIEL = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_SALARIE_EVENEMENTIEL);
            INDEMN_INDEPANDANT_FERMETURE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEPANDANT_FERMETURE);
            INDEMN_DIRIGEANT_SALARIE_FERMETURE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_DIRIGEANT_SALARIE_FERMETURE);
            INDEMN_INDEPENDANT_MANIF_ANNULEE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEPENDANT_MANIF_ANNULEE);
            INDEMN_DIRIGEANT_SALARIE_MANIF_ANNULEE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_DIRIGEANT_SALARIE_MANIF_ANNULEE);
            INDEMN_INDEPENDANT_LIMITATION_ACTIVITE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEPENDANT_LIMITATION_ACTIVITE);
            INDEMN_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE);

            INDEMN_QUARANTAINE_17_09_20_INDEPENDANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_QUARANTAINE_17_09_20_INDEPENDANT);
            INDEMN_QUARANTAINE_17_09_20_SALARIE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_QUARANTAINE_17_09_20_SALARIE);
            INDEMN_GARDE_PARENTALE_17_09_20_INDEPENDANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_GARDE_PARENTALE_17_09_20_INDEPENDANT);
            INDEMN_GARDE_PARENTALE_17_09_20_SALARIE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_GARDE_PARENTALE_17_09_20_SALARIE);
            INDEMN_GARDE_PARENTALE_HANDICAP_17_09_20_INDEPENDANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_GARDE_PARENTALE_HANDICAP_17_09_20_INDEPENDANT);
            INDEMN_GARDE_PARENTALE_HANDICAP_17_09_20_SALARIE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_GARDE_PARENTALE_HANDICAP_17_09_20_SALARIE);

            INDEMN_PERSONNE_VULNERABLE_SALARIE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_PERSONNE_VULNERABLE_SALARIE);
            INDEMN_PERSONNE_VULNERABLE_INDEPENDANT= referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_INDEMN_PERSONNE_VULNERABLE_INDEPENDANT);

            RESTIT_GARDE_ENFANT_SALARIE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_GARDE_ENFANTS_POUR_SALARIE);
            RESTIT_GARDE_ENFANT_INDEPENDANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_GARDE_ENFANTS_POUR_INDEPENDANT);
            RESTIT_QUARANTAINE_SALARIE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_QUARANTAINE_POUR_SALARIE);
            RESTIT_QUARANTAINE_INDEPENDANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_QUARANTAINE_POUR_INDEPENDANT);
            RESTIT_FERMETURE_FORCEE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_FERMETURE_FORCEE);
            RESTIT_INTERDICTION_MANIFESTATION = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_INTERDICTION_MANIFESTATION);
            RESTIT_CAS_RIGUEUR_10k_90k = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_CAS_RIGUEUR_10k_90k);
            RESTIT_GARDE_ENFANT_HANDICAP_SALARIE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_GARDE_ENFANTS_HANDICAP_POUR_SALARIE);
            RESTIT_GARDE_ENFANT_HANDICAP_INDEPENDANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_GARDE_ENFANTS_HANCICAP_POUR_INDEPENDANT);
            RESTIT_SALARIE_EVENEMENTIEL = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_SALARIE_EVENEMENTIEL);
            RESTIT_INDEPANDANT_FERMETURE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_INDEPANDANT_FERMETURE);
            RESTIT_DIRIGEANT_SALARIE_FERMETURE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_DIRIGEANT_SALARIE_FERMETURE);
            RESTIT_INDEPENDANT_MANIF_ANNULEE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_INDEPENDANT_MANIF_ANNULEE);
            RESTIT_DIRIGEANT_SALARIE_MANIF_ANNULEE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_DIRIGEANT_SALARIE_MANIF_ANNULEE);
            RESTIT_INDEPENDANT_LIMITATION_ACTIVITE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_INDEPENDANT_LIMITATION_ACTIVITE);
            RESTIT_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE);

            RESTIT_QUARANTAINE_17_09_20_INDEPENDANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_QUARANTAINE_17_09_20_INDEPENDANT);
            RESTIT_QUARANTAINE_17_09_20_SALARIE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_QUARANTAINE_17_09_20_SALARIE);
            RESTIT_GARDE_PARENTALE_17_09_20_INDEPENDANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_GARDE_PARENTALE_17_09_20_INDEPENDANT);
            RESTIT_GARDE_PARENTALE_17_09_20_SALARIE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_GARDE_PARENTALE_17_09_20_SALARIE);
            RESTIT_GARDE_PARENTALE_HANDICAP_17_09_20_INDEPENDANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_GARDE_PARENTALE_HANDICAP_17_09_20_INDEPENDANT);
            RESTIT_GARDE_PARENTALE_HANDICAP_17_09_20_SALARIE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_GARDE_PARENTALE_HANDICAP_17_09_20_SALARIE);

            RESTIT_PERSONNE_VULNERABLE_SALARIE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_PERSONNE_VULNERABLE_SALARIE);
            RESTIT_PERSONNE_VULNERABLE_INDEPENDANT= referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_RESTIT_PERSONNE_VULNERABLE_INDEPENDANT);

            COT_AVS_ASSURE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_COT_AVS_ASSURE);
            COT_AVS_INDEPENDANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_COT_AVS_INDEPENDANT);
            COT_AC_ASSURE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_COT_AC_SALARIE);
            COT_AC_INDEPENDANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_COT_AC_INDEPENDANT);

            FONDS_DE_COMPENSATION_ASSURE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_FONDS_COMPENSATIONS_ASSURE);
            FONDS_DE_COMPENSATION_EMPLOYEUR = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_FONDS_COMPENSATIONS_EMPLOYEUR);
            FONDS_DE_COMPENSATION_INDEPENDANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_FONDS_COMPENSATIONS_INDEPENDANT);

            IMPOT_SOURCE_ASSURE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_IMPOTS_A_LA_SOURCE);
            IMPOT_SOURCE_INDEPENDANT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_IMPOTS_A_LA_SOURCE);

            COT_LFA = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_COTISATIONS_LFA);
            FRAIS_ADMINISTRATION = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_FRAIS_ADMINISTRATION);
            COMPENSATION = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PANDEMIE_COMPENSATIONS);

        } else {
            AVEC_AC_EMPLOYEUR = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_EMPLOYEUR_AVEC_AC);
            SANS_AC_EMPLOYEUR = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_EMPLOYEUR_SANS_AC);

            AVEC_AC_ASSURE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.APG_AVEC_AC_ASSURE);
            AVEC_AC_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_AVEC_AC_INDEPENDANT);

            SANS_AC_ASSURE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.APG_SANS_AC_ASSURE);
            SANS_AC_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_SANS_AC_INDEPENDANT);

            SANS_COTISATION = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.APG_SANS_COTISATIONS);

            PRESTATION_A_RESTITUER_ASSURE = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_PRESTATION_A_RESTITUER_ASSURE);
            PRESTATION_A_RESTITUER_EMPLOYEUR = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_PRESTATION_A_RESTITUER_EMPLOYEUR);
            PRESTATION_A_RESTITUER_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_PRESTATION_A_RESTITUER_INDEPENDANT);

            COT_AVS_ASSURE = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COTISATION_AVS_ASSURE);
            COT_AVS_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COTISATION_AVS_INDEPENDANT);

            COT_AC_ASSURE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.APG_COTISATION_AC_ASSURE);
            COT_AC_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COTISATION_AC_INDEPENDANT);

            FONDS_DE_COMPENSATION_ASSURE = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_FONDS_DE_COMPENSATION_ASSURE);
            FONDS_DE_COMPENSATION_EMPLOYEUR = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_FONDS_DE_COMPENSATION_EMPLOYEUR);
            FONDS_DE_COMPENSATION_INDEPENDANT = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_FONDS_DE_COMPENSATION_INDEPENDANT);

            FONDS_DE_COMPENSATION_ACM = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_FONDS_DE_COMPENSATION);

            IMPOT_SOURCE_ASSURE = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_IMPOT_A_LA_SOURCE);

            IMPOT_SOURCE_ACM = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_IMPOT_A_LA_SOURCE_ACM);
            COT_LFA = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.APG_COTISATION_LFA);
            FRAIS_ADMINISTRATION = referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_FRAIS_ADMINISTRATION);
            COMPENSATION = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.COMPENSATION_APG_MAT);
            COMPENSATION_ACM = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.COMPENSATION_ALFA);
            ACM_MONTANT_BRUT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_MONTANT_BRUT);
            ACM_RESTITUTION = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_RESTITUTION);

            final AcmNeBean acmNeMecpBean = new AcmNeBean();
            acmNeMecpBean.setRubriqueDeBase(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_NE_MECP));
            acmNeMecpBean.setRubriqueCompensation(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_NE_COMPENSATION_MECP));
            acmNeMecpBean.setRubriqueRestitution(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_NE_RESTITUTION_MECP));
            acmNeMecpBean.setRubriqueImpotSource(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_NE_IMPOT_SOURCE_MECP));
            acmNeMecpBean.setRubriqueFondCompensation(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_NE_FOND_COMPENSATION_MECP));

            mapAcmNeBean.put(APAssuranceTypeAssociation.MECP.getCodesystemToString(), acmNeMecpBean);

            final AcmNeBean acmNePpBean = new AcmNeBean();
            acmNePpBean.setRubriqueDeBase(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_NE_PP));
            acmNePpBean.setRubriqueCompensation(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_NE_COMPENSATION_PP));
            acmNePpBean.setRubriqueRestitution(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_NE_RESTITUTION_PP));
            acmNePpBean.setRubriqueImpotSource(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_NE_IMPOT_SOURCE_PP));
            acmNePpBean.setRubriqueFondCompensation(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_NE_FOND_COMPENSATION_PP));

            mapAcmNeBean.put(APAssuranceTypeAssociation.PP.getCodesystemToString(), acmNePpBean);

            final AcmNeFneBean acmNeFneBean = new AcmNeFneBean();
            acmNeFneBean.setRubriqueDeBase(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_NE_FNE));
            acmNeFneBean.setRubriqueCompensation(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_NE_COMPENSATION_FNE));
            acmNeFneBean.setRubriqueRestitution(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_NE_RESTITUTION_FNE));
            acmNeFneBean.setRubriqueImpotSource(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_NE_IMPOT_SOURCE_FNE));
            acmNeFneBean.setRubriqueFondCompensation(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_NE_FOND_COMPENSATION_FNE));
            acmNeFneBean.setRubriqueCotisation(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_ACM_NE_COTISATION_FNE));

            mapAcmNeBean.put(APAssuranceTypeAssociation.FNE.getCodesystemToString(), acmNeFneBean);

            final ComplementBean complementJUBean = new ComplementBean();
            complementJUBean.setRubriqueParitaireParticipationCotisation(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_JU_PARITAIRE_PARTICIPATION));
            complementJUBean.setRubriqueParitaireMontantBrut(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_JU_PARITAIRE_MONTANT_BRUT));
            complementJUBean.setRubriqueParitaireRestitution(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_JU_PARITAIRE_MONTANT_RESTITUTION));
            complementJUBean.setRubriquePersonnelParticipationCotisation(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_JU_PERSONNEL_PARTICIPATION));
            complementJUBean.setRubriquePersonnelMontantBrut(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_JU_PERSONNEL_MONTANT_BRUT));
            complementJUBean.setRubriquePersonnelRestitution(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_JU_PERSONNEL_MONTANT_RESTITUTION));
            complementJUBean.setRubriquePersonnelCotisationAC(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_JU_PERSONNEL_COTISATIONS_AC));
            complementJUBean.setRubriquePersonnelCotisationAVS(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_JU_PERSONNEL_COTISATIONS_AVS));
            complementJUBean.setRubriqueParitaireImpotSource(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_JU_PARITAIRE_IMPOT_SOURCE));
            complementJUBean.setRubriquePersonnelImpotSource(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_JU_PERSONNEL_IMPOT_SOURCE));

            mapComplementBean.put(ECanton.JU.getValue(), complementJUBean);

            final ComplementBean complementBEBean = new ComplementBean();
            complementBEBean.setRubriqueParitaireParticipationCotisation(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_BE_PARITAIRE_PARTICIPATION));
            complementBEBean.setRubriqueParitaireMontantBrut(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_BE_PARITAIRE_MONTANT_BRUT));
            complementBEBean.setRubriqueParitaireRestitution(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_BE_PARITAIRE_MONTANT_RESTITUTION));
            complementBEBean.setRubriquePersonnelParticipationCotisation(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_BE_PERSONNEL_PARTICIPATION));
            complementBEBean.setRubriquePersonnelMontantBrut(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_BE_PERSONNEL_MONTANT_BRUT));
            complementBEBean.setRubriquePersonnelRestitution(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_BE_PERSONNEL_MONTANT_RESTITUTION));
            complementBEBean.setRubriquePersonnelCotisationAC(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_BE_PERSONNEL_COTISATIONS_AC));
            complementBEBean.setRubriquePersonnelCotisationAVS(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_BE_PERSONNEL_COTISATIONS_AVS));
            complementBEBean.setRubriqueParitaireImpotSource(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_BE_PARITAIRE_IMPOT_SOURCE));
            complementBEBean.setRubriquePersonnelImpotSource(referenceRubrique
                    .getRubriqueByCodeReference(APIReferenceRubrique.APG_COMPCIAB_BE_PERSONNEL_IMPOT_SOURCE));

            mapComplementBean.put(ECanton.BE.getValue(), complementBEBean);
        }
    }

    private ECanton getCanton(TypeComplement type) {
        if (TypeComplement.JU_PARITAIRE.equals(type)
                || TypeComplement.JU_PERSONNEL.equals(type)) {
            return ECanton.JU;
        } else if (TypeComplement.BE_PARITAIRE.equals(type)
                || TypeComplement.BE_PERSONNEL.equals(type)) {
            return ECanton.BE;
        }
        return null;
    }

    private JadeThreadContext initThreadContext(BSession session) throws Exception {

        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);

        return context;
    }

    /**
     * (non-Javadoc)
     *
     * @return DOCUMENT ME!
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    private String rechercheIdCaisseProf(String idAffilie, String assuranceId) throws Exception {

        String sqlQueryCaisseProf = getSqlCaisseProf(idAffilie, assuranceId);

        List<Map<String, String>> listMapResultQueryCaisseProf = executeQuery(sqlQueryCaisseProf);

        // La requête ordre les cotis par date de début desc
        // c'est donc uniquement la première ligne qu'il faut prendre (la coti la prlus récente)
        Map<String, String> aMapRowResultQueryCaisseProf = listMapResultQueryCaisseProf.get(0);

        return aMapRowResultQueryCaisseProf
                .get(APGenererEcrituresComptablesProcess.REQUETE_SQL_CAISSE_PROF_COL_NAME_ID_TIERS_ADMINISTRATION);

    }

    private String replaceSchemaInSqlQuery(String sqlQuery) {
        return sqlQuery.replaceAll("(?i)schema\\.", schemaDBWithTablePrefix);
    }

    /**
     * setter pour l'attribut date comptable
     *
     * @param string une nouvelle valeur pour cet attribut
     */
    public void setDateComptable(final String string) {
        dateComptable = string;
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    /**
     * @param string
     */
    public void setDateSurDocument(final String string) {
        dateSurDocument = string;
    }

    /**
     * setter pour l'attribut no lot
     *
     * @param string une nouvelle valeur pour cet attribut
     */
    public void setIdLot(final String string) {
        idLot = string;
    }
}
