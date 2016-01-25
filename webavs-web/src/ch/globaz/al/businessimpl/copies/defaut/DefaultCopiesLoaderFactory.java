package ch.globaz.al.businessimpl.copies.defaut;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.exceptions.copies.ALCopieBusinessException;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * Factory permettant de déterminer le loader à utiliser pour charger les copie par défaut
 * 
 * @author jts
 * 
 */
public class DefaultCopiesLoaderFactory {

    public static enum CopiesNonActif {
        AGENCE("ch.globaz.al.businessimpl.copies.defaut.DefaultCopiesLoaderNonActifAgence"),
        STANDARD("ch.globaz.al.businessimpl.copies.defaut.DefaultCopiesLoaderNonActifStandard");

        private String classe;

        private CopiesNonActif(String classe) {
            this.classe = classe;
        }

        public DefaultCopiesLoaderAbstract getClasse(ContextDefaultCopiesLoader context)
                throws JadeApplicationException {
            try {

                Class classeCopies = Class.forName(classe);
                Constructor constructeur = classeCopies
                        .getConstructor(new Class[] { ContextDefaultCopiesLoader.class });
                return (DefaultCopiesLoaderAbstract) constructeur.newInstance(new Object[] { context });
            } catch (ClassNotFoundException e) {
                throw new ALCopieBusinessException("CopieNonActif#getClasse : La classe '" + classe
                        + "' n'a pas été trouvée", e);
            } catch (InstantiationException e) {
                throw new ALCopieBusinessException("CopieNonActif#getClasse : La classe '" + classe
                        + "' n'a pas pu être instanciée", e);
            } catch (IllegalAccessException e) {
                throw new ALCopieBusinessException("CopieNonActif#getClasse : La classe '" + classe
                        + "' n'a pas pu être instanciée", e);
            } catch (SecurityException e) {
                throw new ALCopieBusinessException("CopieNonActif#getClasse : La classe '" + classe
                        + "' n'a pas pu être instanciée", e);
            } catch (NoSuchMethodException e) {
                throw new ALCopieBusinessException("CopieNonActif#getClasse : La classe '" + classe
                        + "' n'a pas pu être instanciée", e);
            } catch (IllegalArgumentException e) {
                throw new ALCopieBusinessException("CopieNonActif#getClasse : La classe '" + classe
                        + "' n'a pas pu être instanciée", e);
            } catch (InvocationTargetException e) {
                throw new ALCopieBusinessException("CopieNonActif#getClasse : La classe '" + classe
                        + "' n'a pas pu être instanciée", e);
            }
        }
    }

    /**
     * Retourne une instance de loader permettant de récupérer la liste des copies par défaut
     * 
     * @param context
     *            Contexte contenant les informations permettant de déterminer le loader à utiliser
     * @return instance de loader permettant de récupérer la liste des copies par défaut
     * @throws JadeApplicationException
     *             Exception levée si l'id tiers de l'affilié n'a pas pu être récupéré
     */
    public static DefaultCopiesLoaderAbstract getDefaultCopiesLoader(ContextDefaultCopiesLoader context)
            throws JadeApplicationException {

        if (ALCSDossier.ACTIVITE_NONACTIF.equals(context.getDossier().getDossierModel().getActiviteAllocataire())) {

            try {
                ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                        ALConstParametres.APPNAME, ALConstParametres.TYPE_COPIE_DECISION_NONACTIF,
                        JadeDateUtil.getGlobazFormattedDate(new Date()));

                return CopiesNonActif.valueOf(param.getValeurAlphaParametre().trim()).getClasse(context);

            } catch (JadeApplicationException e) {
                throw new ALCopieBusinessException(
                        "DefaultCopiesLoaderFactory#getDefaultCopiesLoader : Impossible de récupérer le type de copie à utiliser : "
                                + e.getMessage(), e);
            } catch (JadePersistenceException e) {
                throw new ALCopieBusinessException(
                        "DefaultCopiesLoaderFactory#getDefaultCopiesLoader : Impossible de récupérer le type de copie à utiliser : "
                                + e.getMessage(), e);
            }

        } else {

            // paiement indirect
            if (!ALServiceLocator.getDossierBusinessService().isModePaiementDirect(
                    context.getDossier().getDossierModel())) {

                if (ALCSDossier.ACTIVITE_AGRICULTEUR.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {
                    return new DefaultCopiesLoaderAgriculteurIndirect(context);

                } else if (ALCSDossier.ACTIVITE_COLLAB_AGRICOLE.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {
                    return new DefaultCopiesLoaderAgriculteurIndirect(context);

                } else if (ALCSDossier.ACTIVITE_INDEPENDANT.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {
                    return new DefaultCopiesLoaderIndependantIndirect(context);

                } else if (ALCSDossier.ACTIVITE_PECHEUR.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {
                    return new DefaultCopiesLoaderAgriculteurIndirect(context);

                } else if (ALCSDossier.ACTIVITE_SALARIE.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {
                    return new DefaultCopiesLoaderSalarieIndirect(context);

                } else if (ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {
                    return new DefaultCopiesLoaderTravailleurAgricoleIndirect(context);

                } else if (ALCSDossier.ACTIVITE_TSE.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {
                    return new DefaultCopiesLoaderTSEIndirect(context);

                } else if (ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {
                    return new DefaultCopiesLoaderAgriculteurIndirect(context);

                } else {
                    throw new ALCopieBusinessException(
                            "DefaultCopiesLoaderFactory#DefaultCopiesLoaderAbstract : This activity type ('"
                                    + context.getDossier().getDossierModel().getActiviteAllocataire()
                                    + "') is not supported by default copies loader");
                }
                // paiement direct
            } else {
                if (ALCSDossier.ACTIVITE_AGRICULTEUR.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {
                    return new DefaultCopiesLoaderAgriculteurDirect(context);

                } else if (ALCSDossier.ACTIVITE_COLLAB_AGRICOLE.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {
                    return new DefaultCopiesLoaderAgriculteurDirect(context);

                } else if (ALCSDossier.ACTIVITE_INDEPENDANT.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {
                    return new DefaultCopiesLoaderIndependantDirect(context);

                } else if (ALCSDossier.ACTIVITE_PECHEUR.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {
                    return new DefaultCopiesLoaderAgriculteurDirect(context);

                } else if (ALCSDossier.ACTIVITE_SALARIE.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {
                    return new DefaultCopiesLoaderSalarieDirect(context);

                } else if (ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {
                    return new DefaultCopiesLoaderTravailleurAgricoleDirect(context);

                } else if (ALCSDossier.ACTIVITE_TSE.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {
                    return new DefaultCopiesLoaderTSEDirect(context);

                } else if (ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE.equals(context.getDossier().getDossierModel()
                        .getActiviteAllocataire())) {
                    return new DefaultCopiesLoaderAgriculteurDirect(context);

                } else {
                    throw new ALCopieBusinessException(
                            "DefaultCopiesLoaderFactory#DefaultCopiesLoaderAbstract : This activity type ('"
                                    + context.getDossier().getDossierModel().getActiviteAllocataire()
                                    + "') is not supported by default copies loader");
                }
            }
        }
    }
}
