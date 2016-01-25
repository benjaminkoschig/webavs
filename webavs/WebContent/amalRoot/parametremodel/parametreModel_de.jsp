<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@page import="globaz.amal.vb.parametremodel.AMParametreModelViewBean"%>
<%@page import="globaz.amal.utils.AMParametresHelper"%>
<%@page import="ch.globaz.amal.business.constantes.IAMParametres"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="java.util.Iterator"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>	
<%
	//Les labels de cette page commencent par le préfix "JSP_PC_RENTEAVSAI"
	idEcran = "AM00XX";
	//View bean depuis la requte
	AMParametreModelViewBean viewBean = (AMParametreModelViewBean) session.getAttribute("viewBean");

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
	var ACTION_AJAX_PARAMETRE_MODEL="<%=IAMActions.ACTION_PARAMETRES_PARAMETRE_DOCUMENTAJAX%>";

</script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/parametremodel/parametremodel_MembrePart.js"/></script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_AM_PARAM_DATES_SAISIES_TAXATION_D_TITRE"/>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
			<TR>		
				<td colspan="4">
					<div class="conteneurDF">
					
						<!-- *** Menu à onglets *** -->
						<%=AMParametresHelper.getOngletHtml(objSession,viewBean,IAMParametres.ONGLETS_PARAMETRES,request,servletContext + mainServletPath)%>
						<!-- ***  /menu onglets  **** -->
						
						<div class="areaMembre">
												
		<!-- 					<div class="areaSearch">
								<table border="0">
									<tr>
										<td width="120px">Date début</td>
										<td width="200px"><input type="text" name="s_dateDebut" id="s_dateDebut" value="" data-g-calendar=" "/></td>
										<td width="120px">Date taxation</td>
										<td><input type="text" name="s_dateTaxation" id="s_dateTaxation" value="" data-g-calendar=" "/></td>
									</tr>
									<tr>
										<td width="80px">Date fin</td>
										<td><input type="text" name="s_dateFin" id="s_dateFin" value="" data-g-calendar=" "/></td>
										<td></td>
										<td></td>
									</tr>
								</table>			
							</div>	
							<br>										
			-->										
							<!--  *** Membre unique de la famille *** -->
								<!--  Zone Area titre -->
								<table class="areaDataTable" width="100%">
									<thead><!--  en tete de table -->
										<tr>
											<th><ct:FWLabel key="JSP_AM_PARAM_DATES_SAISIES_TAXATION_D_DATE_DEBUT"/></th>
											<th><ct:FWLabel key="JSP_AM_PARAM_DATES_SAISIES_TAXATION_D_DATE_FIN"/></th>
											<th><ct:FWLabel key="JSP_AM_PARAM_DATES_SAISIES_TAXATION_D_DATE_TAXATION"/></th>
										</tr>
									</thead>
									<tbody>
										<!-- Ici viendra le tableau des résultats -->		
									</tbody>
								</table>
								<table class="areaDetail">
									<TR>
										<TD><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_ANNEE"/></TD>
										<TD><input tabindex="1" type="text" name="parametreModelComplex.simpleParametreModel.anneeValiditeDebut" id="anneeValiditeDebut" value="" data-g-calendar=""/></TD>														
									</TR>					
									<TR>
										<TD><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_LIMITEREVENU"/></TD>
										<TD><input tabindex="1" type="text" name="simpleDateSaisieTaxation.dateSaisieFin" id="dateSaisieFin" value="" data-g-calendar=""/></TD>
										<TD></TD>
										<TD></TD>							
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_SUBSIDEADULTE"/></TD>
										<TD><input tabindex="1" type="text" name="simpleDateSaisieTaxation.dateSaisieTaxation" id="dateSaisieTaxation" value="" data-g-calendar=""/></TD>
										<TD></TD>
										<TD></TD>							
									</TR>
								</table>
								<div align="right">
								<ct:ifhasright element="<%=IAMActions.ACTION_PARAMETRES_PARAMETRE_DOCUMENTAJAX%>" crud="cud">
									<input class="btnAjaxDelete" type="button" value="<%=btnDelLabel%>">									
									<input class="btnAjaxValidate" type="button" value="<%=btnValLabel%>">
									<input class="btnAjaxCancel" type="button" value="<%=objSession.getLabel("JSP_PC_SGL_D_ANNULER")%>">									
									<input class="btnAjaxUpdate" type="button" value="<%=btnUpdLabel%>">
									<input class="btnAjaxAdd" type="button" value="<%=btnNewLabel%>">
								</ct:ifhasright>
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