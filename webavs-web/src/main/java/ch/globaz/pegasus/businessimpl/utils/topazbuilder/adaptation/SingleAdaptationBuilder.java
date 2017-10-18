package ch.globaz.pegasus.businessimpl.utils.topazbuilder.adaptation;

import globaz.babel.api.ICTDocument;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSessionUtil;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.db.tiers.TITiers;
import java.util.Map;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.common.codesystem.CodeSystem;
import ch.globaz.common.codesystem.CodeSystemUtils;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.constantes.IPCCatalogueTextes;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.AbstractPegasusBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier.SingleTransfertPCAbstractBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.util.PegasusPubInfoBuilder;
import ch.globaz.pegasus.process.adaptation.imprimerDecisions.CommunicationAdaptationElement;
import ch.globaz.pegasus.process.adaptation.imprimerDecisions.CommunicationAdaptationElement.ResumePrestations;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

public class SingleAdaptationBuilder extends AbstractPegasusBuilder {

    private static final String CHAMP_PERSONNE_REF = "PERSONNE_REF";
    private static final String CR = "\n";
    private static final String CR_FROM_BABEL = "{br}";
    private int annee;
    private ICTDocument babelDoc;
    private String dateGeneration;
    private String dateSurDoc = null;

    private CommunicationAdaptationElement source;

    private void addLignePlanCalcul(Collection table, String suffix, String designation, String montant) {
        DataList line = new DataList("ligne" + suffix);
        line.addData("DESIGNATION_" + suffix, designation);
        line.addData("MONTANT_" + suffix, new FWCurrency(montant).toStringFormat());
        table.add(line);
    }

    /**
     * Chargement du container de transfert pour le remplissage de pixis
     * 
     * @param idTiers
     *            l'identifiant du tiers concerné pour les informations
     * @throws DecisionException
     *             si un problème survient lors du remplissge
     */
    private void loadPixisInfoForPubInfo(String idTiers, JadePublishDocumentInfo pubInfo) throws DecisionException {
        try {
            TIDocumentInfoHelper.fill(pubInfo, idTiers, getSession(), null, null, null);
        } catch (Exception e) {
            throw new DecisionException(
                    "An error happening during filling the document with pyxis informations for this idTiers:["
                            + idTiers + "]", e);
        }
    }

    private TITiers loadTiers(String idTiers) throws Exception {

        if (idTiers == null) {
            throw new CommonTechnicalException("the idTiers can't be null");
        }

        TITiers tiersToReturn = new TITiers();
        tiersToReturn.setId(idTiers);
        tiersToReturn.setSession(getSession());
        tiersToReturn.retrieve();

        return tiersToReturn;
    }

