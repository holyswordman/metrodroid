/*
 * LaxTapTransitData.java
 *
 * Copyright 2015-2016 Michael Farrell <micolous+git@gmail.com>
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
package au.id.micolous.metrodroid.transit.lax_tap;

import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.Nullable;

import au.id.micolous.farebot.R;
import au.id.micolous.metrodroid.card.CardType;
import au.id.micolous.metrodroid.card.UnauthorizedException;
import au.id.micolous.metrodroid.card.classic.ClassicCard;
import au.id.micolous.metrodroid.transit.CardInfo;
import au.id.micolous.metrodroid.transit.TransitIdentity;
import au.id.micolous.metrodroid.transit.nextfare.NextfareTransitData;
import au.id.micolous.metrodroid.transit.nextfare.NextfareTrip;
import au.id.micolous.metrodroid.transit.nextfare.record.NextfareTransactionRecord;
import au.id.micolous.metrodroid.util.StationTableReader;

import java.util.Arrays;
import java.util.TimeZone;

/**
 * Los Angeles Transit Access Pass (LAX TAP) card.
 * https://github.com/micolous/metrodroid/wiki/Transit-Access-Pass
 */

public class LaxTapTransitData extends NextfareTransitData {

    public static final String NAME = "TAP";
    public static final String LONG_NAME = "Transit Access Pass";
    public static final Creator<LaxTapTransitData> CREATOR = new Creator<LaxTapTransitData>() {
        public LaxTapTransitData createFromParcel(Parcel parcel) {
            return new LaxTapTransitData(parcel);
        }

        public LaxTapTransitData[] newArray(int size) {
            return new LaxTapTransitData[size];
        }
    };
    static final byte[] BLOCK1 = {
            0x16, 0x18, 0x1A, 0x1B,
            0x1C, 0x1D, 0x1E, 0x1F,
            0x01, 0x01, 0x01, 0x01,
            0x01, 0x01
    };
    static final byte[] BLOCK2 = {
            0x00, 0x00, 0x00, 0x00
    };

    public static final CardInfo CARD_INFO = new CardInfo.Builder()
            .setImageId(R.drawable.laxtap_card)
            // Using the short name (TAP) may be ambiguous
            .setName(LaxTapTransitData.LONG_NAME)
            .setLocation(R.string.location_los_angeles)
            .setCardType(CardType.MifareClassic)
            .setKeysRequired()
            .setPreview()
            .build();

    static final TimeZone TIME_ZONE = TimeZone.getTimeZone("America/Los_Angeles");

    public LaxTapTransitData(Parcel parcel) {
        super(parcel, "USD");
    }

    public LaxTapTransitData(ClassicCard card) {
        super(card);
    }

    public static TransitIdentity parseTransitIdentity(ClassicCard card) {
        return NextfareTransitData.parseTransitIdentity(card, NAME);
    }

    public static boolean check(ClassicCard card) {
        try {
            byte[] block1 = card.getSector(0).getBlock(1).getData();
            if (!Arrays.equals(Arrays.copyOfRange(block1, 1, 15), BLOCK1)) {
                return false;
            }

            byte[] block2 = card.getSector(0).getBlock(2).getData();
            return Arrays.equals(Arrays.copyOfRange(block2, 0, 4), BLOCK2);
        } catch (UnauthorizedException ex) {
            // It is not possible to identify the card without a key
            return false;
        } catch (IndexOutOfBoundsException ignored) {
            // If the sector/block number is too high, it's not for us
            return false;
        }
    }

    @Override
    protected NextfareTrip newTrip() {
        return new LaxTapTrip();
    }

    @Override
    protected boolean shouldMergeJourneys(NextfareTransactionRecord tap1, NextfareTransactionRecord tap2) {
        // LAX TAP does not record tap-offs. Sometimes this merges trips that are bus -> rail
        // otherwise, but we don't need to do the complex logic in order to figure it out correctly.
        return false;
    }

    @Override
    public String getCardName() {
        return NAME;
    }

    /*
    @Override
    public Uri getMoreInfoPage() {
        return Uri.parse("https://micolous.github.io/metrodroid/laxtap");
    }
    */

    @Override
    public Uri getOnlineServicesPage() {
        return Uri.parse("https://www.taptogo.net/");
    }

    @Override
    protected TimeZone getTimezone() {
        return TIME_ZONE;
    }

    @Nullable
    public static String getNotice() {
        return StationTableReader.getNotice(LaxTapData.LAX_TAP_STR);
    }
}
