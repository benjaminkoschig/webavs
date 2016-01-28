<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<% 
	globaz.phenix.db.communications.CPValidationJournalRetourListViewBean viewBean = (globaz.phenix.db.communications.CPValidationJournalRetourListViewBean) session.getAttribute("listViewBean");
    //detailLink ="phenix?userAction=phenix.communications.validationJournalRetour.afficher&selectedId="; 
//    detailLink = "phenix.communications.validationJournalRetour.modifierDecision";
    detailLink ="phenix?userAction=phenix.communications.validationJournalRetour.modifierDecision";
    size = viewBean.getSize();
    menuName = "phenix.communications.validationJournalRetour.chercher";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <th width="20">&nbsp;</th>
	    <TH width="*" align="center">Journal</TH>
      	<TH width="*" align="center">Jahr</TH>
      	<TH width="*" align="center">Mitglied-Nr.</TH>
      	<TH width="*" align="center">Name</TH>
      	<th  width="*" align="center">Ext.</th>
		<th  width="*" align="center">Veranlagung</th>
		<th  width="*">Einkommen</th>
		<th  width="*">Einkommen 2</th>
		<th  width="*" align="center">Kapital</th>
		<th  width="*" align="center">Vermögen</th>
		<th  width="*">Validiert</th>
		<th  width="*">Stand.</th>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<% 
	  		globaz.phenix.db.communications.CPValidationJournalRetourViewBean line = (globaz.phenix.db.communications.CPValidationJournalRetourViewBean) viewBean.getEntity(i);
	        actionDetail = "parent.location.href='"+detailLink+"&selectedId="+line.getIdValidation()+"&idRetour="+line.getIdRetour()+"&_method=upd'";
			String tmp = detailLink+"&selectedId="+line.getIdValidation();
    	%>
	     <TD class="mtd" width="">
	    <ct:menuPopup menu="CP-ValidationJournauxReception" label="<%=optionsPopupLabel%>" detailLink="<%=tmp%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=line.getIdValidation()%>"/>  
			<ct:menuParam key="idTiers" value="<%=line.getIdTiers()%>"/>
	 		<ct:menuParam key="idAffiliation" value="<%=line.getIdAffiliation()%>"/>
	 		<ct:menuParam key="idRetour" value="<%=line.getIdRetour()%>"/>
		</ct:menuPopup>
	    </TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="*" align="center"><%=line.getIdJournal()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="*" align="center"><%=line.getAnneeDeLaDecision()%>&nbsp;</TD>
	      <%if (line.getIdTiersDecision().equalsIgnoreCase(line.getIdConjoint())){ %>
	     	<TD class="mtd" onclick="<%=actionDetail%>" width="*" align="center"><%=line.getNumAffiliationConjoint()%>&nbsp;</TD>
	     <%} else { %>	
	     	<TD class="mtd" onclick="<%=actionDetail%>" width="*" align="center"><%=line.getNumAffilie()%>&nbsp;</TD>
	     <%}%>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="*" align="left"><%=line.getNomDecision()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="*" align="center"><%=line.getGroupeExtraction()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="*" align="center"><%=line.getLibelleTaxation()%>&nbsp;</TD>
	     
	     <TD class="mtd" onclick="<%=actionDetail%>" width="*" align="right"><%=line.getRevenu1()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="*" align="right"><%=line.getRevenuAutre1()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="*" align="right"><%=line.getCapital()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="*" align="right"><%=line.getFortuneTotale()%>&nbsp;</TD>
	     <TD class="mtd" align="center" width="">
		    <%if (line.getCodeValidation().equals("2")){ %>
				<IMG src="<%=request.getContextPath()%>/images/asurveiller.gif" border="0">
			<%	} else { %>
				<IMG src="<%=request.getContextPath()%>/images/erreur.gif" border="0"> 
			<%	} %>
		</TD>
		<%if (line.getTiers()!=null){ %>
		<TD class="mtd" onclick="<%=actionDetail%>" width="*" align="right"><%=globaz.phenix.translation.CodeSystem.getLibelle(session,line.getTiers().getEtatCivil())%>&nbsp;</TD>
		<%} else { %>
		<TD class="mtd" onclick="<%=actionDetail%>" width="*" align="right">&nbsp;</TD>
		<%}%>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>