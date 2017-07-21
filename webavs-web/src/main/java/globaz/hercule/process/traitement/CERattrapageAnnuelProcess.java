/*
 * Globaz SA.
 */
package globaz.hercule.process.traitement;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.db.ICEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEAffilieControle;
import globaz.hercule.db.controleEmployeur.CEAffilieControleManager;
import globaz.hercule.db.groupement.CEMembre;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Map;

/**
 * Processus de rattrapage annuel des périodes de couverture. <BR>
 * Pour chaque affilié <BR>
 * - on recalcule la note de collaboration suivant le contentieux et la déclaration de salaire<BR>
 * - On recalcule la note globale (Le nombre de points) si les notes ont changées<BR>
 * - On récupère la masse salariale => On recalcule la période de couverture - On met a jour les informations au niveau
 * du groupe si besoin
 * 
 * @author SCO
 * @since SCO 7 juin 2010
 */
public class CERattrapageAnnuelProcess extends BProcess {

    private static final long serialVersionUID = -223287976885485246L;

    private static final int NB_ANNEE_RECHERCHE_MASSE_SALARIALE = 3;

    private String annee;
    private String collaborationC;
    private String collaborationM;
    private String collaborationTM;

    public CERattrapageAnnuelProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
        // Not implemented
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        String numAffilieTraite = "";
        String anneePrecedente;
        boolean status = true;
        StringBuilder messageTransactionError = new StringBuilder();

