package ch.globaz.pegasus.businessimpl.services.annonce.communicationocc;

import globaz.babel.api.ICTDocument;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.IPCCatalogueTextes;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneesPersonnellesException;
import ch.globaz.pegasus.business.models.annonce.CommunicationOCC;
import ch.globaz.pegasus.business.models.annonce.SimpleCommunicationOCC;
import ch.globaz.pegasus.businessimpl.services.lot.AbstractLotBuilder;
import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.topaz.datajuicer.DocumentData;

public class SingleCommOCCBuilder extends AbstractLotBuilder {

    private static final int LABEL_STATUS_PC_OCTROI = 11;
    private static final int LABEL_STATUS_PC_OCTROI_PARTIEL = 12;
    private static final int LABEL_STATUS_PC_REFUS = 10;
    private final static Object[][] labels = { { "LABEL_ENTETE", 1, 1 }, { "LABEL_CONCERNE", 1, 2 },
            { "LABEL_NO_SERIE", 2, 1 }, { "LABEL_NOM_PRENOM", 2, 2 }, { "LABEL_NO_NSS", 2, 3 },
            { "LABEL_NOM_COMMUNE", 2, 4 }, { "LABEL_NO_OCC", 2, 5 }, { "LABEL_DATE_NAISSANCE", 2, 6 },
            { "LABEL_ETAT_CIVIL", 2, 7 }, { "LABEL_SEXE", 2, 9 }, { "LABEL_DATE_EFFET", 3, 1 } };
    private final static Object[][] labelsFormulaire = { { "LABEL_ENTETE", 1, 1 }, { "LABEL_FORMULAIRE", 4, 1 },
            { "LABEL_CONCERNE", 1, 2 }, { "LABEL_NO_SERIE", 2, 1 }, { "LABEL_NOM_PRENOM", 2, 2 },
            { "LABEL_NO_NSS", 2, 3 }, { "LABEL_NOM_COMMUNE", 2, 4 }, { "LABEL_NO_OCC", 2, 5 },
            { "LABEL_DATE_NAISSANCE", 2, 6 }, { "LABEL_ETAT_CIVIL", 2, 7 }, { "LABEL_SEXE", 2, 9 },
            { "LABEL_NOUVEAU_NO_OCC", 4, 2 } };

    private final static Object[][] labelsRequerant = { { "LABEL_NOM_PRENOM_REQUERANT", 2, 10 },
            { "LABEL_NO_OCC_REQUERANT", 2, 11 }, { "LABEL_NO_NSS_REQUERANT", 2, 12 } };

    private static final String PEGASUS_ANNONCES_OCC_SORT = "pegasus.annonces.occ.sort";

    public JadePrintDocumentContainer build(ICTDocument babelDoc, JadePrintDocumentContainer allDoc,
            CommunicationOCC communication, String noSerie) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        DocumentData docData = new DocumentData();

        // initialises labels
        initializeLabels(babelDoc, docData, SingleCommOCCBuilder.labels);

        // initialise champs
        TiersSimpleModel tiers = communication.getTiers().getTiers();
        PersonneSimpleModel personne = communication.getTiers().getPersonne();
        SimpleCommunicationOCC simpleCommOCC = communication.getSimpleCommunicationOCC();

        docData.addData("DATE_RAPPORT", simpleCommOCC.getDateRapport());

        // tiers concerné
        docData.addData("NOM", tiers.getDesignation1());
        docData.addData("PRENOM", tiers.getDesignation2());
        docData.addData("NO_NSS", communication.getTiers().getPersonneEtendue().getNumAvsActuel());
        docData.addData("NOM_COMMUNE", communication.getLocalite().getLocalite());
        docData.addData("NO_OCC", communication.getDonneePersonnelle().getNoOCC());
        docData.addData("DATE_NAISSANCE", personne.getDateNaissance());
        docData.addData("ETAT_CIVIL", BSessionUtil.getSessionFromThreadContext()
                .getCodeLibelle(personne.getEtatCivil()));
        docData.addData("SEXE", BSessionUtil.getSessionFromThreadContext().getCodeLibelle(personne.getSexe()));

        // tiers requerant
        if (!simpleCommOCC.getIdTiers().equals(simpleCommOCC.getIdTiersRequerant())) {
            // initialises labels
            for (Object[] label : SingleCommOCCBuilder.labelsRequerant) {
                docData.addData((String) label[0], babelDoc.getTextes((Integer) label[1]).getTexte((Integer) label[2])
                        .getDescription());
            }
            TiersSimpleModel tiersRequerant = communication.getTiersRequerant().getTiers();
            docData.addData("NOM_PRENOM_REQUERANT",
                    tiersRequerant.getDesignation1() + " " + tiersRequerant.getDesignation2());
            // TODO NO_OCC
            docData.addData("NO_OCC_REQUERANT", communication.getDonneePersonnelleRquerant().getNoOCC());
            docData.addData("NO_NSS_REQUERANT", communication.getTiersRequerant().getPersonneEtendue()
                    .getNumAvsActuel());

        }

