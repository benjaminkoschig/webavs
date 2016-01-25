package globaz.naos.db.plan;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.planAssurance.AFPlanAssurance;
import globaz.naos.db.planAssurance.AFPlanAssuranceListViewBean;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * La classe définissant l'entité Plan.
 * 
 * @author administrator
 */
public class AFPlan extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String planId = new String();
    private java.lang.String planLibelle = new String();
    private java.lang.String selectionAssurance = new String();

    /**
     * Constructeur d'AFPlan.
     */
    public AFPlan() {
        super();
    }

    /**
     * Ajoute un nouveau plan.
     * 
     * @throws Exception
     */
    public void _ajouterPlan(/* String assuranceSelection, String planLibelle */) throws Exception {

        AFPlan plan = new AFPlan();
        plan.setPlanLibelle(getPlanLibelle());
        plan.setSession(getSession());
        plan.add();

        if (!JadeStringUtil.isEmpty(getSelectionAssurance()/* assuranceSelection */)) {

            AFPlanAssurance planAssurance = new AFPlanAssurance();
            planAssurance.setSession(getSession());

            StringTokenizer st = new StringTokenizer(getSelectionAssurance()/* assuranceSelection */, ".");
            while (st.hasMoreTokens()) {
                String assuranceId = st.nextToken();
                planAssurance.setPlanId(plan.getPlanId());
                planAssurance.setAssuranceId(assuranceId);
                planAssurance.add();
            }
        }
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente de +1 le numéro
        setPlanId(this._incCounter(transaction, "0"));
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFPLANP";
    }

    /**
     * Mettre à jour la liste d'assurance du plan.
     * 
     * @throws Exception
     */
    public void _modifierSelectionAssurance(/*
                                             * String assuranceSelection, String planId
                                             */) throws Exception {

        AFPlanAssurance planAssurance = new AFPlanAssurance();
        planAssurance.setSession(getSession());

        // Get old selection Assurance
        AFPlanAssuranceListViewBean manager = new AFPlanAssuranceListViewBean();
        manager.setForPlanId(getPlanId());
        manager.setSession(getSession());
        manager.find();

        ArrayList oldSelection = new ArrayList();
        for (int i = 0; i < manager.size(); i++) {
            oldSelection.add(new Integer(manager.getAssuranceId(i).trim()));
        }

        // Get new selection Assurance
        ArrayList newSelection = new ArrayList();
        if (!JadeStringUtil.isEmpty(getSelectionAssurance()/* assuranceSelection */)) {

            StringTokenizer st = new StringTokenizer(getSelectionAssurance()/* assuranceSelection */, ".");
            while (st.hasMoreTokens()) {
                String assuranceId = st.nextToken().trim();
                newSelection.add(new Integer(assuranceId));
            }
        }

        // Found add item
        for (int i = 0; i < newSelection.size(); i++) {
            Integer assuranceId = (Integer) newSelection.get(i);
            if (!oldSelection.contains(assuranceId)) {
                // ADD
                planAssurance.setPlanId(getPlanId());
                planAssurance.setAssuranceId(assuranceId.toString());
                planAssurance.add();
            }
        }

        // Found delete item
        for (int i = 0; i < oldSelection.size(); i++) {
            Integer assuranceId = (Integer) oldSelection.get(i);
            if (!newSelection.contains(assuranceId)) {
                // DELETE
                planAssurance.setPlanId(getPlanId());
                planAssurance.setAssuranceId(assuranceId.toString());
                planAssurance.retrieve();
                planAssurance.delete();
            }
        }
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        planId = statement.dbReadNumeric("MRIPLA");
        planLibelle = statement.dbReadString("MRLPLA");

    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MRIPLA", this._dbWriteNumeric(statement.getTransaction(), getPlanId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MRIPLA", this._dbWriteNumeric(statement.getTransaction(), getPlanId(), "PlanId"));
        statement
                .writeField("MRLPLA", this._dbWriteString(statement.getTransaction(), getPlanLibelle(), "PlanLibelle"));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getPlanId() {
        return planId;
    }

    public java.lang.String getPlanLibelle() {
        return planLibelle;
    }

    /**
     * Renvoie la liste d'assurance pour le plan.
     * 
     * @return les assurances du plan
     */
    public java.lang.String getSelectionAssurance() {
        return selectionAssurance;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setPlanId(java.lang.String newPlanId) {
        planId = newPlanId;
    }

    public void setPlanLibelle(java.lang.String newPlanLibelle) {
        planLibelle = newPlanLibelle;
    }

    /**
     * Definir la liste d'assurance du plan
     * 
     * @param newSelectionAssurance
     *            les assurances du plan
     */
    public void setSelectionAssurance(java.lang.String newSelectionAssurance) {
        selectionAssurance = newSelectionAssurance;
    }
}
