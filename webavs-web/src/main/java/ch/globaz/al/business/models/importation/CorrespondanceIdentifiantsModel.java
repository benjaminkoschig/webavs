package ch.globaz.al.business.models.importation;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle de correspondance entre les enregistrements des tables Alfa-Gest et WebAF.
 * 
 * @author jts
 */
public class CorrespondanceIdentifiantsModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * ID de l'enregistrement dans ALFA-Gest
     */
    private String idAlfagest = null;
    /**
     * Identifiant de la correspondance (clé primaire)
     */
    private String idCorrespondance = null;
    /**
     * ID de l'enregistrement dans WebAF
     */
    private String idWebaf = null;
    /**
     * Nom de la table dans ALFA-Gest
     */
    private String tableAlfagest = null;
    /**
     * Nom de la table dans WebAF
     */
    private String tableWebaf = null;

    @Override
    public String getId() {
        return idCorrespondance;
    }

    /**
     * @return the idAlfagest
     */
    public String getIdAlfagest() {
        return idAlfagest;
    }

    /**
     * @return the idCorrespondance
     */
    public String getIdCorrespondance() {
        return idCorrespondance;
    }

    /**
     * @return the idWebaf
     */
    public String getIdWebaf() {
        return idWebaf;
    }

    /**
     * @return the tableAlfagest
     */
    public String getTableAlfagest() {
        return tableAlfagest;
    }

    /**
     * @return the tableWebaf
     */
    public String getTableWebaf() {
        return tableWebaf;
    }

    @Override
    public void setId(String id) {
        idCorrespondance = id;
    }

    /**
     * @param idAlfagest
     *            the idAlfagest to set
     */
    public void setIdAlfagest(String idAlfagest) {
        this.idAlfagest = idAlfagest;
    }

    /**
     * @param idCorrespondance
     *            the idCorrespondance to set
     */
    public void setIdCorrespondance(String idCorrespondance) {
        this.idCorrespondance = idCorrespondance;
    }

    /**
     * @param idWebaf
     *            the idWebaf to set
     */
    public void setIdWebaf(String idWebaf) {
        this.idWebaf = idWebaf;
    }

    /**
     * @param tableAlfagest
     *            the tableAlfagest to set
     */
    public void setTableAlfagest(String tableAlfagest) {
        this.tableAlfagest = tableAlfagest;
    }

    /**
     * @param tableWebaf
     *            the tableWebaf to set
     */
    public void setTableWebaf(String tableWebaf) {
        this.tableWebaf = tableWebaf;
    }

}
