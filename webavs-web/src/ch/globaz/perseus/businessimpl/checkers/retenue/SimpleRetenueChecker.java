package ch.globaz.perseus.businessimpl.checkers.retenue;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.perseus.business.constantes.CSTypeRetenue;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueException;
import ch.globaz.perseus.business.models.retenue.SimpleRetenue;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class SimpleRetenueChecker extends PerseusAbstractChecker {

    public static void checkForCreate(SimpleRetenue simpleRetenue) throws RetenueException, JadePersistenceException {
        SimpleRetenueChecker.checkMandatory(simpleRetenue);
    }

    public static void checkForDelete(SimpleRetenue simpleRetenue) throws RetenueException, JadePersistenceException {

    }

    public static void checkForUpdate(SimpleRetenue simpleRetenue) {
        SimpleRetenueChecker.checkMandatory(simpleRetenue);
    }

    private static void checkMandatory(SimpleRetenue simpleRetenue) {
        // Champs toujours obligatoires
        if (JadeStringUtil.isEmpty(simpleRetenue.getCsTypeRetenue())) {
            JadeThread.logError(SimpleRetenueChecker.class.getName(), "perseus.retenues.cstyperetenue.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleRetenue.getDateDebutRetenue())) {
            JadeThread.logError(SimpleRetenueChecker.class.getName(), "perseus.retenues.datedebut.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleRetenue.getIdPcfAccordee())) {
            JadeThread.logError(SimpleRetenueChecker.class.getName(), "perseus.retenues.idpcfaccordee.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleRetenue.getMontantRetenuMensuel())) {
            JadeThread
                    .logError(SimpleRetenueChecker.class.getName(), "perseus.retenues.montantretenumensuel.mandatory");
        }

        // Champs obligatoires si c'est un type de retenue "Impot à la source"
        if (CSTypeRetenue.IMPOT_SOURCE.getCodeSystem().equals(simpleRetenue.getCsTypeRetenue())) {
            if (JadeStringUtil.isEmpty(simpleRetenue.getTauxImposition())) {
                JadeThread.logError(SimpleRetenueChecker.class.getName(), "perseus.retenues.tauximposition.mandatory");
            }
        }

        // Champs obligatoires si c'est un type de retenue "Adresse de paiement"
        if (CSTypeRetenue.ADRESSE_PAIEMENT.getCodeSystem().equals(simpleRetenue.getCsTypeRetenue())) {
            if (JadeStringUtil.isEmpty(simpleRetenue.getMontantTotalARetenir())) {
                JadeThread.logError(SimpleRetenueChecker.class.getName(),
                        "perseus.retenues.montanttotalaretenir.mandatory");
            }
            if (JadeStringUtil.isEmpty(simpleRetenue.getIdTiersAdressePmt())) {
                JadeThread.logError(SimpleRetenueChecker.class.getName(),
                        "perseus.retenues.idtiersadressepmt.mandatory");
            }
            if (JadeStringUtil.isEmpty(simpleRetenue.getIdDomaineApplicatif())) {
                JadeThread.logError(SimpleRetenueChecker.class.getName(),
                        "perseus.retenues.iddomaineapplicatif.mandatory");
            }
        }

        // Champs obligatoires si c'est un type de retenue "Facture existante"
        if (CSTypeRetenue.FACTURE_EXISTANTE.getCodeSystem().equals(simpleRetenue.getCsTypeRetenue())) {
            if (JadeStringUtil.isEmpty(simpleRetenue.getMontantTotalARetenir())) {
                JadeThread.logError(SimpleRetenueChecker.class.getName(),
                        "perseus.retenues.montanttotalaretenir.mandatory");
            }
            // if (JadeStringUtil.isEmpty(simpleRetenue.getMontantDejaRetenu())) {
            // JadeThread.logError(SimpleRetenueChecker.class.getName(),
            // "perseus.retenues.montantdejaretenu.mandatory");
            // }
            if (JadeStringUtil.isEmpty(simpleRetenue.getIdCompteAnnexe())
                    || JadeStringUtil.isEmpty(simpleRetenue.getIdExterneSection())) {
                JadeThread.logError(SimpleRetenueChecker.class.getName(), "perseus.retenues.section.mandatory");
            }

        }
    }

}
