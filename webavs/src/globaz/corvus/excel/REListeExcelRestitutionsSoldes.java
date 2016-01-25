package globaz.corvus.excel;

import globaz.corvus.db.restitution.REMotifsRestitutionsSoldes;
import globaz.corvus.db.restitution.REMotifsRestitutionsSoldesManager;
import globaz.corvus.db.restitution.RERestitutionsSoldes;
import globaz.corvus.db.restitution.RERestitutionsSoldesManager;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.op.common.merge.IMergingContainer;
import globaz.webavs.common.CommonExcelmlContainer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.utils.excel.ExcelAbstractDocumentGenerator;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;

public class REListeExcelRestitutionsSoldes extends ExcelAbstractDocumentGenerator {

    private static final String CONTENTIEUX = "contentieux";
    private static final String CRITERES = "zone_criteres";
    private static final String DATE_SECTION = "date_section";
    private static final String LABEL_CONTENTIEUX = "label_contentieux";
    private static final String LABEL_DATE_SECTION = "label_date_section";
    private static final String LABEL_NOMPRENOM = "label_nomprenom";
    private static final String LABEL_NSS = "label_nss";
    private static final String LABEL_NUM_SECTION = "label_num_section";
    private static final String LABEL_SOLDE = "label_solde";
    private static final String LABEL_TITRE = "label_titre";
    private static final String LABEL_TOTAL = "label_total";
    private static final String LABEL_MOTIF = "label_motif";
    private static final String LABEL_DU = "label_du";
    private static final String LABEL_AU = "label_au";
    private static final String LABEL_COMMENTAIRE = "label_commentaire";
    private static final String NOMPRENOM = "nomprenom";
    private static final String NSS = "nss";
    private static final String NUM_SECTION = "num_section";
    private static final String MOTIF = "motif";
    private static final String DU = "du";
    private static final String AU = "au";
    private static final String COMMENTAIRE = "commentaire";

    private static final String SOLDE = "solde";
    private static final String TOTAL = "total";
    private static final String MODEL_PATH = "corvus/excelml/restitutions_soldes.xml";
    private static final String OUTPUT_NAME = "RestitutionsSoldes";

    private RERestitutionsSoldesManager restitutionsManager;
    private REMotifsRestitutionsSoldesManager motifsRestitutionsSoldesManager;
    private BSession session;

    /**
     * @param session
     * @param annee
     * @param familles
     */
    public REListeExcelRestitutionsSoldes(BSession session, RERestitutionsSoldesManager restitutions,
            REMotifsRestitutionsSoldesManager motifs) {
        super();

        this.session = session;
        restitutionsManager = restitutions;
        motifsRestitutionsSoldesManager = motifs;
    }

    private List<RERestitutionsSoldes> chargerRestitutionSoldes() {
        List<RERestitutionsSoldes> restitutions = new ArrayList<RERestitutionsSoldes>();

        for (int i = 0; i < restitutionsManager.size(); i++) {
            RERestitutionsSoldes uneRestitution = (RERestitutionsSoldes) restitutionsManager.get(i);
            restitutions.add(uneRestitution);
        }

        ajoutMotifsRestitutions(restitutions);

        return restitutions;
    }

    @SuppressWarnings({ "unchecked" })
    private void ajoutMotifsRestitutions(List<RERestitutionsSoldes> restitutions) {

        // Groupement des motifs par section
        ImmutableListMultimap<String, REMotifsRestitutionsSoldes> motifsBySection = Multimaps.index(
                motifsRestitutionsSoldesManager, new Function<REMotifsRestitutionsSoldes, String>() {

                    @Override
                    public String apply(REMotifsRestitutionsSoldes motifs) {
                        return motifs.getIdSection();
                    }

                });

        for (RERestitutionsSoldes restitution : restitutions) {
            List<REMotifsRestitutionsSoldes> motifs = motifsBySection.get(restitution.getIdSection());
            restitution.setMotifsRestitutions(motifs);
        }
    }

    @Override
    public String getModelPath() {
        return MODEL_PATH;
    }

    @Override
    public String getOutputName() {
        return OUTPUT_NAME;
    }

    public BSession getSession() {
        return session;
    }

