<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
		globaz.amal.vb.formule.AMChampformuleViewBean viewBean = (globaz.amal.vb.formule.AMChampformuleViewBean)session.getAttribute("viewBean");
		//selectedIdValue = viewBean.getIdSignet();
		String selectedFormuleId= request.getParameter("forIdFormule");
		
		userActionValue = "amal.formule.champformule.modifier";
		String idFormule = request.getParameter("idFormule");
		selectedIdValue = idFormule;
			
		if(idFormule==null || idFormule.trim().length()==0){
			idFormule = viewBean.getIdFormule();
		}
		boolean isNew = viewBean.isNew();
		
		
		int tabIndex=1;
		
		
	    btnUpdLabel = objSession.getLabel("MODIFIER");
	    btnDelLabel = objSession.getLabel("SUPPRIMER");
	    btnValLabel = objSession.getLabel("VALIDER");
	    btnCanLabel = objSession.getLabel("ANNULER");
	    btnNewLabel = objSession.getLabel("NOUVEAU");
%>
<% idEcran="AMXXXX"; %>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<SCRIPT type="text/javascript" src="<%=servletContext%>/scripts/onglet.js"></SCRIPT>
<SCRIPT language="JavaScript">

function add() {
   document.forms[0].elements('userAction').value="amal.formule.champFormule.ajouter";
}
function upd() {
	<%if(viewBean.isNew()){%>
	  
	<%} else {%>

	<%}%>
}
function validate() {

   var msgDelete = ' Attention pas de champ sélectionné ';
		   
//   if (document.getElementById('widget1').id == null  || document.getElementById('widget1').id == 'widget1'){
	if (document.forms[0].elements('_method').value == "add"){ 
	    //document.getElementById('signetListModel.simpleSignetJointure.idSignet').value = document.getElementById('widget1').id;
        //document.forms[0].elements('userAction').value="amal.formule.champformule.ajouter";	    
   } else{
	//	alert("update");
		//document.getElementById('signetListModel.simpleSignetJointure.idSignet').value = document.getElementById('widget1').id;
   		document.forms[0].elements('userAction').value="amal.formule.champformule.modifier";
   }
    
    
    return true;

}
function cancel() {
	<%if(viewBean.isNew()){%>
 	  //alert("cancel new");
	  document.forms[0].elements('userAction').value="amal.formule.champformule.chercher";
	<%} else {%>
	  //alert("cancel ");
	  document.forms[0].elements('userAction').value="amal.formule.champformule.chercher";
	<%}%>
}
function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="amal.formule.champformule.supprimer";
        document.forms[0].submit();
    }
}
function init(){	

}

$(function() {
	getListSignets();	

	
    $("#selectSignets").change(function () {
        var str = "";
        $("select option:selected").each(function () {
        	str += $(this).val() + ";";
        });
        $("#nbElem").html("<div>"+$("select option:selected").length+" &eacute;l&eacute;ment(s) s&eacutelection&eacute;(s)</div>");
        $("#signetListModel\\.simpleSignetJointure\\.idSignet").val(str);
      })
      .trigger('change');
});

//---------------------------------------------------------------------
//Récupération de la liste des signets
//---------------------------------------------------------------------
function getListSignets(){
	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.models.formule.FormuleSignetsService',
		serviceMethodName:'getListeSignets',
		parametres: "1",
		callBack: loadListSignets
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

function loadListSignets(data){	
	var a_Data = [];
	var s_values = ""; 
	for(var i_Element=0;i_Element<data.nbOfResultMatchingQuery;i_Element++){
		//a_Data[i_Element] = data.searchResults[i_Element];
		var o_signetObj = new Object();
		var s_options = "";
		o_signetObj.id = data.searchResults[i_Element].idSignetModel;
		o_signetObj.label = data.searchResults[i_Element].signet+' - '+data.searchResults[i_Element].libelle;
		o_signetObj.value = data.searchResults[i_Element].signet+' - '+data.searchResults[i_Element].libelle;		
		a_Data[i_Element] = o_signetObj;
		
		s_options = "<option value='"+o_signetObj.id+"'>"+o_signetObj.value+"</option>";
		$("#selectSignets").append(s_options);
	}	
	
	$('#inputSignets').autocomplete({
		source: a_Data,
		select: function(event, ui) {
			$("#signetListModel\\.simpleSignetJointure\\.idSignet").val(ui.item.id);
		}
	});
	$('#classLoaderImg').hide();
}
/*
*/
// stop hiding -->
</SCRIPT>
<style>
#selectSignets {
		height: 500px;
		overflow-y: auto;
		/* prevent horizontal scrollbar */
		overflow-x: hidden;
		/* add padding to account for vertical scrollbar */
		padding-right: 20px;
	}
