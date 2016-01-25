<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.globall.util.*,globaz.pavo.db.compte.*" %>
<%
	idEcran = "CCI0008";
CIRassemblementOuvertureViewBean viewBean = (CIRassemblementOuvertureViewBean)session.getAttribute ("viewBean");
    IFrameHeight = "290";
	bButtonNew = false;
	
%>
<SCRIPT>
	bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script>
	usrAction = "pavo.compte.rassemblementOuvertureEcritures.lister";
</script>
<script>
	bFind = true;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Anzeige der IK-Buchungen des ZIK's<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								Versicherte &nbsp;
							</TD>
							<td>
							<input type="text" readonly size="17" class="disabled" name="numAvs" value="<%=viewBean.getCi().getNssFormate()%>">
							<input type="text" readonly size="76" class="disabled" name="nomPrenom" value="<%=viewBean.getCi().getNomPrenom()%>">
							</td>
						</TR>
						<TR>
							<TD nowrap>Geburtsdatum &nbsp;&nbsp;</TD>
							<TD nowrap>
								<INPUT type="text" name="naissance" class="disabled" size="11" readonly value="<%=viewBean.getCi().getDateNaissance()%>">
								Geschlecht
								&nbsp;
								<INPUT type="text" name="sexe" class="disabled" size="11" readonly value="<%=viewBean.getCi().getSexeLibelle()%>">
								&nbsp;Heimatstaat &nbsp;
								<INPUT type="text" name="pays" class="disabled"	size="44" readonly value="<%=viewBean.getCi().getPaysFormate()%>">
							</TD>
						</TR>
						<tr>
							<td>	
								MZR-SZ &nbsp;
							</td>
							<td>
								<input type="text" size="3" readonly class="disabled" name="motif" value="<%=viewBean.getMotifArc()%>">
							</td>
						</tr>
						<tr>
							<td>
							Kasseneigener Hinweis
							</td>
							<td>
							<input type="text" readonly class="disabled"  size="23" name="refInterne" value="<%=viewBean.getReferenceInterne()%>">
							</td>
						</tr>
						<tr>
							<td>
							Abschlussdatum
							</td>
							<td>
							<input type="text" readonly class="disabled" size="7" name="dateCloture" value="<%=viewBean.getDateClotureFormat()%>">
							</td>
						</tr>
						<tr>
							<td>
							Auftragsdatum
							</td>
							<td>
							<input type="text" size="12" readonly class="disabled" name="dateOrdre" value="<%=viewBean.getDateOrdre()%>">
							</td>
						</tr>
							
						
						<input type="hidden" name="forRassemblementOuvertureId" value="<%=viewBean.getRassemblementOuvertureId()%>">
						<input type="hidden" name="forAssure" value="True">
	 					 
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>
