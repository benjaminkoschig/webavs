package globaz.aquila.db.access.batch;

import globaz.aquila.common.COBEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CARubrique;
import java.util.SortedMap;
import java.util.TreeMap;

public class COParamTaxes extends COBEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param session
     * @param selectedId
     * @param tous
     * @return
     * @throws Exception
     */
    public static final String createEtapeTags(BSession session, String selectedId, boolean tous) throws Exception {
        SortedMap etapes = getAllEtapes(session);
        StringBuffer options = new StringBuffer();

        if (etapes.isEmpty()) {
            if (tous) {
                options.append("<option value=\"-1\">");
                options.append(session.getLabel("TOUS"));
                options.append("</option>");
            }
        } else {
            for (java.util.Iterator etapeIter = etapes.values().iterator(); etapeIter.hasNext();) {
                COEtape etape = (COEtape) etapeIter.next();

                if (etape.getIdEtape().equals(selectedId)) {
                    options.append("<option selected value=\"");
                    options.append(etape.getLibEtape());
                    options.append("\"");

                    options.append(">");
                    options.append(etape.getLibEtapeLibelle());
                    options.append("</options>");
                } else {
                    options.append("<option value=\"");
                    options.append(etape.getLibEtape());
                    options.append("\"");

                    options.append(">");
                    options.append(etape.getLibEtapeLibelle());
                    options.append("</options>");
                }

                // ce role

                // tous les roles
                // if (tousEtape.length() > 0) {
                // tousEtape.append(",");
                // }

                // tousEtape.append(etape.getIdEtape());
            }

            if (tous) {
                options.insert(0, "<option value=\"" + "" + "\">" + session.getLabel("TOUS") + "</option>");
            }
        }

        return options.toString();
    }

    /**
     * @param session
     * @return
     * @throws Exception
     */
    public static final SortedMap getAllEtapes(BSession session) throws Exception {
        SortedMap retValue = new TreeMap();
        COEtapeManager mgr = new COEtapeManager();
        mgr.setOrderByLibEtapeCSOrder("true");
        mgr.setSession(session);
        mgr.find();
        for (int id = 0; id < mgr.size(); id++) {
            COEtape etape = (COEtape) mgr.get(id);
            retValue.put(new Integer(id), etape);
        }
        return retValue;
    }

    private String baseTaxe = "";
    private String etape = "";
    // Uniquement utilisé pour la lecture
    private String idEtape = "";
    private String idRubrique = "";
    private String idSequence = "";
    private String idTaxe = "";
    private String idTraduction = "";
    private Boolean imputerTaxes = new Boolean(false);
    private String libelle = "";
    private String libelleDE = "";
    private String libelleFR = "";

    private String libelleIT = "";
    private String montantFixe = "";
    private CARubrique rubrique;

    private String typeTaxe = "";

    private String typeTaxeEtape = "";

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idTaxe = statement.dbReadNumeric(COParamTaxesManager.FIELD_OIICTX);
        idEtape = statement.dbReadNumeric(COParamTaxesManager.FIELD_ODIETA);
        imputerTaxes = statement.dbReadBoolean(COParamTaxesManager.FIELD_OHBIMP);
        idRubrique = statement.dbReadNumeric(COParamTaxesManager.FIELD_OIIRUB);
        typeTaxe = statement.dbReadNumeric(COParamTaxesManager.FIELD_OITTYP);
        montantFixe = statement.dbReadNumeric(COParamTaxesManager.FIELD_OIMFIX);
        baseTaxe = statement.dbReadNumeric(COParamTaxesManager.FIELD_OITBTX);
        idTraduction = statement.dbReadNumeric(COParamTaxesManager.FIELD_OIITRA);
        typeTaxeEtape = statement.dbReadNumeric(COParamTaxesManager.FIELD_OITTTE);
        etape = statement.dbReadNumeric(COParamTaxesManager.FIELD_ODTETA);
        libelle = statement.dbReadString(COParamTaxesManager.FIELD_LIBELLE);
        idSequence = statement.dbReadNumeric(COParamTaxesManager.FIELD_OFISEQ);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getBaseTaxe() {
        return baseTaxe;
    }

    public String getEtape() {
        return etape;
    }

    public String getIdCompte() {
        return getIdRubrique();
    }

    public String getIdEtape() {
        return idEtape;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public String getIdSequence() {
        return idSequence;
    }

    public String getIdTaxe() {
        return idTaxe;
    }

    public String getIdTraduction() {
        return idTraduction;
    }

    public Boolean getImputerTaxes() {
        return imputerTaxes;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getLibelleDE() {
        return libelleDE;
    }

    public String getLibelleFR() {
        return libelleFR;
    }

    public String getLibelleIT() {
        return libelleIT;
    }

    public String getMontantFixe() {
        return montantFixe;
    }

    /**
     * @return
     */
    public CARubrique getRubriqueEntity() {
        if (JadeStringUtil.isIntegerEmpty(getIdRubrique())) {
            return null;
        }
        if (rubrique == null) {
            rubrique = new CARubrique();
            rubrique.setISession(getSession());
            rubrique.setIdRubrique(getIdRubrique());
            try {
                rubrique.retrieve();
                if (rubrique.isNew()) {
                    rubrique = null;
                }
            } catch (Exception e) {
                rubrique = null;
            }
        }

        return rubrique;
    }

    public String getTypeTaxe() {
        return typeTaxe;
    }

    public String getTypeTaxeEtape() {
        return typeTaxeEtape;
    }

    public void setBaseTaxe(String baseTaxe) {
        this.baseTaxe = baseTaxe;
    }

    public void setEtape(String etape) {
        this.etape = etape;
    }

    public void setIdCompte(String idCompte) {
        setIdRubrique(idCompte);
    }

    public void setIdEtape(String idEtape) {
        this.idEtape = idEtape;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public void setIdSequence(String idSequence) {
        this.idSequence = idSequence;
    }

    public void setIdTaxe(String idTaxe) {
        this.idTaxe = idTaxe;
    }

    public void setIdTraduction(String idTraduction) {
        this.idTraduction = idTraduction;
    }

    public void setImputerTaxes(Boolean imputerTaxes) {
        this.imputerTaxes = imputerTaxes;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setLibelleDE(String libelleDE) {
        this.libelleDE = libelleDE;
    }

    public void setLibelleFR(String libelleFR) {
        this.libelleFR = libelleFR;
    }

    public void setLibelleIT(String libelleIT) {
        this.libelleIT = libelleIT;
    }

    public void setMontantFixe(String montantFixe) {
        this.montantFixe = montantFixe;
    }

    public void setTypeTaxe(String typeTaxe) {
        this.typeTaxe = typeTaxe;
    }

    public void setTypeTaxeEtape(String typeTaxeEtape) {
        this.typeTaxeEtape = typeTaxeEtape;
    }

}
