package ch.globaz.amal.business.models.revenu;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.ArrayList;

public class SimpleRevenuSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeTaxation = null;
    private String forIdContribuable = null;
    private String forIdRevenu = null;
    private String forIdRevenuHistorique = null;
    private String forTypeSource = null;
    private String forTypeTaxation = null;
    private ArrayList<String> inTypeSource = null;
    private ArrayList<String> inTypeTaxation = null;

    public SimpleRevenuSearch() {
        forIdRevenu = new String();
    }

    /**
     * @return the forAnneeTaxation
     */
    public String getForAnneeTaxation() {
        return forAnneeTaxation;
    }

    public String getForIdContribuable() {
        return forIdContribuable;
    }

    public String getForIdRevenu() {
        return forIdRevenu;
    }

    /**
     * @return the forIdRevenuHistorique
     */
    public String getForIdRevenuHistorique() {
        return forIdRevenuHistorique;
    }

    public String getForTypeSource() {
        return forTypeSource;
    }

    public String getForTypeTaxation() {
        return forTypeTaxation;
    }

    public ArrayList<String> getInTypeSource() {
        return inTypeSource;
    }

    public ArrayList<String> getInTypeTaxation() {
        return inTypeTaxation;
    }

    /**
     * @param forAnneeTaxation
     *            the forAnneeTaxation to set
     */
    public void setForAnneeTaxation(String forAnneeTaxation) {
        this.forAnneeTaxation = forAnneeTaxation;
    }

    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    public void setForIdRevenu(String forIdRevenu) {
        this.forIdRevenu = forIdRevenu;
    }

    /**
     * @param forIdRevenuHistorique
     *            the forIdRevenuHistorique to set
     */
    public void setForIdRevenuHistorique(String forIdRevenuHistorique) {
        this.forIdRevenuHistorique = forIdRevenuHistorique;
    }

    public void setForTypeSource(String forTypeSource) {
        this.forTypeSource = forTypeSource;
    }

    public void setForTypeTaxation(String forTypeTaxation) {
        this.forTypeTaxation = forTypeTaxation;
    }

    public void setInTypeSource(ArrayList<String> inTypeSource) {
        this.inTypeSource = inTypeSource;
    }

    public void setInTypeTaxation(ArrayList<String> inTypeTaxation) {
        this.inTypeTaxation = inTypeTaxation;
    }

    @Override
    public Class whichModelClass() {
        return SimpleRevenu.class;
    }

}
