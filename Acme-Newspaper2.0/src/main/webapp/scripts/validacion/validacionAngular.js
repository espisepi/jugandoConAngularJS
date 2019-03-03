// app.config([
// '$routeProvider', function($routeProvider) {
// $routeProvider.when('/validacion/list', {
// templateUrl : 'views/validacion/list.jsp',
// controller : "ValidacionListController as ValidacionListCtrl",
// resolve : {
// async : [
// 'ValidacionService', function(ItemService) {
// return ValidacionService.getValidacions();
// }
// ]
// }
// }).otherwise({
// redirectTo : '/validacion/list'
// });
// }
// ]);

app.service('ValidacionService', [
		'$http', function($http) {

			this.saveValidacion = function saveValidacion(validacion) {
				return $http({
					method : 'POST',
					url : 'validacion/edit.do',
					params : {
						save : 1,
						textMaxLength : validacion.textMaxLength,
						email : validacion.email,
						numberMax : validacion.numberMax,
						numberMin : validacion.numberMin,
						textPattern : validacion.textPattern,
						url : validacion.url
					},
					headers : 'Accept:application/json'
				});
			};

			this.getValidacions = function getValidacions() {
				return $http({
					method : 'GET',
					url : 'validacion/rest/list.do',
					headers : 'Accept:application/json'
				}).then(function(response) {
					return response.data;
				});
			};

		}
]);

app.controller("ValidacionController", [
		'$scope', 'ValidacionService', function($scope, ValidacionService, ngTableParams) {

			$scope.submitted = false;

			$scope.getValidacions = function() {
				ValidacionService.getValidacions().then(function(data) {
					$scope.validacions = data;

					$scope.tableParams = new NgTableParams({}, {
						dataset : $scope.validacions
					});
				});
			};

			$scope.saveValidacion = function() {

				$scope.submitted = true;
				if ($scope.validacionForm.$valid) {
					ValidacionService.saveValidacion($scope.validacion).then(function success(response) {
						$scope.message = 'Validacion added!';
						$scope.errorMessage = '';
						$scope.getValidacions();
						$scope.validacion = null;
						$scope.submitted = false;

					}, function error(response) {
						if (response.status == 409) {
							$scope.errorMessage = response.data.message;
						} else {
							$scope.errorMessage = 'Error adding validacion!';
						}
						$scope.message = '';
					});
				}
			};

		}
]);
