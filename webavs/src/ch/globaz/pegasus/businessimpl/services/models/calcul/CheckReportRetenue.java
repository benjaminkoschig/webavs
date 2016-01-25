package ch.globaz.pegasus.businessimpl.services.models.calcul;

import globaz.jade.persistence.model.JadeAbstractModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplace;
import ch.globaz.pegasus.business.models.pcaccordee.PcaRetenue;
import ch.globaz.pegasus.business.models.pcaccordee.PcaRetenueSearch;
import ch.globaz.pegasus.businessimpl.utils.RequerantConjoint;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueException;

class CheckReportRetenue {

    private CalculPcaReplace anciennePCACourante;
    private BigDecimal montantNewPca;
    private PcaRetenueSearch PcaRetenueSearch;
    private boolean isForDom2R;
    private RequerantConjoint<BigDecimal> sumRetenues;

    /**
     * @param anciennePCACourante: C'est l'ancienne PCA qui avait une retenue
     * @param montantNewPca: Montant de la nouvelle PCA
     * @param PcaRetenueSearch: Liste des retenues active
     * @param isForDom2R: Indique si la nouvelle PCA est un cas de DOM2R(Dommicil avec 2 rentes principales)
     * @throws RetenueException
     */
    public CheckReportRetenue(CalculPcaReplace anciennePCACourante, BigDecimal montantNewPca,
            PcaRetenueSearch PcaRetenueSearch, boolean isForDom2R) throws RetenueException {
        Checkers.checkNotNull(anciennePCACourante, "anciennePCACourante");
        Checkers.checkNotNull(montantNewPca, "montantNewPca");
        Checkers.checkNotNull(PcaRetenueSearch, "PcaRetenueSearch");
        Checkers.checkNotNull(isForDom2R, "isForDom2R");
        this.anciennePCACourante = anciennePCACourante;
        this.montantNewPca = montantNewPca;
        this.PcaRetenueSearch = PcaRetenueSearch;
        this.isForDom2R = isForDom2R;
        sumRetenues = sumRetenue();
    }

    /**
     * Vérifie que le montant de la nouvelle prestation du conjoint et du requérant sont suffisant par rapport à la
     * somme de retenues
     * 
     * @return boolean
     */
    public boolean isReporteRetenuePossible() {
        if (hasRetenue()) {
            return isReportRetenuePossibleWihtRetenue(montantNewPca, sumRetenues, isForDom2R);
        } else {
            return false;
        }
    }

    /**
     * @return le montantTotal de retenues pour le requerant
     */
    public BigDecimal getSumRetenueRequerant() {
        return sumRetenues.getRequerant();
    }

    /**
     * @return le montantTotal de retenues pour le conjoint
     */
    public BigDecimal getSumRetenueConjoint() {
        return sumRetenues.getConjoint();
    }

    /**
     * Vérifie que le montant de la prestation du conjoint est suffisant par rapport à la somme de ces retenues
     * 
     * @return boolean
     */
    public boolean isMontantPresationSuffisantConjoint() {
        RequerantConjoint<BigDecimal> montants = splitMontant(montantNewPca);
        return CheckReportRetenue.isMontantPresationSuffisant(montants.getConjoint(), sumRetenues.getConjoint());
    }

    /**
     * Vérifie que le montant de la prestation du requerant est suffisant par rapport à la somme de ces retenues
     * 
     * @return boolean
     */
    public boolean isMontantPresationSuffisantRequerant() {
        RequerantConjoint<BigDecimal> montants = splitMontant(montantNewPca);
        return CheckReportRetenue.isMontantPresationSuffisant(montants.getRequerant(), sumRetenues.getRequerant());
    }

    RequerantConjoint<BigDecimal> sumRetenue() throws RetenueException {
        RequerantConjoint<BigDecimal> sumRetenues = new RequerantConjoint<BigDecimal>();
        BigDecimal sumRequerant = BigDecimal.ZERO;
        BigDecimal sumConjoint = BigDecimal.ZERO;

        if (PcaRetenueSearch.getSearchResults() != null) {
            for (JadeAbstractModel model : PcaRetenueSearch.getSearchResults()) {
                PcaRetenue retenue = (PcaRetenue) model;
                BigDecimal montantRetenue = new BigDecimal(retenue.getSimpleRetenue().getMontantRetenuMensuel());
                if (isRetenuBoundToRequerant(retenue)) {
                    sumRequerant = sumRequerant.add(montantRetenue);
                } else if (isRetenuBoundToConjoint(retenue)) {
                    sumConjoint = sumConjoint.add(montantRetenue);
                } else {
                    throw new RetenueException("Unable to match the prestationAccordee with this id retenue: "
                            + retenue.getId());
                }
            }
        }
        sumRetenues.setConjoint(sumConjoint);
        sumRetenues.setRequerant(sumRequerant);
        return sumRetenues;
    }

    boolean hasRetenue() {
        return anciennePCACourante.getSimplePrestationsAccordees().getIsRetenues()
                || (anciennePCACourante.getSimplePrestationsAccordeesConjoint() != null && anciennePCACourante
                        .getSimplePrestationsAccordeesConjoint().getIsRetenues());
    }

    private boolean isRetenuBoundToRequerant(PcaRetenue retenue) {
        return retenue.getSimpleRetenue().getIdRenteAccordee()
                .equals(anciennePCACourante.getSimplePrestationsAccordees().getIdPrestationAccordee());
    }

    private boolean isRetenuBoundToConjoint(PcaRetenue retenue) {
        if (anciennePCACourante.getSimplePrestationsAccordeesConjoint() != null) {
            return retenue.getSimpleRetenue().getIdRenteAccordee()
                    .equals(anciennePCACourante.getSimplePrestationsAccordeesConjoint().getIdPrestationAccordee());
        } else {
            return false;
        }
    }

    static boolean isReportRetenuePossibleWihtRetenue(BigDecimal montantNewPca,
            RequerantConjoint<BigDecimal> sumRetenues, boolean isForDom2R) {
        if (isForDom2R) {
            RequerantConjoint<BigDecimal> montants = splitMontant(montantNewPca);
            return isMontantsSuiffisant(sumRetenues, montants);
        } else {
            BigDecimal sumRequerantConjoint = sumRetenues.getRequerant().add(sumRetenues.getConjoint());
            return isMontantPresationSuffisant(montantNewPca, sumRequerantConjoint);
        }
    }

    private static boolean isMontantsSuiffisant(RequerantConjoint<BigDecimal> sumRetenues,
            RequerantConjoint<BigDecimal> montants) {
        return isMontantPresationSuffisant(montants.getRequerant(), sumRetenues.getRequerant())
                && isMontantPresationSuffisant(montants.getConjoint(), sumRetenues.getConjoint());
    }

    static boolean isMontantPresationSuffisant(BigDecimal montantPca, BigDecimal sumRetenue) {
        return montantPca.compareTo(sumRetenue) == 1 || montantPca.compareTo(sumRetenue) == 0;
    }

    private static RequerantConjoint<BigDecimal> splitMontant(BigDecimal montant) {
        RequerantConjoint<BigDecimal> montants = new RequerantConjoint<BigDecimal>();
        BigDecimal montantRequerant = null;
        BigDecimal montantConjoint = new BigDecimal(0);
        montantConjoint = montant.divide(new BigDecimal(2), 0, RoundingMode.FLOOR);
        montantRequerant = montant.subtract(montantConjoint);
        montants.setConjoint(montantConjoint);
        montants.setRequerant(montantRequerant);
        return montants;
    }

}
