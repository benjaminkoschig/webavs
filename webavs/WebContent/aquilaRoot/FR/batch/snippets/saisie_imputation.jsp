<%@ page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@ page import="globaz.globall.db.BSession"%>
<%
	FWViewBeanInterface localViewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
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
      	if (nbrSaut==6)  zone = i;          
      }
   	}   
   
	if (nbrSaut>=6) {
		alert("L'annexe est limitée à 6 lignes");
	 	text = test.substring(0,zone);
		document.getElementById('annexeTexteLibre').value = text;
	}	
}

function NombreMax(test)
{		
	if ( test.length > 484) {
 		test = test.substring(0,484); 		
 		document.getElementById('annexeTexteLibre').value = test;
	}
}
</script>
<TR>
	<TD class="label">Email</TD>	
	<TD class="control" colspan="7"><INPUT type="text" name="email" value="<%=((BSession) localViewBean.getISession()).getUserEMail()%>"></TD>
</TR>
<TR>
	<TD class="label">Annexe</TD> 
	<TD class="control" colspan="5">		
		<TEXTAREA name="annexeTexteLibre" id="annexeTexteLibre" cols="40" rows="2"  OnKeyDown='NombreMax(this.value)' onchange='ChangeEsp(this.value)' ></TEXTAREA>		
	</TD>
</TR>

