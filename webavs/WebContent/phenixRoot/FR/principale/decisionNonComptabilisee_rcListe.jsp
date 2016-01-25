
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%
    globaz.phenix.db.principale.CPDecisionNonComptabiliseeListViewBean viewBean = (globaz.phenix.db.principale.CPDecisionNonComptabiliseeListViewBean)request.getAttribute ("viewBean");
    size = viewBean.size ();
    menuName="CP-decisionNonComptabilisee";
    session.setAttribute("listViewBean",viewBean);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    
<%@page import="globaz.jade.client.util.JadeStringUtil"%><Th nowrap width="16">&nbsp;</Th>
    <Th width="">N° affilié</Th>
    <Th width="">Nom</Th>
    <TH width="">Période</TH>
    <Th width="">Genre</Th>
    <Th width="">Type</Th>
    <Th width="">CI</Th>
    <Th width="">Passage</Th>
    <th width="">Lien CI</th>
    <th width="">Lien décision</th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%
		String style = "";
    	actionDetail = targetLocation+"='"+detailLink+viewBean.getIdDecision(i)+"'";
    	String tmp = detailLink + viewBean.getIdDecision(i);
	%>
    <TD class="mtd" width="">
    <ct:menuPopup menu="<%=menuName%>" label="<%=optionsPopupLabel%>" target="top.fr_main">
	 	<ct:menuParam key="idDecision" value="<%=viewBean.getIdDecision(i)%>"/>
	 	<ct:menuParam key="selectedId" value="<%=viewBean.getIdDecision(i)%>"/>
	</ct:menuPopup>
    </TD>
    <TD class="mtd" <%=style%> width="*" align="right"><%=viewBean.getNumAffilie(i)%></TD>
    <TD class="mtd" <%=style%> width="*" align="left"><%=viewBean.getDescriptionTiers(i)%></TD>
    <TD class="mtd" <%=style%> width="*"><%=viewBean.getPeriodeDecision(i)%></TD>
    <TD class="mtd" <%=style%> width="*"><%=viewBean.getTypeDecision(i)%></TD>
    <TD class="mtd" <%=style%> width="*"><%=viewBean.getGenreDecision(i)%></TD>
    <TD class="mtd" <%=style%> width="*" align="right"><%=viewBean.getMontantCI(i)%></TD>
    <TD class="mtd" <%=style%> width="*" align="right"><%=viewBean.getIdPassage(i)%> - <%=viewBean.getDateFacturation(i)%></TD>
    <TD class="external_link" <%=style%> width="*" align="center">
    	<%
    	String idCompteIndividuel=viewBean.getIdCompteIndividuel(i);
    	if(!JadeStringUtil.isBlankOrZero(idCompteIndividuel)){
    	%>
    	<ct:ifhasright element="pavo.compte.ecriture.chercherEcriture" crud="r">
     	<A href="<%=request.getContextPath()%>\pavo?userAction=pavo.compte.ecriture.chercherEcriture&compteIndividuelId=<%=idCompteIndividuel%>" target="fr_main">CI</A>
     	</ct:ifhasright>
    	<%}%>
    </TD>
    <TD class="external_link" <%=style%> width="*" align="center">
    	<%
    	String idAffiliation=viewBean.getIdAffiliation(i);
    	String idTiers=viewBean.getIdTiers(i);
		if (!JadeStringUtil.isBlankOrZero(idAffiliation) && !JadeStringUtil.isBlankOrZero(idTiers) ) {
		%>
		<ct:ifhasright element="phenix.principale.decision.chercher" crud="r"> 
		<A target="fr_main" href="<%=request.getContextPath()%>\phenix?userAction=phenix.principale.decision.chercher&s&selectedId=<%=idTiers%>&selectedId2=<%=idAffiliation%>">décisions</A>
		</ct:ifhasright>
   		<%}%>	 
     </TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>