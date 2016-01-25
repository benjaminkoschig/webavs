package globaz.naos.db.statOfas;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import java.io.Serializable;

public class AFInfoRom060StatistiquesOfasCotisationManager extends AFInfoRom060StatistiquesOfasManager implements
        Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer fields = new StringBuffer();

        fields.append(" MATTAF, MBTGEN, COUNT(DISTINCT " + AFInfoRom060StatistiquesOfasManager.affiliation
                + ".MAIAFF) AS NOMBRE, SUM(MONTANT) AS MONTANT");

        return fields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer();

        from.append(super._getFrom(statement));

        /**
         * Ce maanger peut être utilisé pour calculer les cotisations des affiliés non actifs ou des étudiants
         * 
         * TODO Pour l'utiliser pour calculer les cotisations d'un autre type d'affiliés, des adaptations sont à faire
         * changer cptAnnexe + ".IDROLE IN (517002,517040) en cptAnnexe + ".IDROLE IN (517002, 517039, 517040)
         * ci-dessous gérer les effets de bord plusieurs lignes sont retournées pour un affilié indépendant et employeur
         * 
         */
        from.append(" INNER JOIN " + _getCollection() + AFInfoRom060StatistiquesOfasManager.cptAnnexe + " "
                + AFInfoRom060StatistiquesOfasManager.cptAnnexe + " ON ("
                + AFInfoRom060StatistiquesOfasManager.affiliation + ".HTITIE="
                + AFInfoRom060StatistiquesOfasManager.cptAnnexe + ".IDTIERS AND "
                + AFInfoRom060StatistiquesOfasManager.affiliation + ".MALNAF="
                + AFInfoRom060StatistiquesOfasManager.cptAnnexe + ".IDEXTERNEROLE AND "
                + AFInfoRom060StatistiquesOfasManager.cptAnnexe + ".IDROLE IN (517002,517040) )" + " INNER JOIN "
                + _getCollection() + AFInfoRom060StatistiquesOfasManager.operation + " "
                + AFInfoRom060StatistiquesOfasManager.operation + " ON ("
                + AFInfoRom060StatistiquesOfasManager.operation + ".IDCOMPTEANNEXE="
                + AFInfoRom060StatistiquesOfasManager.cptAnnexe + ".IDCOMPTEANNEXE AND "
                + AFInfoRom060StatistiquesOfasManager.operation + ".ETAT = " + APIOperation.ETAT_COMPTABILISE + " AND "
                + AFInfoRom060StatistiquesOfasManager.operation + ".IDCOMPTE="
                + AFInfoRom060StatistiquesOfasManager.assurance + ".MBIRUB)" + " INNER JOIN " + _getCollection()
                + AFInfoRom060StatistiquesOfasManager.journal + " " + AFInfoRom060StatistiquesOfasManager.journal
                + " ON (" + AFInfoRom060StatistiquesOfasManager.operation + ".IDJOURNAL="
                + AFInfoRom060StatistiquesOfasManager.journal + ".IDJOURNAL)");

        return from.toString();
    }

    @Override
    protected String _getGroupBy(BStatement statement) {
        return " GROUP BY MATTAF, MBTGEN ";
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return " MATTAF ";
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append(super._getWhere(statement));

        if (!JadeStringUtil.isBlankOrZero(getForAnnee())) {
            sqlAddCondition(
                    sqlWhere,
                    AFInfoRom060StatistiquesOfasManager.journal + ".DATEVALEURCG BETWEEN "
                            + this._dbWriteNumeric(statement.getTransaction(), getForAnnee()) + "0101 AND "
                            + this._dbWriteNumeric(statement.getTransaction(), getForAnnee()) + "1231");
        }

        sqlWhere.append(_getGroupBy(statement));

        sqlWhere.append(_getHaving());

        return sqlWhere.toString();

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFStatistiquesOfas();
    }

}
