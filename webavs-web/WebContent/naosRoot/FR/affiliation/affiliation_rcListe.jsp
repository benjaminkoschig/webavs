<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "naos?userAction=naos.affiliation.affiliation.afficher&selectedId=";
	menuName="affiliationListe";
	globaz.naos.db.affiliation.AFAffiliationListViewBean viewBean = (globaz.naos.db.affiliation.AFAffiliationListViewBean)request.getAttribute ("viewBean");
		
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <%
		boolean taxationPrincipale = false;
		size = viewBean.getSize ();	
		if(size==1) {
			taxationPrincipale = true;
		}
	%>
	<TH width="30">&nbsp;</TH>
	<TH nowrap width="75" align="left">N° affili&eacute;</TH>
	<TH width="250">Type d'affiliation</TH>
	<TH width="100" align="center">D&eacute;but</TH>
	<TH width="100" align="center">Fin</TH>
	<TH width="250">Motif de fin</TH>
	<TH width="250">Personnalit&eacute; juridique</TH>
	<TH width="250">Ind.</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
		actionDetail = targetLocation + "='" + detailLink + viewBean.getAffiliationId(i)+ "&idTiers=" + viewBean.getIdTiers(i) + "'";   
		String linkMenu2 = detailLink + viewBean.getAffiliationId(i)+ "&idTiers=" + viewBean.getIdTiers(i);
		String lineStyle="";
		if(!taxationPrincipale && viewBean.isTaxationPrincipale(i)) {
			lineStyle = "class='giText'";
		} else {
			lineStyle = "class='mtd'";
		}
		if(!globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getDateFin(i))) {
			lineStyle += " style='color:#999999' ";
		}
	%>
     
    <TD <%=lineStyle%> width="30" >
    
    <ct:menuPopup menu="AFOptionsAffiliationListe" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=linkMenu2%>">
		<ct:menuParam key="affiliationId" value="<%=viewBean.getAffiliationId(i)%>"/>
		<ct:menuParam key="selectedId" value="<%=viewBean.getAffiliationId(i)%>"/>
	</ct:menuPopup>
    </TD>
	<TD <%=lineStyle%> onClick="<%=actionDetail%>" width="75" align="left"><nobr><%=viewBean.getAffilieNumero(i)%></nobr></TD>
	<% if(viewBean.getTraitement(i).booleanValue()) { %>
		<TD <%=lineStyle%> onClick="<%=actionDetail%>" width="250">(<%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getTypeAffiliation(i))%>)</TD>
	<% } else { %>
		<TD <%=lineStyle%> onClick="<%=actionDetail%>" width="250"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getTypeAffiliation(i))%></TD>
	<% } %>
	<TD <%=lineStyle%> onClick="<%=actionDetail%>" width="100" align="center" ><%=viewBean.getDateDebut(i)%></TD>
    <TD <%=lineStyle%> onClick="<%=actionDetail%>" width="100" align="center"><%=viewBean.getDateFin(i)%></TD>
    <TD <%=lineStyle%> onClick="<%=actionDetail%>" width="250"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getMotifFin(i))%></TD>
    <TD <%=lineStyle%> onClick="<%=actionDetail%>" width="250"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getPersonnaliteJuridique(i))%></TD>
    <TD <%=lineStyle%> onClick="<%=actionDetail%>" width="250"><%=globaz.naos.translation.CodeSystem.getCode(session,viewBean.getTypeAssocie(i))%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>