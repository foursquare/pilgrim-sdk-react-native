//  Copyright © 2019 Foursquare. All rights reserved.

#import "FSQPChain+JSON.h"

@implementation FSQPChain (JSON)

- (NSDictionary *)json {
    NSMutableDictionary *jsonDict = [NSMutableDictionary dictionary];
    jsonDict[@"id"] = self.foursquareID;
    jsonDict[@"name"] = self.name;
    return jsonDict;
}

@end
