angular.module('bidding').controller('bid', ['$scope', '$http', function ($scope, $http) {

    $scope.$watch('currentPrice', function (newPrice) {
        $scope.bidPrice = newPrice + $scope.bidStep;
    });

    $scope.$watch('bidPrice', function () {
        $scope.minimalBidPrice = ($scope.bidPrice <= $scope.currentPrice + $scope.bidStep);
    });

    $scope.decrementBidPrice = function () {
        if ($scope.bidPrice > $scope.currentPrice + $scope.bidStep) {
            $scope.bidPrice -= $scope.bidStep;
        }
    };

    $scope.incrementBidPrice = function () {
        $scope.bidPrice += $scope.bidStep;
    };

    $scope.bid = function () {
        var requestParams = {
            method: 'PATCH',
            url: 'http://localhost:8080/api/auctions/' + $scope.auctionId,
            headers: {
                'Content-Type': 'application/json'
            },
            data: {
                price: $scope.bidPrice.toFixed(2)
            }
        };

        $http(requestParams).then(
            function () {},
            function (error) {
                $scope.setMessage({status: 'error', text: error.statusText});
                throw new Error('Bidding failed: ' + error.statusText);
            }
        );

        return false;
    };

}]);