/*
 * Créé le 19 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.vb.famille;

import globaz.globall.db.BSpy;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author scr
 * 
 */
public class SFVueGlobaleViewBean extends PRAbstractViewBeanSupport {

    // Taille de la fonte utilisé pour afficher la timeline, en pixel.
    public static final int FONT_SIZE_PIXEL = 12;

    // Taille minimim alloué pour afficher une relation (en carartère)
    public static final int MIN_RELATION_SIZE = 12;
    private List conjointsDesConjointsDuLiant = new ArrayList();

    private List conjointsDuLiant = new ArrayList();

    private Boolean isDisplayPeriode = Boolean.FALSE;

    private SFLiantVO liant = null;

    private List<String> listNssMembresFamille = new ArrayList<String>();

    private JADate maxDateFinRelations = null;
    private JADate minDateDebutRelations = null;

    private int nombreRelation = 0;

    private SFMembreVO parent1Liant = null;
    private SFMembreVO parent2Liant = null;

    private BSpy spy;

    public void addConjointDesConjointsDuLiant(SFConjointVO conjoint) {
        if (conjointsDesConjointsDuLiant == null) {
            conjointsDesConjointsDuLiant = new ArrayList();
        }
        conjointsDesConjointsDuLiant.add(conjoint);
    }

    public void addConjointDuLiant(SFConjointVO elm) {
        if (conjointsDuLiant == null) {
            conjointsDuLiant = new ArrayList();
        }

        conjointsDuLiant.add(elm);
    }

    public void addNssMembreFamille(String nss) {

        if (JadeStringUtil.isBlankOrZero(nss)) {
            return;
        }

        if (!listNssMembresFamille.contains(nss)) {
            listNssMembresFamille.add(nss);
        }
    }

    public void clearListNssMembreFamille() {
        if (listNssMembresFamille != null) {
            listNssMembresFamille.clear();
        }

    }

    public List getConjointsDesConjointsDuLiant() {
        return conjointsDesConjointsDuLiant;
    }

    public List getConjointsDuLiant() {
        return conjointsDuLiant;
    }

    public Boolean getIsDisplayPeriode() {
        return isDisplayPeriode;
    }

    public SFLiantVO getLiant() {
        return liant;
    }

    public List<String> getListNssMembresFamille() {
        return listNssMembresFamille;
    }

    public JADate getMaxDateFinRelations() {
        return maxDateFinRelations;
    }

    public JADate getMinDateDebutRelations() {
        return minDateDebutRelations;
    }

    public int getNombreRelation() {
        return nombreRelation;
    }

    public SFMembreVO getParent1Liant() {
        return parent1Liant;
    }

    public SFMembreVO getParent2Liant() {
        return parent2Liant;
    }

    @Override
    public BSpy getSpy() {
        return spy;
    }

    /**
     * 
     * @return la taille du tableau, en pixel pour afficher la timeline.
     */
    public int getWidth() {

        int r = SFVueGlobaleViewBean.FONT_SIZE_PIXEL * SFVueGlobaleViewBean.MIN_RELATION_SIZE;
        r = r * nombreRelation;
        if (r < 920) {
            r = 920;
        }
        return r;
    }

    public void initTimeLines() throws Exception {

        int maxVisibleChar = 0;
        // On initialise les timelines.

        // 1ère passe
        for (Iterator iterator = conjointsDuLiant.iterator(); iterator.hasNext();) {
            SFConjointVO conjoint = (SFConjointVO) iterator.next();
            int tmp = 0;
            tmp = conjoint.initTimeLine(minDateDebutRelations, maxDateFinRelations, getWidth());
            if (maxVisibleChar < tmp) {
                maxVisibleChar = tmp;
            }
        }

        // Idem avec les conjoints des conjoints...
        for (Iterator iterator = conjointsDesConjointsDuLiant.iterator(); iterator.hasNext();) {
            SFConjointVO conjoint = (SFConjointVO) iterator.next();
            int tmp = 0;
            tmp = conjoint.initTimeLine(minDateDebutRelations, maxDateFinRelations, getWidth());
            if (maxVisibleChar < tmp) {
                maxVisibleChar = tmp;
            }
        }

        // 2ème passe. Suivant la durée d'une relation, celle-ci peut s'afficher
        // de manière condensée.
        // Lorsque ce cas arrive, la longeur de la timeLine peut différer des
        // autres.
        // Cette 2ème passe va aligner la longeur de toute les timelines sur la
        // plus grande précédemment créée.

        for (Iterator iterator = conjointsDuLiant.iterator(); iterator.hasNext();) {
            SFConjointVO conjoint = (SFConjointVO) iterator.next();
            String htmlTL = conjoint.getHtmlTimeLine();
            for (int i = maxVisibleChar; i > conjoint.getTimeLineVisibleCharSize(); i--) {
                htmlTL += "-";
            }
            htmlTL += "></span>";
            conjoint.setHtmlTimeLine(htmlTL);
        }

        // Idem avec les conjoints des conjoints
        for (Iterator iterator = conjointsDesConjointsDuLiant.iterator(); iterator.hasNext();) {
            SFConjointVO conjoint = (SFConjointVO) iterator.next();
            String htmlTL = conjoint.getHtmlTimeLine();
            for (int i = maxVisibleChar; i > conjoint.getTimeLineVisibleCharSize(); i--) {
                htmlTL += "-";
            }
            htmlTL += "></span>";
            conjoint.setHtmlTimeLine(htmlTL);
        }
    }

    public void setIsDisplayPeriode(Boolean isDisplayPeriode) {
        this.isDisplayPeriode = isDisplayPeriode;
    }

    public void setLiant(SFLiantVO liant) {
        this.liant = liant;
    }

    public void setMaxDateFinRelations(JADate maxDateFinRelations) {
        this.maxDateFinRelations = maxDateFinRelations;
    }

    public void setMinDateDebutRelations(JADate minDateDebutRelations) {
        this.minDateDebutRelations = minDateDebutRelations;
    }

    public void setNombreRelation(int nombreRelation) {
        this.nombreRelation = nombreRelation;
    }

    public void setParent1Liant(SFMembreVO parent1Liant) {
        this.parent1Liant = parent1Liant;
    }

    public void setParent2Liant(SFMembreVO parent2Liant) {
        this.parent2Liant = parent2Liant;
    }

    public void setSpy(BSpy spy) {
        this.spy = spy;
    }

    @Override
    public boolean validate() {
        // TODO Auto-generated method stub
        return false;
    }

}
