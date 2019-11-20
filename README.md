
# Pilgrim SDK React Native module

[![CircleCI](https://circleci.com/gh/foursquare/pilgrim-sdk-react-native.svg?style=svg)](https://circleci.com/gh/foursquare/pilgrim-sdk-react-native)

## Table of Contents
* [Installing](#installing)
* [Usage](#usage)
    * [Application Setup](#application-setup)

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
                                                      delegate:nil
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

    PilgrimSdk.Builder builder = new PilgrimSdk.Builder(this).consumer("CONSUMER_KEY", "CONSUMER_SECRET");
    PilgrimSdk.with(builder);

    // Other react native initialization code
  }

  ...

}
```

#### Basic Usage

```javascript
// App.js
import React, { Component } from 'react';
import { Text } from 'react-native';
import PilgrimSdk from 'pilgrim-sdk-react-native';

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