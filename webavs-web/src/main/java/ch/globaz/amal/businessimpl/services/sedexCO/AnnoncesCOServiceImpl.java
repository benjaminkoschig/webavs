package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
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
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCOSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.sedexCO.AnnoncesCOService;
import ch.globaz.amal.businessimpl.services.sedexRP.AnnoncesRPServiceImpl;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class AnnoncesCOServiceImpl extends AnnoncesCOReceptionMessage5232_000202_1 implements AnnoncesCOService {

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
        lineHeader += "R�cepteur" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Status" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Date de cr�ation de l'annonce" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "P�riode_d�but" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "P�riode_fin" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Num_Caisse_maladie" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Caisse_maladie" + AnnoncesRPServiceImpl.CSV_SEPARATOR;

        return _writeFile(listRecords, lineHeader, "exportAnnoncesCO.csv");
    }

    /**
     * Retourne un mod�le search contenant les annonces qui correpondent aux crit�res entr�s sur la page sedex_rc.jsp
     * (appel ajax)
     * 
     * @param filters
     *            Filtres � appliquer
     * @param order
     *            Ordre � appliquer
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
            // On ne fait rien, order pas appliqu�
        }

        annonceSedexCOSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        annonceSedexCOSearch = AmalServiceLocator.getComplexAnnonceSedexCOService().search(annonceSedexCOSearch);
        return annonceSedexCOSearch;
    }

    /**
     * Cr�ation du fichier CSV...
     * 
     * @param listRecords
     *            Ligne � �crire dans le fichier
     * @param lineHeader
     *            Ent�te du fichier
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

}
