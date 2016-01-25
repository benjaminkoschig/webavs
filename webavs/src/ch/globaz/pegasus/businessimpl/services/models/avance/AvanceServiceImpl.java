package ch.globaz.pegasus.businessimpl.services.models.avance;

import globaz.corvus.api.avances.IREAvances;
import globaz.corvus.db.avances.REAvance;
import globaz.corvus.db.avances.REAvanceManager;
import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.pegasus.business.exceptions.models.avance.AvanceException;
import ch.globaz.pegasus.business.models.avance.AvanceVo;
import ch.globaz.pegasus.business.models.avance.AvanceVoSearch;
import ch.globaz.pegasus.business.services.models.avance.AvanceService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class AvanceServiceImpl extends PegasusAbstractServiceImpl implements AvanceService {

    @Override
    public int count(AvanceVoSearch search) throws JadeApplicationException, JadePersistenceException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public AvanceVo create(AvanceVo entity) throws AvanceException {
        REAvance avance = new REAvance();
        avance.setCsDomaine(entity.getCsDomaine());
        avance.setCsDomaineAvance(IREAvances.CS_DOMAINE_AVANCE_PC);
        avance.setCsEtat1erAcompte(entity.getCsEtat1erAcompte());
        avance.setCsEtatAcomptes(entity.getCsEtatAcomptes());
        avance.setDateDebutAcompte(entity.getDateDebutAcompte());
        avance.setDateDebutPmt1erAcompte(entity.getDateDebutPmt1erAcompte());
        avance.setDateFinAcompte(entity.getDateFinAcompte());
        // avance.setDateNaissance(entity.getDateNaissance());
        avance.setDatePmt1erAcompte(entity.getDatePmt1erAcompte());
        avance.setId(entity.getId());
        avance.setIdAffilie(entity.getIdAffilie());
        avance.setIdAvance(entity.getIdAvance());
        avance.setIdTiersAdrPmt(entity.getIdTiersAdrPmt());
        avance.setIdTiersBeneficiaire(entity.getIdTiersBeneficiaire());
        avance.setLibelle(entity.getLibelle());
        avance.setMontant1erAcompte(entity.getMontant1erAcompte());
        avance.setMontantMensuel(entity.getMontantMensuel());

        try {
            avance.add();
        } catch (Exception e) {
            throw new AvanceException("Exception happened during adding avance", e);
        }

        return entity;

    }

    @Override
    public REAvance createReAvance(REAvance avance) throws Exception {

        if (avance == null) {
            throw new AvanceException("The avance model is null and coannot be updated");
        }
        // on set le domaine des pc
        avance.setCsDomaineAvance(IREAvances.CS_DOMAINE_AVANCE_PC);
        avance.add(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

        return avance;
    }

    @Override
    public AvanceVo delete(AvanceVo entity) throws JadeApplicationException, JadePersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public REAvance deleteReAvance(REAvance avance) throws AvanceException {
        try {
            avance.delete();
        } catch (Exception e) {
            throw new AvanceException("Exception happened during deleting avance", e);
        }
        return avance;
    }

    @Override
    public void executer() {

    }

    public AvanceVo[] getSearchResults(REAvanceManager mgr) {

        ArrayList<AvanceVo> forArray = new ArrayList<AvanceVo>();

        for (Object avance : mgr.getContainer()) {
            REAvance reavance = (REAvance) avance;

            // exclusion avance etat 1er acompte terminé ET montant mensuel a o OU etat mensuel terminé, etat accompte
            // mensuel terminé
            if (!reavance.getCsEtat1erAcompte().equals(IREAvances.CS_ETAT_1ER_ACOMPTE_TERMINE)
                    && !reavance.getCsEtatAcomptes().equals(IREAvances.CS_ETAT_ACOMPTE_TERMINE)) {
                AvanceVo av = new AvanceVo();
                av.setCsDomaine(reavance.getCsDomaine());
                av.setCsDomaineAvance(reavance.getCsDomaineAvance());
                av.setCsEtat1erAcompte(reavance.getCsEtat1erAcompte());
                av.setCsEtatAcomptes(reavance.getCsEtatAcomptes());
                av.setDateDebutAcompte(reavance.getDateDebutAcompte());
                av.setDateDebutPmt1erAcompte(reavance.getDateDebutPmt1erAcompte());
                av.setDateFinAcompte(reavance.getDateFinAcompte());
                av.setDateNaissance(reavance.getDateNaissance());
                av.setDatePmt1erAcompte(reavance.getDatePmt1erAcompte());
                av.setId(reavance.getId());
                av.setIdAffilie(reavance.getIdAffilie());
                av.setIdAvance(reavance.getIdAvance());
                av.setIdTiersAdrPmt(reavance.getIdTiersAdrPmt());
                av.setIdTiersBeneficiaire(reavance.getIdTiersBeneficiaire());
                av.setLibelle(reavance.getLibelle());
                av.setMontant1erAcompte(reavance.getMontant1erAcompte());
                av.setMontantMensuel(reavance.getMontantMensuel());
                av.setNationalite(reavance.getNationalite());
                av.setNom(reavance.getNom());
                av.setPrenom(reavance.getPrenom());
                av.setSpy(reavance.getSpy().toString());
                av.setNss(reavance.getNss());

                forArray.add(av);
            }
        }
        AvanceVo[] tableReturn = forArray.toArray(new AvanceVo[forArray.size()]);

        return tableReturn;

    }

    @Override
    public AvanceVo read(String idEntity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object readAvance(String idEntity) throws AvanceException {
        REAvanceManager avanceMgr = new REAvanceManager();

        try {
            avanceMgr.setForIdAvance(idEntity);
            avanceMgr.find(0);
        } catch (Exception e) {
            throw new AvanceException("Exception happened durin seraching avance", e);
        }

        REAvance avance = (REAvance) avanceMgr.getFirstEntity();

        return avance;
    }

    public REAvanceManager returnSearchManager(String idTiers) throws AvanceException {

        REAvanceManager mgr = new REAvanceManager();

        try {

            mgr.setForIdTiersIn(idTiers);
            mgr.setSession(BSessionUtil.getSessionFromThreadContext());
            mgr.find();
            for (Object a : mgr.getContainer()) {
                REAvance avance = (REAvance) a;
            }

        } catch (Exception e) {
            throw new AvanceException("Exception happened durin seraching avance", e);
        }

        return mgr;
    }

    @Override
    public AvanceVoSearch search(AvanceVoSearch search) throws JadeApplicationException, JadePersistenceException {

        AvanceVo[] avanceVos = null;
        try {
            avanceVos = getSearchResults(returnSearchManager(search.getIdTiers()));
            search.setSearchResults(avanceVos);
            search.setNbOfResultMatchingQuery(avanceVos.length);
        } catch (Exception e) {
            throw new AvanceException("Exception happened durin seraching avance", e);
        }
        return search;
    }

    @Override
    public AvanceVo update(AvanceVo entity) throws JadeApplicationException, JadePersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public REAvance updateReAvance(REAvance avance) throws AvanceException {
        try {
            avance.update();
        } catch (Exception e) {
            throw new AvanceException("Exception happened during updating avance", e);
        }
        return avance;
    }

}
