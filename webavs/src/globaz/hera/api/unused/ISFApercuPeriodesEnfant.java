package globaz.hera.api.unused;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
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
public interface ISFApercuPeriodesEnfant extends BIEntity {
    public static final String FIND_FOR_ID_REQUERANT = "setForIdRequerant";

    public Object[] find(Hashtable params) throws Exception;

    public ISFApercuPeriodesEnfant[] findPeriodes(Hashtable params) throws Exception;

    public String getDateDebut();

    public String getDateFin();

    public String getIdEnfant();

    public String getIdMembreFamille();

    public String getIdMembresFamilles();

    public String getIdPeriode();

    public String getIdRequerant();

    public String getIdTiers();

    public String getNomEnfant();

    public String getNumAVS();

    public String getPrenomEnfant();

    public String getType();

    public void retrieve(BITransaction transaction) throws Exception;

}