        docData.addData("MOTIF", getLabelEtatPC(babelDoc, simpleCommOCC.getEtatPC()) + simpleCommOCC.getMotif());

        // adapter le texte s'il y a une date de fin
        String valueDateEffet;
        if (JadeDateUtil.isGlobazDateMonthYear(simpleCommOCC.getDateFinEffet())) {

            String template = babelDoc.getTextes(3).getTexte(3).getDescription();

            valueDateEffet = template.replace("{1}", simpleCommOCC.getDateEffet()).replace("{2}",
                    simpleCommOCC.getDateFinEffet());

        } else {
            String template = babelDoc.getTextes(3).getTexte(2).getDescription();
            valueDateEffet = template.replace("{1}", simpleCommOCC.getDateEffet());

        }
        docData.addData("DATE_EFFET", valueDateEffet);

        docData.addData("NO_SERIE", noSerie);

        docData.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_COMMUNICATION_OCC);

        JadePublishDocumentInfo docInfo = new JadePublishDocumentInfo();
        docInfo.setPublishDocument(false);
        docInfo.setArchiveDocument(true);
        docInfo.setDocumentProperty(SingleCommOCCBuilder.PEGASUS_ANNONCES_OCC_SORT, tiers.getDesignation1() + " "
                + tiers.getDesignation2());

        allDoc.addDocument(docData, docInfo);

        if (JadeStringUtil.isEmpty(communication.getDonneePersonnelleRquerant().getNoOCC())) {
            JadePublishDocumentInfo docInfoFormulaire = new JadePublishDocumentInfo();
            docInfoFormulaire.setPublishDocument(false);
            docInfoFormulaire.setArchiveDocument(true);
            docInfoFormulaire.setDocumentProperty(SingleCommOCCBuilder.PEGASUS_ANNONCES_OCC_SORT,
                    tiers.getDesignation1() + " " + tiers.getDesignation2());

            allDoc.addDocument(buildFormulaire(babelDoc, communication, noSerie), docInfoFormulaire);
        }

        return allDoc;
    }

    private DocumentData buildFormulaire(ICTDocument babelDoc, CommunicationOCC communication, String noSerie)
            throws DonneesPersonnellesException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        DocumentData docData = new DocumentData();

        initializeLabels(babelDoc, docData, SingleCommOCCBuilder.labelsFormulaire);

        PersonneSimpleModel personne = communication.getTiers().getPersonne();
        TiersSimpleModel tiers = communication.getTiers().getTiers();

        // tiers concerné
        docData.addData("NOM", tiers.getDesignation1());
        docData.addData("PRENOM", tiers.getDesignation2());
        docData.addData("NO_NSS", communication.getTiers().getPersonneEtendue().getNumAvsActuel());
        docData.addData("NOM_COMMUNE", communication.getLocalite().getLocalite());
        docData.addData("NO_OCC", communication.getDonneePersonnelleRquerant().getNoOCC());
        docData.addData("DATE_NAISSANCE", personne.getDateNaissance());
        docData.addData("ETAT_CIVIL", BSessionUtil.getSessionFromThreadContext()
                .getCodeLibelle(personne.getEtatCivil()));
        docData.addData("SEXE", BSessionUtil.getSessionFromThreadContext().getCodeLibelle(personne.getSexe()));

        docData.addData("NO_SERIE", noSerie);

        docData.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_FORMULAIRE_COMMUNICATION_OCC);

        return docData;
    }

    private final String getLabelEtatPC(ICTDocument babelDoc, String etatPC) {

        final Map<String, Integer> map = new HashMap<String, Integer>() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            {
                put(IPCValeursPlanCalcul.STATUS_REFUS, SingleCommOCCBuilder.LABEL_STATUS_PC_REFUS);
                put(IPCValeursPlanCalcul.STATUS_OCTROI, SingleCommOCCBuilder.LABEL_STATUS_PC_OCTROI);
                put(IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL, SingleCommOCCBuilder.LABEL_STATUS_PC_OCTROI_PARTIEL);
            }
        };

        Integer idx = map.get(etatPC);

        if (idx == null) {
            // TODO enlever cette condition et changer idx en int pour qu'une exception soit levée.
            return "";
        } else {
            return babelDoc.getTextes(3).getTexte(idx).getDescription() + "\n";
        }
    }

    private void initializeLabels(ICTDocument babelDoc, DocumentData docData, Object[][] catLabels) {
        for (Object[] label : catLabels) {
            docData.addData((String) label[0], babelDoc.getTextes((Integer) label[1]).getTexte((Integer) label[2])
                    .getDescription());
        }
    }
}
