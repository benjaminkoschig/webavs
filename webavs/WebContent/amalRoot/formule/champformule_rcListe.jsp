<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="globaz.amal.vb.formule.AMChampformuleListViewBean"%>
<%@page import="globaz.amal.vb.formule.AMChampformuleViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- <%@ taglib uri="/WEB-INF/amtaglib.tld" prefix="ai" %>--%>
<%  globaz.amal.vb.formule.AMChampformuleListViewBean  viewBean = (globaz.amal.vb.formule.AMChampformuleListViewBean) request.getAttribute("viewBean");
	//globaz.amal.db.envoi.AIChampFormuleListViewBean viewBean = (globaz.ai.db.envoi.AIChampFormuleListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	detailLink = "amal?userAction=amal.formule.champformule.afficher&idSignet=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    	<%-- <TH><ct:FWLabel key="JSP_AM_CH_L_GROUP"/></TH>--%>
    	<TH><ct:FWLabel key="JSP_AM_CH_L_NOMCH"/></TH>
    	<TH><ct:FWLabel key="LIBELLE"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
	globaz.amal.vb.formule.AMChampformuleViewBean line = (globaz.amal.vb.formule.AMChampformuleViewBean)viewBean.getEntity(i);
	
	if (isSelection) { // mode sélection
		actionDetail = "document.body.innerHTML='';parent.location.href='" + globaz.fweb.taglib.FWChooserTag.getSelectLink(pageContext, i) + "'";
	} else { // détail "normal"
		actionDetail = targetLocation  + "='" + detailLink + line.getIdSignet()+"&idFormule="+line.getIdFormule()+"'";   
		//actionDetail = targetLocation  + "='" + line.getId() +"'";
	}
	%>
	
	<%-- <TD class="mtd" onclick="<%=actionDetail%>"></TD>	--%>
	<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getSignetListModel().getSimpleSignetModel().getSignet())?"&nbsp;":line.getSignetListModel().getSimpleSignetModel().getSignet()%></TD>	
	<TD class="mtd" onclick="<%=actionDetail%>"><%=line.getSignetListModel().getSimpleSignetModel().getLibelle()%></TD>	


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