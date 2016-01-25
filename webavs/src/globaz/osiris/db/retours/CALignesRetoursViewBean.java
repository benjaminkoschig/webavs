package globaz.osiris.db.retours;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CASection;
import globaz.pyxis.adresse.datasource.TIAbstractAdressePaiementDataSource;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementDataManager;

public class CALignesRetoursViewBean extends CALignesRetours implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CARetours retour = null;

    /**
     * getter pour l'attribut paiement adresse beneficiaire formate
     * 
     * @return la valeur courante de l'attribut paiement adresse beneficiaire formate
     */
    private String getAdressePaiementBanqueCCP(TIAdressePaiementData adresse) {
        String retValue = "";
        try {
            if ((adresse != null) && !adresse.isNew()) {
                TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();

                source.load(adresse);

                if (JadeStringUtil.isEmpty(source.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_CCP))) {
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
     * getter pour l'attribut paiement adresse beneficiaire formate
     * 
     * @return la valeur courante de l'attribut paiement adresse beneficiaire formate
     */
    private String getAdressePaiementBeneficiaire(TIAdressePaiementData adresse) {
        String retValue = "";
        try {
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
     * Donne l'etat du retour de cette ligne de retour
     * 
     * @return
     */
    public String getCsEtatRetour() {
        loadRetour();
        if (retour != null) {
            return retour.getCsEtatRetour();
        } else {
            return "";
        }
    }

    /**
     * Pour l'affichage de l'adresse de paiement d'une ligne de retour sur adresse de paiement
     * 
     * @return
     */
    public String getDescriptionLigneRetourSurAdressePaiementAdresse() {

        TIAdressePaiementDataManager mgr = new TIAdressePaiementDataManager();
        TIAdressePaiementData adressePmt = new TIAdressePaiementData();

        mgr.setSession(getSession());
        mgr.setForIdTiers(getIdTiers());
        if (!JadeStringUtil.isIntegerEmpty(getIdExterne())) {
            mgr.setForIdExterne(getIdExterne());
        }
        mgr.setForIdApplication(getIdDomaine());
        mgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());

        try {
            mgr.find();
        } catch (Exception e) {
            return "";
        }

        if (mgr.size() > 0) {
            adressePmt = (TIAdressePaiementData) mgr.get(0);
            return getAdressePaiementBanqueCCP(adressePmt);
        }

        return "";
    }

    /**
     * Pour l'affichage du beneficiaire d'une ligne de retour sur adresse de paiement
     * 
     * @return
     */
    public String getDescriptionLigneRetourSurAdressePaiementBeneficiaire() {

        TIAdressePaiementDataManager mgr = new TIAdressePaiementDataManager();
        TIAdressePaiementData adressePmt = new TIAdressePaiementData();

        mgr.setSession(getSession());
        mgr.setForIdTiers(getIdTiers());
        if (!JadeStringUtil.isIntegerEmpty(getIdExterne())) {
            mgr.setForIdExterne(getIdExterne());
        }
        mgr.setForIdApplication(getIdDomaine());
        mgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());

        try {
            mgr.find();
        } catch (Exception e) {
            return "";
        }

        if (mgr.size() > 0) {
            adressePmt = (TIAdressePaiementData) mgr.get(0);
            return getAdressePaiementBeneficiaire(adressePmt);
        }

        return "";
    }

    /**
     * Pour l'affichage d'une ligne de retour sur section
     * 
     * @return
     */
    public String getDescriptionLigneRetourSurSection() {

        if (JadeStringUtil.isIntegerEmpty(getIdSection())) {
            return "";
        } else {
            CASection s = new CASection();
            s.setSession(getSession());
            s.setIdSection(getIdSection());
            try {
                s.retrieve();
                return s.getDescription() + " - " + s.getDateSection() + " - " + s.getSoldeFormate();
            } catch (Exception e) {
                return "";
            }
        }
    }

    public String getIdAvoirPaiementUniqueLigneRetourSurAdressePaiementAdresse() {

        TIAdressePaiementDataManager mgr = new TIAdressePaiementDataManager();
        TIAdressePaiementData adressePmt = new TIAdressePaiementData();

        mgr.setSession(getSession());
        mgr.setForIdTiers(getIdTiers());
        if (!JadeStringUtil.isIntegerEmpty(getIdExterne())) {
            mgr.setForIdExterne(getIdExterne());
        }
        mgr.setForIdApplication(getIdDomaine());
        mgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());

        try {
            mgr.find();
        } catch (Exception e) {
            return "";
        }

        if (mgr.size() > 0) {
            adressePmt = (TIAdressePaiementData) mgr.get(0);
            return adressePmt.getIdAvoirPaiementUnique();
        }

        return "";
    }

    /**
     * Vrais si le retour de cette ligne de retour peut etre modifiee
     * 
     * @return
     */
    public boolean isRetourModifiable() {
        return CARetours.CS_ETAT_RETOUR_OUVERT.equals(getCsEtatRetour())
                || CARetours.CS_ETAT_RETOUR_SUSPENS.equals(getCsEtatRetour())
                || CARetours.CS_ETAT_RETOUR_TRAITE.equals(getCsEtatRetour());
    }

    /**
     * Chanrgement du retour de cette lugne de retour
     */
    private void loadRetour() {
        if ((retour == null) && !JadeStringUtil.isIntegerEmpty(getIdRetour())) {
            retour = new CARetours();
            retour.setSession(getSession());
            retour.setIdRetour(getIdRetour());
            try {
                retour.retrieve();
            } catch (Exception e) {
                retour = null;
            }
        }
    }

}
