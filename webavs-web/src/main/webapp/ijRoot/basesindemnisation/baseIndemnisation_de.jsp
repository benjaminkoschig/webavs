<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/ijRoot/ijtaglib.tld" prefix="ij"%>
<%

	idEcran="PIJ0023";
	globaz.ij.vb.basesindemnisation.IJBaseIndemnisationViewBean viewBean = (globaz.ij.vb.basesindemnisation.IJBaseIndemnisationViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdBaseIndemisation();
	String likeNSS=request.getParameter("likeNSS");
	if (likeNSS==null){
		likeNSS = "";
	}

	bButtonUpdate = bButtonUpdate && viewBean.isModifierPermis();
	bButtonValidate = bButtonValidate && viewBean.isModifierPermis();
	bButtonDelete = bButtonDelete && viewBean.isSupprimerPermis();
	bButtonCancel = false;

	globaz.ij.db.prononces.IJPrononce prononce = viewBean.loadPrononce(null);
    String jourFinPrononce = "";
	String moisFinPrononce = "";
	String anneeFinPrononce = "";
	String dateFinPrononce = "";
    String jourDebutPrononce = "";
	String moisDebutPrononce = "";
	String anneeDebutPrononce = "";
	String dateDebutPrononce = "";

    if(prononce!=null){
    	dateFinPrononce = globaz.jade.client.util.JadeStringUtil.removeChar(prononce.getDateFinPrononce(),'.');
        jourFinPrononce = Integer.toString(globaz.globall.util.JACalendar.getDay(dateFinPrononce));
		moisFinPrononce = Integer.toString(globaz.globall.util.JACalendar.getMonth(dateFinPrononce));
		anneeFinPrononce = Integer.toString(globaz.globall.util.JACalendar.getYear(dateFinPrononce));
		dateDebutPrononce = globaz.jade.client.util.JadeStringUtil.removeChar(prononce.getDateDebutPrononce(),'.');
		jourDebutPrononce = Integer.toString(globaz.globall.util.JACalendar.getDay(dateDebutPrononce));
		moisDebutPrononce = Integer.toString(globaz.globall.util.JACalendar.getMonth(dateDebutPrononce));
		anneeDebutPrononce = Integer.toString(globaz.globall.util.JACalendar.getYear(dateDebutPrononce));
	}

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.ij.api.prononces.IIJPrononce"%>
<%@page import="globaz.ij.application.IJApplication"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" />
<ct:menuChange displayId="options" menuId="ij-basesindemnisations"
	showTab="options">
	<ct:menuSetAllParams key="selectedId"
		value="<%=viewBean.getIdBaseIndemisation()%>" />
	<ct:menuSetAllParams key="forNoBaseIndemnisation"
		value="<%=viewBean.getIdBaseIndemisation()%>" />

	<%if(IIJPrononce.CS_PETITE_IJ.equals(viewBean.getCsTypeIJ())||
			 IIJPrononce.CS_GRANDE_IJ.equals(viewBean.getCsTypeIJ())){ %>
	<ct:menuActivateNode active="no" nodeId="calculerait" />
	<ct:menuActivateNode active="no" nodeId="calculeraa" />
	<%}else if(IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(viewBean.getCsTypeIJ())){ %>
	<ct:menuActivateNode active="no" nodeId="calculergp" />
	<ct:menuActivateNode active="no" nodeId="calculeraa" />
	<%}else if(IIJPrononce.CS_ALLOC_ASSIST.equals(viewBean.getCsTypeIJ())){ %>
	<ct:menuActivateNode active="no" nodeId="calculergp" />
	<ct:menuActivateNode active="no" nodeId="calculerait" />
	<%}%>

	<%if(viewBean.getCsEtat().equals(globaz.ij.api.basseindemnisation.IIJBaseIndemnisation.CS_ANNULE)){%>
	<ct:menuActivateNode active="no" nodeId="corrigerbi" />
	<%} else {%>
	<ct:menuActivateNode active="yes" nodeId="corrigerbi" />
	<%}%>
</ct:menuChange>

<SCRIPT language="javascript">

	function add() {
	    document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_BASE_INDEMNISATION%>.ajouter";
	}

	function upd() {
		document.forms[0].elements('month').disabled="true";
		document.forms[0].elements('year').disabled="true";
	}

	function validate() {
	    state = true;
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_BASE_INDEMNISATION%>.ajouter";
	    }else{
	        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_BASE_INDEMNISATION%>.modifier";
	    }

	    state = validateFields();

	    return state;
	}

	function validateFields(){
		// si l'imposition a la source est cochee dans le prononce
		<%if(viewBean.getIsSoumisImpositionSource()){%>
	
			// si le canton d'imposition et le taux d'imposition ne sont pas fixes
			if(document.forms[0].elements('csCantonImpotSource').value=="" && (
			   document.forms[0].elements('tauxImpotSource').value=="" ||
			   document.forms[0].elements('tauxImpotSource').value=="0.00")){

			   errorObj.text = "<%=viewBean.getSession().getLabel("JSP_ERROR_IMPOSITION_SOURCE_DANS_PRONONCE")%>";
			   showErrors();
			   return false;

			}		
			return validCsMotifInterruption();

		<%}else{%>
			return validCsMotifInterruption();
		<%}%>
	}

	function validCsMotifInterruption() {
		var nombreJoursInterruption = document.forms[0].elements('nombreJoursInterruption').value;
		if(nombreJoursInterruption != null && nombreJoursInterruption != "" && nombreJoursInterruption != "0"){
			var csMotifInterruption = document.forms[0].elements("csMotifInterruption").value;
			if(csMotifInterruption == null || csMotifInterruption == ""){
				errorObj.text = "<%=viewBean.getSession().getLabel("JSP_ERROR_MOTIF_REFUS_OBLIGATOIRE")%>";
				showErrors();
				return false;
			}
		}	
		return true;
	}
	
	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
			document.forms[0].elements('userAction').value="back";
		}else{
		  document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_BASE_INDEMNISATION%>.chercher";
		}
	}

	function del() {
		if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_BASE_INDEMNISATION%>.supprimer";
	        document.forms[0].submit();
	    }
	}

	function getCalendar() {
		document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_BASE_INDEMNISATION%>.displayCalendar";
	    document.forms[0].submit();
	}

	function getFullCalendar() {
		document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_BASE_INDEMNISATION%>.displayFullMonthCalendar";
	    document.forms[0].submit();
	}

	function init(){
		<% if (!viewBean.isNew()) { %>
			document.forms[0].elements('month').disabled="true";
			document.forms[0].elements('year').disabled="true";
		<% } %>
		checkDateFinPrononcePeriodeBaseInd();
	}


	function postInit() {
		setMask(document.forms[0].elements('isPeriodeEtendue'));
	}




	function setMask(checkBox){
		if (checkBox.checked){
  			document.all('bloc-base-mens-1').style.display = 'none';
  			document.all('bloc-base-mens-2').style.display = 'none';
  			document.all('bloc-periode-etendue').style.display = 'block';

  		} else {
  			document.all('bloc-base-mens-1').style.display = 'block';
  			document.all('bloc-base-mens-2').style.display = 'block';
  			document.all('bloc-periode-etendue').style.display = 'none';
  		}

	}

	function setCalendar(data) {

		if (document.forms[0].elements('CAL_JOUR_1')!=null) {
				document.forms[0].elements('CAL_JOUR_1').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_2')!=null) {
				document.forms[0].elements('CAL_JOUR_2').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_3')!=null) {
				document.forms[0].elements('CAL_JOUR_3').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_4')!=null) {
				document.forms[0].elements('CAL_JOUR_4').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_5')!=null) {
				document.forms[0].elements('CAL_JOUR_5').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_6')!=null) {
				document.forms[0].elements('CAL_JOUR_6').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_7')!=null) {
				document.forms[0].elements('CAL_JOUR_7').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_8')!=null) {
				document.forms[0].elements('CAL_JOUR_8').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_9')!=null) {
				document.forms[0].elements('CAL_JOUR_9').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_10')!=null) {
				document.forms[0].elements('CAL_JOUR_10').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_11')!=null) {
				document.forms[0].elements('CAL_JOUR_11').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_12')!=null) {
				document.forms[0].elements('CAL_JOUR_12').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_13')!=null) {
				document.forms[0].elements('CAL_JOUR_13').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_14')!=null) {
				document.forms[0].elements('CAL_JOUR_14').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_15')!=null) {
				document.forms[0].elements('CAL_JOUR_15').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_16')!=null) {
				document.forms[0].elements('CAL_JOUR_16').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_17')!=null) {
				document.forms[0].elements('CAL_JOUR_17').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_18')!=null) {
				document.forms[0].elements('CAL_JOUR_18').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_19')!=null) {
				document.forms[0].elements('CAL_JOUR_19').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_20')!=null) {
				document.forms[0].elements('CAL_JOUR_20').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_21')!=null) {
				document.forms[0].elements('CAL_JOUR_21').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_22')!=null) {
				document.forms[0].elements('CAL_JOUR_22').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_23')!=null) {
				document.forms[0].elements('CAL_JOUR_23').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_24')!=null) {
				document.forms[0].elements('CAL_JOUR_24').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_25')!=null) {
				document.forms[0].elements('CAL_JOUR_25').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_26')!=null) {
				document.forms[0].elements('CAL_JOUR_26').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_27')!=null) {
				document.forms[0].elements('CAL_JOUR_27').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_28')!=null) {
				document.forms[0].elements('CAL_JOUR_28').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_29')!=null) {
			document.forms[0].elements('CAL_JOUR_29').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_30')!=null) {
			document.forms[0].elements('CAL_JOUR_30').value=data;
		}
		if (document.forms[0].elements('CAL_JOUR_31')!=null) {
			document.forms[0].elements('CAL_JOUR_31').value=data;
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

  // controle que la periode de la base d'indemnisation soit plus petite
  // ou egale a la date de fin du prononce.
  function checkDateFinPrononcePeriodeBaseInd(){


  	var dateDebutOk = true;
  	var dateFinOk = true;


  	if (document.forms[0].elements('isPeriodeEtendue').checked) {
  		//check de la date de debut
	  	if(<%=!globaz.jade.client.util.JadeStringUtil.isEmpty(dateDebutPrononce)%>){


		// check de la date de debut
			year = document.forms[0].elements('dateDebutPeriodeEtendue').value.substr(6,4);
			month = document.forms[0].elements('dateDebutPeriodeEtendue').value.substr(3,2);
			day = document.forms[0].elements('dateDebutPeriodeEtendue').value.substr(0,2);

			if(year<<%=anneeDebutPrononce%>){
				dateDebutOk = false;
			}else if(year==<%=anneeDebutPrononce%>){
		  		if(month<<%=moisDebutPrononce%>){
		  			dateDebutOk = false;
		  		}else if(month==<%=moisDebutPrononce%>){
		  			if(day<<%=jourDebutPrononce%>){
		  				dateDebutOk = false;
		  			}else{
		  				dateDebutOk = true;
		  			}
		  		}else{
		  			dateDebutOk = true;
		  		}
		  	}else{
		  		dateDebutOk = true;
		  	}
		}

		// check de la date de fin
	  	if(<%=!globaz.jade.client.util.JadeStringUtil.isEmpty(dateFinPrononce)%>){

	  	year = document.forms[0].elements('dateFinPeriodeEtendue').value.substr(6,4);
			month = document.forms[0].elements('dateFinPeriodeEtendue').value.substr(3,2);
			day = document.forms[0].elements('dateFinPeriodeEtendue').value.substr(0,2);

			if(year><%=anneeFinPrononce%>){
				dateFinOk = false;
			}else if(year==<%=anneeFinPrononce%>){

		  		if(month><%=moisFinPrononce%>){
		  			dateFinOk = false;
		  		}else if(month==<%=moisFinPrononce%>){

		  			if(day><%=jourFinPrononce%>){
		  				dateFinOk = false;
		  			}else{
		  				dateFinOk = true;
		  			}
		  		}else{
		  			dateFinOk = true;
		  		}
		  	}else{
		  		dateFinOk = true;
		  	}
		  }

  	}
  	else {

	  	// check de la date de debut
	  	if(<%=!globaz.jade.client.util.JadeStringUtil.isEmpty(dateDebutPrononce)%>){

			if(document.forms[0].elements('year').value<<%=anneeDebutPrononce%>){
				dateDebutOk = false;
			}else if(document.forms[0].elements('year').value==<%=anneeDebutPrononce%>){

		  		if(document.forms[0].elements('month').value<<%=moisDebutPrononce%>){
		  			dateDebutOk = false;
		  		}else if(document.forms[0].elements('month').value==<%=moisDebutPrononce%>){

		  			if(document.forms[0].elements('jourDebut').value<<%=jourDebutPrononce%>){
		  				dateDebutOk = false;
		  			}else{
		  				dateDebutOk = true;
		  			}
		  		}else{
		  			dateDebutOk = true;
		  		}
		  	}else{
		  		dateDebutOk = true;
		  	}
		}

	  	// check de la date de fin
	  	if(<%=!globaz.jade.client.util.JadeStringUtil.isEmpty(dateFinPrononce)%>){

			if(document.forms[0].elements('year').value><%=anneeFinPrononce%>){
				dateFinOk = false;
			}else if(document.forms[0].elements('year').value==<%=anneeFinPrononce%>){

		  		if(document.forms[0].elements('month').value><%=moisFinPrononce%>){
		  			dateFinOk = false;
		  		}else if(document.forms[0].elements('month').value==<%=moisFinPrononce%>){

		  			if(document.forms[0].elements('jourFin').value><%=jourFinPrononce%>){
		  				dateFinOk = false;
		  			}else{
		  				dateFinOk = true;
		  			}
		  		}else{
		  			dateFinOk = true;
		  		}
		  	}else{
		  		dateFinOk = true;
		  	}
		  }
  	}


	  // si pas de probleme on affiche le bouton de validation, il n'y a pas de message d'erreur
	  if(dateDebutOk && dateFinOk){
	  	enableBtnVal();
	  	document.all("alertText").innerText = "";
	  }else{
	  	disableBtnVal();

	  	if(!dateFinOk){
			document.all("alertText").innerText = "<%=java.text.MessageFormat.format(viewBean.getSession().getLabel("PRONONCE_PLUS_EN_COURS_POUR_DATE_FIN_BI"),
			                                                                         new Object[] {prononce.getDateFinPrononce()})%>";
	  	}else if(!dateDebutOk){
			document.all("alertText").innerText = "<%=java.text.MessageFormat.format(viewBean.getSession().getLabel("PRONONCE_PAS_EN_COURS_POUR_DATE_DEBUT_BI"),
			                                                                         new Object[] {prononce.getDateDebutPrononce()})%>";
	  	}

	  }
	}

	function enableBtnVal() {
		document.all("btnVal").disabled=false;
	}

	function disableBtnVal() {
		document.all("btnVal").disabled=true;
	}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
