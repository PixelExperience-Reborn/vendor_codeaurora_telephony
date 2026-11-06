/*
 * Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause-Clear
 */

package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class NrIcon implements Parcelable {
    private static final String TAG = "NrIcon";

    public static final int INVALID = -1;

    private int mType;  // The icon type from NrIconType.java
    private int mRxCount = INVALID;     // The number of receiving antennas

    public NrIcon(int type, int rxCount) {
        mType = type;
        mRxCount = rxCount;
    }

    public NrIcon(Parcel in) {
        mType = in.readInt();
        mRxCount = in.readInt();
    }

    public int getType() {
        return mType;
    }

    public int getRxCount() {
        return mRxCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mType);
        out.writeInt(mRxCount);
    }

    public static final Parcelable.Creator<NrIcon> CREATOR = new Parcelable.Creator() {
        public NrIcon createFromParcel(Parcel in) {
            return new NrIcon(in);
        }

        public NrIcon[] newArray(int size) {
            return new NrIcon[size];
        }
    };

    public void readFromParcel(Parcel in) {
        mType = in.readInt();
        mRxCount = in.readInt();
    }

    @Override
    public String toString() {
        return TAG + ": type = " + mType + ", Rx = " + mRxCount;
    }
}