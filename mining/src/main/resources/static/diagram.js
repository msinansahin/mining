function nodeStyle() {
	return [
			// The Node.location comes from the "loc" property of the node data,
			// converted by the Point.parse static method.
			// If the Node.location is changed, it updates the "loc" property of
			// the node data,
			// converting back using the Point.stringify static method.
			new go.Binding("location", "loc", go.Point.parse)
				.makeTwoWay(go.Point.stringify), {
					fromSpot : go.Spot.BottomSide, // coming out from right side
					toSpot : go.Spot.TopRightSides, // going into at left side
					// the Node.location is at the center of each node
					locationSpot : go.Spot.Center,
					// isShadowed: true,
					// shadowColor: "#888",
					// handle mouse enter/leave events to show/hide the ports
					// mouseEnter: function (e, obj) { showPorts(obj.part, true); },
					// mouseLeave: function (e, obj) { showPorts(obj.part, false); }
				}
			];
}

/**
 * bağlantı çeşitlerini oluşturur
 * @param diagram
 * @returns
 */
function linkTemplates(diagram) {
	var $ = go.GraphObject.make; // for conciseness in defining templates

	_.forEach(LINK_TEMPLATES, function(value) {
		diagram.linkTemplateMap.add(value.category,
				$(go.Link,  // the whole link panel
						  {
						    routing: go.Link.AvoidsNodes,
						    curve: go.Link.JumpOver, //go.Link.Bezier,
						    corner: 4, toShortLength: 4,
						    relinkableFrom: true,
						    relinkableTo: true,
						    reshapable: true,
						    resegmentable: true,
						    // mouse-overs subtly highlight links:
						    mouseEnter: function(e, link) { link.findObject("HIGHLIGHT").stroke = "rgba(30,144,255,0.2)"; },
						    mouseLeave: function(e, link) { link.findObject("HIGHLIGHT").stroke = "transparent"; }
						  },
						  //new go.Binding("points").makeTwoWay(),
						  $(go.Shape,  // the highlight shape, normally transparent
						    { isPanelMain: true, strokeWidth: 8, stroke: "transparent", name: "HIGHLIGHT" }),
						  $(go.Shape,  // the link path shape
						    { isPanelMain: true, stroke: "gray", strokeWidth: Math.abs(value.sayi * 1) + 2 }),
						  $(go.Shape,  // the arrowhead
						    { toArrow: "standard", stroke: null, fill: "gray"}),
							$(go.TextBlock, { segmentOffset: new go.Point(0, -10) },                        // this is a Link label
							  new go.Binding("text", "text"))  
						));
	});
}

function addDiagramListener(diagram, properties) {
	diagram.addDiagramListener("ObjectSingleClicked",
			function(e, graphObj) {
				var part = e.subject.part;
				if (!(part instanceof go.Link) && properties) {
					properties.load(part.data);
				} else if (properties) {
					properties.remove();
				}
			});	
}

/**
 * 
 * @param data type of Data
 * @param properties node özelliklerinin yazıldığı panel işlerini yapan sınıf
 * @returns
 */
function Diagram(data, properties) {
	var $ = go.GraphObject.make; // for conciseness in defining templates
	var lightText = 'whitesmoke';
	this.myDiagram = $(go.Diagram, "myDiagramDiv",  // create a Diagram for the DIV HTML element
			{
				initialContentAlignment: go.Spot.Center,  // center the content
				maxSelectionCount: 1,
				"undoManager.isEnabled": true,  // enable undo & redo
				layout: $(go.LayeredDigraphLayout, { direction: 90, layerSpacing: 25, setsPortSpots: false }),
				// initialAutoScale: go.Diagram.Uniform,  // an initial automatic zoom-to-fit
			});

	// define a simple Node template
	this.myDiagram.nodeTemplate = 
		$(go.Node, "Auto",  // the Shape will go around the TextBlock
				$(go.Shape, "RoundedRectangle", { strokeWidth: 0},
					// Shape.fill is bound to Node.data.color
					new go.Binding("fill", "color")),
					$(go.TextBlock,
							{ margin: 8 },  // some room around the text
							// TextBlock.text is bound to Node.data.oid
							new go.Binding("text", "key"))
		);

	linkTemplates(this.myDiagram);

	/*
	this.myDiagram.linkTemplateMap.add("flow",
		  $(go.Link,
		    { toShortLength: 8 },
		    $(go.Shape,
		      { stroke: "blue", strokeWidth: 5 }),
		    $(go.Shape,
		      {
		        fill: "blue",
		        stroke: null,
		        toArrow: "Standard",
		        scale: 2.5
		      })
		  ));
	*/
	
	this.myDiagram.nodeTemplateMap.add("",  // the default category
		$(go.Node, "Spot", nodeStyle(),
		  // the main object is a Panel that surrounds a TextBlock with a rectangular Shape
		  $(go.Panel, "Auto",
		    $(go.Shape, "RoundedRectangle",
		      { fill: "#00A9C9", stroke: null },
		      new go.Binding("figure", "figure")),
		    $(go.TextBlock,
		      {
		        font: "bold 11pt Helvetica, Arial, sans-serif",
		        stroke: lightText,
		        margin: 8,
		        maxSize: new go.Size(160, NaN),
		        wrap: go.TextBlock.WrapFit
		      },
		      new go.Binding("text").makeTwoWay())
		  )
		));	

	this.myDiagram.nodeTemplateMap.add("start",
		$(go.Node, "Spot", nodeStyle(),
		  $(go.Panel, "Auto",
		    $(go.Shape, "Circle",
		      { minSize: new go.Size(20, 20), fill: "#79C900", stroke: null }),
		    $(go.TextBlock, "Start",
		      { font: "bold 11pt Helvetica, Arial, sans-serif", stroke: lightText },
		      new go.Binding("text"))
		  )
		));	
	
	this.myDiagram.nodeTemplateMap.add("end",
		$(go.Node, "Spot", nodeStyle(),
		  $(go.Panel, "Auto",
		    $(go.Shape, "Circle",
		      { minSize: new go.Size(20, 20), fill: "#DC3C00", stroke: null }),
		    $(go.TextBlock, "End",
		      { font: "bold 11pt Helvetica, Arial, sans-serif", stroke: lightText },
		      new go.Binding("text"))
		  )
		));	  
		

		// create the model data that will be represented by Nodes and Links
		this.myDiagram.model = new go.GraphLinksModel(data.nodeDataArray, data.linkDataArray);
		addDiagramListener(this.myDiagram, properties);
}
