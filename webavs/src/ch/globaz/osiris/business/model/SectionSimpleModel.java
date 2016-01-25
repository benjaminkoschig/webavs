package ch.globaz.osiris.business.model;

import globaz.globall.db.BConstants;
import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author SCO
 */
public class SectionSimpleModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String _nonImprimable;
    private String dateSection;
    private String idCompteAnnexe;
    private String idExterne;
    private String idJournal;
    private String idSection;
    private String idTypeSection;
    private String solde;

    /*
     * Ne pas utiliser
     */
    public String get_nonImprimable() {
        return _nonImprimable;
    }

    public String getDateSection() {
        return dateSection;
    }

    @Override
    public String getId() {
        return getIdSection();
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdTypeSection() {
        return idTypeSection;
    }

    public Boolean getNonImprimable() {
        return new Boolean(BConstants.DB_BOOLEAN_CHAR_TRUE.equals(_nonImprimable));
    }

    public String getSolde() {
        return solde;
    }

    public void set_nonImprimable(String nonImprimable) {
        _nonImprimable = nonImprimable;
    }

    public void setDateSection(String dateSection) {
        this.dateSection = dateSection;
    }

    @Override
    public void setId(String id) {
        setIdSection(id);
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdTypeSection(String idTypeSection) {
        this.idTypeSection = idTypeSection;
    }

    public void setNonImprimable(Boolean nonImprimable) {
        if (nonImprimable.booleanValue()) {
            _nonImprimable = BConstants.DB_BOOLEAN_CHAR_TRUE.toString();
        } else {
            _nonImprimable = BConstants.DB_BOOLEAN_CHAR_FALSE.toString();
        }
    }

    public void setSolde(String solde) {
        this.solde = solde;
    }

}
