/*
 * ErgTrip.java
 *
 * Copyright 2015-2018 Michael Farrell <micolous+git@gmail.com>
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
package au.id.micolous.metrodroid.transit.erg;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.GregorianCalendar;

import au.id.micolous.metrodroid.transit.TransitCurrency;
import au.id.micolous.metrodroid.transit.Trip;
import au.id.micolous.metrodroid.transit.erg.record.ErgPurseRecord;

/**
 * Represents a trip on an ERG MIFARE Classic card.
 */

public class ErgTrip extends Trip {
    public static final Parcelable.Creator<ErgTrip> CREATOR = new Parcelable.Creator<ErgTrip>() {

        public ErgTrip createFromParcel(Parcel in) {
            return new ErgTrip(in);
        }

        public ErgTrip[] newArray(int size) {
            return new ErgTrip[size];
        }
    };

    protected GregorianCalendar mEpoch;
    protected ErgPurseRecord mPurse;
    @NonNull
    protected String mCurrency;

    public ErgTrip(ErgPurseRecord purse, GregorianCalendar epoch, @NonNull String currency) {
        mPurse = purse;
        mEpoch = epoch;
        mCurrency = currency;
    }

    public ErgTrip(Parcel parcel) {
        mPurse = new ErgPurseRecord(parcel);
        mEpoch = new GregorianCalendar();
        mEpoch.setTimeInMillis(parcel.readLong());
        mCurrency = parcel.readString();
    }

    // Implemented functionality.
    @Override
    public Calendar getStartTimestamp() {
        GregorianCalendar ts = new GregorianCalendar();
        ts.setTimeInMillis(mEpoch.getTimeInMillis());
        ts.add(Calendar.DATE, mPurse.getDay());
        ts.add(Calendar.MINUTE, mPurse.getMinute());

        return ts;
    }

    @Nullable
    @Override
    public TransitCurrency getFare() {
        int o = mPurse.getTransactionValue();
        if (mPurse.getIsCredit()) {
            o *= -1;
        }

        return TransitCurrency.AUD(o);
    }

    @Override
    public Mode getMode() {
        return Mode.OTHER;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        mPurse.writeToParcel(parcel, i);
        parcel.writeLong(mEpoch.getTimeInMillis());
        parcel.writeString(mCurrency);
    }


}
