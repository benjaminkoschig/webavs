package globaz.naos.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.translation.CodeSystem;
import java.util.StringTokenizer;

public class AFAffiliationMerobaES extends BAbstractEntityExternalService {

    private void addPartCotPersAutreAgence(AFAffiliation affiliation) throws Exception {
        AFParticulariteAffiliation particulariteAffiliation = new AFParticulariteAffiliation();
        particulariteAffiliation.setSession(affiliation.getSession());
        particulariteAffiliation.setAffiliationId(affiliation.getAffiliationId());
        particulariteAffiliation.setParticularite(CodeSystem.PARTIC_AFFILIE_COT_PERS_AUTRE_AGENCE);
        particulariteAffiliation.setDateDebut(affiliation.getDateDebut());
        particulariteAffiliation.setDateFin(affiliation.getDateFin());
        particulariteAffiliation.add();
    }

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        if (!isAffiliationGE((AFAffiliation) entity)
                && (CodeSystem.TYPE_AFFILI_INDEP.equalsIgnoreCase(((AFAffiliation) entity).getTypeAffiliation())
                        || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equalsIgnoreCase(((AFAffiliation) entity)
                                .getTypeAffiliation()) || CodeSystem.TYPE_AFFILI_NON_ACTIF
                            .equalsIgnoreCase(((AFAffiliation) entity).getTypeAffiliation()))) {
            addPartCotPersAutreAgence((AFAffiliation) entity);
        }

    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub
    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    private boolean isAffiliationGE(AFAffiliation affiliation) throws Exception {

        // -1 : GE
        // -2 : VD
        // -3 : VS

        StringTokenizer s = new StringTokenizer(affiliation.getAffilieNumero(), "-");
        if (s.countTokens() != 2) {
            throw new Exception(affiliation.getSession().getLabel("ERREUR_FORMATTAGE_NOAFFILIE_CANTON"));
        }
        s.nextToken();
        return "1".equals(s.nextToken());

    }

    @Override
    public void validate(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

}
