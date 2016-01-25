package ch.globaz.pegasus.businessimpl.services.models.decision.validation.suppression;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.externe.IPRConstantesExternes;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement.GenerateOrdversement;

/**
 * 
 * Le but de cette class et de générer les ordres de versement pour les décisions de type suppression. Les ordres de
 * versement générer seront seulement des ov de type restitution.
 * 
 * @author dma
 * 
 */
public class GenerateOvsForSuppression {

    private CalculRestitution calculRestitution;
    private String dateSuppression;

    public GenerateOvsForSuppression(String dateSuppression, String dateDernierPmt) {
        this.dateSuppression = dateSuppression;
        calculRestitution = new CalculRestitution(dateSuppression, dateDernierPmt);
    }

    /**
     * 
     * On vas générer une ov par pca. Pour les couples séparer par la maladie le noGroupe période doit être identique
     * pour la même période de pca. Si la date de fin de la pca est égale à la date de suppression on ne créer pas d'ov
     * 
     * Une fois les ovs généré il est possible de connaître le montant total des ovs
     * 
     * Le plus grand numéro du groupe de période correspond à la PCA le plus récente
     * 
     * @param pcas
     * 
     * @return Les ordres de versement de type restitution
     * @throws OrdreVersementException
     * @throws PCAccordeeException
     */
    public List<SimpleOrdreVersement> generateOv(List<PCAccordee> pcas) throws OrdreVersementException,
            PCAccordeeException {

        if (pcas == null) {
            throw new IllegalArgumentException("Unable to generateOv, the pcas is null!");
        }
        int i = 0;
        String dateDebut = null;

        List<SimpleOrdreVersement> ovs = new ArrayList<SimpleOrdreVersement>();
        for (PCAccordee pca : pcas) {

            if (dateSuppression.equals(pca.getSimplePCAccordee().getDateFin())) {
                continue;
            }
            if (!isPcaConjoint(dateDebut, pca)) {
                i++;
            }
            dateDebut = pca.getSimplePCAccordee().getDateDebut();
            BigDecimal montant = calculRestitution.calculeMontantRestitution(pca);

            SimpleOrdreVersement ov = this.generateOv(pca, montant, i);
            ovs.add(ov);
        }

        return ovs;
    }

    private SimpleOrdreVersement generateOv(PCAccordee pca, BigDecimal montant, int noOv)
            throws OrdreVersementException {
        String csDomaine = GenerateOrdversement.dettermineCsDomaineOv(pca.getSimplePCAccordee().getCsTypePC());
        SimpleOrdreVersement simpleOrdreVersement = new SimpleOrdreVersement();
        simpleOrdreVersement.setCsTypeDomaine(csDomaine);
        simpleOrdreVersement.setCsType(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION);
        simpleOrdreVersement.setIdTiers(pca.getSimplePrestationsAccordees().getIdTiersBeneficiaire());
        simpleOrdreVersement.setIdTiersAdressePaiement(pca.getSimpleInformationsComptabilite().getIdTiersAdressePmt());
        simpleOrdreVersement.setIdTiersAdressePaiementConjoint(pca.getSimpleInformationsComptabiliteConjoint()
                .getIdTiersAdressePmt());
        simpleOrdreVersement.setIdTiersConjoint(pca.getSimplePrestationsAccordeesConjoint().getIdTiersBeneficiaire());
        simpleOrdreVersement.setIdDomaineApplication(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
        simpleOrdreVersement.setIdPca(pca.getSimplePCAccordee().getId());
        simpleOrdreVersement.setNoGroupePeriode(String.valueOf(noOv));
        simpleOrdreVersement.setMontant(montant.toString());
        simpleOrdreVersement.setSousTypeGenrePrestation(pca.getSimplePrestationsAccordees().getSousCodePrestation());
        return simpleOrdreVersement;
    }

    /**
     * @return le montant total des ovs
     */
    public BigDecimal getMontantTotalRestitution() {
        return calculRestitution.getTotal();
    }

    private boolean isPcaConjoint(String dateDebut, PCAccordee pca) {
        return pca.getSimplePCAccordee().getDateDebut().equals(dateDebut);
    }
}
