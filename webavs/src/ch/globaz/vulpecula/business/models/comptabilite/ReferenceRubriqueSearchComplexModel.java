package ch.globaz.vulpecula.business.models.comptabilite;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import ch.globaz.vulpecula.util.I18NUtil;

public class ReferenceRubriqueSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 6266413860730754345L;

    private String forId;
    private String forLangue = I18NUtil.getOneLetterLang();
    private String forIdRubrique;
    private String forReferenceRubrique;
    private String forLangueNull;
    private String forPlageSuperieure;
    private String forPlageInferieure;
    private String likeIdExterne;
    private String likeLibelle;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForLangue() {
        return forLangue;
    }

    public void setForLangue(String forLangue) {
        this.forLangue = forLangue;
    }

    public String getForPlageSuperieure() {
        return forPlageSuperieure;
    }

    public void setForPlageSuperieure(String forPlageSuperieure) {
        this.forPlageSuperieure = forPlageSuperieure;
    }

    public String getForPlageInferieure() {
        return forPlageInferieure;
    }

    public void setForPlageInferieure(String forPlageInferieure) {
        this.forPlageInferieure = forPlageInferieure;
    }

    public void setForPlage(String plageInferieure, String plageSuperieure) {
        forPlageInferieure = plageInferieure;
        forPlageSuperieure = plageSuperieure;
    }

    public String getLikeIdExterne() {
        if (!JadeStringUtil.isEmpty(likeIdExterne)) {
            return "%" + likeIdExterne;
        }
        return null;
    }

    public void setLikeIdExterne(String likeIdExterne) {
        this.likeIdExterne = likeIdExterne;
    }

    public String getLikeLibelle() {
        if (!JadeStringUtil.isEmpty(likeLibelle)) {
            return "%" + likeLibelle;
        }
        return null;
    }

    public void setLikeLibelle(String likeLibelle) {
        this.likeLibelle = likeLibelle;
    }

    public String getForLangueNull() {
        return forLangueNull;
    }

    public void setForLangueNull() {
        forLangueNull = "null";
    }

    public String getForIdRubrique() {
        return forIdRubrique;
    }

    public void setForIdRubrique(String forIdRubrique) {
        this.forIdRubrique = forIdRubrique;
    }

    public String getForReferenceRubrique() {
        return forReferenceRubrique;
    }

    public void setForReferenceRubrique(String forReferenceRubrique) {
        this.forReferenceRubrique = forReferenceRubrique;
    }

    @Override
    public Class<ReferenceRubriqueComplexModel> whichModelClass() {
        return ReferenceRubriqueComplexModel.class;
    }
}
