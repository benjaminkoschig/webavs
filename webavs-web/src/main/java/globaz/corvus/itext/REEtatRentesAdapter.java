package globaz.corvus.itext;

import globaz.corvus.db.rentesaccordees.REEtatRentes;
import globaz.corvus.db.rentesaccordees.REEtatRentesManager;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

public class REEtatRentesAdapter {

    private final String forMois;
    private final REEtatRentesManager mgr = new REEtatRentesManager();

    private final FWCurrency montant_AI_1_TOTAL_RE = new FWCurrency("0.0");
    private final FWCurrency montant_AI_1_TOTAL_RO = new FWCurrency("0.0");
    private final FWCurrency montant_AI_2_TOTAL_RE = new FWCurrency("0.0");
    private final FWCurrency montant_AI_2_TOTAL_RO = new FWCurrency("0.0");
    private final FWCurrency montant_AI_3_TOTAL_RE = new FWCurrency("0.0");
    private final FWCurrency montant_AI_3_TOTAL_RO = new FWCurrency("0.0");
    private final FWCurrency montant_AI_4_TOTAL_RE = new FWCurrency("0.0");
    private final FWCurrency montant_AI_4_TOTAL_RO = new FWCurrency("0.0");
    private final FWCurrency montant_AI_50 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_50_1 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_50_2 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_50_3 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_50_4 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_52 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_53 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_53_1 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_53_2 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_53_3 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_53_4 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_54 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_54_1 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_54_2 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_54_3 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_54_4 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_55 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_55_1 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_55_2 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_55_3 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_55_4 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_56 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_56_1 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_56_2 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_56_3 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_56_4 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_70 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_70_1 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_70_2 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_70_3 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_70_4 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_72 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_72_1 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_73 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_73_1 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_73_2 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_73_3 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_73_4 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_74 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_74_1 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_74_2 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_74_3 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_74_4 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_75 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_75_1 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_75_2 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_75_3 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_75_4 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_76 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_76_1 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_76_2 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_76_3 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_76_4 = new FWCurrency("0.0");
    private final FWCurrency montant_AI_TOTAL_RE = new FWCurrency("0.0");
    private final FWCurrency montant_AI_TOTAL_RO = new FWCurrency("0.0");
    // API
    private final FWCurrency montant_API_81 = new FWCurrency("0.0");
    private final FWCurrency montant_API_82 = new FWCurrency("0.0");
    private final FWCurrency montant_API_83 = new FWCurrency("0.0");
    private final FWCurrency montant_API_84 = new FWCurrency("0.0");
    private final FWCurrency montant_API_85 = new FWCurrency("0.0");
    private final FWCurrency montant_API_86 = new FWCurrency("0.0");
    private final FWCurrency montant_API_87 = new FWCurrency("0.0");
    private final FWCurrency montant_API_88 = new FWCurrency("0.0");
    private final FWCurrency montant_API_89 = new FWCurrency("0.0");
    private final FWCurrency montant_API_91 = new FWCurrency("0.0");
    private final FWCurrency montant_API_92 = new FWCurrency("0.0");
    private final FWCurrency montant_API_93 = new FWCurrency("0.0");
    private final FWCurrency montant_API_94 = new FWCurrency("0.0");
    private final FWCurrency montant_API_95 = new FWCurrency("0.0");
    private final FWCurrency montant_API_96 = new FWCurrency("0.0");
    private final FWCurrency montant_API_97 = new FWCurrency("0.0");
    private final FWCurrency montant_API_TOTAL_AI = new FWCurrency("0.0");
    private final FWCurrency montant_API_TOTAL_AVS = new FWCurrency("0.0");
    // AVS
    private final FWCurrency montant_AVS_10 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_13 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_14 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_15 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_16 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_20 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_23 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_24 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_25 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_26 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_33 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_34 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_35 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_36 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_43 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_44 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_45 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_46 = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_TOTAL_RE = new FWCurrency("0.0");
    private final FWCurrency montant_AVS_TOTAL_RO = new FWCurrency("0.0");

    private final FWCurrency montant_PC_110 = new FWCurrency("0.0");
    private final FWCurrency montant_PC_113 = new FWCurrency("0.0");
    private final FWCurrency montant_PC_150 = new FWCurrency("0.0");
    private final FWCurrency montant_PC_198 = new FWCurrency("0.0");
    private final FWCurrency montant_PC_199 = new FWCurrency("0.0");
    private final FWCurrency montant_PC_TOTAL = new FWCurrency("0.0");

    private final FWCurrency montant_RFM_210 = new FWCurrency("0.0");
    private final FWCurrency montant_RFM_213 = new FWCurrency("0.0");
    private final FWCurrency montant_RFM_250 = new FWCurrency("0.0");
    private final FWCurrency montant_RFM_TOTAL = new FWCurrency("0.0");

    private int nb_AI_1_TOTAL_RE = 0;
    private int nb_AI_1_TOTAL_RO = 0;
    private int nb_AI_2_TOTAL_RE = 0;
    private int nb_AI_2_TOTAL_RO = 0;
    private int nb_AI_3_TOTAL_RE = 0;
    private int nb_AI_3_TOTAL_RO = 0;
    private int nb_AI_4_TOTAL_RE = 0;
    private int nb_AI_4_TOTAL_RO = 0;

    private int nb_AI_50 = 0;
    private int nb_AI_50_1 = 0;
    private int nb_AI_50_2 = 0;
    private int nb_AI_50_3 = 0;
    private int nb_AI_50_4 = 0;
    private int nb_AI_52 = 0;
    private int nb_AI_53 = 0;
    private int nb_AI_53_1 = 0;
    private int nb_AI_53_2 = 0;
    private int nb_AI_53_3 = 0;
    private int nb_AI_53_4 = 0;
    private int nb_AI_54 = 0;
    private int nb_AI_54_1 = 0;
    private int nb_AI_54_2 = 0;
    private int nb_AI_54_3 = 0;
    private int nb_AI_54_4 = 0;
    private int nb_AI_55 = 0;
    private int nb_AI_55_1 = 0;
    private int nb_AI_55_2 = 0;
    private int nb_AI_55_3 = 0;
    private int nb_AI_55_4 = 0;
    private int nb_AI_56 = 0;
    private int nb_AI_56_1 = 0;
    private int nb_AI_56_2 = 0;
    private int nb_AI_56_3 = 0;
    private int nb_AI_56_4 = 0;

    private int nb_AI_70 = 0;
    private int nb_AI_70_1 = 0;
    private int nb_AI_70_2 = 0;
    private int nb_AI_70_3 = 0;
    private int nb_AI_70_4 = 0;
    private int nb_AI_72 = 0;
    private int nb_AI_72_1 = 0;
    private int nb_AI_73 = 0;
    private int nb_AI_73_1 = 0;
    private int nb_AI_73_2 = 0;
    private int nb_AI_73_3 = 0;
    private int nb_AI_73_4 = 0;
    private int nb_AI_74 = 0;
    private int nb_AI_74_1 = 0;
    private int nb_AI_74_2 = 0;
    private int nb_AI_74_3 = 0;
    private int nb_AI_74_4 = 0;
    private int nb_AI_75 = 0;
    private int nb_AI_75_1 = 0;
    private int nb_AI_75_2 = 0;
    private int nb_AI_75_3 = 0;
    private int nb_AI_75_4 = 0;
    private int nb_AI_76 = 0;
    private int nb_AI_76_1 = 0;
    private int nb_AI_76_2 = 0;
    private int nb_AI_76_3 = 0;
    private int nb_AI_76_4 = 0;

    private int nb_AI_TOTAL_RE = 0;
    private int nb_AI_TOTAL_RO = 0;

    private int nb_API_81 = 0;
    private int nb_API_82 = 0;
    private int nb_API_83 = 0;
    private int nb_API_84 = 0;
    private int nb_API_85 = 0;
    private int nb_API_86 = 0;
    private int nb_API_87 = 0;
    private int nb_API_88 = 0;
    private int nb_API_89 = 0;
    private int nb_API_91 = 0;
    private int nb_API_92 = 0;
    private int nb_API_93 = 0;
    private int nb_API_94 = 0;
    private int nb_API_95 = 0;
    private int nb_API_96 = 0;
    private int nb_API_97 = 0;

    private int nb_API_TOTAL_AI = 0;
    private int nb_API_TOTAL_AVS = 0;

    private int nb_AVS_10 = 0;
    private int nb_AVS_13 = 0;
    private int nb_AVS_14 = 0;
    private int nb_AVS_15 = 0;
    private int nb_AVS_16 = 0;
    private int nb_AVS_20 = 0;
    private int nb_AVS_23 = 0;
    private int nb_AVS_24 = 0;
    private int nb_AVS_25 = 0;
    private int nb_AVS_26 = 0;
    private int nb_AVS_33 = 0;
    private int nb_AVS_34 = 0;
    private int nb_AVS_35 = 0;
    private int nb_AVS_36 = 0;
    private int nb_AVS_43 = 0;
    private int nb_AVS_44 = 0;
    private int nb_AVS_45 = 0;
    private int nb_AVS_46 = 0;

    private int nb_AVS_TOTAL_RE = 0;
    private int nb_AVS_TOTAL_RO = 0;

