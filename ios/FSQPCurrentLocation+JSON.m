//  Copyright © 2019 Foursquare. All rights reserved.

#import "FSQPCurrentLocation+JSON.h"
#import "FSQPGeofenceEvent+JSON.h"
#import "FSQPVisit+JSON.h"

@implementation FSQPCurrentLocation (JSON)

- (NSDictionary *)json {
    NSMutableDictionary *jsonDict = [NSMutableDictionary dictionary];
    jsonDict[@"currentPlace"] = [self.currentPlace json];

    NSMutableArray *geofences = [NSMutableArray array];
    for (FSQPGeofenceEvent *event in self.matchedGeofences) {
        [geofences addObject:[event json]];
    }
    jsonDict[@"matchedGeofences"] = geofences;

    return jsonDict;
}

@end
