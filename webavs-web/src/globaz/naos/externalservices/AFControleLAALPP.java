/*
 * Cr�� le 4 avr. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.naos.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.naos.db.affiliation.AFAffiliationViewBean;
import globaz.naos.suivi.AFSuiviAttestIP;
import globaz.naos.suivi.AFSuiviLAA;
import globaz.naos.suivi.AFSuiviLPP;

/**
 * @author ald
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class AFControleLAALPP extends BAbstractEntityExternalService {
    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        // contr�le LAA
        AFSuiviLAA suiviLAA = new AFSuiviLAA();
        if (suiviLAA.isAffiliationConcerne((AFAffiliationViewBean) entity)) {
            suiviLAA.genererControle((AFAffiliationViewBean) entity);
        }
        // contr�le LPP
        AFSuiviLPP suiviLPP = new AFSuiviLPP();
        if (suiviLPP.isAffiliationConcerne((AFAffiliationViewBean) entity)) {
            suiviLPP.genererControle((AFAffiliationViewBean) entity);
        }
        // contr�le Attestation institution de pr�voyance
        AFSuiviAttestIP attestation = new AFSuiviAttestIP();
        if (attestation.isAffiliationConcerne((AFAffiliationViewBean) entity)) {
            attestation.genererControle((AFAffiliationViewBean) entity);
        }
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        AFSuiviLAA suiviLAA = new AFSuiviLAA();
        if (suiviLAA.isAffiliationConcerne((AFAffiliationViewBean) entity)) {
            suiviLAA.genererControle((AFAffiliationViewBean) entity);
        }
        // contr�le LPP
        AFSuiviLPP suiviLPP = new AFSuiviLPP();
        if (suiviLPP.isAffiliationConcerne((AFAffiliationViewBean) entity)) {
            suiviLPP.genererControle((AFAffiliationViewBean) entity);
        }
        // contr�le Attestation institution de pr�voyance
        AFSuiviAttestIP attestation = new AFSuiviAttestIP();
        if (attestation.isAffiliationConcerne((AFAffiliationViewBean) entity)) {
            attestation.genererControle((AFAffiliationViewBean) entity);
        }
    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
        // TODO : supprimer les envoi et journalisation relatif
    }

    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
    }

    @Override
    public void init(BEntity entity) throws Throwable {
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
    }
}
