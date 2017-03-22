package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
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
import globaz.jade.sedex.JadeSedexMessageNotHandledException;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.GroupedSedexMessage;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pyxis.util.CommonNSSFormater;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import org.xml.sax.SAXException;
import ch.gdk_cds.xmlns.da_64a_5234_000401._1.ContentType;
import ch.gdk_cds.xmlns.da_64a_5234_000401._1.HeaderType;
import ch.gdk_cds.xmlns.da_64a_5234_000401._1.Message;
import ch.gdk_cds.xmlns.da_64a_common._1.CertificateOfLossArrivalType;
import ch.gdk_cds.xmlns.da_64a_common._1.ClaimInsuredPersonType;
import ch.gdk_cds.xmlns.da_64a_common._1.DebtorNPType;
import ch.gdk_cds.xmlns.da_64a_common._1.InsuredPersonType;
import ch.gdk_cds.xmlns.da_64a_common._1.InsuredPersonWithClaimType;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOXML;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.FamilleContribuableSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexCO.listes.SimpleOutputList_Decompte_5234_401_1;
import ch.globaz.amal.businessimpl.services.sedexCO.listes.SimpleOutputList_Decompte_5234_402_1;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.domaine.Taux;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.configuration.Configuration;
import ch.globaz.simpleoutputlist.configuration.RowDecorator;
import ch.globaz.simpleoutputlist.configuration.RowStyle;
import ch.globaz.simpleoutputlist.core.Details;
import ch.globaz.simpleoutputlist.outimpl.Configurations;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

public class AnnoncesCOReceptionMessage5234_000401_1 extends AnnoncesCODefault {
    private static final String PACKAGE_CLASS_FOR_READ_SEDEX_DECOMPTE_TRIMESTRIEL = "ch.gdk_cds.xmlns.da_64a_5234_000401._1";
    Set<String> listPersonnesInexistantes = new HashSet<String>();
    Map<String, String> errors = new HashMap<String, String>();
    protected String senderId = null;
    private String idTiersSender;

    public enum TypesOfLossEnum {
        ACTE_DE_DEFAUT_BIEN("1", "42003821"),
        ACTE_DE_DEFAUT_BIEN_FAILLITE("2", "42003822"),
        TITRE_EQUIVALENT("3", "42003823");

        private String value;
        private String cs;

        public String getValue() {
            return value;
        }

        public String getCs() {
            return cs;
        }

        private TypesOfLossEnum(String value, String cs) {
            this.value = value;
            this.cs = cs;
        }
    }

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

    /**
     * Méthode de lecture du message sedex en réception, et traitement
     * 
     * @param currentSimpleMessage
     * @return
     * @throws JadeSedexMessageNotHandledException
     */
    protected void importMessagesSingle(SimpleSedexMessage currentSimpleMessage)
            throws JadeSedexMessageNotHandledException {

        try {
            Class<?>[] addClasses = new Class[] { ch.gdk_cds.xmlns.da_64a_5234_000401._1.Message.class };
            jaxbs = JAXBServices.getInstance();
            Message message = (Message) jaxbs.unmarshal(currentSimpleMessage.fileLocation, false, true, addClasses);

            senderId = message.getHeader().getSenderId();
            idTiersSender = AMSedexRPUtil.getIdTiersFromSedexId(senderId);
            run(message);

            // Sauvegarde du code XML de l'annonce dans la table
            saveAnnonce(addClasses, message);
        } catch (Exception e) {
            throw new JadeSedexMessageNotHandledException("Erreur lors du traitement du message", e);
        }
    }

