package ch.globaz.vulpecula.repositoriesjade.controleemployeur;

import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.Validate;
import ch.globaz.vulpecula.business.models.controleemployeur.ControleEmployeurComplexModel;
import ch.globaz.vulpecula.business.models.controleemployeur.ControleEmployeurSearchComplexModel;
import ch.globaz.vulpecula.business.models.controleemployeur.ControleEmployeurSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.controleemployeur.ControleEmployeur;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.repositories.controleemployeur.ControleEmployeurRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.controleemployeur.converters.ControleEmployeurConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DecompteRepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.util.DBUtil;

public class ControleEmployeurRepositoryJade extends
        RepositoryJade<ControleEmployeur, ControleEmployeurComplexModel, ControleEmployeurSimpleModel> implements
        ControleEmployeurRepository {

    @Override
    public ControleEmployeur findById(String id) {
        Validate.notNull(id);

        ControleEmployeurSearchComplexModel searchModel = new ControleEmployeurSearchComplexModel();
        searchModel.setForId(id);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public DomaineConverterJade<ControleEmployeur, ControleEmployeurComplexModel, ControleEmployeurSimpleModel> getConverter() {
        return ControleEmployeurConverter.getInstance();
    }

    @Override
    public List<ControleEmployeur> findByIdEmployeur(String idEmployeur) {
        Validate.notNull(idEmployeur);

        ControleEmployeurSearchComplexModel searchModel = new ControleEmployeurSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setOrderKey(ControleEmployeurSearchComplexModel.ORDER_BY_DATE_AU_DESC);
        return searchAndFetch(searchModel);
    }

    @Override
    public ControleEmployeur findDernierControleByIdEmployeur(String idEmployeur) {
        Validate.notNull(idEmployeur);

        ControleEmployeurSearchComplexModel searchModel = new ControleEmployeurSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setOrderKey(ControleEmployeurSearchComplexModel.ORDER_BY_DATE_AU_DESC);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public List<ControleEmployeur> findControleEmployeurInPeriode(Periode periode) {
        Validate.notNull(periode);

        ControleEmployeurSearchComplexModel searchModel = new ControleEmployeurSearchComplexModel();
        searchModel.setForDateDebutGOE(periode.getDateDebutAsSwissValue());
        searchModel.setForDateDebutLOE(periode.getDateFinAsSwissValue());
        searchModel.setWhereKey("searchForListe");
        searchModel.setOrderKey(ControleEmployeurSearchComplexModel.ORDER_BY_DATE_AU_DESC);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<Employeur> findEmployeurAControlerParRapportADateControleAu(Date dateControleAu)
            throws JadePersistenceException {
        List<Employeur> employeurs = new ArrayList<Employeur>();
        List<String> ids = new ArrayList<String>();
        String query = " SELECT MAIAFF AS ID_AFFILIATION FROM SCHEMA.AFAFFIP where maiaff in( "
                + " SELECT id_afaffip from SCHEMA.PT_CONTROLES_EMPLOYEURS where id in( " + " SELECT t.id " + " FROM ( "
                + " SELECT id_afaffip, MAX(date_au) as date_au " + " FROM SCHEMA.PT_CONTROLES_EMPLOYEURS "
                + " GROUP BY ID_AFAFFIP " + " ) r " + " INNER JOIN SCHEMA.PT_CONTROLES_EMPLOYEURS t "
                + " ON t.id_afaffip = r.id_afaffip AND t.date_au = r.date_au) " + " and " + " ( " + " DATE_AU<="
                + dateControleAu.getValue() + " AND " + " ( " + " DATE_AU IS NOT NULL " + " AND DATE_AU<>0 " + " ) "
                + " ) " + " ) " + " and " + " ( " + " madfin > " + dateControleAu.getValue() + " or " + " ( "
                + " madfin is null " + " or " + " madfin = 0 " + " ) " + " ) ";
        ArrayList<HashMap<String, Object>> result = DBUtil.executeQuery(query, DecompteRepositoryJade.class);
        for (HashMap<String, Object> value : result) {
            String idAffiliation = String.valueOf(value.get("ID_AFFILIATION"));
            ids.add(idAffiliation);
        }
        for (String id : ids) {
            Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findById(id);
            if (employeur != null) {
                employeurs.add(employeur);
            }
        }
        return employeurs;
    }

    @Override
    public List<Employeur> findEmployeurAControlerTous(Date dateControleAu) throws JadePersistenceException {
        List<Employeur> employeurs = new ArrayList<Employeur>();
        List<String> ids = new ArrayList<String>();
        String query = " SELECT MAIAFF AS ID_AFFILIATION FROM SCHEMA.AFAFFIP where  ( madfin > 20071230 or ( madfin is null or madfin = 0 ) )";
        ArrayList<HashMap<String, Object>> result = DBUtil.executeQuery(query, DecompteRepositoryJade.class);
        for (HashMap<String, Object> value : result) {
            String idAffiliation = String.valueOf(value.get("ID_AFFILIATION"));
            ids.add(idAffiliation);
        }
        for (String id : ids) {
            Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findById(id);
            if (employeur != null) {
                employeurs.add(employeur);
            }
        }
        return employeurs;
    }
}
