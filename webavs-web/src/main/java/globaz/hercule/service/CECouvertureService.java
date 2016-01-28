package globaz.hercule.service;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.hercule.db.couverture.CECouverture;
import globaz.hercule.db.couverture.CECouvertureManager;
import globaz.hercule.exception.HerculeException;

/**
 * Class de service sur les couvertures
 * 
 * @author SCO
 * @since 2 sept. 2010
 */
public class CECouvertureService {

    /**
     * Permet la création d'une couverture
     * 
     * @param session
     * @param idAffilie
     * @param numAffilie
     * @param annee
     * @param isActive
     * @return
     * @throws HerculeException
     */
    public static CECouverture createCouverture(BSession session, String idAffilie, String numAffilie, String annee,
            boolean isActive) throws HerculeException {

        CECouverture couverture = new CECouverture();
        couverture.setSession(session);
        couverture.setAnnee(annee);
        couverture.setCouvertureActive(isActive);
        couverture.setIdAffilie(idAffilie);
        couverture.setNumAffilie(numAffilie);
        // couverture.setDateModification(CEUtils.giveToday());

        try {
            couverture.add();
        } catch (Exception e) {
            throw new HerculeException("Technical exception, unabled to add the couverture", e);
        }

        return couverture;
    }

    /**
     * Permet la suppression d'une couverture
     * 
     * @param session
     * @param idCouverture
     * @throws HerculeException
     */
    public static void deleteCouverture(BSession session, String idCouverture) throws HerculeException {

        // Récupération de la couverture
        CECouverture couverture = retrieveCouverture(session, idCouverture);

        // On la supprime
        if (couverture != null) {
            try {
                couverture.delete();
            } catch (Exception e) {
                throw new HerculeException("Technical exception, unabled to delete the couverture", e);
            }
        }
    }

    /**
     * Permet de rechercher une covuerture grace a un idAffilié<br>
     * On peut aussi restreindre la recherche sur les couvertures actives.
     * 
     * @param session
     * @param idAffilie
     * @param isActive
     * @return
     * @throws HerculeException
     */
    public static CECouverture findCouverture(BSession session, String idAffilie, boolean isActive)
            throws HerculeException {

        CECouvertureManager manager = new CECouvertureManager();
        manager.setSession(session);
        manager.setForIdAffilie(idAffilie);
        manager.setIsActif(new Boolean(isActive));

        try {
            manager.find();
        } catch (Exception e) {
            throw new HerculeException("Technical exception, unabled to find couverture", e);
        }

        if (manager.size() > 0) {
            return (CECouverture) manager.getFirstEntity();
        }

        return null;
    }

    /**
     * PErmet de savoir si une couverture est active pour un id affilié donné
     * 
     * @param session
     * @param idAffilie
     * @return
     * @throws HerculeException
     */
    public static boolean isCouvertureActiveForIdAffilie(BSession session, BTransaction transaction, String idAffilie,
            String exceptIdCouverture) throws HerculeException {

        CECouvertureManager manager = new CECouvertureManager();
        manager.setSession(session);
        manager.setNotForIdCouverture(exceptIdCouverture);
        manager.setForIdAffilie(idAffilie);
        manager.setIsActif(true);

        try {
            manager.find(transaction);
        } catch (Exception e) {
            throw new HerculeException("Technical exception, unabled to find couverture", e);
        }

        if (manager.size() > 0) {
            return true;
        }

        return false;
    }

    /**
     * Permet de récupérer une couverture grace a con id.<br>
     * Return null si aucune couverture n'est trouvé.
     * 
     * @param session
     * @return
     * @throws HerculeException
     */
    public static CECouverture retrieveCouverture(BSession session, String idCouverture) throws HerculeException {

        CECouverture couverture = new CECouverture();
        couverture.setSession(session);
        couverture.setIdCouverture(idCouverture);

        try {
            couverture.retrieve();
        } catch (Exception e) {
            throw new HerculeException("Technical exception, unabled to retrieve the couverture", e);
        }

        if (!couverture.isNew()) {
            return couverture;
        }

        return null;
    }

    /**
     * Peremt la mise a jour d'une couverture
     * 
     * @param session
     * @param idCouverture
     * @param annee
     * @param isActive
     * @return
     * @throws HerculeException
     */
    public static CECouverture updateCouverture(BSession session, String idCouverture, String annee, boolean isActive)
            throws HerculeException {

        // Récupération de la couverture
        CECouverture couverture = retrieveCouverture(session, idCouverture);

        // Mise a jour
        if (couverture != null) {
            couverture.setAnnee(annee);
            couverture.setCouvertureActive(isActive);
            // couverture.setDateModification(CEUtils.giveToday());
            try {
                couverture.update();
            } catch (Exception e) {
                throw new HerculeException("Technical exception, unabled to update the couverture", e);
            }

            return couverture;
        }

        return null;
    }

    /**
     * Constructeur de CEControleEmployeurService
     */
    protected CECouvertureService() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
