package ch.globaz.helios.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author sel
 * 
 */
public class MandatSimpleModel extends JadeSimpleModel {

    // sel : les boolean de la nouvelle persitence (numeric(1)) ne sont pas gérée de la même manière que ceux de
    // l'ancienne persistence (char(1)).
    // private Boolean controleCompte1106 = null;
    // private Boolean estComptabiliteAvs = null;
    // private Boolean estMandatConsolida = null;
    // private Boolean estVerrouille = null;
    private String idClassificationPr = null;
    private String idMandat = null;
    private String idTiers = null;
    private String idTypePlanCompta = null;
    // private Boolean imprimerJournalAut = null;
    // private Boolean imprimerJournalMan = null;
    private String libelleDe = null;
    private String libelleFr = null;
    private String libelleIt = null;
    private String noAgence = null;
    private String noCaisse = null;

    // private Boolean utilise650dcmf = null;
    // private Boolean utiliseLivres = null;
    // private Boolean ventilerCompte1102 = null;

    /**
	 *
	 */
    public MandatSimpleModel() {
        super();
    }

    // /**
    // * @return the controleCompte1106
    // */
    // public Boolean getControleCompte1106() {
    // return this.controleCompte1106;
    // }

    //
    // /**
    // * @return the estComptabiliteAvs
    // */
    // public Boolean getEstComptabiliteAvs() {
    // return this.estComptabiliteAvs;
    // }
    //
    // /**
    // * @return the estMandatConsolida
    // */
    // public Boolean getEstMandatConsolida() {
    // return this.estMandatConsolida;
    // }
    //
    // /**
    // * @return the estVerrouille
    // */
    // public Boolean getEstVerrouille() {
    // return this.estVerrouille;
    // }

    /**
     * @return the idMandat
     */
    @Override
    public String getId() {
        return idMandat;
    }

    /**
     * @return the idClassificationPr
     */
    public String getIdClassificationPr() {
        return idClassificationPr;
    }

    /**
     * @return the idMandat
     */
    public String getIdMandat() {
        return idMandat;
    }

    /**
     * @return the idTiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return the idTypePlanCompta
     */
    public String getIdTypePlanCompta() {
        return idTypePlanCompta;
    }

    // /**
    // * @return the imprimerJournalAut
    // */
    // public Boolean getImprimerJournalAut() {
    // return this.imprimerJournalAut;
    // }
    //
    // /**
    // * @return the imprimerJournalMan
    // */
    // public Boolean getImprimerJournalMan() {
    // return this.imprimerJournalMan;
    // }

    /**
     * @return the libelleDe
     */
    public String getLibelleDe() {
        return libelleDe;
    }

    /**
     * @return the libelleFr
     */
    public String getLibelleFr() {
        return libelleFr;
    }

    /**
     * @return the libelleIt
     */
    public String getLibelleIt() {
        return libelleIt;
    }

    /**
     * @return the noAgence
     */
    public String getNoAgence() {
        return noAgence;
    }

    /**
     * @return the noCaisse
     */
    public String getNoCaisse() {
        return noCaisse;
    }

    // /**
    // * @return the utilise650dcmf
    // */
    // public Boolean getUtilise650dcmf() {
    // return this.utilise650dcmf;
    // }
    //
    // /**
    // * @return the utiliseLivres
    // */
    // public Boolean getUtiliseLivres() {
    // return this.utiliseLivres;
    // }
    //
    // /**
    // * @return the ventilerCompte1102
    // */
    // public Boolean getVentilerCompte1102() {
    // return this.ventilerCompte1102;
    // }

    // /**
    // * @param controleCompte1106
    // */
    // public void setControleCompte1106(Boolean controleCompte1106) {
    // this.controleCompte1106 = controleCompte1106;
    // }

    // /**
    // * @param estComptabiliteAvs
    // */
    // public void setEstComptabiliteAvs(Boolean estComptabiliteAvs) {
    // this.estComptabiliteAvs = estComptabiliteAvs;
    // }
    //
    // /**
    // * @param estMandatConsolida
    // */
    // public void setEstMandatConsolida(Boolean estMandatConsolida) {
    // this.estMandatConsolida = estMandatConsolida;
    // }
    //
    // /**
    // * @param estVerrouille
    // */
    // public void setEstVerrouille(Boolean estVerrouille) {
    // this.estVerrouille = estVerrouille;
    // }

    /**
     * @param idMandat
     */
    @Override
    public void setId(String idMandat) {
        this.idMandat = idMandat;
    }

    /**
     * @param idClassificationPr
     */
    public void setIdClassificationPr(String idClassificationPr) {
        this.idClassificationPr = idClassificationPr;
    }

    /**
     * @param idMandat
     */
    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }

    /**
     * @param idTiers
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @param idTypePlanCompta
     */
    public void setIdTypePlanCompta(String idTypePlanCompta) {
        this.idTypePlanCompta = idTypePlanCompta;
    }

    // /**
    // * @param imprimerJournalAut
    // */
    // public void setImprimerJournalAut(Boolean imprimerJournalAut) {
    // this.imprimerJournalAut = imprimerJournalAut;
    // }
    //
    // /**
    // * @param imprimerJournalMan
    // */
    // public void setImprimerJournalMan(Boolean imprimerJournalMan) {
    // this.imprimerJournalMan = imprimerJournalMan;
    // }

    /**
     * @param libelleDe
     */
    public void setLibelleDe(String libelleDe) {
        this.libelleDe = libelleDe;
    }

    /**
     * @param libelleFr
     */
    public void setLibelleFr(String libelleFr) {
        this.libelleFr = libelleFr;
    }

    /**
     * @param libelleIt
     */
    public void setLibelleIt(String libelleIt) {
        this.libelleIt = libelleIt;
    }

    /**
     * @param noAgence
     */
    public void setNoAgence(String noAgence) {
        this.noAgence = noAgence;
    }

    /**
     * @param noCaisse
     */
    public void setNoCaisse(String noCaisse) {
        this.noCaisse = noCaisse;
    }

    // /**
    // * @param utilise650dcmf
    // */
    // public void setUtilise650dcmf(Boolean utilise650dcmf) {
    // this.utilise650dcmf = utilise650dcmf;
    // }
    //
    // /**
    // * @param utiliseLivres
    // */
    // public void setUtiliseLivres(Boolean utiliseLivres) {
    // this.utiliseLivres = utiliseLivres;
    // }
    //
    // /**
    // * @param ventilerCompte1102
    // */
    // public void setVentilerCompte1102(Boolean ventilerCompte1102) {
    // this.ventilerCompte1102 = ventilerCompte1102;
    // }
}
