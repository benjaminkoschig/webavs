/*
 * Créé le 31 mai 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFLettreBienvenueViewBean;
import globaz.naos.translation.CodeSystem;
import ch.globaz.naos.business.constantes.AFAffiliationType;

/**
 * Permettant d'imprimer la lettre de bienvenue
 * 
 * @author sda Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFLettreBienvenue extends BAbstractEntityExternalService {

    public AFLettreBienvenue() {
        super();
    }

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        if ((entity != null) && (entity instanceof AFAffiliation)) {
            AFAffiliation aff = (AFAffiliation) entity;
            if ((!AFAffiliationType.isTypeCAPCGAS(aff.getTypeAffiliation()))
                    && (!aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_FICHIER_CENT))
                    && (!aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_BENEF_AF))
                    && (!aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_NON_SOUMIS))
                    && !(aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_NON_ACTIF)
                            && aff.getBrancheEconomique().equals(CodeSystem.BRANCHE_ECO_ETUDIANTS) && !JadeStringUtil
                                .isBlank(aff.getDateFin()))) {

                // Imprime une lettre de bienvenue à la création d'une
                // affiliation
                AFLettreBienvenueViewBean lettreBienvenue;
                lettreBienvenue = new AFLettreBienvenueViewBean();
                lettreBienvenue.setSession(entity.getSession());
                lettreBienvenue.setEMailAddress(entity.getSession().getUserEMail());
                lettreBienvenue.setIdAffiliation(aff.getAffiliationId());
                lettreBienvenue.start();
            }
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

    @Override
    public void init(BEntity arg0) throws Throwable {
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
    }

}
