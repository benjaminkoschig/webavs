package ch.globaz.perseus.businessimpl.services.document;

import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.utils.PFUserHelper;
import globaz.pyxis.constantes.IConstantes;
import java.util.Hashtable;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

public class AbstractDocumentBuilder {

    public static final String PUBINFO_DOC_DESTINATION = "5";
    public static final String PUBINFO_DOC_DESTINATION_ATTESTATION_FISCALE = "2";
    public static final String PUBINFO_DOC_DESTINATION_RECTOVERSO = "6";
    public static final String PUBINFO_GED = "1";
    public static final String PUBINFO_GED_RECTOVERSO = "9";
    public static final String PUBINFO_LETTRE_ENTETE_SANS_GED = "7";
    public static final String PUBINFO_LETTRE_ENTETE_SANS_GED_SANS_RECTOVERSO = "8";
    public static final String PUBINFO_SANS_GED = "3";
    public static final String PUBINFO_SANS_GED_RECTOVERSO = "4";
    private BabelContainer babelContainer = new BabelContainer();
    private Hashtable<String, JadePublishDocumentInfo> ConteneurPubInfos = new Hashtable<String, JadePublishDocumentInfo>();
    protected JadeUser gestionnaire = null;
    protected boolean isSendToGed = false;

    public AbstractDocumentBuilder() {
        babelContainer.RegisterCtx(IPFCatalogueTextes.CS_DOCUMENT);
    }

