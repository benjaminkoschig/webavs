package globaz.vulpecula.vb.decomptesalaire;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.domain.models.common.Date;

public class PTAnnoncerMyProdisViewBean extends BJadeSearchObjectELViewBean {
    public enum GenreAnnonce {
        THEORIQUES_ANNUEL,
        THEORIQUES_MENSUEL,
        COTISANTS
    };

    private int anneeAnnuel;
    private int anneeMensuel;
    private int moisMensuel;
    private GenreAnnonce genreAnnonce;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String arg0) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public final int getAnneeAnnuel() {
        if (JadeNumericUtil.isEmptyOrZero(String.valueOf(anneeAnnuel))) {
            return Date.getCurrentYear();
        }
        return anneeAnnuel;
    }

    public final void setAnneeAnnuel(int anneeAnnuel) {
        this.anneeAnnuel = anneeAnnuel;
    }

    public final int getAnneeMensuel() {
        if (JadeNumericUtil.isEmptyOrZero(String.valueOf(anneeMensuel))) {
            return Date.getCurrentYear();
        }
        return anneeMensuel;
    }

    public final void setAnneeMensuel(int anneeMensuel) {
        this.anneeMensuel = anneeMensuel;
    }

    public final int getMoisMensuel() {
        if (JadeNumericUtil.isEmptyOrZero(String.valueOf(moisMensuel))) {
            return Date.getCurrentMonth();
        }
        return moisMensuel;
    }

    public final void setMoisMensuel(int moisMensuel) {
        this.moisMensuel = moisMensuel;
    }

    public final GenreAnnonce getGenreAnnonce() {
        return genreAnnonce;
    }

    public final void setGenreAnnonce(GenreAnnonce genreAnnonce) {
        this.genreAnnonce = genreAnnonce;
    }

    public final void setGenreAnnonce(String genreAnnonceStringValue) {
        for (GenreAnnonce enumItem : GenreAnnonce.values()) {
            if (genreAnnonceStringValue.equals(enumItem.toString())) {
                genreAnnonce = enumItem;
            }
        }
    }

    public String getEmail() {
        return BSessionUtil.getSessionFromThreadContext().getUserEMail();
    }

    public String getGenreAnnonceTheoriquesAnnuel() {
        return GenreAnnonce.THEORIQUES_ANNUEL.toString();
    }

    public String getGenreAnnonceTheoriquesMensuel() {
        return GenreAnnonce.THEORIQUES_MENSUEL.toString();
    }

    public String getGenreAnnonceCotisants() {
        return GenreAnnonce.COTISANTS.toString();
    }

    public String getMessageAnneeObligatoire() {
        return JadeStringUtil.escapeXML(BSessionUtil.getSessionFromThreadContext().getLabel("JSP_ANNEE_NON_SAISIE"));
    }

    public String getMessageMoisObligatoire() {
        return JadeStringUtil.escapeXML(BSessionUtil.getSessionFromThreadContext().getLabel("JSP_MOIS_NON_SAISI"));
    }

}
