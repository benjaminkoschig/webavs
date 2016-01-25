package ch.globaz.al.businessimpl.processus;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.exceptions.processus.ALProcessusCtrlException;
import ch.globaz.al.business.exceptions.processus.ALProcessusException;
import ch.globaz.al.business.models.processus.TraitementPeriodiqueModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * 
 * Classe générique des traitements métier
 * 
 * @author GMO
 * 
 */
public abstract class BusinessTraitement implements BusinessTraitementProtocoleInterface {

    /**
     * Le processus dont fait partie le traitement
     */
    private BusinessProcessus processusConteneur = null;

    /**
     * Représente le traitement au niveau DB
     */
    private TraitementPeriodiqueModel traitementPeriodiqueModel = null;

    /**
     * Annuler le traitement en contrôlant si c'est autorisé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public final void annuler() throws JadeApplicationException, JadePersistenceException {
        if (isReadyToExecuteBack()) {
            executeBack();
            openTraitement();
        } else {
            throw new ALProcessusException("TraitementPeriodique#annuler : traitement ne peut être annulé");
        }
    }

    /**
     * Ferme le traitement et éventuellement le processus lié si il le faut
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private final void closeTraitement() throws JadeApplicationException, JadePersistenceException {
        // on met le traitement en terminé
        traitementPeriodiqueModel.setEtat(ALCSProcessus.ETAT_TERMINE);
        setTraitementPeriodiqueModel(ALServiceLocator.getTraitementPeriodiqueModelService().update(
                traitementPeriodiqueModel));

        if (isLastTraitement()) {
            processusConteneur.getProcessusPeriodiqueModel().setEtat(ALCSProcessus.ETAT_TERMINE);
        }
        if (requiredNextPeriodAfterExecute()) {
            // on ouvre le processus métier pour la période suivante si c'est un
            // processus principal
            // if (!this.processusConteneur.getProcessusPeriodiqueModel().getIsPartiel()) {
            String periodeToInit = ALServiceLocator.getPeriodeAFBusinessService()
                    .getNextPeriode(getProcessusConteneur().getDataCriterias().periodeCriteria).getDatePeriode();
            // TODO: vérifier que le processus ne s'ouvre pas 2x, on ouvre qu'un principal
            // on peut virer le ctrl du partiel si pas de processus ouverts 2x
            ALServiceLocator.getBusinessProcessusService().initBusinessProcessusForPeriode(
                    ALServiceLocator.getBusinessProcessusService().getAppliedTemplate(periodeToInit),
                    processusConteneur.getCSProcessus(), periodeToInit,
                    processusConteneur.getProcessusPeriodiqueModel().getIsPartiel());
            // }

        }

        // Mise à jour effectivement en DB le processus périodique
        getProcessusConteneur().setProcessusPeriodiqueModel(
                ALServiceLocator.getProcessusPeriodiqueModelService().update(
                        getProcessusConteneur().getProcessusPeriodiqueModel()));
    }

    /**
     * Effectue le traitement effectif spécifique au traitement. En cas d'erreur d'exécution métier ou technique se
     * passant dans le
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected abstract void execute() throws JadePersistenceException, JadeApplicationException;

    /**
     * Effectue le traitement effectif inverse spécifique au traitement
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected abstract void executeBack() throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne le CS du traitement métier
     * 
     * @return CS du traitement
     */
    public abstract String getCSTraitement();

    /**
     * Méthode qui retourne le prochain traitement du même processus qui effectue des modidications DB selon le
     * traitement dans lequel on se trouve
     * 
     * @return BusinessTraitement ou null si aucun traitement trouvé
     * */
    private final BusinessTraitement getNextNonReadOnlyTraitement() {
        // TODO:gérer le cas ou le traitement readonly qu'on essaie de lancer est le dernier du processus
        int position = processusConteneur.getListeTraitements().indexOf(this);
        int idxNext = 0;
        for (int i = position; i < processusConteneur.getListeTraitements().size(); i++) {
            BusinessTraitement currentTraitement = processusConteneur.getListeTraitements().get(i);
            if (!currentTraitement.isReadOnly()) {
                idxNext = i;
                break;
            }
        }
        return processusConteneur.getListeTraitements().get(idxNext);
    }

