package ch.globaz.al.businessimpl.rafam.handlers;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Gère les annonces de type Formation (code 22).
 * 
 * @author jts
 * 
 */
public class AnnonceFormationAnticipeeHandler extends AnnonceHandlerAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant toutes les données nécessaire à la gestion des annonces RAFam
     */
    public AnnonceFormationAnticipeeHandler(ContextAnnonceRafam context) {
        this.context = context;
    }

    @Override
    protected void doCreation() throws JadeApplicationException, JadeApplicationException, JadePersistenceException {

        if (ALServiceLocator.getDroitBusinessService().isFormationAnticipee(context.getDroit())
                && isCurrentAllowanceTypeActive()) {

            AnnonceRafamModel annonce = ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce68a(
                    context.getDossier(), context.getDroit(), getType(), context.getEtat());

            annonce.setEcheanceDroit(ALServiceLocator.getDatesEcheanceService()
                    .getDateFinValiditeDroitCalculeeFormAnticipe(context.getDroit()));

            if (isRadiation()) {
                doRadiation(annonce);
            } else {
                enregistrerAnnonce(annonce);
            }
        }
    }

    @Override
    protected void doModificationStandard() throws JadeApplicationException, JadePersistenceException {

        if (!ALServiceLocator.getDroitBusinessService().isFormationAnticipee(context.getDroit())) {
            if (!getLastAnnonce().isNew()) {
                super.doAnnulation();
            }
        } else {
            if (getLastAnnonce().isNew()) {
                doCreation();
            } else if (isCurrentAllowanceTypeActive()) {

                AnnonceRafamModel annonce = ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce68b(
                        context.getDossier(), context.getDroit(), getType(), context.getEtat(),
                        context.getEvenementDeclencheur(), getLastAnnonce());

                String dateEcheanceMaxFormAnticipee = ALServiceLocator.getDatesEcheanceService()
                        .getDateFinValiditeDroitCalculeeFormAnticipe(context.getDroit());

                // si la fin du droit se termine avant les 16 ans on l'utilise, sinon on prend la date des 16 ans
                annonce.setEcheanceDroit(JadeDateUtil.isDateBefore(context.getDroit().getDroitModel()
                        .getFinDroitForcee(), dateEcheanceMaxFormAnticipee) ? context.getDroit().getDroitModel()
                        .getFinDroitForcee() : dateEcheanceMaxFormAnticipee);

                if (isRadiation()) {
                    doRadiation(annonce);
                } else if (AnnoncesChangeChecker.hasChanged(annonce, getLastAnnonce(), context.getDossier(),
                        context.getDroit())) {
                    enregistrerAnnonce(annonce);
                }
            } else {
                doAnnulation();
            }
        }
    }

    protected RafamFamilyAllowanceType getOtherType() throws JadeApplicationException {
        return RafamFamilyAllowanceType.FORMATION_ANTICIPEE_AVEC_SUPPLEMENT;
    }

    @Override
    protected RafamFamilyAllowanceType getType() throws JadeApplicationException {
        return RafamFamilyAllowanceType.FORMATION_ANTICIPEE;
    }

    @Override
    protected boolean isCurrentAllowanceTypeActive() {
        return ALCSDroit.TYPE_FORM.equals(context.getDroit().getDroitModel().getTypeDroit())
                && !context.getDroit().getDroitModel().getSupplementFnb();
    }
}
