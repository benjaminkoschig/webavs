package ch.globaz.auriga.business.models;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

public class DecisionCAPSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = null;
    private String forDateDebutLessEqual = null;
    private String forDateFinGreaterEqual = null;
    private String forEtat = null;
    private String forIdAffiliation = null;
    private String forIdPassageFacturation = null;
    private String forNotEtat = null;
    private String forNotType = null;
    private String forType = null;

    private String fromAnnee = null;
    private List<String> inEtat = null;
    private List<String> inType = null;
    private List<String> notInIdDecision = null;

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForDateDebutLessEqual() {
        return forDateDebutLessEqual;
    }

    public String getForDateFinGreaterEqual() {
        return forDateFinGreaterEqual;
    }

    public String getForEtat() {
        return forEtat;
    }

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public String getForIdPassageFacturation() {
        return forIdPassageFacturation;
    }

    public String getForNotEtat() {
        return forNotEtat;
    }

    public String getForNotType() {
        return forNotType;
    }

    public String getForType() {
        return forType;
    }

    public String getFromAnnee() {
        return fromAnnee;
    }

    public List<String> getInEtat() {
        return inEtat;
    }

    public List<String> getInType() {
        return inType;
    }

    public List<String> getNotInIdDecision() {
        return notInIdDecision;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForDateDebutLessEqual(String forDateDebutLessEqual) {
        this.forDateDebutLessEqual = forDateDebutLessEqual;
    }

    public void setForDateFinGreaterEqual(String forDateFinGreaterEqual) {
        this.forDateFinGreaterEqual = forDateFinGreaterEqual;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    public void setForIdPassageFacturation(String forIdPassageFacturation) {
        this.forIdPassageFacturation = forIdPassageFacturation;
    }

    public void setForNotEtat(String forNotEtat) {
        this.forNotEtat = forNotEtat;
    }

    public void setForNotType(String forNotType) {
        this.forNotType = forNotType;
    }

    public void setForType(String forType) {
        this.forType = forType;
    }

    public void setFromAnnee(String fromAnnee) {
        this.fromAnnee = fromAnnee;
    }

    public void setInEtat(List<String> inEtat) {
        this.inEtat = inEtat;
    }

    public void setInType(List<String> inType) {
        this.inType = inType;
    }

    public void setNotInIdDecision(List<String> notInIdDecision) {
        this.notInIdDecision = notInIdDecision;
    }

    @Override
    public Class<SimpleDecisionCAP> whichModelClass() {
        return SimpleDecisionCAP.class;
    }
}
