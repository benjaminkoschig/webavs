	<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@page import="globaz.amal.vb.deductionsfiscalesenfants.AMDeductionsFiscalesEnfantsViewBean"%>
<%@page import="globaz.amal.utils.AMParametresHelper"%>
<%@page import="ch.globaz.amal.business.constantes.IAMParametres"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
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
	idEcran = "AM1004";
	//View bean depuis la requte
	AMDeductionsFiscalesEnfantsViewBean viewBean = (AMDeductionsFiscalesEnfantsViewBean) session.getAttribute("viewBean");

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
	var ACTION_AJAX_DEDUCTIONS_FISCALES_ENFANTS="<%=IAMActions.ACTION_PARAMETRES_DEDUCTIONS_FISCALES_ENFANTSAJAX%>";

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
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/deductionsfiscalesenfants/DeductionsFiscalesEnfants_MembrePart.js"/></script>
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
										<td width="60px"><ct:FWLabel key="JSP_AM_PARAM_PARAMETRE_ANNUEL_D_ANNEE"/></td>
										<td><input type="text" name="s_anneeTaxation" id="s_anneeTaxation" value="" size="4" data-g-integer="sizeMax:6" maxlength="4"/></td>
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
											<th width="150px">Ann?e taxation</th>
											<th width="150px" class="notSortable">Nombre d'enfants</th>											
											<th>Montant par enfant</th>
											<th>Montant total</th>
										</tr>
									</thead>
									<tbody>
										<!-- Ici viendra le tableau des r?sultats -->					
									</tbody>
								</table>		
								<div class="areaDetail">						
									<table>
										<TR>
											<TD>Ann?e taxation</TD>
											<TD><input tabindex="2" type="text" name="simpleDeductionsFiscalesEnfants.anneeTaxation" id="anneeTaxation" value="<%=viewBean.getSimpleDeductionsFiscalesEnfants().getAnneeTaxation()%>" class="numeroCourt" data-g-integer="sizeMax:4"/></TD>
											<TD></TD>
											<TD></TD>							
										</TR>
										<TR>
											<TD>Nombre enfant</TD>
											<TD><input tabindex="3" type="text" name="simpleDeductionsFiscalesEnfants.nbEnfant" id="nbEnfant" value="<%=viewBean.getSimpleDeductionsFiscalesEnfants().getNbEnfant()%>" data-g-amount=" " class="montant"/></TD>
											<TD></TD>
											<TD></TD>							
										</TR>
										<TR>										
											<TD>Montant par enfant</TD>
											<TD><input tabindex="4" type="text" name="simpleDeductionsFiscalesEnfants.montantDeductionParEnfant" id="montantDeductionParEnfant" value="<%=viewBean.getSimpleDeductionsFiscalesEnfants().getMontantDeductionParEnfant()%>" class="montant" data-g-amount=" "/></TD>
											<TD></TD>										
											<TD></TD>							
										</TR>
										<TR>										
											<TD>Montant total</TD>
											<TD><input tabindex="4" type="text" name="simpleDeductionsFiscalesEnfants.montantDeductionTotal" id="montantDeductionTotal" value="<%=viewBean.getSimpleDeductionsFiscalesEnfants().getMontantDeductionParEnfant()%>" class="montant" data-g-amount=" "/></TD>
											<TD></TD>
											<TD></TD>							
										</TR>
									</table>
									<div align="right" class="btnAjax">
									<ct:ifhasright element="<%=IAMActions.ACTION_PARAMETRES_DEDUCTIONS_FISCALES_ENFANTSAJAX%>" crud="cud">
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