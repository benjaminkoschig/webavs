package ch.globaz.naos.ree.process;

import globaz.globall.db.BSession;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;
import ree.ch.admin.bfs.xmlns.bfs_5054_000102._2.DeliverySalariesSEType;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.naos.ree.domain.CodeSystem;
import ch.globaz.naos.ree.domain.converter.Converter;
import ch.globaz.naos.ree.domain.converter.Converter5054_102;
import ch.globaz.naos.ree.domain.converter.REEBusinessException;
import ch.globaz.naos.ree.domain.pojo.Pojo5054_102;
import ch.globaz.naos.ree.protocol.ProcessProtocol;
import ch.globaz.naos.ree.protocol.ProcessProtocolAndMessages;
import ch.globaz.naos.ree.protocol.ProcessProtocolAndMessages5054;
import ch.globaz.naos.ree.sedex.SedexMessageSender;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.naos.ree.tools.SedexInfo;

public class REEProcess5054_102 extends AbstractREEProcess {

    public REEProcess5054_102(BSession session, SedexInfo sedexInfo, InfoCaisse infoCaisse) {
        super(session, sedexInfo, infoCaisse);
    }

    @Override
    protected ProcessProtocolAndMessages5054 executeProcess(List<String> listIdAffilie,
            SedexMessageSender sedexMessageSender) {

        Map<Integer, ProcessProtocolAndMessages> responseMapByYear = new TreeMap<Integer, ProcessProtocolAndMessages>();

        int anneeFin = new Date().getYear();
        int anneeDepart = anneeFin - 3;

        Converter<Pojo5054_102, DeliverySalariesSEType> converter = new Converter5054_102(getInfoCaisse());

        for (int year = anneeDepart; year < anneeFin; year++) {
            List<Pojo5054_102> list5054102 = new ArrayList<Pojo5054_102>();
            consolideListPojo(list5054102, listIdAffilie, year);

            ProcessProtocol processProtocol = new ProcessProtocol();
            List<DeliverySalariesSEType> contentList = new LinkedList<DeliverySalariesSEType>();
            for (Pojo5054_102 pojo : list5054102) {
                try {
                    contentList.add(converter.convert(pojo));
                } catch (REEBusinessException e) {
                    processProtocol.addError("Donnée non annoncée pour l'affilié [" + pojo.getNumeroAffilie() + "] : "
                            + e.getMessage());
                }
            }
            responseMapByYear.put(year, new ProcessProtocolAndMessages(contentList, processProtocol));
        }

        return new ProcessProtocolAndMessages5054(responseMapByYear);
    }

    private boolean isDifferentDe07(String genreEcriture) {
        return !String.valueOf(CodeSystem.GENRE_ECRITURE_07).equals(genreEcriture);
    }

    private boolean is07CodeSpecialIndependant(String genreEcriture, String genreCodeSpecial) {
        return String.valueOf(CodeSystem.GENRE_ECRITURE_07).equals(genreEcriture)
                && String.valueOf(CodeSystem.CODE_SPECIAL_INDEPENDANT).equals(genreCodeSpecial);
    }

    private void consolideListPojo(List<Pojo5054_102> list5054101, List<String> listIdAffilie, int annee) {

        List<List<String>> splitList = QueryExecutor.split(listIdAffilie, 1000);
        for (List<String> splited : splitList) {
            list5054101.addAll(QueryExecutor.execute(getSql5054202(splited, annee), Pojo5054_102.class, getSession()));
        }

    }

    private String getSql5054202(List<String> affilieSplited, int annee) {

        StringBuilder sql = new StringBuilder();

        sql.append("select ");
        sql.append("tiersAVS.HXNAVS as NSS, ");
        sql.append("tiers.HNIPAY as PAYS, ");
        sql.append("personne.HPTSEX as SEXE, ");
        sql.append("personne.HPDNAI as DATE_NAISSANCE, ");
        sql.append("decision.IADDEB as DATE_DEBUT_PERIODE, ");
        sql.append("decision.IADFIN as DATE_FIN_PERIODE, ");
        sql.append("decision.IADFAC as DATE_DECISION, ");
        sql.append("decision.maiaff as ID_AFFILIATION, ");
        sql.append("decision.IATTDE as GENRE_DECISION, ");
        sql.append("decision.IATGAF as GENRE_AFFILIE, ");
        sql.append("donnesDecision.IHMDCA as MONTANT, ");
        sql.append("affiliation.MALNAF as NUMERO_AFFILIE, ");
        sql.append("particularite.MFTPAR as ACTIVITY ");
        sql.append("from schema.CPDECIP as decision ");
        sql.append("INNER JOIN schema.afaffip AS affiliation ON affiliation.maiaff = decision.MAIAFF ");
        sql.append("INNER JOIN schema.TIPERSP AS personne ON personne.HTITIE = decision.HTITIE ");
        sql.append("INNER JOIN schema.TITIERP AS tiers ON tiers.HTITIE = decision.HTITIE ");
        sql.append("INNER JOIN schema.TIPAVSP AS tiersAVS ON tiersAVS.HTITIE = decision.HTITIE ");
        sql.append("INNER JOIN schema.CPDOCAP AS donnesDecision ON donnesDecision.IAIDEC = decision.IAIDEC and donnesDecision.IHIDCA = 600019 ");
        sql.append("LEFT JOIN schema.AFPARTP AS particularite ON particularite.MAIAFF = decision.MAIAFF and particularite.MFTPAR = 818002 ");
        sql.append("where decision.MAIAFF IN(" + StringUtils.join(affilieSplited, ',') + ") ");

        sql.append("and decision.IAIDEC = ( ");
        sql.append("select IAIDEC from schema.CPDECIP AS dec ");
        sql.append("WHERE ");
        sql.append("dec.maiaff = affiliation.maiaff ");
        sql.append("AND dec.IAANNE = " + annee + " ");
        sql.append("AND dec.IAACTI = '1' ");
        sql.append("and dec.IATGAF in (602001, 602003, 602004, 602006) ");
        sql.append("ORDER BY iadfac DESC, IAIDEC DESC ");
        sql.append("FETCH FIRST ROW ONLY)");

        return sql.toString();
    }
}
