package globaz.vulpecula.vb.registre;

import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.registre.ConventionQualification;
import ch.globaz.vulpecula.external.models.pyxis.CSTiers;
import ch.globaz.vulpecula.web.application.PTActions;
import ch.globaz.vulpecula.web.gson.ConventionQualificationGSON;

public class PTQualificationAjaxViewBean extends PTAbstractAdministrationAjaxViewBean {

    private List<ConventionQualificationGSON> qualificationsGSON;

    public PTQualificationAjaxViewBean() {
        super(CSTiers.CS_TIERS_GENRE_ADMINISTRATION_CONVENTION);
        qualificationsGSON = new ArrayList<ConventionQualificationGSON>();
    }

    @Override
    public String getDestination(final String idEntity) {
        return "/vulpecula?userAction=" + PTActions.QUALIFICATION_AJAX + ".afficherAJAX&idEntity=" + idEntity;
    }

    public List<ConventionQualificationGSON> getQualificationsGSON() {
        return qualificationsGSON;
    }

    public ConventionQualification mergeObject(final ConventionQualification conventionQualification,
            final ConventionQualification conventionQualification2) {
        conventionQualification.setId(conventionQualification2.getId());
        conventionQualification.setIdConvention(conventionQualification2.getIdConvention());
        conventionQualification.setPersonnel(conventionQualification2.getPersonnel());
        conventionQualification.setQualification(conventionQualification2.getQualification());
        conventionQualification.setTypeQualification(conventionQualification2.getTypeQualification());
        return conventionQualification;
    }

    @Override
    public void retrieve() throws Exception {
        super.retrieve();
        List<ConventionQualification> qualifications = VulpeculaRepositoryLocator
                .getConventionQualificationRepository().findByIdConvention(getId());
        for (ConventionQualification conventionQualification : qualifications) {
            ConventionQualificationGSON conventionQualificationGSON = new ConventionQualificationGSON();
            conventionQualificationGSON.id = conventionQualification.getId();
            conventionQualificationGSON.personnel = conventionQualification.getPersonnel().getValue();
            conventionQualificationGSON.qualification = conventionQualification.getQualification().getValue();
            conventionQualificationGSON.typeQualification = conventionQualification.getTypeQualification().getValue();
            qualificationsGSON.add(conventionQualificationGSON);
        }
    }

    public void setQualificationsGSON(final List<ConventionQualificationGSON> qualificationsGSON) {
        this.qualificationsGSON = qualificationsGSON;
    }

    @Override
    public void update() throws Exception {
        // Transformer le container en Simple et persister
        for (ConventionQualificationGSON conventionQualificationGSON : qualificationsGSON) {
            ConventionQualification conventionQualification = conventionQualificationGSON.convertToDomain();
            if (!JadeStringUtil.isBlank(conventionQualificationGSON.statut)) {
                if ("modified".equals(conventionQualificationGSON.statut)) {
                    ConventionQualification conventionQualificationFromDB = VulpeculaRepositoryLocator
                            .getConventionQualificationRepository().findById(conventionQualification.getId());
                    conventionQualification = mergeObject(conventionQualificationFromDB, conventionQualification);
                    VulpeculaRepositoryLocator.getConventionQualificationRepository().update(conventionQualification);
                } else if ("new".equals(conventionQualificationGSON.statut)) {
                    VulpeculaRepositoryLocator.getConventionQualificationRepository().create(conventionQualification);
                } else if ("deleted".equals(conventionQualificationGSON.statut)) {
                    ConventionQualification conventionQualificationFromDB = VulpeculaRepositoryLocator
                            .getConventionQualificationRepository().findById(conventionQualification.getId());
                    conventionQualification = mergeObject(conventionQualificationFromDB, conventionQualification);
                    VulpeculaRepositoryLocator.getConventionQualificationRepository().delete(conventionQualification);
                }
            }
        }
    }
}