    @Override
    public IMergingContainer loadData() throws Exception {
        CommonExcelmlContainer data = new CommonExcelmlContainer();

        remplirValeursGlobale(data);

        List<RERestitutionsSoldes> liste = chargerRestitutionSoldes();
        trierRestitutionsParNomPrenom(liste);

        FWCurrency sum = new FWCurrency(0);

        for (RERestitutionsSoldes restit : liste) {

            if (restit.getMotifsRestitutions().isEmpty()) {
                data.put(REListeExcelRestitutionsSoldes.NSS, restit.getNss());
                data.put(REListeExcelRestitutionsSoldes.NOMPRENOM, restit.getNomPrenom());
                data.put(REListeExcelRestitutionsSoldes.NUM_SECTION, restit.getNumSection());
                data.put(REListeExcelRestitutionsSoldes.DATE_SECTION, restit.getDateSection());
                data.put(REListeExcelRestitutionsSoldes.CONTENTIEUX, restit.getContentieux());

                data.put(REListeExcelRestitutionsSoldes.MOTIF, "");
                data.put(REListeExcelRestitutionsSoldes.DU, "");
                data.put(REListeExcelRestitutionsSoldes.AU, "");
                data.put(REListeExcelRestitutionsSoldes.COMMENTAIRE, "");

                FWCurrency solde = new FWCurrency(restit.getSolde());
                data.put(REListeExcelRestitutionsSoldes.SOLDE, solde.toStringFormat());
                sum.add(solde);
            } else {

                // cpmteurs pour gérer la dernière itératons de smotifs, celle ou sera ajouté le total de la section
                int nombreMotifsPourSection = restit.getMotifsRestitutions().size();
                int cptMotif = 1;

                for (REMotifsRestitutionsSoldes motifs : restit.getMotifsRestitutions()) {
                    data.put(REListeExcelRestitutionsSoldes.NSS, restit.getNss());
                    data.put(REListeExcelRestitutionsSoldes.NOMPRENOM, restit.getNomPrenom());
                    data.put(REListeExcelRestitutionsSoldes.NUM_SECTION, restit.getNumSection());
                    data.put(REListeExcelRestitutionsSoldes.DATE_SECTION, restit.getDateSection());
                    data.put(REListeExcelRestitutionsSoldes.CONTENTIEUX, restit.getContentieux());

                    data.put(REListeExcelRestitutionsSoldes.MOTIF, motifs.getMotif());
                    data.put(REListeExcelRestitutionsSoldes.DU, motifs.getDateDebut());
                    data.put(REListeExcelRestitutionsSoldes.AU, motifs.getDateFin());
                    data.put(REListeExcelRestitutionsSoldes.COMMENTAIRE, motifs.getCommentaire());

                    // Si c'est le dernier motifs
                    if (cptMotif == nombreMotifsPourSection) {
                        FWCurrency solde = new FWCurrency(restit.getSolde());
                        data.put(REListeExcelRestitutionsSoldes.SOLDE, solde.toStringFormat());
                        sum.add(solde);
                    } else {
                        data.put(REListeExcelRestitutionsSoldes.SOLDE, "");
                    }
                    cptMotif++;
                }

            }

        }

        // Total
        data.put(REListeExcelRestitutionsSoldes.LABEL_TOTAL, session.getLabel("EXCEL_RESTITUTIONS_SOLDES_TOTAL"));
        data.put(REListeExcelRestitutionsSoldes.TOTAL, sum.toStringFormat());

        return data;
    }

    /**
     * Renseigne les entetes de colonnes
     * 
     * @param data
     */
    private void remplirValeursGlobale(CommonExcelmlContainer data) {
        String sDate;
        try {
            BigDecimal decimal = new BigDecimal(Long.parseLong(restitutionsManager.getDateValeur()));
            JADate date = new JADate(decimal);
            sDate = JACalendar.format(date, JACalendar.FORMAT_DDsMMsYYYY);
        } catch (JAException e) {
            sDate = restitutionsManager.getDateValeur();
        }

        data.put(REListeExcelRestitutionsSoldes.LABEL_TITRE, session.getLabel("EXCEL_RESTITUTIONS_SOLDES_TITRE")
                + sDate);

        data.put(REListeExcelRestitutionsSoldes.CRITERES, session.getLabel("EXCEL_RESTITUTIONS_SOLDES_CRITERES_ROLE")
                + getSession().getCodeLibelle(restitutionsManager.getForRole()));

        data.put(REListeExcelRestitutionsSoldes.LABEL_NSS, session.getLabel("EXCEL_RESTITUTIONS_SOLDES_LABEL_NSS"));
        data.put(REListeExcelRestitutionsSoldes.LABEL_NOMPRENOM,
                session.getLabel("EXCEL_RESTITUTIONS_SOLDES_LABEL_NOMPRENOM"));
        data.put(REListeExcelRestitutionsSoldes.LABEL_NUM_SECTION,
                session.getLabel("EXCEL_RESTITUTIONS_SOLDES_LABEL_NUM_SECTION"));
        data.put(REListeExcelRestitutionsSoldes.LABEL_DATE_SECTION,
                session.getLabel("EXCEL_RESTITUTIONS_SOLDES_LABEL_DATE_SECTION"));
        data.put(REListeExcelRestitutionsSoldes.LABEL_CONTENTIEUX,
                session.getLabel("EXCEL_RESTITUTIONS_SOLDES_LABEL_CONTENTIEUX"));
        data.put(REListeExcelRestitutionsSoldes.LABEL_SOLDE, session.getLabel("EXCEL_RESTITUTIONS_SOLDES_LABEL_SOLDE"));
        data.put(REListeExcelRestitutionsSoldes.LABEL_MOTIF, session.getLabel("EXCEL_RESTITUTIONS_SOLDES_LABEL_MOTIF"));
        data.put(REListeExcelRestitutionsSoldes.LABEL_DU, session.getLabel("EXCEL_RESTITUTIONS_SOLDES_LABEL_DU"));
        data.put(REListeExcelRestitutionsSoldes.LABEL_AU, session.getLabel("EXCEL_RESTITUTIONS_SOLDES_LABEL_AU"));
        data.put(REListeExcelRestitutionsSoldes.LABEL_COMMENTAIRE,
                session.getLabel("EXCEL_RESTITUTIONS_SOLDES_LABEL_COMMENTAIRES"));

    }

    private void trierRestitutionsParNomPrenom(List<RERestitutionsSoldes> liste) {
        Collections.sort(liste, new Comparator<RERestitutionsSoldes>() {
            @Override
            public int compare(RERestitutionsSoldes restitution1, RERestitutionsSoldes restitution2) {
                if ((restitution1 != null) && !JadeStringUtil.isBlank(restitution1.getNomPrenom())
                        && (restitution2 != null) && !JadeStringUtil.isBlank(restitution2.getNomPrenom())) {
                    return restitution1.getNomPrenom().compareTo(restitution2.getNomPrenom());
                }
                return -1;
            }
        });
    }
}
