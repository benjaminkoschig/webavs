/*
 * Créé le 15 nov. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author scr Retourne toutes les prestations à verser pour le mois. Utiliser pour le grand paiement des rentes.
 */

public class REPaiementRentesManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DD_ADR_PMT = "HCDDRE";
    public static final String FIELDNAME_DF_ADR_PMT = "HCDFRE";
    public static final String FIELDNAME_DOMAINE = "HFIAPP";
    public static final String FIELDNAME_ID_ADR_PMT_ALLOCATION_NOEL = "idAdressePmtAllocNoel";

    public static final String FIELDNAME_ID_ADR_PMT_RENTE = "idAdressePmtRente";
    public static final String FIELDNAME_ID_ADR_PMT_STANDARD = "idAdressePmtStd";
    public static final String FIELDNAME_ID_EXT = "HCIDEX";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_IDTIERS = "HTITIE";
    public static final String FIELDNAME_LEVEL = " LEVEL ";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_NOM = "HTLDE1";
    /** Nom et prénom du tiers bénéficiaire principal de la rente */
    public static final String FIELDNAME_NOM_BP = "tbeNom";

    public static final String FIELDNAME_NSS_BP = "tbeNSS";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_PRENOM = "HTLDE2";

    public static final String FIELDNAME_PRENOM_BP = "tbePrenom";

    private static final String LEVEL_1 = " 1 ";

    private static final String LEVEL_1_IN_CODE = " IN ('10', '13', '20', '23', '50', '70', '72')";

    private static final String LEVEL_2 = " 2 ";
    private static final String LEVEL_2_IN_CODE = " IN ('81', '82', '83', '84', '85', '86', '87', '88','89', '91', '92', '93', '94', '95', '96', '97')";

    // PC (A Traiter comme les API)
    private static final String LEVEL_3 = " 3 ";
    private static final String LEVEL_3_IN_CODE = " IN ('110', '113', '118', '119', '150', '158', '159')";

    private static final String LEVEL_4 = " 4 ";

    private static final String LEVEL_4_IN_CODE = " IN ('33', '53', '73')";

    private static final String LEVEL_5 = " 5 ";

    private static final String LEVEL_5_IN_CODE = " IN ('14', '15', '16', '24', '25', '26', '34', '35', '36', '45', '54', '55', '56', '74', '75', '76')";

    // RFM
    private static final String LEVEL_6 = " 6 ";
    private static final String LEVEL_6_IN_CODE = " IN ('210', '213', '250')";

    /** DOCUMENT ME! */
    public static final String TABLE_AVS = "TIPAVSP";
    /** DOCUMENT ME! */
    public static final String TABLE_TIERS = "TITIERP";
    public static final String TABLE_TIERS_ADR_PMT = "TIAPAIP";

    // Format : mm.aaaa
    private String forDatePaiement = "";
    private Boolean forIsEnErreur = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * selectionne en une seule requete toutes les infos necessaires au versement des rentes
     * <p>
     * redefini car la requete est un peu compliquee.
     * </p>
     * 
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getSql(final BStatement statement) {

        String INNER_JOIN = " INNER JOIN ";
        String LEFT_JOIN = " LEFT OUTER JOIN ";
        String ON = " ON ";
        String OR = " OR ";
        String AND = " AND ";
        String EGAL = " = ";
        String BETWEEN = " BETWEEN ";
        String POINT = ".";

        String today = JACalendar.todayJJsMMsAAAA();
        try {
            today = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(today);
        } catch (JAException e) {
            _addError(statement.getTransaction(), e.getMessage());
        }

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append(", ");
        sql.append(_getCollection()).append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA).append(POINT)
                .append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT).append(", ");
        sql.append(_getCollection()).append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA).append(POINT)
                .append(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_REFERENCE_PMT).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_SOUS_TYPE_GENRE_PRESTATION).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_IS_RETENUES).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_IS_ATTENTE_MAJ_BLOCAGE).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_IS_ATTENTE_MAJ_RETENUE).append(", ");

        // TODO valider la lecture de la valeur de l'id adresse de pmt domaine des alloc de Noël + utiliser les
        // constantes
        sql.append("ap1.HCIAIU idAdressePmtRente, ");
        sql.append("ap2.HCIAIU idAdressePmtStd, ");
        sql.append("ap3.HCIAIU idAdressePmtAllocNoel, ");
        sql.append(_getCollection()).append(REPaiementRentesManager.TABLE_TIERS).append(POINT)
                .append(REPaiementRentesManager.FIELDNAME_NOM).append(", ");
        sql.append(_getCollection()).append(REPaiementRentesManager.TABLE_TIERS).append(POINT)
                .append(REPaiementRentesManager.FIELDNAME_PRENOM).append(", ");
        sql.append(_getCollection()).append(REPaiementRentesManager.TABLE_AVS).append(POINT)
                .append(REPaiementRentesManager.FIELDNAME_NUM_AVS).append(", ");
        sql.append("tiersBeneficiaire.HTLDE1 tbeNom, ");
        sql.append("tiersBeneficiaire.HTLDE2 tbePrenom, ");
        sql.append("tiersAvsBeneficiaire.HXNAVS tbeNSS, ");

        // le champs LEVEL
        sql.append(" CASE ");
        sql.append(" WHEN " + REPrestationsAccordees.FIELDNAME_CODE_PRESTATION
                + REPaiementRentesManager.LEVEL_1_IN_CODE);
        sql.append(" THEN ");
        sql.append(REPaiementRentesManager.LEVEL_1);
        sql.append(" WHEN " + REPrestationsAccordees.FIELDNAME_CODE_PRESTATION
                + REPaiementRentesManager.LEVEL_2_IN_CODE);
        sql.append(" THEN ");
        sql.append(REPaiementRentesManager.LEVEL_2);

        sql.append(" WHEN " + REPrestationsAccordees.FIELDNAME_CODE_PRESTATION
                + REPaiementRentesManager.LEVEL_3_IN_CODE);
        sql.append(" THEN ");
        sql.append(REPaiementRentesManager.LEVEL_3);

        sql.append(" WHEN " + REPrestationsAccordees.FIELDNAME_CODE_PRESTATION
                + REPaiementRentesManager.LEVEL_4_IN_CODE);
        sql.append(" THEN ");
        sql.append(REPaiementRentesManager.LEVEL_4);

        sql.append(" WHEN " + REPrestationsAccordees.FIELDNAME_CODE_PRESTATION
                + REPaiementRentesManager.LEVEL_5_IN_CODE);
        sql.append(" THEN ");
        sql.append(REPaiementRentesManager.LEVEL_5);

        sql.append(" WHEN " + REPrestationsAccordees.FIELDNAME_CODE_PRESTATION
                + REPaiementRentesManager.LEVEL_6_IN_CODE);
        sql.append(" THEN ");
        sql.append(REPaiementRentesManager.LEVEL_6);

        sql.append(" END AS " + REPaiementRentesManager.FIELDNAME_LEVEL);

        sql.append(" FROM ");
        sql.append(_getCollection());
        sql.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);

        // join entre RenteAccordee et InfoCompta
        sql.append(INNER_JOIN).append(_getCollection()).append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA);
        sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA);
        sql.append(EGAL).append(_getCollection()).append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA)
                .append(POINT).append(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);

        // join entre InfoCompta et TITIERP
        sql.append(LEFT_JOIN).append(_getCollection()).append(REPaiementRentesManager.TABLE_TIERS);
        sql.append(ON).append(_getCollection()).append(REPaiementRentesManager.TABLE_TIERS).append(POINT)
                .append(REPaiementRentesManager.FIELDNAME_IDTIERS);
        sql.append(EGAL).append(_getCollection()).append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA)
                .append(POINT).append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);

        // join entre TITIERP et TITAVSP
        sql.append(LEFT_JOIN).append(_getCollection()).append(REPaiementRentesManager.TABLE_AVS);
        sql.append(ON).append(_getCollection()).append(REPaiementRentesManager.TABLE_AVS).append(POINT)
                .append(REPaiementRentesManager.FIELDNAME_IDTIERS);
        sql.append(EGAL).append(_getCollection()).append(REPaiementRentesManager.TABLE_TIERS).append(POINT)
                .append(REPaiementRentesManager.FIELDNAME_IDTIERS);

        // join entre renteAccordee et tiersBeneficiairePrincipal
        sql.append(INNER_JOIN).append(_getCollection()).append(REPaiementRentesManager.TABLE_TIERS)
                .append(" tiersBeneficiaire ");
        sql.append(ON).append(" tiersBeneficiaire.").append(REPaiementRentesManager.FIELDNAME_IDTIERS);
        sql.append(EGAL).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);

        // join entre renteAccordee et tiersAvsBeneficiairePrincipal
        sql.append(INNER_JOIN).append(_getCollection()).append(REPaiementRentesManager.TABLE_AVS)
                .append(" tiersAvsBeneficiaire ");
        sql.append(ON).append(" tiersAvsBeneficiaire.").append(REPaiementRentesManager.FIELDNAME_IDTIERS);
        sql.append(EGAL).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);

        // LEFT OUTER JOIN WEBAVS.TIAPAIP ap1
        sql.append(LEFT_JOIN).append(_getCollection()).append(REPaiementRentesManager.TABLE_TIERS_ADR_PMT)
                .append(" ap1 ");
        sql.append(ON).append(" (").append(_getCollection()).append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA)
                .append(POINT).append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT).append(EGAL)
                .append("ap1.").append(REPaiementRentesManager.FIELDNAME_IDTIERS);

        sql.append(AND).append("ap1.").append(REPaiementRentesManager.FIELDNAME_DOMAINE).append(EGAL)
                .append(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE).append(AND).append("((")
                .append(today).append(BETWEEN).append("ap1.").append(REPaiementRentesManager.FIELDNAME_DD_ADR_PMT)
                .append(AND).append("ap1.").append(REPaiementRentesManager.FIELDNAME_DF_ADR_PMT).append(")")

                .append(OR)

                .append("(ap1.").append(REPaiementRentesManager.FIELDNAME_DF_ADR_PMT).append(EGAL).append(" 0 ")
                .append(AND).append("ap1.").append(REPaiementRentesManager.FIELDNAME_DD_ADR_PMT).append(" <= ")
                .append(today).append("))");

        sql.append(AND).append("ap1.").append(REPaiementRentesManager.FIELDNAME_ID_EXT).append(EGAL).append("'')");

        // LEFT OUTER JOIN WEBAVS.TIAPAIP ap2
        sql.append(LEFT_JOIN).append(_getCollection()).append(REPaiementRentesManager.TABLE_TIERS_ADR_PMT)
                .append(" ap2 ");
        sql.append(ON).append(" (").append(_getCollection()).append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA)
                .append(POINT).append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT).append(EGAL)
                .append("ap2.").append(REPaiementRentesManager.FIELDNAME_IDTIERS);

        sql.append(AND).append("ap2.").append(REPaiementRentesManager.FIELDNAME_DOMAINE).append(EGAL)
                .append(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_DEFAULT).append(AND).append("((")
                .append(today).append(BETWEEN).append("ap2.").append(REPaiementRentesManager.FIELDNAME_DD_ADR_PMT)
                .append(AND).append("ap2.").append(REPaiementRentesManager.FIELDNAME_DF_ADR_PMT).append(")")

                .append(OR)

                .append("(ap2.").append(REPaiementRentesManager.FIELDNAME_DF_ADR_PMT).append(EGAL).append(" 0 ")
                .append(AND).append("ap2.").append(REPaiementRentesManager.FIELDNAME_DD_ADR_PMT).append(" <= ")
                .append(today).append("))");

        sql.append(AND).append("ap2.").append(REPaiementRentesManager.FIELDNAME_ID_EXT).append(EGAL).append("'')");

        // TODO valider la jointure
        // LEFT OUTER JOIN WEBAVS.TIAPAIP ap3 domaine allocation Noël
        sql.append(LEFT_JOIN).append(_getCollection()).append(REPaiementRentesManager.TABLE_TIERS_ADR_PMT)
                .append(" ap3 ");

        sql.append(ON).append(" (").append(_getCollection()).append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA)
                .append(POINT).append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT).append(EGAL)
                .append("ap3.").append(REPaiementRentesManager.FIELDNAME_IDTIERS);

        sql.append(AND).append("ap3.").append(REPaiementRentesManager.FIELDNAME_DOMAINE).append(EGAL)
                .append(IPRConstantesExternes.TIERS_CS_DOMAINE_ALLOCATION_DE_NOEL).append(AND).append("((")
                .append(today).append(BETWEEN).append("ap3.").append(REPaiementRentesManager.FIELDNAME_DD_ADR_PMT)
                .append(AND).append("ap3.").append(REPaiementRentesManager.FIELDNAME_DF_ADR_PMT).append(")")

                .append(OR)

                .append("(ap3.").append(REPaiementRentesManager.FIELDNAME_DF_ADR_PMT).append(EGAL).append(" 0 ")
                .append(AND).append("ap3.").append(REPaiementRentesManager.FIELDNAME_DD_ADR_PMT).append(" <= ")
                .append(today).append("))");

        sql.append(AND).append("ap3.").append(REPaiementRentesManager.FIELDNAME_ID_EXT).append(EGAL).append("'')");

        // Les RA dans l'état validé ou partiel inclusent dans la période
        sql.append(" WHERE ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(" IN (")
                .append(IREPrestationAccordee.CS_ETAT_VALIDE).append(", ")
                .append(IREPrestationAccordee.CS_ETAT_PARTIEL).append(", ")
                .append(IREPrestationAccordee.CS_ETAT_DIMINUE).append(") ");

        // Les prestations accordées avec des montants > 0;
        sql.append(AND).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION).append(" > 0 ");

        sql.append(AND).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(" <= ")
                .append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDatePaiement())).append(AND).append("( ")
                .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" >= ")
                .append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDatePaiement())).append(OR)
                .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(EGAL).append(" 0 )");

        if (getForIsEnErreur() != null) {
            if (getForIsEnErreur().booleanValue()) {
                sql.append(AND).append(_getCollection())
                        .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                        .append(REPrestationsAccordees.FIELDNAME_IS_ERREUR).append("  ='1' ");
            } else {
                sql.append(AND).append("(").append(_getCollection())
                        .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                        .append(REPrestationsAccordees.FIELDNAME_IS_ERREUR).append("  = '0' OR ")
                        .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(POINT).append(REPrestationsAccordees.FIELDNAME_IS_ERREUR).append(" IS NULL OR ")
                        .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(POINT).append(REPrestationsAccordees.FIELDNAME_IS_ERREUR).append(" ='2' OR ")
                        .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(POINT).append(REPrestationsAccordees.FIELDNAME_IS_ERREUR).append("  = '') ");
            }
        }

        sql.append(" ORDER BY ");
        sql.append(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE);
        sql.append(",").append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
        sql.append(",").append(REPrestationsAccordees.FIELDNAME_REFERENCE_PMT);
        sql.append(",").append(REPaiementRentesManager.FIELDNAME_LEVEL);

        return sql.toString();
    }

    @Override
    protected java.lang.String _getSqlCount(final BStatement statement) {
        _addError(statement.getTransaction(), "Unsuported function. Use RECountManager.getCount() method instead");
        return null;
    }

    /**
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REPaiementRentes();
    }

    public String getForDatePaiement() {
        return forDatePaiement;
    }

    public Boolean getForIsEnErreur() {
        return forIsEnErreur;
    }

    /**
     * @param forDatePaiement
     *            format : mm.aaaa
     */
    public void setForDatePaiement(final String forDatePaiement) {
        this.forDatePaiement = forDatePaiement;
    }

    public void setForIsEnErreur(final Boolean forIsEnErreur) {
        this.forIsEnErreur = forIsEnErreur;
    }

}
