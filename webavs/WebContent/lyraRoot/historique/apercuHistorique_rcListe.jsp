<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%

globaz.lyra.vb.historique.LYApercuHistoriqueListViewBean viewBean = (globaz.lyra.vb.historique.LYApercuHistoriqueListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize();

detailLink = baseLink + "afficher&selectedId=";

menuName = "ly-menuprincipal";
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    	<TH></TH>
    		<TH>No</TH>
    		<TH>Domaine - Description</TH>
    		<TH>date d'exécution</TH>
    		<TH>visa</TH>
    		<TH>etat</TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			globaz.lyra.vb.historique.LYApercuHistoriqueViewBean courant = (globaz.lyra.vb.historique.LYApercuHistoriqueViewBean) viewBean.get(i);
		%>
		
		<TD class="mtd" width="">&nbsp;</TD>
		
		<TD class="mtd" nowrap><%=courant.getIdHistorique()%></TD>
		<TD class="mtd" nowrap><%=courant.getEcheanceEtDomaineApplicatif(courant.getIdEcheance())%></TD>
		<TD class="mtd" nowrap align="center"><%=courant.getDateExecution()%></TD>
		<TD class="mtd" nowrap><%=courant.getVisa()%></TD>
		<%
			String image = "";
			globaz.framework.util.FWLog log = courant.retrieveLog();
			if (log != null && !log.isNew()) {
				image += "<img style=\"float:right\" src=\"";
				if (log.getErrorLevel().equals(globaz.framework.util.FWMessage.INFORMATION))
					image += request.getContextPath()+"/images/information.gif";
				else if (log.getErrorLevel().equals(globaz.framework.util.FWMessage.AVERTISSEMENT))
					image += request.getContextPath()+"/images/avertissement.gif";
				else 
					image += request.getContextPath()+"/images/erreur.gif";		
				image += "\" title=\""+log.getMessagesToString()+"\">";
			} else {
				image = "<img style=\"float:right\" src=\""+request.getContextPath()+"/images/information.gif"+"\" title=\""+log.getMessagesToString()+"\">";
			}
		%>
		<TD class="mtd"><%=image%><%=(courant.getLibelleEtat().equals(""))?"&nbsp;":courant.getLibelleEtat()%>&nbsp;</TD>
		
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>