    private int nb_PC_110 = 0;
    private int nb_PC_113 = 0;
    private int nb_PC_150 = 0;
    private int nb_PC_198 = 0;
    private int nb_PC_199 = 0;
    private int nb_PC_TOTAL = 0;

    private int nb_RFM_210 = 0;
    private int nb_RFM_213 = 0;
    private int nb_RFM_250 = 0;
    private int nb_RFM_TOTAL = 0;

    private final BSession session;

    public REEtatRentesAdapter(final BSession session, final String forMoisAnneeComptable) {
        this.session = session;
        forMois = forMoisAnneeComptable;
    }

    public void addMontant_AI_1_TOTAL_RE(final FWCurrency currency) {
        montant_AI_1_TOTAL_RE.add(currency);
        addMontant_AI_TOTAL_RE(currency);
    }

    public void addMontant_AI_1_TOTAL_RO(final FWCurrency currency) {
        montant_AI_1_TOTAL_RO.add(currency);
        addMontant_AI_TOTAL_RO(currency);
    }

    public void addMontant_AI_2_TOTAL_RE(final FWCurrency currency) {
        montant_AI_2_TOTAL_RE.add(currency);
        addMontant_AI_TOTAL_RE(currency);
    }

    public void addMontant_AI_2_TOTAL_RO(final FWCurrency currency) {
        montant_AI_2_TOTAL_RO.add(currency);
        addMontant_AI_TOTAL_RO(currency);
    }

    public void addMontant_AI_3_TOTAL_RE(final FWCurrency currency) {
        montant_AI_3_TOTAL_RE.add(currency);
        addMontant_AI_TOTAL_RE(currency);
    }

    public void addMontant_AI_3_TOTAL_RO(final FWCurrency currency) {
        montant_AI_3_TOTAL_RO.add(currency);
        addMontant_AI_TOTAL_RO(currency);
    }

    public void addMontant_AI_4_TOTAL_RE(final FWCurrency currency) {
        montant_AI_4_TOTAL_RE.add(currency);
        addMontant_AI_TOTAL_RE(currency);
    }

    public void addMontant_AI_4_TOTAL_RO(final FWCurrency currency) {
        montant_AI_4_TOTAL_RO.add(currency);
        addMontant_AI_TOTAL_RO(currency);
    }

    public void addMontant_AI_50_1(final FWCurrency currency) {
        montant_AI_50_1.add(currency);
        montant_AI_50.add(currency);
        addMontant_AI_1_TOTAL_RO(currency);
    }

    public void addMontant_AI_50_2(final FWCurrency currency) {
        montant_AI_50_2.add(currency);
        montant_AI_50.add(currency);
        addMontant_AI_2_TOTAL_RO(currency);
    }

    public void addMontant_AI_50_3(final FWCurrency currency) {
        montant_AI_50_3.add(currency);
        montant_AI_50.add(currency);
        addMontant_AI_3_TOTAL_RO(currency);
    }

    public void addMontant_AI_50_4(final FWCurrency currency) {
        montant_AI_50_4.add(currency);
        montant_AI_50.add(currency);
        addMontant_AI_4_TOTAL_RO(currency);
    }

    public void addMontant_AI_52(final FWCurrency currency) {
        montant_AI_52.add(currency);
        addMontant_AI_1_TOTAL_RO(currency);
    }

    public void addMontant_AI_53_1(final FWCurrency currency) {
        montant_AI_53_1.add(currency);
        montant_AI_53.add(currency);
        addMontant_AI_1_TOTAL_RO(currency);
    }

    public void addMontant_AI_53_2(final FWCurrency currency) {
        montant_AI_53_2.add(currency);
        montant_AI_53.add(currency);
        addMontant_AI_2_TOTAL_RO(currency);
    }

    public void addMontant_AI_53_3(final FWCurrency currency) {
        montant_AI_53_3.add(currency);
        montant_AI_53.add(currency);
        addMontant_AI_3_TOTAL_RO(currency);
    }

    public void addMontant_AI_53_4(final FWCurrency currency) {
        montant_AI_53_4.add(currency);
        montant_AI_53.add(currency);
        addMontant_AI_4_TOTAL_RO(currency);
    }

    public void addMontant_AI_54_1(final FWCurrency currency) {
        montant_AI_54_1.add(currency);
        montant_AI_54.add(currency);
        addMontant_AI_1_TOTAL_RO(currency);
    }

    public void addMontant_AI_54_2(final FWCurrency currency) {
        montant_AI_54_2.add(currency);
        montant_AI_54.add(currency);
        addMontant_AI_2_TOTAL_RO(currency);
    }

    public void addMontant_AI_54_3(final FWCurrency currency) {
        montant_AI_54_3.add(currency);
        montant_AI_54.add(currency);
        addMontant_AI_3_TOTAL_RO(currency);
    }

    public void addMontant_AI_54_4(final FWCurrency currency) {
        montant_AI_54_4.add(currency);
        montant_AI_54.add(currency);
        addMontant_AI_4_TOTAL_RO(currency);
    }

    public void addMontant_AI_55_1(final FWCurrency currency) {
        montant_AI_55_1.add(currency);
        montant_AI_55.add(currency);
        addMontant_AI_1_TOTAL_RO(currency);
    }

    public void addMontant_AI_55_2(final FWCurrency currency) {
        montant_AI_55_2.add(currency);
        montant_AI_55.add(currency);
        addMontant_AI_2_TOTAL_RO(currency);
    }

    public void addMontant_AI_55_3(final FWCurrency currency) {
        montant_AI_55_3.add(currency);
        montant_AI_55.add(currency);
        addMontant_AI_3_TOTAL_RO(currency);
    }

    public void addMontant_AI_55_4(final FWCurrency currency) {
        montant_AI_55_4.add(currency);
        montant_AI_55.add(currency);
        addMontant_AI_4_TOTAL_RO(currency);
    }

    public void addMontant_AI_56_1(final FWCurrency currency) {
        montant_AI_56_1.add(currency);
        montant_AI_56.add(currency);
        addMontant_AI_1_TOTAL_RO(currency);
    }

    public void addMontant_AI_56_2(final FWCurrency currency) {
        montant_AI_56_2.add(currency);
        montant_AI_56.add(currency);
        addMontant_AI_2_TOTAL_RO(currency);
    }

    public void addMontant_AI_56_3(final FWCurrency currency) {
        montant_AI_56_3.add(currency);
        montant_AI_56.add(currency);
        addMontant_AI_3_TOTAL_RO(currency);
    }

    public void addMontant_AI_56_4(final FWCurrency currency) {
        montant_AI_56_4.add(currency);
        montant_AI_56.add(currency);
        addMontant_AI_4_TOTAL_RO(currency);
    }

    public void addMontant_AI_70_1(final FWCurrency currency) {
        montant_AI_70_1.add(currency);
        montant_AI_70.add(currency);
        addMontant_AI_1_TOTAL_RE(currency);
    }

    public void addMontant_AI_70_2(final FWCurrency currency) {
        montant_AI_70_2.add(currency);
        montant_AI_70.add(currency);
        addMontant_AI_2_TOTAL_RE(currency);
    }

    public void addMontant_AI_70_3(final FWCurrency currency) {
        montant_AI_70_3.add(currency);
        montant_AI_70.add(currency);
        addMontant_AI_3_TOTAL_RE(currency);
    }

    public void addMontant_AI_70_4(final FWCurrency currency) {
        montant_AI_70_4.add(currency);
        montant_AI_70.add(currency);
        addMontant_AI_4_TOTAL_RE(currency);
    }

    public void addMontant_AI_72_1(final FWCurrency currency) {
        montant_AI_72_1.add(currency);
        montant_AI_72.add(currency);
        addMontant_AI_1_TOTAL_RE(currency);
    }

    public void addMontant_AI_73_1(final FWCurrency currency) {
        montant_AI_73_1.add(currency);
        montant_AI_73.add(currency);
        addMontant_AI_1_TOTAL_RE(currency);
    }

    public void addMontant_AI_73_2(final FWCurrency currency) {
        montant_AI_73_2.add(currency);
        montant_AI_73.add(currency);
        addMontant_AI_2_TOTAL_RE(currency);
    }

    public void addMontant_AI_73_3(final FWCurrency currency) {
        montant_AI_73_3.add(currency);
        montant_AI_73.add(currency);
        addMontant_AI_3_TOTAL_RE(currency);
    }

    public void addMontant_AI_73_4(final FWCurrency currency) {
        montant_AI_73_4.add(currency);
        montant_AI_73.add(currency);
        addMontant_AI_4_TOTAL_RE(currency);
    }

    public void addMontant_AI_74_1(final FWCurrency currency) {
        montant_AI_74_1.add(currency);
        montant_AI_74.add(currency);
        addMontant_AI_1_TOTAL_RE(currency);
    }

    public void addMontant_AI_74_2(final FWCurrency currency) {
        montant_AI_74_2.add(currency);
        montant_AI_74.add(currency);
        addMontant_AI_2_TOTAL_RE(currency);
    }

