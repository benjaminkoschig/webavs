<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran ="GTI6007";
	globaz.pyxis.db.fusiontiers.TIFusionTiersViewBean viewBean = (globaz.pyxis.db.fusiontiers.TIFusionTiersViewBean)session.getAttribute ("viewBean");
%>


<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT language="JavaScript">
top.document.title = "Tiers - Fusion Tiers"
<!--hide this script from non-javascript-enabled browsers
function add() {
	document.forms[0].elements('userAction').value="pyxis.fusiontiers.fusionTiers.ajouter";
}

function upd() {
}

function validate() {
	state = validateFields();
 
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="pyxis.fusiontiers.fusionTiers.ajouter";
	else
		document.forms[0].elements('userAction').value="pyxis.fusiontiers.fusionTiers.modifier";
	return (state);
}

function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="pyxis.fusiontiers.fusionTiers.chercher";
 else
  document.forms[0].elements('userAction').value="pyxis.fusiontiers.fusionTiers.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="pyxis.fusiontiers.fusionTiers.supprimer";
		document.forms[0].submit();
	}
}

function init() {
}

/*
*/
// stop hiding -->
</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
		<%-- tpl:put name="zoneTitle" --%>
		<ct:FWLabel key='FUSION_TIERS' />
		<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain"  --%>
	<tr>
	    <td>
			<%if("add".equalsIgnoreCase(request.getParameter("_method"))){%>
	    		<ct:FWLabel key="TIERS_DEFINITIF"/>
	    	<%} else {%>
				<a href="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=viewBean.getIdTiersMaster()%>" style="color: blue; text-decoration: underline"><ct:FWLabel key="TIERS_DEFINITIF"/></a>
	    	<%}%>
	    </td>
	    <td>
			<input type="text" value="<%=viewBean.getNumAvsActuelMaster()%>" class="libelleLongDisabled" readonly="readonly"/>
		</td>
	</tr>	   
	<tr>
		<td>&nbsp;</td>
        <td>
			<%if (!globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getTiersMaster().getAdresseAsString(globaz.pyxis.db.adressecourrier.TIAvoirAdresse.CS_DOMICILE,false))){ %>
				<TEXTAREA rows="5" cols="40" name="libelleMaster" readonly="readonly" class="libelleLongDisabled" 
					style="font-weight:bold;font-size:8pt" ><%=viewBean.getTiersMaster().getAdresseAsString(globaz.pyxis.db.adressecourrier.TIAvoirAdresse.CS_DOMICILE,false)%></TEXTAREA>
			<%}else{%>
				<TEXTAREA rows="5" cols="40" name="libelleMasterShort" readonly="readonly" class="libelleLongDisabled" 
					style="font-weight:bold;font-size:8pt" ><%=viewBean.getTiersMaster().getDesignation1() + " " + viewBean.getTiersMaster().getDesignation2()%></TEXTAREA>
			<%}%>				
			<% Object[] tiersMasterMethodsName = new Object[]{
					new String[] {"idTiersMaster","idTiers"},
					new String[] {"tiersMaster.idTiers","idTiers"},
					new String[] {"numAvsActuelMaster","numAvsActuel"},
					new String[] {"tiersMaster.designation1","designation1_tiers"},
					new String[] {"tiersMaster.designation2","designation2_tiers"}
			};%>
 			<ct:FWSelectorTag name="tiersMasterSelector"
				methods="<%=tiersMasterMethodsName%>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.fusiontiers.selectTiers.chercher"/>
				
			<ct:inputHidden name="idTiersMaster"/>
			<ct:inputHidden name="tiersMaster.idTiers"/>
			<ct:inputHidden name="numAvsActuelMaster"/>
			<ct:inputHidden name="tiersMaster.designation1"/>
			<ct:inputHidden name="tiersMaster.designation2"/>
		</td>
	</tr>
	
	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	
	<tr>
	    <td>
			<%if("add".equalsIgnoreCase(request.getParameter("_method"))){%>	    
	    		<ct:FWLabel key="TIERS_DESACTIVER"/>
	    	<%} else {%>
				<a href="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=viewBean.getIdTiersMaster()%>" style="color: blue; text-decoration: underline"><ct:FWLabel key="TIERS_DESACTIVER"/></a>
	    	<%}%>
	    </td>
	    <td>
			<input type="text" value="<%=viewBean.getNumAvsActuelSlave()%>" class="libelleLongDisabled" readonly="readonly"/>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>	
        <td>
			<%if (!globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getTiersSlave().getAdresseAsString(globaz.pyxis.db.adressecourrier.TIAvoirAdresse.CS_DOMICILE,false))){ %>
				<TEXTAREA rows="5" cols="40" name="libelleSlave" readonly="readonly" class="libelleLongDisabled" 
					style="font-weight:bold;font-size:8pt" ><%=viewBean.getTiersSlave().getAdresseAsString(globaz.pyxis.db.adressecourrier.TIAvoirAdresse.CS_DOMICILE,false)%></TEXTAREA>
			<%}else{%>
				<TEXTAREA rows="5" cols="40" name="libelleSlaveShort" readonly="readonly" class="libelleLongDisabled" 
					style="font-weight:bold;font-size:8pt" ><%=viewBean.getTiersSlave().getDesignation1() + " " + viewBean.getTiersSlave().getDesignation2()%></TEXTAREA>
			<%}%>		
			<% Object[] tiersSlaveMethodsName = new Object[]{
					new String[] {"idTiersSlave","idTiers"},
					new String[] {"tiersSlave.idTiers","idTiers"},
					new String[] {"numAvsActuelSlave","numAvsActuel"},
					new String[] {"tiersSlave.designation1","designation1_tiers"},
					new String[] {"tiersSlave.designation2","designation2_tiers"}
			};%>
 			<ct:FWSelectorTag name="tiersSlaveSelector"
				methods="<%=tiersSlaveMethodsName%>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.fusiontiers.selectTiers.chercher"/> 
			
			<ct:inputHidden name="idTiersSlave"/>
			<ct:inputHidden name="tiersSlave.idTiers"/>
			<ct:inputHidden name="numAvsActuelSlave"/>
			<ct:inputHidden name="tiersSlave.designation1"/>
			<ct:inputHidden name="tiersSlave.designation2"/>
		</td>						
	</tr>
  <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
	<%-- tpl:put name="zoneButtons" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
	<%-- tpl:put name="zoneEndPage"  --%>
