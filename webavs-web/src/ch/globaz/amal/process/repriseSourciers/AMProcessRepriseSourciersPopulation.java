/**
 * 
 */
package ch.globaz.amal.process.repriseSourciers;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.pyxis.util.TINSSFormater;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListe;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListeSearch;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierReprise;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierRepriseSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties;

/**
 * @author dhi
 * 
 */
public class AMProcessRepriseSourciersPopulation implements JadeProcessPopulationInterface,
        JadeProcessPopulationNeedProperties<AMProcessRepriseSourciersEnum> {

    private Map<AMProcessRepriseSourciersEnum, String> data = null;

    @Override
    public String getBusinessKey() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties#getEnumForProperties
     * ()
     */
    @Override
    public Class<AMProcessRepriseSourciersEnum> getEnumForProperties() {
        return AMProcessRepriseSourciersEnum.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface#getParametersForUrl
     * (ch.globaz.jade.process.business.models.process.JadeProcessEntity)
     */
    @Override
    public String getParametersForUrl(JadeProcessEntity entity) throws JadePersistenceException,
            JadeApplicationException {

        String selectedId = "";

        try {
            SimpleUploadFichierReprise fileUploaded = null;
            fileUploaded = AmalServiceLocator.getSimpleUploadFichierService().read(entity.getIdRef());
            if (!JadeStringUtil.isEmpty(fileUploaded.getXmlLignes())) {
                String nomPrenom = fileUploaded.getNomPrenom();
                if (nomPrenom.indexOf(";") >= 0) {
                    String[] infos = nomPrenom.split(";", 3);
                    selectedId = "selectedId=" + infos[0];
                    if (infos[1].equals("H")) {
                        selectedId += "&fromHisto=1";
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (JadeStringUtil.isEmpty(selectedId)) {
            try {
                ContribuableRCListeSearch currentSearch = new ContribuableRCListeSearch();
                if (!JadeStringUtil.isEmpty(entity.getValue1())) {
                    currentSearch.setLikeNss(entity.getValue1());
                    currentSearch.setIsContribuable(true);
                    currentSearch = AmalServiceLocator.getContribuableService().searchRCListe(currentSearch);
                    if (currentSearch.getSize() >= 1) {
                        selectedId = "selectedId="
                                + ((ContribuableRCListe) currentSearch.getSearchResults()[0]).getIdContribuable();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return selectedId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface#searchPopulation()
     */
    @Override
    public List<JadeProcessEntity> searchPopulation() throws JadePersistenceException, JadeApplicationException {

        // Recherche des différents enregistrement effectués lors du parsing du fichier BPM
        SimpleUploadFichierRepriseSearch simpleUploadFichierRepriseSearch = new SimpleUploadFichierRepriseSearch();
        simpleUploadFichierRepriseSearch.setLikeTypeReprise("SOURCIER");
        simpleUploadFichierRepriseSearch.setDefinedSearchSize(1000);
        simpleUploadFichierRepriseSearch.setOrderKey("nomPrenom");
        simpleUploadFichierRepriseSearch = AmalServiceLocator.getSimpleUploadFichierService().search(
                simpleUploadFichierRepriseSearch);

        List<JadeProcessEntity> entites = new ArrayList<JadeProcessEntity>();
        boolean needTreatment = true;
        int iOffset = 0;
        try {
            while (needTreatment) {
                for (JadeAbstractModel model : simpleUploadFichierRepriseSearch.getSearchResults()) {

                    SimpleUploadFichierReprise upload = (SimpleUploadFichierReprise) model;
                    JadeProcessEntity entite = new JadeProcessEntity();
                    entite.setIdRef(upload.getId());
                    String desc = upload.getNomPrenom();
                    entite.setDescription(desc);
                    // Enregistrement de nss/avs formatté dans value 1
                    if (!JadeStringUtil.isEmpty(upload.getNoContribuable())) {
                        try {
                            String nssFormatted = TINSSFormater.format(upload.getNoContribuable(),
                                    TINSSFormater.findType(upload.getNoContribuable()));
                            entite.setValue1(nssFormatted);
                        } catch (Exception e) {
                            entite.setValue1("");
                        }
                    } else {
                        entite.setValue1("");
                    }
                    entites.add(entite);
                }
                iOffset += simpleUploadFichierRepriseSearch.getSize();
                simpleUploadFichierRepriseSearch.setOffset(iOffset);

                if (simpleUploadFichierRepriseSearch.isHasMoreElements()) {
                    needTreatment = true;
                    simpleUploadFichierRepriseSearch = AmalServiceLocator.getSimpleUploadFichierService().search(
                            simpleUploadFichierRepriseSearch);
                } else {
                    needTreatment = false;
                }
            }
        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(), "CreateArray Exception --> " + e.getMessage());
        }

        return entites;
    }

    @Override
    public void setProperties(Map<AMProcessRepriseSourciersEnum, String> arg0) {
        data = arg0;
    }

}
