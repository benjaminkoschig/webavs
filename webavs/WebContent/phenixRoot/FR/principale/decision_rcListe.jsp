
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%
    globaz.phenix.db.principale.CPDecisionListViewBean viewBean = (globaz.phenix.db.principale.CPDecisionListViewBean)request.getAttribute ("viewBean");
    size = viewBean.size ();
    detailLink ="phenix?userAction=phenix.principale.decision.afficher&selectedId=";
    menuName="Principale-decision";
    session.setAttribute("listViewBean",viewBean);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <Th nowrap width="16">&nbsp;</Th>
      <TH width="">Annee</TH>
      <Th width="">Type</Th>
    <Th width="">Période</Th>
    <Th width="">Date décision</Th>
    <Th width="">Cotisation</Th>
    <Th width="">Revenu CI</Th>
    <%if (viewBean.isAffichageMiseEnCompte()) {%>
	    <Th width="">Imputation</Th>
    <%}%>
    <Th width="15%">Fortune / Revenu dét.</Th>
    <%if (viewBean.isAffichageSpecification()) {%>
		<Th width="">Spécification</Th>
	<%}%>
    <Th width="">Genre</Th>
    <Th width="">Etat</Th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%
    globaz.phenix.db.principale.CPDecisionListerViewBean lineBean = (globaz.phenix.db.principale.CPDecisionListerViewBean)viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+lineBean.getIdDecision()+"'";
	%>
    <%	String style = "";
    	String typeDecision = lineBean.getTypeDecision();
    	String idEtatDecision = lineBean.getDernierEtat();
    
    if("1".equals(lineBean.getModifiable())){
 			style = "style=font-style:italic;";
	}
	if(!lineBean.getActive().booleanValue()){
			style += "style=color:#999999";
	}
    if(lineBean.isRadie()) {
		if (typeDecision.equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_IMPUTATION)) {
	      		style = "style=color:#00CCCC;text-decoration:line-through";
		} else {
        		style += " style=text-decoration:line-through ";
		}
	} else if (idEtatDecision.equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_SORTIE)){
		style += " style=text-decoration:line-through";
	} else if (typeDecision.equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_IMPUTATION)) {
		if(lineBean.getActive().booleanValue()||idEtatDecision.equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_VALIDATION)){
	      style = "style=background-color:#00CCCC";
		}
    } 
    if ((typeDecision.equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_DEFINITIVE))
		||(typeDecision.equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_CORRECTION))) {	     
		 style += " style=font-style:italic;";
	}
	/*if(lineBean.isInclusSortie()){
		if(!lineBean.getActive().booleanValue()){
			style = "style=color:#FFC0CB";
		} else {
			style = "style=color:#CC0099";
		}
	}*/
     String tmp = detailLink + lineBean.getIdDecision();
	 %>
     <TD class="mtd" width="">
     <ct:menuPopup menu="CP-decision" label="<%=optionsPopupLabel%>" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>" target="top.fr_main">
	 	<ct:menuParam key="selectedId" value="<%=lineBean.getIdDecision()%>"/>
	 	<ct:menuParam key="idDecision" value="<%=lineBean.getIdDecision()%>"/> 
	 </ct:menuPopup>
     </TD>
    <%if (lineBean.isInclusSortie()) {%>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="4%" align="center"><%=lineBean.getAnneeMoisRadiation()%>&nbsp;</TD>
	<%} else {%>
		<TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="4%" align="center"><%=lineBean.getAnneeDecision()%>&nbsp;</TD>
	<%}%>    
    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="15%" align="left"><%=lineBean.getTypeDecisionLibelle()%>&nbsp;</TD>
    <TD class="mtd" <%=style%> onClick="<%=actionDetail%>" width="18%" align="center"><%=lineBean.getPeriodeDecision()%>&nbsp;</TD>
    <%if (viewBean.isAffichageDateFacturation()) {%>
	    <TD class="mtd" <%=style%> onClick="<%=actionDetail%>" width="12%" align="center"><%=lineBean.getDateFacturation()%>&nbsp;</TD>
    <%} else {%>	
		<TD class="mtd" <%=style%> onClick="<%=actionDetail%>" width="12%" align="center"><%=lineBean.getDateInformation()%>&nbsp;</TD>
    <%}%>
    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="9%" align="right"><%=lineBean.getCotisationAnnuelle()%>&nbsp;</TD>
    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="*" align="right"><%=lineBean.getRevenuCi()%>&nbsp;</TD>
    <%if (viewBean.isAffichageMiseEnCompte()) {%>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="*" align="right"><%=lineBean.getMiseEnCompte()%>&nbsp;</TD>
	<%}%>
    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="15%" align="right"><%=lineBean.getRevenuFortuneDeterminant()%>&nbsp;</TD>
    <%if (viewBean.isAffichageSpecification()) {%>
	    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="*" align="left"><%=lineBean.getSpecificationLibelle()%>&nbsp;</TD>
	<%}%>    
    <TD class="mtd" <%=style%> onClick="<%=actionDetail%>" width="*"><%=lineBean.getGenreAffilieLibelle()%>&nbsp;</TD>
  
    <TD class="mtd" align="center" width="">
    <%if (lineBean.isComptabilise()){ %>
		<IMG title="<%=lineBean.getDernierEtatLibelle()%>" src="<%=request.getContextPath()%>/images/ok.gif" border="0">
	<%	} else if (lineBean.isValide()){ %>
		<IMG title="<%=lineBean.getDernierEtatLibelle()%>" src="<%=request.getContextPath()%>/images/asurveiller.gif" border="0"> 
	<%	} else if (globaz.phenix.db.principale.CPDecision.CS_PB_COMPTABILISATION.equalsIgnoreCase(lineBean.getDernierEtat())){ %>	
		<IMG title="<%=lineBean.getDernierEtatLibelle()%>" src="<%=request.getContextPath()%>/images/avertissement.gif" border="0">
	<%	} else { %>
		<IMG title="<%=lineBean.getDernierEtatLibelle()%>" src="<%=request.getContextPath()%>/images/erreur.gif" border="0"> 
	<%	} %>
	</TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>