package globaz.phenix.external;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.prof.JadeProfiler;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPCotisationManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;

/**
 * <code>ExtMiseAjourCotisation</code> est un service externe de la classe AFCotisation
 * <p>
 * Cette classe fournit les services suivants:
 * <ul>
 * <li>Met à jour l'id cotisation de AFCotisdation dans CPCotisation en cas de changment d'affiliation MEt à jour les
 * montants pour la facturation périodique
 * </ul>
 * 
 * @author Emmanuel Fleury
 */
public class ExtMiseAjourCotisation extends BAbstractEntityExternalService {

    /**
     * Constructeur du type CSCCreationCotiService.
     */
    public ExtMiseAjourCotisation() {
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
        AFCotisation cotisation = (AFCotisation) entity;
        if (CodeSystem.GENRE_ASS_PERSONNEL.equals(cotisation.getAssurance().getAssuranceGenre())) {
            if (!cotisation.getSession().hasErrors()) {
                boolean miseAjourEffectuee = false;
                String idAffiliation = "";
                // Récupération de l'id affiliation dans l'adhésion
                // Pour les cas migré il se peut que l'adhesion ne soit pas
                // renseigné => prendre idAffiliation de la cotisation
                AFPlanAffiliation plan = new AFPlanAffiliation();
                plan.setSession(cotisation.getSession());
                plan.setPlanAffiliationId(cotisation.getPlanAffiliationId());
                plan.retrieve();
                if (!plan.isNew()) {
                    idAffiliation = plan.getAffiliationId();
                } else {
                    idAffiliation = cotisation.getAffiliationId();
                }
                if (!JadeStringUtil.isIntegerEmpty(idAffiliation)) {
                    // Mise à jour de l'd cotisation => lecture des décisions
                    // concernées
                    CPDecisionManager manager = new CPDecisionManager();
                    manager.setSession(cotisation.getSession());
                    manager.setFromDateDebutDecision(cotisation.getDateDebut());
                    manager.setForIdAffiliation(idAffiliation);
                    manager.setForExceptTypeDecision(CPDecision.CS_REMISE);
                    manager.orderByAnneeDecision();
                    manager.orderByDateDecision();
                    manager.orderByIdDecision();
                    manager.find(cotisation.getSession().getCurrentThreadTransaction());
                    for (int i = 0; i < manager.size(); i++) {
                        CPDecision myDecision = (CPDecision) manager.getEntity(i);
                        // Parcourir les cotisations cot.pers.
                        CPCotisationManager cotiManager = new CPCotisationManager();
                        cotiManager.setSession(cotisation.getSession());
                        cotiManager.setForIdDecision(myDecision.getIdDecision());
                        cotiManager.find(cotisation.getSession().getCurrentThreadTransaction());
                        CPCotisation coti = null;
                        // Mettre à jour CPCOTIP.meicot avec le nouveau
                        // AFCOTIP.meicot
                        for (int j = 0; j < cotiManager.size(); j++) {
                            coti = ((CPCotisation) cotiManager.getEntity(j));
                            // Tester si c'est la même assurance
                            if (cotisation.getAssurance().getTypeAssurance().equals(coti._getGenreCotisation())) {
                                // Mettre à jour CPCOTIP.meicot avec le nouveau
                                // AFCOTIP.meicot
                                coti.setIdCotiAffiliation(cotisation.getCotisationId());
                                coti.wantCallValidate(false);
                                coti.update(cotisation.getSession().getCurrentThreadTransaction());
                                // Mise à jour des montants périodiques de
                                // l'assurance par rapport à la décision la plus
                                // récente (la première active lue)
                                if (Boolean.TRUE.equals(myDecision.getActive()) && !miseAjourEffectuee) {
                                    // Remise à zéro des montants si salarié dispensé
                                    if (CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(myDecision.getSpecification())
                                            || CPDecision.CS_FRANCHISE.equalsIgnoreCase(myDecision.getSpecification())
                                            || AFParticulariteAffiliation.existeParticularite(cotisation.getSession(),
                                                    myDecision.getIdAffiliation(),
                                                    CodeSystem.PARTIC_AFFILIE_COT_PERS_AUTRE_AGENCE,
                                                    myDecision.getDebutDecision())) {
                                        cotisation.setMontantAnnuel("0");
                                        cotisation.setMontantSemestriel("0");
                                        cotisation.setMontantTrimestriel("0");
                                        cotisation.setMontantMensuel("0");
                                    } else {
                                        cotisation.setMontantAnnuel(coti.getMontantAnnuel());
                                        cotisation.setMontantSemestriel(coti.getMontantSemestriel());
                                        cotisation.setMontantTrimestriel(coti.getMontantTrimestriel());
                                        cotisation.setMontantMensuel(coti.getMontantMensuel());
                                    }
                                    cotisation.setAnneeDecision(myDecision.getAnneeDecision());
                                    cotisation.wantCallExternalServices(false);
                                    cotisation.update(cotisation.getSession().getCurrentThreadTransaction());
                                    cotisation.wantCallExternalServices(true);
                                    miseAjourEffectuee = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (cotisation.getSession().hasErrors()) {
            throw new Exception(cotisation.getSession().getErrors().toString());
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
     * Exécute un service externe avant mise à jour d'une entité
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
     * Exécute un service externe avant suppression d'une entité
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
        AFCotisation cotisation = (AFCotisation) entity;
        if (CodeSystem.GENRE_ASS_PERSONNEL.equals(cotisation.getAssurance().getAssuranceGenre())) {
            if (!cotisation.getSession().hasErrors()) {
                boolean miseAjourEffectuee = false;
                String idAffiliation = "";
                // Récupération de l'id affiliation dans l'adhésion
                // Pour les cas migré il se peut que l'adhesion ne soit pas
                // renseigné => prendre idAffiliation de la cotisation
                AFPlanAffiliation plan = new AFPlanAffiliation();
                plan.setSession(cotisation.getSession());
                plan.setPlanAffiliationId(cotisation.getPlanAffiliationId());
                plan.retrieve();
                if (!plan.isNew()) {
                    idAffiliation = plan.getAffiliationId();
                } else {
                    idAffiliation = cotisation.getAffiliationId();
                }
                if (!JadeStringUtil.isIntegerEmpty(idAffiliation)) {
                    // Mise à jour de l'd cotisation => lecture des décisions
                    // concernées
                    CPDecisionManager manager = new CPDecisionManager();
                    manager.setSession(cotisation.getSession());
                    manager.setFromDateDebutDecision(cotisation.getDateDebut());
                    manager.setForIdAffiliation(idAffiliation);
                    manager.setForExceptTypeDecision(CPDecision.CS_REMISE);
                    manager.orderByAnneeDecision();
                    manager.orderByDateDecision();
                    manager.orderByIdDecision();
                    manager.find(cotisation.getSession().getCurrentThreadTransaction());
                    for (int i = 0; i < manager.size(); i++) {
                        CPDecision myDecision = (CPDecision) manager.getEntity(i);
                        // Parcourir les cotisations cot.pers.
                        CPCotisationManager cotiManager = new CPCotisationManager();
                        cotiManager.setSession(cotisation.getSession());
                        cotiManager.setForIdDecision(myDecision.getIdDecision());
                        cotiManager.find(cotisation.getSession().getCurrentThreadTransaction());
                        CPCotisation coti = null;
                        // Mettre à jour CPCOTIP.meicot avec le nouveau
                        // AFCOTIP.meicot
                        for (int j = 0; j < cotiManager.size(); j++) {
                            coti = ((CPCotisation) cotiManager.getEntity(j));
                            // Tester si c'est la même assurance
                            if (cotisation.getAssurance().getTypeAssurance().equals(coti._getGenreCotisation())) {
                                // Mettre à jour CPCOTIP.meicot avec le nouveau
                                // AFCOTIP.meicot
                                coti.setIdCotiAffiliation(cotisation.getCotisationId());
                                coti.wantCallValidate(false);
                                coti.update(cotisation.getSession().getCurrentThreadTransaction());
                                // Mise à jour des montants périodiques de
                                // l'assurance par rapport à la décision la plus
                                // récente (la première active lue)
                                if (Boolean.TRUE.equals(myDecision.getActive()) && !miseAjourEffectuee) {
                                    // Remise à zéro des montants si salarié dispensé
                                    if (CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(myDecision.getSpecification())
                                            || CPDecision.CS_FRANCHISE.equalsIgnoreCase(myDecision.getSpecification())
                                            || AFParticulariteAffiliation.existeParticularite(cotisation.getSession(),
                                                    myDecision.getIdAffiliation(),
                                                    CodeSystem.PARTIC_AFFILIE_COT_PERS_AUTRE_AGENCE,
                                                    myDecision.getDebutDecision())) {
                                        cotisation.setMontantAnnuel("0");
                                        cotisation.setMontantSemestriel("0");
                                        cotisation.setMontantTrimestriel("0");
                                        cotisation.setMontantMensuel("0");
                                    } else {
                                        cotisation.setMontantAnnuel(coti.getMontantAnnuel());
                                        cotisation.setMontantSemestriel(coti.getMontantSemestriel());
                                        cotisation.setMontantTrimestriel(coti.getMontantTrimestriel());
                                        cotisation.setMontantMensuel(coti.getMontantMensuel());
                                    }
                                    cotisation.setAnneeDecision(myDecision.getAnneeDecision());
                                    cotisation.wantCallExternalServices(false);
                                    cotisation.update(cotisation.getSession().getCurrentThreadTransaction());
                                    cotisation.wantCallExternalServices(true);
                                    miseAjourEffectuee = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (cotisation.getSession().hasErrors()) {
            throw new Exception(cotisation.getSession().getErrors().toString());
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