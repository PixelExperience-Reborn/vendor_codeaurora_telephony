/*
 * Copyright (c) 2023 Qualcomm Innovation Center, Inc. All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause-Clear.
 */

package org.codeaurora.ims;

import android.os.Parcel;
import android.os.Parcelable;

import org.codeaurora.ims.VosMoveInfo;
import org.codeaurora.ims.VosMoveInfo2;
import org.codeaurora.ims.VosTouchInfo;

/**
 * Parcelable object to handle VosActionInfo info
 * @hide
 */

public class VosActionInfo implements Parcelable {
    public static final VosMoveInfo INVALID_MOVEINFO = null;
    public static final VosTouchInfo INVALID_TOUCHINFO = null;
    public static final VosMoveInfo2 INVALID_MOVEINFO2 = null;

    private VosMoveInfo mVosMoveInfo;
    private VosTouchInfo mVosTouchInfo;
    private VosMoveInfo2 mVosMoveInfo2;

    private VosActionInfo(VosMoveInfo vosMoveInfo, VosTouchInfo vosTouchInfo,
            VosMoveInfo2 vosMoveInfo2) {
        mVosMoveInfo = vosMoveInfo;
        mVosTouchInfo = vosTouchInfo;
        mVosMoveInfo2 = vosMoveInfo2;
    }

    public VosActionInfo(VosMoveInfo vosMoveInfo, VosTouchInfo vosTouchInfo) {
        mVosMoveInfo = vosMoveInfo;
        mVosTouchInfo = vosTouchInfo;
    }

    public VosActionInfo(VosTouchInfo vosTouchInfo) {
        this(INVALID_MOVEINFO, vosTouchInfo, INVALID_MOVEINFO2);
    }

    public VosActionInfo(VosMoveInfo vosMoveInfo) {
        this(vosMoveInfo, INVALID_TOUCHINFO, INVALID_MOVEINFO2);
    }

    public VosActionInfo(VosMoveInfo2 vosMoveInfo2) {
        this(INVALID_MOVEINFO, INVALID_TOUCHINFO, vosMoveInfo2);
    }

    public VosActionInfo(Parcel in) {
        readFromParcel(in);
    }

    public VosMoveInfo getVosMoveInfo() {
        return mVosMoveInfo;
    }

    public VosTouchInfo getVosTouchInfo() {
        return mVosTouchInfo;
    }

    public VosMoveInfo2 getVosMoveInfo2() {
        return mVosMoveInfo2;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeParcelable(mVosMoveInfo, flag);
        dest.writeParcelable(mVosTouchInfo, flag);
        dest.writeParcelable(mVosMoveInfo2, flag);
    }

    public void readFromParcel(Parcel in) {
        mVosMoveInfo = in.readParcelable(VosMoveInfo.class.getClassLoader());
        mVosTouchInfo = in.readParcelable(VosTouchInfo.class.getClassLoader());
        mVosMoveInfo2 = in.readParcelable(VosMoveInfo2.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<VosActionInfo> CREATOR =
            new Creator<VosActionInfo>() {
        @Override
        public VosActionInfo createFromParcel(Parcel in) {
            return new VosActionInfo(in);
        }

        @Override
        public VosActionInfo[] newArray(int size) {
            return new VosActionInfo[size];
        }
    };

    public String toString() {
        return ("{VosActionInfo: " + "vosMoveInfo = " +
                mVosMoveInfo + " , vosTouchInfo = " + mVosTouchInfo +
                " , vosMoveInfo2 = " + mVosMoveInfo2 + "}");
    }
}
