<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.hercule.utils.CEUtils"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEListeControleEmployeurListViewBean"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEListeControleEmployeur"%>

<%
	detailLink = "hercule?userAction=hercule.controleEmployeur.controleEmployeur.afficher&selectedId=";
	CEListeControleEmployeurListViewBean viewBean = (CEListeControleEmployeurListViewBean) request.getAttribute ("viewBean");
	size = viewBean.getSize ();
	String prochainControle = null;
	
	String _fromDraco = request.getParameter("_fromDraco");	
%>


<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>

	<%if (JadeStringUtil.isBlank(_fromDraco))  {%>
	<TH width="30">&nbsp;</TH>
	<%} %>
	<TH nowrap width="200"><ct:FWLabel key="AFFILIE"/></TH>
	<TH width="150"><ct:FWLabel key="GENRE_CONTROLE"/></TH>
	<TH width="100"><ct:FWLabel key="DATE_EFFECTIVE"/></TH>
	<TH width="100"><ct:FWLabel key="DATE_PREVUE"/></TH>
	<TH width="100"><ct:FWLabel key="CE_DEBUT_PERIODE_CONTROLE"/></TH>
	<TH width="100"><ct:FWLabel key="CE_FIN_PERIODE_CONTROLE"/></TH>
	<TH width="70"><ct:FWLabel key="REVISEUR"/></TH>
	<TH width="70"><ct:FWLabel key="NUMERO_RAPPORT"/></TH>
	<TH width="30"><ct:FWLabel key="ACTIF"/></TH>
		
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

	<%    
		CEListeControleEmployeur lineBean  = (CEListeControleEmployeur) viewBean.getEntity(i);
	
		actionDetail = targetLocation + "='" + detailLink + lineBean.getIdControleEmployeur()
		+"&"+VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE +"=" + lineBean.getIdTiers() + "'";
		
		//Ce qui suitpermet la gestion de l'écran de recherche car on vient de draco
		if (!JadeStringUtil.isBlank(_fromDraco))  { 
			actionDetail = "";
		}
		
		if(size == 0){
			prochainControle = lineBean._getProchainControle();
		}
		
	    String actif = "";
	    if(lineBean.isControleActif()) {
	    	actif = "<img title=\"Actif\" src=\"" + request.getContextPath()+"/images/ok.gif\" />";
	    } else {
	    	actif = "<img title=\"Inactif\" src=\"" + request.getContextPath()+"/images/verrou.gif\" />";
	    }
	    
	    String style = "";
	    if(JadeStringUtil.isEmpty(lineBean.getChangeType())) {
	    	style = "style='color:gray;font-style:italic;'";
	    	actionDetail = "";
	    }
	    
	%>
	
	<%if (JadeStringUtil.isBlank(_fromDraco) )  {%>
	<TD class="mtd" width="16" >
		<%if(!JadeStringUtil.isEmpty(lineBean.getChangeType())) {%>
		<ct:menuPopup menu="CE-OptionsControlEmployeur" label="<%=optionsPopupLabel%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=lineBean.getIdControleEmployeur()%>"/>
			<ct:menuParam key="idTiers" value="<%=lineBean.getIdTiers()%>"/>
			<ct:menuParam key="dateDebutControle" value="<%=lineBean.getDateDebutControle()%>"/>
			<ct:menuParam key="dateFinControle" value="<%=lineBean.getDateFinControle()%>"/>
			<ct:menuParam key="dateEffective" value="<%=lineBean.getDateEffective()%>"/>
			<ct:menuParam key="visaReviseur" value="<%=lineBean.getVisaResiveur()%>"/>
			<ct:menuParam key="numAffilie" value="<%=lineBean.getNumAffilie()%>"/>
			<ct:menuParam key="idAffiliation" value="<%=lineBean.getIdAffiliation()%>"/>
			<ct:menuParam key="idTiersVueGlobale" value="<%=lineBean.getIdTiers()%>"/>
		</ct:menuPopup>
		<%}%>
	</TD>
	<%} %>
	<TD class="mtd" onClick="<%=actionDetail%>" <%=style%> width="200"><%="<b>" + lineBean.getNumAffilie() + "</b><br>" + lineBean._getNomAffilie()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" <%=style%> width="150"><%=CEUtils.getLibelle(session,lineBean.getGenreControle())%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" <%=style%> align="center" width="100"><%=!JadeStringUtil.isBlank(lineBean.getDateEffective())?lineBean.getDateEffective():"&nbsp;"%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" <%=style%> align="center" width="100"><%=!JadeStringUtil.isBlank(lineBean.getDatePrevue())?lineBean.getDatePrevue():"&nbsp;"%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" <%=style%> align="center" width="100"><%=!JadeStringUtil.isBlank(lineBean.getDateDebutControle())?lineBean.getDateDebutControle():"&nbsp;"%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" <%=style%> align="center" width="100"><%=!JadeStringUtil.isBlank(lineBean.getDateFinControle())?lineBean.getDateFinControle():"&nbsp;"%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" <%=style%> width="70"><%=!JadeStringUtil.isBlank(lineBean.getVisaResiveur()) ? lineBean.getVisaResiveur():"&nbsp;"%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" <%=style%> width="70"><%=!JadeStringUtil.isBlank(lineBean.getNumNouveauRapport()) ? lineBean.getNumNouveauRapport():"&nbsp;"%>&nbsp;</TD>
	<TD align="center" class="mtd" onClick="<%=actionDetail%>" width="30"><%=actif%>&nbsp;</TD>

	
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%>	
	<%if(prochainControle != null){ %>
	<table class="find" cellspacing="0">
		<tr class="somme">
			<td>&nbsp;<ct:FWLabel key="PROCHAIN_CONTROLE"/>&nbsp;&nbsp;&nbsp;<%=prochainControle%>
			</td>
		</tr>
	</table>
	<%}%>	
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>