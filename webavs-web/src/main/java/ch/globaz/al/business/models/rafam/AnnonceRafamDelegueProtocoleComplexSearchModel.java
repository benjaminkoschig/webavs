package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

public class AnnonceRafamDelegueProtocoleComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forEtat = null;
    private String forTypeAnnonce = null;
    private Collection<String> inCodeRetour = null;
    private Collection<String> inTypeAnnonce = null;
    private String likeInternalOffice = null;
    private String maxRecordNumber = null;
    private String minRecordNumber = null;

    public String getForEtat() {
        return forEtat;
    }

    public String getForTypeAnnonce() {
        return forTypeAnnonce;
    }

    public Collection<String> getInCodeRetour() {
        return inCodeRetour;
    }

    public Collection<String> getInTypeAnnonce() {
        return inTypeAnnonce;
    }

    public String getLikeInternalOffice() {
        return likeInternalOffice;
    }

    public String getMaxRecordNumber() {
        return maxRecordNumber;
    }

    public String getMinRecordNumber() {
        return minRecordNumber;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public void setForTypeAnnonce(String forTypeAnnonce) {
        this.forTypeAnnonce = forTypeAnnonce;
    }

    public void setInCodeRetour(Collection<String> inCodeRetour) {
        this.inCodeRetour = inCodeRetour;
    }

    public void setInTypeAnnonce(Collection<String> inTypeAnnonce) {
        this.inTypeAnnonce = inTypeAnnonce;
    }

    public void setLikeInternalOffice(String likeInternalOffice) {
        this.likeInternalOffice = likeInternalOffice;
    }

    public void setMaxRecordNumber(String maxRecordNumber) {
        this.maxRecordNumber = maxRecordNumber;
    }

    public void setMinRecordNumber(String minRecordNumber) {
        this.minRecordNumber = minRecordNumber;
    }

    @Override
    public Class<AnnonceRafamDelegueProtocoleComplexModel> whichModelClass() {
        return AnnonceRafamDelegueProtocoleComplexModel.class;
    }
}
