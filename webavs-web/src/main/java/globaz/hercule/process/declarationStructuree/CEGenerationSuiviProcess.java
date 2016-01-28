package globaz.hercule.process.declarationStructuree;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.db.ICEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEAffilieControle;
import globaz.hercule.db.controleEmployeur.CEAffilieControleManager;
import globaz.hercule.db.groupement.CEMembre;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.service.CEDeclarationStructureeService;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Map;

/**
 * @author SCO
 * @since 15 juil. 2010
 */
public class CEGenerationSuiviProcess extends BProcess {

    private static final long serialVersionUID = -8966382756042732176L;

    private static final int NB_ANNEE_RECHERCHE_MASSE_SALARIALE = 4;

    private String annee;
    private String anneePrecedente;
    private String fromNumAffilie;
    private Map<String, CEMembre> membreGroupe;
    private String toNumAffilie;

    /**
     * Constructeur de CEGenerationSuiviProcess
     */
    public CEGenerationSuiviProcess() {
        super();
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {

        String numAffilieTraite = "";
        StringBuffer errorBuff = new StringBuffer();
        int numAffilieProbleme = 0;
        int numAffilieDejaTraite = 0;
        int nbrGenerationCreee = 0;
        int nbrNonCotiAvs = 0;
        int nbrCouvertByControle = 0;

        try {

            anneePrecedente = CEUtils.getAnneePrecedente(getAnnee());
            membreGroupe = CEControleEmployeurService.loadMapMembreGroupe(getSession());

            // ****************************************
            // On fait la liste les employeurs actifs (affilié paritaire) pour
            // l'année précédente
            // ****************************************
            CEAffilieControleManager manager = new CEAffilieControleManager();
            manager.setSession(getSession());
            manager.setForDateCouverture(annee);
            manager.setForDateFinAffiliation("01.01." + annee);
            manager.setOrderBy(CEAffilieControleManager.ORDERBY_NUM_AFFILIE_CROISSANT);
            manager.setFromNumAffilie(getFromNumAffilie());
            manager.setToNumAffilie(getToNumAffilie());

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

                try {

                    // ****************************************
                    // Controle si date de début d'affiliation = date de fin affiliation
                    // Cas des affiliation par erreur.
                    // BUG 7698
                    // ****************************************
                    if (affControle.getDateDebutAffiliation() != null
                            && affControle.getDateDebutAffiliation().equals(affControle.getDateFinAffiliation())) {
                        numAffilieProbleme += 1;
                        continue;
                    }

                    // ****************************************
                    // Controle si l'affilié a une cotisation AVS pour l'année en cours
                    // ****************************************
                    if (!CEControleEmployeurService.hasCotisationAVS(getSession(), affControle.getIdAffilie(),
                            anneePrecedente)) {
                        nbrNonCotiAvs += 1;
                        continue;
                    }

                    // ****************************************
                    // Récupération de la catégorie de la masse salariale
                    // ****************************************
                    String anneeFinControle = null;
                    if (!JadeStringUtil.isEmpty(affControle.getDateFinControle())) {
                        anneeFinControle = "" + CEUtils.stringDateToAnnee(affControle.getDateFinControle());
                    }

                    // mini patch pour ne pas regarder en dessous de 2008
                    int int_annee_precedente = CEUtils.transformeStringToInt(anneePrecedente);
                    int annee_en_arriere = CEGenerationSuiviProcess.NB_ANNEE_RECHERCHE_MASSE_SALARIALE;
                    if (int_annee_precedente <= 2010) {
                        annee_en_arriere = 2;
                    } else if (int_annee_precedente <= 2011) {
                        annee_en_arriere = 3;
                    }

                    String categorie = CEControleEmployeurService.findCategorieMasseForGenerationSuivi(getSession(),
                            affControle.getNumeroAffilie(), anneePrecedente, anneeFinControle, annee_en_arriere);

                    // *****************************************
                    // Création d'un début de suivi pour l'affilié
                    // Si la masse salarial 0 et I
                    // *****************************************

                    // Si la masse est différente de 0 ou I, on génère un début
                    // de suivi
                    if (ICEControleEmployeur.CATEGORIE_MASSE_1A.equals(categorie)
                            || ICEControleEmployeur.CATEGORIE_MASSE_1B.equals(categorie)
                            || ICEControleEmployeur.CATEGORIE_MASSE_0.equals(categorie)) {

                        // On test si il n'existe pas déjà un début de suivi
                        // pour cet affilié
                        // et qu'il n'est pas dans un groupe.
                        if (!CEDeclarationStructureeService.isDejaJournalise(getSession(),
                                affControle.getNumeroAffilie(), annee)
                                && !membreGroupe.containsKey(affControle.getIdAffilie())) {

                            boolean dernierControleCouvreAff = false;
                            if (!JadeStringUtil.isEmpty(affControle.getDateFinControle())
                                    && !JadeStringUtil.isEmpty(affControle.getDateFinAffiliation())) {
                                if (affControle.getDateFinControle().equals(affControle.getDateFinAffiliation())) {
                                    dernierControleCouvreAff = true;
                                }
                            }

                            // Si il n'y pas de contrôle qui couvre la période d'affiliation, on fait un suivi
                            if (!dernierControleCouvreAff) {
                                CEDeclarationStructureeService.generationDebutSuivi(getSession(), affControle,
                                        categorie, getAnnee());

                                nbrGenerationCreee += 1;
                            } else {
                                // Sinon on ignore ce cas
                                nbrCouvertByControle += 1;
                            }

                        } else {
                            numAffilieDejaTraite += 1;
                        }
                    }

                } catch (Exception e) {
                    numAffilieProbleme += 1;
                    errorBuff.append("\nError generation suivi. Affilie num : " + affControle.getNumeroAffilie()
                            + "(idtiers:" + affControle.getIdTiers() + ",annee:" + getAnnee() + ")");
                }

                if (isAborted()) {
                    return false;
                }

                // On fait avancer la progresse bar
                incProgressCounter();
            }

            // Ajout des informations dans le mail sur le résultat de
            // l'execution du process
            addMailInformations(manager.getSize(), nbrGenerationCreee, numAffilieDejaTraite, numAffilieProbleme,
                    nbrNonCotiAvs, errorBuff.toString(), nbrCouvertByControle);

        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("EXECUTION_GENERATOIN_SUIVI_ERREUR"));

            String messageInformation = "Annee de génération des débuts de suivi : " + getAnnee() + "\n";
            messageInformation += "NumAffilie : " + numAffilieTraite + "\n";
            messageInformation += CEUtils.stack2string(e);

            CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            return false;
        }

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isEmpty(getAnnee())) {
            getSession().addError(getSession().getLabel("VAL_ANNEE"));
        }
    }

    /**
     * Ajoute des informations dans l'email.
     */
    private void addMailInformations(int sizeTraite, int sizeGenere, int sizeDejaTraite, int sizeNonTraite,
            int nbrNocCotiAvs, String errorMessage, int nbrCouvertByControle) throws Exception {
        getMemoryLog().logMessage(getSession().getLabel("TOTAL_AFFILIE_POTENTIEL") + sizeTraite, FWMessage.INFORMATION,
                this.getClass().getName());
        getMemoryLog().logMessage(getSession().getLabel("TOTAL_AFFILIE") + sizeGenere, FWMessage.INFORMATION,
                this.getClass().getName());
        getMemoryLog().logMessage(getSession().getLabel("TOTAL_AFFILIE_DEJA_TRAITE") + sizeDejaTraite,
                FWMessage.INFORMATION, this.getClass().getName());
        getMemoryLog().logMessage(getSession().getLabel("TOTAL_AFFILIE_NON_TRAITE") + sizeNonTraite,
                FWMessage.INFORMATION, this.getClass().getName());
        getMemoryLog().logMessage(getSession().getLabel("TOTAL_AFFILIE_NON_COTI_AVS") + nbrNocCotiAvs,
                FWMessage.INFORMATION, this.getClass().getName());
        getMemoryLog().logMessage(getSession().getLabel("TOTAL_AFFILIE_COUVERT_BY_CONTROLE") + nbrCouvertByControle,
                FWMessage.INFORMATION, this.getClass().getName());

        if (!JadeStringUtil.isBlank(errorMessage.toString())) {
            CEUtils.addMailInformationsError(getMemoryLog(), errorMessage, this.getClass().getName());
        }
    }

    public String getAnnee() {
        return annee;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("GENERATOIN_SUIVI_ERREUR");
        } else {
            return getSession().getLabel("GENERATOIN_SUIVI_OK");
        }
    }

    // *******************************************************
    // Getter
    // ***************************************************

    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public String getToNumAffilie() {
        return toNumAffilie;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    // *******************************************************
    // Getter
    // ***************************************************
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setFromNumAffilie(String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    public void setToNumAffilie(String toNumAffilie) {
        this.toNumAffilie = toNumAffilie;
    }

}