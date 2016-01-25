package globaz.alfagest.tools;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import java.sql.ResultSet;

/**
 * @author jer
 * 
 *         Classe de méthodes utilitaires pour la facturation
 */
public class FactuUtil {

    /**
     * Méthode qui renvoie l'ID d'un tiers AVS correspondant au bénéficiaire AF
     * sur la base d'un id de dossier
     * 
     * @param idDossier
     *            L'ID du dossier lié au tiers AVS
     * @param collection
     *            La collection à laquelle appartiennent les tables
     * @param transaction
     *            Une transaction ouverte pour passer la query SQL
     * @return Id tiers AVS
     */
    public static String getIdTiersAVS(String idDossier, String collection, BSession session) throws Exception {
        String idTiersAVS = "";
        BTransaction transaction = (BTransaction) session.newTransaction();
        try {
            transaction.openTransaction();
            BStatement statement = new BStatement(transaction);
            statement.createStatement();
            ResultSet res = statement.executeQuery("SELECT AIDAVS FROM " + collection + "jafpdos LEFT OUTER JOIN "
                    + collection + "jafpadr on (EADRP=VIDADR)" + "LEFT OUTER JOIN " + collection
                    + "jafpphys on(VIDP=AID)" + "where EID=" + idDossier + ";");
            res.next();
            idTiersAVS = res.getObject(1).toString();
            statement.closeStatement();
        } finally {
            transaction.closeTransaction();
        }

        return idTiersAVS;
    }

}
