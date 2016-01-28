/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDecision;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public class DecisionApresCalculSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String WITH_CURRENT_VERSION = "currentVersion";
    public static final String FOR_MISE_EN_GED_COMPTA_WHERE_KEY = "forMiseEnGedAfterCompta";

    private String forCsEtatDecision = null;
    private String forCsTypePreparation = null;
    private String forExcludeCsEtatDecisionValide = null;
    private String forIdDecisionApresCalcul = null;
    private String forIdDecisionHeader = null;
    private String forIdDemande = null;
    private String forIdPcAccordee = null;
    private String forIdVersionDroit = null;
    private String forDateValidation = null;
    private List<String> forIdPrestationsIn = null;
    private List<String> forCsTypeDecisionIn = null;

    /**
     * Retourne la liste des codes systèmes permattant d'identifier les décisions après-calcul
     * Sauf décision d'adaptation (refus, octroi et octroi après calcul)
     * 
     * @see ch.globaz.pegasus.business.constantes.IPCDecision
     * @return la liste des cs, repris de l'interface <code>IPCDecision</code>
     * 
     */
    public static List<String> getDecisionsApresCalculCsType() {
        List<String> csDecisionsApresCalcul = new ArrayList<String>();
        csDecisionsApresCalcul.add(IPCDecision.CS_TYPE_REFUS_AC);
        csDecisionsApresCalcul.add(IPCDecision.CS_TYPE_OCTROI_AC);
        csDecisionsApresCalcul.add(IPCDecision.CS_TYPE_PARTIEL_AC);
        return csDecisionsApresCalcul;
    }

    public List<String> getForCsTypeDecisionIn() {
        return forCsTypeDecisionIn;
    }

    public void setForCsTypeDecisionIn(List<String> forCsTypeDecisionIn) {
        this.forCsTypeDecisionIn = forCsTypeDecisionIn;
    }

    public List<String> getForIdPrestationsIn() {
        return forIdPrestationsIn;
    }

    public void setForIdPrestationsIn(List<String> forIdPrestationsIn) {
        this.forIdPrestationsIn = forIdPrestationsIn;
    }

    public String getForCsEtatDecision() {
        return forCsEtatDecision;
    }

    public String getForCsTypePreparation() {
        return forCsTypePreparation;
    }

    public String getForExcludeCsEtatDecisionValide() {
        return forExcludeCsEtatDecisionValide;
    }

    /**
     * @return the forIdDecisionApresCalcul
     */
    public String getForIdDecisionApresCalcul() {
        return forIdDecisionApresCalcul;
    }

    /**
     * @return the forIdDecisionHeader
     */
    public String getForIdDecisionHeader() {
        return forIdDecisionHeader;
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdPcAccordee() {
        return forIdPcAccordee;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public void setForCsEtatDecision(String forCsEtatDecision) {
        this.forCsEtatDecision = forCsEtatDecision;
    }

    public void setForCsTypePreparation(String forCsTypePreparation) {
        this.forCsTypePreparation = forCsTypePreparation;
    }

    public void setForExcludeCsEtatDecisionValide(String forExcludeCsEtatDecisionValide) {
        this.forExcludeCsEtatDecisionValide = forExcludeCsEtatDecisionValide;
    }

    /**
     * @param forIdDecisionApresCalcul
     *            the forIdDecisionApresCalcul to set
     */
    public void setForIdDecisionApresCalcul(String forIdDecisionApresCalcul) {
        this.forIdDecisionApresCalcul = forIdDecisionApresCalcul;
    }

    /**
     * @param forIdDecisionHeader
     *            the forIdDecisionHeader to set
     */
    public void setForIdDecisionHeader(String forIdDecisionHeader) {
        this.forIdDecisionHeader = forIdDecisionHeader;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdPcAccordee(String forIdPcAccordee) {
        this.forIdPcAccordee = forIdPcAccordee;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<DecisionApresCalcul> whichModelClass() {
        return DecisionApresCalcul.class;
    }

    public String getForDateValidation() {
        return forDateValidation;
    }

    public void setForDateValidation(String forDateValidation) {
        this.forDateValidation = forDateValidation;
    }

}
