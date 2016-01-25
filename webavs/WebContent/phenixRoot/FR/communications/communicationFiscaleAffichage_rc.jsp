<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@page import="globaz.pyxis.constantes.IConstantes"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	
	idEcran="CCP1002";
	actionNew = "?userAction=phenix.communications.communicationFiscale.afficher&_method=add";
	rememberSearchCriterias=true;
	int autoDigiAff = globaz.phenix.util.CPUtil.getAutoDigitAff(session);
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	
	String numAffilie = request.getParameter("likeNumAffilie");
	bButtonNew = false;
	rememberSearchCriterias=true;
%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<script>
</script>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>
<SCRIPT language="Javascript">
	detailLink = servlet+"?userAction=phenix.communications.communicationFiscale.afficher";
	usrAction="phenix.communications.communicationFiscaleAffichage.lister";

	function postInit() {
	<% if( numAffilie != null) {%>
	
		document.getElementById("dateEnvoiVide").checked = false;
		document.getElementById("nonRecu").checked = false;
		
	<% } %>
	}
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Gestion des demandes de communications fiscales<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
				            <TD nowrap>N° Affilié&nbsp;&nbsp;</TD>
				            <!--      <TD width=50%>
								<INPUT type="text" name="likeNumAffilie"  class="libelleLong">
							</TD>  -->
							<td>
				            <ct:FWPopupList 
					           		name="likeNumAffilie" 
					           		value="" 
					           		className="libelleLong" 
					           		jspName="<%=jspLocation%>" 
					           		autoNbrDigit="<%=autoDigiAff%>" 
					           		size="20"
					           		minNbrDigit="3"
					       		/>
					       	</td>
					     	<TD nowrap>Canton&nbsp;&nbsp;</TD>
					     	<TD nowrap>
							<%
								java.util.HashSet except = new java.util.HashSet();
								except.add(IConstantes.CS_LOCALITE_ETRANGER);
							%>
							<ct:FWCodeSelectTag name="forCanton"
							              defaut=""
									wantBlank="<%=true%>"
							        codeType="PYCANTON"
									libelle="codeLibelle"
									except="<%=except%>"
							/>
					     	</TD>
					     	<td nowrap>Année&nbsp;&nbsp;</td>
							<TD width=50%>
								<INPUT name="forAnneeDecision" class="numeroCourt"/>
							</TD>
					    </TR>
						<TR>
				            <td nowrap>N° Contribuable&nbsp;&nbsp;</td>
							<TD width=50%>
								<INPUT name="likeNumContri" class="libelleLong"/>
							</TD>
							<TD nowrap>Non envoyée&nbsp;&nbsp;&nbsp;</TD>
					     	<TD nowrap>
								<input type="checkbox" name="dateEnvoiVide" checked="checked">
					     	</TD> 
					     	<TD nowrap>Non reçue&nbsp;&nbsp;&nbsp;</TD>
					     	<TD nowrap>
								<input type="checkbox" name="nonRecu" checked="checked">
					     	</TD> 
					     	
					     	<TD nowrap>
								<input type="hidden" name="withAnneeEnCours" value="<%=Boolean.TRUE %>">
						 	</TD> 
				          </TR>
				         <TR>
				            <TD nowrap>Nom&nbsp;&nbsp;</TD>
				            <TD width=50%>
								<INPUT type="text" name="likeNomPrenom"  class="libelleLong">
							</TD>
				           <TD nowrap>Non comptabilisée</TD>
					     	<TD nowrap>
								<input type="checkbox" name="exceptComptabilise" checked="checked">
					     	</TD> 
					     	<TD nowrap>Annulée</TD>
					     	<TD nowrap>
								<input type="checkbox" name="demandeAnnulee">
					     	</TD> 
					     
					     
				          </TR>
				        <tr>
				        <TD nowrap width="30%" >Trié par</TD>
					        <TD nowrap width="70%" >
								<SELECT name="trierPar"    class="libelleLong" >
									<OPTION selected="selected" value='ORDER_BY_AFFILIE'>Affilié</OPTION>
									<OPTION value='ORDER_BY_CONTRIBUABLE'>Contribuable</OPTION>
									<OPTION value='ORDER_BY_NOM_PRENOM'>Nom, Prénom</OPTION>
									<OPTION value='ORDER_BY_ANNEE'>Année</OPTION>
								</SELECT>
							</TD>
				            <TD nowrap>N°IFD&nbsp;&nbsp;</TD>
				            <TD nowrap>
								<INPUT type="text" name="forNumIfd"  class="libelleCourt">
							</TD>
							<TD nowrap width="100">genre</TD>
				            <TD nowrap>
				             <%
								java.util.HashSet except2 = new java.util.HashSet();
								except2.add(globaz.phenix.db.principale.CPDecision.CS_FICHIER_CENTRAL);
								except2.add(globaz.phenix.db.principale.CPDecision.CS_ETUDIANT);
								except2.add(globaz.phenix.db.principale.CPDecision.CS_NON_SOUMIS);
							%>
				            <ct:FWCodeSelectTag name="forGenreAffilie"
								defaut=""
								wantBlank="<%=true%>"
							    codeType="CPGENDECIS"
							     except="<%=except2%>"
								/>
				            </TD>
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