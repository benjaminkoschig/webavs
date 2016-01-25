/*
 * Créé le 13 sept. 05
 */
package globaz.ij.vb.prononces;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJRevenu;
import globaz.ij.db.prononces.IJSituationFamiliale;
import globaz.ij.regles.IJPrononceRegles;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public abstract class IJAbstractPrononceProxyViewBean implements FWViewBeanInterface, BIPersistentObject {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    public static final String CS_ADMIN_GENRE_OFFICE_AI = "509004";

    private String anneeNiveauRevenuReadaptation = "";

    /** pour pouvoir proposer l'office AI correspondant */
    private String csCantonDomicile = "";
    private String csPeriodiciteRevenuReadaptation = "";
    private String heuresSemaineRevenuReadaptation = "";
    private boolean modifie = false;

    private String montantRevenuReadaptation = "";

    private String noAVS = "";
    private String prenomNom = "";
    private IJPrononce prononce;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APAbstractDroitProxyViewBean.
     * 
     * @param prononce
     *            DOCUMENT ME!
     */
    protected IJAbstractPrononceProxyViewBean(IJPrononce prononce) {
        this.prononce = prononce;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc).
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        prononce.add();
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void add(BITransaction transaction) throws Exception {
        prononce.add(transaction);
    }

    /**
     * (non-Javadoc).
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        prononce.delete();
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void delete(BITransaction transaction) throws Exception {
        prononce.delete(transaction);
    }

    /**
     * getter pour l'attribut annee niveau revenu readaptation.
     * 
     * @return la valeur courante de l'attribut annee niveau revenu readaptation
     */
    public String getAnneeNiveauRevenuReadaptation() {
        return anneeNiveauRevenuReadaptation;
    }

    public String getAnneeRenteEnCours() {
        return prononce.getAnneeRenteEnCours();
    }

    public Boolean getAvecDecision() {
        return prononce.getAvecDecision();
    }

    /**
     * @return
     */
    public String getCodesCasSpecial() {
        return prononce.getCodesCasSpecial();
    }

    /**
     * getter pour l'attribut cs canton.
     * 
     * @return la valeur courante de l'attribut cs canton
     */
    public String getCsCantonDomicile() {
        if (JadeStringUtil.isEmpty(csCantonDomicile)) {
            csCantonDomicile = getProprieteTiers(PRTiersWrapper.PROPERTY_ID_CANTON);
            if (JadeStringUtil.isEmpty(csCantonDomicile)) {
                try {
                    csCantonDomicile = CaisseHelperFactory.getInstance().getCsDefaultCantonCaisse(
                            getSession().getApplication());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return csCantonDomicile;
    }

    /**
     * getter pour l'attribut cs etat.
     * 
     * @return la valeur courante de l'attribut cs etat
     */
    public String getCsEtat() {
        return prononce.getCsEtat();
    }

    /**
     * getter pour l'attribut cs genre.
     * 
     * @return la valeur courante de l'attribut cs genre
     */
    public String getCsGenre() {
        return prononce.getCsGenre();
    }

    /**
     * getter pour l'attribut cs periodicite revenu readaptation.
     * 
     * @return la valeur courante de l'attribut cs periodicite revenu readaptation
     */
    public String getCsPeriodiciteRevenuReadaptation() {
        return csPeriodiciteRevenuReadaptation;
    }

    /**
     * getter pour l'attribut cs statut professionnel.
     * 
     * @return la valeur courante de l'attribut cs statut professionnel
     */
    public String getCsStatutProfessionnel() {
        return prononce.getCsStatutProfessionnel();
    }

    /**
     * @return
     */
    public String getCsTypeHebergement() {
        return prononce.getCsTypeHebergement();
    }

    /**
     * getter pour l'attribut cs type IJ.
     * 
     * @return la valeur courante de l'attribut cs type IJ
     */
    public String getCsTypeIJ() {
        return prononce.getCsTypeIJ();
    }

    /**
     * getter pour l'attribut date debut prononce.
     * 
     * @return la valeur courante de l'attribut date debut prononce
     */
    public String getDateDebutPrononce() {
        return prononce.getDateDebutPrononce();
    }

    /**
     * getter pour l'attribut date fin prononce.
     * 
     * @return la valeur courante de l'attribut date fin prononce
     */
    public String getDateFinPrononce() {
        return prononce.getDateFinPrononce();
    }

    /**
     * getter pour l'attribut date prononce.
     * 
     * @return la valeur courante de l'attribut date prononce
     */
    public String getDatePrononce() {
        return prononce.getDatePrononce();
    }

    /**
     * getter pour l'attribut demi IJAC.
     * 
     * @return la valeur courante de l'attribut demi IJAC
     */
    public String getDemiIJAC() {
        return prononce.getDemiIJAC();
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVS());

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession()
                    .getCode(
                            getSession().getSystemCode("CIPAYORI",
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode("CIPAYORI",
                                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            return PRNSSUtil.formatDetailRequerantDetail(getNoAVS(), tiers.getProperty(PRTiersWrapper.PROPERTY_NOM)
                    + " " + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getEchelle() {
        return prononce.getEchelle();
    }

    /**
     * getter pour l'attribut heures semaine revenu readaptation.
     * 
     * @return la valeur courante de l'attribut heures semaine revenu readaptation
     */
    public String getHeuresSemaineRevenuReadaptation() {
        return heuresSemaineRevenuReadaptation;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut id
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return prononce.getId();
    }

    /**
     * getter pour l'attribut id demande.
     * 
     * @return la valeur courante de l'attribut id demande
     */
    public String getIdDemande() {
        return prononce.getIdDemande();
    }

    /**
     * getter pour l'attribut id gestionnaire.
     * 
     * @return la valeur courante de l'attribut id gestionnaire
     */
    public String getIdGestionnaire() {
        return prononce.getIdGestionnaire();
    }

    /**
     * getter pour l'attribut id parent.
     * 
     * @return la valeur courante de l'attribut id parent
     */
    public String getIdParent() {
        return prononce.getIdParent();
    }

    /**
     * getter pour l'attribut id prononce.
     * 
     * @return la valeur courante de l'attribut id prononce
     */
    public String getIdPrononce() {
        return prononce.getIdPrononce();
    }

    /**
     * getter pour l'attribut id revenu readaptation.
     * 
     * @return la valeur courante de l'attribut id revenu readaptation
     */
    public String getIdRevenuReadaptation() {
        return prononce.getIdRevenuReadaptation();
    }

    /**
     * getter pour l'attribut id situation familiale.
     * 
     * @return la valeur courante de l'attribut id situation familiale
     */
    public String getIdSituationFamiliale() {
        return prononce.getIdSituationFamiliale();
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut ISession
     * 
     * @see globaz.framework.bean.FWViewBeanInterface#getISession()
     */
    @Override
    public BISession getISession() {
        return prononce.getISession();
    }

    /**
     * Retourne la liste des codes systèmes pour l'état du droit augmentée des champs 'tous' et 'non définitif'.
     * 
     * @return un Vector de String[2]{noOfficeAI, nomOfficeAI}.
     */
    public Vector getListeOfficeAI() {
        Vector result = new Vector();
        PRTiersWrapper[] officesAI = null;

        try {
            officesAI = PRTiersHelper.getAdministrationForGenre(getSession(),
                    IJAbstractPrononceProxyViewBean.CS_ADMIN_GENRE_OFFICE_AI);
        } catch (Exception e) {
            // erreur dans la recherche dews offoce AI
            e.printStackTrace();
        }

        if (officesAI != null) {
            for (int i = 0; i < officesAI.length; i++) {
                result.add(
                        0,
                        new String[] {
                                officesAI[i].getProperty(PRTiersWrapper.PROPERTY_CODE_ADMINISTRATION),
                                officesAI[i].getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                        + officesAI[i].getProperty(PRTiersWrapper.PROPERTY_PRENOM) });

            }
        }

        return result;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut message
     * 
     * @see globaz.framework.bean.FWViewBeanInterface#getMessage()
     */
    @Override
    public String getMessage() {
        return prononce.getMessage();
    }

    /**
     * getter pour l'attribut montant garanti AA.
     * 
     * @return la valeur courante de l'attribut montant garanti AA
     */
    public String getMontantGarantiAA() {
        return prononce.getMontantGarantiAA();
    }

    /**
     * getter pour l'attribut montant garanti AAReduit.
     * 
     * @return la valeur courante de l'attribut montant garanti AAReduit
     */
    public Boolean getMontantGarantiAAReduit() {
        return prononce.getMontantGarantiAAReduit();
    }

    /**
     * @return
     */
    public String getMontantRenteEnCours() {
        return prononce.getMontantRenteEnCours();
    }

    /**
     * getter pour l'attribut montant revenu readaptation.
     * 
     * @return la valeur courante de l'attribut montant revenu readaptation
     */
    public String getMontantRevenuReadaptation() {
        return montantRevenuReadaptation;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut msg type
     * 
     * @see globaz.framework.bean.FWViewBeanInterface#getMsgType()
     */
    @Override
    public String getMsgType() {
        return prononce.getMsgType();
    }

    /**
     * getter pour l'attribut no AVS.
     * 
     * @return la valeur courante de l'attribut no AVS
     */
    public String getNoAVS() {
        return noAVS;
    }

    public String getNoDecisionAI() {
        return prononce.getNoDecisionAI();
    }

    /**
     * Retourne le no de l'office AI du canton de dimicile de l'assuré
     * 
     * @return
     */
    public String getNoOfficeAICantonal() {
        String result = "";

        PRTiersWrapper[] officesAI = null;

        try {
            officesAI = PRTiersHelper.getAdministrationForGenre(getSession(),
                    IJAbstractPrononceProxyViewBean.CS_ADMIN_GENRE_OFFICE_AI);
        } catch (Exception e) {
            // erreur dans la recherche dews offoce AI
            e.printStackTrace();
        }

        if (officesAI != null) {
            for (int i = 0; i < officesAI.length; i++) {
                if (officesAI[i].getProperty(PRTiersWrapper.PROPERTY_ID_CANTON).equalsIgnoreCase(getCsCantonDomicile())) {
                    return officesAI[i].getProperty(PRTiersWrapper.PROPERTY_CODE_ADMINISTRATION);
                }

            }
        }

        return result;
    }

    /**
     * getter pour l'attribut cs office AI.
     * 
     * @return la valeur courante de l'attribut cs office AI
     */
    public String getOfficeAI() {
        return prononce.getOfficeAI();
    }

    /**
     * getter pour l'attribut nom prenom.
     * 
     * @return la valeur courante de l'attribut nom prenom
     */
    public String getPrenomNom() {
        return prenomNom;
    }

    /**
     * getter pour l'attribut prononce.
     * 
     * @return la valeur courante de l'attribut prononce
     */
    public IJPrononce getPrononce() {
        return prononce;
    }

    /**
     * getter pour l'attribut propriete du tiers lié à la demande liée à ce droit.
     * 
     * @param nom
     *            le nom d'une propriété (PRCiWrapper)
     * 
     * @return la valeur courante de l'attribut propriete tiers ou chaine vide
     */
    private String getProprieteTiers(String nom) {
        String retValue = "";

        if (!JadeStringUtil.isIntegerEmpty(getIdDemande())) {
            try {
                PRDemande demande = loadDemande();

                if (!JadeStringUtil.isIntegerEmpty(demande.getIdTiers())) {
                    retValue = demande.loadTiers().getProperty(nom);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // pas pu charger la demande ou le tiers
            }
        }

        return retValue;
    }

    /**
     * @return
     */
    public String getRam() {
        return prononce.getRam();
    }

    /**
     * getter pour l'attribut session.
     * 
     * @return la valeur courante de l'attribut session
     */
    public BSession getSession() {
        return prononce.getSession();
    }

    /**
     * getter pour l'attribut spy.
     * 
     * @return la valeur courante de l'attribut spy
     */
    public BSpy getSpy() {
        return prononce.getSpy();
    }

    /**
     * @return DOCUMENT ME!
     */
    public boolean hasSpy() {
        return prononce.hasSpy();
    }

    /**
     * retourne vrai si cette ij est une grande ij (type grande_ij).
     * 
     * @return la valeur courante de l'attribut grande
     */
    public boolean isGrandeIJ() {
        return prononce.isGrandeIJ();
    }

    /**
     * getter pour l'attribut modifie.
     * 
     * @return la valeur courante de l'attribut modifie
     */
    public boolean isModifie() {
        return modifie;
    }

    /**
     * getter pour l'attribut modifier permis.
     * 
     * @return la valeur courante de l'attribut modifier permis
     */
    public boolean isModifierPermis() {
        return IJPrononceRegles.isModifierPermis(getPrononce());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public PRDemande loadDemande() throws Exception {
        return prononce.loadDemande(null);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJRevenu loadRevenuReadaptation() throws Exception {
        if (prononce.loadRevenuReadaptation(null) != null) {
            return prononce.loadRevenuReadaptation(null);
        } else {
            return new IJRevenu();
        }
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJSituationFamiliale loadSituationFamiliale() throws Exception {
        return prononce.loadSituationFamiliale(null);
    }

    /**
     * (non-Javadoc).
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        prononce.retrieve();
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void retrieve(BITransaction transaction) throws Exception {
        prononce.retrieve(transaction);
    }

    /**
     * setter pour l'attribut annee niveau revenu readaptation.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setAnneeNiveauRevenuReadaptation(String string) {
        anneeNiveauRevenuReadaptation = string;
    }

    public void setAnneeRenteEnCours(String anneeRenteEnCours) {
        prononce.setAnneeRenteEnCours(anneeRenteEnCours);
    }

    public void setAvecDecision(Boolean avecDecision) {
        prononce.setAvecDecision(avecDecision);
    }

    /**
     * @param string
     */
    public void setCodesCasSpecial(String string) {
        prononce.setCodesCasSpecial(string);
    }

    /**
     * setter pour l'attribut cs etat.
     * 
     * @param csEtat
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsEtat(String csEtat) {
        prononce.setCsEtat(csEtat);
    }

    /**
     * setter pour l'attribut cs genre.
     * 
     * @param csGenre
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsGenre(String csGenre) {
        prononce.setCsGenre(csGenre);
    }

    /**
     * setter pour l'attribut cs periodicite revenu readaptation.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsPeriodiciteRevenuReadaptation(String string) {
        csPeriodiciteRevenuReadaptation = string;
    }

    /**
     * setter pour l'attribut cs statut professionnel.
     * 
     * @param csStatutProfessionnel
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsStatutProfessionnel(String csStatutProfessionnel) {
        prononce.setCsStatutProfessionnel(csStatutProfessionnel);
    }

    /**
     * @param string
     */
    public void setCsTypeHebergement(String string) {
        prononce.setCsTypeHebergement(string);
    }

    /**
     * setter pour l'attribut cs type IJ.
     * 
     * @param csTypeIJ
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeIJ(String csTypeIJ) {
        prononce.setCsTypeIJ(csTypeIJ);
    }

    /**
     * setter pour l'attribut date debut prononce.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutPrononce(String string) {
        prononce.setDateDebutPrononce(string);
    }

    /**
     * setter pour l'attribut date fin prononce.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinPrononce(String string) {
        prononce.setDateFinPrononce(string);
    }

    /**
     * setter pour l'attribut date prononce.
     * 
     * @param datePrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setDatePrononce(String datePrononce) {
        prononce.setDatePrononce(datePrononce);
    }

    /**
     * setter pour l'attribut demande.
     * 
     * @param demande
     *            une nouvelle valeur pour cet attribut
     */
    public void setDemande(PRDemande demande) {
        prononce.setDemande(demande);
    }

    /**
     * setter pour l'attribut demi IJAC.
     * 
     * @param demiIJAC
     *            une nouvelle valeur pour cet attribut
     */
    public void setDemiIJAC(String demiIJAC) {
        prononce.setDemiIJAC(demiIJAC);
    }

    /**
     * @param string
     */
    public void setEchelle(String string) {
        prononce.setEchelle(string);
    }

    /**
     * setter pour l'attribut heures semaine revenu readaptation.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setHeuresSemaineRevenuReadaptation(String string) {
        heuresSemaineRevenuReadaptation = string;
    }

    /**
     * (non-Javadoc).
     * 
     * @param newId
     *            une nouvelle valeur pour cet attribut
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        prononce.setId(newId);
    }

    /**
     * setter pour l'attribut id demande.
     * 
     * @param idDemande
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDemande(String idDemande) {
        prononce.setIdDemande(idDemande);
    }

    /**
     * setter pour l'attribut id gestionnaire.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdGestionnaire(String string) {
        prononce.setIdGestionnaire(string);
    }

    /**
     * setter pour l'attribut id parent.
     * 
     * @param idParent
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdParent(String idParent) {
        prononce.setIdParent(idParent);
    }

    /**
     * setter pour l'attribut id prononce.
     * 
     * @param idPrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrononce(String idPrononce) {
        prononce.setIdPrononce(idPrononce);
    }

    /**
     * setter pour l'attribut id revenu readaptation.
     * 
     * @param idRevenuReadaptation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRevenuReadaptation(String idRevenuReadaptation) {
        prononce.setIdRevenuReadaptation(idRevenuReadaptation);
    }

    /**
     * setter pour l'attribut id situation familiale.
     * 
     * @param idSituationFamiliale
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSituationFamiliale(String idSituationFamiliale) {
        prononce.setIdSituationFamiliale(idSituationFamiliale);
    }

    /**
     * (non-Javadoc).
     * 
     * @param newSession
     *            une nouvelle valeur pour cet attribut
     * 
     * @see globaz.framework.bean.FWViewBeanInterface#setISession(globaz.globall.api.BISession)
     */
    @Override
    public void setISession(BISession newSession) {
        prononce.setISession(newSession);
    }

    /**
     * (non-Javadoc).
     * 
     * @param message
     *            une nouvelle valeur pour cet attribut
     * 
     * @see globaz.framework.bean.FWViewBeanInterface#setMessage(java.lang.String)
     */
    @Override
    public void setMessage(String message) {
        prononce.setMessage(message);
    }

    /**
     * setter pour l'attribut modifie.
     * 
     * @param modifie
     *            une nouvelle valeur pour cet attribut
     */
    public void setModifie(boolean modifie) {
        this.modifie = modifie;
    }

    /**
     * setter pour l'attribut montant garanti AA.
     * 
     * @param montantGarantiAA
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantGarantiAA(String montantGarantiAA) {
        prononce.setMontantGarantiAA(montantGarantiAA);
    }

    /**
     * setter pour l'attribut montant garanti AAReduit.
     * 
     * @param montantGarantiAAReduit
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantGarantiAAReduit(Boolean montantGarantiAAReduit) {
        prononce.setMontantGarantiAAReduit(montantGarantiAAReduit);
    }

    /**
     * @param string
     */
    public void setMontantRenteEnCours(String string) {
        prononce.setMontantRenteEnCours(string);
    }

    /**
     * setter pour l'attribut montant revenu readaptation.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantRevenuReadaptation(String string) {
        montantRevenuReadaptation = string;
    }

    /**
     * (non-Javadoc).
     * 
     * @param msgType
     *            une nouvelle valeur pour cet attribut
     * 
     * @see globaz.framework.bean.FWViewBeanInterface#setMsgType(java.lang.String)
     */
    @Override
    public void setMsgType(String msgType) {
        prononce.setMsgType(msgType);
    }

    /**
     * setter pour l'attribut no AVS.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAVS(String string) {
        noAVS = string;
    }

    public void setNoDecisionAI(String noDecisionAI) {
        prononce.setNoDecisionAI(noDecisionAI);
    }

    /**
     * setter pour l'attribut cs office AI.
     * 
     * @param csOfficeAI
     *            une nouvelle valeur pour cet attribut
     */
    public void setOfficeAI(String officeAI) {
        prononce.setOfficeAI(officeAI);
    }

    /**
     * setter pour l'attribut nom prenom.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPrenomNom(String string) {
        prenomNom = string;
    }

    /**
     * @param string
     */
    public void setRam(String string) {
        prononce.setRam(string);
    }

    /**
     * setter pour l'attribut session.
     * 
     * @param newSession
     *            une nouvelle valeur pour cet attribut
     */
    public void setSession(BSession newSession) {
        prononce.setSession(newSession);
    }

    /**
     * (non-Javadoc).
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        prononce.update();
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void update(BITransaction transaction) throws Exception {
        prononce.update(transaction);
    }

    /**
     * @return DOCUMENT ME!
     */
    public boolean validate() {
        return true;
    }

    /**
     * @param newCallValidate
     *            DOCUMENT ME!
     */
    public void wantCallValidate(boolean newCallValidate) {
        prononce.wantCallValidate(newCallValidate);
    }

}
