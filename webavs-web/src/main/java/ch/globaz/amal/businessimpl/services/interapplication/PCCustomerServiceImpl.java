/**
 * 
 */
package ch.globaz.amal.businessimpl.services.interapplication;

import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;
import ch.globaz.amal.business.services.interapplication.PCCustomerService;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author dhi
 * 
 */
public class PCCustomerServiceImpl implements PCCustomerService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.interapplication.PCCustomerService#getAmalSubsidesByPeriodes(java.util.Map)
     */
    @Override
    public Map<String, Map<String, List<SimpleDetailFamille>>> getAmalSubsidesByPeriodes(
            Map<String, List<String>> periodesTiers) {
        // ----------------------------------------------------------------------------------------------------
        // 0) Préparation hashmap de retour et validation du premier paramètre
        // ----------------------------------------------------------------------------------------------------
        Map<String, Map<String, List<SimpleDetailFamille>>> returnedMap = new HashMap<String, Map<String, List<SimpleDetailFamille>>>();
        if ((periodesTiers == null) || (periodesTiers.size() == 0)) {
            return returnedMap;
        }
        // ----------------------------------------------------------------------------------------------------
        // 1) Map de retour à préparer avec les données en entrée
        // ----------------------------------------------------------------------------------------------------
        Iterator<String> periodesIterator = periodesTiers.keySet().iterator();
        while (periodesIterator.hasNext()) {
            String currentPeriode = periodesIterator.next();
            List<String> currentIdTiers = periodesTiers.get(currentPeriode);
            Iterator<String> idTiersIterator = currentIdTiers.iterator();
            // Préparation de la map idTiers-subsides
            Map<String, List<SimpleDetailFamille>> subsidesByTiers = new HashMap<String, List<SimpleDetailFamille>>();
            while (idTiersIterator.hasNext()) {
                String idTiers = idTiersIterator.next();
                // Préparation de la liste des subsides vide
                List<SimpleDetailFamille> listeSubsides = new ArrayList<SimpleDetailFamille>();
                subsidesByTiers.put(idTiers, listeSubsides);
            }
            // ajout de la valeur période - idsTiers - subsides
            returnedMap.put(currentPeriode, subsidesByTiers);
        }
        // ----------------------------------------------------------------------------------------------------
        // 2) Recherche des subsides pour les données en entrée
        // ----------------------------------------------------------------------------------------------------
        Iterator<String> returnedPeriodesIterator = returnedMap.keySet().iterator();
        while (returnedPeriodesIterator.hasNext()) {
            String periode = returnedPeriodesIterator.next();
            String currentYear = periode;
            try {
                currentYear = periode.substring(3);
            } catch (Exception ex) {
                continue;
            }
            Map<String, List<SimpleDetailFamille>> subsidesByTiers = returnedMap.get(periode);
            Iterator<String> subsidesByTiersIterator = subsidesByTiers.keySet().iterator();
            while (subsidesByTiersIterator.hasNext()) {
                String idTiers = subsidesByTiersIterator.next();
                // Recherche des subsides par année et idTiers
                // ----------------------------------------------------------------------------------------------------
                // 3) Recherche du membre de famille correspondant
                // ----------------------------------------------------------------------------------------------------
                SimpleFamilleSearch familleSearch = new SimpleFamilleSearch();
                familleSearch.setForIdTiers(idTiers);
                familleSearch.setDefinedSearchSize(0);
                try {
                    familleSearch = AmalImplServiceLocator.getSimpleFamilleService().search(familleSearch);
                    if (familleSearch.getSize() == 0) {
                        subsidesByTiers.put(idTiers, null);
                        continue;
                    }
                } catch (Exception ex) {
                    continue;
                }
                // ----------------------------------------------------------------------------------------------------
                // 4) Recherche des éventuels subsides pour l'année en cours, affinage au mois donnée en paramètre
                // si nécessaire
                // ----------------------------------------------------------------------------------------------------
                List<SimpleDetailFamille> subsides = new ArrayList<SimpleDetailFamille>();
                for (int iFamille = 0; iFamille < familleSearch.getSize(); iFamille++) {
                    SimpleFamille currentMembre = (SimpleFamille) familleSearch.getSearchResults()[iFamille];
                    String currentIdFamille = currentMembre.getIdFamille();
                    SimpleDetailFamilleSearch subsideSearch = new SimpleDetailFamilleSearch();
                    subsideSearch.setForIdFamille(currentIdFamille);
                    subsideSearch.setForAnneeHistorique(currentYear);
                    subsideSearch.setForCodeActif(true);
                    subsideSearch.setDefinedSearchSize(0);
                    try {
                        subsideSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(subsideSearch);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        continue;
                    }
                    for (int iSubside = 0; iSubside < subsideSearch.getSize(); iSubside++) {
                        SimpleDetailFamille currentSubside = (SimpleDetailFamille) subsideSearch.getSearchResults()[iSubside];
                        if (currentSubside.getRefus()) {
                            continue;
                        }
                        // Ajout du subside dans l'arraylist
                        subsides.add(currentSubside);
                    }
                }
                // ----------------------------------------------------------------------------------------------------
                // 5) Ajout dans les listes
                // ----------------------------------------------------------------------------------------------------
                subsidesByTiers.put(idTiers, subsides);
            }
            returnedMap.put(periode, subsidesByTiers);
        }

        return returnedMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.interapplication.PCCustomerService#getAmalSubsidesForTiers(java.util.List,
     * java.lang.String)
     */
    @Override
    public HashMap<String, List<SimpleDetailFamille>> getAmalSubsidesForTiers(List<String> idTiersToCheck,
            String moisAnneeDecision) {
        // ----------------------------------------------------------------------------------------------------
        // 0) Préparation hashmap de retour et validation du premier paramètre
        // ----------------------------------------------------------------------------------------------------
        HashMap<String, List<SimpleDetailFamille>> returnedMap = new HashMap<String, List<SimpleDetailFamille>>();
        if (idTiersToCheck == null) {
            return returnedMap;
        }
        if (idTiersToCheck.size() == 0) {
            return returnedMap;
        }
        // ----------------------------------------------------------------------------------------------------
        // 1) Parcours des idTiers passés en paramètre
        // ----------------------------------------------------------------------------------------------------
        Iterator<String> idTiersIterator = idTiersToCheck.iterator();
        while (idTiersIterator.hasNext()) {
            String currentIdTiers = idTiersIterator.next();
            if (JadeStringUtil.isBlankOrZero(currentIdTiers)) {
                returnedMap.put(currentIdTiers, new ArrayList<SimpleDetailFamille>());
                continue;
            }
            if (JadeStringUtil.isEmpty(moisAnneeDecision)) {
                returnedMap.put(currentIdTiers, new ArrayList<SimpleDetailFamille>());
                continue;
            }
            if (moisAnneeDecision.length() != 7) {
                returnedMap.put(currentIdTiers, new ArrayList<SimpleDetailFamille>());
                continue;
            }
            String currentYear = moisAnneeDecision;
            try {
                currentYear = moisAnneeDecision.substring(3);
            } catch (Exception ex) {
                ex.printStackTrace();
                returnedMap.put(currentIdTiers, new ArrayList<SimpleDetailFamille>());
                continue;
            }
            // ----------------------------------------------------------------------------------------------------
            // 2) Recherche du membre de famille correspondant
            // ----------------------------------------------------------------------------------------------------
            SimpleFamilleSearch familleSearch = new SimpleFamilleSearch();
            familleSearch.setForIdTiers(currentIdTiers);
            familleSearch.setDefinedSearchSize(0);
            try {
                familleSearch = AmalImplServiceLocator.getSimpleFamilleService().search(familleSearch);
            } catch (Exception ex) {
                ex.printStackTrace();
                returnedMap.put(currentIdTiers, new ArrayList<SimpleDetailFamille>());
                continue;
            }
            // ----------------------------------------------------------------------------------------------------
            // 3) Recherche des éventuels subsides pour l'année en cours, affinage au mois donnée en paramètre
            // si nécessaire
            // ----------------------------------------------------------------------------------------------------
            List<SimpleDetailFamille> subsides = new ArrayList<SimpleDetailFamille>();
            for (int iFamille = 0; iFamille < familleSearch.getSize(); iFamille++) {
                SimpleFamille currentMembre = (SimpleFamille) familleSearch.getSearchResults()[iFamille];
                String currentIdFamille = currentMembre.getIdFamille();
                SimpleDetailFamilleSearch subsideSearch = new SimpleDetailFamilleSearch();
                subsideSearch.setForIdFamille(currentIdFamille);
                subsideSearch.setForAnneeHistorique(currentYear);
                subsideSearch.setForCodeActif(true);
                subsideSearch.setDefinedSearchSize(0);
                try {
                    subsideSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(subsideSearch);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    continue;
                }
                for (int iSubside = 0; iSubside < subsideSearch.getSize(); iSubside++) {
                    SimpleDetailFamille currentSubside = (SimpleDetailFamille) subsideSearch.getSearchResults()[iSubside];
                    if (currentSubside.getRefus()) {
                        continue;
                    }
                    // Ajout du subside dans l'arraylist
                    subsides.add(currentSubside);
                }
            }
            returnedMap.put(currentIdTiers, subsides);
        }

        return returnedMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.interapplication.PCCustomerService#getAmalSubsidesForTiersByPeriodes(java.util
     * .List, java.util.List)
     */
    @Override
    public HashMap<String, HashMap<String, List<SimpleDetailFamille>>> getAmalSubsidesForTiersByPeriodes(
            List<String> periodes, List<String> idTiers) {

        HashMap<String, HashMap<String, List<SimpleDetailFamille>>> returnedMap = new HashMap<String, HashMap<String, List<SimpleDetailFamille>>>();
        if ((periodes == null) || (periodes.size() == 0) || (idTiers == null) || (idTiers.size() == 0)) {
            return returnedMap;
        }

        try {
            Iterator<String> iteratorPeriodes = periodes.iterator();
            while (iteratorPeriodes.hasNext()) {
                String currentPeriode = iteratorPeriodes.next();
                String currentYear = currentPeriode.split("\\.")[1];
                Iterator<String> iteratorTiers = idTiers.iterator();
                while (iteratorTiers.hasNext()) {
                    String currentIdTiers = iteratorTiers.next();
                    List<SimpleDetailFamille> subsides = new ArrayList<SimpleDetailFamille>();
                    HashMap<String, List<SimpleDetailFamille>> subsidesForPeriode = new HashMap<String, List<SimpleDetailFamille>>();
                    // ----------------------------------------------------------------------------------------------------
                    // 0) Recherche du membre de famille existant
                    // ----------------------------------------------------------------------------------------------------
                    SimpleFamilleSearch familleSearch = new SimpleFamilleSearch();
                    familleSearch.setForIdTiers(currentIdTiers);
                    familleSearch.setDefinedSearchSize(0);
                    try {
                        familleSearch = AmalImplServiceLocator.getSimpleFamilleService().search(familleSearch);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        HashMap<String, List<SimpleDetailFamille>> actualValues = returnedMap.get(currentIdTiers);
                        if (actualValues == null) {
                            subsidesForPeriode.put(currentPeriode, subsides);
                            returnedMap.put(currentIdTiers, subsidesForPeriode);
                        } else {
                            actualValues.put(currentPeriode, subsides);
                            returnedMap.put(currentIdTiers, actualValues);
                        }
                        continue;
                    }
                    // ----------------------------------------------------------------------------------------------------
                    // 1) Recherche des éventuels subsides pour l'année en cours, affinage au mois donnée en paramètre
                    // si nécessaire
                    // ----------------------------------------------------------------------------------------------------
                    for (int iFamille = 0; iFamille < familleSearch.getSize(); iFamille++) {
                        SimpleFamille currentMembre = (SimpleFamille) familleSearch.getSearchResults()[iFamille];
                        String currentIdFamille = currentMembre.getIdFamille();
                        SimpleDetailFamilleSearch subsideSearch = new SimpleDetailFamilleSearch();
                        subsideSearch.setForIdFamille(currentIdFamille);
                        subsideSearch.setForAnneeHistorique(currentYear);
                        subsideSearch.setForCodeActif(true);
                        subsideSearch.setDefinedSearchSize(0);
                        try {
                            subsideSearch = AmalImplServiceLocator.getSimpleDetailFamilleService()
                                    .search(subsideSearch);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            continue;
                        }
                        for (int iSubside = 0; iSubside < subsideSearch.getSize(); iSubside++) {
                            SimpleDetailFamille currentSubside = (SimpleDetailFamille) subsideSearch.getSearchResults()[iSubside];
                            if (currentSubside.getRefus()) {
                                continue;
                            }
                            // Ajout du subside dans l'arraylist
                            subsides.add(currentSubside);
                        }
                    }
                    // ----------------------------------------------------------------------------------------------------
                    // 2) Travail sur la map de retour
                    // ----------------------------------------------------------------------------------------------------
                    HashMap<String, List<SimpleDetailFamille>> actualValues = returnedMap.get(currentIdTiers);
                    if (actualValues == null) {
                        subsidesForPeriode.put(currentPeriode, subsides);
                        returnedMap.put(currentIdTiers, subsidesForPeriode);
                    } else {
                        actualValues.put(currentPeriode, subsides);
                        returnedMap.put(currentIdTiers, actualValues);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return returnedMap;
    }

}
