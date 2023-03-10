package globaz.osiris.eservices.service;

import ch.globaz.naos.business.model.AffiliationSearchSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import globaz.osiris.eservices.enums.ESRole;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.osiris.db.comptes.*;
import globaz.osiris.db.comptes.extrait.CAExtraitCompteManager;
import globaz.osiris.db.contentieux.CAExtraitCompteListViewBean;
import globaz.osiris.eservices.exceptions.ESBadRequestException;
import globaz.osiris.eservices.exceptions.ESInternalException;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

@Slf4j
public class ESSearchService {

    public ESSearchService() {
    }

    private String mapToSelectionRole(String role) {
        return ESRole.valueOf(role.toUpperCase(Locale.ROOT)).getCS();
    }

    private String mapToSelectionSections(String selectionSections) {
        if (selectionSections.equals(CAExtraitCompteManager.SOLDE_OPEN)) {
            selectionSections = CASectionManager.SOLDE_OPEN;
        } else if (selectionSections.equals(CAExtraitCompteManager.SOLDE_CLOSED)) {
            selectionSections = CASectionManager.SOLDE_CLOSED;
        } else {
            selectionSections = CASectionManager.SOLDE_ALL;
        }

        return selectionSections;
    }

    public CACompteAnnexeManager searchComptesAnnexes(String forNumeroAffilie, String role, BSession session) throws ESInternalException, ESBadRequestException {
        CACompteAnnexeManager compteAnnexeManager = new CACompteAnnexeManager();
        compteAnnexeManager.setSession(session);
        compteAnnexeManager.setForIdExterneRole(forNumeroAffilie);
        compteAnnexeManager.setForSelectionRole(mapToSelectionRole(role));

        try {
            compteAnnexeManager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new ESInternalException("Une erreur est survenue lors de la recherche d'un compte annexe pour le num?ro d'affili? : " + forNumeroAffilie, e);
        }

        if (compteAnnexeManager.size() == 0) {
            throw new ESBadRequestException("Impossible de trouver un compte annexe pour le num?ro d'affili? : " + forNumeroAffilie);
        }

        return compteAnnexeManager;
    }

    public CASectionManager searchSections(CACompteAnnexe compteAnnexe, String selectionSections, BSession session) throws ESInternalException {
        CASectionManager sectionManager = new CASectionManager();
        sectionManager.setSession(session);
        sectionManager.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
        sectionManager.setForSelectionTri(CASectionManager.ORDER_DATE_DESCEND); // SelectionTri set par d?fault par Date DESC
        sectionManager.setForSelectionSections(mapToSelectionSections(selectionSections));

        try {
            sectionManager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new ESInternalException("Une exception est survenue lors de la recherche des sections pour l'idCompteAnnexe : " + compteAnnexe.getIdCompteAnnexe(), e);
        }

        if (sectionManager.getSize() == 0) {
            LOG.info("Impossible de trouver une section pour l'idCompteAnnexe : " + compteAnnexe.getIdCompteAnnexe());
        }

        return sectionManager;
    }

    public CAExtraitCompteListViewBean searchLignesExtraitComptes(CASection section, String selectionTris, String selectionSections, String operation, String langue, String startPeriod, String endPeriod, BSession session) {
        CAExtraitCompteListViewBean vb = new CAExtraitCompteListViewBean();
        vb.setIdCompteAnnexe(section.getIdCompteAnnexe());
        vb.setSession(session);
        vb.setForSelectionTri(selectionTris); // mapp? avec la valeur syst?me dans le viewBean
        vb.setForSelectionSections(selectionSections); // mapp? avec la valeur syst?me dans le viewBean
        vb.setForIdTypeOperation(operation);
        vb.setForIdSection(section.getIdSection());
        vb.setPrintLanguage(langue);
        vb.setFromDate(startPeriod);
        vb.setUntilDate(endPeriod);
        vb.find();

        if (vb.getSize() == 0) {
            LOG.info("Impossible de trouver des lignes d'extrait de comptes pour l'idSection : " + section.getIdSection());
        }

        return vb;
    }

    public AffiliationSearchSimpleModel searchAffiliation(String affiliateNumber) throws ESInternalException, ESBadRequestException {
        AffiliationSearchSimpleModel searchModel = new AffiliationSearchSimpleModel();
        searchModel.setForNumeroAffilie(affiliateNumber);

        try {
            searchModel = AFBusinessServiceLocator.getAffiliationService().find(searchModel);
        } catch (Exception e) {
            throw new ESInternalException("Une exception est survenue lors de la recherche d'une affiliation pour le num?ro d'affili? : " + affiliateNumber, e);
        }

        if (searchModel.getSize() == 0) {
            throw new ESBadRequestException("Impossible de trouver une affiliation pour le num?ro d'affili? : " + affiliateNumber);
        }

        return searchModel;
    }
}
