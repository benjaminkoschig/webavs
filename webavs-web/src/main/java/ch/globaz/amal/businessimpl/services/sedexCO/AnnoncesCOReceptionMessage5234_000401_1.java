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
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.sedex.JadeSedexMessageNotHandledException;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.GroupedSedexMessage;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
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
import ch.gdk_cds.xmlns.da_64a_5234_000401._1.HeaderType;
import ch.gdk_cds.xmlns.da_64a_5234_000401._1.Message;
import ch.gdk_cds.xmlns.da_64a_common._1.CertificateOfLossArrivalType;
import ch.gdk_cds.xmlns.da_64a_common._1.CertificateOfLossQuarterlyStatementType;
import ch.gdk_cds.xmlns.da_64a_common._1.DebtorNPType;
import ch.gdk_cds.xmlns.da_64a_common._1.DebtorWithClaimType;
import ch.gdk_cds.xmlns.da_64a_common._1.InsuredPersonType;
import ch.gdk_cds.xmlns.da_64a_common._1.InsuredPersonWithClaimType;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.constantes.TypesOfLossEnum;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCODebiteursAssures;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCODebiteursAssuresSearch;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOAssure;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCODebiteur;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOPersonne;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOXML;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.famille.FamillePersonneEtendue;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexCO.listes.SimpleOutputList_Decompte_5234_401_1;
import ch.globaz.amal.businessimpl.services.sedexCO.listes.SimpleOutputList_Decompte_5234_402_1;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.domaine.Taux;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.configuration.Configuration;
import ch.globaz.simpleoutputlist.configuration.RowDecorator;
import ch.globaz.simpleoutputlist.configuration.RowStyle;
import ch.globaz.simpleoutputlist.core.Details;
import ch.globaz.simpleoutputlist.outimpl.Configurations;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

public class AnnoncesCOReceptionMessage5234_000401_1 extends AnnoncesCODefault {
    private static final String PACKAGE_CLASS_FOR_READ_SEDEX_DECOMPTE_TRIMESTRIEL = "ch.gdk_cds.xmlns.da_64a_5234_000401._1";
    private String idTiersCaisseMaladie = null;
    protected String senderId = null;
    protected Date statementStartDate = null;
    protected Date statementDate = null;

    /**
     * Préparation des users et mots de passe pour le gestion SEDEX (JadeSedexService.xml)
     * 
     * @param properties
     * @throws JadeDecryptionNotSupportedException
     * @throws JadeEncrypterNotFoundException
     * @throws Exception
     */
    @Setup
    public void setUp(Properties properties) throws Exception {

        getUserAndPassSedex(properties);

        JAXBContext jaxbContext = JAXBContext.newInstance(PACKAGE_CLASS_FOR_READ_SEDEX_DECOMPTE_TRIMESTRIEL);
        unmarshaller = jaxbContext.createUnmarshaller();
        marshaller = jaxbContext.createMarshaller();
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

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
            e.printStackTrace();
            JadeLogger.error(this, e);
            throw new JadeSedexMessageNotHandledException("Erreur dans la réception d'une annonce SEDEX CO: ");
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

    }

    @Override
    protected String getSubjectMail() {
        return "Contentieux Amal : réception des annonces 'Décompte trimestriel' effectuée avec succès !";
    }

