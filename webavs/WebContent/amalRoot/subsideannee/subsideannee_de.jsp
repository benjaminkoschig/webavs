	<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@page import="globaz.amal.utils.AMParametresHelper"%>
<%@page import="ch.globaz.amal.business.constantes.IAMParametres"%>
<%@page import="ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnnee"%>
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
<%@page import="globaz.amal.vb.subsideannee.AMSubsideanneeViewBean"%>

<%
	idEcran = "AM1001";
	//View bean depuis la requte
	AMSubsideanneeViewBean viewBean = (AMSubsideanneeViewBean) session.getAttribute("viewBean");

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
	var ACTION_AJAX_SUBSIDE_ANNEE="<%=IAMActions.ACTION_PARAMETRES_SUBSIDE_ANNEEAJAX%>";
	var	b_ctrl=false;

	function upd() {
		$("#anneeSubside").focus();
	}
		
	function add() {
		$("#anneeSubside").focus();
	}
	
</script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/subsideannee/SubsideAnnee_MembrePart.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/ajax_amal.js"/></script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_TITRE"/>
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
								<table border="0">
									<tr>
										<td width="120px"><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_ANNEE"/></td>
										<td><input type="text" name="s_anneeSubside" id="s_anneeSubside" value="" size="4" data-g-integer="sizeMax:6" maxlength="4"/></td>
										<td></td>
										<td></td>
									</tr>
									<tr>
										<td><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_LIMITEREVENU"/></td>
										<td>
											<select id="s_operateur">
												<option value="forLimiteRevenuLOE">&lt;=</option>
												<option selected="selected" value="forLimiteRevenu">=</option>
												<option value="forLimiteRevenuGOE">&gt;=</option>
											</select>
										</td>
										<td><input type="text" name="s_limiteRevenu" id="s_limiteRevenu" size="6" data-g-integer=" "/></td>
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
											<th data-orderKey="annee"><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_ANNEE"/></th>
											<th data-orderKey="limiteRevenu"><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_LIMITEREVENU"/></th>
											<th data-orderKey="subsideAdulte"><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_SUBSIDEADULTE"/></th>
											<th data-orderKey="subsideAdo"><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_SUBSIDEADO"/></th>
											<th data-orderKey="subsideEnfant"><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_SUBSIDEENFANT"/></th>
											<th data-orderKey="subsideSalarie1618"><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_SUBSAL1618"/></th>
											<th data-orderKey="subsideSalarie1925"><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_SUBSAL1925"/></th>											
										</tr>
									</thead>
									<tbody>
										<!-- Ici viendra le tableau des résultats -->					
									</tbody>
								</table>	
								<div class="areaDetail">							
									<table>
										<TR>
											<TD><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_ANNEE"/></TD>
											<TD><input tabindex="1" type="text" name="simpleSubsideAnnee.anneeSubside" id="anneeSubside" value="<%=JadeStringUtil.toNotNullString(viewBean.getSimpleSubsideAnnee().getAnneeSubside())%>" data-g-integer="mandatory:true"/></TD>														
										</TR>					
										<TR>
											<TD><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_LIMITEREVENU"/></TD>
											<TD><input tabindex="2" type="text" name="simpleSubsideAnnee.limiteRevenu" id="limiteRevenu" value="<%=viewBean.getSimpleSubsideAnnee().getLimiteRevenu()%>" class="montant" data-g-amount=" "/></TD>
											<TD><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_SUBSIDEENFANT"/></TD>
											<TD><input tabindex="5" type="text" name="simpleSubsideAnnee.subsideEnfant" id="subsideEnfant" value="<%=viewBean.getSimpleSubsideAnnee().getSubsideEnfant()%>"  data-g-amount=" "/></TD>							
										</TR>
										<TR>
											<TD><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_SUBSIDEADULTE"/></TD>
											<TD><input tabindex="3" type="text" name="simpleSubsideAnnee.subsideAdulte" id="subsideAdulte" value="<%=viewBean.getSimpleSubsideAnnee().getSubsideAdulte()%>"  data-g-amount=" "/></TD>						
											<TD><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_SUBSAL1618"/></TD>
											<TD><input tabindex="6" type="text" name="simpleSubsideAnnee.subsideSalarie1618" id="subsideSalarie1618" value="<%=viewBean.getSimpleSubsideAnnee().getSubsideSalarie1618()%>"  data-g-amount=" "/></TD>							
										</TR>
										<TR>													
											<TD><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_SUBSIDEADO"/></TD>
											<TD><input tabindex="4" type="text" name="simpleSubsideAnnee.subsideAdo" id="subsideAdo"" value="<%=viewBean.getSimpleSubsideAnnee().getSubsideAdo()%>"  data-g-amount=" "/></TD>
											<TD><ct:FWLabel key="JSP_AM_PARAM_SUBSIDEANNEE_D_SUBSAL1925"/></TD>
											<TD><input tabindex="7" type="text" name="simpleSubsideAnnee.subsideSalarie1925" id="subsideSalarie1925" value="<%=viewBean.getSimpleSubsideAnnee().getSubsideSalarie1925()%>"  data-g-amount=" "/></TD>							
										</TR>
									</table>
									<div align="right" class="btnAjax">
									<ct:ifhasright element="<%=IAMActions.ACTION_PARAMETRES_SUBSIDE_ANNEEAJAX%>" crud="cud">
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