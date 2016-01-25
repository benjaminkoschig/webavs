package ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier;

import globaz.babel.api.ICTDocument;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.security.FWSecurityLoginException;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.db.tiers.TITiers;
import java.util.List;
import java.util.Map;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.common.document.TextGiver;
import ch.globaz.common.document.babel.BabelTextDefinition;
import ch.globaz.common.document.babel.TextGiverBabel;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.constantes.IPCCatalogueTextes;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.AbstractPegasusBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.pagedegarde.IPageDeGardeDefinition;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.pagedegarde.PageDeGardeBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.pagedegarde.PageDeGardeDefinition;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.util.PegasusPubInfoBuilder;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.LocaliteSimpleModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.topaz.datajuicer.DocumentData;

public class SingleTransfertSansPCBuilder extends SingleTransfertPCAbstractBuilder {

    private static final String CHAMP_NOM_COLLABORATEUR = "NOM_COLLABO";
    private static final String CHAMP_PERSONNE_REF = "PERSONNE_REF";
    private static final String CHAMP_TEL_COLLABORATEUR = "TEL_COLLABO";
    private List<String> annexes;
    private ICTDocument babelDoc = null;
    private ICTDocument babelPageGardeDoc = null;
    private List<TITiers> copies = null;
    private String dateSurDocument = null;

    private LocaliteSimpleModel dernierDomicileLegal = null;
    private JadeUser gestionnaire = null;

    private String idNouvelleCaisse = null;
    private PersonneEtendueComplexModel requerant = null;

