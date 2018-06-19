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

- (void)intializeWithKey:(NSString *)key secret:(NSString *)secret {
    [[FSQPPilgrimManager sharedManager] configureWithConsumerKey:key secret:secret delegate:self completion:nil];
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(requestAuthorization) {
    [[FSQPPilgrimManager sharedManager] requestAlwaysAuthorizationWithCompletion:^(BOOL didAuthorize) {
        [self sendEventWithName:AuthorizedEvent body:nil];
    }];
}

RCT_EXPORT_METHOD(start) {
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

- (NSDictionary *)constantsToExport {
    return @{AuthorizedEvent: AuthorizedEvent};
}

+ (BOOL)requiresMainQueueSetup {
    return NO;
}

- (NSArray<NSString *> *)supportedEvents {
    return @[AuthorizedEvent, DidVisitEvent, DidBackfillVisitEvent];
}

- (void)fsqpPilgrimManager:(FSQPPilgrimManager *)pilgrimManager didVisit:(FSQPVisit *)visit {
    [self sendEventWithName:DidVisitEvent body:nil]; // TODO VISIT OBJECT ?!?
}

- (void)fsqpPilgrimManager:(FSQPPilgrimManager *)pilgrimManager didBackfillVisit:(FSQPVisit *)visit {
    [self sendEventWithName:DidBackfillVisitEvent body:nil]; // TODO VISIT OBJECT ?!?
}

@end