    public void addMontant_AI_74_3(final FWCurrency currency) {
        montant_AI_74_3.add(currency);
        montant_AI_74.add(currency);
        addMontant_AI_3_TOTAL_RE(currency);
    }

    public void addMontant_AI_74_4(final FWCurrency currency) {
        montant_AI_74_4.add(currency);
        montant_AI_74.add(currency);
        addMontant_AI_4_TOTAL_RE(currency);
    }

    public void addMontant_AI_75_1(final FWCurrency currency) {
        montant_AI_75_1.add(currency);
        montant_AI_75.add(currency);
        addMontant_AI_1_TOTAL_RE(currency);
    }

    public void addMontant_AI_75_2(final FWCurrency currency) {
        montant_AI_75_2.add(currency);
        montant_AI_75.add(currency);
        addMontant_AI_2_TOTAL_RE(currency);
    }

    public void addMontant_AI_75_3(final FWCurrency currency) {
        montant_AI_75_3.add(currency);
        montant_AI_75.add(currency);
        addMontant_AI_3_TOTAL_RE(currency);
    }

    public void addMontant_AI_75_4(final FWCurrency currency) {
        montant_AI_75_4.add(currency);
        montant_AI_75.add(currency);
        addMontant_AI_4_TOTAL_RE(currency);
    }

    public void addMontant_AI_76_1(final FWCurrency currency) {
        montant_AI_76_1.add(currency);
        montant_AI_76.add(currency);
        addMontant_AI_1_TOTAL_RE(currency);
    }

    public void addMontant_AI_76_2(final FWCurrency currency) {
        montant_AI_76_2.add(currency);
        montant_AI_76.add(currency);
        addMontant_AI_2_TOTAL_RE(currency);
    }

    public void addMontant_AI_76_3(final FWCurrency currency) {
        montant_AI_76_3.add(currency);
        montant_AI_76.add(currency);
        addMontant_AI_3_TOTAL_RE(currency);
    }

    public void addMontant_AI_76_4(final FWCurrency currency) {
        montant_AI_76_4.add(currency);
        montant_AI_76.add(currency);
        addMontant_AI_4_TOTAL_RE(currency);
    }

    public void addMontant_AI_TOTAL_RE(final FWCurrency currency) {
        montant_AI_TOTAL_RE.add(currency);
    }

    public void addMontant_AI_TOTAL_RO(final FWCurrency currency) {
        montant_AI_TOTAL_RO.add(currency);
    }

    public void addMontant_API_81(final FWCurrency currency) {
        montant_API_81.add(currency);
        addMontant_API_TOTAL_AI(currency);
    }

    public void addMontant_API_82(final FWCurrency currency) {
        montant_API_82.add(currency);
        addMontant_API_TOTAL_AI(currency);
    }

    public void addMontant_API_83(final FWCurrency currency) {
        montant_API_83.add(currency);
        addMontant_API_TOTAL_AI(currency);
    }

    public void addMontant_API_84(final FWCurrency currency) {
        montant_API_84.add(currency);
        addMontant_API_TOTAL_AI(currency);
    }

    public void addMontant_API_85(final FWCurrency currency) {
        montant_API_85.add(currency);
        addMontant_API_TOTAL_AVS(currency);
    }

    public void addMontant_API_86(final FWCurrency currency) {
        montant_API_86.add(currency);
        addMontant_API_TOTAL_AVS(currency);
    }

    public void addMontant_API_87(final FWCurrency currency) {
        montant_API_87.add(currency);
        addMontant_API_TOTAL_AVS(currency);
    }

    public void addMontant_API_88(final FWCurrency currency) {
        montant_API_88.add(currency);
        addMontant_API_TOTAL_AI(currency);
    }

    public void addMontant_API_89(final FWCurrency currency) {
        montant_API_89.add(currency);
        addMontant_API_TOTAL_AVS(currency);
    }

    public void addMontant_API_91(final FWCurrency currency) {
        montant_API_91.add(currency);
        addMontant_API_TOTAL_AI(currency);
    }

    public void addMontant_API_92(final FWCurrency currency) {
        montant_API_92.add(currency);
        addMontant_API_TOTAL_AI(currency);
    }

    public void addMontant_API_93(final FWCurrency currency) {
        montant_API_93.add(currency);
        addMontant_API_TOTAL_AI(currency);
    }

    public void addMontant_API_94(final FWCurrency currency) {
        montant_API_94.add(currency);
        addMontant_API_TOTAL_AVS(currency);
    }

    public void addMontant_API_95(final FWCurrency currency) {
        montant_API_95.add(currency);
        addMontant_API_TOTAL_AVS(currency);
    }

    public void addMontant_API_96(final FWCurrency currency) {
        montant_API_96.add(currency);
        addMontant_API_TOTAL_AVS(currency);
    }

    public void addMontant_API_97(final FWCurrency currency) {
        montant_API_97.add(currency);
        addMontant_API_TOTAL_AVS(currency);
    }

    public void addMontant_API_TOTAL_AI(final FWCurrency currency) {
        montant_API_TOTAL_AI.add(currency);
    }

    public void addMontant_API_TOTAL_AVS(final FWCurrency currency) {
        montant_API_TOTAL_AVS.add(currency);
    }

    public void addMontant_AVS_10(final FWCurrency currency) {
        montant_AVS_10.add(currency);
        addMontant_AVS_TOTAL_RO(currency);
    }

    public void addMontant_AVS_13(final FWCurrency currency) {
        montant_AVS_13.add(currency);
        addMontant_AVS_TOTAL_RO(currency);
    }

    public void addMontant_AVS_14(final FWCurrency currency) {
        montant_AVS_14.add(currency);
        addMontant_AVS_TOTAL_RO(currency);
    }

    public void addMontant_AVS_15(final FWCurrency currency) {
        montant_AVS_15.add(currency);
        addMontant_AVS_TOTAL_RO(currency);
    }

    public void addMontant_AVS_16(final FWCurrency currency) {
        montant_AVS_16.add(currency);
        addMontant_AVS_TOTAL_RO(currency);
    }

    public void addMontant_AVS_20(final FWCurrency currency) {
        montant_AVS_20.add(currency);
        addMontant_AVS_TOTAL_RE(currency);
    }

    public void addMontant_AVS_23(final FWCurrency currency) {
        montant_AVS_23.add(currency);
        addMontant_AVS_TOTAL_RE(currency);
    }

    public void addMontant_AVS_24(final FWCurrency currency) {
        montant_AVS_24.add(currency);
        addMontant_AVS_TOTAL_RE(currency);
    }

    public void addMontant_AVS_25(final FWCurrency currency) {
        montant_AVS_25.add(currency);
        addMontant_AVS_TOTAL_RE(currency);
    }

    public void addMontant_AVS_26(final FWCurrency currency) {
        montant_AVS_26.add(currency);
        addMontant_AVS_TOTAL_RE(currency);
    }

    public void addMontant_AVS_33(final FWCurrency currency) {
        montant_AVS_33.add(currency);
        addMontant_AVS_TOTAL_RO(currency);
    }

    public void addMontant_AVS_34(final FWCurrency currency) {
        montant_AVS_34.add(currency);
        addMontant_AVS_TOTAL_RO(currency);
    }

    public void addMontant_AVS_35(final FWCurrency currency) {
        montant_AVS_35.add(currency);
        addMontant_AVS_TOTAL_RO(currency);
    }

    public void addMontant_AVS_36(final FWCurrency currency) {
        montant_AVS_36.add(currency);
        addMontant_AVS_TOTAL_RO(currency);
    }

    public void addMontant_AVS_43(final FWCurrency currency) {
        montant_AVS_43.add(currency);
        addMontant_AVS_TOTAL_RE(currency);
    }

    public void addMontant_AVS_44(final FWCurrency currency) {
        montant_AVS_44.add(currency);
        addMontant_AVS_TOTAL_RE(currency);
    }

    public void addMontant_AVS_45(final FWCurrency currency) {
        montant_AVS_45.add(currency);
        addMontant_AVS_TOTAL_RE(currency);
    }

    public void addMontant_AVS_46(final FWCurrency currency) {
        montant_AVS_46.add(currency);
        addMontant_AVS_TOTAL_RE(currency);
    }

    public void addMontant_AVS_TOTAL_RE(final FWCurrency currency) {
        montant_AVS_TOTAL_RE.add(currency);
    }

    public void addMontant_AVS_TOTAL_RO(final FWCurrency currency) {
        montant_AVS_TOTAL_RO.add(currency);
    }

    public void addMontant_PC_110(final FWCurrency currency) {
        montant_PC_110.add(currency);
        addMontant_PC_TOTAL(currency);
    }

    public void addMontant_PC_113(final FWCurrency currency) {
        montant_PC_113.add(currency);
        addMontant_PC_TOTAL(currency);
    }

    public void addMontant_PC_150(final FWCurrency currency) {
        montant_PC_150.add(currency);
        addMontant_PC_TOTAL(currency);
    }

    public void addMontant_PC_198(final FWCurrency currency) {
        montant_PC_198.add(currency);
        addMontant_PC_TOTAL(currency);
    }

    public void addMontant_PC_199(final FWCurrency currency) {
        montant_PC_199.add(currency);
        addMontant_PC_TOTAL(currency);
    }

