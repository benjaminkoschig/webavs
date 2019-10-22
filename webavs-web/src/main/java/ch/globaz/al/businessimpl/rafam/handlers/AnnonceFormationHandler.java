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
import ch.globaz.al.utils.ALDateUtils;

/**
 * Gère les annonces de type Formation (code 20).
 * 
 * @author jts
 * 
 */
public class AnnonceFormationHandler extends AnnonceHandlerAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant toutes les données nécessaire à la gestion des annonces RAFam
     */
    public AnnonceFormationHandler(ContextAnnonceRafam context) {
        this.context = context;
    }

    @Override
    protected void doCreation() throws JadeApplicationException, JadePersistenceException {

        if (isCurrentAllowanceTypeActive() && !AnnoncesChangeChecker.isDateFinDroitExpire(context.getDroit().getDroitModel().getFinDroitForcee())) {

            String dateFinFormationAnticipee = ALServiceLocator.getDatesEcheanceService()
                    .getDateFinValiditeDroitCalculeeFormAnticipe(context.getDroit());

            // si date fin de droit est après fin form ant => annonce
            if (JadeDateUtil.isDateBefore(dateFinFormationAnticipee, context.getDroit().getDroitModel()
                    .getFinDroitForcee())) {

                AnnonceRafamModel annonce = ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce68a(
                        context.getDossier(), context.getDroit(), getType(), context.getEtat());

                if (ALServiceLocator.getDroitBusinessService().isFormationAnticipee(context.getDroit())) {

                    annonce.setDebutDroit(JadeDateUtil.getGlobazFormattedDate(ALDateUtils.addJourDate(
                            1,
                            ALDateUtils.getCalendarDate(ALServiceLocator.getDatesEcheanceService()
                                    .getDateFinValiditeDroitCalculeeFormAnticipe(context.getDroit()))).getTime())

                    );
                }

                if (isRadiation()) {
                    doRadiation(annonce);
                } else {
                    enregistrerAnnonce(annonce);
                }
            }
        }
    }

    @Override
    protected void doModificationStandard() throws JadeApplicationException, JadePersistenceException {

        if (isCurrentAllowanceTypeActive()) {

            String dateFinFormationAnticipee = ALServiceLocator.getDatesEcheanceService()
                    .getDateFinValiditeDroitCalculeeFormAnticipe(context.getDroit());

            // si date fin de droit est après fin form ant => annonce
            if (JadeDateUtil.isDateBefore(dateFinFormationAnticipee, context.getDroit().getDroitModel()
                    .getFinDroitForcee())) {

                AnnonceRafamModel annonce = ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce68b(
                        context.getDossier(), context.getDroit(), getType(), context.getEtat(),
                        context.getEvenementDeclencheur(), getLastAnnonce());

                if (ALServiceLocator.getDroitBusinessService().isFormationAnticipee(context.getDroit())) {

                    annonce.setDebutDroit(JadeDateUtil.getGlobazFormattedDate(ALDateUtils.addJourDate(1,
                            ALDateUtils.getCalendarDate(dateFinFormationAnticipee)).getTime())

                    );
                }

                if (isRadiation()) {
                    doRadiation(annonce);
                } else if (AnnoncesChangeChecker.hasChanged(annonce, getLastAnnonce(), context.getDossier(),
                        context.getDroit())) {
                    enregistrerAnnonce(annonce);
                }
            } else {
                if (!getLastAnnonce().isNew()) {
                    super.doAnnulation();
                }
            }
        } else {
            doAnnulation();
        }
    }

    @Override
    protected RafamFamilyAllowanceType getType() throws JadeApplicationException {
        return RafamFamilyAllowanceType.FORMATION;
    }

    @Override
    protected boolean isCurrentAllowanceTypeActive() {
        return ALCSDroit.TYPE_FORM.equals(context.getDroit().getDroitModel().getTypeDroit())
                && !context.getDroit().getDroitModel().getSupplementFnb();
    }
}
