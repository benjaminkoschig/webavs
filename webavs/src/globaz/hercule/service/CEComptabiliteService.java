package globaz.hercule.service;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.service.comptabilite.CEMassesSalariales;
import globaz.hercule.service.comptabilite.CEMassesSalarialesManager;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class de service sur le module de comptabilité
 * 
 * @author Sullivann Corneille
 * @since 12 février 2014
 */
public class CEComptabiliteService {

    /**
     * <p>
     * Retourn une map qui contient les masses salariales des 4 dernières années ainsi que l'année passée en paramètre.<BR>
     * Si aucune donnée, la map retournée sera vide.
     * <p>
     * 
     * Retourne une map de la forme :<br>
     * [key;value]<br>
     * [2011;1563222]<br>
     * [2010;135354]<br>
     * 
     * @param numeroAffilie
     * @param annee
     * @return Map
     * @throws HerculeException
     */
    public static Map<Integer, BigDecimal> getMasseSalariale5DernieresAnnees(BSession session,
            BTransaction transaction, String numeroAffilie, int annee) throws HerculeException {

        if (numeroAffilie == null) {
            throw new NullPointerException("Unabled to retrive masse salariale. Numero Affilié is null");
        }
        if (session == null) {
            throw new NullPointerException("Unabled to retrive masse salariale. Session is null");
        }

        // Recherche des bornes
        int anneeFin = annee;
        int anneeDebut = annee - 4;

        // Recherche des masses
        CEMassesSalarialesManager manager = new CEMassesSalarialesManager();
        manager.setAnneeDebut(anneeDebut);
        manager.setAnneeFin(anneeFin);
        manager.setNumeroAffilie(numeroAffilie);
        manager.setSession(session);

        try {
            manager.find(transaction);

        } catch (Exception e) {
            throw new HerculeException("Technical exception, unabled to retrieve masse annuel for affilie "
                    + numeroAffilie, e);
        }

        Map<Integer, BigDecimal> donnees = new HashMap<Integer, BigDecimal>();

        Iterator<CEMassesSalariales> iteMasses = manager.iterator();
        while (iteMasses.hasNext()) {
            CEMassesSalariales masses = iteMasses.next();
            donnees.put(Integer.valueOf(masses.getAnnee()), masses.getMasse());
        }

        return donnees;
    }

    /**
     * Constructeur de CEComptabiliteService
     */
    protected CEComptabiliteService() {
        throw new UnsupportedOperationException(); // prevents calls from subclass
    }
}
