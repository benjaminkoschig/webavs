/*
 * Créé le 10 décembre 2010
 */
package globaz.cygnus.vb.ordresversements;

import globaz.corvus.db.lots.RELot;
import globaz.cygnus.api.ordresversements.IREOrdresVersements;
import globaz.cygnus.api.ordresversements.IRFOrdresVersements;
import globaz.cygnus.api.paiement.IRFPrestations;
import globaz.cygnus.db.ordresversements.RFOrdresVersements;
import globaz.cygnus.db.ordresversements.RFOrdresVersementsManager;
import globaz.cygnus.db.paiement.RFPrestation;
import globaz.cygnus.utils.RFUtils;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.math.BigDecimal;
import java.util.Iterator;

/**
 * @author FHA
 * 
 */
public class RFOrdresVersementsViewBean extends RFOrdresVersements implements FWViewBeanInterface {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String montantTotalPrestation = "";

    public String getAdressePaiement(String idTiersAdressePaiement) {

        try {

            if (!JadeStringUtil.isBlankOrZero(idTiersAdressePaiement)) {

                PRTiersWrapper tw = PRTiersHelper.getTiersParId(getSession(), idTiersAdressePaiement);
                if (tw != null) {
                String adrPaiementNom = tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                    String dateEnvoiLot = RFUtils
                            .computeDateEnvoiLotFromPrestation(getIdPrestation(), getSession());
                // Recherche de l'adresse de paiement
                TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(getSession(),
                        (getSession()).getCurrentThreadTransaction(), idTiersAdressePaiement,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", dateEnvoiLot);

                if ((adresse != null) && !adresse.isNew()) {
                    TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
                    source.load(adresse);

                    ITIAdresseFormater tiAdrPaiBanFor;

                    // formatter le no de ccp ou le no bancaire
                    if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                        tiAdrPaiBanFor = new TIAdressePaiementBanqueFormater();
                    } else {
                        tiAdrPaiBanFor = new TIAdressePaiementCppFormater();
                    }

                    String descAdressePaiement = "";
                    if (!JadeStringUtil.isBlank(adrPaiementNom)) {
                        descAdressePaiement = adrPaiementNom + "<br/>"
                                + JadeStringUtil.change(tiAdrPaiBanFor.format(source), "\n", "<br/>");
                    } else {
                        descAdressePaiement = JadeStringUtil.change(tiAdrPaiBanFor.format(source), "\n", "<br/>");
                    }

                    return descAdressePaiement;
                } else {
                    return "";
                }
            } else {
                    return getAdressePaiementFromAdmin(idTiersAdressePaiement);
                }
            } else {
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getAdressePaiementFromAdmin(String idAdministration) throws Exception {
        BSession session = getSession();
        TIAdministrationViewBean adminVB = RFUtils.loadAdministration(idAdministration, session);
        if (adminVB == null) {
            return "";
        }
        String result = "";
        String adrPaiementNom = adminVB.getNom();
        // recherche de l'adresse de paiement
        TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(session,
                session.getCurrentThreadTransaction(), idAdministration,
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

        if ((adresse != null) && !adresse.isNew()) {
            TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
            source.load(adresse);

            ITIAdresseFormater tiAdrPaiBanFor;

            // formatter le no de ccp ou le no bancaire
            if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                tiAdrPaiBanFor = new TIAdressePaiementBanqueFormater();
            } else {
                tiAdrPaiBanFor = new TIAdressePaiementCppFormater();
            }

            if (!JadeStringUtil.isBlank(adrPaiementNom)) {
                result = "<br/>" + adrPaiementNom + "\n" + tiAdrPaiBanFor.format(source) + "\n";
            } else {
                result = "\n" + tiAdrPaiBanFor.format(source) + "\n";
            }

        } else {
            result = adrPaiementNom;
        }
        return result;
    }

    public String getBeneficiaire(String idTiers) {

        PRTiersWrapper tier = null;
        try {
            tier = PRTiersHelper.getTiersParId(getSession(), idTiers);

            if (tier == null) {
                tier = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
            }

            if (tier != null) {
                if (JadeStringUtil.isEmpty(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL))) {
                    return tier.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                } else {
                    return tier.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                }
            } else {
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public String getCsEtatLot() throws Exception {

        RFPrestation rfPrestation = new RFPrestation();
        rfPrestation.setSession(getSession());
        rfPrestation.setIdPrestation(getIdPrestation());
        rfPrestation.retrieve();

        if (!rfPrestation.isNew()) {

            RELot lot = new RELot();
            lot.setSession(getSession());
            lot.setIdLot(rfPrestation.getIdLot());
            lot.retrieve();

            if (!lot.isNew()) {
                return lot.getCsEtatLot();
            } else {
                throw new Exception("RFOrdresVersementsViewBean.getCsEtatLot(): Impossible de retrouver le lot");
            }

        } else {
            throw new Exception("RFOrdresVersementsViewBean.getCsEtatLot(): Impossible de retrouver le lot");
        }
    }

    public String getMontantTotalPrestation() {
        return montantTotalPrestation;
    }

    /*
     * @Override public BSpy getCreationSpy() {
     * 
     * RFOrdresVersements ov = new RFOrdresVersements();
     * 
     * try { ov = RFOrdresVersements.loadOV(this.getSession(), this.getSession().getCurrentThreadTransaction(),
     * this.getId()); } catch (Exception e) { } return ov.getCreationSpy(); }
     * 
     * public String getLibelleType() { return this.getSession().getCodeLibelle(this.getCsRole()); }
     * 
     * @Override public BSpy getSpy() {
     * 
     * RFOrdresVersements ov = new RFOrdresVersements();
     * 
     * try { ov = RFOrdresVersements.loadOV(this.getSession(), this.getSession().getCurrentThreadTransaction(),
     * this.getId()); } catch (Exception e) { } return ov.getSpy(); }
     */

    /**
     * 
     * @return true si le typde de rente est du type "Dette"
     */
    public boolean isCsTypeDette() {

        return IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION.equals(getCsRole());

    }

    public boolean isPrestationValidee() throws Exception {

        RFPrestation prestation = new RFPrestation();
        prestation.setSession(getSession());
        prestation.setIdPrestation(getIdPrestation());
        prestation.retrieve();

        if (!prestation.isNew()) {
            return prestation.getCsEtatPrestation().equals(IRFPrestations.CS_ETAT_PRESTATION_VALIDE);
        } else {
            throw new Exception(
                    "RFOrdresVersementsViewBean.isPrestationValidee():Impossible de retrouver la prestation");
        }
    }

    public void retrieveMontantTotalPrestation() throws Exception {

        BigDecimal montantTotalBigDec = new BigDecimal(0);

        RFOrdresVersementsManager rfOrdVerMgr = new RFOrdresVersementsManager();
        rfOrdVerMgr.setSession(getSession());
        rfOrdVerMgr.setForIdPrestation(getIdPrestation());
        rfOrdVerMgr.changeManagerSize(0);
        rfOrdVerMgr.find();

        for (Iterator<RFOrdresVersements> rfOrdVerItr = rfOrdVerMgr.iterator(); rfOrdVerItr.hasNext();) {

            RFOrdresVersements rfOrdVer = rfOrdVerItr.next();

            if (null != rfOrdVer) {
                if (rfOrdVer.getTypeVersement().equals(IRFOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL)
                        || (rfOrdVer.getIsCompense().booleanValue() && rfOrdVer.getTypeVersement().equals(
                                IRFOrdresVersements.CS_TYPE_RESTITUTION))
                        || (rfOrdVer.getIsCompense().booleanValue() && rfOrdVer.getTypeVersement().equals(
                                IRFOrdresVersements.CS_TYPE_DETTE))) {
                    montantTotalBigDec = montantTotalBigDec.add(new BigDecimal(rfOrdVer.getMontant())
                            .add(new BigDecimal(rfOrdVer.getMontantDepassementQD())));
                }
            } else {
                throw new Exception(
                        "RFOrdresVersementsViewBean.montantTotalPrestation(): Impossible de retrouver l'ov de la prestation n° "
                                + getIdPrestation());
            }
        }

        setMontantTotalPrestation(montantTotalBigDec.toString());

    }

    public void setMontantTotalPrestation(String montantTotalPrestation) {
        this.montantTotalPrestation = montantTotalPrestation;
    }

}
