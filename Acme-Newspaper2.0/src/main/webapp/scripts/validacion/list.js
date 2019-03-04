app.controller("ListController", [
		'$scope', 'ValidacionService', function($scope, ValidacionService) {

			$scope.getValidacions = function() {
				ValidacionService.getValidacions().then(function(data) {
					$scope.validacions = data;
				});
			};

			$scope.getValidacions();

		}
]);
