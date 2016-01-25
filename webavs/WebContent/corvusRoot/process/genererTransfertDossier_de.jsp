<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_LTD_D"

	idEcran="PRE2015";

	REGenererTransfertDossierViewBean viewBean = (REGenererTransfertDossierViewBean)session.getAttribute("viewBean");

	userActionValue=globaz.corvus.servlet.IREActions.ACTION_GENERER_TRANSFERT_DOSSIER+ ".executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.corvus.vb.process.REGenererTransfertDossierViewBean"%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JACalendar"%><ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu">
</ct:menuChange>

<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/ajaxUtils.js"></script>
<script type="text/javascript">
	
	function motifChange(motif){
		for (var i=0; i<motif.length;i++) {
		    if (motif[i].checked) {
		        if(motif[i].value==<%=REGenererTransfertDossierViewBean.MOTIF_RENTE_AVS_AI%>){
		        	document.getElementById('blockNomAssure').style.display='block';
				}else{
					document.getElementById('blockNomAssure').style.display='none';
					document.getElementById('nomAssure').value="";
				}
	        }
	     }
	}

	function limiteur()
    // limite la saisie de la remarque à 255 caractères
    {
       	maximum = 252;
    	if (document.forms[0].elements('remarque').value.length > maximum)
      		document.forms[0].elements('remarque').value = document.forms[0].elements('remarque').value.substring(0, maximum);
    }

	 function addTexteError(texteError){
	 		if(errorObj.text ==""){
	 			errorObj.text = texteError;
	 		}
	 		else{
	 			errorObj.text = errorObj.text+"<br>"+texteError;
	 		}
	 }

	$(document).ready(function() {
		var $boutonOk = $('#btnOk');
		$boutonOk.one('click', function () {
			var $this = $(this);
			$this.prop('disabled', true);

			ajaxUtils.addOverlay($('html'));
		});
	});
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><%=viewBean.getTitreEcran()%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<%if(viewBean.isTransfertCaisseCompetenteAndValidate()){ %>
						<TR>
							<TD><LABEL for="langueAssure"><ct:FWLabel key="TRANSFERT_DOSSIER_LANQUE_ASSURE"/></LABEL></TD>
							<TD><INPUT type="text" name="langueAssure" value="<%=viewBean.getLangueAssure()%>" class="disabled" readonly></TD>
						</TR>
						<TR>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<%}%>
						
						<TR>
							<TD><LABEL for="eMailAddress"><ct:FWLabel key="JSP_LTD_D_EMAIL"/></LABEL></TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?viewBean.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong"></TD>
						</TR>
						<TR>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
						
						<%if(viewBean.isTransfertCaisseCompetente()){ %>
							<TD><LABEL for="Remarque"><ct:FWLabel key="JSP_LTD_D_REMARQUE"/></LABEL></TD>
							<TD colspan="3">
							<textarea name="texteRemarque" cols="85" rows="3" onKeyDown="limiteur();"><%=viewBean.getTexteRemarque()%></textarea></TD>
						<%}%>
						</TR>
						<%if(viewBean.isDemandeCalculPrevisionnel()){ %>
							<TR>
								<TD><LABEL for="motif"><ct:FWLabel key="JSP_LTD_D_MOTIF"/></LABEL></TD>
								<td>
									<INPUT type="radio" name="motif" onclick="motifChange(this.form.motif);" value="<%=REGenererTransfertDossierViewBean.MOTIF_PERCEPTION_DERNIERES_COTI%>" checked="checked"> <ct:FWLabel key="JSP_LTD_D_PERCEPTION_DERNIERES_COTISATIONS"/>
								</td>
							</TR>
							<TR>	
								<td>&nbsp;</td>
								<TD>
									<INPUT type="radio" name="motif" onclick="motifChange(this.form.motif);" value="<%=REGenererTransfertDossierViewBean.MOTIF_DOMICILE_ETRANGER%>"> <ct:FWLabel key="JSP_LTD_D_RENTE_DOMICILIE_ETRANGER"/>
								</TD>
							</TR>
							<TR>	
								<td>&nbsp;</td>
								<TD>
									<INPUT type="radio" name="motif" onclick="motifChange(this.form.motif);" value="<%=REGenererTransfertDossierViewBean.MOTIF_RENTE_AVS_AI%>"> <ct:FWLabel key="JSP_LTD_D_RENTE_AVS_AI"/>
								</TD>
							</TR>
							<TR>
								<TBODY id="blockNomAssure" style="display:'none';">
									<TD><LABEL for="nomAssure"><ct:FWLabel key="JSP_LTD_D_NOM_ASSURE"/></LABEL></TD>
									<TD><INPUT type="text" name="nomAssure"/><ct:FWLabel key="JSP_LTD_D_EXEMPLE_NOM_ASSURE"/></TD>
								</TBODY>
							</TR>
						<%}%>
						
						<%if(viewBean.isTransfertCaisseCompetenteAndValidate()){ %>
							<TR>
								<TD><LABEL for="motif"><ct:FWLabel key="JSP_LTD_D_MOTIF_TRANSMISSION"/></LABEL></TD>
								<TD><INPUT type="text" name="motifTransmission" value="<%=viewBean.getMotifTransmission()%>" class="libelleLong"></TD>
							</TR>
							<TR>
								<TD colspan="2">&nbsp;</TD>
							</TR>
							<TR>
								<TD><LABEL for="Remarque"><ct:FWLabel key="JSP_LTD_D_REMARQUE"/></LABEL></TD>
								<TD colspan="3">
									<textarea name="remarqueTraEncous" cols="85" rows="3" onKeyDown="limiteur();"><%=viewBean.getRemarqueTraEncous()%></textarea>
								</TD>
							</TR>	
							<TR>
								<TD colspan="2">&nbsp;</TD>
							</TR>
							<TR>
								<TD><LABEL for="CessationDuPaiement"><ct:FWLabel key="JSP_LTD_D_CESSATIONPAIEMENT"/></LABEL></TD>
								<TD>
									<input	id="MoisCessationPaiement"
											name="MoisCessationPaiement"
											data-g-calendar="type:month"
											value="<%=viewBean.getMoisCessationPaiement()%>" />
								</TD>
							</TR>
							<TR>
								<TD colspan="2">&nbsp;</TD>
							</TR>
							<TR>
								<TD><LABEL for="DateEnvoi"><ct:FWLabel key="JSP_LTD_D_DATEENVOI"/></LABEL></TD>
								<TD>
									<input	id="DateEnvoi"
											name="DateEnvoi"
											data-g-calendar=" "
											value="<%=JadeStringUtil.isEmpty(viewBean.getDateEnvoi())?JACalendar.todayJJsMMsAAAA():viewBean.getDateEnvoi()%>" />
								</TD>
							</TR>
							<TR>
								<TD colspan="2">&nbsp;</TD>
							</TR>	
						<%}%>
						
						<%if ("1".equals(viewBean.getDisplaySendToGed())) { %> 
										<TR>
											<TD>&nbsp;</TD>
										</TR>
										<TR>
											<TD><ct:FWLabel key="JSP_ENVOYER_DANS_GED"/></TD>
											<TD>
												<INPUT type="checkbox" name="isSendToGed" value="on" CHECKED>
												<INPUT type="hidden" name="displaySendToGed" value="1">
											</TD>
										</TR>
										<TR>
											<TD>&nbsp;</TD>
										</TR>
									<% } else {%>
										<INPUT type="hidden" name="isSendToGed" value="FALSE">
										<INPUT type="hidden" name="displaySendToGed" value="0">
						<%} %>
						<input type="hidden" name="idInfoCompl" value="<%=viewBean.getIdInfoCompl()%>">
						
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>