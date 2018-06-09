(function() {
	var actualizarHora = function() {
		// Obtenemos la fecha
		var fecha = new Date(), horas = fecha.getHours();
		minutos = fecha.getMinutes(), segundos = fecha.getSeconds();

		var pHoras = document.getElementById("horas");
		pHoras.textContent = horas;

		var pMinutos = document.getElementById("minutos");
		pMinutos.textContent = minutos;

		var pSegundos = document.getElementById("segundos");
		pSegundos.textContent = segundos;
	};

	actualizarHora();
	var intervalo = setInterval(actualizarHora, 1000);
}())
