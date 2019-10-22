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

/**
 * G�re les annonces de type Enfant incapable d'exercer (code 12).
 * 
 * @author jts
 * 
 */
public class AnnonceEnfantIncapableExercerHandler extends AnnonceHandlerAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant toutes les donn�es n�cessaire � la gestion des annonces RAFam
     */
    public AnnonceEnfantIncapableExercerHandler(ContextAnnonceRafam context) {
        this.context = context;
    }

    @Override
    protected void doCreation() throws JadeApplicationException, JadePersistenceException {

        // si incapable d'exercer et �ch�ance du droit au-del� des 16 ans
        if (isIncapableExercer() && isCurrentAllowanceTypeActive() && !AnnoncesChangeChecker.isDateFinDroitExpire(context.getDroit().getDroitModel().getFinDroitForcee())) {

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

        if (!isIncapableExercer()) {
            if (!getLastAnnonce().isNew()) {
                super.doAnnulation();
            }
        } else {
            if (getLastAnnonce().isNew()) {
                doCreation();
            } else if (isCurrentAllowanceTypeActive()) {
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
    }

    /**
     * R�cup�re la date d'�ch�ance des 16 ans
     * 
     * @return Date d'�ch�ance
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private String getEcheanceCalculee() throws JadeApplicationException {
        String echeanceCalculee = ALDateUtils.getDateAjoutAnneesFinMois(context.getDroit().getEnfantComplexModel()
                .getPersonneEtendueComplexModel().getPersonne().getDateNaissance(), 16);
        return echeanceCalculee;
    }

    protected RafamFamilyAllowanceType getOtherType() throws JadeApplicationException {
        return RafamFamilyAllowanceType.ENFANT_EN_INCAPACITE_AVEC_SUPPLEMENT;
    }

    @Override
    protected RafamFamilyAllowanceType getType() throws JadeApplicationException {
        return RafamFamilyAllowanceType.ENFANT_EN_INCAPACITE;
    }

    @Override
    protected boolean isCurrentAllowanceTypeActive() {
        return (ALCSDroit.TYPE_ENF.equals(context.getDroit().getDroitModel().getTypeDroit())
                && !context.getDroit().getEnfantComplexModel().getEnfantModel().getCapableExercer() && !context
                .getDroit().getDroitModel().getSupplementFnb());
    }

    /**
     * V�rifie si l'enfant est incapable d'exercer
     * 
     * @return <code>true</code> si l'enfant est incapable d'exercer
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private boolean isIncapableExercer() throws JadeApplicationException {

        return !context.getDroit().getEnfantComplexModel().getEnfantModel().getCapableExercer()
                && JadeDateUtil.isDateBefore(getEcheanceCalculee(), context.getDroit().getDroitModel()
                        .getFinDroitForcee());
    }

    /**
     * Modifie la date de d�but de l'annonce au jour suivant les 16 ans de l'enfant si la date contenue dans l'annonce
     * est ant�rieure � cet �ge.
     * 
     * @param annonce
     *            L'annonce � v�rifier et modifier si n�cessaire
     * 
     * @return L'annonce modifi�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected AnnonceRafamModel setEcheance(AnnonceRafamModel annonce) throws JadeApplicationException {
        String echeanceCalculee = getEcheanceCalculee();

        // si la date de d�but est ant�rieure ou �gale au 16 ans => l'annonce incapable d'exercer d�bute le
        // lendemain des 16 ans. Sinon on laisse la date du droit
        if (JadeDateUtil.isDateBefore(context.getDroit().getDroitModel().getDebutDroit(), echeanceCalculee)
                || echeanceCalculee.equals(context.getDroit().getDroitModel().getDebutDroit())) {
            annonce.setDebutDroit(JadeDateUtil.addDays(echeanceCalculee, 1));
        }

        return annonce;
    }
}
