package globaz.osiris.eservices.service;

import ch.globaz.naos.business.model.AffiliationSearchSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.orion.ws.enums.Role;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.comptes.extrait.CAExtraitCompteManager;
import globaz.osiris.db.contentieux.CAExtraitCompteListViewBean;
import globaz.osiris.eservices.exceptions.ESBadRequestException;
import globaz.osiris.eservices.exceptions.ESInternalException;
import globaz.osiris.external.IntRole;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ESSearchService {

    public ESSearchService() {
    }

    private String mapToSelectionRole(Role role) {
        String selectionRole;
        switch (role) {
            case EMPLOYEUR:
                selectionRole = IntRole.ROLE_AFFILIE_PARITAIRE;
                break;
            case INDEPENDANT:
                selectionRole = IntRole.ROLE_AFFILIE_PERSONNEL;
                break;
            case AF:
                selectionRole = IntRole.ROLE_AF;
                break;
            case AFFILIE:
                selectionRole = IntRole.ROLE_AFFILIE;
                break;
            default:
                throw new IllegalArgumentException("invalid role " + role);
        }

        return selectionRole;
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

    public CACompteAnnexeManager searchComptesAnnexes(String forNumeroAffilie, Role role, BSession session) throws ESInternalException, ESBadRequestException {
        CACompteAnnexeManager compteAnnexeManager = new CACompteAnnexeManager();
        compteAnnexeManager.setSession(session);
        compteAnnexeManager.setForIdExterneRole(forNumeroAffilie);
        compteAnnexeManager.setForSelectionRole(mapToSelectionRole(role));

        try {
            compteAnnexeManager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new ESInternalException("Une erreur est survenue lors de la recherche d'un compte annexe pour le numéro d'affilié : " + forNumeroAffilie, e);
        }

        if (compteAnnexeManager.size() == 0) {
            throw new ESBadRequestException("Impossible de trouver un compte annexe pour le numéro d'affilié : " + forNumeroAffilie);
        }

        return compteAnnexeManager;
    }

    public CASectionManager searchSections(CACompteAnnexe compteAnnexe, String selectionTris, String selectionSections, String startPeriod, String endPeriod, BSession session) throws ESInternalException {
        CASectionManager sectionManager = new CASectionManager();
        sectionManager.setSession(session);
        sectionManager.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
        sectionManager.setForSelectionTri(selectionTris);
        sectionManager.setForSelectionSections(mapToSelectionSections(selectionSections));
        sectionManager.setFromDate(startPeriod);
        sectionManager.setUntilDate(endPeriod);

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

    public CAExtraitCompteListViewBean searchLignesExtraitComptes(CASection section, String selectionTris, String selectionSections, String langue, BSession session) {
        CAExtraitCompteListViewBean vb = new CAExtraitCompteListViewBean();
        vb.setIdCompteAnnexe(section.getIdCompteAnnexe());
        vb.setSession(session);
        vb.setForSelectionTri(selectionTris);
        vb.setForSelectionSections(selectionSections); // mappé avec la valeur système dans le viewBean
        vb.setForIdSection(section.getIdSection());
        vb.setPrintLanguage(langue);
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
            throw new ESInternalException("Une exception est survenue lors de la recherche d'une affiliation pour le numéro d'affilié : " + affiliateNumber, e);
        }

        if (searchModel.getSize() == 0) {
            throw new ESBadRequestException("Impossible de trouver une affiliation pour le numéro d'affilié : " + affiliateNumber);
        }

        return searchModel;
    }
}
