/*
 * Created on Jul 3, 2006
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package globaz.naos.itext.affiliation;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.application.AFApplication;
import globaz.naos.db.annonceAffilie.AFAnnonceAffilie;
import globaz.naos.db.annonceAffilie.AFAnnonceAffilieManager;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ce Singleton permet de connaitre si un affilier doit être imprimer dans tel ou tel document
 * 
 * @author cuva
 * 
 *         To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 */
public final class AFCodeSystemMediator {

    private static List _avisMutationList = new ArrayList();
    private static List _bordereauMutationList = new ArrayList();
    // List
    private static List _cartoThequeList = new ArrayList();

    private final static String[] AvisMutationArray = { CodeSystem.CHAMPS_MOD_CREATION_AFFILIE,
            CodeSystem.CHAMPS_MOD_TIER_CANTON, CodeSystem.CHAMPS_MOD_ADRESSE_DOMICILE, CodeSystem.CHAMPS_MOD_DATE_FIN,
            CodeSystem.CHAMPS_MOD_ADRESSE_PROFES, CodeSystem.CHAMPS_MOD_SIEGE, CodeSystem.CHAMPS_MOD_TIER_NOM,
            CodeSystem.CHAMPS_MOD_PERSON_MAISON, CodeSystem.CHAMPS_MOD_NUM_AFFILIE,
            CodeSystem.CHAMPS_MOD_ADRESSE_COURRIER, CodeSystem.CHAMPS_MOD_TIER_AVS };
    private final static String[] BordereauMutationArray = { CodeSystem.CHAMPS_MOD_CREATION_AFFILIE,
            CodeSystem.CHAMPS_MOD_ADRESSE_COURRIER, CodeSystem.CHAMPS_MOD_ADRESSE_DOMICILE,
            CodeSystem.ADRESSE_PROFESSIONNELLE, CodeSystem.CHAMPS_MOD_CREATION_COTI,
            CodeSystem.CHAMPS_MOD_DATE_DEBUT_COTI, CodeSystem.CHAMPS_MOD_DATE_FIN_COTI, CodeSystem.CHAMPS_MOD_TIER_NOM,
            CodeSystem.CHAMPS_MOD_NUM_AFFILIE, CodeSystem.CHAMPS_MOD_PERSON_JURI, CodeSystem.CHAMPS_MOD_ADRESSE_PROFES,
            CodeSystem.CHAMPS_MOD_TIER_AVS, CodeSystem.CHAMPS_MOD_TIER_ORIG, CodeSystem.CHAMPS_MOD_MOYEN_COMM };
    // List
    private final static String[] CartoThequeArray = { CodeSystem.CHAMPS_MOD_CREATION_AFFILIE,
            CodeSystem.CHAMPS_MOD_NUM_AFFILIE, CodeSystem.CHAMPS_MOD_TIER_CONJOINT, CodeSystem.CHAMPS_MOD_PERSON_JURI,
            CodeSystem.CHAMPS_MOD_BRANCH_ECONO, CodeSystem.CHAMPS_MOD_ADRESSE_COURRIER,
            CodeSystem.CHAMPS_MOD_ADRESSE_DOMICILE, CodeSystem.CHAMPS_MOD_ADRESSE_PAIEMENT,
            CodeSystem.CHAMPS_MOD_TIER_NOM, CodeSystem.CHAMPS_MOD_NUM_AF_ANCIEN, CodeSystem.CHAMPS_MOD_DATE_DEBUT_COTI,
            CodeSystem.CHAMPS_MOD_DATE_DEBUT, CodeSystem.CHAMPS_MOD_DATE_FIN, CodeSystem.CHAMPS_MOD_MOTIF_FIN,
            CodeSystem.CHAMPS_MOD_PERIODICITE, CodeSystem.CHAMPS_MOD_EXO_GENERAL,
            CodeSystem.CHAMPS_MOD_ANNEE_DECIS_COTI, CodeSystem.CHAMPS_MOD_MONT_ANNU_COTI,
            CodeSystem.CHAMPS_MOD_MASSE_ANNU_COTI, CodeSystem.CHAMPS_MOD_CREATION_CONTROL,
            CodeSystem.CHAMPS_MOD_CREATION_COTI, CodeSystem.CHAMPS_MOD_DATE_FIN_COTI, CodeSystem.CHAMPS_MOD_AGENCE_AVS,
            CodeSystem.CHAMPS_MOD_ADRESSE_PROFES, CodeSystem.CHAMPS_MOD_TIER_AVS, CodeSystem.CHAMPS_MOD_TIER_CONTRIB,
            CodeSystem.CHAMPS_MOD_TIER_ORIG, CodeSystem.CHAMPS_MOD_MOYEN_COMM };

