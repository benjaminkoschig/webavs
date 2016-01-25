package globaz.corvus.excel;

import globaz.corvus.db.restitution.RERestitutionsMouvements;
import globaz.corvus.db.restitution.RERestitutionsMouvementsManager;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.op.common.merge.IMergingContainer;
import globaz.webavs.common.CommonExcelmlContainer;
import java.math.BigDecimal;
import java.util.List;
import ch.globaz.utils.excel.ExcelAbstractDocumentGenerator;

public class REListeExcelRestitutionsMouvements extends ExcelAbstractDocumentGenerator {

    private static final String CRITERE_ROLE = "critere_role";
    private static final String CRITERE_RUBRIQUE = "critere_rubrique";
    private static final String ECRITURE_DATE = "ecriture_date";
    private static final String ECRITURE_LIBELLE = "ecriture_libelle";
    private static final String ECRITURE_MONTANT = "ecriture_montant";
    private static final String LABEL_ECRITURE_DATE = "label_ecriture_date";
    private static final String LABEL_ECRITURE_LIBELLE = "label_ecriture_LIBELLE";
    private static final String LABEL_ECRITURE_MONTANT = "label_ecriture_montant";
    private static final String LABEL_NOMPRENOM = "label_nomprenom";
    private static final String LABEL_NSS = "label_nss";
    private static final String LABEL_NUM_SECTION = "label_num_section";
    private static final String LABEL_RUBRIQUE = "label_rubrique";
    private static final String LABEL_RUBRIQUE_LIBELLE = "label_rubrique_libelle";
    private static final String LABEL_TITRE = "label_titre";
    private static final String LABEL_TOTAL = "label_total";
    private static final String NOMPRENOM = "nomprenom";
    private static final String NSS = "nss";
    private static final String NUM_SECTION = "num_section";
    private static final String RUBRIQUE = "rubrique";
    private static final String RUBRIQUE_LIBELLE = "rubrique_libelle";
    private static final String TOTAL = "total";

    private RERestitutionsMouvementsManager restitutions;
    private BSession session;

    /**
     * @param session
     * @param annee
     * @param familles
     */
    public REListeExcelRestitutionsMouvements(BSession session, RERestitutionsMouvementsManager restitutions) {
        super();

        this.session = session;
        this.restitutions = restitutions;
    }

    /**
     * @return
     */
    private String critereRubrique() {
        if (JadeStringUtil.isBlankOrZero(restitutions.getForIdExterneRubrique())) {
            return getSession().getLabel("EXCEL_RESTITUTIONS_MOUVEMENTS_CRITERES_RUBRIQUE_TOUTES");
        }
        return restitutions.getForIdExterneRubrique();
    }

    @Override
    public String getModelPath() {
        return "corvus/excelml/restitutions_mouvements.xml";
    }

    @Override
    public String getOutputName() {
        return "RestitutionsMouvements";
    }

    public BSession getSession() {
        return session;
    }

    @Override
    public IMergingContainer loadData() throws Exception {
        CommonExcelmlContainer data = new CommonExcelmlContainer();

        remplirValeursGlobale(data);

        List<RERestitutionsMouvements> liste = restitutions.getContainer();
        FWCurrency sum = new FWCurrency(0);

        for (RERestitutionsMouvements restit : liste) {
            data.put(REListeExcelRestitutionsMouvements.NSS, restit.getNss());
            data.put(REListeExcelRestitutionsMouvements.NOMPRENOM, restit.getNomPrenom());
            data.put(REListeExcelRestitutionsMouvements.NUM_SECTION, restit.getNumSection());
            data.put(REListeExcelRestitutionsMouvements.RUBRIQUE, restit.getRubriqueNumero());
            data.put(REListeExcelRestitutionsMouvements.RUBRIQUE_LIBELLE, restit.getRubriqueDescription());
            data.put(REListeExcelRestitutionsMouvements.ECRITURE_DATE, restit.getEcritureDate());
            data.put(REListeExcelRestitutionsMouvements.ECRITURE_LIBELLE, restit.getEcritureLibelle());

            FWCurrency solde = new FWCurrency(restit.getEcritureMontant());
            data.put(REListeExcelRestitutionsMouvements.ECRITURE_MONTANT, solde.toStringFormat());
            sum.add(solde);
        }

        // Total
        data.put(REListeExcelRestitutionsMouvements.LABEL_TOTAL,
                session.getLabel("EXCEL_RESTITUTIONS_MOUVEMENTS_TOTAL"));
        data.put(REListeExcelRestitutionsMouvements.TOTAL, sum.toStringFormat());

        return data;
    }

