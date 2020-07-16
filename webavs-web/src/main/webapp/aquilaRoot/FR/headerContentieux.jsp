<%@ page import="globaz.aquila.db.access.poursuite.*"%>
<%@ page import="globaz.osiris.translation.*"%>
<%@ page import="globaz.jade.client.util.*"%>
<%
	COContentieux contentieuxViewBean = (COContentieux) session.getAttribute("contentieuxViewBean");

	String etapeLibelle = "";
	String dateDeclenchement = "";
	String dateExecution = "";
	String numPoursuite = "";
	String motif = "";
	String compteAnnexeInformation = "";

	try {
		etapeLibelle = contentieuxViewBean.getEtape().getLibEtapeLibelle();
		dateDeclenchement = contentieuxViewBean.getDateDeclenchement();
		dateExecution = contentieuxViewBean.getDateExecution();	
		numPoursuite = contentieuxViewBean.getNumPoursuite();
		motif = contentieuxViewBean.loadHistorique().getMotif();
		compteAnnexeInformation = contentieuxViewBean.getCompteAnnexeInformation();
	} catch (Exception e) {
	}
%>
<jsp:include flush="true" page="headerContentieuxMin.jsp"/>
<TR>
	<TD>N° poursuite</TD>
	<TD colspan="3"><INPUT type="text" value="<%=numPoursuite%>" class="libelleLongDisabled" readonly></TD>
	<% if (!JadeStringUtil.isDecimalEmpty(compteAnnexeInformation)) { %> 
           	<TD nowrap>Information</TD>
           	<TD nowrap colspan="2">
            	<INPUT style="color:#FF0000" type="text" name="" value="<%=CACodeSystem.getLibelle(session, compteAnnexeInformation)%>" class="inputDisabled" tabindex="-1" readonly>
           	</TD>
           	<TD nowrap></TD>
           	<TD nowrap></TD>
	<% } else { %>
           	<TD nowrap></TD>
           	<TD nowrap></TD>
           	<TD nowrap></TD>
           	<TD nowrap></TD>
	<% } %>
</TR>		
<TR>
	<TD colspan="8"><HR></TD>
</TR>														
<% if (!COContentieux.ID_CONTENTIEUX_BIDON.equals(contentieuxViewBean.getIdContentieux())) { %>
<TR>
	<TD class="label">Dernière étape</TD>
	<TD class="control" colspan="3"><INPUT type="text" value="<%=etapeLibelle%>" class="disabled" style="width: 100%" readonly></TD>
	<TD class="label">Date déclench.</TD>
	<TD class="control"><INPUT type="text" value="<%=dateDeclenchement%>" class="dateDisabled" readonly></TD>
	<TD class="label">Date exécution</TD>
	<TD class="control"><INPUT type="text" value="<%=dateExecution%>" class="dateDisabled" readonly></TD>
</TR>
<% } %>
<% if (motif != null && motif.length() > 0) { %>
<TR>
	<TD class="label">Motif dernière étape</TD>
	<TD class="control" colspan="7">
		<textarea name="" cols="30" rows="2" disabled><%=motif%></textarea>
	</TD>
</TR>										
<% } %>				

