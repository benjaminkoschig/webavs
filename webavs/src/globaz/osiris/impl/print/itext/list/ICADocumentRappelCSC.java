package globaz.osiris.impl.print.itext.list;

import globaz.framework.printing.itext.FWIScriptDocument;
import globaz.globall.api.BIDocument;
import globaz.globall.api.BISession;
import globaz.osiris.application.CAApplication;
import globaz.osiris.external.IntAdresseCourrier;
import globaz.osiris.external.IntTiers;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Insérez la description du type ici. Date de création : (23.05.2002 14:26:30)
 * 
 * @author: Administrator
 */
public interface ICADocumentRappelCSC {
    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2002 09:50:29)
     */
    void beforePrint(globaz.globall.db.BTransaction transaction) throws Exception;

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 10:45:15)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntAdresseCourrier
     */
    IntAdresseCourrier getAdresseCourrier();

    /**
     * Insérez la description de la méthode ici. Date de création : (13.06.2002 11:48:24)
     * 
     * @return java.lang.String
     */
    String getDateRappel();

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 10:44:52)
     * 
     * @return java.lang.String
     */
    String getDateSurDocument();

    /**
     * Retourne le JasperPrint de la dispositions légales.
     * 
     * @param application
     * @param codeIsoLangue
     * @param name
     * @return
     * @throws Exception
     */
    public JasperPrint getDispositionsLegales(CAApplication application, String codeIsoLangue, String name)
            throws Exception;

    public BIDocument getDocumentClass() throws Exception;

    /**
     * Renvoi le document de l'extrait de compte du rappel.
     * 
     * @return
     */
    public JasperPrint getExtraitCompteDoc();

    /**
     * Insérez la description de la méthode ici. Date de création : (01.07.2002 18:14:14)
     * 
     * @return globaz.framework.printing.FWDocument
     */
    public FWIScriptDocument getFWIDocumentClass() throws Exception;

    public String getIdExterneRole();

    public String getIdTriFormate();

    /**
     * Insérez la description de la méthode ici. Date de création : (06.11.2002 16:13:10)
     * 
     * @return globaz.globall.api.BISession
     */
    BISession getISession();

    public String getLigneAdresse();

    /**
     * Insérez la description de la méthode ici. Date de création : (01.07.2002 18:14:14)
     * 
     * @return globaz.framework.printing.FWDocument
     */

    String getMontant();

    public String getNoConsulat();

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2002 08:46:49)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntTiers
     */
    IntTiers getTiers();

    /**
     * Insérez la description de la méthode ici. Date de création : (23.05.2002 14:47:44)
     * 
     * @param adresseCourrier
     *            globaz.osiris.interfaceext.tiers.IntAdresseCourrier
     */
    void setAdresseCourrier(IntAdresseCourrier adresseCourrier);

    /**
     * Insérez la description de la méthode ici. Date de création : (13.06.2002 11:28:36)
     * 
     * @param newDate
     *            java.lang.String
     */
    void setDateRappel(String newDate);

    /**
     * Insérez la description de la méthode ici. Date de création : (23.05.2002 14:58:45)
     * 
     * @param date
     *            java.lang.String
     */
    void setDateSurDocument(String date);

    /**
     * Ajoute le document de l'extrait de compte au rappel.
     * 
     * @param doc
     */
    public void setExtraitCompteDoc(JasperPrint doc);

    void setIdExterneRole(String id);

    void setIdTriFormate(String idTri);

    /**
     * Insérez la description de la méthode ici. Date de création : (06.11.2002 16:14:21)
     */
    void setISession(BISession session);

    void setMontant(String montant);

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2002 08:46:22)
     * 
     * @param tiers
     *            globaz.osiris.interfaceext.tiers.IntTiers
     */
    void setTiers(IntTiers tiers);
}
