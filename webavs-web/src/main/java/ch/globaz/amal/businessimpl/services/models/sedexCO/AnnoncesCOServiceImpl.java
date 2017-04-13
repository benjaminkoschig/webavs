package ch.globaz.amal.businessimpl.services.models.sedexCO;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedexCO;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.exceptions.models.annoncesedexco.AnnonceSedexCOException;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCODebiteursAssuresSearch;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCOSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.sedexCO.AnnoncesCOService;
import ch.globaz.amal.businessimpl.services.sedexCO.AnnoncesCOReceptionMessage5234_000401_1;
import ch.globaz.amal.businessimpl.services.sedexCO.AnnoncesCOReceptionMessage5234_000402_1;
import ch.globaz.amal.businessimpl.services.sedexRP.AnnoncesRPServiceImpl;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class AnnoncesCOServiceImpl implements AnnoncesCOService {

    @Override
    public AdministrationSearchComplexModel find(AdministrationSearchComplexModel searchModel)
            throws JadePersistenceException, JadeApplicationException {
        return TIBusinessServiceLocator.getAdministrationService().find(searchModel);
    }

    @Override
    public String exportListAnnonces(String filters, String order) throws JadeApplicationException,
            JadePersistenceException {
        ComplexAnnonceSedexCOSearch annonceSedexCOSearch = _searchAnnoncesForExport(filters, order);
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();

        List<StringBuffer> listRecords = new ArrayList<StringBuffer>();
        for (JadeAbstractModel model : annonceSedexCOSearch.getSearchResults()) {
            ComplexAnnonceSedexCO annonceSedexCO = (ComplexAnnonceSedexCO) model;

            StringBuffer sbCsv = new StringBuffer();
            sbCsv.append(annonceSedexCO.getSimpleAnnonceSedexCO().getIdAnnonceSedexCO()
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedexCO.getSimpleAnnonceSedexCO().getDateAnnonce()
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedexCO.getSimpleAnnonceSedexCO().getMessageType()
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(JadeStringUtil.fillWithZeroes(annonceSedexCO.getSimpleAnnonceSedexCO().getMessageSubType(), 6)
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(AMMessagesSubTypesAnnonceSedexCO.getSubTypeCSLibelle(annonceSedexCO.getSimpleAnnonceSedexCO()
                    .getMessageSubType()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedexCO.getSimpleAnnonceSedexCO().getMessageEmetteur()
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedexCO.getSimpleAnnonceSedexCO().getMessageRecepteur()
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(currentSession.getCodeLibelle(annonceSedexCO.getSimpleAnnonceSedexCO().getStatus())
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedexCO.getSimpleAnnonceSedexCO().getStatementDate()
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedexCO.getSimpleAnnonceSedexCO().getStatementStartDate()
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedexCO.getSimpleAnnonceSedexCO().getStatementEndDate()
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedexCO.getCaisseMaladie().getAdmin().getCodeAdministration()
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedexCO.getCaisseMaladie().getTiers().getDesignation1()
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);

            listRecords.add(sbCsv);
        }

        // CSV Line Header
        String lineHeader = "";
        lineHeader += "idAnnonceSedex" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Date" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Type" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Sous-type" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Sous-type_Libelle" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Emetteur" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Récepteur" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Status" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Date de création de l'annonce" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Période_début" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Période_fin" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Num_Caisse_maladie" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Caisse_maladie" + AnnoncesRPServiceImpl.CSV_SEPARATOR;

        return _writeFile(listRecords, lineHeader, "exportAnnoncesCO.csv");
    }

    /**
     * Retourne un modèle search contenant les annonces qui correpondent aux critères entrés sur la page sedex_rc.jsp
     * (appel ajax)
     * 
     * @param filters
     *            Filtres à appliquer
     * @param order
     *            Ordre à appliquer
     * @return
     * @throws JadePersistenceException
     * @throws AnnonceSedexException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private ComplexAnnonceSedexCOSearch _searchAnnoncesForExport(String filters, String order)
            throws JadePersistenceException, AnnonceSedexCOException, JadeApplicationServiceNotAvailableException {
        Map<String, String> mapFilterValue = new HashMap<String, String>();
        String[] pairs = filters.split(";");
        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split(":");
            if (keyValue.length > 1) {
                mapFilterValue.put(keyValue[0], keyValue[1]);
            }
        }
        ComplexAnnonceSedexCOSearch annonceSedexCOSearch = new ComplexAnnonceSedexCOSearch();
        annonceSedexCOSearch.setWhereKey("rcListe");

        for (String mapKey : mapFilterValue.keySet()) {
            String mapVal = mapFilterValue.get(mapKey);

            if ("forCMIdTiersGroupe".equals(mapKey) && "0".equals(mapVal)) {
                annonceSedexCOSearch.setWhereKey("rcListeSansGroupe");
            } else {
                if (JadeStringUtil.isBlankOrZero(mapVal) || "undefined".equals(mapVal)) {
                    continue;
                }
            }

            if ("setForOrder".equals(mapKey)) {
            } else if ("forMessageType".equals(mapKey)) {
                annonceSedexCOSearch.setForSDXMessageType(mapVal);
            } else if ("inMessageSubType".equals(mapKey)) {
                List<String> listVal = JadeStringUtil.tokenize(mapVal, "|");
                ArrayList<String> arrayListSubTypes = new ArrayList<String>();
                for (String val : listVal) {
                    arrayListSubTypes.add(val);
                }
                annonceSedexCOSearch.setInSDXMessageSubType(arrayListSubTypes);
            } else if ("inSDXStatus".equals(mapKey)) {
                List<String> listVal = JadeStringUtil.tokenize(mapVal, "|");
                ArrayList<String> arrayListStatus = new ArrayList<String>();
                for (String val : listVal) {
                    arrayListStatus.add(val);
                }
                annonceSedexCOSearch.setInSDXStatus(arrayListStatus);
            } else if ("forDateMessageGOE".equals(mapKey)) {
                annonceSedexCOSearch.setForSDXDateAnnonceGOE(mapVal);
            } else if ("forDateMessageLOE".equals(mapKey)) {
                annonceSedexCOSearch.setForSDXDateAnnonceLOE(mapVal);
            } else if ("likeCMNomCaisse".equals(mapKey)) {
                annonceSedexCOSearch.setLikeCMNomCaisse(mapVal);
            } else if ("forCMNumCaisse".equals(mapKey)) {
                annonceSedexCOSearch.setForCMNumCaisse(mapVal);
            } else if ("forCMIdTiersGroupe".equals(mapKey)) {
                annonceSedexCOSearch.setForCMIdTiersGroupe(mapVal);
            }
        }

        try {
            if (!JadeStringUtil.isBlankOrZero(order)) {
                String[] order_splitted = order.split(":");
                if (order_splitted.length > 1) {
                    annonceSedexCOSearch.setOrderKey(order_splitted[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // On ne fait rien, order pas appliqué
        }

        annonceSedexCOSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        annonceSedexCOSearch = AmalServiceLocator.getComplexAnnonceSedexCOService().search(annonceSedexCOSearch);
        return annonceSedexCOSearch;
    }

    /**
     * Création du fichier CSV...
     * 
     * @param listRecords
     *            Ligne à écrire dans le fichier
     * @param lineHeader
     *            Entête du fichier
     * @param fileNameWithExt
     *            Nom du fichier avec l'extension
     * @return
     */
    private String _writeFile(List<StringBuffer> listRecords, String lineHeader, String fileNameWithExt) {
        System.setProperty("line.separator", "\r\n");

        // Create file
        FileWriter fstream;
        BufferedWriter out;
        String file = Jade.getInstance().getPersistenceDir() + fileNameWithExt;
        try {
            fstream = new FileWriter(new File(file));
            out = new BufferedWriter(fstream);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            out.write(lineHeader);
            out.newLine();

            for (StringBuffer currentRecord : listRecords) {
                out.write(currentRecord.toString());
                out.newLine();
            }
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return file;
    }

    @Override
    public String printListAnnonces(String idContribuable, String annee, String idTiersCM, String typeDecompte,
            String fromPeriode, String toPeriode) throws JadeApplicationException, JadePersistenceException {

        if (idContribuable == null || idContribuable.isEmpty()) {
            throw new IllegalArgumentException("Id contribuable non présent !");
        }

        ComplexAnnonceSedexCODebiteursAssuresSearch complexAnnonceSedexCODebiteursAssuresSearch = new ComplexAnnonceSedexCODebiteursAssuresSearch();
        complexAnnonceSedexCODebiteursAssuresSearch.setForIdContribuable(idContribuable);

        List<String> listCriteres = new ArrayList<String>();

        if (!JadeStringUtil.isNull(annee)) {
            complexAnnonceSedexCODebiteursAssuresSearch.setForStatementYear(annee);
            listCriteres.add("Année : " + annee);
        }

        if (!JadeStringUtil.isNull(idTiersCM)) {
            complexAnnonceSedexCODebiteursAssuresSearch.setForIdTiersCM(idTiersCM);
            listCriteres.add("Caisse maladie : " + getNomCaisseMaladie(idTiersCM));
        }

        if (!JadeStringUtil.isNull(typeDecompte)) {
            complexAnnonceSedexCODebiteursAssuresSearch.setForMessageSubtype(typeDecompte);
            if (AMMessagesSubTypesAnnonceSedexCO.DECOMPTE_TRIMESTRIEL.getValue().equals(typeDecompte)) {
                listCriteres.add("Type : trimestriel");
            } else if (AMMessagesSubTypesAnnonceSedexCO.DECOMPTE_FINAL.getValue().equals(typeDecompte)) {
                listCriteres.add("Type : final");
            }
        }

        if (!JadeStringUtil.isNull(fromPeriode)) {
            complexAnnonceSedexCODebiteursAssuresSearch.setForDateAnnonceGOE(fromPeriode);
            listCriteres.add("Depuis : " + fromPeriode);
        }
        if (!JadeStringUtil.isNull(toPeriode)) {
            complexAnnonceSedexCODebiteursAssuresSearch.setForDateAnnonceLOE(toPeriode);
            listCriteres.add("Jusqu'à : " + toPeriode);
        }
        complexAnnonceSedexCODebiteursAssuresSearch = (ComplexAnnonceSedexCODebiteursAssuresSearch) JadePersistenceManager
                .search(complexAnnonceSedexCODebiteursAssuresSearch);

        String senderId = null;
        if (!JadeStringUtil.isNull(idTiersCM)) {
            senderId = AMSedexRPUtil.getSedexIdFromIdTiers(idTiersCM);
        }

        if (AMMessagesSubTypesAnnonceSedexCO.DECOMPTE_TRIMESTRIEL.getValue().equals(typeDecompte)) {
            AnnoncesCOReceptionMessage5234_000401_1 reception401 = new AnnoncesCOReceptionMessage5234_000401_1();
            if (senderId != null) {
                reception401.setSenderId(senderId);
            }
            reception401.setListCriteres(listCriteres);
            reception401.generateFromComplexModel(complexAnnonceSedexCODebiteursAssuresSearch);
        } else if (AMMessagesSubTypesAnnonceSedexCO.DECOMPTE_FINAL.getValue().equals(typeDecompte)) {
            AnnoncesCOReceptionMessage5234_000402_1 reception402 = new AnnoncesCOReceptionMessage5234_000402_1();
            if (senderId != null) {
                reception402.setSenderId(senderId);
            }
            reception402.setListCriteres(listCriteres);
            reception402.generateListFinalFromComplexModel(complexAnnonceSedexCODebiteursAssuresSearch);
        }

        return null;
    }

    protected String getNomCaisseMaladie(String noCaisseMaladie) {
        try {
            AdministrationComplexModel admin = TIBusinessServiceLocator.getAdministrationService()
                    .read(noCaisseMaladie);
            if (!admin.isNew()) {
                return admin.getTiers().getDesignation1();
            } else {
                return "Caisse inconnue";
            }
        } catch (Exception ex) {
            return "Caisse inconnue";
        }
    }

}
