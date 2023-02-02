//  Copyright © 2019 Foursquare Labs. All rights reserved.

#import "RNPilgrimSdk.h"
#import <Pilgrim/Pilgrim.h>
#import "FSQPCurrentLocation+JSON.h"

@interface PilgrimDelegate : NSObject <FSQPPilgrimManagerDelegate>
@end

@implementation PilgrimDelegate

+ (instancetype)shared {
    static id instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[[self class] alloc] init];
    });
    return instance;
}

- (void)pilgrimManager:(FSQPPilgrimManager *)pilgrimManager handleVisit:(FSQPVisit *)visit {}

@end

@implementation RNPilgrimSdk

RCT_EXPORT_MODULE();

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
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
            reject(@"get_current_location", error.localizedDescription, error);
            return;
        }
        resolve(currentLocation.json);
    }];
}

RCT_EXPORT_METHOD(fireTestVisit:(double)latitude longitude:(double)longitude) {
    CLLocation *location = [[CLLocation alloc] initWithLatitude:latitude longitude:longitude];
    if ([FSQPPilgrimManager sharedManager].delegate == nil) {
        [FSQPPilgrimManager sharedManager].delegate = [PilgrimDelegate shared];
    }
    [[FSQPPilgrimManager sharedManager].visitTester fireTestVisit:location];
}

RCT_EXPORT_METHOD(showDebugScreen) {
    [FSQPPilgrimManager sharedManager].debugLogsEnabled = YES;
    UIViewController *viewController = [UIApplication sharedApplication].keyWindow.rootViewController;
    [[FSQPPilgrimManager sharedManager] presentDebugViewController:viewController];
}

RCT_REMAP_METHOD(isEnabled,
                 isEnabledWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject) {
    resolve(@([[FSQPPilgrimManager sharedManager] isEnabled]));
}

RCT_EXPORT_METHOD(setUserInfoCustomValue:(NSString *)value
                  forKey:(NSString *)key
                  persisted:(BOOL)persisted
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    FSQPUserInfo *userInfo = [FSQPPilgrimManager sharedManager].userInfo;
    if (!userInfo) {
        userInfo = [[FSQPUserInfo alloc] init];
    }
    NSString *valueInUserInfo = userInfo.source[key];
    if (!valueInUserInfo || ![valueInUserInfo isEqualToString:value]) {
        [userInfo setUserInfo:value forKey:key];
        [[FSQPPilgrimManager sharedManager] setUserInfo:userInfo persisted:persisted];
        // retrieve and check if successfully set
        userInfo = [FSQPPilgrimManager sharedManager].userInfo;
        valueInUserInfo = userInfo.source[key];
        if (!valueInUserInfo) {
            valueInUserInfo = @"";
        }
    }
    if (!valueInUserInfo || ![valueInUserInfo isEqualToString:value]) {
        NSError *error = [[NSError alloc] initWithDomain:NSItemProviderErrorDomain code:-1 userInfo:@{
            @"message": @"setUserInfoCustomValue() failed.",
            @"value": value,
            @"key": key,
        }];
        reject(@"setUserInfoCustomValue", @"failure", error);
    } else {
        resolve(valueInUserInfo);
    }
}

RCT_EXPORT_METHOD(getUserInfoCustomValueWithKey:(NSString *)key
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    FSQPUserInfo *userInfo = [FSQPPilgrimManager sharedManager].userInfo;
    if (!userInfo) {
        NSError *error = [[NSError alloc] initWithDomain:NSItemProviderErrorDomain code:-1 userInfo:@{
            @"message": @"getUserInfoCustomValueWithKey() failed.",
            @"reason": @"userInfo does not exist.",
            @"key": key,
        }];
        reject(@"getUserInfoCustomValueWithKey", @"failure", error);
        return;
    }
    NSString *value = userInfo.source[key];
    if (!value) {
        NSError *error = [[NSError alloc] initWithDomain:NSItemProviderErrorDomain code:-1 userInfo:@{
            @"message": @"getUserInfoCustomValueWithKey() failed.",
            @"reason": @"Entry does not exist.",
            @"key": key,
        }];
        reject(@"getUserInfoCustomValueWithKey", @"failure", error);
        return;
    }
    resolve(value);
}

@end
