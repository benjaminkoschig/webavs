package globaz.apg.db.alfa;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.prestation.APCotisation;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.enums.APTypeDePrestation;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APBouclementAlfaManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebutPeriode = "";
    private String dateFinPeriode = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * selectionne en une seule requete toutes les infos necessaires au bouclement ALFA des caisses horlogeres.
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
        /*
         * Exemple :
         * 
         * SELECT VIIAFF, WATTDE, VHIPRS, VAIDRO, SUM(VHDMOB) AS VHMMOB, SUM(VHNNJS) AS VHNNJS,
         * 
         * (SELECT SUM(VLMMON) FROM WEBAVS.APCOTIS INNER JOIN WEBAVS.APREPAP ON VLIRBP=VIIRBP AND VIIPRA=VHIPRS AND
         * VLLTYP='A' ) AS VHMCOT,
         * 
         * (SELECT SUM(VLMMON) FROM WEBAVS.APCOTIS INNER JOIN WEBAVS.APREPAP ON VLIRBP=VIIRBP AND VIIPRA=VHIPRS AND
         * VLLTYP='I' ) AS VHMIMP
         * 
         * FROM WEBAVS.APPRESP
         * 
         * INNER JOIN WEBAVS.APDROIP ON VAIDRO=VHIDRO INNER JOIN WEBAVS.PRDEMAP ON WAIDEM=VAIDEM INNER JOIN
         * WEBAVS.APREPAP ON VHIPRS=VIIPRA
         * 
         * WHERE
         * 
         * VHTGEN=52015002 AND (VHDPMT<=dateFinPeriode AND VHDPMT>=dateDebutPeriode) -- AND VIIAFF > 0
         * 
         * GROUP BY VIIAFF, WATTDE, VHIPRS, VAIDRO
         */

        final StringBuffer sql = new StringBuffer();

        sql.append("SELECT ");

        sql.append(APRepartitionPaiements.FIELDNAME_IDAFFILIE);
        sql.append(", ");
        sql.append(PRDemande.FIELDNAME_TYPE_DEMANDE);
        sql.append(", ");
        sql.append(APPrestation.FIELDNAME_IDPRESTATIONAPG);
        sql.append(", ");
        sql.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        sql.append(", SUM(");
        sql.append(APPrestation.FIELDNAME_MONTANTBRUT);
        sql.append(") AS VHMMOB, ");
        sql.append("SUM(");
        sql.append(APPrestation.FIELDNAME_NOMBREJOURSSOLDES);
        sql.append(") AS VHNNJS, ");

        sql.append("(SELECT SUM(");
        sql.append(APCotisation.FIELDNAME_MONTANT);
        sql.append(")");

        sql.append(" FROM ");
        sql.append(_getCollection());
        sql.append(APCotisation.TABLE_NAME);
        sql.append(" INNER JOIN ");
        sql.append(_getCollection());
        sql.append(APRepartitionPaiements.TABLE_NAME);
        sql.append(" ON ");
        sql.append(APCotisation.FIELDNAME_IDREPARTITIONBENEFICIAIREPAIEMENT);
        sql.append("=");
        sql.append(APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT);
        sql.append(" AND ");
        sql.append(APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG);
        sql.append("=");
        sql.append(APPrestation.FIELDNAME_IDPRESTATIONAPG);
        sql.append(" AND ");
        sql.append(APCotisation.FIELDNAME_TYPE);
        sql.append("='");
        sql.append(APCotisation.TYPE_ASSURANCE);
        sql.append("' ");
        sql.append(") AS VHMCOT,");

        sql.append("(SELECT SUM(");
        sql.append(APCotisation.FIELDNAME_MONTANT);
        sql.append(")");

        sql.append("FROM ");
        sql.append(_getCollection());
        sql.append(APCotisation.TABLE_NAME);
        sql.append(" INNER JOIN ");
        sql.append(_getCollection());
        sql.append(APRepartitionPaiements.TABLE_NAME);
        sql.append(" ON ");
        sql.append(APCotisation.FIELDNAME_IDREPARTITIONBENEFICIAIREPAIEMENT);
        sql.append("=");
        sql.append(APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT);
        sql.append(" AND ");
        sql.append(APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG);
        sql.append("=");
        sql.append(APPrestation.FIELDNAME_IDPRESTATIONAPG);
        sql.append(" AND ");
        sql.append(APCotisation.FIELDNAME_TYPE);
        sql.append("='");
        sql.append(APCotisation.TYPE_IMPOT);
        sql.append("' ");
        sql.append(") AS VHMIMP");

        sql.append(" FROM ");
        sql.append(_getCollection());
        sql.append(APPrestation.TABLE_NAME);

        sql.append(" INNER JOIN ");
        sql.append(_getCollection());
        sql.append(APDroitLAPG.TABLE_NAME_LAPG);
        sql.append(" ON ");
        sql.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        sql.append("=");
        sql.append(APPrestation.FIELDNAME_IDDROIT);

        sql.append(" INNER JOIN ");
        sql.append(_getCollection());
        sql.append(PRDemande.TABLE_NAME);
        sql.append(" ON ");
        sql.append(PRDemande.FIELDNAME_IDDEMANDE);
        sql.append("=");
        sql.append(APDroitLAPG.FIELDNAME_IDDEMANDE);

        sql.append(" INNER JOIN ");
        sql.append(_getCollection());
        sql.append(APRepartitionPaiements.TABLE_NAME);
        sql.append(" ON ");
        sql.append(APPrestation.FIELDNAME_IDPRESTATIONAPG);
        sql.append("=");
        sql.append(APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG);

        sql.append(" WHERE ");

        sql.append(APPrestation.FIELDNAME_GENRE_PRESTATION);
        sql.append(" IN (");
        sql.append(APTypeDePrestation.ACM_ALFA.getCodesystemString());
        sql.append(",");
        sql.append(APTypeDePrestation.ACM2_ALFA.getCodesystemString());
        sql.append(") ");

        sql.append(" AND (");
        sql.append(APPrestation.FIELDNAME_DATEPAIEMENT);
        sql.append(">=");
        sql.append(this._dbWriteDateAMJ(statement.getTransaction(), dateDebutPeriode));
        sql.append(" AND ");
        sql.append(APPrestation.FIELDNAME_DATEPAIEMENT);
        sql.append("<=");
        sql.append(this._dbWriteDateAMJ(statement.getTransaction(), dateFinPeriode));
        sql.append(") ");

        // bz-5007
        // sql.append(" AND ");
        // sql.append(APRepartitionPaiements.FIELDNAME_IDAFFILIE);
        // sql.append(" > 0 ");

        sql.append(" GROUP BY ");
        sql.append(APRepartitionPaiements.FIELDNAME_IDAFFILIE);
        sql.append(", ");
        sql.append(PRDemande.FIELDNAME_TYPE_DEMANDE);
        sql.append(", ");
        sql.append(APPrestation.FIELDNAME_IDPRESTATIONAPG);
        sql.append(", ");
        sql.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);

        // System.out.println(sql.toString());

        return sql.toString();
    }

    /**
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APBouclementAlfa();
    }

    /**
     * setter pour l'attribut for periode.
     * 
     * @param dateDebutPeriode
     *            une nouvelle valeur pour cet attribut
     * @param dateFinPeriode
     *            une nouvelle valeur pour cet attribut
     */
    public void setForPeriode(final String dateDebutPeriode, final String dateFinPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
        this.dateFinPeriode = dateFinPeriode;
    }
}
