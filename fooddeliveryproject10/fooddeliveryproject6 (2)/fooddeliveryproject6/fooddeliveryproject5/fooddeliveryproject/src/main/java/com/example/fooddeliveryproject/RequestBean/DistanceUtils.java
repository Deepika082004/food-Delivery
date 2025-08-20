//package com.example.fooddeliveryproject.RequestBean;
//
//import com.example.fooddeliveryproject.Entity.Location;
//
//public class DistanceUtils {
//    private static final int EARTH_RADIUS_KM = 6371; // Earth radius in kilometers
//
//    /**
//     * Calculates the distance between two points using the Haversine formula.
//     *
//     * @param loc1 First location
//     * @param loc2 Second location
//     * @return distance in kilometers
//     */
//    public static double calculateDistance(Location loc1, Location loc2) {
//        double latDistance = Math.toRadians(loc2.getLatitude() - loc1.getLatitude());
//        double lonDistance = Math.toRadians(loc2.getLongitude() - loc1.getLongitude());
//
//        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
//                + Math.cos(Math.toRadians(loc1.getLatitude()))
//                * Math.cos(Math.toRadians(loc2.getLatitude()))
//                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//
//        return EARTH_RADIUS_KM * c;
//    }
//}
