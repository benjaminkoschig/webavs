/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.paiement;

import globaz.commons.nss.NSUtil;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.db.qds.RFAssQdDossier;
import globaz.cygnus.db.qds.RFQd;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;

/**
 * 
 * @author fha
 * @revision jje
 */
public class RFPrestationJointTiers extends RFPrestation {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String aliasTiaadrpDomaineRente = "tiaadrpDomaineRente";
    public static final String aliasTiaadrpDomaineStandard = "tiaadrpDomaineStandard";
    public static final String aliasTiadrepDomaineRente = "tiadrepDomaineRente";
    public static final String aliasTiadrepDomaineStandard = "tiadrepDomaineStandard";
    public static final String aliasTilocapDomaineRente = "tilocapDomaineRente";
    public static final String aliasTilocapDomaineStandard = "tilocapDomaineStandard";
    public static final String aliasTipavspBeneficiaire = "tipavspBeneficiaire";
    public static final String aliasTipavspDomaineRente = "tipavspDomaineRente";
    public static final String aliasTipavspDomaineStandard = "tipavspDomaineStandard";
    public static final String aliasTiperspBeneficiaire = "tiperspBeneficiaire";
    public static final String aliasTiperspDomaineRente = "tiperspDomaineRente";
    public static final String aliasTiperspDomaineStandard = "tiperspDomaineStandard";
    public static final String aliasTitierpBeneficiaire = "titierpBeneficiaire";
    public static final String aliasTitierpDomaineRente = "titierpDomaineRente";
    public static final String aliasTitierpDomaineStandard = "titierpDomaineStandard";

    public static final String DOMAINE_RENTE = "519006";
    public static final String DOMAINE_STANDARD = "519004";

    public static final String FIELD_CSNATIONALITEPAIEMENTBENEFICIAIRE = "csNationaliteBeneficiaire";
    public static final String FIELD_CSNATIONALITEPAIEMENTRENTE = "csNationalitePaiementRente";
    public static final String FIELD_CSNATIONALITEPAIEMENTSTANDARD = "csNationalitePaiementStandard";
    public static final String FIELD_CSSEXEBENEFICIAIRE = "cssexeBeneficiaire";
    public static final String FIELD_CSSEXEPAIEMENTDOMAINERENTE = "csSexePaiementDomaineRente";
    public static final String FIELD_CSSEXEPAIEMENTDOMAINESTANDARD = "csSexePaiementDomaineStandard";
    public static final String FIELD_DATEDECESBENEFICIAIRE = "datedecesBeneficiaire";
    public static final String FIELD_DATEDECESPAIEMENTDOMAINERENTE = "dateDecesPaiementDomaineRente";
    public static final String FIELD_DATEDECESPAIEMENTDOMAINESTANDARD = "dateDecesPaiementDomaineStandard";
    public static final String FIELD_DATENAISSANCEBENEFICIAIRE = "datenaissanceBeneficiaire";
    public static final String FIELD_DATENAISSANCEPAIEMENTDOMAINERENTE = "dateNaissancePaiementDomaineRente";
    public static final String FIELD_DATENAISSANCEPAIEMENTDOMAINESTANDARD = "dateNaissancePaiementDomaineStandard";
    public static final String FIELD_IDTIERSBENEFICIAIRE = "idtierBeneficiaire";
    public static final String FIELD_NOMBENEFICIAIRE = "nomBeneficiaire";
    public static final String FIELD_NOMPAIEMENTDOMAINERENTE = "nomPaiementDomaineRente";
    public static final String FIELD_NOMPAIEMENTDOMAINESTANDARD = "nomPaiementDomaineStandard";
    public static final String FIELD_NSSBENEFICIAIRE = "nssBeneficiaire";
    public static final String FIELD_NSSPAIEMENTDOMAINERENTE = "nssPaiementDomaineRente";
    public static final String FIELD_NSSPAIEMENTDOMAINESTANDARD = "nssPaiementDomaineStandard";
    public static final String FIELD_PRENOMBENEFICIAIRE = "prenomBeneficiaire";
    public static final String FIELD_PRENOMPAIEMENTDOMAINERENTE = "prenomPaiementDomaineRente";
    public static final String FIELD_PRENOMPAIEMENTDOMAINESTANDARD = "prenomPaiementDomaineStandard";

