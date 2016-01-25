package globaz.corvus.helpers.decisions;

import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.utils.RETiersForJspUtils;
import globaz.corvus.vb.decisions.REPreparerDecisionAvecAjournementViewBean;
import globaz.corvus.vb.decisions.REPreparerDecisionStandardViewBean;
import globaz.corvus.vb.decisions.REPreparerDecisionViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.utils.ged.PRGedUtils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import ch.globaz.corvus.business.services.CorvusCrudServiceLocator;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.constantes.CodeCasSpecialRente;
import ch.globaz.corvus.domaine.constantes.EtatDemandeRente;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * Helper pour l'action "Pr�parer d�cision" sur une demande de rente, ou un rente accord�e
 */
public class REPreparerDecisionsHelper extends PRAbstractHelper {

    /**
     * Action appel�e lorsque le point de menu "Pr�parer d�cision" est utilis�
     * 
     * @param viewBean
     *            le viewBean en provenance de l'action, celui-ci changera de type selon si la pr�paration de la
     *            d�cision est standard ou est sp�cifique (avec ajournement ou incarc�ration)
     * @param action
     *            l'action qui a �t� lanc�e
     * @param session
     *            la session utilisateur
     * @return le viewBean pr�par�
     * @throws JadeApplicationServiceNotAvailableException
     *             si les services de persistence ne sont pas d�clar�s
     */
    private FWViewBeanInterface afficherPreparation(FWViewBeanInterface viewBean, FWAction action, BSession session) {

        REPreparerDecisionViewBean prepararerDecisionViewBean = (REPreparerDecisionViewBean) viewBean;

        DemandeRente demande = CorvusCrudServiceLocator.getDemandeRenteCrudService().read(
                prepararerDecisionViewBean.getIdDemandeRente());

        boolean necessitePreparationDecisionSpecifiqueAjournement = demande
                .comporteDesRentesAccordeesAvecCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);
        boolean comporteDesRentesSansAjournement = demande
                .comporteDesRentesAccordeesSansCodeCasSpecial(CodeCasSpecialRente.CODE_CAS_SPECIAL_08);

        PersonneAVS requerant = demande.getRequerant();
        String detailRequerant = RETiersForJspUtils.getInstance(session).getDetailsTiers(requerant, false, false);

        if (necessitePreparationDecisionSpecifiqueAjournement) {
            prepararerDecisionViewBean = preparerViewBeanPourDecisionAvecAjournement(demande,
                    new REPreparerDecisionAvecAjournementViewBean(demande.getId(), detailRequerant));
        } else {
            prepararerDecisionViewBean = preparerViewBeanDecisionDeManiereStandard(demande,
                    new REPreparerDecisionStandardViewBean(demande.getId(), detailRequerant));
        }

        prepararerDecisionViewBean
                .setInterdirePreparationCarMelangeRentesAvecEtSansCodeCasSpecial(necessitePreparationDecisionSpecifiqueAjournement
                        && comporteDesRentesSansAjournement);
        prepararerDecisionViewBean.setSession(session);

        return prepararerDecisionViewBean;
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return afficherPreparation(viewBean, action, (BSession) session);
    }

    private REPreparerDecisionStandardViewBean preparerViewBeanDecisionDeManiereStandard(DemandeRente demande,
            REPreparerDecisionStandardViewBean viewBean) {

        viewBean.setDateDecision(demande.getDateTraitement());
        viewBean.setIdTiersRequerant(demande.getRequerant().getId().toString());
        viewBean.setIsDemandeCourantValide(demande.getEtat() == EtatDemandeRente.COURANT_VALIDE);

        return viewBean;
    }

    private REPreparerDecisionAvecAjournementViewBean preparerViewBeanPourDecisionAvecAjournement(DemandeRente demande,
            REPreparerDecisionAvecAjournementViewBean viewBean) {

        viewBean.setEditionDuDocument(true);
        viewBean.setDocumentAvecConfigurationPourLaGed(PRGedUtils
                .isDocumentInGed(IRENoDocumentInfoRom.LETTRE_CONFIRMATION_AJOURNEMENT));

        // la date du jour par d�faut pour le document
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        viewBean.setDateSurLeDocument(dateFormat.format(Calendar.getInstance().getTime()));
        viewBean.setAdresseEmailGestionnaire(BSessionUtil.getSessionFromThreadContext().getUserEMail());

        return viewBean;
    }
}