<span class="postItIcon"><ct:FWNote
		sourceId="<%=viewBean.getIdBaseIndemisation()%>"
		tableSource="<%=IJApplication.KEY_POSTIT_BASES_INDEMNISATION%>" />
</span>
<ct:FWLabel key="JSP_BASE_INDEMNISATION" />
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>

<tr>
	<td width="30%">&nbsp;</td>
	<td width="40%" colspan="2">&nbsp;</td>
	<td width="30%">&nbsp;</td>
<TR>
	<TD valign="top" align="left"><LABEL for="idPrononce"><ct:FWLabel
				key="JSP_BASE_IND_NO_PRONONCE" />
	</LABEL>
	</TD>
	<TD colspan="3" align="left"><span><%=viewBean.getFullDescriptionPrononce()[0]%><br>
			<%=viewBean.getDetailRequerant()%></span> <INPUT type="hidden"
		name="idPrononce" value="<%=viewBean.getIdPrononce()%>"> <INPUT
		type="hidden" name="forIdPrononce"
		value="<%=viewBean.getIdPrononce()%>"> <INPUT type="hidden"
		name="csTypeIJ" value="<%=viewBean.getCsTypeIJ()%>"> <INPUT
		type="hidden" name="descr0"
		value="<%=viewBean.getFullDescriptionPrononce()[0]%>"> <INPUT
		type="hidden" name="descr1"
		value="<%=viewBean.getFullDescriptionPrononce()[1]%>"></TD>
