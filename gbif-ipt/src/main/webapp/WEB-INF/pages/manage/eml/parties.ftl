<#include "/WEB-INF/pages/inc/header.ftl">
	<title><@s.text name='manage.metadata.parties.title'/></title>
	<script type="text/javascript" src="${baseURL}/js/jconfirmaction.jquery.js"></script>
	<style>
	.2col-lft, .2col-rgt{
		width: 45%;
	}
	.right{
	float: right;
	text-align: right;
	padding-left: 15px;
	padding-bottom: 15px;
	}
	</style>

<script type="text/javascript">
$(document).ready(function(){
	$('.confirm').jConfirmAction({question : "<@s.text name="basic.confirm"/>", yesAnswer : "<@s.text name="basic.yes"/>", cancelAnswer : "<@s.text name="basic.no"/>"});
	var partiesCount = parseInt($("#parties .party:last-child").attr("id").split("-")[1]);
	$("#save").click(function() {
		if(partiesCount==99999){
			$('#party-'+partiesCount).remove();
		}
	});
	$("#add").click(function(event) {
		event.preventDefault();
		if(partiesCount==99999){
			$('#party-'+partiesCount).attr("id","party-0");
			$('#party-0').slideDown('slow');
			partiesCount=0;
		}else {
			partiesCount++;
			var newParty=$('#party-0').clone();
			newParty.hide();
			newParty.attr("id","party-"+partiesCount);
			newParty.appendTo('#parties').slideDown('slow');
			$("#party-"+partiesCount+" #eml\\.associatedParties\\[0\\]\\.firstName").attr("id","eml.associatedParties["+partiesCount+"].firstName");
			$("#party-"+partiesCount+" #eml\\.associatedParties\\[0\\]\\.lastName").attr("id","eml.associatedParties["+partiesCount+"].lastName");
			$("#party-"+partiesCount+" #eml\\.associatedParties\\[0\\]\\.phone").attr("id","eml.associatedParties["+partiesCount+"].phone");
			$("#party-"+partiesCount+" #eml\\.associatedParties\\[0\\]\\.role").attr("id","eml.associatedParties["+partiesCount+"].role");
			$("#party-"+partiesCount+" #eml\\.associatedParties\\["+partiesCount+"\\]\\.firstName").attr("name","eml.associatedParties["+partiesCount+"].firstName");
			$("#party-"+partiesCount+" #eml\\.associatedParties\\["+partiesCount+"\\]\\.lastName").attr("name","eml.associatedParties["+partiesCount+"].lastName");
			$("#party-"+partiesCount+" #eml\\.associatedParties\\["+partiesCount+"\\]\\.phone").attr("name","eml.associatedParties["+partiesCount+"].phone");
			$("#party-"+partiesCount+" #eml\\.associatedParties\\["+partiesCount+"\\]\\.role").attr("name","eml.associatedParties["+partiesCount+"].role");
			$("#party-"+partiesCount+" #eml\\.associatedParties\\["+partiesCount+"\\]\\.firstName").attr("value","");
			$("#party-"+partiesCount+" #eml\\.associatedParties\\["+partiesCount+"\\]\\.lastName").attr("value","");
			$("#party-"+partiesCount+" #eml\\.associatedParties\\["+partiesCount+"\\]\\.phone").attr("value","");
			//$("#party-"+partiesCount+" #eml\\.associatedParties\\["+partiesCount+"\\]\\.role").attr("value","");
			$("#party-"+partiesCount+" #removeLink-0").attr("id", "removeLink-"+partiesCount);
			$("#removeLink-"+partiesCount).click(function(event) {
				removeParty(event);
			});
		}
	});
		
	$(".removeLink").click(function(event) {
		removeParty(event);
	});
		
	function removeParty(event){
		event.preventDefault();
		var $target = $(event.target);
		var index=$target.attr("id").split("-")[1];
		$('#party-'+index).slideUp('slow', function() { $(this).remove(); } );
  		// TODO reorder parties indexes after remove
		// $('#party-0').remove();
	}
		
});
</script>


	<#assign sideMenuEml=true />
<#include "/WEB-INF/pages/inc/menu.ftl">

<h1><@s.text name='manage.metadata.parties.title'/>: <em>${ms.resource.title!ms.resource.shortname}</em></h1>

<p><@s.text name='manage.metadata.parties.intro'/></p>


<#include "/WEB-INF/pages/macros/forms.ftl"/>
<form class="topForm" action="metadata-${section}.do" method="post">
<div id="parties">
<#assign next_agent_index=0 />
<#list eml.associatedParties as agent>
	<#assign next_agent_index=agent_index+1>
	<div id="party-${agent_index}" class="party">
	<div class="right">
      <a id="removeLink-${agent_index}" class="removeLink" href="">[ <@s.text name='metadata.removethis'/> <@s.text name='manage.metadata.parties.item'/> ]</a>
    </div>
    </br>
	<div class="2col">
  		<@input name="eml.associatedParties[${agent_index}].firstName" i18nkey="eml.associatedParties.firstName" keyBase="" size=40/>
  		<@input name="eml.associatedParties[${agent_index}].lastName" i18nkey="eml.associatedParties.lastName" keyBase="" size=40/>
  	</div>
	<div class="2col">
  		<@input name="eml.associatedParties[${agent_index}].phone" i18nkey="eml.associatedParties.phone" keyBase="" size=40/>
  		<@select name="eml.associatedParties[${agent_index}].role" i18nkey="eml.associatedParties.role" value="${eml.associatedParties[agent_index].role}" options=roleOptions />
  	</div>
  	</div>
</#list>
<#if next_agent_index==0>
<div id="party-99999" class="party" style="display:none;">
<h2>New Party</h2>
	<div class="right">
      <a id="removeLink-0" class="removeLink" href="">[ <@s.text name='metadata.removethis'/> <@s.text name='manage.metadata.parties.item'/> ]</a>
    </div>
    </br>
	<div class="2col">
  		<@input name="eml.associatedParties[0].firstName" i18nkey="eml.associatedParties.firstName" keyBase="" size=40/>
  		<@input name="eml.associatedParties[0].lastName" i18nkey="eml.associatedParties.lastName" keyBase="" size=40/>
  	</div>
	<div class="2col">
  		<@input name="eml.associatedParties[0].phone" i18nkey="eml.associatedParties.phone" keyBase="" size=40/>
  		<@select name="eml.associatedParties[0].role" i18nkey="eml.associatedParties.role" value="" options=roleOptions />
  	</div>
</div>
</#if>
</div>
<div id='buttons' class="buttons">
  	<a id="add" href=""><@s.text name='metadata.addnew'/> <@s.text name='manage.metadata.parties.item'/></a></br></br>
    <@s.submit name="save" key="button.save"/>
 	<@s.submit name="cancel" key="button.cancel"/>
  </div>	
</form>

<#include "/WEB-INF/pages/inc/footer.ftl">
