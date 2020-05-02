import { NativeModules, NativeEventEmitter } from 'react-native';

type BackgroundTrackGeolocationType = {
  getDeviceName(): Promise<string>;
  startTracking(): void;
  stopTracking(): void;
};
export interface location {
  latitude: number;
  longitude: number;
}
// @ts-ignore
const { BackgroundTrackGeolocation } = NativeModules as TrackLocationType;

export class GeolocationNativeEventEmitter {
  public geolocationMonitorEmitter: NativeEventEmitter;

  constructor() {
    this.geolocationMonitorEmitter = new NativeEventEmitter(
      BackgroundTrackGeolocation
    );
  }
}
export default BackgroundTrackGeolocation as BackgroundTrackGeolocationType;