</TR>

<TR>
	<td colspan="4"><br />
	</td>
</TR>

<TR>
	<TD colspan="4">
		<table width="100%">
			<tr>
				<td><ct:FWLabel key="JSP_TYPE_HEBERGEMENT" /> <ct:select
						name="csTypeHebergement"
						defaultValue="<%=viewBean.getCsTypeHebergement()%>"
						disabled="disabled" styleClass="forceDisable">
						<ct:optionsCodesSystems csFamille="IJTYPHEBER" />
					</ct:select></td>
				<td><ct:FWLabel key="JSP_TYPE_IJ" /> <input type="text"
					value="<%=viewBean.getCsTypeIJLibelle()%>" disabled="disabled"
					class="forceDisable"></td>
				<td><ct:FWLabel key="JSP_PERIODE_ETENDUE" /> <input
					type="checkbox" name="isPeriodeEtendue" onclick="setMask(this);"
					<%=viewBean.getIsPeriodeEtendue().booleanValue()?"CHECKED":""%>>
				</td>
			</tr>
		</table></TD>
</TR>

<TR>
	<td colspan="4"><hr />
	</td>
</TR>

<TBODY id="bloc-base-mens-1">
	<TR>
		<td colspan="4">
			<TABLE cellspacing="5" align="left">
				<TR>
					<TD><ct:FWLabel key="JSP_BASE_IND_NO_MOIS" />
					</TD>
					<TD><ct:FWListSelectTag data="<%=viewBean.getMonths()%>"
							defaut="<%=viewBean.getMonth()%>" name="month" /> <script
							language="javascript">
									 	  	element = document.getElementById("month");
									 	  	element.onchange=function() {getFullCalendar();}
									 	  	element.className = "forceDisable";
									 	</script></TD>
					<TD><ct:FWLabel key="JSP_BASE_IND_ANNEE" />
					</TD>
					<TD><ct:FWListSelectTag data="<%=viewBean.getYears()%>"
							defaut="<%=viewBean.getYear()%>" name="year" /> <script
							language="javascript">
									 	  	element = document.getElementById("year");
									 	  	element.onchange=function() {getFullCalendar();}
									 	  	element.className = "forceDisable";
									 	</script></TD>
					<TD>&nbsp;</TD>
				</TR>
				<TR>
					<TD><ct:FWLabel key="JSP_DU" />
					</TD>
					<TD><ct:FWListSelectTag data="<%=viewBean.getDaysDebut()%>"
							defaut="<%=viewBean.getJourDebut()%>" name="jourDebut" /> <script
							language="javascript">
									 	  	element = document.getElementById("jourDebut");
									 	  	element.onchange=function() {getCalendar();}
									 	</script></TD>
					<TD><ct:FWLabel key="JSP_AU" />
					</TD>
					<TD><ct:FWListSelectTag data="<%=viewBean.getDaysFin()%>"
							defaut="<%=viewBean.getJourFin()%>" name="jourFin" /> <script
							language="javascript">
									 	  	element = document.getElementById("jourFin");
									 	  	element.onchange=function() {getCalendar();}
									 	</script></TD>
				</TR>
			</TABLE></td>
	</TR>
	<TR>
		<td colspan="4"><hr />
		</td>
	</TR>
