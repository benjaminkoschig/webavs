/*
 * Créé le 10 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.phenix.interfaces.ICommunicationRetour;
import globaz.phenix.translation.CodeSystem;
import globaz.pyxis.api.ITIAdministration;

/**
 * @author mmu
 * 
 *         Entité representant un journal de récéption
 */
public class CPCommunicationPlausibilite extends CPParametrePlausibilite {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idCommunication = "";
    private String idLien = "";
    private String idPlausibilite = "";

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "CPLCRPP";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCommunication = statement.dbReadNumeric("IBIDCF");
        idPlausibilite = statement.dbReadNumeric("IXIDPA");
        idLien = statement.dbReadNumeric("ILCRPP");
        super._readProperties(statement);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getIdCommunication() {
        return idCommunication;
    }

    public String getIdLien() {
        return idLien;
    }

    @Override
    public String getIdPlausibilite() {
        return idPlausibilite;
    }

    public String getLibellePlausibilite(BSession session, ICommunicationRetour retour) {
        String varLibelle = "";
        if (session.getIdLangueISO().equalsIgnoreCase("DE")) {
            varLibelle = getId() + " - " + getDescription_de();
        } else if (session.getIdLangueISO().equalsIgnoreCase("IT")) {
            varLibelle = getId() + " - " + getDescription_it();
        } else {
            varLibelle = getId() + " - " + getDescription_fr();
        }
        if (getNomCle().equalsIgnoreCase("isCaisseExterneAvs")) {
            try {
                ITIAdministration admin = AFAffiliationUtil.getNoCaisseAVS(retour.getAffiliation(), retour.getAnnee1());
                if (admin != null) {
                    varLibelle = varLibelle + " " + admin.getCodeAdministration();
                }
            } catch (Exception e) {
                varLibelle = "";
            }
        }
        if (getNomCle().equalsIgnoreCase("isCCVSChangeGenre")) {
            try {
                if (!JadeStringUtil.isEmpty(retour.getChangementGenre())) {
                    varLibelle = varLibelle + " " + CodeSystem.getLibelle(session, retour.getChangementGenre());
                } else if (!JadeStringUtil.isEmpty(retour.getChangementGenreConjoint())) {
                    varLibelle = varLibelle + " "
                            + CodeSystem.getLibelle(session, retour.getChangementGenreConjoint() + " *");
                }
            } catch (Exception e) {
                varLibelle = "";
            }
        }
        return varLibelle;
    }

    public void setIdCommunication(String idCommunication) {
        this.idCommunication = idCommunication;
    }

    public void setIdLien(String idLien) {
        this.idLien = idLien;
    }

    @Override
    public void setIdPlausibilite(String idPlausibilite) {
        this.idPlausibilite = idPlausibilite;
    }

}
