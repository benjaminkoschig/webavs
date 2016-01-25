<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.amal.vb.formule.AMFormulerappelViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.envoi.business.services.models.parametrageEnvoi.SimpleDefinitionFormuleService"%>
<%-- tpl:put name="zoneInit" --%>
<%-- @ taglib uri="/WEB-INF/amtaglib.tld" prefix="ai" --%>

<%
    globaz.amal.vb.formule.AMFormulerappelViewBean viewBean = (globaz.amal.vb.formule.AMFormulerappelViewBean)session.getAttribute("viewBean");
	selectedIdValue = viewBean.getId();
	String selectedIdRappelValue = viewBean.getFormule().getRappel().getId();
	
	userActionValue = "amal.formule.formulerappel.modifier";
	//bButtonDelete=false;
			
	int tabIndex=1;
	String unite = "";
	if (viewBean.getRappel() != null ){
		if  (viewBean.getRappel().getUnite() != null){
			unite = viewBean.getRappel().getUnite();
		}
		
	}

    if(!viewBean.isWithRappel()){
    	
    /*	bButtonDelete = false;
    	bButtonUpdate = false;
    	bButtonValidate = true;
    	bButtonCancel = true;*/
    }

	/*btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");*/

%>
<% idEcran="AMAL0001"; %>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%--<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %> --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<SCRIPT type="text/javascript" src="<%=servletContext%>/scripts/onglet.js"></SCRIPT>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">

function add() {
    
}
function upd() {
	
}
function desactivate(){
		
}
function validate() {
	document.getElementById('rappel.idDefinitionFormule').value = document.getElementById('widget1').id;
	
	state = validateFields();

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="amal.formule.formulerappel.modifier";
	else
		document.forms[0].elements('userAction').value="amal.formule.formulerappel.modifier";
	return state;

}
function cancel() {
	document.forms[0].elements('userAction').value="amal.formule.formulerappel.afficher";	 
}
function del() {
	document.forms[0].elements('userAction').value="amal.formule.formulerappel.supprimer";
	document.forms[0].submit();
	
}

function init(){
	//alert('Init');
//	if (viewBean.isWithRappel()){%>
//	document.forms[0].elements('_method')='add';
//	}%>	
}

</SCRIPT>
<%-- /tpl:put  viewBean.getDefinitionFormule().getCsDocument()--%>

<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<ai:AIProperty key="DETAIL_FORMULE"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%@page import="globaz.amal.vb.formule.AMFormuleViewBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collection"%>
						<%-- tpl:put name="zoneMain" --%>
					<%-- Définiton --%>
									<%-- tpl:put name="zoneMain" --%>
				<%-- Formule --%>						
				
				<TR>
					<TD width="20%"><ct:FWLabel key="JSP_AM_PARAMETRAGE_R_FORMULE"/></TD>
					<TD width="25%">
						<INPUT type="text" name="libelleDocument" value="<%=viewBean.getFormule().getFormule().getLibelleDocument()%>" readonly="readonly" class="libelleLongDisabled">
					</TD>
					<TD width="55%" colspan="3">&nbsp;</TD>
				</TR>
				<TR>
					<TD width="20%"><ct:FWLabel key="LIBELLE"/></TD>
					<TD width="25%">
						<INPUT type="text" name="csDocument" value="<%=objSession.getCodeLibelle(viewBean.getFormule().getDefinitionformule().getCsDocument())%>" readonly="readonly" class="libelleLongDisabled">
					</TD>
					<TD width="55%" colspan="3">&nbsp;</TD>
				</TR>
					<TD>&nbsp;</TD>
				</TR>
				
				<TR><TD colspan="5"><HR></TD></TR>

				<%-- Rappel --%>
				<TR>
					<TD width="20%" style="font-style: italic;">
						<ct:FWLabel key="JSP_AM_RAPPEL_D_RAPPEL"/>
					</TD>
					<TD width="80%" colspan="4">&nbsp;</TD>
				</TR>

				<%-- Temps attentes <%=viewBean.getRappel().getUnite()%>   --%>
				<TR>
					<TD width="20%"><ct:FWLabel key="JSP_AM_RAPPEL_D_TPSATT"/></TD>
					<TD width="25%">
						<INPUT disabled="disabled" type="text" class="numeroCourt" name="rappel.tempsAttente" value="<%=(viewBean.getRappel() == null)?"&nbsp;":viewBean.getRappel().getTempsAttente()%>" tabindex="1"/>
					</TD>
					<TD width="55%" colspan="3">&nbsp;</TD>
				</TR>
				
				<%-- Unité --%>
				<TR>
					<TD width="20%"><ct:FWLabel key="JSP_AM_RAPPEL_D_UNITE"/></TD>
					<TD width="25%">
						<ct:FWCodeSelectTag codeType="ENUNITE" defaut="<%=unite%>" 
							name="rappel.unite" wantBlank="false"/>

					</TD>
					<TD width="55%" colspan="3">&nbsp;</TD> 
				</TR>
				
				<%-- Rappel à envoyer --%>
				<TR>
					<TD width="20%"><ct:FWLabel key="JSP_AM_RAPPEL_D_RAPENV"/></TD>
						
					<TD width="80%" colspan="4">
						<div id="areaMembre1" class="areaMembre1" align="left">
							<input id="rappel.idDefinitionFormule" name="rappel.idDefinitionFormule" type="hidden" class="formule1" tabindex="-1"/>
							<ct:widget disabled="disabled" name="widget1"  id="widget1" notation='data-g-string="mandatory:true"' styleClass="libelle" defaultValue="<%=viewBean.getLibelleSearch()%>">
							<ct:widgetService defaultLaunchSize="1" methodName="searchFormule" className="<%=ch.globaz.envoi.business.services.models.parametrageEnvoi.SimpleDefinitionFormuleService.class.getName()%>">
								<ct:widgetCriteria criteria="likeLibelleCosp" label="JSP_AM_FORMULE_D_LIBELLE_PCOSLI"/>
								<ct:widgetCriteria criteria="likeLibelle" label="JSP_AM_FORMULE_D_LIBELLE"/>													
								<ct:widgetLineFormatter format="#{simpleFwcosp.libelle},#{simpleFwcoup.libelle}"/>
								<ct:widgetJSReturnFunction>
									<script type="text/javascript">
										function(element){
											this.value=$(element).attr('simpleFwcosp.libelle')+','+$(element).attr('simpleFwcoup.libelle');
											$('#nomWord').val($(element).attr('simpleFwcosp.libelle'));
											this.id=$(element).attr('simpleFwcoup.pcosid');
										}
									</script>										
								</ct:widgetJSReturnFunction>
							</ct:widgetService>
							</ct:widget>
						</div>
					</TD>
				</TR>
				<%-- /tpl:put --%>
				<TR>

								
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<ct:menuChange displayId="options" menuId="amal-optionsformules"/>
<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getId()%>" menuId="amal-optionsformules"/>

<SCRIPT language="javascript">
//if (document.forms[0].elements('_method').value != "add") {
reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);
//}
</SCRIPT>
<script language="javascript">
	document.getElementById('btnVal').tabIndex='<%=++tabIndex%>';
	document.getElementById('btnCan').tabIndex='<%=++tabIndex%>';
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
