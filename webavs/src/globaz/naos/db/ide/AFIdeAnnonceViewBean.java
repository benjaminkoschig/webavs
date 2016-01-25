package globaz.naos.db.ide;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.tiers.TITiersViewBean;

public class AFIdeAnnonceViewBean extends AFIdeAnnonce implements FWViewBeanInterface {

    private static final long serialVersionUID = 418565569484029155L;

    private AFAffiliation affiliation = null;
    private TITiersViewBean tiers = null;
    private String adresse = "";
    private String rue = "";
    private String npa = "";
    private String localite = "";

    private String canton = "";
    private String formeJuridique = "";
    private String langueTiers = "";
    private String brancheEconomique = "";

    public boolean isTraite() {
        return CodeSystem.ETAT_ANNONCE_IDE_TRAITE.equalsIgnoreCase(getIdeAnnonceEtat());
    }

    public AFIdeAnnonceViewBean() {
        super();
    }

    @Override
    public void _afterRetrieve(BTransaction transaction) throws Exception {
        // -------- Recherche des donn�es de l'affili� --------
        AFAffiliation affiliation = new AFAffiliation();
        affiliation.setSession(getSession());
        affiliation.setAffiliationId(getIdeAnnonceIdAffiliation());
        affiliation.retrieve();
        setAffiliation(affiliation);
        // -------- Recherche des donn�es de l'affili� --------
        if (!getAffiliation().isNew()) {
            TITiersViewBean tiers = new TITiersViewBean();
            tiers.setSession(getSession());
            tiers.setIdTiers(getAffiliation().getIdTiers());
            tiers.retrieve();
            setTiers(tiers);
        }

        if (isTraite() || CodeSystem.CATEGORIE_ANNONCE_IDE_RECEPTION.equalsIgnoreCase(getIdeAnnonceCategorie())) {
            adresse = getHistAdresse();
            rue = getHistRue();
            npa = getHistNPA();
            localite = getHistLocalite();
            canton = getHistCanton();
            formeJuridique = getHistFormeJuridique();
            langueTiers = getHistLangueTiers();
            brancheEconomique = getHistBrancheEconomique();
            super.setTypeAnnonceDate(super.getHistTypeAnnonceDate());

        } else {

            TIAdresseDataSource adresseDataSource = AFIDEUtil.loadAdresseForIde(getSession(), affiliation);
            if (adresseDataSource != null) {
                adresse = AFIDEUtil.formatAdresseForIde(adresseDataSource);
                rue = adresseDataSource.rue + " " + adresseDataSource.numeroRue;
                npa = adresseDataSource.localiteNpa;
                localite = adresseDataSource.localiteNom;
                canton = adresseDataSource.canton_court;
            }

            formeJuridique = CodeSystem.getLibelle(getSession(), affiliation.getPersonnaliteJuridique());
            langueTiers = tiers.getLangueIso().toUpperCase();
            brancheEconomique = CodeSystem.getLibelle(getSession(), affiliation.getBrancheEconomique());
        }

    }

    public String getAdresse() {
        return adresse;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getNpa() {
        return npa;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getCanton() {
        return canton;
    }

    public String getFormeJuridique() {
        return formeJuridique;
    }

    public String getLangueTiers() {
        return langueTiers;
    }

    public String getBrancheEconomique() {
        return brancheEconomique;
    }

    /**
     * 
     * @see globaz.naos.util.AFIDEUtil#isAnnonceSupprimable(String, String)
     */
    public boolean canDeleteAnnonce() {
        return AFIDEUtil.isAnnonceSupprimable(getIdeAnnonceEtat(), getIdeAnnonceType());
    }

    public boolean canUpdateAnnonce() {
        return (AFIDEUtil.getListEtatAnnonceIdeEnCours().contains(getIdeAnnonceEtat()));
    }

    public AFAffiliation getAffiliation() {
        return affiliation;
    }

    public TITiersViewBean getTiers() {
        return tiers;
    }

    public void setAffiliation(AFAffiliation affiliation) {
        this.affiliation = affiliation;
    }

    public void setTiers(TITiersViewBean tiers) {
        this.tiers = tiers;
    }
}
