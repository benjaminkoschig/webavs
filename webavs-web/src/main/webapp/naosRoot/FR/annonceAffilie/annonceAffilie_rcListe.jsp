<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "naos?userAction=naos.annonceAffilie.annonceAffilie.afficher&selectedId=";
	globaz.naos.db.annonceAffilie.AFAnnonceAffilieListViewBean viewBean = (globaz.naos.db.annonceAffilie.AFAnnonceAffilieListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <%
		
	%>
	<TH width="30">&nbsp;</TH>
    <TH align="left" width="90">N&deg; Affili&eacute;</TH>
    <TH align="center" width="300">Nom de l'affili&eacute;</TH>
    <TH align="center" width="210">Champ modifi&eacute;</TH>
    <TH align="left" width="110">Date d'annonce</TH>
    <TH align="left" width="90">A annoncer</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
		<%    
			actionDetail = targetLocation + "='" + detailLink + viewBean.getAnnoncePreparationId(i)+"&affiliationId="+viewBean.getAffiliation(i).getAffiliationId()+ "'";
			String linkMenu2 = detailLink + viewBean.getAnnoncePreparationId(i)+"&affiliationId="+viewBean.getAffiliation(i).getAffiliationId();
		%>
		    <TD class="mtd" width="30" >
		    <ct:menuPopup menu="AFMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=linkMenu2%>"/>
		    </TD>
		    <TD class="mtd" onClick="<%=actionDetail%>" align="center" width="90"><%=viewBean.getAffiliation(i).getAffilieNumero()%></TD>
		    <TD class="mtd" onClick="<%=actionDetail%>" align="left" width="300"><%=viewBean.getTiers(i).getNom()%></TD>
		    <TD class="mtd" onClick="<%=actionDetail%>" align="center" width="210"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getChampModifier(i))%></TD>
		    <TD class="mtd" onClick="<%=actionDetail%>" align="left" width="110"><%=viewBean.getDateAnnonce(i)%></TD>
	    <% 
	    	if (viewBean.isTraitement(i).booleanValue()){
		%>
	    	<TD class="mtd" onClick="<%=actionDetail%>" align="center" width="90"><IMG src="<%=request.getContextPath()%>/images/select2.gif" ></TD>
	    <% } else {%>
	    	<TD class="mtd" onClick="<%=actionDetail%>" align="center" width="90"><%=""%></TD>
	    <% } %>
	  	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>