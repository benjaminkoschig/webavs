package ch.globaz.pegasus.businessimpl.services.demande;

import globaz.babel.api.ICTDocument;
import globaz.externe.IPRConstantesExternes;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.db.tiers.TITiers;
import java.text.MessageFormat;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import ch.globaz.babel.business.exception.CatalogueTexteException;
import ch.globaz.babel.business.services.BabelServiceLocator;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.constantes.IPCCatalogueTextes;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.services.demande.DemandeBuilder;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.util.PegasusPubInfoBuilder;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.topaz.datajuicer.DocumentData;

public class SingleBillagBuilder extends AbstractDemandeBuilder implements DemandeBuilder {

    private static final String CR = "\n";
    private static final String CR_FROM_BABEL = "{br}";
    private final static String DATE_DEBUT = "{date_debut}";
    private static final String DEUX_POINT = ":";
    private static final String SPACE = " ";
    private static final String TITRE_REPLACE = "{titre}";
    private static final String VIRGULE = ",";
    private ICTDocument babelDoc = null;
    private String dateDebutPc = null;
    private String dateDoc = null;
    private SimpleDemande demande = new SimpleDemande();
    private TITiers tiersDestinataire = null;
    private String idTiers = null;
    private String NSS = "";
    private String gestionnaire = null;

    @Override
    public JadePrintDocumentContainer buildBillag(String idDemande, String dateDoc, String dateDebut, String mailGest,
            String nss, String idTiers, String gestionnaire) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        // objet retourné
        JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

        setFields(dateDoc, dateDebut, nss, idTiers, gestionnaire);
        // chargement des entités
        loadDBEntity(idDemande);

        String mailTitle = getDocumentMailTitle();

        allDoc.addDocument(buildBillagDoc(this.dateDoc, babelDoc, null, NSS, this.idTiers, this.gestionnaire), null);

