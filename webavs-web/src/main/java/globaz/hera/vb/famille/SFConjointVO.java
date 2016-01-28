package globaz.hera.vb.famille;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFPeriode;
import globaz.hera.db.famille.SFPeriodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.tools.PRDateFormater;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author SCR
 * 
 */
public class SFConjointVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String startHtmlStyle() {
        return "<span style = \"font-family : Courier New;font-size:"
                + String.valueOf(SFVueGlobaleViewBean.FONT_SIZE_PIXEL) + "px;\">";
    }

    public static String stopHtmlStyle() {
        return "</span>";
    }

    private String csSexe1 = "";
    private String csSexe2 = "";
    private String dateDeces1 = "";
    private String dateDeces2 = "";
    private String descriptionConjoint1 = "";
    private String descriptionConjoint2 = "";
    private List enfants = null;
    private String htmlDateTimeLine = "";
    private String htmlTimeLine = "";

    private String idMembreFamille1 = "";
    private String idMembreFamille2 = "";
    private String idRelationConjoint = "";
    private String idTiers1 = "";

    private String idTiers2 = "";
    private String nss1 = "";

    private String nss2 = "";

    private List periodesConjoint1 = null;

    private List periodesConjoint2 = null;

    private List relations = null;
    private int timeLineVisibleCharSize = 0;

    public void addEnfant(SFMembreVO enfant) {
        if (enfants == null) {
            enfants = new ArrayList();
        }
        enfants.add(enfant);
    }

    public void addPeriodeConjoint1(SFPeriodeVO p) {
        if (periodesConjoint1 == null) {
            periodesConjoint1 = new ArrayList();
        }
        periodesConjoint1.add(p);
    }

    public void addPeriodeConjoint2(SFPeriodeVO p) {
        if (periodesConjoint2 == null) {
            periodesConjoint2 = new ArrayList();
        }
        periodesConjoint2.add(p);
    }

    public void addRelation(SFRelationVO rel) {
        if (relations == null) {
            relations = new ArrayList();
        }
        relations.add(rel);
    }

    public String getCsSexe1() {
        return csSexe1;
    }

    public String getCsSexe2() {
        return csSexe2;
    }

    public String getDateDeces1() {
        return dateDeces1;
    }

    public String getDateDeces2() {
        return dateDeces2;
    }

    public String getDescriptionConjoint1() {
        return descriptionConjoint1;
    }

    public String getDescriptionConjoint2() {
        return descriptionConjoint2;
    }

    public List getEnfants() {
        return enfants;
    }

    public String getHtmlDateTimeLine() {
        return htmlDateTimeLine;
    }

    public String getHtmlTimeLine() {
        return htmlTimeLine;
    }

    public String getIdMembreFamille1() {
        return idMembreFamille1;
    }

    public String getIdMembreFamille2() {
        return idMembreFamille2;
    }

    public String getIdRelationConjoint() {
        return idRelationConjoint;
    }

    public String getIdTiers1() {
        return idTiers1;
    }

    public String getIdTiers2() {
        return idTiers2;
    }

    public String getImgName1() {
        if (PRACORConst.CS_HOMME.equals(csSexe1)) {
            if (JadeStringUtil.isBlankOrZero(dateDeces1)) {
                return "father.png";
            } else {
                return "fatherDead.png";
            }
        } else {
            if (JadeStringUtil.isBlankOrZero(dateDeces1)) {
                return "mother.png";
            } else {
                return "motherDead.png";
            }

        }
    }

    public String getImgName2() {
        if (PRACORConst.CS_HOMME.equals(csSexe2)) {
            if (JadeStringUtil.isBlankOrZero(dateDeces2)) {
                return "father.png";
            } else {
                return "fatherDead.png";
            }
        } else {
            if (JadeStringUtil.isBlankOrZero(dateDeces2)) {
                return "mother.png";
            } else {
                return "motherDead.png";
            }

        }
    }

    /**
     * 
     * 
     * @return une clé unique, composé des ids des 2 membres de famille, ordonné.
     */
    public String getKey() {
        long id1 = Long.parseLong(idMembreFamille1);
        long id2 = Long.parseLong(idMembreFamille2);

        String result = new String();
        if (id1 < id2) {
            result = idMembreFamille1 + "-" + idMembreFamille2;
        } else {
            result = idMembreFamille2 + "-" + idMembreFamille1;
        }
        return result;

    }

    public String getNss1() {
        return nss1;
    }

    public String getNss2() {
        return nss2;
    }

    public List getPeriodesConjoint1() {
        return periodesConjoint1;
    }

    public List getPeriodesConjoint2() {
        return periodesConjoint2;
    }

    public List getRelations() {
        return relations;
    }

    public int getTimeLineVisibleCharSize() {
        return timeLineVisibleCharSize;
    }

    public void initPeriodes(BSession session, BTransaction transaction) throws Exception {
        SFPeriodeManager periodes = new SFPeriodeManager();
        periodes.setSession(session);
        periodes.setForIdMembreFamille(idMembreFamille1);
        periodes.find(transaction);
        for (int i = 0; i < periodes.size(); i++) {
            SFPeriode periode = (SFPeriode) periodes.get(i);
            SFPeriodeVO pvo = periode.toValueObject(session);

            addPeriodeConjoint1(pvo);
        }

        periodes.setForIdMembreFamille(idMembreFamille2);
        periodes.find(transaction);
        for (int i = 0; i < periodes.size(); i++) {
            SFPeriode periode = (SFPeriode) periodes.get(i);
            SFPeriodeVO pvo = periode.toValueObject(session);

            addPeriodeConjoint2(pvo);
        }

        List enfants = getEnfants();
        if (enfants != null) {
            for (int i = 0; i < enfants.size(); i++) {
                SFMembreVO enfant = (SFMembreVO) enfants.get(i);
                periodes = new SFPeriodeManager();
                periodes.setSession(session);
                periodes.setForIdMembreFamille(enfant.getIdMembreFamille());
                periodes.find(transaction);
                for (int j = 0; j < periodes.size(); j++) {
                    SFPeriode periode = (SFPeriode) periodes.get(j);
                    SFPeriodeVO pvo = periode.toValueObject(session);
                    enfant.addPeriode(pvo);
                }
            }
        }
    }

    public int initTimeLine(JADate minDateDebutRelations, JADate maxDateFinRelations, int lineLength) throws Exception {

        htmlTimeLine = SFConjointVO.startHtmlStyle() + "---";
        htmlDateTimeLine = SFConjointVO.startHtmlStyle() + "&nbsp;&nbsp;&nbsp;";
        timeLineVisibleCharSize = 3;

        long nbrJoursTot = 0;
        JACalendar cal = new JACalendarGregorian();
        JADate d = null;
        if (maxDateFinRelations == null) {
            d = JACalendar.today();
        } else {
            d = maxDateFinRelations;
        }
        nbrJoursTot = cal.daysBetween(minDateDebutRelations, d);

        int nbrCaracteresTimeLine = lineLength / SFVueGlobaleViewBean.FONT_SIZE_PIXEL;

        boolean isFirstRelation = true;

        int counter = nbrCaracteresTimeLine;

        for (Iterator iterator = relations.iterator(); iterator.hasNext();) {
            SFRelationVO rel = (SFRelationVO) iterator.next();
            if (ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN.equals(rel.getCsTypeRelation())
                    || ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE.equals(rel.getCsTypeRelation())) {

                htmlTimeLine += rel.getLibelleRelation(SFVueGlobaleViewBean.MIN_RELATION_SIZE + 1);

                int k = rel.getLibelleRelation(SFVueGlobaleViewBean.MIN_RELATION_SIZE + 1).length();
                for (int j = 0; j < k; j++) {
                    htmlDateTimeLine += "&nbsp;";
                }

                timeLineVisibleCharSize += rel.getLibelleRelation(SFVueGlobaleViewBean.MIN_RELATION_SIZE + 1).length();

                for (int i = rel.getLibelleRelation(SFVueGlobaleViewBean.MIN_RELATION_SIZE + 1).length(); i < nbrCaracteresTimeLine; i++) {
                    htmlTimeLine += "-";
                    htmlDateTimeLine += "&nbsp;";
                    timeLineVisibleCharSize++;
                }
            } else {
                JADate d2 = null;
                if (JadeStringUtil.isBlankOrZero(rel.getDateFin())) {
                    d2 = JACalendar.today();
                } else {
                    d2 = new JADate(rel.getDateFin());
                }
                long diffJours = cal.daysBetween(new JADate(rel.getDateDebut()), d2);

                long percentRelation = (diffJours * 100) / nbrJoursTot;
                long sizeRelation = (percentRelation * nbrCaracteresTimeLine) / 100;

                if (sizeRelation <= SFVueGlobaleViewBean.MIN_RELATION_SIZE) {
                    sizeRelation = 3;
                    // ajouter décalage et le retourner pour prendre en compte
                    // dans la suite des relations.
                }

                // Jeanne ----[Marie ][Divorce------------------------>
                // Léonie bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb[Marie
                // --------->

                long sizeAvantDebutRelation = 0;
                if (isFirstRelation) {
                    // Identification du début de la relation !!!!
                    // Correspond aux 'b'
                    long nbrJoursAvantDebutRelation = cal.daysBetween(minDateDebutRelations,
                            new JADate(rel.getDateDebut()));
                    sizeAvantDebutRelation = (nbrCaracteresTimeLine * nbrJoursAvantDebutRelation) / nbrJoursTot;
                    isFirstRelation = false;
                }

                for (int i = 0; i < sizeAvantDebutRelation - 1; i++) {
                    counter--;
                    htmlTimeLine += "-";
                    htmlDateTimeLine += "&nbsp;";
                    timeLineVisibleCharSize++;
                }

                // Dans ce cas, on affiche la description 'short' de la
                // relation, et on ajouter la description complete
                // dans le title.
                if (sizeRelation < SFVueGlobaleViewBean.MIN_RELATION_SIZE) {
                    htmlTimeLine += "<a href=\"#\" title=\"" + rel.getDateDebut()
                            + "\" style=\"text-decoration:none;font-weight : bold;\">[</a>" + "<a href=\"#\" title=\""
                            + rel.getLibelleRelation(SFVueGlobaleViewBean.MIN_RELATION_SIZE + 1)
                            + "\" style=\"text-decoration:none;\">" + rel.getLibelleRelation(sizeRelation) + "</a>";
                    // S'il s'agit de la dernière relation, on inscrit la date
                    // complète.
                    if (iterator.hasNext()) {
                        htmlDateTimeLine += "&nbsp;&nbsp;";
                    } else {
                        htmlDateTimeLine += PRDateFormater.convertDate_JJxMMxAAAA_to_JJxMMxAA(rel.getDateDebut());
                    }
                } else {
                    htmlTimeLine += "<a href=\"#\" title=\"" + rel.getDateDebut()
                            + "\" style=\"text-decoration:none;font-weight : bold;\">[</a>"
                            + rel.getLibelleRelation(sizeRelation);
                    htmlDateTimeLine += PRDateFormater.convertDate_JJxMMxAAAA_to_JJxMMxAA(rel.getDateDebut());

                }
                timeLineVisibleCharSize += rel.getLibelleRelation(sizeRelation).length() + 1;

                int sizeDescRelation = rel.getLibelleRelation(sizeRelation).length();
                sizeDescRelation++; // pour le [
                counter -= sizeDescRelation;

                for (int i = 0; i < (sizeRelation - sizeDescRelation); i++) {
                    htmlTimeLine += "-";
                    htmlDateTimeLine += "&nbsp;";
                    timeLineVisibleCharSize += 1;
                    counter--;
                    if (counter < 1) {
                        break;
                    }
                }

                // Le dernier charactère de la relation.
                if (!JadeStringUtil.isBlankOrZero(rel.getDateFin())) {
                    htmlTimeLine += "<a href=\"#\" title=\"" + rel.getDateFin()
                            + "\" style=\"text-decoration:none;font-weight : bold\">]</a>";
                    htmlDateTimeLine += "&nbsp;";
                } else {
                    htmlTimeLine += "-";
                    htmlDateTimeLine += "&nbsp;";
                }
                timeLineVisibleCharSize++;
                counter--;
            }
        }
        htmlTimeLine += "-";
        htmlDateTimeLine += "&nbsp;";
        timeLineVisibleCharSize++;
        return timeLineVisibleCharSize;
    }

    public void setCsSexe1(String csSexe1) {
        this.csSexe1 = csSexe1;
    }

    public void setCsSexe2(String csSexe2) {
        this.csSexe2 = csSexe2;
    }

    public void setDateDeces1(String dateDeces1) {
        this.dateDeces1 = dateDeces1;
    }

    public void setDateDeces2(String dateDeces2) {
        this.dateDeces2 = dateDeces2;
    }

    public void setDescriptionConjoint1(String descriptionConjoint1) {
        this.descriptionConjoint1 = descriptionConjoint1;
    }

    public void setDescriptionConjoint2(String descriptionConjoint2) {
        this.descriptionConjoint2 = descriptionConjoint2;
    }

    public void setHtmlDateTimeLine(String htmlDateTimeLine) {
        this.htmlDateTimeLine = htmlDateTimeLine;
    }

    public void setHtmlTimeLine(String htmlTimeLine) {
        this.htmlTimeLine = htmlTimeLine;
    }

    public void setIdMembreFamille1(String idMembreFamille1) {
        this.idMembreFamille1 = idMembreFamille1;
    }

    public void setIdMembreFamille2(String idMembreFamille2) {
        this.idMembreFamille2 = idMembreFamille2;
    }

    public void setIdRelationConjoint(String idRelationConjoint) {
        this.idRelationConjoint = idRelationConjoint;
    }

    public void setIdTiers1(String idTiers1) {
        this.idTiers1 = idTiers1;
    }

    public void setIdTiers2(String idTiers2) {
        this.idTiers2 = idTiers2;
    }

    public void setNss1(String nss1) {
        this.nss1 = nss1;
    }

    public void setNss2(String nss2) {
        this.nss2 = nss2;
    }

    public void setTimeLineVisibleCharSize(int timeLineVisibleCharSize) {
        this.timeLineVisibleCharSize = timeLineVisibleCharSize;
    }

    @Override
    public String toString() {
        return getKey();
    }

}
