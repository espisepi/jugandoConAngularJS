app.controller("ListController", [
		'$scope', 'ValidacionService', 'ngTableParams', function($scope, ValidacionService, ngTableParams) {

			$scope.getValidacions = function() {
				ValidacionService.getValidacions().then(function(data) {
					$scope.validacions = data;

					// Creamos la tabla
					var initialParams = {
						page : 1,
						count : 2
					};
					var initialSettings = {
						counts : [
								2, 5
						],
						// total : $scope.validacions.length,
						dataset : $scope.validacions
					};
					$scope.tableParams = new ngTableParams(initialParams, initialSettings);
					console.log($scope.tableParams);
				});
			};

			$scope.getValidacions();

		}
]);
