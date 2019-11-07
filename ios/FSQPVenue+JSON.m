//  Copyright © 2019 Foursquare. All rights reserved.

#import "FSQPVenue+JSON.h"
#import "FSQPCategory+JSON.h"
#import "FSQPChain+JSON.h"

@implementation FSQPVenue (JSON)

- (NSDictionary *)json {
    NSMutableDictionary *jsonDict = [NSMutableDictionary dictionary];

    jsonDict[@"id"] = self.foursquareID;
    jsonDict[@"name"] = self.name;

    if (self.locationInformation) {
        NSMutableDictionary *locationInformationDict = [NSMutableDictionary dictionary];

        if (self.locationInformation.address) {
            locationInformationDict[@"address"] = self.locationInformation.address;
        }
        if (self.locationInformation.crossStreet) {
            locationInformationDict[@"crossStreet"] = self.locationInformation.crossStreet;
        }
        if (self.locationInformation.city) {
            locationInformationDict[@"city"] = self.locationInformation.city;
        }
        if (self.locationInformation.state) {
            locationInformationDict[@"state"] = self.locationInformation.state;
        }
        if (self.locationInformation.postalCode) {
            locationInformationDict[@"postalCode"] = self.locationInformation.postalCode;
        }
        if (self.locationInformation.country) {
            locationInformationDict[@"country"] = self.locationInformation.country;
        }
        locationInformationDict[@"location"] = @{@"latitude": @(self.locationInformation.coordinate.latitude),
                                                 @"longitude": @(self.locationInformation.coordinate.longitude)};

        jsonDict[@"locationInformation"] = locationInformationDict;
    }

    if (self.partnerVenueId) {
        jsonDict[@"partnerVenueId"] = self.partnerVenueId;
    }

    if (self.probability) {
        jsonDict[@"probability"] = self.probability;
    }

    NSMutableArray *chainsArray = [NSMutableArray array];
    for (FSQPChain *chain in self.chains) {
        [chainsArray addObject:[chain json]];
    }
    jsonDict[@"chains"] = chainsArray;

    jsonDict[@"categories"] = [FSQPVenue categoriesArrayJson:self.categories];

    NSMutableArray *hierarchyArray = [NSMutableArray array];
    for (FSQPVenue *venueParent in self.hierarchy) {
        NSMutableDictionary *venueParentDict = [NSMutableDictionary dictionary];
        venueParentDict[@"id"] = venueParent.foursquareID;
        venueParentDict[@"name"] = venueParent.name;
        venueParentDict[@"categories"] = [FSQPVenue categoriesArrayJson:venueParent.categories];
    }
    jsonDict[@"hierarchy"] = hierarchyArray;

    return jsonDict;
}

+ (NSArray<NSDictionary *> *)categoriesArrayJson:(NSArray<FSQPCategory *> *)categories {
    NSMutableArray *categoriesArray = [NSMutableArray array];
    for (FSQPCategory *category in categories) {
        [categoriesArray addObject:[category json]];
    }
    return categoriesArray;
}

@end
