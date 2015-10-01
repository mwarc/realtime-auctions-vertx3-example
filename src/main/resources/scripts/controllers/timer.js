angular.module('bidding').controller('timer', ['$scope', '$interval', function ($scope, $interval) {

    var toUTCDate = function (date) {
        return new Date(date.getUTCFullYear(), date.getUTCMonth(), date.getUTCDate(),  date.getUTCHours(), date.getUTCMinutes(), date.getUTCSeconds());
    };

    var prepareTimeToEnd = function (endingTimeObj) {
        var endingTime = new Date(Date.UTC(endingTimeObj.year, endingTimeObj.monthValue - 1, endingTimeObj.dayOfMonth,
            endingTimeObj.hour, endingTimeObj.minute, endingTimeObj.second));
        var timeToEnd = (endingTime - new Date() > 0) ? endingTime - new Date() : 0;

        return toUTCDate(new Date(timeToEnd));
    };

    var startCountdown = function () {
        var countdown = $interval(function () {
            var hoursLeft = $scope.timeToEnd.getHours(),
                minutesLeft = $scope.timeToEnd.getMinutes(),
                secondLeft = $scope.timeToEnd.getSeconds();

            if (hoursLeft === 0 && minutesLeft === 0 && secondLeft === 0) {
                $interval.cancel(countdown);
            } else {
                $scope.timeToEnd.setSeconds($scope.timeToEnd.getSeconds() - 1);
            }
        }, 1000);
    };

    $scope.$watch('endingTime', function (endingTime) {
        if (endingTime !== undefined) {
            $scope.timeToEnd = prepareTimeToEnd(endingTime);
            startCountdown();
        }
    });

}]);