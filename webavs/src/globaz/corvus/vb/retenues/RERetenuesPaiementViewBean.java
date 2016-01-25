/*
 * Créé le 17 juil. 07
 */

package globaz.corvus.vb.retenues;

import globaz.corvus.api.retenues.IRERetenues;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.retenues.RERetenuesPaiement;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.retenues.RERetenuesUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CARubriqueManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.comptes.CATypeSection;
import globaz.osiris.db.comptes.CATypeSectionManager;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tauxImposition.api.IPRTauxImposition;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author HPE
 */
public class RERetenuesPaiementViewBean extends RERetenuesPaiement implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_SEL_ADRESSE_PAIEMENT = new Object[] {
            new String[] { "idTiersAdressePmtDepuisPyxis", "idTiers" },
            new String[] { "idDomaineApplicatifDepuisPyxis", "idApplication" },
            new String[] { "numAffilieDepuisPyxis", "idExterneAvoirPaiement" } };

    private String adresseFormattee = "";
    private String ccpOuBanqueFormatte = "";
    private String forIdRenteAccordee = "";
    private String forIdTiersBeneficiaire = "";
    private String idDomaineApplicatifDepuisPyxis = "";
    private String idTiersAdressePmtDepuisPyxis = "";
    private Collection<String> idTiersFamille = null;
    private String isAdresseModifiee = "false";
    private String isChangeBeneficiaire = "";
    private boolean isReAfficher = false;
    private String montantRenteAccordee = "";
    private String numAffilieDepuisPyxis = "";
    private boolean retourDepuisPyxis = false;
    private boolean tiersBeneficiaireChange = false;

    @Override
    protected void _validate(BStatement statement) throws Exception {
        super._validate(statement);

        if (!statement.getTransaction().hasErrors()) {
            // On test que le montant a retenir mensuellement ne soit pas plus grand
            // que le montant de la rente accordée
            if (new FWCurrency(getMontantRenteAccordee()).compareTo(new FWCurrency(getMontantRetenuMensuel())) < 0) {
                _addError(statement.getTransaction(),
                        getSession().getLabel("VALID_RET_PMT_MONTANT_A_RETENIT_PLUS_GRAND_RA"));
            }
            // Le compte annexe doit exister au moment de la création de la retenue.
            if (IRERetenues.CS_TYPE_FACTURE_FUTURE.equals(getCsTypeRetenue())
                    || IRERetenues.CS_TYPE_FACTURE_EXISTANTE.equals(getCsTypeRetenue())) {
                if (!isCompteAnnexe()) {
                    _addError(statement.getTransaction(), getSession().getLabel("JSP_RET_D_COMPTE_ANNEXE"));
                }
            }
            // Le section doit exister au moment de la création de la retenue pour
            // le compte annexe et le type de section donnés.
            if (IRERetenues.CS_TYPE_FACTURE_EXISTANTE.equals(getCsTypeRetenue())) {
                if (!isSectionValide()) {
                    _addError(statement.getTransaction(), getSession().getLabel("JSP_RET_D_SECTION"));
                }
            }
            // bz7199 : La sommes des retenues ne doit pas être supérieur à la rente accordée
            // S'assurer que la somme total des retenues ne dépassent pas le montant de la rente accordée.
            if (isSommeRetenuesPlusGrandeQuePrestationAcordee()) {
                _addError(statement.getTransaction(),
                        getSession().getLabel("VALID_RET_PMT_SOMMES_RETENUES_PLUS_GRANDE_RA"));
            }

            if (isRetenueMensuelleSuprieureRetenueTotal()
                    && !IRERetenues.CS_TYPE_IMPOT_SOURCE.equals(getCsTypeRetenue())) {
                _addError(statement.getTransaction(), getSession().getLabel("VALID_RET_PMT_MNT_MENS_SUP_MNT_TOTAL"));
            }
        }
    }

    public String getAdresseFormattee() {
        return adresseFormattee;
    }

    public String getCcpOuBanqueFormatte() {
        return ccpOuBanqueFormatte;
    }

    public String getCsEtatRenteAccordee() {
        try {
            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(getSession());
            ra.setIdPrestationAccordee(getForIdRenteAccordee());
            ra.retrieve();

            return ra.getCsEtat();
        } catch (Exception e) {
            return "";
        }
    }

    public String getDescriptionRefExterne() {
        return "";
    }

    public String getDesignation() {

        if (getCsTypeRetenue().equals(IRERetenues.CS_TYPE_ADRESSE_PMT)) {

            PRTiersWrapper tier;
            try {
                tier = PRTiersHelper.getTiersParId(getSession(), getIdTiersAdressePmt());
                if (null == tier) {
                    tier = PRTiersHelper.getAdministrationParId(getSession(), getIdTiersAdressePmt());
                }
                if (tier != null) {
                    if (JadeStringUtil.isEmpty(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL))) {
                        return tier.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                    } else {
                        return tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " / "
                                + tier.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                    }
                } else {
                    return "Pas de tiers retrouvé avec l'id: " + getIdTiersAdressePmt();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } else if (getCsTypeRetenue().equals(IRERetenues.CS_TYPE_COMPTE_SPECIAL)) {
            return "Retenue sur compte spécial";
        } else if (getCsTypeRetenue().equals(IRERetenues.CS_TYPE_FACTURE_EXISTANTE)) {
            if (JadeStringUtil.isBlankOrZero(getNoFacture())) {
                if (!JadeStringUtil.isIntegerEmpty(getReferenceInterne())) {
                    try {
                        CASection section = new CASection();
                        section.setSession(getSession());
                        section.setIdSection(getReferenceInterne());
                        section.retrieve();
                        if (!section.isNew()) {
                            return section.getIdExterne();
                        } else {
                            return "Pas de section retrouvée avec ReferenceInterne: " + getReferenceInterne();
                        }
                    } catch (Exception e) {
                        return "";
                    }
                } else {
                    return "";
                }
            } else {
                return getNoFacture();
            }
        } else if (getCsTypeRetenue().equals(IRERetenues.CS_TYPE_FACTURE_FUTURE)) {
            return "Retenue sur facture future";
        } else if (getCsTypeRetenue().equals(IRERetenues.CS_TYPE_IMPOT_SOURCE)) {
            return "Impôt à la source";
        } else {
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

    public String getForIdRenteAccordee() {
        return forIdRenteAccordee;
    }

    public String getForIdTiersBeneficiaire() {
        return forIdTiersBeneficiaire;
    }

    public String getIdDomaineApplicatifDepuisPyxis() {
        return idDomaineApplicatifDepuisPyxis;
    }

    public String getIdTiersAdressePmtDepuisPyxis() {
        return idTiersAdressePmtDepuisPyxis;
    }

    public Collection<String> getIdTiersFamille() {
        return idTiersFamille;
    }

    /**
     * Retourne les ID tiers de la famille en une ligne séparés par des virgules.<br/>
     * Pour une utilisation avec le {@link CACompteAnnexeManager} dans le widget
     * 
     * @return les ID's dans une chaîne de caractères, séparés par des virgules
     * @see {@link CACompteAnnexeManager#setForIdTiersIn(String)}
     */
    public String getIdTiersFamilleInline() {
        if (idTiersFamille == null) {
            return "";
        }

        StringBuilder concat = new StringBuilder();
        boolean isFirstId = true;
        for (String unIdTiers : idTiersFamille) {
            if (!JadeNumericUtil.isInteger(unIdTiers)) {
                continue;
            }
            if (isFirstId) {
                isFirstId = false;
            } else {
                // voir la méthode setForIdTiersIn de CACompteAnnexeManager
                concat.append("|");
            }
            concat.append(unIdTiers);
        }
        return concat.toString();
    }

    public String getIsAdresseModifiee() {
        return isAdresseModifiee;
    }

    public String getIsChangeBeneficiaire() {
        return isChangeBeneficiaire;
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

    public String getLibelleTypeRetenue() {
        return getSession().getCodeLibelle(getCsTypeRetenue());
    }

    public String getListeRubrique(String idRubrique) {
        StringBuffer options = new StringBuffer();
        try {
            CARubriqueManager mgr = new CARubriqueManager();
            mgr.setSession(getSession());
            mgr.setForNatureRubrique(APIRubrique.STANDARD);
            mgr.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < mgr.size(); i++) {
                CARubrique rub = (CARubrique) mgr.getEntity(i);
                /**/
                options.append("<option value='");
                options.append(rub.getIdRubrique() + "'");
                // Sélectionner le bon si existant
                if (!JadeStringUtil.isIntegerEmpty(idRubrique)) {
                    if (rub.getIdRubrique().equals(idRubrique)) {
                        options.append(" selected ");
                    }
                }
                options.append(">");
                options.append(rub.getIdExterne());
                options.append(" - ");
                options.append(rub.getDescription());
                options.append("</option>");
            }
        } catch (Exception ex) {
        }
        return options.toString();
    }

    /**
     * retourne un tableau de correspondance entre methodes client et provider pour le retour depuis pyxis avec le
     * bouton de selection d'une adresse de paiement.
     * 
     * @return la valeur courante de l'attribut methodes selection adresse
     */
    public Object[] getMethodesSelectionAdressePaiement() {
        return RERetenuesPaiementViewBean.METHODES_SEL_ADRESSE_PAIEMENT;
    }

    public String getMontantRenteAccordee() {
        return montantRenteAccordee;
    }

    public String getMontantRetenuMensuelSpecial(String montantRA) throws JAException {

        // Voir si le montant déjà retenu atteint le montant total à retenir ou
        // si le montant à retenir n'est pas plus grand que la différence des 2
        // premiers.

        FWCurrency montantARetenir = new FWCurrency(getMontantRetenuMensuel());
        FWCurrency montantTotalARetenir = new FWCurrency(getMontantTotalARetenir());
        FWCurrency montantDejaRetenu = new FWCurrency(getMontantDejaRetenu());

        // si sur adresse paiement

        if (getCsTypeRetenue().equals(IRERetenues.CS_TYPE_ADRESSE_PMT)
                || getCsTypeRetenue().equals(IRERetenues.CS_TYPE_COMPTE_SPECIAL)
                || getCsTypeRetenue().equals(IRERetenues.CS_TYPE_FACTURE_FUTURE)
                || getCsTypeRetenue().equals(IRERetenues.CS_TYPE_FACTURE_EXISTANTE)) {
            if (montantDejaRetenu.floatValue() >= montantTotalARetenir.floatValue()) {
                return "0.00";
            } else if (montantARetenir.floatValue() > (montantTotalARetenir.floatValue() - montantDejaRetenu
                    .floatValue())) {
                return new FWCurrency(montantTotalARetenir.floatValue() - montantDejaRetenu.floatValue()).toString();
            } else {
                return montantARetenir.toString();
            }
        } else if (getCsTypeRetenue().equals(IRERetenues.CS_TYPE_IMPOT_SOURCE)) {
            FWCurrency montantImpoSource = null;

            if (!JadeStringUtil.isDecimalEmpty(getTauxImposition())) {
                montantImpoSource = new FWCurrency((new FWCurrency(montantRA).floatValue() / 100)
                        * (new FWCurrency(getTauxImposition())).floatValue());
                montantImpoSource.round(FWCurrency.ROUND_ENTIER);
            } else if (!JadeStringUtil.isDecimalEmpty(getMontantRetenuMensuel())) {
                montantImpoSource = new FWCurrency(getMontantRetenuMensuel());
            } else {
                // recherche du taux
                PRTauxImpositionManager tManager = new PRTauxImpositionManager();
                tManager.setSession(getSession());
                tManager.setForCsCanton(getCantonImposition());
                tManager.setForTypeImpot(IPRTauxImposition.CS_TARIF_D);

                JADate dateDebut = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));
                JADate dateFin = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));

                tManager.setForPeriode(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateDebut.toStrAMJ()),
                        PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateFin.toStrAMJ()));
                try {
                    tManager.find();

                    if (tManager.size() > 0) {
                        PRTauxImposition taux = (PRTauxImposition) tManager.getFirstEntity();

                        montantImpoSource = new FWCurrency((new FWCurrency(montantRA).floatValue() / 100)
                                * (new FWCurrency(taux.getTaux())).floatValue());
                        montantImpoSource.round(FWCurrency.ROUND_ENTIER);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    montantImpoSource = new FWCurrency("0.00");
                }
                return montantImpoSource.toString();
            }
            return montantImpoSource.toString();
        } else {
            return "";
        }
    }

    public String getNssBeneficiaire() {
        try {
            PRTiersWrapper tier = PRTiersHelper.getTiersParId(getSession(), getForIdTiersBeneficiaire());

            if (tier != null) {
                return tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            } else {
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getNssTiersAdressePmt() throws Exception {
        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), getIdTiersAdressePmt());

        if (tiers != null) {
            return tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        } else {
            return "";
        }
    }

    public String getNumAffilieDepuisPyxis() {
        return numAffilieDepuisPyxis;
    }

    public String getRubrique() {
        try {
            CARubrique rubrique = new CARubrique();
            rubrique.setSession(getSession());
            rubrique.setIdRubrique(getIdRubrique());
            rubrique.retrieve();

            return rubrique.getIdExterne() + " - " + rubrique.getDescription();
        } catch (Exception e) {
            return "";
        }
    }

    public List<String[]> getTypesSectionsCA() {
        List<String[]> list = new ArrayList<String[]>();

        CATypeSectionManager manTypeSection = new CATypeSectionManager();
        manTypeSection.setSession(getSession());

        try {
            manTypeSection.find();

            for (int i = 0; i < manTypeSection.size(); i++) {
                CATypeSection elm = (CATypeSection) manTypeSection.get(i);
                list.add(new String[] { elm.getIdTypeSection(), elm.getDescription(getSession().getIdLangueISO()) });
            }
        } catch (Exception e) {
            return list;
        }
        return list;
    }

    public boolean isCompteAnnexe() throws Exception {
        if (getCsTypeRetenue().equals(IRERetenues.CS_TYPE_FACTURE_FUTURE)
                || getCsTypeRetenue().equals(IRERetenues.CS_TYPE_FACTURE_EXISTANTE)) {
            if (!JadeStringUtil.isIntegerEmpty(getIdRetenue())) {
                CACompteAnnexe compte = new CACompteAnnexe();
                compte.setSession(getSession());
                compte.setIdExterneRole(getIdExterne());
                compte.setIdRole(getRole());
                compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                compte.retrieve();

                if (compte.isNew()) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean isModifiable() {
        return true;
    }

    /**
     * getter pour l'attribut modifier permis.
     * 
     * @return la valeur courante de l'attribut modifier permis
     */
    public boolean isModifierPermis() {
        return true;
    }

    public boolean isMontantTotalDejaRetenuIs0() {
        if (new FWCurrency(getMontantDejaRetenu()).isZero()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMontantTotalDejaRetenuIsSupA0() {
        if (new FWCurrency(getMontantDejaRetenu()).isPositive() && !new FWCurrency(getMontantDejaRetenu()).isZero()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isReAfficher() {
        return isReAfficher;
    }

    private boolean isRetenueMensuelleSuprieureRetenueTotal() throws Exception {
        BigDecimal montantMensuelRetenue = new BigDecimal(getMontantRetenuMensuel().replace("'", ""));
        BigDecimal montantTotalRetenue = new BigDecimal(getMontantTotalARetenir().replace("'", ""));

        if (montantTotalRetenue.compareTo(montantMensuelRetenue) < 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    public boolean isSectionValide() {
        if (getCsTypeRetenue().equals(IRERetenues.CS_TYPE_FACTURE_EXISTANTE)) {
            if (!JadeStringUtil.isIntegerEmpty(getNoFacture())) {
                try {
                    CACompteAnnexe compte = new CACompteAnnexe();
                    compte.setSession(getSession());
                    compte.setIdExterneRole(getIdExterne());
                    compte.setIdRole(getRole());
                    compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                    compte.retrieve();

                    CASectionManager sManager = new CASectionManager();
                    sManager.setSession(getSession());
                    sManager.setForIdCompteAnnexe(compte.getIdCompteAnnexe());
                    sManager.setForIdTypeSection(getIdTypeSection());
                    sManager.setForIdExterne(getNoFacture());

                    if (sManager.getCount() == 0) {
                        return false;
                    } else {
                        return true;
                    }
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private boolean isSommeRetenuesPlusGrandeQuePrestationAcordee() throws Exception {
        BigDecimal montantRenteAccorrdee = new BigDecimal(montantRenteAccordee.replace("'", ""));
        BigDecimal sommeRetenues = RERetenuesUtil.getSommesRetenuesRenteAccordee(this);

        if (montantRenteAccorrdee.compareTo(sommeRetenues) < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * getter pour l'attribut supprimer permis.
     * 
     * @return la valeur courante de l'attribut supprimer permis.
     */
    public boolean isSupprimerPermis() {
        return true;
    }

    public boolean isTiersBeneficiaireChange() {
        return tiersBeneficiaireChange;
    }

    public void setAdresseFormattee(String string) {
        adresseFormattee = string;
    }

    /**
     * setter pour l'attribut adresse paiement.
     * 
     * @param adressePaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setAdressePaiement(TIAdressePaiementData adressePaiement) {
        if (adressePaiement != null) {
            setIdTiersAdressePmt(adressePaiement.getIdTiers());
            setIdDomaineApplicatif(adressePaiement.getIdApplication());
        } else {
            setIdTiersAdressePmt("");
            setIdDomaineApplicatif("");
        }
    }

    public void setCcpOuBanqueFormatte(String string) {
        ccpOuBanqueFormatte = string;
    }

    public void setForIdRenteAccordee(String string) {
        forIdRenteAccordee = string;
    }

    public void setForIdTiersBeneficiaire(String string) {
        forIdTiersBeneficiaire = string;
    }

    public void setIdDomaineApplicatifDepuisPyxis(String string) {
        idDomaineApplicatifDepuisPyxis = string;
    }

    public void setIdTiersAdressePmtDepuisPyxis(String string) {
        idTiersAdressePmtDepuisPyxis = string;
        retourDepuisPyxis = true;
        tiersBeneficiaireChange = true;
    }

    public void setIdTiersFamille(Collection<String> idTiersFamille) {
        this.idTiersFamille = idTiersFamille;
    }

    /**
     * Permet de définir les ID tiers de la famille dans une chaîne de caractères où les ID sont séparés par une virgule
     * 
     * @param idTiersFamille
     */
    public void setIdTiersFamille(String idTiersFamille) {
        if (!JadeStringUtil.isBlank(idTiersFamille)) {
            Collection<String> parsedIds = new ArrayList<String>();
            for (String unIdTiers : idTiersFamille.split(",")) {
                if (!JadeNumericUtil.isInteger(unIdTiers)) {
                    continue;
                }
                parsedIds.add(unIdTiers);
            }
            this.setIdTiersFamille(parsedIds);
        }
    }

    public void setIsAdresseModifiee(String string) {
        isAdresseModifiee = string;
    }

    public void setIsChangeBeneficiaire(String string) {
        isChangeBeneficiaire = string;
    }

    public void setMontantRenteAccordee(String string) {
        montantRenteAccordee = string;
    }

    public void setNumAffilieDepuisPyxis(String numAffilieDepuisPyxis) {
        this.numAffilieDepuisPyxis = numAffilieDepuisPyxis;
    }

    public void setReAfficher(boolean isReAfficher) {
        this.isReAfficher = isReAfficher;
    }

    public void setRetourDepuisPyxis(boolean b) {
        retourDepuisPyxis = b;
    }

    public void setTiersBeneficiaireChange(boolean b) {
        tiersBeneficiaireChange = b;
    }
}