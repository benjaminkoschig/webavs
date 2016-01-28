package ch.globaz.pegasus.businessimpl.utils.topazbuilder.demanderenseignement;

import globaz.babel.api.ICTDocument;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.security.FWSecurityLoginException;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCCatalogueTextes;
import ch.globaz.pegasus.business.exceptions.models.demanderenseignement.DemandeRenseignementException;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.AbstractPegasusBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier.SingleTransfertPCAbstractBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.util.PegasusPubInfoBuilder;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

public class SingleDemandeRenseignementAgenceCommunaleAVSBuilder extends AbstractPegasusBuilder {

    private static final String CHAMP_NOM_COLLABORATEUR = "NOM_COLLABO";
    private static final String CHAMP_PERSONNE_REF = "PERSONNE_REF";
    private static final String CHAMP_TEL_COLLABORATEUR = "TEL_COLLABO";
    private static final String ID_DOCUMENT_TOPAZ = "agenceCommunaleAVS";
    private List<String> annexes;
    private ICTDocument babelDoc = null;
    private ICTDocument babelDocCommun = null;
    private ICTDocument babelPageGardeDoc = null;
    private List<TiersSimpleModel> copies;
    private JadeUser gestionnaire = null;
    private String idAgenceCommunale = null;
    private List<String> listeTexteLibre;

    private PersonneEtendueComplexModel requerant = null;

    public JadePrintDocumentContainer build(ICTDocument babelDoc, ICTDocument babelDocCommun,
            ICTDocument babelDocPageGarde, JadePrintDocumentContainer allDoc, String idGestionnaire,
            PersonneEtendueComplexModel requerant, List<String> listeTexteLibre, List<String> annexes,
            List<String> copies) throws DemandeRenseignementException {

        this.babelDocCommun = babelDocCommun;
        babelPageGardeDoc = babelDocPageGarde;
        try {
            // récupère l'utilisateur
            gestionnaire = getSession().getApplication()._getSecurityManager()
                    .getUserForVisa(getSession(), idGestionnaire);

            this.listeTexteLibre = listeTexteLibre;
            this.annexes = annexes;
            this.copies = getTiersCopies(copies);
            this.babelDoc = babelDoc;
            this.requerant = requerant;

            buildOriginal(allDoc, false);

            for (TiersSimpleModel tiers : this.copies) {
                buildPageDeGarde(allDoc, tiers);
                buildOriginal(allDoc, true);
            }

            return allDoc;
        } catch (FWSecurityLoginException e) {
            throw new DemandeRenseignementException("Couldn't login when trying to get gestionnaire!", e);
        } catch (Exception e) {
            throw new DemandeRenseignementException("An error happened while building the document!", e);
        }
    }

    private DocumentData buildAnnexes(DocumentData data) {
        data.addData("ANNEXE", babelDocCommun.getTextes(1).getTexte(2).getDescription());
        data.addData("ANNEXES", PegasusUtil.joinArray(annexes, AbstractPegasusBuilder.NEW_LINE));
        return data;
    }

    private void buildContent(DocumentData data) throws DemandeRenseignementException {

        String titre = babelDoc.getTextes(1).getTexte(1).getDescription()
                .replace("{BENEFICIAIRE}", PegasusUtil.formatNomPrenom(requerant.getTiers()));

        data.addData("TITRE", titre);
        data.addData("TITRE_POLITESSE", babelDocCommun.getTextes(1).getTexte(1).getDescription());

        // prestations
        data.addData("CONTENU1", babelDoc.getTextes(1).getTexte(2).getDescription());
        data.addData("CONTENU2", babelDoc.getTextes(1).getTexte(3).getDescription());

        data = buildZoneTexteLibre(data);

        data.addData("SALUTATIONS", babelDoc.getTextes(1).getTexte(4).getDescription());

    }

    private DocumentData buildCopies(DocumentData data) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        // Liste des copies

        String[] names = new String[copies.size()];
        int i = 0;
        for (TiersSimpleModel tiers : copies) {
            names[i++] = PegasusUtil.formatNomPrenom(tiers);
        }

        data.addData("COPIE", babelDocCommun.getTextes(1).getTexte(3).getDescription());
        data.addData("COPIES", PegasusUtil.joinArray(names, AbstractPegasusBuilder.NEW_LINE));