    protected DocumentData buildBasDePageFacture(DocumentData data, String agenceAssurance, boolean copieAgence)
            throws Exception {

        try {
            AdministrationComplexModel AgenceSociale = TIBusinessServiceLocator.getAdministrationService().read(
                    agenceAssurance.toString());

            // Insertion du titre copie, si ce n'est pas l'agence de lausanne
            if (!IPFConstantes.ID_AGENCE_LAUSANNE.equals(agenceAssurance)) {
                data.addData("Copie", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 6, 5));
            }

            String texteCopie = "";

            // Copie pour l'agence sociale
            if (copieAgence == true) {
                if (!IPFConstantes.ID_AGENCE_LAUSANNE.equals(agenceAssurance)) {
                    texteCopie = AgenceSociale.getTiers().getDesignation1() + " "
                            + AgenceSociale.getTiers().getDesignation2();
                } else {
                    if (CSCaisse.CCVD.getCodeSystem().equals(agenceAssurance)) {
                        texteCopie = AgenceSociale.getTiers().getDesignation1() + " "
                                + AgenceSociale.getTiers().getDesignation2();
                    }
                }
            }

            data.addData("TexteCopie", texteCopie);

            // Insertion du titre et du texte pour l'annexe
            data.addData("Annexe", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 6, 1));
            String annexes = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 6, 6);

            data.addData("TexteAnnexe", annexes);

            return data;

        } catch (Exception e) {
            throw new DecisionException("AbstractDecisionBuilder - erreur dans le buildBasDePage :" + e.toString(), e);
        }

    }

    protected DocumentData buildBasDePageFactureRP(DocumentData data, String agenceAssurance, boolean copieAgence)
            throws Exception {

        try {

            // Insertion du titre copie, si ce n'est pas l'agence de lausanne
            if (!IPFConstantes.ID_AGENCE_LAUSANNE.equals(agenceAssurance)) {
                data.addData("Copie", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 6, 5));
            }

            // Copie pour l'agence sociale
            if (copieAgence == true) {
                if (!IPFConstantes.ID_AGENCE_LAUSANNE.equals(agenceAssurance)) {
                    // Ajout du texte assurance sociales texte générique car on ne connait pas l'agence dans les RP
                    data.addData("TexteCopie",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 12, 2));
                } else {
                    if (CSCaisse.CCVD.getCodeSystem().equals(agenceAssurance)) {
                        // Ajout du texte assurance sociales texte générique car on ne connait pas l'agence dans les RP
                        data.addData("TexteCopie",
                                getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 12, 2));
                    }
                }
            }

            // Insertion du titre et du texte pour l'annexe
            data.addData("Annexe", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 6, 1));
            String annexes = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 6, 6);

            data.addData("TexteAnnexe", annexes);

            return data;

        } catch (Exception e) {
            throw new DecisionException("AbstractDecisionBuilder / buildBasDePageFactureRP :" + e.toString(), e);
        }

    }

    protected DocumentData buildHeader(DocumentData data, boolean isCopie, boolean isLettreEnTete,
            String idTiersAdresseCourrier, String idGestionnaire, String csCaisse, String DateJJMMAAAA,
            String titreDocument, boolean isSignatureSpecifique) throws Exception {
        try {

            if (!JadeStringUtil.isEmpty(idGestionnaire)) {
                // Chargement du gestionnaire
                gestionnaire = getSession().getApplication()._getSecurityManager()
                        .getUserForVisa(getSession(), idGestionnaire);
            }

            // Renseigne le titre de la caisse si c'est l'agence de Lausanne
            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(csCaisse)) {
                data.addData("TITRE_CAISSE", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 1, 4));

                // Renseigne le numero de telephone uniquement si c'est l'agence de Lausanne
                // Renseigne le numero du gestionnaire si il y en a un
                if (null != gestionnaire) {
                    String telGestionnaire = gestionnaire.getPhone();
                    if (!JadeStringUtil.isEmpty(telGestionnaire)) {
                        data.addData("TELEPHONE", (getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 1, 3))
                                + " " + (telGestionnaire));
                    } else {
                        // Sinon renseigne le numero de la centrale
                        data.addData("TELEPHONE", (getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 1, 3))
                                + " " + (getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 1, 5)));
                    }
                } else {
                    // Sinon renseigne le numero de la centrale
                    data.addData("TELEPHONE", (getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 1, 3))
                            + " " + (getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 1, 5)));
                }

                // Renseigne le numero de fax si c'est l'agence de Lausanne
                data.addData("FAX", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 1, 6));

                // Renseigne l'adress e-mail si c'est l'agence de Lausanne
                data.addData("E_MAIL", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 1, 7));
            }

            // Renseigner le gestionnaire, nom prénom
            if (null != gestionnaire) {
                String gestionnaire = this.gestionnaire.getLastname() + " " + this.gestionnaire.getFirstname();
                if (JadeStringUtil.isEmpty(gestionnaire)) {
                    throw new DecisionException("AbstractDocumentBuilder - Pas de nom de gestionnaire pour ce dossier");
                } else {
                    data.addData("NOM_COLLABORATEUR", getBabelContainer()
                            .getTexte(IPFCatalogueTextes.CS_DOCUMENT, 1, 1) + " " + gestionnaire);
                }
            }

            // Renseigner l'adresse
            String adresse = getAdresse(idTiersAdresseCourrier);
            PersonneEtendueComplexModel personne = TIBusinessServiceLocator.getPersonneEtendueService().read(
                    idTiersAdresseCourrier);
            String nom = personne.getTiers().getDesignation1() + " " + personne.getTiers().getDesignation2();
            String nss = personne.getPersonneEtendue().getNumAvsActuel();

            if (JadeStringUtil.isEmpty(adresse)) {
                throw new DecisionException("Pas d'adresse de courrier pour le tiers n° :" + nss + " - " + nom);
            } else {
                data.addData("ADRESSE", adresse);
            }

            // renseigner lieu et date
            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(csCaisse)) {
                data.addData(
                        "DATE_ET_LIEU",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 2, 2)/* "Lausanne, le " */
                                + " "
                                + JACalendar.format(DateJJMMAAAA,
                                        getBabelContainer().getCodeIsoLangue(IPFCatalogueTextes.CS_DOCUMENT)));
                data.addData("idEntete", "AGLAU");
                data.addData("idSignature", "AGLAU");
            } else if (CSCaisse.CCVD.getCodeSystem().equals(csCaisse)) {
                data.addData(
                        "DATE_ET_LIEU",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 2, 1)/* "Clarens, le " */
                                + " "
                                + JACalendar.format(DateJJMMAAAA,
                                        getBabelContainer().getCodeIsoLangue(IPFCatalogueTextes.CS_DOCUMENT)));
                data.addData("idEntete", "CCVD");
                data.addData("idSignature", "CCVD");
                data.addData("TITRE", titreDocument);
            } else {
                // Génération d'erreur si pas de caisse
                throw new DecisionException("AbstractDocumentBuilder - Pas de caisse dans la demande");
            }

            // Renseigner si copie
            if (isCopie) {
                data.addData("IS_COPIE", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 1, 2));
            }

            // Insertion de la signature
            if (CSCaisse.CCVD.getCodeSystem().equals(csCaisse)) {
                data.addData("SIGNATURE_NOM_CAISSE", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 4, 1));
            } else if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(csCaisse)) {
                // Le nom de signature change pour la lettre d'en-tete quand ca vient de l'agence de Lausanne
                if (isLettreEnTete) {
                    // Affiche : "Agence communale d'assurances sociales"
                    data.addData("SIGNATURE_NOM_CAISSE",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 4, 3));
                } else if (isSignatureSpecifique) {
                    // Affiche : "Agence d'assurances sociales Lausanne"
                    data.addData("SIGNATURE_NOM_CAISSE",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 4, 6));
                } else {
                    // Affiche : "Le chef de l'agence"
                    data.addData("SIGNATURE_NOM_CAISSE",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 4, 5));
                }
            }
            return data;
        } catch (Exception e) {
            throw new DecisionException("AbstractDocumentBuilder / buildHeader :  " + e);
        }
    }

    // Methode utilisée à travers les différents documents afin de modifier les montants si
    // ceux-ci n'ont pas de centimes.
    protected String convertCentimes(String montant) throws Exception {
        try {
            FWCurrency currency = new FWCurrency(montant);
            montant = currency.toStringFormat();
            if (montant.endsWith(".00")) {
                montant = montant.replace(".00", ".-");
            } else if (montant.endsWith(".0")) {
                montant = montant.replace(".0", ".-");
            } else if (!montant.endsWith(".0")) {
                montant = montant;
            } else if (!montant.endsWith(".00")) {
                montant = montant;
            } else {
                throw new DecisionException("AbstractDocumentBuilder - Erreur dans le montant");
            }
            return montant;
        } catch (Exception e) {
            throw new DecisionException("AbstractDocumentBuilder / convertCentimes : " + ", Détail de l'erreur : "
                    + e.toString(), e);
        }

    }

    protected void createJadePublishDocInfo(String dateDoc, String adMail, boolean isSendToGed, String idTiers,
            String titreMail) throws Exception {

        JadePublishDocumentInfo pubInfos = new JadePublishDocumentInfo();

        pubInfos.setDocumentTitle(titreMail);
        pubInfos.setDocumentSubject(titreMail);
        pubInfos.setOwnerEmail(adMail);
        pubInfos.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, adMail);
        pubInfos.setArchiveDocument(isSendToGed);
        pubInfos.setPublishDocument(false);
        pubInfos.setDocumentDate(dateDoc);
        TIDocumentInfoHelper.fill(pubInfos, idTiers, getSession(), null, null, null);

        getConteneurPubInfos().put(AbstractDocumentBuilder.PUBINFO_GED, pubInfos);

        JadePublishDocumentInfo pubinfos_sans_ged = pubInfos.createCopy();
        pubinfos_sans_ged.setArchiveDocument(false);
        getConteneurPubInfos().put(AbstractDocumentBuilder.PUBINFO_SANS_GED, pubinfos_sans_ged);

        JadePublishDocumentInfo pubinfos_sans_ged_recoverso = pubInfos.createCopy();
        pubinfos_sans_ged_recoverso.setDuplex(true);
        pubinfos_sans_ged_recoverso.setDuplexRule(JadePublishDocumentInfo.DUPLEX_ON_FIRST);
        pubinfos_sans_ged_recoverso.setArchiveDocument(false);
        pubinfos_sans_ged_recoverso.setPublishDocument(false);
        getConteneurPubInfos().put(AbstractDocumentBuilder.PUBINFO_SANS_GED_RECTOVERSO, pubinfos_sans_ged_recoverso);

        JadePublishDocumentInfo pubinfos_attestations = pubInfos.createCopy();
        pubinfos_attestations.setPublishDocument(true);
        pubinfos_attestations.setArchiveDocument(false);
        getConteneurPubInfos().put(AbstractDocumentBuilder.PUBINFO_DOC_DESTINATION_ATTESTATION_FISCALE,
                pubinfos_attestations);

        JadePublishDocumentInfo pubinfos_doc_destination = pubInfos.createCopy();
        pubinfos_doc_destination.setPublishDocument(true);
        pubinfos_doc_destination.setArchiveDocument(false);
        // pubinfos5.setDuplex(true);
        getConteneurPubInfos().put(AbstractDocumentBuilder.PUBINFO_DOC_DESTINATION, pubinfos_doc_destination);

        JadePublishDocumentInfo pubinfos_doc_destination_rectoverso = pubInfos.createCopy();
        pubinfos_doc_destination_rectoverso.setPublishDocument(true);
        pubinfos_doc_destination_rectoverso.setArchiveDocument(false);
        pubinfos_doc_destination_rectoverso.setDuplex(true);
        pubinfos_doc_destination_rectoverso.setDuplexRule(JadePublishDocumentInfo.DUPLEX_ALL);
        getConteneurPubInfos().put(AbstractDocumentBuilder.PUBINFO_DOC_DESTINATION_RECTOVERSO,
                pubinfos_doc_destination_rectoverso);

        JadePublishDocumentInfo pubInfos_lettre_entete_sans_ged = pubInfos.createCopy();
        pubInfos_lettre_entete_sans_ged.setPublishDocument(false);
        pubInfos_lettre_entete_sans_ged.setArchiveDocument(false);
        pubInfos_lettre_entete_sans_ged.setDuplex(true);
        pubInfos_lettre_entete_sans_ged.setDuplexRule(JadePublishDocumentInfo.DUPLEX_ALL);
        pubInfos_lettre_entete_sans_ged.setDocumentType(IPRConstantesExternes.PCF_LETTRE_EN_TETE);
        pubInfos_lettre_entete_sans_ged.setDocumentTypeNumber(IPRConstantesExternes.PCF_LETTRE_EN_TETE);
        getConteneurPubInfos().put(AbstractDocumentBuilder.PUBINFO_LETTRE_ENTETE_SANS_GED,
                pubInfos_lettre_entete_sans_ged);

        JadePublishDocumentInfo pubInfos_lettre_entete_sans_ged_sans_rectoverso = pubInfos.createCopy();
        pubInfos_lettre_entete_sans_ged_sans_rectoverso.setPublishDocument(false);
        pubInfos_lettre_entete_sans_ged_sans_rectoverso.setArchiveDocument(false);
        pubInfos_lettre_entete_sans_ged_sans_rectoverso.setDocumentType(IPRConstantesExternes.PCF_LETTRE_EN_TETE);
        pubInfos_lettre_entete_sans_ged_sans_rectoverso.setDocumentTypeNumber(IPRConstantesExternes.PCF_LETTRE_EN_TETE);
        getConteneurPubInfos().put(AbstractDocumentBuilder.PUBINFO_LETTRE_ENTETE_SANS_GED_SANS_RECTOVERSO,
                pubInfos_lettre_entete_sans_ged_sans_rectoverso);

        JadePublishDocumentInfo pubInfos_ged_rectoverso = pubInfos.createCopy();
        pubInfos_ged_rectoverso.setPublishDocument(false);
        pubInfos_ged_rectoverso.setArchiveDocument(true);
        pubInfos_ged_rectoverso.setDuplex(true);
        pubInfos_ged_rectoverso.setDuplexRule(JadePublishDocumentInfo.DUPLEX_ON_FIRST);
        getConteneurPubInfos().put(AbstractDocumentBuilder.PUBINFO_GED_RECTOVERSO, pubInfos_ged_rectoverso);

    }

    public JadePrintDocumentContainer dataAndPubInfo(JadePrintDocumentContainer container, DocumentData data,
            boolean isCopie, boolean isGed, String numeroDocument) {

        JadePublishDocumentInfo jadePublishDocInfo;
        if (isCopie) {
            jadePublishDocInfo = getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_SANS_GED);
        } else {
            if (isGed) {
                jadePublishDocInfo = getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_GED);
            } else {
                jadePublishDocInfo = getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_SANS_GED);
            }

        }

        jadePublishDocInfo.setDocumentType(numeroDocument);
        jadePublishDocInfo.setDocumentTypeNumber(numeroDocument);
        container.addDocument(data, jadePublishDocInfo);
        return container;
    }

    public JadePrintDocumentContainer dataAndPubInfoRecoVerso(JadePrintDocumentContainer container, DocumentData data,
            boolean isCopie, boolean isGed, String numeroDocument, boolean rectoVerso) {

        JadePublishDocumentInfo jadePublishDocInfo;
        if (isCopie) {
            if (rectoVerso) {
                jadePublishDocInfo = getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_SANS_GED_RECTOVERSO);
            } else {
                jadePublishDocInfo = getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_SANS_GED);
            }

        } else {
            if (isGed) {
                if (rectoVerso) {
                    jadePublishDocInfo = getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_GED_RECTOVERSO);
                } else {
                    jadePublishDocInfo = getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_GED);
                }

            } else {
                if (rectoVerso) {
                    jadePublishDocInfo = getConteneurPubInfos()
                            .get(AbstractDocumentBuilder.PUBINFO_SANS_GED_RECTOVERSO);
                } else {
                    jadePublishDocInfo = getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_SANS_GED);
                }

            }

        }

        jadePublishDocInfo.setDocumentType(numeroDocument);
        jadePublishDocInfo.setDocumentTypeNumber(numeroDocument);
        container.addDocument(data, jadePublishDocInfo);
        return container;
    }

    protected String getAdresse(String idTiers) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        if (!JadeStringUtil.isEmpty(idTiers)) {
            AdresseTiersDetail detailTiers = PFUserHelper.getAdresseAssure(idTiers,
                    IConstantes.CS_AVOIR_ADRESSE_COURRIER, JACalendar.todayJJsMMsAAAA());

            return detailTiers.getAdresseFormate();
        } else {
            return null;
        }

    }

    public BabelContainer getBabelContainer() {
        return babelContainer;
    }

    public Hashtable<String, JadePublishDocumentInfo> getConteneurPubInfos() {
        return ConteneurPubInfos;
    }

    // Methode permettant de retourner le numero NSS du tiers
    protected String getNssNomTiers(Dossier dossier) {
        String nssNom = dossier.getDemandePrestation().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel()
                + " - " + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1() + " "
                + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation2();
        return nssNom;
    }

    /**
     * Retourne la session
     * 
     * @return
     */
    protected BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

    protected String getSujetMailFacture(String idDossier) throws Exception {
        String sujet = "";
        Dossier dossier = PerseusServiceLocator.getDossierService().read(idDossier);

        sujet = dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1() + " "
                + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation2();
        return sujet;
    }

    public boolean isSendToGed() {
        return isSendToGed;
    }

    public void loadEntity(String catTexte, String langue) throws Exception {
        getBabelContainer().RegisterCtx(catTexte);

        getBabelContainer().setCodeIsoLangue(getSession().getCode(langue));

        getBabelContainer().load();

    }

    public void setConteneurPubInfos(Hashtable<String, JadePublishDocumentInfo> conteneurPubInfos) {
        ConteneurPubInfos = conteneurPubInfos;
    }

    public void setSendToGed(boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }
}