    private void saveAnnonce(Class<?>[] addClasses, Message message) {

        // Sauvegarde de l'annonce dans la table
        HeaderType header = message.getHeader();
        ContentType content = message.getContent();
        try {
            SimpleAnnonceSedexCO annonceSedexCO = new SimpleAnnonceSedexCO();
            annonceSedexCO.setMessageId(header.getMessageId());
            annonceSedexCO.setBusinessProcessId(header.getBusinessProcessId());
            annonceSedexCO.setMessageType(header.getMessageType());
            annonceSedexCO.setMessageSubType(header.getSubMessageType());
            annonceSedexCO.setMessageEmetteur(header.getSenderId());
            annonceSedexCO.setMessageRecepteur(header.getRecipientId());
            annonceSedexCO.setIdTiersCM(idTiersSender);
            annonceSedexCO.setPeriodeDebut(AMSedexRPUtil.getDateXMLToString(message.getContent()
                    .getCertificateOfLossQuarterlyStatement().getStatementStartDate()));
            annonceSedexCO.setPeriodeFin(AMSedexRPUtil.getDateXMLToString(message.getContent()
                    .getCertificateOfLossQuarterlyStatement().getStatementEndDate()));
            annonceSedexCO.setStatus(IAMCodeSysteme.AMStatutAnnonceSedex.ENVOYE.getValue());
            annonceSedexCO.setDateAnnonce(Date.now().getSwissValue());
            JadePersistenceManager.add(annonceSedexCO);
            checkJadeThreadErrors();

            saveXml(addClasses, message);
        } catch (Exception ex) {
            JadeThread.logError(
                    "AnnoncesCOReceptionMessage5234_000401_1.saveAnnonce()",
                    "Erreur pendant la sauvegarde de l'annonce du décompte trimestriel ! (Msg id : "
                            + header.getMessageId() + ") => " + ex.getMessage());
        }
    }

    private void saveXml(Class<?>[] addClasses, Message message) throws JAXBException, SAXException, IOException,
            JAXBValidationError, JAXBValidationWarning, JadePersistenceException {
        StringWriter sw = new StringWriter();
        jaxbs.marshal(message, sw, false, true, addClasses);
        SimpleAnnonceSedexCOXML annonceSedexCOXML = new SimpleAnnonceSedexCOXML();
        annonceSedexCOXML.setMessageId(message.getHeader().getMessageId());
        annonceSedexCOXML.setXml(sw.toString());
        JadePersistenceManager.add(annonceSedexCOXML);
        checkJadeThreadErrors();
    }

    /**
     * @param message
     * @return La liste des personnes non trouvées
     * @throws Exception
     */
    private void run(Message message) throws Exception {
        List<CertificateOfLossArrivalType> decomptesTrimestriels = message.getContent()
                .getCertificateOfLossQuarterlyStatement().getCertificateOfLossArrival();
        Date debutPeriodeAObserver = new Date(message.getContent().getCertificateOfLossQuarterlyStatement()
                .getStatementStartDate().toGregorianCalendar().getTime());
        Date finPeriodeAObserver = new Date(message.getContent().getCertificateOfLossQuarterlyStatement()
                .getStatementEndDate().toGregorianCalendar().getTime());
        Periode periodeAObserver = new Periode(debutPeriodeAObserver.getSwissMonthValue(),
                finPeriodeAObserver.getSwissMonthValue());

        List<SimpleOutputList_Decompte_5234_401_1> listLigneDecompteTrimestriel = createLigneDecompte(
                decomptesTrimestriels, periodeAObserver);

        File listePrinted = printList(listLigneDecompteTrimestriel);

        String[] files = new String[1];
        if (listePrinted != null) {
            files[0] = listePrinted.getPath();
        }

        prepareEmail(files);
    }

    protected void prepareEmail(String[] files) throws Exception {
        sendMail(files, "Décompte trimestriel");
    }