    public JadePrintDocumentContainer build(ICTDocument babelDoc, Map<Langues, CTDocumentImpl> babelPageGardeDocuments,
            JadePrintDocumentContainer allDoc, String idGestionnaire, PersonneEtendueComplexModel requerant,
            LocaliteSimpleModel dernierDomicileLegal, String idNouvelleCaisse, List<String> annexes,
            List<String> copies, String dateSurDocument) throws TransfertDossierException {
        try {
            this.dateSurDocument = PegasusDateUtil.convertToJadeDate(dateSurDocument);
            gestionnaire = getSession().getApplication()._getSecurityManager()
                    .getUserForVisa(getSession(), idGestionnaire);
            this.requerant = requerant;
            this.dernierDomicileLegal = dernierDomicileLegal;
            this.idNouvelleCaisse = idNouvelleCaisse;
            this.annexes = annexes;
            this.copies = getTiersCopies(copies);

            this.babelDoc = babelDoc;

            DocumentData mainPage = buildMainPage(false);

            mainPage.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_TRANSFERT_DOSSIER_SANS_PC);

            allDoc.addDocument(mainPage, new PegasusPubInfoBuilder().rectoVersoLast().getPubInfo());

            /*********************** Copies ************/

            for (TITiers tiers : this.copies) {
                // récupération du catalogue de texte de la page de garde associé au tiers à qui on l'envoie
                babelPageGardeDoc = babelPageGardeDocuments.get(LanguageResolver.resolveISOCode(tiers.getLangue()));
                TextGiver<BabelTextDefinition> textGiver = new TextGiverBabel(babelPageGardeDoc);
                IPageDeGardeDefinition pageDeGardeDefinition = new PageDeGardeDefinition(textGiver);
                // Récupération de la référence pour VREF
                String NSSReference = requerant.getPersonneEtendue().getNumAvsActuel();
                PageDeGardeBuilder pageDeGarde = new PageDeGardeBuilder(babelPageGardeDoc, pageDeGardeDefinition,
                        tiers, gestionnaire, null, NSSReference, dateSurDocument);
                DocumentData dataPageGarde = pageDeGarde.buildPageDeGarde();
                dataPageGarde.addData(IPCCatalogueTextes.STR_ID_PROCESS,
                        IPCCatalogueTextes.PROCESS_DECISION_APRESCALCUL_PG);
                allDoc.addDocument(dataPageGarde, new PegasusPubInfoBuilder().rectoVersoLast().getPubInfo());

                DocumentData dataCopie = buildMainPage(true);
                dataCopie.addData(IPCCatalogueTextes.STR_ID_PROCESS,
                        IPCCatalogueTextes.PROCESS_TRANSFERT_DOSSIER_SANS_PC);
                allDoc.addDocument(dataCopie, new PegasusPubInfoBuilder().rectoVersoLast().getPubInfo());
            }

            return allDoc;
        } catch (FWSecurityLoginException e) {
            throw new TransfertDossierException("Couldn't login when trying to get gestionnaire!", e);
        } catch (Exception e) {
            throw new TransfertDossierException("An error happened while building the document!", e);
        }
    }

    /**
     * Création du bloc annexes
     * 
     * @param data
     * @param document
     * @return data, instance de DocumentData mis à jour
     */
    private DocumentData buildAnnexes(DocumentData data) {
        data.addData("ANNEXE", babelDoc.getTextes(1).getTexte(1).getDescription());
        data.addData("ANNEXES", PegasusUtil.joinArray(annexes, "\n"));
        return data;
    }

    private void buildContent(DocumentData data) {
        TiersSimpleModel membreFamille = requerant.getTiers();
        // champs à remplir
        data.addData("SALUTATIONS", babelDoc.getTextes(1).getTexte(3).getDescription());

        String titre = babelDoc.getTextes(2).getTexte(1).getDescription();
        // %=REQUERANT=%
        titre = titre.replaceFirst("\\{REQUERANT\\}", PegasusUtil.formatNomPrenom(membreFamille));
        data.addData("TITRE", titre);

        for (int i = 0; i < 4; i++) {
            String contenu = babelDoc.getTextes(2).getTexte(i + 10).getDescription();
            if (i == 1) {
                // %=COMMUNE=%
                contenu = contenu.replaceFirst("\\{COMMUNE\\}", dernierDomicileLegal.getLocalite());
            }
            data.addData("CONTENU" + i, contenu);

        }

    }

    /**
     * Création du bloc copies
     * 
     * @param data
     * @param document
     * @return data, instance de DocumentData mis à jour
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private DocumentData buildCopies(DocumentData data) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        // Liste des copies

        String[] names = new String[copies.size()];
        int i = 0;
        for (TITiers tiers : copies) {
            names[i++] = tiers.getDesignation1() + " " + tiers.getDesignation2();
        }

        data.addData("COPIE", babelDoc.getTextes(1).getTexte(2).getDescription());
        data.addData("COPIES", PegasusUtil.joinArray(names, "\n"));

        return data;
    }

    private void buildHeader(DocumentData data, Boolean isCopie) throws Exception {
        // Template header
        data.addData("header", "STANDARD");
        // Prépartion des données
        String nomCollabo = gestionnaire.getFirstname() + " " + gestionnaire.getLastname();
        String tel = gestionnaire.getPhone();
        // Infos collaborateur
        data.addData(SingleTransfertSansPCBuilder.CHAMP_PERSONNE_REF, babelDoc.getTextes(1).getTexte(23)
                .getDescription());

        data.addData(SingleTransfertSansPCBuilder.CHAMP_NOM_COLLABORATEUR, nomCollabo);
        data.addData(SingleTransfertSansPCBuilder.CHAMP_TEL_COLLABORATEUR, babelDoc.getTextes(1).getTexte(24)
                .getDescription()
                + " " + tel);

        data.addData("NREF", babelDoc.getTextes(1).getTexte(21).getDescription() + " " + gestionnaire.getVisa());
        data.addData("VREF", babelDoc.getTextes(1).getTexte(22).getDescription() + " "
                + requerant.getPersonneEtendue().getNumAvsActuel());

        if (isCopie) {
            data.addData("IS_COPIE", "COPIE");
        }

        AdresseTiersDetail adresseTiersDetail = PegasusUtil.getAdresseCascadeByType(idNouvelleCaisse,
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                SingleTransfertPCAbstractBuilder.listOrderAdresseTiers);

        // Date et lieu

        data.addData(
                "DATE_ET_LIEU",
                babelDoc.getTextes(1).getTexte(10).getDescription() + " "
                        + PegasusDateUtil.getLitteralDateByTiersLanguage(dateSurDocument, babelDoc.getCodeIsoLangue()));

        data.addData("ADRESSE", adresseTiersDetail.getAdresseFormate());
    }

    private DocumentData buildMainPage(boolean isCopy) throws Exception {
        DocumentData mainPage = new DocumentData();

        buildHeader(mainPage, isCopy);

        buildContent(mainPage);

        buildSignatures(mainPage);

        // Annexes et copies
        buildAnnexes(mainPage);
        buildCopies(mainPage);

        return mainPage;
    }

    private DocumentData buildSignatures(DocumentData data) {
        data.addData("signature", "STANDARD");
        // Ajout d'un caraige return si description caisse sur deux lignes
        data.addData("SIGNATURE_NOM_CAISSE", PRStringUtils.replaceString(babelDoc.getTextes(19).getTexte(2)
                .getDescription(), AbstractPegasusBuilder.CR_FROM_BABEL, AbstractPegasusBuilder.CR));

        data.addData("SIGNATAIRE", babelDoc.getTextes(19).getTexte(3).getDescription());
        data.addData("SIGNATURE_NOM_SERVICE", babelDoc.getTextes(19).getTexte(4).getDescription());
        data.addData("SIGNATURE_GESTIONNAIRE", getUserNomFormatte());
        return data;
    }

}
