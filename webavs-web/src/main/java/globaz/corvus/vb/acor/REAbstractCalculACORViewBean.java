package globaz.corvus.vb.acor;

import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.util.Slashs;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

/**
 * <H1>Description</H1>
 *
 * @author scr
 */
public abstract class REAbstractCalculACORViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private static final Logger LOG = LoggerFactory.getLogger(REAbstractCalculACORViewBean.class);

    private boolean calculable;
    private String contenuAnnoncePay;

    private String contenuAnnonceRR;
    private String contenuAnnonceXML;

    private String contenuFeuilleCalculXML;
    private transient Map filesContent;
    private String noAVSAssure;

    private String nomPrenomAssure;

    /**
     * @deprecated replaced by filesContent
     */
    @Deprecated
    private transient PrintWriter writer;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut chemin annonce pay
     *
     * @return la valeur courante de l'attribut chemin annonce pay
     */
    public String getCheminAnnoncePay() throws PRACORException, Exception {
        String s = getDossierACOR() + PRACORConst.DOSSIER_DEM_COUR + PRACORConst.NF_ANNONCE_PAY;
        s = JadeStringUtil.change(s, "\\", "\\\\");
        return s;
    }

    /**
     * getter pour l'attribut chemin annonce RR
     *
     * @return la valeur courante de l'attribut chemin annonce RR
     */
    public String getCheminAnnonceRR() throws PRACORException, Exception {
        String s = getDossierACOR() + PRACORConst.DOSSIER_DEM_COUR + PRACORConst.NF_ANNONCE_RR;
        s = JadeStringUtil.change(s, "\\", "\\\\");
        return s;
    }

    /**
     * getter pour l'attribut chemin annonce.xml
     *
     * @return la valeur courante de l'attribut chemin annonce RR
     */
    public String getCheminAnnonceXML() throws PRACORException, Exception {
        String s = getDossierACOR() + PRACORConst.DOSSIER_DEM_COUR + PRACORConst.NF_ANNONCE_XML;
        s = JadeStringUtil.change(s, "\\", "\\\\");
        return s;
    }

    /**
     * getter pour l'attribut chemin annonce pay
     *
     * @return la valeur courante de l'attribut chemin annonce pay
     */
    public String getCheminFCalculXML() throws PRACORException, Exception {
        String s = getDossierACOR() + PRACORConst.DOSSIER_DEM_COUR + PRACORConst.NF_FCALCUL_XML;
        s = JadeStringUtil.change(s, "\\", "\\\\");
        return s;
    }

    /**
     * getter pour l'attribut contenu annonce pay
     *
     * @return la valeur courante de l'attribut contenu annonce pay
     */
    public String getContenuAnnoncePay() {
        return contenuAnnoncePay;
    }

    /**
     * getter pour l'attribut contenu annonce RR
     *
     * @return la valeur courante de l'attribut contenu annonce RR
     */
    public String getContenuAnnonceRR() {
        return contenuAnnonceRR;
    }

    public String getContenuAnnonceXML() {
        return contenuAnnonceXML;
    }

    /**
     * @return the contenuFeuilleCalculXML
     */
    public String getContenuFeuilleCalculXML() {
        return contenuFeuilleCalculXML;
    }

    /**
     * getter pour l'attribut dossier ACOR
     *
     * @return la valeur courante de l'attribut dossier ACOR
     */
    public String getDossierACOR() throws PRACORException, Exception {
        return PRACORConst.dossierACOR(getSession());

    }

    public Map getFilesContent() {
        return filesContent;
    }

    /**
     * getter pour l'attribut no AVSAssure
     *
     * @return la valeur courante de l'attribut no AVSAssure
     */
    public String getNoAVSAssure() {
        return noAVSAssure;
    }

    /**
     * @return
     */
    public String getNomPrenomAssure() {
        return nomPrenomAssure;
    }

    /**
     * getter pour l'attribut spy
     *
     * @return null
     */
    @Override
    public BSpy getSpy() {
        return null;
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

    public String getStartNavigateurAcor(BSession session) {
        try {
            return PRACORConst.navigateurACOR(session);
        } catch (PRACORException e) {
            LOG.warn("Impossible de récupérer le navigateur ACOR.", e);
            return StringUtils.EMPTY;
        }
    }

    public String getAdresseWebACOR(String askAction, String token) {
        try {
            return Slashs.addLastSlash(CommonProperties.ACOR_ADRESSE_WEB.getValue()) + askAction + "/" + token;
        } catch (PropertiesException e) {
            LOG.warn("La propriété n'existe pas ou n'est pas renseigné :", e);
        }
        return StringUtils.EMPTY;
    }

    /**
     * getter pour l'attribut writer
     *
     * @return la valeur courante de l'attribut writer
     */
    public PrintWriter getWriter() {
        return writer;
    }

    /**
     * getter pour l'attribut calculable
     *
     * @return la valeur courante de l'attribut calculable
     */
    public boolean isCalculable() {
        return calculable;
    }

    /**
     * setter pour l'attribut calculable
     *
     * @param calculable une nouvelle valeur pour cet attribut
     */
    public void setCalculable(boolean calculable) {
        this.calculable = calculable;
    }

    /**
     * setter pour l'attribut contenu annonce pay
     *
     * @param contenuAnnoncePay une nouvelle valeur pour cet attribut
     */
    public void setContenuAnnoncePay(String contenuAnnoncePay) {
        this.contenuAnnoncePay = contenuAnnoncePay;
    }

    /**
     * setter pour l'attribut contenu annonce RR
     *
     * @param contenuAnnonceRR une nouvelle valeur pour cet attribut
     */
    public void setContenuAnnonceRR(String contenuAnnonceRR) {
        this.contenuAnnonceRR = contenuAnnonceRR;
    }

    public void setContenuAnnonceXML(String contenuAnnonceXML) {
        this.contenuAnnonceXML = contenuAnnonceXML;
    }

    /**
     * @param contenuFeuilleCalculXML the contenuFeuilleCalculXML to set
     */
    public void setContenuFeuilleCalculXML(String contenuFeuilleCalculXML) {
        this.contenuFeuilleCalculXML = contenuFeuilleCalculXML;
    }

    public void setFilesContent(Map filesContent) {
        this.filesContent = filesContent;
    }

    /**
     * setter pour l'attribut no AVSAssure
     *
     * @param noAVSAssure une nouvelle valeur pour cet attribut
     */
    public void setNoAVSAssure(String noAVSAssure) {
        this.noAVSAssure = noAVSAssure;
    }

    /**
     * @param string
     */
    public void setNomPrenomAssure(String string) {
        nomPrenomAssure = string;
    }

    /**
     * setter pour l'attribut writer
     *
     * @param writer une nouvelle valeur pour cet attribut
     */
    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public boolean validate() {
        return true;
    }

}
