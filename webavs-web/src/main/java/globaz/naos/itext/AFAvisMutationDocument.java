/*
 * Created on Aug 4, 2006
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package globaz.naos.itext;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BSession;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.annonceAffilie.AFAnnonceAffilie;
import globaz.naos.db.annonceAffilie.AFAnnonceAffilieManager;
import globaz.naos.translation.CodeSystem;
import java.util.Iterator;
import java.util.List;

/**
 * @author Alexandre Cuva Created on Aug 4, 2006
 * 
 * 
 */
public class AFAvisMutationDocument extends AFAvisMutation_Doc {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private AFAnnonceAffilieManager annonceAffiliationManager = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    /**
     * Constructor for AFAvisMutationDocument
     * 
     */
    public AFAvisMutationDocument() {
        super();
    }

    /**
     * Constructor for AFAvisMutationDocument
     * 
     * @param session
     * @param newSelectionImpression
     * @throws Exception
     */
    public AFAvisMutationDocument(BSession session, String newSelectionImpression) throws Exception {
        super(session, newSelectionImpression);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        // Zones communes à toutes les différents type de décisions
        setSendCompletionMail(false);
        setFileTitle(AFAvisMutation_Doc.FILE_TITLE);
        setTemplateFile(AFAvisMutation_Doc.TEMPLATE_FILE);

        try {
            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession(getSession());
            affiliation.setAffiliationId(getIdAffiliation());
            affiliation.retrieve();
            itAvis.add(affiliation);
            currentAffiliation = affiliation;
        } catch (Exception e) {
            throw new FWIException(e);
        }
    }

    /**
     * Définition des types de modification, ici cela sert a rien, mais pour une classe fils oui. plus utilisé
     */
    @Override
    protected void beforeSetMotif() throws Exception {
        // On recherche le type de changement
        AFAnnonceAffilieManager manager = getAnnonceAffiliationManager();
        if (manager != null) {
            AFAnnonceAffilie annonce = null;
            manager.setSession(getSession());
            manager.setForAffiliationId(getIdAffiliation());
            manager.find();
            Iterator it = manager.iterator();
            while (it.hasNext()) {
                annonce = (AFAnnonceAffilie) it.next();
                if (CodeSystem.CHAMPS_MOD_CREATION_AFFILIE.equals(annonce.getChampModifier())
                        || CodeSystem.CHAMPS_MOD_NUM_AFFILIE.equals(annonce.getChampModifier())) {
                    setSelectionImpression(AFAvisMutation_Doc.IMPR_NOUVELLE_AFFILIATION);
                    return;
                }
                if (CodeSystem.CHAMPS_MOD_DATE_FIN.equals(annonce.getChampModifier())) {
                    setSelectionImpression(AFAvisMutation_Doc.IMPR_SORTIE);
                    return;
                }
                setSelectionImpression(AFAvisMutation_Doc.IMPR_CHANGEMENT);
                if (annonce.getChampModifier().equals(CodeSystem.CHAMPS_MOD_TIER_AVS)) {
                    setAvsChange(true);
                }
                if (annonce.getChampModifier().equals(CodeSystem.CHAMPS_MOD_ADRESSE_DOMICILE)
                        || annonce.getChampModifier().equals(CodeSystem.CHAMPS_MOD_ADRESSE_COURRIER)
                        || annonce.getChampModifier().equals(CodeSystem.CHAMPS_MOD_ADRESSE_PROFES)) {
                    setAdresseChange(true);
                }
                if (annonce.getChampModifier().equals(CodeSystem.CHAMPS_MOD_TIER_NOM)) {
                    setNomChange(true);
                }
                if (annonce.getChampModifier().equals(CodeSystem.CHAMPS_MOD_PERSON_MAISON)) {
                    setPmChange(true);
                }
                if (annonce.getChampModifier().equals(CodeSystem.CHAMPS_MOD_SIEGE)) {
                    setSiegeChange(true);
                }
                if (annonce.getChampModifier().equals(CodeSystem.CHAMPS_MOD_TIER_CANTON)) {
                    setCantonChange(true);
                    return;
                }
            }
        }
        // date de situation
        setDateAvis(AFAffiliationUtil.getDateSituation(currentAffiliation));
    }

    /**
     * @return
     */
    public AFAnnonceAffilieManager getAnnonceAffiliationManager() {
        return annonceAffiliationManager;
    }

    @Override
    protected AFAvisMutation_Doc getInstance(BSession session, String selectionImpression) throws Exception {
        return new AFAvisMutationDocument(session, selectionImpression);
    }

    /**
     * @param manager
     */
    public void setAnnonceAffiliationManager(AFAnnonceAffilieManager manager) {
        annonceAffiliationManager = manager;
    }

    public void setModifications(List changes) {
        Iterator it = changes.iterator();
        String change;
        while (it.hasNext()) {
            change = (String) it.next();
            if (CodeSystem.CHAMPS_MOD_CREATION_AFFILIE.equals(change)
                    || CodeSystem.CHAMPS_MOD_NUM_AFFILIE.equals(change)) {
                setSelectionImpression(AFAvisMutation_Doc.IMPR_NOUVELLE_AFFILIATION);
                // toute autre modification est ignorée
                return;
            }
            if (CodeSystem.CHAMPS_MOD_DATE_FIN.equals(change)) {
                setSelectionImpression(AFAvisMutation_Doc.IMPR_SORTIE);
                // toute autre modification est ignorée
                return;
            }
            if (change.equals(CodeSystem.CHAMPS_MOD_TIER_AVS)) {
                setSelectionImpression(AFAvisMutation_Doc.IMPR_CHANGEMENT);
                setAvsChange(true);
            }
            if (change.equals(CodeSystem.CHAMPS_MOD_ADRESSE_DOMICILE)
                    || change.equals(CodeSystem.CHAMPS_MOD_ADRESSE_COURRIER)
                    || change.equals(CodeSystem.CHAMPS_MOD_ADRESSE_PROFES)) {
                setSelectionImpression(AFAvisMutation_Doc.IMPR_CHANGEMENT);
                setAdresseChange(true);
            }
            if (change.equals(CodeSystem.CHAMPS_MOD_TIER_NOM)) {
                setSelectionImpression(AFAvisMutation_Doc.IMPR_CHANGEMENT);
                setNomChange(true);
            }
            if (change.equals(CodeSystem.CHAMPS_MOD_PERSON_MAISON)) {
                setSelectionImpression(AFAvisMutation_Doc.IMPR_CHANGEMENT);
                setPmChange(true);
            }
            if (change.equals(CodeSystem.CHAMPS_MOD_SIEGE)) {
                setSelectionImpression(AFAvisMutation_Doc.IMPR_CHANGEMENT);
                setSiegeChange(true);
            }
            if (change.equals(CodeSystem.CHAMPS_MOD_TIER_CANTON)) {
                setSelectionImpression(AFAvisMutation_Doc.IMPR_CHANGEMENT);
                setCantonChange(true);
                setMorePageNeeded(true);
            }
        }
    }

}
