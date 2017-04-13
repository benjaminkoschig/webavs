/**
 * 
 */
package globaz.naos.db.controleLpp;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import globaz.prestation.interfaces.util.nss.PRUtil;
import ch.globaz.common.domaine.Montant;

/**
 * @author est
 * 
 */
public class AFExtraitDS extends BEntity {

    private static final long serialVersionUID = 6355736232643677712L;
    private String nss;
    private String nomSalarie;
    private String moisDebut;
    private String annee;
    private String moisFin;
    private String salaire;

    private String seuilEntree;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        nss = statement.dbReadString("KANAVS");
        nomSalarie = statement.dbReadString("KALNOM");
        moisDebut = statement.dbReadString("KBNMOD");
        moisFin = statement.dbReadString("KBNMOF");
        annee = statement.dbReadString("KBNANN");
        salaire = statement.dbReadString("KBMMON");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        //
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        //
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        //
    }

    /***
     * Méthode qui permet de calculer le seuil LPP en fonction des valeurs dans les tabels de valeurs
     */
    public void calculSeuilLPP() {
        int periode = JadeStringUtil.toInt(getMoisFin()) - JadeStringUtil.toInt(getMoisDebut());

        try {
            FWFindParameterManager manager = new FWFindParameterManager();
            manager.setSession(getSession());
            manager.setIdCodeSysteme(CodeSystem.CS_SEUIL_LPP);
            manager.setIdCleDiffere("SEUILLPP");
            manager.setDateDebutValidite("01.01." + getAnnee());
            manager.find(BManager.SIZE_NOLIMIT);

            Montant valeurSeuil = new Montant(((FWFindParameter) manager.getFirstEntity()).getValeurNumParametre());

            valeurSeuil = valeurSeuil.multiply(periode / 12.0);

            setSeuilEntree(valeurSeuil.toStringFormat());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error when retrieving the 'plage de valeur' with key 'SEUILLPP' ("
                    + e.getMessage() + ")");
        }
    }

    // Getters

    public String getNss() {
        return PRUtil.formatNss(nss);
    }

    public String getNomSalarie() {
        return nomSalarie;
    }

    public String getMoisDebut() {
        return moisDebut;
    }

    public String getMoisFin() {
        return moisFin;
    }

    public String getSalaire() {
        return salaire;
    }

    public String getAnnee() {
        return annee;
    }

    public String getSeuilEntree() {
        return seuilEntree;
    }

    // Setters

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setNomSalarie(String nomSalarie) {
        this.nomSalarie = nomSalarie;
    }

    public void setMoisDebut(String moisDebut) {
        this.moisDebut = moisDebut;
    }

    public void setMoisFin(String moisFin) {
        this.moisFin = moisFin;
    }

    public void setSalaire(String salaire) {
        this.salaire = salaire;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setSeuilEntree(String seuilEntree) {
        this.seuilEntree = seuilEntree;
    }

}
