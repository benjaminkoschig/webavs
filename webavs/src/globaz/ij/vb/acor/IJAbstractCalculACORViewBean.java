package globaz.ij.vb.acor;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.io.PrintWriter;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public abstract class IJAbstractCalculACORViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private boolean calculable;
    private String contenuAnnoncePay;
    private String contenuFCalculXML;
    private transient Map filesContent;
    private String noAVSAssure;

    private String nomPrenomAssure;

    // @deprecated
    private transient PrintWriter writer;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut chemin annonce pay
     * 
     * @return la valeur courante de l'attribut chemin annonce pay
     */
    public String getCheminAnnoncePay() throws PRACORException {
        return getDossierACOR() + PRACORConst.DOSSIER_DEM_COUR + PRACORConst.NF_ANNONCE_PAY;
    }

    /**
     * getter pour l'attribut chemin annonce pay
     * 
     * @return la valeur courante de l'attribut chemin annonce pay
     */
    public String getCheminFCalculXML() throws PRACORException {
        return getDossierACOR() + PRACORConst.DOSSIER_DEM_COUR + PRACORConst.NF_FCALCUL_XML;
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
     * getter pour l'attribut contenu FCalcul XML
     * 
     * @return la valeur courante de l'attribut contenu FCalcul XML
     */
    public String getContenuFCalculXML() {
        return contenuFCalculXML;
    }

    /**
     * getter pour l'attribut dossier ACOR
     * 
     * @return la valeur courante de l'attribut dossier ACOR
     */
    public String getDossierACOR() throws PRACORException {
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
     * @param calculable
     *            une nouvelle valeur pour cet attribut
     */
    public void setCalculable(boolean calculable) {
        this.calculable = calculable;
    }

    /**
     * setter pour l'attribut contenu annonce pay
     * 
     * @param contenuAnnoncePay
     *            une nouvelle valeur pour cet attribut
     */
    public void setContenuAnnoncePay(String contenuAnnoncePay) {
        this.contenuAnnoncePay = contenuAnnoncePay;
    }

    /**
     * setter pour l'attribut contenu FCalcul XML
     * 
     * @param contenuFCalculXML
     *            une nouvelle valeur pour cet attribut
     */
    public void setContenuFCalculXML(String contenuFCalculXML) {
        this.contenuFCalculXML = contenuFCalculXML;
    }

    public void setFilesContent(Map filesContent) {
        this.filesContent = filesContent;
    }

    /**
     * setter pour l'attribut no AVSAssure
     * 
     * @param noAVSAssure
     *            une nouvelle valeur pour cet attribut
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
     * @param writer
     *            une nouvelle valeur pour cet attribut
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
