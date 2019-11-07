//  Copyright © 2019 Foursquare. All rights reserved.

#import "FSQPVisit+JSON.h"
#import "CLLocation+JSON.h"
#import "FSQPVenue+JSON.h"

@implementation FSQPVisit (JSON)

- (NSDictionary *)json {
    NSMutableDictionary *jsonDict = [NSMutableDictionary dictionary];

    jsonDict[@"location"] = [self.arrivalLocation json];
    jsonDict[@"locationType"] = @(self.locationType);
    jsonDict[@"confidence"] = @(self.confidence);
    jsonDict[@"arrivalTime"] = @(self.arrivalDate.timeIntervalSince1970);

    if (self.venue) {
        jsonDict[@"venue"] = [self.venue json];
    }

    NSMutableArray *otherPossibleVenuesArray = [NSMutableArray array];
    if (self.otherPossibleVenues) {
        for (FSQPVenue *venue in self.otherPossibleVenues) {
            [otherPossibleVenuesArray addObject:[venue json]];
        }
    }
    jsonDict[@"otherPossibleVenues"] = otherPossibleVenuesArray;

    return jsonDict;
}

@end
