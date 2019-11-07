//  Copyright © 2019 Foursquare Labs. All rights reserved.

#import "RNPilgrimSdk.h"
#import <Pilgrim/Pilgrim.h>

NS_ASSUME_NONNULL_BEGIN

@interface FSQPGeofenceEvent (RCT)
- (NSDictionary *)json;
@end

@implementation FSQPGeofenceEvent (RCT)
- (NSDictionary *)json {
    return @{
        @"geofenceID": self.geofenceID,
        @"name": self.name
    };
}
@end

@interface FSQPVenue (RCT)
- (NSDictionary *)json;
@end

@implementation FSQPVenue (RCT)
- (NSDictionary *)json {
    return @{
        @"name": self.name
    };
}
@end

@interface FSQPVisit (RCT)
- (NSDictionary *)json;
@end

@implementation FSQPVisit (RCT)
- (NSDictionary *)json {
    return @{
        @"venue": self.venue.json
    };
}
@end

@interface FSQPCurrentLocation (RCT)
- (NSDictionary *)json;
@end

@implementation FSQPCurrentLocation (RCT)
- (NSDictionary *)json {
    NSMutableArray *matchedGeofences = [NSMutableArray array];
    for (FSQPGeofenceEvent *geofenceEvent in self.matchedGeofences) {
        [matchedGeofences addObject:geofenceEvent.json];
    }
    return @{
        @"currentPlace": self.currentPlace.json,
        @"matchedGeofences": matchedGeofences
    };
}
@end

@implementation RNPilgrimSdk

RCT_EXPORT_MODULE();

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_REMAP_METHOD(isSupportedDevice,
                 isSupportedDeviceWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject) {
    resolve(@([FSQPPilgrimManager sharedManager].isSupportedDevice));
}

RCT_REMAP_METHOD(canEnable,
                 canEnableWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject) {
    resolve(@([FSQPPilgrimManager sharedManager].canEnable));
}

RCT_REMAP_METHOD(getInstallId,
                 getInstallIdWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject) {
    resolve([FSQPPilgrimManager sharedManager].installId);
}

RCT_EXPORT_METHOD(start) {
    [[FSQPPilgrimManager sharedManager] start];
}

RCT_EXPORT_METHOD(stop) {
    [[FSQPPilgrimManager sharedManager] stop];
}

RCT_REMAP_METHOD(getCurrentLocation,
                 getCurrentLocationWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject) {
    [[FSQPPilgrimManager sharedManager] getCurrentLocationWithCompletion:^(FSQPCurrentLocation * _Nullable currentLocation, NSError * _Nullable error) {
        if (error) {
            reject(@"get_current_location", @"An error occurred getting your current location", error);
            return;
        }
        resolve(currentLocation.json);
    }];
}

RCT_EXPORT_METHOD(fireTestVisit:(double)latitude longitude:(double)longitude) {
    CLLocation *location = [[CLLocation alloc] initWithLatitude:latitude longitude:longitude];
    [[FSQPPilgrimManager sharedManager].visitTester fireTestVisit:location];
}

@end

NS_ASSUME_NONNULL_END
