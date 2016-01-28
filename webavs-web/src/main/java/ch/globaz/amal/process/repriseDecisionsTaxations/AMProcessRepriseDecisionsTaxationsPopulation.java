package ch.globaz.amal.process.repriseDecisionsTaxations;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.webavs.common.CommonNSSFormater;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListe;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListeSearch;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierReprise;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierRepriseSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.jade.process.annotation.BusinessKey;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties;

public class AMProcessRepriseDecisionsTaxationsPopulation implements JadeProcessPopulationInterface,
        JadeProcessPopulationNeedProperties<AMProcessRepriseDecisionsTaxationsEnum> {
    private static JAXBContext context = null;
    private Map<String, String> mapChildrens = null;
    private Unmarshaller u = null;

    public void checker(Map<Enum<?>, String> map) {
        String yearSubside = map.get(AMProcessRepriseDecisionsTaxationsEnum.YEAR_SUBSIDE);
        String yearTaxation = map.get(AMProcessRepriseDecisionsTaxationsEnum.YEAR_TAXATION);
        if (JadeStringUtil.isEmpty(yearSubside)) {
            JadeThread.logError("", "amal.process.repriseDecisionsTaxations.yearSubside.mandatory");
        }
        if (JadeStringUtil.isEmpty(yearTaxation)) {
            JadeThread.logError("", "amal.process.repriseDecisionsTaxations.yearTaxation.mandatory");
        }
    }

    private List<JadeProcessEntity> createArray(SimpleUploadFichierRepriseSearch search) throws Exception {
        List<JadeProcessEntity> entites = new ArrayList<JadeProcessEntity>();
        boolean needTreatment = true;
        int iOffset = 0;
        try {
            while (needTreatment) {
                for (JadeAbstractModel model : search.getSearchResults()) {

                    SimpleUploadFichierReprise upload = (SimpleUploadFichierReprise) model;
                    StringBuffer ios = new StringBuffer(new StringBuffer(upload.getXmlLignes()));

                    ch.globaz.amal.xmlns.am_0001._1.Contribuables cs = (ch.globaz.amal.xmlns.am_0001._1.Contribuables) u
                            .unmarshal(new StreamSource(new StringReader(ios.toString())));

                    for (ch.globaz.amal.xmlns.am_0001._1.Contribuable c : cs.getContribuable()) {
                        JadeProcessEntity entite = new JadeProcessEntity();
                        entite.setIdRef(upload.getId());
                        String desc = "";
                        String nAvs = "";
                        String ndc = "";

                        desc = c.getPersonne().get(0).getNom() + " " + c.getPersonne().get(0).getPrenom();
                        if (c.getPersonne().size() == 2) {
                            desc += " et " + c.getPersonne().get(1).getPrenom();

                            if (!JadeStringUtil.isEmpty(c.getPersonne().get(1).getNomJeuneFille())) {
                                if (!c.getPersonne().get(1).getNomJeuneFille().equals(c.getPersonne().get(1).getNom())) {
                                    desc += " (-" + c.getPersonne().get(1).getNomJeuneFille() + ")";
                                }
                            }
                        }

                        if (mapChildrens.containsKey(c.getNdc())) {
                            desc += " (" + mapChildrens.get(c.getNdc()) + ")";
                        }

                        if (!JadeStringUtil.isBlankOrZero(c.getPersonne().get(0).getNavs13().toString())) {
                            CommonNSSFormater nnssFormater = new CommonNSSFormater();
                            nAvs = nnssFormater.format(c.getPersonne().get(0).getNavs13().toString());
                            desc += " - " + nAvs;
                        }

                        if (!JadeStringUtil.isBlankOrZero(c.getNdc())) {
                            ndc = formatNoContribuable(c.getNdc());
                            desc += " - " + ndc;
                        }

                        entite.setDescription(desc);
                        entite.setValue1(formatNoContribuable(upload.getNoContribuable()));
                        entites.add(entite);
                    }
                }
                iOffset += search.getSize();
                search.setOffset(iOffset);

                if (search.isHasMoreElements()) {
                    needTreatment = true;
                    search = AmalServiceLocator.getSimpleUploadFichierService().search(search);
                } else {
                    needTreatment = false;
                }
            }
        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(), "CreateArray Exception --> " + e.toString());
            throw new Exception(e);
        }

        return entites;
    }

    /**
     * format le numéro au format : xxx.xxx.xxx.xx
     */
    private String formatNoContribuable(String val) {
        if (val == null) {
            return "";
        }

        String str = "";
        for (int i = 0; i < val.length(); i++) {
            str += val.charAt(i);

            switch (i) {
                case 2:
                case 5:
                case 8:
                    str += ".";
                    break;
            }
        }

        return str;
    }

    private void generateMapPersCharges() throws Exception {
        try {
            SimpleUploadFichierRepriseSearch simpleUploadFichierRepriseSearch = new SimpleUploadFichierRepriseSearch();
            simpleUploadFichierRepriseSearch.setDefinedSearchSize(0);
            simpleUploadFichierRepriseSearch.setOrderKey("noContribuable");
            simpleUploadFichierRepriseSearch.setLikeTypeReprise("PCHARG");
            simpleUploadFichierRepriseSearch.setForIdJob("0");
            simpleUploadFichierRepriseSearch = AmalServiceLocator.getSimpleUploadFichierService().search(
                    simpleUploadFichierRepriseSearch);
            mapChildrens = new HashMap<String, String>();
            for (JadeAbstractModel model : simpleUploadFichierRepriseSearch.getSearchResults()) {
                SimpleUploadFichierReprise upload = (SimpleUploadFichierReprise) model;

                String nomsPrenoms = "";
                StringBuffer ios = new StringBuffer(new StringBuffer(upload.getXmlLignes()));
                ch.globaz.amal.xmlns.am_0002._1.Contribuables cs = (ch.globaz.amal.xmlns.am_0002._1.Contribuables) u
                        .unmarshal(new StreamSource(new StringReader(ios.toString())));

                for (ch.globaz.amal.xmlns.am_0002._1.Contribuable c : cs.getContribuable()) {
                    for (ch.globaz.amal.xmlns.am_0002._1.Personne p : c.getTaxations().getTaxation().get(0)
                            .getPersonne()) {
                        if (JadeStringUtil.isBlankOrZero(nomsPrenoms)) {
                            nomsPrenoms += p.getPrenom();
                        } else {
                            nomsPrenoms += ", " + p.getPrenom();
                        }
                    }
                }
                if (!mapChildrens.containsKey(upload.getNoContribuable())) {
                    mapChildrens.put(upload.getNoContribuable(), nomsPrenoms);
                } else {
                    String s_noms = mapChildrens.get(upload.getNoContribuable()).toString();
                    s_noms += ", " + nomsPrenoms;
                    mapChildrens.put(upload.getNoContribuable(), s_noms);
                }
            }
        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(), "CreateArray Exception --> " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @BusinessKey(unique = false, messageKey = "amal.")
    public String getBusinessKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class<AMProcessRepriseDecisionsTaxationsEnum> getEnumForProperties() {
        return AMProcessRepriseDecisionsTaxationsEnum.class;
    }

    @Override
    public String getParametersForUrl(JadeProcessEntity entity) throws JadePersistenceException,
            JadeApplicationException {

        String selectedId = "";

        try {
            SimpleUploadFichierReprise fileUploaded = null;
            fileUploaded = AmalServiceLocator.getSimpleUploadFichierService().read(entity.getIdRef());
            if (!JadeStringUtil.isEmpty(fileUploaded.getXmlLignes())) {
                String customValue = fileUploaded.getCustomValue();
                if (customValue.indexOf(";") >= 0) {
                    String[] infos = customValue.split(";", 4);
                    selectedId = "selectedId=" + infos[0];
                    // if (infos[3].equals("H")) {
                    // selectedId += "&fromHisto=1";
                    // }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (JadeStringUtil.isEmpty(selectedId)) {
            try {
                ContribuableRCListeSearch currentSearch = new ContribuableRCListeSearch();
                if (!JadeStringUtil.isEmpty(entity.getValue1())) {
                    currentSearch.setForNoContribuable(entity.getValue1());
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

    @Override
    public List<JadeProcessEntity> searchPopulation() throws JadePersistenceException, JadeApplicationException {
        try {
            AMProcessRepriseDecisionsTaxationsPopulation.context = JAXBContext.newInstance(
                    ch.globaz.amal.xmlns.am_0001._1.Contribuables.class,
                    ch.globaz.amal.xmlns.am_0002._1.Contribuables.class);

            if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                SimpleUploadFichierRepriseSearch simpleUploadFichierRepriseSearch = new SimpleUploadFichierRepriseSearch();
                simpleUploadFichierRepriseSearch.setDefinedSearchSize(1000);
                simpleUploadFichierRepriseSearch.setOrderKey("nomPrenom");
                simpleUploadFichierRepriseSearch.setLikeTypeReprise("CONTRI");
                simpleUploadFichierRepriseSearch.setForIdJob("0");
                simpleUploadFichierRepriseSearch = AmalServiceLocator.getSimpleUploadFichierService().search(
                        simpleUploadFichierRepriseSearch);

                u = AMProcessRepriseDecisionsTaxationsPopulation.context.createUnmarshaller();
                generateMapPersCharges();

                return createArray(simpleUploadFichierRepriseSearch);

            }
        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(), e.getMessage());
            throw new JadePersistenceException(e.getMessage());
        }

        return null;

    }

    @Override
    public void setProperties(Map<AMProcessRepriseDecisionsTaxationsEnum, String> arg0) {
        // TODO Auto-generated method stub
    }

}
