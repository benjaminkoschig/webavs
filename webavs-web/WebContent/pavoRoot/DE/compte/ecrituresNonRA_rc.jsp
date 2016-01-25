<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.pavo.db.inscriptions.CIJournalViewBean viewBean = (globaz.pavo.db.inscriptions.CIJournalViewBean)session.getAttribute ("viewBean");

		bButtonNew=false;
		String numeroJournal=viewBean.getIdJournal();
		idEcran = "CCI0031";


%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="Javascript">


usrAction="pavo.compte.ecrituresNonRA.lister";
function checkKey(input){
	var re = /[^a-zA-Z\-'הצüִײÜיטפא,\s].*/
	if (re.test(input.value)) {
		input.value = input.value.substr(0,input.value.length-1);
	}
}

function changeName(input)
{
	input.value=input.value.replace('ה','AE');
	input.value=input.value.replace('צ','OE');
	input.value=input.value.replace('ü','UE');
	input.value=input.value.replace('ִ','AE');
	input.value=input.value.replace('ײ','OE');
	input.value=input.value.replace('Ü','UE');

	input.value=input.value.replace('י','E');
	input.value=input.value.replace('ט','E');
	input.value=input.value.replace('פ','O');
	input.value=input.value.replace('א','A');

	input.value=input.value.toUpperCase();
}
</script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
				Suche der unbekannten IK's im VR
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>


						<INPUT type="checkbox" name="ecrituresNonRA" checked>
						<INPUT type="checkbox" name="exclureRA" checked>
						<SCRIPT>

						document.getElementById('ecrituresNonRA').className= 'hidden';
						document.getElementById('ecrituresNonRA').style.display='none';
						</SCRIPT>
						<SCRIPT>
						document.getElementById('exclureRA').className='hidden';
						document.getElementById('exclureRA').style.display='none';
						</SCRIPT>
						<TR>
						<TD width="80">Journal-Nr.</TD>
						<TD width="700" colspan="10"><INPUT size="11" type="text" name="forIdJournal" value="<%=numeroJournal%>" class="disabled" readonly tabIndex="-1">
						<INPUT type="text" name="description" value="<%=viewBean.getDescription()%>" class="disabled" readonly size="70" tabIndex="-1" ></TD>
						</TR>
						<TR>
						<TD>SVN</TD>
						<TD>
						<nss:nssPopup name ="fromAvs" avsMinNbrDigit="99" nssMinNbrDigit="99" />
						</TD>
						<TD>Name, Vorname</TD>
						<TD width="200"><INPUT type="text" name="likeNomPrenom" size="40" onKeyDown="checkKey(this)" onChange="changeName(this)" onKeyUp="checkKey(this)"></TD>
						<TD>Versicherungsausweis</TD>
						<TD><INPUT type="checkbox" name="certificat"></TD>
						</TR>

	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>