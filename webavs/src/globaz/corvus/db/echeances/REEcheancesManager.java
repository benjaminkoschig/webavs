package globaz.corvus.db.echeances;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAVector;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFEnfant;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFPeriode;
import globaz.hera.db.famille.SFRelationConjoint;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.api.ITIPersonne;
import globaz.pyxis.db.location.ITIAvoirGroupeLocalitesDefTable;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IREEnfantEcheances;
import ch.globaz.corvus.business.models.echeances.IREPeriodeEcheances;
import ch.globaz.corvus.business.models.echeances.IRERelationEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.process.echeances.analyseur.REAnalyseurEcheances;
import ch.globaz.corvus.process.echeances.analyseur.REAnalyseurEcheancesFactory;
import ch.globaz.hera.business.constantes.ISFPeriode;
import ch.globaz.hera.business.constantes.ISFRelationConjoint;

/**
 * Recherche et regroupe toutes les rentes actives dans le mois de traitement.<br/>
 * Pour chaque rente, les données suivantes seront recherchées :
 * <ul>
 * <li>Information sur le tiers bénéficiaires</li>
 * <li>Relations conjugales du tiers bénéficiaire</li>
 * <li>Informations sur le/les conjoint(s) du tiers</li>
 * <li>les périodes d'études ou de certificat de vie du tiers</li>
 * <li>les enfants du tiers ayant une rente accordée en cours dans le mois de traitement</li>
 * <li>Est-ce qu'un tiers féminin, au bénéfice d'une rente survivant, a une demande de vieillesse terminée avec comme
 * type d'information complémentaire "Rente de veuve perdure"</li>
 * </ul>
 * Ces informations seront chargées dans des {@link REEcheancesEntity} et ensuite traitées par les différents analyseurs
 * d'échéances.
 * 
 * @author PBA
 * @see REAnalyseurEcheances
 * @see REAnalyseurEcheancesFactory
 */
