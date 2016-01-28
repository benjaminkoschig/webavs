package globaz.corvus.topaz;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import statofas.REStatOFASDemandeACalculer;
import ch.globaz.topaz.datajuicer.DocumentData;

public class REStatistiqueOFASOO {

    private String anneeStatOFAS;
    private DocumentData data;
    private int decisionAI;
    private int decisionsAVS;
    private int demPrevTaxe;
    private DocumentData documentData;
    private FWCurrency intMorMontant;
    private List<REStatOFASDemandeACalculer> listeDemandeEnregistrer;
    private BSession session;
    private Map<String, String> statistiqueRAAnticipe;
    private int totalDecision;

    public REStatistiqueOFASOO() {
        super();

        anneeStatOFAS = "";
        data = null;
        decisionAI = 0;
        decisionsAVS = 0;
        demPrevTaxe = 0;
        documentData = null;
        intMorMontant = new FWCurrency();
        session = null;
        statistiqueRAAnticipe = null;
        totalDecision = 0;
    }

    private void chargementEnTete() throws Exception {
        data.addData("idEntete", "CAISSE");
    }

    public void generationDocument() throws Exception {

        data = new DocumentData();
        data.addData("idProcess", "REStatistiqueOFASOO");

        remplirChampsStatiques();
        remplirAutresChamps();
        chargementEnTete();

        setDocumentData(data);

    }

    public String getAnneeStatOFAS() {
        return anneeStatOFAS;
    }

    public int getDecisionAI() {
        return decisionAI;
    }

    public int getDecisionsAVS() {
        return decisionsAVS;
    }

    public int getDemPrevTaxe() {
        return demPrevTaxe;
    }

    public DocumentData getDocumentData() {
        return documentData;
    }

    public FWCurrency getIntMorMontant() {
        return intMorMontant;
    }

    public List<REStatOFASDemandeACalculer> getListeDemandeEnregistrer() {
        return listeDemandeEnregistrer;
    }

    public BSession getSession() {
        return session;
    }

    public Map<String, String> getStatistiqueRAAnticipe() {
        return statistiqueRAAnticipe;
    }

    public int getTotalDecision() {
        return totalDecision;
    }

    private void remplirAutresChamps() throws Exception {

        data.addData("anneeStatistique", getAnneeStatOFAS());

        if (getListeDemandeEnregistrer().size() != 0) {
            StringBuffer demEnr = new StringBuffer();

            data.addData("titre_demEnre_ravs", getSession().getLabel("STATOFAS_TITRE_DEM_ENREGISTRE"));

            Map<String, REStatOFASDemandeACalculer> maListe = new TreeMap<String, REStatOFASDemandeACalculer>();

            for (REStatOFASDemandeACalculer rd : getListeDemandeEnregistrer()) {
                if (!maListe.containsKey(rd.getAvs())) {
                    maListe.put(rd.getAvs(), rd);
                }
            }

            for (Map.Entry<String, REStatOFASDemandeACalculer> rd : maListe.entrySet()) {
                // for (REStatOFASDemandeACalculer rd : maListe) {
                REStatOFASDemandeACalculer maStatOFAS = rd.getValue();
                demEnr.append(maStatOFAS.getAvs() + " " + maStatOFAS.getNom() + " " + maStatOFAS.getPrenom() + " "
                        + maStatOFAS.getMessageOO() + "\r");
            }
            data.addData("info_demEnre_ravs", demEnr.toString());
        }

        int montantTotRAVSAj = 0;

        getStatistiqueRAAnticipe().get(String.valueOf(Integer.parseInt(anneeStatOFAS) - 4));
        // Ligne 1
        data.addData("annee_ravs_l1", String.valueOf(Integer.parseInt(anneeStatOFAS)));
        data.addData("nb_ravs_l1", getStatistiqueRAAnticipe().get(String.valueOf(Integer.parseInt(anneeStatOFAS))));

        montantTotRAVSAj = montantTotRAVSAj
                + Integer.parseInt(getStatistiqueRAAnticipe().get(String.valueOf(Integer.parseInt(anneeStatOFAS))));

        // Ligne 2
        data.addData("annee_ravs_l2", String.valueOf(Integer.parseInt(anneeStatOFAS) - 1));
        data.addData("nb_ravs_l2", getStatistiqueRAAnticipe().get(String.valueOf(Integer.parseInt(anneeStatOFAS) - 1)));
        montantTotRAVSAj = montantTotRAVSAj
                + Integer.parseInt(getStatistiqueRAAnticipe().get(String.valueOf(Integer.parseInt(anneeStatOFAS) - 1)));

        // Ligne 3
        data.addData("annee_ravs_l3", String.valueOf(Integer.parseInt(anneeStatOFAS) - 2));
        data.addData("nb_ravs_l3", getStatistiqueRAAnticipe().get(String.valueOf(Integer.parseInt(anneeStatOFAS) - 2)));
        montantTotRAVSAj = montantTotRAVSAj
                + Integer.parseInt(getStatistiqueRAAnticipe().get(String.valueOf(Integer.parseInt(anneeStatOFAS) - 2)));

        // Ligne 4
        data.addData("annee_ravs_l4", String.valueOf(Integer.parseInt(anneeStatOFAS) - 3));
        data.addData("nb_ravs_l4", getStatistiqueRAAnticipe().get(String.valueOf(Integer.parseInt(anneeStatOFAS) - 3)));
        montantTotRAVSAj = montantTotRAVSAj
                + Integer.parseInt(getStatistiqueRAAnticipe().get(String.valueOf(Integer.parseInt(anneeStatOFAS) - 3)));

        // Ligne 5
        data.addData("annee_ravs_l5", String.valueOf(Integer.parseInt(anneeStatOFAS) - 4));
        data.addData("nb_ravs_l5", getStatistiqueRAAnticipe().get(String.valueOf(Integer.parseInt(anneeStatOFAS) - 4)));
        montantTotRAVSAj = montantTotRAVSAj
                + Integer.parseInt(getStatistiqueRAAnticipe().get(String.valueOf(Integer.parseInt(anneeStatOFAS) - 4)));

        // Nombre total des rentes avs aj
        data.addData("nb_ravs_total", Integer.toString(montantTotRAVSAj));

        // Nombre des demandes prév
        // TODO pour l'instant mettre "0" comme total car le champs
        // "Taxe prélevée" n'existe pas encore
        data.addData("nb_dprev_total", Integer.toString(getDemPrevTaxe()));
        // this.data.addData("nb_dprev_total", "0");

        // Nombre de décision avec intérêt moratoire
        data.addData("nb_intMor_l1", Integer.toString(getTotalDecision()));
        data.addData("nb_intMor_l2", Integer.toString(getDecisionAI()));
        data.addData("nb_intMor_l3", Integer.toString(getDecisionsAVS()));

        // Montant total intérêts moratoires
        FWCurrency montantInteretMor = new FWCurrency("0.00");
        montantInteretMor.add(getIntMorMontant());

        data.addData("nb_intMor_total", montantInteretMor.toStringFormat());

    }

