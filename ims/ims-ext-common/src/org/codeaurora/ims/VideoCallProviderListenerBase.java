/*
 * Copyright (c) 2025 Qualcomm Innovation Center, Inc. All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause-Clear
 */

package org.codeaurora.ims;

import android.os.RemoteException;
import android.telecom.VideoProfile.CameraCapabilities;

import org.codeaurora.ims.internal.IVideoCallProviderListener;
import org.codeaurora.ims.utils.QtiImsExtUtils;

import java.util.concurrent.Executor;

public abstract class VideoCallProviderListenerBase {

    private final class VideoCallProviderListener extends IVideoCallProviderListener.Stub {
        @Override
        public void changeCameraCapabilities(CameraCapabilities cc) throws RemoteException {
            QtiImsExtUtils.executeMethodAsync(() ->
                VideoCallProviderListenerBase.this.onChangeCameraCapabilities(cc),
                "changeCameraCapabilities", mExecutor);
        }

        @Override
        public void changePeerDimensions(int width, int height) throws RemoteException {
            QtiImsExtUtils.executeMethodAsync(() ->
                VideoCallProviderListenerBase.this.onChangePeerDimensions(width, height),
                "changePeerDimensions", mExecutor);
        }

        @Override
        public void handleCallSessionEvent(int event) throws RemoteException {
            QtiImsExtUtils.executeMethodAsync(() ->
                VideoCallProviderListenerBase.this.onHandleCallSessionEvent(event),
                "handleCallSessionEvent", mExecutor);
        }
    }

    private VideoCallProviderListener mListener;
    private Executor mExecutor;

    public IVideoCallProviderListener getBinder() {
        if (mListener == null) {
            mListener = new VideoCallProviderListener();
        }
        return mListener;
    }

    public VideoCallProviderListenerBase(Executor executor) {
        mExecutor = executor;
    }

    protected void onChangeCameraCapabilities(CameraCapabilities cc) {
        // no-op
    }

    protected void onChangePeerDimensions(int width, int height) {
        // no-op
    }

    protected void onHandleCallSessionEvent(int event) {
        // no-op
    }
}