    /**
     * Méthode de lecture du message sedex en réception, et traitement
     * 
     * @param currentSimpleMessage
     * @return
     * @throws JadeSedexMessageNotHandledException
     */
    protected void importMessagesSingle(SimpleSedexMessage currentSimpleMessage)
            throws JadeSedexMessageNotHandledException {
        File fileDecompte = null;
        try {
            Class<?>[] addClasses = new Class[] { ch.gdk_cds.xmlns.da_64a_5234_000401._1.Message.class };
            jaxbs = JAXBServices.getInstance();
            Message message = (Message) jaxbs.unmarshal(currentSimpleMessage.fileLocation, false, true, addClasses);
            senderId = message.getHeader().getSenderId();
            // Sauvegarde du code XML de l'annonce dans la table
            SimpleAnnonceSedexCO annonceSedexCO = persistAnnonce(message, addClasses);

            fileDecompte = generationList(annonceSedexCO);
        } catch (JadePersistenceException jpe) {
            logErrors("AnnoncesCOReceptionMessage5234_000401_1.importMessagesSingle()",
                    "Erreur pendant le traitement de la liste : " + jpe.getMessage(), jpe);
        } catch (Exception ex) {
            logErrors("AnnoncesCOReceptionMessage5234_000401_1.importMessagesSingle()", "Erreur unmarshall message : "
                    + ex.getMessage(), ex);
        }
    }

    public File generationList(SimpleAnnonceSedexCO annonceSedexCO) throws JadePersistenceException {
        File fileDecompte;
        ComplexAnnonceSedexCODebiteursAssuresSearch annonceSedexCODebiteursAssuresSearch = searchDataReport(annonceSedexCO);
        List<SimpleOutputList_Decompte_5234_401_1> sheetDecomptes = generateList(annonceSedexCODebiteursAssuresSearch);
        fileDecompte = printList(sheetDecomptes);
        sendMail(fileDecompte);
        return fileDecompte;
    }

    protected ComplexAnnonceSedexCODebiteursAssuresSearch searchDataReport(SimpleAnnonceSedexCO annonceSedexCO)
            throws JadePersistenceException {
        ComplexAnnonceSedexCODebiteursAssuresSearch annonceSedexCODebiteursAssuresSearch = new ComplexAnnonceSedexCODebiteursAssuresSearch();
        annonceSedexCODebiteursAssuresSearch.setForIdSedexCO(annonceSedexCO.getIdAnnonceSedexCO());
        annonceSedexCODebiteursAssuresSearch = (ComplexAnnonceSedexCODebiteursAssuresSearch) JadePersistenceManager
                .search(annonceSedexCODebiteursAssuresSearch);
        return annonceSedexCODebiteursAssuresSearch;
    }

