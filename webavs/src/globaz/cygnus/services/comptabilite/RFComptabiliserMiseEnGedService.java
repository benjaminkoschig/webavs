package globaz.cygnus.services.comptabilite;

import globaz.cygnus.process.RFGenererDecisionsGedComptabilisationProcess;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
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
        String[] listeIdsDecisions = new String[prestationsSet.size()];
        listeIdsDecisions = getListeIdDecisions(prestationsSet);

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
