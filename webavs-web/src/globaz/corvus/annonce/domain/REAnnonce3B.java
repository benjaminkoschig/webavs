package globaz.corvus.annonce.domain;

/**
 * REAAL3B
 * Représente le niveau 3A des annonces de rentes
 * 
 * @author lga
 * 
 */
public abstract class REAnnonce3B extends REAnnonce2A {

    private Integer revenuPrisEnCompte;
    private Boolean isLimiteRevenu;
    private Boolean isMinimumGaranti;
    // private Integer revenuAnnuelMoyen;
    private Integer bteMoyennePrisEnCompte;

    /**
     * Ces champs existent dans la base de données (REAAL3B) mais ne sont pas/plus gérés actuellement
     * private String ageDebutInvaliditeEpouse = "";// ZCNDEE
     * private String codeInfirmiteEpouse = "";// ZCLCOE
     * private String degreInvaliditeEpouse = "";// ZCNINE
     * private String officeAiCompEpouse = "";// ZCLAIE
     * private String survenanceEvtAssureEpouse = "";// ZCDSUE
     * private String mensualiteRenteOrdRemp = "";// ZCMROR
     */

    /**
     * @return the revenuPrisEnCompte
     */
    public final Integer getRevenuPrisEnCompte() {
        return revenuPrisEnCompte;
    }

    /**
     * @param revenuPrisEnCompte the revenuPrisEnCompte to set
     */
    public final void setRevenuPrisEnCompte(Integer revenuPrisEnCompte) {
        this.revenuPrisEnCompte = revenuPrisEnCompte;
    }

    /**
     * @return the isLimiteRevenu
     */
    public final Boolean getIsLimiteRevenu() {
        return isLimiteRevenu;
    }

    /**
     * @param isLimiteRevenu the isLimiteRevenu to set
     */
    public final void setIsLimiteRevenu(Boolean isLimiteRevenu) {
        this.isLimiteRevenu = isLimiteRevenu;
    }

    /**
     * @return the isMinimumGaranti
     */
    public final Boolean getIsMinimumGaranti() {
        return isMinimumGaranti;
    }

    /**
     * @param isMinimumGaranti the isMinimumGaranti to set
     */
    public final void setIsMinimumGaranti(Boolean isMinimumGaranti) {
        this.isMinimumGaranti = isMinimumGaranti;
    }

    // /**
    // * @return the revenuAnnuelMoyen
    // */
    // public final Integer getRevenuAnnuelMoyen() {
    // return revenuAnnuelMoyen;
    // }
    //
    // /**
    // * @param revenuAnnuelMoyen the revenuAnnuelMoyen to set
    // */
    // public final void setRevenuAnnuelMoyen(Integer revenuAnnuelMoyen) {
    // this.revenuAnnuelMoyen = revenuAnnuelMoyen;
    // }

    /**
     * @return the bteMoyennePrisEnCompte
     */
    public final Integer getBteMoyennePrisEnCompte() {
        return bteMoyennePrisEnCompte;
    }

    /**
     * @param bteMoyennePrisEnCompte the bteMoyennePrisEnCompte to set
     */
    public final void setBteMoyennePrisEnCompte(Integer bonificationTacheEducative) {
        bteMoyennePrisEnCompte = bonificationTacheEducative;
    }

    /**
     * Retourne le revenu Annuel moyen ans sans le montant BTE</br>
     * revenuAnnuelMoyenSansBTE = revenuAnnuelMoyen != null && bteMoyennePrisEnCompte</br>
     * REBACAL.YIMRAM - REBACNR.YJMBTE
     * 
     * @return le revenu Annuel moyen ans sans le montant BTE
     */
    public Integer getRevenuAnnuelMoyenSansBTE() {
        Integer value = null;
        if (getRamDeterminant() != null && bteMoyennePrisEnCompte != null) {
            value = getRamDeterminant() - bteMoyennePrisEnCompte;
        }
        return value;
    }

}
