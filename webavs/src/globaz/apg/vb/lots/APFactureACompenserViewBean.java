/*
 * Créé le 29 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.vb.lots;

import globaz.apg.api.lots.IAPLot;
import globaz.apg.db.lots.APCompensation;
import globaz.apg.db.lots.APFactureACompenser;
import globaz.apg.db.lots.APLot;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APFactureACompenserViewBean extends APFactureACompenser implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String ERREUR_COMPENSATION_INTROUVABLE = "COMPENSATION_INTROUVABLE";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idLot = "";
    private APLot lot = null;
    private String noAVSOuAffilie = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getIdAffilieCompensation() {
        if (!JadeStringUtil.isIntegerEmpty(getIdCompensationParente())) {
            try {
                APCompensation compensation = new APCompensation();
                compensation.setSession(getSession());
                compensation.setIdCompensation(getIdCompensationParente());
                compensation.retrieve();

                return compensation.getIdAffilie();
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }

    /**
     * getter pour l'attribut id lot
     * 
     * @return la valeur courante de l'attribut id lot
     */
    public String getIdLot() {
        return idLot;
    }

    public String getIdTiersCompensation() {

        if (!JadeStringUtil.isIntegerEmpty(getIdCompensationParente())) {
            try {
                APCompensation compensation = new APCompensation();
                compensation.setSession(getSession());
                compensation.setIdCompensation(getIdCompensationParente());
                compensation.retrieve();

                return compensation.getIdTiers();
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }

    /**
     * @see globaz.apg.db.lots.APFactureACompenser#getMontant()
     */
    @Override
    public String getMontant() {
        return JANumberFormatter.fmt(montant, true, true, true, 2);
    }

    /**
     * getter pour l'attribut montant APG
     * 
     * @return la valeur courante de l'attribut montant APG
     */
    public String getMontantAPG() {
        try {
            APCompensation compensation = loadCompensation();

            if (compensation != null) {
                return JANumberFormatter.fmt(compensation.getMontantTotal(), true, true, true, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getSession().addError(getSession().getLabel(ERREUR_COMPENSATION_INTROUVABLE));
        }

        return "";
    }

    /**
     * getter pour l'attribut no AVSOu affilie
     * 
     * @return la valeur courante de l'attribut no AVSOu affilie
     */
    public String getNoAVSOuAffilie() {
        if (JadeStringUtil.isEmpty(noAVSOuAffilie)) {
            if (tiers != null) {
                noAVSOuAffilie = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            } else if (affilie != null) {
                noAVSOuAffilie = affilie.getNumAffilie();
            }
        }

        return noAVSOuAffilie;
    }

    /**
     * getter pour l'attribut nom beneficiaire base
     * 
     * @return la valeur courante de l'attribut nom beneficiaire base
     */
    public String getNomPrenomBeneficiaireBase() {
        String nomPrenom = "";

        if (!JadeStringUtil.isIntegerEmpty(getIdCompensationParente())) {
            try {
                APCompensation compensation = new APCompensation();
                compensation.setSession(getSession());
                compensation.setIdCompensation(getIdCompensationParente());
                compensation.retrieve();

                if (!JadeStringUtil.isIntegerEmpty(compensation.getIdTiers())) {
                    PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), compensation.getIdTiers());
                    nomPrenom += tiers.getProperty(PRTiersWrapper.PROPERTY_NOM);
                    if (!JadeStringUtil.isEmpty(tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM))) {
                        nomPrenom += ", " + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                    }
                }
            } catch (Exception e) {
                nomPrenom = getSession().getLabel("NOM_INTROUVABLE");
            }
        }
        return nomPrenom;
    }

    /**
     * getter pour l'attribut paiement adresse beneficiaire formate
     * 
     * @return la valeur courante de l'attribut paiement adresse beneficiaire formate
     */
    public String getPaiementAdresseBeneficiaireFormate() {
        String retValue = "";

        try {
            TIAdressePaiementData adresse = loadAdressePaiement();

            if ((adresse != null) && !adresse.isNew()) {
                TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();

                source.load(adresse);

                retValue = new TIAdressePaiementBeneficiaireFormater().format(source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retValue;
    }

    /**
     * getter pour l'attribut adresse paiement
     * 
     * @return la valeur courante de l'attribut adresse paiement
     */
    public String getPaiementCCPOuBanqueFormate() {
        String retValue = "";

        try {
            TIAdressePaiementData adresse = loadAdressePaiement();

            if ((adresse != null) && !adresse.isNew()) {
                TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();

                source.load(adresse);

                if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                    retValue = new TIAdressePaiementBanqueFormater().format(source);
                } else {
                    retValue = new TIAdressePaiementCppFormater().format(source);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retValue;
    }

    /**
     * getter pour l'attribut selecteur methodes
     * 
     * @return la valeur courante de l'attribut selecteur methodes
     */
    public Object[] getSelecteurMethodes() {
        return new Object[] { new String[] { "setIdTiers", "getIdTiers" },
                new String[] { "setIdAdresse", "getIdAdressePaiement" },
                new String[] { "setDomaineAdresse", "getIdApplication" } };
    }

    /**
     * getter pour l'attribut adresse valide
     * 
     * @return la valeur courante de l'attribut adresse valide
     */
    public boolean isAdresseValide() {
        return !JadeStringUtil.isIntegerEmpty(idAdresse);
    }

    /**
     * true si la facture a compenser peut etre modifiee
     * 
     * @return
     */
    public boolean isModifiable() {

        loadLot();
        return lot != null && !IAPLot.CS_VALIDE.equals(lot.getEtat());
    }

    /**
     * getter pour l'attribut valide
     * 
     * @return la valeur courante de l'attribut valide
     */
    public boolean isValide() {
        return !JadeStringUtil.isIntegerEmpty(idCompensationParente);
    }

    public APLot loadLot() {

        if (lot == null) {
            lot = new APLot();
            lot.setSession(getSession());
            lot.setIdLot(getIdLot());
            try {
                lot.retrieve();
            } catch (Exception e) {
                return null;
            }
        }

        return lot;
    }

    /**
     * setter pour l'attribut affilie
     * 
     * @param affilie
     *            une nouvelle valeur pour cet attribut
     */
    public void setAffilie(IPRAffilie affilie) {
        this.affilie = affilie;
        noAVSOuAffilie = affilie.getNumAffilie();
        idTiers = affilie.getIdTiers();
        idAffilie = affilie.getIdAffilie();
        tiers = null;
    }

    /**
     * setter pour l'attribut id lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdLot(String string) {
        idLot = string;
    }

    /**
     * @see globaz.apg.db.lots.APFactureACompenser#setMontant(java.lang.String)
     */
    @Override
    public void setMontant(String string) {
        super.setMontant(JANumberFormatter.deQuote(string));
    }

    /**
     * setter pour l'attribut no AVSOu affilie
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAVSOuAffilie(String string) {
        noAVSOuAffilie = string;
    }

    /**
     * setter pour l'attribut tiers
     * 
     * @param wrapper
     *            une nouvelle valeur pour cet attribut
     */
    public void setTiers(PRTiersWrapper wrapper) {
        tiers = wrapper;
        noAVSOuAffilie = wrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        setIdTiers(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        setIdAffilie("");
        affilie = null;
    }

}
