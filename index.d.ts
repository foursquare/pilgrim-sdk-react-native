export interface PilgrimSdk {
    /**
     * If the current device is supported (no iPads or iPod touches; cellular network required)
     */
    isSupportedDevice(): Promise<boolean>;

    /**
     * If the user is on a supported device and all the required settings ("always" location permission) are on
     */
    canEnable(): Promise<boolean>;

    /**
     * Returns a unique identifier that gets generated the first time this sdk runs on a specific device.
     */
    getInstallId(): Promise<string>;

    /**
     * Call this after configuring the SDK to start the SDK and begin receiving location updates.
     * */
    start(): void;

    /**
     * Stop receiving location updates, until you call `start` again.
     */
    stop(): void;

    /**
     * Gets the current location of the user.
     * This includes possibly a visit and and an array of geofences.
     */
    getCurrentLocation(): Promise<string>;

    /**
     * Generates a visit and optional nearby venues at the given location.
     */
    fireTestVisit(latitude: number, longitude: number): void;
}

declare let PilgrimSdk: PilgrimSdk
module.exports.PilgrimSdk = PilgrimSdk;
export default PilgrimSdk