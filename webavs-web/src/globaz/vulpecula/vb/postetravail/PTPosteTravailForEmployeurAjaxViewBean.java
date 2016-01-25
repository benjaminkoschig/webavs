package globaz.vulpecula.vb.postetravail;

import static ch.globaz.vulpecula.repositoriesjade.QueryParametersRegistry.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import ch.globaz.common.vb.JadeAbstractAjaxFindRawSQLForDomain;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.repositories.QueryParameters;
import ch.globaz.vulpecula.repositoriesjade.QueryParametersImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PTPosteTravailForEmployeurAjaxViewBean extends JadeAbstractAjaxFindRawSQLForDomain<PosteTravail> {

    private static final long serialVersionUID = -1880277176555767286L;

    private String idEmployeur;

    private String[] params = { PT_NOM_PRENOM, PT_ID_POSTE_TRAVAIL, PT_ID_EMPLOYEUR, PT_DATE_NAIS, PT_NSS,
            PT_QUALIFICATION, PT_GENRE };
    private QueryParameters queryParameters = new QueryParametersImpl(params);

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(final String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public void setQueryParameters(String queryParameters) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = gson.fromJson(queryParameters, type);
        this.queryParameters = new QueryParametersImpl(map, params);
    }

    public List<PosteTravail> getPostes() {
        return getList();
    }

    @Override
    public PosteTravail getEntity() {
        return new PosteTravail();
    }

    @Override
    public List<PosteTravail> findBySQL() {
        return VulpeculaRepositoryLocator.getPosteTravailRepository().findByIdAffilieWithPagination(idEmployeur,
                queryParameters, Date.now(), getOffset(), getSize());
    }

    @Override
    public int nbOfResultMathingQuery() {
        int number = VulpeculaRepositoryLocator.getPosteTravailRepository().countByIdAffilie(idEmployeur,
                queryParameters, Date.now());
        return number;
    }
}
