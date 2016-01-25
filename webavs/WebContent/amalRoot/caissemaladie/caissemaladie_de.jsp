	<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="ch.globaz.pyxis.business.model.AdresseComplexModel"%>
<%@page import="globaz.jade.log.JadeLogger"%>
<%@page import="globaz.amal.vb.caissemaladie.AMCaissemaladieViewBean"%>
<%@page import="globaz.amal.utils.AMParametresHelper"%>
<%@page import="ch.globaz.amal.business.constantes.IAMParametres"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>	
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="java.util.Date" %>

<%
	idEcran = "AM3002";
	//View bean depuis la requte
	AMCaissemaladieViewBean viewBean = (AMCaissemaladieViewBean) session.getAttribute("viewBean");

	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));

	autoShowErrorPopup = true;

	// Get the selected Tab Id (open the correct tab when validate, cancel, etc from a detail form)
	String selectedTabId = request.getParameter("selectedTabId");
	if(null == selectedTabId || selectedTabId.length()<=0){
		selectedTabId = "0";
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/DefaultTableAjax.js"/></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css" rel="stylesheet"/>
<%-- tpl:put name="zoneScripts" --%>

<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId()%>";
	var ACTION_CAISSE_MALADIE="<%=IAMActions.ACTION_CAISSEMALADIE%>";
	var ACTION_AJAX_PRIMES_ASSURANCE="<%=IAMActions.ACTION_PRIMES_ASSURANCEAJAX%>";

	function upd() {
	}
		
	function add() {
	}
	function init() {
		
	}
	
	$(document).ready(function() {
		$( '#tabs' ).tabs({ 
			selected: <%=selectedTabId%>, 
			cache: true,	
			load: function (event, ui) {
				var $uiPanle = $(ui.panel)
				notationManager.addNotationOnFragment($uiPanle);
			}
		});
		
		$('*',document.forms[0]).each(function(){
			if(this.name!=null && this.id==""){
				this.id=this.name;
			}
		});		
	});
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
Détail caisse maladie - <%=viewBean.getCaisseMaladie().getNomCaisse() %>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>		
		<td colspan="4">	
			<table width="100%">
				<tr>
					<td align="left" style="vertical-align:top">
						<input type="hidden" name="idTiersCaisse" id="idTiersCaisse" value="<%=viewBean.getCaisseMaladie().getIdTiersCaisse() %>" />											
						<input type="hidden" name="noCaisseMaladie" id="noCaisseMaladie" value="<%=viewBean.getCaisseMaladie().getNumCaisse() %>" />
						<div class="conteneurCaisseMaladie">
						<div class="subtitle">Détails caisse maladie</div>	
							<div class="infosCaisseMaladie">
								<table>
									<col width="122px" align="left"></col>
									<col width="*" align="left"></col>
									<col width="46px" align="right"></col>
									<tr style="height:20px">								
										<td>N&deg; caisse</td>
										<td style="background-color:#FFFFFF;">
											<%=viewBean.getCaisseMaladie().getNumCaisse() %>
										</td>
										<td style="background-color:#FFFFFF;">
											<a id="link_adm" href="<%=servletContext + "/pyxis?userAction=pyxis.tiers.administration.afficher&selectedTabId="+selectedTabId+"&selectedId="+viewBean.getCaisseMaladie().getIdTiersCaisse()%>">
											<img
											width="18px"
											height="18px"
											src="<%=request.getContextPath()%>/images/amal/link.png" title="Tiers" border="0">
											</a>			
										</td>
									</tr>
									<tr style="height:20px">
										<td>Nom caisse</td>
										<td style="background-color:#FFFFFF;" colspan="2">
											<%=viewBean.getCaisseMaladie().getNomCaisse() %>																								
										</td>
									</tr>
									<tr style="height:20px">
										<td>Id SEDEX</td>
										<td colspan="2" style="background-color:#FFFFFF;">
											<%
												String idSedex = "";
												try {
													idSedex = AMSedexRPUtil.getSedexIdFromIdTiers(viewBean.getCaisseMaladie().getIdTiersCaisse()) ;
												} catch (Exception e) {
													idSedex = "-";
												}
											%>			
											<%=idSedex %>
										</td>			
									</tr>
									<%if (!JadeStringUtil.isBlankOrZero(viewBean.getCaisseMaladie().getSedexYear())) { %>
										<tr style="height:20px">
											<td>Année d'annonce</td>
											<td colspan="2" style="background-color:#FFFFFF;">
												<%=viewBean.getCaisseMaladie().getSedexYear() %>	
											</td>			
										</tr>		
									<% } %>
									<tr style="background-color:#D7E4FF">
										<td colspan="3" style="height:22px;border-bottom: 1px solid #B3C4DB" align="right">			
									</td>
									</tr>
									<tr >
										<td style="vertical-align:top;">Adresse</td>
										<td style="background-color:#FFFFFF; font-size:11px;" colspan="2">
										<%
										String adresse = "";
										if(viewBean.getAdresse()!=null && viewBean.getAdresse().getAdresse()!=null){
											String adTitre = viewBean.getAdresse().getAdresse().getAdresse().getTitreAdresse();
											String adLigne1 = viewBean.getAdresse().getAdresse().getAdresse().getLigneAdresse1();
											String adLigne2 = viewBean.getAdresse().getAdresse().getAdresse().getLigneAdresse2();
											String adLigne3 = viewBean.getAdresse().getAdresse().getAdresse().getLigneAdresse3();
											String adLigne4 = viewBean.getAdresse().getAdresse().getAdresse().getLigneAdresse4();
											String adAttention = viewBean.getAdresse().getAdresse().getAdresse().getAttention();
											String adRue = viewBean.getAdresse().getAdresse().getAdresse().getRue();
											String adNo = viewBean.getAdresse().getAdresse().getAdresse().getNumeroRue();
											String adCase = viewBean.getAdresse().getAdresse().getAdresse().getCasePostale();
											String adNPA = viewBean.getAdresse().getAdresse().getLocalite().getNumPostal();
											adNPA = (JadeStringUtil.isBlankOrZero(adNPA)?null:adNPA.substring(0,4));
											String adLocalite = viewBean.getAdresse().getAdresse().getLocalite().getLocalite();
											
											adresse += (JadeStringUtil.isBlankOrZero(adTitre)?"":adTitre+"<br>");
											adresse += (JadeStringUtil.isBlankOrZero(adLigne1)?"":adLigne1+"<br>");
											adresse += (JadeStringUtil.isBlankOrZero(adLigne2)?"":adLigne2+"<br>");
											adresse += (JadeStringUtil.isBlankOrZero(adLigne3)?"":adLigne3+"<br>");
											adresse += (JadeStringUtil.isBlankOrZero(adLigne4)?"":adLigne4+"<br>");
											adresse += (JadeStringUtil.isBlankOrZero(adAttention)?"":adAttention+"<br>");
											adresse += (JadeStringUtil.isBlankOrZero(adRue)?"":adRue);
											adresse += (JadeStringUtil.isBlankOrZero(adNo)?"":" "+adNo+"<br>");
											adresse += (JadeStringUtil.isBlankOrZero(adCase)?"":"Case postale "+adCase+"<br>");
											adresse += (JadeStringUtil.isBlankOrZero(adNPA)?"":adNPA);
											adresse += (JadeStringUtil.isBlankOrZero(adLocalite)?"":" "+adLocalite);
										}
										if(!JadeStringUtil.isBlankOrZero(adresse)){
											%>
											<%=adresse%>
										<%}else{%>
											N/A
										<%
										}	
										%>
										</td>
									</tr>
								</table>
							</div>
						</div>
					</td>
					<td></td>
					<td align="right" style="vertical-align: top;">
						<div class="conteneurGroupeCaisseMaladie">
						<div class="subtitle">Détails Groupe</div>	
							<div class="infosCaisseMaladie">
								<table>
									<col width="122px" align="left"></col>
									<col width="*" align="left"></col>
									<col width="46px" align="right"></col>
									<tr style="height:20px">								
										<td>N&deg;</td>
										<td style="background-color:#FFFFFF;">
											<%=viewBean.getCaisseMaladie().getNumGroupe() %>
										</td>
										<td style="background-color:#FFFFFF;">
											<a href="<%=servletContext + "/pyxis?userAction=pyxis.tiers.administration.afficher&selectedId="+viewBean.getCaisseMaladie().getIdTiersGroupe()%>">
											<img
											width="18px"
											height="18px"
											src="<%=request.getContextPath()%>/images/amal/link.png" title="Tiers" border="0">
											</a>			
										</td>
									</tr>
									<tr style="height:20px">
										<td>Nom</td>
										<td style="background-color:#FFFFFF;" colspan="2">
											<%=viewBean.getCaisseMaladie().getNomGroupe() %>																								
										</td>
									</tr>
									<tr style="height:20px">
										<td></td>
										<td colspan="2" style="background-color:#FFFFFF;">			
										</td>			
									</tr>								
								</table>
							</div>
						</div>								
					</td>
					<td></td>
				</tr>
			</table> 
			<div>&nbsp;</div>
			<div>&nbsp;</div>	
			<style type="text/css">
				#tabs {
					padding: 0;
					background : #B3C4DB;
				}
				
				#tabs ul {
					border:0;
					padding:0;
					margin:0 0 0 5px;	
					background-color : #B3C4DB;
					background: url("<%=servletContext%><%=(mainServletPath+"Root")%>/css/images/ui-bg_highlight-soft_0_b3c4db_1x100.png") repeat-x scroll 50% 50% #B3C4DB; 				
				}
				
				#tabs li {
					border: 0;
				}
				
				#conteneurPrimes, #conteneurAnnonces {
					padding:0;
					margin:0;
					background: url("amalRoot/css/images/ui-bg_highlight-soft_0_b3c4db_1x100.png") repeat-x scroll 50% 50% #B3C4DB;
				}
			</style>
			<div id="tabs">
				<ul style="font-size:13px;font-style:italic" >
					<li><a href="#conteneurAnnonces">Annonces</a></li>
					<li><a href="amal?userAction=amal.primesassurance.primesAssuranceAjax.afficherAJAX&page=true&id=0&selectedTabId=1">Primes</a></li>
				</ul>

				<div id="conteneurAnnonces">
					<%@ include file="/amalRoot/annoncesassurance/annoncesassurance_de.jsp" %>		
				</div>
		
			</div>				
		</TD>
	</TR>
<%-- /tpl:insert --%>
<!-- FIN ZONE PRINCIPALE TITLE AND BODY -->
<!-- ************************************************************************************* -->



<!-- ************************************************************************************* -->
<!-- ZONE COMMON BUTTON AND END OF PAGE -->

<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="amal-menuprincipal" showTab="menu" />
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>

<!-- ************************************************************************************* -->
<!-- FIN ZONE COMMON BUTTON AND END OF PAGE -->