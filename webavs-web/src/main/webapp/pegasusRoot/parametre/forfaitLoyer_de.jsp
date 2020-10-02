<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.pegasus.vb.parametre.PCForfaitPrimesAssuranceMaladieViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.parametre.SimpleForfaitPrimesAssuranceMaladie"%>
<%@page import="ch.globaz.pegasus.business.services.models.parametre.SimpleZoneForfaitsService"%>
<%@page import="ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaits"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@ page import="globaz.pegasus.vb.parametre.PCForfaitLoyerViewBean" %>
<%@ page isELIgnored ="true" %>
<%-- tpl:insert attribute="zoneInit" --%>
 
 
<%// Les labels de cette page commence par la préfix "JSP_PC_PARAM_ZONE_FORFAIT_D"
	idEcran="PPC0119";
	PCForfaitLoyerViewBean viewBean = (PCForfaitLoyerViewBean) session.getAttribute("viewBean");
	SimpleForfaitPrimesAssuranceMaladie sfpa = viewBean.getForfaitPrimesAssuranceMaladie().getSimpleForfaitPrimesAssuranceMaladie();
	//boolean viewBeanIsValid = "fail".equals(request.getParameter("_valid"));
	boolean viewBeanIsNew= true;
	bButtonCancel = true;
	bButtonValidate = true;
	bButtonDelete = true;
	//bButtonNew = "add".equals(request.getParameter("_method"));
	%>
<%@ include file="/theme/detail/javascripts.jspf" %>

<%-- tpl:insert attribute="zoneScripts" --%>



<script type="text/javascript">
  var url = "<%=IPCActions.ACTION_PARAMETRES_FORFAIT_LOYER%>";
  var messageDelete = "<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
</script>
<%-- /tpl:insert --%>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<link rel="stylesheet" type="text/css" media="screen" href="<%=rootPath%>/css/formTableLess.css">
<script type="text/javascript" src="<%=rootPath %>/scripts/parametre/zoneForfait_js.js"></script>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<tr>
<td>
	<div id="main" class="formTableLess"> 
		<div class="formTableLess">
			<label for="forfaitPrimesAssuranceMaladie.simpleForfaitPrimesAssuranceMaladie.idZoneForfait">
				<ct:FWLabel key="JSP_PC_PARAM_FORFAIT_PRIME_AM_D_ZONE_FORFAIT" />
			</label>

			<ct:select name="forfaitPrimesAssuranceMaladie.simpleForfaitPrimesAssuranceMaladie.idZoneForfait"  notation="data-g-select='mandatory:true'">
				<ct:optionsCodesSystems csFamille="PCZOLOYER"/>
			</ct:select>
		
			<label >
				<ct:FWLabel key="JSP_PC_PARAM_FORFAIT_PRIME_AM_D_TYPE_DE_LOGEMENT" />
			</label>
			<ct:select name="forfaitPrimesAssuranceMaladie.simpleForfaitPrimesAssuranceMaladie.csTypePrime" 
			           defaultValue="<%=sfpa.getCsTypePrime()%>" notation="data-g-select='mandatory:true'">
				<ct:optionsCodesSystems csFamille="PCTYLOYER"/>
			</ct:select>
			
			<br />	
		
			<label for="forfaitPrimesAssuranceMaladie.simpleForfaitPrimesAssuranceMaladie.dateDebut">
				<ct:FWLabel key="JSP_PC_PARAM_FORFAIT_PRIME_AM_D_DATE_DEBUT" />
			</label>
			<ct:inputText name="forfaitPrimesAssuranceMaladie.simpleForfaitPrimesAssuranceMaladie.dateDebut" 
			   defaultValue="<%=sfpa.getDateDebut()%>" 
			   id="forfaitPrimesAssuranceMaladie.simpleForfaitPrimesAssuranceMaladie.dateDebut" 
			   notation="data-g-calendar='mandatory:true'"  />
			   
			<label id="lbldatefin" for="forfaitPrimesAssuranceMaladie.simpleForfaitPrimesAssuranceMaladie.dateFin" style="margin-left:34px">
				<ct:FWLabel key="JSP_PC_PARAM_FORFAIT_PRIME_AM_D_DATE_FIN" />
			</label>
		    <ct:inputText name="forfaitPrimesAssuranceMaladie.simpleForfaitPrimesAssuranceMaladie.dateFin" 
			   defaultValue="<%=sfpa.getDateFin()%>" 
			   id="forfaitPrimesAssuranceMaladie.simpleForfaitPrimesAssuranceMaladie.dateFin" 
			   notation="data-g-calendar='mandatory:false'"  />

			<br />  	
			
			<label for="forfaitPrimesAssuranceMaladie.simpleForfaitPrimesAssuranceMaladie.montant">
				<ct:FWLabel key="JSP_PC_PARAM_FORFAIT_PRIME_AM_D_MONTANT" />
			</label>
			<ct:inputText name="forfaitPrimesAssuranceMaladie.simpleForfaitPrimesAssuranceMaladie.montant" 
			   defaultValue="<%=sfpa.getMontantPrimeMoy()%>"
			   id="forfaitPrimesAssuranceMaladie.simpleForfaitPrimesAssuranceMaladie.montant" 
			   notation="data-g-amount='mandatory:true'" />
		</div>
	</div>
</td>
</tr>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
