/*
 * ado Créé le 4 oct. 05
 */
package globaz.hera.unused;

// package globaz.hera.db.famille;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hera.db.famille.SFApercuRequerant;
import globaz.hera.db.famille.SFPeriode;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author ado
 * 
 *         4 oct. 05
 */
public class SFApercuPeriodesRequerant extends SFApercuRequerant {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = "";
    private String dateFin = "";
    private String idPeriode = "";
    private String type = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idPeriode = statement.dbReadNumeric(SFPeriode.FIELD_IDPERIODE);
        type = statement.dbReadNumeric(SFPeriode.FIELD_TYPE);
        dateDebut = statement.dbReadDateAMJ(SFPeriode.FIELD_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(SFPeriode.FIELD_DATEFIN);

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
    public String getIdPeriode() {
        return idPeriode;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new SFApercuPeriodesRequerantManager();
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
    public void setIdPeriode(String string) {
        idPeriode = string;
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
        retour += getIdPeriode() + " - " + getType() + " - " + getDateDebut() + " - " + getDateFin() + " - " + getNss()
                + getPays();
        return retour;
    }
}