    static {
        AFCodeSystemMediator._cartoThequeList.addAll(Arrays.asList(AFCodeSystemMediator.CartoThequeArray));
        AFCodeSystemMediator._bordereauMutationList.addAll(Arrays.asList(AFCodeSystemMediator.BordereauMutationArray));
        AFCodeSystemMediator._avisMutationList.addAll(Arrays.asList(AFCodeSystemMediator.AvisMutationArray));
    }

    private AFApplication _application = null;
    private List _avisModificationList = new ArrayList();
    private List _bordereauModificationList = new ArrayList();
    private List _cartoModificationList = new ArrayList();
    private boolean _isAvisMutation = false;
    private boolean _isBordereauMutation = false;
    // private String _typeAvisMutation = null;
    private boolean _isCartotheque = false;
    // observations
    private StringBuffer _observationsAvis = new StringBuffer();
    private StringBuffer _observationsBordereau = new StringBuffer();
    private StringBuffer _observationsCarto = new StringBuffer();
    private BSession _session = null;
    private BTransaction _transaction = null;
    private boolean isDoAvis = true;
    private boolean isDoBordereau = false;
    private boolean isDoCarto = false;

    /**
     * Default Constructor
     * 
     * @exception java.lang.Exception
     *                - if BSession.GetApplication() throw an Exception
     */
    public AFCodeSystemMediator(BSession pSession, BTransaction pTransaction) throws Exception {
        _session = pSession;
        _transaction = pTransaction;
        _application = (AFApplication) pSession.getApplication();
        isDoAvis = getApplication().asDocAvisMutation();
        isDoBordereau = getApplication().asDocBordereauMutation();
        isDoCarto = getApplication().asDocCartotheque();

    }

    /**
     * Start looking a NumAffilie if it as any doc to print
     * 
     * @param pNumAffilie
     * @exception - Throw an Exception if the work cannot be donne.
     */
    public void check(String pNumAffilie, String date) throws Exception {
        reset();
        // Get the list of annonce for a NumAffilie
        AFAnnonceAffilieManager manager = new AFAnnonceAffilieManager();
        manager.setSession(getSession());
        manager.setForAffiliationId(pNumAffilie);
        if (!JadeStringUtil.isEmpty(date)) {
            manager.setForDateAnnonce(date);
            manager.forTraitement("2");
        } else {
            manager.forTraitement("1");
        }
        // Open a cursor
        String champModifier = null;
        AFAnnonceAffilie entity = null;
        BStatement statement = null;
        try {
            statement = manager.cursorOpen(getTransaction());
            while ((entity = (AFAnnonceAffilie) manager.cursorReadNext(statement)) != null) {
                champModifier = entity.getChampModifier();
                if (champModifier == null) {
                    continue;
                }
                setIsAvisMutation(champModifier, entity.getObservation());
                setIsCartotheque(champModifier, entity.getObservation());
                setIsBordereauMutation(champModifier, entity.getObservation());
                // If Cartotheque is true and no need BordereauMutation and
                // AvisMutation then no need to check again
                /*
                 * DGI: pas correct if (!getApplication().asDocAvisMutation() && isCartoTheque() &&
                 * !getApplication().asDocBordereauMutation()) { break; }
                 */
            }
            manager.cursorClose(statement);
        } catch (Exception e) {
            JadeLogger.debug(this, e);
            throw e;
        } finally {
            if (statement != null) {
                statement.closeStatement();
                statement = null;
            }
        }
    }

