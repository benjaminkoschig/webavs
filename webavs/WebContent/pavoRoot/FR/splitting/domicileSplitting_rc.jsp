
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
top.document.title = "Splitting - Gestion des domiciles à l'étranger";
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
				<%-- tpl:put name="zoneTitle" --%>Aperçu des domiciles à l'étranger pour dossier de splitting<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR>
                                <TD nowrap width="60">Assuré</TD>

								<% if(viewBean.ASSURE.equals(viewBean.getTypePersonne())) { %>
								<TD nowrap>
                                <INPUT type="text" name="idTiersAssureDummy" size="17" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersAssure())%>">
								<INPUT type="text" name="idTiersAssureDummy" size="70" class="disabled" readonly value="<%=viewBean.getTiersAssureNomComplet()%>">
								<INPUT type="hidden" name="forIdTiersPartenaire" value="<%=viewBean.getIdTiersAssure()%>">
								</TD>
				    	</TR>
						<tr>
							<td>
								Date de naissance&nbsp;
							</td>
							<td colspan="4">
								<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceAss()%>">
								&nbsp;
								Sexe &nbsp;
								<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleAss()%>">
								Pays &nbsp;
								<input type="text" size = "50" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateAss()%>">
							</td>
						</tr>   
			    		<TR>
							<TD nowrap width="60">Conjoint</TD>
                               <TD>
							<INPUT type="text" name="idTiersConjointDummy" size="17" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersConjoint())%>">
							<INPUT type="text" name="idTiersConjointDummy" size="70" class="disabled" readonly value="<%=viewBean.getTiersConjointNomComplet()%>">
							</TD>
						</TR>
						<tr>
								<td>
									Date de naissance&nbsp;
								</td>
								<td colspan="4">
									<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceConj()%>">
									&nbsp;
									Sexe &nbsp;
									<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleConj()%>">
									Pays &nbsp;
									<input type="text" size = "50" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateConj()%>">
								</td>
							</tr> 
								<% } else { %>
								<TD>
								<INPUT type="text" name="idTiersAssureDummy" size="17" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersConjoint())%>">
								<INPUT type="text" name="idTiersAssureDummy" size="70" class="disabled" readonly value="<%=viewBean.getTiersConjointNomComplet()%>">
								<input type="hidden" name="forIdTiersPartenaire" value="<%=viewBean.getIdTiersConjoint()%>">
								</TD>
							</tr> 
				    		<tr>
							<td>
								Date de naissance&nbsp;
							</td>
						<td colspan="4">
							<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceConj()%>">
							&nbsp;
							Sexe &nbsp;
							<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleConj()%>">
							Pays &nbsp;
							<input type="text" size = "50" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateConj()%>">
						</td>
					
						</tr> 
				    	<TR>
								<TD nowrap width="60">Conjoint</TD>
                                <TD>
								<INPUT type="text" name="idTiersConjointDummy" size="17" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersAssure())%>">
								<INPUT type="text" name="idTiersConjointDummy" size="70" class="disabled" readonly value="<%=viewBean.getTiersAssureNomComplet()%>">
								</TD>
						</TR>		
						<tr>
							<td>
								Date de naissance&nbsp;
							</td>
							<td colspan="4">
								<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceAss()%>">
								&nbsp;
								Sexe &nbsp;
								<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleAss()%>">
								Pays &nbsp;
								<input type="text" size = "50" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateAss()%>">
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