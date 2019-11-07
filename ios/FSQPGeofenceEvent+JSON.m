//  Copyright © 2019 Foursquare. All rights reserved.

#import "FSQPGeofenceEvent+JSON.h"
#import "CLLocation+JSON.h"
#import "FSQPVenue+JSON.h"

@implementation FSQPGeofenceEvent (JSON)

- (NSDictionary *)json {
    NSMutableDictionary *jsonDict = [NSMutableDictionary dictionary];
    jsonDict[@"id"] = self.geofenceID;
    jsonDict[@"name"] = self.name;

    if (self.venue != nil) {
        FSQPVenue *venue = self.venue;
        jsonDict[@"venueId"] = venue.foursquareID;
        jsonDict[@"venue"] = [self.venue json];
    }

    if (self.partnerVenueID) {
        jsonDict[@"partnerVenueId"] = self.partnerVenueID;
    }

    jsonDict[@"location"] = [self.location json];
    jsonDict[@"timestamp"] = @(self.timestamp.timeIntervalSince1970);
    return jsonDict;
}

@end
