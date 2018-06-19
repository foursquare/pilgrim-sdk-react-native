//
//  RNPilgrimSdk.h
//  RNPilgrimSdk
//
//  Created by Brian Rojas on 6/18/18.
//  Copyright © 2018 Foursquare Labs. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RNPilgrimSdk : RCTEventEmitter <RCTBridgeModule>

- (void)intializeWithKey:(NSString *)key secret:(NSString *)secret;

@end