<script type="text/javascript">
<%if (bButtonUpdate) {%>
	document.getElementById('btnUpd').style.visibility="hidden";
<%}%>
</script>
<ct:menuChange displayId="options" menuId="fusionTiers" showTab="options" >
	<ct:menuSetAllParams key="idFusionsTiers" value="<%=viewBean.getIdFusionsTiers()%>"/>
	<ct:menuSetAllParams key="dateFusion" value="<%=viewBean.getDate()%>"/>
	<ct:menuSetAllParams key="masterNSS" value="<%=viewBean.getNumAvsActuelMaster()%>"/>
	<ct:menuSetAllParams key="masterNom" value="<%=globaz.fweb.util.JavascriptEncoder.getInstance().encode(viewBean.getTiersMaster().getNomPrenom())%>"/>
	<ct:menuSetAllParams key="slaveNSS" value="<%=viewBean.getNumAvsActuelSlave()%>"/>
	<ct:menuSetAllParams key="slaveNom" value="<%=globaz.fweb.util.JavascriptEncoder.getInstance().encode(viewBean.getTiersSlave().getNomPrenom())%>"/>
	<ct:menuSetAllParams key="statutFusion" value="<%=viewBean.getSession().getCodeLibelle(viewBean.getCsStatut())%>"/>
</ct:menuChange>
	 <%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>