package com.foursquare.pilgrim.react;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.foursquare.api.FoursquareLocation;
import com.foursquare.api.types.Category;
import com.foursquare.api.types.Photo;
import com.foursquare.api.types.Venue;
import com.foursquare.api.types.geofence.GeofenceEvent;
import com.foursquare.pilgrim.CurrentLocation;
import com.foursquare.pilgrim.Visit;

import java.util.List;

final class Utils {

    private Utils() {

    }

    static WritableMap currentLocationJson(CurrentLocation currentLocation) {
        WritableMap json = Arguments.createMap();
        json.putMap("currentPlace", visitJson(currentLocation.getCurrentPlace()));

        WritableArray geofenceEvents = Arguments.createArray();
        for (GeofenceEvent event : currentLocation.getMatchedGeofences()) {
            geofenceEvents.pushMap(geofenceEventJson(event));
        }
        json.putArray("matchedGeofences", geofenceEvents);

        return json;
    }

    private static WritableMap visitJson(Visit visit) {
        WritableMap json = Arguments.createMap();
        json.putMap("location", foursquareLocationJson(visit.getLocation()));

        int locationType;
        switch (visit.getType()) {
            case HOME:
                locationType = 1;
                break;
            case WORK:
                locationType = 2;
                break;
            case VENUE:
                locationType = 3;
                break;
            default:
                locationType = 0;
                break;
        }
        json.putInt("locationType", locationType);

        int confidence;
        switch (visit.getConfidence()) {
            case LOW:
                confidence = 1;
                break;
            case MEDIUM:
                confidence = 2;
                break;
            case HIGH:
                confidence = 3;
                break;
            default:
                confidence = 0;
                break;
        }
        json.putInt("confidence", confidence);

        json.putDouble("arrivalTime", visit.getArrival() / 1000);

        if (visit.getVenue() != null) {
            json.putMap("venue", venueJson(visit.getVenue()));
        }

        WritableArray otherPossibleVenuesArray = Arguments.createArray();
        for (Venue venue : visit.getOtherPossibleVenues()) {
            otherPossibleVenuesArray.pushMap(venueJson(venue));
        }
        json.putArray("otherPossibleVenues", otherPossibleVenuesArray);

        return json;
    }

    private static WritableMap geofenceEventJson(GeofenceEvent geofenceEvent) {
        WritableMap json = Arguments.createMap();
        json.putString("id", geofenceEvent.getId());
        json.putString("name", geofenceEvent.getName());

        if (geofenceEvent.getVenue() != null) {
            Venue venue = geofenceEvent.getVenue();
            json.putString("name", venue.getName());
            json.putString("venueId", venue.getId());
            json.putMap("venue", venueJson(venue));
        }

        if (geofenceEvent.getPartnerVenueId() != null) {
            json.putString("partnerVenueId", geofenceEvent.getPartnerVenueId());
        }

        json.putMap("location", locationJson(geofenceEvent.getLat(), geofenceEvent.getLng()));
        json.putDouble("timestamp", geofenceEvent.getTimestamp() / 1000);
        return json;
    }

    private static WritableMap chainJson(Venue.VenueChain chain) {
        WritableMap json = Arguments.createMap();
        json.putString("id", chain.getId());
        json.putString("name", chain.getName());
        return json;
    }

    private static WritableArray chainsArrayJson(List<Venue.VenueChain> chains) {
        WritableArray json = Arguments.createArray();
        if (chains != null) {
            for (Venue.VenueChain chain : chains) {
                json.pushMap(chainJson(chain));
            }
        }
        return json;
    }

    private static WritableArray categoryArrayJson(List<Category> categories) {
        WritableArray json = Arguments.createArray();
        for (Category category : categories) {
            json.pushMap(categoryJson(category));
        }
        return json;
    }

    private static WritableMap categoryJson(Category category) {
        WritableMap json = Arguments.createMap();
        json.putString("id", category.getId());
        json.putString("name", category.getName());

        if (category.getPluralName() != null) {
            json.putString("pluralName", category.getPluralName());
        }

        if (category.getShortName() != null) {
            json.putString("shortName", category.getShortName());
        }

        if (category.getImage() != null) {
            json.putMap("icon", categoryIconJson(category.getImage()));
        }

        json.putBoolean("isPrimary", category.isPrimary());

        return json;
    }

    private static WritableMap categoryIconJson(Photo photo) {
        WritableMap json = Arguments.createMap();
        json.putString("prefix", photo.getPrefix());
        json.putString("suffix", photo.getSuffix());
        return json;
    }

    private static WritableArray hierarchyJson(List<Venue.VenueParent> hierarchy) {
        WritableArray json = Arguments.createArray();
        if (hierarchy != null) {
            for (Venue.VenueParent parent : hierarchy) {
                WritableMap parentJson = Arguments.createMap();
                parentJson.putString("id", parent.getId());
                parentJson.putString("name", parent.getName());
                parentJson.putArray("categories", categoryArrayJson(parent.getCategories()));
                json.pushMap(parentJson);
            }
        }
        return json;
    }

    private static WritableMap venueJson(Venue venue) {
        WritableMap json = Arguments.createMap();
        json.putString("id", venue.getId());
        json.putString("name", venue.getName());

        json.putMap("locationInformation", venueLocationJson(venue.getLocation()));

        if (venue.getPartnerVenueId() != null) {
            json.putString("partnerVenueId", venue.getPartnerVenueId());
        }

        json.putDouble("probability", venue.getProbability());

        json.putArray("chains", chainsArrayJson(venue.getVenueChains()));

        json.putArray("categories", categoryArrayJson(venue.getCategories()));

        json.putArray("hierarchy", hierarchyJson(venue.getHierarchy()));

        return json;
    }

    private static WritableMap venueLocationJson(Venue.Location venueLocation) {
        WritableMap json = Arguments.createMap();

        if (venueLocation.getAddress() != null) {
            json.putString("address", venueLocation.getAddress());
        }
        if (venueLocation.getCrossStreet() != null) {
            json.putString("crossStreet", venueLocation.getCrossStreet());
        }
        if (venueLocation.getCity() != null) {
            json.putString("city", venueLocation.getCity());
        }
        if (venueLocation.getState() != null) {
            json.putString("state", venueLocation.getState());
        }
        if (venueLocation.getPostalCode() != null) {
            json.putString("postalCode", venueLocation.getPostalCode());
        }
        if (venueLocation.getCountry() != null) {
            json.putString("country", venueLocation.getCountry());
        }
        json.putMap("location", locationJson(venueLocation.getLat(), venueLocation.getLng()));

        return json;
    }

    private static WritableMap foursquareLocationJson(FoursquareLocation foursquareLocation) {
        return locationJson(foursquareLocation.getLat(), foursquareLocation.getLng());
    }

    private static WritableMap locationJson(double lat, double lng) {
        WritableMap json = Arguments.createMap();
        json.putDouble("latitude", lat);
        json.putDouble("longitude", lng);
        return json;
    }

}
