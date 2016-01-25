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
 * Permet de recherche une date de survenance d'événement assuré pour une demande API ou d'invalidité (recherche la plus
 * petite date dans les période de la demande)
 * 
 * @author PBA
 */
public class REDateSurvenanceUtil {

    /**
     * Recherche une date de survenance de l'événement assuré dans les périodes API<br/>
     * La plus petite date est recherchée<br/>
     * <br/>
     * La date retournée au format "dd.YYYY"<br/>
     * 
     * @param session
     *            Une session utilisateur
     * @param idDemandeAPI
     *            l'ID de la demande API dont on aimerait avoir la date de survenance
     * @return La date de survenance si trouvée dans les périodes API (au format "dd.YYYY")<br/>
     *         Si aucune période n'est trouvée, retourne une chaîne vide
     * @throws Exception
     *             si une erreur survient lors de la recherche des périodes dans la base de données
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
     * Recherche une date de survenance de l'événement assuré dans les périodes d'invalidité<br/>
     * La plus petite date est recherchée<br/>
     * <br/>
     * date retournée au format "dd.YYYY"
     * 
     * @param session
     *            Une session utilisateur
     * @param idDemandeInvalidite
     *            l'ID de la demande d'invalidité dont on aimerait avoir la date de survenance
     * @return La date de survenance si trouvée dans les périodes d'invalidité (au format "dd.YYYY")<br/>
     *         Si aucune période n'est trouvée, retourne une chaîne vide
     * @throws Exception
     *             si une erreur survient lors de la recherche des périodes dans la base de données
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
