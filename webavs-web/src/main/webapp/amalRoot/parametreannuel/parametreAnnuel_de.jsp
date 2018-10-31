	<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@page import="globaz.amal.vb.parametreannuel.AMParametreAnnuelViewBean"%>
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
	//Les labels de cette page commencent par le préfix "JSP_PC_RENTEAVSAI"
	idEcran = "AM1003";
	//View bean depuis la requte
	AMParametreAnnuelViewBean viewBean = (AMParametreAnnuelViewBean) session.getAttribute("viewBean");

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
	var ACTION_AJAX_PARAMETRE_ANNUEL="<%=IAMActions.ACTION_PARAMETRES_PARAMETRE_ANNUELAJAX%>";

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
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/parametreannuel/ParametreAnnuel_MembrePart.js"/></script>
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
						
						<!-- Pour que le bord haut des onglets ne soit pas masqué -->
						<div style="padding-bottom:5px;visible:hidden"></div>	
						
						<!-- *** Menu à onglets *** -->
						<%=AMParametresHelper.getOngletHtml(objSession,viewBean,IAMParametres.ONGLETS_PARAMETRES,request,servletContext + mainServletPath)%>
						<!-- ***  /menu onglets  **** -->
						
						<div class="areaMembre">
						<div class="areaSearch">
								<table>
									<tr>
										<td width="60px"><ct:FWLabel key="JSP_AM_PARAM_PARAMETRE_ANNUEL_D_ANNEE"/></td>
										<td><input type="text" name="s_annee" id="s_annee" value="" size="4" data-g-integer="sizeMax:6" maxlength="4"/></td>
										<td></td>
										<td></td>
									</tr>
									<tr>
										<td width="60px"><ct:FWLabel key="JSP_AM_PARAM_PARAMETRE_ANNUEL_D_TYPE"/></td>
										<td><ct:FWCodeSelectTag codeType="AMPARAM" wantBlank="true" name="s_type" defaut=""/></td>
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
											<th width="70px" data-orderKey="annee"><ct:FWLabel key="JSP_AM_PARAM_PARAMETRE_ANNUEL_D_ANNEE"/></th>
											<th class="notSortable"><ct:FWLabel key="JSP_AM_PARAM_PARAMETRE_ANNUEL_D_TYPE"/></th>											
											<th data-orderKey="valeurParametre"><ct:FWLabel key="JSP_AM_PARAM_PARAMETRE_ANNUEL_D_VALEUR"/></th>
											<th data-orderKey="valeurParametreString" width="300"><ct:FWLabel key="JSP_AM_PARAM_PARAMETRE_ANNUEL_D_VALEUR"/> (chaîne)</th>												
										</tr>
									</thead>
									<tbody>
										<!-- Ici viendra le tableau des résultats -->					
									</tbody>
								</table>
								<div class="areaDetail">								
									<table>
										<TR>
											<TD style="width:140px"><ct:FWLabel key="JSP_AM_PARAM_PARAMETRE_ANNUEL_D_TYPE"/></TD>
											<TD colspan= "5"><ct:select name="codeTypeParametre" wantBlank="false" notation="data-g-select='mandatory:true'">
													<ct:optionsCodesSystems csFamille="AMPARAM"/>
												</ct:select></TD>										
										</TR>					
										<TR>
											<TD><ct:FWLabel key="JSP_AM_PARAM_PARAMETRE_ANNUEL_D_ANNEE"/></TD>
											<TD><input tabindex="2" type="text" name="simpleParametreAnnuel.anneeParametre" id="anneeParametre" value="<%=viewBean.getSimpleParametreAnnuel().getAnneeParametre()%>" class="numeroCourt" data-g-integer="sizeMax:4"/></TD>
											<TD style="width:100px;text-align:right;"><ct:FWLabel key="JSP_AM_PARAM_PARAMETRE_ANNUEL_D_FROM"/></TD>
											<TD><input tabindex="3" type="text" name="simpleParametreAnnuel.valeurFrom" id="valeurFrom" value="<%=viewBean.getSimpleParametreAnnuel().getValeurFrom()%>" data-g-amount=" " class="montant"/></TD>
											<TD style="width:100px;text-align:right;"><ct:FWLabel key="JSP_AM_PARAM_PARAMETRE_ANNUEL_D_TO"/></TD>
											<TD><input tabindex="4" type="text" name="simpleParametreAnnuel.valeurTo" id="valeurTo" value="<%=viewBean.getSimpleParametreAnnuel().getValeurTo()%>" data-g-amount=" " class="montant""/></TD>
										</TR>
										<TR>
											<TD><ct:FWLabel key="JSP_AM_PARAM_PARAMETRE_ANNUEL_D_VALEUR"/> (numérique)</TD>
											<TD colspan="5"><input tabindex="5" type="text" name="simpleParametreAnnuel.valeurParametre" id="valeurParametre" value="<%=viewBean.getSimpleParametreAnnuel().getValeurParametre()%>" data-g-amount=" " class="montant"/></TD>
										</TR>
										<TR>										
											<TD><ct:FWLabel key="JSP_AM_PARAM_PARAMETRE_ANNUEL_D_VALEUR"/> (chaîne)</TD>
											<TD colspan="5"><input tabindex="6" type="text" name="simpleParametreAnnuel.valeurParametreString" id="valeurParametreString" value="<%=viewBean.getSimpleParametreAnnuel().getValeurParametreString()%>" /></TD>
										</TR>
									</table>
									<div align="right" class="btnAjax">
									<ct:ifhasright element="<%=IAMActions.ACTION_PARAMETRES_PARAMETRE_ANNUELAJAX%>" crud="cud">
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