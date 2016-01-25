<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	//Les labels de cette page commence par le préfix "JSP_PC_PARAM_HOMES_L"

	PCHomeListViewBean viewBean = (PCHomeListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();

	detailLink = "pegasus?userAction="+IPCActions.ACTION_HOME+ ".afficher&selectedId=";
	
	menuName = "pegasus-menuprincipal";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.pegasus.vb.home.PCHomeListViewBean"%>
<%@page import="globaz.pegasus.vb.home.PCHomeViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
		<TH colspan="2"><ct:FWLabel key="JSP_PC_PARAM_HOME_L_NOM_BATIMENT"/></TH>
	    <TH><ct:FWLabel key="JSP_PC_PARAM_HOME_L_NUMERO_IDENTIFICATION"/></TH>
	    <TH><ct:FWLabel key="JSP_PC_PARAM_HOME_L_NPA_LOCALITE"/></TH>
	    <TH><ct:FWLabel key="JSP_PC_PARAM_HOME_L_CANTON"/></TH>
	    <TH><ct:FWLabel key="JSP_PC_PARAM_HOME_L_ID"/></TH> 		
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
		PCHomeViewBean courant = (PCHomeViewBean) viewBean.get(i);
			
			String detailUrl = "parent.location.href='" + detailLink + courant.getHome().getSimpleHome().getIdHome()+"'";
			String desc = courant.getHomeDescription().replaceAll("\\'","\\\\'");
	
		%>
		
		<TD class="mtd" nowrap width="20px">
	     	<ct:menuPopup menu="pegasus-optionshomes" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + courant.getId()%>">
	     			<ct:menuParam key="idHome" value="<%=courant.getId()%>"/>  
	     			<ct:menuParam key="nomHome" value="<%=desc%>"/>  
		 	</ct:menuPopup>
     	</TD>
		
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getHomeDescription()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>" align="right"><%=JadeStringUtil.isEmpty(courant.getHome().getSimpleHome().getNumeroIdentification())?"&nbsp":courant.getHome().getSimpleHome().getNumeroIdentification()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getHome().getAdresse().getLocalite().getNumPostal()+" - "+courant.getHome().getAdresse().getLocalite().getLocalite()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getLibelleCanton()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>" align="right"><%=courant.getId()%></TD>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>