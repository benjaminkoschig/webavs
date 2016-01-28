/**
 * 
 */
package globaz.helios.db.classifications;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.helios.db.comptes.CGSolde;

/**
 * BEntity fictif représentant les informations necessaire à la construction des listes (représente les tables CGCOMP et
 * CGSOLDP).
 * 
 * @author sel
 * 
 */
public class CGExtendedCompteSolde extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String avoir = "";
    private String avoirmonnaie = "";
    private String avoirprovisoire = "";
    private String avoirprovisoiremonnaie = "";
    private String budget = "";
    private String codeISOMonnaie = "";
    private String doit = "";
    private String doitmonnaie = "";
    private String doitprovisoire = "";
    private String doitprovisoiremonnaie = "";
    private String idClasseCompte = "";
    private String idCompte = "";
    private String idDomaine = "";
    private String idExterne = "";
    private String idGenre = "";
    private String idNature = CGExtendedCompte.CS_STANDARD;
    private String idperiodecomptable = "";// to delete not use
    private String idSecteurAVS = "";// to delete not use
    private String libDePlanComptable = "";
    private String libFrPlanComptable = "";
    private String libItPlanComptable = "";
    private String solde = "";
    private String soldemonnaie = "";
    private String soldeprovisoire = "";
    private String soldeprovisoiremonnaie = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CGSolde.TABLE_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCompte = statement.dbReadNumeric("IDCOMPTE");
        idClasseCompte = statement.dbReadString("IDCLASSECOMPTE");
        idExterne = statement.dbReadString("IDEXTERNE");
        idGenre = statement.dbReadString("IDGENRE");
        idDomaine = statement.dbReadString("IDDOMAINE");
        idNature = statement.dbReadString("IDNATURE");
        idSecteurAVS = statement.dbReadString("IDSECTEURAVS");
        codeISOMonnaie = statement.dbReadString("CODEISOMONNAIE");
        avoir = statement.dbReadString("AVOIR");
        avoirprovisoire = statement.dbReadString("AVOIRPROVISOIRE");
        doit = statement.dbReadString("DOIT");
        doitprovisoire = statement.dbReadString("DOITPROVISOIRE");
        avoirmonnaie = statement.dbReadString("AVOIRMONNAIE");
        avoirprovisoiremonnaie = statement.dbReadString("AVOIRPROVISOIREMONNAIE");
        doitmonnaie = statement.dbReadString("DOITMONNAIE");
        doitprovisoiremonnaie = statement.dbReadString("DOITPROVISOIREMONNAIE");
        libFrPlanComptable = statement.dbReadString("LIBFRPLANCOMPTABLE");
        libDePlanComptable = statement.dbReadString("LIBDEPLANCOMPTABLE");
        libItPlanComptable = statement.dbReadString("LIBITPLANCOMPTABLE");
        idperiodecomptable = statement.dbReadString("IDPERIODECOMPTABLE");
        solde = statement.dbReadString("SOLDE");
        soldeprovisoire = statement.dbReadString("SOLDEPROVISOIRE");
        soldemonnaie = statement.dbReadString("SOLDEMONNAIE");
        soldeprovisoiremonnaie = statement.dbReadString("SOLDEPROVISOIREMONNAIE");
        budget = statement.dbReadString("BUDGET");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Not used !
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Not used !
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Not used !
    }

    /**
     * @return the avoir
     */
    public String getAvoir() {
        return avoir;
    }

    /**
     * @return the avoirmonnaie
     */
    public String getAvoirmonnaie() {
        return avoirmonnaie;
    }

    /**
     * @return the avoirprovisoire
     */
    public String getAvoirprovisoire() {
        return avoirprovisoire;
    }

    /**
     * @return the avoirprovisoiremonnaie
     */
    public String getAvoirprovisoiremonnaie() {
        return avoirprovisoiremonnaie;
    }

    /**
     * @return the budget
     */
    public String getBudget() {
        return budget;
    }

    /**
     * @return the codeISOMonnaie
     */
    public String getCodeISOMonnaie() {
        return codeISOMonnaie;
    }

    /**
     * @return the doit
     */
    public String getDoit() {
        return doit;
    }

    /**
     * @return the doitmonnaie
     */
    public String getDoitmonnaie() {
        return doitmonnaie;
    }

    /**
     * @return the doitprovisoire
     */
    public String getDoitprovisoire() {
        return doitprovisoire;
    }

    /**
     * @return the doitprovisoiremonnaie
     */
    public String getDoitprovisoiremonnaie() {
        return doitprovisoiremonnaie;
    }

    /**
     * @return the idClasseCompte
     */
    public String getIdClasseCompte() {
        return idClasseCompte;
    }

    /**
     * @return the idCompte
     */
    public String getIdCompte() {
        return idCompte;
    }

    /**
     * @return the idDomaine
     */
    public String getIdDomaine() {
        return idDomaine;
    }

    /**
     * @return the idExterne
     */
    public String getIdExterne() {
        return idExterne;
    }

    /**
     * @return the idGenre
     */
    public String getIdGenre() {
        return idGenre;
    }

    /**
     * @return the idNature
     */
    public String getIdNature() {
        return idNature;
    }

    /**
     * @return the idperiodecomptable
     */
    public String getIdperiodecomptable() {
        return idperiodecomptable;
    }

    /**
     * @return the idSecteurAVS
     */
    public String getIdSecteurAVS() {
        return idSecteurAVS;
    }

    /**
     * @return the libDePlanComptable
     */
    public String getLibDePlanComptable() {
        return libDePlanComptable;
    }

    /**
     * @return the libFrPlanComptable
     */
    public String getLibFrPlanComptable() {
        return libFrPlanComptable;
    }

    /**
     * @return the libItPlanComptable
     */
    public String getLibItPlanComptable() {
        return libItPlanComptable;
    }

    /**
     * @return the solde
     */
    public String getSolde() {
        return solde;
    }

    /**
     * @return the soldemonnaie
     */
    public String getSoldemonnaie() {
        return soldemonnaie;
    }

    /**
     * @return the soldeprovisoire
     */
    public String getSoldeprovisoire() {
        return soldeprovisoire;
    }

    /**
     * @return the soldeprovisoiremonnaie
     */
    public String getSoldeprovisoiremonnaie() {
        return soldeprovisoiremonnaie;
    }

    /**
     * @param avoir
     *            the avoir to set
     */
    public void setAvoir(String avoir) {
        this.avoir = avoir;
    }

    /**
     * @param avoirmonnaie
     *            the avoirmonnaie to set
     */
    public void setAvoirmonnaie(String avoirmonnaie) {
        this.avoirmonnaie = avoirmonnaie;
    }

    /**
     * @param avoirprovisoire
     *            the avoirprovisoire to set
     */
    public void setAvoirprovisoire(String avoirprovisoire) {
        this.avoirprovisoire = avoirprovisoire;
    }

    /**
     * @param avoirprovisoiremonnaie
     *            the avoirprovisoiremonnaie to set
     */
    public void setAvoirprovisoiremonnaie(String avoirprovisoiremonnaie) {
        this.avoirprovisoiremonnaie = avoirprovisoiremonnaie;
    }

    /**
     * @param budget
     *            the budget to set
     */
    public void setBudget(String budget) {
        this.budget = budget;
    }

    /**
     * @param codeISOMonnaie
     *            the codeISOMonnaie to set
     */
    public void setCodeISOMonnaie(String codeISOMonnaie) {
        this.codeISOMonnaie = codeISOMonnaie;
    }

    /**
     * @param doit
     *            the doit to set
     */
    public void setDoit(String doit) {
        this.doit = doit;
    }

    /**
     * @param doitmonnaie
     *            the doitmonnaie to set
     */
    public void setDoitmonnaie(String doitmonnaie) {
        this.doitmonnaie = doitmonnaie;
    }

    /**
     * @param doitprovisoire
     *            the doitprovisoire to set
     */
    public void setDoitprovisoire(String doitprovisoire) {
        this.doitprovisoire = doitprovisoire;
    }

    /**
     * @param doitprovisoiremonnaie
     *            the doitprovisoiremonnaie to set
     */
    public void setDoitprovisoiremonnaie(String doitprovisoiremonnaie) {
        this.doitprovisoiremonnaie = doitprovisoiremonnaie;
    }

    /**
     * @param idClasseCompte
     *            the idClasseCompte to set
     */
    public void setIdClasseCompte(String idClasseCompte) {
        this.idClasseCompte = idClasseCompte;
    }

    /**
     * @param idCompte
     *            the idCompte to set
     */
    public void setIdCompte(String idCompte) {
        this.idCompte = idCompte;
    }

    /**
     * @param idDomaine
     *            the idDomaine to set
     */
    public void setIdDomaine(String idDomaine) {
        this.idDomaine = idDomaine;
    }

    /**
     * @param idExterne
     *            the idExterne to set
     */
    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    /**
     * @param idGenre
     *            the idGenre to set
     */
    public void setIdGenre(String idGenre) {
        this.idGenre = idGenre;
    }

    /**
     * @param idNature
     *            the idNature to set
     */
    public void setIdNature(String idNature) {
        this.idNature = idNature;
    }

    /**
     * @param idperiodecomptable
     *            the idperiodecomptable to set
     */
    public void setIdperiodecomptable(String idperiodecomptable) {
        this.idperiodecomptable = idperiodecomptable;
    }

    /**
     * @param idSecteurAVS
     *            the idSecteurAVS to set
     */
    public void setIdSecteurAVS(String idSecteurAVS) {
        this.idSecteurAVS = idSecteurAVS;
    }

    /**
     * @param libDePlanComptable
     *            the libDePlanComptable to set
     */
    public void setLibDePlanComptable(String libDePlanComptable) {
        this.libDePlanComptable = libDePlanComptable;
    }

    /**
     * @param libFrPlanComptable
     *            the libFrPlanComptable to set
     */
    public void setLibFrPlanComptable(String libFrPlanComptable) {
        this.libFrPlanComptable = libFrPlanComptable;
    }

    /**
     * @param libItPlanComptable
     *            the libItPlanComptable to set
     */
    public void setLibItPlanComptable(String libItPlanComptable) {
        this.libItPlanComptable = libItPlanComptable;
    }

    /**
     * @param solde
     *            the solde to set
     */
    public void setSolde(String solde) {
        this.solde = solde;
    }

    /**
     * @param soldemonnaie
     *            the soldemonnaie to set
     */
    public void setSoldemonnaie(String soldemonnaie) {
        this.soldemonnaie = soldemonnaie;
    }

    /**
     * @param soldeprovisoire
     *            the soldeprovisoire to set
     */
    public void setSoldeprovisoire(String soldeprovisoire) {
        this.soldeprovisoire = soldeprovisoire;
    }

    /**
     * @param soldeprovisoiremonnaie
     *            the soldeprovisoiremonnaie to set
     */
    public void setSoldeprovisoiremonnaie(String soldeprovisoiremonnaie) {
        this.soldeprovisoiremonnaie = soldeprovisoiremonnaie;
    }

}