    public void addMontant_PC_TOTAL(final FWCurrency currency) {
        montant_PC_TOTAL.add(currency);
    }

    public void addMontant_RFM_210(final FWCurrency currency) {
        montant_RFM_210.add(currency);
        addMontant_RFM_TOTAL(currency);
    }

    public void addMontant_RFM_213(final FWCurrency currency) {
        montant_RFM_213.add(currency);
        addMontant_RFM_TOTAL(currency);
    }

    public void addMontant_RFM_250(final FWCurrency currency) {
        montant_RFM_250.add(currency);
        addMontant_RFM_TOTAL(currency);
    }

    public void addMontant_RFM_TOTAL(final FWCurrency currency) {
        montant_RFM_TOTAL.add(currency);
    }

    public void addNb_AI_1_TOTAL_RE(final int i) {
        nb_AI_1_TOTAL_RE += i;
        addNb_AI_TOTAL_RE(i);
    }

    public void addNb_AI_1_TOTAL_RO(final int i) {
        nb_AI_1_TOTAL_RO += i;
        addNb_AI_TOTAL_RO(i);
    }

    public void addNb_AI_2_TOTAL_RE(final int i) {
        nb_AI_2_TOTAL_RE += i;
        addNb_AI_TOTAL_RE(i);
    }

    public void addNb_AI_2_TOTAL_RO(final int i) {
        nb_AI_2_TOTAL_RO += i;
        addNb_AI_TOTAL_RO(i);
    }

    public void addNb_AI_3_TOTAL_RE(final int i) {
        nb_AI_3_TOTAL_RE += i;
        addNb_AI_TOTAL_RE(i);
    }

    public void addNb_AI_3_TOTAL_RO(final int i) {
        nb_AI_3_TOTAL_RO += i;
        addNb_AI_TOTAL_RO(i);
    }

    public void addNb_AI_4_TOTAL_RE(final int i) {
        nb_AI_4_TOTAL_RE += i;
        addNb_AI_TOTAL_RE(i);
    }

    public void addNb_AI_4_TOTAL_RO(final int i) {
        nb_AI_4_TOTAL_RO += i;
        addNb_AI_TOTAL_RO(i);
    }

    public void addNb_AI_50_1(final int i) {
        nb_AI_50_1 += i;
        nb_AI_50 += i;
        addNb_AI_1_TOTAL_RO(i);
    }

    public void addNb_AI_50_2(final int i) {
        nb_AI_50_2 += i;
        nb_AI_50 += i;
        addNb_AI_2_TOTAL_RO(i);
    }

    public void addNb_AI_50_3(final int i) {
        nb_AI_50_3 += i;
        nb_AI_50 += i;
        addNb_AI_3_TOTAL_RO(i);
    }

    public void addNb_AI_50_4(final int i) {
        nb_AI_50_4 += i;
        nb_AI_50 += i;
        addNb_AI_4_TOTAL_RO(i);
    }

    public void addNb_AI_52(final int i) {
        nb_AI_52 += i;
        addNb_AI_1_TOTAL_RO(i);
    }

    public void addNb_AI_53_1(final int i) {
        nb_AI_53_1 += i;
        nb_AI_53 += i;
        addNb_AI_1_TOTAL_RO(i);
    }

    public void addNb_AI_53_2(final int i) {
        nb_AI_53_2 += i;
        nb_AI_53 += i;
        addNb_AI_2_TOTAL_RO(i);
    }

    public void addNb_AI_53_3(final int i) {
        nb_AI_53_3 += i;
        nb_AI_53 += i;
        addNb_AI_3_TOTAL_RO(i);
    }

    public void addNb_AI_53_4(final int i) {
        nb_AI_53_4 += i;
        nb_AI_53 += i;
        addNb_AI_4_TOTAL_RO(i);
    }

    public void addNb_AI_54_1(final int i) {
        nb_AI_54_1 += i;
        nb_AI_54 += i;
        addNb_AI_1_TOTAL_RO(i);
    }

    public void addNb_AI_54_2(final int i) {
        nb_AI_54_2 += i;
        nb_AI_54 += i;
        addNb_AI_2_TOTAL_RO(i);
    }

    public void addNb_AI_54_3(final int i) {
        nb_AI_54_3 += i;
        nb_AI_54 += i;
        addNb_AI_3_TOTAL_RO(i);
    }

    public void addNb_AI_54_4(final int i) {
        nb_AI_54_4 += i;
        nb_AI_54 += i;
        addNb_AI_4_TOTAL_RO(i);
    }

    public void addNb_AI_55_1(final int i) {
        nb_AI_55_1 += i;
        nb_AI_55 += i;
        addNb_AI_1_TOTAL_RO(i);
    }

    public void addNb_AI_55_2(final int i) {
        nb_AI_55_2 += i;
        nb_AI_55 += i;
        addNb_AI_2_TOTAL_RO(i);
    }

    public void addNb_AI_55_3(final int i) {
        nb_AI_55_3 += i;
        nb_AI_55 += i;
        addNb_AI_3_TOTAL_RO(i);
    }

    public void addNb_AI_55_4(final int i) {
        nb_AI_55_4 += i;
        nb_AI_55 += i;
        addNb_AI_4_TOTAL_RO(i);
    }

    public void addNb_AI_56_1(final int i) {
        nb_AI_56_1 += i;
        nb_AI_56 += i;
        addNb_AI_1_TOTAL_RO(i);
    }

    public void addNb_AI_56_2(final int i) {
        nb_AI_56_2 += i;
        nb_AI_56 += i;
        addNb_AI_2_TOTAL_RO(i);
    }

    public void addNb_AI_56_3(final int i) {
        nb_AI_56_3 += i;
        nb_AI_56 += i;
        addNb_AI_3_TOTAL_RO(i);
    }

    public void addNb_AI_56_4(final int i) {
        nb_AI_56_4 += i;
        nb_AI_56 += i;
        addNb_AI_4_TOTAL_RO(i);
    }

    public void addNb_AI_70_1(final int i) {
        nb_AI_70_1 += i;
        nb_AI_70 += i;
        addNb_AI_1_TOTAL_RE(i);
    }

    public void addNb_AI_70_2(final int i) {
        nb_AI_70_2 += i;
        nb_AI_70 += i;
        addNb_AI_2_TOTAL_RE(i);
    }

    public void addNb_AI_70_3(final int i) {
        nb_AI_70_3 += i;
        nb_AI_70 += i;
        addNb_AI_3_TOTAL_RE(i);
    }

    public void addNb_AI_70_4(final int i) {
        nb_AI_70_4 += i;
        nb_AI_70 += i;
        addNb_AI_4_TOTAL_RE(i);
    }

    public void addNb_AI_72_1(final int i) {
        nb_AI_72_1 += i;
        nb_AI_72 += i;
        addNb_AI_1_TOTAL_RE(i);
    }

    public void addNb_AI_73_1(final int i) {
        nb_AI_73_1 += i;
        nb_AI_73 += i;
        addNb_AI_1_TOTAL_RE(i);
    }

    public void addNb_AI_73_2(final int i) {
        nb_AI_73_2 += i;
        nb_AI_73 += i;
        addNb_AI_2_TOTAL_RE(i);
    }

    public void addNb_AI_73_3(final int i) {
        nb_AI_73_3 += i;
        nb_AI_73 += i;
        addNb_AI_3_TOTAL_RE(i);
    }

    public void addNb_AI_73_4(final int i) {
        nb_AI_73_4 += i;
        nb_AI_73 += i;
        addNb_AI_4_TOTAL_RE(i);
    }

    public void addNb_AI_74_1(final int i) {
        nb_AI_74_1 += i;
        nb_AI_74 += i;
        addNb_AI_1_TOTAL_RE(i);
    }

    public void addNb_AI_74_2(final int i) {
        nb_AI_74_2 += i;
        nb_AI_74 += i;
        addNb_AI_2_TOTAL_RE(i);
    }

    public void addNb_AI_74_3(final int i) {
        nb_AI_74_3 += i;
        nb_AI_74 += i;
        addNb_AI_3_TOTAL_RE(i);
    }

    public void addNb_AI_74_4(final int i) {
        nb_AI_74_4 += i;
        nb_AI_74 += i;
        addNb_AI_4_TOTAL_RE(i);
    }

    public void addNb_AI_75_1(final int i) {
        nb_AI_75_1 += i;
        nb_AI_75 += i;
        addNb_AI_1_TOTAL_RE(i);
    }

    public void addNb_AI_75_2(final int i) {
        nb_AI_75_2 += i;
        nb_AI_75 += i;
        addNb_AI_2_TOTAL_RE(i);
    }

    public void addNb_AI_75_3(final int i) {
        nb_AI_75_3 += i;
        nb_AI_75 += i;
        addNb_AI_3_TOTAL_RE(i);
    }

    public void addNb_AI_75_4(final int i) {
        nb_AI_75_4 += i;
        nb_AI_75 += i;
        addNb_AI_4_TOTAL_RE(i);
    }

    public void addNb_AI_76_1(final int i) {
        nb_AI_76_1 += i;
        nb_AI_76 += i;
        addNb_AI_1_TOTAL_RE(i);
    }

