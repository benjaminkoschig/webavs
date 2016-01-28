/**
 * 
 */
package ch.globaz.amal.business.models.famille;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

/**
 * @author CBU
 * 
 */
public class FamilleContribuableSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeHistorique = null;
    private Boolean forCodeActif = null;
    private Boolean forContribuableActif = null;
    private String forFinDefinitive = null;
    private String forFinDroit = null;
    private String forIdContribuable = null;
    private String forIdFamille = null;
    private String forIdTier = null;
    private Boolean forIsContribuable = null;
    private String forNNSS = null;
    private String forNumeroContribuable = null;
    private List inNumerosContribuables = null;
    private List inTypeDemande = null;

    public FamilleContribuableSearch() {
        forIdContribuable = new String();
    }

    public String getForAnneeHistorique() {
        return forAnneeHistorique;
    }

    public Boolean getForCodeActif() {
        return forCodeActif;
    }

    public Boolean getForContribuableActif() {
        return forContribuableActif;
    }

    public String getForFinDefinitive() {
        return forFinDefinitive;
    }

    public String getForFinDroit() {
        return forFinDroit;
    }

    public String getForIdContribuable() {
        return forIdContribuable;
    }

    public String getForIdFamille() {
        return forIdFamille;
    }

    /**
     * @return the forIdTier
     */
    public String getForIdTier() {
        return forIdTier;
    }

    public Boolean getForIsContribuable() {
        return forIsContribuable;
    }

    public String getForNNSS() {
        return forNNSS;
    }

    public String getForNumeroContribuable() {
        return forNumeroContribuable;
    }

    public List getInNumerosContribuables() {
        return inNumerosContribuables;
    }

    public List getInTypeDemande() {
        return inTypeDemande;
    }

    public void setForAnneeHistorique(String forAnneeHistorique) {
        this.forAnneeHistorique = forAnneeHistorique;
    }

    public void setForCodeActif(Boolean forCodeActif) {
        this.forCodeActif = forCodeActif;
    }

    public void setForContribuableActif(Boolean forContribuableActif) {
        this.forContribuableActif = forContribuableActif;
    }

    public void setForFinDefinitive(String forFinDefinitive) {
        this.forFinDefinitive = forFinDefinitive;
    }

    public void setForFinDroit(String forFinDroit) {
        this.forFinDroit = forFinDroit;
    }

    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    public void setForIdFamille(String forIdFamille) {
        this.forIdFamille = forIdFamille;
    }

    /**
     * @param forIdTier
     *            the forIdTier to set
     */
    public void setForIdTier(String forIdTier) {
        this.forIdTier = forIdTier;
    }

    public void setForIsContribuable(Boolean forIsContribuable) {
        this.forIsContribuable = forIsContribuable;
    }

    public void setForNNSS(String forNNSS) {
        this.forNNSS = forNNSS;
    }

    public void setForNumeroContribuable(String forNumeroContribuable) {
        this.forNumeroContribuable = forNumeroContribuable;
    }

    public void setInNumerosContribuables(List inNumerosContribuables) {
        this.inNumerosContribuables = inNumerosContribuables;
    }

    public void setInTypeDemande(List inTypeDemande) {
        this.inTypeDemande = inTypeDemande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return FamilleContribuable.class;
    }

}
