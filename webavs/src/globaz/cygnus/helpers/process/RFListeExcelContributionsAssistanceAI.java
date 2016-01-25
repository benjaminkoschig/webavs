package globaz.cygnus.helpers.process;

import globaz.corvus.utils.RETiersForJspUtils;
import globaz.cygnus.db.contributions.RFContributionsJointTiers;
import globaz.cygnus.vb.contributions.RFContributionsAssistanceAIUtils;
import globaz.globall.db.BSession;
import globaz.op.common.merge.IMergingContainer;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.List;
import ch.globaz.utils.excel.ExcelAbstractDocumentGenerator;

/**
 * @author PBA
 */
public class RFListeExcelContributionsAssistanceAI extends ExcelAbstractDocumentGenerator {

    private List<RFContributionsJointTiers> contributions;
    private BSession session;

    public RFListeExcelContributionsAssistanceAI(BSession session, List<RFContributionsJointTiers> contributions) {
        super();

        this.session = session;
        this.contributions = contributions;
    }

    private String getDescriptionTiers(RFContributionsJointTiers uneContribution) {
        StringBuilder description = new StringBuilder();

        description.append(uneContribution.getNss()).append(" / ");
        description.append(uneContribution.getNom()).append(" ").append(uneContribution.getPrenom()).append(" / ");
        description.append(uneContribution.getDateNaissance()).append(" / ");
        description.append(RETiersForJspUtils.getInstance(getSession())
                .getLibelleCourtSexe(uneContribution.getCsSexe()));

        return description.toString();
    }

    @Override
    public String getModelPath() {
        return "cygnus/excelml/contributions_assistance_ai.xml";
    }

    @Override
    public String getOutputName() {
        return "ContributionsAssistanceAI";
    }

    public BSession getSession() {
        return session;
    }

    @Override
    public IMergingContainer loadData() throws Exception {
        CommonExcelmlContainer data = new CommonExcelmlContainer();

        remplirValeursGlobale(data);

        for (RFContributionsJointTiers uneContribution : contributions) {
            data.put("description_tiers", getDescriptionTiers(uneContribution));
            data.put("date_reception_decision_CAAI", uneContribution.getDateReceptionDecisionCAAI());
            data.put("date_depot_demande_CAAI", uneContribution.getDateDepotDemandeCAAI());
            data.put("date_decision_CAAI", uneContribution.getDateDecisionAI());
            data.put("date_debut_periode_CAAI", uneContribution.getDateDebutPeriode());
            data.put("date_fin_periode_CAAI", uneContribution.getDateFinPeriode());
            data.put("date_debut_recours", uneContribution.getDateDebutRecours());
            data.put("date_fin_recours", uneContribution.getDateFinRecours());
            data.put("montant_CAAI", uneContribution.getMontantContribution());
            data.put("nombre_heures", uneContribution.getNombreHeures());
            data.put("code_API", uneContribution.getCodeAPI());
            data.put("type_API",
                    RFContributionsAssistanceAIUtils.getLibelleTypeAPI(getSession(), uneContribution.getCodeAPI()));
            data.put("degre_API",
                    RFContributionsAssistanceAIUtils.getLibelleDegreAPI(getSession(), uneContribution.getCodeAPI()));
            data.put("montant_API", uneContribution.getMontantAPI());
            data.put("remarque", uneContribution.getRemarque());
        }

        return data;
    }

    private void remplirValeursGlobale(CommonExcelmlContainer data) {
        data.put("label_description_tiers", session.getLabel("EXCEL_CAAI_DESCRIPTION_TIERS"));
        data.put("label_date_reception_decision_CAAI", session.getLabel("EXCEL_CAAI_DATE_RECEPTION_DECISION_CAAI"));
        data.put("label_date_depot_demande_CAAI", session.getLabel("EXCEL_CAAI_DATE_DEPOT_DEMANDE_CAAI"));
        data.put("label_date_decision_CAAI", session.getLabel("EXCEL_CAAI_DATE_DECISION_CAAI"));
        data.put("label_date_debut_periode_CAAI", session.getLabel("EXCEL_CAAI_DATE_DEBUT_PERIODE_CAAI"));
        data.put("label_date_fin_periode_CAAI", session.getLabel("EXCEL_CAAI_DATE_FIN_PERIODE_CAAI"));
        data.put("label_date_debut_recours", session.getLabel("EXCEL_CAAI_DATE_DEBUT_RECOURS"));
        data.put("label_date_fin_recours", session.getLabel("EXCEL_CAAI_DATE_FIN_RECOURS"));
        data.put("label_montant_CAAI", session.getLabel("EXCEL_CAAI_MONTANT_CAAI"));
        data.put("label_nombre_heures", session.getLabel("EXCEL_CAAI_NOMBRE_HEURES"));
        data.put("label_code_API", session.getLabel("EXCEL_CAAI_CODE_API"));
        data.put("label_type_API", session.getLabel("EXCEL_CAAI_TYPE_API"));
        data.put("label_degre_API", session.getLabel("EXCEL_CAAI_DEGRES_API"));
        data.put("label_montant_API", session.getLabel("EXCEL_CAAI_MONTANT_API"));
        data.put("label_remarque", session.getLabel("EXCEL_CAAI_REMARQUE"));
    }
}
