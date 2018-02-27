/**
 * 
 */
package ch.globaz.osiris.businessimpl.service;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModelSearch;
import ch.globaz.osiris.business.service.CompteAnnexeService;
import ch.globaz.osiris.exception.OsirisException;

/**
 * Implémentation du service de compte annexe
 * 
 * @author SCO 19 mai 2010
 */
/**
 * @author DDE
 * 
 */
public class CompteAnnexeServiceImpl implements CompteAnnexeService {

    /** Clé alternée sur l'identifiant externe */
    public final static int AK_IDEXTERNE = 1;

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.osiris.business.service.CompteAnnexeService#getCompteAnnexe(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.Boolean)
     */
    @Override
    public CompteAnnexeSimpleModel getCompteAnnexe(String idJournal, String idTiers, String idRole,
            String idExterneRole, Boolean createIfNotExist) throws JadePersistenceException, JadeApplicationException {

        // Vérification des paramètres
        if (JadeStringUtil.isIntegerEmpty(idTiers)) {
            throw new OsirisException("Unable to retrieve/create compte annexe, the idTiers passed is empty");
        }
        if (JadeStringUtil.isIntegerEmpty(idRole)) {
            throw new OsirisException("Unable to retrieve/create compte annexe, the idRole passed is empty");
        }
        if (JadeStringUtil.isIntegerEmpty(idExterneRole)) {
            throw new OsirisException("Unable to retrieve/create compte annexe, the idExterneRole passed is empty");
        }
        // Par défaut pas de création du compte
        if (createIfNotExist == null) {
            createIfNotExist = false;
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setIdTiers(idTiers);
        compteAnnexe.setIdRole(idRole);
        compteAnnexe.setIdExterneRole(idExterneRole);

        try {
            compteAnnexe.setAlternateKey(CompteAnnexeServiceImpl.AK_IDEXTERNE);
            compteAnnexe.retrieve();

            compteAnnexe.setAlternateKey(0);

            if (compteAnnexe.isNew() && createIfNotExist) {
                if (JadeStringUtil.isEmpty(idJournal)) {
                    idJournal = "0";
                }
                compteAnnexe.setIdJournal(idJournal);
                compteAnnexe.add();
            }

        } catch (Exception e) {
            throw new OsirisException("Technical Exception, Error in the creation of the compte annexe : "
                    + e.toString(), e);
        }

        return this.parse(compteAnnexe);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.osiris.business.service.CompteAnnexeService#getCompteAnnexeByIdTiers(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public CompteAnnexeSimpleModel getCompteAnnexeByIdTiers(String idJournal, String idTiers, String idRole,
            String idExterneRole) throws JadePersistenceException, JadeApplicationException {
        // Vérification des paramètres
        if (JadeStringUtil.isIntegerEmpty(idTiers)) {
            throw new OsirisException("Unable to retrieve compte annexe, the idTiers passed is empty");
        }
        if (JadeStringUtil.isIntegerEmpty(idRole)) {
            throw new OsirisException("Unable to retrieve compte annexe, the idRole passed is empty");
        }
        if (JadeStringUtil.isIntegerEmpty(idExterneRole)) {
            throw new OsirisException("Unable to retrieve compte annexe, the idExterneRole passed is empty");
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setIdTiers(idTiers);
        compteAnnexe.setIdRole(idRole);
        compteAnnexe.setIdExterneRole(idExterneRole);

        try {
            compteAnnexe.setAlternateKey(CompteAnnexeServiceImpl.AK_IDEXTERNE);
            compteAnnexe.retrieve();

            compteAnnexe.setAlternateKey(0);

        } catch (Exception e) {
            throw new OsirisException("Technical Exception, Error during retrieve operation of the compte annexe", e);
        }

        return this.parse(compteAnnexe);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.osiris.business.service.CompteAnnexeService#getCompteAnnexeByRole(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public CompteAnnexeSimpleModel getCompteAnnexeByRole(String idJournal, String idTiers, String idRole,
            String idExterneRole) throws JadePersistenceException, JadeApplicationException {

        // Vérification des paramètres
        if (JadeStringUtil.isIntegerEmpty(idTiers)) {
            throw new OsirisException("Unable to retrieve/create compte annexe, the idTiers passed is empty");
        }
        if (JadeStringUtil.isIntegerEmpty(idRole)) {
            throw new OsirisException("Unable to retrieve/create compte annexe, the idRole passed is empty");
        }
        if (JadeStringUtil.isIntegerEmpty(idExterneRole)) {
            throw new OsirisException("Unable to retrieve/create compte annexe, the idExterneRole passed is empty");
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setIdTiers(idTiers);
        compteAnnexe.setIdRole(idRole);
        compteAnnexe.setIdExterneRole(idExterneRole);

        try {
            compteAnnexe.setAlternateKey(CompteAnnexeServiceImpl.AK_IDEXTERNE);
            compteAnnexe.retrieve();

            compteAnnexe.setAlternateKey(0);

            if (compteAnnexe.isNew()) {
                if (JadeStringUtil.isEmpty(idJournal)) {
                    idJournal = "0";
                }
                compteAnnexe.setIdJournal(idJournal);
                compteAnnexe.add();
            }

        } catch (Exception e) {
            throw new OsirisException("Technical Exception, Error in the creation of the compte annexe", e);
        }

        return this.parse(compteAnnexe);
    }

    private String jointWithComma(Collection<String> collection) {
        return StringUtils.join(collection.toArray(), ",");
    }

    /**
     * Parser d'une entité compte annexe en un model compte annexe
     * 
     * @param compteAnnexe
     * @return
     * @throws OsirisException
     */
    private CompteAnnexeSimpleModel parse(CACompteAnnexe compteAnnexe) throws OsirisException {

        if (compteAnnexe == null) {
            throw new OsirisException("Unable to parse compte annexe, the entity conmpteAnnexe is null");
        }

        if (compteAnnexe.isNew()) {
            return new CompteAnnexeSimpleModel();
        }

        CompteAnnexeSimpleModel compteAnnexeModel = new CompteAnnexeSimpleModel();
        compteAnnexeModel.setId(compteAnnexe.getIdCompteAnnexe());
        compteAnnexeModel.setIdTiers(compteAnnexe.getIdTiers());
        compteAnnexeModel.setIdRole(compteAnnexe.getIdRole());
        compteAnnexeModel.setIdExterneRole(compteAnnexe.getIdExterneRole());
        compteAnnexeModel.setIdJournal(compteAnnexe.getIdJournal());
        compteAnnexeModel.setDescription(compteAnnexe.getDescription());
        compteAnnexeModel.setSolde(compteAnnexe.getSolde());
        compteAnnexeModel.setSpy(compteAnnexe.getSpy().getFullData());

        return compteAnnexeModel;
    }

    /**
     * Parser d'un model compte annexe en une entité compte annexe
     * 
     * @param compteAnnexeModel
     * @return
     */
    private CACompteAnnexe parse(CompteAnnexeSimpleModel compteAnnexeModel) {
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();

        compteAnnexe.setIdJournal(compteAnnexeModel.getIdJournal());
        compteAnnexe.setIdTiers(compteAnnexeModel.getIdTiers());
        compteAnnexe.setIdRole(compteAnnexeModel.getIdRole());
        compteAnnexe.setIdExterneRole(compteAnnexeModel.getIdExterneRole());
        compteAnnexe.setDescription(compteAnnexeModel.getDescription());
        compteAnnexe.setIdCompteAnnexe(compteAnnexeModel.getId());
        compteAnnexe.setSolde(compteAnnexeModel.getSolde());
        compteAnnexe.populateSpy(compteAnnexeModel.getSpy());

        return compteAnnexe;
    }

    @Override
    public CompteAnnexeSimpleModel read(String idCompteAnnexe) throws JadePersistenceException,
            JadeApplicationException {

        // Vérification des paramètres
        if (JadeStringUtil.isIntegerEmpty(idCompteAnnexe)) {
            throw new OsirisException("Unable to read compte annexe, the idCompteAnnexe passed is empty");
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setIdCompteAnnexe(idCompteAnnexe);

        try {
            // compteAnnexe.setAlternateKey(CompteAnnexeServiceImpl.AK_IDEXTERNE);
            compteAnnexe.retrieve();

        } catch (Exception e) {
            throw new OsirisException("Technical Exception, Error in the creation of the compte annexe : "
                    + e.toString(), e);
        }

        return this.parse(compteAnnexe);
    }

    @Override
    public List<CompteAnnexeSimpleModel> search(CompteAnnexeSimpleModelSearch search) throws OsirisException {
        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        try {
            if (search.getForIdCompteAnnexeIn() != null) {
                manager.setForIdCompteAnnexeIn(jointWithComma(search.getForIdCompteAnnexeIn()));
            }
            manager.changeManagerSize(search.getDefinedSearchSize());
            manager.setForIdTiersIn(search.getForIdTiersIn());
            manager.setForIdExterneRoleIn(search.getForIdExterneRoleIn());
            manager.setForIdRole(search.getForIdRole());
            manager.setForIdTiers(search.getForIdTiers());
            if (search.getForIdRoleIn() != null) {
                manager.setForSelectionRole(jointWithComma(search.getForIdRoleIn()));
            }
            manager.find();
            List<CompteAnnexeSimpleModel> list = new ArrayList<CompteAnnexeSimpleModel>();
            for (int i = 0; i < manager.size(); i++) {
                CACompteAnnexe compteAnnexe = (CACompteAnnexe) manager.get(i);
                list.add(this.parse(compteAnnexe));
            }
            return list;
        } catch (Exception e) {
            throw new OsirisException("Technical Exception,Unable to search the compte annexe ", e);
        }
    }
}
