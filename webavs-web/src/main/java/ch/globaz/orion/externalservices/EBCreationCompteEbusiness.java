package ch.globaz.orion.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.naos.db.affiliation.AFAffiliation;
import ch.globaz.orion.businessimpl.services.ServicesProviders;
import ch.globaz.xmlns.eb.partnerweb.PartnerWebService;
import ch.globaz.xmlns.eb.partnerweb.ProvenanceEnum;

public class EBCreationCompteEbusiness extends BAbstractEntityExternalService {

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        if ((entity != null) && (entity instanceof AFAffiliation)) {
            AFAffiliation affiliation = (AFAffiliation) entity;

            PartnerWebService partnerWebService = ServicesProviders.partnerWebServiceProvide(entity.getSession());
            partnerWebService.createAffilieAndAdminLight(affiliation.getRaisonSocialeCourt(),
                    affiliation.getAffilieNumero(), affiliation.getRaisonSociale(), ProvenanceEnum.WEBAVS);
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
    public void init(BEntity entity) throws Throwable {
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
    }

}
