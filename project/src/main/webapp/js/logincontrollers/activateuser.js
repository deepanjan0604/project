tmsApp
		.controller(
				'activateusercontroller',
				function($scope, $routeParams, $http, $rootScope, $log) {
					$rootScope.isAuthenticated = false;
					$rootScope.isActivatedUser = true;
					$scope.userdata = {};

					$scope.passwordDoNotMatchMsg = "";

					$http.get(
							"/public/check/link?token=" + $routeParams.token
									+ "&user=" + $routeParams.user).success(
							function(response) {
								if (response.error != null) {

									$scope.error = true
									$scope.message = response.error;
								} else {
									$scope.success = true;
								}
							}).error(function(response) {

					})

					$scope.savePassword = function() {
						$http.post(
								"/public/registration?token="
										+ $routeParams.token + "&user="
										+ $routeParams.user, $scope.userdata)
								.success(function() {

									location.href = '/';
									$rootScope.isAuthenticated = false;

								}).error(function(err) {
									$log.info(err);
								})
					}

					$scope.isFormInvalid = function() {
						return $scope.confirmPassword == undefined
								|| $scope.userdata.password == undefined
								|| $scope.matchPassword();
					}

					$scope.matchPassword = function() {
						if (!$scope.userdata.password)
							return '';
						if (!$scope.confirmPassword)
							return '';

						if ($scope.userdata.password != $scope.confirmPassword
								&& ($scope.userdata.password != '' && $scope.confirmPassword != '')) {
							$scope.passwordDoNotMatchMsg = "Does not match with the password";
							return 'has-error has-feedback';
						} else
							return '';
					}

				})
