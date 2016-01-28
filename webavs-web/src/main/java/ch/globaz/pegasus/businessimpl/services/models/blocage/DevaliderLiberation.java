package ch.globaz.pegasus.businessimpl.services.models.blocage;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.corvus.business.exceptions.CorvusException;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.corvus.business.models.blocage.SimpleEnteteBlocage;
import ch.globaz.corvus.businessimpl.services.CorvusImplServiceLocator;
import ch.globaz.pegasus.business.constantes.EPCEtatDeblocage;
import ch.globaz.pegasus.business.constantes.EPCTypeDeblocage;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocage;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersementSearch;
import ch.globaz.pegasus.business.models.lot.SimplePrestationSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.blocage.Deblocage;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class DevaliderLiberation {

    private static void deleteOvByIdsPresation(List<String> idsPresation) throws OrdreVersementException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleOrdreVersementSearch search = new SimpleOrdreVersementSearch();
        search.setWhereKey(SimpleOrdreVersementSearch.SUPPRESSION_WHERE_KEY);
        search.setForInIdPrestation(idsPresation);
        PegasusImplServiceLocator.getSimpleOrdreVersementService().delete(search);
    }

    private static void deletePresationByIdPresation(List<String> idsPresation) throws PegasusException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        SimplePrestationSearch search = new SimplePrestationSearch();
        search.setInIdPrestation(idsPresation);
        PegasusImplServiceLocator.getSimplePrestationService().delete(search);
    }

    private static void delvalider(Deblocage deblocage, List<String> idsPresation) throws JadePersistenceException,
            JadeApplicationException {
        DevaliderLiberation.deleteOvByIdsPresation(idsPresation);
        DevaliderLiberation.deletePresationByIdPresation(idsPresation);
        DevaliderLiberation.deleteLotIfEmpty();
        DevaliderLiberation.updateMontantDebloque(deblocage);

        // Supresison des ligne de blocages dettes
        DevaliderLiberation.deleteLignesBlocages(deblocage.getLingesDeblocages());
    }

    public static void delvalider(String idPca) throws JadePersistenceException, JadeApplicationException {
        Deblocage deblocage = PegasusImplServiceLocator.getDeblocageService().retriveDeblocage(idPca);
        if (deblocage.getIsDevalidable()) {
            // deblocage.getPcaBloque().getIdEnteteBlocage();
            List<String> idsPresation = DevaliderLiberation.determineIdsPresation(deblocage.getLingesDeblocages());
            //
            DevaliderLiberation.delvalider(deblocage, idsPresation);

        } else {
            JadeThread.logError(DevaliderLiberation.class.getName(), "pegasus.deblocage.devalidation.pasPossible");
        }
    }

    private static void deleteLotIfEmpty() throws DecisionException, PrestationException, LotException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        PegasusServiceLocator.getLotService().deleteLotDeblocageIfEmpty();
    }

    private static List<String> determineIdsPresation(Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> lignesDeblocage) {
        List<String> idsPresation = new ArrayList<String>();
        //
        for (Entry<EPCTypeDeblocage, List<SimpleLigneDeblocage>> entry : lignesDeblocage.entrySet()) {
            // on ajoute les ligne de deblocage non comptabilisé
            for (SimpleLigneDeblocage ligne : entry.getValue()) {
                if (!idsPresation.contains(ligne.getIdPrestation())
                        && !ligne.getCsEtat().equals(EPCEtatDeblocage.COMPTABILISE.getCsCode())) {
                    idsPresation.add(ligne.getIdPrestation());
                }
            }
        }
        return idsPresation;
    }

    private static BigDecimal sumMontantValide(Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> lignesDeblocage) {
        BigDecimal sum = new BigDecimal(0);
        for (Entry<EPCTypeDeblocage, List<SimpleLigneDeblocage>> entry : lignesDeblocage.entrySet()) {
            for (SimpleLigneDeblocage ligne : entry.getValue()) {
                if (EPCEtatDeblocage.VALIDE.getCsCode().equals(ligne.getCsEtat())) {
                    sum = sum.add(new BigDecimal(ligne.getMontant()));
                }
            }
        }
        return sum;
    }

    private static void updateEtatLigneBlocageToEnregistreAndDeleteIdPresation(
            Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> lignesDeblocage)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        for (Entry<EPCTypeDeblocage, List<SimpleLigneDeblocage>> entry : lignesDeblocage.entrySet()) {
            for (SimpleLigneDeblocage deblocage : entry.getValue()) {
                if (EPCEtatDeblocage.VALIDE.getCsCode().equals(deblocage.getCsEtat())) {
                    deblocage.setCsEtat(EPCEtatDeblocage.ENREGISTRE.getCsCode());
                    deblocage.setIdPrestation(null);
                    PegasusImplServiceLocator.getSimpleLigneDeblocageService().update(deblocage);
                }
            }
        }
    }

    private static void deleteLignesBlocages(Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> lignesDeblocage)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        for (Entry<EPCTypeDeblocage, List<SimpleLigneDeblocage>> entry : lignesDeblocage.entrySet()) {
            for (SimpleLigneDeblocage deblocage : entry.getValue()) {
                if (EPCEtatDeblocage.VALIDE.getCsCode().equals(deblocage.getCsEtat())) {
                    // deblocage.setCsEtat(EPCEtatDeblocage.ENREGISTRE.getCsCode());
                    // deblocage.setIdPrestation(null);
                    PegasusImplServiceLocator.getSimpleLigneDeblocageService().delete(deblocage);
                }
            }
        }
    }

    protected static void updateMontantDebloque(Deblocage deblocage) throws CorvusException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        BigDecimal sumMontanValide = DevaliderLiberation.sumMontantValide(deblocage.getLingesDeblocages());
        BigDecimal newMontantDebloque = new BigDecimal(deblocage.getPcaBloque().getMontantDebloque())
                .subtract(sumMontanValide);

        SimpleEnteteBlocage model = CorvusImplServiceLocator.getSimpleEnteteBlocageService().read(
                deblocage.getPcaBloque().getIdEnteteBlocage());

        model.setMontantDebloque(newMontantDebloque.toString());

        CorvusImplServiceLocator.getSimpleEnteteBlocageService().update(model);
    }

}
