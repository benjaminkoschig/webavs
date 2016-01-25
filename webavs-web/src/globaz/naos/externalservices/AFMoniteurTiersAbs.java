package globaz.naos.externalservices;

import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JATime;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.annonceAffilie.AFAnnonceAffilie;
import java.rmi.RemoteException;

/**
 * Classe abstraite pour les moniteurs de Tiers, permettant d'insérer toute modification dans les annonces de
 * l'affiliation.
 */
public abstract class AFMoniteurTiersAbs extends BAbstractEntityExternalService {
    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        createAnnonce(entity);
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        // System.out.println("afterUpdate "+entity.toValueObject().toString());
        createAnnonce(entity);
    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
    }

    public abstract void createAnnonce(BEntity entity) throws Exception;

    protected void createAnnonceToAff(BEntity entity, String idTiers, String typeAnnonce, String ancienneDonnee)
            throws RemoteException, Exception, FWSecurityLoginException {
        this.createAnnonceToAff(entity, idTiers, typeAnnonce, null, ancienneDonnee);
    }

    protected void createAnnonceToAff(BEntity entity, String idTiers, String typeAnnonce, String noAff,
            String ancienneDonnee) throws RemoteException, Exception, FWSecurityLoginException {
        // créer session affiliation
        BSession sessionAffiliation = (BSession) GlobazServer.getCurrentSystem()
                .getApplication(AFApplication.DEFAULT_APPLICATION_NAOS).newSession(entity.getSession());
        entity.getSession().connectSession(sessionAffiliation);
        // recherche des affiliations concernées
        AFAffiliationManager affMgr = new AFAffiliationManager();
        affMgr.setSession(sessionAffiliation);
        affMgr.setForIdTiers(idTiers);
        if (!JadeStringUtil.isEmpty(noAff)) {
            affMgr.setForAffilieNumero(noAff);
        }
        affMgr.find();
        for (int i = 0; i < affMgr.size(); i++) {
            AFAffiliation aff = (AFAffiliation) affMgr.getEntity(i);
            // ne pas prendre en compte les affiliations radiées
            if (JadeStringUtil.isEmpty(aff.getDateFin())) {
                // créer annonce
                // TODO à définir quels attributs doivent être communiqués
                AFAnnonceAffilie annonce = new AFAnnonceAffilie();
                annonce.setSession(sessionAffiliation);
                annonce.setChampModifier(typeAnnonce);
                annonce.setUtilisateur(entity.getSession().getUserName());
                annonce.setAffiliationId(aff.getAffiliationId());
                annonce.setDateEnregistrement(JACalendar.todayJJsMMsAAAA());
                annonce.setHeureEnregistrement(new JATime(JACalendar.now()).toStr(""));
                if (!JadeStringUtil.isEmpty(ancienneDonnee)) {
                    annonce.setChampAncienneDonnee(ancienneDonnee);
                }
                annonce.add();
            }
        }
    }

    @Override
    public void init(BEntity entity) throws Throwable {
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
    }
}
