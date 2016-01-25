/*
 * ado Créé le 4 oct. 05
 */
package globaz.hera.api.unused;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import java.util.Hashtable;

/**
 * 
 * Permet d'obtenir les périodes pour les enfants d'un requerant On passe l'id requerant :
 * 
 * <code>
 * 		ISFApercuPeriodesEnfant requerant = (ISFApercuPeriodesEnfant) session.getAPIFor(ISFApercuPeriodesEnfant.class);
 * 		Hashtable t = new Hashtable();
 * 		t.put(ISFApercuPeriodesEnfant.FIND_FOR_ID_REQUERANT, "104");
 * 		ISFApercuPeriodesEnfant[] periodes = requerant.findPeriodes(t);
 * 		for (int i = 0; i < periodes.length; i++) {
 * 			System.out.println(periodes[i].getNomEnfant());
 * 			System.out.println(periodes[i].getPrenomEnfant());
 * 			System.out.println(periodes[i].getNumAVS());
 * 			System.out.println(periodes[i].getDateDebut());
 * 			System.out.println(periodes[i].getDateFin());
 * 		}
 * </code>
 * 
 * @author ado
 * 
 *         4 oct. 05
 */
public class ISFApercuPeriodesEnfantHelper extends GlobazHelper implements ISFApercuPeriodesEnfant {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ENTITY_CLASS_NAME = "globaz.hera.db.famille.SFApercuPeriodesEnfantRequerant";
    public static String PROP_DATEDEBUT = "dateDebut";

    public static String PROP_DATEFIN = "dateFin";
    public static String PROP_IDENFANT = "idEnfant";
    public static String PROP_IDMEMBREFAMILLE = "idMembreFamille";
    public static String PROP_IDMEMBRESFAMILLES = "idMembresFamilles";
    public static String PROP_IDPERIODE = "idPeriode";
    public static String PROP_IDREQUERANT = "idRequerant";
    public static String PROP_IDTIERS = "idTiers";
    public static String PROP_NOMENFANT = "nomEnfant";
    public static String PROP_NUMAVS = "numAVS";
    public static String PROP_PRENOMENFANT = "prenomEnfant";
    public static String PROP_TYPE = "type";

    /**
     * @param valueObject
     */
    public ISFApercuPeriodesEnfantHelper() {
        super(ISFApercuPeriodesEnfantHelper.ENTITY_CLASS_NAME);
    }

    /**
     * @param periode
     */
    public ISFApercuPeriodesEnfantHelper(GlobazValueObject valueObject) {
        super(valueObject);

        // _getValueObject().setProperty("isAPI", new Boolean(true));
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
    public ISFApercuPeriodesEnfant[] findPeriodes(Hashtable params) throws Exception {
        ISFApercuPeriodesEnfant[] result = null;
        Object[] objResult = find(params);
        if (objResult != null) {
            result = new ISFApercuPeriodesEnfantHelper[objResult.length];
            for (int i = 0; i < objResult.length; i++) {
                GlobazValueObject periode = (GlobazValueObject) objResult[i];
                result[i] = new ISFApercuPeriodesEnfantHelper(periode);
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesEnfant#getDateDebut()
     */
    @Override
    public String getDateDebut() {

        return this.getProp(ISFApercuPeriodesEnfantHelper.PROP_DATEDEBUT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesEnfant#getDateFin()
     */
    @Override
    public String getDateFin() {

        return this.getProp(ISFApercuPeriodesEnfantHelper.PROP_DATEFIN);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesEnfant#getIdEnfant()
     */
    @Override
    public String getIdEnfant() {

        return this.getProp(ISFApercuPeriodesEnfantHelper.PROP_IDENFANT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesEnfant#getIdMembreFamille()
     */
    @Override
    public String getIdMembreFamille() {

        return this.getProp(ISFApercuPeriodesEnfantHelper.PROP_IDMEMBREFAMILLE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesEnfant#getIdMembresFamilles()
     */
    @Override
    public String getIdMembresFamilles() {

        return this.getProp(ISFApercuPeriodesEnfantHelper.PROP_IDMEMBRESFAMILLES);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesEnfant#getIdPeriode()
     */
    @Override
    public String getIdPeriode() {

        return this.getProp(ISFApercuPeriodesEnfantHelper.PROP_IDPERIODE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesEnfant#getIdRequerant()
     */
    @Override
    public String getIdRequerant() {

        return this.getProp(ISFApercuPeriodesEnfantHelper.PROP_IDREQUERANT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesEnfant#getIdTiers()
     */
    @Override
    public String getIdTiers() {

        return this.getProp(ISFApercuPeriodesEnfantHelper.PROP_IDTIERS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesEnfant#getNomEnfant()
     */
    @Override
    public String getNomEnfant() {

        return this.getProp(ISFApercuPeriodesEnfantHelper.PROP_NOMENFANT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesEnfant#getNumAVS()
     */
    @Override
    public String getNumAVS() {

        return this.getProp(ISFApercuPeriodesEnfantHelper.PROP_NUMAVS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuPeriodesEnfant#getPrenomEnfant()
     */
    @Override
    public String getPrenomEnfant() {

        return this.getProp(ISFApercuPeriodesEnfantHelper.PROP_PRENOMENFANT);
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
     * @see globaz.hera.api.ISFApercuPeriodesEnfant#getType()
     */
    @Override
    public String getType() {

        return this.getProp(ISFApercuPeriodesEnfantHelper.PROP_TYPE);
    }
}
