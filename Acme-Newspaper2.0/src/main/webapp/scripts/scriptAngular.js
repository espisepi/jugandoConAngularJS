var app = angular.module("app", [
	'ngMessages'
]);

// Todo lo que añadamos como parametro en nuestra function del controlador se instancian ya que son singleton
// app.controller("PruebaController", function($scope,$log, $http) {
app.controller("PruebaController", [
		'$scope', '$log', '$http', function($scope, $log, $http) {
			// Creamos la variable que guardaremos nuestro JSON
			$scope.mensaje = "HOLA MUNDO";
		}
]);