    public static final String FIELDNAME_DATEDECES = "HPDDEC";
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";
    public static final String FIELDNAME_DEB_ADR_PMT = "HEDDAD";
    public static final String FIELDNAME_DOM_ADR_PMT = "HFIAPP";
    public static final String FIELDNAME_FIN_ADR_PMT = "HEDFAD";

    public static final String FIELDNAME_ID_ADR_PMT = "HAIADR";
    public static final String FIELDNAME_ID_CANTON = "HJICAN";
    public static final String FIELDNAME_ID_GESTIONNAIRE = "KUSER";

    public static final String FIELDNAME_ID_LOCALITE = "HJILOC";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NATIONALITE = "HNIPAY";
    public static final String FIELDNAME_NOM = "HTLDE1";

    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";

    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_SEXE = "HPTSEX";

    public static final String FIELDNAME_TYPE_ADR_PMT = "HETTAD";

    public static final String FIELDNAME_VISA_GESTIONNAIRE = "FVISA";

    public static final String TABLE_ADRESSE = "TIAADRP";
    public static final String TABLE_ADRESSE_2 = "TIADREP";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";

    public static final String TABLE_GESTIONNAIRES = "FWSUSRP";
    public static final String TABLE_LOCALITE = "TILOCAP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusque dans les tiers (Nom et AVS)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String as = " AS ";
        String on = " ON ";
        String and = " AND ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);

