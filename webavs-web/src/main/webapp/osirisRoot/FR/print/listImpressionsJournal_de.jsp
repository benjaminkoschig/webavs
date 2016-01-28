<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%@page import="globaz.osiris.application.CAApplication"%>
<%@page import="globaz.osiris.db.print.CAListImpressionsJournalViewBean"%>

<%
	idEcran = "GCA2014";
	CAListImpressionsJournalViewBean viewBean = (CAListImpressionsJournalViewBean) session.getAttribute(CADefaultServletAction.VB_ELEMENT);
	userActionValue = CAApplication.DEFAULT_OSIRIS_NAME + ".print.listImpressionsJournal.executer";
	if(JadeStringUtil.isEmpty(viewBean.getListe())) {
		viewBean.setListe("ecritures");
	}
	String pdfChecked = "pdf".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "";
	String xlsChecked = "xls".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "";
	String libLong = viewBean.getJournal() != null ? viewBean.getJournal().getIdJournal()+" - "+viewBean.getJournal().getLibelle() : "";
%>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
top.document.title = "Liste - <ct:FWLabel key='GCA2014_TITRE_ECRAN'/> - " + top.location.href;

$(document).ready(function() {
	$('.typeImp').attr('disabled', 'disabled');
	
	$('#liste').change(function(){
		typeImpressionSelect();
	});
	
	typeImpressionSelect();
});

var typeImpressionSelect = function () {
	var optionSelect = $("option:selected").val();
	if(optionSelect==='suspens') {
		$('.typeImp').removeAttr('disabled');
	} else {
		$('.typeImp').attr('disabled', 'disabled');
		$("#radPdf").attr('checked', 'checked');
	}
}


</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="GCA2014_TITRE_ECRAN"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

    <tr>
    	<td width="160px"><ct:FWLabel key="GCA2014_JOURNAL"/></td>
      	<td>
      		<input type="hidden" name="idJournal" value="<%=viewBean.getIdJournal()%>">
        	<input type="text" name="_libelleJournal" value="<%=libLong%>" class="libelleLong" disabled="disabled">
      	</td>
    </tr>
    <tr>
      	<td><ct:FWLabel key="EMAIL"/></td>
      	<td><input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong"></td>
    </tr>
    <tr>
    	<td><ct:FWLabel key="GCA2014_LISTE"/></td>
    	<td>
    		<select name="liste" id="liste">
    			<option id="ecritures" value="ecritures" <%if ("ecritures".equals(viewBean.getListe())) {%>selected="selected"<%}%> ><ct:FWLabel key="GCA2014_JOURNAL_ECRITURES"/></option>
    			<option id="paiementEtranger" value="paiementEtranger" <%if ("paiementEtranger".equals(viewBean.getListe())) {%>selected="selected"<%}%>><ct:FWLabel key="GCA2014_JOURNAL_PAIEMENT_ETRANGER"/></option>
    			<option id="suspens" value="suspens" <%if ("suspens".equals(viewBean.getListe())) {%>selected="selected"<%}%>><ct:FWLabel key="GCA2014_JOURNAL_SUSPENS"/></option>
    		</select>
    	</td>
    </tr>
    <tr>
   		<td><ct:FWLabel key="GCA2014_TYPE"/></td>
      	<td>
        	<input id="recap" type="radio" name="showDetail" value="0" <%if (viewBean.getShowDetail() == 0) {%>checked="checked"<%}%>> <label for="recap"><ct:FWLabel key="GCA2014_RECAPITULATIF"/></label><br>
        	<input id="listeDetail" type="radio" name="showDetail" value="1" <%if (viewBean.getShowDetail() == 1) {%><%}%>> <label for="listeDetail"><ct:FWLabel key="GCA2014_LISTE_DETAILLE"/></label><br>
        	<input id="listeRegroupement" type="radio" name="showDetail" value="2" <%if (viewBean.getShowDetail() == 2) {%><%}%>> <label for="listeRegroupement"><ct:FWLabel key="GCA2014_REGROUPEMENT_SECTION"/></label>
      	</td>
    </tr>
    
    <TR id="compteAnnexe">
		<td><ct:FWLabel key="COMPTEANNEXE_DU"/></td>
  		<TD>
   			<input id="fromIdExterneRole" type="text" name="fromIdExterneRole" value="">&nbsp; au &nbsp;
   			<input id="toIdExterneRole" type="text" name="toIdExterneRole" value="">
   		</TD>
    </TR>
    
	<TR>
		<td><ct:FWLabel key="TYPE_IMPRESSION"/></td>
  		<TD>
   			<input type="radio" class="typeImp" id="radPdf" name="typeImpression" value="pdf" <%=pdfChecked%>/>PDF&nbsp;
   			<input type="radio" class="typeImp" name="typeImpression" value="xls" <%=xlsChecked%>/>Excel
   		</TD>
    </TR> 
        <script type="text/javascript">
        var $liste = $("#liste");
        var $compteAnnexe = $("#compteAnnexe");
        var $recap = $("#recap");
        var $listeDetail = $("#listeDetail");
        var $listeRegroupement = $("#listeRegroupement");
        var $fromIdExterneRole = $("#fromIdExterneRole");
        var $toIdExterneRole = $("#toIdExterneRole");
        
        function typeHide(){
        	$compteAnnexe.hide();
        	$fromIdExterneRole.val("");
			$toIdExterneRole.val("");
        }
        
        $(function(){ 
        	$compteAnnexe.hide();
        	
        	$recap.click(function(){
        		typeHide();
    		});
        	$listeDetail.click(function(){
        		if ($liste.val()!="ecritures"){
        			typeHide();
        		} else {
            		$compteAnnexe.show();
            	}
            });
        	$listeRegroupement.click(function(){
        		if ($liste.val()!="ecritures"){
        			typeHide();
        		} else {
            		$compteAnnexe.show();
            	}
            });
        	
        	$liste.change(function(){
        		if ($liste.val() !== "ecritures"){
        			typeHide();
        		} else if ($liste.val() == "ecritures" && $("#recap:checked").length){
        			typeHide();
        		} else  {
        			$compteAnnexe.show();
            	}
        	})        	
        });
        
        </script>    
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>