        return data;
    }

    private void buildHeader(DocumentData data, Boolean isCopie) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        // Template header
        data.addData("header", "STANDARD");
        // Prépartion des données
        String nomCollabo = gestionnaire.getFirstname() + " " + gestionnaire.getLastname();
        String tel = gestionnaire.getPhone();

        data.addData(SingleDemandeRenseignementAgenceCommunaleAVSBuilder.CHAMP_PERSONNE_REF, babelDocCommun
                .getTextes(1).getTexte(23).getDescription());
        // Infos collaborateur
        data.addData(SingleDemandeRenseignementAgenceCommunaleAVSBuilder.CHAMP_NOM_COLLABORATEUR, nomCollabo);
        data.addData(SingleDemandeRenseignementAgenceCommunaleAVSBuilder.CHAMP_TEL_COLLABORATEUR, babelDocCommun
                .getTextes(1).getTexte(24).getDescription()
                + " " + tel);

        data.addData("NREF", babelDocCommun.getTextes(1).getTexte(21).getDescription() + " " + gestionnaire.getVisa());
        data.addData("VREF", babelDocCommun.getTextes(1).getTexte(22).getDescription() + " "
                + requerant.getPersonneEtendue().getNumAvsActuel());

        if (isCopie) {
            data.addData("IS_COPIE", "COPIE");
        }

        AdresseTiersDetail adresseTiersDetail = PegasusUtil.getAdresseCascadeByType(getIdAgence(),
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
        buildAnnexes(mainPage);
        buildCopies(mainPage);

        return mainPage;
    }

    private void buildOriginal(JadePrintDocumentContainer allDoc, boolean isCopy)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        DocumentData mainPage = buildMainPage(isCopy);

        mainPage.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_DEMANDE_RENSEIGNEMENT);
        mainPage.addData(IPCCatalogueTextes.STR_ID_DEMANDE_RENSEIGNEMENT,
                SingleDemandeRenseignementAgenceCommunaleAVSBuilder.ID_DOCUMENT_TOPAZ);

        allDoc.addDocument(mainPage, new PegasusPubInfoBuilder().rectoVersoLast().getPubInfo());
    }

    private void buildPageDeGarde(JadePrintDocumentContainer allDoc, TiersSimpleModel tiers) throws Exception {
        DocumentData data = new DocumentData();

        data.addData("header", "STANDARD");
        String nomCollabo = gestionnaire.getFirstname() + " " + gestionnaire.getLastname();
        String tel = gestionnaire.getPhone();
        // Infos collaborateur
        data.addData(SingleDemandeRenseignementAgenceCommunaleAVSBuilder.CHAMP_NOM_COLLABORATEUR, nomCollabo);
        data.addData(SingleDemandeRenseignementAgenceCommunaleAVSBuilder.CHAMP_TEL_COLLABORATEUR, tel);
        data.addData("SERVICE_COLLABORATEUR", gestionnaire.getDepartment());
        data.addData("EMAIL_COLLABORATEUR", gestionnaire.getEmail());

        AdresseTiersDetail adresseTiersDetail = PegasusUtil.getAdresseCascadeByType(tiers.getIdTiers(),
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                SingleTransfertPCAbstractBuilder.listOrderAdresseTiers);

        data.addData("ADRESSE", adresseTiersDetail.getAdresseFormate());

        // Politesse
        String titreTiers = getSession().getCodeLibelle(tiers.getTitreTiers());
        if (JadeStringUtil.isEmpty(titreTiers)) {
            titreTiers = babelDocCommun.getTextes(1).getTexte(1).getDescription();
        } else {
            titreTiers += ",";
        }
        data.addData("POLITESSE", titreTiers);

        // Ligne intro
        data.addData("INTRO", babelPageGardeDoc.getTextes(1).getTexte(1).getDescription());

        // Salutations
        data.addData("SALUTATIONS", babelPageGardeDoc.getTextes(1).getTexte(4).getDescription() + " " + titreTiers
                + " " + babelPageGardeDoc.getTextes(1).getTexte(5));

        // signature
        buildSignatures(data);

        data.addData("ANNEXE", babelPageGardeDoc.getTextes(1).getTexte(2).getDescription());
        data.addData("ANNEXE_MENT", babelPageGardeDoc.getTextes(1).getTexte(3).getDescription());

        data.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_DECISION_APRESCALCUL_PG);
        allDoc.addDocument(data, new PegasusPubInfoBuilder().rectoVersoFirst().getPubInfo());
    }

    private DocumentData buildSignatures(DocumentData data) {
        data.addData("signature", "STANDARD");
        // Ajout d'un caraige return si description caisse sur deux lignes
        data.addData("SIGNATURE_NOM_CAISSE", PRStringUtils.replaceString(babelDoc.getTextes(19).getTexte(2)
                .getDescription(), AbstractPegasusBuilder.CR_FROM_BABEL, AbstractPegasusBuilder.CR));

        data.addData("SIGNATURE_GESTIONNAIRE", getUserNomFormatte());
        return data;
    }

    private DocumentData buildZoneTexteLibre(DocumentData data) {

        Collection table = new Collection("tabTexteLibre");
        for (String ligneLibre : listeTexteLibre) {
            DataList line = new DataList("ligne");
            line.addData("LIGNE_TEXTE_LIBRE", ligneLibre);
            table.add(line);

        }

        data.add(table);
        return data;
    }

    private String getIdAgence() throws DemandeRenseignementException {
        if (idAgenceCommunale == null) {
            try {
                idAgenceCommunale = TIBusinessServiceLocator.getAdministrationService().getAgenceCommunalAVSIdTiers(
                        requerant.getTiers().getIdTiers());
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new DemandeRenseignementException("Service not Available!", e);
            } catch (JadePersistenceException e) {
                throw new DemandeRenseignementException("A persistence exception happened!", e);
            } catch (JadeApplicationException e) {
                throw new DemandeRenseignementException("An exception happened!", e);
            }
        }
        return idAgenceCommunale;
    }

    protected final List<TiersSimpleModel> getTiersCopies(List<String> idTiersCopies)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        List<TiersSimpleModel> result = new ArrayList<TiersSimpleModel>();

        /**
         * recherche tous les tiers selon leur ID. Vu que les personnes physiques et administrations sont mélangées dans
         * une même liste, on essaie les 2 services pour trouver le tiers Cette manière de chercher les tiers sera à
         * améliorer.
         */
        for (String idTiers : idTiersCopies) {
            if (!JadeNumericUtil.isNumeric(idTiers)) {
                continue;
            }
            PersonneEtendueComplexModel personne = TIBusinessServiceLocator.getPersonneEtendueService().read(idTiers);

            TiersSimpleModel tiers = null;
            if (personne.getTiers().getIdTiers() != null) {
                tiers = personne.getTiers();
            } else {
                AdministrationComplexModel administration = TIBusinessServiceLocator.getAdministrationService().read(
                        idTiers);
                tiers = administration.getTiers();
            }

            result.add(tiers);
        }

        return result;
    }

}
