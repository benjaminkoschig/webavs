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

public class PTPosteTravailForTravailleurAjaxViewBean extends JadeAbstractAjaxFindRawSQLForDomain<PosteTravail> {
    private static final long serialVersionUID = -6066190917179742607L;

    private String idTravailleur;

    private String[] params = { PT_ID_POSTE_TRAVAIL, PT_RAISON_SOCIALE_EMPLOYEUR, PT_GENRE, PT_QUALIFICATION,
            PT_CONVENTION, PT_QUALIFICATION, PT_GENRE };
    private QueryParameters queryParameters = new QueryParametersImpl(params);

    public void setIdTravailleur(final String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public String getIdTravailleur() {
        return idTravailleur;
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
        return VulpeculaRepositoryLocator.getPosteTravailRepository().findByIdTravailleurWithPagination(idTravailleur,
                queryParameters, Date.now(), getOffset(), getSize());
    }

    @Override
    public int nbOfResultMathingQuery() {
        return VulpeculaRepositoryLocator.getPosteTravailRepository().countByIdTravailleur(idTravailleur,
                queryParameters, Date.now());
    }
}
