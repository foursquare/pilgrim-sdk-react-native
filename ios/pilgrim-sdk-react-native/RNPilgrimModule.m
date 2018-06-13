//
//  RNPilgrimModule.m
//  pilgrim-sdk-react-native
//
//  Created by Brian Rojas on 6/13/18.
//  Copyright © 2018 Facebook. All rights reserved.
//

#import "RNPilgrimModule.h"

@implementation RNPilgrimModule

RCT_EXPORT_MODULE(PilgrimSdk);

RCT_EXPORT_METHOD(start) {
  
}

RCT_REMAP_METHOD(getInstallId,
                 getInstallIdWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject) {
  resolve(@"install id");
}

@end
