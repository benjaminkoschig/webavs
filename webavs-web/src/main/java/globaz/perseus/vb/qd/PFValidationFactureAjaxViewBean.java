package globaz.perseus.vb.qd;

import globaz.framework.bean.JadeAbstractAjaxListFindViewBean;
import globaz.globall.db.BSession;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.perseus.utils.PFUserHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.perseus.business.models.qd.Facture;
import ch.globaz.perseus.business.models.qd.FactureSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class PFValidationFactureAjaxViewBean extends JadeAbstractAjaxListFindViewBean {

    private FactureSearchModel searchModel;
    private String forCsCaisse;
    private String likeNss;

    public String getLikeNss() {
        return likeNss;
    }

    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    public String getForCsCaisse() {
        return forCsCaisse;
    }

    public void setForCsCaisse(String forCsCaisse) {
        this.forCsCaisse = forCsCaisse;
    }

    public PFValidationFactureAjaxViewBean() {
        initList();
    }

    @Override
    public void find() throws Exception {
        // désolé pour ceci
        if (likeNss != null && !likeNss.trim().isEmpty()) {
            searchModel.setLikeNss("756." + likeNss);
        }
        // maintenir les perf si pas de filtre caisse

        if (forCsCaisse != null && !forCsCaisse.trim().isEmpty()) {
            FactureSearchModel searchModelBis = new FactureSearchModel();
            searchModelBis.setForCsEtatFacture(searchModel.getForCsEtatFacture());
            searchModelBis.setForIdGestionnaire(searchModel.getForIdGestionnaire());
            searchModelBis.setForCSTypeQD(searchModel.getForCSTypeQD());
            searchModelBis.setLikeNss(searchModel.getLikeNss());
            searchModelBis.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchModelBis = PerseusServiceLocator.getFactureService().search(searchModelBis);
            // idfacture/iddos
            Map<String, String> factDos = new HashMap<String, String>();
            for (JadeAbstractModel factureSearch : searchModelBis.getSearchResults()) {
                Facture facture1 = (Facture) factureSearch;
                factDos.put(facture1.getId(), facture1.getQd().getQdAnnuelle().getDossier().getId());
            }

            if (!factDos.isEmpty()) {
                factDos = filterCsCaisse(factDos);
                if (factDos.isEmpty()) {
                    // pas de dossier qui respecte le filtre : skipper la search action pour avoir result vide
                    return;
                }
                List<String> idList = new ArrayList<String>(factDos.keySet());
                searchModel.setInIdFacture(idList);
            }
        }
        // searchModel.setNbOfResultMatchingQuery(nbOfResultMatchingQuery);
        searchModel = PerseusServiceLocator.getFactureService().search(searchModel);
    }

    private Map<String, String> filterCsCaisse(Map<String, String> factDos) {
        Map<String, String> dosFact = new HashMap<String, String>();
        List<String> values = new ArrayList<String>();
        // TODO avoir de l'unicité dans les idDossier (value de la map)
        Set<String> uniq = new HashSet<String>(factDos.values());

        List<List<String>> splitList = QueryExecutor.split(uniq, 10);
        for (List<String> splited : splitList) {
            StringBuilder sql = new StringBuilder();
            sql.append("select dem.IBIDOS from schema.PFDEMPCF as dem inner join ( ");
            sql.append(sqlFiltre(splited));
            sql.append(" ) as filtred on filtred.idDemande = dem.IBIDPF");
            sql.append(" and dem.IBCSCA = " + forCsCaisse);
            values.addAll(QueryExecutor.execute(sql.toString(), String.class));
        }
        if (!values.isEmpty()) {
            for (Map.Entry<String, String> entry : factDos.entrySet()) {
                if (values.contains(entry.getValue())) {
                    dosFact.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return dosFact;
    }

    private String sqlFiltre(List<String> inIdDossier) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select  ");
        sql.append("        case");
        sql.append("            when demande.idDateFinEnCours is not null then");
        sql.append("                demande.idDateFinEnCours");
        sql.append("            when demande.idDateFin is not null then");
        sql.append("                demande.idDateFin");
        sql.append("        end as idDemande,");
        sql.append("        demande.idDossier");
        sql.append("    from (");
        sql.append("    select dossier.IAIDOS as idDossier, demandeDateFinNull.ID as idDateFinEnCours, demandeDateFinMax.ID as idDateFin");
        sql.append("      from CCVDQUA.PFDOSSI as dossier");
        sql.append("      left join ");
        sql.append("        (select  max(CCVDQUA.PFDEMPCF.IBIDPF) as id, IBIDOS as idDossier");
        sql.append("           from CCVDQUA.PFDEMPCF");
        sql.append("          where (CCVDQUA.PFDEMPCF.IBDFIN is null or CCVDQUA.PFDEMPCF.IBDFIN = 0) ");
        sql.append("            and CCVDQUA.PFDEMPCF.IBTETD = 55008003 ");
        sql.append("          group by CCVDQUA.PFDEMPCF.IBIDOS");
        sql.append("          having count(CCVDQUA.PFDEMPCF.IBIDPF) = 1   ");
        sql.append("        ) as demandeDateFinNull");
        sql.append("        on  demandeDateFinNull.idDossier = dossier.IAIDOS");
        sql.append("      left join ");
        sql.append("        (");
        sql.append("        select max(dt.IBIDPF) as id, dt.IBIDOS as idDossier");
        sql.append("         from CCVDQUA.PFDEMPCF as dt");
        sql.append("        where dt.IBTIMEDV = (");
        sql.append("            select max(d.IBTIMEDV)");
        sql.append("             from CCVDQUA.PFDEMPCF as d");
        sql.append("            where d.IBDFIN = (");
        sql.append("                select max(CCVDQUA.PFDEMPCF.IBDFIN ) as dateFin");
        sql.append("                   from CCVDQUA.PFDEMPCF ");
        sql.append("                  where (CCVDQUA.PFDEMPCF.IBDFIN is not null and CCVDQUA.PFDEMPCF.IBDFIN <> 0) ");
        sql.append("                    and CCVDQUA.PFDEMPCF.IBTETD = 55008003 ");
        sql.append("                    and CCVDQUA.PFDEMPCF.IBIDOS = d.IBIDOS");
        sql.append("                  group by CCVDQUA.PFDEMPCF.IBIDOS   ");
        sql.append("              )");
        sql.append("              and dt.IBIDOS = d.IBIDOS");
        sql.append("              group by d.IBIDOS ");
        sql.append("            ) ");
        sql.append("            group by dt.IBIDOS");
        sql.append("        ) as demandeDateFinMax");
        sql.append("        on  demandeDateFinMax.idDossier = dossier.IAIDOS");
        sql.append(") as demande ");
        sql.append(" where (demande.idDateFinEnCours is not null OR demande.idDateFin is not null)");
        sql.append(" and demande.idDossier in (" + StringUtils.join(inIdDossier, ',') + ")");
        return sql.toString();
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return searchModel;
    }

    @Override
    public void initList() {
        searchModel = new FactureSearchModel();
    }

    public String displayBeneficiaire(PersonneEtendueComplexModel personneEtendueComplexModel) {
        return PFUserHelper.getDetailAssure((BSession) getISession(), personneEtendueComplexModel);
    }

}
