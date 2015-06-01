<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:jsf="http://xmlns.jcp.org/jsf"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core">

<head jsf:id="head">

<title>JSF 2.2</title>

<script>
	function getLocation() {

		if (navigator.geolocation) {

			navigator.geolocation.getCurrentPosition(showPosition);

		} else {

			x.innerHTML = "Geolocation is not supported by this browser.";

		}

	}

	function showPosition(position) {

		var inputLat = document.getElementById('form:latitude');

		inputLat.value = position.coords.latitude;

		var inputLong = document.getElementById('form:longitude');

		inputLong.value = position.coords.longitude;

	}

	getLocation();
</script>

</head>

<body jsf:id="body" onload="">

	<form jsf:id="form">

		<input type="text" jsf:id="lat" placeholder="Digite a latitude"
			jsf:value="#{latLongBean.lagitude}" /> <br></br> <input type="text"
			jsf:id="lon" placeholder="Digite a logitude"
			jsf:value="#{latLongBean.longitude}" />

		<button jsf:id="salvar">

			Salvar

			<f:ajax execute="@form" event="click"
				listener="#{latLongBean.saveLatLong}" />

		</button>

		<br></br>

	</form>

</body>

</html>