/*
 * Copyright (c) 2025 Qualcomm Innovation Center, Inc. All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause-Clear
 */

package org.codeaurora.ims;

import android.os.RemoteException;
import org.codeaurora.ims.internal.IVideoCallProvider;
import android.view.Surface;
import android.util.Log;

public class VideoCallProviderManager {
    private static final String LOG_TAG = "VideoCallProviderManager";

    private QtiImsExtManager mQtiImsExtMgr;
    private volatile IVideoCallProvider mInterface;
    private int mPhoneId;
    private int mToken;

    VideoCallProviderManager(int phoneId, int token, IVideoCallProvider intf,
            QtiImsExtManager imsExtMgr) {
        mPhoneId = phoneId;
        mToken = token;
        mInterface = intf;
        mQtiImsExtMgr = imsExtMgr;
        mQtiImsExtMgr.addCleanupListener(()->{mInterface = null;});
    }

    private IVideoCallProvider getBinder() throws QtiImsException {
        IVideoCallProvider intf = mInterface;
        if (intf != null) {
            return intf;
        }
        mQtiImsExtMgr.validateInvariants(mPhoneId);
        intf = mQtiImsExtMgr.getVideoCallProvider(mPhoneId, mToken);
        if (intf == null) {
            Log.e(LOG_TAG, "mInterface is NULL");
            throw new QtiImsException("Remote Interface is NULL");
        }
        mInterface = intf;
        return intf;
    }

    /**
     * Used by client to set listener for screen share
     * in vendor. Current implementation doesn't allow
     * to set the listener as null and lower layer
     * would overwrite the previous listener if this
     * API is invoked again.
     */
    public void addListener(VideoCallProviderListenerBase listener)
            throws QtiImsException {
        mQtiImsExtMgr.validateInvariants(mPhoneId);
        if (listener == null) {
            Log.e(LOG_TAG, "listener is NULL");
            throw new QtiImsException("Listener is NULL");
        }
        try {
            getBinder().setListener(listener.getBinder());
        } catch (RemoteException e) {
            throw new QtiImsException("Remote ImsService setListener : " + e);
        }
    }

    public void removeListener(VideoCallProviderListenerBase listener) throws QtiImsException {
        mQtiImsExtMgr.validateInvariants(mPhoneId);
        if (listener == null) {
            Log.e(LOG_TAG, "listener is NULL");
            throw new QtiImsException("Listener is NULL");
        }
        try {
            getBinder().removeListener(listener.getBinder());
        } catch (RemoteException e) {
            throw new QtiImsException("Remote ImsService removeListener : " + e);
        }
    }

    public void setPreviewSurface(Surface surface) throws QtiImsException {
        mQtiImsExtMgr.validateInvariants(mPhoneId);
        try {
            getBinder().setPreviewSurface(surface);
        } catch (RemoteException e) {
            throw new QtiImsException("Remote ImsService setPreviewSurface : " + e);
        }
    }

    public void setDisplaySurface(Surface surface) throws QtiImsException {
        mQtiImsExtMgr.validateInvariants(mPhoneId);
        try {
            getBinder().setDisplaySurface(surface);
        } catch (RemoteException e) {
            throw new QtiImsException("Remote ImsService setDisplaySurface : " + e);
        }
    }

    public void setCamera(String cameraId) throws QtiImsException {
        mQtiImsExtMgr.validateInvariants(mPhoneId);
        try {
            getBinder().setCamera(cameraId);
            getBinder().requestCameraCapabilities();
        } catch (RemoteException e) {
            throw new QtiImsException("Remote ImsService setCamera : " + e);
        }
    }

    public void requestCameraCapabilities() throws QtiImsException {
        mQtiImsExtMgr.validateInvariants(mPhoneId);
        try {
            getBinder().requestCameraCapabilities();
        } catch (RemoteException e) {
            throw new QtiImsException("Remote ImsService requestCameraCapabilities : " + e);
        }
    }
}
