package globaz.osiris.external;

import globaz.framework.printing.itext.FWIScriptDocument;
import globaz.globall.api.BIDocument;
import globaz.globall.api.BISession;
import globaz.osiris.api.APIParametreEtape;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APITaxe;
import java.util.Enumeration;

/**
 * Date de création : (23.05.2002 14:26:30)
 * 
 * @author: Administrator
 */
public interface IntDocumentContentieux {
    /**
     * Date de création : (11.06.2002 13:28:48)
     * 
     * @param taxe
     *            globaz.osiris.db.contentieux.CACalculTaxe
     */
    void addTaxe(APITaxe taxe);

    /**
     * Date de création : (27.06.2002 09:50:38)
     */
    void afterPrint(globaz.globall.db.BTransaction transaction) throws Exception;

    /**
     * Date de création : (27.06.2002 09:50:29)
     */
    void beforePrint(globaz.globall.db.BTransaction transaction) throws Exception;

    /**
     * Date de création : (12.06.2002 10:45:15)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntAdresseCourrier
     */
    IntAdresseCourrier getAdresseCourrier();

    /**
     * Date de création : (12.06.2002 10:44:52)
     * 
     * @return java.lang.String
     */
    String getDate();

    /**
     * Date de création : (13.06.2002 11:48:24)
     * 
     * @return java.lang.String
     */
    String getDateDelaiPaiement();

    /**
     * Date de création : (13.06.2002 11:48:24)
     * 
     * @return java.lang.String
     */
    String getDatePremierRappel();

    /**
     * Date de création : (01.07.2002 18:14:14)
     * 
     * @return globaz.framework.printing.FWDocument
     */
    public BIDocument getDocumentClass() throws Exception;

    /**
     * Date de création : (01.07.2002 18:14:14)
     * 
     * @return globaz.framework.printing.FWDocument
     */
    public FWIScriptDocument getFWIDocumentClass() throws Exception;

    /**
     * Date de création : (06.11.2002 16:13:10)
     * 
     * @return globaz.globall.api.BISession
     */
    BISession getISession();

    /**
     * Date de création : (12.06.2002 10:45:38)
     * 
     * @return globaz.osiris.db.contentieux.CAParametreEtape
     */
    APIParametreEtape getParametreEtape();

    /**
     * Date de création : (23.05.2002 14:29:31)
     * 
     * @return java.lang.String
     * @param name
     *            java.lang.String
     */
    String getProperty(String name);

    /**
     * Date de création : (12.06.2002 10:45:56)
     * 
     * @return globaz.osiris.db.comptes.CASection
     */
    APISection getSection();

    /**
     * Date de création : (18.06.2002 08:46:49)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntTiers
     */
    IntTiers getTiers();

    /**
     * Date de création : (12.06.2002 10:46:14)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntTiers
     */
    IntTiers getTiersOfficePoursuites();

    /**
     * Date de création : (12.06.2002 10:44:02)
     * 
     * @return boolean
     */
    boolean hasTaxes();

    /**
     * Date de création : (12.06.2002 10:50:51)
     * 
     * @return boolean
     */
    Boolean isModePrevisionnel();

    /**
     * Date de création : (13.06.2002 10:48:02)
     * 
     * @return boolean
     */
    boolean isPrintable();

    /**
     * Date de création : (12.06.2002 10:44:31)
     * 
     * @return java.util.Enumeration
     */
    Enumeration listTaxes();

    /**
     * Date de création : (23.05.2002 14:47:44)
     * 
     * @param adresseCourrier
     *            globaz.osiris.interfaceext.tiers.IntAdresseCourrier
     */
    void setAdresseCourrier(IntAdresseCourrier adresseCourrier);

    /**
     * Date de création : (23.05.2002 14:58:45)
     * 
     * @param date
     *            java.lang.String
     */
    void setDate(String date);

    /**
     * Date de création : (13.06.2002 11:28:36)
     * 
     * @param newDate
     *            java.lang.String
     */
    void setDateDelaiPaiement(String newDate);

    /**
     * Date de création : (13.06.2002 11:28:36)
     * 
     * @param newDate
     *            java.lang.String
     */
    void setDatePremierRappel(String newDate);

    /**
     * Date de création : (06.11.2002 16:14:21)
     */
    void setISession(BISession session);

    /**
     * Date de création : (12.06.2002 10:50:40)
     * 
     * @param modePervisionnel
     *            boolean
     */
    void setModePrevisonnel(Boolean modePervisionnel);

    /**
     * Date de création : (23.05.2002 14:45:45)
     * 
     * @param parametreEtape
     *            globaz.osiris.db.contentieux.CAParametreEtape
     */
    void setParametreEtape(APIParametreEtape parametreEtape);

    /**
     * Date de création : (23.05.2002 14:51:08)
     * 
     * @param props
     *            java.util.Properties
     */
    void setProperties(java.util.Properties props);

    /**
     * Date de création : (23.05.2002 14:28:27)
     * 
     * @param name
     *            java.lang.String
     * @param value
     *            java.lang.String
     */
    void setProperty(String name, String value);

    /**
     * Date de création : (23.05.2002 14:39:49)
     * 
     * @param section
     *            globaz.osiris.db.comptes.CASection
     */
    void setSection(APISection section);

    /**
     * Date de création : (18.06.2002 08:46:22)
     * 
     * @param tiers
     *            globaz.osiris.interfaceext.tiers.IntTiers
     */
    void setTiers(IntTiers tiers);

    /**
     * Date de création : (23.05.2002 15:56:55)
     * 
     * @param idTiersOfficePoursuites
     *            globaz.osiris.interfaceext.tiers.IntTiers
     */
    void setTiersOfficePoursuites(IntTiers idTiersOfficePoursuites);
}
