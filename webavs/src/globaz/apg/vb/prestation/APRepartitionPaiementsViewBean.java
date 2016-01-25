package globaz.apg.vb.prestation;

import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.external.IntRole;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Hashtable;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @see globaz.apg.servlet.APRepartitionPaiementsAction
 * @author vre
 */
public class APRepartitionPaiementsViewBean extends APRepartitionPaiements implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_SEL_ADRESSE = new Object[] {
            new String[] { "idTiersAdressePaiementDepuisPyxis", "getIdTiers" },
            new String[] { "idDomaineAdressePaiement", "idApplication" },
            new String[] { "numAffilieDepuisPyxis", "idExterneAvoirPaiement" } };

    private static final Object[] METHODES_SEL_BENEFICIAIRE = new Object[] {
            new String[] { "idTiersDepuisPyxis", "idTiers" }, new String[] { "nom", "nom" } };

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String adresseFormattee = "";
    private BSession bSession = null;
    // infos relatives a l'adresse de paiement
    private String ccpOuBanqueFormatte = "";
    private String dateDebutDroit = "";
    private String dateDebutPrestation = "";
    private String dateFinPrestation = "";

    private String droitAcquis = "";
    private String etatPrestation = "";
    private String genrePrestation = "";
    private String genreService = "";
    private String idCompteAnnexe = "";
    // infos relatives au droit
    private String idDroit = "";

    private int idOfIdPrestationCourante;

    // positionnement au sein des prestations pour ce droit
    private List idsPrestations;
    private String isAdresseModifiee = "false";

    private String montantBrutPrestation = "";
    private String nbJoursPrestation = "";

    private String noAVSAssure = "";
    private String nomPrenomAssure = "";

    private String numAffilieDepuisPyxis = "";
    // champs supplementaires.
    private boolean retourDepuisPyxis;
    // infos relatives a la prestation
    private String tauxPrestation = "";
    private boolean tiersBeneficiaireChange = false;

    /**
     * getter pour l'attribut adresse formattee
     * 
     * @return la valeur courante de l'attribut adresse formattee
     */
    public String getAdresseFormattee() {
        return adresseFormattee;
    }

    /**
     * getter pour l'attribut ccp formatte
     * 
     * @return la valeur courante de l'attribut ccp formatte
     */
    public String getCcpOuBanqueFormatte() {
        return ccpOuBanqueFormatte;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date debut droit
     * 
     * @return la valeur courante de l'attribut date debut droit
     */
    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    /**
     * getter pour l'attribut date debut prestation
     * 
     * @return la valeur courante de l'attribut date debut prestation
     */
    public String getDateDebutPrestation() {
        return dateDebutPrestation;
    }

    /**
     * getter pour l'attribut date fin prestation
     * 
     * @return la valeur courante de l'attribut date fin prestation
     */
    public String getDateFinPrestation() {
        return dateFinPrestation;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVSAssure());

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession()
                    .getCode(
                            getSession().getSystemCode("CIPAYORI",
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode("CIPAYORI",
                                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            return PRNSSUtil.formatDetailRequerantDetail(
                    getNoAVSAssure(),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut droit acquis
     * 
     * @return la valeur courante de l'attribut droit acquis
     */
    public String getDroitAcquis() {
        return droitAcquis;
    }

    /**
     * getter pour l'attribut etat prestation
     * 
     * @return la valeur courante de l'attribut etat prestation
     */
    public String getEtatPrestation() {
        return etatPrestation;
    }

    /**
     * @return
     */
    public String getGenrePrestation() {
        return genrePrestation;
    }

    /**
     * getter pour l'attribut genre indemnite
     * 
     * @return la valeur courante de l'attribut genre indemnite
     */
    public String getGenreService() {
        return genreService;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdCompteAnnexe(BSession bSession, String nss) throws Exception {
        try {
            CACompteAnnexeManager caManager = new CACompteAnnexeManager();
            caManager.setSession(bSession);

            caManager.setForIdRole(IntRole.ROLE_APG);

            if (!JadeStringUtil.isBlankOrZero(getIdAffilie())) {

                AFAffiliation afAffiliation = new AFAffiliation();
                afAffiliation.setSession(bSession);
                afAffiliation.setAffiliationId(getIdAffilie());
                afAffiliation.retrieve();

                if (!afAffiliation.hasErrors()) {
                    caManager.setForIdExterneRole(afAffiliation.getAffilieNumero());
                } else {
                    setMsgType(FWMessage.ERREUR);
                    setMessage("Erreur dans APRepartitionPaiementsViewBean->getIdCompteAnnexe() => afAffiliation.hasErrors()=true");
                }
            } else {
                caManager.setForIdExterneRole(nss);
            }

            caManager.find();

            if (!caManager.hasErrors()) {
                if (caManager.size() != 0) {
                    CACompteAnnexe compte = new CACompteAnnexe();
                    compte = (CACompteAnnexe) caManager.getFirstEntity();
                    idCompteAnnexe = compte.getIdCompteAnnexe();
                }
            } else {
                setMsgType(FWMessage.ERREUR);
                setMessage("Erreur dans APRepartitionPaiementsViewBean->getIdCompteAnnexe() => caManager.hasErrors()=true");
            }

        } catch (Exception e) {
            _addError(getSession().getCurrentThreadTransaction(), getSession().getLabel("ERROR_ID_COMPTE_ANNEXE"));
        }
        return idCompteAnnexe;
    }

    /**
     * getter pour l'attribut id droit
     * 
     * @return la valeur courante de l'attribut id droit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * getter pour l'attribut id prestation courante
     * 
     * @return la valeur courante de l'attribut id prestation courante
     */
    public int getIdOfIdPrestationCourante() {
        return idOfIdPrestationCourante;
    }

    /**
     * getter pour l'attribut id prestation courante
     * 
     * @return la valeur courante de l'attribut id prestation courante
     */
    public String getIdPrestationCourante() {
        return (String) idsPrestations.get(idOfIdPrestationCourante);
    }

    /**
     * getter pour l'attribut ids prestations
     * 
     * @return la valeur courante de l'attribut ids prestations
     */
    public List getIdsPrestations() {
        return idsPrestations;
    }

    public String getLibelleGenrePrestation() {
        return getSession().getCodeLibelle(getGenrePrestation());
    }

    /**
     * retourne un tableau de correspondance entre methodes client et provider pour le retour depuis pyxis avec le
     * bouton de selection d'une adresse de paiement.
     * 
     * @return la valeur courante de l'attribut methodes selection adresse
     */
    public Object[] getMethodesSelectionAdresse() {
        return APRepartitionPaiementsViewBean.METHODES_SEL_ADRESSE;
    }

    /**
     * retourne un tableau de correspondance entre methodes client et provider pour le retour depuis pyxis avec le
     * bouton de selection d'un beneficiaire de paiement.
     * 
     * @return la valeur courante de l'attribut methodes selection beneficiaire
     */
    public Object[] getMethodesSelectionBeneficiaire() {
        return APRepartitionPaiementsViewBean.METHODES_SEL_BENEFICIAIRE;
    }

    /**
     * getter pour l'attribut montant brut
     * 
     * @return la valeur courante de l'attribut montant brut
     */
    @Override
    public String getMontantBrut() {
        return JANumberFormatter.fmt(super.getMontantBrut(), true, true, true, 2);
    }

    /**
     * getter pour l'attribut montant brut prestation
     * 
     * @return la valeur courante de l'attribut montant brut prestation
     */
    public String getMontantBrutPrestation() {
        return JANumberFormatter.fmt(montantBrutPrestation, true, true, true, 2);
    }

    /**
     * getter pour l'attribut montant cotisations
     * 
     * @return la valeur courante de l'attribut montant cotisations
     */
    public String getMontantCotisations() {
        if (JadeStringUtil.isIntegerEmpty(getIdParent())) {
            return JANumberFormatter.format(
                    Double.parseDouble(super.getMontantNet()) - Double.parseDouble(super.getMontantBrut()), 0.05, 2,
                    JANumberFormatter.NEAR);
        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut montant net
     * 
     * @return la valeur courante de l'attribut montant net
     */
    @Override
    public String getMontantNet() {
        return JANumberFormatter.fmt(super.getMontantNet(), true, true, true, 2);
    }

    /**
     * getter pour l'attribut montant restant
     * 
     * @return la valeur courante de l'attribut montant restant
     */
    @Override
    public String getMontantRestant() {
        return JANumberFormatter.fmt(super.getMontantRestant(), true, true, true, 2);
    }

    public String getMontantTotalAllocExploitation() throws Exception {
        APPrestationJointLotTiersDroitViewBean viewBean = new APPrestationJointLotTiersDroitViewBean();
        viewBean.setSession(getSession());
        viewBean.setIdPrestationApg(getIdPrestationCourante());
        viewBean.retrieve();
        return viewBean.getMontantTotalAllocExploitation();
    }

    /**
     * getter pour l'attribut montant ventile
     * 
     * @return la valeur courante de l'attribut montant ventile
     */
    @Override
    public String getMontantVentile() {
        return JANumberFormatter.fmt(super.getMontantVentile(), true, true, true, 2);
    }

    /**
     * getter pour l'attribut nb jours prestation
     * 
     * @return la valeur courante de l'attribut nb jours prestation
     */
    public String getNbJoursPrestation() {
        return nbJoursPrestation;
    }

    /**
     * getter pour l'attribut no AVSAssure
     * 
     * @return la valeur courante de l'attribut no AVSAssure
     */
    public String getNoAVSAssure() {
        return noAVSAssure;
    }

    public String getNomPlus() throws Exception {

        String nomPlus = "";

        if (!JadeStringUtil.isBlankOrZero(idAffilie)) {
            // Traitement de l'idExterne, si c'est un idAffilie, on le change en
            // numAffilie
            IAFAffiliation affiliationIfc = (IAFAffiliation) getSession().getAPIFor(IAFAffiliation.class);
            Hashtable criteres = new Hashtable();

            criteres.put(IAFAffiliation.FIND_FOR_AFFILIATIONID, idAffilie);

            IAFAffiliation[] result = affiliationIfc.findAffiliation(criteres);

            // si existe, on change l'idExterne
            if (result.length > 0) {
                nomPlus = result[0].getAffilieNumero() + " - ";
            }
        }

        nomPlus += getNom();

        return nomPlus;

    }

    public String getNomPlusTypeAffiliation() throws Exception {

        String nomPlus = "";

        if (!JadeStringUtil.isBlankOrZero(getIdAffilie())) {
            // Traitement de l'idExterne, si c'est un idAffilie, on le change en
            // numAffilie
            IAFAffiliation affiliationIfc = (IAFAffiliation) getSession().getAPIFor(IAFAffiliation.class);
            Hashtable criteres = new Hashtable();

            criteres.put(IAFAffiliation.FIND_FOR_AFFILIATIONID, getIdAffilie());

            IAFAffiliation[] result = affiliationIfc.findAffiliation(criteres);

            // InfoRom531
            String tAff = "";
            // si existe, on change l'idExterne
            if (result.length > 0) {
                nomPlus = result[0].getAffilieNumero() + " ";
                try {
                    nomPlus += getSession().getCodeLibelle(result[0].getTypeAffiliation()) + " - ";
                } catch (Exception e) {
                    nomPlus = result[0].getAffilieNumero() + " ";
                }
            }
        }

        nomPlus += getNom();

        return nomPlus;

    }

    /**
     * @return
     */
    public String getNomPrenomAssure() {
        return nomPrenomAssure;
    }

    public String getNumAffilieDepuisPyxis() {
        return numAffilieDepuisPyxis;
    }

    /**
     * getter pour l'attribut taux prestation
     * 
     * @return la valeur courante de l'attribut taux prestation
     */
    public String getTauxPrestation() {
        return tauxPrestation;
    }

    /**
     * retourne vrai s'il y a plus d'une prestation pour ce droit et que la prestation courante n'est pas la premiere.
     * 
     * @return DOCUMENT ME!
     */
    public boolean hasPrestationPrecedante() {
        return idOfIdPrestationCourante > 0;
    }

    /**
     * retourne vrai s'il y a plus d'une prestation pour ce droit et que la prestation courante n'est pas la derniere.
     * 
     * @return DOCUMENT ME!
     */
    public boolean hasPrestationSuivante() {
        return idOfIdPrestationCourante < (idsPrestations.size() - 1);
    }

    /**
     * @return
     */
    public String isAdresseModifiee() {
        return isAdresseModifiee;
    }

    /**
     * retourne vrai si la prestation courante est dans l'etat definitif.
     * 
     * @return vrai si la prestation courante est dans l'etat definitif
     */
    public boolean isDefinitif() {
        return IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(etatPrestation);
    }

    /**
     * retourne vrai si la repartition de paiement peut etre modifiee.
     * 
     * @return la valeur courante de l'attribut modifiable
     */
    public boolean isModifiable() {

        // recherche si le repartition de paiement est liee a une prestation
        // dans l'etat definitif
        APPrestation pres = new APPrestation();
        boolean hasPrestationInStateDefinitif = true;
        if (bSession == null) {
            bSession = getSession();
        }
        pres.setSession(bSession);
        pres.setIdPrestationApg(getIdPrestationApg());

        try {
            pres.retrieve();
            hasPrestationInStateDefinitif = pres.getEtat().equalsIgnoreCase(IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return (!JadeStringUtil.isIntegerEmpty(getIdParent()) || !JadeStringUtil
                .isIntegerEmpty(getIdRepartitionBeneficiairePaiement())) && !hasPrestationInStateDefinitif;
    }

    /**
     * getter pour l'attribut restitution.
     * 
     * @return la valeur courante de l'attribut restitution
     */
    public boolean isRestitution() {

        String idPrest = getIdPrestationApg();
        if (JadeStringUtil.isIntegerEmpty(idPrest) && (idsPrestations != null)) {
            idPrest = getIdPrestationCourante();
        }

        APPrestation pre = new APPrestation();
        pre.setSession(getSession());
        pre.setIdPrestationApg(idPrest);

        try {
            pre.retrieve();
        } catch (Exception e) {

            return false;
        }

        return pre.isRestitution();
    }

    /**
     * getter pour l'attribut retour depuis pyxis
     * 
     * @return la valeur courante de l'attribut retour depuis pyxis
     */
    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    /**
     * @return
     */
    public boolean isTiersBeneficiaireChange() {
        return tiersBeneficiaireChange;
    }

    /**
     * retourne vrai si la prestation courante est dans l'etat valide.
     * 
     * @return la valeur courante de l'attribut validee
     */
    public boolean isValidee() {
        return IAPPrestation.CS_ETAT_PRESTATION_VALIDE.equals(etatPrestation);
    }

    /**
     * retourne vrai si la repartition courante est une ventilation du montant d'une repartition.
     * 
     * @return la valeur courante de l'attribut ventilation
     */
    public boolean isVentilation() {
        return !JadeStringUtil.isIntegerEmpty(getIdParent());
    }

    /** passe a la prestation precedante pour ce droit s'il en existe une */
    public void prestationPrecedante() {
        --idOfIdPrestationCourante;
    }

    /** passe a la prestation suivante pour ce droit s'il en existe une */
    public void prestationSuivante() {
        ++idOfIdPrestationCourante;
    }

    /**
     * setter pour l'attribut adresse formattee
     * 
     * @param adresseFormattee
     *            une nouvelle valeur pour cet attribut
     */
    public void setAdresseFormattee(String adresseFormattee) {

        // indique si adresse de paiement modifiee pour effectuer la mise a
        // jours des adresses de paiement
        // vide du beneficiaire dans les prestations du meme droit
        // (IJRepartitionJointPrestationHelper)
        if (isRetourDepuisPyxis() && !this.adresseFormattee.equalsIgnoreCase(adresseFormattee)) {
            setAdresseModifiee("true");
        } else {
            setAdresseModifiee("false");
        }

        this.adresseFormattee = adresseFormattee;
    }

    /**
     * @param b
     */
    public void setAdresseModifiee(String b) {
        isAdresseModifiee = b;
    }

    /**
     * setter pour l'attribut ccp formatte
     * 
     * @param ccpFormatte
     *            une nouvelle valeur pour cet attribut
     */
    public void setCcpOuBanqueFormatte(String ccpFormatte) {
        ccpOuBanqueFormatte = ccpFormatte;
    }

    /**
     * setter pour l'attribut date debut droit
     * 
     * @param dateDebutDroit
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    /**
     * setter pour l'attribut date debut prestation
     * 
     * @param dateDebutPrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutPrestation(String dateDebutPrestation) {
        this.dateDebutPrestation = dateDebutPrestation;
    }

    /**
     * setter pour l'attribut date fin prestation
     * 
     * @param dateFinPrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinPrestation(String dateFinPrestation) {
        this.dateFinPrestation = dateFinPrestation;
    }

    /**
     * setter pour l'attribut droit acquis
     * 
     * @param droitAcquis
     *            une nouvelle valeur pour cet attribut
     */
    public void setDroitAcquis(String droitAcquis) {
        this.droitAcquis = droitAcquis;
    }

    /**
     * setter pour l'attribut etat prestation
     * 
     * @param etatPrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setEtatPrestation(String etatPrestation) {
        this.etatPrestation = etatPrestation;
    }

    /**
     * @param string
     */
    public void setGenrePrestation(String string) {
        genrePrestation = string;
    }

    /**
     * setter pour l'attribut genre indemnite
     * 
     * @param genreIndemnite
     *            une nouvelle valeur pour cet attribut
     */
    public void setGenreService(String genreIndemnite) {
        genreService = genreIndemnite;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * setter pour l'attribut id droit
     * 
     * @param idDroit
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    /**
     * setter pour l'attribut id prestation courante
     * 
     * @param idOfIdPrestationCourante
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdOfIdPrestationCourante(int idOfIdPrestationCourante) {
        this.idOfIdPrestationCourante = idOfIdPrestationCourante;
    }

    /**
     * setter pour l'attribut id prestation courante
     * 
     * @param idOfIdPrestationCourante
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdOfIdPrestationCourante(String idOfIdPrestationCourante) {
        this.idOfIdPrestationCourante = JadeStringUtil.parseInt(idOfIdPrestationCourante, 0);
    }

    /**
     * force l'affichage des repartitions pour une prestation choisie dans l'ecran de liste des prestations pour un
     * droit.
     * 
     * @param idPrestationCourante
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrestationCourante(String idPrestationCourante) {
        idPrestationApg = idPrestationCourante;
    }

    /**
     * setter pour l'attribut ids prestations
     * 
     * @param idsPrestations
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdsPrestations(List idsPrestations) {
        this.idsPrestations = idsPrestations;
    }

    /**
     * setter pour l'attribut id adresse paiement depuis pyxis.
     * 
     * <p>
     * cette methode initialise un flag interne qui indique qu'elle a ete appellee lors d'un retour depuis pyxis.
     * </p>
     * 
     * @param idTiersAdressePaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersAdressePaiementDepuisPyxis(String idTiersAdressePaiement) {
        super.setIdTiersAdressePaiement(idTiersAdressePaiement);
        retourDepuisPyxis = true;
    }

    /**
     * setter pour l'attribut id tiers depuis pyxis
     * 
     * <p>
     * Cette methode initialise un flag interne qui indique que cette methode a ete appellee lors d'un retour depuis
     * pyxis.
     * </p>
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersDepuisPyxis(String idTiers) {
        super.setIdTiers(idTiers);
        retourDepuisPyxis = true;
        tiersBeneficiaireChange = true;

        // on efface les informations qui ne sont plus pertinentes
        setIdTiersAdressePaiement("");
        setIdDomaineAdressePaiement("");
    }

    /**
     * setter pour l'attribut montant brut
     * 
     * @param montantBrut
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setMontantBrut(String montantBrut) {
        super.setMontantBrut(JANumberFormatter.deQuote(montantBrut));
    }

    /**
     * setter pour l'attribut montant brut prestation
     * 
     * @param montantBrutPrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantBrutPrestation(String montantBrutPrestation) {
        this.montantBrutPrestation = montantBrutPrestation;
    }

    /**
     * setter pour l'attribut montant net
     * 
     * @param montantNet
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setMontantNet(String montantNet) {
        super.setMontantNet(JANumberFormatter.deQuote(montantNet));
    }

    /**
     * setter pour l'attribut montant ventile
     * 
     * @param montantVentile
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setMontantVentile(String montantVentile) {
        super.setMontantVentile(JANumberFormatter.deQuote(montantVentile));
    }

    /**
     * setter pour l'attribut nb jours prestation
     * 
     * @param nbJoursPrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbJoursPrestation(String nbJoursPrestation) {
        this.nbJoursPrestation = nbJoursPrestation;
    }

    /**
     * setter pour l'attribut no AVSAssure
     * 
     * @param noAVSAssure
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAVSAssure(String noAVSAssure) {
        this.noAVSAssure = noAVSAssure;
    }

    /**
     * @param string
     */
    public void setNomPrenomAssure(String string) {
        nomPrenomAssure = string;
    }

    public void setNumAffilieDepuisPyxis(String numAffilieDepuisPyxis) throws Exception {
        this.numAffilieDepuisPyxis = numAffilieDepuisPyxis;

        if (!JadeStringUtil.isBlankOrZero(numAffilieDepuisPyxis)) {
            // setter l'idAffilie dans le BEntity
            IAFAffiliation affiliationIfc = (IAFAffiliation) getSession().getAPIFor(IAFAffiliation.class);
            Hashtable criteres = new Hashtable();
            criteres.put(IAFAffiliation.FIND_FOR_NOAFFILIE, numAffilieDepuisPyxis);
            IAFAffiliation[] result = affiliationIfc.findAffiliation(criteres);
            setIdAffilieAdrPmt(result[0].getAffiliationId());
        } else {
            setIdAffilieAdrPmt("");
        }

    }

    /**
     * setter pour l'attribut retour depuis pyxis
     * 
     * @param retourDepuisPyxis
     *            une nouvelle valeur pour cet attribut
     */
    public void setRetourDepuisPyxis(boolean retourDepuisPyxis) {
        this.retourDepuisPyxis = retourDepuisPyxis;
    }

    /**
     * setter pour l'attribut taux prestation
     * 
     * @param tauxPrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setTauxPrestation(String tauxPrestation) {
        this.tauxPrestation = tauxPrestation;
    }

    /**
     * @param b
     */
    public void setTiersBeneficiaireChange(boolean b) {
        tiersBeneficiaireChange = b;
    }

}
