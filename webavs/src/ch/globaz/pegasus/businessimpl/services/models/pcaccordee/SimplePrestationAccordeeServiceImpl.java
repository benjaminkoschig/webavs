/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordeesSearch;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimplePrestationAccordeeService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.utils.OldPersistence;
import ch.globaz.pyxis.common.Messages;

/**
 * @author ECO
 * 
 */
public class SimplePrestationAccordeeServiceImpl extends PegasusAbstractServiceImpl implements
        SimplePrestationAccordeeService {

    private enum OPERATION {
        ADD,
        DELETE,
        UPDATE
    };

    private SimplePrestationsAccordees _adapt(REPrestationsAccordees bean) throws PCAccordeeException {
        SimplePrestationsAccordees model = new SimplePrestationsAccordees(); // destination

        // Préconditions
        if (bean == null) {
            // Préventif, ne devrait jamais arriver...
            throw new PCAccordeeException(Messages.MAPPING_ERROR + " - " + this.getClass().getName() + "._adapt(...)");
        }

        // Mapping des données
        model.setId(bean.getId());
        model.setCsGenre(bean.getCsGenre());
        model.setIdTiersBeneficiaire(bean.getIdTiersBeneficiaire());
        model.setCodePrestation(bean.getCodePrestation());
        model.setIdInfoCompta(bean.getIdInfoCompta());
        model.setMontantPrestation(bean.getMontantPrestation());
        model.setReferencePmt(bean.getReferencePmt());
        model.setIsRetenues(bean.getIsRetenues());
        model.setIsPrestationBloquee(bean.getIsPrestationBloquee());
        model.setDateDebutDroit(bean.getDateDebutDroit());
        model.setDateFinDroit(bean.getDateFinDroit());
        model.setCsEtat(bean.getCsEtat());
        model.setIsErreur(bean.getIsErreur());
        model.setDateEcheance(bean.getDateEcheance());
        model.setFractionRente(bean.getFractionRente());
        model.setIdDemandePrincipaleAnnulante(bean.getIdDemandePrincipaleAnnulante());
        model.setIdCalculInteretMoratoire(bean.getIdCalculInteretMoratoire());
        model.setIdEnteteBlocage(bean.getIdEnteteBlocage());
        model.setIsAttenteMajBlocage(bean.getIsAttenteMajBlocage());
        model.setIsAttenteMajRetenue(bean.getIsAttenteMajRetenue());
        model.setTypeDeMiseAJours(bean.getTypeDeMiseAJours());
        model.setReferencePmt(bean.getReferencePmt());
        model.setSousCodePrestation(bean.getSousTypeGenrePrestation());
        // TODO check if spy from BEntity to jademodel spy
        model.setSpy(bean.getSpy().getFullData());
        return model;
    }

    private REPrestationsAccordees _adapt(SimplePrestationsAccordees model) throws PCAccordeeException {
        REPrestationsAccordees bean = new REPrestationsAccordees(); // destination

        // Préconditions
        if (model == null) {
            // Préventif, ne devrait jamais arriver...
            throw new PCAccordeeException(Messages.MAPPING_ERROR + " - " + this.getClass().getName() + "._adapt(...)");
        }

        // Mapping des données
        bean.setId(model.getId());
        bean.setCsGenre(model.getCsGenre());
        bean.setIdTiersBeneficiaire(model.getIdTiersBeneficiaire());
        bean.setCodePrestation(model.getCodePrestation());
        bean.setIdInfoCompta(model.getIdInfoCompta());
        bean.setMontantPrestation(model.getMontantPrestation());
        bean.setReferencePmt(model.getReferencePmt());
        bean.setIsRetenues(model.getIsRetenues());
        bean.setIsPrestationBloquee(model.getIsPrestationBloquee());
        bean.setDateDebutDroit(model.getDateDebutDroit());
        if (JadeStringUtil.isEmpty(model.getDateFinDroit())) {
            bean.setDateFinDroit("0");
        } else {
            bean.setDateFinDroit(model.getDateFinDroit());
        }
        bean.setCsEtat(model.getCsEtat());
        bean.setIsErreur(model.getIsErreur());
        bean.setDateEcheance(model.getDateEcheance());
        bean.setFractionRente(model.getFractionRente());
        bean.setIdDemandePrincipaleAnnulante(model.getIdDemandePrincipaleAnnulante());
        bean.setIdCalculInteretMoratoire(model.getIdCalculInteretMoratoire());
        bean.setIdEnteteBlocage(model.getIdEnteteBlocage());
        bean.setIsAttenteMajBlocage(model.getIsAttenteMajBlocage());
        bean.setIsAttenteMajRetenue(model.getIsAttenteMajRetenue());
        bean.setTypeDeMiseAJours(model.getTypeDeMiseAJours());
        bean.setReferencePmt(model.getReferencePmt());
        bean.setSousTypeGenrePrestation(model.getSousCodePrestation());

        bean.populateSpy(model.getSpy());
        return bean;
    }

    private SimplePrestationsAccordees _perform(final OPERATION operation, final SimplePrestationsAccordees model)
            throws PCAccordeeException {
        final SimplePrestationAccordeeServiceImpl service = this;

        OldPersistence<SimplePrestationsAccordees> pers = new OldPersistence<SimplePrestationsAccordees>() {
            @Override
            public SimplePrestationsAccordees action() throws PCAccordeeException {
                if (model == null) {
                    throw new PCAccordeeException(Messages.MODEL_IS_NULL + " - PersonneAvsServiceImpl." + operation
                            + "(...)");
                }
                // Execution de l'opération d'ajout ou de mise à jour
                REPrestationsAccordees bean = null;
                SimplePrestationsAccordees modelReturn = null;
                try {
                    bean = service._adapt(model);

                    switch (operation) {
                        case ADD:
                            bean.add();
                            modelReturn = service._adapt(bean);
                            break;

                        case UPDATE:
                            bean.update();
                            modelReturn = service._adapt(bean);
                            break;

                        case DELETE:
                            bean.delete();
                            break;

                    }

                } catch (Exception e) {
                    throw new PCAccordeeException(Messages.TECHNICAL, e);
                }

                return modelReturn;
            }
        };
        try {
            // throw new PCAccordeeException("stop");
            return pers.execute();
        } catch (Exception e) {
            throw (PCAccordeeException) e;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.pcaccordee. PrestationAccordeeService
     * #count(ch.globaz.corvus.business.models.rentesaccordees .SimplePrestationsAccordeesSearch)
     */
    @Override
    public int count(SimplePrestationsAccordeesSearch search) throws JadePersistenceException, JadeApplicationException {
        if (search == null) {
            throw new PCAccordeeException("Unable to count prestationsAccordees, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.pcaccordee. PrestationAccordeeService
     * #create(ch.globaz.corvus.business.models.rentesaccordees .SimplePrestationsAccordees)
     */
    @Override
    public SimplePrestationsAccordees create(SimplePrestationsAccordees simplePrestationAccordee)
            throws JadePersistenceException, JadeApplicationException {
        return _perform(OPERATION.ADD, simplePrestationAccordee);
    }

    @Override
    public SimplePrestationsAccordees delete(SimplePrestationsAccordees simplePrestationAccordee)
            throws JadePersistenceException, JadeApplicationException {
        if (simplePrestationAccordee.get_isRetenues().equalsIgnoreCase("1")) {
            CorvusServiceLocator.getSimpleRetenuePayementService().deleteByIdPrestationAccordee(
                    simplePrestationAccordee.getIdPrestationAccordee());

        }

        SimplePrestationsAccordees simplePrestationsAccordees = _perform(OPERATION.DELETE, simplePrestationAccordee);

        return simplePrestationsAccordees;
    }

    @Override
    public int delete(SimplePrestationsAccordeesSearch simplePrestationAccordeeSearch) throws JadePersistenceException,
            JadeApplicationException {
        if (simplePrestationAccordeeSearch == null) {
            throw new PCAccordeeException("Could not find prestationsAccordees, the search model passed is null!");
        }

        return JadePersistenceManager.delete(simplePrestationAccordeeSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.pcaccordee. PrestationAccordeeService
     * #find(ch.globaz.corvus.business.models.rentesaccordees .SimplePrestationsAccordeesSearch)
     */
    @Override
    public SimplePrestationsAccordeesSearch find(SimplePrestationsAccordeesSearch searchModel)
            throws JadePersistenceException, JadeApplicationException {
        if (searchModel == null) {
            throw new PCAccordeeException("Could not find prestationsAccordees, the search model passed is null!");
        }
        return (SimplePrestationsAccordeesSearch) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.pcaccordee. PrestationAccordeeService#read(java.lang.String)
     */
    @Override
    public SimplePrestationsAccordees read(String idPrestation) throws JadePersistenceException,
            JadeApplicationException {
        SimplePrestationsAccordees model = new SimplePrestationsAccordees();
        model.setId(idPrestation);
        return (SimplePrestationsAccordees) JadePersistenceManager.read(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.pcaccordee. PrestationAccordeeService
     * #update(ch.globaz.corvus.business.models.rentesaccordees .SimplePrestationsAccordees)
     */
    @Override
    public SimplePrestationsAccordees update(SimplePrestationsAccordees simplePrestationAccordee)
            throws JadePersistenceException, JadeApplicationException {
        return _perform(OPERATION.UPDATE, simplePrestationAccordee);
    }

}
