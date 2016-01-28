package globaz.naos.db.affiliation;

import globaz.aquila.api.ICOEtape;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.log.JadeLogger;
import globaz.naos.translation.CodeSystem;

public class AFListeAffilieFraisGestionReduitManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String F_DATE_DEBUT_AFF = AFListeAffilieFraisGestionReduitManager.T_AFFILIATION + "."
            + "MADDEB";
    private static final String F_DATE_DEBUT_COTISATION = AFListeAffilieFraisGestionReduitManager.T_COTISATION + "."
            + "MEDDEB";
    private static final String F_DATE_EXECUTION_ETAPE_CONTENTIEUX = AFListeAffilieFraisGestionReduitManager.T_HISTORIQUE_CONTENTIEUX
            + "." + "OEDEXE";
    private static final String F_DATE_FIN_AFF = AFListeAffilieFraisGestionReduitManager.T_AFFILIATION + "." + "MADFIN";
    private static final String F_DATE_FIN_COTISATION = AFListeAffilieFraisGestionReduitManager.T_COTISATION + "."
            + "MEDFIN";
    private static final String F_ETAPE_CONTENTIEUX_ANNULE = AFListeAffilieFraisGestionReduitManager.T_HISTORIQUE_CONTENTIEUX
            + "." + "OEBANN";
    private static final String F_GENRE_ASSURANCE = AFListeAffilieFraisGestionReduitManager.T_ASSURANCE + "."
            + "MBTGEN";
    private static final String F_IDADHESION_ADHESION = AFListeAffilieFraisGestionReduitManager.T_ADHESION + "."
            + "MRIADH";
    private static final String F_IDADHESION_COTISATION = AFListeAffilieFraisGestionReduitManager.T_COTISATION + "."
            + "MRIADH";
    private static final String F_IDAFFILIATION_ADHESION = AFListeAffilieFraisGestionReduitManager.T_ADHESION + "."
            + "MAIAFF";
    private static final String F_IDAFFILIATION_AFFILIATION = AFListeAffilieFraisGestionReduitManager.T_AFFILIATION
            + "." + "MAIAFF";
    private static final String F_IDASSURANCE_ASSURANCE = AFListeAffilieFraisGestionReduitManager.T_ASSURANCE + "."
            + "MBIASS";
    private static final String F_IDASSURANCE_COTISATION = AFListeAffilieFraisGestionReduitManager.T_COTISATION + "."
            + "MBIASS";
    private static final String F_IDCOMPTEANNEXE_COMPTE_ANNEXE = AFListeAffilieFraisGestionReduitManager.T_COMPTE_ANNEXE
            + "." + "IDCOMPTEANNEXE";
    private static final String F_IDCOMPTEANNEXE_CONTENTIEUX = AFListeAffilieFraisGestionReduitManager.T_CONTENTIEUX
            + "." + "OAICOA";
    private static final String F_IDCONTENTIEUX_CONTENTIEUX = AFListeAffilieFraisGestionReduitManager.T_CONTENTIEUX
            + "." + "OAICON";
    private static final String F_IDCONTENTIEUX_HISTORIQUE_CONTENTIEUX = AFListeAffilieFraisGestionReduitManager.T_HISTORIQUE_CONTENTIEUX
            + "." + "OAICON";
    private static final String F_IDCOTISATION = AFListeAffilieFraisGestionReduitManager.T_COTISATION + "." + "MEICOT";
    private static final String F_IDETAPECONTENTIEUX_ETTAPE_CONTENTIEUX = AFListeAffilieFraisGestionReduitManager.T_ETAPE_CONTENTIEUX
            + "." + "ODIETA";
    private static final String F_IDETAPECONTENTIEUX_HISTORIQUE_CONTENTIEUX = AFListeAffilieFraisGestionReduitManager.T_HISTORIQUE_CONTENTIEUX
            + "." + "ODIETA";

    private static final String F_IDROLE = AFListeAffilieFraisGestionReduitManager.T_COMPTE_ANNEXE + "." + "IDROLE";

    private static final String F_IDTIERS_AFFILIATION = AFListeAffilieFraisGestionReduitManager.T_AFFILIATION + "."
            + "HTITIE";

    private static final String F_IDTIERS_TIERS = AFListeAffilieFraisGestionReduitManager.T_TIERS + "." + "HTITIE";
    private static final String F_NOM_AFFILIE_FIRSTPART = AFListeAffilieFraisGestionReduitManager.T_TIERS + "."
            + "HTLDE1";

    private static final String F_NOM_AFFILIE_SECONDPART = AFListeAffilieFraisGestionReduitManager.T_TIERS + "."
            + "HTLDE2";

    private static final String F_NUMERO_AFFILIE = AFListeAffilieFraisGestionReduitManager.T_AFFILIATION + "."
            + "MALNAF";
    private static final String F_NUMERO_AFFILIE_COMPTE_ANNEXE = AFListeAffilieFraisGestionReduitManager.T_COMPTE_ANNEXE
            + "." + "IDEXTERNEROLE";

    private static final String F_TYPE_ASSURANCE = AFListeAffilieFraisGestionReduitManager.T_ASSURANCE + "." + "MBTTYP";

    private static final String F_TYPE_ETAPE_CONTENTIEUX = AFListeAffilieFraisGestionReduitManager.T_ETAPE_CONTENTIEUX
            + "." + "ODTETA";

    private static final String T_ADHESION = "AFADHEP";
    private static final String T_AFFILIATION = "AFAFFIP";
    private static final String T_ASSURANCE = "AFASSUP";
    private static final String T_COMPTE_ANNEXE = "CACPTAP";
    private static final String T_CONTENTIEUX = "COCAVSP";
    private static final String T_COTISATION = "AFCOTIP";
    private static final String T_ETAPE_CONTENTIEUX = "COETAPP";
    private static final String T_HISTORIQUE_CONTENTIEUX = "COHISTP";
    private static final String T_TIERS = "TITIERP";

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer sqlFields = new StringBuffer("");

        sqlFields.append(" COUNT(*) AS NOMBRESOMMATIONPOURSUITE ");
        sqlFields.append(", ");
        sqlFields.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_NUMERO_AFFILIE);
        sqlFields.append(", ");
        sqlFields.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_NOM_AFFILIE_FIRSTPART);
        sqlFields.append(", ");
        sqlFields.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_NOM_AFFILIE_SECONDPART);
        sqlFields.append(", ");
        sqlFields.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_GENRE_ASSURANCE);
        sqlFields.append(", ");
        sqlFields.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_IDCOTISATION);
        sqlFields.append(", ");
        sqlFields.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_TYPE_ETAPE_CONTENTIEUX);

        return sqlFields.toString();

    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.T_TIERS);
        sqlFrom.append(" INNER JOIN ");
        sqlFrom.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.T_AFFILIATION);
        sqlFrom.append(" ON ");
        sqlFrom.append(" ( " + _getCollection() + AFListeAffilieFraisGestionReduitManager.F_IDTIERS_AFFILIATION + " = "
                + _getCollection() + AFListeAffilieFraisGestionReduitManager.F_IDTIERS_TIERS + " ) ");

        sqlFrom.append(" INNER JOIN ");
        sqlFrom.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.T_ADHESION);
        sqlFrom.append(" ON ");
        sqlFrom.append(" ( " + _getCollection() + AFListeAffilieFraisGestionReduitManager.F_IDAFFILIATION_ADHESION
                + " = " + _getCollection() + AFListeAffilieFraisGestionReduitManager.F_IDAFFILIATION_AFFILIATION
                + " ) ");

        sqlFrom.append(" INNER JOIN ");
        sqlFrom.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.T_COTISATION);
        sqlFrom.append(" ON ");
        sqlFrom.append(" ( " + _getCollection() + AFListeAffilieFraisGestionReduitManager.F_IDADHESION_COTISATION
                + " = " + _getCollection() + AFListeAffilieFraisGestionReduitManager.F_IDADHESION_ADHESION + " ) ");

        sqlFrom.append(" INNER JOIN ");
        sqlFrom.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.T_ASSURANCE);
        sqlFrom.append(" ON ");
        sqlFrom.append(" ( " + _getCollection() + AFListeAffilieFraisGestionReduitManager.F_IDASSURANCE_COTISATION
                + " = " + _getCollection() + AFListeAffilieFraisGestionReduitManager.F_IDASSURANCE_ASSURANCE + " ) ");

        sqlFrom.append(" INNER JOIN ");
        sqlFrom.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.T_COMPTE_ANNEXE);
        sqlFrom.append(" ON ");
        sqlFrom.append(" ( " + _getCollection() + AFListeAffilieFraisGestionReduitManager.F_NUMERO_AFFILIE + " = "
                + _getCollection() + AFListeAffilieFraisGestionReduitManager.F_NUMERO_AFFILIE_COMPTE_ANNEXE + " ) ");

        sqlFrom.append(" INNER JOIN ");
        sqlFrom.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.T_CONTENTIEUX);
        sqlFrom.append(" ON ");
        sqlFrom.append(" ( " + _getCollection()
                + AFListeAffilieFraisGestionReduitManager.F_IDCOMPTEANNEXE_COMPTE_ANNEXE + " = " + _getCollection()
                + AFListeAffilieFraisGestionReduitManager.F_IDCOMPTEANNEXE_CONTENTIEUX + " ) ");

        sqlFrom.append(" INNER JOIN ");
        sqlFrom.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.T_HISTORIQUE_CONTENTIEUX);
        sqlFrom.append(" ON ");
        sqlFrom.append(" ( " + _getCollection() + AFListeAffilieFraisGestionReduitManager.F_IDCONTENTIEUX_CONTENTIEUX
                + " = " + _getCollection()
                + AFListeAffilieFraisGestionReduitManager.F_IDCONTENTIEUX_HISTORIQUE_CONTENTIEUX + " ) ");

        sqlFrom.append(" INNER JOIN ");
        sqlFrom.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.T_ETAPE_CONTENTIEUX);
        sqlFrom.append(" ON ");
        sqlFrom.append(" ( " + _getCollection()
                + AFListeAffilieFraisGestionReduitManager.F_IDETAPECONTENTIEUX_HISTORIQUE_CONTENTIEUX + " = "
                + _getCollection() + AFListeAffilieFraisGestionReduitManager.F_IDETAPECONTENTIEUX_ETTAPE_CONTENTIEUX
                + " ) ");

        return sqlFrom.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer sqlOrder = new StringBuffer();

        sqlOrder.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_NUMERO_AFFILIE);
        sqlOrder.append(", ");
        sqlOrder.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_IDCOTISATION);
        sqlOrder.append(", ");
        sqlOrder.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_TYPE_ETAPE_CONTENTIEUX);

        return sqlOrder.toString();

    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        int theCurrentYear = 0;
        try {
            theCurrentYear = JACalendar.getYear(JACalendar.todayJJsMMsAAAA());
        } catch (Exception e) {
            theCurrentYear = 0;
            getSession()
                    .addError(
                            getSession().getLabel("ERREUR_SYSTEM_AFF_FG_REDUIT")
                                    + " : unreachable code : JACalendar.getYear(JACalendar.todayJJsMMsAAAA()) throw an exception : "
                                    + e.toString());
            JadeLogger.error(
                    this,
                    "unreachable code : JACalendar.getYear(JACalendar.todayJJsMMsAAAA()) throw an exception : "
                            + e.toString());

        }

        // Obtenir les affiliations ouvertes durant l'année en cours
        sqlWhere.append(" ( ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_DATE_FIN_AFF + " >= "
                + this._dbWriteDateAMJ(statement.getTransaction(), "01.01." + theCurrentYear));
        sqlWhere.append(" OR ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_DATE_FIN_AFF + " = 0 ");
        sqlWhere.append(" ) ");
        sqlWhere.append(" AND ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_DATE_DEBUT_AFF + " <= "
                + this._dbWriteDateAMJ(statement.getTransaction(), "31.12." + theCurrentYear));

        sqlWhere.append(" AND ");
        // qui ont une cotisation frais administratifs ouverte durant l'année en cours
        // si la cotisation ferme durant l'année et qu'on en ouvre une nouvelle, il y aura deux lignes retournées
        sqlWhere.append(" ( ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_DATE_FIN_COTISATION + " >= "
                + this._dbWriteDateAMJ(statement.getTransaction(), "01.01." + theCurrentYear));
        sqlWhere.append(" OR ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_DATE_FIN_COTISATION + " = 0 ");
        sqlWhere.append(" ) ");
        sqlWhere.append(" AND ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_DATE_DEBUT_COTISATION + " <= "
                + this._dbWriteDateAMJ(statement.getTransaction(), "31.12." + theCurrentYear));

        sqlWhere.append(" AND ");
        // garder uniquement les cas qui ont une assurance frais administratifs
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_TYPE_ASSURANCE + " = "
                + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_ASS_FRAIS_ADMIN));

        sqlWhere.append(" AND ");
        // filter les comptes annexes
        // un même affilié (numéro affilié) peut avoir plusieurs comptes annexes en fonction de ses rôles(AGP,
        // AFFILIE,...)
        // ICI, il faut garder les rôles (AFFILIE : 517002 , AFFILIE_PARITAIRE : 517039 , AFFILIE_PERSONNEL : 517040)
        sqlWhere.append(" ( ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_IDROLE + " = "
                + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.ROLE_AFFILIE));
        sqlWhere.append(" OR ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_IDROLE + " = "
                + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.ROLE_AFFILIE_PARITAIRE));
        sqlWhere.append(" OR ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_IDROLE + " = "
                + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.ROLE_AFFILIE_PERSONNEL));
        sqlWhere.append(" ) ");

        sqlWhere.append(" AND ");
        // filtrer le contentieux afin de garder uniquement les contentieux et leurs étapes (non annulées) pour l'année
        // en cours
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_DATE_EXECUTION_ETAPE_CONTENTIEUX
                + " <= " + this._dbWriteDateAMJ(statement.getTransaction(), "31.12." + theCurrentYear));
        sqlWhere.append(" AND ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_DATE_EXECUTION_ETAPE_CONTENTIEUX
                + " >= " + this._dbWriteDateAMJ(statement.getTransaction(), "01.01." + theCurrentYear));
        sqlWhere.append(" AND ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_ETAPE_CONTENTIEUX_ANNULE
                + " = '2' ");

        sqlWhere.append(" AND ");
        // garder uniquement les sommations (5200034) et les poursuites (5200001)
        sqlWhere.append(" ( ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_TYPE_ETAPE_CONTENTIEUX + " = "
                + this._dbWriteNumeric(statement.getTransaction(), ICOEtape.CS_SOMMATION_ENVOYEE));
        sqlWhere.append(" OR ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_TYPE_ETAPE_CONTENTIEUX + " = "
                + this._dbWriteNumeric(statement.getTransaction(), ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE));
        sqlWhere.append(" ) ");

        sqlWhere.append(" GROUP BY ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_NUMERO_AFFILIE);
        sqlWhere.append(", ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_NOM_AFFILIE_FIRSTPART);
        sqlWhere.append(", ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_NOM_AFFILIE_SECONDPART);
        sqlWhere.append(", ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_GENRE_ASSURANCE);
        sqlWhere.append(", ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_IDCOTISATION);
        sqlWhere.append(", ");
        sqlWhere.append(_getCollection() + AFListeAffilieFraisGestionReduitManager.F_TYPE_ETAPE_CONTENTIEUX);

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFListeAffilieFraisGestionReduit();
    }

}
