/*
 * @author: Deepanjan
 */

var App = angular.module('tag-input', ['ui.bootstrap']);
App.controller('FormCtrl', function($scope, $http) {
	$scope.myfunc = function() {
		$http({
			method : 'POST',
			url : 'tag.json',
			data : $scope.tag
		}).then(function(response) {
		});

	}
});

App
		.directive(
				'dhTag',
				function($compile) {
					return {

						restrict : 'AE',
						scope : {
							taglist : '=list',
							autocomplete : '=autocomplete',
							display : '@',
							value : '&',
							valuecancel:'&',
							autocomplete:'=autocomplete',
							tags : '=',
						},
						link : function($scope, element, attrs) {

							$scope.defaultWidth = 490;
							$scope.tagText = "";
							$scope.placeholder = attrs.placeholder;
							$scope.tags=$scope.tags;
							$scope.display = attrs.display;
							
							 if ($scope.autocomplete) {
				                    $scope.autocompleteFocus = function (event, ui) {
				                        $(element).find('input').val(ui.item.value);
				                        return false;
				                    };
				                    $scope.autocompleteSelect = function (event, ui) {
				                        $scope.$apply('tagText=\'' + ui.item.value + '\'');
				                        $scope.$apply('addTag()');
				                        return false;
				                    };
				                    $(element).find('input').autocomplete({
				                    	
				                        minLength: 0,
				                        source: function (request, response) {
				                        	
				                            var item;
				                            return response(function () {
				                                var i, len, ref, results;
				                                ref = $scope.autocomplete;
				                                results = [];
				                                for (i = 0, len = ref.length; i < len; i++) {
				                                    if (window.CP.shouldStopExecution(1)) {
				                                        break;
				                                    }
				                                    item = ref[i];
				                                    if (item.toLowerCase().indexOf(request.term.toLowerCase()) !== -1) {
				                                        results.push(item);
				                                    }
				                                }
				                                window.CP.exitedLoop(1);
				                                return results;
				                            }());
				                        },
				                        focus: function (_this) {
				                            return function (event, ui) {
				                                return $scope.autocompleteFocus(event, ui);
				                            };
				                        }(this),
				                        select: function (_this) {
				                            return function (event, ui) {
				                                return $scope.autocompleteSelect(event, ui);
				                            };
				                        }(this)
				                    });
				                }

							$scope.tagArray = function() {
								return $scope.taglist || [];
							};

							$scope.addTag = function() {

								var tagArray;
								if ($scope.tagText.length === 0) {
									return;
								}
								tagArray = $scope.tagArray();

								for (var i = 0; i < tagArray.length; i++) {

									if (tagArray[i].tagName == $scope.tagText) {
										$scope.tagText = "";
										toaster.method('danger',
												'Duplicate tags..!!', ngToast);
										return;
									}
								}

								tagArray.push({
									tagName : $scope.tagText
								});
								$scope.taglist = tagArray;
								$scope.tagText = "";
							};

							$scope.deleteTag = function(key) {
								var tagArray = [];
								tagArray = $scope.tagArray();
								if (tagArray.length > 0
										&& $scope.tagText.length === 0
										&& key === undefined) {
									tagArray.pop();
								} else {
									if (key !== undefined) {
										tagArray.splice(key, 1);
									}
								}
								return $scope.taglist = tagArray;
							};

							/*
							 * $scope .$watch( 'tagText', function(newVal,
							 * oldVal) { var temp; if (!(newVal === oldVal &&
							 * newVal === undefined) && temp) {
							 * $scope.inputWidth = temp ? temp .width() : 0; if
							 * ($scope.inputWidth < $scope.defaultWidth) {
							 * $scope.inputWidth = $scope.defaultWidth; } return
							 * temp.remove(); } });
							 */
							element
									.bind(
											"keydown",
											function(e) {
												var key;
												key = e.which;
												if (key === 9 || key === 13) {
													e.preventDefault();
												}
												if (key === 8) {
													$("div")
															.find(
																	'.dh-tag-ctn .input-tag')
															.css(
																	{
																		"background-color" : "rgba(255, 0, 0, 0.15)"
																	});
													if (key === 8) {
														return $scope
																.$apply('deleteTag()');
													}
												}
											});

							element.bind("keyup", function(e) {
								var key;
								key = e.which;
								if (key === 9 || key === 13 ) {
									$("div").find('.dh-tag-ctn .input-tag')
											.css({
												"background-color" : " #FCF8E3"
											});
									e.preventDefault();
									return $scope.$apply('addTag()');
								}
							});
						},

						template : ""
								+ "<div  class='dh-tag-ctn'>"
								+ "<div class='input-tag' ng-hide={{display}} data-ng-repeat=\"tag in tagArray() track by $index\" ng-class='tag' >"
								+ "<span>{{tag.tagName}}</span>"
								+ "<div class='delete-tag' data-ng-click='deleteTag($index)'>&nbsp;&times;</div>"
								+ "</div>"
								+ "<input type='text' data-ng-style='{width: inputWidth}' ng-model='tagText'  placeholder='{{placeholder}}' ng-click='divshow=true'/>"
								+ "</div>"
								+ "<br>"
								+ "<div ng-show={{display}} class='dh-tag-ctn-view'><div class='input-tag' data-ng-repeat=\"tag in tagArray() track by $index\" ng-class='tag'>{{tag.tagName}}"
								+ "</div>"
								+ "<br>"
								+ "<div class='popover dh-tag-ctn-view' style='display: block;max-width: 459px;height:168px;top: -5px;' ng-show='divshow'><div class='dh-tag-ctn-view'>"
								+ " <input type='text' style='width:98%;'  class='tag' data-ng-style='{width: inputWidth}' ng-model='tagText'  placeholder='Type here to add tags' uib-typeahead='tag for tag in tags | filter:$viewValue | limitTo:8'/>"
								+ "<div  class='dh-tag-ctn-view container' style='overflow-x: auto;height: 88px;padding: 8px 4px 6px 27px;margin-left: -52px;margin-top: -19px;'><div class='input-tag' data-ng-repeat=\"tag in tagArray() track by $index\" ng-class='tag'>{{tag.tagName}}"
								+ "<div class='delete-tag' data-ng-click='deleteTag($index)'>&times;<br></div>"
								+ "</div></div></div><div class='btn-cls'><button class='btn btn-primary'  ng-click='value(); valuecancel(); divshow=false' />Save&nbsp;&nbsp;<button class='btn btn-default' value='Cancel' ng-click='valuecancel();divshow=false'/>Cancel</div>"
					};
				});

