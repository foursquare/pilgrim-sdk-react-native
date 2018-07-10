//
//  RNPilgrimSdk.m
//  RNPilgrimSdk
//
//  Created by Brian Rojas on 6/18/18.
//  Copyright © 2018 Foursquare Labs. All rights reserved.
//

#import "RNPilgrimSdk.h"
#import <Pilgrim/Pilgrim.h>

NSString * const AuthorizedEvent = @"AuthorizedEvent";
NSString * const DidVisitEvent = @"DidVisitEvent";
NSString * const DidBackfillVisitEvent = @"DidBackfillVisitEvent";

@interface RNPilgrimSdk () <FSQPPilgrimManagerDelegate>

@end

@implementation RNPilgrimSdk

RCT_EXPORT_MODULE();

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

RCT_EXPORT_METHOD(requestAuthorization) {
    [[FSQPPilgrimManager sharedManager] requestAlwaysAuthorizationWithCompletion:^(BOOL didAuthorize) {
        [self sendEventWithName:AuthorizedEvent body:@(didAuthorize)];
    }];
}

RCT_EXPORT_METHOD(start) {
    [FSQPPilgrimManager sharedManager].delegate = self;
    [[FSQPPilgrimManager sharedManager] startMonitoringVisits];
}

RCT_EXPORT_METHOD(stop) {
    [[FSQPPilgrimManager sharedManager] stopMonitoringVisits];
}

RCT_REMAP_METHOD(getInstallId,
                 getInstallIdWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject) {
    resolve([FSQPPilgrimManager sharedManager].installId);
}

RCT_EXPORT_METHOD(testVenueVisit:(BOOL)isDeparture) {
    [[FSQPPilgrimManager sharedManager] fireTestVisitWithConfidence:FSQPConfidenceHigh locationType:FSQPLocationTypeVenue isDeparture:isDeparture];
}

- (NSDictionary *)constantsToExport {
    return @{AuthorizedEvent: AuthorizedEvent,
             DidVisitEvent: DidVisitEvent,
             DidBackfillVisitEvent: DidBackfillVisitEvent};
}

+ (BOOL)requiresMainQueueSetup {
    return NO;
}

- (NSArray<NSString *> *)supportedEvents {
    return @[AuthorizedEvent, DidVisitEvent, DidBackfillVisitEvent];
}

+ (NSDictionary *)visitJSONDictionary:(FSQPVisit *)visit {
    FSQPVenue *venue = visit.venue;
    NSMutableDictionary *venueDict = [NSMutableDictionary dictionary];
    venueDict[@"name"] = venue.name;
    
    FSQPVenueLocation *location = visit.venue.locationInformation;
    NSMutableDictionary *locationDict = [NSMutableDictionary dictionary];
    if (location.address) {
        locationDict[@"address"] = location.address;
    }
    if (location.crossStreet) {
        locationDict[@"crossStreet"] = location.crossStreet;
    }
    if (location.city) {
        locationDict[@"city"] = location.city;
    }
    if (location.state) {
        locationDict[@"state"] = location.state;
    }
    if (location.postalCode) {
        locationDict[@"postalCode"] = location.postalCode;
    }
    if (location.country) {
        locationDict[@"country"] = location.country;
    }
    locationDict[@"lat"] = @(location.coordinate.latitude);
    locationDict[@"lng"] = @(location.coordinate.longitude);
    venueDict[@"location"] = locationDict;
    
    return @{@"pilgrimVisitId": visit.pilgrimVisitId,
             @"venue": venueDict,
             @"isArrival": @(visit.isArrival)};
}

- (void)fsqpPilgrimManager:(FSQPPilgrimManager *)pilgrimManager didVisit:(FSQPVisit *)visit {
    [self sendEventWithName:DidVisitEvent body:[[self class] visitJSONDictionary:visit]];
}

- (void)fsqpPilgrimManager:(FSQPPilgrimManager *)pilgrimManager didBackfillVisit:(FSQPVisit *)visit {
    [self sendEventWithName:DidVisitEvent body:[[self class] visitJSONDictionary:visit]];
}

@end