</style>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_AM_CH_R_TITLE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain" --%>
	<%-- Definition Formule --%>
	<input id="signetListModel.simpleSignetJointure.idFormule" name="signetListModel.simpleSignetJointure.idFormule" value="<%=idFormule%>" type="hidden" class="formule1" tabindex="-1"/>
	<!--<input id="signetListModel.formuleList.formule.idFormule" name="signetListModel.formuleList.formule.idFormule" value="<%=idFormule%>" type="hidden" class="formule1" tabindex="-1"/> -->
	<input id="signetListModel.simpleSignetJointure.idSignet" name="signetListModel.simpleSignetJointure.idSignet" type="hidden" class="formule1" tabindex="-1"/>
	<%if(viewBean.isNew()){%>
	<TR>
		<TD width="20%"><ct:FWLabel key="JSP_AM_CH_L_NOMCH"/></TD>
		<td width="80%">	
			<!-- <input id="inputSignets" size="80" data-g-string="sizeMax:250,autoFit:false" tabindex="4" class="clearable" type="text" name="idSignet" 
			value=""> -->
			<select id="selectSignets" multiple="multiple">
			</select>
			<div id="nbElem"></div>
		</td>
	</TR>
	<TR><TD colspan="6">&nbsp;</TD></TR>
												
	<%} else {%>
	<TR>
		<TD width="20%"><ct:FWLabel key="LIBELLE"/></TD>
		<%--<TD width="25%"><INPUT type="text" name="csDocument" value="<%=objSession.getCodeLibelle(viewBean.getChampformule().getFormule().getDefinitionformule().getCsDocument())%>" readonly="readonly" class="libelleLongDisabled" tabindex="-1">--%>
		<TD width="25%"><INPUT type="text" name="csDocument" value="<%=viewBean.getSignetListModel().getSimpleSignetModel().getLibelle()%>" readonly="readonly" class="libelleLongDisabled" tabindex="-1">
		<TD width="55%" colspan="3">&nbsp;</TD>
	</TR>

			
	


	<%-- Champ --%>
	<TR>
		<TD width="20%"><ct:FWLabel key="JSP_AM_CH_L_NOMCH"/></TD>
		<TD width="80%" colspan="4">
			<INPUT type="hidden" name="idChamp" value="viewBean.getChampformule().getChamp().getIdChamp()">
			<INPUT type="hidden" name="selectedId" value="<%=viewBean.getIdFormule()%>">
			<INPUT type="text" name="csGroupeValue" value="<%=viewBean.getSignetListModel().getSimpleSignetModel().getSignet()%>" class="libelleLongDisabled" readonly="readonly" tabindex="-1"/>
		</TD>
	</TR>

	<%-- Active --%>
	<%-- Séparateur --%>
	<TR>
		<TD width="100%" colspan="5"><HR></TD>
	</TR>				
	<TR>
		<TD width="20%">&nbsp;</TD>
	<TR>
		<TD width="20%">Nouveau <ct:FWLabel key="JSP_AM_CH_L_NOMCH"/></TD>
		<td width="70%">	
			<input id="inputSignets" size="80" data-g-string="sizeMax:250,autoFit:false" tabindex="4" class="clearable" type="text" name="idSignet" value="">
		   <!--  <input id="signetListModel.simpleSignetJointure.idFormule" name="signetListModel.simpleSignetJointure.idFormule" value="<%=idFormule%>" type="hidden" class="formule1" tabindex="-1"/>
		<input id="signetListModel.simpleSignetJointure.idSignet" name="signetListModel.simpleSignetJointure.idSignet" type="hidden" class="formule1" tabindex="-1"/> 
			<ct:widget  name="widget1"  id="widget1" notation='data-g-string="mandatory:true"' styleClass="libelle" style="width: 500px;"  defaultValue="">
																							
			<%--<ct:widgetService defaultLaunchSize="1" methodName="findChampList" className="<%=ch.globaz.envoi.business.services.models.parametrageEnvoi.ChampFormuleService.class.getName()%>"> --%>
			<ct:widgetService defaultLaunchSize="1" methodName="findAddSignet" className="<%=ch.globaz.envoi.business.services.models.parametrageEnvoi.SignetListModelService.class.getName()%>">
				<ct:widgetCriteria criteria="forSignetName" label="JSP_AM_FORMULE_D_LIBELLE"/>	
				<ct:widgetCriteria criteria="forIdFormule" fixedValue="<%=idFormule%>"  label="JSP_AM_FORMULE_D_LIBELLE"/>					
				<ct:widgetLineFormatter format="#{simpleSignetModel.signet}, #{simpleSignetModel.libelle}"/>
				<ct:widgetJSReturnFunction>
					<script type="text/javascript">
						function(element){						
							this.value=$(element).attr('simpleSignetModel.signet')+', '+$(element).attr('simpleSignetModel.libelle');
							this.id=$(element).attr('simpleSignetModel.idSignetModel');
						}
					</script>										
				</ct:widgetJSReturnFunction>
			</ct:widgetService>
			</ct:widget>
		-->	
								
		</td>
	
		<TD width="10%" colspan="3">&nbsp;</TD>
	</TR>
<%} %>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<ct:menuChange displayId="options" menuId="amal-optionsformules"/>
<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdFormule()%>" menuId="amal-optionsformules"/>

<SCRIPT language="javascript">

reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);

</SCRIPT>
<script language="javascript">
	document.getElementById('btnVal').tabIndex='<%=++tabIndex%>';
	document.getElementById('btnCan').tabIndex='<%=++tabIndex%>';
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>