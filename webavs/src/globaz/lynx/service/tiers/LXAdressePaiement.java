/**
 * 
 */
package globaz.lynx.service.tiers;

import globaz.globall.db.BAccessBean;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.osiris.external.IntAdressePaiement;
import globaz.pyxis.api.osiris.TIAdresseCourrierOSI;
import globaz.pyxis.api.osiris.TIBanqueOSI;
import globaz.pyxis.api.osiris.TITiersOSI;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;

/**
 * @author sel
 * 
 */
public class LXAdressePaiement extends BAccessBean implements IntAdressePaiement {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private TIAdresseCourrierOSI adr = null;
    private TIAdressePaiement adresse = new TIAdressePaiement();
    private TIBanqueOSI banqueOSI = null;
    private TITiersOSI tiers = new TITiersOSI();
    private TITiersOSI tiersAdrPmt = new TITiersOSI();

    /**
     * @return globaz.osiris.interfaceext.tiers.IntAdresseCourrier
     */
    @Override
    public globaz.osiris.external.IntAdresseCourrier getAdresseCourrier() {
        try {
            if (adr == null) {
                adr = new TIAdresseCourrierOSI();
            }
            if (!getIdAdresseCourrier().equalsIgnoreCase(adr.getIdAdresseCourrier())) {
                adr.setISession(getISession());
                adr.retrieve(getIdAdresseCourrier());
            }
            return adr;
        } catch (Exception e) {
            System.out.println("TIAdressePaiementOSI.getAdresseCourrier(): Exception raised: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return globaz.osiris.interfaceext.tiers.IntBanqueOsi
     */
    @Override
    public globaz.osiris.external.IntBanque getBanque() {
        if (banqueOSI == null) {
            banqueOSI = new TIBanqueOSI();
        }
        if (!banqueOSI.getIdBanque().equalsIgnoreCase(adresse.getIdTiersBanque())) {
            banqueOSI.setISession(getISession());
            try {
                banqueOSI.retrieve(adresse.getIdTiersBanque());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return banqueOSI;
    }

    @Override
    public String getCodeISOPays() {
        return adresse._getPays().getCodeIso();
    }

    @Override
    public String getDateDebutRelation() {
        return "";
    }

    @Override
    public String getDateFinRelation() {
        return "";
    }

    @Override
    public Boolean getEstAdresseDetaillee() {
        return adresse.isDetailAdressePmt();
    }

    @Override
    public String getId() {
        return adresse.getIdAdressePaiement();
    }

    @Override
    public String getIdAdresseCourrier() {
        return adresse.getIdAdresse();
    }

    @Override
    public String getIdAdressePaiement() {
        return adresse.getIdAdressePaiement();
    }

    @Override
    public String getIdBanque() {
        return adresse.getIdTiersBanque();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntAdressePaiement#getIdTiers()
     */
    @Override
    public String getIdTiers() {
        return adresse.getIdTiersAdresse();
    }

    @Override
    public String getIdTiersTitulaire() {
        return null;
    }

    @Override
    public Boolean getImprimerListeDetaillee() {
        return adresse.isImpressionDetail();
    }

    @Override
    public String getLastModifiedDate() {
        return null;
    }

    /**
     * Renvoie l'heure de dernière modification de l'objet (format HH:MM:SS).
     * 
     * @return l'heure de dernière modification de l'objet, null si pas disponible
     */
    @Override
    public final String getLastModifiedTime() {
        if (adresse.getSpy() == null) {
            return null;
        }
        return adresse.getSpy().getTime();
    }

    /**
     * Renvoie l'id du dernier utilisateur qui a modifié l'objet.
     * 
     * @return l'id du dernier utilisateur qui a modifié l'objet, null si pas disponible
     */
    @Override
    public final String getLastModifiedUser() {
        if (adresse.getSpy() == null) {
            return null;
        }
        return adresse.getSpy().getUser();
    }

    @Override
    public String getNomAutreBeneficiaire() {
        return "";
    }

    @Override
    public String getNomTiersAdrPmt() {
        try {
            if (tiersAdrPmt == null) {
                tiersAdrPmt = new TITiersOSI();
            }
            if (!adresse.getIdTiersAdresse().equalsIgnoreCase(tiersAdrPmt.getIdTiers())) {
                tiersAdrPmt.setISession(adresse.getSession());
                tiersAdrPmt.retrieve(adresse.getIdTiersAdresse());
            }

            if (!tiersAdrPmt.isNew()) {
                return tiersAdrPmt.getNom();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String getNumCompte() {
        if ((getTypeAdresse().equals(IntAdressePaiement.CCP))
                || (getTypeAdresse().equals(IntAdressePaiement.CCP_INTERNATIONAL))) {
            return adresse.getNumCcp();
        } else {
            return adresse.getNumCompteBancaire();
        }
    }

    /**
     * @return globaz.osiris.interfaceext.tiers.IntTiers
     */
    @Override
    public globaz.osiris.external.IntTiers getTiers() {
        try {
            if (tiers == null) {
                tiers = new TITiersOSI();
            }
            if (!getIdTiers().equalsIgnoreCase(tiers.getIdTiers())) {

                tiers.setISession(getISession());
                tiers.retrieve(getIdTiers());
            }
            return tiers;
        } catch (Exception e) {
            System.out.println("TIAdressePaiementOSI.getTiers(): Exception raised: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return java.lang.String
     */
    @Override
    public globaz.osiris.external.IntTiers getTiersTitulaire() {
        return getTiers();
    }

    @Override
    public String getTypeAdresse() {
        String sType = adresse.getTypeAdresse();

        if ((getCodeISOPays().equals("CH"))) {
            if (sType.equals(IConstantes.CS_ADRESSE_PAIEMENT_TYPE_CCP)) {
                return IntAdressePaiement.CCP;
            } else if (sType.equals(IConstantes.CS_ADRESSE_PAIEMENT_TYPE_CLEARING)) {
                return IntAdressePaiement.BANQUE;
            } else if (sType.equals(IConstantes.CS_ADRESSE_PAIEMENT_TYPE_MANDAT)) {
                return IntAdressePaiement.MANDAT;
            } else {
                System.out.println("TIAdressePaiementOSI.getTypeAdresse(): Can't determine adress type (idAdresse=)"
                        + getIdAdressePaiement());
                return "";
            }
        } else {
            if (sType.equals(IConstantes.CS_ADRESSE_PAIEMENT_TYPE_CLEARING)) {
                return IntAdressePaiement.BANQUE_INTERNATIONAL;
            } else if (sType.equals(IConstantes.CS_ADRESSE_PAIEMENT_TYPE_MANDAT)) {
                return IntAdressePaiement.MANDAT_INTERNATIONAL;
            } else if (sType.equals(IConstantes.CS_ADRESSE_PAIEMENT_TYPE_CCP)) {
                return IntAdressePaiement.CCP_INTERNATIONAL;
            } else {
                System.out.println("TIAdressePaiementOSI.getTypeAdresse(): Can't determine adress type (idAdresse=)"
                        + getIdAdressePaiement());
                return "";
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntAdressePaiement#isCompteIBAN()
     */
    @Override
    public boolean isCompteIBAN() {
        if (!IConstantes.CS_ADRESSE_PAIEMENT_SANS_FORMAT.equalsIgnoreCase(adresse.findCode())
                && !IConstantes.CS_ADRESSE_PAIEMENT_FUSION.equalsIgnoreCase(adresse.findCode())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isNew() {
        return adresse.isNew();
    }

    /**
     * @param id
     *            java.lang.String
     */
    @Override
    public void retrieve(globaz.globall.api.BITransaction transaction, String idAdressePaiement) throws Exception {

        if (adresse.getSession() == null) {
            BSession session;
            if (transaction != null) {
                session = ((BTransaction) transaction).getSession();
            } else {
                session = getSession();
            }
            adresse.setSession(session);
        }

        adresse.setIdAdressePmtUnique(idAdressePaiement);
        adresse.retrieve();
        // Erreur s'il n'y a pas d'adresse
        if ((adresse == null) || adresse.isNew()) {
            throw new Exception("TIAdressePaiementOSI:retrieve(): missing object TIAdressePaiement id="
                    + idAdressePaiement);
        }
    }

    @Override
    public void retrieve(String idAdressePaiement) throws Exception {
        this.retrieve(null, idAdressePaiement);
    }
}
