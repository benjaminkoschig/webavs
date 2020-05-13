package globaz.apg.util;


import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;

import java.util.ArrayList;
import java.util.List;

public class APGSeodorErreurListEntities{
    List<APGSeodorErreurEntity> apgSeodorErreurEntityList = new ArrayList<>();
    String messageErreur="";
    BSession session;

    public APGSeodorErreurListEntities() {

    }

    public APGSeodorErreurListEntities(List<APGSeodorErreurEntity> apgSeodorErreurListEntities) {
        this.apgSeodorErreurEntityList = apgSeodorErreurListEntities;
    }

    public APGSeodorErreurListEntities(String messageErreur) {
        this.messageErreur = messageErreur;
    }

    public APGSeodorErreurListEntities (APGSeodorErreurListEntities apgSeodorErreurListEntities) {
        this.apgSeodorErreurEntityList = apgSeodorErreurListEntities.getApgSeodorErreurEntityList();
        this.messageErreur = apgSeodorErreurListEntities.getMessageErreur();
    }

    private String getEnteteTableErreursHtml(){
        StringBuilder enteteTableErreursHtml = new StringBuilder();
        enteteTableErreursHtml.append("<table>");
        enteteTableErreursHtml.append("<thead>");
        enteteTableErreursHtml.append("<tr>");
        enteteTableErreursHtml.append("<th>");
        enteteTableErreursHtml.append(session.getLabel("ENTETE_SEODOR_DEBUT_PERIODE"));
        enteteTableErreursHtml.append("</th>");
        enteteTableErreursHtml.append("<th>");
        enteteTableErreursHtml.append(session.getLabel("ENTETE_SEODOR_FIN_PERIODE"));
        enteteTableErreursHtml.append("</th>");
        enteteTableErreursHtml.append("<th>");
        enteteTableErreursHtml.append(session.getLabel("ENTETE_SEODOR_NBRE_JOURS"));
        enteteTableErreursHtml.append("</th>");
        enteteTableErreursHtml.append("<th>");
        enteteTableErreursHtml.append(session.getLabel("ENTETE_SEODOR_GENRE_SERVICE"));
        enteteTableErreursHtml.append("</th>");
        enteteTableErreursHtml.append("</tr>");
        enteteTableErreursHtml.append("</thead>");
        return enteteTableErreursHtml.toString();
    }

    public String getListErreursTableHTML(){
        StringBuilder erreurTableHtml = new StringBuilder();
        erreurTableHtml.append(getEnteteTableErreursHtml());
        erreurTableHtml.append("<tbody>");
        for (APGSeodorErreurEntity apgSeodorErreurEntity : apgSeodorErreurEntityList) {
            erreurTableHtml.append("<tr>");
            erreurTableHtml.append("<td align=\"center\">");
            erreurTableHtml.append(apgSeodorErreurEntity.getDateDebutPeriode());
            erreurTableHtml.append("</td>");
            erreurTableHtml.append("<td align=\"center\">");
            erreurTableHtml.append(apgSeodorErreurEntity.getDateFinPeriode());
            erreurTableHtml.append("</td>");
            erreurTableHtml.append("<td align=\"center\">");
            erreurTableHtml.append(apgSeodorErreurEntity.getNombreJours());
            erreurTableHtml.append("</td>");
            erreurTableHtml.append("<td align=\"center\">");
            erreurTableHtml.append(apgSeodorErreurEntity.getCodeService());
            erreurTableHtml.append("</td>");
            erreurTableHtml.append("</tr>");
        }
        erreurTableHtml.append("</tbody>");
        erreurTableHtml.append("</table>");
        return erreurTableHtml.toString();
    }

    public List<APGSeodorErreurEntity> getApgSeodorErreurEntityList() {
        return apgSeodorErreurEntityList;
    }

    public void setApgSeodorErreurEntityList(List<APGSeodorErreurEntity> apgSeodorErreurEntityList) {
        this.apgSeodorErreurEntityList = apgSeodorErreurEntityList;
    }

    public String getMessageErreur() {
        return messageErreur;
    }

    public void setMessageErreur(String messageErreur) {
        this.messageErreur = messageErreur;
    }

    public BSession getSession() {
        return session;
    }

    public void setSession(BSession session) {
        this.session = session;
    }
}
