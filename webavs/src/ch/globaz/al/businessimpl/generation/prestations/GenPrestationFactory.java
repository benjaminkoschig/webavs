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
 * Factory permettant de d�terminer le type de g�n�ration � ex�cuter en fonction des informations contenue dans un
 * context
 * 
 * @author jts
 * @see ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie
 * @see ch.globaz.al.businessimpl.generation.prestations.context.ContextDossier
 * @see ch.globaz.al.businessimpl.generation.prestations.context.ContextPrestation
 */
public class GenPrestationFactory {

    /**
     * Pour un cas de dossier ADI, retourne une instance de la classe de g�n�ration appropri�e en fonction des
     * informations contenues dans le context pass� en param�tre
     * 
     * @param context
     *            Contexte contenant les informations n�cessaires � la g�n�ration
     * @return instance d'une classe de g�n�ration
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
     * Pour un cas de g�n�ration globale, retourne une instance de la classe de g�n�ration appropri�e en fonction des
     * informations contenues dans le context pass� en param�tre
     * 
     * @param context
     *            Contexte contenant les informations n�cessaires � la g�n�ration
     * @return instance d'une classe de g�n�ration
     */
    private static GenPrestation getClassGenGlobale(ContextAffilie context) {
        if (GenPrestationFactory.isUnitaire(context)) {
            return new GenPrestationUnitaireZero(context);
        } else {
            return new GenPrestationStandard(context);
        }
    }

    /**
     * Pour un cas de dossier avec montant forc�, retourne une instance de la classe de g�n�ration appropri�e en
     * fonction des informations contenues dans le context pass� en param�tre
     * 
     * @param context
     *            Contexte contenant les informations n�cessaires � la g�n�ration
     * @return instance d'une classe de g�n�ration
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
     * Pour un cas de dossier unitaire (jour ou heure), retourne une instance de la classe de g�n�ration appropri�e en
     * fonction des informations contenues dans le context pass� en param�tre
     * 
     * @param context
     *            Contexte contenant les informations n�cessaires � la g�n�ration
     * @return instance d'une classe de g�n�ration
     */
    private static GenPrestation getClassGenUnitaire(ContextAffilie context) {
        if (JadeNumericUtil.isEmptyOrZero(context.getContextDossier().getNbUnites())) {
            return new GenPrestationUnitaireZero(context);
        } else {
            return new GenPrestationUnitaire(context);
        }
    }

    /**
     * Retourne une instance de la classe de g�n�ration appropri�e en fonction des informations pass�es en param�tre
     * contenues dans le context pass� en param�tre
     * 
     * @param context
     *            Contexte contenant les informations n�cessaires � la g�n�ration
     * 
     * @return instance d'une classe de g�n�ration
     * @throws JadeApplicationException
     *             Exception lev�e si aucun type de g�n�ration n'a pu �tre identifi�
     * @throws JadePersistenceException
     */
    public static GenPrestation getGenPrestation(ContextAffilie context) throws JadeApplicationException,
            JadePersistenceException {

        if (context == null) {
            throw new ALGenerationException("GenPrestationFactory#getGenPrestation : context is null");
        }

        // si un montant a �t� forc� (y compris cas de restitution).
        if (!JadeNumericUtil.isEmptyOrZero(context.getContextDossier().getMontantForce())) {
            return GenPrestationFactory.getClassGenMontantForce(context);

            // g�n�ration d'un dossier
        } else if (ALCSPrestation.GENERATION_TYPE_GEN_DOSSIER.equals(context.getTypeGeneration())) {

            // g�n�ration d'une restitutions
            if (context.getContextDossier().isRestitution() || context.getContextDossier().isExtourne()) {
                return new GenPrestationRestitution(context);

                // Cas de g�n�ration ADI
            } else if (ALCSDossier.STATUT_IS.equals(context.getContextDossier().getDossier().getDossierModel()
                    .getStatut())) {
                return GenPrestationFactory.getClassGenADI(context);

                // Cas de dossier au jour ou � l'heure
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
            // g�n�ration globale/affili�
        } else if (ALCSPrestation.GENERATION_TYPE_GEN_AFFILIE.equals(context.getTypeGeneration())
                || ALCSPrestation.GENERATION_TYPE_GEN_GLOBALE.equals(context.getTypeGeneration())) {
            return GenPrestationFactory.getClassGenGlobale(context);

        } else {
            throw new ALGenerationException(
                    "GenPrestationFactory#getGenPrestation : the appropriate generation class can not be defined");
        }
    }

    /**
     * V�rifie si le dossier est de type unitaire (jour ou heure)
     * 
     * @param context
     *            Contexte contenant les informations n�cessaires � la g�n�ration
     * @return <code>true</code> si le dossier est unitaire, <code>false</code> sinon
     */
    private static boolean isUnitaire(ContextAffilie context) {
        return ALCSDossier.UNITE_CALCUL_HEURE.equals(context.getContextDossier().getDossier().getDossierModel()
                .getUniteCalcul())
                || ALCSDossier.UNITE_CALCUL_JOUR.equals(context.getContextDossier().getDossier().getDossierModel()
                        .getUniteCalcul());
    }
}
