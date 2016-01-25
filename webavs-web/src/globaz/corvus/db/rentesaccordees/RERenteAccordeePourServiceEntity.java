package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.creances.RECreanceAccordee;
import globaz.corvus.db.creances.RECreancier;
import globaz.corvus.db.interetsmoratoires.RECalculInteretMoratoire;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
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
import ch.globaz.corvus.business.services.models.rentesaccordees.RenteAccordeeCrudService;
import ch.globaz.corvus.domaine.constantes.CodeCasSpecialRente;
import ch.globaz.corvus.domaine.constantes.TypePrestationDue;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.prestation.domaine.constantes.EtatPrestationAccordee;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;
import ch.globaz.pyxis.domaine.Sexe;
import ch.globaz.pyxis.domaine.constantes.CodeIsoPays;

/**
 * Utilisé par le service de CRUD {@link RenteAccordeeCrudService}
 */
public final class RERenteAccordeePourServiceEntity extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ALIAS_CHAMP_CODE_ISO_PAYS_TIERS_BENEFICIAIRE = "pays_benef_iso";
    private static final String ALIAS_CHAMP_CODE_ISO_PAYS_TIERS_CREANCIER = "pays_crean_iso";
    private static final String ALIAS_CHAMP_CS_TITRE_TIERS_BENEFICIAIRE = "ti_benef_titre";
    private static final String ALIAS_CHAMP_DESIGNATION_1_TIERS_BENEFICIAIRE = "ti_benef_d1";
    private static final String ALIAS_CHAMP_DESIGNATION_1_TIERS_CREANCIER = "ti_crean_d1";
    private static final String ALIAS_CHAMP_DESIGNATION_2_TIERS_BENEFICIAIRE = "ti_benef_d2";
    private static final String ALIAS_CHAMP_DESIGNATION_2_TIERS_CREANCIER = "ti_crean_d2";
    private static final String ALIAS_CHAMP_ID_PAYS_TIERS_BENEFICIAIRE = "pays_benef_id";
    private static final String ALIAS_CHAMP_ID_PAYS_TIERS_CREANCIER = "pays_crean_id";
    private static final String ALIAS_CHAMP_ID_TIERS_BENEFICIAIRE = "ti_benef_id";
    private static final String ALIAS_CHAMP_ID_TIERS_CREANCIER = "ti_crean_id";
    private static final String ALIAS_CHAMP_LIBELLE_DE_TIERS_BENEFICIAIRE = "pays_benef_de";
    private static final String ALIAS_CHAMP_LIBELLE_DE_TIERS_CREANCIER = "pays_crean_de";
    private static final String ALIAS_CHAMP_LIBELLE_FR_TIERS_BENEFICIAIRE = "pays_benef_fr";
    private static final String ALIAS_CHAMP_LIBELLE_FR_TIERS_CREANCIER = "pays_crean_fr";
    private static final String ALIAS_CHAMP_LIBELLE_IT_TIERS_BENEFICIAIRE = "pays_benef_it";
    private static final String ALIAS_CHAMP_LIBELLE_IT_TIERS_CREANCIER = "pays_crean_it";
    private static final String ALIAS_TABLE_PAYS_BENEFICIAIRE = "pays_benef";
    private static final String ALIAS_TABLE_PAYS_CREANCIER = "pays_crean";
    private static final String ALIAS_TABLE_TIERS_BENEFICIAIRE = "ti_benef";
    private static final String ALIAS_TABLE_TIERS_CREANCIER = "ti_crean";

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

    private CodeCasSpecialRente codeCasSpecial01;
    private CodeCasSpecialRente codeCasSpecial02;
    private CodeCasSpecialRente codeCasSpecial03;
    private CodeCasSpecialRente codeCasSpecial04;
    private CodeCasSpecialRente codeCasSpecial05;
    private CodeIsoPays codeIsoPaysTiersBeneficiaire;
    private CodeIsoPays codeIsoPaysTiersCreancier;
    private CodePrestation codePrestation;
    private Integer csTitreTiersBeneficiaire;
    private String dateDecesTiersBeneficiaire;
    private String dateNaissanceTiersBeneficiaire;
    private EtatPrestationAccordee etatPrestationAccordee;
    private Long idBlocage;
    private Long idCompteAnnexePrestationAccordee;
    private Long idCreance;
    private Long idEnTeteBlocage;
    private Long idInteretMoratoire;
    private Long idPaysTiersBeneficiaire;
    private Long idPaysTiersCreancier;
    private Long idPrestationDue;
    private Long idRenteAccordee;
    private Long idRepartitionCreance;
    private Long idTiersAdresseDePaiement;
    private Long idTiersBeneficiaire;
    private Long idTiersCreancier;
    private boolean laPrestationEstBloquee;
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
    private String nomTiersBeneficiaire;
    private String nomTiersCreancier;
    private NumeroSecuriteSociale nssTiersBeneficiaire;
    private String prenomTiersBeneficiaire;
    private String prenomTiersCreancier;
    private String referencePourLePaiementPrestationAccordee;
    private Sexe sexeTiersBeneficiarie;
    private String traductionPaysTiersBeneficiaireEnAllemand;
    private String traductionPaysTiersBeneficiaireEnFrancais;
    private String traductionPaysTiersBeneficiaireEnItalien;
    private String traductionPaysTiersCreancierEnAllemand;
    private String traductionPaysTiersCreancierEnFrancais;
    private String traductionPaysTiersCreancierEnItalien;
    private TypePrestationDue typePrestationDue;

    public RERenteAccordeePourServiceEntity() {
        super();

        codeCasSpecial01 = null;
        codeCasSpecial02 = null;
        codeCasSpecial03 = null;
        codeCasSpecial04 = null;
        codeCasSpecial05 = null;
        codeIsoPaysTiersBeneficiaire = null;
        codeIsoPaysTiersCreancier = null;
        codePrestation = null;
        csTitreTiersBeneficiaire = null;
        dateDecesTiersBeneficiaire = null;
        dateNaissanceTiersBeneficiaire = null;
        etatPrestationAccordee = null;
        idBlocage = null;
        idCompteAnnexePrestationAccordee = null;
        idCreance = null;
        idEnTeteBlocage = null;
        idInteretMoratoire = null;
        idPaysTiersBeneficiaire = null;
        idPaysTiersCreancier = null;
        idPrestationDue = null;
        idRenteAccordee = null;
        idRepartitionCreance = null;
        idTiersAdresseDePaiement = null;
        idTiersBeneficiaire = null;
        idTiersCreancier = null;
        laPrestationEstBloquee = false;
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
        nssTiersBeneficiaire = null;
        prenomTiersBeneficiaire = null;
        prenomTiersCreancier = null;
        sexeTiersBeneficiarie = null;
        traductionPaysTiersBeneficiaireEnAllemand = null;
        traductionPaysTiersBeneficiaireEnFrancais = null;
        traductionPaysTiersBeneficiaireEnItalien = null;
        traductionPaysTiersCreancierEnAllemand = null;
        traductionPaysTiersCreancierEnFrancais = null;
        traductionPaysTiersCreancierEnItalien = null;
        typePrestationDue = null;
    }

    @Override
    protected String _getFields(final BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableRenteAccordee = _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableInfoComptabilite = _getCollection() + REInformationsComptabilite.TABLE_NAME_INFO_COMPTA;
        String tablePersonne = _getCollection() + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;
        String tableRepartitionCreance = _getCollection() + RECreanceAccordee.TABLE_NAME_CREANCES_ACCORDEES;
        String tableCreance = _getCollection() + RECreancier.TABLE_NAME_CREANCIER;
        String tableInteretMoratoire = _getCollection() + RECalculInteretMoratoire.TABLE_NAME_CALCUL_INTERET_MORATOIRE;
        String tablePrestationDue = _getCollection() + REPrestationDue.TABLE_NAME_PRESTATIONS_DUES;
        String tableEnTeteBlocage = _getCollection() + REEnteteBlocage.TABLE_NAME_ENTETE_BLOCAGE;
        String tableBlocage = _getCollection() + REPrestationAccordeeBloquee.TABLE_NAME_PRESTATION_ACCORDEE_BLOQUEE;

        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE).append(",");
        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1).append(",");
        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2).append(",");
        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3).append(",");
        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4).append(",");
        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5).append(",");

        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_REFERENCE_PMT)
                .append(",");

        sql.append(tableInfoComptabilite).append(".").append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT)
                .append(",");
        sql.append(tableInfoComptabilite).append(".").append(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE)
                .append(",");

        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.ID_TIERS).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_ID_TIERS_BENEFICIAIRE).append(",");
        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.CS_TITRE_TIERS).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_CS_TITRE_TIERS_BENEFICIAIRE).append(",");
        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.DESIGNATION_1).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_DESIGNATION_1_TIERS_BENEFICIAIRE).append(",");
        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.DESIGNATION_2).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_DESIGNATION_2_TIERS_BENEFICIAIRE).append(",");

        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_PAYS_BENEFICIAIRE).append(".")
                .append(ITIPaysDefTable.ID_PAYS).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_ID_PAYS_TIERS_BENEFICIAIRE).append(",");
        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_PAYS_BENEFICIAIRE).append(".")
                .append(ITIPaysDefTable.CODE_ISO).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_CODE_ISO_PAYS_TIERS_BENEFICIAIRE).append(",");
        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_PAYS_BENEFICIAIRE).append(".")
                .append(ITIPaysDefTable.LIBELLE_DE).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_LIBELLE_DE_TIERS_BENEFICIAIRE).append(",");
        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_PAYS_BENEFICIAIRE).append(".")
                .append(ITIPaysDefTable.LIBELLE_FR).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_LIBELLE_FR_TIERS_BENEFICIAIRE).append(",");
        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_PAYS_BENEFICIAIRE).append(".")
                .append(ITIPaysDefTable.LIBELLE_IT).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_LIBELLE_IT_TIERS_BENEFICIAIRE).append(",");

        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.CS_SEXE).append(",");
        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.DATE_DECES).append(",");
        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.DATE_NAISSANCE).append(",");

        sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(",");

        sql.append(tableRepartitionCreance).append(".").append(RECreanceAccordee.FIELDNAME_ID_CREANCE_ACCORDEE)
                .append(",");
        sql.append(tableRepartitionCreance).append(".").append(RECreanceAccordee.FIELDNAME_MONTANT).append(",");

        sql.append(tableCreance).append(".").append(RECreancier.FIELDNAME_ID_CREANCIER).append(",");
        sql.append(tableCreance).append(".").append(RECreancier.FIELDNAME_MONTANT_REVANDIQUE).append(",");

        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.ID_TIERS).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_ID_TIERS_CREANCIER).append(",");
        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.DESIGNATION_1).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_DESIGNATION_1_TIERS_CREANCIER).append(",");
        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.DESIGNATION_2).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_DESIGNATION_2_TIERS_CREANCIER).append(",");

        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_PAYS_CREANCIER).append(".")
                .append(ITIPaysDefTable.ID_PAYS).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_ID_PAYS_TIERS_CREANCIER).append(",");
        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_PAYS_CREANCIER).append(".")
                .append(ITIPaysDefTable.CODE_ISO).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_CODE_ISO_PAYS_TIERS_CREANCIER).append(",");
        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_PAYS_CREANCIER).append(".")
                .append(ITIPaysDefTable.LIBELLE_DE).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_LIBELLE_DE_TIERS_CREANCIER).append(",");
        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_PAYS_CREANCIER).append(".")
                .append(ITIPaysDefTable.LIBELLE_FR).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_LIBELLE_FR_TIERS_CREANCIER).append(",");
        sql.append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_PAYS_CREANCIER).append(".")
                .append(ITIPaysDefTable.LIBELLE_IT).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_LIBELLE_IT_TIERS_CREANCIER).append(",");

        sql.append(tableInteretMoratoire).append(".")
                .append(RECalculInteretMoratoire.FIELDNAME_ID_CALCUL_INTERET_MORATOIRE).append(",");

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

        String tableRenteAccordee = _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableInfoComptabilite = _getCollection() + REInformationsComptabilite.TABLE_NAME_INFO_COMPTA;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePays = _getCollection() + ITIPaysDefTable.TABLE_NAME;
        String tablePersonne = _getCollection() + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;
        String tableRepartitionCreance = _getCollection() + RECreanceAccordee.TABLE_NAME_CREANCES_ACCORDEES;
        String tableCreance = _getCollection() + RECreancier.TABLE_NAME_CREANCIER;
        String tableInteretMoratoire = _getCollection() + RECalculInteretMoratoire.TABLE_NAME_CALCUL_INTERET_MORATOIRE;
        String tablePrestationDue = _getCollection() + REPrestationDue.TABLE_NAME_PRESTATIONS_DUES;
        String tableEnTeteBlocage = _getCollection() + REEnteteBlocage.TABLE_NAME_ENTETE_BLOCAGE;
        String tableBlocage = _getCollection() + REPrestationAccordeeBloquee.TABLE_NAME_PRESTATION_ACCORDEE_BLOQUEE;

        sql.append(tableRenteAccordee);

        sql.append(" INNER JOIN ").append(tablePrestationAccordee);
        sql.append(" ON ").append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE)
                .append("=").append(tablePrestationAccordee).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        sql.append(" INNER JOIN ").append(tableInfoComptabilite);
        sql.append(" ON ").append(tablePrestationAccordee).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA).append("=").append(tableInfoComptabilite)
                .append(".").append(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);

        sql.append(" INNER JOIN ").append(tableTiers).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_TIERS_BENEFICIAIRE);
        sql.append(" ON ").append(tablePrestationAccordee).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append("=")
                .append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.ID_TIERS);

        sql.append(" LEFT OUTER JOIN ").append(tablePays).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_PAYS_BENEFICIAIRE);
        sql.append(" ON ").append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.ID_PAYS).append("=")
                .append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_PAYS_BENEFICIAIRE).append(".")
                .append(ITIPaysDefTable.ID_PAYS);

        sql.append(" INNER JOIN ").append(tablePersonne);
        sql.append(" ON ").append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.ID_TIERS).append("=").append(tablePersonne).append(".")
                .append(ITIPersonneDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonneAvs);
        sql.append(" ON ").append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS).append("=")
                .append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

        sql.append(" LEFT OUTER JOIN ").append(tableRepartitionCreance);
        sql.append(" ON ").append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE)
                .append("=").append(tableRepartitionCreance).append(".")
                .append(RECreanceAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

        sql.append(" LEFT OUTER JOIN ").append(tableCreance);
        sql.append(" ON ").append(tableRepartitionCreance).append(".").append(RECreanceAccordee.FIELDNAME_ID_CREANCIER)
                .append("=").append(tableCreance).append(".").append(RECreancier.FIELDNAME_ID_CREANCIER);

        sql.append(" LEFT OUTER JOIN ").append(tableTiers).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_TIERS_CREANCIER);
        sql.append(" ON ").append(tableCreance).append(".").append(RECreancier.FIELDNAME_ID_TIERS).append("=")
                .append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_TIERS_CREANCIER).append(".")
                .append(ITITiersDefTable.ID_TIERS);

        sql.append(" LEFT OUTER JOIN ").append(tablePays).append(" AS ")
                .append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_PAYS_CREANCIER);
        sql.append(" ON ").append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_TIERS_CREANCIER).append(".")
                .append(ITITiersDefTable.ID_PAYS).append("=")
                .append(RERenteAccordeePourServiceEntity.ALIAS_TABLE_PAYS_CREANCIER).append(".")
                .append(ITIPaysDefTable.ID_PAYS);

        sql.append(" LEFT OUTER JOIN ").append(tableInteretMoratoire);
        sql.append(" ON ").append(tablePrestationAccordee).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_CALCUL_INTERET_MORATOIRE).append("=")
                .append(tableInteretMoratoire).append(".")
                .append(RECalculInteretMoratoire.FIELDNAME_ID_CALCUL_INTERET_MORATOIRE);

        sql.append(" LEFT OUTER JOIN ").append(tablePrestationDue);
        sql.append(" ON ").append(tablePrestationAccordee).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append("=").append(tablePrestationDue)
                .append(".").append(REPrestationDue.FIELDNAME_ID_RENTE_ACCORDEE);

        sql.append(" LEFT OUTER JOIN ").append(tableEnTeteBlocage);
        sql.append(" ON ").append(tablePrestationAccordee).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_ENTETE_BLOCAGE).append("=").append(tableEnTeteBlocage)
                .append(".").append(REEnteteBlocage.FIELDNAME_ID_ENTETE_BLOCAGE);

        sql.append(" LEFT OUTER JOIN ").append(tableBlocage);
        sql.append(" ON ").append(tableEnTeteBlocage).append(".").append(REEnteteBlocage.FIELDNAME_ID_ENTETE_BLOCAGE)
                .append("=").append(tableBlocage).append(".")
                .append(REPrestationAccordeeBloquee.FIELDNAME_ID_ENTETE_BLOCAGE);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
    }

    @Override
    protected void _readProperties(final BStatement statement) throws Exception {

        idRenteAccordee = RERenteAccordeePourServiceEntity.parseLong(
                statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE), "renteAccordee.id");
        String codeCasSpecial01 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1);
        if (!JadeStringUtil.isBlank(codeCasSpecial01)) {
            this.codeCasSpecial01 = CodeCasSpecialRente.parse(codeCasSpecial01);
        }
        String codeCasSpecial02 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2);
        if (!JadeStringUtil.isBlank(codeCasSpecial02)) {
            this.codeCasSpecial02 = CodeCasSpecialRente.parse(codeCasSpecial02);
        }
        String codeCasSpecial03 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3);
        if (!JadeStringUtil.isBlank(codeCasSpecial03)) {
            this.codeCasSpecial03 = CodeCasSpecialRente.parse(codeCasSpecial03);
        }
        String codeCasSpecial04 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4);
        if (!JadeStringUtil.isBlank(codeCasSpecial04)) {
            this.codeCasSpecial04 = CodeCasSpecialRente.parse(codeCasSpecial04);
        }
        String codeCasSpecial05 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5);
        if (!JadeStringUtil.isBlank(codeCasSpecial05)) {
            this.codeCasSpecial05 = CodeCasSpecialRente.parse(codeCasSpecial05);
        }

        codePrestation = CodePrestation.getCodePrestation(RERenteAccordeePourServiceEntity.parseInt(
                statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION),
                "renteAccordee.codePrestation"));
        etatPrestationAccordee = EtatPrestationAccordee.parse(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_CS_ETAT));
        laPrestationEstBloquee = statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE);
        moisDebutDroitPrestationAccordee = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT));
        moisFinDroitPrestationAccordee = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));
        montantPrestationAccordee = new BigDecimal(
                statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION));
        referencePourLePaiementPrestationAccordee = statement
                .dbReadString(REPrestationsAccordees.FIELDNAME_REFERENCE_PMT);

        idCompteAnnexePrestationAccordee = RERenteAccordeePourServiceEntity.parseLong(
                statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE),
                "renteAccordee.informationComptable.compteAnnexe.id");
        idTiersAdresseDePaiement = RERenteAccordeePourServiceEntity.parseLong(
                statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT),
                "renteAccordee.informationComptable.adressePaiement.id");

        idTiersBeneficiaire = RERenteAccordeePourServiceEntity.parseLong(
                statement.dbReadNumeric(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_ID_TIERS_BENEFICIAIRE),
                "renteAccordee.beneficiaire.id");
        csTitreTiersBeneficiaire = RERenteAccordeePourServiceEntity.parseInt(
                statement.dbReadNumeric(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_CS_TITRE_TIERS_BENEFICIAIRE),
                "renteAccordee.beneficiaire.csTitre");
        nomTiersBeneficiaire = statement
                .dbReadString(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_DESIGNATION_1_TIERS_BENEFICIAIRE);
        prenomTiersBeneficiaire = statement
                .dbReadString(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_DESIGNATION_2_TIERS_BENEFICIAIRE);

        idPaysTiersBeneficiaire = RERenteAccordeePourServiceEntity.parseLong(
                statement.dbReadNumeric(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_ID_PAYS_TIERS_BENEFICIAIRE),
                "renteAccordee.beneficiaire.pays.id");
        codeIsoPaysTiersBeneficiaire = CodeIsoPays.parse(statement
                .dbReadString(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_CODE_ISO_PAYS_TIERS_BENEFICIAIRE));
        traductionPaysTiersBeneficiaireEnAllemand = statement
                .dbReadString(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_LIBELLE_DE_TIERS_BENEFICIAIRE);
        traductionPaysTiersBeneficiaireEnFrancais = statement
                .dbReadString(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_LIBELLE_FR_TIERS_BENEFICIAIRE);
        traductionPaysTiersBeneficiaireEnItalien = statement
                .dbReadString(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_LIBELLE_IT_TIERS_BENEFICIAIRE);

        sexeTiersBeneficiarie = Sexe.parse(statement.dbReadNumeric(ITIPersonneDefTable.CS_SEXE));
        dateDecesTiersBeneficiaire = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadNumeric(ITIPersonneDefTable.DATE_DECES));
        dateNaissanceTiersBeneficiaire = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadNumeric(ITIPersonneDefTable.DATE_NAISSANCE));

        nssTiersBeneficiaire = new NumeroSecuriteSociale(
                statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL));

        String idRepartitionCreance = statement.dbReadNumeric(RECreanceAccordee.FIELDNAME_ID_CREANCE_ACCORDEE);
        if (!JadeStringUtil.isBlankOrZero(idRepartitionCreance)) {
            this.idRepartitionCreance = RERenteAccordeePourServiceEntity.parseLong(idRepartitionCreance,
                    "renteAccordee.repartitionCreance.id");
            montantRepartitionCreance = new BigDecimal(statement.dbReadNumeric(RECreanceAccordee.FIELDNAME_MONTANT));

            idCreance = RERenteAccordeePourServiceEntity.parseLong(
                    statement.dbReadNumeric(RECreancier.FIELDNAME_ID_CREANCIER), "creance.id");
            montantCreance = new BigDecimal(statement.dbReadNumeric(RECreancier.FIELDNAME_MONTANT_REVANDIQUE));

            idTiersCreancier = RERenteAccordeePourServiceEntity.parseLong(
                    statement.dbReadNumeric(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_ID_TIERS_CREANCIER),
                    "creance.tiersCreancier.id");
            nomTiersCreancier = statement
                    .dbReadString(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_DESIGNATION_1_TIERS_CREANCIER);
            prenomTiersCreancier = statement
                    .dbReadString(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_DESIGNATION_2_TIERS_CREANCIER);

            String idPaysTiersCreancier = statement
                    .dbReadNumeric(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_ID_PAYS_TIERS_CREANCIER);
            if (!JadeStringUtil.isBlankOrZero(idPaysTiersCreancier)) {
                this.idPaysTiersCreancier = RERenteAccordeePourServiceEntity.parseLong(idPaysTiersCreancier,
                        "creance.tiersCreancier.pays.id");
                codeIsoPaysTiersCreancier = CodeIsoPays.parse(statement
                        .dbReadString(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_CODE_ISO_PAYS_TIERS_CREANCIER));
                traductionPaysTiersCreancierEnAllemand = statement
                        .dbReadString(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_LIBELLE_DE_TIERS_CREANCIER);
                traductionPaysTiersCreancierEnFrancais = statement
                        .dbReadString(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_LIBELLE_FR_TIERS_CREANCIER);
                traductionPaysTiersCreancierEnItalien = statement
                        .dbReadString(RERenteAccordeePourServiceEntity.ALIAS_CHAMP_LIBELLE_IT_TIERS_CREANCIER);
            }
        }

        String idInteretMoratoire = statement
                .dbReadNumeric(RECalculInteretMoratoire.FIELDNAME_ID_CALCUL_INTERET_MORATOIRE);
        if (!JadeStringUtil.isBlankOrZero(idInteretMoratoire)) {
            this.idInteretMoratoire = RERenteAccordeePourServiceEntity.parseLong(idInteretMoratoire,
                    "renteAccordee.interetMoratoire.id");
        }

        String idPrestationDue = statement.dbReadNumeric(REPrestationDue.FIELDNAME_ID_PRESTATION_DUE);
        if (!JadeStringUtil.isBlankOrZero(idPrestationDue)) {
            this.idPrestationDue = Long.parseLong(idPrestationDue);
            moisDebutPrestationDue = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                    .dbReadNumeric(REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT));
            moisFinPrestationDue = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                    .dbReadNumeric(REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT));
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
                moisBlocage = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                        .dbReadNumeric(REPrestationAccordeeBloquee.FIELDNAME_DATE_BLOCAGE));
                montantBlocage = new BigDecimal(statement.dbReadNumeric(REPrestationAccordeeBloquee.FIELDNAME_MONTANT));
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

    public final CodeIsoPays getCodeIsoPaysTiersBeneficiaire() {
        return codeIsoPaysTiersBeneficiaire;
    }

    public final CodeIsoPays getCodeIsoPaysTiersCreancier() {
        return codeIsoPaysTiersCreancier;
    }

    public final CodePrestation getCodePrestation() {
        return codePrestation;
    }

    public final Set<CodeCasSpecialRente> getCodesCasSpeciaux() {
        Set<CodeCasSpecialRente> codesCasSpeciaux = new HashSet<CodeCasSpecialRente>();

        if (codeCasSpecial01 != null) {
            codesCasSpeciaux.add(codeCasSpecial01);
        }
        if (codeCasSpecial02 != null) {
            codesCasSpeciaux.add(codeCasSpecial02);
        }
        if (codeCasSpecial03 != null) {
            codesCasSpeciaux.add(codeCasSpecial03);
        }
        if (codeCasSpecial04 != null) {
            codesCasSpeciaux.add(codeCasSpecial04);
        }
        if (codeCasSpecial05 != null) {
            codesCasSpeciaux.add(codeCasSpecial05);
        }

        return codesCasSpeciaux;
    }

    public Integer getCsTitreTiersBeneficiaire() {
        return csTitreTiersBeneficiaire;
    }

    public final String getDateDecesTiersBeneficiaire() {
        return dateDecesTiersBeneficiaire;
    }

    public final String getDateNaissanceTiersBeneficiaire() {
        return dateNaissanceTiersBeneficiaire;
    }

    public final EtatPrestationAccordee getEtatPrestationAccordee() {
        return etatPrestationAccordee;
    }

    public final Long getIdBlocage() {
        return idBlocage;
    }

    public final Long getIdCompteAnnexePrestationAccordee() {
        return idCompteAnnexePrestationAccordee;
    }

    public final Long getIdCreance() {
        return idCreance;
    }

    public final Long getIdEnTeteBlocage() {
        return idEnTeteBlocage;
    }

    public final Long getIdInteretMoratoire() {
        return idInteretMoratoire;
    }

    public final Long getIdPaysTiersBeneficiaire() {
        return idPaysTiersBeneficiaire;
    }

    public final Long getIdPaysTiersCreancier() {
        return idPaysTiersCreancier;
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

    public final Long getIdTiersAdresseDePaiement() {
        return idTiersAdresseDePaiement;
    }

    public final Long getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public final Long getIdTiersCreancier() {
        return idTiersCreancier;
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

    public final String getNomTiersBeneficiaire() {
        return nomTiersBeneficiaire;
    }

    public final String getNomTiersCreancier() {
        return nomTiersCreancier;
    }

    public final NumeroSecuriteSociale getNssTiersBeneficiaire() {
        return nssTiersBeneficiaire;
    }

    public final String getPrenomTiersBeneficiaire() {
        return prenomTiersBeneficiaire;
    }

    public final String getPrenomTiersCreancier() {
        return prenomTiersCreancier;
    }

    public String getReferencePourLePaiementPrestationAccordee() {
        return referencePourLePaiementPrestationAccordee;
    }

    public final Sexe getSexeTiersBeneficiarie() {
        return sexeTiersBeneficiarie;
    }

    public Map<Langues, String> getTraductionPaysBeneficiaireParLangue() {
        Map<Langues, String> traductions = new HashMap<Langues, String>();

        if (traductionPaysTiersBeneficiaireEnAllemand != null) {
            traductions.put(Langues.Allemand, traductionPaysTiersBeneficiaireEnAllemand);
        }
        if (traductionPaysTiersBeneficiaireEnFrancais != null) {
            traductions.put(Langues.Francais, traductionPaysTiersBeneficiaireEnFrancais);
        }
        if (traductionPaysTiersBeneficiaireEnItalien != null) {
            traductions.put(Langues.Italien, traductionPaysTiersBeneficiaireEnItalien);
        }

        return traductions;
    }

    public Map<Langues, String> getTraductionPaysCreancierParLangue() {
        Map<Langues, String> traductions = new HashMap<Langues, String>();

        if (traductionPaysTiersCreancierEnAllemand != null) {
            traductions.put(Langues.Allemand, traductionPaysTiersCreancierEnAllemand);
        }
        if (traductionPaysTiersCreancierEnFrancais != null) {
            traductions.put(Langues.Francais, traductionPaysTiersCreancierEnFrancais);
        }
        if (traductionPaysTiersCreancierEnItalien != null) {
            traductions.put(Langues.Italien, traductionPaysTiersCreancierEnItalien);
        }

        return traductions;
    }

    public final TypePrestationDue getTypePrestationDue() {
        return typePrestationDue;
    }

    public boolean laPrestationEstBloquee() {
        return laPrestationEstBloquee;
    }
}
