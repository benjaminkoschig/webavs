package globaz.aries.print;

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
import ch.globaz.aries.business.beans.decisioncgas.DecisionCGASBean;
import ch.globaz.aries.business.constantes.ARDecisionType;
import ch.globaz.aries.business.exceptions.AriesTechnicalException;
import ch.globaz.aries.business.models.SimpleDetailDecisionCGAS;
import ch.globaz.aries.business.services.AriesServiceLocator;
import ch.globaz.naos.business.model.AffiliationComplexModel;

public class ARDecisionCgas_Doc extends ARAbstractDoc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String CAT_DOMAINE = "835006";
    private final static String CAT_TYPE = "836038";
    private final static String MODEL_NAME = "ARIES_DECISION_CGAS";
    private final static String NUM_INFOROM = "0296CAF";

    private AffiliationComplexModel affiliationComplex;
    private DecisionCGASBean decisionCgasBean;
    private boolean isFirst = true;
    private String montantCotisationRectifiee;
    private String montantTotalNotreVotreFaveur;
    private String plafondCotiAnnuelle;

    @Override
    public void beforeBuildReport() throws FWIException {
    }

    @Override
    public void beforeExecuteReport() {
        try {
            setApp((BApplication) GlobazServer.getCurrentSystem().getApplication("ARIES"));
        } catch (Exception e) {
            getSession().addError("unable to load Aries Application");
            this.abort();
        }
        setFileTitle(getApp().getLabel("DOC_TITLE_DECISION_CGAS", getSession().getIdLangueISO()));
    }

    @Override
    public void createDataSource() throws Exception {
        // set du template
        setTemplateFile(ARDecisionCgas_Doc.MODEL_NAME);

        // set de la langue du document (langue de l'affilié dans ce cas)
        setLangueIsoDoc(getSession().getCode(affiliationComplex.getTiersSimpleModel().getLangue()));

        // remplissage du docInfo et du titre du doc
        fillDocInfo(affiliationComplex, ARDecisionCgas_Doc.NUM_INFOROM);
        setDocumentTitle(getApp().getLabel("DOC_TITLE_DECISION_CGAS", getSession().getIdLangueISO()) + " "
                + affiliationComplex.getAffiliationSimpleModel().getAffilieNumero());

        // mise en place du header
        putHeader(affiliationComplex);

        // chargement du catalogue de texte et des labels en fonction de la langue de l'affilié
        catalogue(ARDecisionCgas_Doc.CAT_DOMAINE, ARDecisionCgas_Doc.CAT_TYPE);

        String labelTypeDecision = "";
        if (ARDecisionType.DEFINITIVE.equals(decisionCgasBean.getDecisionCGAS().getType())) {
            labelTypeDecision = getApp().getLabel("DOC_DEFINITIVE_TYPE_DECISION_CGAS", getLangueIsoDoc());
        } else if (ARDecisionType.PROVISOIRE.equals(decisionCgasBean.getDecisionCGAS().getType())) {
            labelTypeDecision = getApp().getLabel("DOC_PROVISOIRE_TYPE_DECISION_CGAS", getLangueIsoDoc());
        } else if (ARDecisionType.PROVISOIRE_RECTIFICATIVE.equals(decisionCgasBean.getDecisionCGAS().getType())) {
            labelTypeDecision = getApp().getLabel("DOC_PROVISOIRE_RECTIFICATIVE_TYPE_DECISION_CGAS", getLangueIsoDoc());
        } else if (ARDecisionType.DEFINITIVE_RECTIFICATIVE.equals(decisionCgasBean.getDecisionCGAS().getType())) {
            labelTypeDecision = getApp().getLabel("DOC_DEFINITIVE_RECTIFICATIVE_TYPE_DECISION_CGAS", getLangueIsoDoc());
        } else if (ARDecisionType.PROVISOIRE_DEFINITIVE.equals(decisionCgasBean.getDecisionCGAS().getType())) {
            labelTypeDecision = getApp().getLabel("DOC_PROVISOIRE_DEFINITIVE_TYPE_DECISION_CGAS", getLangueIsoDoc());
        } else {
            throw new AriesTechnicalException("Not implemented");
        }

        String concerneTexte = getTexte(1, 1, null) + " " + labelTypeDecision;
        String corpsTexte = getTexte(1, 2, null);
        String corpsConclusionTexte = getTexte(1, 3, null);

        String periodeLabel = getApp().getLabel("DOC_PERIODE_DECISION_CGAS", getLangueIsoDoc());
        String detailsLabel = getApp().getLabel("DOC_DETAILS_DECISION_CGAS", getLangueIsoDoc());
        String libelleLabel = getApp().getLabel("DOC_LIBELLE_DECISION_CGAS", getLangueIsoDoc());
        String nombreLabel = getApp().getLabel("DOC_NOMBRE_DECISION_CGAS", getLangueIsoDoc());
        String montantUnitaireLabel = getApp().getLabel("DOC_MONTANT_UNITAIRE_DECISION_CGAS", getLangueIsoDoc());
        String montantTotalLabel = getApp().getLabel("DOC_MONTANT_TOTAL_DECISION_CGAS", getLangueIsoDoc());
        String cotisationLabel = getApp().getLabel("DOC_COTISATION_DECISION_CGAS", getLangueIsoDoc());
        String cotisationRectifieeLabel = getApp().getLabel("DOC_COTI_RECTIFIEE_DECISION_CGAS", getLangueIsoDoc());
        String totalNotreFaveurLabel = getApp().getLabel("DOC_TOTAL_NOTRE_FAVEUR_DECISION_CGAS", getLangueIsoDoc());
        String totalVotreFaveurLabel = getApp().getLabel("DOC_TOTAL_VOTRE_FAVEUR_DECISION_CGAS", getLangueIsoDoc());
        String plafondLabel = getApp().getLabel("DOC_PLAFOND_DECISION_CGAS", getLangueIsoDoc());

        // envoi des paramètres dans le template
        this.setParametres("P_CONCERNE", concerneTexte);
        this.setParametres("P_CORPS", corpsTexte);
        this.setParametres("P_CORPS_CONCLUSION", corpsConclusionTexte);
        this.setParametres("P_PERIODE_LABEL", periodeLabel);
        this.setParametres("P_PERIODE", decisionCgasBean.getDecisionCGAS().getDateDebut() + " - "
                + decisionCgasBean.getDecisionCGAS().getDateFin());

        this.setParametres("P_DETAILS_LABEL", detailsLabel);
        this.setParametres("P_LIBELLE_LABEL", libelleLabel);
        this.setParametres("P_NOMBRE_LABEL", nombreLabel);
        this.setParametres("P_MONTANT_UNITAIRE_LABEL", montantUnitaireLabel);
        this.setParametres("P_MONTANT_TOTAL_LABEL", montantTotalLabel);

        // // envoi des paramètre pour les details
        List<Map<String, String>> listDataSource = getListLignesDetail();
        if (!listDataSource.isEmpty()) {
            List<Map<String, String>> dataSource = new ArrayList<Map<String, String>>();
            dataSource.addAll(listDataSource);
            this.setDataSource(dataSource);
        }

        this.setParametres("P_COTISATION_LABEL", cotisationLabel);
        String cotisationAnnuelle = decisionCgasBean.getDecisionCGAS().getCotisationAnnuelle();
        if (cotisationAnnuelle.equals(plafondCotiAnnuelle)) {
            this.setParametres("P_COTISATION",
                    JANumberFormatter.formatNoRound(getDecisionCgasBean().getDecisionCGAS().getCotisationPeriode())
                            + " " + plafondLabel);
        } else {
            this.setParametres("P_COTISATION",
                    JANumberFormatter.formatNoRound(getDecisionCgasBean().getDecisionCGAS().getCotisationPeriode()));
        }

        // si il s'agit d'une décision rectificative
        if (!JadeStringUtil.isBlankOrZero(decisionCgasBean.getDecisionCGAS().getIdDecisionRectifiee())) {
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
    }

    public AffiliationComplexModel getAffiliationComplex() {
        return affiliationComplex;
    }

    public DecisionCGASBean getDecisionCgasBean() {
        return decisionCgasBean;
    }

    private List<Map<String, String>> getListLignesDetail() throws Exception {
        List<Map<String, String>> lignes = new ArrayList<Map<String, String>>();
        List<SimpleDetailDecisionCGAS> listDetail = AriesServiceLocator.getDecisionCGASService()
                .beanToListDetailDecisionCGAS(decisionCgasBean);
        for (SimpleDetailDecisionCGAS detailDecision : listDetail) {
            Map<String, String> fields = new HashMap<String, String>();

            fields.put("F_LIBELLE", CodeSystem.getLibelleIso(getSession(), detailDecision.getType(), getLangueIsoDoc()));
            fields.put("F_NOMBRE", JANumberFormatter.formatNoRound(detailDecision.getNombre()));
            fields.put("F_MONTANT_UNITAIRE", JANumberFormatter.formatNoRound(detailDecision.getMontant()));
            fields.put("F_MONTANT_TOTAL", JANumberFormatter.formatNoRound(detailDecision.getTotal()));
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

    public String getPlafondCotiAnnuelle() {
        return plafondCotiAnnuelle;
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

    public void setDecisionCgasBean(DecisionCGASBean decisionCgasBean) {
        this.decisionCgasBean = decisionCgasBean;
    }

    public void setMontantCotisationRectifiee(String montantCotisationRectifiee) {
        this.montantCotisationRectifiee = montantCotisationRectifiee;
    }

    public void setMontantTotalNotreVotreFaveur(String montantTotalNotreVotreFaveur) {
        this.montantTotalNotreVotreFaveur = montantTotalNotreVotreFaveur;
    }

    public void setPlafondCotiAnnuelle(String plafondCotiAnnuelle) {
        this.plafondCotiAnnuelle = plafondCotiAnnuelle;
    }
}
