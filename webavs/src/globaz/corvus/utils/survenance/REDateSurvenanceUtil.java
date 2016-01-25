package globaz.corvus.utils.survenance;

import globaz.corvus.db.demandes.REPeriodeAPI;
import globaz.corvus.db.demandes.REPeriodeAPIManager;
import globaz.corvus.db.demandes.REPeriodeInvalidite;
import globaz.corvus.db.demandes.REPeriodeInvaliditeManager;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import java.util.Iterator;

/**
 * Permet de recherche une date de survenance d'�v�nement assur� pour une demande API ou d'invalidit� (recherche la plus
 * petite date dans les p�riode de la demande)
 * 
 * @author PBA
 */
public class REDateSurvenanceUtil {

    /**
     * Recherche une date de survenance de l'�v�nement assur� dans les p�riodes API<br/>
     * La plus petite date est recherch�e<br/>
     * <br/>
     * La date retourn�e au format "dd.YYYY"<br/>
     * 
     * @param session
     *            Une session utilisateur
     * @param idDemandeAPI
     *            l'ID de la demande API dont on aimerait avoir la date de survenance
     * @return La date de survenance si trouv�e dans les p�riodes API (au format "dd.YYYY")<br/>
     *         Si aucune p�riode n'est trouv�e, retourne une cha�ne vide
     * @throws Exception
     *             si une erreur survient lors de la recherche des p�riodes dans la base de donn�es
     */
    public static String getSurvenancePeriodeAPI(BISession session, String idDemandeAPI) throws Exception {
        REPeriodeAPIManager manager = new REPeriodeAPIManager();
        manager.setSession((BSession) session);
        manager.setForIdDemandeRente(idDemandeAPI);
        manager.find();

        String dateLaPlusPetite = "31.12.2999";
        for (Iterator<REPeriodeAPI> iterator = manager.iterator(); iterator.hasNext();) {
            REPeriodeAPI periode = iterator.next();
            if (JadeDateUtil.isDateBefore(periode.getDateDebutInvalidite(), dateLaPlusPetite)) {
                dateLaPlusPetite = periode.getDateDebutInvalidite();
            }
        }
        if ("31.12.2999".equals(dateLaPlusPetite)) {
            return "";
        } else if (JadeDateUtil.isGlobazDate(dateLaPlusPetite)) {
            return dateLaPlusPetite.substring(3);
        } else {
            return dateLaPlusPetite;
        }
    }

    /**
     * Recherche une date de survenance de l'�v�nement assur� dans les p�riodes d'invalidit�<br/>
     * La plus petite date est recherch�e<br/>
     * <br/>
     * date retourn�e au format "dd.YYYY"
     * 
     * @param session
     *            Une session utilisateur
     * @param idDemandeInvalidite
     *            l'ID de la demande d'invalidit� dont on aimerait avoir la date de survenance
     * @return La date de survenance si trouv�e dans les p�riodes d'invalidit� (au format "dd.YYYY")<br/>
     *         Si aucune p�riode n'est trouv�e, retourne une cha�ne vide
     * @throws Exception
     *             si une erreur survient lors de la recherche des p�riodes dans la base de donn�es
     */
    public static String getSurvenancePeriodeInvalidite(BISession session, String idDemandeInvalidite) throws Exception {
        REPeriodeInvaliditeManager manager = new REPeriodeInvaliditeManager();
        manager.setSession((BSession) session);
        manager.setForIdDemandeRente(idDemandeInvalidite);
        manager.find();

        String dateLaPlusPetite = "31.12.2999";
        for (Iterator<REPeriodeInvalidite> iterator = manager.iterator(); iterator.hasNext();) {
            REPeriodeInvalidite periode = iterator.next();
            if (JadeDateUtil.isDateBefore(periode.getDateDebutInvalidite(), dateLaPlusPetite)) {
                dateLaPlusPetite = periode.getDateDebutInvalidite();
            }
        }
        if ("31.12.2999".equals(dateLaPlusPetite)) {
            return "";
        } else if (JadeDateUtil.isGlobazDate(dateLaPlusPetite)) {
            return dateLaPlusPetite.substring(3);
        } else {
            return dateLaPlusPetite;
        }
    }
}
