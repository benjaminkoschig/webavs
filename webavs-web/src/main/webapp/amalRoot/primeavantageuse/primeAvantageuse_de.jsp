	<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@page import="globaz.amal.vb.primeavantageuse.AMPrimeAvantageuseViewBean"%>
<%@page import="globaz.amal.utils.AMParametresHelper"%>
<%@page import="ch.globaz.amal.business.constantes.IAMParametres"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>	
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
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
	//Les labels de cette page commencent par le pr?fix "JSP_PC_RENTEAVSAI"
	idEcran = "AM1002";
	//View bean depuis la requte
	AMPrimeAvantageuseViewBean viewBean = (AMPrimeAvantageuseViewBean) session.getAttribute("viewBean");

	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));

	autoShowErrorPopup = true;

	bButtonDelete = false;

	if (viewBeanIsNew) {
		// change "Valider" action pour
		//userActionValue
	} else {
		bButtonCancel = false;
		bButtonUpdate = false;
		bButtonValidate = false;
	}
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%>

<link rel="stylesheet" type="text/css" href="<%=servletContext+(mainServletPath+"Root")%>/css/parametres/amalparametres.css"/>
<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId()%>";
	var ACTION_AJAX_PRIME_AVANTAGEUSE="<%=IAMActions.ACTION_PARAMETRES_PRIME_AVANTAGEUSEAJAX%>";

	function upd() {
	}
		
	function add() {
	}
	
	$(document).ready(function() {
		$('*',document.forms[0]).each(function(){
			if(this.name!=null && this.id==""){
				this.id=this.name;
			}
		});		
	});
</script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/primeavantageuse/PrimeAvantageuse_MembrePart.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/ajax_amal.js"/></script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_AM_PARAM_PARAMETRE_ANNUEL_D_TITRE"/>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
			<TR>		
				<td colspan="4">
					<div class="conteneurDF">
						
						<!-- Pour que le bord haut des onglets ne soit pas masqu? -->
						<div style="padding-bottom:5px;visible:hidden"></div>	
						
						<!-- *** Menu ? onglets *** -->
						<%=AMParametresHelper.getOngletHtml(objSession,viewBean,IAMParametres.ONGLETS_PARAMETRES,request,servletContext + mainServletPath)%>
						<!-- ***  /menu onglets  **** -->
						
						<div class="areaMembre">
						<div class="areaSearch">
								<table>
									<tr>
										<td width="60px"><ct:FWLabel key="JSP_AM_PARAM_PRIMES_AVANTAGEUSES_D_ANNEE"/></td>
										<td><input type="text" name="s_annee" id="s_annee" value="" size="4" data-g-integer="sizeMax:6" maxlength="4"/></td>
										<td></td>
										<td></td>
									</tr>
								</table>	
								<div align="right">	
									<input type="button" class="bt_search" value="Rechercher" />
									<input type="button" class="bt_clear" value="Effacer" />
								</div>				
							</div>	
							<br>
						
							<!--  *** Membre unique de la famille *** -->
								<!--  Zone Area titre -->
								<table class="areaDataTable" width="100%">
									<thead><!--  en tete de table -->
										<tr>
											<th width="70px" data-orderKey="annee"><ct:FWLabel key="JSP_AM_PARAM_PRIMES_AVANTAGEUSES_D_ANNEE"/></th>
											<th data-orderKey="montantPrimeAdulte"><ct:FWLabel key="JSP_AM_PARAM_PRIMES_AVANTAGEUSES_D_MONTANT_ADULTE"/></th>											
											<th data-orderKey="montantPrimeFormation"><ct:FWLabel key="JSP_AM_PARAM_PRIMES_AVANTAGEUSES_D_MONTANT_FORMATION"/></th>												
											<th data-orderKey="montantPrimeEnfant"><ct:FWLabel key="JSP_AM_PARAM_PRIMES_AVANTAGEUSES_D_MONTANT_ENFANT"/></th>
										</tr>
									</thead>
									<tbody>
										<!-- Ici viendra le tableau des r?sultats -->					
									</tbody>
								</table>
								<div class="areaDetail">								
									<table>
										<TR>
											<TD><ct:FWLabel key="JSP_AM_PARAM_PRIMES_AVANTAGEUSES_D_ANNEE"/></TD>
											<TD><input tabindex="1" type="text" name="simplePrimeAvantageuse.anneeSubside" id="anneeSubside" value="<%=viewBean.getSimplePrimeAvantageuse().getAnneeSubside()%>" class="numeroCourt" data-g-integer="sizeMax:4"/></TD>
											<TD></TD>
											<TD></TD>							
										</TR>
										<TR>
											<TD><ct:FWLabel key="JSP_AM_PARAM_PRIMES_AVANTAGEUSES_D_MONTANT_ADULTE"/></TD>
											<TD><input tabindex="2" type="text" name="simplePrimeAvantageuse.montantPrimeAdulte" id="montantPrimeAdulte" value="<%=viewBean.getSimplePrimeAvantageuse().getMontantPrimeAdulte()%>" data-g-amount=" " class="montant"/></TD>
											<TD></TD>
											<TD></TD>							
										</TR>
										<TR>
											<TD><ct:FWLabel key="JSP_AM_PARAM_PRIMES_AVANTAGEUSES_D_MONTANT_FORMATION"/></TD>
											<TD><input tabindex="3" type="text" name="simplePrimeAvantageuse.montantPrimeFormation" id="montantPrimeFormation" value="<%=viewBean.getSimplePrimeAvantageuse().getMontantPrimeFormation()%>" data-g-amount=" " class="montant"/></TD>
											<TD></TD>
											<TD></TD>							
										</TR>
										<TR>
											<TD><ct:FWLabel key="JSP_AM_PARAM_PRIMES_AVANTAGEUSES_D_MONTANT_ENFANT"/></TD>
											<TD><input tabindex="4" type="text" name="simplePrimeAvantageuse.montantPrimeEnfant" id="montantPrimeEnfant" value="<%=viewBean.getSimplePrimeAvantageuse().getMontantPrimeEnfant()%>" data-g-amount=" " class="montant"/></TD>
											<TD></TD>
											<TD></TD>							
										</TR>
									</table>
									<div align="right" class="btnAjax">
									<ct:ifhasright element="<%=IAMActions.ACTION_PARAMETRES_PRIME_AVANTAGEUSEAJAX%>" crud="cud">
										<input class="btnAjaxDelete" type="button" value="<%=btnDelLabel%>">									
										<input class="btnAjaxValidate" type="button" value="<%=btnValLabel%>">
										<input class="btnAjaxCancel" type="button" value="<%=objSession.getLabel("JSP_AM_SGL_D_ANNULER")%>">									
										<input class="btnAjaxUpdate" type="button" value="<%=btnUpdLabel%>">
										<input class="btnAjaxAdd" type="button" value="<%=btnNewLabel%>">
									</ct:ifhasright>
									</div>
								</div>
						</div>
					</div>
				</TD>
			</TR>			
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>