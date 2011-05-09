<script type="text/javascript">
$(document).ready(function(){
	var	itemsCount=-1;
	calcNumberOfItems();
	
	function calcNumberOfItems(){
		var lastItem = $("#items .item:last-child").attr("id");
		if(lastItem != undefined)
			itemsCount=parseInt(lastItem.split("-")[1]);
		else
			itemsCount=-1;
	}
	
	$("#plus").click(function(event) {
		event.preventDefault();
		addNewItem(true);
	});
		
	$(".removeLink").click(function(event) {
		removeItem(event);
	});
	
	$("[id^=plus-subItem]").click(function(event) {
			addNewSubItem(event);
	});
	
	$("[id^=trash]").click(function(event) {
			removeSubItem(event);			
	});
	
	function addNewSubItem(event){
		event.preventDefault();
		var baseItem = $("#item-"+$(event.target).attr("id").split("-")[2]);
		// calculating the last taxon index.
		var idBaseItem = baseItem.attr("id");
		var lastIndex = $("#"+idBaseItem+" .sub-item:last-child").attr("id");
		if(lastIndex == undefined) {
			lastIndex = 0;
		} else {
			lastIndex = parseInt(lastIndex.split("-")[1])+1;
		}			
		// cloning the taxonItem and setting the corresponding id.
		var subBaseItem = $("#subItem-9999").clone();			
		// setting the ids to the rest of the components of the taxomItem
		$("#"+idBaseItem+" #subItems").append(subBaseItem);						
		// setting the ids to the rest of the components of the taxonItem.
		setSubItemIndex(baseItem, subBaseItem, lastIndex);	
		subBaseItem.slideDown('slow');
	}
		
		function setSubItemIndex(baseItem, subItem, subBaseIndex) {
			<#switch "${section}">
  				<#case "taxcoverage">
					subItem.attr("id", "subItem-"+subBaseIndex);
					$("#"+baseItem.attr("id")+" #"+subItem.attr("id")).find("[id$='scientificName']").attr("id", "eml.taxonomicCoverages["+baseItem.attr("id").split("-")[1]+"].taxonKeywords["+subBaseIndex+"].scientificName").attr("name", function() {return $(this).attr("id");});
					$("#"+baseItem.attr("id")+" #"+subItem.attr("id")).find("[for$='scientificName']").attr("for", "eml.taxonomicCoverages["+baseItem.attr("id").split("-")[1]+"].taxonKeywords["+subBaseIndex+"].scientificName");
					$("#"+baseItem.attr("id")+" #"+subItem.attr("id")).find("[id$='commonName']").attr("id", "eml.taxonomicCoverages["+baseItem.attr("id").split("-")[1]+"].taxonKeywords["+subBaseIndex+"].commonName").attr("name", function() {return $(this).attr("id");});
					$("#"+baseItem.attr("id")+" #"+subItem.attr("id")).find("[for$='commonName']").attr("for", "eml.taxonomicCoverages["+baseItem.attr("id").split("-")[1]+"].taxonKeywords["+subBaseIndex+"].commonName");
					$("#"+baseItem.attr("id")+" #"+subItem.attr("id")).find("[id$='rank']").attr("id", "eml.taxonomicCoverages["+baseItem.attr("id").split("-")[1]+"].taxonKeywords["+subBaseIndex+"].rank").attr("name", function() {return $(this).attr("id");});
					$("#"+baseItem.attr("id")+" #"+subItem.attr("id")).find("[for$='rank']").attr("for", "eml.taxonomicCoverages["+baseItem.attr("id").split("-")[1]+"].taxonKeywords["+subBaseIndex+"].rank");
					$("#"+baseItem.attr("id")+" #"+subItem.attr("id")).find("[id^='trash']").attr("id", "trash-"+baseItem.attr("id").split("-")[1]+"-"+subBaseIndex).attr("name", function() {return $(this).attr("id");});
					$("#trash-"+baseItem.attr("id").split("-")[1]+"-"+subBaseIndex).click(function(event) {
						removeSubItem(event);
					});
					<#break>
				<#default>
			</#switch>		
		}
		
		function removeSubItem(event) {
			event.preventDefault();
			var $target = $(event.target);
			$("#item-"+$target.attr("id").split("-")[1]+" #subItem-"+$target.attr("id").split("-")[2]).slideUp("slow", function() {
				var indexItem = $(this).find("[id^='trash']").attr("id").split("-")[1];
				$(this).remove();
				$("#item-"+indexItem+" .sub-item").each(function(index) {
					var indexItem = $(this).find("[id^='trash']").attr("id").split("-")[1];
					setSubItemIndex($("#item-"+indexItem), $(this), index);					
				});
			});
		}
	
	function addNewItem(effects){
		var newItem=$('#baseItem').clone();
		if(effects) newItem.hide();
		newItem.appendTo('#items');
		if(effects) newItem.slideDown('slow');
		
		initHelp("#baseItem");
		
		setItemIndex(newItem, ++itemsCount);
	}
		
	function removeItem(event){
		event.preventDefault();
		var $target = $(event.target);
		$('#item-'+$target.attr("id").split("-")[1]).slideUp('slow', function() { 
			$(this).remove();
			$("#items .item").each(function(index) { 
					setItemIndex($(this), index);
				});
			calcNumberOfItems();
		});
	}
	
	function setItemIndex(item, index){
		item.attr("id","item-"+index);
		$("#item-"+index+" .removeLink").attr("id", "removeLink-"+index);
		$("#removeLink-"+index).click(function(event) {
			removeItem(event);
		});	
		
	  <#switch "${section}">
  		<#case "parties">
  			$("#item-"+index+" [id$='firstName']").attr("id", "eml.associatedParties["+index+"].firstName").attr("name", function() {return $(this).attr("id");});
			$("#item-"+index+" [for$='firstName']").attr("for", "eml.associatedParties["+index+"].firstName");
  			$("#item-"+index+" [id$='lastName']").attr("id", "eml.associatedParties["+index+"].lastName").attr("name", function() {return $(this).attr("id");});
			$("#item-"+index+" [for$='lastName']").attr("for", "eml.associatedParties["+index+"].lastName");
  			$("#item-"+index+" [id$='position']").attr("id", "eml.associatedParties["+index+"].position").attr("name", function() {return $(this).attr("id");});
			$("#item-"+index+" [for$='position']").attr("for", "eml.associatedParties["+index+"].position");
  			$("#item-"+index+" [id$='organisation']").attr("id", "eml.associatedParties["+index+"].organisation").attr("name", function() {return $(this).attr("id");});
			$("#item-"+index+" [for$='organisation']").attr("for", "eml.associatedParties["+index+"].organisation");
  			$("#item-"+index+" [id$='address']").attr("id", "eml.associatedParties["+index+"].address.address").attr("name", function() {return $(this).attr("id");});
			$("#item-"+index+" [for$='address']").attr("for", "eml.associatedParties["+index+"].address.address");
  			$("#item-"+index+" [id$='city']").attr("id", "eml.associatedParties["+index+"].address.city").attr("name", function() {return $(this).attr("id");});
			$("#item-"+index+" [for$='city']").attr("for", "eml.associatedParties["+index+"].address.city");
  			$("#item-"+index+" [id$='province']").attr("id", "eml.associatedParties["+index+"].address.province").attr("name", function() {return $(this).attr("id");});
			$("#item-"+index+" [for$='province']").attr("for", "eml.associatedParties["+index+"].address.province");
  			$("#item-"+index+" [id$='country']").attr("id", "eml.associatedParties["+index+"].address.country").attr("name", function() {return $(this).attr("id");});
			$("#item-"+index+" [for$='country']").attr("for", "eml.associatedParties["+index+"].address.country");
  			$("#item-"+index+" [id$='phone']").attr("id", "eml.associatedParties["+index+"].phone").attr("name", function() {return $(this).attr("id");});
			$("#item-"+index+" [for$='phone']").attr("for", "eml.associatedParties["+index+"].phone");
  			$("#item-"+index+" [id$='email']").attr("id", "eml.associatedParties["+index+"].email").attr("name", function() {return $(this).attr("id");});
			$("#item-"+index+" [for$='email']").attr("for", "eml.associatedParties["+index+"].email");
  			$("#item-"+index+" [id$='homepage']").attr("id", "eml.associatedParties["+index+"].homepage").attr("name", function() {return $(this).attr("id");});
			$("#item-"+index+" [for$='homepage']").attr("for", "eml.associatedParties["+index+"].homepage");
  			$("#item-"+index+" [id$='role']").attr("id", "eml.associatedParties["+index+"].role").attr("name", function() {return $(this).attr("id");});
			$("#item-"+index+" [for$='role']").attr("for", "eml.associatedParties["+index+"].role");
    	<#break>
    	<#case "methods">
			$("#item-"+index+" textarea").attr("id", "eml.methodSteps["+index+"]");	
			$("#item-"+index+" label").attr("for", "eml.methodSteps["+index+"]");		
			$("#item-"+index+" textarea").attr("name", "eml.methodSteps["+index+"]");
		<#break>
 		<#case "citations">
			$("#item-"+index+" input").attr("id","eml.bibliographicCitationSet.bibliographicCitations["+index+"]");
			$("#item-"+index+" input").attr("name","eml.bibliographicCitationSet.bibliographicCitations["+index+"]");
			$("#item-"+index+" label").attr("for","eml.bibliographicCitationSet.bibliographicCitations["+index+"]");
		<#break>
		<#case "collections">
			$("#item-"+index+" input").attr("id",function() {
				var parts=$(this).attr("id").split(".");var n=parseInt(parts.length)-1;
				return "eml.jgtiCuratorialUnits["+index+"]."+parts[n]; });
			$("#item-"+index+" select").attr("id","type-"+index).unbind().change(function() {
				updateSubitem($(this));
			});
			$("#item-"+index+" label").attr("for",function() {
				var parts=$(this).attr("for").split(".");var n=parseInt(parts.length)-1;
				return "eml.jgtiCuratorialUnits["+index+"]."+parts[n]; });
			$("#item-"+index+" input").attr("name",function() {return $(this).attr("id"); });
			$("#item-"+index+" select").attr("name",function() {return $(this).attr("id"); });
			$("#item-"+index+" .subitem").attr("id","subitem-"+index);			
			var selectValue = $("#item-"+index+" #type-"+index).attr("value");
			if(selectValue == "COUNT_RANGE") {
				$("#item-"+index+" [id^='range-']").attr("id", "range-"+index).attr("name", function() {
						$(this).css("display", "");
						return $(this).attr("id"); 
				});
			} else {
				$("#item-"+index+" [id^='uncertainty-']").attr("id", "uncertainty-"+index).attr("name", function() {
						$(this).css("display", "");
						return $(this).attr("id");
				});
			}
		<#break>
		<#case "physical">
			$("#item-"+index+" input").attr("id",function() {
				var parts=$(this).attr("id").split(".");var n=parseInt(parts.length)-1;
				return "eml.physicalData["+index+"]."+parts[n]; });
			$("#item-"+index+" select").attr("id",function() {
				var parts=$(this).attr("id").split(".");var n=parseInt(parts.length)-1;
				return "eml.physicalData["+index+"]."+parts[n]; });
			$("#item-"+index+" label").attr("for",function() {
				var parts=$(this).attr("for").split(".");var n=parseInt(parts.length)-1;
				return "eml.physicalData["+index+"]."+parts[n]; });		
			$("#item-"+index+" input").attr("name",function() {return $(this).attr("id"); });
			$("#item-"+index+" select").attr("name",function() {return $(this).attr("id"); });
		<#break>
		<#case "keywords">
			$("#item-"+index+" input").attr("id",function() {
				var parts=$(this).attr("id").split(".");var n=parseInt(parts.length)-1;
				return "eml.keywords["+index+"]."+parts[n]; });
			$("#item-"+index+" textarea").attr("id",function() {
				var parts=$(this).attr("id").split(".");var n=parseInt(parts.length)-1;
				return "eml.keywords["+index+"]."+parts[n]; });	
			$("#item-"+index+" select").attr("id",function() {
				var parts=$(this).attr("id").split(".");var n=parseInt(parts.length)-1;
				return "eml.keywords["+index+"]."+parts[n]; });
			$("#item-"+index+" label").attr("for",function() {
				var parts=$(this).attr("for").split(".");var n=parseInt(parts.length)-1;
				return "eml.keywords["+index+"]."+parts[n]; });		
			$("#item-"+index+" input").attr("name",function() {return $(this).attr("id"); });
			$("#item-"+index+" textarea").attr("name",function() {return $(this).attr("id"); });
			$("#item-"+index+" select").attr("name",function() {return $(this).attr("id"); });
		<#break>
		<#case "taxcoverage">
			$("#item-"+index+" #plus-subItem").attr("id", "plus-subItem-"+index);
			$("#plus-subItem-"+index).click(function(event){
				event.preventDefault();
				addNewSubItem(event);
			});			
			$("#item-"+index+" [id$='description']").attr("id", "eml.taxonomicCoverages["+index+"].description").attr("name", function() {return $(this).attr("id");});
			$("#item-"+index+" [for$='description']").attr("for", "eml.taxonomicCoverages["+index+"].description");	
		<#break>
		<#default>
  	  </#switch>		
	}
	
	$("[id^='type-']").change(function() {
		updateSubitem($(this));
	});
	
	function updateSubitem(select) {		
		<#switch "${section}">
  			<#case "collections">
				var selection = select.attr("value");
				var index = select.attr("id").split("-")[1];
				if(selection == "COUNT_RANGE") {
					$("#subitem-"+index+" [id^='uncertainty-']").fadeOut(function() {
						$(this).remove();
						var newItem = $("#range-99999").clone().css("display", "").attr("id", "range-"+index).attr("name", function() {$(this).attr("id")});
						$("#subitem-"+index).append(newItem).hide().fadeIn(function() {
							setItemIndex($("#item-"+index), index);
						});
					});		
				} else {			
					$("#subitem-"+index+" [id^='range-']").fadeOut(function() {
						$(this).remove();
						var newItem = $("#uncertainty-99999").clone().css("display", "").attr("id", "uncertainty-"+index).attr("name", function() {$(this).attr("id")});
						$("#subitem-"+index).append(newItem).hide().fadeIn(function() {
							setItemIndex($("#item-"+index), index);
						});
					});
				}			
    		<#break>
    		<#default>
    	</#switch>
		
	}
		
});
</script>