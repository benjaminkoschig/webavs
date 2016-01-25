package globaz.osiris.db.retours;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.pyxis.adresse.datasource.TIAbstractAdressePaiementDataSource;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementDataManager;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class CARetoursViewBean extends CARetours implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_SEL_ADRESSE = new Object[] {
            new String[] { "idTiersDepuisPyxis", "getIdTiers" },
            new String[] { "setIdDomaineLigneRetourSurAdressePaiement", "idApplication" },
            new String[] { "numAffilieLigneRetourSurAdressePaiement", "idExterneAvoirPaiement" } };

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String NOM_ECRAN = "retours_de";

    private List attachedDocuments = null;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private CACompteAnnexe compteAnnexe = null;

    private String forCsEtatRetour = "";
    private String forCsMotifRetour = "";
    private String forDateRetour = "";

    // pour l'impression de la liste des retours
    private String forIdLot = "";
    private String forMontantRetour = "";

    private String idDomaineLigneRetourSurAdressePaiement = "";
    // pour les lignes de retour sur section
    private String idSectionLigneRetourSurSection = "";
    // pour les lignes de retour sur adresse de paiement
    private String idTiersLigneRetourSurAdressePaiement = "";
    private String likeLibelleRetour = "";

    private String likeNumNom = "";
    private CALotsRetours lot = null;

    private String montantLigneRetourSurAdressePaiement = "";
    private String montantLigneRetourSurSection = "";
    private String numAffilieLigneRetourSurAdressePaiement = "";
    // gestion des retours depuis Pyxis
    private boolean retourDepuisPyxis;
    private Vector sectionsLibres = null;
    // pour la suppression des lignes de retour
    private String selectedId = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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

    public List getAttachedDocuments() {
        return attachedDocuments;
    }

    /**
     * Donne le compte annexe de ce retour Date de création : (18.01.2002 10:59:42)
     * 
     * @return globaz.osiris.db.comptes.CACompteAnnexe
     */
    public CACompteAnnexe getCompteAnnexe() {
        if (compteAnnexe == null) {
            compteAnnexe = new CACompteAnnexe();
            compteAnnexe.setISession(getSession());
            compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());
            try {
                compteAnnexe.retrieve();
                if (compteAnnexe.isNew()) {
                    compteAnnexe = null;
                }
            } catch (Exception e) {
                compteAnnexe = null;
            }
        }
        return compteAnnexe;
    }

    /**
     * Donne l'etat du lot de retour de ce retour
     * 
     * @return
     */
    public String getCsEtatLot() {
        loadLotRetour();
        if (lot != null) {
            return lot.getCsEtatLot();
        } else {
            return "";
        }
    }

    /**
     * Donne le libelle de l'etat de ce retour
     * 
     * @return
     */
    public String getCsEtatRetourLibelle() {
        return getSession().getCodeLibelle(super.getCsEtatRetour());
    }

    /**
     * Donne le libelle du motif de ce retour
     * 
     * @return
     */
    public String getCsMotifRetourLibelle() {
        return getSession().getCodeLibelle(super.getCsMotifRetour());
    }

    /**
     * Pour l'affichage de l'adresse de paiement d'une ligne de retour sur adresse de paiement
     * 
     * @return
     */
    public String getDescriptionLigneRetourSurAdressePaiementAdresse() {

        TIAdressePaiementDataManager mgr = new TIAdressePaiementDataManager();
        TIAdressePaiementData adressePmt = new TIAdressePaiementData();

        // pas de retour depuis Pyxis
        if (JadeStringUtil.isEmpty(getIdTiersLigneRetourSurAdressePaiement())
                || JadeStringUtil.isEmpty(getIdDomaineLigneRetourSurAdressePaiement())) {
            return "";
        }

        mgr.setSession(getSession());
        mgr.setForIdTiers(getIdTiersLigneRetourSurAdressePaiement());
        mgr.setForIdExterne(getNumAffilieLigneRetourSurAdressePaiement());
        mgr.setForIdApplication(getIdDomaineLigneRetourSurAdressePaiement());
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

        // pas de retour depuis Pyxis
        if (JadeStringUtil.isEmpty(getIdTiersLigneRetourSurAdressePaiement())
                || JadeStringUtil.isEmpty(getIdDomaineLigneRetourSurAdressePaiement())) {
            return "";
        }

        mgr.setSession(getSession());
        mgr.setForIdTiers(getIdTiersLigneRetourSurAdressePaiement());
        if (JadeStringUtil.isIntegerEmpty(getNumAffilieLigneRetourSurAdressePaiement())) {
            mgr.setForIdExterne(getNumAffilieLigneRetourSurAdressePaiement());
        }
        mgr.setForIdApplication(getIdDomaineLigneRetourSurAdressePaiement());
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

    public String getForCsEtatRetour() {
        return forCsEtatRetour;
    }

    public String getForCsMotifRetour() {
        return forCsMotifRetour;
    }

    public String getForDateRetour() {
        return forDateRetour;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public String getForMontantRetour() {
        return forMontantRetour;
    }

    public String getIdDomaineLigneRetourSurAdressePaiement() {
        return idDomaineLigneRetourSurAdressePaiement;
    }

    public String getIdSectionLigneRetourSurSection() {
        return idSectionLigneRetourSurSection;
    }

    public String getIdTiersLigneRetourSurAdressePaiement() {
        return idTiersLigneRetourSurAdressePaiement;
    }

    /**
     * Donne le libelle du lot de ce retour
     * 
     * @return
     */
    public String getLibelleLot() {
        loadLotRetour();
        if (lot != null) {
            return lot.getLibelleLot();
        } else {
            return "";
        }
    }

    public String getLikeLibelleRetour() {
        return likeLibelleRetour;
    }

    public String getLikeNumNom() {
        return likeNumNom;
    }

    /**
     * retourne un tableau de correspondance entre methodes client et provider pour le retour depuis pyxis avec le
     * bouton de selection d'une adresse de paiement.
     * 
     * @return la valeur courante de l'attribut methodes selection adresse
     */
    public Object[] getMethodesSelectionAdresse() {
        return CARetoursViewBean.METHODES_SEL_ADRESSE;
    }

    public String getMontantLigneRetourSurAdressePaiement() {
        return montantLigneRetourSurAdressePaiement;
    }

    public String getMontantLigneRetourSurSection() {
        return montantLigneRetourSurSection;
    }

    /**
     * Donne le montant total du lot de ce retour
     * 
     * @return
     */
    public String getMontantTotal() {
        loadLotRetour();
        if (lot != null) {
            return lot.getMontantTotal();
        } else {
            return "";
        }
    }

    public String getNumAffilieLigneRetourSurAdressePaiement() {
        return numAffilieLigneRetourSurAdressePaiement;
    }

    /**
     * Retourne le vecteur de tableaux a 2 entrées {sectionId, sectionDescription} des sections libres d'au compte
     * annexe.
     * 
     * <p>
     * Le vecteur n'est créé qu'une seule fois pour chaque instance de ce view bean.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut responsable data
     */
    public Vector getSectionsLibres() {
        // créer les responsables s'ils n'ont pas déjà été récupérés
        sectionsLibres = null;
        if (sectionsLibres == null) {
            try {

                CASectionManager sManager = new CASectionManager();
                sManager.setSession(getSession());
                sManager.setForIdCompteAnnexe(getIdCompteAnnexe());
                sManager.setForSelectionSections("3"); // solde > 0
                sManager.find();

                // on cherche les sections qui sont deja utilisee
                CALignesRetoursManager lrManager = new CALignesRetoursManager();
                lrManager.setSession(getSession());
                lrManager.setForIdRetour(getIdRetour());
                lrManager.find();

                Iterator lignesRetIter = lrManager.iterator();
                String idsSectionUtilisees = "";
                while (lignesRetIter.hasNext()) {
                    CALignesRetours lr = (CALignesRetours) lignesRetIter.next();

                    if (!JadeStringUtil.isIntegerEmpty(lr.getIdSection())) {

                        idsSectionUtilisees += "," + lr.getIdSection();
                    }
                }

                sectionsLibres = new Vector(sManager.size());
                Iterator iter = sManager.iterator();
                while (iter.hasNext()) {
                    CASection section = (CASection) iter.next();

                    // si la section a deja ete utilisee dans ce retour, on ne
                    // le met pas dans la liste des sections libres
                    if (idsSectionUtilisees.indexOf(section.getIdSection()) == -1) {
                        sectionsLibres.add(new String[] {
                                section.getIdSection(),
                                section.getDescription() + " - " + section.getDateSection() + " - "
                                        + section.getSoldeFormate() });
                    }

                }

            } catch (Exception e) {
                getSession().addError(getSession().getLabel("Erreur lors de la recherche des séctions libres"));
            }

            // on veut une ligne vide
            if (sectionsLibres == null) {
                sectionsLibres = new Vector();
            }

            sectionsLibres.insertElementAt(new String[] { "", "" }, 0);
        }

        return sectionsLibres;
    }

    public String getSelectedId() {
        return selectedId;
    }

    /**
     * Donne le solde pour ce retour
     * 
     * @return
     */
    public String getSolde() {
        CALignesRetoursManager lrManager = new CALignesRetoursManager();
        lrManager.setSession(getSession());
        lrManager.setForIdRetour(getIdRetour());
        try {
            BigDecimal sommeLignes = lrManager.getSum(CALignesRetours.FIELDNAME_MONTANT);

            FWCurrency solde = new FWCurrency(getMontantRetour());
            solde.sub(sommeLignes.toString());
            return solde.toStringFormat();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Vrais si les lignes de retour de ce retour peuvent etre modifiee
     * 
     * @return
     */
    public boolean isLignesRetourModifiables() {
        return CARetours.CS_ETAT_RETOUR_OUVERT.equals(getCsEtatRetour())
                || CARetours.CS_ETAT_RETOUR_SUSPENS.equals(getCsEtatRetour())
                || CARetours.CS_ETAT_RETOUR_TRAITE.equals(getCsEtatRetour());
    }

    /**
     * Vrais si le retour peut etre annule
     * 
     * @return
     */
    public boolean isRetourAnnulable() {
        return CALotsRetours.CS_ETAT_LOT_COMPTABILISE.equals(getCsEtatLot())
                && (CARetours.CS_ETAT_RETOUR_OUVERT.equals(getCsEtatRetour())
                        || CARetours.CS_ETAT_RETOUR_SUSPENS.equals(getCsEtatRetour()) || CARetours.CS_ETAT_RETOUR_TRAITE
                            .equals(getCsEtatRetour()));
    }

    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    /**
     * Vrais si les proprietes du retour peuvent etre modifiees
     * 
     * @return
     */
    public boolean isRetourModifiable() {
        return CALotsRetours.CS_ETAT_LOT_OUVERT.equals(getCsEtatLot());
    }

    /**
     * chargement du lot
     */
    private void loadLotRetour() {
        if ((lot == null) && !JadeStringUtil.isIntegerEmpty(getIdLot())) {
            lot = new CALotsRetours();
            lot.setSession(getSession());
            lot.setIdLot(getIdLot());
            try {
                lot.retrieve();
            } catch (Exception e) {
                lot = null;
            }
        }
    }

    public void setAttachedDocuments(List attachedDocuments) {
        this.attachedDocuments = attachedDocuments;
    }

    public void setForCsEtatRetour(String forCsEtatRetour) {
        this.forCsEtatRetour = forCsEtatRetour;
    }

    public void setForCsMotifRetour(String forCsMotifRetour) {
        this.forCsMotifRetour = forCsMotifRetour;
    }

    public void setForDateRetour(String forDateRetour) {
        this.forDateRetour = forDateRetour;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForMontantRetour(String forMontantRetour) {
        this.forMontantRetour = forMontantRetour;
    }

    public void setIdDomaineLigneRetourSurAdressePaiement(String idDomaineLigneRetourSurAdressePaiement) {
        this.idDomaineLigneRetourSurAdressePaiement = idDomaineLigneRetourSurAdressePaiement;
    }

    public void setIdSectionLigneRetourSurSection(String idSectionLigneRetour) {
        idSectionLigneRetourSurSection = idSectionLigneRetour;
    }

    /**
     * setter pour l'attribut idTiers depuis pyxis.
     * 
     * <p>
     * cette methode initialise un flag interne qui indique qu'elle a ete appellee lors d'un retour depuis pyxis.
     * </p>
     * 
     * @param idTiersAdressePaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersDepuisPyxis(String idTiersAdressePaiement) {
        setIdTiersLigneRetourSurAdressePaiement(idTiersAdressePaiement);
        retourDepuisPyxis = true;
    }

    public void setIdTiersLigneRetourSurAdressePaiement(String idTiersLigneRetourSurAdressePaiement) {
        this.idTiersLigneRetourSurAdressePaiement = idTiersLigneRetourSurAdressePaiement;
    }

    public void setLikeLibelleRetour(String likeLibelleRetour) {
        this.likeLibelleRetour = likeLibelleRetour;
    }

    public void setLikeNumNom(String likeNumNom) {
        this.likeNumNom = likeNumNom;
    }

    public void setMontantLigneRetourSurAdressePaiement(String montantLigneRetourSurAdressePaiement) {
        this.montantLigneRetourSurAdressePaiement = montantLigneRetourSurAdressePaiement;
    }

    public void setMontantLigneRetourSurSection(String montantLigneRetour) {
        montantLigneRetourSurSection = montantLigneRetour;
    }

    public void setNumAffilieLigneRetourSurAdressePaiement(String numAffilieLigneRetourSurAdressePaiement) {
        this.numAffilieLigneRetourSurAdressePaiement = numAffilieLigneRetourSurAdressePaiement;
    }

    public void setRetourDepuisPyxis(boolean retourDepuisPyxis) {
        this.retourDepuisPyxis = retourDepuisPyxis;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

}
