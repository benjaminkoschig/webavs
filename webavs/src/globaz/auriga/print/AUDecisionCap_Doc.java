package globaz.auriga.print;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BApplication;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.auriga.business.constantes.AUDecisionType;
import ch.globaz.auriga.business.exceptions.AurigaTechnicalException;
import ch.globaz.auriga.business.models.ComplexEnfantDecisionCAPTiers;
import ch.globaz.auriga.business.models.SimpleDecisionCAP;
import ch.globaz.naos.business.model.AffiliationComplexModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;

public class AUDecisionCap_Doc extends AUAbstractDoc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String CAT_DOMAINE = "835006";
    private final static String CAT_TYPE = "836037";
    private final static String MODEL_NAME = "AURIGA_DECISION_CAP";
    private final static String NUM_INFOROM = "0295CAF";

    private AffiliationComplexModel affiliationComplex;
    private SimpleDecisionCAP decisionCap;
    private boolean isFirst = true;
    private List<ComplexEnfantDecisionCAPTiers> listEnfant;
    private String montantCotisationRectifiee;
    private String montantTotalNotreVotreFaveur;
    private String plafondCotiBrute;
    private String plancherCotiBrute;

    @Override
    public void beforeBuildReport() throws FWIException {
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        try {
            setApp((BApplication) GlobazServer.getCurrentSystem().getApplication("AURIGA"));
        } catch (Exception e) {
            getSession().addError("unable to load Auriga Application");
            this.abort();
        }
        setFileTitle(getApp().getLabel("DOC_TITLE_DECISION_CAP", getSession().getIdLangueISO()));
    }

    @Override
    public void createDataSource() throws Exception {
        // set du template
        setTemplateFile(AUDecisionCap_Doc.MODEL_NAME);

        // set de la langue du document (langue de l'affilié dans ce cas)
        setLangueIsoDoc(getSession().getCode(affiliationComplex.getTiersSimpleModel().getLangue()));

        // remplissage du docInfo et du titre du doc
        fillDocInfo(affiliationComplex, AUDecisionCap_Doc.NUM_INFOROM);
        setDocumentTitle(getApp().getLabel("DOC_TITLE_DECISION_CAP", getSession().getIdLangueISO()) + " "
                + affiliationComplex.getAffiliationSimpleModel().getAffilieNumero());

        // mise en place du header
        putHeader(affiliationComplex);

        // chargement du catalogue de texte et des labels en fonction de la langue de l'affilié
        catalogue(AUDecisionCap_Doc.CAT_DOMAINE, AUDecisionCap_Doc.CAT_TYPE);

        String labelTypeDecision = "";
        if (AUDecisionType.DEFINITIVE.equals(decisionCap.getType())) {
            labelTypeDecision = getApp().getLabel("DOC_DEFINITIVE_TYPE_DECISION_CAP", getLangueIsoDoc());
        } else if (AUDecisionType.PROVISOIRE.equals(decisionCap.getType())) {
            labelTypeDecision = getApp().getLabel("DOC_PROVISOIRE_TYPE_DECISION_CAP", getLangueIsoDoc());
        } else if (AUDecisionType.PROVISOIRE_RECTIFICATIVE.equals(decisionCap.getType())) {
            labelTypeDecision = getApp().getLabel("DOC_PROVISOIRE_RECTIFICATIVE_TYPE_DECISION_CAP", getLangueIsoDoc());
        } else if (AUDecisionType.DEFINITIVE_RECTIFICATIVE.equals(decisionCap.getType())) {
            labelTypeDecision = getApp().getLabel("DOC_DEFINITIVE_RECTIFICATIVE_TYPE_DECISION_CAP", getLangueIsoDoc());
        } else if (AUDecisionType.PROVISOIRE_DEFINITIVE.equals(decisionCap.getType())) {
            labelTypeDecision = getApp().getLabel("DOC_PROVISOIRE_DEFINITIVE_TYPE_DECISION_CAP", getLangueIsoDoc());
        } else {
            throw new AurigaTechnicalException("Not implemented");
        }

        String concerneTexte = getTexte(1, 1, null) + " " + labelTypeDecision;
        String corpsTexte = getTexte(1, 2, null);
        String corpsAllocationTexte = getTexte(1, 3, null);
        String corpsConclusionTexte = getTexte(1, 4, null);

        String assuranceLibelleLabel = getApp().getLabel("DOC_ASSURANCE_LIBELLE_DECISION_CAP", getLangueIsoDoc());
        String periodeLabel = getApp().getLabel("DOC_PERIODE_DECISION_CAP", getLangueIsoDoc());
        String forfaitLabel = getApp().getLabel("DOC_FORFAIT_DECISION_CAP", getLangueIsoDoc());
        String revenuLabel = getApp().getLabel("DOC_REVENU_DECISION_CAP", getLangueIsoDoc());
        String tauxLabel = getApp().getLabel("DOC_TAUX_DECISION_CAP", getLangueIsoDoc());
        String cotisationBruteLabel = getApp().getLabel("DOC_COTISATION_BRUTE_DECISION_CAP", getLangueIsoDoc());
        String allocationEnfantLabel = getApp().getLabel("DOC_ALLOCATIONS_DECISION_CAP", getLangueIsoDoc());
        String nomEnfantLabel = getApp().getLabel("DOC_NOM_ENFANT_DECISION_CAP", getLangueIsoDoc());
        String prenomEnfantLabel = getApp().getLabel("DOC_PRENOM_ENFANT_DECISION_CAP", getLangueIsoDoc());
        String dateNaissanceEnfantLabel = getApp()
                .getLabel("DOC_DATE_NAISSANCE_ENFANT_DECISION_CAP", getLangueIsoDoc());
        String dateRadiationEnfantLabel = getApp()
                .getLabel("DOC_DATE_RADIATION_ENFANT_DECISION_CAP", getLangueIsoDoc());
        String montantEnfantLabel = getApp().getLabel("DOC_MONTANT_ENFANT_DECISION_CAP", getLangueIsoDoc());
        String totalAllocationLabel = getApp().getLabel("DOC_TOTAL_ALLOCATION_CAP", getLangueIsoDoc());
        String cotisationLabel = getApp().getLabel("DOC_COTISATION_DECISION_CAP", getLangueIsoDoc());
        String plancherLabel = getApp().getLabel("DOC_PLANCHER_DECISION_CAP", getLangueIsoDoc());
        String plafondLabel = getApp().getLabel("DOC_PLAFOND_DECISION_CAP", getLangueIsoDoc());
        String cotisationRectifieeLabel = getApp().getLabel("DOC_COTI_RECTIFIEE_DECISION_CAP", getLangueIsoDoc());
        String totalNotreFaveurLabel = getApp().getLabel("DOC_TOTAL_NOTRE_FAVEUR_DECISION_CAP", getLangueIsoDoc());
        String totalVotreFaveurLabel = getApp().getLabel("DOC_TOTAL_VOTRE_FAVEUR_DECISION_CAP", getLangueIsoDoc());

        // envoi des paramètres dans le template
        this.setParametres("P_CONCERNE", concerneTexte);
        this.setParametres("P_CORPS", corpsTexte);
        this.setParametres("P_ASSURANCE_LIBELLE_LABEL", assuranceLibelleLabel);
        this.setParametres("P_ASSURANCE_LIBELLE", getAssuranceLibelle());
        this.setParametres("P_PERIODE_LABEL", periodeLabel);
        this.setParametres("P_PERIODE", decisionCap.getDateDebut() + " - " + decisionCap.getDateFin());
        if (decisionCap.getCategorie().equals(CodeSystem.TYPE_ASS_CAP_10)) {
            this.setParametres("P_FORFAIT_REVENU_LABEL", revenuLabel);
            if (!JadeStringUtil.isBlankOrZero(decisionCap.getRevenuFRV())) {
                this.setParametres("P_FORFAIT_REVENU", JANumberFormatter.formatNoRound(decisionCap.getRevenuFRV()));
            } else {
                this.setParametres("P_FORFAIT_REVENU", JANumberFormatter.formatNoRound(decisionCap.getRevenuIFD()));
            }
            this.setParametres("P_TAUX_LABEL", tauxLabel);
            this.setParametres("P_TAUX", decisionCap.getTauxAssurance() + " %");
            this.setParametres("P_COTISATION_BRUTE_LABEL", cotisationBruteLabel);

            // indiquer si il s'agit d'un montant plancher ou plafond
            if (decisionCap.getCotisationBrute().equals(plancherCotiBrute)) {
                this.setParametres("P_COTISATION_BRUTE",
                        JANumberFormatter.formatNoRound(decisionCap.getCotisationBrute()) + " " + plancherLabel);
            } else if (decisionCap.getCotisationBrute().equals(plafondCotiBrute)) {
                this.setParametres("P_COTISATION_BRUTE",
                        JANumberFormatter.formatNoRound(decisionCap.getCotisationBrute()) + " " + plafondLabel);
            } else {
                this.setParametres("P_COTISATION_BRUTE",
                        JANumberFormatter.formatNoRound(decisionCap.getCotisationBrute()));
            }
        } else {
            this.setParametres("P_FORFAIT_REVENU_LABEL", forfaitLabel);
            this.setParametres("P_FORFAIT_REVENU", JANumberFormatter.formatNoRound(decisionCap.getForfait()));
        }
        if (listEnfant.size() > 0) {
            this.setParametres("P_ALLOCATION_ENFANT_LABEL", allocationEnfantLabel);
            this.setParametres("P_CORPS_ALLOCATION", corpsAllocationTexte);
            this.setParametres("P_NOM_ENFANT_LABEL", nomEnfantLabel);
            this.setParametres("P_PRENOM_ENFANT_LABEL", prenomEnfantLabel);
            this.setParametres("P_DATE_NAISSANCE_ENFANT_LABEL", dateNaissanceEnfantLabel);
            this.setParametres("P_DATE_RADIATION_ENFANT_LABEL", dateRadiationEnfantLabel);
            this.setParametres("P_MONTANT_ENFANT_LABEL", montantEnfantLabel);

            // envoi des paramètre pour les enfants
            List<Map<String, String>> listDataSource = getListLignesEnfants();
            if (!listDataSource.isEmpty()) {
                List<Map<String, String>> dataSource = new ArrayList<Map<String, String>>();
                dataSource.addAll(listDataSource);
                this.setDataSource(dataSource);
            }

            this.setParametres("P_TOTAL_ALLOCATION_LABEL", totalAllocationLabel);
            this.setParametres("P_PRESTATION", JANumberFormatter.formatNoRound(decisionCap.getPrestation()));
        }
        this.setParametres("P_COTISATION_LABEL", cotisationLabel);
        this.setParametres("P_COTISATION", JANumberFormatter.formatNoRound(decisionCap.getCotisationPeriode()));

        // si il s'agit d'une décision rectificative
        if (!JadeStringUtil.isBlankOrZero(decisionCap.getIdDecisionRectifiee())) {
            this.setParametres("P_COTISATION_RECTIFIEE_LABEL", cotisationRectifieeLabel);
            this.setParametres("P_COTISATION_RECTIFIEE", montantCotisationRectifiee);

            if (JadeStringUtil.isBlank(montantTotalNotreVotreFaveur)) {
                throw new Exception("montantTotalNotreVotreFaveur can not be null or empty");
            }

            FWCurrency total = new FWCurrency(montantTotalNotreVotreFaveur);
            if (total.isNegative()) {
                this.setParametres("P_TOTAL_FAVEUR_LABEL", totalVotreFaveurLabel);
                total.negate();
                this.setParametres("P_TOTAL_FAVEUR", total.toStringFormat());
            } else {
                this.setParametres("P_TOTAL_FAVEUR_LABEL", totalNotreFaveurLabel);
                this.setParametres("P_TOTAL_FAVEUR", total.toStringFormat());
            }
        }

        this.setParametres("P_CORPS_CONCLUSION", corpsConclusionTexte);
    }

    public AffiliationComplexModel getAffiliationComplex() {
        return affiliationComplex;
    }

    private String getAssuranceLibelle() throws Exception {
        return AFBusinessServiceLocator.getAssuranceService().getAssuranceLibelle(decisionCap.getIdAssurance(),
                getLangueIsoDoc());
    }

    public SimpleDecisionCAP getDecisionCap() {
        return decisionCap;
    }

    public List<ComplexEnfantDecisionCAPTiers> getListEnfant() {
        return listEnfant;
    }

    private List<Map<String, String>> getListLignesEnfants() {
        List<Map<String, String>> lignes = new ArrayList<Map<String, String>>();
        for (ComplexEnfantDecisionCAPTiers enfant : listEnfant) {
            Map<String, String> fields = new HashMap<String, String>();

            fields.put("F_NOM_ENFANT", enfant.getTiers().getDesignation1());
            fields.put("F_PRENOM_ENFANT", enfant.getTiers().getDesignation2());
            fields.put("F_DATE_NAISSANCE_ENFANT", enfant.getEnfantDecisionCAP().getDateNaissance());
            fields.put("F_DATE_RADIATION_ENFANT", enfant.getEnfantDecisionCAP().getDateRadiation());
            fields.put("F_MONTANT_ENFANT", JANumberFormatter.formatNoRound(enfant.getEnfantDecisionCAP().getMontant()));
            lignes.add(fields);
        }
        return lignes;
    }

    public String getMontantCotisationRectifiee() {
        return montantCotisationRectifiee;
    }

    public String getMontantTotalNotreVotreFaveur() {
        return montantTotalNotreVotreFaveur;
    }

    public String getPlafondCotiBrute() {
        return plafondCotiBrute;
    }

    public String getPlancherCotiBrute() {
        return plancherCotiBrute;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {
        if (isFirst) {
            isFirst = false;
            return true;
        }
        return false;
    }

    public void setAffiliationComplex(AffiliationComplexModel affiliationComplex) {
        this.affiliationComplex = affiliationComplex;
    }

    public void setDecisionCap(SimpleDecisionCAP decisionCap) {
        this.decisionCap = decisionCap;
    }

    public void setListEnfant(List<ComplexEnfantDecisionCAPTiers> listEnfant) {
        this.listEnfant = listEnfant;
    }

    public void setMontantCotisationRectifiee(String montantCotisationRectifiee) {
        this.montantCotisationRectifiee = montantCotisationRectifiee;
    }

    public void setMontantTotalNotreVotreFaveur(String montantTotalNotreVotreFaveur) {
        this.montantTotalNotreVotreFaveur = montantTotalNotreVotreFaveur;
    }

    public void setPlafondCotiBrute(String plafondCotiBrute) {
        this.plafondCotiBrute = plafondCotiBrute;
    }

    public void setPlancherCotiBrute(String plancherCotiBrute) {
        this.plancherCotiBrute = plancherCotiBrute;
    }
}
