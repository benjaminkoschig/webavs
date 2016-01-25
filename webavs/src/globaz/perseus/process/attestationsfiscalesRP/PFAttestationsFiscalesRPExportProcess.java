package globaz.perseus.process.attestationsfiscalesRP;

import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.perseus.process.PFAbstractJob;
import globaz.perseus.utils.PFUserHelper;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.constantes.IConstantes;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSEtatRentePont;
import ch.globaz.perseus.business.constantes.CSSexePersonne;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.models.rentepont.RentePontSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.attestationsfiscales.AttestationsFiscalesRPBuilder;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

public class PFAttestationsFiscalesRPExportProcess extends PFAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * La variable de l'adresse email est automatiquement setter à NULL si elle est nommée (eMailAddress) et doit donc
     * être renommée différement (mailAd) pour fonctionner correctement.
     */

    private String adresseCaisse = null;
    private JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();
    private String anneeAttestations = null;
    private boolean attestationUniqueAutreCaisse = false;
    private boolean attestationUniqueMontantZero = false;
    private String caisse = null;
    private String codePostaleCaisse = null;
    private String dateDebut = null;
    private String dateDocument = null;
    private String dateFin = null;
    private RentePont dernierRentePont = null;
    private Dossier dossier = null;
    private Set<String> groupDossier = new HashSet<String>();
    private boolean hasDecisionOctroiComplet = false;
    private String idDossier = null;
    private boolean isAttestationTiersUnique = false;
    private boolean isAttestationUnique = false;
    private String isSendToGed = "";
    private String localiteCaisse = null;
    private String mailAd = null;
    private String nomCaisse = null;
    private String nomPersonnecontactCaisse = null;
    private String prenomPersonneContactCaisse = null;
    private boolean presenceAttestation = false;
    private RentePont rentePont = null;
    private String telPersonneContactCaisse = null;

    private String createFile(String content, String name) throws JadeApplicationException, IOException {

        String paht = Jade.getInstance().getPersistenceDir() + "_" + name + "_" + JadeUUIDGenerator.createStringUUID()
                + ".csv";
        java.io.File f = new java.io.File(paht);
        FileWriter fstream;

        fstream = new FileWriter(f);
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(content);
        out.close();

        return paht;
    }

    public void createLetteAttestationRequerant(RentePont dernierRentePont) throws Exception {

        JadePublishDocumentInfo pubInfos = new JadePublishDocumentInfo();
        pubInfos.setDocumentTitle(getSession().getLabel("PDF_LETTRE_EN_TETE_ATTESTATION_TITRE_MAIL"));
        pubInfos.setDocumentSubject(getSession().getLabel("PDF_LETTRE_EN_TETE_ATTESTATION_TITRE_MAIL"));
        pubInfos.setOwnerEmail(mailAd);
        pubInfos.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, mailAd);
        if ("on".equals(isSendToGed)) {
            pubInfos.setArchiveDocument(true);
        } else {
            pubInfos.setArchiveDocument(false);
        }
        pubInfos.setPublishDocument(false);
        pubInfos.setDocumentType(IPRConstantesExternes.PCF_ATTESTATION_FISCALE_RP);
        pubInfos.setDocumentTypeNumber(IPRConstantesExternes.PCF_ATTESTATION_FISCALE_RP);

        AttestationsFiscalesRPBuilder builder = new AttestationsFiscalesRPBuilder();
        builder.setCaisse(dernierRentePont.getSimpleRentePont().getCsCaisse());
        builder.setDateDocument(dateDocument);
        builder.setAdMail(mailAd);
        builder.setAnnee(anneeAttestations);
        builder.setDernierRentePont(dernierRentePont);

        allDoc.addDocument(builder.build(), pubInfos);

    }

    public RentePont dernierRentePont(String idDossier) throws RentePontException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        RentePont rp = null;
        RentePontSearchModel searchRP = new RentePontSearchModel();
        searchRP.setForIdDossier(idDossier);
        searchRP.setForCsEtat(CSEtatRentePont.VALIDE.getCodeSystem());
        searchRP.setOrderKey(RentePontSearchModel.ORDER_BY_DATE_DEBUT_DESC);
        searchRP = PerseusServiceLocator.getRentePontService().search(searchRP);
        for (JadeAbstractModel model : searchRP.getSearchResults()) {
            rp = (RentePont) model;
            break;
        }

        return rp;

    }

    public String getAdresseCaisse() {
        return adresseCaisse;
    }

    public String getAnneeAttestations() {
        return anneeAttestations;
    }

    public String getCaisse() {
        return caisse;
    }

    public String getCodePostaleCaisse() {
        return codePostaleCaisse;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIsSendToGed() {
        return isSendToGed;
    }

    public String getLocaliteCaisse() {
        return localiteCaisse;
    }

    public String getMailAd() {
        return mailAd;
    }

    @Override
    public String getName() {
        return null;
    }

    public String getNomCaisse() {
        return nomCaisse;
    }

    public String getNomPersonnecontactCaisse() {
        return nomPersonnecontactCaisse;
    }

    public String getPrenomPersonneContactCaisse() {
        return prenomPersonneContactCaisse;
    }

    public String getTelPersonneContactCaisse() {
        return telPersonneContactCaisse;
    }

    public boolean isAttestationUnique() {
        return isAttestationUnique;
    }

    @Override
    protected void process() throws Exception {
        try {

            RentePontSearchModel rpsearch = new RentePontSearchModel();
            if (!JadeStringUtil.isEmpty(idDossier)) {
                rpsearch.setForIdDossier(idDossier);
                isAttestationTiersUnique = true;
                dossier = PerseusServiceLocator.getDossierService().read(idDossier);
            }

            rpsearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            rpsearch.setForCsEtat(CSEtatRentePont.VALIDE.getCodeSystem());
            rpsearch.setForCsCaisse(caisse);
            rpsearch.setForDateDebut("01.01." + anneeAttestations);
            rpsearch.setForDateFin("31.12." + anneeAttestations);
            rpsearch.setWhereKey(RentePontSearchModel.WITH_ANNEE_VALABLE);
            rpsearch.setOrderKey(RentePontSearchModel.ORDER_BY_DATE_DEBUT_ASC_NOM_ASC);
            rpsearch = PerseusServiceLocator.getRentePontService().search(rpsearch);

            // Saisir l'employeur en dehors du for
            StringBuffer content = new StringBuffer();

            // TRAITEMENT DES DONNEES PERSONNELLES DE L'EMPLOYEUR
            // 1. Renseigne l'identifiant 'employeur' pour l'application fiscale
            content.append("1;");
            // 2. Renseigne le nom
            content.append(getNomCaisse() + ";");
            // 3. Renseigne la rue
            content.append(getAdresseCaisse() + ";");
            // 4. Renseigne la case postale
            content.append(";");
            // 5. Renseigne le numéro postale
            content.append(getCodePostaleCaisse() + ";");
            // 6. Renseigne la localité
            content.append(getLocaliteCaisse() + ";");
            // 7. Renseigne le pays
            content.append(";");
            // 8. Renseigne le numéro de téléphone
            content.append(";");
            // 9. Renseigne le prénom de la personne de contact
            content.append(getPrenomPersonneContactCaisse() + ";");
            // 10. Renseigne le nom de la personne de contact
            content.append(getNomPersonnecontactCaisse() + ";");
            // 11. Renseigne le téléphone de la personne de contact
            content.append(getTelPersonneContactCaisse() + ";");
            // Insertion d'un retour à la ligne pour les données suivantes
            content.append("\n");

            for (JadeAbstractModel model : rpsearch.getSearchResults()) {
                rentePont = (RentePont) model;

                if (groupDossier.contains(rentePont.getDossier().getId())) {
                    continue;
                }

                // Ne pas prendre en compte la demande si elle est imposée à la source et si le montant rétroactif
                // ou le montant menseul = 0
                if (!"0.00".equals(rentePont.getSimpleRentePont().getMontantImpotSource())
                        || ("0.00".equals(rentePont.getSimpleRentePont().getMontantImpotSource()) && "0.00"
                                .equals(rentePont.getSimpleRentePont().getMontantRetroactif()))) {
                    continue;
                } else {
                    hasDecisionOctroiComplet = true;
                }

                // Récupération de l'adresse
                AdresseTiersDetail detailTiers = PFUserHelper.getAdresseAssure(rentePont.getDossier()
                        .getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers(),
                        IConstantes.CS_AVOIR_ADRESSE_DOMICILE, JACalendar.todayJJsMMsAAAA());

                if (null == detailTiers.getAdresseFormate()) {
                    // Si pas d'adresse, je logg pour quel tiers aucune adresse n'a été trouvée
                    JadeThread.logWarn(this.getClass().getName(), "Aucune adresse pour le tiers : "
                            + rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                                    .getNumAvsActuel()
                            + " "
                            + rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                                    .getDesignation1()
                            + " "
                            + rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                                    .getDesignation2());
                    groupDossier.add(rentePont.getSimpleRentePont().getIdDossier());
                    continue;
                }

                // La dernière rente pont doit être utilisé pour déterminer la caisse !!!!
                dernierRentePont = dernierRentePont(rentePont.getSimpleRentePont().getIdDossier());

                if (!caisse.equals(dernierRentePont.getSimpleRentePont().getCsCaisse())) {
                    if (isAttestationTiersUnique) {
                        attestationUniqueAutreCaisse = true;
                    }
                    continue;
                }

                // Determiner la dernière demande de rente pont en octroi pour la date de fin
                if (!"0.00".equals(dernierRentePont.getSimpleRentePont().getMontantImpotSource())
                        || ("0.00".equals(dernierRentePont.getSimpleRentePont().getMontantImpotSource()) && "0.00"
                                .equals(dernierRentePont.getSimpleRentePont().getMontantRetroactif()))) {
                    RentePontSearchModel searchRP = new RentePontSearchModel();
                    searchRP.setForIdDossier(idDossier);
                    searchRP.setForCsEtat(CSEtatRentePont.VALIDE.getCodeSystem());
                    searchRP.setOrderKey(RentePontSearchModel.ORDER_BY_DATE_DEBUT_DESC);
                    searchRP = PerseusServiceLocator.getRentePontService().search(searchRP);
                    for (JadeAbstractModel modelRP : searchRP.getSearchResults()) {
                        RentePont rp = (RentePont) modelRP;
                        if (!"0.00".equals(rp.getSimpleRentePont().getMontantImpotSource())
                                || ("0.00".equals(rp.getSimpleRentePont().getMontantImpotSource()) && "0.00".equals(rp
                                        .getSimpleRentePont().getMontantRetroactif()))) {
                            dernierRentePont = rp;
                        }
                    }
                }

                // Controle pour prendre la date de début au plus tôt le 01.01 de l'année
                JACalendar j = new JACalendarGregorian();
                if (JACalendar.COMPARE_FIRSTLOWER == j.compare(rentePont.getSimpleRentePont().getDateDebut(), "01.01."
                        + anneeAttestations)) {
                    dateDebut = "01.01." + anneeAttestations;
                } else {
                    dateDebut = rentePont.getSimpleRentePont().getDateDebut();
                }

                // Date fin
                if (JadeStringUtil.isEmpty(dernierRentePont.getSimpleRentePont().getDateFin())) {
                    // Si date de fin vide
                    String anneeDernierPmtMensuel = PRDateFormater.convertDate_MMxAAAA_to_AAAA(PerseusServiceLocator
                            .getPmtMensuelRentePontService().getDateDernierPmt());
                    if (anneeDernierPmtMensuel.equals(anneeAttestations)) {
                        // utilisation de la date du dernier pmt menseul
                        dateFin = JadeDateUtil
                                .addDays(
                                        JadeDateUtil.addMonths("01."
                                                + PerseusServiceLocator.getPmtMensuelRentePontService()
                                                        .getDateDernierPmt(), 1), -1);
                    } else {
                        // utilisation de la date de fin d'année
                        dateFin = "31.12." + anneeAttestations;
                    }

                } else {
                    // Si date de fin non vide
                    // Attention elle pourrait être plus grande que celle de l'année que l'on est en train de traité

                    if (anneeAttestations.equals(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(dernierRentePont
                            .getSimpleRentePont().getDateFin()))) {
                        if (!hasDecisionOctroiComplet) {
                            dateFin = JadeDateUtil.addDays(dernierRentePont.getSimpleRentePont().getDateDebut(), -1);
                        } else {
                            dateFin = dernierRentePont.getSimpleRentePont().getDateFin();
                        }

                    } else {
                        dateFin = "31.12." + anneeAttestations;
                    }
                }

                // Calcul du montant versée pour l'année de l'attestation
                FWCurrency montantVerse = PerseusServiceLocator.getDossierService().calculerMontantVerseAttestationRP(
                        rentePont.getDossier(), anneeAttestations);

                if (montantVerse.intValue() > 0) {

                    presenceAttestation = true;

                    // TRAITEMENT DES DONNEES PERSONNELLES DE L'EMPLOYEE/REQUERANT
                    // 1. Renseigne l'identifiant 'employé/requérant' pour l'application fisclae
                    content.append("2;");
                    // 2. Renseigne l'ancien numero AVS du requérant
                    content.append(";");
                    // 3. Renseigne le nouveau numéro AVS du requérant
                    content.append(rentePont.getDossier().getDemandePrestation().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel()
                            + ";");
                    // 4. Renseigne la date de naissance du requérant
                    content.append(rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getPersonne()
                            .getDateNaissance()
                            + ";");
                    // 5. Renseigne le sexe du requérant
                    String sexe = "";
                    if (CSSexePersonne.MALE.getCodeSystem().equals(
                            rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getPersonne().getSexe())) {
                        sexe = "M";
                    } else if (CSSexePersonne.FEMELLE.getCodeSystem().equals(
                            rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getPersonne().getSexe())) {
                        sexe = "F";
                    }
                    content.append(sexe + ";");
                    // 6. Renseigne le prénom du requérant
                    content.append(rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                            .getDesignation2()
                            + ";");
                    // 7. Renseigne le nom du reqérant
                    content.append(rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                            .getDesignation1()
                            + ";");

                    // 8. Renseigne l'adresse du requérant
                    content.append(detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE) + " "
                            + detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO) + ";");
                    // 9. Renseigne la case postale
                    content.append(";");
                    // 10. Renseigne le numéro postal du requérant
                    content.append(detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA) + ";");
                    // 11. Renseigne la localité du requérant
                    content.append(detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE) + ";");
                    // 12. Renseigne le pays du requérant
                    content.append(detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_PAYS) + ";");
                    // Insertion d'un retour à la ligne pour les données suivantes
                    content.append("\n");

                    // TRAITEMENT DES DONNEES FINANCIERES DU REQUERANT
                    // 1. Renseigne l'identifiant 'donneesFinanciere' pour l'application fiscale
                    content.append("3;");
                    // 2. Renseigne le type d'attestation <<SR = Certificat de salaire>> ou <<AR = Attestation de
                    // rente>>
                    // remarque: SR obligatoire pour pouvoir insérer l'impot à la source dans les données
                    content.append("AR;");
                    // 3. Renseigne l'année de traitement
                    content.append(anneeAttestations + ";");
                    // 4. Renseigne la date de début
                    content.append(dateDebut + ";");
                    // 5. Renseigne la date de fin
                    content.append(dateFin + ";");
                    // 6. Renseigne si transports gratuits entre domicile et lieu de travail
                    content.append(";");
                    // 7. Renseigne si reaps cantine /chèque repas
                    content.append(";");
                    // 8. Renseigne salaire/rente (montant)
                    content.append(montantVerse.toString() + ";");
                    // 9. Renseigne prestation de salaire accessoire : pension, logement (montant)
                    content.append(";");
                    // 10. Renseigne prestation de salaire accessoire : part privée voiture de service (montant)
                    content.append(";");
                    // 11. Renseigne prestation salariale accessoires : autre - genre (texte)
                    content.append(";");
                    // 12. Renseigne prestation salariale accessoire : autre (montant)
                    content.append(";");
                    // 13. Renseigne prestation non periodique : genre (texte)
                    content.append(";");
                    // 14. Renseigne prestation non periodique (montant)
                    content.append(";");
                    // 15. Renseigne prestation en capital : genre (texte)
                    content.append(";");
                    // 16. Renseigne prestation en capital (montant)
                    content.append(";");
                    // 17. Renseigne droit de participation selon annexe (montant)
                    content.append(";");
                    // 18. Renseigne indeminité des membres de l'administration (montant)
                    content.append(";");
                    // 19. Renseigne autres prestations : genre (texte)
                    content.append(";");
                    // 20. Renseigne autres prestations (montant)
                    content.append(";");
                    // 21. Renseigne cotisation AVS/AI/APG/AC/AANP
                    content.append(";");
                    // 22. Renseigne prévoyance professionnelle 2ème pilier : cotisation ordinaire (montant)
                    content.append(";");
                    // 23. Renseigne prévoyance professionnelle 2ème pilier : cotisation pour le rachat
                    content.append(";");
                    // 24. Renseigne retenue de l'impôt à la source(montant)
                    // content.append(PerseusServiceLocator.getDossierService().findImpotSource(
                    // this.dernierDemande.getDossier(), this.dateDebut, "31.12." + this.anneeAttestations)
                    // + ";");
                    content.append(";");
                    // 25. Renseigne allocationpour frais : frais effectifs
                    content.append(";");
                    // 26. Renseigne allocation pour frais : voyage, repas, nuitées (montant)
                    content.append(";");
                    // 27. Renseigne allocation pour frais : frais effectifs - autre - genre (texte)
                    content.append(";");
                    // 28. Renseigne allocation pour frais : frais effectifs - autre - genre (montant)
                    content.append(";");
                    // 29. Renseigne allocation pour frais : frais forfaitaires - représentation (montant)
                    content.append(";");
                    // 30. Renseigne allocation pour frais : frais forfaitaires - voiture (montant)
                    content.append(";");
                    // 31. Renseigne allocation pour frais : frais forfaitaires - autre - genre (texte)
                    content.append(";");
                    // 32. Renseigne allocation pour frais : frais forfaitaires - autre (montant)
                    content.append(";");
                    // 33. Renseigne contribution au perfectionnment
                    content.append(";");
                    // 34. Renseigne autres prestations salariale accessoires : genre (texte)
                    content.append(";");
                    // 35. Renseigne des observations (texte)
                    content.append(";");
                    // Insertion d'un retour à la ligne pour les données suivantes
                    content.append("\n");

                    groupDossier.add(rentePont.getSimpleRentePont().getIdDossier());
                    createLetteAttestationRequerant(dernierRentePont);
                } else if (montantVerse.intValue() == 0) {
                    if (isAttestationTiersUnique) {
                        attestationUniqueMontantZero = true;
                    }
                }
            }

            if (presenceAttestation) {
                // Envois du mail avec le fichier .csv si présent
                if (isAttestationTiersUnique) {
                    // Génération du mail pour le tiers spécifié
                    JadeSmtpClient.getInstance().sendMail(
                            mailAd,
                            getSession().getLabel("PDF_ATTESTATIONSFISCALES_RP_D_TITRE_MAIL")
                                    + rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                                            .getDesignation1()
                                    + " "
                                    + rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                                            .getDesignation2() + " - " + CSCaisse.getEnumFromCodeSystem(caisse) + " "
                                    + anneeAttestations,
                            getSession().getLabel("MESSAGE_ATTESTATION_RECU"),
                            new String[] { createFile(content.toString(),
                                    getSession().getLabel("PDF_ATTESTATIONSFISCALES_RP_D_TITRE_MAIL")) });

                } else {
                    // Génération du mail pour tous les tiers propre à une caisse donnée et une année donnée
                    JadeSmtpClient.getInstance().sendMail(
                            mailAd,
                            getSession().getLabel("PDF_ATTESTATIONSFISCALES_RP_D_TITRE_MAIL")
                                    + CSCaisse.getEnumFromCodeSystem(caisse) + " " + anneeAttestations,
                            getSession().getLabel("MESSAGE_ATTESTATION_RECU"),
                            new String[] { createFile(content.toString(),
                                    getSession().getLabel("PDF_ATTESTATIONSFISCALES_RP_D_TITRE_MAIL")) });
                }

                if (presenceAttestation) {
                    if (isAttestationTiersUnique) {
                        // Si la lettre d'en-tête concerne un tiers précis
                        JadePublishDocumentInfo pubInfosDestination = new JadePublishDocumentInfo();
                        pubInfosDestination.setDocumentTitle(getSession().getLabel(
                                "PDF_LETTRE_EN_TETE_ATTESTATION_RP_TITRE_MAIL")
                                + " : "
                                + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1()
                                + " "
                                + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation2()
                                + " - " + CSCaisse.getEnumFromCodeSystem(caisse) + " " + anneeAttestations);
                        pubInfosDestination.setDocumentSubject(getSession().getLabel(
                                "PDF_LETTRE_EN_TETE_ATTESTATION_RP_TITRE_MAIL")
                                + " : "
                                + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1()
                                + " "
                                + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation2()
                                + " - " + CSCaisse.getEnumFromCodeSystem(caisse) + " " + anneeAttestations);
                        pubInfosDestination.setDocumentNotes(getSession()
                                .getLabel("MESSAGE_LETTRE_EN_TETE_ATTESTATION"));
                        pubInfosDestination.setOwnerEmail(mailAd);
                        pubInfosDestination.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, mailAd);
                        pubInfosDestination.setArchiveDocument(false);
                        pubInfosDestination.setPublishDocument(true);
                        allDoc.setMergedDocDestination(pubInfosDestination);
                    } else {
                        // Si la lettre d'en-tête concerne un ensemble de tiers
                        JadePublishDocumentInfo pubInfosDestination = new JadePublishDocumentInfo();
                        pubInfosDestination.setDocumentTitle(getSession().getLabel(
                                "PDF_LETTRE_EN_TETE_ATTESTATION_RP_TITRE_MAIL")
                                + " - " + CSCaisse.getEnumFromCodeSystem(caisse) + " " + anneeAttestations);
                        pubInfosDestination.setDocumentSubject(getSession().getLabel(
                                "PDF_LETTRE_EN_TETE_ATTESTATION_RP_TITRE_MAIL")
                                + " - " + CSCaisse.getEnumFromCodeSystem(caisse) + " " + anneeAttestations);
                        pubInfosDestination.setDocumentNotes(getSession()
                                .getLabel("MESSAGE_LETTRE_EN_TETE_ATTESTATION"));
                        pubInfosDestination.setOwnerEmail(mailAd);
                        pubInfosDestination.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, mailAd);
                        pubInfosDestination.setArchiveDocument(false);
                        pubInfosDestination.setPublishDocument(true);
                        pubInfosDestination.setDocumentType(IPRConstantesExternes.PCF_ATTESTATION_FISCALE_RP);
                        pubInfosDestination.setDocumentTypeNumber(IPRConstantesExternes.PCF_ATTESTATION_FISCALE_RP);
                        allDoc.setMergedDocDestination(pubInfosDestination);
                    }
                }
                this.createDocuments(allDoc);

            } else {
                // Envois du mail sans le fichier .csv avec message d'absence d'attestation
                if (isAttestationTiersUnique) {
                    // Génération du mail pour le tiers spécifié
                    if (rentePont != null) {
                        // La demande du searchModel est en etat validée

                        String warning = "";

                        if (attestationUniqueAutreCaisse) {
                            warning = getSession().getLabel("MESSAGE_CAISSE_DERNIERE_DEMANDE_MAIL")
                                    + CSCaisse.getEnumFromCodeSystem(dernierRentePont.getSimpleRentePont()
                                            .getCsCaisse());
                        } else if (attestationUniqueMontantZero) {
                            warning = getSession().getLabel("MESSAGE_AUCUN_MONTANT_VERSE_MAIL");
                        }

                        JadeSmtpClient.getInstance().sendMail(
                                mailAd,
                                getSession().getLabel("PDF_ATTESTATIONSFISCALES_RP_D_TITRE_MAIL")
                                        + rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                                                .getDesignation1()
                                        + " "
                                        + rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                                                .getDesignation2() + " - " + CSCaisse.getEnumFromCodeSystem(caisse)
                                        + " " + anneeAttestations,
                                rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                                        .getDesignation1()
                                        + " "
                                        + rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                                                .getDesignation2()
                                        + " : "
                                        + getSession().getLabel("MESSAGE_ABSENCE_ATTESTATION_MAIL") + ". " + warning,
                                new String[] { "" });
                    } else {
                        // La demande n'est pas validée, et il faut atteindre le dossier pour récupérer les infos tiers

                        JadeSmtpClient.getInstance().sendMail(
                                mailAd,
                                getSession().getLabel("PDF_ATTESTATIONSFISCALES_RP_D_TITRE_MAIL")
                                        + dossier.getDemandePrestation().getPersonneEtendue().getTiers()
                                                .getDesignation1()
                                        + " "
                                        + dossier.getDemandePrestation().getPersonneEtendue().getTiers()
                                                .getDesignation2() + " - " + CSCaisse.getEnumFromCodeSystem(caisse)
                                        + " " + anneeAttestations,
                                dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1()
                                        + " "
                                        + dossier.getDemandePrestation().getPersonneEtendue().getTiers()
                                                .getDesignation2() + " : "
                                        + getSession().getLabel("MESSAGE_ABSENCE_ATTESTATION_MAIL") + " - "
                                        + getSession().getLabel("MESSAGE_ETAT_DEMANDE_ATTESTATION_MAIL"),
                                new String[] { "" });

                    }

                } else {
                    // Génération du mail pour tous les tiers propre à une caisse donnée et une année donnée
                    JadeSmtpClient.getInstance().sendMail(
                            mailAd,
                            getSession().getLabel("PDF_ATTESTATIONSFISCALES_RP_D_TITRE_MAIL")
                                    + CSCaisse.getEnumFromCodeSystem(caisse) + " " + anneeAttestations,
                            getSession().getLabel("MESSAGE_ABSENCE_ATTESTATION_MAIL") + " - "
                                    + CSCaisse.getEnumFromCodeSystem(caisse) + " " + anneeAttestations,
                            new String[] { "" });
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            JadeThread.logError(this.getClass().getName(),
                    "Erreur : " + System.getProperty("line.separator") + e.toString());

        }

        Iterator test = JadeLogger.getInstance().logsIterator();
        while (test.hasNext()) {
            Object o = test.next();
            JadeThread.logError(this.getClass().getName(), "/n" + "Erreur technique (jadeLogger : " + o.toString());
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeBusinessMessage[] messages = JadeThread.logMessages();
            for (int i = 0; i < messages.length; i++) {
                getLogSession().addMessage(messages[i]);
            }
            List<String> email = new ArrayList<String>();
            email.add(mailAd);
            this.sendCompletionMail(email);
        } else if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            JadeBusinessMessage[] messages = JadeThread.logMessages();
            for (int i = 0; i < messages.length; i++) {
                getLogSession().addMessage(messages[i]);
            }
            List<String> email = new ArrayList<String>();
            email.add(mailAd);
            this.sendCompletionMail(email);
        }
    }

    public void setAdresseCaisse(String adresseCaisse) {
        this.adresseCaisse = adresseCaisse;
    }

    public void setAnneeAttestations(String anneeAttestations) {
        this.anneeAttestations = anneeAttestations;
    }

    public void setAttestationUnique(boolean isAttestationUnique) {
        this.isAttestationUnique = isAttestationUnique;
    }

    public void setCaisse(String caisse) {
        this.caisse = caisse;
    }

    public void setCodePostaleCaisse(String codePostaleCaisse) {
        this.codePostaleCaisse = codePostaleCaisse;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIsSendToGed(String isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setLocaliteCaisse(String localiteCaisse) {
        this.localiteCaisse = localiteCaisse;
    }

    public void setMailAd(String mailAd) {
        this.mailAd = mailAd;
    }

    public void setNomCaisse(String nomCaisse) {
        this.nomCaisse = nomCaisse;
    }

    public void setNomPersonnecontactCaisse(String nomPersonnecontactCaisse) {
        this.nomPersonnecontactCaisse = nomPersonnecontactCaisse;
    }

    public void setPrenomPersonneContactCaisse(String prenomPersonneContactCaisse) {
        this.prenomPersonneContactCaisse = prenomPersonneContactCaisse;
    }

    public void setTelPersonneContactCaisse(String telPersonneContactCaisse) {
        this.telPersonneContactCaisse = telPersonneContactCaisse;
    }

}