</TBODY>



<TBODY id="bloc-periode-etendue">
	<TR>
		<td colspan="4">
			<TABLE cellspacing="5" align="left">
				<TR>
					<TD><ct:FWLabel key="JSP_DU" />
					</TD>
					<TD><ct:FWCalendarTag name="dateDebutPeriodeEtendue"
							value="<%=viewBean.getDateDebutPeriodeEtendue()%>" /> <script
							language="javascript">
									 	  	element = document.getElementById("dateDebutPeriodeEtendue");
									 	  	element.onblur=function() {fieldFormat(this,'CALENDAR');checkDateFinPrononcePeriodeBaseInd();}
									 	</script></TD>
					<TD><ct:FWLabel key="JSP_AU" />
					</TD>
					<TD><ct:FWCalendarTag name="dateFinPeriodeEtendue"
							value="<%=viewBean.getDateFinPeriodeEtendue()%>" /> <script
							language="javascript">
									 	  	element = document.getElementById("dateFinPeriodeEtendue");
									 	  	element.onblur=function() {fieldFormat(this,'CALENDAR');checkDateFinPrononcePeriodeBaseInd();}
									 	</script></TD>
					<TD align="right"></TD>
				</TR>
			</TABLE></td>
	</TR>
	<TR>
		<td colspan="4"><hr />
		</td>
	</TR>
