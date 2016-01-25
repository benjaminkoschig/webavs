package globaz.pyxis.api.osiris;

import globaz.osiris.external.IntAdressePaiement;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;

/**
 * Insérez la description du type ici. Date de création : (07.02.2003 15:35:58)
 * 
 * @author: Administrator
 */
public class TIAdressePaiementOSI implements IntAdressePaiement {
    private TIAdresseCourrierOSI adr = new TIAdresseCourrierOSI();
    private TIAdressePaiement adresse = new TIAdressePaiement();
    private TIBanqueOSI banqueOSI = null;
    private TIAvoirPaiement lienAdresse = new TIAvoirPaiement();
    private TITiersOSI tiers = new TITiersOSI();
    private TITiersOSI tiersAdrPmt = new TITiersOSI();

    /**
     * Commentaire relatif au constructeur TIAdressePaiementOSI.
     */
    public TIAdressePaiementOSI() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2001 16:55:52)
     * 
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
     * Insérez la description de la méthode ici. Date de création : (06.12.2001 11:04:10)
     * 
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

    /**
     * Return le code ISO du pays de l'adresse de paiement.
     */
    @Override
    public String getCodeISOPays() {
        return adresse._getPays().getCodeIso();
    }

    /**
     * Cette méthode retourne la date de début de la relation de l'adresse de paiement
     * 
     * @return String date de début de la relation
     */
    @Override
    public String getDateDebutRelation() {
        return lienAdresse.getDateDebutRelation();
    }

    /**
     * Cette méthode retourne la date de fin de la relation de l'adresse de paiement
     * 
     * @return String date de fin de la relation
     */
    @Override
    public String getDateFinRelation() {
        return lienAdresse.getDateFinRelation();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 15:45:11)
     * 
     * @return java.lang.Boolean
     */
    @Override
    public Boolean getEstAdresseDetaillee() {
        return adresse.isDetailAdressePmt();
    }

    /**
     * Renvoie l'id unique de l'entité
     * 
     * @return l'id unique de l'entité
     */
    @Override
    public String getId() {
        return lienAdresse.getId();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2001 16:55:27)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdAdresseCourrier() {
        return adresse.getIdAdresse();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 15:20:04)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdAdressePaiement() {
        return lienAdresse.getIdAdrPmtIntUnique();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 15:22:17)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdBanque() {
        return adresse.getIdTiersBanque();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.12.2001 14:15:14)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdTiers() {
        return lienAdresse.getIdTiers();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 15:20:45)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdTiersTitulaire() {
        return null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 15:45:47)
     * 
     * @return java.lang.Boolean
     */
    @Override
    public Boolean getImprimerListeDetaillee() {
        return adresse.isImpressionDetail();
    }

    /**
     * Renvoie la session en cours
     * 
     * @return la session en cours
     */
    @Override
    public globaz.globall.api.BISession getISession() {
        return lienAdresse.getISession();
    }

    /**
     * Renvoie la date de dernière modification de l'objet (format DD.MM.YYYY).
     * 
     * @return la date de dernière modification de l'objet, null si pas disponible
     */
    @Override
    public final String getLastModifiedDate() {
        if (adresse.getSpy() == null) {
            return null;
        }
        return adresse.getSpy().getDate();
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

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2001 16:56:19)
     * 
     * @return java.lang.String
     */
    @Override
    public String getNomAutreBeneficiaire() {
        return lienAdresse.getBeneficiaireAutre();
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
            // TODO Auto-generated catch block
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 15:21:48)
     * 
     * @return java.lang.String
     */
    @Override
    public String getNumCompte() {
        if ((getTypeAdresse().equals(CCP)) || (getTypeAdresse().equals(CCP_INTERNATIONAL))) {
            return adresse.getNumCcp();
        } else {
            return adresse.getNumCompteBancaire();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.02.2002 17:07:52)
     * 
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
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 15:21:21)
     * 
     * @return java.lang.String
     */
    @Override
    public globaz.osiris.external.IntTiers getTiersTitulaire() {
        return getTiers();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 15:46:32)
     * 
     * @return int
     */
    @Override
    public String getTypeAdresse() {
        String sType = adresse.getTypeAdresse();

        if ((getCodeISOPays().equals("CH"))) {
            if (sType.equals(IConstantes.CS_ADRESSE_PAIEMENT_TYPE_CCP)) {
                return CCP;
            } else if (sType.equals(IConstantes.CS_ADRESSE_PAIEMENT_TYPE_CLEARING)) {
                return BANQUE;
            } else if (sType.equals(IConstantes.CS_ADRESSE_PAIEMENT_TYPE_MANDAT)) {
                return MANDAT;
            } else {
                System.out.println("TIAdressePaiementOSI.getTypeAdresse(): Can't determine adress type (idAdresse=)"
                        + getIdAdressePaiement());
                return "";
            }
        } else {
            if (sType.equals(IConstantes.CS_ADRESSE_PAIEMENT_TYPE_CLEARING)) {
                return BANQUE_INTERNATIONAL;
            } else if (sType.equals(IConstantes.CS_ADRESSE_PAIEMENT_TYPE_MANDAT)) {
                return MANDAT_INTERNATIONAL;
            } else if (sType.equals(IConstantes.CS_ADRESSE_PAIEMENT_TYPE_CCP)) {
                return CCP_INTERNATIONAL;
            } else {
                System.out.println("TIAdressePaiementOSI.getTypeAdresse(): Can't determine adress type (idAdresse=)"
                        + getIdAdressePaiement());
                return "";
            }
        }
    }

    @Override
    public boolean isCompteIBAN() {
        if (!TIAdressePaiement.CS_SANS_FORMAT.equalsIgnoreCase(adresse.findCode())
                && !TIAdressePaiement.CS_FUSION.equalsIgnoreCase(adresse.findCode())) {
            return true;
        }
        return false;
    }

    /**
     * Indique si l'entité est nouvelle (i.e. n'existe pas dans la BD)
     * 
     * @return true si l'entité n'existe pas dans la BD; false sinon
     */
    @Override
    public boolean isNew() {
        return lienAdresse.isNew();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 15:19:26)
     * 
     * @param id
     *            java.lang.String
     */
    @Override
    public void retrieve(globaz.globall.api.BITransaction transaction, String idAdressePaiement) throws Exception {
        // Récupérer le lien tiers/adresse de paiement
        lienAdresse.setIdAdrPmtIntUnique(idAdressePaiement);
        lienAdresse.retrieve(transaction);
        // Récupérer l'adresse de paiement
        if (!lienAdresse.isNew()) {
            // --> seb _adresse =
            // _adresse.getAdressePaiement(_lienAdresse.getSession(),_lienAdresse.getIdAdressePaiement());
            adresse.setIdAdressePmtUnique(lienAdresse.getIdAdressePaiement());
            adresse.setSession(lienAdresse.getSession());
            adresse.retrieve();
            // Erreur s'il n'y a pas d'adresse
            if (adresse == null || adresse.isNew()) {
                throw new Exception("TIAdressePaiementOSI:retrieve(): missing object TIAdressePaiement id="
                        + idAdressePaiement);
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 15:19:26)
     * 
     * @param id
     *            java.lang.String
     */
    @Override
    public void retrieve(String idAdressePaiement) throws Exception {
        retrieve(null, idAdressePaiement);
    }

    /**
     * Modifie la session en cours
     * 
     * @param newISession
     *            la nouvelle session
     */
    @Override
    public void setISession(globaz.globall.api.BISession newSession) {
        lienAdresse.setISession(newSession);
        adresse.setISession(newSession);
    }
}
