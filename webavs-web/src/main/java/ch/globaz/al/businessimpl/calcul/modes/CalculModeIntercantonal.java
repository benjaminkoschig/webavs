package ch.globaz.al.businessimpl.calcul.modes;

import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.calcul.CalculBusinessService;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;
import ch.globaz.al.properties.ALProperties;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

import java.util.ArrayList;
import java.util.List;

/**
 * Mode de calcul intercantonal
 * 
 * Il effectue le calcul des droits pour l'allocataire puis selon le tarif applicable dans le canton de l'employeur de
 * l'autre parent. Finalement il effectue une comparaison entre les deux et en retourne le résultat
 * 
 * @author jts
 */
public class CalculModeIntercantonal extends CalculModeAbstract {

    /**
     * Effectue une comparaison entre les montants déterminés pour l'allocataire et ceux déterminés pour l'autre parent.
     * Si la différence est supérieure ou égale à zéro, c'est ce montant qui est utilisé, sinon le montant est fixé à
     * 0.0
     * 
     * @param dossier
     *            Dossier pour lequel le calcul est exécuté
     * @param droits
     *            Liste des droits calculés selon le tarif de l'allocataire
     * @param droitsAutreParent
     *            Liste des droits calculés selon le tarif du canton de l'autre parent
     * @return Liste des droits calculés selon le mode intercantonal
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected List<CalculBusinessModel> compare(DossierModel dossier, List<CalculBusinessModel> droits,
            List<CalculBusinessModel> droitsAutreParent) throws JadeApplicationException {

        if (dossier == null) {
            throw new ALCalculException("CalculModeIntercantonal:compare : dossier is null");
        }

        if (droits == null) {
            throw new ALCalculException("CalculModeIntercantonal:compare : droits is null");
        }

        if (droitsAutreParent == null) {
            throw new ALCalculException("CalculModeIntercantonal:compare : droitsAutreParent is null");
        }

        double diff = 0.0;
        double diffIS = 0.0;
        int j = 0;
        boolean found = false;

        CalculBusinessModel droitAllocataire, droitAutreParent;

        // boucle des droits de l'allocataire
        for (int i = 0; i < droits.size(); i++) {

            found = false;
            j = 0;
            droitAllocataire = (droits.get(i));

            // boucle sur les droits de l'autre parent
            while ((j < droitsAutreParent.size()) && !found) {

                droitAutreParent = (droitsAutreParent.get(j));

                // si l'id du droit est le même c'est qu'on a trouvé les
                // droits correspondant
                if (droitAutreParent.getDroit().getId().equals(droitAllocataire.getDroit().getId())) {

                    found = true;

                    // pas de comparaison pour les droits de ménage
                    if (ALCSDroit.TYPE_ENF.equals(droitAllocataire.getType())
                            || ALCSDroit.TYPE_FORM.equals(droitAllocataire.getType())
                            || ALCSDroit.TYPE_NAIS.equals(droitAllocataire.getType())
                            || ALCSDroit.TYPE_ACCE.equals(droitAllocataire.getType())) {

                        // montant allocataire - montant autre parent

                        // ENF ou FORM forcé
                        if ((ALCSDroit.TYPE_ENF.equals(droitAllocataire.getType()) || ALCSDroit.TYPE_FORM
                                .equals(droitAllocataire.getType()))
                                && JadeNumericUtil.isNumericPositif(droitAllocataire.getDroit().getDroitModel()
                                        .getMontantForce())) {
                            diff = Double.parseDouble(droitAllocataire.getCalculResultMontantBase());
                            if(ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()
                                && !JadeStringUtil.isEmpty(droitAllocataire.getCalculResultMontantIS())) {
                                diffIS = Double.parseDouble(droitAllocataire.getCalculResultMontantIS());
                            }
                            // NAIS ou ACCE forcé
                        } else if ((ALCSDroit.TYPE_NAIS.equals(droitAllocataire.getType()) || ALCSDroit.TYPE_ACCE
                                .equals(droitAllocataire.getType()))
                                && JadeNumericUtil.isNumericPositif(droitAllocataire.getDroit().getEnfantComplexModel()
                                        .getEnfantModel().getMontantAllocationNaissanceFixe())) {

                            diff = Double.parseDouble(droitAllocataire.getDroit().getEnfantComplexModel()
                                    .getEnfantModel().getMontantAllocationNaissanceFixe());
                        } else {
                            diff = Double.parseDouble(droitAllocataire.getCalculResultMontantBase())
                                    - Double.parseDouble(droitAutreParent.getCalculResultMontantBase());
                            if(ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()
                                    && !JadeStringUtil.isEmpty(droitAllocataire.getCalculResultMontantIS())
                                    && !JadeStringUtil.isEmpty(droitAutreParent.getCalculResultMontantIS())) {
                                diffIS = Double.parseDouble(droitAllocataire.getCalculResultMontantIS())
                                        - Double.parseDouble(droitAutreParent.getCalculResultMontantIS());
                            }
                        }

                        droitAllocataire.setMontantAutreParent(droitAutreParent.getCalculResultMontantBase());

                        droitAllocataire.setMontantAllocataire(droitAllocataire.getCalculResultMontantBase());

                        if (diff >= 0.0) {
                            droitAllocataire.setCalculResultMontantBase(String.valueOf(diff));
                        } else {
                            droitAllocataire.setCalculResultMontantBase("0.0");
                            droitAllocataire.setCalculResultMontantEffectif("0.0");
                        }

                        if(ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()) {
                            if (diffIS >= 0.0) {
                                droitAllocataire.setCalculResultMontantIS(String.valueOf(diffIS));
                            } else {
                                droitAllocataire.setCalculResultMontantIS("0.0");
                            }
                        }

                        // on retire le droit de la liste, on en aura plus
                        // besoin
                        droitsAutreParent.remove(j);
                    } else {
                        droitAllocataire.setMontantAutreParent(droitAutreParent.getCalculResultMontantBase());
                        droitAllocataire.setMontantAllocataire(droitAllocataire.getCalculResultMontantBase());
                    }
                }

                j++;
            }

            // si aucun montant n'a été calculé pour l'autre parent => 0
            if (!found) {
                droitAllocataire.setMontantAutreParent("0.0");
                droitAllocataire.setMontantAllocataire(droitAllocataire.getCalculResultMontantBase());
            }

        }

        return droits;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.calcul.modes.CalculMode#compute(ch.globaz.al
     * .business.models.dossier.DossierComplexModel, java.lang.String)
     */
    @Override
    public List<CalculBusinessModel> compute(ContextCalcul context) throws JadeApplicationException,
            JadePersistenceException {

        if (context == null) {
            throw new ALCalculException("CalculModeJura#compute : context is null");
        }

        initCalculMode(context);

        // parcours des droits
        super.processDroits(context.getDossier(), context.getDateCalcul());

        // si on est pas déjà en train de traiter les droits de l'autre parent
        if (JadeStringUtil.isNull(JadeThread.currentContext().getTemporaryAttribute("TARIF_AUTRE_PARENT"))) {
            compare(context.getDossier().getDossierModel(), droitsCalcules,
                    computeAutreParent(context.getDossier(), context.getDateCalcul()));
        }

        return droitsCalcules;
    }

    /**
     * Effectue le calcul des droits pour l'autre parent
     * 
     * @param dossier
     *            Dossier pour lequel les droits sont calculés
     * @param dateCalcul
     *            Date pour laquelle le calcul est exécuté
     * @return Liste des droits calcul selon les tarifs en vigueur dans le canton de l'autre parent
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected List<CalculBusinessModel> computeAutreParent(DossierComplexModelRoot dossier, String dateCalcul)
            throws JadeApplicationException, JadePersistenceException {
        try {

            JadeThread.currentContext().setTemporaryAttribute("TARIF_AUTRE_PARENT",
                    dossier.getDossierModel().getLoiConjoint());

            CalculBusinessService calc = ALServiceLocator.getCalculBusinessService();
            return calc.getCalcul(dossier, dateCalcul);

        } finally {
            JadeThread.currentContext().setTemporaryAttribute("TARIF_AUTRE_PARENT", null);
        }
    }
}