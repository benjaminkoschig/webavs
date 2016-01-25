
<TD width="128">Journal</TD>
<TD>
	<INPUT type="hidden" name="forIdJournal" value="<%= request.getParameter("idJournal") %>"/>
	<INPUT type="hidden" name="idJournal" value="<%= request.getParameter("idJournal") %>"/>
	<INPUT type="text" value="<%=globaz.lynx.utils.LXJournalUtil.getLibelleJournal(objSession, request.getParameter("idJournal"))%>" style="width:7cm" class="libelleLongDisabled" readonly="readonly">
</TD>
<TD>&nbsp;</TD>
<TD width="128">Schuldnerfirma</TD>
<TD>
	<INPUT type="hidden" name="forIdSociete" value="<%= request.getParameter("idSociete") %>"/>
	<INPUT type="hidden" name="idSociete" value="<%= request.getParameter("idSociete") %>"/>
	<INPUT type="text" value="<%=globaz.lynx.utils.LXSocieteDebitriceUtil.getLibelle(objSession, request.getParameter("idSociete"))%>" style="width:7m" class="libelleLongDisabled" readonly="readonly">
</TD>