<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.vb.motifsDeRefus.RFRechercheMotifsDeRefusViewBean"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<%@page import="globaz.cygnus.utils.RFUtils"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.cygnus.vb.motifsDeRefus.RFSoinMotif"%>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/tabsStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/dataTableStyle.css"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%
	idEcran="PRF0032";
	RFRechercheMotifsDeRefusViewBean viewBean = (RFRechercheMotifsDeRefusViewBean) session.getAttribute("viewBean");
	
	bButtonCancel = true;		
	if(viewBean.getIsMotifRefusSysteme())
		bButtonDelete = false;
	else
		bButtonDelete = true;
%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

  servlet = "<%=(servletContext + mainServletPath)%>";
 
  function add() {	  	
    document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_RECHERCHE_MOTIFS_DE_REFUS%>.ajouter";        
  }

  function upd() {	      
		if(!$('[name=descriptionFR]').attr("disabled"))
			$('a').css("color", "blue");	
		else
			$('a').css("color", "gray");	
					
		if(<%=viewBean.getIsMotifRefusSysteme()%>){
			document.getElementsByName("descriptionFR")[0].disabled=true;
			document.getElementsByName("descriptionIT")[0].disabled=true;
			document.getElementsByName("descriptionDE")[0].disabled=true;				
		}
		else{
			document.getElementsByName("descriptionFR")[0].disabled=false;
			document.getElementsByName("descriptionIT")[0].disabled=false;
			document.getElementsByName("descriptionDE")[0].disabled=false;			
		}
  }
  
	function clearListesTypesDeSoins() {			
						
		document.getElementsByName("codeTypeDeSoin")[0].value="";
		document.getElementsByName("codeSousTypeDeSoin")[0].value="";
		document.getElementsByName("codeTypeDeSoinList")[0].value="";
		document.getElementsByName("codeSousTypeDeSoinList")[0].value="";
		document.getElementsByName("imgCodeSousTypeDeSoin")[0].src=<%="'"+request.getContextPath()+viewBean.getImageError()+"'"%>;
		document.getElementsByName("imgCodeTypeDeSoin")[0].src=<%="'"+request.getContextPath()+viewBean.getImageError()+"'"%>

	}

  function validate(){
	  
	if(document.forms[0].elements('_method').value == "add")			
		actionNew = "<%=IRFActions.ACTION_RECHERCHE_MOTIFS_DE_REFUS%>.ajouter";
	else
		actionNew = "<%=IRFActions.ACTION_RECHERCHE_MOTIFS_DE_REFUS%>.modifier";
	var myform=document.forms[0];		
	myform.elements("userAction").value = actionNew;	
	myform.action=servlet;
	myform.submit();
  }
  
  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
    	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_RECHERCHE_MOTIFS_DE_REFUS%>.afficher";
    else
      	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_RECHERCHE_MOTIFS_DE_REFUS%>.afficher";
  }

  function del() {
	    if (window.confirm("<ct:FWLabel key='WARNING_RF_MOTIF_REFUS_SUPPRESSION_OBJET'/>")){
        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_RECHERCHE_MOTIFS_DE_REFUS%>.supprimer";
        document.forms[0].submit();
    }
  }
  	
  function init(){
	  
	   // recharger la page rcListe du parent si une modification a ete effectuee        
	<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  	// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}
	<%}%>	

  }

	function postInit(){	

		  //selon si l'on est en read ou en ajout il faut le forcer		  
		if (<%=!viewBean.getIsUpdate()%>) {
			action('add');
		}else{
			if(document.forms[0].elements('_method').value!="upd")
				action('read');
		}
		
		if(!$('[name=descriptionFR]').attr("disabled"))
			$('a').css("color", "blue");	
		else
			$('a').css("color", "gray");
							
		clearListesTypesDeSoins();
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


  function ajouterMotifRefus(){	  	
	  
		actionNew = "<%=IRFActions.ACTION_RECHERCHE_MOTIFS_DE_REFUS%>.ajouterSoinMotif";
		
		var myform=document.forms[0];		
		myform.elements("userAction").value = actionNew;	
		myform.action=servlet;
		myform.submit();		  
  }

  function suppressionSM(idMotifRefus,type, sousType){

	  //execute le code que si les champs sont déjà dégrisés	  
	  if(!$('[name=descriptionFR]').attr("disabled")){
		actionNew = "<%=IRFActions.ACTION_RECHERCHE_MOTIFS_DE_REFUS%>.supprimerCoupleMotifRefusSTS";
		detailLink = "<%=actionNew%>";

		var myform=document.forms[0];
		myform.elements("userAction").value = actionNew;
		$('<input type="hidden"/>').appendTo(myform).attr({
			"name":"idMotifRefus",
			"value":idMotifRefus
		});		
		$('<input type="hidden"/>').appendTo(myform).attr({
			"name":"type",
			"value":type
		});	
		$('<input type="hidden"/>').appendTo(myform).attr({
			"name":"sousType",
			"value":sousType
		});	
		
		myform.action=servlet;		
		myform.submit();			
	  } 	   
  }

  $(function(){
		if(<%=viewBean.getHideHasMontant()%>)
			$('.cacherHasMontant').hide();
		else	
			$('.cacherHasMontant').show();		
	});

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_SAISIE_MOTIFS_REFUS_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>

<TR>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_MOTIF_REFUS_DESCRIPTION_DE" /></TD>	
	<TD><INPUT style="width: 570px" type="text" name="descriptionDE" value="<%=viewBean.getDescriptionDE()%>" /></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_MOTIF_REFUS_DESCRIPTION" /></TD>	
	<TD><INPUT style="width: 570px" type="text" name="descriptionFR" value="<%=viewBean.getDescriptionFR()%>" />
	<INPUT type="hidden" name="isMotifRefusSysteme" value="<%=viewBean.getIsMotifRefusSysteme()%>"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_MOTIF_REFUS_DESCRIPTION_IT" /></TD>	
	<TD><INPUT style="width: 570px" type="text" name="descriptionIT" value="<%=viewBean.getDescriptionIT()%>" /></TD>
</TR>
<TR><TD colspan="6"><br/></TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_MOTIF_REFUS_DESCRIPTION_LONGUE_DE" /></TD>	
	<TD><TEXTAREA style="width: 570px" rows="5" name="descriptionLongueDE"><%=viewBean.getDescriptionLongueDE()%></TEXTAREA></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_MOTIF_REFUS_DESCRIPTION_LONGUE" /></TD>	
	<TD><TEXTAREA style="width: 570px" rows="5" name="descriptionLongueFR"><%=viewBean.getDescriptionLongueFR()%></TEXTAREA></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_MOTIF_REFUS_DESCRIPTION_LONGUE_IT" /></TD>	
	<TD><TEXTAREA style="width: 570px" rows="5" name="descriptionLongueIT"><%=viewBean.getDescriptionLongueIT()%></TEXTAREA></TD>
</TR>
<TR><TD colspan="6"><br/></TD></TR>
<%if(!viewBean.getIsMotifRefusSysteme()){ %>
<TR>
	<TD class="cacherHasMontant"><ct:FWLabel key="JSP_RF_SAISIE_MOTIF_REFUS_HAS_MONTANT" /></TD>	
	<TD class="cacherHasMontant"><INPUT type="checkbox" name="hasMontant" <%="add".equals(request.getParameter("_method")) || viewBean.getHasMontant().booleanValue()==true?"CHECKED":""%>/></TD>	
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<%@ include file="../utils/typeSousTypeDeSoinsListes.jspf"%>
<TR>
	<TD colspan="6">
 	<INPUT type="hidden" name="isSaisieDemande" value="false"/>
 	<INPUT type="hidden" name="isEditSoins" value="false"/>
 	<INPUT type="hidden" name="isSaisieQd" value="false"/>	                	
	</TD>
</TR> 
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
    <TD>
		<INPUT type="button" value="<ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_AK_REQ_AJOUTER"/>" onclick="ajouterMotifRefus()" />
	</TD>
</TR>      
<TR><TD colspan="6">&nbsp;</TD></TR>
<!-- Création du tableau couple type de soin / sous type-->
<TR><TD colspan="4">
<div>
<TABLE>				
<TR>
	<TD  colspan="4">
		<%
			//remplir le tableau soinMotifArray à partir de ce qu'on a en BDD -> trouver l'action où l'on charge le motif de refus
			if(viewBean.getSoinMotifArray().size()>0){
		%>			
		<div class="dataTable">
		<TABLE style="border=1px ridge black " bgcolor="white" cellpadding="5" cellspacing="0" >
			<TR style="border=1px solid black; " >
				<th><ct:FWLabel key="JSP_RF_MOTIF_REFUS_TYPE_SOIN"/></th>
				<th><ct:FWLabel key="JSP_RF_MOTIF_REFUS_SOUS_TYPE_SOIN"/></th>
				<th><ct:FWLabel key="JSP_RF_CONV_EFFACER_LIGNE_TABLE"/></th>
			</TR>
			<% 								
				for(Iterator it = viewBean.getSoinMotifArray().iterator();it.hasNext();){
					RFSoinMotif soinMotif=(RFSoinMotif)it.next();
					if(!soinMotif.getSupprimer()){
			%>
					<TR class="mtd">
						<TD style="border-right=1px solid #C0C0C0"><%=!JadeStringUtil.isEmpty(soinMotif.getTypeDeSoin())?(!"*".equals(soinMotif.getTypeDeSoin())?soinMotif.getTypeDeSoin()+". "+viewBean.getSession().getCodeLibelle(RFUtils.getIdTypeDeSoin(soinMotif.getTypeDeSoin(),viewBean.getSession())):"*"):" "%></TD>
						<TD style="border-right=1px solid #C0C0C0"><%=!JadeStringUtil.isEmpty(soinMotif.getSousTypeDeSoin())?(!"*".equals(soinMotif.getSousTypeDeSoin())?soinMotif.getSousTypeDeSoin()+". "+viewBean.getSession().getCodeLibelle(RFUtils.getIdSousTypeDeSoin(soinMotif.getTypeDeSoin(),soinMotif.getSousTypeDeSoin(),viewBean.getSession())):"*"):" "%></TD>
						<TD style="border-right=1px solid #C0C0C0"><a href="javascript:suppressionSM('<%=soinMotif.getIdMotifRefus().toString() %>','<%=soinMotif.getTypeDeSoin().toString() %>','<%=soinMotif.getSousTypeDeSoin().toString() %>')">suppr.</a></TD>
					</TR>
			<%		
					}
				}
			%>
		</TABLE>   
		<script>$('.mtd:odd').css("background-color", "#E8EEF4");</script> 
		</div>
		<% } %>
	</TD>
</TR> 
</TABLE>
</div>   
<style type="text/css">
	.dataTable { height:expression(this.scrollHeight > 380 ? "380px" : "auto"); }
</style>
<%} %>	
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>