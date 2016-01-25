package ch.globaz.pegasus.businessimpl.services.models.decision.validation.suppression;

import globaz.corvus.api.prestations.IREPrestations;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.List;
import ch.globaz.common.domaine.GroupePeriodes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPresation;
import ch.globaz.pegasus.business.models.lot.SimplePrestation;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.utils.periode.GroupePeriodesResolver;
import ch.globaz.utils.periode.GroupePeriodesResolver.EachPeriode;

/**
 * Cette class permet de générer des prestations pour les décisions de suppression. La date date de début de la
 * prestations vas correspondre à la date de suppression moins un mois. Pour la date de fin si une pca à pas de date de
 * fin on vas utilisé la date du dernier paiement sinon on prend la plus grande date de fin des pca.
 * 
 * 
 * @author dma
 * 
 */
public class GeneratePrestation {
    private String dateDernierPmt;
    private String dateSuppression = null;
    private String idCompteAnnexeConjoint = null;
    private String idCompteAnnexeRequerant = null;

    public GeneratePrestation(String dateDernierPmt, String dateSuppression) {
        if (JadeStringUtil.isBlankOrZero(dateDernierPmt)) {
            throw new IllegalArgumentException("Unable to GeneratePrestation, the dateDernierPmt is null!");
        }
        this.dateDernierPmt = dateDernierPmt;
        this.dateSuppression = dateSuppression;
    }

    public SimplePrestation generate(List<PCAccordee> pcas, BigDecimal montant, String idVersionDroit) {

        if (pcas.isEmpty()) {
            throw new IllegalArgumentException("The list of pca is empty! idVersionDroit:" + idVersionDroit);
        }

        String idTiers = pcas.get(0).getSimplePrestationsAccordees().getIdTiersBeneficiaire();

        resolveCompteAnnexe(pcas);

        GroupePeriodes periodes = generatePeriode(pcas);

        SimplePrestation prestation = new SimplePrestation();
        prestation.setMontantTotal(montant.negate().toString());
        prestation.setIdVersionDroit(idVersionDroit);
        prestation.setCsEtat(IREPrestations.CS_ETAT_PRE_DEFINITIF);
        prestation.setCsTypePrestation(IPCPresation.CS_TYPE_DE_PRESTATION_DECISION);
        prestation.setIdTiersBeneficiaire(idTiers);
        prestation.setIdCompteAnnexeRequerant(idCompteAnnexeRequerant);
        prestation.setIdCompteAnnexeConjoint(idCompteAnnexeConjoint);
        prestation.setDateDebut(resolveDateDebut());
        prestation.setDateFin(periodes.getDateFinMax());

        if (periodes.hasDateFinNullValue()) {
            prestation.setDateFin(dateDernierPmt);
        } else {
            prestation.setDateFin(periodes.getDateFinMax());
        }

        return prestation;
    }

    private GroupePeriodes generatePeriode(List<PCAccordee> pcas) {
        GroupePeriodes periodes = GroupePeriodesResolver.genearateListPeriode(pcas, new EachPeriode<PCAccordee>() {
            @Override
            public String[] dateDebutFin(PCAccordee t) {
                return new String[] { t.getSimplePCAccordee().getDateDebut(), t.getSimplePCAccordee().getDateFin() };
            }
        });
        return periodes;
    }

    private void resolveCompteAnnexe(List<PCAccordee> pcas) {
        for (PCAccordee pca : pcas) {
            String csRole = pca.getSimplePCAccordee().getCsRoleBeneficiaire();
            String id = pca.getSimpleInformationsComptabilite().getIdCompteAnnexe();
            if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(csRole)) {
                idCompteAnnexeRequerant = id;
            }
            if (IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(csRole)) {
                idCompteAnnexeConjoint = id;
            }
        }
    }

    /**
     * On ajoute un mois car la période de suppression se fait après la date de suppression
     * 
     * @return
     */
    private String resolveDateDebut() {
        return JadeDateUtil.addMonths("01." + dateSuppression, 1).substring(3);
    }

}
