var app = angular.module("app", []);

// Todo lo que añadamos como parametro en nuestra function del controlador se instancian ya que son singleton
// app.controller("PruebaController", function($scope,$log, $http) {
app.controller("PruebaController", [
		'$scope', '$log', '$http', function($scope, $log, $http) {
			// Creamos la variable que guardaremos nuestro JSON
			$scope.comments = [];
			$scope.mensaje = "HOLITA";

			// Obtenemos el Json por medio del singletone $http
			$http.get("http://localhost:8080/Acme-Recycling/commentAngular/list.do").success(function(data, status, headers, config) {

				// $log.debug() es similar a poner console.log()
				$log.debug("data: " + data);

				// Guardamos en nuestro array materiales el json obtenido
				$scope.comments = data;

			}).error(function(err, status, headers, config) {
				$log.debug("Ha fallado la petición. Estado HTTP:" + status);
			});
		}
]);

app.controller("TestController", [
		'$scope', function($scope) {
			$scope.mensaje = "HOLA JEJE";
		}
]);
