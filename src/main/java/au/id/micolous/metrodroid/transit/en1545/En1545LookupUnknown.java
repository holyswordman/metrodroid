/*
 * En1545LookupUnknown.java
 *
 * Copyright 2018 Google
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package au.id.micolous.metrodroid.transit.en1545;

import au.id.micolous.metrodroid.transit.Station;
import au.id.micolous.metrodroid.transit.Trip;

public abstract class En1545LookupUnknown implements En1545Lookup {

    @Override
    public String getRouteName(Integer routeNumber, Integer routeVariant, Integer agency, Integer transport) {
        if (routeNumber == null)
            return null;
        String routeReadable = Integer.toString(routeNumber);
        if (routeVariant != null) {
            routeReadable += "/" + routeVariant;
        }
        return routeReadable;
    }

    @Override
    public String getAgencyName(Integer agency, boolean isShort) {
        if (agency == null || agency == 0)
            return null;
        return Integer.toString(agency);
    }

    @Override
    public Station getStation(int station, Integer agency, Integer transport) {
        if (station == 0)
            return null;
        return Station.unknown(station);
    }

    @Override
    public String getSubscriptionName(Integer agency, Integer contractTariff) {
        if (contractTariff == null)
            return null;
        return Integer.toString(contractTariff);
    }

    @Override
    public Trip.Mode getMode(Integer agency, Integer route) {
        return Trip.Mode.OTHER;
    }
}
