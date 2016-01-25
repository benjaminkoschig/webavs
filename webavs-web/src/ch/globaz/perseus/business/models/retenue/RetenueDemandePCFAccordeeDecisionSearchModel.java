package ch.globaz.perseus.business.models.retenue;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class RetenueDemandePCFAccordeeDecisionSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String DATEDEBUT_DATEVALIDATION_NUMERODECISION_DESC = "dateDebutDateValidationNumeroDecisionDESC";

    private String forCsEtatDecision = null;
    private String forCsTypeDecision = null;
    private String forIdDossier = null;
    private List<String> forIdDossiersIn = null;

    /**
     * @return the forCsEtatDecision
     */
    public String getForCsEtatDecision() {
        return forCsEtatDecision;
    }

    /**
     * @return the forCsTypeDecision
     */
    public String getForCsTypeDecision() {
        return forCsTypeDecision;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forIdDossiersIn (Liste d'id dossier)
     */
    public List<String> getForIdDossiersIn() {
        return forIdDossiersIn;
    }

    /**
     * @param forCsEtatDecision
     *            the forCsEtatDecision to set
     */
    public void setForCsEtatDecision(String forCsEtatDecision) {
        this.forCsEtatDecision = forCsEtatDecision;
    }

    /**
     * @param forCsTypeDecision
     *            the forCsTypeDecision to set
     */
    public void setForCsTypeDecision(String forCsTypeDecision) {
        this.forCsTypeDecision = forCsTypeDecision;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forIdDossiersIn
     *            the forIdDossiersIn to set (Liste d'id Dossier)
     */
    public void setForIdDossiersIn(List<String> forIdDossiersIn) {
        this.forIdDossiersIn = forIdDossiersIn;
    }

    @Override
    public Class<RetenueDemandePCFAccordeeDecision> whichModelClass() {
        return RetenueDemandePCFAccordeeDecision.class;
    }

}
