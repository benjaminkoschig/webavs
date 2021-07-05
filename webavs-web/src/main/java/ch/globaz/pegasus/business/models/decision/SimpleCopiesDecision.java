/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author SCE Modele simple des copies de décisions 14 juil. 2010
 */
public class SimpleCopiesDecision extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Boolean annexes = null;

    private Boolean copies = null;

    private String idAffilieCopie = null;
    private String idCopiesDecision = null;
    private String idDecisionHeader = null;
    private String idTiersCopie = null;
    private Boolean lettreBase = null;
    private Boolean moyensDeDroit = null;
    private Boolean pageDeGarde = null;
    private Boolean plandeCalcul = null;
    private Boolean recapitulatif = null;
    private Boolean remarque = null;
    private Boolean signature = null;
    private Boolean versementA = null;

    public SimpleCopiesDecision() {
        super();
    }

    public SimpleCopiesDecision(Boolean lettreBase, Boolean annexes, Boolean copies, Boolean moyensDeDroit,
            Boolean pageDeGarde, Boolean plandeCalcul, Boolean recapitulatif, Boolean remarque, Boolean signature,
            Boolean versementA) {
        super();
        this.annexes = annexes;
        this.lettreBase = lettreBase;
        this.copies = copies;
        this.moyensDeDroit = moyensDeDroit;
        this.pageDeGarde = pageDeGarde;
        this.plandeCalcul = plandeCalcul;
        this.recapitulatif = recapitulatif;
        this.remarque = remarque;
        this.signature = signature;
        this.versementA = versementA;
    }

    /**
     * @return the annexes
     */
    public Boolean getAnnexes() {
        return annexes;
    }

    /**
     * @return the copies
     */
    public Boolean getCopies() {
        return copies;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idCopiesDecision;
    }

    /**
     * @return the idAffilieCopie
     */
    public String getIdAffilieCopie() {
        return idAffilieCopie;
    }

    /**
     * @return the idCopiesDecision
     */
    public String getIdCopiesDecision() {
        return idCopiesDecision;
    }

    /**
     * @return the idDecision
     */
    public String getIdDecisionHeader() {
        return idDecisionHeader;
    }

    /**
     * @return the idTiersCopie
     */
    public String getIdTiersCopie() {
        return idTiersCopie;
    }

    public Boolean getLettreBase() {
        if (lettreBase != null) {
            return lettreBase;
        } else {
            return false;
        }
    }

    /**
     * @return the moyensDeDroit
     */
    public Boolean getMoyensDeDroit() {
        return moyensDeDroit;
    }

    /**
     * @return the pageDeGarde
     */
    public Boolean getPageDeGarde() {
        return pageDeGarde;
    }

    /**
     * @return the plandeCalcul
     */
    public Boolean getPlandeCalcul() {
        return plandeCalcul;
    }

    /**
     * @return the decompte
     */
    public Boolean getRecapitulatif() {
        return recapitulatif;
    }

    /**
     * @return the remarque
     */
    public Boolean getRemarque() {
        return remarque;
    }

    /**
     * @return the signature
     */
    public Boolean getSignature() {
        return signature;
    }

    /**
     * @return the versementA
     */
    public Boolean getVersementA() {
        return versementA;
    }

    /**
     * @param annexes
     *            the annexes to set
     */
    public void setAnnexes(Boolean annexes) {
        this.annexes = annexes;
    }

    /**
     * @param copies
     *            the copies to set
     */
    public void setCopies(Boolean copies) {
        this.copies = copies;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idCopiesDecision = id;

    }

    /**
     * @param idAffilieCopie
     *            the idAffilieCopie to set
     */
    public void setIdAffilieCopie(String idAffilieCopie) {
        this.idAffilieCopie = idAffilieCopie;
    }

    /**
     * @param idCopiesDecision
     *            the idCopiesDecision to set
     */
    public void setIdCopiesDecision(String idCopiesDecision) {
        this.idCopiesDecision = idCopiesDecision;
    }

    /**
     * @param idDecision
     *            the idDecision to set
     */
    public void setIdDecisionHeader(String idDecision) {
        idDecisionHeader = idDecision;
    }

    /**
     * @param idTiersCopie
     *            the idTiersCopie to set
     */
    public void setIdTiersCopie(String idTiersCopie) {
        this.idTiersCopie = idTiersCopie;
    }

    public void setLettreBase(Boolean lettreBase) {
        this.lettreBase = lettreBase;
    }

    /**
     * @param moyensDeDroit
     *            the moyensDeDroit to set
     */
    public void setMoyensDeDroit(Boolean moyensDeDroit) {
        this.moyensDeDroit = moyensDeDroit;
    }

    /**
     * @param pageDeGarde
     *            the pageDeGarde to set
     */
    public void setPageDeGarde(Boolean pageDeGarde) {
        this.pageDeGarde = pageDeGarde;
    }

    /**
     * @param plandeCalcul
     *            the plandeCalcul to set
     */
    public void setPlandeCalcul(Boolean plandeCalcul) {
        this.plandeCalcul = plandeCalcul;
    }

    /**
     * @param decompte
     *            the decompte to set
     */
    public void setRecapitulatif(Boolean recapitulatif) {
        this.recapitulatif = recapitulatif;
    }

    /**
     * @param remarque
     *            the remarque to set
     */
    public void setRemarque(Boolean remarque) {
        this.remarque = remarque;
    }

    /**
     * @param signature
     *            the signature to set
     */
    public void setSignature(Boolean signature) {
        this.signature = signature;
    }

    /**
     * @param versementA
     *            the versementA to set
     */
    public void setVersementA(Boolean versementA) {
        this.versementA = versementA;
    }

    /**
     * Copy l'objet avec les paramètres par défaut
     */
    @Override
    public SimpleCopiesDecision clone() {
        return new SimpleCopiesDecision(lettreBase, annexes, copies, moyensDeDroit,
                pageDeGarde, plandeCalcul, recapitulatif, remarque, signature, versementA);
    }

}
