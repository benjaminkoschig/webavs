package ch.globaz.naos.ree.process;

import globaz.globall.db.BSession;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.xml.sax.SAXException;
import ree.ch.admin.bfs.xmlns.bfs_5053_000101._1.MasterDataType;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.exceptions.ValidationException;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.naos.ree.domain.converter.Converter;
import ch.globaz.naos.ree.domain.converter.Converter5053_101;
import ch.globaz.naos.ree.domain.converter.REEBusinessException;
import ch.globaz.naos.ree.domain.pojo.Pojo5053_101;
import ch.globaz.naos.ree.protocol.ProcessProtocol;
import ch.globaz.naos.ree.protocol.ProcessProtocolAndMessages;
import ch.globaz.naos.ree.sedex.SedexMessageSender;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.naos.ree.tools.SedexInfo;

public class REEProcess5053_101 extends AbstractREEProcess {

    public REEProcess5053_101(BSession session, SedexInfo sedexInfo, InfoCaisse infoCaisse) {
        super(session, sedexInfo, infoCaisse);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ProcessProtocolAndMessages executeProcess(final List<String> idAffilies,
            SedexMessageSender sedexMessageSender) {

        ProcessProtocol processProtocol = new ProcessProtocol();

        List<Pojo5053_101> list5053000101 = QueryExecutor
                .execute(getSqlAffilie5053(), Pojo5053_101.class, getSession());
        // liste d'idAff pour les process suivants
        idAffilies.addAll(CollectionUtils.collect(list5053000101, new BeanToPropertyValueTransformer("idAffilie")));

        // map d'idTiers/NumeroAffilie pour les loader(adresse) du converter (numAff étant idExterne)
        Map<String, String> ids = new HashMap<String, String>(list5053000101.size());
        for (Pojo5053_101 aff : list5053000101) {
            ids.put(aff.getIdTiers(), aff.getNumeroAffilie());
        }
        Converter<Pojo5053_101, MasterDataType> converter = new Converter5053_101(ids, idAffilies, getInfoCaisse(),
                getSession());

        List<MasterDataType> contentList = new LinkedList<MasterDataType>();
        for (Pojo5053_101 pojo : list5053000101) {
            try {

                MasterDataType data = converter.convert(pojo);
                sedexMessageSender.validateUnitMessage(data);
                contentList.add(data);

            } catch (REEBusinessException e) {
                idAffilies.remove(pojo.getIdAffilie());
                processProtocol.addError("Affiliation non traitée [" + pojo.getNumeroAffilie() + "] : "
                        + e.getMessage());
            } catch (ValidationException e) {
                idAffilies.remove(pojo.getIdAffilie());
                processProtocol.addError("Affiliation non validée [" + pojo.getNumeroAffilie() + "] : "
                        + e.getFormattedMessage());

            } catch (SAXException e) {
                throw new RuntimeException(e);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                String erreur = "IdAffilié = " + pojo.getIdAffilie() + ", idTiers = " + pojo.getIdTiers()
                        + e.toString();
                System.out.println(erreur);
            }
        }

        return new ProcessProtocolAndMessages(contentList, processProtocol);
    }

    private String getSqlAffilie5053() {

        String threeYearsAgo = (new Date().getYear() - 3) + "0101";

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("aff.MAIAFF as ID_AFFILIE,");
        sql.append("tiers.HTITIE as ID_TIERS,");
        sql.append("aff.MALNAF as NUMERO_AFFILIE,");
        sql.append("aff.MADDEB as DATE_DEBUT_AFFILIATION,");
        sql.append("aff.MADFIN as DATE_FIN_AFFILIATION,");
        sql.append("aff.MATMCR as MOTIF_AFFILIATION,");
        sql.append("aff.MATMOT as MOTIF_FIN,");
        sql.append("aff.MALFED as NUMERO_IDE,");
        sql.append("aff.MATJUR as FORME_JURIDIQUE,");
        sql.append("aff.MATTAF as TYPE_ENTREPRISE,");
        sql.append("aff.MATBRA as BRANCHE_ECONOMIQUE,");
        sql.append("tiers.HTLDE1 as NOM_TIERS,");
        sql.append("tiers.HTLDE2 as PRENOM_TIERS,");
        sql.append("pers.HPTSEX as SEXE,");
        sql.append("pers.HPDNAI as DATE_NAISSANCE,");
        sql.append("avs.HXNAVS as NUMERO_N_S_S,");
        sql.append("tiers.HTPPHY as PERSONNE_PHYSIQUE,");
        sql.append("tiers.HTPMOR as PERSONNE_MORALE,");
        sql.append("coti.MEDDEB as DATE_DEBUT_COTISATION,");
        sql.append("coti.MEDFIN as DATE_FIN_COTISATION,");
        sql.append("coti.METMOT as MOTIF_FIN_COTISATION ");
        sql.append("FROM schema.afaffip AS aff ");
        sql.append("inner join schema.titierp AS tiers on tiers.htitie = aff.htitie ");
        sql.append("inner join schema.TIPERSP AS pers on pers.HTITIE = aff.HTITIE ");
        sql.append("inner join schema.TIPAVSP AS avs on avs.HTITIE = aff.HTITIE ");
        sql.append("inner join (SELECT planAff.MAIAFF AS MAIAFF,");
        sql.append("MAX (case when cotisation.MEDFIN = 0 then 99999999 else cotisation.MEDFIN end) AS MEDFIN,");
        // Date de début de coti
        sql.append("(SELECT MIN(cotisation2.MEDDEB) from schema.AFCOTIP AS cotisation2 ");
        sql.append("INNER JOIN schema.AFPLAFP AS planAff2 ON cotisation2.MUIPLA = planAff2.MUIPLA ");
        sql.append("WHERE planAff2.MAIAFF = planAff.MAIAFF ");
        sql.append("AND cotisation2.MBIASS IN ( SELECT assurance.MBIASS ");
        sql.append("FROM schema.AFASSUP AS assurance ");
        sql.append("WHERE assurance.MBTTYP = 812001 ");// --cotisation de type AVS/AI
        sql.append(")) AS MEDDEB,");
        // Motif de fin de la dernière fin de cotisation AVS
        sql.append("(SELECT cotisation3.METMOT from schema.AFCOTIP AS cotisation3 ");
        sql.append("INNER JOIN schema.AFPLAFP AS planAff3 ON cotisation3.MUIPLA = planAff3.MUIPLA ");
        sql.append("WHERE planAff3.MAIAFF = planAff.MAIAFF ");
        sql.append("AND cotisation3.MBIASS IN ( SELECT MBIASS ");
        sql.append("FROM schema.AFASSUP AS assurance ");
        sql.append("WHERE assurance.MBTTYP = 812001 ");// --cotisation de type AVS/AI
        sql.append(")ORDER BY cotisation3.MEDFIN DESC FETCH FIRST 1 ROWS ONLY  ) as METMOT ");

        sql.append("FROM schema.AFCOTIP AS cotisation ");
        sql.append("INNER JOIN schema.AFPLAFP AS planAff ON cotisation.MUIPLA = planAff.MUIPLA ");
        sql.append("WHERE ( cotisation.MEDFIN >= ").append(threeYearsAgo).append(" OR cotisation.MEDFIN = 0)");
        // --coti ouverte ou fermée depuis plus long que année en cours -3ans
        sql.append("AND cotisation.MEDDEB != cotisation.MEDFIN ");// --ne pas tenir compte des coti inactives
        sql.append("AND cotisation.MBIASS IN (");
        sql.append("SELECT assurance.MBIASS ");
        sql.append("FROM schema.AFASSUP AS assurance ");
        sql.append("WHERE assurance.MBTTYP = 812001 ");// --cotisation de type AVS/AI
        sql.append(")GROUP BY planAff.MAIAFF");// --num affilié distinct
        sql.append(") coti on coti.MAIAFF = aff.MAIAFF ");
        sql.append("where ");
        sql.append("aff.mattaf not in (804007,804009,804013)");// --Exclusion de affiliés de type « fichier central », «
                                                               // non soumis » et « provisoire »
        sql.append("AND aff.MADDEB <> aff.MADFIN ");// --Exclusion des affiliations erronées
        sql.append("AND (aff.MADFIN = 0 OR aff.MADFIN >= ").append(threeYearsAgo).append(") ");
        sql.append("AND aff.MABTRA = 2 "); // Exclure les affiliations provisoires (la case à cocher).
        // --Pas de date de radiation ou il contient une
        // date de radiation supérieure ou égale à l’année
        // en cours – 3 ans")

        return sql.toString();
    }

}
