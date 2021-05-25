/*
 * Créé le 15 juin 05
 */
package globaz.apg.process;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.business.service.APAnnoncesRapgService;
import globaz.apg.db.annonces.APAnnonceAPG;
import globaz.apg.db.annonces.APAnnonceAPGManager;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.properties.APProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author dde
 */
@Slf4j
public class APEnvoyerAnnoncesSedexProcess extends BProcess {

    private static final long serialVersionUID = 1L;
    private String forMoisAnneeComptable = "";
    private List<String> forGenreServiceIn = new ArrayList<>();


    /**
     * Crée une nouvelle instance de la classe APEnvoyerAnnoncesProcess.
     */
    public APEnvoyerAnnoncesSedexProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe APEnvoyerAnnoncesProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public APEnvoyerAnnoncesSedexProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe APEnvoyerAnnoncesProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public APEnvoyerAnnoncesSedexProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        List<APChampsAnnonce> annoncesToSend = new ArrayList<APChampsAnnonce>();
        HashMap<String, APAnnonceAPG> annoncesBd = new HashMap<String, APAnnonceAPG>();

        try {
            // Envoi des annonces à envoyer
            APAnnonceAPGManager mgr = new APAnnonceAPGManager();
            mgr.setSession(getSession());
            mgr.setForEtat(IAPAnnonce.CS_VALIDE);
            mgr.setForMoisAnneeComptable(forMoisAnneeComptable);
            mgr.setForTypeAnnonce(IAPAnnonce.CS_APGSEDEX);
            mgr.find(BManager.SIZE_NOLIMIT);

            Iterator<APAnnonceAPG> it = mgr.iterator();
            while (it.hasNext()) {
                APAnnonceAPG annonce = it.next();
                annoncesToSend.add(annonce.toChampsAnnonce());
                annoncesBd.put(annonce.getIdAnnonce(), annonce);
            }

            // Ajout des annonces erronées
            mgr = new APAnnonceAPGManager();
            mgr.setSession(getSession());
            mgr.setForEtat(IAPAnnonce.CS_ERRONE);
            mgr.setForTypeAnnonce(IAPAnnonce.CS_APGSEDEX);
            mgr.find(BManager.SIZE_NOLIMIT);

            it = mgr.iterator();
            while (it.hasNext()) {
                APAnnonceAPG annonce = it.next();
                annoncesToSend.add(annonce.toChampsAnnonce());
                annoncesBd.put(annonce.getIdAnnonce(), annonce);
            }

            boolean isv5 = APProperties.RAPG_ISV5.getBooleanValue().booleanValue();
            APAnnoncesRapgService service = isv5 ? globaz.apg.ApgServiceLocator.getAnnoncesRapgServiceV5() : globaz.apg.ApgServiceLocator.getAnnoncesRapgService();

            // Envoi des annonces
            annoncesToSend = service.envoyerAnnonces(annoncesToSend,
                    getMemoryLog(), getSession());

            getMemoryLog().logMessage(annoncesToSend.size() + " annonces envoyées !", FWMessage.INFORMATION,
                    this.getClass().getSimpleName());

            // Sauvegarde de l'état des annonces dans la db
            for (APChampsAnnonce champAnnonce : annoncesToSend) {
                APAnnonceAPG annonce = annoncesBd.get(champAnnonce.getMessageId());
                annonce.fromChampsAnnonce(champAnnonce);
                annonce.setEtat(IAPAnnonce.CS_ENVOYE);
                annonce.update();
            }

        } catch (Exception e) {
            try {
                getTransaction().rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }


            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel("ENVOYER_ANNONCES"));

            LOG.error("ERROR TO SEND ANNONCE",e);
            return false;
        }

        return true;
    }

    @Override
    protected String getEMailObject() {
        if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("PROCESS_ENVOI_ANNONCES_FAILED");
        } else {
            return getSession().getLabel("PROCESS_ENVOI_ANNONCES_SUCCESS");
        }
    }

    /**
     * @return the forMoisAnneeComptable
     */
    public String getForMoisAnneeComptable() {
        return forMoisAnneeComptable;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * @param forMoisAnneeComptable
     *            the forMoisAnneeComptable to set
     */
    public void setForMoisAnneeComptable(String forMoisAnneeComptable) {
        this.forMoisAnneeComptable = forMoisAnneeComptable;
    }


    public List<String> getForGenreServiceIn() {
        return forGenreServiceIn;
    }

    public void setForGenreServiceIn(final List<String> forGenreServiceIn) {
        this.forGenreServiceIn = forGenreServiceIn;
    }
}
