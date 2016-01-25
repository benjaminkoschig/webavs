package ch.globaz.al.businessimpl.calcul.modes;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALConstAttributsEntite;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.allocataire.AllocataireAgricoleComplexModel;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;
import ch.globaz.al.business.models.attribut.AttributEntiteSearchModel;
import ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * Classe fournissant les méthodes nécessaire à l'identification du mode de calcul à appliquer en fonction du contexte
 * courant
 * 
 * @author jts
 */
public abstract class CalculModeFactory {

    /**
     * Enum représentant les modes de calcul possibles
     * 
     * @author jts
     * 
     */
    private static enum CalculModes {
        AGR_MONTAGNE_JU("ch.globaz.al.businessimpl.calcul.modes.CalculModeAgriculteurMontagneJura"),
        AGR_PLAINE_JU("ch.globaz.al.businessimpl.calcul.modes. CalculModeAgriculteurPlaineJura"),
        AGRICULTURE("ch.globaz.al.businessimpl.calcul.modes.CalculModeAgriculture"),
        COLLAB_AGR_MONT_JU("ch.globaz.al.businessimpl.calcul.modes.CalculModeCollaborateurAgricoleMontagneJura"),
        FPV("ch.globaz.al.businessimpl.calcul.modes.CalculModeFPV"),
        FPV_VISANA("ch.globaz.al.businessimpl.calcul.modes.CalculModeFPVVisana"),
        INTERCANTO("ch.globaz.al.businessimpl.calcul.modes.CalculModeIntercantonal"),
        INTERCANTO_AGR("ch.globaz.al.businessimpl.calcul.modes.CalculModeIntercantonalAgricole"),
        JURA("ch.globaz.al.businessimpl.calcul.modes.CalculModeJura"),
        STANDARD("ch.globaz.al.businessimpl.calcul.modes.CalculModeStandard");

        private String classe;

        private CalculModes(String classe) {
            this.classe = classe;
        }

        /**
         * Retourne une instance de la classe implémentant <code>CalculMode</code>
         * 
         * @return instance de la classe de mode de calcul
         * @throws JadeApplicationException
         *             Exception levée si la classe n'a pas pu être instanciée
         */
        public CalculMode getCalculModeClass() throws JadeApplicationException {

            try {
                return (CalculMode) Class.forName(classe).newInstance();

            } catch (ClassNotFoundException e) {
                throw new ALCalculException(
                        "CalculModes#getCalculModeClass : La classe correspondant au mode de calcul n'a pas été trouvée",
                        e);
            } catch (InstantiationException e) {
                throw new ALCalculException(
                        "CalculModes#getCalculModeClass : La classe correspondant au mode de calcul n'a pas pu être instanciée",
                        e);
            } catch (IllegalAccessException e) {
                throw new ALCalculException(
                        "CalculModes#getCalculModeClass : La classe correspondant au mode de calcul n'a pas pu être instanciée",
                        e);
            }
        }
    }

