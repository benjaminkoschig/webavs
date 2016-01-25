
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ page import="globaz.globall.util.*" %>
<%
	idEcran = "CCI0020";
	globaz.pavo.db.splitting.CIDossierSplittingViewBean viewBean = (globaz.pavo.db.splitting.CIDossierSplittingViewBean)session.getAttribute ("viewBeanDossier");
	subTableHeight = 50;
	bButtonFind = false;
	bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT>
bFind= true;
usrAction = "pavo.splitting.domicileSplitting.lister";
detailLink = servlet+"?userAction=pavo.splitting.domicileSplitting.afficher&_method=add";
top.document.title = "IK - Anzeige der Auslandaufenthalte des Versicherten";
timeWaiting = 1;

</SCRIPT>
	<ct:menuChange displayId="options" menuId="splitting-navigation" showTab="options">
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDossierSplitting()%>"/>
		<ct:menuSetAllParams key="idDossierSplitting" value="<%=viewBean.getIdDossierSplitting()%>"/>
		<ct:menuSetAllParams key="idTiersAssure" value="<%=viewBean.getIdTiersAssure()%>"/>
		<ct:menuSetAllParams key="idTiersConjoint" value="<%=viewBean.getIdTiersConjoint()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Anzeige der Auslandaufenthalte des Versicherten<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR>
                                <TD nowrap width="60">Versicherte</TD>

								<% if(viewBean.ASSURE.equals(viewBean.getTypePersonne())) { %>
								<TD nowrap>
                                <INPUT type="text" name="idTiersAssureDummy" size="17" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersAssure())%>">
								<INPUT type="text" name="idTiersAssureDummy" size="73" class="disabled" readonly value="<%=viewBean.getTiersAssureNomComplet()%>">
								<INPUT type="hidden" name="forIdTiersPartenaire" value="<%=viewBean.getIdTiersAssure()%>">
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
										&nbsp;Heimatstaat &nbsp;
										<input type="text" size = "45" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateAss()%>">
									</td>
							
								</tr>   
				    			<TR>
								<TD nowrap width="60">Ehepartner</TD>
                                <TD nowrap">
								<INPUT type="text" name="idTiersConjointDummy" size="17" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersConjoint())%>">
								<INPUT type="text" name="idTiersConjointDummy" size="73" class="disabled" readonly value="<%=viewBean.getTiersConjointNomComplet()%>">
								</TD>
								</TR>
								<tr>
								<td>
									Geburtsdatum&nbsp;
								</td>
								<td colspan="4">
									<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceConj()%>">
									&nbsp;
									Geschlecht &nbsp;
									<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleConj()%>">
									&nbsp;Heimatstaat &nbsp;
									<input type="text" size = "45" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateConj()%>">
								</td>
								</tr> 
								<% } else { %>
								<TD nowrap>
								<INPUT type="text" name="idTiersAssureDummy" size="17" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersConjoint())%>">
								<INPUT type="text" name="idTiersAssureDummy" size="73" class="disabled" readonly value="<%=viewBean.getTiersConjointNomComplet()%>">
								<input type="hidden" name="forIdTiersPartenaire" value="<%=viewBean.getIdTiersConjoint()%>">
								</TD>
			
				    			<tr>
							<td>
								Geburtsdatum &nbsp;
							</td>
							<td colspan="4">
								<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceConj()%>">
								&nbsp;
								Geschlecht &nbsp;
								<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleConj()%>">
								&nbsp;Heimatstaat &nbsp;
								<input type="text" size = "45" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateConj()%>">
							</td>
					
						</tr> 
				    			
				    			<TR>
								<TD nowrap width="60">Ehepartner</TD>
                                <TD nowrap>
								<INPUT type="text" name="idTiersConjointDummy" size="17" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersAssure())%>">
								<INPUT type="text" name="idTiersConjointDummy" size="73" class="disabled" readonly value="<%=viewBean.getTiersAssureNomComplet()%>">
								</TD>
								<tr>
									<td>
										Geburtsdatum&nbsp;
									</td>
									<td colspan="4">
										<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceAss()%>">
										&nbsp;
										Geschlecht &nbsp;
										<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleAss()%>">
										&nbsp;Heimatstaat &nbsp;
										<input type="text" size = "45" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateAss()%>">
									</td>
							
								</tr>
						
              					<% } %>
								<INPUT type="hidden" name="forIdDossierSplitting" value="<%=viewBean.getIdDossierSplitting()%>">
        
          <%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> 
      
      <%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>