package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.sedex.JadeSedexMessageNotHandledException;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.GroupedSedexMessage;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import ch.gdk_cds.xmlns.da_64a_5234_000402._1.HeaderType;
import ch.gdk_cds.xmlns.da_64a_5234_000402._1.Message;
import ch.gdk_cds.xmlns.da_64a_common._1.CertificateOfLossArrivalType;
import ch.gdk_cds.xmlns.da_64a_common._1.CertificateOfLossFinalStatementType;
import ch.gdk_cds.xmlns.da_64a_common._1.CertificateOfLossPaymentType;
import ch.gdk_cds.xmlns.da_64a_common._1.DebtorNPType;
import ch.gdk_cds.xmlns.da_64a_common._1.DebtorType;
import ch.gdk_cds.xmlns.da_64a_common._1.PaymentType;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.constantes.PaymentCategoryEnum;
import ch.globaz.amal.business.constantes.TypesOfLossEnum;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCODebiteursAssures;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCODebiteursAssuresSearch;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCODebiteur;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOPaiements;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOPersonne;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOXML;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexCO.listes.SimpleOutputList_DecomptePaiement_5234_402_1;
import ch.globaz.amal.businessimpl.services.sedexCO.listes.SimpleOutputList_Decompte_5234_401_1;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.configuration.Configuration;
import ch.globaz.simpleoutputlist.configuration.RowDecorator;
import ch.globaz.simpleoutputlist.configuration.RowStyle;
import ch.globaz.simpleoutputlist.core.Details;
import ch.globaz.simpleoutputlist.outimpl.Configurations;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

public class AnnoncesCOReceptionMessage5234_000402_1 extends AnnoncesCOReceptionMessage5234_000401_1 {
    private static final String PACKAGE_CLASS_FOR_READ_SEDEX_DECOMPTE_FINAL = "ch.gdk_cds.xmlns.da_64a_5234_000402._1";
    private String idTiersCaisseMaladie = null;
    private Class<?>[] addClasses = new Class[] { ch.gdk_cds.xmlns.da_64a_5234_000402._1.Message.class };

    /**
     * Préparation des users et mots de passe pour le gestion SEDEX (JadeSedexService.xml)
     * 
     * @param properties
     * @throws JadeDecryptionNotSupportedException
     * @throws JadeEncrypterNotFoundException
     * @throws Exception
     */
    @Override
    @Setup
    public void setUp(Properties properties) throws Exception {

        getUserAndPassSedex(properties);

        JAXBContext jaxbContext = JAXBContext.newInstance(PACKAGE_CLASS_FOR_READ_SEDEX_DECOMPTE_FINAL);
        unmarshaller = jaxbContext.createUnmarshaller();
        marshaller = jaxbContext.createMarshaller();
    }

