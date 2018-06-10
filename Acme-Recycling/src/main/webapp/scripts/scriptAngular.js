var app = angular.module("app", []);

// Todo lo que añadamos como parametro en nuestra function del controlador se instancian ya que son singleton
// app.controller("PruebaController", function($scope,$log, $http) {
app.controller("PruebaController", [
		'$scope', '$log', '$http', function($scope, $log, $http) {
			$http.get("http://localhost:8080/Acme-Recycling/materialJSON/listJSON.do").success(function(data, status, headers, config) {

				// $log.debug() es similar a poner console.log()
				$log.debug(data);
				$log.debug("status: " + status);
				$log.debug("headers: " + headers);
				$log.debug("config: " + config);

			}).error(function(err, status, headers, config) {
				$log.debug("Ha fallado la petición. Estado HTTP:" + status);
			});
		}
]);
