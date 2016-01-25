package ch.globaz.pegasus.businessimpl.services.models.decision.ged;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.lot.Prestation;
import ch.globaz.pegasus.business.models.lot.PrestationSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * Classe permettant la gestion de la mise en GED des décisionApresCalcul Gére la'ppel à la construction du
 * <code>JadePublishDocumentInfo</code>
 * 
 * @version 1.12
 * @author sce
 * 
 */
public class DACGedHandler {

    private static final String CR = "\n\r";
    private static final String STAR_SEPARATOR = "*********************************************************************";

    public static DACGedHandler getInstanceForTraitementDecisions(ArrayList<String> idsDecisions, String persref) {

        if ((idsDecisions == null) || (idsDecisions.size() == 0)) {
            throw new IllegalArgumentException("The list of the decision(s) to print cannot be null or empty ["
                    + DACGedHandler.class.getName() + "]");
        }

        DACGedHandler handler = new DACGedHandler();
        handler.setIdsDacToPutInGed(idsDecisions);
        handler.setPersref(persref);

        return handler;
    }

    public static DACGedHandler getInstanceForTraitementPourLot(String idLot, String persref, BSession session,
            ArrayList<String> idDecisionsToPrint) throws PrestationException, DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, LotException {

        if (JadeStringUtil.isBlank(idLot)) {
            throw new IllegalArgumentException("The idLot cannot be null or egal to 0 ["
                    + DACGedHandler.class.getName() + "]");
        }

        DACGedHandler handler = new DACGedHandler();
        handler.setIdLot(idLot);
        handler.setPersref(persref);
        handler.setSession(session);
        handler.setIdsDacToPutInGed(idDecisionsToPrint);
        handler.setLot(CorvusServiceLocator.getLotService().read(idLot));

        return handler;
    }

    public static DACGedHandler getInstanceForTraitementPourLots(List<String> idsLots, String persref,
            BSession session, ArrayList<String> idDecisionsToPrint) throws LotException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (idsLots.size() == 0) {
            throw new IllegalArgumentException("The idLots is empty");
        }

        DACGedHandler handler = new DACGedHandler();
        handler.setPersref(persref);
        handler.setSession(session);
        handler.setIdsDacToPutInGed(idDecisionsToPrint);

        for (String idLot : idsLots) {
            handler.addLot(CorvusServiceLocator.getLotService().read(idLot));
        }

        return handler;
    }

    private JadePrintDocumentContainer container = null;

    private String idLot = null;

    private ArrayList<String> idsDacToPutInGed = null;
    private SimpleLot lot = null;
    private List<SimpleLot> lots = null;

    private String persref = null;

    private BSession session = null;

    private DACGedHandler() {
    }

    public void addLot(SimpleLot lot) {
        if (null == lots) {
            lots = new ArrayList<SimpleLot>();
        }
        lots.add(lot);
    }

    /**
     * Méthode retournant les statistiques du traitement de mise en ged
     * 
     * @return
     */
    public String generateMiseEnGedStatistiques() {
        StringBuilder msg = new StringBuilder(DACGedHandler.STAR_SEPARATOR);
        msg.append(DACGedHandler.CR);

        msg.append(idsDacToPutInGed.size());
        msg.append(" ");
        msg.append(session.getLabel("PROCESS_MAIL_GED_STATS_TITLE")).append(" ");
        msg.append(idLot);

        msg.append(DACGedHandler.CR).append("*********************************************************************");
        msg.append(DACGedHandler.CR).append(session.getLabel("PROCESS_MAIL_GES_STATS_LISTE")).append(" ");

        for (String idDecision : idsDacToPutInGed) {
            msg.append(DACGedHandler.CR);
            msg.append(idDecision);
        }

        return msg.toString();
    }

    public JadePrintDocumentContainer getContainer() {
        return container;
    }

    public String getIdLot() {
        return idLot;
    }

    public ArrayList<String> getIdsDacToPutInGed() {
        return idsDacToPutInGed;
    }

    /**
     * Retourne la liste des prestations pour un idLot donné
     * 
     * @param idLot
     * @return
     * @throws PrestationException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    protected ArrayList<String> getIdsPrestationsForLotDecision(String idLot) throws PrestationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        ArrayList<String> listeIdsPrestations = new ArrayList<String>();

        PrestationSearch search = new PrestationSearch();
        search.setForIdLot(idLot);

        // iteration sur les résultats
        for (JadeAbstractModel model : PegasusServiceLocator.getPrestationService().search(search).getSearchResults()) {
            listeIdsPrestations.add(((Prestation) model).getId());
        }

        return listeIdsPrestations;
    }

    public SimpleLot getLot() {
        return lot;
    }

    public String getPersref() {
        return persref;
    }

    public BSession getSession() {
        return session;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdsDacToPutInGed(ArrayList<String> idsDacToPutInGed) {
        this.idsDacToPutInGed = idsDacToPutInGed;
    }

    public void setLot(SimpleLot lot) {
        this.lot = lot;
    }

    public void setPersref(String persref) {
        this.persref = persref;
    }

    public void setSession(BSession session) {
        this.session = session;
    }
}
