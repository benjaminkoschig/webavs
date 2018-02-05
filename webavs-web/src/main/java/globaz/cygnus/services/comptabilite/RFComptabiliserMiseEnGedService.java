package globaz.cygnus.services.comptabilite;

import globaz.cygnus.db.paiement.RFPrestation;
import globaz.cygnus.db.paiement.RFPrestationManager;
import globaz.cygnus.process.RFGenererDecisionsGedComptabilisationProcess;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import java.util.Iterator;
import java.util.Set;

/**
 * @author mbo
 */
public class RFComptabiliserMiseEnGedService {

    private String adresseMail = null;
    private String idGestionnaire = null;
    private FWMemoryLog memoryLog = new FWMemoryLog();
    private BSession session = null;
    private BTransaction transaction = null;

    public String getAdresseMail() {
        return adresseMail;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    /**
     * Methode permettant de retourner la liste des ids d�cisions contenu dans chaque prestation du lot
     * 
     * @param prestationsSet
     * @return String [] listeIds
     */
    public String[] getListeIdDecisions(Set<RFPrestationData> prestationsSet) {

        int i = 0;
        String[] listeIds = new String[prestationsSet.size()];

        for (RFPrestationData prestationCourante : prestationsSet) {
            listeIds[i] = prestationCourante.getIdDecision().toString();
            i++;
        }

        return listeIds;
    }

    public FWMemoryLog getMemoryLog() {
        return memoryLog;
    }

    public BSession getSession() {
        return session;
    }

    public BTransaction getTransaction() {
        return transaction;
    }

    /**
     * Methode qui permet de retrouver les d�cisions, et de lancer la reg�n�ration des documents pour la mise en GED
     * 
     * @throws Exception
     */
    public void miseEnGedDesDecisionsDuLot(String idLot, Set<RFPrestationData> prestationsSet) throws Exception {

        // R�cup�ration des id d�cisions contenu dans le lot
        String[] listeIdsDecisions;
        // On prend uniquement
        if (RFPropertiesUtils.miseEnGedDesDecisionsAZero()) {
            listeIdsDecisions = rechercheIDDecisionDuLot(idLot);
        } else {
            // R�cup�ration des id d�cisions contenu dans le lot
            listeIdsDecisions = new String[prestationsSet.size()];
            listeIdsDecisions = getListeIdDecisions(prestationsSet);
        }

        if ((listeIdsDecisions != null) && (listeIdsDecisions.length > 0)) {
            RFGenererDecisionsGedComptabilisationProcess process = new RFGenererDecisionsGedComptabilisationProcess();
            process.setEmailAdress(getAdresseMail());
            process.setIdGestionnaire(getIdGestionnaire());
            process.setIsMiseEnGed(true);
            process.setMemoryLog(memoryLog);
            process.setSession(getSession());
            process.setTransaction(getTransaction());
            process.setListeIdsDecisions(listeIdsDecisions);
            process.setIdLot(idLot);
            BProcessLauncher.start(process, false);
        } else {
            throw new Exception(
                    "Aucuns id decision trouv� : RFComptabiliserMiseEnGedService / miseEnGedDesDecisionsDuLot");
        }
    }

    /**
     * Methode qui permet de retrouver tous les ID d�cisions d'un lot
     * 
     * @throws Exception
     */
    private String[] rechercheIDDecisionDuLot(String idLot) throws Exception {
        RFPrestationManager prestationsDuLotMgr = new RFPrestationManager();
        prestationsDuLotMgr.setSession(getSession());
        prestationsDuLotMgr.setForIdLot(idLot);
        prestationsDuLotMgr.changeManagerSize(0);
        prestationsDuLotMgr.find();

        int i = 0;
        String[] listeIds = new String[prestationsDuLotMgr.size()];

        Iterator<RFPrestation> rfPreItr = prestationsDuLotMgr.iterator();

        while (rfPreItr.hasNext()) {
            RFPrestation rfPrestationitr = rfPreItr.next();
            listeIds[i] = rfPrestationitr.getIdDecision();
            i++;
        }

        return listeIds;
    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setMemoryLog(FWMemoryLog memoryLog) {
        this.memoryLog = memoryLog;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTransaction(BTransaction transaction) {
        this.transaction = transaction;
    }
}
