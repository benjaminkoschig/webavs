<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<% 
	globaz.phenix.db.communications.CPJournalRetourListViewBean viewBean = (globaz.phenix.db.communications.CPJournalRetourListViewBean) session.getAttribute("listViewBean");
    detailLink ="phenix?userAction=phenix.communications.journalRetour.afficher&selectedId="; 
    String communicationLink = "phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.chercher&idJournalRetour="; 
    size = viewBean.getSize();
    menuName = "Communications-journalRetour-default";
    
    
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    
		<th width="20">&nbsp;</th>
		<TH width="10">&nbsp;</TH>
      	<TH width="5" align="left">Nr.</TH>
      	<TH width="*" align="left">Name des Journals</TH>
      	<TH width="5" align="left">Kanton</TH>
      	<th  width="15%">Empfangsdatum</th>
		<th  width="10%">Anzahl Steuermeldungen</th>
		<th  width="15%">Status</th>
		<th  width="15%">Typ</th>
		<th  width="15%">Geöffnetes Journal</th>  
		<!--th  width="10%">Succès</th-->

    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>


		<% 
	  		globaz.phenix.db.communications.CPJournalRetour line = (globaz.phenix.db.communications.CPJournalRetour) viewBean.getEntity(i);
	    	actionDetail = targetLocation+"='"+communicationLink+line.getIdJournalRetour()+"'";	  
	 		String tmp = detailLink + line.getIdJournalRetour();
		 %>
	     <TD class="mtd" width="2">
		     <ct:menuPopup menu="CP-journalRetour" label="<%=optionsPopupLabel%>" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>" target="top.fr_main">
			 	<ct:menuParam key="idJournalRetour" value="<%=line.getIdJournalRetour()%>"/>  
			 </ct:menuPopup>
	     </TD>
	  	 <TD class="mtd" width="10" onClick="" align="right"><input name="listIdJournalRetour" type="checkbox" value="<%=line.getIdJournalRetour()%>"/></TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="5" align="right"><%=line.getIdJournalRetour()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="*"><%=line.getLibelleJournal()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="*"><%=line.getCodeCanton()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="15%" align="center"><%=line.getDateReception()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="10%" align="right"><%=line.getNbCommunication()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="15%"><%=line.getVisibleStatus()%>&nbsp;</TD>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="10%"><%=globaz.phenix.translation.CodeSystem.getLibelle(session,line.getTypeJournal())%>&nbsp;</TD>
	     <%if(line.getTypeJournal().equals(globaz.phenix.db.communications.CPJournalRetour.CS_TYPE_JOURNAL_SEDEX)){%>
	     <TD class="mtd" onclick="<%=actionDetail%>" width="5%" align="center"> <IMG src="<%=request.getContextPath()%><%=line.isJournalEnCours().booleanValue()?"/images/sedex_ouvert.jpg" : "/images/sedex_ferme.jpg"%>"></TD>
	     <%}%>
	     <%--TD class="mtd" onclick="<%=actionDetail%>" width="10%" align="center"> <IMG src="<%=request.getContextPath()%><%=line.isSuccess()?"/images/ok.gif" : "/images/erreur.gif"%>"></TD--%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>