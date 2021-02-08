<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
    globaz.hermes.db.parametrage.HEAttenteEnvoiCIListViewBean viewBean = (globaz.hermes.db.parametrage.HEAttenteEnvoiCIListViewBean) request.getAttribute("viewBean");
    size = viewBean.getSize();
    detailLink = "hermes?userAction=hermes.parametrage.attenteEnvoiCI.afficher&selectedId=";
    /* menuName = "attenteReception-detail"; */
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
<%@page import="globaz.hermes.api.IHEAnnoncesViewBean" %>
<%@page import="globaz.hermes.utils.HENNSSUtils" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<th>Abr.-Nr.</th>
<th>Einkommenscode</th>
<th>Periode</th>
<th>Einkommen</th>
<th>Arbeitgeber oder Einkommensart</th>
<% int index38001 = 0;
    globaz.hermes.db.parametrage.HEAttenteEnvoiCIViewBean line1;
    globaz.hermes.db.parametrage.HEAttenteEnvoiCIViewBean line2;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>
<%condition = (index38001 % 2 == 0);%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

<%
    line1 = (globaz.hermes.db.parametrage.HEAttenteEnvoiCIViewBean) viewBean.getEntity(i);
    actionDetail = targetLocation + "='" + detailLink + line1.getIdAnnonce() + "&refUnique=" + line1.getRefUnique() + "&hiddenStatus=" + line1.getStatut() + "&isArchivage=" + line1.getArchivage() + "'";
%>
<%
    String numAffBrut = line1.getField(IHEAnnoncesViewBean.NUMERO_AFILLIE);
    String numAff = "";
    String codeRevenu = line1.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_GENRE_COTISATIONS);
    if (StringUtils.equals("55555555555", numAffBrut)) {
        numAff = numAffBrut;
    } else {
        if ("8".equals(codeRevenu)) {
            if (HENNSSUtils.isNNSSNegatif(numAffBrut)) {
                numAff = HENNSSUtils.convertNegatifToNNSS(numAffBrut);
            } else {
                numAff = numAffBrut;
            }
            numAff = globaz.commons.nss.NSUtil.formatAVSUnknown(numAff);
        } else {
            numAff = ((globaz.hermes.application.HEApplication) line1.getSession().getApplication()).formatAffilie(numAffBrut);
        }
    }

    if (line1.getField(IHEAnnoncesViewBean.CODE_1_OU_2).equals("1")) {
        index38001++;
        String extourne = line1.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_EXTOURNES);
        String chiffreParticulier = line1.getField(IHEAnnoncesViewBean.CHIFFRE_CLEF_PARTICULIER);
%>
<TD class="mtd" onClick="<%=actionDetail%>"><%= numAff.equals("") ? "" : numAff%>&nbsp;</TD>
<TD class="mtd"
    onClick="<%=actionDetail%>"><%= (extourne + codeRevenu + chiffreParticulier).equals("") ? "" : (extourne + codeRevenu + chiffreParticulier)%>&nbsp;
</TD>
<TD class="mtd"
    onClick="<%=actionDetail%>"><%= line1.getField(IHEAnnoncesViewBean.DUREE_COTISATIONS_DEBUT) + "-" + line1.getField(IHEAnnoncesViewBean.DUREE_COTISATIONS_FIN) + "." + line1.getField(IHEAnnoncesViewBean.ANNEES_COTISATIONS_AAAA)%>&nbsp;
</TD>
<TD class="mtd" onClick="<%=actionDetail%>" align="right"><%=line1.getRevenuFormatte(extourne)%>&nbsp;</TD>

<%
    line2 = (i == viewBean.size() - 1) ? null : (globaz.hermes.db.parametrage.HEAttenteEnvoiCIViewBean) viewBean.getEntity(i + 1);
    if (line2 != null && line2.getField(IHEAnnoncesViewBean.CODE_1_OU_2).equals("2")) {
        if (i + 1 != viewBean.size() - 1) {
            i++;
        }
        actionDetail = targetLocation + "='" + detailLink + line2.getIdAnnonce() + "&refUnique=" + line2.getRefUnique() + "&hiddenStatus=" + line2.getStatut() + "&isArchivage=" + line2.getArchivage() + "'";
%>
<TD class="mtd" onClick="<%=actionDetail%>" colspan="4"><%= line2.getTextRevenu(numAffBrut, extourne, codeRevenu)%>&nbsp;</TD>
<%
} else {
    actionDetail = targetLocation + "='" + detailLink + line1.getIdAnnonce() + "&refUnique=" + line1.getRefUnique() + "&hiddenStatus=" + line1.getStatut() + "&isArchivage=" + line1.getArchivage() + "'";
%>
<TD class="mtd" onClick="<%=actionDetail%>" colspan="4"><%= line1.getTextRevenu(numAffBrut, extourne, codeRevenu)%>&nbsp;</TD>
<%}%>
<%
} else if (line1.getChampEnregistrement().startsWith("39001")) {
    index38001++;
%>
<TD class="mtd" onClick="<%=actionDetail%>" colspan="5"
    align="right"><%= globaz.globall.util.JAUtil.isIntegerEmpty(line1.getField(IHEAnnoncesViewBean.NOMBRE_INSCRIPTIONS_CI)) ? "Inscriptions : 0" : "Inscriptions : " + globaz.hermes.utils.StringUtils.removeUnsignigicantZeros(line1.getField(IHEAnnoncesViewBean.NOMBRE_INSCRIPTIONS_CI))%>&nbsp;<%= line1.getField(IHEAnnoncesViewBean.CI_ADDITIONNEL).equals("") ? "&nbsp" : (", CI Additionnel : " + (line1.getField(IHEAnnoncesViewBean.CI_ADDITIONNEL).equals("0") ? "Non" : "Oui"))%>
</TD>
<% } else if (line1.getChampEnregistrement().startsWith("39002")) {%>
<TD class="mtd" onClick="<%=actionDetail%>" colspan="5"
    align="right"><%= globaz.globall.util.JAUtil.isIntegerEmpty(line1.getField(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA)) ? "Date de clôture : -" : "Date de clôture : " + globaz.hermes.utils.DateUtils.convertDate(line1.getField(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA), globaz.hermes.utils.DateUtils.MMAA, globaz.hermes.utils.DateUtils.MMAA_DOTS)%>&nbsp;
</TD>
<% }%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>