    public JadePrintDocumentContainer build(Map<Langues, CTDocumentImpl> documentsBabel,
            JadePrintDocumentContainer allDoc, CommunicationAdaptationElement source, String dateSurDoc)
            throws TransfertDossierException {

        this.source = source;
        dateGeneration = source.getDateValidite();
        annee = Integer.parseInt(dateGeneration.substring(6));
        this.dateSurDoc = dateSurDoc;

        try {

            TITiers tiersBeneficiaire = loadTiers(source.getIdTiersAyantDroit());
            babelDoc = documentsBabel.get(LanguageResolver.resolveISOCode(tiersBeneficiaire.getLangue()));

            // prepare pubInfos
            JadePublishDocumentInfo pubInfo = new PegasusPubInfoBuilder().ged().rectoVersoFirst().getPubInfo();

            loadPixisInfoForPubInfo(this.source.getIdTiersAyantDroit(), pubInfo);

            pubInfo.setDocumentType(IPRConstantesExternes.PC_REF_INFOROM_COMMUNICATION_ADAPTATION);
            pubInfo.setDocumentTypeNumber(IPRConstantesExternes.PC_REF_INFOROM_COMMUNICATION_ADAPTATION);

            DocumentData mainPage = buildMainPage(tiersBeneficiaire);

            mainPage.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_COMMUNICATION_ADAPTATION);
            allDoc.addDocument(mainPage, pubInfo);

            return allDoc;
        } catch (Exception e) {
            throw new TransfertDossierException("An error happened while building the document!", e);
        }
    }

    private void buildContent(DocumentData data, TITiers tiersBeneficiaire) {

        data.addData("LABEL_NSS", babelDoc.getTextes(1).getTexte(6).getDescription());
        data.addData("NSS", source.getNssAyantDroit());
        data.addData("LABEL_AYANT_DROIT", babelDoc.getTextes(1).getTexte(7).getDescription());
        data.addData("NOM_AYANT_DROIT", source.getNomAyantDroit());

        data.addData("TITRE",
                babelDoc.getTextes(1).getTexte(1).getDescription().replace("{DATE_GENERATION}", dateGeneration));
        String formulePolitesse = "";
        String tiersLangue = LanguageResolver.resolveCodeSystemFromLanguage(tiersBeneficiaire.getLangue());
        String tiersLangueIso = tiersBeneficiaire.getLangueIso();

        try {
            formulePolitesse = tiersBeneficiaire.getFormulePolitesse(tiersLangue);
        } catch (Exception e) {
            throw new CommonTechnicalException(
                    "An exception happened while trying to load the civility of the following tiers : ["
                            + tiersBeneficiaire.getIdTiers() + "]", e);
        }

        data.addData("TITRE_TIERS", formulePolitesse + ",");

        data.addData("CONTENU0",
                babelDoc.getTextes(1).getTexte(2).getDescription().replace("{DATE_OCTROI}", dateGeneration));
        // resume prestations
        // champs d'entete
        data.addData("GENRE", babelDoc.getTextes(2).getTexte(1).getDescription());
        data.addData("ANNEE_PRECEDENTE", String.valueOf(annee - 1));
        data.addData("ANNEE", String.valueOf(annee));

        data.addData("LABEL_TOTAL_MENSUEL", babelDoc.getTextes(2).getTexte(2).getDescription());
        // valeurs
        buildZonePrestations(data);

        if (source.getIsHome() && source.isInTypeChambreNonMedicalise()) {
            data.addData("HOME_MEDICALISE",
                    babelDoc.getTextes(2).getTexte(10).getDescription().replace("{ANNEE}", String.valueOf(annee)));
            data.addData("PAIEMENT0", babelDoc.getTextes(2).getTexte(11).getDescription());
            data.addData("PAIEMENT1", babelDoc.getTextes(2).getTexte(12).getDescription());
            data.addData("PAIEMENT2", babelDoc.getTextes(2).getTexte(13).getDescription());
        }

        data.addData("CONTENU1", babelDoc.getTextes(3).getTexte(1).getDescription());

        // plan de calcul
        // // fortune
        data.addData("LABEL_FORTUNE", babelDoc.getTextes(3).getTexte(2).getDescription());
        buildZoneFortune(data, tiersLangueIso);
        // // revenus
        data.addData("LABEL_REVENUS", babelDoc.getTextes(3).getTexte(3).getDescription());
        buildZoneRevenus(data, tiersLangueIso);
        // // dépenses
        data.addData("LABEL_DEPENSES", babelDoc.getTextes(3).getTexte(4).getDescription());
        buildZoneDepenses(data, tiersLangueIso);

        data.addData("CONTENU2", babelDoc.getTextes(1).getTexte(3).getDescription());
        data.addData("CONTENU3", babelDoc.getTextes(1).getTexte(4).getDescription());
        data.addData(
                "POLITESSE",
                babelDoc.getTextes(1).getTexte(5).getDescription().replace("{ANNEE}", String.valueOf(annee))
                        .replace("{TITRE}", formulePolitesse));
        data.addData("CONTENU4", babelDoc.getTextes(1).getTexte(25).getDescription());
        // signature
        // // nom de la caisse
        data.addData("SIGNATURE_NOM_CAISSE", babelDoc.getTextes(4).getTexte(1).getDescription());

    }

    private void buildHeader(DocumentData data) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        // Template header
        data.addData("header", "STANDARD");
        // Prépartion des données

        JadeUser gestionnaire = null;
        try {
            gestionnaire = getSession().getApplication()._getSecurityManager()
                    .getUserForVisa(getSession(), source.getIdGestionnaire());
        } catch (Exception e) {
            throw new AdaptationException("Couldn't get current gestionnaire!", e);
        }

        String nomCollabo = gestionnaire.getFirstname() + " " + gestionnaire.getLastname();
        String tel = gestionnaire.getPhone();
        // Infos collaborateur
        data.addData(SingleAdaptationBuilder.CHAMP_PERSONNE_REF, babelDoc.getTextes(1).getTexte(23).getDescription());

        data.addData("TEL_GESTIONNAIRE", gestionnaire.getPhone());
        data.addData("GESTIONNAIRE", gestionnaire.getFirstname() + " " + gestionnaire.getLastname());
        data.addData("ID_USER", gestionnaire.getIdUser());
        data.addData("NSS_BENEFICIAIRE", source.getNssAyantDroit());

        data.addData("NOM_COLLABO", nomCollabo);
        data.addData("TEL_COLLABO", babelDoc.getTextes(1).getTexte(24).getDescription() + " " + tel);
        data.addData("TEL_GESTIONNAIRE", tel);

        data.addData(
                "DATE_ET_LIEU",
                babelDoc.getTextes(1).getTexte(8).getDescription() + " "
                        + PegasusDateUtil.getLitteralDateByTiersLanguage(dateSurDoc, babelDoc.getCodeIsoLangue()));

        data.addData("NREF", babelDoc.getTextes(1).getTexte(21).getDescription() + " " + gestionnaire.getVisa());
        data.addData("VREF", babelDoc.getTextes(1).getTexte(22).getDescription() + " " + source.getNssAyantDroit());

        AdresseTiersDetail adresseTiersDetail = PegasusUtil.getAdresseCascadeByType(source.getIdTiersAyantDroit(),
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                SingleTransfertPCAbstractBuilder.listOrderAdresseTiers);

        data.addData("ADRESSE", adresseTiersDetail.getAdresseFormate());
    }

    private DocumentData buildMainPage(TITiers tiersBeneficiaire) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        DocumentData mainPage = new DocumentData();

        buildHeader(mainPage);

        buildContent(mainPage, tiersBeneficiaire);

        buildSignatures(mainPage);

        return mainPage;
    }

    private DocumentData buildSignatures(DocumentData data) {
        data.addData("signature", "STANDARD");
        // Ajout d'un caraige return si description caisse sur deux lignes
        data.addData("SIGNATURE_NOM_CAISSE", PRStringUtils.replaceString(babelDoc.getTextes(19).getTexte(2)
                .getDescription(), SingleAdaptationBuilder.CR_FROM_BABEL, SingleAdaptationBuilder.CR));

        data.addData("SIGNATAIRE", babelDoc.getTextes(19).getTexte(3).getDescription());
        data.addData("SIGNATURE_NOM_SERVICE", babelDoc.getTextes(19).getTexte(4).getDescription());

        data.addData("SIGNATURE_GESTIONNAIRE", getUserNomFormatte());

        // TODO PC: PC header agla

        // data.addData("GESTIONNAIRE", preprateurDecision.getFirstname() + " " + preprateurDecision.getLastname());
        // data.addData("ID_USER", preprateurDecision.getIdUser());
        // data.addData("NSS_BENEFICIAIRE", dacOO.getDecisionHeader().getPersonneEtendue().getPersonneEtendue()

        return data;
    }

    private DocumentData buildZoneDepenses(DocumentData data, String tiersLangueIso) {

        final String[] fields;

        if (source.getIsHome()) {
            // récupérer des variables métiers le forfait légal pour dépenses personnelles
            fields = new String[] { IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TAXES_PENSION_RECONNUE,
                    IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL };
        } else {
            // récupérer des variables métier le montant forfaitaire destiné à la couverture des besoins vitaux
            fields = new String[] { IPCValeursPlanCalcul.CLE_DEPEN_BES_VITA_TOTAL,
                    IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL };
        }

        Collection table = new Collection("tabDepenses");
        Map<String, String> planCalcul = source.getResumePlanCalcul();

        for (String field : fields) {
            String valeur = planCalcul.get(field);
            String libelle = "";
            try {
                CodeSystem csLibelle = CodeSystemUtils.searchCodeSystemTraduction(field,
                        BSessionUtil.getSessionFromThreadContext(), tiersLangueIso);
                libelle = csLibelle.getTraduction().replace("@", " ");
            } catch (Exception e) {
                JadeLogger.warn(e, e.getMessage());
                libelle = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(field).replace("@", " ");
            }
            addLignePlanCalcul(table, "DEPENSES", libelle, valeur);
        }

        data.add(table);
        return data;
    }

    private DocumentData buildZoneFortune(DocumentData data, String tiersLangueIso) {

        final String[] fields = { IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_TOTAL,
                IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_TOTAL, IPCValeursPlanCalcul.CLE_FORTU_FOR_DESS_TOTAL };

        Collection table = new Collection("tabFortune");
        Map<String, String> planCalcul = source.getResumePlanCalcul();

        for (String field : fields) {
            String valeur = planCalcul.get(field);
            String libelle = "";
            try {
                CodeSystem csLibelle = CodeSystemUtils.searchCodeSystemTraduction(field,
                        BSessionUtil.getSessionFromThreadContext(), tiersLangueIso);
                libelle = csLibelle.getTraduction().replace("@", " ");
            } catch (Exception e) {
                JadeLogger.warn(e, e.getMessage());
                libelle = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(field).replace("@", " ");
            }
            addLignePlanCalcul(table, "FORTUNE", libelle, valeur);
        }

        data.add(table);
        return data;
    }

    private DocumentData buildZonePrestations(DocumentData data) {

        float totalDF = 0;
        float totalDFPrecedent = 0;

        Collection table = new Collection("tabPrestations");
        for (ResumePrestations lignePrestation : source.getListePrestations()) {
            DataList line = new DataList("ligne");
            line.addData("DESIGNATION_DF", lignePrestation.designation);
            line.addData("NOM_BENEFICIAIRE", lignePrestation.nomBeneficiaire);
            line.addData("NSS_BENEFICIAIRE", lignePrestation.nssBeneficiaire);
            if (lignePrestation.montantMensuelPrecendent != null) {
                line.addData("MONTANT_DF_PRECEDENT",
                        new FWCurrency(lignePrestation.montantMensuelPrecendent).toStringFormat());
                totalDFPrecedent += Float.parseFloat(lignePrestation.montantMensuelPrecendent);
            }
            line.addData("MONTANT_DF", new FWCurrency(lignePrestation.montantMensuel).toStringFormat());
            totalDF += Float.parseFloat(lignePrestation.montantMensuel);
            table.add(line);
        }
        data.add(table);

        data.addData("TOTAL_DF", new FWCurrency(totalDF).toStringFormat());
        data.addData("TOTAL_DF_PRECEDENT", new FWCurrency(totalDFPrecedent).toStringFormat());

        return data;
    }

    private DocumentData buildZoneRevenus(DocumentData data, String tiersLangueIso) {

        final String[] fields = { IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_TOTAL,
                IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_TOTAL, IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL,
                IPCValeursPlanCalcul.CLE_REVEN_RENAVSAI_TOTAL, IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_TOTAL,
                IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_TOTAL, IPCValeursPlanCalcul.CLE_REVEN_DESS_REV_TOTAL };

        Collection table = new Collection("tabRevenus");
        Map<String, String> planCalcul = source.getResumePlanCalcul();

        for (String field : fields) {
            String valeur = planCalcul.get(field);
            String libelle = "";
            try {
                CodeSystem csLibelle = CodeSystemUtils.searchCodeSystemTraduction(field,
                        BSessionUtil.getSessionFromThreadContext(), tiersLangueIso);
                libelle = csLibelle.getTraduction().replace("@", " ");
            } catch (Exception e) {
                JadeLogger.warn(e, e.getMessage());
                libelle = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(field).replace("@", " ");
            }
            addLignePlanCalcul(table, "REVENUS", libelle, valeur);
        }

        data.add(table);
        return data;
    }

}
