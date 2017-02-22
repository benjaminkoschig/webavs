package ch.globaz.naos.ree.domain;

import globaz.globall.db.BSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.naos.ree.domain.pojo.PojoSuivi;

public class SuiviCaisseLoader {
    private static final Logger LOG = LoggerFactory.getLogger(SuiviCaisseLoader.class);
    private Map<String, List<PojoSuivi>> cache = new ConcurrentHashMap<String, List<PojoSuivi>>();
    private BSession session;

    public SuiviCaisseLoader(BSession session) {
        this.session = session;
    }

    public void load(List<String> idList) {

        List<PojoSuivi> listSuivi = new ArrayList<PojoSuivi>();
        List<List<String>> splitList = QueryExecutor.split(idList, 1000);
        for (List<String> splited : splitList) {
            listSuivi.addAll(QueryExecutor.execute(getSqlSuiviCaisse(splited), PojoSuivi.class, session));
        }
        for (PojoSuivi pojo : listSuivi) {
            String idAff = pojo.getIdAffilie();
            if (!cache.containsKey(idAff)) {
                cache.put(idAff, new ArrayList<PojoSuivi>());
            }
            cache.get(idAff).add(pojo);
        }
    }

    public PojoSuivi getAfter(String idAffilier) {
        try {
            for (PojoSuivi pojo : cache.get(idAffilier)) {
                if (pojo.getIsNext().booleanValue()) {
                    return pojo;
                }
            }
        } catch (NullPointerException e) {
            LOG.debug("il n'y a pas de suivi caisse [After] pour cette affilie [{}]", idAffilier);
        }
        return null;
    }

    public PojoSuivi getBefore(String idAffilier) {
        try {
            for (PojoSuivi pojo : cache.get(idAffilier)) {
                if (pojo.getIsPrevious().booleanValue()) {
                    return pojo;
                }
            }
        } catch (NullPointerException e) {
            LOG.debug("il n'y a pas de suivi caisse [Before] pour cette affilie [{}]", idAffilier);
        }
        return null;
    }

    private String getSqlSuiviCaisse(List<String> splited) {

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("suivi.MAIAFF as ID_AFFILIE,");
        sql.append("suivi.MYDFIN as DATE_FIN_SUIVI,");
        sql.append("(case when (suivi.MYDFIN > 0 AND aff.MADDEB >= suivi.MYDFIN) then 1 else 0 end) as IS_PREVIOUS,");
        sql.append("suivi.MYDDEB as DATE_DEBUT_SUIVI,");
        sql.append("(case when ( suivi.MYDDEB > 0 AND (case when aff.MADFIN = 0 then 99999999 else aff.MADFIN end) <= suivi.MYDDEB) then 1 else 0 end) as IS_NEXT,");
        sql.append("tiers.HBCADM as CODE_CAISSE ");

        sql.append("from schema.AFSUAFP as suivi ");
        sql.append("inner join schema.AFAFFIP as aff on suivi.MAIAFF = aff.MAIAFF ");
        sql.append("inner join schema.TIADMIP as tiers on suivi.HTITIE = tiers.HTITIE ");
        sql.append("where suivi.MYTGEN = 830001 ");
        sql.append("AND(( suivi.MYDFIN > 0 AND aff.MADDEB >= suivi.MYDFIN) ");
        sql.append("OR ( suivi.MYDDEB > 0 AND (case when aff.MADFIN = 0 then 99999999 else aff.MADFIN end) <= suivi.MYDDEB)) ");
        sql.append("AND suivi.MAIAFF IN(" + StringUtils.join(splited, ',') + ") ");

        return sql.toString();
    }

    public boolean hasBefore(String idAffilie) {
        return getBefore(idAffilie) != null;
    }

    public boolean hasAfter(String idAffilie) {
        return getAfter(idAffilie) != null;
    }

}