    @Override
    @OnReceive
    public void importMessages(SedexMessage message) throws JadeSedexMessageNotHandledException {
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), getContext());

            if (message instanceof GroupedSedexMessage) {
                // ---------------------------------------------------------
                // Contrôle de la réception d'un message groupé
                // ---------------------------------------------------------
                GroupedSedexMessage currentGroupedMessage = (GroupedSedexMessage) message;
                Iterator<SimpleSedexMessage> messagesIterator = currentGroupedMessage.iterator();
                while (messagesIterator.hasNext()) {
                    importMessagesSingle(messagesIterator.next());
                }
            } else if (message instanceof SimpleSedexMessage) {
                // ---------------------------------------------------------
                // Contrôle de la réception d'un message simple
                // ---------------------------------------------------------
                importMessagesSingle((SimpleSedexMessage) message);
            } else {
                JadeLogger
                        .error(this,
                                "Une erreur s'est produite pendant la lecture d'une annonce CO : il ne s'agit pas d'un SimpleSedexMessage ou GroupedSedexMessage");
            }
        } catch (Exception e) {
            JadeLogger.error(this, "SEDEX: error receiving message ");
            JadeLogger.error(this, e);
            throw new JadeSedexMessageNotHandledException("Erreur dans la réception d'une annonce SEDEX CO: ");
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

    }

    /**
     * Méthode de lecture du message sedex en réception, et traitement
     * 
     * @param currentSimpleMessage
     * @return
     * @throws JadeSedexMessageNotHandledException
     */
    @Override
    protected void importMessagesSingle(SimpleSedexMessage currentSimpleMessage)
            throws JadeSedexMessageNotHandledException {
        File fileDecompte = null;
        try {
            jaxbs = JAXBServices.getInstance();
            Message message = (Message) jaxbs.unmarshal(currentSimpleMessage.fileLocation, false, true, addClasses);
            senderId = message.getHeader().getSenderId();
            // Sauvegarde du code XML de l'annonce dans la table
            SimpleAnnonceSedexCO annonceSedexCO = persistAnnonce(message);

            fileDecompte = generateListFinal(annonceSedexCO);
        } catch (JadePersistenceException jpe) {
            logErrors("AnnoncesCOReceptionMessage5234_000402_1.importMessagesSingle()",
                    "Erreur pendant le traitement de la liste : " + jpe.getMessage(), jpe);
        } catch (Exception ex) {
            logErrors("AnnoncesCOReceptionMessage5234_000402_1.importMessagesSingle()", "Erreur unmarshall message : "
                    + ex.getMessage(), ex);
        }
    }

    @Override
    protected String getSubjectMail() {
        return "Contentieux Amal : réception des annonces 'Décompte annuel' effectuée avec succès !";
    }

    public File generateListFinal(SimpleAnnonceSedexCO annonceSedexCO) throws JadePersistenceException {

        ComplexAnnonceSedexCODebiteursAssuresSearch annonceSedexCODebiteursAssuresSearch = super
                .searchDataReport(annonceSedexCO);

        List<SimpleOutputList_Decompte_5234_401_1> sheetDecomptes = super
                .generateList(annonceSedexCODebiteursAssuresSearch);
        List<SimpleOutputList_DecomptePaiement_5234_402_1> sheetPaiements = generateListPaiement(annonceSedexCODebiteursAssuresSearch);

        File fileDecompte = printList(sheetDecomptes, sheetPaiements);
        sendMail(fileDecompte);
        return fileDecompte;

    }

    public File generateListFinalFromComplexModel(
            ComplexAnnonceSedexCODebiteursAssuresSearch annonceSedexCODebiteursAssuresSearch)
            throws JadePersistenceException {

        List<SimpleOutputList_Decompte_5234_401_1> sheetDecomptes = super
                .generateList(annonceSedexCODebiteursAssuresSearch);
        List<SimpleOutputList_DecomptePaiement_5234_402_1> sheetPaiements = generateListPaiement(annonceSedexCODebiteursAssuresSearch);

        File fileDecompte = printList(sheetDecomptes, sheetPaiements);
        sendMail(fileDecompte);
        return fileDecompte;

    }

    private File printList(List<SimpleOutputList_Decompte_5234_401_1> listLigneDecompteFinaux,
            List<SimpleOutputList_DecomptePaiement_5234_402_1> listLigneDecompteFinalPaiement) {

        if (listLigneDecompteFinaux.isEmpty()) {
            return null;
        }

        if (listLigneDecompteFinalPaiement.isEmpty()) {
            return null;
        }

        Details details = new Details();
        details.add("Reçu le", Date.now().getSwissValue());
        details.newLigne();
        Configuration config = Configurations.buildeDefault();
        config.setRowDecorator(new RowDecorator<Object>() {
            @Override
            public RowStyle decorat(Object outputList, int rowNum, int size) {
                final RowStyle rowStyle = new RowStyle();
                if (rowNum % 2 == 0) {
                    rowStyle.setBackGroundColor(247, 252, 255);
                }

                String nss = "";
                if (outputList instanceof SimpleOutputList_Decompte_5234_401_1) {
                    nss = ((SimpleOutputList_Decompte_5234_401_1) outputList).getNssDebiteur();
                } else if (outputList instanceof SimpleOutputList_DecomptePaiement_5234_402_1) {
                    nss = ((SimpleOutputList_DecomptePaiement_5234_402_1) outputList).getNssDebiteur();
                }

                if ("Total".equals(nss) || "85% du total".equals(nss)) {
                    rowStyle.setBackGroundColor(203, 210, 212);
                }
                return rowStyle;
            }
        });

        String nomCaisseMaladieEmetteur = "";
        if (senderId != null && !senderId.isEmpty()) {
            nomCaisseMaladieEmetteur = getNomCaisseMaladie(getIdTiersCaisseMaladie());
        } else {
            nomCaisseMaladieEmetteur = "Caisse maladie non trouvée ou non spécifiée";
        }

        @SuppressWarnings("squid:S2095" /*Dans ce cas le close est fait par la fonction build, il n'y a pas besoin de faire le close*/)
        SimpleOutputListBuilder builderOnglet1 = SimpleOutputListBuilderJade.newInstance()
                .outputNameAndAddPath("decompteAnnuel").addList(listLigneDecompteFinaux)
                .addTitle(nomCaisseMaladieEmetteur, Align.LEFT).addSubTitle("Décompte final").configure(config)
                .addHeaderDetails(details);
        SimpleOutputListBuilder builderOnglet2 = builderOnglet1.jump().addList(listLigneDecompteFinalPaiement)
                .addTitle(nomCaisseMaladieEmetteur, Align.LEFT).addSubTitle("Paiement").addHeaderDetails(details);

        // Onglet2 contient onglet 1
        File file = builderOnglet2.asXls().build();

        return file;
    }

    private List<SimpleOutputList_DecomptePaiement_5234_402_1> generateListPaiement(
            ComplexAnnonceSedexCODebiteursAssuresSearch annonceSedexCODebiteursAssuresSearch) {
        List<SimpleOutputList_DecomptePaiement_5234_402_1> decomptePaiements = new ArrayList<SimpleOutputList_DecomptePaiement_5234_402_1>();
        // try {
        String lastIdDebiteur = "";

        Montant totalPaiementDebiteurs = new Montant(0);
        Montant totalRPRetro = new Montant(0);
        Montant totalAnnulation = new Montant(0);
        Montant totalTotal = new Montant(0);
        for (JadeAbstractModel abstractComplex : annonceSedexCODebiteursAssuresSearch.getSearchResults()) {
            SimpleOutputList_DecomptePaiement_5234_402_1 ligneDecompte = new SimpleOutputList_DecomptePaiement_5234_402_1();
            ComplexAnnonceSedexCODebiteursAssures debiteursPaiements = (ComplexAnnonceSedexCODebiteursAssures) abstractComplex;
            String idDebiteur = debiteursPaiements.getSimpleAnnonceSedexCODebiteur().getIdAnnonceSedexCODebiteur();
            SimpleAnnonceSedexCODebiteur debiteur = debiteursPaiements.getSimpleAnnonceSedexCODebiteur();
            SimpleAnnonceSedexCOPaiements paiement = debiteursPaiements.getSimpleAnnonceSedexCOPaiements();

            if (paiement.isNew()) {
                continue;
            }

            if (lastIdDebiteur.isEmpty()
                    || !lastIdDebiteur.equals(debiteursPaiements.getSimpleAnnonceSedexCODebiteur()
                            .getIdAnnonceSedexCODebiteur())) {

                try {
                    ligneDecompte.setNssDebiteur(formatNSS(debiteur.getNssDebiteur()));
                } catch (Exception ex) {
                    ligneDecompte.setNssDebiteur(debiteur.getNssDebiteur());
                }
                ligneDecompte.setNomPrenomDebiteur(debiteur.getNomPrenomDebiteur());

                ligneDecompte.setTypeActe(debiteur.getActe());

                String paiementCategory = paiement.getPaiementCategory();

                if (PaymentCategoryEnum.isPaiementDebiteur(paiementCategory)) {
                    Montant montantPmtDebiteur = new Montant(paiement.getPaiementTotalAmout());
                    ligneDecompte.setPaiementDebiteur(montantPmtDebiteur);
                    totalPaiementDebiteurs = totalPaiementDebiteurs.add(montantPmtDebiteur);
                    totalTotal = totalTotal.add(montantPmtDebiteur);
                } else if (PaymentCategoryEnum.isRPRetro(paiementCategory)) {
                    Montant montantRPRetro = new Montant(paiement.getPaiementTotalAmout());
                    ligneDecompte.setRpRetro(montantRPRetro);
                    totalRPRetro = totalRPRetro.add(montantRPRetro);
                    totalTotal = totalTotal.add(montantRPRetro);
                } else if (PaymentCategoryEnum.isAnnulation(paiementCategory)) {
                    Montant montantAnnulation = new Montant(paiement.getPaiementTotalAmout());
                    ligneDecompte.setAnnulation(montantAnnulation);
                    totalAnnulation = totalAnnulation.add(montantAnnulation);
                    totalTotal = totalTotal.add(montantAnnulation);
                }
            }

            lastIdDebiteur = idDebiteur;
            decomptePaiements.add(ligneDecompte);
        }

        SimpleOutputList_DecomptePaiement_5234_402_1 ligneTotal = new SimpleOutputList_DecomptePaiement_5234_402_1();
        ligneTotal.setNssDebiteur("Total");
        ligneTotal.setNomPrenomDebiteur("");
        ligneTotal.setPaiementDebiteur(totalPaiementDebiteurs);
        ligneTotal.setRpRetro(totalRPRetro);
        ligneTotal.setAnnulation(totalAnnulation);
        ligneTotal.setTypeActe(totalTotal.getValue());
        ligneTotal.setMessage("");
        decomptePaiements.add(ligneTotal);

        return decomptePaiements;
    }

    private SimpleAnnonceSedexCO persistAnnonce(Message message) {

        // Sauvegarde de l'annonce dans la table
        HeaderType header = message.getHeader();
        SimpleAnnonceSedexCO annonceSedexCO = new SimpleAnnonceSedexCO();
        try {
            String idTiersSender = AMSedexRPUtil.getIdTiersFromSedexId(message.getHeader().getSenderId());

            annonceSedexCO.setMessageId(header.getMessageId());
            annonceSedexCO.setBusinessProcessId(header.getBusinessProcessId());
            annonceSedexCO.setMessageType(header.getMessageType());
            annonceSedexCO.setMessageSubType(header.getSubMessageType());
            annonceSedexCO.setMessageEmetteur(header.getSenderId());
            annonceSedexCO.setMessageRecepteur(header.getRecipientId());
            annonceSedexCO.setIdTiersCM(idTiersSender);
            annonceSedexCO.setStatus(IAMCodeSysteme.AMStatutAnnonceSedex.RECU.getValue());
            annonceSedexCO.setDateAnnonce(Date.now().getSwissValue());

            CertificateOfLossFinalStatementType certificateOfLossArrivalType = message.getContent()
                    .getCertificateOfLossFinalStatement();

            Date statementDate = new Date(AMSedexRPUtil.getDateXMLToString(certificateOfLossArrivalType
                    .getStatementDate()));

            annonceSedexCO.setStatementDate(statementDate.getSwissValue());
            annonceSedexCO.setStatementYear(statementDate.getAnnee());

            annonceSedexCO.setStatementStartDate(AMSedexRPUtil.getDateXMLToString(certificateOfLossArrivalType
                    .getStatementStartDate()));
            annonceSedexCO.setStatementEndDate(AMSedexRPUtil.getDateXMLToString(certificateOfLossArrivalType
                    .getStatementEndDate()));
            JadePersistenceManager.add(annonceSedexCO);
            checkJadeThreadErrors();

            // Sauvegarde du XML dans la base
            saveXml(message, annonceSedexCO);

            List<CertificateOfLossArrivalType> decomptesFinauxsActes = message.getContent()
                    .getCertificateOfLossFinalStatement().getCertificateOfLossArrival();
            statementStartDate = new Date(AMSedexRPUtil.getDateXMLToString(message.getContent()
                    .getCertificateOfLossFinalStatement().getStatementStartDate()));
            statementDate = new Date(AMSedexRPUtil.getDateXMLToString(message.getContent()
                    .getCertificateOfLossFinalStatement().getStatementDate()));
            // Sauvegarde des débiteurs et personnes assurées
            saveActesDefautBien(decomptesFinauxsActes, annonceSedexCO);

            List<CertificateOfLossPaymentType> decomptesFinauxPaiements = message.getContent()
                    .getCertificateOfLossFinalStatement().getCertificateOfLossPayment();

            savePaiements(decomptesFinauxPaiements, annonceSedexCO);
        } catch (Exception ex) {
            logErrors(
                    "AnnoncesCOReceptionMessage5234_000402_1.saveAnnonce()",
                    "Erreur pendant la sauvegarde de l'annonce du décompte trimestriel ! (Msg id : "
                            + header.getMessageId() + ") => " + ex.getMessage(), ex);
        }

        return annonceSedexCO;
    }

    private void saveXml(Message message, SimpleAnnonceSedexCO simpleAnnonceCO) throws JAXBException, SAXException,
            IOException, JAXBValidationError, JAXBValidationWarning, JadePersistenceException {
        StringWriter swFull = new StringWriter();
        List<String> splitXml = new ArrayList<String>();
        JAXBServices.getInstance().marshal(message, swFull, false, true, addClasses);
        String fullXml = swFull.toString();
        int iCounter = 0;
        StringWriter swSplitted = new StringWriter();
        for (int idx = 0; idx < fullXml.length(); idx++) {
            String currentChar = String.valueOf(fullXml.charAt(idx));
            if (currentChar.equals("'")) {
                swSplitted.append("''");
            } else {
                swSplitted.append(fullXml.charAt(idx));
            }
            if (iCounter == 20000) {
                iCounter = 0;
                splitXml.add(swSplitted.toString());
                swSplitted = new StringWriter();
            }
            iCounter++;
        }
        if (swSplitted.toString().length() > 0) {
            splitXml.add(swSplitted.toString());
        }

        SimpleAnnonceSedexCOXML annonceSedexCOXML = new SimpleAnnonceSedexCOXML();
        annonceSedexCOXML.setIdAnnonceSedex(simpleAnnonceCO.getIdAnnonceSedexCO());
        annonceSedexCOXML.setMessageId(message.getHeader().getMessageId());
        annonceSedexCOXML.setXml(splitXml.get(0));
        JadePersistenceManager.add(annonceSedexCOXML);
        for (int idx = 1; idx < splitXml.size(); idx++) {
            String query = "UPDATE SCHEMA.MASDXCO_XML SET XML=CONCAT(XML, '" + splitXml.get(idx)
                    + "') WHERE IDSEDEX = " + simpleAnnonceCO.getIdAnnonceSedexCO() + " AND MSGID = '"
                    + message.getHeader().getMessageId() + "'";
            try {
                QueryExecutor.executeUpdate(query, getSession());
            } catch (Exception e) {
                logErrors("AnnoncesCOReceptionMessage5234_000402_1.saveXml()", "Not possible to call QueryExecutor : "
                        + e.getMessage(), e);
            }
        }

        checkJadeThreadErrors();
    }

    @Override
    protected String getIdTiersCaisseMaladie() {
        if (senderId == null || senderId.isEmpty()) {
            throw new IllegalStateException("Aucun sedex id n'a été défini !");
        }
        try {
            if (idTiersCaisseMaladie == null) {
                idTiersCaisseMaladie = AMSedexRPUtil.getIdTiersFromSedexId(senderId);
            }
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage());
        }

        return idTiersCaisseMaladie;
    }

    private void savePaiements(List<CertificateOfLossPaymentType> decomptesFinauxPaiements,
            SimpleAnnonceSedexCO simpleAnnonceSedexCO) {
        for (CertificateOfLossPaymentType certificateOfLossPayment : decomptesFinauxPaiements) {
            try {
                if (certificateOfLossPayment.getDebtor().getDebtorNP() == null) {
                    JadeThread.logInfo(this.getClass().getName(), "Débiteur non personne physique");
                    continue;
                }

                SimpleAnnonceSedexCODebiteur simpleAnnonceSedexCODebiteur = saveDebiteur(simpleAnnonceSedexCO,
                        certificateOfLossPayment.getCertificateOfLoss().getTypeOfLoss(),
                        certificateOfLossPayment.getDebtor());

                for (PaymentType payment : certificateOfLossPayment.getPayment()) {
                    SimpleAnnonceSedexCOPaiements simpleAnnonceSedexCOPaiements = new SimpleAnnonceSedexCOPaiements();
                    simpleAnnonceSedexCOPaiements.setIdAnnonceSedexCODebiteur(simpleAnnonceSedexCODebiteur
                            .getIdAnnonceSedexCODebiteur());
                    simpleAnnonceSedexCOPaiements.setPaiementTotalAmout(payment.getTotalAmount().toString());
                    simpleAnnonceSedexCOPaiements.setPaiementCategory(payment.getPaymentCategory());
                    JadePersistenceManager.add(simpleAnnonceSedexCOPaiements);
                }
            } catch (JadePersistenceException jpe) {
                logErrors("AnnoncesCOReceptionMessage5234_000402_1.savePaiements()",
                        "Erreur pendant la création du débiteur en DB : " + jpe.getMessage(), jpe);
            }
        }
    }

    protected SimpleAnnonceSedexCODebiteur saveDebiteur(SimpleAnnonceSedexCO simpleAnnonceSedexCO, String typeOfLoss,
            DebtorType debtorType) throws JadeNoBusinessLogSessionError, JadePersistenceException {

        DebtorNPType debtorNP = debtorType.getDebtorNP();

        SimpleAnnonceSedexCODebiteur annonceSedexCODebiteur = new SimpleAnnonceSedexCODebiteur();
        annonceSedexCODebiteur.setIdAnnonceSedexCO(simpleAnnonceSedexCO.getIdAnnonceSedexCO());
        annonceSedexCODebiteur.setNssDebiteur(String.valueOf(debtorNP.getVn()));
        annonceSedexCODebiteur.setNomPrenomDebiteur(debtorNP.getOfficialName() + " " + debtorNP.getFirstName());
        annonceSedexCODebiteur.setNpaLocaliteDebiteur(getNPALocalite(debtorNP.getAddress()));
        annonceSedexCODebiteur.setRueNumeroDebiteur(getRueNumero(debtorNP.getAddress()));
        TypesOfLossEnum typeActe = getTypeActe(typeOfLoss);
        annonceSedexCODebiteur.setActe(typeActe.getCs());

        if (debtorNP.getVn() != null && debtorNP.getVn() > 0) {
            annonceSedexCODebiteur = findCorrespondanceDebiteur(debtorNP, annonceSedexCODebiteur, null);

            if (annonceSedexCODebiteur != null && annonceSedexCODebiteur.getIdFamille() != null) {
                SimpleAnnonceSedexCOPersonne simpleAnnonceSedexCOPersonne = new SimpleAnnonceSedexCOPersonne();
                simpleAnnonceSedexCOPersonne.setIdAnnonceSedexCO(simpleAnnonceSedexCO.getIdAnnonceSedexCO());
                simpleAnnonceSedexCOPersonne.setIdFamille(annonceSedexCODebiteur.getIdFamille());
                simpleAnnonceSedexCOPersonne.setIdContribuable(annonceSedexCODebiteur.getIdContribuable());
                try {
                    AmalServiceLocator.getSimpleAnnonceSedexCOPersonneService().create(simpleAnnonceSedexCOPersonne);
                } catch (Exception ex) {
                    throw new JadePersistenceException(ex.getMessage());
                }
            }
        }
        annonceSedexCODebiteur = (SimpleAnnonceSedexCODebiteur) JadePersistenceManager.add(annonceSedexCODebiteur);
        return annonceSedexCODebiteur;
    }

}
