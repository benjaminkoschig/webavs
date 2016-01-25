package globaz.corvus.db.demandes;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.creances.RECreanceAccordee;
import globaz.corvus.db.creances.RECreancier;
import globaz.corvus.db.interetsmoratoires.RECalculInteretMoratoire;
import globaz.corvus.db.interetsmoratoires.REInteretMoratoire;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeBloquee;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.adressecourrier.ITIPaysDefTable;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ch.globaz.corvus.business.services.models.demande.DemandeRenteCrudService;
import ch.globaz.corvus.domaine.constantes.CodeCasSpecialRente;
import ch.globaz.corvus.domaine.constantes.EtatDemandeRente;
import ch.globaz.corvus.domaine.constantes.TypeCalculDemandeRente;
import ch.globaz.corvus.domaine.constantes.TypeDemandeRente;
import ch.globaz.corvus.domaine.constantes.TypePrestationDue;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.prestation.domaine.constantes.EtatPrestationAccordee;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;
import ch.globaz.pyxis.domaine.Sexe;
import ch.globaz.pyxis.domaine.constantes.CodeIsoPays;

/**
 * Entité pour l'implémentation Jade du service de chargement des demandes de rente {@link DemandeRenteCrudService}
 */
// @formatter:off
public final class REDemandeRentePourServiceDomaine extends BEntity {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ALIAS_CHAMP_CODE_ISO_PAYS_BASE_CALCUL = "pays_bc_iso";
	private static final String ALIAS_CHAMP_CODE_ISO_PAYS_BENEFICIAIRE = "pays_benef_iso";
	private static final String ALIAS_CHAMP_CODE_ISO_PAYS_CREANCIER = "pays_crean_iso";
	private static final String ALIAS_CHAMP_CODE_ISO_PAYS_REQUERANT = "pays_requ_iso";
	private static final String ALIAS_CHAMP_CS_TITRE_TIERS_BASE_CALCUL = "ti_bc_titre";
	private static final String ALIAS_CHAMP_CS_TITRE_TIERS_BENEFICIAIRE = "ti_benef_titre";
	private static final String ALIAS_CHAMP_CS_TITRE_TIERS_CREANCIER = "ti_crean_titre";
	private static final String ALIAS_CHAMP_CS_TITRE_TIERS_REQUERANT = "ti_requ_titre";
	private static final String ALIAS_CHAMP_DATE_DECES_BASE_CALCUL = "pers_bc_datedec";
	private static final String ALIAS_CHAMP_DATE_DECES_BENEFICIAIRE = "pers_benef_datedec";
	private static final String ALIAS_CHAMP_DATE_DECES_REQUERANT = "pers_requ_datedec";
	private static final String ALIAS_CHAMP_DATE_NAISSANCE_BASE_CALCUL = "pers_bc_datenai";
	private static final String ALIAS_CHAMP_DATE_NAISSANCE_BENEFICIAIRE = "pers_benef_datenai";
	private static final String ALIAS_CHAMP_DATE_NAISSANCE_REQUERANT = "pers_requ_datenai";
	private static final String ALIAS_CHAMP_ID_PAYS_BASE_CALCUL = "ti_bc_idpays";
	private static final String ALIAS_CHAMP_ID_PAYS_BENEFICIAIRE = "ti_benef_idpays";
	private static final String ALIAS_CHAMP_ID_PAYS_CREANCIER = "ti_crean_idpays";
	private static final String ALIAS_CHAMP_ID_PAYS_REQUERANT = "ti_requ_idpays";
	private static final String ALIAS_CHAMP_ID_TIERS_BASE_CALCUL = "ti_bc_id";
	private static final String ALIAS_CHAMP_ID_TIERS_BENEFICIAIRE = "ti_benef_id";
	private static final String ALIAS_CHAMP_ID_TIERS_CREANCIER = "ti_crean_id";
	private static final String ALIAS_CHAMP_ID_TIERS_REQUERANT = "ti_requ_id";
	private static final String ALIAS_CHAMP_NOM_BASE_CALCUL = "ti_bc_nom";
	private static final String ALIAS_CHAMP_NOM_BENEFICIAIRE = "ti_benef_nom";
	private static final String ALIAS_CHAMP_NOM_CREANCIER = "ti_crean_nom";
	private static final String ALIAS_CHAMP_NOM_REQUERANT = "ti_requ_nom";
	private static final String ALIAS_CHAMP_NSS_BASE_CALCUL = "pavs_bc_nss";
	private static final String ALIAS_CHAMP_NSS_BENEFICIAIRE = "pavs_benef_nss";
	private static final String ALIAS_CHAMP_NSS_REQUERANT = "pavs_requ_nss";
	private static final String ALIAS_CHAMP_PRENOM_BASE_CALCUL = "ti_bc_prenom";
	private static final String ALIAS_CHAMP_PRENOM_BENEFICIAIRE = "ti_benef_prenom";
	private static final String ALIAS_CHAMP_PRENOM_CREANCIER = "ti_crean_prenom";
	private static final String ALIAS_CHAMP_PRENOM_REQUERANT = "ti_requ_prenom";
	private static final String ALIAS_CHAMP_SEXE_BASE_CALCUL = "pers_bc_sexe";
	private static final String ALIAS_CHAMP_SEXE_BENEFICIAIRE = "pers_benef_sexe";
	private static final String ALIAS_CHAMP_SEXE_REQUERANT = "pers_requ_sexe";
	private static final String ALIAS_CHAMP_TRADUCTION_ALLEMANDE_PAYS_BASE_CALCUL = "pays_bc_de";
	private static final String ALIAS_CHAMP_TRADUCTION_ALLEMANDE_PAYS_BENEFICIAIRE = "pays_benef_de";
	private static final String ALIAS_CHAMP_TRADUCTION_ALLEMANDE_PAYS_CREANCIER = "pays_crean_de";
	private static final String ALIAS_CHAMP_TRADUCTION_ALLEMANDE_PAYS_REQUERANT = "pays_requ_de";
	private static final String ALIAS_CHAMP_TRADUCTION_FRANCAISE_PAYS_BASE_CALCUL = "pays_bc_fr";
	private static final String ALIAS_CHAMP_TRADUCTION_FRANCAISE_PAYS_BENEFICIAIRE = "pays_benef_fr";
	private static final String ALIAS_CHAMP_TRADUCTION_FRANCAISE_PAYS_CREANCIER = "pays_crean_fr";
	private static final String ALIAS_CHAMP_TRADUCTION_FRANCAISE_PAYS_REQUERANT = "pays_requ_fr";
	private static final String ALIAS_CHAMP_TRADUCTION_ITALIENNE_PAYS_BASE_CALCUL = "pays_bc_it";
	private static final String ALIAS_CHAMP_TRADUCTION_ITALIENNE_PAYS_BENEFICIAIRE = "pays_benef_it";
	private static final String ALIAS_CHAMP_TRADUCTION_ITALIENNE_PAYS_CREANCIER = "pays_crean_it";
	private static final String ALIAS_CHAMP_TRADUCTION_ITALIENNE_PAYS_REQUERANT = "pays_requ_it";
	private static final String ALIAS_CHAMP_TYPE_TIERS_CREANCIER = "ti_crean_type";
	private static final String ALIAS_TABLE_PAYS_BASE_CALCUL = "pays_bc";
	private static final String ALIAS_TABLE_PAYS_BENEFICIAIRE = "pays_benef";
	private static final String ALIAS_TABLE_PAYS_CREANCIER = "pays_crean";
	private static final String ALIAS_TABLE_PAYS_REQUERANT = "pays_requ";
	private static final String ALIAS_TABLE_PERSONNE_AVS_BASE_CALCUL = "pavs_bc";
	private static final String ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE = "pavs_benef";
	private static final String ALIAS_TABLE_PERSONNE_AVS_REQUERANT = "pavs_requ";
	private static final String ALIAS_TABLE_PERSONNE_BASE_CALCUL = "pers_bc";
	private static final String ALIAS_TABLE_PERSONNE_BENEFICIAIRE = "pers_benef";
	private static final String ALIAS_TABLE_PERSONNE_REQUERANT = "pers_requ";
	private static final String ALIAS_TABLE_TIERS_BASE_CALCUL = "ti_bc";
	private static final String ALIAS_TABLE_TIERS_BENEFICIAIRE = "ti_benef";
	private static final String ALIAS_TABLE_TIERS_CREANCIER = "ti_crean";
	private static final String ALIAS_TABLE_TIERS_REQUERANT = "ti_requ";

