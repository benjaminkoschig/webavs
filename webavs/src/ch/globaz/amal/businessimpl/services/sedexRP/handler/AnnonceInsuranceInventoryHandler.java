package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import ch.gdk_cds.xmlns.pv_5213_000601._3.InsuranceInventoryType;
import ch.gdk_cds.xmlns.pv_5213_000601._3.Message;
import ch.gdk_cds.xmlns.pv_5213_000601._3.ObjectFactory;
import ch.gdk_cds.xmlns.pv_common._3.InsuranceQueryResultType;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.AMMessagesTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTraitementsAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.AnnoncesRPServiceImpl;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Gestionnaire de réception pour les annonces 'Effectif des assurés'
 * 
 * @author cbu
 * @version XSD _1
 */
public class AnnonceInsuranceInventoryHandler extends AnnonceHandlerAbstract {
    private Message insuranceInventory;

    public AnnonceInsuranceInventoryHandler(Message insuranceInventory) {
        this.insuranceInventory = insuranceInventory;
    }

    /**
     * Création du fichier CSV
     * 
     * @param statementType
     * @return
     * @throws Exception
     */
    private String[] _writeFile(List<StringBuffer> listRecords) throws Exception {
        System.setProperty("line.separator", "\r\n");

        // Create file
        FileWriter fstream;
        BufferedWriter out;
        String uuidMsg = JadeUUIDGenerator.createStringUUID();
        String fileName = Jade.getInstance().getPersistenceDir() + "insuranceInventoryMessage_" + uuidMsg + ".csv";
        fstream = new FileWriter(new File(fileName));
        out = new BufferedWriter(fstream);

        try {
            // CSV Line Header
            String lineHeader = "";
            // -------------------------------------------------------------------
            // insuranceInventoryType
            // -------------------------------------------------------------------
            lineHeader += "Jour référence" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Début période" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Fin période" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            // -------------------------------------------------------------------
            // personType
            // -------------------------------------------------------------------
            lineHeader += "NNSS" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Nom" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Prénom" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Sexe" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Date de naissance" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            // -------------------------------------------------------------------
            // addressType
            // -------------------------------------------------------------------
            lineHeader += "AdresseLine1" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "AdresseLine2" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Rue" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Numéro" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Localité" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "NPA" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Pays" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            // -------------------------------------------------------------------
            // insuranceDataType
            // -------------------------------------------------------------------
            lineHeader += "Prime mensuelle" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Ass. accident" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Début rapport assurance" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Fin rapport assurance" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Franchise annuelle" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Groupe tarifaire" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Nom du produit" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Produit bonus" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Groupe âge tarifaire" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Pays tarifaire" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Zone tarifaire CH" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Zone tarifaire ETR" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Région tarifaire CH" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Région tarifaire ETR" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            out.write(lineHeader);
            out.newLine();

            for (StringBuffer currentRecord : listRecords) {
                out.write(currentRecord.toString());
                out.newLine();
            }

        } catch (Exception e) {// Catch exception if any
            System.err.println("Error writing Sedex Statement CSV file: " + e.getMessage());
            throw new Exception("Error writing Sedex Statement CSV file: " + e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new Exception(ex);
            }
        }

        String[] fileNames = new String[2];
        fileNames[0] = fileName;

        return fileNames;
    }

