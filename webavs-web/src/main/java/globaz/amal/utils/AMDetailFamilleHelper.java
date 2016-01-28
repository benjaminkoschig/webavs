package globaz.amal.utils;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.amal.business.exceptions.models.CaisseMaladieException;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladie;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieGroupeRCListe;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieGroupeRCListeSearch;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMDetailFamilleHelper {

    private static Map sortByComparator(Map unsortMap) {

        List list = new LinkedList(unsortMap.entrySet());

        // sort list based on comparator
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        // put sorted list into map again
        // LinkedHashMap make sure order in which keys were inserted
        Map sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public Map<String, Map<String, String>> buildCMList() throws CaisseMaladieException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        HashMap<String, Map<String, String>> mapGroupesCaisses = new HashMap<String, Map<String, String>>();
        HashMap<String, String> mapGroupes = new HashMap<String, String>();

        mapGroupes = getGroupesCM();

        for (String idGroupe : mapGroupes.keySet()) {
            CaisseMaladieSearch caisseMaladieSearch = new CaisseMaladieSearch();
            caisseMaladieSearch.setForIdTiersGroupe(idGroupe);
            if ("0".equals(idGroupe)) {
                caisseMaladieSearch.setWhereKey("rcListeSansGroupeOnlyActifs");
            } else {
                caisseMaladieSearch.setWhereKey("rcListeOnlyActifs");
            }
            caisseMaladieSearch = AmalServiceLocator.getCaisseMaladieService().search(caisseMaladieSearch);
            Map<String, String> mapCaisses = new TreeMap<String, String>();
            for (JadeAbstractModel model : caisseMaladieSearch.getSearchResults()) {
                CaisseMaladie cm = (CaisseMaladie) model;
                if (!mapGroupes.containsKey(cm.getIdTiersCaisse())) {
                    mapCaisses.put(cm.getIdTiersCaisse(), cm.getNomCaisse());
                }
            }
            mapCaisses = AMDetailFamilleHelper.sortByComparator(mapCaisses);
            mapGroupesCaisses.put(idGroupe, mapCaisses);
        }

        return mapGroupesCaisses;
    }

    /**
     * Recherche les groupes de caisse maladie
     * 
     * HashMap<value, libelle>
     * 
     * @param job
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws CaisseMaladieException
     */
    public HashMap<String, String> getGroupesCM() throws CaisseMaladieException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        HashMap<String, String> aReturn = new HashMap<String, String>();
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();

        CaisseMaladieGroupeRCListeSearch caisseMaladieGroupeRCListeSearch = new CaisseMaladieGroupeRCListeSearch();
        // 19150084 == Lien de type "Groupe de caisse maladie"
        caisseMaladieGroupeRCListeSearch.setForTypeLien("19150084");
        caisseMaladieGroupeRCListeSearch = AmalServiceLocator.getCaisseMaladieService().searchGroupe(
                caisseMaladieGroupeRCListeSearch);

        aReturn.put("0", "Sans groupe");
        for (JadeAbstractModel model : caisseMaladieGroupeRCListeSearch.getSearchResults()) {
            CaisseMaladieGroupeRCListe caisseMaladieGroupeRCListe = (CaisseMaladieGroupeRCListe) model;
            if (!aReturn.containsKey(caisseMaladieGroupeRCListe.getId())) {
                aReturn.put(caisseMaladieGroupeRCListe.getId(), caisseMaladieGroupeRCListe.getNomGroupe());
            }
        }

        return aReturn;
    }
}
