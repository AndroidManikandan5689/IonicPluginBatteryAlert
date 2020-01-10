/********* BatteryAlert.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>

@interface BatteryAlert : CDVPlugin {
  // Member variables go here.
}

- (void)batteryAlert:(CDVInvokedUrlCommand*)command;
@end

@implementation BatteryAlert

- (void)batteryAlert:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* echo = [command.arguments objectAtIndex:0];

    UIDevice *myDevice = [UIDevice currentDevice];
    [myDevice setBatteryMonitoringEnabled:YES];
    
    int state = [myDevice batteryState];
    NSLog(@"battery status: %d",state); // 0 unknown, 1 unplegged, 2 charging, 3 full
    
    double batLeft = (float)[myDevice batteryLevel] * 100;
    NSLog(@"battery left: %f", batLeft);
    NSNumber *myDoubleNumber = [NSNumber numberWithDouble:batLeft];
    NSString *str = [NSString stringWithFormat: @"Device Battery Percentage %@ ", [myDoubleNumber stringValue]];
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Battery Status"
                                                                   message:str preferredStyle:UIAlertControllerStyleAlert]; // 1
    UIAlertAction *firstAction = [UIAlertAction actionWithTitle:@"OK"
                                                          style:UIAlertActionStyleDefault handler:^(UIAlertAction * action) {
                                                              NSLog(@"You pressed button one");
                                                          }]; // 2
    
    
    [alert addAction:firstAction]; // 4
    
    
    [self.viewController presentViewController:alert animated:YES completion:nil];
    
    if (echo != nil && [echo length] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:echo];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
