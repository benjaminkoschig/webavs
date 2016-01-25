/**
 * 
 */
package ch.globaz.osiris.businessimpl.service;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.utils.CAUtil;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleSearch;
import ch.globaz.osiris.business.service.SectionService;
import ch.globaz.osiris.exception.OsirisException;

/**
 * @author SCO
 * 
 */
public class SectionServiceImpl implements SectionService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.osiris.business.service.SectionService#create(ch.globaz.osiris .business.model.SectionSimpleModel)
     */
    @Override
    public SectionSimpleModel createSection(String idCompteAnnexe, String idExterne, String idTypeSection, String date,
            String idJournal) throws JadePersistenceException, JadeApplicationException {

        CASection section = new CASection();
        section.setIdCompteAnnexe(idCompteAnnexe);
        section.setIdExterne(idExterne);
        section.setIdTypeSection(idTypeSection);
        section.setIdJournal(idJournal);
        section.setDateSection(date);

        try {

            section.add();
            if (section.hasErrors()) {
                throw new OsirisException("Unable to create the section : " + section.getErrors().toString());
            }

        } catch (Exception e) {
            throw new OsirisException("Technical exception, error to create the section", e);
        }

        return this.parse(section);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.osiris.business.service.SectionService#creerNumeroSectionUnique (java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public SectionSimpleModel creerNumeroSectionUnique(String idRole, String idExterneRole, String idTypeSection,
            String annee, String categorieSection) throws JadePersistenceException, JadeApplicationException {

        SectionSimpleModel sectionModel = new SectionSimpleModel();

        try {
            sectionModel.setIdExterne(CAUtil.creerNumeroSectionUnique(BSessionUtil.getSessionFromThreadContext(),
                    BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction(), idRole, idExterneRole,
                    idTypeSection, annee, categorieSection));
        } catch (Exception e) {
            throw new OsirisException("Technical exception, error to create a unique section id", e);
        }

        return sectionModel;
    }

    @Override
    public String findDescription(String idSection) throws OsirisException {
        // Verification des parametres
        if (JadeStringUtil.isBlankOrZero(idSection)) {
            throw new OsirisException("Unable to read the section, the idSection passed is empty");
        }

        CASectionManager mgr = new CASectionManager();
        mgr.setForIdSection(idSection);

        try {
            mgr.find();
            if (mgr.hasErrors()) {
                new OsirisException("Technical exception, Error in research of the section");
            }
        } catch (Exception e) {
            new OsirisException("Technical exception, Error in research of the section", e);
        }
        if (mgr.size() == 1) {
            CASection section = (CASection) mgr.getFirstEntity();
            return section.getDescription();
        } else if (mgr.size() == 0) {
            return null;
        } else {
            throw new OsirisException("Technical exception, there are multiple sections defined by this parameters");
        }
    }

    public SectionSimpleModel findSectionByIdExterne(String idCompteAnnexe, String idTypeSection, String idExterne)
            throws JadePersistenceException, JadeApplicationException {

        // Verification des parametres
        if (JadeStringUtil.isDecimalEmpty(idCompteAnnexe)) {
            throw new OsirisException("Unable to find the section, the idCompteAnnexe passed is empty");
        }
        if (JadeStringUtil.isDecimalEmpty(idTypeSection)) {
            throw new OsirisException("Unable to find the section, the idTypeSection passed is empty");
        }
        if (JadeStringUtil.isDecimalEmpty(idExterne)) {
            throw new OsirisException("Unable to find the section, the idExterne passed is empty");
        }
        SectionSimpleModel sectionModel = null;

        CASectionManager mgr = new CASectionManager();
        mgr.setForIdCompteAnnexe(idCompteAnnexe);
        mgr.setForIdExterne(idExterne);
        mgr.setForIdTypeSection(idTypeSection);

        try {
            mgr.find();
            if (mgr.hasErrors()) {
                new OsirisException("Technical exception, Error in research of the section");
            }
        } catch (Exception e) {
            new OsirisException("Technical exception, Error in research of the section", e);
        }

        if (mgr.size() == 1) {
            CASection section = (CASection) mgr.getFirstEntity();
            sectionModel = this.parse(section);
        } else if (mgr.size() == 0) {
            return null;
        } else {
            throw new OsirisException("Technical exception, there are multiple sections defined by this parameters");
        }

        return sectionModel;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.osiris.business.service.SectionService#getSectionByIdExterne (java.lang.String, java.lang.String,
     * java.lang.String)
     */
    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.osiris.business.service.SectionService#getSectionByIdExterne (java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public SectionSimpleModel getSectionByIdExterne(String idCompteAnnexe, String idTypeSection, String idExterne,
            JournalSimpleModel journal) throws JadePersistenceException, JadeApplicationException {

        // Verification des parametres
        if (JadeStringUtil.isDecimalEmpty(idCompteAnnexe)) {
            throw new OsirisException("Unable to find the section, the idCompteAnnexe passed is empty");
        }
        if (JadeStringUtil.isDecimalEmpty(idTypeSection)) {
            throw new OsirisException("Unable to find the section, the idTypeSection passed is empty");
        }
        if (JadeStringUtil.isDecimalEmpty(idExterne)) {
            throw new OsirisException("Unable to find the section, the idExterne passed is empty");
        }
        if ((journal == null) || journal.isNew()) {
            throw new OsirisException("Unable to find the section, the journal passed is null or new");
        }

        // *************************
        // //Création de la recherche
        // SectionSearchSimpleModel searchModel = new
        // SectionSearchSimpleModel();
        // searchModel.setForIdCompteAnnexe(idCompteAnnexe);
        // searchModel.setForIdExterne(idExterne);
        // searchModel.setForIdTypeSection(idTypeSection);
        //
        // searchModel = (SectionSearchSimpleModel)
        // JadePersistenceManager.search(searchModel);
        //
        // //Aucune section trouvée, on leve une exception
        // if(searchModel.getSize() <= 0) {
        // new
        // OsirisException("Unable to find the section defined by this parameters");
        // }
        //
        // //On retourne le premier résultat
        // return (SectionSimpleModel) searchModel.getSearchResults()[0] ;
        // ********************

        SectionSimpleModel sectionModel = null;

        CASectionManager mgr = new CASectionManager();
        mgr.setForIdCompteAnnexe(idCompteAnnexe);
        mgr.setForIdExterne(idExterne);
        mgr.setForIdTypeSection(idTypeSection);

        try {
            mgr.find();
            // FIXME : retourner les erreurs dans l'exception (?)
            if (mgr.hasErrors()) {
                new OsirisException("Technical exception, Error in research of the section");
            }
        } catch (Exception e) {
            new OsirisException("Technical exception, Error in research of the section", e);
        }

        if (mgr.size() == 1) {
            CASection section = (CASection) mgr.getFirstEntity();
            sectionModel = this.parse(section);
        } else if (mgr.size() == 0) {
            sectionModel = createSection(idCompteAnnexe, idExterne, idTypeSection, journal.getDateValeurCG(),
                    journal.getIdJournal());
        } else {
            throw new OsirisException("Technical exception, there are multiple sections defined by this parameters");
        }

        return sectionModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.osiris.business.service.SectionService#getSoldeSection(java .lang.String, java.lang.String)
     */
    @Override
    public FWCurrency getSoldeSection(String idJournal, String idSection) throws JadePersistenceException,
            JadeApplicationException {

        if (JadeStringUtil.isDecimalEmpty(idJournal)) {
            throw new OsirisException("Unable to retrieve solde section, the id journal passed is null or empty");
        }
        if (JadeStringUtil.isDecimalEmpty(idSection)) {
            throw new OsirisException("Unable to retrieve solde section, the id section passed is null or empty");
        }

        // ---------------------------
        // 1. récupération du solde de la section
        // 2. récupération du total des écritures et versements du journal en
        // état non inactifs.
        // 3. somme des 2 premiers
        // ---------------------------
        FWCurrency currency = null;

        CASection section = new CASection();
        section.setIdSection(idSection);

        try {
            section.retrieve();

        } catch (Exception e) {
            throw new OsirisException("Technical exception, unable to retrieve the section", e);
        }

        if (section.hasErrors() || section.isNew()) {
            throw new OsirisException("Unable to retrieve the section");
        }

        CAOperationManager manager = new CAOperationManager();
        manager.setForIdJournal(idJournal);
        manager.setForIdSection(idSection);

        ArrayList<String> idTypeOperationLike = new ArrayList<String>();
        idTypeOperationLike.add(APIOperation.CAECRITURE);
        idTypeOperationLike.add(APIOperation.CAOPERATIONORDREVERSEMENT);
        manager.setForIdTypeOperationLikeIn(idTypeOperationLike);

        ArrayList<String> etatNotIn = new ArrayList<String>();
        etatNotIn.add(APIOperation.ETAT_INACTIF);
        manager.setForEtatNotIn(etatNotIn);

        try {
            currency = new FWCurrency((manager.getSum(CAOperation.FIELD_MONTANT)).doubleValue());
        } catch (Exception e) {
            throw new OsirisException("Technical exception, unable to retrieve sum of the ecriture", e);
        }

        currency.add(section.getSoldeToCurrency());

        return currency;
    }

    /**
     * Parser d'une entité section en un model de section
     * 
     * @param section
     * @return
     * @throws OsirisException
     */
    private SectionSimpleModel parse(CASection section) throws OsirisException {

        if (section == null) {
            throw new OsirisException("Unable to parse section, the entity section is null");
        }

        if (section.isNew()) {
            return new SectionSimpleModel();
        }

        SectionSimpleModel sectionModel = new SectionSimpleModel();
        sectionModel.setId(section.getIdSection());
        sectionModel.setIdCompteAnnexe(section.getIdCompteAnnexe());
        sectionModel.setIdExterne(section.getIdExterne());
        sectionModel.setIdTypeSection(section.getIdTypeSection());
        sectionModel.setDateSection(section.getDateSection());
        sectionModel.setSpy(section.getSpy().getFullData());
        sectionModel.setSolde(section.getSolde());

        return sectionModel;
    }

    /**
     * Parser d'un model de section en une entité section
     * 
     * @param sectionModel
     * @return
     */
    private CASection parse(SectionSimpleModel sectionModel) {

        CASection section = new CASection();

        section.setIdSection(sectionModel.getId());
        section.setIdCompteAnnexe(sectionModel.getIdCompteAnnexe());
        section.setIdExterne(sectionModel.getIdExterne());
        section.setIdTypeSection(sectionModel.getIdTypeSection());
        section.setDateSection(sectionModel.getDateSection());
        section.populateSpy(sectionModel.getSpy());

        return section;

    }

    @Override
    public SectionSimpleModel readSection(String id) throws OsirisException {
        CASection caSection = new CASection();
        SectionSimpleModel sectionModel;
        try {
            caSection.setIdSection(id);
            caSection.retrieve();
            sectionModel = this.parse(caSection);
            if (caSection.hasErrors()) {
                throw new OsirisException("Unable to reat the section : " + id + ", "
                        + caSection.getErrors().toString());
            }

        } catch (Exception e) {
            throw new OsirisException("Technical exception, error to read the section: " + id, e);
        }

        return sectionModel;
    }

    @Override
    public List<SectionSimpleModel> search(SectionSimpleSearch search) throws OsirisException {
        List<SectionSimpleModel> list = new ArrayList<SectionSimpleModel>();
        if (search == null) {
            throw new OsirisException("Unable to search the section, the search model is null");
        }

        CASectionManager mgr = new CASectionManager();

        mgr.setForIdSectionIn(search.getInIdsSection());
        mgr.changeManagerSize(search.getDefinedSearchSize());
        mgr.setForIdTypeSection(search.getForIdTypeSection());
        if (search.getForEtatSolde() != null) {
            mgr.setForSelectionSections(search.getForEtatSolde().getEtat());
        }
        mgr.setLikeIdExterne(search.getLikeIdExterne());
        mgr.setForIdCompteAnnexe(search.getForIdCompteAnnexe());
        try {
            mgr.find();

            if (mgr.hasErrors()) {
                new OsirisException("Technical exception, Error in research of the section: "
                        + mgr.getErrors().toString());
            }

            for (int i = 0; i < mgr.size(); i++) {
                CASection section = (CASection) mgr.get(i);
                list.add(this.parse(section));
            }
        } catch (Exception e) {
            new OsirisException("Technical exception, Error in research of the section", e);
        }

        return list;
    }
}
