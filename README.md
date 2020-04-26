
# Pilgrim SDK React Native module

[![CircleCI](https://circleci.com/gh/foursquare/pilgrim-sdk-react-native.svg?style=svg)](https://circleci.com/gh/foursquare/pilgrim-sdk-react-native)

## Table of Contents
* [Installing](#installing)
* [Usage](#usage)
    * [Application Setup](#application-setup)
    * [Getting User's Current Location](#getting-users-current-location)
    * [Passive Location Detection](#passive-location-detection)
    * [Debug Screen](#debug-screen)
    * [Test Visits](#test-visits)
* [Samples](#samples)
* [FAQ](#faq)

## Installing

1. Install module

    npm
    ```bash
    npm install @foursquare/pilgrim-sdk-react-native
    ```

    Yarn
    ```bash
    yarn add @foursquare/pilgrim-sdk-react-native
    ```

2. Link native code

    With [autolinking](https://github.com/react-native-community/cli/blob/master/docs/autolinking.md) (react-native 0.60+) 

    ```bash
    cd ios && pod install && cd .. 
    ```

    Pre 0.60

   ```bash
   react-native link @foursquare/pilgrim-sdk-react-native
   ```

## Usage

### Application Setup

#### iOS Setup

You must call `[[FSQPPilgrimManager sharedManager] configureWithConsumerKey:secret:delegate:completion:]` from `application:didFinishLaunchingWithOptions` in a your application delegate, for example:

```objc
// AppDelegate.m
#import "AppDelegate.h"

#import <React/RCTBridge.h>
#import <React/RCTBundleURLProvider.h>
#import <React/RCTRootView.h>
#import <Pilgrim/Pilgrim.h>

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  [[FSQPPilgrimManager sharedManager] configureWithConsumerKey:@"CONSUMER_KEY"
                                                        secret:@"CONSUMER_SECRET"
                                                      delegate:self
                                                    completion:nil];

  
  // Other react native initialization code
  
  return YES;
}

...

@end

```

#### Android Setup

You must call `PilgrimSdk.with(PilgrimSdk.Builder)` from `onCreate` in a your `android.app.Application` subclass, for example:

```java
// MainApplication.java
import android.app.Application;
import com.facebook.react.ReactApplication;
import com.foursquare.pilgrim.PilgrimSdk;

public class MainApplication extends Application implements ReactApplication {

    @Override
  public void onCreate() {
    super.onCreate();

    PilgrimSdk.Builder builder = new PilgrimSdk.Builder(this)
            .consumer("CONSUMER_KEY", "CONSUMER_SECRET")
            .enableDebugLogs();
    PilgrimSdk.with(builder);

    // Other react native initialization code
  }

  ...

}
```

#### Basic Usage

```javascript
import React, { Component } from 'react';
import { Text } from 'react-native';
import PilgrimSdk from '@foursquare/pilgrim-sdk-react-native';

export default class Screen extends Component {
    state = {
        installId: "-",
    };

    componentDidMount() {
        PilgrimSdk.getInstallId().then(installId => {
            this.setState({ installId: installId });
        });
    }

    render() {
        return (
            <>
                <Text>Install ID: {this.state.installId}</Text>
            </>
        );
    }
}
```

### Getting User's Current Location

You can actively request the current location of the user by calling the `PilgrimSdk.getCurrentLocation` method.  The return value will be a `Promise<CurrentLocation>`.  The `CurrentLocation` object has the current venue the device is most likely at as well as any geofences that the device is in (if configured). More information [here](https://developer.foursquare.com/docs/pilgrim-sdk/quickstart#get-current-location). Example usage below:

```javascript
import React, { Component } from 'react';
import { Alert, Text } from 'react-native';
import PilgrimSdk from '@foursquare/pilgrim-sdk-react-native';

export default class Screen extends Component {
    state = {
        currentLocation: null
    };

    getCurrentLocation = async function () {
        try {
            const currentLocation = await PilgrimSdk.getCurrentLocation();
            this.setState({ currentLocation: currentLocation });
        } catch (e) {
            Alert.alert("Pilgrim SDK", `${e}`);
        }
    }

    componentDidMount() {
        this.getCurrentLocation();
    }

    render() {
        if (this.state.currentLocation != null) {
            const venue = this.state.currentLocation.currentPlace.venue;
            const venueName = venue.name || "Unnamed venue";
            return (
                <>
                    <Text>Venue: {venueName}</Text>
                </>
            );
        } else {
            return (
                <>
                    <Text>Loading...</Text>
                </>
            );
        }
    }
}
```

### Passive Location Detection

Passive location detection is controlled with the `PilgrimSdk.start` and `PilgrimSdk.stop` methods. When started Pilgrim SDK will send notifications to [Webhooks](https://developer.foursquare.com/docs/pilgrim-sdk/webhooks) and other [third-party integrations](https://developer.foursquare.com/docs/pilgrim-sdk/integrations).  Example usage below:

```javascript
import React, { Component } from 'react';
import { Alert, Button } from 'react-native';
import PilgrimSdk from '@foursquare/pilgrim-sdk-react-native';

export default class Screen extends Component {
    startPilgrim = async function () {
        const canEnable = await PilgrimSdk.canEnable();
        const isSupportedDevice = await PilgrimSdk.isSupportedDevice();
        if (canEnable && isSupportedDevice) {
            PilgrimSdk.start();
            Alert.alert("Pilrim SDK", "Pilgrim started");
        } else {
            Alert.alert("Pilrim SDK", "Error starting");
        }
    }

    stopPilgrim = function () {
        PilgrimSdk.stop();
        Alert.alert("Pilrim SDK", "Pilgrim stopped");
    }

    render() {
        return (
            <>
                <Button title="Start" onPress={() => { this.startPilgrim(); }} />
                <Button title="Stop" onPress={() => { this.stopPilgrim(); }} />
            </>
        );
    }
}
```

### Debug Screen

The debug screen is shown using the `PilgrimSdk.showDebugScreen` method. This screen contains logs sent from Pilgrim SDK and other debugging tools/information. Example usage below:


```javascript
import React, { Component } from 'react';
import { Button } from 'react-native';
import PilgrimSdk from '@foursquare/pilgrim-sdk-react-native';

export default class Screen extends Component {
    showDebugScreen = function () {
        PilgrimSdk.showDebugScreen();
    }

    render() {
        return (
            <>
                <Button title="Show Debug Screen" onPress={() => { this.showDebugScreen(); }} />
            </>
        );
    }
}
```

### Test Visits

Test arrival visits can be fired with the method `PilgrimSdk.fireTestVisit`. You must pass a location to be used for the test visit. The arrival notification will be received via [Webhooks](https://developer.foursquare.com/docs/pilgrim-sdk/webhooks) and other [third-party integrations](https://developer.foursquare.com/docs/pilgrim-sdk/integrations)

```javascript
import React, { Component } from 'react';
import { Button } from 'react-native';
import PilgrimSdk from '@foursquare/pilgrim-sdk-react-native';

export default class Screen extends Component {
    fireTestVisit = async function () {
        navigator.geolocation.getCurrentPosition((position) => {
            const latitude = position.coords.latitude;
            const longitude = position.coords.longitude;
            PilgrimSdk.fireTestVisit(latitude, longitude);
            Alert.alert("Pilgrim SDK", `Sent test visit with location: (${latitude},${longitude})`);
        }, err => {
            Alert.alert("Pilgrim SDK", `${err}`);
        });
    }

    render() {
        return (
            <>
                <Button title="Fire Test Visit" onPress={() => { this.fireTestVisit(); }} />
            </>
        );
    }
}
```

## Samples

* [React Native Pilgrim SDK Sample App](https://github.com/foursquare/RNPilgrimSample) - Basic application using pilgrim-sdk-react-native

## FAQ

Consult the Pilgrim documentation [here](https://developer.foursquare.com/docs/pilgrim-sdk/FAQ)
