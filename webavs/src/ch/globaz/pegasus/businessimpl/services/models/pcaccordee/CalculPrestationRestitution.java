package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class CalculPrestationRestitution {

    private String dateSuppression = null;

    private PCAccordee pca = null;

    private BigDecimal total = new BigDecimal(0);

    public CalculPrestationRestitution(PCAccordee pca, String dateSuppression) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, PmtMensuelException {
        // this.idsPcaForCalcul = idsPcaForcalcul;
        this.pca = pca;
        this.dateSuppression = dateSuppression;
        // this.calculeMontantRestitution();
    }

    public BigDecimal calculeMontantRestitutionForPca(boolean courantTraite) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, PmtMensuelException {
        // recherche des pca pour le calcul

        // PCAccordeeSearch pcaSearch = new PCAccordeeSearch();
        // pcaSearch.setInIdPCAccordee(this.idsPcaForCalcul);
        // pcaSearch = PegasusImplServiceLocator.getPCAccordeeService().search(pcaSearch);
        // boolean courantTraite = false;// Définit si le courant a ete traite, TOUJOURS le premier résultat!!!
        // Itération des pca
        // for (JadeAbstractModel pca : pcaSearch.getSearchResults()) {
        // PCAccordee pcaOld = (PCAccordee) pca;
        BigDecimal montantPca = new BigDecimal(pca.getSimplePrestationsAccordees().getMontantPrestation());

        // si prestations pour conjoitn
        if (!JadeStringUtil.isBlankOrZero(pca.getSimplePrestationsAccordeesConjoint().getMontantPrestation())) {
            montantPca = montantPca.add(new BigDecimal(pca.getSimplePrestationsAccordeesConjoint()
                    .getMontantPrestation()));
        }
        int nbreMois = 0;

        // Si la date de suppression est au millieux d'une période
        // on enleve le +1, vu que date de fin période précédente
        // on ajoute un mois à la date de suppression, pour avoir la date de debut pour le calcul

        if (!courantTraite) {
            dateSuppression = JadeDateUtil.addMonths("01." + dateSuppression, 1).substring(3);
        }
        // String dateDernierPaiement = Pegasus

        String dateFin = pca.getSimplePCAccordee().getDateFin();
        String dateDebut = pca.getSimplePCAccordee().getDateDebut();

        if (JadeDateUtil.isDateMonthYearAfter(dateSuppression, pca.getSimplePCAccordee().getDateDebut())) {
            dateDebut = dateSuppression;
        }

        // Si la date de fin est vide on prend la date du prochain paiement
        if (JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getDateFin())) {
            dateFin = PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
        }

        nbreMois = JadeDateUtil.getNbMonthsBetween("01." + dateDebut, "01." + dateFin);
        nbreMois++; // on incrémente pour avoir le bon nombre de mois

        float totalForPca = -nbreMois * Float.parseFloat(montantPca.toString());
        // System.out.println("dateDebut: " + dateDebut + " dateFin: " + dateFin + " montant: " + montantPca.toString()
        // + " nbreMois:" + nbreMois + " =>" + totalForPca);
        return new BigDecimal(totalForPca);
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
