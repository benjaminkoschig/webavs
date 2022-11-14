package globaz.corvus.vb.creances;

import globaz.corvus.api.creances.IRECreancier;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculManager;
import globaz.corvus.db.creances.RECreanceAccordee;
import globaz.corvus.db.creances.RECreanceAccordeeManager;
import globaz.corvus.db.creances.RECreancier;
import globaz.corvus.db.creances.RECreancierManager;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRImagesConstants;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;

import java.math.BigDecimal;
import java.util.*;

/**
 * ViewBean pour les filtres de recherche de l'écran des créanciers
 */
public class RECreancierViewBean extends RECreancier implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final Object[] METHODES_SEL_ADRESSE_PAIEMENT = new Object[] {
            new String[] { "idTiersAdressePmtDepuisPyxis", "getIdTiers" },
            new String[] { "idDomaineApplicatifDepuisPyxis", "idApplication" },
            new String[] { "numAffilieDepuisPyxis", "idExterneAvoirPaiement" } };
    private static final Object[] METHODES_SEL_BENEFICIAIRE = new Object[] { new String[] { "idTiersDepuisPyxis",
            "idTiers" } };

    private static final Object[] METHODES_SEL_REFERENCE_PAIEMENT_QR = new Object[]{
            new String[]{"setIdReferenceQRDepuisPyxis", "getIdReferenceQR"}};

    private static final Object[] PARAMS_CHERCHER_REFERENCE_PAIEMENT = new Object[]{
            new String[]{"forIdTiers", "forIdTiers"}, new String[]{"forIdAdressePaiement", "forIdAdressePaiement"}, new String[]{"forCompteLike", "forCompteLike"}};

    private String referenceQRFormattee = "";
    private String ccpOuBanqueFormatte = "";
    private REDecisionEntity decision = null;
    private String idLot;
    private String isAdresseModifiee = "false";
    private BigDecimal montantPrestationsDejaVersees = BigDecimal.ZERO;
    private BigDecimal montantRetroactif = BigDecimal.ZERO;
    private String numAffilieDepuisPyxis = "";
    private boolean retourDepuisPyxis;
    private PRTiersWrapper tiers = null;
    private boolean tiersBeneficiaireChange = false;
    private TIAdressePaiementData adressePaiementData = null;

    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (IRECreancier.CS_IMPOT_SOURCE.equals(getCsType())) {
            if (JadeStringUtil.isBlank(JANumberFormatter.deQuote(getRevenuAnnuelDeterminant()))) {
                _addError(statement.getTransaction(), getSession().getLabel("JSP_CRE_D_ERREUR_CREANCIER_TAUX_IMPOSITION_REVENU_ANNUEL_DETERMINANT"));
            }
        }
        if (!IRECreancier.CS_IMPOT_SOURCE.equals(getCsType())) {
            if (JadeStringUtil.isEmpty(getTiersNomPrenom())) {
                _addError(statement.getTransaction(), getSession().getLabel("JSP_CRE_D_ERREUR_CREANCIER"));
            }

            // Contrôle la présence d'une référence QR si le numéro de compte de l'adresse de paiement est QR-IBAN
            if (JadeStringUtil.isBlankOrZero(this.getIdReferenceQR()) && Objects.nonNull(getAdressePaiementData()) && TIAdressePaiement.isQRIban(this.getAdressePaiementData().getCompte())) {
                _addError(statement.getTransaction(), getSession().getLabel("JSP_REFERENCE_QR_EMPTY"));
            }
        }

    }

    /**
     * Retrouve la liste des assures (beneficiaires rentes accordees)
     * 
     * @return
     */
    public String[] getAssuresList() {

        ArrayList<String> tiersBenefs;
        String[] result = {};
        try {
            REDemandeRente dr = new REDemandeRente();
            dr.setSession(getSession());
            dr.setIdDemandeRente(getIdDemandeRente());
            dr.retrieve();

            Set<String> idTiersBenefTiersDesc = new HashSet<String>();
            tiersBenefs = new ArrayList<String>();
            tiersBenefs.add("");
            tiersBenefs.add("");

            if ((dr != null) && !JadeStringUtil.isIntegerEmpty(dr.getIdRenteCalculee())) {

                // on cherche les bases de calcul pour la demande de rents
                REBasesCalculManager bcManager = new REBasesCalculManager();
                bcManager.setSession(getSession());
                bcManager.setForIdRenteCalculee(dr.getIdRenteCalculee());
                bcManager.find();

                for (int i = 0; i < bcManager.size(); i++) {

                    REBasesCalcul bc = (REBasesCalcul) bcManager.getEntity(i);
                    // pour toutes les rentes accordees de la base de calcul
                    RERenteAccordeeManager raManager = new RERenteAccordeeManager();
                    raManager.setSession(getSession());
                    raManager.setForIdBaseCalcul(bc.getIdBasesCalcul());
                    raManager.find();

                    for (int j = 0; j < raManager.size(); j++) {

                        RERenteAccordee ra = (RERenteAccordee) raManager.getEntity(j);
                        String idTiersBenef = ra.getIdTiersBeneficiaire();
                        // on memorise le tiers beneficiaire
                        if (!idTiersBenefTiersDesc.contains(idTiersBenef)) {
                            PRTiersWrapper tiersBenef;
                            try {
                                tiersBenef = PRTiersHelper.getTiersParId(getSession(), idTiersBenef);

                                idTiersBenefTiersDesc.add(idTiersBenef);
                                tiersBenefs.add(idTiersBenef);
                                tiersBenefs.add(tiersBenef.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                        + tiersBenef.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            return result;
        }

        return tiersBenefs.toArray(result);
    }

    public String getCcpOuBanqueFormatte() {
        return ccpOuBanqueFormatte;
    }

    /**
     * Donne l'etat de la decision
     * 
     * @return
     */
    public String getCsEtatDecision(String idDecision) {

        loadDecision(idDecision);

        if (decision != null) {
            return decision.getCsEtat();
        }
        return "";
    }

    public String getCsTypeLibelle() {
        return getSession().getCodeLibelle(getCsType());
    }

    /**
     * Méthode qui contrôle si la préparation de la décision peut s'effectuer
     * 
     * @return true or false
     */
    public String getDateFinDroit() {

        try {
            REDemandeRente dem = new REDemandeRente();
            dem.setSession(getSession());
            dem.setIdDemandeRente(getIdDemandeRente());
            dem.retrieve();
            PRAssert.notIsNew(dem, "Entity not found");

            return dem.getDateFin();

        } catch (Exception e) {
            return "";
        }
    }

    public String getDetailRequerant() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), getIdTiersAdressePmt());

        if (tiers != null) {
            return PRNSSUtil.formatDetailRequerantDetail(
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)),
                    getLibellePays(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
        } else {
            return "";
        }

    }

    public String getIdLot() {
        return idLot;
    }

    /**
     * Méthode pour affichages des ordres de versement
     */
    public String getIdPrestation(String idDecision) {
        REPrestationsManager prestMgr = new REPrestationsManager();
        prestMgr.setSession(getSession());
        prestMgr.setForIdDecision(idDecision);
        prestMgr.setForIdDemandeRente(getIdDemandeRente());

        try {
            prestMgr.find();
        } catch (Exception e) {
            return "";
        }

        REPrestations prest = (REPrestations) prestMgr.getFirstEntity();

        return prest.getIdPrestation();
    }

    public String getIsAdresseModifiee() {
        return isAdresseModifiee;
    }

    /**
     * getter pour l'image isBloque
     * 
     * @return l'image correspondant a isBloque
     */
    public String getIsBloqueImage() {
        if (getIsBloque().booleanValue()) {
            return PRImagesConstants.IMAGE_OK;
        } else {
            return PRImagesConstants.IMAGE_ERREUR;
        }
    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays(String idPays) {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", idPays)))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", idPays));
        }

    }

    /**
     * retourne un tableau de correspondance entre methodes client et provider pour le retour depuis pyxis avec le
     * bouton de selection d'une adresse de paiement.
     * 
     * @return la valeur courante de l'attribut methodes selection adresse
     */
    public Object[] getMethodesSelectionAdressePaiement() {
        return RECreancierViewBean.METHODES_SEL_ADRESSE_PAIEMENT;
    }

    /**
     * getter pour l'attribut methodes selection beneficiaire (pour les tiers)
     * 
     * @return la valeur courante de l'attribut methodes selection beneficiaire
     */
    public Object[] getMethodesSelectionBeneficiaire() {
        return RECreancierViewBean.METHODES_SEL_BENEFICIAIRE;
    }

    public Object[] getMethodesSelectionReferencePaiement() {
        return RECreancierViewBean.METHODES_SEL_REFERENCE_PAIEMENT_QR;
    }

    public Object[] getParamsChercherReferencePaiement() {
        return RECreancierViewBean.PARAMS_CHERCHER_REFERENCE_PAIEMENT;
    }

    public String getMontantEnFaveurAssure() {
        return montantRetroactif.subtract(montantPrestationsDejaVersees).toString();
    }

    /**
     * Méthode pour affichages des ordres de versement
     */
    public String getMontantPrestation(String idDecision) {
        REPrestationsManager prestMgr = new REPrestationsManager();
        prestMgr.setSession(getSession());
        prestMgr.setForIdDecision(idDecision);
        prestMgr.setForIdDemandeRente(getIdDemandeRente());

        try {
            prestMgr.find();
        } catch (Exception e) {
            return "";
        }

        REPrestations prest = (REPrestations) prestMgr.getFirstEntity();

        return prest.getMontantPrestation();
    }

    public String getMontantPrestationsDejaVersees() {
        return montantPrestationsDejaVersees.toString();
    }

    public String getMontantReparti() {

        RECreanceAccordeeManager caManager = new RECreanceAccordeeManager();
        caManager.setSession(getSession());
        caManager.setForIdCreancier(getIdCreancier());
        try {
            BigDecimal montantReparti = caManager.getSum(RECreanceAccordee.FIELDNAME_MONTANT);
            return montantReparti.toString();

        } catch (Exception e) {
            return "0.00";
        }
    }

    public String getMontantRetroactif() {
        return montantRetroactif.toString();
    }

    public String getNumAffilieDepuisPyxis() {
        return numAffilieDepuisPyxis;
    }

    public String getTiersNomPrenom() throws Exception {
        if (loadTiers()) {
            ITITiers tiersNom = (ITITiers) getSession().getAPIFor(ITITiers.class);
            Hashtable<String, String> params = new Hashtable<String, String>();
            params.put(ITITiers.FIND_FOR_IDTIERS, tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            ITITiers[] t = tiersNom.findTiers(params);
            if ((t != null) && (t.length > 0)) {
                tiersNom = t[0];
            }

            String nom = "";

            if (!JadeStringUtil.isEmpty(tiersNom.getDesignation1())) {
                nom = tiersNom.getDesignation1();
            }
            if (!JadeStringUtil.isEmpty(tiersNom.getDesignation2())) {
                if (!JadeStringUtil.isEmpty(nom)) {
                    nom = nom + " " + tiersNom.getDesignation2();
                } else {
                    nom = tiersNom.getDesignation2();
                }
            }
            if (!JadeStringUtil.isEmpty(tiersNom.getDesignation3())) {
                if (!JadeStringUtil.isEmpty(nom)) {
                    nom = nom + " " + tiersNom.getDesignation3();
                } else {
                    nom = tiersNom.getDesignation3();
                }
            }
            if (!JadeStringUtil.isEmpty(tiersNom.getDesignation4())) {
                if (!JadeStringUtil.isEmpty(nom)) {
                    nom = nom + " " + tiersNom.getDesignation4();
                } else {
                    nom = tiersNom.getDesignation4();
                }
            }

            return nom;

        }

        return "";
    }

    /**
     * Vrais si il y a au moins un creancier pour la demande de rente
     * 
     * @return
     */
    public boolean hasCreancier(String noDemandeRente) {

        RECreancierManager cManager = new RECreancierManager();
        cManager.setSession(getSession());
        cManager.setForIdDemandeRente(noDemandeRente);

        try {
            return cManager.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Vrais si il y a au moins une rente accordee pour la demande de rente
     * 
     * @return
     */
    public boolean hasRenteAccordee(String noDemandeRente) {

        RERenteAccJoinTblTiersJoinDemRenteManager rajdrManager = new RERenteAccJoinTblTiersJoinDemRenteManager();
        rajdrManager.setSession(getSession());
        rajdrManager.setForNoDemandeRente(noDemandeRente);

        try {
            return rajdrManager.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isAfficherBTImprimer() throws Exception {
        RECreancierManager creMgr = new RECreancierManager();
        creMgr.setSession(getSession());
        creMgr.setForIdDemandeRente(getIdDemandeRente());
        creMgr.find();
        if (creMgr.size() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isBloque() {
        if (getIsBloque().booleanValue()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isModifiable() {

        REDemandeRente dr = new REDemandeRente();
        dr.setSession(getSession());
        dr.setIdDemandeRente(getIdDemandeRente());
        try {
            dr.retrieve();
        } catch (Exception e) {
            return false;
        }

        if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(dr.getCsEtat())
                || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(dr.getCsEtat())
                || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(dr.getCsEtat())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Méthode qui contrôle si la préparation de la décision peut s'effectuer
     * 
     * @return true or false
     */
    public boolean isPreparationDecisionValide() {

        try {

            // Recherche des dates dt,dj,dpmt,ddeb
            REDemandeRente dem = new REDemandeRente();
            dem.setSession(getSession());
            dem.setIdDemandeRente(getIdDemandeRente());
            dem.retrieve();
            PRAssert.notIsNew(dem, "Entity not found");

            JACalendar cal = new JACalendarGregorian();
            JADate datePmtMensuel = null;

            if (!JadeStringUtil.isBlankOrZero(REPmtMensuel.getDateDernierPmt(getSession()))) {
                datePmtMensuel = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(REPmtMensuel
                        .getDateDernierPmt(getSession())));
            }

            JADate dateDebutDroit = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dem.getDateDebut()));
            JADate dateTraitement = new JADate(
                    PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dem.getDateTraitement()));
            JADate dateJour = JACalendar.today();
            dateJour.setDay(1);

            // Si (dt=dj et dt=dpmt) ou (dt<dj et dt<dpmt et ddeb > dpmt) la
            // préparation de la décision peut s'effectuer
            if (datePmtMensuel != null) {
                return (((cal.compare(dateTraitement, dateJour) == JACalendar.COMPARE_FIRSTLOWER)
                        && (cal.compare(dateTraitement, datePmtMensuel) == JACalendar.COMPARE_FIRSTLOWER) && (cal
                            .compare(dateDebutDroit, datePmtMensuel) == JACalendar.COMPARE_FIRSTUPPER)) ||

                ((cal.compare(dateTraitement, dateJour) == JACalendar.COMPARE_EQUALS) && (cal.compare(dateTraitement,
                        datePmtMensuel) == JACalendar.COMPARE_EQUALS)));
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    public boolean isTiersBeneficiaireChange() {
        return tiersBeneficiaireChange;
    }

    private void loadDecision(String idDecision) {
        try {
            REDecisionEntity d = new REDecisionEntity();
            d.setSession(getSession());
            d.setIdDecision(idDecision);
            d.retrieve();

            decision = d;
        } catch (Exception e) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
        }
    }

    private boolean loadTiers() {
        if (tiers == null) {

            try {
                tiers = PRTiersHelper.getTiersParId(getSession(), getIdTiers());

                if (tiers == null) {
                    tiers = PRTiersHelper.getAdministrationParId(getSession(), getIdTiers());
                }
            } catch (Exception e) {
                getSession().addError("Le Tiers " + getIdTiers() + "ne peut pas être chargée.");
            }
        }
        return tiers != null;
    }

    public void setCcpOuBanqueFormatte(String string) {
        ccpOuBanqueFormatte = string;
    }

    public void setIdDomaineApplicatifDepuisPyxis(String string) {
        setIdDomaineApplicatif(string);
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdTiersAdressePmtDepuisPyxis(String string) {
        setIdTiersAdressePmt(string);
        setIdReferenceQR("");
        setRetourDepuisPyxis(true);
    }

    public void setIdTiersDepuisPyxis(String string) {
        setIdTiers(string);
        tiers = null;
        setIdReferenceQR("");
        setRetourDepuisPyxis(true);
        setTiersBeneficiaireChange(true);
    }

    public void setIdReferenceQRDepuisPyxis(String string) {
        setIdReferenceQR(string);
        setRetourDepuisPyxis(true);
    }

    public void setIsAdresseModifiee(String string) {
        isAdresseModifiee = string;
    }

    public void setMontantPrestationsDejaVersees(String montantPrestationsDejaVersees) {
        this.montantPrestationsDejaVersees = new BigDecimal(montantPrestationsDejaVersees);
    }

    public void setMontantRetroactif(String montantRetroactif) {
        this.montantRetroactif = new BigDecimal(montantRetroactif);
    }

    public void setNumAffilieDepuisPyxis(String numAffilieDepuisPyxis) throws Exception {
        this.numAffilieDepuisPyxis = numAffilieDepuisPyxis;

        IPRAffilie affilie = PRAffiliationHelper.getEmployeurParNumAffilie(getSession(), numAffilieDepuisPyxis);

        if (affilie != null) {
            setIdAffilieAdressePmt(affilie.getIdAffilie());
        }
    }

    /**
     * setter pour l'attribut adresse paiement.
     *
     * @param adressePaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setAdressePaiementData(TIAdressePaiementData adressePaiement) {
        if (adressePaiement != null) {
            adressePaiementData = adressePaiement;
        }
    }

    public TIAdressePaiementData getAdressePaiementData() {
        return adressePaiementData;
    }

    public void setRetourDepuisPyxis(boolean b) {
        retourDepuisPyxis = b;
    }

    public void setTiersBeneficiaireChange(boolean b) {
        tiersBeneficiaireChange = b;
    }

    public String getReferenceQRFormattee() {
        return referenceQRFormattee;
    }

    public void setReferenceQRFormattee(String referenceQRFormattee) {
        this.referenceQRFormattee = referenceQRFormattee;
    }

    public boolean hasRepartiLesCreances(String noRente) throws Exception {
        boolean isReparti = true;
        String idCreancier = getIdCreancier(noRente);

        if (!idCreancier.equals("")) {
            RECreanceAccordeeManager caManager = new RECreanceAccordeeManager();
            caManager.setSession(getSession());
            caManager.setForIdCreancier(idCreancier);
            caManager.setForCsTypeCreancier(IRECreancier.CS_IMPOT_SOURCE);
            caManager.find(BManager.SIZE_NOLIMIT);
            //quand c'est réparti deux lignes sont misent dans la table RECRACC sinon y'a rien
            if (caManager.size() == 0) {
                isReparti = false;
            }
        }

        return isReparti;
    }
    private String getIdCreancier(String noRente) throws Exception {
        RECreancierManager creMgr = new RECreancierManager();
        creMgr.setSession(getSession());
        creMgr.setForCsType(IRECreancier.CS_IMPOT_SOURCE);
        creMgr.setForIdDemandeRente(noRente);
        creMgr.find(BManager.SIZE_NOLIMIT);

        if (creMgr.size() > 0) {

            for (Iterator iterator = creMgr.iterator(); iterator.hasNext(); ) {
                RECreancier creancier = (RECreancier) iterator.next();
                return creancier.getIdCreancier();
            }
        }
        return "";
    }

}