package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pyxis.util.TIIbanFormater;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CompteBancaireCCPException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.fortuneusuelle.CompteBancaireCCP;
import ch.globaz.pegasus.business.models.fortuneusuelle.CompteBancaireCCPSearch;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.CompteBancaireCCPService;
import ch.globaz.pegasus.business.vo.donneeFinanciere.IbanCheckResultVO;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pyxis.business.model.BanqueComplexModel;
import ch.globaz.pyxis.business.model.BanqueSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class CompteBancaireCCPServiceImpl extends PegasusAbstractServiceImpl implements CompteBancaireCCPService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.fortuneusuelle.CompteBancaireCCPService#checkChIban(java.lang.String)
     */
    @Override
    public IbanCheckResultVO checkChIban(String chIban) throws JadePersistenceException, JadeApplicationException {

        IbanCheckResultVO result = new IbanCheckResultVO();

        TIIbanFormater ibanFormatter = new TIIbanFormater();

        chIban = ibanFormatter.unformat(chIban);

        if ((!JadeStringUtil.isEmpty(chIban)) && chIban.startsWith("CH")) {

            result.setIsCheckable(Boolean.TRUE);

            // si l'iban est checkable, on le format meme si il n'est pas valide. ceci pour faciliter la correction
            result.setFormattedIban(ibanFormatter.format(chIban));

            if (TIBusinessServiceLocator.getIBANService().checkIBANforCH(chIban)) {

                result.setIsValidChIban(Boolean.TRUE);

                String noClearing = chIban.substring(4, 9);

                BanqueSearchComplexModel banqueSearchModel = new BanqueSearchComplexModel();
                banqueSearchModel.setForClearing(noClearing);
                banqueSearchModel.setDefinedSearchSize(1);
                banqueSearchModel = TIBusinessServiceLocator.getBanqueService().find(banqueSearchModel);

                if (banqueSearchModel.getSize() == 1) {
                    BanqueComplexModel banque = (BanqueComplexModel) banqueSearchModel.getSearchResults()[0];

                    result.setBankDescription(banque.getTiersBanque().getDesignation1() + " "
                            + banque.getTiersBanque().getDesignation2());
                }

            }
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CompteBancaireCCPService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .CompteBancaireCCPSearch)
     */
    @Override
    public int count(CompteBancaireCCPSearch search) throws CompteBancaireCCPException, JadePersistenceException {
        if (search == null) {
            throw new CompteBancaireCCPException("Unable to count CompteBancaireCCP, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CompteBancaireCCPService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .CompteBancaireCCP)
     */
    @Override
    public CompteBancaireCCP create(CompteBancaireCCP compteBancaireCCP) throws JadePersistenceException,
            CompteBancaireCCPException, DonneeFinanciereException {
        if (compteBancaireCCP == null) {
            throw new CompteBancaireCCPException("Unable to create CompteBancaireCCP, the model passed is null!");
        }

        try {
            compteBancaireCCP.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            compteBancaireCCP.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleCompteBancaireCCPService().create(
                    compteBancaireCCP.getSimpleCompteBancaireCCP());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CompteBancaireCCPException("Service not available - " + e.getMessage());
        }

        return compteBancaireCCP;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CompteBancaireCCPService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .CompteBancaireCCP)
     */
    @Override
    public CompteBancaireCCP delete(CompteBancaireCCP compteBancaireCCP) throws CompteBancaireCCPException,
            JadePersistenceException {
        if (compteBancaireCCP == null) {
            throw new CompteBancaireCCPException("Unable to delete CompteBancaireCCP, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleCompteBancaireCCPService().delete(
                    compteBancaireCCP.getSimpleCompteBancaireCCP());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CompteBancaireCCPException("Service not available - " + e.getMessage());
        }

        return compteBancaireCCP;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CompteBancaireCCPService#read(java.lang.String)
     */
    @Override
    public CompteBancaireCCP read(String idCompteBancaireCCP) throws JadePersistenceException,
            CompteBancaireCCPException {
        if (JadeStringUtil.isEmpty(idCompteBancaireCCP)) {
            throw new CompteBancaireCCPException("Unable to read CompteBancaireCCP, the id passed is null!");
        }
        CompteBancaireCCP CompteBancaireCCP = new CompteBancaireCCP();
        CompteBancaireCCP.setId(idCompteBancaireCCP);
        return (CompteBancaireCCP) JadePersistenceManager.read(CompteBancaireCCP);
    }

    /**
     * Chargement d'un CompteBancaireCPP via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws CompteBancaireCPPException
     * @throws JadePersistenceException
     */
    @Override
    public CompteBancaireCCP readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws CompteBancaireCCPException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new CompteBancaireCCPException(
                    "Unable to find CompteBancaireCCP the idDonneeFinanciereHeader passed si null!");
        }

        CompteBancaireCCPSearch search = new CompteBancaireCCPSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (CompteBancaireCCPSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new CompteBancaireCCPException("More than one CompteBancaireCCP find, one was exepcted!");
        }

        return (CompteBancaireCCP) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((CompteBancaireCCPSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CompteBancaireCCPService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .CompteBancaireCCPSearch)
     */
    @Override
    public CompteBancaireCCPSearch search(CompteBancaireCCPSearch compteBancaireCCPSearch)
            throws JadePersistenceException, CompteBancaireCCPException {
        if (compteBancaireCCPSearch == null) {
            throw new CompteBancaireCCPException("Unable to search CompteBancaireCCP, the search model passed is null!");
        }
        return (CompteBancaireCCPSearch) JadePersistenceManager.search(compteBancaireCCPSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CompteBancaireCCPService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .CompteBancaireCCP)
     */
    @Override
    public CompteBancaireCCP update(CompteBancaireCCP compteBancaireCCP) throws JadePersistenceException,
            CompteBancaireCCPException, DonneeFinanciereException {
        if (compteBancaireCCP == null) {
            throw new CompteBancaireCCPException("Unable to update CompteBancaireCCP, the model passed is null!");
        }

        try {
            compteBancaireCCP.setSimpleCompteBancaireCCP(PegasusImplServiceLocator.getSimpleCompteBancaireCCPService()
                    .update(compteBancaireCCP.getSimpleCompteBancaireCCP()));
            compteBancaireCCP.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            compteBancaireCCP.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CompteBancaireCCPException("Service not available - " + e.getMessage());
        }

        return compteBancaireCCP;
    }

}
