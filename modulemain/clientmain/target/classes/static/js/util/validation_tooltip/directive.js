
app.directive('validationMessage', function () {
    return {
        restrict: 'A',
        priority: 1000,
        require: '^validationTooltip',
        link: function (scope, element, attr, ctrl) {
            ctrl.$addExpression(attr.ngIf || true);
        }
    }
});
app.directive('validationTooltip', function ($timeout) {
    return {
        restrict: 'E',
        transclude: true,
        require: '^form',
        scope: {},
        template: '<span class="label" ng-show="errorCount > 0" style="height: inherit;"></span>',
        controller: function ($scope) {
            var expressions = [];
            $scope.errorCount = 0;
            
            this.$addExpression = function (expr) {
                expressions.push(expr);
            }
            $scope.$watch(function () {
                var count = 0;
                angular.forEach(expressions, function (expr) {
                    if ($scope.$eval(expr)) {
                        ++count;
                    }
                });
                return count;

            }, function (newVal) {
                $scope.errorCount = newVal;
            });

        },
        link: function (scope, element, attr, formController, transcludeFn) {
            scope.$form = formController;

            transcludeFn(scope, function (clone) {
                var badge = $(element).find('span.label');
                var tooltip = angular.element('<div class="validationMessageTemplate tooltip-danger" />');
                tooltip.append(clone);
                element.append(tooltip);
                $timeout(function () {
                    scope.$field = formController[attr.target];
                    badge.tooltip({
                        placement: 'bottom',
                        html: true,
                        title: clone
                    });

                });
            });
        }

    }
});