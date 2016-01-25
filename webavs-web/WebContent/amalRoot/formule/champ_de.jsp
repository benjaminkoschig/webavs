<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="ch.globaz.envoi.business.services.models.parametrageEnvoi.SimpleSignetService"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>

<%-- tpl:put name="zoneInit" --%>
<%--<%@ taglib uri="/WEB-INF/aitaglib.tld" prefix="ai" %>--%>
<%
		globaz.amal.vb.formule.AMChampViewBean viewBean = (globaz.amal.vb.formule.AMChampViewBean)session.getAttribute("viewBean");
		selectedIdValue = viewBean.getId();		
		userActionValue = "amal.formule.champ.modifier";
		
		String csGroupe = "";
		if(viewBean.isNew()){
			csGroupe = request.getParameter("csGroupe");

		} else {
			csGroupe = viewBean.getChamp().getCsGroupe();		
		}
		
		tableHeight = 130;
				
		int tabIndex=1;
		
	    btnUpdLabel = objSession.getLabel("MODIFIER");
	    btnDelLabel = objSession.getLabel("SUPPRIMER");
	    btnValLabel = objSession.getLabel("VALIDER");
	    btnNewLabel = objSession.getLabel("NOUVEAU");
	    btnCanLabel = objSession.getLabel("ANNULER");
%>
<% idEcran="IEN0130"; %>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneScripts" --%>

<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<SCRIPT type="text/javascript" src="<%=servletContext%>/scripts/onglet.js"></SCRIPT>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="amal.formule.champ.ajouter";
}
function upd() {
	document.getElementsByName("Widget1").disabled=false;
}
function validate() {

    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="amal.formule.champ.ajouter";
    else
        document.forms[0].elements('userAction').value="amal.formule.champ.modifier";
    
    return state;

}
function cancel() {
	<%if(viewBean.isNew()){%>
	  //document.forms[0].elements('userAction').value="amal.formule.champ.chercher";
	<%} else {%>
	  //document.forms[0].elements('userAction').value="amal.formule.champ.afficher";
	<%}%>
}
function del() {
	alert("dede");
	//var msgDelete = '<%=objSession.getLabel("MESSAGE_SUPPRESSION")%>';
    //if (window.confirm(msgDelete)){
       // document.forms[0].elements('userAction').value="amal.formule.champ.supprimer";
       // document.forms[0].submit();
    //}
}


function init(){
	//initSignet('idSignet', 'csChampCode');
}

<%
globaz.amal.vb.formule.AMSignetListViewBean signetManager = (globaz.amal.vb.formule.AMSignetListViewBean) session.getAttribute("signetManager");
%>




/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ai:AIProperty key="DETAIL_CHAMPS"/><ct:FWLabel key="LIBELLE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%-- Champ --%>
						<TR>
							<TD width="20%"><ai:AIProperty key="CHAMP"/><ct:FWLabel key="LIBELLE"/></TD>
							
							<TD width="60%" colspan="4">
								<div id="areaMembre1"  class="areaMembre1" align="left">
									<ct:widget disabled="true"  name="Widget1" style="width: 350px;" id="Widget1" notation='data-g-string="mandatory:true"' defaultValue="<%=objSession.getCodeLibelle(viewBean.getChamp().getCsChamp())%>" styleClass="libelle">
										<ct:widgetService defaultLaunchSize="1" methodName="searchLibelle" className="<%=ch.globaz.envoi.business.services.models.parametrageEnvoi.SimpleSignetService.class.getName()%>">
											<ct:widgetCriteria criteria="forLibelle" label="JSP_AM_FORMULE_D_LIBELLE"/>				
											<ct:widgetLineFormatter format="#{nom}"/>
											<ct:widgetJSReturnFunction>
												<script type="text/javascript">
													function(element){
														this.value=$(element).attr('nom')+'';this.id=$(element).attr('idSignet')
													}
												</script>										
											</ct:widgetJSReturnFunction>
										</ct:widgetService>
									</ct:widget>
					
							  	</div>
						  				
							</TD> 
							<TD width="20%" colspan="4">
										<INPUT type="hidden" name="csGroupe" value="<%=csGroupe%>">
										<INPUT type="hidden" name="idSignet" value="<%=viewBean.getId()%>" id="idSignet">
										<INPUT type="hidden" name="csChamp" value="<%=viewBean.getChamp().getCsChamp()%>" id="csChamp">
									
										<%--<INPUT type="text" class="libelleLongDisabled" name="csChampLibelle"
											maxlength="25" tabindex="-1" value="<%=objSession.getCodeLibelle(viewBean.getChamp().getCsChamp())%>"
											readonly="readonly" id="csChampLibelle"> --%>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%if(!("add".equalsIgnoreCase(request.getParameter("_method")))){%>
	<ct:menuChange displayId="options" menuId="optionsAMDetail"/>
<%}%>
<script language="javascript">
if (document.forms[0].elements('_method').value != "add") {
	reloadMenuFrame(top.fr_menu, MENU_TAB_MENU);
}
</SCRIPT>
<script language="javascript">
	document.getElementById('btnVal').tabIndex='<%=++tabIndex%>';
	document.getElementById('btnCan').tabIndex='<%=++tabIndex%>';
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>