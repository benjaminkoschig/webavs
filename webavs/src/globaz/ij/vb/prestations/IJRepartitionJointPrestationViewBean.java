package globaz.ij.vb.prestations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJRepartitionJointPrestation;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.external.IntRole;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Hashtable;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJRepartitionJointPrestationViewBean extends IJRepartitionJointPrestation implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final Object[] METHODES_SEL_ADRESSE = new Object[] {
            new String[] { "idTiersAdressePaiementDepuisPyxis", "getIdTiers" },
            new String[] { "idDomaineAdressePaiement", "idApplication" },
            new String[] { "numAffilieEmployeur", "idExterneAvoirPaiement" } };
    private static final Object[] METHODES_SEL_BENEFICIAIRE = new Object[] {
            new String[] { "idTiersDepuisPyxis", "idTiers" }, new String[] { "nom", "nom" } };
    private static final Object[] METHODES_SEL_BENEFICIAIRE2 = new Object[] {
            new String[] { "idTiersDepuisPyxis", "idTiers" }, new String[] { "nom", "nom" } };

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String adresseFormattee = "";
    // infos relatives a l'adresse de paiement
    private String ccpOuBanqueFormatte = "";
    // infos repatives a la prestation
    private String dateDebutBaseIndemnisation = "";

    private String dateFinBaseIndemnisation = "";
    private String datePrononce = "";

    private String etatPrestation = "";
    private String idCompteAnnexe = "";
    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    private int idOfIdPrestationCourante;

    // positionnement au sein des prestations pour ce droit
    private List idsPrestations;
    // Setté à true lors de la recherche d'un affilié directement par sont no
    // (action : rechercerAffilie)
    private boolean isActionRechercherAffilie = false;
    private String isAdresseModifiee = "false";

    // Si setté à true, indique que l'on souhaite créer un nouveau bénéficiaire
    // de paiement (employeur)
    private boolean isCreationNouvelleRepartition = false;
    // infos relatives au prononce
    private String noAVSAssure = "";

    private String nomPrenomAssure = "";

    private String numAffilieEmployeur = "";

    // infos supplementaires
    private boolean retourDepuisPyxis;

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
     * getter pour l'attribut ccp ou banque formatte
     * 
     * @return la valeur courante de l'attribut ccp ou banque formatte
     */
    public String getCcpOuBanqueFormatte() {
        return ccpOuBanqueFormatte;
    }

    /**
     * getter pour l'attribut date debut base indemnisation
     * 
     * @return la valeur courante de l'attribut date debut base indemnisation
     */
    public String getDateDebutBaseIndemnisation() {
        return dateDebutBaseIndemnisation;
    }

    /**
     * getter pour l'attribut date fin base indemnisation
     * 
     * @return la valeur courante de l'attribut date fin base indemnisation
     */
    public String getDateFinBaseIndemnisation() {
        return dateFinBaseIndemnisation;
    }

    /**
     * getter pour l'attribut date prononce
     * 
     * @return la valeur courante de l'attribut date prononce
     */
    public String getDatePrononce() {
        return datePrononce;
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

    public String getEtatPrestation() {
        return etatPrestation;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdCompteAnnexe(BSession bSession, String nss) throws Exception {
        try {

            CACompteAnnexeManager caManager = new CACompteAnnexeManager();
            caManager.setSession(bSession);

            caManager.setForIdRole(IntRole.ROLE_IJAI);

            if (!JadeStringUtil.isBlankOrZero(getIdAffilie())) {
                AFAffiliation afAffiliation = new AFAffiliation();
                afAffiliation.setSession(bSession);
                afAffiliation.setAffiliationId(getIdAffilie());
                afAffiliation.retrieve();

                if (!afAffiliation.hasErrors()) {
                    caManager.setForIdExterneRole(afAffiliation.getAffilieNumero());
                } else {
                    setMsgType(FWMessage.ERREUR);
                    setMessage("Erreur dans IJRepartitionJointPresationViewBean->getIdCompteAnnexe() => afAffiliation.hasErrors()=true");
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
                setMessage("Erreur dans IJRepartitionJointPresationViewBean->getIdCompteAnnexe() => caManager.hasErrors()=true");
            }
        } catch (Exception e) {
            _addError(getSession().getCurrentThreadTransaction(), getSession().getLabel("ERROR_ID_COMPTE_ANNEXE"));
        }
        return idCompteAnnexe;
    }

    /**
     * getter pour l'attribut id of id prestation courante
     * 
     * @return la valeur courante de l'attribut id of id prestation courante
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

    /**
     * getter pour l'attribut methodes selection adresse
     * 
     * @return la valeur courante de l'attribut methodes selection adresse
     */
    public Object[] getMethodesSelectionAdresse() {
        return IJRepartitionJointPrestationViewBean.METHODES_SEL_ADRESSE;
    }

    /**
     * getter pour l'attribut methodes selection beneficiaire (pour les administrations)
     * 
     * @return la valeur courante de l'attribut methodes selection beneficiaire
     */
    public Object[] getMethodesSelectionBeneficaire2() {
        return IJRepartitionJointPrestationViewBean.METHODES_SEL_BENEFICIAIRE2;
    }

    /**
     * getter pour l'attribut methodes selection beneficiaire (pour les tiers)
     * 
     * @return la valeur courante de l'attribut methodes selection beneficiaire
     */
    public Object[] getMethodesSelectionBeneficiaire() {
        return IJRepartitionJointPrestationViewBean.METHODES_SEL_BENEFICIAIRE;
    }

    /**
     * getter pour l'attribut montant brut
     * 
     * @return la valeur courante de l'attribut montant brut
     */
    @Override
    public String getMontantBrut() {
        if (isVentilation()) {
            return "";
        } else {
            return JANumberFormatter.format(super.getMontantBrut());
        }
    }

    /**
     * calcul le montant des cotisations en soustrayant le montant brut de la prestation au montant net.
     * 
     * @return la valeur courante de l'attribut montant cotisations
     */
    public String getMontantCotisations() {
        if (isVentilation()) {
            return "";
        } else {
            return JANumberFormatter.formatNoQuote(JadeStringUtil.toDouble(super.getMontantNet())
                    - JadeStringUtil.toDouble(super.getMontantBrut()));
        }
    }

    /**
     * getter pour l'attribut montant net
     * 
     * @return la valeur courante de l'attribut montant net
     */
    @Override
    public String getMontantNet() {
        return JANumberFormatter.format(super.getMontantNet());
    }

    /**
     * getter pour l'attribut montant restant
     * 
     * @return la valeur courante de l'attribut montant restant
     */
    @Override
    public String getMontantRestant() {
        return JANumberFormatter.format(super.getMontantRestant());
    }

    /**
     * getter pour l'attribut montant ventile
     * 
     * @return la valeur courante de l'attribut montant ventile
     */
    @Override
    public String getMontantVentile() {
        if (isVentilation()) {
            return JANumberFormatter.format(super.getMontantVentile());
        } else {
            return "";
        }
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

        if (!JadeStringUtil.isBlankOrZero(getIdAffilie())) {
            // Traitement de l'idExterne, si c'est un idAffilie, on le change en
            // numAffilie
            IAFAffiliation affiliationIfc = (IAFAffiliation) getSession().getAPIFor(IAFAffiliation.class);
            Hashtable criteres = new Hashtable();

            criteres.put(IAFAffiliation.FIND_FOR_AFFILIATIONID, getIdAffilie());

            IAFAffiliation[] result = affiliationIfc.findAffiliation(criteres);

            // si existe, on change l'idExterne
            if (result.length > 0) {
                nomPlus = result[0].getAffilieNumero() + " - ";
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

    /**
     * getter pour l'attribut num affilie.
     * 
     * @return la valeur courante de l'attribut num affilie
     */
    public String getNumAffilieEmployeur() {
        if (JadeStringUtil.isEmpty(numAffilieEmployeur)) {
            try {
                IJSituationProfessionnelle sp = loadSituationProfessionnelle();
                if ((sp == null) || sp.isNew()) {
                    return "";
                }
                IPRAffilie affilie = sp.loadEmployeur().loadAffilie();

                if (affilie != null) {
                    numAffilieEmployeur = affilie.getNumAffilie();
                }
            } catch (Exception e) {
                _addError(getSession().getCurrentThreadTransaction(), getSession().getLabel("EMPLOYEUR_INTROUVABLE"));
            }
        }

        return numAffilieEmployeur;
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

    public boolean isActionRechercherAffilie() {
        return isActionRechercherAffilie;
    }

    /**
     * @return
     */
    public String isAdresseModifiee() {
        return isAdresseModifiee;
    }

    /**
     * @return
     */
    public boolean isCreationNouvelleRepartition() {
        return isCreationNouvelleRepartition;
    }

    /**
     * retourne vrai si la prestation courante est dans l'etat definitif.
     * 
     * @return vrai si la prestation courante est dans l'etat definitif
     */
    public boolean isDefinitif() {
        return IIJPrestation.CS_DEFINITIF.equals(etatPrestation);
    }

    /**
     * retourne vrai si la repartition de paiement peut etre modifiee.
     * 
     * @return la valeur courante de l'attribut modifiable
     */
    public boolean isModifiable() {

        // recherche si le repartition de paiement est liee a une prestation
        // dans l'etat definitif
        IJPrestation pres = new IJPrestation();
        boolean hasPrestationInStateDefinitif = true;
        pres.setSession(getSession());
        pres.setIdPrestation(getIdPrestation());

        try {
            pres.retrieve();
            if (pres.isNew()) {
                return true;
            }
            hasPrestationInStateDefinitif = IIJPrestation.CS_DEFINITIF.equalsIgnoreCase(pres.getCsEtat());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (!JadeStringUtil.isIntegerEmpty(getIdParent()) || !JadeStringUtil
                .isIntegerEmpty(getIdRepartitionPaiement())) && !hasPrestationInStateDefinitif;
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

    public void setActionRechercherAffilie(boolean isActionRechercherAffilie) {
        this.isActionRechercherAffilie = isActionRechercherAffilie;
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
        // vide du beneficiaire dans les prestations de la meme base
        // d'indémnisation (IJRepartitionJointPrestationHelper)
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
     * setter pour l'attribut ccp ou banque formatte
     * 
     * @param ccpOuBanqueFormatte
     *            une nouvelle valeur pour cet attribut
     */
    public void setCcpOuBanqueFormatte(String ccpOuBanqueFormatte) {
        this.ccpOuBanqueFormatte = ccpOuBanqueFormatte;
    }

    /**
     * setter pour l'attribut date debut base indemnisation
     * 
     * @param dateDebutBaseIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutBaseIndemnisation(String dateDebutBaseIndemnisation) {
        this.dateDebutBaseIndemnisation = dateDebutBaseIndemnisation;
    }

    /**
     * setter pour l'attribut date fin base indemnisation
     * 
     * @param dateFinBaseIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinBaseIndemnisation(String dateFinBaseIndemnisation) {
        this.dateFinBaseIndemnisation = dateFinBaseIndemnisation;
    }

    /**
     * setter pour l'attribut date prononce
     * 
     * @param datePrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setDatePrononce(String datePrononce) {
        this.datePrononce = datePrononce;
    }

    public void setEtatPrestation(String etatPrestation) {
        this.etatPrestation = etatPrestation;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * setter pour l'attribut id of id prestation courante
     * 
     * @param idOfIdPrestationCourante
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdOfIdPrestationCourante(int idOfIdPrestationCourante) {
        this.idOfIdPrestationCourante = idOfIdPrestationCourante;
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
     * setter pour l'attribut id tiers adresse paiement depuis pyxis
     * 
     * @param idAdressePaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersAdressePaiementDepuisPyxis(String idAdressePaiement) {
        super.setIdTiersAdressePaiement(idAdressePaiement);
        retourDepuisPyxis = true;
    }

    /**
     * setter pour l'attribut id tiers depuis pyxis
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersDepuisPyxis(String idTiers) {
        super.setIdTiers(idTiers);
        retourDepuisPyxis = true;
        tiersBeneficiaireChange = true;
    }

    /**
     * @param b
     */
    public void setIsCreationNouvelleRepartition(boolean b) {
        isCreationNouvelleRepartition = b;
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

    /**
     * MAJ du no affilié, après le retour de tiers. Met également à jour l'id affilié sur l'employeur est un affilié.
     * 
     * @param string
     */
    public void setNumAffilieEmployeur(String string) throws Exception {
        numAffilieEmployeur = string;
        IPRAffilie affilie = PRAffiliationHelper.getEmployeurParNumAffilie(getSession(), numAffilieEmployeur);
        if (affilie != null) {
            setIdAffilieAdrPmt(affilie.getIdAffilie());
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
     * @param b
     */
    public void setTiersBeneficiaireChange(boolean b) {
        tiersBeneficiaireChange = b;
    }

}