    private AFApplication getApplication() {
        return _application;
    }

    public List getAvisModificationList() {
        return _bordereauModificationList;
    }

    /**
     * Get the changed field list if a check have been made before, or an empty list if not.
     * 
     * @return
     */
    public List getBordereauModificationList() {
        return _bordereauModificationList;
    }

    public List getCartoModificationList() {
        return _bordereauModificationList;
    }

    private BSession getSession() {
        return _session;
    }

    private BTransaction getTransaction() throws Exception {
        if (_transaction == null) {
            _transaction = (BTransaction) getSession().newTransaction();
        }
        return _transaction;
    }

    /**
     * @return
     */
    // public String getTypeAvisMutation() {
    // return _typeAvisMutation;
    // }

    /**
     * Get to know if an Affielied need to be print with the AvisMutation Doc
     * 
     * @return true if a field is found
     */
    public boolean isAvisMutation() {
        return _isAvisMutation;
    }

    // private void setIsAvisMutation(boolean value) {
    // _isAvisMutation = _isAvisMutation || value;
    // }

    /**
     * Get to know if an Affielied need to be print with the BordereauMutation Doc
     * 
     * @return true if a field is found
     */
    public boolean isBordereauMutation() {
        return _isBordereauMutation;
    }

    /**
     * Get to know if an Affielied need to be print with the CartoTheque Doc
     * 
     * @return true if a field is found
     */
    public boolean isCartoTheque() {
        return _isCartotheque;
    }

    /**
     * Reset all the value to process a new request
     */
    public void reset() {
        _bordereauModificationList.clear();
        _cartoModificationList.clear();
        _avisModificationList.clear();
        _isAvisMutation = false;
        _isBordereauMutation = false;
        _isCartotheque = false;
    }

    private void setBordereauList(List pBordereauList) {
        _bordereauModificationList = pBordereauList;
    }

    private void setIsAvisMutation(String champModifier, String observation) {
        boolean isAValue = isDoAvis && AFCodeSystemMediator._avisMutationList.contains(champModifier);
        if (isAValue) {
            _isAvisMutation = _isAvisMutation || isAValue;
            _avisModificationList.add(champModifier);
            _observationsAvis.append(observation + "\n");
            /*
             * Déplacé dans AFAvisMutationDocument if(CodeSystem.CHAMPS_MOD_CREATION_AFFILIE.equals(champModifier)){
             * setTypeAvisMutation (AFAvisMutationDocument.IMPR_NOUVELLE_AFFILIATION); return; }
             * if(CodeSystem.CHAMPS_MOD_DATE_FIN.equals(champModifier)){
             * setTypeAvisMutation(AFAvisMutationDocument.IMPR_SORTIE); return; }
             * setTypeAvisMutation(AFAvisMutationDocument.IMPR_CHANGEMENT);
             */
        }
    }

    private void setIsBordereauMutation(String champModifier, String observation) {
        boolean isAValue = isDoBordereau && AFCodeSystemMediator._bordereauMutationList.contains(champModifier);
        if (isAValue) {
            _isBordereauMutation = _isBordereauMutation || isAValue;
            _bordereauModificationList.add(champModifier);
            _observationsBordereau.append(observation + "\n");
        }
    }

    private void setIsCartotheque(String champModifier, String observation) {
        boolean isAValue = isDoCarto && AFCodeSystemMediator._cartoThequeList.contains(champModifier);
        if (isAValue) {
            _isCartotheque = _isCartotheque || isAValue;
            _cartoModificationList.add(champModifier);
            _observationsCarto.append(observation + "\n");
        }
    }

    /**
     * @param string
     */
    // private void setTypeAvisMutation(String string) {
    // _typeAvisMutation = string;
    // }
}
