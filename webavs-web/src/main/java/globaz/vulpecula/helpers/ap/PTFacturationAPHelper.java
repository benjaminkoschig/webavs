package globaz.vulpecula.helpers.ap;

import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.association.AssociationCotisationService;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.association.AssociationGenre;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.process.ap.FacturationAssociationsProfessionnellesProcess;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.jade.client.util.JadeStringUtil;
import globaz.vulpecula.vb.ap.PTFacturationAPViewBean;

public class PTFacturationAPHelper extends FWHelper {
    private static final String GENRE_ASSOCIATION_PROFESSIONNELLE = "68900004";
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {

            PTFacturationAPViewBean vb = (PTFacturationAPViewBean) viewBean;

            FacturationAssociationsProfessionnellesProcess process = new FacturationAssociationsProfessionnellesProcess();
            process.setIdAssociationProfessionnelle(vb.getIdAssociationProfessionnelle());
            process.setIdEmployeur(vb.getIdEmployeur());
            process.setIdPassage(vb.getIdPassage());
            process.setAnnee(vb.getAnnee());
            process.setGenre(getGenreCoti(vb.getIdEmployeur(), vb.getIdAssociationProfessionnelle()));
            process.setReplaceFacture(vb.getReplaceFacture());
            process.setEMailAddress(vb.getEmail());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }

    private GenreCotisationAssociationProfessionnelle getGenreCoti(String idEmployeur,
            String idAssociationProfessionnelle) {
        if (!JadeStringUtil.isBlank(idEmployeur)) {
            AssociationCotisationService assCotService = VulpeculaServiceLocator.getAssociationCotisationService();
            Map<AssociationGenre, List<AssociationCotisation>> tmpCotis = assCotService
                    .getCotisationByAssociation(idEmployeur);
            AssociationCotisation validAssociation = null;
            for (List<AssociationCotisation> lstAssociations : tmpCotis.values()) {
                if (lstAssociations.isEmpty()) {
                    return GenreCotisationAssociationProfessionnelle.AUCUN;
                } else {
                    for (AssociationCotisation association : lstAssociations) {
                        if (association.getPeriode().isActif()) {
                            validAssociation = association;
                            break;
                        }
                    }
                }
            }
            return validAssociation == null ? GenreCotisationAssociationProfessionnelle.AUCUN : validAssociation
                    .isMembre() ? GenreCotisationAssociationProfessionnelle.MEMBRE
                            : GenreCotisationAssociationProfessionnelle.NON_MEMBRE;

        } else if (!JadeStringUtil.isBlank(idAssociationProfessionnelle)) {
            Administration admin = VulpeculaRepositoryLocator.getAdministrationRepository().findById(idAssociationProfessionnelle);
            if(GENRE_ASSOCIATION_PROFESSIONNELLE.equals(admin.getGenre())) {
                return GenreCotisationAssociationProfessionnelle.MEMBRE;
            }else {
                return GenreCotisationAssociationProfessionnelle.NON_MEMBRE;
            }
        }
        return GenreCotisationAssociationProfessionnelle.AUCUN;
    }
}
