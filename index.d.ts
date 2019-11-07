export type Location = {
    latitude: number;
    longitude: number;
};

/**
 * An object representing an interaction with one or more registered geofence radii.
 */
export type GeofenceEvent = {
    id: string;
    name: string;
    venueId: string;
    venue: Venue;
    partnerVenueId: string;
    location: Location;
    timestamp: number;
};

/**
 * Foursquare location object for a venue.
 */
export type LocationInformation = {
    address: string;
    crossStreet: string;
    city: string;
    state: string;
    postalCode: string;
    country: string;
    location: Location;
};

/**
 * Foursquare representation of a chain of venues, i.e. Starbucks.
 */
export type Chain = {
    id: string;
    name: string;
};

/**
 * Foursquare category for a venue.
 */
export type Category = {
    id: string;
    name: string;
    pluralName: string;
    shortName: string;
    icon: CategoryIcon;
    isPrimary: boolean;
};

/**
 * The icon image information for a category.
 */
export type CategoryIcon = {
    prefix: string;
    suffix: string;
};

/**
 * Representation of a venue in the Foursquare Places database.
 */
export type Venue = {
    id: string;
    name: string;
    locationInformation: LocationInformation;
    partnerVenueId: string;
    probability: number;
    chains: [Chain];
    categories: [Category];
    hierarchy: [VenueParent];
};

export type VenueParent = {
    id: string;
    name: string;
    categories: [Category];
};

/**
 * Everything Pilgrim knows about a user's location, including raw data and a probable venue.
 */
export type Visit = {
    location: Location;
    locationType: number;
    confidence: number;
    arrivalTime: number;
    venue: Venue;
    otherPossibleVenues: [Venue];
};

/**
 * An object representing the current location of the user.
 */
export type CurrentLocation = {
    currentPlace: Visit;
    matchedGeofences: [GeofenceEvent];
};

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
    getCurrentLocation(): Promise<CurrentLocation>;

    /**
     * Generates a visit and optional nearby venues at the given location.
     */
    fireTestVisit(latitude: number, longitude: number): void;
}

declare let PilgrimSdk: PilgrimSdk
export default PilgrimSdk