package globaz.phenix.external;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.prof.JadeProfiler;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.toolbox.CPToolBox;

/**
 * @author Emmanuel Fleury
 */
public class ExtSortieService extends BAbstractEntityExternalService {

    /**
     * Constructeur du type CSCCreationCotiService.
     */
    public ExtSortieService() {
        super();
    }

    /**
     * Execute un deuxième afterAdd
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        JadeLogger.trace(this, "beforeAdd(" + entity + ")");
        JadeProfiler.begin(this, "beforeAdd()");
        AFAffiliation affiliation = (AFAffiliation) entity;
        if (!affiliation.getSession().hasErrors()) {
            CPDecision decision = new CPDecision();
            if (CodeSystem.TYPE_AFFILI_INDEP.equalsIgnoreCase(affiliation.getTypeAffiliation())
                    || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equalsIgnoreCase(affiliation.getTypeAffiliation())
                    || CodeSystem.TYPE_AFFILI_TSE.equalsIgnoreCase(affiliation.getTypeAffiliation())
                    || CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE.equalsIgnoreCase(affiliation.getTypeAffiliation())
                    || CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(affiliation.getTypeAffiliation())) {
                decision._basculerDansAffiliation(affiliation, affiliation.getSession().getCurrentThreadTransaction());
            }
        }
        if (affiliation.getSession().hasErrors()) {
            throw new Exception(affiliation.getSession().getErrors().toString());
        }
    }

    /**
     * Exécute un service externe après suppression d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe après chargement d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe après mise à jour d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        AFAffiliation affiliation = (AFAffiliation) entity;
        if (!affiliation.getSession().hasErrors()) {
            CPDecision decision = new CPDecision();
            if (CodeSystem.TYPE_AFFILI_INDEP.equalsIgnoreCase(affiliation.getTypeAffiliation())
                    || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equalsIgnoreCase(affiliation.getTypeAffiliation())
                    || CodeSystem.TYPE_AFFILI_TSE.equalsIgnoreCase(affiliation.getTypeAffiliation())
                    || CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE.equalsIgnoreCase(affiliation.getTypeAffiliation())
                    || CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(affiliation.getTypeAffiliation())) {
                decision._basculerDansAffiliation(affiliation, affiliation.getSession().getCurrentThreadTransaction());
            }
        }
        if (affiliation.getSession().hasErrors()) {
            throw new Exception(affiliation.getSession().getErrors().toString());
        }
    }

    /**
     * Exécute un service externe avant ajout d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe avant suppression d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe avant chargement d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe avant mise à jour d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
        JadeLogger.trace(this, "beforeUpdate(" + entity + ")");
        JadeProfiler.begin(this, "beforeUpdate()");
        AFAffiliation affiliation = (AFAffiliation) entity;
        if (!affiliation.getSession().hasErrors()) {
            // Ne pas faire de sortie si date de fin=0
            // ou si le motif de fin = Exclusion ou Changement de nom
            // ou si la date de fin n'a pas changé
            if (!JadeStringUtil.isEmpty(affiliation.getDateFin())
                    || !JadeStringUtil.isEmpty(affiliation.getDateFinSave())
                    || !JadeStringUtil.isEmpty(affiliation.getDateDebut())
                    || !JadeStringUtil.isEmpty(affiliation.getDateDebutSave())) {
                if (!BSessionUtil.compareDateEqual(affiliation.getSession(), affiliation.getDateFin(),
                        affiliation.getDateFinSave())
                        && !affiliation.getMotifFin().equalsIgnoreCase("803005")) // Changement de nom
                {
                    CPDecision decision = new CPDecision();
                    decision.setSession(entity.getSession());
                    decision._sortieFin(affiliation, affiliation.getSession().getCurrentThreadTransaction());
                }
                if (!BSessionUtil.compareDateEqual(affiliation.getSession(), affiliation.getDateDebut(),
                        affiliation.getDateDebutSave())) // Changement de nom
                {
                    CPDecision decision = new CPDecision();
                    decision.setSession(entity.getSession());
                    decision._sortieDeb(affiliation, affiliation.getSession().getCurrentThreadTransaction());
                }
                String anneeLimite = CPToolBox.anneeLimite(affiliation.getSession().getCurrentThreadTransaction());
                if (!affiliation.getSession().getCurrentThreadTransaction().hasErrors()) {
                    CPToolBox.miseAjourDecisionActive(affiliation.getSession().getCurrentThreadTransaction(),
                            affiliation, "", Integer.parseInt(anneeLimite));
                    CPToolBox.miseAjourImputation(affiliation.getSession().getCurrentThreadTransaction(), affiliation,
                            "", Integer.parseInt(anneeLimite));
                    CPToolBox.miseAjourRemise(affiliation.getSession().getCurrentThreadTransaction(), affiliation, "",
                            Integer.parseInt(anneeLimite));
                }
            }
            if (affiliation.getSession().hasErrors()) {
                throw new Exception(affiliation.getSession().getErrors().toString());
            }
        }
    }

    /**
     * Exécute un service externe pour initialiser une entité
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void init(BEntity entity) throws Throwable {
    }

    /**
     * Exécute un service externe pour valider le contenu d'une entité
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void validate(BEntity entity) throws Throwable {
    }
}