package globaz.aquila.service.historique;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeManager;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.db.access.poursuite.COHistoriqueManager;
import globaz.aquila.util.COJournalAdapter;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class COHistoriqueService {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public static final String CS_NUM_SERIE = "5010015";
    public static final String CS_TYPE_SAISIE = "5010016";

    /**
     * Ajoute une ligne d'historique pour le contentieux et les informations données.
     * 
     * @return l'historique qui vient d'être ajouté
     */
    public COHistorique ajoutHistorique(BSession session, BTransaction transaction, COContentieux contentieux,
            String motif, String idEtapePrecedante, String idHistoriquePrecedant, COJournalAdapter journal)
            throws Exception {
        COHistorique historique = new COHistorique();

        historique.setSession(session);

        // Ajout des informations courantes à l'historique
        historique.setIdContentieux(contentieux.getIdContentieux());
        historique.setIdSequence(contentieux.getIdSequence());
        historique.setIdEtape(idEtapePrecedante);
        historique.setDateDeclenchement(contentieux.getDateDeclenchement());
        historique.setDateExecution(contentieux.getDateExecution());
        historique.setSolde(contentieux.getSolde());
        historique.setUser(session.getUserId());
        historique.setMotif(motif);
        historique.setNumPoursuite(contentieux.getNumPoursuite());
        historique.setIdHistoriquePrecedant(idHistoriquePrecedant);

        if (journal != null) {
            journal.renseignerInfosJournal(historique);
        }

        historique.add(transaction);

        return historique;
    }

    /**
     * Retourne l'historique à partir d'un libellé d'étape.
     * 
     * @param session
     * @param contentieux
     * @param libEtape
     *            Libellé de l'étape
     * @return COHistorique Historique
     * @throws Exception
     *             En cas de problème
     */
    public COHistorique getHistoriqueForLibEtape(BSession session, COContentieux contentieux, String libEtape)
            throws Exception {
        String idEtapeCherchee = "";

        // Recherche l'IdEtape à partir du libellé
        COEtapeManager etapeManager = new COEtapeManager();
        etapeManager.setSession(session);
        etapeManager.setForLibEtape(libEtape);
        etapeManager.setForIdSequence(contentieux.getIdSequence());
        etapeManager.find();

        if (etapeManager.size() > 0) {
            idEtapeCherchee = ((COEtape) etapeManager.getEntity(0)).getIdEtape();
        }

        // Recherche de l'historique
        return this.loadHistorique(session, contentieux, idEtapeCherchee);
    }

    /**
     * Retourne l'historique à partir d'un libellé d'étape.
     * 
     * @param session
     * @param contentieux
     * @param libEtape
     *            Libellé de l'étape
     * @param typeSaisie
     *            (mobilière, immobilière)
     * @return COHistorique Historique
     * @throws Exception
     *             En cas de problème
     */
    public COHistorique getHistoriqueForLibEtapeTypeSaisie(BSession session, COContentieux contentieux,
            String libEtape, String typeSaisie) throws Exception {
        String idEtapeCherchee = "";

        // Recherche l'IdEtape à partir du libellé
        COEtapeManager etapeManager = new COEtapeManager();
        etapeManager.setSession(session);
        etapeManager.setForLibEtape(libEtape);
        etapeManager.setForIdSequence(contentieux.getIdSequence());
        etapeManager.find();

        if (etapeManager.size() > 0) {
            idEtapeCherchee = ((COEtape) etapeManager.getEntity(0)).getIdEtape();
        }

        // Recherche de l'historique
        return loadHistoriqueTypeSaisie(session, contentieux, idEtapeCherchee, typeSaisie);
    }

    /**
     * charge la dernière ligne de l'historique pour ce contentieux.
     * 
     * @param session
     * @param contentieux
     * @param idEtape
     * @return la dernière ligne de l'historique ou null s'il n'y en a pas.
     * @throws Exception
     */
    public COHistorique loadHistorique(BSession session, COContentieux contentieux, String idEtape) throws Exception {
        return this.loadHistorique(session, contentieux, idEtape, Boolean.FALSE, Boolean.TRUE);
    }

    public COHistorique loadHistorique(BSession session, COContentieux contentieux, String idEtape,
            Boolean afficheEtapesAnnulees, Boolean cacheImputerVersement) throws Exception {
        COHistoriqueManager historiques = new COHistoriqueManager();

        historiques.setForIdContentieux(contentieux.getIdContentieux());
        historiques.setForIdEtape(idEtape);
        historiques.setSession(session);
        historiques.setAfficheEtapesAnnulees(afficheEtapesAnnulees);
        historiques.setCacheImputerVersement(cacheImputerVersement);
        historiques.find();

        if (historiques.isEmpty()) {
            return null;
        } else {
            // retourne le dernier historique, c'est-a-dire le plus recent pour
            // cette etape
            return (COHistorique) historiques.get(historiques.size() - 1);
        }
    }

    /**
     * Charge l'historique sans tenir compte des étapes avec la case IgnorerDateExecution cochée.
     * 
     * @param session
     * @param contentieux
     * @return l'historique sans tenir compte des étapes avec la case IgnorerDateExecution cochée
     * @throws Exception
     */
    public COHistorique loadHistoriqueIgnorerDateExecution(BSession session, COContentieux contentieux)
            throws Exception {
        COHistoriqueManager historiques = new COHistoriqueManager();

        historiques.setForIdContentieux(contentieux.getIdContentieux());
        historiques.setSession(session);
        historiques.setIgnorerDateExecution(Boolean.TRUE);
        historiques.setAfficheEtapesAnnulees(Boolean.FALSE);
        historiques.setCacheImputerVersement(Boolean.TRUE);
        historiques.find();

        if (historiques.isEmpty()) {
            return null;
        } else {
            // retourne le dernier historique, c'est-a-dire le plus recent pour
            // cette etape
            return (COHistorique) historiques.get(historiques.size() - 1);
        }
    }

    /**
     * charge la dernière ligne de l'historique pour ce contentieux.
     * 
     * @param session
     * @param contentieux
     * @param afficheEtapesAnnulees
     * @param cacheImputerVersement
     * @return la dernière ligne de l'historique ou null s'il n'y en a pas.
     * @throws Exception
     */
    public COHistorique loadHistoriqueNotForEtapeSpecifique(BSession session, COContentieux contentieux,
            Boolean afficheEtapesAnnulees, Boolean cacheImputerVersement) throws Exception {
        COHistoriqueManager historiques = new COHistoriqueManager();

        historiques.setForIdContentieux(contentieux.getIdContentieux());
        historiques.setSession(session);
        historiques.setAfficheEtapesAnnulees(afficheEtapesAnnulees);
        historiques.setCacheImputerVersement(cacheImputerVersement);
        historiques.setNotForTypeEtape(ICOEtape.CS_TYPE_SPECIFIQUE);
        historiques.find();

        if (historiques.isEmpty()) {
            return null;
        } else {
            // retourne le dernier historique, c'est-a-dire le plus recent pour
            // cette etape
            return (COHistorique) historiques.get(historiques.size() - 1);
        }
    }

    /**
     * charge la dernière ligne de l'historique pour ce contentieux.
     * 
     * @param session
     * @param contentieux
     * @param idEtape
     * @param typeSaisie
     *            (mobilière, immobilière)
     * @return la dernière ligne de l'historique ou null s'il n'y en a pas.
     * @throws Exception
     */
    public COHistorique loadHistoriqueTypeSaisie(BSession session, COContentieux contentieux, String idEtape,
            String typeSaisie) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(idEtape)) {
            return new COHistorique();
        }

        COHistoriqueManager historiques = new COHistoriqueManager();

        historiques.setForIdContentieux(contentieux.getIdContentieux());
        historiques.setForIdEtape(idEtape);
        historiques.setSession(session);
        historiques.setAfficheEtapesAnnulees(Boolean.FALSE);
        historiques.setCacheImputerVersement(Boolean.TRUE);
        // historiques.setOrderBy(COHistoriqueManager.ORDER_BY_OEHIS_DESCENDING);
        historiques.setForTriHistorique("1"); // ORDER_BY_OEHIS
        historiques.setTriDecroissant("true"); // DESCENDING
        historiques.find();

        if (historiques.isEmpty()) {
            return null;
        } else {
            COHistorique historique;
            for (int i = 0; i < historiques.size(); i++) {
                historique = (COHistorique) historiques.getEntity(i);
                if ((historique.loadEtapeInfo(COHistoriqueService.CS_TYPE_SAISIE)).getValeur().equals(typeSaisie)) {
                    return historique;
                }
            }
            return null;
        }
    }

}
