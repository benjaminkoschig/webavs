
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%
    globaz.phenix.db.principale.CPDecisionListViewBean viewBean = (globaz.phenix.db.principale.CPDecisionListViewBean)request.getAttribute ("viewBean");
    size = viewBean.size ();
    detailLink ="phenix?userAction=phenix.principale.decision.chercher&selectedId=";
    //menuName="Principale-decision";
    session.setAttribute("listViewBean",viewBean);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
     <TH width="">Mitglied-Nr.</TH>
    <TH width="">Name</TH>
    <Th width="">Typ</Th>
    <Th width="">Beitragsperiode</Th>
    <Th width="">J�hrliche Beitr�ge</Th>
    <Th width="">IK-Eintrag</Th>
    <Th width="15%">Massgebendes Verm�gen</Th>
    <Th width="">Art</Th>
    <Th width="">Job-Nr.</Th>
    <Th width="">Status</Th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%
    globaz.phenix.db.principale.CPDecisionListerViewBean lineBean = (globaz.phenix.db.principale.CPDecisionListerViewBean)viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+lineBean.getIdTiers()+"&selectedId2=" + lineBean.getIdAffiliation()+"'";
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
	      		style = "style=background-color:#00CCCC;text-decoration:line-through";
		} else {
        		style += " style=text-decoration:line-through ";
		}
	} else if (idEtatDecision.equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_SORTIE)){
		style += " style=text-decoration:line-through";
	} else if (typeDecision.equalsIgnoreCase(globaz.phenix.db.principale.CPDecision.CS_IMPUTATION)) {
	      style = "style=background-color:#00CCCC";
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
    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="15%" align="left"><%=lineBean.getNumAffilie()%></TD>
    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="18%" align="left"><%=lineBean.getNomPrenom()%></TD>
    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="15%" align="left"><%=lineBean.getTypeDecisionLibelle()%></TD>
    <TD class="mtd" <%=style%> onClick="<%=actionDetail%>" width="15%" align="center"><%=lineBean.getPeriodeDecision()%></TD>
    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="9%" align="right"><%=lineBean.getCotisationAnnuelle()%></TD>
    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="*" align="right"><%=lineBean.getRevenuCi()%></TD>
    <TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="15%" align="right"><%=lineBean.getRevenuFortuneDeterminant()%></TD>
    <TD class="mtd" <%=style%> onClick="<%=actionDetail%>" width="*"><%=lineBean.getGenreAffilieLibelle()%></TD>
    <TD class="mtd" <%=style%> onClick="<%=actionDetail%>" width="*"><%=lineBean.getIdPassage()%></TD>
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
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>