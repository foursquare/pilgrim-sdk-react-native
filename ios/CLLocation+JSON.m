//  Copyright © 2019 Foursquare. All rights reserved.

#import "CLLocation+JSON.h"

@implementation CLLocation (JSON)

- (NSDictionary *)json {
    return @{@"latitude": @(self.coordinate.latitude),
             @"longitude": @(self.coordinate.longitude)};
}

@end
