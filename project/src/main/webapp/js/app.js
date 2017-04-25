var tmsApp = angular.module('tmsApp', [ 'ngRoute',
		'ui.filters', 'ui.bootstrap', 'angucomplete-alt', 'tag-input',
		'angular-loading-bar', 'angularFileUpload', 'gg.editableText',
		'ngTagsInput', 'mentio', 'ngToast', 'ngSanitize', 'ngclipboard' ]);

tmsApp.config([ 'ngToastProvider', function(ngToastProvider) {
	ngToastProvider.configure({
		additionalClasses : 'my-animation'
	});
} ]);


tmsApp
		.config([
				'$httpProvider',
				function($httpProvider) {
					$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
				} ]);

tmsApp.service('authInterceptor', function($q, $rootScope) {
	var service = this;

	service.responseError = function(response) {
		if (response.status == 401) {
			$rootScope.isAuthenticated = false;
			window.location = "#/";
		}
		
		return $q.reject(response);
	};
})

tmsApp.config([ '$routeProvider', '$httpProvider',
		function($routeProvider, $httpProvider) {

			$routeProvider.when('/admin', {
				templateUrl : 'admin.html',


			}).when('/projectuser', {
				templateUrl : 'userprojects.html',

			}).when('/addtenant', {
				templateUrl : 'addtenant.html',

			}).when('/viewtenant', {
				templateUrl : 'tenants.html',

			}).when('/activateuser/:token/:user/:random', {
				templateUrl : 'activateuser.html',

			}).when('/task', {
				templateUrl : 'tasks.html',

			}).when('/project/:projectId/tasks', {
				templateUrl : 'tasks.html',

			}).when('/project', {
				templateUrl : 'project.html',

			}).when('/createproject', {
				templateUrl : 'projectform.html',

			}).when('/editproject/:projectId', {
				templateUrl : 'editprojectform.html',

			}).when('/createtask', {
				templateUrl : 'taskinputform.html',

			}).when('/project/:projectId/task/:taskId', {
				templateUrl : 'edittask.html',

			}).when('/projectroles', {
				templateUrl : 'rolesinputform.html',

			}).when('/adduserstoproject/:projectId', {
				templateUrl : 'adduserstoproject.html',

			}).when('/preferences', {
				templateUrl : 'preferences.html',

			}).when('/forgotpassword', {
				templateUrl : 'forgotpassword.html',

			}).when('/register', {
				templateUrl : 'register.html',

			}).when('/users/tenant/:domain', {
				templateUrl : 'userstotenant.html',

			}).when('/tenant/:tenantname/:random', {
				templateUrl : 'userregistration.html',

			}).when('/tms', {
				templateUrl : 'index.html',

			}).otherwise('/', {
				templateUrl : 'index.html',

			})

			$httpProvider.interceptors.push('authInterceptor');
			
		} ]);

tmsApp.factory('authorityutility', function(){
	return{
		
		method:function($http,projectId){
		return $http.get('/all/getprojectroleauthorities?projectId='+projectId)
	}
	}
})
tmsApp
		.factory(
				'tmsutility',
				function() {
					return {
						method : function($scope) {
							$(document).ready(function() {
								$('[data-toggle="tooltip"]').tooltip({
									delay : {
										hide : 500
									}
								});
							});

							$scope.alerts = [ {
								type : 'danger',
								msg : 'Oh snap! Try submitting again.'
							}, {
								type : 'success',
								msg : 'Well done! successfully saved.'
							} ];
							$scope.closeAlert = function(index) {
								$scope.alert = false;
							}

							$scope.today = function() {
								$scope.dt = new Date();
							};
							$scope.today();

							$scope.clear = function() {
								$scope.dt = null;
							};

							$scope.inlineOptions = {
								customClass : getDayClass,
								minDate : new Date(),
								showWeeks : true
							};

							$scope.dateOptions = {

								formatYear : 'yy',
								maxDate : new Date(2199, 12, 31),
								minDate : new Date(),
								startingDay : 1
							};

							$scope.toggleMin = function() {
								$scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null
										: new Date();
								$scope.dateOptions.minDate = $scope.inlineOptions.minDate;
							};

							$scope.toggleMin();

							$scope.open1 = function() {
								$scope.popup1.opened = true;
							};

							$scope.open2 = function() {
								$scope.popup2.opened = true;
							};

							$scope.setDate = function(year, month, day) {
								$scope.dt = new Date(year, month, day);
							};

							$scope.formats = [ 'dd-MMMM-yyyy', 'yyyy/MM/dd',
									'dd.MM.yyyy', 'shortDate' ];
							$scope.format = $scope.formats[0];
							$scope.altInputFormats = [ 'M!/d!/yyyy' ];

							$scope.popup1 = {
								opened : false
							};

						
							$scope.popup2 = {
								opened : false
							};

							var tomorrow = new Date();
							tomorrow.setDate(tomorrow.getDate() + 1);
							var afterTomorrow = new Date();
							afterTomorrow.setDate(tomorrow.getDate() + 1);
							$scope.events = [ {
								date : tomorrow,
								status : 'full'
							}, {
								date : afterTomorrow,
								status : 'partially'
							} ];

							function getDayClass(data) {
								var date = data.date, mode = data.mode;
								if (mode === 'day') {
									var dayToCheck = new Date(date).setHours(0,
											0, 0, 0);

									for (var i = 0; i < $scope.events.length; i++) {
										var currentDay = new Date(
												$scope.events[i].date)
												.setHours(0, 0, 0, 0);

										if (dayToCheck === currentDay) {
											return $scope.events[i].status;
										}
									}
								}

								return '';
							}
						}
					}

				})