    public void addNb_AI_76_2(final int i) {
        nb_AI_76_2 += i;
        nb_AI_76 += i;
        addNb_AI_2_TOTAL_RE(i);
    }

    public void addNb_AI_76_3(final int i) {
        nb_AI_76_3 += i;
        nb_AI_76 += i;
        addNb_AI_3_TOTAL_RE(i);
    }

    public void addNb_AI_76_4(final int i) {
        nb_AI_76_4 += i;
        nb_AI_76 += i;
        addNb_AI_4_TOTAL_RE(i);
    }

    public void addNb_AI_TOTAL_RE(final int i) {
        nb_AI_TOTAL_RE += i;
    }

    public void addNb_AI_TOTAL_RO(final int i) {
        nb_AI_TOTAL_RO += i;
    }

    public void addNb_API_81(final int i) {
        nb_API_81 += i;
        addNb_API_TOTAL_AI(i);
    }

    public void addNb_API_82(final int i) {
        nb_API_82 += i;
        addNb_API_TOTAL_AI(i);
    }

    public void addNb_API_83(final int i) {
        nb_API_83 += i;
        addNb_API_TOTAL_AI(i);
    }

    public void addNb_API_84(final int i) {
        nb_API_84 += i;
        addNb_API_TOTAL_AI(i);
    }

    public void addNb_API_85(final int i) {
        nb_API_85 += i;
        addNb_API_TOTAL_AVS(i);
    }

    public void addNb_API_86(final int i) {
        nb_API_86 += i;
        addNb_API_TOTAL_AVS(i);
    }

    public void addNb_API_87(final int i) {
        nb_API_87 += i;
        addNb_API_TOTAL_AVS(i);
    }

    public void addNb_API_88(final int i) {
        nb_API_88 += i;
        addNb_API_TOTAL_AI(i);
    }

    public void addNb_API_89(final int i) {
        nb_API_89 += i;
        addNb_API_TOTAL_AVS(i);
    }

    public void addNb_API_91(final int i) {
        nb_API_91 += i;
        addNb_API_TOTAL_AI(i);
    }

    public void addNb_API_92(final int i) {
        nb_API_92 += i;
        addNb_API_TOTAL_AI(i);
    }

    public void addNb_API_93(final int i) {
        nb_API_93 += i;
        addNb_API_TOTAL_AI(i);
    }

    public void addNb_API_94(final int i) {
        nb_API_94 += i;
        addNb_API_TOTAL_AVS(i);
    }

    public void addNb_API_95(final int i) {
        nb_API_95 += i;
        addNb_API_TOTAL_AVS(i);
    }

    public void addNb_API_96(final int i) {
        nb_API_96 += i;
        addNb_API_TOTAL_AVS(i);
    }

    public void addNb_API_97(final int i) {
        nb_API_97 += i;
        addNb_API_TOTAL_AVS(i);
    }

    public void addNb_API_TOTAL_AI(final int i) {
        nb_API_TOTAL_AI += i;
    }

    public void addNb_API_TOTAL_AVS(final int i) {
        nb_API_TOTAL_AVS += i;
    }

    public void addNb_AVS_10(final int i) {
        nb_AVS_10 += i;
        addNb_AVS_TOTAL_RO(i);
    }

    public void addNb_AVS_13(final int i) {
        nb_AVS_13 += i;
        addNb_AVS_TOTAL_RO(i);
    }

    public void addNb_AVS_14(final int i) {
        nb_AVS_14 += i;
        addNb_AVS_TOTAL_RO(i);
    }

    public void addNb_AVS_15(final int i) {
        nb_AVS_15 += i;
        addNb_AVS_TOTAL_RO(i);
    }

    public void addNb_AVS_16(final int i) {
        nb_AVS_16 += i;
        addNb_AVS_TOTAL_RO(i);
    }

    public void addNb_AVS_20(final int i) {
        nb_AVS_20 += i;
        addNb_AVS_TOTAL_RE(i);
    }

    public void addNb_AVS_23(final int i) {
        nb_AVS_23 += i;
        addNb_AVS_TOTAL_RE(i);
    }

    public void addNb_AVS_24(final int i) {
        nb_AVS_24 += i;
        addNb_AVS_TOTAL_RE(i);
    }

    public void addNb_AVS_25(final int i) {
        nb_AVS_25 += i;
        addNb_AVS_TOTAL_RE(i);
    }

    public void addNb_AVS_26(final int i) {
        nb_AVS_26 += i;
        addNb_AVS_TOTAL_RE(i);
    }

    public void addNb_AVS_33(final int i) {
        nb_AVS_33 += i;
        addNb_AVS_TOTAL_RO(i);
    }

    public void addNb_AVS_34(final int i) {
        nb_AVS_34 += i;
        addNb_AVS_TOTAL_RO(i);
    }

    public void addNb_AVS_35(final int i) {
        nb_AVS_35 += i;
        addNb_AVS_TOTAL_RO(i);
    }

    public void addNb_AVS_36(final int i) {
        nb_AVS_36 += i;
        addNb_AVS_TOTAL_RO(i);
    }

    public void addNb_AVS_43(final int i) {
        nb_AVS_43 += i;
        addNb_AVS_TOTAL_RE(i);
    }

    public void addNb_AVS_44(final int i) {
        nb_AVS_44 += i;
        addNb_AVS_TOTAL_RE(i);
    }

    public void addNb_AVS_45(final int i) {
        nb_AVS_45 += i;
        addNb_AVS_TOTAL_RE(i);
    }

    public void addNb_AVS_46(final int i) {
        nb_AVS_46 += i;
        addNb_AVS_TOTAL_RE(i);
    }

    public void addNb_AVS_TOTAL_RE(final int i) {
        nb_AVS_TOTAL_RE += i;
    }

    public void addNb_AVS_TOTAL_RO(final int i) {
        nb_AVS_TOTAL_RO += i;
    }

    public void addNb_PC_110(final int i) {
        nb_PC_110 += i;
        addNb_PC_TOTAL(i);
    }

    public void addNb_PC_113(final int i) {
        nb_PC_113 += i;
        addNb_PC_TOTAL(i);
    }

    public void addNb_PC_150(final int i) {
        nb_PC_150 += i;
        addNb_PC_TOTAL(i);
    }

    public void addNb_PC_198(final int i) {
        nb_PC_198 += i;
        addNb_PC_TOTAL(i);
    }

    public void addNb_PC_199(final int i) {
        nb_PC_199 += i;
        addNb_PC_TOTAL(i);
    }

    public void addNb_PC_TOTAL(final int i) {
        nb_PC_TOTAL += i;
    }

    public void addNb_RFM_210(final int i) {
        nb_RFM_210 += i;
        addNb_RFM_TOTAL(i);
    }

    public void addNb_RFM_213(final int i) {
        nb_RFM_213 += i;
        addNb_RFM_TOTAL(i);
    }

    public void addNb_RFM_250(final int i) {
        nb_RFM_250 += i;
        addNb_RFM_TOTAL(i);
    }

    public void addNb_RFM_TOTAL(final int i) {
        nb_RFM_TOTAL += i;
    }

