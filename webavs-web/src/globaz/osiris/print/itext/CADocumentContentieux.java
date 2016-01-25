package globaz.osiris.print.itext;

import globaz.framework.printing.itext.FWIScriptDocument;
import globaz.osiris.api.APIParametreEtape;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APITaxe;
import globaz.osiris.external.IntAdresseCourrier;
import globaz.osiris.external.IntDocumentContentieux;
import globaz.osiris.external.IntTiers;
import globaz.osiris.print.itext.list.CADocumentManager;
import java.util.Properties;
import java.util.Vector;

/**
 * Classe abstraite parente de tous les documents du contentieux osiris. <br>
 * Date de création : (27.05.2002 08:00:50)
 * 
 * @author: Administrator
 */
public abstract class CADocumentContentieux extends CADocumentManager implements IntDocumentContentieux {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private IntAdresseCourrier adresseCourrier = null;
    private String date = "";
    private String dateDelaiPaiement = "";
    private String datePremierRappel = "";
    private APIParametreEtape etape = null;
    private Boolean modePrevisionnel = new Boolean(false);
    private boolean printable = true;
    private Properties props = null;
    private APISection section = null;
    private Vector taxes = null;
    private IntTiers tiers = null;
    private IntTiers tiersOffice;

    /**
     * Commentaire relatif au constructeur CADocumentContentieux.
     * 
     * @throws Exception
     * @throws Exception
     */
    public CADocumentContentieux() throws Exception {
        super();
    }

    /**
     * Date de création : (12.06.2002 10:08:16)
     * 
     * @param newTaxe
     *            globaz.osiris.db.contentieux.CATaxe
     */
    @Override
    public void addTaxe(APITaxe newTaxe) {
        // Instancier le vecteur
        if (taxes == null) {
            taxes = new Vector();
        }

        // Ajouter la nouvelle taxe
        if (newTaxe != null) {
            taxes.add(newTaxe);
        }
    }

    /**
     * Date de création : (13.06.2002 10:51:26)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    public abstract void afterPrint(globaz.globall.db.BTransaction transaction) throws Exception;

    /**
     * Date de création : (23.05.2002 15:05:13)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    public abstract void beforePrint(globaz.globall.db.BTransaction transaction) throws Exception;

    @Override
    public IntAdresseCourrier getAdresseCourrier() {
        return adresseCourrier;
    }

    /**
     * Date sur document
     * 
     * @see globaz.osiris.external.IntDocumentContentieux#getDate()
     */
    @Override
    public String getDate() {
        return date;
    }

    /**
     * Date de création : (13.06.2002 11:48:51)
     * 
     * @return String
     */
    @Override
    public String getDateDelaiPaiement() {
        return dateDelaiPaiement;
    }

    /**
     * Date de création : (30.07.2002 12:12:00)
     * 
     * @return String
     */
    @Override
    public String getDatePremierRappel() {
        return datePremierRappel;
    }

    /**
     * Date de création : (21.07.2003 14:39:49)
     * 
     * @return FWIScriptDocument
     */
    @Override
    public FWIScriptDocument getFWIDocumentClass() {
        try {
            return (FWIScriptDocument) getDocumentClass();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public APIParametreEtape getParametreEtape() {
        return etape;
    }

    /**
     * Date de création : (23.05.2002 14:29:31)
     * 
     * @return String
     * @param name
     *            String
     */
    @Override
    public String getProperty(String name) {
        if (props == null) {
            return null;
        } else {
            return props.getProperty(name);
        }
    }

    @Override
    public APISection getSection() {
        return section;
    }

    /**
     * Date de création : (18.06.2002 08:47:25)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntTiers
     */
    @Override
    public IntTiers getTiers() {
        return tiers;
    }

    @Override
    public IntTiers getTiersOfficePoursuites() {
        return tiersOffice;
    }

    /**
     * Date de création : (27.05.2002 08:35:54)
     * 
     * @return boolean
     */
    @Override
    public boolean hasTaxes() {
        if (taxes == null) {
            return false;
        } else {
            return !taxes.isEmpty();
        }
    }

    /**
     * Date de création : (12.06.2002 10:51:12)
     * 
     * @return boolean
     */
    @Override
    public Boolean isModePrevisionnel() {
        return modePrevisionnel;
    }

    /**
     * Date de création : (13.06.2002 10:48:36)
     * 
     * @return boolean
     */
    @Override
    public boolean isPrintable() {
        return printable;
    }

    /**
     * Date de création : (11.06.2002 13:32:58)
     * 
     * @return java.util.Enumeration
     */
    @Override
    public java.util.Enumeration listTaxes() {
        if (taxes == null) {
            return null;
        } else {
            return taxes.elements();
        }
    }

    /**
     * Date de création : (23.05.2002 14:47:44)
     * 
     * @param adresseCourrier
     *            globaz.osiris.interfaceext.tiers.IntAdresseCourrier
     */
    @Override
    public void setAdresseCourrier(IntAdresseCourrier adresseCourrier) {
        this.adresseCourrier = adresseCourrier;
    }

    /**
     * Date de création : (23.05.2002 14:58:45)
     * 
     * @param date
     *            String
     */
    @Override
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Date de création : (13.06.2002 11:48:51)
     * 
     * @param newDateDelaiPaiement
     *            String
     */
    @Override
    public void setDateDelaiPaiement(String newDateDelaiPaiement) {
        dateDelaiPaiement = newDateDelaiPaiement;
    }

    /**
     * Date de création : (30.07.2002 12:12:00)
     * 
     * @param newDatePremierRappel
     *            String
     */
    @Override
    public void setDatePremierRappel(String newDatePremierRappel) {
        datePremierRappel = newDatePremierRappel;
    }

    /**
     * Date de création : (12.06.2002 10:51:12)
     * 
     * @param newModePrevisionnel
     *            boolean
     */
    public void setModePrevisionnel(Boolean newModePrevisionnel) {
        modePrevisionnel = newModePrevisionnel;
    }

    /**
     * Date de création : (23.05.2002 14:45:45)
     * 
     * @param parametreEtape
     *            globaz.osiris.db.contentieux.CAParametreEtape
     */
    @Override
    public void setParametreEtape(APIParametreEtape parametreEtape) {
        etape = parametreEtape;
    }

    /**
     * Date de création : (23.05.2002 14:51:08)
     * 
     * @param props
     *            java.util.Properties
     */
    @Override
    public void setProperties(java.util.Properties props) {
        this.props = props;
    }

    /**
     * Date de création : (23.05.2002 14:28:27)
     * 
     * @param name
     *            String
     * @param value
     *            String
     */
    @Override
    public void setProperty(String name, String value) {
        if (props == null) {
            props = new Properties();
        }
        props.setProperty(name, value);
    }

    /**
     * Date de création : (23.05.2002 14:39:49)
     * 
     * @param section
     *            globaz.osiris.db.comptes.CASection
     */
    @Override
    public void setSection(APISection section) {
        this.section = section;
    }

    /**
     * Date de création : (18.06.2002 08:47:25)
     * 
     * @param newTiers
     *            IntTiers
     */
    @Override
    public void setTiers(IntTiers newTiers) {
        tiers = newTiers;
    }

    /**
     * Date de création : (23.05.2002 15:56:55)
     * 
     * @param idTiersOfficePoursuites
     *            IntTiers
     */
    @Override
    public void setTiersOfficePoursuites(IntTiers tiersOfficePoursuites) {
        tiersOffice = tiersOfficePoursuites;
    }
}
