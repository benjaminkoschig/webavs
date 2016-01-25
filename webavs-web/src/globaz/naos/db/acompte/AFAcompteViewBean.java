package globaz.naos.db.acompte;

import globaz.globall.db.BStatement;
import globaz.naos.db.affiliation.AFAffiliation;

public class AFAcompteViewBean extends AFAffiliation implements globaz.framework.bean.FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String dateDebut = new String();
    private java.lang.String dateFin = new String();
    private java.lang.String idPlanAffiliation = new String();
    private java.lang.Boolean maisonMere = new Boolean(false);
    private java.lang.Boolean surDocAcompte = new Boolean(true);

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
        super._readProperties(statement);
        dateDebut = statement.dbReadDateAMJ("MEDDEB");
        dateFin = statement.dbReadDateAMJ("MEDFIN");
        maisonMere = statement.dbReadBoolean("MEBMER");
        surDocAcompte = statement.dbReadBoolean("MBBSDA");
        idPlanAffiliation = statement.dbReadNumeric("MUIPLA");

    }

    /**
     * @return
     */
    @Override
    public java.lang.String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return
     */
    @Override
    public java.lang.String getDateFin() {
        return dateFin;
    }

    /**
     * @return
     */
    public java.lang.Boolean getMaisonMere() {
        return maisonMere;
    }

    /**
     * @return
     */
    public java.lang.Boolean getSurDocAcompte() {
        return surDocAcompte;
    }

    /**
     * @param string
     */
    @Override
    public void setDateDebut(java.lang.String string) {
        dateDebut = string;
    }

    /**
     * @param string
     */
    @Override
    public void setDateFin(java.lang.String string) {
        dateFin = string;
    }

    /**
     * @param boolean1
     */
    public void setMaisonMere(java.lang.Boolean boolean1) {
        maisonMere = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setSurDocAcompte(java.lang.Boolean boolean1) {
        surDocAcompte = boolean1;
    }

}
