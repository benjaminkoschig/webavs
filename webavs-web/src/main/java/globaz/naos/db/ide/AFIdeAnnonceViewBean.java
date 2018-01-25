package globaz.naos.db.ide;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.tiers.TITiersViewBean;

public class AFIdeAnnonceViewBean extends AFIdeAnnonce implements FWViewBeanInterface {

    private static final long serialVersionUID = 418565569484029155L;

    private static final String CODE_NOGA_INCONNU = "990099";

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

    // D0181
    private String naissance = "";
    private String activite = "";
    /**
     * code noga selon le registre != code noga dans l'affiliation
     */
    private String noga = "";
    private String erreurNoga = "";
    private String prenomNom = "";

    public boolean isTraite() {
        return CodeSystem.ETAT_ANNONCE_IDE_TRAITE.equalsIgnoreCase(getIdeAnnonceEtat());
    }

    public AFIdeAnnonceViewBean() {
        super();
    }

    @Override
    public void _afterRetrieve(BTransaction transaction) throws Exception {
        // -------- Recherche des données de l'affilié --------
        AFAffiliation affiliationRetrieved = new AFAffiliation();
        affiliationRetrieved.setSession(getSession());
        affiliationRetrieved.setAffiliationId(getIdeAnnonceIdAffiliation());
        affiliationRetrieved.retrieve();
        setAffiliation(affiliationRetrieved);
        // -------- Recherche des données de l'affilié --------
        if (!getAffiliation().isNew()) {
            TITiersViewBean tiersRetrieved = new TITiersViewBean();
            tiersRetrieved.setSession(getSession());
            tiersRetrieved.setIdTiers(getAffiliation().getIdTiers());
            tiersRetrieved.retrieve();
            setTiers(tiersRetrieved);
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
            naissance = getHistNaissance();
            activite = getHistActivite();
            noga = AFIDEUtil.formatNogaRegistre(getHistNoga(), transaction.getSession());

            if (!isCodeNogaKnown(noga)) {
                if (CODE_NOGA_INCONNU.equals(noga)) {
                    erreurNoga = FWMessageFormat.format(
                            getSession().getApplication().getLabel("NAOS_CODE_NOGA_INCONNU",
                                    getSession().getIdLangueISO()), noga);
                } else {
                    erreurNoga = FWMessageFormat.format(
                            getSession().getApplication().getLabel("NAOS_CODE_NOGA_INDEFINI",
                                    getSession().getIdLangueISO()), noga);
                }
            }

        } else {
            TIAdresseDataSource adresseDataSource = AFIDEUtil.loadAdresseFromCascadeIde(affiliation);

            if (!adresseDataSource.getData().get(TIAbstractAdresseDataSource.ADRESSE_ID_ADRESSE).isEmpty()) {
                adresse = AFIDEUtil.formatAdresseForIde(adresseDataSource);
                rue = adresseDataSource.rue + " " + adresseDataSource.numeroRue;
                npa = adresseDataSource.localiteNpa;
                localite = adresseDataSource.localiteNom;
                canton = adresseDataSource.canton_court;
            } else {

                setMessageErreurForTechnicalUser(getSession().getLabel(
                        "NAOS_ANNONCE_IDE_CREATION_MUTATION_MANDATORY_ERREUR_ADRESSE_TIERS_BLANK"));

                setMessageErreurForBusinessUser(getSession().getLabel(
                        "NAOS_ANNONCE_IDE_CREATION_MUTATION_MANDATORY_ERREUR_ADRESSE_TIERS_BLANK"));
            }
            formeJuridique = CodeSystem.getLibelle(getSession(), affiliationRetrieved.getPersonnaliteJuridique());
            langueTiers = tiers.getLangueIso().toUpperCase();
            brancheEconomique = CodeSystem.getLibelle(getSession(), affiliationRetrieved.getBrancheEconomique());

            noga = AFIDEUtil.getCodeNogaFromCsCodeNoga(affiliationRetrieved.getCodeNoga(), transaction.getSession());

            if (!noga.isEmpty()) {
                if (!isCodeNogaKnown(noga)) {

                    if (CODE_NOGA_INCONNU.equals(noga)) {
                        erreurNoga = FWMessageFormat.format(
                                getSession().getApplication().getLabel("NAOS_CODE_NOGA_INCONNU",
                                        getSession().getIdLangueISO()), noga);
                    } else {
                        erreurNoga = FWMessageFormat.format(
                                getSession().getApplication().getLabel("NAOS_CODE_NOGA_INDEFINI",
                                        getSession().getIdLangueISO()), noga);
                    }
                }
            }
            activite = affiliationRetrieved.getActivite();
            if (tiers.getPersonnePhysique()) {
                naissance = tiers.getDateNaissance();
                prenomNom = tiers.getPrenomNom();
            }

        }

    }

    /***
     * Méthode qui permet de récupérer l'id d'un code noga
     * 
     * @param ideDataBean
     * @throws Exception
     */
    private boolean isCodeNogaKnown(String codeNoga) throws Exception {
        FWParametersSystemCodeManager param = new FWParametersSystemCodeManager();

        param.setSession(getSession());
        param.setForCodeUtilisateur(codeNoga);

        param.find(BManager.SIZE_NOLIMIT);

        if (param.size() == 0) {
            return false;
        } else {
            return true;
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
        return AFIDEUtil.getListEtatAnnonceIdeEnCours().contains(getIdeAnnonceEtat());
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

    public String getNaissance() {
        return naissance;
    }

    public String getActivite() {
        return activite;
    }

    public String getNoga() {
        return noga;
    }

    @Override
    public String getRaisonSociale() {
        if (JadeStringUtil.isBlankOrZero(prenomNom)) {
            return super.getRaisonSociale();
        }
        return prenomNom;
    }

    public String getErreurNoga() {
        return erreurNoga;
    }
    
    
}
