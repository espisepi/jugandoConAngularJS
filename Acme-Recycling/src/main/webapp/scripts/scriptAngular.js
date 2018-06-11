var app = angular.module("app", []);

// Todo lo que a�adamos como parametro en nuestra function del controlador se instancian ya que son singleton
// app.controller("PruebaController", function($scope,$log, $http) {
app.controller("PruebaController", [
		'$scope', '$log', '$http', function($scope, $log, $http) {
			// Creamos la variable que guardaremos nuestro JSON
			$scope.materiales = [];

			// Obtenemos el Json por medio del singletone $http
			$http.get("http://localhost:8080/Acme-Recycling/materialJSON/listJSON.do").success(function(data, status, headers, config) {

				// $log.debug() es similar a poner console.log()
				$log.debug(data);

				// Guardamos en nuestro array materiales el json obtenido
				$scope.materiales = data;

			}).error(function(err, status, headers, config) {
				$log.debug("Ha fallado la petici�n. Estado HTTP:" + status);
			});
		}
]);