    protected List<SimpleOutputList_Decompte_5234_401_1> generateList(
            ComplexAnnonceSedexCODebiteursAssuresSearch annonceSedexCODebiteursAssuresSearch) {
        List<SimpleOutputList_Decompte_5234_401_1> decompteSheet = new ArrayList<SimpleOutputList_Decompte_5234_401_1>();
        String lastIdDebiteur = "";

        Montant totalMontantPrime = new Montant(0);
        Montant totalMontantParticipation = new Montant(0);
        Montant totalInterets = new Montant(0);
        Montant totalFrais = new Montant(0);
        Montant totalTotal = new Montant(0);
        for (JadeAbstractModel abstractComplex : annonceSedexCODebiteursAssuresSearch.getSearchResults()) {
            SimpleOutputList_Decompte_5234_401_1 ligneDecompte = new SimpleOutputList_Decompte_5234_401_1();
            ComplexAnnonceSedexCODebiteursAssures debiteursAssures = (ComplexAnnonceSedexCODebiteursAssures) abstractComplex;
            String idDebiteur = debiteursAssures.getSimpleAnnonceSedexCODebiteur().getIdAnnonceSedexCODebiteur();
            SimpleAnnonceSedexCODebiteur debiteur = debiteursAssures.getSimpleAnnonceSedexCODebiteur();
            SimpleAnnonceSedexCOAssure assure = debiteursAssures.getSimpleAnnonceSedexCOAssure();

            // On vérifie qu'on ai bien un assuré (pour ne pas prendre en compte les paiements qui eux n'ont pas
            // d'assurés)
            if (assure.isNew()) {
                continue;
            }

            if (lastIdDebiteur.isEmpty()
                    || !lastIdDebiteur.equals(debiteursAssures.getSimpleAnnonceSedexCODebiteur()
                            .getIdAnnonceSedexCODebiteur())) {

                String nss = debiteur.getNssDebiteur();
                try {
                    nss = formatNSS(debiteur.getNssDebiteur());
                } catch (Exception e) {
                    // En cas d'erreur, on garde le nss non formaté tel que récupéré plus haut
                }
                ligneDecompte.setNssDebiteur(nss);
                ligneDecompte.setNomPrenomDebiteur(debiteur.getNomPrenomDebiteur());
                ligneDecompte.setTypeActe(debiteur.getActe());
                if (!debiteur.getInterets().isEmpty()) {
                    ligneDecompte.setInterets(new Montant(debiteur.getInterets()));
                } else {
                    ligneDecompte.setInterets(new Montant("0"));
                }

                if (!debiteur.getFrais().isEmpty()) {
                    ligneDecompte.setFrais(new Montant(debiteur.getFrais()));
                } else {
                    ligneDecompte.setFrais(new Montant("0"));
                }

                if (!debiteur.getTotal().isEmpty()) {
                    ligneDecompte.setTotal(new Montant(debiteur.getTotal()));
                } else {
                    ligneDecompte.setTotal(new Montant("0"));
                }
                ligneDecompte.addMessageDebiteur(debiteur.getMessage());

                totalInterets = totalInterets.add(debiteur.getInterets());
                totalFrais = totalFrais.add(debiteur.getFrais());
                totalTotal = totalTotal.add(debiteur.getTotal());
            }

            ligneDecompte.setNssAssure(assure.getNssAssure());
            ligneDecompte.setNomPrenomAssure(assure.getNomPrenomAssure());
            ligneDecompte.setTypeSubside(assure.getTypeSubside());

            if (assure.getPrimePeriodeDebut() != null) {
                Periode primePeriode = new Periode(assure.getPrimePeriodeDebut(), assure.getPrimePeriodeFin());
                ligneDecompte.setPrimePeriode(primePeriode);
                ligneDecompte.setPrimeMontant(new Montant(assure.getPrimeMontant()));
                totalMontantPrime = totalMontantPrime.add(assure.getPrimeMontant());
            }

            if (assure.getCostSharingPeriodeDebut() != null) {
                Periode sharingPeriode = new Periode(assure.getCostSharingPeriodeDebut(),
                        assure.getCostSharingPeriodeFin());
                ligneDecompte.setSharingPeriode(sharingPeriode);
                ligneDecompte.setSharingMontant(new Montant(assure.getCostSharingMontant()));
                totalMontantParticipation = totalMontantParticipation.add(assure.getCostSharingMontant());
            }

            ligneDecompte.addMessageAssure(assure.getMessage());
            lastIdDebiteur = idDebiteur;
            decompteSheet.add(ligneDecompte);
        }

        SimpleOutputList_Decompte_5234_402_1 ligneTotal = new SimpleOutputList_Decompte_5234_402_1();
        ligneTotal.setNssDebiteur("Total");
        ligneTotal.setNomPrenomDebiteur("");
        ligneTotal.setTypeActe("");
        ligneTotal.setNssAssure("");
        ligneTotal.setNomPrenomAssure("");
        ligneTotal.setPrimePeriode(null);
        ligneTotal.setPrimeMontant(totalMontantPrime);
        ligneTotal.setSharingPeriode(null);
        ligneTotal.setSharingMontant(totalMontantParticipation);
        ligneTotal.setInterets(totalInterets);
        ligneTotal.setFrais(totalFrais);
        ligneTotal.setTotal(totalTotal);
        decompteSheet.add(ligneTotal);

        SimpleOutputList_Decompte_5234_402_1 ligneTotal85 = new SimpleOutputList_Decompte_5234_402_1();
        ligneTotal85.setNssDebiteur("85% du total");
        ligneTotal85.setNomPrenomDebiteur("");
        ligneTotal85.setTypeActe("");
        ligneTotal85.setNssAssure("");
        ligneTotal85.setNomPrenomAssure("");
        ligneTotal85.setPrimePeriode(null);
        Taux taux85 = new Taux(85);
        Montant totalMontantPrime85 = totalMontantPrime.multiply(taux85).normalize();
        ligneTotal85.setPrimeMontant(totalMontantPrime85);
        ligneTotal85.setSharingPeriode(null);
        Montant totalMontantParticipation85 = totalMontantParticipation.multiply(taux85).normalize();
        ligneTotal85.setSharingMontant(totalMontantParticipation85);
        Montant totalInterets85 = totalInterets.multiply(taux85).normalize();
        ligneTotal85.setInterets(totalInterets85);
        Montant totalFrais85 = totalFrais.multiply(taux85).normalize();
        ligneTotal85.setFrais(totalFrais85);
        Montant totalTotal85 = totalTotal.multiply(taux85).normalize();
        ligneTotal85.setTotal(totalTotal85);
        decompteSheet.add(ligneTotal85);

        return decompteSheet;
    }

