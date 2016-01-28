package ch.globaz.pegasus.businessimpl.utils.topazbuilder.decisions;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.topaz.datajuicer.DocumentData;

public abstract class AbstractDecisionBuilder {

    private static final int NOM_PRENOM_SIZE = 20;

    enum TYPE_DOCUMENT {

        COPIE("C"),
        ORIGINAL("O");

        String code;

        TYPE_DOCUMENT(String code) {
            this.code = code;
        }

        public static TYPE_DOCUMENT getForIsCopie(boolean isCopie) {
            if (isCopie) {
                return COPIE;
            } else {
                return ORIGINAL;
            }
        }
    }

    protected BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

    public JadePrintDocumentContainer mergeDataAndPubInfos(JadePrintDocumentContainer container, DocumentData data,
            JadePublishDocumentInfo pubInfo) {

        container.addDocument(data, pubInfo);

        return container;
    }

    /**
     * Ajout des propriétés de remplissage pixis
     * 
     * @param container
     *            l'instance de JadePrintDocumentContainer encapsulant les documents
     * @param data
     *            l'instance de DocumentData concercné
     * @param pubInfo
     *            l'instance de JadePublishInfo à laquelle définir les propriétés
     * @param idTiers
     *            l'idTiers servant de base à la méthode TIDocumentHelper.fill pour générer les propriétés en question
     * @return l'instance de JadePrintDocumentContainer modifié
     * @throws DecisionException
     *             si un problème survient lors du remplissage
     */
    public JadePrintDocumentContainer mergeDataAndPubInfosWithPixisFill(JadePrintDocumentContainer container,
            DocumentData data, JadePublishDocumentInfo pubInfo, JadePublishDocumentInfo pixisPropertiesPubInfos,
            PersonneEtendueComplexModel personne, TYPE_DOCUMENT type, String idTiersCourrier, String noDecision)
            throws DecisionException {

        pubInfo.setDocumentProperties(pixisPropertiesPubInfos.getDocumentProperties());
        fillForLigneTechnique(pubInfo, personne, type, idTiersCourrier, noDecision);
        container.addDocument(data, pubInfo);

        return container;
    }

    private void fillForLigneTechnique(JadePublishDocumentInfo pubInfo, PersonneEtendueComplexModel personne,
            TYPE_DOCUMENT type, String idTiersCourrier, String noDecision) {

        pubInfo.setDocumentProperty("AGLA_LT_PCRFM_DOC_TYPE_NUMBER",
                IPRConstantesExternes.PC_REF_INFOROM_DECISION_APRES_CALCUL);
        pubInfo.setDocumentProperty("AGLA_LT_PCRFM_TYPE_DECISION", type.code);
        pubInfo.setDocumentProperty("AGLA_LT_PCRFM_NSS_TIERS", personne.getPersonneEtendue().getNumAvsActuel());

        pubInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_TIERS", idTiersCourrier);

        pubInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_DECISION", noDecision);
        pubInfo.setDocumentProperty("AGLA_LT_PCRFM_NOM_TIERS",
                limiteStringSize(personne.getTiers().getDesignation1(), NOM_PRENOM_SIZE));
        pubInfo.setDocumentProperty("AGLA_LT_PCRFM_PRENOM_TIERS",
                limiteStringSize(personne.getTiers().getDesignation2(), NOM_PRENOM_SIZE));
    }

    private String limiteStringSize(String origin, int size) {
        StringBuilder s = new StringBuilder(origin);
        s.setLength(size);
        return s.toString().trim();
    }

}