	private static final Integer parseInt(final String str, final String nomDuChamp) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException ex) {
			throw new RETechnicalException("[" + nomDuChamp + "] is null or invalid", ex);
		}
	}

	private static final Long parseLong(final String str, final String nomDuChamp) {
		try {
			return Long.parseLong(str);
		} catch (NumberFormatException ex) {
			throw new RETechnicalException("[" + nomDuChamp + "] is null or invalid", ex);
		}
	}

	private CodeCasSpecialRente codeCasSpecial1;
	private CodeCasSpecialRente codeCasSpecial2;
	private CodeCasSpecialRente codeCasSpecial3;
	private CodeCasSpecialRente codeCasSpecial4;
	private CodeCasSpecialRente codeCasSpecial5;
	private CodeIsoPays codeIsoPaysTiersBaseCalcul;
	private CodeIsoPays codeIsoPaysTiersBeneficiaire;
	private CodeIsoPays codeIsoPaysTiersCreancier;
	private CodeIsoPays codeIsoPaysTiersRequerant;
	private CodePrestation codePrestationRenteAccordee;
	private Integer csTitreTiersBaseCalcul;
	private Integer csTitreTiersBeneficiaire;
	private Integer csTitreTiersCreancier;
	private Integer csTitreTiersRequerant;
	private String dateDebutDroitDemandeRente;
	private String dateDecesTiersBaseCalcul;
	private String dateDecesTiersBeneficiaire;
	private String dateDecesTiersRequerant;
	private String dateDepotDemandeRente;
	private String dateFinDroitDemandeRente;
	private String dateNaissanceTiersBaseCalcul;
	private String dateNaissanceTiersBeneficiaire;
	private String dateNaissanceTiersRequerant;
	private String dateReceptionDemandeRente;
	private String dateTraitementDemandeRente;
	private EtatDemandeRente etatDemandeRente;
	private EtatPrestationAccordee etatPrestationAccordee;
	private String gestionnaireDemandeRente;
	private Long idBaseCalcul;
	private Long idBlocage;
	private Long idCreance;
	private Long idDemandePrestation;
	private Long idDemandeRente;
	private Long idEnTeteBlocage;
	private Long idInformationComplementaire;
	private Long idInteretMoratoire;
	private Long idPaysTiersBaseCalcul;
	private Long idPaysTiersBeneficiaire;
	private Long idPaysTiersCreancier;
	private Long idPaysTiersRequerant;
	private Long idPrestationDue;
	private Long idRenteAccordee;
	private Long idRepartitionCreance;
	private Long idTiersBaseCalcul;
	private Long idTiersBeneficiairePrestationAccordee;
	private Long idTiersCreancier;
	private Long idTiersRequerant;
	private boolean laPrestationAccordeeEstBloquee;
	private String moisBlocage;
	private String moisDebutDroitPrestationAccordee;
	private String moisDebutPrestationDue;
	private String moisFinDroitPrestationAccordee;
	private String moisFinPrestationDue;
	private BigDecimal montantBlocage;
	private BigDecimal montantCreance;
	private BigDecimal montantDebloque;
	private BigDecimal montantPrestationAccordee;
	private BigDecimal montantPrestationDue;
	private BigDecimal montantRepartitionCreance;
	private String nomTiersBaseCalcul;
	private String nomTiersBeneficiaire;
	private String nomTiersCreancier;
	private String nomTiersRequerant;
	private NumeroSecuriteSociale nssTiersBaseCalcul;
	private NumeroSecuriteSociale nssTiersBeneficiaire;
	private NumeroSecuriteSociale nssTiersRequerant;
	private String prenomTiersBaseCalcul;
	private String prenomTiersBeneficiaire;
	private String prenomTiersCreancier;
	private String prenomTiersRequerant;
	private Sexe sexeTiersBaseCalcul;
	private Sexe sexeTiersBeneficiaire;
	private Sexe sexeTiersRequerant;
	private String traductionPaysTiersBaseCalculEnAllemand;
	private String traductionPaysTiersBaseCalculEnFrancais;
	private String traductionPaysTiersBaseCalculEnItalien;
	private String traductionPaysTiersBeneficiaireEnAllemand;
	private String traductionPaysTiersBeneficiaireEnFrancais;
	private String traductionPaysTiersBeneficiaireEnItalien;
	private String traductionPaysTiersCreancierEnAllemand;
	private String traductionPaysTiersCreancierEnFrancais;
	private String traductionPaysTiersCreancierEnItalien;
	private String traductionPaysTiersRequerantEnAllemand;
	private String traductionPaysTiersRequerantEnFrancais;
	private String traductionPaysTiersRequerantEnItalien;
	private TypeCalculDemandeRente typeCalculDemandeRente;
	private TypeDemandeRente typeDemandeRente;
	private TypePrestationDue typePrestationDue;

	public REDemandeRentePourServiceDomaine() {
		super();

		codeCasSpecial1 = null;
		codeCasSpecial2 = null;
		codeCasSpecial3 = null;
		codeCasSpecial4 = null;
		codeCasSpecial5 = null;
		codeIsoPaysTiersBeneficiaire = null;
		codeIsoPaysTiersCreancier = null;
		codeIsoPaysTiersRequerant = null;
		codeIsoPaysTiersBaseCalcul = null;
		codePrestationRenteAccordee = null;
		csTitreTiersBeneficiaire = null;
		csTitreTiersCreancier = null;
		csTitreTiersRequerant = null;
		csTitreTiersBaseCalcul = null;
		dateDebutDroitDemandeRente = null;
		dateDecesTiersBeneficiaire = null;
		dateDecesTiersRequerant = null;
		dateDecesTiersBaseCalcul = null;
		dateDepotDemandeRente = null;
		dateFinDroitDemandeRente = null;
		dateNaissanceTiersBeneficiaire = null;
		dateNaissanceTiersRequerant = null;
		dateNaissanceTiersBaseCalcul = null;
		dateReceptionDemandeRente = null;
		dateTraitementDemandeRente = null;
		etatDemandeRente = null;
		etatPrestationAccordee = null;
		gestionnaireDemandeRente = null;
		idBaseCalcul = null;
		idBlocage = null;
		idCreance = null;
		idEnTeteBlocage = null;
		idDemandePrestation = null;
		idDemandeRente = null;
		idInformationComplementaire = null;
		idInteretMoratoire = null;
		idPaysTiersBeneficiaire = null;
		idPaysTiersCreancier = null;
		idPaysTiersRequerant = null;
		idPaysTiersBaseCalcul = null;
		idPrestationDue = null;
		idRenteAccordee = null;
		idRepartitionCreance = null;
		idTiersBeneficiairePrestationAccordee = null;
		idTiersCreancier = null;
		idTiersRequerant = null;
		idTiersBaseCalcul = null;
		laPrestationAccordeeEstBloquee = false;
		moisBlocage = null;
		moisDebutDroitPrestationAccordee = null;
		moisDebutPrestationDue = null;
		moisFinDroitPrestationAccordee = null;
		moisFinPrestationDue = null;
		montantBlocage = null;
		montantCreance = null;
		montantDebloque = null;
		montantPrestationAccordee = null;
		montantPrestationDue = null;
		montantRepartitionCreance = null;
		nomTiersBeneficiaire = null;
		nomTiersCreancier = null;
		nomTiersRequerant = null;
		nomTiersBaseCalcul = null;
		nssTiersBeneficiaire = null;
		nssTiersRequerant = null;
		nssTiersBaseCalcul = null;
		prenomTiersBeneficiaire = null;
		prenomTiersCreancier = null;
		prenomTiersRequerant = null;
		prenomTiersBaseCalcul = null;
		sexeTiersBeneficiaire = null;
		sexeTiersRequerant = null;
		sexeTiersBaseCalcul = null;
		traductionPaysTiersBeneficiaireEnAllemand = null;
		traductionPaysTiersBeneficiaireEnFrancais = null;
		traductionPaysTiersBeneficiaireEnItalien = null;
		traductionPaysTiersCreancierEnAllemand = null;
		traductionPaysTiersCreancierEnFrancais = null;
		traductionPaysTiersCreancierEnItalien = null;
		traductionPaysTiersRequerantEnAllemand = null;
		traductionPaysTiersRequerantEnFrancais = null;
		traductionPaysTiersRequerantEnItalien = null;
		traductionPaysTiersBaseCalculEnAllemand = null;
		traductionPaysTiersBaseCalculEnFrancais = null;
		traductionPaysTiersBaseCalculEnItalien = null;
		typeCalculDemandeRente = null;
		typeDemandeRente = null;
		typePrestationDue = null;
	}



	@Override
	protected String _getFields(final BStatement statement) {
		StringBuilder sql = new StringBuilder();

		String tableDemandeRente = _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;
		String tableDossierPrestation = _getCollection() + PRDemande.TABLE_NAME;
		String tableBaseCalcul = _getCollection() + REBasesCalcul.TABLE_NAME_BASES_CALCUL;
		String tableRenteAccordee = _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
		String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
		String tableInteretMoratoire = _getCollection() + REInteretMoratoire.TABLE_NAME_INTERET_MORATOIRE;
		String tableRepartitionCreance = _getCollection() + RECreanceAccordee.TABLE_NAME_CREANCES_ACCORDEES;
		String tableCreance = _getCollection() + RECreancier.TABLE_NAME_CREANCIER;
		String tablePrestationDue = _getCollection() + REPrestationDue.TABLE_NAME_PRESTATIONS_DUES;
		String tableEnTeteBlocage = _getCollection() + REEnteteBlocage.TABLE_NAME_ENTETE_BLOCAGE;
		String tableBlocage = _getCollection() + REPrestationAccordeeBloquee.TABLE_NAME_PRESTATION_ACCORDEE_BLOQUEE;

		sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE).append(",");
		sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_CS_ETAT).append(",");
		sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_CS_TYPE_CALCUL).append(",");
		sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE).append(",");
		sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_DATE_DEBUT).append(",");
		sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_DATE_DEPOT).append(",");
		sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_DATE_FIN).append(",");
		sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_DATE_RECEPTION).append(",");
		sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_DATE_TRAITEMENT).append(",");
		sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_GESTIONNAIRE).append(",");
		sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_INFO_COMPLEMENTAIRE).append(",");

		sql.append(tableDossierPrestation).append(".").append(PRDemande.FIELDNAME_IDDEMANDE).append(",");

		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_REQUERANT).append(".").append(ITITiersDefTable.ID_TIERS).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_ID_TIERS_REQUERANT).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_REQUERANT).append(".").append(ITITiersDefTable.DESIGNATION_1).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_NOM_REQUERANT).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_REQUERANT).append(".").append(ITITiersDefTable.DESIGNATION_2).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_PRENOM_REQUERANT).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_REQUERANT).append(".").append(ITITiersDefTable.CS_TITRE_TIERS).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_CS_TITRE_TIERS_REQUERANT).append(",");

		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_REQUERANT).append(".").append(ITIPersonneDefTable.CS_SEXE).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_SEXE_REQUERANT).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_REQUERANT).append(".").append(ITIPersonneDefTable.DATE_DECES).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_DATE_DECES_REQUERANT).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_REQUERANT).append(".").append(ITIPersonneDefTable.DATE_NAISSANCE).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_DATE_NAISSANCE_REQUERANT).append(",");

		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_AVS_REQUERANT).append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_NSS_REQUERANT).append(",");

		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_REQUERANT).append(".").append(ITIPaysDefTable.ID_PAYS).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_ID_PAYS_REQUERANT).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_REQUERANT).append(".").append(ITIPaysDefTable.CODE_ISO).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_CODE_ISO_PAYS_REQUERANT).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_REQUERANT).append(".").append(ITIPaysDefTable.LIBELLE_DE).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_FRANCAISE_PAYS_REQUERANT).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_REQUERANT).append(".").append(ITIPaysDefTable.LIBELLE_FR).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_ALLEMANDE_PAYS_REQUERANT).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_REQUERANT).append(".").append(ITIPaysDefTable.LIBELLE_IT).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_ITALIENNE_PAYS_REQUERANT).append(",");

		sql.append(tableBaseCalcul).append(".").append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL).append(",");

		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_BASE_CALCUL).append(".").append(ITITiersDefTable.ID_TIERS).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_ID_TIERS_BASE_CALCUL).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_BASE_CALCUL).append(".").append(ITITiersDefTable.DESIGNATION_1).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_NOM_BASE_CALCUL).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_BASE_CALCUL).append(".").append(ITITiersDefTable.DESIGNATION_2).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_PRENOM_BASE_CALCUL).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_BASE_CALCUL).append(".").append(ITITiersDefTable.CS_TITRE_TIERS).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_CS_TITRE_TIERS_BASE_CALCUL).append(",");

		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_BASE_CALCUL).append(".").append(ITIPersonneDefTable.CS_SEXE).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_SEXE_BASE_CALCUL).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_BASE_CALCUL).append(".").append(ITIPersonneDefTable.DATE_DECES).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_DATE_DECES_BASE_CALCUL).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_BASE_CALCUL).append(".").append(ITIPersonneDefTable.DATE_NAISSANCE).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_DATE_NAISSANCE_BASE_CALCUL).append(",");

		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_AVS_BASE_CALCUL).append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_NSS_BASE_CALCUL).append(",");

		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_BASE_CALCUL).append(".").append(ITIPaysDefTable.ID_PAYS).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_ID_PAYS_BASE_CALCUL).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_BASE_CALCUL).append(".").append(ITIPaysDefTable.CODE_ISO).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_CODE_ISO_PAYS_BASE_CALCUL).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_BASE_CALCUL).append(".").append(ITIPaysDefTable.LIBELLE_DE).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_FRANCAISE_PAYS_BASE_CALCUL).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_BASE_CALCUL).append(".").append(ITIPaysDefTable.LIBELLE_FR).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_ALLEMANDE_PAYS_BASE_CALCUL).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_BASE_CALCUL).append(".").append(ITIPaysDefTable.LIBELLE_IT).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_ITALIENNE_PAYS_BASE_CALCUL).append(",");

		sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE).append(",");
		sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1).append(",");
		sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2).append(",");
		sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3).append(",");
		sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4).append(",");
		sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5).append(",");

		sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(",");
		sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(",");
		sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(",");
		sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(",");
		sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION).append(",");
		sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append(",");
		sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE).append(",");

		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".").append(ITITiersDefTable.ID_TIERS).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_ID_TIERS_BENEFICIAIRE).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".").append(ITITiersDefTable.DESIGNATION_1).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_NOM_BENEFICIAIRE).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".").append(ITITiersDefTable.DESIGNATION_2).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_PRENOM_BENEFICIAIRE).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".").append(ITITiersDefTable.CS_TITRE_TIERS).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_CS_TITRE_TIERS_BENEFICIAIRE).append(",");

		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_BENEFICIAIRE).append(".").append(ITIPersonneDefTable.CS_SEXE).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_SEXE_BENEFICIAIRE).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_BENEFICIAIRE).append(".").append(ITIPersonneDefTable.DATE_DECES).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_DATE_DECES_BENEFICIAIRE).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_BENEFICIAIRE).append(".").append(ITIPersonneDefTable.DATE_NAISSANCE).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_DATE_NAISSANCE_BENEFICIAIRE).append(",");

		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE).append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_NSS_BENEFICIAIRE).append(",");

		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_BENEFICIAIRE).append(".").append(ITIPaysDefTable.ID_PAYS).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_ID_PAYS_BENEFICIAIRE).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_BENEFICIAIRE).append(".").append(ITIPaysDefTable.CODE_ISO).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_CODE_ISO_PAYS_BENEFICIAIRE).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_BENEFICIAIRE).append(".").append(ITIPaysDefTable.LIBELLE_DE).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_FRANCAISE_PAYS_BENEFICIAIRE).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_BENEFICIAIRE).append(".").append(ITIPaysDefTable.LIBELLE_FR).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_ALLEMANDE_PAYS_BENEFICIAIRE).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_BENEFICIAIRE).append(".").append(ITIPaysDefTable.LIBELLE_IT).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_ITALIENNE_PAYS_BENEFICIAIRE).append(",");

		sql.append(tableInteretMoratoire).append(".").append(REInteretMoratoire.FIELDNAME_ID_INTERET_MORATOIRE).append(",");

		sql.append(tableRepartitionCreance).append(".").append(RECreanceAccordee.FIELDNAME_ID_CREANCE_ACCORDEE).append(",");
		sql.append(tableRepartitionCreance).append(".").append(RECreanceAccordee.FIELDNAME_MONTANT).append(",");

		sql.append(tableCreance).append(".").append(RECreancier.FIELDNAME_ID_CREANCIER).append(",");
		sql.append(tableCreance).append(".").append(RECreancier.FIELDNAME_MONTANT_REVANDIQUE).append(",");

		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_CREANCIER).append(".").append(ITITiersDefTable.ID_TIERS).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_ID_TIERS_CREANCIER).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_CREANCIER).append(".").append(ITITiersDefTable.DESIGNATION_1).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_NOM_CREANCIER).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_CREANCIER).append(".").append(ITITiersDefTable.DESIGNATION_2).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_PRENOM_CREANCIER).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_CREANCIER).append(".").append(ITITiersDefTable.CS_TITRE_TIERS).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_CS_TITRE_TIERS_CREANCIER).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_CREANCIER).append(".").append(ITITiersDefTable.CS_TYPE_TIERS).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TYPE_TIERS_CREANCIER).append(",");

		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_CREANCIER).append(".").append(ITIPaysDefTable.ID_PAYS).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_ID_PAYS_CREANCIER).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_CREANCIER).append(".").append(ITIPaysDefTable.CODE_ISO).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_CODE_ISO_PAYS_CREANCIER).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_CREANCIER).append(".").append(ITIPaysDefTable.LIBELLE_DE).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_FRANCAISE_PAYS_CREANCIER).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_CREANCIER).append(".").append(ITIPaysDefTable.LIBELLE_FR).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_ALLEMANDE_PAYS_CREANCIER).append(",");
		sql.append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_CREANCIER).append(".").append(ITIPaysDefTable.LIBELLE_IT).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_ITALIENNE_PAYS_CREANCIER).append(",");

		sql.append(tablePrestationDue).append(".").append(REPrestationDue.FIELDNAME_ID_PRESTATION_DUE).append(",");
		sql.append(tablePrestationDue).append(".").append(REPrestationDue.FIELDNAME_CS_TYPE).append(",");
		sql.append(tablePrestationDue).append(".").append(REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT).append(",");
		sql.append(tablePrestationDue).append(".").append(REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT).append(",");
		sql.append(tablePrestationDue).append(".").append(REPrestationDue.FIELDNAME_MONTANT).append(",");

		sql.append(tableEnTeteBlocage).append(".").append(REEnteteBlocage.FIELDNAME_ID_ENTETE_BLOCAGE).append(",");
		sql.append(tableEnTeteBlocage).append(".").append(REEnteteBlocage.FIELDNAME_MONTANT_DEBLOQUE).append(",");

		sql.append(tableBlocage).append(".").append(REPrestationAccordeeBloquee.FIELDNAME_ID_PA_BLOQUEE).append(",");
		sql.append(tableBlocage).append(".").append(REPrestationAccordeeBloquee.FIELDNAME_DATE_BLOCAGE).append(",");
		sql.append(tableBlocage).append(".").append(REPrestationAccordeeBloquee.FIELDNAME_MONTANT);

		return sql.toString();
	}

	@Override
	protected String _getFrom(final BStatement statement) {
		StringBuilder sql = new StringBuilder();

		String tableDemandeRente = _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;
		String tableDossierPrestation = _getCollection() + PRDemande.TABLE_NAME;
		String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
		String tablePersonne = _getCollection() + ITIPersonneDefTable.TABLE_NAME;
		String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;
		String tablePays = _getCollection() + ITIPaysDefTable.TABLE_NAME;
		String tableBaseCalcul = _getCollection() + REBasesCalcul.TABLE_NAME_BASES_CALCUL;
		String tableRenteAccordee = _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
		String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
		String tableCalculInteretMoratoire = _getCollection() + RECalculInteretMoratoire.TABLE_NAME_CALCUL_INTERET_MORATOIRE;
		String tableInteretMoratoire = _getCollection() + REInteretMoratoire.TABLE_NAME_INTERET_MORATOIRE;
		String tableRepartitionCreance = _getCollection() + RECreanceAccordee.TABLE_NAME_CREANCES_ACCORDEES;
		String tableCreance = _getCollection() + RECreancier.TABLE_NAME_CREANCIER;
		String tablePrestationDue = _getCollection() + REPrestationDue.TABLE_NAME_PRESTATIONS_DUES;
		String tableEnTeteBlocage = _getCollection() + REEnteteBlocage.TABLE_NAME_ENTETE_BLOCAGE;
		String tableBlocage = _getCollection() + REPrestationAccordeeBloquee.TABLE_NAME_PRESTATION_ACCORDEE_BLOQUEE;

		sql.append(tableDemandeRente);

		sql.append(" INNER JOIN ").append(tableDossierPrestation);
		sql.append(" ON ").append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION).append("=").append(tableDossierPrestation).append(".").append(PRDemande.FIELDNAME_IDDEMANDE);

		sql.append(" INNER JOIN ").append(tableTiers).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_REQUERANT);
		sql.append(" ON ").append(tableDossierPrestation).append(".").append(PRDemande.FIELDNAME_IDTIERS).append("=").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_REQUERANT).append(".").append(ITITiersDefTable.ID_TIERS);

		sql.append(" INNER JOIN ").append(tablePersonne).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_REQUERANT);
		sql.append(" ON ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_REQUERANT).append(".").append(ITITiersDefTable.ID_TIERS).append("=").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_REQUERANT).append(".").append(ITIPersonneDefTable.ID_TIERS);

		sql.append(" INNER JOIN ").append(tablePersonneAvs).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_AVS_REQUERANT);
		sql.append(" ON ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_REQUERANT).append(".").append(ITIPersonneDefTable.ID_TIERS).append("=").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_AVS_REQUERANT).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

		sql.append(" INNER JOIN ").append(tablePays).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_REQUERANT);
		sql.append(" ON ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_REQUERANT).append(".").append(ITITiersDefTable.ID_PAYS).append("=").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_REQUERANT).append(".").append(ITIPaysDefTable.ID_PAYS);

		sql.append(" LEFT OUTER JOIN ").append(tableBaseCalcul);
		sql.append(" ON ").append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE).append("=").append(tableBaseCalcul).append(".").append(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE);

		sql.append(" LEFT OUTER JOIN ").append(tableTiers).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_BASE_CALCUL);
		sql.append(" ON ").append(tableBaseCalcul).append(".").append(REBasesCalcul.FIELDNAME_ID_TIERS_BASE_CALCUL).append("=").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_BASE_CALCUL).append(".").append(ITITiersDefTable.ID_TIERS);

		sql.append(" LEFT OUTER JOIN ").append(tablePersonne).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_BASE_CALCUL);
		sql.append(" ON ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_BASE_CALCUL).append(".").append(ITITiersDefTable.ID_TIERS).append("=").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_BASE_CALCUL).append(".").append(ITIPersonneDefTable.ID_TIERS);

		sql.append(" LEFT OUTER JOIN ").append(tablePersonneAvs).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_AVS_BASE_CALCUL);
		sql.append(" ON ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_BASE_CALCUL).append(".").append(ITIPersonneDefTable.ID_TIERS).append("=").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_AVS_BASE_CALCUL).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

		sql.append(" LEFT OUTER JOIN ").append(tablePays).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_BASE_CALCUL);
		sql.append(" ON ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_BASE_CALCUL).append(".").append(ITITiersDefTable.ID_PAYS).append("=").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_BASE_CALCUL).append(".").append(ITIPaysDefTable.ID_PAYS);

		sql.append(" LEFT OUTER JOIN ").append(tableRenteAccordee);
		sql.append(" ON ").append(tableBaseCalcul).append(".").append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL).append("=").append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);

		sql.append(" LEFT OUTER JOIN ").append(tablePrestationAccordee);
		sql.append(" ON ").append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE).append("=").append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

		sql.append(" LEFT OUTER JOIN ").append(tableTiers).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_BENEFICIAIRE);
		sql.append(" ON ").append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append("=").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".").append(ITITiersDefTable.ID_TIERS);

		sql.append(" LEFT OUTER JOIN ").append(tablePersonne).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_BENEFICIAIRE);
		sql.append(" ON ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".").append(ITITiersDefTable.ID_TIERS).append("=").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_BENEFICIAIRE).append(".").append(ITIPersonneDefTable.ID_TIERS);

		sql.append(" LEFT OUTER JOIN ").append(tablePersonneAvs).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE);
		sql.append(" ON ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_BENEFICIAIRE).append(".").append(ITIPersonneDefTable.ID_TIERS).append("=").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

		sql.append(" LEFT OUTER JOIN ").append(tablePays).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_BENEFICIAIRE);
		sql.append(" ON ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".").append(ITITiersDefTable.ID_PAYS).append("=").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_BENEFICIAIRE).append(".").append(ITIPaysDefTable.ID_PAYS);

		sql.append(" LEFT OUTER JOIN ").append(tableCalculInteretMoratoire);
		sql.append(" ON ").append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_CALCUL_INTERET_MORATOIRE).append("=").append(tableCalculInteretMoratoire).append(".").append(RECalculInteretMoratoire.FIELDNAME_ID_CALCUL_INTERET_MORATOIRE);

		sql.append(" LEFT OUTER JOIN ").append(tableInteretMoratoire);
		sql.append(" ON ").append(tableCalculInteretMoratoire).append(".").append(RECalculInteretMoratoire.FIELDNAME_ID_INTERET_MORATOIRE).append("=").append(tableInteretMoratoire).append(".").append(REInteretMoratoire.FIELDNAME_ID_INTERET_MORATOIRE);

		sql.append(" LEFT OUTER JOIN ").append(tableRepartitionCreance);
		sql.append(" ON ").append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append("=").append(tableRepartitionCreance).append(".").append(RECreanceAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

		sql.append(" LEFT OUTER JOIN ").append(tableCreance);
		sql.append(" ON ").append(tableRepartitionCreance).append(".").append(RECreanceAccordee.FIELDNAME_ID_CREANCIER).append("=").append(tableCreance).append(".").append(RECreancier.FIELDNAME_ID_CREANCIER);

		sql.append(" LEFT OUTER JOIN ").append(tableTiers).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_CREANCIER);
		sql.append(" ON ").append(tableCreance).append(".").append(RECreancier.FIELDNAME_ID_TIERS).append("=").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_CREANCIER).append(".").append(ITITiersDefTable.ID_TIERS);

		sql.append(" LEFT OUTER JOIN ").append(tablePays).append(" AS ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_CREANCIER);
		sql.append(" ON ").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_TIERS_CREANCIER).append(".").append(ITITiersDefTable.ID_PAYS).append("=").append(REDemandeRentePourServiceDomaine.ALIAS_TABLE_PAYS_CREANCIER).append(".").append(ITIPaysDefTable.ID_PAYS);

		sql.append(" LEFT OUTER JOIN ").append(tablePrestationDue);
		sql.append(" ON ").append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append("=").append(tablePrestationDue).append(".").append(REPrestationDue.FIELDNAME_ID_RENTE_ACCORDEE);

		sql.append(" LEFT OUTER JOIN ").append(tableEnTeteBlocage);
		sql.append(" ON ").append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_ENTETE_BLOCAGE).append("=").append(tableEnTeteBlocage).append(".").append(REEnteteBlocage.FIELDNAME_ID_ENTETE_BLOCAGE);

		sql.append(" LEFT OUTER JOIN ").append(tableBlocage);
		sql.append(" ON ").append(tableEnTeteBlocage).append(".").append(REEnteteBlocage.FIELDNAME_ID_ENTETE_BLOCAGE).append("=").append(tableBlocage).append(".").append(REPrestationAccordeeBloquee.FIELDNAME_ID_ENTETE_BLOCAGE);

		return sql.toString();
	}

	@Override
	protected String _getTableName() {
		return _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;
	}

	@Override
	protected void _readProperties(final BStatement statement) throws Exception {

		idDemandeRente = REDemandeRentePourServiceDomaine.parseLong(statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE), "demande.id");
		etatDemandeRente = EtatDemandeRente.parse(statement.dbReadNumeric(REDemandeRente.FIELDNAME_CS_ETAT));
		typeCalculDemandeRente = TypeCalculDemandeRente.parse(statement.dbReadNumeric(REDemandeRente.FIELDNAME_CS_TYPE_CALCUL));
		typeDemandeRente = TypeDemandeRente.parse(statement.dbReadNumeric(REDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE));
		dateDebutDroitDemandeRente = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement.dbReadNumeric(REDemandeRente.FIELDNAME_DATE_DEBUT));
		dateDepotDemandeRente = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement.dbReadNumeric(REDemandeRente.FIELDNAME_DATE_DEPOT));
		dateFinDroitDemandeRente = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement.dbReadNumeric(REDemandeRente.FIELDNAME_DATE_FIN));
		dateReceptionDemandeRente = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement.dbReadNumeric(REDemandeRente.FIELDNAME_DATE_RECEPTION));
		dateTraitementDemandeRente = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement.dbReadNumeric(REDemandeRente.FIELDNAME_DATE_TRAITEMENT));
		gestionnaireDemandeRente = statement.dbReadString(REDemandeRente.FIELDNAME_ID_GESTIONNAIRE);
		idInformationComplementaire = REDemandeRentePourServiceDomaine.parseLong(statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_INFO_COMPLEMENTAIRE), "demande.informationComplementaire.id");

		idDemandePrestation = REDemandeRentePourServiceDomaine.parseLong(statement.dbReadNumeric(PRDemande.FIELDNAME_IDDEMANDE), "demande.dossier.id");

		idTiersRequerant = REDemandeRentePourServiceDomaine.parseLong(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_ID_TIERS_REQUERANT), "demande.requerant.id");
		nomTiersRequerant = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_NOM_REQUERANT);
		prenomTiersRequerant = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_PRENOM_REQUERANT);
		csTitreTiersRequerant = REDemandeRentePourServiceDomaine.parseInt(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_CS_TITRE_TIERS_REQUERANT), "demande.requerant.csTitre");

		sexeTiersRequerant = Sexe.parse(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_SEXE_REQUERANT));
		dateDecesTiersRequerant = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_DATE_DECES_REQUERANT));
		dateNaissanceTiersRequerant = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_DATE_NAISSANCE_REQUERANT));

		nssTiersRequerant = new NumeroSecuriteSociale(statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_NSS_REQUERANT));

		idPaysTiersRequerant = REDemandeRentePourServiceDomaine.parseLong(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_ID_PAYS_REQUERANT), "demande.requerant.pays.id");
		codeIsoPaysTiersRequerant = CodeIsoPays.parse(statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_CODE_ISO_PAYS_REQUERANT));
		traductionPaysTiersRequerantEnAllemand = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_ALLEMANDE_PAYS_REQUERANT);
		traductionPaysTiersRequerantEnFrancais = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_FRANCAISE_PAYS_REQUERANT);
		traductionPaysTiersRequerantEnItalien = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_ITALIENNE_PAYS_REQUERANT);

		String idBaseCalcul = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
		if (!JadeStringUtil.isBlankOrZero(idBaseCalcul)) {
			this.idBaseCalcul = REDemandeRentePourServiceDomaine.parseLong(idBaseCalcul, "baseCalcul.id");

			idTiersBaseCalcul = REDemandeRentePourServiceDomaine.parseLong(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_ID_TIERS_BASE_CALCUL), "baseCalcul.tiersBC.id");
			nomTiersBaseCalcul = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_NOM_BASE_CALCUL);
			prenomTiersBaseCalcul = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_PRENOM_BASE_CALCUL);
			csTitreTiersBaseCalcul = REDemandeRentePourServiceDomaine.parseInt(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_CS_TITRE_TIERS_BASE_CALCUL), "baseCalcul.tiersBC.csTitre");

			sexeTiersBaseCalcul = Sexe.parse(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_SEXE_BASE_CALCUL));
			dateDecesTiersBaseCalcul = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_DATE_DECES_BASE_CALCUL));
			dateNaissanceTiersBaseCalcul = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_DATE_NAISSANCE_BASE_CALCUL));

			nssTiersBaseCalcul = new NumeroSecuriteSociale(statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_NSS_BASE_CALCUL));

			idPaysTiersBaseCalcul = REDemandeRentePourServiceDomaine.parseLong(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_ID_PAYS_BASE_CALCUL), "baseCalcul.tiersBC.pays.id");
			codeIsoPaysTiersBaseCalcul = CodeIsoPays.parse(statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_CODE_ISO_PAYS_BASE_CALCUL));
			traductionPaysTiersBaseCalculEnAllemand = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_ALLEMANDE_PAYS_BASE_CALCUL);
			traductionPaysTiersBaseCalculEnFrancais = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_FRANCAISE_PAYS_BASE_CALCUL);
			traductionPaysTiersBaseCalculEnItalien = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_ITALIENNE_PAYS_BASE_CALCUL);

			idRenteAccordee = REDemandeRentePourServiceDomaine.parseLong(statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE), "renteAccordee.id");
			String codeCasSpecial1 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1);
			if (!JadeStringUtil.isBlankOrZero(codeCasSpecial1)) {
				this.codeCasSpecial1 = CodeCasSpecialRente.parse(codeCasSpecial1);
			}
			String codeCasSpecial2 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2);
			if (!JadeStringUtil.isBlankOrZero(codeCasSpecial2)) {
				this.codeCasSpecial2 = CodeCasSpecialRente.parse(codeCasSpecial2);
			}
			String codeCasSpecial3 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3);
			if (!JadeStringUtil.isBlankOrZero(codeCasSpecial3)) {
				this.codeCasSpecial3 = CodeCasSpecialRente.parse(codeCasSpecial3);
			}
			String codeCasSpecial4 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4);
			if (!JadeStringUtil.isBlankOrZero(codeCasSpecial4)) {
				this.codeCasSpecial4 = CodeCasSpecialRente.parse(codeCasSpecial4);
			}
			String codeCasSpecial5 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5);
			if (!JadeStringUtil.isBlankOrZero(codeCasSpecial5)) {
				this.codeCasSpecial5 = CodeCasSpecialRente.parse(codeCasSpecial5);
			}

			codePrestationRenteAccordee = CodePrestation.getCodePrestation(Integer.parseInt(statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION)));
			etatPrestationAccordee = EtatPrestationAccordee.parse(statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_CS_ETAT));
			laPrestationAccordeeEstBloquee = statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE);
			moisDebutDroitPrestationAccordee = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT));
			moisFinDroitPrestationAccordee = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));
			montantPrestationAccordee = new BigDecimal(statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION));

			idTiersBeneficiairePrestationAccordee = REDemandeRentePourServiceDomaine.parseLong(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_ID_TIERS_BENEFICIAIRE), "renteAccordee.beneficiaire.id");
			nomTiersBeneficiaire = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_NOM_BENEFICIAIRE);
			prenomTiersBeneficiaire = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_PRENOM_BENEFICIAIRE);
			csTitreTiersBeneficiaire = REDemandeRentePourServiceDomaine.parseInt(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_CS_TITRE_TIERS_BENEFICIAIRE), "renteAccordee.beneficiaire.csTitre");

			sexeTiersBeneficiaire = Sexe.parse(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_SEXE_BENEFICIAIRE));
			dateDecesTiersBeneficiaire = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_DATE_DECES_BENEFICIAIRE));
			dateNaissanceTiersBeneficiaire = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_DATE_NAISSANCE_BENEFICIAIRE));

			nssTiersBeneficiaire = new NumeroSecuriteSociale(statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_NSS_BENEFICIAIRE));

			idPaysTiersBeneficiaire = REDemandeRentePourServiceDomaine.parseLong(statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_ID_PAYS_BENEFICIAIRE), "renteAccordee.beneficiaire.pays.id");
			codeIsoPaysTiersBeneficiaire = CodeIsoPays.parse(statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_CODE_ISO_PAYS_BENEFICIAIRE));
			traductionPaysTiersBeneficiaireEnAllemand = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_ALLEMANDE_PAYS_BENEFICIAIRE);
			traductionPaysTiersBeneficiaireEnFrancais = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_FRANCAISE_PAYS_BENEFICIAIRE);
			traductionPaysTiersBeneficiaireEnItalien = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_ITALIENNE_PAYS_BENEFICIAIRE);

			String idInteretMoratoire = statement.dbReadNumeric(REInteretMoratoire.FIELDNAME_ID_INTERET_MORATOIRE);
			if (!JadeStringUtil.isBlankOrZero(idInteretMoratoire)) {
				this.idInteretMoratoire = REDemandeRentePourServiceDomaine.parseLong(idInteretMoratoire, "renteAccordee.interetMoratoire.id");
			}

			String idRepartitionCreance = statement.dbReadNumeric(RECreanceAccordee.FIELDNAME_ID_CREANCE_ACCORDEE);
			if (!JadeStringUtil.isBlankOrZero(idRepartitionCreance)) {
				this.idRepartitionCreance = REDemandeRentePourServiceDomaine.parseLong(idRepartitionCreance, "renteAccordee.repartitionCreance.id");
				montantRepartitionCreance = new BigDecimal(statement.dbReadNumeric(RECreanceAccordee.FIELDNAME_MONTANT));

				idCreance = REDemandeRentePourServiceDomaine.parseLong(statement.dbReadNumeric(RECreancier.FIELDNAME_ID_CREANCIER), "creance.id");
				montantCreance = new BigDecimal(statement.dbReadNumeric(RECreancier.FIELDNAME_MONTANT_REVANDIQUE));

				String idTiersCreancier = statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_ID_TIERS_CREANCIER);
				if (!JadeStringUtil.isBlankOrZero(idTiersCreancier)) {
					this.idTiersCreancier = REDemandeRentePourServiceDomaine.parseLong(idTiersCreancier, "creance.tiersCreancier.id");
					nomTiersCreancier = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_NOM_CREANCIER);
					prenomTiersCreancier = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_PRENOM_CREANCIER);

					String csTitreCreancier = statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_CS_TITRE_TIERS_CREANCIER);
					if (!JadeStringUtil.isBlankOrZero(csTitreCreancier)) {
						csTitreTiersCreancier = REDemandeRentePourServiceDomaine.parseInt(csTitreCreancier, "creance.tiersCreancier.csTitre");
					}

					String idPaysTiersCreancier = statement.dbReadNumeric(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_ID_PAYS_CREANCIER);
					if (!JadeStringUtil.isBlankOrZero(idPaysTiersCreancier)) {
						this.idPaysTiersCreancier = REDemandeRentePourServiceDomaine.parseLong(idPaysTiersCreancier, "creance.tiersCreancier.pays.id");
						codeIsoPaysTiersCreancier = CodeIsoPays.parse(statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_CODE_ISO_PAYS_CREANCIER));
						traductionPaysTiersCreancierEnAllemand = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_ALLEMANDE_PAYS_CREANCIER);
						traductionPaysTiersCreancierEnFrancais = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_FRANCAISE_PAYS_CREANCIER);
						traductionPaysTiersCreancierEnItalien = statement.dbReadString(REDemandeRentePourServiceDomaine.ALIAS_CHAMP_TRADUCTION_ITALIENNE_PAYS_CREANCIER);
					}
				}
			}

			String idPrestationDue = statement.dbReadNumeric(REPrestationDue.FIELDNAME_ID_PRESTATION_DUE);
			if (!JadeStringUtil.isBlankOrZero(idPrestationDue)) {
				this.idPrestationDue = Long.parseLong(idPrestationDue);
				moisDebutPrestationDue = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement.dbReadNumeric(REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT));
				moisFinPrestationDue = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement.dbReadNumeric(REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT));
				montantPrestationDue = new BigDecimal(statement.dbReadNumeric(REPrestationDue.FIELDNAME_MONTANT));
				typePrestationDue = TypePrestationDue.parse(statement.dbReadNumeric(REPrestationDue.FIELDNAME_CS_TYPE));
			}

			String idEnTeteBlocage = statement.dbReadNumeric(REEnteteBlocage.FIELDNAME_ID_ENTETE_BLOCAGE);
			if (!JadeStringUtil.isBlankOrZero(idEnTeteBlocage)) {
				this.idEnTeteBlocage = Long.parseLong(idEnTeteBlocage);
				montantDebloque = new BigDecimal(statement.dbReadNumeric(REEnteteBlocage.FIELDNAME_MONTANT_DEBLOQUE));

				String idBlocage = statement.dbReadNumeric(REPrestationAccordeeBloquee.FIELDNAME_ID_PA_BLOQUEE);
				if (!JadeStringUtil.isBlankOrZero(idBlocage)) {
					this.idBlocage = Long.parseLong(idBlocage);
					moisBlocage = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement.dbReadNumeric(REPrestationAccordeeBloquee.FIELDNAME_DATE_BLOCAGE));
					montantBlocage = new BigDecimal(statement.dbReadNumeric(REPrestationAccordeeBloquee.FIELDNAME_MONTANT));
				}
			}
		}
	}

	@Override
	protected void _validate(final BStatement statement) throws Exception {
	}

	@Override
	protected void _writePrimaryKey(final BStatement statement) throws Exception {
	}

	@Override
	protected void _writeProperties(final BStatement statement) throws Exception {
	}

	public final CodeCasSpecialRente getCodeCasSpecial1() {
		return codeCasSpecial1;
	}

	public final CodeCasSpecialRente getCodeCasSpecial2() {
		return codeCasSpecial2;
	}

	public final CodeCasSpecialRente getCodeCasSpecial3() {
		return codeCasSpecial3;
	}

	public final CodeCasSpecialRente getCodeCasSpecial4() {
		return codeCasSpecial4;
	}

	public final CodeCasSpecialRente getCodeCasSpecial5() {
		return codeCasSpecial5;
	}

	public final CodeIsoPays getCodeIsoPaysTiersBaseCalcul() {
		return codeIsoPaysTiersBaseCalcul;
	}

	public final CodeIsoPays getCodeIsoPaysTiersBeneficiaire() {
		return codeIsoPaysTiersBeneficiaire;
	}

	public final CodeIsoPays getCodeIsoPaysTiersCreancier() {
		return codeIsoPaysTiersCreancier;
	}

	public final CodeIsoPays getCodeIsoPaysTiersRequerant() {
		return codeIsoPaysTiersRequerant;
	}

	public final CodePrestation getCodePrestationRenteAccordee() {
		return codePrestationRenteAccordee;
	}

	public final Set<CodeCasSpecialRente> getCodesCasSpeciaux() {
		Set<CodeCasSpecialRente> codesCasSpeciaux = new HashSet<CodeCasSpecialRente>();

		if (codeCasSpecial1 != null) {
			codesCasSpeciaux.add(codeCasSpecial1);
		}
		if (codeCasSpecial2 != null) {
			codesCasSpeciaux.add(codeCasSpecial2);
		}
		if (codeCasSpecial3 != null) {
			codesCasSpeciaux.add(codeCasSpecial3);
		}
		if (codeCasSpecial4 != null) {
			codesCasSpeciaux.add(codeCasSpecial4);
		}
		if (codeCasSpecial5 != null) {
			codesCasSpeciaux.add(codeCasSpecial5);
		}

		return codesCasSpeciaux;
	}

	public final Integer getCsTitreTiersBaseCalcul() {
		return csTitreTiersBaseCalcul;
	}

	public final Integer getCsTitreTiersBeneficiaire() {
		return csTitreTiersBeneficiaire;
	}

	public final Integer getCsTitreTiersCreancier() {
		return csTitreTiersCreancier;
	}

	public final Integer getCsTitreTiersRequerant() {
		return csTitreTiersRequerant;
	}

	public final String getDateDebutDroitDemandeRente() {
		return dateDebutDroitDemandeRente;
	}

	public final String getDateDecesTiersBaseCalcul() {
		return dateDecesTiersBaseCalcul;
	}

	public final String getDateDecesTiersBeneficiaire() {
		return dateDecesTiersBeneficiaire;
	}

	public final String getDateDecesTiersRequerant() {
		return dateDecesTiersRequerant;
	}

	public final String getDateDepotDemandeRente() {
		return dateDepotDemandeRente;
	}

	public final String getDateFinDroitDemandeRente() {
		return dateFinDroitDemandeRente;
	}

	public final String getDateNaissanceTiersBaseCalcul() {
		return dateNaissanceTiersBaseCalcul;
	}

	public final String getDateNaissanceTiersBeneficiaire() {
		return dateNaissanceTiersBeneficiaire;
	}

	public final String getDateNaissanceTiersRequerant() {
		return dateNaissanceTiersRequerant;
	}

	public final String getDateReceptionDemandeRente() {
		return dateReceptionDemandeRente;
	}

	public final String getDateTraitementDemandeRente() {
		return dateTraitementDemandeRente;
	}

	public final EtatDemandeRente getEtatDemandeRente() {
		return etatDemandeRente;
	}

	public final EtatPrestationAccordee getEtatPrestationAccordee() {
		return etatPrestationAccordee;
	}

	public final String getGestionnaireDemandeRente() {
		return gestionnaireDemandeRente;
	}

	public final Long getIdBaseCalcul() {
		return idBaseCalcul;
	}

	public final Long getIdBlocage() {
		return idBlocage;
	}

	public final Long getIdCreance() {
		return idCreance;
	}

	public final Long getIdDemandePrestation() {
		return idDemandePrestation;
	}

	public final Long getIdDemandeRente() {
		return idDemandeRente;
	}

	public final Long getIdEnTeteBlocage() {
		return idEnTeteBlocage;
	}

	public final Long getIdInfoComplementaire() {
		return idInformationComplementaire;
	}

	public final Long getIdInformationComplementaire() {
		return idInformationComplementaire;
	}

	public final Long getIdInteretMoratoire() {
		return idInteretMoratoire;
	}

	public final Long getIdPaysTiersBaseCalcul() {
		return idPaysTiersBaseCalcul;
	}

	public final Long getIdPaysTiersBeneficiaire() {
		return idPaysTiersBeneficiaire;
	}

	public final Long getIdPaysTiersCreancier() {
		return idPaysTiersCreancier;
	}

	public final Long getIdPaysTiersRequerant() {
		return idPaysTiersRequerant;
	}

	public final Long getIdPrestationDue() {
		return idPrestationDue;
	}

	public final Long getIdRenteAccordee() {
		return idRenteAccordee;
	}

	public final Long getIdRepartitionCreance() {
		return idRepartitionCreance;
	}

	public final Long getIdTiersBaseCalcul() {
		return idTiersBaseCalcul;
	}

	public final Long getIdTiersBeneficiairePrestationAccordee() {
		return idTiersBeneficiairePrestationAccordee;
	}

	public final Long getIdTiersCreancier() {
		return idTiersCreancier;
	}

	public final Long getIdTiersRequerant() {
		return idTiersRequerant;
	}

	public final String getMoisBlocage() {
		return moisBlocage;
	}

	public final String getMoisDebutDroitPrestationAccordee() {
		return moisDebutDroitPrestationAccordee;
	}

	public final String getMoisDebutPrestationDue() {
		return moisDebutPrestationDue;
	}

	public final String getMoisFinDroitPrestationAccordee() {
		return moisFinDroitPrestationAccordee;
	}

	public final String getMoisFinPrestationDue() {
		return moisFinPrestationDue;
	}

	public final BigDecimal getMontantBlocage() {
		return montantBlocage;
	}

	public final BigDecimal getMontantCreance() {
		return montantCreance;
	}

	public final BigDecimal getMontantDebloque() {
		return montantDebloque;
	}

	public final BigDecimal getMontantPrestationAccordee() {
		return montantPrestationAccordee;
	}

	public final BigDecimal getMontantPrestationDue() {
		return montantPrestationDue;
	}

	public final BigDecimal getMontantRepartitionCreance() {
		return montantRepartitionCreance;
	}

	public final String getNomTiersBaseCalcul() {
		return nomTiersBaseCalcul;
	}

	public final String getNomTiersBeneficiaire() {
		return nomTiersBeneficiaire;
	}

	public final String getNomTiersCreancier() {
		return nomTiersCreancier;
	}

	public final String getNomTiersRequerant() {
		return nomTiersRequerant;
	}

	public final NumeroSecuriteSociale getNssTiersBaseCalcul() {
		return nssTiersBaseCalcul;
	}

	public final NumeroSecuriteSociale getNssTiersBeneficiaire() {
		return nssTiersBeneficiaire;
	}

	public final NumeroSecuriteSociale getNssTiersRequerant() {
		return nssTiersRequerant;
	}

	public final String getPrenomTiersBaseCalcul() {
		return prenomTiersBaseCalcul;
	}

	public final String getPrenomTiersBeneficiaire() {
		return prenomTiersBeneficiaire;
	}

	public final String getPrenomTiersCreancier() {
		return prenomTiersCreancier;
	}

	public final String getPrenomTiersRequerant() {
		return prenomTiersRequerant;
	}

	public final Sexe getSexeTiersBaseCalcul() {
		return sexeTiersBaseCalcul;
	}

	public final Sexe getSexeTiersBeneficiaire() {
		return sexeTiersBeneficiaire;
	}

	public final Sexe getSexeTiersRequerant() {
		return sexeTiersRequerant;
	}

	public final String getTraductionPaysTiersBaseCalculEnAllemand() {
		return traductionPaysTiersBaseCalculEnAllemand;
	}

	public final String getTraductionPaysTiersBaseCalculEnFrancais() {
		return traductionPaysTiersBaseCalculEnFrancais;
	}

	public final String getTraductionPaysTiersBaseCalculEnItalien() {
		return traductionPaysTiersBaseCalculEnItalien;
	}

	public final String getTraductionPaysTiersBeneficiaireEnAllemand() {
		return traductionPaysTiersBeneficiaireEnAllemand;
	}

	public final String getTraductionPaysTiersBeneficiaireEnFrancais() {
		return traductionPaysTiersBeneficiaireEnFrancais;
	}

	public final String getTraductionPaysTiersBeneficiaireEnItalien() {
		return traductionPaysTiersBeneficiaireEnItalien;
	}

	public final String getTraductionPaysTiersCreancierEnAllemand() {
		return traductionPaysTiersCreancierEnAllemand;
	}

	public final String getTraductionPaysTiersCreancierEnFrancais() {
		return traductionPaysTiersCreancierEnFrancais;
	}

	public final String getTraductionPaysTiersCreancierEnItalien() {
		return traductionPaysTiersCreancierEnItalien;
	}

	public final String getTraductionPaysTiersRequerantEnAllemand() {
		return traductionPaysTiersRequerantEnAllemand;
	}

	public final String getTraductionPaysTiersRequerantEnFrancais() {
		return traductionPaysTiersRequerantEnFrancais;
	}

	public final String getTraductionPaysTiersRequerantEnItalien() {
		return traductionPaysTiersRequerantEnItalien;
	}

	public final Map<Langues, String> getTraductionsPaysTiersBaseCalcul() {
		Map<Langues, String> traductions = new HashMap<Langues, String>();

		if (!JadeStringUtil.isBlank(traductionPaysTiersBaseCalculEnAllemand)) {
			traductions.put(Langues.Allemand, traductionPaysTiersBaseCalculEnAllemand);
		}
		if (!JadeStringUtil.isBlank(traductionPaysTiersBaseCalculEnFrancais)) {
			traductions.put(Langues.Francais, traductionPaysTiersBaseCalculEnFrancais);
		}
		if (!JadeStringUtil.isBlank(traductionPaysTiersBaseCalculEnItalien)) {
			traductions.put(Langues.Italien, traductionPaysTiersBaseCalculEnItalien);
		}

		return traductions;
	}

	public final Map<Langues, String> getTraductionsPaysTiersBeneficiaire() {
		Map<Langues, String> traductions = new HashMap<Langues, String>();

		if (!JadeStringUtil.isBlank(traductionPaysTiersBeneficiaireEnAllemand)) {
			traductions.put(Langues.Allemand, traductionPaysTiersBeneficiaireEnAllemand);
		}
		if (!JadeStringUtil.isBlank(traductionPaysTiersBeneficiaireEnFrancais)) {
			traductions.put(Langues.Francais, traductionPaysTiersBeneficiaireEnFrancais);
		}
		if (!JadeStringUtil.isBlank(traductionPaysTiersBeneficiaireEnItalien)) {
			traductions.put(Langues.Italien, traductionPaysTiersBeneficiaireEnItalien);
		}

		return traductions;
	}

	public final Map<Langues, String> getTraductionsPaysTiersCreancier() {
		Map<Langues, String> traductions = new HashMap<Langues, String>();

		if (!JadeStringUtil.isBlank(traductionPaysTiersCreancierEnAllemand)) {
			traductions.put(Langues.Allemand, traductionPaysTiersCreancierEnAllemand);
		}
		if (!JadeStringUtil.isBlank(traductionPaysTiersCreancierEnFrancais)) {
			traductions.put(Langues.Francais, traductionPaysTiersCreancierEnFrancais);
		}
		if (!JadeStringUtil.isBlank(traductionPaysTiersCreancierEnItalien)) {
			traductions.put(Langues.Italien, traductionPaysTiersCreancierEnItalien);
		}

		return traductions;
	}

	public final Map<Langues, String> getTraductionsPaysTiersRequerant() {
		Map<Langues, String> traductions = new HashMap<Langues, String>();

		if (!JadeStringUtil.isBlank(traductionPaysTiersRequerantEnAllemand)) {
			traductions.put(Langues.Allemand, traductionPaysTiersRequerantEnAllemand);
		}
		if (!JadeStringUtil.isBlank(traductionPaysTiersRequerantEnFrancais)) {
			traductions.put(Langues.Francais, traductionPaysTiersRequerantEnFrancais);
		}
		if (!JadeStringUtil.isBlank(traductionPaysTiersRequerantEnItalien)) {
			traductions.put(Langues.Italien, traductionPaysTiersRequerantEnItalien);
		}

		return traductions;
	}

	public final TypeCalculDemandeRente getTypeCalculDemandeRente() {
		return typeCalculDemandeRente;
	}

	public final TypeDemandeRente getTypeDemandeRente() {
		return typeDemandeRente;
	}

	public final TypePrestationDue getTypePrestationDue() {
		return typePrestationDue;
	}

	public final boolean laPrestationAccordeeEstBloquee() {
		return laPrestationAccordeeEstBloquee;
	}
}
// @formatter:on