    protected File printList(List<SimpleOutputList_Decompte_5234_401_1> listLigneDecompteTrimestriel) {

        if (listLigneDecompteTrimestriel.isEmpty()) {
            return null;
        }

        Details details = new Details();
        details.add("Reçu le", Date.now().getSwissValue());
        details.newLigne();
        Configuration config = Configurations.buildeDefault();
        config.setRowDecorator(new RowDecorator<SimpleOutputList_Decompte_5234_401_1>() {
            @Override
            public RowStyle decorat(SimpleOutputList_Decompte_5234_401_1 outputList_Decompte_5234_401_1, int rowNum,
                    int size) {
                final RowStyle rowStyle = new RowStyle();
                if (rowNum % 2 == 0) {
                    rowStyle.setBackGroundColor(247, 252, 255);
                }

                if ("Total".equals(outputList_Decompte_5234_401_1.getNssDebiteur())
                        || "85% du total".equals(outputList_Decompte_5234_401_1.getNssDebiteur())) {
                    rowStyle.setBackGroundColor(203, 210, 212);
                }
                return rowStyle;
            }
        });

        String nomCaisseMaladieEmetteur = getNomCaisseMaladie(getIdTiersCaisseMaladie());

        SimpleOutputListBuilder builder = SimpleOutputListBuilderJade.newInstance()
                .outputNameAndAddPath("décompteTrimestriel").addList(listLigneDecompteTrimestriel)
                .classElementList(SimpleOutputList_Decompte_5234_401_1.class);
        File file = builder.addTitle(nomCaisseMaladieEmetteur, Align.LEFT).addSubTitle("Décompte trimestriel")
                .configure(config).addHeaderDetails(details).asXls().build();

        return file;
    }

    // @Override
    // protected void sendMail(File file, String typeDecompte) throws Exception {
    // String subject = "Contentieux Amal : réception des annonces '" + typeDecompte + "' effectuée avec succès !";
    // StringBuilder body = new StringBuilder();
    // if (!personnesNotFound.isEmpty()) {
    // if (personnesNotFound.size() > 1) {
    // subject = personnesNotFound.size()
    // + " personnes non connues détectées lors de la réception des annonces de type '" + typeDecompte
    // + "' !";
    // } else {
    // subject = "1 personne non connue détectée lors de la réception des annonces de type '" + typeDecompte
    // + "' !";
    // }
    // body.append("Liste des personnes non trouvées :\n");
    //
    // for (String personneInexistante : personnesNotFound) {
    // body.append("   -" + personneInexistante + "\n");
    // }
    //
    // }
    //
    // if (errors.size() > 0) {
    // body.append("Autres erreurs détectée(s)");
    // for (String error : errors) {
    // body.append(error);
    // }
    // }
    //
    // String[] files = new String[1];
    // if (file != null) {
    // files[0] = file.getPath();
    // }
    //
    // JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
    // body.toString(), files);
    // }

