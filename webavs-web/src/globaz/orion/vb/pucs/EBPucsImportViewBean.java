package globaz.orion.vb.pucs;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.naos.services.AFAffiliationServices;
import globaz.orion.utils.EBDanUtils;
import globaz.orion.vb.EBAbstractViewBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.orion.business.constantes.EBProperties;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.businessimpl.services.dan.DanServiceImpl;
import ch.globaz.orion.businessimpl.services.pucs.PucsServiceImpl;
import ch.globaz.xmlns.eb.dan.Dan;
import ch.globaz.xmlns.eb.pucs.PucsEntrySummary;
import com.google.gson.Gson;

/**
 * 
 * @author
 * @revision SCO 10 oct. 2011
 */
public class EBPucsImportViewBean extends EBAbstractViewBean implements FWAJAXViewBeanInterface {

    private static final long serialVersionUID = 1L;
    private Collection<String> idPucsEntry = new ArrayList<String>();
    private Collection<String> idMiseEnGed = new ArrayList<String>();
    private Collection<String> idValidationDeLaDs = new ArrayList<String>();
    private String mode = "";
    private List<PucsFile> pucsList = new ArrayList<PucsFile>();
    private Map<String, List<PucsFile>> mapPucsByNumAffilie = new TreeMap<String, List<PucsFile>>();
    private Boolean isMiseEnGedDefault = false;
    private Boolean isValidationDefault = false;
    private String fusionJson = "";

    public Map<String, List<String>> getPucsToMerge() {
        Gson gson = new Gson();
        fusionJson = fusionJson.replaceAll("%27", "'");
        Map<String, List<String>> map = gson.fromJson(fusionJson, HashMap.class);
        return map;
    }

    /**
     * indique si un user à un niveau de droit (complément codeSecure) suffisant par rapport au niveau de sécurité sur
     * l'affiliation
     * 
     * @return vrai si le user à le droit >= à la sécurité de l'affiliation
     */
    public boolean hasRightAccesSecurity(String numAffilie) {
        if (JadeStringUtil.isBlankOrZero(numAffilie)) {
            return true;
        }
        return AFAffiliationServices.hasRightAccesSecurity(numAffilie);
    }

    @Override
    public void retrieve() throws Exception {
        Iterator<String> it = idPucsEntry.iterator();
        while (it.hasNext()) {
            PucsFile pucsFile = EBDanUtils.getDataFromPucsData(it.next());
            if (pucsFile.getProvenance().isPucs()) {
                PucsEntrySummary pucs = PucsServiceImpl.getPucsEntry(pucsFile.getId(), getSession());
                pucsList.add(EBDanUtils.mapPucsfile(pucs));
            } else if (pucsFile.getProvenance().isDan()) {
                Dan dan = DanServiceImpl.getDanFile(pucsFile.getId(), getSession());
                pucsList.add(EBDanUtils.mapDanfile(dan));
            } else if (pucsFile.getProvenance().isSwissDec()) {
                pucsList.add(pucsFile);
            }
        }
        isMiseEnGedDefault = EBProperties.MISE_EN_GED_DEFAULT.getBooleanValue();
        isValidationDefault = EBProperties.VALIDATION_DEFAULT.getBooleanValue();
        JadeGedFacade.isInstalled();
        getPucsToMerge();
    }

    public Boolean getIsMiseEnGedDefault() {
        return isMiseEnGedDefault;
    }

    public Boolean isMiseEnGedDefault() throws PropertiesException {
        return isMiseEnGedDefault;
    }

    public Boolean isValidationDsDefault() throws PropertiesException {
        return isValidationDefault;
    }

    public void setIdPucsEntry(Collection<String> idPucsEntry) {
        this.idPucsEntry = idPucsEntry;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getFusionJson() {
        return fusionJson;
    }

    public void setFusionJson(String fusionJson) {
        this.fusionJson = fusionJson;
    }

    public Collection<String> getIdMiseEnGed() {
        return idMiseEnGed;
    }

    public void setIdMiseEnGed(Collection<String> idMiseEnGed) {
        this.idMiseEnGed = idMiseEnGed;
    }

    public Collection<String> getIdValidationDeLaDs() {
        return idValidationDeLaDs;
    }

    public void setIdValidationDeLaDs(Collection<String> idValidationDeLaDs) {
        this.idValidationDeLaDs = idValidationDeLaDs;
    }

    public Boolean getIsValidationDefault() {
        return isValidationDefault;
    }

    public static <V extends List<E>, E> Map<String, List<E>> sortByValues(final Map<String, V> map) {
        Comparator<String> valueComparator = new Comparator<String>() {
            @Override
            public int compare(String k1, String k2) {
                int compare = map.get(k2).size() - (map.get(k1)).size();
                // Attention il ne faut pas retourner 0 car si on retourne 0 cela vas écraser l'ancien noeud. On vas
                // donc perdre des infos dans notre map
                if (compare == 0) {
                    compare = k1.compareTo(k2);
                }

                if (compare == 0) {
                    compare = -1;
                }
                return compare;
            }
        };
        Map<String, List<E>> sortedByValues = new TreeMap<String, List<E>>(valueComparator);
        for (Entry<String, V> entry : map.entrySet()) {
            sortedByValues.put(entry.getKey(), entry.getValue());
        }

        return sortedByValues;
    }

    public Map<String, List<PucsFile>> getMapPucsByNumAffilie() {
        if (mapPucsByNumAffilie.isEmpty()) {
            if (pucsList.isEmpty()) {
                for (String pucs : idPucsEntry) {
                    pucsList.add(EBDanUtils.getDataFromPucsData(pucs));
                }
            }

            for (PucsFile pucs : pucsList) {
                String key = pucs.getNumeroAffilie() + "_" + pucs.getAnneeDeclaration() + "_" + pucs.getProvenance()
                        + "_" + pucs.isForTest() + "_" + pucs.isAfSeul();
                if (!mapPucsByNumAffilie.containsKey(key)) {
                    mapPucsByNumAffilie.put(key, new ArrayList<PucsFile>());
                }
                mapPucsByNumAffilie.get(key).add(pucs);
            }
        }

        return sortByValues(mapPucsByNumAffilie);
    }

    public Collection<String> getIdPucsEntry() {
        return idPucsEntry;
    }

    public String getMode() {
        return mode;
    }

    public String serialize(PucsFile pucs) {
        return EBDanUtils.createPucsDataForProcess(pucs);
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return null;
    }

    @Override
    public boolean hasList() {
        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    public boolean isSimulation() {
        return "simulation".equals(mode);
    }

    @Override
    public void setGetListe(boolean getListe) {

    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {

    }

    public String getNumeroInforom() {
        return PucsServiceImpl.NUMERO_INFORM_PUCS_LISIBLE;
    }

}
