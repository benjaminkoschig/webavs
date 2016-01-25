/*
 * ado Créé le 4 oct. 05
 */
package globaz.hera.api.unused;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
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
public interface ISFApercuPeriodesRequerant extends BIEntity {

    public final static String FIND_FOR_ID_REQUERANT = "setForIdRequerant";

    public Object[] find(Hashtable params) throws Exception;

    public ISFApercuPeriodesRequerant[] findPeriodes(Hashtable params) throws Exception;

    /*******************************************************************************/
    public String getDateDebut();

    public String getDateFin();

    public String getIdPeriode();

    public String getNoAvs();

    public String getType();

    public void retrieve(BITransaction transaction) throws Exception;
}
