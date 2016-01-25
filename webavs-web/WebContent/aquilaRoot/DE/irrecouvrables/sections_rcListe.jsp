<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
	var selectedSections = new Array(); 
</SCRIPT>
<%@ page import="globaz.aquila.db.irrecouvrables.*"%>
<%
	COSectionsListViewBean viewBean = (COSectionsListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "aquila?userAction=aquila.irrecouvrables.sections.afficher&selectedId=";
	//menuName ="optionsDossierContext";
	wantPagination = false;
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<script language="JavaScript">
	$(document).ready(function() {
		 var $selectAllCheckbox=$("#selectAllCheckbox");
		 
		 //click sur la case cocher/décocher tout
		 $selectAllCheckbox.click(function(){
			 selectedSections = new Array();
			 if(this.checked){
				 var idSectionsList="";
				 var checkboxs = $(".checkboxClass"); 
				 checkboxs.attr('checked', true);
				 $.each(checkboxs, function(key, value) {
					 var idSection = $(value).val(); 
					 selectedSections.push(idSection);
				 });
			 }else{
				 $(".checkboxClass").attr('checked',false);
			 }
			 $("#idSectionsList").val(selectedSections.toString());
		 });
		 
		 //click sur une checkbox
		 $(".checkboxClass").click(function(){
			 var idSection = $(this).val();
			 updateIdSectionsList(idSection);
		 });
		 
		//click sur une ligne contenant une checkbox
		 $(".checkboxLine").click(function(){
			 var $checkbox = $(this).closest("tr").find(":checkbox");
			 var idSection = $checkbox.val();
			 if($checkbox.attr('checked')=='checked'){
				 $checkbox.attr('checked',false);
			 }else{
				 $checkbox.attr('checked',true);
			 }
			 updateIdSectionsList(idSection);
		 });
	});
	
	// mise à jour de la liste des idSection
	function updateIdSectionsList(idSection){
		var $idSectionCheckbox = $("#checkbox_"+idSection);
		if($idSectionCheckbox.attr('checked')=='checked'){
			selectedSections.push(idSection);
		}else{
			var keyToDelete;
			for(var i=0;i<selectedSections.length;i++){
				if(selectedSections[i]==idSection){
					keyToDelete=i;
				}
			}
			deleteElement(selectedSections, keyToDelete);
		}
		$("#idSectionsList").val(selectedSections.toString());
	}
	
	//supprime une élément d'un tableau(array)
	function deleteElement(array,a){
		return (a>array.length)?false:(array.splice(a,1));
	}
	
	function submitIrrecouvrable() {
		if (selectedSections.length==0) {
			alert("Il faut sélectionner au moins une section");
		} else {
			document.forms[0].submit();
		}
	}
</script>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    
	<!--<TH>&nbsp;</TH>    -->
	<TH>Nummer</TH> 
	<TH>Datum</TH>
	<TH>Periode</TH>
	<TH>Beschreibung</TH>		
	<TH>Rechnungsbetrag</TH> 
	<TH>Zahlungen Ausgleich</TH> 	
	<TH>Saldo</TH> 
	<TH>Verbandskasse</TH>
	<TH><input type="checkbox" id="selectAllCheckbox" name="selectAllCheckbox" />Uneintreibbar</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
		COSectionsViewBean line = (COSectionsViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdSection()+"'";
	%>
    <!--<TD class="mtd" width="18"><ct:FWOptionSelectorTag name="<%=\"item\"+i%>" selectedId="<%=line.getIdSection()%>"/></TD>-->
	<TD class="mtd checkboxLine" nowrap="nowrap"><%=line.getIdExterne()%></TD>
	<TD class="mtd checkboxLine" nowrap="nowrap"><%=line.getDateSection()%></TD>
	<TD class="mtd checkboxLine" nowrap="nowrap"><%=line.getDateDebutPeriode()+"-"+line.getDateFinPeriode()%></TD>
	<TD class="mtd checkboxLine" nowrap="nowrap"><%=line.getCSTypeSection()%></TD>
	<TD class="mtd checkboxLine" nowrap="nowrap"><%=line.getBaseFormate()%></TD>	
	<TD class="mtd checkboxLine" nowrap="nowrap"><%=line.getPmtCmpFormate()%></TD>		
	<TD class="mtd checkboxLine" nowrap="nowrap"><%=line.getSoldeFormate()%></TD>
	<TD class="mtd checkboxLine" nowrap="nowrap"><%=line.getCaisseProfessionnelleNumero()%></TD>
	<TD class="mtd" nowrap="nowrap"><input type="checkbox" id="checkbox_<%=line.getIdSection()%>" class="checkboxClass"  value="<%=line.getIdSection()%>" name="checkbox_<%=line.getIdSection()%>"></TD>
	
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<tr>
		<TD class="mtd" colspan="8" align="center">
		
			<FORM method="get" target="fr_main">
				<input type="hidden" name="userAction" value="aquila.irrecouvrables.sectionIrrec.afficher">
				<INPUT type="hidden" name="idSectionsList" id="idSectionsList">
				<input type="button" value="Uneintreibbar" onclick="submitIrrecouvrable();">
				<!-- <INPUT type="hidden" name="_method" value="upd"> -->
			</FORM>
		</TD>
	</tr>
	
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>