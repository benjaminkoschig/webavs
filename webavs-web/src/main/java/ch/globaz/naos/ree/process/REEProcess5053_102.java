package ch.globaz.naos.ree.process;

import globaz.globall.db.BSession;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import ree.ch.admin.bfs.xmlns.bfs_5053_000102._1.CombinationType;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.naos.ree.domain.converter.Converter;
import ch.globaz.naos.ree.domain.converter.Converter5053_102;
import ch.globaz.naos.ree.domain.converter.REEBusinessException;
import ch.globaz.naos.ree.domain.pojo.Pojo5053_102;
import ch.globaz.naos.ree.protocol.ProcessProtocol;
import ch.globaz.naos.ree.protocol.ProcessProtocolAndMessages;
import ch.globaz.naos.ree.sedex.SedexMessageSender;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.naos.ree.tools.SedexInfo;

public class REEProcess5053_102 extends AbstractREEProcess {

    public REEProcess5053_102(BSession session, SedexInfo sedexInfo, InfoCaisse infoCaisse) {
        super(session, sedexInfo, infoCaisse);
    }

    @Override
    protected ProcessProtocolAndMessages executeProcess(List<String> idAffilie, SedexMessageSender sedexMessageSender) {

        ProcessProtocol processProtocol = new ProcessProtocol();

        // SPLIT
        List<Pojo5053_102> list5053000102 = new ArrayList<Pojo5053_102>();
        List<List<String>> splitList = QueryExecutor.split(idAffilie, 1000);
        for (List<String> splited : splitList) {
            list5053000102.addAll(QueryExecutor.execute(getSqlLiensAffilie5053(splited), Pojo5053_102.class,
                    getSession()));
        }
        // check if principal Aff is in IdList
        removeUnwanted(list5053000102, idAffilie);

        // converts
        List<CombinationType> contentList = new LinkedList<CombinationType>();
        Converter<Pojo5053_102, CombinationType> converter = new Converter5053_102(getInfoCaisse());
        for (Pojo5053_102 pojo : list5053000102) {
            try {
                contentList.add(converter.convert(pojo));
            } catch (REEBusinessException e) {
                processProtocol.addError("Un lien n'a pas pu être annoncée pour l'affilié [" + pojo.getNumeroAffilieP()
                        + "] : " + e.getMessage());
            }
        }

        return new ProcessProtocolAndMessages(contentList, processProtocol);
    }

    /**
     * contrôle et retire de la liste les elements dont l'id d'affilié PRINCIPAL (notion de principal métier) n'est pas
     * dans la liste des ID
     * d'affilié annoncé par le 5053-101
     * 
     * @param liste des pojo remonté par la requête (les affilié LIES sont contrôlé par le IN de cette requête)
     * @param liste des ID affilié de
     */
    private void removeUnwanted(List<Pojo5053_102> listPojo, List<String> listIdAff) {
        List<Pojo5053_102> toRemove = new ArrayList<Pojo5053_102>();
        for (Pojo5053_102 pojo : listPojo) {
            if (!(listIdAff.contains(pojo.getIdAffilieP()))) {
                toRemove.add(pojo);
            }
        }
        listPojo.removeAll(toRemove);
    }

    private String getSqlLiensAffilie5053(List<String> splited) {

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("liens.MWTLIE AS TYPE_LIEN,");
        sql.append("affPrincipal.MAIAFF AS ID_AFFILIE_P, ");
        sql.append("affPrincipal.MALNAF AS NUMERO_AFFILIE_P, ");
        sql.append("affPrincipal.MALFED AS NUMERO_IDE_P,");
        sql.append("affLie.MALNAF AS NUMERO_AFFILIE_L, ");
        sql.append("affLie.MALFED AS NUMERO_IDE_L ");
        sql.append("FROM schema.AFLIENP AS liens ");
        sql.append("INNER JOIN schema.afaffip AS affPrincipal ON liens.MAIAFF = affPrincipal.MAIAFF ");
        sql.append("INNER JOIN schema.afaffip AS affLie ON liens.AFA_MAIAFF = affLie.MAIAFF ");
        sql.append("WHERE (liens.MWDFIN = 0 OR (liens.MWDFIN > 0 ");// --lien en cours");
        sql.append("AND ( liens.MWDFIN = affPrincipal.MADFIN ");
        sql.append("OR liens.MWDFIN = affLie.MADFIN))) ");// --ou fermé avec l'affiliation
        sql.append("AND affLie.MAIAFF IN(" + StringUtils.join(splited, ',') + ") ");
        return sql.toString();
    }
}
