/*
 * Copyright (c) 2023 Qualcomm Innovation Center, Inc. All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause-Clear.
 */

package org.codeaurora.ims;

import android.os.Parcel;
import android.os.Parcelable;

import org.codeaurora.ims.Coordinate2D;

/**
 * Parcelable object to handle VosMoveInfo info
 * @hide
 */

public class VosMoveInfo2 implements Parcelable {

    private Coordinate2D mCoordinate2D;
    private int mIndex;
    private long mTimestamp;
    private int mDuration;

    public VosMoveInfo2(Coordinate2D coordinate2D, int index, long timestamp, int duration) {
        mCoordinate2D = coordinate2D;
        mIndex = index;
        mTimestamp = timestamp;
        mDuration = duration;
    }

    public VosMoveInfo2(Parcel in) {
        readFromParcel(in);
    }

    public Coordinate2D getCoordinate2D() {
        return mCoordinate2D;
    }

    public int getIndex() {
        return mIndex;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public int getDuration() {
        return mDuration;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeParcelable(mCoordinate2D, flag);
        dest.writeInt(mIndex);
        dest.writeLong(mTimestamp);
        dest.writeInt(mDuration);
    }

    public void readFromParcel(Parcel in) {
        mCoordinate2D = in.readParcelable(Coordinate2D.class.getClassLoader());
        mIndex = in.readInt();
        mTimestamp = in.readLong();
        mDuration = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<VosMoveInfo2> CREATOR =
            new Creator<VosMoveInfo2>() {
        @Override
        public VosMoveInfo2 createFromParcel(Parcel in) {
            return new VosMoveInfo2(in);
        }

        @Override
        public VosMoveInfo2[] newArray(int size) {
            return new VosMoveInfo2[size];
        }
    };

    public String toString() {
        return ("{VosMoveInfo2: " + "Coordinate2D = " +  mCoordinate2D
                + " , index = " + mIndex + " , timestamp = " + mTimestamp
                + " , duration = " + mDuration + "}");
    }
}