    private SimpleAnnonceSedexCO persistAnnonce(Message message, Class<?>[] addClasses) {

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

            CertificateOfLossQuarterlyStatementType certificateOfLossArrivalType = message.getContent()
                    .getCertificateOfLossQuarterlyStatement();

            statementDate = new Date(AMSedexRPUtil.getDateXMLToString(certificateOfLossArrivalType.getStatementDate()));
            statementStartDate = new Date(AMSedexRPUtil.getDateXMLToString(message.getContent()
                    .getCertificateOfLossQuarterlyStatement().getStatementStartDate()));

            annonceSedexCO.setStatementDate(statementDate.getSwissValue());
            annonceSedexCO.setStatementYear(statementDate.getAnnee());
            annonceSedexCO.setStatementStartDate(AMSedexRPUtil.getDateXMLToString(certificateOfLossArrivalType
                    .getStatementStartDate()));
            annonceSedexCO.setStatementEndDate(AMSedexRPUtil.getDateXMLToString(certificateOfLossArrivalType
                    .getStatementEndDate()));
            JadePersistenceManager.add(annonceSedexCO);
            checkJadeThreadErrors();

            // Sauvegarde du XML dans la base
            saveXml(message, addClasses, annonceSedexCO);

            List<CertificateOfLossArrivalType> decomptesTrimestriels = message.getContent()
                    .getCertificateOfLossQuarterlyStatement().getCertificateOfLossArrival();

            // Sauvegarde des débiteurs et personnes assurées
            saveActesDefautBien(decomptesTrimestriels, annonceSedexCO);
        } catch (Exception ex) {
            logErrors(
                    "AnnoncesCOReceptionMessage5234_000401_1.saveAnnonce()",
                    "Erreur pendant la sauvegarde de l'annonce du décompte trimestriel ! (Msg id : "
                            + header.getMessageId() + ") => " + ex.getMessage(), ex);
        }