    /**
     * Récupère le processus contenant le traitement
     * 
     * @return processusConteneur
     */
    public BusinessProcessus getProcessusConteneur() {
        return processusConteneur;
    }

    /**
     * Retourne une liste de protocole au format CSV
     */
    @Override
    public ArrayList<String> getProtocolesCSV() {
        return new ArrayList<String>();
    }

    /**
     * Retourne le traitement périodique lié
     * 
     * @return traitementPeriodiqueModel le traitement périodique
     */
    public TraitementPeriodiqueModel getTraitementPeriodiqueModel() {
        return traitementPeriodiqueModel;
    }

    /**
     * Contrôle si le traitement est le dernier de la liste des traitements du processus
     * 
     * @return vrai/faux
     */
    private final boolean isFirstTraitement() {
        if (processusConteneur.getListeTraitements().indexOf(this) == 0) {
            return true;

        } else {
            return false;
        }
    }

    /**
     * Contrôle si le traitement est le premier de la liste des traitements du processus
     * 
     * @return vrai/faux
     */
    private final boolean isLastTraitement() {
        if (processusConteneur.getListeTraitements().indexOf(this) == processusConteneur.getListeTraitements().size() - 1) {
            return true;

        } else {
            return false;
        }
    }

    /**
     * Indique si le traitement effectue des modifications en DB ou non
     * 
     * @return
     */
    public abstract boolean isReadOnly();

