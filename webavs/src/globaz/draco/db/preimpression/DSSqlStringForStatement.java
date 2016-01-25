package globaz.draco.db.preimpression;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;

/**
 * @author JMC Classe qui g�n�re le sql avant la pr�-impresssion. Cela permet d'�viter les doublons entre Exception et
 *         insc. de l'ann�e pr�c�dente
 */
public class DSSqlStringForStatement extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Retourne le code sql pour �viter les doublons lors de l'impression des DS
     * 
     * @param anneeEnCours
     * @return
     */
    public String getSqlForPreImpression(String anneeEnCours) {
        String sql = "update " + _getCollection() + "ciexcp  set daeng =" + anneeEnCours
                + "0101 where idexcp in (select IDEXCP from " + _getCollection() + "ciexcp ex " + "join "
                + _getCollection() + "ciecrip ec on ( ec.kaiind = ex.kaiind ) join " + _getCollection()
                + "afaffip af on af.maiaff = ex.maiaff where	ex.maiaff = ec.kbitie and ex.daeng = " + anneeEnCours
                + "0000 and kbnann = " + String.valueOf(Integer.parseInt(anneeEnCours) - 1) + " and kbnmof = 12)";
        return sql;

    }
}