</TBODY>

<TR>
	<TD colspan="4"><FONT id="alertText" color="red"></FONT>
	</TD>
</TR>



<TR>
	<TD><ct:FWLabel key="JSP_BASE_IND_NBR_JRS_INT" />
	</TD>
	<TD><INPUT type="text" name="nombreJoursInterne"
		value="<%=JadeStringUtil.isIntegerEmpty(viewBean.getNombreJoursInterne())?"":viewBean.getNombreJoursInterne()%>">
	</TD>
	<TD><ct:FWLabel key="JSP_BASE_IND_NBR_JRS_EXT" />
	</TD>
	<TD><INPUT type="text" name="nombreJoursExterne"
		value="<%=viewBean.getNombreJoursExterne()%>">
	</TD>
</TR>

<TR>
	<TD><ct:FWLabel key="JSP_BASE_IND_NBR_JRS_INTERRUPT" />
	</TD>
	<TD><INPUT type="text" name="nombreJoursInterruption"
		value="<%=viewBean.getNombreJoursInterruption()%>">
	</TD>

	<TD><ct:FWLabel key="JSP_BASE_IND_NBR_JRS_MOTIF_INT" />
	</TD>
	<TD><ct:select name="csMotifInterruption" wantBlank="true"
			defaultValue="<%=
			viewBean.getCsMotifInterruption()
			%>">
			<ct:optionsCodesSystems csFamille="IJMOTIFINT" />
		</ct:select></TD>
