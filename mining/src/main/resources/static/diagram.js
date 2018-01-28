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
					toSpot : go.Spot.TopSide, // going into at left side
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
 * 
 * @param data type of Data
 * @returns
 */
function Diagram(data) {
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
				{
					fromSpot: go.Spot.RightSide,  // coming out from right side
					toSpot: go.Spot.LeftSide
				},   // going into at left side
				$(go.Shape, "RoundedRectangle", { strokeWidth: 0},
					// Shape.fill is bound to Node.data.color
					new go.Binding("fill", "color")),
					$(go.TextBlock,
							{ margin: 8 },  // some room around the text
							// TextBlock.text is bound to Node.data.oid
							new go.Binding("text", "key"))
		);

	/*
	myDiagram.linkTemplate =
	$(go.Link,
		{ curve: go.Link.Bezier },  // Bezier curve
		{ routing: go.Link.AvoidsNodes, corner: 10 },   // link route should avoid nodes
	$(go.Shape),                           // this is the link shape (the line)
	$(go.Shape, { toArrow: "Standard" }),  // this is an arrowhead
	$(go.TextBlock, { segmentOffset: new go.Point(0, -10) },                        // this is a Link label
	  new go.Binding("text", "text"))
	);
	*/

	this.myDiagram.linkTemplatexx =
		$(go.Link,
		{ curve: go.Link.Bezier },
		// { routing: go.Link.AvoidsNodes },
		$(go.Shape),
		$(go.Shape, { toArrow: "Standard" }),
		$(go.TextBlock, { segmentOffset: new go.Point(0, -10) },                        // this is a Link label
		  new go.Binding("text", "text"))
		);

	this.myDiagram.linkTemplate =
		$(go.Link,  // the whole link panel
		  {
		    routing: go.Link.AvoidsNodes,
		    curve: go.Link.JumpOver,
		    corner: 5, toShortLength: 4,
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
		    { isPanelMain: true, stroke: "gray", strokeWidth: 2 }),
		  $(go.Shape,  // the arrowhead
		    { toArrow: "standard", stroke: null, fill: "gray"}),
			$(go.TextBlock, { segmentOffset: new go.Point(0, -10) },                        // this is a Link label
			  new go.Binding("text", "text"))  
  
		);

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
	
	this.myDiagram.nodeTemplateMap.add("",  // the default category
		$(go.Node, "Spot", nodeStyle(),
		  // the main object is a Panel that surrounds a TextBlock with a rectangular Shape
		  $(go.Panel, "Auto",
		    $(go.Shape, "Rectangle",
		      { fill: "#00A9C9", stroke: null },
		      new go.Binding("figure", "figure")),
		    $(go.TextBlock,
		      {
		        font: "bold 11pt Helvetica, Arial, sans-serif",
		        stroke: lightText,
		        margin: 8,
		        maxSize: new go.Size(160, NaN),
		        wrap: go.TextBlock.WrapFit,
		        //editable: true
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
		
		// but use the default Link template, by not setting Diagram.linkTemplate
		
		
		// create the model data that will be represented by Nodes and Links
		/*
		myDiagram.model = new go.GraphLinksModel(
		[
		{ key: "Alpha", color: "lightblue" },
		{ key: "Beta", color: "orange" },
		{ key: "Gamma", color: "lightgreen" },
		{ key: "Delta", color: "pink" },
		{ key: "Sinan", color: "blue" }
		],
		[
		{ from: "Alpha", to: "Beta", "category":"flow", text: "50" },
		{ from: "Alpha", to: "Gamma",  text: "12"},
		{ from: "Beta", to: "Beta", text: "24"},
		{ from: "Gamma", to: "Delta", text: "20" },
		{ from: "Delta", to: "Alpha", text: "40"},
		{ from: "Sinan", to: "Delta",  "category":"flow", text: "5"},
		{ from: "Beta", to: "Sinan", text: "15"}
		]);
		*/

		// create the model data that will be represented by Nodes and Links
		this.myDiagram.model = new go.GraphLinksModel(data.nodeDataArray, data.linkDataArray);

		this.myDiagram.addDiagramListener("ObjectSingleClicked", function(e) {
			var part = e.subject.part;
			if (!(part instanceof go.Link)) {
				console.log("Clicked on " + part.data.key);
			}
		});
}
