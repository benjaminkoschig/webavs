package ch.globaz.musca.business.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jonas Paratte (JPA)
 * 
 */
public class PlanFacturationPassageSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 8817912894289381847L;

    private String forIdPassage;
    private String forNotIdPassage;
    private String likeLibellePassage;
    private String forTypeFacturation;
    private String forEtat;
    private List<String> forEtats;
    private List<String> forTypesFacturation;

    public String getForEtat() {
        return forEtat;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public String getForTypeFacturation() {
        return forTypeFacturation;
    }

    public void setForTypeFacturation(String forTypeFacturation) {
        this.forTypeFacturation = forTypeFacturation;
    }

    public String getLikeLibellePassage() {
        return likeLibellePassage;
    }

    public void setLikeLibellePassage(String likeLibellePassage) {
        this.likeLibellePassage = likeLibellePassage;
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    public void setForIdPassage(String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    public List<String> getForEtats() {
        return forEtats;
    }

    public void setForEtats(List<String> forEtats) {
        this.forEtats = forEtats;
    }

    public void setForEtats(String... forEtats) {
        this.forEtats = Arrays.asList(forEtats);
    }

    public void setForTypesFacturation(String... modules) {
        forTypesFacturation = Arrays.asList(modules);
    }

    public List<String> getForTypesFacturation() {
        return forTypesFacturation;
    }

    /**
     * @return the forNotIdPassage
     */
    public String getForNotIdPassage() {
        return forNotIdPassage;
    }

    /**
     * @param forNotIdPassage the forNotIdPassage to set
     */
    public void setForNotIdPassage(String forNotIdPassage) {
        this.forNotIdPassage = forNotIdPassage;
    }

    @Override
    public Class<PlanFacturationPassageComplexModel> whichModelClass() {
        return PlanFacturationPassageComplexModel.class;
    }

}