    /**
     * Charge les infos et trie les résultats par genre de prestations.
     */
    public void chargerParGenrePrestation() throws Exception {

        mgr.setSession(session);
        mgr.setForDate(forMois);

        boolean hasOpenedTransaction = false;
        BTransaction transaction = session.getCurrentThreadTransaction();
        BStatement statement = null;
        try {
            if (transaction == null) {
                transaction = (BTransaction) session.newTransaction();
                transaction.openTransaction();
                hasOpenedTransaction = true;
            }

            // DO SQL Request
            statement = mgr.cursorOpen(transaction);
            REEtatRentes etatRentes = null;

            while ((etatRentes = (REEtatRentes) mgr.cursorReadNext(statement)) != null) {

                int codePrestation = 0;

                if (!JadeStringUtil.isEmpty(etatRentes.getCodePrestation())) {
                    codePrestation = Float.valueOf(etatRentes.getCodePrestation()).intValue();
                } else {
                    throw new Exception(session.getLabel("ERREUR_RENTE_ACC_SANS_CODE_PREST"));
                }

                int fractionRentes = 0;

                switch (codePrestation) {
                // /////////////////////////////////////////////////////////////////////////
                // AVS
                // /////////////////////////////////////////////////////////////////////////
                    case 10:
                        addMontant_AVS_10(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_10(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 20:
                        addMontant_AVS_20(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_20(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 13:
                        addMontant_AVS_13(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_13(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 23:
                        addMontant_AVS_23(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_23(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 33:
                        addMontant_AVS_33(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_33(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 14:
                        addMontant_AVS_14(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_14(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 24:
                        addMontant_AVS_24(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_24(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 15:
                        addMontant_AVS_15(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_15(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 25:
                        addMontant_AVS_25(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_25(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 16:
                        addMontant_AVS_16(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_16(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 26:
                        addMontant_AVS_26(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_26(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 34:
                        addMontant_AVS_34(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_34(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 35:
                        addMontant_AVS_35(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_35(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 36:
                        addMontant_AVS_36(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_36(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 43:
                        addMontant_AVS_43(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_43(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 44:
                        addMontant_AVS_44(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_44(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 45:
                        addMontant_AVS_45(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_45(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 46:
                        addMontant_AVS_46(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AVS_46(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    // /////////////////////////////////////////////////////////////////////////
                    // AI
                    // /////////////////////////////////////////////////////////////////////////
                    case 50:
                        if (!JadeStringUtil.isIntegerEmpty(etatRentes.getFractionRente())) {
                            fractionRentes = Float.valueOf(etatRentes.getFractionRente()).intValue();

                            if (fractionRentes == 1) {
                                addMontant_AI_50_1(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_50_1(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 2) {
                                addMontant_AI_50_2(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_50_2(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 3) {
                                addMontant_AI_50_3(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_50_3(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 4) {
                                addMontant_AI_50_4(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_50_4(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else {
                                throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                            }

                        } else {
                            throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                        }

                    case 52:
                        addMontant_AI_52(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AI_52(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;
                    case 53:
                        if (!JadeStringUtil.isIntegerEmpty(etatRentes.getFractionRente())) {
                            fractionRentes = Float.valueOf(etatRentes.getFractionRente()).intValue();

                            if (fractionRentes == 1) {
                                addMontant_AI_53_1(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_53_1(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 2) {
                                addMontant_AI_53_2(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_53_2(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 3) {
                                addMontant_AI_53_3(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_53_3(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 4) {
                                addMontant_AI_53_4(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_53_4(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else {
                                throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                            }

                        } else {
                            throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                        }

                    case 54:
                        if (!JadeStringUtil.isIntegerEmpty(etatRentes.getFractionRente())) {
                            fractionRentes = Float.valueOf(etatRentes.getFractionRente()).intValue();

                            if (fractionRentes == 1) {
                                addMontant_AI_54_1(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_54_1(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 2) {
                                addMontant_AI_54_2(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_54_2(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 3) {
                                addMontant_AI_54_3(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_54_3(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 4) {
                                addMontant_AI_54_4(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_54_4(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else {
                                throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                            }

                        } else {
                            throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                        }

                    case 55:
                        if (!JadeStringUtil.isIntegerEmpty(etatRentes.getFractionRente())) {
                            fractionRentes = Float.valueOf(etatRentes.getFractionRente()).intValue();

                            if (fractionRentes == 1) {
                                addMontant_AI_55_1(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_55_1(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 2) {
                                addMontant_AI_55_2(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_55_2(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 3) {
                                addMontant_AI_55_3(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_55_3(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 4) {
                                addMontant_AI_55_4(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_55_4(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else {
                                throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                            }

                        } else {
                            throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                        }

                    case 56:
                        if (!JadeStringUtil.isIntegerEmpty(etatRentes.getFractionRente())) {
                            fractionRentes = Float.valueOf(etatRentes.getFractionRente()).intValue();

                            if (fractionRentes == 1) {
                                addMontant_AI_56_1(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_56_1(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 2) {
                                addMontant_AI_56_2(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_56_2(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 3) {
                                addMontant_AI_56_3(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_56_3(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 4) {
                                addMontant_AI_56_4(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_56_4(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else {
                                throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                            }

                        } else {
                            throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                        }

                    case 70:
                        if (!JadeStringUtil.isIntegerEmpty(etatRentes.getFractionRente())) {
                            fractionRentes = Float.valueOf(etatRentes.getFractionRente()).intValue();

                            if (fractionRentes == 1) {
                                addMontant_AI_70_1(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_70_1(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 2) {
                                addMontant_AI_70_2(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_70_2(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 3) {
                                addMontant_AI_70_3(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_70_3(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 4) {
                                addMontant_AI_70_4(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_70_4(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else {
                                throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                            }

                        } else {
                            throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                        }
                    case 72:
                        addMontant_AI_72_1(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_AI_72_1(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;
                    case 73:
                        if (!JadeStringUtil.isIntegerEmpty(etatRentes.getFractionRente())) {
                            fractionRentes = Float.valueOf(etatRentes.getFractionRente()).intValue();

                            if (fractionRentes == 1) {
                                addMontant_AI_73_1(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_73_1(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 2) {
                                addMontant_AI_73_2(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_73_2(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 3) {
                                addMontant_AI_73_3(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_73_3(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 4) {
                                addMontant_AI_73_4(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_73_4(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else {
                                throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                            }

                        } else {
                            throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                        }

                    case 74:
                        if (!JadeStringUtil.isIntegerEmpty(etatRentes.getFractionRente())) {
                            fractionRentes = Float.valueOf(etatRentes.getFractionRente()).intValue();

                            if (fractionRentes == 1) {
                                addMontant_AI_74_1(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_74_1(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 2) {
                                addMontant_AI_74_2(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_74_2(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 3) {
                                addMontant_AI_74_3(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_74_3(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 4) {
                                addMontant_AI_74_4(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_74_4(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else {
                                throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                            }

                        } else {
                            throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                        }

                    case 75:
                        if (!JadeStringUtil.isIntegerEmpty(etatRentes.getFractionRente())) {
                            fractionRentes = Float.valueOf(etatRentes.getFractionRente()).intValue();

                            if (fractionRentes == 1) {
                                addMontant_AI_75_1(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_75_1(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 2) {
                                addMontant_AI_75_2(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_75_2(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 3) {
                                addMontant_AI_75_3(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_75_3(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 4) {
                                addMontant_AI_75_4(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_75_4(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else {
                                throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                            }

                        } else {
                            throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                        }

                    case 76:
                        if (!JadeStringUtil.isIntegerEmpty(etatRentes.getFractionRente())) {
                            fractionRentes = Float.valueOf(etatRentes.getFractionRente()).intValue();

                            if (fractionRentes == 1) {
                                addMontant_AI_76_1(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_76_1(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 2) {
                                addMontant_AI_76_2(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_76_2(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 3) {
                                addMontant_AI_76_3(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_76_3(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else if (fractionRentes == 4) {
                                addMontant_AI_76_4(new FWCurrency(etatRentes.getMontantTotalForCode()));
                                addNb_AI_76_4(Float.valueOf(etatRentes.getNbForCode()).intValue());
                                break;
                            } else {
                                throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                            }

                        } else {
                            throw new Exception(session.getLabel("ERREUR_RENTE_AI_SANS_FRACTION_RENTE"));
                        }

                        // /////////////////////////////////////////////////////////////////////////
                        // API
                        // /////////////////////////////////////////////////////////////////////////
                    case 81:
                        addMontant_API_81(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_API_81(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 82:
                        addMontant_API_82(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_API_82(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 83:
                        addMontant_API_83(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_API_83(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 84:
                        addMontant_API_84(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_API_84(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 88:
                        addMontant_API_88(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_API_88(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 91:
                        addMontant_API_91(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_API_91(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 92:
                        addMontant_API_92(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_API_92(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 85:
                        addMontant_API_85(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_API_85(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 86:
                        addMontant_API_86(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_API_86(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 87:
                        addMontant_API_87(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_API_87(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 89:
                        addMontant_API_89(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_API_89(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 93:
                        addMontant_API_93(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_API_93(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 94:
                        addMontant_API_94(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_API_94(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 95:
                        addMontant_API_95(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_API_95(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 96:
                        addMontant_API_96(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_API_96(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    case 97:
                        addMontant_API_97(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_API_97(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;
                    case 110:
                        addMontant_PC_110(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_PC_110(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;
                    case 113:
                        addMontant_PC_113(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_PC_113(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;
                    case 150:
                        addMontant_PC_150(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_PC_150(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;
                    case 198:
                        addMontant_PC_198(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_PC_198(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;
                    case 199:
                        addMontant_PC_199(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_PC_199(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;
                    case 210:
                        addMontant_RFM_210(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_RFM_210(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;
                    case 213:
                        addMontant_RFM_213(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_RFM_213(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;
                    case 250:
                        addMontant_RFM_250(new FWCurrency(etatRentes.getMontantTotalForCode()));
                        addNb_RFM_250(Float.valueOf(etatRentes.getNbForCode()).intValue());
                        break;

                    default:
                        throw new Exception("Code prestation inconnu : " + codePrestation);
                }
            }
        } finally {
            try {
                if (statement != null) {
                    try {
                        mgr.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } finally {
                if (hasOpenedTransaction) {
                    transaction.closeTransaction();
                }
            }
        }
    }

    public String getForMois() {
        return forMois;
    }

    public FWCurrency getMontant_AI_1_TOTAL_RE() {
        return montant_AI_1_TOTAL_RE;
    }

    public FWCurrency getMontant_AI_1_TOTAL_RO() {
        return montant_AI_1_TOTAL_RO;
    }

    public FWCurrency getMontant_AI_2_TOTAL_RE() {
        return montant_AI_2_TOTAL_RE;
    }

    public FWCurrency getMontant_AI_2_TOTAL_RO() {
        return montant_AI_2_TOTAL_RO;
    }

    public FWCurrency getMontant_AI_3_TOTAL_RE() {
        return montant_AI_3_TOTAL_RE;
    }

    public FWCurrency getMontant_AI_3_TOTAL_RO() {
        return montant_AI_3_TOTAL_RO;
    }

    public FWCurrency getMontant_AI_4_TOTAL_RE() {
        return montant_AI_4_TOTAL_RE;
    }

    public FWCurrency getMontant_AI_4_TOTAL_RO() {
        return montant_AI_4_TOTAL_RO;
    }

    public FWCurrency getMontant_AI_50() {
        return montant_AI_50;
    }

    public FWCurrency getMontant_AI_50_1() {
        return montant_AI_50_1;
    }

    public FWCurrency getMontant_AI_50_2() {
        return montant_AI_50_2;
    }

    public FWCurrency getMontant_AI_50_3() {
        return montant_AI_50_3;
    }

    public FWCurrency getMontant_AI_50_4() {
        return montant_AI_50_4;
    }

    public FWCurrency getMontant_AI_52() {
        return montant_AI_52;
    }

    public FWCurrency getMontant_AI_53() {
        return montant_AI_53;
    }

    public FWCurrency getMontant_AI_53_1() {
        return montant_AI_53_1;
    }

    public FWCurrency getMontant_AI_53_2() {
        return montant_AI_53_2;
    }

    public FWCurrency getMontant_AI_53_3() {
        return montant_AI_53_3;
    }

    public FWCurrency getMontant_AI_53_4() {
        return montant_AI_53_4;
    }

    public FWCurrency getMontant_AI_54() {
        return montant_AI_54;
    }

    public FWCurrency getMontant_AI_54_1() {
        return montant_AI_54_1;
    }

    public FWCurrency getMontant_AI_54_2() {
        return montant_AI_54_2;
    }

    public FWCurrency getMontant_AI_54_3() {
        return montant_AI_54_3;
    }

    public FWCurrency getMontant_AI_54_4() {
        return montant_AI_54_4;
    }

    public FWCurrency getMontant_AI_55() {
        return montant_AI_55;
    }

    public FWCurrency getMontant_AI_55_1() {
        return montant_AI_55_1;
    }

    public FWCurrency getMontant_AI_55_2() {
        return montant_AI_55_2;
    }

    public FWCurrency getMontant_AI_55_3() {
        return montant_AI_55_3;
    }

    public FWCurrency getMontant_AI_55_4() {
        return montant_AI_55_4;
    }

    public FWCurrency getMontant_AI_56() {
        return montant_AI_56;
    }

    public FWCurrency getMontant_AI_56_1() {
        return montant_AI_56_1;
    }

    public FWCurrency getMontant_AI_56_2() {
        return montant_AI_56_2;
    }

    public FWCurrency getMontant_AI_56_3() {
        return montant_AI_56_3;
    }

    public FWCurrency getMontant_AI_56_4() {
        return montant_AI_56_4;
    }

    public FWCurrency getMontant_AI_70() {
        return montant_AI_70;
    }

    public FWCurrency getMontant_AI_70_1() {
        return montant_AI_70_1;
    }

    public FWCurrency getMontant_AI_70_2() {
        return montant_AI_70_2;
    }

    public FWCurrency getMontant_AI_70_3() {
        return montant_AI_70_3;
    }

    public FWCurrency getMontant_AI_70_4() {
        return montant_AI_70_4;
    }

    public FWCurrency getMontant_AI_72() {
        return montant_AI_72;
    }

    public FWCurrency getMontant_AI_72_1() {
        return montant_AI_72_1;
    }

    public FWCurrency getMontant_AI_73() {
        return montant_AI_73;
    }

    public FWCurrency getMontant_AI_73_1() {
        return montant_AI_73_1;
    }

    public FWCurrency getMontant_AI_73_2() {
        return montant_AI_73_2;
    }

    public FWCurrency getMontant_AI_73_3() {
        return montant_AI_73_3;
    }

    public FWCurrency getMontant_AI_73_4() {
        return montant_AI_73_4;
    }

    public FWCurrency getMontant_AI_74() {
        return montant_AI_74;
    }

    public FWCurrency getMontant_AI_74_1() {
        return montant_AI_74_1;
    }

    public FWCurrency getMontant_AI_74_2() {
        return montant_AI_74_2;
    }

    public FWCurrency getMontant_AI_74_3() {
        return montant_AI_74_3;
    }

    public FWCurrency getMontant_AI_74_4() {
        return montant_AI_74_4;
    }

    public FWCurrency getMontant_AI_75() {
        return montant_AI_75;
    }

    public FWCurrency getMontant_AI_75_1() {
        return montant_AI_75_1;
    }

    public FWCurrency getMontant_AI_75_2() {
        return montant_AI_75_2;
    }

    public FWCurrency getMontant_AI_75_3() {
        return montant_AI_75_3;
    }

    public FWCurrency getMontant_AI_75_4() {
        return montant_AI_75_4;
    }

    public FWCurrency getMontant_AI_76() {
        return montant_AI_76;
    }

    public FWCurrency getMontant_AI_76_1() {
        return montant_AI_76_1;
    }

    public FWCurrency getMontant_AI_76_2() {
        return montant_AI_76_2;
    }

    public FWCurrency getMontant_AI_76_3() {
        return montant_AI_76_3;
    }

    public FWCurrency getMontant_AI_76_4() {
        return montant_AI_76_4;
    }

    public FWCurrency getMontant_AI_TOTAL_RE() {
        return montant_AI_TOTAL_RE;
    }

    public FWCurrency getMontant_AI_TOTAL_RO() {
        return montant_AI_TOTAL_RO;
    }

    public FWCurrency getMontant_API_81() {
        return montant_API_81;
    }

    public FWCurrency getMontant_API_82() {
        return montant_API_82;
    }

    public FWCurrency getMontant_API_83() {
        return montant_API_83;
    }

    public FWCurrency getMontant_API_84() {
        return montant_API_84;
    }

    public FWCurrency getMontant_API_85() {
        return montant_API_85;
    }

    public FWCurrency getMontant_API_86() {
        return montant_API_86;
    }

    public FWCurrency getMontant_API_87() {
        return montant_API_87;
    }

    public FWCurrency getMontant_API_88() {
        return montant_API_88;
    }

    public FWCurrency getMontant_API_89() {
        return montant_API_89;
    }

    public FWCurrency getMontant_API_91() {
        return montant_API_91;
    }

    public FWCurrency getMontant_API_92() {
        return montant_API_92;
    }

    public FWCurrency getMontant_API_93() {
        return montant_API_93;
    }

    public FWCurrency getMontant_API_94() {
        return montant_API_94;
    }

    public FWCurrency getMontant_API_95() {
        return montant_API_95;
    }

    public FWCurrency getMontant_API_96() {
        return montant_API_96;
    }

    public FWCurrency getMontant_API_97() {
        return montant_API_97;
    }

    public FWCurrency getMontant_API_TOTAL_AI() {
        return montant_API_TOTAL_AI;
    }

    public FWCurrency getMontant_API_TOTAL_AVS() {
        return montant_API_TOTAL_AVS;
    }

    public FWCurrency getMontant_AVS_10() {
        return montant_AVS_10;
    }

    public FWCurrency getMontant_AVS_13() {
        return montant_AVS_13;
    }

    public FWCurrency getMontant_AVS_14() {
        return montant_AVS_14;
    }

    public FWCurrency getMontant_AVS_15() {
        return montant_AVS_15;
    }

    public FWCurrency getMontant_AVS_16() {
        return montant_AVS_16;
    }

    public FWCurrency getMontant_AVS_20() {
        return montant_AVS_20;
    }

    public FWCurrency getMontant_AVS_23() {
        return montant_AVS_23;
    }

    public FWCurrency getMontant_AVS_24() {
        return montant_AVS_24;
    }

    public FWCurrency getMontant_AVS_25() {
        return montant_AVS_25;
    }

    public FWCurrency getMontant_AVS_26() {
        return montant_AVS_26;
    }

    public FWCurrency getMontant_AVS_33() {
        return montant_AVS_33;
    }

    public FWCurrency getMontant_AVS_34() {
        return montant_AVS_34;
    }

    public FWCurrency getMontant_AVS_35() {
        return montant_AVS_35;
    }

    public FWCurrency getMontant_AVS_36() {
        return montant_AVS_36;
    }

    public FWCurrency getMontant_AVS_43() {
        return montant_AVS_43;
    }

    public FWCurrency getMontant_AVS_44() {
        return montant_AVS_44;
    }

    public FWCurrency getMontant_AVS_45() {
        return montant_AVS_45;
    }

    public FWCurrency getMontant_AVS_46() {
        return montant_AVS_46;
    }

    public FWCurrency getMontant_AVS_TOTAL_RE() {
        return montant_AVS_TOTAL_RE;
    }

    public FWCurrency getMontant_AVS_TOTAL_RO() {
        return montant_AVS_TOTAL_RO;
    }

    public FWCurrency getMontant_PC_110() {
        return montant_PC_110;
    }

    public FWCurrency getMontant_PC_113() {
        return montant_PC_113;
    }

    public FWCurrency getMontant_PC_150() {
        return montant_PC_150;
    }

    public FWCurrency getMontant_PC_198() {
        return montant_PC_198;
    }

    public FWCurrency getMontant_PC_199() {
        return montant_PC_199;
    }

    public FWCurrency getMontant_PC_TOTAL() {
        return montant_PC_TOTAL;
    }

    public FWCurrency getMontant_RFM_210() {
        return montant_RFM_210;
    }

    public FWCurrency getMontant_RFM_213() {
        return montant_RFM_213;
    }

    public FWCurrency getMontant_RFM_250() {
        return montant_RFM_250;
    }

    public FWCurrency getMontant_RFM_TOTAL() {
        return montant_RFM_TOTAL;
    }

    public int getNb_AI_1_TOTAL_RE() {
        return nb_AI_1_TOTAL_RE;
    }

    public int getNb_AI_1_TOTAL_RO() {
        return nb_AI_1_TOTAL_RO;
    }

    public int getNb_AI_2_TOTAL_RE() {
        return nb_AI_2_TOTAL_RE;
    }

    public int getNb_AI_2_TOTAL_RO() {
        return nb_AI_2_TOTAL_RO;
    }

    public int getNb_AI_3_TOTAL_RE() {
        return nb_AI_3_TOTAL_RE;
    }

    public int getNb_AI_3_TOTAL_RO() {
        return nb_AI_3_TOTAL_RO;
    }

    public int getNb_AI_4_TOTAL_RE() {
        return nb_AI_4_TOTAL_RE;
    }

    public int getNb_AI_4_TOTAL_RO() {
        return nb_AI_4_TOTAL_RO;
    }

    public int getNb_AI_50() {
        return nb_AI_50;
    }

    public int getNb_AI_50_1() {
        return nb_AI_50_1;
    }

    public int getNb_AI_50_2() {
        return nb_AI_50_2;
    }

    public int getNb_AI_50_3() {
        return nb_AI_50_3;
    }

    public int getNb_AI_50_4() {
        return nb_AI_50_4;
    }

    public int getNb_AI_52() {
        return nb_AI_52;
    }

    public int getNb_AI_53() {
        return nb_AI_53;
    }

    public int getNb_AI_53_1() {
        return nb_AI_53_1;
    }

    public int getNb_AI_53_2() {
        return nb_AI_53_2;
    }

    public int getNb_AI_53_3() {
        return nb_AI_53_3;
    }

    public int getNb_AI_53_4() {
        return nb_AI_53_4;
    }

    public int getNb_AI_54() {
        return nb_AI_54;
    }

    public int getNb_AI_54_1() {
        return nb_AI_54_1;
    }

    public int getNb_AI_54_2() {
        return nb_AI_54_2;
    }

    public int getNb_AI_54_3() {
        return nb_AI_54_3;
    }

    public int getNb_AI_54_4() {
        return nb_AI_54_4;
    }

    public int getNb_AI_55() {
        return nb_AI_55;
    }

    public int getNb_AI_55_1() {
        return nb_AI_55_1;
    }

    public int getNb_AI_55_2() {
        return nb_AI_55_2;
    }

    public int getNb_AI_55_3() {
        return nb_AI_55_3;
    }

    public int getNb_AI_55_4() {
        return nb_AI_55_4;
    }

    public int getNb_AI_56() {
        return nb_AI_56;
    }

    public int getNb_AI_56_1() {
        return nb_AI_56_1;
    }

    public int getNb_AI_56_2() {
        return nb_AI_56_2;
    }

    public int getNb_AI_56_3() {
        return nb_AI_56_3;
    }

    public int getNb_AI_56_4() {
        return nb_AI_56_4;
    }

    public int getNb_AI_70() {
        return nb_AI_70;
    }

    public int getNb_AI_70_1() {
        return nb_AI_70_1;
    }

    public int getNb_AI_70_2() {
        return nb_AI_70_2;
    }

    public int getNb_AI_70_3() {
        return nb_AI_70_3;
    }

    public int getNb_AI_70_4() {
        return nb_AI_70_4;
    }

    public int getNb_AI_72() {
        return nb_AI_72;
    }

    public int getNb_AI_72_1() {
        return nb_AI_72_1;
    }

    public int getNb_AI_73() {
        return nb_AI_73;
    }

    public int getNb_AI_73_1() {
        return nb_AI_73_1;
    }

    public int getNb_AI_73_2() {
        return nb_AI_73_2;
    }

    public int getNb_AI_73_3() {
        return nb_AI_73_3;
    }

    public int getNb_AI_73_4() {
        return nb_AI_73_4;
    }

    public int getNb_AI_74() {
        return nb_AI_74;
    }

    public int getNb_AI_74_1() {
        return nb_AI_74_1;
    }

    public int getNb_AI_74_2() {
        return nb_AI_74_2;
    }

    public int getNb_AI_74_3() {
        return nb_AI_74_3;
    }

    public int getNb_AI_74_4() {
        return nb_AI_74_4;
    }

    public int getNb_AI_75() {
        return nb_AI_75;
    }

    public int getNb_AI_75_1() {
        return nb_AI_75_1;
    }

    public int getNb_AI_75_2() {
        return nb_AI_75_2;
    }

    public int getNb_AI_75_3() {
        return nb_AI_75_3;
    }

    public int getNb_AI_75_4() {
        return nb_AI_75_4;
    }

    public int getNb_AI_76() {
        return nb_AI_76;
    }

    public int getNb_AI_76_1() {
        return nb_AI_76_1;
    }

    public int getNb_AI_76_2() {
        return nb_AI_76_2;
    }

    public int getNb_AI_76_3() {
        return nb_AI_76_3;
    }

    public int getNb_AI_76_4() {
        return nb_AI_76_4;
    }

    public int getNb_AI_TOTAL_RE() {
        return nb_AI_TOTAL_RE;
    }

    public int getNb_AI_TOTAL_RO() {
        return nb_AI_TOTAL_RO;
    }

    public int getNb_API_81() {
        return nb_API_81;
    }

    public int getNb_API_82() {
        return nb_API_82;
    }

    public int getNb_API_83() {
        return nb_API_83;
    }

    public int getNb_API_84() {
        return nb_API_84;
    }

    public int getNb_API_85() {
        return nb_API_85;
    }

    public int getNb_API_86() {
        return nb_API_86;
    }

    public int getNb_API_87() {
        return nb_API_87;
    }

    public int getNb_API_88() {
        return nb_API_88;
    }

    public int getNb_API_89() {
        return nb_API_89;
    }

    public int getNb_API_91() {
        return nb_API_91;
    }

    public int getNb_API_92() {
        return nb_API_92;
    }

    public int getNb_API_93() {
        return nb_API_93;
    }

    public int getNb_API_94() {
        return nb_API_94;
    }

    public int getNb_API_95() {
        return nb_API_95;
    }

    public int getNb_API_96() {
        return nb_API_96;
    }

    public int getNb_API_97() {
        return nb_API_97;
    }

    public int getNb_API_TOTAL_AI() {
        return nb_API_TOTAL_AI;
    }

    public int getNb_API_TOTAL_AVS() {
        return nb_API_TOTAL_AVS;
    }

    public int getNb_AVS_10() {
        return nb_AVS_10;
    }

    public int getNb_AVS_13() {
        return nb_AVS_13;
    }

    public int getNb_AVS_14() {
        return nb_AVS_14;
    }

    public int getNb_AVS_15() {
        return nb_AVS_15;
    }

    public int getNb_AVS_16() {
        return nb_AVS_16;
    }

    public int getNb_AVS_20() {
        return nb_AVS_20;
    }

    public int getNb_AVS_23() {
        return nb_AVS_23;
    }

    public int getNb_AVS_24() {
        return nb_AVS_24;
    }

    public int getNb_AVS_25() {
        return nb_AVS_25;
    }

    public int getNb_AVS_26() {
        return nb_AVS_26;
    }

    public int getNb_AVS_33() {
        return nb_AVS_33;
    }

    public int getNb_AVS_34() {
        return nb_AVS_34;
    }

    public int getNb_AVS_35() {
        return nb_AVS_35;
    }

    public int getNb_AVS_36() {
        return nb_AVS_36;
    }

    public int getNb_AVS_43() {
        return nb_AVS_43;
    }

    public int getNb_AVS_44() {
        return nb_AVS_44;
    }

    public int getNb_AVS_45() {
        return nb_AVS_45;
    }

    public int getNb_AVS_46() {
        return nb_AVS_46;
    }

    public int getNb_AVS_TOTAL_RE() {
        return nb_AVS_TOTAL_RE;
    }

    public int getNb_AVS_TOTAL_RO() {
        return nb_AVS_TOTAL_RO;
    }

    public int getNb_PC_110() {
        return nb_PC_110;
    }

    public int getNb_PC_113() {
        return nb_PC_113;
    }

    public int getNb_PC_150() {
        return nb_PC_150;
    }

    public int getNb_PC_198() {
        return nb_PC_198;
    }

    public int getNb_PC_199() {
        return nb_PC_199;
    }

    public int getNb_PC_TOTAL() {
        return nb_PC_TOTAL;
    }

    public int getNb_RFM_210() {
        return nb_RFM_210;
    }

    public int getNb_RFM_213() {
        return nb_RFM_213;
    }

    public int getNb_RFM_250() {
        return nb_RFM_250;
    }

    public int getNb_RFM_TOTAL() {
        return nb_RFM_TOTAL;
    }
}