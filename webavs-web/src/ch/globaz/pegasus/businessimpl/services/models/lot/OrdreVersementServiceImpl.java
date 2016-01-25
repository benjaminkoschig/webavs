package ch.globaz.pegasus.businessimpl.services.models.lot;

import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.models.lot.OrdreVersement;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForListSearch;
import ch.globaz.pegasus.business.models.lot.OrdreVersementSearch;
import ch.globaz.pegasus.business.services.models.lot.OrdreVersementService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationData;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationHandler;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class OrdreVersementServiceImpl extends PegasusAbstractServiceImpl implements OrdreVersementService {

    private class CleCompose implements Comparable<CleCompose> {
        protected String idTiers;
        protected String nom;
        protected String prenom;

        public CleCompose(String idTiers, String nom, String prenom) {
            this.idTiers = idTiers;
            this.nom = nom;
            this.prenom = prenom;
        }

        @Override
        public int compareTo(CleCompose o) {
            if (JadeStringUtil.isBlank(nom) || JadeStringUtil.isBlank(o.nom)) {
                return idTiers.compareTo(o.idTiers);
            }

            int compareNom = nom.compareTo(o.nom);
            if (compareNom != 0) {
                return compareNom;
            }
            return prenom.compareTo(o.prenom);
        }

        @Override
        public String toString() {
            return idTiers + "(" + nom + " " + prenom + ")";
        }
    }

    @Override
    public ComptabilisationData computedOrdrevrsement(String idPrestation) throws JadePersistenceException,
            JadeApplicationException, JAException {
        return ComptabilisationHandler.generateEcritureForOnePresation(idPrestation);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.lot.OrdreVersementService#
     * count(ch.globaz.pegasus.business.models.lot.OrdreVersementSearch)
     */
    @Override
    public int count(OrdreVersementSearch search) throws OrdreVersementException, JadePersistenceException {
        if (search == null) {
            throw new OrdreVersementException("Unable to count ordreVersement, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    private SortedMap<CleCompose, Map<String, List<OrdreVersementForList>>> createMapOv(
            List<OrdreVersementForList> listOv) {
        sort(listOv);
        SortedMap<CleCompose, Map<String, List<OrdreVersementForList>>> mapOvByTier = new TreeMap<CleCompose, Map<String, List<OrdreVersementForList>>>();

        for (OrdreVersementForList unOV : listOv) {
            Map<String, List<OrdreVersementForList>> ovPourUnTiers = null;
            CleCompose cle = new CleCompose(unOV.getIdTiersRequerant(), unOV.getDesignationRequerant1(),
                    unOV.getDesignationRequerant2());

            if (mapOvByTier.containsKey(cle)) {
                ovPourUnTiers = mapOvByTier.get(cle);
            } else {
                ovPourUnTiers = new HashMap<String, List<OrdreVersementForList>>();
                mapOvByTier.put(cle, ovPourUnTiers);
            }

            List<OrdreVersementForList> ovPourPrestationDuTiers = null;
            if (ovPourUnTiers.containsKey(unOV.getSimpleOrdreVersement().getIdPrestation())) {
                ovPourPrestationDuTiers = ovPourUnTiers.get(unOV.getSimpleOrdreVersement().getIdPrestation());
            } else {
                ovPourPrestationDuTiers = new ArrayList<OrdreVersementForList>();
                ovPourUnTiers.put(unOV.getSimpleOrdreVersement().getIdPrestation(), ovPourPrestationDuTiers);
            }
            ovPourPrestationDuTiers.add(unOV);
        }
        return mapOvByTier;
    }

    public void findComputedOvByIdPrestation() {

    }

    @Override
    public Map<String, List<OrdreVersementForList>> groupByType(List<OrdreVersementForList> listeOVs)
            throws JadePersistenceException, OrdreVersementException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.lot.OrdreVersementService# read(java.lang.String)
     */
    @Override
    public OrdreVersement read(String idOrdreVersement) throws JadePersistenceException, OrdreVersementException {
        if (JadeStringUtil.isEmpty(idOrdreVersement)) {
            throw new OrdreVersementException("Unable to read ordreVersement, the id passed is null!");
        }
        OrdreVersement ordreVersement = new OrdreVersement();
        ordreVersement.setId(idOrdreVersement);
        return (OrdreVersement) JadePersistenceManager.read(ordreVersement);
    }

    @Override
    public OrdreVersementForListSearch search(OrdreVersementForListSearch search) throws JadePersistenceException,
            OrdreVersementException {
        if (search == null) {
            throw new OrdreVersementException(
                    "Unable to search OrdreVersementForList, the search model passed is null!");
        }
        return (OrdreVersementForListSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.lot.OrdreVersementService#
     * search(ch.globaz.pegasus.business.models.lot.OrdreVersementSearch)
     */
    @Override
    public OrdreVersementSearch search(OrdreVersementSearch search) throws JadePersistenceException,
            OrdreVersementException {
        if (search == null) {
            throw new OrdreVersementException("Unable to search ordreVersement, the search model passed is null!");
        }
        return (OrdreVersementSearch) JadePersistenceManager.search(search);
    }

    @Override
    public Map<String, List<OrdreVersement>> searchAndGroupByType(OrdreVersementSearch search)
            throws JadePersistenceException, OrdreVersementException {

        search.setOrderKey(OrdreVersementSearch.ORDRE_BY_IDLOT_CS_TYPE);
        if (search.getSize() == 0) {
            search = this.search(search);
        }
        List<OrdreVersement> listOv = PersistenceUtil.typeSearch(search, search.whichModelClass());

        Map<String, List<OrdreVersement>> hashMap = JadeListUtil.groupBy(listOv,
                new JadeListUtil.Key<OrdreVersement>() {
                    @Override
                    public String exec(OrdreVersement e) {
                        return e.getSimpleOrdreVersement().getCsType();
                    }
                });

        return hashMap;
    }

    @Override
    public List<OrdreVersementForList> searchOvByLot(String idLot) throws JadePersistenceException,
            OrdreVersementException {
        OrdreVersementForListSearch ordreVersementSearch = new OrdreVersementForListSearch();
        ordreVersementSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        ordreVersementSearch.setForIdLot(idLot);
        ordreVersementSearch = this.search(ordreVersementSearch);

        List<OrdreVersementForList> listOv = PersistenceUtil.typeSearch(ordreVersementSearch,
                ordreVersementSearch.whichModelClass());
        return listOv;
    }

    @Override
    public List<OrdreVersementForList> searchOvByPrestation(String idPrestation) throws JadePersistenceException,
            OrdreVersementException {
        OrdreVersementForListSearch ordreVersementSearch = new OrdreVersementForListSearch();
        ordreVersementSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        ordreVersementSearch.setForIdPrestation(idPrestation);
        ordreVersementSearch = this.search(ordreVersementSearch);

        List<OrdreVersementForList> listOv = PersistenceUtil.typeSearch(ordreVersementSearch,
                ordreVersementSearch.whichModelClass());
        return listOv;
    }

    private void sort(List<OrdreVersementForList> listOv) {
        Collections.sort(listOv, new Comparator<OrdreVersementForList>() {

            @Override
            public int compare(OrdreVersementForList o1, OrdreVersementForList o2) {

                int compareIdTiers = o1.getIdTiersRequerant().compareTo(o2.getIdTiersRequerant());

                if (compareIdTiers != 0) {
                    return compareIdTiers;
                }

                if (JadeStringUtil.isBlank(o1.getDesignationRequerant1())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    @Override
    public OrdreVersement update(OrdreVersement ordreVersement) throws JadePersistenceException,
            OrdreVersementException, JadeApplicationServiceNotAvailableException {
        if (ordreVersement == null) {
            throw new OrdreVersementException("Unable to update ordreVersement, the model passed is null!");
        }

        PegasusImplServiceLocator.getSimpleOrdreVersementService().update(ordreVersement.getSimpleOrdreVersement());
        return ordreVersement;
    }

}
