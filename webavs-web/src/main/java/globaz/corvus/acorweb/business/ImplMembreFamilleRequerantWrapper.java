package globaz.corvus.acorweb.business;

import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

public class ImplMembreFamilleRequerantWrapper implements ISFMembreFamilleRequerant {

    // ~ Instance fields
    // --------------------------------------------------------------------------------------------

    public final static String NO_CS_RELATION_BLANK = "no-relation";

    public final static String NO_CS_RELATION_EX_CONJOINT_DU_CONJOINT = "ex-conjoint";
    private String idMFDuConjoint = "";

    private ISFMembreFamilleRequerant membre;
    private String nssConjoint = "";
    private PRTiersWrapper tiersRequerant;
    private String relationAuRequerant = null;

    /**
     * Crée une nouvelle instance de la classe ISFMembreFamilleRequerantWrapper.
     *
     * @param membre
     *            DOCUMENT ME!
     */
    public ImplMembreFamilleRequerantWrapper(ISFMembreFamilleRequerant membre, PRTiersWrapper tiersRequerant) {
        this.membre = membre;
        this.tiersRequerant = tiersRequerant;
    }

    /**
     * getter pour l'attribut cs canton domicile.
     *
     * @return la valeur courante de l'attribut cs canton domicile
     */
    @Override
    public String getCsCantonDomicile() {
        return membre.getCsCantonDomicile();
    }

    @Override
    public String getCsEtatCivil() {
        return membre.getCsEtatCivil();
    }

    /**
     * getter pour l'attribut cs nationalite.
     *
     * @return la valeur courante de l'attribut cs nationalite
     */
    @Override
    public String getCsNationalite() {
        return membre.getCsNationalite();
    }

    /**
     * getter pour l'attribut cs sexe.
     *
     * @return la valeur courante de l'attribut cs sexe
     */
    @Override
    public String getCsSexe() {
        return membre.getCsSexe();
    }

    /**
     * getter pour l'attribut date deces.
     *
     * @return la valeur courante de l'attribut date deces
     */
    @Override
    public String getDateDeces() {
        return membre.getDateDeces();
    }

    /**
     * getter pour l'attribut date naissance.
     *
     * @return la valeur courante de l'attribut date naissance
     */
    @Override
    public String getDateNaissance() {
        return membre.getDateNaissance();
    }

    // ~ Methods
    // ----------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut id membre famille.
     *
     * @return la valeur courante de l'attribut id membre famille
     */
    @Override
    public String getIdMembreFamille() {
        return membre.getIdMembreFamille();
    }

    public String getIdMFDuConjoint() {
        return idMFDuConjoint;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.hera.api.ISFMembreFamilleRequerant#getIdTiers()
     */
    @Override
    public String getIdTiers() {
        return membre.getIdTiers();
    }

    /**
     * getter pour l'attribut nom.
     *
     * @return la valeur courante de l'attribut nom
     */
    @Override
    public String getNom() {
        return membre.getNom();
    }

    /**
     * Retourne le no AVS du membre de la situation familiale ou un no AVS bidon pour les membres qui ont ete saisi
     * sans no AVS.
     *
     * @return la valeur courante de l'attribut nss
     */
    @Override
    public String getNss() {
        return membre.getNss();
    }

    public String getNssConjoint() {
        return nssConjoint;
    }

    /**
     * getter pour l'attribut nss direct.
     *
     * @return la valeur courante de l'attribut nss direct
     */
    public String getNssDirect() {
        return membre.getNss();
    }

    /**
     * getter pour l'attribut prenom.
     *
     * @return la valeur courante de l'attribut prenom
     */
    @Override
    public String getPrenom() {
        return membre.getPrenom();
    }

    // On surcharge cette methode set/get de manière à biaiser le système, pour éviter que lors
    // du traitement des exConjoints du conjoint du requérant, ils apparaissent comme conjoint du requérant et non
    // comme ex-conjoints.
    @Override
    public String getRelationAuRequerant() {
        if (JadeStringUtil.isBlankOrZero(relationAuRequerant)) {
            return membre.getRelationAuRequerant();
        } else {
            return relationAuRequerant;
        }
    }

    public void setIdMFDuConjoint(String idMFDuConjoint) {
        this.idMFDuConjoint = idMFDuConjoint;
    }

    public void setNssConjoint(String nssConjoint) {
        this.nssConjoint = nssConjoint;
    }

    public void setRelationAuRequerant(String relationAuRequerant) {
        this.relationAuRequerant = relationAuRequerant;
    }

    @Override
    public String getPays() {
        return membre.getPays();
    }


    public PRTiersWrapper getTiersRequerant() {
        return tiersRequerant;
    }

    public void setTiersRequerant(PRTiersWrapper tiersRequerant) {
        this.tiersRequerant = tiersRequerant;
    }
}
