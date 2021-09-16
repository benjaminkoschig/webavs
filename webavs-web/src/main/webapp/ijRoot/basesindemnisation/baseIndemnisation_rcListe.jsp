<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.ij.vb.basesindemnisation.IJBaseIndemnisationJoinTiersViewBean"%>
<%@page import="globaz.ij.vb.basesindemnisation.IJBaseIndemnisationJoinTiersListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	IJBaseIndemnisationJoinTiersListViewBean viewBean = (IJBaseIndemnisationJoinTiersListViewBean) request.getAttribute("viewBean");
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
	menuName = globaz.ij.menu.IAppMenu.MENU_OPTION_BASES_INDEMNISATIONS;
	size = viewBean.size();
	
	String csTypeIJ = request.getParameter("csTypeIJ");
	
	if(IIJPrononce.CS_PETITE_IJ.equals(csTypeIJ)||
	   		IIJPrononce.CS_GRANDE_IJ.equals(csTypeIJ)||
			IIJPrononce.CS_FPI.equals(csTypeIJ)){
		detailLink = "ij?userAction=ij.basesindemnisation.baseIndemnisation.afficher&selectedId=";	
	}else{
		detailLink = "ij?userAction=ij.basesindemnisation.baseIndemnisationAitAa.afficher&selectedId=";
	}
	globaz.prestation.tools.PRIterateurHierarchique iterH = viewBean.iterateurHierarchique();
%>

<%@page import="globaz.ij.api.prononces.IIJPrononce"%>
<%@page import="globaz.ij.application.IJApplication"%>
<SCRIPT language="JavaScript">
	function afficherCacher(id) {
		if (document.all("groupe_" + id).style.display == "none") {
			document.all("groupe_" + id).style.display = "block";
			document.all("bouton_" + id).value = "-";
		} else {
			document.all("groupe_" + id).style.display = "none";
			document.all("bouton_" + id).value = "+";
		}
	}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH>&nbsp;</TH>    
	<TH><ct:FWLabel key="JSP_BASE_IND_NO"/></TH>
    <TH><ct:FWLabel key="JSP_COT_D_PERIODE"/></TH>
    <TH><ct:FWLabel key="JSP_BASE_IND_NBR_JOURS_COUVERT"/></TH>
    <TH><ct:FWLabel key="JSP_BASE_IND_ETAT"/></TH>
    <TH><ct:FWLabel key="JSP_BASE_IND_HERITEE"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
<%
		IJBaseIndemnisationJoinTiersViewBean line = null;
		try {
			line = (IJBaseIndemnisationJoinTiersViewBean) iterH.next();
		} catch (Exception e) {
			e.printStackTrace();
			break;
		}

		//on transmet dans la requête le type d'annonce pour savoir sur quel ecran aller ensuite
		actionDetail = targetLocation  + "='" + detailLink + line.getIdBaseIndemisation()+"'";

if (iterH.isPositionPlusPetite()) { %>
</TBODY>
<%
} else if (iterH.isPositionPlusGrande()) {
	%><TBODY id="groupe_<%=line.getIdParent()%>" style="display: none;"><%
} %>

    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<TD class="mtd">
<% for (int idPosition = 1; idPosition < iterH.getPosition(); ++idPosition) { %>
	&nbsp;&nbsp;
<% } %>
	<%
	String link = "";
	String link2 = "";
	if(IIJPrononce.CS_PETITE_IJ.equals(csTypeIJ)|| IIJPrononce.CS_GRANDE_IJ.equals(csTypeIJ) || IIJPrononce.CS_FPI.equals(csTypeIJ)){
		link = "ij?userAction=ij.controleAbsences.dossierControleAbsencesAjax.afficher&idBaseIndemnisation=" + line.getIdBaseIndemisation() + "&idTiers=" + line.getIdTiers() + "&idPrononce=" + line.getIdPrononce();
		link2 = targetLocation + "='" + link+"'";
	}
	else {
		link = detailLink + line.getIdBaseIndemisation();
		link2 = actionDetail;
	}
	
	

	%>
	<ct:menuPopup menu="ij-basesindemnisations" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=link%>">
		<ct:menuParam key="selectedId" value="<%=line.getIdBaseIndemisation()%>"/>
		<ct:menuParam key="forNoBaseIndemnisation" value="<%=line.getIdBaseIndemisation()%>"/>
		<ct:menuParam key="idPrononce" value="<%=viewBean.getForIdPrononce()%>"/>
		
		
		
		<%if(IIJPrononce.CS_PETITE_IJ.equals(csTypeIJ)||
			 	IIJPrononce.CS_GRANDE_IJ.equals(csTypeIJ)||
				IIJPrononce.CS_FPI.equals(csTypeIJ)){ %>
			<ct:menuExcludeNode nodeId="calculerait"/>
			<ct:menuExcludeNode nodeId="calculeraa"/>
		<%}else if(IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(csTypeIJ)){ %>
			<ct:menuExcludeNode nodeId="calculergp"/>
			<ct:menuExcludeNode nodeId="calculeraa"/>
		<%}else if(IIJPrononce.CS_ALLOC_ASSIST.equals(csTypeIJ)){ %>
			<ct:menuExcludeNode nodeId="calculergp"/>
			<ct:menuExcludeNode nodeId="calculerait"/>
		<%}%>
		
		<%if(line.getCsEtat().equals(globaz.ij.api.basseindemnisation.IIJBaseIndemnisation.CS_ANNULE)){%>
			<ct:menuExcludeNode nodeId="corrigerbi"/>
		<%}%>
	
		<%if(line.getCsEtat().equals(globaz.ij.api.basseindemnisation.IIJBaseIndemnisation.CS_COMMUNIQUE)
				|| line.getCsEtat().equals(globaz.ij.api.basseindemnisation.IIJBaseIndemnisation.CS_ANNULE)){%>
			<ct:menuExcludeNode nodeId="supprimerbi"/>
		<%}%>
		
	</ct:menuPopup>
	
	
	
	
	
		
<% if (iterH.isPere()) { %>
	<span align="right"><INPUT id="bouton_<%=line.getIdBaseIndemisation()%>" type="button" value="+" onclick="afficherCacher(<%=line.getIdBaseIndemisation()%>)"></span>
<% } %>
</TD>

<%if(line.hasPostit()){%>
	<TD class="mtd" nowrap="nowrap">
		<table width="100%">
			<tr>
				<td onClick="<%=link2%>"><%=line.getIdBaseIndemisation()%>&nbsp;</td>
				<td align="right"><ct:FWNote sourceId="<%=line.getIdBaseIndemisation()%>" tableSource="<%=IJApplication.KEY_POSTIT_BASES_INDEMNISATION%>"/></td>
			</tr>
		</table>
	</TD>
<%}else{%>
	<TD class="mtd" nowrap="nowrap" onClick="<%=link2%>"><%=line.getIdBaseIndemisation()%>&nbsp;</TD>
<%}%>
	<TD class="mtd" nowrap="nowrap" onClick="<%=link2%>"><%=line.getDateDebutPeriode()+ " - " + line.getDateFinPeriode()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=link2%>"><%=line.getNombreJoursCouverts()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=link2%>"><%=line.getEtatLibelle()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=link2%>" align="center"><%=globaz.ij.api.basseindemnisation.IIJBaseIndemnisation.CS_NORMAL.equals(line.getCsTypeBase())?"":"<img src=\""+servletContext+"/images/ok.gif\">"%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>