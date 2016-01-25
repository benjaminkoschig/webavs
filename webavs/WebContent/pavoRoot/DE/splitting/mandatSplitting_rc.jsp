<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ page import="globaz.globall.util.*" %>
<%
	idEcran = "CCI0017";
    globaz.pavo.db.splitting.CIMandatSplittingRCViewBean viewBean = (globaz.pavo.db.splitting.CIMandatSplittingRCViewBean)session.getAttribute ("viewBeanDossier");
	if(!viewBean.isModificationAllowedFromDossier()) {
		bButtonNew = false;
	}
	bButtonFind = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
bFind = true;
usrAction = "pavo.splitting.mandatSplitting.lister";
top.document.title = "Splitting - Gestion des mandats";
timeWaiting = 1;




</SCRIPT>
	<ct:menuChange displayId="options" menuId="splitting-navigation" showTab="options">
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDossierSplitting()%>"/>
		<ct:menuSetAllParams key="idDossierSplitting" value="<%=viewBean.getIdDossierSplitting()%>"/>
		<ct:menuSetAllParams key="idTiersAssure" value="<%=viewBean.getIdTiersAssure()%>"/>
		<ct:menuSetAllParams key="idTiersConjoint" value="<%=viewBean.getIdTiersConjoint()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Splittingsaufträge<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
                        <TR>
                                <TD nowrap width="60">Versicherte</TD>

								<% if(viewBean.ASSURE.equals(viewBean.getTypePersonne())) { %>
								<TD nowrap >
                                <INPUT type="text" name="idTiersAssureDummy" size="17" class="disabled" 
                                readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown( viewBean.getIdTiersAssure())%>">
								<INPUT type="text" name="idTiersAssureDummy" size="60" class="disabled" readonly value="<%=viewBean.getTiersAssureNomComplet()%>">
								<INPUT type="hidden" name="forIdTiersPartenaire" value="<%=viewBean.getIdTiersAssure()%>">
								</TD>
				    			<TD nowrap width="60">Heirat</TD>
                                    <TD nowrap>
                                <INPUT type="text" name="duree" size="24" class="disabled" readonly value="<%=viewBean.getDuree()%>">
                                </TD></TR>
                         <tr>
							<td>
								Geburtsdatum &nbsp;
							</td>
							<td colspan="4">
								<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceAss()%>">
								&nbsp;
								Geschlecht &nbsp;
								<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleAss()%>">
								Heimatstaat &nbsp;
								<input type="text" size = "50" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateAss()%>">
							</td>
					
						</tr>          
          				<tr>	
								<TD nowrap width="60">Ehepartner</TD>
                                <TD nowrap>
								<INPUT type="text" name="idTiersConjointDummy" size="17" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersConjoint())%>">
								<INPUT type="text" name="idTiersConjointDummy" size="60" class="disabled" readonly value="<%=viewBean.getTiersConjointNomComplet()%>">
								</TD>
						</tr>
						<tr>
							<td>
								Geburtsdatum &nbsp;
							</td>
							<td colspan="4">
								<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceConj()%>">
								&nbsp;
								Geschlecht &nbsp;
								<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleConj()%>">
								Heimatstaat &nbsp;
								<input type="text" size = "50" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateConj()%>">
							</td>
					
						</tr> 
								<% } else { %>
								<TD nowrap>
								<INPUT type="text" name="idTiersAssureDummy" size="17" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersConjoint())%>">
								<INPUT type="text" name="idTiersAssureDummy" size="60" class="disabled" readonly value="<%=viewBean.getTiersConjointNomComplet()%>">
								<input type="hidden" name="forIdTiersPartenaire" value="<%=viewBean.getIdTiersConjoint()%>">
								</TD>
				    			<TD nowrap width="60">Heirat</TD>
                                    <TD nowrap>
                                <INPUT type="text" name="duree" size="24" class="disabled" readonly value="<%=viewBean.getDuree()%>">
                                </TD>
                         <tr>
							<td>
								Geburtsdatum&nbsp;
							</td>
							<td colspan="4">
								<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceConj()%>">
								&nbsp;
								Geschlecht &nbsp;
								<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleConj()%>">
								Heimatstaat &nbsp;
								<input type="text" size = "50" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateConj()%>">
							</td>
					
						</tr> 
                                <TR>
								<TD nowrap width="60">Ehepartner</TD>
                                <TD nowrap>
								<INPUT type="text" name="idTiersConjointDummy" size="17" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersAssure())%>">
								<INPUT type="text" name="idTiersConjointDummy" size="60" class="disabled" readonly value="<%=viewBean.getTiersAssureNomComplet()%>">
								</TD>
						</TR>		
						<tr>
							<td>
								Geburtsdatum&nbsp;
							</td>
							<td colspan="4">
								<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceAss()%>">
								&nbsp;
								Geschlecht &nbsp;
								<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleAss()%>">
								Heimatstaat &nbsp;
								<input type="text" size = "50" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateAss()%>">
							</td>
					
						</tr>
              					<% } %>
								<INPUT type="hidden" name="forIdDossierSplitting" value="<%=viewBean.getIdDossierSplitting()%>">
                        
                        <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
            <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>