tmsApp.service("CommentService", [ '$sce', function($sce) {
	return {
		parseComment : function(comment, baseUrl) {
			/**
			 * Parses a given comment and replaces task identifiers with links
			 * to the task The result of this function should be bound to the UI
			 * using ng-bind
			 */
			
			var re = /#(\w+)(?!\w)/g;
			return $sce.trustAsHtml(comment.replace(re, function(a, b) {
				var url = baseUrl + b;
				return "<a href='" + url + "'>" + a + "</a>";
			}));
		}
	}
} ]);


tmsApp.run(function($rootScope, $http, $location,getGlyphicons) {

	$http({
		method : 'GET',
		url : '/public/login',

	}).then(function(response) {
		
		// $rootScope.authenticated=true;
		$rootScope.response = response.data;
		
		if (response.status===200) {
			
			if ($rootScope.response.roles[0].roleType === "admin") {
				$rootScope.getProjects();
				$rootScope.isAuthenticated = true;
				$rootScope.admin = true;
				$rootScope.sa = false;
				$rootScope.user = false;

			} else if ($rootScope.response.roles[0].roleType === "user") {
				$rootScope.getProjects();
				$rootScope.isAuthenticated = true;
				$rootScope.user = true;
				$rootScope.admin = false;
				$rootScope.sa = false;

			} else if ($rootScope.response.roles[0].roleType === "sa") {
				$rootScope.isAuthenticated = true;
				$rootScope.sa = true;
				$rootScope.admin = false;
				$rootScope.user = false;
			}
			else {
				location.href="#/";
				$rootScope.isAuthenticated = false;
				$rootScope.sa = false;
				$rootScope.admin = false;
				$rootScope.user = false;	
			}
			$rootScope.isSuccess = true;
		} else {
			$rootScope.isAuthenticated = false;
		}
		
	});
	
	$rootScope.getProjects = function() {
		$http({
			method : 'GET',
			url : '/all/project/getprojects/users'
		}).then(function(response) {

			$rootScope.projects = angular.copy(response.data);
			
		})
		
		
		

		$http.get('/all/task/getstatus').success(function(res){
			
			$rootScope.statuses=[];
				angular.forEach(res, function(key, value) {

					$rootScope.statuses.push({
						"name" : key,
						"value" : value,
						"glyphicon":getGlyphicons.status(value).glyphicon,
						"index":getGlyphicons.status(value).index
					})
					
				})
		}).error(function(){
			
		})
		
		
		
		
		$http.get('all/task/getpriority').success(function(res){
			
			$rootScope.priorities=[];
			angular.forEach(res,function(key,value){
				
			$rootScope.priorities.push({
				"name":key,
				"value":value,
				"glyphicon":getGlyphicons.priority(value).glyphicon,
				"index":getGlyphicons.priority(value).index
			})
			})
		}).error(function(){
		})
	
	}
	

	tmsApp.run(function($rootScope) {
		var lastDigestRun = new Date();
		$rootScope.$watch(function detectIdle() {
			var now = new Date();
			if (now - lastDigestRun > 10 * 60 * 60) {
				// logout here, like delete cookie, navigate to login ...
				$rootScope.isAuthenticated = false;
				$rootScope.isSuccess = false;
				$location.url('/');
			}
			lastDigestRun = now;
		});
	});
})

tmsApp.controller('indexController', function($scope, $rootScope, $http,
		$window, $location) {
	$scope.redirect = function() {
		$rootScope.isSuccess = true;
	}
	$scope.logout = function() {
		$http.get('/logout').then(function() {
			$rootScope.isAuthenticated = false;
			$rootScope.isSuccess = false;
			$location.url('/');
		})
	}
	$scope.isActive = function(viewLocation) {
		if (viewLocation === $location.path()) {
			return true;
		} else {
			return false;
		}
	}
	$rootScope.toggleLeftMenu = function() {
		$('#sidebar-wrapper').toggleClass('show');
	}
});
