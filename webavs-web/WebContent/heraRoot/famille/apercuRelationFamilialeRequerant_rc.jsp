<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<%@ page import="globaz.commons.nss.*"%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%><script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
		<%@ page import="globaz.hera.tools.*"%>
		<%@ page import="globaz.hera.db.famille.*"%>



		<%
idEcran="GSF0001";
SFRequerantDTO requerant = (SFRequerantDTO)SFSessionDataContainerHelper.getData(session,SFSessionDataContainerHelper.KEY_REQUERANT_DTO);
boolean hasRequerant = false;
if (requerant!=null) {
	hasRequerant = true;
}
btnNewLabel = objSession.getLabel("JSP_MEMBRE_FAMILLE_NOUVEAU_REQUERANT");


java.util.Vector orderBy = new java.util.Vector(2);
orderBy.add(new String[] {"CASE WHEN " + SFMembreFamille.FIELD_TI_NOM +
                          " is not null THEN " + SFMembreFamille.FIELD_TI_NOM +
                          " ELSE " + SFMembreFamille.FIELD_NOM + " END" +
                          ", " +
                          "CASE WHEN " + SFMembreFamille.FIELD_TI_PRENOM +
                          " is not null THEN " + SFMembreFamille.FIELD_TI_PRENOM +
                          " ELSE " + SFMembreFamille.FIELD_PRENOM + " END"
                          , objSession.getLabel("JSP_MEMBRE_FAMILLE_NOM")});
orderBy.add(new String[] {SFMembreFamille.TABLE_AVS + "." + SFMembreFamille.FIELD_AVS_NOAVS, objSession.getLabel("JSP_MEMBRE_FAMILLE_NUMAVS")});

%>
	<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
		<ct:menuChange displayId="menu" menuId="sf-menuprincipal"
			showTab="menu" />
		<ct:menuChange displayId="options" menuId="sf-optionsempty" />

		<SCRIPT language="javascript">

<%if(hasRequerant){%>
	bFind = true;
<%} else {%>
	bFind = false;
<% } %>
	usrAction = "hera.famille.apercuRelationFamilialeRequerant.lister";


function updateForm(tag){
	if (tag.select && tag.select.selectedIndex != -1) {
		element = tag.select[tag.select.selectedIndex];
	}
}

function updateInput(){
}

</SCRIPT>
	<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					<ct:FWLabel key="JSP_MEMBRE_FAMILLE_TITLE" />
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%if(session.getAttribute(globaz.hera.tools.SFSessionDataContainerHelper.KEY_VALEUR_RETOUR)!=null){
							String urlBack = servletContext + mainServletPath + "?userAction=" + globaz.hera.servlet.ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + ".quitterApplication" ;
						%>
						<tr>
							<td colspan="6"><a href='<%=urlBack%>'><ct:FWLabel key="JSP_URL_FROM"/></a></td>
						</tr>
						<%
						}%>

						<%if(!JadeStringUtil.isBlankOrZero(request.getParameter("message"))){%>
							<tr>
								<TD colspan="6">
									<font color="#FF0000"><b><%=request.getParameter("message")%></b></font>
								</TD>
							</tr>
						<%}%>





						<%if(hasRequerant){%>
						<tr>

							<td><ct:FWLabel key="JSP_SF_DOMAINE"/></td>
							<td><input type="text" name="dummy" value="<%=objSession.getCodeLibelle(requerant.getIdDomaineApplication())%>" disabled/></td>
							<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_NUMAVS"/></td>
							<td>
								<input type="text" name="NoAvs" value="<%=requerant.getVisibleNoAvs()%>" disabled/>
							</td>
							<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_NOMPRENOM"/></td>

						<%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(requerant.getDateDeces())) {%>

								<td><input type="text" name="nomPrenomRequerant" value="<%=requerant.getNom()+" "+requerant.getPrenom()%>" disabled size="40" />
									<span style="font-family:wingdings">U</span> <%=requerant.getDateDeces()%>&nbsp;&nbsp;
								</td>
						<%} else {%>
								<td><input type="text" name="nomPrenomRequerant" value="<%=requerant.getNom()+" "+requerant.getPrenom()%>" disabled size="40" /></td>
						<%} %>
							
						<tr>
							<td><ct:FWLabel key="JSP_ENFANTS_DATEN"/></td>
							<td><input type="text" name="dateNaissance" value="<%=requerant.getDateNaissance()%>" disabled="disabled"/></td>
							<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_SEXE"/></td>
							<td><input type="text" name="sexe" value="<%=requerant.getLibelleSexe() %>" disabled="disabled"/></td>
							<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_NATIONALITE"/></td>
							<TD><input type="text" name="pays" value="<%=requerant.getLibelleNationalite() %>" disabled="disabled"/></td>
						</tr>
						<tr><td colspan="6"><hr/></td></tr>
						<%}%>
						<tr>
							<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_NUMAVS"/></td>
							<%	String params = "&provenance1=TIERS";
								params += "&provenance2=CI";
								String jspLocation = servletContext + "/heraRoot/numeroSecuriteSocialeSF_select.jsp"; %>

							<td>
								<ct1:nssPopup jspName="<%=jspLocation%>" avsMinNbrDigit="99" nssMinNbrDigit="99" params="<%=params%>" name="likeNumeroAvs" value=""/>
							</td>
							<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_NOM"/></td>
							<td><input type="text" name="likeNom" value=""/></td>
							<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_PRENOM"/></td>
							<td><input type="text" name="likePrenom" value=""/></td>
						</tr>

						<tr>
							<td><ct:FWLabel key="JSP_ENFANTS_DATEN"/></td>
							<td><ct:FWCalendarTag name="forDateNaissance" value=""/></td>
							<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_SEXE"/></td>
							<td><ct:FWCodeSelectTag name="forCsSexe" wantBlank="<%=true%>" codeType="PYSEXE" defaut=""/></td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>

						<tr>
						<%if(hasRequerant){%>
							<td><ct:FWLabel key="JSP_MEMBRE_FAMILLE_FAMILLEREQUERENT"/></td>
							<td><input type="checkbox" name="wantFamilleRequerant" <%if(hasRequerant) out.println("checked=\"checked\""); %>></td>
							<td>&nbsp;</td>
							<TD>&nbsp;</TD>
							<TD>&nbsp;</TD>
							<td>&nbsp;</td>
						<%}%>
						</tr>

						<TR>
							<TD><ct:FWLabel key="JSP_TRIER_PAR"/></TD>
							<TD><ct:FWListSelectTag data="<%=orderBy%>" defaut="" name="orderBy"/></TD>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</TR> 
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>