package globaz.orion.vb.pucs;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.services.AFAffiliationServices;
import globaz.orion.utils.EBDanUtils;
import globaz.orion.vb.EBAbstractViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.orion.business.constantes.EBProperties;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.businessimpl.services.pucs.PucsServiceImpl;
import ch.globaz.orion.service.EBPucsFileService;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;
import com.google.gson.Gson;

/**
 * 
 * @author
 * @revision SCO 10 oct. 2011
 */
public class EBPucsImportViewBean extends EBAbstractViewBean implements FWAJAXViewBeanInterface {

    private static final long serialVersionUID = 1L;
    private Collection<String> idMiseEnGed = new ArrayList<String>();
    private Collection<String> idValidationDeLaDs = new ArrayList<String>();
    private String mode = "";
    private Map<String, Collection<PucsFile>> mapPucsByNumAffilie = new TreeMap<String, Collection<PucsFile>>();
    private Boolean isMiseEnGedDefault = false;
    private Boolean isValidationDefault = false;
    private String fusionJson = "";
    private String selectedIds;
    private List<String> listOfSelectedIds = new ArrayList<String>();
    private Collection<PucsFile> pucsFiles = new ArrayList<PucsFile>();

    private Map<String, AFAffiliation> affiliations = new HashMap<String, AFAffiliation>();

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
        AFAffiliation affiliation = affiliations.get(numAffilie);
        if (affiliation != null) {
            return AFAffiliationServices.hasRightAccesSecurity(affiliation, getSession());
        } else {
            return true;
        }
    }

    private Map<String, AFAffiliation> findAffiliations(Collection<PucsFile> list) {
        List<String> numAffiliations = new ArrayList<String>();

        for (PucsFile pucsFile : list) {
            numAffiliations.add(pucsFile.getNumeroAffilie());
        }

        List<AFAffiliation> affiliations = AFAffiliationServices.searchAffiliationByNumeros(numAffiliations,
                BSessionUtil.getSessionFromThreadContext());

        Map<String, AFAffiliation> map = new HashMap<String, AFAffiliation>();

        for (AFAffiliation afAffiliation : affiliations) {
            map.put(afAffiliation.getId(), afAffiliation);
        }
        return map;
    }

    @Override
    public void retrieve() throws Exception {
        if (!JadeStringUtil.isEmpty(selectedIds)) {
            listOfSelectedIds = Arrays.asList(selectedIds.split(","));
        }
        if (!listOfSelectedIds.isEmpty()) {
            pucsFiles = keepATraiter(searchPucsFilesByIds(listOfSelectedIds));
        }
        affiliations = findAffiliations(pucsFiles);
        isMiseEnGedDefault = EBProperties.MISE_EN_GED_DEFAULT.getBooleanValue();
        isValidationDefault = EBProperties.VALIDATION_DEFAULT.getBooleanValue();
        JadeGedFacade.isInstalled();
        getPucsToMerge();
        mapPucsByNumAffilie = toMapPucsByNumAffilie();
    }

    /**
     * Recherche les fichiers PUCS selon la liste des ids passés en paramètre.
     * 
     * @param idsPucs
     * @return
     */
    private Collection<PucsFile> searchPucsFilesByIds(Collection<String> idsPucs) {
        return EBPucsFileService.readByIds(idsPucs, getSession());
    }

    /**
     * Retourne uniquement les fichiers qui sont au statut à traiter.
     * Les autres ne doivent pas pouvoir être fusionnés et importés
     * 
     * @param pucsFiles Liste de fichiers pucs
     */
    private Collection<PucsFile> keepATraiter(Collection<PucsFile> pucsFiles) {
        List<PucsFile> pucsFilesATraiter = new ArrayList<PucsFile>();
        for (PucsFile pucsFile : pucsFiles) {
            if (pucsFile.isTraitable()) {
                pucsFilesATraiter.add(pucsFile);
            }
        }
        return pucsFilesATraiter;
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

    public String getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(String selectedIds) {
        this.selectedIds = selectedIds;
    }

    private Map<String, Collection<PucsFile>> toMapPucsByNumAffilie() {

        return Multimaps.index(pucsFiles, new Function<PucsFile, String>() {
            @Override
            public String apply(PucsFile pucs) {
                return pucs.getNumeroAffilie() + "_" + pucs.getAnneeDeclaration() + "_" + pucs.getProvenance() + "_"
                        + pucs.isForTest() + "_" + pucs.isAfSeul();
            }
        }).asMap();
    }

    public Map<String, Collection<PucsFile>> getMapPucsByNumAffilie() {
        return mapPucsByNumAffilie;
    }

    public void setMapPucsByNumAffilie(Map<String, Collection<PucsFile>> mapPucsByNumAffilie) {
        this.mapPucsByNumAffilie = mapPucsByNumAffilie;
    }

}
