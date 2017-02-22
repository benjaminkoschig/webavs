package ch.globaz.naos.ree.process;

import globaz.globall.db.BSession;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;
import ree.ch.admin.bfs.xmlns.bfs_5054_000101._2.DeliverySalariesIKType;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.naos.ree.domain.CodeSystem;
import ch.globaz.naos.ree.domain.converter.Converter;
import ch.globaz.naos.ree.domain.converter.Converter5054_101;
import ch.globaz.naos.ree.domain.converter.REEBusinessException;
import ch.globaz.naos.ree.domain.pojo.Pojo5054_101;
import ch.globaz.naos.ree.protocol.ProcessProtocol;
import ch.globaz.naos.ree.protocol.ProcessProtocolAndMessages;
import ch.globaz.naos.ree.protocol.ProcessProtocolAndMessages5054;
import ch.globaz.naos.ree.sedex.SedexMessageSender;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.naos.ree.tools.SedexInfo;

public class REEProcess5054_101 extends AbstractREEProcess {

    public REEProcess5054_101(BSession session, SedexInfo sedexInfo, InfoCaisse infoCaisse) {
        super(session, sedexInfo, infoCaisse);
    }

    @Override
    protected ProcessProtocolAndMessages5054 executeProcess(List<String> listIdAffilie,
            SedexMessageSender sedexMessageSender) {

        Map<Integer, ProcessProtocolAndMessages> responseMapByYear = new TreeMap<Integer, ProcessProtocolAndMessages>();

        int anneeFin = new Date().getYear();
        int anneeDepart = anneeFin - 3;

        Converter<Pojo5054_101, DeliverySalariesIKType> converter = new Converter5054_101(getInfoCaisse());

        for (int year = anneeDepart; year < anneeFin; year++) {
            List<Pojo5054_101> list5054101 = new ArrayList<Pojo5054_101>();
            consolideListPojo(list5054101, listIdAffilie, year);
            ProcessProtocol processProtocol = new ProcessProtocol();

            List<DeliverySalariesIKType> contentList = new LinkedList<DeliverySalariesIKType>();
            for (Pojo5054_101 pojo : list5054101) {
                try {
                    // Rejet des cas non voulus
                    if (pojo.getDateAnnonceCentrale() != null
                            && (isDifferentDe07(pojo.getGenreEcriture()) || is07CodeSpecialSalarie(
                                    pojo.getGenreEcriture(), pojo.getGenreCodeSpeciale()))) {

                        if (!CodeSystem.MOIS_PERIODE_CI.contains(pojo.getDateDebutPeriode())) {
                            throw new REEBusinessException("La date de début de période n'est pas reconnue : "
                                    + pojo.getDateDebutPeriode());
                        }

                        if (!CodeSystem.MOIS_PERIODE_CI.contains(pojo.getDateFinPeriode())) {
                            throw new REEBusinessException("La date de fin de période n'est pas reconnue : "
                                    + pojo.getDateFinPeriode());
                        }

                        contentList.add(converter.convert(pojo));
                    }
                } catch (REEBusinessException e) {
                    processProtocol.addError("Donnée non annoncée pour un assuré de l'affilié ["
                            + pojo.getNumeroAffilie() + "] : " + e.getMessage());
                }
            }
            responseMapByYear.put(year, new ProcessProtocolAndMessages(contentList, processProtocol));
        }

        return new ProcessProtocolAndMessages5054(responseMapByYear);
    }

    private boolean isDifferentDe07(String genreEcriture) {
        return !String.valueOf(CodeSystem.GENRE_ECRITURE_07).equals(genreEcriture);
    }

    private boolean is07CodeSpecialSalarie(String genreEcriture, String genreCodeSpecial) {
        return String.valueOf(CodeSystem.GENRE_ECRITURE_07).equals(genreEcriture)
                && String.valueOf(CodeSystem.CODE_SPECIAL_SALARIE).equals(genreCodeSpecial);
    }

    private void consolideListPojo(List<Pojo5054_101> list5054101, List<String> listIdAffilie, int annee) {

        List<List<String>> splitList = QueryExecutor.split(listIdAffilie, 1000);
        for (List<String> splited : splitList) {
            list5054101.addAll(QueryExecutor.execute(getSql50541(splited, annee), Pojo5054_101.class, getSession()));
        }

    }

    private String getSql50541(List<String> affilieSplited, int annee) {

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT  ");
        sql.append("ecr.KBTGEN as GENRE_ECRITURE, ");
        sql.append("ecr.KBTSPE as GENRE_CODE_SPECIALE, ");
        sql.append("ecr.KBTEXT as CODE_EXTOURNE, "); // --SI précence 311001 = extourne
        sql.append("ecr.KBNMOD as DATE_DEBUT_PERIODE, ");
        sql.append("ecr.KBNMOF as DATE_FIN_PERIODE, ");
        sql.append("ecr.KBNANN as ANNEE_ECRITURE, ");
        sql.append("ecr.KBMMON as MONTANT, ");
        sql.append("ecr.KBDCEN as DATE_ANNONCE_CENTRALE, ");
        sql.append("SUBSTR(ecr.KBLESP,1,8) as DATE_CREATION_SPY, ");
        sql.append("ind.KANAVS as NSS, ");
        sql.append("ind.KAIPAY as PAYS, ");
        sql.append("ind.KATSEX as SEXE, ");
        sql.append("ind.KADNAI as DATE_NAISSANCE, ");
        sql.append("aff.MALNAF as NUMERO_AFFILIE, ");
        sql.append("aff.maiaff as ID_AFFILIATION ");

        // Liens entre les différentes tables
        sql.append("FROM schema.CIECRIP AS ecr ");
        sql.append("INNER JOIN schema.CIJOURP AS jou ON jou.KCID = ecr.KCID "); // --Lien sur les journaux pour exclures
                                                                                // certains type d'inscription
        sql.append("INNER JOIN schema.CIINDIP AS ind ON ind.KAIIND = ecr.KAIIND ");// --Lien sur l'êntete CI
        sql.append("INNER JOIN schema.afaffip AS aff ON aff.maiaff = ecr.KBITIE "); // --Lien sur l'affiliation

        // Where
        sql.append("WHERE ");
        sql.append("jou.KCITIN NOT IN (301009,301007,301006,301010) "); // --Ne pas prendre les inscriptiopns chomage,
                                                                        // ijai, apg, assurance militaire
        sql.append("AND ecr.KBNANN = " + annee + " ");

        sql.append("AND ecr.KBTGEN IN (310001, 310006, 310007) "); // Restriction sur le type de revenu salarié
        sql.append("AND aff.maiaff IN(" + StringUtils.join(affilieSplited, ',') + ") ");

        return sql.toString();
    }

}
