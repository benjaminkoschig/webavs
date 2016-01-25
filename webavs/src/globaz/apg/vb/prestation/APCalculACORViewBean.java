package globaz.apg.vb.prestation;

import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.utils.APGUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.tools.PRImagesConstants;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APCalculACORViewBean extends PRAbstractViewBeanSupport implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public static final int CALCULER_PREST_ACOR = 20;
    // pour savoir si ACOR est appele lors du calcul de prestations standard ou
    // de toutes les prestations
    public static final int CALCULER_TOUTES_PREST_ACOR = 10;
    private String annoncePay = "";
    private int appelant = -1;

    private APDroitLAPG droit;
    private transient Map filesContent;
    private String genreService = "";

    private String idDroit = "";
    private boolean isFileContent = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut annonce pay
     * 
     * @return la valeur courante de l'attribut annonce pay
     */
    public String getAnnoncePay() {
        return annoncePay;
    }

    /**
     * @return
     */
    public int getAppelant() {
        return appelant;
    }

    /**
     * getter pour l'attribut chemin annonce pay
     * 
     * @return la valeur courante de l'attribut chemin annonce pay
     */
    public String getCheminAnnoncePay() {
        return getDossierACOR() + PRACORConst.DOSSIER_DEM_COUR + PRACORConst.NF_ANNONCE_PAY;
    }

    /**
     * getter pour l'attribut chemin annonce RR
     * 
     * @return la valeur courante de l'attribut chemin annonce RR
     */
    public String getCheminAnnonceRR() {
        return getDossierACOR() + PRACORConst.DOSSIER_DEM_COUR + PRACORConst.NF_ANNONCE_RR;
    }

    /**
     * getter pour l'attribut chemin image active x
     * 
     * @return la valeur courante de l'attribut chemin image active x
     */
    public String getCheminImageActiveX() {
        // TODO: faire une image en allemand pour le boîte de dialogue d'erreur
        // de ActiveX
        return "/" + APApplication.APPLICATION_APG_REP + PRImagesConstants.IMAGE_ACTIVEX_FR;
    }

    /**
     * getter pour l'attribut chemin image bouton calculer
     * 
     * @return la valeur courante de l'attribut chemin image bouton calculer
     */
    public String getCheminImageBoutonCalculer() {
        return "/" + APApplication.APPLICATION_APG_REP + PRImagesConstants.IMAGE_BOUTON_CALCULER;
    }

    /**
     * getter pour l'attribut chemin image bouton host
     * 
     * @return la valeur courante de l'attribut chemin image bouton host
     */
    public String getCheminImageBoutonHost() {
        return "/" + APApplication.APPLICATION_APG_REP + PRImagesConstants.IMAGE_BOUTON_HOST;
    }

    /**
     * getter pour l'attribut chemin image active x
     * 
     * @return la valeur courante de l'attribut chemin image active x
     */
    public String getCheminImageExecuter() {
        // TODO: faire une image en allemand pour le boîte de dialogue d'erreur
        // executer
        return "/" + APApplication.APPLICATION_APG_REP + PRImagesConstants.IMAGE_EXECUTER_FR;
    }

    /**
     * getter pour l'attribut dossier ACOR
     * 
     * @return la valeur courante de l'attribut dossier ACOR
     */
    public String getDossierACOR() {
        try {
            return PRACORConst.dossierACOR(getSession());
        } catch (PRACORException e) {
            e.printStackTrace();

            return "";
        }
    }

    public String getDossierAcorInHost(BSession session) {

        String dossierInHost;
        try {
            dossierInHost = globaz.prestation.acor.PRACORConst.dossierACOR(session);
            dossierInHost += globaz.prestation.acor.PRACORConst.DOSSIER_IN_HOST;
            return dossierInHost;
        } catch (PRACORException e) {
            return "";
        }

    }

    public Map getFilesContent() {
        return filesContent;
    }

    /**
     * getter pour l'attribut genre service
     * 
     * @return la valeur courante de l'attribut genre service
     */
    public String getGenreService() {
        return genreService;
    }

    /**
     * getter pour l'attribut id droit
     * 
     * @return la valeur courante de l'attribut id droit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * getter pour l'attribut libelle bouton validate
     * 
     * @return la valeur courante de l'attribut libelle bouton validate
     */
    public String getLibelleBoutonValidate() {
        return getSession().getLabel("JSP_IMPORTER_ACOR");
    }

    /**
     * getter pour l'attribut spy
     * 
     * @return la valeur courante de l'attribut spy
     */
    @Override
    public BSpy getSpy() {
        return new BSpy("");
    }

    public String getStartAcorCmd(BSession session) {
        try {
            return PRACORConst.dossierACOR(session) + PRACORConst.EXECUTABLE_ACOR;
        } catch (PRACORException e) {
            e.printStackTrace();
            // Ne devrait jamais arriver !!!
            return "C:\\Acor\\" + PRACORConst.EXECUTABLE_ACOR + "\"";
        }
    }

    public boolean isFileContent() {
        return isFileContent;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public APDroitLAPG loadDroit() throws Exception {
        if ((droit == null) || !droit.getIdDroit().equals(idDroit)) {
            droit = APGUtils.loadDroit(getSession(), idDroit, genreService);
        }

        return droit;
    }

    /**
     * setter pour l'attribut annonce pay
     * 
     * @param annoncePay
     *            une nouvelle valeur pour cet attribut
     */
    public void setAnnoncePay(String annoncePay) {
        this.annoncePay = annoncePay;
    }

    /**
     * @param i
     */
    public void setAppelant(int i) {
        appelant = i;
    }

    public void setFilesContent(Map filesContent) {
        this.filesContent = filesContent;
    }

    /**
     * setter pour l'attribut genre service
     * 
     * @param genreService
     *            une nouvelle valeur pour cet attribut
     */
    public void setGenreService(String genreService) {
        this.genreService = genreService;
    }

    /**
     * setter pour l'attribut id droit
     * 
     * @param idDroit
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    /**
     * setter pour l'attribut ISession
     * 
     * @param newSession
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setISession(BISession newSession) {
        super.setISession(newSession);

        if (droit != null) {
            droit.setISession(newSession);
        }
    }

    public void setIsFileContent(boolean isFileContent) {
        this.isFileContent = isFileContent;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean validate() {
        return false;
    }

}
