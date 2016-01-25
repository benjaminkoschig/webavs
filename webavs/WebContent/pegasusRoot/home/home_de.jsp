	<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@page import="ch.globaz.pegasus.business.constantes.EPCProperties"%>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.constantes.EPCProperties"%>
<%@page import="globaz.pegasus.vb.home.PCHomeViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>
<%
	//Les labels de cette page commencent par le préfix "JSP_PC_PARAM_HOMES_D"
	idEcran="PPC0103";

	PCHomeViewBean viewBean = (PCHomeViewBean) session.getAttribute("viewBean");
	
	boolean viewBeanIsNew="add".equals(request.getParameter("_method"));
	
	autoShowErrorPopup = true;
	
	bButtonDelete = false;
	
	if(viewBeanIsNew){
		// change "Valider" action pour
		//userActionValue
	} else {
		bButtonCancel=false;
		bButtonUpdate=false;
		bButtonValidate=false;
	}
	String tiersOrAdmin = objSession.getApplication().getProperty(EPCProperties.HOME_TIERS_OR_ADMIN.getProperty());
	String descHome = viewBean.getHomeDescription().replaceAll("\\'","\\\\'");
%>

<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<ct:serializeObject variableName="home" objectName="viewBean.home.simpleHome" destination="javascript"/>
<ct:serializeObject variableName="homeAjaxViewBean" objectName="viewBean.ajaxViewBean" destination="javascript"/>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<%@page import="ch.globaz.pegasus.business.models.home.PeriodeServiceEtat"%><ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="pegasus-optionshomes">
	<ct:menuSetAllParams key="idHome" value="<%=viewBean.getHome().getSimpleHome().getIdHome()%>"/>
	<ct:menuSetAllParams key="nomHome" value="<%=descHome%>"/>
</ct:menuChange>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/home/home_de.css"/>

<script language="JavaScript">
	var ACTION_HOME="<%=IPCActions.ACTION_HOME%>";
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var ACTION_AJAX="<%=IPCActions.ACTION_HOME_AJAX%>";
	var ACTION_AJAX_PERIODE="<%=IPCActions.ACTION_HOME_PERIODE_AJAX%>";
	var PAGE_ID_HOME="<%=viewBean.getId() %>"; 
	var MAIN_URL="<%=formAction%>";
	var chercheTiersHint=["Désignation","NPA","Localité"];

	var urlTiers="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=";
	var globazGlobal = {};
	globazGlobal.isNew = <%= viewBeanIsNew %> ;