    private void remplirChampsStatiques() throws Exception {

        data.addData("TITRE_DOCUMENT", getSession().getLabel("STATOFAS_PROCESS_TITRE"));
        data.addData("TITRE_RENTE_AVS", getSession().getLabel("STATOFAS_TITRE_RENTE_AVS"));
        data.addData("CH_RAVS_L1", getSession().getLabel("STATOFAS_CHIFFRE") + " 066");
        data.addData("CH_RAVS_L2", getSession().getLabel("STATOFAS_CHIFFRE") + " 067");
        data.addData("CH_RAVS_L3", getSession().getLabel("STATOFAS_CHIFFRE") + " 068");
        data.addData("CH_RAVS_L4", getSession().getLabel("STATOFAS_CHIFFRE") + " 069");
        data.addData("CH_RAVS_L5", getSession().getLabel("STATOFAS_CHIFFRE") + " 070");
        data.addData("CH_RAVS_TOTAL", getSession().getLabel("STATOFAS_CHIFFRE") + " 071");
        data.addData("ravs_total", getSession().getLabel("STATOFAS_TOTAL"));

        data.addData("TITRE_DEMANDE_PREV", getSession().getLabel("STATOFAS_TITRE_DEMANDE_PREV"));
        data.addData("PARA1_DEMANDE_PREV", getSession().getLabel("STATOFAS_PARA1_DEMANDE_PREV"));
        data.addData("PARA2_DEMANDE_PREV", getSession().getLabel("STATOFAS_PARA2__DEMANDE_PREV"));
        data.addData("CH_DPREV", getSession().getLabel("STATOFAS_CHIFFRE") + " 306");

        data.addData("TITRE_INT_MOR", getSession().getLabel("STATOFAS_TITRE_INT_MOR"));
        data.addData("PARA1_INT_MOR", getSession().getLabel("STATOFAS_PARA1_INT_MOR"));

        data.addData("CH_INTMOR_L1", getSession().getLabel("STATOFAS_CHIFFRE") + " 307");
        data.addData("CH_INTMOR_L2", getSession().getLabel("STATOFAS_CHIFFRE") + " 308");
        data.addData("CH_INTMOR_L3", getSession().getLabel("STATOFAS_CHIFFRE") + " 309");
        data.addData("CH_INTMOR_TOTAL", getSession().getLabel("STATOFAS_CHIFFRE") + " 310");
        data.addData("CAS_INTMOR_L1", getSession().getLabel("STATOFAS_TOTAL_CAS_INT_MOR"));
        data.addData("CAS_INTMOR_L2", getSession().getLabel("STATOFAS_NB_AI"));
        data.addData("CAS_INTMOR_L3", getSession().getLabel("STATOFAS_NB_AVS"));
        data.addData("TOTAL_INTMOR", getSession().getLabel("STATOFAS_TOTAL_INT_MOR"));
    }

    public void setAnneeStatOFAS(String anneeStatOFAS) {
        this.anneeStatOFAS = anneeStatOFAS;
    }

    public void setDecisionAI(int decisionAI) {
        this.decisionAI = decisionAI;
    }

    public void setDecisionsAVS(int decisionsAVS) {
        this.decisionsAVS = decisionsAVS;
    }

    public void setDemPrevTaxe(int demPrevTaxe) {
        this.demPrevTaxe = demPrevTaxe;
    }

    public void setDocumentData(DocumentData documentData) {
        this.documentData = documentData;
    }

    public void setIntMorMontant(FWCurrency intMorMontant) {
        this.intMorMontant = intMorMontant;
    }

    public void setListeDemandeEnregistrer(List<REStatOFASDemandeACalculer> listeDemandeEnregistrer) {
        this.listeDemandeEnregistrer = listeDemandeEnregistrer;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setStatistiqueRAAnticipe(Map<String, String> statistiqueRAAnticipe) {
        this.statistiqueRAAnticipe = statistiqueRAAnticipe;
    }

    public void setTotalDecision(int totalDecision) {
        this.totalDecision = totalDecision;
    }
}