        return annonceSedexCO;
    }

    private void saveXml(Message message, Class<?>[] addClasses, SimpleAnnonceSedexCO simpleAnnonceCO)
            throws JAXBException, SAXException, IOException, JAXBValidationError, JAXBValidationWarning,
            JadePersistenceException {
        StringWriter sw = new StringWriter();
        jaxbs.marshal(message, sw, false, true, addClasses);
        SimpleAnnonceSedexCOXML annonceSedexCOXML = new SimpleAnnonceSedexCOXML();
        annonceSedexCOXML.setIdAnnonceSedex(simpleAnnonceCO.getIdAnnonceSedexCO());
        annonceSedexCOXML.setMessageId(message.getHeader().getMessageId());
        annonceSedexCOXML.setXml(sw.toString());
        JadePersistenceManager.add(annonceSedexCOXML);
        checkJadeThreadErrors();
    }

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

    protected SimpleAnnonceSedexCODebiteur findCorrespondanceDebiteur(DebtorNPType debtorNP,
            SimpleAnnonceSedexCODebiteur annonceSedexCODebiteur, List<InsuredPersonWithClaimType> insuredPersonTypes) {

        if (debtorNP.getVn() == null) {
            throw new IllegalArgumentException("NSS débiteur is null or incorrect ! => " + debtorNP.getVn());
        }

        String strNss = String.valueOf(debtorNP.getVn());
        try {
            FamillePersonneEtendue famillePersonneEtendue = searchPersonne(strNss, insuredPersonTypes,
                    annonceSedexCODebiteur);

            if (famillePersonneEtendue != null) {
                annonceSedexCODebiteur.setIdFamille(famillePersonneEtendue.getSimpleFamille().getIdFamille());
                annonceSedexCODebiteur.setIdContribuable(famillePersonneEtendue.getSimpleContribuable()
                        .getIdContribuable());
            } else {
                personnesNotFound.add("Débiteur " + debtorNP.getVn() + " - " + debtorNP.getOfficialName() + " "
                        + debtorNP.getFirstName());

                annonceSedexCODebiteur.addMessage("Debiteur non retrouvé");
            }
        } catch (Exception ex) {
            logErrors("AnnoncesCOReceptionMessage5234_000401_1.findCorrespondanceDebiteur()",
                    "Erreur lors de la recherche de correspondance du débiteur " + ex.getMessage(), ex);
        }
        return annonceSedexCODebiteur;
    }

    protected SimpleAnnonceSedexCOAssure findCorrespondancePersonneAssuree(InsuredPersonType insuredPerson,
            SimpleAnnonceSedexCOAssure annonceSedexCOAssure) {
        try {
            String strNss = String.valueOf(insuredPerson.getVn());
            FamillePersonneEtendue famillePersonneEtendue = getPersonneEtendue(strNss, false);

            if (famillePersonneEtendue != null) {
                annonceSedexCOAssure.setIdFamille(famillePersonneEtendue.getSimpleFamille().getIdFamille());
                annonceSedexCOAssure.setIdContribuable(famillePersonneEtendue.getSimpleContribuable()
                        .getIdContribuable());

                getInfosSubside(annonceSedexCOAssure, famillePersonneEtendue);
            } else {
                personnesNotFound.add("Personne " + insuredPerson.getVn() + " - " + insuredPerson.getOfficialName()
                        + " " + insuredPerson.getFirstName());
                annonceSedexCOAssure.addMessage("Personne assurée non retrouvée !");
            }
        } catch (Exception ex) {
            logErrors("AnnoncesCOReceptionMessage5234_000401_1.findCorrespondancePersonneAssuree()",
                    "Erreur lors de la recherche de correspondance de la personne assurée : " + ex.getMessage(), ex);
        }
        return annonceSedexCOAssure;
    }

    private void getInfosSubside(SimpleAnnonceSedexCOAssure annonceSedexCOAssure,
            FamillePersonneEtendue famillePersonneEtendue) throws JadePersistenceException, DetailFamilleException,
            JadeApplicationServiceNotAvailableException {
        SimpleDetailFamilleSearch simpleDetailFamilleSearch = new SimpleDetailFamilleSearch();
        simpleDetailFamilleSearch.setForIdFamille(famillePersonneEtendue.getSimpleFamille().getIdFamille());
        simpleDetailFamilleSearch.setForAnneeHistorique(statementStartDate.getAnnee());
        simpleDetailFamilleSearch.setForCodeActif(Boolean.TRUE);
        simpleDetailFamilleSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(
                simpleDetailFamilleSearch);

        if (simpleDetailFamilleSearch.getNbOfResultMatchingQuery() > 0) {
            SimpleDetailFamille subside = (SimpleDetailFamille) simpleDetailFamilleSearch.getSearchResults()[0];
            annonceSedexCOAssure.setIdDetailFamille(subside.getIdDetailFamille());
            String noCaisseMaladie = subside.getNoCaisseMaladie();
            String idTiersSender = getIdTiersCaisseMaladie();
            if (!noCaisseMaladie.equals(idTiersSender)) {
                annonceSedexCOAssure.addMessage("Caisse maladie différente");
            }

            if (IAMCodeSysteme.AMTypeDemandeSubside.ASSISTE.getValue().equals(subside.getTypeDemande())) {
                annonceSedexCOAssure.setTypeSubside("A");
            } else if (IAMCodeSysteme.AMTypeDemandeSubside.DEMANDE.getValue().equals(subside.getTypeDemande())) {
                annonceSedexCOAssure.setTypeSubside("");
            } else if (IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue().equals(subside.getTypeDemande())) {
                annonceSedexCOAssure.setTypeSubside("P");
            } else if (IAMCodeSysteme.AMTypeDemandeSubside.REPRISE.getValue().equals(subside.getTypeDemande())) {
                annonceSedexCOAssure.setTypeSubside("R");
            } else if (IAMCodeSysteme.AMTypeDemandeSubside.SOURCE.getValue().equals(subside.getTypeDemande())) {
                annonceSedexCOAssure.setTypeSubside("S");
            }

        }
    }

    protected void saveActesDefautBien(List<CertificateOfLossArrivalType> decomptesTrimestriels,
            SimpleAnnonceSedexCO simpleAnnonceSedexCO) {
        for (CertificateOfLossArrivalType certificateOfLossArrivalType : decomptesTrimestriels) {
            try {
                DebtorWithClaimType debtorWithClaim = certificateOfLossArrivalType.getDebtorWithClaim();

                List<InsuredPersonWithClaimType> insuredPersonTypes = certificateOfLossArrivalType
                        .getInsuredPersonWithClaim();
                if (debtorWithClaim.getDebtor().getDebtorNP() == null) {
                    JadeThread.logInfo(this.getClass().getName(), "Débiteur non personne physique");
                    continue;
                }

                SimpleAnnonceSedexCODebiteur simpleAnnonceSedexCODebiteur = saveDebiteurNP(simpleAnnonceSedexCO,
                        certificateOfLossArrivalType.getCertificateOfLoss().getTypeOfLoss(), debtorWithClaim,
                        insuredPersonTypes);

                savePersonneAssuree(insuredPersonTypes, simpleAnnonceSedexCODebiteur);
            } catch (JadePersistenceException jpe) {
                logErrors("AnnoncesCOReceptionMessage5234_000401_1.saveActesDefautBien()",
                        "Erreur pendant la création du débiteur en DB : " + jpe.getMessage(), jpe);
            }
        }
    }

    protected SimpleAnnonceSedexCODebiteur saveDebiteurNP(SimpleAnnonceSedexCO simpleAnnonceSedexCO, String typeOfLoss,
            DebtorWithClaimType debtorWithClaim, List<InsuredPersonWithClaimType> insuredPersonTypes)
            throws JadeNoBusinessLogSessionError, JadePersistenceException {

        if (debtorWithClaim.getDebtor().getDebtorNP() == null) {
            throw new IllegalArgumentException("Le débiteur doit être une personne physique (NPType)");
        }
        DebtorNPType debtorNP = debtorWithClaim.getDebtor().getDebtorNP();

        SimpleAnnonceSedexCODebiteur annonceSedexCODebiteur = new SimpleAnnonceSedexCODebiteur();
        annonceSedexCODebiteur.setIdAnnonceSedexCO(simpleAnnonceSedexCO.getIdAnnonceSedexCO());
        annonceSedexCODebiteur.setNssDebiteur(String.valueOf(debtorNP.getVn()));
        annonceSedexCODebiteur.setNomPrenomDebiteur(debtorNP.getOfficialName() + " " + debtorNP.getFirstName());
        annonceSedexCODebiteur.setNpaLocaliteDebiteur(getNPALocalite(debtorNP.getAddress()));
        annonceSedexCODebiteur.setRueNumeroDebiteur(getRueNumero(debtorNP.getAddress()));
        TypesOfLossEnum typeActe = getTypeActe(typeOfLoss);
        annonceSedexCODebiteur.setActe(typeActe.getCs());
        annonceSedexCODebiteur.setInterets(debtorWithClaim.getClaimDebtor().getInterests().toString());
        annonceSedexCODebiteur.setFrais(debtorWithClaim.getClaimDebtor().getExpenses().toString());
        annonceSedexCODebiteur.setTotal(debtorWithClaim.getClaimDebtor().getTotalClaim().toString());

        if (debtorNP.getVn() != null && debtorNP.getVn() > 0) {
            annonceSedexCODebiteur = findCorrespondanceDebiteur(debtorNP, annonceSedexCODebiteur, insuredPersonTypes);

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

    private boolean hasAutreDecomptes(InsuredPersonType insuredPerson) {
        try {
            ComplexAnnonceSedexCODebiteursAssuresSearch annonceSedexCODebiteursAssuresSearch = new ComplexAnnonceSedexCODebiteursAssuresSearch();
            annonceSedexCODebiteursAssuresSearch.setForMessageSubtype("401");
            annonceSedexCODebiteursAssuresSearch.setForStatementYear(statementStartDate.getAnnee().toString());
            annonceSedexCODebiteursAssuresSearch.setLikeNssAssure(String.valueOf(insuredPerson.getVn()));
            int nb = JadePersistenceManager.count(annonceSedexCODebiteursAssuresSearch);

            return nb > 0;
        } catch (JadePersistenceException jpe) {
            logErrors("AnnoncesCOReceptionMessage5234_000401_1.hasAutreDecomptes()",
                    "Erreur pendant la recherche d'un autre décompte d'une personne assurée : " + jpe.getMessage(), jpe);
        }
        return false;

    }

    private void savePersonneAssuree(List<InsuredPersonWithClaimType> insuredPersonTypes,
            SimpleAnnonceSedexCODebiteur annonceSedexCODebiteur) throws JadePersistenceException {
        for (InsuredPersonWithClaimType insuredPersonWithClaimType : insuredPersonTypes) {
            InsuredPersonType insuredPerson = insuredPersonWithClaimType.getInsuredPerson();

            SimpleAnnonceSedexCOAssure annonceSedexCOAssure = new SimpleAnnonceSedexCOAssure();
            findCorrespondancePersonneAssuree(insuredPerson, annonceSedexCOAssure);
            annonceSedexCOAssure.setIdAnnonceSedexCODebiteur(annonceSedexCODebiteur.getId());

            if (hasAutreDecomptes(insuredPerson)) {
                annonceSedexCOAssure.addMessage("Un décompte existe déjà pour cette personne");
            }

            annonceSedexCOAssure.setNssAssure(String.valueOf(insuredPerson.getVn()));
            annonceSedexCOAssure.setNomPrenomAssure(insuredPerson.getOfficialName() + " "
                    + insuredPerson.getFirstName());
            annonceSedexCOAssure.setNpaLocaliteAssure(getNPALocalite(insuredPerson.getAddress()));
            annonceSedexCOAssure.setRueNumeroAssure(getRueNumero(insuredPerson.getAddress()));

            if (insuredPersonWithClaimType.getPremium() != null) {
                annonceSedexCOAssure.setPrimePeriodeDebut(AMSedexRPUtil.getDateXMLToString(insuredPersonWithClaimType
                        .getPremium().getClaimStartDate()));
                annonceSedexCOAssure.setPrimePeriodeFin(AMSedexRPUtil.getDateXMLToString(insuredPersonWithClaimType
                        .getPremium().getClaimEndDate()));
                annonceSedexCOAssure.setPrimeMontant(insuredPersonWithClaimType.getPremium().getClaimAmount()
                        .toString());
            }

            if (insuredPersonWithClaimType.getCostSharing() != null) {
                annonceSedexCOAssure.setCostSharingPeriodeDebut(AMSedexRPUtil
                        .getDateXMLToString(insuredPersonWithClaimType.getCostSharing().getClaimStartDate()));
                annonceSedexCOAssure.setCostSharingPeriodeFin(AMSedexRPUtil
                        .getDateXMLToString(insuredPersonWithClaimType.getCostSharing().getClaimEndDate()));
                annonceSedexCOAssure.setCostSharingMontant(insuredPersonWithClaimType.getCostSharing().getClaimAmount()
                        .toString());
            }
            JadePersistenceManager.add(annonceSedexCOAssure);
        }
    }

    protected void checkJadeThreadErrors() throws JadePersistenceException {
        if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeBusinessMessage[] msg = JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR);
            String strErrors = "";
            for (JadeBusinessMessage jadeBusinessMessage : msg) {
                strErrors += jadeBusinessMessage.getMessageId() + "\n";
            }
            throw new JadePersistenceException("Erreur(s) pendant la création de l'annonce : " + strErrors);
        }

    }
}
