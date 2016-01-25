package globaz.osiris.db.sections;

import globaz.globall.db.BStatement;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.ordres.CAOrdreNonVerseManager;
import java.util.ArrayList;
import java.util.List;

public class CASectionsACompenserManager extends CASectionManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getFrom(BStatement statement) {
        return super._getFrom(statement);
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String superWhere = super._getWhere(statement);
        if (superWhere != null) {
            sql.append(superWhere);
        }

        if (sql.length() != 0) {
            sql.append(" AND ");
        }

        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_ATTENTE_LSVDD);
        sql.append("=");
        sql.append("'2'");

        sql.append(" AND ");

        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDSECTION);
        sql.append(" NOT IN (");
        sql.append(getSqlForOrdreNonVerse(statement));
        sql.append(" AND ");
        sql.append(_getCollection()).append(CAOperation.TABLE_CAOPERP).append(".")
                .append(CAOperation.FIELD_IDCOMPTEANNEXE);
        sql.append("=");
        sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDCOMPTEANNEXE);
        sql.append(")");

        return sql.toString();
    }

    private String getSqlForOrdreNonVerse(BStatement statement) {
        CAOrdreNonVerseManager manager = new CAOrdreNonVerseManager();
        manager.setSession(getSession());

        manager.setForTousOrdresEnAttente(Boolean.TRUE);
        manager.setForOrdresNonRecouvert(Boolean.TRUE);
        manager.setForOrdresNonVerse(Boolean.TRUE);

        List<String> etatIn = new ArrayList<String>();
        etatIn.add(APIOperation.ETAT_COMPTABILISE);
        etatIn.add(APIOperation.ETAT_PROVISOIRE);
        etatIn.add(APIOperation.ETAT_OUVERT);
        manager.setForEtatIn(etatIn);

        return manager.getSql(statement);
    }
}
