package globaz.apg.vb.prestation;

import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.prestation.APRepartitionJointPrestationManager;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @see globaz.apg.servlet.APRepartirLesMontantsViewBean
 * @author bsc
 */
public class APRepartirLesMontantsViewBean extends PRAbstractViewBeanSupport {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    APRepartitionPaiementsViewBean repartitionPaiements = null;
    APRepartitionJointPrestationManager repJointPres = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
	 * 
	 */
    public APRepartirLesMontantsViewBean(APRepartitionPaiementsViewBean repartitionPaiements) {
        super();
        this.repartitionPaiements = repartitionPaiements;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return
     */
    public String getAdresseFormattee() {
        return repartitionPaiements.getAdresseFormattee();
    }

    /**
     * @return
     */
    public String getCcpOuBanqueFormatte() {
        return repartitionPaiements.getCcpOuBanqueFormatte();
    }

    /**
     * @return
     */
    public String getCollection() {
        return repartitionPaiements.getCollection();
    }

    /**
     * @return
     */
    public String getDateDebutDroit() {
        return repartitionPaiements.getDateDebutDroit();
    }

    /**
     * @return
     */
    public String getDateDebutPrestation() {
        return repartitionPaiements.getDateDebutPrestation();
    }

    /**
     * @return
     */
    public String getDateFinPrestation() {
        return repartitionPaiements.getDateFinPrestation();
    }

    /**
     * @return
     */
    public String getDateValeur() {
        return repartitionPaiements.getDateValeur();
    }

    /**
     * @return
     */
    public String getDroitAcquis() {
        return repartitionPaiements.getDroitAcquis();
    }

    /**
     * @return
     */
    public StringBuffer getErrors() {
        return repartitionPaiements.getErrors();
    }

    /**
     * @return
     */
    public String getEtatPrestation() {
        return repartitionPaiements.getEtatPrestation();
    }

    /**
     * @return
     */
    public String getGenrePrestation() {
        return repartitionPaiements.getGenrePrestation();
    }

    /**
     * @return
     */
    public String getGenreService() {
        return repartitionPaiements.getGenreService();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#getId()
     */
    @Override
    public String getId() {
        return repartitionPaiements.getId();
    }

    /**
     * @return
     */
    public String getIdAffilie() {
        return repartitionPaiements.getIdAffilie();
    }

    /**
     * @return
     */
    public String getIdCompensation() {
        return repartitionPaiements.getIdCompensation();
    }

    /**
     * @return
     */
    public String getIdDomaineAdressePaiement() {
        return repartitionPaiements.getIdDomaineAdressePaiement();
    }

    /**
     * @return
     */
    public String getIdDroit() {
        return repartitionPaiements.getIdDroit();
    }

    /**
     * @return
     */
    public String getIdInscriptionCI() {
        return repartitionPaiements.getIdInscriptionCI();
    }

    /**
     * @return
     */
    public String getIdMajeur() {
        return repartitionPaiements.getIdMajeur();
    }

    /**
     * @return
     */
    public int getIdOfIdPrestationCourante() {
        return repartitionPaiements.getIdOfIdPrestationCourante();
    }

    /**
     * @return
     */
    public String getIdParent() {
        return repartitionPaiements.getIdParent();
    }

    /**
     * @return
     */
    public String getIdPrestationApg() {
        return repartitionPaiements.getIdPrestationApg();
    }

    /**
     * @return
     */
    public String getIdPrestationCourante() {
        return repartitionPaiements.getIdPrestationCourante();
    }

    /**
     * @return
     */
    public String getIdRepartitionBeneficiairePaiement() {
        return repartitionPaiements.getIdRepartitionBeneficiairePaiement();
    }

    /**
     * @return
     */
    public String getIdSituationProfessionnelle() {
        return repartitionPaiements.getIdSituationProfessionnelle();
    }

    /**
     * @return
     */
    public List getIdsPrestations() {
        return repartitionPaiements.getIdsPrestations();
    }

    /**
     * @return
     */
    public String getIdTiers() {
        return repartitionPaiements.getIdTiers();
    }

    /**
     * @return
     */
    public String getIdTiersAdressePaiement() {
        return repartitionPaiements.getIdTiersAdressePaiement();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#getISession()
     */
    @Override
    public BISession getISession() {
        return repartitionPaiements.getISession();
    }

    /**
     * @return
     */
    public String getLastModifiedDate() {
        return repartitionPaiements.getLastModifiedDate();
    }

    /**
     * @return
     */
    public String getLastModifiedTime() {
        return repartitionPaiements.getLastModifiedTime();
    }

    /**
     * @return
     */
    public String getLastModifiedUser() {
        return repartitionPaiements.getLastModifiedUser();
    }

    /**
     * @return
     */
    public String getLibelleGenrePrestation() {
        return repartitionPaiements.getLibelleGenrePrestation();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#getMessage()
     */
    @Override
    public String getMessage() {
        return repartitionPaiements.getMessage();
    }

    /**
     * @return
     */
    public Object[] getMethodesSelectionAdresse() {
        return repartitionPaiements.getMethodesSelectionAdresse();
    }

    /**
     * @return
     */
    public Object[] getMethodesSelectionBeneficiaire() {
        return repartitionPaiements.getMethodesSelectionBeneficiaire();
    }

    /**
     * @return
     */
    public String[] getMethodsToLoad() {
        return repartitionPaiements.getMethodsToLoad();
    }

    /**
     * @return
     */
    public String getMontantBrut() {
        return repartitionPaiements.getMontantBrut();
    }

    /**
     * @return
     */
    public String getMontantBrutPrestation() {
        return repartitionPaiements.getMontantBrutPrestation();
    }

    /**
     * @return
     */
    public String getMontantCotisations() {
        return repartitionPaiements.getMontantCotisations();
    }

    /**
     * @return
     */
    public String getMontantNet() {
        return repartitionPaiements.getMontantNet();
    }

    /**
     * @return
     */
    public String getMontantRestant() {
        return repartitionPaiements.getMontantRestant();
    }

    /**
     * @return
     * @throws Exception
     */
    public String getMontantTotalAllocExploitation() throws Exception {
        return repartitionPaiements.getMontantTotalAllocExploitation();
    }

    /**
     * @return
     */
    public String getMontantVentile() {
        return repartitionPaiements.getMontantVentile();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#getMsgType()
     */
    @Override
    public String getMsgType() {
        return repartitionPaiements.getMsgType();
    }

    /**
     * @return
     */
    public String getNbJoursPrestation() {
        return repartitionPaiements.getNbJoursPrestation();
    }

    /**
     * @return
     */
    public String getNoAVSAssure() {
        return repartitionPaiements.getNoAVSAssure();
    }

    /**
     * @return
     */
    public String getNom() {
        return repartitionPaiements.getNom();
    }

    /**
     * @return
     */
    public String getNomPrenomAssure() {
        return repartitionPaiements.getNomPrenomAssure();
    }

    /**
     * @return
     */
    public String getReferenceInterne() {
        return repartitionPaiements.getReferenceInterne();
    }

    /**
     * 
     * @return
     */
    public APRepartitionPaiementsViewBean getRepartitionPaiements() {
        return repartitionPaiements;
    }

    /**
     * Donne un iterateur sur les repartitions d'une prestation
     */
    public Iterator getRepartitions() {

        if (repJointPres == null) {
            repJointPres = new APRepartitionJointPrestationManager();
            repJointPres.setSession(getSession());
            repJointPres.setForIdPrestation(repartitionPaiements.getIdPrestationCourante());
            repJointPres.setParentOnly(true);
            repJointPres.setForTypePrestation(IAPRepartitionPaiements.CS_NORMAL);
            try {
                repJointPres.find();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return repJointPres.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#getSession()
     */
    @Override
    public BSession getSession() {
        return repartitionPaiements.getSession();
    }

    /**
     * @return
     */
    public APSituationProfessionnelle getSituatuionPro() {
        return repartitionPaiements.getSituatuionPro();
    }

    /**
     * @return
     */
    @Override
    public BSpy getSpy() {
        return repartitionPaiements.getSpy();
    }

    /**
     * @return
     */
    public String getTauxPrestation() {
        return repartitionPaiements.getTauxPrestation();
    }

    /**
     * @return
     */
    public String getTauxRJM() {
        return repartitionPaiements.getTauxRJM();
    }

    /**
     * @return
     */
    public String getTypePaiement() {
        return repartitionPaiements.getTypePaiement();
    }

    /**
     * @return
     */
    public String getTypePrestation() {
        return repartitionPaiements.getTypePrestation();
    }

    /**
     * @return
     */
    public boolean isRetourDepuisPyxis() {
        return repartitionPaiements.isRetourDepuisPyxis();
    }

    /**
     * @return
     */
    public boolean isTiersBeneficiaireChange() {
        return repartitionPaiements.isTiersBeneficiaireChange();
    }

    /**
     * @return
     */
    public boolean isTiersSet() {
        return repartitionPaiements.isTiersSet();
    }

    /**
     * @return
     */
    public boolean isValidee() {
        return repartitionPaiements.isValidee();
    }

    /**
     * @return
     */
    public boolean isVentilation() {
        return repartitionPaiements.isVentilation();
    }

    /**
     * @param dateValeurCompta
     * @return
     * @throws Exception
     */
    public TIAdressePaiementData loadAdressePaiement(String dateValeurCompta) throws Exception {
        return repartitionPaiements.loadAdressePaiement(dateValeurCompta);
    }

    /**
     * @return
     * @throws Exception
     */
    public APSituationProfessionnelle loadSituationProfessionnelle() throws Exception {
        return repartitionPaiements.loadSituationProfessionnelle();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {
        return true;
    }
}
