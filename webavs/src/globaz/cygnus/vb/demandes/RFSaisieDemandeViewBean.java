package globaz.cygnus.vb.demandes;

import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.db.demandes.RFAssDemandeDev19Ftd15Manager;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeDev19;
import globaz.cygnus.db.demandes.RFDemandeDev19JointAssDemandeDev19Ftd15;
import globaz.cygnus.db.demandes.RFDemandeDev19JointAssDemandeDev19Ftd15Manager;
import globaz.cygnus.db.demandes.RFDemandeFra16;
import globaz.cygnus.db.demandes.RFDemandeFrq17Fra18;
import globaz.cygnus.db.demandes.RFDemandeMai13;
import globaz.cygnus.db.demandes.RFDemandeMoy5_6_7;
import globaz.cygnus.db.demandes.RFPrDemandeJointDossier;
import globaz.cygnus.db.dossiers.RFPeriodeCAAIWrapper;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.contributions.RFContributionsAssistanceAIUtils;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.cygnus.business.constantes.ERFProperties;

/**
 * @author jje
 */
public class RFSaisieDemandeViewBean extends RFSaisieDemandeAbstractViewBean {

    private String csEtat = "";
    private String csGenreDeSoin = "";
    private String csSource = "";
    private String csStatut = "";
    private String csTypeVhc = "";
    private String dateDebutTraitement = "";
    // DemandeMoy5
    private String dateDecisionOAI = "";
    // DemandeFrq1718
    private String dateDecompte = "";
    /**
     * Pour le mandat InfoRom D0034
     */
    private String dateDernierPaiement = "";
    private String dateEnvoiAcceptation = "";
    private String dateEnvoiMDC = "";
    // DemandeDev19
    private String dateEnvoiMDT = "";
    private String dateFacture = "";
    private String dateFinTraitement = "";
    private String dateImputation = "";
    // Demande
    private String dateReception = "";
    private String dateReceptionPreavis = "";
    private String idDecision = "";
    private String idDemande = "";
    private String idDossier = "";
    private String idQdPrincipale = "";
    private String idSousTypeDeSoin = "";
    private Boolean isContratDeTravail = Boolean.FALSE;
    private Boolean isForcerPaiement = Boolean.FALSE;
    // DemandeFrq17
    private Boolean isPP = Boolean.FALSE;
    // Demande 16
    private Boolean isTVA = false;
    // DemandeFtd15
    private Vector<String[]> listDevisData = null;
    private String listIdDevis = "";
    private String montantAcceptation = "";
    private String montantADeduire = "";
    private String montantAPayer = "";
    private String montantDecompte = "";
    private String montantEnExcedent = "";
    private String montantFacture = "";
    private String montantFacture44 = "";
    private String montantMensuel = "";
    private String montantVerseOAI = "";
    // DemandeMaintienDom13
    private String nombreHeure = "";
    private String nombreKilometres = "";
    private String numeroDecompte = "";
    private String numeroFacture = "";
    /**
     * Pour le mandat InfoRom D0034
     */
    private List<RFPeriodeCAAIWrapper> periodesCAAI = null;
    private String prixKilometre = "";
    private String remarqueFournisseur = "";

    private Boolean isTexteRedirection = Boolean.FALSE;

    public RFSaisieDemandeViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    public String getCodeAPpourJSP(String codeApi) {
        if (JadeStringUtil.isBlankOrZero(codeApi)) {
            return getSession().getLabel("WARNING_RF_DEM_S_DETAIL_CONTRIBUTION_ASSISTANCE_AI_NON_DEFINI");
        }
        return RFContributionsAssistanceAIUtils.getDetailCodeAPI(getSession(), codeApi);
    }

    public String getCsEtat() {
        return csEtat;
    }

    public String getCsGenreDeSoin() {
        return csGenreDeSoin;
    }

    public String getCsSource() {
        return csSource;
    }

    public String getCsStatut() {
        return csStatut;
    }

    public String getCsTypeVhc() {
        return csTypeVhc;
    }

    public String getDateDebutTraitement() {
        return dateDebutTraitement;
    }

    public String getDateDecisionOAI() {
        return dateDecisionOAI;
    }

