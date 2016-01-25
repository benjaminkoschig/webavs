<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- <%@ taglib uri="/WEB-INF/aitaglib.tld" prefix="ai" %>--%>
<%
globaz.amal.vb.formule.AMChampListViewBean viewBean = (globaz.amal.vb.formule.AMChampListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	detailLink = "amal?userAction=amal.formule.champ.afficher&idChamp=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
		<TH><ai:AIProperty key="LISTE_DE_CHAMPS"/><ct:FWLabel key="LIBELLE"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
	globaz.amal.vb.formule.AMChampViewBean line = (globaz.amal.vb.formule.AMChampViewBean)viewBean.getEntity(i);
	if (isSelection) { // mode sélection
		actionDetail = "document.body.innerHTML='';parent.location.href='" + globaz.fweb.taglib.FWChooserTag.getSelectLink(pageContext, i) + "'";
	} else { // détail "normal"
		actionDetail = targetLocation  + "='" + detailLink + line.getId()+"&idSignet="+ line.getChamp().getIdSignet()+ "'";
	}
	%>
	<TD class="mtd"  onclick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getChamp().getCsChamp())%></TD>

<ct:menuChange displayId="options" menuId="amal-optionsformules"/>
<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdFormule()%>" menuId="amal-optionsformules"/>

<SCRIPT language="javascript">

reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>