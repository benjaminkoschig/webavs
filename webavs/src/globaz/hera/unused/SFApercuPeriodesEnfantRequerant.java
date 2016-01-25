/*
 * ado Créé le 4 oct. 05
 */
package globaz.hera.unused;

// package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author ado
 * 
 *         4 oct. 05
 */
public class SFApercuPeriodesEnfantRequerant extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        // try {
        // //new SFApercuPeriodesEnfantRequerant().go();
        // } catch (ClassNotFoundException e) {
        // e.printStackTrace();
        // } catch (SQLException e) {
        // e.printStackTrace();
        // }
        System.exit(0);
    }

    private String dateDebut = "";

    private String dateFin = "";

    private String idEnfant = "";

    private String idMembreFamille = "";

    private String idMembresFamilles = "";

    private String idPeriode = "";

    private String idRequerant = "";

    private String idTiers = "";

    private String nomEnfant = "";

    private String numAVS = "";

    private String prenomEnfant = "";

    private String type = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        if (isLoadedFromManager()) {
            // TODO
        } else {
            // TODO
        }
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        if (isLoadedFromManager()) { // lecture de la jointure

            statement.dbReadNumeric("WFIRFR");
            statement.dbReadNumeric("WFIMEF");
            idRequerant = statement.dbReadNumeric("WFIREQ");
            // -----------
            statement.dbReadNumeric("WIIENF");
            statement.dbReadNumeric("WIDADO");
            // -----------
            idMembreFamille = statement.dbReadNumeric("WIIMEF");
            statement.dbReadNumeric("WIICON");
            idMembresFamilles = statement.dbReadNumeric("WGIMEF");
            idTiers = statement.dbReadNumeric("WGITIE");
            statement.dbReadString("WGLNOM");
            statement.dbReadString("WGLPRE");
            statement.dbReadString("WGLAVS");
            statement.dbReadNumeric("WGDNAI");
            // -----------
            statement.dbReadNumeric("WGTSEX");
            statement.dbReadNumeric("WGDDEC");
            statement.dbReadNumeric("WGTCDO");
            statement.dbReadNumeric("WGTNAT");
            statement.dbReadNumeric("WGTPDO");
            idPeriode = statement.dbReadNumeric("WHIPER");
            type = statement.dbReadNumeric("WHTTYP");
            dateDebut = statement.dbReadNumeric("WHDDEB");
            dateFin = statement.dbReadNumeric("WHDFIN");
            statement.dbReadNumeric("WHTPAY");
            statement.dbReadNumeric("WHIDMF");
            // -----------
            statement.dbReadNumeric("WHIDBT");
            statement.dbReadNumeric("HTITIE");
            statement.dbReadNumeric("HNIPAY");
            statement.dbReadNumeric("HTTTIE");
            statement.dbReadNumeric("HTTTTI");
            nomEnfant = statement.dbReadString("HTLDE1");
            prenomEnfant = statement.dbReadString("HTLDE2");
            statement.dbReadString("HTLDE3");
            statement.dbReadString("HTLDE4");
            statement.dbReadNumeric("HTTLAN");
            // -----------
            statement.dbReadString("HTPPHY");
            statement.dbReadString("HTPMOR");
            statement.dbReadString("HTINAC");
            statement.dbReadString("HTLDU1");
            statement.dbReadString("HTLDU2");
            statement.dbReadString("HTLDEC");
            statement.dbReadString("HTLDUC");
            statement.dbReadNumeric("HTITIE");
            numAVS = statement.dbReadString("HXNAVS");
            statement.dbReadString("HXNAFF");
            statement.dbReadString("HXNCON");
            statement.dbReadString("HXAAVS");
            statement.dbReadNumeric("HXTGAF");
            statement.dbReadNumeric("HXDDAC");
            statement.dbReadNumeric("HXDFAC");
            // -----------
            statement.dbReadNumeric("HTITIE");
            statement.dbReadNumeric("HJILOC");
            statement.dbReadNumeric("HPDNAI");
            statement.dbReadNumeric("HPDDEC");
            statement.dbReadNumeric("HPTETC");
            statement.dbReadNumeric("HPTSEX");
            statement.dbReadNumeric("HPTCAN");
            statement.dbReadString("HPDIST");
            // -----------

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

    }

    /**
     * Methode utilisée par les API.
     * 
     * @param params
     * @return
     * @throws Exception
     */
    public BManager find(Hashtable params) throws Exception {
        BManager manager = getManager();
        manager.setSession(getSession());
        if (params != null) {
            Enumeration methods = params.keys();
            while (methods.hasMoreElements()) {
                String methodName = (String) methods.nextElement();
                String value = (String) params.get(methodName);
                Method m = manager.getClass().getMethod(methodName, new Class[] { String.class });
                if (m != null) {
                    m.invoke(manager, new Object[] { value });
                }
            }
        }
        manager.find();
        return manager;
    }

    /**
     * @return
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return
     */
    public String getIdEnfant() {
        return idEnfant;
    }

    /**
     * @return
     */
    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    /**
     * @return
     */
    public String getIdMembresFamilles() {
        return idMembresFamilles;
    }

    /**
     * @return
     */
    public String getIdPeriode() {
        return idPeriode;
    }

    /**
     * @return
     */
    public String getIdRequerant() {
        return idRequerant;
    }

    /**
     * @return
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new SFApercuPeriodesEnfantRequerantManager();
    }

    /**
     * @return
     */
    public String getNomEnfant() {
        return nomEnfant;
    }

    /**
     * @return
     */
    public String getNumAVS() {
        return numAVS;
    }

    /**
     * @return
     */
    public String getPrenomEnfant() {
        return prenomEnfant;
    }

    /**
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * @param string
     */
    public void setDateDebut(String string) {
        dateDebut = string;
    }

    /**
     * @param string
     */
    public void setDateFin(String string) {
        dateFin = string;
    }

    /**
     * @param string
     */
    public void setIdEnfant(String string) {
        idEnfant = string;
    }

    /**
     * @param string
     */
    public void setIdMembreFamille(String string) {
        idMembreFamille = string;
    }

    /**
     * @param string
     */
    public void setIdMembresFamilles(String string) {
        idMembresFamilles = string;
    }

    /**
     * @param string
     */
    public void setIdPeriode(String string) {
        idPeriode = string;
    }

    /**
     * @param string
     */
    public void setIdRequerant(String string) {
        idRequerant = string;
    }

    /**
     * @param string
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * @param string
     */
    public void setNomEnfant(String string) {
        nomEnfant = string;
    }

    /**
     * @param string
     */
    public void setNumAVS(String string) {
        numAVS = string;
    }

    /**
     * @param string
     */
    public void setPrenomEnfant(String string) {
        prenomEnfant = string;
    }

    /**
     * @param string
     */
    public void setType(String string) {
        type = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#toString()
     */
    public String toMyString() {
        String retour = "";
        retour += "dateFin = " + dateFin + " - ";

        retour += "dateDebut = " + dateDebut + " - ";

        retour += "numAVS = " + numAVS + " - ";

        retour += "prenomEnfant = " + prenomEnfant + " - ";

        retour += "nomEnfant = " + nomEnfant + " - ";

        retour += "idPeriode = " + idPeriode + " - ";

        retour += "type = " + type + " - ";

        retour += "idTiers = " + idTiers + " - ";

        retour += "idMembresFamilles = " + idMembresFamilles + " - ";

        retour += "idMembreFamille = " + idMembreFamille + " - ";

        retour += "idEnfant = " + idEnfant + " - ";

        retour += "idRequerant = " + idRequerant + " - ";
        return retour;
    }

}
