
import { NativeModules } from 'react-native';

const PilgrimSdk = NativeModules.RNPilgrimSdk;
module.exports.PilgrimSdk = PilgrimSdk;
export default PilgrimSdk;