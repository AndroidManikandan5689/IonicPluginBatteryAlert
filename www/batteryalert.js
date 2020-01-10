var exec = require('cordova/exec');

module.exports.batteryAlert = function (arg0, success, error) {
    exec(success, error, 'BatteryAlert', 'batteryAlert', [arg0]);
};