        // jointure entre la table des prestations et la table décision
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestation.FIELDNAME_ID_DECISION);

        // jointure entre la table des decisions et la table des gestionnaires
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationJointTiers.TABLE_GESTIONNAIRES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_GESTIONNAIRE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationJointTiers.TABLE_GESTIONNAIRES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_GESTIONNAIRE);

        // jointure entre la table des décisions et la table RFQDBAS
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQd.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQd.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQd.FIELDNAME_ID_QD);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_QD_PRINICIPALE);

        // jointure entre la table RFQDBAS et ASSQDDossier
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssQdDossier.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssQdDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssQdDossier.FIELDNAME_ID_QD);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQd.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQd.FIELDNAME_ID_QD);

        // jointure entre la table des AssQdDossier et la table des dossiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssQdDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssQdDossier.FIELDNAME_ID_DOSSIER);

        // jointure entre la table des dossiers et la table des demandes PR
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_PRDEM);

        // jointure entre la table des demandes PR et la table des numeros AVS
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationJointTiers.TABLE_AVS);
        fromClauseBuffer.append(as);
        fromClauseBuffer.append(RFPrestationJointTiers.aliasTipavspBeneficiaire);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(RFPrestationJointTiers.aliasTipavspBeneficiaire);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(as);
        fromClauseBuffer.append(RFPrestationJointTiers.aliasTiperspBeneficiaire);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(RFPrestationJointTiers.aliasTipavspBeneficiaire);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(RFPrestationJointTiers.aliasTiperspBeneficiaire);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des personnes et la table des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationJointTiers.TABLE_TIERS);
        fromClauseBuffer.append(as);
        fromClauseBuffer.append(RFPrestationJointTiers.aliasTitierpBeneficiaire);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(RFPrestationJointTiers.aliasTiperspBeneficiaire);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(RFPrestationJointTiers.aliasTitierpBeneficiaire);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI);

        /**************************************************************************************************/
        // Recherche des informations de l'adresse de paiement

        // Domaine standard:

        // jointure entre la table des décisions et la table titierp
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationJointTiers.TABLE_TIERS);
        fromClauseBuffer.append(as);
        fromClauseBuffer.append(RFPrestationJointTiers.aliasTitierpDomaineStandard);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(RFPrestationJointTiers.aliasTitierpDomaineStandard);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_ADRESSE_PAIEMENT);

        // jointure entre la table titierp et la table des numeros AVS
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationJointTiers.TABLE_AVS);
        fromClauseBuffer.append(as);
        fromClauseBuffer.append(RFPrestationJointTiers.aliasTipavspDomaineStandard);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(RFPrestationJointTiers.aliasTitierpDomaineStandard);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(RFPrestationJointTiers.aliasTipavspDomaineStandard);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(as);
        fromClauseBuffer.append(RFPrestationJointTiers.aliasTiperspDomaineStandard);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(RFPrestationJointTiers.aliasTipavspDomaineStandard);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(RFPrestationJointTiers.aliasTiperspDomaineStandard);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre table titierp et tiaadrp
        // fromClauseBuffer.append(leftJoin);
        // fromClauseBuffer.append(schema);
        // fromClauseBuffer.append(RFPrestationJointTiers.TABLE_ADRESSE);
        // fromClauseBuffer.append(as);
        // fromClauseBuffer.append(RFPrestationJointTiers.aliasTiaadrpDomaineStandard);
        // fromClauseBuffer.append(on);
        // fromClauseBuffer.append(" ( ");
        // fromClauseBuffer.append(RFPrestationJointTiers.aliasTiaadrpDomaineStandard);
        // fromClauseBuffer.append(point);
        // fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI);
        // fromClauseBuffer.append(egal);
        // fromClauseBuffer.append(RFPrestationJointTiers.aliasTitierpDomaineStandard);
        // fromClauseBuffer.append(point);
        // fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI);
        /*
         * fromClauseBuffer.append(and + RFPrestationJointTiers.aliasTiaadrpDomaineStandard + point +
         * RFPrestationJointTiers.FIELDNAME_TYPE_ADR_PMT + " = " + AdresseService.CS_TYPE_DOMICILE);
         * fromClauseBuffer.append(and + RFPrestationJointTiers.aliasTiaadrpDomaineStandard + point +
         * RFPrestationJointTiers.FIELDNAME_DOM_ADR_PMT + " = " + RFPrestationJointTiers.DOMAINE_STANDARD);
         */
        // fromClauseBuffer.append(")");

        // jointure entre TIAADRP et TIADREP
        // fromClauseBuffer.append(leftJoin);
        // fromClauseBuffer.append(schema);
        // fromClauseBuffer.append(RFPrestationJointTiers.TABLE_ADRESSE_2);
        // fromClauseBuffer.append(as);
        // fromClauseBuffer.append(RFPrestationJointTiers.aliasTiadrepDomaineStandard);
        // fromClauseBuffer.append(on);
        // fromClauseBuffer.append(" (");
        // fromClauseBuffer.append(RFPrestationJointTiers.aliasTiaadrpDomaineStandard);
        // fromClauseBuffer.append(point);
        // fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_ADR_PMT);
        // fromClauseBuffer.append(egal);
        // fromClauseBuffer.append(RFPrestationJointTiers.aliasTiadrepDomaineStandard);
        // fromClauseBuffer.append(point);
        // fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_ADR_PMT);
        // fromClauseBuffer.append(")");

        // jointure entre TIADREP et TILOCAP
        // fromClauseBuffer.append(leftJoin);
        // fromClauseBuffer.append(schema);
        // fromClauseBuffer.append(RFPrestationJointTiers.TABLE_LOCALITE);
        // fromClauseBuffer.append(as);
        // fromClauseBuffer.append(RFPrestationJointTiers.aliasTilocapDomaineStandard);
        // fromClauseBuffer.append(on);
        // fromClauseBuffer.append(" ( ");
        // fromClauseBuffer.append(RFPrestationJointTiers.aliasTiadrepDomaineStandard);
        // fromClauseBuffer.append(point);
        // fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_LOCALITE);
        // fromClauseBuffer.append(egal);
        // fromClauseBuffer.append(RFPrestationJointTiers.aliasTilocapDomaineStandard);
        // fromClauseBuffer.append(point);
        // fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_LOCALITE);
        // fromClauseBuffer.append(")");

        // Domaine rente:

        // jointure entre la table des décisions et la table titierp
        /*
         * fromClauseBuffer.append(leftJoin); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFPrestationJointTiers.TABLE_TIERS); fromClauseBuffer.append(as);
         * fromClauseBuffer.append(RFPrestationJointTiers.aliasTitierpDomaineRente); fromClauseBuffer.append(on);
         * fromClauseBuffer.append(RFPrestationJointTiers.aliasTitierpDomaineRente); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI); fromClauseBuffer.append(egal);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(RFDecision.TABLE_NAME);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFDecision.FIELDNAME_ID_ADRESSE_PAIEMENT);
         * 
         * // jointure entre la table titierp et la table des numeros AVS fromClauseBuffer.append(leftJoin);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(RFPrestationJointTiers.TABLE_AVS);
         * fromClauseBuffer.append(as); fromClauseBuffer.append(RFPrestationJointTiers.aliasTipavspDomaineRente);
         * fromClauseBuffer.append(on); fromClauseBuffer.append(RFPrestationJointTiers.aliasTitierpDomaineStandard);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI);
         * fromClauseBuffer.append(egal); fromClauseBuffer.append(RFPrestationJointTiers.aliasTipavspDomaineRente);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI);
         * 
         * // jointure entre la table des numeros AVS et la table des personnes fromClauseBuffer.append(leftJoin);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(RFPrestationJointTiers.TABLE_PERSONNE);
         * fromClauseBuffer.append(as); fromClauseBuffer.append(RFPrestationJointTiers.aliasTiperspDomaineRente);
         * fromClauseBuffer.append(on); fromClauseBuffer.append(RFPrestationJointTiers.aliasTipavspDomaineRente);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI);
         * fromClauseBuffer.append(egal); fromClauseBuffer.append(RFPrestationJointTiers.aliasTiperspDomaineRente);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI);
         * 
         * // jointure entre table titierp et tiaadrp fromClauseBuffer.append(leftJoin);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(RFPrestationJointTiers.TABLE_ADRESSE);
         * fromClauseBuffer.append(as); fromClauseBuffer.append(RFPrestationJointTiers.aliasTiaadrpDomaineRente);
         * fromClauseBuffer.append(on); fromClauseBuffer.append(" ( ");
         * fromClauseBuffer.append(RFPrestationJointTiers.aliasTiaadrpDomaineRente); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI); fromClauseBuffer.append(egal);
         * fromClauseBuffer.append(RFPrestationJointTiers.aliasTitierpDomaineRente); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI); fromClauseBuffer.append(and +
         * RFPrestationJointTiers.aliasTiaadrpDomaineRente + point + RFPrestationJointTiers.FIELDNAME_TYPE_ADR_PMT +
         * " = " + AdresseService.CS_TYPE_DOMICILE); fromClauseBuffer.append(and +
         * RFPrestationJointTiers.aliasTiaadrpDomaineRente + point + RFPrestationJointTiers.FIELDNAME_DOM_ADR_PMT +
         * " = " + RFPrestationJointTiers.DOMAINE_RENTE); fromClauseBuffer.append(")");
         * 
         * // jointure entre TIAADRP et TIADREP fromClauseBuffer.append(leftJoin); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFPrestationJointTiers.TABLE_ADRESSE_2); fromClauseBuffer.append(as);
         * fromClauseBuffer.append(RFPrestationJointTiers.aliasTiadrepDomaineRente); fromClauseBuffer.append(on);
         * fromClauseBuffer.append(" ("); fromClauseBuffer.append(RFPrestationJointTiers.aliasTiaadrpDomaineRente);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_ADR_PMT);
         * fromClauseBuffer.append(egal); fromClauseBuffer.append(RFPrestationJointTiers.aliasTiadrepDomaineRente);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_ADR_PMT);
         * fromClauseBuffer.append(")");
         * 
         * // jointure entre TIADREP et TILOCAP fromClauseBuffer.append(leftJoin); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFPrestationJointTiers.TABLE_LOCALITE); fromClauseBuffer.append(as);
         * fromClauseBuffer.append(RFPrestationJointTiers.aliasTilocapDomaineRente); fromClauseBuffer.append(on);
         * fromClauseBuffer.append(" ( "); fromClauseBuffer.append(RFPrestationJointTiers.aliasTiadrepDomaineRente);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_LOCALITE);
         * fromClauseBuffer.append(egal); fromClauseBuffer.append(RFPrestationJointTiers.aliasTilocapDomaineRente);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFPrestationJointTiers.FIELDNAME_ID_LOCALITE);
         * fromClauseBuffer.append(")");
         */

        return fromClauseBuffer.toString();
    }

    private String csCanton = "";
    private String csNationalite = "";
    private String csNationalitePaiementRente = "";
    private String csNationalitePaiementStandard = "";
    private String csSexe = "";
    private String csSexePaiementDomaineRente = "";
    private String csSexePaiementDomaineStandard = "";
    private String dateDeces = "";
    private String dateDecesPaiementDomaineRente = "";
    private String dateDecesPaiementDomaineStandard = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateNaissance = "";

    private String dateNaissancePaiementDomaineRente = "";
    private String dateNaissancePaiementDomaineStandard = "";
    private transient String fromClause = null;
    private String idTiers = "";
    private String nom = "";
    private String nomPaiementDomaineRente = "";
    private String nomPaiementDomaineStandard = "";
    // champs nécessaires description tiers
    private String nss = "";
    private String nssPaiementDomaineRente = "";

    private String nssPaiementDomaineStandard = "";
    private String prenom = "";
    private String prenomPaiementDomaineRente = "";
    private String prenomPaiementDomaineStandard = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFPrestationJointTiers.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        super._readProperties(statement);

        nss = NSUtil.formatAVSUnknown(statement.dbReadString(RFPrestationJointTiers.FIELD_NSSBENEFICIAIRE));
        dateNaissance = statement.dbReadDateAMJ(RFPrestationJointTiers.FIELD_DATENAISSANCEBENEFICIAIRE);
        dateDeces = statement.dbReadDateAMJ(RFPrestationJointTiers.FIELD_DATEDECESBENEFICIAIRE);
        csSexe = statement.dbReadNumeric(RFPrestationJointTiers.FIELD_CSSEXEBENEFICIAIRE);
        nom = statement.dbReadString(RFPrestationJointTiers.FIELD_NOMBENEFICIAIRE);
        prenom = statement.dbReadString(RFPrestationJointTiers.FIELD_PRENOMBENEFICIAIRE);
        idTiers = statement.dbReadString(RFPrestationJointTiers.FIELD_IDTIERSBENEFICIAIRE);
        csNationalite = statement.dbReadString(RFPrestationJointTiers.FIELD_CSNATIONALITEPAIEMENTBENEFICIAIRE);

        // Domaine standard paiement

        nomPaiementDomaineStandard = statement.dbReadString(RFPrestationJointTiers.FIELD_NOMPAIEMENTDOMAINESTANDARD);
        prenomPaiementDomaineStandard = statement
                .dbReadString(RFPrestationJointTiers.FIELD_PRENOMPAIEMENTDOMAINESTANDARD);
        nssPaiementDomaineStandard = NSUtil.formatAVSUnknown(statement
                .dbReadString(RFPrestationJointTiers.FIELD_NSSPAIEMENTDOMAINESTANDARD));
        dateNaissancePaiementDomaineStandard = statement
                .dbReadDateAMJ(RFPrestationJointTiers.FIELD_DATENAISSANCEPAIEMENTDOMAINESTANDARD);
        dateDecesPaiementDomaineStandard = statement
                .dbReadDateAMJ(RFPrestationJointTiers.FIELD_DATEDECESPAIEMENTDOMAINESTANDARD);
        csSexePaiementDomaineStandard = statement
                .dbReadNumeric(RFPrestationJointTiers.FIELD_CSSEXEPAIEMENTDOMAINESTANDARD);
        csNationalitePaiementStandard = statement
                .dbReadNumeric(RFPrestationJointTiers.FIELD_CSNATIONALITEPAIEMENTSTANDARD);

        // Domaine rente paiement

        /*
         * this.nomPaiementDomaineRente = statement.dbReadString(RFPrestationJointTiers.FIELD_NOMPAIEMENTDOMAINERENTE);
         * this.prenomPaiementDomaineRente = statement
         * .dbReadString(RFPrestationJointTiers.FIELD_PRENOMPAIEMENTDOMAINERENTE); this.nssPaiementDomaineRente =
         * NSUtil.formatAVSUnknown(statement .dbReadString(RFPrestationJointTiers.FIELD_NSSPAIEMENTDOMAINERENTE));
         * this.dateNaissancePaiementDomaineRente = statement
         * .dbReadDateAMJ(RFPrestationJointTiers.FIELD_DATENAISSANCEPAIEMENTDOMAINERENTE);
         * this.dateDecesPaiementDomaineRente = statement
         * .dbReadDateAMJ(RFPrestationJointTiers.FIELD_DATEDECESPAIEMENTDOMAINERENTE); this.csSexePaiementDomaineRente =
         * statement .dbReadNumeric(RFPrestationJointTiers.FIELD_CSSEXEPAIEMENTDOMAINERENTE);
         * this.csNationalitePaiementRente = statement
         * .dbReadNumeric(RFPrestationJointTiers.FIELD_CSNATIONALITEPAIEMENTRENTE);
         */

    }

    public String getCsCanton() {
        return csCanton;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsNationalitePaiementRente() {
        return csNationalitePaiementRente;
    }

    public String getCsNationalitePaiementStandard() {
        return csNationalitePaiementStandard;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getCsSexePaiementDomaineRente() {
        return csSexePaiementDomaineRente;
    }

    public String getCsSexePaiementDomaineStandard() {
        return csSexePaiementDomaineStandard;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateDecesPaiementDomaineRente() {
        return dateDecesPaiementDomaineRente;
    }

    public String getDateDecesPaiementDomaineStandard() {
        return dateDecesPaiementDomaineStandard;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateNaissancePaiementDomaineRente() {
        return dateNaissancePaiementDomaineRente;
    }

    public String getDateNaissancePaiementDomaineStandard() {
        return dateNaissancePaiementDomaineStandard;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNom() {
        return nom;
    }

    public String getNomPaiementDomaineRente() {
        return nomPaiementDomaineRente;
    }

    public String getNomPaiementDomaineStandard() {
        return nomPaiementDomaineStandard;
    }

    public String getNss() {
        return nss;
    }

    public String getNssPaiementDomaineRente() {
        return nssPaiementDomaineRente;
    }

    public String getNssPaiementDomaineStandard() {
        return nssPaiementDomaineStandard;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getPrenomPaiementDomaineRente() {
        return prenomPaiementDomaineRente;
    }

    public String getPrenomPaiementDomaineStandard() {
        return prenomPaiementDomaineStandard;
    }

    public void setCsCanton(String csCanton) {
        this.csCanton = csCanton;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsNationalitePaiementRente(String csNationalitePaiementRente) {
        this.csNationalitePaiementRente = csNationalitePaiementRente;
    }

    public void setCsNationalitePaiementStandard(String csNationalitePaiementStandard) {
        this.csNationalitePaiementStandard = csNationalitePaiementStandard;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setCsSexePaiementDomaineRente(String csSexePaiementDomaineRente) {
        this.csSexePaiementDomaineRente = csSexePaiementDomaineRente;
    }

    public void setCsSexePaiementDomaineStandard(String csSexePaiementDomaineStandard) {
        this.csSexePaiementDomaineStandard = csSexePaiementDomaineStandard;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateDecesPaiementDomaineRente(String dateDecesPaiementDomaineRente) {
        this.dateDecesPaiementDomaineRente = dateDecesPaiementDomaineRente;
    }

    public void setDateDecesPaiementDomaineStandard(String dateDecesPaiementDomaineStandard) {
        this.dateDecesPaiementDomaineStandard = dateDecesPaiementDomaineStandard;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateNaissancePaiementDomaineRente(String dateNaissancePaiementDomaineRente) {
        this.dateNaissancePaiementDomaineRente = dateNaissancePaiementDomaineRente;
    }

    public void setDateNaissancePaiementDomaineStandard(String dateNaissancePaiementDomaineStandard) {
        this.dateNaissancePaiementDomaineStandard = dateNaissancePaiementDomaineStandard;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNomPaiementDomaineRente(String nomPaiementDomaineRente) {
        this.nomPaiementDomaineRente = nomPaiementDomaineRente;
    }

    public void setNomPaiementDomaineStandard(String nomPaiementDomaineStandard) {
        this.nomPaiementDomaineStandard = nomPaiementDomaineStandard;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setNssPaiementDomaineRente(String nssPaiementDomaineRente) {
        this.nssPaiementDomaineRente = nssPaiementDomaineRente;
    }

    public void setNssPaiementDomaineStandard(String nssPaiementDomaineStandard) {
        this.nssPaiementDomaineStandard = nssPaiementDomaineStandard;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setPrenomPaiementDomaineRente(String prenomPaiementDomaineRente) {
        this.prenomPaiementDomaineRente = prenomPaiementDomaineRente;
    }

    public void setPrenomPaiementDomaineStandard(String prenomPaiementDomaineStandard) {
        this.prenomPaiementDomaineStandard = prenomPaiementDomaineStandard;
    }

}
