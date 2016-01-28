package globaz.pegasus.vb.renteijapi;

import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.avsAi.ApiType;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.renteijapi.RenteIjApi;
import ch.globaz.pegasus.business.models.renteijapi.RenteIjApiSearch;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAllocationImpotent;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import com.google.gson.Gson;

public class PCAllocationImpotentViewBean extends PCAbstractRequerantDonneeFinanciereViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return
     */
    public String getTypeApiMapGson() {
        Map<String, List<String>> typeApiMap = new HashMap<String, List<String>>();
        for (ApiType apiType : ApiType.values()) {
            List<String> genreEtDegreList = new ArrayList<String>();
            genreEtDegreList.add(apiType.getApiGenre().getValue());
            genreEtDegreList.add(apiType.getApiDegre().getValue());
            typeApiMap.put(apiType.getValue(), genreEtDegreList);
        }

        Gson gson = new Gson();
        String gsonMap = gson.toJson(typeApiMap);
        System.out.println(gsonMap);

        return gsonMap;
    }

    @Override
    public void retrieve() throws Exception {
        super.retrieve();

        // cherche les données financières
        RenteIjApiSearch search = new RenteIjApiSearch();
        search.setForIdDroit(getId());
        search.setForNumeroVersion(getNoVersion());
        search.setWhereKey("forVersionedAllocationImpotent");
        search = PegasusServiceLocator.getDroitService().searchRenteIjApi(search);

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            RenteIjApi donnee = (RenteIjApi) it.next();
            if (donnee.getDonneeFinanciere() instanceof SimpleAllocationImpotent) {
                DroitMembreFamille f = donnee.getMembreFamilleEtendu().getDroitMembreFamille();

                List donneesMembre = (List) donnees.get(f.getId());
                if (donneesMembre == null) {
                    donneesMembre = new ArrayList();
                    donnees.put(f.getId(), donneesMembre);
                }
                donneesMembre.add(donnee);
            }
        }
    }

}
