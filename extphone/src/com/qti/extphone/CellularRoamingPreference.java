/*
 * Copyright (c) 2023-2024 Qualcomm Innovation Center, Inc. All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause-Clear
 */

package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class CellularRoamingPreference implements Parcelable {

    private static final String TAG = "CellularRoamingPreference";

    public static final int INVALID = -1;   // Cellular roaming invalid
    public static final int DISABLED = 0;   // Cellular roaming disabled
    public static final int ENABLED = 1;    // Cellular roaming enabled

    private int mInternationalPref = INVALID;
    private int mDomesticPref = INVALID;

    public CellularRoamingPreference(int internationalPref, int domesticPref) {
        mInternationalPref = internationalPref;
        mDomesticPref = domesticPref;
    }

    public CellularRoamingPreference(Parcel in) {
        mInternationalPref = in.readInt();
        mDomesticPref = in.readInt();
    }

    public int getInternationalCellularRoamingPref() {
        return mInternationalPref;
    }

    public int getDomesticCellularRoamingPref() {
        return mDomesticPref;
    }

    public String convertCellularRoamingModeToString(int mode) {
        switch (mode) {
            case DISABLED:
                return "DISABLED";
            case ENABLED:
                return "ENABLED";
            default:
                return "INVALID";
        }
    }

    public boolean isValid() {
        return (mInternationalPref != INVALID && mDomesticPref != INVALID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mInternationalPref);
        out.writeInt(mDomesticPref);
    }

    public static final Parcelable.Creator<CellularRoamingPreference> CREATOR =
            new Parcelable.Creator() {
        @Override
        public CellularRoamingPreference createFromParcel(Parcel in) {
            return new CellularRoamingPreference(in);
        }

        @Override
        public CellularRoamingPreference[] newArray(int size) {
            return new CellularRoamingPreference[size];
        }
    };

    @Override
    public String toString() {
        return TAG + " internationalPref = " +
                convertCellularRoamingModeToString(mInternationalPref) + ", domesticPref = " +
                convertCellularRoamingModeToString(mDomesticPref);
    }
}