public class REEcheancesManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_A_UNE_RENTE_API = "aUneRenteAPI";
    public static final String ALIAS_BENEFICIAIRE_MEMBRE_FAMILLE = "benef_mbrfam";
    public static final String ALIAS_BENEFICIAIRE_PERSONNE = "benef_pers";
    public static final String ALIAS_BENEFICIAIRE_PERSONNE_CS_SEXE = "benef_pers_cs_sexe";
    public static final String ALIAS_BENEFICIAIRE_PERSONNE_DATE_DECES = "benef_pers_date_deces";
    public static final String ALIAS_BENEFICIAIRE_PERSONNE_DATE_NAISSANCE = "benef_pers_date_naissance";
    public static final String ALIAS_BENEFICIAIRE_TIERS = "benef_tiers";
    public static final String ALIAS_BENEFICIAIRE_TIERS_ID_TIERS = "benef_tiers_id_tiers";
    public static final String ALIAS_BENEFICIAIRE_TIERS_NOM = "benef_tiers_nom";
    public static final String ALIAS_BENEFICIAIRE_TIERS_PRENOM = "benef_tiers_prenom";
    public static final String ALIAS_CONJOINT_MEMBRE_FAMILLE = "conjoint_mbrfam";
    public static final String ALIAS_CONJOINT_MEMBRE_FAMILLE_DATE_DECES = "conjoint_mbrfam_date_deces";
    public static final String ALIAS_CONJOINT_MEMBRE_FAMILLE_DATE_NAISSANCE = "conjoint_mbrfam_date_naiss";
    public static final String ALIAS_CONJOINT_PERSONNE = "conjoint_pers";
    public static final String ALIAS_CONJOINT_PERSONNE_CS_SEXE = "conjoint_pers_cs_sexe";
    public static final String ALIAS_CONJOINT_PERSONNE_DATE_DECES = "conjoint_pers_date_deces";
    public static final String ALIAS_CONJOINT_PERSONNE_DATE_NAISSANCE = "conjoint_pers_date_naissance";
    public static final String ALIAS_CONJOINT_TIERS = "conjoint_tiers";
    public static final String ALIAS_CONJOINT_TIERS_ID_PRENOM = "conjoint_tiers_prenom";
    public static final String ALIAS_CONJOINT_TIERS_ID_TIERS = "conjoint_tiers_id_tiers";
    public static final String ALIAS_CONJOINT_TIERS_NOM = "conjoint_tiers_nom";
    public static final String ALIAS_ENFANT_MEMBRE_FAMILLE = "enfant_mbrfam";
    public static final String ALIAS_ENFANT_MEMBRE_FAMILLE_DATE_DECES = "enfant_mbrf_date_deces";
    public static final String ALIAS_ENFANT_MEMBRE_FAMILLE_DATE_NAISSANCE = "enfant_mbrf_date_naissance";
    public static final String ALIAS_ENFANT_MEMBRE_FAMILLE_ID_MEMBRE_FAMILLE = "enfant_mbrf_id_mbrf";
    public static final String ALIAS_ENFANT_MEMBRE_FAMILLE_NOM = "enfant_mbrf_nom";
    public static final String ALIAS_ENFANT_MEMBRE_FAMILLE_PRENOM = "enfant_mbrf_prenom";
    public static final String ALIAS_ENFANT_PERSONNE = "enfant_pers";
    public static final String ALIAS_ENFANT_PERSONNE_DATE_DECES = "enfant_pers_date_deces";
    public static final String ALIAS_ENFANT_PERSONNE_DATE_NAISSANCE = "enfant_pers_date_naissance";
    public static final String ALIAS_ENFANT_TIERS = "enfant_tiers";
    public static final String ALIAS_ENFANT_TIERS_ID_TIERS = "enfant_tiers_id_tiers";
    public static final String ALIAS_ENFANT_TIERS_NOM = "enfant_tiers_nom";
    public static final String ALIAS_ENFANT_TIERS_PRENOM = "enfant_tiers_prenom";
    public static final String ALIAS_PRESTATION_BENEFICIAIRE = "prst_benef";
    public static final String ALIAS_PRESTATION_BENEFICIAIRE_CODE_PRESTATION = "prst_benef_code_prst";
    public static final String ALIAS_PRESTATION_BENEFICIAIRE_CS_ETAT = "prst_benef_cs_etat";
    public static final String ALIAS_PRESTATION_BENEFICIAIRE_DATE_DEBUT = "prst_benef_date_debut";
    public static final String ALIAS_PRESTATION_BENEFICIAIRE_DATE_ECHEANCE = "prst_benef_date_echeance";
    public static final String ALIAS_PRESTATION_BENEFICIAIRE_DATE_FIN = "prst_benef_date_fin";
    public static final String ALIAS_PRESTATION_BENEFICIAIRE_ID_PRESTATION_ACCORDEE = "prst_benef_id_prst_accordee";
    public static final String ALIAS_PRESTATION_BENEFICIAIRE_IS_PRESTATION_BLOQUEE = "prst_benef_is_bloquee";
    public static final String ALIAS_PRESTATION_CONJOINT = "prst_conj";
    public static final String ALIAS_PRESTATION_CONJOINT_CODE_PRESTATION = "prst_conj_code_prst";
    public static final String ALIAS_PRESTATION_CONJOINT_CS_ETAT = "prst_conj_cs_etat";
    public static final String ALIAS_PRESTATION_CONJOINT_DATE_DEBUT = "prst_conj_date_debut";
    public static final String ALIAS_PRESTATION_CONJOINT_DATE_FIN = "prst_conj_date_fin";
    public static final String ALIAS_PRESTATION_CONJOINT_ID_PRESTATION_ACCORDEE = "prst_conj_id_prst_accordee";
    public static final String ALIAS_PRESTATION_ENFANT = "prst_enf";
    public static final String ALIAS_PRESTATION_ENFANT_CODE_PRESTATION = "prst_enf_code_prst";
    public static final String ALIAS_PRESTATION_ENFANT_CS_ETAT = "prst_enf_cs_etat";
    public static final String ALIAS_PRESTATION_ENFANT_DATE_DEBUT = "prst_enf_date_debut";
    public static final String ALIAS_PRESTATION_ENFANT_DATE_FIN = "prst_enf_date_fin";
    public static final String ALIAS_PRESTATION_ENFANT_ID_PRESTATION_ACCORDEE = "prst_enf_id_prst_accordee";
    public static final String ALIAS_PRESTATION_ENFANT_IS_BLOQUEE = "prst_enf_is_bloquee";

    private String forDateTraitement;
    private String forIdGroupeLocalite;

    public REEcheancesManager(BSession session, String forDateTraitement) {
        this(session, forDateTraitement, "");
    }

    public REEcheancesManager(BSession session, String forDateTraitement, String forIdGroupeLocalite) {
        super();

        if (session == null) {
            throw new NullPointerException("Session null");
        }
        setSession(session);

        this.forDateTraitement = forDateTraitement;
        this.forIdGroupeLocalite = forIdGroupeLocalite;

        wantCallMethodAfterFind(true);
    }

    /**
     * Regroupe les échéances, trouvées par la requête SQL, par tiers
     */
    @Override
    protected void _afterFind(BTransaction transaction) throws Exception {

        Map<String, REEcheancesEntity> echeancesParTiers = new HashMap<String, REEcheancesEntity>();

        // regroupement des échéances par ID tiers
        for (int i = 0; i < getSize(); i++) {
            REEcheancesEntity uneEcheanceDeLaRequete = (REEcheancesEntity) get(i);

            // si c'est une rente survivant, et que le tiers est une femme,
            // on vérifie si c'est une rente de veuve perdure
            if (ITIPersonne.CS_FEMME.equals(uneEcheanceDeLaRequete.getCsSexeTiers())
                    && hasRenteSurvivant(uneEcheanceDeLaRequete)) {
                checkRenteVeuvePerdure(uneEcheanceDeLaRequete);
            }

            if (echeancesParTiers.containsKey(uneEcheanceDeLaRequete.getIdTiers())) {
                IREEcheances echeancePourUnTiers = echeancesParTiers.get(uneEcheanceDeLaRequete.getIdTiers());

                for (IRERenteEcheances uneRenteDuTiers : uneEcheanceDeLaRequete.getRentesDuTiers()) {
                    echeancePourUnTiers.getRentesDuTiers().add(uneRenteDuTiers);
                }
                for (IREPeriodeEcheances unePeriode : uneEcheanceDeLaRequete.getPeriodes()) {
                    echeancePourUnTiers.getPeriodes().add(unePeriode);
                }
                for (IREEnfantEcheances unEnfant : uneEcheanceDeLaRequete.getEnfantsDuTiers()) {
                    if (echeancePourUnTiers.getEnfantsDuTiers().contains(unEnfant)) {
                        if (unEnfant.getRentes().size() != 0) {
                            for (IREEnfantEcheances enfant : echeancePourUnTiers.getEnfantsDuTiers()) {
                                if (enfant.equals(unEnfant)) {
                                    enfant.getRentes().addAll(unEnfant.getRentes());
                                    break;
                                }
                            }
                        }
                    } else {
                        echeancePourUnTiers.getEnfantsDuTiers().add(unEnfant);
                    }
                }
                for (IRERelationEcheances uneRelation : uneEcheanceDeLaRequete.getRelations()) {
                    if (echeancePourUnTiers.getRelations().contains(uneRelation)) {
                        if (uneRelation.getRentesDuConjoint().size() > 0) {
                            for (IRERelationEcheances relationDejaSauvee : echeancePourUnTiers.getRelations()) {
                                if (relationDejaSauvee.equals(uneRelation)
                                        && !relationDejaSauvee.getRentesDuConjoint().containsAll(
                                                uneRelation.getRentesDuConjoint())) {
                                    relationDejaSauvee.getRentesDuConjoint().addAll(uneRelation.getRentesDuConjoint());
                                }
                            }
                        }
                    } else {
                        echeancePourUnTiers.getRelations().add(uneRelation);
                    }
                }
            } else {
                echeancesParTiers.put(uneEcheanceDeLaRequete.getIdTiers(), uneEcheanceDeLaRequete);
            }
        }

        JAVector newContainer = new JAVector();
        // on re-trie les entités, comme le fait l'order by de la requête, grâce à l'utilisation d'un liste triée
        // (REEcheance étend Comparable)
        newContainer.addAll(new TreeSet<REEcheancesEntity>(echeancesParTiers.values()));
        setContainer(newContainer);
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        sql.append("DISTINCT ");

        sql.append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE_ID_PRESTATION_ACCORDEE).append(",");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE_CODE_PRESTATION).append(",");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE_CS_ETAT).append(",");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_ECHEANCE).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE_DATE_ECHEANCE).append(",");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE_DATE_DEBUT).append(",");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE_DATE_FIN).append(",");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE_IS_PRESTATION_BLOQUEE).append(",");

        sql.append(RERenteAccordee.FIELDNAME_DATE_REVOCATION_AJOURNEMENT).append(",");
        sql.append(RERenteAccordee.FIELDNAME_ANNEE_ANTICIPATION).append(",");
        sql.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1).append(",");
        sql.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2).append(",");
        sql.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3).append(",");
        sql.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4).append(",");
        sql.append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5).append(",");

        sql.append(RERenteAccordee.FIELDNAME_CS_GENRE_DROIT_API).append(",");

        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_TIERS).append(".").append(ITITiersDefTable.DESIGNATION_1)
                .append(" AS ").append(REEcheancesManager.ALIAS_BENEFICIAIRE_TIERS_NOM).append(",");
        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_TIERS).append(".").append(ITITiersDefTable.DESIGNATION_2)
                .append(" AS ").append(REEcheancesManager.ALIAS_BENEFICIAIRE_TIERS_PRENOM).append(",");
        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_TIERS).append(".").append(ITITiersDefTable.ID_TIERS)
                .append(" AS ").append(REEcheancesManager.ALIAS_BENEFICIAIRE_TIERS_ID_TIERS).append(",");

        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_PERSONNE).append(".")
                .append(ITIPersonneDefTable.DATE_NAISSANCE).append(" AS ")
                .append(REEcheancesManager.ALIAS_BENEFICIAIRE_PERSONNE_DATE_NAISSANCE).append(",");
        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_PERSONNE).append(".").append(ITIPersonneDefTable.DATE_DECES)
                .append(" AS ").append(REEcheancesManager.ALIAS_BENEFICIAIRE_PERSONNE_DATE_DECES).append(",");
        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_PERSONNE).append(".").append(ITIPersonneDefTable.CS_SEXE)
                .append(" AS ").append(REEcheancesManager.ALIAS_BENEFICIAIRE_PERSONNE_CS_SEXE).append(",");

        sql.append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(",");

        sql.append(SFPeriode.FIELD_IDPERIODE).append(",");
        sql.append(SFPeriode.FIELD_TYPE).append(",");
        sql.append(SFPeriode.FIELD_DATEDEBUT).append(",");
        sql.append(SFPeriode.FIELD_DATEFIN).append(",");

        sql.append(SFRelationConjoint.FIELD_IDRELATIONCONJOINT).append(",");
        sql.append(SFRelationConjoint.FIELD_DATEDEBUT).append(",");
        sql.append(SFRelationConjoint.FIELD_DATEFIN).append(",");
        sql.append(SFRelationConjoint.FIELD_TYPERELATION).append(",");

        sql.append(REEcheancesManager.ALIAS_CONJOINT_TIERS).append(".").append(ITITiersDefTable.ID_TIERS)
                .append(" AS ").append(REEcheancesManager.ALIAS_CONJOINT_TIERS_ID_TIERS).append(",");
        sql.append(REEcheancesManager.ALIAS_CONJOINT_TIERS).append(".").append(ITITiersDefTable.DESIGNATION_1)
                .append(" AS ").append(REEcheancesManager.ALIAS_CONJOINT_TIERS_NOM).append(",");
        sql.append(REEcheancesManager.ALIAS_CONJOINT_TIERS).append(".").append(ITITiersDefTable.DESIGNATION_2)
                .append(" AS ").append(REEcheancesManager.ALIAS_CONJOINT_TIERS_ID_PRENOM).append(",");

        sql.append(REEcheancesManager.ALIAS_CONJOINT_PERSONNE).append(".").append(ITIPersonneDefTable.CS_SEXE)
                .append(" AS ").append(REEcheancesManager.ALIAS_CONJOINT_PERSONNE_CS_SEXE).append(",");
        sql.append(REEcheancesManager.ALIAS_CONJOINT_PERSONNE).append(".").append(ITIPersonneDefTable.DATE_NAISSANCE)
                .append(" AS ").append(REEcheancesManager.ALIAS_CONJOINT_PERSONNE_DATE_NAISSANCE).append(",");
        sql.append(REEcheancesManager.ALIAS_CONJOINT_PERSONNE).append(".").append(ITIPersonneDefTable.DATE_DECES)
                .append(" AS ").append(REEcheancesManager.ALIAS_CONJOINT_PERSONNE_DATE_DECES).append(",");

        sql.append(REEcheancesManager.ALIAS_CONJOINT_MEMBRE_FAMILLE).append(".")
                .append(SFMembreFamille.FIELD_DATENAISSANCE).append(" AS ")
                .append(REEcheancesManager.ALIAS_CONJOINT_MEMBRE_FAMILLE_DATE_NAISSANCE).append(",");
        sql.append(REEcheancesManager.ALIAS_CONJOINT_MEMBRE_FAMILLE).append(".")
                .append(SFMembreFamille.FIELD_DATEDECES).append(" AS ")
                .append(REEcheancesManager.ALIAS_CONJOINT_MEMBRE_FAMILLE_DATE_DECES).append(",");

        sql.append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT_ID_PRESTATION_ACCORDEE).append(",");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT_CS_ETAT).append(",");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT_CODE_PRESTATION).append(",");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT_DATE_DEBUT).append(",");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT_DATE_FIN).append(",");

        sql.append(REDemandeRente.FIELDNAME_CS_ETAT).append(",");

        sql.append(PRInfoCompl.FIELDNAME_TYPE_INFO_COMPL).append(",");

        sql.append(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE).append(".")
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE).append(" AS ")
                .append(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE_ID_MEMBRE_FAMILLE).append(",");
        sql.append(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE).append(".").append(SFMembreFamille.FIELD_NOM)
                .append(" AS ").append(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE_NOM).append(",");
        sql.append(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE).append(".").append(SFMembreFamille.FIELD_PRENOM)
                .append(" AS ").append(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE_PRENOM).append(",");
        sql.append(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE).append(".")
                .append(SFMembreFamille.FIELD_DATENAISSANCE).append(" AS ")
                .append(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE_DATE_NAISSANCE).append(",");
        sql.append(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE).append(".").append(SFMembreFamille.FIELD_DATEDECES)
                .append(" AS ").append(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE_DATE_DECES).append(",");

        sql.append(REEcheancesManager.ALIAS_ENFANT_TIERS).append(".").append(ITITiersDefTable.ID_TIERS).append(" AS ")
                .append(REEcheancesManager.ALIAS_ENFANT_TIERS_ID_TIERS).append(",");
        sql.append(REEcheancesManager.ALIAS_ENFANT_TIERS).append(".").append(ITITiersDefTable.DESIGNATION_1)
                .append(" AS ").append(REEcheancesManager.ALIAS_ENFANT_TIERS_NOM).append(",");
        sql.append(REEcheancesManager.ALIAS_ENFANT_TIERS).append(".").append(ITITiersDefTable.DESIGNATION_2)
                .append(" AS ").append(REEcheancesManager.ALIAS_ENFANT_TIERS_PRENOM).append(",");

        sql.append(REEcheancesManager.ALIAS_ENFANT_PERSONNE).append(".").append(ITIPersonneDefTable.DATE_DECES)
                .append(" AS ").append(REEcheancesManager.ALIAS_ENFANT_PERSONNE_DATE_DECES).append(",");
        sql.append(REEcheancesManager.ALIAS_ENFANT_PERSONNE).append(".").append(ITIPersonneDefTable.DATE_NAISSANCE)
                .append(" AS ").append(REEcheancesManager.ALIAS_ENFANT_PERSONNE_DATE_NAISSANCE).append(",");

        sql.append(REEcheancesManager.ALIAS_PRESTATION_ENFANT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_ENFANT_ID_PRESTATION_ACCORDEE).append(",");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_ENFANT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_ENFANT_CS_ETAT).append(",");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_ENFANT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_ENFANT_IS_BLOQUEE).append(",");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_ENFANT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_ENFANT_CODE_PRESTATION).append(",");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_ENFANT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_ENFANT_DATE_DEBUT).append(",");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_ENFANT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_ENFANT_DATE_FIN);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE);

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        sql.append(" ON ");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(".")
                .append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        sql.append("=");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(" AS ")
                .append(REEcheancesManager.ALIAS_BENEFICIAIRE_TIERS);
        sql.append(" ON ");
        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_TIERS).append(".").append(ITITiersDefTable.ID_TIERS);
        sql.append("=");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append(ITIPersonneDefTable.TABLE_NAME).append(" AS ")
                .append(REEcheancesManager.ALIAS_BENEFICIAIRE_PERSONNE);
        sql.append(" ON ");
        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_PERSONNE).append(".").append(ITITiersDefTable.ID_TIERS);
        sql.append("=");
        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_TIERS).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append(ITIPersonneAvsDefTable.TABLE_NAME);
        sql.append(" ON ");
        sql.append(_getCollection()).append(ITIPersonneAvsDefTable.TABLE_NAME).append(".")
                .append(ITITiersDefTable.ID_TIERS);
        sql.append("=");
        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_TIERS).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        sql.append(" ON ");
        sql.append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL).append(".")
                .append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
        sql.append("=");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(".")
                .append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        sql.append(" ON ");
        sql.append(_getCollection()).append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE).append(".")
                .append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);
        sql.append("=");
        sql.append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL).append(".")
                .append(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE);

        sql.append(" LEFT OUTER JOIN ");
        sql.append(_getCollection()).append(REDemandeRenteAPI.TABLE_NAME_DEMANDE_RENTE_API);
        sql.append(" ON ");
        sql.append(_getCollection()).append(REDemandeRenteAPI.TABLE_NAME_DEMANDE_RENTE_API).append(".")
                .append(REDemandeRenteAPI.FIELDNAME_ID_DEMANDE_RENTE_API);
        sql.append("=");
        sql.append(_getCollection()).append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE).append(".")
                .append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);

        sql.append(" LEFT OUTER JOIN ");
        sql.append(_getCollection()).append(PRInfoCompl.TABLE_NAME);
        sql.append(" ON ");
        sql.append(_getCollection()).append(PRInfoCompl.TABLE_NAME).append(".")
                .append(PRInfoCompl.FIELDNAME_ID_INFO_COMPL);
        sql.append("=");
        sql.append(_getCollection()).append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE).append(".")
                .append(REDemandeRente.FIELDNAME_ID_INFO_COMPLEMENTAIRE);

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append(SFMembreFamille.TABLE_NAME).append(" AS ")
                .append(REEcheancesManager.ALIAS_BENEFICIAIRE_MEMBRE_FAMILLE);
        sql.append(" ON ");
        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_MEMBRE_FAMILLE).append(".")
                .append(SFMembreFamille.FIELD_IDTIERS);
        sql.append("=");
        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_TIERS).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" LEFT OUTER JOIN ");
        sql.append(_getCollection()).append(SFPeriode.TABLE_NAME);
        sql.append(" ON (");
        sql.append(_getCollection()).append(SFPeriode.TABLE_NAME).append(".").append(SFPeriode.FIELD_IDMEMBREFAMILLE);
        sql.append("=");
        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_MEMBRE_FAMILLE).append(".")
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);

        sql.append(" AND ");
        // BZ 5195, ajout du code système pour le certificat de vie
        sql.append(_getCollection()).append(SFPeriode.TABLE_NAME).append(".").append(SFPeriode.FIELD_TYPE);
        sql.append(" IN (");
        sql.append(ISFPeriode.CS_TYPE_PERIODE_ETUDE).append(",");
        sql.append(ISFPeriode.CS_TYPE_PERIODE_CERTIFICAT_DE_VIE);
        sql.append("))");

        sql.append(" LEFT OUTER JOIN ");
        sql.append(_getCollection()).append(SFConjoint.TABLE_NAME);
        sql.append(" ON (");
        sql.append(_getCollection()).append(SFConjoint.TABLE_NAME).append(".").append(SFConjoint.FIELD_IDCONJOINT1);
        sql.append("=");
        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_MEMBRE_FAMILLE).append(".")
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);

        sql.append(" OR ");
        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_MEMBRE_FAMILLE).append(".")
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        sql.append("=");
        sql.append(_getCollection()).append(SFConjoint.TABLE_NAME).append(".").append(SFConjoint.FIELD_IDCONJOINT2);
        sql.append(")");

        String dateMariage;
        try {
            JACalendarGregorian jaCalGre = new JACalendarGregorian();

            JADate dateTraitement = new JADate(getForDateTraitement());

            dateMariage = JACalendar.format(
                    jaCalGre.addDays(
                            jaCalGre.addMonths(
                                    new JADate("01."
                                            + (Integer.toString(dateTraitement.getMonth()).length() < 2 ? "0"
                                                    + Integer.toString(dateTraitement.getMonth()) + "."
                                                    + Integer.toString(dateTraitement.getYear()) : Integer
                                                    .toString(dateTraitement.getMonth())
                                                    + "."
                                                    + Integer.toString(dateTraitement.getYear()))), 1), -1),
                    JACalendar.FORMAT_YYYYMMDD);
        } catch (Exception ex) {
            dateMariage = getForDateTraitement();
        }

        sql.append(" LEFT OUTER JOIN ");
        sql.append(_getCollection()).append(SFRelationConjoint.TABLE_NAME);
        sql.append(" ON (");
        sql.append(_getCollection()).append(SFRelationConjoint.TABLE_NAME).append(".")
                .append(SFRelationConjoint.FIELD_IDCONJOINTS);
        sql.append("=");
        sql.append(_getCollection()).append(SFConjoint.TABLE_NAME).append(".").append(SFConjoint.FIELD_IDCONJOINTS);

        sql.append(" AND ");
        sql.append(_getCollection()).append(SFRelationConjoint.TABLE_NAME).append(".")
                .append(SFRelationConjoint.FIELD_TYPERELATION);
        sql.append(" IN(");
        sql.append(ISFRelationConjoint.CS_REL_CONJ_MARIE).append(",");
        sql.append(ISFRelationConjoint.CS_REL_CONJ_SEPARE_DE_FAIT);
        sql.append(")");

        sql.append(" AND ");
        sql.append(_getCollection()).append(SFRelationConjoint.TABLE_NAME).append(".")
                .append(SFRelationConjoint.FIELD_DATEDEBUT);
        sql.append("<=");
        sql.append(dateMariage);

        sql.append(" AND (");
        sql.append(_getCollection()).append(SFRelationConjoint.TABLE_NAME).append(".")
                .append(SFRelationConjoint.FIELD_DATEFIN).append(">").append(dateMariage);
        sql.append(" OR ");
        sql.append(_getCollection()).append(SFRelationConjoint.TABLE_NAME).append(".")
                .append(SFRelationConjoint.FIELD_DATEFIN).append(" is NULL ");
        sql.append(" OR ");
        sql.append(_getCollection()).append(SFRelationConjoint.TABLE_NAME).append(".")
                .append(SFRelationConjoint.FIELD_DATEFIN).append("=").append(0);
        sql.append("))");

        sql.append(" LEFT OUTER JOIN ");
        sql.append(_getCollection()).append(SFMembreFamille.TABLE_NAME).append(" AS ")
                .append(REEcheancesManager.ALIAS_CONJOINT_MEMBRE_FAMILLE);
        sql.append(" ON ((");
        sql.append(REEcheancesManager.ALIAS_CONJOINT_MEMBRE_FAMILLE).append(".")
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        sql.append("=");
        sql.append(_getCollection()).append(SFConjoint.TABLE_NAME).append(".").append(SFConjoint.FIELD_IDCONJOINT2);

        sql.append(" OR ");
        sql.append(REEcheancesManager.ALIAS_CONJOINT_MEMBRE_FAMILLE).append(".")
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        sql.append("=");
        sql.append(_getCollection()).append(SFConjoint.TABLE_NAME).append(".").append(SFConjoint.FIELD_IDCONJOINT1);
        sql.append(")");

        sql.append(" AND ");
        sql.append(REEcheancesManager.ALIAS_CONJOINT_MEMBRE_FAMILLE).append(".")
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        sql.append("<>");
        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_MEMBRE_FAMILLE).append(".")
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        sql.append(")");

        sql.append(" LEFT OUTER JOIN ");
        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(" AS ")
                .append(REEcheancesManager.ALIAS_CONJOINT_TIERS);
        sql.append(" ON ");
        sql.append(REEcheancesManager.ALIAS_CONJOINT_TIERS).append(".").append(ITITiersDefTable.ID_TIERS);
        sql.append("=");
        sql.append(REEcheancesManager.ALIAS_CONJOINT_MEMBRE_FAMILLE).append(".").append(SFMembreFamille.FIELD_IDTIERS);

        sql.append(" LEFT OUTER JOIN ");
        sql.append(_getCollection()).append(ITIPersonneDefTable.TABLE_NAME).append(" AS ")
                .append(REEcheancesManager.ALIAS_CONJOINT_PERSONNE);
        sql.append(" ON ");
        sql.append(REEcheancesManager.ALIAS_CONJOINT_PERSONNE).append(".").append(ITITiersDefTable.ID_TIERS);
        sql.append("=");
        sql.append(REEcheancesManager.ALIAS_CONJOINT_MEMBRE_FAMILLE).append(".").append(SFMembreFamille.FIELD_IDTIERS);

        sql.append(" LEFT OUTER JOIN ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT);
        sql.append(" ON (");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append("=");
        sql.append(REEcheancesManager.ALIAS_CONJOINT_PERSONNE).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" AND ");
        sql.append("((");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
        sql.append(">=");
        sql.append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDateTraitement()));

        sql.append(") OR (");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append("=0");
        sql.append(") OR (");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" IS NULL");
        sql.append(")) AND (");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
        sql.append("<=");
        sql.append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(JadeDateUtil.convertDateMonthYear(JadeDateUtil
                .addMonths("01." + getForDateTraitement(), 1))));
        sql.append(") AND ");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_CONJOINT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
        sql.append(" IN (");
        sql.append(IREPrestationAccordee.CS_ETAT_VALIDE).append(",");
        sql.append(IREPrestationAccordee.CS_ETAT_PARTIEL).append(",");
        sql.append(IREPrestationAccordee.CS_ETAT_AJOURNE);
        sql.append("))");

        if (!JadeStringUtil.isBlank(forIdGroupeLocalite)) {
            sql.append(" INNER JOIN ");
            sql.append(_getCollection()).append(ITIAvoirGroupeLocalitesDefTable.TABLE_NAME);
            sql.append(" ON (");
            sql.append(_getCollection()).append(ITIAvoirGroupeLocalitesDefTable.TABLE_NAME).append(".")
                    .append(ITIAvoirGroupeLocalitesDefTable.ID_TIERS);
            sql.append("=");
            sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);

            sql.append(" AND ");
            sql.append(_getCollection()).append(ITIAvoirGroupeLocalitesDefTable.TABLE_NAME).append(".")
                    .append(ITIAvoirGroupeLocalitesDefTable.ID_GROUPE_LOCALITES);
            sql.append("=");
            sql.append(forIdGroupeLocalite);
            sql.append(")");
        }

        sql.append(" LEFT OUTER JOIN ");
        sql.append(_getCollection()).append(SFEnfant.TABLE_NAME);
        sql.append(" ON ");
        sql.append(_getCollection()).append(SFEnfant.TABLE_NAME).append(".").append(SFEnfant.FIELD_IDCONJOINT);
        sql.append("=");
        sql.append(_getCollection()).append(SFConjoint.TABLE_NAME).append(".").append(SFConjoint.FIELD_IDCONJOINTS);

        sql.append(" LEFT OUTER JOIN ");
        sql.append(_getCollection()).append(SFMembreFamille.TABLE_NAME).append(" AS ")
                .append(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE);
        sql.append(" ON ");
        sql.append(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE).append(".")
                .append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        sql.append("=");
        sql.append(_getCollection()).append(SFEnfant.TABLE_NAME).append(".").append(SFEnfant.FIELD_IDMEMBREFAMILLE);

        sql.append(" LEFT OUTER JOIN ");
        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(" AS ")
                .append(REEcheancesManager.ALIAS_ENFANT_TIERS);
        sql.append(" ON ");
        sql.append(REEcheancesManager.ALIAS_ENFANT_TIERS).append(".").append(ITITiersDefTable.ID_TIERS);
        sql.append("=");
        sql.append(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE).append(".").append(SFMembreFamille.FIELD_IDTIERS);

        sql.append(" LEFT OUTER JOIN ");
        sql.append(_getCollection()).append(ITIPersonneDefTable.TABLE_NAME).append(" AS ")
                .append(REEcheancesManager.ALIAS_ENFANT_PERSONNE);
        sql.append(" ON ");
        sql.append(REEcheancesManager.ALIAS_ENFANT_PERSONNE).append(".").append(ITIPersonneDefTable.ID_TIERS);
        sql.append("=");
        sql.append(REEcheancesManager.ALIAS_ENFANT_TIERS).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" LEFT OUTER JOIN ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(" AS ")
                .append(REEcheancesManager.ALIAS_PRESTATION_ENFANT);
        sql.append(" ON (");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_ENFANT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append("=");
        sql.append(REEcheancesManager.ALIAS_ENFANT_TIERS).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" AND ");
        sql.append("((");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_ENFANT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
        sql.append(">=");
        sql.append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDateTraitement()));

        sql.append(") OR (");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_ENFANT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append("=0");
        sql.append(") OR (");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_ENFANT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" IS NULL");
        sql.append(")) AND ");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_ENFANT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
        sql.append(" IN (");
        sql.append(IREPrestationAccordee.CS_ETAT_VALIDE).append(",");
        sql.append(IREPrestationAccordee.CS_ETAT_PARTIEL).append(",");
        sql.append(IREPrestationAccordee.CS_ETAT_DIMINUE).append(",");
        sql.append(IREPrestationAccordee.CS_ETAT_AJOURNE);
        sql.append("))");

        return sql.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_TIERS).append(".").append(ITITiersDefTable.DESIGNATION_1)
                .append(",");
        sql.append(REEcheancesManager.ALIAS_BENEFICIAIRE_TIERS).append(".").append(ITITiersDefTable.DESIGNATION_2)
                .append(",");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        return sql.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        sql.append("((");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
        sql.append(">=");
        sql.append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDateTraitement()));

        sql.append(") OR (");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
        sql.append("=");
        sql.append("0");

        sql.append(") OR (");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
        sql.append(" IS NULL");

        sql.append(")) AND ");
        sql.append(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
        sql.append(" IN (");
        sql.append(IREPrestationAccordee.CS_ETAT_VALIDE).append(",");
        sql.append(IREPrestationAccordee.CS_ETAT_PARTIEL).append(",");
        sql.append(IREPrestationAccordee.CS_ETAT_DIMINUE).append(",");
        sql.append(IREPrestationAccordee.CS_ETAT_AJOURNE);
        sql.append(")");

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REEcheancesEntity();
    }

    private void checkRenteVeuvePerdure(REEcheancesEntity uneEcheanceDeLaRequete) {
        boolean hasRenteVeuvePerdure = false;

        REDemandeJoinInfoComplManager demandeJoinInfoComplManager = new REDemandeJoinInfoComplManager(getSession());
        demandeJoinInfoComplManager
                .setForCsTypeInfoCompl(IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_RENTE_VEUVE_PERDURE);
        demandeJoinInfoComplManager.setForIdTiersRequerant(uneEcheanceDeLaRequete.getIdTiers());
        try {
            demandeJoinInfoComplManager.find();
            if (demandeJoinInfoComplManager.size() > 0) {
                hasRenteVeuvePerdure = true;
            }
        } catch (Exception ex) {
            JadeLogger.error(this, ex.toString());
        }

        uneEcheanceDeLaRequete.setHasRenteVeuvePerdure(hasRenteVeuvePerdure);
    }

    public String getForDateTraitement() {
        return forDateTraitement;
    }

    public String getForIdGroupeLocalite() {
        return forIdGroupeLocalite;
    }

    private boolean hasRenteSurvivant(IREEcheances echeance) {
        for (IRERenteEcheances rente : echeance.getRentesDuTiers()) {
            int codePrestation = Integer.parseInt(rente.getCodePrestation());
            switch (codePrestation) {
                case REGenresPrestations.RENTE_PRINCIPALE_SURVIVANT_ORDINAIRE:
                case REGenresPrestations.RENTE_PRINCIPALE_SURVIVANT_EXTRAORDINAIRE:
                    return true;
            }
        }
        return false;
    }

    public void setForDateTraitement(String forDateTraitement) {
        this.forDateTraitement = forDateTraitement;
    }

    public void setForIdGroupeLocalite(String forIdGroupeLocalite) {
        this.forIdGroupeLocalite = forIdGroupeLocalite;
    }
}