</TR>

<TR>
	<td colspan="4">&nbsp;</td>
</TR>

<TBODY id="bloc-base-mens-2">
	<tr>
		<td width="30%">&nbsp;</td>
		<td width="40%" colspan="2"><ij:IJCalendar
				data="<%=viewBean.getMois()%>" />
		</td>
		<td width="30%"><INPUT type="button" name="interne"
			value="Interne"
			onClick="javascript:setCalendar('<%=globaz.ij.api.basseindemnisation.IIJBaseIndemnisation.IJ_CALENDAR_INTERNE%>');"><br>
			<INPUT type="button" name="externe" value="Externe"
			onClick="javascript:setCalendar('<%=globaz.ij.api.basseindemnisation.IIJBaseIndemnisation.IJ_CALENDAR_EXTERNE%>');"><br>
			<INPUT type="button" name="nonAtteste" value="Non attesté"
			onClick="javascript:setCalendar('<%=globaz.ij.api.basseindemnisation.IIJBaseIndemnisation.IJ_CALENDAR_NON_ATTESTE%>');">
		</td>
	</TR>
</TBODY>
<TR>
	<td colspan="4">&nbsp;</td>
</TR>

<TR>
	<TD><ct:FWLabel key="JSP_BASE_IND_REMARQUE" />
	</TD>
	<TD><textarea name="remarque" cols="40" rows="5"><%=viewBean.getRemarque()%></textarea>
	</TD>
	<TD><ct:FWLabel key="JSP_BASE_IND_ETAT" />
	</TD>
	<TD><ct:select name="csEtat"
			defaultValue="<%=viewBean.getCsEtat()%>" disabled="true"
			styleClass="forceDisable">
			<ct:optionsCodesSystems csFamille="IJETABASIN" />
		</ct:select></TD>
</TR>

<TR>
	<TD><ct:FWLabel key="JSP_CANTON_IMPOT_SOURCE" />
	</TD>
	<TD><ct:FWCodeSelectTag codeType="PYCANTON"
			name="csCantonImpotSource"
			defaut="<%=viewBean.getCsCantonImpotSource()%>" wantBlank="true" />
	</TD>
	<TD><ct:FWLabel key="JSP_TAUX_IMPOT_SOURCE" />
	</TD>
	<TD><INPUT type="text" name="tauxImpotSource"
		value="<%=viewBean.getTauxImpotSource()%>" class="montant"
		onchange="validateFloatNumber(this);"
		onkeypress="return filterCharForFloat(window.event);">
	</TD>
</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<SCRIPT language="javaScript">
		setMask(document.forms[0].elements('isPeriodeEtendue'));
	</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>