    /**
     * Méthode qui indique si le traitement pour être exécuté ou non
     * 
     * @return exécutable ou non
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée lorsqu'un service n'est pas accessible
     */
    private final boolean isReadyToExecute() throws JadeApplicationServiceNotAvailableException,
            JadeApplicationException, JadePersistenceException {

        BusinessTraitement nextNonReadOnly = getNextNonReadOnlyTraitement();

        // -----------------------------------------------------------------------
        // Règle #1 : si le traitement est déjà en cours, on ne peut pas l'exécuter
        if (ALCSProcessus.ETAT_ENCOURS.equals(getTraitementPeriodiqueModel().getEtat())) {
            return false;
        }
        // -----------------------------------------------------------------------
        // Règle #2 : si c'est un readonly et que le prochain non read only n'est pas déjà exécuté, le traitement est
        // exécutable
        if (isReadOnly() && ALCSProcessus.ETAT_OUVERT.equals(nextNonReadOnly.getTraitementPeriodiqueModel().getEtat())) {
            return true;
        }
        // -----------------------------------------------------------------------
        // Règle #3 : si le traitement est déjà terminé, on ne peut pas l'exécuter
        if (ALCSProcessus.ETAT_TERMINE.equals(getTraitementPeriodiqueModel().getEtat())) {
            return false;
        }
        // -----------------------------------------------------------------------
        // Règle #4 : si c'est le premier traitement on peut l'exécuter

        if (isFirstTraitement()) {
            return true;
        }
        // -----------------------------------------------------------------------
        // Règle #5: il faut que le prédecesseur soit fermé pour exécuter le traitement
        // si son prédécesseur est ok, on retourne true, sinon false
        if ((!isFirstTraitement())
                && ALCSProcessus.ETAT_TERMINE.equals((getProcessusConteneur().getListeTraitements()
                        .get(processusConteneur.getListeTraitements().indexOf(this) - 1))
                        .getTraitementPeriodiqueModel().getEtat())) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Méthode qui indique si le traitement inverse pour être exécuté ou non
     * 
     * @return exécutable ou non
     */
    private final boolean isReadyToExecuteBack() {
        // si c'est le dernier traitement
        if (isLastTraitement()) {
            return true;
        }
        // si son successeur est ok, on retourne true, sinon false
        if (ALCSProcessus.ETAT_OUVERT.equals((getProcessusConteneur().getListeTraitements().get(processusConteneur
                .getListeTraitements().indexOf(this) + 1)).getTraitementPeriodiqueModel().getEtat())) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Ouvre le traitement et eventuellement le processus lié si il le faut
     */
    private final void openTraitement() {
        getTraitementPeriodiqueModel().setEtat(ALCSProcessus.ETAT_OUVERT);
        if (isLastTraitement()) {
            processusConteneur.getProcessusPeriodiqueModel().setEtat(ALCSProcessus.ETAT_OUVERT);
        }
    }

    /**
     * Indique si après l'éxecution du traitement, il faut ouvrir le processus pour la prochaine période ou non
     * 
     * @return true/false
     */
    private final boolean requiredNextPeriodAfterExecute() {
        // les traitements de préparation
        if (ALCSProcessus.NAME_TRAITEMENT_PREPARATION_COMPENSATION.equals(getCSTraitement())
                || ALCSProcessus.NAME_TRAITEMENT_PREPARATION_VERSEMENT_DIRECTS.equals(getCSTraitement())) {
            return true;
        }
        // si on est pas dans un de ces 2 traitements, on regarde si dans le même processus, il y avait ces 2
        // si oui, alors si on est dans le dernier, il ne sera plus opener de la prochaine période
        boolean alreadyOpenerInProcessus = false;
        for (BusinessTraitement traitement : processusConteneur.getListeTraitements()) {
            if (ALCSProcessus.NAME_TRAITEMENT_PREPARATION_COMPENSATION.equals(traitement.getCSTraitement())
                    || ALCSProcessus.NAME_TRAITEMENT_PREPARATION_VERSEMENT_DIRECTS.equals(traitement.getCSTraitement())) {
                alreadyOpenerInProcessus = true;
            }
        }
        // si il y avait pas d'opener dans le processus et que c'est le dernier traitement, c'est lui l'opener
        if (isLastTraitement() && !alreadyOpenerInProcessus) {
            return true;
        }
        return false;
    }

    /**
     * Définit le processus qui va contenir le traitement
     * 
     * @param processusConteneur
     *            le processus contenant le traitement
     */
    public void setProcessusConteneur(BusinessProcessus processusConteneur) {
        this.processusConteneur = processusConteneur;
    }

    /**
     * @param traitementPeriodiqueModel
     *            le traitement périodique auquel appliquer la logique métier
     */
    public void setTraitementPeriodiqueModel(TraitementPeriodiqueModel traitementPeriodiqueModel) {
        this.traitementPeriodiqueModel = traitementPeriodiqueModel;
    }

    /**
     * Démarre le traitement en contrôlant si c'est autorisé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public final void start() throws JadeApplicationException, JadePersistenceException {

        if (isReadyToExecute()) {

            try {
                traitementPeriodiqueModel.setDateExecution(JadeDateUtil.getGlobazFormattedDate(new Date()));

                String nowHeure = JadeDateUtil.getHMTime(new Date());
                traitementPeriodiqueModel.setHeureExecution(nowHeure.substring(0, 2) + ":" + nowHeure.substring(2));
                traitementPeriodiqueModel.setUserExecution(JadeThread.currentUserName());
                traitementPeriodiqueModel.setEtat(ALCSProcessus.ETAT_ENCOURS);

                setTraitementPeriodiqueModel(ALServiceLocator.getTraitementPeriodiqueModelService().update(
                        traitementPeriodiqueModel));
                // on commit ici pour car le traitement est définitivement lancé
                JadeThread.commitSession();

                execute();
            }
            // n'importe quelle exception dans l'execute met le traitement en
            // erreur
            catch (JadeApplicationException e) {
                // on la remonte pour indiquer dans le mail job ce qui s'est
                // passé
                throw e;

            } catch (JadePersistenceException e) {
                // on la remonte pour indiquer dans le mail job ce qui s'est
                // passé
                throw e;

            } catch (Exception e) {
                throw new ALProcessusException("BusinessTraitement#start: Unable to start traitement ("
                        + e.getMessage() + ")", e);
            }
            // si il y a des erreurs métiers , on le met aussi en erreur
            if ((getProtocoleLogger() != null) && getProtocoleLogger().hasError()) {

                traitementPeriodiqueModel.setEtat(ALCSProcessus.ETAT_ERREUR);
                setTraitementPeriodiqueModel(ALServiceLocator.getTraitementPeriodiqueModelService().update(
                        traitementPeriodiqueModel));
            }
        } else {
            throw new ALProcessusCtrlException("Le traitement n'est pas prêt");
        }

        closeTraitement();

    }
}
