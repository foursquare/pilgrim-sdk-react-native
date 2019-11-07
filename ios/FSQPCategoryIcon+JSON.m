//  Copyright © 2019 Foursquare. All rights reserved.

#import "FSQPCategoryIcon+JSON.h"

@implementation FSQPCategoryIcon (JSON)

- (NSDictionary *)json {
    NSMutableDictionary *jsonDict = [NSMutableDictionary dictionary];
    jsonDict[@"prefix"] = self.prefix;
    jsonDict[@"suffix"] = self.suffix;
    return jsonDict;
}

@end
