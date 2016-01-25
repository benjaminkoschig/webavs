/*
 * Créé le 12 juil. 07
 */

package globaz.corvus.vb.ci;

import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * @author BSC
 */

public class RESaisieManuelleInscriptionCIViewBean extends PRAbstractViewBeanSupport {

    private String anneeCotisations = "";
    private Boolean attenteCiAdd = Boolean.FALSE;
    private String brancheEconomique = "";
    private String codeADS = "";
    private String codeExtourne = "";
    private String codeParticulier = "";
    private String codeSpecial = "";
    private String codeSplitting = "";
    private String genreCotisation = "";
    private String idRCI = "";
    // Demande de rassemblement
    private String idTiers = "";
    private String isSequenceAjouterCI = "";
    private String moisDebutCotisations = "";
    private String moisFinCotisations = "";
    private String noAffilie = "";
    private String numeroAgence = "";
    // Inscription CI
    private String numeroCaisse = "";
    private String partBonifAssist = "";
    private String revenu = "";
    private String attenteCIAdditionnel;

    /**
     * @return
     */
    public String getAnneeCotisations() {
        return anneeCotisations;
    }

    public Boolean getAttenteCiAdd() {
        return attenteCiAdd;
    }

    /**
     * @return
     */
    public String getBrancheEconomique() {
        return brancheEconomique;
    }

    /**
     * @return
     */
    public String getBrancheEconomiqueCode() {
        return getSession().getCode(getBrancheEconomique());
    }

    /**
     * @return
     */
    public String getCodeADS() {
        return codeADS;
    }

    public String getCodeExtourne() {
        return codeExtourne;
    }

    public String getCodeParticulier() {
        return codeParticulier;
    }

    /**
     * @return
     */
    public String getCodeSpecial() {
        return codeSpecial;
    }

    /**
     * @return
     */
    public String getCodeSplitting() {
        return codeSplitting;
    }

    /**
     * @return
     */
    public String getGenreCotisation() {
        return genreCotisation;
    }

    /**
     * @return
     */
    public String getIdRCI() {
        return idRCI;
    }

    /**
     * @return
     */
    public String getIdTiers() {
        return idTiers;
    }

    public String getIsSequenceAjouterCI() {
        return isSequenceAjouterCI;
    }

    /**
     * @return
     */
    public String getMoisDebutCotisations() {
        return moisDebutCotisations;
    }

    /**
     * @return
     */
    public String getMoisFinCotisations() {
        return moisFinCotisations;
    }

    /**
     * @return
     */
    public String getNoAffilie() {
        return noAffilie;
    }

    /**
     * @return
     */
    public String getNumeroAgence() {
        return numeroAgence;
    }

    /**
     * @return
     */
    public String getNumeroCaisse() {
        return numeroCaisse;
    }

    /**
     * @return
     */
    public String getPartBonifAssist() {
        return partBonifAssist;
    }

    /**
     * @return
     */
    public String getRevenu() {
        return revenu;
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(getSession());
    }

    public boolean isSequenceAjouterCI() {
        return !JadeStringUtil.isBlank(isSequenceAjouterCI);
    }

    /**
     * @param string
     */
    public void setAnneeCotisations(String string) {
        anneeCotisations = string;
    }

    public void setAttenteCiAdd(Boolean attenteCiAdd) {
        this.attenteCiAdd = attenteCiAdd;
    }

    /**
     * @param string
     */
    public void setBrancheEconomique(String string) {
        brancheEconomique = string;
    }

    /**
     * @param string
     */
    public void setCodeADS(String string) {
        codeADS = string;
    }

    public void setCodeExtourne(String codeExtourne) {
        this.codeExtourne = codeExtourne;
    }

    public void setCodeParticulier(String codeParticulier) {
        this.codeParticulier = codeParticulier;
    }

    /**
     * @param string
     */
    public void setCodeSpecial(String string) {
        codeSpecial = string;
    }

    /**
     * @param string
     */
    public void setCodeSplitting(String string) {
        codeSplitting = string;
    }

    /**
     * @param string
     */
    public void setGenreCotisation(String string) {
        genreCotisation = string;
    }

    /**
     * @param string
     */
    public void setIdRCI(String string) {
        idRCI = string;
    }

    /**
     * @param string
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    public void setIsSequenceAjouterCI(String isSequenceAjouterCI) {
        this.isSequenceAjouterCI = isSequenceAjouterCI;
    }

    /**
     * @param string
     */
    public void setMoisDebutCotisations(String string) {
        moisDebutCotisations = string;
    }

    /**
     * @param string
     */
    public void setMoisFinCotisations(String string) {
        moisFinCotisations = string;
    }

    /**
     * @param string
     */
    public void setNoAffilie(String string) {
        noAffilie = string;
    }

    /**
     * @param string
     */
    public void setNumeroAgence(String string) {
        numeroAgence = string;
    }

    /**
     * @param string
     */
    public void setNumeroCaisse(String string) {
        numeroCaisse = string;
    }

    /**
     * @param string
     */
    public void setPartBonifAssist(String string) {
        partBonifAssist = string;
    }

    /**
     * @param string
     */
    public void setRevenu(String string) {
        revenu = string;
    }

    @Override
    public boolean validate() {
        return false;
    }

    public void setAttenteCIAdditionnel(String attenteCIAdditionnel) {
        this.attenteCIAdditionnel = attenteCIAdditionnel;
    }

    public String getAttenteCIAdditionnel() {
        return attenteCIAdditionnel;
    }
}
