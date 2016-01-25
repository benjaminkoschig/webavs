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
import ch.globaz.pegasus.business.constantes.IPCCatalogueTextes;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.AbstractPegasusBuilder;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.topaz.datajuicer.DocumentData;

public class SingleTransfertRentePCBuilder extends SingleTransfertPCAbstractBuilder {

    private static final String CHAMP_NOM_COLLABORATEUR = "NOM_COLLABO";
    private static final String CHAMP_PERSONNE_REF = "PERSONNE_REF";

    private static final String CHAMP_TEL_COLLABORATEUR = "TEL_COLLABO";

    private static final String STR_CAISSE_ACCEPTANT = "acceptant";

    private static final String STR_CAISSE_REFUSANT = "refusant";

    private static final String STR_ID_CAISSE = "caisse";

    private ICTDocument babelDoc = null;
    private String dateAnnonce = null;
    private String dateTransfert = null;

    private JadeUser gestionnaire = null;
    private String idCaisseAgence = null;

    private Boolean isCaisseAcceptant = null;
    private PersonneEtendueComplexModel requerant = null;

    public JadePrintDocumentContainer build(ICTDocument babelDoc, JadePrintDocumentContainer allDoc,
            String idGestionnaire, PersonneEtendueComplexModel requerant, String idNouvelleCaisse,
            Boolean isCaisseAcceptant, String dateTransfert, String dateAnnonce) throws TransfertDossierException {

        try {
            // récupère l'utilisateur
            gestionnaire = getSession().getApplication()._getSecurityManager()
                    .getUserForVisa(getSession(), idGestionnaire);
            idCaisseAgence = idNouvelleCaisse;

            this.dateAnnonce = dateAnnonce;
            this.dateTransfert = PegasusDateUtil.convertToJadeDate(dateTransfert);
            this.babelDoc = babelDoc;
            this.requerant = requerant;

            this.isCaisseAcceptant = isCaisseAcceptant;

            buildOriginal(allDoc, false);

            return allDoc;
        } catch (FWSecurityLoginException e) {
            throw new TransfertDossierException("Couldn't login when trying to get gestionnaire!", e);
        } catch (Exception e) {
            throw new TransfertDossierException("An error happened while building the document!", e);
        }
    }

    private void buildContent(DocumentData data) throws TransfertDossierException {

        String nomPrenomRequerant = PegasusUtil.formatNomPrenom(requerant.getTiers());

        if (isCaisseAcceptant) {
            buildContentAcceptant(data, nomPrenomRequerant);
        } else {
            buildContentRefusant(data, nomPrenomRequerant);
        }

    }

    private void buildContentAcceptant(DocumentData data, String nomPrenomRequerant) throws TransfertDossierException {

        data.addData("TITRE",
                babelDoc.getTextes(2).getTexte(5).getDescription().replace("{REQUERANT}", nomPrenomRequerant));
        data.addData("TITRE_POLITESSE", babelDoc.getTextes(1).getTexte(1).getDescription());

        // prestations
        data.addData("CONTENU1", babelDoc.getTextes(2).getTexte(1).getDescription());
        data.addData("CONTENU2",
                babelDoc.getTextes(2).getTexte(2).getDescription().replace("{DATE_TRANSFERT}", dateTransfert));
        data.addData("CONTENU3", babelDoc.getTextes(2).getTexte(3).getDescription());

        data.addData("SALUTATIONS", babelDoc.getTextes(2).getTexte(4).getDescription());
    }

    private void buildContentRefusant(DocumentData data, String nomPrenomRequerant) throws TransfertDossierException {

        data.addData("TITRE",
                babelDoc.getTextes(3).getTexte(6).getDescription().replace("{REQUERANT}", nomPrenomRequerant));
        data.addData("TITRE_POLITESSE", babelDoc.getTextes(1).getTexte(1).getDescription());

        // prestations
        data.addData("CONTENU1", babelDoc.getTextes(3).getTexte(1).getDescription());
        data.addData("CONTENU2",
                babelDoc.getTextes(3).getTexte(2).getDescription().replace("{DATE_TRANSFERT}", dateTransfert));
        data.addData("CONTENU2_2", babelDoc.getTextes(3).getTexte(3).getDescription());
        data.addData("CONTENU3", babelDoc.getTextes(3).getTexte(4).getDescription());

        data.addData("SALUTATIONS", babelDoc.getTextes(3).getTexte(5).getDescription());
    }

    private void buildHeader(DocumentData data, Boolean isCopie) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        // Template header
        data.addData("header", "STANDARD");
        // Prépartion des données
        String nomCollabo = gestionnaire.getFirstname() + " " + gestionnaire.getLastname();
        String tel = gestionnaire.getPhone();

        data.addData(SingleTransfertRentePCBuilder.CHAMP_PERSONNE_REF, babelDoc.getTextes(1).getTexte(23)
                .getDescription());

        // Infos collaborateur
        data.addData(SingleTransfertRentePCBuilder.CHAMP_NOM_COLLABORATEUR, nomCollabo);
        data.addData(SingleTransfertRentePCBuilder.CHAMP_TEL_COLLABORATEUR, babelDoc.getTextes(1).getTexte(24)
                .getDescription()
                + " " + tel);

        data.addData("NREF", babelDoc.getTextes(1).getTexte(21).getDescription() + " " + gestionnaire.getVisa());
        data.addData("VREF", babelDoc.getTextes(1).getTexte(22).getDescription() + " "
                + requerant.getPersonneEtendue().getNumAvsActuel());

        if (isCopie) {
            data.addData("IS_COPIE", "COPIE");
        }

        // Date et lieu
        data.addData(
                "DATE_ET_LIEU",
                babelDoc.getTextes(1).getTexte(10).getDescription() + " "
                        + PegasusDateUtil.getLitteralDateByTiersLanguage(dateAnnonce, babelDoc.getCodeIsoLangue()));

        AdresseTiersDetail adresseTiersDetail = PegasusUtil.getAdresseCascadeByType(idCaisseAgence,
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                SingleTransfertPCAbstractBuilder.listOrderAdresseTiers);

        data.addData("ADRESSE", adresseTiersDetail.getAdresseFormate());
    }

    private DocumentData buildMainPage(boolean isCopy) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        DocumentData mainPage = new DocumentData();

        buildHeader(mainPage, isCopy);

        buildContent(mainPage);

        buildSignatures(mainPage);

        return mainPage;
    }

    private void buildOriginal(JadePrintDocumentContainer allDoc, boolean isCopy)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        DocumentData mainPage = buildMainPage(isCopy);

        mainPage.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_TRANSFERT_RENTE_PC);

        mainPage.addData(SingleTransfertRentePCBuilder.STR_ID_CAISSE,
                isCaisseAcceptant ? SingleTransfertRentePCBuilder.STR_CAISSE_ACCEPTANT
                        : SingleTransfertRentePCBuilder.STR_CAISSE_REFUSANT);

        allDoc.addDocument(mainPage, null);
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
