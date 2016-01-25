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
import ch.gdk_cds.xmlns.pv_5214_000701._3.Message;
import ch.gdk_cds.xmlns.pv_5214_000701._3.ObjectFactory;
import ch.gdk_cds.xmlns.pv_5214_000701._3.StatementContributionYearType;
import ch.gdk_cds.xmlns.pv_5214_000701._3.StatementPersonPeriodType;
import ch.gdk_cds.xmlns.pv_5214_000701._3.StatementPersonType;
import ch.gdk_cds.xmlns.pv_5214_000701._3.StatementType;
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
 * Gestionnaire pour les annonces 'Décompte annuel'
 * 
 * @author cbu
 * @version XSD _1
 */
public class AnnonceStatementHandler extends AnnonceHandlerAbstract {
    private Message statement;

    public AnnonceStatementHandler(Message statement) {
        this.statement = statement;
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
        String fileName = Jade.getInstance().getPersistenceDir() + "statementMessage_" + uuidMsg + ".csv";
        fstream = new FileWriter(new File(fileName));
        out = new BufferedWriter(fstream);

        try {
            // CSV Line Header
            String lineHeader = "";
            lineHeader += "Jour référence" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Année décompte" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Année prime" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "NNSS" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Nom" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Prénom" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Sexe" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Date de naissance" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "AdresseLine1" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "AdresseLine2" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Rue" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Numéro" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Localité" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "NPA" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Pays" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "DecreeId" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Dernier num courant OE" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Dernier num courant AM" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Mois de début" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Mois de fin" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Prime tarifaire" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
            lineHeader += "Total subsides" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
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

    /**
     * Création de la liste CSV de décompte annuel
     * 
     * @param idTiers
     * @throws Exception
     */
    private void createCSVStatementList(String idTiers) throws Exception {

        StatementType statementType = statement.getContent().getStatement();

        String dateStatement = AMSedexRPUtil.getDateXMLToString(statementType.getStatementDate());
        String yearStatement = AMSedexRPUtil.getDateXMLToString(statementType.getStatementYear());

        List<StringBuffer> listRecords = new ArrayList<StringBuffer>();

        for (StatementContributionYearType scyt : statementType.getStatementContributionYear()) {
            StringBuffer sbCsv = new StringBuffer();

            sbCsv.append(dateStatement + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(yearStatement + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(AMSedexRPUtil.getDateXMLToString(scyt.getYear()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            for (StatementPersonType spt : scyt.getStatementPerson()) {
                StringBuffer sbCsv_L2 = new StringBuffer();
                sbCsv_L2.append("=" + '"' + spt.getPerson().getVn() + '"' + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv_L2.append(spt.getPerson().getOfficialName() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv_L2.append(spt.getPerson().getFirstName() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                if ("2".equals(spt.getPerson().getSex())) {
                    sbCsv_L2.append("F" + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                } else {
                    sbCsv_L2.append("H" + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                }
                sbCsv_L2.append(AMSedexRPUtil.getDateXMLToString(spt.getPerson().getDateOfBirth())
                        + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv_L2.append(notNullString(spt.getPerson().getAddress().getAddressLine1())
                        + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv_L2.append(notNullString(spt.getPerson().getAddress().getAddressLine2())
                        + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv_L2.append(spt.getPerson().getAddress().getStreet() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv_L2.append(notNullString(spt.getPerson().getAddress().getHouseNumber())
                        + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv_L2.append(spt.getPerson().getAddress().getTown() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv_L2.append(spt.getPerson().getAddress().getSwissZipCode() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv_L2.append(spt.getPerson().getAddress().getCountry() + AnnoncesRPServiceImpl.CSV_SEPARATOR);

                for (StatementPersonPeriodType sppt : spt.getStatementPersonPeriod()) {
                    StringBuffer sbCsv_L3 = new StringBuffer();
                    sbCsv_L3.append("=" + '"' + sppt.getDecreeId() + '"' + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                    sbCsv_L3.append(sppt.getLastBusinessProcessIdAgency().toString()
                            + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                    if (!(sppt.getLastBusinessProcessIdInsurance() == null)) {
                        sbCsv_L3.append(notNullString(sppt.getLastBusinessProcessIdInsurance().toString())
                                + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                    } else {
                        sbCsv_L3.append(AnnoncesRPServiceImpl.CSV_SEPARATOR);
                    }
                    sbCsv_L3.append(AMSedexRPUtil.getDateXMLToString(sppt.getBeginMonth())
                            + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                    sbCsv_L3.append(AMSedexRPUtil.getDateXMLToString(sppt.getEndMonth())
                            + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                    sbCsv_L3.append(sppt.getPremium().toString() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                    sbCsv_L3.append(sppt.getAmount().toString() + AnnoncesRPServiceImpl.CSV_SEPARATOR);

                    StringBuffer sbCsvTmp = new StringBuffer(sbCsv.toString() + sbCsv_L2.toString()
                            + sbCsv_L3.toString());
                    listRecords.add(sbCsvTmp);
                }
            }
        }

        String fileName[] = _writeFile(listRecords);

        String headerFile = JAXBServices.getInstance().marshal(statement, false, false, new Class<?>[] {});
        fileName[1] = headerFile;

        String subject = "Web@Amal - Sedex RP : Annonce 'Décompte annuel'";
        String message = "Une annonce de type 'Décompte annuel' est arrivée :\n\n";

        AdministrationComplexModel acm = new AdministrationComplexModel();
        acm = TIBusinessServiceLocator.getAdministrationService().read(idTiers);
        String exp = "Inconnu";
        if (!acm.isNew()) {
            exp = acm.getTiers().getDesignation1();
        }

        message += "Expéditeur : " + exp + "\n";
        message += "Date : " + AMSedexRPUtil.getDateXMLToString(statement.getHeader().getMessageDate()) + "\n";
        JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
                message, fileName);

    }

    @Override
    public SimpleAnnonceSedex execute() {
        String idTiers = null;
        try {
            // Enregistrement des données génériques
            super.recordAnnonce(statement.getHeader().getMessageDate());

            // Enregistrement des données spécifiques à l'annonce
            annonceSedex.setMessageEmetteur(statement.getHeader().getSenderId());
            annonceSedex.setMessageRecepteur(statement.getHeader().getRecipientId());
            annonceSedex.setMessageId(statement.getHeader().getMessageId());
            String headerAsString = getHeaderMessage(statement).toString();
            annonceSedex.setMessageHeader(headerAsString);
            annonceSedex.setMessageSubType(AMMessagesSubTypesAnnonceSedex.DECOMPTE_ANNUEL.getValue());
            annonceSedex.setMessageType(AMMessagesTypesAnnonceSedex.DECOMPTE_ANNUEL.getValue());
            annonceSedex.setNumeroCourant(statement.getHeader().getBusinessProcessId().toString());
            annonceSedex.setNumeroDecision(statement.getHeader().getMessageId());
            annonceSedex.setTraitement(AMTraitementsAnnonceSedex.TRAITE_AUTO.getValue());

            idTiers = AMSedexRPUtil.getIdTiersFromSedexId(statement.getHeader().getSenderId());
            annonceSedex.setIdTiersCM(idTiers);

            // Création de la liste par rapport au contenu de l'annonce
            createCSVStatementList(idTiers);

            // Si tout s'est bien passé, on crée l'annonce
            annonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService().create(annonceSedex);
        } catch (Exception e) {
            _setErrorOnReception(annonceSedex, e, statement);
        }

        return annonceSedex;
    }

    @Override
    public StringBuffer getDetailsAnnonce() {
        StringBuffer stringBufferInfos = new StringBuffer();

        stringBufferInfos.append("<strong>Données du décompte annuel :</strong></br>");
        stringBufferInfos.append("Jour de référence : "
                + AMSedexRPUtil.getDateXMLToString(statement.getContent().getStatement().getStatementDate()) + "</br>");
        stringBufferInfos.append("Année du décompte : "
                + AMSedexRPUtil.getDateXMLToString(statement.getContent().getStatement().getStatementYear()) + "</br>");
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

    private String notNullString(String value) {
        return value == null ? "" : value;
    }
}
