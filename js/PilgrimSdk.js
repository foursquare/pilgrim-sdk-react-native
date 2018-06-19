import { NativeModules, NativeEventEmitter } from 'react-native'

if (!NativeModules.RNPilgrimSdk) {
    throw new Error('NativeModules.RNPilgrimSdk is undefined');
}
 
export default NativeModules.RNPilgrimSdk;