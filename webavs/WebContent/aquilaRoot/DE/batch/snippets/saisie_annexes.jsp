<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.aquila.util.CODateUtils"%>
<%
	String annexeDecisionLabel = "1 Kopie von unserer Verfügung vom";
	String annexeSommationLabel = "1 Kopie von unserer Mahnung vom";
	String annexeCDPLabel = "1 Zahlungsbefehl";
	String annexeExtraitArtLabel = "1 Auszug Art. 41 bis AHVV und 42 AHVV";
%>
<script language='javascript'>
function ChangeEsp(test)
{	
	var text=0;
	var nbrSaut=0;	
	var zone = 0;
	
	for (var i=0; i<test.length; i++)
	{
      carac = test.substring(i,i+2);
      if (carac.match("\r\n")){
      	nbrSaut++;
      	if (nbrSaut==2)  zone = i;          
      }
   	}   
   
	if (nbrSaut>=2){ 
		alert("Die Beilage ist auf 2 freie Linien beschränkt");
		text = test.substring(0,zone);
		text = text.replace(/\r\n/g,"\r\n ");
		text = text.replace(/\r\n  /g,"\r\n ");   
		document.getElementById('annexeTexteLibre').value = text;
	}else 
	{
		test = test.replace(/\r\n/g,"\r\n ");
		test = test.replace(/\r\n  /g,"\r\n ");   
		document.getElementById('annexeTexteLibre').value = test;
	}   
		
}

function NombreMax(test)
{		
	if ( test.length > 151) {
 		test = test.substring(0,151); 		
 		document.getElementById('annexeTexteLibre').value = test;
	}
}
</script>
<TR>
	<TD class="label">Beilagen</TD>
	<TD class="control" colspan="5">
		<INPUT type="hidden" name="annexeDecisionLabel" value="<%=annexeDecisionLabel%>">
		<INPUT type="checkbox" name="annexeDecisionChecked" checked>&nbsp;<%=annexeDecisionLabel%>&nbsp;
		<ct:FWCalendarTag name="dateDecision" doClientValidation="CALENDAR" value="<%=CODateUtils.getTodayPlusDaysDDsMMsYYYY(0)%>"/>
	</TD>
</TR>
<TR>
	<TD></TD>
	<TD class="control" colspan="5">
		<INPUT type="hidden" name="annexeSommationLabel" value="<%=annexeSommationLabel%>">
		<INPUT type="checkbox" name="annexeSommationChecked" checked>&nbsp;<%=annexeSommationLabel%>&nbsp;
		<ct:FWCalendarTag name="dateSommation" doClientValidation="CALENDAR" value="<%=CODateUtils.getTodayPlusDaysDDsMMsYYYY(0)%>"/>
	</TD>
</TR>
<TR>
	<TD></TD>
	<TD class="control" colspan="5">
		<INPUT type="hidden" name="annexeCDPLabel" value="<%=annexeCDPLabel%>">
		<INPUT type="checkbox" name="annexeCDPChecked" checked>&nbsp;<%=annexeCDPLabel%>
	</TD>
</TR>
<TR>
	<TD></TD>
	<TD class="control" colspan="5">
		<INPUT type="hidden" name="annexeExtraitArtLabel" value="<%=annexeExtraitArtLabel%>">
		<INPUT type="checkbox" name="annexeExtraitArtChecked" checked>&nbsp;<%=annexeExtraitArtLabel%>
	</TD>
</TR>
<TR>
	<TD></TD>
	<TD class="control" colspan="5">		
		<TEXTAREA name="annexeTexteLibre" id="annexeTexteLibre" cols="40" rows="2" OnKeyDown='NombreMax(this.value)' onchange='ChangeEsp(this.value)'> </TEXTAREA>
	</TD>
</TR>