    /**
     * Renseigne les entetes de colonnes
     * 
     * @param data
     */
    private void remplirValeursGlobale(CommonExcelmlContainer data) {
        String sDateDe;
        String sDateA;
        try {
            BigDecimal decimal = new BigDecimal(Long.parseLong(restitutions.getDateDe()));
            JADate dateDe = new JADate(decimal);
            sDateDe = JACalendar.format(dateDe, JACalendar.FORMAT_DDsMMsYYYY);
        } catch (JAException e) {
            sDateDe = restitutions.getDateDe();
        }
        try {
            BigDecimal decimal = new BigDecimal(Long.parseLong(restitutions.getDateA()));
            JADate dateA = new JADate(decimal);
            sDateA = JACalendar.format(dateA, JACalendar.FORMAT_DDsMMsYYYY);
        } catch (JAException e) {
            sDateA = restitutions.getDateDe();
        }
        data.put(
                REListeExcelRestitutionsMouvements.LABEL_TITRE,
                session.getLabel("EXCEL_RESTITUTIONS_MOUVEMENTS_TITRE") + sDateDe + " "
                        + session.getLabel("EXCEL_RESTITUTIONS_MOUVEMENTS_TITRE_AU") + " " + sDateA);

        data.put(
                REListeExcelRestitutionsMouvements.CRITERE_ROLE,
                session.getLabel("EXCEL_RESTITUTIONS_MOUVEMENTS_CRITERES_ROLE")
                        + getSession().getCodeLibelle(restitutions.getForRole()));
        data.put(REListeExcelRestitutionsMouvements.CRITERE_RUBRIQUE,
                session.getLabel("EXCEL_RESTITUTIONS_MOUVEMENTS_CRITERES_RUBRIQUE") + critereRubrique());

        data.put(REListeExcelRestitutionsMouvements.LABEL_NSS,
                session.getLabel("EXCEL_RESTITUTIONS_MOUVEMENTS_LABEL_NSS"));
        data.put(REListeExcelRestitutionsMouvements.LABEL_NOMPRENOM,
                session.getLabel("EXCEL_RESTITUTIONS_MOUVEMENTS_LABEL_NOMPRENOM"));
        data.put(REListeExcelRestitutionsMouvements.LABEL_NUM_SECTION,
                session.getLabel("EXCEL_RESTITUTIONS_MOUVEMENTS_LABEL_NUM_SECTION"));
        data.put(REListeExcelRestitutionsMouvements.LABEL_RUBRIQUE,
                session.getLabel("EXCEL_RESTITUTIONS_MOUVEMENTS_LABEL_RUBRIQUE"));
        data.put(REListeExcelRestitutionsMouvements.LABEL_RUBRIQUE_LIBELLE,
                session.getLabel("EXCEL_RESTITUTIONS_MOUVEMENTS_LABEL_RUBRIQUE_LIBELLE"));
        data.put(REListeExcelRestitutionsMouvements.LABEL_ECRITURE_DATE,
                session.getLabel("EXCEL_RESTITUTIONS_MOUVEMENTS_LABEL_ECRITURE_DATE"));
        data.put(REListeExcelRestitutionsMouvements.LABEL_ECRITURE_LIBELLE,
                session.getLabel("EXCEL_RESTITUTIONS_MOUVEMENTS_LABEL_ECRITURE_LIBELLE"));
        data.put(REListeExcelRestitutionsMouvements.LABEL_ECRITURE_MONTANT,
                session.getLabel("EXCEL_RESTITUTIONS_MOUVEMENTS_LABEL_ECRITURE_MONTANT"));

    }
}
