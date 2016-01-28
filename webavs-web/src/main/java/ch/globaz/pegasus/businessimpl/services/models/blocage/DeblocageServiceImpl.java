package ch.globaz.pegasus.businessimpl.services.models.blocage;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.external.IntRole;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleSearch;
import ch.globaz.osiris.business.model.SectionSimpleSearch.EtatSolde;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.osiris.exception.OsirisException;
import ch.globaz.pegasus.business.constantes.EPCEtatDeblocage;
import ch.globaz.pegasus.business.constantes.EPCTypeDeblocage;
import ch.globaz.pegasus.business.exceptions.models.blocage.BlocageException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.blocage.PcaBloque;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocage;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocageSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.blocage.DeblocageService;
import ch.globaz.pegasus.business.vo.blocage.Deblocage;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.SectionPegasus;

public class DeblocageServiceImpl implements DeblocageService {

    private Boolean isDevalidable(Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> lignes) {
        for (Entry<EPCTypeDeblocage, List<SimpleLigneDeblocage>> entry : lignes.entrySet()) {
            // devalidable seulement si c'ets validé
            for (SimpleLigneDeblocage deblocage : entry.getValue()) {
                if (EPCEtatDeblocage.VALIDE.getCsCode().equals(deblocage.getCsEtat())) {
                    return true;
                }
            }
        }
        return false;
    }

    private CompteAnnexeSimpleModel readCompteAnnexe(PcaBloque pcaBloque) throws JadePersistenceException,
            BlocageException {
        CompteAnnexeSimpleModel compteAnnexe;
        try {
            compteAnnexe = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(null,
                    pcaBloque.getIdTiersBeneficiaire(), IntRole.ROLE_RENTIER, pcaBloque.getNss(), false);
        } catch (JadeApplicationException e) {
            throw new BlocageException("Unabled to search the compteAnnexe", e);
        }
        return compteAnnexe;
    }

    @Override
    public Deblocage retriveDeblocage(String idPca) throws BlocageException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, PCAccordeeException {
        Deblocage deblocage = new Deblocage();
        Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> lignes = searchDeblocageAndGroupe(idPca);
        PcaBloque pcaBloque = PegasusImplServiceLocator.getPcaBloqueService().readPcaBloque(idPca);
        deblocage.setLingesDeblocages(lignes);
        deblocage.setSumTotalDeblocage(sumMontantDebloque(lignes));
        deblocage.setSumTotatEtatEnregistre(sumMontantDebloqueEtatEnregistrer(lignes));
        deblocage.setPcaBloque(pcaBloque);
        deblocage.setIsDevalidable(isDevalidable(lignes));
        deblocage.setPca(PegasusImplServiceLocator.getPCAccordeeService().readDetail(idPca));
        deblocage.setCompteAnnexe(readCompteAnnexe(pcaBloque));
        SectionSimpleSearch search = new SectionSimpleSearch();
        try {
            search.setLikeIdExterne(pcaBloque.getDateDebutPca().substring(3));
            search.setForEtatSolde(EtatSolde.NEGATIF);
            search.setForIdTypeSection(SectionPegasus.BLOCAGE.getType());
            search.setForIdCompteAnnexe(deblocage.getCompteAnnexe().getIdCompteAnnexe());
            deblocage.setSectionDeblocage(CABusinessServiceLocator.getSectionService().search(search));
        } catch (OsirisException e) {
            throw new BlocageException("Unabled to search the section deblocage", e);
        }
        return deblocage;
    }

    private Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> searchDeblocageAndGroupe(String idPca)
            throws BlocageException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleLigneDeblocageSearch search = new SimpleLigneDeblocageSearch();
        search.setForIdPca(idPca);
        return PegasusServiceLocator.getSimpleDeblocageService().searchAndGroupByCsTypeDeblocage(search);
    }

    private BigDecimal sumMontantDebloque(Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> deblocages) {
        BigDecimal sumDebloque = new BigDecimal(0);
        for (Entry<EPCTypeDeblocage, List<SimpleLigneDeblocage>> entry : deblocages.entrySet()) {
            for (SimpleLigneDeblocage simpleDeblocage : entry.getValue()) {
                sumDebloque = sumDebloque.add(new BigDecimal(simpleDeblocage.getMontant()));
            }
        }
        return sumDebloque;
    }

    private BigDecimal sumMontantDebloqueEtatEnregistrer(Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> deblocages) {
        BigDecimal sumDebloque = new BigDecimal(0);
        for (Entry<EPCTypeDeblocage, List<SimpleLigneDeblocage>> entry : deblocages.entrySet()) {
            for (SimpleLigneDeblocage simpleDeblocage : entry.getValue()) {
                if (EPCEtatDeblocage.ENREGISTRE.getCsCode().equals(simpleDeblocage.getCsEtat())) {
                    sumDebloque = sumDebloque.add(new BigDecimal(simpleDeblocage.getMontant()));
                }
            }
        }
        return sumDebloque;
    }

}
