package globaz.prestation.external;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.prestation.db.demandes.PRDemandeManager;
import globaz.pyxis.db.tiers.TITiersViewBean;

public class CorvusTiersUpdateRestrictionExternalService extends BAbstractEntityExternalService {

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
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
    public void beforeRetrieve(BEntity tiersViewBean) throws Throwable {
    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
        String element = "corvus.tiers";
        BSession session = entity.getSession();
        TITiersViewBean tiersViewBean = (TITiersViewBean) entity;

        PRDemandeManager demandePrestationMan = new PRDemandeManager();
        demandePrestationMan.setSession(session);
        demandePrestationMan.setForIdTiers(tiersViewBean.getIdTiers());
        int nb = demandePrestationMan.getCount();

        if (nb > 0) {
            /*
             * Si il y a une demande, on regarde encore si le champs est sous contôle, car seul les modifications sur
             * les champs suivants sont contrôler Titre, Nom et prénom, NSS, Date de naissance, Sexe, Date de décès, Nom
             * de jeune fille, Langue, Nationalité
             */
            TITiersViewBean beforeUpdateTiersValues = new TITiersViewBean();
            beforeUpdateTiersValues.setIdTiers(tiersViewBean.getIdTiers());
            beforeUpdateTiersValues.setSession(session);
            beforeUpdateTiersValues.retrieve();

            if ((!beforeUpdateTiersValues.getTitreTiers().equals(tiersViewBean.getTitreTiers()))
                    || (!beforeUpdateTiersValues.getDesignation1().equals(tiersViewBean.getDesignation1()))
                    || (!beforeUpdateTiersValues.getDesignation2().equals(tiersViewBean.getDesignation2()))
                    || (!beforeUpdateTiersValues.getNumAvsActuel().equals(tiersViewBean.getNumAvsActuel()))
                    || (!beforeUpdateTiersValues.getDateNaissance().equals(tiersViewBean.getDateNaissance()))
                    || (!beforeUpdateTiersValues.getSexe().equals(tiersViewBean.getSexe()))
                    || (!beforeUpdateTiersValues.getDateDeces().equals(tiersViewBean.getDateDeces()))
                    || (!beforeUpdateTiersValues.getNomJeuneFille().equals(tiersViewBean.getNomJeuneFille()))
                    || (!beforeUpdateTiersValues.getLangue().equals(tiersViewBean.getLangue()))
                    || (!beforeUpdateTiersValues.getPays().equals(tiersViewBean.getPays()))

            ) {
                /*
                 * Si un / plusieurs des champs testé ci-dessus à changé....
                 */
                if (!session.hasRight(element, FWSecureConstants.UPDATE)) {

                    /*
                     * ... et que l'on a pas les droits nécessaires, on lève une erreur.
                     */
                    entity.getSession().addError(
                            "La modification sur ce tiers est resteinte aux personnes ayant les droits de modification sur '"
                                    + element + "'");

                }
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
