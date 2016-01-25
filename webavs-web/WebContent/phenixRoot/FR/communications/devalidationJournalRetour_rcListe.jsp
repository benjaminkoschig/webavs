<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<% 
	globaz.phenix.db.communications.CPDevalidationJournalRetourListViewBean viewBean = (globaz.phenix.db.communications.CPDevalidationJournalRetourListViewBean) session.getAttribute("listViewBean");
    //detailLink ="phenix?userAction=phenix.communications.validationJournalRetour.afficher&selectedId="; 
//    detailLink = "phenix.communications.validationJournalRetour.modifierDecision";
    detailLink ="phenix?userAction=phenix.communications.validationJournalRetour.devalider";
    size = viewBean.getSize();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <th width="20">&nbsp;</th>
	    <TH width="*" align="center">N°Journal</TH>
      	<TH width="*" align="center">Année</TH>
      	<TH width="*" align="center">N°Affilié</TH>
      	<TH width="*" align="center">Nom</TH>
      	<th  width="*" align="center">Extraction</th>
		<th  width="*" align="center">Taxation</th>
		<th  width="*">Revenu ou rente</th>
		<%if(((globaz.phenix.application.CPApplication) viewBean.getSession().getApplication()).isRevenuAgricole()) {%>
			<th  width="*" align="center"><%=viewBean.getSession().getApplication().getLabel("REVENU_AGRICOLE", "FR")%></th>
		<%}else{ %>
			<th  width="*" align="center"><%=viewBean.getSession().getApplication().getLabel("REVENU_AUTRE", "FR")%></th>
		<%}%>
		<th  width="*" align="center">Capital</th>
		<th  width="*" align="center">Fortune</th>
		<th  width="*">Validé</th>
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
	    <ct:menuPopup menu="CP-DevalidationJournauxReception" label="<%=optionsPopupLabel%>" detailLink="<%=tmp%>" target="top.fr_main">
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
	     
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>