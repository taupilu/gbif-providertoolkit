<%@ include file="/common/taglibs.jsp"%>

<div id="regionTreeBox"></div>
<script>
	var resourceId = <s:property value="resource_id"/>;
	var regionJustOpened = 1;
	
	regiontree=new dhtmlXTreeObject($('regionTreeBox'),"100%","100%",0);
	regiontree.setImagePath("/scripts/dhtmlxtree/imgs/");
	regiontree.enableCheckBoxes(false);
	regiontree.enableDragAndDrop(false);
	regiontree.enableTreeImages(false);
	regiontree.enableHighlighting(true);
	regiontree.enableTreeLines(true);
	regiontree.attachEvent("onClick",onRegionNodeSelect); //set function object to call on node select
	regiontree.attachEvent("onOpenStart",onRegionNodeOpen); // onOpenStart 
	regiontree.setXMLAutoLoading("/ajax/regionTreeNodes.html?resource_id=<s:property value="resource_id"/>");
	regiontree.loadXML("/ajax/regionTreeNodes.html?resource_id=<s:property value="resource_id"/>"); //load root level from xml
	
	function onRegionNodeSelect(nodeId){
		if (regionJustOpened>0){
			//auto click when opening a new node. prevend this 
			regionJustOpened=0;
		}else{					
			// only open terminal nodes
			if (taxtree.hasChildren(nodeId)<1){
				var regionUrl = '/occRegion.html?resource_id='+resourceId+'&id='+nodeId;;
				window.location.href=regionUrl
			}
		}
	}
	
	function onRegionNodeOpen(nodeId, state){
		// state: Current open state of tree item. 0 - item has not childs, -1 - item closed, 1 - item opened.
		if (state>-1){
			regionJustOpened=1;
		}
		return true;
	}
</script>
