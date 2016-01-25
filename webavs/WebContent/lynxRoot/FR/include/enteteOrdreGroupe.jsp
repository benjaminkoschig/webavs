
<%@page import="globaz.jade.client.util.JadeStringUtil"%><TR> 

	<% if (!JadeStringUtil.isBlank(request.getParameter("idOrdreGroupe"))) { %>
	<TD width="128">Ordre group&eacute;</TD>
	<TD>
		<INPUT type="hidden" name="forIdOrdreGroupe" value="<%= request.getParameter("idOrdreGroupe") %>"/>
		<INPUT type="hidden" name="idOrdreGroupe" value="<%= request.getParameter("idOrdreGroupe") %>"/>
		
		<INPUT type="text" value='<%=globaz.lynx.utils.LXOrdreGroupeUtil.getLibelleOrdreGroupe(objSession, request.getParameter("idOrdreGroupe"))%>' style="width:7cm" class="libelleLongDisabled" readonly="readonly">
	</TD>
	<% } %>
	
	<% if (!JadeStringUtil.isBlank(request.getParameter("idJournal"))) { %>
	<TD width="128">Journal</TD>
	<TD>
		<INPUT type="hidden" name="forIdJournal" value="<%= request.getParameter("idJournal") %>"/>
		<INPUT type="hidden" name="idJournal" value="<%= request.getParameter("idJournal") %>"/>
	
		<INPUT type="text" value="<%=globaz.lynx.utils.LXJournalUtil.getLibelleJournal(objSession, request.getParameter("idJournal"))%>" style="width:7cm" class="libelleLongDisabled" readonly="readonly">
	</TD>
	<% } %>
	
	<TD>&nbsp;</TD>
	<TD width="128">Soci&eacute;t&eacute; d&eacute;bitrice</TD>
	<TD>
		<INPUT type="hidden" name="forIdSociete" value="<%= request.getParameter("idSociete") %>"/>
		<INPUT type="hidden" name="idSociete" value="<%= request.getParameter("idSociete") %>"/>
		<INPUT type="text" value='<%=globaz.lynx.utils.LXSocieteDebitriceUtil.getLibelle(objSession, request.getParameter("idSociete"))%>' style="width:7m" class="libelleLongDisabled" readonly="readonly">
	</TD>
</TR> 

<% if (!JadeStringUtil.isBlank(request.getParameter("idOrdreGroupe"))) { %>
<TR> 
	<TD width="128">Organe d'ex&eacute;cution</TD>
	<TD>
		<INPUT type="hidden" name="forIdOrganeExecution" value="<%= request.getParameter("idOrganeExecution") %>"/>
		<INPUT type="hidden" name="idOrganeExecution" value="<%= request.getParameter("idOrganeExecution") %>"/>
		<INPUT type="text" value='<%=globaz.lynx.utils.LXOrganeExecutionUtil.getNom(objSession, request.getParameter("idOrganeExecution"))%>' style="width:7cm" class="libelleLongDisabled" readonly="readonly">
	</TD>
</TR> 
<% } %>