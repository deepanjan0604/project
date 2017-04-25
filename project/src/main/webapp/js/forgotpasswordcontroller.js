tmsApp.controller('forgotpasscontroller', function($scope, $http, $rootScope,
		$location, $log) {

	$rootScope.isActivatedUser = true;
	$rootScope.isAuthenticated = false;
	$scope.sendEmail = function() {

		$http.post('/public/request/forgetpassword', $scope.userdata).success(
				function(response) {
					$scope.successmail = true;
				}).error(function(err) {
			$log.info(err);
		})
	}
})