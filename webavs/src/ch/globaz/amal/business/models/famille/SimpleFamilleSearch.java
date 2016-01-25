package ch.globaz.amal.business.models.famille;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.ArrayList;

public class SimpleFamilleSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateNaissance = null;
    private String forDateNaissanceGOE = null;
    private String forDateNaissanceLOE = null;
    private String forDifferentPereMereEnfant = null;
    private String forFinDefinitive = null;
    private String forFinDefinitiveGOE = null;
    private String forFinDefinitiveLOE = null;
    private String forIdContribuable = null;
    private String forIdFamille = null;
    private String forIdTiers = null;
    private String forIdTiersNotEmpty = null;
    private String forNoPersonne = null;
    private String forPereMereEnfant = null;
    private ArrayList<String> inNumerosPersonnes = null;
    private ArrayList<String> inPereMereEnfant = null;
    private Boolean isContribuable = null;
    private String likeNoAVS = null;
    private String likeNomPrenom = null;

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForDateNaissanceGOE() {
        return forDateNaissanceGOE;
    }

    public String getForDateNaissanceLOE() {
        return forDateNaissanceLOE;
    }

    public String getForDifferentPereMereEnfant() {
        return forDifferentPereMereEnfant;
    }

    public String getForFinDefinitive() {
        return forFinDefinitive;
    }

    /**
     * @return the forFinDefinitiveGOE
     */
    public String getForFinDefinitiveGOE() {
        return forFinDefinitiveGOE;
    }

    public String getForFinDefinitiveLOE() {
        return forFinDefinitiveLOE;
    }

    public String getForIdContribuable() {
        return forIdContribuable;
    }

    public String getForIdFamille() {
        return forIdFamille;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForIdTiersNotEmpty() {
        return forIdTiersNotEmpty;
    }

    /**
     * @return the forNoPersonne
     */
    public String getForNoPersonne() {
        return forNoPersonne;
    }

    public String getForPereMereEnfant() {
        return forPereMereEnfant;
    }

    public ArrayList<String> getInNumerosPersonnes() {
        return inNumerosPersonnes;
    }

    public ArrayList<String> getInPereMereEnfant() {
        return inPereMereEnfant;
    }

    public Boolean getIsContribuable() {
        return isContribuable;
    }

    /**
     * @return the likeNoAVS
     */
    public String getLikeNoAVS() {
        return likeNoAVS;
    }

    /**
     * @return the likeNomPrenom
     */
    public String getLikeNomPrenom() {
        return likeNomPrenom;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForDateNaissanceGOE(String forDateNaissanceGOE) {
        this.forDateNaissanceGOE = forDateNaissanceGOE;
    }

    public void setForDateNaissanceLOE(String forDateNaissanceLOE) {
        this.forDateNaissanceLOE = forDateNaissanceLOE;
    }

    public void setForDifferentPereMereEnfant(String forDifferentPereMereEnfant) {
        this.forDifferentPereMereEnfant = forDifferentPereMereEnfant;
    }

    public void setForFinDefinitive(String forFinDefinitive) {
        this.forFinDefinitive = forFinDefinitive;
    }

    /**
     * @param forFinDefinitiveGOE
     *            the forFinDefinitiveGOE to set
     */
    public void setForFinDefinitiveGOE(String forFinDefinitiveGOE) {
        this.forFinDefinitiveGOE = forFinDefinitiveGOE;
    }

    public void setForFinDefinitiveLOE(String forFinDefinitiveLOE) {
        this.forFinDefinitiveLOE = forFinDefinitiveLOE;
    }

    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    public void setForIdFamille(String forIdFamille) {
        this.forIdFamille = forIdFamille;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForIdTiersNotEmpty(String forIdTiersNotEmpty) {
        this.forIdTiersNotEmpty = forIdTiersNotEmpty;
    }

    /**
     * @param forNoPersonne
     *            the forNoPersonne to set
     */
    public void setForNoPersonne(String forNoPersonne) {
        this.forNoPersonne = forNoPersonne;
    }

    public void setForPereMereEnfant(String forPereMereEnfant) {
        this.forPereMereEnfant = forPereMereEnfant;
    }

    public void setInNumerosPersonnes(ArrayList<String> inNumerosPersonnes) {
        this.inNumerosPersonnes = inNumerosPersonnes;
    }

    public void setInPereMereEnfant(ArrayList<String> inPereMereEnfant) {
        this.inPereMereEnfant = inPereMereEnfant;
    }

    public void setIsContribuable(Boolean isContribuable) {
        this.isContribuable = isContribuable;
    }

    /**
     * @param likeNoAVS
     *            the likeNoAVS to set
     */
    public void setLikeNoAVS(String likeNoAVS) {
        this.likeNoAVS = likeNoAVS;
    }

    /**
     * @param likeNomPrenom
     *            the likeNomPrenom to set
     */
    public void setLikeNomPrenom(String likeNomPrenom) {
        this.likeNomPrenom = likeNomPrenom;
    }

    @Override
    public Class whichModelClass() {
        return SimpleFamille.class;
    }
}
