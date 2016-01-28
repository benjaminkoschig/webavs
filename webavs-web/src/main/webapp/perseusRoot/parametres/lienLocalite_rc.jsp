<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@page import="globaz.perseus.utils.parametres.PFParametresHandler"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="ch.globaz.perseus.business.models.parametres.SimpleZone"%>
<%@page import="ch.globaz.perseus.business.models.parametres.SimpleLienLocalite"%>
<%@ page language="java" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>


<%--
<%@ page import="globaz.perseus.vb.parametres.PFLoyerViewBean" %>
--%>

<%
// Les labels de cette page commence par le préfix "JSP_PF_PARAM_LIEN_LOCALITE"
idEcran="PPF0912";
IFrameDetailHeight = "420";
String idZone = request.getParameter("idZone");
	//PFLienLocaliteViewBean viewBean = (PFLienLocaliteViewBean) session.getAttribute ("viewBean");

BSession objSession = ((globaz.globall.db.BSession)session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_SESSION));  

if(objSession.hasRight("perseus", FWSecureConstants.ADD)){
	bButtonNew = true;
}else{
	bButtonNew = false;
}

%>

<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script> 
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>

<%-- tpl:insert attribute="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="perseus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="perseus-optionsempty"/>

<script language="JavaScript" >
	var bFind = true;
	var usrAction = "perseus.parametres.lienLocalite.lister";
	var detailLink = "perseus?userAction=perseus.parametres.lienLocalite.afficher&_method=add";
</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_LIEN_LOCALITE_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>

	<tr>
		<td width="300" align="left">
			<div class="formTableLess">
				<label for="forDateValableConverter">
					<ct:FWLabel key="JSP_PF_PARAM_LIEN_LOCALITE_PERIODE_DATE"/>
				</label>
				<ct:inputText notation="data-g-calendar='type:month '" name="forDateValableConverter" id="forDateValable"/>
		</td>
		<td width="200" align="left">
				<label for="lienLocaliteSearch.forIdZone">
					<ct:FWLabel key="JSP_PF_PARAM_LIEN_LOCALITE_ZONE" />
				</label>   
				<ct:select name="lienLocaliteSearch.forIdZone" wantBlank="true" defaultValue="<%=idZone%>">
					    <%for(JadeAbstractModel model : PFParametresHandler.getListZone()){ 
					     	SimpleZone simpleZone = (SimpleZone)model;
					     %>
					     <ct:option value="<%=simpleZone.getId()%>" label="<%=simpleZone.getDesignation()%>"/>
						<%} %>
					</ct:select>
		</td>
		<td width="500" align="left">				
				<label for="lienLocaliteSearch.likeNpa">
					<ct:FWLabel key="JSP_PF_PARAM_LIEN_LOCALITE_LIBELLE_NPA"/>
				</label>
				
				<ct:widget id='localite' 
				           name='lienLocaliteSearch.likeNpa' 

				           styleClass='libelleLong'>
						<ct:widgetService methodName="findLocalite" className="<%=ch.globaz.pyxis.business.service.AdresseService.class.getName()%>">										
							<ct:widgetCriteria  criteria="forNpaLike" label="JSP_PF_PARAM_LIEN_LOCALITE_LIBELLE_NPA"/>
							<ct:widgetCriteria criteria="forLocaliteUpperLike" label="JSP_PF_PARAM_LIEN_LOCALITE_LIBELLE_LOCALITE"/>
							<ct:widgetLineFormatter format="#{numPostal}, #{localite}"/>
							<ct:widgetJSReturnFunction>
								<script type="text/javascript">
									function(element){
										$(this).val($(element).attr('numPostal'));
										
									}
								</script>											
							</ct:widgetJSReturnFunction>
						</ct:widgetService>
					</ct:widget>
				
				
				
			</div>
		</td>
	</tr>


	 					<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>



