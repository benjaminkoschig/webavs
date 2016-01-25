<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%
	idEcran ="GTI0051";
	globaz.pyxis.db.tiers.TIHistoTiersViewBean viewBean = (globaz.pyxis.db.tiers.TIHistoTiersViewBean) session.getAttribute("viewBean");
	selectedIdValue = request.getParameter("selectedId");
	
	String idTiers = "";
	if(!globaz.jade.client.util.JadeStringUtil.isNull(request.getParameter("idTiers"))){
		idTiers = request.getParameter("idTiers");
	}

	bButtonDelete = bButtonDelete && viewBean.isSupprimable();
	bButtonUpdate = bButtonUpdate && viewBean.isModifiable();
%>
<SCRIPT language="JavaScript">
</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT language="JavaScript">

function add() {
	document.forms[0].elements('userAction').value="pyxis.tiers.histoTiers.ajouter"
}
function upd() {
	document.getElementById('champ').disabled='disabled';
	document.getElementById('champ').readOnly='readOnly';
}
function validate() {
	state = validateFields(); 
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pyxis.tiers.histoTiers.ajouter";
    else
	document.forms[0].elements('userAction').value="pyxis.tiers.histoTiers.modifier";
	return (state);
}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="pyxis.tiers.histoTiers.afficher"
}
function del() {
	var msgDelete = '<%=globaz.pyxis.util.TIUtil.encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
		document.forms[0].elements('userAction').value="pyxis.tiers.histoTiers.supprimer";
		document.forms[0].submit();
	}
}
function init(){
}
function postInit(){
	onchangeChamp(document.getElementById('champ'));
}
</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="HISTORIQUE_TIERS_DETAIL"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain"  --%>
	<TR>
		<TD><ct:FWLabel key="CHAMP"/></TD>
		<TD>
			<select name="champ" id="champ" onchange="onchangeChamp(this);">
				<option value="<%=globaz.pyxis.db.tiers.TITiers.FIELD_DESIGNATION1%>" <%=globaz.pyxis.db.tiers.TITiers.FIELD_DESIGNATION1.equals(viewBean.getChamp())?"selected=\"selected\"":""%>><ct:FWLabel key="NOM_RAISON_SOC"/></option>
				<option value="<%=globaz.pyxis.db.tiers.TITiers.FIELD_DESIGNATION2%>" <%=globaz.pyxis.db.tiers.TITiers.FIELD_DESIGNATION2.equals(viewBean.getChamp())?"selected=\"selected\"":""%>><ct:FWLabel key="PRENOM_RAISON_SOC"/></option>
				<option value="<%=globaz.pyxis.db.tiers.TITiers.FIELD_DESIGNATION3%>" <%=globaz.pyxis.db.tiers.TITiers.FIELD_DESIGNATION3.equals(viewBean.getChamp())?"selected=\"selected\"":""%>><ct:FWLabel key="NOM_SUITE_1"/></option>
				<option value="<%=globaz.pyxis.db.tiers.TITiers.FIELD_DESIGNATION4%>" <%=globaz.pyxis.db.tiers.TITiers.FIELD_DESIGNATION4.equals(viewBean.getChamp())?"selected=\"selected\"":""%>><ct:FWLabel key="NOM_SUITE_2"/></option>
				<option value="<%=globaz.pyxis.db.tiers.TITiers.FIELD_TITRE%>" <%=globaz.pyxis.db.tiers.TITiers.FIELD_TITRE.equals(viewBean.getChamp())?"selected=\"selected\"":""%>><ct:FWLabel key="TITRE"/></option>
				<option value="<%=globaz.pyxis.db.tiers.TITiers.FIELD_PAYS%>" <%=globaz.pyxis.db.tiers.TITiers.FIELD_PAYS.equals(viewBean.getChamp())?"selected=\"selected\"":""%>><ct:FWLabel key="NATIONALITE"/></option>
				<option value="<%=globaz.pyxis.db.tiers.TITiers.FIELD_DATE_DECES%>" <%=globaz.pyxis.db.tiers.TITiers.FIELD_DATE_DECES.equals(viewBean.getChamp())?"selected=\"selected\"":""%>><ct:FWLabel key="DECES"/></option>
			</select>
			<script language="Javascript"> 
				function onchangeChamp(node){
					for(var i=0;i<node.children.length;i++){
						if(node.children[i].selected){
							if(node.children[i].value=='<%=globaz.pyxis.db.tiers.TITiers.FIELD_DESIGNATION1%>'){
								document.getElementById('val1').style.display='block';
								document.getElementById('val2').style.display='none';
								document.getElementById('val3').style.display='none';
								document.getElementById('val4').style.display='none';
								document.getElementById('valTi').style.display='none';
								document.getElementById('valPa').style.display='none';
								document.getElementById('valDa').style.display='none';
							} else if(node.children[i].value=='<%=globaz.pyxis.db.tiers.TITiers.FIELD_DESIGNATION2%>'){
								document.getElementById('val1').style.display='none';
								document.getElementById('val2').style.display='block';
								document.getElementById('val3').style.display='none';
								document.getElementById('val4').style.display='none';
								document.getElementById('valTi').style.display='none';
								document.getElementById('valPa').style.display='none';
								document.getElementById('valDa').style.display='none';
							} else if(node.children[i].value=='<%=globaz.pyxis.db.tiers.TITiers.FIELD_DESIGNATION3%>'){
								document.getElementById('val1').style.display='none';
								document.getElementById('val2').style.display='none';
								document.getElementById('val3').style.display='block';
								document.getElementById('val4').style.display='none';
								document.getElementById('valTi').style.display='none';
								document.getElementById('valPa').style.display='none';
								document.getElementById('valDa').style.display='none';
							} else if(node.children[i].value=='<%=globaz.pyxis.db.tiers.TITiers.FIELD_DESIGNATION4%>'){
								document.getElementById('val1').style.display='none';
								document.getElementById('val2').style.display='none';
								document.getElementById('val3').style.display='none';
								document.getElementById('val4').style.display='block';
								document.getElementById('valTi').style.display='none';
								document.getElementById('valPa').style.display='none';
								document.getElementById('valDa').style.display='none';
							} else if(node.children[i].value=='<%=globaz.pyxis.db.tiers.TITiers.FIELD_TITRE%>'){
								document.getElementById('val1').style.display='none';
								document.getElementById('val2').style.display='none';
								document.getElementById('val3').style.display='none';
								document.getElementById('val4').style.display='none';
								document.getElementById('valTi').style.display='block';
								document.getElementById('valPa').style.display='none';
								document.getElementById('valDa').style.display='none';
							} else if(node.children[i].value=='<%=globaz.pyxis.db.tiers.TITiers.FIELD_PAYS%>'){
								document.getElementById('val1').style.display='none';
								document.getElementById('val2').style.display='none';
								document.getElementById('val3').style.display='none';
								document.getElementById('val4').style.display='none';
								document.getElementById('valTi').style.display='none';
								document.getElementById('valPa').style.display='block';
								document.getElementById('valDa').style.display='none';
							} else if(node.children[i].value=='<%=globaz.pyxis.db.tiers.TITiers.FIELD_DATE_DECES%>'){
								document.getElementById('val1').style.display='none';
								document.getElementById('val2').style.display='none';
								document.getElementById('val3').style.display='none';
								document.getElementById('val4').style.display='none';
								document.getElementById('valTi').style.display='none';
								document.getElementById('valPa').style.display='none';
								document.getElementById('valDa').style.display='block';
							}
						}
					}					
				}
			</script>
		</TD>
		<INPUT type="hidden" name="forIdTiers" value="<%=idTiers%>">
		<INPUT type="hidden" name="idTiers" value="<%=idTiers%>">
	</TR>
	
	<TR>
		<TD><ct:FWLabel key="VALEUR"/></TD>
		<TD id="val1"><INPUT type="text" name="valeurDesignation1" value="<%=viewBean.getValeurDesignation1()%>"></TD>
		<TD id="val2"><INPUT type="text" name="valeurDesignation2" value="<%=viewBean.getValeurDesignation2()%>"></TD>
		<TD id="val3"><INPUT type="text" name="valeurDesignation3" value="<%=viewBean.getValeurDesignation3()%>"></TD>
		<TD id="val4"><INPUT type="text" name="valeurDesignation4" value="<%=viewBean.getValeurDesignation4()%>"></TD>
		<TD id="valTi"><ct:FWCodeSelectTag name="csTitre" defaut="<%=viewBean.getCsTitre()%>" codeType="PYTITRE" wantBlank="true"/></TD>
		<TD id="valPa"><ct:FWListSelectTag name="idPays" defaut="<%=viewBean.getIdPays()%>" data="<%=globaz.pyxis.db.adressecourrier.TIPays.getPaysList(session)%>"/></TD>
		<TD id="valDa"><ct:FWCalendarTag name="valeurDate" value="<%=viewBean.getValeurDate()%>" doClientValidation="CALENDAR"/></TD>
	</TR>
	
	<TR>
		<TD nowrap width="140"><ct:FWLabel key="MOTIF"/></TD>
		<TD nowrap>
			<ct:FWCodeSelectTag name="motif"
				defaut="<%=viewBean.getMotif()%>" codeType="PYMOTIFHIS" wantBlank="true"/>
		</TD>
    </TR>
        
    <TR>
		<TD><ct:FWLabel key="ENTREE_VIGUEUR"/></TD>
		<TD>
			<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>"/>
		</TD>
	</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> 
<SCRIPT>
</SCRIPT> 
<%	} %> 
<ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>