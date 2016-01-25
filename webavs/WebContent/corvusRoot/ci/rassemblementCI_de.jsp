<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<%@page import="globaz.corvus.vb.ci.RERassemblementCIViewBean"%>
<%@page import="globaz.corvus.api.ci.IRERassemblementCI"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_RCI_D"
	
	idEcran="PRE0043";
	
	RERassemblementCIViewBean viewBean = (RERassemblementCIViewBean) session.getAttribute("viewBean");
	
	String IdTiers = request.getParameter("idTiers");
	String[] rassemblementCI = viewBean.getRassemblementCIList(IdTiers);
	
	bButtonDelete = bButtonDelete && viewBean.isSupprimable() && controller.getSession().hasRight(IREActions.ACTION_RASSEMBLEMENT_CI, FWSecureConstants.UPDATE);
	bButtonUpdate = bButtonUpdate && controller.getSession().hasRight(IREActions.ACTION_RASSEMBLEMENT_CI, FWSecureConstants.UPDATE);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty">
</ct:menuChange>

<script language="JavaScript">

	function add() {
	    document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RASSEMBLEMENT_CI%>.ajouter";
	}
	
	function upd() {
		document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RASSEMBLEMENT_CI%>.modifier";
	}
	
	function validate() {
	    state = true;
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RASSEMBLEMENT_CI%>.ajouter";
	    }else{
	    	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RASSEMBLEMENT_CI%>.modifier";
	    }
	    
	    if(document.all('isCiAdditionnel').checked &&
	       document.forms[0].elements('idParent').value == ''){
	    	errorObj.text = "<ct:FWLabel key='JSP_RCI_D_MSG_CI_ADD'/>";
	    	state = false;
	    }
	    
	    showErrors();
		errorObj.text = "";
	    
	    return state;
	}
	
	function cancel() {
		document.forms[0].elements('userAction').value="back";
		document.forms[0].submit();
	}
	
	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RASSEMBLEMENT_CI+".supprimer"%>";
	        document.forms[0].submit();
	    }
	}
 	
	function readOnly(flag) {
	  	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
	    for(i=0; i < document.forms[0].length; i++) {
	        if (!document.forms[0].elements[i].readOnly && 
	        	document.forms[0].elements[i].className != 'forceDisable' &&
	        	document.forms[0].elements[i].type != 'hidden') {
	            document.forms[0].elements[i].disabled = flag;
	        }
	    }
	}
  
	function init(){
	}
	
	function postInit(){
		<%if(rassemblementCI.length <= 4){%>	
			document.forms[0].elements('isCiAdditionnel').className = 'forceDisable';
			document.forms[0].elements('idParent').className = 'forceDisable';
			document.forms[0].elements('dateTraitement').className = 'forceDisable';
			document.forms[0].elements('isCiAdditionnel').disabled = true;
			document.forms[0].elements('idParent').disabled = true;
			document.forms[0].elements('dateTraitement').disabled = true;
			document.forms[0].elements('isCiAdditionnel').readonly = true;
			document.forms[0].elements('idParent').readonly = true;
			document.forms[0].elements('dateTraitement').readonly = true;
		<%}%>
	}
	
	function isCiAdditionnelChange(){
	
		if (!document.all('isCiAdditionnel').checked) {
			document.forms[0].elements('idParent').value = '';
			document.forms[0].elements('dateTraitement').value = '';
		}
	}
	
	function rassemblementChange(index){
		
		var motif = "motif_"+((index*4)+2);
		var dateCloture = "dateCloture_"+((index*4)+3);
		
		var newMotif = document.getElementById(motif).value;
		var newDateCloture = document.getElementById(dateCloture).value;
		
		document.getElementById("motif").value = newMotif;
		document.getElementById("dateCloture").value = newDateCloture;
		
		if(index == 0){
			document.all('isCiAdditionnel').checked = false;
		}else{
			document.all('isCiAdditionnel').checked = true;
		}
	}
  
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RCI_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<LABEL for="requerantDescription"><ct:FWLabel key="JSP_RCI_D_REQUERANT"/></LABEL>
								<INPUT type="hidden" name="idTiers" value="<%=IdTiers%>">
								<INPUT type="hidden" name="idTiersAyantDroit" value="<%=IdTiers%>">
								<INPUT type="hidden" name="forIdTiers" value="<%=IdTiers%>">
							</TD>
							<TD colspan="5">
								<re:PRDisplayRequerantInfoTag
										session="<%=(globaz.globall.db.BSession)controller.getSession()%>" 
										idTiers="<%=IdTiers%>"
										style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/>
							</TD>
						</TR>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD><LABEL for="motif"><ct:FWLabel key="JSP_RCI_D_MOTIF"/></LABEL></TD>
							<TD colspan="5"><INPUT type="text" name="motif" value="<%=JadeStringUtil.isIntegerEmpty(viewBean.getMotif())?"":viewBean.getMotif()%>"  maxlength="2" size="2"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_RCI_D_ETAT"/></TD>
							<TD>
								<ct:select name="csEtat" defaultValue="<%=viewBean.getCsEtat()%>" wantBlank="true">
									<ct:optionsCodesSystems csFamille="<%=IRERassemblementCI.CS_GROUPE_ETAT_RASSEMBLEMENT%>">
									</ct:optionsCodesSystems>
								</ct:select>
							</TD>
							<TD><ct:FWLabel key="JSP_RCI_D_DATE_CLOTURE"/></TD>
							<TD colspan="3">
								<input	id="dateCloture"
										name="dateCloture"
										data-g-calendar="type:month"
										value="<%=viewBean.getDateCloture()%>" />
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_RCI_D_DATE_RASSEMBLEMENT"/></TD>
							<TD>
								<input	id="dateRassemblement"
										name="dateRassemblement"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateRassemblement()%>" />
							</TD>
							<TD><ct:FWLabel key="JSP_RCI_D_DATE_REVOCATION"/></TD>
							<TD colspan="3">
								<input	id="dateRevocation"
										name="dateRevocation"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateRevocation()%>" />
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_RCI_D_DATE_TRAITEMENT"/></TD>
							<TD colspan="5">
								<input	id="dateTraitement"
										name="dateTraitement"
										data-g-calendar="type:default"
										value="<%=viewBean.getDateTraitement()%>" />
							</TD>
						</TR>
						<TR>
							<TD colspan="6"><HR>&nbsp;</TD>
						</TR>
						<TR>
							<TD><LABEL for="isCiAdditionnel"><ct:FWLabel key="JSP_RCI_D_CI_ADDITIONNEL"/></LABEL></TD>
							<TD><INPUT type="checkbox" name="isCiAdditionnel" <%=!JadeStringUtil.isIntegerEmpty(viewBean.getIdParent())?"CHECKED":""%> onclick="isCiAdditionnelChange()"></TD>
							<TD><ct:FWLabel key="JSP_RCI_D_RASSEMBLEMENT_PARENT"/></TD>
							<TD colspan="3">
							   	<SELECT name="idParent" onchange="rassemblementChange(this.options.selectedIndex)">
									<%for (int i=0; i<rassemblementCI.length; i=i+4){%>
									<OPTION value="<%=rassemblementCI[i]%>" <%=rassemblementCI[i].equals(viewBean.getIdParent())?"selected":""%>><%=rassemblementCI[i+1]%></OPTION>
									<%}%>
								</SELECT>
								
								<%for (int j=0; j<rassemblementCI.length; j++){%>
									<%if(j%4 == 2){%>
										<INPUT type="hidden" name="motif_<%=j%>" value="<%=rassemblementCI[j]%>">
									<%}%>
									<%if(j%4 == 3){%>
										<INPUT type="hidden" name="dateCloture_<%=j%>" value="<%=rassemblementCI[j]%>">
									<%}%>
								<%}%>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>