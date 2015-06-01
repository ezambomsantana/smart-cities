<!DOCTYPE html>
<%@page import="java.sql.Date"%>
<%@page import="java.util.List"%>
<%@page import="com.santana.sc.model.Bus"%>
<html>
  <head>
    <title>Simple Map</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <style>
      html, body, #map-canvas {
        height: 100%;
        margin: 0px;
        padding: 0px
      }
    </style>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
    <script>

    function initialize() {

    	  var myLatlng = new google.maps.LatLng(-23.573741625000004, -46.726239);
    	  var mapOptions = {
    	    zoom: 4,
    	    center: myLatlng
    	  }
    	  var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
    	  var text = '';
			<%
			List<Bus> buses = (List<Bus>) request.getAttribute("lista");
			
			for (Bus bus : buses) {
			
				String name = bus.getName();
				int code = bus.getCode();
				String letters = bus.getLetters();
				
				double lat = bus.getLat();
				double lon = bus.getLon();
				Date data = bus.getDateBus();
			
			%>
			text = '<%= bus.getCode() %> - <%= bus.getName()%>';
    	  
				mark(map, <%= lat %>, <%= lon %>, text);
    	  
    	  <%
    	  
			}
    	  %>
    	}

    function mark(map, lat, lon, text) {

  	  var myLatlng = new google.maps.LatLng(lat, lon);
  	  var marker = new google.maps.Marker({
	      position: myLatlng,
	      map: map,
	      title: text
	  });
    }
    
    	google.maps.event.addDomListener(window, 'load', initialize);

    </script>
  </head>
  <body>
  	<h2>Lista Clientes</h2>
 

	
		<table>
			<tr>
				<td>Nome</td>
				<td>Code</td>
				<td>Letreiro</td>
				<td>Lat</td>
				<td>Long</td>
				<td>Data</td>
				
			</tr>
			
				<%
	buses = (List<Bus>) request.getAttribute("lista");
	
	for (Bus bus : buses) {
	
		String name = bus.getName();
		int code = bus.getCode();
		String letters = bus.getLetters();
		
		double lat = bus.getLat();
		double lon = bus.getLon();
		Date data = bus.getDateBus();
	
	%>
 
		
		
				
				<tr>
					<td><%= name %></td>
					<td><%= code %></td>
					<td><%= letters %></td>
					<td><%= lat %></td>
					<td><%= lon %></td>
					<td><%= data %></td>

				</tr>
<% } %>
		
		</table>
		
  
    <div id="map-canvas"></div>
  </body>
</html>