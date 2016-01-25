package globaz.naos.process;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.attestation.AFEcritureAffilieForAttesPerso;
import globaz.naos.db.attestation.AFEcritureAffilieForAttesPersoManager;
import globaz.naos.itext.AFAttestationPersonnelle_doc;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.comptes.CAReferenceRubriqueManager;
import globaz.osiris.db.comptes.CARubrique;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Process permettant de sortir une lettre d'attestation des cotisations factur�es<br>
 * Cette lettre indique le montant des cotisationspour l'ann�e concern�e et <br>
 * �ventuellement le montant qui aurait �t� factur� pour des ann�es pr�c�dentes (cas r�troactif)
 * 
 * @author SCO
 * @since 05 juil. 2011
 */
@SuppressWarnings("serial")
public class AFAttestationPersonnelleProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = null;
    private String dateEnvoiMasse = "";
    private String fromAffilie = null;

    private Collection<String> listRubrique = null;

    private Collection<String> rubriqueMaj = null;
    private Collection<String> rubriqueMin = null;
    private String toAffilie = null;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        String numAffilie = null;

        try {

            // ----------------------------
            // 1. R�cup�ration des rubriques
            // ----------------------------
            initialiseRubrique();

            // Trier les affili�s suivant les bornes pour ne garder que les ind�pendants et non-actif
            // ----------------------------
            // 2. R�cup�ration de la liste des affili�es
            // ----------------------------
            AFAffiliationManager affManager = new AFAffiliationManager();
            affManager.setSession(getSession());

            // Si des bornes de num�ro d'affili� a �t� sp�cifi�, on les recherche
            if (!JadeStringUtil.isEmpty(getFromAffilie()) && !JadeStringUtil.isEmpty(getToAffilie())) {
                affManager.setFromAffilieNumero(getFromAffilie());
                affManager.setToAffilieNumero(getToAffilie());
            }

            String[] listTypeAffiliation = { CodeSystem.TYPE_AFFILI_INDEP, CodeSystem.TYPE_AFFILI_INDEP_EMPLOY,
                    CodeSystem.TYPE_AFFILI_NON_ACTIF, CodeSystem.TYPE_AFFILI_TSE, CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE };

            affManager.setForTypeAffiliation(listTypeAffiliation);
            affManager.setOrderBy(AFAffiliationManager.ORDER_AFFILIENUMERO);
            affManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            setProgressScaleValue(affManager.getSize());
            Object[] listAffilie = affManager.getContainer().toArray();

            // ----------------------------
            // Pour chaque affili�, on g�n�re ou pas l'attestation
            // ----------------------------
            for (Object obj_affilie : listAffilie) {

                AFAffiliation aff = (AFAffiliation) obj_affilie;

                // gestion des doublons
                if (!JadeStringUtil.isEmpty(numAffilie) && numAffilie.equals(aff.getAffilieNumero())) {
                    // On fait avancer la progresse bar
                    incProgressCounter();

                    // On passe au suivant.
                    continue;
                }

                // Information pour le process
                numAffilie = aff.getAffilieNumero();

                setProgressDescription("Num Affilie : " + numAffilie);

                FWCurrency montantAnneeEnCours = new FWCurrency();
                FWCurrency montantAnneeAnterieur = new FWCurrency();

                // ----------------------------
                // 3. On recherche les �critures en compta
                // ----------------------------
                AFEcritureAffilieForAttesPersoManager manager = new AFEcritureAffilieForAttesPersoManager();
                manager.setSession(getSession());
                manager.setForAnnee(getAnnee());
                manager.setForNumAffilie(aff.getAffilieNumero());
                manager.setForIdExterneRubriqueIn(listRubrique);
                manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

                Object[] listEcritures = manager.getContainer().toArray();

                // ----------------------------
                // Pour chaque ecriture, on fait les cumuls qui vont bien :)
                // ----------------------------
                for (Object obj_ecriture : listEcritures) {
                    AFEcritureAffilieForAttesPerso ecriture = (AFEcritureAffilieForAttesPerso) obj_ecriture;

                    // si c'est l'ann�e en cours
                    if (getAnnee().equals(ecriture.getAnneeCotisation())) {

                        // On ajout ou on soustrait ce montant ?
                        // if (this.rubriqueMaj.contains(ecriture.getIdExterne())) {
                        // montantAnneeEnCours.add(ecriture.getMontantSigne());
                        // } else {
                        // montantAnneeEnCours.sub(ecriture.getMontantSigne());
                        // }

                        montantAnneeEnCours.add(ecriture.getMontantSigne());
                    } else { // Cela concerne les ann�es ant�rieurs

                        // On ajout ou on soustrait ce montant ?
                        // if (this.rubriqueMaj.contains(ecriture.getIdExterne())) {
                        // montantAnneeAnterieur.add(ecriture.getMontantSigne());
                        // } else {
                        // montantAnneeAnterieur.sub(ecriture.getMontantSigne());
                        // }

                        montantAnneeAnterieur.add(ecriture.getMontantSigne());
                    }
                }

                FWCurrency total = new FWCurrency();
                total.add(montantAnneeEnCours);
                total.add(montantAnneeAnterieur);

                if ((total.doubleValue() > 1.00) || (total.doubleValue() < -1.00)) {
                    // Cr�ation du document
                    AFAttestationPersonnelle_doc attesPersoDoc = new AFAttestationPersonnelle_doc(getSession());
                    attesPersoDoc.setParent(this);
                    attesPersoDoc.setAnnee(getAnnee());
                    if (JadeStringUtil.isBlankOrZero(getDateEnvoiMasse())) {
                        attesPersoDoc.setDateEnvoi(JACalendar.todayJJsMMsAAAA());
                    } else {
                        attesPersoDoc.setDateEnvoi(getDateEnvoiMasse());
                    }
                    attesPersoDoc.setNumAffilie(numAffilie);
                    attesPersoDoc.setIdTiers(aff.getIdTiers());
                    attesPersoDoc.setSommeAnnee(montantAnneeEnCours.toString());
                    attesPersoDoc.setSommeAnterieur(montantAnneeAnterieur.toString());
                    attesPersoDoc.setTotalCoti(total.toString());

                    attesPersoDoc.executeProcess();
                }

                // On fait avancer la progresse bar
                incProgressCounter();

                if (isAborted()) {
                    return true;
                }
            }

            // merge du document
            JadePublishDocumentInfo _docInfo = createDocumentInfo();
            _docInfo.setDocumentTypeNumber(AFAttestationPersonnelle_doc.NUMERO_INFOROM);
            _docInfo.setPublishDocument(true);
            _docInfo.setArchiveDocument(false);
            this.mergePDF(_docInfo, false, 0, false, null);

        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("EXECUTION_ATTESTATION_PERSONNELLE_ERREUR"));

            String messageInformation = "Annee des attestations : " + getAnnee() + "\n";
            messageInformation += "NumAffilie : " + numAffilie + "\n";
            messageInformation += AFUtil.stack2string(e);

            AFUtil.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            return false;
        }

        return true;
    }

    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isEmpty(getAnnee())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_ANNEE_ATT_PERSO"));
        }

        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_EMAIL_ATT_PERSO"));
        }

    }

    public String getAnnee() {
        return annee;
    }

    public String getDateEnvoiMasse() {
        return dateEnvoiMasse;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("IMPRESSION_ATTESTATION_PERSONNELLE_ERREUR") + " " + getAnnee();
        } else {
            return getSession().getLabel("IMPRESSION_ATTESTATION_PERSONNELLE_OK") + " " + getAnnee();
        }
    }

    public String getFromAffilie() {
        return fromAffilie;
    }

    public String getToAffilie() {
        return toAffilie;
    }

    /**
     * Permet la r�cup�ration de toutes les rubriques relatif au document que l'on veut g�n�rer. <br>
     * Grace a cette liste, on va pouvoir regarder es ecritures dans la comptabilit�.
     * 
     * Les informations sont stock�es dans 2 map. <br>
     * rubriqueMaj : Map qui stocke les rubriques dont les �critures majoreront les cotisations <br>
     * rubriqueMin : Map qui stocke les rubriques dont les �critures minoreront les cotisations
     * 
     * @throws Exception
     */
    private void initialiseRubrique() throws Exception {

        rubriqueMaj = new ArrayList<String>();
        rubriqueMin = new ArrayList<String>();
        listRubrique = new ArrayList<String>();

        // R�cup�ration des rubriques dont le genre est personnelle
        AFAssuranceManager manager = new AFAssuranceManager();
        manager.setSession(getSession());
        manager.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
        manager.setForInTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AC + "," + CodeSystem.TYPE_ASS_COTISATION_AC2
                + "," + CodeSystem.TYPE_ASS_COTISATION_AF + "," + CodeSystem.TYPE_ASS_COTISATION_AVS_AI + ","
                + CodeSystem.TYPE_ASS_AFI + "," + CodeSystem.TYPE_ASS_MATERNITE);
        manager.wantCallMethodBeforeFind(false);
        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        Object[] assurances = manager.getContainer().toArray();

        // Pour chaque assurance trouv�e
        for (Object obj_assurance : assurances) {

            AFAssurance assurance = (AFAssurance) obj_assurance;

            // On ajoute dans la map des rubriques majorante la rubrique relatif a l'assurance
            rubriqueMaj.add(assurance.getRubriqueComptable().getIdExterne());

            // On recherche les parametres d'assurance "r�duction" et "remise"
            String paramAssuReduc = assurance.getParametreAssuranceValeur(CodeSystem.GEN_PARAM_ASS_REDUCTION, "01.01."
                    + getAnnee(), "");
            String paramAssuRemise = assurance.getParametreAssuranceValeur(CodeSystem.GEN_PARAM_ASS_REMISE, "01.01."
                    + getAnnee(), "");

            // Si existante, on ajoute la valeur correspondante a la rubrique de ce parametre d'assurance
            // On l'ajoute dans la map des rubriques minorantes
            if (paramAssuRemise != null) {
                rubriqueMin.add(paramAssuRemise);
            }
            if (paramAssuReduc != null) {
                rubriqueMin.add(paramAssuReduc);
            }
        }

        // R�cup�ration des rubriques d'imputation "avs" et frais
        CAReferenceRubriqueManager refRubManager = new CAReferenceRubriqueManager();
        refRubManager.setSession(getSession());

        // recherche dans les r�f�rence rubrique des imputations
        List<String> codeReference = new ArrayList<String>();
        codeReference.add(APIReferenceRubrique.IMPUTATION_DE_COTISATION_PERSONNELLE);
        // codeReference.add(APIReferenceRubrique.MONTANT_MINIME);

        refRubManager.setForCodeReferenceIn(codeReference);
        refRubManager.find(getTransaction());

        Object[] refRubs = refRubManager.getContainer().toArray();

        // Pour chaque r�f�rence trouv�, on cherche la rubrique correspondante
        for (Object obj_refRub : refRubs) {
            CAReferenceRubrique refRub = (CAReferenceRubrique) obj_refRub;

            CARubrique rubrique = new CARubrique();
            rubrique.setIdRubrique(refRub.getIdRubrique());
            rubrique.retrieve(getTransaction());

            // Si il existe bien une rubrique associ�, on ajoute l'id externe
            if (!rubrique.isNew()) {
                rubriqueMin.add(rubrique.getIdExterne());
            }

        }

        listRubrique.addAll(rubriqueMaj);
        listRubrique.addAll(rubriqueMin);
        listRubrique.addAll(extrasRubriques());
    }

    /**
     * Retourne les id externes rubriques compl�mentaires � prendre en compte pour l'�dition de l'attestation.
     * 
     * @return Collection d'id externes
     * @throws Exception
     */
    public Collection<String> extrasRubriques() throws Exception {
        String idExternes = getSession().getApplication().getProperty(
                AFApplication.PROPERTY_ATTESTATION_RUBRIQUES_COMPLEMENTAIRES);
        if (!JadeStringUtil.isEmpty(idExternes)) {
            return Arrays.asList(idExternes.split(","));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateEnvoiMasse(String dateEnvoiMasse) {
        this.dateEnvoiMasse = dateEnvoiMasse;
    }

    public void setFromAffilie(String fromAffilie) {
        this.fromAffilie = fromAffilie;
    }

    public void setToAffilie(String toAffilie) {
        this.toAffilie = toAffilie;
    }

}
