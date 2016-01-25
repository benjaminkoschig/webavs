<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PIJ0021";

rememberSearchCriterias = true;

String totalSansRestitutuion = (String)request.getAttribute("totalSansRestitutuion");
String totalRestitutuion = (String)request.getAttribute("totalRestitutuion");
String moisAnnee = (String)request.getAttribute("forMoisAnneeComptable");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "ij.annonces.annonce.lister";
	lastMoisAnneeValue = "<%=globaz.jade.client.util.JadeStringUtil.isEmpty(moisAnnee)?"":moisAnnee%>";
	
	function forMoisAnneeComptableChange(moisAnnee){
		if(moisAnnee.value != "" && moisAnnee.value != "null"){
			if(moisAnnee.value != lastMoisAnneeValue && lastMoisAnneeValue != ""){
				document.getElementById('resultatLabel').style.color = '#FFFFFF';
				document.getElementById('totalSansRestitutuion').style.color = '#FFFFFF';
				document.getElementById('totalRestitutuion').style.color = '#FFFFFF';
				document.getElementById('totalSansRestitutuionLabel').style.color = '#FFFFFF';
				document.getElementById('totalRestitutuionLabel').style.color = '#FFFFFF';
				lastMoisAnneeValue=moisAnnee.value;
			}else{
				document.all('blockBtnCalculer').style.display = 'block';
				lastMoisAnneeValue=moisAnnee.value;
			}
		}else{
			document.all('blockBtnCalculer').style.display = 'none';
			lastMoisAnneeValue="";
		}
	}
	
	function calculer(){
		document.all('isActionCalculer').value="true";
	    document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_ANNONCE%>.chercher";
	    document.forms[0].target = "fr_main";
	    document.forms[0].submit();
	}
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_ANNONCES"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<ct:FWLabel key="JSP_ANNONCES_POUR_MOIS_ANNEE_COMPTABLE"/>
								<INPUT type="hidden" name="isActionCalculer" value="fasle">
							</TD>
							<TD>
								<ct:FWCalendarTag name="forMoisAnneeComptable" value='<%=globaz.jade.client.util.JadeStringUtil.isEmpty(moisAnnee)?"":moisAnnee%>' displayType="MONTH"/>
								<SCRIPT language="javascript">
									fmac = document.getElementById("forMoisAnneeComptable");
 	  								fmac.onchange=function() {forMoisAnneeComptableChange(this);}
								</SCRIPT>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_ETAT"/></TD>
							<TD>
								<SELECT name="forCsEtat">
									<OPTION value="<%=globaz.ij.api.annonces.IIJAnnonce.CS_OUVERT%>"><ct:FWLabel key="JSP_OUVERT"/></OPTION>
									<OPTION value="<%=globaz.ij.api.annonces.IIJAnnonce.CS_VALIDE%>"><ct:FWLabel key="JSP_VALIDE"/></OPTION>
									<OPTION value="<%=globaz.ij.api.annonces.IIJAnnonce.CS_ERRONEE%>"><ct:FWLabel key="JSP_ERRONE"/></OPTION>
									<OPTION value="<%=globaz.ij.api.annonces.IIJAnnonce.CS_ENVOYEE%>"><ct:FWLabel key="JSP_ENVOYE"/></OPTION>
									<OPTION value="<%=globaz.ij.api.annonces.IIJAnnonce.ETATS_NON_ENVOYE%>" selected="selected"><ct:FWLabel key="JSP_NON_ENVOYE"/></OPTION>
								</SELECT>
							</TD>
						</TR>
						<TR><TD colspan="2">&nbsp;</TD></TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_TRIER_PAR"/></TD>
							<TD>
								<SELECT name="orderBy"> 
									<OPTION value="<%=globaz.ij.db.annonces.IJAnnonce.FIELDNAME_MOISANNEECOMPTABLE%>"><ct:FWLabel key="JSP_ANNONCES_MOIS_ANNEE_COMPTABLE"/></OPTION>
									<OPTION value="<%=globaz.ij.db.annonces.IJAnnonce.FIELDNAME_NOASSURE%>" selected="selected"><ct:FWLabel key="JSP_NSS_ABREGE"/></OPTION>
								</SELECT>
							</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<TABLE width="90%" align="left">
					<TBODY id="blockBtnCalculer" style="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(totalSansRestitutuion)?"display: none;":"display: block;"%>">
						<TR bgcolor="#FFFFFF">
							<TD>
								<INPUT type="button" name="btnCalculer" value="<ct:FWLabel key="JSP_CALCULER_TOTAUX"/>" onclick="calculer();">
							</TD>
							<TD id="resultatLabel" style="color=<%=globaz.jade.client.util.JadeStringUtil.isEmpty(totalSansRestitutuion)?"#FFFFFF":"black"%>">
									<ct:FWLabel key="JSP_MONTANT_TOTAL_ALLOCATION"/> <ct:FWLabel key="JSP_NON_ERRONEES"/> <%=moisAnnee%> :
							</TD>
							<TD id="totalSansRestitutuion" align="right" style="color=<%=globaz.jade.client.util.JadeStringUtil.isEmpty(totalSansRestitutuion)?"#FFFFFF":"black"%>">
								<%=totalSansRestitutuion%>
							</TD>
							<TD id="totalSansRestitutuionLabel" style="color=<%=globaz.jade.client.util.JadeStringUtil.isEmpty(totalSansRestitutuion)?"#FFFFFF":"black"%>">
								<ct:FWLabel key="JSP_QUESTIONNAIRES_DUPLICATAS_RETROACTIFS"/>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD colspan="2">
								&nbsp;
							</TD>
							<TD id="totalRestitutuion" align="right" style="color=<%=globaz.jade.client.util.JadeStringUtil.isEmpty(totalSansRestitutuion)?"#FFFFFF":"black"%>">
								<%=totalRestitutuion%>
							</TD>
							<TD id="totalRestitutuionLabel" style="color=<%=globaz.jade.client.util.JadeStringUtil.isEmpty(totalSansRestitutuion)?"#FFFFFF":"black"%>">
								<ct:FWLabel key="JSP_RESTITUTIONS"/>
							</TD>
						</TR>
					</TBODY>
				</TABLE>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>