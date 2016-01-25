package globaz.aquila.process.batch.utils;

import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.process.COProcessContentieuxInfo;
import globaz.aquila.print.COJournalContentieux_DS;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CATypeSection;
import java.util.HashMap;
import java.util.Map;

public class COImprimerListeDeclenchement {

    private COJournalContentieux_DS report;

    private Map<String, CATypeSection> typeSectionCache;

    public COImprimerListeDeclenchement(String dateSurDocument, String dateReference, boolean previsionnel) {
        report = new COJournalContentieux_DS();
        report.setDate(dateSurDocument);
        report.setDateReference(dateReference);
        report.setModePrevisionnel(previsionnel);
    }

    public COJournalContentieux_DS getReport() {
        return report;
    }

    /**
     * Ajoute une ligne pour un nouveau cas de contentieux créé.
     * 
     * @param session
     * @param contentieux
     * @param ctxInfo
     * @param transition
     * @throws Exception
     */
    public void insertRowCasAmorce(BSession session, COContentieux contentieux, COProcessContentieuxInfo ctxInfo,
            COTransition transition) throws Exception {
        // Informer si le montant de base (solde-compensation/paiement) est
        // inférieur ou égal à zéro, cas douteux
        FWCurrency fMontant = contentieux.getSection().getSoldeToCurrency();
        fMontant.sub(contentieux.getSection().getPmtCmp());

        if ((fMontant.isNegative() || fMontant.isZero())
                && !APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(contentieux.getSection().getIdTypeSection())) {
            report.insertRow(ctxInfo, loadDescTypeSection(session, ctxInfo), transition.getEtape(),
                    session.getLabel("CONTENTIEUX_CAS_DOUTEUX"));
        } else {
            report.insertRow(ctxInfo, loadDescTypeSection(session, ctxInfo), transition.getEtape(), "");
        }
    }

    /**
     * Ajoute une ligne pour une transition effectuée avec succès.
     * 
     * @param session
     * @param contentieux
     * @param transition
     * @param totalTaxeListe
     * @throws Exception
     */
    public void insertRowTransitionEffectue(BSession session, COContentieux contentieux, COTransition transition,
            FWCurrency totalTaxeListe, String messageId) {
        String commentaire = "";

        if (messageId.equals("SEUIL_MINIMAL_INFERIEUR")) {
            if (JadeStringUtil.parseFloat(contentieux.getSection().getSolde(), 0) < JadeStringUtil.parseFloat(
                    transition.getEtapeSuivante().getMontantMinimal(), 0)) {
                commentaire += session.getLabel("SEUIL_MINIMAL_INFERIEUR") + " ("
                        + JadeStringUtil.parseFloat(transition.getEtapeSuivante().getMontantMinimal(), 0) + ")";
            }
        } else if (messageId.equals("RDP_TAXESRESTANTES_NON_ACCEPTER")) {
            commentaire += session.getLabel("RDP_TAXESRESTANTES_NON_ACCEPTER");
        } else if (messageId.equals("RDP_IMEXISTANT_EXTRAITCOMPTE")) {
            commentaire += session.getLabel("RDP_IMEXISTANT_EXTRAITCOMPTE");
        } else {
            if (session.hasErrors()) {
                commentaire += session.getErrors().toString();
            } else {
                commentaire += messageId;
            }

        }

        report.insertRow(contentieux.getSection(), transition.getEtapeSuivante(), contentieux
                .getProchaineDateDeclenchement(), totalTaxeListe.toString(), contentieux.getSection().getSolde(),
                commentaire);
    }

    /**
     * Charge la liste des types de sections en cache.
     * 
     * @param session
     * @param ctxInfo
     * @return
     * @throws Exception
     */
    private String loadDescTypeSection(BSession session, COProcessContentieuxInfo ctxInfo) throws Exception {
        if (typeSectionCache == null) {
            typeSectionCache = new HashMap<String, CATypeSection>();
        }

        CATypeSection typeSection = typeSectionCache.get(ctxInfo.getIdTypeSection());

        if (typeSection == null) {
            typeSection = new CATypeSection();
            typeSection.setIdTypeSection(ctxInfo.getIdTypeSection());
            typeSection.setSession(session);
            typeSection.retrieve();

            typeSectionCache.put(ctxInfo.getIdTypeSection(), typeSection);
        }

        typeSection.getSectionDescriptor().setSection(ctxInfo.getIdExterneSection(), ctxInfo.getIdTypeSection(),
                ctxInfo.getCategorieSection(), ctxInfo.getDateSection(), ctxInfo.getDateDebutPeriodeSection(),
                ctxInfo.getDateFinPeriodeSection());

        return typeSection.getSectionDescriptor().getDescription();
    }
}
