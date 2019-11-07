//  Copyright © 2019 Foursquare. All rights reserved.

#import <CoreLocation/CoreLocation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CLLocation (JSON)

- (NSDictionary *)json;

@end

NS_ASSUME_NONNULL_END
