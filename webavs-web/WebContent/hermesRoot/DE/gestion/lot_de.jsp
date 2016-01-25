<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
HEConfigurationServiceListViewBean vb = (HEConfigurationServiceListViewBean) session.getAttribute("vb");
globaz.hermes.db.gestion.HELotViewBean viewBean = (globaz.hermes.db.gestion.HELotViewBean)session.getAttribute("viewBean");
selectedIdValue = viewBean.getIdLot();
subTableWidth = "";
if(!(viewBean.isLotReception(viewBean.getType()))){
	userActionValue = "hermes.gestion.lot.chercher";
}else{
	userActionValue = "hermes.gestion.lot.executer";
}
boolean isDateNSS = globaz.hermes.utils.HEUtil.isNNSSActif(viewBean.getSession());
idEcran="GAZ0002";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 

<%@page import="globaz.hermes.db.gestion.HEConfigurationServiceListViewBean"%>
<%@page import="globaz.hermes.utils.HEConfigurationServiceUtils"%><SCRIPT language="javascript"> 
function add() {}

function upd() {
}

function validate() {
	return true;
} 

function cancel() {
  document.forms[0].elements('userAction').value="hermes.gestion.lot.afficher";
}

function del() {
	    if (window.confirm("Sie sind dabei, das ausgew�hlte Objekt zu l�schen! Wollen Sie fortfahren?")){
	        document.forms[0].elements('userAction').value="hermes.gestion.lot.supprimer";
       	 document.forms[0].submit();
	    }
}

function init(){

}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail des Jobs<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <tr> 
            <td>Nummer&nbsp;&nbsp;</td>
            <td> 
              <input type="text" value="<%=viewBean.getIdLot()%>" name="idLot" readonly class=disabled size="13">
            </td>
          </tr>
          <tr> 
            <td>ZAS-Datum&nbsp;&nbsp;</td>
            <td> 
              <input type="text" name='dateEnvoi' value="<%=viewBean.getDateCentraleLibelle()%>" class="disabled" size="13" readonly>
            </td>
          </tr>
          <tr> 
            <td>Verarbeitungsdatum&nbsp;&nbsp;</td>
            <td> 
              <input type="text" name='dateEnvoi' value="<%=viewBean.getDateTraitementLibelle()%>" class="disabled" size="13" readonly>
            </td>
          </tr>
          <tr> 
            <td>Verarbeitungszeit&nbsp;&nbsp;</td>
            <td> 
              <input type="text" value="<%=viewBean.getHeureTraitement()%>" name="heureEnvoi" size="13" maxlength="10" class="disabled" readonly>
            </td>
          </tr>
          <tr> 
            <td>Typ&nbsp;&nbsp;</td>
            <td> 
              <input type="text" value="<%=viewBean.getTypeLibelle()%>" name="typeLibelle" size="13" maxlength="13" readonly class="disabled">
              <input type="hidden" value="<%=viewBean.getType()%>" name="type">
            </td>
          </tr>
          <tr> 
            <td>Total MZR&nbsp;&nbsp;</td>
            <td> 
              <input type="text" value="<%=viewBean.getNbAnnonces()%>" name="nbAnnonces" size="13" maxlength="10" readonly class="disabled">
            </td>
          </tr>
          <%if(viewBean.getType().equals(viewBean.CS_TYPE_RECEPTION)){%>
          <tr> 
            <td colspan="2"> 
              <hr>
            </td>
          </tr>
          <tr> 
            <td>Liste der empfangene MZR ausdrucken</td>
            <td> 
              <input type="checkbox" name="ImprimerListeAnnonceRecue" value="true">
            </td>
          </tr>
          <tr> 
            <td>Liste der empfangene MZR ausdrucken, f�r die man einen VA erwartet</td>
            <td> 
              <input type="checkbox" name="caonly" value="true">
            </td>
          </tr>
          <% if(!isDateNSS) {%>
           <tr> 
            <td>Briefe f�r die Mitglieder ausdrucken</td>
            <td> 
              <input type="checkbox" name="ImprimerLettreAffilies" value="true">
            </td>
          </tr>
          <%} %>
          <tr> 
            <td>Vollst�ndige IK-Ausz�ge ausdrucken</td>
            <td> 
              <input type="checkbox" name="ImprimerExtraitCiTermine" value="true">
            </td>
          </tr>
          <tr> 
            <td>Empfangene Nachtrags-IK ausdrucken</td>
            <td> 
              <input type="checkbox" name="ImprimerCIAdditionnels" value="true">
            </td>
          </tr>
          
          <tr> 
            <td>Liste von Uneinbringlichen ausdrucken</td>
            <td> 
              <input type="checkbox" name="ImprimerIrrecouvrable" value="true">
            </td>
          </tr>
          
          <% if(isDateNSS) {%>
          <tr>
	            <td>Versicherungsausweise ausdrucken</td>
	            <td> 
	              <input type="checkbox" name="ImprimerRemiseCertifCA" value="true">
	            </td>
          </tr>          	          
          <tr> 
	            <td>Versicherungsnachweise ausdrucken</td>
	            <td> 
	              <input type="checkbox" name="ImprimerRemiseAttestCA" value="true">
	            </td>
          </tr>
		<%} %>	          
	      <tr>
	       	 <td><b>Dokument senden an : </b></td>
         	 <td>
              <%String strEmail = viewBean.getSession().getUserEMail()==null?"":viewBean.getSession().getUserEMail();%>
              
              <input type="text" value="<%=strEmail%>" maxlength="40" size="40" style="width:8cm;" name="email">
              <input type="hidden" name="isArchivage" value="<%=viewBean.isArchivage()%>">
            </td>
          </tr>
          <tr>
	       	 <td><b>Dienst zu ausw�hlen : </b></td>
         	 <td>
              <%String listReference = HEConfigurationServiceUtils.getListReferenceInterne(viewBean.getSession(), "referenceName");
              	if (!globaz.jade.client.util.JadeStringUtil.isBlank(listReference)) {
					out.print(listReference);
				}%>
            </td>
          </tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <%}%>
          <%if(globaz.hermes.db.gestion.HELotViewBean.CS_TYPE_AVIS_DECES.equals(viewBean.getType())){%>
          <tr> 
            <td colspan="2"> 
              <hr>
            </td>
          </tr>
          <tr> 
            <td>Ausdruck der Todesmeldungen per E-Mail senden an&nbsp;&nbsp;</td>
            <td> 
              <%String strEmail = viewBean.getSession().getUserEMail()==null?"":viewBean.getSession().getUserEMail();%>
              <input type="text" value="<%=strEmail%>" maxlength="40" size="40" style="width:8cm;" name="email">
              <input type="hidden" name="isArchivage" value="<%=viewBean.isArchivage()%>">
            </td>
          </tr>
          <%}%>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>