    /**
     * Vérifie si le dossier correspond aux critères suivants :
     * 
     * <ul>
     * <li>non LAFam</li>
     * <li>agricole</li>
     * <li>tarif jurassien</li>
     * <li>agriculteur ou collaborateur agricole</li>
     * </ul>
     * 
     * @param context
     *            contexte contenant les données nécessaire à l'exécution du calcul
     * @return true si tous les critères sont vérifiés, false sinon
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static boolean checkAgriculteurJura(ContextCalcul context) throws JadePersistenceException,
            JadeApplicationException {

        if (context == null) {
            throw new ALCalculException("CalculModeFactory#checkAgriculteurJura : context is null");
        }

        // si agriculteur ou collaborateur agricole
        if (ALCSDossier.ACTIVITE_AGRICULTEUR.equals(context.getDossier().getDossierModel().getActiviteAllocataire())
                || ALCSDossier.ACTIVITE_COLLAB_AGRICOLE.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {

            /**
             * si :
             * 
             * - non LAFam
             * 
             * - agricole
             * 
             * - tarif jurassien
             * 
             */
            if (!ALImplServiceLocator.getCalculService().isDateLAFam(context.getDateCalcul())
                    && ALServiceLocator.getDossierBusinessService().isAgricole(
                            context.getDossier().getDossierModel().getActiviteAllocataire())
                    && ALCSTarif.CATEGORIE_JU.equals(context.getTarif())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne le mode de calcul correspondant au contexte courant
     * 
     * @param context
     *            contexte contenant les données nécessaire à l'exécution du calcul
     * @return Mode à utiliser pour l'exécution du calcul.
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see CalculMode
     */
    public static CalculMode getCalculMode(ContextCalcul context) throws JadePersistenceException,
            JadeApplicationException {

        if (context == null) {
            throw new ALCalculException("CalculModeFactory#CalculModeFactory# : context is null");
        }

        ParameterModel nomCaisse = ParamServiceLocator.getParameterModelService().getParameterByName(
                ALConstParametres.APPNAME, ALConstParametres.NOM_CAISSE, context.getDateCalcul());

        if ("FPV".equalsIgnoreCase(nomCaisse.getValeurAlphaParametre())) {
            return CalculModeFactory.getCalculModeFPV(context);
        } else {

            // dossier dans une caisse horlogère
            if (ALImplServiceLocator.getHorlogerBusinessService().isCaisseHorlogere()
                    && !JadeDateUtil.isDateBefore(context.getDateCalcul(), CalculModeHorloger.DATE_DEBUT_SUP_HORLO)) {

                if (ALCSDossier.STATUT_CS.equals(context.getDossier().getDossierModel().getStatut())) {
                    return new CalculModeIntercantonalHorloger();
                } else {
                    return new CalculModeHorloger();
                }
            }

            // dossier intercantonal
            else if (ALCSDossier.STATUT_CS.equals(context.getDossier().getDossierModel().getStatut())) {

                if (ALServiceLocator.getDossierBusinessService().isAgricole(
                        context.getDossier().getDossierModel().getActiviteAllocataire())) {
                    return new CalculModeIntercantonalAgricole();
                } else {
                    return new CalculModeIntercantonal();
                }

                // agriculteur jurassien de plaine avant le régime LAFam
            } else if (CalculModeFactory.checkAgriculteurJura(context)) {

                AllocataireAgricoleComplexModel allocataire = ((DossierAgricoleComplexModel) context.getDossier())
                        .getAllocataireAgricoleComplexModel();
                // domaine de montagne
                if (allocataire.getAgricoleModel().getDomaineMontagne().booleanValue()) {
                    if (ALCSDossier.ACTIVITE_AGRICULTEUR.equals(context.getDossier().getDossierModel()
                            .getActiviteAllocataire())) {
                        return new CalculModeAgriculteurMontagneJura();
                    } else if (ALCSDossier.ACTIVITE_COLLAB_AGRICOLE.equals(context.getDossier().getDossierModel()
                            .getActiviteAllocataire())) {
                        return new CalculModeCollaborateurAgricoleMontagneJura();
                    } else {
                        throw new ALCalculException("CalculModeFactory#getCalculMode : unable to find apropriate mode");
                    }
                    // domaine de plaine
                } else {
                    if (ALCSDossier.ACTIVITE_AGRICULTEUR.equals(context.getDossier().getDossierModel()
                            .getActiviteAllocataire())) {
                        return new CalculModeAgriculteurPlaineJura();
                    } else if (ALCSDossier.ACTIVITE_COLLAB_AGRICOLE.equals(context.getDossier().getDossierModel()
                            .getActiviteAllocataire())) {
                        // return new CalculModeCollaborateurAgricolePlaineJura();
                        return new CalculModeAgriculture();
                    } else {
                        throw new ALCalculException("CalculModeFactory#getCalculMode : unable to find apropriate mode");
                    }
                }
                // cas d'un agriculteur "standard"
            } else if ((context.getDossier() instanceof DossierAgricoleComplexModel)) {
                return new CalculModeAgriculture();
            } else if (ALCSTarif.CATEGORIE_JU.equals(context.getTarif())
                    && !ALImplServiceLocator.getCalculService().isDateLAFam(context.getDateCalcul())) {
                return new CalculModeJura();
            } else {

                // dans tous les autres cas, utilisation du mode standard
                return new CalculModeStandard();
            }
        }
    }

    /**
     * Recherche le mode de calcul à utiliser dans le cas de la FPV
     * 
     * @param context
     *            contexte contenant les données nécessaire à l'exécution du calcul
     * @return instance du mode de calcul à utiliser
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static CalculMode getCalculModeFPV(ContextCalcul context) throws JadeApplicationException,
            JadePersistenceException {

        // dossier CS
        if (ALCSDossier.STATUT_CS.equals(context.getDossier().getDossierModel().getStatut())) {
            CalculMode calcModeCS = CalculModeFactory.getModeCS(context);

            if (calcModeCS == null) {
                return new CalculModeIntercantonalFPV();
            } else {
                return calcModeCS;
            }
        } else {
            CalculMode calcMode = CalculModeFactory.getModeForce(context);

            if (calcMode == null) {
                return new CalculModeFPV();
            } else {
                return calcMode;
            }
        }
    }

    private static CalculMode getModeCS(ContextCalcul context) throws JadeApplicationException,
            JadePersistenceException {

        AttributEntiteSearchModel search = new AttributEntiteSearchModel();
        search.setForCleEntiteAlpha(context.getAssuranceInfo().getCodeCaisseProf());
        search.setForNomAttribut(ALConstAttributsEntite.ATTRIBUT_MODE_CALC_CS);
        search = ALServiceLocator.getAttributEntiteModelService().search(search);

        if (search.getSize() == 0) {
            return null;
        } else if (search.getSize() == 1) {
            return CalculModes.valueOf(((AttributEntiteModel) search.getSearchResults()[0]).getValeurAlpha())
                    .getCalculModeClass();
        } else {
            throw new ALCalculException(
                    "CalculModeFactory#affilieHasForceMode : Un seul mode forcé est autorisé pour une entité");
        }
    }

    /**
     * Vérifie si un mode forcé a été défini pour l'affilié en cours de traitement
     * 
     * @param context
     *            contexte contenant les données nécessaire à l'exécution du calcul
     * @return instance du mode si existant, <code>null</code> si aucun mode n'a été forcé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static CalculMode getModeForce(ContextCalcul context) throws JadeApplicationException,
            JadePersistenceException {

        AttributEntiteSearchModel search = new AttributEntiteSearchModel();
        search.setForCleEntiteAlpha(context.getDossier().getDossierModel().getNumeroAffilie());
        search.setForNomAttribut(ALConstAttributsEntite.ATTRIBUT_MODE_CALC_FORCE);
        search = ALServiceLocator.getAttributEntiteModelService().search(search);

        if (search.getSize() == 0) {
            return null;
        } else if (search.getSize() == 1) {
            return CalculModes.valueOf(((AttributEntiteModel) search.getSearchResults()[0]).getValeurAlpha())
                    .getCalculModeClass();
        } else {
            throw new ALCalculException(
                    "CalculModeFactory#affilieHasForceMode : Un seul mode forcé est autorisé pour une entité");
        }
    }
}
