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
 * Le but de cette class et de g�n�rer les ordres de versement pour les d�cisions de type suppression. Les ordres de
 * versement g�n�rer seront seulement des ov de type restitution.
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
     * On vas g�n�rer une ov par pca. Pour les couples s�parer par la maladie le noGroupe p�riode doit �tre identique
     * pour la m�me p�riode de pca. Si la date de fin de la pca est �gale � la date de suppression on ne cr�er pas d'ov
     * 
     * Une fois les ovs g�n�r� il est possible de conna�tre le montant total des ovs
     * 
     * Le plus grand num�ro du groupe de p�riode correspond � la PCA le plus r�cente
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