    protected void sendMail(String[] files, String typeDecompte) throws Exception {
        String subject = "Contentieux Amal : réception des annonces '" + typeDecompte + "' effectuée avec succès !";
        StringBuilder body = new StringBuilder();
        if (!listPersonnesInexistantes.isEmpty()) {
            if (listPersonnesInexistantes.size() > 1) {
                subject = listPersonnesInexistantes.size()
                        + " personnes non connues détectées lors de la réception des annonces de type '" + typeDecompte
                        + "' !";
            } else {
                subject = "1 personne non connue détectée lors de la réception des annonces de type '" + typeDecompte
                        + "' !";
            }
            body.append("Liste des personnes non trouvées :\n");

            for (String personneInexistante : listPersonnesInexistantes) {
                body.append("   -" + personneInexistante + "\n");
            }

        }

        if (!errors.isEmpty()) {
            body.append("Autre(s) erreur(s) :\n");
            for (Entry<String, String> error : errors.entrySet()) {
                String type = error.getKey();
                String msg = error.getValue();
                body.append("   - " + type + " - " + msg + "\n");
            }
        }
        JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
                body.toString(), files);
    }

    protected List<SimpleOutputList_Decompte_5234_401_1> createLigneDecompte(
            List<CertificateOfLossArrivalType> decomptesTrimestriels, Periode periodeAObserver)
            throws JadePersistenceException, JadeNoBusinessLogSessionError {

        List<SimpleOutputList_Decompte_5234_401_1> listLigneDecompteTrimestriel = new ArrayList<SimpleOutputList_Decompte_5234_401_1>();

        Montant totalMontantPrime = new Montant(0);
        Montant totalMontantParticipation = new Montant(0);
        Montant totalInterets = new Montant(0);
        Montant totalFrais = new Montant(0);
        Montant totalTotal = new Montant(0);
        for (CertificateOfLossArrivalType decompteTrim : decomptesTrimestriels) {
            SimpleOutputList_Decompte_5234_401_1 ligneDecompte = new SimpleOutputList_Decompte_5234_401_1();

            if (decompteTrim.getDebtorWithClaim().getDebtor().getDebtorNP() != null) {
                DebtorNPType debiteur = decompteTrim.getDebtorWithClaim().getDebtor().getDebtorNP();
                CommonNSSFormater nssFormateur = new CommonNSSFormater();

                String nssDebiteurFormate = "";
                try {
                    nssDebiteurFormate = nssFormateur.format(debiteur.getVn().toString());
                } catch (Exception e) {
                    nssDebiteurFormate = "N/A";
                }

                List<InsuredPersonWithClaimType> listAssures = decompteTrim.getInsuredPersonWithClaim();
                int cptPersonne = 0;
                for (InsuredPersonWithClaimType insuredPersonWithClaimType : listAssures) {
                    ligneDecompte = new SimpleOutputList_Decompte_5234_401_1();

                    InsuredPersonType insuredPersonne = insuredPersonWithClaimType.getInsuredPerson();
                    String nssAssureFormate = "";
                    try {
                        nssAssureFormate = nssFormateur.format(String.valueOf(insuredPersonne.getVn()));
                    } catch (Exception e) {
                        nssAssureFormate = "N/A";
                    }
                    ligneDecompte.setNssAssure(nssAssureFormate);

                    ligneDecompte.setNomPrenomAssure(insuredPersonne.getOfficialName() + " "
                            + insuredPersonne.getFirstName());

                    ClaimInsuredPersonType premium = insuredPersonWithClaimType.getPremium();
                    ClaimInsuredPersonType costSharing = insuredPersonWithClaimType.getCostSharing();
                    Periode periodePremiumOrSharing = null;
                    if (costSharing != null) {
                        periodePremiumOrSharing = new Periode(getDateFromXmlToString(costSharing.getClaimStartDate()),
                                getDateFromXmlToString(costSharing.getClaimEndDate()));
                        ligneDecompte.setSharingPeriode(periodePremiumOrSharing);
                        Montant sharingMontant = new Montant(costSharing.getClaimAmount());
                        ligneDecompte.setSharingMontant(sharingMontant);
                        totalMontantParticipation = totalMontantParticipation.add(sharingMontant);
                    }

                    if (premium != null) {
                        periodePremiumOrSharing = new Periode(getDateFromXmlToString(premium.getClaimStartDate()),
                                getDateFromXmlToString(premium.getClaimEndDate()));
                        ligneDecompte.setPrimePeriode(periodePremiumOrSharing);
                        Montant primeMontant = new Montant(premium.getClaimAmount());
                        ligneDecompte.setPrimeMontant(primeMontant);
                        totalMontantPrime = totalMontantPrime.add(primeMontant);
                    }

                    // On n'affiche les informations suivantes (NSS, nom, prénom,...) que si c'est la 1ère fois qu'on
                    // affiche la ligne
                    if (cptPersonne == 0) {
                        ligneDecompte.setNssDebiteur(nssDebiteurFormate);

                        ligneDecompte.setNomPrenomDebiteur(debiteur.getOfficialName() + " " + debiteur.getFirstName());

                        TypesOfLossEnum typeActe = getTypeActe(decompteTrim.getCertificateOfLoss().getTypeOfLoss());
                        ligneDecompte.setTypeActe(typeActe.getCs());

                        Montant interets = new Montant(decompteTrim.getDebtorWithClaim().getClaimDebtor()
                                .getInterests());
                        ligneDecompte.setInterets(interets);
                        totalInterets = totalInterets.add(interets);

                        Montant frais = new Montant(decompteTrim.getDebtorWithClaim().getClaimDebtor().getExpenses());
                        ligneDecompte.setFrais(frais);
                        totalFrais = totalFrais.add(frais);

                        Montant total = new Montant(decompteTrim.getDebtorWithClaim().getClaimDebtor().getTotalClaim());
                        ligneDecompte.setTotal(total);
                        totalTotal = totalTotal.add(total);

                        StringBuilder message = new StringBuilder();
                        FamilleContribuable familleContribuable = searchPersonne(nssDebiteurFormate, periodeAObserver);
                        if (familleContribuable != null) {
                            if (!interets.isZero() || !frais.isZero()) {
                                message.append(getMessageBeneficiaireASouPC(familleContribuable,
                                        periodePremiumOrSharing));
                            }
                            message.append(getMessageAssureurDifferent(familleContribuable, periodePremiumOrSharing));
                        }
                        ligneDecompte.setMessage(message.toString());
                    }

                    FamilleContribuable personneAssuree = searchPersonne(nssAssureFormate, null);

                    cptPersonne++;
                    listLigneDecompteTrimestriel.add(ligneDecompte);
                }
            } else if (decompteTrim.getDebtorWithClaim().getDebtor().getDebtorJP() != null) {
                JadeThread.logWarn(this.getClass().getName(),
                        "Les personnes morales ne sont pas prises en charge par l'application");
            }
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
        listLigneDecompteTrimestriel.add(ligneTotal);

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
        listLigneDecompteTrimestriel.add(ligneTotal85);

        return listLigneDecompteTrimestriel;
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

    private StringBuilder getMessageAssureurDifferent(FamilleContribuable familleContribuable,
            Periode periodePremiumOrSharing) {
        StringBuilder colonneMessage = new StringBuilder();
        Periode periodeSubside = getPeriodeSubside(familleContribuable);
        try {
            if (periodePremiumOrSharing.isDateDansLaPeriode(periodeSubside.getDateDebut())
                    && periodePremiumOrSharing.isDateDansLaPeriode(periodeSubside.getDateFin())) {
                String noCaisseMaladieCO = AMSedexRPUtil.getIdTiersFromSedexId(senderId);
                String noCaisseMaladieSubside = familleContribuable.getSimpleDetailFamille().getNoCaisseMaladie();
                if (!noCaisseMaladieCO.equals(noCaisseMaladieSubside)) {
                    colonneMessage.append("Assureur différent,");
                }
            }
        } catch (AnnonceSedexException e) {
            e.printStackTrace();
            colonneMessage.append("Erreur : " + e);
        }

        return colonneMessage;
    }

    private StringBuilder getMessageBeneficiaireASouPC(FamilleContribuable familleContribuable,
            Periode periodePremiumOrSharing) {
        StringBuilder colonneMessage = new StringBuilder();
        Periode periodeSubside = getPeriodeSubside(familleContribuable);

        if (periodePremiumOrSharing.isDateDansLaPeriode(periodeSubside.getDateDebut())
                && periodePremiumOrSharing.isDateDansLaPeriode(periodeSubside.getDateFin())) {
            if (IAMCodeSysteme.AMTypeDemandeSubside.ASSISTE.equals(familleContribuable.getSimpleDetailFamille()
                    .getTypeDemande())
                    || IAMCodeSysteme.AMTypeDemandeSubside.PC.equals(familleContribuable.getSimpleDetailFamille()
                            .getTypeDemande())) {
                colonneMessage.append("Bénéficiaire aide social ou PC, ");
            }
        }

        return colonneMessage;
    }

    private Periode getPeriodeSubside(FamilleContribuable familleContribuable) {
        String finDroit = "";
        if (JadeStringUtil.isBlankOrZero(familleContribuable.getSimpleDetailFamille().getFinDroit())) {
            Date dateDebut = new Date(familleContribuable.getSimpleDetailFamille().getDebutDroit());
            finDroit = "12." + dateDebut.getAnnee();
        } else {
            finDroit = familleContribuable.getSimpleDetailFamille().getFinDroit();
        }
        Periode periodeSubside = new Periode(familleContribuable.getSimpleDetailFamille().getDebutDroit(), finDroit);
        return periodeSubside;
    }

    protected FamilleContribuable searchPersonne(String nss, Periode periodeAObserver) throws JadePersistenceException {

        try {
            FamilleContribuableSearch familleContribuableSearch = new FamilleContribuableSearch();
            familleContribuableSearch.setForNNSS(nss);
            // Pas de date de fin au membre famille
            familleContribuableSearch.setForFinDefinitive("0");

            if (periodeAObserver != null) {
                familleContribuableSearch.setForAnneeHistorique(new Date(periodeAObserver.getDateDebut()).getAnnee());
                familleContribuableSearch.setForDebutDroitGOE(periodeAObserver.getDateDebut());
                familleContribuableSearch.setForFinDroitLOE(periodeAObserver.getDateFin());
            }
            familleContribuableSearch = AmalServiceLocator.getFamilleContribuableService().search(
                    familleContribuableSearch);

            if (familleContribuableSearch.getNbOfResultMatchingQuery() == 0) {
                listPersonnesInexistantes.add("Personne non trouvée avec le nss : " + nss);
                return null;
            } else {
                return (FamilleContribuable) familleContribuableSearch.getSearchResults()[0];
            }
        } catch (Exception ex) {
            throw new JadePersistenceException("Erreur pendant la recherche de la personne " + nss, ex);
        }
    }

    private File printList(List<SimpleOutputList_Decompte_5234_401_1> listLigneDecompteTrimestriel) {

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

        String nomCaisseMaladieEmetteur = getNomCaisseMaladie(idTiersSender);

        SimpleOutputListBuilder builder = SimpleOutputListBuilderJade.newInstance()
                .outputNameAndAddPath("décompteTrimestriel").addList(listLigneDecompteTrimestriel)
                .classElementList(SimpleOutputList_Decompte_5234_401_1.class);
        File file = builder.addTitle(nomCaisseMaladieEmetteur, Align.LEFT).addSubTitle("Décompte trimestriel")
                .configure(config).addHeaderDetails(details).asXls().build();

        return file;
    }

    protected String getDateFromXmlToString(XMLGregorianCalendar date) {
        String jour = JadeStringUtil.fillWithZeroes(String.valueOf(date.getDay()), 2);
        String mois = JadeStringUtil.fillWithZeroes(String.valueOf(date.getMonth()), 2);

        return jour + "." + mois + "." + date.getYear();

    }

    protected TypesOfLossEnum getTypeActe(String typeOfLoss) {
        if (TypesOfLossEnum.ACTE_DE_DEFAUT_BIEN.getValue().equals(typeOfLoss)) {
            return TypesOfLossEnum.ACTE_DE_DEFAUT_BIEN;
        } else if (TypesOfLossEnum.ACTE_DE_DEFAUT_BIEN_FAILLITE.getValue().equals(typeOfLoss)) {
            return TypesOfLossEnum.ACTE_DE_DEFAUT_BIEN_FAILLITE;
        } else if (TypesOfLossEnum.TITRE_EQUIVALENT.getValue().equals(typeOfLoss)) {
            return TypesOfLossEnum.TITRE_EQUIVALENT;
        } else {
            return null;
        }
    }

    protected void checkJadeThreadErrors() throws JadePersistenceException {
        if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeBusinessMessage[] msg = JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR);
            String strErrors = "";
            for (JadeBusinessMessage jadeBusinessMessage : msg) {
                errors.put("JadeError", jadeBusinessMessage.getMessageId());
                strErrors += jadeBusinessMessage.getMessageId() + "\n";
            }
            throw new JadePersistenceException("Erreur(s) pendant la création de l'annonce : " + strErrors);
        }

    }
}