    private void createCSVInsuranceInventoryList(String idTiers) throws Exception {

        InsuranceInventoryType insuranceInventoryType = insuranceInventory.getContent().getInsuranceInventory();

        String inventoryDate = AMSedexRPUtil.getDateXMLToString(insuranceInventoryType.getInventoryDate());
        String beginDate = AMSedexRPUtil.getDateXMLToString(insuranceInventoryType.getBeginDate());
        String endDate = AMSedexRPUtil.getDateXMLToString(insuranceInventoryType.getEndDate());

        List<StringBuffer> listRecords = new ArrayList<StringBuffer>();

        for (InsuranceQueryResultType iqrt : insuranceInventoryType.getInsuranceQueryResult()) {
            StringBuffer sbCsv = new StringBuffer();
            // -------------------------------------------------------------------
            // insuranceInventoryType
            // -------------------------------------------------------------------
            sbCsv.append(inventoryDate + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(beginDate + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(endDate + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            // -------------------------------------------------------------------
            // personType
            // -------------------------------------------------------------------
            sbCsv.append(iqrt.getPerson().getVn() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(iqrt.getPerson().getOfficialName() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(iqrt.getPerson().getFirstName() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            if ("2".equals(iqrt.getPerson().getSex())) {
                sbCsv.append("F" + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            } else {
                sbCsv.append("H" + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            }
            sbCsv.append(AMSedexRPUtil.getDateXMLToString(iqrt.getPerson().getDateOfBirth())
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            // -------------------------------------------------------------------
            // addressType
            // -------------------------------------------------------------------
            sbCsv.append(notNull(iqrt.getPerson().getAddress().getAddressLine1()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(notNull(iqrt.getPerson().getAddress().getAddressLine2()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(iqrt.getPerson().getAddress().getStreet() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(notNull(iqrt.getPerson().getAddress().getHouseNumber()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(iqrt.getPerson().getAddress().getTown() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(iqrt.getPerson().getAddress().getSwissZipCode() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(iqrt.getPerson().getAddress().getCountry() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            // -------------------------------------------------------------------
            // insuranceDataType
            // -------------------------------------------------------------------
            sbCsv.append(iqrt.getInsuranceData().getPremium() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(iqrt.getInsuranceData().isAccident() ? "Oui" : "Non" + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(AMSedexRPUtil.getDateXMLToString(iqrt.getInsuranceData().getContractStartDate())
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(AMSedexRPUtil.getDateXMLToString(iqrt.getInsuranceData().getContractEndDate())
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(notNull(iqrt.getInsuranceData().getFranchise()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(notNull(iqrt.getInsuranceData().getPremiumGroup()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(notNull(iqrt.getInsuranceData().getProductName()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(notNull(iqrt.getInsuranceData().getBonus()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(notNull(iqrt.getInsuranceData().getPremiumAge()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(notNull(iqrt.getInsuranceData().getPremiumCountry()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(notNull(iqrt.getInsuranceData().getPremiumZoneCH()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(notNull(iqrt.getInsuranceData().getPremiumZoneForeign()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(notNull(iqrt.getInsuranceData().getPremiumRegionCH()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(notNull(iqrt.getInsuranceData().getPremiumRegionForeign())
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);

            listRecords.add(sbCsv);
        }

        String fileName[] = _writeFile(listRecords);

        String headerFile = JAXBServices.getInstance().marshal(insuranceInventory, false, false, new Class<?>[] {});
        fileName[1] = headerFile;

        String subject = "Web@Amal - Sedex RP : Annonce 'Effectif des assuré-e-s'";
        String message = "Une annonce de type 'Effectif des assuré-e-s' est arrivée :\n\n";

        AdministrationComplexModel acm = new AdministrationComplexModel();
        acm = TIBusinessServiceLocator.getAdministrationService().read(idTiers);
        String exp = "Inconnu";
        if (!acm.isNew()) {
            exp = acm.getTiers().getDesignation1();
        }

        message += "Expéditeur : " + exp + "\n";
        message += "Date : " + AMSedexRPUtil.getDateXMLToString(insuranceInventory.getHeader().getMessageDate()) + "\n";
        JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
                message, fileName);
    }

    @Override
    public SimpleAnnonceSedex execute() {
        try {
            // Enregistrement des données génériques
            super.recordAnnonce(insuranceInventory.getHeader().getMessageDate());

            // Enregistrement des données spécifiques à l'annonce
            annonceSedex.setMessageEmetteur(insuranceInventory.getHeader().getSenderId());
            annonceSedex.setMessageRecepteur(insuranceInventory.getHeader().getRecipientId());
            annonceSedex.setMessageId(insuranceInventory.getHeader().getMessageId());
            // TODO Content généralement trop grande pour être inséré en DB
            annonceSedex.setMessageHeader(getHeaderMessage(insuranceInventory).toString());
            annonceSedex.setMessageSubType(AMMessagesSubTypesAnnonceSedex.EFFECTIFS_ASSURES.getValue());
            annonceSedex.setMessageType(AMMessagesTypesAnnonceSedex.EFFECTIFS_ASSURES.getValue());
            annonceSedex.setNumeroCourant(insuranceInventory.getHeader().getBusinessProcessId().toString());
            annonceSedex.setNumeroDecision(insuranceInventory.getHeader().getMessageId());
            annonceSedex.setTraitement(AMTraitementsAnnonceSedex.TRAITE_AUTO.getValue());

            String idTiers = AMSedexRPUtil.getIdTiersFromSedexId(insuranceInventory.getHeader().getSenderId());
            annonceSedex.setIdTiersCM(idTiers);

            // Création de la liste par rapport au contenu de l'annonce
            createCSVInsuranceInventoryList(idTiers);

            // Si tout s'est bien passé, on crée l'annonce
            annonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService().create(annonceSedex);
        } catch (Exception e) {
            _setErrorOnReception(annonceSedex, e, insuranceInventory);
        }

        return annonceSedex;
    }

    @Override
    public StringBuffer getDetailsAnnonce() {
        StringBuffer stringBufferInfos = new StringBuffer();

        stringBufferInfos.append("<strong>Données de l'annonce 'Effectif des assurés' :</strong></br>");
        stringBufferInfos.append("<br>");
        stringBufferInfos.append("Mois de référence : "
                + AMSedexRPUtil.getDateXMLToString(insuranceInventory.getContent().getInsuranceInventory()
                        .getInventoryDate()) + "<br>");
        stringBufferInfos.append("Mois de début : "
                + AMSedexRPUtil.getDateXMLToString(insuranceInventory.getContent().getInsuranceInventory()
                        .getBeginDate()) + "<br>");
        stringBufferInfos.append("Mois de fin : "
                + AMSedexRPUtil
                        .getDateXMLToString(insuranceInventory.getContent().getInsuranceInventory().getEndDate())
                + "<br>");
        stringBufferInfos.append("<br>");
        stringBufferInfos.append("***********************************************</br>");

        return stringBufferInfos;
    }

    public String getHeaderMessage(Object message) {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setHeader(((Message) message).getHeader());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            JAXBServices.getInstance().marshal(m, os, false, false, new Class[] {});
        } catch (Exception e) {
            JadeLogger.info(this, e.getMessage());
            e.printStackTrace();
        }
        return JadeStringUtil.decodeUTF8(os.toString());
    }

    private String notNull(Object value) {
        return value == null ? "" : value.toString();
    }
}
