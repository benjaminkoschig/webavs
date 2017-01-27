package globaz.pegasus.vb.demande;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.models.demande.ListDemandes;
import ch.globaz.pegasus.business.models.demande.ListDemandesSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCDemandeListViewBean extends BJadePersistentObjectListViewBean {

    private ListDemandesSearch listDemandesSearch = null;
    private Map<String, Droit> mapDroitByIdDemande = new HashMap<String, Droit>();

    public PCDemandeListViewBean() {
        super();
        listDemandesSearch = new ListDemandesSearch();
    }

    @Override
    public void find() throws Exception {
        listDemandesSearch = PegasusServiceLocator.getDemandeService().findDemandeForList(listDemandesSearch);

        List<String> idsDemande = new ArrayList<String>();
        for (JadeAbstractModel model : listDemandesSearch.getSearchResults()) {
            ListDemandes listDemande = (ListDemandes) model;
            idsDemande.add(listDemande.getSimpleDemande().getIdDemande());
        }

        if (!idsDemande.isEmpty()) {
            List<Droit> droits = PegasusServiceLocator.getDroitService()
                    .findCurrentVersionDroitByIdsDemande(idsDemande);
            for (Droit droit : droits) {
                mapDroitByIdDemande.put(droit.getDemande().getSimpleDemande().getIdDemande(), droit);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        if (idx < listDemandesSearch.getSize()) {
            ListDemandes demande = (ListDemandes) listDemandesSearch.getSearchResults()[idx];
            return new PCDemandeViewBean(demande, mapDroitByIdDemande.get(demande.getSimpleDemande().getIdDemande()));
        }
        return new PCDemandeViewBean();
    }

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return listDemandesSearch.getForDateNaissance();
    }

    /**
     * @return the demandeSearch
     */
    public ListDemandesSearch getListDemandesSearch() {
        return listDemandesSearch;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return listDemandesSearch;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        listDemandesSearch.setForDateNaissance(forDateNaissance);
    }

    /**
     * @param demandeSearch
     *            the demandeSearch to set
     */
    public void setListDemandesSearch(ListDemandesSearch listDemandesSearch) {
        this.listDemandesSearch = listDemandesSearch;
    }

}
