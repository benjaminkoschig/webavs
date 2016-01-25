package ch.globaz.al.businessimpl.generation.prestations;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.constantes.ALConstPrestations.TypeMontantForce;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextTucana;

/**
 * Factory permettant de déterminer le type de génération à exécuter en fonction des informations contenue dans un
 * context
 * 
 * @author jts
 * @see ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie
 * @see ch.globaz.al.businessimpl.generation.prestations.context.ContextDossier
 * @see ch.globaz.al.businessimpl.generation.prestations.context.ContextPrestation
 */
public class GenPrestationFactory {

    /**
     * Pour un cas de dossier ADI, retourne une instance de la classe de génération appropriée en fonction des
     * informations contenues dans le context passé en paramètre
     * 
     * @param context
     *            Contexte contenant les informations nécessaires à la génération
     * @return instance d'une classe de génération
     * @throws ALGenerationException
     */
    private static GenPrestation getClassGenADI(ContextAffilie context) throws ALGenerationException {
        if (ALConstPrestations.TypeGeneration.ADI_TEMPORAIRE.equals(context.getContextDossier().getTypeGenPrestation())) {

            if (GenPrestationFactory.isUnitaire(context)
                    && JadeNumericUtil.isEmptyOrZero(context.getContextDossier().getNbUnites())) {
                return new GenPrestationUnitaireZero(context);
            } else {
                return new GenPrestationADITemporaire(context);
            }
        } else if (ALConstPrestations.TypeGeneration.ADI_DEFINITIF.equals(context.getContextDossier()
                .getTypeGenPrestation())) {
            return new GenPrestationADI(context);
        } else {
            throw new ALGenerationException(
                    "GenPrestationFactory#getClassGenADI : the appropriate generation class can not be defined");
        }
    }

    /**
     * Pour un cas de génération globale, retourne une instance de la classe de génération appropriée en fonction des
     * informations contenues dans le context passé en paramètre
     * 
     * @param context
     *            Contexte contenant les informations nécessaires à la génération
     * @return instance d'une classe de génération
     */
    private static GenPrestation getClassGenGlobale(ContextAffilie context) {
        if (GenPrestationFactory.isUnitaire(context)) {
            return new GenPrestationUnitaireZero(context);
        } else {
            return new GenPrestationStandard(context);
        }
    }

    /**
     * Pour un cas de dossier avec montant forcé, retourne une instance de la classe de génération appropriée en
     * fonction des informations contenues dans le context passé en paramètre
     * 
     * @param context
     *            Contexte contenant les informations nécessaires à la génération
     * @return instance d'une classe de génération
     * @throws ALGenerationException
     */
    private static GenPrestation getClassGenMontantForce(ContextAffilie context) throws ALGenerationException {
        TypeMontantForce type = context.getContextDossier().getTypeMontantForce();

        if (TypeMontantForce.MONTANT_FORCE_GEN.equals(type)) {
            return new GenPrestationForcee(context);
        } else if (TypeMontantForce.MONTANT_FORCE_DOSSIER.equals(type)) {
            return new GenPrestationForceeDossier(context);
        } else {
            throw new ALGenerationException(
                    "GenPrestationFactory#getClassGenMontantForce : the appropriate generation class can not be defined, unknown TypeMontantForce");
        }
    }

    /**
     * Pour un cas de dossier unitaire (jour ou heure), retourne une instance de la classe de génération appropriée en
     * fonction des informations contenues dans le context passé en paramètre
     * 
     * @param context
     *            Contexte contenant les informations nécessaires à la génération
     * @return instance d'une classe de génération
     */
    private static GenPrestation getClassGenUnitaire(ContextAffilie context) {
        if (JadeNumericUtil.isEmptyOrZero(context.getContextDossier().getNbUnites())) {
            return new GenPrestationUnitaireZero(context);
        } else {
            return new GenPrestationUnitaire(context);
        }
    }

    /**
     * Retourne une instance de la classe de génération appropriée en fonction des informations passées en paramètre
     * contenues dans le context passé en paramètre
     * 
     * @param context
     *            Contexte contenant les informations nécessaires à la génération
     * 
     * @return instance d'une classe de génération
     * @throws JadeApplicationException
     *             Exception levée si aucun type de génération n'a pu être identifié
     * @throws JadePersistenceException
     */
    public static GenPrestation getGenPrestation(ContextAffilie context) throws JadeApplicationException,
            JadePersistenceException {

        if (context == null) {
            throw new ALGenerationException("GenPrestationFactory#getGenPrestation : context is null");
        }

        // si un montant a été forcé (y compris cas de restitution).
        if (!JadeNumericUtil.isEmptyOrZero(context.getContextDossier().getMontantForce())) {
            return GenPrestationFactory.getClassGenMontantForce(context);

            // génération d'un dossier
        } else if (ALCSPrestation.GENERATION_TYPE_GEN_DOSSIER.equals(context.getTypeGeneration())) {

            // génération d'une restitutions
            if (context.getContextDossier().isRestitution() || context.getContextDossier().isExtourne()) {
                return new GenPrestationRestitution(context);

                // Cas de génération ADI
            } else if (ALCSDossier.STATUT_IS.equals(context.getContextDossier().getDossier().getDossierModel()
                    .getStatut())) {
                return GenPrestationFactory.getClassGenADI(context);

                // Cas de dossier au jour ou à l'heure
            } else if (GenPrestationFactory.isUnitaire(context)) {
                return GenPrestationFactory.getClassGenUnitaire(context);

                // Cas standard
            } else {
                if (ContextTucana.tucanaIsEnabled()) {
                    return new GenPrestationDossierHorloger(context);
                } else {
                    return new GenPrestationDossier(context);
                }
            }
            // génération globale/affilié
        } else if (ALCSPrestation.GENERATION_TYPE_GEN_AFFILIE.equals(context.getTypeGeneration())
                || ALCSPrestation.GENERATION_TYPE_GEN_GLOBALE.equals(context.getTypeGeneration())) {
            return GenPrestationFactory.getClassGenGlobale(context);

        } else {
            throw new ALGenerationException(
                    "GenPrestationFactory#getGenPrestation : the appropriate generation class can not be defined");
        }
    }

    /**
     * Vérifie si le dossier est de type unitaire (jour ou heure)
     * 
     * @param context
     *            Contexte contenant les informations nécessaires à la génération
     * @return <code>true</code> si le dossier est unitaire, <code>false</code> sinon
     */
    private static boolean isUnitaire(ContextAffilie context) {
        return ALCSDossier.UNITE_CALCUL_HEURE.equals(context.getContextDossier().getDossier().getDossierModel()
                .getUniteCalcul())
                || ALCSDossier.UNITE_CALCUL_JOUR.equals(context.getContextDossier().getDossier().getDossierModel()
                        .getUniteCalcul());
    }
}
