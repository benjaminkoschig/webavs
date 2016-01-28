package globaz.hercule.service;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.service.comptesIndividuels.CENombreCI;
import globaz.hercule.service.comptesIndividuels.CENombreCIManager;
import globaz.pavo.db.compte.CIEcritureCounter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Service d'accés au service des comptes individuelles
 * 
 * @author Sullivann Corneille
 * @since 13 déc. 2010
 */
public class CEComptesIndividuelsService {

    /**
     * Permet de récupérer le nombre d'inscription au CI
     * 
     * @param session
     * @param transaction
     * @param idAffilie
     * @param annee
     * @return
     */
    public static int retrieveNombreInscription(BSession session, BTransaction transaction, String idAffilie,
            String annee) {

        int nbrCI = 0;

        CIEcritureCounter manager = new CIEcritureCounter();
        manager.setSession(session);
        manager.setForAnnee(annee);
        manager.setForEmployeur(idAffilie);

        try {
            nbrCI = manager.getCount(transaction);
        } catch (Exception e) {
            nbrCI = 0;
        }

        return nbrCI;
    }

    /**
     * <p>
     * Retourn une map qui contient le nombre de CI des 4 dernières années ainsi que l'année passée en paramètre.<BR>
     * Si aucune donnée, la map retournée sera vide.
     * <p>
     * 
     * Retourne une map de la forme :<br>
     * [key;value]<br>
     * [2011;150]<br>
     * [2010;15]<br>
     * 
     * @param session
     * @param transaction
     * @param numeroAffilie
     * @param annee
     * @return
     * @throws HerculeException
     */
    public static Map<Integer, BigDecimal> getNombreCI5DernieresAnnees(BSession session, BTransaction transaction,
            String numeroAffilie, int annee) throws HerculeException {
        if (numeroAffilie == null) {
            throw new NullPointerException("Unabled to retrive nombre CI . Numero Affilié is null");
        }
        if (session == null) {
            throw new NullPointerException("Unabled to retrive nombre CI . Session is null");
        }

        // Recherche des bornes
        int anneeFin = annee;
        int anneeDebut = annee - 4;

        // Recherche des masses
        CENombreCIManager manager = new CENombreCIManager();
        manager.setAnneeDebut(anneeDebut);
        manager.setAnneeFin(anneeFin);
        manager.setNumeroAffilie(numeroAffilie);
        manager.setSession(session);

        try {
            manager.find(transaction);

        } catch (Exception e) {
            throw new HerculeException("Technical exception, unabled to retrieve nombre CI for affilie "
                    + numeroAffilie + " and anneeDeb = " + anneeDebut + " and anneeFin = " + anneeFin, e);
        }

        Map<Integer, BigDecimal> donnees = new HashMap<Integer, BigDecimal>();

        Iterator<CENombreCI> iteNombreCI = manager.iterator();
        while (iteNombreCI.hasNext()) {
            CENombreCI nbCI = iteNombreCI.next();
            donnees.put(Integer.valueOf(nbCI.getAnnee()), nbCI.getNombreCI());
        }

        return donnees;
    }

    /**
     * Constructeur de CEComptesIndividuelsService
     */
    protected CEComptesIndividuelsService() {
        throw new UnsupportedOperationException(); // prevents calls from subclass
    }
}