        try {

            collaborationC = Integer.toString((new FWCurrency(getSession()
                    .getCode(ICEControleEmployeur.COLLABORATION_C))).intValue());
            collaborationM = Integer.toString((new FWCurrency(getSession()
                    .getCode(ICEControleEmployeur.COLLABORATION_M))).intValue());
            collaborationTM = Integer.toString((new FWCurrency(getSession().getCode(
                    ICEControleEmployeur.COLLABORATION_TM))).intValue());

            anneePrecedente = CEUtils.getAnneePrecedente(getAnnee());

            // ****************************************
            // On fait la liste les employeurs actifs (affilié paritaire) pour
            // l'année précédente
            // ****************************************
            CEAffilieControleManager manager = new CEAffilieControleManager();
            manager.setSession(getSession());
            manager.setForRattrapageDate(getAnnee());

            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            if (manager.isEmpty()) {
                return false;
            }

            // Progress bar
            setProgressScaleValue(manager.size());

            // Pour chaque affilié, on recalcul ses points (si besoin) et on
            // calcul l'année de couverture
            for (int i = 0; i < manager.size(); i++) {
                CEAffilieControle affControle = (CEAffilieControle) manager.getEntity(i);

                // Information pour le process
                setProgressDescription("Num Affilie : " + affControle.getNumeroAffilie() + " | " + i + " / "
                        + manager.size());

                // Utile si erreur dans le procee
                numAffilieTraite = affControle.getNumeroAffilie();

                // ****************************************
                // Recalcul de la note de collaboration
                // ****************************************
                Integer nbrPoints = null;

                // Si pas d'attribution de point , on ne fait pas recalcul de
                // note
                if (!JadeStringUtil.isBlankOrZero(affControle.getIdAttributionPts())) {
                    String noteCollaboration = CEControleEmployeurService.reCalculNotesCollaboration(getSession(),
                            getTransaction(), affControle, getAnnee(), anneePrecedente, getCollaborationM(),
                            getCollaborationC(), getCollaborationTM());

                    // ****************************************
                    // Recalcul du nombre de point
                    // ****************************************
                    nbrPoints = CEControleEmployeurService.calculNombrePoints(getSession(),
                            affControle.getDerniereRevision(), affControle.getQualiteRH(), noteCollaboration,
                            affControle.getCriteresEntreprise());

                    // On met a jour la note et le nombre de point (si
                    // changement)
                    CEControleEmployeurService.updateAttributionPoints(getSession(), getTransaction(), affControle,
                            noteCollaboration, nbrPoints);
                }

                // ****************************************
                // Récupération de la catégorie de la masse salariale
                // ****************************************
                String anneeFinControle = null;
                if (!JadeStringUtil.isEmpty(affControle.getDateFinControle())) {
                    anneeFinControle = Integer.toString(CEUtils.stringDateToAnnee(affControle.getDateFinControle()));
                }

                double masseSalariale = CEControleEmployeurService.findMasseSalariale(getSession(),
                        affControle.getNumeroAffilie(), anneePrecedente, anneeFinControle,
                        NB_ANNEE_RECHERCHE_MASSE_SALARIALE);

                String categorie = CEControleEmployeurService.findCategorie(masseSalariale);

                // 3. Pour chaque affilié de la requete en 1., on regarde sa
                // masse salariale pour voir si il n'a pas changé de catégorie

                // ****************************************
                // Récupération des parametres pour le calcul de la période
                // ****************************************
                Map<String, String> params = CEControleEmployeurService.retrieveParamTableauPeriode(getSession(),
                        getAnnee());

                // ****************************************
                // Recalcul de la période de couverture
                // ****************************************
                String anneeCouverture = CEControleEmployeurService.calculCouverture(getSession(), getTransaction(),
                        affControle, nbrPoints, categorie, masseSalariale, getAnnee(), params,
                        CEUtils.transformeStringToInt(affControle.getParticulariteDerogation()));

                // mise a jour de la couverture si changé
                if (JadeStringUtil.isEmpty(affControle.getAnneCouverture())
                        || !anneeCouverture.equals(affControle.getAnneCouverture())) {
                    CEControleEmployeurService.updateCouverture(getSession(), getTransaction(),
                            affControle.getIdCouverture(), anneeCouverture, affControle.getIdAffilie(),
                            affControle.getNumeroAffilie());
                }

                // ****************************************
                // MAJ de l'annee de couverture du groupe si l'affilié est dans
                // un groupe
                // Et si la catégorie est supérieur a 0 ou 1
                // ****************************************
                Map<String, CEMembre> membreGroupe = CEControleEmployeurService.loadMapMembreGroupe(getSession());
                if (membreGroupe.containsKey(affControle.getIdAffilie())
                        && !ICEControleEmployeur.CATEGORIE_MASSE_0.equals(categorie)
                        && !ICEControleEmployeur.CATEGORIE_MASSE_1.equals(categorie)) {
                    CEControleEmployeurService.updateDateCouvertureGroupe(getSession(), getTransaction(),
                            membreGroupe.get(affControle.getIdAffilie()), anneeCouverture);
                }

                if (isAborted()) {
                    return false;
                }

                // Si la transaction est en erreur, on log
                if (getTransaction().hasErrors()) {
                    messageTransactionError.append(getTransaction().getErrors());
                    messageTransactionError.append("(NumAffilie = " + affControle.getNumeroAffilie() + ")\n");

                    getTransaction().rollback();
                    getTransaction().clearErrorBuffer();
                } else {
                    // Sinon on commit les modifications
                    getTransaction().commit();
                }

                // On fait avancer la progresse bar
                incProgressCounter();
            }

            addMailInformations(manager.getSize());

            // On envoi le mail avec les problemes si il y en a.
            if (messageTransactionError.length() != 0) {
                CEUtils.addMailInformationsError(getMemoryLog(), messageTransactionError.toString(), getClass()
                        .getName());
            }

        } catch (Exception e) {

            _addError(getTransaction(), getSession().getLabel("EXECUTION_RATTRAPAGE_ANNUEL_ERREUR"));

            String messageInformation = "Annee de rattrapage : " + getAnnee() + "\n";
            messageInformation += "NumAffilie : " + numAffilieTraite + "\n";
            messageInformation += CEUtils.stack2string(e);

            CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, getClass().getName());

            status = false;
        }

        return status;
    }

    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isEmpty(getAnnee())) {
            _addError(getTransaction(), getSession().getLabel("VAL_ANNEE_RATTRAPAGE"));
        }

        if (!CEUtils.validateYear(getAnnee())) {
            _addError(getTransaction(), getSession().getLabel("VAL_ANNEE_NON_VALIDE"));
        }
    }

    private void addMailInformations(int size) throws Exception {
        getMemoryLog().logMessage(getSession().getLabel("TOTAL_AFFILIE") + size, FWMessage.INFORMATION,
                getClass().getName());
    }

    public String getAnnee() {
        return annee;
    }

    public String getCollaborationC() {
        return collaborationC;
    }

    public String getCollaborationM() {
        return collaborationM;
    }

    public String getCollaborationTM() {
        return collaborationTM;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("RATTRAPAGE_ANNUEL_ERREUR") + getAnnee();
        } else {
            return getSession().getLabel("RATTRAPAGE_ANNUEL_OK") + getAnnee();
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

}
