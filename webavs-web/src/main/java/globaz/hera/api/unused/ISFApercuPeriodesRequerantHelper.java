/*
 * ado Créé le 4 oct. 05
 */
package globaz.hera.api.unused;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import java.util.Hashtable;

/**
 * Permet d'obtenir toutes les périodes pour un requerant
 * 
 * <code> 
 * 		ISFApercuPeriodesRequerant requerant = (ISFApercuPeriodesRequerant) session.getAPIFor(ISFApercuPeriodesRequerant.class);
 * 		Hashtable t = new Hashtable();
 * 		t.put(ISFApercuPeriodesRequerant.FIND_FOR_ID_REQUERANT, "104");
 * 		ISFApercuPeriodesRequerant[] periodes = requerant.findPeriodes(t);
 * 		for (int i = 0; i < periodes.length; i++) {
 * 			System.out.println(periodes[i].getDateDebut());
 * 			System.out.println(periodes[i].getDateFin());
 * 			System.out.println(periodes[i].getNoAvs());
 * 		}
 * 
 * </code>
 * 
 * @author ado
 * 
 *         4 oct. 05
 */
public class ISFApercuPeriodesRequerantHelper extends GlobazHelper implements ISFApercuPeriodesRequerant {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ENTITY_CLASS_NAME = "globaz.hera.db.famille.SFApercuPeriodesRequerant";
    public static final String PROP_DATEDEBUT = "dateDebut";
    public static final String PROP_DATEFIN = "dateFin";

    public static final String PROP_IDPERIODE = "idPeriode";
    public static final String PROP_NUMAVS = "noAvs";
    public static final String PROP_TYPE = "type";

    /**
     * @param valueObject
     */
    public ISFApercuPeriodesRequerantHelper() {
        super(ISFApercuPeriodesRequerantHelper.ENTITY_CLASS_NAME);
    }

    /**
     * @param periode
     */
    public ISFApercuPeriodesRequerantHelper(GlobazValueObject periode) {
        super(periode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesEnfant#find(java.util.Hashtable)
     */
    @Override
    public Object[] find(Hashtable params) throws Exception {
        Object[] result = null;
        result = _getArray("find", new Object[] { params });
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesEnfant#findAdhesions(java.util.Hashtable )
     */
    @Override
    public ISFApercuPeriodesRequerant[] findPeriodes(Hashtable params) throws Exception {
        ISFApercuPeriodesRequerant[] result = null;
        Object[] objResult = find(params);
        if (objResult != null) {
            result = new ISFApercuPeriodesRequerantHelper[objResult.length];
            for (int i = 0; i < objResult.length; i++) {
                GlobazValueObject periode = (GlobazValueObject) objResult[i];
                result[i] = new ISFApercuPeriodesRequerantHelper(periode);
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesRequerant#getDateDebut()
     */
    @Override
    public String getDateDebut() {
        return this.getProp(ISFApercuPeriodesRequerantHelper.PROP_DATEDEBUT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesRequerant#getDateFin()
     */
    @Override
    public String getDateFin() {
        return this.getProp(ISFApercuPeriodesRequerantHelper.PROP_DATEFIN);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesRequerant#getIdPeriode()
     */
    @Override
    public String getIdPeriode() {
        return this.getProp(ISFApercuPeriodesRequerantHelper.PROP_IDPERIODE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesRequerant#getNoAvs()
     */
    @Override
    public String getNoAvs() {
        return this.getProp(ISFApercuPeriodesRequerantHelper.PROP_NUMAVS);
    }

    /**
     * @param vo
     * @param name
     * @return
     */
    private String getProp(GlobazValueObject vo, String name) {
        Object retValue = vo.getProperty(name);
        if (retValue == null) {
            return "";
        } else {
            return retValue.toString();
        }
    }

    /**
     * @param name
     * @return
     */
    private String getProp(String name) {
        return this.getProp(_getValueObject(), name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesRequerant#getType()
     */
    @Override
    public String getType() {
        return this.getProp(ISFApercuPeriodesRequerantHelper.PROP_TYPE);
    }
}