</script>
<% if(!viewBeanIsNew){%> 
	
<% } %>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/home/HomePart.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/home/PeriodesPart.js"/></script>
<!--  <script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/home/home_de.js"/></script>-->

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_PARAM_HOMES_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
			<TR>		
				<td colspan="4">
					<div>
						<fieldset><legend class="ui-widget-header"><ct:FWLabel key="JSP_PC_PARAM_HOMES_D_HOME"/></legend>  
						<table id="homeDetail" class="mainContainerAjax">
						<tr>
								<% if(tiersOrAdmin.equalsIgnoreCase("tiers")){%>
							<TD valign="top" width="149px"><ct:FWLabel key="JSP_PC_PARAM_HOMES_D_TIERS"/></TD>
								<%} else if (tiersOrAdmin.equalsIgnoreCase("admin")){%>
									<TD valign="top" width="149px"><ct:FWLabel key="JSP_PC_PARAM_HOMES_D_ADMIN"/></TD>
								<%}%>
							<TD align="left" colspan="2">
									<input type="hidden" id="home.simpleHome.idHome" value="<%=JadeStringUtil.toNotNullString(viewBean.getHome().getSimpleHome().getIdHome())%>"></input>	
									
									<% if(tiersOrAdmin.equalsIgnoreCase("tiers")){%>
								<ct:widget name="tiersWidget" id="tiersWidget" styleClass="libelleLong" notation="data-g-string='mandatory:true'">
									<ct:widgetService methodName="findAdresse" className="<%=PersonneEtendueService.class.getName()%>">
										<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_PARAM_HOMES_W_TIERS_DESIGNATION"/>								
										<ct:widgetCriteria criteria="forNpaLike" label="JSP_PC_PARAM_HOMES_W_TIERS_NPA"/>
										<ct:widgetCriteria criteria="forLocaliteLike" label="JSP_PC_PARAM_HOMES_W_TIERS_LOCALITE"/>
												<ct:widgetCriteria criteria="forTypeAdresse" fixedValue="<%=ch.globaz.pyxis.business.service.AdresseService.CS_TYPE_DOMICILE%>" label="JSP_PC_PARAM_HOMES_W_TIERS_LOCALITE"/>
										<ct:widgetLineFormatter format="#{cs(tiers.titreTiers)} #{tiers.designation2} #{tiers.designation1}, #{tiers.idTiers}  - (#{localite.numPostal} #{localite.localite})"/>
										<ct:widgetJSReturnFunction>
											<script type="text/javascript">
												function(element){
													$(this).val('');
													
													$('.detailAdresseTiers').html(
														$(element).attr('cs(tiers.titreTiers)')+' '+$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2')+'<br>'+
														$(element).attr('adresse.rue')+' '+$(element).attr('adresse.numeroRue')+'<br>'+
														$(element).attr('localite.numPostal')+' '+$(element).attr('localite.localite')+'<br>'
													);
															$('#home\\.simpleHome\\.idTiersHome').val($(element).attr('tiers.id'));
													$('.external_link').attr('href',urlTiers+$(element).attr('tiers.id'));
												}
											</script>										
										</ct:widgetJSReturnFunction>
									</ct:widgetService>
								</ct:widget>
												<%
									    if (!JadeStringUtil.isBlankOrZero(viewBean.getHome().getSimpleHome().getIdTiersHome())) {
									    %>
											<a href="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=viewBean.getHome().getSimpleHome().getIdTiersHome()%>" class="external_link"><ct:FWLabel key="JSP_LIEN_TIERS"/></a>
										<%
										}
										%>
									<%
									} else if (tiersOrAdmin.equalsIgnoreCase("admin")){
									%>
										<ct:widget name="adminWidget" id="adminWidget" styleClass="libelleLong" notation="data-g-string='mandatory:true'">
											<ct:widgetService methodName="findAdministrationAdresse" className="<%=ch.globaz.pyxis.business.service.AdministrationService.class.getName()%>">
												<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_PC_PARAM_HOMES_W_CODE_ADMINISTRATION"/>								
												<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_PARAM_HOMES_W_DESIGNATION"/>
												<ct:widgetCriteria criteria="forTypeAdresse" fixedValue="<%=ch.globaz.pyxis.business.service.AdresseService.CS_TYPE_DOMICILE%>" label="JSP_PC_PARAM_HOMES_W"/>
											<%-- <ct:widgetCriteria criteria="forGenreAdministration" label="JSP_PC_PARAM_HOMES_W_CANTON" fixedValue="1"/>  --%> 
												<ct:widgetLineFormatter format="#{adresse.tiers.designation1} #{adresse.tiers.designation2} (#{admin.codeAdministration}) -  #{cs(adresse.localite.idCanton)} #{adresse.localite.numPostal} #{adresse.localite.localite}"/>
												<ct:widgetJSReturnFunction>
													<script type="text/javascript">
														function(element){  
															$(this).val('');
															$('.detailAdresseTiers').html(
																$(element).attr('adresse.tiers.designation1')+' '+$(element).attr('adresse.tiers.designation2')+'<br>'+
																$(element).attr('admin.codeAdministration')+' '+$(element).attr('adresse.localite.idCanton')+'<br>' +
																$(element).attr('adresse.adresse.rue')+' '+$(element).attr('adressee.adresse.numeroRue')+'<br>'+
																$(element).attr('adresse.localite.numPostal')+' '+$(element).attr('adresse.localite.localite')+'<br>'
															);
															$('#home\\.simpleHome\\.idTiersHome').val($(element).attr('adresse.tiers.id'));
															$('.external_link').attr('href',urlTiers+$(element).attr('adresse.tiers.id'));
														}
													</script>
												</ct:widgetJSReturnFunction>
											</ct:widgetService>
										</ct:widget>
									
									<%
									}
									%>
							</TD>
						</TR>
						<tr>
							<td>&nbsp;</td>
							<td width="372px">
								<PRE><span class="detailAdresseTiers"><%=viewBean.getHomeAdresseFormatee()%></span></PRE>
									<input type="hidden" id="home.simpleHome.idTiersHome" value="<%=JadeStringUtil.toNotNullString(viewBean.getHome().getSimpleHome().getIdTiersHome())%>">
							</TD>
							<td valign="top">
							
							</td>
						</TR>
						<TR><TD colspan="3">&nbsp;</TD></TR>		
						<TR>
							<TD><LABEL for="home.simpleHome.nomBatiment"><ct:FWLabel key="JSP_PC_PARAM_HOMES_D_NOM_BATIMENT"/></LABEL></TD>
							<TD colspan="3">
									<input type="text" id="home.simpleHome.nomBatiment" value="<%=JadeStringUtil.toNotNullString(viewBean.getHome().getSimpleHome().getNomBatiment())%>" class="libelleLong" >
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="home.simpleHome.numeroIdentification"><ct:FWLabel key="JSP_PC_PARAM_HOMES_D_NUMERO_IDENTIFICATION"/></LABEL></TD>
							<TD>
								<input type="text" id="home.simpleHome.numeroIdentification" value="<%=JadeStringUtil.toNotNullString(viewBean.getHome().getSimpleHome().getNumeroIdentification())%>" class="libelle" >
							</TD>
							<TD colspan="2" ><LABEL for="home.simpleHome.isHorsCanton"><ct:FWLabel key="JSP_PC_PARAM_EST_HORS_CANTON"/></LABEL>
								<!-- 	<input type="checkbox" id="home.simpleHome.isHorsCanton" "<%=(viewBean.getHome().getSimpleHome().getIsHorsCanton())?"checked='checked'":""%>" class="libelle" >
								 -->
								<span style="display: inline-block; vertical-align: middle;" ><span id="isHorsCanton" class="ui-icon <%=(viewBean.getHome().getSimpleHome().getIsHorsCanton())?"ui-icon-check":"ui-icon-closethick"%>"> </span></span>
							</TD>
						</TR>
			<% if(!viewBeanIsNew){ %>
						<tr>
							<td class="btnAjax" colspan="3">
								<input class="btnAjaxValidate" type="button" value="<%=btnValLabel%>">
								<input class="btnAjaxCancel" type="button" value="<%=objSession.getLabel("JSP_PC_SGL_D_ANNULER")%>">
								<input class="btnAjaxUpdate" type="button" value="<%=btnUpdLabel%>">
							</td>
						</tr>
				<% } else { %>
							<td class="btnAjax" colspan="3">
								<input class="btnAjaxAdd" type="button" value="<%=btnNewLabel%>">
								<input class="btnAjaxValidate" type="button" value="<%=btnValLabel%>">
								<input class="btnAjaxCancel" type="button" value="<%=objSession.getLabel("JSP_PC_SGL_D_ANNULER")%>">
							</td>
			<% } %>
					</table>
					</fieldset>
					</div>
				</td>
			</tr>
			<TR>
				<TD colspan="4" id="areaAllPeriodes">
					<fieldset><legend class="ui-widget-header"><ct:FWLabel key="JSP_PC_PARAM_HOMES_D_PERIODES"/></legend>  
					<DIV class="areaPeriodes">					
						<%-- PERIODES SERVICE DE L'ETAT TABLEAU--%>
					 
						<TABLE class="areaPeriodeDataTable areaTabel">
							<thead>
								<TR>
									<TH><ct:FWLabel key="JSP_PC_PARAM_HOMES_D_PERIODE"/></TH>
									<TH><ct:FWLabel key="JSP_PC_PARAM_HOMES_D_SERVICE"/></TH>
								<!--	<TH><ct:FWLabel key="JSP_PC_PARAM_HOMES_D_RECONNU"/></TH> -->
								</TR>
							</thead>
							<tbody>
								<ct:forEach items="<%=viewBean.getListePeriodeServiceEtat().iterator()%>" var="periode">
								<% PeriodeServiceEtat periode = (PeriodeServiceEtat)pageContext.getAttribute("periode"); %>
								<TR idEntity="<%=periode.getId()%>">
									<TD align="left" ><%=periode.getSimplePeriodeServiceEtat().getDateDebut() + " - "+periode.getSimplePeriodeServiceEtat().getDateFin()%></TD>
									<TD><%=objSession.getCodeLibelle(periode.getSimplePeriodeServiceEtat().getCsServiceEtat())%></TD>
								</TR>
								</ct:forEach>
							</tbody>					
						</TABLE>
						
						<%-- PERIODES SERVICE DE L'ETAT detail_ajax --%>
						<div class="areaPeriodeDetail ">
						<TABLE>
							<TR>
								<TD><ct:FWLabel key="JSP_PC_PARAM_HOMES_D_SERVICE_ETAT"/></TD>
								<TD>
									<ct:select name="csService" wantBlank="true" notation="data-g-select='mandatory:true'">
										<ct:optionsCodesSystems csFamille="PCSRVETA"/>
									</ct:select>
								</TD>
								<TD><!-- <ct:FWLabel key="JSP_PC_PARAM_HOMES_D_RECONNU"/>--></TD>
								<TD>
									<!-- <input type="checkbox" id="isReconnu"></input>-->
								</TD>	
							</TR>
							<TR>
								<TD><ct:FWLabel key="JSP_PC_PARAM_HOMES_D_DATE_DEBUT"/></TD>
								<TD>
									<input type="text" id="dateDebut" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month"/>
								</TD>
								<TD><ct:FWLabel key="JSP_PC_PARAM_HOMES_D_DATE_FIN"/></TD>
								<TD>
									<input type="text" id="dateFin" name="dateFin" value="" data-g-calendar="mandatory:false,type:month"/>
								</TD>	
							</TR>
						</TABLE>
						</div>
						<%-- PERIODES SERVICE DE L'ETAT BUTTON--%>
						<DIV class="btnAjax">
							<input class="btnAjaxDelete" type="button" value="<%=btnDelLabel%>">
							<input class="btnAjaxAdd" type="button" value="<%=btnNewLabel%>">
							<input class="btnAjaxValidate" type="button" value="<%=btnValLabel%>">
							<input class="btnAjaxCancel" type="button" value="<%=objSession.getLabel("JSP_PC_SGL_D_ANNULER")%>">
							<input class="btnAjaxUpdate" type="button" value="<%=btnUpdLabel%>">
						</DIV>
				
					</DIV>
					</fieldset>
				</TD>
			</TR>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>