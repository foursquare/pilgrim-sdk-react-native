
# pilgrim-sdk-react-native

## Yarn

```
yarn add pilgrim-sdk-react-native
```

#### iOS (run `pod install` for [Autolinking](https://github.com/react-native-community/cli/blob/master/docs/autolinking.md))
```
cd ios && pod install && cd .. 
yarn react-native run-ios
```

#### Android
```
yarn react-native run-android
```

## npm/npx

```
npm install pilgrim-sdk-react-native
```

#### iOS (run `pod install` for [Autolinking](https://github.com/react-native-community/cli/blob/master/docs/autolinking.md))
```
cd ios && pod install && cd .. 
npx react-native run-ios
```

#### Android
```
npx react-native run-android
```

(TODO: remove below when in npm)
Until available in npm need to use path to local dir e.g. `yarn add /path/to/pilgrim-sdk-react-native/` for npm need to make sure it doesn't symlink `npm install $(npm pack /path/to/pilgrim-sdk-react-native/ | tail -1)`
