package ch.globaz.vulpecula.business.models.taxationoffice;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;

public class TaxationOfficeSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 1L;

    private String forId;
    private Collection<String> forIds;
    private String forIdPassageFacturation;
    private String forIdDecompte;
    private Collection<String> forEtatIn;
    private Collection<String> forEtatNotIn;
    private String likeNoAffilie;
    private String forIdEmployeur;
    private String forDateDe;
    private String forDateAu;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdPassageFacturation() {
        return forIdPassageFacturation;
    }

    public void setForIdPassageFacturation(String forIdPassageFacturation) {
        this.forIdPassageFacturation = forIdPassageFacturation;
    }

    public String getForIdDecompte() {
        return forIdDecompte;
    }

    public void setForIdDecompte(String forIdDecompte) {
        this.forIdDecompte = forIdDecompte;
    }

    public Collection<String> getForEtatIn() {
        return forEtatIn;
    }

    public void setForEtatIn(List<EtatTaxation> etats) {
        Collection<String> csEtats = new ArrayList<String>();
        for (EtatTaxation etat : etats) {
            csEtats.add(etat.getValue());
        }
        forEtatIn = csEtats;
    }

    public Collection<String> getForIds() {
        return forIds;
    }

    public void setForIds(Collection<String> forIds) {
        this.forIds = forIds;
    }

    public void setForEtatIn(EtatTaxation... etats) {
        setForEtatIn(Arrays.asList(etats));
    }

    public String getLikeNoAffilie() {
        return likeNoAffilie;
    }

    public void setLikeNoAffilie(String likeNoAffilie) {
        this.likeNoAffilie = likeNoAffilie;
    }

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    public void setForIdEmployeur(String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    public Collection<String> getForEtatNotIn() {
        return forEtatNotIn;
    }

    public void setForEtatNotIn(EtatTaxation... etats) {
        Collection<String> forEtatNotIn = new ArrayList<String>();
        for (EtatTaxation etat : etats) {
            forEtatNotIn.add(etat.getValue());
        }
        this.forEtatNotIn = forEtatNotIn;
    }

    public void setForEtatNotIn(Collection<String> forEtatNotIn) {
        this.forEtatNotIn = forEtatNotIn;
    }

    public void setForEtatIn(Collection<String> forEtatIn) {
        this.forEtatIn = forEtatIn;
    }

    public void setForDateContenueDansPeriode(Date date) {
        setForDateDe(date.getAnneeMois());
        setForDateAu(date.getAnneeMois());
    }

    public void setForDateDe(Date date) {
        setForDateDe(date.getAnneeMois());
    }

    public String getForDateDe() {
        return forDateDe;
    }

    public void setForDateDe(String forDateDe) {
        this.forDateDe = forDateDe;
    }

    public String getForDateAu() {
        return forDateAu;
    }

    public void setForDateAu(String forDateAu) {
        this.forDateAu = forDateAu;
    }

    @Override
    public Class<TaxationOfficeComplexModel> whichModelClass() {
        return TaxationOfficeComplexModel.class;
    }
}
