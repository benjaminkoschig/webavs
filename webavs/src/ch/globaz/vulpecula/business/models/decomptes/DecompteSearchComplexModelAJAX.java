package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;

public class DecompteSearchComplexModelAJAX extends JadeSearchComplexModel {
    private static final long serialVersionUID = -6641472392212694779L;

    private String forId;
    private String forNoDecompte;
    private String forIdEmployeur;
    private String forIdConvention;
    private String likeNoAffilie;
    private String forDateDe;
    private String forDateAu;
    private String likeRaisonSociale;
    private String likeRaisonSocialeUpper;
    private String forType;
    private String forEtat;
    private Collection<String> inEtats;
    private Collection<String> inTypes;
    private String forIdPassage;
    private Boolean wantRectifie;
    private String beforeDate;
    private String forDateReception;
    private String forEtatTaxation;

    public String getForDateReception() {
        return forDateReception;
    }

    public void setForDateReception(String forDateReception) {
        this.forDateReception = forDateReception;
    }

    public Boolean getWantRectifie() {
        return wantRectifie;
    }

    public void setWantRectifie(Boolean wantRectifie) {
        this.wantRectifie = wantRectifie;
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    public void setForIdPassage(final String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    public DecompteSearchComplexModelAJAX() {
        inEtats = new ArrayList<String>();
        inTypes = new ArrayList<String>();
    }

    public String getForDateAu() {
        return forDateAu;
    }

    public String getForDateDe() {
        return forDateDe;
    }

    public String getForEtat() {
        return forEtat;
    }

    public String getForId() {
        return forId;
    }

    public String getForIdConvention() {
        return forIdConvention;
    }

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    public String getForNoDecompte() {
        return forNoDecompte;
    }

    public String getForType() {
        return forType;
    }

    public Collection<String> getInEtats() {
        return inEtats;
    }

    public Collection<String> getInTypes() {
        return inTypes;
    }

    public String getLikeNoAffilie() {
        return likeNoAffilie;
    }

    public String getLikeRaisonSociale() {
        return likeRaisonSociale;
    }

    public String getLikeRaisonSocialeUpper() {
        return likeRaisonSocialeUpper;
    }

    public void setLikeRaisonSocialeUpper(String likeRaisonSocialeUpper) {
        this.likeRaisonSocialeUpper = JadeStringUtil.toUpperCase(likeRaisonSocialeUpper);
    }

    public void setForDateAu(final String forDateAu) {
        this.forDateAu = forDateAu;
    }

    public void setForDateDe(final String forDateDe) {
        this.forDateDe = forDateDe;
    }

    public void setForEtat(final String forEtat) {
        this.forEtat = forEtat;
    }

    public void setForId(final String forId) {
        this.forId = forId;
    }

    public void setForIdConvention(final String forIdConvention) {
        this.forIdConvention = forIdConvention;
    }

    public void setForIdEmployeur(final String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    public void setForNoDecompte(final String forNoDecompte) {
        this.forNoDecompte = forNoDecompte;
    }

    public void setForType(final String forType) {
        this.forType = forType;
    }

    /**
     * @param etats
     *            the EtatDecompte to set. if none are provided, then this search criteria is discarded.
     */
    public void setInEtats(final EtatDecompte... etats) {
        if (etats.length == 0) {
            inEtats = null;
        } else {
            inEtats = new ArrayList<String>();
            for (EtatDecompte etatDecompte : etats) {
                inEtats.add(etatDecompte.getValue());
            }
        }
    }

    /**
     * @param etats
     *            a String representing one or more (comma-separated) EtatDecompte to set. if none are provided, then
     *            this search criteria is discarded.
     */
    public void setInEtats(final String etats) {
        if (etats.length() == 0) {
            inEtats = null;
            return;
        }
        List<EtatDecompte> etatsDecompte = new ArrayList<EtatDecompte>();
        if (etats.contains(",")) {
            for (String etat : etats.split(",")) {
                etatsDecompte.add(EtatDecompte.fromValue(etat));
            }
        } else {
            etatsDecompte.add(EtatDecompte.fromValue(etats));
        }
        EtatDecompte[] etatsDecompteArray = new EtatDecompte[etatsDecompte.size()];
        setInEtats(etatsDecompte.toArray(etatsDecompteArray));
    }

    /**
     * Rajoute un critère de recherche sur le type de décompte, qui doit être contenu dans la liste passée en paramètre.
     * 
     * @param types les types de décomptes que l'on recherche
     */
    public void setInTypes(TypeDecompte... types) {
        if (types.length == 0) {
            inTypes = null;
        } else {
            inTypes = new ArrayList<String>();
            for (TypeDecompte typeDecompte : types) {
                inTypes.add(typeDecompte.getValue());
            }
        }
    }

    public void setLikeNoAffilie(final String likeNoAffilie) {
        this.likeNoAffilie = likeNoAffilie;
    }

    public void setLikeRaisonSociale(final String likeRaisonSociale) {
        this.likeRaisonSociale = likeRaisonSociale;
    }

    public String getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(String beforeDate) {
        this.beforeDate = beforeDate;
    }

    public void setBeforeDate(Date beforeDate) {
        this.beforeDate = beforeDate.getValue();
    }

    public String getForEtatTaxation() {
        return forEtatTaxation;
    }

    public void setForEtatTaxation(String forEtatTaxation) {
        this.forEtatTaxation = forEtatTaxation;
    }

    @Override
    public Class<DecompteComplexModelAJAX> whichModelClass() {
        return DecompteComplexModelAJAX.class;
    }
}
