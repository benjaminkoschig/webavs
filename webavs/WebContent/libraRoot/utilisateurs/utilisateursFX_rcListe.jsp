<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
    globaz.libra.vb.utilisateurs.LIUtilisateursFXListViewBean viewBean = (globaz.libra.vb.utilisateurs.LIUtilisateursFXListViewBean)request.getAttribute ("viewBean");
    size = viewBean.getSize();
    session.setAttribute("listViewBean",viewBean);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>    
			<TH><ct:FWLabel key="ECRAN_RC_RUSER_VISA"/></TH>
			<TH><ct:FWLabel key="ECRAN_RC_RUSER_NOM"/></TH>
			<TH><ct:FWLabel key="ECRAN_RC_RUSER_PRENOM"/></TH>
			<TH><ct:FWLabel key="ECRAN_RC_RUSER_EMAIL"/></TH>		 		
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
			<%
			globaz.libra.vb.utilisateurs.LIUtilisateursFXViewBean line = (globaz.libra.vb.utilisateurs.LIUtilisateursFXViewBean)viewBean.getEntity(i);
			%>
			<TD class="mtd"><%="".equals(line.getVisa())?"&nbsp;":line.getVisa()%></TD>
			<TD class="mtd"><%="".equals(line.getLastname())?"&nbsp;":line.getLastname()%></TD>
			<TD class="mtd"><%="".equals(line.getFirstname())?"&nbsp;":line.getFirstname()%></TD>
			<TD class="mtd"><%="".equals(line.getEmail())?"&nbsp;":line.getEmail()%></TD>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>