    public String getDateDecompte() {
        return dateDecompte;
    }

    public String getDateDernierPaiement() {
        return dateDernierPaiement;
    }

    public String getDateEnvoiAcceptation() {
        return dateEnvoiAcceptation;
    }

    public String getDateEnvoiMDC() {
        return dateEnvoiMDC;
    }

    public String getDateEnvoiMDT() {
        return dateEnvoiMDT;
    }

    public String getDateFacture() {
        return dateFacture;
    }

    public String getDateFinTraitement() {
        return dateFinTraitement;
    }

    public String getDateImputation() {
        return dateImputation;
    }

    public String getDateReception() {
        return dateReception;
    }

    public String getDateReceptionPreavis() {
        return dateReceptionPreavis;
    }

    @Override
    public String getId() {
        return getIdDemande();
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdQdPrincipale() {
        return idQdPrincipale;
    }

    public String getIdSousTypeDeSoin() {
        return idSousTypeDeSoin;
    }

    public Boolean getIsContratDeTravail() {
        return isContratDeTravail;
    }

    public Boolean getIsForcerPaiement() {
        return isForcerPaiement;
    }

    public Boolean getIsPP() {
        return isPP;
    }

    public final Boolean getIsTVA() {
        return isTVA;
    }

    public Vector<String[]> getListDevisData() {
        return listDevisData;
    }

    public String getListIdDevis() {
        return listIdDevis;
    }

    public String getMontantAcceptation() {
        return montantAcceptation;
    }

    public String getMontantADeduire() {
        return montantADeduire;
    }

    public String getMontantAPayer() {
        return montantAPayer;
    }

    public String getMontantAssocieListIdDevis(String idDevis) {

        String[] idDevisMontantTab = listIdDevis.split(",");
        String montantAssocie = "";

        for (int i = 0; i < idDevisMontantTab.length; i++) {
            if (!JadeStringUtil.isBlankOrZero(idDevisMontantTab[i])) {

                String[] idDevisTab = idDevisMontantTab[i].split("-");

                if (idDevisTab[0].equals(idDevis)) {
                    montantAssocie = idDevisTab[1];
                }

            }
        }

        return montantAssocie;
    }

    public String getMontantCAAI() {
        // if (JadeStringUtil.isBlankOrZero(this.montantCAAI)) {
        // return this.getSession().getLabel("WARNING_RF_DEM_S_DETAIL_CONTRIBUTION_ASSISTANCE_AI_NON_DEFINI");
        // }
        // StringBuilder montant = new StringBuilder();
        // montant.append("<strong>");
        // montant.append(this.montantCAAI);
        // montant.append(" CHF</strong>");
        // return montant.toString();
        // TODO : refaire
        return "";
    }

    public String getMontantDecompte() {
        return montantDecompte;
    }

    public String getMontantEnExcedent() {
        return montantEnExcedent;
    }

    public String getMontantFacture() {
        return montantFacture;
    }

    public String getMontantFacture44() {
        return montantFacture44;
    }

    public String getMontantMensuel() {
        return montantMensuel;
    }

    public String getMontantVerseOAI() {
        return montantVerseOAI;
    }

    public String getNombreHeure() {
        return nombreHeure;
    }

    public String getNombreKilometres() {
        return nombreKilometres;
    }

    public String getNumeroDecompte() {
        return numeroDecompte;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public List<RFPeriodeCAAIWrapper> getPeriodesCAAI() {
        return periodesCAAI;
    }

    public String getPrixKilometre() {
        return prixKilometre;
    }

    public String getRemarqueFournisseur() {
        return remarqueFournisseur;
    }

    private boolean isDevisAssocieDemande(String idDemande, String idDevis) throws Exception {

        RFAssDemandeDev19Ftd15Manager rfDevisMgr = new RFAssDemandeDev19Ftd15Manager();
        rfDevisMgr.setSession(getSession());

        rfDevisMgr.setForIdDemande19(idDevis);
        rfDevisMgr.setForIdDemande15(idDemande);
        rfDevisMgr.changeManagerSize(0);
        rfDevisMgr.find();

        if (rfDevisMgr.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Méthode qui retourne la liste des devis (demande 19) liée à la demande ftd 15
     * 
     * @return le détail du requérant formaté
     */
    public void rechercheListDevis(String idDemandeCourante) throws Exception {

        listDevisData = new Vector<String[]>();

        RFDemandeDev19JointAssDemandeDev19Ftd15Manager rfDevisMgr = new RFDemandeDev19JointAssDemandeDev19Ftd15Manager();
        rfDevisMgr.setSession(getSession());

        if (!getIsAfficherDetail()) {
            rfDevisMgr.setForCsEtat(IRFDemande.ENREGISTRE);
        }

        RFPrDemandeJointDossier rfPrDemJoiDos = RFUtils.getDossierJointPrDemande(getIdTiers(), getSession());

        if (rfPrDemJoiDos != null) {
            rfDevisMgr.setForIdDossier(rfPrDemJoiDos.getIdDossier());

            JACalendarGregorian cal = new JACalendarGregorian();

            JADate dateDeReference = null;

            if (getIsAfficherDetail()) {
                dateDeReference = new JADate(getDateFacture());
            } else {
                dateDeReference = JACalendar.today();
            }

            int nbMoisRechercheDevisInt = Integer.valueOf(RFPropertiesUtils.getNbMoisRechercheDevis()).intValue();

            String fromDateDebut = cal.addMonths(dateDeReference, -nbMoisRechercheDevisInt).toStr(".");
            String toDateFin = cal.addMonths(dateDeReference, +nbMoisRechercheDevisInt).toStr(".");

            rfDevisMgr.setFromDateDebutFacture(fromDateDebut);
            rfDevisMgr.setFromDateFinFacture(toDateFin);
            rfDevisMgr.changeManagerSize(0);
            rfDevisMgr.find();

            Set<String> devisTraiteSet = new HashSet<String>();

            if (!getIsAfficherDetail()) {
                Iterator<RFDemandeDev19JointAssDemandeDev19Ftd15> iterator = rfDevisMgr.iterator();

                while (iterator.hasNext()) {
                    RFDemandeDev19JointAssDemandeDev19Ftd15 rfDemDev19 = iterator.next();

                    if (null != rfDemDev19) {
                        if (!JadeStringUtil.isBlankOrZero(rfDemDev19.getMontantAcceptation())
                                && !JadeStringUtil.isBlankOrZero(rfDemDev19.getDateEnvoiAcceptation())
                                && !devisTraiteSet.contains(rfDemDev19.getIdDemandeDevis19())) {

                            BigDecimal soldeBde = new BigDecimal(rfDemDev19.getMontantAcceptation().replace("'", ""))
                                    .add(RFUtils.retrieveMontantAffecteDevis(rfDemDev19.getIdDemandeDevis19(),
                                            getSession()).negate());

                            if (soldeBde.compareTo(new BigDecimal("0")) > 0) {

                                String[] dev19 = new String[8];
                                dev19[0] = rfDemDev19.getIdDemandeDevis19();
                                dev19[1] = rfDemDev19.getDateFacture();
                                dev19[2] = rfDemDev19.getDateReceptionPreavis();
                                dev19[3] = rfDemDev19.getMontantFacture();
                                dev19[4] = rfDemDev19.getMontantAcceptation();
                                dev19[5] = soldeBde.toString();
                                dev19[6] = Boolean.FALSE.toString();
                                dev19[7] = "";

                                listDevisData.add(dev19);
                            }

                            devisTraiteSet.add(rfDemDev19.getIdDemandeDevis19());

                        }

                    }
                }
            } else {

                Iterator<RFDemandeDev19JointAssDemandeDev19Ftd15> iterator = rfDevisMgr.iterator();
                while (iterator.hasNext()) {
                    RFDemandeDev19JointAssDemandeDev19Ftd15 rfDemDev19 = iterator.next();

                    if (null != rfDemDev19) {
                        if (!JadeStringUtil.isBlankOrZero(rfDemDev19.getMontantAcceptation())
                                && !JadeStringUtil.isBlankOrZero(rfDemDev19.getDateEnvoiAcceptation())
                                && !devisTraiteSet.contains(rfDemDev19.getIdDemandeDevis19())) {

                            String[] dev19 = new String[8];
                            dev19[0] = rfDemDev19.getIdDemandeDevis19();
                            dev19[1] = rfDemDev19.getDateFacture();
                            dev19[2] = rfDemDev19.getDateReceptionPreavis();
                            dev19[3] = rfDemDev19.getMontantFacture();
                            dev19[4] = rfDemDev19.getMontantAcceptation();
                            dev19[5] = new BigDecimal(rfDemDev19.getMontantAcceptation().replace("'", "")).add(
                                    RFUtils.retrieveMontantAffecteDevis(rfDemDev19.getIdDemandeDevis19(), getSession())
                                            .negate()).toString();

                            if (!rfDemDev19.getIdDemandeFtd15Ass().equals(idDemandeCourante)) {
                                dev19[6] = Boolean.FALSE.toString();
                                dev19[7] = "";

                            } else {
                                dev19[6] = Boolean.TRUE.toString();
                                dev19[7] = rfDemDev19.getMontantAssocieAuDevis();

                                if (JadeStringUtil.isEmpty(listIdDevis)) {
                                    listIdDevis += dev19[0] + "-" + dev19[7];
                                } else {
                                    listIdDevis += "," + dev19[0] + "-" + dev19[7];
                                }
                            }

                            // Si il est lié à la demande, on l'ajoute de toute façon
                            if (isDevisAssocieDemande(idDemandeCourante, rfDemDev19.getIdDemandeDevis19())) {

                                if (rfDemDev19.getIdDemandeFtd15Ass().equals(idDemandeCourante)) {
                                    listDevisData.add(dev19);
                                    devisTraiteSet.add(rfDemDev19.getIdDemandeDevis19());
                                }
                                // Si il n'est pas lié on l'ajoute seulement si il n'est pas soldé
                            } else {
                                if (!JadeStringUtil.isBlankOrZero(dev19[5])) {
                                    listDevisData.add(dev19);
                                }
                                devisTraiteSet.add(rfDemDev19.getIdDemandeDevis19());
                            }

                        }
                    }
                }

            }
        }

    }

    @Override
    public void retrieve() throws Exception {

        Integer CodeTypeDeSoin = new Integer(getCodeTypeDeSoinList());

        if ((CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_MOYENS_AUXILIAIRES_5)
                || (CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_MOYENS_AUXILIAIRES_6)
                || (CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_MOYENS_AUXILIAIRES_7)) {

            RFDemandeMoy5_6_7 rfDemandeMoy5 = new RFDemandeMoy5_6_7();
            rfDemandeMoy5.setSession(getSession());
            rfDemandeMoy5.setIdDemandeMoyensAux(getIdDemande());
            rfDemandeMoy5.setHasCreationSpy(true);
            rfDemandeMoy5.setHasSpy(true);
            rfDemandeMoy5.retrieve();

            if (!rfDemandeMoy5.isNew()) {
                setViewBeanDemandeMoyAuxProperties(rfDemandeMoy5);
            } else {
                RFUtils.setMsgErreurInattendueViewBean(this, "retrieve()", "RFSaisieDemandeViewBean");
            }

        } else if (CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_MAINTIEN_A_DOMICILE) {

            RFDemandeMai13 rfDemandeMai13 = new RFDemandeMai13();
            rfDemandeMai13.setSession(getSession());
            rfDemandeMai13.setIdDemandeMaintienDom13(getIdDemande());
            rfDemandeMai13.setHasCreationSpy(true);
            rfDemandeMai13.setHasSpy(true);
            rfDemandeMai13.retrieve();

            if (!rfDemandeMai13.isNew()) {
                setViewBeanDemandeMail13Properties(rfDemandeMai13);
            } else {
                RFUtils.setMsgErreurInattendueViewBean(this, "retrieve()", "RFSaisieDemandeViewBean");
            }

        } else if ((CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_FRQP)
                || (CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_FRAIS_REFUSES)) {

            RFDemandeFrq17Fra18 rfDemandeFrq17Fra18 = new RFDemandeFrq17Fra18();
            rfDemandeFrq17Fra18.setSession(getSession());
            rfDemandeFrq17Fra18.setIdDemande1718(getIdDemande());
            rfDemandeFrq17Fra18.setHasCreationSpy(true);
            rfDemandeFrq17Fra18.setHasSpy(true);
            rfDemandeFrq17Fra18.retrieve();

            if (!rfDemandeFrq17Fra18.isNew()) {
                setViewBeanDemandeFrq17Fra18Properties(rfDemandeFrq17Fra18);
            } else {
                RFUtils.setMsgErreurInattendueViewBean(this, "retrieve()", "RFSaisieDemandeViewBean");
            }

        } else if (CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_DEVIS_DENTAIRE) {

            RFDemandeDev19 rfDemandeDev19 = new RFDemandeDev19();
            rfDemandeDev19.setSession(getSession());
            rfDemandeDev19.setIdDemandeDevis19(getIdDemande());
            rfDemandeDev19.setHasCreationSpy(true);
            rfDemandeDev19.setHasSpy(true);
            rfDemandeDev19.retrieve();

            if (!rfDemandeDev19.isNew()) {
                setViewBeanDemandeDev19Properties(rfDemandeDev19);
            } else {
                RFUtils.setMsgErreurInattendueViewBean(this, "retrieve()", "RFSaisieDemandeViewBean");
            }

        } else if (CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_FRAIS_DENTAIRE) {

            RFDemande rfDemande = new RFDemande();
            rfDemande.setSession(getSession());
            rfDemande.setIdDemande(getIdDemande());
            rfDemande.retrieve();

            if (!rfDemande.isNew()) {
                setViewBeanDemandeProperties(rfDemande);
            } else {
                RFUtils.setMsgErreurInattendueViewBean(this, "retrieve()", "RFSaisieDemandeViewBean");
            }
        } else if (CodeTypeDeSoin.intValue() == RFUtils.CODE_TYPE_DE_SOIN_TRANSPORT_16) {
            RFDemandeFra16 rfDemandeFra16 = new RFDemandeFra16();
            rfDemandeFra16.setSession(getSession());
            rfDemandeFra16.setIdDemandeFra16(getIdDemande());
            rfDemandeFra16.setHasCreationSpy(true);
            rfDemandeFra16.setHasSpy(true);
            rfDemandeFra16.retrieve();

            if (!rfDemandeFra16.isNew()) {
                setViewBeanDemandeFra16Properties(rfDemandeFra16);
            } else {
                RFUtils.setMsgErreurInattendueViewBean(this, "retrieve()", "RFSaisieDemandeViewBean");
            }
        } else {

            RFDemande rfDemande = new RFDemande();
            rfDemande.setSession(getSession());
            rfDemande.setIdDemande(getIdDemande());
            rfDemande.retrieve();

            if (!rfDemande.isNew()) {
                setViewBeanDemandeProperties(rfDemande);
            } else {
                RFUtils.setMsgErreurInattendueViewBean(this, "retrieve()", "RFSaisieDemandeViewBean");
            }
        }

    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsGenreDeSoin(String csGenreDeSoin) {
        this.csGenreDeSoin = csGenreDeSoin;
    }

    public void setCsSource(String csSource) {
        this.csSource = csSource;
    }

    public void setCsStatut(String csStatut) {
        this.csStatut = csStatut;
    }

    public void setCsTypeVhc(String csTypeVhc) {
        this.csTypeVhc = csTypeVhc;
    }

    public void setDateDebutTraitement(String dateDebutTraitement) {
        this.dateDebutTraitement = dateDebutTraitement;
    }

    public void setDateDecisionOAI(String dateDecisionOAI) {
        this.dateDecisionOAI = dateDecisionOAI;
    }

    public void setDateDecompte(String dateDecompte) {
        this.dateDecompte = dateDecompte;
    }

    public void setDateDernierPaiement(String dateDernierPaiement) {
        this.dateDernierPaiement = dateDernierPaiement;
    }

    public void setDateEnvoiAcceptation(String dateEnvoiAcceptation) {
        this.dateEnvoiAcceptation = dateEnvoiAcceptation;
    }

    public void setDateEnvoiMDC(String dateEnvoiMDC) {
        this.dateEnvoiMDC = dateEnvoiMDC;
    }

    public void setDateEnvoiMDT(String dateEnvoiMDT) {
        this.dateEnvoiMDT = dateEnvoiMDT;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }

    public void setDateFinTraitement(String dateFinTraitement) {
        this.dateFinTraitement = dateFinTraitement;
    }

    public void setDateImputation(String dateImputation) {
        this.dateImputation = dateImputation;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public void setDateReceptionPreavis(String dateReceptionPreavis) {
        this.dateReceptionPreavis = dateReceptionPreavis;
    }

    @Override
    public void setId(String newId) {
        setIdDemande(newId);
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdQdPrincipale(String idQdPrincipale) {
        this.idQdPrincipale = idQdPrincipale;
    }

    public void setIdSousTypeDeSoin(String idSousTypeDeSoin) {
        this.idSousTypeDeSoin = idSousTypeDeSoin;
    }

    public void setIsContratDeTravail(Boolean isContratDeTravail) {
        this.isContratDeTravail = isContratDeTravail;
    }

    public void setIsForcerPaiement(Boolean isForcerPaiement) {
        this.isForcerPaiement = isForcerPaiement;
    }

    public void setIsPP(Boolean isPP) {
        this.isPP = isPP;
    }

    public final void setIsTVA(Boolean isTVA) {
        this.isTVA = isTVA;
    }

    public void setListDevisData(Vector<String[]> listDevis) {
        listDevisData = listDevis;
    }

    public void setListIdDevis(String listIdDevis) {
        this.listIdDevis = listIdDevis;
    }

    public void setMontantAcceptation(String montantAcceptation) {
        this.montantAcceptation = montantAcceptation;
    }

    public void setMontantADeduire(String montantADeduire) {
        this.montantADeduire = montantADeduire;
    }

    public void setMontantAPayer(String montantAPayer) {
        this.montantAPayer = montantAPayer;
    }

    public void setMontantDecompte(String montantDecompte) {
        this.montantDecompte = montantDecompte;
    }

    public void setMontantEnExcedent(String montantEnExcedent) {
        this.montantEnExcedent = montantEnExcedent;
    }

    public void setMontantFacture(String montantFacture) {
        this.montantFacture = montantFacture;
    }

    public void setMontantFacture44(String montantFacture44) {
        this.montantFacture44 = montantFacture44;
    }

    public void setMontantMensuel(String montantMensuel) {
        this.montantMensuel = montantMensuel;
    }

    public void setMontantVerseOAI(String montantVerseOAI) {
        this.montantVerseOAI = montantVerseOAI;
    }

    public void setNombreHeure(String nombreHeure) {
        this.nombreHeure = nombreHeure;
    }

    public void setNombreKilometres(String nombreKilometres) {
        this.nombreKilometres = nombreKilometres;
    }

    public void setNumeroDecompte(String numeroDecompte) {
        this.numeroDecompte = numeroDecompte;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public void setPeriodesCAAI(List<RFPeriodeCAAIWrapper> periodesCAAI) {
        this.periodesCAAI = periodesCAAI;
    }

    public void setPrixKilometre(String prixKilometre) {
        this.prixKilometre = prixKilometre;
    }

    public void setRemarqueFournisseur(String remarqueFournisseur) {
        this.remarqueFournisseur = remarqueFournisseur;
    }

    private void setViewBeanDemandeDev19Properties(RFDemandeDev19 demande) throws Exception {

        setViewBeanDemandeProperties(demande);

        setIdDemande(demande.getIdDemandeDevis19());

        setDateEnvoiMDT(demande.getDateEnvoiMDT());
        setDateEnvoiMDC(demande.getDateEnvoiMDC());
        setDateReceptionPreavis(demande.getDateReceptionPreavis());
        setMontantAcceptation(demande.getMontantAcceptation());
        setDateEnvoiAcceptation(demande.getDateEnvoiAcceptation());

    }

    private void setViewBeanDemandeFra16Properties(RFDemandeFra16 demande) throws Exception {

        setViewBeanDemandeProperties(demande);

        setIdDemande(demande.getIdDemandeFra16());
        setCsTypeVhc(demande.getCsTypeVhc());
        setNombreKilometres(demande.getNombreKilometres());
        setPrixKilometre(demande.getPrixKilometre());
        setIsTVA(demande.getIsTva());

    }

    private void setViewBeanDemandeFrq17Fra18Properties(RFDemandeFrq17Fra18 demande) throws Exception {

        setViewBeanDemandeProperties(demande);

        setIdDemande(demande.getIdDemande1718());

        setDateDecompte(demande.getDateDecompte());
        setMontantDecompte(demande.getMontantDecompte());
        setNumeroDecompte(demande.getNumeroDecompte());
        setCsGenreDeSoin(demande.getCsGenreDeSoin());

    }

    private void setViewBeanDemandeMail13Properties(RFDemandeMai13 demande) throws Exception {

        setViewBeanDemandeProperties(demande);

        setIdDemande(demande.getIdDemandeMaintienDom13());

        setNombreHeure(demande.getNombreHeure());

    }

    private void setViewBeanDemandeMoyAuxProperties(RFDemandeMoy5_6_7 demande) throws Exception {

        setViewBeanDemandeProperties(demande);

        setIdDemande(demande.getIdDemandeMoyensAux());

        setDateDecisionOAI(demande.getDateDecisionOAI());
        setMontantVerseOAI(demande.getMontantVerseOAI());
        setMontantFacture44(demande.getMontantFacture44());

    }

    private void setViewBeanDemandeProperties(RFDemande demande) throws Exception {

        setIdDemande(demande.getIdDemande());

        setDateReception(demande.getDateReception());
        setDateFacture(demande.getDateFacture());

        if (demande.getIdSousTypeDeSoin().equals(IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE)
                || demande.getIdSousTypeDeSoin().equals(IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE_DIABETIQUE)
                || (demande.getIdSousTypeDeSoin().equals(IRFTypesDeSoins.st_17_FRANCHISE_ET_QUOTEPARTS) && demande
                        .getIsPP())) {

            setDateDebutTraitement(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(demande.getDateDebutTraitement()));
            if (!JadeStringUtil.isBlankOrZero(demande.getDateFinTraitement())) {
                setDateFinTraitement(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(demande.getDateFinTraitement()));
            }

        } else {

            setDateDebutTraitement(demande.getDateDebutTraitement());
            if (!JadeStringUtil.isBlankOrZero(demande.getDateFinTraitement())) {
                setDateFinTraitement(demande.getDateFinTraitement());
            }

        }

        setDateImputation(demande.getDateImputation());
        setNumeroFacture(demande.getNumeroFacture());
        setRemarqueFournisseur(demande.getRemarqueFournisseur());
        setMontantFacture(demande.getMontantFacture());
        setMontantAPayer(demande.getMontantAPayer());
        setIsForcerPaiement(demande.getIsForcerPaiement());
        setIsContratDeTravail(demande.getIsContratDeTravail());
        setIsPP(demande.getIsPP());
        setIsTexteRedirection(demande.getIsTexteRedirection());
        setCsEtat(demande.getCsEtat());
        setCsSource(demande.getCsSource());
        setCsStatut(demande.getCsStatut());
        setIdFournisseurDemande(demande.getIdFournisseur());
        setIdGestionnaire(demande.getIdGestionnaire());
        setIsRetro(demande.getIsRetro());
        setIdDossier(demande.getIdDossier());
        setIdAdressePaiementDemande(demande.getIdAdressePaiement());
        setIdSousTypeDeSoin(demande.getIdSousTypeDeSoin());
        setIdQdPrincipale(demande.getIdQdPrincipale());
        setIdDecision(demande.getIdDecision());
        setSpy(demande.getSpy());
        setCreationSpy(demande.getCreationSpy());
        setMontantMensuel(demande.getMontantMensuel());
        setIdDemandeParent(demande.getIdDemandeParent());

    }

    @Override
    public void update() throws Exception {
    }

    public Boolean getIsTexteRedirection() {
        return isTexteRedirection;
    }

    public void setIsTexteRedirection(Boolean isTexteRedirection) {
        this.isTexteRedirection = isTexteRedirection;
    }

    public Boolean isGestionTexteRedirection() {
        try {
            return ERFProperties.GESTION_TEXTE_REDIRECTION.getBooleanValue();
        } catch (PropertiesException e) {
            JadeLogger.error(e, "Unabled to find the property");
        }

        return false;
    }
}
