package ch.globaz.al.businessimpl.rafam.handlers;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Gère les annonces de type enfant (code 10)
 * 
 * @author jts
 * 
 */
public class AnnonceEnfantHandler extends AnnonceHandlerAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant toutes les données nécessaire à la gestion des annonces RAFam
     */
    public AnnonceEnfantHandler(ContextAnnonceRafam context) {
        this.context = context;
    }

    @Override
    protected void doCreation() throws JadeApplicationException, JadePersistenceException {

        if (isCurrentAllowanceTypeActive() && !AnnoncesChangeChecker.isDateFinDroitExpire(context.getDroit().getDroitModel().getFinDroitForcee())) {

            AnnonceRafamModel annonce = ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce68a(
                    context.getDossier(), context.getDroit(), getType(), context.getEtat());
            annonce = setEcheance(annonce);

            if (isRadiation()) {
                doRadiation(annonce);
            } else {
                enregistrerAnnonce(annonce);
            }
        }
    }

    @Override
    protected void doModificationStandard() throws JadeApplicationException, JadePersistenceException {
        if (isCurrentAllowanceTypeActive()) {
            AnnonceRafamModel annonce = ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce68b(
                    context.getDossier(), context.getDroit(), getType(), context.getEtat(),
                    context.getEvenementDeclencheur(), getLastAnnonce());

            annonce = setEcheance(annonce);

            if (isRadiation()) {
                doRadiation(annonce);
            } else if (AnnoncesChangeChecker.hasChanged(annonce, getLastAnnonce(), context.getDossier(),
                    context.getDroit())) {
                enregistrerAnnonce(annonce);
            }
        } else {
            doAnnulation();
        }
    }

    protected String getEcheance() throws JadeApplicationException {
        String echeanceCalculee = ALDateUtils.getDateAjoutAnneesFinMois(context.getDroit().getEnfantComplexModel()
                .getPersonneEtendueComplexModel().getPersonne().getDateNaissance(), 16);
        return echeanceCalculee;
    }

    @Override
    protected RafamFamilyAllowanceType getType() throws JadeApplicationException {
        return RafamFamilyAllowanceType.ENFANT;
    }

    @Override
    protected boolean isCurrentAllowanceTypeActive() throws JadeApplicationException {

        if (!context.getDroit().getEnfantComplexModel().getEnfantModel().getCapableExercer()
                && JadeDateUtil.isDateBefore(getEcheance(), context.getDroit().getDroitModel().getDebutDroit())) {
            return false;
        } else {

            return (ALCSDroit.TYPE_ENF.equals(context.getDroit().getDroitModel().getTypeDroit()) && !context.getDroit()
                    .getDroitModel().getSupplementFnb());
        }
    }

    /**
     * Modifie la date de fin de l'annonce aux 16 ans de l'enfant si la date contenue dans l'annonce est au-delà de cet
     * âge. S'il s'agit d'un enfant incapable d'exercer, la part de 16 à 20 ans sera traitée par la classe de gestion de
     * ce type de cas : {@link AnnonceEnfantIncapableExercerHandler}
     * 
     * @param annonce
     *            L'annonce à vérifier et modifier si nécessaire
     * 
     * @return L'annonce modifiée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected AnnonceRafamModel setEcheance(AnnonceRafamModel annonce) throws JadeApplicationException {

        String echeanceCalculee = getEcheance();

        if (JadeDateUtil.isDateBefore(echeanceCalculee, context.getDroit().getDroitModel().getFinDroitForcee())) {
            annonce.setEcheanceDroit(echeanceCalculee);
        }

        return annonce;
    }
}