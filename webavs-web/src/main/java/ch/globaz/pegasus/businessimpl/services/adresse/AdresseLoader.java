package ch.globaz.pegasus.businessimpl.services.adresse;

import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAbstractAdresseData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import ch.globaz.common.domaine.Adresse;

public class AdresseLoader {

    private Map<String, List<TIAbstractAdresseData>> cache = new ConcurrentHashMap<String, List<TIAbstractAdresseData>>();

    /**
     * Permet de charger les adresses valables pour un groupe de tiers. Fait la cascade.
     * 
     * 
     * @param idsTiers
     * @param domaine
     * @param type
     * @return
     */
    public Map<String, List<Adresse>> loadLastByIdsTiersAndGroupByIdTiers(List<String> idsTiers, String domaine,
            String type) {
        Map<String, List<Adresse>> map;
        try {
            map = searchByLot(idsTiers, domaine, type, 2000);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de charger les adresses avec le manager ->TIAdresseLoaderManager ",
                    e);
        }

        return map;
    }

    /**
     * This split a list by the limit defined.
     * 
     * @param list The list to split
     * @param limit The number of elements in the list
     * @return the list splited
     */
    public static <T> List<List<T>> split(Collection<? extends T> list, Integer limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("The limit is less than or equal to zero, limit passed: " + limit);
        }
        List<List<T>> listSplit = new ArrayList<List<T>>();
        List<T> temp = new ArrayList<T>();
        int i = 0;
        for (final Iterator<? extends T> iterator = list.iterator(); iterator.hasNext();) {
            if ((i % (limit) == 0) && i > 0) {
                listSplit.add(temp);
                temp = new ArrayList<T>();
            }
            temp.add(iterator.next());
            i++;
        }

        if (!temp.isEmpty()) {
            listSplit.add(temp);
        }

        return listSplit;
    }

    private boolean isAllIdsInCache(Collection<String> idsTiers) {
        List<String> idsNotInCache = new ArrayList<String>();
        for (final Iterator<String> iterator = idsTiers.iterator(); iterator.hasNext();) {
            String id = iterator.next();
            if (!cache.containsKey(id)) {
                idsNotInCache.add(id);
            }
        }
        return idsNotInCache.isEmpty();
    }

    public Map<String, List<Adresse>> searchByLot(Collection<String> idsTiers, String domaine, String type,
            int limitSize) throws Exception {
        List<List<String>> idsSplited = AdresseLoader.split(idsTiers, limitSize);
        if (!isAllIdsInCache(idsTiers)) {
            for (List<String> ids : idsSplited) {
                TIAdresseLoaderManager mgr = new TIAdresseLoaderManager();
                mgr.setForIdsTiers(ids);
                mgr.setOrderBy("tiersHTLDU1, tiersHTLDU2");
                mgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
                mgr.find(BManager.SIZE_NOLIMIT);
                for (int j = 0; j < mgr.size(); j++) {
                    TIAbstractAdresseData entityData = (TIAbstractAdresseData) mgr.getEntity(j);
                    String idTiers = entityData.getIdTiers();
                    if (!cache.containsKey(idTiers)) {
                        cache.put(idTiers, new ArrayList<TIAbstractAdresseData>());
                    }
                    cache.get(idTiers).add(entityData);
                }
            }
        }

        Map<String, List<Adresse>> map = new HashMap<String, List<Adresse>>();
        for (Entry<String, List<TIAbstractAdresseData>> entry : cache.entrySet()) {
            String idExterne = "";

            TIAbstractAdresseData data = _getAdresseData(entry.getValue(), domaine, type, idExterne);
            Adresse addresse;
            if (data != null) {

                if (!map.containsKey(data.getIdTiers())) {
                    map.put(data.getIdTiers(), new ArrayList<Adresse>());
                }
                try {

                    addresse = new Adresse(data.getLocalite(), data.getCasePostale(), data.getAttention(),
                            data.getNpa(), data.getPaysIso(), data.getIdCanton(), data.getTitre_adr(), data.getRue(),
                            data.getNumero(), data.getDesignation1_adr(), data.getDesignation2_adr(),
                            data.getDesignation3_adr(), data.getDesignation4_adr(), data.getIdAdresseUnique(),
                            data.getDesignation1_tiers(), data.getDesignation2_tiers(), data.getDesignation3_tiers(),
                            data.getDesignation4_tiers(), data.getTitre_tiers());
                    map.get(data.getIdTiers()).add(addresse);
                } catch (Exception e) {
                    new TechnicalExceptionWithTiers("Impossible de charger l'adresse id:" + data.getIdAdresseUnique(),
                            data.getIdTiers(), e);
                    map.get(data.getIdTiers()).add(new Adresse());
                }
            }
        }

        return map;
    }

    /*
     * Effectue l'héritage...
     */
    private TIAbstractAdresseData _getAdresseData(List<TIAbstractAdresseData> adresses4Tiers, String domaine,
            String type, String idExterne) {
        TIAbstractAdresseData data = null;

        TreeMap<Integer, String> cascadeDomaine = TIApplication.getCachedCascadesDomaine(
                BSessionUtil.getSessionFromThreadContext()).get(domaine);
        if (cascadeDomaine != null) {
            for (Integer positionDomaine : cascadeDomaine.keySet()) {
                // 1) Domaine CascadeDomaineX, type Y, idExterne
                if (data == null) {
                    data = findAdresse(adresses4Tiers, cascadeDomaine.get(positionDomaine), type, idExterne);
                }
                if (data == null) {
                    // 2) Domaine CascadeDomaineX, type Y
                    data = findAdresse(adresses4Tiers, cascadeDomaine.get(positionDomaine), type);
                }
            }
        }

        if (data == null) {
            // 1) Domaine X, type Y, idExterne
            data = findAdresse(adresses4Tiers, domaine, type, idExterne);
            if (data == null) {
                // 2) Domaine X, type Y
                data = findAdresse(adresses4Tiers, domaine, type);
                if (data == null) {
                    // 3) Domaine X, type courrier,idExterne
                    data = findAdresse(adresses4Tiers, domaine, IConstantes.CS_AVOIR_ADRESSE_COURRIER, idExterne);
                    if (data == null) {
                        // 4) Domaine X, type courrier
                        data = findAdresse(adresses4Tiers, domaine, IConstantes.CS_AVOIR_ADRESSE_COURRIER);
                        if (data == null) {
                            // 5) Domaine std, type Y, idExterne
                            data = findAdresse(adresses4Tiers, IConstantes.CS_APPLICATION_DEFAUT, type, idExterne);
                            if (data == null) {
                                // 6) Domaine std, type Y
                                data = findAdresse(adresses4Tiers, IConstantes.CS_APPLICATION_DEFAUT, type);
                                if (data == null) {
                                    // 7) Domaine std, type courrier, idExterne
                                    data = findAdresse(adresses4Tiers, IConstantes.CS_APPLICATION_DEFAUT,
                                            IConstantes.CS_AVOIR_ADRESSE_COURRIER, idExterne);
                                    if (data == null) {
                                        // 8) Domaine std, type courrier
                                        data = findAdresse(adresses4Tiers, IConstantes.CS_APPLICATION_DEFAUT,
                                                IConstantes.CS_AVOIR_ADRESSE_COURRIER);
                                        if (data == null) {
                                            // 9) domaine std, type domicile
                                            data = findAdresse(adresses4Tiers, IConstantes.CS_APPLICATION_DEFAUT,
                                                    IConstantes.CS_AVOIR_ADRESSE_DOMICILE);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return data;
    }

    /**
     * @param adresses4Tiers
     * @param domaine
     * @param type
     * @return TIAbstractAdresseData
     */
    private TIAbstractAdresseData findAdresse(Collection<TIAbstractAdresseData> adresses4Tiers, String domaine,
            String type) {
        TIAbstractAdresseData data = null;
        for (Iterator<TIAbstractAdresseData> it = adresses4Tiers.iterator(); it.hasNext();) {
            data = it.next();
            if ((data.getTypeAdresse().equals(type)) && ("".equals(data.getIdExterne()))
                    && (data.getIdApplication().equals(domaine))) {
                return data;
            }
        } // for

        return null;
    } // findAdresse

    private TIAbstractAdresseData findAdresse(Collection<TIAbstractAdresseData> adresses4Tiers, String domaine,
            String type, String idExterne) {
        TIAbstractAdresseData data = null;
        for (Iterator<TIAbstractAdresseData> it = adresses4Tiers.iterator(); it.hasNext();) {
            data = it.next();
            if ((data.getTypeAdresse().equals(type)) && (data.getIdExterne().equals(idExterne))
                    && (data.getIdApplication().equals(domaine))) {
                return data;
            }
        }
        return null;
    }

    // /**
    // * @param mgr
    // * @param idTiers
    // * @return
    // */
    // private Collection<TIAbstractAdresseData> findAdresse4Tiers(TIAdresseDataManager mgr, String idTiers) {
    // Collection<TIAbstractAdresseData> adresse4Tiers = new ArrayList<TIAbstractAdresseData>();
    // TIAbstractAdresseData data = null;
    // for (int i = 0; i < mgr.size(); i++) {
    // data = (TIAbstractAdresseData) mgr.getEntity(i);
    // if (data.getIdTiers().equals(idTiers)) {
    // adresse4Tiers.add(data);
    // }
    // }
    // return adresse4Tiers;
    // }

}
