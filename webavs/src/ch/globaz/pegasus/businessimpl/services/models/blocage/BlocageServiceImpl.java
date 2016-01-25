package ch.globaz.pegasus.businessimpl.services.models.blocage;

import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.external.IntRole;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.SoldeCompteCourant;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.pegasus.business.constantes.EPCEtatDeblocage;
import ch.globaz.pegasus.business.constantes.EPCTypeDeblocage;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.blocage.BlocageException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.blocage.PcaBloque;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocage;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtenduSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.blocage.BlocageService;
import ch.globaz.pegasus.business.vo.blocage.Creancier;
import ch.globaz.pegasus.business.vo.blocage.Deblocage;
import ch.globaz.pegasus.business.vo.blocage.DeblocageDetail;
import ch.globaz.pegasus.business.vo.blocage.DetteComptat;
import ch.globaz.pegasus.business.vo.blocage.SoldeCompteCouranSection;
import ch.globaz.pegasus.business.vo.blocage.VersementBeneficiaire;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusComptaUtils;
import ch.globaz.pegasus.businessimpl.utils.adresse.AdresseHandler;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class BlocageServiceImpl implements BlocageService {

    private List<Creancier> createCreanciers(List<SimpleLigneDeblocage> list)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        List<Creancier> creanciers = new ArrayList<Creancier>();
        if (list != null) {
            for (SimpleLigneDeblocage deblocage : list) {
                Creancier creancier = new Creancier();
                DeblocageUtils.addValueDeblocage(deblocage, creancier);
                creancier.setRefPaiement(deblocage.getRefPaiement());
                creancier.setAdressePaiement(AdresseHandler.convertAdressePaiement(TIBusinessServiceLocator
                        .getAdresseService().getAdressePaiementTiers(deblocage.getIdTiersAdressePaiement(), false,
                                deblocage.getIdApplicationAdressePaiement(), JACalendar.todayJJsMMsAAAA(), null)));

                TiersSimpleModel tiersCreancier = TIBusinessServiceLocator.getTiersService().read(
                        deblocage.getIdTiersCreancier());

                creancier.setDesignationTiers1(tiersCreancier.getDesignation1());
                creancier.setDesignationTiers2(tiersCreancier.getDesignation2());
                creanciers.add(creancier);
            }
        }
        return creanciers;
    }

    private List<VersementBeneficiaire> createVersementBeneficiaire(List<SimpleLigneDeblocage> list,
            PersonneEtendueComplexModel requerant, PersonneEtendueComplexModel conjoint) throws BlocageException {
        List<VersementBeneficiaire> beneficiaires = new ArrayList<VersementBeneficiaire>();
        boolean hasStateEnregistre = false;
        if (list != null) {
            for (SimpleLigneDeblocage deblocage : list) {
                VersementBeneficiaire beneficiaire = new VersementBeneficiaire();
                DeblocageUtils.addValueDeblocage(deblocage, beneficiaire);

                if (EPCEtatDeblocage.ENREGISTRE.equals(beneficiaire.getEtatDeblocage())) {
                    hasStateEnregistre = true;
                }
                if (deblocage.getIdTiersAdressePaiement().equals(requerant.getTiers().getIdTiers())) {
                    beneficiaire.setCsRoleMembreFamille(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
                    beneficiaire.setPersonne(requerant);
                } else if ((conjoint != null)
                        && deblocage.getIdTiersAdressePaiement().equals(conjoint.getTiers().getIdTiers())) {
                    beneficiaire.setCsRoleMembreFamille(IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
                    beneficiaire.setPersonne(conjoint);
                } else {
                    throw new BlocageException("Unable to determine the personne with this id tiers: "
                            + deblocage.getIdTiersAdressePaiement());
                }
                beneficiaires.add(beneficiaire);
            }
        }

        if (!hasStateEnregistre) {
            VersementBeneficiaire beneficiaire = new VersementBeneficiaire();
            beneficiaire.setCsRoleMembreFamille(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
            beneficiaire.setPersonne(requerant);
            beneficiaires.add(beneficiaire);
            if (conjoint != null) {
                beneficiaire = new VersementBeneficiaire();
                beneficiaire.setCsRoleMembreFamille(IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
                beneficiaire.setPersonne(conjoint);
                beneficiaires.add(beneficiaire);
            }
        }
        return beneficiaires;
    }

    @Override
    public SoldeCompteCourant determineLeCompteCouranAUtiliser(PcaBloque pcaBloque) throws JadePersistenceException,
            JadeApplicationException, JadeApplicationServiceNotAvailableException {

        List<SoldeCompteCourant> comptesCourant = findSoldeCompteCourant(pcaBloque);

        if ((comptesCourant != null) && (comptesCourant.size() == 1)) {
            return comptesCourant.get(0);
        } else if ((comptesCourant != null) && (comptesCourant.size() > 1)) {
            for (SoldeCompteCourant compte : comptesCourant) {
                if (!JadeStringUtil.isBlankOrZero(compte.getMontant()) && (Float.valueOf(compte.getMontant()) < 0)) {
                    return compte;
                }
            }
            return comptesCourant.get(0);
        } else {
            throw new BlocageException("Unable to find the compte courant with values: " + pcaBloque.toString());
        }
    }

    @Override
    public void devaliderLiberation(String idPca) throws JadePersistenceException, JadeApplicationException {
        DevaliderLiberation.delvalider(idPca);
    }

    private List<SoldeCompteCouranSection> findCompteBlocageAndAddDescriptionSection(PcaBloque pcaBloque,
            CompteAnnexeSimpleModel compteAnnexeSimpleModel) throws JadePersistenceException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException {

        List<SoldeCompteCourant> list = findSoldeCompteCourant(pcaBloque);
        List<SoldeCompteCouranSection> listCompteWithDescription = new ArrayList<SoldeCompteCouranSection>();
        for (SoldeCompteCourant compte : list) {
            SoldeCompteCouranSection compteSection = new SoldeCompteCouranSection();
            compteSection.setDescription(compte.getDescription());
            compteSection.setDescriptionSection(CABusinessServiceLocator.getSectionService().findDescription(
                    compte.getIdSection()));
            compteSection.setIdExterneCompteCourant(compte.getIdExterneCompteCourant());
            compteSection.setIdExterneRole(compte.getIdExterneRole());
            compteSection.setIdExterneSection(compte.getIdExterneSection());
            compteSection.setIdSection(compte.getIdSection());
            compteSection.setMontant(compte.getMontant());
            listCompteWithDescription.add(compteSection);
        }
        return listCompteWithDescription;
    }

    private MembreFamilleEtenduSearch findDroitMembreFamille(String idDroit) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, DecisionException {
        MembreFamilleEtenduSearch membreSearch = new MembreFamilleEtenduSearch();
        membreSearch.setForIdDroit(idDroit);
        try {
            membreSearch = PegasusServiceLocator.getDroitService().searchMembreFamilleEtendu(membreSearch);
        } catch (DroitException e1) {
            throw new DecisionException("Unable to search the membreFamille ", e1);
        }
        return membreSearch;
    }

    @Override
    public List<SoldeCompteCourant> findSoldeCompteCourant(PcaBloque pcaBloque) throws JadePersistenceException,
            JadeApplicationException, JadeApplicationServiceNotAvailableException {

        CompteAnnexeSimpleModel compteAnnexe = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(null,
                pcaBloque.getIdTiersBeneficiaire(), IntRole.ROLE_RENTIER, pcaBloque.getNss(), false);

        String idRubriqueCompteCourant = PegasusComptaUtils
                .findRubrique(APIReferenceRubrique.PC_COMPTE_COURANT_BLOCAGE).getIdRubrique();// "1105";

        String dateValeurSection = pcaBloque.getDateDebutPca().substring(3) + "0101";

        List<SoldeCompteCourant> list = CABusinessServiceLocator.getCompteCourantService().searchSoldeCompteCourant(
                dateValeurSection, compteAnnexe.getIdCompteAnnexe(), idRubriqueCompteCourant);
        return list;
    }

    @Override
    public void libererBlocage(String idPca) throws BlocageException, DecisionException, PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        Deblocage deblocage = PegasusImplServiceLocator.getDeblocageService().retriveDeblocage(idPca);

        LibererBlocage.libererBlocage(deblocage);
    }

    @Override
    public DeblocageDetail readForDeblocage(String idPca) throws JadePersistenceException, JadeApplicationException {

        List<String> listIdTiers = new ArrayList<String>();
        PersonneEtendueComplexModel conjoint = null;
        PersonneEtendueComplexModel beneficiaire = null;
        Deblocage deblocages = PegasusImplServiceLocator.getDeblocageService().retriveDeblocage(idPca);
        PcaBloque pcaBloque = deblocages.getPcaBloque();

        MembreFamilleEtenduSearch membreSearch = findDroitMembreFamille(pcaBloque.getIdDroit());

        for (JadeAbstractModel model : membreSearch.getSearchResults()) {
            MembreFamilleEtendu mf = (MembreFamilleEtendu) model;
            PersonneEtendueComplexModel personne = mf.getDroitMembreFamille().getMembreFamille().getPersonneEtendue();
            listIdTiers.add(personne.getTiers().getIdTiers());

            if (pcaBloque.getIdTiersBeneficiaire().equals(personne.getTiers().getIdTiers())) {
                beneficiaire = personne;
            } else {
                if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(mf.getDroitMembreFamille().getSimpleDroitMembreFamille()
                        .getCsRoleFamillePC())) {
                    conjoint = personne;
                } else if (IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(mf.getDroitMembreFamille()
                        .getSimpleDroitMembreFamille().getCsRoleFamillePC())) {
                    conjoint = personne;
                }
            }
        }

        List<DetteComptat> dettes = PegasusImplServiceLocator.getDeblocageDetteService().generateDetteForDeblocage(
                deblocages.getLingesDeblocages().get(EPCTypeDeblocage.CS_DETTE_EN_COMPTA), listIdTiers, pcaBloque,
                conjoint);

        // on ne veut pas le conjoint qui ne sont pas du dom2r
        if (JadeStringUtil.isBlankOrZero(pcaBloque.getIdPrestationAccordeeConjoint())) {
            conjoint = null;
        }

        List<SoldeCompteCouranSection> listCompteBlocage = findCompteBlocageAndAddDescriptionSection(pcaBloque,
                deblocages.getCompteAnnexe());

        BigDecimal sumComptBlocage = sumComptBlocage(listCompteBlocage);
        BigDecimal solde = new BigDecimal(pcaBloque.getMontantBloque()).subtract(deblocages.getSumTotalDeblocage());
        BigDecimal montantALiberer = sumComptBlocage.abs().min(solde.add(deblocages.getSumTotatEtatEnregistre()));

        DeblocageDetail deblocageDetail = new DeblocageDetail();
        deblocageDetail.setPcaBloque(pcaBloque);
        deblocageDetail.setComptesBlocage(listCompteBlocage);
        deblocageDetail.setCreanciers(createCreanciers(deblocages.getLingesDeblocages().get(
                EPCTypeDeblocage.CS_CREANCIER)));
        deblocageDetail.setVersementBeneficiaire(createVersementBeneficiaire(
                deblocages.getLingesDeblocages().get(EPCTypeDeblocage.CS_VERSEMENT_BENEFICIAIRE), beneficiaire,
                conjoint));
        deblocageDetail.setDetteEnCompta(dettes);
        deblocageDetail.setSolde(solde.setScale(2));
        deblocageDetail.setSoldeCompteBlocage(sumComptBlocage.setScale(2).abs());
        deblocageDetail.setMontantTotalADebloque(montantALiberer.setScale(2));
        deblocageDetail.setMontantLiberer(deblocages.getSumTotatEtatEnregistre().setScale(2));
        deblocageDetail.setIsDevalidable(deblocages.getIsDevalidable());
        return deblocageDetail;

    }

    private BigDecimal sumComptBlocage(List<SoldeCompteCouranSection> list) throws JadePersistenceException,
            JadeApplicationException, JadeApplicationServiceNotAvailableException {
        BigDecimal solde = new BigDecimal(0);
        for (SoldeCompteCouranSection comtpe : list) {
            if (!JadeStringUtil.isBlankOrZero(comtpe.getMontant())) {
                solde = solde.add(new BigDecimal(comtpe.getMontant()));
            }
        }
        return solde;
    }
}
