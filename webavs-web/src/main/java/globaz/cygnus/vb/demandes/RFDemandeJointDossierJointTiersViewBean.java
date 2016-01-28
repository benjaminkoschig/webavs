/*
 * Créé le 30 novembre 2009
 */
package globaz.cygnus.vb.demandes;

import globaz.commons.nss.NSUtil;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeJointDossierJointTiers;
import globaz.cygnus.utils.RFSoinsListsBuilder;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.db.BSpy;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRCodeSystem;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Vector;

/**
 * @author jje
 */
public class RFDemandeJointDossierJointTiersViewBean extends RFDemandeJointDossierJointTiers implements
        FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String CLE_DROITS_TOUS = "";
    private static final String LABEL_DROITS_TOUS = "DROITS_TOUS";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private BSpy creationSpy = null;
    private String csGenreDeSoin = "";
    private String dateDecisionOAI = "";
    private String dateDecompte = "";

    private String dateEnvoiAcceptation = "";
    private String dateEnvoiMDC = "";
    private String dateEnvoiMDT = "";

    private String dateReceptionPreavis = "";
    private transient Vector<String[]> etatsDemande = null;

    private String likeNumeroAVS = "";
    private String montantAcceptation = "";
    private String montantDecompte = "";
    private String montantFacture44 = "";
    private String montantVerseOAI = "";
    private String nombreHeure = "";
    private String numeroDecision = "";
    private String numeroDecompte = "";
    private transient Vector<String[]> orderBy = null;
    private String provenance = "";
    private String sousTypeDeSoinParTypeInnerJavascript = "";
    private BSpy spy = null;
    private transient Vector<String[]> statutsDemande = null;
    private boolean trouveDansCI = false;
    private boolean trouveDansTiers = false;
    private transient Vector<String[]> typeDeSoinsDemande = null;

    // private transient Map codeSousTypeDeSoinsDemande = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    public BSpy getCreationSpy() {
        return creationSpy;
    }

    public Vector<String[]> getCsEtatDemandeData(boolean isdroitsTous) {
        if (etatsDemande == null) {
            etatsDemande = PRCodeSystem.getLibellesPourGroupe(IRFDemande.CS_GROUPE_ETAT_DEMANDE, getSession());

            // ajout des options custom
            if (isdroitsTous) {
                etatsDemande.add(0, new String[] { RFDemandeJointDossierJointTiersViewBean.CLE_DROITS_TOUS,
                        getSession().getLabel(RFDemandeJointDossierJointTiersViewBean.LABEL_DROITS_TOUS) });
            }
        }

        return etatsDemande;
    }

    public String getCsGenreDeSoin() {
        return csGenreDeSoin;
    }

    public Vector<String[]> getCsStatutDemandeData(boolean isdroitsTous) {
        if (statutsDemande == null) {
            statutsDemande = PRCodeSystem.getLibellesPourGroupe(IRFDemande.CS_GROUPE_STATUT_DEMANDE, getSession());

            // ajout des options custom
            if (isdroitsTous) {
                statutsDemande.add(0, new String[] { RFDemandeJointDossierJointTiersViewBean.CLE_DROITS_TOUS,
                        getSession().getLabel(RFDemandeJointDossierJointTiersViewBean.LABEL_DROITS_TOUS) });
            }
        }

        return statutsDemande;
    }

    public String getDateDecisionOAI() {
        return dateDecisionOAI;
    }

    public String getDateDecompte() {
        return dateDecompte;
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

    public String getDateReceptionPreavis() {
        return dateReceptionPreavis;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailAssure() throws Exception {

        if (!JadeStringUtil.isEmpty(getDateDeces())) {
            return PRNSSUtil.formatDetailRequerantListeDecede(getNss(), getNom() + " " + getPrenom(),
                    getDateNaissance(), getLibelleCourtSexe(), getLibellePays(), getDateDeces());
        } else {
            return PRNSSUtil.formatDetailRequerantListe(getNss(), getNom() + " " + getPrenom(), getDateNaissance(),
                    getLibelleCourtSexe(), getLibellePays());
        }
    }

    public String getDetailGestionnaire() throws FWSecurityLoginException, Exception {

        if (JadeStringUtil.isEmpty(getIdGestionnaire())) {
            return "";
        } else {
            JadeUser userName = getSession().getApplication()._getSecurityManager()
                    .getUserForVisa(getSession(), getIdGestionnaire());
            return userName.getIdUser() + " - " + userName.getFirstname() + " " + userName.getLastname();
        }
    }

    public Vector<String[]> getEtatsDemande() {
        return etatsDemande;
    }

    public String getGestionnaire() throws FWSecurityLoginException, Exception {

        return getVisaGestionnaire();

        /*
         * if(JadeStringUtil.isEmpty(getIdGestionnaire())){ return ""; }else{ JadeUser userName =
         * getSession().getApplication()._getSecurityManager( ).getUserForVisa(getSession(), getIdGestionnaire());
         * return userName.getIdUser(); }
         */
    }

    public String getImageError() {
        return "/images/erreur.gif";
    }

    public String getImageSuccess() {
        return "/images/ok.gif";
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe() {

        if (PRACORConst.CS_HOMME.equals(getCsSexe())) {
            return "H";// getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(getCsSexe())) {
            return "F";// getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays() {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", getCsNationalite())))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", getCsNationalite()));
        }

    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public String getMontantAcceptation() {
        return montantAcceptation;
    }

    public String getMontantDecompte() {
        return montantDecompte;
    }

    public String getMontantFacture44() {
        return montantFacture44;
    }

    public String getMontantVerseOAI() {
        return montantVerseOAI;
    }

    public String getNombreHeure() {
        return nombreHeure;
    }

    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     * 
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNss(), this.isNNSS().equals(Boolean.TRUE.toString()) ? true : false);
    }

    public String getNumeroDecision() {
        return numeroDecision;
    }

    public String getNumeroDecompte() {
        return numeroDecompte;
    }

    public Vector<String[]> getOrderBy() {
        return orderBy;
    }

    /**
     * getter pour l'attribut order by data
     * 
     * @return la valeur courante de l'attribut order by data
     */
    public Vector<String[]> getOrderByData() {
        if (orderBy == null) {
            orderBy = new Vector<String[]>(2);

            orderBy.add(new String[] {
                    RFDemande.FIELDNAME_DATE_FACTURE + " DESC ," + RFDemandeJointDossierJointTiers.FIELDNAME_NOM + ","
                            + RFDemandeJointDossierJointTiers.FIELDNAME_PRENOM,
                    getSession().getLabel("JSP_RF_DOS_R_TRIER_PAR_DATE_FACTURE") });

            orderBy.add(new String[] {
                    RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT + " DESC ," + RFDemande.FIELDNAME_DATE_FACTURE + ","
                            + RFDemandeJointDossierJointTiers.FIELDNAME_NOM + ","
                            + RFDemandeJointDossierJointTiers.FIELDNAME_PRENOM,
                    getSession().getLabel("JSP_RF_DOS_R_TRIER_PAR_DATE_DEBUT_TRAITEMENT") });

            orderBy.add(new String[] {
                    RFDemandeJointDossierJointTiers.FIELDNAME_NOM + ","
                            + RFDemandeJointDossierJointTiers.FIELDNAME_PRENOM,
                    getSession().getLabel("JSP_RF_DOS_R_TRIER_PAR_NOM") });
        }

        return orderBy;
    }

    public String getProvenance() {
        return provenance;
    }

    /**
     * Méthode qui retourne un tableau javascript de sous type de soins à 2 dimension (code,CSlibelle)
     * 
     * @return String
     */
    public String getSousTypeDeSoinParTypeInnerJavascript() {

        if (JadeStringUtil.isBlank(sousTypeDeSoinParTypeInnerJavascript)) {
            try {
                sousTypeDeSoinParTypeInnerJavascript = RFSoinsListsBuilder.getInstance(getSession())
                        .getSousTypeDeSoinParTypeInnerJavascript();
            } catch (Exception e) {
                setMessage(e.getMessage());
                setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        return sousTypeDeSoinParTypeInnerJavascript;

    }

    @Override
    public BSpy getSpy() {
        return spy;
    }

    public Vector<String[]> getStatutsDemande() {
        return statutsDemande;
    }

    public Vector<String[]> getTiPays() throws Exception {
        return PRTiersHelper.getPays(getSession());
    }

    public Vector<String[]> getTypeDeSoinData() {
        try {
            if (typeDeSoinsDemande == null) {
                typeDeSoinsDemande = RFSoinsListsBuilder.getInstance(getSession()).getTypeDeSoinsDemande();
            }
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }

        return typeDeSoinsDemande;
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getNss())) {
            return "";
        }

        if (getNss().length() > 14) {
            return Boolean.TRUE.toString();
        } else {
            return Boolean.FALSE.toString();
        }
    }

    /**
     * Méthode qui retourne une string avec true si le NSS passé en paramètre est un NNSS, sinon false
     * 
     * @param noAvs
     * @return String (true ou false)
     */
    public String isNNSS(String noAvs) {

        if (JadeStringUtil.isBlankOrZero(noAvs)) {
            return "";
        }

        if (noAvs.length() > 14) {
            return Boolean.TRUE.toString();
        } else {
            return Boolean.FALSE.toString();
        }
    }

    public boolean isTrouveDansCI() {
        return trouveDansCI;
    }

    public boolean isTrouveDansTiers() {
        return trouveDansTiers;
    }

    @Override
    public void setCreationSpy(BSpy creationSpy) {
        this.creationSpy = creationSpy;
    }

    public void setCsGenreDeSoin(String csGenreDeSoin) {
        this.csGenreDeSoin = csGenreDeSoin;
    }

    public void setDateDecisionOAI(String dateDecisionOAI) {
        this.dateDecisionOAI = dateDecisionOAI;
    }

    public void setDateDecompte(String dateDecompte) {
        this.dateDecompte = dateDecompte;
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

    public void setDateReceptionPreavis(String dateReceptionPreavis) {
        this.dateReceptionPreavis = dateReceptionPreavis;
    }

    public void setEtatsDemande(Vector<String[]> etatsDemande) {
        this.etatsDemande = etatsDemande;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setMontantAcceptation(String montantAcceptation) {
        this.montantAcceptation = montantAcceptation;
    }

    public void setMontantDecompte(String montantDecompte) {
        this.montantDecompte = montantDecompte;
    }

    public void setMontantFacture44(String montantFacture44) {
        this.montantFacture44 = montantFacture44;
    }

    public void setMontantVerseOAI(String montantVerseOAI) {
        this.montantVerseOAI = montantVerseOAI;
    }

    public void setNombreHeure(String nombreHeure) {
        this.nombreHeure = nombreHeure;
    }

    public void setNumeroDecision(String numeroDecision) {
        this.numeroDecision = numeroDecision;
    }

    public void setNumeroDecompte(String numeroDecompte) {
        this.numeroDecompte = numeroDecompte;
    }

    public void setOrderBy(Vector<String[]> orderBy) {
        this.orderBy = orderBy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setProvenance(java .lang.String)
     */
    public void setProvenance(String string) {
        if (PRUtil.PROVENANCE_TIERS.equals(string)) {
            trouveDansCI = false;
            trouveDansTiers = true;
        } else if (PRUtil.PROVENANCE_CI.equals(string)) {
            trouveDansCI = true;
            trouveDansTiers = false;
        } else {
            trouveDansCI = false;
            trouveDansTiers = false;
        }
    }

    @Override
    public void setSpy(BSpy spy) {
        this.spy = spy;
    }

    public void setStatutsDemande(Vector<String[]> statutsDemande) {
        this.statutsDemande = statutsDemande;
    }

    public void setTrouveDansCI(boolean trouveDansCI) {
        this.trouveDansCI = trouveDansCI;
    }

    public void setTrouveDansTiers(boolean trouveDansTiers) {
        this.trouveDansTiers = trouveDansTiers;
    }

}