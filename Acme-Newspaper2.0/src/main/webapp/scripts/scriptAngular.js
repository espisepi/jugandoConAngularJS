angular.module("app", [
	"ngRoute"
]).config(function($routeProvider) {
	$routeProvider.when("/", {
		templateUrl : "/views/layout.jsp",
		controller : "ControllerList"

	}).when("", {
		templateUrl : "",
		controller : ""

	});
});

// Todo lo que añadamos como parametro en nuestra function del controlador se instancian ya que son singleton
// app.controller("PruebaController", function($scope,$log, $http) {
angular.module("app").controller("PruebaController", [
		'$scope', '$log', '$http', function($scope, $log, $http) {
			// Creamos la variable que guardaremos nuestro JSON
			$scope.mensaje = "HOLA MUNDO";
		}
]);

angular.module("app").controller("ControllerList", [
		'$scope', '$log', '$http', function($scope, $log, $http) {
			console.log("Aqui");

			function getContacts() {
				$http.get("http://localhost:8080/Acme-Newspaper/user/list.do").then(function(response) {
					$scope.contacts = response.data;
					console.log(response.data)
				});
			}

			getContacts();
		}
]);