        // PubInfos
        JadePublishDocumentInfo pubInfos = new PegasusPubInfoBuilder().publish().getPubInfo();
        pubInfos.setDocumentTitle(mailTitle);
        pubInfos.setOwnerEmail(mailGest);
        pubInfos.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, mailGest);

        pubInfos.setDocumentType(IPRConstantesExternes.PC_REF_INFOROM_DEMANDE_ATTESTATION_BILLAG);
        pubInfos.setDocumentTypeNumber(IPRConstantesExternes.PC_REF_INFOROM_DEMANDE_ATTESTATION_BILLAG);
        pubInfos.setDocumentDate(dateDoc);
        pubInfos.setDocumentSubject(mailTitle);

        /********************* Fusion *****************/
        // on fusionne le doc original ert les copies
        allDoc.setMergedDocDestination(pubInfos);
        // on retourne le container
        return allDoc;

    }

    /**
     * 
     * @param dateDoc
     *            date sur le document
     * @param babelDoc
     *            catalogue de texte pour le document
     * @param dateDebutPc
     *            date de ébut de la pc
     * @param NSS
     *            nss de la personne
     * @param idTiers
     *            l'idTiers de la personne
     * @return une instance de DocumentData
     * @throws Exception
     *             exception succeptible d'être levée
     */
    public DocumentData buildBillagDoc(String dateDoc, ICTDocument babelDoc, String dateDebutPc, String NSS,
            String idTiers, String gestionnaire) throws Exception {

        this.babelDoc = babelDoc;
        this.NSS = NSS;
        this.idTiers = idTiers;
        this.gestionnaire = gestionnaire;

        DocumentData data = new DocumentData();
        String prop = getSession().getApplication().getProperty(IPCDecision.DESTINATAIRE_REDEVANCE);
        if("BILLAG".equalsIgnoreCase(prop)) {
            data.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_DECISION_BILLAG);
        } else {
            data.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_DECISION_REDEVANCE);
        }

        // date debut pc non setter cas de billag par dac
        if (this.dateDebutPc == null) {
            this.dateDebutPc = "01." + dateDebutPc;
        }
        // header
        data = buildHeader(data, dateDoc);
        // data = this.buildHeader(data, false);
        // Attestations
        data.addData("TITRE_BILLAG", this.babelDoc.getTextes(18).getTexte(10).getDescription());
        data.addData("NSS_BILLAG", NSS);
        data.addData("POLITESSE_BILLAG",
                new StringBuilder(addCommaIFFrench(getTitreTiers(), getTiersFromIdTiers().getLangue())).toString());
        data.addData("PAR1_BILLAG", this.babelDoc.getTextes(18).getTexte(30).getDescription());
        data.addData("PAR2_BILLAG", PRStringUtils.replaceString(this.babelDoc.getTextes(18).getTexte(40)
                .getDescription(), SingleBillagBuilder.DATE_DEBUT, this.dateDebutPc));
        data.addData("PAR3_BILLAG", this.babelDoc.getTextes(18).getTexte(50).getDescription());
        data.addData("PAR4_BILLAG", this.babelDoc.getTextes(18).getTexte(60).getDescription());
        data.addData(
                "SALUTATIONS_BILLAG",
                this.babelDoc.getTextes(18).getTexte(70).getDescription()
                        .replace(SingleBillagBuilder.TITRE_REPLACE, getTitreTiers()));

        buildBlocSignatures(data);

        data.addData("TOURNEZ_BILLAG", this.babelDoc.getTextes(18).getTexte(80).getDescription());

        data.addData("ADRESSE_FORM", this.babelDoc.getTextes(18).getTexte(100).getDescription());
        // Formulaire
        data.addData("TITRE_FORM_BILLAG", this.babelDoc.getTextes(18).getTexte(110).getDescription());
        data.addData("NOM_FORM_BILLAG", this.babelDoc.getTextes(18).getTexte(120).getDescription());
        data.addData("PRENOM_FORM_BILLAG", this.babelDoc.getTextes(18).getTexte(121).getDescription());
        data.addData("NAISS_FORM_BILLAG", this.babelDoc.getTextes(18).getTexte(122).getDescription());
        data.addData("RUE_NO_FORM_BILLAG", this.babelDoc.getTextes(18).getTexte(123).getDescription());
        data.addData("NPA_LIEU_FORM_BILLAG", this.babelDoc.getTextes(18).getTexte(124).getDescription());
        data.addData("TEL_FORM_BILLAG", this.babelDoc.getTextes(18).getTexte(125).getDescription());
        data.addData("CHK1_FORM_BILLAG", this.babelDoc.getTextes(18).getTexte(126).getDescription());

        data.addData("NOCLIENT_FORM_BILLAG", this.babelDoc.getTextes(18).getTexte(127).getDescription());

        data.addData("DEJA_CLIENT_FORM_BILLAG", this.babelDoc.getTextes(18).getTexte(128).getDescription());

        data.addData("PAS_CLIENT_FORM_BILLAG", this.babelDoc.getTextes(18).getTexte(129).getDescription());

        data.addData("DATE_FORM_BILLAG", this.babelDoc.getTextes(18).getTexte(130).getDescription());
        data.addData("SIGN_FORM_BILLAG", this.babelDoc.getTextes(18).getTexte(131).getDescription());

        return data;

    }

    private DocumentData buildBlocSignatures(DocumentData data) {
        data.addData("signature", "STANDARD");
        // Ajout d'un caraige return si description caisse sur deux lignes
        data.addData("SIGNATURE_NOM_CAISSE", PRStringUtils.replaceString(babelDoc.getTextes(19).getTexte(2)
                .getDescription(), SingleBillagBuilder.CR_FROM_BABEL, SingleBillagBuilder.CR));

        data.addData("SIGNATAIRE", babelDoc.getTextes(19).getTexte(3).getDescription());
        data.addData("SIGNATURE_NOM_SERVICE", babelDoc.getTextes(19).getTexte(4).getDescription());

        data.addData("SIGNATURE_GESTIONNAIRE", getUserNomFormatte());
        return data;
    }

    /**
     * Construction du header
     * 
     * @param data
     * @return data, l'objet alimenté avec les infos du header
     * @throws Exception
     */
    private DocumentData buildHeader(DocumentData data, String dateDoc) throws Exception {

        String nomCollabo = "";
        String tel = "";

        JadeUser user = getSession().getApplication()._getSecurityManager().getUserForVisa(getSession(), gestionnaire);

        if (user != null) {
            // Prépartion des données
            nomCollabo = user.getFirstname() + " " + user.getLastname();
            tel = "Tél: " + user.getPhone();//
                                            // ).this.decisionRefus.getDecisionHeader().getSimpleDecisionHeader().getPreparationPar());
            // Infos collaborateur
            data.addData("PERSONNE_REF", babelDoc.getTextes(1).getTexte(2).getDescription());
            data.addData("NOM_COLLABO", nomCollabo);
            data.addData("TEL_COLLABO", tel);
            // Références

            data.addData("NREF", babelDoc.getTextes(1).getTexte(3).getDescription() + " " + user.getVisa());
            data.addData("VREF", babelDoc.getTextes(1).getTexte(4).getDescription() + " " + NSS);

            data.addData("TEL_GESTIONNAIRE", user.getPhone());
            data.addData("GESTIONNAIRE", user.getFirstname() + " " + user.getLastname());
            data.addData("ID_USER", user.getIdUser());
            data.addData("NSS_BENEFICIAIRE", NSS);
        }
        data.addData("header", "STANDARD");
        data.addData("ADRESSE",
                PRTiersHelper.getAdresseCourrierFormatee(getSession(), idTiers, "", AdresseService.CS_TYPE_COURRIER));

        if (JadeStringUtil.isBlank(this.dateDoc)) {
            this.dateDoc = dateDoc;
        }
        data.addData(
                "DATE_ET_LIEU",
                babelDoc.getTextes(1).getTexte(1).getDescription() + " "
                        + PegasusDateUtil.getLitteralDateByTiersLanguage(dateDoc, getTiersFromIdTiers().getLangue()));

        // Libelle et valeur

        return data;
    }

    private String getDocumentMailTitle() throws Exception {
        String prop = getSession().getApplication().getProperty(IPCDecision.DESTINATAIRE_REDEVANCE);
        return new StringBuilder(MessageFormat.format(getSession().getLabel("TOPAZ_BILLAG_DEMANDE_NO"), prop)).append(SingleBillagBuilder.SPACE)
                .append(SingleBillagBuilder.DEUX_POINT).append(demande.getIdDemande()).toString();
    }

    /**
     * Retourne le titre de la personne. Si pas trouvé (null ou vide), on retourne le titre défini dans babel
     * 
     * @return le titre de la personne si trouvé dans le tiers
     * @throws Exception
     *             exception succeptible d'être levée
     */
    private String getTitreTiers() throws Exception {
        TITiers tiers = getTiersFromIdTiers();
        String titreTiers = null;
        if (tiers != null) {
            titreTiers = tiers.getFormulePolitesse(LanguageResolver.resolveCodeSystemFromLanguage(tiers.getLangue()));
        }
        if ((null == titreTiers) || StringUtils.isEmpty(titreTiers)) {
            titreTiers = babelDoc.getTextes(18).getTexte(20).getDescription();
        }
        return titreTiers;
    }

    /**
     * Cette méthode permet de récupérer un tiers par son id.
     * Si le tiers est déjà instancié, on retourne le tiers existant.
     * Sinon on créer l'objet tiers et on le retourne.
     * 
     * @return un tiers (TITiers)
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    private TITiers getTiersFromIdTiers() throws JadePersistenceException, JadeApplicationException {
        if (tiersDestinataire != null) {
            return tiersDestinataire;
        } else {
            TITiers tiers = new TITiers();
            tiers.setId(idTiers);
            tiers.setSession(getSession());
            try {
                tiers.retrieve();
                return tiers;
            } catch (Exception e) {
                throw new JadePersistenceException("an error happened while loading the tiers with the following id : "
                        + idTiers, e);
            }
        }
    }

    /**
     * Chargement des entités de bases de données. Decision, Babel, et TupleDonneesRapport
     */
    private void loadDBEntity(String idDemande) throws CatalogueTexteException, Exception {

        demande = PegasusImplServiceLocator.getSimpleDemandeService().read(idDemande);
        // liste pcal

        // Chargement BAbel
        // on charge le tiers destinataire
        Langues langueTiers = LanguageResolver.resolveISOCode(getTiersFromIdTiers().getLangue());
        Map<Langues, CTDocumentImpl> documentsBabel;
        documentsBabel = BabelServiceLocator.getPCCatalogueTexteService().searchForTypeDecision(
                IPCCatalogueTextes.BABEL_DOC_NAME_APRES_CALCUL);
        babelDoc = documentsBabel.get(langueTiers);

    }

    private void setFields(String dateDoc, String dateDebut, String nss, String idTiers, String gestionnaire) {
        NSS = nss;
        this.idTiers = idTiers;
        this.dateDoc = dateDoc;
        dateDebutPc = dateDebut;
        this.gestionnaire = gestionnaire;
    }

    private String addCommaIFFrench(String formulePolitesse, String codeIsoLangue) {
        Langues langue = LanguageResolver.resolveISOCode(codeIsoLangue);
        if (Langues.Francais.equals(langue)) {
            return formulePolitesse + ",";
        } else {
            return formulePolitesse;
        }
    }

}
